package me.matamor.test.packets;

import me.matamor.test.utils.ByteBuilder;

import java.nio.ByteBuffer;

public class PacketEncoder {

    public byte[] encode(Packet packet) {
        byte[] packetBytes = packet.write();

        ByteBuilder byteBuilder = new ByteBuilder((Integer.BYTES * 2) + packetBytes.length);
        byteBuilder.addInt(packetBytes.length + Integer.BYTES);
        byteBuilder.addInt(packet.getId());
        byteBuilder.addBytes(packetBytes);

        return byteBuilder.toArray();
    }
}
