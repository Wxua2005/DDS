import node.Node;
import publisher.Publisher;
import subscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        // Create nodes
        Node node1 = new Node("node1");
        Node node2 = new Node("node2");
        Node node3 = new Node("node3");

        // Create publishers and subscribers
        Publisher pub1 = node1.createPublisher("topic1");
        // Subscriber sub2 = node2.createSubscriber("topic1");
        // Subscriber sub3 = node3.createSubscriber("topic1");

        // Give some time for subscribers to start
        // try {
        //     Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }
        Subscriber sub3 = node2.createSubscriber("topic2");
        // Publish messages
        while (true) {
            pub1.publish("Hello World");
            
        }
        // pub1.publish("Second message from node1!");
    }
}