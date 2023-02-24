package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;



@TeleOp

    
public class TeleOpFinal extends LinearOpMode {
    
    private CRServo armMotor1;
    private CRServo armMotor2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private Blinker control_Hub;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private CRServo grabServo;
    private Gyroscope imu;
//random idk why this exists
    private DcMotorSimple arm1 = null;
    private DcMotorSimple arm2 = null;
    private CRServo grab = null;
    
    // todo: write your code here
    
   @Override
    public void runOpMode() {
        
        final int armRange = 6150;
        
        //4 drive motors
        frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        frontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        backRight = hardwareMap.get(DcMotor.class, "BackRight");
        
        //Arm motors using sparkMini-style motor controllers
        arm2 = hardwareMap.get(DcMotorSimple.class, "ArmMotor2");
        arm1 =hardwareMap.get(DcMotorSimple.class, "ArmMotor1");
       
       //Grab servo is a continuous rotation servo. Seem like it uses the 
       //DCMotorSimple class, like the ESCs.
       grab = hardwareMap.get(CRServo.class, "GrabServo");
       

        
        
        
       
        
       

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        
        //ElapsedTime cooldown = new ElapsedTime();
        ElapsedTime runtime = new ElapsedTime();
        double lastGrabPress = 0;
        //double startTime = 0;
        //double looseTime = 0;
        
        boolean gripIt = false;
        boolean looseIt =true;
        
        
        //BOOOOST BUTTON!!!
        boolean BOOOOST = false;
        
        //Precise movement button
        boolean Precision = false;
        
        double grabPower = -1;
        boolean grabbing = false;
        // run until the end of the match (driver presses STOP)
        int initEncoder = frontLeft.getCurrentPosition();
        int targetPos = initEncoder;
        while (opModeIsActive()) 
        {
            int armEncoder = frontLeft.getCurrentPosition();
        
            //DRIVETRAIN CODE
            //configuring the controller 
            double speed = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            double strafe = -gamepad1.left_stick_x;
            
            BOOOOST = gamepad1.y;
            Precision = gamepad1.x;
            
            
            
            //weird math to make mecanum work
            //Mecanum motors
            
            
            double frontLeftPower = speed+turn+strafe;
            double backLeftPower = speed+turn-strafe;
            double frontRightPower = speed-turn-strafe;
            double backRightPower = speed-turn+strafe;
            
            
            //ARM CODE
            
            //Arm motor power settings
            //2 arm motors
            float rightTrigger = gamepad1.right_trigger;
            float leftTrigger = gamepad1.left_trigger;
            
            //initialization nonsense.
            arm1.setDirection(DcMotorSimple.Direction.FORWARD);
            arm2.setDirection(DcMotorSimple.Direction.FORWARD);
            
            //Sets arm power to the square of the trigger value.
            //Left goes down (subtracted) right goes up (positive)
            //We are squaring it to hopefully make a throttle curve.
            double armPower = (double)(-1.00)*((leftTrigger*leftTrigger)-(rightTrigger*rightTrigger));
            
            //Creates toggle buttons
            //dpad up makes arm go all the way up.
            //dpad down makes arm go all the way down.
            /*if(armPower==0)
            {
                
                if(gamepad1.dpad_up)
                {
                    targetPos = initEncoder + armRange;
                }
                else if(gamepad1.dpad_down)
                {
                    targetPos = initEncoder + 1 ;
                }
                
                //Keeps the arm at the target point.
                if(armEncoder!=targetPos)
                {
                    
                    if(armEncoder<targetPos)
                    {
                        //Increase the power to maintain position.
                        armPower = 0.5;
                    }
                    else
                    {
                        //Decrease the power to maintain position.
                        armPower = -0.5;
                    }
                    
                }
                
            }
            else
            {
                
                targetPos=armEncoder;
            }
            */
            
            //If the arm is going down, reduce the power to quarter.
            //limits arm power to half going up as well.
            
            //EDIT: Made this commented bc we solved issues.
            /*if(leftTrigger>0)
            {
                armPower/=4;
            }
            else if (rightTrigger>0)
            {
                armPower/=2;
            } */
            
            //GRABBER CODE:
            
            boolean leftBumper = gamepad1.left_bumper;
            boolean rightBumper = gamepad1.right_bumper;
            
            
            
            /*
            if((gamepad1.right_bumper) && (runtime.milliseconds()-lastGrabPress > 250))
            {
            lastGrabPress = runtime.milliseconds();
                grabbing = !grabbing;
            }
            */
            
            if(leftBumper&&!rightBumper)
            {
                grabbing = false;
            }
            else if (rightBumper&&!leftBumper)
            {
                grabbing = true;
            }
            
            
            
            
            
            if(grabbing)
            {
                grabPower= 0.70;
            }
            else
            {
                grabPower = -1.00;
            }
            
            /*
            //Coding Buttons to make grabber open/close with a single press
            if((gamepad1.a)&&(runtime.milliseconds()-startTime)>=1250)
            {
            startTime = runtime.milliseconds();
            gripIt = true;
            
            }
            else if (!(gamepad1.a)&&(runtime.milliseconds()-startTime>=1250))
            {
                gripIt = false;
            }
            
            if((gamepad1.b)&&(runtime.milliseconds()-looseTime)>=1250)
            {
            looseTime = runtime.milliseconds();
            gripIt = false;
            looseIt = true;
            }
            else if (!(gamepad1.b)&&(runtime.milliseconds()-looseTime>=1250))
            {
                looseIt = false;
            }
            
            if(looseIt)
            {
                grabPower = -1;
            }
            else if (gripIt)
            {
                grabPower = 1;
            }
            
            if(runtime.milliseconds()<=1250)
            {
                grabPower = 0;
            }
            */
            /*
            int armDelta = initEncoder-armEncoder;
            //Arm safety mechanism
            if ( (Math.abs(armDelta)>=armRange) && (armPower!=0) )
            {
             armPower=0;   
            }
            */
            
            arm1.setPower(armPower);
            arm2.setPower(armPower);
            
            grab.setPower(grabPower);
            
            
            if (Precision)
            {
                frontLeftPower/=5.00;
                frontRightPower/=5.00;
                backRightPower/=5.00;
                backLeftPower/=5.00;
            }
            else if ( BOOOOST == Precision )
            {
                frontLeftPower/=2.00;
                frontRightPower/=2.00;
                backRightPower/=2.00;
                backLeftPower/=2.00;
                
            }
            
            telemetry.addData("Arm power", armPower);
            telemetry.addData("Grabber power", grabPower);
            telemetry.addData("Speed", speed);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Turn", turn);
            telemetry.addData("Arm Encoder Val:", armEncoder);
            telemetry.addData("Initial Encoder Val:", initEncoder);
            telemetry.addData("Target arm position:", targetPos);
            telemetry.addData("Position Delta", (frontLeft.getCurrentPosition()-initEncoder));
            telemetry.update();
            
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower((frontRightPower));
            backRight.setPower(-backRightPower);
            backLeft.setPower(-backLeftPower);
            
        }
    } 
}