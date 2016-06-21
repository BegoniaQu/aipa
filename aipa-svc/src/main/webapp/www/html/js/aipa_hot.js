//热门数据榜  aipa_hot.js
var AiPa_hot = {
	init: function(){
		var $hot_btn = $("#aipa_hot_btn");
		var $hot_dowm_btn = $(".hot-drop-down");
		$hot_btn.on('click',function(){
			$(".hot-modle").show();
		})

		$hot_dowm_btn.on('click',function(){
			$(".hot-modle").hide();
		})

		/*微信公众号关注*/
		$(".wx_icon_shadow").on('click',function(){
			$(".wx-popup-container").show();
		})

		$(".wx-popup-container").on('click',function(){
			$(".wx-popup-container").hide();
		})

	}
}
AiPa_hot.init();
		