/*
 * @(#)CuposExtraForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.agenda;

import java.util.HashMap;
import util.UtilidadFecha;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * Forma para manejo presentaci�n de la funcionalidad 
 * Cupos Extras.
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 08 /May/ 2006
 */
public class CuposExtraForm extends ActionForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaCuposExtras;
	
	/**
	 * almacena el numero de filas en el HashMap mapaCuposExtras
	 */
	private int numeroElementos;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;

     /**
      * Poscicion del mapa en la consulta de facturas
      */
     private int posicionMapa;
     
     /**
      * String de la fecha inicial para la busqueda 
      */
     private String fechaInicial;
     
     /**
      * String de la fehca final para la busqueda
      */
     private String fechaFinal;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * Usuario que realizo la factura
 	 */
 	private String usuario;
 	
 	/**
 	 * Codigo de la unida de consulta a buscar
 	 */
 	private int codigoUnidadConsulta;
 	
 	/**
 	 * Codigo del consultorio a buscar
 	 */
 	private int codigoConsultorio;
 	
 	/**
 	 * Codigo del dia de la semana a buscar
 	 */
 	private int codigoDiaSemana;
 	
 	/**
 	 * codigo del medico a buscar
 	 */
 	private int codigoMedico;
 	
 	/**
 	 * Mensaje de culminacion del proceso (Erroneo)
 	 */
 	private String mensajeCulminacion;
 	
 	/**
 	 * String con el nombre del profesional
 	 */
 	private String nombreProfesional;
 	
 	/**
 	 * String para el mensaje exitoso de procesos
 	 */
 	private String mensajeExitoso;
 	
 	
 	/**
 	 * variable para almacenar el centro de antencion  
 	 */
 	private int centroAtencion;
 	
 	/**
 	 * Variable para almacenar el Centro de Atencion.
 	 */
 	private String nombreCentroAtencion;
 	
 	/**
	 * Listado de centros de atencion y unidades de agenda autorizados para el usuario
	 */
	private HashMap unidadAgendaMap;
	
	private String centrosAtencion;
	
	private String unidadesAgenda;
	
	/**
	 * Centros de atencion validos para el usuario
	 */
	private HashMap centrosAtencionAutorizados;
	
	/**
	 * Unidades de agenda validas para el usuario
	 */
	private HashMap unidadesAgendaAutorizadas;
	
 	/**
 	 * Reset generla de la Forma
 	 */
	public void reset ()
	{
		this.mapaCuposExtras = new HashMap ();
		this.estado = "";
		this.linkSiguiente = "";
		this.fechaFinal = "";
		this.fechaInicial = "";
	 	this.usuario = "";
	 	this.codigoUnidadConsulta = 0;
	 	this.codigoConsultorio = 0;
	 	this.codigoDiaSemana = 0;
	 	this.codigoMedico = 0;
	 	this.mensajeCulminacion = "";
	 	this.nombreProfesional = "";
	 	this.mensajeExitoso = "";
	 	
	 	this.centroAtencion = 0;
	 	this.nombreCentroAtencion = "";	
	 
	 	this.unidadAgendaMap=new HashMap();
	 	this.unidadAgendaMap.put("numRegistros", "0");
	 	this.centrosAtencion="";
	 	this.unidadesAgenda="";
	 	this.centrosAtencionAutorizados=new HashMap();
		this.centrosAtencionAutorizados.put("numRegistros", "0");
		this.unidadesAgendaAutorizadas=new HashMap();
		this.unidadesAgendaAutorizadas.put("numRegistros", "0");
	}
	
	
	/**
	 * Reset �nico para los mapas
	 */
	public void resetMapa()
	{
		this.mapaCuposExtras = new HashMap ();
	}
	
	
	
	
	/**
	 * @return Returns the mensajeExitoso.
	 */
	public String getMensajeExitoso()
	{
		return mensajeExitoso;
	}


	/**
	 * @param mensajeExitoso The mensajeExitoso to set.
	 */
	public void setMensajeExitoso(String mensajeExitoso)
	{
		this.mensajeExitoso=mensajeExitoso;
	}


	/**
	 * @return Returns the nombreProfesional.
	 */
	public String getNombreProfesional()
	{
		return nombreProfesional;
	}


	/**
	 * @param nombreProfesional The nombreProfesional to set.
	 */
	public void setNombreProfesional(String nombreProfesional)
	{
		this.nombreProfesional=nombreProfesional;
	}


	/**
	 * @return Returns the mensajeCulminacion.
	 */
	public String getMensajeCulminacion()
	{
		return mensajeCulminacion;
	}


	/**
	 * @param mensajeCulminacion The mensajeCulminacion to set.
	 */
	public void setMensajeCulminacion(String mensajeCulminacion)
	{
		this.mensajeCulminacion=mensajeCulminacion;
	}


	/**
	 * @return Returns the codigoConsultorio.
	 */
	public int getCodigoConsultorio()
	{
		return codigoConsultorio;
	}


	/**
	 * @param codigoConsultorio The codigoConsultorio to set.
	 */
	public void setCodigoConsultorio(int codigoConsultorio)
	{
		this.codigoConsultorio=codigoConsultorio;
	}


	/**
	 * @return Returns the codigoDiaSemana.
	 */
	public int getCodigoDiaSemana()
	{
		return codigoDiaSemana;
	}


	/**
	 * @param codigoDiaSemana The codigoDiaSemana to set.
	 */
	public void setCodigoDiaSemana(int codigoDiaSemana)
	{
		this.codigoDiaSemana=codigoDiaSemana;
	}


	/**
	 * @return Returns the codigoMedico.
	 */
	public int getCodigoMedico()
	{
		return codigoMedico;
	}


	/**
	 * @param codigoMedico The codigoMedico to set.
	 */
	public void setCodigoMedico(int codigoMedico)
	{
		this.codigoMedico=codigoMedico;
	}


	/**
	 * @return Returns the codigoUnidadConsulta.
	 */
	public int getCodigoUnidadConsulta()
	{
		return codigoUnidadConsulta;
	}


	/**
	 * @param codigoUnidadConsulta The codigoUnidadConsulta to set.
	 */
	public void setCodigoUnidadConsulta(int codigoUnidadConsulta)
	{
		this.codigoUnidadConsulta=codigoUnidadConsulta;
	}


	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario)
	{
		this.usuario=usuario;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}
	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal= fechaFinal;
	}
	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}
	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial= fechaInicial;
	}
	/**
	
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}
	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa= posicionMapa;
	}
	
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente= linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset= offset;
	}
	
	
    /**
     * @return Retorna el estado.
     */
    public  String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
	/**
	 * @return Returns the mapaCuposExtras.
	 */
	public HashMap getMapaCuposExtras()
	{
		return mapaCuposExtras;
	}
	
	/**
	 * @param mapaCuposExtras The mapaCuposExtras to set.
	 */
	public void setMapaCuposExtras(HashMap mapaCuposExtras)
	{
		this.mapaCuposExtras = mapaCuposExtras;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCuposExtras(String key) 
	{
		return mapaCuposExtras.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCuposExtras(String key, Object value) 
	{
		mapaCuposExtras.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getCuposExtras(String key) 
	{
		return mapaCuposExtras.get(key);
	}

	
	/**
	 * @return Returns the numeroElementos.
	 */
	public int getNumeroElementos()
	{
		return numeroElementos;
	}
	/**
	 * @param numeroElementos The numeroElementos to set.
	 */
	public void setNumeroElementos(int numeroElementos)
	{
		this.numeroElementos= numeroElementos;
	}
	
	 /**
     * @return Retorna patronOrdenar.
     */
    public String getPatronOrdenar()
    {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar Asigna patronOrdenar.
     */
    public void setPatronOrdenar(String patronOrdenar) 
    {
        this.patronOrdenar = patronOrdenar;
    }
	
    /**
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() 
	{
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) 
	{
		this.ultimoPatron = ultimoPatron;
	}
	
	
	/**
	 * Funci�n de validaci�n: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		
		/*********************************************************************************/
		if(estado.equals("resultadoBusqueda"))
		{
			int nroMeses = 0 ;
			if(this.getCodigoMedico()==-1)
			{
				errores.add("errors.required", new ActionMessage("errors.required", "El Profesional de la Salud"));
			}
			if(!this.getFechaInicial().trim().equals(""))
			{
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))<0)
				{
					errores.add("Fecha Inicial mayor a la fecha del Sistema", new ActionMessage("errors.fechaAnteriorIgualActual","Inicial", " Actual"));
				}
				if(!UtilidadFecha.validarFecha(this.getFechaInicial()))
				{
					errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
				}
			}
			if(!this.getFechaFinal().trim().equals(""))
			{
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))<0)
				{
					errores.add("Fecha Final mayor a la fecha del Sistema", new ActionMessage("errors.fechaAnteriorIgualActual","Final", " Actual"));
				}
				if(!UtilidadFecha.validarFecha(this.getFechaFinal()))
				{
					errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
				}
			}
			if(!this.getFechaInicial().trim().equals("") && !this.getFechaFinal().trim().equals(""))
			{
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial()))<0)
				{
					errores.add("Fecha Final menor a Fecha Incial", new ActionMessage("errors.fechaAnteriorIgualActual","Final", " Inicial"));				
				}
			}
			if((!this.getFechaInicial().equals(""))&&(this.getFechaFinal().equals("")))
			{
				errores.add("Definir Fecha  Final", new ActionMessage("error.cuposExtras.rangosFechaFinalNoDefinidos","Definir Fecha Final"));
			}
			if((this.getFechaInicial().trim().equals("")&&!this.getFechaFinal().trim().equals("")))
			{
				errores.add("Definir Fecha Inicial", new ActionMessage("error.cuposExtras.rangosFechaInicialNoDefinidos","Definir Fecha Inicial"));
			}
			if((!this.getFechaInicial().trim().equals("")&&!this.getFechaFinal().trim().equals("")))
			{
				nroMeses = UtilidadFecha.numeroMesesEntreFechas(this.getFechaInicial(), this.getFechaFinal(),true);
				//Se le incrementa uno pues la funcion devuelve un numero menos de meses
				//nroMeses =  nroMeses +1;
				if (nroMeses > 1)
				{
					errores.add("rango consulta mayor", new ActionMessage("error.cuposExtras.rangoMayorUnMes"));
				}
			}
		}
		return errores;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}


	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}


	/**
	 * @return the unidadAgendaMap
	 */
	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(HashMap unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}
	
	/**
	 * @return the unidadAgendaMap
	 */
	public Object getUnidadAgendaMap(String llave) {
		return unidadAgendaMap.get(llave);
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(String llave, Object obj) {
		this.unidadAgendaMap.put(llave, obj);
	}


	/**
	 * @return the centrosAtencion
	 */
	public String getCentrosAtencion() {
		return centrosAtencion;
	}


	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(String centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	/**
	 * @return the unidadesAgenda
	 */
	public String getUnidadesAgenda() {
		return unidadesAgenda;
	}


	/**
	 * @param unidadesAgenda the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(String unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public HashMap getCentrosAtencionAutorizados() {
		return centrosAtencionAutorizados;
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(HashMap centrosAtencionAutorizados) {
		this.centrosAtencionAutorizados = centrosAtencionAutorizados;
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public Object getCentrosAtencionAutorizados(String llave) {
		return centrosAtencionAutorizados.get(llave);
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(String llave, Object obj) {
		this.centrosAtencionAutorizados.put(llave, obj);
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public HashMap getUnidadesAgendaAutorizadas() {
		return unidadesAgendaAutorizadas;
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(HashMap unidadesAgendaAutorizadas) {
		this.unidadesAgendaAutorizadas = unidadesAgendaAutorizadas;
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public Object getUnidadesAgendaAutorizadas(String llave) {
		return unidadesAgendaAutorizadas.get(llave);
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(String llave, Object obj) {
		this.unidadesAgendaAutorizadas.put(llave, obj);
	}
	
	
}
