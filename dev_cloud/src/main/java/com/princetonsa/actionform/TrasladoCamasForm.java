/*
 * @(#)ConsultaFacturasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;



/**
 * Forma para manejo presentación de la funcionalidad 
 * Traslado de Camas. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 06 /Jul/ 2005
 */
public class TrasladoCamasForm extends ValidatorForm
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(TrasladoCamasForm.class);
	
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	
	/**
	 * Fecha Y Hora de Traslado de la Cama
	 */
	private String fechaHoraTraslado;
	
	
	
	/**
	 * String para la cama en el JSp
	 */
	private String cama;
	
	

	/**
	 * String con la cama antigua que tenia asignada el paciente
	 */
	private String camaAntigua;
	
	/**
	 * String del centro de costo al cual pertenece la cama
	 */
	private String centroCosto;
	
	
	
	/**
	 * Boolean para saber si la cama es de Uci
	 */
	private boolean esUciBoolen;
	
	/**
	 * String con el esatdo de la cama
	 */
	private String estadoCama="";
	
	/**
	 * String para la Fecha-Hora de Grabacion del Traslado de Cama
	 */
	private String fechaHoraGrabacion;
	
	/**
	 * Strng con el Login del Usuario que grabo el Traslado de Cama
	 */
	private String loginUsuario;
	
	/**
	 * almacena los datos de la consulta de la cam actual del paciente
	 */
	private HashMap mapaCamaActualPaciente;
	
	/**
	 * almacena los datos de la consulta de la cam nueval del paciente
	 */
	private HashMap mapaCamaNuevaPaciente;
	
	/**
	 * almacena los datos de la consulta los traslados de area
	 */
	private HashMap mapaTrasladosArea;
	
	
	/**
	 * Almacena los datos de la consulta de los traslados por fecha
	 */
	private HashMap mapaTrasladosFecha;

	/**
	 * Alamcena los datos de la consulta del detalle de un traslado por fecha
	 */
	private HashMap mapaDetalleTrasladoFecha;
	
	
	
	/**
	 * Almacena los datos de la consulta de los traslados anteriores de un paciente
	 */
	private HashMap mapaTrasladosAnterioresPaciente;
	
	/**
	 * Almacena los datos de la consulta de los traslados de un paciente
	 */
	private HashMap mapaTrasladosPaciente;
	
	/**
	 * Alamacena el detalle de un traslado de un paciente
	 */
	private HashMap mapaDetalleTrasladoPaciente;
	
	/**
	 * almacena el numero de filas en el HashMap mapaActualizacion
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
	 * String del responsable de la factura
	 */
	private String responsable;
	
	/**
	 * Via de Ingreso
	 */
	private String viaIngreso;
	
	/**
	 * Estado de la factura 
	 */
	private String estadoFactura;
	
	/**
	 * Esatdo del paciente con respecto a la Factura
	 */
	private String estadoPaciente;
	
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
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * String para la fecha del traslado de camas cuando se ingresa a la consulta por fecha
 	 */
 	private String fechaTraslado;
 	
 	/**
 	 * String con la hora de traslado
 	 */
 	private String horaTraslado;
 	
 	
 	/**
	 * Código del centro de costo de la cama
	 */
	private int codigoCentroCostoCama=0;
	
	/**
	 * Codigo del paciente al que se le hace el traslado
	 */
	private int codigoPaciente=0;
	
	/**
	 * Fecha de admision del paciente de hospitalizacion
	 */
	private String fechaAdmision="";
	
	
	/**
	 * Atributo que indica de donde ha sido llamada la aplicacion
	 */
	private String origenLlamado; 
	
	/**
	 * atributo que indica si se debe cocultar el 
	 * encabezado o no
	 */
	private boolean ocultarEncabezado;
	


	/**
	 * Cama si la tenia registrada
	 */
	
	private String camaReservada="";
	
	//********ATRIBUTOS PARA LA VALIDACION DE CENTRO ATENCION*********
	private int codigoCentroAtencion;
	
	/**
	 * Indica si la funcionalidad ha sido llamada desde un flujo externo
	 */
	private boolean llamadoExterno;
	
	public void reset ()
	{
		this.mapaTrasladosArea = new HashMap ();
		this.mapaCamaActualPaciente = new HashMap ();
		this.mapaCamaNuevaPaciente = new HashMap ();
		this.mapaTrasladosFecha= new HashMap();
		this.mapaDetalleTrasladoFecha= new HashMap();
		this.mapaTrasladosAnterioresPaciente=new HashMap();
		this.mapaTrasladosPaciente= new HashMap();
		this.mapaDetalleTrasladoPaciente = new HashMap();
		this.estado="";
		this.cuenta=0;
		this.linkSiguiente="";
		this.fechaHoraTraslado="";
		this.responsable="";
		this.viaIngreso="";
		this.estadoFactura="";
		this.estadoPaciente="";
		this.cama="-1";
		this.centroCosto="";
		this.camaAntigua="";
		this.fechaTraslado=UtilidadFecha.getFechaActual();
		this.codigoPaciente=0;
		this.fechaAdmision="";
		this.camaReservada="";
		this.origenLlamado="";
		this.ocultarEncabezado=false;
		this.codigoCentroAtencion = 0;
		this.llamadoExterno=false;
		
	}
	
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaTrasladosArea = new HashMap ();
		this.mapaCamaActualPaciente = new HashMap ();
		this.mapaTrasladosFecha= new HashMap();
		this.mapaDetalleTrasladoFecha= new HashMap();
		this.mapaTrasladosAnterioresPaciente= new HashMap ();
		this.mapaTrasladosPaciente=new HashMap();
		this.mapaDetalleTrasladoPaciente=new HashMap();
	}
	
	
	public void resetpager()
	{
		
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
		
	}
	
	
	/**
	 * @return Returns the fechaAdmision.
	 */
	public String getFechaAdmision()
	{
		return fechaAdmision;
	}


	/**
	 * @param fechaAdmision The fechaAdmision to set.
	 */
	public void setFechaAdmision(String fechaAdmision)
	{
		this.fechaAdmision=fechaAdmision;
	}


	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPaciente()
	{
		return codigoPaciente;
	}


	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(int codigoPaciente)
	{
		this.codigoPaciente=codigoPaciente;
	}


	/**
	 * @return Returns the cama.
	 */
	public String getCama()
	{
		return cama;
	}
	/**
	 * @param cama The cama to set.
	 */
	public void setCama(String cama)
	{
		this.cama= cama;
	}
	/**
	 * @return Returns the horaTraslado.
	 */
	public String getHoraTraslado()
	{
		return horaTraslado;
	}
	/**
	 * @param horaTraslado The horaTraslado to set.
	 */
	public void setHoraTraslado(String horaTraslado)
	{
		this.horaTraslado= horaTraslado;
	}
	/**
	 * @return Returns the camaAntigua.
	 */
	public String getCamaAntigua()
	{
		return camaAntigua;
	}
	/**
	 * @param camaAntigua The camaAntigua to set.
	 */
	public void setCamaAntigua(String camaAntigua)
	{
		this.camaAntigua= camaAntigua;
	}
	/**
	 * @return Returns the fechaTraslado.
	 */
	public String getFechaTraslado()
	{
		return fechaTraslado;
	}
	/**
	 * @param fechaTraslado The fechaTraslado to set.
	 */
	public void setFechaTraslado(String fechaTraslado)
	{
		this.fechaTraslado= fechaTraslado;
	}
	/**
	 * @return Returns the codigoCentroCostoCama.
	 */
	public int getCodigoCentroCostoCama()
	{
		return codigoCentroCostoCama;
	}
	/**
	 * @param codigoCentroCostoCama The codigoCentroCostoCama to set.
	 */
	public void setCodigoCentroCostoCama(int codigoCentroCostoCama)
	{
		this.codigoCentroCostoCama= codigoCentroCostoCama;
	}
	/**
	 * @return Returns the estadoCama.
	 */
	public String getEstadoCama()
	{
		return estadoCama;
	}
	/**
	 * @param estadoCama The estadoCama to set.
	 */
	public void setEstadoCama(String estadoCama)
	{
		this.estadoCama= estadoCama;
	}
	
	/**
	 * @return Returns the centroCosto.
	 */
	public String getCentroCosto()
	{
		return centroCosto;
	}
	/**
	 * @param centroCosto The centroCosto to set.
	 */
	public void setCentroCosto(String centroCosto)
	{
		this.centroCosto= centroCosto;
	}
	
	/**
	 * @return Returns the esUciBoolen.
	 */
	public boolean isEsUciBoolen()
	{
		return esUciBoolen;
	}
	/**
	 * @param esUciBoolen The esUciBoolen to set.
	 */
	public void setEsUciBoolen(boolean esUciBoolen)
	{
		this.esUciBoolen= esUciBoolen;
	}
	/**
	 * @return Returns the fechaHoraGrabacion.
	 */
	public String getFechaHoraGrabacion()
	{
		return fechaHoraGrabacion;
	}
	/**
	 * @param fechaHoraGrabacion The fechaHoraGrabacion to set.
	 */
	public void setFechaHoraGrabacion(String fechaHoraGrabacion)
	{
		this.fechaHoraGrabacion= fechaHoraGrabacion;
	}
	
	
	/**
	 * @return Returns the loginUsuario.
	 */
	public String getLoginUsuario()
	{
		return loginUsuario;
	}
	/**
	 * @param loginUsuario The loginUsuario to set.
	 */
	public void setLoginUsuario(String loginUsuario)
	{
		this.loginUsuario= loginUsuario;
	}
	
	/**
	 * @param mapaTrasladosArea The mapaTrasladosArea to set.
	 */
	public void setMapaTrasladosArea(HashMap mapaTrasladosArea)
	{
		this.mapaTrasladosArea= mapaTrasladosArea;
	}
	/**
	 * @return Returns the estadoFactura.
	 */
	public String getEstadoFactura()
	{
		return estadoFactura;
	}
	/**
	 * @param estadoFactura The estadoFactura to set.
	 */
	public void setEstadoFactura(String estadoFactura)
	{
		this.estadoFactura= estadoFactura;
	}
	/**
	 * @return Returns the estadoPaciente.
	 */
	public String getEstadoPaciente()
	{
		return estadoPaciente;
	}
	/**
	 * @param estadoPaciente The estadoPaciente to set.
	 */
	public void setEstadoPaciente(String estadoPaciente)
	{
		this.estadoPaciente= estadoPaciente;
	}
	/**
	 * @return Returns the responsable.
	 */
	public String getResponsable()
	{
		return responsable;
	}
	/**
	 * @param responsable The responsable to set.
	 */
	public void setResponsable(String responsable)
	{
		this.responsable= responsable;
	}
	/**
	 * @return Returns the viaIngreso.
	 */
	public String getViaIngreso()
	{
		return viaIngreso;
	}
	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(String viaIngreso)
	{
		this.viaIngreso= viaIngreso;
	}
	
	
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
	 * @return Returns the fechaHoraTraslado
	 */
	public String getFechaHoraTraslado()
	{
		return fechaHoraTraslado;
	}
	/**
	 * @param fechaHoraElaboracion The fechaHoraTraslado to set.
	 */
	public void setFechaHoraTraslado(String fechaHoraTraslado)
	{
		this.fechaHoraTraslado= fechaHoraTraslado;
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
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
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
	 * @return Returns the mapaTrasladosArea.
	 */
	public HashMap getMapaTrasladosArea()
	{
		return mapaTrasladosArea;
	}
	
	
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTrasladosArea(String key, Object value) 
	{
		mapaTrasladosArea.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTrasladosArea(String key) 
	{
		return mapaTrasladosArea.get(key);
	}
	
	/**
	 * @return Returns the mapaCamaActualPaciente.
	 */
	public HashMap getMapaCamaActualPaciente()
	{
		return mapaCamaActualPaciente;
	}
	
	/**
	 * @param mapaFacturasPaciente The mapaCamaActualPaciente to set.
	 */
	public void setMapaCamaActualPaciente(HashMap mapaCamaActualPaciente)
	{
		this.mapaCamaActualPaciente= mapaCamaActualPaciente;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCamaActualPaciente(String key, Object value) 
	{
		mapaCamaActualPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCamaActualPaciente(String key) 
	{
		return mapaCamaActualPaciente.get(key);
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
    public String getPatronOrdenar() {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar Asigna patronOrdenar.
     */
    public void setPatronOrdenar(String patronOrdenar) {
        this.patronOrdenar = patronOrdenar;
    }
	
    /**
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	
	/**
	 * @return Returns the mapaTrasladosFecha.
	 */
	public HashMap getMapaTrasladosFecha()
	{
		return mapaTrasladosFecha;
	}
	
	/**
	 * @param mapaTrasladosFecha The mapaTrasladosFecha to set.
	 */
	public void setMapaTrasladosFecha(HashMap mapaTrasladosFecha)
	{
		this.mapaTrasladosFecha= mapaTrasladosFecha;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTrasladosFecha(String key, Object value) 
	{
		mapaTrasladosFecha.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTrasladosFecha(String key) 
	{
		return mapaTrasladosFecha.get(key);
	}
	
	/**
	 * @return Returns the mapaDetalleTrasladoFecha.
	 */
	public HashMap getMapaDetalleTrasladoFecha()
	{
		return mapaDetalleTrasladoFecha;
	}
	
	/**
	 * @param mapaDetalleTrasladoFecha The mapaDetalleTrasladoFecha to set.
	 */
	public void setMapaDetalleTrasladoFecha(HashMap mapaDetalleTrasladoFecha)
	{
		this.mapaDetalleTrasladoFecha= mapaDetalleTrasladoFecha;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleTrasladoFecha(String key, Object value) 
	{
		mapaDetalleTrasladoFecha.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleTrasladoFecha(String key) 
	{
		return mapaDetalleTrasladoFecha.get(key);
	}
	
		
	
	/**
	 * @return Returns the mapaTrasladosAnterioresPaciente.
	 */
	public HashMap getMapaTrasladosAnterioresPaciente()
	{
		return mapaTrasladosAnterioresPaciente;
	}
	
	/**
	 * @param mapaTrasladosAnterioresPaciente The mapaTrasladosAnterioresPaciente to set.
	 */
	public void setMapaTrasladosAnterioresPaciente(HashMap mapaTrasladosAnterioresPaciente)
	{
		this.mapaTrasladosAnterioresPaciente= mapaTrasladosAnterioresPaciente;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTrasladosAnterioresPaciente(String key, Object value) 
	{
		mapaTrasladosAnterioresPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTrasladosAnterioresPaciente(String key) 
	{
		return mapaTrasladosAnterioresPaciente.get(key);
	}
	
	/**
	 * @return Returns the mapaTrasladosPaciente.
	 */
	public HashMap getMapaTrasladosPaciente()
	{
		return mapaTrasladosPaciente;
	}
	
	/**
	 * @param mapaTrasladosPaciente The mapaTrasladosPaciente to set.
	 */
	public void setMapaTrasladosPaciente(HashMap mapaTrasladosPaciente)
	{
		this.mapaTrasladosPaciente= mapaTrasladosPaciente;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTrasladosPaciente(String key, Object value) 
	{
		mapaTrasladosPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTrasladosPaciente(String key) 
	{
		return mapaTrasladosPaciente.get(key);
	}
	
	/**
	 * @return Returns the mapaDetalleTrasladoPaciente.
	 */
	public HashMap getMapaDetalleTrasladoPaciente()
	{
		return mapaDetalleTrasladoPaciente;
	}
	
	/**
	 * @param mapaDetalleTrasladoPaciente The mapaDetalleTrasladoPaciente to set.
	 */
	public void setMapaDetalleTrasladoPaciente(HashMap mapaDetalleTrasladoPaciente)
	{
		this.mapaDetalleTrasladoPaciente= mapaDetalleTrasladoPaciente;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleTrasladoPaciente(String key, Object value) 
	{
		mapaDetalleTrasladoPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleTrasladoPaciente(String key) 
	{
		return mapaDetalleTrasladoPaciente.get(key);
	}
	
	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		
		logger.info("ESTADO EN EL FORM=> "+this.estado);
		
		//*********************************************************************************
		if(estado.equals("guardarPaciente")||estado.equals("guardarTrasladoArea"))
		{
			boolean fechaValida = true;
			boolean horaValida = true;
			
			/**se verifica que las fechas sean válidas**/
			if(this.getFechaTraslado().equals(""))
			{
				fechaValida = false;
				errores.add("La Fecha de Traslado", new ActionMessage("errors.required", "La Fecha de Traslado"));
			}
			if(fechaValida&&!UtilidadFecha.validarFecha(this.getFechaTraslado()))
			{
				fechaValida = false;
				errores.add("fecha traslado", new ActionMessage("errors.formatoFechaInvalido",this.getFechaTraslado()));
			}
			if(this.getHoraTraslado().equals(""))
			{
				horaValida = false;
				errores.add("La Hora de Traslado", new ActionMessage("errors.required", "La Hora de Traslado"));
			}
			if(fechaValida&&(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado()))>0)
			{				
				errores.add("Fecha Nota", new ActionMessage("errors.fechaAnteriorIgualActual"," del Traslado ", " de la Admisión"));
			}
			if(fechaValida&&(UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))>0)
			{
				errores.add("Fecha Notas mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual"," del Traslado ", " Actual"));
			}
			if(fechaValida&&horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(),this.getFechaTraslado(), this.getHoraTraslado()).isTrue())
			{
				errores.add("Hora", new ActionMessage("errors.horaPosteriorIgualAOtraDeReferencia", "del Traslado", "del Sistema"));
			}
			
			if (this.camaReservada.equals(""))
				if(this.getCama().equals("-1")||this.getCama().equals(""))
					errores.add("La nueva cama es requerida", new ActionMessage("errors.seleccion", "nueva cama"));
			
	        try
	        {
	        	if(fechaValida&&horaValida)
	        	{
			        java.sql.Connection con=UtilidadBD.abrirConexion();
	
			        int codigoCama=Integer.parseInt(this.getCama());
			        boolean camaOcupada=UtilidadValidacion.esCamaOcupadaRangoFechaHoraMayorIgualDado(con, UtilidadFecha.conversionFormatoFechaABD(this.fechaTraslado), this.horaTraslado,  codigoCama);
			        if(camaOcupada)
			        {
			            errores.add("cama ocupada", new ActionMessage("error.trasladocama.camaOcupada"));
			        }
			        if(UtilidadValidacion.pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(con, UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado()),this.getHoraTraslado(), this.getCuenta()))
					{
						errores.add("existe traslado posterior", new ActionMessage("error.trasladocama.existeTrasladoAnterior", this.fechaHoraTraslado, this.horaTraslado));
					}
			        UtilidadBD.cerrarConexion(con);
	        	}
	        }
	        catch (SQLException sqle)
	        {
	        	sqle.printStackTrace();
	        }
		}
		return errores;
	}




	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}


	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}


	/**
	 * @return the mapaCamaNuevaPaciente
	 */
	public HashMap getMapaCamaNuevaPaciente() {
		return mapaCamaNuevaPaciente;
	}


	/**
	 * @param mapaCamaNuevaPaciente the mapaCamaNuevaPaciente to set
	 */
	public void setMapaCamaNuevaPaciente(HashMap mapaCamaNuevaPaciente) {
		this.mapaCamaNuevaPaciente = mapaCamaNuevaPaciente;
	}
	
	/**
	 * @return the mapaCamaNuevaPaciente
	 */
	public Object getMapaCamaNuevaPaciente(String key) {
		return mapaCamaNuevaPaciente.get(key);
	}


	/**
	 * @param mapaCamaNuevaPaciente the mapaCamaNuevaPaciente to set
	 */
	public void setMapaCamaNuevaPaciente(String key,Object obj) {
		this.mapaCamaNuevaPaciente.put(key,obj);
	}


	public String getCamaReservada() {
		return camaReservada;
	}


	public void setCamaReservada(String camaReservada) {
		this.camaReservada = camaReservada;
	}


	public String getOrigenLlamado() {
		return origenLlamado;
	}


	public void setOrigenLlamado(String origenLlamado) {
		this.origenLlamado = origenLlamado;
	}


	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}


	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}


	/**
	 * @return Retorna llamadoExterno
	 */
	public boolean isLlamadoExterno() {
		return llamadoExterno;
	}


	/**
	 * @param llamadoExterno Asigna el atributo llamadoExterno
	 */
	public void setLlamadoExterno(boolean llamadoExterno) {
		this.llamadoExterno = llamadoExterno;
	}


	
}
