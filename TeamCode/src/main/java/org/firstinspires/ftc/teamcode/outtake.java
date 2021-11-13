package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.outtake.boxPos.IN;
import static org.firstinspires.ftc.teamcode.outtake.boxPos.OUT;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;
import java.util.List;

@Config
public class outtake {


    public static double kp = 0.003;
    public static double bottom = 0;
    public static double mid = 450;
    public static double up = 900;

    public static double in1 = 0;
    public static double out1 = 0.5;
    public static double in2 = 0.06;
    public static double out2 = 0.5;

    public enum liftPos {
        BOTTOM,
        MID,
        UP
    }

    public enum boxPos {
        IN,
        OUT
    }

    private liftPos targetLiftPos;
    private DcMotor lift1;
    private DcMotor lift2;

    private Servo outtake1;
    private Servo outtake2;
    private HardwareMap hw;
    public double p;
    public outtake(HardwareMap ahw) {
        targetLiftPos = liftPos.BOTTOM;
        hw = ahw;
        outtake1 = hw.get(Servo.class, "outtake1");
        outtake2 = hw.get(Servo.class, "outtake2");
        lift1 = hw.get(DcMotor.class, "lift1");
        lift2 = hw.get(DcMotor.class, "lift2");
        lift1.setDirection(DcMotorSimple.Direction.REVERSE);
        lift1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void update() {


        double targetPos;
        switch (targetLiftPos) {
            case BOTTOM:
                targetPos = bottom;
                break;
            case MID:
                targetPos = mid;
                break;
            case UP:
                targetPos = up;
                break;
            default:
                targetPos = bottom;
                break;
        }

        double averagePos = (getCurrentPosition().get(0) + getCurrentPosition().get(1))/2;
        p = kp * (targetPos - averagePos);


        setLiftMotorPower(p);

    }

    public void setTargetLiftPos(liftPos pos) {
        targetLiftPos = pos;
    }

    public void setLiftMotorPower(double power) {
        lift1.setPower(power);
        lift2.setPower(power);
    }




    public List<Integer> getCurrentPosition() {
        return Arrays.asList(lift1.getCurrentPosition(), lift2.getCurrentPosition());
    }

    public void setOuttake(boxPos box) {
        switch (box) {
            case IN:
                outtake2.setPosition(in2);

                break;
            case OUT:
                outtake2.setPosition(out2);
                break;
        }

    }




}
