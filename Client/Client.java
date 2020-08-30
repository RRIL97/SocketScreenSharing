
package Client;
 
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket; 
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


 
public class Client implements Runnable{
    
    private String clientIp  ;
    private int    clientPort;
     
    private Socket clientSocket;
    
    private PrintWriter clientPrintWriter;
    
    private ClientFrame clientWind;
    
    private boolean      remoteScreen = true;
    private InputStream  inputStream ;
    
    public Client(String clientIp, int clientPort)
    {
        this.clientIp   = clientIp;
        this.clientPort = clientPort; 
    }
    
    public String getServerInfo()
    {
        return clientIp+":"+clientPort;
    }
    public void sendMessage(String message)
    {
            System.out.println(message);
            clientPrintWriter.println(message);
            clientPrintWriter.flush(); 
            System.out.println("Flushed");
        
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
   }  
    public void remoteScreenShare()
    {
       try{
        JLabel imageLabel;
        inputStream = clientSocket.getInputStream(); 
        imageLabel  = clientWind.getImageLabel();
        
        while(remoteScreen){ 
	     BufferedImage imgReadFromStream = ImageIO.read(inputStream); 
             if(imgReadFromStream != null){
             imgReadFromStream = resize(imgReadFromStream,500,490);
    	     imageLabel.setText("");
	     ImageIcon remoteImageIcon = new ImageIcon(imgReadFromStream);
	     imageLabel.setIcon(remoteImageIcon);
             }
        }
        }catch(IOException remoteScreenException){
            System.out.println("Exception In Remote Screening!");
        }
    }
    
    /**
    * Handles all communication from the server to the server 
    */
    public void readSocket()
    { 
     try{
     BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
     String         message;
    
     while (clientSocket.isConnected()) {
     message = inputReader.readLine();
     
     switch(message)
     {
         default:
             break;
         case "INITSH": 
             clientWind    = new ClientFrame(this); 
             remoteScreenShare();
             break; 
     }
     }
     }catch(IOException e){ 
             System.out.println("Error Reading From Socket! " + e.getMessage());  
     }  
    }
    @Override
    public void run() {
        try {
            clientSocket      = new Socket(clientIp,clientPort);   
            clientPrintWriter = new PrintWriter(clientSocket.getOutputStream());
           
            System.out.println("Client Connected!"); 
            sendMessage("INITSH");   
            readSocket();
            
        } catch (IOException e) {
             throw new RuntimeException("Connection To Server Failed! ",e);
        }
        
    }
    
}
