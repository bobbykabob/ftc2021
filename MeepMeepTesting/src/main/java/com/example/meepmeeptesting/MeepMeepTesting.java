package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class MeepMeepTesting {
    static Pose2d startPose = new Pose2d(-36, 66, Math.toRadians(270));
    static Pose2d hubPose = new Pose2d(-10, 38, Math.toRadians(270));

    static MeepMeep meepMeep = new MeepMeep(800);
    public static void main(String[] args) {
        // TODO: If you experience poor performance, enable this flag
        // System.setProperty("sun.java2d.opengl", "true");
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.TANK)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .splineTo(new Vector2d(-60, 55), Math.toRadians(110))
                                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                })
                                .waitSeconds(5)
                                .setReversed(true)
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {

                                })

                                .splineTo(hubPose.vec(), hubPose.getHeading())
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //put up slides
                                })
                                .waitSeconds(1)
                                .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                                    //put down slides & turn on intake

                                })

                                .setReversed(false)
                                .splineTo(new Vector2d(-50, 55), Math.toRadians(90))
                                .waitSeconds(2)
                                .setReversed(true)
                                .splineTo(hubPose.vec(), hubPose.getHeading())
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //put up slides
                                })
                                .waitSeconds(1)
                                .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                                    //put down slides & turn on intake
                                })
                                .setReversed(false)
                                .splineTo(new Vector2d(-60, 35), Math.toRadians(225))
                                .build()
                );

        // Set field image
        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                // Set theme
                .setTheme(new ColorSchemeRedDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                .addEntity(myBot);


        for(int i = 0; i<100; i++){

            //RoadRunnerBotEntity abot = new myBot;
            meepMeep.addEntity(makeBot( i/10.0));
        }
        meepMeep.start();
    }

    public static final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static RoadRunnerBotEntity makeBot(double delay){

        return new DefaultBotBuilder(meepMeep)
                .setDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.TANK)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .waitSeconds(delay)
                                .splineTo(new Vector2d(-60, 55), Math.toRadians(110))
                                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                                })
                                .waitSeconds(5)
                                .setReversed(true)
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {

                                })

                                .splineTo(hubPose.vec(), hubPose.getHeading())
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //put up slides
                                })
                                .waitSeconds(1)
                                .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                                    //put down slides & turn on intake

                                })

                                .setReversed(false)
                                .splineTo(new Vector2d(-50, 55), Math.toRadians(90))
                                .waitSeconds(2)
                                .setReversed(true)
                                .splineTo(hubPose.vec(), hubPose.getHeading())
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //put up slides
                                })
                                .waitSeconds(1)
                                .UNSTABLE_addTemporalMarkerOffset(2, ()-> {
                                    //put down slides & turn on intake
                                })
                                .setReversed(false)
                                .splineTo(new Vector2d(-60, 35), Math.toRadians(225))
                                .build()
                );

    }
}