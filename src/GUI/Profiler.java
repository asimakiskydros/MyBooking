package GUI;

import userTypes.*;
import misc.MyPair;
import data.Database;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;

/**
 * Profiler
 * This frame implements the profile display tab of the user
 * On call, displays user's info, image, e.t.c.
 * Supports buttons to jump to EditMode, MessageReader,
 *  BookingsHistoryViewer (for customers) and ListingsDisplayer (for providers)
 */
public class Profiler extends JFrame{
    private JButton editButton;
    private JButton seeBookingsButton;
    private JButton seeMessagesButton;
    private JPanel imagePanel;
    private JPanel mainCustomerPanel;
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel nameLabel;
    private JLabel userTypeLabel;
    private JLabel phoneNumberLabel;
    private JLabel addressLabel;
    private JLabel bookingsHistoryLabel;
    private JLabel unreadMessagesLabel;
    private JLabel addressHQLabel;
    private JButton seeMyListingsButton;
    private JButton backButton;

    public Profiler(User person, Database dataReference){
        super();
        this.setTitle("My Profile");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(700, 500));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainCustomerPanel);
        this.pack();

        imagePanel.setVisible(false);
        imageLabel.setVisible(false);
        initializeDisplayer(person,dataReference);

        editButton.addActionListener(e -> {
            new EditMode(person, dataReference);
            super.dispose();
            //refresh the page
            initializeDisplayer(person,dataReference);
        });
        seeBookingsButton.addActionListener(e -> {
            new BookingsHistoryViewer((Customer)person, dataReference);
            super.dispose();
            //refresh the page
            initializeDisplayer(person, dataReference);
        });
        seeMessagesButton.addActionListener(e -> {
            new MessageReader(person);
            //refresh the page
            initializeDisplayer(person,dataReference);
        });
        seeMyListingsButton.addActionListener(e ->{
            new ListingsDisplayer((Provider)person, dataReference);
            super.dispose();
            });

        backButton.addActionListener(e ->{
            new Menu(person, dataReference);
            super.dispose();
        });
    }
    /**
     * Info refresher
     * updates labels with the new user's info
     * @param person the user in question that holds the updated info
     * @param dataReference the central database
     */
    private void initializeDisplayer(User person,Database dataReference){
        usernameLabel.setText(person.getUsername());
        nameLabel.setText("Name: " + person.getName());
        phoneNumberLabel.setText("Phone Number: " + person.getPhoneNumber());
        addressLabel.setText("Address: " + person.getAddress());
        unreadMessagesLabel.setText("Has " + person.getMessages().size() + " messages");
        if(person instanceof Customer) {
            userTypeLabel.setText("Registered as: Customer");
            addressHQLabel.setVisible(false);
            seeBookingsButton.setVisible(true);
            seeMyListingsButton.setVisible(false);
            bookingsHistoryLabel.setText("Has " + ((Customer)person).getReservations().size() + " active reservations, "
                            + ((Customer)person).getCancellations().size() + " active cancellations"
                    );
        }
        else if(person instanceof Provider){
            userTypeLabel.setText("Registered as: Provider");
            addressHQLabel.setVisible(true);
            seeBookingsButton.setVisible(false);
            seeMyListingsButton.setVisible(true);
            addressHQLabel.setText("HQ: " + ((Provider)person).getAddressHQ());
            bookingsHistoryLabel.setText("Total reservation score: " +
                            ((Provider)person).getTotalReservations(dataReference) +
                            ", Total cancellation score: " + ((Provider)person).getTotalCancellations(dataReference)
                    );
        }
    }
    @SuppressWarnings({"DuplicatedCode", "CommentedOutCode"})
    public static void main(String[] args){
        Customer tempUser = new Customer(
                "Asimakinho",
                "1234",
                "Asimakis Kydros",
                "Galdemi 11",
                "6989704212",
                true
        );
        HashMap<String, String> tempMessage = new HashMap<>();
        tempMessage.put("Hello asimakis, this is a message", "Myself");
        tempMessage.put("Hello asimakis, this is another message", "Yourself");
        tempUser.setMessages(tempMessage);

        HashMap<MyPair, String> testReservations = new HashMap<>();
        testReservations.put(new MyPair(20210711, 20210720), "Ios Relax Hotel");
        testReservations.put(new MyPair(20210821, 20210830), "Paros Resort");
        testReservations.put(new MyPair(20220101, 20220201), "Elpida Hotel");
        tempUser.setReservations(testReservations);

        HashSet<String> testCancellations = new HashSet<>();
        testCancellations.add("Touzla");
        testCancellations.add("Cool Apartment by the Beach");
        tempUser.setCancellations(testCancellations);

        new Profiler(tempUser, new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));

//        Provider tempOwner = new Provider(
//                "grigorakis123",
//                "hehexd",
//                "Grigoris Grigoridis",
//                "Grigoriou 1256",
//                "6989794213",
//                "Silicon Valley, LA",
//                true
//        );
//        HashSet<String> tempListings = new HashSet<>();
//        tempListings.add("Cool house by the beach");
//        tempListings.add("My house");
//        tempListings.add("A listing");
//
//        tempOwner.setOwnedAccommodations(tempListings);
//
//        new Profiler(tempOwner, new Database(
//                          "src" + File.separator + "data" + File.separator + "files" + File.separator
//        ));
    }
}
