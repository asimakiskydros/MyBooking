package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import userTypes.*;
import data.Database;

/**
 * UsersBrowser
 * This frame allows admins to view and search registered users, be it customers, providers, or other admins
 * On call, summons and displays all registered users in a list, if there exist none it displays a "dead" message
 * Supports button to search users by username and display their profile in a second list
 * After finding a user, buttons to admit them (if they haven't been already)
 * and to send them a message (by jumping to Messenger) appear
 */
public class UsersBrowser extends JFrame{
    private JPanel mainBrowserPanel;
    private JList<String> usersDisplayList;
    private JList<String> userProfileList;
    private JTextField inputUserTextField;
    private JButton searchButton;
    private JButton admitUserButton;
    private JButton sendMessageButton;
    private JLabel warningLabel;
    private JLabel registeredUsersLabel;
    private JPanel userProfileLabel;
    private JLabel searchLabel;
    private JButton backButton;

    public UsersBrowser(Admin administrator, Database dataReference){
        super();
        this.setTitle("Browse Users");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(800,600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainBrowserPanel);
        this.pack();

        admitUserButton.setVisible(false);
        sendMessageButton.setVisible(false);
        warningLabel.setVisible(false);
        userProfileList.setVisible(false);
        userProfileLabel.setVisible(false);

        //fetch all users
        HashSet<Customer> tempCustomers = dataReference.getRegisteredCustomers();
        HashSet<Provider> tempProviders = dataReference.getRegisteredProviders();
        HashSet<Admin> tempAdmins = dataReference.getAdmins();

        //if not null, feed to model
        DefaultListModel<String> modelAllUsers = new DefaultListModel<>();
        if(tempCustomers != null)
            for(Customer person : tempCustomers)
                modelAllUsers.addElement(person.getUsername() + " [Customer]");
        if(tempProviders != null)
            for(Provider person : tempProviders)
                modelAllUsers.addElement(person.getUsername() + " [Provider]");
        if(tempAdmins != null)
            for(Admin person : tempAdmins)
                modelAllUsers.addElement(person.getUsername() + " [Admin]");

        //if no users exist
        if(tempCustomers == null && tempAdmins == null && tempProviders == null){
            //show "dead" screen
            usersDisplayList.setVisible(false);
            registeredUsersLabel.setText("No registered users to show");
            inputUserTextField.setVisible(false);
            searchButton.setVisible(false);
            searchLabel.setVisible(false);
        }
        else{
            //set userList model
            usersDisplayList.setModel(modelAllUsers);

            //set empty model (at first)
            userProfileList.setModel(new DefaultListModel<>());
        }

        searchButton.addActionListener(e -> {
            //refresh
            warningLabel.setVisible(false);
            sendMessageButton.setVisible(false);
            admitUserButton.setVisible(false);

            //search user
            User result = Admin.searchUsers(inputUserTextField.getText(),dataReference);

            if(result == null)
                //search failed; user does not exist
                warningLabel.setVisible(true);
            else{
                //reveal buttons
                sendMessageButton.setVisible(true);
                if(!result.getAdmitted())
                    admitUserButton.setVisible(true);

                //project result's profile to second list
                DefaultListModel<String> modelProfile = new DefaultListModel<>();
                String rank;
                if(result instanceof Customer) rank = " [Customer]";
                else if(result instanceof Provider) rank = " [Provider]";
                else rank = " [Admin]";

                modelProfile.addElement("Username: " + result.getUsername() + rank);
                modelProfile.addElement("Name: " + result.getName());
                modelProfile.addElement("Phone Number: " + result.getPhoneNumber());
                modelProfile.addElement("Address: " + result.getAddress());
                if(result instanceof Provider)
                    modelProfile.addElement("Address HQ: " + ((Provider)result).getAddressHQ());

                userProfileList.setModel(modelProfile);
                userProfileList.setVisible(true);

                admitUserButton.addActionListener(d -> {
                    result.setAdmitted(true);
                    if(result instanceof Customer)
                        dataReference.pushRegisteredCustomer((Customer) result);
                    else if(result instanceof Provider)
                        dataReference.pushRegisteredProvider((Provider) result);
                    //hide button, no longer needed
                    admitUserButton.setVisible(false);
                });
                sendMessageButton.addActionListener(g ->
                        new Messenger(administrator, result, dataReference));
            }
        });
        backButton.addActionListener(e -> {
                new Menu(administrator, dataReference);
                super.dispose();
        });
    }
    public static void main(String[] args){
        new UsersBrowser(new Admin(
                "argiris",
                "123",
                " edw",
                "Argirakis Argiriou",
                "6989704212"
        ), new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}
