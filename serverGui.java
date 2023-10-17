import javax.swing.*;
import java.awt.*;

public class serverGui extends JFrame{

    public serverGui(){
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        JMenuBar menu = new JMenuBar();
        JMenu diff = new JMenu("Difficulty");
        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem hard = new JMenuItem("Hard");
        JPanel panel = new JPanel(new GridLayout(2,2));

        diff.add(easy);
        diff.add(medium);
        diff.add(hard);
        Server serv = new Server();

        start.addActionListener(e -> {
            new Thread(() -> serv.start(1)).start();
        });

        stop.addActionListener(e -> {
            new client();
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
        panel.add(start);
        panel.add(stop);
        
        menu.add(diff);
        this.setJMenuBar(menu);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 200);
        this.setVisible(true);
        //new Server().start();
    }
}