/*
 * @(#)ActualizacionAutorizacionForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import org.apache.struts.action.ActionForm;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Actualizacion Autorizacion. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 24 /May/ 2005
 */
public class ActualizacionAutorizacionForm extends ActionForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	
	/**
	 * Variable que indica si el usuario es Profesional de la Salud
	 * Médico 
	 */
	private boolean esMedico;
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Numero de autorizacion que se dea cambiar
	 */
	private String numeroAutorizacion;
	
	/**
	 * Descripcion del Servicio
	 */
	private String  descripcionServicio;
	
	/**
	 * Fecha de la Solicitud
	 */
	private String fechaSolicitud;
	
	/**
	 * Hora de la Solicitud
	 */
	private String horaSolicitud;
	
	/**
	 * Consecutivo de Ordenes Medicas
	 */
	private String consecutivo;
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaActualizacion;
	
	/**
	 * almacena los datos de la consulta y no los modifica
	 */
	private HashMap mapaActualizacionNoModificado;
	
	/**
	 * Mapa implementado para almacenar la justificación de 
	 * los servicios o medicamentos
	 */
	private HashMap justificacion;
	
	/**
	 * almacena el numero de filas en el HashMap mapaActualizacion
	 */
	private int numeroElementos;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	
	private String ultimoPatron;
	
	/**
	 * Criterio de Seleccion en la actualizacion
	 */
	private String criterio;
	
	 /**
	 * //num de columnas que devuelve la consulta de la info de los 
	 * motivos de anulacion en este caso (fecha,hora,consecutivo, descripcion, numeroAutorizacion),
	 * este valor es necesaario a la hora de calcular el numero de filas del mapa 
	 */
	private int tamanioNumeroColumnas=5;
	
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
     /**
      * Número de registros por pàgina
      */
     private int maxPageItems;

     /**
      * Mapa con las cuentas del paciente
      */
     private HashMap mapaCuentas;
     
     
     private int posicionMapa;
     
     /**
      * Mapa para las solicitudes de medicamentos
      */
     private HashMap mapaMedicamentos;
     
     /**
      * Mapa que almacena las justificaciones de los medicamentos
      */
     private HashMap jusMedicamentos;
     
     /**
      * Numero de  Solicitud
      */
     private int numeroSolicitud;
     
     /**
      * Saber en la busqueda acvanzasda si es una busqueda de Servcios o de Medicamentos
      */
     private boolean esServicio;
     
     
     //********ATRIBUTOS ESPECÍFICOS PARA LAS SOLICITUDES DE CIRUGIAS*****************
     /**
      * Número de solicitud de cirugia seleccionada
      */
     private int numSolCx;
     
     /**
      * Cirugias de la solicitud
      */
     private HashMap cirugias = new HashMap();
     
     /**
      * Mapa que almacena las autirizaciones antiguas de cada cirugia
      */
     private HashMap autorizacionesAntiguasCx = new HashMap();
     
     /**
      * Tamaño de las cirugias
      */
     private int numCirugias;
     
     /**
      * Mapa que almacena las justificaciones de las cirugias
      */
     private HashMap jusCirugias = new HashMap();
     
     //*******************************************************************************
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;

	
	public void reset ()
	{
		this.mapaActualizacion = new HashMap ();
		this.mapaActualizacionNoModificado= new HashMap();
		this.justificacion = new HashMap();
		this.estado="";
		this.esMedico = false;
		this.cuenta=0;
		this.numeroAutorizacion="";
		this.linkSiguiente="";
		this.maxPageItems = 0;
		this.mapaMedicamentos=new HashMap();
		this.jusMedicamentos = new HashMap();
		this.fechaSolicitud="";
		this.consecutivo="";
		this.descripcionServicio="";
		
		//datos de las solicitudes de cirugias
		this.numSolCx = 0;
		this.cirugias = new HashMap();
		this.autorizacionesAntiguasCx = new HashMap();
		this.numCirugias = 0;
		this.jusCirugias = new HashMap();
	}
	
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
	    this.mapaActualizacion=new HashMap();
	    this.mapaActualizacionNoModificado= new HashMap();
	    this.mapaMedicamentos=new HashMap();
	}
	
	public void resetMapaActualizacion()
	{
		this.mapaActualizacion=new HashMap();
	}
	
	
	/**
	 * Método que compara los dos Mapas Original - Modificado, y los compara hasta el
	 * size del original y devuelve un nuevo HashMap con los campos que han sido modificados.
	 * @return
	 */
	public HashMap comparar2HashMap()
	{
	    HashMap mapaCamposModificados=new HashMap();
	    String campoMapaOriginalNoMod="", campoMapaModificado=""; 
	    for(int k=0; k<mapaActualizacionNoModificado.size()/this.tamanioNumeroColumnas; k++)
	    {
	        campoMapaOriginalNoMod=this.getMapaActualizacionNoModificado("numeroAutorizacion_"+k)+"";
	        
	        if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
	        {
	            campoMapaModificado=this.getMapaActualizacion("numeroAutorizacion_"+k)+"";
	            
	            if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
	            {
	                mapaCamposModificados.put("fechaSolicitud_"+k, mapaActualizacion.get("fechaSolicitud_"+k));
	                mapaCamposModificados.put("horaSolicitud_"+k, mapaActualizacion.get("horaSolicitud_"+k));
	                mapaCamposModificados.put("consecutivo_"+k, mapaActualizacion.get("consecutivo_"+k));
	                mapaCamposModificados.put("descripcionServicio_"+k, mapaActualizacion.get("descripcionServicio_"+k));
	                mapaCamposModificados.put("numeroAutorizacion_"+k, mapaActualizacion.get("numeroAutorizacion_"+k));
	            }
	        }
	    }
	    return mapaCamposModificados;
	}
	
	
	
	
	/**
	 * @return Returns the esServcio.
	 */
	public boolean isEsServicio()
	{
		return esServicio;
	}
	/**
	 * @param esServcio The esServcio to set.
	 */
	public void setEsServicio(boolean esServcio)
	{
		this.esServicio= esServcio;
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
	 * @return Returns the consecutivo.
	 */
	public String getConsecutivo()
	{
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(String consecutivo)
	{
		this.consecutivo= consecutivo;
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
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud= numeroSolicitud;
	}
	/**
	 * @return Returns the fechaSolicitud.
	 */
	public String getFechaSolicitud()
	{
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud)
	{
		this.fechaSolicitud= fechaSolicitud;
	}
	/**
	 * @return Returns the horaSolicitud.
	 */
	public String getHoraSolicitud()
	{
		return horaSolicitud;
	}
	/**
	 * @param horaSolicitud The horaSolicitud to set.
	 */
	public void setHoraSolicitud(String horaSolicitud)
	{
		this.horaSolicitud= horaSolicitud;
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
	 * @return Returns the criterio.
	 */
	public String getCriterio()
	{
		return criterio;
	}
	/**
	 * @param criterio The criterio to set.
	 */
	public void setCriterio(String criterio)
	{
		this.criterio= criterio;
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
	 * @return Returns the numeroAutorizacion.
	 */
	public String getNumeroAutorizacion()
	{
		return numeroAutorizacion;
	}
	/**
	 * @param numeroAutorizacion The numeroAutorizacion to set.
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion)
	{
		this.numeroAutorizacion= numeroAutorizacion;
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
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaNoMod(){
        return mapaActualizacionNoModificado.size()/tamanioNumeroColumnas;
    }
    
    
    
	/**
	 * @return Returns the tamanioNumeroColumnas.
	 */
	public int getTamanioNumeroColumnas()
	{
		return tamanioNumeroColumnas;
	}
	/**
	 * @param tamanioNumeroColumnas The tamanioNumeroColumnas to set.
	 */
	public void setTamanioNumeroColumnas(int tamanioNumeroColumnas)
	{
		this.tamanioNumeroColumnas= tamanioNumeroColumnas;
	}
    /**
	 * @return Returns the mapaActualizacion.
	 */
	public HashMap getMapaActualizacion()
	{
		return mapaActualizacion;
	}
	/**
	 * @param mapaActualizacion The mapaActualizacion to set.
	 */
	public void setMapaActualizacion(HashMap mapaActualizacion)
	{
		this.mapaActualizacion= mapaActualizacion;
	}
	
	/**
	 * @param Asigna un elemento al mapa mapaActualizacion.
	 */
	public void setMapaActualizacion(String key,Object obj)
	{
		this.mapaActualizacion.put(key,obj);
	}
	/**
	 * @return Returns the mapaActualizacionNoModificado.
	 */
	public HashMap getMapaActualizacionNoModificado()
	{
		return mapaActualizacionNoModificado;
	}
	/**
	 * @param mapaActualizacionNoModificado The mapaActualizacionNoModificado to set.
	 */
	public void setMapaActualizacionNoModificado(HashMap mapaActualizacionNoModificado)
	{
		this.mapaActualizacionNoModificado= mapaActualizacionNoModificado;
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
	 * @param key
	 * @param value
	 */
	public void setMapaActualizacionNoModificado(String key, Object value) 
	{
	    mapaActualizacionNoModificado.put(key, value);
	}
	/**
	 * Get del mapa de gen excepciones farmacia No modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaActualizacionNoModificado(String key) 
	{
		return mapaActualizacionNoModificado.get(key);
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
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaActualizacion(String key) 
	{
		return mapaActualizacion.get(key);
	}
	
	/**
	 * @return Returns the mapaMedicamentos.
	 */
	public HashMap getMapaMedicamentos()
	{
		return mapaMedicamentos;
	}
	/**
	 * @param mapaMedicamentos The mapaMedicamentos to set.
	 */
	public void setMapaMedicamentos(HashMap mapaMedicamentos)
	{
		this.mapaMedicamentos= mapaMedicamentos;
	}
	
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaMedicamentos(String key) 
	{
		return mapaMedicamentos.get(key);
	}
	
	/**
	 * @param Asigna un elemento al mapa mapaMedicamentos.
	 */
	public void setMapaMedicamentos(String key,Object obj)
	{
		this.mapaMedicamentos.put(key,obj);
	}
	
	/**
	 * @return Returns the mapaCuentas.
	 */
	public HashMap getMapaCuentas()
	{
		return mapaCuentas;
	}
	/**
	 * @param mapaCuentas The mapaCuentas to set.
	 */
	public void setMapaCuentas(HashMap mapaCuentas)
	{
		this.mapaCuentas= mapaCuentas;
	}
	
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaCuentas(String key) 
	{
		return mapaCuentas.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCuentas(String key, Object value) 
	{
	    mapaCuentas.put(key, value);
	}
	
	
	
	/**
	 * @return Returns the numSolCx.
	 */
	public int getNumSolCx() {
		return numSolCx;
	}
	/**
	 * @param numSolCx The numSolCx to set.
	 */
	public void setNumSolCx(int numSolCx) {
		this.numSolCx = numSolCx;
	}
	/**
	 * @return Returns the autorizacionesAntiguasCx.
	 */
	public HashMap getAutorizacionesAntiguasCx() {
		return autorizacionesAntiguasCx;
	}
	/**
	 * @param autorizacionesAntiguasCx The autorizacionesAntiguasCx to set.
	 */
	public void setAutorizacionesAntiguasCx(HashMap autorizacionesAntiguasCx) {
		this.autorizacionesAntiguasCx = autorizacionesAntiguasCx;
	}
	/**
	 * @return Retorna un elemento del mapa autorizacionesAntiguasCx.
	 */
	public Object getAutorizacionesAntiguasCx(String key) {
		return autorizacionesAntiguasCx.get(key);
	}
	/**
	 * @param asigna un elemento al mapa  autorizacionesAntiguasCx .
	 */
	public void setAutorizacionesAntiguasCx(String key,Object obj) {
		this.autorizacionesAntiguasCx.put(key,obj);
	}
	
	/**
	 * @return Returns the cirugias.
	 */
	public HashMap getCirugias() {
		return cirugias;
	}
	/**
	 * @param cirugias The cirugias to set.
	 */
	public void setCirugias(HashMap cirugias) {
		this.cirugias = cirugias;
	}
	/**
	 * @return Retorna un elemento del mapa cirugias.
	 */
	public Object getCirugias(String key) {
		return cirugias.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa cirugias.
	 */
	public void setCirugias(String key,Object obj) {
		this.cirugias.put(key,obj);
	}
	
	
	/**
	 * @return Returns the numCirugias.
	 */
	public int getNumCirugias() {
		return numCirugias;
	}
	/**
	 * @param numCirugias The numCirugias to set.
	 */
	public void setNumCirugias(int numCirugias) {
		this.numCirugias = numCirugias;
	}
	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the esMedico.
	 */
	public boolean isEsMedico() {
		return esMedico;
	}
	/**
	 * @param esMedico The esMedico to set.
	 */
	public void setEsMedico(boolean esMedico) {
		this.esMedico = esMedico;
	}
	/**
	 * @return Returns the justificacion.
	 */
	public HashMap getJustificacion() {
		return justificacion;
	}
	/**
	 * @param justificacion The justificacion to set.
	 */
	public void setJustificacion(HashMap justificacion) {
		this.justificacion = justificacion;
	}
	/**
	 * @return Retorna un elemento del mapa justificacion.
	 */
	public Object getJustificacion(String key) {
		return justificacion.get(key);
	}
	/**
	 * @param Asgina un elemento al mapa justificacion.
	 */
	public void setJustificacion(String key,Object obj) {
		this.justificacion.put(key,obj);
	}
	/**
	 * @return Returns the jusCirugias.
	 */
	public HashMap getJusCirugias() {
		return jusCirugias;
	}
	/**
	 * @param jusCirugias The jusCirugias to set.
	 */
	public void setJusCirugias(HashMap jusCirugias) {
		this.jusCirugias = jusCirugias;
	}
	/**
	 * @return Retorna un elemento del mapa jusCirugias.
	 */
	public Object getJusCirugias(String key) {
		return jusCirugias.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa jusCirugias.
	 */
	public void setJusCirugias(String key,Object obj) {
		this.jusCirugias.put(key,obj);
	}
	/**
	 * @return Returns the jusMedicamentos.
	 */
	public HashMap getJusMedicamentos() {
		return jusMedicamentos;
	}
	/**
	 * @param jusMedicamentos The jusMedicamentos to set.
	 */
	public void setJusMedicamentos(HashMap jusMedicamentos) {
		this.jusMedicamentos = jusMedicamentos;
	}
	/**
	 * @return Retorna un elemento del mapa jusMedicamentos.
	 */
	public Object getJusMedicamentos(String key) {
		return jusMedicamentos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa jusMedicamentos .
	 */
	public void setJusMedicamentos(String key,Object obj) 
	{
		this.jusMedicamentos.put(key,obj);
	}
}
