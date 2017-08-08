package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bhL on 2017/2/22.
 */
public class DataBaseConnector {

    DataBaseModule dbModule;

    public DataBaseConnector() {
        dbModule = new DataBaseModule();
    }

    //根据座位号获取该座位的坐标
    public ArrayList GetSeatPositionByID(int seatID) {
        ArrayList arrayList = new ArrayList();
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT * FROM seats where seatID=" + seatID);
        arrayList.add(0, list.get(0).get("leftTopPosX"));
        arrayList.add(1, list.get(0).get("leftTopPosY"));
        arrayList.add(2, list.get(0).get("leftTopPosZ"));
        arrayList.add(3, list.get(0).get("leftBelowPosX"));
        arrayList.add(4, list.get(0).get("leftBelowPosY"));
        arrayList.add(5, list.get(0).get("leftBelowPosZ"));
        arrayList.add(6, list.get(0).get("rightTopPosX"));
        arrayList.add(7, list.get(0).get("rightTopPosY"));
        arrayList.add(8, list.get(0).get("rightTopPosZ"));
        arrayList.add(9, list.get(0).get("rightBelowPosX"));
        arrayList.add(10, list.get(0).get("rightBelowPosY"));
        arrayList.add(11, list.get(0).get("rightBelowPosZ"));

        return arrayList;
    }
    //根据房间号获取座位数
    public int GetSeatCount(int roomID){
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT count(*) as cnt FROM seats where roomID=" + roomID);
    	return Integer.parseInt(String.valueOf(list.get(0).get("cnt")));
    }
    //根据窗户号获取该窗户的坐标    
    public ArrayList GetWindowPositionByID(int windowID) {
        ArrayList arrayList = new ArrayList();
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT * FROM windows where windowID=" + windowID);
        arrayList.add(0, list.get(0).get("leftTopPosX"));
        arrayList.add(1, list.get(0).get("leftTopPosY"));
        arrayList.add(2, list.get(0).get("leftTopPosZ"));
        arrayList.add(3, list.get(0).get("leftBelowPosX"));
        arrayList.add(4, list.get(0).get("leftBelowPosY"));
        arrayList.add(5, list.get(0).get("leftBelowPosZ"));
        arrayList.add(6, list.get(0).get("rightTopPosX"));
        arrayList.add(7, list.get(0).get("rightTopPosY"));
        arrayList.add(8, list.get(0).get("rightTopPosZ"));
        arrayList.add(9, list.get(0).get("rightBelowPosX"));
        arrayList.add(10, list.get(0).get("rightBelowPosY"));
        arrayList.add(11, list.get(0).get("rightBelowPosZ"));

        return arrayList;
    }
    //根据房间号获取窗户数
    public int GetWindowCount(int roomID){
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT count(*) as cnt FROM windows where roomID=" + roomID);
    	return Integer.parseInt(String.valueOf(list.get(0).get("cnt")));
    }
    //获取房间经纬度
    public ArrayList GetRoomCoordinate(int roomID){
        ArrayList arrayList = new ArrayList();
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT * FROM rooms where roomID=" + roomID);
        arrayList.add(0,list.get(0).get("LONG"));
        arrayList.add(1,list.get(0).get("LAT"));
        
        return arrayList;
        
    }
    
    public int getSeatRoomID(int seatID){
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT roomID FROM seats where seatID=" + seatID);
        return Integer.parseInt(String.valueOf(list.get(0).get("roomID")));
    }
    
    public int getWindowRoomID(int windowID){
        List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT windowID FROM windows where windowID=" + windowID);
        return Integer.parseInt(String.valueOf(list.get(0).get("windowID")));
    }
    
    public List getRoomSeatID(int roomID){
    	List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT seatID FROM seats where roomID=" + roomID);
        
        return list;
    }
    public List getRoomWindowID(int roomID){
    	List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT windowID FROM windows where roomID=" + roomID);
        
        return list;
    }
    
    public String getRoomPSWD(int roomID){
    	List<Map<String, Object>> list;
        list = dbModule.getSqlQueryResToList("SELECT PSWD FROM rooms where roomID=" + roomID);
        
        return (String) list.get(0).get("PSWD");
    }
    @Override
    protected void finalize() throws Throwable {
        dbModule.release();
        super.finalize();
    }
}
