package GUI;

import accommodationTypes.*;
import data.Database;
import misc.Features;
import userTypes.Admin;
import userTypes.Provider;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;

/**
 * AccommodationsEditor
 * This frame allows providers to edit the characteristics and features of their owned accommodations
 * On call, summons and displays the already registered info bound to accommodation in question
 * Supports textFields and CheckButtons so providers can interact with the info
 * Supports save and exit button to update the database with the new accommodation's info,
 * in case of wrong input (such as NumberFormatException), changes are reverted.
 */
public class AccommodationsEditor extends JFrame{
    public JPanel mainEditorPanel;
    private JTextField nameTextField, addressTextField, areaTextField, roomsTextField, bedroomsTextField,
                        bathroomsTextField, bedsTextField, atFloorTextField, numFloorsTextField;
    private JTextArea descTextArea;
    private JCheckBox freeWiFiCheckBox, parkingCheckBox, smokingAllowedCheckBox, petsAllowedCheckBox,
                        elevatorCheckBox, barCheckBox, poolCheckBox, spaCheckBox, proximityCheckBox;
    private JButton saveAndExitButton;
    private JLabel warningLabel, accommodationTitleLabel;
    private JButton backButton;

    //save old info to temp backup so that if an error occurs along the way we can revert
    private Accommodation backUp;

