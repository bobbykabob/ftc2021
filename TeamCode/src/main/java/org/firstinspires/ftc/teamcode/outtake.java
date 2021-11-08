package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public outtake() {
  
  
  public static double bottom = 0;
  public static double mid = 1000;
  public static double up = 2000;
  
  public enum liftPos { 
    BOTTOM,
    MID,
    UP
  }
  private targetLiftPos = liftPos.BOTTOM;
  private DcMotor lift1;
  private DcMotor lift2;
  private hardwareMap hw;
  public void outtake(hardwareMap ahw) { 
    hw = ahw;
    lift1 = hw.get(DcMotor.class, "lift1");
    lift2 = hw.get(DcMotor.class, "lift2");
    
  }
  
  public void loop() { 
    double lift1Pos = lift1.getCurrentPosition();
    double lift2Pos = lift2.getCurrentPosition();
    
    switch (targetLiftPos) { 
      case BOTTOM:
        break;
      case MID:
        break;
      case UP:
        break;
  }
  
  public setTargetLiftPos(liftPos pos) { 
    targetLiftPos = pos;
  }
    
   public setLiftMotorPower(double power) { 
      lift1.setPower(power);
      lift2.setPower(power);
     
   }
}
