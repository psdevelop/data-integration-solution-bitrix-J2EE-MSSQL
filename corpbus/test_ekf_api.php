<?require($_SERVER["DOCUMENT_ROOT"]."/bitrix/header.php");
$APPLICATION->SetTitle("Testing EKF Api:");
?>
Testing EKF Api:
пример вызова 
<pre>
       $.getJSON('http://192.168.50.200:8080/EKFApi/warehouses?key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test').html('error');
       });
</pre>
<div id="ekf_api_test" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://192.168.50.200:8080/EKFApi/products?pnum=1&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test2').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test2').html('error');
       });
</pre>
<div id="ekf_api_test2" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://192.168.50.200:8080/EKFApi/pramounts?pnum=5&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test3').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test3').html('error');
       });
</pre>
<div id="ekf_api_test3" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://192.168.50.200:8080/EKFApi/prreceipts?pnum=10&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test4').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test4').html('error');
       });
</pre>
<div id="ekf_api_test4" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://192.168.50.200:8080/EKFIMSApi/qb?key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6&partner_id=4184c99d-dd8d-11e3-8973-005056b80040').done(function(data)   {
           $('#ekf_api_test5').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test5').html('error');
       });
</pre>
<div id="ekf_api_test5" style="max-height:200px !important;overflow: scroll !important;"></div>
<!--==============================================-->
    <script>
       $.getJSON('http://192.168.50.200:8080/EKFApi/warehouses?key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test').html('error');
       });
    </script>
    <script>
       $.getJSON('http://192.168.50.200:8080/EKFApi/products?pnum=1&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test2').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test2').html('error');
       });
    </script>
    <script>
       $.getJSON('http://192.168.50.200:8080/EKFApi/pramounts?pnum=5&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test3').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test3').html('error');
       });
    </script>
    <script>
       $.getJSON('http://192.168.50.200:8080/EKFApi/prreceipts?pnum=10&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test4').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test4').html('error');
       });
    </script>
    <script>
       $.getJSON('http://192.168.50.200:8080/EKFIMSApi/qb?key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6&partner_id=4184c99d-dd8d-11e3-8973-005056b80040').done(function(data)   {
           $('#ekf_api_test5').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test5').html('error');
       });
    </script>
<?require($_SERVER["DOCUMENT_ROOT"]."/bitrix/footer.php");?>