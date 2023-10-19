import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

import javax.swing.*;

//public boolean visible = false;

public class GUI extends JPanel implements ActionListener {

    JPanel head = new JPanel(new GridLayout(1, 2));
    JPanel grid;
    JPanel startPanel;
    JMenuBar menu = new JMenuBar();
    JMenu headMenu = new JMenu("Menu");
    JMenuItem server = new JMenuItem("create to Server");
    JMenu diff = new JMenu("Difficulty");
    JButton newGame = new JButton("New Game");
    JMenuItem addPlayer = new JMenuItem("Join server");
    JMenuItem disconnect = new JMenuItem("Disconnect from server");
    JButton exit = new JButton("Exit");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");
    JLabel timerLabel = new JLabel();
    boolean visible = false;
    JFrame main;
    Matrix m;
    final static int PORT = 2000;
    private final static String FILE_PATH = "C:\\Users\\raafat\\Desktop\\ISMIN CS\\java avance\\src\\diff.txt";
    static String text = "";
    private int nbOuvert = 0;
    int nbMine;
    Case[][] cases;
    String content;
    private Timer timer;
    private int seconds;
    private HashMap<String, Integer> scores = new HashMap<String, Integer>();
    Socket serv ;
    private DataInputStream in;
    private DataOutputStream out;
    boolean isConnect = false;
    boolean propagate = true;

