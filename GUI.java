import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.net.*;
import java.io.*;

import javax.swing.*;

//public boolean visible = false;

public class GUI extends JPanel implements ActionListener{
    
    JPanel head = new JPanel(new GridLayout(1,2));
    JPanel grid;
    JPanel startPanel;
    JMenuBar menu = new JMenuBar();
    JMenu headMenu = new JMenu("Menu");
    JMenuItem server = new JMenuItem("Server");
    JMenu diff = new JMenu("Difficulty");
    JButton newGame = new JButton("New Game");
    JButton exit = new JButton("Exit");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");
    boolean visible = false;
    Main main;
    Matrix m;
    final static int PORT = 2000;
    static String text ="";
    int nbOuvert = 1;
    int nbMine;
    Case[][] cases;
    

    //how to fill the grid with blanck cases
    
    public void FillWindow(GUI gui) {
            for(int i = 0; i < gui.m.getDIM(); i++){
                for(int j = 0; j < gui.m.getDIM(); j++){
                    if(m.getCase(i, j)){
                        grid.add(new JLabel(" X"));
                    }else{  
                        grid.add(new JLabel(" "+ Integer.toString(m.computeMinesAround(i, j)))); 
                    }
                    grid.add(new JLabel("") );
                }
            } 
        add(grid, BorderLayout.CENTER);
        // grid.setVisible(visible);
    }

    // server handler
    // private static class ServerHandler implements Runnable {

    //     private final Socket server;

    //     public ServerHandler(Socket server){
    //         this.server = server;
    //     }
    //     @Override
    //     public void run() {
    //         try {
    //             DataInputStream in = new DataInputStream(server.getInputStream());
    //             DataOutputStream out = new DataOutputStream(server.getOutputStream());
    //             int msg = in.readInt();
    //             System.out.println("server in thread 1 says: " + msg);
    //             Thread.sleep(100);
    //             out.writeInt(5);
    //             System.out.println("client sending ... ");
    //             while(true){
    //                 msg = in.readInt();
    //                 switch(msg){
    //                     case 100:
    //                         msg = in.readInt();
    //                         System.out.println("server in thread 2 says: the dim = " + msg);
    //                         break;
    //                     case 200:
    //                         msg = in.readInt();
    //                         System.out.println("server in thread 2 says: the nb mines = " + msg);
    //                         break;
    //                     default:
    //                         System.out.println("server in thread 2 says: " + msg);
    //                         break;
    //                 }
    //             }
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
        
    //     }


    class Case extends JPanel implements MouseListener{ 
        private final static int DIM=50 ; 
        private int x, y;
        static int previousVal = 0;
        
        private String txt = "";
        public int state = 0; // 0: hidden, 1: flagged, 2: revealed
        public Case (int x, int y) { 
            this.x = x;
            this.y = y;
            setPreferredSize(new Dimension(DIM, DIM)); // taille de la case 
            addMouseListener(this); // ajout listener souris 
        }
        public String getTxt() {
            return txt;
        }
        public void setTxt(String txt) {
            this.txt = txt;
        }

        public void openCase(int x, int y){
            cases[x][y].setBackground(Color.white);
            cases[x][y].state = 2;
            int numCase;
            numCase = m.computeMinesAround(x , y);
            previousVal = numCase;
            cases[x][y].setTxt(Integer.toString(numCase));
            nbOuvert++;
            cases[x][y].repaint();
        }
        
