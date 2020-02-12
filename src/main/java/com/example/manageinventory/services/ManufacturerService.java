package com.example.manageinventory.services;

import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.view_models.ManufacturerViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public ResponseEntity getListOfManufacturers(){
        return ResponseEntity.status(HttpStatus.OK).body(this.manufacturerRepository.findAll());
    }

    public ResponseEntity getManufacturerById(int id) {
        Manufacturer manufacturer = this.manufacturerRepository.findManufacturerById(id);

        if(manufacturer == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found",id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(manufacturer);
    }

    public ResponseEntity createNewManufacturer(ManufacturerViewModel manufacturerViewModel) {

            Manufacturer manufacturer = new Manufacturer();
            BeanUtils.copyProperties(manufacturerViewModel, manufacturer, "id","productList", "available");
            manufacturer.setAvailable(true);
            manufacturerRepository.saveAndFlush(manufacturer);
            return ResponseEntity.status(HttpStatus.CREATED).body(manufacturer);
    }

    public ResponseEntity updateManufacturerDetails(int id, ManufacturerViewModel manufacturerViewModel) {

        Manufacturer manufacturer = this.manufacturerRepository.findManufacturerById(id);
        if(manufacturer == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found",id));
        }

        BeanUtils.copyProperties(manufacturerViewModel, manufacturer, "id","productList");
        manufacturerRepository.saveAndFlush(manufacturer);
        return ResponseEntity.status(HttpStatus.OK).body(manufacturer);
    }

    public ResponseEntity updateManufacturerMarkAsUnavailable(int id) {
        Manufacturer manufacturer = this.manufacturerRepository.findManufacturerById(id);
        if(manufacturer == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found",id));
        }
        manufacturer.setAvailable(false);
        manufacturerRepository.saveAndFlush(manufacturer);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Manufacturer %d has been marked as Not Available", manufacturer.getId()));
    }

    public ManufacturerViewModel mapToManufacturerView(Manufacturer manufacturer){
        ManufacturerViewModel manufacturerViewModel = new ManufacturerViewModel();

        manufacturerViewModel.setAddress1(manufacturer.getAddress1());
        manufacturerViewModel.setAddress2(manufacturer.getAddress2());
        manufacturerViewModel.setAddress3(manufacturer.getAddress3());
        manufacturerViewModel.setId(manufacturer.getId());
        manufacturerViewModel.setAvailable(manufacturer.getAvailable());
        manufacturerViewModel.setName(manufacturer.getName());

        for(Product product: manufacturer.getProductList()){
            manufacturerViewModel.getProductList().add(product.getId());
        }
        return manufacturerViewModel;
    }
}
