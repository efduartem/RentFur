/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.report;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import rentfur.util.DbConnectUtil;

/**
 *
 * @author FDuarte
 */
public class ReportController {
    
    public static void getReceipt(int receiptId){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/Receipt.jasper"));
            //ReportController.class.getResource("")
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("receiptId", receiptId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("receipt_"+receiptId+".pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "receipt_"+receiptId+".pdf"; 
            try{ 
              //definiendo la ruta en la propiedad file
              Runtime.getRuntime().exec("cmd /c start "+fileLocal);

            }catch(IOException e){
                e.printStackTrace();
            } 
            
        } catch (JRException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
