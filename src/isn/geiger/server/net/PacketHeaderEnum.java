package isn.geiger.server.net;

/**
 *
 * @author LoadLow
 */
public enum PacketHeaderEnum {

    NULL(0x00),
    //Packets sent by the client
    RECV_HIT(0x01),
    RECV_HITS_LIST(0x02),
    RECV_AUTHKEY(0x03),
    //Packets sent by the server
    ASK_HITS_LIST(0x04),
    ASK_AUTH(0x05),
    AUTH_SUCCESS(0x06),
    AUTH_ERROR(0x07);
    public final byte value;

    private PacketHeaderEnum(byte value) {
        this.value = value;
    }

    private PacketHeaderEnum(int value) {
        this((byte) value);
    }

    public static PacketHeaderEnum valueOf(byte value) {
        for (PacketHeaderEnum header : values()) {
            if (header.value == value) {
                return header;
            }
        }
        return NULL;
    }

    public static PacketHeaderEnum valueOf(int value) {
        return valueOf((byte) value);
    }
}
