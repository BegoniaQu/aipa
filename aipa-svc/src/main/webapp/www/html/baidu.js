

var aipa_map;				//地图实例
var INIT_MAP_POINT = null;	//初始化地图时的中心点


var AiPa = {
	init: function(){

		//地图加载零碎的文件结束(可简单地理解为地图移动结束)后会触发tilesloaded事件
	    var mapMoveEnd = function(){ 
	    	aipa_map.removeEventListener('dragend',sortList);
	    	aipa_map.addEventListener('dragend',sortList);
	    	aipa_map.removeEventListener('tilesloaded',mapMoveEnd);	
	    }


		// 百度地图API功能
		INIT_MAP_POINT = new BMap.Point(121.476753,31.224349);
		aipa_map = new BMap.Map("aipa_map",{		
 	        enableMapClick:false
 	        //minZoom:9
 	    });
		
		aipa_map.centerAndZoom(INIT_MAP_POINT,13); 	    

		/*
		//卫星
 	    aipa_map.addControl(
 	        new BMap.MapTypeControl({
 	            type:BMAP_MAPTYPE_CONTROL_MAP
 	    })); 
 	    //视野级别
 	    aipa_map.addControl(
 			new BMap.NavigationControl({
 				offset:new BMap.Size(10,20)
 			}));
 		//右下角
 	    aipa_map.addControl(
 			new BMap.OverviewMapControl()
 	    );*/
 	    var panoramaControl=new BMap.PanoramaControl({
 	    	offset:new BMap.Size(13,70)
 	    });
 	    //aipa_map.addControl(panoramaControl); //全景
 	    aipa_map.enableScrollWheelZoom(); 	
 	    //map.addEventListener('tilesloaded',mapMoveEnd);
	

		//添加点信息
		this.creat_make();

	},
	creat_make: function(){
		$.ajax({
	          type: 'GET',
	          url: 'http://yuntuapi.amap.com/datamanage/data/list?',
	          async: false,
	          dataType: "jsonp",
			  data:{tableid:"568237a77bbf19793a918eb5",limit:100,page:1,key:"192ad23a2f9b34977c1c363edb92ba80"},
	          jsonp: "callback",
	          jsonpCallback:"callback",
	          success: function(data){
	          	
				  $.each(data.datas,function(i,par){
				  	AiPa.model_marker(par,i);
				  })
				  //addCluster(0);
	          }
	    });
	},
	model_marker: function(data,i){
		var markerClickHandler = function(){
			console.log(this.count);
		}
		var fly  = function( $elem ){
			var wrapper = $elem.parent();
	    	var key = 'zIndex';
	    	wrapper.data(key,wrapper.css(key));
	    	wrapper.css(key,0);
		};
		var fall = function( $elem ){
			var wrapper = $elem.parent();
	    	var key = 'zIndex';
	    	wrapper.css(key,wrapper.data(key));
		}
	    var markerMouseOverHandler = function(){ 
			fly( $('.marker'+this.id) );
	    };	
		
	    var markerMouseOutHandler = function(){
	    	fall( $('.marker'+this.id) );
	    };

		var mk_data = data;
		var mk_temp = "<div class='q marker q"+mk_data.orientation+" marker"+mk_data._id+"' >"
	                +      "<span></span>"
				    + "</div>";
				   // console.log(mk_data.orientation);
      	var marker = new BMapLib.RichMarker(mk_temp,new BMap.Point(mk_data._location.split(",")[0],mk_data._location.split(",")[1]),{"anchor" : new BMap.Size(-47, -116)});
			marker.id = mk_data._id;
			marker.count = mk_data.comment;
			aipa_map.addOverlay(marker);
			console.log(mk_data.comment);
			marker.addEventListener('click', markerClickHandler);
            marker.addEventListener('mouseover', markerMouseOverHandler);
            marker.addEventListener('mouseout', markerMouseOutHandler);
            

	}
}
AiPa.init();