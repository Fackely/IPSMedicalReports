package com.princetonsa.mundo.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Listado;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ordenesmedicas.ResponderConsultasEntSubcontratadasDao;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;

public class ResponderConsultasEntSubcontratadas {

	private static Logger logger = Logger.getLogger(ResponderConsultasEntSubcontratadas.class);
	
	public static ResponderConsultasEntSubcontratadasDao getResponderConsultasEntSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResponderConsultasEntSubcontratadasDao();
	}

	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static HashMap validarPermisos(Connection con, UsuarioBasico usuario) {
		
		HashMap respuesta=new HashMap();
		respuesta.put("tipoEntidadEjecuta","");
		respuesta.put("entidadesSubContratadas",new ArrayList<DtoEntidadSubcontratada>());
		respuesta.put("permisoResponderConsulta", ConstantesBD.acronimoNo);
		
		respuesta.put("tipoEntidadEjecuta",UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con,usuario.getCodigoCentroCosto())); 
		
		if(respuesta.get("tipoEntidadEjecuta").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			respuesta.put("entidadesSubContratadas",CargosEntidadesSubcontratadas.obtenerEntidadesSubcontratadasCentroCosto(con,usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()));  	
		}
		 		
		
        return respuesta;
	}

	
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, int entidadSubcontratada, int institucion, String especialidadesProf)
	{
		String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		
		return getResponderConsultasEntSubcontratadasDao().obtenerListadoSolicitudes(codigoPersona,entidadSubcontratada,tar,especialidadesProf);
		
	}
	
	/**
	   * 
	   * @param parametrosBusqueda
	   * @return
	   */
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametrosBusqueda) {
		
		  String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(parametrosBusqueda.get("centroAtencion").toString()));
		  return getResponderConsultasEntSubcontratadasDao().obtenerSolicitudesXRango(parametrosBusqueda,tar);
	   }
	
	
public static ArrayList<DtoSolicitudesSubCuenta> ordenarColumna(ArrayList<DtoSolicitudesSubCuenta> lista, String ultimaPropiedad,String propiedad){
		
		
		ArrayList<DtoSolicitudesSubCuenta> listaOrdenada = new ArrayList<DtoSolicitudesSubCuenta>();
		HashMap mapaSolicitudes=new HashMap();
		mapaSolicitudes.put("numRegistros","0");
		HashMap mapaOrdenado= new HashMap();
		mapaOrdenado.put("numRegistros","0");
		
		String[] indices={"codigosolicitud_", "solicitud_", "fechasolicitud_","horasolicitud_", "servicio_","especialidadServicio_","codigopaciente_","nombrepaciente_","tipoidpaciente_","numeroidpaciente_", "codcentroatencion_","nomcentroatencion_","esUrgente_" };
		
		logger.info ("\n\n Tamaño Lista >> "+lista.size());
		
		for(int i=0; i<lista.size();i++)
		{	
		   // Datos Visibles	
		   DtoSolicitudesSubCuenta dtoSolicitudes = (DtoSolicitudesSubCuenta)lista.get(i);
		   mapaSolicitudes.put("codigosolicitud_"+i, dtoSolicitudes.getCodigo());
		   mapaSolicitudes.put("solicitud_"+i, dtoSolicitudes.getNumeroSolicitud());
		   mapaSolicitudes.put("fechasolicitud_"+i, dtoSolicitudes.getFechaSolicitud());
		   mapaSolicitudes.put("horasolicitud_"+i, dtoSolicitudes.getHoraSolicitud());
		   mapaSolicitudes.put("codcentroatencion_"+i, dtoSolicitudes.getCodCentroAtencionIngreso());
		   mapaSolicitudes.put("nomcentroatencion_"+i, dtoSolicitudes.getCentroAtencionIngreso());
		   mapaSolicitudes.put("servicio_"+i, dtoSolicitudes.getServicio());
		   mapaSolicitudes.put("especialidadServicio_"+i, dtoSolicitudes.getEspecialidadServicioSolicitud());
		   
		   //Datos Paciente Asociado
		   mapaSolicitudes.put("codigopaciente_"+i,dtoSolicitudes.getCodigoPaciente());
		   mapaSolicitudes.put("nombrepaciente_"+i, dtoSolicitudes.getNombrePaciente());
		   mapaSolicitudes.put("tipoidpaciente_"+i, dtoSolicitudes.getTipoIdPaciente());
		   mapaSolicitudes.put("numeroidpaciente_"+i, dtoSolicitudes.getNumeroIdPaciente());
		   mapaSolicitudes.put("esUrgente_"+i, dtoSolicitudes.isUrgenteSolicitud());
		   
		
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
			dto.setHoraSolicitud(mapaOrdenado.get("horasolicitud_"+j).toString());
			
			dto.setServicio(new InfoDatosString( ((InfoDatosString)mapaOrdenado.get("servicio_"+j)).getCodigo(),((InfoDatosString)mapaOrdenado.get("servicio_"+j)).getNombre() ));	
			dto.setEspecialidadServicioSolicitud(mapaOrdenado.get("especialidadServicio_"+j).toString());
			dto.setCodCentroAtencionIngreso(Utilidades.convertirAEntero(mapaOrdenado.get("codcentroatencion_"+j).toString()));
			dto.setCentroAtencionIngreso(mapaOrdenado.get("nomcentroatencion_"+j).toString());
			
			dto.setCodigoPaciente(Utilidades.convertirAEntero(mapaOrdenado.get("codigopaciente_"+j).toString()));
			dto.setNombrePaciente(mapaOrdenado.get("nombrepaciente_"+j).toString());
			dto.setTipoIdPaciente(mapaOrdenado.get("tipoidpaciente_"+j).toString());
			dto.setNumeroIdPaciente(mapaOrdenado.get("numeroidpaciente_"+j).toString());
			if(Boolean.valueOf(mapaOrdenado.get("esUrgente_"+j).toString()))
			{
				dto.setUrgenteSolicitud(true);
			}
			else
			{
				dto.setUrgenteSolicitud(false);
			}
			
			listaOrdenada.add(dto);
		   }
		
		return listaOrdenada;
		
		
	}

	

