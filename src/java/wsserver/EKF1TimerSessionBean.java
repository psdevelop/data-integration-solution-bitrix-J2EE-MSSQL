/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsserver;

import entity.Bx1CProd;
import entity.Bx1CSect;
import entity.BxBsect;
import entity.CbEkfgroupAdd1csectToBx;
import entity.CbEkfgroupDel1csectFromBx;
import entity.CbNewPrFrom1cWprops;
import entity.CbEkfgroupToUpdatedBx1c;
import entity.CbEkfgroupUpd1csectToBx;
import entity.CbEkfroupDelFromBxView;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringEscapeUtils;
import resources.Tools;
import session.Bx1CProdFacade;
import session.Bx1CSectFacade;
import session.BxBsectFacade;
import session.CbLogsFacade;
import session.CbNewPrFrom1cWpropsFacade;
import session.CbEkfgroupToUpdatedBx1cFacade;
import session.CbEkfroupDelFromBxViewFacade;
import session.CbEkfgroupAdd1csectToBxFacade;
import session.CbEkfgroupUpd1csectToBxFacade;
import session.CbEkfgroupDel1csectFromBxFacade;
import session.TablesOperatingStateFacade;
import session.CbSettingsFacade;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.misc.IOUtils;

/**
 *
 * @author Poltarokov SP
 */
@Stateless
public class EKF1TimerSessionBean {
    
    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;
    @EJB
    private BxBsectFacade bxBsectFacade;
    @EJB
    private Bx1CSectFacade bx1CSectFacade;
    @EJB
    private Bx1CProdFacade bx1CProdFacade;
    @EJB
    private CbLogsFacade cbLogsFacade;
    @EJB
    private CbNewPrFrom1cWpropsFacade cbNewPrFrom1cWpropsFacade;
    @EJB
    private CbEkfgroupToUpdatedBx1cFacade cbEkfgroupToUpdatedBx1cFacade;
    @EJB
    private CbEkfroupDelFromBxViewFacade cbEkfroupDelFromBxViewFacade;
    @EJB
    private CbEkfgroupAdd1csectToBxFacade cbEkfgroupAdd1csectToBxFacade;
    @EJB
    private CbEkfgroupUpd1csectToBxFacade cbEkfgroupUpd1csectToBxFacade;
    @EJB
    private CbEkfgroupDel1csectFromBxFacade cbEkfgroupDel1csectFromBxFacade;
    @EJB
    private TablesOperatingStateFacade tablesOperatingStateFacade;
    @EJB
    private CbSettingsFacade cbSettingsFacade;
    
    
    public long frequencyCounter=0;
    
    public static long timerValue = 0;
    public static boolean exchangeInProcess = false; 
    public static String systemURL = "http://yoursite.net/";
    private String mountImgPath = "/mnt/"; //Поменять при смене монтируемого каталога, а лучше монтировать сюда же))))
    public static boolean hasLong1CWait = false;
    public static boolean fullExchangeCircle = true;
    public static int fullExchangeBadCnt = 0;
    public static boolean noChangesOrCorruptMDS = false;
    public static long noChangesOrCorruptMDSCounter = 0;
    public static int timerIdleCoeff=1;
    
    private static final int IMG_WIDTH = 400;
    private static final int IMG_HEIGHT = 400;

    @Schedule(hour = "*", minute = "*", second = "*/10", info = "EKFGroupExchange Every 10 second timer", persistent = false)
    public void myTimer() {
        timerValue++;
        //System.out.println("EKFGroupExchange Timer1 event: " + new Date()+", fc="+frequencyCounter);

        
        
        if(frequencyCounter>10000000)
            frequencyCounter=0;
        if(noChangesOrCorruptMDSCounter>10000000)
            noChangesOrCorruptMDSCounter=0;
        
        
        if(frequencyCounter%(6*timerIdleCoeff)==0)    {
            
            //callT1();
            if(!exchangeInProcess)  {
                exchangeInProcess = true;
                try {
                    
                    
                    
                    try {
                        cbLogsFacade.checkEkfProdLogsCount();
                    }   catch(Exception lge)  {
                        try {
                            cbLogsFacade.insertLog("ERROR", "Error of check logs size ", "Error of check logs size "+lge);
                        } catch(Exception lgex)  {

                        }  
                    }
                    
                    //int icnt = 0;//bx1CProdFacade.insertBx1ProdMultiply();                        
                    //try {
                    //    cbLogsFacade.insertLog("INFO", "Complete persist test bx_1cprod urldata ", "Complete persist test bx_1cprod urldata "+icnt);
                    //} catch(Exception lge)  {     
                    //}
                    //tablesOperatingStateFacade.setEkfGrExchDataDelta();
                    
                    
                    
                    if(tablesOperatingStateFacade.getEkfGrExchDataDelta())    {
                        
                        
                        noChangesOrCorruptMDSCounter=0;
                        noChangesOrCorruptMDS=false;
                        
                        try {
                            cbLogsFacade.insertBoldLog("INFO", "Timer condition worked", "%"+(60*timerIdleCoeff)+"s exchange event entered");
                        } catch(Exception lge)  { }
                        
                        //HttpClient httpclientw = new DefaultHttpClient();
                        
                        boolean checkDelta = true; 
                        if(!fullExchangeCircle) {
                            fullExchangeBadCnt++;
                            try {
                            cbLogsFacade.insertLog("INFO", "Bad full exchange circle", fullExchangeBadCnt+"`s attempt");
                            } catch(Exception lge)  { }
                        }   else
                        {
                            timerIdleCoeff=1;
                            fullExchangeBadCnt = 0;
                        }
                        //Трижды ошибка при занятости данными - пропускаем итерацию на минуту - длительная занятость таблиц
                        if(fullExchangeBadCnt>=3&&(tablesOperatingStateFacade.getEkfGrExchDataCorrupt()&&false))  {
                            checkDelta=false;
                            try {
                                cbLogsFacade.insertLog("INFO", "Too many bad full exchange circle", "Go to next iteration, to check corrupt!");
                            } catch(Exception lge)  { }
                        }
                        //Снятие признака наличия изменений в надежде, что следующие изменения приведут к удачному обмену
                        if(fullExchangeBadCnt>=30&&!(tablesOperatingStateFacade.getEkfGrExchDataCorrupt()&&false))  {
                            try {
                                tablesOperatingStateFacade.setEkfGrExchDataUnDelta();
                            } catch(Exception lge)  { }
                            try {
                                cbLogsFacade.insertLog("CRITICAL_ERRS", "Too many bad full exchange circle (>30)", "Attempt to lock delta flag!");
                            } catch(Exception lge)  { }
                        }
                        //Замедление цикла итераций обмена (раз в полчаса) до появления первой успешной итерации - тогда снова раз в минуту
                        //Подтверждение частой установки признака новых данных, либо несовершенства алгоритма
                        if(fullExchangeBadCnt>=50) {
                            if(timerIdleCoeff<30)   {
                                timerIdleCoeff=30;
                                try {
                                    cbLogsFacade.insertLog("CRITICAL_ERRS", "Too many bad full exchange circle (>100)", "Set low frequency (1/30min) exchange mode!");
                                } catch(Exception lge)  { }    
                            }
                        }
                        fullExchangeCircle = false;
                        hasLong1CWait=false;
                        
                        
                        
                        if(checkDelta) {
                            //try {
                            //cbLogsFacade.insertLog("INFO", "Exchange in process 6", "Exchange in process 6");
                        //} catch(Exception lge)  { }
                            
                            callT2();
                        }   else {
                            exchangeInProcess = false;
                        }
                    }   else    {
                        //try {
                        //    cbLogsFacade.insertLog("INFO", "Exchange in process 4", "Exchange in process 4");
                        //} catch(Exception lge)  { }
                        
                        exchangeInProcess = false;
                        if(!noChangesOrCorruptMDS||(noChangesOrCorruptMDS&&noChangesOrCorruptMDSCounter%30==0)) {
                            try {
                                cbLogsFacade.insertLog("INFO", "getEkfGrExchDataDelta", "None changes in MDS Data OR 1c data corrupt (>="+(noChangesOrCorruptMDSCounter/30*5)+"min)");
                            } catch(Exception lge)  { }
                        }
                        noChangesOrCorruptMDSCounter++;
                        noChangesOrCorruptMDS=true;
                    }
                } catch(Exception lge)  {
                    //checkInterval=6;
                    exchangeInProcess = false;
                    try {
                        cbLogsFacade.insertLog("ERROR", "Error of getEkfGrExchDataDelta or setEkfGrExchDataUnDelta or MDS data load", "Error of getEkfGrExchDataDelta");
                    } catch(Exception lgex)  {

                    }  
                }
            }   else    {
                //try {
                //    cbLogsFacade.insertLog("INFO", "Exchange already in process", "Exchange already in process");
                //} catch(Exception lge)  { }
            }
        }
        
        frequencyCounter++;
        
    }
    
    private void callT1()   {
        Runnable r = new Runnable() {

                public void run() {
                    try {
                        try {
                            cbLogsFacade.insertLog("INFO", "Start load bx_bsect", "Start load bx_bsect url="+systemURL);
                        } catch(Exception lge)  {
                            
                        }
                        String url = systemURL+"get_json_data.php?ENTITY=BSECT";

                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                        // optional default is GET
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(180000);

                        //add request header
                        con.setRequestProperty("User-Agent", "Mozilla-Firefox");

                        int responseCode = con.getResponseCode();
                        System.out.println("\nSending 'GET' request to URL : " + url);
                        System.out.println("Response Code : " + responseCode);

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                        }
                        in.close();

                        try {
                            cbLogsFacade.insertLog("INFO", "Complete load bx_bsect urldata", "Complete load bx_bsect urldata");
                        } catch(Exception lge)  {
                            
                        }
                        JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
                        
                        bxBsectFacade.clearBxBsect();

                        int crcnt=0;
                        int badCnt=0;
                        JsonArray jarray = jsonReader.readArray();
                        //JsonParser parser = Json.createParser(in);
                        boolean hasCrashes = false;
                        for(int i=0;i<jarray.size();i++)    {
                            JsonObject jobject = jarray.getJsonObject(i);
                            BxBsect bsectObj = new BxBsect();
                            bsectObj.setId(-1);
                            try {
                                bsectObj.setBxId(Tools.parseInt(jobject.getString("ID", "-1"), -1));
                            } catch(Exception e)    {
                                bsectObj.setBxId(-1);
                            }

                            try {
                                String f1cId = jobject.getString("1C_ID", "");
                                if(f1cId.length()==36)
                                    bsectObj.setF1cId(f1cId);
                                else
                                    bsectObj.setF1cId("NULL");
                            } catch(Exception e)    {
                                bsectObj.setF1cId("NULL");
                            }
                            try {
                                bsectObj.setParentBxId(Tools.parseInt(jobject.getString("PARENT_ID", "-1"), -1));
                            } catch(Exception e)    {
                                bsectObj.setParentBxId(-1);
                            }
                            try {
                                bsectObj.setName(jobject.getString("NAME", "NULL"));
                            } catch(Exception e)    {
                                bsectObj.setName("NULL");
                            }
                            int try_cnt=0;
                            boolean notSucc = true;
                            String err="";
                            while(try_cnt<10&&notSucc) {
                                try {
                                    bxBsectFacade.create(bsectObj);
                                    crcnt++;
                                    notSucc=false;
                                } catch(Exception e)    {
                                    notSucc=true;
                                    badCnt++;
                                    try_cnt++;
                                    err+="[["+Tools.parseInt(jobject.getString("ID", "-1"), -1)+"]]<<==!!||||||!!==>>Error of bxBsectFacade.create "+e;
                                }
                            }
                            
                            try {
                                if(try_cnt>0)
                                    cbLogsFacade.insertLog("ERROR", "Error of bxBSectFacade", err);
                            } catch(Exception lge)  {
                            
                            }
                            hasCrashes = hasCrashes | notSucc;
                        }
                        
                        try {
                            cbLogsFacade.insertLog("INFO", "Complete load bx_bsect", "Complete load bx_bsect "+", all="+jarray.size()+",succ="+crcnt+",errcnt="+badCnt);
                        } catch(Exception lge)  {
                            
                        }
                        
                        BxBsect bsectObjCompl = new BxBsect();
                        bsectObjCompl.setId(-1);
                        bsectObjCompl.setBxId(jarray.size());
                        if(hasCrashes)
                            bsectObjCompl.setF1cId("00000000-0000-0000-0000-00nocomplete");
                        else
                            bsectObjCompl.setF1cId("00000000-0000-0000-0000-0000complete");
                        //bsectObjCompl.setF1cId("00000000-0000-0000-0000-0bxinprocess");
                        //bsectObjCompl.setF1cId("00000000-0000-0000-0000-01сinprocess");
                        //bsectObjCompl.setF1cId("00000000-0000-0000-0000-bxancomplete");
                        //bsectObjCompl.setF1cId("00000000-0000-0000-0000-1cancomplete");
                        bsectObjCompl.setParentBxId(badCnt);
                        bsectObjCompl.setName("jasz="+jarray.size()+",crcnt="+crcnt);
                        int try_cnt22=0;
                        boolean notSucc22 = true;
                        while(try_cnt22<10&&notSucc22) {
                            try {
                                //bxBsectFacade.create(bsectObjCompl);
                                       notSucc22=false;
                                } catch(Exception e) {
                                    notSucc22=true;
                                    //badCnt22++;
                                    try_cnt22++;
                                }
                            }

                    } catch(Exception e) {
                        System.out.println("<<==!!||||||!!==>>Error of get-parse json "+e);
                        try {
                            cbLogsFacade.insertLog("ERROR", "Error of get-parse json bx_bsect", "<<==!!||||||!!==>>Error of get-parse json "+e);
                        } catch(Exception lge)  {
                            
                        }
                    } finally   {
                        callT2();
                    }
                }

            };
            
