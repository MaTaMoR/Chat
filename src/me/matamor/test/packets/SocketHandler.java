package me.matamor.test.packets;

import me.matamor.test.utils.Callback;
import me.matamor.test.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketHandler implements Runnable {

    private static final PacketEncoder PACKET_ENCODER = new PacketEncoder();
    private static final PacketDecoder PACKET_DECODER = new PacketDecoder();

    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Callback<Pair<SocketHandler, Packet>> callback;

    public SocketHandler(Socket socket, Callback<Pair<SocketHandler, Packet>> callback) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();

        this.callback = callback;
    }

    public boolean isClosed() {
        return this.socket == null || this.socket.isClosed();
    }

    public void close() throws IOException {
        if (this.socket != null) {
            this.inputStream = null;
            this.outputStream = null;

            this.socket.close();
            this.socket = null;
        }
    }

    public Callback<Pair<SocketHandler, Packet>> getCallback() {
        return this.callback;
    }

    public void setCallback(Callback<Pair<SocketHandler, Packet>> callback) {
        this.callback = callback;
    }

    public void sendPacket(Packet packet) throws IOException {
        if (!isClosed()) {
            //This instance shouldn't be here, but it's just a test
            byte[] bytes = PACKET_ENCODER.encode(packet);

            this.outputStream.write(1);
            this.outputStream.write(bytes);
        }
    }

    @Override
    public void run() {
        if (!isClosed()) {
            try {
                //Wait a bit to see if there is any available byte
                long availableTime = System.currentTimeMillis();
                while (this.inputStream.available() < 0 && (System.currentTimeMillis() - availableTime < 10)) {

                }

                if (this.inputStream.available() > 0) {
                    if (this.inputStream.read() == 1) {
                        //First we gotta read the packet size, so we can know how many more bytes we need
                        byte[] packetSizeBytes = new byte[Integer.BYTES];
                        this.inputStream.read(packetSizeBytes);

                        int packetSize = ByteBuffer.wrap(packetSizeBytes).getInt();

                        //Probably add a limit here for the packet size

                        //Create the array to handle all the incoming bytes
                        byte[] packetBytes = new byte[packetSize];

                        //Now we start reading the packet
                        long startTime = System.currentTimeMillis();

                        int totalReadBytes = 0;

                        //Read for half a second
                        while (!isClosed() && totalReadBytes != packetSize && (System.currentTimeMillis() - startTime < 500)) {
                            totalReadBytes += this.inputStream.read(packetBytes);
                        }

                        if (totalReadBytes != packetSize) {
                            System.out.println("Didn't read all the bytes! " + totalReadBytes + " / " + packetSize);
                        } else {
                            Packet packet = PACKET_DECODER.decode(packetBytes);

                            System.out.println("Received packet: " + packet.getId() + " | From: " + this.socket.getInetAddress().getHostAddress());

                            this.callback.callback(new Pair<>(this, packet), null);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("closed");
        }
    }
}