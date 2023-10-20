import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import java.io.*;

public class Server {

    final static int PORT = 2000;
    Matrix m = new Matrix(7, 7);
    ServerSocket serv;
    Socket client;
    serverGui gui;
    private List<Socket> connectedClients = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    int nbJ;

    Server(serverGui gui) {
        this.gui = gui;
        try {
            serv = new ServerSocket(PORT);
            System.out.println("server started ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void start(int code) {
        try {
            nbJ = 0;
            while (true) {
                client = serv.accept();
                scores.add(0);
                connectedClients.add(client);
                nbJ++;
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                DataInputStream in = new DataInputStream(client.getInputStream());
                out.writeInt(420);
                String msg = in.readUTF();
                names.add(msg);
                gui.setScores();
                System.out.println("client connected " + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void caseOpening() {
        new Thread(() -> {
            try {
                if (connectedClients.size() == 0)
                    caseOpening();
                else {
                    while (true) {
                        for(int i=0; i<connectedClients.size(); i++){
                            
                            DataInputStream in = new DataInputStream(connectedClients.get(i).getInputStream());
                            if (in.available() == 0)
                                continue;
                            else {
                                switch(in.readInt()){
                                    case 2000:
                                        int x = in.readInt();
                                        int y = in.readInt();
                                        int score = in.readInt();
                                        scores.set(i, scores.get(i) + score);
                                        gui.setScores();

                                        // send code with x and y to all clients
                                        for (int j = 0; j < connectedClients.size(); j++) {
                                            DataOutputStream out2 = new DataOutputStream(
                                                    connectedClients.get(j).getOutputStream());
                                            out2.writeInt(202);// code that the message is received
                                            out2.writeInt(x);
                                            out2.writeInt(y);
                                        }
                                        break;
                                    case 1000:// client disconnected
                                        connectedClients.get(i).close();
                                        connectedClients.remove(i);
                                        scores.remove(i);
                                        nbJ--;
                                        gui.setScores();
                                        break;
                                }
                                
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public List<Integer> getScores(){
        return scores;
    }

    public List<String> getNames(){
        return names;
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
