package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.Listado;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.ProrrogarAnularAutorizacionesEntSubcontratadasForm;
import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dao.manejoPaciente.ProrrogarAnularAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

public class ProrrogarAnularAutorizacionesEntSubcontratadas {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(ProrrogarAnularAutorizacionesEntSubcontratadas.class);
     
	/**
	 * 
	 * @return
	 */
	public static ProrrogarAnularAutorizacionesEntSubcontratadasDao getProrrogarAnularAutorizacionesEntSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProrrogarAnularAutorizacionesEntSubcontratadasDao();
	}
	
	 /**
	 *  Metodo para realzar la consulta de las Autorizaciones Entidades SubContratadas 
	 * @param codPaciente
	 * @param codInstitucion
	 * @return
	 */
	public static ArrayList<DtoAutorizacionEntSubContratada> listadoAutorizacionesEntSub(int codPaciente, int codInstitucion)
	{
		return getProrrogarAnularAutorizacionesEntSubcontratadasDao().listadoAutorizacionesEntSub(codPaciente, codInstitucion);
	}
	
	
	/**
	 * Metodo de Ordenamiento por columna
	 * @param lista
	 * @param ultimaPropiedad
	 * @param propiedad
	 * @return
	 */
public static ArrayList<DtoAutorizacionEntSubContratada> ordenarColumna(ArrayList<DtoAutorizacionEntSubContratada> lista, String ultimaPropiedad,String propiedad){
		
		
		ArrayList<DtoAutorizacionEntSubContratada> listaOrdenada = new ArrayList<DtoAutorizacionEntSubContratada>();
		HashMap mapaAutorizaciones=new HashMap();
		mapaAutorizaciones.put("numRegistros","0");
		HashMap mapaOrdenado= new HashMap();
		mapaOrdenado.put("numRegistros","0");
		
		String[] indices={"codigoPk_", "fechaAutorizacion_", "horaAutorizacion_","codEntidadauto_", "nomEntidadAuto_", "numeroSolicitud_","codConvenio_","nomConvenio_","fechaVencimiento_","codServicio_","nomServicio_","cantidad_","observaciones_","tipoAutorizacion_","estado_", "fechaModificacion_","horaModificacion_" ,"codInstitucion_","nomInstitucion_", "codPaciente_","nomPaciente_","tipoIdPaciente_","numIdPaciente_","fechaAnulacion_","horaAnulacion_","usuarioAnulacion_","consecutivoAutorizacion_","anioConsecutivo_","codTipoContrato_","nomTipoContrato_","nivel_","numIngreso_" };
		
		logger.info ("\n\n Tama�o Lista >> "+lista.size());
		
		for(int i=0; i<lista.size();i++)
		{	
		   // Datos Visibles	
		   DtoAutorizacionEntSubContratada dtoAutorizacion = (DtoAutorizacionEntSubContratada)lista.get(i);
		   mapaAutorizaciones.put("codigoPk_"+i, dtoAutorizacion.getCodigoPk());
		   mapaAutorizaciones.put("fechaAutorizacion_"+i, dtoAutorizacion.getFechaAutorizacion());
		   mapaAutorizaciones.put("horaAutorizacion_"+i, dtoAutorizacion.getHoraAutorizacion());
		   mapaAutorizaciones.put("codEntidadauto_"+i, dtoAutorizacion.getCodEntidadAutorizada());
		   mapaAutorizaciones.put("nomEntidadAuto_"+i, dtoAutorizacion.getNomEntidadAutorizada());
		   
		   mapaAutorizaciones.put("numeroSolicitud_"+i, dtoAutorizacion.getNumeroSolicitud());
		   mapaAutorizaciones.put("codConvenio_"+i, dtoAutorizacion.getCodConvenio());
		   mapaAutorizaciones.put("nomConvenio_"+i, dtoAutorizacion.getNomConvenio());
		   mapaAutorizaciones.put("fechaVencimiento_"+i, dtoAutorizacion.getFechaVencimiento());
		   mapaAutorizaciones.put("codServicio_"+i, dtoAutorizacion.getCodServicio());
		   mapaAutorizaciones.put("nomServicio_"+i, dtoAutorizacion.getNomServicio());
		   mapaAutorizaciones.put("cantidad_"+i, dtoAutorizacion.getCantidad());
		   mapaAutorizaciones.put("observaciones_"+i, dtoAutorizacion.getObservaciones());
		  
		   mapaAutorizaciones.put("tipoAutorizacion_"+i,dtoAutorizacion.getTipoAutorizacion());
		   mapaAutorizaciones.put("estado_"+i, dtoAutorizacion.getEstado());
		   mapaAutorizaciones.put("fechaModificacion_"+i, dtoAutorizacion.getFechaModificacion());
		   mapaAutorizaciones.put("horaModificacion_"+i, dtoAutorizacion.getHoraModificacion());
		   mapaAutorizaciones.put("codInstitucion_"+i, dtoAutorizacion.getCodInstitucion());
		   mapaAutorizaciones.put("nomInstitucion_"+i, dtoAutorizacion.getNomInstitucion());
		   
		   mapaAutorizaciones.put("codPaciente_"+i, dtoAutorizacion.getCodPaciente());
		   mapaAutorizaciones.put("nomPaciente_"+i, dtoAutorizacion.getNomPaciente());
		   mapaAutorizaciones.put("tipoIdPaciente_"+i, dtoAutorizacion.getTipoIdPacinte());
		   mapaAutorizaciones.put("numIdPaciente_"+i, dtoAutorizacion.getNumIdPaciente());
		   
		  
		   mapaAutorizaciones.put("fechaAnulacion_"+i, dtoAutorizacion.getFechaAnulacion());
		   mapaAutorizaciones.put("horaAnulacion_"+i, dtoAutorizacion.getHoraAnulacion());
		   
		   mapaAutorizaciones.put("usuarioAnulacion_"+i, dtoAutorizacion.getUsuarioAnulacion());
		   mapaAutorizaciones.put("consecutivoAutorizacion_"+i, dtoAutorizacion.getConsecutivoAutorizacion());
		   mapaAutorizaciones.put("anioConsecutivo_"+i, dtoAutorizacion.getAnoConsecutivo());
		   
		   mapaAutorizaciones.put("codTipoContrato_"+i, dtoAutorizacion.getCodTipoContrato());
		   mapaAutorizaciones.put("nomTipoContrato_"+i, dtoAutorizacion.getNomTipoContrato());
		   mapaAutorizaciones.put("nivel_"+i, dtoAutorizacion.getNivel());
		   mapaAutorizaciones.put("numIngreso_"+i, dtoAutorizacion.getIngreso());
		   
		   
		   mapaAutorizaciones.put("numRegistros", i+1);
		  	   
		} 
		mapaOrdenado = Listado.ordenarMapa(indices, propiedad, ultimaPropiedad, mapaAutorizaciones, Utilidades.convertirAEntero(mapaAutorizaciones.get("numRegistros").toString()));
		mapaOrdenado.put("numRegistros", mapaAutorizaciones.get("numRegistros").toString());
		
	    
		for(int j=0; j< Utilidades.convertirAEntero(mapaOrdenado.get("numRegistros").toString()); j++)
	 	   {
			DtoAutorizacionEntSubContratada dto = new DtoAutorizacionEntSubContratada();	
						
			dto.setCodigoPk(mapaOrdenado.get("codigoPk_"+j).toString());
			dto.setFechaAutorizacion(mapaOrdenado.get("fechaAutorizacion_"+j).toString());
			dto.setHoraAutorizacion(mapaOrdenado.get("horaAutorizacion_"+j).toString());
			dto.setCodEntidadAutorizada(Utilidades.convertirAEntero(mapaOrdenado.get("codEntidadauto_"+j).toString()));
			dto.setNomEntidadAutorizada(mapaOrdenado.get("nomEntidadAuto_"+j).toString());
			dto.setNumeroSolicitud(mapaOrdenado.get("numeroSolicitud_"+j).toString());
			dto.setCodConvenio(Utilidades.convertirAEntero(mapaOrdenado.get("codConvenio_"+j).toString()));
			dto.setNomConvenio(mapaOrdenado.get("nomConvenio_"+j).toString());
			dto.setFechaVencimiento(mapaOrdenado.get("fechaVencimiento_"+j).toString());			
			dto.setCodServicio(Utilidades.convertirAEntero(mapaOrdenado.get("codServicio_"+j).toString()));
			dto.setNomServicio(mapaOrdenado.get("nomServicio_"+j).toString());			
			dto.setCantidad(mapaOrdenado.get("cantidad_"+j).toString());
			dto.setObservaciones(mapaOrdenado.get("observaciones_"+j).toString());			
			dto.setTipoAutorizacion(mapaOrdenado.get("tipoAutorizacion_"+j).toString());
			dto.setEstado(mapaOrdenado.get("estado_"+j).toString());
			dto.setFechaModificacion(mapaOrdenado.get("fechaModificacion_"+j).toString());
			dto.setHoraModificacion(mapaOrdenado.get("horaModificacion_"+j).toString());	
			dto.setCodInstitucion(Utilidades.convertirAEntero(mapaOrdenado.get("codInstitucion_"+j).toString()));
			dto.setNomInstitucion(mapaOrdenado.get("nomInstitucion_"+j).toString());			
			dto.setCodPaciente(Utilidades.convertirAEntero(mapaOrdenado.get("codPaciente_"+j).toString()));
			dto.setNomPaciente(mapaOrdenado.get("nomPaciente_"+j).toString());	
			dto.setTipoIdPacinte(mapaOrdenado.get("tipoIdPaciente_"+j).toString());
			dto.setNumIdPaciente(mapaOrdenado.get("numIdPaciente_"+j).toString());
			dto.setFechaAnulacion(mapaOrdenado.get("fechaAnulacion_"+j).toString());			
			dto.setHoraAnulacion(mapaOrdenado.get("horaAnulacion_"+j).toString());
			dto.setUsuarioAnulacion(mapaOrdenado.get("usuarioAnulacion_"+j).toString());
			dto.setConsecutivoAutorizacion(mapaOrdenado.get("consecutivoAutorizacion_"+j).toString());
			dto.setAnoConsecutivo(mapaOrdenado.get("anioConsecutivo_"+j).toString());
			dto.setCodTipoContrato(Utilidades.convertirAEntero(mapaOrdenado.get("codTipoContrato_"+j).toString()));
			dto.setNomTipoContrato(mapaOrdenado.get("nomTipoContrato_"+j).toString());
			dto.setNivel(mapaOrdenado.get("nivel_"+j).toString());
			dto.setIngreso(mapaOrdenado.get("numIngreso_"+j).toString());
			
			listaOrdenada.add(dto);
		   }
		
		return listaOrdenada;
		
		
	}


