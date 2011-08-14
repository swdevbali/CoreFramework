/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smsgateway;

/**
 *
 * @author Eko SW
 */
public abstract class TimerExecution {
    public abstract void executeTimer(); //ini dijalankan setiap 2 detik, setelah outbox dikirim
}
