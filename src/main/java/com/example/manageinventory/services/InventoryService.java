package com.example.manageinventory.services;

import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class InventoryService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public ProductRepository productRepository;

    public UserRepository userRepository;




}
