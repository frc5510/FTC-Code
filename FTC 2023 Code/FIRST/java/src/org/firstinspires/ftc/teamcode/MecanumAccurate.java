package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;

@TeleOp

public class MecanumAccurate extends LinearOpMode
{
    private DcMotor backLeft;
    private DcMotor backRight;
    private Blinker control_Hub;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private Gyroscope imu;

    // todo: write your code here
    
@Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        frontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        backRight = hardwareMap.get(DcMotor.class, "BackRight");
       

        
        
        
       
        
       

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) 
        {
            //DRIVETRAIN CODE
            //configuring the controller 
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;
            double power = -Math.hypot(x,y);
            double theta = Math.atan2(y,x);
            
            //weird math to make mecanum work
            
            double sin = Math.sin(theta - Math.PI/4);
            double cos = Math.cos(theta - Math.PI/4);
            double max = Math.max(Math.abs(sin), Math.abs(cos));
            
            
            double frontLeftPower = power*cos/max+turn;
            double backLeftPower = power*sin/max-turn;
            double frontRightPower = power*sin/max+turn;
            double backRightPower = power*cos/max-turn;
            
            if((power+Math.abs(turn)) > 1) {
                frontLeftPower /= power+turn;
                backLeftPower /= power+turn;
                frontRightPower /= power+turn;
                backRightPower /= power+turn;
            }
            
          // leftDrive.setPower(leftPower);
            //rightDrive.setPower(rightPower);
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(-frontRightPower);
            backRight.setPower(-backRightPower);
            
        }
    } 
    
}