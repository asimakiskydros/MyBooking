package GUI;

import userTypes.Customer;
import javax.swing.*;
import accommodationTypes.*;
import data.Database;
import misc.Features;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;

/**
 * AccommodationsBrowser
 * This frame implements a browser, through which customers can input certain demands (features) and
 * see the available listings that satisfy them.
 * Supports lists that project all uploaded accommodations, and checkButtons to specify demands
 * Double-clicking a listing will lead to BookingMaker, so a reservation can take place
 */
public class AccommodationsBrowser extends JFrame{
    private JPanel mainBrowserPanel;
    private JList<String> outputList;
    private JCheckBox freeWiFiCheckBox, parkingCheckBox, smokingCheckBox, petsCheckBox, elevatorCheckBox,
                        barCheckBox, poolCheckBox, spaCheckBox, proximityCheckBox;
    private JButton searchButton, backButton;
    private JLabel outputLabel, toolTipLabel;

    public AccommodationsBrowser(Customer person, Database dataReference){
        super();
        this.setTitle("Browse Accommodations");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(500, 600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainBrowserPanel);
        this.pack();

        //fetch all registered accommodations
        HashSet<Accommodation> tempListings = dataReference.getRegisteredAccommodations();
        if(tempListings.isEmpty()){
            //no accommodations to show/search; display "dead" screen
            outputList.setVisible(false);
            toolTipLabel.setVisible(false);
            outputLabel.setText("No registered accommodations to show");
        }
        else
            //show nothing at first, wait for demands
            outputList.setModel(new DefaultListModel<>());

        searchButton.addActionListener(e -> {
            if(!tempListings.isEmpty()) {
                HashSet<Features> demands = new HashSet<>();
                //get all of customer's preferences
                if (freeWiFiCheckBox.isSelected()) demands.add(Features.WIFI);
                if (parkingCheckBox.isSelected()) demands.add(Features.PARKING);
                if (smokingCheckBox.isSelected()) demands.add(Features.SMOKING);
                if (petsCheckBox.isSelected()) demands.add(Features.PETS);
                if (elevatorCheckBox.isSelected()) demands.add(Features.ELEVATOR);
                if (barCheckBox.isSelected()) demands.add(Features.BAR);
                if (poolCheckBox.isSelected()) demands.add(Features.POOL);
                if (spaCheckBox.isSelected()) demands.add(Features.SPA);
                if (proximityCheckBox.isSelected()) demands.add(Features.PROXIMITY);

                HashSet<Apartment> apartments = new HashSet<>();
                HashSet<Hotel> hotels = new HashSet<>();
                HashSet<Maisonette> maisonettes = new HashSet<>();

                //filter in the listings that satisfy all the demands and out those that do not
                for (Accommodation listing : tempListings)
                    //if accommodation's features encompass user's demands
                    if (compare(listing.getFeatures(), demands)) {
                        if (listing instanceof Apartment) apartments.add((Apartment) listing);
                        else if (listing instanceof Hotel) hotels.add((Hotel) listing);
                        else maisonettes.add((Maisonette) listing);
                    }
                DefaultListModel<String> modelOutput = new DefaultListModel<>();
                //feed info to model
                for (Apartment apartment : apartments)
                    modelOutput.addElement("Apartment: " + apartment.getName());
                for (Hotel hotel : hotels)
                    modelOutput.addElement("Hotel: " + hotel.getName());
                for (Maisonette maisonette : maisonettes)
                    modelOutput.addElement("Maisonette: " + maisonette.getName());

                //push model to list
                outputList.setModel(modelOutput);
            }
        });
        outputList.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("StringConcatenationInLoop")
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!tempListings.isEmpty() && !outputList.isSelectionEmpty() && e.getClickCount() == 2){
                    //get selection
                    String selection = "";
                    char[] selectionChars = outputList.getSelectedValue().toCharArray();
                    int letter = 0;
                    //ignore all letters until ':' is reached
                    while(selectionChars[letter] != ':') letter++;
                    //jump two letter (as between ':' and accommodation name there are some spaces)
                    ++letter;
                    for(++letter; letter < selectionChars.length; letter++)
                        //read name
                        selection += ((Character)selectionChars[letter]).toString();

                    //find corresponding accommodation
                    Accommodation result = null;
                    for(Accommodation listing : tempListings)
                        if(selection.equals(listing.getName())){
                            result = listing;
                            break;
                        }
                    //initiate BookingMaker with result accommodation
                    if(result != null){
                        new BookingMaker(result, person, dataReference);
                        AccommodationsBrowser.super.dispose();
                    }
                }
            }
        });

        backButton.addActionListener(e ->{
            new Menu(person, dataReference);
            super.dispose();
        });
    }
    /**
     * Basic compare function (for sets of Features)
     * @param set1 supposed superset
     * @param set2 supposed subset
     * @return true if set2 is subset of set1
     */
    private boolean compare(HashSet<Features> set1,HashSet<Features> set2){
        //return true only if all elements of set2 are contained within set1
        //this basically means true if set2 subset of set1, which suffices here
        for(Features feature : set2)
            if(!set1.contains(feature))
                return false;
        return true;
    }
    public static void main(String[] args){
        new AccommodationsBrowser(new Customer(
                "grigorakis",
                "123",
                "Grigoris Grigoridis",
                "edw",
                "6989704212",
                true
        ), new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}