package edu.ua.culverhouse.mis;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NewController {

  // Separate JPanels for edit and new functions
  private JPanel newJPanel, editJPanel;
  private JTextField newNameFld, newBreedFld, newAgeFld, newWeightFld, newImgFld;
  private JTextField editIdFld, editNameFld, editBreedFld, editAgeFld, editWeightFld, editImgFld;
  private JComboBox<String> newSexComboBox, editSexComboBox;

  public NewController() {
  }

  // Used if NEW button was clicked from homepage
  public Dog createAndDisplayNewGUI() {
    Dog tempDog = new Dog();

    // Set layout for newPane, store button clicked in selection
    int selection = JOptionPane.showConfirmDialog(null, getNewPanel(),
        "Add Dog", JOptionPane.OK_CANCEL_OPTION);

    // If user clicks OK, check text fields for errors
    if (selection == JOptionPane.OK_OPTION) {
      if (newErrorCheck()) {
        // If text fields are filled out correctly, create new Dog object and
        // return to MainController
        tempDog = new Dog(0, newNameFld.getText(), newBreedFld.getText(),
            newSexComboBox.getItemAt(newSexComboBox.getSelectedIndex()),
            Integer.parseInt(newAgeFld.getText()), Float.parseFloat(newWeightFld.getText()), newImgFld.getText());
        return tempDog;
        // If text fields were not displayed properly, errorCheck function will display error
        // message, then redisplay newJPanel
      } else {
        createAndDisplayNewGUI();
      }
    }
    return null;
  }

  // Used if EDIT button was clicked from homepage, pass in text field values from selected dog
  // in dogLst
  public Dog createAndDisplayEditGUI(String id, String name, String breed, int sexIndex,
      String age, String weight, String imgPath) {
    Dog tempDog = new Dog();
    // Create editJPanel, set layout
    int selection = JOptionPane.showConfirmDialog(null,
        getEditPanel(id, name, breed, sexIndex, age, weight, imgPath), "Edit Dog",
        JOptionPane.OK_CANCEL_OPTION);
    // If okay button is pressed, check text fields for errors
    if (selection == JOptionPane.OK_OPTION) {
      // If no errors, create and initialize tempDog object and send it back to MainController
      if (editErrorCheck()) {
        tempDog = new Dog(Integer.parseInt(editIdFld.getText()), editNameFld.getText(),
            editBreedFld.getText(),
            editSexComboBox.getItemAt(editSexComboBox.getSelectedIndex()),
            Integer.parseInt(editAgeFld.getText()), Float.parseFloat(editWeightFld.getText()), editImgFld.getText());
        return tempDog;
      } else {
        createAndDisplayEditGUI(id, name, breed, sexIndex, age, weight, imgPath);
      }
    }
    return tempDog;
  }

  // Create and set layout for newJOptionPane
  private JPanel getNewPanel() {
    newJPanel = new JPanel();
    newJPanel.setLayout(new GridLayout(0, 2, 2, 2));

    newNameFld = new JTextField("");
    newBreedFld = new JTextField("");
    newAgeFld = new JTextField("");
    newWeightFld = new JTextField("");
    String[] sexOptions = {" ", "M", "F"};
    newSexComboBox = new JComboBox<>(sexOptions);
    newImgFld = new JTextField("");

    newJPanel.add(new JLabel("Name"));
    newJPanel.add(newNameFld);

    newJPanel.add(new JLabel("Breed"));
    newJPanel.add(newBreedFld);

    newJPanel.add(new JLabel("Sex"));
    newJPanel.add(newSexComboBox);

    newJPanel.add(new JLabel("Age"));
    newJPanel.add(newAgeFld);

    newJPanel.add(new JLabel("Weight"));
    newJPanel.add(newWeightFld);

    newJPanel.add(new JLabel("Image URL"));
    newJPanel.add(newImgFld);

    newJPanel.setSize(new Dimension(350, 10));
    newJPanel.setPreferredSize(new Dimension(350, newJPanel.getPreferredSize().height));

    return newJPanel;
  }

  // Create and set layout for editJOptionPane
  private JPanel getEditPanel(String id, String name, String breed, int sexIndex, String age,
      String weight, String imgPath) {
    editJPanel = new JPanel();
    editJPanel.setLayout(new GridLayout(0, 2, 2, 2));

    editIdFld = new JTextField(id);
    editNameFld = new JTextField(name);
    editBreedFld = new JTextField(breed);
    editAgeFld = new JTextField(age);
    editWeightFld = new JTextField(weight);
    String[] sexOptions = {" ", "M", "F"};
    editSexComboBox = new JComboBox<>(sexOptions);
    editSexComboBox.setSelectedIndex(sexIndex);
    editImgFld = new JTextField(imgPath);

    editJPanel.add(new JLabel("ID"));
    editJPanel.add(editIdFld);

    editJPanel.add(new JLabel("Name"));
    editJPanel.add(editNameFld);

    editJPanel.add(new JLabel("Breed"));
    editJPanel.add(editBreedFld);

    editJPanel.add(new JLabel("Sex"));
    editJPanel.add(editSexComboBox);

    editJPanel.add(new JLabel("Age"));
    editJPanel.add(editAgeFld);

    editJPanel.add(new JLabel("Weight"));
    editJPanel.add(editWeightFld);

    editJPanel.add(new JLabel("Image URL"));
    editJPanel.add(editImgFld);

    editJPanel.setSize(new Dimension(350, 10));
    editJPanel.setPreferredSize(new Dimension(350, editJPanel.getPreferredSize().height));

    return editJPanel;
  }

  // Check text fields for errors in newJPanel, display error messages if errors
  private boolean newErrorCheck() {
    if (newNameFld.getText().equals("")) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a name!",
          "Invalid name",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (newBreedFld.getText().equals("")) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a breed!",
          "Invalid breed",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (newSexComboBox.getSelectedIndex() == 0) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please choose a sex!",
          "No sex selected",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      Integer.parseInt(newAgeFld.getText());
    } catch (Exception a) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid age!",
          "Invalid age",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      Float.parseFloat(newWeightFld.getText());
    } catch (Exception a) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid weight!",
          "Invalid weight",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (newImgFld.getText().equals("")) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid Image URL!",
          "Invalid url",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  // Check text fields for errors in editJPanel, display error messages if errors
  private boolean editErrorCheck() {
    try {
      Integer.parseInt(editIdFld.getText());
    } catch (Exception a) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid id!",
          "Invalid age",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (editNameFld.getText().equals("")) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a name!",
          "Invalid name",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (editBreedFld.getText().equals("")) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a breed!",
          "Invalid breed",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (editSexComboBox.getSelectedIndex() == 0) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please choose a sex!",
          "No sex selected",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      Integer.parseInt(editAgeFld.getText());
    } catch (Exception a) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid age!",
          "Invalid age",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      Float.parseFloat(editWeightFld.getText());
    } catch (Exception a) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid weight!",
          "Invalid weight",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (editImgFld.getText().equals("")) {
      JOptionPane.showMessageDialog(new JFrame(),
          "Please enter a valid Image URL!",
          "Invalid url",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
}
