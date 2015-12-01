package com.princetonsa.mundo.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.AnulacionCondonacionMultasDao;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.mundo.PersonaBasica;

public class AnulacionCondonacionMultas {

	/**
     *  Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(AnulacionCondonacionMultas.class);	
    
	
	public static AnulacionCondonacionMultasDao getAnulacionCondonacionMultasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAnulacionCondonacionMultasDao();
	}
	
	/**
	 * Valida Permisos del paciente para realizar Anulacion y/o Condonacion de Multas
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static String validarPermisos(Connection con, String usuario)
	{
	   HashMap mapaAnular=new HashMap();
	   HashMap mapaCondonar=new HashMap();
	   mapaAnular=UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario, ConstantesBD.codigoActividadAutorizadaAnularMultasCitas);	
	   mapaCondonar=UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario, ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas);
	   if(Utilidades.convertirAEntero(mapaAnular.get("numRegistros").toString())>0 || Utilidades.convertirAEntero(mapaCondonar.get("numRegistros").toString())>0 )
	    {
		  return ConstantesBD.acronimoSi;  
	    }
	   
	   return ConstantesBD.acronimoNo;
	}
	
	/**
	 *  Obtiene el Array de Unidades de Agenda asociadas a un arreglo de actividades
	 * @param con
	 * @param usuario
	 * @param actividades
	 * @return
	 */
	public static ArrayList unidadesAgenda(Connection con, String usuario, ArrayList actividades)
	{
		ArrayList agenda=new ArrayList();
		String[] auxUnidades= UtilidadesConsultaExterna.unidadesAgendaXUsuario2(con, usuario, actividades, ConstantesIntegridadDominio.acronimoTipoAtencionGeneral).split(",");
		 for(int i=0 ;i< auxUnidades.length; i++)
		 {
			 agenda.add(auxUnidades[i]);
		 }
		return agenda;
	}
	
	/**
	 * Metodo para obtener la cadena que contiene las Unidades de agenda separdas por comas
	 * @param con
	 * @param usuario
	 * @param actividades
	 * @return
	 */
	public static String unidadesAgendaStr(String usuario, ArrayList actividades)
	{
		  Connection con = null;
		  con = UtilidadBD.abrirConexion(); 
	      return UtilidadesConsultaExterna.unidadesAgendaXUsuario2(con, usuario, actividades, ConstantesIntegridadDominio.acronimoTipoAtencionGeneral);
	}
	
	/**
	 * Valida que una actividad esté autorizada
	 * @param con
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public static boolean esActividadAutorizada(Connection con, int unidadAgenda,int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen){
		
		return UtilidadesConsultaExterna.esActividadAurtorizada(con, unidadAgenda, actividad, usuario, centroAtencion, isUniAgenOrAgen);
	}

	/**
	 * Metodo para obtener las citas con multas, realizando una busqueda por paciente
	 * @param con
	 * @param codigoPersona
	 * @param loginUsuario
	 * @return
	 */
	public ArrayList <DtoMultasCitas> obtenerCitasconMultas( int codigoPersona, String loginUsuario,String unidadesAgenda) 
	{
		
		return getAnulacionCondonacionMultasDao().obtenerCitasconMultas( codigoPersona,  loginUsuario, unidadesAgenda, true);
	}
	
	
	/**
	 * 
	 * @param codigoPersona
	 * @param loginUsuario
	 * @param unidadesAgenda
	 * @return
	 */
	public ArrayList <DtoMultasCitas> obtenerCitasMultasSinPermisos( int codigoPersona, String loginUsuario) 
	{
		return getAnulacionCondonacionMultasDao().obtenerCitasconMultas( codigoPersona,  loginUsuario, "", false);	
	}
	
	/**
	 * Metodo para obtenre las citas con multas realizadon una busquedad por rango
	 * @param usuario
	 * @param parametros
	 * @return
	 */
	public static  ArrayList<DtoMultasCitas> busquedaPorRangoMultasCita( String usuario, HashMap parametros)
	{
		return getAnulacionCondonacionMultasDao().busquedaPorRangoMultasCita(usuario,parametros);
	}

