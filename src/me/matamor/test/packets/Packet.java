package me.matamor.test.packets;

public interface Packet {

    int getId();

    byte[] write();

    void read(byte[] bytes);

}
