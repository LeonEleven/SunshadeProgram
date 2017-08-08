package weather;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * java调用中央气象局天气预报接口
 * 
 * @author Administrator
 * 
 */
public class Weather {
	/**
	 * 
	 * 获取实时天气2<br>
	 * 方 法 名： getTodayWeather <br>
	 * 
	 * @param Cityid
	 *            城市编码
	 */
	public static Map<String, Object> getTodayWeather(String Cityid)
			throws IOException, NullPointerException {
		// 连接中央气象台的API
		URL url = new URL("http://www.weather.com.cn/data/cityinfo/" + Cityid
				+ ".html");
		URLConnection connectionData = url.openConnection();
		connectionData.setConnectTimeout(1000);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connectionData.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line);
			String datas = sb.toString();
			System.out.println(datas);
			JSONObject jsonData = JSONObject.fromObject(datas);
			JSONObject info = jsonData.getJSONObject("weatherinfo");
			map.put("city", info.getString("city").toString());// 城市
			map.put("temp1", info.getString("temp1").toString());// 最高温度
			map.put("temp2", info.getString("temp2").toString());// 最低温度
			map.put("weather", info.getString("weather").toString());//天气
			map.put("ptime", info.getString("ptime").toString());// 发布时间

		} catch (SocketTimeoutException e) {
			System.out.println("连接超时");
		} catch (FileNotFoundException e) {
			System.out.println("加载文件出错");
		}

		return map;
		}
	public static void main(String[] args) {
		try {
			Map<String, Object> map = getTodayWeather("101020100");
			System.out.println(map.get("city") + "\t" + map.get("temp1")
					+ "\t" + map.get("temp2") + "\t" + map.get("weather")
					+ "\t" + map.get("time"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
