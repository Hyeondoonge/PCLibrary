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
            params.put("from", "01097762378"); //아니 직원 번호 넣고싶은데 등록된 번호로밖에 안된다고 하네 내가 직원이 되어야하나......
            params.put("type", "SMS");
            params.put("text", "PC 반납 예정일은 " + sdf.format(d) + "입니다.");
            params.put("app_version", "test app 1.2"); // application name and version

            try {
                JSONObject obj = (JSONObject) coolsms.send(params);
                System.out.println(obj.toString());
                System.out.println("보냈습니다.");
            } catch (CoolsmsException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCode());
            }
        }
    }
}
