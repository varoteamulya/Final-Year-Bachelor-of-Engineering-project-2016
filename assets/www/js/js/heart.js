$(document).on("pageshow","#heartmain",function(){


//update profile
 
  var fname=window.localStorage.getItem("fname");
        var mname=window.localStorage.getItem("mname");
        var lname=window.localStorage.getItem("lname");
        var gender=window.localStorage.getItem("gender");
        var dob=window.localStorage.getItem("dob");
        var pcontactno=window.localStorage.getItem("pcontactno");
        var scontactno=window.localStorage.getItem("scontactno");
        var image=window.localStorage.getItem("image"); 
		
 		$("#heartname").html(fname+" "+mname+" "+lname);
			$("#heartmain #gender").html(gender);
			$("#heartmain #dob").html(dob);
			$("#heartmain #pcontactno").html(pcontactno);
			$("#heartmain #scontactno").html(scontactno);
	   	   	var dbImage1 = document.getElementById('myimageheart');
			dbImage1.style.display = 'block';
			dbImage1.src="data:image/jpeg;base64,"+window.localStorage.getItem("image");
	
 
function slideShow1(){
	$('#slider1') .cycle({
			fx: 'fade', //'scrollLeft,scrollDown,scrollRight,scrollUp',blindX, blindY, blindZ, cover, curtainX, curtainY, fade, fadeZoom, growX, growY, none, scrollUp,scrollDown,scrollLeft,scrollRight,scrollHorz,scrollVert,shuffle,slideX,slideY,toss,turnUp,turnDown,turnLeft,turnRight,uncover,ipe ,zoom
		speed:  'slow', 
   		timeout: 5000,
   		next:   '#next', 
    	prev:   '#prev',
    	slideResize: true,
        containerResize: true,
        width: '100%',
		height: '60%',
        fit: 0


	});
	


}


	var MaxHeight  = $("li").height();
//	$(".slider").height(MaxHeight+'px');
//	$(".slider").height(MaxHeight+'px');
	slideShow1();


$(window).resize(function(){
	var MaxHeight  = $("li").height();
	
	$(".slider").height(MaxHeight+'px');

});
	

//swipe

$(document).off('swipeleft').on('swipeleft', 'div[data-role="page"]', function(event){    
    if(event.handled !== true) // This will prevent event triggering more then once
    {    
        var nextpage = $.mobile.activePage.next('div[data-role="page"]');
		
        // swipe using id of next page if exists
        if (nextpage.length > 0) {
            $.mobile.changePage(nextpage, {transition: "flip", reverse: false}, true, true);
			
	       }
		   event.handled = true;
    }
    return false;         
});


$(document).off('swiperight').on('swiperight', 'div[data-role="page"]', function(event){   
    if(event.handled !== true) // This will prevent event triggering more then once
    {      
        var prevpage = $(this).prev('div[data-role="page"]');
		
        if (prevpage.length > 0) {
            $.mobile.changePage("#home", {transition: "flip", reverse: true}, true, true);
			
        }
        event.handled = true;
    }
    return false;            
});


});



