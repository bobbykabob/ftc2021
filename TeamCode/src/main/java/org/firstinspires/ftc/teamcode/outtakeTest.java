package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.outtake.boxPos.IN;
import static org.firstinspires.ftc.teamcode.outtake.boxPos.OUT;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class outtakeTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        outtake outtake = new outtake(hardwareMap);
        waitForStart();

        while(opModeIsActive()){
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

            telemetry.addData("p", outtake.p);

            telemetry.update();
            outtake.update();
        }
    }
}
