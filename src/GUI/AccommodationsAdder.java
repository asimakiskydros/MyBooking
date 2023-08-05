package GUI;

import javax.swing.*;
import accommodationTypes.*;
import misc.Features;
import userTypes.Admin;
import userTypes.Provider;
import data.Database;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;

/**
 * AccommodationsAdder
 * This frame allows providers to upload their owned accommodations to the app
 * Supports textFields to insert main characteristics and checkboxes to specify features
 */
public class AccommodationsAdder extends JFrame{
    private JPanel mainAdderPanel;
    private JCheckBox freeWiFiCheckBox, parkingCheckBox, smokingAllowedCheckBox, petsAllowedCheckBox,
                        elevatorCheckBox, barCheckBox, poolCheckBox, spaCheckBox, proximityCheckBox;
    private JButton uploadButton;
    private JLabel feedbackLabel, warningLabel, askHotelLabel, addressLabel, areaLabel, roomsLabel,
                        bedroomsLabel, bathroomsLabel, bedsLabel, floorsLabel, atFloorLabel;
    private JTextField nameTextField, addressTextField, areaTextField, roomsTextField, bedroomsTextField,
                        bathroomsTextField, bedsTextField, floorsTextField, hotelTextField, atFloorTextField;
    private JTextArea descTextArea;
    private JRadioButton apartmentRadioButton, maisonetteRadioButton, hotelRadioButton, hotelRoomRadioButton;
    private JButton backButton;

