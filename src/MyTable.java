import javax.swing.*;
import javax.swing.table.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

/**
 * classe che gestisce tutte le tabelle e le principali operazioni che possono essere fatte su di esse
 */

@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyTable {

    static JTextField strFind, text1, text2, text3, visualizer, dateVisualizer;
    static JTextField balView, balViewEnt, balViewUsc;
    static JRadioButton c1, c2;
    static ButtonGroup bg;
    static int nextRow, visSelect = 0, yearSelect = 0;
    static JButton addButton, updateButton, clearButton, butFind, butNext ;
    static JButton visAll, visDay, visWeek, visMonth, visYear, visRange, b;
    static JPanel buttonPanel, find, strFindPanel, visual1, visual2, visual, visual0;
    static JPanel textPanel, datePanel, impPanel, descrPanel, checkPanel, align;
    static JLabel labEnt, labUsc, labSal, labFind, label1, label2, label3, labVisual;
    static JFrame dateSel;
    static boolean resFindEnd=false, findUse=false;
    static double bal=0, balEnt=0, balUsc=0, balVisual=0, balEntVisual=0, balUscVisual=0;
    static String monthSelect = null, daySelect = null;
    static String[] weekSelect = {" "," "}, rangeSelect = {" "," "};
    static String[] columns = {"Data", "Descrizione", "Tipologia" , "Importo"};
    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    private static final DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private static final DefaultTableModel modelDay = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private static final DefaultTableModel modelWeek = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private static final DefaultTableModel modelMonth = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private static final DefaultTableModel modelYear = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private static final DefaultTableModel modelRange = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    /**
     * metodo per ripristinare la tabella principale e il corrispettivo saldo
     */
    public static void resetModel()
    {
        getModel().setRowCount(0);
        bal = 0;
        balEnt = 0;
        balUsc = 0;
    }

    /**
     * metodo per aggiungere una riga alla tabella principale in seguito ad un caricamento
     * @param t1 (data)
     * @param t2 (descrizione)
     * @param t3 (tipologia)
     * @param t4 (importo)
     */
    public static void updateModel(String t1, String t2, String t3, String t4)
    {
        getModel().addRow(new Object[]{ t1, t2, t3, t4 });
        if(t3.equals("Entrata"))
        {
            balEnt = balEnt + Double.parseDouble(t4.substring(4));
            balViewEnt.setText(Double.toString(balEnt));
        }
        else
        {
            balUsc = balUsc + Double.parseDouble(t4.substring(4));
            balViewUsc.setText(Double.toString(balUsc));
        }
        bal = balEnt - balUsc;
        balView.setText(Double.toString(bal));
    }

    /**
     * metodo per verificare se una data appartiene alla visualizzazione corrente
     * @param dateTmp (data da controllare)
     * @return (vero se appartiene alla visualizzazione, falso altrimenti)
     */
    public static boolean checkView(String dateTmp)
    {
        if(visSelect==1)
        {
            if(dateTmp.equals(daySelect))
            {
                return true;
            }
            return false;
        }

        Date tmp;
        try {
            tmp = formatter.parse(dateTmp);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        if(visSelect==2)
        {
            Date firstDayW;
            try {
                firstDayW = formatter.parse(weekSelect[0]);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            Date lastDayW ;
            try {
                lastDayW = formatter.parse(weekSelect[1]);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            if(tmp.compareTo(firstDayW)>=0 && tmp.compareTo(lastDayW)<=0)
            {
                return true;
            }
            return false;
        }

        if(visSelect==3)
        {
            if((dateTmp.substring(3)).equals(monthSelect))
            {
                return true;
            }
            return false;
        }

        if(visSelect==4)
        {
            if(Integer.parseInt(dateTmp.substring(6))==yearSelect)
            {
                return true;
            }
            return false;
        }

        if(visSelect==5)
        {
            Date firstDayR;
            try {
                firstDayR = formatter.parse(rangeSelect[0]);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            Date lastDayR;
            try {
                lastDayR = formatter.parse(rangeSelect[1]);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            if(tmp.compareTo(firstDayR)>=0 && tmp.compareTo(lastDayR)<=0)
            {
                return true;
            }
            return false;
        }

        return false;
    }

    /**
     * metodo per trovare la riga della visualizzazione corrente nella tabella principale
     * @param t1 (data)
     * @param t2 (descrizione)
     * @param t3 (tipologia)
     * @param t4 (importo)
     * @return (l'indice della riga)
     */
    public static int findRowIndex (String t1, String t2, String t3, String t4)
    {
        int nRow = getModel().getRowCount();
        for(int i=0;i<nRow;i++)
        {
            if(t1.equals(getModel().getValueAt(i,0)) &&
                    t2.equals(getModel().getValueAt(i,1)) &&
                    t3.equals(getModel().getValueAt(i,2)) &&
                    t4.equals(getModel().getValueAt(i,3)))
            {
                return i;
            }
        }
        return -1;

    }

    /**
     * metodo per verificare se una stringa contiene un valore numerico o no
     * @param str (stringa da verificare)
     * @return (vero se la stringa è numerica, falso altrimenti)
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            if(Double.parseDouble(str)>0)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * metodo per aggiungere una riga alla tabella della visualizzazione corrente
     * @param s1 (data)
     * @param s2 (descrizione)
     * @param s3 (tipologia)
     * @param s4 (importo)
     */
    public static void addRowVisual(String s1, String s2, String s3, String s4)
    {
        switch (visSelect){
            case 1:
                getModelDay().addRow( new Object[]{ s1, s2, s3, s4});
                break;
            case 2:
                getModelWeek().addRow( new Object[]{ s1, s2, s3, s4});
                break;
            case 3:
                getModelMonth().addRow( new Object[]{ s1, s2, s3, s4});
                break;
            case 4:
                getModelYear().addRow( new Object[]{ s1, s2, s3, s4});
                break;
            case 5:
                getModelRange().addRow( new Object[]{ s1, s2, s3, s4});
                break;
        }
    }

    private static final JTable table = new JTable(getModel());

    static JPanel mainPanel;

    /**
     * metodo per ottenere la jTable del frame
     * @return (la jTable)
     */
    public static JTable getTable()
    {
        return table;
    }

    /**
     * metodo per ottenere la tabella principale
     * @return (la DefaultTableModel)
     */
    public static DefaultTableModel getModel()
    {
        return model;
    }

    /**
     * metodo per ottenere la tabella necessaria alla visualizzazione di un giorno
     * @return (la DefaultTableModel)
     */
    public static DefaultTableModel getModelDay()
    {
        return modelDay;
    }

    /**
     * metodo per ottenere la tabella necessaria alla visualizzazione di una settimana
     * @return (la DefaultTableModel)
     */
    public static DefaultTableModel getModelWeek()
    {
        return modelWeek;
    }

    /**
     * metodo per ottenere la tabella necessaria alla visualizzazione di un mese
     * @return (la DefaultTableModel)
     */
    public static DefaultTableModel getModelMonth()
    {
        return modelMonth;
    }

    /**
     * metodo per ottenere la tabella necessaria alla visualizzazione di un anno
     * @return (la DefaultTableModel)
     */
    public static DefaultTableModel getModelYear()
    {
        return modelYear;
    }

    /**
     * metodo per ottenere la tabella necessaria alla visualizzazione di un intervallo
     * @return (la DefaultTableModel)
     */
    public static DefaultTableModel getModelRange()
    {
        return modelRange;
    }

    /**
     * costruttore che gestisce i metodi per l'interfaccia utente, il salvataggio automatico e i pulsanti della barra laterale
     */
    public MyTable() {

        new MyGUI();
        new MyAutoSave().start();

        addButton.addActionListener(e -> new MyAddButton());

        clearButton.addActionListener(e -> new MyClearButton());

        updateButton.addActionListener(e -> new MyUpdateButton());

        butFind.addActionListener(e -> new MyFind(0));

        butNext.addActionListener(e -> new MyFind(1));

        visAll.addActionListener(e -> new MyView(0));

        visDay.addActionListener(e -> new MyView(1));

        visWeek.addActionListener(e -> new MyView(2));

        visMonth.addActionListener(e -> new MyView(3));

        visYear.addActionListener(e -> new MyView(4));

        visRange.addActionListener(e -> new MyView(5));

        Date date = new Date();
        text1.setText(formatter.format(date));
        b.addActionListener(e -> {
            String tmp = text1.getText();
            text1.setText(new MyCalendarUI(dateSel,"Seleziona un giorno").setPickedDate());
            if(text1.getText().equals(""))
            {
                text1.setText(tmp);
            }
        });
    }

    /**
     * metodo per ottenere il pannello principale
     * @return (il pannello contenente tutti i componenti)
     */
    public JComponent getComponent() {
        return mainPanel;
    }
}
