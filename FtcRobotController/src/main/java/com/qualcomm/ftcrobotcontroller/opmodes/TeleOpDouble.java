package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
 */

/**
 * TeleOpDouble Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TeleOpDouble extends DriveTrainLayer {

    // Create objects for hardware

    DcMotor intakeMotor;

    DcMotor liftMotor;

    Servo LeftRightServo;

    Servo UpDownServo;

    DcMotor pistonMotor;

    /* Uncomment when accessories are added
    Servo climbersServo;

    Servo ziplinerServo;

   // Variables for controlling speed on the climbersServo
    private ElapsedTime servotime = new ElapsedTime();
    private double servoPosition;

    //tweak these values for desired speed
    private double servoDelta = 0.01;
    private double servoDelayTime = 0.003;

    private boolean climbersLifted = false;
    private boolean DPadUpPressedLong = false;
    */

    boolean YButton = false;
    boolean AButton = false;
    boolean XButton = false;
    boolean BButton = false;


    ElapsedTime manipTime = new ElapsedTime();

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {

        super.init();

        /*
        Declare hardware devices from configuration files
         */

        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

        liftMotor = hardwareMap.dcMotor.get("liftMotor");

        LeftRightServo = hardwareMap.servo.get("LeftRightServo");

        UpDownServo = hardwareMap.servo.get("UpDownServo");

        pistonMotor = hardwareMap.dcMotor.get("pistonMotor");

        /* Uncomment when accessories are added
        climbersServo.setPosition(0.15);
        ziplinerServo.setPosition(1);
        climbersServo = hardwareMap.servo.get("climbersServo");
        ziplinerServo = hardwareMap.servo.get("ziplinersServo");
        */

    }

    @Override
    public void start() {
        /* Uncomment when accessories are added
        servotime.reset();
        */

        LeftRightServo.setPosition(0.45);
        UpDownServo.setPosition(0.7);

    }

    /*
         * This method will be called repeatedly in a loop
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
         */
    @Override
    public void loop() {

        /*
         * Gamepad 2
         *
         * Gamepad 2 Controls
         *
         */

        // Variables for shorter code on the buttons

        /*
         * Intake Sections | Left - In, Right - Out
         */
        double leftTrigger = gamepad2.left_trigger;
        boolean leftBumper = gamepad2.left_bumper;

        if (leftTrigger > 0.1) {
            intakeMotor.setPower(-1);
        }

        if (leftBumper) {
            intakeMotor.setPower(1);
        }
        if (leftTrigger == 0 && !leftBumper) {
            intakeMotor.setPower(0);
        }

        /*
         * Lift motor | Y axis on the right joystick makes lift go up and down
         */

        float leftYStick = -gamepad2.left_stick_y;

        // Power to decrease motor

        double ScalingPower = 0.25;


        if (leftYStick == 0) {
            liftMotor.setPower(0);
        } else {
            if ((leftYStick > 0) && (leftYStick <= 1)) {
                liftMotor.setPower(scaleInput(leftYStick));
            }
            if ((leftYStick < -0.01) && (leftYStick >= -1)) {
                liftMotor.setPower(scaleInput(leftYStick * ScalingPower));
            }
        }


        /*
         * Collector Box
         * Y - Move box up +
         * A - Move box down -
         * X - Move box Left +
         * B - Move box Right -
         *  down - Home for servo positions
         */

        boolean YButtonPressed = gamepad2.y;
        boolean AButtonPressed = gamepad2.a;
        boolean XButtonPressed = gamepad2.x;
        boolean BButtonPressed = gamepad2.b;
        float RightTriggerPressed = gamepad2.right_trigger;
        boolean ManualMode = gamepad2.right_bumper;
        float RightJoystick2 = -gamepad2.right_stick_y;
        // TODO Figure out what button switches between manual mode

        double[] homeValues = {
                0.45, 0.6
        }; //   LR    UD

        double[] flatValues = {
                0.45, 0.5
        }; //   LR    UD
        double tiltPosition = 0.7;
        double dropPosition = 0.35; // .48 or //.5
        double leftPosition = 0.69;
        double rightPosition = 0.19;

        // TODO bind correct buttons

        if (RightTriggerPressed == 1) {
            LeftRightServo.setPosition(flatValues[0]);
            UpDownServo.setPosition(flatValues[1]);
        }

        if (RightJoystick2 == 1) {
            UpDownServo.setPosition(tiltPosition);
        }
        if (RightJoystick2 == -1) {
            UpDownServo.setPosition(dropPosition);
        }


        if (ManualMode) {
            if (YButtonPressed) {
                if (!YButton) {
                    UpDownServo.setPosition(Range.clip(UpDownServo.getPosition() + 0.03, 0, 1));
                    YButton = true;
                }
            } if (!YButtonPressed) {
                YButton = false;
            }

            if (AButtonPressed) {
                if (!AButton) {
                    UpDownServo.setPosition(Range.clip(UpDownServo.getPosition() - 0.03, 0, 1));
                    AButton = true;
                }
            } if (!AButtonPressed) {
                AButton = false;
            }

            if (XButtonPressed) {
                if (!XButton) {
                    LeftRightServo.setPosition(Range.clip(LeftRightServo.getPosition() + 0.03, 0, 1));
                    XButton = true;
                }
            } if (!XButtonPressed) {
                XButton = false;
            }

            if (BButtonPressed) {
                if (!BButton) {
                    LeftRightServo.setPosition(Range.clip(LeftRightServo.getPosition() - 0.03, 0, 1));
                    BButton = true;
                }
            } if (!BButtonPressed) {
                BButton = false;
            }
        } else {

            if (YButtonPressed) {
                if (!YButton) {
                    UpDownServo.setPosition(tiltPosition);
                    LeftRightServo.setPosition(homeValues[0]);
                    YButton = true;
                }
            } if (!YButtonPressed) {
                YButton = false;
            }

            if (AButtonPressed) {
                if (!AButton) {
                    LeftRightServo.setPosition(homeValues[0]);
                    UpDownServo.setPosition(homeValues[1]);
                    AButton = true;
                }
            } if (!AButtonPressed) {
                AButton = false;
            }

            if (XButtonPressed) {
                if (!XButton) {
                    LeftRightServo.setPosition(leftPosition);
                    UpDownServo.setPosition(dropPosition);
                    XButton = true;
                }
            } if (!XButtonPressed) {
                XButton = false;
            }

            if (BButtonPressed) {
                if (!BButton) {
                    LeftRightServo.setPosition(rightPosition);
                    UpDownServo.setPosition(dropPosition);
                    BButton = true;
                }
            } if (!BButtonPressed) {
                BButton = false;
            }
        }


        telemetry.addData("LRServo", String.valueOf(LeftRightServo.getPosition()));
        telemetry.addData("UDServo", String.valueOf(UpDownServo.getPosition()));




        /*
         * Accessories - Includes zipliners, and climbers servo
         * D-Pad Up - Throw climbers into bucket
         * D-Pad Down - Put climbers servo back down
         * D-Pad Left - Throw out zip liners servo
         * D-Pad Right - Put in zip liners servo
         */

        boolean DPadLeftPressed = gamepad2.dpad_left;
        boolean DPadRightPressed = gamepad2.dpad_right;
        boolean DPadUpPressed = gamepad2.dpad_up;
        boolean DPadDownPressed = gamepad2.dpad_down;

        /* Uncomment when accessories are added
        if (DPadLeftPressed) {
            ziplinerServo.setPosition(1);
        }

        if (DPadRightPressed) {
            ziplinerServo.setPosition(0);
        }


        if (DPadUpPressedLong) {

            if (!climbersLifted) {
                if (servotime.time() > servoDelayTime) {
                    climbersServo.setPosition(Range.clip(servoPosition += servoDelta, 0.15, 0.9));
                    servotime.reset();
                }
            }

        }

        if (DPadUpPressed)

            if (DPadDownPressed) {
                climbersServo.setPosition(0.3);
            }
        }

        if (climbersServo.getPosition() == 0.9) {
            climbersLifted = true;
        }

        */




//-------------------------------------------------------------------------------------------------//


		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls
		 *
		 */

        /*
         * Piston | Left Joystick moves piston up and down
         */

        float leftYStick2 = -gamepad1.left_stick_y;

        pistonMotor.setPower(scaleInput(leftYStick2 * 0.25));



        /*
         * Driving code extracted from First Tech Challenge example code
         * Right joystick - Drive
         */

        // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: right_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.right_stick_y;
        float direction = -gamepad1.right_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        driveLeft(left);
        driveRight(right);

        telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));



    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }
    /*
     * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
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
}