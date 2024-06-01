package com.bluestaq.codechallenge;

import java.util.SortedSet;
import java.util.TreeSet;

public class Elevator implements Runnable {
  
  //------------------------------------------------------- Member Variables
  
  private boolean shutDown = false;
  private boolean paused = false;
  private boolean running = false;
  private int numFloors;
  private int currentFloor;
  private boolean movingUp = false;
  private boolean movingDown = false;
  
  private SortedSet<Integer> insideButtons;       // buttons inside elevator
  private SortedSet<Integer> outsideUpButtons;    // up buttons outside elevator
  private SortedSet<Integer> outsideDownButtons;  // down buttons outside elevator
  
  //----------------------------------------------------- Constructor / Init
  
  /**
   * Public constructor taking in the number of floors and the starting floor
   * @param numFloors Total number of floors
   * @param startingFloor Floor to start on
   */
  public Elevator(int numFloors, int startingFloor) {
    this.numFloors = numFloors;
    this.currentFloor = startingFloor;
    this.insideButtons = new TreeSet<Integer>();
    this.outsideUpButtons = new TreeSet<Integer>();
    this.outsideDownButtons = new TreeSet<Integer>();
  }
  
  //--------------------------------------------------------- Public Methods
  
  /**
   * run method which is the entry point when started as a separate thread
   */
  @Override
  public void run() {
    System.out.println("Elevator: Starting");
    
    this.running = true;
    
    // loop until shutDown method is called
    while(!shutDown) {
      if (!this.paused) {
        
        // call the main cycle loop
        cycle();
        
      }
      try {
        // sleep for 1 second to simulate moving from floor to floor
        Thread.sleep(1000);
      } catch(InterruptedException ie) {
        
      }
    }
    System.out.println("Elevator: shutting down");
  }
  
  /**
   * sets the paused member variable to true
   */
  public void pause() {
    this.paused = true;
  }
  
  /**
   * sets the paused member variable to false
   */
  public void unpause() {
    this.paused = false;
  }
  
  /**
   * sets the shutDown member variable to true
   */
  public void shutDown() {
    this.shutDown = true;
  }
  
  /**
   * isRunning - returns true if the running variable is true
   * @return
   */
  public boolean isRunning() {
    return this.running;
  }
  
  /**
   * Sets the inside button as pushed for the give floor number
   * @param floorNum Floor number to marked as pressed
   */
  public void pushInsideButton(int floorNum) {
    insideButtons.add(floorNum);
    setDirection(floorNum);
  }
  
  /**
   * Sets the down button as pressed for the given floor
   * @param floorNum Floor number to mark the down arrow as pressed
   */
  public void pushDownButton(int floorNum) {
    outsideDownButtons.add(floorNum);
    setDirection(floorNum);
  }

  /**
   * Sets the up button as pressed for the given floor
   * @param floorNum Floor number to mark the up arrow as pressed
   */
  public void pushUpButton(int floorNum) {
    outsideUpButtons.add(floorNum);
    setDirection(floorNum);
  }
  
  /**
   * Returns the number of floors that this Elevator services
   * @return
   */
  public int getNumFloors() {
    return numFloors;
  }
  
  //--------------------------------------------------------- Private Methods
  
  /**
   * Main logic cycle that 
   */
  private void cycle() {
    
    // first check if there are any current floor commands
    if (allClear()) {
      movingDown = false;
      movingUp = false;
      printWaitingMsg();
      return;
    }
    
    // logic for when currently moving down
    if (movingDown) {
      
      // decrement the current floor
      currentFloor--;
      
      // print the stop message if the current floor is pressed inside,
      // or the down button is pressed outside
      if (insideButtons.contains(currentFloor) || outsideDownButtons.contains(currentFloor)) {
        printStoppingMsg();
        
      // print the stop message if up button is pressed and there are no more buttons pressed below
      } else if (outsideUpButtons.contains(currentFloor) 
          && (!insideButtonsBelow() && !outsideDownButtonsBelow())) {
        printStoppingMsg();
        
        // also switch direction since there are no more buttons pressed below
        switchDirection();
        
      // otherwise, just passing through this floor so print the passing message
      } else {
        printPassingMsg();
      }
      
    // logic for when currently moving up  
    } else if (movingUp) {
      
      // increment the current floor
      currentFloor++;
      
      // print the stop message if the current floor is pressed inside,
      // or the up button is pressed outside
      if (insideButtons.contains(currentFloor) || outsideUpButtons.contains(currentFloor)) {
        printStoppingMsg();
        
      // print the top message if the down button is pressed and there are no more buttons pressed above
      } else if (outsideDownButtons.contains(currentFloor)
          && (!insideButtonsAbove() && !outsideUpButtonsAbove())) {
        printStoppingMsg();
        
        // also switch direction since there are no more buttons pressed above
        switchDirection();
        
      // otherwise, just passing through this floor so print the passing message
      } else {        
        printPassingMsg();
      }

    }
    
    // clear the current floor
    clearFloor();
    
    // determine the next direction to move
    setNextDirection();
  }
  
