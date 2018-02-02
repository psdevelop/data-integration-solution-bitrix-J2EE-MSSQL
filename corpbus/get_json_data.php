<?require($_SERVER["DOCUMENT_ROOT"]."/bitrix/modules/main/include/prolog_before.php");?>
<?CModule::IncludeModule('iblock');
CModule::IncludeModule('catalog');
?>

<?

function addUF($ufname, $iblock_id_val, $ftype='string', $multiple='N', $ssz='20')	{
	/** Добавление пользовательского свойства*/
	$oUserTypeEntity    = new CUserTypeEntity;
	$aUserFields    = array(
		'ENTITY_ID'         => 'IBLOCK_'.$iblock_id_val.'_SECTION',
		'FIELD_NAME'        => $ufname,
		'USER_TYPE_ID'      => $ftype,
		'SORT'              => 555,
		'MULTIPLE'          => $multiple,
		'EDIT_FORM_LABEL'   => array(
			'ru'    => $ufname, 'en'    => $ufname,),
	);
	
	if(strlen($ssz)>0)	
		$aUserFields['SETTINGS']=array(
			'SIZE'          => $ssz,
			'ROWS'          => '1',
		);
	
	$obUserField  = new CUserTypeEntity;
	if($res = $obUserField->Add($aUserFields)) return false;
	else return true;
	//double - Число
	//string - Строка
	//file - Файл
}

$ibid = 116;

$iblock_props = CIBlock::GetProperties( $ibid, array(), array('CODE'=>'MAIN_PICT'));	
if($ibprop = $iblock_props->Fetch())    { }	else	{
	$ibprop = Array(
		"NAME" => 'Есть изображение',
		"ACTIVE" => "Y",
		"XML_ID"=>'0000000000-0000000001',
		"SORT" => "555",
		"CODE" => 'MAIN_PICT',
	    "PROPERTY_TYPE" => "S",
        "MULTIPLE"=>'N',
	    "IBLOCK_ID" => $ibid);

		$ibp = new CIBlockProperty;
		if($PropID = $ibp->Add($ibprop))	{ }
		else $oreport .= '[Err create prop MAIN_PICT]';
}

$iblock_props = CIBlock::GetProperties( $ibid, array(), array('CODE'=>'PROPS_1C_CNT'));	
if($ibprop = $iblock_props->Fetch())    { }	else	{
	$ibprop = Array(
		"NAME" => 'Количество свойств',
		"ACTIVE" => "Y",
		"XML_ID"=>'0000000000-0000000004',
		"SORT" => "555",
		"CODE" => 'PROPS_1C_CNT',
	    "PROPERTY_TYPE" => "N",
        "MULTIPLE"=>'N',
	    "IBLOCK_ID" => $ibid);

		$ibp = new CIBlockProperty;
		if($PropID = $ibp->Add($ibprop))	{ }
		else $oreport .= '[Err create prop PROPS_1C_CNT]';
}

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
		else $oreport .= '[Err create prop artikul]';
}

$iblock_props = CIBlock::GetProperties( $ibid, array(), array('CODE'=>'BASE_PICTURE'));	
if($ibprop = $iblock_props->Fetch())    { }	else	{
	$ibprop = Array(
		"NAME" => 'Основное изображение',
		"ACTIVE" => "Y",
		"XML_ID"=>'0000000000-0000000002',
		"SORT" => "555",
		"CODE" => 'BASE_PICTURE',
	    "PROPERTY_TYPE" => "F",
        "MULTIPLE"=>'N',
	    "IBLOCK_ID" => $ibid);

		$ibp = new CIBlockProperty;
		if($PropID = $ibp->Add($ibprop))	{ }
		else $oreport .= '[Err create prop BASE_PICTURE]';
}

