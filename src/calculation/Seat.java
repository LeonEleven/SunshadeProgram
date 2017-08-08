package calculation;

import java.util.ArrayList;
import java.util.Calendar;
import database.DataBaseConnector;

public class Seat {
	DataBaseConnector dbConnector;

	private double seat_left_below_coordinate[] = new double[3]; // ���½�����
	private double seat_left_top_coordinate[] = new double[3]; // ���Ͻ�����
	private double seat_right_top_coordinate[] = new double[3]; // ���Ͻ�����
	private double seat_right_below_coordinate[] = new double[3]; // ���½�����

	// ͶӰ����
	private double result_seat_left_below_coordinate[] = new double[3]; // ���½�����
	private double result_seat_left_top_coordinate[] = new double[3]; // ���Ͻ�����
	private double result_seat_right_top_coordinate[] = new double[3]; // ���Ͻ�����
	private double result_seat_right_below_coordinate[] = new double[3]; // ���½�����

	private SunAngle sunAngle; // ̫������
	private double LONG; // ����
	private double LAT; // γ��
	private final double PI = 3.14159265358979; // Բ����
	private int start_hour = 11; // ����ʼ����ʱ��
	private int finish_hour = 15; // ������ֹ����ʱ��
	private int seat_number; // ��λ���к�
	private boolean seat_use = false; // ��λռ�����
	private int window_count;
	private int window_state[][];//= { { 0, 0, 0, 0 }, { 0, 0, 0, 0 } } // ����װ���ۼ�״̬��¼

	// ��ʼ��
	public Seat(double[] seat_left_below_coordinate, double[] seat_left_top_coordinate,
			double[] seat_right_top_coordinate, double[] seat_right_below_coordinate, int seat_namber) {
		this.seat_left_below_coordinate = seat_left_below_coordinate;
		this.seat_left_top_coordinate = seat_left_top_coordinate;
		this.seat_right_top_coordinate = seat_right_top_coordinate;
		this.seat_right_below_coordinate = seat_right_below_coordinate;
		this.seat_number = seat_namber;
	}

	// ���ݿ��ȡ���� ��ʼ��
	public Seat(int seat_number,double LONG,double LAT) {
		this.seat_number = seat_number;

		dbConnector = new DataBaseConnector();

		ArrayList arrayListSeatPos = dbConnector.GetSeatPositionByID(seat_number);

		this.seat_left_below_coordinate[0] = (double) arrayListSeatPos.get(3);
		this.seat_left_below_coordinate[1] = (double) arrayListSeatPos.get(4);
		this.seat_left_below_coordinate[2] = (double) arrayListSeatPos.get(5);

		this.seat_left_top_coordinate[0] = (double) arrayListSeatPos.get(0);
		this.seat_left_top_coordinate[1] = (double) arrayListSeatPos.get(1);
		this.seat_left_top_coordinate[2] = (double) arrayListSeatPos.get(2);

		this.seat_right_top_coordinate[0] = (double) arrayListSeatPos.get(6);
		this.seat_right_top_coordinate[1] = (double) arrayListSeatPos.get(7);
		this.seat_right_top_coordinate[2] = (double) arrayListSeatPos.get(8);

		this.seat_right_below_coordinate[0] = (double) arrayListSeatPos.get(9);
		this.seat_right_below_coordinate[1] = (double) arrayListSeatPos.get(10);
		this.seat_right_below_coordinate[2] = (double) arrayListSeatPos.get(11);

		ArrayList arrayListRoomCoordinate = dbConnector.GetRoomCoordinate(dbConnector.getSeatRoomID(seat_number));
		this.LONG = LONG;//(double) arrayListRoomCoordinate.get(0);
		this.LAT = LAT;//(double) arrayListRoomCoordinate.get(1);
		
		window_count=dbConnector.GetWindowCount(dbConnector.getSeatRoomID(seat_number));
		
		window_state=new int[window_count][4];
		
		for(int i=0;i<window_count;i++){
			window_state[i][0]=0;
			window_state[i][1]=0;
			window_state[i][2]=0;
			window_state[i][3]=0;
		}
	}
	

