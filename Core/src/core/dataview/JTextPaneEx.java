

package core.dataview;

import javax.swing.JTextPane;

/**
 *
 * @author PRAM WEE
 */
public class JTextPaneEx extends JTextPane implements CoreConstants{
    private int dataType = STRING;
    private String fieldName = "";
    private boolean focusFirst;
    private boolean manuallyFilled=false;

    public int getTextAsInt() {
        return Integer.parseInt(getText());
    }

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
    public JTextPaneEx() {
        setText("");
    }

    public String getSQLValue() {
        if (dataType == DATE) {
            if ("".equals(getText())) {
                return null;
            } else {
                return "'" + getText() + "'";
            }
        }
        return getText();


    }

}
