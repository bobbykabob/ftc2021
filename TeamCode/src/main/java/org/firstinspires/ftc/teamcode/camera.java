package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.pipelines.TSEpipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


public class camera {
    public OpenCvCamera webcam;

    HardwareMap hw;
    public TSEpipeline tsepipeline = new TSEpipeline();
    public camera(HardwareMap hw) {
        this.hw = hw;
        int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hw.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.setPipeline(tsepipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() {
                        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                    }

                    @Override
                    public void onError(int errorCode) {

                    }

                });

            }

            @Override
            public void onError(int errorCode) {

            }
        });

        FtcDashboard.getInstance().startCameraStream(webcam, 0);

    }


}
