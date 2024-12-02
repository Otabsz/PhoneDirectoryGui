import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class PhoneDirectory extends JFrame {
    private Hashtable<String, Long> contacts;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextArea outputArea;

    public PhoneDirectory() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(PhoneDirectory.class.getResource("/Picture/Vector.png")));
        contacts = new Hashtable<>();

        setTitle("Phone Directory");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        nameField = new JTextField();
        phoneNumberField = new JTextField();

        phoneNumberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                // Check if the entered character is a digit
                if (!Character.isDigit(c)) {
                    e.consume(); // Ignore the non-numeric character
                }

                // Get the current text in the field
                String currentText = phoneNumberField.getText();

                // Check if the length exceeds 11 digits
                if (currentText.length() >= 11) {
                    e.consume(); // Ignore the key if the limit is reached
                }
                // Check if the first character is '0'
                if (currentText.isEmpty() && c != '0') {
                    e.consume(); // Ignore the key if it's not '0' as the first character
                }
            }
        });
        JButton addButton = new JButton("Add Contact");
        JButton searchButton = new JButton("Search Contact");
        JButton updateButton = new JButton("Update Contact");
        JButton deleteButton = new JButton("Delete Contact");
        JButton displayButton = new JButton("Display All Contacts");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneNumberField);
        getContentPane().add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(displayButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateContact();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllContacts();
            }
        });
    }

    private void addContact() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone number are required.");
            return;
        }

        // Check if the name already exists
        if (contacts.containsKey(name)) {
            JOptionPane.showMessageDialog(this, "Contact with the same name already exists.");
            return;
        }

        // Check if the phone number already exists
        for (Long existingPhoneNumber : contacts.values()) {
            if (existingPhoneNumber.equals(Long.parseLong(phoneNumber))) {
                JOptionPane.showMessageDialog(this, "Contact with the same phone number already exists.");
                return;
            }
        }

        if (phoneNumber.length() == 11) {
            try {
                Long phoneNumberLong = Long.parseLong(phoneNumber);
                contacts.put(name, phoneNumberLong);
                outputArea.setText("Contact added successfully!");
                nameField.setText("");
                phoneNumberField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Phone number must be a valid number.");
            }
        }
    }

    private void searchContact() {
        String name = nameField.getText();
        StringBuilder allPhoneNumbers = new StringBuilder();

        for (String contactName : contacts.keySet()) {
            if (contactName.equals(name)) {
                Long phoneNumber = contacts.get(contactName);
                String phoneNumberString = String.format("%011d", phoneNumber);
                
                allPhoneNumbers.append("Name: ").append(contactName).append("\n")
                               .append("Phone Number: ").append(phoneNumberString).append("\n");
            }
        }

        if (allPhoneNumbers.length() > 0) {
            outputArea.setText(allPhoneNumbers.toString());
        } else {
            outputArea.setText("Contact not found.");
        }
    }

    private void updateContact() {
        String name = nameField.getText();
        String newPhoneNumber = phoneNumberField.getText();

        if (name.isEmpty() || newPhoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and new phone number are required.");
            return;
        }

        Long phoneNumber;
        try {
            phoneNumber = Long.parseLong(newPhoneNumber);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Phone number must be a valid number.");
            return;
        }

        if (contacts.containsValue(phoneNumber)) {
            outputArea.setText("Phone number " + phoneNumber + " is already associated with another contact.");
        } else if (contacts.containsKey(name)) {
            contacts.put(name, phoneNumber);
            outputArea.setText("Phone number for " + name + " updated successfully!");
            nameField.setText("");
            phoneNumberField.setText("");
        } else {
            outputArea.setText("Contact not found. Please enter a valid name.");
        }
    }

    private void deleteContact() {
        String name = nameField.getText();
        String newPhoneNumber = phoneNumberField.getText();
        contacts.remove(name);
        contacts.remove(newPhoneNumber);
        
        outputArea.setText("Contact deleted successfully!");
        nameField.setText("");
        phoneNumberField.setText("");
    }

    private void displayAllContacts() {
        StringBuilder allContacts = new StringBuilder();
        for (String name : contacts.keySet()) {
            Long phoneNumber = contacts.get(name);

            String phoneNumberString = String.format("%011d", phoneNumber);

            allContacts.append("Name: ").append(name).append("\n")
                   .append("Phone Number: ").append(phoneNumberString).append("\n");
    }
    outputArea.setText(allContacts.toString());
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneDirectory phoneDirectoryGUI = new PhoneDirectory();
            phoneDirectoryGUI.setVisible(true);
        });
    }
}