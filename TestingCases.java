package pedroPathing.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "Test Case For: All Motors and Servos", group = "Testing")
public class TestingCases extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("\uD83D\uDEA6 Test Suite Ready");
        telemetry.addLine("Press ▶ to begin testing all connected motors and servos.");
        telemetry.update();
        waitForStart();

        telemetry.clearAll();
        Set<DcMotor> motorsAlreadyUsed = new HashSet<>();
        List<String> testResults = new ArrayList<>();

        // ADD MOTOR COUPLINGS BELOW

        // === Motor Coupling: Drivetrain ===
        MotorCoupling drivetrain = new MotorCoupling();
        drivetrain.addMotor(hardwareMap.get(DcMotor.class, "leftFront"), false);
        drivetrain.addMotor(hardwareMap.get(DcMotor.class, "leftRear"), false);
        drivetrain.addMotor(hardwareMap.get(DcMotor.class, "rightFront"), true);
        drivetrain.addMotor(hardwareMap.get(DcMotor.class, "rightRear"), true);
        motorsAlreadyUsed.addAll(drivetrain.motors.keySet());

        telemetry.addLine("\uD83D\uDD17 Testing Coupled Motors: drivetrain");
        telemetry.update();
        TestPlugin.testMotorGroup(this, drivetrain, "drivetrain", 0, 500, testResults);

        // === Motor Coupling: Lift ===
        MotorCoupling lift = new MotorCoupling();
        lift.addMotor(hardwareMap.get(DcMotor.class, "verticalLeft"), true);
        lift.addMotor(hardwareMap.get(DcMotor.class, "verticalRight"), false);
        motorsAlreadyUsed.addAll(lift.motors.keySet());

        telemetry.addLine("\uD83D\uDD17 Testing Coupled Motors: lift");
        telemetry.update();
        TestPlugin.testMotorGroup(this, lift, "lift", 0, 500, testResults);

        // ADD MOTOR COUPLINGS ABOVE

        // === Test Uncoupled Motors ===
        telemetry.addLine("\uD83D\uDD27 Testing Uncoupled Motors...");
        telemetry.update();

        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            String name = entry.getKey();
            DcMotor motor = entry.getValue();

            if (motorsAlreadyUsed.contains(motor)) continue;

            telemetry.addLine("Now testing motor: " + name);
            telemetry.update();

            try {
                TestPlugin.testMotor(this, motor, name, 0, 500, testResults);
            } catch (Exception e) {
                telemetry.addLine("❌ Error testing motor " + name + ": " + e.getMessage());
                testResults.add("❌ " + name + " — Error: " + e.getMessage());
                telemetry.update();
                sleep(3000);
            }
        }

        // === Test All Servos ===
        telemetry.addLine("\uD83D\uDD04 Beginning Servo Tests...");
        telemetry.update();

        for (Map.Entry<String, Servo> entry : hardwareMap.servo.entrySet()) {
            String name = entry.getKey();
            Servo servo = entry.getValue();

            telemetry.addLine("Now testing servo: " + name);
            telemetry.update();

            try {
                TestPlugin.testServo(this, servo, name, testResults);
            } catch (Exception e) {
                telemetry.addLine("❌ Error testing servo " + name + ": " + e.getMessage());
                testResults.add("❌ " + name + " — Error: " + e.getMessage());
                telemetry.update();
                sleep(3000);
            }
        }

        // === Final Report ===
        telemetry.clearAll();
        telemetry.addLine("✅ Testing complete. Summary:");
        for (String result : testResults) {
            telemetry.addLine(result);
        }
        telemetry.update();
        while (opModeIsActive()) idle();
    }
}