$iblock_props = CIBlock::GetProperties( $ibid, array(), array('CODE'=>'IS_NEW_ITEM'));	
if($ibprop = $iblock_props->Fetch())    { }	else	{
	$ibprop = Array(
		"NAME" => 'Новинка',
		"ACTIVE" => "Y",
		"XML_ID"=>'0000000000-0000000003',
		"SORT" => "555",
		"CODE" => 'IS_NEW_ITEM',
	    "PROPERTY_TYPE" => "N",
        "MULTIPLE"=>'N',
	    "IBLOCK_ID" => $ibid);

		$ibp = new CIBlockProperty;
		if($PropID = $ibp->Add($ibprop))	{ }
		else $oreport .= '[Err create prop IS_NEW_ITEM]';
}

$missed_uf=false;

$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_FULL_DESCRIPTION", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_FULL_DESCRIPTION", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_TYPE_COMPLETING", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_TYPE_COMPLETING", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_CHAR_GABARITS", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_CHAR_GABARITS", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DOCUMENTATION", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DOCUMENTATION", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_SHORT_DESCRIPTION", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_SHORT_DESCRIPTION", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_VIDEO_DESCRIPTION", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_VIDEO_DESCRIPTION", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_ADD_DESCRIPTION", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_ADD_DESCRIPTION", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_BASE_PICTURE", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_BASE_PICTURE", $ibid, 'file', 'N', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_MASTER_CATALOG", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_MASTER_CATALOG", $ibid, 'file', 'N', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_COLLAPSEVC", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_COLLAPSEVC", $ibid, 'double', 'N', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_ADVANTS", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_ADVANTS", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_FILTER_PROPS", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_FILTER_PROPS", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DESCRIPTS_JSON", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DESCRIPTS_JSON", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_GABARITS_JSON", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_GABARITS_JSON", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DOCS_JSON", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DOCS_JSON", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DESCRIPTS_FILES", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DESCRIPTS_FILES", $ibid, 'file', 'Y', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_GABARITS_FILES", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_GABARITS_FILES", $ibid, 'file', 'Y', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DOCS_FILES", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DOCS_FILES", $ibid, 'file', 'Y', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DESCRIPTS_TEXTS", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DESCRIPTS_TEXTS", $ibid, 'string', 'Y', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_GABARITS_TEXTS", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_GABARITS_TEXTS", $ibid, 'string', 'Y', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DOCS_TEXT", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DOCS_TEXT", $ibid, 'string', 'Y', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_CHGB_JSON", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_CHGB_JSON", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_SORT_ORDER", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_SORT_ORDER", $ibid, 'integer', 'N', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_SEO_TITLE", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_SEO_TITLE", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_SEO_H1", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_SEO_H1", $ibid, 'string', 'N', '20')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_GBCHS_DIR", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_GBCHS_DIR", $ibid, 'string', 'N', '50')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DCS_DIR", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DCS_DIR", $ibid, 'string', 'N', '50')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_DOCS_DIR", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_DOCS_DIR", $ibid, 'string', 'N', '50')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_HOT_ORDER", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_HOT_ORDER", $ibid, 'integer', 'N', '')) $missed_uf=true;	}
$rsData = CUserTypeEntity::GetList( array(), array("FIELD_NAME"=>"UF_PICTURE_PATH", "ENTITY_ID"=>'IBLOCK_'.$ibid.'_SECTION') );
if($arRes = $rsData->Fetch()) { }	else { if(addUF("UF_PICTURE_PATH", $ibid, 'string', 'N', '50')) $missed_uf=true;	}

if($missed_uf) die("MISSED UFS");

$arBSectFilter = Array("IBLOCK_ID"=>6);
$arBSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID");//, "PROPERTY_privyazka"

$arBLowSectFilter = Array("IBLOCK_ID"=>6);
$arBLowSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "PROPERTY_PRIVYAZKA");//

