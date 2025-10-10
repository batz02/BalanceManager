import org.jopendocument.dom.Length;
import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.io.File;

/**
 * classe per la gestione del menu File, nel quale è possibile scegliere una delle varie opzioni di salvataggio,
 * caricamento o esportazione della tabella
 */
@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyMenu {

    JMenu menu, submenu;

    /**
     * opzione per il salvataggio in un file binario
     */
    public JMenuItem i1;
    /**
     * opzione per il caricamento da file binario
     */
    public JMenuItem i2;
    /**
     * opzione per la stampa
     */
    public JMenuItem i3;
    /**
     * opzione per l'esportazione in CSV
     */
    public JMenuItem i4;
    /**
     * opzione per l'esportazione in formato testo
     */
    public JMenuItem i5;
    /**
     * opzione per l'esportazione in Open Document
     */
    public JMenuItem i6;
    JMenuBar mb=new JMenuBar();

    /**
     * costruttore per l'implementazione della jMenuBar e le funzionalità legate alle varie opzioni
     * @param app (jFrame principale proveniente dal main)
     */
    public MyMenu(JFrame app) {

        menu = new JMenu("File");
        submenu = new JMenu("Esporta");
        i1 = new JMenuItem("Salva...");
        i2 = new JMenuItem("Carica");
        i3 = new JMenuItem("Stampa");
        i4 = new JMenuItem("CSV (.csv)");
        i5 = new JMenuItem("Testo (.txt)");
        i6 = new JMenuItem("Open Document (.ods)");

        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        submenu.add(i4);
        submenu.add(i5);
        submenu.add(i6);

        menu.add(submenu);
        mb.add(menu);
        app.setJMenuBar(mb);

        i1.addActionListener(e -> save());

        i2.addActionListener(e -> load());

        i3.addActionListener(e -> print());
        
        i4.addActionListener(e -> exportCsv());

        i5.addActionListener(e -> exportTxt());

        i6.addActionListener(e -> exportOds());

    }

    /**
     * metodo per la stampa della tabella principale (con anteprima di stampa)
     */
    public void print()
    {
        DefaultTableModel tmp=MyTable.getModel();
        if(tmp.getRowCount()==0)
        {
            JOptionPane.showMessageDialog(null,"Inserire almeno un'operazione");
            return;
        }
        new MyPrintUI(tmp).setVisible(true);
    }

    /**
     * metodo per il salvataggio della tabella principale in un file binario
     */
    public void save(){
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva come...");
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath() + ".dat";

            int index = filePath.lastIndexOf('/');
            File f = new File(filePath.substring(0, index));
            File[] files = f.listFiles();

            for (int i = 0; i < files.length; i++) {
                if ((files[i].getAbsolutePath().equals(filePath))) {
                    int s = JOptionPane.showConfirmDialog(null, "Sovrasrivere?");
                    if (s == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
            }

            FileOutputStream file ;
            try {
                file = new FileOutputStream(filePath);
            }
            catch (IOException h)
            {
                return;
            }
            DataOutputStream os= new DataOutputStream(file);

            DefaultTableModel tmp=MyTable.getModel();

            try {
                os.writeInt(tmp.getRowCount());
                for (int i=0;i< tmp.getRowCount();i++)
                {
                    for (int j=0;j< tmp.getColumnCount();j++)
                    {
                        String tmpStr = tmp.getValueAt(i,j).toString();
                        switch (j){
                            case 0:
                                os.writeBytes(tmpStr);
                                break;
                            case 1:
                                int length = tmpStr.length();
                                int diff = 40 - length;
                                os.writeInt(length);
                                for (int sp=0;sp<diff;sp++)
                                {
                                    tmpStr=tmpStr+" ";
                                }
                                os.writeBytes(tmpStr);
                                break;
                            case 2:
                                if(tmpStr.equals("Entrata"))
                                {
                                    os.writeChar('e');
                                }
                                else
                                {
                                    os.writeChar('u');
                                }
                                break;
                            case 3:
                                tmpStr=tmpStr.substring(4);
                                double val = Double.parseDouble(tmpStr);
                                os.writeDouble(val);
                                break;
                        }
                    }
                }
                os.close();

            }
            catch (IOException g) {
                throw new RuntimeException(g);
            }

        }
    }

    /**
     * metodo per il caricamento del contenuto presente all'interno di un file binario
     */
    public void load(){

        if(MyTable.getModel().getRowCount()>0)
        {
            int s=JOptionPane.showConfirmDialog(null,"Vuoi salvare la tabella attuale?");
            if(s==JOptionPane.YES_OPTION){
                save();
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File fileToOpen=fileChooser.getSelectedFile();
        if(fileToOpen==null)
        {
            return;
        }

        String filePath = fileToOpen.getAbsolutePath();

        int index = filePath.lastIndexOf('.');

        if(!filePath.substring(index).equals(".dat"))
        {
            JOptionPane.showMessageDialog(null, "Scegliere un file di estensione .dat");
            return;
        }

        FileInputStream fin ;
        try {
            fin = new FileInputStream(fileToOpen);
        }
        catch (FileNotFoundException x)
        {
            return;
        }

        DataInputStream is = new DataInputStream(fin);

        try {
            int nRow = is.readInt();
            if(nRow<=0)
            {
                JOptionPane.showMessageDialog(null,"File vuoto");
                return;
            }
            else
            {
                MyTable.resetModel();
            }

            for(int i=0;i<nRow;i++)
            {
                String text1 = new String(is.readNBytes(10));
                int length = is.readInt();
                String text2 = new String(is.readNBytes(40));
                text2=text2.substring(0,length);
                char text3Tmp = is.readChar();
                double impTmp = is.readDouble();
                String text3;
                String imp;
                if(text3Tmp=='e')
                {
                    text3="Entrata";
                    imp = "+ € ";
                }
                else
                {
                    text3="Uscita";
                    imp = "- € ";
                }
                imp = imp + impTmp;
                MyTable.updateModel(text1,text2, text3, imp);
            }

            if(MyTable.visSelect!=0)
            {
                MyTable.visualizer.setText("Tutto");
                MyTable.dateVisualizer.setText("");
                MyTable.visSelect=0;
                MyTable.balView.setText(Double.toString(MyTable.bal));
                MyTable.balViewEnt.setText(Double.toString(MyTable.balEnt));
                MyTable.balViewUsc.setText(Double.toString(MyTable.balUsc));
                MyTable.getTable().setModel(MyTable.getModel());
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * metodo per l'esportazione della tabella principale in un file di tipo CSV (comma separated values)
     */
    public void exportCsv(){

        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Esporta in CSV");

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath()+".csv";

            int index=filePath.lastIndexOf('/');
            File f = new File(filePath.substring(0,index));
            File[] files = f.listFiles();

            for (int i = 0; i < files.length; i++) {
                if((files[i].getAbsolutePath().equals(filePath)))
                {
                    int s=JOptionPane.showConfirmDialog(null,"Sovrasrivere?");
                    if(s==JOptionPane.NO_OPTION){
                        return;
                    }
                }
            }


            Writer writer = null;
            DefaultTableModel dtm = MyTable.getModel();
            int nRow = dtm.getRowCount();
            int nCol = dtm.getColumnCount();
            try {

                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

                StringBuffer bufferHeader = new StringBuffer();
                for (int j = 0; j < nCol; j++) {
                    bufferHeader.append(dtm.getColumnName(j));
                    if (j != (nCol-1)) bufferHeader.append(", ");
                }
                try {
                    writer.write(bufferHeader + "\r\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                for (int i = 0; i < nRow; i++) {
                    StringBuffer buffer = new StringBuffer();
                    for (int j = 0; j < nCol; j++) {
                        buffer.append(dtm.getValueAt(i, j));
                        if (j != (nCol-1)) buffer.append(", ");
                    }
                    writer.write(buffer + "\r\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * metodo per l'esportazione della tabella principale in formato testo
     */
    public void exportTxt()
    {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Esporta in TXT");

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath() + ".txt";

            int index = filePath.lastIndexOf('/');
            File f = new File(filePath.substring(0, index));
            File[] files = f.listFiles();

            for (int i = 0; i < files.length; i++) {
                if ((files[i].getAbsolutePath().equals(filePath))) {
                    int s = JOptionPane.showConfirmDialog(null, "Sovrasrivere?");
                    if (s == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
            }

            Writer writer = null;
            DefaultTableModel dtm = MyTable.getModel();
            int nRow = dtm.getRowCount();
            int nCol = dtm.getColumnCount();
            try {

                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

                StringBuffer bufferHeader = new StringBuffer();
                for (int j = 0; j < nCol; j++) {
                    bufferHeader.append(dtm.getColumnName(j));
                    if (j != (nCol - 1))
                    {
                        bufferHeader.append("\t");
                    }


                }
                try {
                    writer.write(bufferHeader + "\r\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                for (int i = 0; i < nRow; i++) {
                    StringBuffer buffer = new StringBuffer();
                    for (int j = 0; j < nCol; j++) {
                        buffer.append(dtm.getValueAt(i, j));
                        if (j != (nCol - 1))
                        {
                            buffer.append("\t");
                        }
                    }
                    writer.write(buffer + "\r\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * metodo per l'esportazione della tabella principale in un file Open Document (Libre Office)
     */
    public void exportOds() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Esporta in Open Document");

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath() + ".ods";

            File f = new File(filePath);
            DefaultTableModel dtm = MyTable.getModel();

            int index = filePath.lastIndexOf('/');
            File testF = new File(filePath.substring(0, index));

            File[] files = testF.listFiles();

            for (int i = 0; i < files.length; i++) {
                if ((files[i].getAbsolutePath().equals(filePath))) {
                    int s = JOptionPane.showConfirmDialog(null, "Sovrasrivere?");
                    if (s == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
            }
            try {
                SpreadSheet odsSheet = SpreadSheet.createEmpty(dtm);
                odsSheet.getFirstSheet().getColumn(1).setWidth(Length.MM(85));
                odsSheet.getFirstSheet().getColumn(3).setWidth(Length.MM(40));
                odsSheet.saveAs(f);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            try {
                OOUtils.open(f);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
