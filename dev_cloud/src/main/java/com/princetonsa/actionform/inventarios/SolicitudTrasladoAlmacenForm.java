
/*
 * Creado   11/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.actionform.historiaClinica.ReporteReferenciaExternaForm;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.RespuestaValidacion;
import util.UtilidadFecha;
import util.inventarios.UtilidadInventarios;

/**
 * 
 *
 * @version 1.0, 11/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SolicitudTrasladoAlmacenForm extends ActionForm 
{
	/**
	 * Atributos del logger, para manejo de errores e impresiones
	 */
	//Logger logger = Logger.getLogger(SolicitudTrasladoAlmacenForm.class);
	Logger logger = Logger.getAnonymousLogger();
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * lista de almacenes
     */
    private HashMap mapAlmacenes;
    /**
     * almacena el codigo del almacen 
     * seleccionado
     */
    private int codAlmacen;
    /**
     * almacena el nombre del almacen
     * seleccionado
     */
    private String nombreAlmacen;
    /**
     * observaciones del traslado
     */
    private String observaciones;
    /**
     * mapa que almacenas atributos de 
     * la forma
     */
    private HashMap mapaAtributos;    
    /**
     * fecha de elaboración de la 
     * solicitud
     */
    private String fechaElaboracion;
    /**
     * prioridad de la solicitud
     */
    private String esPrioritario;
    /**
     * almacena la hora de la solicitud
     */
    private String horaElaboracion;
    /**
     * código de transacción utilizado
     * para los traslados de almacenes
     */
    private String codigoTransaccionTraslado;
    /**
     * consecutivo con el cual es insertada
     * la solicitud
     */
    private int numeroTrasladoConsecutivo;
    /**
     * almacena el estado del detalle, para 
     * mostrar el detalle de la solicitud
     */
    private String cargarDetalle;
    /**
     * almacena el estado de la transaccion,
     * pendiente o cerrada
     */
    private String esCerrarTransaccion;
    /**
     * almacena la información articulos
     */
    private HashMap mapaArticulos;
    /**
     * numero de registros en el mapa de articulos
     */
    private int numRegistrosArticulos;
    /**
     * numero de registros por pager
     */
    private int maxPageItem;
    /**
     * pager en el cual se encuentra posicionado
     */
    private int offset;
    /**
     * proximo pager
     */
    private String linkSiguiente;
    /**
     * patron para ordenar el mapa.
     */
    private String patronOrdenar;
    /**
     * ultimo patron por el que se ordeno el mapa
     */
    private String ultimoPatron;
    /**
     * connection para guardar el detalle de 
     * forma transaccional
     */
    private Connection conTrans;
    /**
     * estado de la transacción para el detalle
     */
    private boolean enTransaccion;
    /**
     * motivo de la anulación de la solicitud
     */
    private String motivoAnulacion;
    /**
     * listado de las solicitudes
     */
    private HashMap mapaSolicitudes;    
    /**
     * almacena la posición de lasolicitud seleccionada
     */
    private int regSeleccionado;
    /**
     * almacena el tipo de operacion de la
     * solicitud, nuevaSolicitud ó modificarSolicitud
     */
    private String accion;
    /**
     * almacena los codigos de los articulos
     * que se eliminan de la BD
     */
    private ArrayList regEliminadosBD;
    
    /**
     * 
     */
    private String parejasClaseGrupo; 
    
    /**
     * 
     */
    private String modificarFecha;
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {   
        this.mapAlmacenes=new HashMap();
        this.mapaAtributos=new HashMap();
        this.codAlmacen=ConstantesBD.codigoNuncaValido;
        this.nombreAlmacen="";
        this.observaciones="";
        this.resetMapaAtributos();
        this.fechaElaboracion="";
        this.esPrioritario="";
        this.horaElaboracion="";
        this.codigoTransaccionTraslado="";
        this.numeroTrasladoConsecutivo=ConstantesBD.codigoNuncaValido;
        this.cargarDetalle="";
        this.esCerrarTransaccion="";
        this.mapaArticulos=new HashMap();
        this.numRegistrosArticulos=0;
        this.maxPageItem=ConstantesBD.codigoNuncaValido;
        this.offset=0;
        this.linkSiguiente="";
        this.patronOrdenar="";
        this.ultimoPatron="";
        this.conTrans=null;
        this.enTransaccion=true;
        this.motivoAnulacion="";
        this.mapaSolicitudes=new HashMap();
        this.regSeleccionado=ConstantesBD.codigoNuncaValido;
        this.accion="";
        this.regEliminadosBD=new ArrayList();
        this.parejasClaseGrupo="";
    }
    /**inicializar valores de los atributos del mapa*/
    public void resetMapaAtributos()
    {
        this.mapaAtributos.put("estadoTraslado","");
        this.mapaAtributos.put("almacenSolicita","");
        this.mapaAtributos.put("usuarioElabora","");
        this.mapaAtributos.put("esModificarFecha","");
        this.mapaAtributos.put("clase","");
        this.mapaAtributos.put("grupo","");
        this.mapaAtributos.put("posArticuloEliminar",ConstantesBD.codigoNuncaValido+"");
        this.mapaAtributos.put("mostrarListado","");
        this.mapaAtributos.put("numero_traslado_inicial","");
        this.mapaAtributos.put("numero_traslado_final","");
        this.mapaAtributos.put("fecha_elaboracion_inicial","");
        this.mapaAtributos.put("fecha_elaboracion_final","");
    }
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{	    
	    ActionErrors errores = new ActionErrors();
	    if(this.estado.equals("continuarSolicitud")||this.estado.equals("generarDetalleTransaccion"))
	    {
	        if(this.fechaElaboracion.equals(""))
	        {
	            errores.add("falta campo", new ActionMessage("errors.required","LA FECHA DE ELABORACION"));
	        }
		    else
		    {
		        if(!UtilidadFecha.validarFecha(this.fechaElaboracion))
		        {
		            errores.add("formato invalido", new ActionMessage("errors.fechaInvalidaMMYYYY",this.fechaElaboracion));   
		        }
		        else
		        {
			        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaElaboracion,UtilidadFecha.getFechaActual()))
			        {
			            errores.add("fecha anterior", new ActionMessage("errors.fechaPosteriorIgualActual","de elaboración "+this.fechaElaboracion,UtilidadFecha.getFechaActual()));
			        }
			        UsuarioBasico usuario=new UsuarioBasico();
			        if(UtilidadInventarios.existeCierreInventarioParaFecha(this.fechaElaboracion,usuario.getCodigoInstitucionInt()))
			        {
			            errores.add("existe cierre", new ActionMessage("error.inventarios.existeCierreInventarios",this.fechaElaboracion));            
			        }
		        }
		    }
	        if(this.horaElaboracion.equals(""))
	        {
	            errores.add("falta campo", new ActionMessage("errors.required","LA HORA DE ELABORACION"));  
	        }
	        else
	        {
	            RespuestaValidacion r=UtilidadFecha.validacionHora(this.horaElaboracion);
	            if(!r.puedoSeguir)
	            {
	                errores.add("formato invalido", new ActionMessage("errors.formatoHoraInvalido",this.horaElaboracion));
	            }
	        }
	    }
	    if(this.estado.equals("generarDetalleTransaccion"))
	    {
	    	for(int i=0;i<this.numRegistrosArticulos;i++)
	    	{
	    		
	    		if((this.mapaArticulos.get("cantidadArticulo_"+i)+"").trim().equals(""))
	    		{
	    			errores.add("cantidad REQUERIDO", new ActionMessage("errors.required","LA CANTIDAD PARA EL ARTICULO "+this.mapaArticulos.get("codigoArticulo_"+i)+" - "+this.mapaArticulos.get("descripcionArticulo_"+i)));
	    		}
		    	
	    	}
	    	
	    }
	    if(this.estado.equals("generarBusqueda"))
	    {
	        try
	        {
		    	if((this.mapaAtributos.get("numero_traslado_inicial")+"").equals("") && (this.mapaAtributos.get("numero_traslado_final")+"").equals("") &&
		           (this.mapaAtributos.get("fecha_elaboracion_inicial")+"").equals("") && (this.mapaAtributos.get("fecha_elaboracion_final")+"").equals("") )
		        {
		            errores.add("faltan parametros", new ActionMessage("errors.minimoCampos","un parametro","busqueda"));
		        }
	        }
	        catch (Exception e) 
	        {
	        	errores.add("Numero invalido",new ActionMessage("errors.invalid",1));
			}
	    }
	    if(this.estado.equals("generarAnulacion"))
	    {
	        if(this.motivoAnulacion.trim().equals(""))
	        {
	        	errores.add("motivoAnulacion REQUERIDO", new ActionMessage("errors.required","EL MOTIVO DE ANULACION"));
	        }        
	    }
	    return errores;
	}
	/**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna mapAlmacenes.
     */
    public HashMap getMapAlmacenes() {
        return mapAlmacenes;
    }
    /**
     * @param mapAlmacenes Asigna mapAlmacenes.
     */
    public void setMapAlmacenes(HashMap mapAlmacenes) {
        this.mapAlmacenes = mapAlmacenes;
    }
    /**
     * @return Retorna mapAlmacenes.
     */
    public Object getMapAlmacenes(String key) {
        return mapAlmacenes.get(key);
    }
    /**
     * @param mapAlmacenes Asigna mapAlmacenes.
     */
    public void setMapAlmacenes(String key,Object value) {
        this.mapAlmacenes.put(key, value);
    }
    /**
     * @return Retorna codAlmacen.
     */
    public int getCodAlmacen() {
        return codAlmacen;
    }
    /**
     * @param codAlmacen Asigna codAlmacen.
     */
    public void setCodAlmacen(int codAlmacen) {
        this.codAlmacen = codAlmacen;
    }
    /**
     * @return Retorna nombreAlmacen.
     */
    public String getNombreAlmacen() {
        return nombreAlmacen;
    }
    /**
     * @param nombreAlmacen Asigna nombreAlmacen.
     */
    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }
    /**
     * @return Retorna observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones Asigna observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @return Retorna mapaAtributos.
     */
    public HashMap getMapaAtributos() {
        return mapaAtributos;
    }
    /**
     * @param mapaAtributos Asigna mapaAtributos.
     */
    public void setMapaAtributos(HashMap mapaAtributos) {
        this.mapaAtributos = mapaAtributos;
    }
    /**
     * @return Retorna mapaAtributos.
     */
    public Object getMapaAtributos(String key) {
        return mapaAtributos.get(key);
    }
    /**
     * @param mapaAtributos Asigna mapaAtributos.
     */
    public void setMapaAtributos(String key,Object value) {
        this.mapaAtributos.put(key, value);
    }
    /**
     * @return Retorna fechaElaboracion.
     */
    public String getFechaElaboracion() {
        return fechaElaboracion;
    }
    /**
     * @param fechaElaboracion Asigna fechaElaboracion.
     */
    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }
    /**
     * @return Retorna esPrioritario.
     */
    public String getEsPrioritario() {
        return esPrioritario;
    }
    /**
     * @param esPrioritario Asigna esPrioritario.
     */
    public void setEsPrioritario(String esPrioritario) {
        this.esPrioritario = esPrioritario;
    }
    /**
     * @return Retorna horaElaboracion.
     */
    public String getHoraElaboracion() {
        return horaElaboracion;
    }
    /**
     * @param horaElaboracion Asigna horaElaboracion.
     */
    public void setHoraElaboracion(String horaElaboracion) {
        this.horaElaboracion = horaElaboracion;
    }
    /**
     * @return Retorna codigoTransaccionTraslado.
     */
    public String getCodigoTransaccionTraslado() {
        return codigoTransaccionTraslado;
    }
    /**
     * @param codigoTransaccionTraslado Asigna codigoTransaccionTraslado.
     */
    public void setCodigoTransaccionTraslado(String codigoTransaccionTraslado) {
        this.codigoTransaccionTraslado = codigoTransaccionTraslado;
    }
    /**
     * @return Retorna numeroTrasladoConsecutivo.
     */
    public int getNumeroTrasladoConsecutivo() {
        return numeroTrasladoConsecutivo;
    }
    /**
     * @param numeroTrasladoConsecutivo Asigna numeroTrasladoConsecutivo.
     */
    public void setNumeroTrasladoConsecutivo(int numeroTrasladoConsecutivo) {
        this.numeroTrasladoConsecutivo = numeroTrasladoConsecutivo;
    }
    /**
     * @return Retorna cargarDetalle.
     */
    public String getCargarDetalle() {
        return cargarDetalle;
    }
    /**
     * @param cargarDetalle Asigna cargarDetalle.
     */
    public void setCargarDetalle(String cargarDetalle) {
        this.cargarDetalle = cargarDetalle;
    }
    /**
     * @return Retorna esCerrarTransaccion.
     */
    public String getEsCerrarTransaccion() {
        return esCerrarTransaccion;
    }
    /**
     * @param esCerrarTransaccion Asigna esCerrarTransaccion.
     */
    public void setEsCerrarTransaccion(String esCerrarTransaccion) {
        this.esCerrarTransaccion = esCerrarTransaccion;
    }
    /**
     * @return Retorna mapaArticulos.
     */
    public HashMap getMapaArticulos() {
        return mapaArticulos;
    }
    /**
     * @param mapaArticulos Asigna mapaArticulos.
     */
    public void setMapaArticulos(HashMap mapaArticulos) {
        this.mapaArticulos = mapaArticulos;
    }
    /**
     * @return Retorna mapaArticulos.
     */
    public Object getMapaArticulos(String key) {
        return mapaArticulos.get(key);
    }
    /**
     * @param mapaArticulos Asigna mapaArticulos.
     */
    public void setMapaArticulos(String key,Object value) {
        this.mapaArticulos.put(key, value);
    }
    /**
     * @return Retorna getNumRegistrosArticulos.
     */
    public int getNumRegistrosArticulos() {
        return this.numRegistrosArticulos;
    }
    /**
     * @param numRegistrosArticulos Asigna numRegistrosArticulos.
     */
    public void setNumRegistrosArticulos(int numRegistrosArticulos) {
        this.numRegistrosArticulos = numRegistrosArticulos;
    }
    /**
     * @return Retorna maxPageItem.
     */
    public int getMaxPageItem() {
        return maxPageItem;
    }
    /**
     * @param maxPageItem Asigna maxPageItem.
     */
    public void setMaxPageItem(int maxPageItem) {
        this.maxPageItem = maxPageItem;
    }
    /**
     * @return Retorna offset.
     */
    public int getOffset() {
        return offset;
    }
    /**
     * @param offset Asigna offset.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * @return Retorna linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente Asigna linkSiguiente.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
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
     * @return Retorna ultimoPatron.
     */
    public String getUltimoPatron() {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron Asigna ultimoPatron.
     */
    public void setUltimoPatron(String ultimoPatron) {
        this.ultimoPatron = ultimoPatron;
    }
    /**
     * @return Retorna conTrans.
     */
    public Connection getConTrans() {
        return conTrans;
    }
    /**
     * @param conTrans Asigna conTrans.
     */
    public void setConTrans(Connection conTrans) {
        this.conTrans = conTrans;
    }
    /**
     * @return Retorna enTransaccion.
     */
    public boolean isEnTransaccion() {
        return enTransaccion;
    }
    /**
     * @param enTransaccion Asigna enTransaccion.
     */
    public void setEnTransaccion(boolean enTransaccion) {
        this.enTransaccion = enTransaccion;
    }
    /**
     * @return Retorna motivoAnulacion.
     */
    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }
    /**
     * @param motivoAnulacion Asigna motivoAnulacion.
     */
    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
    /**
     * @return Retorna mapaSolicitudes.
     */
    public HashMap getMapaSolicitudes() {
        return mapaSolicitudes;
    }
    /**
     * @param mapaSolicitudes Asigna mapaSolicitudes.
     */
    public void setMapaSolicitudes(HashMap mapaSolicitudes) {
        this.mapaSolicitudes = mapaSolicitudes;
    }
    /**
     * @return Retorna mapaSolicitudes.
     */
    public Object getMapaSolicitudes(String key) {
        return mapaSolicitudes.get(key);
    }
    /**
     * @param mapaSolicitudes Asigna mapaSolicitudes.
     */
    public void setMapaSolicitudes(String key,Object value) {
        this.mapaSolicitudes.put(key, value);
    }
    /**
     * @return Retorna regSeleccionado.
     */
    public int getRegSeleccionado() {
        return regSeleccionado;
    }
    /**
     * @param regSeleccionado Asigna regSeleccionado.
     */
    public void setRegSeleccionado(int regSeleccionado) {
        this.regSeleccionado = regSeleccionado;
    }
    /**
     * @return Retorna accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @return Retorna regEliminadosBD.
     */
    public ArrayList getRegEliminadosBD() {
        return regEliminadosBD;
    }
    /**
     * @param regEliminadosBD Asigna regEliminadosBD.
     */
    public void setRegEliminadosBD(ArrayList regEliminadosBD) {
        this.regEliminadosBD = regEliminadosBD;
    }
    /**
     * @return Retorna regEliminadosBD.
     */
    public Object getRegEliminadosBD(int pos) {
        return regEliminadosBD.get(pos);
    }
    /**
     * @param regEliminadosBD Asigna regEliminadosBD.
     */
    public void setRegEliminadosBD(Object value) {
        this.regEliminadosBD.add(value);
    }
	public String getParejasClaseGrupo()
	{
		return parejasClaseGrupo;
	}
	public void setParejasClaseGrupo(String parejasClaseGrupo)
	{
		this.parejasClaseGrupo = parejasClaseGrupo;
	}
	/**
	 * @return the modificarFecha
	 */
	public String getModificarFecha() {
		return modificarFecha;
	}
	/**
	 * @param modificarFecha the modificarFecha to set
	 */
	public void setModificarFecha(String modificarFecha) {
		this.modificarFecha = modificarFecha;
	}
	
	
}
