package com.tpt.tpt_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        columnNames = "email"
                )
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false, name = "username", length = 255)
    @NotBlank(message = "Username cannot be blank")
    @Min(value = 8, message = "Minlength of username is 8")
    @Max(value = 255, message = "Maxlength of username is 8")
    private String userName;

    @Column(unique = true, nullable = false, name = "password", length = 255)
    @NotBlank(message = "Password cannot be blank")
    @Min(value = 8, message = "Minlength of password is 8")
    @Max(value = 255, message = "Maxlength of password is 8")
    private String password;


    @Column(unique = true, nullable = false, name = "email", length = 255)
    @Email(message = "Email is invalid")
    @Max(message = "Maxlength of password is 255", value = 255)
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
                fetch = FetchType.EAGER
    )
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            name = "user_roles"
    )
    private Set<Role> roles = new HashSet<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"),
            name = "user_addresses"
    )
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Product> products = new HashSet<>();

    public User(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(max = 50) @Email String email, String encode) {
    }
}
