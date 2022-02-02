package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.intake;
import org.firstinspires.ftc.teamcode.outtake;
import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;

public class hardware {
  
  public intake intake;
  public outtake outtake;
  public duck duck;
  public camera camera;
  public TSEarm TSEarm;
  private HardwareMap hw;
  public hardware(HardwareMap ahw) {
    hw = ahw;
    intake = new intake(hw);
    outtake = new outtake(hw);
    duck = new duck(hw);
    camera = new camera(hw);
    TSEarm = new TSEarm(hw);
  }

  public void setLiftfromTSE(TSEpipeline.TSEpos pos) {
    switch (pos) {
      case LEFT:
        outtake.setTargetLiftPos(org.firstinspires.ftc.teamcode.outtake.liftPos.BOTTOM);
        break;
      case RIGHT:
        outtake.setTargetLiftPos(org.firstinspires.ftc.teamcode.outtake.liftPos.UP);
        break;
      case MIDDLE:
        outtake.setTargetLiftPos(org.firstinspires.ftc.teamcode.outtake.liftPos.MID);
        break;
    }
  }
  public void update() {
    outtake.update();
    duck.update();
  }
}
