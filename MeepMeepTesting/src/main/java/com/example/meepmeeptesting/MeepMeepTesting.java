package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.SampleTankDrive;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // TODO: If you experience poor performance, enable this flag
        // System.setProperty("sun.java2d.opengl", "true");
        Pose2d startPose = new Pose2d(-36, 66, Math.toRadians(270));
        Pose2d hubPose = new Pose2d(-10, 38, Math.toRadians(270));


        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        MeepMeep mm = new MeepMeep(400)
                // Set field image
                .setBackground(MeepMeep.Background.FIELD_FREIGHT_FRENZY)
                // Set theme
                .setTheme(new ColorSchemeRedDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 10)
                .setBotDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.TANK)
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
                )

                .start();
    }
}