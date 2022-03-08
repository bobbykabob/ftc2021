package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.outtake;
import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;

@Autonomous
public class blueduck extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        hardware robit = new hardware(hardwareMap);

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);
        Pose2d startPose = new Pose2d(-36, 66, Math.toRadians(270));
        Pose2d hubPose = new Pose2d(-25, 24, Math.toRadians(0));

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
        drive.followTrajectorySequenceAsync(
                drive.trajectorySequenceBuilder(startPose)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                        })
                        .splineTo(new Vector2d(-62, 62), Math.toRadians(135))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            //robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED_START);
                            robit.duck.spinDuckOther();
                        })
                        .waitSeconds(3)
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.duck.stop();
                            robit.setLiftfromTSE(pos);
                        })
                        .splineTo(new Vector2d(-58, 30), Math.toRadians(-45))
                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            robit.outtake.setOuttake(outtake.outtakePos.IN_OPEN);
                            robit.intake.setMotorPower(1);
                        })

                        .setReversed(false)
                        .splineTo(new Vector2d(-58, 30), Math.toRadians(90))
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);

                        })
                        .splineTo(new Vector2d(-60, 64), Math.toRadians(90))
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            robit.intake.setMotorPower(0);
                            //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(3, ()-> {
                            //robit.outtake.setOuttake(outtake.outtakePos.OUT_CLOSED_START);
                        })
                        .splineTo(new Vector2d(-58, 30), Math.toRadians(270))

                        .UNSTABLE_addTemporalMarkerOffset(0.5, ()-> {
                            robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                        })
                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(0.5, ()-> {
                            //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                        })
                        .setReversed(false)
                        .splineTo(new Vector2d(-60, 45), Math.toRadians(90))
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
