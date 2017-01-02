<%--
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software
 * except in compliance with the terms of the license at:
 * http://developer.sun.com/berkeley_license.html

 * author: Poltarokov SP
--%>


<%-- HTML markup starts below --%>
<c:set var="view" value="/pramounts" scope="session"/>
{[
    <c:forEach var="prreceipt" items="${prreceipts}">
        {id:'${prreceipt.id}',quantity:${prreceipt.quantity},state:'${prreceipt.state}',date:'${prreceipt.date}',prod_id:'${prreceipt.productId}',whs_id:'${prreceipt.warehouseId}',},
    </c:forEach>
    ]}