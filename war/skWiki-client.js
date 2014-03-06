$(document).ready(function() { 
	
	$("#newuser-text").click(function() {
		console.log("a clicked"+$(".login").text());
		if ($(".login").text() === "SKWIKI LOGIN") {
			$(".newuser-password-text").fadeIn('slow');
			$("#newuser-password-field").fadeIn('slow');
			$(".login").text('SKWIKI NEW USER LOGIN');
			$("#newuser-text").text('Existing user');
		} else if ($(".login").text() === "SKWIKI NEW USER LOGIN") {
			$(".newuser-password-text").fadeOut('slow');
			$("#newuser-password-field").fadeOut('slow');
			$(".login").text('SKWIKI LOGIN');
			$("#newuser-text").text('Create new user');
		}
	});
	
	//open popup
	$("#project-viewer-add-project").click(function(){
		$("#new-project-form").fadeIn(300);
		positionPopup();
	});
	
	//close popup
	$("#new-project-form-close").click(function(){
		$("#new-project-form").fadeOut(500);
	});
	 
	//position the popup at the center of the page
	function positionPopup(){
		if(!$("#new-project-form").is(':visible')){
			return;
		}
		$("#new-project-form").css({
		left: ($(window).width() - $('#new-project-form').width()) / 2,
		top: ($(window).width() - $('#new-project-form').width()) / 7,
		position:'absolute'
		});
	}
	
	//click screenshot -- save picture as a png image -- opens in a new window
	$("#screenshot-button").click(function() {
		html2canvas(document.getElementById("skwiki-viewer"), {
		  onrendered: function(canvas) {
			var context = canvas.getContext('2d');
			context.translate(-30, -30);
	        var w = window.open('about:blank','image from canvas');
			w.document.write("<img src='"+canvas.toDataURL("image/png")+"' alt='from canvas'/>");
		 }
		});
	});
	
});