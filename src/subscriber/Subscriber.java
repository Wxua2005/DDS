package subscriber;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import node.Node;

public class Subscriber implements Runnable {
    private static final int REGISTRY_PORT = 5000;
    private final Node node;
    private final String topic;
    private final DatagramSocket socket;
    private volatile boolean running;
    private Thread receiveThread;

    public Subscriber(Node node, String topic) {
        this.node = node;
        this.topic = topic;
        this.socket = node.getSocket();
        this.running = true;
        registerWithRegistry();
        startReceiving();
    }

    private void registerWithRegistry() {
        try {
            String message = "SUBSCRIBE:" + topic;
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                buffer,
                buffer.length,
                InetAddress.getLocalHost(),
                REGISTRY_PORT
            );
            socket.send(packet);
            System.out.println(node.getName() + " registered for topic: " + topic);
        } catch (IOException e) {
            System.err.println("Error registering with registry: " + e.getMessage());
        }
    }

    private void startReceiving() {
        receiveThread = new Thread(this);
        receiveThread.start();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (running) {
            try {
                socket.receive(packet);
                String received = new String(
                    packet.getData(),
                    0,
                    packet.getLength(),
                    StandardCharsets.UTF_8
                );
                System.out.println(node.getName() + " received: " + received);
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            }
        }
    }

    public void stop() {
        running = false;
        socket.close();
    }
}