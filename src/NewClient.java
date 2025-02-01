import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class Sender {
    public DatagramSocket DGC = null;
    public int port;
    Sender(int port) {
        try {
        DGC = new DatagramSocket();
        System.out.println(DGC.getLocalPort());
        this.port = port;
        }
        catch (SocketException err) {
            System.out.println(err);
        }
    }

    void sendData(String data, int port) {
        try {
        byte[] buffer = data.getBytes();
        DatagramPacket DGP = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port);
        DGC.send(DGP);
        }
        catch (UnknownHostException err) {
            System.out.println(err);
        }
        catch (IOException err) {
            System.out.println(err);
        }
    }
}

class NewClient {
    public static void main(String[] args) {
        Sender sender = new Sender(5001);
        Scanner scanner = new Scanner(System.in);
        String data;
        while (true) {
            data = scanner.nextLine();
            sender.sendData(data, 5000);
            if (data.equals("End")) {
                break;
            }
        }
        sender.DGC.close();
        scanner.close();
    }
}