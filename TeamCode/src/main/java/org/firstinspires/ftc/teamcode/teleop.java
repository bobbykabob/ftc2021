package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.outtake.outtakePos.OUT_OPEN;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;

@TeleOp
@Config

public class teleop extends LinearOpMode {

    public static double deltaY = 0.075;
    public static double accelTolerance = 0.05;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        hardware robit = new hardware(hardwareMap, hardware.color.BLUE);

        waitForStart();

        double speeeed = 1;
        double prevY = 0;
        boolean dtforward = false;
        while (!isStopRequested()) {
            robit.intake.setMotorPower((1.5) * gamepad1.left_trigger - gamepad1.right_trigger -0.5);

            if (gamepad1.left_bumper ) {
                speeeed = 0.3;
            } else if (gamepad1.right_bumper) {
                speeeed = 0.8;
            }
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



            drive.setWeightedDrivePower(
                    new Pose2d(
                            gamepadY,
                            0,
                            turn
                    )
            );

            if (gamepad1.y || gamepad2.y) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
            } else if (gamepad1.x || gamepad2.x) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.MID);
            } else if (gamepad1.a || gamepad2.a) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
            }

            if (gamepad1.dpad_left || gamepad2.dpad_left) {
                robit.outtake.iterateOuttakeBackward();
            } else if (gamepad1.dpad_right || gamepad2.dpad_right) {
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
