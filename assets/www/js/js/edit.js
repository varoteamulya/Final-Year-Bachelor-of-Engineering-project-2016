		
$(document).on("pageshow","#edit",function(){

	alert("edit");
		var DB_VERSION = "1.0";
		var DB_NAME = "Database";
		var DB_DISPLAY_NAME = "Cordova Demo";
		var DB_SIZE = 20000;
		var DEBUG=true;
		var db=window.openDatabase (DB_NAME, DB_VERSION, DB_DISPLAY_NAME, DB_SIZE);
	
		var imgdata;
			alert("edi2t");
		$("#imgsubmit").bind('click', function(){
		// Take picture using device camera and retrieve image as base64-encoded string
		navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 10,
		destinationType: destinationType.DATA_URL });
		});
		 
    var pictureSource;   // picture source
    var destinationType; // sets the format of returned value 
	

	alert("edi3t");
		//	pictureSource=navigator.camera.PictureSourceType;
		//	destinationType=navigator.camera.DestinationType;


	
		
		// Called when a photo is successfully retrieved
		//
		function onPhotoDataSuccess(imageData) {
		  // Uncomment to view the base64 encoded image data
		  // console.log(imageData);

		  // Get image handle
		  //
		  var smallImage = document.getElementById('myimage');

		  // Unhide image elements
		  //
		  smallImage.style.display = 'block';

		  // Show the captured photo
		  // The inline CSS rules are used to resize the image
		  //
		  smallImage.src = "data:image/jpeg;base64," + imageData;
		  //   imgdata=b64toBlob(imageData, "image/jpeg",8);
		  imgdata=imageData;
		  //database 
		}
			alert("edi4t");
		
		function onFail(message) {
			//    alert('Failed because: ' + message);
		}
	
		alert("edi5t");
     $("#edit_submit").bind('click', function(){
    
     
       //var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);
       valid=true;
      //document.getElementById('fname').value=fname;
       // document.getElementById('mname').value=mname;
       // document.getElementById('lname').value=lname;
      //  document.getElementById('gender').value=gender;
      //  document.getElementById('dob').value=dob;
      //  document.getElementById('pcontactno').value=pcontactno;
      //  document.getElementById('scontactno').value=scontactno;
     //  	var dbImage = document.getElementById('myimage');
				var num=true;
       			var emailp=window.localStorage.getItem("email");
			   	var passwp=window.localStorage.getItem("password");
			   	var fname=document.getElementById('fname').value
        		var mname=document.getElementById('mname').value;
        		var lname=document.getElementById('lname').value;
				//var gender=document.getElementById('gender').value;
				
        		var gender=window.localStorage.getItem("gender");
        		var dob=document.getElementById('dob').value;
        		var pcontactno=document.getElementById('pcontactno').value;
        		var scontactno=document.getElementById('scontactno').value;
				var preg= new RegExp("^[0-9]+$","g");
			if(!pcontactno.match(preg))
			  {num=false;
			  }
			   var sreg= new RegExp("^[0-9]+$");
			   if(!scontactno.match(sreg))
			   //else
			   {num=false;
			  }
				if(emailp=="") { valid=false;}
			   if(passwp=="") {valid=false;}
			   if(fname=="") {valid=false;document.getElementById("fnmark").style.visibility = "visible";} 
			   else document.getElementById("fnmark").style.visibility = "hidden";
			  
			   if(lname=="") {valid=false;document.getElementById("lnmark").style.visibility = "visible";}
			   else
			   document.getElementById("lnmark").style.visibility = "hidden";
			   if(gender=="") {valid=false;}
			   if(dob=="") {valid=false;document.getElementById("dobmark").style.visibility = "visible";}
			   else
			   document.getElementById("dobmark").style.visibility = "hidden";
			   if(pcontactno=="") {num=false;document.getElementById("pcnomark").style.visibility = "visible";}
			   else
			   document.getElementById("pcnomark").style.visibility = "hidden";
			   if(scontactno=="") { num=false;document.getElementById("scnomark").style.visibility = "visible";}
			   else
			   document.getElementById("scnomark").style.visibility = "hidden";
			   if(valid==true && num==true)
       db.transaction(updatepatient, errorCB, successCB);
       else if(valid==true && num==false)
			{alert("Contact number should have digits");}
			else if(valid==false && num==false)
			alert("Fill all the mandatory fields or check Contact number");
			else
			alert("Fill all the mandatory fields");
		
     });
     

     function startedit() {
         var fname=window.localStorage.getItem("fname");
        var mname=window.localStorage.getItem("mname");
        var lname=window.localStorage.getItem("lname");
        var gender=window.localStorage.getItem("gender");
        var dob=window.localStorage.getItem("dob");
        var pcontactno=window.localStorage.getItem("pcontactno");
        var scontactno=window.localStorage.getItem("scontactno");
        var image=window.localStorage.getItem("image"); 
        imgdata=image;
        // $("#fname").html(fname);
        document.getElementById('fname').value=fname;
        document.getElementById('mname').value=mname;
        document.getElementById('lname').value=lname;
        if(gender=="Male") {
        $('#gender').attr('checked',true).checkboxradio("refresh");
        }
        else {
       $('#gender1').attr('checked',true).checkboxradio("refresh");}
	    var g;
			   if(document.getElementById('gender').checked) {
 					g=document.getElementById('gender').value;
				}else if(document.getElementById('gender1').checked) {
 					g=document.getElementById('gender1').value;
				}
        //document.getElementById('gender').value=gender;
        document.getElementById('dob').value=dob;
        document.getElementById('pcontactno').value=pcontactno;
        document.getElementById('scontactno').value=scontactno;
       	var dbImg = document.getElementById('myimage');
        dbImg.style.display = 'block';
        dbImg.src="data:image/jpeg;base64,"+window.localStorage.getItem("image");
    
    
     }
  	alert("edi6t");
     function updatepatient(tx) {
       var emailp=window.localStorage.getItem("email");
       var passwp=window.localStorage.getItem("password");
       var fname=document.getElementById('fname').value;
       var lname=document.getElementById('lname').value;
       var mname=document.getElementById('mname').value;
        var g;
		if(document.getElementById('gender').checked) {
 		g=document.getElementById('gender').value;
		}else if(document.getElementById('gender1').checked) {
 		g=document.getElementById('gender1').value;
		}
       var dob=document.getElementById('dob').value;
       var pcontactno=document.getElementById('pcontactno').value;
       var scontactno=document.getElementById('scontactno').value;
       tx.executeSql('UPDATE DETAILS SET image="'+imgdata+'",fname="'+fname+'", mname="'+mname+'", lname="'+lname+'", gender="'+g+'", dob="'+dob+'", pcontactno="'+pcontactno+'", scontactno="'+scontactno+'" WHERE emailid="'+emailp+'"');
      }
        
     function successCB() {
       // var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);
        db.transaction(queryDB, errorCB);
     }
    
     function queryDB(tx) {
        var email=window.localStorage.getItem("email");
        tx.executeSql('SELECT * FROM DETAILS where emailid="'+email+'"', [], querySuccess, errorCB);
     }
    
    
	 function errorCB(err) {
        if(DEBUG)
      console.log("Error processing SQL: "+err.code);
     }
    
      
    
     function querySuccess(tx, results) {
        var len = results.rows.length;
        if(len==0)
        alert("You have not signed up!, please sign up!");
        else
        {
			 var email;
			 var passwrd;
			 email=results.rows.item(0).emailid;
			 fn=results.rows.item(0).fname;
			 ln=results.rows.item(0).lname;
			 var gender=results.rows.item(0).gender;
			 var dob=results.rows.item(0).dob;
			 var pcontactno=results.rows.item(0).pcontactno;
			 var scontactno=results.rows.item(0).scontactno;
			 var mn=results.rows.item(0).mname;
			 passwrd=results.rows.item(0).password;     
			 window.localStorage.setItem("email",email);
			 window.localStorage.setItem("password",passwrd);
			 window.localStorage.setItem("fname",fn);
			 window.localStorage.setItem("mname",mn);
			 window.localStorage.setItem("lname",ln);
			 window.localStorage.setItem("gender",gender);
			 window.localStorage.setItem("dob",dob);
			 window.localStorage.setItem("pcontactno",pcontactno);
			 window.localStorage.setItem("scontactno",scontactno);
			  document.getElementById("fnmark").style.visibility = "hidden";
			  document.getElementById("mnmark").style.visibility = "hidden";
			  document.getElementById("lnmark").style.visibility = "hidden";
			  document.getElementById("dobmark").style.visibility = "hidden";
			  document.getElementById("pcnomark").style.visibility = "hidden";
			  document.getElementById("scnomark").style.visibility = "hidden";
			 alert("Edit Successful..");
			document.location.href = "./PatientProfileView.html";
         
        }
     }
	alert("edi6t");
	 function success(){  
     //  alert("sucess");
     }
	//startedit();
  alert("e1dit");
 });