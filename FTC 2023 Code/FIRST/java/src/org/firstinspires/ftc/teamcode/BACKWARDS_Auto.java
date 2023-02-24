package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DistanceSensor;

@Autonomous

public class BACKWARDS_Auto extends LinearOpMode
{
    private CRServo armMotor1;
    private CRServo armMotor2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private Blinker control_Hub;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private CRServo grabServo;
    private ColorSensor colorSensor;
    private Gyroscope imu;
    private int signalDirection = 0;
//random idk why this exists
    private DcMotorSimple arm1 = null;
    private DcMotorSimple arm2 = null;
    private CRServo grab = null;
    
    // todo: write your code here
    
   @Override
    public void runOpMode() {
        colorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");
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
       
       //Color Sensor Initialization
       colorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");
       
       

        
        
        
       
        
       

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        ElapsedTime runtime = new ElapsedTime();
        
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) 
        {
            
            
            //COLOR SENSOR CODE:
            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();
            //Compensating for sensor inaccuracy
            blue = (int)( ((double)(blue)) / 0.6 ) ;
            red = (int)  ( ((double)(red)) / 0.875 );
            green = (int) ( ((double)(green)) / 0.925 );
        
            //Combined color value
            int hue = colorSensor.argb();
            //Total luminosity
            int brightness = colorSensor.alpha();
            
            
            telemetry.addLine("RAW COLOR SENSOR VALUES:");
            telemetry.addData("Red value:", red);
            telemetry.addData("Green value:", green);
            telemetry.addData("Blue value:", blue);
            telemetry.addData("Hue value:", hue);
            telemetry.addData("Brightness value:", brightness);
            
            
            telemetry.addLine("");

            //this is just some dumb logic to maybe figure out what the heck it's looking at
            String currentColor;
            if (blue>(green+red))
            {
                currentColor = "Blue";
            }
            else if(red> (green+blue))
            {
                currentColor = "Red";
            }
            else if(green > (red+blue))
            {
                currentColor = "Green";
            }
            else if( ((red+blue)/2)>(green) )
            {
                currentColor="Purple";
            }
            else if ( ((red+green)/2)>(blue) )
            {
                currentColor="Yellow";
            }
            else if (brightness<1000)
            {
                currentColor = "Black";
            }
            else if (brightness>5000)
            {
                currentColor = "White";
            }
            else if ( ((green+blue)/2)>(red) )
            {
                currentColor="Cyan";
            }
            else
            {
                currentColor = "idek";
            }
            
            
            telemetry.addData("Detected Color:", currentColor);
            
            //DRIVETRAIN CODE
            // 
            double speed = 0;
            double turn = -0;
            double strafe = -0;
            
            
            
            
           
            
            
            if(runtime.milliseconds()<=2650)
            {
                speed = 0.25;
            }
            else if ((runtime.milliseconds()>1000)&&(runtime.milliseconds()<=3000))
            {
                speed = 0;
                //Deciding which direction to go in...
                if((currentColor.equals("Purple"))|| (currentColor.equals("Blue")) )
                {
                    //if purple, do nothing.
                    signalDirection = 0;
                }
                else if(currentColor.equals("Yellow"))
                {
                    //If yellow, go left.
                    signalDirection = 1;
                }
                else if(currentColor.equals("Cyan"))
                {
                    //If Green, go right.
                    signalDirection = -1;
                }
                else
                {
                    //If we dunno the color, then do the green one as a failsafe.
                    signalDirection = -1;
                }
            }
            else if ((runtime.milliseconds()>3000)&&(runtime.milliseconds()<=4200))
            {
                speed = 0;
                strafe = ((-signalDirection)/2.00);
                
                
            }
            else
            {
                strafe = 0;
                speed = 0;
            }
            
            telemetry.addLine("MOVEMENT VALUES:");
            telemetry.addData("Speed", speed);
            telemetry.addData("Strafe", strafe);
            telemetry.update();
            

            
            
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
            
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(-backLeftPower);
            frontRight.setPower((frontRightPower));
            backRight.setPower(-backRightPower);
            
        }
    } 
}