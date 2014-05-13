package isn.geiger.server.net.packets;

import isn.geiger.server.net.PacketHeaderEnum;
import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author LoadLow
 */
public class AuthStatePacket extends PacketPrototype {

    private boolean success;

    public AuthStatePacket(boolean success) {
        this.success = success;
    }

    @Override
    public IoBuffer encode() {
        IoBuffer pbuff = IoBuffer.allocate(2);
        pbuff.put(success ? (PacketHeaderEnum.AUTH_SUCCESS.value) : (PacketHeaderEnum.AUTH_ERROR.value));
        return pbuff.flip();
    }
}
