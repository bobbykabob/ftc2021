package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@TeleOp
@Config

public class newteleop extends LinearOpMode {

    public static double deltaY = 0.075;
    public static double gamepadToerlance = 0.1;
    public static double turnMultipler = 0.5;
    enum driveState {
        teleOp,
        back,
        forward,
        running
    }
    driveState drivestate = driveState.teleOp;

    @Override
    public void runOpMode() throws InterruptedException {



        SampleTankDrive drive = new SampleTankDrive(hardwareMap);
        Pose2d startPose = new Pose2d(36, 66, Math.toRadians(0));
        Pose2d hubPose = new Pose2d(-12, 36, Math.toRadians(-90));

        TrajectorySequence back = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .back(10)
                .splineTo(hubPose.vec(), hubPose.getHeading())
                .build();
        TrajectorySequence forward = drive.trajectorySequenceBuilder(new Pose2d(hubPose.vec(), hubPose.getHeading() + Math.toRadians(180)))
                .setReversed(false)
                .turn(Math.toRadians(45))
                .splineTo(startPose.vec(), startPose.getHeading())
                .build();
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        hardware robit = new hardware(hardwareMap);

        waitForStart();

        double speeeed = 1;
        double prevY = 0;
        boolean dtforward = false;
        while (!isStopRequested()) {
            if (gamepad1.left_bumper) {
                speeeed = 0.5;
            } else if (gamepad1.right_bumper) {
                speeeed = 1;
            }


            robit.intake.setMotorPower(gamepad1.left_trigger - gamepad1.right_trigger);


            if (gamepad1.dpad_up) {
                robit.duck.spinDuck();
            } else if (gamepad1.dpad_down ) {
                robit.duck.spinDuckOther();
            } else {
                robit.duck.stop();
            }
            double gamepadY;
            if (gamepad1.left_stick_y < 0) {
                //move forward
                gamepadY = Math.pow(Math.abs(gamepad1.left_stick_y), 2) * speeeed;
                telemetry.addData("prevY", prevY);
                telemetry.addData("gamepadY", gamepadY);


            } else {
                //move backward
                gamepadY = -Math.pow(Math.abs(gamepad1.left_stick_y), 2) * speeeed;
            }




            //g = 1, p = 0
            //g= -1, p = 0
            //g = 0, p = 1
            //g = 0, p= -1
            if (prevY < 0) {
                if (prevY - gamepadY < 0) {
                    gamepadY = prevY + deltaY;
                }
            } else if (prevY > 0) {
                if (prevY - gamepadY > 0) {
                    gamepadY = prevY - deltaY;
                }
            }



            prevY = gamepadY;


            double turn = -gamepad1.right_stick_x;

            if (turn > 0) {
                turn = Math.pow(turn,2);
            }  else {
                turn = - Math.pow(Math.abs(turn), 2);
            }

            if (gamepadY < gamepadToerlance && gamepadY > -gamepadToerlance) {
                gamepadY = 0;
            }

            if (turn < gamepadToerlance && turn > -gamepadToerlance) {
                turn =0;
            }

            if (gamepad1.dpad_left) {
                drivestate = driveState.back;
            } else if (gamepad1.dpad_right) {
                drivestate = driveState.forward;
            }
            if (gamepadY != 0 || turn != 0) {
                drivestate = driveState.teleOp;
            }
            switch (drivestate) {
                case teleOp:
                    drive.setWeightedDrivePower(
                            new Pose2d(
                                    gamepadY,
                                    0,
                                    turn * turnMultipler
                            )
                    );
                    break;
                case back:
                    drive.setPoseEstimate(startPose);
                    drive.followTrajectorySequenceAsync(back);
                    drivestate = driveState.running;
                    break;
                case forward:
                    drive.setPoseEstimate(new Pose2d(hubPose.vec(), hubPose.getHeading() + Math.toRadians(180)));
                    drive.followTrajectorySequenceAsync(forward);
                    drivestate = driveState.running;
                case running:
                    if (!drive.isBusy()) {
                        drivestate = driveState.teleOp;
                    }
                    break;
            }

            if (gamepad1.y || gamepad2.y) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
            } else if (gamepad1.x || gamepad2.x) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.MID);
            } else if (gamepad1.a || gamepad2.a) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
            }

            if (gamepad1.left_stick_button || gamepad2.dpad_left) {
                robit.outtake.iterateOuttakeBackward();
            } else if (gamepad1.right_stick_button || gamepad2.dpad_right) {
                robit.outtake.iterateOuttakeForward();
            }



            robit.update();

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("outtake Pos", robit.outtake.currentPos);
            telemetry.update();
        }


    }
}