$ar1CSectFilter = Array("IBLOCK_ID"=>$ibid, 'ACTIVE'=>'Y');
if(isset($_REQUEST['FILL_PICT']))
	$ar1CSectdSelect = Array();//UF_BASE_PICTURE, UF_MASTER_CATALOG
else
$ar1CSectdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "PICTURE", "DESCRIPTION", "UF_FULL_DESCRIPTION", "UF_TYPE_COMPLETING", "UF_CHAR_GABARITS", "UF_DOCUMENTATION", "UF_SHORT_DESCRIPTION", "UF_VIDEO_DESCRIPTION", "UF_ADD_DESCRIPTION", "DETAIL_PICTURE", "UF_BASE_PICTURE", "UF_MASTER_CATALOG","UF_COLLAPSEVC", "UF_ADVANTS", "UF_FILTER_PROPS","UF_DESCRIPTS_JSON", "UF_CHGB_JSON", "UF_DOCS_JSON", "UF_SORT_ORDER", "SORT", "CODE", "UF_SEO_TITLE", "UF_SEO_H1", "UF_PICTURE_PATH");

$ar1CProdFilter = Array("IBLOCK_ID"=> $ibid, 'ACTIVE'=>'Y');//"!SECTION_ID"=>false,
$ar1CProdSelect = Array("ID", "NAME", "IBLOCK_SECTION_ID", "XML_ID", "PROPERTY_ARTIKUL", "PREVIEW_PICTURE", "SORT", "DETAIL_PICTURE", "PROPERTY_MAIN_PICT", "PROPERTY_PROPS_1C_CNT");

