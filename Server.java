
package Server;
 
 
import java.io.IOException;
import java.net.ServerSocket; 
import java.net.Socket; 
import javax.swing.JOptionPane;

 
public class Server implements Runnable{
     
    private   int          serverPort  ;
    private   ServerSocket serverSocket;
    
    private   boolean      stoppedServer = false;
     
    
    
    public Server(int serverPort)
    {
        this.serverPort = serverPort; 
     }
    
    public void openServer()
    { 
        try {
            serverSocket    = new ServerSocket(serverPort);
        } catch (IOException openServException) {
           System.out.println("Cannot listen to port " + serverPort);
        } 
    }
    
    public void closeServer()
    {
        try {
            serverSocket.close();
            stoppedServer = true;
        } catch (IOException closeServException) {
            System.out.println("Cannot close server with port " + serverPort);
        }
    }
      
   
    @Override
    public void run() {
        openServer();
        
        while(!stoppedServer)
        { 
            try {
                Socket clientSocket = serverSocket.accept();
                int acceptClient = JOptionPane.showConfirmDialog(null,"Accept Connection ?");
                if(acceptClient == 0){ 
                new Thread(new ServerHandler(clientSocket)).start();
                System.out.println("Debug| Accepted Connection! ");
                }else
                {
                    System.out.println("Debug| Declined Connection!");
                    clientSocket.close();
                }
                
            } catch (IOException e) {
                 throw new RuntimeException("Error accepting connection", e);
            }
            
        }
    }
}
