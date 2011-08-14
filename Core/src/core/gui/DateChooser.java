package core.gui;
import core.system.Startup;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
public class DateChooser extends JDialog
        implements ItemListener, MouseListener, FocusListener, KeyListener, ActionListener {
    /** Names of the months. */
    private static final String[] MONTHS =
            new String[] {
        "Januari",
        "Februari",
        "Maret",
        "April",
        "Mei",
        "Juni",
        "Juli",
        "Augustus",
        "September",
        "Oktober",
        "November",
        "Desember"
    };
    
    /** Names of the days of the week. */
    private static final String[] DAYS =
            new String[] {
        "Min",
        "Sen",
        "Sel",
        "Rab",
        "Kam",
        "Jum",
        "Sab"
    };
    private static final Color WEEK_DAYS_FOREGROUND = Color.black;
    private static final Color DAYS_FOREGROUND = Color.blue;
    private static final Color SELECTED_DAY_FOREGROUND = Color.white;
    private static final Color SELECTED_DAY_BACKGROUND = Color.blue;
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1,1,1,1);
    private static final Border FOCUSED_BORDER = BorderFactory.createLineBorder(Color.yellow,1);
    private static final int FIRST_YEAR = 1900;
    private static final int LAST_YEAR = 2100;

    private GregorianCalendar calendar;
    private JLabel[][] days;
    
    /** Day selection control. It is just a panel that can receive the
     * focus. The actual user interaction is driven by the
     * <code>DateChooser</code> class. */
    private FocusablePanel daysGrid;
    
    private JComboBox month;
    private JComboBox year;
    private JButton ok;
    private JButton cancel;
    
    /** Day of the week (0=Sunday) corresponding to the first day of
     * the selected month. Used to calculate the position, in the
     * calendar ({@link #days}), corresponding to a given day. */
    private int offset;
    
    /** Last day of the selected month. */
    private int lastDay;
    
    private JLabel day;
    
    /** <code>true</code> if the "Ok" button was clicked to close the
     * dialog box, <code>false</code> otherwise. */
    private boolean okClicked;
    
    private Date date=new Date();
    
    public DateChooser(Window parent) {
        super(parent,DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
        construct();
        today();
        update();
    }
    
    private void construct() {
        calendar = new GregorianCalendar();
        
        month = new JComboBox(MONTHS);
        month.addItemListener( this );
        
        year = new JComboBox();
        for ( int i=FIRST_YEAR; i<=LAST_YEAR; i++ )
            year.addItem( Integer.toString(i) );
        year.addItemListener( this );
        
        days = new JLabel[7][7];
        for ( int i=0; i<7; i++ ) {
            days[0][i] = new JLabel(DAYS[i],JLabel.RIGHT);
            days[0][i].setForeground( WEEK_DAYS_FOREGROUND );
        }
        int today=1;
        for ( int i=1; i<7; i++ ){
            for ( int j=0; j<7; j++ ) {
                days[i][j] = new JLabel("",JLabel.RIGHT);
                days[i][j].setForeground( DAYS_FOREGROUND );
                days[i][j].setBackground(SELECTED_DAY_BACKGROUND);
                days[i][j].setBorder( EMPTY_BORDER );
                days[i][j].addMouseListener( this );
                today++;
            }
            
        }
        ok = new JButton("Done");
        ok.addActionListener( this );
        cancel = new JButton("Cancel");
        cancel.addActionListener( this );
        
        JPanel monthYear = new JPanel();
        monthYear.add( month );
        monthYear.add( year );
        
        daysGrid = new FocusablePanel(new GridLayout(7,7,5,0));
        daysGrid.addFocusListener( this );
        daysGrid.addKeyListener( this );
        for ( int i=0; i<7; i++ ){
            for ( int j=0; j<7; j++ ){
                daysGrid.add( days[i][j] );
            }
        }
        daysGrid.setBackground( Color.white );
        daysGrid.setBorder( BorderFactory.createLoweredBevelBorder());
        JPanel daysPanel = new JPanel();
        daysPanel.add( daysGrid );
        
        JPanel buttons = new JPanel();
        buttons.add( ok );
        buttons.add( cancel );
        JPanel bgPanel = new javax.swing.JPanel();
        bgPanel.setBackground(Color.WHITE);
        
        Container dialog = getContentPane();
        bgPanel.add(monthYear,java.awt.BorderLayout.CENTER);
        bgPanel.add(daysPanel,java.awt.BorderLayout.WEST );
        bgPanel.add(buttons,java.awt.BorderLayout.EAST );
        
        pack();
        setResizable( false );
        getContentPane().add(bgPanel, java.awt.BorderLayout.CENTER);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-264)/2, (screenSize.height-270)/2, 264, 270);
    }
    
    
    private int getSelectedDay() {
        if ( day == null )
            return -1 ;
        try {
            return Integer.parseInt(day.getText());
        } catch ( NumberFormatException e ) {
        }
        return -1;
    }
    
    public boolean isDone(){
        return okClicked;
    }
    private void setSelected( JLabel newDay ) {
        if ( day != null ) {
            day.setForeground( DAYS_FOREGROUND );
            day.setOpaque( false );
            day.setBorder( EMPTY_BORDER );
        }
        day = newDay;
        day.setForeground( SELECTED_DAY_FOREGROUND );
        day.setOpaque( true );
        if ( daysGrid.hasFocus() )
            day.setBorder( FOCUSED_BORDER );
    }
    
    private void setSelected( int newDay ) {
        setSelected( days[(newDay+offset-1)/7+1][(newDay+offset-1)%7] );
    }
    
    private void update() {
        int iday = getSelectedDay();
        for ( int i=0; i<7; i++ ) {
            days[1][i].setText( " " );
            days[5][i].setText( " " );
            days[6][i].setText( " " );
        }
        calendar.set( Calendar.DATE,1);
        offset = calendar.get(Calendar.DAY_OF_WEEK)-Calendar.SUNDAY;
        lastDay = calendar.getActualMaximum(Calendar.DATE);
        int today=1;
        for ( int i=0; i<lastDay; i++ ){
            if(today==date.getDate()){
                days[(i+offset)/7+1][(i+offset)%7].setText("(("+String.valueOf(i+1)+"))");
            }else{
                days[(i+offset)/7+1][(i+offset)%7].setText(String.valueOf(i+1));
            }
            today++;
        }
        if ( iday != -1 ) {
            if ( iday > lastDay )
                iday = lastDay;
            setSelected( iday );
        }
    }
    public void actionPerformed( ActionEvent e ) {
        if ( e.getSource() == ok ){
            if(day==null){
                JOptionPane.showMessageDialog(this,"Tolong pilih !!!");
            }else{
                okClicked = true;
                dispose();
            }
        }else{
            dispose();
        }
    }
    /**
     * Called when the calendar gains the focus. Just re-sets the
     * selected day so that it is redrawn with the border that
     * indicate focus.
     **/
    public void focusGained( FocusEvent e ) {
        setSelected( day );
    }
    
    /**
     * Called when the calendar loses the focus. Just re-sets the
     * selected day so that it is redrawn without the border that
     * indicate focus.
     **/
    public void focusLost( FocusEvent e ) {
        setSelected( day );
    }
    /**
     * Called when a new month or year is selected. Updates the calendar
     * to reflect the selection.
     **/
    public void itemStateChanged( ItemEvent e ) {
        update();
    }
    
    /**
     * Called when a key is pressed and the calendar has the
     * focus. Handles the arrow keys so that the user can select a day
     * using the keyboard.
     **/
    public void keyPressed( KeyEvent e ) {
        int iday = getSelectedDay();
        switch ( e.getKeyCode() ) {
            case KeyEvent.VK_LEFT:
                if ( iday > 1 )
                    setSelected( iday-1 );
                break;
            case KeyEvent.VK_RIGHT:
                if ( iday < lastDay )
                    setSelected( iday+1 );
                break;
            case KeyEvent.VK_UP:
                if ( iday > 7 )
                    setSelected( iday-7 );
                break;
            case KeyEvent.VK_DOWN:
                if ( iday <= lastDay-7 )
                    setSelected( iday+7 );
                break;
        }
    }
    
    public void mouseClicked( MouseEvent e ) {
        JLabel day = (JLabel)e.getSource();
        if ( !day.getText().equals(" ") )
            setSelected( day );
        daysGrid.requestFocus();
    }
    
    public void keyReleased( KeyEvent e ) {}
    public void keyTyped( KeyEvent e ) {}
    public void mouseEntered( MouseEvent e ) {}
    public void mouseExited( MouseEvent e) {
        
    }
    public void mousePressed( MouseEvent e ) {}
    public void mouseReleased( MouseEvent e) {}
    
    public void today(){
        month.setSelectedIndex(date.getMonth());
        year.setSelectedItem(""+(date.getYear()+FIRST_YEAR));
    }
    
    public String getChoosedDate(){
        String strMonth=""+(month.getSelectedIndex()+1);
        // membersihkan dari penanda...
        if(day.getText().substring(0,1).equals("(")){
            day.setText(day.getText().substring(2,day.getText().length()-2));
        }
        if(day.getText().length()==1){
            day.setText("0"+day.getText());
        }
        if(month.getSelectedIndex()<=9){
            strMonth="0"+(month.getSelectedIndex()+1);
        }
        if(strMonth.length()==3)strMonth=month.getSelectedIndex()+1+"";
        //return day.getText()+"/"+strMonth+"/"+(String) year.getSelectedItem();
        return (String) year.getSelectedItem() + "-" + strMonth + "-" + day.getText();
    }
    
    
    /***** to defined variabel bentukan ****/
    private static class FocusablePanel extends JPanel {
        public FocusablePanel( LayoutManager layout ) {
            super( layout );
        }
        public boolean isFocusTraversable() {
            return true;
        }
        
    }
}

