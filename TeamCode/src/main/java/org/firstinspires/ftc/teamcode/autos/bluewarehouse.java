package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class bluewarehouse extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap);

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        Pose2d startPose = new Pose2d(9, 66, Math.toRadians(270));
        Pose2d hubPose = new Pose2d(-8, 42, Math.toRadians(270));
        drive.setPoseEstimate(startPose);
        telemetry.addLine("ready");
        telemetry.addData("TSE position", robit.camera.tsepipeline.getTSEpos());
        telemetry.update();
        waitForStart();


        TrajectorySequence start = drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    //lift slide
                    robit.setLiftfromTSE();
                })

                .splineTo(new Vector2d(0, 48), Math.toRadians(270))
                .turn(Math.toRadians(160))
                .setReversed(true)
                .splineTo(hubPose.vec(), hubPose.getHeading())
                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                    //put cube in
                    robit.outtake.setOuttake(outtake.boxPos.OUT);
                })
                .setReversed(false)

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
                .splineTo(new Vector2d(55, 66), Math.toRadians(0))

                .build();


        drive.followTrajectorySequenceAsync(start);



        long startTime = System.currentTimeMillis();
        while (opModeIsActive()) {

            if (!drive.isBusy()) {
                long currentTime = System.currentTimeMillis();
                if ((30000 -(currentTime - startTime)) > 5000) {

                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .setReversed(true)
                            .splineTo(new Vector2d(20, 66), Math.toRadians(180))
                            .UNSTABLE_addTemporalMarkerOffset(1, () -> {
                                //put slide up
                                robit.intake.setMotorPower(0);
                                robit.outtake.setTargetLiftPos(outtake.liftPos.UP);

                            })

                            .splineTo(hubPose.vec(), hubPose.getHeading())
                            .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                //put cube in
                                robit.outtake.setOuttake(outtake.boxPos.OUT);
                            })
                            .waitSeconds(0.5)
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
                            .splineTo(new Vector2d(55, 66), Math.toRadians(0))
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
