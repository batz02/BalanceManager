import javax.swing.*;
import java.awt.*;

/**
 * classe principale che gestisce il main
 */

@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij

public class BalanceManager{

    /**
     * main che crea un jFrame con tutti i parametri necessari, il cui contenuto viene settato con
     *  * i componenti provenienti da MyTable. Viene creata inoltre una jMenuBar
     * @param args (argomenti che, tramite linea di comando, possono essere letti dal programma)
     */
    public static void main(String[] args){
        JFrame app = new JFrame("Balance Manager");
        new MyMenu(app);
        app.getContentPane().add(new MyTable().getComponent());
        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setResizable(true);
        app.setMinimumSize(new Dimension(1000, 650));
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}