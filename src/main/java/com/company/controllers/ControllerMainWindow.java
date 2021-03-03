package com.company.controllers;

import com.company.dao.DaoImpl;
import com.company.model.Client;
import com.company.model.ClientService;
import com.company.service.ServiceDaoImpClient;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
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

//    @FXML
//    private TableColumn<Client, String> columnTegs;

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
        columnCountDate.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getClientServices().size()));
        columnDateReg.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getRegistrationDate()));
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
        selectFromCriteria();
    }

    public void selectFromAll(){
        txtSearch.textProperty().addListener((observableValue, s, t1) -> {
            FilteredList<Client> filteredList = new FilteredList<>(listClients, c -> {
                if (t1 == null || t1.isEmpty()){
                    return true;
                }

                String loverCase = t1.toLowerCase();

                if (c.getFirstName().toLowerCase().contains(loverCase)){
                    return true;
                } else if (c.getLastName().toLowerCase().contains(loverCase)){
                    return true;
                } else if (c.getPatronymic().toLowerCase().contains(loverCase)){
                    return true;
                }
                if (c.getPhone().toLowerCase().contains(loverCase)){
                    return true;
                }
                if (c.getEmail().toLowerCase().contains(loverCase)){
                    return true;
                }


               return false;
            });
            tableClients.setItems(filteredList);
        });
    }

    public void selectFromCriteria(){
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
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/logo.png")));
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

    public void exportPDF() throws IOException, DocumentException {

        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(new Stage());
        String pathFile = file.getAbsolutePath();


        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(pathFile));

        document.open();
//        paragraph;
        String FONT = "./src/main/resources/font/arial.ttf";

        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf,30,Font.NORMAL);
        Font font1 = new Font(bf, 8, Font.NORMAL);


        Paragraph paragraph = new Paragraph("Auto service", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);

        Image image = Image.getInstance("./src/main/resources/images/head.png");
        image.scaleAbsoluteHeight(70);
        image.scaleAbsoluteWidth(130);
        image.setAlignment(Element.ALIGN_RIGHT);
        document.add(image);


//        create table;
        int quantityColumn = tableClients.getColumns().size();
        PdfPTable table = new PdfPTable(quantityColumn);
        ObservableList<TableColumn<Client, ?>> columns = tableClients.getColumns();
        columns.forEach(c -> new PdfPCell(new Phrase(c.getText())));

        for (TableColumn<Client, ?> column: columns) {
            table.addCell(new PdfPCell(new Phrase(column.getText(),font1)));
        }
        table.setHeaderRows(1);


        for (int i = 0; i < quantityColumn; i++) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(tableClients.getItems().get(i).getId()),font1)));
            table.addCell(new PdfPCell(new Phrase(tableClients.getItems().get(i).getFirstName(),font1)));
            table.addCell(new PdfPCell(new Phrase(tableClients.getItems().get(i).getLastName(),font1)));
            table.addCell(new PdfPCell(new Phrase(tableClients.getItems().get(i).getPatronymic(),font1)));
            table.addCell(new PdfPCell(new Phrase(tableClients.getItems().get(i).getPhone(),font1)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(tableClients.getItems().get(i).getBirthday()),font1)));
            table.addCell(new PdfPCell(new Phrase(tableClients.getItems().get(i).getEmail(),font1)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(tableClients.getItems().get(i).getRegistrationDate()),font1)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(
                    tableClients.getItems().get(i).getClientServices().size()),font1)));

            Set<ClientService> clientServices = tableClients.getItems().get(i).getClientServices();
            String str = "";
            if (clientServices.size() != 0){
                Date date = clientServices.stream().max(Comparator
                        .comparing(ClientService::getStartTime)).get().getStartTime();
                str = new SimpleDateFormat("dd-MM-yyyy").format(date);
            } table.addCell(new PdfPCell(new Phrase(str,font1)));
        }

        document.add(table);
        document.close();
    }

    public void exportExel() throws IOException {

        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(new Stage());
        String path = file.getAbsolutePath();


        int sizeTableClient = tableClients.getColumns().size(); 

//        создание документа
        XSSFWorkbook client = new XSSFWorkbook();
//        Создание листа;
        Sheet sheet = client.createSheet("Clients auto service");

//        Создание строки;
        Row columnNames = sheet.createRow(0);

//        Задания имён столбцам;
        for (int i = 0; i < tableClients.getColumns().size(); i++) {
//        Добавление ячеек;
            Cell cell = columnNames.createCell(i);
            cell.setCellValue(tableClients.getColumns().get(i).getText());
        }

//         Формат даты;
        DataFormat format = client.createDataFormat();
        CellStyle dateStyle = client.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd-MM-yyyy"));

        for (int i = 0; i < tableClients.getItems().size(); i++) {
            Row row = sheet.createRow(i+1);
            Cell id = row.createCell(0);
            Cell name = row.createCell(1);
            Cell lastName = row.createCell(2);
            Cell patronymic = row.createCell(3);
            Cell birthday = row.createCell(4);
            Cell phone = row.createCell(5);
            Cell mail = row.createCell(6);
            Cell dateRegistration = row.createCell(7);
            Cell lastDateVisit = row.createCell(8);
            Cell quantityVisit = row.createCell(9);


            id.setCellValue(tableClients.getItems().get(i).getId());
            name.setCellValue(tableClients.getItems().get(i).getFirstName());
            lastName.setCellValue(tableClients.getItems().get(i).getLastName());
            patronymic.setCellValue(tableClients.getItems().get(i).getPatronymic());
            birthday.setCellStyle(dateStyle);
            birthday.setCellValue(tableClients.getItems().get(i).getBirthday());
            dateRegistration.setCellStyle(dateStyle);
            dateRegistration.setCellValue(tableClients.getItems().get(i).getRegistrationDate());
            mail.setCellValue(tableClients.getItems().get(i).getEmail());
            phone.setCellValue(tableClients.getItems().get(i).getPhone());
            quantityVisit.setCellValue(tableClients.getItems().get(i).getClientServices().size());

            Set<ClientService> clientServices = tableClients.getItems().get(i).getClientServices();
            String lastDate = "";
            if (clientServices.size() != 0){
                Date date = clientServices.stream().max(Comparator
                        .comparing(ClientService::getStartTime)).get().getStartTime();
                lastDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                lastDateVisit.setCellValue(lastDate);
            } new SimpleObjectProperty<>("");
        }





        sheet.autoSizeColumn(1);

        client.write(new FileOutputStream(path));
        client.close();
    }

}