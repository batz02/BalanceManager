import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

/**
 * classe che estende Thread per la gestione del salvataggio automatico
 */
@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyAutoSave extends Thread {

    /**
     * metodo per avviare il funzionamento del Thread con un tempo impostato a 90 secondi, dove ogni
     * volta viene salvato un file temporaneo
     */
    @Override
    public void run() {
        while (true) {
           
            createFile();

            try {
                Thread.sleep(90*1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * metodo per la creazione del file temporaneo contenente la tabella principale
     */
    public void createFile()
    {
        try {

            Path tempFile = Files.createTempFile("tempFile", null);
            System.out.println("File temporaneo salvato: "+tempFile);

            DefaultTableModel dtm = MyTable.getModel();
            int nRow = dtm.getRowCount();

            for (int i=0;i<nRow;i++)
            {
                List<String> content = Arrays.asList("Data: "+dtm.getValueAt(i,0).toString(),
                        "Descrizione: "+dtm.getValueAt(i,1).toString(),
                        "Tipologia: "+dtm.getValueAt(i,2).toString(),
                        "Importo: "+dtm.getValueAt(i,3).toString(), " ");
                Files.write(tempFile, content, StandardOpenOption.APPEND);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}