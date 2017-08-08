package calculation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import database.DataBaseConnector;

import gui.WindowMenu;

public class Window {
	DataBaseConnector dbConnector;
	private double[] left_below_coordinate=new double[3];//���±߽�����
    private double[] left_middle_coordinate=new double[3];//���б߽�����
    private double[] left_top_coordinate=new double[3];//���ϱ߽������
    private double[] top_middle_coordinate=new double[3];//���б߽�����
    private double[] right_top_coordinate=new double[3];//���ϱ߽�����
    private double[] right_middle_coordinate=new double[3];//���б߽�����
    private double[] right_below_coordinate=new double[3];//���±߽�����
    private double[] below_middle_coordinate=new double[3];//���б߽�����
    private double[] centre_coordinate=new double[3];//��������

    //ͶӰ����
    private double[] result_left_below_coordinate=new double[3];//���±߽�����
    private double[] result_left_middle_coordinate=new double[3];//���б߽�����
    private double[] result_left_top_coordinate=new double[3];//���ϱ߽������
    private double[] result_top_middle_coordinate=new double[3];//���б߽�����
    private double[] result_right_top_coordinate=new double[3];//���ϱ߽�����
    private double[] result_right_middle_coordinate=new double[3];//���б߽�����
    private double[] result_right_below_coordinate=new double[3];//���±߽�����
    private double[] result_below_middle_coordinate=new double[3];//���б߽�����
    private double[] result_centre_coordinate=new double[3];//��������
    private int window_number; //�������к�
    private int window_state[]={0,0,0,0};  //����װ���ۼ�״̬��¼
    private boolean window_state_two[]={false,false,false,false};//����װ������״̬

    private SunAngle sunAngle; //̫������
    private double LONG=121.393; //����
    private double LAT=31.315;  //γ��
    private final double PI = 3.14159265358979;  //Բ����

    //��ʼ��
    public Window(double left_below_coordinate[],double left_middle_coordinate[],double left_top_coordinate[],double top_middle_coordinate[],double right_top_coordinate[],double right_middle_coordinate[],double right_below_coordinate[],double below_middle_coordinate[],double centre_coordinate[],int window_namber){
        this.left_below_coordinate=left_below_coordinate;
        this.left_middle_coordinate=left_middle_coordinate;
        this.left_top_coordinate=left_top_coordinate;
        this.top_middle_coordinate=top_middle_coordinate;
        this.right_top_coordinate=right_top_coordinate;
        this.right_middle_coordinate=right_middle_coordinate;
        this.right_below_coordinate=right_below_coordinate;
        this.below_middle_coordinate=below_middle_coordinate;
        this.centre_coordinate=centre_coordinate;
        this.window_number=window_namber;
    }
    //���ݿ��ȡ���ݳ�ʼ��
    public Window(int window_number,double LONG,double LAT){
	   dbConnector=new DataBaseConnector();
	   this.window_number=window_number;
	   
	   ArrayList arrayListSeatPos = dbConnector.GetWindowPositionByID(window_number);
	   
       this.left_below_coordinate[0]=(double) arrayListSeatPos.get(3);
       this.left_below_coordinate[1]=(double) arrayListSeatPos.get(4);
       this.left_below_coordinate[2]=(double) arrayListSeatPos.get(5);
       
       this.left_top_coordinate[0]=(double) arrayListSeatPos.get(0);
       this.left_top_coordinate[1]=(double) arrayListSeatPos.get(1);
       this.left_top_coordinate[2]=(double) arrayListSeatPos.get(2);
       
       
       this.right_top_coordinate[0]=(double) arrayListSeatPos.get(6);
       this.right_top_coordinate[1]=(double) arrayListSeatPos.get(7);
       this.right_top_coordinate[2]=(double) arrayListSeatPos.get(8);
       
       this.right_below_coordinate[0]=(double) arrayListSeatPos.get(9);
       this.right_below_coordinate[1]=(double) arrayListSeatPos.get(10);
       this.right_below_coordinate[2]=(double) arrayListSeatPos.get(11);
       
       for(int i=0;i<3;i++){
    	   this.left_middle_coordinate[i]=(this.left_top_coordinate[i]+this.left_below_coordinate[i])/2;
    	   this.top_middle_coordinate[i]=(this.left_top_coordinate[i]+this.right_top_coordinate[i])/2;
    	   this.right_middle_coordinate[i]=(this.right_top_coordinate[i]+this.right_below_coordinate[i])/2;
    	   this.below_middle_coordinate[i]=(this.right_below_coordinate[i]+this.left_below_coordinate[i])/2;
    	   this.centre_coordinate[i]=(this.top_middle_coordinate[i]+this.below_middle_coordinate[i])/2;
       }
       /*
       ArrayList arrayListRoomCoordinate = dbConnector.GetRoomCoordinate(dbConnector.getWindowRoomID(window_number));
       this.LONG = (double) arrayListRoomCoordinate.get(0);
       this.LAT = (double) arrayListRoomCoordinate.get(1);
       */
       this.LONG=LONG;
       this.LAT=LAT;
   }

