import javax.swing.*;
import java.text.ParseException;
import java.util.Date;

/**
 * classe che implementa il funzionamento del pulsante Aggiungi
 */
@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyAddButton {

    Date dateNow = new Date();

    /**
     * costruttore che gestisce la data scelta dal calendario e che richiama i metodi per l'aggiunta di una riga
     */
    public MyAddButton(){

        if(!errorMessage())
        {
            return;
        }

        Date tmp;
        try {
            tmp = MyTable.formatter.parse(MyTable.text1.getText());
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        if(tmp.compareTo(dateNow)>0 )
        {
            MyTable.text1.setText(MyTable.formatter.format(dateNow));
            JOptionPane.showMessageDialog(null, "Inserire una data precedente " +
                    "a quella odierna");
            return;
        }

        add();
    }


    /**
     * metodo per la gestione delle situazioni di errore sulla descrizione, importo e scelta del tipo di operazione
     * @return vero se non sono stati trovati errori nella scrittura dei dati, falso altrimenti
     */
    public boolean errorMessage()
    {
        if(MyTable.text2.getText().equals("") || MyTable.text3.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Campo descrizione o importo vuoti");
            return false;
        }

        if(!MyTable.isNumeric(MyTable.text3.getText())) {
            JOptionPane.showMessageDialog(null, "Inserire un importo valido o " +
                    "utilizzare il . per separare la parte intera dalla decimale");
            MyTable.text3.setText("");
            return false;
        }

        if ((MyTable.text2.getText()).length() > 40) {
            JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                    "massimo 40 caratteri");
            MyTable.text2.setText(MyTable.text2.getText().substring(0,40));
            return false;
        }
        return true;
    }

    /**
     * metodo che implementa l'aggiunta di una riga con i relativi controlli sul tipo di operazione inserita
     */
    public void add()
    {
        Date dateNow = new Date();

        if(MyTable.c1.isSelected()) {

            MyTable.balEnt = MyTable.balEnt + Double.parseDouble(MyTable.text3.getText());
            MyTable.bal =MyTable.balEnt - MyTable.balUsc;

            if(MyTable.visSelect==0)
            {
                MyTable.balView.setText(Double.toString(MyTable.bal));
                MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
            }

            if(MyTable.visSelect!=0 && MyTable.checkView(MyTable.text1.getText()))
            {
                MyTable.balEntVisual = MyTable.balEntVisual + Double.parseDouble(MyTable.text3.getText());
                MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
                MyTable.balView.setText(Double.toString(MyTable.balVisual));
                MyTable.balViewEnt.setText(Double.toString(MyTable.balEntVisual));
            }

            String impView = "+ € " + MyTable.text3.getText();
            MyTable. getModel().addRow( new Object[]{ MyTable.text1.getText(), MyTable.text2.getText(), "Entrata", impView});
            if(MyTable.checkView(MyTable.text1.getText()))
            {
                MyTable.addRowVisual(MyTable.text1.getText(), MyTable.text2.getText(), "Entrata", impView);
            }

            MyTable.text1.setText(MyTable.formatter.format(dateNow));
            MyTable.text2.setText("");
            MyTable.text3.setText("");
            MyTable.bg.clearSelection();

        } else if (MyTable.c2.isSelected()) {

            MyTable.balUsc = MyTable.balUsc + Double.parseDouble(MyTable.text3.getText());
            MyTable.bal = MyTable.balEnt - MyTable.balUsc;

            if(MyTable.visSelect==0)
            {
                MyTable.balView.setText(Double.toString(MyTable.bal));
                MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
            }

            if(MyTable.visSelect!=0 && MyTable.checkView(MyTable.text1.getText()))
            {
                MyTable.balUscVisual = MyTable.balUscVisual + Double.parseDouble(MyTable.text3.getText());
                MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
                MyTable.balView.setText(Double.toString(MyTable.balVisual));
                MyTable.balViewUsc.setText(Double.toString(MyTable.balUscVisual));
            }

            String impView = "- € " + MyTable.text3.getText();
            MyTable.getModel().addRow( new Object[]{ MyTable.text1.getText(), MyTable.text2.getText(), "Uscita", impView});
            if(MyTable.checkView(MyTable.text1.getText()))
            {
                MyTable.addRowVisual(MyTable.text1.getText(), MyTable.text2.getText(), "Uscita", impView);
            }

            MyTable.text1.setText(MyTable.formatter.format(dateNow));
            MyTable.text2.setText("");
            MyTable.text3.setText("");
            MyTable.bg.clearSelection();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Selezionare entrata o uscita");
        }
    }
}
