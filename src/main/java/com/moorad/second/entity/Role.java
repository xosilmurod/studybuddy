package com.moorad.second.entity;

import com.moorad.second.entity.defaults.DefaultFields;
import com.moorad.second.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
@EqualsAndHashCode(callSuper = true)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends DefaultFields {
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
