package me.matamor.test.packets;

import java.util.*;

public class PacketManager {

    private final PacketEncoder packetEncoder = new PacketEncoder();
    private final PacketDecoder packetDecoder = new PacketDecoder();

    private final Map<Class<? extends Packet>, List<PacketHandler>> handlers = new LinkedHashMap<>();

    public PacketEncoder getPacketEncoder() {
        return this.packetEncoder;
    }

    public PacketDecoder getPacketDecoder() {
        return this.packetDecoder;
    }

    public <T extends Packet> void registerHandler(Class<T> packetClass, PacketHandler packetHandler) {
        List<PacketHandler> handlers = this.handlers.computeIfAbsent(packetClass, k -> new ArrayList<>());

        if (!handlers.contains(packetHandler)) {
            handlers.add(packetHandler);
        }
    }

    public void executeHandlers(SocketHandler socketHandler, Packet packet) {
        System.out.println("Trying to execute: " + packet.getClass());

        List<PacketHandler> handlers = this.handlers.get(packet.getClass());
        if (handlers == null) {
            return;
        }

        Iterator<PacketHandler> iterator = handlers.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().handle(socketHandler, packet);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public <T extends Packet> void unregisterHandler(Class<T> packetClass, PacketHandler packetHandler) {
        List<PacketHandler> handlers = this.handlers.get(packetClass);

        if (handlers != null) {
            handlers.remove(packetHandler);
        }
    }
    public List<PacketHandler> getHandlers(Class<? extends Packet> packetClass) {
        return this.handlers.getOrDefault(packetClass, Collections.emptyList());
    }
}
