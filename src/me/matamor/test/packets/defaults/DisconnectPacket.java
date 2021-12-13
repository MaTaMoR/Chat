package me.matamor.test.packets.defaults;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.Protocol;
import me.matamor.test.utils.ByteBuilder;

public class DisconnectPacket implements Packet {

    private String message;

    public DisconnectPacket() {

    }

    public DisconnectPacket(String message) {
        this.message = message;
    }

    @Override
    public int getId() {
        return Protocol.DISCONNECT.getPacketId();
    }

    @Override
    public byte[] write() {
        return new ByteBuilder().addString(this.message).toArray();
    }

    @Override
    public void read(byte[] bytes) {
        this.message = new ByteBuilder(bytes).getString();
    }
}
