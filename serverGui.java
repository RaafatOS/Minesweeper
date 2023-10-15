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
        menu.add(diff);
        this.setJMenuBar(menu);

        start.addActionListener(e -> {
            new Thread(() -> new Server().start()).start();
        });

        stop.addActionListener(e -> {
            new client();
        });

        
        panel.add(start);
        panel.add(stop);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 200);
        this.setVisible(true);
        //new Server().start();
    }
}