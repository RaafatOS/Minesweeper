
import java.net.*;
import java.io.*;

public class client {
    final static int PORT = 2000;

    client() {
        try {
            System.out.println("client started ...");
            Socket server = new Socket("localhost", PORT);
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            System.out.println("client sendind ...");
            out.writeInt(200);
            System.out.println("client reading ...");

            Thread sThread = new Thread(new ServerHandler(server));
            sThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ServerHandler implements Runnable {

        private final Socket server;

        public ServerHandler(Socket server) {
            this.server = server;
        }

        @Override
        public void run() {
            try {
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                int msg = in.readInt();
                System.out.println("server in thread 1 says: " + msg);
                Thread.sleep(100);
                out.writeInt(5);
                System.out.println("client sending ... ");
                while (true) {
                    if (in.available() == 0)
                        continue;
                    msg = in.readInt();
                    switch (msg) {
                        case 100:
                            msg = in.readInt();
                            System.out.println("server in thread 2 says: the dim = " + msg);
                            break;
                        case 200:
                            msg = in.readInt();
                            System.out.println("server in thread 2 says: the nb mines = " + msg);
                            break;
                        default:
                            System.out.println("server in thread 2 says: " + msg);
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new client();
    }
}
