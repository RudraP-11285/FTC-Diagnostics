# FTC-Diagnostics
This tool makes it easy for FTC teams to test all motors and servos before a match, after a rebuild, or during debugging. It checks wiring, configuration, and mechanical movement in just a few minutes.

What It Does:

- Tests motors one by one and asks if they moved correctly
- Supports motor groups (like drivetrains or lifts), with options to reverse specific motors
- Uses encoders to check if the motor is actually moving, not just spinning randomly
- Gives suggestions if something goes wrong (like a broken encoder or bad wiring)
- Creates a full summary at the end showing what passed and what failed

How To Set It Up:

1. Start with an existing FTC SDK project. If you don’t have one, clone the official base project: https://github.com/FIRST-Tech-Challenge/FtcRobotController

2. Download and add the 3 files from this repo to your "TeamCode/src/main/java/..." folder (or whichever folder you are storing your code in)

3. In each file, change the package name at the top (the first line) to match your project’s folder structure.

4. Create your own motor couplings. (Lists of motors that need to be ran together, such as drive train motors)

   Here's how:

   - In 'TestingCases.java' find the region which contains motor couplings.
   - Replace the existing couplings with one that match your motors.
   - Use the format to create new couplings, replacing "couplingName" with the name of the group (eg. drivetrain) and "motorName" with your   motor names.
   - If the motors should be moving in reverse, set the boolean to true. Otherwise, leave it as false.
  
   // === Motor Coupling: CouplingName ===
   MotorCoupling couplingName = new MotorCoupling();
   couplingName.addMotor(hardwareMap.get(DcMotor.class, "motorName"), true);
   couplingName.addMotor(hardwareMap.get(DcMotor.class, "motorName"), false);
   motorsAlreadyUsed.addAll(couplingName.motors.keySet());

   telemetry.addLine("\uD83D\uDD17 Testing Coupled Motors: couplingName");
   telemetry.update();
   TestPlugin.testMotorGroup(this, couplingName, "couplingName", 0, 500, testResults);

5. Run the code via your driver station with a gamepad1 plugged in. Interact with buttons A and B.
   - It should be named "Test Case For: All Motors and Servos" under the TeleOp section.

Made by Rudra P.
Team 11285: PATENT PENDING
