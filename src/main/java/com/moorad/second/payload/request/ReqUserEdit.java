package com.moorad.second.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUserEdit {
    private String firstName;
    private String lastName;
    private String email;
    private String previousPassword;
    private String newPassword;
}
