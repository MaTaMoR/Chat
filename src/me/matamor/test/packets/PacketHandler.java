package me.matamor.test.packets;

public interface PacketHandler {

    void handle(SocketHandler socketHandler, Packet packet);

}
