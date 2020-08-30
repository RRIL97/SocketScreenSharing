 
package Test;

import Client.Client;

 
public class testClient {
    public static void main(String [] args)
    {    
        Client testClient = new Client("localhost",8080);
        new Thread(testClient).start();
    }
}
