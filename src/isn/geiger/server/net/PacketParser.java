package isn.geiger.server.net;

import isn.geiger.server.Main;
import static isn.geiger.server.Main.App;
import static isn.geiger.server.Main.propagate;
import isn.geiger.server.database.entities.Hitlog;
import isn.geiger.server.net.packets.AuthStatePacket;
import isn.geiger.server.net.packets.PacketPrototype;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.Charsets;
import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author LoadLow
 */
public class PacketParser {

    private static final Map<Byte, PacketDecoder> decoders = new HashMap<>();

    private interface PacketDecoder {

        public boolean canParse(NetClient client);

        public void decode(NetClient client, IoBuffer pBuff) throws Exception;
    }

    private static void register(PacketHeaderEnum head, PacketDecoder decoder) {
        decoders.put(head.value, decoder);
    }
    private final static CharsetDecoder UTF8_DECODER = Charsets.UTF_8.newDecoder();

    static {
        register(PacketHeaderEnum.RECV_AUTHKEY, new PacketDecoder() {
            @Override
            public void decode(NetClient client, IoBuffer pBuff) {
                try {
                    String auth = pBuff.getString(UTF8_DECODER);
                    boolean success = client.authentifiate(auth);
                    PacketPrototype resp = new AuthStatePacket(success);
                    if (success) {
                        client.setLogged();
                        client.send(resp);
                    } else {
                        client.sendAndDisconnect(resp);
                    }
                } catch (CharacterCodingException ex) {
                    client.disconnect();
                }
            }

            @Override
            public boolean canParse(NetClient client) {
                return !client.isLogged();
            }
        });

        register(PacketHeaderEnum.RECV_HIT, new PacketDecoder() {
            @Override
            public void decode(NetClient client, IoBuffer pBuff) throws Exception {
                Hitlog hitLog = new Hitlog(new Date(pBuff.getInt() * 1000), pBuff.getInt());
                App().Database.HitLogController.create(hitLog);
            }

            @Override
            public boolean canParse(NetClient client) {
                return client.isLogged();
            }
        });

        register(PacketHeaderEnum.RECV_HITS_LIST, new PacketDecoder() {
            @Override
            public void decode(NetClient client, IoBuffer pBuff) throws Exception {
                try{
                int count = pBuff.getInt();
                for (int i = 0; i < count; i++) {
                    long date = pBuff.getInt();
                    Hitlog hitLog = new Hitlog(new Date(date*1000), pBuff.getInt());
                    App().Database.HitLogController.create(hitLog);
                }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }

            @Override
            public boolean canParse(NetClient client) {
                return client.isLogged();
            }
        });
    }

    public static void Parse(NetClient client, IoBuffer pBuff) {
        try {
            if (pBuff.remaining() > 2) {
                PacketHeaderEnum head = PacketHeaderEnum.valueOf(pBuff.get());
                PacketDecoder decoder = decoders.get(head.value);
                if (decoder != null && decoder.canParse(client)) {
                    Main.println(client, "packet " + head.name() + " received.");
                    decoder.decode(client, pBuff);
                }
            }
        } catch (Exception e) {
            propagate(e);
        }
    }
}
