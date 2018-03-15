	var DB_VERSION = "1.0";
	var DB_NAME = "Database";
	var DB_DISPLAY_NAME = "Cordova Demo";
	var DB_SIZE = 20000;
	var DEBUG=true;
	var db=window.openDatabase (DB_NAME, DB_VERSION, DB_DISPLAY_NAME, DB_SIZE);
	var valid=true;
	document.addEventListener("deviceready", onDeviceReady, false);
	function onConfirmQuit(button){}
	function onDeviceReady() {}
	var result_id=0;
	
	function calling()
	{	
		//alert("Opening Face Recognition..Please Wait...");
		db.transaction(searchForId, errorCB, successCB);
		calll();
	}

	function searchForId(tx) 
	{
		tx.executeSql('SELECT * FROM DETAILS_PATIENT WHERE id = (select max(id) FROM DETAILS_PATIENT)', [], querySuccessSearch, errorCB);
	}
			
	function querySuccessSearch(tx,results)
	{
		var len=results.rows.length;
		result_id=1;
	}
			
	function calll()
	{
		valid=true;
		var num=true;
		var mail = true;
		var pass=true;
		var fname="anand";
		var lname="biradar";
		var mname="shivu";
		var gender="Male";
		var gender1="Female";
		var dob="2015-07-07";
		var pcontactno="9999999999";
		var patemail="p2@ind.com";
		var patpass="123456";
		var patcpass="123456";
		var docname="chitra";
		var docemail="gdvtest@gdv.com.au";
		var docmob="1234567890";
		db.transaction(populate, errorCB, successCB);					
	}

	function successCB() {}
	function errorCB(err) 
	{
		if(DEBUG)
		console.log("Error processing SQL: "+err.code);
	}
		
	   

	function populate(tx) 
	{
		var fname="Anand";
		var lname="biradar";
		var mname="shivu";
		var dob="2015-07-07";
		var pcontactno="9999999999";
		var patemail="p2@ind.com";
		var patpass="123456";
		var docname="chitra";
		var docemail="gdvtest@gdv.com.au";
		var docmob="1234567890";
		var g="Male";
		window.localStorage.setItem("gender",g);
		tx.executeSql('INSERT INTO DETAILS_PATIENT ( fname, mname, lname, gender, dob, pcontactno, patemail, pwd, docname, docemail, docmob) VALUES ("'+fname+'", "'+mname+'", "'+lname+'", "'+g+'", "'+dob+'", "'+pcontactno+'", "'+patemail+'", "'+patpass+'", "'+docname+'", "'+docemail+'", "'+docmob+'")');
		window.localStorage.setItem("id",result_id);
		window.localStorage.setItem("patpass",patpass);	 
		window.localStorage.setItem("user_type","patient");
		pushPatCloud();
		document.location.href = "./PatientProfileView.html";
	}
		
	function onFail(message) 
	{
		if(DEBUG)
		console.log("Error processing: "+message);
	}

	function pushPatCloud()
	{
		var id=result_id;
		var fname="anand";
		var lname="biradar";
		var mname="shivu";
		var gender=window.localStorage.getItem("gender");			   
		var dob="2015-07-07";
		var pcontactno="9999999999";
		var patemail="p2@ind.com";
		var patpass="123456";
		var docname="chitra";
		var docemail="gdvtest@gdv.com.au";
		var docmob="1234567890";
		var message=fname+"::"+mname+"::"+lname+"::"+gender+"::"+dob+"::"+pcontactno+"::"+docemail+"::"+patemail+"::"+patpass+"::"+id;
		var default_level=3;
		var default_password="gdv007admin";
		jQuery.support.cors = true;
		//alert("Uploading Patient details to cloud..Please Wait...");
		var request = $.ajax({
			url: "http://mhealthcloud.elasticbeanstalk.com/createAdmin",
			type: "POST",
			cache : false,
			data: {txt_fName:docname,txt_uid:docemail,txt_mNumber:docmob,txt_level:default_level,txt_pswd:default_password},
			dataType: "html",
			async:false
		});


		request.done(function( msg ) {
			faddingMessage("Data Successfully pushed to Cloud!!");
		});

		request.fail(function( jqXHR, textStatus ) {
			faddingMessage("Sorry!!..Data could not be pushed");
		});

		jQuery.support.cors = true;
		//alert("Checking Doctor Details from cloud..Please Wait...");
					 var request = $.ajax({
					  url: "http://mhealthcloud.elasticbeanstalk.com/sign",
					  type: "POST",
					  cache : false,
					 data: {EmailId:patemail, content:message},
					  dataType: "html",
					  async:false
					});


					request.done(function( msg ) {
						faddingMessage("Doctor Validation Successfull");
					});

					request.fail(function( jqXHR, textStatus ) {
					  faddingMessage("Sorry!!..Doctor Validation Unsuccessfull");
					});
		}

	function faddingMessage(e)
	{
		$.mobile.showPageLoadingMsg("a",e,true);
		setTimeout($.mobile.hidePageLoadingMsg,2500);
	} 