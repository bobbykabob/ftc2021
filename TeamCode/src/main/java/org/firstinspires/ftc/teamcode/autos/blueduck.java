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
        Pose2d hubPose = new Pose2d(-14, 40, Math.toRadians(290));

        drive.setPoseEstimate(startPose);

        while (!isStarted()) {
            telemetry.addData("pos", robit.camera.tsepipeline.getTSEpos());
            telemetry.update();
        }
        TSEpipeline.TSEpos pos = robit.camera.tsepipeline.getTSEpos();
        drive.followTrajectorySequenceAsync(
                drive.trajectorySequenceBuilder(startPose)
                        .splineTo(new Vector2d(-62, 63), Math.toRadians(135))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            robit.duck.spinDuckOther();
                        })
                        .waitSeconds(3)
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //robit.setLiftfromTSE(pos);
                            robit.duck.stop();

                        })

                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                            //robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                            //robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                            robit.intake.setMotorPower(1);
                            //put down slides & turn on intake
                        })

                        .setReversed(false)
                        .splineTo(new Vector2d(-60, 64), Math.toRadians(90))
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                            robit.intake.setMotorPower(0);
                            //robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                        })
                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {

                            //robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                        })
                        .waitSeconds(1)
                        .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                            robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                            //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                            //put down slides & turn on intake
                        })
                        .setReversed(false)
                        .splineTo(new Vector2d(-60, 35), Math.toRadians(225))
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
