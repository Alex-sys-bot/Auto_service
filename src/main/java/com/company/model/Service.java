package com.company.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Title", nullable = false)
    private String name;

    @Column(name = "Cost", nullable = false)
    private double cost;

    @Column(name = "DurationInSeconds", nullable = false)
    private int durationInSeconds;
    private String description;

    @Column(name = "Discount", nullable = false)
    private double discount;

    @Column(name = "MainImagePath", nullable = false)
    private String imagePath;

    @OneToMany(mappedBy = "services",fetch = FetchType.EAGER)
    private Set<ClientService> clientServices;

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", durationInSeconds=" + durationInSeconds +
                ", description='" + description + '\'' +
                ", discount=" + discount +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
