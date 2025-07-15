package competition.operator_interface;

import javax.inject.Inject;
import javax.inject.Singleton;

import competition.subsystems.drive.commands.DriveInASquareCommand;
import competition.subsystems.drive.commands.RelativeDriveCommand;
import competition.subsystems.drive.commands.TurnLeft90DegreesCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import competition.simulation.EduSimulator;
import competition.subsystems.drive.commands.DriveToPositionCommand;
import xbot.common.controls.sensors.XXboxController.XboxButton;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

/**
 * Maps operator interface buttons to commands
 */
@Singleton
public class OperatorCommandMap {
    
    @Inject
    public OperatorCommandMap() {}

    // Example for setting up a command to fire when a button is pressed:
    @Inject
    public void setupMyCommands(
            OperatorInterface operatorInterface,
            SetRobotHeadingCommand resetHeading,
            DriveInASquareCommand driveInASquare,
            TurnLeft90DegreesCommand turnLeft90
            )
    {
        resetHeading.setHeadingToApply(90);
        operatorInterface.gamepad.getifAvailable(XboxButton.Start).whileTrue(resetHeading);
        operatorInterface.gamepad.getifAvailable(XboxButton.A).onTrue(driveInASquare);
        operatorInterface.gamepad.getifAvailable(XboxButton.X).onTrue(turnLeft90);


        // Add new button mappings here!
    }


    @Inject
    public void setupDriveToPositionTesting(DriveToPositionCommand driveToPositionCommand, EduSimulator simulator) {
        // let's teleport the robot back to the start each time the button is pressed so repeated testing is easier
        SmartDashboard.putData("DriveToPosition",
                new InstantCommand(() -> {
                    simulator.reset();
                }).andThen(driveToPositionCommand));
        driveToPositionCommand.setTargetPosition(5);
    }
}
