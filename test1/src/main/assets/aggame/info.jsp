<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,java.text.SimpleDateFormat"%>
<%@page import="com.gw.util.Constant"%>
<%@page import="com.gw.product.config.init.Registry"%>
<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", -30);
%>
<%!String getDate()  { 
        Date now = new Date();
        now.setTime(now.getTime()-Integer.parseInt(Registry.getPeoperty(Constant.DSP_FIXHOUR))*60*60*1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
        return sdf.format(now); 
    }
%>
<%
String isGuest = request.getParameter("guest");
String BasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
String guestName = "";
if(isGuest!= null && isGuest.equals("0")) {
    //guestName = GuestManager.getGuest();
} else if (isGuest!= null && isGuest.equals("1")) {
    guestName = "demo";
} else {
    guestName = "live";
}
%>
{
	"LoginName":"<%=guestName%>",
	"UserName":"<%=session.getAttribute("username")%>",
	"Password":"<%=session.getAttribute("password")%>",
	"Website":"<%=session.getAttribute("website")%>",
	"Key":"<%=session.getAttribute("key")%>",
	"RemoteAddr":"<%=session.getAttribute("remoteIp")%>",
	"Vip":"<%=session.getAttribute("vip")%>",
	"Pid":"<%=session.getAttribute("pid")%>",
	"DomainName":"<%=session.getAttribute("dm")%>",
	"SeverTime":"<%=getDate()%>",
	"ServerName":"<%=BasePath%>",
	"LineIP":"<%=session.getAttribute("lineIP")%>",
	"CdnUrl":"<%=com.gw.product.config.init.Registry.getPeoperty("flash.server.domain")%>",
	"DirectType":"<%=session.getAttribute("gameType")%>",
    "DirectID":"<%=session.getAttribute("videoID")%>"
}
