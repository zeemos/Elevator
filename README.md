# Elevator - Simulator project

This is a simple project with 2 classes.  ElevatorDemo is the main entry point which when
run collects some commandline input from the user to create an instance of an Elevator class
and then instructs it on what to do based on user command line input.

# Usage

Either run the ElevatorDemo class from within your IDE,
or at the command line:

java com.bluestaq.codechallenge.ElevatorDemo

The command line interface will first prompt for the number of floors that the elevator
will service.  It will then prompt for the starting floor.  Once entered, these values 
cannot be changed.  
The interface will then enter a command input loop.
Possible commands are:
enter key w/o any input will pause the execution of the Elevator
'start' to start the Elevator going
'exit' to stop the Elevator and exit the program
'help' will print these command instructions again
button push command in the format {direction as U or D} : {floor number}
   For example U:10 to push the up button on the 10 floor or D:5 to push
   the down button on the fifth floor.

# Assumptions and Possible Improvements
The program assumes the floors are numbered 1 to the number of floors entered which doesn't
allow for special floor names like 'P' or 'B' for a sub-level.
It also assumes that each floor has one door with 1 up and 1 down button.  A possible
improvement would be to allow for a back door like some service elevators have which
would give another set of up and down buttons for that door.
Another improvement might take into account special floors that may possibly be disabled and
therefore skipped or some floors that require special access like a badge swipe.
