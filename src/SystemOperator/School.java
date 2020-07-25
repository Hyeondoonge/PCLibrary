package SystemOperator;

public class School {
	
	String school, address, tel;
	
		public School(String school, String address, String tel) {
			this.school = school;
			this.address = address;
			this.tel = tel;
		}
		
		public String getSchool() {
			return school;
		}
		public String getAddress() {
			return address;
		}
		public String getTel() {
			return tel;
		}
}
