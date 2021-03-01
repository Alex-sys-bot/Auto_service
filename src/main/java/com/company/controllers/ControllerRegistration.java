package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.model.Client;
import com.company.model.User;
import com.company.service.ServiceDaoImpClient;
import com.company.service.ServiceDaoImplUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerRegistration {

    private final SessionFactory factory;
    public ControllerRegistration(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    ObservableList<String> listRoles = FXCollections.observableArrayList("admin", "user");
    List<User> listUsers = new ArrayList<>();

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPass;

    @FXML
    private ComboBox<String> comboRoles;

    @FXML
    private Button buttonPrew;

    @FXML
    private Label labelStatus;

    @FXML
    private Button buttonClose;


    public void initialize(){
        comboRoles.setItems(listRoles);
        initUser();
    }

    public void initUser(){
        DaoImpl<User, Integer> daoClient = new ServiceDaoImplUser(factory);
        listUsers.addAll(daoClient.readAll());
    }


    @FXML
    void buttonAddUser(ActionEvent event) {
        String login = "";
        DaoImpl<User, Integer> daoUser = new ServiceDaoImplUser(factory);
        User user = new User();

//        labelStatus.setText("Пользователь с таким логином уже существует");
//        labelStatus.setText("Пользователь" +  " " + txtLogin.getText() + " " + "создан");

        for (User user1: listUsers) {
            if (user1.getLogin().equals(txtLogin.getText())){
                login = user1.getLogin();
            }
        }

        if (!txtLogin.getText().equals(login)) {
            user.setLogin(txtLogin.getText());
            user.setPassword(txtPass.getText());
            user.setRole(comboRoles.getValue());
            daoUser.create(user);
            labelStatus.setText("Пользователь" +  " " + txtLogin.getText() + " " + "создан");
        } else labelStatus.setText("Пользователь с таким логином уже существует");
    }

    public void ButtonBack() throws IOException {
        buttonClose.getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/view/windowAutorisation.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Auto service");
        stage.setScene(new Scene(parent));
        stage.show();
    }

}
