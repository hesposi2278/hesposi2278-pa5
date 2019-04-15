package edu.ua.culverhouse.mis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DogFile {

  public static Dog[] getAllDogs() throws IOException {
    Dog.setCount(0);
    Scanner inFile = new Scanner(new File("dogs.txt"));
    Dog[] myDogs = new Dog[100];

    while (inFile.hasNext()) {
      String tempString = inFile.nextLine();
      String[] tempArray = tempString.split("#");
      myDogs[Dog.getCount()] =
          new Dog(Integer.parseInt(tempArray[0]), tempArray[1],
              tempArray[2], tempArray[3],
              Integer.parseInt(tempArray[4]), Float.parseFloat(tempArray[5]), tempArray[6]);
      if (tempArray[7].equals("true")) {
        myDogs[Dog.getCount()].setRented();
      }
      if (tempArray[8].equals("true")) {
        myDogs[Dog.getCount()].setDeleted();
      }

      Dog.incCount();
    }

    inFile.close();
    return myDogs;
  }

  public static void writeAllDogs(Dog[] myDogs) throws IOException {

    FileWriter fileWriter = new FileWriter("dogs.txt");
    PrintWriter outFile = new PrintWriter(fileWriter);

    for (int i = 0; i < Dog.getCount(); ++i) {
      outFile.println(myDogs[i].toFile());
    }

    outFile.close();
  }
}
