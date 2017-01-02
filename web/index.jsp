<%--
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software
 * except in compliance with the terms of the license at:
 * http://developer.sun.com/berkeley_license.html

 * author: tgiunipero
--%>


<%-- Set session-scoped variable to track the view user is coming from.
     This is used by the language mechanism in the Controller so that
     users view the same page when switching between English and Czech. --%>
<html>
<head><title>EKF Corporate bus monitoring</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<!-- Optional theme -->
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<script src="js/jquery-2.2.4.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="js/bootstrap.min.js"></script>
</head><body>
<c:set var='view' value='/index' scope='session' />
<h2>EKF Corporate bus monitoring</h2>
<h3>Logs</h3>
<!--<a href="#" onclick=" $('#main_logs').attr('src','http://185.31.208.212:8080/EKFGroupExchange/logs_all'); $(this).hide(); ">Show All</a>&nbsp;&nbsp;&nbsp;
<a href="#" onclick=" $('#main_logs').attr('src','http://185.31.208.212:8080/EKFGroupExchange/logs'); ">Ext</a>-->
<iframe id="main_logs" src="http://185.31.208.212:7071/EKFGroupExchange/logs" width="100%" height="100%" style="min-height: 600px;" frameborder="no"></iframe>
<!--<table width="100%" border="0"><tr><td>
    Critical Info
    <iframe src="http://185.31.208.212:7071/EKFGroupExchange/critical_info" width="95%" height="400" frameborder="no"></iframe>    
    </td><td>
    Critical Errors
    <iframe src="http://185.31.208.212:7071/EKFGroupExchange/critical_errs" width="95%" height="400" frameborder="no"></iframe>                     
    </td></tr>-->
</table>
</body></html>