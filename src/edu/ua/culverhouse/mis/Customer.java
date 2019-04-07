package edu.ua.culverhouse.mis;

import java.io.IOException;

public class Customer {

  private String email;
  private Dog rentedDog;
  private static int count = 0;

  public Customer(String email, Dog rentedDog) {
    this.email = email;
    this.rentedDog = rentedDog;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Dog getRentedDog() {
    return rentedDog;
  }

  public void setRentedDog(Dog rentedDog) {
    this.rentedDog = rentedDog;
  }

  public static int getCount() {
    return count;
  }

  public static void incCount() {
    Customer.count++;
  }

  public static void decCount() {
    Customer.count--;
  }

  public static void setCount(int count) {
    Customer.count = count;
  }

  @Override
  public String toString() {
    if (getRentedDog() != null) {
      return getEmail() + "#" + getRentedDog().toFile();
    } else {
      return getEmail() + "#null";
    }

  }

  // Called from MainController, finds customer by email. Creates new customer if email is not
  // found, else update customer's Dog attribute
  public static Dog rentDog(Customer[] myCustomers, Dog[] myDogs,
      String email, Dog dogToRent) throws IOException {

    // Sequential search to see if customer already exists (search by email)
    int foundIndex = -1;
    for (int i = 0; i < Customer.getCount(); ++i) {
      if (myCustomers[i].getEmail().equals(email)) {
        foundIndex = i;
      }
    }

    // If customer does not exist, create new customer object and add to myCustomers array, write to
    // customers text file
    if (foundIndex == -1) {
      Dog.rentDog(myDogs, dogToRent.getId());
      myCustomers[Customer.getCount()] = new Customer(email, dogToRent);
      Customer.incCount();
      // Else if customer does exist, check if they already have a dog rented, if not set Dog
      // attribute to selected dog from dogLst
    } else {
      // If customer already has a dog rented, return null object to EventListener in MainController
      if (myCustomers[foundIndex].getRentedDog() != null) {
        return null;
        // Else call rentDog function from dog class (updates rented status in dogs text file) and
        // set customers rentedDog attribute to dog selected from DogLst
      } else {
        Dog.rentDog(myDogs, dogToRent.getId());
        myCustomers[foundIndex].setRentedDog(dogToRent);
      }
    }
    // Write updated customer array to file
    CustomerFile.writeAllCustomers(myCustomers);
    return dogToRent;
  }

  public static boolean returnDog(Customer[] myCustomers, Dog[] myDogs,
      String email) throws IOException {
    int foundIndex = -1;
    for (int i = 0; i < Customer.getCount(); ++i) {
      if (myCustomers[i].getEmail().equals(email)) {
        foundIndex = i;
      }
    }
    // If customer does not exist, return false
    if (foundIndex == -1) {
      return false;

    } else {
      // If customer does not currently have a rented dog, return false
      if (myCustomers[foundIndex].getRentedDog() == null) {
        return false;
      } else {
        // Else, call rentDog function from Dog class (updates rented status and writes to file) and
        // set customers dog attribute to null
        Dog.rentDog(myDogs, myCustomers[foundIndex].getRentedDog().getId());
        myCustomers[foundIndex].setRentedDog(null);
        CustomerFile.writeAllCustomers(myCustomers);
        return true;
      }
    }
  }
}
