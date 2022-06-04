package com.moorad.second.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDefault {
    private String message;
    private boolean success;
    private Object data;
}
