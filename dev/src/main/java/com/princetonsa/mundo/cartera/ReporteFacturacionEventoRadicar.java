package com.princetonsa.mundo.cartera;

import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.TxtFile;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.cartera.ReporteFacturacionEventoRadicarForm;
import com.princetonsa.actionform.glosas.ReporteEstadoCarteraGlosasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cartera.ReporteFacturacionEventoRadicarDao;
import com.princetonsa.dao.glosas.ReporteFacturasReiteradasDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ReporteEstadoCarteraGlosas;


public class ReporteFacturacionEventoRadicar
{
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ReporteFacturacionEventoRadicar.class);
	
	private static ReporteFacturacionEventoRadicarDao getReporteFacturacionEventoRadicarDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteFacturacionEventoRadicarDao();
	}
	
	public static void crearCvs (ReporteFacturacionEventoRadicarForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		//arma el nombre del archivo
		String nombreReport="Reporte_Facturacion_Evento_Radicar"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
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
	
	public static String plano (ReporteFacturacionEventoRadicarForm forma,UsuarioBasico usuario )
	{
		HashMap tmp = new HashMap();
		String planoStr="";
		
		String convenio="-1", centroAtencion="-1", viaIngreso="-1",fechaIni="",fechaFin="",factIni="",factFin="";
		        
        if(!forma.getConvenio().equals("-1"))
        	convenio=forma.getConvenio().split(ConstantesBD.separadorSplit)[0];

        if(!forma.getCentroAtencion().equals("-1"))
	        centroAtencion= forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0];

        if(!forma.getFechaElabIni().equals(""))
        	fechaIni= forma.getFechaElabIni();	          

        if(!forma.getFechaElabFin().equals(""))
        	fechaFin= forma.getFechaElabFin();

        if(!forma.getFactIni().equals(""))
        	factIni= "  Facura Inicial: "+forma.getFactIni();	          

        if(!forma.getFactFin().equals(""))
        	factFin= "  Factura Final: "+forma.getFactFin();
        
        if(!forma.getViaIngreso().equals("-1"))
        	viaIngreso= "  Via Ingreso: "+forma.getViaIngreso().split(ConstantesBD.separadorSplit)[1];
		
        String consulta="";
                
        if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 1)
		{	
        	consulta = ConsultasBirt.reporteFacturacionEventoRadicar(centroAtencion, 
						convenio, fechaIni, fechaFin, factIni, factFin, viaIngreso, 1);
		}
        else if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 2)
        {
        	consulta = ConsultasBirt.reporteFacturacionEventoRadicar(centroAtencion, 
					convenio, fechaIni, fechaFin, factIni, factFin, viaIngreso, 2);
        }	
         logger.info("\n\nconsultaaaa en el mundo: "+consulta);
		 tmp=ReporteEstadoCarteraGlosas.ejecutarQuery(consulta);
		 logger.info("\n\nel resultado: "+tmp);
		 			 		 
		 String filtro1="";
	        
		 boolean existe=false; 
		 
		  // Centro Atencion
	        if(!forma.getCentroAtencion().equals("-1"))
	        {
	        	filtro1 += "  Centro Atención: "+forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[1];
	        }
	       
	        // Convenio
	        if(!forma.getConvenio().equals("-1"))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += " Convenio: "+forma.getConvenio().split(ConstantesBD.separadorSplit)[1];
	        }
	        	
	        // Fecha Elab Inicial
	        if(!forma.getFechaElabIni().equals(""))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Fecha Elaboración Inicial: "+forma.getFechaElabIni();
	        }

	        // Fecha Elab Final
	        if(!forma.getFechaElabFin().equals(""))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Fecha Elaboración Final: "+forma.getFechaElabFin();
	        }

	        // Factura Inicial
	        if(!forma.getFactIni().equals(""))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Facura Inicial: "+forma.getFactIni();	
	        }

	        // Factura Final
	        if(!forma.getFactFin().equals(""))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Factura Final: "+forma.getFactFin();
	        }
	        
	        // Via Ingreso
	        if(!forma.getViaIngreso().equals("-1"))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Via Ingreso: "+forma.getViaIngreso().split(ConstantesBD.separadorSplit)[1];
	        }	 
	       
	        
		  forma.setCriteriosConsulta(filtro1);			  
		  
		  planoStr+="Reporte Facturacion Evento Radicar"+",";
		  planoStr+="Criterios de Búsqueda:"+",";
		  planoStr+=forma.getCriteriosConsulta()+"\n\n";
					  
		 for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
		 {			 	
			 if(i == 0)
			 {
				 planoStr+="Factura, Fecha Elab., Convenio, Via Ing., C. Atención," +
			  		" Tipo y No. ID, Paciente, Valor Total, Valor Convenio";
				 				 
				 if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 2)
					 planoStr+= ", Cta Cobro, Días sin Radicar";
			 }
			 
			 planoStr+="\n"+tmp.get("consecutivofactura_"+i)+", " +
			 		""+tmp.get("fechaelaboracion_"+i)+", " +
	 				""+tmp.get("nomconvenio_"+i)+",  " +
					""+tmp.get("idviaingreso_"+i)+", " +
					""+tmp.get("centroatencion_"+i)+", " +
					""+tmp.get("idpaciente_"+i)+", " +
					""+tmp.get("nombrepaciente_"+i)+", " +
					""+tmp.get("valortotalfact_"+i)+", " +
					""+tmp.get("valorconvfact_"+i)+"";
			 
			 if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 2)
				 planoStr+=","+tmp.get("consecutivofactura_"+i)+", "+
				 		""+tmp.get("consecutivofactura_"+i)+", ";
		 }	
		
		return planoStr;
	}
}