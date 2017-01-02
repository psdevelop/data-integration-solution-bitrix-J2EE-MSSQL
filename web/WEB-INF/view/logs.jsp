<%--
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software
 * except in compliance with the terms of the license at:
 * http://developer.sun.com/berkeley_license.html

 * author: Poltarokov SP
--%>


<%-- Set session-scoped variable to track the view user is coming from.
     This is used by the language mechanism in the Controller so that
     users view the same page when switching between English and Czech. --%>
<html>
<head><title>EKF Corporate bus monitoring</title> 
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
</head><body>
<c:set var='view' value='/warehouses' scope='session' />
    <table class="table">
        <thead><th>#</th><th>When</th><th>Type</th><th>Title</th><th>What`s up</th></thead>
    <c:forEach var="log" items="${logs}">
        <tr><td style="width:40px;font-size: 12px !important;">${log.id} </td>
            <td style="width:220px;font-size: 12px !important;"> ${log.dt} </td>
            <td style="width:25px;font-size: 12px !important;"> ${log.logType} </td>
            <td style="width:190px;font-size: 12px !important;"> ${log.caption} </td>
            <td style="width:580px;font-size: 12px !important;"> ${log.logText} </td>
    </c:forEach>
    </table>
</body></html>