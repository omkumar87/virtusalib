package om.test.virtusalib;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by omahalingam on 6/16/17.
 */

public class WeatherManager {
    OkHttpClient client = new OkHttpClient();
    Notifiable actNotifiable;
    private static WeatherManager weathermanager;
    private WeatherManager(){

    }
    public static WeatherManager getWeatherManager(){
        if(weathermanager == null){
            weathermanager = new WeatherManager();
        }
        return weathermanager;
    }

    public void getWeatherData(String cityname,Notifiable notifiable){
        actNotifiable = notifiable;
        AsyncThreadV asyncThreadV = new AsyncThreadV();
        asyncThreadV.execute(cityname);
        actNotifiable = notifiable;
    }

    private class AsyncThreadV extends AsyncTask<String,Void,WeatherData> {

        @Override
        protected WeatherData doInBackground(String... strings) {
            Request.Builder builder = new Request.Builder();
            builder.url(Consts.URL_Weather+ strings[0] + Consts.token);
            Request request = builder.build();

            Request.Builder builder2 = new Request.Builder();

            try {
                Response response = client.newCall(request).execute();
                WeatherData wd = getJsonParsedData(response.body().string());
                System.out.print("Success Om" + Consts.ICON_URL+wd.icon);
                builder2.url(Consts.ICON_URL+wd.icon);
                if(wd != null) {
                    System.out.print("Success Om" + response.isSuccessful() + "  " + Consts.ICON_URL+wd.icon);
                    Request request2 = builder2.build();
                    response = client.newCall(request2).execute();
                    System.out.print("Success Om" + response.isSuccessful() + "  " + Consts.ICON_URL+wd.icon);
                    if (response.isSuccessful()) {
                        ResponseBody in = response.body();
                        System.out.print("Success " + Consts.ICON_URL+wd.icon+ " " +  in.bytes());
                        InputStream inputStream = in.byteStream();
                        // convert inputstram to bufferinoutstream
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        wd.bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    }
                }

            return wd;
            }catch (IOException e) {
                System.out.print("eXCEPTION");
                e.printStackTrace();
            }catch (Exception e){
                System.out.print("eXCEPTION");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(WeatherData weatherData) {
            super.onPostExecute(weatherData);
            actNotifiable.updateData(weatherData);
        }

        public WeatherData getJsonParsedData(String jsondata) {
            WeatherData wd = new WeatherData();
            try {
                JSONObject jsonobj =  new JSONObject(jsondata);
                if(jsonobj != null){
                    JSONArray weather = jsonobj.optJSONArray(Consts.KEY_WEATHER);
                    if(weather != null && weather.length() > 0){
                        JSONObject fw = (JSONObject) weather.get(0);
                        if(fw != null){
                            wd.condition = fw.optString(Consts.KEY_MAIN);
                            wd.desc = fw.optString(Consts.KEY_DESC);
                            wd.icon = fw.optString(Consts.KEY_ICON);
                        }
                    }

                    JSONObject main = jsonobj.optJSONObject(Consts.KEY_MAIN);
                    if(main != null){
                            wd.temp = main.optString(Consts.KEY_TEMP);
                            wd.pressure = main.optString(Consts.KEY_PRESSURE);
                    }

                    wd.city = jsonobj.optString(Consts.KEY_NAME);

                    JSONObject wind = jsonobj.optJSONObject(Consts.KEY_WIND);
                    if(wind != null){
                        wd.windspeed = wind.optString(Consts.KEY_SPEED);
                    }


                }
                return wd;

            } catch (JSONException e) {
                e.printStackTrace();
            }
           return null;
        }
    }

}
