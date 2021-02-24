package com.company.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "gender")
public class Gender {

    @Id
    private char code;

    @Column(name = "name",nullable = true)
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
