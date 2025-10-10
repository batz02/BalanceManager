import javax.swing.*;

/**
 * classe che implementa la ricerca sulla tabella nella visualizzazione corrente
 */

@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyFind {

    /**
     * costruttore che gestisce la ricerca in base all'opzione scelta
     * @param select (0 se viene cliccato il pulsante Cerca / 1 se viene cliccato il pulsante Successivo)
     */
    public MyFind(int select){

        int nRow=getNRow();
        int nCol=MyTable.getModel().getColumnCount();

        if(select==0)
        {

            if(MyTable.nextRow!=0)
            {
                JOptionPane.showMessageDialog(null, "Per proseguire la ricerca premere il " +
                        "pulsante Successivo");
                return;
            }

            if(MyTable.nextRow==0 && MyTable.resFindEnd)
            {
                JOptionPane.showMessageDialog(null, "Non ci sono ulteriori risultati");
                MyTable.resFindEnd=false;
                MyTable.getTable().removeRowSelectionInterval(0, nRow-1);
                MyTable.strFind.setText("");
                return;
            }

            MyTable.nextRow=0;
            if(nRow==0)
            {
                JOptionPane.showMessageDialog(null, "Inserire almeno un'operazione");
                return;
            }
            if(MyTable.strFind.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Inserire testo da ricercare");
                MyTable.getTable().removeRowSelectionInterval(0, nRow-1);
                return;
            }
            if (MyTable.strFind.getText().length() > 40) {
                JOptionPane.showMessageDialog(null, "Testo troppo lungo - " +
                        "massimo 40 caratteri");
                return;
            }

            findStr(nRow,nCol);
            return;
        }

        if(select==1)
        {
            if(MyTable.strFind.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Per avviare la ricerca premere" +
                        " il pulsante Cerca");
                return;
            }

            findNextStr(nRow,nCol);
        }
    }

    /**
     * metodo per ottenere il numero di righe della tabella visualizzata
     * @return (numero di righe)
     */
    public int getNRow()
    {
        if(MyTable.visSelect==0)
        {
            return MyTable.getModel().getRowCount();
        }
        if(MyTable.visSelect==1)
        {
            return MyTable.getModelDay().getRowCount();
        }
        if(MyTable.visSelect==2)
        {
            return MyTable.getModelWeek().getRowCount();
        }
        if(MyTable.visSelect==3)
        {
            return MyTable.getModelMonth().getRowCount();
        }
        if(MyTable.visSelect==4)
        {
            return MyTable.getModelYear().getRowCount();
        }
        if(MyTable.visSelect==5)
        {
            return MyTable.getModelMonth().getRowCount();
        }
        return -1;
    }

    /**
     * metodo per ottenere la stringa da cercare in base alla visualizzazione corrente
     * @param i (numero riga)
     * @param j (numero colonna)
     * @return (stringa presente alla riga i,j nella tabella visualizzata)
     */
    public String getValueModel (int i, int j)
    {
        if(MyTable.visSelect==0)
        {
            return MyTable.getModel().getValueAt(i,j).toString().toLowerCase();
        }
        if(MyTable.visSelect==1)
        {
            return MyTable.getModelDay().getValueAt(i,j).toString().toLowerCase();
        }
        if(MyTable.visSelect==2)
        {
            return MyTable.getModelWeek().getValueAt(i,j).toString().toLowerCase();
        }
        if(MyTable.visSelect==3)
        {
            return MyTable.getModelMonth().getValueAt(i,j).toString().toLowerCase();
        }
        if(MyTable.visSelect==4)
        {
            return MyTable.getModelYear().getValueAt(i,j).toString().toLowerCase();
        }
        if(MyTable.visSelect==5)
        {
            return MyTable.getModelRange().getValueAt(i,j).toString().toLowerCase();
        }
        return "";
    }

    /**
     * metodo per la ricerca del primo risultato all'interno della tabella visualizzata
     * @param nRow (numero delle righe della tabella)
     * @param nCol (numero delle colonne della tabella)
     */
    public void findStr(int nRow, int nCol)
    {
        MyTable.getTable().removeRowSelectionInterval(0, nRow-1);
        for (int i = 0;i<nRow;i++)
        {
            for (int j=0;j<nCol;j++)
            {
                String tmp = getValueModel(i,j);
                if(nCol==3)
                {
                    tmp=tmp.substring(4);
                }
                if(tmp.contains(MyTable.strFind.getText()))
                {
                    if((i+1)!=nRow)
                    {
                        MyTable.nextRow=i+1;
                    }
                    else
                    {
                        MyTable.resFindEnd=true;
                    }
                    MyTable.getTable().setRowSelectionInterval(i,i);
                    MyTable.findUse=true;
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Nessun risultato trovato");
        MyTable.strFind.setText("");
    }

    /**
     * metodo per la ricerca degli eventuali successivi risultati nella tabella visualizzata
     * @param nRow (numero delle righe della tabella)
     * @param nCol (numero delle colonne della tabella)
     */
    public void findNextStr(int nRow, int nCol)
    {
        if(MyTable.nextRow==0 && !MyTable.findUse)
        {
            JOptionPane.showMessageDialog(null,"Per avviare la ricerca premere il " +
                    "pulsante Cerca");
            return;
        }

        if(MyTable.nextRow!=0)
        {
            MyTable.getTable().removeRowSelectionInterval(0, nRow-1);
            for (int i = MyTable.nextRow;i<nRow;i++)
            {
                for (int j=0;j<nCol;j++)
                {
                    String tmp = getValueModel(i,j);
                    if(nCol==3)
                    {
                        tmp=tmp.substring(4);
                    }
                    if(tmp.contains(MyTable.strFind.getText()))
                    {
                        if((i+1)!=nRow)
                        {
                            MyTable.nextRow=i+1;
                        }
                        else
                        {
                            MyTable.nextRow=0;
                        }
                        MyTable.getTable().setRowSelectionInterval(i,i);
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Non ci sono ulteriori risultati");
            MyTable.nextRow=0;
            MyTable.findUse=false;
            MyTable.strFind.setText("");
        }
        else {
            JOptionPane.showMessageDialog(null, "Non ci sono ulteriori risultati");
            MyTable.nextRow=0;
            MyTable.getTable().removeRowSelectionInterval(0, nRow-1);
            MyTable.resFindEnd=false;
            MyTable.findUse=false;
            MyTable.strFind.setText("");

        }
    }
}
