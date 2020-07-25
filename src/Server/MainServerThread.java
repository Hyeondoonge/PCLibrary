package Server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

import Main.User;

public class MainServerThread extends Thread {

	private MainServer server = null;
	private Socket socket = null;
	private int ID = -1;
	private InputStream is = null;
	private OutputStream os = null;
	private FileOutputStream fs = null;
	private FileInputStream fis = null;
	// private User user = null;
	private int tempsize = 0;
	private int cnt = 0;
	private int a_cnt = 0;
	private int total_pc;
	private Protocol protocol = null;
	private String updownStudentNum = null;
	private SQL sql;
	private File f = null;

	private String randomNum;
	private String stuMail;
	private String stuId;
	private StringTokenizer st;
	// 개별 다운로드 전용
	String filenameD = null;
	String filesizeD = null;
	String studentNumD = null;
	int cntD = 0; // 몇 번째 분할
	private int dataSize = 0;
	private String data = "";
	private User user = null;

	ArrayList<byte[]> dataD = new ArrayList<>();

	public MainServerThread(MainServer _server, Socket _socket) {
		super();
		server = _server;
		socket = _socket;
		ID = socket.getPort();
	}

	public void DNULL() {
		filenameD = null;
		filesizeD = null;
		studentNumD = null;
		cntD = 0; // 몇 번째 분할

		dataD = new ArrayList<>();
	}

	public User getUserInfo() {
		return user;
	}

	public void setUserInfo(String id, String password, String name, String nickName, int code, String phoneNum,
			String mail, String school) {
		user = new User(id, password, name, nickName, code, phoneNum, mail, school);
	}

	public int getID() {
		return ID;
	}

	public String getUpdownStudentNum() {
		return updownStudentNum;
	}

	public int getTempSize() {
		return tempsize;
	}

	public void setTempSize(int tempsize) {
		this.tempsize = tempsize;
	}

	public void send(byte[] packet) {
		try {
			os.write(packet);
			os.flush();
		} catch (IOException ioe) {
			System.out.println(ID + " ERROR sending: " + ioe.getMessage());
			stop();
		}
	}

	public void run() {
		System.out.println("Server Thread " + ID + " running.");
		byte[] buf = new byte[Protocol.LEN_PROTOCOL_TYPE + Protocol.LEN_ID + Protocol.LEN_DATA];
		sql = new SQL();

		try {
			while (true) {

				is.read(buf);
				protocol = new Protocol();
				protocol.setPacket(buf[0], buf);

				if (buf[0] == Protocol.PT_REQ_CERTIFICATENUM) { // 인증번호 요청

					Random r = new Random();
					int random = r.nextInt(9000) + 1000; //
					randomNum = Integer.toString(random);

					stuMail = protocol.getMail();
					stuId = protocol.getId();
					sendMail(randomNum, stuMail);

					protocol = new Protocol(Protocol.PT_RES_CERTIFICATENUM);
					os.write(protocol.getPacket());
				}

				else if (buf[0] == Protocol.PT_REQ_CERTIFICATENUM_CHECK) {
					String code;
					if (protocol.getData().equals(randomNum)) {
						code = "1";
						sql.updateCertificateStudent(stuId, stuMail);
					} else
						code = "0";

					protocol = new Protocol(Protocol.PT_RES_CERTIFICATENUM_CHECK);

					protocol.setCode(code);
					os.write(protocol.getPacket());
				}

				else if (buf[0] == Protocol.PT_REQ_USERLIST) { // 회원목록
					if (cnt == 0) {
						String code = protocol.getCode();

						if (code.equals("0")) { // 0 -> 학생 + 관리자
							data = sql.requestUserList();
						} else if (code.equals("1")) { // 1 -> 학생
							data = sql.requestStudentList();
						} else { // 2 -> 관리자
							data = sql.requestManagerList();
						}
					}
					// 코드에 따라 sql 실행하고 그 결과를 data에 저장

					protocol = new Protocol(Protocol.PT_RES_USERLIST);

					// 분할 데이터 전송을 위한 코드
					if (data.length() >= 5000) {
						dataSize = data.length();
						String tempData = data.substring(0, 5000);

						data = data.substring(5000);
						dataSize -= 5000;

						protocol.setIslast("0");
						protocol.setUserlist(tempData);

						cnt++;
					} else {
						protocol.setIslast("1");
						protocol.setUserlist(data); // 마지막
						data = "";
						dataSize = 0;

						cnt = 0;
					}
					os.write(protocol.getPacket());
				}

				else if (buf[0] == Protocol.PT_REQ_SCHOOLLIST) {

					if (cnt == 0) {
						data = sql.getSchoolList();
					}
					protocol = new Protocol(Protocol.PT_RES_SCHOOLLIST);

					// 분할 데이터 전송을 위한 코드
					if (data.length() >= 5000) {
						dataSize = data.length();
						String tempData = data.substring(0, 5000);

						data = data.substring(5000);
						dataSize -= 5000;

						protocol.setIslast("0");
						protocol.setSchoollist(tempData);

						cnt++;
					} else {
						protocol.setIslast("1");
						protocol.setSchoollist(data); // 마지막
						data = "";
						dataSize = 0;

						cnt = 0;
					}
					os.write(protocol.getPacket());
				} else {
					server.handle(os, fs, ID, buf);
				}

			} // end while

		} catch (IOException ioe) {
			System.out.println(ID + " ERROR reading: " + ioe.getMessage());
			stop();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setFileInfo(String studentNum) {
		long fsize = 0;
		// filename은 DB table에 접근해서 얻어낸다.
		// filename = Select 이미지경로 from 결핵진단서 where 학번 = studentNum;
		filenameD = "packman.png";

		f = new File("C:\\tuber\\" + studentNum + ".jpg"); // 파일 위치
		if (f.exists()) {
			fsize = f.length();
		}
		filesizeD = Long.toString(fsize);
	}

	private void fileSplit(FileInputStream file) throws FileNotFoundException, IOException {
		int fsize = Integer.parseInt(filesizeD);

		dataD = new ArrayList();
		for (int i = 0; i <= fsize / 2000; i++) { // 103358 byte -> 104번 0~ 103
			dataD.add(new byte[2000]);
			file.read(dataD.get(i), 0, 2000);
		}

	}

	public void open() throws IOException {
		is = socket.getInputStream();
		os = socket.getOutputStream();
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (is != null)
			is.close();
		if (os != null)
			os.close();
	}

	public void sendMail(String ran, String receiver) {

		String host = "localhost";
		final String user = "jsi06138@gmail.com"; //
		final String password = "dtd06134707";

		String to = receiver;// receiver

		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smpt.debug", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// Compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("PC Library 에서 보내온 인증번호입니다");
			message.setText("인증번호: " + ran);

			// send the message
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}