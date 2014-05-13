package isn.geiger.server.net.packets;

import isn.geiger.server.net.PacketHeaderEnum;
import java.util.Date;
import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author LoadLow
 */
public class AskHitsListPacket extends PacketPrototype {

    private int startTime;
    private int endTime;

    public AskHitsListPacket(Date startTime, Date endTime) {
        this.startTime = (int) startTime.getTime() / 1000;
        this.endTime = (int) endTime.getTime() / 1000;
    }

    public AskHitsListPacket(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public IoBuffer encode() {
        IoBuffer buffer = IoBuffer.allocate(12);
        buffer.put(PacketHeaderEnum.ASK_HITS_LIST.value);
        buffer.putInt(startTime);
        buffer.putInt(endTime);
        return buffer.flip();
    }
}
