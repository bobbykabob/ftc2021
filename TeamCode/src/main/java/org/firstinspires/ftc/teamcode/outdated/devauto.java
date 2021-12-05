package org.firstinspires.ftc.teamcode.outdated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;

@Disabled
public class devauto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        hardware robit = new hardware(hardwareMap);
        autoDrive drive = new autoDrive(hardwareMap, telemetry);

        waitForStart();

        drive.moveForward(-24);

        robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
        double currentTime = System.currentTimeMillis();
        double startTime = currentTime;
        while (currentTime -startTime < 500 && opModeIsActive()) {
            currentTime = System.currentTimeMillis();
            robit.update();

        }
        robit.outtake.setOuttake(outtake.boxPos.OUT);
        currentTime = System.currentTimeMillis();
        startTime = currentTime;
        while (currentTime -startTime < 1000 && opModeIsActive()) {
            robit.update();
            currentTime = System.currentTimeMillis();

        }
        robit.outtake.setOuttake(outtake.boxPos.IN);

        currentTime = System.currentTimeMillis();
        startTime = currentTime;
        robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
        while (currentTime -startTime < 500 && opModeIsActive()) {
            currentTime = System.currentTimeMillis();
            robit.update();
        }

        drive.moveForward(10);

        drive.drive.turn(Math.toRadians(90));
    }
}
