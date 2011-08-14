/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.gui;

/**
 *
 * @author irul
 */
class GUI {
    private static GUI instance = null;

    public static GUI getInstance() {
        if(instance==null)instance = new GUI();
        return instance;
    }
    
    private GUI(){}

    void loadQueryToTable(String query) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
