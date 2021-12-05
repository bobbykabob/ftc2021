package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;

import java.util.List;
@Disabled
public class dtTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            List<Double> wheelPos = drive.getWheelPositions();
            telemetry.addData("left", wheelPos.get(0));
            telemetry.addData("right", wheelPos.get(1));
            telemetry.update();
        }
    }
}
