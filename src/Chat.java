import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Chat {
	
	public static int DEFAULT_PORT = 9999;
	
	public static void main(String args[]) throws IOException {
		
		int serverPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
		ArrayList<Socket> clients = new ArrayList<Socket>();
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort);
			System.out.println("Aguardando conexao no endereco: "+ InetAddress.getLocalHost().getHostAddress()+ " na porta" 
					+ " " +serverPort);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("O usuario "+clientSocket.getInetAddress().getHostAddress()+ " entrou no chat");
				clients.add(clientSocket);
				MessageClient messageClient = new MessageClient(clients,clientSocket,serverSocket);
			}
		}catch(IOException  e) {
			e.printStackTrace();

		}		
		

		
	}

}

class MessageClient extends Thread{
	private ArrayList<Socket> clients;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	public MessageClient(ArrayList<Socket>clients, Socket clientSocket, ServerSocket serverSocket) {
		this.clients = clients;
		this.serverSocket = serverSocket;
		this.clientSocket = clientSocket;
		this.start();
		
	}
	public void run() {
		
		try {
			Scanner inClient  = new Scanner(this.clientSocket.getInputStream());
			while (inClient.hasNextLine()) {
				sendToAll(this.clients,this.clientSocket,inClient.nextLine());
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendToAll(ArrayList<Socket>clients, Socket clientMessage,String message ) {
		for(Socket client : clients) {
			if (!client.equals(clientMessage)) {
				try {
					PrintStream output_client = new PrintStream(client.getOutputStream());
					output_client.println(message);

				}catch(IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}