<?require($_SERVER["DOCUMENT_ROOT"]."/bitrix/modules/main/include/prolog_before.php");?>
<?CModule::IncludeModule('iblock');
CModule::IncludeModule('catalog');
define('CORPBUS_EXCHANGE');
?>

<?

class Translit
{
    function Transliterate($string)
    {
      $cyr=array(
         "Щ",  "Ш", "Ч", "Ц","Ю", "Я", "Ж", "А","Б","В","Г","Д","Е","Ё","З","И","Й","К","Л","М","Н","О","П","Р","С","Т","У","Ф","Х", "Ь","Ы","Ъ","Э","Є","Ї",
         "щ",  "ш", "ч", "ц","ю", "я", "ж", "а","б","в","г","д","е","ё","з","и","й","к","л","м","н","о","п","р","с","т","у","ф","х", "ь","ы","ъ","э","є","ї"
      );
      $lat=array(
         "Shh","Sh","Ch","C","Ju","Ja","Zh","A","B","V","G","D","Je","Jo","Z","I","J","K","L","M","N","O","P","R","S","T","U","F","Kh","'","Y","`","E","Je","Ji",
         "shh","sh","ch","c","ju","ja","zh","a","b","v","g","d","je","jo","z","i","j","k","l","m","n","o","p","r","s","t","u","f","kh","'","y","`","e","je","ji"
      );
      for($i=0; $i<count($cyr); $i++)
      {
         $c_cyr = $cyr[$i];
         $c_lat = $lat[$i];
         $string = str_replace($c_cyr, $c_lat, $string);
      }
      $string = preg_replace("/([qwrtpsdfghklzxcvbnmQWRTPSDFGHKLZXCVBNM]+)[jJ]e/", "\${1}e", $string);
      $string = preg_replace("/([qwrtpsdfghklzxcvbnmQWRTPSDFGHKLZXCVBNM]+)[jJ]/", "\${1}'", $string);
      $string = preg_replace("/([eyuioaEYUIOA]+)[Kk]h/", "\${1}h", $string);
      $string = preg_replace("/^kh/", "h", $string);
      $string = preg_replace("/^Kh/", "H", $string);
      return $string;
   }

   function UrlTranslit($string)
   {
      $string = preg_replace("/[_\s\.,?!\[\](){}]+/", "_", $string);
      $string = preg_replace("/-{2,}/", "--", $string);
      $string = preg_replace("/_-+_/", "--", $string);
      $string = preg_replace("/[_\-]+$/", "", $string);
      $string = Translit::Transliterate($string);
      $string = ToLower($string);
      $string = preg_replace("/j{2,}/", "j", $string);
      $string = preg_replace("/[^0-9a-z_\-]+/", "", $string);
      return $string;
   }
}

$ibid = 116;
$oreport="";

$iblock_props = CIBlock::GetProperties( $ibid, array(), array('CODE'=>'ARTIKUL'));	
if($ibprop = $iblock_props->Fetch())    { }	else	{
	$ibprop = Array(
		"NAME" => 'Артикул',
		"ACTIVE" => "Y",
		"XML_ID"=>'0000000000-0000000000',
		"SORT" => "555",
		"CODE" => 'artikul',
	    "PROPERTY_TYPE" => "S",
        "MULTIPLE"=>'N',
	    "IBLOCK_ID" => $ibid);

		$ibp = new CIBlockProperty;
		if($PropID = $ibp->Add($ibprop))	{ }
		else $oreport .= '[Err create prop artikul';
}

$arBSectFilter = Array("IBLOCK_ID"=>6);
$arBSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");//, "PROPERTY_privyazka"

$arBLowSectFilter = Array("IBLOCK_ID"=>6);
$arBLowSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "PROPERTY_PRIVYAZKA");//

$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
$ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");

$ar1CProdFilter = Array("IBLOCK_ID"=> $ibid, "!SECTION_ID"=>false, 'ACTIVE'=>'Y');
$ar1CProdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "PROPERTY_ARTIKUL", "PREVIEW_PICTURE");

if(isset($_REQUEST['SET_MAIN_PICT_TEST']))  {
CIBlockElement::SetPropertyValuesEx(89021, $ibid, array('MAIN_PICT'=>'fff') );
die();
}

if(isset($_REQUEST['FILL_EMPTY_SECT_CODE']))  {
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID");
    $ar1CSectFilter['XML_ID']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['CODE'])<=0)
		{
			$arFields = Array(
                  "ACTIVE" => 'Y',
				  "CODE" => Translit::UrlTranslit($ar_fields["NAME"])
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo $ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			//break;
		}
    }
	die();
}

if(isset($_REQUEST['EMPTY_UF_FILTER_PROPS']))  { 
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_FILTER_PROPS");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['UF_FILTER_PROPS'])>0)
		{
			$arFields = Array(
				  "UF_FILTER_PROPS" => ""
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo $ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			//break;
		}
    }
	die();
}

if(isset($_REQUEST['EMPTY_UF_DESCRIPTS_JSON']))  { 
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_GABARITS_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		echo strlen($ar_fields['UF_DESCRIPTS_JSON']).">>>".$ar_fields['UF_DESCRIPTS_JSON'].">>>";
		if(strlen($ar_fields['UF_DESCRIPTS_JSON'])>50)
		{
			$arFields = Array(
				  "UF_DESCRIPTS_JSON" => ""
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo $ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			//break;
		}
    }
	die();
	
}
	
if(isset($_REQUEST['EMPTY_UF_GABARITS_JSON']))  { 
	
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['UF_CHGB_JSON'])>50)
		{
			$arFields = Array(
				  "UF_CHGB_JSON" => "",
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo "<br/>".$ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			//break;
		}
    }
	die();
}

if(isset($_REQUEST['EMPTY_UF_DOCS_JSON']))  { 
	
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid, "ID"=>2964);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['UF_DOCS_JSON'])>50)
		{
			$arFields = Array(
				  "UF_DOCS_JSON" => ""
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo "<br/>".$ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			break;
		}
    }
	die();
}

if(isset($_REQUEST['GET_JSON_SECT_PROPS']))  { 
	
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	$cnt=0;
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['UF_DESCRIPTS_JSON'])>35||strlen($ar_fields['UF_CHGB_JSON'])>35||strlen($ar_fields['UF_DOCS_JSON'])>35)
		{
			echo "UF_DESCRIPTS_JSON=".$ar_fields['UF_DESCRIPTS_JSON'].strlen($ar_fields['UF_DESCRIPTS_JSON'])."<br/>";
			echo "UF_CHGB_JSON=".$ar_fields['UF_CHGB_JSON']."<br/>";
			echo "UF_DOCS_JSON=".$ar_fields['UF_DOCS_JSON']."<br/>";
				
			echo "<br/>[".$cnt."][".$ar_fields['ID']."]".$ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"])."<br/>";
			//break;
			$cnt++;
		}
    }
	die();
}


									
if(isset($_REQUEST['EMPTY_ALL_JSON_UNLINK_SECT_PROPS']))  { 
	
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	$cnt=0; //$cnt2=0; $cnt3=0;
	while($ar_fields = $res1CSECT->GetNext())	{
		if(strlen($ar_fields['UF_DESCRIPTS_JSON'])>50||true)	{
        $cnt++;
			$arFields = Array(
					"UF_DESCRIPTS_JSON" => "",
					"UF_CHGB_JSON" => ""
					//"UF_DOCS_JSON" => ""
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo "<br/>[".$cnt."]".$ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
		}
    }
	//echo "[[[".$cnt2."]]]";
	die();
}

if(isset($_REQUEST['EMPTY_JSON_SECT_PROPS']))  { 
	
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['UF_DESCRIPTS_JSON'])>0||strlen($ar_fields['UF_CHGB_JSON'])>0||strlen($ar_fields['UF_DOCS_JSON'])>0)
		{
			$arFields = Array(
					"UF_DESCRIPTS_JSON" => "",
					"UF_CHGB_JSON" => "",
					"UF_DOCS_JSON" => ""
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo "<br/>".$ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			//break;
		}
    }
	die();
}

if(isset($_REQUEST['EMPTY_JSON_SECT_SINGLE_PROPS']))  { 
	
	$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid, "ID"=>intval($_REQUEST['EMPTY_JSON_SECT_SINGLE_PROPS']));
    $ar1CSectdSelect = Array("ID", "NAME", "CODE", "XML_ID", "UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON");
    //$ar1CSectFilter['UF_FILTER_PROPS']=$grsid_val;
    $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
	while($ar_fields = $res1CSECT->GetNext())	{
        //$sectid = $ar_fields['ID'];
		if(strlen($ar_fields['UF_DESCRIPTS_JSON'])>0||strlen($ar_fields['UF_CHGB_JSON'])>0||strlen($ar_fields['UF_DOCS_JSON'])>0)
		{
			$arFields = Array(
					"UF_DESCRIPTS_JSON" => "",
					"UF_CHGB_JSON" => "",
					"UF_DOCS_JSON" => ""
                  );

				$bs = new CIBlockSection;
                if($ar_fields['ID'] > 0)
                {
                  $res = $bs->Update($ar_fields['ID'], $arFields);
                }
				
			echo "<br/>".$ar_fields['NAME']."=".Translit::UrlTranslit($ar_fields["NAME"]);
			break;
		}
    }
	die();
}

if(isset($_REQUEST['BX_DB_INFO']))  {
	
    echo "<form action=\"#\" enctype=\"multipart/form-data\"><input id=\"i1\" type=\"file\"/><input id=\"i1\" type=\"file\"/><input type=\"hidden\" name=\"BX_DB_INFO\" value=\"yes\"/><input type=\"submit\" value=\"test\"></form>";
    phpinfo();
    echo "===============";
    $iblock_props = CIBlock::GetProperties( $ibid, array(), array());
    $cnt=0;
    while($ibprop = $iblock_props->Fetch())    {
        if(($ibprop['PROPERTY_TYPE']=='S'||$ibprop['PROPERTY_TYPE']=='N'||$ibprop['PROPERTY_TYPE']=='L')&&$ibprop['MULTIPLE']=='N'&&$ibprop['ACTIVE']=='Y')  {
            $cnt++;
            echo "<pre>";
            echo $cnt."<br/>";
            print_r($ibprop);
            echo "</pre>";
        }
    }
die();
}

$errcnt=0;
$warncnt=0;
$critical_info="";
$critical_errs="";

