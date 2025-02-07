package registry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class Registry {
    private static final int REGISTRY_PORT = 5000;
    private DatagramSocket registrySocket;
    private Map<String, List<SubscriberInfo>> topicSubscribers;
    private volatile boolean running;

    public static class SubscriberInfo {
        public final InetAddress address;
        public final int port;

        public SubscriberInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }

    public Registry() {
        try {
            registrySocket = new DatagramSocket(REGISTRY_PORT);
            topicSubscribers = new ConcurrentHashMap<>();
            running = true;
            startListening();
        } catch (SocketException e) {
            System.err.println("Failed to start registry: " + e.getMessage());
        }
    }

    private void startListening() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (running) {
                try {
                    registrySocket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    handleMessage(message, packet.getAddress(), packet.getPort());
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Registry error: " + e.getMessage());
                    }
                }
            }
        }).start();
    } 

    private void handleMessage(String message, InetAddress address, int port) {
        String[] parts = message.split(":");
        if (parts.length < 2) return;

        String command = parts[0];
        String topic = parts[1];

        switch (command) {
            case "SUBSCRIBE":
                addSubscriber(topic, address, port);
                break;
            case "PUBLISH":
                notifySubscribers(topic, parts[2], address, port);
                break;
        }
    }

    private void addSubscriber(String topic, InetAddress address, int port) {
        topicSubscribers.computeIfAbsent(topic, k -> new ArrayList<>())
                       .add(new SubscriberInfo(address, port));
        System.out.println("Added subscriber for topic " + topic + " at " + address + ":" + port);
    }

    private void notifySubscribers(String topic, String message, InetAddress publisherAddress, int publisherPort) {
        List<SubscriberInfo> subscribers = topicSubscribers.get(topic);
        if (subscribers == null) return;

        byte[] buffer = message.getBytes();
        for (SubscriberInfo subscriber : subscribers) {
            try {
                DatagramPacket packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    subscriber.address,
                    subscriber.port
                );
                registrySocket.send(packet);
            } catch (IOException e) {
                System.err.println("Failed to forward message to subscriber: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
        registrySocket.close();
    }

    public static void main(String[] args) {
        new Registry();
        System.out.println("Registry started on port " + REGISTRY_PORT);
    }
}