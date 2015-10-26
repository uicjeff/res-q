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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TeleOpUltrasonic extends OpMode {

	DcMotor motorLeft;
	DcMotor motorRight;

	UltrasonicSensor ultrasonic;

    // Initial scaling power
    double scalePower = 0.65;

	/*
    Setting drive power for both motors with one command. Run with setDrivePower() function
     */
	public void setDrivePower(Double power1, Double power2) {
		motorLeft.setPower(power1);
		motorRight.setPower(power2);

	}

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

		motorLeft = hardwareMap.dcMotor.get("motorleft");
		motorRight = hardwareMap.dcMotor.get("motorright");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        boolean ultrasonicDetected = false;

        if (ultrasonic.getUltrasonicLevel() <= 20) {
            ultrasonicDetected = true;
        }

        if (ultrasonicDetected == false) {
            float leftTrigger = gamepad1.left_trigger;

            if (leftTrigger != 0) {
                scalePower = 0.35;
            } if (leftTrigger == 0) {
                scalePower = 0.65;
            }

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the right stick
		 */

            // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
            // 1 is full down
            // direction: right_stick_x ranges from -1 to 1, where -1 is full left
            // and 1 is full right
            float throttle = -gamepad1.right_stick_y;
            float direction = gamepad1.right_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = (float)scaleInput(right * scalePower);
            left =  (float)scaleInput(left * scalePower);

            // write the values to the motors
            setDrivePower((double)left, (double)right);
            /*
             * Send telemetry data back to driver station. Note that if we are using
             * a legacy NXT-compatible motor controller, then the getPower() method
             * will return a null value. The legacy NXT-compatible motor controllers
             * are currently write only.
             */
            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        }





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