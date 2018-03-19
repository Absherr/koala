package komunikator;

/**
 *
 * @author ADAM
 */

import client.Client;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import utils.Holder;
/**
 * Główne okno programu
 */
public class ListaKontaktowPanel extends JFrame implements ActionListener {
    private Glowne frame;
    public JScrollPane userContacts;
    public ListaKontaktow contacts;
    
    public Client mKlient;
    
    public JPopupMenu userContactOptions;
    
    public JMenuBar menuBar;
    private JPanel panel;
    private JList contactList;
    private JRadioButtonMenuItem invisible;
    private JRadioButtonMenuItem unAvaliable;
    private JRadioButtonMenuItem avaliable;
     

    public ListaKontaktowPanel(Client pmKlinet, JPanel pmPanel, Glowne pmFrame) {
	super("Lista kontaktow");
        this.mKlient = pmKlinet;
        this.panel = pmPanel;
        this.frame = pmFrame;
	initComponent();
	createPanel();
    }

    //Inicjalizacja komponentów
    private void initComponent() {
	contacts = new ListaKontaktow();
	contactList = new JList();
	userContacts = new JScrollPane(contactList);
	//createMenuBar();
	createPopupMenu();
        refreshContactList(new ListaKontaktow().vec);
        contactList.setFont(new Font("sansserif", Font.BOLD, 24));
    }

    //Tworzy panel okna
    private void createPanel() {
	panel.setLayout(new BorderLayout());
	panel.add(userContacts, BorderLayout.CENTER);
	//panel.add(menuBar, BorderLayout.NORTH);
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
	getContentPane().add(panel);
	//setVisible(true);
	//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Funkcja odświeżająca liste kontaktów
     * @param contacts tablica kontaktów
     */
    public void refreshContactList(Vector<Holder> vec) {
        
        String[] contacts = new String[vec.size()];
        for(int i = 0;i < vec.size();i++)
        {
            contacts[i] = vec.get(i).getNazwa();
        }
	createContactList(contacts,vec);
    }

    //Tworzy i dodaje liste auktualnioną liste kontaktów
    private void createContactList(String[] contacts_str, Vector<Holder> vec) {
        int lvTemptIndex = contactList.getSelectedIndex();
	contactList = new JList(contacts_str);
                
	contactList.addMouseListener(new MouseListener() {
	    
	    @Override
	   public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()<=1)
                {
                }else
		if (e.getClickCount() >= 2  && mKlient.getCzyZalgowany() == true &&
                    contacts.vec.get(contactList.getSelectedIndex()).getStatus().equalsIgnoreCase(utils.Status.AVALIBLE)) 
                {  
                    String text = contacts.Get_Number(contactList.getSelectedValue().toString());
                if(text.equalsIgnoreCase(frame.mId))
                {
                    JOptionPane.showMessageDialog(null, "To twój własny numer");
                }else
                {
                    try {
                        mKlient.requestTxtToServer(text);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
		}
                else 
                {
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
	
	MouseListener popupListener = new ListaKontaktowPanel.PopupListener(userContactOptions);
	
	contactList.addMouseListener(popupListener);
	contactList.setCellRenderer(new MyListCellThing(vec));
        
	panel.remove(userContacts);
	//contactList.setCellRenderer(new ContactListCellRender());
	userContacts = new JScrollPane(contactList);
        
	panel.add(userContacts, BorderLayout.CENTER);
	
        //this.rev
        if(lvTemptIndex == -1) lvTemptIndex = 0;
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
	
	if (e.getActionCommand().equals("Napisz Wiadomosc")) 
        {
	    //Rozpoczyna nowe okno rozmowy
	    if (contactList.getSelectedValue() != null && mKlient.getCzyZalgowany() == true &&
                    contacts.vec.get(contactList.getSelectedIndex()).getStatus().equalsIgnoreCase(utils.Status.AVALIBLE)) 
            { 
		String text = contacts.Get_Number(contactList.getSelectedValue().toString());
                if(text.equalsIgnoreCase(frame.mId))
                {
                    JOptionPane.showMessageDialog(null, "To twój własny numer");
                }else
                {
                    try {
                        mKlient.requestTxtToServer(text);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                
//     
            }else 
            {
		    JOptionPane.showMessageDialog(null, "Musisz wybrac kontakt lub dany kontakt jest niedostępny");
            }
	} else if (e.getActionCommand().equals("Edytuj kontakt")) 
        {
	    if (contactList.getSelectedValue() != null) 
            {
		EdytujKontakt frame = new EdytujKontakt(contacts.Get_Contact(contactList.getSelectedValue().toString()),
                        true,this);
                frame.setVisible(true);
                
	    }else
            {
                 JOptionPane.showMessageDialog(null, "Kliknij na kontakt!");

            }
	} else if (e.getActionCommand().equals("Usun kontakt")) 
        {
	    if (contactList.getSelectedValue() != null) {
		contacts.Delete(contactList.getSelectedValue().toString());
                refreshContactList(contacts.vec);
	    }else
            {
                 JOptionPane.showMessageDialog(null, "Kliknij na kontakt!");

            }
	} else if (e.getActionCommand().equals("Dodaj kontakt")) 
        {
             new EdytujKontakt(contacts.Get_Contact(contactList.getSelectedValue().toString()),false,this).setVisible(true);
       
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

        private Vector<Holder> vec;
        
        public MyListCellThing(Vector<Holder> pmVec) {
            setOpaque(true);
            this.vec = pmVec;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Assumes the stuff in the list has a pretty toString
            setText(value.toString());
            setFont(new Font("sansserif", Font.BOLD, 24));
            // based on the index you set the color.  This produces the every other effect.
            setBorder(BorderFactory.createLineBorder(Color.black));
            
            if(this.vec.get(index).getStatus().equalsIgnoreCase(utils.Status.AVALIBLE))
            {
                if(isSelected)
                {
                    setBackground(new Color(100, 255, 100));
                }else
                {
                    setBackground(Color.GREEN);
                }
            }else
            {
                if(isSelected)
                {
                  setBackground(new Color(255, 100, 50));  
                }else
                {
                    setBackground(Color.RED);
                }
            }
            return this;
        }
}
}


