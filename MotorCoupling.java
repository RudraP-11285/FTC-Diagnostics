package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.LinkedHashMap;
import java.util.Map;

public class MotorCoupling {
    // Ordered map of motor -> isReversed
    public Map<DcMotor, Boolean> motors = new LinkedHashMap<>();

    public void addMotor(DcMotor motor, boolean reversed) {
        motors.put(motor, reversed);
    }
}
