/*    if (stage == 0) {
            if (!gyro.isCalibrating()) {
                double target_angle_degrees = 135;
                double error_degrees = target_angle_degrees - gyro.getHeading();
                if ( error_degrees > 25) {
                    driveLeft(0.75);
                    driveRight(-0.75);
                } else {
                    driveLeft(0.57);
                    driveRight(-0.57);
                } if (isGyroInTolerance((int) target_angle_degrees)) {
                    driveLeft(0);
                    driveRight(0);
                    stage++;
                }
            } if (gyro.isCalibrating()) {
                driveLeft(0);
                driveRight(0);
            }
        }
         */
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.library.devices.VCNL4010;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * DriveToBeacon
 * <p/>
 * Drive to the beacon red side drop climbers,and defence
 */
public class DriveToBeaconRed2 extends DriveTrainLayer {

    final static int TOLERANCE = 1;
    ColorSensor lineColorSensor;
    GyroSensor gyro;
    int stage = 0;
    ElapsedTime manipTime = new ElapsedTime();
    ElapsedTime waitTime = new ElapsedTime();
    ElapsedTime startTime = new ElapsedTime();
    DcMotor intakeMotor;
    Servo climbersServo;
    double restingPosition = 0;
    Servo leftAngelArm;
    Servo rightAngelArm;
    VCNL4010 prox;
    private double servoDelta = 0.01;
    double servoPosition = restingPosition;
    private ElapsedTime servotime = new ElapsedTime();
    private double servoDelayTime2 = 0.0001;

