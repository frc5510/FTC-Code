package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DistanceSensor;

@Autonomous

public class WhatColorV2 extends LinearOpMode
{
    final double SCALE_FACTOR = 255;
    private CRServo armMotor1;
    private CRServo armMotor2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private ColorSensor colorSensor;
    private Blinker control_Hub;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private CRServo grabServo;
    private Gyroscope imu;
    
    
    
    //EDIT THESE COLORS.. WE HAVE NOT CALIBRATED THESE VALUES FOR OUR SENSOR.
    /*
    private final Color kBlueTarget = new Color(0.143, 0.427, 0.429);
    private final Color kGreenTarget = new Color(0.197, 0.561, 0.240);
    private final Color kRedTarget = new Color(0.561, 0.232, 0.114);
    private final Color kYellowTarget = new Color(0.361, 0.524, 0.113);
    */
    
    //This code will run when you press the INIT button
    @Override
    public void runOpMode()
    {
        

        //Configure variables before the "initialized" section
        float hsvValues[] = {0F, 0F, 0F};
        final double scaleFactor = 255;
        colorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");
        
        //Confirming all is well
        telemetry.addData("Status", "Initialized");
        telemetry.addLine("COLOR SENSOR RAW DATA VALUES:");
        
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            //This code will loop REALLY fast until you press stop.
            /*
            Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                    (int) (colorSensor.green() * SCALE_FACTOR),
                    (int) (colorSensor.blue() * SCALE_FACTOR),
                    hsvValues);
                    */


            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();
            
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
                currentColor="Cyan...?";
            }
            else
            {
                currentColor = "idek";
            }
            
            
            telemetry.addData("Detected Color:", currentColor);
            
            telemetry.update();
            
            

            
            
        }
    }
    
    
}