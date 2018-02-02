<?require($_SERVER["DOCUMENT_ROOT"]."/bitrix/header.php");
$APPLICATION->SetTitle("Testing EKF Api:");
?>
Testing EKF Api:
пример вызова 
<pre>
       $.getJSON('http://188.127.242.42:8080/EKFApi/warehouses?key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test').html('error');
       });
</pre>
<div id="ekf_api_test" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://188.127.242.42:8080/EKFApi/products?pnum=1&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test2').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test2').html('error');
       });
</pre>
<div id="ekf_api_test2" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://188.127.242.42:8080/EKFApi/pramounts?pnum=5&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test3').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test3').html('error');
       });
</pre>
<div id="ekf_api_test3" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
       $.getJSON('http://188.127.242.42:8080/EKFApi/prreceipts?pnum=10&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test4').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test4').html('error');
       });
</pre>
<div id="ekf_api_test4" style="max-height:200px !important;overflow: scroll !important;"></div>
<pre>
		$.getJSON('http://188.127.242.42:8080/EKFIMSApi/ords_compl_reps?key=Wq4FeQgRA23ZYnBiDIklFujuVAon9pZezVGY&order_id=7027c092-65cd-11e6-b17c-000c29c6d5f2').done(function(data)   {
           $('#ekf_api_test5').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test5').html('error');
       });
</pre>
<div id="ekf_api_test5" style="max-height:200px !important;overflow: scroll !important;"></div>
<!--==============================================-->
    <script>
       $.getJSON('http://188.127.242.42:8080/EKFApi/warehouses?key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test').html('error');
       });
    </script>
    <script>
       $.getJSON('http://188.127.242.42:8080/EKFApi/products?pnum=1&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test2').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test2').html('error');
       });
    </script>
    <script>
       $.getJSON('http://188.127.242.42:8080/EKFApi/pramounts?pnum=5&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test3').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test3').html('error');
       });
    </script>
    <script>
       $.getJSON('http://188.127.242.42:8080/EKFApi/prreceipts?pnum=10&key=7RExwzbmldMZZopYALPQDhgvggIu13p61xX6').done(function(data)   {
           $('#ekf_api_test4').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test4').html('error');
       });
    </script>
	<script>
       $.getJSON('http://188.127.242.42:8080/EKFIMSApi/ords_compl_reps?key=Wq4FeQgRA23ZYnBiDIklFujuVAon9pZezVGY&order_id=7027c092-65cd-11e6-b17c-000c29c6d5f2').done(function(data)   {
           $('#ekf_api_test5').html(JSON.stringify(data));
       }).fail(function(eee, eee2)   {
           $('#ekf_api_test5').html('error');
       });
    </script>
<?require($_SERVER["DOCUMENT_ROOT"]."/bitrix/footer.php");?>