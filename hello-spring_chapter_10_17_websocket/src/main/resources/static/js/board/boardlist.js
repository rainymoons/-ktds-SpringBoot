$().ready(function() {
	
	// 검색 버튼 0번 페이지로 이동
	$(".search-btn").on("click", function() {
		movePage(0);
	});
	
	// 검색타입이 맞지 않는 경우
	
	$(".search-type").on("change", function() {
		var type = $(this).val();
		
		var keywordDom = $(".search-keyword");
		if(type === "boardId") {
			keywordDom.attr("type", "number");
		} else if (type === "email") {
			keywordDom.attr("type", "email");
		} else {
			keywordDom.attr("type", "text");
		}
	});
	$(".search-type").change();
	
	
	$(".list-size").on("change", function() {
		// 현재 select된 항목의 값을 받아온다.
		//var listSize = $(this).val(); // select 태그의 이름 = listSize
		
		//location을 이용한 페이지 이동
		//location.href = "/board/list?pageNo=0&listSize=" + listSize;
		
		movePage(0); // 아래 코드는 필요없고 0번 페이지로 이동하는것만 남겨둠.
		
/*		
		// 전송하기전 페이지 no의 값을 0으로 바꾸고 전송해라.
		// 3번 페이지에서 40개 보기로 변경하면 1번 페이지로 바뀜.
		$(".page-no").val("0");
		// 클래스가 search-form을 가져와서 속성 정의
		$(".search-form").attr({
			// 호출하는 url이 /board/list 근데 이걸 잡고있는 것이 getMapping
			"method": "GET", 
			// 뒤의 파라미터는 붙이지 않는다.
			"action": "/board/list" 
		}).submit(); // 폼을 전송
		// listSize의 파라미터가 변경된다.
*/		
	});
});

// pageNo를 전달 받아서.
// 이건 공개가 된 fucntion -> jsp 에서 호출하기 위해 만듦.
function movePage(pageNo) {
	$(".page-no").val(pageNo);
	$(".search-form").attr({
		"method": "GET", 
		"action": "/board/list" 
	}).submit(); // 폼을 전송

}