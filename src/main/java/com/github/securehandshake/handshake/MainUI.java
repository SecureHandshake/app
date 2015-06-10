/*
 * Copyright (C) 2015 "Shubham Rao <cshubhamrao@gmail.com>"
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.securehandshake.handshake;

import com.github.securehandshake.handshake.Library.OSIdentifier;

import java.awt.SplashScreen;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.*;
import javax.imageio.ImageIO;
import org.tukaani.xz.XZInputStream;

/**
 *
 * @author "Shubham Rao <cshubhamrao@gmail.com>"
 */
public class MainUI extends javax.swing.JFrame {

    /**
     * Creates new form MainUI
     */
    public MainUI() {
        initComponents();
        setIcon();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        showSplashScreen();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }

    private static void showSplashScreen() {
        SplashScreen splash = SplashScreen.getSplashScreen();

        if (splash != null) {
//                 Long long long task goes here:
//            
//                 Find the system, architecture
//                 Look for the .dll or .so inside jar
            String libName = "gnupg-for-java";
            String arch = "";
            String resRoot = "/libs/";
            String libraryResource;

            switch (OSIdentifier.fetchCurrentOs()) {
                case LINUX:
                    libName = "lib" + libName + ".so";
                    break;
                case WINDOWS:
                    libName += ".dll";
                    break;
                case MAC:
                    libName = "mac-lib" + libName + ".so";
                    break;
                case UNKNOWN:
                    break;
            }

            switch (OSIdentifier.fetchCurrentArch()) {
                case THIRTY_TWO_BIT:
                    arch = "x32";
                    break;
                case SIXTY_FOUR_BIT:
                    arch = "x64";
                    break;
                case UNKNOWN:
                    break;
            }

            // For eg. Linux 64 bit: libs/x64_libgnupg.so
            libraryResource = resRoot + arch + "_" + libName + ".xz";

            loadLibrary(mainUi, libraryResource);

//                 Move .dll/.so in $TEMP/HandshakeApp 
//                 Mind the JAVA PATH
//                 Modify path to include $TEMP/HandshakeApp
//            
//                 Instantiate the GnuPGContext
//            
//                 Return
//            
//                 All of this for later...
//                 Until then let's sleep for 5s zzzzz.....
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("ERROR in showing splash screen");
        }

    }

    private void setIcon() {
        BufferedImage icon;
        try {
            icon = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
            setIconImage(icon);
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.INFO, null, ex);
            System.out.println("Unable to set icon, reverting to default");
        }
    }

    private static void loadLibrary(MainUI mainUi, String libraryResource) {
        File tempFolder;
        File library;
        String fileName = libraryResource.substring(
            libraryResource.indexOf("_") + 1, libraryResource.lastIndexOf("."));
        
        try {
            tempFolder = Files.createTempDirectory("HandshakeApp").toFile();
            tempFolder.deleteOnExit();

            library = new File(tempFolder, fileName);

            try (
                    OutputStream decompressedLib = new FileOutputStream(library);
                    XZInputStream compressedLib = new XZInputStream(
                       mainUi.getClass().getResourceAsStream(libraryResource));
                )
            {
                int read = 0;
                byte[] bytes = new byte[1024];
                
                while ( (read = compressedLib.read(bytes)) != -1)
                {
                    decompressedLib.write(bytes, 0, read);
                }
            }
            } catch (IOException ex) {
                System.out.println("Unable to create temporary directory for app");
                System.out.println("Details:\n" + ex);
            }

        }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
