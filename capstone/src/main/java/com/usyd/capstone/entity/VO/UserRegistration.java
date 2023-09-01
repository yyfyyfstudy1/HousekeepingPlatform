package com.usyd.capstone.entity.VO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRegistration {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

}
