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

    public static double inPivot = 0.2;
    public static double outPivot = 0.8;
    public static double inHoriz = 1;
    public static double outHoriz = 0;
    public static double openClaw = 1;
    public static double closedClaw = 0.5;

    public static int timeBetweenSTART_PIVOT = 300;
    public static int MS_between_presses = 500;

    //lift
    public static double p;

    public enum liftPos {
        BOTTOM,
        MID,
        UP
    }

    public enum outtakePos {
        IN_OPEN_START,
        IN_OPEN_END,
        IN_CLOSED,
        OUT_CLOSED_START,
        OUT_CLOSED_PIVOT,
        OUT_OPEN,
    }

    private enum outtakedirection {
        forward,
        backward,
        none
    }
    private static outtakedirection outtakeDirection;
    private liftPos targetLiftPos;
    private DcMotor lift1;
    private DcMotor lift2;

    private Servo outtakePivot;
    private Servo outtakeClaw;
    private Servo outtakeHoriz;
    private HardwareMap hw;


    public outtakePos currentPos = outtakePos.IN_OPEN_START;
    private long start_time = System.currentTimeMillis();
    private long prevClick = System.currentTimeMillis();
    public outtake(HardwareMap ahw) {
        targetLiftPos = liftPos.BOTTOM;
        hw = ahw;
        outtakePivot = hw.get(Servo.class, "outtakePivot");
        outtakeClaw = hw.get(Servo.class, "outtakeClaw");
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
        setLiftMotorPower(p);


        //outtake update
        if (outtakeDirection == outtakedirection.forward) {
            if (currentPos == outtakePos.OUT_CLOSED_START) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - start_time > timeBetweenSTART_PIVOT) {
                    currentPos = outtakePos.OUT_CLOSED_PIVOT;
                    setOuttake(currentPos);
                }

            } else if (currentPos == outtakePos.IN_OPEN_START) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - start_time > timeBetweenSTART_PIVOT) {
                    currentPos = outtakePos.IN_OPEN_END;
                    setOuttake(currentPos);
                }
            }
        } else if (outtakeDirection == outtakedirection.backward) {
            if (currentPos == outtakePos.OUT_CLOSED_START) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - start_time > timeBetweenSTART_PIVOT) {
                    currentPos = outtakePos.IN_CLOSED;
                    setOuttake(currentPos);
                }

            } else if (currentPos == outtakePos.IN_OPEN_START) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - start_time > timeBetweenSTART_PIVOT) {
                    currentPos = outtakePos.OUT_OPEN;
                    setOuttake(currentPos);
                }
            }
        }

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
        outtakeDirection = outtakedirection.forward;
        prevClick = System.currentTimeMillis();
        switch (currentPos) {
            case IN_OPEN_START:
                currentPos = outtakePos.IN_CLOSED;
                break;
            case IN_OPEN_END:
                currentPos = outtakePos.IN_CLOSED;
                break;
            case IN_CLOSED:
                start_time = System.currentTimeMillis();
                currentPos = outtakePos.OUT_CLOSED_START;
                break;
            case OUT_CLOSED_START:
                currentPos = outtakePos.OUT_OPEN;
                break;
            case OUT_CLOSED_PIVOT:
                currentPos = outtakePos.OUT_OPEN;
                break;
            case OUT_OPEN:
                start_time = System.currentTimeMillis();
                currentPos = outtakePos.IN_OPEN_START;
                break;
        }
        setOuttake(currentPos);
    }

    public void iterateOuttakeBackward() {
        if ((System.currentTimeMillis() - prevClick) < MS_between_presses) return;
        outtakeDirection = outtakedirection.backward;
        prevClick = System.currentTimeMillis();
        start_time = System.currentTimeMillis();
        switch (currentPos) {
            case IN_OPEN_START:
                currentPos = outtakePos.OUT_CLOSED_PIVOT;
                break;
            case IN_OPEN_END:
                currentPos = outtakePos.IN_OPEN_START;
                break;
            case IN_CLOSED:
                currentPos = outtakePos.IN_OPEN_END;
                break;
            case OUT_CLOSED_START:
                currentPos = outtakePos.IN_OPEN_END;
                break;
            case OUT_CLOSED_PIVOT:
                currentPos = outtakePos.OUT_CLOSED_START;
                break;
            case OUT_OPEN:
                currentPos = outtakePos.OUT_CLOSED_PIVOT;
                break;
        }
        setOuttake(currentPos);

    }

    public void setOuttake(outtakePos pos) {
        currentPos = pos;
        start_time = System.currentTimeMillis();
        switch (pos) {
            case IN_OPEN_START:
                outtakePivot.setPosition(inPivot);
                outtakeClaw.setPosition(closedClaw);
                outtakeHoriz.setPosition(inHoriz);
                setTargetLiftPos(liftPos.BOTTOM);
            case IN_OPEN_END:
                outtakePivot.setPosition(inPivot);
                outtakeClaw.setPosition(openClaw);
                outtakeHoriz.setPosition(inHoriz);
                setTargetLiftPos(liftPos.BOTTOM);
                break;
            case IN_CLOSED:
                outtakePivot.setPosition(inPivot);
                outtakeClaw.setPosition(closedClaw);
                outtakeHoriz.setPosition(inHoriz);
                break;
            case OUT_CLOSED_START:
                outtakePivot.setPosition(inPivot);
                outtakeClaw.setPosition(closedClaw);
                outtakeHoriz.setPosition(outHoriz);
                break;
            case OUT_CLOSED_PIVOT:
                outtakePivot.setPosition(outPivot);
                outtakeClaw.setPosition(closedClaw);
                outtakeHoriz.setPosition(outHoriz);
                setTargetLiftPos(liftPos.UP);
                break;
            case OUT_OPEN:
                outtakePivot.setPosition(outPivot);
                outtakeClaw.setPosition(openClaw);
                outtakeHoriz.setPosition(outHoriz);
                break;
        }

    }




}
