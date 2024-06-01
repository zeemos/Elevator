package com.bluestaq.codechallenge;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ElevatorDemo {
  
  //------------------------------------------------------- Member Variables
  
  //----------------------------------------------------- Constructor / Init
  
  //--------------------------------------------------------- Public Methods

  /**
   * Entry point when running from command line
   * @param args Not used here
   */
  public static void main(String args[]) {
    
    ExecutorService service = null;
    Scanner input = new Scanner(System.in);
    boolean running = true;
    
    try {
      
      // retrieve number of floor input from user
      System.out.printf("Enter number of floors (2-100):%n");
      String numFloorsStr = input.nextLine();
      while(!validateNumFloors(numFloorsStr)) {
        System.out.printf("Invalid number of floors.  Please enter a number from 2 to 100%n");
        numFloorsStr = input.nextLine();
      }
      
      // retrieve starting floor input from user
      System.out.printf("Enter starting floor (1-%s):%n", numFloorsStr);
      String startFloor = input.nextLine();
      while(!validateStartingFloor(startFloor, numFloorsStr)) {
        System.out.printf("Invalid starting floor.  Please enter a number from 1 to %s%n", numFloorsStr);
        startFloor = input.nextLine();
      }
      
      // create the Elevator object
      Elevator elevator = new Elevator(Integer.parseInt(numFloorsStr), Integer.parseInt(startFloor));
      
      // print the help instructions
      printHelpText();
      
      // start command input loop
      while(running) {
        
        final String commandStr = input.nextLine().trim();
        
        switch(commandStr) {
          // empty command which means to pause execution
          case "":
            System.out.printf("Paused - Enter more commands:%n");
            elevator.pause();
            break;
          
          // exit the program
          case "exit":
            elevator.shutDown();
            running = false;
            break;
            
          // start or un-pause the elevator
          case "start":
            if (elevator.isRunning()) {
              elevator.unpause();
            } else {
              service = Executors.newSingleThreadExecutor();
              service.execute(elevator);
            }
            break;
          
          // print the help text again
          case "help":
            printHelpText();
            break;
          
          // otherwise try to parse as a floor command
          default:
            parseAndEnterCommand(commandStr, elevator);
        }
      }
      
    } finally {
      if (service != null) {
        service.shutdownNow();
      }
      if (input != null) {
        input.close();
      }
      System.out.printf("Goodbye%n");
    }
  }
  
  //--------------------------------------------------------- Private Methods
  
  /**
   * Attempts to parse the command and enter it as a button press to the Elevator
   * @param inputStr String entered by the user
   * @param e The Elevator instance
   * @param numFloors Number of floors in the Elevator instance
   */
  private static void parseAndEnterCommand(String inputStr, Elevator e) {
    
    String command = inputStr.toUpperCase();
    
    // validate with regex comparison
    if (!validateFloorCommand(command)) {
      System.out.println("Invalid Command");
      return;
    }
    
    // first deal with outside button pushes (start with U: or D:)
    if (command.startsWith("U") || command.startsWith("D")) {
      String[] parts = command.split(":");
      int floor = Integer.parseInt(parts[1]);
      if (floor < 1 || floor > e.getNumFloors()) {
        System.out.println("Inavlid floor number: out of range");
        return;
      }
      if (parts[0].equals("U")) {
        e.pushUpButton(Integer.parseInt(parts[1]));
      } else if (parts[0].equals("D")) {
        e.pushDownButton(Integer.parseInt(parts[1]));
      }
      
    // otherwise try as inside button press
    } else {
      int floor = Integer.parseInt(inputStr);
      if (floor < 1 || floor > e.getNumFloors()) {
        System.out.println("Inavlid floor number: out of range");
        return;
      }
      e.pushInsideButton(floor);
    }
  }
  
  /**
   * Prints the command loop help text
   */
  private static void printHelpText() {
    System.out.println("Valid commands: 'start', 'exit', 'help' or enter a floor number");
    System.out.println("To simulate a floor button inside simply enter the floor number.");
    System.out.println("To simulate a floor button outside the elevator enter either U: or D: and then the floor number like 'U:10' or 'D:5'");
    System.out.println("To pause execution and add more commands, just hit the 'Enter' key");
  }
  
  /**
   * Validates the number of floors entered.  Must be an int and between 2 and 100.
   * @param floors Input entered by the user.
   * @return boolean true if the input is valid
   */
  private static boolean validateNumFloors(String floors) {
    try {
      int numFloors = Integer.parseInt(floors);
      return (numFloors >= 2 && numFloors <= 100);
    } catch(NumberFormatException nfe) {
      return false;
    }
  }
  
  /**
   * Validates the starting floor entered.  Must be an int and between 1 and the 
   * number of floors entered previously.
   * @param startFloor Input entered by the user.
   * @param numFloors Number of floors entered by the user.
   * @return boolean true if the starting floor number entered is valid
   */
  private static boolean validateStartingFloor(String startFloor, String numFloors) {
    try {
      int floors = Integer.parseInt(numFloors);
      int floorNum = Integer.parseInt(startFloor);
      return floorNum >= 1 && floorNum <= floors;
      
    } catch(NumberFormatException nfe) {
      return false;
    }
  }
  
  /**
   * Validates the floor command against a regular expression.
   * @param command Input entered by the user
   * @return boolean true if the command matches the regex
   */
  private static boolean validateFloorCommand(String command) {
    Pattern pattern = Pattern.compile("(U:|D:)?\\d*");
    return pattern.matcher(command.toUpperCase()).matches();
  }
}
