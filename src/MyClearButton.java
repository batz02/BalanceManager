import javax.swing.*;

/**
 * classe che implementa il funzionamento del pulsante Elimina
 */

@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyClearButton {

    /**
     * costruttore che gestisce, in base alla visualizzazione impostata, la rimozione di una riga
     */
    public MyClearButton()
    {
        if(MyTable.getModel().getRowCount()>0) {

            int rowIndex = MyTable.getTable().getSelectedRow();
            if (rowIndex < 0) {
                JOptionPane.showMessageDialog(null, "Selezionare una riga da eliminare");
                return;
            }

            String checktmp;
            double imptmp;

            if (MyTable.visSelect == 0) {
                checktmp = MyTable.getModel().getValueAt(rowIndex, 2).toString();
                imptmp = Double.parseDouble(MyTable.getModel().getValueAt(rowIndex, 3).toString()
                        .substring(4));

                if (checktmp.equals("Entrata")) {

                    MyTable.balEnt = MyTable.balEnt - imptmp;
                    MyTable.bal = MyTable.balEnt - MyTable.balUsc;
                    MyTable.balView.setText(Double.toString(MyTable.bal));
                    MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
                } else {

                    MyTable.balUsc = MyTable.balUsc - imptmp;
                    MyTable.bal = MyTable.balEnt - MyTable.balUsc;
                    MyTable.balView.setText(Double.toString(MyTable.bal));
                    MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
                }
                MyTable.getModel().removeRow(rowIndex);
                return;

            }

            int modelIndex;

            if (MyTable.visSelect == 1) {
                checktmp = MyTable.getModelDay().getValueAt(rowIndex, 2).toString();
                imptmp = Double.parseDouble(MyTable.getModelDay().getValueAt(rowIndex, 3).toString()
                        .substring(4));
                modelIndex = MyTable.findRowIndex(MyTable.getModelDay().getValueAt(rowIndex, 0).toString(),
                        MyTable.getModelDay().getValueAt(rowIndex, 1).toString(),
                        MyTable.getModelDay().getValueAt(rowIndex, 2).toString(),
                        MyTable.getModelDay().getValueAt(rowIndex, 3).toString());

                if (checktmp.equals("Entrata")) {

                    setBalanceVisual(imptmp, 0);
                } else {

                    setBalanceVisual(imptmp, 1);
                }
                MyTable.getModelDay().removeRow(rowIndex);
                if (modelIndex >= 0) {
                    MyTable.getModel().removeRow(modelIndex);
                }
                return;

            }

            if (MyTable.visSelect == 2) {
                checktmp = MyTable.getModelWeek().getValueAt(rowIndex, 2).toString();
                imptmp = Double.parseDouble(MyTable.getModelWeek().getValueAt(rowIndex, 3).toString()
                        .substring(4));
                modelIndex = MyTable.findRowIndex(MyTable.getModelWeek().getValueAt(rowIndex, 0).toString(),
                        MyTable.getModelWeek().getValueAt(rowIndex, 1).toString(),
                        MyTable.getModelWeek().getValueAt(rowIndex, 2).toString(),
                        MyTable.getModelWeek().getValueAt(rowIndex, 3).toString());

                if (checktmp.equals("Entrata")) {

                    setBalanceVisual(imptmp, 0);
                } else {

                    setBalanceVisual(imptmp, 1);
                }
                MyTable.getModelWeek().removeRow(rowIndex);
                if (modelIndex >= 0) {
                    MyTable.getModel().removeRow(modelIndex);
                }
                return;

            }

            if (MyTable.visSelect == 3) {
                checktmp = MyTable.getModelMonth().getValueAt(rowIndex, 2).toString();
                imptmp = Double.parseDouble(MyTable.getModelMonth().getValueAt(rowIndex, 3).toString()
                        .substring(4));
                modelIndex = MyTable.findRowIndex(MyTable.getModelMonth().getValueAt(rowIndex, 0).toString(),
                        MyTable.getModelMonth().getValueAt(rowIndex, 1).toString(),
                        MyTable.getModelMonth().getValueAt(rowIndex, 2).toString(),
                        MyTable.getModelMonth().getValueAt(rowIndex, 3).toString());

                if (checktmp.equals("Entrata")) {

                    setBalanceVisual(imptmp, 0);
                } else {

                    setBalanceVisual(imptmp, 1);
                }
                MyTable.getModelMonth().removeRow(rowIndex);
                if (modelIndex >= 0) {
                    MyTable.getModel().removeRow(modelIndex);
                }
                return;

            }
            if (MyTable.visSelect == 4) {
                checktmp = MyTable.getModelYear().getValueAt(rowIndex, 2).toString();
                imptmp = Double.parseDouble(MyTable.getModelYear().getValueAt(rowIndex, 3).toString()
                        .substring(4));
                modelIndex = MyTable.findRowIndex(MyTable.getModelYear().getValueAt(rowIndex, 0).toString(),
                        MyTable.getModelYear().getValueAt(rowIndex, 1).toString(),
                        MyTable.getModelYear().getValueAt(rowIndex, 2).toString(),
                        MyTable.getModelYear().getValueAt(rowIndex, 3).toString());

                if (checktmp.equals("Entrata")) {

                    setBalanceVisual(imptmp, 0);
                } else {

                    setBalanceVisual(imptmp, 1);
                }
                MyTable.getModelYear().removeRow(rowIndex);
                if (modelIndex >= 0) {
                    MyTable.getModel().removeRow(modelIndex);
                }
                return;

            }
            if (MyTable.visSelect == 5) {
                checktmp = MyTable.getModelRange().getValueAt(rowIndex, 2).toString();
                imptmp = Double.parseDouble(MyTable.getModelRange().getValueAt(rowIndex, 3).toString()
                        .substring(4));
                modelIndex = MyTable.findRowIndex(MyTable.getModelRange().getValueAt(rowIndex, 0).toString(),
                        MyTable.getModelRange().getValueAt(rowIndex, 1).toString(),
                        MyTable.getModelRange().getValueAt(rowIndex, 2).toString(),
                        MyTable.getModelRange().getValueAt(rowIndex, 3).toString());

                if (checktmp.equals("Entrata")) {

                    setBalanceVisual(imptmp, 0);
                } else {
                    setBalanceVisual(imptmp, 1);
                }
                MyTable.getModelRange().removeRow(rowIndex);
                if (modelIndex >= 0) {
                    MyTable.getModel().removeRow(modelIndex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Inserire almeno un'operazione");
            }
        }
    }

    /**
     * metodo per l'aggiornamento del saldo in seguito alla cancellazione di una riga (tranne che per l'opzione tutto)
     * @param imptmp (importo dell'operazione rimossa)
     * @param option (0 per entrata / 1 per uscita)
     */
    public void setBalanceVisual(double imptmp, int option)
    {
        if(option==0)
        {
            MyTable.balEntVisual = MyTable.balEntVisual - imptmp;
            MyTable.balEnt = MyTable.balEnt - imptmp;
            MyTable.bal = MyTable.balEnt - MyTable.balUsc;
            MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
            MyTable.balView.setText(Double.toString(MyTable.balVisual));
            MyTable.balViewEnt.setText(Double.toString(MyTable.balEntVisual));
        }
        else
        {
            MyTable.balUscVisual = MyTable.balUscVisual - imptmp;
            MyTable.balUsc = MyTable.balUsc - imptmp;
            MyTable.bal = MyTable.balEnt - MyTable.balUsc;
            MyTable.balVisual = MyTable.balEntVisual - MyTable.balUscVisual;
            MyTable.balView.setText(Double.toString(MyTable.balVisual));
            MyTable.balViewUsc.setText(Double.toString(MyTable.balUscVisual));
        }
    }
}
