
package Test;

import Server.Server;


public class testServer {
    public static void main(String [] args)
    {
        Server currentServer = new Server(8080);
        new Thread(currentServer).start();  
    }
}
