package GUI;

import data.Database;
import userTypes.*;
import userTypes.Customer;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * EditMode
 * This frame allows users to edit their uploaded characteristics/info
 * Supports textFields to hold the new information and buttons to absorb these inputs and update
 * the corresponding user object
 */
public class EditMode extends JFrame{
    private JPanel mainEditPanel;
    private JButton saveNameButton;
    private JButton savePhoneNumberButton;
    private JButton saveAddressButton;
    private JTextField nameTextField;
    private JTextField phoneNumberTextField;
    private JTextField addressTextField;
    private JButton saveAddressHQButton;
    private JTextField addressHQTextField;
    private JButton backButton;

    public EditMode(User user, Database dataReference){
        super();
        this.setTitle("Edit Profile");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainEditPanel);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(400, 200));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();

        nameTextField.setText(user.getName());
        phoneNumberTextField.setText(user.getPhoneNumber());
        addressTextField.setText(user.getAddress());
        saveAddressHQButton.setVisible(false);
        addressHQTextField.setVisible(false);

        if(user instanceof Provider){
            addressHQTextField.setText(((Provider)user).getAddressHQ());
            addressHQTextField.setVisible(true);
            saveAddressHQButton.setVisible(true);
        }

        saveNameButton.addActionListener(e ->
                user.setName(nameTextField.getText())
        );
        savePhoneNumberButton.addActionListener(e ->
                user.setPhoneNumber(phoneNumberTextField.getText())
        );
        saveAddressButton.addActionListener(e ->
                user.setAddress(addressTextField.getText())
        );
        saveAddressHQButton.addActionListener(e -> {
            //Accessing this button when not a provider shouldn't be possible, but safeguard anyway
            if(user instanceof Provider)
                ((Provider)user).setAddressHQ(addressHQTextField.getText());
        });

        backButton.addActionListener(e ->{
            //push updated user to database
            if(user instanceof Customer)
                dataReference.pushRegisteredCustomer((Customer)user);
            else if(user instanceof Provider)
                dataReference.pushRegisteredProvider((Provider)user);
            new Profiler(user, dataReference);
            super.dispose();
        });
    }
    public static void main (String[] args){
        new EditMode(new Customer(
                "Asimakinho",
                "1234",
                "Asimakis Kydros",
                "Galdemi 11",
                "6989704212",
                true
        ), new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}
