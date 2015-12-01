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

import com.princetonsa.actionform.glosas.ReporteEstadoCarteraGlosasForm;
import com.princetonsa.actionform.glosas.ReporteFacturasVencidasNoObjetadasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ReporteEstadoCarteraGlosasDao;
import com.princetonsa.dao.glosas.ReporteFacturasVencidasNoObjetadasDao;
import com.princetonsa.mundo.UsuarioBasico;

public class ReporteFacturasVencidasNoObjetadas
{
	/**
	 * Para manejo de Logs
	 */	
	private static Logger logger = Logger.getLogger(ReporteFacturasVencidasNoObjetadas.class);
	
	private static ReporteFacturasVencidasNoObjetadasDao getReporteFacturasVencidasNoObjetadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteFacturasVencidasNoObjetadasDao();
	}
	
	public static void crearCvs (ReporteFacturasVencidasNoObjetadasForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		//arma el nombre del archivo
		String nombreReport="Reporte_Facturas_Vencidas_No_Objetadas"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-" +
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
	}	
	
	public static String plano (ReporteFacturasVencidasNoObjetadasForm forma,UsuarioBasico usuario )
	{
		HashMap tmp = new HashMap();
		String planoStr="";
		
		int convenio=-1;
        String tipoconvenio="-1";
        
        if(!forma.getConvenio().equals("-1"))
        	convenio=Utilidades.convertirAEntero(forma.getConvenio().split(ConstantesBD.separadorSplit)[0]);
        	        
        if(!forma.getTipoConvenio().equals("-1"))
        	tipoconvenio=forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[0];
		
        String parametro="";
        
        parametro=ValoresPorDefecto.getNumeroDiasNotificarGlosa(usuario.getCodigoInstitucionInt());
        
        HashMap criterios = new HashMap();
        
        criterios.put("convenio", convenio);
        criterios.put("tipoConvenio", tipoconvenio);
        criterios.put("fecha", forma.getFechaCorte());
        criterios.put("parametro", parametro);
				
        String consulta = ConsultasBirt.ReporteFacturasVencidasNoObjetadas(criterios);
	
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
	  
	  planoStr+="Reporte Facturas Vencidas No Objetadas, ";
	  planoStr+="Criterios de Busqueda:"+",";
	  planoStr+=forma.getCriteriosConsulta()+"\n\n";
	  			  
	  double subtotal=0;
	  
		 for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
		 {
			 int j= i-1;
			 int k= i+1;
			 if(!(tmp.get("tipoconvenio_"+i)+"").equals(tmp.get("tipoconvenio_"+j)+""))
			 {
				 planoStr+="Tipo Convenio:\n";
				 planoStr+=""+tmp.get("tipoconvenio_"+i)+"\n\n";
			 }
			 planoStr+="Factura, Fecha Elab., Fecha Rad., Convenio, Paciente," +
		  		" Valor Convenio , Valor Paciente, Saldo Factura\n";
			 planoStr+=tmp.get("consecutivofact_"+i)+", "+tmp.get("fechaelabfact_"+i)+", "+tmp.get("fecharadcc_"+i)+",  "+tmp.get("nomconvenio_"+i)+"," +
		  		" "+tmp.get("nompaciente_"+i)+", "+tmp.get("valorconv_"+i)+", "+tmp.get("valorbrutopac_"+i)+", "+tmp.get("saldofact_"+i)+"\n";
		
			 subtotal += Utilidades.convertirADouble( tmp.get("saldofact_"+i)+"");
			 
			 if(!(tmp.get("tipoconvenio_"+i)+"").equals(tmp.get("tipoconvenio_"+k)+""))
				 planoStr+="Subtotal, "+subtotal+"\n\n";
		 }
		 
		return planoStr;
	}
}