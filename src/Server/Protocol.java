package Server;

public class Protocol {
   public static final int PT_UNDEFINED = -1; // 프로토콜이 지정되어 있지 않은 경우
   public static final int PT_EXIT = 0; // 프로그램 종료
   //학생
   public static final int PT_REQ_WINDOW = 1;
   public static final int PT_RES_WINDOW = 2;
   public static final int PT_REQ_SEARCHSCHOOL = 3; // 학교 찾기
   public static final int PT_RES_SEARCHSCHOOL = 4; 
   public static final int PT_REQ_CHECKDUPLICATE_ID = 5;// 아이디 중복 체크
   public static final int PT_RES_CHECKDUPLICATE_ID = 6;
   public static final int PT_REQ_LOGIN = 7; //로그인
   public static final int PT_RES_LOGIN = 8; // 로그인 응답
   public static final int PT_REQ_CERTIFICATENUM = 51; // 학생 인증번호 요청
   public static final int PT_RES_CERTIFICATENUM = 52;
   public static final int PT_REQ_CERTIFICATENUM_CHECK = 53; // 학생 인증번호 요청
   public static final int PT_RES_CERTIFICATENUM_CHECK = 54;
   public static final int PT_REQ_CERTIFICATESTUDENT = 9; // 학생 인증 
   public static final int PT_RES_CERTIFICATESTUDENT = 10;
   public static final int PT_REQ_CHANGEDATA = 11; // 회원정보 수정
   public static final int PT_RES_CHANGEDATA = 12;
   public static final int PT_REQ_EXTENDAPPLY = 13; // 연장 신청
   public static final int PT_RES_EXTENDAPPLY = 14;
   public static final int PT_REQ_SORTED_PCLIST = 15; // 정렬 PC
   public static final int PT_RES_SORTED_PCLIST = 16;
   public static final int PT_REQ_PCAPPLY = 17; // pc 신청
   public static final int PT_RES_PCAPPLY = 18;
   public static final int PT_REQ_PCRETURN = 63; // pc 신청
   public static final int PT_RES_PCRETURN = 64;
   public static final int PT_REQ_NOTELIST = 57; //게시물
   public static final int PT_RES_NOTELIST = 58;
   public static final int PT_REQ_NOTE = 19; //게시물
   public static final int PT_RES_NOTE = 20;
   public static final int PT_REQ_SIGNFORUSER = 55;
   public static final int PT_RES_SIGNFORUSER = 56;
   
   //관리자

   public static final int PT_REQ_REGISTERPC = 21; //pc 등록
   public static final int PT_RES_REGISTERPC = 22;
   public static final int PT_REQ_MODIFYPC = 23; //pc 수정
   public static final int PT_RES_MODIFYPC = 24;
   public static final int PT_REQ_DELETEPC = 25; //pc 삭제
   public static final int PT_RES_DELETEPC = 26;
   public static final int PT_REQ_RETURNMESSAGE = 27; // 메시지 전송
   public static final int PT_RES_RETURNMESSAGE = 28;
	public static final int PT_REQ_MAG_MYPAGE = 100; // 관리자 마이페이지
	public static final int PT_RES_MAG_MYPAGE = 101; //
   //운영자
   public static final int PT_REQ_USERLIST = 29; //회원 관리
   public static final int PT_RES_USERLIST = 30;
//   public static final int PT_REQ_SORTED_USERLIST = 31; //사용자 구분에 따른 회원 리스트
//   public static final int PT_RES_SORTED_USERLIST = 32;
   public static final int PT_REQ_USERINFORM = 33; // 사용자 정보
   public static final int PT_RES_USERINFORM = 34;
   public static final int PT_REQ_DROPUSER = 35; // 영구정지
   public static final int PT_RES_DROPUSER = 36;
   public static final int PT_REQ_CERTIFICATEMG = 37; //직원 인증
   public static final int PT_RES_CERTIFICATEMG = 38;
   public static final int PT_REQ_UPLOAD = 39; //공지사항 업로드
   public static final int PT_RES_UPLOAD = 40;
   public static final int PT_REQ_DELETENOTE = 41; //게시물 삭제
   public static final int PT_RES_DELETENOTE = 42;
   public static final int PT_REQ_MYPCINFORM = 45; // 대여PC 조회
   public static final int PT_RES_MYPCINFORM = 46;
   public static final int PT_REQ_MYPC_RETURNDATE = 47;
   public static final int PT_RES_MYPC_RETURNDATE = 48;
   public static final int PT_REQ_CANBORROW = 49;
   public static final int PT_RES_CANBORROW = 50;
   public static final int PT_REQ_SCHOOLLIST = 59;
   public static final int PT_RES_SCHOOLLIST = 60;
   public static final int PT_REQ_SCHOOLDETAIL = 61;
   public static final int PT_RES_SCHOOLDETAIL = 62;
   public static final int PT_REQ_CHANGEDATA_PAGE = 102; //게시물 수정창
   public static final int PT_RES_CHANGEDATA_PAGE = 103; 
	
