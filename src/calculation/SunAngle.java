package calculation;

public class SunAngle {
	private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private double LONG;  //经度
    private double LAT;   //纬度
    private final double PI = 3.14159265358979;
    private double result_h_sun;
    private  double result_gam_sun;

    public SunAngle(int year,int month,int day,int hour,int minute,int second,double LONG,double LAT){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.LONG = LONG*PI/180;
        this.LAT = LAT*PI/180;
        this.result_h_sun=h_sun();
        this.result_gam_sun=gam_sun();
    }

    public boolean year_judge(int y){
        if((y%4==0&&y%100!=0)||y%400==0) return true;
        else return false;
    }

    public int  n_ca(int a,int b,int c){
        int n=0;
        switch(a)
        {
            case 1:
                n=b;break;
            case 2:
                n=31+b;break;
            case 3:
                n=31+28+b;break;
            case 4:
                n=31+28+31+b;break;
            case 5:
                n=31+28+31+30+b;break;
            case 6:
                n=31+28+31+30+31+b;break;
            case 7:
                n=31+28+31+30+31+30+b;break;
            case 8:
                n=31+28+31+30+31+30+31+b;break;
            case 9:
                n=31+28+31+30+31+30+31+31+b;break;
            case 10:
                n=31+28+31+30+31+30+31+31+30+b;break;
            case 11:
                n=31+28+31+30+31+30+31+31+30+31+b;break;
            case 12:
                n=31+28+31+30+31+30+31+31+30+31+30+b;break;
            default:
                System.out.println("月份输入错误!");
        }
        if(a>2&&year_judge(c))n++;
        return  n;
    }

    public double angle_day_ca(int y){
        int n=n_ca(month,day,year);
        double n0=79.6764+0.2433*(y-1985)-((y-1985)/4);
        double t=n-n0;
        return 2*PI*t/365.2422;
    }

    public double delt_sun_ca(){
        int a=n_ca(month,day,year);
        return  (0.006918-0.399912*Math.cos(2*PI*(a-1)/365)+0.070257*Math.sin(2*PI*(a-1)/365)-0.006758*Math.cos(2*2*PI*(a-1)/365)+0.000907*Math.sin(2*2*PI*(a-1)/365)-0.002697*Math.cos(3*2*PI*(a-1)/365)+0.00148*Math.sin(3*2*PI*(a-1)/365));
    }

    public double et_ca(){
        double a=angle_day_ca(year);
        return 0.0028-1.9857*Math.sin(a)+9.9059*Math.sin(2*a)-7.0924*Math.cos(a)-0.6882*Math.cos(2*a);
    }

    public double angle_hour_ca(){
        double e=et_ca();
        return  ((hour+(LONG*180/PI-120)*4/60+(e+minute)/60+second/3600)-12)*15*PI/180;
    }

    public double h_sun(){
        double delt_sun;
        double angle_hour;
        double sin_h;
        double h_sun;
        delt_sun=delt_sun_ca();
        angle_hour=angle_hour_ca();
        sin_h=Math.sin(LAT)*Math.sin(delt_sun)+Math.cos(LAT)*Math.cos(delt_sun)*Math.cos(angle_hour);
        h_sun=Math.asin(sin_h);
        return h_sun;
    }

    public double gam_sun(){
        double delt_sun;
        double angle_hour;
        double sin_h;
        double h_sun;
        double cos_gam;
        double gam_sun;
        double gam_sun1;
        delt_sun=delt_sun_ca();
        angle_hour=angle_hour_ca();
        sin_h=Math.sin(LAT)*Math.sin(delt_sun)+Math.cos(LAT)*Math.cos(delt_sun)*Math.cos(angle_hour);
        h_sun=Math.asin(sin_h);
        cos_gam=(sin_h*Math.sin(LAT)-Math.sin(delt_sun))/(Math.cos(h_sun)*Math.cos(LAT));
        gam_sun=Math.acos(cos_gam);
        gam_sun1=gam_sun*180/PI;
        if(hour>12&&hour<24)gam_sun1=-gam_sun1;
        gam_sun1=180-gam_sun1;
        gam_sun1=gam_sun1*PI/180;
        return gam_sun1;
    }

    public double getResult_h_sun() {
        return result_h_sun;
    }

    public double getResult_gam_sun() {
        return result_gam_sun;
    }
}
