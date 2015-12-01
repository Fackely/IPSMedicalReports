package com.princetonsa.mundo.glosas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.CsvFile;
import util.TxtFile;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ConsultarImpFacAudiForm;
import com.princetonsa.actionform.manejoPaciente.CensoCamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConsultarImpFacAudiDao;
import com.princetonsa.mundo.UsuarioBasico;
//import com.sun.java.util.jar.pack.Attribute;

public class ConsultarImpFacAudi
{
	/**
	 * Para manejo de Logs
	 */
	
	private static Logger logger = Logger.getLogger(ConsultarImpFacAudi.class);
	
	private static ConsultarImpFacAudiDao getConsultarImpFacAudiDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImpFacAudiDao();
	}
	
	
	/**
	 * Metodo encargado de consultar las facturas auditadas
	 * @param connection
	 * @param consulta --> consulta a realizar
	 * @return HashMap
	 * -----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------
	 * codigoGlosa_,fechaAuditoriaBd_,
	 * fechaAuditoria_,nombreConvenio_,
	 * preGlosa_, valorPreGlosa_,
	 * fechaElaboracionBd_,fechaElaboracion_,
	 * consecutivoFactura_, nombrePaciente_,
	 * valorTotal_
	 */
	public static HashMap consultaFacturasAuditadas (Connection connection, String consulta)
	{
		return getConsultarImpFacAudiDao().consultaFacturasAuditadas(connection, consulta);
	}	
	
	public static String plano (Connection connection, ConsultarImpFacAudiForm forma,UsuarioBasico usuario )
	{
		HashMap tmp = new HashMap();
		String planoStr="";
		
		if(forma.getTipoReporte().equals("ListadoFacturasAuditadas"))
		{
			String consulta = ConsultasBirt.listadoFacturasAuditadas(
						connection, 
						UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAuditoriaInicial()), 
						UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAuditoriaFinal()), 
						forma.getFacturaInicial(), 
						forma.getFacturaFinal(), 
						Utilidades.convertirAEntero(forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[0]), 
						forma.getCodigoContrato(), 
						forma.getNumeroPreGlosa(), 
						usuario.getCodigoInstitucionInt());
			 logger.info("Consulta: "+consulta);
			 tmp=consultaFacturasAuditadas(connection, consulta);
			 Utilidades.imprimirMapa(tmp);
			 int numReg=Utilidades.convertirAEntero(tmp.get("numRegistros")+""); 
			 
			 String encabezado="Factura, Valor Total, Fecha Elab., Fecha Audi., Paciente, Pre-Glosa, Valor Pre-Glosa \n";
			 		 
			 String filtro1="";
		        
			 boolean existe=false; 
			 
		        // Fechas
		        if(!forma.getFechaAuditoriaInicial().equals("") && !forma.getFechaAuditoriaFinal().equals(""))
		        {	
		        	filtro1 += "  Fecha Inicial: "+forma.getFechaAuditoriaInicial()+",  Fecha Final: "+forma.getFechaAuditoriaFinal();
		        	existe=true;
		        }	
		        
		        // Consecutivo Facturas
		        if(!forma.getFacturaInicial().equals("") && !forma.getFacturaFinal().equals(""))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += "  Factura Inicial: "+forma.getFacturaInicial()+",  Factura Final: "+forma.getFacturaFinal();
		        	existe=true;
		        }	
		        	
		        // Convenio
		        if(!forma.getCodigoConvenio().equals(""))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += "  Convenio: "+forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[1];
		        	existe=true;
		        }
		        // Contrato
		        if(forma.getCodigoContrato()!=ConstantesBD.codigoNuncaValido)
		        {
		        	filtro1+= existe?",":"";
		           	filtro1 += " Contrato: "+forma.getCodigoContrato();
		           	existe=true;
		        }       
		        // Pre-Glosa
		        if(!forma.getNumeroPreGlosa().equals(""))
		        {
		        	filtro1+= existe?",":"";
		        	filtro1 += " Pre-Glosa: "+forma.getNumeroPreGlosa();
		        	existe=true;
		        }		 	  
			  forma.setCriteriosConsulta(filtro1);			  
			  
			  planoStr+="Reporte: "+",";
			  planoStr+=forma.getTipoReporte()+"\n";
			  planoStr+="Criterios de Búsqueda:"+",";
			  planoStr+=forma.getCriteriosConsulta()+"\n\n";
			  
			  Set<Integer> listaConvenios= obtenerCodigosConvenios(tmp);
			  
			  double valorTotal=0;
			  for(Integer codConvenio: listaConvenios)
			  {
				  boolean colocoEncabezado=false;
				  double valorXConvenio=0;
				  for (int i=0;i<numReg;i++)		  
				  {
					 
					 if(codConvenio==Utilidades.convertirAEntero(tmp.get("codigoConvenio_"+i)+"") ) 
					 {
						 if(!colocoEncabezado)
						 {	 
							 planoStr+=tmp.get("nombreConvenio_"+i)+"\n";
							 planoStr+=encabezado;
							 planoStr+= Utilidades.convertirAEntero(tmp.get("estadofactura_"+i)+"")==ConstantesBD.codigoEstadoFacturacionAnulada?" (ANULADA) ":"";
							 
							 BigDecimal a = new BigDecimal(tmp.get("valorTotal_"+i)+"");
							 logger.info("Valor double: "+a.doubleValue());
							 logger.info("String: "+a.toString());
							 logger.info("float: "+a.floatValue());
							 logger.info("Plain String: "+a.toPlainString());
							 logger.info("Plain String 1: "+a.longValueExact());
							 
							 planoStr+=tmp.get("consecutivoFactura_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tmp.get("valorTotal_"+i)+""))+","+tmp.get("fechaElaboracion_"+i)+","+tmp.get("fechaAuditoria_"+i)+","+tmp.get("nombrePaciente_"+i)+","+tmp.get("preGlosa_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tmp.get("valorPreGlosa_"+i)+""))+"\n";
							 colocoEncabezado=true;
						 }
						 else
						 {
							 planoStr+= Utilidades.convertirAEntero(tmp.get("estadofactura_"+i)+"")==ConstantesBD.codigoEstadoFacturacionAnulada?" (ANULADA) ":"";
							 planoStr+=tmp.get("consecutivoFactura_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tmp.get("valorTotal_"+i)+"")) +","+tmp.get("fechaElaboracion_"+i)+","+tmp.get("fechaAuditoria_"+i)+","+tmp.get("nombrePaciente_"+i)+","+tmp.get("preGlosa_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tmp.get("valorPreGlosa_"+i)+""))+"\n";
						 }
						 valorXConvenio+= Utilidades.convertirAEntero(tmp.get("estadofactura_"+i)+"")!=ConstantesBD.codigoEstadoFacturacionAnulada?Utilidades.convertirADouble(tmp.get("valorPreGlosa_"+i)+""):0;
					 }
					 
					 if(i==(numReg-1))
					 {
						 planoStr+="Total Pre-Glosa x Convenio "+UtilidadTexto.formatearExponenciales(valorXConvenio)+" \n";
						 valorTotal+=valorXConvenio;
					 }
				  }	 	  			  
			  }
			  planoStr+="Total Pre-Glosa "+UtilidadTexto.formatearExponenciales(valorTotal)+"";
		}
		else if(forma.getTipoReporte().equals("ImpresionGlosaFactura"))
		{
			String consulta = ConsultasBirt.ImpresionGlosaFactura(
					connection, 
					forma.getCodFactura(), 
					usuario.getCodigoInstitucionInt(), forma.getCodigoGlosa());
		 
			 tmp=consultarFactura(connection, forma.getCodFactura());
			 			 			 			 
			 String encabezado="Solicitud, Servicio/Articulo, Cantidad, Valor, Canti. Pre- GLosa, Valor Pre- Glosa, Concepto \n";
			 forma.setCriteriosConsulta(" Codigo Factura: "+forma.getCodFactura());
			 
			 planoStr+="Reporte: ";				
			 planoStr+=forma.getTipoReporte()+"\n";	
			 planoStr+="Criterios de Búsqueda: ";				
			 planoStr+=forma.getCriteriosConsulta()+"\n\n";	
			 				 		 		  
			 for (int i=0;i<Utilidades.convertirAEntero(forma.getConsultaDetFacMap("numRegistros")+"");i++)
			 {
				 if (i==0)
				  {
					planoStr+="Convenio: ";				
					planoStr+=tmp.get("nomconvenio_"+i)+",";
					planoStr+="Pre- GLosa: ";
					planoStr+=tmp.get("preglosa_"+i)+",";
					planoStr+="Valor Total Pre- GLosa: ";
					planoStr+=tmp.get("valorglosa_"+i)+",";
					planoStr+="Estado: ";
					planoStr+=tmp.get("estado_"+i)+",";
					planoStr+="Factura: ";
					planoStr+=tmp.get("codfactura_"+i)+",";
					planoStr+="Valor Pre- Glosa Factura: "+",";
					planoStr+=tmp.get("valorglosafactura_"+i)+"\n\n";
					planoStr+=encabezado;
					planoStr+=tmp.get("solicitud_"+i)+","+tmp.get("servicio_"+i)+","+tmp.get("cantidadsol_"+i)+","+tmp.get("valorfactura_"+i)+","+tmp.get("cantidadglosa_"+i)+","+tmp.get("valorglosa_"+i)+","+tmp.get("concepto_"+i)+"\n\n";

				  }
				  else
					  planoStr+=tmp.get("solicitud_"+i)+","+tmp.get("servicio_"+i)+","+tmp.get("cantidadsol_"+i)+","+tmp.get("valorfactura_"+i)+","+tmp.get("cantidadglosa_"+i)+","+tmp.get("valorglosa_"+i)+","+tmp.get("concepto_"+i)+"\n";
			 }			  
			 planoStr+="Valor Total: "+","+tmp.get("valorglosafactura_"+0)+"\n";
		}			  
		return planoStr;
	}
	
	
	/**
	 * 
	 * @param forma
	 * @return
	 */
	private static Set<Integer> obtenerCodigosConvenios(
			HashMap mapa) 
	{
		HashSet<Integer> set= new HashSet<Integer>();
		for (int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
		{
			set.add(Utilidades.convertirAEntero(mapa.get("codigoConvenio_"+i)+""));
		}
		return set;
	}


	public static void crearCvs (Connection connection, ConsultarImpFacAudiForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		if(forma.getTipoReporte().equals("ListadoFacturasAuditadas"))
		{		
			//arma el nombre del archivo
			String nombreReport="Listado_Facturas_Auditadas"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
			//se genera el documento con la informacion
		
			String path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
			String url="../upload"+System.getProperty("file.separator");
			String txt = plano(connection, forma, usuario);
				 	
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
		else if(forma.getTipoReporte().equals("ImpresionGlosaFactura"))
		{
			//arma el nombre del archivo
			String nombreReport="Impresion_Glosa_Factura"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
			//se genera el documento con la informacion
			String path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
			String url="../upload"+System.getProperty("file.separator");
			String txt = plano(connection, forma, usuario);
				 	
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
	
	/**
	 * Metodo que actualiza el Log de consulta
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap criterios)
	{
		return getConsultarImpFacAudiDao().guardar(con, criterios);
	}
	
	/**
	 * Metodo que consulta detalles de factura
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarFactura(Connection con, String consFactura)
	{
		return getConsultarImpFacAudiDao().consultarFactura(con, consFactura);
	}
}