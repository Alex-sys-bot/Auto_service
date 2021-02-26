package com.company.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "Patronymic", nullable = false)
    private String patronymic;

    @Column(name = "Birthday")
    private Date birthday;

    @Column(name = "RegistrationDate", nullable = false)
    private Date registrationDate;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "PhotoPath")
    private String photoPath;

    @Column(name = "Phone")
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GenderCode")
    private Gender gender;

    @OneToMany(mappedBy = "clients",fetch = FetchType.EAGER)
    private Set<ClientService> clientServices;


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", registrationDate=" + registrationDate +
                ", email='" + email + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                '}';
    }
}