    public AccommodationsEditor(Accommodation listing, Provider owner, Database dataReference){
        super();
        this.setTitle("Accommodation Editor");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(600, 600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainEditorPanel);
        this.pack();

        warningLabel.setVisible(false);

        //initialize textFields to old data
        nameTextField.setText(listing.getName());
        descTextArea.setText(listing.getDescription());
        if(listing instanceof Apartment){
            addressTextField.setText(((Apartment)listing).getAddress());
            areaTextField.setText((((Apartment)listing).getArea()).toString());
            roomsTextField.setText((((Apartment)listing).getRooms()).toString());
            bedroomsTextField.setText((((Apartment)listing).getBedrooms()).toString());
            bathroomsTextField.setText((((Apartment)listing).getBathrooms()).toString());

            accommodationTitleLabel.setText("Editing Apartment " + listing.getName());

            //inform back-up
            backUp = new Apartment(listing.getName(), listing.getPhotos(), listing.getDescription(),
                    listing.getFeatures(), ((Apartment) listing).getAddress(), ((Apartment) listing).getArea(),
                    ((Apartment) listing).getRooms(), listing.getOwner());
        }
        else if(listing instanceof Maisonette){
            addressTextField.setText(((Maisonette)listing).getAddress());
            areaTextField.setText((((Maisonette)listing).getArea()).toString());
            roomsTextField.setText((((Maisonette)listing).getRooms()).toString());
            bedroomsTextField.setText((((Maisonette)listing).getBedrooms()).toString());
            bathroomsTextField.setText((((Maisonette)listing).getBathrooms()).toString());
            numFloorsTextField.setText((((Maisonette)listing).getFloors()).toString());

            accommodationTitleLabel.setText("Editing Maisonette " + listing.getName());

            //inform back-up
            backUp = new Maisonette(listing.getName(), listing.getPhotos(), listing.getDescription(),
                    listing.getFeatures(), ((Maisonette) listing).getAddress(), ((Maisonette) listing).getArea(),
                    ((Maisonette) listing).getRooms(), ((Maisonette) listing).getFloors(), listing.getOwner());
        }
        else if(listing instanceof Hotel){
            addressTextField.setText(((Hotel)listing).getAddress());
            numFloorsTextField.setText((((Hotel)listing).getFloors()).toString());

            accommodationTitleLabel.setText("Editing Hotel " + listing.getName());

            //inform back-up
            backUp = new Hotel(listing.getName(), listing.getPhotos(), listing.getDescription(), listing.getFeatures(),
                    null, ((Hotel) listing).getAddress(), ((Hotel) listing).getFloors(), listing.getOwner());
        }
        else if(listing instanceof HotelRoom){
            areaTextField.setText((((HotelRoom)listing).getArea()).toString());
            atFloorTextField.setText((((HotelRoom)listing).getFloor()).toString());
            bedsTextField.setText((((HotelRoom)listing).getBeds()).toString());

            accommodationTitleLabel.setText("Editing Hotel Room " + listing.getName());

            //inform back-up
            backUp = new HotelRoom(listing.getName(), listing.getPhotos(), listing.getDescription(),
                    listing.getFeatures(), ((HotelRoom) listing).getFloor(), ((HotelRoom) listing).getArea(),
                    ((HotelRoom) listing).getBeds(), listing.getOwner());
        }

        //fetch features
        HashSet<Features> tempFeatures = listing.getFeatures();
        //initialize checkBoxes to old data
        if(tempFeatures.contains(Features.WIFI)) freeWiFiCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.PARKING)) parkingCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.SMOKING)) smokingAllowedCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.PETS)) petsAllowedCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.ELEVATOR)) elevatorCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.BAR)) barCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.POOL)) poolCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.SPA)) spaCheckBox.setSelected(true);
        if(tempFeatures.contains(Features.PROXIMITY)) proximityCheckBox.setSelected(true);

        saveAndExitButton.addActionListener(e -> {
            warningLabel.setVisible(false);
            boolean errorOccurred = false;
            //update characteristics
            //(!) Avoid empty textFields or blank inputs
            String input = nameTextField.getText();
            //Names should also be unique
            if(!input.isEmpty() && !input.isBlank() && Admin.searchAccommodations(input, dataReference) == null)
                listing.setName(input);
            input = descTextArea.getText();
            if(!input.isEmpty() && !input.isBlank()) listing.setDescription(input);

            try{
                if(listing instanceof Apartment){
                    input = addressTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Apartment) listing).setAddress(input);

                    input = areaTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Apartment) listing).setArea(Integer.parseInt(input));

                    input = roomsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Apartment) listing).setRooms(Integer.parseInt(input));

                    input = bedroomsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Apartment) listing).setBedrooms(Integer.parseInt(input));

                    input = bathroomsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Apartment) listing).setBathrooms(Integer.parseInt(input));
                }
                else if(listing instanceof Maisonette){
                    input = addressTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Maisonette) listing).setAddress(input);

                    input = areaTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Maisonette) listing).setArea(Integer.parseInt(input));

                    input = roomsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Maisonette) listing).setRooms(Integer.parseInt(input));

                    input = bedroomsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Maisonette) listing).setBedrooms(Integer.parseInt(input));

                    input = bathroomsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Maisonette) listing).setBathrooms(Integer.parseInt(input));

                    input = numFloorsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Maisonette) listing).setFloors(Integer.parseInt(input));
                }
                else if(listing instanceof Hotel){
                    input = addressTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Hotel)listing).setAddress(input);

                    input = numFloorsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((Hotel) listing).setFloors(Integer.parseInt(input));
                }
                else if(listing instanceof HotelRoom){
                    input = areaTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((HotelRoom) listing).setArea(Integer.parseInt(input));

                    input = atFloorTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((HotelRoom) listing).setFloor(Integer.parseInt(input));

                    input = bedsTextField.getText();
                    if(!input.isEmpty() && !input.isBlank()) ((HotelRoom) listing).setBeds(Integer.parseInt(input));
                }
            }catch(NumberFormatException d){
                //(!)(!) Input given is not an integer, show error message and revert
                warningLabel.setVisible(true);
                revert(listing);
                errorOccurred = true;
            }
            if(!errorOccurred) {
                //update features
                HashSet<Features> newFeatures = new HashSet<>();
                if (freeWiFiCheckBox.isSelected()) newFeatures.add(Features.WIFI);
                if (parkingCheckBox.isSelected()) newFeatures.add(Features.PARKING);
                if (smokingAllowedCheckBox.isSelected()) newFeatures.add(Features.SMOKING);
                if (petsAllowedCheckBox.isSelected()) newFeatures.add(Features.PETS);
                if (elevatorCheckBox.isSelected()) newFeatures.add(Features.ELEVATOR);
                if (barCheckBox.isSelected()) newFeatures.add(Features.BAR);
                if (poolCheckBox.isSelected()) newFeatures.add(Features.POOL);
                if (spaCheckBox.isSelected()) newFeatures.add(Features.SPA);
                if (proximityCheckBox.isSelected()) newFeatures.add(Features.PROXIMITY);

                listing.setFeatures(newFeatures);

                //update database
                dataReference.pushRegisteredAccommodation(listing);
                new ListingsDisplayer(owner, dataReference);
                super.dispose();
            }
        });

        backButton.addActionListener(e ->{
            new ListingsDisplayer(owner, dataReference);
            super.dispose();
        });
    }
    /**
     * Revert function to undo changes if an error occurs
     * (most likely NumberFormatException)
     * @param listing the modified accommodation to be reverted
     */
    private void revert(Accommodation listing){
        //error encountered, return to back-up info
        listing.setName(backUp.getName());
        listing.setDescription(backUp.getDescription());
        if(listing instanceof Apartment){
            ((Apartment)listing).setAddress(((Apartment) backUp).getAddress());
            ((Apartment)listing).setArea(((Apartment) backUp).getArea());
            ((Apartment)listing).setRooms(((Apartment) backUp).getRooms());
            ((Apartment)listing).setBedrooms(((Apartment) backUp).getBedrooms());
            ((Apartment)listing).setBathrooms(((Apartment) backUp).getBathrooms());
        }
        else if(listing instanceof Maisonette){
            ((Maisonette)listing).setAddress(((Maisonette) backUp).getAddress());
            ((Maisonette)listing).setArea(((Maisonette) backUp).getArea());
            ((Maisonette)listing).setRooms(((Maisonette) backUp).getRooms());
            ((Maisonette)listing).setBedrooms(((Maisonette) backUp).getBedrooms());
            ((Maisonette)listing).setBathrooms(((Maisonette) backUp).getBathrooms());
            ((Maisonette)listing).setFloors(((Maisonette) backUp).getFloors());
        }
        else if(listing instanceof Hotel){
            ((Hotel)listing).setAddress(((Hotel) backUp).getAddress());
            ((Hotel)listing).setFloors(((Hotel) backUp).getFloors());
        }
        else if(listing instanceof HotelRoom){
            ((HotelRoom)listing).setArea(((HotelRoom) backUp).getArea());
            ((HotelRoom)listing).setFloor(((HotelRoom) backUp).getFloor());
            ((HotelRoom)listing).setBeds(((HotelRoom) backUp).getBeds());
        }
    }
    public static void main(String[] args){
        String[] photos = {"empty"};
        HashSet<Features> features = new HashSet<>();
        features.add(Features.PROXIMITY);
        features.add(Features.WIFI);
        new AccommodationsEditor(new Apartment(
                "Test",
                photos,
                "cool apartment",
                features,
                "11 Downing Street",
                50,
                3,
                "Asimakinho"
        ),new Provider(
                "manolakis",
                "222",
                "Manolakis Manoliou",
                "ekei",
                "6989704212",
                "pera",
                true
        ), new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}

