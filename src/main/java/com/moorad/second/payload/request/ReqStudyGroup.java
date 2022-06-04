package com.moorad.second.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqStudyGroup {
    private String name;
    private String description;
    private String requirement;
    private UUID interestId;
    private String contacts;
}
