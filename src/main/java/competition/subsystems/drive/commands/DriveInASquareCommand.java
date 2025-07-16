package competition.subsystems.drive.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import javax.inject.Inject;
import javax.inject.Provider;

public class DriveInASquareCommand extends SequentialCommandGroup {
    @Inject
    public DriveInASquareCommand(Provider<RelativeDriveCommand> relativeDriveProvider, Provider<TurnLeft90DegreesCommand>turnLeft90Degrees) {
        this.addCommands(
                new SequentialCommandGroup(
                        relativeDriveProvider.get().setTargetPosition(1),
                        turnLeft90Degrees.get(),
                        relativeDriveProvider.get().setTargetPosition(1),
                        turnLeft90Degrees.get(),
                        relativeDriveProvider.get().setTargetPosition(1),
                        turnLeft90Degrees.get(),
                        relativeDriveProvider.get().setTargetPosition(1),
                        turnLeft90Degrees.get()
                )
        );
    }
}
