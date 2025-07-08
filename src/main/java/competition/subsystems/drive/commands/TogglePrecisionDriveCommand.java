package competition.subsystems.drive.commands;

import javax.inject.Inject;

import xbot.common.command.BaseCommand;
import competition.subsystems.drive.DriveSubsystem;

public class TogglePrecisionDriveCommand extends BaseCommand {

    DriveSubsystem drive;
    private boolean toggled = false;

    @Inject
    public TogglePrecisionDriveCommand(DriveSubsystem driveSubsystem) {
        drive = driveSubsystem;
    }

    @Override
    public void initialize() {
        // Here, you want to call the DriveSubsystem and tell it to change its precision
        // mode.
        // This means you'll need to add a new method into DriveSubsystem, and there are
        // two
        // major ways to do this:
        // 1) Create something like getPrecisionMode() and setPrecisionMode() on the
        // DriveSubsystem. Your
        // command can read the current value, change it, and set it back into the
        // subsystem.
        // 2) Create a togglePrecisionMode() on the DriveSubsystem. The system will
        // internally change
        // the current mode.
        // In all of these cases you'll need to have the mode somehow affect the
        // TankDrive method.
        if (toggled) {
            this.drive.setPrecisionMode(1);
            this.toggled = false;
        } else {
            this.drive.setPrecisionMode(0.5);
            this.toggled = true;
        }
    }

    @Override
    public void execute() {
        // No need to put any code here - since we want the toggle to run once,
        // initialize() is the
        // best place to put the code.
    }

    @Override
    public boolean isFinished() {
        // Commands keep running until they are finished.
        // Since we want this command to just run once (toggling precision mode), we
        // say that the command is finished right away.
        return true;
    }
}
