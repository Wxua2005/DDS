package publisher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;
import node.Node;

public class Publisher {
    private final Node node;
    private final String topic;
    private final MulticastSocket multicastSocket;
    private final InetAddress multicastGroup;
    private final int multicastPort;
    private int publishingRateMs = 1000; 
    private Thread publishingThread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private String dataToPublish = "";

    public Publisher(Node node, String topic) {
        this.node = node;
        this.topic = topic;
        
        String[] addressInfo = Node.getMulticastAddressForTopic(topic);
        
        try {
            this.multicastSocket = new MulticastSocket();
            this.multicastGroup = InetAddress.getByName(addressInfo[0]);
            this.multicastPort = Integer.parseInt(addressInfo[1]);
            
            multicastSocket.setTimeToLive(1);
            
            System.out.println(node.getName() + " created publisher for topic: " + topic +
                   " using multicast group: " + multicastGroup.getHostAddress() + ":" + multicastPort);
        } catch (IOException e) {
            System.err.println("Error creating multicast publisher: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Publisher(Node node, String topic, int publishingRateMs) {
        this(node, topic);
        this.publishingRateMs = publishingRateMs;
    }

    public void setPublishingRate(int rateMs) {
        this.publishingRateMs = rateMs;
        System.out.println("Publishing rate set to " + rateMs + " ms");
    }

    public void startPublishing(String initialData) {
        if (running.get()) {
            System.out.println("Publisher is already running");
            return;
        }
        
        this.dataToPublish = initialData;
        running.set(true);
        
        publishingThread = new Thread(() -> {
            int count = 0;
            while (running.get()) {
                String message = dataToPublish + " [" + count++ + "]";
                publish(message);
                
                try {
                    Thread.sleep(publishingRateMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        publishingThread.start();
        System.out.println("Started publishing to " + topic + " at rate: " + publishingRateMs + " ms");
    }
    
    public void stopPublishing() {
        running.set(false);
        if (publishingThread != null) {
            publishingThread.interrupt();
            try {
                publishingThread.join(2000); 
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Stopped publishing to " + topic);
        }
    }

    public void publish(String data) {
        try {
            String message = topic + ":" + data;
            byte[] buffer = message.getBytes();
            
            DatagramPacket packet = new DatagramPacket(
                buffer, 
                buffer.length,
                multicastGroup, 
                multicastPort
            );
            
            multicastSocket.send(packet);
        } 
        catch (IOException e) {
            System.err.println("Error publishing message: " + e.getMessage());
        }
    }
    
    public void close() {
        if (running.get()) {
            stopPublishing();
        }
        multicastSocket.close();
    }
}
