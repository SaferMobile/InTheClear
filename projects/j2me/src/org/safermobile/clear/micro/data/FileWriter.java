package org.safermobile.clear.micro.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
 
/*
 * http://wiki.forum.nokia.com/index.php/FleConnection_Example_-_JSR_75
 */
public class FileWriter extends MIDlet implements CommandListener {
    private Command save, exit;
    private TextBox text;
    private Display display;
 
    protected void destroyApp(boolean unconditional) {
        // TODO Auto-generated method stub
    }
 
    protected void pauseApp() {
        // TODO Auto-generated method stub
    }
 
    protected void startApp() {
        if (display == null) {
            display = Display.getDisplay(this);
            text = new TextBox("", "", 400, TextField.ANY);
            save = new Command("Save", Command.SCREEN, 1);
            exit = new Command("Exit", Command.EXIT, 1);
            text.addCommand(save);
            text.addCommand(exit);
            text.setCommandListener(this);
        }
        display.setCurrent(text);
    }
 
    public void commandAction(Command command, Displayable displayable) {
        if (command == save) {
            // Alert used for notify the user that the file had already been
            // saved
            Alert alert;
            try {
                saveFile();
                alert = new Alert("File saved.");
            } catch (Exception e) {
                alert = new Alert(e.toString());
            }
            alert.setTimeout(Alert.FOREVER);
            display.setCurrent(alert);
        }
        if (command == exit) {
            destroyApp(true);
            notifyDestroyed();
        }
    }
 
    private void saveFile() throws IOException {
        String folder = System.getProperty("fileconn.dir.photos");
 
        // Creating a connection.
        FileConnection c = (FileConnection) Connector.open(folder, Connector.READ_WRITE);
        try {
            // Checking if the directoy exists or not. If it doesn't exist we
            // create it.
            if (c.exists()) {
                System.out.println("existe");
            } else {
                System.out.println("nao existe");
                c.mkdir();
            }
        } finally {
            c.close();
        }
 
        
        c = (FileConnection) Connector.open(folder + "thiago.txt", Connector.READ_WRITE);
        try {
            if (!c.exists()) {
                // create the file
                c.create();
            }
 
            // create an OutputStream
            OutputStream out = c.openOutputStream();
            try {
                // Get the user text
                String userText = text.getString();
                // write out the user's text into the file
                out.write(userText.getBytes());
            } finally {
                out.close();
            }
        } finally {
            // Never forget to close a connection or you can face problems.
            // Pay attention here! If you close the connection before and
            // later try to
            // write something it will throw an exception.
            c.close();
        }
    }
}
