package calculation;

import java.util.Map;
import java.util.Scanner;
import weather.Weather;
import gui.WindowMenu;
public class Test {
	public static void main(String[] args){
		
		
		//界面
		WindowMenu win = new WindowMenu(0,0,0,700,600);
		
		
		//天气
		String dataCity="",dataWeather="",dataMinTemp="",dataMaxTemp="";
		/*Weather weather=new Weather();
		try {
			Map<String, Object> map = weather.getTodayWeather("101020100");
			dataCity=map.get("city").toString();
			dataWeather=map.get("weather").toString();
			dataMinTemp=map.get("temp1").toString();
			dataMaxTemp=map.get("temp2").toString();
			System.out.println(map.get("city") + "\t" + map.get("temp1")
					+ "\t" + map.get("temp2") + "\t" + map.get("weather")
					+ "\t" + map.get("time"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//计算
		if(dataWeather=="晴天"){
		int x;
		SunThread sunthread;
        double seat_test[][][]={{{-0.2,1,0.6},{-0.2,1,1.2},{0.2,1,1.2},{0.2,1,0.6}},{{-0.2,1,0.6},{-0.2,1,1.2},{0.2,1,1.2},{0.2,1,0.6}}};
        double window_test[][][]={{{-0.5,0,1},{-0.5,0,1.5},{-0.5,0,2},{0,0,2},{0.5,0,2},{0.5,0,1.5},{0.5,0,1},{0,0,1},{0,0,1.5}},{{-0.5,0,1},{-0.5,0,1.5},{-0.5,0,2},{0,0,2},{0.5,0,2},{0.5,0,1.5},{0.5,0,1},{0,0,1},{0,0,1.5}}};
        int window_test_namber=1;
        int seat_test_namber[]={0,1};
        Window window[]={new Window(window_test[0][0],window_test[0][1],window_test[0][2],window_test[0][3],window_test[0][4],window_test[0][5],window_test[0][6],window_test[0][7],window_test[0][8],0),new Window(window_test[1][0],window_test[1][1],window_test[1][2],window_test[1][3],window_test[1][4],window_test[1][5],window_test[1][6],window_test[1][7],window_test[1][8],1)};
        Seat seat[]={new Seat(seat_test[0][0],seat_test[0][1],seat_test[0][2],seat_test[0][3],seat_test_namber[0]),new Seat(seat_test[1][0],seat_test[1][1],seat_test[1][2],seat_test[1][3],seat_test_namber[1])};
        System.out.println("请输入数字0-3：");
        x=new Scanner(System.in).nextInt();
        while(x!=88){
        switch(x){
        case 0:
        	seat[0].setSeat_use(true);
        	sunthread=new SunThread(seat[0], window, window_test_namber); 
        	sunthread.start();
        	break;
        case 1:
        	seat[0].setSeat_use(false);
        	break;
        case 2:
        	seat[1].setSeat_use(true);
        	sunthread=new SunThread(seat[1], window, window_test_namber); 
        	sunthread.start();
        	break;
        case 3:
        	seat[1].setSeat_use(false);
        	break;
        }
        System.out.println("请输入数字0-3：");
        x=new Scanner(System.in).nextInt();
        }
        }
		else{
			
		}*/
		
		
        }
}
