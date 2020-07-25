package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import Main.User;

public class SQL {
	private String IP;
	private Connection conn;
	private User info;
	private String data;
	private StringTokenizer st;
	Statement state = null;
	ResultSet res = null;
	String SQL;

	public SQL() {
		try {
			IP = "127.0.0.1";
			conn = DriverManager.getConnection("jdbc:mysql://localhost/servermanagement?useSSL=false&serverTimezone=UTC",
					"root", "Dtd0613~~");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public SQL(String IP) {
		try {
			this.IP = IP;
			conn = DriverManager.getConnection("jdbc:mysql://localhost/servermanagement?useSSL=false&serverTimezone=UTC",
					"root", "Dtd0613~~");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SQL(String IP, String id, String pw) {
		try {
			this.IP = IP;
			conn = DriverManager.getConnection("jdbc:mysql://localhost/servermanagement?useSSL=false&serverTimezone=UTC",
					"root", "Dtd0613~~");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void execute1() {
		try {
			state = conn.createStatement();
			String sql = "select 대여자.대여자id, 사용자.성명, 대여자.IP주소, pc모델.코드명 as PC, pc정보.pc_id, pc정보.password\r\n"
					+ "from 대여자, 사용자, pc정보, (select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as pc모델 \r\n"
					+ "where 대여자.대여자id = 사용자.id and 대여자.IP주소 = pc정보.IP주소 and pc정보.PC_모델코드 = pc모델.코드;";
			res = state.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execute2() {
		try {
			state = conn.createStatement();
			String sql = "select 학생.학생id, 사용자.닉네임, 사용자.성명, 학생.휴대전화번호, 학생.이메일 \r\n" + "from 사용자, 학생\r\n"
					+ "where 학생.학교코드 = '87' and 사용자.id = 학생.학생id;"; // 저 87도 가져와야 한다...
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // 임시로 execute1, execute2 이런 식으로 해놨지만 실제로는 한 함수로 합쳐셔 switch문으로 묶는 식으로???

	public String doLogin(String id, String pw) {
		try {
			state = conn.createStatement();
			String result = "";
			
			String sql = "select 회원.id, 회원.password, 회원.성명, 회원.닉네임, 회원.구분코드, 회원.휴대전화번호 as 전화번호, 회원.이메일, 공통코드상세.코드명 as 학교명\r\n" + 
					"from\r\n" + 
					"(select * from 사용자, 학생 where 사용자.id = 학생.학생id\r\n" + 
					"union \r\n" + 
					"select * from 사용자, pc_관리직원 where 사용자.id = pc_관리직원.pc_관리직원id\r\n" + 
					"union\r\n" + 
					"select 사용자.id, 사용자.password, 사용자.성명, 사용자.닉네임, 사용자.구분코드, 운영자.운영자id, null, 운영자.전화번호, 운영자.이메일 from 사용자, 운영자 where 사용자.id = 운영자.운영자id) as 회원 left join 공통코드상세 on \r\n" + 
					"공통코드상세.공통코드 = 001 and 공통코드상세.코드 = 회원.학교코드 \r\n" + 
					"where 회원.id = '"+ id +"' and 회원.password = '"+ pw +"';";
			res = state.executeQuery(sql);
			
			while(res.next()) {
				result += res.getString(1) + "#";
				result += res.getString(2) + "#";
				result += res.getString(3) + "#";
				result += res.getString(4) + "#";
				result += res.getString(5) + "#";
				result += res.getString(6) + "#";
				result += res.getString(7) + "#";
				result += res.getString(8) + "#";
			}
			
			if(!result.equals("")) {
				sql = "select 사용자_상태코드 from 사용자접근권한 where id='"+id+"';";
				res = state.executeQuery(sql);
				
				while(res.next()) {
				result += res.getString(1) + "#"; }
			}
			
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String searchSchool(String word) {
		try {
			state = conn.createStatement();
			String sql = "\r\n" + 
					"select 대학.코드명 as 학교명 \r\n" + 
					"from 대학교, \r\n" + 
					"(select * from 공통코드상세 where 공통코드 = '001') as 대학\r\n" + 
					"where 대학교.학교코드 = 대학.코드 and 대학.코드명 like '"+word+"%';";
			res = state.executeQuery(sql);
			
			String result ="";
			
			while(res.next()) {
				result += res.getString(1) + "#";
			}
			return result;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void signforUser(String name, String nickname, String school, String code, 
			String id, String pw) {
		
		try {
			state = conn.createStatement();
			String userCode = "00"+code;
			
			String sql = "insert into 사용자 values ('"+id+"', '"+pw+"', '"+name+"', '"+nickname+"', '"+userCode+"');\r\n"; 
			state.executeUpdate(sql);
			sql = "set @tempCode = (select 공통코드상세.코드 from 공통코드상세 where 공통코드 = '"+userCode+"' and 코드명 = '"+school+"');\r\n";
			state.executeQuery(sql);
			
			sql="insert into 학생 values ('"+id+"', @tempCode, null, null);\r\n"; 
			state.executeUpdate(sql);
			sql="INSERT INTO 사용자접근권한 (id, 사용자_상태코드) VALUES ('"+id+"', '"+userCode+"');";
			state.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public String duplicateCheckId(String id) throws SQLException {
		
		state = conn.createStatement();
		String sql = "SELECT id FROM 사용자 where id='"+id+"';";
		res = state.executeQuery(sql);
		
		String result = "";
		if(res.next())result += res.getString(1);
		
		return result;
	}
	
	public String getStudentstate(String id) {
		try {
			state = conn.createStatement();

			String sql = "select 사용자_상태코드 from 사용자접근권한 where 사용자접근권한.id = '"+id+"';";
			res = state.executeQuery(sql);
			
			String result = "";
			if(res.next()) result += res.getString(1) + "#";
			
			sql = "SELECT * FROM 대여자 where 대여자id = '"+id+"';"; // 대여자 인지 아닌지
			res = state.executeQuery(sql);
			
			if(res.next()) { result += "1#"; }
			else {result += "0#";}
			
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getManagerstate(String id) {
		try {
			String result = "";
			state = conn.createStatement();

			String sql = "select 사용자_상태코드 from 사용자접근권한 where 사용자접근권한.id = '"+id+"';";
			res = state.executeQuery(sql);
			
			if(res.next()) { 
				result += res.getInt(1); 
				}
			
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getHideMail(String school) {
		try {
			state = conn.createStatement();

			String sql = "CREATE TEMPORARY TABLE IF NOT EXISTS hideEmail \r\n" + 
					"select \r\n" + 
					"case when (대학교.홈페이지 like 'http://www.%') then SUBSTRING_INDEX(replace(대학교.홈페이지, 'http://www.', '') , '/', 1)\r\n" + 
					"when (대학교.홈페이지 like 'https://www.%') then SUBSTRING_INDEX(replace(대학교.홈페이지, 'https://www.', '') , '/', 1)\r\n" + 
					"when (대학교.홈페이지 like 'https://%') then SUBSTRING_INDEX(SUBSTRING_INDEX(replace (대학교.홈페이지, 'https://',''), ':', 1) , '/', 1)\r\n" + 
					"when (대학교.홈페이지 like 'http://%') then SUBSTRING_INDEX(replace (대학교.홈페이지, 'http://', '') , '/', 1)\r\n" + 
					"when (대학교.홈페이지 like 'www.%') then SUBSTRING_INDEX(replace (대학교.홈페이지, 'www.', '') , '/', 1)\r\n" + 
					"else SUBSTRING_INDEX(대학교.홈페이지, '/', 1) end as 이메일뒷자리,\r\n" + 
					"공통코드상세.코드명\r\n" + 
					"from 대학교, 공통코드상세 \r\n" + 
					"where 공통코드상세.공통코드 = '001' and 대학교.학교코드 = 공통코드상세.코드;\r\n" + 
					"\r\n";
					
			
			state.executeUpdate(sql);
			
			sql = "select 이메일뒷자리 from hideEmail where 코드명='"+school+"'";
			res = state.executeQuery(sql);
			
			String result = "";
			
			if(res.next())result += res.getString(1);
			
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getResultSet() {
		return res;
	}

	public void updateUserdata(String id, String pw, String phoneNum) {
		try {
			state = conn.createStatement();
			
			if(!pw.equals("")) {
				String sql = "UPDATE 사용자 SET password = '"+pw+"' WHERE (사용자.id = '"+id+"');"; 
				state.executeUpdate(sql);
			}
			if(!phoneNum.equals("")){
				String sql = "UPDATE 학생 SET 휴대전화번호 = '"+phoneNum+"' WHERE (학생.학생id = '"+id+"');";
				state.executeUpdate(sql);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPCList(String school, String first, String second) { // 대학코드 얻고 나서..
		try {
			String strRes =""; //Query
			
			state = conn.createStatement();
			//Before start of result set
			String sql = "SELECT 코드 FROM 공통코드상세 where 코드명='"+school+"' and 공통코드=001;";
			
			res = state.executeQuery(sql);

			String schoolCode = null;
			
			while(res.next()) {
				schoolCode = res.getString(1);
			}
			
			if(first.equals("선택안함")){ // first.equals가 선택안함이면 second 선택 X
				if(second.equals("선택안함")){ // default
					sql = "select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.pc_상태코드, pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, cpu.성능순위, count(*) as 잔여개수 \r\n" + 
							"from pc정보, pc제조사, cpu \r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드  \r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블, \r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,"+ 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU, \r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 "+
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							";";
				}
				else if(second.equals("총 성능 순")){
					sql = "select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.pc_상태코드, pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, (cpu.성능순위 + gpu.성능순위 + rank() over (order by pc정보.`RAM(GB)` desc)) as 순위, count(*) as 잔여개수\r\n" + 
							"from pc정보, pc제조사, cpu, gpu\r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드 and pc정보.GPU_모델코드 = gpu.모델코드 \r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 \r\n" + 
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							"order by 추출테이블.순위;";
				}
				else if(second.equals("CPU성능 순")) {
					sql ="select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.pc_상태코드, pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, cpu.성능순위, count(*) as 잔여개수\r\n" + 
							"from pc정보, pc제조사, cpu\r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드\r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,"+ 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 "+ 
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							"order by 추출테이블.성능순위;";
				}
				else {
					sql ="select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, gpu.성능순위, count(*) as 잔여개수, pc정보.pc_상태코드\r\n" + 
							"from pc정보, pc제조사, gpu\r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.GPU_모델코드 = gpu.모델코드 \r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,"+ 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 "+ 
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							"order by 추출테이블.성능순위;";
				}
			}
			else { // 대여 가능
				if(second.equals("선택안함")){
					sql = "select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.pc_상태코드, pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, cpu.성능순위, count(*) as 잔여개수 \r\n" + 
							"from pc정보, pc제조사, cpu \r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드  and pc정보.pc_상태코드 = '001'\r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블, \r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,"+ 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU, \r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 "+
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							";";
				}
				else if(second.equals("총 성능 순")) {
					sql = "select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.pc_상태코드, pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, (cpu.성능순위 + gpu.성능순위 + rank() over (order by pc정보.`RAM(GB)` desc)) as 순위, count(*) as 잔여개수\r\n" + 
							"from pc정보, pc제조사, cpu, gpu\r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드 and pc정보.GPU_모델코드 = gpu.모델코드 and pc정보.pc_상태코드 = '001'\r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 \r\n" + 
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							"order by 추출테이블.순위;";
				}
				else if(second.equals("CPU성능 순")){
					sql = "select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, cpu.성능순위, count(*) as 잔여개수, pc정보.pc_상태코드 \r\n" + 
							"from pc정보, pc제조사, cpu \r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드 and pc정보.pc_상태코드 = '001'\r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블, \r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,"+
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU, \r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 "+ 
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							"order by 추출테이블.성능순위;";
					
				}
				else {
					sql = "select pc상태.코드명 as pc상태, 회사명.코드명 as 제조사, pc모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)`, 추출테이블.잔여개수\r\n" + 
							"from (select pc정보.대학코드, pc제조사.회사코드, pc정보.ip주소, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, gpu.성능순위, count(*) as 잔여개수, pc정보.pc_상태코드\r\n" + 
							"from pc정보, pc제조사, gpu\r\n" + 
							"where pc제조사.모델코드 = pc정보.pc_모델코드 and pc정보.GPU_모델코드 = gpu.모델코드 and pc정보.pc_상태코드 = '001' \r\n" + 
							"group by pc제조사.회사코드, pc_모델코드, cpu_모델코드, gpu_모델코드, `RAM(GB)`) as 추출테이블,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,"+
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
							"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as pc상태 "+ 
							"where 추출테이블.대학코드 = 대학교.코드 and 대학교.코드명 = '"+school+"' and 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.pc_상태코드 = pc상태.코드\r\n" + 
							"order by 추출테이블.성능순위;";
				}
			}
			res = state.executeQuery(sql);
			
			int row = 0;
			while(res.next()) {
				strRes += res.getString(1) + "#"; // 상태
				strRes += res.getString(2) + "#"; // 제조사
				strRes += res.getString(3) + "#"; // 모델
				strRes += res.getString(4) + "#"; // cpu
				strRes += res.getString(5) + "#"; // gpu
				strRes += res.getString(6) + "#"; // ram
				strRes += res.getString(7) + "#"; // 잔여개수
				row ++;
			}
			strRes = Integer.toString(row) + "#" + strRes;
			return strRes;
			
		}
		 catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}
	
	public void enrollPCBorrower(String id, String man, String model, String cpu, String gpu,
			String ram, String returnD, String alarm) throws SQLException {
		
		state = conn.createStatement();
		String sql ="CREATE TEMPORARY TABLE IF NOT EXISTS temp (\r\n" + 
				"  `IP주소` VARCHAR(25) NOT NULL,\r\n" + 
				"  `제조사` VARCHAR(20) NOT NULL,\r\n" + 
				"  `PC_모델` VARCHAR(50) NOT NULL,\r\n" + 
				"  `CPU_모델` VARCHAR(50) NOT NULL,\r\n" + 
				"  `GPU_모델` VARCHAR(50) NOT NULL,\r\n" + 
				"  `RAM(GB)` INT NOT NULL,\r\n" + 
				"  `PC_id` VARCHAR(30) NOT NULL,\r\n" + 
				"  `password` VARCHAR(20) NOT NULL,\r\n" + 
				"  PRIMARY KEY (`IP주소`));";
		state.executeUpdate(sql);
		
		sql = "insert into temp (select pc정보.IP주소, 회사명.코드명 as 제조사, PC모델.코드명 as PC, CPU.코드명 as CPU, GPU.코드명 as GPU, PC정보.`RAM(GB)`, pc정보.PC_id, pc정보.password\r\n" + 
				"from pc제조사, pc정보, cpu, gpu,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU\r\n" + 
				"where 회사명.코드명 = '"+man+"' and PC모델.코드명 = '"+model+"' and CPU.코드명 = '"+cpu+"' and GPU.코드명 = '"+gpu+"' and pc정보.`RAM(GB)` = "+ram+" and\r\n" + 
				"pc제조사.모델코드 = pc정보.PC_모델코드 and pc정보.CPU_모델코드 = cpu.모델코드 and pc정보.GPU_모델코드 = gpu.모델코드 and pc제조사.회사코드 = 회사명.코드\r\n" + 
				"and pc정보.PC_모델코드 = PC모델.코드 and cpu.모델코드 = CPU.코드 and gpu.모델코드 = GPU.코드 and pc정보.PC_상태코드 = '001');";
		state.executeUpdate(sql);
		
		sql = "set @ip:= (select temp.IP주소\r\n" + 
				"from  temp, \r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU \r\n" + 
				"where  temp.제조사 = 회사명.코드명 and  temp.PC_모델 = PC모델.코드명 and  temp.CPU_모델 = CPU.코드명 and  temp.GPU_모델 = gpu.코드명 \r\n" + 
				"group by  temp.IP주소\r\n" + 
				"limit 1);";
		state.executeQuery(sql);
		
		sql = "UPDATE pc정보 SET PC_상태코드 = '002' WHERE (`IP주소` = @ip);";
		state.executeUpdate(sql);
		
		sql = "set @ip:= (select temp.IP주소\r\n" + 
				"from temp, \r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU \r\n" + 
				"where  temp.제조사 = 회사명.코드명 and  temp.PC_모델 = PC모델.코드명 and  temp.CPU_모델 = CPU.코드명 and  temp.GPU_모델 = gpu.코드명 \r\n" + 
				"group by  temp.IP주소\r\n" + 
				"limit 1);";
		state.executeQuery(sql);
		
		sql = "INSERT INTO `servermanagement`.`대여자` (`대여자id`, `IP주소`, `대여일`, `반납예정일`, `연장여부`, `만료알림설정여부`) VALUES ('"+id+"', @ip, curdate(), '"+returnD+"', '1', '"+alarm+"');" ;
		state.executeUpdate(sql);
	}
	
	public String pcinform(String id) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "select pc모델.코드명 as PC명, 회사명.코드명 as 제조사, 대여자.IP주소, pc정보.PC_id, pc정보.password, CPU.코드명 as CPU, GPU.코드명 as GPU, pc정보.`RAM(GB)`, if(대여자.연장여부, 'O', 'X') as 연장신청가능여부, 대여자.대여일, 대여자.반납예정일 \r\n" + 
				"from 대여자, pc정보, pc제조사,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
				"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU \r\n" + 
				"where 대여자.대여자id = '"+id+"' and 대여자.IP주소 = pc정보.IP주소 and pc정보.PC_모델코드 = pc제조사.모델코드 and pc제조사.회사코드 = 회사명.코드 and pc정보.PC_모델코드 = PC모델.코드 and pc정보.CPU_모델코드 = CPU.코드 and pc정보.GPU_모델코드 = GPU.코드;";
	
		res = state.executeQuery(sql);
		
		String data = "";
		
		while(res.next()) {
			data += res.getString(1) + "#"; 
			data += res.getString(2) + "#"; 
			data += res.getString(3) + "#"; 
			data += res.getString(4) + "#"; 
			data += res.getString(5) + "#"; 
			data += res.getString(6) + "#"; 
			data += res.getString(7) + "#"; 
			data += res.getString(8) + "#"; 
			data += res.getString(9) + "#"; 
			data += res.getString(10) + "#"; 
			data += res.getString(11) + "#"; 
		}
		
		return data;
	}
	
	public String getPcreturnDate(String id) throws SQLException {
		state = conn.createStatement();
		
		String sql = "SELECT 반납예정일 FROM servermanagement.대여자 where 대여자id ='"+id+"'";
		
		res = state.executeQuery(sql);
		
		while(res.next()) {
			data = res.getString(1);
		}
		
		return data;
	}
	
	public void updateExtendDate(String id, String d) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "update 대여자 set 반납예정일 = '"+d+"' , 연장여부 = \"0\" where 대여자id ='"+id+"' ;";
		
		state.executeUpdate(sql);
		
	}

	public String isBorrower(String id) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "SELECT * FROM servermanagement.대여자 where 대여자id = '"+id+"';";
		
		res = state.executeQuery(sql);
		
		String data = "";
		data += "0";
		
		while(res.next()) {
			data = "1";
		}
		return data;
	}

	public void updateCertificateStudent(String stuId, String stuMail) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "UPDATE `servermanagement`.`학생` SET `이메일` = '"+stuMail+"' WHERE (`학생id` = '"+stuId+"');";
		state.executeUpdate(sql);
		
		sql = "Update 사용자접근권한 set 사용자_상태코드 = 002 where id = '"+stuId+"'";
		state.executeUpdate(sql);
	}

	public String requestUserList() {
		try {
			state = conn.createStatement();
			
			String sql = "select 명단.id, 명단.password, 명단.성명, 대학교.코드명 as 대학교, 회원분류.코드명 as 회원분류, 명단.닉네임, 명단.휴대전화번호, 명단.이메일, 명단.구분코드\r\n" + 
					"from\r\n" + 
					"(select * from 사용자, 학생 where 사용자.id = 학생.학생id\r\n" + 
					"union\r\n" + 
					"select * from 사용자, pc_관리직원 where 사용자.id = pc_관리직원.PC_관리직원id) as 명단, 사용자접근권한,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '008') as 회원분류\r\n" + 
					"where 명단.학교코드 = 대학교.코드 and 명단.id = 사용자접근권한.id and 회원분류.코드 = 사용자접근권한.사용자_상태코드;\r\n" + 
					"\r\n" + 
					"";
			
			res = state.executeQuery(sql);
			
			String data = "";
			
			while(res.next()) {
				data += res.getString(1) + "#";  // id
				data += res.getString(2) + "#"; // 비번
				data += res.getString(3) + "#";  // 이름
				data += res.getString(4) + "#";	 // 대학교
				data += res.getString(5) + "#";
				data += res.getString(6) + "#";  //
				data += res.getString(7) + "#"; 
				data += res.getString(8) + "#"; 
				data += res.getInt(9) + "#"; 
			}
			
			return data;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
	} // 운영자 : 학생, 관리자 리스트 조회
	
	public String requestStudentList() {
		try {
			state = conn.createStatement();
			
			String sql = "select 명단.id, 명단.password, 명단.성명, 대학교.코드명 as 대학교, 회원분류.코드명 as 회원분류, 명단.닉네임, 명단.휴대전화번호, 명단.이메일, 명단.구분코드\r\n" + 
					"from\r\n" + 
					"(select * from 사용자, 학생 where 사용자.id = 학생.학생id) as 명단, 사용자접근권한,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '008') as 회원분류\r\n" + 
					"where 명단.학교코드 = 대학교.코드 and 명단.id = 사용자접근권한.id and 회원분류.코드 = 사용자접근권한.사용자_상태코드;";
			
			res = state.executeQuery(sql);
			
			String data = "";
			while(res.next()) {
				data += res.getString(1) + "#";  // id
				data += res.getString(2) + "#"; // 비번
				data += res.getString(3) + "#";  // 이름
				data += res.getString(4) + "#";	 // 대학교
				data += res.getString(5) + "#";
				data += res.getString(6) + "#";  //
				data += res.getString(7) + "#"; 
				data += res.getString(8) + "#"; 
				data += res.getInt(9) + "#"; 
			}
			
			return data;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
	} //운영자 : 학생 리스트 조회
	
	public ResultSet requestOurSchoolStudentList(String school) {
		try {
			state = conn.createStatement();
			String sql = "select 학생.학생id, 사용자.닉네임, 사용자.성명, 학생.휴대전화번호, 학생.이메일 \r\n" + 
					"from 사용자, 학생, (select * from 공통코드상세 where 공통코드 = '001') as 대학교\r\n" + 
					"where 학생.학교코드 = 대학교.코드 and 대학교.코드명 = '"+ school +"' and 사용자.id = 학생.학생id;";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public String requestManagerList() {
		try {
			state = conn.createStatement();
			String sql = "select 명단.id, 명단.password, 명단.성명, 대학교.코드명 as 대학교, 회원분류.코드명 as 회원분류, 명단.닉네임, 명단.전화번호, 명단.이메일, 명단.구분코드\r\n" + 
					"from\r\n" + 
					"(select * from 사용자, pc_관리직원 where 사용자.id = pc_관리직원.PC_관리직원id) as 명단, 사용자접근권한,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '008') as 회원분류\r\n" + 
					"where 명단.학교코드 = 대학교.코드 and 명단.id = 사용자접근권한.id and 회원분류.코드 = 사용자접근권한.사용자_상태코드;";
			
			res = state.executeQuery(sql);
			
			String data = "";
			while(res.next()) {
				data += res.getString(1) + "#";  // id
				data += res.getString(2) + "#"; // 비번
				data += res.getString(3) + "#";  // 이름
				data += res.getString(4) + "#";	 // 대학교
				data += res.getString(5) + "#";
				data += res.getString(6) + "#";  //
				data += res.getString(7) + "#"; 
				data += res.getString(8) + "#"; 
				data += res.getInt(9) + "#"; 
			}
			return data;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
	} // 운영자 : 직원 리스트 조회
	
	public void dropUser(String id) {
		try {
			state = conn.createStatement();
			String sql = "UPDATE `servermanagement`.`사용자접근권한` SET `사용자_상태코드` = '003' WHERE (`id` = '"+ id +"');";
			int temp = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // 운영자 : 학생, 관리직원 영구 정지

	public void certificateUser(String id) {
		try {
			state = conn.createStatement();
			String sql = "UPDATE `servermanagement`.`사용자접근권한` SET `사용자_상태코드` = '002' WHERE (`id` = '"+ id +"');";
			state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // 운영자 : 인증
	
	public String requestNotice() {
		try {
			String data = "";
			state = conn.createStatement();
			String sql = "select * from 공지사항;";
			res = state.executeQuery(sql);
			
			while(res.next()) {
				data += res.getInt(1) + "#";
				data += res.getString(4) + "#";
				data += res.getString(2) + "#";
				data += res.getString(3) + "#";
				data += res.getInt(6) + "#";
				data += res.getString(5) + "#";
			}
			return data;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} // 공지사항 리스트 띄우기
	
	public String selectDocument(int no) {
		try {
			String data = "";
			state = conn.createStatement();
			String sql = "UPDATE 공지사항 SET 공지사항.조회수 = 공지사항.조회수 + 1 WHERE (공지사항.번호 = '"+ no + "');";
			state.executeUpdate(sql);
			sql = "select * from 공지사항 where 공지사항.번호 = "+ no +";";
			res = state.executeQuery(sql);
			
			while(res.next()) {
				data += res.getInt(1) + "#";
				data += res.getString(4) + "#";
				data += res.getString(2) + "#";
				data += res.getString(3) + "#";
				data += res.getInt(6) + "#";
				data += res.getString(5) + "#";
			}
			return data;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} // 공지사항 선택
	
	
	public void upLoadDocument(String op_id, String title, String content) { //네트워크 때 파라미터 조정
		try {
			state = conn.createStatement();
			
			String sql = "INSERT INTO 공지사항 (번호, 작성자id,  게시일 ,  제목 ,  내용 ,  조회수 ) VALUES ('0', '"+ op_id +"', now(), '"+ title +"', '"+ content +"', '0');";
			state.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // 운영자 : 게시물 올리기
	
	
	public void deleteDocument(int no) {
		try {
			state = conn.createStatement();
			String sql = "DELETE FROM 공지사항 WHERE (번호 = "+ no +");";
			state.executeUpdate(sql);
			sql = "select @cnt:=0;";
			state.executeQuery(sql);
			sql = "update 공지사항 set 공지사항.번호 = @cnt:=@cnt+1;";
			state.executeUpdate(sql);
			sql = "select @cnt;";
			res = state.executeQuery(sql);
			while(res.next()) {
				int startPoint = startPoint = res.getInt(1);
				sql = "alter table 공지사항 auto_increment=" + startPoint;
			}
			state.executeUpdate(sql);					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // 운영자 : 게시물 삭제하기
	
	public String getSchoolList() {
		try {
			state = conn.createStatement();
			String sql = "select 대학.코드명 as 학교명, 대학교.주소, 대학교.전화번호 \r\n" + 
					"from 대학교, \r\n" + 
					"(select * from 공통코드상세 where 공통코드 = '001') as 대학\r\n" + 
					"where 대학교.학교코드 = 대학.코드;";
			res = state.executeQuery(sql);
			
			String rs = "";
			
			while(res.next()) {
				rs += res.getString(1) + "#";
				rs += res.getString(2) + "#";
				rs += res.getString(3) + "#";
			}
		
			return rs;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} // 운영자 : 대학교 리스트 불러오기
	
	public String getSchool(String str) {
		try {
			state = conn.createStatement();
			String sql = "select 대학.코드명 as 학교명, 대학교.주소, 대학교.전화번호 \r\n" + 
					"from 대학교, \r\n" + 
					"(select * from 공통코드상세 where 공통코드 = '001') as 대학\r\n" + 
					"where 대학교.학교코드 = 대학.코드 and 대학.코드명 like '"+ str +"%';";
			res = state.executeQuery(sql);
			
			String rs = "";
			int cnt = 0 ;
			
			while(res.next()) {
				rs += res.getString(1) + "#";
				rs += res.getString(2) + "#";
				rs += res.getString(3) + "#";
				cnt ++;
			}
			
			return cnt+"#"+rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} // 운영자 : 학교 검색
	
	public void returnPC(String ip) {
		
		try {
			state = conn.createStatement();

			String sql ;
					//"set @ip:= (select 대여자.IP주소 from 대여자, pc정보 where 대여자.반납예정일 = curdate() and 대여자.IP주소 = pc정보.ip주소);";
			
			sql = "delete from 대여자 where 대여자.IP주소 = '"+ip+"';";
		    state.execute(sql);
		    
		    sql = "delete from temp where temp.IP주소 = '"+ip+"';";
		    state.execute(sql);
		    
			sql ="update pc정보 set pc정보.pc_상태코드= '001' where pc정보.IP주소 = '"+ip+"';";
			state.executeUpdate(sql);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("반납 거절");
		//e.printStackTrace();
		}
	}
	
	public ResultSet requestLenderList(String school) {
		try {
			state = conn.createStatement();
			String sql = "select 대여자.대여자id, 사용자.성명, 대여자.IP주소, pc모델.코드명 as PC, pc정보.pc_id, pc정보.password\r\n" + 
					"from 대여자, 사용자, pc정보, 학생, (select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as pc모델, (select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학교\r\n" + 
					"where 대여자.대여자id = 사용자.id and 대여자.대여자id = 학생.학생id and 대학교.코드명 = '"+ school +"' and 학생.학교코드 = 대학교.코드 and 대여자.IP주소 = pc정보.IP주소 and pc정보.PC_모델코드 = pc모델.코드;";
			res = state.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public ResultSet requestMessagingTarget(String school) {
		try {
			state = conn.createStatement();
			String sql = "select 대여자상세.휴대전화번호\r\n" + 
					"from (select * \r\n" + 
					"from 대여자, 학생, (select * from 공통코드상세 where 공통코드 = '001') as 대학교\r\n" + 
					"where 대여자.대여자id = 학생.학생id and 대학교.코드명 = '"+ school +"' and 대학교.코드 = 학생.학교코드) as 대여자상세\r\n" + 
					"where 대여자상세.반납예정일 = curdate() + 7 and 대여자상세.만료알림설정여부 = 1 and 대여자상세.휴대전화번호 is not null;";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return res;
	} // 메시지 보낼 사람 몇 명인지
	
	public ResultSet showPCList(String school) {
		try {
			state = conn.createStatement();
			String sql = "select 추출테이블.IP주소, 회사명.코드명 as 제조사, PC모델.코드명 as 모델명, CPU.코드명 as CPU, GPU.코드명 as GPU, 추출테이블.`RAM(GB)` as `RAM(GB)`, 추출테이블.PC_id, 추출테이블.password, PC_상태.코드명 as 상태\r\n" + 
					"from (select pc정보.IP주소, pc제조사.회사코드, pc정보.pc_모델코드, pc정보.cpu_모델코드, pc정보.gpu_모델코드, pc정보.`RAM(GB)`, pc정보.PC_id, pc정보.password, pc정보.pc_상태코드\r\n" + 
					"from pc정보, pc제조사, (select * from 공통코드상세 where 공통코드상세.공통코드 = '001') as 대학 \r\n" + 
					"where pc정보.pc_모델코드 = pc제조사.모델코드 and 대학.코드 = pc정보.대학코드 and 대학.코드명 = '"+ school +"') as 추출테이블,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '005') as 회사명,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '004') as PC모델,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '002') as CPU,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '003') as GPU,\r\n" + 
					"(select * from 공통코드상세 where 공통코드상세.공통코드 = '006') as PC_상태\r\n" + 
					"where 추출테이블.회사코드 = 회사명.코드 and 추출테이블.pc_모델코드 = PC모델.코드 and 추출테이블.cpu_모델코드 = CPU.코드 and 추출테이블.gpu_모델코드 = GPU.코드 and 추출테이블.PC_상태코드 = PC_상태.코드\r\n" + 
					"order by 추출테이블.`RAM(GB)` desc;";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return res;
	} // 관리직원 pc 리스트
	
	public ResultSet getCPU() {
		try {
			state = conn.createStatement();
			String sql = "select 코드명 from 공통코드상세 where 공통코드 = '002';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} //CPU 콤보박스
	
	public ResultSet getGPU() {
		try {
			state = conn.createStatement();
			String sql = "SELECT 코드명 FROM 공통코드상세  where 공통코드 = '003';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} //GPU 콤보박스
	
	public ResultSet getPC(String company) {
		try {
			state = conn.createStatement();
			String sql = "SELECT * FROM \r\n" + 
					"pc제조사, (SELECT * FROM 공통코드상세 where 공통코드 = '004') as PC, (SELECT * FROM 공통코드상세 where 공통코드 = '005') as 회사\r\n" + 
					"where pc제조사.모델코드 = PC.코드 and pc제조사.회사코드 = 회사.코드 and 회사.코드명 = '"+ company +"';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} // PC 콤보박스
	
	public ResultSet getCompany() {
		try {
			state = conn.createStatement();
			String sql = "SELECT 코드명 FROM 공통코드상세 where 공통코드 = '005';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} //제조사 콤보박스
	
	public ResultSet getState() {
		try {
			state = conn.createStatement();
			String sql = "SELECT 코드명 FROM 공통코드상세 where 공통코드 = '006';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} // 상태정보 콤보박스
	
	
	
	public int registerPC(String ip, String pc, String cpu, String gpu, int ram, String id, String password, String school, String pcState) {
		int rs = -1;
		try {
			state = conn.createStatement();
			String sql = "INSERT INTO PC정보 (IP주소, PC_모델코드, CPU_모델코드, GPU_모델코드, `RAM(GB)`, PC_id, password, 대학코드, PC_상태코드) \r\n" + 
					"VALUES ('"+ ip + "', (select 코드 from 공통코드상세 where 공통코드 = '004' and 코드명 = '"+ pc +"'), "
					+ "(select 코드 from 공통코드상세 where 공통코드 = '002' and 코드명 = '"+ cpu +"'), "
					+ "(select 코드 from 공통코드상세 where 공통코드 = '003' and 코드명 = '"+ gpu +"'), '"+ram+"', "
					+ "'"+ id +"', '" + password + "', "
					+ "(select 코드 from 공통코드상세 where 공통코드 = '001' and 코드명 = '"+ school +"'), "
					+ "(select 코드 from 공통코드상세 where 공통코드 = '006'and 코드명 = '"+ pcState +"'));";
			rs = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	} // pc 등록
	
	public int modifyPC(String standard, String newIp, String pc, String cpu, String gpu, int ram, String id, String password, String school, String pcState) {
		int rowCnt = 0;
		try {
			state = conn.createStatement();
			String sql = "UPDATE `pc정보` \r\n" + 
					"SET `IP주소` = '"+ newIp +"', `PC_모델코드` = (select 코드 from 공통코드상세 where 공통코드 = '004' and 코드명 = '"+ pc +"'), \r\n" + 
					"`CPU_모델코드` = (select 코드 from 공통코드상세 where 공통코드 = '002' and 코드명 = '"+ cpu +"'), \r\n" + 
					"`GPU_모델코드` = (select 코드 from 공통코드상세 where 공통코드 = '003' and 코드명 = '"+ gpu +"'), \r\n" + 
					"`RAM(GB)` = '"+ ram +"', \r\n" + 
					"`PC_id` = '"+ id +"', \r\n" + 
					"`password` = '"+ password +"', \r\n" + 
					"`대학코드` = (select 코드 from 공통코드상세 where 공통코드 = '001' and 코드명 = '"+ school +"'),\r\n" + 
					"`PC_상태코드` = (select 코드 from 공통코드상세 where 공통코드 = '006'and 코드명 = '"+ pcState +"')\r\n" + 
					"WHERE (`IP주소` = '"+ standard +"');";
			rowCnt = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCnt;
	}
	
	public int deletePC(String ip) {
		int rowCnt = 0;
		try {
			state = conn.createStatement();
			String sql = "DELETE FROM pc정보 WHERE (`IP주소` = '"+ ip +"');";
			rowCnt = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCnt;
	}
}

