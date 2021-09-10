package edu.yu.parallel;

/** Skeleton client to set up and display a GUI that allows users to enter AND
 * a GUI that displays received text.  When a user enters text into the GUI,
 * currently the GUI is implemented to print that text to the console.
 * Students will need to add code to send this text to the server and to
 * display text that it receives from the server
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


public class SimpleChatClient
{
  JTextArea incoming;
  JTextField outgoing;
    
  public void go(String hostName, int portNumber) {

    JFrame frame = new JFrame("Simple Chat Client");
    JPanel mainPanel = new JPanel();
    incoming = new JTextArea(15, 50);
    incoming.setLineWrap(true);
    incoming.setWrapStyleWord(true);
    incoming.setEditable(false);
    JScrollPane qScroller = new JScrollPane(incoming);
    qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    outgoing = new JTextField(20);
    JButton sendButton = new JButton("Send");
    mainPanel.add(qScroller);
    mainPanel.add(outgoing);
    mainPanel.add(sendButton);
    frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
    frame.setSize(400, 500);
    frame.setVisible(true);

    try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
    ) {
      sendButton.addActionListener(new SendButtonListener(out));
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
      String fromServer;
      int count = 0;
      while (true) {
        if (in.ready()) {
          fromServer = in.readLine();
          incoming.append(fromServer + "\n");
          //System.out.println(fromServer);
        }
      }
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host " + hostName);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to " +
              hostName);
      System.exit(1);
    }

  }
    
  public class SendButtonListener implements ActionListener {

    public PrintWriter out = null;

    public SendButtonListener(PrintWriter out){
      this.out = out;
    }

    public void actionPerformed(ActionEvent ev) {
      try {
        out.println(outgoing.getText());
        incoming.append("You sent > " + outgoing.getText() + "\n");
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      outgoing.setText("");
      outgoing.requestFocus();
    }
  }
    
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println(
              "Usage: java SimpleChatClient <host name> <port number>");
      System.exit(1);
    }

    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);

    new SimpleChatClient().go(hostName, portNumber);
  }
}