    // file reader and writer
    public static String readFile(String path) {
        String content = "";
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                content += sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String path, String content) {
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // getters and setters for nbOuvert
    public int getNbOuvert() {
        return nbOuvert;
    }

    public void resetNbOuvert() {
        nbOuvert = 0;
    }

    public void incrementNbOuvert() {
        nbOuvert++;
    }

    // timer

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                seconds++;
                updateTimerDisplay();
            }
        });
        timer.start();
    }

    private void updateTimerDisplay() {
        timerLabel.setText(
                "Time: " + String.format("%02d", (seconds / 60)) + ":" + String.format("%02d", (seconds % 60)));
    }

    private void stopTimer() {
        timer.stop();
    }

    private void resetTimer() {
        seconds = 0;
        updateTimerDisplay();
    }

    public void resetCounter() {
        nbOuvert = 1;
        nbMine = m.computeMinesNumber();
    }

    // score handling
    public void addScore(String name, int score) {
        scores.put(name, score);
    }

    public void displayScores() {
        for (String name : scores.keySet()) {
            System.out.println(name + " : " + scores.get(name));
        }
    }
    
    // case class with functions

    public class Case extends JPanel implements MouseListener {
        private final static int DIM = 50;
        private int x, y;
        static int previousVal = 0;

        private String txt = "";
        public int state = 0; // 0: hidden, 1: flagged, 2: revealed

        public Case(int x, int y) {
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

        public void openCase(int x, int y) {
            if(!m.getCase(x, y)){
                cases[x][y].setBackground(Color.white);
                cases[x][y].state = 2;
                int numCase;
                numCase = m.computeMinesAround(x, y);
                previousVal = numCase;
                cases[x][y].setTxt(Integer.toString(numCase));
                incrementNbOuvert();
                if(isConnect) sendCaseOpened(x, y, m.computeMinesAround(x, y));
                cases[x][y].repaint();
            }else {
                cases[x][y].setBackground(Color.RED);
                cases[x][y].setTxt("X");
                cases[x][y].repaint();
                cases[x][y].state = 2;
            }
        }

        public void propagate(int x, int y) {
            if (x < 0 || x >= m.getDIM() || y < 0 || y >= m.getDIM() || m.getCase(x, y) || cases[x][y].state == 2) {
                return;
            }
            // how to open the cases around the one clicked
            openCase(x,y);
            if (m.computeMinesAround(x, y) > 0) {
                return;
            }
            propagate(x - 1, y);
            propagate(x + 1, y);
            propagate(x, y - 1);
            propagate(x, y + 1);
            propagate(x - 1, y - 1);
            propagate(x + 1, y + 1);
            propagate(x - 1, y + 1);
            propagate(x + 1, y - 1);
        }

        public void checkWin() {
            if (getNbOuvert() == m.getDIM() * m.getDIM() - nbMine) {
                addScore(content, 100);
                displayScores();
                JOptionPane.showMessageDialog(this, "You won!");
                stopTimer();
                resetTimer();
                changeForm(m.getDIM(), m.minesNumber);
                // player.playPanel.removeAll();
                // player.initializePlayer();
            } 
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int numCase;
                numCase = m.computeMinesAround(x, y);

                if (m.getCase(x, y)) {
                    if (cases[x][y].state == 0) {
                        int score = (100 * (nbOuvert)) / (m.getDIM() * m.getDIM() - nbMine);
                        addScore(content, score);
                        displayScores();
                        cases[x][y].setBackground(Color.RED);
                        cases[x][y].setTxt("X");
                        if(!isConnect){
                            JOptionPane.showMessageDialog(this, "You lost!");
                            stopTimer();
                            resetTimer();
                            changeForm(m.getDIM(), m.minesNumber);
                        } else {
                            sendCaseOpened(x, y, -5);
                        }
                    }
                } else {
                    if (cases[x][y].state == 0) {
                        if (numCase == 0) {
                            if(propagate)propagate(x, y);
                            else openCase(x,y);
                            checkWin();
                        } else {
                            openCase(x,y);
                            checkWin();
                        }
                    }
                }
                repaint();
            } else {
                txt = "";
                if (cases[x][y].state == 0) {
                    cases[x][y].state = 1;
                    cases[x][y].setBackground(Color.YELLOW);
                } else {
                    cases[x][y].state = 0;
                    cases[x][y].setBackground(Color.LIGHT_GRAY);
                }
            }

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = DIM / 2 - getFontMetrics(getFont()).stringWidth(getTxt()) / 2;
            int y = DIM / 2;
            g.setColor(Color.BLACK);
            g.drawString(getTxt(), x, y);
            // g.fillRect(5, 5, DIM, DIM);
            // repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }

    }

    // server functions
    public void clientHandler() {
        try {
            System.out.println("client started ...");
            serv = new Socket("localhost", PORT);
            in = new DataInputStream(serv.getInputStream());
            out = new DataOutputStream(serv.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // change difficulty
    public synchronized void sendCaseOpened(int x, int y, int sc) {
        new Thread(() -> {
            try {
                DataOutputStream outCase = new DataOutputStream(serv.getOutputStream());
                outCase.writeInt(2000);// code to opening case
                outCase.writeInt(x);
                outCase.writeInt(y);
                outCase.writeInt(sc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void changeDifficulty() {
        new Thread(() -> {
            try {
                int msg;
                DataInputStream inDiff = new DataInputStream(serv.getInputStream());
    
                while (true) {
                    msg = inDiff.readInt();
    
                    switch (msg) {
                        case 911:// easy
                            changeForm(4, 2);
                            break;
                        case 912:// medium
                            changeForm(7, 7);
                            break;
                        case 913:// hard
                            changeForm(9, 18);
                            break;
                        case 202:
                            int x = inDiff.readInt();
                            int y = inDiff.readInt();
                            if(this.cases[x][y].state != 2)this.cases[x][y].openCase(x, y);
                            break;
                        default:
                            System.out.println("Server in thread 2 says: " + msg);
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    
    // functions
    // 1) create a new matrix

    private void changeForm(int dim, int nb) {
        GUI.this.stopTimer();
        GUI.this.resetTimer();
        text = "";
        Matrix nwMatrix = new Matrix(nb, dim);
        GUI.this.m = nwMatrix;
        GUI.this.m.placeNumbersDisplay();
        GUI.this.startPanel.removeAll();
        GUI.this.startPanel.setLayout(new GridLayout(GUI.this.m.getDIM(), GUI.this.m.getDIM()));
        initializeLabels(GUI.this);
        main.pack();
    }

    // 2) fill the window with the matrix as cases
    private void initializeLabels(GUI gui) {
        cases = new Case[gui.m.getDIM()][gui.m.getDIM()];
        for (int i = 0; i < gui.m.getDIM(); i++) {
            for (int j = 0; j < gui.m.getDIM(); j++) {// gui.m.getDIM()
                cases[i][j] = new Case(i, j);
                cases[i][j].setOpaque(true);
                cases[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cases[i][j].setBackground(Color.LIGHT_GRAY);
                startPanel.add(cases[i][j]);
            }
        }
        add(startPanel, BorderLayout.CENTER);

        GUI.this.resetCounter();
        GUI.this.resetNbOuvert();
        startTimer();
    }
    // GUI creation
    /**
     * @param mat
     * @param main
     */
    GUI(Matrix mat, JFrame main) {
        this.main = main;
        // reading latest diff and applying it to the game
        Matrix init;
        content = readFile(FILE_PATH);
        System.out.println(content);
        switch (content) {
            case "easy":
                init = new Matrix(2, 4);
                break;
            case "medium":
                init = new Matrix(7, 7);
                break;
            case "hard":
                init = new Matrix(18, 9);
                break;
            default:
                init = mat;
                break;
        }
        m = init;
        grid = new JPanel(new GridLayout(m.getDIM(), m.getDIM()));
        startPanel = new JPanel(new GridLayout(m.getDIM(), m.getDIM()));
        this.setLayout(new BorderLayout());

        head.add(newGame);
        headMenu.add(addPlayer);
        head.add(exit);

        diff.add(easy);
        diff.add(medium);
        diff.add(hard);

        menu.add(diff);
        headMenu.add(server);
        disconnect.setEnabled(false);
        headMenu.add(disconnect);
        server.addActionListener(this);
        menu.add(headMenu);
        head.add(menu);
        head.add(timerLabel);

        exit.addActionListener(this);
        exit.setToolTipText("the end");
        newGame.addActionListener(this);
        addPlayer.setEnabled(true);
        addPlayer.addActionListener(this);
        disconnect.addActionListener(this);
        easy.addActionListener(this);
        medium.addActionListener(this);
        hard.addActionListener(this);
        add(head, BorderLayout.NORTH);

        nbMine = m.computeMinesNumber();

        initializeLabels(this);
        m.placeNumbersDisplay();
        main.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {
            System.exit(0);

        }
        if (e.getSource() == newGame) {
            text = "";
            GUI.this.m.changeMatrix();
            GUI.this.startPanel.removeAll();
            GUI.this.stopTimer();
            GUI.this.resetTimer();
            initializeLabels(GUI.this);
            main.pack();
        }
        if (e.getSource() == hard) {
            changeForm(9, 18);
            writeFile(FILE_PATH, "hard");
            content = "hard";
        }
        if (e.getSource() == easy) {
            changeForm(4, 2);
            writeFile(FILE_PATH, "easy");
            content = "easy";
        }
        if (e.getSource() == medium) {
            changeForm(7, 7);
            writeFile(FILE_PATH, "medium");
            content = "medium";
        }

        if (e.getSource() == server) {
            // new Server().start();
            ((Main) main).createServer();
            server.setEnabled(false);
            addPlayer.setEnabled(true);
            //new client();
        }

        if (e.getSource() == addPlayer) {
            String name = JOptionPane.showInputDialog(this, "Enter your name");
            if (name != null) {
                // create a new player in a new window
                isConnect = true;
                propagate = false;
                main.setTitle(name);
                clientHandler();
                changeDifficulty();
                addPlayer.setEnabled(false);
                disconnect.setEnabled(true);
            } 
        }

        if (e.getSource() == disconnect) {
            try {
                isConnect = false;
                propagate = true;
                out.writeInt(1000);
                out.close();
                in.close();
                serv.close();
                addPlayer.setEnabled(true);
                disconnect.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