	// ��ʵ����ת��ͶӰ����
	private double[] seat_projection(double[] seat_coordinate) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; // Calendar��ȡ�·�Ϊ0-11
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		sunAngle = new SunAngle(year, month, day, hour, minute, second, LONG, LAT);
		double gam_sun = sunAngle.getResult_gam_sun();
		double h_sun = sunAngle.getResult_h_sun();
		double seat_projection[] = new double[3];
		if (gam_sun <= PI) {
			double correct_gam_sun = gam_sun - (PI / 2);
			double temp_one = Math.abs(seat_coordinate[1]) / Math.tan(correct_gam_sun);
			double result_x = seat_coordinate[0] + temp_one;
			double temp_two = Math.abs(seat_coordinate[1]) / Math.sin(correct_gam_sun);
			double temp_three = seat_coordinate[2] / Math.tan(h_sun);
			double temp_four = temp_two + temp_three;
			double result_z = seat_coordinate[2] * temp_four / temp_three;
			double result_y = 0;
			seat_projection[0] = result_x;
			seat_projection[1] = result_y;
			seat_projection[2] = result_z;
		} else if (gam_sun > PI) {
			double correct_gam_sun = 1.5 * PI - gam_sun;
			double temp_one = Math.abs(seat_coordinate[1]) / Math.tan(correct_gam_sun);
			double result_x = seat_coordinate[0] - temp_one;
			double temp_two = Math.abs(seat_coordinate[1]) / Math.sin(correct_gam_sun);
			double temp_three = seat_coordinate[2] / Math.tan(h_sun);
			double temp_four = temp_two + temp_three;
			double result_z = seat_coordinate[2] * temp_four / temp_three;
			double result_y = 0;
			seat_projection[0] = result_x;
			seat_projection[1] = result_y;
			seat_projection[2] = result_z;
		}
		return seat_projection;
	}

	// ��ȡͶӰ����
	private void get_result_seat_coordinate() {
		result_seat_left_below_coordinate = seat_projection(seat_left_below_coordinate);
		result_seat_left_top_coordinate = seat_projection(seat_left_top_coordinate);
		result_seat_right_top_coordinate = seat_projection(seat_right_top_coordinate);
		result_seat_right_below_coordinate = seat_projection(seat_right_below_coordinate);
		System.out.println("������λͶӰ����Ϊ:" + "(" + result_seat_left_below_coordinate[0] + ","
				+ result_seat_left_below_coordinate[1] + "," + result_seat_left_below_coordinate[2] + ")");
		System.out.println("������λͶӰ����Ϊ:" + "(" + result_seat_left_top_coordinate[0] + ","
				+ result_seat_left_top_coordinate[1] + "," + result_seat_left_top_coordinate[2] + ")");
		System.out.println("������λͶӰ����Ϊ:" + "(" + result_seat_right_top_coordinate[0] + ","
				+ result_seat_right_top_coordinate[1] + "," + result_seat_right_top_coordinate[2] + ")");
		System.out.println("������λͶӰ����Ϊ:" + "(" + result_seat_right_below_coordinate[0] + ","
				+ result_seat_right_below_coordinate[1] + "," + result_seat_right_below_coordinate[2] + ")");
	}

	// �ж����������
	public void judgement(Window win) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour >= start_hour && hour <= finish_hour) {
			win.get_result_window_coordinate();
			get_result_seat_coordinate();
			boolean judge_one = judge(result_seat_left_below_coordinate, result_seat_left_top_coordinate,
					result_seat_right_top_coordinate, result_seat_right_below_coordinate,
					win.getResult_left_below_coordinate(), win.getResult_left_middle_coordinate(),
					win.getResult_centre_coordinate(), win.getResult_below_middle_coordinate());
			boolean judge_two = judge(result_seat_left_below_coordinate, result_seat_left_top_coordinate,
					result_seat_right_top_coordinate, result_seat_right_below_coordinate,
					win.getResult_left_middle_coordinate(), win.getResult_left_top_coordinate(),
					win.getResult_top_middle_coordinate(), win.getResult_centre_coordinate());
			boolean judge_three = judge(result_seat_left_below_coordinate, result_seat_left_top_coordinate,
					result_seat_right_top_coordinate, result_seat_right_below_coordinate,
					win.getResult_centre_coordinate(), win.getResult_top_middle_coordinate(),
					win.getResult_right_top_coordinate(), win.getResult_right_middle_coordinate());
			boolean judge_four = judge(result_seat_left_below_coordinate, result_seat_left_top_coordinate,
					result_seat_right_top_coordinate, result_seat_right_below_coordinate,
					win.getResult_below_middle_coordinate(), win.getResult_centre_coordinate(),
					win.getResult_right_middle_coordinate(), win.getResult_right_below_coordinate());
			
			boolean judge[]={judge_one,judge_two,judge_three,judge_four};
			
			
			int temp[] = win.getWindow_state();
			int winCurrentNum=win.getWindow_number();
			for(int i=3;i>=0;i--){
				if(!judge[i]){
					if (window_state[winCurrentNum][i] != 0) {
						window_state[winCurrentNum][i]--;
						temp[i]--;
					}
				}else{
					window_state[winCurrentNum][i]++;
					temp[i]++;
				}
			}
			win.setWindow_state(temp);
		}
	}

	// ͶӰ���ж��Ƿ���Ҫ������ϵͳ
	private boolean judge(double[] judge_seat_left_below_coordinate, double[] judge_seat_left_top_coordinate,
			double[] judge_seat_right_top_coordinate, double[] judge_seat_right_below_coordinate,
			double[] judge_window_left_below_coordinate, double[] judge_window_left_top_coordinate,
			double[] judge_window_right_top_coordinate, double[] judge_window_right_below_coordinate) {
		double min_seat_x = Math.min(judge_seat_left_below_coordinate[0], judge_seat_left_top_coordinate[0]);
		min_seat_x = Math.min(judge_seat_right_top_coordinate[0], min_seat_x);
		min_seat_x = Math.min(judge_seat_right_below_coordinate[0], min_seat_x);
		double max_seat_x = Math.max(judge_seat_left_below_coordinate[0], judge_seat_left_top_coordinate[0]);
		max_seat_x = Math.max(judge_seat_right_top_coordinate[0], max_seat_x);
		max_seat_x = Math.max(judge_seat_right_below_coordinate[0], max_seat_x);
		double min_seat_z = Math.min(judge_seat_left_below_coordinate[2], judge_seat_left_top_coordinate[2]);
		min_seat_z = Math.min(judge_seat_right_top_coordinate[2], min_seat_z);
		min_seat_z = Math.min(judge_seat_right_below_coordinate[2], min_seat_z);
		double max_seat_z = Math.max(judge_seat_left_below_coordinate[2], judge_seat_left_top_coordinate[2]);
		max_seat_z = Math.max(judge_seat_right_top_coordinate[2], max_seat_z);
		max_seat_z = Math.max(judge_seat_right_below_coordinate[2], max_seat_z);

		double min_window_x = Math.min(judge_window_left_below_coordinate[0], judge_window_left_top_coordinate[0]);
		min_window_x = Math.min(judge_window_right_top_coordinate[0], min_window_x);
		min_window_x = Math.min(judge_window_right_below_coordinate[0], min_window_x);
		double max_window_x = Math.max(judge_window_left_below_coordinate[0], judge_window_left_top_coordinate[0]);
		max_window_x = Math.max(judge_window_right_top_coordinate[0], max_window_x);
		max_window_x = Math.max(judge_window_right_below_coordinate[0], max_window_x);
		double min_window_z = Math.min(judge_window_left_below_coordinate[2], judge_window_left_top_coordinate[2]);
		min_window_z = Math.min(judge_window_right_top_coordinate[2], min_window_z);
		min_window_z = Math.min(judge_window_right_below_coordinate[2], min_window_z);
		double max_window_z = Math.max(judge_window_left_below_coordinate[2], judge_window_left_top_coordinate[2]);
		max_window_z = Math.max(judge_window_right_top_coordinate[2], max_window_z);
		max_window_z = Math.max(judge_window_right_below_coordinate[2], max_window_z);
		if (((min_seat_x > min_window_x && min_seat_x < max_window_x)
				&& (min_seat_z > min_window_z && min_seat_z < max_window_z))
				|| ((min_seat_x > min_window_x && min_seat_x < max_window_x)
						&& (max_seat_z > min_window_z && max_seat_z < max_window_z))
				|| ((max_seat_x > min_window_x && max_seat_x < max_window_x)
						&& (min_seat_z > min_window_z && min_seat_z < max_window_z))
				|| ((max_seat_x > min_window_x && max_seat_x < max_window_x)
						&& (max_seat_z > min_window_z && max_seat_z < max_window_z))) {
			return true;
		} else
			return false;
	}

	// �����߳�ʱ����
	public void seat_cancel(Window win) {
		if (win.getWindow_number() == 0) {
			int temp[] = win.getWindow_state();
			temp[0] -= window_state[0][0];
			temp[1] -= window_state[0][1];
			temp[2] -= window_state[0][2];
			temp[3] -= window_state[0][3];
			win.setWindow_state(temp);
			for(int i=0;i<window_count;i++){
				window_state[i][0]=0;
				window_state[i][1]=0;
				window_state[i][2]=0;
				window_state[i][3]=0;
		}
		} else if (win.getWindow_number() == 1) {
			int temp[] = win.getWindow_state();
			temp[0] -= window_state[1][0];
			temp[1] -= window_state[1][1];
			temp[2] -= window_state[1][2];
			temp[3] -= window_state[1][3];
			win.setWindow_state(temp);
			for(int i=0;i<window_count;i++){
				window_state[i][0]=0;
				window_state[i][1]=0;
				window_state[i][2]=0;
				window_state[i][3]=0;
		}
		}
	}

	public boolean getSeat_use() {
		return seat_use;
	}

	public void setSeat_use(boolean seat_use) {
		this.seat_use = seat_use;
	}

	public int getSeat_namber() {
		return seat_number;
	}

	public void setSeat_namber(int seat_namber) {
		this.seat_number = seat_namber;
	}
	
	public double[] getSeatLeftTop(){
		return  seat_left_top_coordinate;
	}
}
