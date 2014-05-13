package isn.geiger.server.net;

import isn.geiger.server.Main;
import isn.geiger.server.net.packets.PacketPrototype;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.configuration.Configuration;
import org.apache.mina.core.buffer.CachedBufferAllocator;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author LoadLow
 */
public class NetServerSocket {

    private NioSocketAcceptor acceptor;
    public final CopyOnWriteArrayList<NetClient> clients = new CopyOnWriteArrayList();

    public void broadcast(PacketPrototype packet) {
        IoBuffer packed = packet.encode();
        String logMessage = "packet " + packet.getClass().getCanonicalName() + " sent.";
        for (NetClient client : clients) {
            client.send(packed);
            Main.println(client, logMessage);
        }
    }

    public NetServerSocket() {
        this.acceptor = new NioSocketAcceptor();
        acceptor.setReuseAddress(true);
        acceptor.getSessionConfig().setTcpNoDelay(true);
        acceptor.setBacklog(10000);

        acceptor.getSessionConfig().setSendBufferSize(1024);
        acceptor.getSessionConfig().setReadBufferSize(1024);

        IoBuffer.setAllocator(new CachedBufferAllocator());

        acceptor.setHandler(new NetServerHandler());
    }

    public void bind(String addr, int port) throws IOException {
        acceptor.bind(new InetSocketAddress(addr, port));
    }

    public void bind(int port) throws IOException {
        acceptor.bind(new InetSocketAddress(port));
    }

    public void stop() {
        acceptor.unbind();
        acceptor.dispose(true);
    }

    public void start(Configuration config) throws Exception {
        this.bind(config.getString("server-host"), config.getInt("server-port"));
    }
}
