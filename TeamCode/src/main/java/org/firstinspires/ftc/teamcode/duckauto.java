package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class duckauto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        hardware robit = new hardware(hardwareMap);
        autoDrive drive = new autoDrive(hardwareMap, telemetry);

        waitForStart();


        drive.moveForward(10);

        double currentTime = System.currentTimeMillis();

        double startTime = currentTime;
        while (currentTime -startTime < 10000 && opModeIsActive()) {
            currentTime = System.currentTimeMillis();
            robit.duck.spinDuck();
        }
        robit.duck.stop();
        drive.moveForward(-20);

    }
}
