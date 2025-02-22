package com.tpt.tpt_ecom.service;

import com.tpt.tpt_ecom.dto.AddressDTO;

import java.util.List;
import java.util.Set;

public interface AddressService {
    AddressDTO addNewAddress(AddressDTO addressDTO);
    Set<AddressDTO> getAddressesByUser();
    AddressDTO getSpecificAddressByUser(Long addressId);
    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);
    String deleteAddress(Long addressId);
}
