package com.example.manageinventory.services;

import com.example.manageinventory.models.Location;
import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.models.ProductStatus;
import com.example.manageinventory.repositories.LocationRepository;
import com.example.manageinventory.view_models.LocationViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LocationService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Autowired
    private LocationRepository locationRepository;

    public ResponseEntity getListOfLocations(){
        return ResponseEntity.status(HttpStatus.OK).body(this.locationRepository.findAll());
    }

    public ResponseEntity getLocationById(int id) {
        return null;
    }

    public ResponseEntity createNewLocation(LocationViewModel locationViewModel) {

            Location location = new Location();

            BeanUtils.copyProperties(locationViewModel, location, "id","productList", "" );
            location.setAvailable(true);
            locationRepository.saveAndFlush(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    public ResponseEntity updateLocationDetails(int id, LocationViewModel locationViewModel) {
        return null;
    }

    public ResponseEntity updateLocationMarkAsDelete(int id) {
        return null;
    }
}
