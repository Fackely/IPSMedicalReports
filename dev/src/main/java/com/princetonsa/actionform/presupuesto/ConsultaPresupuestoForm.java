/*
 * @(#)ConsultaPresupuestoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.presupuesto;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Consulta /  Impresióin de Presupuesto. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 17 /Ene/ 2006
 */
public class ConsultaPresupuestoForm extends ActionForm
{
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Entero con el codigo de presupuesto con el que se trabaja
	 */
	private int codigoPresupuesto;
	
	/**
	 * Descripcion del Servicio
	 */
	private String  descripcionServicio;
	
	/**
	 * Fecha Y Hora de elaboracion De la Factura
	 */
	private String fechaHoraElaboracion;
	
	/**
	 * almacena los datos de la consulta para la seccion de servicios
	 */
	private HashMap mapaServicios;
	
	/**
	 * almacena los datos de la consulta para la seccion de articulos
	 */
	private HashMap mapaArticulos;
	
	/**
	 * almacena los datos de la consulta de un presupuesto en especifico
	 */
	private HashMap mapaDetallePresupuesto;
	
	/**
	 * Mapa para los tipos de Id
	 */
	private HashMap mapaTipoId;
	
	/**
	 * Mapa para los medicos tratantes
	 */
	private HashMap mapaMedicos;
	
	/**
	 * Mapa para las intervenciones
	 */
	private HashMap mapaIntervenciones;
	
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
      * Mapa para los presupuestos en la busqueda Avanzada
      */
     private HashMap mapaPresupuestos;
     
     /**
      * String de la fecha inicial para la busqueda de Presupuestos
      */
     private String fechaElaboracionInicial;
     
     /**
      * String de la fehca final para la busqueda de Presupuestos
      */
     private String fechaElaboracionFinal;
     
     /**
      * int para el numero de presupuesto en la busqueda de Presupuestos
      */
     private int presupuestoInicial;
     
     /**
      * int para el numero de presupuestoFinal de la busqueda de Presupuestos
      */
     private int presupuestoFinal;
     
          
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempResponsable;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempTipoId;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempMedico;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * El codigo del medico a buscar
 	 */
 	private int codigoMedico;
 	
 	/**
 	 * String para el tipo de Id de paciente a buscar
 	 */
 	private String tipoId;
 	
 	/**
 	 *  Entero con el número id del paciente
 	 */
 	private int numeroId;
 	
 	/**
 	 * Double para almacenar el total de un presupuesto
 	 */
 	private double totalPresupuesto;
 	
 	/**
 	 * Mapa con los formatos de impresión existentes
 	 */
 	private HashMap mapaFormatosImpresion;
 	
 	/**
 	 * Entero con el codigo de formato por el cual se va a imprimir
 	 */
 	private int codigoFormato;
 	
 	/**
 	 * 
 	 */
 	private int temporal;
 	
 	/**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    /**
     * 
     */
    private String factorConversionMoneda;
    
    /**
     * 
     */
    private String totalPresupuestoMon;
 	
 	/**
 	 * Metodo para hacer rest de todos los atributos del formulario
 	 */
	public void reset (int codigoInstitucion)
	{
		this.mapaDetallePresupuesto = new HashMap ();
		this.mapaTipoId = new HashMap ();
		this.mapaMedicos = new HashMap ();
		this.mapaIntervenciones = new HashMap ();
		this.mapaFormatosImpresion = new HashMap ();
		this.mapaServicios = new HashMap ();
		this.mapaArticulos = new HashMap ();
		this.mapaPresupuestos = new HashMap ();
		this.propiedadTempResponsable=-1;
		this.estado="";
		this.cuenta=0;
		this.linkSiguiente="";
		this.fechaHoraElaboracion="";
		this.descripcionServicio="";
		this.presupuestoFinal=0;
		this.presupuestoInicial=0;
		this.fechaElaboracionFinal="";
		this.fechaElaboracionInicial="";
		this.responsable="";
		this.tipoId="";
		this.codigoMedico=0;
		this.codigoPresupuesto=0;
		this.numeroId=0;
		this.totalPresupuesto=0;
		this.codigoFormato=0;
		this.temporal=0;
		this.index=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.inicializarTagMap(codigoInstitucion);
        this.factorConversionMoneda="";
        this.totalPresupuestoMon="";
		
	}
	
