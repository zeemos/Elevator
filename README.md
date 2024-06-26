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

Example session:

Enter number of floors (2-100):
10
Enter starting floor (1-10):
5
Valid commands: 'start', 'exit', 'help' or enter a floor number
To simulate a floor button inside simply enter the floor number.
To simulate a floor button outside the elevator enter either U: or D: and then the floor number like 'U:10' or 'D:5'
To pause execution and add more commands, just hit the 'Enter' key
6
10
D:5
U:2
start
Elevator: Starting
Elevator: Stopping at floor 6
Elevator: Passing floor 7
Elevator: Passing floor 8
Elevator: Passing floor 9
Elevator: Stopping at floor 10
Elevator: Passing floor 9
Elevator: Passing floor 8
Elevator: Passing floor 7
Elevator: Passing floor 6
Elevator: Stopping at floor 5
Elevator: Passing floor 4
Elevator: Passing floor 3
Elevator: Stopping at floor 2
Elevator: Nothing to do, just waiting on floor 2

Paused - Enter more commands:
exit
Goodbye
Elevator: shutting down


# Assumptions and Possible Improvements
The program assumes the floors are numbered 1 to the number of floors entered which doesn't
allow for special floor names like 'P' or 'B' for a sub-level.
It also assumes that each floor has one door with 1 up and 1 down button.  A possible
improvement would be to allow for a back door like some service elevators have which
would give another set of up and down buttons for that door.
Another improvement might take into account special floors that may possibly be disabled and
therefore skipped or some floors that require special access like a badge swipe.
