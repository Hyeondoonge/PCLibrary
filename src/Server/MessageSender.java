package Server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class MessageSender {
	private final String api_key = "NCS5QQEVYCD0IR2Z";
    private final String api_secret = "U5JR8AI784TCLQXDD4ZESEVGBXWCN9UN";
    Message coolsms = new Message(api_key, api_secret);
    
    public void sendMessage(ArrayList<String> list, String managerNum) {
        Date d = new Date();
        d.setTime(d.getTime() + (1000 * 60 * 60 * 24 * 7));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to", list.get(i));
            params.put("from", "01097762378"); //�ƴ� ���� ��ȣ �ְ������ ��ϵ� ��ȣ�ιۿ� �ȵȴٰ� �ϳ� ���� ������ �Ǿ���ϳ�......
            params.put("type", "SMS");
            params.put("text", "PC �ݳ� �������� " + sdf.format(d) + "�Դϴ�.");
            params.put("app_version", "test app 1.2"); // application name and version

            try {
                JSONObject obj = (JSONObject) coolsms.send(params);
                System.out.println(obj.toString());
                System.out.println("���½��ϴ�.");
            } catch (CoolsmsException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCode());
            }
        }
    }
}
