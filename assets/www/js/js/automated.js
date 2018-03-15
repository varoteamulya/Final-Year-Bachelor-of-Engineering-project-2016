 var con30 = 0;
 
function clearBPTimer(){
   con30=window.CustomNativeAccess.BLdisconnect();      // Added PU 04/02/15
}

 //update profile

        var id=window.localStorage.getItem("id");                  // Added by PU 10/02/15
        var fname=window.localStorage.getItem("fname");
        var mname=window.localStorage.getItem("mname");
        var lname=window.localStorage.getItem("lname");
        var gender=window.localStorage.getItem("gender");
        var dob=window.localStorage.getItem("dob");
        var pcontactno=window.localStorage.getItem("pcontactno");

     	var name=(fname+" "+mname+" "+lname);
     	var username=window.localStorage.getItem("patemail");

 alert("BP fname "+ name);
 alert("BP username "+ username);


 $(document).ready(function(){ 
	var ii,jj=0,kk=0;
	var con31 = 0;
	var con32 = 0;
	var con33 = 0;
	var	con3=0;
	var bpcountscale=0;
	var svalue=0;
	var hvalue=0;
	var bppulse=0;
	var cholest=0;
	var uri=0;                                   // By PU 8 Sept'14
	var bpcount=0;
	var weight=0,oxihvalue=0,oxibpvaluesys=0,oxibpvaluedia=0;
	var bpvaluesys=0;
	var bpvaluedia=0;
	//polling();
	startScan();
	
	$("#autoo").bind('click', function(){

		polling();
	});  
	
	$("#cloud").bind('click', function(){

		
		polling2();
	});
	
	$("#cloud1").bind('click', function(){

		alert("BP Device already paired");
		polling1();
	});
//======================================Polling===========================	
	function startScan(){
		var gga=window.CustomNativeAccess.startBP();
		//var gga=window.CustomNativeAccess.startBLEScan();
		//alert(gga);
	}
	
	
	function polling1(){
		var gga=window.CustomNativeAccess.callBP();
		//alert(gga);
	}
	
	function polling2(){
		var gga=window.CustomNativeAccess.callSPO2();
		//alert(gga);
	}
	
	function polling()
	{
		con31=="0";

		faddingMessage1("Polling BT Devices");

		//alert("Loop Start "+ con31);
		/*
		con31=window.CustomNativeAccess.connectpolling();
		if (con31 == "Device Connected")
		{															pu 28Aug15 */
		con31=window.CustomNativeAccess.connectpolling();

		//alert("OXY is Polling "+ con31);
		if(con31=="connectedOxy")
		{
			//alert("OXY is Called "+ con31);
			setTimeout(BPnewoxy, 200);
			//alert("OXY is finished "+ con31);
			con31=="Not Connected";
		}
		else if(con31=="connectedBP")
		{
			//alert("BP is Called "+ con31);
			setTimeout(BPnew, 200);
			//alert("BP is finished "+ con31);
			con31=="Not Connected";
		}

		else if(con31=="connectedBS")
		{
			setTimeout(BSnew, 200 );
		//	setTimeout(Cholnew, 50000);
			//setTimeout(Uricnew, 100000);
			//alert("ETB-01-0047F6 is called "+ con31);
			con31=="Not Connected";
		}

		else if(con31=="connectedWT")
		{
			//alert("Scale is called "+ con31);
			setTimeout(BPnewscale, 200);
			//alert("OXY is called "+ con31);
			con31=="Not Connected";
		}
		// PU 28Aug15}
		
		else
		{
		
			//alert("Loop End "+ con31);
			con31=window.CustomNativeAccess.readresetpolling();
			//alert("Loop End "+ con31);
			setTimeout(polling, 400);
		}
	}
	
		

	function Weight_Call()
	{
		$.blockUI({message:"<h1>Connecting to Weight Sensor..<h1>",
		css: {
            border: 'none',
            padding: '15px',
            backgroundColor: '#000',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .5,
            color: '#fff'
        } });
		con30=window.CustomNativeAccess.connectFCnewScale();
		BPnewscale();
	}
	
		//BP-------------------
	
	function test_BP_now()
	{
		con3=0;
		$.blockUI({message:"<h1>Connecting to BloodPressure Sensor..<h1>",
		css: {
            border: 'none',
            padding: '15px',
            backgroundColor: '#000',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .5,
            color: '#fff'
        } });
		BPnew();
		//con30=0;
	}
	
	 
	
	function BPnew()
	{
		bpcount++;
		
		con3=window.CustomNativeAccess.connectFCnew();
		
		if(con3=="connectedBP")
		{
			var packet=window.CustomNativeAccess.readBloodPressurenew();       // PU 19/6
			//$.unblockUI();
			var n=packet.split("|");
			bpvaluesys=parseInt(n[0].trim());
			bpvaluedia=parseInt(n[1].trim());
			bppulse=parseInt(n[2].trim());
			if(bpvaluesys <= 120 && bpvaluedia <= 80 )
			{
   
			}
			else if((bpvaluesys <= 140 && bpvaluesys > 120) || (bpvaluedia <=90 && bpvaluedia > 80) )
			{
			
			}
			else if((bpvaluesys <= 160 && bpvaluesys > 140) || (bpvaluedia <=100 && bpvaluedia > 90) )
			{
			
			}
			else if(bpvaluesys > 160 ||  bpvaluedia > 100 )
			{
			
			}
			else{
			
			}
			$.unblockUI();
			
			pushtocloud_BP();
			bpcount=0;
			bpvaluesys=0;
			bpvaluedia=0;
			hvalue=0;
			con30=0;
			con3=0;			 
		}
		else
		{
			if(bpcount<60)
			{
				setTimeout(BPnew, 1500);    // PU changed from 200
			}
			else
			{
				//con30=window.CustomNativeAccess.BLdisconnect();
				bpcount=0;
				kk=0;
				if(kk>2)
					alert("BloodPressure sensor is not Enabled");
				$.unblockUI();
				bpvaluesys=0;
				bpvaluedia=0;
				hvalue=0;
				con30=0;
			}
		}
	}
	
	function pushtocloud_BP()
	{
		//var name=document.getElementById("namemain").innerHTML;
		//alert("push to cloud");
		//var name="p7 u7";
		//alert("BP fname "+ name); 
		
		//alert("BP Username "+ username);
		var hl7Msg=encodeToHl7(username,name,hvalue,svalue,bpvaluesys,bpvaluedia,weight,oxihvalue,oxibpvaluesys,oxibpvaluedia,uri,cholest,bppulse);
		//alert("encoded");
		//alert("encoded");
		var sensor_type="BP";
		var sensor_value=hvalue+":"+svalue+":"+bpvaluesys+":"+bpvaluedia+":"+bppulse+":"+weight+":"+oxihvalue+":"+oxibpvaluesys+":"+oxibpvaluedia+":"+uri+":"+cholest;
		//alert(sensor_value);
		if(!((hvalue<5 || hvalue>220)&&(bpvaluesys<50 || bpvaluesys>280)&&(bpvaluedia<10 || bpvaluedia>150)))
		{
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

				jQuery.support.cors = true;
				var request = $.ajax({
					url: "http://mhealthcloud.elasticbeanstalk.com/PushHealthdata",
					type: "POST",
					data: {content:hl7Msg,Email:username},
					dataType: "html"
					//  async:false
				});

				request.done(function( msg ) {
					$.unblockUI();
					//alert("Data Successfully pushed to Cloud!!");
					faddingMessage("Data Successfully pushed to Cloud!!");
					return;
				});

				request.fail(function( jqXHR, textStatus ) 
				{
					$.unblockUI();
					//alert( "Sorry!!..Data could not be pushed");
					faddingMessage("Sorry!!..Data could not be pushed");
					return;
				});
			}
			else
			{
				//alert("You are offline..turn on internet");
				faddingMessage("You are offline..turn on internet");
				$.unblockUI();
				return;
			}
			return;
		}
		
		else
		{
			faddingMessage("Invalid Data");
			$.unblockUI();
		}
		//con30=window.CustomNativeAccess.BLdisconnect();
		$.unblockUI();
		return;
	}
	
//Blood Sugar -----------------

  function BSnew()
  {
	bpcount++;
	con3=window.CustomNativeAccess.connectFC();
    if(con3=="connected1")
    {
		    var packet=window.CustomNativeAccess.readBloodSugar();
		    $.unblockUI();
		    svalue1=parseInt(packet);
		    svalue2=svalue1/18.0182;
		    svalue2=svalue2.toFixed(2);
		    svalue=svalue2;
		    pushtocloud_glucose();
			bpcount=0;
		    $.unblockUI();
		    bpvaluesys=0;
		    bpvaluedia=0;
		    svalue=0;
		    hvalue=0;
		    con31=0;
		    con3=0;
		    
		    
		    
		    
		    //polling();					// Vijay 02/3/2016
		    
		    
		    
    }
    else
	{
	    if(bpcount<10)
		{
		    //alert("der"+bpcount);
		    setTimeout(BSnew, 1500);    // PU changed from 200
		    }
		    else{
		    con3=window.CustomNativeAccess.BLdisconnect();
		    bpcount=0;
		    alert("connection failed");
		    $.unblockUI();
		    bpvaluesys=0;
		    bpvaluedia=0;
		    hvalue=0;
		    con3=0;
		    
		    
		    
		    //polling();					// Vijay 02/3/2016
		    
		    
		    
    		}
    	}
}


 function Cholnew()
 {
	var bpcount=0,svalue1=0,cholest=0,bpvaluesys=0,bpvaluedia=0,hvalue=0;
	bpcount++;
	var con3=window.CustomNativeAccess.connectFCchol();        // PU 29/10
    if(con3=="connected2")
	{         
		var packet=window.CustomNativeAccess.readBloodchol();       // PU 29/10
		svalue1=parseInt(packet);
		cholest=svalue1/18.0182;
		cholest=cholest.toFixed(2);
	}
    else{
    if(bpcount<10){
    setTimeout(Cholnew, 3000);    // PU changed from 200
    }
    else
	{
		con3=window.CustomNativeAccess.BLdisconnect();
		bpcount=0;
		alert("connection failed");
		$.unblockUI();
		bpvaluesys=0;
		bpvaluedia=0;
		hvalue=0;
		con3=0;
    }
    }
}

 function Uricnew()
 {
	var bpcount=0,svalue1=0,svalue2=0,bpvaluesys=0,bpvaluedia=0,hvalue=0;
	bpcount++;
	var con3=window.CustomNativeAccess.connectFCuric();        // PU 19/6
    if(con3=="connected3"){              // PU 19/6
    var packet=window.CustomNativeAccess.readBlooduric();       // PU 19/6
    svalue1=parseInt(packet);
    svalue2=svalue1/59.48;
    svalue2=svalue2.toFixed(2);
    }
    else{
    if(bpcount<10)
	{
		setTimeout(Uricnew, 3000);    // PU changed from 200
    }
    else
	{
    con3=window.CustomNativeAccess.BLdisconnect();
    bpcount=0;
    alert("connection failed");
    $.unblockUI();
    bpvaluesys=0;
    bpvaluedia=0;
    hvalue=0;
    con3=0;
    }
    }
}


function pushtocloud_glucose(){
	//var name=window.localStorage.getItem("fname");
	//var username=window.localStorage.getItem("patemail");
	var hl7Msg=encodeToHl7(username,name,hvalue,svalue,bpvaluesys,bpvaluedia,weight,oxihvalue,oxibpvaluesys,oxibpvaluedia,uri,cholest,bppulse);
	var sensor_type="Blood Glucose";
	var sensor_value=hvalue+":"+svalue+":"+bpvaluesys+":"+bpvaluedia+":"+bppulse+":"+weight+":"+oxihvalue+":"+oxibpvaluesys+":"+oxibpvaluedia+":"+uri+":"+cholest;
	
	if(window.onLine==true)
	{
		//alert("Entered C= "+svalue);
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

		jQuery.support.cors = true;
		var request = $.ajax({
			url: "http://mhealthcloud.elasticbeanstalk.com/PushHealthdata",
			type: "POST",
			data: {content:hl7Msg,Email:username},
			dataType: "html"
		});
		request.done(function( msg ) 
		{
			$.unblockUI();
			 
			faddingMessage("Data Successfully pushed to Cloud!!");
			 return 0;
			});

	request.fail(function( jqXHR, textStatus ) 
	{
		$.unblockUI();
		 
		faddingMessage("Sorry!!..Data could not be pushed_PulseOxy");
	});
  }
  else{
	faddingMessage("You are offline..turn on internet");
	$.unblockUI();
  }
  //con30=window.CustomNativeAccess.BLdisconnect();
 $.unblockUI();
   
 }
	
//Oxy--------------
	function BPnewoxy()
{
	bpcount++;
	// con30=window.CustomNativeAccess.connectFCnewOxy();  	// PU 19/6
	if(con31=="connectedOxy")
	{
		//faddingMessage("Connecting to Oxy");
		var packet=window.CustomNativeAccess.readBloodPressurenewOxy();       // PU 19/6
		$.unblockUI();
		var n=packet.split("|");

		oxibpvaluesys=parseInt(n[0].trim());
		oxibpvaluedia=parseInt(n[1].trim());
		oxihvalue=parseInt(n[2].trim());
		oxibpvaluesys=10;      // Added by PU 15 NOV'14
		var output = "";
		output = "Val1:"+oxibpvaluesys+" Val2:"+oxibpvaluedia+" Val3: "+oxihvalue;
		var data = n[1].trim();
		//setPercentValue(data+"");
		pushtocloud_oxy();
		$.unblockUI();
		bpcount=0;
		oxibpvaluesys=0;
		oxibpvaluedia=0;
		oxihvalue=0;
		con30=0;
		con3=0;
	}
    else
	{
		if(bpcount<03){
			 setTimeout(BPnewoxy, 1000);    // PU changed from 200
		}
		else
		{
			//con30=window.CustomNativeAccess.BLdisconnect();
			
			$.unblockUI();
			oxibpvaluesys=0;
			oxibpvaluedia=0;
			oxihvalue=0;
			con30=0;
			//Weight_Call();
		}
    }
}

function faddingMessage1(e)
{
	$.mobile.showPageLoadingMsg("a",e,true);
	setTimeout($.mobile.hidePageLoadingMsg,2000);
} 

function faddingMessage(e)
{
	$.mobile.showPageLoadingMsg("a",e,true);
	setTimeout($.mobile.hidePageLoadingMsg,2500);
} 

function pushtocloud_oxy(){
	//var name=window.localStorage.getItem("fname");
	//var name="p7 u7";
	//alert("OXY fname "+ name); 
	//var username=window.localStorage.getItem("patemail");
	//alert("OXY Username "+ username);
	var hl7Msg=encodeToHl7(username,name,hvalue,svalue,bpvaluesys,bpvaluedia,weight,oxihvalue,oxibpvaluesys,oxibpvaluedia,uri,cholest,bppulse);
	var sensor_type="Oximeter";
	var sensor_value=hvalue+":"+svalue+":"+bpvaluesys+":"+bpvaluedia+":"+bppulse+":"+weight+":"+oxihvalue+":"+oxibpvaluesys+":"+oxibpvaluedia+":"+uri+":"+cholest;
	
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

		jQuery.support.cors = true;
		var request = $.ajax({
			url: "http://mhealthcloud.elasticbeanstalk.com/PushHealthdata",
			type: "POST",
			data: {content:hl7Msg,Email:username},
			dataType: "html"
		});
		request.done(function( msg ) 
		{
			$.unblockUI();
			 
			faddingMessage("Data Successfully pushed to Cloud!!");
			 return 0;
			});

	request.fail(function( jqXHR, textStatus ) 
	{
		$.unblockUI();
		 
		faddingMessage("Sorry!!..Data could not be pushed_PulseOxy");
	});
  }
  else{
	faddingMessage("You are offline..turn on internet");
	$.unblockUI();
  }
  con30=window.CustomNativeAccess.BLdisconnect();
 $.unblockUI();
   
 }


