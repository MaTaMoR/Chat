package me.matamor.test.packets;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketDecoder {

    public Packet decode(byte[] bytes) {
        byte[] packetIdBytes = Arrays.copyOf(bytes, 4);
        int packetId = ByteBuffer.wrap(packetIdBytes).getInt();

        Protocol protocol = Protocol.getById(packetId);
        Class<? extends Packet> packetClass = protocol.getPacketClass();

        try {
            Packet packet = packetClass.newInstance();
            packet.read(Arrays.copyOfRange(bytes, 4, bytes.length));

            return packet;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Packet exception!", e);
        }
    }
}
