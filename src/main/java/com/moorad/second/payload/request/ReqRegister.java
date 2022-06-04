package com.moorad.second.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqRegister {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
