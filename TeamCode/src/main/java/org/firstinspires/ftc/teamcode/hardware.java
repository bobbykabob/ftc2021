package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;

public class hardware {

  public intake intake;
  public outtake outtake;
  public duck duck;
  public camera camera;
  private HardwareMap hw;
  private color acolor;
  public hardware(HardwareMap ahw) {
    this(ahw, color.BLUE);

  }
  public hardware(HardwareMap ahw, color acolor) {
    this.acolor = acolor;
    hw = ahw;
    intake = new intake(hw);
    outtake = new outtake(hw);
    duck = new duck(hw);
    camera = new camera(hw, acolor);

  }

  public void setLiftfromTSE(TSEpipeline.TSEpos pos) {
    switch (pos) {
      case LEFT:
        outtake.setTargetLiftPos(org.firstinspires.ftc.teamcode.outtake.liftPos.AUTO_BOTTOM);
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
    intake.update();
  }
  public enum color {
    BLUE,
    RED
  }
}
