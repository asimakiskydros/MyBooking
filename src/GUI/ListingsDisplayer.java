package GUI;

import accommodationTypes.Hotel;
import accommodationTypes.HotelRoom;
import data.Database;
import accommodationTypes.Accommodation;
import userTypes.Provider;
import userTypes.Admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;

/**
 * ListingsDisplayer
 * This frame displays all owned and uploaded accommodations to their owned
 * Supports search button so the owner can track down and edit a certain listing (jumps to AccommodationsEditor)
 * Double-clicking on a listing on the list will remove it from the database
 */
public class ListingsDisplayer extends JFrame{
    private JPanel mainListingsDisplayerLabel;
    private JList<String> ownedListingsList;
    private JTextField inputListingTextField;
    private JButton searchButton;
    private JLabel descriptorLabel;
    private JLabel ownedListingsLabel;
    private JLabel warningLabel;
    private JLabel feedbackLabel;
    private JButton refreshListingsButton;
    private JButton backButton;

    public ListingsDisplayer(Provider owner, Database dataReference){
        super();
        this.setTitle("My Listings");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(500, 500));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainListingsDisplayerLabel);
        this.pack();

        //fetch owned accommodations
        HashSet<String> tempListings = owner.getOwnedAccommodations();
        if(!tempListings.isEmpty()) {
            warningLabel.setVisible(false);
            descriptorLabel.setText("Search through your listings");

            refreshList(tempListings);
        }
        else{
            ownedListingsList.setVisible(false);
            inputListingTextField.setVisible(false);
            searchButton.setVisible(false);
            ownedListingsLabel.setVisible(false);
            warningLabel.setVisible(false);
            feedbackLabel.setVisible(false);
            refreshListingsButton.setVisible(false);
            descriptorLabel.setText("No registered accommodations to show");
        }
        searchButton.addActionListener(e -> {
            //"refresh" the warning signal
            warningLabel.setVisible(false);
            feedbackLabel.setText("Double Click on a listing to delete it");
            //get input
            String input = inputListingTextField.getText();
            if(tempListings.contains(input)){
                Accommodation listing = Admin.searchAccommodations(input, dataReference);
                if(listing != null) {
                    new AccommodationsEditor(listing, owner, dataReference);
                    super.dispose();
                }
            }
            else{
                warningLabel.setVisible(true);
            }
        });
        ownedListingsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!ownedListingsList.isSelectionEmpty() && e.getClickCount() == 2){
                    //get input
                    String selection = ownedListingsList.getSelectedValue();

                    //remove it from all databases
                    Accommodation inputListing = Admin.searchAccommodations(selection, dataReference);
                    if(inputListing != null) {
                        //if hotel was selected, and the hotel supports rooms, the user intends to delete a room
                        //if they do intend to delete the hotel, they will have to delete all its rooms first
                        if(inputListing instanceof Hotel && !((Hotel)inputListing).getRooms().isEmpty()){
                            //update list to show only this hotel's rooms
                            DefaultListModel<String> hotelModel = new DefaultListModel<>();
                            hotelModel.addElement("Hotel " + inputListing.getName() + "'s rooms: ");
                            HashSet<HotelRoom> rooms = ((Hotel)inputListing).getRooms();
                            for(HotelRoom room : rooms)
                                hotelModel.addElement(room.getName());
                            ownedListingsList.setModel(hotelModel);
                        }
                        else {
                            owner.removeAccommodation(inputListing, dataReference);
                            tempListings.remove(selection);
                            feedbackLabel.setText(">>> Listing removed");
                        }
                    }
                }
            }
        });
        refreshListingsButton.addActionListener(e ->
                refreshList(tempListings)
        );

        backButton.addActionListener(e ->{
            new Profiler(owner, dataReference);
            super.dispose();
        });
    }
    /**
     * Model refresher for list
     * @param listings the new data-set of listings, so a new model can be created
     */
    private void refreshList(HashSet<String> listings){
        DefaultListModel<String> model = new DefaultListModel<>();
        //feed accommodation info to model
        for (String listing : listings)
            model.addElement(listing);
        ownedListingsList.setModel(model);
    }
    public static void main(String[] args){
        Provider temp = new Provider(
                "yes",
                "no",
                "maybe",
                "perhaps",
                "sure",
                "yeya",
                true
        );
        HashSet<String> tempListings = new HashSet<>();
        tempListings.add("Cool house by the beach");
        tempListings.add("My house");
        tempListings.add("A listing");
        temp.setOwnedAccommodations(tempListings);

        new ListingsDisplayer(temp, new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }

}
