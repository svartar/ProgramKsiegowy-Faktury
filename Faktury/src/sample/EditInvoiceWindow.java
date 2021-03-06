/**
 * This class represents window that is used to edit selected invoice.
 */

package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditInvoiceWindow {

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

    private Invoice currentInvoice;
    private double netto;
    private double vat;
    private double brutto;
    private String vatDeduction;
    private String citDeduction;
    ToggleGroup toggleGroupVat;
    ToggleGroup toggleGroupCit;

    public void initData(Invoice invoice){
        currentInvoice = invoice;
        initializeCurrentInvoice();
    }

    @FXML
    public void initialize(){
        initializeCbInvoiceType();
        initializeBtnGroupVat();
        initializeBtnGroupCit();
        initializeChooseCbInvoiceType();
        initializeBtnSave();
        initializeBtnClose();
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
    }

    public void initializeBtnGroupCit(){
        toggleGroupCit = new ToggleGroup();
        rbCIT100.setToggleGroup(toggleGroupCit);
        rbCIT0.setToggleGroup(toggleGroupCit);
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
                currentInvoice.setSaleOrPurchase(cbInvoiceType.getValue());
                currentInvoice.setInvoiceNumber(tfInvoiceNumber.getText());
                currentInvoice.setContractor(tfContractor.getText());
                currentInvoice.setInvoiceDate(dpInvoiceDate.getValue());
                currentInvoice.setSaleDate(dpSaleDate.getValue());
                currentInvoice.setNetto(netto);
                currentInvoice.setVat(vat);
                currentInvoice.setBrutto(brutto);
                currentInvoice.setDeductionVat(vatDeduction);
                currentInvoice.setDeductionCit(citDeduction);
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

    public void initializeCurrentInvoice(){
        if(currentInvoice.getSaleOrPurchase().equals("Sprzedaż")){
            cbInvoiceType.setValue("Sprzedaż");
        }
        else if(currentInvoice.getSaleOrPurchase().equals("Zakup")){
            cbInvoiceType.setValue("Zakup");
        }
        tfInvoiceNumber.setText(currentInvoice.getInvoiceNumber());
        tfContractor.setText(currentInvoice.getContractor());
        dpInvoiceDate.setValue(currentInvoice.getInvoiceDate());
        dpSaleDate.setValue(currentInvoice.getSaleDate());
        tfNetto.setText(Double.toString(currentInvoice.getNetto()));
        tfVat.setText(Double.toString(currentInvoice.getVat()));
        tfBrutto.setText(Double.toString(currentInvoice.getBrutto()));
        if(currentInvoice.getDeductionVat().equals("100%")){
            rbVAT100.setSelected(true);
        }
        else if(currentInvoice.getDeductionVat().equals("50%")){
            rbVAT50.setSelected(true);
        }
        else{
            rbVAT0.setSelected(true);
        }
        if(currentInvoice.getDeductionCit().equals("100%")){
            rbCIT100.setSelected(true);
        }
        else{
            rbCIT0.setSelected(true);
        }
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

    public boolean validateAmount(){
        if(netto+vat==brutto){
            return true;
        }
        else{
            return false;
        }
    }
}
