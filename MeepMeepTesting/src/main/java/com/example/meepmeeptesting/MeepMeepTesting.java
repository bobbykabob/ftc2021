package com.example.meepmeeptesting;

import static java.lang.Thread.sleep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;


public class MeepMeepTesting {


    static MeepMeep meepMeep = new MeepMeep(800);

    /* poses*/
    static Pose2d startPose = new Pose2d(12, 66, Math.toRadians(90));
    static Pose2d hubPose = new Pose2d(-12, 36, Math.toRadians(270));

    public static void main(String[] args) {

        RoadRunnerBotEntity blueWarehouse = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark())
                .setDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.MECANUM)
                .setConstraints(50, 50, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive ->

                                drive.trajectorySequenceBuilder(startPose)
                                        .setReversed(true)
                                        .UNSTABLE_addTemporalMarkerOffset(0, ()-> {
                                            //robit.outtake.setOuttake(outtake.outtakePos.IN_CLOSED);
                                        })
                                        .lineToConstantHeading(hubPose.vec())

                                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                                        })
                                        .setReversed(false)
                                        .splineTo(new Vector2d(0, 60), Math.toRadians(0))
                                        .splineTo(new Vector2d(30, 66), Math.toRadians(0))
                                        .splineTo(new Vector2d(55, 66), Math.toRadians(0))
                                        .setReversed(true)
                                        .splineTo(new Vector2d(30, 66), Math.toRadians(180))
                                        .splineTo(new Vector2d(0, 60), Math.toRadians(180))
                                        .splineTo(hubPose.vec(), hubPose.getHeading())
                                        .setReversed(false)
                                        .splineTo(new Vector2d(0, 60), Math.toRadians(0))
                                        .splineTo(new Vector2d(30, 66), Math.toRadians(0))
                                        .splineTo(new Vector2d(55, 66), Math.toRadians(0))
                                        .build()
                );
        Pose2d startPose = new Pose2d(-36, 66, Math.toRadians(90));
        Pose2d hubPose = new Pose2d(-35, 22, Math.toRadians(0));
        RoadRunnerBotEntity blueduck = new DefaultBotBuilder(meepMeep)

                .setColorScheme(new ColorSchemeBlueDark())
                .setDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.MECANUM)
                .setConstraints(50, 50, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(startPose)
                        .setReversed(true)
                        .lineToConstantHeading(new Vector2d(-70, 53))
                        .lineToConstantHeading(new Vector2d(-70, 56))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .waitSeconds(3)
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .setReversed(true)
                        .splineTo(new Vector2d(-65, 30), Math.toRadians(270))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })

                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .waitSeconds(1)

                        .setReversed(false)
                        .splineTo(new Vector2d(-65, 30), Math.toRadians(90))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .splineTo(new Vector2d(-65, 40), Math.toRadians(45))
                        .lineToLinearHeading(new Pose2d(-45, 40, Math.toRadians(45)))
                        .lineToLinearHeading(new Pose2d(-45, 50, Math.toRadians(45)))
                        .turn(Math.toRadians(90))
                        .lineToLinearHeading(new Pose2d(-35, 63, Math.toRadians(135)))
                        .lineToLinearHeading(new Pose2d(-65, 63, Math.toRadians(135)))
                        .turn(Math.toRadians(45))
                        .setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .splineTo(new Vector2d(-65, 30), Math.toRadians(270))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .splineTo(hubPose.vec(), hubPose.getHeading())
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {

                        })
                        .waitSeconds(1)

                        .setReversed(false)
                        .UNSTABLE_addTemporalMarkerOffset(1, () -> {


                        })
                        .splineTo(new Vector2d(-70, 35), Math.toRadians(90))

                        .build()
                );

        // Set field image
        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                // Set theme
                .setTheme(new ColorSchemeRedDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                .addEntity(blueWarehouse)
                .addEntity(blueduck)
                .start();




    }





}