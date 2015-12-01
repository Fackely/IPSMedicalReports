package com.princetonsa.mundo.glosas;

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

import com.princetonsa.actionform.glosas.ReporteFacturasReiteradasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ReporteFacturasReiteradasDao;
import com.princetonsa.mundo.UsuarioBasico;


public class ReporteFacturasReiteradas
{
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ReporteFacturasReiteradas.class);
	
	private static ReporteFacturasReiteradasDao getReporteFacturasReiteradasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteFacturasReiteradasDao();
	}
	
	public static boolean crearCvs (ReporteFacturasReiteradasForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		//arma el nombre del archivo
		String nombreReport="Reporte_Facturas_Reiteradas"+"-" +
				""+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-" +
				""+UtilidadFecha.getHoraActual()+"-"+usuario.getLoginUsuario();
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
	
		return true;
	}
	
	public static String plano (ReporteFacturasReiteradasForm forma,UsuarioBasico usuario )
	{
		HashMap tmp = new HashMap();
		String planoStr="";
		
		int convenio=-1;
        String tipoconvenio="-1";
        
        if(!forma.getConvenio().equals("-1"))
        	convenio=Utilidades.convertirAEntero(forma.getConvenio().split(ConstantesBD.separadorSplit)[0]);
        	        
        if(!forma.getTipoConvenio().equals("-1"))
        	tipoconvenio=forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[0];
		
		String consulta = ConsultasBirt.reporteFacturasReiteradas(
				UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()), 
				tipoconvenio, 
				convenio, 
				usuario.getCodigoInstitucionInt());
		
		 tmp=ReporteEstadoCarteraGlosas.ejecutarQuery(consulta);
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
		  
		  planoStr+="Reporte Facturas Reiteradas"+",";
		  planoStr+="Criterios de B�squeda:"+",";
		  planoStr+=forma.getCriteriosConsulta()+"\n\n";		  			  
		
		  
		 for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
		 {
			 int j= i-1;
			 			 
			 if(i==0)
			 {
				 planoStr+="Factura, Fecha Elab., Fecha Rad.., Convenio, Valor Paciente.," +
				 " Valor Convenio, "+" Total Fact., Glosa, Glosa Entidad, Fecha Notificaci�n, Valor Tot. Glosa, Fecha Rad. Rta.\n\n";
			 }
		  	
			 if (!(tmp.get("desctipoconv_"+i)+"").equals(tmp.get("desctipoconv_"+j)+""))
			 {
				 planoStr+=""+tmp.get("desctipoconv_"+i)+"\n\n";
			  }
			 
			 planoStr+=tmp.get("consecutivofactura_"+i)+", "+tmp.get("fechaelaboracion_"+i)+", "+tmp.get("fecharadicacion_"+i)+",  "+tmp.get("nomconvenio_"+i)+"," +
		  		" "+tmp.get("valorpaciente_"+i)+", "+tmp.get("valorconvenio_"+i)+", "+tmp.get("valortotal_"+i)+", "+tmp.get("glosasis_"+i)+", " +
		  				""+tmp.get("glosaent_"+i)+", "+tmp.get("fechanot_"+i)+", "+tmp.get("valorglosafact_"+i)+", "+tmp.get("fecharesp_"+i)+",\n";
			 
		}		  				 
			
		return planoStr;
	}
}