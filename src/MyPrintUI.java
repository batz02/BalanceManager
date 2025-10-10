import java.awt.*;
import java.awt.print.Printable;  
import java.awt.print.PrinterException;  
import java.awt.print.PrinterJob;
import javax.swing.JButton;  
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;  
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.table.DefaultTableModel;

/**
 * classe che implementa la funzionalità di stampa della tabella principale
 */
@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyPrintUI extends JFrame {

    static JTable table;

    /**
     * tabella principale gestita per l'anteprima di stampa
     */
    private final DefaultTableModel model;

    /**
     * costruttore per l'avvio dell'interfaccia utente per la stampa
     * @param tmp (tabella principale con tutti i dati da stampare)
     */
    public MyPrintUI (DefaultTableModel tmp) {
        super("TablePrintTest");
        this.model = tmp;
        print();
    }

    /**
     * metodo che implementa l'anteprima di stampa e la successiva scelta della stampante
     */
    public void print() {

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(95);
        table.getColumnModel().getColumn(1).setPreferredWidth(275);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        JButton printButton = new JButton("Stampa");

        printButton.addActionListener(e -> {
            Printable printable = table.getPrintable(PrintMode.NORMAL,
                    null, null);
            PrinterJob pJob = PrinterJob.getPrinterJob();
            pJob.setPrintable(printable);

            if (pJob.printDialog()) {
                try {
                    pJob.print();
                } catch (PrinterException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(printButton, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        setContentPane(contentPane);
        setTitle("Anteprima di stampa");
        setPreferredSize(new Dimension(600, 350));
        pack();
        setLocationRelativeTo(null);

    }
}

