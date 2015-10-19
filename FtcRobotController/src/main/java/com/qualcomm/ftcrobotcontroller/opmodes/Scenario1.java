/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * EmptyOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class Scenario1 extends OpMode {

    DcMotor motorleft;
    DcMotor motorright;

    ColorSensor color;

    int stage = 0;

    final static int GEAR_RATIO = 2;
    final static int WHEEL_DIAMETER = 4;

    double calcEncoderValue(double inches) {
        return (1440 * (inches/(Math.PI * WHEEL_DIAMETER)))/GEAR_RATIO;
    }

    public void setDriveChannelMode (DcMotorController.RunMode mode) {
        motorleft.setChannelMode(mode);
        motorright.setChannelMode(mode);
    }

    public void setDrivePower (double power) {
        motorleft.setPower(power);
        motorright.setPower(power);
    }

    /* public boolean isWithinTolerance (double distance) {
        if ((motorleft.getCurrentPosition() <= distance+2)) {

        }
    } */


	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

        motorleft = hardwareMap.dcMotor.get("motorleft");
        motorright = hardwareMap.dcMotor.get("motorright");
        motorleft.setDirection(DcMotor.Direction.REVERSE);

        color = hardwareMap.colorSensor.get("color");

        color.enableLed(false);

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        if (stage == 0) {
            // Emptying the climbers code goes into here
            stage++;
        } if (stage == 1) {
            // Scanning for color and pushing button goes here
            int currentcolor = color.argb();
            if (currentcolor == Color.RED){
                // Code for having robot push red button
            } if (currentcolor == Color.BLUE) {
                // Code for having robot push blue button
            } else {
                DbgLog.msg("Could not find out color");
            }
            stage++;
        } if (stage == 2) {
            setDriveChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            setDriveChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

            motorleft.setTargetPosition((int) calcEncoderValue(12));
            motorleft.setTargetPosition((int) calcEncoderValue(12));

            setDrivePower(0.2);
            stage++;
        } /*if (stage == 3) {
            if (motorleft.getCurrentPosition() == calcEncoderValue(12))
        }*/

        telemetry.addData("stage", stage);


	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

	// Scaling input has been moved to BasicFunctions
}
