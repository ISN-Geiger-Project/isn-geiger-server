package isn.geiger.server.net.packets;

import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author LoadLow
 */
public abstract class PacketPrototype {
    
    public abstract IoBuffer encode();
}
