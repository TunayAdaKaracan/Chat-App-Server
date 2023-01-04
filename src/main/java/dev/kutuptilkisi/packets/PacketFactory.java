package dev.kutuptilkisi.packets;

import dev.kutuptilkisi.packets.impl.common.MessagePacket;
import dev.kutuptilkisi.packets.impl.incoming.AuthPacket;
import dev.kutuptilkisi.packets.impl.incoming.RegistryPacket;
import dev.kutuptilkisi.packets.impl.incoming.RequestHistoryPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketFactory {
    private final Map<Integer, Supplier<Packet>> packets = new HashMap<>();

    public PacketFactory(){
        register(0, MessagePacket::new)
                .register(1, AuthPacket::new)
                .register(3, RegistryPacket::new)
                .register(4, RequestHistoryPacket::new);
    }

    public PacketFactory register(int id, Supplier<Packet> creator){
        packets.put(id, creator);
        return this;
    }

    public <T extends Packet> T fromId(int id){
        if(!packets.containsKey(id)) return null;
        return (T) packets.get(id).get();
    }
}
