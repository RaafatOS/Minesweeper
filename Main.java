import java.awt.*;
import java.util.*;
import javax.swing.*;


public class Main extends JFrame {

    public void createServer() {
        SwingUtilities.invokeLater(()-> new serverGui());
    }

    public serverGui getServerGui(){
        return new serverGui();
    }

    public Main() {
         Matrix m = new Matrix(7,7);

        // //display
        // m.display();
        // m.computeMinesNumber();
        // m.placeNumbersDisplay();

        GUI gui = new GUI(m, this);
        
        setContentPane(gui);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        //setTitle("Minesweeper");
        //m.changeMatrix();
    }
    public static void main(String[] args) {
        new Main();
    }
}
