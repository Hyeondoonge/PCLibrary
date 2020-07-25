package Main;

public class User {
	private String id;
	private String password;
	private String name;
	private String nickName;
	private int code;
	private String phoneNum;
	private String mail;
	private String school;
	private String strCode;
	
	public User(String id, String password, String name, String nickName, int code, String phoneNum, String mail, String school) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.nickName = nickName;
		this.code = code;
		this.phoneNum = phoneNum;
		this.mail = mail;
		this.school = school;
		
		if(code == 1) {
			strCode = "切积";
		}
		else if(code == 2){
			strCode = "包府流盔";
		}
		else {
			strCode = "款康磊";
		}
	}

	public String getId() {
		return id;
	}
	
	public String getPassword() {
		return password;
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

	public String getMail() {
		return mail;
	}

	public String getSchool() {
		return school;
	}
	public String getStrCode() {
		return strCode;
	}
}
