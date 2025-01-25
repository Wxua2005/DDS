import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

class Server {
    public DatagramSocket DGS = null;
    public int port;
    Server(int port) {
        try {
        DGS = new DatagramSocket(port);
        this.port = port;
        }
        catch (SocketException err) {
            System.out.println(err.getMessage());
        }
    }

    void receiveData() {
        try {
        byte[] buffer = new byte[1024];
        DatagramPacket DGP = new DatagramPacket(buffer, buffer.length);
        while (true) {
            DGS.receive(DGP);
            String received = new String(DGP.getData(), 0, DGP.getLength(), StandardCharsets.UTF_8);
            System.out.println(received);
            if (received.equals("End")) {
                break;
            }
        } 
        }
        catch (UnknownHostException err) {
            System.out.println(err.getMessage());
        }
        catch (IOException err) {
            System.out.println(err.getMessage());
        }
    }
}

class NewServer {
    public static void main(String[] args) {
        Server Serverclass = new Server(5000);
        Serverclass.receiveData();
        Serverclass.DGS.close();
    }
}