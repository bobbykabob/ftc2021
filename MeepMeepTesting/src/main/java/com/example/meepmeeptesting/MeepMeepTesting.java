package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // TODO: If you experience poor performance, enable this flag
        // System.setProperty("sun.java2d.opengl", "true");

        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        MeepMeep mm = new MeepMeep(800)
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
                        drive.trajectorySequenceBuilder(new Pose2d(0, -66, Math.toRadians(270)))
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //lift slide
                                })
                                .setReversed(true)
                                .splineTo(new Vector2d(-18, -38), Math.toRadians(70))
                                .setReversed(false)
                                .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                    //put cube in
                                })
                                .waitSeconds(1)
                                .UNSTABLE_addTemporalMarkerOffset(1, ()-> {
                                    //put lift down
                                })
                                .splineTo(new Vector2d(-60, -50), Math.toRadians(250))
                                .setReversed(true)
                                .splineTo(new Vector2d(-60, -35), Math.toRadians(90))
                                .build()
                )
                .start();
    }
}