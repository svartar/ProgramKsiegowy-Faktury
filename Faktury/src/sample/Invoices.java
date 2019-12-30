/**
 * This class represents list of invoices.
 */

package sample;

import java.util.HashMap;

public class Invoices {

    private HashMap<String, Invoice> invoices;
    private int size;

    public Invoices(){
        invoices = new HashMap<>();
    }

    public boolean addInvoice(Invoice invoice){

        StringBuilder sb = new StringBuilder();
        sb.append(invoice.getInvoiceNumber());
        sb.append(invoice.getContractor());

        if(invoices.get(sb.toString())==null){
            invoices.put(sb.toString(), invoice);
            size++;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeInvoice(String invoiceNumber, String contractor){

        if(invoices.remove(createStringBuilder(invoiceNumber, contractor)) != null){
            size--;
            return true;
        }
        else{
            return false;
        }
    }

    public Invoice getInvoice(String invoiceNumber, String contractor){

        return invoices.get(createStringBuilder(invoiceNumber, contractor));
    }

    public int getAmountInvoices(){
        return size;
    }

    public HashMap<String, Invoice> getInvoices(){
        return invoices;
    }

    public String createStringBuilder(String a, String b){

        StringBuilder sb = new StringBuilder();
        sb.append(a);
        sb.append(b);
        String result = sb.toString();
        return result;
    }
}
