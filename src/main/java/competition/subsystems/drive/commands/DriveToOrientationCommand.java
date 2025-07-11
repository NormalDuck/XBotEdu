package competition.subsystems.drive.commands;

import javax.inject.Inject;
import javax.sound.midi.SysexMessage;

import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import competition.subsystems.drive.DriveSubsystem;

public class DriveToOrientationCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;
    double initialRotation;
    private double previousRotation;
    private double currentVelocity;
    public double targetRotation;

    @Inject
    public DriveToOrientationCommand(DriveSubsystem driveSubsystem, PoseSubsystem pose) {
        this.drive = driveSubsystem;
        this.pose = pose;
    }

    public void setTargetHeading(double heading) {
        targetRotation = heading;
    }

    @Override
    public void initialize() {
        initialRotation = getCurrentRotation();
        System.out.println(initialRotation);
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
        double rawErrorRotation = (initialRotation + targetRotation) - getCurrentRotation() + Math.abs(initialRotation);

        if (rawErrorRotation <= 360) {
            return rawErrorRotation;
        } else {
            return rawErrorRotation - 360;
        }
    }
}