function BPnewscale(){
  bpcount++;
  con30=window.CustomNativeAccess.connectFCnewScale();        // PU 19/6
       if(con30=="connectedWT"){              // PU 19/6
    var packet=window.CustomNativeAccess.readBloodPressurenewScale();       // PU 19/6
    //$.unblockUI();
        // By PU 8 Sept'14

    var dataw = packet.split(".");

    weight1=parseInt(dataw[0].trim());        // By PU 8 Sept'14
    weight2=parseInt(dataw[1].trim());        // By PU 8 Sept'14
    con30=0;                                   // By PU 8 Sept'14
	weight=parseFloat(packet);
	//setWeightValue(weight1,weight2);          //data[0],data[1] PU 12 Sept'14
	pushtocloud_weigh();
	$.unblockUI();
	bpcount=0;
	bpvaluesys=0;
	bpvaluedia=0;
	hvalue=0;
	con30=0;
	weight=0;
	con3=0;

	//return;
    }
    else{

    if(bpcount<3)
    	{     
		setTimeout(BPnewscale, 1000);    // PU changed from 200
    	}
	else
		{

				alert("Weight sensor is not enabled");
				$.unblockUI();
				bpvaluesys=0;
				bpvaluedia=0;
				hvalue=0;
				con30=0;
				weight=0;
				//test_BP_now();
				//return;
			
		}
    }


}

	function pushtocloud_weigh()
	{
		 
		//var name=window.localStorage.getItem("fname");
		//var username=window.localStorage.getItem("patemail");
		var hl7Msg=encodeToHl7(username,name,hvalue,svalue,bpvaluesys,bpvaluedia,weight,oxihvalue,oxibpvaluesys,oxibpvaluedia,uri,cholest,bppulse);
		var sensor_type="Body Weight";
		var sensor_value=hvalue+":"+svalue+":"+bpvaluesys+":"+bpvaluedia+":"+bppulse+":"+weight+":"+oxihvalue+":"+oxibpvaluesys+":"+oxibpvaluedia+":"+uri+":"+cholest;
		$.unblockUI();
		if(window.onLine==true)
		{			 
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

			 			jQuery.support.cors = true;
			var request = $.ajax({
				url: "http://mhealthcloud.elasticbeanstalk.com/PushHealthdata",
				type: "POST",
				data: {content:hl7Msg,Email:username},
				dataType: "html"
			});
			request.done(function( msg ) 
			{
				$.unblockUI();
			 	faddingMessage("Data Successfully pushed to Cloud!!");
				return;
			});
			request.fail(function( jqXHR, textStatus ) 
			{
				$.unblockUI();
				faddingMessage("Sorry!!..Data could not be pushed_Weight");
				return;
			});
		}
		else
		{
			faddingMessage("You are offline..turn on internet");
			$.unblockUI();
			return;
		}	 
		return;
	}
});
