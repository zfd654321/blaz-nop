var game_size = 0;
var show_game = 1;
var s;
var roll_able = true;
$(function() {
	game_size = $(".leishow").length;
	do_roll();
	msroll();
	mslight();
	var height = document.documentElement.clientHeight;
	$(".lmian").css("height", height - 110);
	document.getElementById('rollcall').addEventListener('touchstart',
			function(e) {
				nStartY = e.targetTouches[0].pageY;
				nStartX = e.targetTouches[0].pageX;
			});
	document.getElementById('rollcall').addEventListener('touchend',
			function(e) {
				nChangY = e.changedTouches[0].pageY;
				nChangX = e.changedTouches[0].pageX;
				if (nStartX - nChangX > 30) {
					console.log("left");
					clearTimeout(s);
					do_roll();
				} else if (nChangX - nStartX > 30) {
					console.log("right");
					clearTimeout(s);
					back_roll();
				}

			});
})

function back_roll() {
	if (roll_able) {

		if (show_game < game_size) {
			show_game += 1;
		} else {
			show_game = 1;
		}
		rollturn();
	}
}

function do_roll() {
	if (roll_able) {
		if (show_game > 1) {
			show_game -= 1;
		} else {
			show_game = game_size;
		}
		rollturn();
	}
}

function rollturn() {
	roll_able=false;
	for (var i = 0; i < game_size; i++) {
		if ((i + show_game) % game_size == 0) {
			$("#leishow" + i)
					.attr("style",
							"transform-origin: 120%;transform: rotateY(-5deg);left:-976px;opacity: 0");
		} else if ((i + show_game) % game_size == 1) {
			$("#leishow" + i)
					.attr("style",
							"transform-origin: 120%;transform: rotateY(-5deg);left:-476px;opacity: 1");
		} else if ((i + show_game) % game_size == 2) {
			$("#leishow" + i)
					.attr("style",
							"transform-origin: 120%;transform: rotateY(0deg);left:108px;opacity: 1");
		} else if ((i + show_game) % game_size == 3) {
			$("#leishow" + i)
					.attr("style",
							"transform-origin: -20%;transform: rotateY(5deg);left:692px;opacity: 1");
		} else if ((i + show_game) % game_size == 4) {
			$("#leishow" + i)
					.attr("style",
							"transform-origin: -20%;transform: rotateY(5deg);left:1192px;opacity: 0");
		} else {
			$("#leishow" + i).attr("style", "opacity: 0");
		}
	}
	si = setTimeout(clearRoll, 1000);
	s = setTimeout(do_roll, 3000);
}

function clearRoll() {
	roll_able=true;
}

function rebuild_lies() {

}

function show_lies() {

}


var mslineleft = 0;
function msroll() {

	if (mslineleft < -95 - parseInt($(".msline .c1").css("width"))) {
		mslineleft = 0
	} else {
		mslineleft -= 1
	}
	$(".msline").css("left", mslineleft);
	setTimeout(msroll, 10);
}

var slight = true;
function mslight() {
	if (slight) {
		$(".msgicon").css("opacity", 0);
		$(".pleft").css("left", 220);
		$(".pright").css("right", 220);
		slight = false;
	} else {
		$(".msgicon").css("opacity", 1);
		$(".pleft").css("left", 200);
		$(".pright").css("right", 200);
		slight = true;
	}
	setTimeout(mslight, 500);
}
