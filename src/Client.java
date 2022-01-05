import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

public class Client extends JFrame {


    Socket socket;

    BufferedReader br;
    PrintWriter out;


//    Declaring components
    private JLabel heading = new JLabel("Client area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);


//    Constructor
    public Client() {
        try {
            System.out.println("Sending request to server");
            socket=new Socket("127.0.0.1",6666);
            System.out.println("Connection done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());
//

            createGUI();
            handleEvents();
            startReading();
//            startWriting();



        } catch (Exception e) {

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
        this.setTitle("Client Messenger[END]");
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



//   for recieving and reading the data [method]
    public void startReading(){
        Runnable r1=()->{

            System.out.println("Reader started..");

            try {
            while (true) {

                   String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server terminted the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
//                    System.out.println("Server : " + msg);
                messageArea.append("Server : " + msg+"\n");

                }
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }




//    for Writing data [Method]
    public void startWriting(){
        Runnable r2 = ()->{
            System.out.println("Writer started...");

            try {

            while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }

                System.out.println("Connection is closed");
            }catch (Exception e) {
                e.printStackTrace();                 //<-----used to handle exceptions and errors
            }
        };
        new Thread(r2).start();

    }


    public static void main(String[] args) {
        System.out.println("This is client...");
        new Client();
    }









    
}
