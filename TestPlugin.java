package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;
import java.util.Map;

public class TestPlugin {

    public static void testMotor(LinearOpMode opMode, DcMotor motor, String name, int minEncoder, int maxEncoder, List<String> results) throws InterruptedException {
        final double power = 0.5;
        final int tolerance = 20;

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int startPos = motor.getCurrentPosition();

        motor.setTargetPosition(maxEncoder);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
        Thread.sleep(2000);
        int forward = motor.getCurrentPosition();

        motor.setTargetPosition(minEncoder);
        motor.setPower(power);
        Thread.sleep(2000);
        int backward = motor.getCurrentPosition();

        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        boolean encoderMoved = Math.abs(forward - startPos) > tolerance &&
                Math.abs(backward - forward) > tolerance;

        opMode.telemetry.addLine("Did the motor '" + name + "' move?");
        opMode.telemetry.addLine("• Press A if it moved correctly.");
        opMode.telemetry.addLine("• Press B if it did NOT move.");
        opMode.telemetry.update();

        while (opMode.opModeIsActive() && !opMode.gamepad1.a && !opMode.gamepad1.b) opMode.idle();

        if (opMode.gamepad1.a && encoderMoved) {
            opMode.telemetry.addLine("✅ Motor + Encoder working.");
            results.add("✅ " + name + " — Passed");
        } else if (opMode.gamepad1.a) {
            opMode.telemetry.addLine("⚠️ Encoder not changing.");
            opMode.telemetry.addLine("• Suggestion: Check encoder wiring or unplugged port.");
            results.add("⚠️ " + name + " — Encoder not responding");
        } else if (opMode.gamepad1.b && encoderMoved) {
            opMode.telemetry.addLine("⚠️ Motor moved (encoder changed), but not visible.");
            opMode.telemetry.addLine("• Suggestion: Mechanically disconnected?");
            results.add("⚠️ " + name + " — Mechanical issue?");
        } else {
            opMode.telemetry.addLine("❌ Motor not moving + no encoder change.");
            opMode.telemetry.addLine("• Suggestion: Check wiring, config, or replace motor.");
            results.add("❌ " + name + " — No movement");
        }

        opMode.telemetry.update();
        Thread.sleep(2000);
    }

    public static void testServo(LinearOpMode opMode, Servo servo, String name, List<String> results) throws InterruptedException {
        opMode.telemetry.addLine("Sweeping servo: " + name);
        opMode.telemetry.update();

        try {
            servo.setPosition(0);
            Thread.sleep(750);
            servo.setPosition(1);
            Thread.sleep(750);
            servo.setPosition(0.5);
        } catch (Exception e) {
            opMode.telemetry.addLine("❌ Servo error: " + e.getMessage());
            opMode.telemetry.addLine("• Suggestion: Check wiring or port config.");
            results.add("❌ " + name + " — Servo error: " + e.getMessage());
            opMode.telemetry.update();
            Thread.sleep(3000);
            return;
        }

        opMode.telemetry.addLine("Did the servo '" + name + "' move?");
        opMode.telemetry.addLine("• Press A if yes.");
        opMode.telemetry.addLine("• Press B if no.");
        opMode.telemetry.update();

        while (opMode.opModeIsActive() && !opMode.gamepad1.a && !opMode.gamepad1.b) opMode.idle();

        if (opMode.gamepad1.a) {
            opMode.telemetry.addLine("✅ Servo working.");
            results.add("✅ " + name + " — Passed");
        } else {
            opMode.telemetry.addLine("❌ Servo not moving.");
            opMode.telemetry.addLine("• Suggestion: Check wiring, horn, or config.");
            results.add("❌ " + name + " — No movement");
        }

        opMode.telemetry.update();
        Thread.sleep(2000);
    }

    public static void testMotorGroup(LinearOpMode opMode, MotorCoupling group, String groupName, int minEncoder, int maxEncoder, List<String> results) throws InterruptedException {
        final double power = 0.5;
        final int tolerance = 20;

        for (DcMotor motor : group.motors.keySet()) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        for (Map.Entry<DcMotor, Boolean> entry : group.motors.entrySet()) {
            DcMotor motor = entry.getKey();
            boolean reversed = entry.getValue();
            motor.setTargetPosition(reversed ? -maxEncoder : maxEncoder);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(power);
        }
        Thread.sleep(2000);

        for (Map.Entry<DcMotor, Boolean> entry : group.motors.entrySet()) {
            DcMotor motor = entry.getKey();
            boolean reversed = entry.getValue();
            motor.setTargetPosition(reversed ? -minEncoder : minEncoder);
            motor.setPower(power);
        }
        Thread.sleep(2000);

        for (DcMotor motor : group.motors.keySet()) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        boolean anyMoved = group.motors.keySet().stream()
                .anyMatch(m -> Math.abs(m.getCurrentPosition()) > tolerance);

        opMode.telemetry.addLine("Did the coupled motors move? [" + groupName + "]");
        opMode.telemetry.addLine("• Press A if yes.");
        opMode.telemetry.addLine("• Press B if no.");
        opMode.telemetry.update();

        while (opMode.opModeIsActive() && !opMode.gamepad1.a && !opMode.gamepad1.b) opMode.idle();

        if (opMode.gamepad1.a && anyMoved) {
            opMode.telemetry.addLine("✅ Coupled motors + encoders OK.");
            results.add("✅ Coupling [" + groupName + "] — Passed");
        } else if (opMode.gamepad1.a && !anyMoved) {
            opMode.telemetry.addLine("⚠️ Movement seen, but encoders didn't change.");
            results.add("⚠️ Coupling [" + groupName + "] — Encoder issue?");
        } else if (opMode.gamepad1.b && anyMoved) {
            opMode.telemetry.addLine("⚠️ Encoder changed, but no visible movement.");
            results.add("⚠️ Coupling [" + groupName + "] — Mechanical disconnect?");
        } else {
            opMode.telemetry.addLine("❌ Coupled motors not responding.");
            results.add("❌ Coupling [" + groupName + "] — No movement");
        }

        opMode.telemetry.update();
        Thread.sleep(2000);
    }
}
