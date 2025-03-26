package subscriber;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import node.Node;

public class Subscriber implements Runnable {
    private final Node node;
    private final String topic;
    private final MulticastSocket multicastSocket;
    private final InetAddress multicastGroup;
    private final int multicastPort;
    private volatile boolean running;
    private Thread receiveThread;

    public Subscriber(Node node, String topic) {
        this.node = node;
        this.topic = topic;
        
        String[] addressInfo = Node.getMulticastAddressForTopic(topic);
        
        try {
            this.multicastSocket = new MulticastSocket(Integer.parseInt(addressInfo[1]));
            this.multicastGroup = InetAddress.getByName(addressInfo[0]);
            this.multicastPort = Integer.parseInt(addressInfo[1]);
            
            multicastSocket.joinGroup(multicastGroup);
            
            this.running = true;
            System.out.println(node.getName() + " subscribed to topic: " + topic +
                   " using multicast group: " + multicastGroup.getHostAddress() + ":" + multicastPort);
            
            startReceiving();
        } 
        catch (IOException e) {
            System.err.println("Error creating multicast subscriber: " + e.getMessage());
            throw new RuntimeException(e);
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
                multicastSocket.receive(packet);
                String received = new String(
                    packet.getData(),
                    0,
                    packet.getLength(),
                    StandardCharsets.UTF_8
                );
                
                if (received.startsWith(topic + ":")) {
                    String message = received.substring(topic.length() + 1);
                    System.out.println(node.getName() + " received: " + message);
                }
            } 
            catch (IOException e) {
                if (running) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            }
        }
    }

    public void stop() {
        running = false;
        try {
            multicastSocket.leaveGroup(multicastGroup);
        } catch (IOException e) {
            System.err.println("Error leaving multicast group: " + e.getMessage());
        }
        multicastSocket.close();
    }
}
