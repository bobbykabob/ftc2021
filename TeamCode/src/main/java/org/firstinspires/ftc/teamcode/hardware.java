package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.intake;
import org.firstinspires.ftc.teamcode.outtake;

public class hardware {
  
  public intake intake;
  public outtake outtake;
  public duck duck;
  public camera camera;
  private HardwareMap hw;
  public hardware(HardwareMap ahw) {
    hw = ahw;
    intake = new intake(hw);
    outtake = new outtake(hw);
    duck = new duck(hw);
    camera = new camera(hw);
  }

  public void update() {
    outtake.update();
  }
}
