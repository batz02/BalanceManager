import javax.swing.*;
import java.awt.*;

/**
 * classe per l'implementazione dell'interfaccia utente
 */
@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyGUI {

    /**
     * costruttore che chiama i tre metodi per la creazione della grafica
     */
    public MyGUI()
    {
        createComponent();

        setComponent();

        addPanel();

    }

    /**
     * metodo necessario alla creazione e inizializzazione dei componenti grafici
     */
    public void createComponent()
    {
        MyTable.mainPanel = new JPanel(new BorderLayout());
        MyTable.buttonPanel = new JPanel();
        MyTable.find = new JPanel(new BorderLayout());
        MyTable.strFindPanel = new JPanel(new BorderLayout());
        MyTable.visual1 = new JPanel(new BorderLayout());
        MyTable.visual2 = new JPanel(new BorderLayout());
        MyTable.visual0 = new JPanel(new BorderLayout());
        MyTable.visual = new JPanel(new BorderLayout());
        MyTable.textPanel = new JPanel(new BorderLayout());
        MyTable.datePanel = new JPanel(new BorderLayout());
        MyTable.descrPanel = new JPanel(new BorderLayout());
        MyTable.impPanel = new JPanel(new BorderLayout());
        MyTable.checkPanel = new JPanel();
        MyTable.align = new JPanel(new BorderLayout(30,30));

        MyTable.addButton = new JButton("+ Aggiungi");
        MyTable.updateButton = new JButton("⟳ Modifica");
        MyTable.clearButton = new JButton("- Elimina");
        MyTable.butFind = new JButton("Cerca");
        MyTable.butNext = new JButton("Successivo");
        MyTable.visAll = new JButton("Tutto");
        MyTable.visDay = new JButton("Giorno");
        MyTable.visWeek = new JButton("Settimana");
        MyTable.visMonth = new JButton("Mese");
        MyTable.visYear = new JButton("Anno");
        MyTable.visRange = new JButton("Intervallo");
        MyTable.b = new JButton("Calendario");

        MyTable.c1 = new JRadioButton("Entrata");
        MyTable.c2 = new JRadioButton("Uscita");

        MyTable.bg=new ButtonGroup();

        MyTable.labEnt = new JLabel("Entrate: ");
        MyTable.labUsc = new JLabel("Uscite: ");
        MyTable.labSal = new JLabel("Totale: ");
        MyTable.labFind = new JLabel(" Ricerca: ");
        MyTable.labVisual = new JLabel(" Visualizzazione corrente: ");
        MyTable.label1 = new JLabel(" Data:");
        MyTable.label2 = new JLabel(" Descrizione:");
        MyTable.label3 = new JLabel(" Importo:");

        MyTable.balView = new JTextField(10);
        MyTable.balViewUsc = new JTextField(10);
        MyTable.balViewEnt = new JTextField(10);
        MyTable.strFind = new JTextField(18);
        MyTable.visualizer = new JTextField(8);
        MyTable.dateVisualizer = new JTextField();
        MyTable.text1 = new JTextField();
        MyTable.text2 = new JTextField();
        MyTable.text3 = new JTextField();

        MyTable.dateSel = new JFrame();
    }

    /**
     * metodo necessario all'impostazione e modifica dei componenti grafici
     */
    public void setComponent()
    {
        MyTable.balViewEnt.setText(Double.toString( MyTable.balEnt));
        MyTable.balViewEnt.setEditable(false);
        MyTable.balViewUsc.setText(Double.toString( MyTable.balUsc));
        MyTable.balViewUsc.setEditable(false);
        MyTable.balView.setText(Double.toString( MyTable.bal));
        MyTable.balView.setEditable(false);
        MyTable.visualizer.setEditable(false);
        MyTable.dateVisualizer.setEditable(false);
        MyTable.visualizer.setHorizontalAlignment(SwingConstants.CENTER);
        MyTable.dateVisualizer.setHorizontalAlignment(SwingConstants.CENTER);
        MyTable.visualizer.setText("Tutto");
        MyTable.text1.setEditable(false);
        MyTable.text1.setHorizontalAlignment(SwingConstants.CENTER);
        MyTable.checkPanel.add(MyTable.c1);
        MyTable.checkPanel.add(MyTable.c2);
        MyTable.bg.add(MyTable.c1);
        MyTable.bg.add(MyTable.c2);
        MyTable.dateSel.getContentPane().add(MyTable.datePanel);
        MyTable.dateSel.pack();
        MyTable.dateSel.setVisible(false);
        MyTable.textPanel.setLayout(new BorderLayout(30,30));
    }

    /**
     * metodo necessario all'aggiunta di tutti i componenti all'interno dei corrispettivi pannelli
     */
    public void addPanel()
    {
        MyTable.buttonPanel.add( MyTable.addButton);
        MyTable.buttonPanel.add( MyTable.updateButton);
        MyTable.buttonPanel.add( MyTable.clearButton);
        MyTable.buttonPanel.add( MyTable.labEnt);
        MyTable.buttonPanel.add(MyTable.balViewEnt);
        MyTable.buttonPanel.add(MyTable.labUsc);
        MyTable.buttonPanel.add(MyTable.balViewUsc);
        MyTable.buttonPanel.add(MyTable.labSal);
        MyTable.buttonPanel.add(MyTable.balView);
        MyTable.strFindPanel.add(MyTable.labFind, BorderLayout.WEST);
        MyTable.strFindPanel.add(MyTable.strFind, BorderLayout.CENTER);
        MyTable.find.add(MyTable.strFindPanel, BorderLayout.NORTH);
        MyTable.find.add(MyTable.butFind, BorderLayout.CENTER);
        MyTable.find.add(MyTable.butNext, BorderLayout.SOUTH);
        MyTable.visual0.add(MyTable.labVisual, BorderLayout.WEST);
        MyTable.visual0.add(MyTable.visualizer, BorderLayout.EAST);
        MyTable.visual0.add(MyTable.dateVisualizer, BorderLayout.SOUTH);
        MyTable.visual1.add(MyTable.visAll, BorderLayout.NORTH);
        MyTable.visual1.add(MyTable.visDay, BorderLayout.CENTER);
        MyTable.visual1.add(MyTable.visWeek, BorderLayout.SOUTH);
        MyTable.visual2.add(MyTable.visMonth, BorderLayout.NORTH);
        MyTable.visual2.add(MyTable.visYear, BorderLayout.CENTER);
        MyTable.visual2.add(MyTable.visRange, BorderLayout.SOUTH);
        MyTable.visual.add(MyTable.visual0, BorderLayout.NORTH);
        MyTable.visual.add(MyTable.visual1, BorderLayout.CENTER);
        MyTable.visual.add(MyTable.visual2, BorderLayout.SOUTH);
        MyTable.datePanel.add(MyTable.label1, BorderLayout.WEST);
        MyTable.datePanel.add(MyTable.text1, BorderLayout.CENTER);
        MyTable.datePanel.add(MyTable.b, BorderLayout.EAST);
        MyTable.descrPanel.add(MyTable.label2, BorderLayout.WEST);
        MyTable.descrPanel.add(MyTable.text2, BorderLayout.CENTER);
        MyTable.impPanel.add(MyTable.label3, BorderLayout.WEST);
        MyTable.impPanel.add(MyTable.text3, BorderLayout.SOUTH);
        MyTable.impPanel.add(MyTable.checkPanel, BorderLayout.CENTER);
        MyTable.textPanel.add(MyTable.datePanel, BorderLayout.NORTH);
        MyTable.textPanel.add(MyTable.descrPanel, BorderLayout.CENTER);
        MyTable.textPanel.add(MyTable.impPanel, BorderLayout.SOUTH);
        MyTable.align.add(MyTable.textPanel, BorderLayout.NORTH);
        MyTable.align.add(MyTable.find, BorderLayout.CENTER);
        MyTable.align.add(MyTable.visual, BorderLayout.SOUTH);
        MyTable.mainPanel.add(MyTable.align, BorderLayout.WEST);
        MyTable.mainPanel.add(new JScrollPane(MyTable.getTable()),BorderLayout.CENTER);
        MyTable.mainPanel.add(MyTable.buttonPanel, BorderLayout.SOUTH);
    }
}
