package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

abstract public class HardwareAccess extends OpMode {

    /*
    This class lists the hardware that is currently on the robot with the configuration file
     */


    // Declare left motor
    DcMotor motorLeft;
    // Declare right motor
    DcMotor motorRight;

    // Declare IR Seeker
    IrSeekerSensor irSeeker;

    public void init() {

        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        irSeeker = hardwareMap.irSeekerSensor.get("irseeker");


    }

}