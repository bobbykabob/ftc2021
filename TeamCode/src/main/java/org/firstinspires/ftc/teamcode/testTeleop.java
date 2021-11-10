package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.hardware;

@TeleOp
public class testTeleop extends LinearOpMode {
  @Override
  public void runOpMode() throw InterruptedException {
    SampleTankDrive drive = new SampleTankDrive(hardwareMap);
    
    drive.setMode(DcMototr.RunMode.RUN_WITHOUT_ENCODER);
    
    
    hardware robit = new hardware(hardwareMap);
    
    waitForStart();
    
    while (!istStopRequested()) {
      drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            0,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    
  }
}
