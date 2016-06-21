/* 
   aipashequ.com
   by  YuRui
   user.js
 */

var user = {};

user.login = function(obj, callback){
	user.ajaxCenter( '','GET',{page:1}, alert(1),2);
};	
user.reg   = function(obj, callback){

};
user.ajaxCenter = function(url, type, data, success, failure) {
	$.ajax({
		'url' : url,
		'type' : type || 'GET',
		'data' : data,
		'dataType' : 'json',
		'success' : function(d) {
			if (d && d.code == 200) {
				success && success(d);
			} else {
				success && success(d);
			}
		},
		'error' : function() {
			success && success('请求失败');
		}
	});
}

