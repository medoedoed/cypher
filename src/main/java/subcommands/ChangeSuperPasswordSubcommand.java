package subcommands;

import picocli.CommandLine.*;
import utils.handlers.PassphraseHandler;
import utils.handlers.ConfigHandler;
import utils.handlers.DirectoryHandler;

@Command(name = "chspw", description = "Change super password")
public class ChangeSuperPasswordSubcommand implements Runnable {

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter.", defaultValue = "false")
    private Boolean isVisible;

    @Override
    public void run() {
        String contentFolder = ConfigHandler.getConfig().getString("contentFolder");
        var checksumPath = DirectoryHandler.getFullPath(contentFolder + ".checksum");

        if (PassphraseHandler.checkCurrentPassphrase(checksumPath, isVisible)) {
            System.err.println("Super password does not match.");
            return;
        }

       PassphraseHandler.saveChecksum(checksumPath, isVisible);
    }
}
