package com.moorad.second.repository;

import com.moorad.second.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InterestRepository extends JpaRepository<Interest, UUID> {
    Interest findByName(String name);
}
