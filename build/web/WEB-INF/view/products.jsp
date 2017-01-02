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
<c:set var="view" value="/products" scope="session" />


<%-- HTML markup starts below --%>

    {[
    <c:forEach var="product" items="${products}">

        {id:'${product.id}',vendor_code:'${product.vendorCode}',name:'${product.name}',short_name:'${product.shortName}',},

    </c:forEach>
    ]}
