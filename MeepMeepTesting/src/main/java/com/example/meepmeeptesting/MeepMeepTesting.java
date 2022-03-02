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

import javafx.geometry.Pos;


public class MeepMeepTesting {


    static MeepMeep meepMeep = new MeepMeep(800);

    /* poses*/
    static Pose2d startPose = new Pose2d(36, 66, Math.toRadians(0));
    static Pose2d hubPose = new Pose2d(-12, 36, Math.toRadians(-90));

    public static void main(String[] args) {



        List<RoadRunnerBotEntity> bots = new ArrayList<>();

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark())
                .setDimensions(12, 12)
                .setDriveTrainType(DriveTrainType.TANK)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(hubPose.vec(), hubPose.getHeading() + Math.toRadians(180)))
                                .setReversed(false)
                                .splineTo(startPose.vec(), startPose.getHeading())
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