package com.gn.chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint(value="/chatting")
public class ChattingServer {
	
	@OnOpen
	public void handleOpen(Session session, EndpointConfig config) {
		System.out.println("클라이언트 접속");	
	}
	
	@OnMessage
	public int handleMessage(String json) {
		// 문자열 형태로 받아온 데이터를 JSON 형태로 변환
		JSONParser parser = new JSONParser();    
		// 데이터베이스 연결
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			// JSON 데이터 파싱
			JSONObject obj = (JSONObject) parser.parse(json);
			String msg = (String)obj.get("msg");
			String sender = (String)obj.get("sender");
			String receiver = (String)obj.get("receiver");
			
			// DB에 파싱한 데이터 추가
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://127.0.0.1:3306/chat";
			String user = "scott";
			String pw = "tiger";
			conn = DriverManager.getConnection(url, user, pw);
			String sql = "INSERT INTO chatting_tbl (chat_text,send_user,receive_user)"
					+ "VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, msg);
			pstmt.setString(2, sender);
			pstmt.setString(3, receiver);
			// 결과 받아오기
			result = pstmt.executeUpdate();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@OnError
	public void handleError(Throwable t) {
		System.out.println("에러확인");
	}
	
}