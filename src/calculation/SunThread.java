package calculation;

import gui.WindowMenu;

public class SunThread extends Thread{
	private Seat seat;
	private Window window[];
	private int window_number;
	private WindowMenu menu;
	public SunThread(Seat seat,Window[] window,int window_number,WindowMenu menu){
		this.seat=seat;
		this.window=window;
		this.window_number=window_number;
		this.menu=menu;
	}
	public void run(){
		while(seat.getSeat_use()==true){
	        for(int i=0;i<window_number;i++){
	        	seat.judgement(window[i]);
	            window[i].get_state(menu,i);
	            //System.out.println("座位号："+seat.getSeat_namber());
	            try{
	            	Thread.sleep(10000);
	            }catch(InterruptedException e){
	            	System.out.println("线程出错！");
	            }
		}
        }
		for(int i=0;i<window_number;i++){
			seat.seat_cancel(window[i]);
			System.out.println("sss");
			window[i].get_state(menu,i);
		}
	}
}