$(document).on("pageinit","#heartmain",function(){


  var bpcountscale=0;
  var weight=0;
  var oxihvalue=0,oxibpvaluesys=0,oxibpvaluedia=0;
  var bpvaluesys=0;
  var bpvaluedia=0;
  var bppulse=0;
  var hvalue=0
  var cholest=0;
  var uri=0;
  var bpcount=0;


	//foracare bp new
	$("#startheart").bind('click', function(){ 

	
	bpcount=0;
 $.blockUI({message:"<h1>Measurement in progress...<h1>",
			css: { 
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        } }); 
	setTimeout(Getheartrate, 1000); 

	
	});

	
	
	function Getheartrate(){
		if(con1=="connected")
		var d=window.CustomNativeAccess.disconnect();
		
		con1=window.CustomNativeAccess.connect(); //zephyr heart rate
		if(con1=="connected"){
			
			setTimeout(heartDelay,2000);
			
		}
		else{
			$.unblockUI();
		$("#distance").html("0.0");
	$("#pulse1").html("0");
	$("#speed").html("0.0");
	$("#strides").html("0");
		hvalue=0;
	alert("Zephyr Not connected");
		
		}
	
	

		

}	
		
		  
function heartDelay(){
		
	$.unblockUI();
		faddingMessage("Zephyr Sensor connected");

		d=0;
		sd=0;
		c=0;
		sc=0;
		var packet=window.CustomNativeAccess.getHeartRate();
		var n=packet.split("|");
		hvalue=parseInt(n[0].trim());

		var c=$("#cloudheart option:selected").text();
		
		if(c=="Yes"){
		pushtocloud();
		}
		hid=setInterval(printheartinfo, 2000);
		$.mobile.changePage( "#heartmap", { transition: "slideup", changeHash: false });
		//document.location.href="#map";	
		
		
}
		
		  
function pushtocloud(){

 //var name=document.getElementById("name").innerHTML;
var name=$('#heartname').html();
 var username=window.localStorage.getItem("patemail");

var hl7Msg=encodeToHl7(username,name,hvalue,svalue,bpvaluesys,bpvaluedia,weight,oxihvalue,oxibpvaluesys,oxibpvaluedia,uri,cholest);
 var sensor_type="Heart Rate";
 var sensor_value=hvalue+":"+svalue+":"+bpvaluesys+":"+bpvaluedia+":"+bppulse+":"+weight+":"+oxihvalue+":"+oxibpvaluesys+":"+oxibpvaluedia+":"+uri+":"+cholest;
// if(!((hvalue<5 || hvalue>220)))
//{	
if(window.onLine==true)
{	

//loading
$.blockUI({ 
 message:"<h1>Pushing to Cloud..</h1>",css: { 
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        } }); 
 		   
    
// var encodedmsg=window.CustomNativeAccess.Encode("ak123","Akhil","Naik",134,123);
 //alert(encodedmsg);
 jQuery.support.cors = true;
 var request = $.ajax({
  url: "http://mhealthcloud.elasticbeanstalk.com/PushHealthdata",
  type: "POST",
  data: { sensor_type:sensor_type,content:sensor_value,Email:username},
  dataType: "html"
});
	

request.done(function( msg ) {
  
  $.unblockUI();
  //alert("Data Successfully pushed to Cloud!!");
  	faddingMessage("Data Successfully pushed to Cloud!!");
});
 
request.fail(function( jqXHR, textStatus ) {
$.unblockUI();
  //alert( "Sorry!!..Data could not be pushed");
  faddingMessage("Sorry!!..Data could not be pushed");
});


	}
	else{
//alert("You are offline..turn on internet");
 faddingMessage("You are offline..turn on internet");
}
//	}else{
//	 faddingMessage("Invalid Data");

//	}
//



}
		  


var flag=0;

var c=0,d=0,f=0,f1=0;
var sc=0,sd=0;
function printheartinfo(){
	var dist;	
	var strides;
	
	var packet=window.CustomNativeAccess.getHeartRate();
	var n=packet.split("|");
	dist=parseInt(n[2].trim());
	strides=parseInt(n[3].trim());
	if(dist==240)
	f=1;
	if(d==0)
	d=dist;
	if(dist==255 && f==1)
	{
	f=0;
	c++;
	}
	
if(sd==0)
	sd=strides;
	
	if(strides==120)
	f1=1;
	if(strides==127 && f1==1){
	f1=0;
	sc++;
	}
	dist+=255*c;
	dist=dist-d;
	
	strides+=127*sc;
	strides=strides-sd;
//	$("#test").html("hp"+n[0].trim()+"<br>speed"+n[1].trim()+"<br>dist"+n[2].trim()+"<br>strides"+n[3].trim()+"<br>rr"+n[5].trim()+"<br>battery"+n[4].trim());
	$("#distance").html(dist+"");
//	$("#pulse").html(n[0].trim());
	hearttemp=parseInt(n[0].trim());
	$("#pulse1").html(n[0].trim());
	$("#speed").html(n[1].trim());	
	$("#strides").html(strides+"");
	
	$("#heartmap #distance").html(dist+"");
//	$("#pulse").html(n[0].trim());
	hearttemp=parseInt(n[0].trim());
	$("#heartmap #pulse1").html(n[0].trim());
	$("#heartmap #speed").html(n[1].trim());	
	
	$("#heartmap #strides").html(strides+""); 
	if(n[0].trim()=="000" || n[0].trim()=="0" || n[0].trim()=="00")
	{
	//alert("sensor not connected");
	hvalue=0;
	$("#distance").html("0.0");
	$("#pulse1").html("0");
	$("#speed").html("0.0");
	$("#strides").html("0");
	$("#heartmap #distance").html("0.0");
	$("#heartmap #pulse1").html("0");
	$("#heartmap #speed").html("0.0");
	$("#heartmap #strides").html("0");
	faddingMessage("Zephyr sensor disconnected");
	//clearTimeout(hid);
	clearInterval(hid);
	
	
	
	}

	
}	



});


var hearttemp=0;
var id1;
$(document).on("pageshow","#heartmap",function(){



$(document).off('swiperight').on('swiperight', 'div[data-role="page"]', function(event){   
    if(event.handled !== true) // This will prevent event triggering more then once
    {      
        var prevpage = $(this).prev('div[data-role="page"]');
        if (prevpage.length > 0) {
            $.mobile.changePage("#heartmain", {transition: "flip", reverse: true}, true, true);
			
        }
        event.handled = true;
    }
    return false;            
});


function updategraph(){
	
//alert("cqc");

	     $('#chartContainer1').dxCircularGauge({
scale: {
		startValue: 50,
		endValue: 200,
		majorTick: {
		
			tickInterval: 5
		}
	},
	rangeContainer: {
		width:10,
		backgroundColor: '#727272',
		ranges: [
			{ startValue: 40, endValue: 60, color: "pink" },
			{ startValue: 60, endValue: 100, color: "#A6C567" },
			{ startValue: 100, endValue: 140, color: "#FFFA00" },
			{ startValue: 140, endValue: 180, color: "#FCBB69"  },
			{ startValue: 180, endValue: 240, color: "#FA6B63" }
		]
	},
	
	value: hearttemp,
	valueIndicator: {
		offset:-40,
		type: 'triangleNeedle',
		color: '#727272',
		width: 3,
		spindleGapSize:10,
		spindleSize: 30
		
	},
	subvalues:[hearttemp],
	subvalueIndicator:{
	type:'triangleMarker',
	color:'blue',
	offset:0
	}
});
	  
	  }


clearInterval(id1);
		id1=setInterval(updategraph,2000);




});