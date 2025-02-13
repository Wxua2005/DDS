package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

import publisher.Publisher;
import subscriber.Subscriber;

public class Node {
    private static final int REGISTRY_PORT = 5000;
    private final String nodeName;
    private final DatagramSocket socket;

    public Node(String nodeName) {
        try {
            this.nodeName = nodeName;
            this.socket = new DatagramSocket();
            System.out.println("Node " + nodeName + " started on port " + socket.getLocalPort());
        } catch (SocketException err) {
            System.err.println("Error creating node: " + err.getMessage());
            throw new RuntimeException(err);
        }
    }

    public Publisher createPublisher(String topic) {
        return new Publisher(this, topic);
    }

    public Subscriber createSubscriber(String topic) {
        return new Subscriber(this, topic);
    }

    public int getPort() {
        return socket.getLocalPort();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public String getName() {
        return nodeName;
    }

    public void registerWithRegistry(String topic, int port) {
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
        } catch (IOException e) {
            System.err.println("Failed to register with registry: " + e.getMessage());
        }
    }
}