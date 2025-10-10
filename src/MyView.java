import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * classe per la gestione della visualizzazione della tabella
 */
@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyView {

    /**
     * costruttore per la gestione e l'impostazione della visualizzazione scelta dall'utente
     * @param select (opzione per selezionare la visualizzazione: 0 per tutto, 1 per un giorno, 2 per una settimana,
     *               3 per un mese, 4 per un anno, 5 per un intervallo di tempo)
     */
    public MyView(int select)
    {
        if(select==0)
        {
            if (MyTable.visSelect==0)
            {
                return;
            }

            visualAll();
            return;
        }

        JFrame f = new JFrame();
        f.pack();
        f.setVisible(false);

        if(select==1)
        {
            String dayTmp = new MyCalendarUI(f, "Seleziona un giorno").setPickedDate();

            if(dayTmp.equals(""))
            {
                return;
            }

            if(MyTable.visSelect==1 && MyTable.daySelect.equals(dayTmp))
            {
                JOptionPane.showMessageDialog(null,"Giorno già presente " +
                        "nella visualizzazione corrente");
                return;
            }

            visualDay(dayTmp);
            return;
        }

        if(select==2)
        {
            String dayTmp = new MyCalendarUI(f,"Seleziona un giorno per indicare " +
                    "la settimana da cercare").setPickedDate();

            if (dayTmp.equals("")) {
                return;
            }

            visualWeek(dayTmp);
            return;
        }

        if(select==3)
        {
            String dayTmp = new MyCalendarUI(f,"Seleziona un giorno per " +
                    "indicare il mese da cercare").setPickedDate();

            if(dayTmp.equals(""))
            {
                return;
            }

            if(MyTable.visSelect==3 && MyTable.monthSelect.equals(dayTmp.substring(3)))
            {
                JOptionPane.showMessageDialog(null,"Mese già presente " +
                        "nella visualizzazione corrente");
                return;
            }

            visualMonth(dayTmp);
            return;
        }

        if(select==4)
        {
            String yearTmp = JOptionPane.showInputDialog(null,"Inserisci anno");

            if(yearTmp==null || yearTmp.equals(""))
            {
                return;
            }

            int year = Integer.parseInt(yearTmp);

            if(MyTable.visSelect==4 && year==MyTable.yearSelect)
            {
                JOptionPane.showMessageDialog(null,"Anno già presente " +
                        "nella visualizzazione corrente");
                return;
            }

            visualYear(year);
            return;
        }

        if(select==5)
        {
            String firstDay = new MyCalendarUI(f,"Seleziona il giorno iniziale").setPickedDate();
            String lastDay = new MyCalendarUI(f, "Seleziona il giorno finale").setPickedDate();

            if (firstDay.equals("") || lastDay.equals("")) {
                return;
            }

            visualRange(firstDay,lastDay);
        }
    }

    /**
     * metodo per aggiornare il saldo della visualizzazione corrente con l'importo contenuto in una riga della
     * tabella principale
     * @param i (riga della tabella principale)
     */
    public void setBalanceVisual(int i)
    {
        if(MyTable.getModel().getValueAt(i,2).toString().equals("Entrata"))
        {
            MyTable.balEntVisual = MyTable.balEntVisual + Double.parseDouble(MyTable.getModel().getValueAt(i,3).
                    toString().substring(4));
        }
        else
        {
            MyTable.balUscVisual = MyTable.balUscVisual + Double.parseDouble(MyTable.getModel().getValueAt(i,3).
                    toString().substring(4));
        }
        MyTable.balVisual =MyTable. balEntVisual - MyTable.balUscVisual;
        MyTable.balView.setText(Double.toString(MyTable.balVisual));
        MyTable.balViewEnt.setText(Double.toString(MyTable.balEntVisual));
        MyTable.balViewUsc.setText(Double.toString(MyTable.balUscVisual));
    }

    /**
     * metodo per impostare la visualizzazione a tutte le operazioni con il rispettivo bilancio
     */
    public void visualAll()
    {
        MyTable.getTable().setModel(MyTable.getModel());
        MyTable.balView.setText(Double.toString(MyTable.bal));
        MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
        MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
        MyTable.visSelect=0;
        MyTable.visualizer.setText("Tutto");
        MyTable.dateVisualizer.setText("");
    }

    /**
     * metodo per impostare la visualizzazione ad un certo giorno con il rispettivo bilancio
     * @param dayTmp (giorno scelto dall'utente)
     */
    public void visualDay(String dayTmp)
    {
        MyTable.getModelDay().setRowCount(0);
        MyTable.balEntVisual=0;
        MyTable.balUscVisual=0;
        MyTable.balVisual=0;
        boolean find = false;
        for(int i=0;i<MyTable.getModel().getRowCount();i++)
        {
            String tmp = (MyTable.getModel().getValueAt(i,0).toString());
            if(tmp.equals(dayTmp))
            {
                find=true;

                setBalanceVisual(i);

                MyTable.getModelDay().addRow(new Object[]{
                        MyTable.getModel().getValueAt(i,0),
                        MyTable.getModel().getValueAt(i,1),
                        MyTable.getModel().getValueAt(i,2),
                        MyTable.getModel().getValueAt(i,3)} );
            }
        }

        if(!find)
        {
            JOptionPane.showMessageDialog(null, "Nessuna operazione nel giorno " + dayTmp);
            visualAll();
            return;
        }

        MyTable.getTable().setModel(MyTable.getModelDay());
        MyTable.visSelect=1;
        MyTable.visualizer.setText("Giorno");
        MyTable.dateVisualizer.setText("Scelta: "+dayTmp);
        MyTable.daySelect=dayTmp;
    }

    /**
     * metodo per impostare la visualizzazione ad una settimana con il rispettivo bilancio
     * @param dayTmp (giorno della settimana scelto dall'utente)
     */
    public void visualWeek(String dayTmp)
    {
        Calendar c = Calendar.getInstance();

        Date date;
        try {
            date = MyTable.formatter.parse(dayTmp);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String firstDay = df.format(c.getTime());
        for (int i = 0; i <6; i++) {
            c.add(Calendar.DATE, 1);
        }
        String lastDay = df.format(c.getTime());
        Date d1, d2;
        try {
            d1 = MyTable.formatter.parse(firstDay);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        try {
            d2 = MyTable.formatter.parse(lastDay);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }


        if (MyTable.visSelect == 2 && MyTable.weekSelect[0].equals(firstDay) && MyTable.weekSelect[1].equals(lastDay))
        {
            JOptionPane.showMessageDialog(null, "Settimana già presente " +
                    "nella visualizzazione corrente");
            return;
        }

        MyTable.getModelWeek().setRowCount(0);
        MyTable.balEntVisual = 0;
        MyTable.balUscVisual = 0;
        MyTable.balVisual = 0;
        boolean find = false;
        for (int i = 0; i < MyTable.getModel().getRowCount(); i++) {

            Date tmp;
            try {
                tmp = MyTable.formatter.parse(MyTable.getModel().getValueAt(i, 0).toString());
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            if (tmp.compareTo(d1)>=0 && tmp.compareTo(d2)<=0) {
                find = true;

                setBalanceVisual(i);
                MyTable.getModelWeek().addRow(new Object[]{
                        MyTable. getModel().getValueAt(i, 0),
                        MyTable.getModel().getValueAt(i, 1),
                        MyTable.getModel().getValueAt(i, 2),
                        MyTable.getModel().getValueAt(i, 3)
                });

            }

        }
        if (!find) {
            JOptionPane.showMessageDialog(null, "Nessuna operazione nella settimana " +
                    "dal "+ firstDay+ " al "+ lastDay);
            visualAll();
            return;
        }
        MyTable.getTable().setModel(MyTable.getModelWeek());
        MyTable.visSelect = 2;
        MyTable.visualizer.setText("Settimana");
        MyTable.dateVisualizer.setText("Scelta: dal " + firstDay + " al " + lastDay);
        MyTable.weekSelect[0]=firstDay;
        MyTable.weekSelect[1]=lastDay;
    }

    /**
     * metodo per impostare la visualizzazione ad un mese con il rispettivo bilancio
     * @param dayTmp (giorno del mese scelto dall'utente)
     */
    public void visualMonth(String dayTmp)
    {
        MyTable.getModelMonth().setRowCount(0);
        MyTable.balEntVisual=0;
        MyTable.balUscVisual=0;
        MyTable.balVisual=0;
        boolean find = false;
        for(int i=0;i<MyTable.getModel().getRowCount();i++)
        {
            String tmp = (MyTable.getModel().getValueAt(i,0).toString()).substring(3);
            if(tmp.equals(dayTmp.substring(3)))
            {
                find=true;

                setBalanceVisual(i);
                MyTable.getModelMonth().addRow(new Object[]{
                        MyTable.getModel().getValueAt(i,0),
                        MyTable.getModel().getValueAt(i,1),
                        MyTable.getModel().getValueAt(i,2),
                        MyTable.getModel().getValueAt(i,3)
                });

            }

        }
        if(!find)
        {
            JOptionPane.showMessageDialog(null, "Nessuna operazione nel mese " +
                    (Month.of(Integer.parseInt(dayTmp.substring(3,5)))
                            .getDisplayName(TextStyle.FULL_STANDALONE, Locale.ITALIAN)) + " " + dayTmp.substring(6));
            visualAll();
            return;
        }
        MyTable.getTable().setModel(MyTable.getModelMonth());
        MyTable.visSelect=3;
        MyTable.monthSelect=dayTmp.substring(3);
        MyTable.visualizer.setText("Mese");
        MyTable.dateVisualizer.setText("Scelta: " + Month.of(Integer.parseInt(dayTmp.substring(3,5)))
                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.ITALIAN) + " " + dayTmp.substring(6));
    }

    /**
     * metodo per impostare la visualizzazione ad un anno con il rispettivo bilancio
     * @param year (anno inserito dall'utente)
     */
    public void visualYear(int year)
    {
        MyTable.getModelYear().setRowCount(0);
        MyTable.balEntVisual=0;
        MyTable.balUscVisual=0;
        MyTable. balVisual=0;
        boolean find = false;
        for(int i=0;i<MyTable.getModel().getRowCount();i++)
        {
            if(year==Integer.parseInt((MyTable.getModel().getValueAt(i,0).toString()).substring(6)))
            {
                find=true;

                setBalanceVisual(i);

                MyTable.getModelYear().addRow(new Object[]{
                        MyTable.getModel().getValueAt(i,0),
                        MyTable.getModel().getValueAt(i,1),
                        MyTable.getModel().getValueAt(i,2),
                        MyTable.getModel().getValueAt(i,3)
                });

            }

        }
        if(!find)
        {
            JOptionPane.showMessageDialog(null, "Nessuna operazione nell'anno "+year);
            visualAll();
            return;
        }
        MyTable.getTable().setModel(MyTable.getModelYear());
        MyTable.visSelect=4;
        MyTable.visualizer.setText("Anno");
        MyTable.dateVisualizer.setText("Scelta: "+year);
        MyTable.yearSelect=year;
    }

    /**
     * metodo per impostare la visualizzazione ad un intervallo di date con il rispettivo bilancio
     * @param firstDay (giorno iniziale dell'intervallo scelto dall'utente)
     * @param lastDay (giorno finale dell'intervallo scelto dall'utente)
     */
    public void visualRange(String firstDay, String lastDay)
    {
        Date d1, d2;
        try {
            d1 = MyTable.formatter.parse(firstDay);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        try {
            d2 = MyTable.formatter.parse(lastDay);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        if(d1.compareTo(d2)>0)
        {
            String tmp;
            tmp=firstDay;
            firstDay=lastDay;
            lastDay=tmp;

            try {
                d1 = MyTable.formatter.parse(firstDay);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            try {
                d2 = MyTable.formatter.parse(lastDay);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (MyTable.visSelect == 5 && MyTable.rangeSelect[0].equals(firstDay) && MyTable.rangeSelect[1].equals(lastDay))
        {
            JOptionPane.showMessageDialog(null, "Intervallo già presente " +
                    "nella visualizzazione corrente");
            return;
        }

        MyTable.getModelRange().setRowCount(0);
        MyTable. balEntVisual = 0;
        MyTable.balUscVisual = 0;
        MyTable.balVisual = 0;
        boolean find = false;
        for (int i = 0; i < MyTable.getModel().getRowCount(); i++) {

            Date tmp;
            try {
                tmp = MyTable.formatter.parse(MyTable.getModel().getValueAt(i, 0).toString());
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            if (tmp.compareTo(d1)>=0 && tmp.compareTo(d2)<=0) {
                find = true;

                setBalanceVisual(i);

                MyTable.getModelRange().addRow(new Object[]{
                        MyTable.getModel().getValueAt(i, 0),
                        MyTable.getModel().getValueAt(i, 1),
                        MyTable.getModel().getValueAt(i, 2),
                        MyTable.getModel().getValueAt(i, 3) });
            }
        }
        if (!find) {
            JOptionPane.showMessageDialog(null, "Nessuna operazione nell'intervallo " +
                    "dal giorno "+firstDay + " al giorno "+ lastDay);
            visualAll();
            return;
        }
        MyTable.getTable().setModel(MyTable.getModelRange());
        MyTable.visSelect = 5;
        MyTable.visualizer.setText("Intervallo");
        MyTable.dateVisualizer.setText("Scelta: dal "+ firstDay + " al " + lastDay);
        MyTable.rangeSelect[0]=firstDay;
        MyTable.rangeSelect[1]=lastDay;
    }
}