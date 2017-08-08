package calculation;

import java.util.ArrayList;
import java.util.Calendar;

import database.DataBaseConnector;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Room {
	private int roomID;
	private int[] seatID;
	private int[] windowID;
	private int seatCount;
	private int windowCount;
	private double LONG; // 经度
	private double LAT; // 纬度
	SunAngle sunAngle;
	double gam_sun;
	double h_sun;
	private final double PI = 3.14159265358979;
	private String pswd;

	Seat seat[];
	Window window[];

	public Room(int roomID) {
		DataBaseConnector dbConnector = new DataBaseConnector();

		ArrayList arrayListRoomCoordinate = dbConnector.GetRoomCoordinate(roomID);
		this.LONG = (double) arrayListRoomCoordinate.get(0);
		this.LAT = (double) arrayListRoomCoordinate.get(1);
		this.roomID = roomID;
		this.seatCount = dbConnector.GetSeatCount(roomID);
		this.windowCount = dbConnector.GetWindowCount(roomID);
		seatID = new int[seatCount];
		windowID = new int[windowCount];
		seat = new Seat[seatCount];
		window = new Window[windowCount];
		pswd=dbConnector.getRoomPSWD(roomID);
		List<Map<String, Object>> listSeatID;
		listSeatID = dbConnector.getRoomSeatID(roomID);
		for (int i = 0; i < seatCount; i++) {
			seatID[i] = (int) listSeatID.get(i).get("seatID");
			seat[seatID[i]] = new Seat(seatID[i], LONG, LAT);
		}

		List<Map<String, Object>> listWindowID;
		listWindowID = dbConnector.getRoomWindowID(roomID);
		for (int i = 0; i < windowCount; i++) {
			windowID[i] = (int) listWindowID.get(i).get("windowID");
			window[windowID[i]] = new Window(windowID[i], LONG, LAT);
		}
		
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;    //Calendar获取月份为0-11
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        sunAngle = new SunAngle(year, month, day, hour, minute, second, LONG, LAT);
        gam_sun = sunAngle.getResult_gam_sun();
        h_sun = sunAngle.getResult_h_sun();
	}

	public int getSeatCount(){
		return seatCount;
	}

	public int getWindowCount(){
		return windowCount;
	}
	
	public Seat[] getSeatArray(){
		return seat;
	}
	
	public Window[] getWindowArray(){
		return window;
	}
	
	public int[] getWindowID(){
		return windowID;
	}
	
	public int[] getSeatID(){
		return seatID;
	}
	
	public String getPSWD(){
		return pswd;
	}
	
	public double[] getSeatLeftTop(int seatNum){
		
		return seat[seatID[seatNum]].getSeatLeftTop();
	}
	
	public double[] getWindowLeftTop(int windowNum){
		return window[windowID[windowNum]].getWindowLeftTop();
	}
	
	public int getGamSun(){
		return (int) ((int) 180*gam_sun/PI);
	}
	public int getHSun(){
		return (int) ((int) 180*h_sun/PI);
	}
}
