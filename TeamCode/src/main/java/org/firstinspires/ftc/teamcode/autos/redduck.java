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
        Pose2d startPose = new Pose2d(-36, -66, Math.toRadians(90));
        Pose2d hubPose = new Pose2d(-14, -38, Math.toRadians(70));

        drive.setPoseEstimate(startPose);
        telemetry.addLine("ready");
        telemetry.update();
        waitForStart();

        drive.followTrajectorySequenceAsync(
                drive.trajectorySequenceBuilder(startPose)
                        .splineTo(new Vector2d(-62, -63), Math.toRadians(225))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            robit.duck.spinDuckOther();
                        })
                        .waitSeconds(3)
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.setLiftfromTSE();
                            robit.duck.stop();

                        })

                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.outtake.setOuttake(outtake.boxPos.OUT);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            robit.outtake.setOuttake(outtake.boxPos.IN);
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                            robit.intake.setMotorPower(1);
                            //put down slides & turn on intake
                        })

                        .setReversed(false)
                        .splineTo(new Vector2d(-60, -60), Math.toRadians(270))
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                            robit.intake.setMotorPower(0);
                            robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                        })
                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {

                            robit.outtake.setOuttake(outtake.boxPos.OUT);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                            robit.outtake.setOuttake(outtake.boxPos.IN);
                            //put down slides & turn on intake
                        })
                        .setReversed(false)
                        .splineTo(new Vector2d(-60, -35), Math.toRadians(135))
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