  /**
   * Returns true if all the buttons are un-pressed currently
   * @return
   */
  private boolean allClear() {
    return this.insideButtons.isEmpty() 
        && this.outsideDownButtons.isEmpty()
        && outsideUpButtons.isEmpty();
  }
  
  /**
   * clearFloor - clears the current floor from the inside buttons list.
   * Also clears from the outside down buttons list if moving down or the
   * outside up buttons if moving up
   */
  private void clearFloor() {
    this.insideButtons.remove(currentFloor);
    if (movingDown) this.outsideDownButtons.remove(currentFloor);
    if (movingUp) this.outsideUpButtons.remove(currentFloor);
  }
  
  /**
   * Toggles the movingUp and movingDown booleans
   */
  private void switchDirection() {
    movingUp = !movingUp;
    movingDown = !movingDown;
  }
  
  /**
   * setNextDirection - sets the next direction (or leaves it the same)
   * based on the current direction and what other buttons are pressed.
   * Priority is given to inside buttons in the current direction, then
   * outside buttons in the current direction, then inside buttons in 
   * the opposite direction, then outside buttons in the opposite
   * direction.
   */
  private void setNextDirection() {
    // if moving up and more inside buttons above, continue on
    if (movingUp && insideButtonsAbove()) {
      return;
    }
    // if moving down and more inside buttons below, continue on
    if (movingDown && insideButtonsBelow()) {
      return;
    }
    // if moving up and more outside buttons above, continue on
    if (movingUp && outsideUpButtonsAbove()) {
      return;
    }
    // if moving down and more outside buttons below, continue on
    if (movingDown && outsideDownButtonsBelow()) {
      return;
    }
    // if moving up but has inside buttons below, switch directions
    if (movingUp && insideButtonsBelow()) {
      switchDirection();
      return;
    }
    // if moving down but has inside buttons above, switch directions
    if (movingDown && insideButtonsAbove()) {
      switchDirection();
      return;
    }
    // if moving up but has outside down buttons below
    if (movingUp && outsideDownButtonsBelow()) {
      switchDirection();
      return;
    }
    // if moving down but has outside up buttons below
    if (movingDown && outsideUpButtonsAbove()) {
      switchDirection();
      return;
    }
    // if moving up, but at the top, switch directions
    if (movingUp && currentFloor == numFloors) {
      switchDirection();
      return;
    }
    // if moving down, but at the first floor, switch directions
    if (movingDown && currentFloor == 1) {
      switchDirection();
      return;
    }
    // if all buttons are cleared, set movingUp and movingDown to false
    if (allClear()) {
      movingDown = false;
      movingUp = false;
      return;
    }
    
  }
  
  /**
   * setDirection - called after a new command is entered and sets the 
   * direction if it isn't already moving in one direction or the other.
   * @param floorNum
   */
  private void setDirection(int floorNum) {
    // if already moving in one direction return
    if (movingUp || movingDown) {
      return;
    }
    // if the floor number entered is above the current floor, 
    // set the direction to up, otherwise set it to down
    if (floorNum > currentFloor) {
      movingUp = true;
    } else if (floorNum < currentFloor) {
      movingDown = true;
    }
  }
  
  /**
   * helper method which returns true if there are inside buttons pressed
   * for floors below the current floor.
   * @return boolean true if there are inside buttons below the current floor
   */
  private boolean insideButtonsBelow() {
    return !this.insideButtons.headSet(currentFloor).isEmpty();
  }
  
  /**
   * helper method which returns true if there are inside buttons pressed
   * for floors above the current floor.
   * @return boolean true if there are inside buttons above the current floor
   */
  private boolean insideButtonsAbove() {
    return !this.insideButtons.tailSet(currentFloor + 1).isEmpty();
  }
  
  /**
   * helper method that returns true if there are down buttons pressed below
   * the current floor.
   * @return boolean true if there are down buttons pressed below the current floor
   */
  private boolean outsideDownButtonsBelow() {
    return !this.outsideDownButtons.headSet(currentFloor).isEmpty();
  }
  
  /**
   * helper method that returns true if there are up buttons pressed above
   * the current floor.
   * @return boolean true if there are up buttons pressed above the current floor
   */
  private boolean outsideUpButtonsAbove() {
    return !this.outsideUpButtons.tailSet(currentFloor + 1).isEmpty();
  }
  
  /**
   * Prints the elevator stopping message.
   */
  private void printStoppingMsg() {
    System.out.printf("Elevator: Stopping at floor %d%n", currentFloor);
  }
  
  /**
   * Prints the elevator passing message.
   */
  private void printPassingMsg() {
    System.out.printf("Elevator: Passing floor %d%n", currentFloor);
  }
  
  /**
   * Prints the elevator waiting message
   */
  private void printWaitingMsg() {
    System.out.printf("Elevator: Nothing to do, just waiting on floor %d%n", currentFloor);
  }
}
