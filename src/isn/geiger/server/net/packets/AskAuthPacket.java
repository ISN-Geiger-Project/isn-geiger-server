package isn.geiger.server.net.packets;

import isn.geiger.server.net.PacketHeaderEnum;
import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author LoadLow
 */
public class AskAuthPacket extends PacketPrototype {

    private int[] key;

    public AskAuthPacket(int[] key) {
        this.key = key;
    }

    @Override
    public IoBuffer encode() {
        IoBuffer pbuff = IoBuffer.allocate(1 + 4 + 4 * key.length);
        //header (1)
        //length of int[] (4)
        //int per int(4*length)
        pbuff.put(PacketHeaderEnum.ASK_AUTH.value);
        pbuff.putInt(key.length);
        for (int i = 0; i < key.length; i++) {
            pbuff.putInt(key[i]);
        }
        return pbuff.flip();
    }
}
