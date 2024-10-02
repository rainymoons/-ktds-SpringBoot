$().ready(function () {
  $("#email").keyup(function () {
    // "/member/regist/available"에 파라미터를 전송한다.
    $.get(
      "/member/regist/available",
      { email: $(this).val() },
      function (availableResponse) {
        var email = availableResponse.email;
        var available = availableResponse.available;

        if (available) {
          // 사용할 수 있는 이메일이라면? $(this) 는 안됨. 이떄 this는 응답에 사용되었던 function에서 this를 쓰면 get이 나옴.
          $("#email").addClass("available");
          $("#email").removeClass("unusable");
          $("input[type=submit]").removeAttr("diabled"); // 버튼 비활성화
        } else {
          // 사용할 수 없는 이메일이라면?
          $("#email").addClass("unusable");
          $("#email").removeClass("available");
          $("input[type=submit]").attr("disabled", "disabled");
        }
        console.log(email);
        console.log(available);
      }
    );
  });
});
