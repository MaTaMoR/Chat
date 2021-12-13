package me.matamor.test.packets.defaults;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.Protocol;
import me.matamor.test.utils.ByteBuilder;

public class LoginResultPacket implements Packet {

    private boolean result;
    private String message;

    public LoginResultPacket() {

    }

    public LoginResultPacket(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean getResult() {
        return this.result;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getId() {
        return Protocol.LOGIN_RESULT.getPacketId();
    }

    @Override
    public byte[] write() {
        return new ByteBuilder().addBoolean(this.result).addString(this.message).toArray();
    }

    @Override
    public void read(byte[] bytes) {
        ByteBuilder byteBuilder = new ByteBuilder(bytes);

        this.result = byteBuilder.getBoolean();
        this.message = byteBuilder.getString();
    }
}
