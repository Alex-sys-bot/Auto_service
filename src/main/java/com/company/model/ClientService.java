package com.company.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ClientService")
public class ClientService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientID")
    private Client clients;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ServiceID")
    private Service services;

    @Column(name = "StartTime", nullable = false)
    private Date startTime;

    @Column(name = "Comment", nullable = false)
    private String comment;

    @Override
    public String toString() {
        return "ClientService{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", comment='" + comment + '\'' +
                '}';
    }
}
