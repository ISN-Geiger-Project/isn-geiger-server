package isn.geiger.server.net;

import isn.geiger.server.Main;
import static isn.geiger.server.Main.App;
import isn.geiger.server.net.packets.PacketPrototype;
import java.net.InetSocketAddress;
import java.util.Random;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author LoadLow
 */
public class NetClient {

    private IoSession session;
    private boolean logged;
    private String authKey;
    private final static Random rand = new Random();

    public int[] genAuthKey() {
        StringBuilder builder = new StringBuilder();
        int[] key = new int[8];
        for (int i = 0; i < 8; i++) {
            key[i] = rand.nextInt(65635);
            builder.append(key[i]);
        }
        this.authKey = builder.toString();
        return key;
    }

    public boolean authentifiate(String auth) {
        //Just antiKikoo protection
        StringBuilder realAuth = new StringBuilder(App().getConfig().getString("client-user"));
        realAuth.append('\u0000');
        realAuth.append(App().getConfig().getString("client-pass"));
        realAuth.append('\u0000');
        realAuth.append(authKey);
        return auth.equals(DigestUtils.md5Hex(realAuth.toString()));
    }

    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) session.getRemoteAddress();
    }

    public boolean isLogged() {
        return logged;
    }

    public NetClient(IoSession session) {
        this.session = session;
    }

    public void setLogged() {
        this.logged = true;
        App().ServerSocket.clients.add(this);
        Main.println(this, "now logged.");
    }

    public void send(PacketPrototype toSend) {
        session.write(toSend.encode());
        Main.println(this, "packet " + toSend.getClass().getCanonicalName() + " sent.");
    }

    public void send(IoBuffer toSend) {
        session.write(toSend);
    }

    public void sendAndDisconnect(PacketPrototype toSend) {
        session.write(toSend.encode()).addListener(IoFutureListener.CLOSE);
        Main.println(this, "packet " + toSend.getClass().getCanonicalName() + " sent and client disconnected.");
    }

    public void disconnect() {
        session.close(true);
    }
}
