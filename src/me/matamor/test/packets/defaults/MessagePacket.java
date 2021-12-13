package me.matamor.test.packets.defaults;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.Protocol;

import java.nio.charset.StandardCharsets;

public class MessagePacket implements Packet {

    private String message;

    public MessagePacket() {

    }

    public MessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public int getId() {
        return Protocol.MESSAGE.getPacketId();
    }

    @Override
    public byte[] write() {
        return this.message.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void read(byte[] bytes) {
        this.message = new String(bytes, StandardCharsets.UTF_8);
    }
}
