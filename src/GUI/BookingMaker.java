package GUI;

import accommodationTypes.*;
import data.Database;
import misc.DateSystem;
import misc.Features;
import userTypes.Customer;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;

/**
 * BookingMaker
 * This frame projects the accommodation's profile to the customer and allows him to make a reservation
 * Customer must enter desired check-in, check-out dates to textFields, which are then turned into integers and
 * compared with pre-existing bookings for the same reservation. If none overlap, reservation completes.
 */
public class BookingMaker extends JFrame{
    private JPanel mainBookingPanel;
    private JTextField checkOutTextField, checkInTextField;
    private JButton bookButton, updateButton;
    private JLabel feedbackLabel, wifiLabel, parkingLabel, smokingLabel, petsLabel, elevatorLabel,
                    barLabel, poolLabel, spaLabel, proximityLabel;
    private JLabel imageLabel, nameLabel, descriptionLabel, addressLabel, areaLabel, roomsLabel, bedroomsLabel,
                    bathroomsLabel, bedsLabel, floorsLabel, atFloorLabel;
    private JComboBox<String> monthComboBox, roomsCheckBox;
    private JComboBox<Integer> yearComboBox;
    private JList<String> roomsList;
    private JLabel selectRoomLabel, roomsListDisplayLabel;
    private JButton backButton;
    private JTable calendarTable;
    DateSystem dateManager = new DateSystem();

    public BookingMaker(Accommodation listing, Customer person, Database dataReference){
        super();
        this.setTitle("View and Book Accommodation");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(800 ,600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainBookingPanel);
        this.pack();

        roomsList.setVisible(false);
        selectRoomLabel.setVisible(false);
        roomsCheckBox.setVisible(false);
        roomsListDisplayLabel.setVisible(false);
        feedbackLabel.setVisible(false);
        imageLabel.setVisible(false);

        //feed listing info to corresponding labels
        nameLabel.setText("Name: " + listing.getName());
        descriptionLabel.setText("<html><p style=\"width:100px\">"+listing.getDescription()+"</p></html>");

        if(listing instanceof Apartment){
            addressLabel.setText("Address: " + ((Apartment)listing).getAddress());
            areaLabel.setText("Area: " + (((Apartment)listing).getArea()).toString());
            roomsLabel.setText("Rooms: " + (((Apartment)listing).getRooms()).toString());
            bedroomsLabel.setText("Bedrooms: " + (((Apartment)listing).getBedrooms()).toString());
            bathroomsLabel.setText("Bathrooms: " + (((Apartment)listing).getBathrooms()).toString());

            //hide irrelevant labels
            bedsLabel.setVisible(false);
            floorsLabel.setVisible(false);
            atFloorLabel.setVisible(false);
        }
        else if(listing instanceof Maisonette){
            addressLabel.setText("Address: " + ((Maisonette)listing).getAddress());
            areaLabel.setText("Area: " + (((Maisonette)listing).getArea()).toString());
            roomsLabel.setText("Rooms: " + (((Maisonette)listing).getRooms()).toString());
            bedroomsLabel.setText("Bedrooms: " + (((Maisonette)listing).getBedrooms()).toString());
            bathroomsLabel.setText("Bathrooms: " + (((Maisonette)listing).getBathrooms()).toString());
            floorsLabel.setText("Floors: " + (((Maisonette)listing).getFloors()).toString());

            //hide irrelevant labels
            bedsLabel.setVisible(false);
            atFloorLabel.setVisible(false);
        }
        else if(listing instanceof Hotel){
            addressLabel.setText("Address: " + ((Hotel)listing).getAddress());
            floorsLabel.setText("Floors: " + (((Hotel)listing).getFloors()).toString());

            //hide irrelevant labels
            bedsLabel.setVisible(false);
            atFloorLabel.setVisible(false);

            //show extra panels
            roomsList.setVisible(true);
            selectRoomLabel.setVisible(true);
            roomsCheckBox.setVisible(true);
            roomsListDisplayLabel.setVisible(true);

            //feed room info to roomsList and roomsCheckBox
            DefaultListModel<String> roomsModel = new DefaultListModel<>();
            HashSet<HotelRoom> rooms = ((Hotel)listing).getRooms();
            for(HotelRoom room : rooms) {
                //Format: A1, @2 floor, 3 beds
                roomsModel.addElement(room.getName() + ", @" + room.getFloor() + " floor, " + room.getBeds() + " beds");
                roomsCheckBox.addItem(room.getName());
            }
            roomsList.setModel(roomsModel);
        }
        //fetch features
        HashSet<Features> features = listing.getFeatures();
        //show only the labels pointing to the features that are supported
        if(!features.contains(Features.WIFI)) wifiLabel.setVisible(false);
        if(!features.contains(Features.PARKING)) parkingLabel.setVisible(false);
        if(!features.contains(Features.SMOKING)) smokingLabel.setVisible(false);
        if(!features.contains(Features.PETS)) petsLabel.setVisible(false);
        if(!features.contains(Features.ELEVATOR)) elevatorLabel.setVisible(false);
        if(!features.contains(Features.BAR)) barLabel.setVisible(false);
        if(!features.contains(Features.POOL)) poolLabel.setVisible(false);
        if(!features.contains(Features.SPA)) spaLabel.setVisible(false);
        if(!features.contains(Features.PROXIMITY)) proximityLabel.setVisible(false);

        //initialize year, month checkBoxes
        String[] months = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };
        for(String month : months)
            monthComboBox.addItem(month);
        for(int year = 1971; year < 2038; year++)
            yearComboBox.addItem(year);