   public static final int PT_REQ_PCLIST = 104; // pc 목록 요청
   public static final int PT_RES_PCLIST = 105;
	
   public static final int PT_REQ_STUDENTLIST = 106; //학생 목록 조회(관리 직원)
   public static final int PT_RES_STUDENTLIST = 107;
   public static final int PT_REQ_LENDERLIST = 108; //pc 대여자 조회
   public static final int PT_RES_LENDERLIST = 109;
   public static final int PT_REQ_TARGET_PHONENUM = 110; // 반납 대상자 폰번호
   public static final int PT_RES_TARGET_PHONENUM = 111;
   public static final int PT_REQ_CERTIFICATE_MANAGER = 112; 
   public static final int PT_RES_CERTIFICATE_MANAGER = 113;
	
   public static final int LEN_PROTOCOL_TYPE = 1;
   public static final int LEN_DATA = 10000; // 최대 10000바이트
   public static final int LEN_LAST = 2;
   public static final int LEN_FRAG = 2;
   public static final int LEN_SEQNUM = 2;
   public static final int LEN_ID = 20;
   public static final int LEN_PASSWORD = 20;
   public static final int LEN_NAME = 30;
   public static final int LEN_NICKNAME = 20;
   public static final int LEN_USERCODE = 3;
   public static final int LEN_PHONENUM = 20;
   public static final int LEN_SCHOOL = 50;
   public static final int LEN_MAIL = 40;
   public static final int LEN_CODE = 3;
   public static final int LEN_CHOICE = 15;
   public static final int LEN_MAXDATA = 6000;
   
   public static final int LEN_WINDOW_NUMBER = 2; 
   
   protected int protocolType;
   private byte[] packet; // 프로토콜과 데이터의 저장공간이 되는 바이트 배열

   public Protocol() { // 생성자
      this(PT_UNDEFINED);
   }

   public Protocol(int protocolType) { // 생성자
      this.protocolType = protocolType;
      getPacket(protocolType);
   }

