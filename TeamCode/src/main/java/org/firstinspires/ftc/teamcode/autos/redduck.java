package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;

@Autonomous
public class redduck extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap);

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        telemetry.addLine("ready");
        telemetry.update();
        waitForStart();

        drive.followTrajectorySequenceAsync(
                drive.trajectorySequenceBuilder(new Pose2d(-24, -66, Math.toRadians(270)))
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //lift slide
                        })
                        .setReversed(true)
                        .splineTo(new Vector2d(-18, -38), Math.toRadians(70))
                        .setReversed(false)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //put cube in
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                            //put lift down
                        })
                        .splineTo(new Vector2d(-60, -50), Math.toRadians(250))
                        .build()
        );

        while (opModeIsActive()) {
            drive.update();
            robit.update();
            telemetry.addLine("running");
            telemetry.update();
        }

    }
}
