package com.example.manageinventory.controllers;

import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.constants.InventoryConstants;
import com.example.manageinventory.repositories.LocationRepository;
import com.example.manageinventory.services.LocationService;
import com.example.manageinventory.view_models.LocationViewModel;
import com.example.manageinventory.view_models.ProductViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.Positive;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = APIConstants.Location.LOCATION_ROOT)
public class LocationController {
    @Autowired
    private LocationService locationService;

    /**
     * //Get list of locations in the inventory
     * @return
     */
    @GetMapping
    public ResponseEntity listOfLocations() {
        try {
            return this.locationService.getListOfLocations();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * View Location details
     * @param id
     * @return
     */

    //Get details of a Location
    @GetMapping(path = APIConstants.Product.PRODUCT_GET_UPDATE_DELETE)
    public ResponseEntity getLocationById(
            @PathVariable int id) {
        try {
            return this.locationService.getLocationById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Create/register a new Location.
     * @param locationViewModel
     * @return
     */

    @PostMapping
    public ResponseEntity createLocation(@RequestBody final LocationViewModel locationViewModel) {
        try {
            return this.locationService.createNewLocation(locationViewModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    /**
     * Update location details
     * @param locationViewModel
     * @param id
     * @return
     */

    @PutMapping(path = APIConstants.Location.LOCATION_GET_UPDATE_DELETE)
    public ResponseEntity updateLocation(@RequestBody final LocationViewModel locationViewModel,
                                        @PathVariable int id) {
        try{
            return this.locationService.updateLocationDetails(id, locationViewModel);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    /**
     * API to soft delete Location.
     * @param id
     * @return
     */
    @DeleteMapping(path = APIConstants.Location.LOCATION_GET_UPDATE_DELETE)
    public ResponseEntity deleteLocation(@PathVariable int id) {
        try{
            return this.locationService.updateLocationMarkAsDelete(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get products residing within a given location
     * @return
     */
    @GetMapping(path = APIConstants.Location.LOCATION_PRODUCTS)
    public ResponseEntity getAllProductsForLocation(@PathVariable int id){
        try{
            return this.locationService.getProductsForLocation(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get Indents residing within a given location
     * @return
     */
    @GetMapping(path = APIConstants.Location.LOCATION_INDENTS)
    public ResponseEntity getAllIndentsForLocation(){
        return null;
    }

    /**
     * Place products into a new location
     * @param productId
     * @param quantity
     * @return
     */

    @PostMapping(path = APIConstants.Location.LOCATION_PLACE_PRODUCTS)
    public ResponseEntity placeProductsInLocation(@RequestParam int productId, @RequestParam int quantity){
        return null;
    }

    /**
     * TODO
     * Remove products from a location
     * @param productId
     * @param quantity
     * @return
     */

    @PostMapping(path = APIConstants.Location.LOCATION_REMOVE_PRODUCTS)
    public ResponseEntity removeProductsFromLocation(@RequestParam int productId, @RequestParam int quantity){
        return null;
    }

}
