package isn.geiger.server;

import isn.geiger.server.net.NetClient;

/**
 *
 * @author LoadLow
 */
public class Main {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (App != null) {
                    println("Closing server...");
                    App.stop();
                }
                println("Server closed!");
            }
        });
    }
    private static App App;

    public static App App() {
        return App;
    }

    /**
     * @param args[0]: the config.xml file path(optional)
     */
    public static void main(String[] args) {
        System.out.println("ISN Geiger Project Server");
        System.out.println("=========================");
        
        App = new App();
        App.start(args);
        
        println("Server started!");
    }

    public static void propagate(Throwable tr) {
        App.errorsLogger.logErr(tr);
    }

    public static void println(String message) {
        System.out.println(message);
        App.infosLogger.log(message);
    }

    public static void println(NetClient client, String message) {
        println("[" + client.getRemoteAddress().getHostString()
                + ":" + client.getRemoteAddress().getPort() + "] "
                + message);
    }
}
