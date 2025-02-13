import node.Node;
import publisher.Publisher;

public class Main {
    public static void main(String[] args) {
        // Create node
        Node node1 = new Node("node1");
        // Create publisher
        Publisher pub1 = node1.createPublisher("topic1");

        // Give some time for discovery
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Publish messages
        int count = 0;
        while (true) {
            pub1.publish("Hello from node1 + [" + count + "]");
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}