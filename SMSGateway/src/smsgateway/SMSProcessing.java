package smsgateway;

/**
 *
 * @author PRAM WEE
 */
public interface SMSProcessing {
    public void afterModemInstalled();
    public String delegateMessageProcessing(java.lang.String[] token,
            java.lang.String incomingPhoneNumber);
}
