package com.example.meepmeeptesting;

import static java.lang.Thread.sleep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.ArrayList;
import java.util.List;


public class MeepMeepTesting {


    static MeepMeep meepMeep = new MeepMeep(800);

    /* poses*/
    static Pose2d startPose = new Pose2d(-36, 66, Math.toRadians(270));
    static Pose2d hubPose = new Pose2d(-30, 24, Math.toRadians(0));

    public static void main(String[] args) {



        List<RoadRunnerBotEntity> bots = new ArrayList<>();

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark())
                .setDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.TANK)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .splineTo(new Vector2d(-62, 63), Math.toRadians(135))
                                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                   // robit.duck.spinDuckOther();
                                })
                                .setReversed(true)
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //robit.duck.stop();

                                })
                                .splineTo(new Vector2d(-58, 30), Math.toRadians(270))

                                .splineTo(hubPose.vec(), hubPose.getHeading())
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                                })
                                .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                                    //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                                    //robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                                    //robit.intake.setMotorPower(1);
                                    //put down slides & turn on intake
                                })

                                .setReversed(false)
                                .splineTo(new Vector2d(-58, 30), Math.toRadians(90))
                                .splineTo(new Vector2d(-60, 64), Math.toRadians(90))
                                .setReversed(true)
                                .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                                    //robit.intake.setMotorPower(0);
                                    //robit.outtake.setTargetLiftPos(outtake.liftPos.UP);
                                })
                                .splineTo(new Vector2d(-58, 30), Math.toRadians(270))
                                .splineTo(hubPose.vec(), hubPose.getHeading())
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {

                                    //robit.outtake.setOuttake(outtake.outtakePos.OUT_OPEN);
                                })
                                .waitSeconds(1)
                                .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                                    //robit.outtake.setTargetLiftPos(outtake.liftPos.BOTTOM);
                                    //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                                    //put down slides & turn on intake
                                })
                                .setReversed(false)
                                .splineTo(new Vector2d(-60, 35), Math.toRadians(90))
                                .build()
                );

        bots.add(myBot);
        // Set field image
        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                // Set theme
                .setTheme(new ColorSchemeRedDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                .addEntity(myBot);

        meepMeep.start();




    }





}