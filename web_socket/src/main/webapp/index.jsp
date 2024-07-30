<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 추가하기</title>
</head>
<body>
	<textarea id="txt_msg" rows="3" placeholder="채팅 입력하기"></textarea>
	<input type="button" id="send_btn" value="보내기">
	<script>
		// 웹소켓 서버에 연결하기
		// ws : http 프로토콜 이용, wss: https 프로토콜 이용
		const ws = new WebSocket("ws://localhost:8089/<%=request.getContextPath()%>/chatting");
		// 서버 접속이 완료됐을 때 실행되는 함수 
		ws.onopen = function(e){
			console.log("서버 접속 완료!!");
		}
		// 서버로부터 받아온 데이터를 처리하는 핸들러
		ws.onmessage = function(e){
			if(e.data == 1){
				// 정상적으로 작업이 이루어졌을때 수행할 코드
				console.log("채팅 성공");
			} else{
				// 연결 실패했을 때 수행할 코드
				alert("채팅 실패");
			}
		}
	
		// 버튼을 클릭하면 양방향 통신 시작
		document.getElementById("send_btn").addEventListener('click',function(){
			// 1. 작성된 데이터 가져오기
			// 2. send 함수로 서버에 데이터 전송	
			const msg = document.getElementById("txt_msg").value;
			const sender = "김민수";
			const receiver = "이영희";
			const obj = {
				msg : msg,
				sender : sender,
				receiver : receiver
			};
			ws.send(JSON.stringify(obj));
		});
	</script>
</body>
</html>