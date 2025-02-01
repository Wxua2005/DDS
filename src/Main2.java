import node.Node;
import publisher.Publisher;
import subscriber.Subscriber;

public class Main2 {
    public static void main(String[] args) {
        Node n2 = new Node("node2");

        Subscriber p2 = n2.create_subscriber("topic1");
    }
}
