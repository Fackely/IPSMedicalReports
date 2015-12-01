package com.princetonsa.mundo.glosas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.TxtFile;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ConsultarImpFacAudiForm;
import com.princetonsa.actionform.glosas.ReporteEstadoCarteraGlosasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConsultarImpFacAudiDao;
import com.princetonsa.dao.glosas.ReporteEstadoCarteraGlosasDao;
import com.princetonsa.mundo.UsuarioBasico;


public class ReporteEstadoCarteraGlosas
{
	/**
	 * Para manejo de Logs
	 */
	
	private static Logger logger = Logger.getLogger(ReporteEstadoCarteraGlosas.class);
	
	private static ReporteEstadoCarteraGlosasDao getReporteEstadoCarteraGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteEstadoCarteraGlosasDao();
	}
	
	public static HashMap ejecutarQuery(String consulta)
	{
		return getReporteEstadoCarteraGlosasDao().ejecutarQuery(consulta);
	}
	
	public static void crearCvs (ReporteEstadoCarteraGlosasForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
		{	
			//arma el nombre del archivo
			String nombreReport="Reporte_Estado_Cartera_Y_Glosas_Resumido"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
			//se genera el documento con la informacion
		
			String path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
			String url="../upload"+System.getProperty("file.separator");
			String txt = plano(forma, usuario);
			
			if (UtilidadCadena.noEsVacio(txt))
					OperacionTrue=TxtFile.generarTxt(new StringBuffer(txt) , nombreReport,path,".csv");
			
				if (OperacionTrue)
				{
					//se genera el archivo en formato Zip
					ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombreReport+".zip -j"+" "+path+nombreReport+".csv");
					//se ingresa la direccion donde se almaceno el archivo
					forma.setRuta(path+nombreReport+".csv");
					//se ingresa la ruta para poder descargar el archivo
					forma.setUrlArchivo(url+nombreReport+".zip");	
					//se valida si existe el txt
					existeTxt=UtilidadFileUpload.existeArchivo(path, nombreReport+".csv");
					//se valida si existe el zip
					forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(path, nombreReport+".zip"));
				}
				if (existeTxt )
					forma.setOperacionTrue(true);				
		}
		else if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 2)
		{
			//arma el nombre del archivo
			String nombreReport="Reporte_Estado_Cartera_Y_Glosas_Por_Factura"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
			//se genera el documento con la informacion
			String path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
			String url="../upload"+System.getProperty("file.separator");
			String txt = plano(forma, usuario);
				 	
			if (UtilidadCadena.noEsVacio(txt))
					OperacionTrue=TxtFile.generarTxt(new StringBuffer(txt) , nombreReport,path,".csv");
			
				if (OperacionTrue)
				{
					//se genera el archivo en formato Zip
					ban=BackUpBaseDatos.EjecutarComandoSO("zip  -j "+path+nombreReport+".zip"+" "+path+nombreReport+".csv");
					//se ingresa la direccion donde se almaceno el archivo
					forma.setRuta(path+nombreReport+".csv");
					//se ingresa la ruta para poder descargar el archivo
					forma.setUrlArchivo(url+nombreReport+".zip");	
					//se valida si existe el txt
					existeTxt=UtilidadFileUpload.existeArchivo(path, nombreReport+".csv");
					//se valida si existe el zip
					forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(path, nombreReport+".zip"));
				}
				if (existeTxt )
					forma.setOperacionTrue(true);
		}
	}	
	
	public static String plano (ReporteEstadoCarteraGlosasForm forma,UsuarioBasico usuario )
	{
		HashMap tmp = new HashMap();
		String planoStr="";
		
		int convenio=-1;
        String tipoconvenio="-1";
        
        if(!forma.getConvenio().equals("-1"))
        	convenio=Utilidades.convertirAEntero(forma.getConvenio().split(ConstantesBD.separadorSplit)[0]);
        	        
        if(!forma.getTipoConvenio().equals("-1"))
        	tipoconvenio=forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[0];
		
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
		{		
			String consulta = ConsultasBirt.reporteEstadoCarteraYGlosas(
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()), 
					tipoconvenio, 
					convenio, 
					usuario.getCodigoInstitucionInt(),1);
			
			 tmp=ejecutarQuery(consulta);
			 logger.info("\n\nel resultado: "+tmp);
			 			 		 
			 String filtro1="";
		        
			 boolean existe=false; 
			 
		        // Fecha
		        	filtro1 += " Fecha Corte: "+forma.getFechaCorte();
		        	existe=true;
		        
		        // Tipo Convenio
		        if(!forma.getTipoConvenio().equals("-1"))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += "  Tipo Convenio: "+forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[1];
		        }	
		        	
		        // Convenio
		        if(!forma.getConvenio().equals("-1") && !forma.getConvenio().equals(""))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += "  Convenio: "+forma.getConvenio().split(ConstantesBD.separadorSplit)[1];
		        }
		        
			  forma.setCriteriosConsulta(filtro1);			  
			  
			  planoStr+="Reporte Estado Cartera y Glosas, Resumido"+",";
			  planoStr+="Criterios de Búsqueda:"+",";
			  planoStr+=forma.getCriteriosConsulta()+"\n\n";
			  			  
			  double valorTotalCarteraObj=0;
			  double valorTotalGlosaSop=0;			  
			  double valorTotalCorriente=0;			  
			  double valorTotalFactPend=0;			  
			  double valorTotalGlosaPend=0;			  
			  double valorTotalNoCorriente=0;			  
			  double valorTotalSaldoCartera=0;			  
			  double SumaCarteraObj=0;
			  double SumaGlosaSop=0;
			  double SumaCorriente=0;
			  double SumaFactPend=0;
			  double SumaGlosaPend=0;
			  double SumaNoCorriente=0;
			  double SumaSaldoCartera=0;
			  
				 for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
				 {
					 int j= i-1;
					 if(!(tmp.get("desctipoconv_"+i)+"").equals(tmp.get("desctipoconv_"+j)+""))
					 {
						 planoStr+="Tipo Convenio:\n";
						 planoStr+=""+tmp.get("desctipoconv_"+i)+"\n\n";
					 }
					 planoStr+="Convenio, Cartera Sin Objeción, Glosas Soportadas por Rad., Total Cartera Corriente, Facturas Pendientes de Rad.," +
				  		" Glosas Pendientes por Responder: "+", Total Cartera no Corriente, Saldo Cartera\n";
					 planoStr+=tmp.get("nomconv_"+i)+", "+tmp.get("carteraobj_"+i)+", "+tmp.get("glosasop_"+i)+",  "+tmp.get("totalcorriente_"+i)+"," +
				  		" "+tmp.get("factpend_"+i)+", "+tmp.get("glosapend_"+i)+", "+tmp.get("totalnocorriente_"+i)+", "+tmp.get("saldocartera_"+i)+"\n";
					 
					 valorTotalCarteraObj+= Utilidades.convertirADouble(tmp.get("carteraobj_"+i)+"");
					 valorTotalGlosaSop+= Utilidades.convertirADouble(tmp.get("glosasop_"+i)+"");
					 valorTotalCorriente+= Utilidades.convertirADouble(tmp.get("totalcorriente_"+i)+"");
					 valorTotalFactPend+= Utilidades.convertirADouble(tmp.get("factpend_"+i)+"");
					 valorTotalGlosaPend+= Utilidades.convertirADouble(tmp.get("glosapend_"+i)+"");
					 valorTotalNoCorriente+= Utilidades.convertirADouble(tmp.get("totalnocorriente_"+i)+"");
					 valorTotalSaldoCartera+= Utilidades.convertirADouble(tmp.get("saldocartera_"+i)+"");
					 
					 SumaCarteraObj+=valorTotalCarteraObj;
					 SumaGlosaSop+=valorTotalGlosaSop;
					 SumaCorriente+=valorTotalCorriente;
					 SumaFactPend+=valorTotalFactPend;
					 SumaGlosaPend+=valorTotalGlosaPend;
					 SumaNoCorriente+=valorTotalNoCorriente;
					 SumaSaldoCartera+=valorTotalSaldoCartera;
					 
					 planoStr+="Subtotal, "+valorTotalCarteraObj+", "+valorTotalGlosaSop+", "+valorTotalCorriente+", "+valorTotalFactPend+"" +
				 		", "+valorTotalGlosaPend+", "+valorTotalNoCorriente+", "+valorTotalSaldoCartera+"\n\n";
				 }			  				 
				 
				 planoStr+="TOTAL GENERAL, "+SumaCarteraObj+", "+SumaGlosaSop+", "+SumaCorriente+", "+SumaFactPend+"" +
			 		", "+SumaGlosaPend+", "+SumaNoCorriente+", "+SumaSaldoCartera+" ";
				 
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
		{
			String consulta = ConsultasBirt.reporteEstadoCarteraYGlosas(
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()), 
					tipoconvenio, 
					convenio, 
					usuario.getCodigoInstitucionInt(),2);
			
			 tmp=ejecutarQuery(consulta);
			 logger.info("\n\nel resultado: "+tmp);
			 			 		 
			 String filtro1="";
		        
			 boolean existe=false; 
			 
		        // Fecha
		        	filtro1 += " Fecha Corte: "+forma.getFechaCorte();
		        	existe=true;
		        
		        // Tipo Convenio
		        if(!forma.getTipoConvenio().equals("-1"))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += "  Tipo Convenio: "+forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[1];
		        }	
		        	
		        // Convenio
		        if(!forma.getConvenio().equals("-1") && !forma.getConvenio().equals(""))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += "  Convenio: "+forma.getConvenio().split(ConstantesBD.separadorSplit)[1];
		        }
		        
			  forma.setCriteriosConsulta(filtro1);			  
			  
			  planoStr+="Reporte Estado Cartera y Glosas, Detallado Por Factura "+",";
			  planoStr+="Criterios de Búsqueda:"+",";
			  planoStr+=forma.getCriteriosConsulta()+"\n\n";
			  			  
			  double valorTotalCarteraObj=0;
			  double valorTotalGlosaSop=0;			  
			  double valorTotalCorriente=0;			  
			  double valorTotalFactPend=0;			  
			  double valorTotalGlosaPend=0;			  
			  double valorTotalNoCorriente=0;			  
			  double valorTotalSaldoCartera=0;		
			  
			  double valorTCCarteraObj=0;
			  double valorTCGlosaSop=0;			  
			  double valorTCCorriente=0;			  
			  double valorTCFactPend=0;			  
			  double valorTCGlosaPend=0;			  
			  double valorTCNoCorriente=0;			  
			  double valorTCSaldoCartera=0;	
			  
			  double SumaCarteraObj=0;
			  double SumaGlosaSop=0;
			  double SumaCorriente=0;
			  double SumaFactPend=0;
			  double SumaGlosaPend=0;
			  double SumaNoCorriente=0;
			  double SumaSaldoCartera=0;
			  
				 for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
				 {
					 int j=i-1;
					 int k=i+1;
					 if (!(tmp.get("desctipoconv_"+i)+"").equals(tmp.get("desctipoconv_"+j)+""))
					 {
						 planoStr+="Tipo Convenio:\n";
						 planoStr+=""+tmp.get("desctipoconv_"+i)+"\n\n";
					  }
					 if(!(tmp.get("nomconv_"+i)+"").equals(tmp.get("nomconv_"+j)+""))
					 {						 
						 planoStr+="Convenio:\n";
						 planoStr+=""+tmp.get("nomconv_"+i)+"\n\n";
					 }
					 planoStr+="Factura, Cartera Sin Objeción, Glosas Soportadas por Rad., Total Cartera Corriente, Facturas Pendientes de Rad.," +
				  		" Glosas Pendientes por Responder: "+", Total Cartera no Corriente, Saldo Cartera\n";
					 planoStr+=tmp.get("factura_"+i)+", "+tmp.get("carteraobj_"+i)+", "+tmp.get("glosasop_"+i)+",  "+tmp.get("totalcorriente_"+i)+"," +
				  		" "+tmp.get("factpend_"+i)+", "+tmp.get("glosapend_"+i)+", "+tmp.get("totalnocorriente_"+i)+", "+tmp.get("saldocartera_"+i)+"\n";
				
					 valorTotalCarteraObj+= Utilidades.convertirADouble(tmp.get("carteraobj_"+i)+"");
					 valorTotalGlosaSop+= Utilidades.convertirADouble(tmp.get("glosasop_"+i)+"");
					 valorTotalCorriente+= Utilidades.convertirADouble(tmp.get("totalcorriente_"+i)+"");
					 valorTotalFactPend+= Utilidades.convertirADouble(tmp.get("factpend_"+i)+"");
					 valorTotalGlosaPend+= Utilidades.convertirADouble(tmp.get("glosapend_"+i)+"");
					 valorTotalNoCorriente+= Utilidades.convertirADouble(tmp.get("totalnocorriente_"+i)+"");
					 valorTotalSaldoCartera+= Utilidades.convertirADouble(tmp.get("saldocartera_"+i)+"");
					 
					 SumaCarteraObj+=valorTotalCarteraObj;
					 SumaGlosaSop+=valorTotalGlosaSop;
					 SumaCorriente+=valorTotalCorriente;
					 SumaFactPend+=valorTotalFactPend;
					 SumaGlosaPend+=valorTotalGlosaPend;
					 SumaNoCorriente+=valorTotalNoCorriente;
					 SumaSaldoCartera+=valorTotalSaldoCartera;	
					 
					 if(!(tmp.get("nomconv_"+i)+"").equals(tmp.get("nomconv_"+k)+""))
					 {					
						 valorTCCarteraObj+= valorTotalCarteraObj;
						 valorTCGlosaSop+= valorTotalGlosaSop;
						 valorTCCorriente+= valorTotalCorriente;
						 valorTCFactPend+= valorTotalFactPend;
						 valorTCGlosaPend+= valorTotalGlosaPend;
						 valorTCNoCorriente+= valorTotalNoCorriente;
						 valorTCSaldoCartera+= valorTotalSaldoCartera;
						 planoStr+="Subtotal Convenio, "+valorTotalCarteraObj+", "+valorTotalGlosaSop+", "+valorTotalCorriente+", "+valorTotalFactPend+"" +
					 		", "+valorTotalGlosaPend+", "+valorTotalNoCorriente+", "+valorTotalSaldoCartera+"\n\n";
					 }
					 if (!(tmp.get("desctipoconv_"+i)+"").equals(tmp.get("desctipoconv_"+k)+""))
					 {
						 planoStr+="Subtotal, "+valorTCCarteraObj+", "+valorTCGlosaSop+", "+valorTCCorriente+", "+valorTCFactPend+"" +
					 		", "+valorTCGlosaPend+", "+valorTCNoCorriente+", "+valorTCSaldoCartera+"\n\n";
					 }
				 }			  				 
				 
				
				 
				 planoStr+="TOTAL GENERAL, "+SumaCarteraObj+", "+SumaGlosaSop+", "+SumaCorriente+", "+SumaFactPend+"" +
			 		", "+SumaGlosaPend+", "+SumaNoCorriente+", "+SumaSaldoCartera+" ";
		}			  
		return planoStr;
	}
}