/**
 * 
 * @param parametros
 * @return
 */
public static ActionErrors validacionBusquedaporRango(HashMap parametros)
{
	Utilidades.imprimirMapa(parametros);
	ActionErrors errores = new ActionErrors();

	if(parametros.get("entidadSubcontratada").toString().equals("") )
	{
		errores.add("descripcion",new ActionMessage("errors.required","La Entidad Subcontratada"));
	}
	
	
	if(parametros.get("fechaInicial").toString().equals("") && 
			parametros.get("fechaFinal").toString().equals(""))
		
	{
		errores.add("descripcion",new ActionMessage("errors.required","La Fecha  Inicial y La Fecha  Final  "));
	}
	
	     if((!parametros.get("fechaInicial").toString().equals("") 
					&& parametros.get("fechaFinal").toString().equals("")) || 
						(parametros.get("fechaInicial").toString().equals("")
							&& !parametros.get("fechaFinal").toString().equals("")))
			{
				if(parametros.get("fechaInicial").toString().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial "));
				
				if(parametros.get("fechaFinal").toString().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final "));
			}
			else if(!parametros.get("fechaInicial").toString().equals("") && 
						!parametros.get("fechaFinal").toString().equals(""))
			{
				if(!UtilidadFecha.validarFecha(parametros.get("fechaInicial").toString()))
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha  Inicial "));
				
				if(!UtilidadFecha.validarFecha(parametros.get("fechaFinal").toString()))
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Final "));
				
				if(UtilidadFecha.validarFecha(parametros.get("fechaInicial").toString()) && 
						UtilidadFecha.validarFecha(parametros.get("fechaFinal").toString()))
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinal").toString(), parametros.get("fechaInicial").toString()))
					{
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","  Final  ","  Inicial "));
					}
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaInicial").toString()))
					{
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual ","  Inicial"));
					}
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaFinal").toString()))
					{
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual "," Final"));
					}
				}
			}
	     
	     return errores;		     
}
	
	
	
	
	
	
}
