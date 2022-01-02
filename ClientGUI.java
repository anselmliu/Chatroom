import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientGUI {
    PrintWriter out;
    BufferedReader in;
    JLabel defaultname;
    static String recentText = null;
    String name = null;
    JFrame frame;
    JPanel panel01;
    JScrollPane scroll;
    JTextField you;
    JTextArea text_current;
    JButton send;
    static JTextArea text_history;
    String sendText;
    Socket socket;
    int delay = 1000;

    public ClientGUI(String address, int port) throws IOException {

        frame = new JFrame("Chat Room");
        panel01 = new JPanel();

        panel01.setLayout(null);

        panel01.setBounds(500,500,1000,100);
        frame.add(panel01);

        defaultname = new JLabel("Change your name!");
        text_history = new JTextArea();
        scroll = new JScrollPane(text_history);
        send = new JButton("Send");
        text_current = new JTextArea();
        you = new JTextField("Default Name");

        text_history.setEditable(false);
        text_history.setLineWrap(true);
        text_history.setWrapStyleWord(true);
        scroll.setBounds(50,25, 900, 800);
        defaultname.setBounds(40,850, 150, 35);
        you.setBounds(25,890, 150, 35);
        text_current.setBounds(200,850,650,100);
        send.setBounds(875,865,100,75);

        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = you.getText();
                recentText = text_current.getText();
                sendText = "\n"+name+": "+recentText;
                text_current.setText("");
                out.println(sendText);
                out.flush();

            }
        });

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel01.add(scroll);
        panel01.add(you);
        panel01.add(text_current);
        panel01.add(send);
        panel01.add(defaultname);

        frame.add(panel01);

        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(1000,1000);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        updateScroll();
    }

    public void updateScroll(){
        while(true){
            try {
                //client receiving the message does not automatically update as text_history is only updated after the button action event
                text_history.append(in.readLine()+"\n");
                System.out.println("This client has updated text history");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    public static void main(String args[]) throws IOException {

        ClientGUI c = new ClientGUI("192.168.1.3", 1234);

    }
}