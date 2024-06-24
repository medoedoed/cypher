package subcommands;

import picocli.CommandLine.*;
import utils.ChecksumHandler;
import utils.ConfigHandler;
import utils.DirectoryHandler;

@Command(name = "chspw", description = "Change super password")
public class ChangeSuperPasswordSubcommand implements Runnable {

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter.", defaultValue = "false")
    private Boolean isVisible;

    @Override
    public void run() {
        String contentFolder = ConfigHandler.getConfig().getString("contentFolder");
        var checksumPath = DirectoryHandler.getFullPath(contentFolder + ".checksum");

        if (ChecksumHandler.checkCurrentPassword(checksumPath, isVisible)) {
            System.err.println("Super password does not match.");
            return;
        }

       ChecksumHandler.saveChecksum(checksumPath, isVisible);
    }
}
