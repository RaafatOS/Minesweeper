import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class feild extends JPanel implements MouseListener, ActionListener {
    private int x, y;
    static int previousVal = 0;

    private String txt = "";
    public int state = 0; // 0: hidden, 1: flagged, 2: revealed

    public feild(int x, int y) {
        this.x = x;
        this.y = y;
        setPreferredSize(new Dimension(50, 50)); // taille de la case
        addMouseListener(this); // ajout listener souris
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
