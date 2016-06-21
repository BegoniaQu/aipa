define("map", [], function() {
	var e = {};
	e.zoom = 12;
	var t = {
		region: "",
		district: "",
		metro: "",
		station: "",
		price: "",
		roomType: "",
		tag: "",
		sortType: "",
		iPage: 1,
		iRows: 20,
		getlonjd: "",
		getsLatwd: ""
	},
		n = {
			hoverFlag: !0,
			ajaxstatus: !0,
			pageDataLength: !0,
			zoomOrdragtwo: !1,
			flitermouseOver: function() {
				$(this).hasClass("disable") || ($(this).addClass("n-hover"), $(this).children(".mapicon").removeClass("m_down").addClass("m_up"), $(this).children("ul").show())
			},
			flitermouseOut: function() {
				$(this).removeClass("n-hover"), $(this).children(".mapicon").removeClass("m_up").addClass("m_down"), $(this).children("ul").hide()
			},
			showStation: function() {
				var e = $("#subwaylist").height() + 22,
					t = $(this).position().left + 90;
				$(this).next("ul").css({
					left: t,
					display: "block",
					"min-height": e
				})
			},
			hideStation: function() {
				n.hoverFlag && $(this).next("ul").hide()
			},
			hoverStation: function() {
				$("#subwaylist").show(), $(this).show(), $(this).prev(".lineway").addClass("station_current"), n.hoverFlag = !1
			},
			outStation: function() {
				$(this).hide(), n.hoverFlag = !0, $(this).prev(".lineway").removeClass("station_current")
			},
			mapRegionFliter: function() {
				var r = ' <i class="hideline"></i>';
				t.iPage = 1, n.pageDataLength = !0;
				if ($(this).data("val") != "") {
					var i = $(this).children("span").html(),
						s = $(this).children("ul").html();
					$("#region").children("label").html(i), t.region = i, t.district = "", $("#subregion").removeClass("disable"), $("#subregionlist").html(r + s), $(this).addClass("li-current").siblings().removeClass("li-current");
					var o = parseFloat($(this).children("span").data("ix")),
						u = parseFloat($(this).children("span").data("iy"));
					$("#subregionlist").attr("data-ix", o), $("#subregionlist").attr("data-iy", u), t.getlonjd = o, t.getsLatwd = u, e.MAP.setZoomAndCenter(13, new AMap.LngLat(o, u)), n.zoomOrdragtwo = !1, n.checkZoom(), $("#subregion").children("label").html("板块"), $("#regionlist").hide()
				} else {
					$("#subregion").addClass("disable"), $("#subregionlist").html(r), t.getlonjd = "", t.getsLatwd = "", $("#region").children("label").html("区域"), t.region = "", t.district = "", $("#subregion").children("label").html("板块"), $(this).addClass("li-current").siblings().removeClass("li-current");
					var o = $("#citylongitudeid").val() || "121.469",
						u = $("#citylatitudeid").val() || "31.2382";
					e.MAP.setZoomAndCenter(11, new AMap.LngLat(o, u)), n.zoomOrdragtwo = !1, n.checkZoom(), $("#regionlist").hide()
				}
			},
			mapsubRegionFliter: function() {
				t.iPage = 1, n.pageDataLength = !0;
				var r = $(this).html();
				$("#subregion").children("label").html(r), $(this).addClass("li-current").siblings().removeClass("li-current"), $("#subregionlist").hide();
				if (r == "全部") {
					var i = parseFloat($(this).parent("ul").data("ix")),
						s = parseFloat($(this).parent("ul").data("iy"));
					t.district = "", e.MAP.setZoomAndCenter(13, new AMap.LngLat(i, s)), n.zoomOrdragtwo = !1, n.checkZoom()
				} else {
					t.district = r;
					var i = parseFloat($(this).data("ix")),
						s = parseFloat($(this).data("iy"));
					e.MAP.setZoomAndCenter(14, new AMap.LngLat(i, s)), n.zoomOrdragtwo = !1, n.checkZoom()
				}
			},
			mapSubwaylistFliter: function() {
				t.iPage = 1, n.zoomOrdragtwo = !1, n.pageDataLength = !0;
				var r, i = $(this).children("a").length;
				i > 0 ? r = $(this).children("a").html() : r = $(this).text(), i > 0 || $(this).hasClass("way_nolimit") ? ($(this).hasClass("way_nolimit") ? ($("#subway").attr("data-type", "metro"), t.metro = $(this).html().replace("全部", ""), t.station = "") : ($("#subway").attr("data-type", "station"), t.station = $(this).children("a").html(), t.metro = $(this).parent(".way_station").prev().text()), $(this).addClass("li-current").siblings().removeClass("li-current"), $(this).parent(".way_station").prev().addClass("li-current").siblings().removeClass("li-current"), $(this).parent(".way_station").siblings().find("li").removeClass("li-current"), e.MAP.clearMap(), n.loupanData(!0, !0)) : ($(this).addClass("li-current").siblings().removeClass("li-current"), $(this).siblings(".way_station").children("li").removeClass("li-current"), $("#subway").attr("data-type", "metro"), t.metro = $(this).text(), t.station = "", $(this).text() == "全部" ? (t.metro = "", t.station = "", n.checkZoom()) : (e.MAP.clearMap(), n.loupanData(!0, !0))), $("#subway").children("label").html(r), $("#subwaylist").hide()
			},
			mapPriceFilter: function() {
				t.iPage = 1, n.zoomOrdragtwo = !1, n.pageDataLength = !0;
				var r = $(this).html();
				$("#price").children("label").html(r), $(this).addClass("li-current").siblings().removeClass("li-current"), $("#pricelist").hide(), r == "全部" ? (t.price = "", n.checkZoom()) : (t.price = r, e.MAP.clearMap(), n.loupanData(!0, !0))
			},
			maphouseTypeFilter: function() {
				t.iPage = 1, n.zoomOrdragtwo = !1, n.pageDataLength = !0;
				var r = $(this).html();
				$("#houseType").children("label").html(r), $(this).addClass("li-current").siblings().removeClass("li-current"), $("#houseTypeList").hide(), r == "全部" ? (t.roomType = "", n.checkZoom()) : (t.roomType = r, e.MAP.clearMap(), n.loupanData(!0, !0))
			},
			mapfeatureFilter: function() {
				t.iPage = 1, n.zoomOrdragtwo = !1, n.pageDataLength = !0;
				var r = $(this).html();
				$("#feature").children("label").html(r), $(this).addClass("li-current").siblings().removeClass("li-current"), $("#featureList").hide(), r == "全部" ? (t.tag = "", n.checkZoom()) : (t.tag = r, e.MAP.clearMap(), n.loupanData(!0, !0))
			},
			initMap: function() {
				var t = $("#citylongitudeid").val() || "121.469",
					r = $("#citylatitudeid").val() || "31.2382";
				$("#mapContainer").height($(window).height() - 120), e.MAP = new AMap.Map("mapContainer", {
					resizeEnable: !0,
					view: new AMap.View2D({
						center: new AMap.LngLat(t, r),
						zoom: 11
					})
				}), e.MAP.plugin(["AMap.ToolBar"], function() {
					var t = new AMap.ToolBar({
						offset: new AMap.Pixel(10, 55)
					});
					e.MAP.addControl(t)
				}), n.checkZoom(), AMap.event.addListener(e.MAP, "zoomend", function() {
					n.zoomOrdragtwo = !0, n.checkZoom()
				}), AMap.event.addListener(e.MAP, "dragend", function() {
					n.zoomOrdragtwo = !0, n.checkZoom()
				})
			},
			checkZoom: function() {
				var r = e.MAP.getZoom();
				r < 13 ? t.metro == "" && t.station == "" && t.price == "" && t.roomType == "" && t.tag == "" ? (e.MAP.clearMap(), n.regionData()) : n.pageDataLength && (e.MAP.clearMap(), n.loupanData(!1, !0)) : 13 <= r && r < 14 ? t.metro == "" && t.station == "" && t.price == "" && t.roomType == "" && t.tag == "" ? (e.MAP.clearMap(), n.blockData()) : n.pageDataLength && (e.MAP.clearMap(), n.loupanData(!1, !0)) : (e.MAP.clearMap(), n.loupanData(!0, !0))
			},
			regionData: function() {
				$("#map-loading").show();
				var t = 1,
					r = e.MAP.getBounds(),
					i = r.getNorthEast().getLng(),
					s = r.getNorthEast().getLat(),
					o = r.getSouthWest().getLng(),
					u = r.getSouthWest().getLat();
				$.ajax({
					url: "/" + sCurrentCityCode + "/map/mapgetzone",
					type: "post",
					data: {
						zoomLevel: t,
						maxLon: i,
						maxLat: s,
						minLon: o,
						minLat: u
					},
					dataType: "json",
					success: function(e) {
						var t = e.data.data.data;
						$("#map-loading").hide();
						if (t.length > 0) for (var r = 0; r < t.length; r++) n.regionMapMarker(t[r].sName, t[r].sNum, t[r].sLon, t[r].sLat)
					}
				}), n.zoomOrdragtwo || n.loupanData(!1, !0)
			},
			regionMapMarker: function(t, n, r, i) {
				if (n != 0) {
					var s = '<div class="mapMarker"><p class="m_lptitle">' + t + "</p>" + '<p class="m_num">' + n + "个楼盘</p>" + "</div>",
						o = new AMap.Marker({
							map: e.MAP,
							position: new AMap.LngLat(r, i),
							topWhenClick: !0,
							topWhenMouseOver: !0,
							offset: new AMap.Pixel(-53, -50),
							content: s
						});
					o.setMap(e.MAP), AMap.event.addListener(o, "click", function() {
						$("#regionlist").children("li").each(function() {
							$(this).children("span").html() == t && $(this).trigger("click")
						})
					})
				}
			},
			blockData: function() {
				$("#map-loading").show();
				var r = 2,
					i = e.MAP.getBounds(),
					s = i.getNorthEast().getLng(),
					o = i.getNorthEast().getLat(),
					u = i.getSouthWest().getLng(),
					a = i.getSouthWest().getLat();
				$.ajax({
					url: "/" + sCurrentCityCode + "/map/mapgetzone",
					type: "post",
					data: {
						zoomLevel: r,
						maxLon: s,
						maxLat: o,
						minLon: u,
						minLat: a
					},
					dataType: "json",
					success: function(r) {
						$("#map-loading").hide();
						var i = r.data.data.data;
						if (i.length > 0) for (var s = 0; s < i.length; s++) n.blockMapMarker(i[s].sName, i[s].sNum, i[s].sLon, i[s].sLat);
						else {
							var o = t.getlonjd,
								u = t.getsLatwd;
							o && u && (e.MAP.setZoomAndCenter(14, new AMap.LngLat(o, u)), n.checkZoom())
						}
					}
				}), n.zoomOrdragtwo || n.loupanData(!1, !0)
			},
			blockMapMarker: function(t, n, r, i) {
				if (n != 0) {
					var s = '<div class="mapMarker"><p class="m_lptitle">' + t + "</p>" + '<p class="m_num">' + n + "个楼盘</p>" + "</div>",
						o = new AMap.Marker({
							map: e.MAP,
							position: new AMap.LngLat(r, i),
							offset: new AMap.Pixel(-53, -50),
							topWhenClick: !0,
							topWhenMouseOver: !0,
							content: s
						});
					o.setMap(e.MAP), AMap.event.addListener(o, "click", function() {
						$("#regionlist").children("li").each(function() {
							$(this).children("ul").html() != "" && $(this).children("ul").children("li").each(function() {
								$(this).html() == t && ($(this).parent().parent().trigger("click"), $("#subregionlist").children("li").each(function() {
									$(this).html() == t && $(this).trigger("click")
								}))
							})
						})
					})
				}
			},
			loupanData: function(e, r) {
				e && $("#map-loading").show();
				if (r) var i = {
					region: t.region,
					district: t.district,
					metro: t.metro,
					station: t.station,
					price: t.price,
					roomType: t.roomType,
					tag: t.tag,
					sortType: t.sortType,
					iPage: t.iPage,
					iRows: t.iRows
				};
				else var i = {
					region: t.region,
					district: t.district,
					metro: t.metro,
					station: t.station,
					price: t.price,
					roomType: t.roomType,
					tag: t.tag,
					sortType: t.sortType
				};
				n.rightListAjax(i, e, r)
			},
			loupanMapMarker: function(t, r, i, s, o) {
				var u = o.split(",");
				r == "价格待定" || r == "0" ? r = "售价待定" : r = "<span>" + n.formatPrice(r) + "</span>元/平米";
				var a = '<div class="m_mapall"><a href="' + i + '" target="_blank"><div class="mlp_divhover">' + '<div class="mp_innerBg2 clearfix">' + '<div class="mp_leftimage"><img src="' + s + '"></div>' + '<div class="right_lpname">' + '<p class="lp_name">' + t + "</p>" + '<p class="lp_price">' + r + "</p>" + "</div>" + '</div><s class="lp_border"></s></div>' + '<div class="mlp_div"><div class="mp_innerBg"><div class="mright_lpname"><p class="lp_name">' + t + '</p><p class="lp_price">' + r + '</p></div></div><s class="lp_red"></s></div></a></div>',
					f = new AMap.Marker({
						map: e.MAP,
						position: new AMap.LngLat(u[1], u[0]),
						offset: new AMap.Pixel(-47, -56),
						topWhenClick: !0,
						topWhenMouseOver: !0,
						content: a
					});
				f.setMap(e.MAP)
			},
			rightListAjax: function(r, i, s) {
				!i && (t.metro != "" || t.station != "" || t.price != "" || t.roomType != "" || t.tag != "") && (i = !0), $.ajax({
					url: "/" + sCurrentCityCode + "/map/mapgetbuildings",
					type: "post",
					data: r,
					dataType: "json",
					success: function(r) {
						var o = r.data.data.data;
						r.data.data.total ? $("#LoupanTotal").html(r.data.data.total) : t.iPage == 1 && $("#LoupanTotal").html("0");
						var u = $("#mcityname").val(),
							a = r.data.data.tel;
						if (o.length > 0) {
							$("#map-loading").hide(), s && t.iPage != 1 && i && e.MAP.clearMap(), n.pageDataLength = !0, n.ajaxstatus = !0, $("#nolistData").hide(), $("#map_lp_list").show();
							var f = [];
							for (var l = 0; l < o.length; l++) {
								i && n.loupanMapMarker(o[l].sLoupanName, o[l].iBidingPrice, o[l].sdetailUrl, o[l].sDefaultImg, o[l].Coordinate);
								var c = [],
									h, p = "";
								if (o[l].sTags_ss) {
									o[l].sTags_ss.length > 3 ? h = 3 : h = o[l].sTags_ss.length;
									if (h > 0 && o[l].sTags_ss[0] != "") {
										for (var d = 0; d < h; d++) c[d] = "<a>" + o[l].sTags_ss[d] + "</a>";
										p = c.join("")
									} else p = ""
								}
								var v = [],
									m = "";
								if (o[l].sRoomTypeInfo_ss) {
									for (var g = 0; g < o[l].sRoomTypeInfo_ss.length; g++) o[l].sRoomTypeInfo_ss[g] = o[l].sRoomTypeInfo_ss[g].replace("(", "<i>(").replace(")", ")</i>"), v[g] = '<span><a href="/' + sCurrentCityCode + "/newhouse/loudetail/" + o[l].iUnitID + '#roomTypeArea"  target="_blank">' + o[l].sRoomTypeInfo_ss[g] + "</a></span>";
									m = v.join("<span>|</span>")
								}
								var y = "bgnone";
								o[l].sEJScore == "AAA" ? y = "jkrs" : o[l].sEJScore == "A+" ? y = "tjgm" : o[l].sEJScore == "BBB+" ? y = "jsrs" : o[l].sEJScore == "C" && (y = "cbgw");
								var b, w, E = o[l].sDefaultPhone;
								a[u][o[l].sRegionName] ? b = a[u][o[l].sRegionName] : b = a[u]["全部"], E == "" ? w = "400-810-6999 转" + b : w = E;
								var S = "<span>" + n.formatPrice(o[l].iBidingPrice) + "</span> 元/㎡",
									x = n.formatPrice(o[l].iBidingPrice);
								x == 0 ? S = "<span>待定</span>" : S = "<span>" + n.formatPrice(o[l].iBidingPrice) + "</span> 元/㎡", f[l] = '<li class="clearfix bgf mapLphover" data-xy="' + o[l].Coordinate + '">' + '<div class="imge_box">' + ' <a href="' + o[l].sdetailUrl + '" target="_blank"><img src="' + o[l].sDefaultImg + '" /></a>' + "</div>" + '<div class="image_info">' + '<div class="item clearfix">' + ' <div class="item_l house_name">' + " <em>" + o[l].iTotalScore.toFixed(2) + "</em>" + '<a href="' + o[l].sdetailUrl + '" target="_blank">' + o[l].sLoupanName + "</a>" + "</div>" + '<div class="item_r mr15">售价' + S + "</div>" + "</div>" + '<div class="item clearfix mt5">' + '<div class="item_l hl24">' + "<p>[" + o[l].sRegionName + "-" + o[l].sZoneName + "] " + o[l].sAddress + "</p>" + "</div>" + '<div class="item_r">' + '<em class="zt ' + y + '"></em>' + "</div>" + "</div>" + '<div class="item room_num mt10">' + m + "</div>" + ' <div class="item clearfix mt13 house_tag">' + '<div class="item_l">' + p + "</div>" + '<div class="item_r mr15">' + ' <p class="phone"> ' + w + "</p>" + "</div>" + " </div>" + "</div>" + "</li>"
							}
							s && t.iPage != 1 ? n.zoomOrdragtwo || $("#map_lp_list").append(f.join("")) : (t.iPage = 1, n.zoomOrdragtwo = !1, $("#map_lp_list").html(f.join("")))
						} else $("#map-loading").hide(), n.pageDataLength = !1, t.iPage == 1 && ($("#nolistData").show(), $("#map_lp_list").hide())
					}
				})
			},
			mpriceOrder: function() {
				t.iPage = 1, $(this).children("s").show(), $("#mStoreOrder").children("s").hide(), $(this).addClass("mcurrent").siblings().removeClass("mcurrent"), $(this).parent().prev().hasClass("mcurrent") && $(this).parent().prev().removeClass("mcurrent"), $(this).children("s").hasClass("upicon") ? ($(this).children("s").removeClass("upicon"), t.sortType = 2, (t.metro != "" || t.station != "" || t.price != "" || t.roomType != "" || t.tag != "") && e.MAP.clearMap(), n.loupanData(!1, !0)) : ($(this).children("s").addClass("upicon"), t.sortType = 1, (t.metro != "" || t.station != "" || t.price != "" || t.roomType != "" || t.tag != "") && e.MAP.clearMap(), n.loupanData(!1, !0))
			},
			mStoreOrder: function() {
				t.iPage = 1, $(this).children("s").show(), $("#mpriceOrder").children("s").hide(), $(this).addClass("mcurrent").siblings().removeClass("mcurrent"), $(this).parent().prev().hasClass("mcurrent") && $(this).parent().prev().removeClass("mcurrent"), $(this).children("s").hasClass("upicon") ? ($(this).children("s").removeClass("upicon"), t.sortType = 4, (t.metro != "" || t.station != "" || t.price != "" || t.roomType != "" || t.tag != "") && e.MAP.clearMap(), n.loupanData(!1, !0)) : ($(this).children("s").addClass("upicon"), t.sortType = 3, (t.metro != "" || t.station != "" || t.price != "" || t.roomType != "" || t.tag != "") && e.MAP.clearMap(), n.loupanData(!1, !0))
			},
			morenOrder: function() {
				$("#mStoreOrder").children("s").hide(), $("#mpriceOrder").children("s").hide(), $(this).addClass("mcurrent"), $(this).next().children().removeClass("mcurrent"), t.sortType = "", t.iPage = 1, (t.metro != "" || t.station != "" || t.price != "" || t.roomType != "" || t.tag != "") && e.MAP.clearMap(), n.loupanData(!1, !0)
			},
			scrollHandler: function() {
				var e = $(this).height(),
					r = $(this)[0].scrollTop,
					i = $(this)[0].scrollHeight;
				if (r + e >= i && n.ajaxstatus) {
					if (!n.pageDataLength) return !1;
					n.ajaxstatus = !1, t.iPage++, n.zoomOrdragtwo = !1, n.loupanData(!1, !0)
				}
			},
			formatPrice: function(e) {
				var t = parseInt(e),
					n, r;
				for (t = t.toString().replace(/^(\d*)$/, "$1."), t = (t + "00").replace(/(\d*\.\d\d)\d*/, "$1"), t = t.replace(".", ","), n = /(\d)(\d{3},)/; n.test(t);) t = t.replace(n, "$1,$2");
				return t = t.replace(/,(\d\d)$/, ".$1"), r = t.split("."), r[1] == "00" && (t = r[0]), t
			}
		};
	return n
}), define("webcommon", [], function() {
	var e = {
		scrollLoadimg: function(e) {
			var t = parseInt($(window).height()) + parseInt($(window).scrollTop()),
				n = parseInt($(window).scrollTop());
			if (e.indexOf(",") == -1) {
				var r = parseInt($("#" + e).offset().top);
				r >= n && r <= t && $("#" + e).find("img.lazyloadimg").each(function() {
					$(this).data("src") != "" && $(this).attr("src", $(this).data("src"))
				})
			} else {
				var i = e.split(",");
				for (var s = 0; s < i.length; s++) {
					var r = parseInt($("#" + i[s]).offset().top);
					r >= n && r <= t && $("#" + i[s]).find("img.lazyloadimg").each(function() {
						$(this).data("src") != "" && $(this).attr("src", $(this).data("src"))
					})
				}
			}
		},
		listmohuSearch: function() {
			var e = $("#search_input2").val();
			e = e.replace(/^\s+/gi, "").replace(/\s+$/gi, "").replace(/[~!@#$%\^\+\*&\\\/\?\|:\.<>{}';="]/, "");
			if (e == "") $("#search_listurl").hide();
			else {
				var t = {
					keywords: e
				};
				$.post("/" + sCurrentCityCode + "/map/mapajax", t, function(e) {
					var t = e.data.data.length;
					t > 5 ? t = 5 : t = e.data.data.length;
					if (t > 0) {
						var n = [];
						for (var r = 0; r < t; r++) n[r] = '<li><a href="' + e.data.data[r].Url + '"  target="_blank"><span>' + e.data.data[r].sLoupanName + "</span><em>" + e.data.data[r].sAddress + "</em></a></li>";
						$("#search_listurl").html(n.join("")), $("#search_listurl").show()
					} else $("#search_listurl").hide()
				}, "json")
			}
		},
		listSearch: function() {
			var e = $("#search_input2").val();
			e = e.replace(/^\s+/gi, "").replace(/\s+$/gi, "").replace(/[~!@#$%\^\+\*&\\\/\?\|:\.<>{}';="]/, ""), e != "" ? window.location.href = "/" + sCurrentCityCode + "/newhouse/index?k=" + encodeURIComponent(e) : window.location.href = "/" + sCurrentCityCode + "/newhouse/index"
		}
	};
	return $(function() {
		$("#city_select").hover(function() {
			$(this).children(".city_list ").show()
		}, function() {
			$(this).children(".city_list ").hide()
		}), $(".city_list").on("click", "a", function() {
			var e = $(this).html();
			$("#city_select").children("span").html(e), $(this).parent(".city_list ").hide()
		}), $(".ct_per_info").mouseenter(function(e) {
			$(this).children("ul").show()
		}).mouseleave(function(e) {
			$(this).children("ul").hide()
		}), $("#w_phonenumbtn").mouseenter(function(e) {
			$(this).css("background-position", "0 -576px"), $(this).children(".phone_num_con").show()
		}).mouseleave(function(e) {
			$(this).css("background-position", "0 -516px"), $(this).children(".phone_num_con").hide()
		}), $("#w_gotoTopbtn").mouseenter(function(e) {
			$(this).css("background-position", "-90px -576px"), $(this).children(".down_con").show()
		}).mouseleave(function(e) {
			$(this).css("background-position", "-90px -516px"), $(this).children(".down_con").hide()
		});
		var t = $("#userInfoID").val(),
			n = $("#sToken").val();
		t != "" && n != "" && $("#logout").on("click", function() {
			$.post("/user/index/logout", {
				userID: t,
				token: n
			}, function(e) {
				e.data.msg == "ok" ? window.location = "/index/index" : alert("退出失败请稍后再试")
			}, "json")
		}), $(window).scroll(function() {
			$(window).scrollTop() > .1 ? $(".right_suspend").is(":hidden") && $(".right_suspend").fadeIn() : $(".right_suspend").is(":visible") && $(".right_suspend").fadeOut()
		}), $(".back_top").click(function(e) {
			$("body,html").animate({
				scrollTop: 0
			}, 500)
		}), $("#search_input2").on("keydown", function(t) {
			t.which == 13 && e.listSearch()
		}), $("#searchbtn_index2").on("click", function() {
			e.listSearch()
		}), $("#search_input2").on("keyup", e.listmohuSearch), window.onbeforeunload = function() {
			$(window).scrollTop(0)
		}
	}), e
}), require.config({
	paths: {
		webcommon: "../js/webCommon",
		map: "../js/map"
	}
}), require(["map", "webcommon"], function(e, t) {
	$(function() {
		e.initMap(), $(".menuitem").hover(e.flitermouseOver, e.flitermouseOut), $(".lineway").hover(e.showStation, e.hideStation), $(".way_station").hover(e.hoverStation, e.outStation), $("#mapContainer").on("mouseover", ".m_mapall", function() {
			$(this).parents(".amap-marker").css({
				"z-index": "800"
			})
		}), $("#mapContainer").on("mouseleave", ".m_mapall", function() {
			$(this).parents(".amap-marker").css({
				"z-index": "100"
			})
		}), $(".mapright_list").height($(window).height() - 173), $(document).bind("click", function(e) {
			$("#search_listurl").hide()
		}), $(window).resize(function() {
			$("#mapContainer").height($(window).height() - 118), $(".mapright_list").height($(window).height() - 173)
		}), $("#regionlist").on("click", "li", e.mapRegionFliter), $("#subregion").on("click", "li", e.mapsubRegionFliter), $("#subwaylist").on("click", "li", e.mapSubwaylistFliter), $("#price").on("click", "li", e.mapPriceFilter), $("#houseType").on("click", "li", e.maphouseTypeFilter), $("#feature").on("click", "li", e.mapfeatureFilter), $("#mpriceOrder").on("click", e.mpriceOrder), $("#mStoreOrder").on("click", e.mStoreOrder), $("#morenOrder").on("click", e.morenOrder), $(".mapright_list").scroll(e.scrollHandler)
	})
}), define("enterjs/mapconfig", function() {});