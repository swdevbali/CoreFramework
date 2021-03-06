

package core.dataview;

import javax.swing.JSlider;

/**
 *
 * @author  PRAM WEE
 */
public class JSliderEx extends JSlider implements CoreConstants {
    
    private int dataType = STRING;
    private String fieldName="";
    private boolean focusFirst;
    private boolean manuallyFilled=false;

    public boolean isManuallyFilled() {
        return manuallyFilled;
    }

    public void setManuallyFilled(boolean manuallyFilled) {
        this.manuallyFilled = manuallyFilled;
    }

    public boolean isFocusFirst() {
        return focusFirst;
    }

    public void setFocusFirst(boolean focusFirst) {
        this.focusFirst = focusFirst;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    
    /** Creates new form BeanForm */
    public JSliderEx() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