	/**
	 * Metodo de Ordenamiento por columna
	 * @param lista
	 * @param ultimaPropiedad
	 * @param propiedad
	 * @return
	 */
public static ArrayList<DtoMultasCitas> ordenarColumna(ArrayList<DtoMultasCitas> lista, String ultimaPropiedad,String propiedad){
		
		
		ArrayList<DtoMultasCitas> listaOrdenada = new ArrayList<DtoMultasCitas>();
		HashMap mapaMultas=new HashMap();
		mapaMultas.put("numRegistros","0");
		HashMap mapaOrdenado= new HashMap();
		mapaOrdenado.put("numRegistros","0");
		
		String[] indices={"multa_", "fechamulta_", "estadomulta_","valormulta_", "uniagenda_", "fechacita_", "horacita_", "estadocita_","nombrepaciente_","idpaciente_", "profesional_","codigocita_","servicioscita_","codigounidadagenda_","codigomedico_","usuario_","codcentroatencion_","nombrecentroatencion_","codigomotivomulta_","descripcionmotivo_","nombreconvenio_","codconvenio_","condonar_","anular_"};
		
		logger.info ("\n\n Tamaño Lista >> "+lista.size());
		
		for(int i=0; i<lista.size();i++)
		{	
		   // Datos Visibles	
		   DtoMultasCitas dtoMultas = (DtoMultasCitas)lista.get(i);
		   mapaMultas.put("multa_"+i, dtoMultas.getCodigoMulta());
		   mapaMultas.put("fechamulta_"+i, dtoMultas.getFechaMulta());
		   mapaMultas.put("estadomulta_"+i, dtoMultas.getEstadoMulta());
		   mapaMultas.put("valormulta_"+i, dtoMultas.getValor());
		   mapaMultas.put("uniagenda_"+i, dtoMultas.getDescripcionUnidadAgenda());
		   mapaMultas.put("fechacita_"+i, dtoMultas.getFechaCita());
		   mapaMultas.put("horacita_"+i, dtoMultas.getHoraCita());
		   mapaMultas.put("estadocita_"+i, dtoMultas.getEstadoCita());
		   mapaMultas.put("profesional_"+i, dtoMultas.getProfesional());
		   
		   //Datos Invisibles
		   mapaMultas.put("codigocita_"+i, dtoMultas.getCita());
		   mapaMultas.put("servicioscita_"+i, dtoMultas.getServiciosCita());
		   mapaMultas.put("nombrepaciente_"+i, dtoMultas.getNombrePaciente());
		   mapaMultas.put("idpaciente_"+i, dtoMultas.getIdPaciente());
		   mapaMultas.put("codigounidadagenda_"+i, dtoMultas.getUnidadAgenda());
		   mapaMultas.put("codigomedico_"+i, dtoMultas.getCodProfesional());
		   mapaMultas.put("usuario_"+i, dtoMultas.getUsuario());
		   mapaMultas.put("codcentroatencion_"+i, dtoMultas.getCodCentroAtencion());
		   mapaMultas.put("nombrecentroatencion_"+i,dtoMultas.getNombreCentroAtencion());
		   mapaMultas.put("codigomotivomulta_"+i, dtoMultas.getCodMotivo_Cond_anul());
		   mapaMultas.put("descripcionmotivo_"+i, dtoMultas.getDescripcionMotivo_Cond_Anul());
		   mapaMultas.put("nombreconvenio_"+i, dtoMultas.getConvenioCita());
		   mapaMultas.put("codconvenio_"+i, dtoMultas.getCodConvenioCita());
		   
		   if(dtoMultas.isCondonar())
			   {	   
			   mapaMultas.put("condonar_"+i, ConstantesBD.acronimoTrueCorto);
			   }
			 else
			  {
				   mapaMultas.put("condonar_"+i, ConstantesBD.acronimoFalseCorto);
			   }
		   if(dtoMultas.isAnular())
			   {	   
			   mapaMultas.put("anular_"+i, ConstantesBD.acronimoTrueCorto);
			   }
		     else
		     {
		    	 mapaMultas.put("anular_"+i, ConstantesBD.acronimoFalseCorto);
		     }
		   mapaMultas.put("numRegistros", i+1);
			   
		} 
		mapaOrdenado = Listado.ordenarMapa(indices, propiedad, ultimaPropiedad, mapaMultas, Utilidades.convertirAEntero(mapaMultas.get("numRegistros").toString()));
		mapaOrdenado.put("numRegistros", mapaMultas.get("numRegistros").toString());
		
	    
		for(int j=0; j< Utilidades.convertirAEntero(mapaOrdenado.get("numRegistros").toString()); j++)
	 	   {
			DtoMultasCitas dtoMultas = new DtoMultasCitas();
			
			dtoMultas.setCodigoMulta(Utilidades.convertirAEntero(mapaOrdenado.get("multa_"+j).toString()));
			dtoMultas.setFechaMulta(mapaOrdenado.get("fechamulta_"+j).toString());
			dtoMultas.setEstadoMulta(mapaOrdenado.get("estadomulta_"+j).toString());
			dtoMultas.setValor(Utilidades.convertirAEntero(mapaOrdenado.get("valormulta_"+j).toString()));
			dtoMultas.setUnidadAgenda(Utilidades.convertirAEntero(mapaOrdenado.get("codigounidadagenda_"+j).toString()));
			dtoMultas.setDescripcionUnidadAgenda(mapaOrdenado.get("uniagenda_"+j).toString());
			dtoMultas.setFechaCita(mapaOrdenado.get("fechacita_"+j).toString());
			dtoMultas.setHoraCita(mapaOrdenado.get("horacita_"+j).toString());
			dtoMultas.setEstadoCita(mapaOrdenado.get("estadocita_"+j).toString());
			dtoMultas.setProfesional(mapaOrdenado.get("profesional_"+j).toString());
			
			dtoMultas.setNombrePaciente(mapaOrdenado.get("nombrepaciente_"+j).toString());
			dtoMultas.setIdPaciente(mapaOrdenado.get("idpaciente_"+j).toString());
			dtoMultas.setCodProfesional(Utilidades.convertirAEntero(mapaOrdenado.get("codigomedico_"+j).toString()));
			dtoMultas.setUsuario(mapaOrdenado.get("usuario_"+j).toString());
			dtoMultas.setCodCentroAtencion(Utilidades.convertirAEntero(mapaOrdenado.get("codcentroatencion_"+j).toString()));
			dtoMultas.setNombreCentroAtencion(mapaOrdenado.get("nombrecentroatencion_"+j).toString());
			dtoMultas.setCodMotivo_Cond_anul(Utilidades.convertirAEntero(mapaOrdenado.get("codigomotivomulta_"+j).toString()));
			dtoMultas.setDescripcionMotivo_Cond_Anul(mapaOrdenado.get("descripcionmotivo_"+j).toString());
			dtoMultas.setConvenioCita(mapaOrdenado.get("nombreconvenio_"+j).toString());
			dtoMultas.setCodConvenioCita(Utilidades.convertirAEntero(mapaOrdenado.get("codconvenio_"+j).toString()));
			dtoMultas.setCita(Utilidades.convertirAEntero(mapaOrdenado.get("codigocita_"+j).toString()));
			dtoMultas.setServiciosCita((HashMap)mapaOrdenado.get("servicioscita_"+j));
			
			if(mapaOrdenado.get("condonar_"+j).toString().equals(ConstantesBD.acronimoTrueCorto))
			  {
				dtoMultas.setCondonar(true);
			  }
			   else
			  {
				dtoMultas.setCondonar(false);
			  }
			 
			if(mapaOrdenado.get("anular_"+j).toString().equals(ConstantesBD.acronimoTrueCorto))
			  {			
				dtoMultas.setAnular(true);
			  }
				else
				{
					dtoMultas.setAnular(false);
				}
				
			listaOrdenada.add(dtoMultas);
		   }
		
		return listaOrdenada;
		
		
	}
	


