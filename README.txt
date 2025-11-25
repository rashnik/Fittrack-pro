Online Fitness Tracking Application - Working Version (Pure Java, no external jars)
-----------------------------------------------------------------------------
This version uses Java serialization to persist data to 'data.ser' and does NOT require
any external JDBC drivers or additional jars. It should work out-of-the-box with Java 8+.

How to run:
1) Extract the zip and open a terminal in the 'src' folder.
   cd OnlineFitnessApp_Working/src
2) Compile all Java files:
   javac *.java
3) Run the application:
   java Main
The app will create 'data.ser' in the same folder to store users and workouts.
Default accounts (created automatically if empty):
  - Admin: admin / admin123
  - User:  user1 / pass123