    public void propagate(int x, int y){
        if(x < 0 || x >= m.getDIM() || y < 0 || y >= m.getDIM() || m.getCase(x, y) || cases[x][y].state == 2){
            return;
        }
        // how to open the cases around the one clicked
        openCase(x, y);
        if(m.computeMinesAround(x, y)>0){
            return;
        }
        propagate(x-1, y);
        propagate(x+1, y);
        propagate(x, y-1);
        propagate(x, y+1);
        propagate(x-1, y-1);
        propagate(x+1, y+1);
        propagate(x-1, y+1);
        propagate(x+1, y-1);
    }
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()== MouseEvent.BUTTON1){
                if(m.getCase(x, y)){
                    e.getComponent().setBackground(Color.RED);
                    txt = "X";
                    JOptionPane.showMessageDialog(this, "You lost!");

                }else{
                    //e.getComponent().setBackground(Color.white);
                    int numCase;
                    numCase = m.computeMinesAround(x , y);
                    if(nbOuvert == m.getDIM()*m.getDIM() - nbMine){
                        JOptionPane.showMessageDialog(this, "You won!");
                    }
                    if(numCase == 0){
                        propagate(x, y);
                    }else{
                        openCase(x, y);
                    }
                }
                repaint();
            }else{
                txt = "";
                e.getComponent().setBackground(Color.YELLOW);
            }
            
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = DIM/2 - getFontMetrics(getFont()).stringWidth(getTxt())/2;
            int y = DIM/2 ;
            g.setColor(Color.BLACK);
            g.drawString(getTxt(), x, y);
            //g.fillRect(5, 5, DIM, DIM);
            //repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
        }
        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
        }
        
    }

    private void changeForm(int dim, int nb){
        text = "";
        Matrix nwMatrix = new Matrix(nb, dim);
        GUI.this.m = nwMatrix;
        GUI.this.m.placeNumbersDisplay();
        GUI.this.startPanel.removeAll();
        GUI.this.startPanel.setLayout(new GridLayout(GUI.this.m.getDIM(), GUI.this.m.getDIM()));
            
        initializeLabels(GUI.this);
            //FillWindow(GUI.this);
        main.pack();
    }

    private void initializeLabels(GUI gui) {
        cases = new Case[gui.m.getDIM()][gui.m.getDIM()];
        for (int i = 0; i < gui.m.getDIM(); i++) {
            for (int j = 0; j < gui.m.getDIM(); j++) {//gui.m.getDIM()
                cases[i][j] = new Case(i,j);
                cases[i][j].setOpaque(true);
                cases[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cases[i][j].setBackground(Color.LIGHT_GRAY);
                startPanel.add(cases[i][j]);
            }
        }
        add(startPanel, BorderLayout.CENTER);
    }

    GUI(Matrix mat, Main main){
        this.main = main;  
        m = mat;
        grid = new JPanel(new GridLayout(m.getDIM(),m.getDIM()));
        startPanel = new JPanel(new GridLayout(m.getDIM(),m.getDIM()));
        //startPanel = grid;
        this.setLayout(new BorderLayout());

        head.add(newGame);
        head.add(exit);

        diff.add(easy);
        diff.add(medium);
        diff.add(hard);
        
        menu.add(diff);
        headMenu.add(server);
        server.addActionListener(this);
        menu.add(headMenu);
        head.add(menu);

        exit.addActionListener(this) ;
        exit.setToolTipText("the end");
        newGame.addActionListener(this) ;
        easy.addActionListener(this);
        medium.addActionListener(this);
        hard.addActionListener(this);
        add(head, BorderLayout.NORTH);

        nbMine = m.computeMinesNumber();

        //FillWindow(this);
        initializeLabels(this);
        m.placeNumbersDisplay();
        main.pack();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exit){
            System.exit(0);

        }
        if(e.getSource() == newGame){
            text = "";
            GUI.this.m.changeMatrix();
            GUI.this.startPanel.removeAll();
            
            initializeLabels(GUI.this);
            main.pack();
        }
        if(e.getSource() == hard){
            changeForm(9, 12);
        }
        if(e.getSource() == easy){
            changeForm(4, 1);
        }
        if(e.getSource() == medium){
            changeForm(7, 7);

        }

        // if(e.getSource() == server){
        //     try{
        //         System.out.println("client started ...");
        //         Socket server = new Socket("localhost", PORT);
        //         DataInputStream in = new DataInputStream(server.getInputStream());
        //         DataOutputStream out = new DataOutputStream(server.getOutputStream());
        //         System.out.println("client sendind ...");
        //         out.writeInt(200);
        //         System.out.println("client reading ...");

        //         Thread sThread = new Thread(new ServerHandler(server));
        //         sThread.start();
        //     }
        //     catch(Exception ex){ex.printStackTrace();}
        // }
    }
}