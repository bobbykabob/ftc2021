package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class duck {
    private CRServo duck1;
    private CRServo duck2;
    private HardwareMap hw;
    public duck(HardwareMap ahw) {
        hw = ahw;
        duck1 = hw.get(CRServo.class, "duck1");
        duck2 = hw.get(CRServo.class, "duck2");
    }
    public void update() {

    }
    public void spinDuck() {
        duck1.setPower(1);
        duck2.setPower(1);
    }

    public void stop() {
        duck1.setPower(0);
        duck2.setPower(0);

    }
}
