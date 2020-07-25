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
			String sql = "select �뿩��.�뿩��id, �����.����, �뿩��.IP�ּ�, pc��.�ڵ�� as PC, pc����.pc_id, pc����.password\r\n"
					+ "from �뿩��, �����, pc����, (select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as pc�� \r\n"
					+ "where �뿩��.�뿩��id = �����.id and �뿩��.IP�ּ� = pc����.IP�ּ� and pc����.PC_���ڵ� = pc��.�ڵ�;";
			res = state.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execute2() {
		try {
			state = conn.createStatement();
			String sql = "select �л�.�л�id, �����.�г���, �����.����, �л�.�޴���ȭ��ȣ, �л�.�̸��� \r\n" + "from �����, �л�\r\n"
					+ "where �л�.�б��ڵ� = '87' and �����.id = �л�.�л�id;"; // �� 87�� �����;� �Ѵ�...
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // �ӽ÷� execute1, execute2 �̷� ������ �س����� �����δ� �� �Լ��� ���ļ� switch������ ���� ������???

	public String doLogin(String id, String pw) {
		try {
			state = conn.createStatement();
			String result = "";
			
			String sql = "select ȸ��.id, ȸ��.password, ȸ��.����, ȸ��.�г���, ȸ��.�����ڵ�, ȸ��.�޴���ȭ��ȣ as ��ȭ��ȣ, ȸ��.�̸���, �����ڵ��.�ڵ�� as �б���\r\n" + 
					"from\r\n" + 
					"(select * from �����, �л� where �����.id = �л�.�л�id\r\n" + 
					"union \r\n" + 
					"select * from �����, pc_�������� where �����.id = pc_��������.pc_��������id\r\n" + 
					"union\r\n" + 
					"select �����.id, �����.password, �����.����, �����.�г���, �����.�����ڵ�, ���.���id, null, ���.��ȭ��ȣ, ���.�̸��� from �����, ��� where �����.id = ���.���id) as ȸ�� left join �����ڵ�� on \r\n" + 
					"�����ڵ��.�����ڵ� = 001 and �����ڵ��.�ڵ� = ȸ��.�б��ڵ� \r\n" + 
					"where ȸ��.id = '"+ id +"' and ȸ��.password = '"+ pw +"';";
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
				sql = "select �����_�����ڵ� from ��������ٱ��� where id='"+id+"';";
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
					"select ����.�ڵ�� as �б��� \r\n" + 
					"from ���б�, \r\n" + 
					"(select * from �����ڵ�� where �����ڵ� = '001') as ����\r\n" + 
					"where ���б�.�б��ڵ� = ����.�ڵ� and ����.�ڵ�� like '"+word+"%';";
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
			
			String sql = "insert into ����� values ('"+id+"', '"+pw+"', '"+name+"', '"+nickname+"', '"+userCode+"');\r\n"; 
			state.executeUpdate(sql);
			sql = "set @tempCode = (select �����ڵ��.�ڵ� from �����ڵ�� where �����ڵ� = '"+userCode+"' and �ڵ�� = '"+school+"');\r\n";
			state.executeQuery(sql);
			
			sql="insert into �л� values ('"+id+"', @tempCode, null, null);\r\n"; 
			state.executeUpdate(sql);
			sql="INSERT INTO ��������ٱ��� (id, �����_�����ڵ�) VALUES ('"+id+"', '"+userCode+"');";
			state.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public String duplicateCheckId(String id) throws SQLException {
		
		state = conn.createStatement();
		String sql = "SELECT id FROM ����� where id='"+id+"';";
		res = state.executeQuery(sql);
		
		String result = "";
		if(res.next())result += res.getString(1);
		
		return result;
	}
	
	public String getStudentstate(String id) {
		try {
			state = conn.createStatement();

			String sql = "select �����_�����ڵ� from ��������ٱ��� where ��������ٱ���.id = '"+id+"';";
			res = state.executeQuery(sql);
			
			String result = "";
			if(res.next()) result += res.getString(1) + "#";
			
			sql = "SELECT * FROM �뿩�� where �뿩��id = '"+id+"';"; // �뿩�� ���� �ƴ���
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

			String sql = "select �����_�����ڵ� from ��������ٱ��� where ��������ٱ���.id = '"+id+"';";
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
					"case when (���б�.Ȩ������ like 'http://www.%') then SUBSTRING_INDEX(replace(���б�.Ȩ������, 'http://www.', '') , '/', 1)\r\n" + 
					"when (���б�.Ȩ������ like 'https://www.%') then SUBSTRING_INDEX(replace(���б�.Ȩ������, 'https://www.', '') , '/', 1)\r\n" + 
					"when (���б�.Ȩ������ like 'https://%') then SUBSTRING_INDEX(SUBSTRING_INDEX(replace (���б�.Ȩ������, 'https://',''), ':', 1) , '/', 1)\r\n" + 
					"when (���б�.Ȩ������ like 'http://%') then SUBSTRING_INDEX(replace (���б�.Ȩ������, 'http://', '') , '/', 1)\r\n" + 
					"when (���б�.Ȩ������ like 'www.%') then SUBSTRING_INDEX(replace (���б�.Ȩ������, 'www.', '') , '/', 1)\r\n" + 
					"else SUBSTRING_INDEX(���б�.Ȩ������, '/', 1) end as �̸��ϵ��ڸ�,\r\n" + 
					"�����ڵ��.�ڵ��\r\n" + 
					"from ���б�, �����ڵ�� \r\n" + 
					"where �����ڵ��.�����ڵ� = '001' and ���б�.�б��ڵ� = �����ڵ��.�ڵ�;\r\n" + 
					"\r\n";
					
			
			state.executeUpdate(sql);
			
			sql = "select �̸��ϵ��ڸ� from hideEmail where �ڵ��='"+school+"'";
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
				String sql = "UPDATE ����� SET password = '"+pw+"' WHERE (�����.id = '"+id+"');"; 
				state.executeUpdate(sql);
			}
			if(!phoneNum.equals("")){
				String sql = "UPDATE �л� SET �޴���ȭ��ȣ = '"+phoneNum+"' WHERE (�л�.�л�id = '"+id+"');";
				state.executeUpdate(sql);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPCList(String school, String first, String second) { // �����ڵ� ��� ����..
		try {
			String strRes =""; //Query
			
			state = conn.createStatement();
			//Before start of result set
			String sql = "SELECT �ڵ� FROM �����ڵ�� where �ڵ��='"+school+"' and �����ڵ�=001;";
			
			res = state.executeQuery(sql);

			String schoolCode = null;
			
			while(res.next()) {
				schoolCode = res.getString(1);
			}
			
			if(first.equals("���þ���")){ // first.equals�� ���þ����̸� second ���� X
				if(second.equals("���þ���")){ // default
					sql = "select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.pc_�����ڵ�, pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, cpu.���ɼ���, count(*) as �ܿ����� \r\n" + 
							"from pc����, pc������, cpu \r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ�  \r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�, \r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,"+ 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU, \r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� "+
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							";";
				}
				else if(second.equals("�� ���� ��")){
					sql = "select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.pc_�����ڵ�, pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, (cpu.���ɼ��� + gpu.���ɼ��� + rank() over (order by pc����.`RAM(GB)` desc)) as ����, count(*) as �ܿ�����\r\n" + 
							"from pc����, pc������, cpu, gpu\r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ� and pc����.GPU_���ڵ� = gpu.���ڵ� \r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� \r\n" + 
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							"order by �������̺�.����;";
				}
				else if(second.equals("CPU���� ��")) {
					sql ="select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.pc_�����ڵ�, pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, cpu.���ɼ���, count(*) as �ܿ�����\r\n" + 
							"from pc����, pc������, cpu\r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ�\r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,"+ 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� "+ 
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							"order by �������̺�.���ɼ���;";
				}
				else {
					sql ="select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, gpu.���ɼ���, count(*) as �ܿ�����, pc����.pc_�����ڵ�\r\n" + 
							"from pc����, pc������, gpu\r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.GPU_���ڵ� = gpu.���ڵ� \r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,"+ 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� "+ 
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							"order by �������̺�.���ɼ���;";
				}
			}
			else { // �뿩 ����
				if(second.equals("���þ���")){
					sql = "select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.pc_�����ڵ�, pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, cpu.���ɼ���, count(*) as �ܿ����� \r\n" + 
							"from pc����, pc������, cpu \r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ�  and pc����.pc_�����ڵ� = '001'\r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�, \r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,"+ 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU, \r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� "+
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							";";
				}
				else if(second.equals("�� ���� ��")) {
					sql = "select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.pc_�����ڵ�, pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, (cpu.���ɼ��� + gpu.���ɼ��� + rank() over (order by pc����.`RAM(GB)` desc)) as ����, count(*) as �ܿ�����\r\n" + 
							"from pc����, pc������, cpu, gpu\r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ� and pc����.GPU_���ڵ� = gpu.���ڵ� and pc����.pc_�����ڵ� = '001'\r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� \r\n" + 
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							"order by �������̺�.����;";
				}
				else if(second.equals("CPU���� ��")){
					sql = "select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, cpu.���ɼ���, count(*) as �ܿ�����, pc����.pc_�����ڵ� \r\n" + 
							"from pc����, pc������, cpu \r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ� and pc����.pc_�����ڵ� = '001'\r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�, \r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,"+
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU, \r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� "+ 
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							"order by �������̺�.���ɼ���;";
					
				}
				else {
					sql = "select pc����.�ڵ�� as pc����, ȸ���.�ڵ�� as ������, pc��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)`, �������̺�.�ܿ�����\r\n" + 
							"from (select pc����.�����ڵ�, pc������.ȸ���ڵ�, pc����.ip�ּ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, gpu.���ɼ���, count(*) as �ܿ�����, pc����.pc_�����ڵ�\r\n" + 
							"from pc����, pc������, gpu\r\n" + 
							"where pc������.���ڵ� = pc����.pc_���ڵ� and pc����.GPU_���ڵ� = gpu.���ڵ� and pc����.pc_�����ڵ� = '001' \r\n" + 
							"group by pc������.ȸ���ڵ�, pc_���ڵ�, cpu_���ڵ�, gpu_���ڵ�, `RAM(GB)`) as �������̺�,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,"+
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
							"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as pc���� "+ 
							"where �������̺�.�����ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+school+"' and �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.pc_�����ڵ� = pc����.�ڵ�\r\n" + 
							"order by �������̺�.���ɼ���;";
				}
			}
			res = state.executeQuery(sql);
			
			int row = 0;
			while(res.next()) {
				strRes += res.getString(1) + "#"; // ����
				strRes += res.getString(2) + "#"; // ������
				strRes += res.getString(3) + "#"; // ��
				strRes += res.getString(4) + "#"; // cpu
				strRes += res.getString(5) + "#"; // gpu
				strRes += res.getString(6) + "#"; // ram
				strRes += res.getString(7) + "#"; // �ܿ�����
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
				"  `IP�ּ�` VARCHAR(25) NOT NULL,\r\n" + 
				"  `������` VARCHAR(20) NOT NULL,\r\n" + 
				"  `PC_��` VARCHAR(50) NOT NULL,\r\n" + 
				"  `CPU_��` VARCHAR(50) NOT NULL,\r\n" + 
				"  `GPU_��` VARCHAR(50) NOT NULL,\r\n" + 
				"  `RAM(GB)` INT NOT NULL,\r\n" + 
				"  `PC_id` VARCHAR(30) NOT NULL,\r\n" + 
				"  `password` VARCHAR(20) NOT NULL,\r\n" + 
				"  PRIMARY KEY (`IP�ּ�`));";
		state.executeUpdate(sql);
		
		sql = "insert into temp (select pc����.IP�ּ�, ȸ���.�ڵ�� as ������, PC��.�ڵ�� as PC, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, PC����.`RAM(GB)`, pc����.PC_id, pc����.password\r\n" + 
				"from pc������, pc����, cpu, gpu,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU\r\n" + 
				"where ȸ���.�ڵ�� = '"+man+"' and PC��.�ڵ�� = '"+model+"' and CPU.�ڵ�� = '"+cpu+"' and GPU.�ڵ�� = '"+gpu+"' and pc����.`RAM(GB)` = "+ram+" and\r\n" + 
				"pc������.���ڵ� = pc����.PC_���ڵ� and pc����.CPU_���ڵ� = cpu.���ڵ� and pc����.GPU_���ڵ� = gpu.���ڵ� and pc������.ȸ���ڵ� = ȸ���.�ڵ�\r\n" + 
				"and pc����.PC_���ڵ� = PC��.�ڵ� and cpu.���ڵ� = CPU.�ڵ� and gpu.���ڵ� = GPU.�ڵ� and pc����.PC_�����ڵ� = '001');";
		state.executeUpdate(sql);
		
		sql = "set @ip:= (select temp.IP�ּ�\r\n" + 
				"from  temp, \r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU \r\n" + 
				"where  temp.������ = ȸ���.�ڵ�� and  temp.PC_�� = PC��.�ڵ�� and  temp.CPU_�� = CPU.�ڵ�� and  temp.GPU_�� = gpu.�ڵ�� \r\n" + 
				"group by  temp.IP�ּ�\r\n" + 
				"limit 1);";
		state.executeQuery(sql);
		
		sql = "UPDATE pc���� SET PC_�����ڵ� = '002' WHERE (`IP�ּ�` = @ip);";
		state.executeUpdate(sql);
		
		sql = "set @ip:= (select temp.IP�ּ�\r\n" + 
				"from temp, \r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU \r\n" + 
				"where  temp.������ = ȸ���.�ڵ�� and  temp.PC_�� = PC��.�ڵ�� and  temp.CPU_�� = CPU.�ڵ�� and  temp.GPU_�� = gpu.�ڵ�� \r\n" + 
				"group by  temp.IP�ּ�\r\n" + 
				"limit 1);";
		state.executeQuery(sql);
		
		sql = "INSERT INTO `servermanagement`.`�뿩��` (`�뿩��id`, `IP�ּ�`, `�뿩��`, `�ݳ�������`, `���忩��`, `����˸���������`) VALUES ('"+id+"', @ip, curdate(), '"+returnD+"', '1', '"+alarm+"');" ;
		state.executeUpdate(sql);
	}
	
	public String pcinform(String id) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "select pc��.�ڵ�� as PC��, ȸ���.�ڵ�� as ������, �뿩��.IP�ּ�, pc����.PC_id, pc����.password, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, pc����.`RAM(GB)`, if(�뿩��.���忩��, 'O', 'X') as �����û���ɿ���, �뿩��.�뿩��, �뿩��.�ݳ������� \r\n" + 
				"from �뿩��, pc����, pc������,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
				"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU \r\n" + 
				"where �뿩��.�뿩��id = '"+id+"' and �뿩��.IP�ּ� = pc����.IP�ּ� and pc����.PC_���ڵ� = pc������.���ڵ� and pc������.ȸ���ڵ� = ȸ���.�ڵ� and pc����.PC_���ڵ� = PC��.�ڵ� and pc����.CPU_���ڵ� = CPU.�ڵ� and pc����.GPU_���ڵ� = GPU.�ڵ�;";
	
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
		
		String sql = "SELECT �ݳ������� FROM servermanagement.�뿩�� where �뿩��id ='"+id+"'";
		
		res = state.executeQuery(sql);
		
		while(res.next()) {
			data = res.getString(1);
		}
		
		return data;
	}
	
	public void updateExtendDate(String id, String d) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "update �뿩�� set �ݳ������� = '"+d+"' , ���忩�� = \"0\" where �뿩��id ='"+id+"' ;";
		
		state.executeUpdate(sql);
		
	}

	public String isBorrower(String id) throws SQLException {
		
		state = conn.createStatement();
		
		String sql = "SELECT * FROM servermanagement.�뿩�� where �뿩��id = '"+id+"';";
		
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
		
		String sql = "UPDATE `servermanagement`.`�л�` SET `�̸���` = '"+stuMail+"' WHERE (`�л�id` = '"+stuId+"');";
		state.executeUpdate(sql);
		
		sql = "Update ��������ٱ��� set �����_�����ڵ� = 002 where id = '"+stuId+"'";
		state.executeUpdate(sql);
	}

	public String requestUserList() {
		try {
			state = conn.createStatement();
			
			String sql = "select ���.id, ���.password, ���.����, ���б�.�ڵ�� as ���б�, ȸ���з�.�ڵ�� as ȸ���з�, ���.�г���, ���.�޴���ȭ��ȣ, ���.�̸���, ���.�����ڵ�\r\n" + 
					"from\r\n" + 
					"(select * from �����, �л� where �����.id = �л�.�л�id\r\n" + 
					"union\r\n" + 
					"select * from �����, pc_�������� where �����.id = pc_��������.PC_��������id) as ���, ��������ٱ���,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '008') as ȸ���з�\r\n" + 
					"where ���.�б��ڵ� = ���б�.�ڵ� and ���.id = ��������ٱ���.id and ȸ���з�.�ڵ� = ��������ٱ���.�����_�����ڵ�;\r\n" + 
					"\r\n" + 
					"";
			
			res = state.executeQuery(sql);
			
			String data = "";
			
			while(res.next()) {
				data += res.getString(1) + "#";  // id
				data += res.getString(2) + "#"; // ���
				data += res.getString(3) + "#";  // �̸�
				data += res.getString(4) + "#";	 // ���б�
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
	} // ��� : �л�, ������ ����Ʈ ��ȸ
	
	public String requestStudentList() {
		try {
			state = conn.createStatement();
			
			String sql = "select ���.id, ���.password, ���.����, ���б�.�ڵ�� as ���б�, ȸ���з�.�ڵ�� as ȸ���з�, ���.�г���, ���.�޴���ȭ��ȣ, ���.�̸���, ���.�����ڵ�\r\n" + 
					"from\r\n" + 
					"(select * from �����, �л� where �����.id = �л�.�л�id) as ���, ��������ٱ���,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '008') as ȸ���з�\r\n" + 
					"where ���.�б��ڵ� = ���б�.�ڵ� and ���.id = ��������ٱ���.id and ȸ���з�.�ڵ� = ��������ٱ���.�����_�����ڵ�;";
			
			res = state.executeQuery(sql);
			
			String data = "";
			while(res.next()) {
				data += res.getString(1) + "#";  // id
				data += res.getString(2) + "#"; // ���
				data += res.getString(3) + "#";  // �̸�
				data += res.getString(4) + "#";	 // ���б�
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
	} //��� : �л� ����Ʈ ��ȸ
	
	public ResultSet requestOurSchoolStudentList(String school) {
		try {
			state = conn.createStatement();
			String sql = "select �л�.�л�id, �����.�г���, �����.����, �л�.�޴���ȭ��ȣ, �л�.�̸��� \r\n" + 
					"from �����, �л�, (select * from �����ڵ�� where �����ڵ� = '001') as ���б�\r\n" + 
					"where �л�.�б��ڵ� = ���б�.�ڵ� and ���б�.�ڵ�� = '"+ school +"' and �����.id = �л�.�л�id;";
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
			String sql = "select ���.id, ���.password, ���.����, ���б�.�ڵ�� as ���б�, ȸ���з�.�ڵ�� as ȸ���з�, ���.�г���, ���.��ȭ��ȣ, ���.�̸���, ���.�����ڵ�\r\n" + 
					"from\r\n" + 
					"(select * from �����, pc_�������� where �����.id = pc_��������.PC_��������id) as ���, ��������ٱ���,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '008') as ȸ���з�\r\n" + 
					"where ���.�б��ڵ� = ���б�.�ڵ� and ���.id = ��������ٱ���.id and ȸ���з�.�ڵ� = ��������ٱ���.�����_�����ڵ�;";
			
			res = state.executeQuery(sql);
			
			String data = "";
			while(res.next()) {
				data += res.getString(1) + "#";  // id
				data += res.getString(2) + "#"; // ���
				data += res.getString(3) + "#";  // �̸�
				data += res.getString(4) + "#";	 // ���б�
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
	} // ��� : ���� ����Ʈ ��ȸ
	
	public void dropUser(String id) {
		try {
			state = conn.createStatement();
			String sql = "UPDATE `servermanagement`.`��������ٱ���` SET `�����_�����ڵ�` = '003' WHERE (`id` = '"+ id +"');";
			int temp = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // ��� : �л�, �������� ���� ����

	public void certificateUser(String id) {
		try {
			state = conn.createStatement();
			String sql = "UPDATE `servermanagement`.`��������ٱ���` SET `�����_�����ڵ�` = '002' WHERE (`id` = '"+ id +"');";
			state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // ��� : ����
	
	public String requestNotice() {
		try {
			String data = "";
			state = conn.createStatement();
			String sql = "select * from ��������;";
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
	} // �������� ����Ʈ ����
	
	public String selectDocument(int no) {
		try {
			String data = "";
			state = conn.createStatement();
			String sql = "UPDATE �������� SET ��������.��ȸ�� = ��������.��ȸ�� + 1 WHERE (��������.��ȣ = '"+ no + "');";
			state.executeUpdate(sql);
			sql = "select * from �������� where ��������.��ȣ = "+ no +";";
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
	} // �������� ����
	
	
	public void upLoadDocument(String op_id, String title, String content) { //��Ʈ��ũ �� �Ķ���� ����
		try {
			state = conn.createStatement();
			
			String sql = "INSERT INTO �������� (��ȣ, �ۼ���id,  �Խ��� ,  ���� ,  ���� ,  ��ȸ�� ) VALUES ('0', '"+ op_id +"', now(), '"+ title +"', '"+ content +"', '0');";
			state.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // ��� : �Խù� �ø���
	
	
	public void deleteDocument(int no) {
		try {
			state = conn.createStatement();
			String sql = "DELETE FROM �������� WHERE (��ȣ = "+ no +");";
			state.executeUpdate(sql);
			sql = "select @cnt:=0;";
			state.executeQuery(sql);
			sql = "update �������� set ��������.��ȣ = @cnt:=@cnt+1;";
			state.executeUpdate(sql);
			sql = "select @cnt;";
			res = state.executeQuery(sql);
			while(res.next()) {
				int startPoint = startPoint = res.getInt(1);
				sql = "alter table �������� auto_increment=" + startPoint;
			}
			state.executeUpdate(sql);					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // ��� : �Խù� �����ϱ�
	
	public String getSchoolList() {
		try {
			state = conn.createStatement();
			String sql = "select ����.�ڵ�� as �б���, ���б�.�ּ�, ���б�.��ȭ��ȣ \r\n" + 
					"from ���б�, \r\n" + 
					"(select * from �����ڵ�� where �����ڵ� = '001') as ����\r\n" + 
					"where ���б�.�б��ڵ� = ����.�ڵ�;";
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
	} // ��� : ���б� ����Ʈ �ҷ�����
	
	public String getSchool(String str) {
		try {
			state = conn.createStatement();
			String sql = "select ����.�ڵ�� as �б���, ���б�.�ּ�, ���б�.��ȭ��ȣ \r\n" + 
					"from ���б�, \r\n" + 
					"(select * from �����ڵ�� where �����ڵ� = '001') as ����\r\n" + 
					"where ���б�.�б��ڵ� = ����.�ڵ� and ����.�ڵ�� like '"+ str +"%';";
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
	} // ��� : �б� �˻�
	
	public void returnPC(String ip) {
		
		try {
			state = conn.createStatement();

			String sql ;
					//"set @ip:= (select �뿩��.IP�ּ� from �뿩��, pc���� where �뿩��.�ݳ������� = curdate() and �뿩��.IP�ּ� = pc����.ip�ּ�);";
			
			sql = "delete from �뿩�� where �뿩��.IP�ּ� = '"+ip+"';";
		    state.execute(sql);
		    
		    sql = "delete from temp where temp.IP�ּ� = '"+ip+"';";
		    state.execute(sql);
		    
			sql ="update pc���� set pc����.pc_�����ڵ�= '001' where pc����.IP�ּ� = '"+ip+"';";
			state.executeUpdate(sql);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�ݳ� ����");
		//e.printStackTrace();
		}
	}
	
	public ResultSet requestLenderList(String school) {
		try {
			state = conn.createStatement();
			String sql = "select �뿩��.�뿩��id, �����.����, �뿩��.IP�ּ�, pc��.�ڵ�� as PC, pc����.pc_id, pc����.password\r\n" + 
					"from �뿩��, �����, pc����, �л�, (select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as pc��, (select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���б�\r\n" + 
					"where �뿩��.�뿩��id = �����.id and �뿩��.�뿩��id = �л�.�л�id and ���б�.�ڵ�� = '"+ school +"' and �л�.�б��ڵ� = ���б�.�ڵ� and �뿩��.IP�ּ� = pc����.IP�ּ� and pc����.PC_���ڵ� = pc��.�ڵ�;";
			res = state.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public ResultSet requestMessagingTarget(String school) {
		try {
			state = conn.createStatement();
			String sql = "select �뿩�ڻ�.�޴���ȭ��ȣ\r\n" + 
					"from (select * \r\n" + 
					"from �뿩��, �л�, (select * from �����ڵ�� where �����ڵ� = '001') as ���б�\r\n" + 
					"where �뿩��.�뿩��id = �л�.�л�id and ���б�.�ڵ�� = '"+ school +"' and ���б�.�ڵ� = �л�.�б��ڵ�) as �뿩�ڻ�\r\n" + 
					"where �뿩�ڻ�.�ݳ������� = curdate() + 7 and �뿩�ڻ�.����˸��������� = 1 and �뿩�ڻ�.�޴���ȭ��ȣ is not null;";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return res;
	} // �޽��� ���� ��� �� ������
	
	public ResultSet showPCList(String school) {
		try {
			state = conn.createStatement();
			String sql = "select �������̺�.IP�ּ�, ȸ���.�ڵ�� as ������, PC��.�ڵ�� as �𵨸�, CPU.�ڵ�� as CPU, GPU.�ڵ�� as GPU, �������̺�.`RAM(GB)` as `RAM(GB)`, �������̺�.PC_id, �������̺�.password, PC_����.�ڵ�� as ����\r\n" + 
					"from (select pc����.IP�ּ�, pc������.ȸ���ڵ�, pc����.pc_���ڵ�, pc����.cpu_���ڵ�, pc����.gpu_���ڵ�, pc����.`RAM(GB)`, pc����.PC_id, pc����.password, pc����.pc_�����ڵ�\r\n" + 
					"from pc����, pc������, (select * from �����ڵ�� where �����ڵ��.�����ڵ� = '001') as ���� \r\n" + 
					"where pc����.pc_���ڵ� = pc������.���ڵ� and ����.�ڵ� = pc����.�����ڵ� and ����.�ڵ�� = '"+ school +"') as �������̺�,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '005') as ȸ���,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '004') as PC��,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '002') as CPU,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '003') as GPU,\r\n" + 
					"(select * from �����ڵ�� where �����ڵ��.�����ڵ� = '006') as PC_����\r\n" + 
					"where �������̺�.ȸ���ڵ� = ȸ���.�ڵ� and �������̺�.pc_���ڵ� = PC��.�ڵ� and �������̺�.cpu_���ڵ� = CPU.�ڵ� and �������̺�.gpu_���ڵ� = GPU.�ڵ� and �������̺�.PC_�����ڵ� = PC_����.�ڵ�\r\n" + 
					"order by �������̺�.`RAM(GB)` desc;";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return res;
	} // �������� pc ����Ʈ
	
	public ResultSet getCPU() {
		try {
			state = conn.createStatement();
			String sql = "select �ڵ�� from �����ڵ�� where �����ڵ� = '002';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} //CPU �޺��ڽ�
	
	public ResultSet getGPU() {
		try {
			state = conn.createStatement();
			String sql = "SELECT �ڵ�� FROM �����ڵ��  where �����ڵ� = '003';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} //GPU �޺��ڽ�
	
	public ResultSet getPC(String company) {
		try {
			state = conn.createStatement();
			String sql = "SELECT * FROM \r\n" + 
					"pc������, (SELECT * FROM �����ڵ�� where �����ڵ� = '004') as PC, (SELECT * FROM �����ڵ�� where �����ڵ� = '005') as ȸ��\r\n" + 
					"where pc������.���ڵ� = PC.�ڵ� and pc������.ȸ���ڵ� = ȸ��.�ڵ� and ȸ��.�ڵ�� = '"+ company +"';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} // PC �޺��ڽ�
	
	public ResultSet getCompany() {
		try {
			state = conn.createStatement();
			String sql = "SELECT �ڵ�� FROM �����ڵ�� where �����ڵ� = '005';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} //������ �޺��ڽ�
	
	public ResultSet getState() {
		try {
			state = conn.createStatement();
			String sql = "SELECT �ڵ�� FROM �����ڵ�� where �����ڵ� = '006';";
			res = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	} // �������� �޺��ڽ�
	
	
	
	public int registerPC(String ip, String pc, String cpu, String gpu, int ram, String id, String password, String school, String pcState) {
		int rs = -1;
		try {
			state = conn.createStatement();
			String sql = "INSERT INTO PC���� (IP�ּ�, PC_���ڵ�, CPU_���ڵ�, GPU_���ڵ�, `RAM(GB)`, PC_id, password, �����ڵ�, PC_�����ڵ�) \r\n" + 
					"VALUES ('"+ ip + "', (select �ڵ� from �����ڵ�� where �����ڵ� = '004' and �ڵ�� = '"+ pc +"'), "
					+ "(select �ڵ� from �����ڵ�� where �����ڵ� = '002' and �ڵ�� = '"+ cpu +"'), "
					+ "(select �ڵ� from �����ڵ�� where �����ڵ� = '003' and �ڵ�� = '"+ gpu +"'), '"+ram+"', "
					+ "'"+ id +"', '" + password + "', "
					+ "(select �ڵ� from �����ڵ�� where �����ڵ� = '001' and �ڵ�� = '"+ school +"'), "
					+ "(select �ڵ� from �����ڵ�� where �����ڵ� = '006'and �ڵ�� = '"+ pcState +"'));";
			rs = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	} // pc ���
	
	public int modifyPC(String standard, String newIp, String pc, String cpu, String gpu, int ram, String id, String password, String school, String pcState) {
		int rowCnt = 0;
		try {
			state = conn.createStatement();
			String sql = "UPDATE `pc����` \r\n" + 
					"SET `IP�ּ�` = '"+ newIp +"', `PC_���ڵ�` = (select �ڵ� from �����ڵ�� where �����ڵ� = '004' and �ڵ�� = '"+ pc +"'), \r\n" + 
					"`CPU_���ڵ�` = (select �ڵ� from �����ڵ�� where �����ڵ� = '002' and �ڵ�� = '"+ cpu +"'), \r\n" + 
					"`GPU_���ڵ�` = (select �ڵ� from �����ڵ�� where �����ڵ� = '003' and �ڵ�� = '"+ gpu +"'), \r\n" + 
					"`RAM(GB)` = '"+ ram +"', \r\n" + 
					"`PC_id` = '"+ id +"', \r\n" + 
					"`password` = '"+ password +"', \r\n" + 
					"`�����ڵ�` = (select �ڵ� from �����ڵ�� where �����ڵ� = '001' and �ڵ�� = '"+ school +"'),\r\n" + 
					"`PC_�����ڵ�` = (select �ڵ� from �����ڵ�� where �����ڵ� = '006'and �ڵ�� = '"+ pcState +"')\r\n" + 
					"WHERE (`IP�ּ�` = '"+ standard +"');";
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
			String sql = "DELETE FROM pc���� WHERE (`IP�ּ�` = '"+ ip +"');";
			rowCnt = state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCnt;
	}
}

