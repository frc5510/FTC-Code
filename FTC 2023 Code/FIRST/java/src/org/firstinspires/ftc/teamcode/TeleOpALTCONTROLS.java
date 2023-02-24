package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp

    
public class TeleOpALTCONTROLS extends LinearOpMode {
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
        
        ElapsedTime timer = new ElapsedTime();
        
        
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
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) 
        {
            //DRIVETRAIN CODE
            //configuring the controller 
            double speed = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            double strafe = -gamepad1.left_stick_x;
            
            
            
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
            
            
            
            
            
            boolean leftBumper = gamepad1.left_bumper;
            boolean rightBumper = gamepad1.right_bumper;
            
            double armPower=0;
            double armPower2=0;
            
            if (leftBumper)
            {
                armPower = (float)-1.00;
            }
            else if (leftTrigger!=0)
            {
                armPower = leftTrigger;
            }
            
            
            
            if(rightBumper)
            {
                armPower2 = (float)-1.00;
            }
            else if (rightTrigger!=0)
            {
                
            }
            
            
           
            //GRABBER CODE:
            
            
            
    
            
            // EDIT: COMMENTED BECAUSE NEW CONTROLS USE BUMPER
            
            double grabPower = 0;
            if(gamepad1.a)
            {
                grabPower= -1.00;
            }
            else if (gamepad1.y)
            {
                grabPower = 1.00;
            }
            else if (gamepad1.y&&gamepad1.a)
            {
                grabPower= 0.00;
            } 
            
            arm1.setPower(armPower);
            arm2.setPower(armPower2);
            
            grab.setPower(grabPower);
            
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(-frontRightPower);
            backRight.setPower(-backRightPower);
            
        }
    } 
}