/**
 * This class represents main window, that show you all invoices. In this window you can add, edit and remove invoice. You can also calculate taxes.
 */

package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MainWindow {
    @FXML
    private Button btnAddInvoice;
    @FXML
    private Button btnRemoveInvoice;
    @FXML
    private Button btnCalculateTaxes;
    @FXML
    private Button btnEditInvoice;
    @FXML
    private Label labelVat;
    @FXML
    private Label labelCit;
    @FXML
    private TableView myTableView;
    @FXML
    private TableColumn<String, Invoice> tcType;
    @FXML
    private TableColumn<String, Invoice> tcInvoiceNumber;
    @FXML
    private TableColumn<String, Invoice> tcContractor;
    @FXML
    private TableColumn<LocalDate, Invoice> tcInvoiceData;
    @FXML
    private TableColumn<LocalDate, Invoice> tcSaleData;
    @FXML
    private TableColumn<Double, Invoice> tcNetto;
    @FXML
    private TableColumn<Double, Invoice> tcVat;
    @FXML
    private TableColumn<Double, Invoice> tcBrutto;
    @FXML
    private TableColumn<String, Invoice> tcVatDeduction;
    @FXML
    private TableColumn<String, Invoice> tcCitDeduction;

    private ObservableList<Invoice> tableViewData = FXCollections.observableArrayList();
    private Invoice currentInvoice;
    private Invoices invoices;
    private TableView.TableViewSelectionModel<Invoice> selectionModel;

    private double saleAmount;
    private double saleVat;
    private double purchaseAmount;
    private double purchaseVat;
    private double payVat;
    private double refundVat;
    private double cit;
    private double payCit;
    private double lossCit;

    @FXML
    private void initialize(){
        initializeData();
        initializeTableView();
        initializeBtnAddInvoiceClick();
        initializeBtnRemoveInvoiceClick();
        initializeBtnEditInvoiceClick();
        initializeTableSelectionClick();
        initializeBtnCalculateTaxesClick();
    }

    public void initializeData(){
        invoices = new Invoices();
        columnProperties();
    }

    public void columnProperties(){
        tcType.setCellValueFactory((new PropertyValueFactory<>("saleOrPurchase")));
        tcInvoiceNumber.setCellValueFactory((new PropertyValueFactory<>("invoiceNumber")));
        tcContractor.setCellValueFactory((new PropertyValueFactory<>("contractor")));
        tcInvoiceData.setCellValueFactory((new PropertyValueFactory<>("invoiceDate")));
        tcSaleData.setCellValueFactory((new PropertyValueFactory<>("saleDate")));
        tcNetto.setCellValueFactory((new PropertyValueFactory<>("netto")));
        tcVat.setCellValueFactory((new PropertyValueFactory<>("vat")));
        tcBrutto.setCellValueFactory((new PropertyValueFactory<>("brutto")));
        tcVatDeduction.setCellValueFactory((new PropertyValueFactory<>("deductionVat")));
        tcCitDeduction.setCellValueFactory((new PropertyValueFactory<>("deductionCit")));
    }

    public void initializeBtnAddInvoiceClick(){
        btnAddInvoice.setOnAction(event -> {
            try{
                showAddNewInvoiceWindow();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    public void initializeBtnRemoveInvoiceClick(){
        btnRemoveInvoice.setOnAction(event -> {
            if(currentInvoice != null) {
                invoices.removeInvoice(currentInvoice.getInvoiceNumber(), currentInvoice.getContractor());
                initializeTableView();
            }
        });
    }

   public void initializeBtnEditInvoiceClick(){
        btnEditInvoice.setOnAction(event -> {
            try{
                if(currentInvoice != null) {
                    showEditInvoiceWindow(currentInvoice);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    public Stage showAddNewInvoiceWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddNewInvoiceWindow.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        AddNewInvoiceWindow controller = loader.<AddNewInvoiceWindow>getController();
        controller.initData(invoices);

        stage.show();

        stage.setOnHiding(event -> {
            initializeTableView();
        });
        return stage;
    }

    public void initializeTableView(){

        tableViewData.clear();

        for(Invoice invoice : invoices.getInvoices().values()){
            tableViewData.add(invoice);
        }

        myTableView.setItems(tableViewData);
    }

    public void initializeTableSelectionClick(){
        selectionModel = myTableView.getSelectionModel();
        ObservableList<Invoice> selectedItem = selectionModel.getSelectedItems();
        selectedItem.addListener(new ListChangeListener<Invoice>() {
            @Override
            public void onChanged(Change<? extends Invoice> change) {
                currentInvoice = selectedItem.get(0);
            }
        });
    }

    public Stage showEditInvoiceWindow(Invoice invoice) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditInvoiceWindow.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        EditInvoiceWindow controller = loader.<EditInvoiceWindow>getController();
        controller.initData(invoice);

        stage.show();

        stage.setOnHiding(event -> {
            initializeTableView();
        });

        return stage;
    }

    public void initializeBtnCalculateTaxesClick() {
        btnCalculateTaxes.setOnAction(event -> {
            calculateTaxes();
        });
    }

    public void calculateTaxes(){
        saleAmount=0;
        saleVat=0;
        purchaseAmount=0;
        purchaseVat=0;
        payVat=0;
        refundVat=0;
        cit=0;
        payCit=0;
        lossCit=0;

        for(Invoice invoice : invoices.getInvoices().values()){

            if(invoice.getSaleOrPurchase().equals("Sprzedaż")){
                saleAmount+=invoice.getNetto();
                saleVat+=invoice.getVat();
            }
            else if(invoice.getSaleOrPurchase().equals("Zakup") && invoice.getDeductionVat().equals("100%") && invoice.getDeductionCit().equals("100%")){
                purchaseAmount+=invoice.getNetto();
                purchaseVat+=invoice.getVat();
            }
            else if(invoice.getSaleOrPurchase().equals("Zakup") && invoice.getDeductionVat().equals("100%") && invoice.getDeductionCit().equals("Nie odliczaj")){
                purchaseVat+=invoice.getVat();
            }
            else if(invoice.getSaleOrPurchase().equals("Zakup") && invoice.getDeductionVat().equals("50%") && invoice.getDeductionCit().equals("100%")){
                purchaseVat+=invoice.getVat()/2;
                purchaseAmount+=invoice.getNetto()+invoice.getVat()/2;
            }
            else if(invoice.getSaleOrPurchase().equals("Zakup") && invoice.getDeductionVat().equals("50%") && invoice.getDeductionCit().equals("Nie odliczaj")){
                purchaseVat+=invoice.getVat()/2;
            }
            else if(invoice.getSaleOrPurchase().equals("Zakup") && invoice.getDeductionVat().equals("Nie odliczaj") && invoice.getDeductionCit().equals("100%")){
                purchaseAmount+=invoice.getBrutto();
            }
        }

        if(saleVat>purchaseVat){
            payVat=Math.round(saleVat-purchaseVat);
            labelVat.setText("Podatek VAT podlegający wpłacie do Urzędu Skarbowego: "+payVat+" PLN.");
        }
        else if(saleVat<purchaseVat){
            refundVat=Math.round(purchaseVat-saleVat);
            labelVat.setText("Podatek VAT podlegający zwrotowi z Urzędu Skarbowego: "+refundVat+" PLN.");
        }
        else{
            labelVat.setText("Podatek VAT wynosi 0PLN. Jesteś już rozliczony z Urzędem Skarbowym.");
        }

        if(saleAmount>purchaseAmount){
            cit=saleAmount-purchaseAmount;
            payCit=Math.round(0.19*cit);
            labelCit.setText("Podatek CIT podlegający wpłacie do Urzędu Skarbowego: "+payCit+" PLN.");
        }
        else if(saleAmount<purchaseAmount){
            lossCit=purchaseAmount-saleAmount;
            labelCit.setText("Twoja strata do CIT wynosi: "+lossCit+" PLN, więc podatek CIT podlegający wpłacie do Urzędu Skarbowego: 0 PLN.");
        }
        else{
            labelCit.setText("Podatek CIT wynosi 0PLN. Jesteś już rozliczony z Urzędem Skarbowym.");
        }
    }
}
