 
package Server;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;  
import javax.imageio.ImageIO;

 
public class ServerHandler implements Runnable {
    
    /**  Handled client socket  */
    private Socket       clientSocket;  
    private OutputStream serverOutWriter;
    private PrintWriter  serverPrintWriter;
    
    public ServerHandler(Socket clientSocket)
    {
     this.clientSocket = clientSocket;   
     
     try{
     this.serverPrintWriter = new PrintWriter(clientSocket.getOutputStream());
     }catch(IOException e){}
     
     }
    
    public void sendMessage(String message)
    {
            serverPrintWriter.println(message);
            serverPrintWriter.flush(); 
    }
    /* Kills current connection */

package Server;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;  
import javax.imageio.ImageIO;

 
public class ServerHandler implements Runnable {
    
    /**  Handled client socket  */
    private Socket       clientSocket;  
    private OutputStream serverOutWriter;
    private PrintWriter  serverPrintWriter;
    
    private boolean sendScreens        = false;
    private Thread  remoteScreenThread = null;
    
    public ServerHandler(Socket clientSocket)
    {
     this.clientSocket = clientSocket;   
     
     try{
     this.serverPrintWriter = new PrintWriter(clientSocket.getOutputStream());
     }catch(IOException e){}
     
     }
    
    public void sendMessage(String message)
    {
            serverPrintWriter.println(message);
            serverPrintWriter.flush(); 
    }
    /* Kills current connection */
    public void killConnection()
    {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Exception Terminating Connection! " + e.getMessage());
        }
    }
    /* returns an image for the screen sharing */  
    public BufferedImage getScreenShot()
    {
	BufferedImage screenImg = null ;
	try{
	Dimension screenDim   = Toolkit.getDefaultToolkit().getScreenSize();
	Robot     screenRobot = new Robot();
	screenImg=screenRobot.createScreenCapture(new Rectangle(screenDim));		
        
	}catch(Exception e)
        {
            System.out.println("Failed Grabbing Screenshot!");
        }
        return screenImg;
    }

      
    Runnable remoteScreening = new Runnable()
    {
        public void run()
        {
            try{
            serverOutWriter = clientSocket.getOutputStream();
            while(sendScreens){ 
            BufferedImage currenScreenStateImg = getScreenShot();
	    ImageIO.write(currenScreenStateImg,"PNG",serverOutWriter);
            serverOutWriter.flush();
            }
            }catch(Exception remoteScreenException){
                
            }
        }
    };
    
    /**
    * Handles screen sharing with the client
    */
    public void remoteScreenShare()
    { 
        remoteScreenThread = new Thread(remoteScreening);
        remoteScreenThread.start();  
     }
    public void stopRemoteScreenShare()
    {
        sendScreens = false; 
        if(remoteScreenThread != null) remoteScreenThread.stop();
    }
    
    /**
    * Handles all communication from the client to the server 
    */
    public void readSocket()
    { 
     try{
     BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
     String         message;
    
     while (clientSocket.isConnected()) {
     message = inputReader.readLine();
     System.out.println(message);
     switch(message)
     {
         default:
             break;
         case "INITSH":  
             sendMessage("INITSH");
             sendScreens = true;
             remoteScreenShare();
             break;
         case "STOPSH": //Not implemented yet
             stopRemoteScreenShare(); 
             break;
     }
     }
     }catch(IOException e){ 
             System.out.println("Error Reading From Socket! " + e.getMessage());  
     } 
     stopRemoteScreenShare();
     killConnection(); 
    }
     
    @Override
    public void run() { 
        readSocket(); 
    }
}

