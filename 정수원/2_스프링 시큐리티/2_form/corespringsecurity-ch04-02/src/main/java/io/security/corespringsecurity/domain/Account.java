package io.security.corespringsecurity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long Id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String age;
    private String role;
}
