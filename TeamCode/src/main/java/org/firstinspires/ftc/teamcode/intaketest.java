package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@TeleOp
@Config

public class intaketest extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {





        hardware robit = new hardware(hardwareMap);

        waitForStart();
        FtcDashboard dashboard = FtcDashboard.getInstance();


        while (!isStopRequested()) {


            robit.intake.setMotorPower(gamepad1.left_trigger - gamepad1.right_trigger);




            robit.update();


            TelemetryPacket packet = new TelemetryPacket();
            packet.put("block", robit.intake.intakedBlock());
            packet.put("distance", robit.intake.getMotorCurrent());
            packet.put("0", 0);

            dashboard.sendTelemetryPacket(packet);

        }


    }
}
