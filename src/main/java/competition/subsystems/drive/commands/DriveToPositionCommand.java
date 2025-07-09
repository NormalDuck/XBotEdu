package competition.subsystems.drive.commands;

import javax.inject.Inject;

import xbot.common.command.BaseCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.XYPair;

public class DriveToPositionCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    private double targetPosition;
    private boolean driveToTargetFinished;
    private double previousPosition;
    private double currentPosition;
    private double currentVelocity;

    @Inject
    public DriveToPositionCommand(DriveSubsystem driveSubsystem, PoseSubsystem pose) {
        this.drive = driveSubsystem;
        this.pose = pose;
    }

    public void setTargetPosition(double position) {
        // This method will be called by the test, and will give you a goal distance.
        // You'll need to remember this target position and use it in your calculations.
        this.targetPosition = position;
    }

    @Override
    public void initialize() {
        // If you have some one-time setup, do it here.
    }

    @Override
    public void execute() {
        // Here you'll need to figure out a technique that:
        // - Gets the robot to move to the target position
        // - Hint: use pose.getPosition() to find out where you are
        // - Gets the robot stop (or at least be moving really really slowly) at the
        // target position

        // How you do this is up to you. If you get stuck, ask a mentor or student for
        // some hints!

        this.step();

        System.out.println(this.currentVelocity);

        double error = targetPosition - pose.getPosition();

        double power = (error * 1/2) - currentVelocity * 13;
        this.drive.tankDrive(power, power);
    }

    @Override
    public boolean isFinished() {
        // Modify this to return true once you have met your goal,
        // and you're moving fairly slowly (ideally stopped)
        double error = targetPosition - pose.getPosition();

        return Math.abs(error) < 0.01 && Math.abs(currentVelocity) < 0.01;
    }

    @Override
    public void end(boolean isInterrupted) {
        drive.tankDrive(0, 0);
    }

    private void step() {
        currentVelocity = this.pose.getPosition() - this.previousPosition;
        this.previousPosition = pose.getPosition();
    }
}
