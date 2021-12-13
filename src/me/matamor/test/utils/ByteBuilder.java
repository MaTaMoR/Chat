package me.matamor.test.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteBuilder {

    private byte[] content;
    private int currentPosition;

    public ByteBuilder() {
        this(0);
    }

    public ByteBuilder(int bytes) {
        this.content = new byte[bytes];
    }

    public ByteBuilder(byte[] content) {
        this.content = content;
        this.currentPosition = content.length;
    }

    public ByteBuilder addInt(int value) {
        byte[] bytes = new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value };

        return addBytes(bytes);
    }

    public ByteBuilder addBoolean(boolean value) {
        return addBytes(new byte[] { (value ? (byte) 1 : (byte) 0) });
    }

    public ByteBuilder addString(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

        return addInt(bytes.length).addBytes(bytes);
    }

    public int getInt() {
        if (this.content.length < Integer.BYTES) {
            throw new IllegalStateException("Not enough bytes to read an Int!");
        }

        byte[] content = Arrays.copyOfRange(this.content, 0, Integer.BYTES);

        removeBytes(Integer.BYTES);

        return  ((0xFF & content[0]) << 24) |
                ((0xFF & content[1]) << 16) |
                ((0xFF & content[2]) << 8) |
                (0xFF & content[3]);
    }

    public boolean getBoolean() {
        if (this.content.length < 1) {
            throw new IllegalStateException("Not enough bytes to read an Boolean!");
        }

        byte value = this.content[0];

        removeBytes(1);

        return (value == 1);
    }

    public String getString() {
        int size = getInt();

        if (this.content.length < size) {
            throw new IllegalStateException("Not enough bytes to read a String!");
        }

        byte[] content = Arrays.copyOfRange(this.content, 0, size);

        removeBytes(size);

        return new String(content, StandardCharsets.UTF_8);
    }

    public ByteBuilder addBytes(byte[] bytes) {
        //First we need to know how many more bytes are left free in our array
        int remainingBytes = this.content.length - this.currentPosition;
        //Now we get how many extra bytes we need
        int neededBytes = bytes.length - remainingBytes;

        //If we need any more bytes we gotta expand our array
        if (neededBytes > 0) {
            byte[] content = new byte[this.content.length + neededBytes];
            System.arraycopy(this.content, 0, content, 0, this.content.length);

            this.content = content;
        }

        //Now we can finally add the extra bytes
        System.arraycopy(bytes, 0, this.content, this.currentPosition, bytes.length);

        this.currentPosition += bytes.length;

        return this;
    }

    private ByteBuilder removeBytes(int bytes) {
        if (bytes > this.content.length) {
            throw new IllegalStateException("Can't remove more bytes than the actual content!");
        }

        this.content = Arrays.copyOfRange(this.content, bytes, this.content.length);
        this.currentPosition -= bytes;

        return this;
    }

    public byte[] toArray() {
        return this.content;
    }
}
