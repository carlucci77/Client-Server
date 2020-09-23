import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.net.*;
public class KnockKnock {
	private static Socket socket = null;
	private static PrintWriter writer = null;
	private static BufferedReader reader = null;

	//Opening Server Connection
	public static void activateSocket(JButton connectButton, JTextField IPField, JTextField portField, JTextArea communicationField) {
		try {
			socket = new Socket(IPField.getText(), Integer.parseInt(portField.getText()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			connectButton.setText("Disconnect");
			communicationField.append("Connected to Server" + "\n");
		}catch (UnknownHostException u) {
			communicationField.append("Error: Unknown Host");
			socket = null;
		}catch (IOException i) {
			communicationField.append("Error: IO Exception");
			socket = null;
		}

	}

	//Closing Server Connection
	public static void deactivateSocket(JButton connectButton, JTextArea communicationField) {
		try {
			connectButton.setText("Connect");
			communicationField.append("Disconnected" + "\n");
			writer.close();
			reader.close();
			socket.close();
			socket = null;
		}catch(IOException i) {
			communicationField.append("IO Exception");
			socket = null;
		}
	}
	public static void main(String [] args) {
		//GUI Frame Code
		JFrame frame = new JFrame();
		frame.setTitle("Program 1a Knock Knock Client");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(1000, 600));
		frame.setMaximumSize(new Dimension(1000, 600));
		frame.setMinimumSize(new Dimension(1000, 600));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//IP Panel
		JLabel IPLabel = new JLabel("IP Address: ");
		JTextField IPField = new JTextField("constance.cs.rutgers.edu");
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(IPLabel);//,BorderLayout.SOUTH);
		panel.add(IPField);//, BorderLayout.EAST);
		panel.setVisible(true);
		frame.add(panel, BorderLayout.WEST);

		//Port Panel
		JLabel portLabel = new JLabel("Port Number: ");
		JTextField portField = new JTextField("5520");
		//JPanel panel2 = new JPanel(new BorderLayout());
		panel.add(portLabel);//,BorderLayout.NORTH);
		panel.add(portField);//, BorderLayout.CENTER);
		panel.setVisible(true);
		//frame.add(panel2, BorderLayout.EAST);

		//Connect Button Panel
		JButton connectButton = new JButton("Connect");
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.add(connectButton, BorderLayout.CENTER);
		panel3.setVisible(true);
		frame.add(panel3, BorderLayout.NORTH);

		//Message Field Panel
		JLabel messageLabel = new JLabel("Message to Server: ");
		frame.add(messageLabel);
		JTextField messageField = new JTextField();
		messageField.setPreferredSize(new Dimension(100,25));
		JButton sendButton = new JButton("Send");
		JPanel panel4 = new JPanel(new BorderLayout());
		panel4.add(messageLabel,BorderLayout.NORTH);
		panel4.add(messageField, BorderLayout.CENTER);
		panel4.add(sendButton, BorderLayout.WEST);
		panel4.setVisible(true);
		frame.add(panel4, BorderLayout.AFTER_LAST_LINE);
		frame.getRootPane().setDefaultButton(sendButton);

		//Communication Panel
		JLabel communicationLabel = new JLabel("Client/Server Communication:");
		JTextArea communicationField = new JTextArea();
		//communicationField.setPreferredSize(new Dimension(200, 100));
		//JScrollPane scroll = new JScrollPane (communicationField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel panel5 = new JPanel(new BorderLayout());
		panel5.add(communicationLabel,BorderLayout.WEST);
		panel5.add(communicationField, BorderLayout.CENTER);
		communicationField.setVisible(true);
		panel5.setVisible(true);
		//panel5.add(scroll);
		frame.add(panel5, BorderLayout.CENTER);
		frame.setVisible(true);

		//Connect Button Actions
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent connect) {
				if(connectButton.getText().equals("Connect")) {
					activateSocket(connectButton, IPField, portField, communicationField);
				}else {
					deactivateSocket(connectButton, communicationField);
				}
			}
		});

		//Send Button Actions
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent send) {
				if(connectButton.getText().equals("Disconnect") && socket != null) {
					try {
						writer.println(messageField.getText().trim());
						communicationField.append("Client: " + messageField.getText().trim() + "\n");   
						messageField.setText("");
						String temp = reader.readLine();
						communicationField.append("Server: " + temp + "\n");
						if(temp.equals("Good Bye!")) {
							deactivateSocket(connectButton, communicationField);
						}
					}catch(IOException ex) {
						communicationField.append("Error: IO Exception");
						socket = null;
					}
				}
			}
		});
	}
}
