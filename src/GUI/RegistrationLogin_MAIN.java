package GUI;

import data.Database;
import userTypes.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * RegistrationLogin
 * This is the starting frame of the app, where the Login and Registration mechanics are fused together
 * On the left side, users can input their username and password, which are then examined and if they match the
 * info held in the database, users are allowed connection
 * On the right side, a registration form allows all future users to create their accounts, to then be admitted
 * by the Admins
 */
public class RegistrationLogin_MAIN extends JFrame{
    private JPasswordField passwordField;
    private JTextField usernameTextField;
    private JButton enterButton, registerButton;
    private JRadioButton customerRadioButton, providerRadioButton;
    private JTextField setUsernameTextField, passwordTextField, fullNameTextField,
                        addressTextField, phoneTextField, HQTextField;
    private JPanel mainPanel;
    private JLabel warningLabel, hqLabel, feedbackLabel;

    @SuppressWarnings("StringConcatenationInLoop")
    public RegistrationLogin_MAIN(Database dataReference){
        super();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("MyBooking - Start");
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        this.setPreferredSize(new Dimension(700, 500));
        this.pack();

        warningLabel.setVisible(false);
        feedbackLabel.setVisible(false);

        customerRadioButton.addActionListener(e -> {
            if (customerRadioButton.isSelected()) {
                providerRadioButton.setSelected(false);
                hqLabel.setVisible(false);
                HQTextField.setVisible(false);
            }
            });
        providerRadioButton.addActionListener(e -> {
            if (providerRadioButton.isSelected()){
                customerRadioButton.setSelected(false);
                hqLabel.setVisible(true);
                HQTextField.setVisible(true);
            }
        });
        enterButton.addActionListener(e -> {
            User result = Admin.searchUsers(usernameTextField.getText(),dataReference);
            if (result!=null){
                String password = "";
                char[] letters = passwordField.getPassword();
                for (char letter : letters){
                    password += ((Character)letter).toString();
                }
                if (result.checkPassword(password) && result.getAdmitted()){
                    new Menu(result,dataReference);
                    super.dispose();
                }
                else{
                    warningLabel.setVisible(true);
                }
            }
            else {
                warningLabel.setVisible(true);
            }
        });
        registerButton.addActionListener(e -> {
            if (customerRadioButton.isSelected() && validateInputs(false,dataReference)){
               Customer person = new Customer(
                       setUsernameTextField.getText(),
                       passwordTextField.getText(),
                       fullNameTextField.getText(),
                       addressTextField.getText(),
                       phoneTextField.getText(),
                       false
               );
               dataReference.pushRegisteredCustomer(person);
               feedbackLabel.setText("Registration Successful");
               feedbackLabel.setVisible(true);
            }
            else if (providerRadioButton.isSelected() && validateInputs(true,dataReference)){
                Provider person = new Provider(
                        setUsernameTextField.getText(),
                        passwordTextField.getText(),
                        fullNameTextField.getText(),
                        addressTextField.getText(),
                        phoneTextField.getText(),
                        HQTextField.getText(),
                        false
                );
                dataReference.pushRegisteredProvider(person);
                feedbackLabel.setText("Registration Successful");
                feedbackLabel.setVisible(true);
            }
            else{
                feedbackLabel.setText("Registration Failed");
                feedbackLabel.setVisible(true);
            }
        });
    }
    /**
     * textField data validator
     * When creating a new account, it is important that "garbage" accounts that hold empty info are disallowed
     * (also in case of human error, i.e. forgetting to fill a certain textField)
     * Also, multiple accounts with the same username should also be forbidden for security reasons
     * @param isProvider whether the new account is going to be a provider, so that the program can know whether
     *                   it should ask for addressHQ or not
     * @param dataReference the central database
     * @return true only if all textFields are non-empty, non-blank and no users exist with the same username
     */
    private boolean validateInputs(boolean isProvider,Database dataReference){
        String check = setUsernameTextField.getText();
        if (check.isEmpty() || check.isBlank()){
            return false;
        }
        if (Admin.searchUsers(check,dataReference) != null){
            return false;
        }
        check = passwordTextField.getText();
        if (check.isEmpty() || check.isBlank()){
            return false;
        }
        check = fullNameTextField.getText();
        if (check.isEmpty() || check.isBlank()){
            return false;
        }
        check = addressTextField.getText();
        if (check.isEmpty() || check.isBlank()){
            return false;
        }
        check = phoneTextField.getText();
        if (check.isEmpty() || check.isBlank()){
            return false;
        }
        if (isProvider){
            check = HQTextField.getText();
            return !check.isEmpty() && !check.isBlank();

        }
        return true;
    }
    public static void main(String[] args){
        new RegistrationLogin_MAIN(new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}