            Thread t = new Thread(r);
            t.start();
    }
    
    public long maxPostFilePartSize = 85000000;
    public long maxPostedFileSize = 8000000;
    public long maxPostedDocFileSize = 25000000;
    
    private boolean postMultipartPrAdd(List<CbNewPrFrom1cWprops > npwps)    {
        String charset = "UTF-8";
        String requestURL = systemURL+"manipulate_data.php";
        boolean reply=true;
        String sreply="";
        
        if(npwps.isEmpty())  
            return reply;
        
        String logStr = "New prod cnt="+npwps_.size()+". ";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            int index=0;
            multipart.addFormField("OTYPE","ADD");
            multipart.addFormField("ENTITY","1CPROD");
            int ocnt=npwps.size(); 
            
            //String logStr = "";
            long allFileSize = 0;
            boolean uploadFiles=true; 
            multipart.addFormField("OCNT","50");
            for(CbNewPrFrom1cWprops npwp: npwps)    {
                
                long productFilesSize=0;
                boolean error_file_operation=false;
                try {
                    if(npwp.getFp0().length()>0&&uploadFiles) {
                        File f = new File(mountImgPath+npwp.getFp0().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getFp1().length()>0&&uploadFiles) { 
                        File f = new File(mountImgPath+npwp.getFp1().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getFp2().length()>0&&uploadFiles) { 
                        File f = new File(mountImgPath+npwp.getFp2().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getFp3().length()>0&&uploadFiles) { 
                        File f = new File(mountImgPath+npwp.getFp3().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getFp4().length()>0&&uploadFiles) { 
                        File f = new File(mountImgPath+npwp.getFp4().replace("\\", "/"));
                        if(f.length()<=2000000&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                
                }   catch(Exception fle) {
                    productFilesSize=0;
                    error_file_operation=true;
                    logStr+=("NAME "+npwp.getName()+" - Error prev file operation!!! "+fle);
                }
                
                if((allFileSize+productFilesSize)>maxPostFilePartSize)  {
                    logStr+=("NAME "+npwp.getName()+" - too big product files sizes summ!!!");
                    //continue;
                }
                else    {
                    multipart.addFormField("PIDPREV1"+index, npwp.getId());
                    multipart.addFormField("PIDPREV2"+index, npwp.getId());
                    multipart.addFormField("PID"+index, npwp.getId());
                    multipart.addFormField("PART"+index, npwp.getVendorCode());
                    multipart.addFormField("PNAME"+index, npwp.getName());

                    logStr+=("["+index+"] NAME "+npwp.getId()+npwp.getName()+","+npwp.getBasePrice()+","+npwp.getIshopPrice()+","+npwp.getQuant()+
                            ","+npwp.getBxGroupExternalCode()+","+npwp.getVendorCode());//+",[["+npwp.getJsonData()+"]]"

                    multipart.addFormField("PSHNAME"+index, npwp.getShortName());
                    multipart.addFormField("PPROPS"+index, npwp.getJsonData());
                    multipart.addFormField("PGRPID"+index, npwp.getProductGroupId());
                    multipart.addFormField("PBPRICE"+index, ""+npwp.getBasePrice());
                    multipart.addFormField("PISPRICE"+index, ""+npwp.getIshopPrice());
                    multipart.addFormField("PQUANT"+index, ""+npwp.getQuant());
                    multipart.addFormField("PGRXMLID"+index, npwp.getBxGroupExternalCode());

                    if(!error_file_operation)    {
                        try {
                            if(npwp.getFp0().length()>0&&uploadFiles) {
                                File f = new File(mountImgPath+npwp.getFp0().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("DETAIL_PICTURE"+index, (f) );
                                    logStr+=",fp0"+npwp.getFp0();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp0 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp1().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp1().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_01", (f) );
                                    logStr+=",fp1="+npwp.getFp1();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp1 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp2().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp2().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_02", (f) );
                                    logStr+=",fp2="+npwp.getFp2();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp2 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp3().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp3().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_03", (f) );
                                    logStr+=",fp3="+npwp.getFp3();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp3 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp4().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp4().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_04", (f) );
                                    logStr+=",fp4="+npwp.getFp4();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp4 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                        }   catch(Exception fle) {
                            //error_file_operation=true;
                            logStr+=("NAME "+npwp.getName()+" - Error file operation!!! "+fle);
                        }
                    }
                    
                    index++;
                }
                if(allFileSize>maxPostFilePartSize) {
                    logStr+=("End of request construction - too big prev products files sizes summ!!!");
                    break;
                }
            }
           
             
            try {
                if(logStr.length()>0)
                cbLogsFacade.insertLog("INFO", "NEW PROD TO SEND ", "<p style=\"font-size:10px !important;\">"+index+"cnt,"+logStr+"</p>"+", allFilesSizes(bytes)="+allFileSize);
            } catch(Exception lgen)  {
            }
 
            //multipart.addFormField("OCNT",""+index);
            multipart.addFormField("OCNT","50");//+50);
            List<String> response = multipart.finish(); 
            
            for (String line : response) {
                sreply=sreply+line;
            }
            
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(sreply));
                JsonObject jobject = jsonReader.readObject();
                try {
                    cbLogsFacade.insertLog("INFO", "SUCCESS OF PARSE SERVER REPLY", "SUCCESS OF PARSE SERVER REPLY="+jobject.toString());
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_info", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_INFO", "SERVER REPLY DETAIL", jobject.getString("critical_info", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_errs", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_ERRS", "SERVER REPLY DETAIL", jobject.getString("critical_errs", ""));
                } catch(Exception lge)  {

                }
            }   catch(Exception pe) {
                try {
                    cbLogsFacade.insertLog("ERROR", "ERROR OF PARSE SERVER REPLY", "ERROR OF PARSE SERVER REPLY"+sreply);
                } catch(Exception lge)  {

                }
            }
            
        } catch (Exception ex) {
            try {
                cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartPrAdd", logStr+" ERROR of postMultipartPrAdd"+ex);
            } catch(Exception lge)  {

            }
        }
        
        return reply;
    }
    
    List<CbNewPrFrom1cWprops> npwps_;
    
    private void sendCompareData()  {
        Runnable r4 = new Runnable() {
                public void run() { 
                    try {
                        if(npwps_.size()==0)    {
                            try {
                                    cbLogsFacade.insertLog("INFO", "Data params", "New prod cnt="+npwps_.size());
                                } catch(Exception lge)  {

                                }
                        }
                        if(postMultipartPrAdd(npwps_))   {
                            sendCompareDataUpd();
                        }   else    {
                            exchangeInProcess = false;
                        }
                    }   catch(Exception e)    {
                        exchangeInProcess = false;
                        try {
                                cbLogsFacade.insertLog("ERROR", "Error of sendCompareData procedure", "Error of sendCompareData procedure"+e);
                            } catch(Exception lge)  {

                            }
                    }
            }
                
        };
                
        Thread t = new Thread(r4);
        t.start();
    }
    
    ArrayList<String> updProdsIds = new ArrayList<String>();
    
    private boolean postMultipartPrUpd(List<CbEkfgroupToUpdatedBx1c > upwps)    {
        String charset = "UTF-8";
        String requestURL = systemURL+"manipulate_data.php";
        boolean reply=true;
        String sreply="";
        
        if(upwps.isEmpty())  
            return reply;
        
        String logStr="Upd prod cnt="+upwps.size()+". ";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            int index=0;
            multipart.addFormField("OTYPE","UPDATE");
            multipart.addFormField("ENTITY","1CPROD");
            int ocnt=upwps.size(); 
            multipart.addFormField("OCNT",""+ocnt);
            //String logStr="";
            long allFileSize = 0;
            boolean uploadFiles=true;
            for(CbEkfgroupToUpdatedBx1c npwp: upwps)    { 
                try {
                    
                long productFilesSize=0;
                boolean error_file_operation=false;
                try {
                    if(!npwp.getF1cMainPict().equals(npwp.getBxMainPict())&&npwp.getF1cMainPict().length()>10&&uploadFiles) {
                        File f = new File(mountImgPath+npwp.getF1cMainPict().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    /*if(npwp.getF1cAddPict1().length()>0&&uploadFiles&&false) { 
                        File f = new File(mountImgPath+npwp.getF1cAddPict1().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getF1cAddPict1().length()>0&&uploadFiles&&false) { 
                        File f = new File(mountImgPath+npwp.getF1cAddPict1().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getF1cAddPict1().length()>0&&uploadFiles&&false) { 
                        File f = new File(mountImgPath+npwp.getF1cAddPict1().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }
                    if(npwp.getF1cAddPict1().length()>0&&uploadFiles&&false) { 
                        File f = new File(mountImgPath+npwp.getF1cAddPict1().replace("\\", "/"));
                        if(f.length()<=2000000&&((allFileSize+productFilesSize+f.length())<maxPostFilePartSize))   {
                            productFilesSize+=f.length();
                        }
                    }*/
                
                }   catch(Exception fle) {
                    productFilesSize=0;
                    error_file_operation=true;
                    logStr+=("NAME "+npwp.getName()+" - Error prev file operation!!! "+fle);
                    if(!npwp.getF1cMainPict().equals(npwp.getBxMainPict())&&npwp.getBxMainPict().length()<=5) {
                        multipart.addFormField("SETMAIN_PICT_ERR"+index, "1");
                        multipart.addFormField("MAIN_PICT"+index, StringEscapeUtils.escapeJava((npwp.getBxMainPict()==null||(npwp.getBxMainPict().length()>10)?"a":npwp.getBxMainPict()+"a")));
                    }
                }
                
                if((allFileSize+productFilesSize)>maxPostFilePartSize)  {
                    logStr+=("NAME "+npwp.getName()+" - too big product files sizes summ!!!");
                    //continue;
                }
                else    {
                    if(!error_file_operation)    {
                        try {
                            if(!npwp.getF1cMainPict().equals(npwp.getBxMainPict())&&npwp.getF1cMainPict().length()>10&&uploadFiles) {
                                File f = new File(mountImgPath+npwp.getF1cMainPict().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("DETAIL_PICTURE"+index, (f) );
                                    multipart.addFormField("SETDETAIL_PICTURE"+index, "1");
                                    multipart.addFormField("SETMAIN_PICT"+index, "1");
                                    multipart.addFormField("MAIN_PICT"+index, StringEscapeUtils.escapeJava(npwp.getF1cMainPict()));
                                    logStr+=",fp0"+npwp.getF1cMainPict();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp0 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            /*if(npwp.getFp1().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp1().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_01", (f) );
                                    logStr+=",fp1="+npwp.getFp1();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp1 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp2().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp2().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_02", (f) );
                                    logStr+=",fp2="+npwp.getFp2();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp2 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp3().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp3().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_03", (f) );
                                    logStr+=",fp3="+npwp.getFp3();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp3 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }
                            if(npwp.getFp4().length()>0&&uploadFiles) { 
                                File f = new File(mountImgPath+npwp.getFp4().replace("\\", "/"));
                                if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                    multipart.addFilePart("PHOTO"+index+"_04", (f) );
                                    logStr+=",fp4="+npwp.getFp4();
                                    allFileSize+=f.length();
                                }
                                else    {
                                    logStr+=",fp4 "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                }
                            }*/
                        }   catch(Exception fle) {
                            error_file_operation=true;
                            logStr+=("NAME "+npwp.getName()+" - Error file operation!!! "+fle);
                            if(!npwp.getF1cMainPict().equals(npwp.getBxMainPict())&&npwp.getBxMainPict().length()<=5) {
                                multipart.addFormField("SETMAIN_PICT_ERR"+index, "1");
                                multipart.addFormField("MAIN_PICT"+index, StringEscapeUtils.escapeJava((npwp.getBxMainPict()==null||(npwp.getBxMainPict().length()>10)?"a":npwp.getBxMainPict()+"a")));
                            }
                        }
                    }
                }
   
                updProdsIds.add(npwp.getF1cId());
                multipart.addFormField("PIDPREV1"+index, ""+npwp.getId());
                multipart.addFormField("PIDPREV2"+index, ""+npwp.getId());
                
                if(!npwp.getF1cMainPict().equals(npwp.getBxMainPict())&&(npwp.getF1cMainPict().length()<=10||(npwp.getBxMainPict().length()>=5&&npwp.getBxMainPict().length()<=15))) {
                    multipart.addFormField("SETMAIN_PICT_DIR"+index, "1");
                    multipart.addFormField("MAIN_PICT"+index, StringEscapeUtils.escapeJava(npwp.getF1cMainPict()));
                }
                multipart.addFormField("PID"+index, ""+npwp.getId());
                multipart.addFormField("PXMLID"+index, npwp.getF1cId());
                multipart.addFormField("PART"+index, npwp.getArtikul());
                multipart.addFormField("PNAME"+index, npwp.getName());
                
                if(npwp.getToUpdate()==1||(Math.abs(npwp.getBxPropCnt()-npwp.getF1cPropCnt())>1))
                    multipart.addFormField("PPROPS"+index, npwp.getJsonData());
                
                logStr+="["+index+"] NAME "+npwp.getName()+","+npwp.getBasePrice()+","+npwp.getIshopPrice()+","+npwp.getQuant()+
                        ","+npwp.getBxGroupExternalCode()+npwp.getArtikul()+"["+npwp.getToUpdate()+"["+npwp.getJsonData()+"]]bxpcnt="+
                        npwp.getBxPropCnt()+",1fpcnt="+npwp.getF1cPropCnt();//

                if(npwp.getBasePrice()!=npwp.getBbsprice())
                    multipart.addFormField("SETPBPRICE"+index, "1");
                else
                    multipart.addFormField("SETPBPRICE"+index, "0");
                if(npwp.getIshopPrice()!=npwp.getBisprice())
                    multipart.addFormField("SETPISPRICE"+index, "1");
                else
                    multipart.addFormField("SETPISPRICE"+index, "0");
                if(npwp.getQuant()!=npwp.getBamount())
                    multipart.addFormField("SETPQUANT"+index, "1");
                else
                    multipart.addFormField("SETPQUANT"+index, "0");
                multipart.addFormField("PBPRICE"+index, ""+npwp.getBasePrice());
                multipart.addFormField("PISPRICE"+index, ""+npwp.getIshopPrice());
                multipart.addFormField("PQUANT"+index, ""+npwp.getQuant());
                if(!npwp.getBxGroupExternalCode().equals(npwp.getParent1cId()))
                    multipart.addFormField("SETPGRXMLID"+index, "1");
                else
                    multipart.addFormField("SETPGRXMLID"+index, "0");
                multipart.addFormField("PGRXMLID"+index, npwp.getBxGroupExternalCode());
                
                if(!npwp.getBxName().equals(npwp.getName())&&npwp.getName().length()>0)
                    multipart.addFormField("SETPNAME"+index, "1");
                else
                    multipart.addFormField("SETPNAME"+index, "0");
                
                if(npwp.getBxSortOrder()!=npwp.getF1cSortOrder())
                    multipart.addFormField("SETSORT_ORDER"+index, "1");
                else
                    multipart.addFormField("SETSORT_ORDER"+index, "0");
                multipart.addFormField("SORT_ORDER"+index, ""+npwp.getF1cSortOrder());
                
                } catch (Exception ex) {
                    try {
                        cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartPrUpd set data", "ERROR of postMultipartPrUpd set data "+ex);
                        return false;
                    } catch(Exception lge)  {

                    }
                }
                
                index++;
            }
            
            try {
                if(logStr.length()>0)    
                cbLogsFacade.insertLog("INFO", "UPD PROD TO SEND ", "<p style=\"font-size:10px !important;\">"+logStr+"</p>");
            } catch(Exception lgen)  {
            }
 
            List<String> response = multipart.finish(); 
            
            for (String line : response) {
                sreply=sreply+line;
            }
            
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(sreply));
                JsonObject jobject = jsonReader.readObject();
                try {
                    cbLogsFacade.insertLog("INFO", "SUCCESS OF PARSE SERVER REPLY", "SUCCESS OF PARSE SERVER REPLY="+jobject.toString());
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_info", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_INFO", "SERVER REPLY DETAIL", jobject.getString("critical_info", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_errs", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_ERRS", "SERVER REPLY DETAIL", jobject.getString("critical_errs", ""));
                } catch(Exception lge)  {

                }
            }   catch(Exception pe) {
                try {
                    cbLogsFacade.insertLog("ERROR", "ERROR OF PARSE SERVER REPLY", "ERROR OF PARSE SERVER REPLY"+sreply);
                } catch(Exception lge)  {

                }
            }
            
        } catch (Exception ex) {
            try {
                cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartPrUpd", logStr+" ERROR of postMultipartPrUpd "+ex.getMessage());
            } catch(Exception lge)  {

            }
        }
        
        return reply;
    }
    
    List<CbEkfgroupToUpdatedBx1c> upwps_;
    
    private void sendCompareDataUpd()  {
        Runnable r4 = new Runnable() {
                public void run() {
                    try {
                        
                        if(upwps_.size()==0)    {
                        try {
                                cbLogsFacade.insertLog("INFO", "Data params", "Upd prod cnt="+upwps_.size());
                            } catch(Exception lge)  {

                            }
                        }
                        
                        updProdsIds = new ArrayList<String>();
                        
                        if(postMultipartPrUpd(upwps_))  {
                            StringBuilder in_cond = new StringBuilder();
                                in_cond.append("(");
                            try {
                                
                                for(int i=0;i<updProdsIds.size();i++)   {
                                    if(i>0) in_cond.append(",");
                                    in_cond.append("'").append(updProdsIds.get(i).replace("'", "''")).append("'");
                                }
                                in_cond.append(")");
                                if(cbEkfgroupToUpdatedBx1cFacade.updProdMultiply(updProdsIds))  {
                                    //try {
                                    //    cbLogsFacade.insertLog("INFO", "Success products updated_at SET", 
                                    //            "Success products updated_at SET, pcount="+updProdsIds.size());
                                    //} catch(Exception lge)  {

                                    //}
                                    sendCompareDataDel();
                                }   else    {
                                    exchangeInProcess = false;
                                    try {
                                        cbLogsFacade.insertLog("ERROR", "Unsuccess products updated_at SET", 
                                                "Unsuccess products updated_at SET, pcount="+updProdsIds.size());
                                    } catch(Exception lge)  {

                                    }
                                }
                            } catch(Exception lgee)  {
                                exchangeInProcess = false;
                                    try {
                                        cbLogsFacade.insertLog("ERROR", "!!!!!!Unsuccess products updated_at SET", 
                                                "Unsuccess products updated_at SET, pcount="+updProdsIds.size()+", "+
                                                        in_cond.toString());
                                    } catch(Exception lge)  {

                                    }
                            }
                            
                        }   else    {
                            exchangeInProcess = false;
                        }
                        
                    }   catch(Exception e)    {
                        exchangeInProcess = false;
                        try {
                                cbLogsFacade.insertLog("ERROR", "Error of sendCompareDataUpd procedure", "Error of sendCompareDataUpd procedure"+e);
                            } catch(Exception lge)  {

                            }
                    }
            }
                
        };
                
        Thread t = new Thread(r4);
        t.start();
    }
    
    private boolean postMultipartPrDel(List<CbEkfroupDelFromBxView > dpwps)    {
        String charset = "UTF-8";
        String requestURL = systemURL+"manipulate_data.php";
        boolean reply=true;
        String sreply="";
        
        if(dpwps.isEmpty())  
            return reply;
        
        String logStr="Del prod cnt="+dpwps.size()+". ";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            int index=0;
            multipart.addFormField("OTYPE","DELETE");
            multipart.addFormField("ENTITY","1CPROD");
            int ocnt=dpwps.size(); 
            multipart.addFormField("OCNT",""+ocnt);
            //String logStr="";
            for(CbEkfroupDelFromBxView npwp: dpwps)    { 
                multipart.addFormField("PID"+index, npwp.getId());
                
                logStr+="["+index+"] PID "+npwp.getId();
                index++;
            }
            try {
                if(logStr.length()>0)
                cbLogsFacade.insertLog("INFO", "DEL PROD TO SEND ", logStr);
            } catch(Exception lgen)  {
            }
 
            List<String> response = multipart.finish(); 
            
            for (String line : response) {
                sreply=sreply+line;
            }
            
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(sreply));
                JsonObject jobject = jsonReader.readObject();
                try {
                    cbLogsFacade.insertLog("INFO", "SUCCESS OF PARSE SERVER REPLY", "SUCCESS OF PARSE SERVER REPLY="+jobject.toString());
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_info", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_INFO", "SERVER REPLY DETAIL", jobject.getString("critical_info", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_errs", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_ERRS", "SERVER REPLY DETAIL", jobject.getString("critical_errs", ""));
                } catch(Exception lge)  {

                }
            }   catch(Exception pe) {
                try {
                    cbLogsFacade.insertLog("ERROR", "ERROR OF PARSE SERVER REPLY", "ERROR OF PARSE SERVER REPLY"+sreply);
                } catch(Exception lge)  {

                }
            }
            
        } catch (Exception ex) {
            try {
                cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartPrDel", logStr+" ERROR of postMultipartPrDel"+ex);
            } catch(Exception lge)  {

            }
        }
        
        return reply;
    }
    
    List<CbEkfroupDelFromBxView> dpwps_;
    
    private void sendCompareDataDel()  {
        Runnable r4 = new Runnable() {
                public void run() {
                    try {
                        
                        if(dpwps_.size()==0)    {
                        try {
                                cbLogsFacade.insertLog("INFO", "Data params", "Del prod cnt="+dpwps_.size());
                            } catch(Exception lge)  {

                            }
                        }

                        if(postMultipartPrDel(dpwps_))   {
                            //
                        }
                        exchangeInProcess = false;
                        fullExchangeCircle = true;
                        
                        if(npwps_.size()==0&&upwps_.size()==0&&dpwps_.size()==0&&nswps_.size()==0&&uswps_.size()==0&&dswps_.size()==0)  {
                            
                            try {
                                tablesOperatingStateFacade.setEkfGrExchDataUnDelta();
                            } catch(Exception lgex)  {

                            }
                        }
                    }   catch(Exception e)    {
                        exchangeInProcess = false;
                        try {
                                cbLogsFacade.insertLog("ERROR", "Error of sendCompareDataDel procedure", "Error of sendCompareDataDel procedure"+e);
                            } catch(Exception lge)  {

                            }
                    }
            }
                
        };
                
        Thread t = new Thread(r4);
        t.start();
    }
    
    private boolean postMultipartSectAdd(List<CbEkfgroupAdd1csectToBx > nswps)    {
        String charset = "UTF-8";
        String requestURL = systemURL+"manipulate_data.php";
        boolean reply=true;
        String sreply="";
 
        if(nswps.isEmpty())  
            return reply;
        
        String logStr="New sect cnt="+nswps.size()+". ";
        
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            int index=0;
            multipart.addFormField("OTYPE","ADD");
            multipart.addFormField("ENTITY","1CSECT");
            int ocnt=nswps.size(); 
            multipart.addFormField("OCNT",""+ocnt);
            //String logStr="";
            for(CbEkfgroupAdd1csectToBx npwp: nswps)    { 
                multipart.addFormField("SID"+index, npwp.getId());
                multipart.addFormField("SNAME"+index, npwp.getName());  
                multipart.addFormField("SGRXMLID"+index, npwp.getParentId());

                logStr+="["+index+"] NAME "+npwp.getName()+","+npwp.getId()+","+npwp.getParentId();
                index++;
            }
            
            try {
                if(logStr.length()>0)
                cbLogsFacade.insertLog("INFO", "NEW SECT TO SEND ", logStr);
            } catch(Exception lgen)  {
            }
 
            List<String> response = multipart.finish(); 
            
            for (String line : response) {
                sreply=sreply+line;
            }
            
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(sreply));
                JsonObject jobject = jsonReader.readObject();
                try {
                    cbLogsFacade.insertLog("INFO", "SUCCESS OF PARSE SERVER REPLY", "SUCCESS OF PARSE SERVER REPLY="+jobject.toString());
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_info", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_INFO", "SERVER REPLY DETAIL", jobject.getString("critical_info", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_errs", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_ERRS", "SERVER REPLY DETAIL", jobject.getString("critical_errs", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(Tools.parseInt(jobject.getString("errcnt", "0"),1)>0)    {
                        reply=false;
                        try {
                            cbLogsFacade.insertLog("ERROR", "EXCHANGE STOPPED", "UNSUCCESS EXECUTE SECT ADD SCRIPT");
                        } catch(Exception lge)  {

                        }
                    }
                } catch(Exception lge)  {
                    reply=false;
                }
            }   catch(Exception pe) {
                try {
                    cbLogsFacade.insertLog("ERROR", "ERROR OF PARSE SERVER REPLY", "ERROR OF PARSE SERVER REPLY"+sreply);
                } catch(Exception lge)  {

                }
            }
            
        } catch (Exception ex) {
            try {
                cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartSectAdd", logStr+" ERROR of postMultipartSectAdd"+ex);
            } catch(Exception lge)  {

            }
        }
        
        return reply;
    }
    
    List<CbEkfgroupAdd1csectToBx> nswps_;
    
    private void sendCompareSectDataAdd()  {
        Runnable r4 = new Runnable() {
                public void run() {
                    try {

                        if(nswps_.size()==0)    {
                        try {
                                cbLogsFacade.insertLog("INFO", "Data params", "New sect cnt="+nswps_.size());
                            } catch(Exception lge)  {

                            }
                        }
                        
                        if(postMultipartSectAdd(nswps_))   {
                            //try {
                            //    cbLogsFacade.insertLog("INFO", "Break sections add!!!", "Break add!!!");
                            //} catch(Exception lge)  {

                            //}
                            sendCompareSectDataUpd();
                        }   else    {
                            exchangeInProcess = false;
                        }
                    }   catch(Exception e)    {
                        exchangeInProcess = false;
                        try {
                                cbLogsFacade.insertLog("ERROR", "Error of sendCompareSectData procedure", "Error of sendCompareSectData procedure"+e);
                            } catch(Exception lge)  {

                            }
                    }
            }
                
        };
                
        Thread t = new Thread(r4);
        t.start();
    }
    
    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	g.dispose();

	return resizedImage;
    }
    
    private void testPost() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://localhost:8080" +
                    "/servlets-examples/servlet/RequestInfoExample");

            FileBody bin = new FileBody(new File(""));
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("bin", bin)
                    .addPart("comment", comment)
                    .build();


            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            try {
                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        System.out.println("Response content length: " + resEntity.getContentLength());
                    }
                    try {
                        EntityUtils.consume(resEntity);
                    } catch(Exception ee)   {

                    }
                } finally {
                    try {
                        response.close();
                    } catch(Exception ee)   {

                    }
                }
            } catch(Exception ee)   {
                    
            }
            
        } finally {
            try {
                httpclient.close();
            } catch(Exception ee)   {
                    
            }
        }
    }
    
    private boolean postMultipartSectUpd(List<CbEkfgroupUpd1csectToBx > uswps)    {
        String charset = "UTF-8";
        String requestURL = systemURL+"manipulate_data.php";
        boolean reply=true;
        String sreply="";
 
        if(uswps.isEmpty())  
            return reply;
        long allFileSize=0;
        
        String logStr="Upd sect cnt="+uswps.size()+". ";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            HttpPost httppost = new HttpPost(requestURL);
            MultipartEntityBuilder reqBuilder = MultipartEntityBuilder.create();
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            httppost.addHeader("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            int index=0;
            multipart.addFormField("OTYPE","UPDATE");
            reqBuilder.addPart("OTYPE", new StringBody("UPDATE", ContentType.TEXT_PLAIN));
            multipart.addFormField("ENTITY","1CSECT");
            reqBuilder.addPart("ENTITY", new StringBody("1CSECT", ContentType.TEXT_PLAIN));
            int ocnt=uswps.size(); 
            multipart.addFormField("OCNT",""+ocnt);
            reqBuilder.addPart("OCNT", new StringBody(""+ocnt, ContentType.TEXT_PLAIN));
            //String logStr=""; 
            for(CbEkfgroupUpd1csectToBx npwp: uswps)    { 
                multipart.addFormField("SID"+index, npwp.getSid());
                reqBuilder.addPart("SID"+index, new StringBody(npwp.getSid(), ContentType.TEXT_PLAIN));
                //if(!npwp.getSid().equals(npwp.getCbBxgroupId()))
                //    multipart.addFormField("SETSID"+index, "1");
                //else
                //    multipart.addFormField("SETSID"+index, "0");
                //multipart.addFormField("NEWSID"+index, npwp.getCbBxgroupId());
                
                logStr+="["+index+"] NAME "+npwp.getBxname()+","+npwp.getF1cname()+","+npwp.getBxparentId()+","+npwp.getF1cparentId();
               
                if(!npwp.getBxname().equals(npwp.getF1cname())) {
                    multipart.addFormField("SETSNAME"+index, "1");
                    reqBuilder.addPart("SETSNAME"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSNAME"+index, "0");
                    reqBuilder.addPart("SETSNAME"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                if(!npwp.getBxparentId().equals(npwp.getF1cparentId())) {
                    multipart.addFormField("SETSGRXMLID"+index, "1");
                    reqBuilder.addPart("SETSGRXMLID"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSGRXMLID"+index, "0");
                    reqBuilder.addPart("SETSGRXMLID"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("SNAME"+index, npwp.getF1cname());
                reqBuilder.addPart("SNAME"+index, new StringBody(npwp.getF1cname(), ContentType.TEXT_PLAIN));
                multipart.addFormField("SGRXMLID"+index, npwp.getF1cparentId());
                reqBuilder.addPart("SGRXMLID"+index, new StringBody(npwp.getF1cparentId(), ContentType.TEXT_PLAIN));
                
                boolean error_file_operation=false, uploadFiles=true;  
                long sectFilesSize=0;
                boolean tooLargeDsc=false, tooLargeGb=false, tooLargeDoc=false;
                
                ArrayList<String> dscFiles = new ArrayList<String>();
                
                try {
                    if(!npwp.getBxDescriptsJson().equals(npwp.getF1cDescriptsJson()))   {
                        //logStr+="["+npwp.getF1cDescriptsJson()+"]";
                        JsonReader jsonReader = Json.createReader(new StringReader(npwp.getF1cDescriptsJson()));
                        JsonObject jo = jsonReader.readObject();
                        
                        int dcount=0;
                        logStr+="cnt="+jo.getString("dc","0");
                        for(int i=0;i<Tools.parseInt(jo.getString("dc","0"),0);i++)    {
                            logStr+=("["+i+"]");
                            try {
                                if(jo.getString("fp"+i).length()>0&&uploadFiles) {
                                    File f = new File(mountImgPath+jo.getString("fp"+i).replace("\\", "/"));
                                    if(f.length()<=maxPostedFileSize&&((allFileSize+sectFilesSize+f.length())<maxPostFilePartSize))   {
                                        dscFiles.add(jo.getString("fp"+i).replace("\\", "/"));
                                        multipart.addFormField("FDSC"+index+"DSC"+dcount, jo.getString("dsc"+i));
                                        multipart.addFormField("FDSC"+index+"FN"+dcount, jo.getString("fn"+i));
                                        reqBuilder.addPart("FDSC"+index+"DSC"+dcount, new StringBody(jo.getString("dsc"+i), ContentType.TEXT_PLAIN));
                                        reqBuilder.addPart("FDSC"+index+"FN"+dcount, new StringBody(jo.getString("fn"+i), ContentType.TEXT_PLAIN));
                                        sectFilesSize+=f.length();
                                        dcount++;
                                    }
                                    else { 
                                        tooLargeDsc=true;
                                        logStr+="Too large "+jo.getString("fp"+i); 
                                    }
                                }
                                
                            }   catch(Exception fle) {
                                //sectFilesSize=0;
                                error_file_operation=true;
                                logStr+=("NAME "+npwp.getF1cname()+" - Error dsc file operation!!! "+fle);
                            }
                            
                        }
                        
                        if(dcount>0) { 
                            multipart.addFormField("FDSCDC"+index, ""+dcount); 
                            reqBuilder.addPart("FDSCDC"+index, new StringBody(""+dcount, ContentType.TEXT_PLAIN));
                        }

                    }
                }   catch(Exception fle) {
                    logStr+=(npwp.getF1cname()+" Error F1cDescriptsJson parse operation!!! "+fle+"[["+npwp.getF1cDescriptsJson()+"]]");
                }
                
                ArrayList<String> gbFiles = new ArrayList<String>();
                
                try {
                    if(!npwp.getBxGabaritsJson().equals(npwp.getF1cGabaritsJson()))   {
                        //logStr+="["+npwp.getF1cGabaritsJson()+"]";
                        JsonReader jsonReader = Json.createReader(new StringReader(npwp.getF1cGabaritsJson()));
                        JsonObject jo = jsonReader.readObject();
                        
                        int dcount=0;
                        logStr+="cnt="+jo.getString("dc","0");
                        for(int i=0;i<Tools.parseInt(jo.getString("dc","0"),0);i++)    {
                            logStr+=("["+i+"]");
                            try {
                                if(jo.getString("fp"+i).length()>0&&uploadFiles) {
                                    File f = new File(mountImgPath+jo.getString("fp"+i).replace("\\", "/"));
                                    if(f.length()<=maxPostedFileSize&&((allFileSize+sectFilesSize+f.length())<maxPostFilePartSize))   {
                                        gbFiles.add(jo.getString("fp"+i).replace("\\", "/"));
                                        multipart.addFormField("FGB"+index+"DSC"+dcount, jo.getString("dsc"+i));
                                        multipart.addFormField("FGB"+index+"FN"+dcount, jo.getString("fn"+i));
                                        reqBuilder.addPart("FGB"+index+"DSC"+dcount, new StringBody(jo.getString("dsc"+i), ContentType.TEXT_PLAIN));
                                        reqBuilder.addPart("FGB"+index+"FN"+dcount, new StringBody(jo.getString("fn"+i), ContentType.TEXT_PLAIN));
                                        sectFilesSize+=f.length();
                                        dcount++;
                                        logStr+="[="+dcount+"=]";
                                    }
                                    else {
                                        tooLargeGb=true;
                                        logStr+="Too large "+jo.getString("fp"+i);
                                    }
                                }
                                
                            }   catch(Exception fle) {
                                //sectFilesSize=0;
                                error_file_operation=true;
                                logStr+=("NAME "+npwp.getF1cname()+" - Error dsc file operation!!! "+fle);
                            }
                            
                        }
                        
                        if(dcount>0) { 
                            multipart.addFormField("FGBDC"+index, ""+dcount); 
                            reqBuilder.addPart("FGBDC"+index, new StringBody(""+dcount, ContentType.TEXT_PLAIN));
                        }

                    }
                }   catch(Exception fle) {
                    logStr+=(npwp.getF1cname()+" Error F1cGabaritsJson parse operation!!! "+fle+"[["+npwp.getF1cGabaritsJson()+"]]");
                }
                
                ArrayList<String> docFiles = new ArrayList<String>();
                
                try {
                    if(!npwp.getBxDocsJson().equals(npwp.getF1cDocsJson()))   {
                        //logStr+="["+npwp.getF1cDocsJson()+"]";
                        JsonReader jsonReader = Json.createReader(new StringReader(npwp.getF1cDocsJson()));
                        JsonObject jo = jsonReader.readObject();
                        
                        int dcount=0;
                        logStr+="cnt="+jo.getString("dc","0");
                        for(int i=0;i<Tools.parseInt(jo.getString("dc","0"),0);i++)    {
                            logStr+=("["+i+"]");
                            try {
                                if(jo.getString("fp"+i).length()>0&&uploadFiles) {
                                    File f = new File(mountImgPath+jo.getString("fp"+i).replace("\\", "/"));
                                    if(f.length()<=maxPostedDocFileSize&&((allFileSize+sectFilesSize+f.length())<maxPostFilePartSize))   {
                                        docFiles.add(jo.getString("fp"+i).replace("\\", "/"));
                                        multipart.addFormField("FDOC"+index+"DSC"+dcount, jo.getString("dsc"+i));
                                        //logStr+=("FDOC"+index+"FN+"+dcount+"="+jo.getString("fn"+i));
                                        multipart.addFormField("FDOC"+index+"FN"+dcount, jo.getString("fn"+i));
                                        reqBuilder.addPart("FDOC"+index+"DSC"+dcount, new StringBody(jo.getString("dsc"+i), ContentType.TEXT_PLAIN));
                                        reqBuilder.addPart("FDOC"+index+"FN"+dcount, new StringBody(StringEscapeUtils.escapeJava(
                                                jo.getString("fn"+i)), ContentType.TEXT_PLAIN));
                                        sectFilesSize+=f.length();
                                        dcount++;
                                    }
                                    else {
                                        tooLargeDoc=true;
                                        logStr+="Too large "+jo.getString("fp"+i);
                                    }
                                }
                                
                            }   catch(Exception fle) {
                                //sectFilesSize=0;
                                error_file_operation=true;
                                logStr+=("NAME "+npwp.getF1cname()+" - Error doc file operation!!! "+fle);
                            }
                            
                        }
                        
                        if(dcount>0) {
                            multipart.addFormField("FDOCDC"+index, ""+dcount);
                            reqBuilder.addPart("FDOCDC"+index, new StringBody(""+dcount, ContentType.TEXT_PLAIN));
                        }

                    }
                }   catch(Exception fle) {
                    logStr+=(npwp.getF1cname()+" Error F1cDocsJson parse operation!!! "+fle+"[["+npwp.getF1cDocsJson()+"]]");
                }
                
                try {
                    if(npwp.getF1cPicture().length()>0&&npwp.getBxPicture().length()==0&&uploadFiles) {
                        File f = new File(mountImgPath+npwp.getF1cPicture().replace("\\", "/"));
                        if(f.length()<=maxPostedFileSize&&((allFileSize+sectFilesSize+f.length())<maxPostFilePartSize))   {
                            sectFilesSize+=f.length();
                        }
                    }
                }   catch(Exception fle) {
                    //sectFilesSize=0;
                    error_file_operation=true;
                    logStr+=("NAME "+npwp.getF1cname()+" - Error getF1cPicture file operation!!! "+fle);
                }
                
                try {
                    
                    if(npwp.getF1cMcatalog().length()>0&&npwp.getBxMcatalog().length()==0&&uploadFiles) {
                        File f = new File(mountImgPath+npwp.getF1cMcatalog().replace("\\", "/"));
                        if(f.length()<=maxPostedDocFileSize&&((allFileSize+sectFilesSize+f.length())<maxPostFilePartSize))   {
                            sectFilesSize+=f.length();
                        }
                    }
                
                }   catch(Exception fle) {
                    //sectFilesSize=0;
                    error_file_operation=true;
                    logStr+=("NAME "+npwp.getF1cname()+" - Error getF1cMcatalog file operation!!! "+fle);
                }
                
                if((allFileSize+sectFilesSize)>maxPostFilePartSize)  {
                    logStr+=("NAME "+npwp.getF1cname()+" - too big sect files sizes summ!!!");
                    //continue; 
                }
                else    { 
                    if(!error_file_operation)    {
                            try {
                                if(!npwp.getF1cPicture().equals(npwp.getBxPicture())&&uploadFiles) {
                                    File f = new File(mountImgPath+npwp.getF1cPicture().replace("\\", "/"));
                                    if(f.length()<=maxPostedFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   { 
                                        logStr+=",getF1cPicture "+npwp.getF1cPicture()+"fname="+f.getName()+
                                                ",fpath="+f.getPath()+",flength=["+f.length()+"]";
                                        allFileSize+=f.length();
                                        
                                        try {
                                            BufferedImage originalImage = ImageIO.read(f);
                                            if((originalImage.getHeight()>400||originalImage.getWidth()>400)&&false) {
                                                int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                                                BufferedImage resizeImageJpg = resizeImage(originalImage, type);
                                                
                                                logStr+="Not require resize";
                                                //multipart.addFilePart("PICTURE"+index, (resizeImageJpg) );
                                                //multipart.addFormField("SETPICTURE"+index, "1");
                                            }   else    {
                                                logStr+="Not require resize";
                                                multipart.addFilePart("PICTURE"+index, (f) );
                                                multipart.addFormField("SETPICTURE"+index, "1");
                                                multipart.addFormField("PICTURE_PATH"+index, 
                                                    StringEscapeUtils.escapeJava(npwp.getF1cPicture()));
                                                reqBuilder.addPart("PICTURE"+index, new FileBody(f));
                                                reqBuilder.addPart("SETPICTURE"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                                            }
                                        } catch(Exception frle) {
                                            logStr+="Error resizing"+frle;
                                            multipart.addFilePart("PICTURE"+index, (f) );
                                            multipart.addFormField("SETPICTURE"+index, "1");
                                            multipart.addFormField("PICTURE_PATH"+index, 
                                                    StringEscapeUtils.escapeJava(npwp.getF1cPicture()));
                                            reqBuilder.addPart("PICTURE"+index, new FileBody(f));
                                            reqBuilder.addPart("SETPICTURE"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                                        }
                                    }
                                    else    {
                                        multipart.addFormField("SETPICTURE"+index, "0");
                                        reqBuilder.addPart("SETPICTURE"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                                        logStr+=",getF1cPicture "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                    }
                                }
                            }   catch(Exception fle) {
                                error_file_operation=true;
                                logStr+=("NAME "+npwp.getF1cname()+" - Error getF1cPicture file operation!!! "+fle);
                            }
                            try {
                                if(npwp.getF1cMcatalog().length()>0&&npwp.getBxMcatalog().length()==0&&uploadFiles) {
                                    File f = new File(mountImgPath+npwp.getF1cMcatalog().replace("\\", "/"));
                                    if(f.length()<=maxPostedDocFileSize&&((allFileSize+f.length())<maxPostFilePartSize))   {
                                        logStr+=",getF1cMcatalog "+npwp.getF1cMcatalog();
                                        allFileSize+=f.length();
                                        multipart.addFilePart("MASTER_CATALOG"+index, (f) );
                                        multipart.addFormField("SETMASTER_CATALOG"+index, "1");
                                        reqBuilder.addPart("MASTER_CATALOG"+index, new FileBody(f));
                                        reqBuilder.addPart("SETMASTER_CATALOG"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                                    }
                                    else    {
                                        multipart.addFormField("SETMASTER_CATALOG"+index, "0");
                                        reqBuilder.addPart("SETMASTER_CATALOG"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                                        logStr+=",getF1cMcatalog "+f.length()+" more "+maxPostedDocFileSize/1000000+"Mbytes or too big allFileSize";
                                    }
                                }
                            }   catch(Exception fle) {
                                error_file_operation=true;
                                logStr+=("NAME "+npwp.getF1cname()+" - Error getF1cMcatalog file operation!!! "+fle);
                            }
                            
                            
                            int succFilesCount=0;
                            for(int i=0;i<dscFiles.size();i++)    {
                                try {
                                    if(dscFiles.get(i).length()>0&&uploadFiles) {
                                        File f = new File(mountImgPath+dscFiles.get(i));
                                        long lastFSize=0;
                                        lastFSize=f.length();
                                        if(lastFSize<=maxPostedFileSize&&((allFileSize+lastFSize)<maxPostFilePartSize))   {
                                            logStr+=",dscFiles "+dscFiles.get(i);
                                            multipart.addFilePart("FDSC"+index+"FP"+i, (f));
                                            multipart.addFormField("FDSC"+index+"FPSET"+i, "1");
                                            reqBuilder.addPart("FDSC"+index+"FP"+i, new FileBody(f));
                                            reqBuilder.addPart("FDSC"+index+"FPSET"+i, new StringBody("1", ContentType.TEXT_PLAIN));
                                            succFilesCount++;
                                            allFileSize+=lastFSize;
                                        }
                                        else    {
                                            //if(lastFSize>maxPostedFileSize)
                                            //    succFilesCount++;
                                            tooLargeDsc=true;
                                            multipart.addFormField("FDSC"+index+"FPSET"+i, "0");
                                            reqBuilder.addPart("FDSC"+index+"FPSET"+i, new StringBody("0", ContentType.TEXT_PLAIN));
                                            logStr+=",dscFiles "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                        }
                                    }
                                }   catch(Exception fle) {
                                    tooLargeDsc=true;
                                    error_file_operation=true;
                                    logStr+=("NAME "+npwp.getF1cname()+" - Error dscFiles file operation!!! "+fle);
                                }
                                
                            }
                            
                            if(!tooLargeDsc)    {
                            if( (dscFiles.size()<=0) ||( (succFilesCount>=1) && (succFilesCount>=(int)(dscFiles.size()/2)) ) ) {
                                if(!npwp.getBxDescriptsJson().equals(npwp.getF1cDescriptsJson()))   {
                                    multipart.addFormField("SETDESCRIPTS_JSON"+index, "1");
                                    reqBuilder.addPart("SETDESCRIPTS_JSON"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                                }
                                else    {
                                    multipart.addFormField("SETDESCRIPTS_JSON"+index, "0");
                                    reqBuilder.addPart("SETDESCRIPTS_JSON"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                                }
                                multipart.addFormField("DESCRIPTS_JSON"+index, StringEscapeUtils.escapeJava(npwp.getF1cDescriptsJson()));
                                reqBuilder.addPart("DESCRIPTS_JSON"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cDescriptsJson()), ContentType.TEXT_PLAIN));
                            }   else logStr+=("NAME "+npwp.getF1cname()+" - Error dscFiles count!!! ");
                            }
                            
                            succFilesCount=0;
                            for(int i=0;i<gbFiles.size();i++)    {
                                try {
                                    if(gbFiles.get(i).length()>0&&uploadFiles) {
                                        File f = new File(mountImgPath+gbFiles.get(i));
                                        long lastFSize=0;
                                        lastFSize=f.length();
                                        if(lastFSize<=maxPostedFileSize&&((allFileSize+lastFSize)<maxPostFilePartSize))   {
                                            logStr+=",gbFiles "+gbFiles.get(i);
                                            multipart.addFilePart("FGB"+index+"FP"+i, (f));
                                            multipart.addFormField("FGB"+index+"FPSET"+i, "1");
                                            reqBuilder.addPart("FGB"+index+"FP"+i, new FileBody(f));
                                            reqBuilder.addPart("FGB"+index+"FPSET"+i, new StringBody("1", ContentType.TEXT_PLAIN));
                                            succFilesCount++;
                                            allFileSize+=lastFSize;
                                        }
                                        else    {
                                            //if(lastFSize>maxPostedFileSize)
                                            //    succFilesCount++;
                                            tooLargeGb=true;
                                            multipart.addFormField("FGB"+index+"FPSET"+i, "0");
                                            reqBuilder.addPart("FGB"+index+"FPSET"+i, new StringBody("0", ContentType.TEXT_PLAIN));
                                            logStr+=",gbFiles "+f.length()+" more "+maxPostedFileSize/1000000+"Mbytes or too big allFileSize";
                                        }
                                    }
                                }   catch(Exception fle) {
                                    tooLargeGb=true;
                                    error_file_operation=true;
                                    logStr+=("NAME "+npwp.getF1cname()+" - Error gbrFiles file operation!!! "+fle);
                                }
                                
                            }
                            
                            if(!tooLargeGb)    {
                            if( (gbFiles.size()<=0) ||( (succFilesCount>=1) && (succFilesCount>=(int)(gbFiles.size()/2)) ) ) {
                                if(!npwp.getBxGabaritsJson().equals(npwp.getF1cGabaritsJson())) {
                                    multipart.addFormField("SETGABARITS_JSON"+index, "1");
                                    reqBuilder.addPart("SETGABARITS_JSON"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                                }
                                else    {
                                    multipart.addFormField("SETGABARITS_JSON"+index, "0");
                                    reqBuilder.addPart("SETGABARITS_JSON"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                                }
                                multipart.addFormField("GABARITS_JSON"+index, StringEscapeUtils.escapeJava(npwp.getF1cGabaritsJson()));
                                reqBuilder.addPart("GABARITS_JSON"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cGabaritsJson()), ContentType.TEXT_PLAIN));
                            }   else logStr+=("NAME "+npwp.getF1cname()+" - Error gbrFiles count!!! ");
                            }
                            
                            succFilesCount=0;
                            for(int i=0;i<docFiles.size();i++)    {
                                try {
                                    if(docFiles.get(i).length()>0&&uploadFiles) {
                                        File f = new File(mountImgPath+docFiles.get(i));
                                        long lastFSize=0;
                                        lastFSize=f.length();
                                        if(lastFSize<=maxPostedDocFileSize&&((allFileSize+lastFSize)<maxPostFilePartSize))   {
                                            logStr+=",docFiles "+docFiles.get(i);
                                            multipart.addFilePart("FDOC"+index+"FP"+i, (f));
                                            multipart.addFormField("FDOC"+index+"FPSET"+i, "1");
                                            reqBuilder.addPart("FDOC"+index+"FP"+i, new FileBody(f));
                                            reqBuilder.addPart("FDOC"+index+"FPSET"+i, new StringBody("1", ContentType.TEXT_PLAIN));
                                            succFilesCount++;
                                            allFileSize+=lastFSize;
                                        }
                                        else    {
                                            //if(lastFSize>maxPostedFileSize)
                                            //    succFilesCount++;
                                            tooLargeDoc=true;
                                            multipart.addFormField("FDOC"+index+"FPSET"+i, "0");
                                            reqBuilder.addPart("FDOC"+index+"FPSET"+i, new StringBody("0", ContentType.TEXT_PLAIN));
                                            logStr+=",docFiles "+f.length()+" more "+maxPostedDocFileSize/1000000+"Mbytes or too big allFileSize";
                                        }
                                    }
                                }   catch(Exception fle) {
                                    tooLargeDoc=true;
                                    error_file_operation=true;
                                    logStr+=("NAME "+npwp.getF1cname()+" - Error docFiles file operation!!! "+fle);
                                }
                                
                            }
                            
                            if(!tooLargeDoc)    {
                            if( (docFiles.size()<=0) ||( (succFilesCount>=1) && (succFilesCount>=(int)(docFiles.size()/2)) ) ) {
                                if(!npwp.getBxDocsJson().equals(npwp.getF1cDocsJson())) {
                                    multipart.addFormField("SETDOCS_JSON"+index, "1");
                                    reqBuilder.addPart("SETDOCS_JSON"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                                }
                                else    {
                                    multipart.addFormField("SETDOCS_JSON"+index, "0");
                                    reqBuilder.addPart("SETDOCS_JSON"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                                }
                                multipart.addFormField("DOCS_JSON"+index, StringEscapeUtils.escapeJava(npwp.getF1cDocsJson()));
                                reqBuilder.addPart("DOCS_JSON"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cDocsJson()), ContentType.TEXT_PLAIN));
                            }   else logStr+=("NAME "+npwp.getF1cname()+" - Error docFiles count!!! ");
                            }
                            
                    } 
                }
                
                if(npwp.getBxSortOrder()!=npwp.getF1cSortOrder())   {
                    multipart.addFormField("SETSORT_ORDER"+index, "1");
                    reqBuilder.addPart("SETSORT_ORDER"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSORT_ORDER"+index, "0");
                    reqBuilder.addPart("SETSORT_ORDER"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("SORT_ORDER"+index, ""+npwp.getF1cSortOrder());
                reqBuilder.addPart("SORT_ORDER"+index, new StringBody(""+npwp.getF1cSortOrder(), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxDescription().equals(npwp.getF1cDescription()))   {
                    multipart.addFormField("SETDESCRIPTION"+index, "1");
                    reqBuilder.addPart("SETDESCRIPTION"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETDESCRIPTION"+index, "0");
                    reqBuilder.addPart("SETDESCRIPTION"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("DESCRIPTION"+index, StringEscapeUtils.escapeJava(npwp.getF1cDescription()));
                reqBuilder.addPart("DESCRIPTION"+index, new StringBody(""+npwp.getF1cSortOrder(), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxFullDescription().equals(npwp.getF1cFullDescription()))   {
                    multipart.addFormField("SETFULL_DESCRIPTION"+index, "1");
                    reqBuilder.addPart("SETFULL_DESCRIPTION"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETFULL_DESCRIPTION"+index, "0");
                    reqBuilder.addPart("SETFULL_DESCRIPTION"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("FULL_DESCRIPTION"+index, StringEscapeUtils.escapeJava(npwp.getF1cFullDescription()));
                reqBuilder.addPart("FULL_DESCRIPTION"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cFullDescription()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxTypeCompleting().equals(npwp.getF1cTypeCompleting())) {
                    multipart.addFormField("SETTYPE_COMPLETING"+index, "1");
                    reqBuilder.addPart("SETTYPE_COMPLETING"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETTYPE_COMPLETING"+index, "0");
                    reqBuilder.addPart("SETTYPE_COMPLETING"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("TYPE_COMPLETING"+index, StringEscapeUtils.escapeJava(npwp.getF1cTypeCompleting()));
                reqBuilder.addPart("TYPE_COMPLETING"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cTypeCompleting()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxCharGabarits().equals(npwp.getF1cCharGabarits())) {
                    multipart.addFormField("SETCHAR_GABARITS"+index, "1");
                    reqBuilder.addPart("SETCHAR_GABARITS"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETCHAR_GABARITS"+index, "0");
                    reqBuilder.addPart("SETCHAR_GABARITS"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("CHAR_GABARITS"+index, StringEscapeUtils.escapeJava(npwp.getF1cCharGabarits()));
                reqBuilder.addPart("CHAR_GABARITS"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cCharGabarits()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxShortDescription().equals(npwp.getF1cShortDescription())) {
                    multipart.addFormField("SETSHORT_DESCRIPTION"+index, "1");
                    reqBuilder.addPart("SETSHORT_DESCRIPTION"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSHORT_DESCRIPTION"+index, "0");
                    reqBuilder.addPart("SETSHORT_DESCRIPTION"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("SHORT_DESCRIPTION"+index, StringEscapeUtils.escapeJava(npwp.getF1cShortDescription()));
                reqBuilder.addPart("SHORT_DESCRIPTION"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cShortDescription()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxDocumentation().equals(npwp.getF1cDocumentation()))   {
                    multipart.addFormField("SETDOCUMENTATION"+index, "1");
                    reqBuilder.addPart("SETDOCUMENTATION"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETDOCUMENTATION"+index, "0");
                    reqBuilder.addPart("SETDOCUMENTATION"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("DOCUMENTATION"+index, StringEscapeUtils.escapeJava(npwp.getF1cDocumentation()));
                reqBuilder.addPart("DOCUMENTATION"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cDocumentation()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxVideoDescription().equals(npwp.getF1cVideoDescription())) {
                    multipart.addFormField("SETVIDEO_DESCRIPTION"+index, "1");
                    reqBuilder.addPart("SETVIDEO_DESCRIPTION"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETVIDEO_DESCRIPTION"+index, "0");
                    reqBuilder.addPart("SETVIDEO_DESCRIPTION"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("VIDEO_DESCRIPTION"+index, StringEscapeUtils.escapeJava(npwp.getF1cVideoDescription()));
                reqBuilder.addPart("VIDEO_DESCRIPTION"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cVideoDescription()), ContentType.TEXT_PLAIN));
                
                if(npwp.getBxCollapsevc()!=npwp.getF1cCollapsevc()) {
                    multipart.addFormField("SETCOLLAPSEVC"+index, "1");
                    reqBuilder.addPart("SETCOLLAPSEVC"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETCOLLAPSEVC"+index, "0");
                    reqBuilder.addPart("SETCOLLAPSEVC"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("COLLAPSEVC"+index, StringEscapeUtils.escapeJava(""+npwp.getF1cCollapsevc()));
                reqBuilder.addPart("COLLAPSEVC"+index, new StringBody(StringEscapeUtils.escapeJava(""+npwp.getF1cCollapsevc()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxAdvants().equals(npwp.getF1cAdvants()))   {
                    multipart.addFormField("SETADVANTS"+index, "1");
                    reqBuilder.addPart("SETADVANTS"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETADVANTS"+index, "0");
                    reqBuilder.addPart("SETADVANTS"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("ADVANTS"+index, StringEscapeUtils.escapeJava(npwp.getF1cAdvants()));
                reqBuilder.addPart("ADVANTS"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cAdvants()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxFilterProps().equals(npwp.getF1cFilterProps()))   {
                    multipart.addFormField("SETFILTER_PROPS"+index, "1");
                    reqBuilder.addPart("SETFILTER_PROPS"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETFILTER_PROPS"+index, "0");
                    reqBuilder.addPart("SETFILTER_PROPS"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("FILTER_PROPS"+index, StringEscapeUtils.escapeJava(npwp.getF1cFilterProps()));
                reqBuilder.addPart("FILTER_PROPS"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cFilterProps()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxSeoAliasUrl().equals(npwp.getF1cSeoAliasUrl())&&npwp.getF1cSeoAliasUrl().length()>3)  {
                    multipart.addFormField("SETSEO_ALIAS_URL"+index, "1");
                    reqBuilder.addPart("SETSEO_ALIAS_URL"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSEO_ALIAS_URL"+index, "0");
                    reqBuilder.addPart("SETSEO_ALIAS_URL"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("SEO_ALIAS_URL"+index, StringEscapeUtils.escapeJava(npwp.getF1cSeoAliasUrl()));
                reqBuilder.addPart("SEO_ALIAS_URL"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cSeoAliasUrl()), ContentType.TEXT_PLAIN));
                
                if(npwp.getBxSeoAliasUrl().length()<=0&&npwp.getF1cSeoAliasUrl().length()<=3&&
                        npwp.getF1cname().length()>0)   {
                    multipart.addFormField("SETSEO_ALIAS_URL_FROM_NAME"+index, "1");
                    multipart.addFormField("SEO_ALIAS_URL_FROM_NAME"+index, StringEscapeUtils.escapeJava(npwp.getF1cname()));
                    reqBuilder.addPart("SETSEO_ALIAS_URL_FROM_NAME"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                    reqBuilder.addPart("SEO_ALIAS_URL_FROM_NAME"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cname()), ContentType.TEXT_PLAIN));
                }
                
                if(!npwp.getBxSeoTitle().equals(npwp.getF1cSeoTitle())) {
                    multipart.addFormField("SETSEO_TITLE"+index, "1");
                    reqBuilder.addPart("SETSEO_TITLE"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSEO_TITLE"+index, "0");
                    reqBuilder.addPart("SETSEO_TITLE"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("SEO_TITLE"+index, StringEscapeUtils.escapeJava(npwp.getF1cSeoTitle()));
                reqBuilder.addPart("SEO_TITLE"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cSeoTitle()), ContentType.TEXT_PLAIN));
                
                if(!npwp.getBxSeoH1().equals(npwp.getF1cSeoH1()))   {
                    multipart.addFormField("SETSEO_H1"+index, "1");
                    reqBuilder.addPart("SETSEO_H1"+index, new StringBody("1", ContentType.TEXT_PLAIN));
                }
                else    {
                    multipart.addFormField("SETSEO_H1"+index, "0");
                    reqBuilder.addPart("SETSEO_H1"+index, new StringBody("0", ContentType.TEXT_PLAIN));
                }
                multipart.addFormField("SEO_H1"+index, StringEscapeUtils.escapeJava(npwp.getF1cSeoH1()));
                reqBuilder.addPart("SEO_H1"+index, new StringBody(StringEscapeUtils.escapeJava(npwp.getF1cSeoH1()), ContentType.TEXT_PLAIN));
                                
                index++;
            }
            
            try {
                if(logStr.length()>0)
                cbLogsFacade.insertLog("INFO", "UPD SECT TO SEND ", "<p style=\"font-size:10px !important;\">"+logStr+"</p>");
            } catch(Exception lgen)  {
            }
 
            List<String> response = multipart.finish(); 
            
            for (String line : response) {
                sreply=sreply+line;
            }
            
            /*sreply = "{}";
            
            HttpEntity reqEntity = reqBuilder.build();
            httppost.setEntity(reqEntity);

            //System.out.println("executing request " + httppost.getRequestLine());
            try {
                CloseableHttpResponse clresponse = httpclient.execute(httppost);
                try {
                    //System.out.println("----------------------------------------");
                    //clresponse.getStatusLine();
                    HttpEntity resEntity = clresponse.getEntity();
                    if (resEntity != null) {
                        //System.out.println("Response content length: " + resEntity.getContentLength());
                        //resEntity.getContent(); resEntity.getContentEncoding()
                        try {
                            String responseString = EntityUtils.toString(resEntity, "UTF-8");
                            sreply = responseString;
                            EntityUtils.consume(resEntity);
                        } catch(Exception ee)   {
                            
                        }
                    }
                    
                } finally {
                    try {
                        clresponse.close();
                    } catch(Exception ee)   {

                    }
                }
            } catch(Exception ee)   {
                    
            }*/
            
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(sreply));
                JsonObject jobject = jsonReader.readObject();
                try {
                    cbLogsFacade.insertLog("INFO", "SUCCESS OF PARSE SERVER REPLY", "SUCCESS OF PARSE SERVER REPLY="+jobject.toString());
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_info", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_INFO", "SERVER REPLY DETAIL", jobject.getString("critical_info", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_errs", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_ERRS", "SERVER REPLY DETAIL", jobject.getString("critical_errs", ""));
                } catch(Exception lge)  {

                }
            }   catch(Exception pe) {
                try {
                    cbLogsFacade.insertLog("ERROR", "ERROR OF PARSE SERVER REPLY", "ERROR OF PARSE SERVER REPLY"+sreply);
                } catch(Exception lge)  {

                }
            }
            
        } catch (Exception ex) {
            try {
                cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartSectUpd", logStr+" ERROR of postMultipartSectUpd "+ex);
            } catch(Exception lge)  {

            }
        }
        
        return reply;
    }
    
    List<CbEkfgroupUpd1csectToBx> uswps_;
    
    private void sendCompareSectDataUpd()  {
        Runnable r4 = new Runnable() {
                public void run() {
                    try {

                        if(uswps_.size()==0)    {
                        try {
                                cbLogsFacade.insertLog("INFO", "Data params", "Upd sect cnt="+uswps_.size());
                            } catch(Exception lge)  {

                            }
                        }
                        
                        if(postMultipartSectUpd(uswps_))   {
                            sendCompareSectDataDel();
                        }   else    {
                            exchangeInProcess = false;
                        }
                    }   catch(Exception e)    {
                        exchangeInProcess = false;
                        try {
                                cbLogsFacade.insertLog("ERROR", "Error of sendCompareDataUpd procedure", "Error of sendCompareDataUpd procedure"+e);
                            } catch(Exception lge)  {

                            }
                    }
            }
                
        };
                
        Thread t = new Thread(r4);
        t.start();
    }
    
    private boolean postMultipartSectDel(List<CbEkfgroupDel1csectFromBx > dswps)    {
        String charset = "UTF-8";
        String requestURL = systemURL+"manipulate_data.php";
        boolean reply=true;
        String sreply="";
        
        if(dswps.isEmpty())  
            return reply;
        
        String logStr="Del sect cnt="+dswps.size()+". ";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            int index=0;
            multipart.addFormField("OTYPE","DELETE");
            multipart.addFormField("ENTITY","1CSECT");
            int ocnt=dswps.size(); 
            multipart.addFormField("OCNT",""+ocnt);
            //String logStr="";
            for(CbEkfgroupDel1csectFromBx npwp: dswps)    { 
                multipart.addFormField("SID"+index, npwp.getId());
                multipart.addFormField("SNAME"+index, npwp.getName());
                multipart.addFormField("SBXID"+index, ""+npwp.getBxId());
                //npwp.
                logStr+="["+index+"] id="+npwp.getId()+","+npwp.getName()+",BXID="+npwp.getBxId();
                index++;
            }
           
            try {
                if(logStr.length()>0)
                cbLogsFacade.insertLog("INFO", "DEL SECT TO SEND ", logStr);
            } catch(Exception lgen)  {
            }
 
            List<String> response = multipart.finish(); 
            
            for (String line : response) {
                sreply=sreply+line;
            }
            
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(sreply));
                JsonObject jobject = jsonReader.readObject();
                try {
                    cbLogsFacade.insertLog("INFO", "SUCCESS OF PARSE SERVER REPLY", "SUCCESS OF PARSE SERVER REPLY="+jobject.toString());
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_info", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_INFO", "SERVER REPLY DETAIL", jobject.getString("critical_info", ""));
                } catch(Exception lge)  {

                }
                try {
                    if(jobject.getString("critical_errs", "").length()>0)
                        cbLogsFacade.insertLog("CRITICAL_ERRS", "SERVER REPLY DETAIL", jobject.getString("critical_errs", ""));
                } catch(Exception lge)  {

                }
                
            }   catch(Exception pe) {
                try {
                    cbLogsFacade.insertLog("ERROR", "ERROR OF PARSE SERVER REPLY", "ERROR OF PARSE SERVER REPLY"+sreply);
                } catch(Exception lge)  {

                }
            }
            
        } catch (Exception ex) {
            try {
                cbLogsFacade.insertLog("ERROR", "ERROR of postMultipartSectDel", logStr+" ERROR of postMultipartSectDel"+ex);
            } catch(Exception lge)  {

            }
        }
        
        return reply;
    }
    
    List<CbEkfgroupDel1csectFromBx> dswps_;
    
    private void sendCompareSectDataDel()  {
        Runnable r4 = new Runnable() {
                public void run() {
                    try {

                        if(dswps_.size()==0)    {
                        try {
                                cbLogsFacade.insertLog("INFO", "Data params", "Del sect cnt="+dswps_.size());
                            } catch(Exception lge)  {

                            }
                        }

                        if(postMultipartSectDel(dswps_))   {
                            //try {
                            //    cbLogsFacade.insertLog("INFO", "Break deleting!!!", "Break deleting!!!");
                            //} catch(Exception lge)  {

                            //}
                            
                            sendCompareData();
                        }   else    {
                            exchangeInProcess = false;
                        }

                    }   catch(Exception e)    {
                        exchangeInProcess = false;
                        try {
                                cbLogsFacade.insertLog("ERROR", "Error of sendCompareDataDel procedure", "Error of sendCompareDataDel procedure"+e);
                            } catch(Exception lge)  {

                            }
                    }
            }
                
        };
                
        Thread t = new Thread(r4);
        t.start();
    }
    
    private void callT2()   {
        
        Runnable r2 = new Runnable() {

                public void run() {
                    int badCnt2=0;
                    boolean success_sql_operation=false;
                    StringBuilder insert_sql_values_sb = new StringBuilder();
                    int insert_sql_cnt = 0;
                    String requestRes = "";
                    try {
                        
                        try {
                            cbLogsFacade.insertLog("INFO", "Лог перед запросом в сеть к данным ekfgroup.com", "Если за этими логами ничего не следует кроме (Timer condition worked и Bad full exchange circle) то запрос накрылся из-за недоступности сайта или отсутствия интернета, админы вы где?");
                        } catch(Exception lge)  { }
                        
                        String url2 = systemURL+"get_json_data.php?ENTITY=1CSECT";
                        
                        //try {
                        //    cbLogsFacade.insertLog("INFO", "Start load bx_1csect", "Start load bx_1csect, url="+systemURL);
                        //} catch(Exception lge)  {
                            
                        //}

                        URL obj2 = new URL(url2);
                        HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();

                        // optional default is GET
                        con2.setRequestMethod("GET");
                        con2.setConnectTimeout(180000);
                        con2.setReadTimeout(180000);

                        //add request header
                        con2.setRequestProperty("User-Agent", "Mozilla-Firefox");

                        int responseCode2 = con2.getResponseCode();
                        System.out.println("\nSending 'GET' request to URL : " + url2);
                        System.out.println("Response Code : " + responseCode2);

                        BufferedReader in2 = new BufferedReader(
                                new InputStreamReader(con2.getInputStream()));
                        String inputLine2;
                        StringBuffer response2 = new StringBuffer();

                        while ((inputLine2 = in2.readLine()) != null) {
                                response2.append(inputLine2);
                        }
                        in2.close();
                        
                        //try {
                        //    cbLogsFacade.insertLog("INFO", "Exchange in process 8", "Exchange in process 8");
                        //} catch(Exception lge)  { }
                        
                        requestRes = response2.toString();

                        try {
                            cbLogsFacade.insertLog("INFO", "Complete load bx_1csect urldata", "Complete load bx_1csect urldata, url="+url2);
                        } catch(Exception lge)  {
                            
                        }
                        
                        JsonReader jsonReader2 = Json.createReader(new StringReader(response2.toString()));
                        
                        bx1CSectFacade.clearBx1CSect();

                        JsonArray jarray2 = jsonReader2.readArray();
                        int crcnt2=0;
                        
                        boolean hasCrashes2 = false;
                        String saveBxSectLog = "";
                        for(int i=0;i<jarray2.size();i++)    {
                            JsonObject jobject2 = jarray2.getJsonObject(i);
                            Bx1CSect b1cssectObj = new Bx1CSect();
                            b1cssectObj.setId(-1);
                            
                            if(insert_sql_cnt>0)   insert_sql_values_sb.append(" ,"); 
                            insert_sql_values_sb.append("( ");
                            
                            try {
                                b1cssectObj.setBxId(Tools.parseInt(jobject2.getString("ID", "-1"), -1));
                            } catch(Exception e)    {
                                b1cssectObj.setBxId(-1);
                            }
                            try {
                            String f1cId = jobject2.getString("1C_ID", "");
                            if(f1cId.length()==36)
                                b1cssectObj.setF1cId(f1cId);
                            else
                                b1cssectObj.setF1cId("NULL");
                            } catch(Exception e)    {
                                b1cssectObj.setF1cId("NULL");
                            }
                            try {
                                b1cssectObj.setParentBxId(Tools.parseInt(jobject2.getString("PARENT_ID", "-1"), -1));
                            } catch(Exception e)    {
                                b1cssectObj.setParentBxId(-1);
                            }
                            
                            try {
                            String parent1cId = jobject2.getString("PARENT_1CID", "NULL");
                            if(parent1cId.length()==36)
                                b1cssectObj.setParent1cId(parent1cId);
                            else
                                b1cssectObj.setParent1cId("NULL");
                            } catch(Exception e)    {
                                b1cssectObj.setParent1cId("NULL");
                            }
                            
                            try {
                                b1cssectObj.setName(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("NAME", "NULL")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setName("NULL");
                            }
                            
                            try {
                                b1cssectObj.setPicture(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("PICTURE", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setPicture("");
                            }
                            
                            try {
                                b1cssectObj.setMcatalog(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("MASTER_CATALOG", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setMcatalog("");
                            }
                            
                            //inputLine2)
                            
                            try {
                                b1cssectObj.setDescription(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("DESCRIPTION", "")).
                                                replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setDescription("");
                            }
                            
                            try {
                                b1cssectObj.setFullDescription(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("FULL_DESCRIPTION", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setFullDescription("");
                            }
                            
                            try {
                                b1cssectObj.setTypeCompleting(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("TYPE_COMPLETING", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setTypeCompleting("");
                            }
                            
                            try {
                                b1cssectObj.setCharGabarits(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("CHAR_GABARITS", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setCharGabarits("");
                            }
                            
                            try {
                                b1cssectObj.setDocumentation(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("DOCUMENTATION", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setDocumentation("");
                            }
                            
                            try {
                                b1cssectObj.setShortDesription(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("SHORT_DESCRIPTION", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setShortDesription("");
                            }
                            
                            try {
                                b1cssectObj.setVideoDescription(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("VIDEO_DESCRIPTION", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cssectObj.setVideoDescription("");
                            }
                            
                            insert_sql_values_sb.append(b1cssectObj.getBxId());
                            insert_sql_values_sb.append(",'");
                            insert_sql_values_sb.append(b1cssectObj.getName().replace("'", "''"));
                            insert_sql_values_sb.append("',");
                            insert_sql_values_sb.append(b1cssectObj.getParentBxId());
                            insert_sql_values_sb.append(",'");
                            insert_sql_values_sb.append(b1cssectObj.getF1cId().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getParent1cId().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getFullDescription().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getTypeCompleting().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getCharGabarits().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getShortDesription().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getDocumentation().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getDescription().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getPicture().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cssectObj.getVideoDescription().replace("'", "''"));
                            insert_sql_values_sb.append("','");//
                            insert_sql_values_sb.append(b1cssectObj.getMcatalog().replace("'", "''"));
                            insert_sql_values_sb.append("',");
                            insert_sql_values_sb.append((Tools.parseInt(jobject2.getString("COLLAPSEVC", "0"),0)==1?1:0));
                            insert_sql_values_sb.append(",'");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("ADVANTS", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("','");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("FILTER_PROPS", "")).replace("&quot;", "\"")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("','");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("DESCRIPTS_JSON", "")).replace("&quot;", "\"").replace("'", "''")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("','");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("GABARITS_JSON", "")).replace("&quot;", "\"").replace("'", "''")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("','");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("DOCS_JSON", "")).replace("&quot;", "\"").replace("'", "''")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("',");
                            try {
                            insert_sql_values_sb.append(
                                        Tools.parseInt(StringEscapeUtils.unescapeJson(jobject2.getString("SORT_ORDER", "0")),0)  );
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append(",'");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("SEO_ALIAS_URL", "")).replace("&quot;", "\"").replace("'", "''")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("','");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("SEO_TITLE", "")).replace("&quot;", "\"").replace("'", "''")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("','");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject2.getString("SEO_H1", "")).replace("&quot;", "\"").replace("'", "''")));
                            } catch(Exception e)    {}
                            insert_sql_values_sb.append("')");
                            
                            int try_cnt2=0;
                            boolean notSucc2 = true;
                            String err="";
                            while(try_cnt2<10&&notSucc2) {
                                try {
                                    //bx1CSectFacade.create(b1cssectObj);
                                    //bx1CSectFacade.
                                    crcnt2++;
                                    notSucc2=false;
                                } catch(Exception e) {
                                    notSucc2=true;
                                    badCnt2++;
                                    try_cnt2++;
                                    err+="[["+Tools.parseInt(jobject2.getString("ID", "-1"), -1)+"]]<<==!!||||||!!==>>Error of bx1CsectFacade.create "+e;
                                }
                            }
                            try {
                                if(try_cnt2>0)
                                    cbLogsFacade.insertLog("ERROR", "Error of bx1CSectFacade", err);
                            } catch(Exception lge)  {
                            
                            }
                            hasCrashes2 = hasCrashes2 | notSucc2;
                            
                            insert_sql_cnt++;
                            if(insert_sql_cnt>=500||i>=(jarray2.size()-1))   {
                                try {
                                    success_sql_operation = bx1CSectFacade.insertBx1SectMultiply(insert_sql_values_sb.toString(), insert_sql_cnt);
                                } catch(Exception lgesq)  {
                                    success_sql_operation = false;
                                    try {
                                        cbLogsFacade.insertLog("INFO", "!!!Unsuccess sending complex sql instruct to bx_1csect", "Err "+lgesq);
                                    } catch(Exception lge)  {

                                    }
                                }
                                
                                if(!success_sql_operation)  {
                                    try {
                                        cbLogsFacade.insertLog("INFO", "!!!Unsuccess complex sql instruct to bx_1csect", "Count record to bx_1csect "+insert_sql_cnt);
                                    } catch(Exception lge)  {

                                    }
                                    break;
                                }   else    {
                                    //saveBxSectLog+=("Count record to bx_1cpsect "+insert_sql_cnt+". ");
                                    //try {
                                    //    cbLogsFacade.insertLog("INFO", "Success complex sql instruct to bx_1csect", "Count record to bx_1cpsect "+insert_sql_cnt);
                                    //} catch(Exception lge)  {

                                    //}
                                }
                                insert_sql_cnt=0;
                                insert_sql_values_sb.setLength(0);
                            }
                        }
                        
                        try {
                            cbLogsFacade.insertLog("INFO", "Complete load bx_1csect", saveBxSectLog+" Complete load bx_1csect "+", all="+jarray2.size()+",succ="+crcnt2+",errcnt="+badCnt2);
                        } catch(Exception lge)  {
                            
                        }
                        
                        if(badCnt2<=0&&success_sql_operation)
                            callT3();
                        else
                            exchangeInProcess = false;

                    } catch(Exception e) {
                        exchangeInProcess = false;
                        try {
                            cbLogsFacade.insertLog("ERROR", "Error of get-parse json bx_1csect", "<<==!!||||||!!==>>Error of get-parse json "+e+",server reply["+requestRes.substring(0,200)+"]");
                        } catch(Exception lge)  {
                            
                        }
                        
                    }   finally {
                        
                    }
                }

            };
        
        Thread t2 = new Thread(r2);
        t2.start();
        
    }
    
    private void callT3()   {
        Runnable r3 = new Runnable() {

                public void run() {
                    int badCnt3=0;
                    int crcnt=0;
                    int allcnt=-1;
                    boolean success_sql_operation=false;
                    StringBuilder insert_sql_values_sb = new StringBuilder();
                    int insert_sql_cnt = 0;
                    try {
                        
                        String url = systemURL+"get_json_data.php?ENTITY=1CPROD";
                        
                        //try {
                        //    cbLogsFacade.insertLog("INFO", "Start load bx_1cprod", "Start load bx_1cprod, url="+systemURL);
                        //} catch(Exception lge)  {
                            
                        //}

                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                        // optional default is GET
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(180000);
                        con.setReadTimeout(180000);

                        //add request header
                        con.setRequestProperty("User-Agent", "Mozilla-Firefox");

                        int responseCode = con.getResponseCode();
                        System.out.println("\nSending 'GET' request to URL : " + url);
                        System.out.println("Response Code : " + responseCode);

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                        }
                        in.close();

                        try {
                            cbLogsFacade.insertLog("INFO", "Complete load bx_1cprod urldata", "Complete load bx_1cprod urldata, url="+systemURL);
                        } catch(Exception lge)  {
                            
                        }
                        JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
                        
                        //int icnt = 0;//bx1CProdFacade.insertBx1ProdMultiply();
                        
                        //try {
                        //    cbLogsFacade.insertLog("INFO", "Complete persist test bx_1cprod urldata ", "Complete persist test bx_1cprod urldata "+icnt);
                        //} catch(Exception lge)  {
                        //}
                        
                        bx1CProdFacade.clearBx1CProd();

                        JsonArray jarray = jsonReader.readArray();
                        
                        
                        boolean hasCrashes = false;
                        String saveBxDataLog = "";
                        for(int i=0;i<jarray.size();i++)    {
                            JsonObject jobject = jarray.getJsonObject(i);
                            Bx1CProd b1cprodObj = new Bx1CProd();
                            
                            b1cprodObj.setId(-1);
                            if(insert_sql_cnt>0)   insert_sql_values_sb.append(" ,"); 
                            insert_sql_values_sb.append("( ");
                            try {
                                b1cprodObj.setBxId(Tools.parseInt(jobject.getString("ID", "-1"), -1));
                            } catch(Exception e)    {
                                b1cprodObj.setBxId(-1);
                            }
                            
                            try {
                            String f1cId = jobject.getString("1C_ID", "NULL");
                            if(f1cId.length()==36)
                                b1cprodObj.setF1cId(f1cId);
                            else
                                b1cprodObj.setF1cId("NULL");
                            } catch(Exception e)    {
                                b1cprodObj.setF1cId("NULL");
                            }
                            try {
                                b1cprodObj.setParentBxId(Tools.parseInt(jobject.getString("PARENT_ID", "-1"), -1));
                            } catch(Exception e)    {
                                b1cprodObj.setParentBxId(-1);
                            }
                            
                            try {
                            String parent1cId = jobject.getString("PARENT_1CID", "NULL");
                            if(parent1cId.length()==36)
                                b1cprodObj.setParent1cId(parent1cId);
                            else
                                b1cprodObj.setParent1cId("NULL");
                            } catch(Exception e)    {
                                b1cprodObj.setParent1cId("NULL");
                            }
                            
                            try {
                                b1cprodObj.setName(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject.getString("NAME", "NULL")).replace("&quot;", "\"")));
                            } catch(Exception e)    {
                                b1cprodObj.setName("NULL");
                            }
                            
                            try {
                                b1cprodObj.setArtikul(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject.getString("ARTICUL", "NULL"))));
                            } catch(Exception e)    {
                                b1cprodObj.setArtikul("NULL");
                            }
                            
                            insert_sql_values_sb.append(b1cprodObj.getBxId());
                            insert_sql_values_sb.append(",'");
                            insert_sql_values_sb.append(b1cprodObj.getName().replace("'", "''"));
                            insert_sql_values_sb.append("',");
                            insert_sql_values_sb.append(b1cprodObj.getParentBxId());
                            insert_sql_values_sb.append(",'");
                            insert_sql_values_sb.append(b1cprodObj.getF1cId().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cprodObj.getParent1cId().replace("'", "''"));
                            insert_sql_values_sb.append("','");
                            insert_sql_values_sb.append(b1cprodObj.getArtikul().replace("'", "''"));
                            
                            insert_sql_values_sb.append("',");
                            
                            try { 
                                b1cprodObj.setPrice(new BigDecimal(jobject.getString("PRICE", "0")));
                                insert_sql_values_sb.append(jobject.getString("PRICE", "0").replace(",", "."));
                            } catch(Exception e)    {
                                b1cprodObj.setPrice(new BigDecimal(0));
                                insert_sql_values_sb.append("0");
                            }
                            insert_sql_values_sb.append(",");
                            
                            try {
                                b1cprodObj.setAmount(new BigDecimal(jobject.getString("QUANTITY", "0")));
                                insert_sql_values_sb.append(jobject.getString("QUANTITY", "0").replace(",", "."));
                            } catch(Exception e)    {
                                b1cprodObj.setAmount(new BigDecimal(0));
                                insert_sql_values_sb.append("0");
                            }
                            insert_sql_values_sb.append(",");
                            
                            try {
                                b1cprodObj.setBprice(new BigDecimal(jobject.getString("BPRICE", "0")));
                                insert_sql_values_sb.append(jobject.getString("BPRICE", "0").replace(",", "."));
                            } catch(Exception e)    {
                                b1cprodObj.setBprice(new BigDecimal(0));
                                insert_sql_values_sb.append("0");
                            }
                            insert_sql_values_sb.append(",");
                            
                            try {
                                //b1cprodObj.setBprice(new BigDecimal(jobject.getString("SORT_ORDER", "0")));
                                insert_sql_values_sb.append(jobject.getString("SORT_ORDER", "0").replace(",", "."));
                            } catch(Exception e)    {
                                //b1cprodObj.setBprice(new BigDecimal(0));
                                insert_sql_values_sb.append("0");
                            }
                            insert_sql_values_sb.append(",'");
                            try {
                            insert_sql_values_sb.append(StringEscapeUtils.unescapeHtml4(
                                        StringEscapeUtils.unescapeJson(jobject.getString("MAIN_PICT", ""))).replace("'", "''"));
                            } catch(Exception e)    {}
                            
                            insert_sql_values_sb.append("',");
                            
                            try {
                                //b1cprodObj.setBprice(new BigDecimal(jobject.getString("SORT_ORDER", "0")));
                                insert_sql_values_sb.append(""+jobject.getInt("PROP_CNT", 0));
                            } catch(Exception e)    {
                                //b1cprodObj.setBprice(new BigDecimal(0));
                                insert_sql_values_sb.append("0");
                            }
                            
                            insert_sql_values_sb.append(")");
                            
                            int try_cnt=0;
                            boolean notSucc = true;
                            String err="";
                            while(try_cnt<10&&notSucc) {
                                try {
                                    //bx1CSectFacade.
                                    //bx1CProdFacade.create(b1cprodObj);
                                    crcnt++;
                                    notSucc=false;
                                } catch(Exception e) {
                                    notSucc=true;
                                    badCnt3++;
                                    try_cnt++;
                                    err+="[["+Tools.parseInt(jobject.getString("ID", "-1"), -1)+"]]<<==!!||||||!!==>>Error of bx1CProdFacade.create "+e;
                                }
                            }
                            
                            try {
                                if(try_cnt>0)   {
                                    cbLogsFacade.insertLog("ERROR", "Error of bx1CProdFacade", err);
                                }
                            } catch(Exception lge)  {
                            
                            }
                            hasCrashes = hasCrashes | notSucc;
                            
                            insert_sql_cnt++;
                            if(insert_sql_cnt>=500||i>=(jarray.size()-1))   {
                                try {
                                    success_sql_operation = bx1CProdFacade.insertBx1ProdMultiply(insert_sql_values_sb.toString(), insert_sql_cnt);
                                } catch(Exception lgesq)  {
                                    success_sql_operation = false;
                                    try {
                                        cbLogsFacade.insertLog("INFO", "!!!Unsuccess sending complex sql instruct to bx_1cprod", "Err "+lgesq);
                                    } catch(Exception lge)  {

                                    }
                                }
                                
                                if(!success_sql_operation)  {
                                    try {
                                        cbLogsFacade.insertLog("INFO", "!!!Unsuccess complex sql instruct to bx_1cprod", "Count record to bx_1cprods "+insert_sql_cnt);
                                    } catch(Exception lge)  {

                                    }
                                    break;
                                }   else    {
                                    saveBxDataLog+=("Succ "+insert_sql_cnt);//complex sql instruct to bx_1cprod. "+
                                    //        "Count record to bx_1cprods "+insert_sql_cnt);
                                    //try {
                                    //    cbLogsFacade.insertLog("INFO", "Success complex sql instruct to bx_1cprod", "Count record to bx_1cprods "+insert_sql_cnt);
                                    //} catch(Exception lge)  {

                                    //}
                                }
                                insert_sql_cnt=0;
                                insert_sql_values_sb.setLength(0);
                            }
                        }
                        
                        try {
                            allcnt = jarray.size();
                            cbLogsFacade.insertLog("INFO", "Complete load bx_1cprod", saveBxDataLog+" Complete load bx_1cprods "+", all="+allcnt+",succ="+crcnt+",errcnt="+badCnt3);
                        } catch(Exception lge)  {
                            
                        }
                        
                        if(badCnt3<=20&&(allcnt==crcnt)&&success_sql_operation)  {
                            //sendCompareData();
                            //sendCompareSectDataDel();
                            //sendCompareSectDataUpd();
                            boolean repeat_load=true;
                            int repeat_counter=0;
                            while(repeat_load&&repeat_counter<10)  {
                               repeat_counter++; 
                               String selectExchngDataLog="";
                                try {

                                    if(tablesOperatingStateFacade.getEkfGrExchDataCorrupt()&&false)    {
                                        Thread.sleep(30000);
                                        try {
                                            cbLogsFacade.insertLog("INFO", "getEkfGrExchDataCorrupt", "Wait on 30 sec, attempt "+repeat_counter+" of 10...");
                                        } catch(Exception lgex)  {

                                        }
                                    }   else {
                                        repeat_load=false;
                                        int ekfProdCount = cbLogsFacade.ekfProdCount();
                                        int ekfProdSectCount = cbLogsFacade.ekfProdSectCount();
                                        if(ekfProdCount>=1000&&ekfProdCount<=100000&&ekfProdSectCount>=200&&ekfProdSectCount<=10000)   {
                                            if(tablesOperatingStateFacade.setEkfGrExchDataCorrupt())   {

                                                //try {
                                                //    cbLogsFacade.insertLog("INFO", "Start of request cbNewPrFrom1cWpropsFacade", "Start of request cbNewPrFrom1cWpropsFacade");
                                                //} catch(Exception lge)  { }
                                                selectExchngDataLog+="Start of request cbNewPrFrom1cWpropsFacade ";
                                                npwps_ = cbNewPrFrom1cWpropsFacade.findAll();

                                                //try {
                                                //    cbLogsFacade.insertLog("INFO", "Start of request cbEkfgroupDel1csectFromBxFacade", "Complete prev, Start of request cbEkfgroupDel1csectFromBxFacade");
                                                //} catch(Exception lge)  { }
                                                selectExchngDataLog+="Complete prev, Start of request cbEkfgroupDel1csectFromBxFacade ";
                                                dswps_ = cbEkfgroupDel1csectFromBxFacade.findAll();

                                                //try {
                                                //    cbLogsFacade.insertLog("INFO", "Start of request cbEkfgroupUpd1csectToBxFacade", "Complete prev, Start of request cbEkfgroupUpd1csectToBxFacade");
                                                //} catch(Exception lge)  { }
                                                selectExchngDataLog+="Complete prev, Start of request cbEkfgroupUpd1csectToBxFacade ";
                                                uswps_ = cbEkfgroupUpd1csectToBxFacade.findAll();

                                                //try {
                                                //    cbLogsFacade.insertLog("INFO", "Start of request cbEkfgroupAdd1csectToBxFacade", "Complete prev, Start of request cbEkfgroupAdd1csectToBxFacade");
                                                //} catch(Exception lge)  { }
                                                selectExchngDataLog+="Complete prev, Start of request cbEkfgroupAdd1csectToBxFacade ";
                                                nswps_ = cbEkfgroupAdd1csectToBxFacade.findAll();

                                                //try {
                                                //    cbLogsFacade.insertLog("INFO", "Start of request cbEkfroupDelFromBxViewFacade", "Complete prev, Start of request cbEkfroupDelFromBxViewFacade");
                                                //} catch(Exception lge)  { }
                                                selectExchngDataLog+="Complete prev, Start of request cbEkfroupDelFromBxViewFacade ";
                                                dpwps_ = cbEkfroupDelFromBxViewFacade.findAll();

                                                //try {
                                                //    cbLogsFacade.insertLog("INFO", "Start of request CbEkfgroupToUpdatedBx1cFacade", "Complete prev, Start of request CbEkfgroupToUpdatedBx1cFacade");
                                                //} catch(Exception lge)  { }
                                                selectExchngDataLog+="Complete prev, Start of request CbEkfgroupToUpdatedBx1cFacade ";
                                                upwps_ = cbEkfgroupToUpdatedBx1cFacade.findAll();
                                                try {
                                                    cbLogsFacade.insertLog("INFO", "End of request CbEkfgroupToUpdatedBx1cFacade", selectExchngDataLog+" End of request CbEkfgroupToUpdatedBx1cFacade");
                                                } catch(Exception lge)  { }

                                                if(npwps_.size()==0&&upwps_.size()==0&&dpwps_.size()==0)  {
                                                    if (cbSettingsFacade.updExchanheLastDt())    {
                                                        //try {
                                                        //    cbLogsFacade.insertLog("INFO", "Success update exchange last datetime", 
                                                        //            "Success update exchange last datetime");
                                                        //} catch(Exception lge)  {

                                                        //}
                                                    }   else    {
                                                        try {
                                                            cbLogsFacade.insertLog("ERROR", "Unsuccess update exchange last datetime", 
                                                                    "Unsuccess update exchange last datetime");
                                                        } catch(Exception lge)  {

                                                        }
                                                    }
                                                }

                                                if(tablesOperatingStateFacade.setEkfGrExchDataUnCorrupt())   {
                                                    sendCompareSectDataAdd();
                                                }
                                                else    {
                                                    exchangeInProcess = false;
                                                    try {
                                                        cbLogsFacade.insertLog("ERROR", "Unsuccess setEkfGrExchDataCorrupt 0", "Unsuccess setEkfGrExchDataCorrupt 0");
                                                    } catch(Exception lgex)  {

                                                    }

                                                }

                                            }
                                            else    {
                                                exchangeInProcess = false;
                                                try {
                                                    cbLogsFacade.insertLog("ERROR", "Unsuccess setEkfGrExchDataCorrupt 1", "Unsuccess setEkfGrExchDataCorrupt 1");
                                                } catch(Exception lgex)  {

                                                } 
                                            }
                                        }
                                        else    {
                                            exchangeInProcess = false;
                                            try {
                                                cbLogsFacade.insertLog("ERROR", "Invalid data sizes, exchange stopped", "Data sizes mismatch conditions 200<=ekfProdSectCount<=10000, "
                                                        + "1000<=ekfProdCount<=100000");
                                            } catch(Exception lgex)  {

                                            } 
                                        }
                                    }
                                } catch(Exception lge)  {
                                    exchangeInProcess = false;
                                    repeat_load=false;
                                    try {
                                        cbLogsFacade.insertLog("ERROR", "Error of getEkfGrExchDataCorrupt or setEkfGrExchDataCorrupt or MDS data load", "Error of getEkfGrExchDataCorrupt, detail: "+selectExchngDataLog);
                                    } catch(Exception lgex)  {

                                    }  
                                }
                            }

                            
                            if(repeat_load) {
                                hasLong1CWait = true;
                                exchangeInProcess = false;
                            }
                            
                        }
                        else
                            exchangeInProcess = false;

                    } catch(Exception e) {
                        exchangeInProcess = false;
                        System.out.println("<<==!!||||||!!==>>Error of get-parse bx_1cprod json "+e);
                        try {
                            cbLogsFacade.insertLog("ERROR", "Error of get-parse json bx_1cprod", "<<==!!||||||!!==>>Error of get-parse json "+e);
                        } catch(Exception lge)  {
                            
                        }
                    }   finally {
                        try {
                            //tablesOperatingStateFacade.setEkfGrExchDataUnDelta();
                            if(tablesOperatingStateFacade.setEkfGrExchDataUnCorrupt())   {

                            }
                            else    {
                                try {
                                    cbLogsFacade.insertLog("ERROR", "Unsuccess setEkfGrExchDataCorrupt 0", "Unsuccess setEkfGrExchDataCorrupt 0");
                                } catch(Exception lgex)  {

                                } 
                            }
                        } catch(Exception lgex)  {

                        } 
                    }
                }

            };
            
            Thread t3 = new Thread(r3);
            t3.start();
    }

}
