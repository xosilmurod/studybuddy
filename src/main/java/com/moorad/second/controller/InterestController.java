package com.moorad.second.controller;


import com.moorad.second.entity.Interest;
import com.moorad.second.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/interest")
public class InterestController {
    @Autowired
    InterestRepository interestRepository;

    @GetMapping("/get-all")
    public HttpEntity<?> getInterests(){
        List<Interest> interests = interestRepository.findAll();
        return ResponseEntity.ok(interests);
    }
}
