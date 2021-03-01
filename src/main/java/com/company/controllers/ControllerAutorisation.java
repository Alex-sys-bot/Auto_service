package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.model.Gender;
import com.company.model.User;
import com.company.service.ServiceDaoImplUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class ControllerAutorisation {

    private final SessionFactory factory;
    public static String role;

    public ControllerAutorisation(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    ObservableList<User> listUsers = FXCollections.observableArrayList();

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPass;

    @FXML
    private Label labelStatus;

    @FXML
    private Button buttonCloseWindow;

    @FXML
    private Button buttonCloseRegistration;

    public void initialize(){
        initUsers();
    }

    public void initUsers(){
        DaoImpl<User, Integer> daoUser = new ServiceDaoImplUser(factory);
        listUsers.addAll(daoUser.readAll());
    }

    @FXML
    void buttonInput(ActionEvent event) throws IOException {
        for (User user: listUsers) {

            if (user.getLogin().equals(txtLogin.getText()) && user.getPassword().equals(txtPass.getText())){
                buttonCloseWindow.getScene().getWindow().hide();
                labelStatus.setText("Вход...");
                role = user.getRole();
                Parent parent = FXMLLoader.load(getClass().getResource("/view/mainWindow.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Auto service");
                stage.setScene(new Scene(parent));
                stage.show();
            } else labelStatus.setText("Неправильный логин или пароль");
        }
    }

    @FXML
    void buttonRegistration(ActionEvent event) throws IOException {
        buttonCloseRegistration.getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/view/windowRegistration.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Auto service");
        stage.setScene(new Scene(parent));
        stage.show();
    }

}
