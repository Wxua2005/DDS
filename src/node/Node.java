package node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import publisher.Publisher;
import subscriber.Subscriber;

// Node contains 2 DatagramSockets in the following Classes
//                       |
//                 -------------
//                 |           |
//              Publisher  Subscriber
// 
// Subscriber socket is member variable of node class while 
// Publisher socket is only created when create_publisher()
// method is called 

public class Node {
    static ArrayList<Node> nodeList = new ArrayList<Node>();
    static int IDCount = 0;
    static Map<String, ArrayList<Node>> topicSubscribers = new HashMap<>();

    public int nodeID;
    public int nodePort;
    public String nodeName = null; 
    public int publisherCount = 0;
    public int subscriberCount = 0;
    public DatagramSocket clientSocket; // Node for receiving data

    public Node(String nodeName) {
        try {
            this.nodeName = nodeName;
            this.nodeID = IDCount++;
            this.clientSocket = new DatagramSocket();
            this.nodePort = clientSocket.getLocalPort();
        } catch (SocketException err) {
            System.out.println(err);
        }
    }

    public Publisher create_publisher(String topic) {
            // For publishing data, the DatagramSocket of the publisher is used as the node
            Publisher publisher = new Publisher(this, topic);
            return publisher;
    }

    public Subscriber create_subscriber(String topic) {
        Subscriber subscriber = new Subscriber(this, topic);
        return subscriber;
    }
}