   // 프로토콜 타입에 따라 바이트 배열 packet의 length가 다름
   public byte[] getPacket(int protocolType) {
      if (packet == null) {
         switch (protocolType) {
         case PT_UNDEFINED:
             packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA + LEN_MAXDATA];
             break;
         case PT_EXIT:
             packet = new byte[LEN_PROTOCOL_TYPE];
             break;
         case PT_REQ_WINDOW://
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_WINDOW_NUMBER];
             break;
         case PT_RES_WINDOW:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_FRAG + LEN_LAST + LEN_SEQNUM + LEN_DATA];
             break;
         case PT_REQ_SEARCHSCHOOL:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_SCHOOL];
        	 break;
         case PT_RES_SEARCHSCHOOL:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_REQ_LOGIN:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID + LEN_PASSWORD];
        	 break;
         case PT_RES_LOGIN:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE + LEN_ID + LEN_PASSWORD + LEN_NAME 
        	                   + LEN_NICKNAME + LEN_USERCODE + LEN_PHONENUM + LEN_SCHOOL + LEN_MAIL];
        	 break;
         case PT_REQ_CHECKDUPLICATE_ID:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
        	 break;
         case PT_RES_CHECKDUPLICATE_ID:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
        	 break;
         case PT_REQ_SIGNFORUSER:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_RES_SIGNFORUSER:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_CERTIFICATESTUDENT:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID + LEN_SCHOOL];
        	 break;
         case PT_RES_CERTIFICATESTUDENT:
        	 packet = new byte[LEN_PROTOCOL_TYPE+ LEN_CODE + LEN_CODE+ LEN_MAIL];
        	 break;
         case PT_REQ_CERTIFICATEMG:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
        	 break;
         case PT_RES_CERTIFICATEMG:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_CHANGEDATA:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID + LEN_PASSWORD + LEN_PHONENUM];
        	 break;
         case PT_RES_CHANGEDATA:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_SORTED_PCLIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_SCHOOL + LEN_CHOICE + LEN_CHOICE];
        	 break;
         case PT_RES_SORTED_PCLIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_REQ_PCAPPLY:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_RES_PCAPPLY:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_MYPCINFORM:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
        	 break;
         case PT_RES_MYPCINFORM:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_REQ_MYPC_RETURNDATE:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
        	 break;
         case PT_RES_MYPC_RETURNDATE:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_REQ_EXTENDAPPLY:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID + LEN_DATA];
        	 break;
         case PT_RES_EXTENDAPPLY:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_CANBORROW:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
        	 break;
         case PT_RES_CANBORROW:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
        	 break;
         case PT_REQ_CERTIFICATENUM:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID + LEN_MAIL];
        	 break;
         case PT_RES_CERTIFICATENUM:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_CERTIFICATENUM_CHECK:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_RES_CERTIFICATENUM_CHECK:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
        	 break;
         case PT_REQ_NOTELIST: 
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_RES_NOTELIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_REQ_NOTE: // 
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
        	 break;
         case PT_RES_NOTE:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA]; // 내용
        	 break;
         case PT_REQ_UPLOAD:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_RES_UPLOAD:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_DELETENOTE:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA]; // 번호
        	 break;
         case PT_RES_DELETENOTE:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_USERLIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
        	 break;
         case PT_RES_USERLIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_LAST + LEN_DATA];
        	 break;	 
         case PT_REQ_DROPUSER:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
        	 break;
         case PT_RES_DROPUSER:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;	 
         case PT_REQ_SCHOOLLIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_RES_SCHOOLLIST:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_LAST + LEN_DATA];
        	 break;	 
         case PT_REQ_SCHOOLDETAIL:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_RES_SCHOOLDETAIL:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_REQ_PCRETURN:
        	 packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
        	 break;
         case PT_RES_PCRETURN:
        	 packet = new byte[LEN_PROTOCOL_TYPE];
        	 break;
         case PT_REQ_CHANGEDATA_PAGE:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_PASSWORD];
				break;
		 case PT_RES_CHANGEDATA_PAGE:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE + LEN_PHONENUM];
				break;
		 case PT_REQ_REGISTERPC:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_RES_REGISTERPC:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
				break;
		 case PT_REQ_STUDENTLIST:
				packet = new byte[LEN_PROTOCOL_TYPE];
				break;
		 case PT_RES_STUDENTLIST:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_REQ_LENDERLIST:
				packet = new byte[LEN_PROTOCOL_TYPE];
				break;
		 case PT_RES_LENDERLIST:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_REQ_MAG_MYPAGE:
				packet = new byte[LEN_PROTOCOL_TYPE];
				break;
		 case PT_RES_MAG_MYPAGE:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_REQ_PCLIST:
				packet = new byte[LEN_PROTOCOL_TYPE];
				break;
		 case PT_RES_PCLIST:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_REQ_MODIFYPC:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_RES_MODIFYPC:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
				break;
		 case PT_REQ_DELETEPC:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_RES_DELETEPC:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
				break;
		 case PT_REQ_TARGET_PHONENUM:
				packet = new byte[LEN_PROTOCOL_TYPE];
				break;
		 case PT_RES_TARGET_PHONENUM:
				packet = new byte[LEN_PROTOCOL_TYPE + LEN_DATA];
				break;
		 case PT_REQ_CERTIFICATE_MANAGER:
			 packet = new byte[LEN_PROTOCOL_TYPE + LEN_ID];
				break;
		 case PT_RES_CERTIFICATE_MANAGER:
			 packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
				break;
         } // end switch
      } // end if
      packet[0] = (byte) protocolType; // packet 바이트 배열의 첫 번째 바이트에 프로토콜 타입 설정
      return packet;
   }
   
   public void setProtocolType(int protocolType) {
	      this.protocolType = protocolType;
	   }

	   public int getProtocolType() {
	      return protocolType;
	   }

	   public byte[] getPacket() {
	      return packet;
	   }

	   // Default 생성자로 생성한 후 Protocol 클래스의 packet 데이터를 바꾸기 위한 메서드
	   public void setPacket(int pt, byte[] buf) {
	      packet = null;
	      packet = getPacket(pt);
	      protocolType = pt;
	      System.arraycopy(buf, 0, packet, 0, packet.length);
	   }
	   
	   public void setWindowNum(String num) {
		   System.arraycopy(num.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, num.trim().getBytes().length);
		      packet[LEN_PROTOCOL_TYPE + num.trim().getBytes().length] = '\0';
	   }
	   
	   public String getWindowNum() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_WINDOW_NUMBER).trim();
	   } 
	   
	   public void setLogin(String login) {
		   System.arraycopy(login.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, login.trim().getBytes().length);
		      packet[LEN_PROTOCOL_TYPE + login.trim().getBytes().length] = '\0';
	   }
	   
	   public String getLogin() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_ID + LEN_PASSWORD).trim();
	   } 
	   
	   public void setCode(String code) {
		   System.arraycopy(code.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, code.trim().getBytes().length);
		      packet[LEN_PROTOCOL_TYPE + code.trim().getBytes().length] = '\0';
	   }
	   
	   public String getCode() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_CODE).trim();
	   } 
	   
	   
	   public void setUserInform(String user) {
		   System.arraycopy(user.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_CODE, user.trim().getBytes().length);
		      packet[LEN_PROTOCOL_TYPE + LEN_CODE + user.trim().getBytes().length] = '\0';
	   }
	   
	   public String getUserInform() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_CODE, LEN_ID + LEN_PASSWORD + LEN_NAME 
                   + LEN_NICKNAME + LEN_USERCODE + LEN_PHONENUM + LEN_SCHOOL + LEN_MAIL).trim();
	   } 

	   public void setData(String data) {
		   System.arraycopy(data.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, data.trim().getBytes().length);
		      packet[LEN_PROTOCOL_TYPE + data.trim().getBytes().length] = '\0';
	   }
	   
	   public String getData() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
	   }
	   public void setSearchschool(String sch) {
		   System.arraycopy(sch.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, sch.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + sch.trim().getBytes().length] = '\0';
	   }
	   
	   public String getSearchschool() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_SCHOOL).trim();
	   } 
	   
	   public void setSchools(String sch) {
		   System.arraycopy(sch.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, sch.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + sch.trim().getBytes().length] = '\0';
	   }
	   
	   public String getSchools() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
	   } 
	   
	   public void setId(String id) {
		   System.arraycopy(id.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, id.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + id.trim().getBytes().length] = '\0';
	   }
	   
	   public String getId() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_ID).trim();
	   } 
	   
	   public void setSchool(String sc) {
		   System.arraycopy(sc.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_ID, sc.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_ID + sc.trim().getBytes().length] = '\0';
	   }
	   
	   public String getSchool() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_ID, LEN_MAIL).trim();
	   } 
	   
	   public void setBorrowCode(String cd) {
		   System.arraycopy(cd.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_CODE, cd.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_CODE +  cd.trim().getBytes().length] = '\0';
	   }
	   
	   public String getBorrowCode() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_CODE, LEN_MAIL).trim();
	   } 
	   
	   public void setHidemail(String hm) {
		   System.arraycopy(hm.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_CODE+ LEN_CODE, hm.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_CODE +LEN_CODE+ hm.trim().getBytes().length] = '\0';
	   }
	   
	   public String getHidemail() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_CODE + LEN_CODE, LEN_MAIL).trim();
	   } 
	   
	   public void setPassword(String pw) {
		   System.arraycopy(pw.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_ID , pw.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE  + LEN_ID + pw.trim().getBytes().length] = '\0';
	   }
	   
	   public String getPassword() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_ID , LEN_PASSWORD).trim();
	   } 
	   
	   public void setPhoneNum(String pn) {
		   System.arraycopy(pn.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE  + LEN_ID + LEN_PASSWORD, pn.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_ID  + LEN_PASSWORD + pn.trim().getBytes().length] = '\0';
	   }
	   
	   public String getPhoneNum() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_ID + LEN_PASSWORD, LEN_PHONENUM).trim();
	   } 
	   
	   public void setPC_School(String sc) {
		   System.arraycopy(sc.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, sc.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + sc.trim().getBytes().length] = '\0';
	   }
	   public String getPC_School() {
		   return new String(packet, LEN_PROTOCOL_TYPE , LEN_SCHOOL).trim();
	   } 
	   
	   public void setFirstchoice(String c) {
		   System.arraycopy(c.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_SCHOOL, c.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_SCHOOL+ c.trim().getBytes().length] = '\0';
	   }
	   public String getFirstchoice() {
		   return new String(packet, LEN_PROTOCOL_TYPE+ LEN_SCHOOL  , LEN_CHOICE).trim();
	   } 
	   
	   public void setSecondchoice(String c) {
		   System.arraycopy(c.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE+ LEN_SCHOOL+ LEN_CHOICE, c.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE+ LEN_SCHOOL + LEN_CHOICE + c.trim().getBytes().length] = '\0';
	   }
	   public String getSecondchoice() {
		   return new String(packet, LEN_PROTOCOL_TYPE+ LEN_SCHOOL+ LEN_CHOICE, LEN_CHOICE).trim();
	   } 
	   public void setSortedpcList(String pc) {
		   System.arraycopy(pc.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, pc.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + pc.trim().getBytes().length] = '\0';
	   }
	   public String getSortedpcList() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
	   }
	   public void setPCApplyData(String pc) {
		   System.arraycopy(pc.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, pc.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + pc.trim().getBytes().length] = '\0';
	   }
	   public String getPCApplyData() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
	   }
	   public void setPCExtendDate(String pc) {
		   System.arraycopy(pc.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_ID, pc.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE+ LEN_ID + pc.trim().getBytes().length] = '\0';
	   }
	   public String getPCExtendDate() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_ID, LEN_DATA).trim();
	   }
	   public void setMail(String m) {
		   System.arraycopy(m.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_ID , m.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_ID+ m.trim().getBytes().length] = '\0';
	   }
	   public String getMail() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_ID , LEN_MAIL).trim();
	   }
	   
	   public void setUserlist(String d) {
		   System.arraycopy(d.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LAST , d.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_LAST + d.trim().getBytes().length] = '\0';
	   }
	   public String getUserlist() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_LAST , LEN_DATA).trim();
	   }
	   
	   public void setIslast(String d) {
		   System.arraycopy(d.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE , d.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE+ d.trim().getBytes().length] = '\0';
	   }
	   
	   public String getIslast() {
		   return new String(packet, LEN_PROTOCOL_TYPE, LEN_LAST ).trim();
	   }
	   
	   public void setSchoollist(String d) {
		   System.arraycopy(d.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LAST , d.trim().getBytes().length);
		    packet[LEN_PROTOCOL_TYPE + LEN_LAST + d.trim().getBytes().length] = '\0';
	   }
	   public String getSchoollist() {
		   return new String(packet, LEN_PROTOCOL_TYPE + LEN_LAST , LEN_DATA).trim();
	   }
	   public void setPCLenderList(String info) {
			System.arraycopy(info.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, info.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + info.trim().getBytes().length] = '\0';
		}
		
		public String getPCLenderList() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		public void setTargetPhoneNum(String info) {
			System.arraycopy(info.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, info.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + info.trim().getBytes().length] = '\0';
		}
		
		public String getTargetPhoneNum() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		public void setStudentList(String info) {
			System.arraycopy(info.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, info.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + info.trim().getBytes().length] = '\0';
		}
		
		public String getStudentList() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		public void setModifyPCInfo(String info) {
			System.arraycopy(info.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, info.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + info.trim().getBytes().length] = '\0';
		}
		
		public String getModifyPCInfo() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		public void setDeletePCInfo(String info) {
			System.arraycopy(info.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, info.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + info.trim().getBytes().length] = '\0';
		}
		
		public String getDeletePCInfo() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		
		public void setMyPage(String myPage) {
			//System.out.println("마이페이지 바이트 : " + myPage.trim().getBytes().length);
			System.arraycopy(myPage.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, myPage.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + myPage.trim().getBytes().length] = '\0';
		}
		
		public String getMyPage() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		public void setNoteList(String noteList) {
			System.arraycopy(noteList.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, noteList.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + noteList.trim().getBytes().length] = '\0';
		}
		
		public void setPasswordToIdentify(String pw) {
			System.arraycopy(pw.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, pw.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + pw.trim().getBytes().length] = '\0';
		}

		public String getPasswordToIdentify() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_PASSWORD).trim();
		}
		
		public void setExistingPhoneNum(String pw) {
			System.arraycopy(pw.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_CODE, pw.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + pw.trim().getBytes().length] = '\0';
		}

		public String getExistingPhoneNum() {
			return new String(packet, LEN_PROTOCOL_TYPE + LEN_CODE, LEN_PHONENUM).trim();
		}
		
		public void setDataAboutPC(String pw) {
			System.arraycopy(pw.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, pw.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + pw.trim().getBytes().length] = '\0';
		}

		public String getDataAboutPC() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
		
		public void setPCRegistrationInfo(String info) {
			System.arraycopy(info.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, info.trim().getBytes().length);
			packet[LEN_PROTOCOL_TYPE + info.trim().getBytes().length] = '\0';
		}

		public String getPCRegistrationInfo() {
			return new String(packet, LEN_PROTOCOL_TYPE, LEN_DATA).trim();
		}
}