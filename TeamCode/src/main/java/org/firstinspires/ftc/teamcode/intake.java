package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class intake() {
  private hardwareMap hw;
  private DcMotor intake;
  public void intake(hardwareMap ahw){
    hw = ahw;
    intake = hw.get(DcMotor.class, "intake");
  }
  
  public void setMotorPower(double power){
    intake.setPower(power);
  }
}
