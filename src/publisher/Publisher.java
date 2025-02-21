package publisher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import node.Node;

public class Publisher {
    private static final int REGISTRY_PORT = 5001;
    private static final String REGISTRY_IP = "192.168.201.150"; // Registry IP address
    private final Node node;
    private final String topic;
    private final DatagramSocket socket;

    public Publisher(Node node, String topic) {
        this.node = node;
        this.topic = topic;
        this.socket = node.getSocket();
    }

    public void publish(String data) {
        try {
            String message = "PUBLISH:" + topic + ":" + data;
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                buffer,
                buffer.length,
                InetAddress.getByName(REGISTRY_IP),
                REGISTRY_PORT
            );
            socket.send(packet);
            // System.out.println(node.getName() + " published: " + data + " to topic: " + topic);
        } catch (IOException e) {
            System.err.println("Error publishing message: " + e.getMessage());
        }
    }
}
