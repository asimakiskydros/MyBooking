package GUI;

import javax.swing.*;
import misc.MyPair;
import userTypes.Admin;
import userTypes.Customer;
import data.Database;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * ReservationsBrowser
 * This frame allows admins to view and search active reservations made by customers
 * On call, summons and displays all active reservations in a list, if there exist none, it displays a "dead" message
 * Supports button to search by customers and/or accommodations and highlight associated reservations
 * in a second list
 */
public class ReservationsBrowser extends JFrame{
    private JPanel mainBrowserPanel;
    private JList<String> outputList;
    private JList<String> activeReservationsList;
    private JTextField searchTextField;
    private JButton searchButton;
    private JLabel warningLabel;
    private JLabel outputLabel;
    private JLabel activeReservationsLabel;
    private JLabel titleLabel;
    private JLabel titleLabel1;
    private JButton backButton;

    public ReservationsBrowser(Admin admin, Database dataReference){
        super();
        this.setTitle("Browse Reservations");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(850,600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainBrowserPanel);
        this.pack();

        warningLabel.setVisible(false);

        //fetch customers
        HashSet<Customer> tempCustomers = dataReference.getRegisteredCustomers();
        DefaultListModel<String> modelActiveReservations = new DefaultListModel<>();

        //scan all customers and format their reservations into a string, then push it into model
        //provided customers exist, of course
        if(tempCustomers != null)
            for(Customer customer : tempCustomers){
                HashMap<MyPair,String> tempBookings = customer.getReservations();
                    for(MyPair dates : tempBookings.keySet()){
                        String reservation = tempBookings.get(dates);
                        //push into model
                        modelActiveReservations.addElement(customer.printReservation(
                                                            reservation,dates.getHead(),dates.getValue())
                                                        );
                    }
            }
        if(modelActiveReservations.isEmpty()){
            //no reservations have been made yet, show "dead" screen
            outputList.setVisible(false);
            activeReservationsList.setVisible(false);
            searchTextField.setVisible(false);
            searchButton.setVisible(false);
            warningLabel.setVisible(false);
            outputLabel.setVisible(false);
            activeReservationsLabel.setText("No active reservations to show");
            titleLabel.setVisible(false);
            titleLabel1.setVisible(false);
        }
        else{
            //print model
            activeReservationsList.setModel(modelActiveReservations);
            //set default-empty model (at first)
            outputList.setModel(new DefaultListModel<>());
        }

        searchButton.addActionListener(e -> {
            //refresh
            warningLabel.setVisible(false);

            String input = searchTextField.getText();
            //input can either be person's username or accommodation's name
            DefaultListModel<String> modelOutput;

            //search in customers and in their reservations
            Customer resultUser = null;
            boolean isListing = false;
            assert tempCustomers != null;
            for(Customer customer : tempCustomers) {
                if (Objects.equals(customer.getUsername(), input)) {
                    //input is a person
                    resultUser = customer;
                    break;
                } else if (customer.getReservations().containsValue(input)) {
                    //input is an accommodation
                    isListing = true;
                    break;
                }
            }

            if(resultUser != null){
                //print customer's reservations
                modelOutput = new DefaultListModel<>();
                HashMap<MyPair, String> tempReservations = resultUser.getReservations();
                for(MyPair tempDates : tempReservations.keySet()){
                    String reservation = tempReservations.get(tempDates);
                    modelOutput.addElement(resultUser.printReservation(reservation,tempDates.getHead(),
                            tempDates.getValue()) + " by " + resultUser.getUsername());
                }
                //push model to list
                outputList.setModel(modelOutput);
            }
            else if(isListing){
                //search all customers and if they have a reservation to that listing
                //push formatted reservation to new model
                modelOutput = new DefaultListModel<>();
                for(Customer customer : tempCustomers){
                    HashMap<MyPair,String> tempReservations = customer.getReservations();
                    if(tempReservations.containsValue(input)){
                        MyPair tempDates = new MyPair(0, 0);
                        for(MyPair dates : tempReservations.keySet())
                            if(Objects.equals(tempReservations.get(dates), input)){
                                tempDates = dates;
                                break;
                            }
                        modelOutput.addElement(customer.printReservation(input,tempDates.getHead(),
                                tempDates.getValue()) + " by " + customer.getUsername());
                    }
                    //push model to list
                    outputList.setModel(modelOutput);
                }
            }
            else{
                //input is wrong, print warning message
                warningLabel.setVisible(true);
            }
        });
        backButton.addActionListener(e ->{
            new Menu(admin, dataReference);
            super.dispose();
        });
    }
    public static void main(String[] args){
        new ReservationsBrowser(new Admin("argiris",
                "123",
                " edw",
                "Argirakis Argiriou",
                "6989704212"
        ),new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}
