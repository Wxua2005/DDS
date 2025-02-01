package subscriber;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import node.Node;
import publisher.Publisher;

public class Subscriber {
    public static Map<String, ArrayList<Node>> topicSubscribers = new HashMap<>();
    public int publisherPort;
    public DatagramSocket clientSocket = null;
    public String topic;

    public Subscriber(Node node, String topic) {
        // Search in topicPublisher for a node
        // Which publishes to the topic this
        // Node is subscribing from and get its
        // port no and make a UDP connection and
        // recevie the data
    try {
            this.topic = topic;
            if (topicSubscribers.size() != 0 || topicSubscribers.containsKey(topic) == false) {
                topicSubscribers.put(topic, new ArrayList<Node>(){});
                topicSubscribers.get(topic).add(node);
            }
            else {
                topicSubscribers.get(topic).add(node);
            }
            this.clientSocket = node.clientSocket;
            this.Subscribe();
        }
        catch (Exception err) {
            System.out.println(err);
            }
        }
    void Subscribe() {
        try {
            this.publisherPort = Publisher.topicPublishers.get(this.topic).get(0).nodePort;
            byte[] buffer = new byte[1024];
            DatagramPacket DGP = new DatagramPacket(buffer, buffer.length);
            while (true) {
                this.clientSocket.receive(DGP);
                String received = new String(DGP.getData(), 0, DGP.getLength(), StandardCharsets.UTF_8);
                System.out.println(received);
            } 
        }
        catch (UnknownHostException err) {
            System.out.println(err);
            }
        catch (IOException err) {
            System.out.println(err);
            }
        }
    }

