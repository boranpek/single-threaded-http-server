import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Server {
    int tcpPort;
    String documentRoot;
    ServerSocket server;
    BufferedReader buffer;
    FileOutputStream output;
    String tempRoot;

    public Server(int tcpPort, String documentRoot) {
        this.documentRoot = documentRoot;
        this.tcpPort = tcpPort;

        try {
            try{
                tempRoot=this.documentRoot;
                this.server = new ServerSocket(tcpPort);
                System.out.println("Server is up!");Socket socket = server.accept();
                System.out.println("Connected");
                buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = buffer.readLine();
                output = (FileOutputStream) socket.getOutputStream();
                output.flush();
                String []token = input.split(" ");
                this.documentRoot +=token[1];
                File f = new File(this.documentRoot);
                boolean exists = f.exists();
                if (!exists){
                    buffer.close();
                    socket.close();
                    this.output.close();
                    f=new File(this.tempRoot+"/index.html");
                    System.out.println("File does not exist!");
                }
                if (token[1].equals("/"))
                    f=new File(this.tempRoot+"/index.html");
                Path p = f.toPath();
                byte[] bytes = Files.readAllBytes(p);
                String response = "HTTP/0.9 200 OK\r\n\r\n";
                byte[] bytesResponse = response.getBytes();
                output.write(bytesResponse);
                output.write(bytes);
                output.flush();
            }
            catch (SocketException e){
                output.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("TCP Port: ");
        int tcpPort = scanner.nextInt();
        System.out.print("Document Root: ");
        String documentRoot = scanner.next(); // ../Desktop/a
        Server server = new Server(tcpPort,documentRoot);
    }
}
