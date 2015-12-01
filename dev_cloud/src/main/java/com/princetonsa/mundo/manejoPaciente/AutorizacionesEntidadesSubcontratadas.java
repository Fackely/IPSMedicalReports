package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.Listado;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.AutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

/**
 * @author 
 */
public class AutorizacionesEntidadesSubcontratadas {

	private static Logger logger = Logger.getLogger(AutorizacionesEntidadesSubcontratadas.class);
	
	
	public static AutorizacionesEntidadesSubcontratadasDao getAutorizacionesEntidadesSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionesEntidadesSubcontratadasDao();
	}
	
/**
 * 	
 * @param codigoPersona
 * @param institucion
 * @return
 */
	
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, int institucion)
	{
		String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		
		return getAutorizacionesEntidadesSubcontratadasDao().obtenerListadoSolicitudes(codigoPersona, tar); 
	}
	
	
	/**
   	* @param parametrosBusqueda
   	* @return
   	*/
	@SuppressWarnings("rawtypes")
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametrosBusqueda) {
		
		  String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(parametrosBusqueda.get("centroatencion").toString()));
		  
		  return getAutorizacionesEntidadesSubcontratadasDao().obtenerSolicitudesXRango(parametrosBusqueda,tar);
	   }
	
	
	/**
	 * Metodo de Ordenamiento por columna
	 * @param lista
	 * @param ultimaPropiedad
	 * @param propiedad
	 * @return
	 */
