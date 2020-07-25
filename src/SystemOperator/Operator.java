package SystemOperator;

public class Operator {
		
	String id;
	String name;
	String nickName;
	int code;
	String phoneNum;
	
	public Operator(String id, String name, String nickName, int code, String phoneNum) {
		this.id = id;
		this.name = name;
		this.nickName = nickName;
		this.code = code;
		this.phoneNum = phoneNum;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getNickName() {
		return nickName;
	}
	public int getCode() {
		return code;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	
	
}
