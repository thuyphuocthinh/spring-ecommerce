package com.tpt.tpt_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(unique = true, nullable = false, length = 255)
    @Min(message = "Minlength of street is 1", value = 1)
    @Max(message = "Maxlength of street is 255", value = 255)
    private String street;

    @Column(unique = true, nullable = false, length = 255)
    @Min(message = "Minlength of city is 1", value = 1)
    @Max(message = "Maxlength of city is 255", value = 255)
    private String city;

    @Column(unique = true, nullable = false, length = 255)
    @Min(message = "Minlength of state is 1", value = 1)
    @Max(message = "Maxlength of state is 255", value = 255)
    private String state;

    @Column(unique = true, nullable = false, length = 255)
    @Min(message = "Minlength of zip is 1", value = 1)
    @Max(message = "Maxlength of zip is 255", value = 255)
    private String zip;

    @Column(unique = true, nullable = false, length = 255)
    @Min(message = "Minlength of country is 1", value = 1)
    @Max(message = "Maxlength of country is 255", value = 255)
    private String country;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private Set<User> users = new HashSet<>();
}
