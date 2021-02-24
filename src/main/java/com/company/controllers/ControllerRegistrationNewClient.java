package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.dao.DaoImplGender;
import com.company.model.Client;
import com.company.model.Gender;
import com.company.service.ServiceDaoImpClient;
import com.company.service.ServiceDaoImplGender;
import com.sun.javafx.binding.StringFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllerRegistrationNewClient {

    private final SessionFactory factory;

    public ControllerRegistrationNewClient() {
        factory = new Configuration().configure().buildSessionFactory();
    }
    ObservableList<Gender> genders = FXCollections.observableArrayList();


    @FXML
    private DatePicker dateBirthday;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtPatronymic;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtDateReg;

    @FXML
    private TextField txtPhotoPath;

    @FXML
    private ComboBox<Gender> listGenders;

    @FXML
    private Label labelStatus;

    public void initialize() throws ParseException {
        DaoImplGender<Gender> daoGender = new ServiceDaoImplGender(factory);
        genders.addAll(daoGender.selectAll());
        listGenders.setItems(genders);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        txtDateReg.setText(format.format(new Date()));

    }


    @FXML
    void buttonAddClient(ActionEvent event) throws ParseException {
        DaoImpl<Client, Integer> daoClient = new ServiceDaoImpClient(factory);
        Client client = new Client();

        client.setFirstName(txtFirstName.getText());
        client.setLastName(txtLastName.getText());
        client.setPatronymic(txtPatronymic.getText());
        client.setEmail(txtEmail.getText());
        client.setPhone(txtPhone.getText());
        client.setPhotoPath(txtPhotoPath.getText());
        client.setGender(listGenders.getValue());

//        Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = format.parse(String.valueOf(dateBirthday.getValue()));
        client.setRegistrationDate(new Date());
        client.setBirthday(birthday);

        daoClient.create(client);

        labelStatus.setTextFill(Color.GREEN);
        labelStatus.setText("Пользователь добавлен:)");
    }

}
