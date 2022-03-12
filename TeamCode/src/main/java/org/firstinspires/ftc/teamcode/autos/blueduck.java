package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.hardware.color.BLUE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;
import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;


@Autonomous
public class blueduck extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap, BLUE);
        Pose2d startPose = new Pose2d(-36, 66, Math.toRadians(90));
        Pose2d hubPose = new Pose2d(-35, 22, Math.toRadians(0));
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);


        drive.setPoseEstimate(startPose);

        while (!isStarted()) {
            robit.intake.setMotorPower(gamepad1.left_trigger - gamepad1.right_trigger);
            if (gamepad1.dpad_left) {
                //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
            } else if (gamepad1.dpad_right) {
                robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
            }
            telemetry.addData("pos", robit.camera.tsepipeline.getTSEpos());
            telemetry.update();
        }
        TSEpipeline.TSEpos pos = robit.camera.tsepipeline.getTSEpos();
        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(startPose)

                .setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robit.intake.setMotorPower(-1);
                })

                .lineToConstantHeading(new Vector2d(-70, 53))
                .lineToConstantHeading(new Vector2d(-70, 56), SampleMecanumDrive.getVelocityConstraint(5, 5, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.duck.spinDuckOtherPrimitive();
                })
                .waitSeconds(3)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.duck.stop();
                })
                .setReversed(true)
                .splineTo(new Vector2d(-65, 30), Math.toRadians(270))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                    robit.setLiftfromTSE(pos);
                })

                .splineTo(hubPose.vec(), hubPose.getHeading(),
                        SampleMecanumDrive.getVelocityConstraint(30, 30, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED);
                    robit.intake.setMotorPower(-1);
                })
                .waitSeconds(1)

                .setReversed(false)
                .splineTo(new Vector2d(-65, 30), Math.toRadians(90))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                    robit.intake.setMotorPower(0.7);
                })
                .splineTo(new Vector2d(-65, 50), Math.toRadians(45))

                .lineToLinearHeading(new Pose2d(-45, 50, Math.toRadians(45)), SampleMecanumDrive.getVelocityConstraint(15, 15, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .turn(Math.toRadians(90))
                .lineToLinearHeading(new Pose2d(-35, 63, Math.toRadians(135)), SampleMecanumDrive.getVelocityConstraint(15, 15, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .lineToLinearHeading(new Pose2d(-65, 63, Math.toRadians(135)), SampleMecanumDrive.getVelocityConstraint(15, 15, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .turn(Math.toRadians(45))
                .setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.intake.setMotorPower(1);
                })
                .splineTo(new Vector2d(-65, 30), Math.toRadians(270))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.intake.setMotorPower(0);
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                })
                .splineTo(hubPose.vec(), hubPose.getHeading())
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                    robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED);
                })
                .waitSeconds(1)

                .setReversed(false)
                .UNSTABLE_addTemporalMarkerOffset(1, () -> {

                    robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                })
                .splineTo(new Vector2d(-70, 35), Math.toRadians(90))

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
