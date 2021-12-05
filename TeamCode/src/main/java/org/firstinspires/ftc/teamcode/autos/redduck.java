package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;

@Autonomous
public class redduck extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap);

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);
        Pose2d startPose = new Pose2d(-24, -66, Math.toRadians(270));
        drive.setPoseEstimate(startPose);
        telemetry.addLine("ready");
        telemetry.update();
        waitForStart();

        drive.followTrajectorySequenceAsync(
                drive.trajectorySequenceBuilder(startPose)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                        })
                        .setReversed(true)
                        .splineTo(new Vector2d(-18, -38), Math.toRadians(70))
                        .setReversed(false)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.outtake.setOuttake(outtake.boxPos.OUT);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                        })
                        .splineTo(new Vector2d(-60, -50), Math.toRadians(250))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            robit.duck.spinDuck();
                        })
                        .waitSeconds(5)
                        .setReversed(true)
                        .splineTo(new Vector2d(-60, -35), Math.toRadians(90))
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
