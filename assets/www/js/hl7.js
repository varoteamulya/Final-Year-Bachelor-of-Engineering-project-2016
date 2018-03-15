function getMSH()
{
	var d=new Date();
	var yr=d.getFullYear().toString();
	var m=0;
	m=d.getUTCMonth();
	m=m+1;
	m=m.toString();
	if(m.length==1)
	m="0"+m;
	var dt=d.getDate().toString();
	if(dt.length==1)
	dt="0"+dt;
	var hr=d.getHours();
	hr=hr.toString();
	if(hr.length==1)
	hr="0"+hr;
	var mn=d.getMinutes();
	mn=mn.toString();
	if(mn.length==1)
	mn="0"+mn;
	var sec=d.getSeconds();
	sec=sec.toString();
	if(sec.length==1)
	sec="0"+sec;
	var e=d.getTimezoneOffset().toString();
	var dval=yr.concat(m);
	dval=dval.concat(dt);
	dval=dval.concat(hr);
	dval=dval.concat(mn);
	dval=dval.concat(sec);
	dval=dval.concat(e);
	var MSH="MSH";
	MSH=MSH.concat("|^~\&|");
	MSH=MSH.concat("|mHealthApp||||");
	MSH=MSH.concat(dval);
	MSH=MSH.concat("||ADT^A01|");
	var rand=Math.floor(Math.random() * (99999 - 1 + 1)) + 1;
	rand=rand.toString();
	MSH=MSH.concat(rand);
	MSH=MSH.concat("|T|2.2|123");
	return MSH;
}

function getPID(id)
{
	var pid="PID";
	pid=pid.concat("|||||");
	pid=pid.concat(id);
	return pid;
}

function getOBX(id,val,arg,ch)
{
	var obx="OBX|";
	id=id.toString();
	obx=obx.concat(id);
	val=val.toString();
	obx=obx.concat("|CE|");
	obx=obx.concat(val);
	obx=obx.concat("|1|");
	arg=arg.toString();
	if(ch==1)
		obx=obx.concat("Name^");
	if(ch==2)
		obx=obx.concat("HeartRate^");
	if(ch==3)
		obx=obx.concat("BloodSugar^");
		if(ch==4)
		obx=obx.concat("BloodPressureSys^");
		if(ch==5)
		obx=obx.concat("BloodPressureDia^");
		if(ch==6)
		obx=obx.concat("BloodWeight^");
		if(ch==7)
		obx=obx.concat("BloodOxiHvalue^");
		if(ch==8)
		obx=obx.concat("BloodOxiSys^");
		if(ch==9)
		obx=obx.concat("BloodOxiDia^");
		if(ch==10)
		obx=obx.concat("UricAcid^");
		if(ch==11)
		obx=obx.concat("Cholesterol^");
		if(ch==12)
		obx=obx.concat("BloodPressurePulse^");
	obx=obx.concat(arg);
	return obx;	
}

function encodeToHl7(id,name,hr,bs,bp_sys,bp_dia,wt,ohr,obp_sys,obp_dia,uric,chol,bppulse)
{
	//alert("hl7 encoding");
	var MSH=getMSH();
	var PID=getPID(id);
	var obx_id=0;
	obx_id=obx_id+1;
	var OBX1=getOBX(obx_id,12345,name,1);
	obx_id=obx_id+1;
	var OBX2=getOBX(obx_id,23456,hr,2);
	obx_id=obx_id+1;
	var OBX3=getOBX(obx_id,34567,bs,3);
	obx_id=obx_id+1;
	var OBX4=getOBX(obx_id,45678,bp_sys,4);
	obx_id=obx_id+1;
	var OBX5=getOBX(obx_id,45678,bp_dia,5);
	obx_id=obx_id+1;
	var OBX6=getOBX(obx_id,56789,wt,6);
	obx_id=obx_id+1;
	var OBX7=getOBX(obx_id,67890,ohr,7);
	obx_id=obx_id+1;
	var OBX8=getOBX(obx_id,78901,obp_sys,8);
	obx_id=obx_id+1;
	var OBX9=getOBX(obx_id,89012,obp_dia,9);
	obx_id=obx_id+1;
	var OBX10=getOBX(obx_id,90123,uric,10);
	obx_id=obx_id+1;
	var OBX11=getOBX(obx_id,01234,chol,11);
	obx_id=obx_id+1;
	var OBX12=getOBX(obx_id,12345,bppulse,12);
	var Hl7Msg=MSH;
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(PID);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX1);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX2);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX3);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX4);
	Hl7Msg=Hl7Msg.concat("\n");
    Hl7Msg=Hl7Msg.concat(OBX5);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX6);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX7);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX8);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX9);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX10);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX11);
	Hl7Msg=Hl7Msg.concat("\n");
	Hl7Msg=Hl7Msg.concat(OBX12);
	Hl7Msg=Hl7Msg.concat("\n");
	//alert("hl7 ecoded message");
	//alert(Hl7Msg);
	return Hl7Msg;
}