if(isset($_REQUEST['ENTITY']))  {

    if($_REQUEST['ENTITY']=='BSECT')    {
        $resBSECT = CIBlockSection::GetList(Array(), $arBSectFilter, false, $arBSectdSelect);
        $dataArray = array();
        while($ar_fields = $resBSECT->GetNext())	{
            $xml_id = "NULL";
            if(isset($ar_fields['XML_ID']))
                $xml_id = $ar_fields['XML_ID'];
            $parent_id = "-1";
            if(isset($ar_fields['IBLOCK_SECTION_ID']))
                $parent_id = $ar_fields['IBLOCK_SECTION_ID'];
            $dataArray[] = array( 'ID'=>$ar_fields['ID'], '1C_ID'=>$xml_id, 'NAME'=>$ar_fields['NAME'], 'PARENT_ID'=>$parent_id, 'LINK_1CSECT_BX_ID'=>"-1", 'LINK_1CSECT_1C_ID'=>"NULL");
        }
        $resBLowSect = CIBlockElement::GetList(Array(), $arBLowSectFilter, false, false, $arBLowSectdSelect);
        while($ar_fields = $resBLowSect->GetNext())	{
            $xml_id = "NULL";
            if(isset($ar_fields['XML_ID']))
                $xml_id = $ar_fields['XML_ID'];
            $parent_id = "-1";
            if(isset($ar_fields['IBLOCK_SECTION_ID']))
                $parent_id = $ar_fields['IBLOCK_SECTION_ID'];
            $link_1csect = "-1";
            if(isset($ar_fields['PROPERTY_PRIVYAZKA_VALUE']))
                $link_1csect = $ar_fields['PROPERTY_PRIVYAZKA_VALUE'];
            
            $dataArray[] = array( 'ID'=>$ar_fields['ID'], '1C_ID'=>$xml_id, 'NAME'=>$ar_fields['NAME'], 'PARENT_ID'=>$parent_id, 'LINK_1CSECT_BX_ID'=>$link_1csect, 'LINK_1CSECT_1C_ID'=>"NULL");
        }
        echo json_encode($dataArray);
    }
    else if($_REQUEST['ENTITY']=='1CSECT')    {
        $res1CSECT = CIBlockSection::GetList(Array(), $ar1CSectFilter, false, $ar1CSectdSelect);
        $dataArray = array();
        $ext_ids=array();
		//$dataArray[] = $ar1CSectFilter;
		//$dataArray[] = $ar1CSectdSelect;
        while($ar_fields = $res1CSECT->GetNext())	{
			//$dataArray[] = 'ddd';
            $xml_id = "NULL";
            if(isset($ar_fields['XML_ID']))
                $xml_id = $ar_fields['XML_ID'];
            $parent_id = "-1";
            $parent_1c_id = "NULL";
			
            if(isset($ar_fields['IBLOCK_SECTION_ID']))  {
                $parent_id = $ar_fields['IBLOCK_SECTION_ID'];
                
                $res1CPRODSECT = CIBlockSection::GetList(Array(), Array("IBLOCK_ID"=>$ibid, 'ID'=>$parent_id), false, array('ID','XML_ID'));
                if ($ar_fields_ps = $res1CPRODSECT->GetNext())	{
                    $parent_1c_id = $ar_fields_ps["XML_ID"];
                }
            }
            $has_ext_id=false;
			$picture=(isset($ar_fields['UF_PICTURE_PATH'])?$ar_fields['UF_PICTURE_PATH']:"");
			//if($ar_fields['PICTURE']) $picture="yes";
			//else	{
			//	if($ar_fields['UF_BASE_PICTURE']) $picture="yes";
			//}
			$mcatalog="";
			if($ar_fields['UF_MASTER_CATALOG']) $mcatalog="yes";
			//if(isset($_REQUEST['FILL_PICT'])) {//&&strlen($picture)<=0)
			//	print_r($ar_fields); 
			//	continue;
			//}
			$decription=""; if($ar_fields['UF_ADD_DESCRIPTION']) $decription=$ar_fields['UF_ADD_DESCRIPTION'];
			$full_decription=""; if($ar_fields['UF_FULL_DESCRIPTION']) $full_decription=$ar_fields['UF_FULL_DESCRIPTION'];
			$type_completing=""; if($ar_fields['UF_TYPE_COMPLETING']) $type_completing=$ar_fields['UF_TYPE_COMPLETING'];
			$char_gabarits=""; if($ar_fields['UF_CHAR_GABARITS']) $char_gabarits=$ar_fields['UF_CHAR_GABARITS'];
			$documentation=""; if($ar_fields['UF_DOCUMENTATION']) $documentation=$ar_fields['UF_DOCUMENTATION'];
			$short_description=""; if($ar_fields['UF_SHORT_DESCRIPTION']) $short_description=$ar_fields['UF_SHORT_DESCRIPTION'];
            $video_description=""; if($ar_fields['UF_VIDEO_DESCRIPTION']) $video_description=$ar_fields['UF_VIDEO_DESCRIPTION'];
			$collapsevc=""; if($ar_fields['UF_COLLAPSEVC']) $collapsevc=$ar_fields['UF_COLLAPSEVC'];
			$advants=""; if($ar_fields['UF_ADVANTS']) $advants=$ar_fields['UF_ADVANTS'];
			$fprops=""; if($ar_fields['UF_FILTER_PROPS']) $fprops=$ar_fields['UF_FILTER_PROPS'];
			$descjson=""; if($ar_fields['UF_DESCRIPTS_JSON']) $descjson=$ar_fields['UF_DESCRIPTS_JSON'];
			$gbjson=""; if($ar_fields['UF_CHGB_JSON']) $gbjson=$ar_fields['UF_CHGB_JSON'];
			$docjson=""; if($ar_fields['UF_DOCS_JSON']) $docjson=$ar_fields['UF_DOCS_JSON'];
			$sort_order=0; if($ar_fields['SORT']) $sort_order="".$ar_fields["SORT"];
           
			if(strlen($xml_id)==36&&in_array($xml_id,$ext_ids)) $has_ext_id=true;
            if(!$has_ext_id)
            $dataArray[] = array( 'ID'=>$ar_fields['ID'], '1C_ID'=>$xml_id, 'NAME'=>$ar_fields['NAME'], 'PARENT_ID'=>$parent_id, 'PARENT_1CID'=>$parent_1c_id, 'PICTURE'=>$picture, 'DESCRIPTION'=>$decription, 'FULL_DESCRIPTION'=>$full_decription, 'TYPE_COMPLETING'=>$type_completing, 'CHAR_GABARITS'=>$char_gabarits, 'DOCUMENTATION'=>$documentation, 'SHORT_DESCRIPTION'=>$short_description, "VIDEO_DESCRIPTION"=>$video_description, "MASTER_CATALOG"=>$mcatalog, "COLLAPSEVC"=>$collapsevc, "ADVANTS"=>$advants, "FILTER_PROPS"=>$fprops, "DESCRIPTS_JSON"=>$descjson, "GABARITS_JSON"=>$gbjson, "DOCS_JSON"=>$docjson, "SORT_ORDER"=>$sort_order, "SEO_ALIAS_URL"=>(isset($ar_fields['CODE'])?$ar_fields['CODE']:""), "SEO_TITLE"=>(isset($ar_fields['UF_SEO_TITLE'])?$ar_fields['UF_SEO_TITLE']:""), "SEO_H1"=>(isset($ar_fields['UF_SEO_H1'])?$ar_fields['UF_SEO_H1']:"") );
            if(strlen($xml_id)==36) {
                $ext_ids[]=$xml_id;
            }
        }
        echo json_encode($dataArray);
    }
    else if($_REQUEST['ENTITY']=='1CPROD')    {
		$iblock_props = CIBlock::GetProperties( $ibid, array(), array());
		$iblock_props_cnt = $iblock_props->SelectedRowsCount();
		
        $res1CPROD = CIBlockElement::GetList(Array(), $ar1CProdFilter, false, false, $ar1CProdSelect);
        $dataArray = array();
        $ext_ids=array();
        while($ar_fields = $res1CPROD->GetNext())	{
			
			$sel_rcnt=0;
								
			if(isset($_REQUEST['SET_PP_CNT']))	{
				$db_props = CIBlockElement::GetProperty($ibid, $ar_fields["ID"], array(), Array("EMPTY"=>'Y'));//"PROPERTY_TYPE"=>"L", 
				if($db_props)	{
					$sel_rcnt=$db_props->SelectedRowsCount();
				
				}
				echo "[[".$sel_rcnt."]]";
				CIBlockElement::SetPropertyValuesEx($ar_fields["ID"], $ibid, array('PROPS_1C_CNT'=>$sel_rcnt) );
			}
			
            $xml_id = "NULL";
            if(isset($ar_fields['XML_ID']))
                $xml_id = $ar_fields['XML_ID'];
            $parent_id = "-1";
            $parent_1c_id = "NULL";
			$sort_order = 0;
			if(isset($ar_fields['SORT']))	{
				$sort_order = $ar_fields['SORT'];
			}
            if(isset($ar_fields['IBLOCK_SECTION_ID']))
            {
                $parent_id = $ar_fields['IBLOCK_SECTION_ID'];
            
                $res1CPRODSECT = CIBlockSection::GetList(Array(), Array("IBLOCK_ID"=>$ibid, 'ID'=>$parent_id), false, array('ID','XML_ID'));
                if ($ar_fields_ps = $res1CPRODSECT->GetNext())	{
                    $parent_1c_id = $ar_fields_ps["XML_ID"];
                }
            }
			
			$sel_rcnt=(isset($ar_fields["PROPERTY_PROPS_1C_CNT_VALUE"])?$ar_fields["PROPERTY_PROPS_1C_CNT_VALUE"]:0);
			$sel_rcnt=$iblock_props_cnt-$sel_rcnt;
			if(isset($ar_fields['PROPERTY_ARTIKUL_VALUE'])) $sel_rcnt--;
			if(isset($ar_fields['PROPERTY_MAIN_PICT_VALUE'])) $sel_rcnt--;//BASE_PICTURE, IS_NEW_ITEM, PROPS_1C_CNT
			//$db_props = CIBlockElement::GetProperty($ibid, $ar_fields["ID"], array(), Array("EMPTY"=>'Y'));//"PROPERTY_TYPE"=>"L", 
			//if($db_props)	{
			//	$sel_rcnt=$db_props->SelectedRowsCount();
			//}
			//while($ar_props = $db_props->Fetch())	{
			   // $FORUM_TOPIC_ID = IntVal($ar_props["VALUE"]);
			//}
            
            
            $base_price = 0;
            if ($base_price_def = GetCatalogProductPrice($ar_fields["ID"], 4)) {
                $base_price = $base_price_def['PRICE'];
            }
            else
                $base_price = 0;
            
            $sale_price = 0;
            $price_def=array();
            if ($price_def = GetCatalogProductPrice($ar_fields["ID"], 2)) {
                $sale_price = $price_def['PRICE'];
            }
            else
                $sale_price = 0;
            
            //{"ID":22762,"QUANTITY":"39","QUANTITY_RESERVED":"0","QUANTITY_TRACE":"Y","QUANTITY_TRACE_ORIG":"D","WEIGHT":"240","WIDTH":"0","LENGTH":"0","HEIGHT":"0","MEASURE":"6","VAT_ID":"1","VAT_INCLUDED":"Y","CAN_BUY_ZERO":"Y","CAN_BUY_ZERO_ORIG":"D","NEGATIVE_AMOUNT_TRACE":"Y","NEGATIVE_AMOUNT_TRACE_ORIG":"D","PRICE_TYPE":"S","RECUR_SCHEME_TYPE":"D","RECUR_SCHEME_LENGTH":"0","TRIAL_PRICE_ID":null,"WITHOUT_ORDER":"N","SELECT_BEST_PRICE":"Y","TMP_ID":null,"PURCHASING_PRICE":null,"PURCHASING_CURRENCY":null,"BARCODE_MULTI":"N","TIMESTAMP_X":"06.05.2016 11:03:33","SUBSCRIBE":"Y","SUBSCRIBE_ORIG":"D","TYPE":"1"
            $pr_quantity = "0";
            if($ar_res = CCatalogProduct::GetByID($ar_fields["ID"]))    {
                $pr_quantity=  $ar_res['QUANTITY']; 
            }
			
			$main_pict="";
			if($ar_fields['DETAIL_PICTURE'])	{
				$main_pict="yes";
			}
            
            $has_ext_id=false;
            if(strlen($xml_id)==36&&in_array($xml_id,$ext_ids)) $has_ext_id=true;
            if(!$has_ext_id)
            $dataArray[] = array( 'ID'=>$ar_fields['ID'], '1C_ID'=>$xml_id, 'NAME'=>$ar_fields['NAME'], 'PARENT_ID'=>$parent_id, 'ARTICUL'=>$ar_fields['PROPERTY_ARTIKUL_VALUE'], 'PRICE'=>$sale_price, 'BPRICE'=>$base_price, 'QUANTITY'=>$pr_quantity, 'PARENT_1CID'=>$parent_1c_id, "SORT_ORDER"=>$sort_order, "MAIN_PICT"=>(isset($ar_fields['PROPERTY_MAIN_PICT_VALUE'])?$ar_fields['PROPERTY_MAIN_PICT_VALUE']:""), "PROP_CNT"=>round($sel_rcnt));
            if(strlen($xml_id)==36) {
                $ext_ids[]=$xml_id;
            }
        }
        echo json_encode($dataArray);
    }
    else    {
        //echo "www";
        $res = CIBlockElement::GetList(Array(), $arFilter, false, array('nTopCount'=>8), $arSelect);
    }
    
        
}
else    {
    
}
?>