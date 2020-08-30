
package Client;
 

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientFrame{
    
 
    private JFrame mainWindow; 
    private JLabel imageLabel; //Holds Remote Picture
    
    
    public ClientFrame(Client client)
    {  
     
        
        mainWindow = new JFrame("Remote Connection! Connected To - " + client.getServerInfo());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
        mainWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(mainWindow.getClass().getResource("/Icons/icons8_Remote_Control_28px.png")));
        mainWindow.setSize(500,500);
        
        imageLabel = new JLabel();
        mainWindow.add(imageLabel);
        imageLabel.setLocation(0,10);
        imageLabel.setSize(490,490);
    } 
    
    public JLabel getImageLabel()
    {
        return imageLabel;
    }
}
