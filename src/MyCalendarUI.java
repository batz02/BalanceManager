import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

/**
 * classe per l'implementazione del pannello calendario per la gestione delle date
 */

@SuppressWarnings("SpellCheckingInspection") //comando per la rimozione dell'ispezione dei caratteri su intellij
public class MyCalendarUI {

    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    JLabel l = new JLabel("", JLabel.CENTER);
    String day = "";
    JDialog d;
    JButton[] button = new JButton[56];
    String name;

    /**
     * costruttore che crea e definisce l'interfaccia drl calendario e lo scorrimento tra i vari mesi
     * @param parent (jFrame necessario alla visualizzazione)
     * @param name (titolo del frame in base alla situazione in cui serve il calendario)
     */
    public MyCalendarUI(JFrame parent, String name) {

        d = new JDialog();
        d.setModal(true);
        this.name = name;
        String[] header = { "Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom" };
        JPanel p1 = new JPanel(new GridLayout(8, 7));
        p1.setPreferredSize(new Dimension(450, 170));

        for (int x = 0; x < button.length; x++) {
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.white);
            if (x > 6)
                button[x].addActionListener(ae -> {
                    day = button[selection].getActionCommand();
                    d.dispose();
                });
            if (x < 7) {
                button[x].setText(header[x]);
                button[x].setForeground(Color.red);
            }
            p1.add(button[x]);
        }

        JPanel p2 = new JPanel(new GridLayout(1, 3));
        JButton previous = new JButton("<< Precedente");

        previous.addActionListener(ae -> {
            month--;
            displayDate();
        });
        p2.add(previous);
        p2.add(l);

        JButton next = new JButton("Successivo >>");

        next.addActionListener(ae -> {
            month++;
            displayDate();
        });
        p2.add(next);

        d.add(p1, BorderLayout.CENTER);
        d.add(p2, BorderLayout.SOUTH);
        d.pack();
        d.setLocationRelativeTo(parent);
        displayDate();
        d.setVisible(true);
    }

    /**
     * metodo per la visualizzazione del calendario e i bottoni per la scelta del giorno
     */
    public void displayDate() {
        for (int x = 7; x < button.length; x++){
            button[x].setText("");
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "MMMM yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(year, month, 1);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        for (int x = 12 + dayOfWeek, day = 1; day <= (daysInMonth); x++, day++)
            button[x].setText("" + day);

        l.setText(sdf.format(cal.getTime()));
        d.setTitle(name);
    }

    /**
     * metodo per impostare la data scelta con il pattern scelto
     * @return data sottoforma di stringa
     */
    public String setPickedDate() {
        if (day.equals(""))
            return day;
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MM-yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(year, month, Integer.parseInt(day));
        return formatter.format(cal.getTime());
    }
}

