package isn.geiger.server;

import static isn.geiger.server.Main.App;
import isn.geiger.server.net.packets.AskHitsListPacket;
import isn.geiger.server.net.packets.PacketPrototype;
import java.io.Console;

/**
 *
 * @author LoadLow
 */
public class ConsoleCommands implements Runnable {

    private Thread _t;
    boolean running = false;

    public ConsoleCommands() {
        this._t = new Thread(this);
        _t.setDaemon(true);
        running = true;
        _t.start();
    }

    public void stop() {
        running = false;
        _t.interrupt();
    }

    @Override
    public void run() {
        while (running) {
            Console console = System.console();
            try {
                if (console != null) {
                    String command = console.readLine();
                    evalCommand(command);
                }
            } catch (Exception e) {
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void evalCommand(String command) {
        String[] args = command.split(" ");
        String fct = args[0].toLowerCase();
        switch (fct) {
            case "exit": {
                System.exit(0);
                break;
            }
            case "synchronize": {
                int start = Integer.parseInt(args[1]);
                int end = Integer.parseInt(args[2]);
                PacketPrototype packet = new AskHitsListPacket(start, end);
                App().ServerSocket.broadcast(packet);
                Main.println("Synchronizing hits with geiger-clients at[" + start + "; " + end + "].");
                break;
            }
            case "synchronize-lasthour": {
                int end = ((int) System.currentTimeMillis() / 1000);
                int start = end - 3600;
                PacketPrototype packet = new AskHitsListPacket(start, end);
                App().ServerSocket.broadcast(packet);
                Main.println("Synchronizing last hour hits with geiger-clients.");
            }
            default:
                System.out.println("Commande non reconnue ou incomplete.");
                break;
        }
    }
}
