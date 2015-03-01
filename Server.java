package asdf;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
    	ServerSocket ss = new ServerSocket(5000);
    	//ServerSocket ss1 = new ServerSocket(13337);
    	byte[] b = new byte[4096];
    	Socket s = ss.accept();
    	
    	
    	while(true){
    	
    		//Socket s1 = ss1.accept();
    		int len = s.getInputStream().read(b, 0, b.length);
            s.getOutputStream().write(new String(b).replace("POST /thing HTTP/1.1", "HTTP/1.0 200 OK\n1.1").getBytes());
            
            byte[] trimmed = new byte[len];
            for(int i = 0; i < len; i++){
            	trimmed[i] = b[i];
            }

            String st = new String(trimmed).split("url=")[1].replace("%3A", ":").replace("%2F", "/");
            String q = st.substring(st.lastIndexOf('/') + 1);
            //new DataOutputStream(s1.getOutputStream()).writeBytes(q + "\n");

            System.out.println(new String(b).split("url=")[1].replace("%3A", ":").replace("%2F", "/"));
    	}
    	
    }

}