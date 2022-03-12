package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;
import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class bluewarehouse extends LinearOpMode {

    private enum cyclePos {
        start,
        finding,
        out
    }
    private cyclePos cyclepos = cyclePos.start;
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap, hardware.color.BLUE);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(12, 66, Math.toRadians(90));
        Pose2d hubPose = new Pose2d(-16, 40, Math.toRadians(270));
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


        TrajectorySequence start = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                    robit.setLiftfromTSE(pos);
                })
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robit.intake.setMotorPower(-1);
                })
                .lineToConstantHeading(hubPose.vec())
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED);
                })
                .waitSeconds(0.25)
                .setReversed(false)
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                    robit.intake.setMotorPower(1);
                })
                .splineTo(new Vector2d(25, 66), Math.toRadians(0))
                .splineTo(new Vector2d(35, 66), Math.toRadians(0))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                    cyclepos = cyclePos.finding;
                })
                .splineTo(new Vector2d(55, 66), Math.toRadians(0),
                        SampleMecanumDrive.getVelocityConstraint(5, 5, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();


        drive.followTrajectorySequenceAsync(start);



        int numberofruns = 0;
        long startTime = System.currentTimeMillis();
        while (opModeIsActive()) {
            if (cyclepos == cyclePos.finding) {
                long currentTime = System.currentTimeMillis();
                if ((30000 - (currentTime - startTime)) < 5000) {
                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                robit.intake.setMotorPower(-1);
                            })
                            .back(1)

                            .build());
                } else if (robit.intake.intakedBlock()) {
                    Pose2d currentPose = drive.getPoseEstimate();


                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(currentPose)
                            .setReversed(true)
                            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                robit.intake.setMotorPower(-1);
                            })
                            .splineTo(new Vector2d(25, 66), Math.toRadians(180))
                            .UNSTABLE_addDisplacementMarkerOffset(0, ()-> {
                                robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                            })

                            .splineTo(new Vector2d(-18, 45), Math.toRadians(270), SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH),
                                    SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                            .splineTo(new Vector2d(-18, 40), Math.toRadians(270), SampleMecanumDrive.getVelocityConstraint(20, 20, DriveConstants.TRACK_WIDTH),
                                    SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))

                            .setReversed(false)
                            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED);
                            })
                            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                                robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                                robit.intake.setMotorPower(1);

                            })
                            .splineTo(new Vector2d(20, 66), Math.toRadians(0))
                            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                robit.intake.setMotorPower(1);
                            })
                            .splineTo(new Vector2d(35 + 3* numberofruns, 66), Math.toRadians(0))
                            .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                cyclepos = cyclePos.finding;
                            })
                            .splineToConstantHeading(new Vector2d(55, 66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(5, 5, DriveConstants.TRACK_WIDTH),
                                    SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                            .build()

                   );
                    numberofruns++;
                    cyclepos = cyclePos.out;
                }
            }


            drive.update();
            robit.update();
            telemetry.addLine("running");
            telemetry.update();
        }

    }
}
