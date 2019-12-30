/**
 * This class represents window that is used to add a new invoice.
 */

package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddNewInvoiceWindow {

    @FXML
    private ChoiceBox<String> cbInvoiceType;
    @FXML
    private TextField tfInvoiceNumber;
    @FXML
    private TextField tfContractor;
    @FXML
    private DatePicker dpInvoiceDate;
    @FXML
    private DatePicker dpSaleDate;
    @FXML
    private TextField tfNetto;
    @FXML
    private TextField tfVat;
    @FXML
    private TextField tfBrutto;
    @FXML
    private RadioButton rbVAT100;
    @FXML
    private RadioButton rbVAT50;
    @FXML
    private RadioButton rbVAT0;
    @FXML
    private RadioButton rbCIT100;
    @FXML
    private RadioButton rbCIT0;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private Label labelMessage;

    private Invoices invoices;
    private double netto;
    private double vat;
    private double brutto;
    private String vatDeduction;
    private String citDeduction;
    ToggleGroup toggleGroupVat;
    ToggleGroup toggleGroupCit;

    public void initData(Invoices invoices){
        this.invoices = invoices;
    }

    @FXML
    public void initialize(){
        initializeCbInvoiceType();
        initializeBtnSave();
        initializeBtnClose();
        initializeBtnGroupVat();
        initializeBtnGroupCit();
        initializeChooseCbInvoiceType();
        initializeBruttoFromNetto();
        initializeBruttoOrNettoFromVat();
        initializeNettoFromBrutto();
    }

    public void initializeCbInvoiceType(){
        cbInvoiceType.getItems().add("Sprzedaż");
        cbInvoiceType.getItems().add("Zakup");
    }

    public void initializeBtnGroupVat(){
        toggleGroupVat = new ToggleGroup();
        rbVAT100.setToggleGroup(toggleGroupVat);
        rbVAT50.setToggleGroup(toggleGroupVat);
        rbVAT0.setToggleGroup(toggleGroupVat);
        rbVAT100.setSelected(true);
    }

    public void initializeBtnGroupCit(){
        toggleGroupCit = new ToggleGroup();
        rbCIT100.setToggleGroup(toggleGroupCit);
        rbCIT0.setToggleGroup(toggleGroupCit);
        rbCIT100.setSelected(true);
    }

    public boolean validateInput(){
        if(cbInvoiceType.getValue() != null && tfInvoiceNumber.getText() != null &&
                !tfInvoiceNumber.getText().isEmpty() && tfContractor.getText() != null &&
                !tfContractor.getText().isEmpty() && dpInvoiceDate.getValue() != null &&
                dpSaleDate.getValue() != null && tfNetto.getText() != null &&
                !tfNetto.getText().isEmpty() && tfVat.getText() != null &&
                !tfVat.getText().isEmpty() && tfBrutto.getText() != null &&
                !tfBrutto.getText().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean validateDouble(){
        try{
            netto = (double)Math.round(Double.parseDouble(tfNetto.getText())*100)/100;
            vat = (double)Math.round(Double.parseDouble(tfVat.getText())*100)/100;
            brutto = (double)Math.round(Double.parseDouble(tfBrutto.getText())*100)/100;
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean validateAmount(){
        if(netto+vat==brutto){
            return true;
        }
        else{
            return false;
        }
    }

    public void deduction(){
        RadioButton selectedRadioButtonVat = (RadioButton) toggleGroupVat.getSelectedToggle();
        RadioButton selectedRadioButtonCit = (RadioButton) toggleGroupCit.getSelectedToggle();
        vatDeduction = selectedRadioButtonVat.getText();
        citDeduction = selectedRadioButtonCit.getText();
    }

    public void initializeBtnSave(){
        btnSave.setOnAction((event) ->{
            if(validateInput() && validateDouble() && validateAmount()){
                deduction();
                Invoice inv = new Invoice(cbInvoiceType.getValue(), tfInvoiceNumber.getText(), tfContractor.getText(), dpInvoiceDate.getValue(), dpSaleDate.getValue(), netto, vat, brutto, vatDeduction, citDeduction);
                invoices.addInvoice(inv);
                labelMessage.setTextFill(Color.GREEN);
                labelMessage.setText("Gotowe!");
            }
            else{
                labelMessage.setTextFill(Color.FIREBRICK);
                labelMessage.setText("Nie wszystkie pola zostały poprawnie wypełnione.");
            }
        });
    }

    public void initializeBtnClose(){
        btnClose.setOnAction((event) -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void initializeChooseCbInvoiceType(){
        cbInvoiceType.setOnAction(event -> {
            if(cbInvoiceType.getValue().equals("Sprzedaż")){
                rbVAT100.setSelected(true);
                rbCIT100.setSelected(true);
                rbVAT100.setDisable(true);
                rbVAT50.setDisable(true);
                rbVAT0.setDisable(true);
                rbCIT100.setDisable(true);
                rbCIT0.setDisable(true);
            }
            if(cbInvoiceType.getValue().equals("Zakup")){
                rbVAT100.setDisable(false);
                rbVAT50.setDisable(false);
                rbVAT0.setDisable(false);
                rbCIT100.setDisable(false);
                rbCIT0.setDisable(false);
            }
        });
    }

    public void initializeBruttoFromNetto(){
        tfNetto.setOnAction(event -> {
            completeBrutto();
        });
    }

    public void initializeBruttoOrNettoFromVat(){
        tfVat.setOnAction(event -> {
            if(tfNetto.getText() != null && !tfNetto.getText().isEmpty()){
                completeBrutto();
            }
            else if(tfBrutto.getText() != null && !tfBrutto.getText().isEmpty()){
                completeNetto();
            }
        });
    }

    public void completeBrutto(){
        if(tfNetto.getText() != null && !tfNetto.getText().isEmpty() && tfVat != null && !tfVat.getText().isEmpty() && validateForBrutto()){
            tfBrutto.setText(Double.toString(netto+vat));
        }
    }

    public boolean validateForBrutto(){
        try{
            netto = Double.parseDouble(tfNetto.getText());
            vat = Double.parseDouble(tfVat.getText());
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public void initializeNettoFromBrutto(){
        tfBrutto.setOnAction(event -> {
            completeNetto();
        });
    }

    public void completeNetto(){
        if(tfBrutto.getText() != null && !tfBrutto.getText().isEmpty() && tfVat != null && !tfVat.getText().isEmpty() && validateForNetto()){
            tfNetto.setText(Double.toString(brutto-vat));
        }
    }

    public boolean validateForNetto(){
        try{
            brutto = Double.parseDouble(tfBrutto.getText());
            vat = Double.parseDouble(tfVat.getText());
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
