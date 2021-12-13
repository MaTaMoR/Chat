package me.matamor.test.packets;

import me.matamor.test.packets.defaults.DisconnectPacket;
import me.matamor.test.packets.defaults.LoginPacket;
import me.matamor.test.packets.defaults.LoginResultPacket;
import me.matamor.test.packets.defaults.MessagePacket;

public enum Protocol {

    MESSAGE(1, MessagePacket.class),
    LOGIN(2, LoginPacket.class),
    LOGIN_RESULT(3, LoginResultPacket.class),
    DISCONNECT(4, DisconnectPacket.class);

    private final int packetId;
    private final Class<? extends Packet> clazz;

    Protocol(int packetId, Class<? extends Packet> clazz) {
        this.packetId = packetId;
        this.clazz = clazz;
    }

    public int getPacketId() {
        return this.packetId;
    }

    public Class<? extends Packet> getPacketClass() {
        return this.clazz;
    }

    public static Protocol getById(int id) {
        for (Protocol protocol : values()) {
            if (protocol.getPacketId() == id) {
                return protocol;
            }
        }

        return null;
    }
}
