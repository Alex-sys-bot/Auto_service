package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.dao.DaoImplGender;
import com.company.model.Client;
import com.company.model.ClientService;
import com.company.model.Gender;
import com.company.service.ServiceDaoImpClient;
import com.company.service.ServiceDaoImpClientService;
import com.company.service.ServiceDaoImplGender;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

public class ControllerMainWindow {

    ObservableList<Client> listClients = FXCollections.observableArrayList();
    ObservableList<String> listCriteria = FXCollections.observableArrayList("ФИО", "mail", "номер");
    ObservableList<Character> listGender = FXCollections.observableArrayList('м','ж');
    private int maxClientSize;
    private int totalPage;
    private final SessionFactory factory;

    public ControllerMainWindow() {
        factory = new Configuration().configure().buildSessionFactory();
    }

    @FXML
    private ComboBox<String> comboCriteria;

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
    private TableColumn<Client, String> columnLastDate;

    @FXML
    private TableColumn<Client, Integer> columnCountDate;

    @FXML
    private TableColumn<Client, String> columnTegs;

    @FXML
    private Pagination pagination;

    @FXML
    private Label labelSizeListClients;

    @FXML
    private ComboBox<Integer> quantityRows;

    @FXML TextField txtSearch;

    @FXML Button buttonDeleteFX;

    @FXML Button buttonRegNewClientFX;

    @FXML
    private ComboBox<Character> comboGender;


    public void initialize(){
        if (ControllerAutorisation.role.equals("user")){
            buttonDeleteFX.setVisible(false);
            buttonRegNewClientFX.setVisible(false);
        }

        comboCriteria.setItems(listCriteria);

        initClients();
        maxClientSize = listClients.size();


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
        columnLastDate.setCellValueFactory(c -> {
            Set<ClientService> clientServices = c.getValue().getClientServices();
            String str = "";
            if (clientServices.size() != 0) {
                Date date = clientServices.stream().max(
                        Comparator.comparing(ClientService::getStartTime)).get().getStartTime();
                new SimpleDateFormat("yyyy-MM-dd").format(date);
                return new SimpleObjectProperty<>(str = date.toString());
            }
                return new SimpleObjectProperty<>("");
        });

        selectFromAll();
        pages();
        searchByGender();
    }

    public void selectFromAll(){
        txtSearch.textProperty().addListener((observableValue, s, t1) -> {
            FilteredList<Client> filteredList = new FilteredList<>(listClients, c -> {
                if (t1 == null || t1.isEmpty()){
                    return true;
                }

                String loverCase = t1.toLowerCase();

                if(comboCriteria.getValue().equals("ФИО")){
                    if (c.getFirstName().toLowerCase().contains(loverCase)){
                        return true;
                    } else if (c.getLastName().toLowerCase().contains(loverCase)){
                        return true;
                    } else if (c.getPatronymic().toLowerCase().contains(loverCase)){
                        return true;
                    }
                } else if (comboCriteria.getValue().equals("номер")){
                    if (c.getPhone().toLowerCase().contains(loverCase)){
                        return true;
                    }
                } else if(comboCriteria.getValue().equals("mail")){
                    if (c.getEmail().toLowerCase().contains(loverCase)){
                        return true;
                    }
                }


                return false;
            });
            tableClients.setItems(filteredList);
        });
    }

    public void pages(){
        //        comboBox;
        ObservableList<Integer> listQuantityRows = FXCollections.observableArrayList(10,50,200, maxClientSize);
        maxClientSize = listClients.size();
        quantityRows.setItems(listQuantityRows);
        quantityRows.setValue(listQuantityRows.get(0));


        quantityRows.valueProperty().addListener((obj, oldValue, newValue) -> {
            int valueComboBox = quantityRows.getValue();

            if (quantityRows.getValue() > listClients.size()){
                quantityRows.setValue(listClients.size());
                newValue = listClients.size();
            }

            totalPage = (int) (Math.ceil(maxClientSize * 1.0 / valueComboBox));


//       pages;
            pagination.setPageCount(totalPage);
            pagination.setCurrentPageIndex(0);
            tableClients.setItems(FXCollections.observableArrayList(
                    listClients.subList(pagination.getCurrentPageIndex(),newValue)));

//       pagination;
            pagination.currentPageIndexProperty().addListener((obj1, oldValue1, newValue1) -> {
                try {
                    tableClients.setItems(FXCollections.observableArrayList(listClients.subList(
                            valueComboBox * (newValue1.intValue() + 1) - valueComboBox, valueComboBox * (newValue1.intValue() + 1))));

                } catch (IndexOutOfBoundsException exception) {
                    tableClients.setItems(FXCollections.observableArrayList(listClients.subList(
                            valueComboBox * (newValue1.intValue() + 1) - valueComboBox, maxClientSize)));

                    labelSizeListClients.setText(listClients.size() + "/" + maxClientSize);
                }
            });
        });
    }

    public void initClients(){
        DaoImpl<Client, Integer> daoClients = new ServiceDaoImpClient(factory);
        listClients.addAll(daoClients.readAll());
    }

    public void searchByGender(){
        comboGender.setItems(listGender);
        comboGender.valueProperty().addListener((observableValue, character, c) -> {
            FilteredList<Client> filteredList = new FilteredList<>(listClients,
                    gender -> c.equals(gender.getGender().getCode()));
            tableClients.setItems(filteredList);
        });
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

    public void buttonAllRows(){
        for (int i = 0; i < listClients.size(); i++) {
            listClients.remove(i--);
        }

        initialize();

        labelSizeListClients.setText(listClients.size() + "/" + maxClientSize);
    }

    public void buttonDelete(){
    }

}