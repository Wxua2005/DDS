import node.Node;
import publisher.Publisher;
import subscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        Node node1 = new Node("node1");
        
        Publisher pub1 = node1.createPublisher("topic1", 300);
        
        pub1.startPublishing("Hello from node1");
        
        try {
            Thread.sleep(Long.MAX_VALUE);
        } 
        catch (InterruptedException e) {
            pub1.stopPublishing();
            Thread.currentThread().interrupt();
        }
    }
}