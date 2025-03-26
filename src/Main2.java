import node.Node;
import subscriber.Subscriber;

public class Main2 {
    public static void main(String[] args) {
        Node node2 = new Node("node2");

        Subscriber sub2 = node2.createSubscriber("topic1");
        
        System.out.println("Subscriber running. Press Ctrl+C to stop.");
        
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            sub2.stop();
            Thread.currentThread().interrupt();
        }
    }
}