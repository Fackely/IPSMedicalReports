package com.princetonsa.mundo.glosas;

import java.util.ArrayList;
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

import com.princetonsa.actionform.glosas.EdadGlosaXFechaRadicacionForm;
import com.princetonsa.actionform.glosas.ReporteEstadoCarteraGlosasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;

public class EdadGlosaXFechaRadicacion
{
	private static Logger logger = Logger.getLogger(EdadGlosaXFechaRadicacion.class);
	
	public static ArrayList<DtoGlosa> accionBuscarDocs(DtoGlosa dto, boolean radicadas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEdadGlosaXFechaRadicacionDao().accionBuscarDocs(dto, radicadas);
	}
	
	public static void crearCvs (EdadGlosaXFechaRadicacionForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		//arma el nombre del archivo
		String nombreReport="Edad_Glosa_X_Fecha_Radicacion"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
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
	
	public static String plano (EdadGlosaXFechaRadicacionForm forma,UsuarioBasico usuario )
	{
		HashMap tmp = new HashMap();
		String planoStr="";
		
		int convenio=-1;
        String tipoconvenio="-1";
        
        if(!forma.getGlosaBusqueda().getConvenio().getNombre().equals("-1") && !forma.getGlosaBusqueda().getConvenio().getNombre().equals(""))
        	convenio=Utilidades.convertirAEntero(forma.getGlosaBusqueda().getConvenio().getNombre().split(ConstantesBD.separadorSplit)[0]);
        	        
        if(!forma.getGlosaBusqueda().getTipoConvenio().equals("-1") && !forma.getGlosaBusqueda().getTipoConvenio().equals(""))
        	tipoconvenio=forma.getGlosaBusqueda().getTipoConvenio().split(ConstantesBD.separadorSplit)[0];
			
		HashMap criterios = new HashMap();
		
		criterios.put("fecha", forma.getGlosaBusqueda().getFechaRegistroGlosa());
        if(!forma.getGlosaBusqueda().getTipoConvenio().equals("") && !forma.getGlosaBusqueda().getTipoConvenio().equals("-1"))
        	criterios.put("tipoConvenio", forma.getGlosaBusqueda().getTipoConvenio().split(ConstantesBD.separadorSplit)[0]);
        else
        	criterios.put("tipoConvenio", "");
        if(!forma.getGlosaBusqueda().getConvenio().getNombre().equals("") && !forma.getGlosaBusqueda().getConvenio().getNombre().equals("-1"))
        	criterios.put("convenio", forma.getGlosaBusqueda().getConvenio().getNombre().split(ConstantesBD.separadorSplit)[0]);
        else
        	criterios.put("convenio", "");
        
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
        	criterios.put("consulta", "1");
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
        	criterios.put("consulta", "2");
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        	criterios.put("consulta", "3");        
		
		String consulta = ConsultasBirt.EdadGlosaFechaRadicacion(criterios);
		
		 tmp=ReporteEstadoCarteraGlosas.ejecutarQuery(consulta);
		 logger.info("\n\nel resultado: "+tmp);
		 			 		 
		 String filtro1="";
	        
		 boolean existe=false; 
		 
	        // Fecha
	        	filtro1 += " Fecha Corte: "+forma.getGlosaBusqueda().getFechaRegistroGlosa();
	        	existe=true;
	        
	        // Tipo Convenio
	        if(!forma.getGlosaBusqueda().getTipoConvenio().equals("-1"))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Tipo Convenio: "+forma.getGlosaBusqueda().getTipoConvenio().split(ConstantesBD.separadorSplit)[1];
	        }	
	        	
	        // Convenio
	        if(!forma.getGlosaBusqueda().getConvenio().getNombre().equals("-1") && !forma.getGlosaBusqueda().getConvenio().getNombre().equals(""))
	        {
	        	filtro1+= existe?",":"";
	        	filtro1 += "  Convenio: "+forma.getGlosaBusqueda().getConvenio().getNombre().split(ConstantesBD.separadorSplit)[1];
	        }
	        
		  forma.setCriteriosConsulta(filtro1);			  
		  
		  logger.info("\n\ntipo reporte:: "+forma.getTipoReporte());
		  
		  if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
			  planoStr+="Edad Glosa Por Fecha de Radicación\n Resumido"+",";
		  else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
        	  planoStr+="Edad Glosa Por Fecha de Radicación\n Detallado Por Factura"+",";
		  else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        	  planoStr+="Edad Glosa Por Fecha de Radicación\n Detallado Por Cuenta Cobro"+",";      
		 
		  planoStr+="Criterios de Búsqueda:"+",";
		  planoStr+=forma.getCriteriosConsulta()+"\n\n";
		  			  
		  double porRadicar=0, valorEdad1=0, valorEdad2=0, valorEdad3=0, valorEdad4=0, valorEdad5=0, valorEdad6=0, 
		  		 valorEdad7=0, valorEdad8=0, valorEdad9=0, TotalRad=0, TotalGlosaVencida=0;
		  
		  double sumaPorRadicar=0, sumaValorEdad1=0, sumaValorEdad2=0, sumaValorEdad3=0, sumaValorEdad4=0, sumaValorEdad5=0, sumaValorEdad6=0, 
		  		 sumaValorEdad7=0, sumaValorEdad8=0, sumaValorEdad9=0, sumaTotalRad=0, sumaTotalGlosaVencida=0;
		  
		 for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
		 {
			 int j= i-1;
			 int k= i+1;
			 if(!(tmp.get("desctipoconvenio_"+i)+"").equals(tmp.get("desctipoconvenio_"+j)+""))
			 {
				 planoStr+=""+tmp.get("desctipoconvenio_"+i)+"\n\n";
				 if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
					  planoStr+="Convenio,";
				 else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
		        	  planoStr+="Factura,";
				 else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
		        	  planoStr+="Cuenta Cobro,";
				 planoStr+=" Por Radicar, 0., 1 a 30, 31 a 60.," +
			  		" 61 a 90 , 91 a 120, 121 a 150, 151 a 180, 181 a 300, > 300, Total Radicado. Total Glosa Vencida\n";
			 }
			 
			 TotalRad+= valorEdad1 + valorEdad2 + valorEdad3 + valorEdad4 + valorEdad5 + valorEdad6 + valorEdad7 + valorEdad8 + valorEdad9;
			 TotalGlosaVencida+= porRadicar + valorEdad1 + valorEdad2 + valorEdad3 + valorEdad4 + valorEdad5 + valorEdad6 + valorEdad7 + valorEdad8 + valorEdad9;
			 
			 planoStr+=tmp.get("nomconvenio_"+i)+", "+tmp.get("porradicar_"+i)+", "+tmp.get("edadvalor1_"+i)+",  "+tmp.get("edadvalor2_"+i)+"," +
		  		" "+tmp.get("edadvalor3_"+i)+", "+tmp.get("edadvalor4_"+i)+", "+tmp.get("edadvalor5_"+i)+", "+tmp.get("edadvalor6_"+i)+", " +
		  		" "+tmp.get("edadvalor7_"+i)+", "+tmp.get("edadvalor8_"+i)+", "+tmp.get("edadvalor9_"+i)+", "+TotalRad+", "+TotalGlosaVencida+"\n";
			 
			 porRadicar+= Utilidades.convertirADouble(tmp.get("porradicar_"+i)+"");
			 valorEdad1+= Utilidades.convertirADouble(tmp.get("edadvalor1_"+i)+"");
			 valorEdad2+= Utilidades.convertirADouble(tmp.get("edadvalor2_"+i)+"");
			 valorEdad3+= Utilidades.convertirADouble(tmp.get("edadvalor3_"+i)+"");
			 valorEdad4+= Utilidades.convertirADouble(tmp.get("edadvalor4_"+i)+"");
			 valorEdad5+= Utilidades.convertirADouble(tmp.get("edadvalor5_"+i)+"");
			 valorEdad6+= Utilidades.convertirADouble(tmp.get("edadvalor6_"+i)+"");
			 valorEdad7+= Utilidades.convertirADouble(tmp.get("edadvalor7_"+i)+"");
			 valorEdad8+= Utilidades.convertirADouble(tmp.get("edadvalor8_"+i)+"");
			 valorEdad9+= Utilidades.convertirADouble(tmp.get("edadvalor9_"+i)+"");				
			 
			 sumaPorRadicar+=porRadicar;
			 sumaValorEdad1+=valorEdad1;
			 sumaValorEdad2+=valorEdad2;
			 sumaValorEdad3+=valorEdad3;
			 sumaValorEdad4+=valorEdad4;
			 sumaValorEdad5+=valorEdad5;
			 sumaValorEdad6+=valorEdad6;
			 sumaValorEdad7+=valorEdad7;
			 sumaValorEdad8+=valorEdad8;
			 sumaValorEdad9+=valorEdad9;
			 sumaTotalRad+=TotalRad;
			 sumaTotalGlosaVencida+=TotalGlosaVencida;
			 
			 if(!(tmp.get("desctipoconvenio_"+i)+"").equals(tmp.get("desctipoconvenio_"+k)+""))
			 {			 
				 planoStr+="Subtotal, "+porRadicar+", "+valorEdad1+", "+valorEdad2+", "+valorEdad3+"" +
		 			", "+valorEdad4+", "+valorEdad5+", "+valorEdad6+", "+valorEdad7+", "+valorEdad8+", "+valorEdad9+", "+TotalRad+", "+TotalGlosaVencida+"\n\n";
			 }
		 }			  				 
		 
		 planoStr+="TOTAL GENERAL, "+sumaPorRadicar+", "+sumaValorEdad1+", "+sumaValorEdad2+", "+sumaValorEdad3+"" +
	 		", "+sumaValorEdad4+", "+sumaValorEdad5+", "+sumaValorEdad6+", "+sumaValorEdad7+", "+sumaValorEdad8+", "+sumaValorEdad9+" " +
	 		", "+sumaTotalRad+", "+sumaTotalGlosaVencida+"";				 
		 return planoStr;
	}
}