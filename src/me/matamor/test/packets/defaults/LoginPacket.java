package me.matamor.test.packets.defaults;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.Protocol;
import me.matamor.test.utils.ByteBuilder;

public class LoginPacket implements Packet {

    private String username;
    private String password;

    public LoginPacket() {

    }

    public LoginPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int getId() {
        return Protocol.LOGIN.getPacketId();
    }

    @Override
    public byte[] write() {
        ByteBuilder byteBuilder = new ByteBuilder();

        byteBuilder.addString(this.username);
        byteBuilder.addString(this.password);

        return byteBuilder.toArray();
    }

    @Override
    public void read(byte[] bytes) {
        ByteBuilder byteBuilder = new ByteBuilder(bytes);

        this.username = byteBuilder.getString();
        this.password = byteBuilder.getString();
    }
}
