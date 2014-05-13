package isn.geiger.server.net;

import isn.geiger.server.Main;
import static isn.geiger.server.Main.App;
import static isn.geiger.server.Main.propagate;
import isn.geiger.server.net.packets.AskAuthPacket;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author LoadLow
 */
public class NetServerHandler extends IoHandlerAdapter {

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        NetClient client = new NetClient(session);
        session.setAttribute("client", client);
        client.send(new AskAuthPacket(client.genAuthKey()));
        Main.println(client, "connected.");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Object objC = session.getAttribute("client");
        if (objC != null && objC instanceof NetClient) {
            NetClient client = (NetClient) objC;
            if (message instanceof IoBuffer) {
                PacketParser.Parse(client, (IoBuffer) message);
            }
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Object objC = session.removeAttribute("client");
        if (objC != null && objC instanceof NetClient) {
            NetClient client = (NetClient) objC;
            if (client.isLogged()) {
                Main.println(client, "logout.");
                App().ServerSocket.clients.remove(client);
            } else {
                Main.println(client, "disconnected.");
            }
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        propagate(cause);
    }
}
