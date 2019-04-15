package edu.ua.culverhouse.mis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CustomerFile {

  public static Customer[] getAllCustomers() throws IOException {
    Customer.setCount(0);
    Scanner inFile = new Scanner(new File("customers.txt"));
    Customer[] myCustomers = new Customer[100];

    while (inFile.hasNext()) {
      String tempString = inFile.nextLine();
      String[] tempArray = tempString.split("#");
      if (tempArray[1].equals("null")) {
        myCustomers[Customer.getCount()] = new Customer(tempArray[0], null);
        Customer.incCount();
      } else {
        myCustomers[Customer.getCount()] = new Customer(tempArray[0],
            new Dog(Integer.parseInt(tempArray[1]), tempArray[2], tempArray[3], tempArray[4],
                Integer.parseInt(tempArray[5]), Float.parseFloat(tempArray[6]), tempArray[7]));
        myCustomers[Customer.getCount()].getRentedDog().setRented();
        Customer.incCount();
      }
    }

    inFile.close();
    return myCustomers;
  }

  public static void writeAllCustomers(Customer[] myCustomers) throws IOException {
    FileWriter fileWriter = new FileWriter("customers.txt");
    PrintWriter outFile = new PrintWriter(fileWriter);

    for (int i = 0; i < Customer.getCount(); ++i) {
      outFile.println(myCustomers[i].toString());
    }
    outFile.close();
  }
}
