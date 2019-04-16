package edu.ua.culverhouse.mis;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainController {

  // Private arrays and list for keeping track of dogs and customers
  private Dog[] myDogs;
  private Customer[] myCustomers;
  DefaultListModel<Dog> tempDogs;

  // Private GUI components
  private JFrame mainJFrame;
  private JButton closeBtn, delBtn, editBtn, newBtn, rntBtn, returnBtn;
  private JList<Dog> dogLst;

  private JTextField idFld, nameFld, breedFld, ageFld, weightFld, rentedFld;
  private JComboBox<String> sexComboBox;

  private JLabel idLbl, nameLbl, breedLbl, sexLbl, ageLbl, weightLbl, rentedLbl, imgLbl;

  // Constructor for home page, calls prepareGUI() which sets layout and
  // functionality of
  // the home page
  private MainController() throws IOException {
    prepareGUI();
  }

  // At program start, create object of type MainController (Home page)
  public static void main(String[] args) throws IOException {
    MainController MainController = new MainController();

  }

  // Main method where layout and functionality of the home page is defined
  private void prepareGUI() throws IOException {
    // Initialize main JFrame
    mainJFrame = new JFrame();

    // Initialize myDogs, myCustomers, and DefaultListModel tempDogs (used to live
    // update dogLst)
    update();

    imgLbl = new JLabel();
    imgLbl.setBounds(600, 69, 250, 330);
    mainJFrame.add(imgLbl);

    // Initialize list of dogs on the right side of home page
    dogLst = new JList<>(tempDogs);
    dogLst.setBounds(13, 69, 341, 412);

    // Fill text fields with info if a dog is selected, else clear all text fields
    dogLst.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {

          // Clear text fields if no dog is selected
          if (dogLst.getSelectedIndex() == -1) {
            clearTextFields();

            // Fill text fields with respective data from dogLst
          } else {
            idFld.setText(Integer.toString(tempDogs.get(dogLst.getSelectedIndex()).getId()));
            nameFld.setText(tempDogs.get(dogLst.getSelectedIndex()).getName());
            breedFld.setText(tempDogs.get(dogLst.getSelectedIndex()).getBreed());
            if (tempDogs.get(dogLst.getSelectedIndex()).getSex().equals("M")) {
              sexComboBox.setSelectedIndex(1);
            } else {
              sexComboBox.setSelectedIndex(2);
            }
            ageFld.setText(Integer.toString(tempDogs.get(dogLst.getSelectedIndex()).getAge()));
            weightFld.setText(Float.toString(tempDogs.get(dogLst.getSelectedIndex()).getWeight()));
            rentedFld.setText(Boolean.toString(tempDogs.get(dogLst.getSelectedIndex()).isRented()));

            String path = tempDogs.get(dogLst.getSelectedIndex()).getImgPath();
            BufferedImage image = null;
            try {
            URL url = new URL(path);
            image = ImageIO.read(url);
            } catch (IOException a) {
              a.printStackTrace();
            }
            Image dimg = image.getScaledInstance(imgLbl.getWidth(), imgLbl.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            imgLbl.setIcon(imageIcon);
          }
        }
      }
    });
    mainJFrame.add(dogLst);

    // Initialize close button
    closeBtn = new JButton("Close");
    closeBtn.setBounds(778, 500, 73, 20);

    // If clicked, exit program
    closeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    mainJFrame.add(closeBtn);

    // Initialize delete button
    delBtn = new JButton("Delete");
    delBtn.setBounds(675, 500, 73, 20);

    // ActionListener for delete button click
    delBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        // Prevents any rented dogs from being deleted, would probably break program if
        // allowed
        if (!dogLst.isSelectionEmpty()) {
          if (tempDogs.elementAt(dogLst.getSelectedIndex()).isRented()) {
            JOptionPane.showMessageDialog(mainJFrame, "You cannot delete a dog that is currently rented!", "Error",
                JOptionPane.ERROR_MESSAGE);
          } else {
            String dogName = nameFld.getText();

            // Make user confirm they want to delete selected dog, save button choice in
            // selection
            int selection = JOptionPane.showConfirmDialog(mainJFrame,
                ("Are you sure you want you want to delete " + nameFld.getText() + "?"), "Confirm Deletion",
                JOptionPane.OK_CANCEL_OPTION);

            // If the OK button was clicked
            if (selection == JOptionPane.OK_OPTION) {
              try {
                // Call deleteDog method to change selected dog's deleted variable
                // to true, write to file
                Dog.deleteDog(myDogs, Integer.parseInt(idFld.getText()));

                // Remove dog from DefaultListModel, dogLst is automatically updated
                // and set list selection to none
                tempDogs.removeElementAt(dogLst.getSelectedIndex());
                dogLst.clearSelection();
                JOptionPane.showMessageDialog(mainJFrame, (dogName + " was successfully deleted!"), "Success!",
                    JOptionPane.INFORMATION_MESSAGE);
              } catch (IOException a) {
                JOptionPane.showMessageDialog(mainJFrame, "Somethings broken", "IOException",
                    JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        } else {
          // If no dog is selected from dogLst, show error message
          JOptionPane.showMessageDialog(mainJFrame, "Please select a dog from the list to delete!", "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    mainJFrame.add(delBtn);

    // Initialize edit button and set layout on MainFrame
    editBtn = new JButton("Edit");
    editBtn.setBounds(572, 500, 73, 20);

    // ActionListener for edit button
    editBtn.addActionListener(new ActionListener() {
      Dog tempDog = new Dog();

      @Override
      public void actionPerformed(ActionEvent e) {
        // First make sure that a dog is selected from the list
        if (!dogLst.isSelectionEmpty()) {
          // Prevents any rented dogs from being edited, would probably break program if
          // allowed
          if (tempDogs.elementAt(dogLst.getSelectedIndex()).isRented()) {
            JOptionPane.showMessageDialog(mainJFrame, "You cannot edit a dog that is currently rented!", "Error",
                JOptionPane.ERROR_MESSAGE);
            // If dog is not rented, create NewController object and initialize edit
            // JOptionPane
          } else {
            NewController NewController = new NewController();

            // Call createAndDisplayEditGUI, sets layout and functionality of edit
            // JOptionPane
            // Once done editing, create new Dog object with updated attributes and store
            // it/
            // in tempDog
            tempDog = NewController.createAndDisplayEditGUI(idFld.getText(), nameFld.getText(), breedFld.getText(),
                sexComboBox.getSelectedIndex(), ageFld.getText(), weightFld.getText(), tempDogs.get(dogLst.getSelectedIndex()).getImgPath());

            try {
              // Set dog to be edited in tempDogs list equal to edited dog from NewController
              // and clear list selection so dogList is updated
              tempDogs.set(dogLst.getSelectedIndex(), Dog.editDog(myDogs, tempDog.getId(), tempDog.getName(),
                  tempDog.getBreed(), tempDog.getSex(), tempDog.getAge(), tempDog.getWeight(), tempDog.getImgPath()));
              dogLst.clearSelection();
              JOptionPane.showMessageDialog(mainJFrame, (tempDog.getName() + " was successfully edited!"), "Success!",
                  JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException a) {
              JOptionPane.showMessageDialog(mainJFrame, "Somethings Broken", "Error", JOptionPane.ERROR_MESSAGE);
            }
          }

        } else {
          // If no dog is selected from dogLst, show error message
          JOptionPane.showMessageDialog(mainJFrame, "Please select a dog from the list to edit!", "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }

    });

    mainJFrame.add(editBtn);

    // Initialize newBtn and set layout
    newBtn = new JButton("New");
    newBtn.setBounds(469, 500, 73, 20);
    newBtn.addActionListener(new ActionListener() {
      Dog tempDog = null;

      @Override
      public void actionPerformed(ActionEvent e) {
        // Create NewController object
        NewController NewController = new NewController();
        // Set layout and functionality of New Dog JOptionPane and display Pane
        tempDog = NewController.createAndDisplayNewGUI();

        try {
          // If new dog was successfully added in createAndDisplayNewGui(), tempDog will
          // not equal
          // null and can now be added to tempDogs list
          if (tempDog != null) {
            tempDog = Dog.addDog(myDogs, tempDog.getName(), tempDog.getBreed(), tempDog.getSex(), tempDog.getAge(),
                tempDog.getWeight(), tempDog.getImgPath());
            // Add newly created Dog to tempDogs, dogLst automatically updated
            tempDogs.addElement(tempDog);
            dogLst.clearSelection();
            JOptionPane.showMessageDialog(mainJFrame, (tempDog.getName() + " was successfully added!"), "Success!",
                JOptionPane.INFORMATION_MESSAGE);
          }
        } catch (IOException a) {
          JOptionPane.showMessageDialog(mainJFrame, "Somethings Broken", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    mainJFrame.add(newBtn);

    // Initialize rent button and set layout
    rntBtn = new JButton("Rent");
    rntBtn.setBounds(778, 420, 73, 52);
    rntBtn.addActionListener(new ActionListener() {
      Dog tempDog = null;

      @Override
      public void actionPerformed(ActionEvent e) {
        // Make sure that a dog is selected from dogLst
        if (!dogLst.isSelectionEmpty()) {
          // Make sure that selected dog is not currently rented
          if (!tempDogs.elementAt(dogLst.getSelectedIndex()).isRented()) {
            // Prompt user for email address with JOptionPane (customer "primary key")
            // and store in string
            String email = JOptionPane.showInputDialog(mainJFrame, "Please enter your email address", "Rent a dog",
                JOptionPane.INFORMATION_MESSAGE);
            try {
              // Call rentDog function, which adds rented dog to Customer object and writes to
              // dog and customer text files
              tempDog = Customer.rentDog(myCustomers, myDogs, email, tempDogs.elementAt(dogLst.getSelectedIndex()));
              // If rentDog was successful, will return not null dog
              if (tempDog != null) {
                // Update arrays and tempDogs list and clear dogLst selection
                update();
                dogLst.clearSelection();
                JOptionPane.showMessageDialog(mainJFrame, (tempDog.getName() + " was successfully rented!"), "Success!",
                    JOptionPane.INFORMATION_MESSAGE);
                // Show error message if email entered already has a dog rented
              } else {
                JOptionPane.showMessageDialog(mainJFrame, ("You already have a dog rented!"), "Error",
                    JOptionPane.ERROR_MESSAGE);
              }
            } catch (IOException a) {
              JOptionPane.showMessageDialog(mainJFrame, "Somethings broken", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Show error message if selected dog is already rented
          } else {
            JOptionPane.showMessageDialog(mainJFrame, (nameFld.getText() + " is already rented!"), "Error",
                JOptionPane.ERROR_MESSAGE);
          }
          // Show error message if no dog was selected from dogLst
        } else {
          JOptionPane.showMessageDialog(mainJFrame, "Please select a dog from the list to rent!", "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    mainJFrame.add(rntBtn);

    // Initialize and set layout for rent button
    returnBtn = new JButton("Return");
    returnBtn.setBounds(675, 420, 73, 52);
    returnBtn.addActionListener(new ActionListener() {
      boolean flag = false;

      @Override
      public void actionPerformed(ActionEvent e) {
        // Prompt user to enter email address and store in string
        String email = JOptionPane.showInputDialog(mainJFrame, "Please enter your email address", "Return a dog",
            JOptionPane.INFORMATION_MESSAGE);

        try {
          // if customer currently has a dog rented, flag will be set to true, else flag
          // is false
          flag = Customer.returnDog(myCustomers, myDogs, email);
          if (flag) {
            // If returnDog was successful, update arrays and tempDogs list and
            // clear dogLst selection
            update();
            dogLst.clearSelection();
            JOptionPane.showMessageDialog(mainJFrame, ("Return successful!"), "Success!",
                JOptionPane.INFORMATION_MESSAGE);
            // Show error if user tries to return dog when they don't have one currently
            // rented
          } else {
            JOptionPane.showMessageDialog(mainJFrame, "You don't have any dogs rented!", "You fucked up",
                JOptionPane.ERROR_MESSAGE);
          }
        } catch (IOException a) {
          JOptionPane.showMessageDialog(mainJFrame, "Somethings broken", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    mainJFrame.add(returnBtn);

    // Initialize text boxes and set layouts
    idFld = new JTextField();
    idFld.setBounds(380, 69, 200, 20);
    idFld.setEditable(false);
    mainJFrame.add(idFld);

    nameFld = new JTextField();
    nameFld.setBounds(380, 134, 200, 20);
    nameFld.setEditable(false);
    mainJFrame.add(nameFld);

    breedFld = new JTextField();
    breedFld.setBounds(380, 199, 200, 20);
    breedFld.setEditable(false);
    mainJFrame.add(breedFld);

    String[] sexOptions = { "", "M", "F" };
    sexComboBox = new JComboBox<>(sexOptions);
    sexComboBox.setBounds(380, 264, 200, 20);
    sexComboBox.setEnabled(false);
    mainJFrame.add(sexComboBox);

    ageFld = new JTextField();
    ageFld.setBounds(380, 329, 200, 20);
    ageFld.setEditable(false);
    mainJFrame.add(ageFld);

    weightFld = new JTextField();
    weightFld.setBounds(380, 394, 200, 20);
    weightFld.setEditable(false);
    mainJFrame.add(weightFld);

    rentedFld = new JTextField();
    rentedFld.setBounds(380, 459, 200, 20);
    rentedFld.setEditable(false);
    mainJFrame.add(rentedFld);

    // Initialize text box labels and set layout
    idLbl = new JLabel("ID");
    idLbl.setBounds(388, 49, 60, 20);
    mainJFrame.add(idLbl);

    nameLbl = new JLabel("Name");
    nameLbl.setBounds(388, 114, 60, 20);
    mainJFrame.add(nameLbl);

    breedLbl = new JLabel("Breed");
    breedLbl.setBounds(388, 179, 60, 20);
    mainJFrame.add(breedLbl);

    sexLbl = new JLabel("Sex");
    sexLbl.setBounds(388, 244, 120, 20);
    mainJFrame.add(sexLbl);

    ageLbl = new JLabel("Age");
    ageLbl.setBounds(388, 309, 60, 20);
    mainJFrame.add(ageLbl);

    weightLbl = new JLabel("Weight");
    weightLbl.setBounds(388, 374, 60, 20);
    mainJFrame.add(weightLbl);

    rentedLbl = new JLabel("Rented?");
    rentedLbl.setBounds(388, 439, 60, 20);
    mainJFrame.add(rentedLbl);

    // Finally set MainJFrame size, default close (when x button is clicked), set
    // layout to null
    // and make it visible
    mainJFrame.setSize(860, 550);
    mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainJFrame.setLayout(null);
    mainJFrame.setVisible(true);
  }

  // Helper function to clear all text fields when no Dog is selected from dogLst
  private void clearTextFields() {
    idFld.setText("");
    nameFld.setText("");
    breedFld.setText("");
    sexComboBox.setSelectedIndex(0);
    ageFld.setText("");
    weightFld.setText("");
    rentedFld.setText("");
  }

  // Helper function to update all arrays/lists, pulls info from text files, then
  // add non deleted
  // dogs to tempDogs list from myDogs array
  private void update() throws IOException {
    myDogs = DogFile.getAllDogs();
    myCustomers = CustomerFile.getAllCustomers();
    tempDogs = new DefaultListModel<>();

    for (int i = 0; i < Dog.getCount(); ++i) {
      if (!myDogs[i].isDeleted()) {
        tempDogs.addElement(myDogs[i]);
      }
    }
  }
}
