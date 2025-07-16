package competition.subsystems.drive.commands;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;

import javax.inject.Inject;

public class RelativeDriveCommand extends BaseCommand  {
    DriveSubsystem drive;
    PoseSubsystem pose;

    public double targetPosition = 100;
    public double initialPosition;

    private double previousPosition;
    private double currentVelocity;

    @Inject
    public RelativeDriveCommand(DriveSubsystem drive, PoseSubsystem pose) {
        this.drive = drive;
        this.pose = pose;
    }

    public RelativeDriveCommand setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
        return this;
    }

    @Override
    public void initialize() {
        this.initialPosition = pose.getPosition();
    }

    @Override
    public void execute() {
        this.step();

        double error = (targetPosition + initialPosition) - pose.getPosition();

        double power = (error * 1/2) - currentVelocity * 13;
        this.drive.tankDrive(power, power);
    }

    @Override
    public boolean isFinished() {
        // Modify this to return true once you have met your goal,
        // and you're moving fairly slowly (ideally stopped)
        double error = (targetPosition + initialPosition) - pose.getPosition();

        return Math.abs(error) < 0.01 && Math.abs(currentVelocity) < 0.001;
    }

    @Override
    public void end(boolean isInterrupted) {
        drive.tankDrive(0, 0);
    }

    private void step() {
        currentVelocity = pose.getPosition() - this.previousPosition;
        this.previousPosition = pose.getPosition();
    }
}
