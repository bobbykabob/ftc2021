package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class TSEarm {

    //1st number tes, 2nd number pivot
    public static double pick_up_ready_pos = 0.08;
    public static double pick_up_ready_pos_pivot = 0.22;
    public static double picked_up_pos = 0.08;
    public static double picked_up_pos_pivot = 0.1;
    public static double store_pos = 0.9;
    public static double store_pivot = 1;
    public static double cap_pos = 0.6;
    public static double cap_pivot = 0.6;
    public static double cap_drop_pos = 0.6;
    public static double cap_drop_pivot = 0.6;


    private pos currentPos = pos.STORE;

    private long lastPress;
    public static int MS_between_presses = 500;

    public pos getPos() {
        return currentPos;
    }

    public enum pos {
        PICK_UP_READY,
        PICKED_UP,
        STORE,
        CAP,
        CAP_DROP
    }
    private Servo tse, tse2, tsePivot;
    private HardwareMap hw;
    public TSEarm(HardwareMap ahw) {
        hw = ahw;
        tse = hw.get(Servo.class, "tse");
        tse2 = hw.get(Servo.class, "tse2");
        tsePivot = hw.get(Servo.class, "tsepivot");
        move(pos.STORE);
        lastPress = System.currentTimeMillis();
    }

    private void setTSE(double pos) {
        tse.setPosition(pos);
        tse2.setPosition(1 - pos);
    }
    public void move(pos position) {
        switch (position) {
            case PICK_UP_READY:
                setTSE(pick_up_ready_pos);
                tsePivot.setPosition(pick_up_ready_pos_pivot);
                currentPos = pos.PICK_UP_READY;
                break;
            case PICKED_UP:
                setTSE(picked_up_pos);
                tsePivot.setPosition(picked_up_pos_pivot);
                currentPos = pos.PICKED_UP;
                break;
            case STORE:
                setTSE(store_pos);
                tsePivot.setPosition(store_pivot);
                currentPos = pos.STORE;
                break;
            case CAP:
                setTSE(cap_pos);
                tsePivot.setPosition(cap_pivot);
                currentPos = pos.CAP;
                break;
            case CAP_DROP:
                setTSE(cap_drop_pos);
                tsePivot.setPosition(cap_drop_pivot);
                currentPos = pos.CAP_DROP;
                break;
        }
    }

    public void iterateForward() {
        if (!enoughTime()) {
            return;
        }

        switch (currentPos) {
            case PICK_UP_READY:
                move(pos.PICKED_UP);
                break;
            case PICKED_UP:
                move(pos.STORE);
                break;
            case STORE:
                move(pos.CAP);
                break;
            case CAP:
                move(pos.CAP_DROP);
                break;
            case CAP_DROP:
                move(pos.PICK_UP_READY);
                break;
        }


    }

    public void iterateBackward() {
        if (!enoughTime()) {
            return;
        }

        switch (currentPos) {
            case PICK_UP_READY:
                move(pos.CAP_DROP);
                break;
            case PICKED_UP:
                move(pos.PICK_UP_READY);
                break;
            case STORE:
                move(pos.PICKED_UP);
                break;
            case CAP:
                move(pos.STORE);
                break;
            case CAP_DROP:
                move(pos.CAP);
                break;
        }
    }

    private boolean enoughTime() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > MS_between_presses) {
            lastPress = currentTime;
            return true;
        } else {
            return false;
        }
    }


}