	/**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }
	
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaDetallePresupuesto = new HashMap ();
		this.mapaTipoId = new HashMap ();
		this.mapaMedicos = new HashMap ();
		this.mapaFormatosImpresion = new HashMap ();
		this.mapaIntervenciones = new HashMap ();
		this.mapaServicios = new HashMap ();
		this.mapaArticulos = new HashMap ();
		this.mapaPresupuestos = new HashMap ();
	}
	
	
	
	
	
	/**
	 * @return Returns the temporal.
	 */
	public int getTemporal()
	{
		return temporal;
	}
	/**
	 * @param temporal The temporal to set.
	 */
	public void setTemporal(int temporal)
	{
		this.temporal=temporal;
	}
	/**
	 * @return Returns the codigoFormato.
	 */
	public int getCodigoFormato()
	{
		return codigoFormato;
	}
	/**
	 * @param codigoFormato The codigoFormato to set.
	 */
	public void setCodigoFormato(int codigoFormato)
	{
		this.codigoFormato=codigoFormato;
	}
	/**
	 * @return Returns the totalPresupuesto.
	 */
	public double getTotalPresupuesto()
	{
		return totalPresupuesto;
	}
	/**
	 * @param totalPresupuesto The totalPresupuesto to set.
	 */
	public void setTotalPresupuesto(double totalPresupuesto)
	{
		this.totalPresupuesto=totalPresupuesto;
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
	 * @return Returns the numeroId.
	 */
	public int getNumeroId()
	{
		return numeroId;
	}
	/**
	 * @param numeroId The numeroId to set.
	 */
	public void setNumeroId(int numeroId)
	{
		this.numeroId=numeroId;
	}
	/**
	 * @return Returns the codigoPresupuesto.
	 */
	public int getCodigoPresupuesto()
	{
		return codigoPresupuesto;
	}
	/**
	 * @param codigoPresupuesto The codigoPresupuesto to set.
	 */
	public void setCodigoPresupuesto(int codigoPresupuesto)
	{
		this.codigoPresupuesto=codigoPresupuesto;
	}
	
	
	/**
	 * @return Returns the tipoId.
	 */
	public String getTipoId()
	{
		return tipoId;
	}
	/**
	 * @param tipoId The tipoId to set.
	 */
	public void setTipoId(String tipoId)
	{
		this.tipoId=tipoId;
	}
	/**
	 * @return Returns the propiedadTempMedico.
	 */
	public int getPropiedadTempMedico()
	{
		return propiedadTempMedico;
	}
	/**
	 * @param propiedadTempMedico The propiedadTempMedico to set.
	 */
	public void setPropiedadTempMedico(int propiedadTempMedico)
	{
		this.propiedadTempMedico=propiedadTempMedico;
	}
	/**
	 * @return Returns the propiedadTempResponsable.
	 */
	public int getPropiedadTempResponsable()
	{
		return propiedadTempResponsable;
	}
	/**
	 * @param propiedadTempResponsable The propiedadTempResponsable to set.
	 */
	public void setPropiedadTempResponsable(int propiedadTempResponsable)
	{
		this.propiedadTempResponsable=propiedadTempResponsable;
	}
	/**
	 * @return Returns the propiedadTempTipoId.
	 */
	public int getPropiedadTempTipoId()
	{
		return propiedadTempTipoId;
	}
	/**
	 * @param propiedadTempTipoId The propiedadTempTipoId to set.
	 */
	public void setPropiedadTempTipoId(int propiedadTempTipoId)
	{
		this.propiedadTempTipoId=propiedadTempTipoId;
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
	 * @return Returns the presupuestoFinal.
	 */
	public int getPresupuestoFinal()
	{
		return presupuestoFinal;
	}
	/**
	 * @param presupuestoFinal The presupuestoFinal to set.
	 */
	public void setPresupuestoFinal(int presupuestoFinal)
	{
		this.presupuestoFinal=presupuestoFinal;
	}
	/**
	 * @return Returns the presupuestoInicial.
	 */
	public int getPresupuestoInicial()
	{
		return presupuestoInicial;
	}
	/**
	 * @param presupuestoInicial The presupuestoInicial to set.
	 */
	public void setPresupuestoInicial(int presupuestoInicial)
	{
		this.presupuestoInicial= presupuestoInicial;
	}
	/**
	 * @return Returns the fechaElaboracionFinal.
	 */
	public String getFechaElaboracionFinal()
	{
		return fechaElaboracionFinal;
	}
	/**
	 * @param fechaElaboracionFinal The fechaElaboracionFinal to set.
	 */
	public void setFechaElaboracionFinal(String fechaElaboracionFinal)
	{
		this.fechaElaboracionFinal= fechaElaboracionFinal;
	}
	/**
	 * @return Returns the fechaElaboracionInicial.
	 */
	public String getFechaElaboracionInicial()
	{
		return fechaElaboracionInicial;
	}
	/**
	 * @param fechaElaboracionInicial The fechaElaboracionInicial to set.
	 */
	public void setFechaElaboracionInicial(String fechaElaboracionInicial)
	{
		this.fechaElaboracionInicial= fechaElaboracionInicial;
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
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio()
	{
		return descripcionServicio;
	}
	/**
	 * @param descripcionServicio The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio)
	{
		this.descripcionServicio= descripcionServicio;
	}
	
	/**
	 * @return Returns the fechaHoraElaboracion
	 */
	public String getFechaHoraElaboracion()
	{
		return fechaHoraElaboracion;
	}
	/**
	 * @param fechaHoraElaboracion The fechaSolicitud to set.
	 */
	public void setFechaHoraElaboracion(String fechaHoraElaboracion)
	{
		this.fechaHoraElaboracion= fechaHoraElaboracion;
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
	 * @return Returns the mapaDetallePresupuesto.
	 */
	public HashMap getMapaDetallePresupuesto()
	{
		return mapaDetallePresupuesto;
	}
	
	/**
	 * @param mapaDetallePresupuesto The mapaDetallePresupuesto to set.
	 */
	public void setMapaDetallePresupuesto(HashMap mapaDetallePresupuesto)
	{
		this.mapaDetallePresupuesto= mapaDetallePresupuesto;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetallePresupuesto(String key, Object value) 
	{
		mapaDetallePresupuesto.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetallePresupuesto(String key) 
	{
		return mapaDetallePresupuesto.get(key);
	}
	
	/**
	 * @return Returns the mapaTipoId.
	 */
	public HashMap getMapaTipoId()
	{
		return mapaTipoId;
	}
	
	/**
	 * @param mapaTipoId The mapaTipoId to set.
	 */
	public void setMapaTipoId(HashMap mapaTipoId)
	{
		this.mapaTipoId= mapaTipoId;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTipoId(String key, Object value) 
	{
		mapaTipoId.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTipoId(String key) 
	{
		return mapaTipoId.get(key);
	}
	
	/**
	 * @return Returns the mapaMedicos.
	 */
	public HashMap getMapaMedicos()
	{
		return mapaMedicos;
	}
	
	/**
	 * @param mapaMedicos The mapaMedicos to set.
	 */
	public void setMapaMedicos(HashMap mapaMedicos)
	{
		this.mapaMedicos= mapaMedicos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaMedicos(String key, Object value) 
	{
		mapaMedicos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaMedicos(String key) 
	{
		return mapaMedicos.get(key);
	}
	
	/**
	 * @return Returns the mapaIntervenciones.
	 */
	public HashMap getMapaIntervenciones()
	{
		return mapaIntervenciones;
	}
	
	/**
	 * @param mapaIntervenciones The mapaIntervenciones to set.
	 */
	public void setMapaIntervenciones(HashMap mapaIntervenciones)
	{
		this.mapaIntervenciones= mapaIntervenciones;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaIntervenciones(String key, Object value) 
	{
		mapaIntervenciones.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaIntervenciones(String key) 
	{
		return mapaIntervenciones.get(key);
	}
	
	/**
	 * @return Returns the mapaServicios.
	 */
	public HashMap getMapaServicios()
	{
		return mapaServicios;
	}
	
	/**
	 * @param mapaServicios The mapaServicios to set.
	 */
	public void setMapaServicios(HashMap mapaServicios)
	{
		this.mapaServicios= mapaServicios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaServicios(String key, Object value) 
	{
		mapaServicios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaServicios(String key) 
	{
		return mapaServicios.get(key);
	}
	
	/**
	 * @return Returns the mapaArticulos.
	 */
	public HashMap getMapaArticulos()
	{
		return mapaArticulos;
	}
	
	/**
	 * @param mapaArticulos The mapaArticulos to set.
	 */
	public void setMapaArticulos(HashMap mapaArticulos)
	{
		this.mapaArticulos= mapaArticulos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaArticulos(String key, Object value) 
	{
		mapaArticulos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaArticulos(String key) 
	{
		return mapaArticulos.get(key);
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
	 * @return Returns the mapaPresupuestos.
	 */
	public HashMap getMapaPresupuestos()
	{
		return mapaPresupuestos;
	}
	
	/**
	 * @param mapaPresupuestos The mapaPresupuestos to set.
	 */
	public void setMapaPresupuestos(HashMap mapaPresupuestos)
	{
		this.mapaPresupuestos= mapaPresupuestos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaPresupuestos(String key, Object value) 
	{
		mapaPresupuestos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaPresupuestos(String key) 
	{
		return mapaPresupuestos.get(key);
	}
	
	
	/**
	 * @return Returns the mapaFormatosImpresion.
	 */
	public HashMap getMapaFormatosImpresion()
	{
		return mapaFormatosImpresion;
	}
	
	/**
	 * @param mapaFormatosImpresion The mapaFormatosImpresion to set.
	 */
	public void setMapaFormatosImpresion(HashMap mapaFormatosImpresion)
	{
		this.mapaFormatosImpresion= mapaFormatosImpresion;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatosImpresion(String key, Object value) 
	{
		mapaFormatosImpresion.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatosImpresion(String key) 
	{
		return mapaFormatosImpresion.get(key);
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
		/*********************************************************************************/
		if(estado.equals("resultadoBusqueda"))
		{
			if(this.getPresupuestoInicial()==0&&this.getPresupuestoFinal()==0&&this.getFechaElaboracionInicial().trim().equals("")&&this.getFechaElaboracionFinal().trim().equals("")&&this.getPropiedadTempResponsable()==-1&&this.getTipoId().trim().equals("")&&this.getNumeroId()==0&&this.getCodigoMedico()==0)
			{
				errores.add("Definir algun campo para la Busqueda", new ActionMessage("error.manejoPaciente.camposBusquedaNoDefinidos","Definir algun campo para la Busqueda"));
			}
			if(this.getPresupuestoInicial()==0&&this.getPresupuestoFinal()==0&&this.getFechaElaboracionInicial().trim().equals("")&&this.getFechaElaboracionFinal().trim().equals("")&&this.getPropiedadTempResponsable()>-1&&this.getTipoId().trim().equals("")&&this.getNumeroId()==0&&this.getCodigoMedico()==0)
			{
				errores.add("Definir algun campo para la Busqueda", new ActionMessage("error.manejoPaciente.camposBusquedaNoValidos","Definir algun campo para la Busqueda"));
			}
			if((this.getPresupuestoInicial()>0&&this.getPresupuestoFinal()==0))
			{
				errores.add("Definir Presupuesto Final", new ActionMessage("error.manejoPaciente.rangosPresupuestoFinalNoDefinidos","Definir Presupuesto Final"));
			}
			if((this.getPresupuestoInicial()==0&&this.getPresupuestoFinal()>0))
			{
				errores.add("Definir Presupuesto Inicial", new ActionMessage("error.manejoPaciente.rangosPresupuestoInicialNoDefinidos","Definir Presupuesto Inicial"));
			}
			if(this.getPresupuestoFinal()<this.getPresupuestoInicial())
			{
				errores.add("Definir Presupuesto Inicial", new ActionMessage("errors.rangoMayorIgual","El Presupuesto Final", "El Presupuesto Inicial"));
			}
			if((!this.getTipoId().trim().equals("")&&((this.getNumeroId()+"").equals("")||(this.getNumeroId()+"").equals("0"))))
			{
				errores.add("Definir Numero Id", new ActionMessage("error.manejoPaciente.definirNumeroId","Definir Número Id "));
			}
			if((this.getTipoId().trim().equals("")&&this.getNumeroId()>0))
			{
				errores.add("Definir Tipo Id", new ActionMessage("errro.manejoPaciente.definirTipoId","Definir Tipo Id "));
			}
			if(this.getPresupuestoInicial()<0)
			{
				errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial de Presupuesto"));
			}
			if(this.getPresupuestoFinal()<0)
			{
				errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final de Presupuesto"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionInicial().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))>0)
			{
				errores.add("Fecha Elaboracion Inicial mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual","La Fecha de Elaboracion Inicial", " Actual"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("Fecha Elaboracion Final mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual","La Fecha de Elaboracion Final", " Actual"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionInicial()))<0)
			{
				errores.add("Fecha Elaboracion Final menor a Fecha Elaboracion Incial", new ActionMessage("errors.fechaPosteriorIgualActual","La Fecha de Elaboracion Final", " de Elaboracion Inicial"));				
			}
			if((!this.getFechaElaboracionInicial().equals(""))&&(this.getFechaElaboracionFinal().equals("")))
			{
				errores.add("Definir Fecha Elaboracion Final", new ActionMessage("error.facturacion.rangosFechaFinalNoDefinidos","Definir Fecha de Elaboracion Final"));
			}
			if((this.getFechaElaboracionInicial().trim().equals("")&&!this.getFechaElaboracionFinal().trim().equals("")))
			{
				errores.add("Definir Fecha Elaboracion Inicial", new ActionMessage("error.facturacion.rangosFechaInicialNoDefinidos","Definir Fecha Elaboracion Inicial"));
			}
		}
		return errores;
	}

	/**
	 * @return the factorConversionMoneda
	 */
	public String getFactorConversionMoneda() {
		return factorConversionMoneda;
	}

	/**
	 * @param factorConversionMoneda the factorConversionMoneda to set
	 */
	public void setFactorConversionMoneda(String factorConversionMoneda) {
		this.factorConversionMoneda = factorConversionMoneda;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean isManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the totalPresupuestoMon
	 */
	public String getTotalPresupuestoMon() {
		return totalPresupuestoMon;
	}

	/**
	 * @param totalPresupuestoMon the totalPresupuestoMon to set
	 */
	public void setTotalPresupuestoMon(String totalPresupuestoMon) {
		this.totalPresupuestoMon = totalPresupuestoMon;
	}
	
	
}
