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

/**
 * DriveTrainLayer program
 * <p>
 * All programs share the drive train in common, why not unify it?
 */
public class DriveTrainLayer extends OpMode {

    // Left drive train motors
    DcMotor motorLeft1;
    DcMotor motorLeft2;

    // Right drive train motors
    DcMotor motorRight1;
    DcMotor motorRight2;


    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {

        // Get motors from configuration file
        // Reverse Right motors

        motorLeft1 = hardwareMap.dcMotor.get("motorleft1");
        motorLeft2 = hardwareMap.dcMotor.get("motorleft2");
        motorRight1 = hardwareMap.dcMotor.get("motorright1");
        motorRight2 = hardwareMap.dcMotor.get("motorright2");


    }

    @Deprecated
    public void driveLeft(double powerLeft) {
        motorLeft1.setPower(powerLeft);
        motorLeft2.setPower(powerLeft);
    }

    @Deprecated
    public void driveRight(double powerRight) {
        motorRight1.setPower(powerRight);
        motorRight2.setPower(powerRight);
    }

    public void powerLeft(double power) {
        motorRight1.setPower(power);
        motorRight2.setPower(power);
    }

    public void powerRight(double power) {
        motorLeft1.setPower(power);
        motorLeft2.setPower(power);
    }

    /*
    These methods are unused...
     */
    @Override
    public void loop() {}
    @Override
    public void stop() {}



}