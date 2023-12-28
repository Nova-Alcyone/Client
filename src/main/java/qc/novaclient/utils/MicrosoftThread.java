package qc.novaclient.utils;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

public class MicrosoftThread implements Runnable {
    @Override
    public void run() {
        try {
            OptionPanel.auth();
        } catch (MicrosoftAuthenticationException ignored) {
        }
    }
}