    //��ʵ����ת��ͶӰ����
    private synchronized double[] window_projection(double[] window_coordinate){
        double window_projection[]=new double[3];
        if(window_coordinate[1]!=0){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;    //Calendar��ȡ�·�Ϊ0-11
            int day = calendar.get(Calendar.DATE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            sunAngle = new SunAngle(year, month, day, hour, minute, second, LONG, LAT);
            double gam_sun = sunAngle.getResult_gam_sun();
            double h_sun = sunAngle.getResult_h_sun();
            if(window_coordinate[1]>0){
                if (gam_sun <= PI) {
                    double correct_gam_sun = gam_sun - (PI / 2);
                    double temp_one = Math.abs(window_coordinate[1]) / Math.tan(correct_gam_sun);
                    double result_x = window_coordinate[0] + temp_one;
                    double temp_two = Math.abs(window_coordinate[1]) / Math.sin(correct_gam_sun);
                    double temp_three = window_coordinate[2] / Math.tan(h_sun);
                    double temp_four = temp_two + temp_three;
                    double result_z = window_coordinate[2] * temp_four / temp_three;
                    double result_y = 0;
                    window_projection[0]=result_x;
                    window_projection[1]=result_y;
                    window_projection[2]=result_z;
                }else if (gam_sun > PI) {
                    double correct_gam_sun = 1.5 * PI - gam_sun;
                    double temp_one = Math.abs(window_coordinate[1]) / Math.tan(correct_gam_sun);
                    double result_x = window_coordinate[0] - temp_one;
                    double temp_two = Math.abs(window_coordinate[1]) / Math.sin(correct_gam_sun);
                    double temp_three = window_coordinate[2] / Math.tan(h_sun);
                    double temp_four = temp_two + temp_three;
                    double result_z = window_coordinate[2] * temp_four / temp_three;
                    double result_y = 0;
                    window_projection[0]=result_x;
                    window_projection[1]=result_y;
                    window_projection[2]=result_z;
                }
            } else if(window_coordinate[1]<0){
                if (gam_sun <= PI) {
                    double correct_gam_sun = gam_sun - (PI / 2);
                    double temp_one = Math.abs(window_coordinate[1]) / Math.tan(correct_gam_sun);
                    double result_x = window_coordinate[0] - temp_one;
                    double temp_two = Math.abs(window_coordinate[1]) / Math.sin(correct_gam_sun);
                    double temp_three = window_coordinate[2] / Math.tan(h_sun);
                    double temp_four = temp_three-temp_two;
                    double result_z = window_coordinate[2] * temp_four / temp_three;
                    double result_y = 0;
                    window_projection[0]=result_x;
                    window_projection[1]=result_y;
                    window_projection[2]=result_z;
                } else if (gam_sun > PI) {
                    double correct_gam_sun = 1.5 * PI - gam_sun;
                    double temp_one = Math.abs(window_coordinate[1]) / Math.tan(correct_gam_sun);
                    double result_x = window_coordinate[0] + temp_one;
                    double temp_two = Math.abs(window_coordinate[1]) / Math.sin(correct_gam_sun);
                    double temp_three = window_coordinate[2] / Math.tan(h_sun);
                    double temp_four = temp_three-temp_two;
                    double result_z = window_coordinate[2] * temp_four / temp_three;
                    double result_y = 0;
                    window_projection[0]=result_x;
                    window_projection[1]=result_y;
                    window_projection[2]=result_z;
                }
            }
        }
        else if(window_coordinate[1]==0){
            window_projection[0]=window_coordinate[0];
            window_projection[1]=window_coordinate[1];
            window_projection[2]=window_coordinate[2];
        }
        return window_projection;
    }

    //��ȡͶӰ����
    public synchronized void get_result_window_coordinate(){
        result_left_below_coordinate=window_projection(left_below_coordinate);
        result_left_middle_coordinate=window_projection(left_middle_coordinate);
        result_left_top_coordinate=window_projection(left_top_coordinate);
        result_top_middle_coordinate=window_projection(top_middle_coordinate);
        result_right_top_coordinate=window_projection(right_top_coordinate);
        result_right_middle_coordinate=window_projection(right_middle_coordinate);
        result_right_below_coordinate=window_projection(right_below_coordinate);
        result_below_middle_coordinate=window_projection(below_middle_coordinate);
        result_centre_coordinate=window_projection(centre_coordinate);
        System.out.println("���´���ͶӰ����Ϊ:"+"("+result_left_below_coordinate[0]+","+result_left_below_coordinate[1]+","+result_left_below_coordinate[2]+")");
        System.out.println("���д���ͶӰ����Ϊ:"+"("+result_left_middle_coordinate[0]+","+result_left_middle_coordinate[1]+","+result_left_middle_coordinate[2]+")");
        System.out.println("���ϴ���ͶӰ����Ϊ:"+"("+result_left_top_coordinate[0]+","+result_left_top_coordinate[1]+","+result_left_top_coordinate[2]+")");
        System.out.println("���д���ͶӰ����Ϊ:"+"("+result_top_middle_coordinate[0]+","+result_top_middle_coordinate[1]+","+result_top_middle_coordinate[2]+")");
        System.out.println("���ϴ���ͶӰ����Ϊ:"+"("+result_right_top_coordinate[0]+","+result_right_top_coordinate[1]+","+result_right_top_coordinate[2]+")");
        System.out.println("���д���ͶӰ����Ϊ:"+"("+result_right_middle_coordinate[0]+","+result_right_middle_coordinate[1]+","+result_right_middle_coordinate[2]+")");
        System.out.println("���´���ͶӰ����Ϊ:"+"("+result_right_below_coordinate[0]+","+result_right_below_coordinate[1]+","+result_right_below_coordinate[2]+")");
        System.out.println("���д���ͶӰ����Ϊ:"+"("+result_below_middle_coordinate[0]+","+result_below_middle_coordinate[1]+","+result_below_middle_coordinate[2]+")");
        System.out.println("���Ĵ���ͶӰ����Ϊ:"+"("+result_centre_coordinate[0]+","+result_centre_coordinate[1]+","+result_centre_coordinate[2]+")");
    }

    //�޸�����װ��״̬
    public synchronized void get_state(WindowMenu menu,int i){
        if(window_state[0]==0){
            if(window_state_two[0]==false){
                //System.out.println("������������ͣ��");   
            } else{
            	//System.out.println("ͣ������������");
            	menu.label[i][1][0].setBackground(Color.white);
                window_state_two[0]=false;
            }
        }else if(window_state[0]>0){
            if(window_state_two[0]==false){
                //System.out.println("��������������");
            	menu.label[i][1][0].setBackground(Color.black);
                window_state_two[0]=true;
            }else{
                //System.out.println("����������������");
            }
        }

        if(window_state[1]==0){
            if(window_state_two[1]==false){
                //System.out.println("������������ͣ��");
            } else{
                //System.out.println("ͣ������������");
                menu.label[i][0][0].setBackground(Color.white);
                window_state_two[1]=false;
            }
        }else if(window_state[1]>0){
            if(window_state_two[1]==false){
                //System.out.println("��������������");
            	menu.label[i][0][0].setBackground(Color.black);
                window_state_two[1]=true;
            }else{
                //System.out.println("����������������");
            }
        }

        if(window_state[2]==0){
            if(window_state_two[2]==false){
                //System.out.println("������������ͣ��");
            } else{
                //System.out.println("ͣ������������");
            	menu.label[i][0][1].setBackground(Color.white);
                window_state_two[2]=false;
            }
        }else if(window_state[2]>0){
            if(window_state_two[2]==false){
                //System.out.println("��������������");
            	menu.label[i][0][1].setBackground(Color.black);
                window_state_two[2]=true;
            }else{
                //System.out.println("����������������");
            }
        }

        if(window_state[3]==0){
            if(window_state_two[3]==false){
                //System.out.println("������������ͣ��");
            } else{
                //System.out.println("ͣ������������");
            	menu.label[i][1][1].setBackground(Color.white);
                window_state_two[3]=false;
            }
        }else if(window_state[3]>0){
            if(window_state_two[3]==false){
                //System.out.println("��������������");
            	menu.label[i][1][1].setBackground(Color.black);
                window_state_two[3]=true;
            }else{
                //System.out.println("����������������");
            }
        }
    }

    public void setWindow_state(int[] window_state) {
        this.window_state = window_state;
    }

    public double[] getResult_left_below_coordinate() {
        return result_left_below_coordinate;
    }

    public double[] getResult_left_middle_coordinate() {
        return result_left_middle_coordinate;
    }

    public double[] getResult_left_top_coordinate() {
        return result_left_top_coordinate;
    }

    public double[] getResult_top_middle_coordinate() {
        return result_top_middle_coordinate;
    }

    public double[] getResult_right_top_coordinate() {
        return result_right_top_coordinate;
    }

    public double[] getResult_right_middle_coordinate() {
        return result_right_middle_coordinate;
    }

    public double[] getResult_right_below_coordinate() {
        return result_right_below_coordinate;
    }

    public double[] getResult_below_middle_coordinate() {
        return result_below_middle_coordinate;
    }

    public double[] getResult_centre_coordinate() {
        return result_centre_coordinate;
    }

    public int[] getWindow_state() {
        return window_state;
    }

    public int getWindow_number() {
        return window_number;
    }
    
	public double[] getWindowLeftTop(){
		return  left_top_coordinate;
	}
}
