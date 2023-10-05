
import java.net.*;
import java.io.*;

public class Server {
    
    final static int PORT = 2000;
    Matrix m = new Matrix(7,7);
    
    public void start(){
        try{
            ServerSocket serv = new ServerSocket(PORT);
            System.out.println("server started ...");
            int nbJ = 0;
            while(true){
                Socket client = serv.accept();
                nbJ++;
                System.out.println("client connected "+ nbJ);
                Thread cThread = new Thread(new ClientHandler(client));
                cThread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable{
        private final Socket client;

        public ClientHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run(){
            try{
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                
                System.out.println("thread started ...");
                Thread.sleep(100);
                out.writeInt(200);
                System.out.println("thread sending 1...");
                Thread.sleep(100);
                int msg = in.readInt();
                System.out.println("thread reading 2: "+ msg);
                // authentication complete game time

                /* there will be the standard game loop here
                 * 100 = send dim
                 * 200 = send nb mines
                 */
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
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new Server().start();
    }
}
