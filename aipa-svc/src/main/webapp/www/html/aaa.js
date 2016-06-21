define( [ 'jquery','roomu','template','rc','pagination','underscore','jqueryui','typeahead'],
function(	$,		U,		template,  rc,	pagination,	_){ 
	
	/**
	 *  custom event(自定义事件)有 filter, refresh, refreshed, draw
	 *  filter   表示执行搜房条件的过滤
	 *  refresh  表示刷新左边表格和右边地图
	 *  refreshed表示刷新结束，图片裁切功能需要监听该行为
	 */
	
	var map;					//地图实例
	var INIT_MAP_POINT = null;	//初始化地图时的中心点
	var NULL = {				//一个空对象，用于刷新URL
		searchWord:null,		//搜索关键字
		communityId:null,		//小区编号
		areaId:null,			//商圈编号
		stationId:null,			//地铁站编号
		districtId:null,		//行政区编号
		order:null,				//排序方式的枚举值
		roomId:null				//房源编号
	};
	var ROOT;					//图片URL的根目录，接口的前缀

    // 从一个数组中裁剪掉一项 ,返回裁剪后的数组
    var splice = function( arr, key ){
    	var index = arr.indexOf(key); 
    	if( index!= -1){
    		arr.splice(index,1);
    	}
    	return arr;
    };
    
    // 向查询面板的已选结果中添加一项 仅用作添加搜索字段和小区字段
    var addFilter = function(data){
		if( !_.isObject(data) ){
			return;
		}
		if( data.name ){
			data.name = $.trim(data.name);
		}
		
    	var $context = $('.selectParams');    	
		var $item 	 = $context.find('.item[type="'+data.type+'"]');
        var frag 	 = template('filterTmpl',data);			
        var hasCurr = $.trim(  $item.find('span').text() ) == data.name ;  
		
		$item.remove();
        if( !data.toggle || !hasCurr ){     
			$context.append( frag );
        }        
        return hasCurr;
    };		
	
	


    //清除找房过滤面板中所有已选的选项 (但这并不包括清除URL中的信息) 重置地图到初始化的中心点，清空地图中的地铁线
    var resetFilter = function(){
    	$('.panel-filter')
    		.find('.list .active,.mock-radio.active').removeClass('active').end()
			.find('.list .on').removeClass('on').end()
			.find('.fee-range.changed').removeClass('changed').text(feeRange.join('-')+'+').end()
			.find('.drop-menu.changed').each(function(){
				var name = $('.default',this).data('default');
				$('.holder',this).text(name).removeAttr('code');
				$(this).removeClass('changed');
			}).end()
			.find('.sorting-list')
				.find('.by-price,.by-area').removeClass('active').end()
				.find('a').eq(0).addClass('active').end().end()
			.end();
		slider.slider( "values", feeRange );	
		$('.selectParams').empty();
		map.panTo( INIT_MAP_POINT );
		map.removeOverlay(subwayOverlay);
		subwayOverlay = null;
		$('.content-wrapper').scrollTop(0);
    };		
    
    
    // 页面加载时读取location.pathname 将搜索面板中对应项重新选中
    var pending = function(){
    	var codes = [];
    	var reg = /room\/([^?#]*)/;
    	var path = '';
    	var context = null;
		var drawAnchor = null;
    	if( reg.test(location.pathname)){
    		path = reg.exec(location.pathname)[1];
    		_(path.split('/')).each(function(it){
    			if(it == ''){
    				return;
    			}
    			if(/\w+\-\w+/.test(it)){
    				_(it.split('-')).each(function(vo){
    					codes.push(vo);
    				});
    			}else{
    				codes.push(it);
    			}
    		});
    		context = $('.filter-panel');
    		_(codes).each(function(code){
    			var it = context.find('[code="'+code+'"]');
    			var priceReg = /jg(\d+),(\d+)/
    			var range =[];
    			var curr = $('[code="'+code+'"]');
    			if(priceReg.test(code) ){	//如果是滑块
    				range.push( priceReg.exec(code)[1] );
    				range.push( priceReg.exec(code)[2] );
    				var suffix = range[1]== feeRange[1] ? '+' : '元';
    				slider.slider( "values", range ).siblings('.fee-range').addClass('changed').text(range.join('-')+suffix).attr('code',code);
    			}else if(curr.closest('.drop-menu').length == 1){	//如果是下拉菜单
    				curr.trigger('click',true);	//触发单击事件的同时，告诉click事件的handler，prevent为true，意思是阻止接下来的业务行为
    			}else if( curr.parent().is('.item') ){	//是区域栏
    				curr.addClass('active');
					drawAnchor = curr;
    			}else if(curr.closest('.row-expand').length==1){	//是商圈
    				curr.addClass('active').closest('.item').children('.active').removeClass('active').addClass('on');   
					drawAnchor = curr;
    			}else{	    				
    				curr.addClass('active');
					if(curr.closest('.subway-list').length==1){	//是地铁
    					curr.trigger('draw');
    				}
    			}
    		});
			if(drawAnchor!=null){
				drawAnchor.trigger('draw');
			}
    		
    	}
    	var q = U.deserializeURL();	//q内部有格式，需要将英文逗号再替换成空格，被英文逗号分隔的每一项需要 decodeURIComponent
    	var keywordArr = null;
    	if(q.searchWord!=null){	//如果URL中有查询信息，将该信息同步到顶部输入框中
    		keywordArr =  _(q.searchWord.split(',')).map(function(it){
				return decodeURIComponent(it);
			});
    		$('.my-search').val(keywordArr.join(' '));
    		addFilter({
				name:keywordArr.join('，'),
				type:'searchWord'
			});
    	}
		var $anchor = null;
		if(q.districtId!=null){
			$('.region-list a[data-id="'+q.districtId.replace(/[\[\]]/g,'')+'"]').addClass('active');
		}
		if(q.areaId!=null){
			$('.region-list a[data-id="'+q.areaId+'"]').closest('.item').children('a').removeClass('active').addClass('on').end().end().addClass('active');
		}
    	var orderMap = {
    			1:{
    				anchor:'.by-price',
    				fix:'fa-rotate-180'
    			},
    			2:{
    				anchor:'.by-price'
    			},
    			3:{
    				anchor:'.by-area',
    				fix:'fa-rotate-180'
    			},
    			4:{
    				anchor:'.by-area'
    			}
    	};
    	var sp = null;
    	if(q.order!=null&&q.order!=0){	//如果URL中有排序信息，同步排序信息到界面中
    		sp = orderMap[q.order];
    		$(sp.anchor).closest('.row-body').find('.active').removeClass('active');
			$(sp.anchor).addClass('active');
    		if(sp.fix!=null){
    			$(sp.anchor).find('i').addClass(sp.fix);
    		}
    	}
    	$(document).trigger('filter',{	//刷新条件面板
    		prevent:true,		//是否阻止接下来的刷新列表和刷新地图
		    newInteractive:false	//表示这不是一次新的交互，新的交互将清除URL中的page信息等
    	});
    };
    
    // 根据地图的中心点排序左边列表
    var sortList = function(){
		if( getSortEnum() != 0 ){
			return;
		}
		var center = map.getCenter();
		var url = U.mixURL({
		   search:{
			   lng:center.lng,
			   lat:center.lat
		   },
		   pathname:ROOT+'mapMove/'+buildPath().join('/')
		});
		rc.addAjaxLoader();
		$.post(url,function(data){
			refreshList.call(this,data);
			rc.removeAjaxLoader();
		},'json');
		
		return false;
    };    
	
	var sortListByPoint = function(point){
		var url = U.mixURL({
		   search:{
			   lng:point.lng,
			   lat:point.lat
		   },
		   pathname:ROOT+'mapMove/'+buildPath().join('/')
		});
		rc.addAjaxLoader();
		$.post(url,function(data){
			refreshList.call(this,data);
			rc.removeAjaxLoader();
		},'json');
	}
	

	
    //地图加载零碎的文件结束(可简单地理解为地图移动结束)后会触发tilesloaded事件
    var mapMoveEnd = _.throttle(function(){ 
    	map.removeEventListener('dragend',sortList);
    	map.addEventListener('dragend',sortList);
    	map.removeEventListener('tilesloaded',mapMoveEnd);	
    },600);
    
  
    var buildMap = function(id,lng,lat){
    	INIT_MAP_POINT = new BMap.Point(lng,lat);
    	map = new BMap.Map(id,{		//开始构建地图
 	        enableMapClick:false,
 	        minZoom:9
 	    });
 	    map.centerAndZoom(INIT_MAP_POINT,13); 	    
 	    map.addControl(
 	        new BMap.MapTypeControl({
 	            type:BMAP_MAPTYPE_CONTROL_MAP
 	        }));
 	    map.addControl(
 			new BMap.NavigationControl({
 				offset:new BMap.Size(10,20)
 			}));
 	    map.addControl(
 			new BMap.OverviewMapControl()
 	    );
 	    var panoramaControl=new BMap.PanoramaControl({
 	    	offset:new BMap.Size(13,70)
 	    });
 	    map.addControl(panoramaControl);
 	    map.enableScrollWheelZoom(); 	
 	    map.addEventListener('tilesloaded',mapMoveEnd);	//地图移动结束后会触发 tilesloaded 事件
    };
	
	var mapCenterMarker = null;
    var refreshMapCenterMarker = _.throttle(function( point ){
    	if(mapCenterMarker==null){
			$('.marker.bounce').removeClass('animated infinite bounce');
    		mapCenterMarker= new BMapLib.RichMarker('<img class="map-center-marker animated infinite bounce" src="'+ROOT+'static/images/point3.png">',point);
    		map.addOverlay(mapCenterMarker); 
    		mapCenterMarker.enableDragging();
			mapCenterMarker.ondragend = function(){
				var point = mapCenterMarker.getPosition();
				map.panTo(point);
				sortListByPoint(point);
			};
    		mapCenterMarker.setPosition(point);
    	//	$('.map-center-marker').parent().css('zIndex',-100);
    		mapCenterMarker.setAnchor(new BMap.Size(5,-16) );  
    	}else{
    		mapCenterMarker.setPosition(point);
    	}
	
    },400);
    
	       
	var feeRange = [ 0, 20000 ];	//房租的默认范围
    var slider = $( "#paramPrice" ).slider({		//升级为滑块组件
        range: true,
        min: 0,
        max: 20000,
		step:100,
        values: feeRange,
        slide: function( event, ui ) {
			var least = ui.values[0];
			var limit = ui.values[1];
			var suffix = ui.values[1]== feeRange[1] ? '+' : '元';
			var code = 'jg'+ui.values.join(',');
        	$(this).siblings(".fee-range").addClass('changed').html(ui.values.join('-')+suffix).removeAttr('code').attr('code',code);
        },
        change:function( event ,ui ){
        	if( !$(this).is('.mui') && $(this).siblings('.fee-range').is('.changed') ){
        		$(document).trigger('filter',{
     			    newInteractive:true
        		});
        	}        	
        }
    });	
	
	
	window._slider = slider;
    
    $(document).on('click','.drop-menu ul a',function(e,prevent){
    	e.preventDefault();
    	var link = $(this);
    	var text = link.text();
		var type;
    	if( link.is('.default') ){
    		text = link.data('default');
			type = $(this).closest('.drop-menu').find('.holder').attr('type');
			$('.selectParams').find('.item[type="'+type+'"]').remove();
			link.closest('.drop-menu').removeClass('changed').children('span').text(text).removeAttr('code');
			
    	}else{
    		link.closest('.drop-menu').addClass('changed').children('span').text(text).attr('code',link.attr('code'));
    	}    
		var ul = $(this).closest('ul');
		ul.css({display:'none'});
		setTimeout(function(){
			ul.removeAttr('style');
		},16);	
    	if(!prevent){	
    		$(document).trigger('filter',{
 			    newInteractive:true
    		});	
    	}
    });
    
    
	//面板中有4样： 链接列表，模拟的radio按钮，滑块控件，模拟的下拉列表
    $(document).on('click','.clearParams',function(){
    	resetFilter();
    	$('.my-search').val('');
    	$(document).trigger('filter',{
			newInteractive:true,
			data:NULL
		});
    	$('.selectParams').removeClass('fixed').removeAttr('style');
    	return false;
    });
    
    //移除当前查询条件
    $(document).on('click','.closeParam',function(){
    	var item = $(this).closest('.item');
    	var code = item.attr('code');
    	var target = $('.panel-filter').find('[code="'+code+'"]');
    	var context = target.closest('.drop-menu');
    	var holder = null;
    	var fixData = {};
		var equalToSearchInput = item.find('span').text() == $('.my-search').val();
		if( equalToSearchInput ){
			$('.my-search').val('');				
		}
		
    	if( item.is('[type="community"]') ){	//小区名
    		fixData.communityId = null;
			fixData.roomId = null
    	}else if(item.is('[type="room"]') ){
    		fixData.roomId = null;	
    	}else if(item.is('[type="searchWord"]')){	//搜索关键字
    		fixData.searchWord = null;
    		$('.my-search').val('');
    	}else if(context.length>0){	//下拉框
    		holder = context.find('.default');
    		context.removeClass('changed').find('.holder').text(holder.data('default')).removeAttr('code');
    	}else if( target.is('.fee-range') ){	//滑块控件 
			target.text( feeRange.join('-') ).removeClass('changed');
			slider.slider( "values", feeRange );			
		}else{	//是最常规的链接
			if( item.is('[type="subway"]') ){			//是地铁线
				map.removeOverlay(subwayOverlay);
				subwayOverlay = null;				
				fixData.subwayId = null;
				fixData.stationId = null;
			}else if( item.is('[type="station"]') ){	//是地铁站
				fixData.subwayId = null;
				fixData.stationId = null;
			}else if( item.is('[type="district"]') ){	//是行政区
				target.removeClass('on');
				fixData.districtId = null;
				fixData.areaId = null;
			}else if( item.is('[type="busiArea"]') ){	//是商圈
				fixData.districtId = null;
				fixData.areaId = null;
				target.closest('.item').children('a').removeClass('on');
			}			
			target.removeClass('active');
		}
		
    	$(this).closest('.item').remove();
    	$(document).trigger('filter',{
			newInteractive:true,
			data:fixData	//这次新交互将要清除的数据或者将要添加的数据，fixData中通过将key所对应的value值设置为null清除对应项
    	});
    });
    
	//修正“全部清除”按钮
    var updateBuffer = function(){
    	var context = $('.selectParams');
    	if( context.find('.item').length == 0 ){
    		context.addClass('empty').find('.clearParams').remove();
    	}else if( context.find('.clearParams').length == 0){
    		context.removeClass('empty').prepend('<div class="clearParams"><span>全部清除</span></div>');
    	}
    };
    
    // order 取值为 0~4 。 0：推荐排序；1：价格升；2：价格降；3：面积升；4：面积降
    var getSortEnum = function(){
 	   var order;
 	   var anchor = $('.panel-filter .sorting-list .active');
 	   var icon = anchor.children('i');
 	   if( anchor.find('i').length === 0 ){
 		   order = 0;
 	   }else{
 		   if( anchor.is('.by-price') ){ 	//按价格排序
 			   order = icon.is('.fa-rotate-180') ?1:2;
 		   }else if( anchor.is('.by-area') ){	//按面积排序
 			   order = icon.is('.fa-rotate-180') ?3:4;
 		   }
 	   }
 	   return order;
    };
    
    //获取已选择的搜房过滤条件 该函数将返回一个数组
    var getFilterData = function(){
    	var arr = [];
    	$('.panel-filter')
    	.find('.list .active,.mock-radio.active').each(function(){
    		arr.push({
    			name:$(this).text(),
    			code:$(this).attr('code'),
				type:$(this).attr('type')
    		});
    	}).end()
    	.find('.drop-menu.changed span').each(function(){
    		arr.push({
    			name:$(this).text(),
    			code:$(this).attr('code'),
				type:$(this).attr('type')
    		});
    	}).end()
		.find('.fee-range.changed').each(function(){
			arr.push({
				name:$(this).text(),
				code:$(this).attr('code'),
				type:'price'
			});
		});
    	return arr;
    };
    
    //根据设计文档的要求，构建文档路径
    var buildPath = function(filterData){
    	var arr = [];
    	var re=[];
    	var panel = $('.panel-filter');
    	var region = panel.find('.region-list .on,.region-list .item > .active');
    	var subway = panel.find('.subway-list .active');
    	if(filterData== null){
    		filterData = getFilterData();
    	}
    	arr = _(filterData).map(function(it){
    		return it.code;
    	});
    	
    	if( region.length==1){
    		re.push(region.attr('code'));
    		arr = splice(arr,region.attr('code'));
    	}
    	if( subway.length==1){
    		re.push(subway.attr('code'));
    		arr = splice(arr,subway.attr('code'));
    	}
    	if(arr.length>0){
    		re.push(arr.join('-'));
    	}
    	return re;
    };    
   
    
    //刷新URL U.refreshURL的外观函数，省去拼写pathname前缀
    var refreshHistory = function(path,search){
    	U.refreshURL({
    		search:search,
    		pathname:ROOT+'room/'+path
    	});
    };
	
	var refreshPagination = function(curr,total){
		var $pageWrapper = $('.panel-rooms .pagination-wrapper');
    	$pageWrapper.empty().append(pagination.build({
			curr:curr,
			total:total
		})).find('a').each(function(){				
			$(this).attr('href',U.mixURL({
				search:{
					page:$(this).attr('page')
				}
			}));
		});
    	var pageUIWidth = $pageWrapper.find('ul').width();
    	$pageWrapper.find('ul').css('marginLeft',-pageUIWidth/2);
		if($pageWrapper.find('a').length>3){
			$pageWrapper.prepend('<p class="text-center" style="margin-bottom:5px">共'+total+'页</p>');
		}
		
	};
    
    //刷新左边列表
    var refreshList = function(data){
		var search = U.deserializeURL();
		var titleSuffix = '';
		   
		if( (search.communityId||search.roomId) && data.roomInfos && data.roomInfos.length){
			if(data.roomInfos[0].districtName){
			   titleSuffix = data.roomInfos[0].districtName + '-';
			}
			data.communityTitle = {
				name:titleSuffix + data.roomInfos[0].comName,
				number:_(data.roomInfos).reduce(function(memo,it){
					if( it.roomCount == null){
						it.roomCount = 1;
					}
					return memo + it.roomCount;
				},0),
				desc:data.roomInfos[0].metroInfo
			};
            _(addMarkers.cache).each(function(it){
                if(it.id==search.communityId){
                    data.communityTitle.number = it.count;
                }
            });
		}
		data.hasCommunity = U.deserializeURL().communityId?true:false;
    	$('.panel-rooms').empty().append(rc.render( '#mainTmpl',data) );
    	refreshPagination(data.currentPage,data.totalPage);
		if(data.title){
			document.title = data.title;
		}
		$(document).trigger('refreshed');
    };   
    
    
	//触发自定义事件 filter时，刷新筛选条件
    //prevent 状态量 为true时阻止刷新列表
    //newInteractive 状态量 为true时表示这是一次新的交互
    $(document).on('filter',function(e,param){
    	var prevent = param.prevent;
    	var newInteractive = param.newInteractive;
    	var arr = getFilterData();
    	var path = buildPath( arr );
    	var tp = '{{each list as it}}'+
    				'<div class="item" code="{{it.code}}" type="{{it.type}}">'+
    					'<span>{{it.name}}</span>'+
						'<i class="closeParam"></i>'+
					'</div>'+
    			'{{/each}}';
    	var render = template.render(tp);
    	var search = {};
    	var order = getSortEnum();
    	search.order = order==0?null:order;	//0表示默认排序，还是移除记录为order为0的情况吧，清爽一些
    	$.extend(search,param.data||{});	//新旧交互都可以设置其它URL信息，这些信息通过param.data传递
    	if(newInteractive){	//如果这被定义成一次新交互（param.newInteractive为true表示这是一次新交互）
    		search.page = null;		//新交互默认清除URL中的page信息
    	}
    	if(search.communityId===null){
    		$('.selectParams').find('[type="community"]').remove();
    	}
		/*
		var currArr = [];
		$('.selectParams .item').each(function(){
			var type = $(this).attr('type');
			if( type!='community' && type!='searchWord'){
				var item = {
					name:$('span',this).text(),
					code:$(this).attr('code'),
					type:$(this).attr('type')
				};
				var find = _(arr).find(function(it){
					return it.code = item.code;
				});
				if( !find ){
					currArr.push(item);
				}				
			}
		});*/
		var frag = _(arr).map(function(it){
			return template('filterTmpl',it);
		}).join('');
    	$('.selectParams').find('.item').not('[type=community],[type=searchWord],[type=room],[type=station]').remove().end().end().append( frag );
    	updateBuffer();
		if(newInteractive){
			refreshHistory(path.join('/'),search);
		}    	
    	if(!prevent){    		
    		$(document).trigger('refresh',path);	
    	}    	
    }); 
    
    $(document).on('mouseover','.selectParams .clearParams',function(){
    	$('.selectParams .item').addClass('hover');
    });
    
    $(document).on('mouseout','.selectParams .clearParams',function(){
    	$('.selectParams .item').removeClass('hover');
    });    
   
    $(document).on('click','.pagination a',function(){      	
    	rc.addAjaxLoader();     
    	$.post($(this).attr('href'),$.proxy(function(data){
			var page = $(this).attr('page');
			U.refreshURL({
				search:{
					page:page
				}
			});    		
    		refreshList(data);
			$('.content-wrapper').scrollTop( $('.panel-filter').height());
    		rc.removeAjaxLoader(); 
    	},this),'json');   	
    	return false;
    });        
          
   
    $(document).on('refresh',function(e,path){
    	var uri = [].slice.call(arguments,1).join('/')+location.search;    	
		rc.addAjaxLoader();
		$.post(ROOT+'room/'+uri,function(data){
			refreshList(data);
			var hasCommunity = U.deserializeURL().communityId;
			if(!hasCommunity){				
				addMarkers(data.crs);
			}
			rc.removeAjaxLoader();
		},'json');
    });
    
    //当查询结果为空时，点击按钮“清空查询条件”，清除全部已选条件
    $(document).on('click','.reset-cmd',function(){
    	resetFilter();
    	$('.my-search').val('');
    	$(document).trigger('filter',{
			newInteractive:true,
			data:NULL
		 });
    });
   
    
   
    
    //点击地图中的房源标识时的处理函数
    var markerClickHandler = function(){
    	var curr = $('.marker'+this.id);
        var hasCurr = addFilter({
			name:curr.find('.community-name').text(),
			code:'cid'+this.id,
			type:'community',
			toggle:true
		});
    	updateBuffer();
    	U.refreshURL({
    		search:{
    			communityId:hasCurr?null:this.id
    		}
    	}); 
    	$.post(location.pathname+location.search,function(data){
    		refreshList(data);
			var hasCommunity = U.deserializeURL().communityId;
			if(hasCommunity){				
				$('.content-wrapper').scrollTop( $('.panel-filter').height()+25 );
			}else{
				addMarkers(data.crs);
				$('.selectParams').removeClass('fixed').removeAttr('style');
				$('.content-wrapper').scrollTop(0);
			}			
			
			//$('.content-wrapper').scrollTop($('.selectParams').offset().top);	//已选条件栏立马吸附在顶部
			
    	},'json');
    	$('.q.open').not(curr).removeClass('open manual');
    	curr.toggleClass('open manual');
    	
    };
	
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
   

    //向地图添加房源标识
    var addMarkers = function(data) {
        var s = '<div class="q {{if flatTag==2}}q2{{/if}} marker marker{{communityId}}" id="{{index}}">'
                    +'<div class="r">'
                    +'<span class="community-name">{{comName}}</span><small class="num">{{count}}</small><small class="unit">套</small><i></i><b></b>'
                    +'</div>'
               +'</div>';
        var render = template.render(s);      
      
        _(addMarkers.cache).each(function(it){
        	it.removeEventListener('click',markerClickHandler);
        	it.removeEventListener('mouseover', markerMouseOverHandler);
        	it.removeEventListener('mouseout', markerMouseOutHandler);
        	map.removeOverlay(it);
        });
         
        if( !_.isArray(data) ){
            return ;    //无数据时略过
        }
        
        addMarkers.cache = [];

        _(data).each(function(it,i){
            var frag = render(it); 
            var marker = new BMapLib.RichMarker(frag, new BMap.Point(it.lng, it.lat));
            marker.id = it.communityId;       
            marker.count = it.count;
            map.addOverlay(marker);
            addMarkers.cache.push(marker);
            marker.addEventListener('click', markerClickHandler);
            marker.addEventListener('mouseover', markerMouseOverHandler);
            marker.addEventListener('mouseout', markerMouseOutHandler);
        });
		
		var mapCenterSetter = null;
		
		if(data.length == 1){
			mapCenterSetter = function(){
				$('.marker'+data[0].communityId).addClass('open');
				map.panTo( new BMap.Point(data[0].lng,data[0].lat)  );
				map.removeEventListener('tilesloaded',mapCenterSetter);
				mapCenterSetter = null;
			//	$('.marker'+data[0].communityId).addClass('open').find('.r').addClass('animated infinite bounce');
				
			};
			map.addEventListener('tilesloaded',mapCenterSetter);			
		}
    };
    addMarkers.cache = [];
    
    
    var imgPercent =  9 / 16;
    //更改图片的填充方式（默认为水平方向100%填充，垂直方向溢出或者缺省）为垂直方向100%填充，水平方向溢出
    var imgHorizontalFill = function(){
    	var width = $(this).width();
		var height = $(this).height();
		//如果图片垂直方向不能填充满wrapper，改为垂直方向填充满，水平方向溢出
		if(height/width < imgPercent){
			$(this).css({
				width:'auto',
				height:fixHeight
			});
		}
    };
    
    //修正图片，和clipImg函数一起配合，以保证图片永远都是把容器空间填充满的
    $(document).on('imgFix','img',function(e,data){
    	var width = $(this).width();
		var height = $(this).height();
		//如果图片垂直方向不能填充满wrapper，改为垂直方向填充满，水平方向溢出
		if(height/width < data.imgPercent){
			$(this).css({
				width:'auto',
				height:data.fixHeight
			});
		}
		$(this).css({
			'-webkit-transform': 'translatey(-10%)',
			'-moz-transform':'translatey(-10%)',
			'-ms-transform':'translatey(-10%)',
			'transform':'translatey(-10%)'
		});
    });
	
	
    
    //如果图片在垂直方向上有溢出，剪裁图片溢出的部分
    var clipImg = function(){  
    	var percent = 9 / 16;	//左边列表中的图片的高宽比
    	var wrapper = $('.mapList2');
    	var fixHeight = wrapper.width() * imgPercent;
    	var imgFix = function(){
    		$(this).trigger('imgFix',{
    			percent:percent,
    			fixHeight:fixHeight
    		});
    	};
    	wrapper.height( fixHeight );
    	wrapper.find('img').bind('load',imgFix);
    };    
//    $(document).on('refreshed',clipImg);    
//    $(window).on('resize',clipImg);
	
    
    // 城市选择列表的行为修正
    $(document).on('click','.mock-select .holder-option a',function(e){
    	e.preventDefault();
    });
   
    //选择面板中切换高亮样式 （不处理排序列表）
    //注意，“更多”一栏也复用该函数切换样式
   $(document).on('click','.panel-filter .row-body a:not(".drop-menu a,.sorting-list a")',function(e){ 
	   e.preventDefault();
	   $(this).closest('.list,.sorting-list').find('.active,.on').not(this).removeClass( 'active on' );
	   if( $(this).is('.on') ){
		    $(this).removeClass('on');
	   }else{
		    $(this).toggleClass('active');
	   }
	    
	   var fixData ={};
	   if( $(this).closest('.region-list,.subway-list').length==1){
		   fixData.communityId = null;
		   fixData.areaId = null;
		   fixData.districtId = null;
	   }else if ( $(this).closest('.subway-list').length == 1 ){
		   fixData.communityId = null;
		   fixData.stationId = null;
	   }
	   var subPanel = $(this).closest('.row-expand');
	   if( subPanel.length == 1 ){
		   subPanel.siblings('a')[$(this).is('.active')?'addClass':'removeClass']('on');
	   }else if($(this).closest('.subway-list').length==1){
		   $(this).trigger('draw');	//注意trigger draw之前按钮已经设置好了active状态
	   }
	   var code = $(this).attr('code');
	   if( !$(this).is('.active') ){
		   $('.selectParams').find('[code="'+code+'"]').remove();
	   }	   
	   $(document).trigger('filter',{
		   prevent:false,
		   newInteractive:true,
		   data:fixData
	   });
   });   
      
   //选择面板中切换高亮样式（仅处理排序列表）   
   $(document).on('click','.panel-filter .sorting-list a',function(e){
	   e.preventDefault();	   
	   if( $(this).is('.active') ){	
		   if( $('i',this).length == 0 ){	//已使用推荐排序且想再重复点击时，不作处理
			   return;
		   }else{	//已使用价格排序或者面积排序，切换升降序
			   $('i',this).toggleClass('fa-rotate-180');
		   }
		   
	   }else{	//将切换为使用当前排序方式
		   $(this).closest('.sorting-list').find('.active').removeClass('active').end().end().addClass('active');
	   }
	   $(document).trigger('filter',{
		   prevent:false,
		   newInteractive:true
	   });
   });
   
   // 选择面板中修正悬浮对话框的显示位置
   $(document).on('mouseover','.region-list .item > a',function(){
	   var $expand = $(this).siblings('.row-expand'); 
	   var top = -$(this).closest('.list').offset().top + $(this).closest('.item').offset().top + $(this).height();
	   $expand.css('top',top);	
   });
   
   $(document).on('click','.region-list a',function(){
	   $(this).trigger('draw');
   });
   
   //点击行政区域或者商圈，将地图移到对应中心点
   $(document).on('draw','.region-list a',function(){
	   var anchor = $(this);
	   var lat = anchor.attr('lat'),lng = anchor.attr('lng');	//lat,lng都为0代表着非商圈搜索，比如“上海周边”
	   var point;
	   if(lat!=0&&lng!=0){
		   map.removeEventListener('dragend',sortList);
		   point =  new BMap.Point(lng,lat) ;
		   map.panTo( point );
		   refreshMapCenterMarker( point );
		   map.addEventListener('tilesloaded',mapMoveEnd);		   
	   }	   
   });
   
   //在地图上绘制地铁线
   var subwayOverlay;  
   $(document).on('draw','.subway-list a',function(){
	   var subwaySearch;
	   if($(this).is('.active')){
		   subwaySearch = new BMap.BusLineSearch(map,{
				renderOptions:{},
				onGetBusListComplete: function(result){
				   if(result) {
					 var line = result.getBusListItem(0);//0表示上行，1表示下行
					 subwaySearch.getBusLine(line);
				   }
				},
				onGetBusLineComplete:function(s){
					map.removeOverlay(subwayOverlay);
					if(subwaySearch.getStatus()!=BMAP_STATUS_SUCCESS){
						return;
					}
					subwayOverlay=new BMap.Polyline(s.getPath(),{
						strokeColor:"#3744df",
						strokeOpacity:1,
						strokeWeight: 3,
						fillOpacity:0.2,
						strokeStyle:"solid"
					});
					map.addOverlay(subwayOverlay);
				}
			});
		   subwaySearch.getBusList($(this).text().trim());
	   }else{
		   map.removeOverlay(subwayOverlay);
		   subwayOverlay = null;
	   }
		   
   });
   
   
   //顶部head的搜索处理函数
   var searchInHead = function(){
		var  arr = [],
			input = $('.my-search').val(),	//用户实际输入的内容
			text='';		//实现传输以及使用的内容
		resetFilter();
		if( input != ''){
			input = input.trim();
			arr   = input.split(/\s+/);
			text = _(arr).map(function(it){
			   return encodeURIComponent(it);
			}).join(',');
			addFilter({
				name:arr.join(','),
				type:'searchWord'
			});
		}
		$(document).trigger('filter',{
			newInteractive:true,
			data:$.extend({},NULL,{searchWord:text})
		 });
   };   
 

	//为顶部的搜索定义更个性化的行为 （解除它的默认行为，再添加行为）
   var typeahead = function(cityId){
	   var selector = '.my-search';
	   $(selector).autocomplete('unbind');
	   rc.typeahead(selector,{
		   cityId:cityId,
		   choice:function(elem){
			   var data = elem.data();
			   resetFilter();
			   addFilter({
				   name:this.getValue(elem),
				   type:data.type
			   });
			   updateBuffer();
			   var point = new BMap.Point(data.xCoordinate,data.yCoordinate) ;
			   map.panTo( point );
			   refreshMapCenterMarker( point );
			   var anchor = $('.panel-filter').find('a[type="'+data.type+'"]').filter('[data-id="'+data.id+'"]');
			   if(anchor.length==1){	//	.panel-filter下面有对应链接，委托它处理
				   anchor.click();
			   }else{					//  没有对应链接，自己处理
					if(history && history.pushState){
						history.pushState(history.state,document.title, elem.find('a').attr('href') ); 
						$.post(ROOT+'room',U.buildType(data.type,data.id),function(json){
							refreshList(json);
							addMarkers(json.crs);
						},'json');
					}else if( location.href != elem.find('a').attr('href') ){
						location.href=elem.find('a').attr('href');
						return;
					}
			   }			   			   
		   },
		   rest:searchInHead
	   });
   };

   
   var timeoutId = null;
   var clearId = null;
   $(document).on('mouseover','.item-room',_.debounce(function(e){
		if( timeoutId!= null){
		   clearTimeout(timeoutId);
		   timeoutId = null;
		}
		if(clearId != null){
			clearTimeout(clearId);
			clearId = null;
		}
		$('.map-center-marker').removeClass('animated infinite bounce');
		if( $(this).is('.open') ){
			return;
		}
		timeoutId = setTimeout($.proxy(function(){
			var communityId = $(this).data('cid');
			var prevMarker = $('.marker.open').not('.manual');
			var currMarker = $('.marker'+communityId);			
			prevMarker.removeClass('open').find('.r').removeClass('animated infinite bounce');			
			currMarker.addClass('open').find('.r').addClass('animated infinite bounce');
			fall( prevMarker );
			fly( currMarker );
			var lat = $(this).data('lat');
			var lng = $(this).data('lng');
			if(lat==null||lat==0||lng==null||lng==0){
			   return;
			}		   
			var point = new BMap.Point(lng,lat);
			map.panTo(point);
		},this),600);
   },500));
   
   
   $(document).on('mouseout','.item-room',_.debounce(function(e){
		clearTimeout(timeoutId);
		timeoutId = null;
		$('.map-center-marker.bounce').removeClass();
		clearId = setTimeout($.proxy(function(){
			var communityId = $(this).data('cid'); 
			var marker = $('.marker'+communityId);
			if( !marker.is('.manual') ){
				marker.removeClass('open');
			}
			marker.find('.r').removeClass('animated infinite bounce');
			$('.map-center-marker').addClass('animated infinite bounce');
			fall( marker );
		},this),500);
   },500));
   
  $(document).off(rc.touchStart,'.my-search-btn').on(rc.touchStart,'.my-search-btn',function(){
	  searchInHead();
  });

	return {
		setRoot:function(root){
			ROOT = root;
		},
		refreshPagination:refreshPagination,
		addMarkers:addMarkers,	//向地图添加标记
		typeahead:typeahead,
		addFilter:addFilter,
		buildMap:buildMap,		//构建地图 需要传入城市名称
		refreshList:refreshList,	//刷新左边列表，数据为空时将显示占位符
		pending:pending				//页面加载时读取location.pathname 将搜索面板中对应项重新选中
	};

});