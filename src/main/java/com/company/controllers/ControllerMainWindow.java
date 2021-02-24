package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.model.Client;
import com.company.model.ClientService;
import com.company.service.ServiceDaoImpClient;
import com.company.service.ServiceDaoImpClientService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;
import java.util.Date;

public class ControllerMainWindow {

    ObservableList<Client> listClients = FXCollections.observableArrayList();
    ObservableList<Integer> listQuantityRows = FXCollections.observableArrayList(10,50,200);
    ObservableList<ClientService> listClientService = FXCollections.observableArrayList();

    private int maxClientSize;

    private final SessionFactory factory;

    public ControllerMainWindow() {
        factory = new Configuration().configure().buildSessionFactory();
    }

    @FXML
    private Label labelID;

    @FXML
    private TableView<Client> tableClients;

    @FXML
    private TableColumn<Client, Integer> columnID;

    @FXML
    private TableColumn<Client, String> columnLastName;

    @FXML
    private TableColumn<Client, String> columnFirstName;

    @FXML
    private TableColumn<Client, String> columnPatronymic;

    @FXML
    private TableColumn<Client, Date> columnBirthday;

    @FXML
    private TableColumn<Client, String> columnPhone;

    @FXML
    private TableColumn<Client, String> columnEmail;

    @FXML
    private TableColumn<Client, Date> columnDateReg;

    @FXML
    private TableColumn<Client, Date> columnLastDate;

    @FXML
    private TableColumn<Client, Integer> columnCountDate;

    @FXML
    private TableColumn<Client, String> columnTegs;

    @FXML
    private Label labelSizeListClients;

    @FXML
    private ComboBox<Integer> quantityRows;


    public void initialize(){
        quantityRows.setItems(listQuantityRows);

        initClients();
        maxClientSize = listClients.size();
        labelSizeListClients.setText(listClients.size() + "/" + maxClientSize);

        tableClients.setItems(listClients);
        columnID.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        columnFirstName.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getFirstName()));
        columnLastName.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getLastName()));
        columnPatronymic.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPatronymic()));
        columnBirthday.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getBirthday()));
        columnPhone.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPhone()));
        columnEmail.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getEmail()));
        columnDateReg.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getRegistrationDate()));
        columnCountDate.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getClientServices().size()));

        tableClients.getSelectionModel().selectedItemProperty().addListener((observableValue, client, t1) -> showPersonDetail(t1));
    }

    public void showPersonDetail(Client client){
    }

    public void initClients(){
        DaoImpl<Client, Integer> daoClients = new ServiceDaoImpClient(factory);
        listClients.addAll(daoClients.readAll());
        factory.close();
    }

    public void cutRows(){
        for (int i = 0; i < listClients.size(); i++) {
            listClients.remove(i--);
        }

        initClients();

        for (int i = quantityRows.getValue(); i < listClients.size(); i++) {
            listClients.remove(i--);
        }

    }

    public void buttonRegNewClient() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/registrationNewClient.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Registration new client");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.showAndWait();
    }



    public void buttonQuantityRows(){
        cutRows();
        labelSizeListClients.setText(listClients.size() + "/" + maxClientSize);
    }

    public void buttonAllRows(){
        for (int i = 0; i < listClients.size(); i++) {
            listClients.remove(i--);
        }
        initClients();
        labelSizeListClients.setText(listClients.size() + "/" + maxClientSize);
    }

    public void buttonDelete(){
        DaoImpl<Client, Integer> daoClient = new ServiceDaoImpClient(factory);
        Client client = new Client();
        client.setId(tableClients.getSelectionModel().getSelectedItem().getId());
        daoClient.delete(client);

        System.out.println(tableClients.getSelectionModel().getSelectedItem().getId());
        cutRows();
        labelSizeListClients.setText(listClients.size() + "/" + maxClientSize);
    }
}