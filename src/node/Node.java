package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import publisher.Publisher;
import subscriber.Subscriber;

public class Node {
    private final String nodeName;
    private final DatagramSocket socket;
    private static final Map<String, String[]> topicAddressMap = new ConcurrentHashMap<>();
    private static final String MULTICAST_BASE_ADDRESS = "224.0.0.";
    private static final int MULTICAST_BASE_PORT = 6000;
    private static int topicCounter = 1;

    public Node(String nodeName) {
        try {
            this.nodeName = nodeName;
            this.socket = new DatagramSocket();
            System.out.println("Node " + nodeName + " started on port " + socket.getLocalPort());
        } 
        catch (SocketException err) {
            System.err.println("Error creating node: " + err.getMessage());
            throw new RuntimeException(err);
        }
    }

    public Publisher createPublisher(String topic) {
        return new Publisher(this, topic);
    }

    public Publisher createPublisher(String topic, int publishingRateMs) {
        return new Publisher(this, topic, publishingRateMs);
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

    public static String[] getMulticastAddressForTopic(String topic) {
        if (topicAddressMap.containsKey(topic)) {
            return topicAddressMap.get(topic);
        }
        
        synchronized (topicAddressMap) {

            if (!topicAddressMap.containsKey(topic)) {
                int lastOctet = topicCounter++ % 255;
                if (lastOctet == 0) lastOctet = 1; 
                String ipAddress = MULTICAST_BASE_ADDRESS + lastOctet;
                int port = MULTICAST_BASE_PORT + (topicCounter % 1000);
                
                topicAddressMap.put(topic, new String[] {ipAddress, String.valueOf(port)});
                System.out.println("Allocated multicast address " + ipAddress + ":" + port + " for topic " + topic);
            }
            return topicAddressMap.get(topic);

        }
    }
}