   public static HashMap guardarMultaCita(Connection con, int multa, String estadoMulta, int motivo, String observaciones, String usuario)
    {
	   HashMap parametros = new HashMap();
	   parametros.put("numRegistros", "0");
	   parametros.put("consecutivomulta", multa);
	   parametros.put("estadomulta", estadoMulta);
	   parametros.put("motivomulta", motivo);
	   parametros.put("observaciones", observaciones);
	   parametros.put("usuario", usuario);
	  
	   
	   return getAnulacionCondonacionMultasDao().guardarMultaCita(con,parametros);
    }
	
	
   
   /**
	 * Inicializa los parametros de busqueda por rango 
	 * */
	public static HashMap inicializarParametrosBusquedaRango(int centroAtencion) 
	{
		HashMap parametros = new HashMap();
		parametros.put("fechaInicialGeneracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaFinalGeneracion",UtilidadFecha.getFechaActual());
		parametros.put("centroatencion",centroAtencion);
		parametros.put("unidadagenda", "");
		parametros.put("convenio","");
		parametros.put("profesional", "");
		parametros.put("estadocita","");
		
		
		return parametros;
	}
	
	
	/**
	 * 
	 * @param centrosAtencion
	 * @return
	 */
    public static HashMap unidadesAgendaCentrosAtencion(HashMap centrosAtencion)
    {
    	return getAnulacionCondonacionMultasDao().unidadesAgendaCentrosAtencion(centrosAtencion);
    }
	
    /**
     * 
     * @param centrosAtencion
     * @return
     */
    public static HashMap unidadesAgendaXCentroAtencion(int centroAtencion)
    {
    	return getAnulacionCondonacionMultasDao().unidadesAgendaXCentroAtencion(centroAtencion);
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
    
			    if((!parametros.get("fechaInicialGeneracion").toString().equals("") 
						&& parametros.get("fechaFinalGeneracion").toString().equals("")) || 
							(parametros.get("fechaInicialGeneracion").toString().equals("")
								&& !parametros.get("fechaFinalGeneracion").toString().equals("")))
				{
					if(parametros.get("fechaInicialGeneracion").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Generación de la Multa"));
					
					if(parametros.get("fechaFinalGeneracion").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Generación de la Multa"));
				}
				else if(!parametros.get("fechaInicialGeneracion").toString().equals("") && 
							!parametros.get("fechaFinalGeneracion").toString().equals(""))
				{
					if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialGeneracion").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Inicial de Generación Multa"));
					
					if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalGeneracion").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Generación Multa"));
					
					if(UtilidadFecha.validarFecha(parametros.get("fechaInicialGeneracion").toString()) && 
							UtilidadFecha.validarFecha(parametros.get("fechaFinalGeneracion").toString()))
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalGeneracion").toString(), parametros.get("fechaInicialGeneracion").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Generación de Multa "," Inicial de Generación de Multa"));
						}
						
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaInicialGeneracion").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual "," Inicial de Generación de Multa"));
						}
						
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaFinalGeneracion").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Actual "," Final de Generación de Multa"));
						}
						
					}	
			
	    }
    return errores;	
	}
}
