import javax.swing.*;
import java.text.ParseException;
import java.util.Date;

/**
 * classe che implementa il funzionamento del pulsante Modifica
 */

@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyUpdateButton {

    /**
     * costruttore per gestire la modifica su una riga e colonna scelta dall'utente
     */
    public MyUpdateButton() {

        if(MyTable.getModel().getRowCount()>0)
        {
            int rowSel = MyTable.getTable().getSelectedRow();

            if(rowSel<0)
            {
                JOptionPane.showMessageDialog(null, "Selezionare una riga e una colonna da modificare");
                return;
            }

            int colSel = MyTable.getTable().getSelectedColumn();

            if(MyTable.getTable().getRowCount()>1)
            {
                MyTable.getTable().removeRowSelectionInterval(0, (MyTable.getTable().getRowCount())-1);
            }

            if (colSel == 0)
            {
                updateDate(rowSel,colSel);
                return;
            }

            if (colSel == 1)
            {
                updateDescr(rowSel,colSel);
                return;
            }

            if (colSel == 2)
            {
                updateTip(rowSel,colSel);
                return;
            }

            if (colSel == 3)
            {
                updateImp(rowSel,colSel);
            }

        }
        else {

            JOptionPane.showMessageDialog(null, "Inserire almeno un'operazione");
        }

    }

    /**
     * metodo per l'aggiornamento del bilancio in caso di modifica della data contenuta in un periodo diverso
     * dalla visualizzazione scelta
     * @param tmpImp (importo da rimuovere dal saldo visualizzato)
     * @param option (0 se si tratta di un'entrata / 1 se si tratta di un'uscita)
     */
    public void updateDateBalVisualOption (double tmpImp, int option)
    {
        if(option==0)
        {
            MyTable.balEntVisual = MyTable.balEntVisual - tmpImp;
            MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
            MyTable.balViewEnt.setText(Double.toString(MyTable.balEntVisual));
        }
        else
        {
            MyTable.balUscVisual = MyTable.balUscVisual - tmpImp;
            MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
            MyTable.balViewUsc.setText(Double.toString(MyTable.balUscVisual));
        }
        MyTable.balView.setText(Double.toString(MyTable.balVisual));
    }

    /**
     * metodo per l'aggiornamento dei jTextField contenente il saldo Entrata-Uscita-Totale, in seguito alla modifica
     * di una tipologia di operazione
     * @param imp (importo convertito in entrata o uscita)
     * @param option (0 se viene modificato da entrata ad uscita / 1 se viene modificato da uscita ad entrata)
     */
    public void updateBalanceVisualOption (double imp, int option)
    {
        if(option==0)
        {
            MyTable.balEnt = MyTable.balEnt - imp;
            MyTable.balUsc = MyTable.balUsc + imp;
            MyTable.bal = MyTable.balEnt - MyTable.balUsc;
            MyTable.balEntVisual = MyTable.balEntVisual - imp;
            MyTable.balUscVisual = MyTable.balUscVisual + imp;
        }
        else
        {
            MyTable.balEnt = MyTable.balEnt + imp;
            MyTable.balUsc = MyTable.balUsc - imp;
            MyTable.bal = MyTable.balEnt - MyTable.balUsc;
            MyTable.balEntVisual = MyTable.balEntVisual + imp;
            MyTable.balUscVisual = MyTable.balUscVisual - imp;
        }

        MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
        MyTable.balView.setText(Double.toString(MyTable.balVisual));
        MyTable.balViewEnt.setText(Double.toString(MyTable.balEntVisual));
        MyTable.balViewUsc.setText(Double.toString(MyTable.balUscVisual));
    }

    /**
     * metodo per l'aggiornamento dei jTextField contenente il saldo Entrata-Uscita-Totale, in seguito alla modifica di un importo
     * @param tmp (valore vecchio)
     * @param newVal (valore nuovo)
     * @param option (0 se si tratta di un'entrata / 1 se si tratta di un'uscita)
     */
    public void updateImpVisualOption (double tmp, double newVal, int option)
    {
        if(option==0)
        {
            MyTable.bal=MyTable.bal-tmp+newVal;
            MyTable.balEnt=MyTable.balEnt-tmp+newVal;
            MyTable.balVisual=MyTable.balVisual-tmp+newVal;
            MyTable.balEntVisual=MyTable.balEntVisual-tmp+newVal;
        }
        else
        {
            MyTable.bal=MyTable.bal+tmp-newVal;
            MyTable.balUsc=MyTable.balUsc-tmp+newVal;
            MyTable.balVisual=MyTable.balVisual+tmp-newVal;
            MyTable.balUscVisual=MyTable.balUscVisual-tmp+newVal;
        }
        MyTable.balView.setText(Double.toString(MyTable.balVisual));
        MyTable.balViewEnt.setText(Double.toString(MyTable.balEntVisual));
        MyTable.balViewUsc.setText(Double.toString(MyTable.balUscVisual));
    }

    /**
     * metodo per impostare la visualizzazione a Tutto nel caso in cui non ci siano altre operazioni nella
     * visualizzazione corrente
     */
    public void setVisualAll()
    {
        MyTable.getTable().setModel(MyTable.getModel());
        JOptionPane.showMessageDialog(null,"Nella visualizzazione scelta non ci sono" +
                " altre operazioni --> visualizzazione impostata su: Tutto ");
        MyTable.visualizer.setText("Tutto");
        MyTable.dateVisualizer.setText("");
        MyTable.visSelect=0;
        MyTable.balView.setText(Double.toString(MyTable.bal));
        MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
        MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
    }

    /**
     * metodo per la modifica di una data presente in una certa riga e colonna
     * @param rowSel (riga selezionata)
     * @param colSel (colonna selezionata)
     */
    public void updateDate(int rowSel, int colSel)
    {
        JFrame f = new JFrame();
        f.pack();
        f.setVisible(false);

        String newVal = new MyCalendarUI(f,"Calendario").setPickedDate();

        if(newVal.equals(""))
        {
            return;
        }
        Date tmp;
        try {
            tmp = MyTable.formatter.parse(newVal);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        Date dateNow = new Date();

        if(tmp.compareTo(dateNow)>0 )
        {
            JOptionPane.showMessageDialog(null, "Inserire una data precedente " +
                    "a quella odierna");
            return;
        }

        int modelIndex;

        if(MyTable.visSelect==0)
        {
            MyTable.getModel().setValueAt(newVal,rowSel,colSel);
            return;
        }

        if(MyTable.visSelect==1)
        {
            modelIndex = MyTable.findRowIndex(MyTable.getModelDay().getValueAt(rowSel,0).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,1).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,2).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,3).toString());

            if(MyTable.checkView(newVal))
            {
                MyTable.getModelDay().setValueAt(newVal, rowSel, colSel);
            }
            else
            {

                if(MyTable.getModelDay().getValueAt(rowSel,2).equals("Entrata"))
                {
                    double tmpImp = Double.parseDouble(MyTable.getModelDay().getValueAt
                            (rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 0);
                }
                else
                {
                    double tmpImp =  Double.parseDouble
                            (MyTable.getModelDay().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 1);
                }

                MyTable.getModelDay().removeRow(rowSel);
            }
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            if(MyTable.getTable().getRowCount()<=1)
            {
                setVisualAll();
            }
            return;
        }

        if(MyTable.visSelect==2)
        {
            modelIndex = MyTable.findRowIndex(MyTable.getModelWeek().getValueAt(rowSel,0).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,1).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,2).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,3).toString());
            if(MyTable.checkView(newVal))
            {
                MyTable.getModelWeek().setValueAt(newVal, rowSel, colSel);
            }
            else
            {

                if(MyTable.getModelWeek().getValueAt(rowSel,2).equals("Entrata"))
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelWeek().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 0);
                }
                else
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelWeek().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 1);
                }

                MyTable.getModelWeek().removeRow(rowSel);
            }
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            if(MyTable.getTable().getRowCount()<=1)
            {
                setVisualAll();
            }
            return;
        }

        if(MyTable.visSelect==3)
        {
            modelIndex = MyTable.findRowIndex(MyTable.getModelMonth().getValueAt(rowSel,0).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,1).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,2).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,3).toString());
            if(MyTable.checkView(newVal))
            {
                MyTable.getModelMonth().setValueAt(newVal, rowSel, colSel);
            }
            else
            {

                if(MyTable.getModelMonth().getValueAt(rowSel,2).equals("Entrata"))
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelMonth().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 0);
                }
                else
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelMonth().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 1);
                }

                MyTable.getModelMonth().removeRow(rowSel);
            }
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            if(MyTable.getTable().getRowCount()<=1)
            {
                setVisualAll();
            }
            return;
        }

        if(MyTable.visSelect==4)
        {
            modelIndex = MyTable.findRowIndex(MyTable.getModelYear().getValueAt(rowSel,0).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,1).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,2).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,3).toString());
            if(MyTable.checkView(newVal))
            {
                MyTable.getModelYear().setValueAt(newVal, rowSel, colSel);
            }
            else
            {

                if(MyTable.getModelYear().getValueAt(rowSel,2).equals("Entrata"))
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelYear().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 0);
                }
                else
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelYear().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 1);
                }

                MyTable.getModelYear().removeRow(rowSel);
            }
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            if(MyTable.getTable().getRowCount()<=1)
            {
                setVisualAll();
            }
            return;
        }

        if(MyTable.visSelect==5)
        {
            modelIndex = MyTable.findRowIndex(MyTable.getModelRange().getValueAt(rowSel,0).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,1).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,2).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,3).toString());
            if(MyTable.checkView(newVal))
            {
                MyTable.getModelRange().setValueAt(newVal, rowSel, colSel);
            }
            else
            {

                if(MyTable.getModelRange().getValueAt(rowSel,2).equals("Entrata"))
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelRange().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 0);
                }
                else
                {
                    double tmpImp = Double.parseDouble
                            (MyTable.getModelRange().getValueAt(rowSel,3).toString().substring(4));
                    updateDateBalVisualOption(tmpImp, 1);
                }

                MyTable.getModelRange().removeRow(rowSel);
            }
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            if(MyTable.getTable().getRowCount()<=1)
            {
                setVisualAll();
            }
        }
    }

    /**
     * metodo per la modifica di una descrizione presente in una certa riga e colonna
     * @param rowSel (riga selezionata)
     * @param colSel (colonna selezionata)
     */
    public void updateDescr (int rowSel, int colSel)
    {
        if(MyTable.visSelect==0)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica descrizione",
                    MyTable.getModel().getValueAt(rowSel,colSel).toString());
            if(newVal==null) {
                return;
            }
            if (newVal.length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }
            MyTable.getModel().setValueAt(newVal,rowSel,colSel);
            return;
        }

        int modelIndex;

        if(MyTable.visSelect==1)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica descrizione",
                    MyTable.getModelDay().getValueAt(rowSel,colSel).toString());
            if(newVal==null) {
                return;
            }
            if (newVal.length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelDay().getValueAt(rowSel,0).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,1).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,2).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,3).toString());
            MyTable.getModelDay().setValueAt(newVal, rowSel, colSel);
            if(modelIndex>=0)
            {
                MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            }
            return;
        }

        if(MyTable.visSelect==2)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica descrizione",
                    MyTable.getModelWeek().getValueAt(rowSel,colSel).toString());
            if(newVal==null) {
                return;
            }
            if (newVal.length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelWeek().getValueAt(rowSel,0).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,1).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,2).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,3).toString());
            MyTable.getModelWeek().setValueAt(newVal, rowSel, colSel);
            if(modelIndex>=0)
            {
                MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            }
            return;
        }

        if(MyTable.visSelect==3)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica descrizione",
                    MyTable.getModelMonth().getValueAt(rowSel,colSel).toString());
            if(newVal==null) {
                return;
            }
            if (newVal.length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelMonth().getValueAt(rowSel,0).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,1).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,2).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,3).toString());
            MyTable.getModelMonth().setValueAt(newVal, rowSel, colSel);
            if(modelIndex>=0)
            {
                MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            }
            return;
        }

        if(MyTable.visSelect==4)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica descrizione",
                    MyTable.getModelYear().getValueAt(rowSel,colSel).toString());
            if(newVal==null) {
                return;
            }
            if (newVal.length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelYear().getValueAt(rowSel,0).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,1).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,2).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,3).toString());
            MyTable.getModelYear().setValueAt(newVal, rowSel, colSel);
            if(modelIndex>=0)
            {
                MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            }
            return;
        }

        if(MyTable.visSelect==5)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica descrizione",
                    MyTable.getModelRange().getValueAt(rowSel,colSel).toString());
            if(newVal==null) {
                return;
            }
            if (newVal.length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelRange().getValueAt(rowSel,0).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,1).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,2).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,3).toString());
            MyTable.getModelRange().setValueAt(newVal, rowSel, colSel);
            if(modelIndex>=0)
            {
                MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
            }
        }
    }

    /**
     * metodo per la modifica della tipologia di operazione presente ad una certa riga e colonna
     * @param rowSel (riga selezionata)
     * @param colSel (colonna selezionata)
     */
    public void updateTip (int rowSel, int colSel)
    {
        String[] options = { "Uscita", "Entrata" };
        var optChoose = JOptionPane.showOptionDialog(null, "Entrata o uscita?",
                "Modifica tipologia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options);

        if(MyTable.visSelect==0)
        {
            String oldVal = MyTable.getModel().getValueAt(rowSel,colSel).toString();
            double tmp = Double.parseDouble(MyTable.getModel().getValueAt(rowSel,3).toString()
                    .substring(4));

            if (optChoose == 0 && !oldVal.equals(options[0])) {

                MyTable.balEnt = MyTable.balEnt - tmp;
                MyTable.balUsc = MyTable.balUsc + tmp;
                MyTable.bal = MyTable.balEnt - MyTable.balUsc;
                MyTable.balView.setText(Double.toString(MyTable.bal));
                MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
                MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
                MyTable.getModel().setValueAt(options[0],rowSel,colSel);
                String signTmp = MyTable.getModel().getValueAt(rowSel, 3).toString();
                signTmp = "- " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, rowSel, 3);


            } else if (optChoose == 1 && !oldVal.equals(options[1])) {

                MyTable.balEnt = MyTable.balEnt + tmp;
                MyTable.balUsc = MyTable.balUsc - tmp;
                MyTable.bal = MyTable.balEnt - MyTable.balUsc;
                MyTable.balView.setText(Double.toString(MyTable.bal));
                MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
                MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
                MyTable.getModel().setValueAt(options[1],rowSel,colSel);
                String signTmp = MyTable.getModel().getValueAt(rowSel, 3).toString();
                signTmp = "+ " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, rowSel, 3);
            }
            else {

                JOptionPane.showMessageDialog(null, "Tipologia non modificata");
                return;
            }
        }

        int modelIndex;

        if(MyTable.visSelect==1)
        {
            String oldVal = MyTable.getModelDay().getValueAt(rowSel,colSel).toString();
            double tmp = Double.parseDouble(MyTable.getModelDay().getValueAt(rowSel,3).toString()
                    .substring(4));
            modelIndex = MyTable.findRowIndex(MyTable.getModelDay().getValueAt(rowSel,0).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,1).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,2).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,3).toString());

            if (optChoose == 0 && !oldVal.equals(options[0])) {

                updateBalanceVisualOption(tmp,0);
                MyTable.getModel().setValueAt(options[0],modelIndex,colSel);
                MyTable.getModelDay().setValueAt(options[0],rowSel,colSel);
                String signTmp = MyTable.getModelDay().getValueAt(rowSel, 3).toString();
                signTmp = "- " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelDay().setValueAt(signTmp, rowSel, 3);

            } else if (optChoose == 1 && !oldVal.equals(options[1])) {

                updateBalanceVisualOption(tmp,1);
                MyTable.getModel().setValueAt(options[1],modelIndex,colSel);
                MyTable.getModelDay().setValueAt(options[1],rowSel,colSel);
                String signTmp = MyTable.getModelDay().getValueAt(rowSel, 3).toString();
                signTmp = "+ " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelDay().setValueAt(signTmp, rowSel, 3);
            }
            else {

                JOptionPane.showMessageDialog(null, "Tipologia non modificata");
                return;
            }
        }

        if(MyTable.visSelect==2)
        {
            String oldVal = MyTable.getModelWeek().getValueAt(rowSel,colSel).toString();
            double tmp = Double.parseDouble(MyTable.getModelWeek().getValueAt(rowSel,3).toString()
                    .substring(4));
            modelIndex = MyTable.findRowIndex(MyTable.getModelWeek().getValueAt(rowSel,0).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,1).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,2).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,3).toString());


            if (optChoose == 0 && !oldVal.equals(options[0])) {

                updateBalanceVisualOption(tmp,0);
                MyTable.getModel().setValueAt(options[0],modelIndex,colSel);
                MyTable.getModelWeek().setValueAt(options[0],rowSel,colSel);
                String signTmp = MyTable.getModelWeek().getValueAt(rowSel, 3).toString();
                signTmp = "- " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelWeek().setValueAt(signTmp, rowSel, 3);

            } else if (optChoose == 1 && !oldVal.equals(options[1])) {

                updateBalanceVisualOption(tmp,1);
                MyTable.getModel().setValueAt(options[1],modelIndex,colSel);
                MyTable.getModelWeek().setValueAt(options[1],rowSel,colSel);
                String signTmp = MyTable.getModelWeek().getValueAt(rowSel, 3).toString();
                signTmp = "+ " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelWeek().setValueAt(signTmp, rowSel, 3);
            }
            else {

                JOptionPane.showMessageDialog(null, "Tipologia non modificata");
                return;
            }
        }

        if(MyTable.visSelect==3)
        {
            String oldVal = MyTable.getModelMonth().getValueAt(rowSel,colSel).toString();
            double tmp = Double.parseDouble(MyTable.getModelMonth().getValueAt(rowSel,3).toString()
                    .substring(4));
            modelIndex = MyTable.findRowIndex(MyTable.getModelMonth().getValueAt(rowSel,0).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,1).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,2).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,3).toString());


            if (optChoose == 0 && !oldVal.equals(options[0])) {

                updateBalanceVisualOption(tmp,0);
                MyTable.getModel().setValueAt(options[0],modelIndex,colSel);
                MyTable.getModelMonth().setValueAt(options[0],rowSel,colSel);
                String signTmp = MyTable.getModelMonth().getValueAt(rowSel, 3).toString();
                signTmp = "- " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelMonth().setValueAt(signTmp, rowSel, 3);

            } else if (optChoose == 1 && !oldVal.equals(options[1])) {

                updateBalanceVisualOption(tmp,1);
                MyTable.getModel().setValueAt(options[1],modelIndex,colSel);
                MyTable.getModelMonth().setValueAt(options[1],rowSel,colSel);
                String signTmp = MyTable.getModelMonth().getValueAt(rowSel, 3).toString();
                signTmp = "+ " + signTmp.substring(2);
                MyTable. getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelMonth().setValueAt(signTmp, rowSel, 3);
            }
            else {

                JOptionPane.showMessageDialog(null, "Tipologia non modificata");
                return;
            }
        }

        if(MyTable.visSelect==4)
        {
            String oldVal = MyTable.getModelYear().getValueAt(rowSel,colSel).toString();
            double tmp = Double.parseDouble(MyTable.getModelYear().getValueAt(rowSel,3).toString()
                    .substring(4));
            modelIndex = MyTable.findRowIndex(MyTable.getModelYear().getValueAt(rowSel,0).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,1).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,2).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,3).toString());


            if (optChoose == 0 && !oldVal.equals(options[0])) {

                updateBalanceVisualOption(tmp, 0);
                MyTable.getModel().setValueAt(options[0],modelIndex,colSel);
                MyTable.getModelYear().setValueAt(options[0],rowSel,colSel);
                String signTmp = MyTable.getModelYear().getValueAt(rowSel, 3).toString();
                signTmp = "- " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelYear().setValueAt(signTmp, rowSel, 3);

            } else if (optChoose == 1 && !oldVal.equals(options[1])) {

                updateBalanceVisualOption(tmp, 1);
                MyTable.getModel().setValueAt(options[1],modelIndex,colSel);
                MyTable.getModelYear().setValueAt(options[1],rowSel,colSel);
                String signTmp = MyTable.getModelYear().getValueAt(rowSel, 3).toString();
                signTmp = "+ " + signTmp.substring(2);
                MyTable. getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelYear().setValueAt(signTmp, rowSel, 3);
            }
            else {

                JOptionPane.showMessageDialog(null, "Tipologia non modificata");
                return;
            }
        }

        if(MyTable.visSelect==5)
        {
            String oldVal = MyTable.getModelRange().getValueAt(rowSel,colSel).toString();
            double tmp = Double.parseDouble(MyTable.getModelRange().getValueAt(rowSel,3).toString()
                    .substring(4));
            modelIndex = MyTable.findRowIndex(MyTable.getModelRange().getValueAt(rowSel,0).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,1).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,2).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,3).toString());


            if (optChoose == 0 && !oldVal.equals(options[0])) {

                updateBalanceVisualOption(tmp, 0);
                MyTable.getModel().setValueAt(options[0],modelIndex,colSel);
                MyTable.getModelRange().setValueAt(options[0],rowSel,colSel);
                String signTmp = MyTable.getModelRange().getValueAt(rowSel, 3).toString();
                signTmp = "- " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelRange().setValueAt(signTmp, rowSel, 3);

            } else if (optChoose == 1 && !oldVal.equals(options[1])) {

                updateBalanceVisualOption(tmp, 1);
                MyTable.getModel().setValueAt(options[1],modelIndex,colSel);
                MyTable.getModelRange().setValueAt(options[1],rowSel,colSel);
                String signTmp = MyTable.getModelRange().getValueAt(rowSel, 3).toString();
                signTmp = "+ " + signTmp.substring(2);
                MyTable.getModel().setValueAt(signTmp, modelIndex, 3);
                MyTable.getModelRange().setValueAt(signTmp, rowSel, 3);
            }
            else {

                JOptionPane.showMessageDialog(null, "Tipologia non modificata");
            }
        }
    }

    /**
     * metodo per la modifica di un importo presente in una certa riga e colonna
     * @param rowSel (riga selezionata)
     * @param colSel (colonna selezionata)
     */
    public void updateImp (int rowSel, int colSel)
    {
        if(MyTable.visSelect==0)
        {
            String newVal=JOptionPane.showInputDialog(null,"Modifica importo",
                    MyTable.getModel().getValueAt(rowSel,colSel).toString().substring(4));
            if(newVal==null) {
                return;
            }
            if(!MyTable.isNumeric(newVal)) {

                JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                        "utilizzare il . per separare la parte intera dalla decimale");
                return;
            }

            double tmp = Double.parseDouble(MyTable.getModel().getValueAt(rowSel,colSel).
                    toString().substring(4));

            if(MyTable.getModel().getValueAt(rowSel,2).toString().equals("Entrata")) {

                MyTable.bal=MyTable.bal-tmp+Double.parseDouble(newVal);
                MyTable.balEnt=MyTable.balEnt-tmp+Double.parseDouble(newVal);
                newVal = "+ € " + Double.parseDouble(newVal);
            }
            else {
                MyTable.bal=MyTable.bal+tmp-Double.parseDouble(newVal);
                MyTable.balUsc=MyTable.balUsc-tmp+Double.parseDouble(newVal);
                newVal = "- € " + Double.parseDouble(newVal);
            }
            MyTable.balView.setText(Double.toString(MyTable.bal));
            MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
            MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
            MyTable.getModel().setValueAt(newVal,rowSel,colSel);
        }

        int modelIndex;

        if(MyTable.visSelect==1)
        {

            String newVal=JOptionPane.showInputDialog(null,"Modifica importo",
                    MyTable.getModelDay().getValueAt(rowSel,colSel).toString().substring(4));
            if(newVal==null) {
                return;
            }
            if(!MyTable.isNumeric(newVal)) {

                JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                        "utilizzare il . per separare la parte intera dalla decimale");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelDay().getValueAt(rowSel,0).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,1).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,2).toString(),
                    MyTable.getModelDay().getValueAt(rowSel,3).toString());

            double tmp = Double.parseDouble(MyTable.getModelDay().getValueAt(rowSel,colSel).
                    toString().substring(4));

            if(MyTable.getModelDay().getValueAt(rowSel, 2).toString().equals("Entrata")) {

                updateImpVisualOption(tmp,Double.parseDouble(newVal), 0);
                newVal = "+ € " + Double.parseDouble(newVal);
            }
            else {
                updateImpVisualOption(tmp,Double.parseDouble(newVal), 1);
                newVal = "- € " + Double.parseDouble(newVal);
            }
            MyTable.getModelDay().setValueAt(newVal,rowSel,colSel);
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
        }

        if(MyTable.visSelect==2)
        {

            String newVal=JOptionPane.showInputDialog(null,"Modifica importo",
                    MyTable.getModelWeek().getValueAt(rowSel,colSel).toString().substring(4));
            if(newVal==null) {
                return;
            }
            if(!MyTable.isNumeric(newVal)) {

                JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                        "utilizzare il . per separare la parte intera dalla decimale");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelWeek().getValueAt(rowSel,0).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,1).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,2).toString(),
                    MyTable.getModelWeek().getValueAt(rowSel,3).toString());

            double tmp = Double.parseDouble(MyTable.getModelWeek().getValueAt(rowSel,colSel).
                    toString().substring(4));

            if(MyTable.getModelWeek().getValueAt(rowSel, 2).toString().equals("Entrata")) {

                updateImpVisualOption(tmp,Double.parseDouble(newVal), 0);
                newVal = "+ € " + Double.parseDouble(newVal);
            }
            else {
                updateImpVisualOption(tmp,Double.parseDouble(newVal), 1);
                newVal = "- € " + Double.parseDouble(newVal);
            }
            MyTable.getModelWeek().setValueAt(newVal,rowSel,colSel);
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
        }

        if(MyTable.visSelect==3)
        {

            String newVal=JOptionPane.showInputDialog(null,"Modifica importo",
                    MyTable.getModelMonth().getValueAt(rowSel,colSel).toString().substring(4));
            if(newVal==null) {
                return;
            }
            if(!MyTable.isNumeric(newVal)) {

                JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                        "utilizzare il . per separare la parte intera dalla decimale");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelMonth().getValueAt(rowSel,0).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,1).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,2).toString(),
                    MyTable.getModelMonth().getValueAt(rowSel,3).toString());

            double tmp = Double.parseDouble(MyTable.getModelMonth().getValueAt(rowSel,colSel).
                    toString().substring(4));
            if(MyTable.getModelMonth().getValueAt(rowSel, 2).toString().equals("Entrata")) {

                updateImpVisualOption(tmp,Double.parseDouble(newVal), 0);
                newVal = "+ € " + Double.parseDouble(newVal);
            }
            else {
                updateImpVisualOption(tmp,Double.parseDouble(newVal), 1);
                newVal = "- € " + Double.parseDouble(newVal);
            }
            MyTable.getModelMonth().setValueAt(newVal,rowSel,colSel);
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
        }

        if(MyTable.visSelect==4)
        {

            String newVal=JOptionPane.showInputDialog(null,"Modifica importo",
                    MyTable.getModelYear().getValueAt(rowSel,colSel).toString().substring(4));
            if(newVal==null) {
                return;
            }
            if(!MyTable.isNumeric(newVal)) {

                JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                        "utilizzare il . per separare la parte intera dalla decimale");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelYear().getValueAt(rowSel,0).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,1).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,2).toString(),
                    MyTable.getModelYear().getValueAt(rowSel,3).toString());

            double tmp = Double.parseDouble(MyTable.getModelYear().getValueAt(rowSel,colSel).
                    toString().substring(4));

            if(MyTable.getModelYear().getValueAt(rowSel, 2).toString().equals("Entrata")) {

                updateImpVisualOption(tmp,Double.parseDouble(newVal), 0);
                newVal = "+ € " + Double.parseDouble(newVal);
            }
            else {

                updateImpVisualOption(tmp,Double.parseDouble(newVal), 1);
                newVal = "- € " + Double.parseDouble(newVal);
            }
            MyTable.getModelYear().setValueAt(newVal,rowSel,colSel);
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
        }

        if(MyTable.visSelect==5)
        {

            String newVal=JOptionPane.showInputDialog(null,"Modifica importo",
                    MyTable.getModelRange().getValueAt(rowSel,colSel).toString().substring(4));
            if(newVal==null) {
                return;
            }
            if(!MyTable.isNumeric(newVal)) {

                JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                        "utilizzare il . per separare la parte intera dalla decimale");
                return;
            }
            modelIndex = MyTable.findRowIndex(MyTable.getModelRange().getValueAt(rowSel,0).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,1).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,2).toString(),
                    MyTable.getModelRange().getValueAt(rowSel,3).toString());

            double tmp = Double.parseDouble(MyTable.getModelRange().getValueAt(rowSel,colSel).
                    toString().substring(4));

            if(MyTable.getModelRange().getValueAt(rowSel, 2).toString().equals("Entrata")) {

                updateImpVisualOption(tmp,Double.parseDouble(newVal), 0);
                newVal = "+ € " + Double.parseDouble(newVal);
            }
            else {
                updateImpVisualOption(tmp,Double.parseDouble(newVal), 1);
                newVal = "- € " + Double.parseDouble(newVal);
            }
            MyTable.getModelRange().setValueAt(newVal,rowSel,colSel);
            MyTable.getModel().setValueAt(newVal,modelIndex, colSel);
        }
    }
}
