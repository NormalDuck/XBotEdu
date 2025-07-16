package competition.subsystems.drive.commands;

import javax.inject.Inject;

import xbot.common.command.BaseCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class TurnLeft90DegreesCommand extends BaseCommand {

    DriveSubsystem drive;
    PoseSubsystem pose;

    double currentYaw;
    double target_rotation;
    double lastYaw = 0;
    double velocity;

    // compareCurrentAndGoal not needed because fixed 90 degrees rotation EVERY TIME in this scenario.
    public double compareCurrentAndGoal(double current, double goal) {
        current %= 180;
        goal %= 180;

        double distance;
        double magnitude1;
        double magnitude2;

        if (current > goal) {
            magnitude1 = current - goal;
            magnitude2 = current - (goal + 360);
        } else {
            magnitude1 = current - goal;
            magnitude2 = current - (goal - 360);
        }

        if (Math.abs(magnitude1) > Math.abs(magnitude2)){
            distance = magnitude2;
        } else {
            distance = magnitude1;
        }

        return -distance;
    }

    @Inject
    public TurnLeft90DegreesCommand(DriveSubsystem driveSubsystem, PoseSubsystem pose) {
        this.drive = driveSubsystem;
        this.pose = pose;
    }

    @Override
    public void initialize() {
        currentYaw = pose.getCurrentHeading().getDegrees();
        target_rotation = currentYaw + 90;

        if (target_rotation > 180) {
            target_rotation -= 360;
        } else if (target_rotation < -180) {
            target_rotation += 360;
        }
    }

    @Override
    public void execute() {
        // It works... but check the visualizer 4th dropdown option.

        double rotationUntilGoal = compareCurrentAndGoal(currentYaw, target_rotation);
        double power = (rotationUntilGoal * 0.023) - (velocity * 0.43);

        drive.tankDrive(-power, power);
        lastYaw = currentYaw;
    }

    public boolean isFinished() {
        currentYaw = pose.getCurrentHeading().getDegrees();
        velocity = currentYaw - lastYaw;

        return (Math.abs(target_rotation - currentYaw) < 0.1 && Math.abs(velocity) < 0.1);
    }
}