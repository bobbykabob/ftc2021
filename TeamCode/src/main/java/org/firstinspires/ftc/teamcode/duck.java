package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class duck {
    private DcMotor duck;

    public static double initalPower = 0.8;
    public static double endPower = 1;
    public static long MS_between_Powers = 1000;
    private boolean isOn = false;
    private long startTime = System.currentTimeMillis();

    private HardwareMap hw;
    public duck(HardwareMap ahw) {
        hw = ahw;
       duck = hw.get(DcMotor.class, "duck");
    }

    public void update() {
        if (isOn) {
            if ((System.currentTimeMillis() - startTime) > MS_between_Powers) {
                duck.setPower(endPower *(duck.getPower() / initalPower));
            }
        }
    }
    public void spinDuck() {
        isOn = true;
        startTime = System.currentTimeMillis();
        duck.setPower(initalPower);
    }

    public void spinDuckOther() {
        isOn = true;
        startTime = System.currentTimeMillis();
        duck.setPower(-initalPower);
    }

    public void stop() {
        isOn = false;
        duck.setPower(0);

    }
}
