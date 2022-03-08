package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class duck {

    private enum duckMode {
        OFF,
        INITIAL,
        END
    }
    private duckMode currentMode = duckMode.OFF;
    private DcMotor duck;

    public static double initalPower = 0.4;
    public static double endPower = 1;
    public static double MS_between_Powers = 900;
    public static double MS_between_End = 300;


    private long startTime = System.currentTimeMillis();

    private HardwareMap hw;
    public duck(HardwareMap ahw) {
        currentMode = duckMode.OFF;
        hw = ahw;
       duck = hw.get(DcMotor.class, "duck");
        duck.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void update() {
        if (currentMode == duckMode.INITIAL) {
            if ((System.currentTimeMillis() - startTime) > MS_between_Powers) {
                startTime = System.currentTimeMillis();
                duck.setPower(endPower *(duck.getPower() / initalPower));
            }
        } else  if (currentMode == duckMode.END) {
            if ((System.currentTimeMillis() - startTime) > MS_between_End) {
                stop();
            }
        }
    }
    public void spinDuck() {
        if (currentMode == duckMode.INITIAL) return;
        currentMode = duckMode.INITIAL;
        startTime = System.currentTimeMillis();
        duck.setPower(initalPower);
    }

    public void spinDuckOther() {
        if (currentMode == duckMode.INITIAL) return;
        currentMode = duckMode.INITIAL;
        startTime = System.currentTimeMillis();
        duck.setPower(-initalPower);
    }

    public void stop() {
        currentMode = duckMode.END;
        duck.setPower(0);

    }
}
