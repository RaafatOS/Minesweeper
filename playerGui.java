import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class playerGui extends JPanel implements ActionListener{
    private String name;
    private JPanel playPanel = new JPanel(new GridLayout(9, 9));
    private JButton button = new JButton("Play");

    

    playerGui(String title, Matrix mat){
        
        super(new BorderLayout());
        name = title;
        playPanel.setBorder(BorderFactory.createTitledBorder(name));
        playPanel.setPreferredSize(new Dimension(500, 500));
        playPanel.setBackground(Color.WHITE);
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                feild cas = new feild(i,j);
                cas.setPreferredSize(new Dimension(50, 50));
                cas.setBackground(Color.LIGHT_GRAY);
                playPanel.add(cas);
            }
        }
        add(playPanel, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