if(isset($_REQUEST['ENTITY'])&&isset($_REQUEST['OTYPE'])&&isset($_REQUEST['OCNT']))  {
    
    if($_REQUEST['OTYPE']=="ADD")   {
        if($_REQUEST['ENTITY']=='BSECT')    {
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                
            }
        }
        else if($_REQUEST['ENTITY']=='1CSECT')    {
            $oreport .=  "[OCNT:".$_REQUEST['OCNT']."]";
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                $oreport .=  "{".$i."}";
                $bs = new CIBlockSection;
                $SID=0;
                $res=false;
                
                $sectid = false;
                $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                if(isset($_REQUEST['SGRXMLID'.$i])) {
                if(strlen($_REQUEST['SGRXMLID'.$i])>0)    {
                        $grsid_val = str_replace("\r\n","",$_REQUEST['SGRXMLID'.$i]);
                        $ar1CSectFilter['XML_ID']=$grsid_val;
                        $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                        if($ar_fields = $res1CSECT->GetNext())	{
                            $sectid = $ar_fields['ID'];
                        }
                        else    {
                            $oreport .= '[No find parent by XML_ID or root section][xml_id='.$grsid_val.']';
                            if(strlen($grsid_val)==36)  {
                                $critical_info.='[No find parent by XML_ID or root section][xml_id='.$grsid_val.']';
                            }
                        }
                    
                    }
                }
                else
                    $oreport .= '[No SGROUP present]';
                
                $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                if(isset($_REQUEST['SID'.$i])) {
                    if(strlen($_REQUEST['SID'.$i])>0)    {
                        $sid_val = str_replace("\r\n","",$_REQUEST['SID'.$i]);
                        $ar1CSectFilter['XML_ID']=$sid_val;
                        $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                        if($ar_fields = $res1CSECT->GetNext())	{
                            $SID = $ar_fields['ID'];
							while($ar_fields = $res1CSECT->GetNext())	{
								if(!CIBlockSection::Delete($ar_fields['ID']))
								{
									$oreport .= '[SECT delete success]'.$ar_fields['ID'];
								}
								else
									$oreport .= '[!!!SECT delete unsuccess]'.$ar_fields['ID'];
							}
                        }
                        else
                            $oreport .= '[No find SID by XML_ID]';
                    }
                }
                else
                    $oreport .= '[No SID present]';
                
                $arFields = Array(
                  "ACTIVE" => 'Y',
                  "IBLOCK_SECTION_ID" => $sectid,
                  "IBLOCK_ID" => $ibid,
                  "NAME" => $_REQUEST['SNAME'.$i],
				  "CODE"=>Translit::UrlTranslit($_REQUEST['SNAME'.$i].date('H_i_s')),
                  "XML_ID" => $_REQUEST['SID'.$i]
                  );
				  
				if(isset($_FILES["PICTURE".$i]))
					$arFields["PICTURE"] = $_FILES["PICTURE".$i];
				if(isset($_REQUEST["DESCRIPTION".$i]))	{
					$arFields["DESCRIPTION"] = $_REQUEST["DESCRIPTION".$i];
					$arFields["UF_ADD_DESCRIPTION"] = $_REQUEST["DESCRIPTION".$i];
				}
				if(isset($_REQUEST["FULL_DESCRIPTION".$i]))
					$arFields["UF_FULL_DESCRIPTION"] = $_REQUEST["FULL_DESCRIPTION".$i];
				if(isset($_REQUEST["TYPE_COMPLETING".$i]))
					$arFields["UF_TYPE_COMPLETING"] = $_REQUEST["TYPE_COMPLETING".$i];
				if(isset($_REQUEST["CHAR_GABARITS".$i]))
					$arFields["UF_CHAR_GABARITS"] = $_REQUEST["CHAR_GABARITS".$i];
				if(isset($_REQUEST["DOCUMENTATION".$i]))
					$arFields["UF_DOCUMENTATION"] = $_REQUEST["DOCUMENTATION".$i];
				if(isset($_REQUEST["SHORT_DESCRIPTION".$i]))
					$arFields["UF_SHORT_DESCRIPTION"] = $_REQUEST["SHORT_DESCRIPTION".$i];//MASTER_CATALOG
				if(isset($_REQUEST["VIDEO_DESCRIPTION".$i]))
					$arFields["UF_VIDEO_DESCRIPTION"] = $_REQUEST["VIDEO_DESCRIPTION".$i];
				if(isset($_FILES["MASTER_CATALOG".$i]))
					$arFields["UF_MASTER_CATALOG"] = $_FILES["MASTER_CATALOG".$i];

                $isupd = false;
                if($SID > 0)
                {
                  $res = $bs->Update($SID, $arFields);
                  $isupd = true;
                }
                else
                {
                  $SID = $bs->Add($arFields);
                  $res = ($SID>0);
                }

                if(!$res)   {
                    $critical_errs .= '[1cSect Add!!! Bad '.$bs->LAST_ERROR.']['.$_REQUEST['SNAME'.$i].']';
                    $oreport .= '[1cSect Add!!! Bad '.$bs->LAST_ERROR.']';
                    $errcnt++;
                }   else
                {
                    $oreport .= '[1cSect Add!!! Succ ['.$SID.']]';
                    $critical_info.=($isupd?'Upd EXIST SECT, probably w.empty link':'NewSECT w.empty link').' ['.$SID.'] nm='.$_REQUEST['SNAME'.$i];
                }
            }
        }
        else if($_REQUEST['ENTITY']=='1CPROD')    {
            $oreport .=  "[OCNT:".$_REQUEST['OCNT']."]";
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                $oreport .=  "{".$i."}";
                $el = new CIBlockElement;
				
				if(!isset($_REQUEST['PID'.$i]))	{
					$oreport .=  "{".$i."}missedPID,";
					continue;
				}
                
                $PROP = array();
		        $PROP['MORE_PHOTO'] = array();
                for($phi=1;$phi<10;$phi++)
                if(isset($_FILES['PHOTO'.$i.'_0'.$phi]))    {
                    $arrFile = array_merge( $_FILES['PHOTO'.$i.'_0'.$phi], array("del" => "N", "MODULE_ID" => "iblock"));

                    $fres = CFile::CheckImageFile($arrFile, 10000000, 1800, 1800);
                    $fres_small = CFile::CheckImageFile($arrFile, 10000000, 99, 99);
                    if (strlen($fres)>0||strlen($fres_small)==0)    {     
                    }   else
                        $PROP['MORE_PHOTO'][] = array("VALUE" => $_FILES['PHOTO'.$i.'_0'.$phi],"DESCRIPTION"=>"");
                }
                $PROP['artikul']=$_REQUEST['PART'.$i];
				$PROP['ARTIKUL']=$_REQUEST['PART'.$i];
                
                $sectid = false;
                $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                if(isset($_REQUEST['PGRXMLID'.$i])) {
                    $pgrid_val = str_replace("\r\n","",$_REQUEST['PGRXMLID'.$i]);
                    if(strlen($_REQUEST['PGRXMLID'.$i])>0)    {
                        $ar1CSectFilter['XML_ID']=$pgrid_val;
                        $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                        if($ar_fields = $res1CSECT->GetNext())	{
                            $sectid = $ar_fields['ID'];
                        }   else    {
                            $oreport .= '[No parent GROUP find]['.$pgrid_val.']['.$_REQUEST['PGRXMLID'.$i].']';
                            if(strlen($pgrid_val)==36)  {
                                $critical_info.='[No parent GROUP find][xml_id='.$pgrid_val.'][product='.(strlen($_REQUEST['PSHNAME'.$i])>0?$_REQUEST['PSHNAME'.$i]:$_REQUEST['PNAME'.$i]).']';
                            }
                        }
                    }
                }
                else
                    $oreport .= '[No PRGOUP present]';
                    
                $arLoadProductArray = Array(
                    //"MODIFIED_BY"    => $USER->GetID(), // элемент изменен текущим пользователем
                    "IBLOCK_SECTION_ID" => $sectid,//1714,//false,          // элемент лежит в корне раздела
                    "IBLOCK_ID"      => $ibid,
                    "PROPERTY_VALUES"=> $PROP,
                    "NAME"           => $_REQUEST['PNAME'.$i],
					"CODE"=>Translit::UrlTranslit($_REQUEST['PNAME'.$i].date('H_i_s')),
                    "XML_ID"         => $_REQUEST['PID'.$i],
                    "ACTIVE"         => "Y",            // активен
                    "ACTIVE_DATE"         => "Y",            // активен
                    "PREVIEW_TEXT"   => (strlen($_REQUEST['PNAME'.$i])>0?$_REQUEST['PNAME'.$i]:$_REQUEST['PSHNAME'.$i]),
                    //"DETAIL_TEXT"    => $_REQUEST['PPROPS'.$i],
                  );
                if(isset($_FILES['DETAIL_PICTURE'.$i])) {
                    $arrFile = array_merge( $_FILES["DETAIL_PICTURE".$i], array("del" => "N", "MODULE_ID" => "iblock"));

                    $fres = CFile::CheckImageFile($arrFile, 10000000, 3800, 3800);
                    $fres_small = CFile::CheckImageFile($arrFile, 10000000, 99, 99);
                    if (strlen($fres)>0||strlen($fres_small)==0)    {     
                    }
                    else    { 
                        
                        $fMini = CFile::ResizeImageFile(      // 
                             $sourceFile = $_FILES['DETAIL_PICTURE'.$i]['tmp_name'],
                             $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES['DETAIL_PICTURE'.$i]['name'],
                             $arSize = array('width'=>500, 'height'=>500),
                             $resizeType = BX_RESIZE_IMAGE_PROPORTIONAL
                             //$arWaterMark = array(),
                             //$jpgQuality=false,
                             //$arFilters =false
                             );

                        if($fMini)
                            $arLoadProductArray["DETAIL_PICTURE"] = CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES['DETAIL_PICTURE'.$i]['name']);

                        $f2res = CFile::CheckImageFile($arrFile, 100000, 100, 100);
                        if (strlen($f2res)>0)    {   
                            
                             $fMini = CFile::ResizeImageFile(      // 
                             $sourceFile = $_FILES['DETAIL_PICTURE'.$i]['tmp_name'],
                             $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize/".$_FILES['DETAIL_PICTURE'.$i]['name'],
                             $arSize = array('width'=>100, 'height'=>100),
                             $resizeType = BX_RESIZE_IMAGE_EXACT
                             );
                            
                            if($fMini)  {
                                if($fMini)
                                $arLoadProductArray["PREVIEW_PICTURE"] = CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize/".$_FILES['DETAIL_PICTURE'.$i]['name']);
                            }
                        }
                        else    { 
                            $arLoadProductArray["PREVIEW_PICTURE"] = $_FILES['DETAIL_PICTURE'.$i];
                        }
                    }
                    
                }
                
                if(isset($_REQUEST['1CPROD_ADD_PREV_PICT'.$i]))	{
                    $arLoadProductArray["DETAIL_PICTURE"]=CFile::MakeFileArray($_REQUEST['PREV_PICT']);
                    $arLoadProductArray["PREVIEW_PICTURE"]=CFile::MakeFileArray(str_replace('/files','/files/thumbnail',$_REQUEST['PREV_PICT']));
                }
                
                $ar1CProdFilter = Array("IBLOCK_ID"=> $ibid, "PROPERTY_ARTIKUL"=>$_REQUEST['PART'.$i]);//"!SECTION_ID"=>false,
                $ar1CProdSelect = Array("ID", "NAME", "IBLOCK_ID");
                $res1CPROD = CIBlockElement::GetList(Array(), $ar1CProdFilter, false, false, $ar1CProdSelect);
                
                $ar1CXMLIDProdFilter = Array("IBLOCK_ID"=> $ibid, "XML_ID"=>$_REQUEST['PID'.$i]);//"!SECTION_ID"=>false,
                $ar1CXMLIDProdSelect = Array("ID", "NAME", "IBLOCK_ID");
                $res1CXMLPROD = CIBlockElement::GetList(Array(), $ar1CXMLIDProdFilter, false, false, $ar1CXMLIDProdSelect);

                $succ_add=false;
                $prexist=false;
                $PRODUCT_ID=false;
                if($ar_fields = $res1CXMLPROD->GetNext())	{
                    $prexist=true;
                    $PRODUCT_ID=$ar_fields['ID'];
					unset($arLoadProductArray['PROPERTY_VALUES']);
					unset($arLoadProductArray['DETAIL_PICTURE']);
					unset($arLoadProductArray['PREVIEW_PICTURE']);
                    if($el->Update( $PRODUCT_ID, $arLoadProductArray))   {
                        $oreport .=  "[UPDATE EXIST XML_ID:".$PRODUCT_ID."]".$_REQUEST['PART'.$i]."IBID=".$ar_fields['IBLOCK_ID'];
						
                        $succ_add=true;
						//$res1CXMLPROD22 = CIBlockElement::GetList(Array(), $ar1CProdFilter, false, false, $ar1CProdSelect);
						//while($ar_fields22 = $res1CXMLPROD22->GetNext())	{
						//	CIBlockElement::SetPropertyValuesEx($ar_fields22['ID'], $ibid, array('artikul'=>$_REQUEST['PART'.$i]) );
						//	CIBlockElement::SetPropertyValuesEx($ar_fields22['ID'], $ibid, array('ARTIKUL'=>$_REQUEST['PART'.$i]) );
						//}
						while($dar_fields = $res1CXMLPROD->GetNext())	{
							$PRODUCT_ID = $dar_fields['ID'];
							if(!CIBlockElement::Delete($PRODUCT_ID))
							{
								$oreport .= "[Success clone delete]".$PRODUCT_ID;
							}
							else
								$oreport .= "[Bad clone delete]".$PRODUCT_ID;
						}
                    }
                } else  {
                    if(false) { //$ar_fields2 = $res1CPROD->GetNext())	{
                        $prexist=true;
                        $PRODUCT_ID=$ar_fields2['ID'];
                        if($el->Update( $PRODUCT_ID, $arLoadProductArray))   {
                            $oreport .=  "[UPDATE EXIST ART:".$PRODUCT_ID."]"."IBID=".$ar_fields2['IBLOCK_ID'];
                            $succ_add=true;
                        }
                    } else  {
                        if($PRODUCT_ID = $el->Add($arLoadProductArray)) {
                            $oreport .=  "[ADD NEW:".$PRODUCT_ID."]";
                            $succ_add=true;
                        }
                    }
                }
                
                if($succ_add)	{
                    //CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('artikul'=>$_REQUEST['PART'.$i]) );
                    $ishop_price=0;
                    $base_price=0;
                    //$oreport .= "[bprice=".$_REQUEST['PBPRICE'.$i].",=".floatval($_REQUEST['PBPRICE'.$i])."]";
                    if(isset($_REQUEST['PBPRICE'.$i])) 
                        $base_price=floatval($_REQUEST['PBPRICE'.$i]);
                    if(isset($_REQUEST['PISPRICE'.$i])) 
                        $ishop_price=floatval($_REQUEST['PISPRICE'.$i]);
                    
                    $PRICE_TYPE_ID = 2;

                    $arFields = Array(
                        "PRODUCT_ID" => $PRODUCT_ID,
                        "CATALOG_GROUP_ID" => $PRICE_TYPE_ID,
                        "PRICE" => $ishop_price,
                        "CURRENCY" => "RUB"
                        //"QUANTITY_FROM" => 1,
                        //"QUANTITY_TO" => 10
                    );

                    $res = CPrice::GetList(
                            array(),
                            array(
                                    "PRODUCT_ID" => $PRODUCT_ID,
                                    "CATALOG_GROUP_ID" => $PRICE_TYPE_ID
                                )
                        );

                    if ($arr = $res->Fetch())
                    {
                        if(CPrice::Update($arr["ID"], $arFields))   {
                            $oreport .= "[PIsUpd suc]";
                        }
                        else    {
                            $oreport .= '[PIsUpd !bad '.$PRODUCT_ID.']';
                            $errcnt++;
                        }
                            
                    }
                    else
                    {
                        if(CPrice::Add($arFields))  {
                            $oreport .= "[PIsAdd suc]";
                        }
                        else    {
                            $oreport .= '[PIsAdd !bad '.$PRODUCT_ID.']';
                            $errcnt++;
                        }
                    }
                    
                    $PRICE_TYPE_ID = 4;

                    $arFields = Array(
                        "PRODUCT_ID" => $PRODUCT_ID,
                        "CATALOG_GROUP_ID" => $PRICE_TYPE_ID,
                        "PRICE" => $base_price,
                        "CURRENCY" => "RUB"
                        //"QUANTITY_FROM" => 1,
                        //"QUANTITY_TO" => 10
                    );

                    $res = CPrice::GetList(
                            array(),
                            array(
                                    "PRODUCT_ID" => $PRODUCT_ID,
                                    "CATALOG_GROUP_ID" => $PRICE_TYPE_ID
                                )
                        );

                    if ($arr = $res->Fetch())
                    {
                        if(CPrice::Update($arr["ID"], $arFields))   {
                            $oreport .= "[PriceBaseUpd success ".$PRODUCT_ID.']';
                        }
                        else    {
                            $oreport .= '[PrBaseUpd!!! Bad '.$PRODUCT_ID.']';
                            $errcnt++;
                        }
                            
                    }
                    else
                    {
                        if(CPrice::Add($arFields))  {
                            $oreport .= "[PriceBaseAdd success ".$PRODUCT_ID.']';
                        }
                        else    {
                            $oreport .= '[PriceBaseAdd !!!Bad '.$PRODUCT_ID.']';
                            $errcnt++;
                        }
                    }
                    
                    
                    //$arFields = array('QUANTITY_TRACE'=>'Y','QUANTITY'=>100,'QUANTITY_RESERVED' => 0);// зарезервированное количество
                    //CCatalogProduct::Update($PRODUCT_ID, $arFields);
                    $pquant=0;
                    //if(isset($_REQUEST['PQUANT'.$i])) 
                        $pquant=floatval($_REQUEST['PQUANT'.$i]);
                    
                    $arFields = array(
                                      "ID" => $PRODUCT_ID, 
                                      "QUANTITY" => $pquant, //выставляем тип ндс (задается в админке)  
                                      "QUANTITY_TRACE" => "Y" //НДС входит в стоимость
                                      );
                    if(CCatalogProduct::Add($arFields)) {
                        $oreport .= "[CatQuantAdd success ".$PRODUCT_ID.']';
                    }
                    else    {
                        $oreport .= '[CatQuantAdd !!!Bad '.$PRODUCT_ID.']';
                        $errcnt++;
                    }
                    
                    if(isset($_REQUEST['PPROPS'.$i]))   {
						$oreport .= "***-3";
                        $pprops = json_decode($_REQUEST['PPROPS'.$i],true);
                        if(isset($pprops)) {
							$oreport .= "***-2";
                            if(isset($pprops['dc']))    {
                                $oreport .= "***-1";
                                for($ii=0;$ii<intval($pprops['dc']);$ii++)  {
                                    $iblock_props = CIBlock::GetProperties( $ibid, array(), array('XML_ID'=>$pprops['c'.$ii]));
									$oreport .= "***0";
									$has_field=false;
                                    if($ibprop = $iblock_props->Fetch())    {
										$has_field=true;
										$oreport .= "***1";
									}	else	{
										if(isset($pprops['n'.$ii]))	{
											
											//$ibprop['PROPERTY_TYPE']='L';
											//$ibprop['MULTIPLE']='N';
											//$ibprop['CODE']=Translit::UrlTranslit($pprops['n'.$ii]);
											$PCODE=Translit::UrlTranslit($pprops['n'.$ii]);
													$PCODE=substr($PCODE,0,40);
													$iblock_props22 = CIBlock::GetProperties( $ibid, array(), array('CODE'=>$PCODE));
													$nct=0;
													while($ibprop22 = $iblock_props22->Fetch())    {
														$PCODE=$PCODE.$nct;
														$iblock_props22 = CIBlock::GetProperties( $ibid, array(), array('CODE'=>$PCODE));
														$nct++;
														if($nct>999) break;
													}
											
											$ibprop = Array(
											  "NAME" => $pprops['n'.$ii],
											  "ACTIVE" => "Y",
											  "XML_ID"=>$pprops['c'.$ii],
											  "SORT" => "555",
											  "CODE" => $PCODE,
											  "PROPERTY_TYPE" => "L",
											  "MULTIPLE"=>'N',
											  "IBLOCK_ID" => $ibid
											  );
											$oreport .= "***2";
											
											$ibp = new CIBlockProperty;
											if($PropID = $ibp->Add($ibprop))	{
												$has_field=true;
												$ibprop['ID'] = $PropID;
												$oreport .= "***3";
											}
											else
												$oreport .= '[Err create prop ext_code '.$pprops['c'.$ii].']';
											
										}
									}
									if($has_field)	{
                                    if(strlen($pprops['v'.$ii])>0)    {
                                        if($ibprop['PROPERTY_TYPE']=='L'&&$ibprop['MULTIPLE']=='N')   {
                                            $enres = CIBlockProperty::GetPropertyEnum( $ibprop['ID'], array(), array());
                                            $has_in_en=false;
                                            while($eni = $enres->Fetch())   {
                                                if($eni['VALUE']==$pprops['v'.$ii])
                                                {
                                                    $has_in_en=true;
                                                    CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>$eni['ID']) );
                                                    break;
                                                }
                                            }
                                            if(!$has_in_en)  {
                                                
                                                $ibpenum = new CIBlockPropertyEnum;
                                                if($PropID = $ibpenum->Add(Array('PROPERTY_ID'=>$ibprop['ID'], 'VALUE'=>$pprops['v'.$ii]))) {//$pprops['v'.$ii]
                                                    //CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('STATUS_TOVARA'=>$ibprop['CODE']."===".$PropID) );
                                                    CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>$PropID) );
                                                }   else    {
                                                    $oreport .= '[Uns add listval, code '.$ibprop['CODE'].'ID='.$ibprop['ID'].'val='.$pprops['v'.$ii].'!]';
                                                    $errcnt++;
                                                }
                                            }
                                        }
                                        else if(($ibprop['PROPERTY_TYPE']=='S'||$ibprop['PROPERTY_TYPE']=='N')&&$ibprop['MULTIPLE']=='N')   {
                                        CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>$pprops['v'.$ii]) );    }
                                    }
                                    }   else    {
                                        $oreport .= '[Nfind prop ext_code '.$pprops['c'.$ii].']';
                                        $errcnt++;
                                    }
                                }
                                
                            }
							
							
							
                        }   else    {
                            switch (json_last_error()) {
                                case JSON_ERROR_NONE:
                                    //$oreport .=  ' - Ошибок нет';
                                break;
                                case JSON_ERROR_DEPTH:
                                    $oreport .= '[JSON PARSE ERROR - Достигнута максимальная глубина стека]';
                                    $errcnt++;
                                break;
                                case JSON_ERROR_STATE_MISMATCH:
                                    $oreport .= '[JSON PARSE ERROR - Некорректные разряды или не совпадение режимов]';
                                    $errcnt++;
                                break;
                                case JSON_ERROR_CTRL_CHAR:
                                    $oreport .= '[JSON PARSE ERROR - Некорректный управляющий символ]';
                                    $errcnt++;
                                break;
                                case JSON_ERROR_SYNTAX:
                                    $oreport .= '[JSON PARSE ERROR - Синтаксическая ошибка, не корректный JSON]';
                                    $errcnt++;
                                break;
                                case JSON_ERROR_UTF8:
                                    $oreport .= '[JSON PARSE ERROR - Некорректные символы UTF-8, возможно неверная кодировка]';
                                    $errcnt++;
                                break;
                                default:
                                    $oreport .= '[JSON PARSE ERROR - Неизвестная ошибка]';
                                    $errcnt++;
                                break;
                            }
							
							
							
                        }
						
						
                    }//End of pprops analyze
                    else
                        $oreport .= '[No PROPS present]';
					
					$sel_rcnt=0;
							$db_props = CIBlockElement::GetProperty($ibid, $PRODUCT_ID, array(), Array("EMPTY"=>'Y'));//"PROPERTY_TYPE"=>"L", 
							if($db_props)	{
								$sel_rcnt=$db_props->SelectedRowsCount();
							}
							CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('PROPS_1C_CNT'=>$sel_rcnt) );
                    
                }
                else    { //Error Add Product into $ibid IBlock
                    $critical_errs .= '[Error of '.($prexist?"UPDATE EXIST":"ADD NEW").': '.$el->LAST_ERROR.']['.(strlen($_REQUEST['PSHNAME'.$i])>0?$_REQUEST['PSHNAME'.$i]:$_REQUEST['PNAME'.$i]).']';
                    $oreport .= '[Error of '.($prexist?"UPDATE EXIST":"ADD NEW").': '.$el->LAST_ERROR.']';
                    $errcnt++;
                }
                     
            }//End for 
            
        }
        else    {
            $oreport .= "[Unknown ENTITY param value]";
            $errcnt++;
        }
        
    }//End ADD Operation
    
    else if($_REQUEST['OTYPE']=="UPDATE")   {
        if($_REQUEST['ENTITY']=='BSECT')    {
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                
            }
        }
        else if($_REQUEST['ENTITY']=='1CSECT')    {
            $oreport .=  "[OCNT:".$_REQUEST['OCNT']."]";
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                $oreport .=  "{".$i."}";
                $bs = new CIBlockSection;
                $SID=0;
                $res=false;
                
                $arSFields = Array(
                  "ACTIVE" => 'Y'
                  );
                
                $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                if(intval($_REQUEST['SETSGRXMLID'.$i])==1)    {
					$arSFields["IBLOCK_SECTION_ID"] = false;
                    if(isset($_REQUEST['SGRXMLID'.$i])) {
						
                    if(strlen($_REQUEST['SGRXMLID'.$i])>0)    {
                            $grsid_val = str_replace("\r\n","",$_REQUEST['SGRXMLID'.$i]);
                            $ar1CSectFilter['XML_ID']=$grsid_val;
                            $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                            if($ar_fields = $res1CSECT->GetNext())	{
                                $arSFields["IBLOCK_SECTION_ID"] = $ar_fields['ID'];
                            }
                            else    {		
                                $oreport .= '[Not find IBLOCK_SECTION_ID][{$grsid_val}]';
                            }
                        }
                    }
                    else
                        $oreport .= '[No SGROUP present]';
                } //else
                  //  $oreport .= "[unable SGRXMLID set][{$_REQUEST['SETSGRXMLID'.$i]}]";
                
                if(intval($_REQUEST['SETSNAME'.$i])==1)    {
                    $arSFields["NAME"] = str_replace("\r\n","",$_REQUEST['SNAME'.$i]);
					$arSFields["CODE"] = Translit::UrlTranslit($arSFields["NAME"].date('H_i_s'));
                } 
				if(intval($_REQUEST['SETSEO_ALIAS_URL_FROM_NAME'.$i])==1)    {
					$arSFields["CODE"] = Translit::UrlTranslit($_REQUEST['SEO_ALIAS_URL_FROM_NAME'.$i].date('H_i_s'));
				}
				//else
                  //  $oreport .= "[unable SNAME set][{$_REQUEST['SETSNAME'.$i]}]";
				
				//if(intval($_REQUEST['SETSID'.$i])==1)    {
                //    $arSFields["XML_ID"] = str_replace("\r\n","",$_REQUEST['NEWSID'.$i]);
                //}
                
                $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "UF_VIDEO_DESCRIPTION", "DESCRIPTION");
                if(isset($_REQUEST['SID'.$i])) {
					$ufvd="none";
					$desc="none";
                    $sid_val = str_replace("\r\n","",$_REQUEST['SID'.$i]);
                    if(strlen($_REQUEST['SID'.$i])>0)    {
                        $ar1CSectFilter['XML_ID']=$sid_val; 
                        $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                        if($ar_fields = $res1CSECT->GetNext())	{
                            $SID = $ar_fields['ID'];
							$ufvd = $ar_fields['UF_VIDEO_DESCRIPTION'];
							$desc = $ar_fields['DESCRIPTION'];
							while($ar_fields = $res1CSECT->GetNext())	{
								if(!CIBlockSection::Delete($ar_fields['ID']))
								{
									$oreport .= '[SECT delete success]'.$ar_fields['ID'];
								}
								else
									$oreport .= '[!!!SECT delete unsuccess]'.$ar_fields['ID'];
							}
                        }
                    }
					
					if(intval($_REQUEST["SETPICTURE".$i])==1)	{
						if(isset($_FILES["PICTURE".$i]))	{
							
							$arSFields["UF_PICTURE_PATH"] = (isset($_REQUEST["PICTURE_PATH".$i])?$_REQUEST["PICTURE_PATH".$i]:"");
							
							$oreport .= "name=".$_FILES["PICTURE".$i]['name'].",size=".$_FILES["PICTURE".$i]['size'];
							$arrFile = array_merge( $_FILES["PICTURE".$i], array("del" => "N", "MODULE_ID" => "iblock"));

							$fres = CFile::CheckImageFile($arrFile, 10000000, 6800, 6800);
							//$fres_small = CFile::CheckImageFile($arrFile, 10000000, 99, 99);
							if (strlen($fres)>0&&substr_count($fres,"Файл не является графическим")==0)	{//||strlen($fres_small)==0)    {
								$oreport .= '[!!!SECT PICT check unsuccess]['.$fres.']';
								$arSFields["UF_BASE_PICTURE"] = $_FILES['PICTURE'.$i];
								//$arSFields["DETAIL_PICTURE"] = $_FILES['PICTURE'.$i];
							}
							else    { 
								
								$fMini = CFile::ResizeImageFile(      // 
									 $sourceFile = $_FILES['PICTURE'.$i]['tmp_name'],
									 $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES['PICTURE'.$i]['name'],
									 $arSize = array('width'=>500, 'height'=>500),
									 $resizeType = BX_RESIZE_IMAGE_PROPORTIONAL
									 );

								if($fMini)
									$arSFields["PICTURE"] = CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES['PICTURE'.$i]['name']);
								else	{
									$arSFields["PICTURE"] = $_FILES['PICTURE'.$i];
									$oreport .= '[!!!SECT PICT resize unsuccess]';
								}
							}
							
						}	else	{
							$oreport .= '[!!!SECT PICT param is empty]';
						}
					}
					
					$SECTPROP = array();
					$SPROP_ELM = false;
					
					if(isset($_REQUEST["FDSCDC".$i])&&($_REQUEST["SETDESCRIPTS_JSON".$i]=="1"))	{
						//$oreport .= '[!!!UF_DESCRIPTS==>]';
						$fds_files = array();
						$fds_texts = array();
						$file_names = array();
						$crash_events=false;
						$fdjsonarr = array();
						$oreport .= '['.$fdi.'[*]'.$_REQUEST["FDSCDC".$i].']';
						for($fdi=0;$fdi<intval($_REQUEST["FDSCDC".$i]);$fdi++)	{
							
							$oreport .= '['.$fdi.'*]';
							if(intval($_REQUEST["FDSC".$i."FPSET".$fdi])==1)	{
								$oreport .= '['.$fdi.'**]';
								if(isset($_FILES["FDSC".$i."FP".$fdi]))	{
									$oreport .= '['.$fdi.'***]';
									
									$arrFile = array_merge( $_FILES["FDSC".$i."FP".$fdi], array("del" => "N", "MODULE_ID" => "iblock"));

									$fres = CFile::CheckImageFile($arrFile, 5000000, 3800, 3800);
									if (strlen($fres)>0&&!($fres=="Файл не является графическим."))	{//||strlen($fres_small)==0)    {
										$oreport .= '[!!!FDS PICT check unsuccess]['.$fres.']';
										$arr_file=Array(
											"name" => $_FILES["FDSC".$i."FP".$fdi]['name'],
											"size" => $_FILES["FDSC".$i."FP".$fdi]['size'],
											"tmp_name" => $_FILES["FDSC".$i."FP".$fdi]['tmp_name'],
											"type" => "",
											"old_file" => "",
											"del" => "Y",
											"MODULE_ID" => "iblock");
										$fid = CFile::SaveFile($arr_file, "1c_katalog");
										if($fid>0)	{
											//CFile::GetPath($fid);
											$fdjsonarr["fdnm".$fdi] = CFile::GetPath($fid);
											$fdjsonarr["fdtx".$fdi] = $_REQUEST["FDSC".$i."DSC".$fdi].
												(strlen($_REQUEST["FDSC".$i."DSC".$fdi])>0?"":"-");
										}	else	{
											$oreport .= '[Err of savefile]';
										}
									}
									else    { 
										$oreport .= '['.$fdi.'****]';
										$arTmpFile = CFile::MakeFileArray($_FILES["FDSC".$i."FP".$fdi]['tmp_name']);
										$arTmpFile['name'] = $_FILES["FDSC".$i."FP".$fdi]['name'];
										$fds_files[] = array("VALUE" => $arTmpFile,"DESCRIPTION"=>"sect doc");
										$fds_texts[] = $_REQUEST["FDSC".$i."DSC".$fdi].
												(strlen($_REQUEST["FDSC".$i."DSC".$fdi])>0?"":"-");
										$fMini = CFile::ResizeImageFile(      // 
											 $sourceFile = $_FILES["FDSC".$i."FP".$fdi]['tmp_name'],
											 $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES["FDSC".$i."FP".$fdi]['name'],
											 $arSize = array('width'=>300, 'height'=>300),
											 $resizeType = BX_RESIZE_IMAGE_PROPORTIONAL
											 );
											 
										$arr_file=Array(
											"name" => $_FILES["FDSC".$i."FP".$fdi]['name'],
											"size" => $_FILES["FDSC".$i."FP".$fdi]['size'],
											"tmp_name" => $_FILES["FDSC".$i."FP".$fdi]['tmp_name'],
											"type" => "",
											"old_file" => "",
											"del" => "Y",
											"MODULE_ID" => "iblock");
										$fid = CFile::SaveFile($arr_file, "1c_katalog");
										if($fid>0)	{
											//CFile::GetPath($fid);
											$fdjsonarr["fdnm_big".$fdi] = CFile::GetPath($fid);
											$fdjsonarr["fdtx".$fdi] = $_REQUEST["FDSC".$i."DSC".$fdi].
												(strlen($_REQUEST["FDSC".$i."DSC".$fdi])>0?"":"-");
										}	else	{
											$oreport .= '[Err of savefile2]';
										}

										if($fMini)	{
											/*$fds_files[] = array("VALUE" => CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES["FDSC".$i."FP".$fdi]['name']),"DESCRIPTION"=>"");
											$fds_texts[] = $_REQUEST["FDSC".$i."DSC".$fdi].
												(strlen($_REQUEST["FDSC".$i."DSC".$fdi])>0?"":"-");*/
											$fdtxi = $_REQUEST["FDSC".$i."DSC".$fdi].
												(strlen($_REQUEST["FDSC".$i."DSC".$fdi])>0?"":"-");
											$fdjson .= "\"fdnm".$fdi."\":\"http://ekfgroup.com/upload/exchange_resize_big/".$_FILES["FDSC".$i."FP".$fdi]['name']."\",\"fdtx".$fdi."\":\"".str_replace("\"","'",$fdtxi)."\",";
											$fdjsonarr["fdnm".$fdi] = "/upload/exchange_resize_big/".$_FILES["FDSC".$i."FP".$fdi]['name'];
											$fdjsonarr["fdtx".$fdi] = $fdtxi;
											//$oreport .= "[".$_REQUEST["FDSC".$i."DSC".$fdi]."]";
										}
										else	{
											if($fid>0)	{
												$fdjsonarr["fdnm".$fdi] = CFile::GetPath($fid);
											}
											//$crash_events=true;
											$oreport .= '[!!!FDS PICT resize unsuccess]';
										}
									}
									
								}	else	{
									$crash_events=true;
									$oreport .= '[!!!FDS PICT param is empty]';
								}
							}
							
							//$oreport .= var_dump($fds_texts, true);

						}
						
						$fdjsonarr["fdcnt"] = $fdi;
						
						if(!$crash_events)	{
						
							if($SID > 0) {
								
								if(sizeof($SECTPROP)==0)	{
									$arBLowSectFilter = Array("IBLOCK_ID"=>114, "PROPERTY_SECTION_SID"=>$SID);
									$arBLowSectdSelect = Array("ID");
									
									$res1CPROD = CIBlockElement::GetList(Array(), $arBLowSectFilter, false, false, $arBLowSectdSelect);
									while($ar_fields_114 = $res1CPROD->GetNext())	{
										$SPROP_ELM = $ar_fields_114['ID'];
										/*$el = new CIBlockElement;

										$arLoadProductArray = Array(
										  "ACTIVE"         => "N"
										  );

										$PRODUCT_ID = $ar_fields_114['ID'];  // изменяем элемент с кодом (ID) 2
										$res = $el->Update($PRODUCT_ID, $arLoadProductArray);*/
									}
								}
							
								$arSFields["UF_DESCRIPTS_FILES"] = $fds_files;
								$arSFields["UF_DESCRIPTS_TEXTS"] = $fds_texts;
								$arSFields["UF_DCS_DIR"] = json_encode($fdjsonarr);
								//$el = new CIBlockElement;

								$SECTPROP["DESCRIPTS_FILES"] = $fds_files;  // свойству с кодом 12 присваиваем значение "Белый"
								$SECTPROP["DESCRIPTS_TEXTS"] = $fds_texts;
								$SECTPROP["SECTION_SID"] = $SID;// свойству с кодом 3 присваиваем значение 38

								/*$arLoadProductArray = Array(
								  //"MODIFIED_BY"    => $USER->GetID(), // элемент изменен текущим пользователем
								  "IBLOCK_SECTION_ID" => false,          // элемент лежит в корне раздела
								  "IBLOCK_ID"      => 114,
								  "PROPERTY_VALUES"=> $SECTPROP,
								  "NAME"           => "Элемент",
								  "ACTIVE"         => "Y",            // активен
								  "PREVIEW_TEXT"   => "текст для списка элементов",
								  "DETAIL_TEXT"    => "текст для детального просмотра"//,
								  //"DETAIL_PICTURE" => CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/image.gif")
								  );

								if($PRODUCT_ID = $el->Add($arLoadProductArray))
								  $oreport .=  "New ID: ".$PRODUCT_ID;
								else
								  $oreport .=  "Error: ".$el->LAST_ERROR;*/
							
							}
							
							if($_REQUEST["SETDESCRIPTS_JSON".$i]=="1")	{
								if(isset($_REQUEST["DESCRIPTS_JSON".$i]))	{
									$oreport .= '[SETDESCRIPTS_JSON]';//str_replace("\r\n","",
									$arSFields["UF_DESCRIPTS_JSON"] = $_REQUEST["DESCRIPTS_JSON".$i];
								}
							}
						
						}  else $oreport .= '[!!!UF_DESCRIPTS_CRASHES]';
						
					}
					//else  $oreport .= '[!!!UF_DESCRIPTS][['.$_REQUEST["SETDESCRIPTS_JSON".$i].']][[<'.$_REQUEST["FDSCDC".$i].">]]";
					
					if($_REQUEST["SETDESCRIPTS_JSON".$i]=="1"&&strlen($_REQUEST["DESCRIPTS_JSON".$i])<50)	{
								if(isset($_REQUEST["DESCRIPTS_JSON".$i]))	{
									$oreport .= '[SETDESCRIPTS_JSON<50]';//str_replace("\r\n","",
									$arSFields["UF_DESCRIPTS_JSON"] = $_REQUEST["DESCRIPTS_JSON".$i];
								}
							}
					
					if(isset($_REQUEST["FGBDC".$i])&&($_REQUEST["SETGABARITS_JSON".$i]=="1"))	{
						$oreport .= '[YES_UF_GABARITS==>]';
						$fds_files = array();
						$fds_texts = array();
						$crash_events=false;
						$fdjson = "{";
						$oreport .= '['.$fdi.'[*]'.$_REQUEST["FGBDC".$i].']';
						$fdjsonarr = array();
						for($fdi=0;$fdi<intval($_REQUEST["FGBDC".$i]);$fdi++)	{
							
							$oreport .= '['.$fdi.'*]';
							if(intval($_REQUEST["FGB".$i."FPSET".$fdi])==1)	{
								$oreport .= '['.$fdi.'**]';
								if(isset($_FILES["FGB".$i."FP".$fdi]))	{
									$oreport .= '['.$fdi.'***]';
									
									$arrFile = array_merge( $_FILES["FGB".$i."FP".$fdi], array("del" => "N", "MODULE_ID" => "iblock"));

									$fres = CFile::CheckImageFile($arrFile, 5000000, 3800, 3800);
									if (strlen($fres)>0&&!($fres=="Файл не является графическим."))	{//||strlen($fres_small)==0)    {
										$oreport .= '[!!!FGB PICT check unsuccess]['.$fres.']';
										$arr_file=Array(
											"name" => $_FILES["FGB".$i."FP".$fdi]['name'],
											"size" => $_FILES["FGB".$i."FP".$fdi]['size'],
											"tmp_name" => $_FILES["FGB".$i."FP".$fdi]['tmp_name'],
											"type" => "",
											"old_file" => "",
											"del" => "Y",
											"MODULE_ID" => "iblock");
										$fid = CFile::SaveFile($arr_file, "1c_katalog");
										if($fid>0)	{
											//CFile::GetPath($fid);
											$fdjsonarr["fdnm".$fdi] = CFile::GetPath($fid);
											$fdjsonarr["fdtx".$fdi] = $_REQUEST["FGB".$i."DSC".$fdi].(strlen($_REQUEST["FGB".$i."DSC".$fdi])>0?"":"-");
										}	else	{
											$oreport .= '[Err of savefile]';
										}
									}
									else    { 
										$oreport .= '['.$fdi.'****]';
										$arTmpFile = CFile::MakeFileArray($_FILES["FGB".$i."FP".$fdi]['tmp_name']);
										$arTmpFile['name'] = $_FILES["FGB".$i."FP".$fdi]['name'];
										$fds_files[] = array("VALUE" => $arTmpFile,"DESCRIPTION"=>"sect doc");
										$fds_texts[] = $_REQUEST["FGB".$i."DSC".$fdi].
												(strlen($_REQUEST["FGB".$i."DSC".$fdi])>0?"":"-");
										$fMini = CFile::ResizeImageFile(      
											 $sourceFile = $_FILES["FGB".$i."FP".$fdi]['tmp_name'],
											 $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES["FGB".$i."FP".$fdi]['name'],
											 $arSize = array('width'=>300, 'height'=>300),
											 $resizeType = BX_RESIZE_IMAGE_PROPORTIONAL
											 );
											 
										$arr_file=Array(
											"name" => $_FILES["FGB".$i."FP".$fdi]['name'],
											"size" => $_FILES["FGB".$i."FP".$fdi]['size'],
											"tmp_name" => $_FILES["FGB".$i."FP".$fdi]['tmp_name'],
											"type" => "",
											"old_file" => "",
											"del" => "Y",
											"MODULE_ID" => "iblock");
										$fid = CFile::SaveFile($arr_file, "1c_katalog");
										if($fid>0)	{
											//CFile::GetPath($fid);
											$fdjsonarr["fdnm_big".$fdi] = CFile::GetPath($fid);
										}	else	{
											$oreport .= '[Err of savefile2]';
										}

										if($fMini)	{
											$fdtxi = $_REQUEST["FGB".$i."DSC".$fdi].(strlen($_REQUEST["FGB".$i."DSC".$fdi])>0?"":"-");
											$fdjson .= "\"fdnm".$fdi."\":\"http://ekfgroup.com/upload/exchange_resize_big/".$_FILES["FGB".$i."FP".$fdi]['name']."\",\"fdtx".$fdi."\":\"".str_replace("\"","'",$fdtxi)."\",";
											//$fdjsonarr["fdnm_big".$fdi] = "/upload/exchange_resize_big/".$_FILES["FGB".$i."FP".$fdi]['name'];
											$fdjsonarr["fdnm".$fdi] = "/upload/exchange_resize_big/".$_FILES["FGB".$i."FP".$fdi]['name'];
											$fdjsonarr["fdtx".$fdi] = $fdtxi;
											/*$fds_files[] = array("VALUE" => CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES["FGB".$i."FP".$fdi]['name']),"DESCRIPTION"=>"");
											$fds_texts[] = $_REQUEST["FGB".$i."DSC".$fdi].
												(strlen($_REQUEST["FGB".$i."DSC".$fdi])>0?"":"-");
											$oreport .= "[".$_REQUEST["FGB".$i."DSC".$fdi]."]";
											*/
										}
										else 	{
											$fdtxi = $_REQUEST["FGB".$i."DSC".$fdi].(strlen($_REQUEST["FGB".$i."DSC".$fdi])>0?"":"-");
											$fdjsonarr["fdtx".$fdi] = $fdtxi;
											if($fid>0)	{
												$fdjsonarr["fdnm".$fdi] = CFile::GetPath($fid);
											}
											$oreport .= '[!!!FGB PICT resize unsuccess]';
										}
									}
									
								}	else	{
									$crash_events=true;
									$oreport .= '[!!!FGB PICT param is empty]';
								}
							}
							
							//$oreport .= var_dump($fds_texts, true);
							
							
							
						}
						
						$fdjson .= "\"fdcnt\":\"".$fdi."\"";
						$fdjson .= "}";
						$fdjsonarr["fdcnt"] = $fdi;
						
						if(!$crash_events)	{
						
							if($SID > 0) {
								if(sizeof($SECTPROP)==0)	{
									$arBLowSectFilter = Array("IBLOCK_ID"=>114, "PROPERTY_SECTION_SID"=>$SID);
									$arBLowSectdSelect = Array("ID");
									
									$res1CPROD = CIBlockElement::GetList(Array(), $arBLowSectFilter, false, false, $arBLowSectdSelect);
									while($ar_fields_114 = $res1CPROD->GetNext())	{
										$SPROP_ELM = $ar_fields_114['ID'];
										/*$el = new CIBlockElement;

										$arLoadProductArray = Array(
										  "ACTIVE"         => "N"
										  );

										$PRODUCT_ID = $ar_fields_114['ID'];  // изменяем элемент с кодом (ID) 2
										$res = $el->Update($PRODUCT_ID, $arLoadProductArray);*/
									}
								}
							
								$arSFields["UF_GABARITS_FILES"] = $fds_files;
								$arSFields["UF_GABARITS_TEXTS"] = $fds_texts;
								$arSFields["UF_GBCHS_DIR"] = json_encode($fdjsonarr);
								//$el = new CIBlockElement;

								$SECTPROP["GABARITS_FILES"] = $fds_files;  // свойству с кодом 12 присваиваем значение "Белый"
								$SECTPROP["GABARITS_TEXTS"] = $fds_texts;
								$SECTPROP["SECTION_SID"] = $SID;// свойству с кодом 3 присваиваем значение 38

								/*$arLoadProductArray = Array(
								  //"MODIFIED_BY"    => $USER->GetID(), // элемент изменен текущим пользователем
								  "IBLOCK_SECTION_ID" => false,          // элемент лежит в корне раздела
								  "IBLOCK_ID"      => 114,
								  "PROPERTY_VALUES"=> $SECTPROP,
								  "NAME"           => "Элемент",
								  "ACTIVE"         => "Y",            // активен
								  "PREVIEW_TEXT"   => "текст для списка элементов",
								  "DETAIL_TEXT"    => "текст для детального просмотра"//,
								  //"DETAIL_PICTURE" => CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/image.gif")
								  );

								if($PRODUCT_ID = $el->Add($arLoadProductArray))
								  $oreport .=  "New ID: ".$PRODUCT_ID;
								else
								  $oreport .=  "Error: ".$el->LAST_ERROR;*/
							
							}
							
							if($_REQUEST["SETGABARITS_JSON".$i]=="1")	{
								if(isset($_REQUEST["GABARITS_JSON".$i]))	{
									$oreport .= '[SETGABARITS_JSON]';//str_replace("\r\n","",
									$arSFields["UF_CHGB_JSON"] = $_REQUEST["GABARITS_JSON".$i];
								}
							}
						
						}  else $oreport .= '[!!!UF_GABARITS_CRASHES]';
						
					}
					else  $oreport .= '[!!!UF_GABARITS][['.$_REQUEST["SETGABARITS_JSON".$i].",".$_REQUEST["FGBDC".$i]."]]";
					
					if($_REQUEST["SETGABARITS_JSON".$i]=="1"&&strlen($_REQUEST["GABARITS_JSON".$i])<50)	{
								if(isset($_REQUEST["GABARITS_JSON".$i]))	{
									$oreport .= '[SETGABARITS_JSON<50]';//str_replace("\r\n","",
									$arSFields["UF_CHGB_JSON"] = $_REQUEST["GABARITS_JSON".$i];
								}
							}
					
					if(isset($_REQUEST["FDOCDC".$i])&&($_REQUEST["SETDOCS_JSON".$i]=="1")&&strlen($_REQUEST["DOCS_JSON".$i])<8120)	{
						$oreport .= '[!!!UF_DOCS==>]';
						$fds_files = array();
						$fds_texts = array();
						$file_names = array();
						$crash_events=false;
						$fdjsonarr = array();
						for($fdi=0;$fdi<intval($_REQUEST["FDOCDC".$i]);$fdi++)	{
							
							$oreport .= '['.$fdi.'*]';
							if(intval($_REQUEST["FDOC".$i."FPSET".$fdi])==1)	{
								$oreport .= '['.$fdi.'**]';
								if(isset($_FILES["FDOC".$i."FP".$fdi]))	{
									//$oreport .= '['.$fdi.'***]'.$_REQUEST["FDOC".$i."FN".$fdi].", ".$_REQUEST["FDOC".$i."DSC".$fdi];
									
									$arTmpFile = CFile::MakeFileArray($_FILES["FDOC".$i."FP".$fdi]['tmp_name']);
									$arTmpFile['name'] = $_FILES["FDOC".$i."FP".$fdi]['name'];
									$fds_files[] = array("VALUE" => $arTmpFile,"DESCRIPTION"=>"sect doc");
									$file_names[] = array("VALUE" => $_FILES["FDOC".$i."FP".$fdi]['name'],"DESCRIPTION"=>"");
									$fds_texts[] = $_REQUEST["FDOC".$i."FN".$fdi].(strlen($_REQUEST["FDOC".$i."FN".$fdi])<=0?$_REQUEST["FDOC".$i."DSC".$fdi]:"").(strlen($_REQUEST["FDOC".$i."FN".$fdi].$_REQUEST["FDOC".$i."DSC".$fdi])>0?"":"-");
									//$oreport .= '['.$fdi.'***]';
									$arr_file=Array(
										"name" => $_FILES["FDOC".$i."FP".$fdi]['name'],
										"size" => $_FILES["FDOC".$i."FP".$fdi]['size'],
										"tmp_name" => $_FILES["FDOC".$i."FP".$fdi]['tmp_name'],
										"type" => "",
										"old_file" => "",
										"del" => "Y",
										"MODULE_ID" => "iblock");
									$fid = CFile::SaveFile($arr_file, "1c_katalog");
									if($fid>0)	{
										//CFile::GetPath($fid);
										$fdjsonarr["fdnm".$fdi] = CFile::GetPath($fid);
										$fdjsonarr["fdtx".$fdi] = $_REQUEST["FDOC".$i."FN".$fdi].(strlen($_REQUEST["FDOC".$i."FN".$fdi])<=0?$_REQUEST["FDOC".$i."DSC".$fdi]:"").(strlen($_REQUEST["FDOC".$i."FN".$fdi].$_REQUEST["FDOC".$i."DSC".$fdi])>0?"":"-");
										$oreport .= '[==>>'.$fdjsonarr["fdtx".$fdi].'***]';
									}	else	{
										$oreport .= '[Err of savefile]';
									}
									
								}	else	{
									$crash_events=true;
									$oreport .= '[!!!FDOC PICT param is empty]';
								}
							}
							
							//$oreport .= var_dump($fds_texts, true);
							
							
							
						}
						
						$fdjsonarr["fdcnt"] = $fdi;
						
						if(!$crash_events)	{
							
							if($SID > 0) {
								
								if(sizeof($SECTPROP)==0)	{
									$arBLowSectFilter = Array("IBLOCK_ID"=>114, "PROPERTY_SECTION_SID"=>$SID);
									$arBLowSectdSelect = Array("ID");
									
									$res1CPROD = CIBlockElement::GetList(Array(), $arBLowSectFilter, false, false, $arBLowSectdSelect);
									while($ar_fields_114 = $res1CPROD->GetNext())	{
										$SPROP_ELM = $ar_fields_114['ID'];
										/*$el = new CIBlockElement;

										$arLoadProductArray = Array(
										  "ACTIVE"         => "N"
										  );

										$PRODUCT_ID = $ar_fields_114['ID'];  // изменяем элемент с кодом (ID) 2
										$res = $el->Update($PRODUCT_ID, $arLoadProductArray);*/
									}
								}
							
								$arSFields["UF_DOCS_FILES"] = $fds_files;
								$arSFields["UF_DOCS_TEXTS"] = $fds_texts;
								$arSFields["UF_DOCS_DIR"] = json_encode($fdjsonarr);
								$arSFields["UF_DOCS_DIR"] = substr($arSFields["UF_DOCS_DIR"],0,1000);
								
								$SECTPROP["DOCS_FILES"] = $fds_files;  // свойству с кодом 12 присваиваем значение "Белый"
								$SECTPROP["DOCS_TEXTS"] = $fds_texts;
								$SECTPROP["FILE_NAMES"] = $file_names;
								$SECTPROP["SECTION_SID"] = $SID;// свойству с кодом 3 присваиваем значение 38
							
							}
							
							if($_REQUEST["SETDOCS_JSON".$i]=="1")	{
								if(isset($_REQUEST["DOCS_JSON".$i]))	{
									$oreport .= '[SETDOCS_JSON]';//str_replace("\r\n","",
									$arSFields["UF_DOCS_JSON"] = substr($_REQUEST["DOCS_JSON".$i],0,1000);
								}
							}
						
						} else $oreport .= '[!!!UF_DOCS_CRASHES]';
					}
					//else  $oreport .= '[!!!UF_DOCS]['.$_REQUEST["SETDOCS_JSON".$i]."]";
					
					if($_REQUEST["SETDOCS_JSON".$i]=="1"&&strlen($_REQUEST["DOCS_JSON".$i])<50)	{
								if(isset($_REQUEST["DOCS_JSON".$i]))	{
									$oreport .= '[SETDOCS_JSON<50]';//str_replace("\r\n","",
									$arSFields["UF_DOCS_JSON"] = $_REQUEST["DOCS_JSON".$i];
								}
							}
					
					if(sizeof($SECTPROP)>0)	{
							$el = new CIBlockElement;

							$arLoadProductArray = Array(
							  //"MODIFIED_BY"    => $USER->GetID(), // элемент изменен текущим пользователем
							  "IBLOCK_SECTION_ID" => false,          // элемент лежит в корне раздела
							  "IBLOCK_ID"      => 114,
							  "PROPERTY_VALUES"=> $SECTPROP,
							  "NAME"           => "Элемент",
							  "ACTIVE"         => "Y",            // активен
							  "PREVIEW_TEXT"   => "текст для списка элементов",
							  "DETAIL_TEXT"    => "текст для детального просмотра"//,
							  //"DETAIL_PICTURE" => CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/image.gif")
							  );

							if($SPROP_ELM)	{

								foreach($SECTPROP as $sp_key=>$sp_val)	{
									if(in_array($sp_key,array("GABARITS_FILES", "DESCRIPTS_FILES", "DOCS_FILES", "FILE_NAMES")))
										CIBlockElement::SetPropertyValuesEx($SPROP_ELM, 114, array($sp_key => array('VALUE' => array("del" => "Y"))));
									CIBlockElement::SetPropertyValues($SPROP_ELM, 114, $sp_val, $sp_key);
								}
								
							}	else	{
								if($PRODUCT_ID = $el->Add($arLoadProductArray))
									$oreport .=  "SECTPROP New ID: ".$PRODUCT_ID;
								else
								  $oreport .=  " SECTPROPError: ".$el->LAST_ERROR;
							}
					}
					
						//$arSFields["PICTURE"] = $_FILES["PICTURE".$i];//MASTER_CATALOG
					if(intval($_REQUEST["SETMASTER_CATALOG".$i])==1)	{
						if(isset($_FILES["MASTER_CATALOG".$i]))	{
							
							$arSFields["UF_MASTER_CATALOG"] = $_FILES["MASTER_CATALOG".$i];
							
						}	else	{
							$oreport .= '[!!!SECT MASTER_CATALOG param is empty]';
						}
					}
					
					if(intval($_REQUEST["SETSORT_ORDER".$i])==1)
					if(isset($_REQUEST["SORT_ORDER".$i]))	{
						$oreport .= '[SETSORT_ORDER]';//.$_REQUEST["SORT_ORDER".$i].'=>'.intval($_REQUEST["SORT_ORDER".$i]);
						$arSFields["SORT"] = intval($_REQUEST["SORT_ORDER".$i]);//$_REQUEST["SORT_ORDER".$i];
					}
						
					if(intval($_REQUEST["SETDESCRIPTION".$i])==1)
					if(isset($_REQUEST["DESCRIPTION".$i]))	{
						$oreport .= '[SETDESCRIPTION]';//.$desc.'=>'.$_REQUEST["DESCRIPTION".$i];
						$arSFields["DESCRIPTION"] = $_REQUEST["DESCRIPTION".$i];
						$arSFields["UF_ADD_DESCRIPTION"] = $_REQUEST["DESCRIPTION".$i];
					}
					if(intval($_REQUEST["SETSEO_ALIAS_URL".$i])==1)
					if(isset($_REQUEST["SEO_ALIAS_URL".$i]))	{
						$oreport .= '[SETSEO_ALIAS_URL]'.$_REQUEST["SEO_ALIAS_URL".$i];
						$arSFields["CODE"] = $_REQUEST["SEO_ALIAS_URL".$i];
					}
					if(intval($_REQUEST["SETSEO_TITLE".$i])==1)
					if(isset($_REQUEST["SEO_TITLE".$i]))	{
						$oreport .= '[SETSEO_TITLE]';
						$arSFields["UF_SEO_TITLE"] = $_REQUEST["SEO_TITLE".$i];
					}
					if(intval($_REQUEST["SETSEO_H1".$i])==1)
					if(isset($_REQUEST["SEO_H1".$i]))	{
						$oreport .= '[SETSEO_H1]'.$_REQUEST["SEO_H1".$i];
						$arSFields["UF_SEO_H1"] = $_REQUEST["SEO_H1".$i];
					}
					if(intval($_REQUEST["SETFULL_DESCRIPTION".$i])==1)
					if(isset($_REQUEST["FULL_DESCRIPTION".$i]))	{
						$oreport .= '[SETFULL_DESCRIPTION]';//.$_REQUEST["FULL_DESCRIPTION".$i];str_replace("\r\n","",
						$arSFields["UF_FULL_DESCRIPTION"] = $_REQUEST["FULL_DESCRIPTION".$i];
					}
					if(intval($_REQUEST["SETTYPE_COMPLETING".$i])==1)
					if(isset($_REQUEST["TYPE_COMPLETING".$i]))	{
						$oreport .= '[SETTYPE_COMPLETING]';//str_replace("\r\n","",
						$arSFields["UF_TYPE_COMPLETING"] = $_REQUEST["TYPE_COMPLETING".$i];
					}
					if(intval($_REQUEST["SETCHAR_GABARITS".$i])==1)
					if(isset($_REQUEST["CHAR_GABARITS".$i]))	{
						$oreport .= '[SETCHAR_GABARITS]';//str_replace("\r\n","",
						$arSFields["UF_CHAR_GABARITS"] = $_REQUEST["CHAR_GABARITS".$i];
					}
					if(intval($_REQUEST["SETDOCUMENTATION".$i])==1)
					if(isset($_REQUEST["DOCUMENTATION".$i]))	{
						$oreport .= '[SETDOCUMENTATION]';//str_replace("\r\n","",
						$arSFields["UF_DOCUMENTATION"] = $_REQUEST["DOCUMENTATION".$i];
					}
					if(intval($_REQUEST["SETSHORT_DESCRIPTION".$i])==1)
					if(isset($_REQUEST["SHORT_DESCRIPTION".$i]))	{
						$oreport .= '[SETSHORT_DESCRIPTION]';//str_replace("\r\n","",
						$arSFields["UF_SHORT_DESCRIPTION"] = $_REQUEST["SHORT_DESCRIPTION".$i];
					}
					if(intval($_REQUEST["SETVIDEO_DESCRIPTION".$i])==1)
					if(isset($_REQUEST["VIDEO_DESCRIPTION".$i]))	{
						$oreport .= '[SETVIDEO_DESCRIPTION]'.$ufvd.'=>'.$_REQUEST["VIDEO_DESCRIPTION".$i];//str_replace("\r\n","",
						$arSFields["UF_VIDEO_DESCRIPTION"] = $_REQUEST["VIDEO_DESCRIPTION".$i];
					}
					if(intval($_REQUEST["SETCOLLAPSEVC".$i])==1)
					if(isset($_REQUEST["COLLAPSEVC".$i]))	{
						$oreport .= '[SETCOLLAPSEVC]'.$ufvd.'=>'.$_REQUEST["COLLAPSEVC".$i];//str_replace("\r\n","",
						$arSFields["UF_COLLAPSEVC"] = $_REQUEST["COLLAPSEVC".$i];
					}
					//if(intval($_REQUEST["COLLAPSEVC".$i])==0)
					//	$arSFields["ACTIVE"] = 'N';
					if(intval($_REQUEST["SETADVANTS".$i])==1)
					if(isset($_REQUEST["ADVANTS".$i]))	{
						$oreport .= '[SETADVANTS]';//str_replace("\r\n","",
						$arSFields["UF_ADVANTS"] = $_REQUEST["ADVANTS".$i];
					}
					if(intval($_REQUEST["SETFILTER_PROPS".$i])==1)
					if(isset($_REQUEST["FILTER_PROPS".$i]))	{
						$oreport .= '[SETFILTER_PROPS]';//str_replace("\r\n","",
						//$sjson = json_encode(json_decode($_REQUEST["FILTER_PROPS".$i],true));
						$arSFields["UF_FILTER_PROPS"] = $_REQUEST["FILTER_PROPS".$i];
					}
					
                    
                    if($SID > 0)
                    {
                      $res = $bs->Update($SID, $arSFields);
                        
                        if(!$res)   {
                            $oreport .= '[1cSect UPD!!! Bad '.$bs->LAST_ERROR.']';
                            $errcnt++;
                        }   else
                        {
                            $oreport .= '[Sc'.$SID.']';
                        }    
                    }
                    else
                    {
                        $oreport .= '[No SID FIND XML_ID='.$sid_val.']';
                        //$errcnt++;
                    }
                    
                }
                else    {
                    $oreport .= '[No SID present]';
                    //$errcnt++;
                }
                
            }
        }
        else if($_REQUEST['ENTITY']=='1CPROD')    {
			$set_prop_array = array();
			$iblock_props = CIBlock::GetProperties( $ibid, array(), array());
			while($ibprop = $iblock_props->Fetch())    {
				if(($ibprop['PROPERTY_TYPE']=='S'||$ibprop['PROPERTY_TYPE']=='N'||$ibprop['PROPERTY_TYPE']=='L')&&$ibprop['MULTIPLE']=='N'&&$ibprop['ACTIVE']=='Y')  {
					//if($ibprop['PROPERTY_TYPE']=='L')	{
						//CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>false) );
					//}
					if($ibprop['CODE']!='artikul'&&$ibprop['CODE']!='BASE_PICTURE'&&$ibprop['CODE']!='IS_NEW_ITEM'&&$ibprop['CODE']!='PROPS_1C_CNT'&&$ibprop['CODE']!='MAIN_PICT')
						$set_prop_array[$ibprop['CODE']]=false;
				}
			}
			
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                $oreport .=  "{".$i."}";
                //CIBlockElement::SetPropertyValuesEx($_REQUEST['DEL_EKF_OBJ'], 103, array("REMOVED"=>"YES") );
                if(strlen($_REQUEST['PXMLID'.$i])>0) {
                    $pxml_id = str_replace("\r\n","",$_REQUEST['PXMLID'.$i]);
                    $ar1CProdFilter = Array("IBLOCK_ID"=> $ibid, "XML_ID"=>$pxml_id);//"!SECTION_ID"=>false,
                    $ar1CProdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "PROPERTY_ARTIKUL", "PREVIEW_PICTURE");
                    $res1CPROD = CIBlockElement::GetList(Array(), $ar1CProdFilter, false, false, $ar1CProdSelect);

                    if($ar_fields = $res1CPROD->GetNext())	{
                        
                        $PRODUCT_ID = $ar_fields['ID'];

                        $sectid = false;
                        $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                        $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                        $arUPFields = array();
						//$arUPFields["PROPERTY_VALUES"] = array();
                        
                        if(isset($_REQUEST['PGRXMLID'.$i])&&intval($_REQUEST['SETPGRXMLID'.$i])==1) {
                            $pgrid_val = str_replace("\r\n","",$_REQUEST['PGRXMLID'.$i]);
                            if(strlen($_REQUEST['PGRXMLID'.$i])>0) {
                                $ar1CSectFilter['XML_ID']=$pgrid_val;
                                $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                                if($ar_fields2 = $res1CSECT->GetNext())	{
                                    $sectid = $ar_fields2['ID'];
                                    $oreport .= "*0";
                                }
                            }
                            //$arUPFields = array();
							$arUPFields['IBLOCK_SECTION'] = $sectid;
                            $oreport .= "*00[".$sectid."]";
                        }
                        
                        if(intval($_REQUEST['SETPNAME'.$i])==1)    {
                            $arUPFields["NAME"] = str_replace("\r\n","",$_REQUEST['PNAME'.$i]);
							$arUPFields["CODE"] = Translit::UrlTranslit($arUPFields["NAME"].date('H_i_s'));
                            $oreport .= "*1";
                        }

						if(intval($_REQUEST['SETSORT_ORDER'.$i])==1&&isset($_REQUEST['SORT_ORDER'.$i]))    {
                            $arUPFields["SORT"] = intval($_REQUEST['SORT_ORDER'.$i]);
                            $oreport .= "**1";
                        }//else
                        //    $oreport .= "[unable SNAME set][{$_REQUEST['SETPNAME'.$i]}]";
					
						if(intval($_REQUEST['SETMAIN_PICT_ERR'.$i])==1)	{
							$oreport .= "[SETMAIN_PICT_ERR]";
							//$arUPFields["PROPERTY_VALUES"]["MAIN_PICT"] = $_REQUEST['MAIN_PICT'.$i];
							CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('MAIN_PICT'=>$_REQUEST['MAIN_PICT'.$i]) );
						}
					
						if(intval($_REQUEST['SETMAIN_PICT_DIR'.$i])==1)	{
							$oreport .= "[SETMAIN_PICT_DIR]";
							//$arUPFields["PROPERTY_VALUES"]["MAIN_PICT"] = $_REQUEST['MAIN_PICT'.$i];
							CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('MAIN_PICT'=>$_REQUEST['MAIN_PICT'.$i]) );
						}
					
						if(isset($_FILES['DETAIL_PICTURE'.$i])&&(intval($_REQUEST['SETDETAIL_PICTURE'.$i])==1)&&(intval($_REQUEST['SETMAIN_PICT'.$i])==1)) {
							$arrFile = array_merge( $_FILES["DETAIL_PICTURE".$i], array("del" => "N", "MODULE_ID" => "iblock"));

							$oreport .= "[SETMAIN_PICT]";
							$fres = CFile::CheckImageFile($arrFile, 10000000, 10800, 10800);
							$fres_small = CFile::CheckImageFile($arrFile, 10000000, 99, 99);
							if (strlen($fres)>0||strlen($fres_small)==0)    { 
								$oreport .= '[!!!PROD PICT check unsuccess]['.$fres.']';
								//$arUPFields["PROPERTY_VALUES"]["MAIN_PICT"] = $_REQUEST['MAIN_PICT'.$i];
								CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('MAIN_PICT'=>$_REQUEST['MAIN_PICT'.$i]) );
								$oreport .= "[SETMAIN_PICT1=".$_REQUEST['MAIN_PICT'.$i]."]";
							}
							else    {

								//$arUPFields["PROPERTY_VALUES"]["MAIN_PICT"] = $_REQUEST['MAIN_PICT'.$i];
								CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('MAIN_PICT'=>$_REQUEST['MAIN_PICT'.$i]) );
								$oreport .= "[SETMAIN_PICT2=".$_REQUEST['MAIN_PICT'.$i].",pid=".$PRODUCT_ID."]";
								
								$fMini = CFile::ResizeImageFile(      // 
									 $sourceFile = $_FILES['DETAIL_PICTURE'.$i]['tmp_name'],
									 $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES['DETAIL_PICTURE'.$i]['name'],
									 $arSize = array('width'=>500, 'height'=>500),
									 $resizeType = BX_RESIZE_IMAGE_PROPORTIONAL
									 //$arWaterMark = array(),
									 //$jpgQuality=false,
									 //$arFilters =false
									 );

								if($fMini)
									$arUPFields["DETAIL_PICTURE"] = CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize_big/".$_FILES['DETAIL_PICTURE'.$i]['name']);

								$f2res = CFile::CheckImageFile($arrFile, 100000, 100, 100);
								if (strlen($f2res)>0)    {   
									
									 $fMini = CFile::ResizeImageFile(      // 
									 $sourceFile = $_FILES['DETAIL_PICTURE'.$i]['tmp_name'],
									 $destinationFile =  $_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize/".$_FILES['DETAIL_PICTURE'.$i]['name'],
									 $arSize = array('width'=>100, 'height'=>100),
									 $resizeType = BX_RESIZE_IMAGE_EXACT
									 );
									
									if($fMini)  {
										if($fMini)
										$arUPFields["PREVIEW_PICTURE"] = CFile::MakeFileArray($_SERVER["DOCUMENT_ROOT"]."/upload/exchange_resize/".$_FILES['DETAIL_PICTURE'.$i]['name']);
									}
								}
								else    { 
									$arUPFields["PREVIEW_PICTURE"] = $_FILES['DETAIL_PICTURE'.$i];
								}
							}
							
						}
                        
                        if(sizeof($arUPFields)>0) {
							$arUPFields['ACTIVE']='Y';
							//$oreport .= var_export($arUPFields,true);
                            $obEl = new CIBlockElement();
                            if($obEl->Update($PRODUCT_ID,$arUPFields))   {
                                //$oreport .= "[PrGrSet success]";
                                $oreport .= "*2";
                            }   else    {
                                $oreport .= "[PrGrSet !!!Bad]".': '.$obEl->LAST_ERROR.']';
                                $errcnt++;
                            }
                        }
                        
                        if(intval($_REQUEST['SETPISPRICE'.$i])==1)    {
                            $PRICE_TYPE_ID = 2;
                            $arFields = Array(
                                "PRODUCT_ID" => $PRODUCT_ID,
                                "CATALOG_GROUP_ID" => $PRICE_TYPE_ID,
                                "PRICE" => floatval($_REQUEST['PISPRICE'.$i]),
                                "CURRENCY" => "RUB"
                                //"QUANTITY_FROM" => 1,
                                //"QUANTITY_TO" => 10
                            );

                            $res = CPrice::GetList(
                                    array(),
                                    array(
                                            "PRODUCT_ID" => $PRODUCT_ID,
                                            "CATALOG_GROUP_ID" => $PRICE_TYPE_ID
                                        )
                                );

                            if ($arr = $res->Fetch())
                            {
                                if(CPrice::Update($arr["ID"], $arFields))   {
                                    //$oreport .= "[ISPrUpd sucess]";
                                    //$oreport .= "*3";
                                }
                                else    {
                                    $oreport .= "[ISPrUpd !!!bad]";
                                    $errcnt++;
                                }
                            }
                            else
                            {
                                if(CPrice::Add($arFields))  {
                                    //$oreport .= "[ISPrAdd sucess]";
                                    //$oreport .= "*4";
                                }
                                else    {
                                    $oreport .= "[ISPrAdd !!!Bad]";
                                    $errcnt++;
                                }
                            }
                        }
                        //else
                        //    $oreport .= "[unable ISPrAdd set][{$_REQUEST['SETPISPRICE'.$i]}]";
                        
                        if(intval($_REQUEST['SETPBPRICE'.$i])==1)    {
                            $PRICE_TYPE_ID = 4;
                            $arFields = Array(
                                "PRODUCT_ID" => $PRODUCT_ID,
                                "CATALOG_GROUP_ID" => $PRICE_TYPE_ID,
                                "PRICE" => floatval($_REQUEST['PBPRICE'.$i]),
                                "CURRENCY" => "RUB"
                                //"QUANTITY_FROM" => 1,
                                //"QUANTITY_TO" => 10
                            );

                            $res = CPrice::GetList(
                                    array(),
                                    array(
                                            "PRODUCT_ID" => $PRODUCT_ID,
                                            "CATALOG_GROUP_ID" => $PRICE_TYPE_ID
                                        )
                                );

                            if ($arr = $res->Fetch())
                            {
                                if(CPrice::Update($arr["ID"], $arFields))   {
                                    //$oreport .= "[BasePrUpd sucess]";
                                    //$oreport .= "*5";
                                }
                                else    {
                                    $oreport .= "[BasePrUpd !!!BAD]";
                                    $errcnt++;
                                }
                            }
                            else
                            {
                                if(CPrice::Add($arFields))  {
                                    //$oreport .= "[BasePrAdd sucess]";
                                    //$oreport .= "*6";
                                }
                                else    {
                                    $oreport .= "[BasePrAdd !!!Bad]";
                                    $errcnt++;
                                }
                            }
                        }
                        //else
                        //    $oreport .= "[unable BasePr set][{$_REQUEST['SETPBPRICE'.$i]}]";
                        
                        if(intval($_REQUEST['SETPQUANT'.$i])==1)    {
                            if(!CCatalogProduct::GetByID($PRODUCT_ID))  {
                                $arcFields = array(
                                              "ID" => $PRODUCT_ID, 
                                              "QUANTITY" => floatval($_REQUEST['PQUANT'.$i]), //выставляем тип ндс (задается в админке)  
                                              "QUANTITY_TRACE" => "Y" //НДС входит в стоимость
                                              );
                                if(CCatalogProduct::Add($arcFields))    {
                                    //$oreport .= "[CatAdd sucess".$PRODUCT_ID.']';
                                    //$oreport .= "*7";
                                }
                                else    {
                                    $oreport .= "[CatAdd !!!Bad".$PRODUCT_ID.']';
                                    $errcnt++;
                                }
                            }
                            else    {
                                $arFields = array('QUANTITY_TRACE'=>'Y','QUANTITY'=>floatval($_REQUEST['PQUANT'.$i]));// зарезервированное количество
                                if(CCatalogProduct::Update($PRODUCT_ID, $arFields)) {
                                    //$oreport .= "[CatUpd success]";
                                    //$oreport .= "*8";
                                }
                                else    {
                                    $oreport .= "[CatUpd !!!Bad".$PRODUCT_ID.']';
                                    $errcnt++;
                                }
                            }
                        }
                        //else
                        //    $oreport .= "[unable Quant set][{$_REQUEST['SETPQUANT'.$i]}]";

                        //$oreport .= "[[".$PRODUCT_ID."]]";
						
						
						if(isset($_REQUEST['PPROPS'.$i]))   {
							//$oreport .= "***-5";
							if(strlen($_REQUEST['PPROPS'.$i])>0)   {
								//$oreport .= "***-4";
								$pprops = json_decode($_REQUEST['PPROPS'.$i],true);
								if(isset($pprops)) {
									//$oreport .= "***-3";
									if(isset($pprops['dc']))    {
										//$oreport .= "***-2";
										if(sizeof($set_prop_array)>0)
										if(intval($_REQUEST['OCNT'])<210||intval($pprops['dc'])>0)	{
											//CIBlockElement::SetPropertyValues($PRODUCT_ID, $ibid, array('artikul'=>$_REQUEST['PART'.$i]) );
											$set_prop_array['artikul']=$_REQUEST['PART'.$i];
											unset($set_prop_array['MAIN_PICT']);
											unset($set_prop_array['IS_NEW_ITEM']);
											unset($set_prop_array['PROPS_1C_CNT']);
											unset($set_prop_array['BASE_PICTURE']);
											CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, $set_prop_array );
											$oreport .= "*9";
										}
										
										//if($_REQUEST['PART'.$i]!='mdb-47-grey-pro')
										for($ii=0;$ii<intval($pprops['dc']);$ii++)  {
											$iblock_props = CIBlock::GetProperties( $ibid, array(), array('XML_ID'=>$pprops['c'.$ii]));
											$oreport .= "***-1";	
											$has_field=false;
											if($ibprop = $iblock_props->Fetch())    {
												$has_field=true;
												$oreport .= "***0";
											}	else	{
												$oreport .= "***1";
												if(isset($pprops['n'.$ii]))	{
													
													//$ibprop['PROPERTY_TYPE']='L';
													//$ibprop['MULTIPLE']='N';
													$PCODE=Translit::UrlTranslit($pprops['n'.$ii]);
													$PCODE=substr($PCODE,0,40);
													$iblock_props22 = CIBlock::GetProperties( $ibid, array(), array('CODE'=>$PCODE));
													$nct=0;
													while($ibprop22 = $iblock_props22->Fetch())    {
														$PCODE=$PCODE.$nct;
														$iblock_props22 = CIBlock::GetProperties( $ibid, array(), array('CODE'=>$PCODE));
														$nct++;
														if($nct>999) break;
													}
													
													$iblock_props = CIBlock::GetProperties( $ibid, array(), array('XML_ID'=>$pprops['c'.$ii]));
													$ibprop = Array(
													  "NAME" => $pprops['n'.$ii],
													  "ACTIVE" => "Y",
													  "XML_ID"=>$pprops['c'.$ii],
													  "SORT" => "555",
													  "CODE" => $PCODE,
													  "PROPERTY_TYPE" => "L",
													  "MULTIPLE"=>'N',
													  "IBLOCK_ID" => $ibid
													  );
													
													$oreport .= "***2";
													$ibp = new CIBlockProperty;
													if($PropID = $ibp->Add($ibprop))	{
														$has_field=true;
														$ibprop['ID'] = $PropID;
														$oreport .= "***3";
													}
													else
														$oreport .= '[Err create prop ext_code '.$pprops['c'.$ii].']';
													
												}
											}
											if($has_field)	{
											if(strlen($pprops['v'.$ii])>0)    {
												if($ibprop['PROPERTY_TYPE']=='L'&&$ibprop['MULTIPLE']=='N')   {
													$enres = CIBlockProperty::GetPropertyEnum( $ibprop['ID'], array(), array());
													$has_in_en=false;
													while($eni = $enres->Fetch())   {
														if($eni['VALUE']==$pprops['v'.$ii])
														{
															$has_in_en=true;
															CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>$eni['ID']) );
															break;
														}
													}
													if(!$has_in_en)  {
														
														$ibpenum = new CIBlockPropertyEnum;
														if($PropID = $ibpenum->Add(Array('PROPERTY_ID'=>$ibprop['ID'], 'VALUE'=>$pprops['v'.$ii]))) {//$pprops['v'.$ii]
															//CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('STATUS_TOVARA'=>$ibprop['CODE']."===".$PropID) );
															CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>$PropID) );
														}   else    {
															$oreport .= '[Uns add listval, code '.$ibprop['CODE'].'ID='.$ibprop['ID'].'val='.$pprops['v'.$ii].'!]';
															$errcnt++;
														}
													}
												}
												else if(($ibprop['PROPERTY_TYPE']=='S'||$ibprop['PROPERTY_TYPE']=='N')&&$ibprop['MULTIPLE']=='N')   {
												CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array($ibprop['CODE']=>$pprops['v'.$ii]) );    }
											}
											}   else    {
												$oreport .= '[Nfind prop ext_code '.$pprops['c'.$ii].']';
												$errcnt++;
											}
										}
										
									}
								}   else    {
									switch (json_last_error()) {
										case JSON_ERROR_NONE:
											//$oreport .=  ' - Ошибок нет';
										break;
										case JSON_ERROR_DEPTH:
											$oreport .= '[JSON PARSE ERROR - Достигнута максимальная глубина стека]';
											$errcnt++;
										break;
										case JSON_ERROR_STATE_MISMATCH:
											$oreport .= '[JSON PARSE ERROR - Некорректные разряды или не совпадение режимов]';
											$errcnt++;
										break;
										case JSON_ERROR_CTRL_CHAR:
											$oreport .= '[JSON PARSE ERROR - Некорректный управляющий символ]';
											$errcnt++;
										break;
										case JSON_ERROR_SYNTAX:
											$oreport .= '[JSON PARSE ERROR - Синтаксическая ошибка, не корректный JSON]';
											$errcnt++;
										break;
										case JSON_ERROR_UTF8:
											$oreport .= '[JSON PARSE ERROR - Некорректные символы UTF-8, возможно неверная кодировка]';
											$errcnt++;
										break;
										default:
											$oreport .= '[JSON PARSE ERROR - Неизвестная ошибка]';
											$errcnt++;
										break;
									}
								}
									
							}//End of pprops analyze
							else
								$oreport .= '[No PROPS present2]';
						}//End of pprops analyze
						else
							$oreport .= '[No PROPS present1]';
						
						if($ar_fields['PROPERTY_ARTIKUL_VALUE']!=$_REQUEST['PART'.$i]||strlen($ar_fields['PROPERTY_ARTIKUL_VALUE'])<=0)	{
							$oreport .= '[[SET_ART]'.$ar_fields['PROPERTY_ARTIKUL_VALUE'].'=>'.$_REQUEST['PART'.$i]."]";
							CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('artikul'=>$_REQUEST['PART'.$i]) );
							CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('ARTIKUL'=>$_REQUEST['PART'.$i]) );
						}
						
						$sel_rcnt=0;
								$db_props = CIBlockElement::GetProperty($ibid, $PRODUCT_ID, array(), Array("EMPTY"=>'Y'));//"PROPERTY_TYPE"=>"L", 
								if($db_props)	{
									$sel_rcnt=$db_props->SelectedRowsCount();
								}
								CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('PROPS_1C_CNT'=>$sel_rcnt) );
						
						while($dar_fields = $res1CPROD->GetNext())	{
							$PRODUCT_ID = $dar_fields['ID'];
							//if($dar_fields['PROPERTY_ARTIKUL_VALUE']!=$_REQUEST['PART'.$i]||strlen($dar_fields['PROPERTY_ARTIKUL_VALUE'])<=0)	{
							//	CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('artikul'=>$_REQUEST['PART'.$i]) );
							//	CIBlockElement::SetPropertyValuesEx($PRODUCT_ID, $ibid, array('ARTIKUL'=>$_REQUEST['PART'.$i]) );
							//}
							if(!CIBlockElement::Delete($PRODUCT_ID))
							{
								$oreport .= "[Success clone delete]".$PRODUCT_ID;
							}
							else
								$oreport .= "[Bad clone delete]".$PRODUCT_ID;
						}
                    }
                    else    {
                        $oreport .= "[[".$i."]][[Not find product by PrXMLID]]";
                        $errcnt++;
                    }
                }
                else
                    $oreport .= "[[".$i."]][[Missing PrXMLID]][[{$_REQUEST['PXMLID'.$i]}]]";
            }
            
        }
        else    {
            $oreport .= "[Unknown ENTITY param value]";
            $errcnt++;
        }
        
    }//End UPDATE Operation
    
    else if($_REQUEST['OTYPE']=="DELETE")   {
        if($_REQUEST['ENTITY']=='BSECT')    {
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                
            }
        }
        else if($_REQUEST['ENTITY']=='1CSECT')    {
            $oreport .=  "[OCNT:".$_REQUEST['OCNT']."]";
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                $oreport .=  "{".$i."}";
                $bs = new CIBlockSection;
                $SID=0;
                $res=false;
                $sid_val="NO_PRESENT_SID, BX_ID=".$_REQUEST['SBXID'.$i];
                
                $arSFields = Array(
                  "ACTIVE" => 'N'
                  );
                
                $ar1CSectFilter = Array("IBLOCK_ID"=>$ibid);
                $ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                if(isset($_REQUEST['SID'.$i])) {
                    $sid_val = str_replace("\r\n","",$_REQUEST['SID'.$i]);
                    //if(strlen($_REQUEST['SID'.$i])==36)    {
                        $ar1CSectFilter['XML_ID']=$sid_val;
                        $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
                        if($ar_fields = $res1CSECT->GetNext())	{
                            $SID = $ar_fields['ID'];
							while($ar_fields = $res1CSECT->GetNext())	{
								if(!CIBlockSection::Delete($ar_fields['ID']))
								{
									$oreport .= '[SECT delete success]'.$ar_fields['ID'];
								}
								else
									$oreport .= '[!!!SECT delete unsuccess]'.$ar_fields['ID'];
							}
                        }
                }
                if($SID==0)    {
                    if(isset($_REQUEST['SBXID'.$i])) {
                        $SID = intval($_REQUEST['SBXID'.$i]);
                    }
                    else    {
                        $oreport .= '[No SID or SBXID present]';
                    }
                }
                    
                if($SID > 0)
                    {
                      $res = $bs->Update($SID, $arSFields);
                        
                        if(!$res)   {
                            $oreport .= '[1cSect DEL!!! Bad '.$bs->LAST_ERROR.']';
                            $errcnt++;
                        }   else
                        {
                            $oreport .= '[1cSect DEL!!! Succ ['.$SID.']]';
                            $critical_info.='DelSECT ['.$SID.'] nm='.$_REQUEST['SNAME'.$i];
                        }    
                    }
                else
                    {
                        $oreport .= '[No SID FIND XML_ID='.$sid_val.']';
                        //$errcnt++;
                    }
                    
                
                
            }
        }
        else if($_REQUEST['ENTITY']=='1CPROD')    {
            for($i=0;$i<intval($_REQUEST['OCNT']);$i++) {
                $oreport .=  "{".$i."}";
                if(strlen($_REQUEST['PID'.$i])>0) {
                    $pid_val = str_replace("\r\n","",$_REQUEST['PID'.$i]);
                    $ar1CProdFilter = Array("IBLOCK_ID"=> $ibid, "XML_ID"=>$pid_val);//"!SECTION_ID"=>false,
                    $ar1CProdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");
                    $res1CPROD = CIBlockElement::GetList(Array(), $ar1CProdFilter, false, false, $ar1CProdSelect);

                    while($ar_fields = $res1CPROD->GetNext())	{
                        $PRODUCT_ID = $ar_fields['ID'];
                        //$PRODUCT_ID = $_REQUEST['PID'.$i];
                        $obEl = new CIBlockElement();
                        if($obEl->Update($PRODUCT_ID,array('ACTIVE' => 'N')))   {
                            $oreport .= "[ProdDel success]";
                        }   else    {
                            $oreport .= "[ProdDel !!!Bad]";
                            $errcnt++;
                        }
                        $oreport .= "[[".$PRODUCT_ID."]]";
                    }
                }
            }
        }
        else    {
            $oreport .= "[Unknown ENTITY param value]";
            $errcnt++;
        }
        
    }//End DELETE Operation
    
    else    {
        $oreport .= "[Unknown OTYPE param value]";
        $errcnt++;
    }
    
        
}
else    {
    $oreport .= "[Missed params]".(!isset($_REQUEST['ENTITY'])?'ENTITY':"").(!isset($_REQUEST['OTYPE'])?'OTYPE':"").(!isset($_REQUEST['OCNT'])?'OCNT['.$_REQUEST['OCNT'].']':"");
    $errcnt++;
}
echo json_encode(array('oreport'=>$oreport, 'errcnt'=>$errcnt, 'critical_info'=>$critical_info, 'critical_errs'=>$critical_errs));
?>