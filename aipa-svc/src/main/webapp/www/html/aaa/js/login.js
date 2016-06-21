
var aipa_login = {
	init : function(){
		var y_go = $("#login_go");
		y_go.on('click',function(){
			aipa_login.login();
		})
	},
	login: function(){
		var y_username = $("#username").val();
		var y_password = $("#password").val();
		$.ajax({
			type:"POST",
			url:"http://120.26.45.193:8090/aipa-svc/sign/signIn.ap?appplt=web&appver=1.0",
			data:"username=shquyang&pwd=qy1517",
			success:function(data){
				console.log(data);
			}
		})
	}
}
aipa_login.init();
