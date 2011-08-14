/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.system;

/**
 *
 * @author Eko SW
 * From http://www.rgagnon.com/javadetails/java-0145.html
 */
public class Wait {
 @SuppressWarnings("static-access")
  public static void oneSec() {
     try {
       Thread.currentThread().sleep(1000);
       }
     catch (InterruptedException e) {
       e.printStackTrace();
       }
     }
  public static void manySec(long s) {
     try {
       Thread.currentThread().sleep(s * 1000);
       }
     catch (InterruptedException e) {
       e.printStackTrace();
       }
     }
}