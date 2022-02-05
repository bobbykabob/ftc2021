package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;
import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class redwarehouse extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap);

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        Pose2d startPose = new Pose2d(12, -66, Math.toRadians(90));
        Pose2d hubPose = new Pose2d(-12, -40, Math.toRadians(90));
        drive.setPoseEstimate(startPose);
        while (!isStarted()) {
            robit.intake.setMotorPower(gamepad1.left_trigger - gamepad1.right_trigger);
            if (gamepad1.dpad_left) {
                robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
            } else if (gamepad1.dpad_right) {
                robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
            }
            telemetry.addData("pos", robit.camera.tsepipeline.getTSEpos());
            telemetry.update();
        }

        TSEpipeline.TSEpos pos = robit.camera.tsepipeline.getTSEpos();

        TrajectorySequence start = drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);

                })
                .UNSTABLE_addTemporalMarkerOffset(0.5, ()-> {
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED_PIVOT);

                })

                .splineTo(new Vector2d(-9, -48), Math.toRadians(90))
                .turn(Math.toRadians(180))
                .setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    robit.setLiftfromTSE(pos);
                })
                .splineTo(hubPose.vec(), hubPose.getHeading())
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    //put cube in
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                })
                .setReversed(false)
                .waitSeconds(0.5)
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    //put cube in
                    robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                })
                .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                    //put lift down and put servos back
                    robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                })
                .splineTo(new Vector2d(20, -66), Math.toRadians(0))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.intake.setMotorPower(1);

                })
                .splineTo(new Vector2d(50, -66), Math.toRadians(0))

                .build();


        drive.followTrajectorySequenceAsync(start);



        long startTime = System.currentTimeMillis();
        while (opModeIsActive()) {

            if (!drive.isBusy()) {
                long currentTime = System.currentTimeMillis();

                if ((30000 - (currentTime - startTime)) > 5000) {

                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .setReversed(true)
                            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                                //put slide up
                                robit.intake.setMotorPower(-1);
                                robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                            })
                            .splineTo(new Vector2d(20, -66), Math.toRadians(180))
                            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                //put cube in
                                robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED_START);
                            })
                            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                                //put cube in
                                robit.intake.setMotorPower(0);
                                robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                            })
                            .splineTo(hubPose.vec(), hubPose.getHeading())
                            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                                //put cube in
                                robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);

                            })
                            .waitSeconds(1)
                            .UNSTABLE_addTemporalMarkerOffset(1, () -> {
                                //put lift down and put servos back
                                robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                                robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                            })
                            .setReversed(false)
                            .splineTo(new Vector2d(20, -66), Math.toRadians(0))
                            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                //turn intake on
                                robit.intake.setMotorPower(1);
                            })
                            .splineTo(new Vector2d(50, -66), Math.toRadians(0))
                            .build());
                }



            }
            drive.update();
            robit.update();
            telemetry.addLine("running");
            telemetry.update();
        }

    }
}
