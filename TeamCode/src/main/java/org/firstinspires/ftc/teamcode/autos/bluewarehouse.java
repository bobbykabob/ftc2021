package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;

@Autonomous
public class bluewarehouse extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap);

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        Pose2d startPose = new Pose2d(0, 66, Math.toRadians(90));
        Vector2d hubVector = new Vector2d(-4, 38);
        drive.setPoseEstimate(startPose);
        telemetry.addLine("ready");
        telemetry.update();
        waitForStart();

        drive.followTrajectorySequenceAsync(
                drive.trajectorySequenceBuilder(startPose)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //lift slide
                            robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                        })
                        .setReversed(true)
                        .splineTo(hubVector, Math.toRadians(250))
                        .setReversed(false)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //put cube in
                            robit.outtake.setOuttake(outtake.boxPos.OUT);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                            //put lift down and put servos back
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                            robit.outtake.setOuttake(outtake.boxPos.IN);
                        })
                        .splineTo(new Vector2d(20, 66), Math.toRadians(0))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            robit.intake.setMotorPower(1);
                            //turn intake on
                        })
                        .splineTo(new Vector2d(50, 66), Math.toRadians(0))
                        .waitSeconds(2)
                        .setReversed(true)
                        .splineTo(new Vector2d(20, 66), Math.toRadians(180))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            //put slide up
                            robit.intake.setMotorPower(0);
                            robit.outtake.setOuttake(outtake.boxPos.OUT);

                        })

                        .splineTo(hubVector, Math.toRadians(250))
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //put cube in
                            robit.outtake.setOuttake(outtake.boxPos.OUT);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                            //put lift down and put servos back
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                            robit.outtake.setOuttake(outtake.boxPos.IN);
                        })
                        .setReversed(false)
                        .splineTo(new Vector2d(20, 66), Math.toRadians(0))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            //turn intake on
                            robit.intake.setMotorPower(1);
                        })
                        .splineTo(new Vector2d(50, 66), Math.toRadians(0))
                        .waitSeconds(2)
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
