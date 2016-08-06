package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.*;

import org.omg.CORBA.CurrentHelper;

import Chess.Chessboard;

public class MyDialog extends JDialog implements ActionListener {
	JButton yes, no, yes2, yes3, yes4, no2;
	JFrame frame;
	private String text;
	private String[][] items;
	private JTextField[] textFields;
	private String[] result;
	private MyPanel miniBoard;
	public static int[][] theChess;
	private int time, whoTurn;
	private OutputStream outputStream;
	private JComboBox comboBox1, comboBox2;
	
	public MyDialog(Frame f, String title, boolean modal, String[][] i) {
		// TODO Auto-generated constructor stub
		super(f, title, modal);
		this.items = i;
		init();
	}
	
	public MyDialog(JFrame f)
	{
		super(f, "选择需要加载的棋局", true);
		theChess = new int[8][8];
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
				theChess[i][j] = 0;
		}
		frame = f;
		openFile();
	}
	
	public MyDialog(JFrame f, boolean bool)
	{
		super(f, "请选择难度和先后手", true);
		frame = f;
		
		yes4 = new JButton("确定");
		no2 = new JButton("取消");
		yes4.addActionListener(this);
		no2.addActionListener(this);
		JPanel p2 = new JPanel();
		p2.add(yes4);
		p2.add(no2);
		add(p2, BorderLayout.SOUTH);
		comboBox1 = new JComboBox();
		comboBox2 = new JComboBox();
		comboBox1.addItem("简单");
		comboBox1.addItem("中等");
		comboBox1.addItem("困难");
		comboBox2.addItem("先手");
		comboBox2.addItem("后手");
		add(comboBox1, BorderLayout.NORTH);
		add(comboBox2, BorderLayout.CENTER);
		int x = (int) (frame.getBounds().x + 0.25 * frame.getBounds().width);
		int y = (int) (frame.getBounds().y + 0.25 * frame.getBounds().height);
		setBounds(x, y, 300, 130);
	}
	
	public MyDialog(JFrame f, String con, boolean flag2) {
		// TODO Auto-generated constructor stub
		super(f, "提醒", flag2);
		frame =  f;
		this.text = con;
		warn();
	}
	
	public MyDialog(JFrame f, String con, OutputStream os) {
		// TODO Auto-generated constructor stub
		super(f, "悔棋", true);
		outputStream = os;
		frame = f;
		this.text = con;
		yes3 = new JButton("确定");
		no = new JButton("取消");
		yes3.addActionListener(this);
		no.addActionListener(this);
		JPanel p1 = new JPanel();
		p1.add(new JLabel(text));
		JPanel p2 = new JPanel();
		p2.add(yes3);
		p2.add(no);
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		int x = (int) (frame.getBounds().x + 0.25 * frame.getBounds().width);
		int y = (int) (frame.getBounds().y + 0.25 * frame.getBounds().height);
		setBounds(x, y, 200, 110);
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}
		});
	}
	
	public MyDialog(JFrame f, int turn)
	{
		super(f, "提示", true);
		frame = f;
		win(turn);
	}
	
	public String[] getResult() {
		return result;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == yes)
		{
			//Chessboard.clear();
			frame.paint(frame.getGraphics());
			setVisible(false);
		}
		else if(e.getSource() == yes2)
		{
			Chessboard.setHasChess(theChess);
			Chessboard.subTime = time;
			Chessboard.setWhoTurn(whoTurn);
			frame.paint(frame.getGraphics());
			setVisible(false);
		}
		else if(e.getSource() == yes3)
		{
			byte[] order = new byte[1];
			order[0] = 4;
			try {
				outputStream.write(order);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Chessboard.setHasChess(Chessboard.beforeHasChess[59 - Chessboard.canSetCount()]);
			Chessboard.subTime = 0;
			Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
			Chessboard.setAgree(1);
			frame.repaint();
			setVisible(false);
		}
		else if(e.getSource() == no)
		{
			byte[] order = new byte[1];
			order[0] = 5;
			try {
				outputStream.write(order);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setVisible(false);
		}
		else if(e.getSource() == yes4)
		{
			if(comboBox1.getSelectedItem().toString().equals("简单"))
				Chessboard.hard = 1;
			if(comboBox1.getSelectedItem().toString().equals("中等"))
				Chessboard.hard = 2;
			if(comboBox1.getSelectedItem().toString().equals("困难"))
				Chessboard.hard = 4;
			if(comboBox2.getSelectedItem().toString().equals("先手"))
				Chessboard.isFirst = 1;
			if(comboBox2.getSelectedItem().toString().equals("后手"))
				Chessboard.isFirst = 2;
			setVisible(false);
		}
	}
	
	public void openFile()
	{
		yes2 = new JButton("确定");
		no = new JButton("取消");
		yes2.addActionListener(this);
		no.addActionListener(this);
		miniBoard = new MyPanel();
		miniBoard.setSize(700, 700);
		add(miniBoard, BorderLayout.CENTER);
		JPanel p2 = new JPanel();
		p2.add(yes2);
		p2.add(no);
		add(p2, BorderLayout.SOUTH);
		JComboBox comboBox = new JComboBox();
		File file = new File("Map");
		String[] fileList = file.list();
		for(int i=0;i<fileList.length;++i)
		{
			comboBox.addItem(fileList[i]);
		}
		add(comboBox, BorderLayout.NORTH);
		
		int x = (int) (frame.getBounds().x + 0.25 * frame.getBounds().width);
		int y = (int) (frame.getBounds().y + 0.25 * frame.getBounds().height);
		setBounds(x, y, 500, 550);
		ItemListener itemListener = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					File selectFile = new File("Map/" + e.getItem().toString());
					try {
						InputStream inputStream = new FileInputStream(selectFile);
						byte[][] buf = new byte[8][8];
						for(int i=0;i<8;++i)
						{
							inputStream.read(buf[i]);
						}
						for(int i=0;i<8;++i)
						{
							for(int j=0;j<8;++j)
							{
								theChess[i][j] = buf[i][j];
							}
						}
						byte[] info = new byte[2];
						inputStream.read(info);
						time = info[1];
						whoTurn = info[0];
						miniBoard.repaint();
						inputStream.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		};
		
		comboBox.addItemListener(itemListener);
	}
	
	public void warn()
	{
		yes = new JButton("确定");
		no = new JButton("取消");
		yes.addActionListener(this);
		no.addActionListener(this);
		JPanel p1 = new JPanel();
		p1.add(new JLabel(text));
		JPanel p2 = new JPanel();
		p2.add(yes);
		//p2.add(no);
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		int x = (int) (frame.getBounds().x + 0.25 * frame.getBounds().width);
		int y = (int) (frame.getBounds().y + 0.25 * frame.getBounds().height);
		setBounds(x, y, 200, 110);
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}
		});
	}
	
	public void win(int turn)
	{
		yes = new JButton("确定");
		yes.addActionListener(this);
		JPanel p1 = new JPanel();
		String string = (turn == 1) ? "黑胜" : "白胜";
		p1.add(new JLabel(string));
		JPanel p2 = new JPanel();
		p2.add(yes);
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		int x = (int) (frame.getBounds().x + 0.25 * frame.getBounds().width);
		int y = (int) (frame.getBounds().y + 0.25 * frame.getBounds().height);
		setBounds(x, y, 200, 110);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				//super.windowClosing(e);
				setVisible(false);
				
			}
		});
	}
	
	void init(){
		Container container = this.getContentPane();
		container.setLayout(new GridLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		int count = items.length;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		for(int i=0;i<count;++i)
		{
			gridBagConstraints.gridy = i;
			container.add(new JLabel(items[i][0]), gridBagConstraints);
		}
		textFields = new JTextField[count];
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		for(int i=0;i<count;i++)
		{
			gridBagConstraints.gridy = i;
			textFields[i] = new JTextField(items[i][1]);
			container.add(textFields[i], gridBagConstraints);
		}
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = count;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(7, 7, 7, 7);
		JButton button = new JButton("确定");
		container.add(button, gridBagConstraints);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				result = new String[items.length];
				for(int i=0;i<result.length;i++)
					result[i] = textFields[i].getText();
				MyDialog.this.dispose();
			}
		});
		this.setSize(500, 80);
		int dialogWidth = this.getWidth();
		int dialogHeight = this.getHeight();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		this.setLocation((screen.width - dialogWidth) / 2, (screen.height - dialogHeight) / 2);
		
	}
	
}

class MyPanel extends JPanel
{
	@Override
	public void paint(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paint(arg0);
		arg0.setColor(Color.black);
		for(int i=50;i<=450;i+=50)
			arg0.drawLine(i, 30, i, 430);
		for(int i=30;i<=430;i+=50)
			arg0.drawLine(50, i, 450, i);
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(MyDialog.theChess[i][j] == 1)
				{
					arg0.setColor(Color.black);
					arg0.fillOval(i * 50 + 55, j * 50 + 35, 40, 40);
				}
				if(MyDialog.theChess[i][j] == 2)
				{
					arg0.setColor(Color.white);
					arg0.fillOval(i * 50 + 55, j * 50 + 35, 40, 40);
				}
			}
		}
	}
}
