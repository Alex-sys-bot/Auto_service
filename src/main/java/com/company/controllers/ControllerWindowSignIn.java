package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.model.User;
import com.company.service.ServiceDaoImplUser;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerWindowSignIn {

    List<User> listUsers = new ArrayList<>();

    public static String role;

    private final SessionFactory factory;
    public ControllerWindowSignIn(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private Label txtStatus;

    @FXML
    private Button fxButtonSignIn;



    public void initUser(){
        DaoImpl<User, Integer> daoUser = new ServiceDaoImplUser(factory);
        listUsers.addAll(daoUser.readAll());
    }


    public void buttonSignIn() throws IOException {
        initUser();
        for (User user : listUsers) {
            if (user.getLogin().equals(txtLogin.getText()) && user.getPassword().equals(txtPassword.getText())) {
                    role = user.getLogin();
                    fxButtonSignIn.getScene().getWindow().hide();
                    Parent parent = FXMLLoader.load(getClass().getResource("/view/mainWindow.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Auto service");
                    stage.setScene(new Scene(parent));
                    stage.show();
                }
            }
        }
    }

