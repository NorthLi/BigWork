package Chess;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import game.ChatClient;
import game.ChatServer;
public class UI extends JFrame {
	private MenuListener menuListener;
	private JMenuBar menuBar;
	private JMenu gameMenu, settingsMenu;
	private JMenuItem newGame;
	private JMenuItem openFile;
	private JMenuItem saveGame;
	private JMenuItem undoMove;
	private JMenuItem settings;
	private JMenuItem newGame2;
	private JMenuItem joinGame;
	private JMenuItem aiGame;
	private JButton f1, f2;
	//private ButtonListener buttonListener;
	public JTextField textField1, textField2;
	public JTextArea textArea;
	private ChatClient chatClient;
	private ChatServer chatServer;
	
	@SuppressWarnings("deprecation")
	public UI() throws Exception {
		// TODO Auto-generated constructor stub
		setTitle("黑白棋");
		setSize(1300, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		newGame = new JMenuItem("New game");
		openFile = new JMenuItem("Open file");
		saveGame = new JMenuItem("Save game");
		undoMove = new JMenuItem("Undo move");
		settings = new JMenuItem("Settings");
		joinGame = new JMenuItem("Join a game");
		newGame2 = new JMenuItem("New online game");
		gameMenu = new JMenu("Game");
		aiGame = new JMenuItem("play with AI");
		settingsMenu = new JMenu("All settings");
		menuBar = new JMenuBar();
		menuBar.add(newGame);
		menuBar.add(newGame2);
		menuBar.add(openFile);
		menuBar.add(saveGame);
		menuBar.add(undoMove);
		menuBar.add(joinGame);
		menuBar.add(aiGame);
		menuBar.add(settings);
		add(menuBar, BorderLayout.NORTH);
		setResizable(false);
		
		ImageIcon img = new ImageIcon("bg.jpg");
		JLabel label = new JLabel(img);
		getLayeredPane().add(label,	new Integer(Integer.MIN_VALUE));
		label.setBounds(0, 0, 850, 1000);
		((JPanel) this.getContentPane()).setOpaque(false);
		
		JLabel n1 = new JLabel("黑方", JLabel.CENTER);
		n1.setFont(new Font("Serif", Font.PLAIN, 20));
		getLayeredPane().add(n1);
		n1.setBounds(900, 50, 300, 50);
		n1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		JPanel t1 = new JPanel();
		t1.setLayout(new FlowLayout());
		textField1 = new JTextField("", 15);
		getLayeredPane().add(t1);
		t1.add(textField1);
		t1.setBounds(900, 100, 300, 40);
		t1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		f1 = new JButton("发送");
		t1.add(f1);
		
		JLabel l1 = new JLabel();
		l1.setFont(new Font("Serif", Font.PLAIN, 20));
		getLayeredPane().add(l1);
		l1.setBounds(900, 50, 300, 300);
		l1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		ImageIcon head1 = new ImageIcon("head1.jpg");
		JLabel l2 = new JLabel(head1);
		getLayeredPane().add(l2);
		l2.setBounds(900, 140, 300, 170);
		l2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		((JPanel) this.getContentPane()).setOpaque(false);
		
		JLabel l3 = new JLabel("剩余时间：");
		l3.setFont(new Font("Serif", Font.PLAIN, 15));
		getLayeredPane().add(l3);
		l3.setBounds(900, 310, 150, 40);
		l3.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		JLabel l4 = new JLabel("当前黑子个数：  2");
		l4.setFont(new Font("Serif", Font.PLAIN, 15));
		getLayeredPane().add(l4);
		l4.setBounds(1050, 310, 150, 40);
		l4.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		JLabel n2 = new JLabel("白方", JLabel.CENTER);
		n2.setFont(new Font("Serif", Font.PLAIN, 20));
		getLayeredPane().add(n2);
		n2.setBounds(900, 550, 300, 50);
		n2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		JPanel t2 = new JPanel();
		t2.setLayout(new FlowLayout());
		textField2 = new JTextField("", 15);
		getLayeredPane().add(t2);
		t2.add(textField2);
		t2.setBounds(900, 600, 300, 40);
		t2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		f2 = new JButton("发送");
		t2.add(f2);
		
		JLabel l5 = new JLabel();
		l5.setFont(new Font("Serif", Font.PLAIN, 20));
		getLayeredPane().add(l5);
		l5.setBounds(900, 550, 300, 300);
		l5.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		ImageIcon head2 = new ImageIcon("head2.jpg");
		JLabel l6 = new JLabel(head2);
		getLayeredPane().add(l6);
		l6.setBounds(900, 640, 300, 170);
		l6.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		((JPanel) this.getContentPane()).setOpaque(false);
		
		JLabel l7 = new JLabel("剩余时间：");
		l7.setFont(new Font("Serif", Font.PLAIN, 15));
		getLayeredPane().add(l7);
		l7.setBounds(900, 810, 150, 40);
		l7.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		JLabel l8 = new JLabel("当前白子个数：  2");
		l8.setFont(new Font("Serif", Font.PLAIN, 15));
		getLayeredPane().add(l8);
		l8.setBounds(1050, 810, 150, 40);
		l8.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		JPanel panel = new JPanel();
		textArea = new JTextArea();
		textArea.setFont(new Font("Serif", Font.PLAIN, 17));
		getLayeredPane().add(panel);
		panel.add(textArea);
		//panel.setLayout(null);
//		JScrollPane sp = new JScrollPane(textArea);
//		panel.add(sp);
		textArea.setBounds(900, 370, 300, 160);
		panel.setBounds(900, 370, 300, 160);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		
		chatServer = new ChatServer(this);
		chatClient = new ChatClient(this);
		
		menuListener = new MenuListener(newGame, openFile, saveGame, undoMove, settings, newGame2, joinGame, aiGame, this, l3, l4, l7, l8, f1, f2, textField1, textField2, textArea, chatClient, chatServer);
		newGame.addActionListener(menuListener);
		openFile.addActionListener(menuListener);
		saveGame.addActionListener(menuListener);
		undoMove.addActionListener(menuListener);
		settings.addActionListener(menuListener);
		newGame2.addActionListener(menuListener);
		joinGame.addActionListener(menuListener);
		aiGame.addActionListener(menuListener);
		f1.setEnabled(false);
		f2.setEnabled(false);
		saveGame.setEnabled(false);
//		URL cbUrl;
//		File file = new File("music.wav");
		
//		cbUrl = file.toURL();
//		AudioClip aau; 
//		aau = Applet.newAudioClip(cbUrl); 
//		aau.loop();
		
		//buttonListener = new ButtonListener(f1, f2);
		
//		chatServer = new ChatServer(this);
//		chatServer.start();
//		chatClient = new ChatClient(this);
//		chatClient.start();
		f1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				chatServer.sendmsg(textField1.getText());
				repaint();
			}
		});
		
		f2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				chatClient.sendmsg(textField2.getText());
				repaint();
			}
		});
		Chessboard.l1 = l3;
		Chessboard.l2 = l4;
		Chessboard.l3 = l7;
		Chessboard.l4 = l8;
		Chessboard.frame = this;
		
		
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		drawBoard(g);
		drawChessMan(g);
	}
	
	private void drawBoard(Graphics g)
	{
		g.setColor(Color.black);
		for(int i=20;i<=820;i+=100)
			g.drawLine(i, 80, i, 880);
		for(int i=80;i<=880;i+=100)
			g.drawLine(20, i, 820, i);
	}
	
	private void drawChessMan(Graphics g)
	{
		Color color;
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(Chessboard.getOneChess(i, j) == 1)
				{
					g.setColor(Color.black);
					g.fillOval(i * 100 + 25, j * 100 + 85, 90, 90);
				}
				if(Chessboard.getOneChess(i, j) == 2)
				{
					g.setColor(Color.white);
					g.fillOval(i * 100 + 25, j * 100 + 85, 90, 90);
				}
			}
		}
		g.setColor(Color.red);
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(Chessboard.canSetChess(i, j, Chessboard.getWhoTurn()))
				{
					g.fillOval(i * 100 + 60, j * 100 + 120, 20, 20);
				}
			}
		}
		if(Chessboard.lastX != -1)
		{
			g.setColor(Color.red);
			g.fillRect(Chessboard.lastX * 100 + 23, Chessboard.lastY * 100 + 83, 5, 25);
			g.fillRect(Chessboard.lastX * 100 + 23, Chessboard.lastY * 100 + 83, 25, 5);
			g.fillRect(Chessboard.lastX * 100 + 112, Chessboard.lastY * 100 + 83, 5, 25);
			g.fillRect(Chessboard.lastX * 100 + 92, Chessboard.lastY * 100 + 83, 25, 5);
			g.fillRect(Chessboard.lastX * 100 + 23, Chessboard.lastY * 100 + 152, 5, 25);
			g.fillRect(Chessboard.lastX * 100 + 23, Chessboard.lastY * 100 + 172, 25, 5);
			g.fillRect(Chessboard.lastX * 100 + 112, Chessboard.lastY * 100 + 152, 5, 25);
			g.fillRect(Chessboard.lastX * 100 + 92, Chessboard.lastY * 100 + 172, 25, 5);
		}
		if(Chessboard.getWhoTurn() == 1)
		{
			g.fillRect(975, 170, 5, 160);
			g.fillRect(975, 170, 155, 5);
			g.fillRect(975, 325, 155, 5);
			g.fillRect(1128, 170, 5, 160);
		}
		else
		{
			g.fillRect(975, 170 + 500, 5, 160);
			g.fillRect(975, 170 + 500, 155, 5);
			g.fillRect(975, 325 + 500, 155, 5);
			g.fillRect(1128, 170 + 500, 5, 160);
		}
		
	}
	
}
