package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class intake {
  private HardwareMap hw;
  private DcMotorEx intake;
  private DistanceSensor distance;
  private RevBlinkinLedDriver lights;

  public static double intakeDistanceThreshold = 60.0;

  public intake(HardwareMap ahw){

    hw = ahw;
    lights = hw.get(RevBlinkinLedDriver.class, "lights");
    distance = hw.get(DistanceSensor.class, "distance");
    intake = hw.get(DcMotorEx.class, "intake");
  }
  
  public void setMotorPower(double power){
    intake.setPower(power);
  }
  public double getMotorCurrent() {
    return intake.getCurrent(CurrentUnit.AMPS);

  }

  private double currentCall;
  public double getDistance() {
    currentCall = distance.getDistance(DistanceUnit.MM);

    return currentCall;
  }
  public boolean intakedBlock() {
    return currentCall < intakeDistanceThreshold;
  }
  public void update() {
    getDistance();
    if (currentCall > 1000.0) {
      lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_FOREST_PALETTE);
    } else if (intakedBlock()) {
      lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
    } else {
      lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
    }

  }
}
