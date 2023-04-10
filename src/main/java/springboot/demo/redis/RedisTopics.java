package springboot.demo.redis;


public class RedisTopics {
    public final static String Device_Regist = "thcl:cache:platform:regist";

    public final static String Iot_Device = "iot:device:%s";

    public final static String Elevatorstar_Regist = "thcl:cache:elevatorstar:regist:lift_code:%s:serial_number";

    public final static String Qiaotong_Liftid_BindSn = "thcl:cache:qiaotong:regist:lift_id:%s:serial_number";

    public final static int Expire_Normal = 180;
}
