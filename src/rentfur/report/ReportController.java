/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.report;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import rentfur.util.DbConnectUtil;
import rentfur.util.NumberToLetterConverter;

/**
 *
 * @author FDuarte
 */
public class ReportController {
    private EventsTotalByDate eventsTotalByDate;
    private PendingEventsByDate pendingEventsByDate;
    private SubjectAccountStatusReport subjectAccountStatusReport;
    
    
    public EventsTotalByDate getEventsTotalByDateView(){
        if(eventsTotalByDate == null){
            eventsTotalByDate = new EventsTotalByDate(this);
        }
        return eventsTotalByDate;
    }
    
    public PendingEventsByDate getPendingEventsByDateView(){
        if(pendingEventsByDate == null){
            pendingEventsByDate = new PendingEventsByDate(this);
        }
        return pendingEventsByDate;
    }
    
    public SubjectAccountStatusReport getSubjectAccountStatusReportView(){
        if(subjectAccountStatusReport == null){
            subjectAccountStatusReport = new SubjectAccountStatusReport(this);
        }
        return subjectAccountStatusReport;
    }
    
    public void eventsTotalByDateViewClosed(){
        eventsTotalByDate = null;
    }
    
    public void pendingEventsByDateViewClosed(){
        pendingEventsByDate = null;
    }
    
    public void subjectAccountStatusReportViewClosed(){
        subjectAccountStatusReport = null;
    }
    
    public void executeEventsTotalByDateReport(Date initDate, Date endDate){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/EventosResumido.jasper"));
            //ReportController.class.getResource("")
            Locale locale = new Locale("es", "PY");
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("fecha_ini", initDate);
            params.put("fecha_fin", endDate);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("ResumenDeEventos.pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "ResumenDeEventos.pdf"; 
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
    
    public void executePendingEventsByDateReport(Date endDate){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/EventosPendientes.jasper"));
            //ReportController.class.getResource("")
            Locale locale = new Locale("es", "PY");
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("fecha_ini", new Date());
            params.put("fecha_fin", endDate);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("ResumenEventosPendientes.pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "ResumenEventosPendientes.pdf"; 
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
    
    public void executeSubjectAccountStatusReport(Date initDate, Date endDate, String subjectCode){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/AccountStatus.jasper"));
            //ReportController.class.getResource("")
            Locale locale = new Locale("es", "PY");
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("fecha_ini", initDate);
            params.put("fecha_fin", endDate);
            params.put("subjectCode", subjectCode);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("EstadoDeCuenta_cliente_"+subjectCode+".pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "EstadoDeCuenta_cliente_"+subjectCode+".pdf"; 
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
    
    public static void getReceiptReport(int receiptId){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/Receipt.jasper"));
            Locale locale = new Locale("es", "PY");
            //ReportController.class.getResource("")
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("receiptId", receiptId);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, params, connRentFur);
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
    
    public static void getInvoiceReport(int invoiceId, double netTotal){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/Invoice.jasper"));
            Locale locale = new Locale("es", "PY");
            String netTotalString = NumberToLetterConverter.convertNumberToLetter(netTotal);
            //ReportController.class.getResource("")
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("invoiceId", invoiceId);
            params.put("netTotalString", netTotalString);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, params, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("invoice_"+invoiceId+".pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "invoice_"+invoiceId+".pdf"; 
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
    
    public static void getContractReport(int eventId){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/Contract.jasper"));
            Locale locale = new Locale("es", "PY");
            
            //ReportController.class.getResource("")
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("eventId", eventId);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, params, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("contract_event_"+eventId+".pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "contract_event_"+eventId+".pdf"; 
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
    
    public static void getContractAnnexedReport(int eventId){
        try {
            Connection connRentFur = null;
            connRentFur = DbConnectUtil.getConnection();
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("Receipt.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(ReportController.class.getResource("/rentfur/jasper/templates/Annexed.jasper"));
            Locale locale = new Locale("es", "PY");
            
            //ReportController.class.getResource("")
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("eventId", eventId);
            params.put(JRParameter.REPORT_LOCALE, locale);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, params, connRentFur);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("contract_annexed_event_"+eventId+".pdf"));
            exporter.exportReport();
            
            //String file = ReportController.class.getResource("/rentfur/reportePDF.pdf").getPath(); 
            String fileLocal = "contract_annexed_event_"+eventId+".pdf"; 
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
