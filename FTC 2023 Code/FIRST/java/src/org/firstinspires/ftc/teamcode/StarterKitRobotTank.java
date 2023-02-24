package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp

public class StarterKitRobotTank extends LinearOpMode {
    private Gyroscope imu;
    //LeftDrive is intended for Motor 0
    private DcMotor LeftDrive;
    //RightDrive is intended for Motor 2
    private DcMotor RightDrive;
    //BackLeftDrive is intended for Motor 1
    private DcMotor BackLeftDrive;
    //BackRightDrive is intended for Motor 3
    private DcMotor BackRightDrive;
    
    
    private DcMotor ArmMotor;
    private DcMotor IntakeMotor;
    private Servo DuckSpinner;


    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        LeftDrive = hardwareMap.get(DcMotor.class, "FrontLeft");
        RightDrive = hardwareMap.get(DcMotor.class, "FrontRight");
        ArmMotor = hardwareMap.get(DcMotor.class, "Arm");
        ArmMotor.setTargetPosition(0);
        ArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        IntakeMotor = hardwareMap.get(DcMotor.class, "Intake");
        
        DuckSpinner = hardwareMap.get(Servo.class, "DuckSpinner");
        
        int ArmTarget = 0;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //DRIVETRAIN CODE
            double rightAxis = -gamepad1.left_stick_y;
            double leftAxis = gamepad1.right_stick_y;
            
            double leftPower = leftAxis;
            double rightPower = rightAxis;
            
            LeftDrive.setPower(leftPower);
            RightDrive.setPower(rightPower);
            
            //MECHANISM CODE
            
            double IntakePower = (gamepad1.right_trigger>0.5)?1:((gamepad1.left_trigger>0.5)?-1:0);
            
            double SpinnerPower = gamepad1.dpad_left?1:(gamepad1.dpad_right?0:0.5);
            
            if (gamepad1.a) {
                ArmTarget = 0; //On the ground for starting and intaking
            }
            else if (gamepad1.x) {
                ArmTarget = 120; //Low level on the goal
            }
            else if (gamepad1.y) {
                ArmTarget = 260; //Mid level on the goal
            }
            else if (gamepad1.b) {
                ArmTarget = 410; //High level on the goal
            }
            else if (gamepad1.right_bumper) {
                ArmTarget = 1420; //High level on the goal scoring backwards
            }
            else if (gamepad1.left_bumper) {
                ArmTarget = 1570; //Mid level on the goal scoring backwards
            }
            
            //stuff for arm position control
            ArmMotor.setTargetPosition(ArmTarget);
            ArmMotor.setPower(1);
            
            IntakeMotor.setPower(IntakePower);
            
            DuckSpinner.setPosition(SpinnerPower);
             
            telemetry.addData("Arm Position", ArmMotor.getCurrentPosition());
            telemetry.update();
            }
        }
    }

