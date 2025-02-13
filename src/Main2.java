import node.Node;
import subscriber.Subscriber;

public class Main2 {
    public static void main(String[] args) {
        // Create node
        Node node2 = new Node("node2");

        // Create subscriber
        Subscriber sub2 = node2.createSubscriber("topic1");

        // Keep the main thread alive to allow the subscriber to receive messages
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}