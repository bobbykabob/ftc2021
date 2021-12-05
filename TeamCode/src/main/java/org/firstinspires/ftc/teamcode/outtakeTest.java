package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.outtake.boxPos.IN;
import static org.firstinspires.ftc.teamcode.outtake.boxPos.OUT;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@Disabled
public class outtakeTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        outtake outtake = new outtake(hardwareMap);
        waitForStart();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        while(opModeIsActive()){
            /*
            if (gamepad1.x) {
                outtake.setTargetLiftPos(org.firstinspires.ftc.teamcode.outtake.liftPos.UP);
            } else if (gamepad1.y) {
                outtake.setTargetLiftPos(org.firstinspires.ftc.teamcode.outtake.liftPos.BOTTOM);
            }

            if (gamepad1.dpad_left) {
                outtake.setOuttake(IN);
            } else if (gamepad1.dpad_right) {
                outtake.setOuttake(OUT);
            }

             */


            TelemetryPacket packet = new TelemetryPacket();
            packet.put("pos", outtake.getCurrentPosition());
            dashboard.sendTelemetryPacket(packet);
            telemetry.addData("pos", outtake.getCurrentPosition());
            telemetry.update();
        }
    }
}
