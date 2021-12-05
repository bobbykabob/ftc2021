package org.firstinspires.ftc.teamcode.outdated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outdated.autoDrive;

@Disabled
public class parkauto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        hardware robit = new hardware(hardwareMap);
        autoDrive drive = new autoDrive(hardwareMap, telemetry);

        waitForStart();



        drive.moveForward(-40);

    }
}