@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public static ArrayList<DtoSolicitudesSubCuenta> ordenarColumna(ArrayList<DtoSolicitudesSubCuenta> lista, String ultimaPropiedad,String propiedad){
		
		
		ArrayList<DtoSolicitudesSubCuenta> listaOrdenada = new ArrayList<DtoSolicitudesSubCuenta>();
		HashMap mapaSolicitudes=new HashMap();
		mapaSolicitudes.put("numRegistros","0");
		HashMap mapaOrdenado= new HashMap();
		mapaOrdenado.put("numRegistros","0");
		
		String[] indices={"codigosolicitud_", "solicitud_", "fechasolicitud_","centrocosto_","nomcentrocosto_", "cups_", "servicio_","nomservicio_","codserviciocodmanualestandar_","convenio_","codconvenio_","tipocontrato_","ingreso_","codigopaciente_","nombrepaciente_","tipoidpaciente_","numeroidpaciente_", "codcentroatencion_","nomcentroatencion_", "tipoentidadejecuta_","codtipopaciente_","codnaturalezapaciente_","codviaingreso_","nomviaingreso_","nombreprofesional_" };
		
		logger.info ("\n\n Tamaño Lista >> "+lista.size());
		
		for(int i=0; i<lista.size();i++)
		{	
		    // Datos Visibles	
		   DtoSolicitudesSubCuenta dtoSolicitudes = (DtoSolicitudesSubCuenta)lista.get(i);
		   mapaSolicitudes.put("codigosolicitud_"+i, dtoSolicitudes.getCodigo());
		   mapaSolicitudes.put("solicitud_"+i, dtoSolicitudes.getNumeroSolicitud());
		   mapaSolicitudes.put("fechasolicitud_"+i, dtoSolicitudes.getFechaSolicitud());
		   mapaSolicitudes.put("centrocosto_"+i, dtoSolicitudes.getCentroCostoEjecuta());
		   mapaSolicitudes.put("nomcentrocosto_"+i,dtoSolicitudes.getCentroCostoEjecuta().getNombre());
		   mapaSolicitudes.put("cups_"+i, dtoSolicitudes.getCodigoCups());
		   mapaSolicitudes.put("servicio_"+i, dtoSolicitudes.getServicio());
		   mapaSolicitudes.put("nomservicio_"+i, dtoSolicitudes.getServicio().getNombre());
		   mapaSolicitudes.put("codserviciocodmanualestandar_"+i, dtoSolicitudes.getCodServicioCodManualEstandar());
		   mapaSolicitudes.put("convenio_"+i, dtoSolicitudes.getConvenio());
		   mapaSolicitudes.put("codconvenio_"+i, dtoSolicitudes.getCodConvenio());
		   mapaSolicitudes.put("tipocontrato_"+i, dtoSolicitudes.getTipoContrato());
		   mapaSolicitudes.put("ingreso_"+i, dtoSolicitudes.getIngreso());
		   mapaSolicitudes.put("tipoentidadejecuta_"+i, dtoSolicitudes.getTipoEntidadEjecuta());
		   mapaSolicitudes.put("codcentroatencion_"+i, dtoSolicitudes.getCodCentroAtencionIngreso());
		   mapaSolicitudes.put("nomcentroatencion_"+i, dtoSolicitudes.getCentroAtencionIngreso());
		   
		   //Datos Paciente Asociado
		   mapaSolicitudes.put("codigopaciente_"+i,dtoSolicitudes.getCodigoPaciente());
		   mapaSolicitudes.put("nombrepaciente_"+i, dtoSolicitudes.getNombrePaciente());
		   mapaSolicitudes.put("tipoidpaciente_"+i, dtoSolicitudes.getTipoIdPaciente());
		   mapaSolicitudes.put("numeroidpaciente_"+i, dtoSolicitudes.getNumeroIdPaciente());
		   
		   mapaSolicitudes.put("codtipopaciente_"+i, dtoSolicitudes.getCodTipoPaciente());
		   mapaSolicitudes.put("codnaturalezapaciente_"+i, dtoSolicitudes.getCodNaturalezaPaciente());
		   mapaSolicitudes.put("codviaingreso_"+i, dtoSolicitudes.getCodViaIngreso());
		   mapaSolicitudes.put("nomviaingreso_"+i, dtoSolicitudes.getViaIngreso());
		   mapaSolicitudes.put("nombreprofesional_"+i, dtoSolicitudes.getProfesional());
		   
		   mapaSolicitudes.put("numRegistros", i+1);
		  	   
		} 
		mapaOrdenado = Listado.ordenarMapa(indices, propiedad, ultimaPropiedad, mapaSolicitudes, Utilidades.convertirAEntero(mapaSolicitudes.get("numRegistros").toString()));
		mapaOrdenado.put("numRegistros", mapaSolicitudes.get("numRegistros").toString());
		
	    
		for(int j=0; j< Utilidades.convertirAEntero(mapaOrdenado.get("numRegistros").toString()); j++)
	 	   {
			DtoSolicitudesSubCuenta dto = new DtoSolicitudesSubCuenta();
			
			dto.setCodigo(mapaOrdenado.get("codigosolicitud_"+j).toString());
			dto.setNumeroSolicitud(mapaOrdenado.get("solicitud_"+j).toString());
			dto.setFechaSolicitud(mapaOrdenado.get("fechasolicitud_"+j).toString());
			dto.setCentroCostoEjecuta(new InfoDatosInt(((InfoDatosInt)mapaOrdenado.get("centrocosto_"+j)).getCodigo(),((InfoDatosInt)mapaOrdenado.get("centrocosto_"+j)).getNombre()));
			dto.setCodigoCups(mapaOrdenado.get("cups_"+j).toString());
			dto.setServicio(new InfoDatosString( ((InfoDatosString)mapaOrdenado.get("servicio_"+j)).getCodigo(),((InfoDatosString)mapaOrdenado.get("servicio_"+j)).getNombre() ));	
			dto.setCodServicioCodManualEstandar(mapaOrdenado.get("codserviciocodmanualestandar_"+j).toString());
			dto.setIngreso(Utilidades.convertirAEntero(mapaOrdenado.get("ingreso_"+j).toString()));
			dto.setConvenio(mapaOrdenado.get("convenio_"+j).toString());
			dto.setCodConvenio(Utilidades.convertirAEntero(mapaOrdenado.get("codconvenio_"+j).toString()));
			dto.setTipoContrato(mapaOrdenado.get("tipocontrato_"+j).toString());
			dto.setTipoEntidadEjecuta(mapaOrdenado.get("tipoentidadejecuta_"+j).toString());
			dto.setCodCentroAtencionIngreso(Utilidades.convertirAEntero(mapaOrdenado.get("codcentroatencion_"+j).toString()));
			dto.setCentroAtencionIngreso(mapaOrdenado.get("nomcentroatencion_"+j).toString());
			
			dto.setCodigoPaciente(Utilidades.convertirAEntero(mapaOrdenado.get("codigopaciente_"+j).toString()));
			dto.setNombrePaciente(mapaOrdenado.get("nombrepaciente_"+j).toString());
			dto.setTipoIdPaciente(mapaOrdenado.get("tipoidpaciente_"+j).toString());
			dto.setNumeroIdPaciente(mapaOrdenado.get("numeroidpaciente_"+j).toString());
			dto.setCodTipoPaciente(mapaOrdenado.get("codtipopaciente_"+j).toString());
			dto.setProfesional(mapaOrdenado.get("nombreprofesional_"+j).toString());
			
			dto.setCodNaturalezaPaciente(Utilidades.convertirAEntero(mapaOrdenado.get("codnaturalezapaciente_"+j).toString()));
			dto.setCodViaIngreso(Utilidades.convertirAEntero(mapaOrdenado.get("codviaingreso_"+j).toString()));
			dto.setViaIngreso(mapaOrdenado.get("nomviaingreso_"+j).toString());
			
			listaOrdenada.add(dto);
		   }
		
		return listaOrdenada;
		
		
	}
	


