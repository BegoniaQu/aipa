

var aipa_map;				//地图实例
var ap_create_marker = $("#create_marker");
var ap_clouddatalayer = "";
var AiPa = {
	//地图初始化
	init: function(){

		//高德地图API功能
		aipa_map = new AMap.Map("aipa_map",{
	        view: new AMap.View2D({
	        	zoom:12,
				lang: "zh_en",
				resizeEnable:true
	        })
		}); 
		
		//地图中添加地图插件
		/*aipa_map.plugin(["AMap.ToolBar","AMap.MapType"],function(){		
			toolBar = new AMap.ToolBar(); 
			aipa_map.addControl(toolBar);		
			toolBar.hideDirection();	
		});*/

		//添加点信息
		this.dataMake();
		//添加定义插件
		this.maptoolbar();
		//创建坐标按钮
		this.creatMake();
		//地理位置选择城市
		this.setCityVg();

		AMap.event.addListener(aipa_map, "zoomend", function() {
			AiPa.Jonila();
		});
		AMap.event.addListener(aipa_map, "dragend", function() {
			if(aipa_map.getZoom()>10){
				aipa_map.clearMap();
				AiPa.dataMake();
			}
		});


	},
	//自定义视野缩放
	maptoolbar: function(){
		var z = $(".zoom-in"),u = $(".zoom-out");
		z.on('click',function(){
			aipa_map.setZoom(aipa_map.getZoom()+1);
			zm();
		}),u.on('click',function(){
			aipa_map.setZoom(aipa_map.getZoom()-1);
			zm();
		})

		function zm(){
			var e = aipa_map.getZoom(),
			t = $(".zoom-in"),
			a = $(".zoom-out"),
			z = {
				max: 18,
				min: 3
			}
			e >= z.max ? t.addClass("disabled") : e <= z.min ? a.addClass("disabled") : (t.removeClass("disabled"), a.removeClass("disabled"))
		}
	},
	//云图数据
	cloudDataTemp:function(){
		aipa_map.plugin('AMap.CloudDataLayer', function () {
			var layerOptions = {clickable:true};
			ap_clouddatalayer = new AMap.CloudDataLayer("541fb6b5e4b08b446bc31b9f", layerOptions);
			ap_clouddatalayer.setMap(aipa_map); 

			AMap.event.addListener(ap_clouddatalayer, 'click', function (d) {
				//给坐标详细信息传值
				AiPa.markerVdetail(d.data);
			})
		})
	},
	Jonila: function(){
		
		if(aipa_map.getZoom()<=10){
			aipa_map.clearMap();
			AiPa.cloudDataTemp();
			console.log(1);
		}else{
			aipa_map.clearMap();
			AiPa.dataMake();
			if(ap_clouddatalayer){
				ap_clouddatalayer.setMap(null);
			}
			ap_clouddatalayer = null;
		}
	},
	//读取坐标数据
	dataMake: function(){
		$(".aipa-drop-wrap").fadeTo(0, 0).animate({
            opacity: 0,
            bottom: 0
        }, 500,function(){
        	$(this).hide();
        })

		var t, a, n, i, r = aipa_map.getBounds();
			a = r.getSouthWest(), i = r.getNorthEast(), t = a.getLng(), n = i.getLng(), a = a.getLat(), i = i.getLat(), 72.95 > t && (t = 72.95), n > 135.88 && (n = 135.88), i > 54.06 && (i = 54.06), 1.41 > a && (a = 1.41),
			u = 'http://yuntuapi.amap.com/'+'datasearch/'+'polygon?output=jsonp&limit=100&tableid=541fb6b5e4b08b446bc31b9f&polygon=' + r.getSouthWest() + ';' + r.getNorthEast() +'&key=192ad23a2f9b34977c1c363edb92ba80';

		//console.log(u);
		$.ajax({
	          type: 'GET',
	          url: u,
			  async: false,
			  dataType: "jsonp",
	          jsonpCallback:"callback",
	          success: function(e){

	          	if (e.status) {	
	          		var t = e.datas;
	          		//t 为空时显示为0
	          		$(".aipa-map-marker-number span").html(0);
	          		//将批量数据循环执行
	          		$.each(e.datas,function(i,par){
	          			AiPa.modelMarker(t[i]);
	          			$(".aipa-map-marker-number span").html(i>=99 ? "99+" : i+1);
	          		})
				}
	          }
	    });
	},
	//生成坐标
	modelMarker: function(t){
		/*console.log(t); 所有坐标的数据*/
		//console.log(t);
		var t = t,
			W = {
				male: new AMap.Pixel(-11, -17),
				female: new AMap.Pixel(-10, -27)
			},
			//将坐标经纬度分割
			s = t._location.split(","),
			g = new AMap.LngLat(s[0], s[1]),
			//自定义坐标样式图层
			a = "<div class='q marker q"+ t.sex  + "' data-id='"+t._id+"'><span></span></div>",
			//生成坐标源
			o = new AMap.Marker({
					map: aipa_map,
					content: a,
					//offset: W.male,
					position: g
			});
			//绑定给marker数据  o.id 可自定义
			o.id= t._id;  
			o.comment = t.comment;
			o.orientation = t.orientation;
			
			//将坐标加载到页面上
			o.setMap(aipa_map); 

			//给坐标绑定点击事件
			AMap.event.addListener(o, "click", function() {  
				//下方弹出
				AiPa.markerVtemp(this.id,this.comment,this.orientation,this.position);
				infoWindow.open(aipa_map, g);

				//给坐标详细信息传值
				AiPa.markerVdetail(t);
			})

			var infoWindow = new AMap.InfoWindow({
		        isCustom: true,  //使用自定义窗体
		        content: "<div class='ghj gmk"+t._id+"'><span class='ap-desc-aeo org_"+o.orientation+"'></span>"+((o.comment==null || o.comment=='') ? "标记人似乎没说什么" : "<span class='ap-desc-cmt'>" + o.comment )+"</span></div>",
		        offset: new AMap.Pixel(0, -40)
		    });
	},
	//地理位置，选择城市定位
	setCityVg: function(){
		var cbox = $(".choise_city_box"),
			ct   = $("#CityList"),
			cput = $(".C_commonInput");
			cmr  = $(".city_list_more .lmr")
		//文本框触发显示城市列表
		cput.on('click',function(){
			cbox.show();
			if($(".sharebg").length>0){
				$(".sharebg").addClass("sharebg-active");
			}else{
				$("body").append('<div class="sharebg"></div>');
				$(".sharebg").addClass("sharebg-active");
			}
		})

		//关闭按钮
		cmr.on('click',function(){
			cbox.hide();
			setTimeout(function(){
				$(".sharebg-active").removeClass("sharebg-active");	
				$(".sharebg").remove();	
			},100);
		})

		//城市选择
		ct.find("li").on('click',function(){
			var cityName = $(this).html();
			setTimeout(function(){
				$(".sharebg-active").removeClass("sharebg-active");	
				$(".sharebg").remove();	
			},100);
			cbox.hide();

			aipa_map.clearMap();
			aipa_map.setCity(cityName);
		})
	},
	//坐标信息简介
	markerVtemp: function(id,comment,orientation){  //下方弹出
		var ap_dash = $(".aipa-drop-wrap"),
			ap_comment = ap_dash.find(".ap-des-act");
			ap_orientation = ap_dash.find(".ap-desc-aeo");
        	
        ap_comment.html(comment==null || comment=="" ? "标记人似乎没说什么" : comment);
        ap_orientation.attr("class","ap-desc-aeo org_"+orientation);
        /*ap_dash.fadeTo().animate({
            opacity: 1,
            bottom: 0
        }, 500,function(){
        	$(this).show();
        })*/
        $(".aipa_tools .zooms").css('bottom','110px');
	},
	//坐标详细信息
	markerVdetail: function(d){
		//console.log(d);
		var x = $("#mk_detail_info");
		var m = ["male","female"];
		var or = ["男女","男男","女女"];
		var n = "";
		var l = ["家","酒店","郊外","汽车","船","其他"];
		var time = ["15分钟","30分钟","1小时","2小时","更多"];

		if(d.position.length == 0){
			n = "<span class='fg-span'>无</span>";
		}else{
			for (t = 0, a = d.position.split(","), n = ""; t < a.length; t++) {
				n = n + '<span class="fh-span mlove-ico-'+a[t]+'"><i></i></span>';
			}
		}

		x.find(".mkd-sex")["male" == m[d.sex] ? "removeClass" : "addClass"]("female");
		x.find(".mkd-xtail")["male" == m[d.sex] ? "removeClass" : "addClass"]("female");

		x.find(".mif_comment").html(d.comment.length == 0 ? "TA没有感受～" : d.comment),
		x.find(".mif_time").html(d._createtime);
		x.find(".mif_orientation").html(or[d.orientation]);
		x.find(".mif_place").html(d.place.length == 0 ? "<span class='fg-span'>无</span>" : "<span class='fg-span'>" + l[d.place] + "</span>");
		x.find(".mif_inother").html(d.inother.length == 0 ? "<span class='fg-span'>没使用</span>" : "<span class='fg-span'>使用了</span>");
		
		console.log(111);
		//面板: 体位
		d.position.length == 0 ? x.find(".mif_position").hide() : x.find(".mif_position").show(),x.find(".mif_position .details").html(n);
		//面板：做爱时间
		d.lovetime.length == 0 ? x.find(".mif_lovetime").hide() : x.find(".mif_lovetime").show(),x.find(".mif_lovetime .details").html("<span class='fg-span'>" + time[d.lovetime] + "</span>");


		$(".aipa-marker-detail-info").addClass("am-modal-active");	
		if($(".sharebg").length>0){
			$(".sharebg").addClass("sharebg-active");
		}else{
			$("body").append('<div class="sharebg"></div>');
			$(".sharebg").addClass("sharebg-active");
		}

		$(".mkd-top-close,.mkd-dm").on('click',function(){
			$(".aipa-marker-detail-info").removeClass("am-modal-active");	
			setTimeout(function(){
				$(".sharebg-active").removeClass("sharebg-active");	
				$(".sharebg").remove();	
			},300);
		})

	},
	//创建坐标按钮
	creatMake: function(){
		var go_btn = $(".create_marker");
		
		go_btn.on('click',function(){
			AiPa.crtBoxtemp();
			$(".go_form").addClass("md-show");
			AiPa.crtBoxanm();
			aipa_map.clearInfoWindow();
		})
	},
	//初始化创建信息操作方法
	crtBoxanm:function(){
		var y = $(".go_list_ui");
		y.find("li").on("click", function(t) {
			var a = "selected",
				n = ".selected",
				o = $(this);
			o && (/mkpos-0/.test(o[0].className) ? o.hasClass("nothing") ? (setTimeout(function() {
				o.parent().find(n).removeClass(a)
			}, 10), setTimeout(function() {
				o.addClass(a)
			}, 30)) : (o.siblings(".nothing").removeClass(a), setTimeout(function() {
				o.hasClass(a) ? o.removeClass(a) : o.addClass(a)
			}, 10), setTimeout(function() {
				0 == o.parent().find(n).length && o.addClass(a)
			}, 30)) : (setTimeout(function() {
				o.parent().find(n).removeClass(a)
			}, 10), setTimeout(function() {
				o.addClass(a)
			}, 30)), t.stopPropagation(), t.preventDefault())
		})		

		$(".gmk_position li").on("click", function() {
			$(".item_position ").hide();
			$(".item_position li").removeClass("selected");
			$(".postion_ico_"+$(this).index()).show();
		})

		var crt_btn = $(".go_btn .create");
		var close_btn = $(".go_btn .close");

		crt_btn.on('click',function(){
			AiPa.crtdata();
			$(".create_marker_info").remove();
		})

		close_btn.on('click',function(){
			$(".create_marker_info").remove();
		})

	},
	//生成录入坐标信息面板
	crtBoxtemp: function(){
		var shtml = '<!-- 创建标记信息 -->'+
					'<div class="create_marker_info">'+
					'<div class="go_form" id="goForm">'+
					'	<div class="go_item_wrap">'+
					'		<div class="go_item clearfix">'+
					'			<h4>您的性别</h4>'+
					'			<div class="go_list_ui item_sex clearfix">'+
					'				<ul>'+
					'					<li class="selected" data-val="0">男</li>'+
					'					<li data-val="1">女</li>'+
					'				</ul>'+
					'			</div>'+
					'		</div>'+
					'		<div class="go_item item_orientation clearfix">'+
					'			<h4>性取向</h4>'+
					'			<div class="go_list_ui clearfix">'+
					'				<ul class="gmk_position">'+
					'					<li data-val="0" class="selected">男女</li>'+
					'					<li data-val="1" >男男</li>'+
					'					<li data-val="2" >女女</li>'+
					'				</ul>'+
					'			</div>'+
					'		</div>'+
					'		<div class="go_item clearfix">'+
					'			<h4>姿势(多选)</h4>'+
					'			<div class="postion_ico_0 item_position go_list_ui clearfix">'+
					'				<ul>'+
					'					<li class="mkpos-0" data-val="0"><span class="mlove-ico mlove-ico-0"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="1"><span class="mlove-ico mlove-ico-1"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="2"><span class="mlove-ico mlove-ico-2"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="3"><span class="mlove-ico mlove-ico-3"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="4"><span class="mlove-ico mlove-ico-4"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="5"><span class="mlove-ico mlove-ico-5"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="6"><span class="mlove-ico mlove-ico-6"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="7"><span class="mlove-ico mlove-ico-7"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="8"><span class="mlove-ico mlove-ico-8"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="9"><span class="mlove-ico mlove-ico-9"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="10"><span class="mlove-ico mlove-ico-10"><i></i></span></li>'+
					'				</ul>'+
					'			</div>'+
					'			<div class="postion_ico_1 item_position go_list_ui clearfix" style="display:none;">'+
					'				<ul>'+
					'					<li class="mkpos-0" data-val="11"><span class="mlove-ico mlove-ico-11"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="12"><span class="mlove-ico mlove-ico-12"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="13"><span class="mlove-ico mlove-ico-13"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="14"><span class="mlove-ico mlove-ico-14"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="15"><span class="mlove-ico mlove-ico-15"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="16"><span class="mlove-ico mlove-ico-16"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="17"><span class="mlove-ico mlove-ico-17"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="18"><span class="mlove-ico mlove-ico-18"><i></i></span></li>'+
					'				</ul>'+
					'			</div>'+
					'			<div class="postion_ico_2 item_position go_list_ui clearfix" style="display:none;">'+
					'				<ul>'+
					'					<li class="mkpos-0" data-val="19"><span class="mlove-ico mlove-ico-19"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="20"><span class="mlove-ico mlove-ico-20"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="21"><span class="mlove-ico mlove-ico-21"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="22"><span class="mlove-ico mlove-ico-22"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="23"><span class="mlove-ico mlove-ico-23"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="24"><span class="mlove-ico mlove-ico-24"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="25"><span class="mlove-ico mlove-ico-25"><i></i></span></li>'+
					'					<li class="mkpos-0" data-val="26"><span class="mlove-ico mlove-ico-26"><i></i></span></li>'+
					'				</ul>'+
					'			</div>'+
					'		</div>'+
					'		<div class="go_item clearfix">'+
					'			<h4>地点</h4>'+
					'			<div class="go_list_ui item_place clearfix">'+
					'				<ul>'+
					'					<li data-val="0" class="selected">家</li>'+
					'					<li data-val="1">酒店</li>'+
					'					<li data-val="2">郊外</li>'+
					'					<li data-val="3">汽车</li>'+
					'					<li data-val="4">船</li>'+
					'					<li data-val="5">其他</li>'+
					'				</ul>'+
					'			</div>'+
					'		</div>'+
					'		<div class="go_item clearfix">'+
					'			<h4>做爱时间</h4>'+
					'			<div class="go_list_ui go_time_ui item_lovetime clearfix">'+
					'				<ul>'+
					'					<li data-val="0">15分钟</li>'+
					'					<li data-val="1">30分钟</li>'+
					'					<li data-val="2">1小时</li>'+
					'					<li data-val="3">2小时</li>'+
					'					<li data-val="4">更多</li>'+		
					'				</ul>'+
					'			</div>'+
					'		</div>'+
					'		<div class="go_item clearfix">'+
					'			<h4>是否使用道具</h4>'+
					'			<div class="go_list_ui go_time_ui item_inother clearfix">'+
					'				<ul>'+
					'					<li data-val="0" class="selected">没使用</li>'+
					'					<li data-val="1">使用了</li>'+	
					'				</ul>'+
					'			</div>'+
					'		</div>'+
					'		<div class="go_item clearfix">'+
					'			<h4>说点啥</h4>'+
					'			<textarea id="comment" class="go_textarea"  placeholder="说说当时的感受"></textarea>'+
					'		</div>'+
					'	</div>'+
					'	<div class="go_btn">'+
					'		<a href="javascript:;" class="close">关闭</a>'+
					'		<a href="javascript:;" class="create" id="createMaker">确定标记</a>'+
					'	</div>'+
					'</div>'+
					'</div>'
		return $(".aipa-map-container").append(shtml);
	},
	//录入坐标信息并创建
	crtdata:function(){

		var _url = 'http://yuntuapi.amap.com/datamanage/data/create?',
			_mpcenter = aipa_map.getCenter().toString(),
			fm_sex = $(".item_sex .selected").attr("data-val"),
			fm_orientation = $(".item_orientation .selected").attr("data-val"),
			_k = [],
			fm_position = [],
			fm_place= $(".item_place .selected").attr("data-val"),
			fm_lovetime = $(".item_lovetime .selected").attr("data-val"),
			fm_inother = $(".item_inother .selected").attr("data-val"),
			fm_comment = $("#comment").val();

			$(".item_position .selected").each(function(i){
				_k.push($(this).attr("data-val"));
			})
			fm_position = _k.join(",")
			var _data = {
					_name:"爱啪标记",
					_location:_mpcenter,
					sex:fm_sex,
					orientation:fm_orientation, 
					position:fm_position,
					place:fm_place,
					lovetime:fm_lovetime,
					inother:fm_inother,
					comment:fm_comment
				}
		$.ajax({
			url:_url,
			type:"GET",
			dataType:"jsonp",
			data: {
				key:"192ad23a2f9b34977c1c363edb92ba80",
				tableid:"541fb6b5e4b08b446bc31b9f",
				data:JSON.stringify(_data)
			},
			success: function(){
				//console.log(_mpcenter);
				//AiPa.dataMake();
				var s = _mpcenter.split(",");
				var g = new AMap.LngLat(s[0], s[1]);
				var a = "<div class='q marker q"+fm_sex+"'><span></span></div>";
				var o = new AMap.Marker({
						map: aipa_map,
						content: a,
						position: g
				});

				//跳动
				o.setAnimation('AMAP_ANIMATION_DROP');

				//将坐标加载到页面上
				o.setMap(aipa_map); 
			
			},
			error: function (x, t, e) {
				alert('请求发生错误，请稍后重试！');
			}
		});

	},
	aipaBesk : {
		t : "541fb6b5e4b08b446bc31b9f",
		k : "192ad"+"23"+"a2f9b349"+"77"+"c1c363edb92ba80",
		winPct : 0,
		healthy : 0
	}
}
AiPa.init();