/**
 * This class represents single invoice
 */

package sample;

import java.time.LocalDate;

public class Invoice {

    private String saleOrPurchase; // if it's a sale invoice, value = "sale", but if it's a purchase invoice, value = "purchase"
    private String invoiceNumber;
    private String contractor;
    private LocalDate invoiceDate;
    private LocalDate saleDate;
    private double netto;
    private double vat;
    private double brutto;
    private String deductionVat;
    private String deductionCit;

    public Invoice(String saleOrPurchase, String invoiceNumber, String contractor, LocalDate invoiceDate, LocalDate saleDate, double netto, double vat, double brutto, String deductionVat, String deductionCit){
        this.saleOrPurchase=saleOrPurchase;
        this.invoiceNumber=invoiceNumber;
        this.contractor=contractor;
        this.invoiceDate=invoiceDate;
        this.saleDate=saleDate;
        this.netto=netto;
        this.vat=vat;
        this.brutto=brutto;
        this.deductionVat=deductionVat;
        this.deductionCit=deductionCit;
    }

    public String getSaleOrPurchase() {
        return saleOrPurchase;
    }

    public void setSaleOrPurchase(String saleOrPurchase) {
        this.saleOrPurchase = saleOrPurchase;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public double getNetto() {
        return netto;
    }

    public void setNetto(double netto) {
        this.netto = netto;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getBrutto() {
        return brutto;
    }

    public void setBrutto(double brutto) {
        this.brutto = brutto;
    }

    public String getDeductionVat() {
        return deductionVat;
    }

    public void setDeductionVat(String deductionVat) {
        this.deductionVat = deductionVat;
    }

    public String getDeductionCit() {
        return deductionCit;
    }

    public void setDeductionCit(String deductionCit) {
        this.deductionCit = deductionCit;
    }
}
