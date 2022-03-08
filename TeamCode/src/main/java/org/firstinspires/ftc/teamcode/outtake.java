package org.firstinspires.ftc.teamcode;

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
    public static double mid = 300;
    public static double up = 630;

    public static double openPivot = 0.55;
    public static double closedPivot = 0;
    public static double inHoriz = 0.08;
    public static double outHoriz = 0.4;

    public static double minP = -0.1;

    public static int MS_between_presses = 500;

    //lift

    public static double p;

    public enum liftPos {
        BOTTOM,
        MID,
        UP
    }

    public enum outtakePos {
        IN_OPEN,
        IN_OPEN_SLIDE,
        OUT_OPEN,
        OUT_CLOSED,
    }


    private liftPos targetLiftPos;
    private DcMotor lift1;
    private DcMotor lift2;

    private Servo outtakePivot;
    private Servo outtakeHoriz;
    private HardwareMap hw;


    public outtakePos currentPos = outtakePos.IN_OPEN;
    private long start_time = System.currentTimeMillis();
    private long prevClick = System.currentTimeMillis();
    public outtake(HardwareMap ahw) {
        targetLiftPos = liftPos.BOTTOM;
        hw = ahw;
        outtakePivot = hw.get(Servo.class, "outtakePivot");
        outtakeHoriz = hw.get(Servo.class, "outtakeHoriz");
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
        if (p < minP) {
            p = minP;
        }
        setLiftMotorPower(p);


        if (System.currentTimeMillis() - start_time > 500 && currentPos == outtakePos.IN_OPEN) {
            currentPos = outtakePos.IN_OPEN_SLIDE;
            setOuttake(currentPos);
        }
        //outtake update


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


    public void iterateOuttakeForward() {
        if ((System.currentTimeMillis() - prevClick) < MS_between_presses) return;

        prevClick = System.currentTimeMillis();
        switch (currentPos) {
            case IN_OPEN:
                currentPos = outtakePos.OUT_OPEN;
                break;
            case IN_OPEN_SLIDE:
                currentPos = outtakePos.OUT_OPEN;
                break;
            case OUT_OPEN:
                currentPos = outtakePos.OUT_CLOSED;
                break;
            case OUT_CLOSED:
                currentPos = outtakePos.IN_OPEN;
                start_time = System.currentTimeMillis();
                break;

        }
        setOuttake(currentPos);
    }

    public void iterateOuttakeBackward() {
        if ((System.currentTimeMillis() - prevClick) < MS_between_presses) return;

        prevClick = System.currentTimeMillis();
        switch (currentPos) {
            case IN_OPEN:
                currentPos = outtakePos.OUT_CLOSED;
                break;

            case OUT_OPEN:
                currentPos = outtakePos.IN_OPEN;
                break;
            case OUT_CLOSED:
                currentPos = outtakePos.OUT_OPEN;
                break;

        }
        setOuttake(currentPos);

    }

    public void setOuttake(outtakePos pos) {
        currentPos = pos;
        switch (pos) {
            case IN_OPEN:
                outtakePivot.setPosition(openPivot);
                outtakeHoriz.setPosition(inHoriz);
                break;
            case IN_OPEN_SLIDE:
                setTargetLiftPos(liftPos.BOTTOM);
                break;
            case OUT_OPEN:
                outtakePivot.setPosition(openPivot);
                outtakeHoriz.setPosition(outHoriz);
                setTargetLiftPos(liftPos.UP);
                break;
            case OUT_CLOSED:
                outtakePivot.setPosition(closedPivot);
                outtakeHoriz.setPosition(outHoriz);
                break;

        }

    }




}
