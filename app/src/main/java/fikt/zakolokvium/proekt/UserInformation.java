package fikt.zakolokvium.proekt;

/**
 * Created by User on 2/8/2017.
 */

public class UserInformation {

    private String humidity;
    private String WaterLevel;
    private String systemid;

    public UserInformation(){

    }

    public String getWaterLevel() {
        return WaterLevel;
    }

    public void setWaterLevel(String WaterLevel) {
        this.WaterLevel = WaterLevel;
    }

    public String gethumidity() {
        return humidity;
    }

    public void sethumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getsystemid() {
        return systemid;
    }

    public void setsystemid(String systemid) {
        this.systemid = systemid;
    }
}
