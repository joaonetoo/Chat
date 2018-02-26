import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
    public static String DEFAULT_SERVER_HOST = "172.28.128.10";
	public static int DEFAULT_PORT = 9999;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		String serverHost = args.length > 0 ? args[0] : DEFAULT_SERVER_HOST;
		int serverPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
		try {
			Socket clientSocket =  new Socket(serverHost,serverPort);
			Scanner in_client = new Scanner(System.in);
			PrintStream output_client = new PrintStream(clientSocket.getOutputStream());
			MessageServer messageServer = new MessageServer(clientSocket.getInputStream());
			while (in_client.hasNextLine()) {
				output_client.println(clientSocket.getLocalAddress().getHostAddress()+ ": " +in_client.nextLine());
			}

		}catch(IOException e) {
			e.printStackTrace();
		}

	}

}
class MessageServer extends Thread{
	private InputStream messageServer;
	
	public MessageServer(InputStream message) {
		this.messageServer = message;
		this.start();
	}
	public void run() {
		Scanner in =  new Scanner(this.messageServer);
		while (in.hasNextLine()) {
			System.out.println(in.nextLine());
			}

	}
}