        updateButton.addActionListener(e ->
                //update list model
                calendarTable.setModel(dateManager.getCalendarModel(
                        yearComboBox.getItemAt(yearComboBox.getSelectedIndex()),
                        monthComboBox.getItemAt(monthComboBox.getSelectedIndex())
                )
        ));
        bookButton.addActionListener(e -> {
            try{
                //Making a booking in a hotel means that you essentially book a hotelRoom,
                //thus if the listing is a Hotel type, get user input from roomsCheckBox and find room
                Accommodation accommodation = null;
                if(listing instanceof Hotel) {
                    HashSet<HotelRoom> tempRooms = ((Hotel) listing).getRooms();
                    String choiceRoom = roomsCheckBox.getItemAt(roomsCheckBox.getSelectedIndex());
                    for(HotelRoom room : tempRooms)
                        if(Objects.equals(choiceRoom, room.getName())) {
                            accommodation = room;
                            break;
                        }
                }
                else accommodation = listing;

                assert accommodation != null;
                if(person.addReservation(
                        accommodation,
                        //get dates
                        unformatDate(checkInTextField.getText(), dateManager), //check-in date
                        unformatDate(checkOutTextField.getText(), dateManager), //check-out date
                        dataReference
                ))
                    feedbackLabel.setText("Booking completed successfully");
                else
                    //reservation already booked during this time by a third party
                    feedbackLabel.setText("(!) Specified timeframe already occupied");
            }catch(NumberFormatException d){
                //user gave garbage or format was wrong by mistake
                feedbackLabel.setText("(!)(!) Unknown date format given");
            }
            feedbackLabel.setVisible(true);
        });

        backButton.addActionListener(e ->{
            new AccommodationsBrowser(person, dataReference);
            super.dispose();
        });
    }
    /**
     * Function to dissect a string-date into date/month/year ints
     * Expects DD/MM/YYYY format to work properly
     * Pseudo-checks the dissected strings for wrong input (parseInt throws exception)
     * @param inputDate the date into string format DD/MM/YYYY
     * @param manager the DateSystem which manages date info
     * @return the corresponding date code for the string-date
     * @throws NumberFormatException if string was not of correct format/was not a date at all (parseInt fails)
     */
    @SuppressWarnings("StringConcatenationInLoop")
    private int unformatDate(String inputDate, DateSystem manager) throws NumberFormatException{
        char[] ddmmyyyy = inputDate.toCharArray();
        String date = "", month = "", year = "";

        int letter = 0;
        //DD/MM/YYYY format expected
        for(; letter < ddmmyyyy.length && ddmmyyyy[letter] != '/'; letter++)
            date += ((Character)ddmmyyyy[letter]).toString();
        for(++letter; letter < ddmmyyyy.length && ddmmyyyy[letter] != '/'; letter++)
            month += ((Character)ddmmyyyy[letter]).toString();
        for(++letter; letter < ddmmyyyy.length && ddmmyyyy[letter] != '/'; letter++)
            year += ((Character)ddmmyyyy[letter]).toString();

        return manager.getDateCode(
                Integer.parseInt(date),
                Integer.parseInt(month),
                Integer.parseInt(year)
        );
    }
    public static void main(String[] args){
        String[] photos = {"empty"};
        new BookingMaker(
                new Apartment(
                        "Test",
                        photos,
                        "cool apartment",
                        new HashSet<>(),
                        "11 Downing Street",
                        50,
                        3,
                        "Asimakinho"
                ),
                new Customer(
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
