package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainServer implements Runnable{
   
   private MainServerThread clients[] = new MainServerThread[50]; 
   private ServerSocket server = null;
   private Thread thread = null;
   private InputStream is = null;
   private OutputStream os = null;
   private Protocol protocol = null;
   private Socket socket = null;
   private SQL sql = null;
   private StringTokenizer st ;
   private String res;
   
   private ResultSet rs;
   int resultCode;

   private int clientCount = 0;

   public MainServer(int port) {
      try {
            protocol = new Protocol();
            sql = new SQL("127.0.0.1", "root", "Dtd0613~~");
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
         } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
         }
      }
   
   private void addThread(Socket socket) {
         if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new MainServerThread(this, socket);
            try {
               clients[clientCount].open(); // Stream open
               clients[clientCount].start();
               clientCount++;
            } catch (IOException ioe) {
               System.out.println("Error opening thread: " + ioe);
            }
         } else
            System.out.println("Client refused: maximum " + clients.length + " reached.");
      }
   
   public void run() {
         while (thread != null) {
            try {
               System.out.println("Waiting for a client ...");
               addThread(server.accept());
            } catch (IOException ioe) {
               System.out.println("Server accept error: " + ioe);
               stop();
            }
         }
      }

      public void start() {
         if (thread == null) {
            thread = new Thread(this);
            thread.start();
         }
      }

      public synchronized void remove(int ID) {
      }

      public void stop() {
         if (thread != null) {
            thread.stop();
            thread = null;
         }
      }
      
      private int findClient(int ID) {
            for (int i = 0; i < clientCount; i++)
               if (clients[i].getID() == ID) {
                  return i;
               }
            return -1;
         }
   
      public synchronized void handle(OutputStream os, FileOutputStream fs, int ID, byte[] buf)
               throws IOException, SQLException {

            int packetType = buf[0]; // 수신 데이터에서 패킷 타입 얻음
            protocol.setPacket(packetType, buf); // 패킷 타입을 Protocol 객체의 packet 멤버변수에 buf를 복사
          
            switch (packetType) {
           
            case Protocol.PT_EXIT: // 프로그램 종료 수신
               protocol = new Protocol(Protocol.PT_EXIT);
               os.write(protocol.getPacket());

               break;

            case Protocol.PT_REQ_LOGIN:

               st = new StringTokenizer(protocol.getLogin(), "#");
               res = sql.doLogin(st.nextToken(), st.nextToken()); // id, pw

               protocol = new Protocol(Protocol.PT_RES_LOGIN);

               if (res != "") { // 로그인 성공
                  protocol.setCode("1");
                  protocol.setUserInform(res);
                  
                  if(res.endsWith("3#")) { // 영구정지
                     protocol.setCode("3");
                  }
                  
                  st = new StringTokenizer(res, "#");
                 
                  clients[findClient(ID)].setUserInfo(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
                  Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken());
               
               } else { // 로그인 실패
                  protocol.setCode("2");
               }
               os.write(protocol.getPacket());

               break;

            case Protocol.PT_REQ_SEARCHSCHOOL:

               st = new StringTokenizer(protocol.getSearchschool(), "#");
               res = sql.searchSchool(st.nextToken()); // id, pw

               protocol = new Protocol(Protocol.PT_RES_SEARCHSCHOOL);

               protocol.setSchools(res);
               os.write(protocol.getPacket());

               break;

            case Protocol.PT_REQ_CHECKDUPLICATE_ID: // 중복 결과를 보냄. 0이면 중복 X 1이면 중복 O

               res = sql.duplicateCheckId(protocol.getId());

               protocol = new Protocol(Protocol.PT_RES_CHECKDUPLICATE_ID);

               if (res.equals("")) { // 중복 X
                  protocol.setCode("0");
               } else { // 중복O
                  protocol.setCode("1");
               }
               os.write(protocol.getPacket());

               break;

            case Protocol.PT_REQ_SIGNFORUSER:
              
               st = new StringTokenizer(protocol.getData(), "#");
              
               String name = st.nextToken();
               String nickname = st.nextToken();
               String school = st.nextToken();
               String code = st.nextToken();
               String id = st.nextToken();
               String pw = st.nextToken();

               sql.signforUser(name, nickname, school, code, id, pw); // 회원 정보 등록

               protocol = new Protocol(Protocol.PT_RES_SIGNFORUSER);
               os.write(protocol.getPacket());
               
               clients[findClient(ID)].setUserInfo(id, pw, name, nickname, Integer.parseInt(code), "null","null",school);
               
               break;

            case Protocol.PT_REQ_NOTELIST:
               
               String noteList = sql.requestNotice();
               protocol = new Protocol(Protocol.PT_RES_NOTELIST);
               protocol.setData(noteList);
               os.write(protocol.getPacket());
               break;

            case Protocol.PT_REQ_WINDOW: // 20

               String str = "";
               String kindofOpen = protocol.getWindowNum();
               protocol = new Protocol(Protocol.PT_RES_WINDOW);

               switch (kindofOpen) {
               case "1":
                  protocol.setCode("1");
                  break; // 마이페이지 비밀번호 입력

               case "2":
                  protocol.setCode("2");
                  os.write(protocol.getPacket());
                  break; // PC 등록 창

               case "3":
                  protocol.setCode("1");
                  os.write(protocol.getPacket());
                  break; // PC 수정 창
               }
               os.write(protocol.getPacket());
               break;

            case Protocol.PT_REQ_MAG_MYPAGE:
               // 사용자 정보를 어떻게 들고 있어야할지 의문이다// 이건 그냥 테스트 용이고
               protocol = new Protocol(Protocol.PT_RES_MAG_MYPAGE);
               protocol.setMyPage(clients[findClient(ID)].getUserInfo().getId() + "#"
                     + clients[findClient(ID)].getUserInfo().getNickName() + "#" + clients[findClient(ID)].getUserInfo().getName()
                     + "#" + clients[findClient(ID)].getUserInfo().getSchool() + "#"
                     + clients[findClient(ID)].getUserInfo().getPhoneNum());
               os.write(protocol.getPacket());
              
               break;

            case Protocol.PT_REQ_PCLIST:
               protocol = new Protocol(Protocol.PT_RES_PCLIST);
               rs = sql.showPCList(clients[findClient(ID)].getUserInfo().getSchool());
               String pcList = "";
               while (rs.next()) {
                  pcList += rs.getString(1) + "#" + rs.getString(2) + "#" + rs.getString(3) + "#" + rs.getString(4) + "#"
                        + rs.getString(5) + "#" + rs.getString(6) + "#" + rs.getString(7) + "#" + rs.getString(8) + "#"
                        + rs.getString(9) + "#";
               }
               protocol.setDataAboutPC(pcList);
               os.write(protocol.getPacket());
               break;

            case Protocol.PT_REQ_REGISTERPC:
               res = protocol.getPCRegistrationInfo();
               st = new StringTokenizer(res, "#");
               resultCode = sql.registerPC(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
                     Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(),
                     clients[findClient(ID)].getUserInfo().getSchool(), st.nextToken());
               protocol = new Protocol(Protocol.PT_RES_REGISTERPC);
               if (resultCode == 1) {
                  protocol.setCode("1");
               } else {
                  protocol.setCode("2");
               }
               os.write(protocol.getPacket());
               break;
               
            case Protocol.PT_REQ_MODIFYPC:
               res = protocol.getModifyPCInfo();
               st = new StringTokenizer(res, "#");
               int rowCnt = sql.modifyPC(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), clients[findClient(ID)].getUserInfo().getSchool(), st.nextToken());
               protocol = new Protocol(Protocol.PT_RES_MODIFYPC);
               if(rowCnt == 1) {
                  protocol.setCode("1");
               }else {
                  protocol.setCode("2");
               }
               os.write(protocol.getPacket());
               break;

            case Protocol.PT_REQ_DELETEPC:
               res = protocol.getDeletePCInfo();
               protocol = new Protocol(Protocol.PT_RES_DELETEPC);
               if(sql.deletePC(res) == 1) {
                  protocol.setCode("1");
               }else {
                  protocol.setCode("2");
               }
               os.write(protocol.getPacket());
               break;
               
            case Protocol.PT_REQ_STUDENTLIST:
               rs = sql.requestOurSchoolStudentList(clients[findClient(ID)].getUserInfo().getSchool());
               res = "";
               
               while(rs.next()) {
                  res += rs.getString(1) + "#" + rs.getString(2) + "#" +rs.getString(3) + "#" +rs.getString(4) + "#" +rs.getString(5) + "#";
               }
               protocol = new Protocol(Protocol.PT_RES_STUDENTLIST);
               protocol.setStudentList(res);
               os.write(protocol.getPacket());
               break;
               
            case Protocol.PT_REQ_LENDERLIST:
               rs = sql.requestLenderList(clients[findClient(ID)].getUserInfo().getSchool());
               res = "";
               
               while(rs.next()) {
                  res += rs.getString(1) + "#" + rs.getString(2) + "#" + rs.getString(3) + "#" + rs.getString(4) + "#" + rs.getString(5) + "#" + rs.getString(6) + "#";
               }
               
               protocol = new Protocol(Protocol.PT_RES_LENDERLIST);
               protocol.setPCLenderList(res);
               os.write(protocol.getPacket());
               break;
               
            case Protocol.PT_REQ_TARGET_PHONENUM:
               rs = sql.requestMessagingTarget(clients[findClient(ID)].getUserInfo().getSchool());
               res = clients[findClient(ID)].getUserInfo().getPhoneNum() + "#";
               
               while(rs.next()){
                  res += rs.getString(1) + "#";
               }
            
               protocol = new Protocol(Protocol.PT_RES_TARGET_PHONENUM);
               protocol.setTargetPhoneNum(res);
               os.write(protocol.getPacket());
               break;
               
            case Protocol.PT_REQ_NOTE:
               
               res = sql.selectDocument(Integer.parseInt(protocol.getCode()));
               
               protocol = new Protocol(Protocol.PT_RES_NOTE);
               protocol.setData(res);
               os.write(protocol.getPacket());
               break;
                    
            case Protocol.PT_REQ_CERTIFICATESTUDENT: // 학생 인증상태와 메일주소
                           
                           String stid = protocol.getId();
                           String stschool = protocol.getSchool();
                           
                           String state= sql.getStudentstate(stid);
                           
                           st = new StringTokenizer(state,"#");
                           String cer = st.nextToken();
                           String bor = st.nextToken();
                           
                           if(cer.equals("1")) { // 비인증
                              protocol = new Protocol(Protocol.PT_RES_CERTIFICATESTUDENT);
                              protocol.setHidemail(sql.getHideMail(stschool));
                              protocol.setCode("0");
                           }
                           else { //인증
                              protocol = new Protocol(Protocol.PT_RES_CERTIFICATESTUDENT);
                              protocol.setCode("1");
                              if(bor.equals("1")) { // 대여자 O
                                 protocol.setBorrowCode("1");
                              }
                              else  protocol.setBorrowCode("0"); // 대여자 X
                           }
                           os.write(protocol.getPacket());
                          
                           break;
                           
                        case Protocol.PT_REQ_CHANGEDATA:
                           
                           stid = protocol.getId();
                           pw = protocol.getPassword();
                           String phoneNum = protocol.getPhoneNum();
                           sql.updateUserdata(stid,  pw, phoneNum);
                           
                           protocol = new Protocol(Protocol.PT_RES_CHANGEDATA);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_SORTED_PCLIST: // data를 분할해서 보낸 후 
                                                      // client에서는 string에다 모두 저장
                           school = protocol.getPC_School();
                           // 처음 sorstedPclist면 당연히 암 것도 없지
                           String firstc = protocol.getFirstchoice();
                           String secondc = protocol.getSecondchoice();
                           pcList = sql.getPCList(school, firstc, secondc);

                           protocol = new Protocol(Protocol.PT_RES_SORTED_PCLIST);
                           protocol.setSortedpcList(pcList); // pc개수 + ...
                           os.write(protocol.getPacket());
                              
                           break;
                       
                        case Protocol.PT_REQ_PCAPPLY: // data를 분할해서 보낸 후 
                           // client에서는 string에다 모두 저장
                              st = new StringTokenizer(protocol.getPCApplyData());
                              id = st.nextToken("#");
                              String man = st.nextToken("#");
                              String model = st.nextToken("#");
                              String cpu = st.nextToken("#");
                              String gpu = st.nextToken("#");
                              String ram = st.nextToken("#");
                              String returnD = st.nextToken("#");
                              String alarm = st.nextToken("#");
                              
                           sql.enrollPCBorrower(id, man, model, cpu, gpu, ram, returnD, alarm);
                           protocol = new Protocol(Protocol.PT_RES_PCAPPLY);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_MYPCINFORM:
                           
                           id = protocol.getId();
                           String data = sql.pcinform(id);
                           
                           protocol = new Protocol(Protocol.PT_RES_MYPCINFORM);
                           protocol.setData(data);
                           os.write(protocol.getPacket());
                           
                           break;
                        case Protocol.PT_REQ_MYPC_RETURNDATE:
                           
                           id = protocol.getId();
                           data = sql.getPcreturnDate(id);
                           
                           protocol = new Protocol(Protocol.PT_RES_MYPC_RETURNDATE);
                           protocol.setData(data);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_EXTENDAPPLY:
                           
                           id = protocol.getId();
                           String date = protocol.getPCExtendDate();
                           sql.updateExtendDate(id, date);
                           
                           protocol = new Protocol(Protocol.PT_RES_EXTENDAPPLY);
                           os.write(protocol.getPacket());
                           
                           break;
                        case Protocol.PT_REQ_CANBORROW:
                           
                           id = protocol.getId();
                           String isBorrower = sql.isBorrower(id);
                           
                           protocol = new Protocol(Protocol.PT_RES_CANBORROW);
                           
                           if(isBorrower.equals("0")) {
                              protocol.setCode("0");
                           }
                           else {
                              protocol.setCode("1");
                           }
                           os.write(protocol.getPacket());
                           
                           break;
                        case Protocol.PT_REQ_CERTIFICATENUM:
                           
                           protocol = new Protocol(Protocol.PT_RES_CERTIFICATENUM);
                           
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_UPLOAD:
                           
                           data = protocol.getData();
                           StringTokenizer st = new StringTokenizer(data, "#");
                           
                           String op_id = st.nextToken();
                           String title = st.nextToken();
                           String content = st.nextToken();
                           
                           Pattern pattern = Pattern.compile("<[^>]*>");
                           Matcher matcher = pattern.matcher(content);
                           
                           final StringBuffer text = new StringBuffer(content.length());

                           while (matcher.find()) {
                               matcher.appendReplacement(text," ");}

                             matcher.appendTail(text);

                          sql.upLoadDocument(op_id, title, text.toString().trim());
                           protocol = new Protocol(Protocol.PT_RES_UPLOAD);
                           
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_DELETENOTE: 
                           sql.deleteDocument(Integer.parseInt(protocol.getData()));
                           protocol = new Protocol(Protocol.PT_RES_DELETENOTE);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_DROPUSER: 
                           
                           sql.dropUser(protocol.getId());
                           protocol = new Protocol(Protocol.PT_RES_DROPUSER);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_CERTIFICATEMG:
                           
                           sql.certificateUser(protocol.getId());
                           protocol = new Protocol(Protocol.PT_RES_CERTIFICATEMG);
                           
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_SCHOOLDETAIL:
                           
                           data = sql.getSchool(protocol.getData());
                           
                           protocol = new Protocol(Protocol.PT_RES_SCHOOLDETAIL);
                           protocol.setData(data);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_PCRETURN:
                           
                           sql.returnPC(protocol.getData());
                           
                           protocol = new Protocol(Protocol.PT_RES_PCRETURN);
                           os.write(protocol.getPacket());
                           
                           break;
                           
                        case Protocol.PT_REQ_CERTIFICATE_MANAGER:
                        	
                           data = sql.getManagerstate(protocol.getId());
                        	
                           protocol = new Protocol(Protocol.PT_RES_CERTIFICATE_MANAGER);
                           protocol.setCode(data);
                           os.write(protocol.getPacket());
                            
                           break;
                        	
            }
         }

   public static void main(String[] args) {
      MainServer server = new MainServer(3000);
   }
} //////////// MAIN SERVER