/**
 * Metodo que inicializa lo parametros de prorroga 
 * @param loginUsuario
 * @param fechaInicial
 * @param codAutorizacion
 * @return
 */
  public static HashMap inicializarParametrosProrroga(String loginUsuario, String fechaInicial, String codAutorizacion)
  {
	    HashMap parametros = new HashMap();
		parametros.put("fechaProrroga", fechaInicial);
		parametros.put("horaProrroga", UtilidadFecha.getHoraActual());
		parametros.put("usuarioProrroga", loginUsuario);
		parametros.put("autorizacion", codAutorizacion);
		parametros.put("fechaVencimientoInicial", fechaInicial);
		return parametros;
  }
  
  /**
   * Metodo que inicializa los parametros de Anulacion
   * @param loginUsuario
   * @param codAutorizacion
   * @return
   */
  public static HashMap inicializarParametrosAnulacion(String loginUsuario, String codAutorizacion)
  {
	    HashMap parametros = new HashMap();
		parametros.put("motivoAnulacion", "");
		parametros.put("autorizacion", codAutorizacion);
		parametros.put("usuarioAnulacion", loginUsuario);
		
		return parametros;
  }
  
  
  

  /**
   * Metodo para realizar validaciones 
   * @param parametros
   * @return
   */
  public static ActionErrors validacionBusquedaporRango(HashMap parametros)
  {
  	Utilidades.imprimirMapa(parametros);
  	ActionErrors errores = new ActionErrors();

  	if(parametros.get("fechaAutorizacionInicial").toString().equals("") && 
  			parametros.get("fechaAutorizacionFinal").toString().equals(""))
  		
  	{
  		errores.add("descripcion",new ActionMessage("errors.required","La Fecha Autorizacion Inicial y La Fecha Autorizacion Final  "));
  	}
  	
  	     if((!parametros.get("fechaAutorizacionInicial").toString().equals("") 
  					&& parametros.get("fechaAutorizacionFinal").toString().equals("")) || 
  						(parametros.get("fechaAutorizacionInicial").toString().equals("")
  							&& !parametros.get("fechaAutorizacionFinal").toString().equals("")))
  			{
  				if(parametros.get("fechaAutorizacionInicial").toString().equals(""))
  					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Autorizacion Inicial "));
  				
  				if(parametros.get("fechaAutorizacionFinal").toString().equals(""))
  					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Autorizacion Final "));
  			}
  			else if(!parametros.get("fechaAutorizacionInicial").toString().equals("") && 
  						!parametros.get("fechaAutorizacionFinal").toString().equals(""))
  			{
  				if(!UtilidadFecha.validarFecha(parametros.get("fechaAutorizacionInicial").toString()))
  					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Autorizaion Inicial "));
  				
  				if(!UtilidadFecha.validarFecha(parametros.get("fechaAutorizacionFinal").toString()))
  					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Autorizacion Final "));
  				
  				if(UtilidadFecha.validarFecha(parametros.get("fechaAutorizacionInicial").toString()) && 
  						UtilidadFecha.validarFecha(parametros.get("fechaAutorizacionFinal").toString()))
  				{
  					if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaAutorizacionFinal").toString(), parametros.get("fechaAutorizacionInicial").toString()))
  					{
  						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Autorizacion Final  "," Autorizacion Inicial "));
  					}
  					
  					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaAutorizacionInicial").toString()))
  					{
  						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual "," Autorizacion Inicial"));
  					}
  					
  					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaAutorizacionFinal").toString()))
  					{
  						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual ","Autorizacion Final"));
  					}		  
  					
  				}	
  		
                }
  	    
  	     if((!parametros.get("ordenInicial").toString().equals("") 
  					&& parametros.get("ordenFinal").toString().equals("")) || 
  						(parametros.get("ordenInicial").toString().equals("")
  							&& !parametros.get("ordenFinal").toString().equals("")))
  			{
  				if(parametros.get("ordenInicial").toString().equals(""))
  					errores.add("descripcion",new ActionMessage("errors.required"," SI la orden Final se diligencia, la Orden Inicial "));
  				
  				if(parametros.get("ordenFinal").toString().equals(""))
  					errores.add("descripcion",new ActionMessage("errors.required","SI la Orden Inicial se diligencia, la Orden Final "));
  			}
  			else if(!parametros.get("ordenInicial").toString().equals("") && 
  						!parametros.get("ordenFinal").toString().equals(""))
  			{
  				if(Utilidades.convertirAEntero(parametros.get("ordenInicial").toString()) > Utilidades.convertirAEntero(parametros.get("ordenFinal").toString()) )
  				{
  					
  					errores.add("descripcion",new ActionMessage("errors.notEspecific","La Orden Final debe ser mayor a la Orden Inicial "));	
  				}
  			}
  	     
  	     if((!parametros.get("autorizacionInicial").toString().equals("") 
  					&& parametros.get("autorizacionFinal").toString().equals("")) || 
  						(parametros.get("autorizacionInicial").toString().equals("")
  							&& !parametros.get("autorizacionFinal").toString().equals("")))
  			{
  				if(parametros.get("autorizacionInicial").toString().equals(""))
  					errores.add("descripcion",new ActionMessage("errors.required"," SI la Autorizacion Final se diligencia, la Autorizacion Inicial "));
  				
  				if(parametros.get("autorizacionFinal").toString().equals(""))
  					errores.add("descripcion",new ActionMessage("errors.required","SI la Autorizacion Inicial se diligencia, la Autorizacion Final "));
  			}
  			else if(!parametros.get("autorizacionInicial").toString().equals("") && 
  						!parametros.get("autorizacionFinal").toString().equals(""))
  			{
  				if(Utilidades.convertirAEntero(parametros.get("autorizacionInicial").toString()) > Utilidades.convertirAEntero(parametros.get("autorizacionFinal").toString()) )
  				{
  					
  					errores.add("descripcion",new ActionMessage("errors.notEspecific","La Autorizacion Final debe ser mayor a la Autorizacion Inicial "));	
  				}
  			}
      	    
      return errores;	
  }
  
  
  
  
  /**
   * Metodo para inicializar los parametros de busqueda
   * @return
   */
  public static HashMap inicializarParametrosBusqueda()
  {
	    HashMap parametros = new HashMap();
		parametros.put("fechaAutorizacionInicial", UtilidadFecha.getFechaActual());
		parametros.put("fechaAutorizacionFinal", UtilidadFecha.getFechaActual() );
		parametros.put("entidadAutorizada", "");
		parametros.put("tipoAutorizacion", "");
		parametros.put("ordenInicial", "" );
		parametros.put("ordenFinal", "");
		parametros.put("autorizacionInicial", "");
		parametros.put("autorizacionFinal", "");
		
	  return parametros;
  }
	
  /**
   * Metodo para guardar datos de prorroga
   * @param con
   * @param parametrosProrroga
   * @return
   */
  public static HashMap guardarProrroga(Connection con,HashMap parametrosProrroga)
  {
	  return getProrrogarAnularAutorizacionesEntSubcontratadasDao().guardarProrroga(con,parametrosProrroga);
  }
	
  
  /**
   * Metodo para guardar datos de Anulacion
   * @param con
   * @param parametrosAnulacion
   * @return
   */
  public static HashMap guardarAnulacion(Connection con,HashMap parametrosAnulacion)
  {
	  return getProrrogarAnularAutorizacionesEntSubcontratadasDao().guardarAnulacion(con,parametrosAnulacion);
  }

public ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(HashMap parametrosBusqueda, int codigoInstitucionInt) {
	
	 return getProrrogarAnularAutorizacionesEntSubcontratadasDao().obtenerAutorizacionesEntSubContrXRango(parametrosBusqueda,codigoInstitucionInt);
}
	
}
