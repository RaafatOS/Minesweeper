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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDimNb(Socket client) {
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

    public void easyG() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < connectedClients.size(); i++) {
                        DataInputStream in = new DataInputStream(connectedClients.get(i).getInputStream());
                        DataOutputStream out = new DataOutputStream(connectedClients.get(i).getOutputStream());

                        System.out.println("thread started ...");
                        Thread.sleep(100);
                        out.writeInt(911);//easy
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void mediumG() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < connectedClients.size(); i++) {
                        DataInputStream in = new DataInputStream(connectedClients.get(i).getInputStream());
                        DataOutputStream out = new DataOutputStream(connectedClients.get(i).getOutputStream());

                        System.out.println("thread started ...");
                        Thread.sleep(100);
                        out.writeInt(912);//medium
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void hardG() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < connectedClients.size(); i++) {
                        DataInputStream in = new DataInputStream(connectedClients.get(i).getInputStream());
                        DataOutputStream out = new DataOutputStream(connectedClients.get(i).getOutputStream());

                        System.out.println("thread started ...");
                        Thread.sleep(100);
                        out.writeInt(913);//hard
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
