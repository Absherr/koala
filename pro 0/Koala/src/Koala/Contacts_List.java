/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Koala;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;
import utils.Holder;

/// widok główny

/*
 * !
 * Główne okno programu
 */
public class Contacts_List extends JFrame implements ActionListener {

    public MainFrame frame;
    public JScrollPane userContacts;
    public Contacts contacts;
    public client.Client program;
    public JPopupMenu userContactOptions;
    public JMenuBar menuBar;
    public JPanel panel;
    private JList contactList;
    private JRadioButtonMenuItem invisible;
    private JRadioButtonMenuItem unAvaliable;
    private JRadioButtonMenuItem avaliable;

    public Contacts_List(client.Client logika, JPanel panel, MainFrame frame) {
        super("Lista kontaktów");
        this.panel = panel;
        this.frame = frame;
        this.program = logika;
        initComponent();
        createPanel();
    }

    private void initComponent() {
        contacts = new Contacts();
        contactList = new JList();
        userContacts = new JScrollPane(contactList);
        createPopupMenu();
        refreshContactList(contacts.vec);
    }

    //Tworzy panel okna
    private void createPanel() {
        panel.setLayout(new BorderLayout());
        panel.add(userContacts, BorderLayout.CENTER);
    }

    //Ustawia własciwosci okna
    private void setFrameOptions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Dimension size = new Dimension(screenSize);//rozmiar ramki
        size.width = size.width / 6;
        size.height = (int) (size.height / 1.5);

        setLocation((int) ((screenSize.width / 2) - (size.width / 2)),
                (int) ((screenSize.height / 2) - (size.height / 2)));

        setSize(size);
        add(panel);
        //setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void refreshContactList(ArrayList<utils.Holder> vec) {

        String[] contacts = new String[vec.size()];
        for (int i = 0; i < vec.size(); i++) {
            contacts[i] = vec.get(i).getNazwa();
        }
        Arrays.sort(contacts);
        createContactList(contacts, vec);
    }

    //Tworzy i dodaje liste auktualnioną liste kontaktów
    private void createContactList(String[] contacts_str, ArrayList<Holder> vec) {
        int lvTemptIndex = contactList.getSelectedIndex();
        contactList = new JList(contacts_str);

        contactList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() <= 1) {
                } else if (e.getClickCount() >= 2 && program.getCzyZalgowany() == true
                        && contacts.vec.get(contactList.getSelectedIndex()).getStatus().equalsIgnoreCase(utils.Status.AVALIBLE)) {
                    String text = contacts.Get_Number(contactList.getSelectedValue().toString());
                    if (text.equalsIgnoreCase(frame.getUserId())) {
                        JOptionPane.showMessageDialog(null, "To twój własny numer");
                    } else {
                        try {
                            program.requestTxtToServer(text);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Musisz wybrac kontakt lub dany kontakt jest niedostępny");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        MouseListener popupListener = new Contacts_List.PopupListener(userContactOptions);

        contactList.addMouseListener(popupListener);
        contactList.setCellRenderer(new MyListCellThing(vec));

        panel.remove(userContacts);
        userContacts = new JScrollPane(contactList);
        panel.add(userContacts, BorderLayout.CENTER);
        if (lvTemptIndex == -1) {
            lvTemptIndex = 0;
        }
        contactList.setSelectedIndex(lvTemptIndex);
        frame.revalidate();
        frame.repaint();

    }

    //Tworzy popupmenu
    private void createPopupMenu() {
        JMenuItem menuItem;

        //Create the popup menu.
        userContactOptions = new JPopupMenu();
        menuItem = new JMenuItem("Napisz Wiadomosc");
        menuItem.addActionListener(this);
        userContactOptions.add(menuItem);
        menuItem = new JMenuItem("Zadzwon");
        menuItem.addActionListener(this);
        userContactOptions.add(menuItem);
        menuItem = new JMenuItem("Edytuj kontakt");
        menuItem.addActionListener(this);
        userContactOptions.add(menuItem);
        menuItem = new JMenuItem("Dodaj kontakt");
        menuItem.addActionListener(this);
        userContactOptions.add(menuItem);
        userContactOptions.addSeparator();
        //menuItem = tOptions.add(menuItem);
        menuItem = new JMenuItem("Usun kontakt");
        menuItem.addActionListener(this);
        userContactOptions.add(menuItem);

    }

    /**
     * Nadpisana metoda służaca do obsługi popupmenu
     *
     * @param e wykonana akcja
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Napisz Wiadomosc")) {
            //Rozpoczyna nowe okno rozmowy
            if (contactList.getSelectedValue() != null && program.getCzyZalgowany() == true
                    && contacts.vec.get(contactList.getSelectedIndex()).getStatus().equalsIgnoreCase(utils.Status.AVALIBLE)) {
                String text = contacts.Get_Number(contactList.getSelectedValue().toString());
                if (text.equalsIgnoreCase(frame.getId())) {
                    JOptionPane.showMessageDialog(null, "To twój własny numer");
                } else {
                    try {
                        program.requestTxtToServer(text);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Coś jest nie halo, linijka 208, nie ma go");
            }
        } else if (e.getActionCommand().equals("Edytuj kontakt")) {
            if (contactList.getSelectedValue() != null) {
                Add frame = new Add(contacts.Get_Contact(contactList.getSelectedValue().toString()), this, true);
                utils.Holder con = contacts.Get_Contact(contactList.getSelectedValue().toString());
                frame.setName(con.getNazwa());
                frame.setNumber(con.getId());
                frame.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "Kliknij na kontakt!");

            }
        } else if (e.getActionCommand().equals("Usun kontakt")) {
            if (contactList.getSelectedValue() != null) {
                contacts.Delete(contactList.getSelectedValue().toString());
                refreshContactList(contacts.vec);
            } else {
                JOptionPane.showMessageDialog(null, "Kliknij na kontakt!");
            }
        } else if (e.getActionCommand().equals("Dodaj kontakt")) {
            new Add(null, this, false).setVisible(true);

        } else if (e.getActionCommand().equals("Zadzwon")) {
            //Rozpoczyna nowe okno rozmowy
            if (contactList.getSelectedValue() != null && program.getCzyZalgowany() == true) {
                String text = contacts.Get_Number(contactList.getSelectedValue().toString());
                //==========================================
                //otwiera okno rozmowy, frame do zrobienia
                //==========================================
            } else {
                JOptionPane.showMessageDialog(null, "Musisz sie zarejestrowac lub wybrac kontakt");
            }

        }

    }

    //Klasa słuchacza PopUpMenu
    class PopupListener extends MouseAdapter {

        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }

    public class MyListCellThing extends JLabel implements ListCellRenderer {

        private ArrayList<Holder> vec;

        public MyListCellThing(ArrayList<Holder> pmVec) {
            setOpaque(true);
            this.vec = pmVec;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setFont(new Font("sansserif", Font.BOLD, 18));
            // based on the index you set the color.  This produces the every other effect.
            setBorder(BorderFactory.createLineBorder(Color.black));

            if (this.vec.get(index).getStatus().equalsIgnoreCase(utils.Status.AVALIBLE)) {
                if (isSelected) {
                    setBackground(new Color(100, 255, 100));
                } else {
                    setBackground(Color.GREEN);
                }
            } else {
                if (isSelected) {
                    setBackground(new Color(255, 100, 50));
                } else {
                    setBackground(Color.RED);
                }
            }
            return this;
        }
    }
}