    public AccommodationsAdder(Provider owner, Database dataReference){
        super();
        this.setTitle("Add Accommodations");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(700, 600));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainAdderPanel);
        this.pack();

        toggleAskHotel(false);

        uploadButton.addActionListener(e -> {
            //get features
            HashSet<Features> features = new HashSet<>();
            if (freeWiFiCheckBox.isSelected()) features.add(Features.WIFI);
            if (parkingCheckBox.isSelected()) features.add(Features.PARKING);
            if (smokingAllowedCheckBox.isSelected()) features.add(Features.SMOKING);
            if (petsAllowedCheckBox.isSelected()) features.add(Features.PETS);
            if (elevatorCheckBox.isSelected()) features.add(Features.ELEVATOR);
            if (barCheckBox.isSelected()) features.add(Features.BAR);
            if (poolCheckBox.isSelected()) features.add(Features.POOL);
            if (spaCheckBox.isSelected()) features.add(Features.SPA);
            if (proximityCheckBox.isSelected()) features.add(Features.PROXIMITY);

            if(apartmentRadioButton.isSelected()){
                //get info
                String[] info = new String[7];
                info[0] = nameTextField.getText();
                info[1] = descTextArea.getText();
                info[2] = addressTextField.getText();
                info[3] = areaTextField.getText();
                info[4] = roomsTextField.getText();
                info[5] = bedroomsTextField.getText();
                info[6] = bathroomsTextField.getText();

                if(validateSafeInputs(info, 3, dataReference)){
                    //create apartment
                    String[] photos = {"empty"};
                    Apartment apartment = new Apartment(
                            info[0], //name
                            photos,  //photos
                            info[1], //description
                            features,//features
                            info[2], //address
                            Integer.parseInt(info[3]), //area
                            Integer.parseInt(info[4]), //Number of rooms
                            owner.getUsername() //owner's name
                    );
                    apartment.setBedrooms(Integer.parseInt(info[5])); //Number of bedrooms
                    apartment.setBathrooms(Integer.parseInt(info[6])); //Number of bathrooms

                    //push it to database
                    if(owner.addAccommodation(apartment, dataReference)) feedbackLabel.setVisible(true);
                }
                else{
                    warningLabel.setText("(!) Wrong inputs detected");
                    warningLabel.setVisible(true);
                }
            }
            else if(maisonetteRadioButton.isSelected()){
                //get info
                String[] info = new String[8];
                info[0] = nameTextField.getText();
                info[1] = descTextArea.getText();
                info[2] = addressTextField.getText();
                info[3] = areaTextField.getText();
                info[4] = roomsTextField.getText();
                info[5] = floorsTextField.getText();
                info[6] = bathroomsTextField.getText();
                info[7] = bedroomsTextField.getText();

                if(validateSafeInputs(info, 3, dataReference)){
                    //create maisonette
                    String[] photos = {"empty"};
                    Maisonette maisonette = new Maisonette(
                            info[0], //name
                            photos,  //photos
                            info[1], //description
                            features,//features
                            info[2], //address
                            Integer.parseInt(info[3]), //area
                            Integer.parseInt(info[4]), //Number of rooms
                            Integer.parseInt(info[5]), //Number of floors
                            owner.getUsername() //owner's name
                    );
                    maisonette.setBathrooms(Integer.parseInt(info[6]));
                    maisonette.setBedrooms(Integer.parseInt(info[7]));

                    //push it to database
                    if(owner.addAccommodation(maisonette, dataReference)) feedbackLabel.setVisible(true);
                }
                else{
                    warningLabel.setText("(!) Wrong inputs detected");
                    warningLabel.setVisible(true);
                }
            }
            else if(hotelRadioButton.isSelected()){
                String[] info = new String[4];
                info[0] = nameTextField.getText();
                info[1] = descTextArea.getText();
                info[2] = addressTextField.getText();
                info[3] = floorsTextField.getText();

                if(validateSafeInputs(info, 3, dataReference)){
                    //create hotel
                    String[] photos = {"empty"};
                    Hotel hotel = new Hotel(
                            info[0], //name
                            photos,  //photos
                            info[1], //description
                            features,//features
                            new HashSet<>(), //rooms
                            info[2], //address
                            Integer.parseInt(info[3]), //Number of floors
                            owner.getUsername() //owner's name
                    );
                    //push it to database
                    if(owner.addAccommodation(hotel, dataReference)) feedbackLabel.setVisible(true);
                }
                else{
                    warningLabel.setText("(!) Wrong inputs detected");
                    warningLabel.setVisible(true);
                }
            }
            else if(hotelRoomRadioButton.isSelected()){
                //special case: get hotel first
                String hotel = hotelTextField.getText();
                HashSet<Accommodation> tempAccommodations = dataReference.getRegisteredAccommodations();
                //search for said hotel
                Hotel boundHotel = null;
                if(tempAccommodations != null)
                    for(Accommodation accommodation : tempAccommodations)
                        if( accommodation instanceof Hotel &&
                            Objects.equals(accommodation.getName(), hotel) &&
                            Objects.equals(accommodation.getOwner(), owner.getUsername())){
                            //the provider attempting to add the new hotel room must own the whole hotel
                            boundHotel = (Hotel)accommodation;
                            break;
                        }
                if(boundHotel != null){
                    //proceed as before
                    String[] info = new String[5];
                    info[0] = nameTextField.getText();
                    info[1] = descTextArea.getText();
                    info[2] = atFloorTextField.getText();
                    info[3] = areaTextField.getText();
                    info[4] = bedsTextField.getText();

                    if(validateSafeInputs(info, 2, dataReference)){
                        //create hotel room
                        String[] photos = {"empty"};
                        HotelRoom hotelRoom = new HotelRoom(
                                //final room name: "Hotel_Name: room_name"
                                boundHotel.getName() +": " + info[0],
                                photos,  //photos
                                info[1],  //description
                                features, //features
                                Integer.parseInt(info[2]), //#floor situated
                                Integer.parseInt(info[3]), //area
                                Integer.parseInt(info[4]), //Number of beds
                                owner.getUsername() //owner's name
                        );
                        //push hotel room to its bound hotel
                        if(boundHotel.addRoom(hotelRoom)) {
                            //update central database
                            dataReference.pushRegisteredAccommodation(boundHotel);
                            feedbackLabel.setVisible(true);
                        }
                        else{
                            warningLabel.setText("(!) Upload failed");
                            warningLabel.setVisible(true);
                        }
                    }
                    else{
                        warningLabel.setText("(!) Wrong inputs detected");
                        warningLabel.setVisible(true);
                    }
                }
                else{
                    warningLabel.setText("(!)(!) Hotel not found");
                    warningLabel.setVisible(true);
                }
            }
        });
        apartmentRadioButton.addActionListener(e -> {
            if(apartmentRadioButton.isSelected()){
                toggleAskHotel(false);
                //prohibit user from choosing more than one accommodation types at once
                maisonetteRadioButton.setSelected(false);
                hotelRadioButton.setSelected(false);
                hotelRoomRadioButton.setSelected(false);

                //show only demanded text fields for each type
                toggleLabelTextField(addressLabel, addressTextField, true);
                toggleLabelTextField(areaLabel, areaTextField, true);
                toggleLabelTextField(roomsLabel, roomsTextField, true);
                toggleLabelTextField(bedroomsLabel, bedroomsTextField, true);
                toggleLabelTextField(bathroomsLabel, bathroomsTextField, true);
                toggleLabelTextField(bedsLabel, bedsTextField, false);
                toggleLabelTextField(floorsLabel, floorsTextField, false);
                toggleLabelTextField(atFloorLabel, atFloorTextField, false);
            }
        });
        maisonetteRadioButton.addActionListener(e -> {
            if(maisonetteRadioButton.isSelected()) {
                toggleAskHotel(false);

                apartmentRadioButton.setSelected(false);
                hotelRadioButton.setSelected(false);
                hotelRoomRadioButton.setSelected(false);

                toggleLabelTextField(addressLabel, addressTextField, true);
                toggleLabelTextField(areaLabel, areaTextField, true);
                toggleLabelTextField(roomsLabel, roomsTextField, true);
                toggleLabelTextField(bedroomsLabel, bedroomsTextField, true);
                toggleLabelTextField(bathroomsLabel, bathroomsTextField, true);
                toggleLabelTextField(bedsLabel, bedsTextField, false);
                toggleLabelTextField(floorsLabel, floorsTextField, true);
                toggleLabelTextField(atFloorLabel, atFloorTextField, false);
            }
        });
        hotelRadioButton.addActionListener(e -> {
            if(hotelRadioButton.isSelected()){
                toggleAskHotel(false);

                maisonetteRadioButton.setSelected(false);
                apartmentRadioButton.setSelected(false);
                hotelRoomRadioButton.setSelected(false);

                toggleLabelTextField(addressLabel, addressTextField, true);
                toggleLabelTextField(areaLabel, areaTextField, false);
                toggleLabelTextField(roomsLabel, roomsTextField, false);
                toggleLabelTextField(bedroomsLabel, bedroomsTextField, false);
                toggleLabelTextField(bathroomsLabel, bathroomsTextField, false);
                toggleLabelTextField(bedsLabel, bedsTextField, false);
                toggleLabelTextField(floorsLabel, floorsTextField, true);
                toggleLabelTextField(atFloorLabel, atFloorTextField, false);
            }
        });
        hotelRoomRadioButton.addActionListener(e -> {
            if(hotelRoomRadioButton.isSelected()){
                toggleAskHotel(true);

                maisonetteRadioButton.setSelected(false);
                hotelRadioButton.setSelected(false);
                apartmentRadioButton.setSelected(false);

                toggleLabelTextField(addressLabel, addressTextField, false);
                toggleLabelTextField(areaLabel, areaTextField, true);
                toggleLabelTextField(roomsLabel, roomsTextField, false);
                toggleLabelTextField(bedroomsLabel, bedroomsTextField, false);
                toggleLabelTextField(bathroomsLabel, bathroomsTextField, false);
                toggleLabelTextField(bedsLabel, bedsTextField, true);
                toggleLabelTextField(floorsLabel, floorsTextField, false);
                toggleLabelTextField(atFloorLabel, atFloorTextField, true);
            }
        });

        backButton.addActionListener(e ->{
            new Menu(owner, dataReference);
            super.dispose();
        });
    }
    /**
     * Toggle so that every time a provider selects to add a hotel room,
     * the program asks for its bound hotel name,
     * and when the provider selects something else, the program erases the message
     * @param isActive whether activation of the textField to ask is desired or not
     */
    private void toggleAskHotel(boolean isActive){
        hotelTextField.setVisible(isActive);
        askHotelLabel.setVisible(isActive);
        //refresh labels
        warningLabel.setVisible(false);
        feedbackLabel.setVisible(false);
    }
    /**
     * Safe input informer
     * "Battle-tests" parameter info for empty inputs and NumberFormatException situations
     * @param info the string array holding the data wished tested
     * @param indexIntegers the position in the array where from then on every input should be an integer
     * @return true if all tests were passed successfully
     */
    private boolean validateSafeInputs(String[] info, int indexIntegers, Database dataReference){
        boolean isSafe = true;
        //check for blank or empty strings
        for (String s : info)
            if (s.isEmpty() || s.isBlank()) {
                isSafe = false;
                break;
            }
        //check if name is already in use
        if(isSafe && Admin.searchAccommodations(info[0], dataReference) != null)
            isSafe = false;
        //check for wrong inputs
        for(int i = indexIntegers; i < info.length && isSafe; i++) {
            try {
                Integer.parseInt(info[i]);
            } catch (NumberFormatException d) {
                isSafe = false;
            }
        }
        return isSafe;
    }
    /**
     * Activation toggle so that labels and fields that are bound together can be (de)activated simultaneously
     * @param label JLabel bound to field
     * @param field JTextField bound to label
     * @param isActive whether the above should appear or not
     */
    private void toggleLabelTextField(JLabel label, JTextField field, boolean isActive){
        field.setVisible(isActive);
        label.setVisible(isActive);
    }
    public static void main(String[] args){
        Provider owner = new Provider(
                "yes",
                "no",
                "maybe",
                "perhaps",
                "true",
                "false",
                true
        );
        new AccommodationsAdder(owner,
                new Database(
                        "src" + File.separator + "data" + File.separator + "files" + File.separator
                ));
    }
}
