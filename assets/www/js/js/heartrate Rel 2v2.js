$(document).on("pageshow","#heartrate",function(){

 //update profile

  var fname=window.localStorage.getItem("fname");
        var mname=window.localStorage.getItem("mname");
        var lname=window.localStorage.getItem("lname");
        var gender=window.localStorage.getItem("gender");
        var dob=window.localStorage.getItem("dob");
        var pcontactno=window.localStorage.getItem("pcontactno");
        var scontactno=window.localStorage.getItem("scontactno");
        var image=window.localStorage.getItem("image");

     $("#bloodpressurename").html(fname+" "+mname+" "+lname);
      $("#bloodpressure #gender").html(gender);
      $("#pulseoximeter #dob").html(dob);
      $("#pulseoximeter #pcontactno").html(pcontactno);
      $("#pulseoximeter #scontactno").html(scontactno);


      var dbImage1 = document.getElementById('myimagebloodpressure');
      dbImage1.style.display = 'block';
      dbImage1.src="data:image/jpeg;base64,"+window.localStorage.getItem("image");

function slideShow(){
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



//swipe
$(document).off('swipeleft').on('swipeleft', '#bloodpressure', function(event){
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



}


  var MaxHeight  = $("li").height();
  //$(".slider").height(MaxHeight+'px');
  //$(".slider").height(MaxHeight+'px');
  slideShow();


$(window).resize(function(){
  var MaxHeight  = $("li").height();

  $(".slider").height(MaxHeight+'px');

});




});

$(document).on("pageinit","#bloodpressure",function(){



      //imgdata1="data:image/jpeg;base64,"+results.rows.item(0).image;
  //  var dbImg= $('#myimage').val();
    //   dbImg.style.display = 'block';
  //   dbImg.src="data:image/jpeg;base64,"+window.localStorage.getItem("image");



  //
  //ECG var











  var bpcount=0;

  //foracare bp new
  $("#startheartrate").bind('click', function(){


  //$("#table1").table("refresh");
  //location.reload();
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

  setTimeout(BPnew, 1000);


  });




  function BPnew(){

  bpcount++;
  con3=window.CustomNativeAccess.connectFCnew();        // PU 19/6

    if(con3=="connected2new"){              // PU 19/6

    var packet=window.CustomNativeAccess.readBloodPressurenew();       // PU 19/6
    $.unblockUI();
    var n=packet.split("|");


    bpvaluesys=parseInt(n[0].trim());
    bpvaluedia=parseInt(n[1].trim());
    hvalue=parseInt(n[2].trim());

    if(bpvaluesys <= 120 && bpvaluedia <= 80 )
    {
    $("#bloodpressure #bloodpressurecategory").html("Normal");
    }
    else if((bpvaluesys <= 140 && bpvaluesys > 120) || (bpvaluedia <=90 && bpvaluedia > 80) )
    {
    $("#bloodpressuremap #bloodpressurecategory").html("Prehypertension");
    //$('#trcolor td').toggleClass("yellow-cell");
    }
    else if((bpvaluesys <= 160 && bpvaluesys > 140) || (bpvaluedia <=100 && bpvaluedia > 90) )
    {
    $("#bloodpressuremap #bloodpressurecategory").html("Hypertension, Stage 1");
    }
    else if(bpvaluesys > 160 ||  bpvaluedia > 100 )
    {
    $("#bloodpressuremap #bloodpressurecategory").html("Hypertension, Stage 2");
    }
    else{
    $("#bloodpressuremap #bloodpressurecategory").html("Measure Again");
    }
    //$("#FC_BP").html(" "+n[0].trim()+" SYS/"+" "+n[1].trim()+" DIA");
    //$("#bpcategory").html("Normal");
    $("#bloodpressuremap #bloodpressuresys").html(n[0].trim());
    $("#bloodpressuremap #bloodpressuredia").html(n[1].trim());
    $("#bloodpressuremap #bloodpressurepulse").html(n[2].trim());

    var c=$("#bloodpressurecloud option:selected").text();
    if(c=="Yes"){

    pushtocloud();
    }
  //  document.location.href="#map";
    //alert("Measurments have been taken....Please turn off the sensor");
  //$('#bpmsg' ).popup('open');

      $.unblockUI();
      $.mobile.changePage( "#bloodpressuremap", { transition: "slideup", changeHash: false });
    }
    else{

    if(bpcount<30){

    //alert("der"+bpcount);
    setTimeout(BPnew, 2000);    // PU changed from 200
    }
    else{

    alert("connection failed");
    $.unblockUI();
    bpvaluesys=0;
    bpvaluedia=0;
    hvalue=0;
    con3=0;
    }
    }


}

function pushtocloud(){

 //var name=document.getElementById("namemain").innerHTML;

var name=$('#bloodpressurename').html();

 var username=window.localStorage.getItem("fname");
var hl7Msg=encodeToHl7(username,name,hvalue,svalue,bpvaluesys,bpvaluedia);

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


// var encodedmsg=window.CustomNativeAccess.Encode("ak123","Akhil","Naik",134,123);
 //alert(encodedmsg);
 jQuery.support.cors = true;
 var request = $.ajax({
  url: "http://hiwicloud.appspot.com/sign2",
  type: "POST",
 data: { guestbookName: "default", Name:username, content:hl7Msg},
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
  }else{
   faddingMessage("Invalid Data");

  }
//



}



});
//end



$(document).on("pageshow","#bloodpressuremap",function(){




$(document).off('swiperight').on('swiperight', 'div[data-role="page"]', function(event){
    if(event.handled !== true) // This will prevent event triggering more then once
    {
        var prevpage = $(this).prev('div[data-role="page"]');
        if (prevpage.length > 0) {
            $.mobile.changePage(prevpage, {transition: "flip", reverse: true}, true, true);

        }
        event.handled = true;
    }
    return false;
});


////end of swipe

function updategraph(){

       $('#chartContainer').dxCircularGauge({
scale: {
    startValue: 50,
    endValue: 240,
    majorTick: {

      tickInterval: 10
    }
  },
  rangeContainer: {
    width:6,
    backgroundColor: '#727272',
    ranges: [
      { startValue: 50, endValue: 90, color: "pink" },
      { startValue: 90, endValue: 140, color: "#A6C567" },
      { startValue: 140, endValue: 210, color: "#FCBB69"  },
      { startValue: 210, endValue: 240, color: "#FA6B63" }
    ]
  },

  value: bpvaluesys,
  valueIndicator: {
    type: 'rectangleNeedle',
    color: '#727272',
    width: 3,
    spindleGapSize:5,
    spindleSize: 10,
    offset:0
  },
  subvalues:[bpvaluesys],
  subvalueIndicator:{
  type:'textCloud',
  color:'#727272',
  offset:0
  }
});
$('#chartCon1').dxCircularGauge({
scale: {
    startValue: 10,
    endValue: 140,
    majorTick: {
      tickInterval: 5
    }
  },
  rangeContainer: {
  width:7,
    backgroundColor: '#727272',
    ranges: [
      { startValue: 35, endValue: 60, color: "pink" },
      { startValue: 60, endValue: 90, color: "#A6C567" },
      { startValue: 90, endValue: 120, color: "#FCBB69"  },
      { startValue: 120, endValue: 140, color: "#FA6B63" }
    ]
  },

  geometry: { startAngle: 360, endAngle: 0 },

  value: bpvaluedia,
  valueIndicator: {
    type: 'rectangleNeedle',
    color: '#727272',
    width: 3,
    spindleGapSize:5,
    spindleSize: 10,
    offset:0
  },
  subvalues:[bpvaluedia],
  subvalueIndicator:{
  type:'textCloud',
  color:'#727272',
   offset:0
  }
});

    }



//swipe


setTimeout(updategraph ,200);


////end of swipe
//page change
/*
$(document).delegate('.ui-page', 'pageshow', function () {

if($.mobile.activePage.attr('id')=="map"){

    updategraph();
    //setInterval(updategraph,500);
    setTimeout(updategraph ,200);
    }


});
*/




});
//end