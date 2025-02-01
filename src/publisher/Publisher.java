package publisher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import node.Node;
import subscriber.Subscriber;

public class Publisher {
    public DatagramSocket pubDGS = null;
    public int subscriberPort;
    public static Map<String, ArrayList<Node>> topicPublishers = new HashMap<>();
    public String topic;

    public Publisher(Node node, String topic) {
        try {
            this.topic = topic;
            pubDGS = new DatagramSocket();
            if (topicPublishers.size() != 0 || topicPublishers.containsKey(topic) == false) {
                topicPublishers.put(topic, new ArrayList<Node>(){});
                topicPublishers.get(topic).add(node);
                return;
            }
            else {
                topicPublishers.get(topic).add(node);
            }
        }
        catch (SocketException err) {
            System.out.println(err);
        }
    }

    public void publish(String data) {
        try {
            this.subscriberPort = Subscriber.topicSubscribers.get(this.topic).get(0).nodePort;
            byte[] buffer = data.getBytes();
            DatagramPacket DGP = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), this.subscriberPort);
            pubDGS.send(DGP);
            System.out.println("Sent data: " + data);
        }
        catch (UnknownHostException err) {
            System.out.println(err);
        }
        catch (IOException err) {
            System.out.println(err);
        }
    }
}

