import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class serverGui extends JFrame{
    Server serv;
    private List<Integer> scores = new ArrayList<>();

    JButton newP = new JButton("new Player");
    JButton stop = new JButton("Stop");
    JButton start = new JButton("Start");
    JMenuBar menu = new JMenuBar();
    JMenu diff = new JMenu("Difficulty");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");
    JPanel panel = new JPanel(new GridLayout(4,1));
    JPanel scorePanel = new JPanel(new GridLayout(4,1));
    public serverGui(){

        diff.add(easy);
        diff.add(medium);
        diff.add(hard);
        serv = new Server(this);
        new Thread(() -> serv.start(1)).start();
        start.addActionListener(e -> {
            new Thread(() -> serv.hardG()).start();
        });
        newP.addActionListener(e -> {
            new Main();
        });

        stop.addActionListener(e -> {
            System.exit(0);
        });

        easy.addActionListener(e -> {
            // make easy all players
            new Thread(() -> serv.easyG()).start();
        });

        medium.addActionListener(e -> {
            // make medium all players
            new Thread(() -> serv.mediumG()).start();
        });

        hard.addActionListener(e -> {
            // make hard all players
            new Thread(() -> serv.hardG()).start();
        });

        serv.caseOpening();
        panel.add(start);
        panel.add(newP);
        panel.add(stop);
        scorePanel.setVisible(true);
        panel.add(scorePanel);
        menu.add(diff);
        this.setJMenuBar(menu);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 200);
        this.setVisible(true);
        //new Server().start();
    }

    public Server getServer(){
        return serv;
    }

    public void setScores(){
        scores = serv.getScores();
        List<String> namList = serv.getNames();
        SwingUtilities.invokeLater(() -> {
            scorePanel.removeAll();
            for(int i=0; i<scores.size(); i++){
                scorePanel.setLayout(new GridLayout(scores.size(),1));
                JLabel score = new JLabel("Player " + (i+1) + " : "+ namList.get(i)+" " + scores.get(i));
                scorePanel.add(score);
            }
            scorePanel.revalidate();
            scorePanel.repaint();
        });
    }
    
}