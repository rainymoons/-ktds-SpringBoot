$().ready(function () {
  // 현재 웹 페이지의 url 정보를 가져온다.  호스트 이름과 포트 번호 이후의 경로 =  /board/list (localhost:8080은 빠진다.)
  var pathname = location.pathname;
  // 현재 url의 쿼리 파라미터를 가져와서 search에 저장. login?user=123 이면 search는 ?user=123
  // pathname 이후의쿼리파라미터.
  var search = location.search;
  // 다음으로 보여줄 Url 변수 선언
  var nextUrl = "";
  
  // 만약 현재 경로가 member/login이면 로그인을 하려는 것.
  if (pathname === "/member/login") {
	// 다음으로 보여줄 url은 /board/list. 
    nextUrl = "/board/list";
  } else {
	// 현재 페이지가 로그인 페이지가 아니면 -> 로그인 후에 현재 페이지로 돌아 갈 수 있도록 설정.
	// 장바구니에 물품 담다가 로그인 페이지 떴는데 다 날아가면 다시 안온다.
    nextUrl = pathname + search;
  }
  // <input type="hidden" name="nextUrl" /> 이놈의 nextUrl을 설정해주는 것.
  $("input[name=nextUrl]").val(nextUrl);
});