    public boolean aboveWhiteLine() {
        boolean returnValue = false;
        if ((lineColorSensor.red() >= 2) && (lineColorSensor.green() >= 2) && (lineColorSensor.blue() >= 2)) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean aboveRedLine() {
        boolean returnValue = false;
        if ((lineColorSensor.red() > lineColorSensor.green() + 2) && (lineColorSensor.red() > lineColorSensor.blue() + 2)) {
            returnValue = true;
        }
        return returnValue;
    }



    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {
        super.init();

        prox = new VCNL4010(hardwareMap, "prox");

        lineColorSensor = hardwareMap.colorSensor.get("lineColorSensor");

        gyro = hardwareMap.gyroSensor.get("gyro");
        lineColorSensor.enableLed(false);
        gyro.calibrate();

        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

        prox.setProxRate(0x5);
        prox.setLEDSensitivity(20);

        climbersServo = hardwareMap.servo.get("climbersServo");
        climbersServo.setDirection(Servo.Direction.REVERSE);

        leftAngelArm = hardwareMap.servo.get("leftAngelArm");
        rightAngelArm = hardwareMap.servo.get("rightAngelArm");

    }

    @Override
    public void start() {
        super.start();

        climbersServo.setPosition(restingPosition);

        leftAngelArm.setPosition(0);
        rightAngelArm.setPosition(0.8);

        lineColorSensor.enableLed(true);
        gyro.calibrate();
        manipTime.reset();

        startTime.reset();
    }
//int out of the loop
    int currentGyro;
    int lastByte = -1;
    int flux = 10;
    int counter = 0;
    int offset = 15;

    /*d
         * This method will be called repeatedly in a loop
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
         */
    @Override
    public void loop() {
       int highByte;
        int lowByte;

        //Get Prox Data
        prox.refreshData();
        highByte = prox.getHb();
        lowByte = prox.getLb();

        if (stage == 0) {
            if (!gyro.isCalibrating()) {
                manipTime.reset();
                stage ++;          }
        }
// drive out straight from wall
        if (stage == 1) {
            motorRight1.setPower(-.4);
            motorRight2.setPower(-.4);
            motorLeft1.setPower(-.4);
            motorLeft2.setPower(-.4);
            if (manipTime.time() >= 0.3) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                stage ++;
                waitTime.reset();
            }
        }
        // spin to heading
        if (stage == 2) {
            //Start turning clockwise
            motorLeft1.setPower(1);
            motorLeft2.setPower(1);
            motorRight1.setPower(-.5);
            motorRight2.setPower(-.5);
            currentGyro = gyro.getHeading();
            //Until 311 degrees
            if (currentGyro <= (311 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                waitTime.reset();
                stage ++;
            }
        }

        //drive forward
        if (stage == 3) {
            motorLeft1.setPower(-.4);
            motorLeft2.setPower(-.4);
            motorRight1.setPower(-.4);
            motorRight2.setPower(-.4);
            if (manipTime.time() >= 0.3) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                stage ++;
                waitTime.reset();
            }
        }
        // Turn to heading
        if (stage == 4) {
            motorLeft1.setPower(.5);
            motorLeft2.setPower(.5);
            motorRight1.setPower(-.25);
            motorRight2.setPower(-.25);
            currentGyro = gyro.getHeading();
            if (currentGyro <= (311 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                waitTime.reset();
                stage ++;
            }
        }
        // drive forward
        if (stage ==5) {
            motorLeft1.setPower(-.4);
            motorLeft2.setPower(-.4);
            motorRight1.setPower(-.4);
            motorRight2.setPower(-.4);
            if (manipTime.time() >= 0.3) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                stage ++;
                waitTime.reset();

            }
        }

        if (stage == 6) {
            motorLeft1.setPower(.5);
            motorLeft2.setPower(.5);
            motorRight1.setPower(-.25);
            motorRight2.setPower(-.25);
            currentGyro = gyro.getHeading();
            if (currentGyro <= (311 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                waitTime.reset();
                stage ++;
            }
        }
        if (stage ==7) {
            motorLeft1.setPower(-.4);
            motorLeft2.setPower(-.4);
            motorRight1.setPower(-.4);
            motorRight2.setPower(-.4);
            if (manipTime.time() >= 0.3) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                stage ++;
                waitTime.reset();

            }
        }
        if (stage == 8) {
            motorLeft1.setPower(.5);
            motorLeft2.setPower(.5);
            motorRight1.setPower(-.25);
            motorRight2.setPower(-.25);
            currentGyro = gyro.getHeading();
            if (currentGyro <= (311 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                waitTime.reset();
                stage ++;
            }
        }
        if (stage == 9) {
            if (aboveWhiteLine()) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                stage ++;
            }
            if (!aboveWhiteLine()) {
                motorLeft1.setPower(-0.2);
                motorLeft2.setPower(-0.2);
                motorRight1.setPower(-0.2);
                motorRight2.setPower(-0.2);
                }
            }
        //spin to face beacon
        if (stage == 10) {
            motorLeft1.setPower(-1);
            motorLeft2.setPower(-1);
            motorRight1.setPower(.5);
            motorRight2.setPower(.5);
            //storing current gyro reading
            currentGyro = gyro.getHeading();
            if (currentGyro <= (80 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                telemetry.addData("Gyro", currentGyro);
                telemetry.addData("Stage", stage);
                stage++;
            }
        }
       //drive "forward" out of box
        if (stage == 11) {
            motorLeft1.setPower(-.4);
            motorLeft2.setPower(-.4);
            motorRight1.setPower(-.4);
            motorRight2.setPower(-.4);
            if (manipTime.time() >= 0.3) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                stage ++;
                waitTime.reset();
            }
        }
        //drive backwards
        if (stage == 12) {
            if (aboveWhiteLine()) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                stage ++;
            }
            if (!aboveWhiteLine()) {
                motorLeft1.setPower(0.2);
                motorLeft2.setPower(0.2);
                motorRight1.setPower(0.2);
                motorRight2.setPower(0.2);
            }
        }
       // spin clockwise to straighten out
        if (stage == 13) {
            motorLeft1.setPower(.3);
            motorLeft2.setPower(.3);
            motorRight1.setPower(-.6);
            motorRight2.setPower(-.6);
            //storing current gyro reading
            currentGyro = gyro.getHeading();
            if (currentGyro >= (90 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                telemetry.addData("Gyro", currentGyro);
                telemetry.addData("Stage", stage);
                stage++;
            }
        }


        //Stage Case/IF loops
        if (stage == 14) {
            telemetry.addData("Prox", highByte);
// Drive forward
            motorRight1.setPower(-.2);
            motorRight2.setPower(-.2);
            motorLeft1.setPower(-.27);
            motorLeft2.setPower(-27);
            //Decides if its safe to throw climbers
            if (highByte >= 9) {
                //if the highByte is 9 you are close enough to the wall to throw
                telemetry.addData("Text", "Throw Climbers");
                stage=15;
            } else {
                if (highByte <= 8) {
                    //if the high byte is 8 you may not be close enough but, only if the low is greater than 150 throw climbers
                    if (lastByte >= (lowByte - flux)) {//Otter didnt move much sincs last loop
                        counter++;
                        lastByte = lowByte;
                        if (counter >= 100) {//checking how lomg Otters been stuck
                            stage = 16;//abort
                        } else {
                            stage = 14;
                        }
                    } else {//Otter is still moving
                        counter = 0;
                        stage = 14;//recheck
                        lastByte = lowByte;
                    }


                }


            }
        }

        if (stage == 15) {
            //Sets motor power to zero and throws climbers

            motorLeft1.setPower(0);
            motorLeft2.setPower(0);
            motorRight1.setPower(0);
            motorRight2.setPower(0);;
            climbersServo.setPosition(Range.clip(servoPosition += servoDelta, restingPosition, 1));
            stage++;
        }
        if (stage == 16) {
            //otter is stuck short of the beacon ,cannot throw so stop motors
            motorLeft1.setPower(0);
            motorLeft2.setPower(0);
            motorRight1.setPower(0);
            motorRight2.setPower(0);
            telemetry.addData("Text", "Stopping");
        }
        if (stage == 17) {
            motorRight1.setPower(.4);
            motorRight2.setPower(.4);
            motorLeft1.setPower(.4);
            motorLeft2.setPower(.4);
            if (manipTime.time() >= 0.3) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                stage ++;
                waitTime.reset();
            }
        }
        if (stage == 18) {
            motorLeft1.setPower(-.6);
            motorLeft2.setPower(-.6 );
            motorRight1.setPower(.3);
            motorRight2.setPower(.3);
            //storing current gyro reading
            currentGyro = gyro.getHeading();
            if (currentGyro <= (170 - offset)) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                telemetry.addData("Gyro", currentGyro);
                telemetry.addData("Stage", stage);
                stage++;
            }
        }
        //Drive to block other team
        if (stage == 19) {
            if (aboveWhiteLine()) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                stage ++;
            }
            if (!aboveWhiteLine()) {
                motorLeft1.setPower(0.7);
                motorLeft2.setPower(0.7);
                motorRight1.setPower(0.7);
                motorRight2.setPower(0.7);
            }
        }


        telemetry.addData("Gyro", currentGyro);
        telemetry.addData("Stage", stage);
        telemetry.addData("motor", String.valueOf(motorRight1.getPower()));
        telemetry.addData("gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("white", String.valueOf(aboveWhiteLine()));


    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop().
     */

    }

    @Override
    public void stop() {

    }


}