import node.Node;
import publisher.Publisher;
import subscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        Node n1 = new Node("node1");
        Publisher p1 = n1.create_publisher("topic1");
        p1.publish("hi");
    }
}
