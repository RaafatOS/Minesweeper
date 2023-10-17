import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Server {

    final static int PORT = 2000;
    Matrix m = new Matrix(7, 7);
    ServerSocket serv;
    Socket client;
    private final List<Socket> connectedClients = new ArrayList<>();

    Server(){
        try {
            serv = new ServerSocket(PORT);
            serv.setSoTimeout(1000);
            System.out.println("server started ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void start(int code) {
        try {
            int nbJ = 0;
            while (true) {
                client = serv.accept();
                connectedClients.add(client);   
                nbJ++;
                System.out.println("client connected " + nbJ);
                switch (code) {
                    case 1:
                        getDimNb(connectedClients.get(0));
                        break;
                    case 2:
                        easyG(connectedClients.get(0));
                        break;
                    case 3:
                        mediumG(connectedClients.get(0));
                        break;
                    case 4:
                        hardG(connectedClients.get(0));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDimNb(Socket client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());

                    System.out.println("thread started ...");
                    Thread.sleep(100);
                    out.writeInt(200);
                    System.out.println("thread sending 1...");
                    Thread.sleep(100);
                    int msg = in.readInt();
                    System.out.println("thread reading 2: " + msg);

                    out.writeInt(100);
                    Thread.sleep(100);
                    out.writeInt(m.getDIM());
                    Thread.sleep(100);
                    out.writeInt(200);
                    Thread.sleep(100);
                    out.writeInt(m.computeMinesNumber());
                    out.close();
                    client.close();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void easyG(Socket client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());

                    System.out.println("thread started ...");
                    Thread.sleep(100);
                    out.writeInt(911);
                    Thread.sleep(100);
                    // for (Socket otherClient : connectedClients) {
                    //     out.write(911);
                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void mediumG(Socket client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());

                    System.out.println("thread started ...");
                    Thread.sleep(100);
                    out.writeInt(912);
                    Thread.sleep(100);
                    // for (Socket otherClient : connectedClients) {
                    //     out.write(911);
                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void hardG(Socket client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());

                    System.out.println("thread started ...");
                    Thread.sleep(100);
                    out.writeInt(913);
                    Thread.sleep(100);
                    // for (Socket otherClient : connectedClients) {
                    //     out.write(911);
                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