/**
 * Inicializa los parametros de busqueda por rango 
 * */
@SuppressWarnings({ "rawtypes", "unchecked" })
public static HashMap inicializarParametrosBusquedaRango(int centroAtencion) 
{
	HashMap parametros = new HashMap();
	parametros.put("fechaSolicitudInicial",UtilidadFecha.getFechaActual());
	parametros.put("fechaSolicitudFinal",UtilidadFecha.getFechaActual());
	parametros.put("centroatencion",centroAtencion);
	parametros.put("centrocosto", "");
	parametros.put("convenio","");
	parametros.put("viaingreso", "");
	parametros.put("tipoPaciente","");

	return parametros;
}


@SuppressWarnings("rawtypes")
public static HashMap ingresarAutorizacion(Connection con,HashMap parametros,ArrayList<DtoSolicitudesSubCuenta> articulosServicios)
{
	
	return getAutorizacionesEntidadesSubcontratadasDao().ingresarAutorizacion(con,parametros,articulosServicios);
}



/**
 * Metodo para consultar el Nivel de un Servicio
 * 
 * @param servicio
 * @return
 */
  public static String nivelServicio(int servicio)
  {
	  return getAutorizacionesEntidadesSubcontratadasDao().nivelServicio(servicio);
  }
	
  
 /**
  * Metodo para realizar las validaciones de los datos requeridos y de fecha 
  * @param parametros
  * @return
  */
  @SuppressWarnings("rawtypes")
public static ActionErrors validacionBusquedaporRango(HashMap parametros)
	{
		Utilidades.imprimirMapa(parametros);
		ActionErrors errores = new ActionErrors();
  
		if(parametros.get("fechaSolicitudInicial").toString().equals("") && 
				parametros.get("fechaSolicitudFinal").toString().equals(""))
			
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha Solicitud Inicial y La Fecha Solicitud Final  "));
		}
		
		     if((!parametros.get("fechaSolicitudInicial").toString().equals("") 
						&& parametros.get("fechaSolicitudFinal").toString().equals("")) || 
							(parametros.get("fechaSolicitudInicial").toString().equals("")
								&& !parametros.get("fechaSolicitudFinal").toString().equals("")))
				{
					if(parametros.get("fechaSolicitudInicial").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Solicitud Inicial "));
					
					if(parametros.get("fechaSolicitudFinal").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Solicitud Final "));
				}
				else if(!parametros.get("fechaSolicitudInicial").toString().equals("") && 
							!parametros.get("fechaSolicitudFinal").toString().equals(""))
				{
					if(!UtilidadFecha.validarFecha(parametros.get("fechaSolicitudInicial").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Solicitud Inicial "));
					
					if(!UtilidadFecha.validarFecha(parametros.get("fechaSolicitudFinal").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Solicitud Final "));
					
					if(UtilidadFecha.validarFecha(parametros.get("fechaSolicitudInicial").toString()) && 
							UtilidadFecha.validarFecha(parametros.get("fechaSolicitudFinal").toString()))
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaSolicitudFinal").toString(), parametros.get("fechaSolicitudInicial").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Solicitud Final  "," Solicitud Inicial "));
						}
						
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaSolicitudInicial").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual "," Solicitud Inicial"));
						}
						
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaSolicitudFinal").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual ","Solicitud Final"));
						}
						
					   if(UtilidadFecha.numeroDiasEntreFechas(parametros.get("fechaSolicitudInicial").toString(), parametros.get("fechaSolicitudFinal").toString())>120)
							errores.add("", new ActionMessage("errors.rangoMayorCientoVeiteDias","de Solicitudes"));
						
					}	
			
	    }
		if(parametros.get("centroatencion").equals(""))
	     {
	    	errores.add("descripcion",new ActionMessage("errors.required","EL Centro de Atención "));	
    	 }
	    	    
        return errores;	
	}
	
  
  /**
   * Metodo para consultar los centrod de Costo asociados a un centro de Atencion 
   * @param centroAtencion
   * @return
   */
  @SuppressWarnings("rawtypes")
public static HashMap centroCostoRango(int centroAtencion)
  {
	  return getAutorizacionesEntidadesSubcontratadasDao().centroCostoRango(centroAtencion);
  }

  
	
	
}
