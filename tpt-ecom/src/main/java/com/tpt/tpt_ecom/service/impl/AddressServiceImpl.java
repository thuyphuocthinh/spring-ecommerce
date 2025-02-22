package com.tpt.tpt_ecom.service.impl;

import com.tpt.tpt_ecom.dto.AddressDTO;
import com.tpt.tpt_ecom.exceptions.APIException;
import com.tpt.tpt_ecom.exceptions.ResourceNotFoundException;
import com.tpt.tpt_ecom.model.Address;
import com.tpt.tpt_ecom.model.User;
import com.tpt.tpt_ecom.repository.AddressRepository;
import com.tpt.tpt_ecom.repository.UserRepository;
import com.tpt.tpt_ecom.service.AddressService;
import com.tpt.tpt_ecom.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final AuthUtil authUtil;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, ModelMapper modelMapper, AuthUtil authUtil) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }

    @Override
    public AddressDTO addNewAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);

        User user = this.authUtil.loggedInUser();

        Set<Address> addresses = user.getAddresses();
        addresses.add(address);
        user.setAddresses(addresses);

        Set<User> users = new HashSet<>();
        users.add(user);
        address.setUsers(users);

        userRepository.save(user);
        addressRepository.save(address);

        return addressDTO;
    }

    @Override
    public Set<AddressDTO>  getAddressesByUser() {
        User user = this.authUtil.loggedInUser();

        Set<Address> addresses = user.getAddresses();

        if(addresses.isEmpty()) {
            throw new APIException("No address found");
        }

        Set<AddressDTO> addressDTOS = new HashSet<>();

        addresses.forEach(a -> {
            AddressDTO addressDTO = modelMapper.map(a, AddressDTO.class);
            addressDTOS.add(addressDTO);
        });

        return addressDTOS;
    }

    @Override
    public AddressDTO getSpecificAddressByUser(Long addressId) {
        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("No addess found", "Address id", addressId));

        return this.modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("No addess found", "Address id", addressId));

        Address updatedAddress = modelMapper.map(addressDTO, Address.class);
        updatedAddress.setAddressId(addressId);

        User user = this.authUtil.loggedInUser();
        Set<Address> addresses = user.getAddresses();
        for(Address add : addresses) {
            if(Objects.equals(add.getAddressId(), addressId)) {
                add = updatedAddress;
                break;
            }
        }
        user.setAddresses(addresses);

        this.addressRepository.save(updatedAddress);
        this.userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("No addess found", "Address id", addressId));

        this.addressRepository.delete(address);

        return "Success";
    }
}
