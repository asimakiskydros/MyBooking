package GUI;

import userTypes.Customer;
import userTypes.User;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * MessageReader
 * This frame implements the customer's or provider's message inbox
 * On call, summons and displays the user's messages in a list, if there exist none it displays a "dead" message
 * Supports button to clean all messages, simulating a "disregard" mechanic, cleaned messages are no longer available
 */
public class MessageReader extends JFrame{

    private JPanel mainMessageReaderPanel;
    private JLabel noMessagesSignalLabel;
    private JButton messageCleanerButton;
    private JList<String> messagesList;
    private JScrollPane messagesScrollPane;

    public MessageReader(User person){
        super();
        this.setTitle("Messages");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(450, 450));
        this.setContentPane(mainMessageReaderPanel);
        this.pack();

        HashMap<String, String> tempMessages = person.getMessages();
        messagesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        if(tempMessages.isEmpty()){
            //display only "no messages" signal, not the rest
            noMessagesSignalLabel.setVisible(true);
            messagesList.setVisible(false);
            messageCleanerButton.setVisible(false);
        }
        else{
            //the reverse
            noMessagesSignalLabel.setVisible(false);
            messageCleanerButton.setVisible(true);
            messagesList.setVisible(true);

            //format the messages like this:
            // Author: <value> , Message: <Key>
            DefaultListModel<String> model = new DefaultListModel<>();
            for(String message : tempMessages.keySet())
                model.addElement("Author: " + tempMessages.get(message) + ", Message: " + message);

            messagesList.setModel(model);
        }
        messageCleanerButton.addActionListener(e -> {
            //safeguard anyway
            if(!tempMessages.isEmpty())
                //create new Map, so that the previous one will be deleted by the GarbageCollector
                //and future null-related problems are avoided
                person.setMessages(new HashMap<>());

            //revert to "No new messages" screen
            noMessagesSignalLabel.setVisible(true);
            messagesList.setVisible(false);
            messageCleanerButton.setVisible(false);
        });
    }
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args){
        Customer temp = new Customer(
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
        temp.setMessages(tempMessage);

        new MessageReader(temp);
    }

}
