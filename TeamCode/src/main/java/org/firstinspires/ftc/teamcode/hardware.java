package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.intake;
import org.firstinspires.ftc.teamcode.outtake;

public hardware() {
  
  public intake intake;
  public outtake outtake;
  private hardwareMap hw;
  public void hardware(hardwareMap ahw) {
    hw = ahw;
    intake = new intake(hw);
    outtake = new outtake(hw);
  } 
}
