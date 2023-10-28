package qc.novaclient.utils;

import qc.novaclient.Launcher;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

 public class MicrosoftThread implements Runnable {
     @Override
     public void run() {
         try {
             Launcher.auth();
         } catch (MicrosoftAuthenticationException ignored) {
         }
     }
 }