package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.rmi.CORBA.UtilDelegate;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.TxtFile;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.glosas.ConsultarImpFacAudiForm;
import com.princetonsa.actionform.glosas.ConsultarImprimirGlosasSinRespuestaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.RevisionCuentaDao;
import com.princetonsa.dao.glosas.ConsultarImprimirGlosasSinRespuestaDao;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;


public class ConsultarImprimirGlosasSinRespuesta {
	
	/**
	 * Instancia el Dao
	 * */
	public static ConsultarImprimirGlosasSinRespuestaDao getConsultarImprimirGlosasSinRespuestaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImprimirGlosasSinRespuestaDao();
	}
	
	/**
	 * 
	 * @param con
	 * @param string 
	 * @return
	 */
	public static ArrayList<DtoGlosa> consultarListadoGlosas(Connection con, String filtroConvenio, String filtroContrato, String fechaInicial, String fechaFinal, String indicativo, String consecutivoFactura)
	{
  	 
		return getConsultarImprimirGlosasSinRespuestaDao().consultarListadoGlosas(con,filtroConvenio,filtroContrato,fechaInicial,fechaFinal,indicativo, consecutivoFactura);
	}
	
	
	/**
	 * 
	 * @param convenio
	 * @param contrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static String cadenaConsultaGlosasSinResp(String convenio, int contrato, String fechaInicial, String fechaFinal, String indicativo ){
		HashMap parametros = new HashMap();
		parametros.put("convenio",convenio);
		parametros.put("contrato",contrato);
		parametros.put("fechaIni",fechaInicial);
		parametros.put("fechaFin",fechaFinal); 	
		parametros.put("indicativo",indicativo);
		return getConsultarImprimirGlosasSinRespuestaDao().cadenaConsultaGlosasSinResp(parametros);
	}
	
	
	
	public static String cadenaArchivoPlano(Connection con, ConsultarImprimirGlosasSinRespuestaForm forma,UsuarioBasico usuario){
			
			String Cadena=new String("");
			String campos=new String("");
			String convenio=new String(" ");
			double valorTotalGlosa=0;
			int numRegistros= forma.getGlosasSinRespuesta().size();
			campos="Convenio,   Contrato,   Glosa Entidad,   F Notificacion,   Glosa,    Ind,   Estado,   ValorGlosa,   Respuesta,   Dias sin Rta \n";
			
			if(!forma.getCodigoConvenio().equals("")){
				convenio=forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[1];	
			}  
			
			
			for(int i=0; i< numRegistros; i++){
				
				if(i==0){
					Cadena+="Reporte:, GLOSAS SIN RESPUESTA, \n";
					Cadena+="Criterios de Busqueda: "+"\n ";
					Cadena+="Convenio:,"+convenio+", \n";
					Cadena+="Contrato:,"+forma.getCodigoContrato()+", \n";
					Cadena+="Fecha Not Inicial:, "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaNotificacionInicial())+",\n ";
					Cadena+="Fecha Not Final:, "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaNotificacionFinal())+", \n";
					Cadena+="Indicativo: "+forma.getIndicativoFueAuditada()+"\n\n\n";	
				    forma.setCriteriosConsulta(Cadena);
					Cadena+= campos; 
				    Cadena+=forma.getGlosasSinRespuesta().get(i).getNombreConvenio()+",  "+
				    forma.getGlosasSinRespuesta().get(i).getCodigoContrato()+",  "+
				    forma.getGlosasSinRespuesta().get(i).getGlosaEntidad()+",  "+
				    UtilidadFecha.conversionFormatoFechaAAp(forma.getGlosasSinRespuesta().get(i).getFechaNotificacion())+",  "+
				    forma.getGlosasSinRespuesta().get(i).getGlosaSistema()+",  "+
				    forma.getGlosasSinRespuesta().get(i).getIndicativoFueAuditada()+",  "+
				    forma.getGlosasSinRespuesta().get(i).getEstado()+",  "+
				    String.valueOf(forma.getGlosasSinRespuesta().get(i).getValorGlosa())+",  "+
				    forma.getGlosasSinRespuesta().get(i).getDtoRespuestaGlosa().getRespuestaConsecutivo()+",  "+
				    String.valueOf(UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(forma.getGlosasSinRespuesta().get(i).getFechaNotificacion()),UtilidadFecha.getFechaActual()))+" \n ";
				    
				    valorTotalGlosa=valorTotalGlosa + forma.getGlosasSinRespuesta().get(i).getValorGlosa();
				}else{
				
					Cadena+=forma.getGlosasSinRespuesta().get(i).getNombreConvenio()+",  "+
				    forma.getGlosasSinRespuesta().get(i).getCodigoContrato()+",  "+
				    forma.getGlosasSinRespuesta().get(i).getGlosaEntidad()+",  "+
				    UtilidadFecha.conversionFormatoFechaAAp(forma.getGlosasSinRespuesta().get(i).getFechaNotificacion())+",  "+
				    forma.getGlosasSinRespuesta().get(i).getGlosaSistema()+",  " +
				    forma.getGlosasSinRespuesta().get(i).getIndicativoFueAuditada()+",   "+
				    forma.getGlosasSinRespuesta().get(i).getEstado()+",  "+
				    String.valueOf(forma.getGlosasSinRespuesta().get(i).getValorGlosa())+",  "+
				    forma.getGlosasSinRespuesta().get(i).getDtoRespuestaGlosa().getRespuestaConsecutivo()+",  "+
				    String.valueOf(UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(forma.getGlosasSinRespuesta().get(i).getFechaNotificacion()),UtilidadFecha.getFechaActual()))+" \n\n ";
					
					valorTotalGlosa=valorTotalGlosa + forma.getGlosasSinRespuesta().get(i).getValorGlosa();    
				}		
   			 }
			
			  
			Cadena+="\n\n Total Glosas sin Respuesta:, " +UtilidadTexto.formatearExponenciales(valorTotalGlosa);			
		
			return Cadena;
			
		}
	
	
	public static void crearCvs (Connection con, ConsultarImprimirGlosasSinRespuestaForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
			//arma el nombre del archivo
			String nombreReport="Listado_Glosas_sin_Respuesta"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
			//se genera el documento con la informacion
			String path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
			String url="../upload"+System.getProperty("file.separator");
			String txt = cadenaArchivoPlano(con, forma, usuario);
				 	
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
	
	
	public static boolean guardar(Connection con, HashMap criterios)
	{
		return getConsultarImprimirGlosasSinRespuestaDao().guardar(con, criterios);
	}

	
	
	
}
