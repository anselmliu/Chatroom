import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Server class
class Server {

    private static ArrayList<ClientHandler> clientList = new ArrayList<>();
    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            while (true) {

                Socket client = server.accept();

                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);
                clientList.add(clientSock);
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        BufferedReader in;
        PrintWriter out;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

        }
        public void write(Object obj) throws IOException {

            out.println(obj);
        }
        public void run()
        {
            try {
                String line;
                while ((line = in.readLine()) != null) {

                    // writing the received message from client
                    if (!line.equals("")){
                        for(ClientHandler client : clientList){
                            client.write(line);
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}