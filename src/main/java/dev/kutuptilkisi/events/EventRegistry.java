package dev.kutuptilkisi.events;

import dev.kutuptilkisi.packets.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegistry {
    private static HashMap<Class<? extends Packet>, Map.Entry<Object, List<Method>>> eventCallbacks = new HashMap<>();

    public static void register(Object o){
        for(Method method : o.getClass().getMethods()){
            if(method.getParameterCount() == 2){
                Class<?>[] types = method.getParameterTypes();
                if(types[0] == Socket.class && isImplementingPaketInterface(types[1])){
                    Class<? extends Packet> packetClass = (Class<? extends Packet>) types[1];

                    if(!eventCallbacks.containsKey(packetClass)){
                        eventCallbacks.put(packetClass, Map.entry(o, new ArrayList<>()));
                    }

                    eventCallbacks.get(packetClass).getValue().add(method);
                }
            }
        }
    }

    public static void fireEvent(Socket socket, Packet packet){
        if(eventCallbacks.containsKey(packet.getClass())){
            for(Method method : eventCallbacks.get(packet.getClass()).getValue()){
                try {
                    method.invoke(eventCallbacks.get(packet.getClass()).getKey(), socket, packet);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static boolean isImplementingPaketInterface(Class<?> clazz){
        for(Class<?> clazzz : clazz.getInterfaces()){
            if(clazzz == Packet.class){
                return true;
            }
        }
        return false;
    }


}
