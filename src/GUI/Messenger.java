package GUI;

import javax.swing.*;
import data.Database;
import userTypes.*;
import java.awt.*;
import java.io.File;

/**
 * Messenger
 * This frame implements the "Send message" ability of admins
 * On call, a text panel allows admins to input their message
 * By pressing the "Send" button, the user's message hashmap is updated to hold the author's name and message
 */
public class Messenger extends JFrame{
    private JPanel mainMessengerPanel;
    private JTextArea inputMessageTextArea;
    private JButton sendMessageButton;
    private JLabel nameDisplayerLabel;

    public Messenger(Admin administrator, User person, Database dataReference){
        super();
        this.setTitle("Send Message");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(400,500));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainMessengerPanel);
        this.pack();

        nameDisplayerLabel.setText("Write message to " + person.getUsername() + ":");

        sendMessageButton.addActionListener(e -> {
            String message = inputMessageTextArea.getText();
            //if button was pressed with the text area being empty/only whitespaces, do nothing
            if(!message.isEmpty() && !message.isBlank())
                //send message
                administrator.sendMessage(message, person, dataReference);
                super.dispose();
        });
    }
    public static void main(String[] args){
        new Messenger(
                new Admin(
                    "argirakis",
                    "111",
                    "ekei",
                    "Argiris Argiridis",
                    "6970805367"
                ),
                new User(
                    "grigoris",
                    "123",
                    "edw",
                    "Grigoris Grigoridis",
                    "6989704212",
                    true
        ), new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}
