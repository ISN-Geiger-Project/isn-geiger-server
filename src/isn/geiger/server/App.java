package isn.geiger.server;

import static isn.geiger.server.Main.propagate;
import isn.geiger.server.database.Database;
import isn.geiger.server.net.NetServerSocket;
import isn.geiger.server.utils.Config;
import isn.geiger.server.utils.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author LoadLow
 */
public class App {

    private Config Configuration;
    public final Database Database;
    public final NetServerSocket ServerSocket;
    public final ScheduledExecutorService Scheduler;
    public final Logger errorsLogger;
    public final Logger infosLogger;
    public final ConsoleCommands ConsoleCommands;
    
    public Configuration getConfig(){
        return Configuration.getConfiguration();
    }

    public App() {
        this.Database = new Database("isn.geiger.database");
        this.ServerSocket = new NetServerSocket();
        this.Scheduler = Executors.newScheduledThreadPool(2);
        this.errorsLogger = new Logger("errors.log");
        this.infosLogger = new Logger("infos.log");
        this.ConsoleCommands = new ConsoleCommands();
    }

    public void start(String[] args) {
        try {
            this.Configuration = new Config(args.length > 0 ? (args[0]) : ("config.xml"));

            this.errorsLogger.scheduleBy(Scheduler);
            this.infosLogger.scheduleBy(Scheduler);
            this.ServerSocket.start(this.Configuration.getConfiguration());
            this.ConsoleCommands.start();
        } catch (Exception e) {
            propagate(e);
        }
    }

    public void stop() {
        this.ServerSocket.stop();
        this.Scheduler.shutdown();
        this.infosLogger.close();
        this.errorsLogger.close();
        this.ConsoleCommands.stop();
    }
}
