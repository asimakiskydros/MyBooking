package GUI;

import data.Database;
import userTypes.Admin;
import userTypes.Customer;
import accommodationTypes.Accommodation;
import javax.swing.*;
import java.awt.*;
import misc.MyPair;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * BookingsHistoryViewer
 * This frame displays to the user their active reservations and their past cancellations
 * If the user hasn't made any reservations yet and/or hasn't cancelled any bookings, it displays the corresponding
 * "dead" message
 * Supports double-clicking on reservationList components, AKA active reservations, to cancel them
 * (this action is not reversible)
 */
public class BookingsHistoryViewer extends JFrame{
    private JPanel mainHistoryPanel;
    private JLabel cancellationsSignalLabel;
    private JLabel reservationsSignalLabel;
    private JList<String> reservationsList;
    private JList<String> cancellationsList;
    private JLabel feedbackLabel;
    private JButton backButton;

    public BookingsHistoryViewer(Customer person, Database dataReference){
        super();
        this.setTitle("Bookings Info");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(700,500));
        this.setLocationRelativeTo(null);
        this.setContentPane(mainHistoryPanel);
        this.pack();

        //fetch reservation/cancellation info
        HashMap<MyPair, String> tempReservations = person.getReservations();
        HashSet<String> tempCancellations = person.getCancellations();

        //reservations side of window
        if(tempReservations.isEmpty()){
            reservationsList.setVisible(false);
            reservationsSignalLabel.setVisible(true);
            feedbackLabel.setVisible(false);
            reservationsSignalLabel.setText("No active reservations");
        }
        else{
            reservationsList.setVisible(true);
            reservationsSignalLabel.setVisible(true);
            reservationsSignalLabel.setText("Active Reservations:");
            feedbackLabel.setText("Cancel a booking by double clicking it");

            //update list
            reservationsList.setModel(refreshReservations(tempReservations,person));
        }
        //cancellations side of window
        if(tempCancellations.isEmpty()){
            cancellationsList.setVisible(false);
            cancellationsSignalLabel.setVisible(true);
            cancellationsSignalLabel.setText("No active cancellations");
        }
        else{
            cancellationsList.setVisible(true);
            cancellationsSignalLabel.setVisible(true);
            cancellationsSignalLabel.setText("Active Cancellations:");

            //update list
            cancellationsList.setModel(refreshCancellations(tempCancellations));
        }
        MouseListener mouseListener = new MouseAdapter() {
            @SuppressWarnings("StringConcatenationInLoop")
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!reservationsList.isSelectionEmpty() &&  e.getClickCount() == 2){
                    //get selected reservation
                    String selection = "";
                    char[] selectionChars = reservationsList.getSelectedValue().toCharArray();
                    //remove dates info, keep the name
                    //start at index 2, as the first two characters are always "> "
                    //end concatenation when ',' reached
                    int letter = 2;
                    while(selectionChars[letter] != ',')
                            selection += ((Character)selectionChars[letter++]).toString();

                    //find and delete it from central database
                    Accommodation cancellation = Admin.searchAccommodations(selection, dataReference);
                    assert cancellation != null;
                    if(person.cancelReservation(cancellation, dataReference)) {
                        feedbackLabel.setText(">>> Reservation cancelled");

                        //update lists
                        reservationsList.setModel(refreshReservations(tempReservations, person));
                        cancellationsList.setModel(refreshCancellations(tempCancellations));
                    }
                    else
                        feedbackLabel.setText("(!) Cancellation failed");
                }
            }
        };
        reservationsList.addMouseListener(mouseListener);

        backButton.addActionListener(e ->{
            new Profiler(person, dataReference);
            super.dispose();
        });
    }
    /**
     * Info refresher for reservationsList
     * @param newMap the new reservations info
     * @param person the customer in question
     * @return new model of the formatted into strings reservations of user, to be pushed to reservationsList
     */
    private DefaultListModel<String> refreshReservations(HashMap<MyPair, String> newMap, Customer person){
        DefaultListModel<String> modelReservations = new DefaultListModel<>();
        //push all reservations into model in string format
        for(MyPair dates : newMap.keySet()) {
            String reservation = newMap.get(dates);
            modelReservations.addElement(person.printReservation(reservation, dates.getHead(), dates.getValue()));
        }
        return modelReservations;
    }
    /**
     * Info refresher for cancellationsList
     * @param newSet the new cancellations info
     * @return new model of the formatted into strings cancellations of user, to be pushed into cancellationsList
     */
    private DefaultListModel<String> refreshCancellations(HashSet<String> newSet){
        DefaultListModel<String> modelCancellations = new DefaultListModel<>();
        //push all reservations into model in string format
        for(String cancellation : newSet)
            modelCancellations.addElement("> " + cancellation);

        return modelCancellations;
    }
    @SuppressWarnings("DuplicatedCode")
    public static void main (String[] args){
        //test
        Customer temp = new Customer(
                "Asimakinho",
                "1234",
                "Asimakis Kydros",
                "11 Downing Street",
                "6989704212",
                true
        );

        HashMap<MyPair, String> testReservations = new HashMap<>();
        testReservations.put(new MyPair(20210711, 20210720), "Ios Relax Hotel");
        testReservations.put(new MyPair(20210821, 20210830), "Paros Resort");
        testReservations.put(new MyPair(20220101, 20220201), "Elpida Hotel");
        temp.setReservations(testReservations);

        HashSet<String> testCancellations = new HashSet<>();
        testCancellations.add("Touzla");
        testCancellations.add("Cool Apartment by the Beach");
        temp.setCancellations(testCancellations);

        new BookingsHistoryViewer(temp, new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }

}
