package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;

@Autonomous

public class AutonomousTesting extends LinearOpMode
{
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
        ElapsedTime runtime = new ElapsedTime();
        boolean ran = false;
        
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) 
        {
            //DRIVETRAIN CODE
            // 
            double speed = 0;
            double turn = -0;
            double strafe = -0;
            
            
            if(!ran)
            {
            runtime.reset();
            ran = true;
            }
            
           
            
            
            if(runtime.milliseconds()<=2100)
            {
                strafe = 1;
            }
            else
            {
                strafe = 0;
            }

            
            
            /* Originally:
            double speed = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            double strafe = -gamepad1.left_stick_x; */
            
            
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
            double armPower = (double)((leftTrigger*leftTrigger)-(rightTrigger*rightTrigger));
            
            
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
            
            double grabPower = 0;
            if(leftBumper)
            {
                grabPower= -1.00;
            }
            else if (rightBumper)
            {
                grabPower = 1.00;
            }
            else if ((!(leftBumper&&rightBumper))||(leftBumper&&rightBumper))
            {
                grabPower= 0.00;
            }
            
            arm1.setPower(armPower);
            arm2.setPower(armPower);
            
            grab.setPower(grabPower);
            
            frontLeft.setPower(frontLeftPower/2.00);
            backLeft.setPower(backLeftPower/2.00);
            frontRight.setPower((-frontRightPower/2.00)*1.15);
            backRight.setPower(-backRightPower/2.00);
            
        }
    } 
}