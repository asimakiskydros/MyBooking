package GUI;

import data.Database;
import userTypes.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Menu
 * This frame implements the main menu of the app, allowing users to perform certain actions
 * Admins:
 *      First action --> Browse active reservations
 *      Second action --> Browse registered users
 *      Third action --> Read messages (others have that feature in their profiles)
 * Customers:
 *      First action --> Search accommodations
 *      Second action --> View profile
 *      Third action --> N/A
 * Providers:
 *      First action --> Add new accommodation
 *      Second action --> View profile
 *      Third action --> N/A
 *
 * Also supports "Log Out" button for all users
 */
public class Menu extends JFrame{
    private JPanel mainMenuPanel;
    private JButton firstActionButton;
    private JButton secondActionButton;
    private JLabel actionLabel;
    private JButton thirdActionButton;
    private JButton logOutButton;
    private JLabel titleLabel;

    public Menu(User person, Database dataReference){
        super();
        this.setTitle("Menu");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(700, 500));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainMenuPanel);
        this.pack();

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 35));
        actionLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

        if(person instanceof Customer) {
            thirdActionButton.setVisible(false);
            firstActionButton.setText("Search accommodations");
            secondActionButton.setText("View profile");
        }
        else if (person instanceof Provider) {
            thirdActionButton.setVisible(false);
            firstActionButton.setText("Add new accommodation");
            secondActionButton.setText("View profile");
        }

        firstActionButton.addActionListener(e -> {
            if(person instanceof Admin) {
                new ReservationsBrowser((Admin)person, dataReference);
                super.dispose();
            }
            else if(person instanceof Customer) {
                new AccommodationsBrowser((Customer) person, dataReference);
                super.dispose();
            }
            else if(person instanceof Provider) {
                new AccommodationsAdder((Provider) person, dataReference);
                super.dispose();
            }
        });
        secondActionButton.addActionListener(e -> {
            if(person instanceof Admin) {
                new UsersBrowser((Admin) person, dataReference);
            }
            else {
                new Profiler(person, dataReference);
            }
            super.dispose();
        });
        thirdActionButton.addActionListener(e ->
            new MessageReader(person)
        );
        logOutButton.addActionListener(e -> {
            new RegistrationLogin_MAIN(dataReference);
            super.dispose();
        });
    }
    @SuppressWarnings("CommentedOutCode")
    public static void main (String[] args){
//        new Menu(new Admin("argiris",
//                "123",
//                " edw",
//                "Argirakis Argiriou",
//                "6989704212"
//        ), new Database());
//        new Menu(new Customer(
//                "grigoris",
//                "111",
//                "Grigoris Grigoriou",
//                "edw",
//                "6989704212",
//                true
//        ), new Database());
        new Menu(new Provider(
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
