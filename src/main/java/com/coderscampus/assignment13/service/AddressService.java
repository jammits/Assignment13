package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    public Address findById(Long userId) {
        Optional<Address> addressOpt = addressRepository.findById(userId);
        return addressOpt.orElse(new Address());
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }
}
