package com.seller.panel.dto;

import lombok.Data;

@Data
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String companyName;
    private String companyCode;

}
