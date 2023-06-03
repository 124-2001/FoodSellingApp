package com.example.foodsellingapp.model.entity;

import com.example.foodsellingapp.model.eenum.StatusAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.*;


@Table
@Entity
@Getter
@Setter
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number", length = 10, nullable = false)
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must have 10 digits")
    private String phone;

    private int isEnable;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();
    public User() {
    }

    public User(String username, String password, String email, String address, String firstName, String lastName,String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

}
