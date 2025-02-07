package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

import publisher.Publisher;
import subscriber.Subscriber;

public class Node {
    private static List<Node> nodeList = new ArrayList<>();
    private static int IDCount = 0;
    private static Map<String, List<Node>> topicPublishers = new ConcurrentHashMap<>();
    private static Map<String, List<Node>> topicSubscribers = new ConcurrentHashMap<>();

    private int nodeID;
    private int nodePort;
    private String nodeName;
    private DatagramSocket socket;

    public Node(String nodeName) {
        try {
            this.nodeName = nodeName;
            this.nodeID = IDCount++;
            this.socket = new DatagramSocket();
            this.nodePort = socket.getLocalPort();
            nodeList.add(this);
        } catch (SocketException err) {
            System.err.println("Error creating node: " + err.getMessage());
        }
    }

    public Publisher createPublisher(String topic) {
        return new Publisher(this, topic);
    }

    public Subscriber createSubscriber(String topic) {
        return new Subscriber(this, topic);
    }

    public int getPort() {
        return nodePort;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public String getName() {
        return nodeName;
    }

    public static void addPublisher(String topic, Node node) {
        topicPublishers.computeIfAbsent(topic, k -> new ArrayList<>()).add(node);
    }

    public static void addSubscriber(String topic, Node node) {
        topicSubscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(node);
    }

    public static List<Node> getPublishers(String topic) {
        return topicPublishers.getOrDefault(topic, new ArrayList<>());
    }

    public static List<Node> getSubscribers(String topic) {
        return topicSubscribers.getOrDefault(topic, new ArrayList<>());
    }
}