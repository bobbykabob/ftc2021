package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;

import java.util.List;
@Config
public class autoDrive {
    public static double admissableError = 0.5;
    public static double tolerance = 1;
    public static double p = 0.05;
    SampleTankDrive drive;
    HardwareMap hw;
    Telemetry telemetry;

    private double targetLeft;
    private double targetRight;
    private double leftPower;
    private double rightPower;
    private List<Double> initialEncoderPos;
    private List<Double> currentEncoderPos;
    public autoDrive(HardwareMap ahw, Telemetry atelemetry) {
        telemetry = atelemetry;
        hw = ahw;
        drive = new SampleTankDrive(hw);

    }

    public void moveForward(double inches) {
        initialEncoderPos = drive.getWheelPositions();
        currentEncoderPos = initialEncoderPos;
        targetLeft = currentEncoderPos.get(0) + inches;
        targetRight = currentEncoderPos.get(1) + inches;
        while (!withinTolerance(currentEncoderPos.get(0), targetLeft) && !withinTolerance(currentEncoderPos.get(1), targetRight)) {
            updateDrive();
        }
        double initalTime = System.currentTimeMillis();

        double currentTime = initalTime;

        while ((currentTime - initalTime) < admissableError * 1000) {
            currentTime = System.currentTimeMillis();
            telemetry.addData("current TIme", currentTime - initalTime);
            updateDrive();
        }
        drive.setMotorPowers(0, 0);


    }

    public boolean withinTolerance(double currentPos, double targetPos) {
        if (Math.abs(currentPos - targetPos) < tolerance) {
            return true;
        } else {
            return false;
        }
    }

    public void updateDrive() {
        currentEncoderPos = drive.getWheelPositions();
        leftPower = (targetLeft - currentEncoderPos.get(0)) * p;
        rightPower = (targetRight - currentEncoderPos.get(1)) * p;

        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);
        telemetry.update();
        drive.setMotorPowers(leftPower, rightPower);
    }



}
