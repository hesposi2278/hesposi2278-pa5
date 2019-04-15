package edu.ua.culverhouse.mis;

import java.io.IOException;

public class Dog {

  private int id;
  private String name;
  private String breed;
  private String sex;
  private int age;
  private float weight;
  private String imgPath;
  private boolean rented;
  private boolean deleted;

  private static int count = 0;

  public Dog() {
  }

  public Dog(int id, String name, String breed, String sex, int age, float weight, String imgPath) {
    this.id = id;
    this.name = name;
    this.breed = breed;
    this.sex = sex;
    this.age = age;
    this.weight = weight;
    this.imgPath = imgPath;
    rented = false;
    deleted = false;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBreed() {
    return breed;
  }

  public void setBreed(String breed) {
    this.breed = breed;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public float getWeight() {
    return weight;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public boolean isRented() {
    return rented;
  }

  public void setRented() {
    this.rented = !this.rented;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted() {
    this.deleted = !this.deleted;
  }

  public String getImgPath() { return this.imgPath; }

  public void setImgPath(String imgPath) { this.imgPath = imgPath; }

  public static int getCount() {
    return count;
  }

  public static void incCount() {
    Dog.count++;
  }

  public static void decCount() {
    Dog.count--;
  }

  public static void setCount(int count) {
    Dog.count = count;
  }

  @Override
  public String toString() {
    return name;
  }

  public String toFile() {
    return getId() + "#" + getName() + "#" + getBreed() + "#" + getSex() + "#" + getAge() + "#" +
        getWeight() + "#" + getImgPath() + "#" + isRented() + "#" + isDeleted();
  }

  public static Dog addDog(Dog[] myDogs, String name, String breed, String sex,
      int age, float weight, String imgPath) throws IOException {
    Dog tempDog = new Dog(Dog.getCount() + 1, name, breed, sex, age, weight, imgPath);
    myDogs[Dog.getCount()] = tempDog;
    Dog.incCount();
    DogFile.writeAllDogs(myDogs);
    return tempDog;
  }

  public static Dog editDog(Dog[] myDogs, int id, String name, String breed, String sex,
      int age, float weight, String imgPath) throws IOException {

    int foundIndex = -1;
    for (int i = 0; i < Dog.getCount(); ++i) {
      if (myDogs[i].getId() == id) {
        foundIndex = i;
        break;
      }
    }

    myDogs[foundIndex].setName(name);
    myDogs[foundIndex].setBreed(breed);
    myDogs[foundIndex].setSex(sex);
    myDogs[foundIndex].setAge(age);
    myDogs[foundIndex].setWeight(weight);
    myDogs[foundIndex].setImgPath(imgPath);
    Dog tempDog = myDogs[foundIndex];
    DogFile.writeAllDogs(myDogs);
    return tempDog;
  }

  public static void deleteDog(Dog[] myDogs, int id) throws IOException {
    int foundIndex = -1;
    for (int i = 0; i < Dog.getCount(); ++i) {
      if (myDogs[i].getId() == id) {
        foundIndex = i;
        break;
      }
    }
    myDogs[foundIndex].setDeleted();
    DogFile.writeAllDogs(myDogs);

  }

  public static void rentDog(Dog[] myDogs, int id) throws IOException {
    int foundIndex = -1;
    for (int i = 0; i < Dog.getCount(); ++i) {
      if (myDogs[i].getId() == id) {
        foundIndex = i;
        break;
      }
    }
    myDogs[foundIndex].setRented();
    DogFile.writeAllDogs(myDogs);
  }
}
