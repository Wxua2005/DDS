import node.Node;
import publisher.Publisher;
import subscriber.Subscriber;

public class Main2 {
    public static void main(String[] args) {
        // Create nodes
        Node node3 = new Node("node3");

        Subscriber sub3 = node3.createSubscriber("topic1");
        Publisher pub3 = node3.createPublisher("topic2");
                
        // Subscriber sub3 = node3.createSubscriber("topic1");
        while (true) {
            pub3.publish("pub3 message");
        }
        // Give some time for subscribers to start
        // try {
        //     Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }

        // Publish messages
        // pub1.publish("Second message from node1!");
    }
}