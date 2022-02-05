package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.outtake.outtakePos.IN_CLOSED;
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

    public static double deltaY = -0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        hardware robit = new hardware(hardwareMap);

        waitForStart();

        double speeeed = 1;
        double prevY = 0;
        while (!isStopRequested()) {
            robit.intake.setMotorPower(gamepad1.left_trigger - gamepad1.right_trigger);

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

            double gamepadY = -gamepad1.left_stick_y * speeeed;

            if ((gamepadY - prevY) < deltaY) {
                gamepadY = deltaY;
            }

            drive.setWeightedDrivePower(
                    new Pose2d(
                            gamepadY,
                            0,
                            -gamepad1.right_stick_x
                    )
            );
            prevY = gamepadY;


            if (gamepad1.y) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
            } else if (gamepad1.x) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.MID);
            } else if (gamepad1.a) {
                robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
            }

            if (gamepad1.dpad_left) {
                robit.outtake.iterateOuttakeBackward();
            } else if (gamepad1.dpad_right) {
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
