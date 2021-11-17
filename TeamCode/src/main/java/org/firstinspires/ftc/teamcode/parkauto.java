package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class parkauto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        hardware robit = new hardware(hardwareMap);
        autoDrive drive = new autoDrive(hardwareMap, telemetry);

        waitForStart();



        drive.moveForward(-40);

    }
}
