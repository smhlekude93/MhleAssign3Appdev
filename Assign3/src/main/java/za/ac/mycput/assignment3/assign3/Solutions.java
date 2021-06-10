/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.mycput.assignment3.assign3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Siyasanga Mhlekude
 */
public class Solutions {

ObjectInputStream read;
FileWriter writer;
BufferedWriter buffWriter;
OutputStreamWriter output;
ArrayList<Customer> customer= new ArrayList<Customer>();
ArrayList<Supplier> supplier= new ArrayList<Supplier>();

    public void openFileRead(){
        try{
            read = new ObjectInputStream( new FileInputStream( "stakeholder.ser" ) );
            System.out.println("*** ser file created and opened for reading  ***");
        }
        catch (IOException ioe){
            System.out.println("error opening ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    public void readFile(){
        try{
           while(true){
               Object line = read.readObject();
               String c ="Customer";
               String s = "Supplier";
               String name = line.getClass().getSimpleName();
               if ( name.equals(c)){
                   customer.add((Customer)line);
               } else if ( name.equals(s)){
                   supplier.add((Supplier)line);
               } else {
                   System.out.println("It didn't work");
               }
           }
        }
        catch (EOFException eofe) {
            System.out.println("End of file reached");
        }
        catch (ClassNotFoundException ioe) {
            System.out.println("Class error reading ser file: "+ ioe);
        }
        catch (IOException ioe) {
            System.out.println("Error reading ser file: "+ ioe);
        }
    }
    public void readCloseFile(){
        try{
            read.close( ); 
        }
        catch (IOException ioe){
            System.out.println("error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public void sortCustomer(){
        String[] sortID = new String[customer.size()];
        ArrayList<Customer> sortA= new ArrayList<Customer>();
        int count = customer.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = customer.get(i).getStHolderId();
        }
        Arrays.sort(sortID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(customer.get(j).getStHolderId())){
                    sortA.add(customer.get(j));
                }
            }
        }
        customer.clear();
        customer = sortA;
    }
    public int getAge(String dob){
        String[] seperation = dob.split("-");
        
        LocalDate birth = LocalDate.of(Integer.parseInt(seperation[0]), Integer.parseInt(seperation[1]), Integer.parseInt(seperation[2]));
        LocalDate current = LocalDate.now();
        Period difference = Period.between(birth, current);
        int age = difference.getYears();
        return age;
    }
    public String formatDob(String dob){
        DateTimeFormatter changeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");       
        LocalDate birth = LocalDate.parse(dob);
        String formatted = birth.format(changeFormat);
        return formatted;
    }
    
    public void displayCustomersText(){
        try{
            writer = new FileWriter("customerOutFile.txt");
            buffWriter = new BufferedWriter(writer);
            buffWriter.write(String.format("%s \n","==============================Customers============================="));
            buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
            buffWriter.write(String.format("%s \n","===================================================================="));
            for (int i = 0; i < customer.size(); i++) {
                buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", customer.get(i).getStHolderId(), customer.get(i).getFirstName(), customer.get(i).getSurName(), formatDob(customer.get(i).getDateOfBirth()),getAge(customer.get(i).getDateOfBirth())));
            }
            buffWriter.write(String.format("%s\n"," "));
            buffWriter.write(String.format("%s",rent()));
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            buffWriter.close( );
        }
        catch (IOException ioe){
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public String rent(){
        int count = customer.size();
        int canRent = 0;
        int notRent = 0;
        for (int i = 0; i < count; i++) {
            if (customer.get(i).getCanRent()){
                canRent++;
            }else {
                notRent++;
            }
        }
        String line =String.format("Number of customers who can rent : %4s\nNumber of customers who cannot rent : %s\n", canRent, notRent);
        return line;
    }
    
    public void sortSuppliers(){
        String[] sortID = new String[supplier.size()];
        ArrayList<Supplier> sortA= new ArrayList<Supplier>();
        int count = supplier.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = supplier.get(i).getName();
        }
        Arrays.sort(sortID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(supplier.get(j).getName())){
                    sortA.add(supplier.get(j));
                }
            }
        }
        supplier.clear();
        supplier = sortA;
    }
    public void displaySupplierText(){
        try{
            writer = new FileWriter("supplierOutFile.txt", true);
            buffWriter = new BufferedWriter(writer);
            buffWriter.write("===============================SUPPLIERS==============================\n");
            buffWriter.write(String.format("%-15s %-20s %-15s %-15s \n", "ID","Name","Prod Type","Description"));
            buffWriter.write("======================================================================\n");
            for (int i = 0; i < supplier.size(); i++) {
                buffWriter.write(String.format("%-15s %-20s %-15s %-15s \n", supplier.get(i).getStHolderId(), supplier.get(i).getName(), supplier.get(i).getProductType(),supplier.get(i).getProductDescription()));
            }
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            buffWriter.close( );
        }
        catch (IOException ioe){
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }    
    
    public static void main(String args[])  {
        Solutions sol = new Solutions();
        sol.openFileRead();
        sol.readFile();
        sol.readCloseFile();
        sol.sortCustomer();
        sol.sortSuppliers();
        sol.displayCustomersText();
        sol.displaySupplierText();
        
    }
}
