import dataAccess.ConnectionProvider;
import dataAccess.PasswordRepository;
import picocli.CommandLine;
import subcommands.ChangePassphrase;
import subcommands.InitSubcommand;
import subcommands.SaveSubcommand;
import subcommands.ShowSubcommand;

import java.sql.SQLException;

@CommandLine.Command(name = "PasswordManager",
        version = "Password manager 'pwm' 0.1",
        subcommands = {
                InitSubcommand.class,
                ChangePassphrase.class,
                SaveSubcommand.class,
                ShowSubcommand.class,
        },
        mixinStandardHelpOptions = true)
public class PasswordManager implements Runnable {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String dbDir = System.getProperty("user.home") + "/.passwords";
//        int exitCode = new CommandLine(new PasswordManager()).execute(args);
        var rep = new PasswordRepository(new ConnectionProvider().connect(dbDir));
        rep.createPasswordTable();
        rep.saveService("service", "adqwdq", "dqdwqdqwd");
        rep.getAllServices().stream().forEach(System.out::println);
//        System.exit(exitCode);
    }

    @Override
    public void run() {

    }
}
