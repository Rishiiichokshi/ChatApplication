import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


public class Server extends JFrame{

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

//    Declare Components
    private JLabel heading = new JLabel("Server area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    //    constructor
    public Server() {
        try {
            server=new ServerSocket(6666);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting...");
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
//            startWriting();


        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
    

    public void handleEvents(){

        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("Key Released " + e.getKeyCode());
                if (e.getKeyCode() == 10) {
//                    System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+ contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    //    creating GUI
    private void createGUI() {
        this.setTitle("Server Messenger[END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//      Coding for Component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("E:\\chatapp\\src\\bubblee.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

//      setting frame layout
        messageArea.setEditable(false);
        this.setLayout(new BorderLayout());

//      adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }


    public void startReading(){
        Runnable r1=()->{

            System.out.println("Reader started..");

            try {
                while (true) {

                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Client terminted the chat");
                        JOptionPane.showMessageDialog(this, "Client Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
//                    System.out.println("Server : " + msg);
                    messageArea.append("Client : " + msg+"\n");

                }
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2=()->{
            System.out.println("Writer started...");

            try {
            while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")){
                        socket.close();
                        break;
                    }


                }
            }catch (Exception e) {
                System.out.println("Connection is closed");                                            //<-----used to handle exceptions and errors
            }

            System.out.println("Connection is closed");

        };
        new Thread(r2).start();

    }



    public static void main(String[] args) {
        System.out.println("This is server...going to start server");
        new Server();
    }
}
