package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import calculation.Seat;
import calculation.SunAngle;
import calculation.SunThread;
import calculation.Window;
import calculation.Room;
import weather.Weather;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;

public class WindowMenu extends JFrame {
	int roomID;
	Room room;
	int seatCount;
	int windowCount;

	JMenuBar menubar;
	JMenu menu1, menu2, menu3;
	JMenuItem item1, item2, item3;
	JPanel contentPane, panel, eastPanel, southPanel, sEastPanel, nEastPanel;
	private int windowID[];
	private double angle;
	private boolean isSeatUse[];
	private String dataCity = "", dataWeather = "", dataMinTemp = "", dataMaxTemp = "";
	private Weather weather = new Weather();
	public Label[][][] label = new Label[2][2][2];

	private SunThread sunthread;
	private Window window[];
	private Seat seat[];

	String pswd;

	public WindowMenu(int roomID, int x, int y, int w, int h) {
		getWeather();
		init("�Զ�����ϵͳ");
		setLocation(x, y);
		setSize(w, h);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

	}

	private void init(String s) {
		setTitle(s);
		room = new Room(roomID);
		seatCount = room.getSeatCount();
		windowCount = room.getWindowCount();
		seat = room.getSeatArray();
		window = room.getWindowArray();
		isSeatUse = new boolean[seatCount];
		windowID = room.getWindowID();
		menubar = new JMenuBar();
		menu1 = new JMenu("�˵�");
		menu2 = new JMenu("ѡ��");
		menu3 = new JMenu("����");
		item1 = new JMenuItem("�˳� (Q)");
		item2 = new JMenuItem("���� (S)");
		item3 = new JMenuItem("���� (A)");
		item1.setAccelerator(KeyStroke.getKeyStroke("Q"));
		item2.setAccelerator(KeyStroke.getKeyStroke("S"));
		item3.setAccelerator(KeyStroke.getKeyStroke("A"));
		menu1.add(item1);
		menu2.add(item2);
		menu3.add(item3);
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		setJMenuBar(menubar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JLabel pNorth = new JLabel("ѡ����λ:");
		contentPane.add(pNorth, BorderLayout.NORTH);
		JPanel pCenter = getCenterPanel();
		contentPane.add(pCenter, BorderLayout.CENTER);
		JPanel pEast = getEastPanel();
		pEast.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(pEast, BorderLayout.EAST);
		JPanel pSouth = getSouthPanel();
		contentPane.add(pSouth, BorderLayout.SOUTH);
	}

	private JPanel getCenterPanel() {
		if (panel == null) {
			panel = new JPanel();

			panel.setLayout(null);
			
			panel.setPreferredSize(new Dimension(450,450));
			Dimension d = new Dimension();
			panel.getSize(d);
			//System.out.println(d.getHeight());
			//System.out.println(d.getWidth());

			panel.setBorder(BorderFactory.createLineBorder(Color.black));
			// panel.setLayout(new FlowLayout());
			JCheckBox[] checkBox = new JCheckBox[seatCount];

			for (int i = 0; i < seatCount; i++) {

				double[] seatPos = room.getSeatLeftTop(i);
				//System.out.println(seatPos[0]);
				//System.out.println(seatPos[1]);
				double posSeatInPanelX = 450 - seatPos[1] * 100;
				double posSeatInPanelY = 200 - seatPos[0] * 100;
				checkBox[i] = new JCheckBox("��λ" + (i + 1));

				checkBox[i].addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						// TODO Auto-generated method stub
						JCheckBox jCheckBox = (JCheckBox) e.getSource();
						seatListener(jCheckBox);
					}
				});
				checkBox[i].setBounds((int) posSeatInPanelX, (int) posSeatInPanelY, 75, 25);
				panel.add(checkBox[i]);
			}
		}
		return panel;
	}

	private JPanel getSouthPanel() {
		if (southPanel == null) {
			southPanel = new JPanel();
			/*
			southPanel.setLayout(new GridLayout(1, 2));
			pCity = new JLabel("���У�"+dataCity);
			pWeather = new JLabel("������" + dataWeather);
			pTemp = new JLabel("�¶ȣ�" + dataTemp);
			pESouth = new JLabel("̫���ǣ�" + angle);
			*/
			southPanel.add(new JLabel("���У�"+dataCity));
			southPanel.add(new JLabel("������" + dataWeather));
			southPanel.add(new JLabel("�¶ȣ�" + dataMinTemp+"~"+dataMaxTemp));
			southPanel.add(new JLabel("̫���߶Ƚǣ�" + room.getHSun()+"��"));
			//southPanel.add();
			southPanel.add(new JLabel("̫����λ�ǣ�"+ room.getGamSun()+"��"));
		}
		return southPanel;
	}

	private JPanel getEastPanel() {
		if (eastPanel == null) {
			eastPanel = new JPanel();
			eastPanel.setPreferredSize(new Dimension(200,300));
			eastPanel.setLayout(null);
			JPanel[] east = new JPanel[windowCount];
			
			for (int i = 0; i < windowCount; i++) {
				double[] windowPos = room.getWindowLeftTop(i);
				System.out.println(windowPos[0]);
				System.out.println(windowPos[2]);
				double posWindowInPanelX = 0 + windowPos[2] * 50;
				double posWindowInPanelY = 200 + windowPos[0] * 100;
				east[i] = getWindow(i);

				east[i].setBounds((int)posWindowInPanelX,(int)posWindowInPanelY, 75,50);
				eastPanel.add(east[i]);
				
			}
		}
		return eastPanel;
	}

	public JPanel getWindow(int windowNum) {
		sEastPanel = new JPanel();
		sEastPanel.add(new JLabel("����" + (windowNum + 1)), BorderLayout.WEST);
		nEastPanel = new JPanel();
		
		nEastPanel.setLayout(new GridLayout(2, 2));
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				label[windowNum][i][j] = new Label();
				label[windowNum][i][j].setBackground(Color.white);
				nEastPanel.add(label[windowNum][i][j]);
			}
		}
		sEastPanel.add(nEastPanel, BorderLayout.CENTER);
		return sEastPanel;
	}

	private void getWeather() {
		try {
			Map<String, Object> map = weather.getTodayWeather("101020100");
			dataCity = map.get("city").toString();
			dataWeather = map.get("weather").toString();
			dataMinTemp = map.get("temp1").toString();
			dataMaxTemp = map.get("temp2").toString();
			System.out.println(map.get("city") + "\t" + map.get("temp1") + "\t" + map.get("temp2") + "\t"
					+ map.get("weather") + "\t" + map.get("time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dataInit() { // �������ݿ����ݳ�ʼ����λ�ʹ���

	}

	public void seatListener(JCheckBox jCheckBox) { // ��λ��ѡ�������
		if (pswd != null) {
			pswd = null;
			return;
		}
		for (int i = 0; i < seatCount; i++) {
			if (jCheckBox.getText().toString().equals("��λ" + (i + 1))) {
				if (jCheckBox.isSelected() == true) {

					pswd = JOptionPane.showInputDialog("����������");
					if (pswd != null && pswd.equals(room.getPSWD())) {
						jCheckBox.setSelected(true);
						if (isSeatUse[i] == true) {
							;
						} else {
							seat[i].setSeat_use(true);
							sunthread = new SunThread(seat[i], window, windowID[0], this);
							sunthread.start();
							isSeatUse[i] = true;
						}
						System.out.println("seat " + i + " selected");
					} else {
						JOptionPane.showMessageDialog(null, "�������", "��ʾ��Ϣ", JOptionPane.ERROR_MESSAGE);
						pswd = null;
					}
				} else if (jCheckBox.isSelected() == false) {
					if (isSeatUse[i] == true) {
						seat[i].setSeat_use(false);
						isSeatUse[i] = false;
					} else {
						; // ����
					}
					System.out.println("seat " + i + " unselected");
				}
				break;
			}

		}
		/*
		 * if(jCheckBox.getText().toString().equals("��λ1")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[0]==true){ ; }else{
		 * seat[0].setSeat_use(true); sunthread=new SunThread(seat[0], window,
		 * windowID[0],this); sunthread.start(); isSeatUse[0]=true; } }else
		 * if(jCheckBox.isSelected()==false){ if(isSeatUse[0]==true){
		 * seat[0].setSeat_use(false); isSeatUse[0]=false; }else{ ; //���� } } }
		 * else if(jCheckBox.getText().toString().equals("��λ2")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[1]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[1]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ3")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[2]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[2]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ4")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[3]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[3]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ5")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[4]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[4]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ6")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[5]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[5]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ7")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[6]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[6]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ8")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[7]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[7]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ9")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[8]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[8]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ10")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[9]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[9]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ11")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[10]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[10]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ12")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[11]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[11]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ13")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[12]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[12]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ14")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[13]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[13]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ15")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[14]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[14]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ16")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[15]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[15]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ17")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[16]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[16]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ18")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[17]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[17]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ19")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[18]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[18]==true){
		 * 
		 * }else{
		 * 
		 * } } } else if(jCheckBox.getText().toString().equals("��λ20")){
		 * if(jCheckBox.isSelected()==true){ if(isSeatUse[19]==true){
		 * 
		 * }else{
		 * 
		 * } }else if(jCheckBox.isSelected()==false){ if(isSeatUse[19]==true){
		 * 
		 * }else{
		 * 
		 * } } }
		 */
	}
}
