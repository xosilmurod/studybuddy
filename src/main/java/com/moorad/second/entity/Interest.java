package com.moorad.second.entity;

import com.moorad.second.entity.defaults.DefaultFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@EqualsAndHashCode(callSuper = true)

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Interest extends DefaultFields {
    private String name;
}
