package com.tpt.tpt_ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
}
