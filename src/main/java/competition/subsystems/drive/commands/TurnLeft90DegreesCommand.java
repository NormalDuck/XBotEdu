package competition.subsystems.drive.commands;

import javax.inject.Inject;

import xbot.common.command.BaseCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class TurnLeft90DegreesCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    double initialRotation;
    private double previousRotation;
    private double currentVelocity;
    public double targetRotation = 90;

    @Inject
    public TurnLeft90DegreesCommand(DriveSubsystem driveSubsystem, PoseSubsystem pose) {
        this.drive = driveSubsystem;
        this.pose = pose;
    }

    @Override
    public void initialize() {
        initialRotation = getCurrentRotation();
    }

    @Override
    public void execute() {
        step();

        double power = (getError() * 1/2) - currentVelocity * 3;
        this.drive.tankDrive(0, power);
    }

    private double getCurrentRotation() {
        return pose.getCurrentHeading().getDegrees();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(getError()) < 1 && Math.abs(currentVelocity) < 0.01;
    }

    @Override
    public void end(boolean isInterrupted) {
        drive.tankDrive(0, 0);
    }

    private void step() {
        currentVelocity = getCurrentRotation() - previousRotation;
        previousRotation = getCurrentRotation();
    }

    private double getError() {
        double rawErrorRotation = (initialRotation + targetRotation) - getCurrentRotation();
        if (rawErrorRotation <= 360) {
            return rawErrorRotation;
        } else {
            return rawErrorRotation - 360;
        }
    }
}
