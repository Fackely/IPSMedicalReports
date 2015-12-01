/*
 * @(#)DespachoPedidosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.pedidos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * Form que contiene todos los datos específicos para generar 
 * el Despacho de pedidos
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Septiembre 30, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class DespachoPedidosForm extends ValidatorForm
{
	/**
	 * Número de pedido
	 */
	private int numeroPedido;
	
	/**
	 * Fecha - Hora del pedido
	 */
	private String fechaHoraPedido;

	/**
	 * Usuario Solicitante
	 */
	private String usuarioSolicitante;
	
	/**
	 * Observaciones generales
	 */
	private String observacionesGenerales;

	/**
	 * Fecha y Hora de grabación del pedido
	 */
	private String fechaHoraGrabacion;
	
	/**
	 * Centro de costo que solicita
	 */
	private String centroCostoSolicitante;
	
	/**
	 * Centro de costo que solicita
	 */
	private String codCentroCostoSolicitante;
	
	/**
	 * Identificador de Prioridad
	 */
	private String identificadorPrioridad;
	
	/**
	 * Estado del pedido
	 */
	private String estadoPedido;
	
	/**
	 * Farmacia del pedido
	 */
	private String farmacia;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	/**
	 * Contiene la cantidad a despachar de los N articulos 
	 * de pedidos
	 */
	public  HashMap despachoPedidosMap = new HashMap();
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";
	
	/**
	 * Fecha y Hora de Despacho
	 */
	private String fechaHoraDespacho;
	
	/**
	 * Nombre del almacen del despacho
	 */
	private String nombreAlmacen;
	
	/**
	 * 
	 */
	private boolean interfazCompras;
	
	/**
	 * 
	 */
	private HashMap<String, Object> almacenesConsignacion;
	
	/**
	 * 
	 */
	private HashMap<String, Object> conveniosProveedor;
	
	/**
	 * 
	 */
	private HashMap<String, Object> proveedorCatalogo;
	
	
	/**
	 * 
	 */
	private int institucion;
	
	private boolean mostrarListaPedidos;

	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("salirGuardar"))
		{
			if(getColSize() > 0)
			{  
			    for(int k=0; k < getColSize(); k++)
	            { 
			    	String codigoArticulo= String.valueOf(getDespachoPedidosMap("codigosArt_"+k));
			    	boolean valExistConsig=false;
			    	try
				    {
			    		//si se tiene interfaz hacer la siguiente validacion
			    		if(interfazCompras&&despachoPedidosMap.containsKey("tipodespacho_"+k))
			    		{
			    			if((despachoPedidosMap.get("tipodespacho_"+k)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
			    			{
			    				if(!despachoPedidosMap.containsKey("almacenConsignacion_"+k)||UtilidadTexto.isEmpty(despachoPedidosMap.get("almacenConsignacion_"+k)+""))
			    				{
			    					errores.add("falta campo", new ActionMessage("errors.required","El almacen de consignacion del Articulo "+codigoArticulo));
			    				}
			    				else
			    				{
			    					valExistConsig=true;
			    				}
			    				if(!despachoPedidosMap.containsKey("proveedorCompra_"+k)||UtilidadTexto.isEmpty(despachoPedidosMap.get("proveedorCompra_"+k)+""))
			    				{
			    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codigoArticulo));
			    				}
			    			}
			    			else if((despachoPedidosMap.get("tipodespacho_"+k)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
			    			{
			    				if(!despachoPedidosMap.containsKey("proveedorCatalogo_"+k)||UtilidadTexto.isEmpty(despachoPedidosMap.get("proveedorCatalogo_"+k)+""))
			    				{
			    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codigoArticulo));
			    				}
			    			}
			    			
			    		}
			    		
				    	int cantidadDespachada= Integer.parseInt(String.valueOf(getDespachoPedidosMap("cantidadADespachar_"+k)));
				
					    
					    if(cantidadDespachada <0)
						{
						 	errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","El despacho de Pedidos para el artículo "+codigoArticulo," 0"));  
						}
				        else if(valExistConsig)
				        {
				        	int exArticulo=Utilidades.convertirAEntero(UtilidadInventarios.getExistenciasXArticulo(Utilidades.convertirAEntero(codigoArticulo), Utilidades.convertirAEntero(despachoPedidosMap.get("almacenConsignacion_"+k)+""), institucion));
				        	if(cantidadDespachada>exArticulo)
				        	{
				        		errores.add("error.inventarios.existenciasInsuficientes",new ActionMessage("error.inventarios.existenciasInsuficientes",codigoArticulo,exArticulo+"",Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(despachoPedidosMap.get("almacenConsignacion_"+k)+""), institucion)));
					    	}
				        }
					    
					    if(UtilidadTexto.getBoolean(getDespachoPedidosMap("manejaLoteArticulo_"+k)+"") && (getDespachoPedidosMap("lote_"+k)+"").equals(""))
					    {
					    	errores.add("falta campo", new ActionMessage("errors.required","El lote del Articulo "+codigoArticulo));
					    }
					    if(UtilidadTexto.getBoolean(getDespachoPedidosMap("manejaFechaVencimientoArticulo_"+k)+""))
					    {
						    if((getDespachoPedidosMap("fechavencimiento_"+k)+"").equals(""))
						    {
						    	errores.add("falta campo", new ActionMessage("errors.required","La fecha de Vencimiento del Articulo "+codigoArticulo));
						    }
						    //si maneja fecha de venciminto, y es un lote-fechav generico en null, el lote y fechav seran ' ' 
						    else if(!((getDespachoPedidosMap("fechavencimiento_"+k)+"").trim().equals(""))&&!UtilidadFecha.validarFecha(getDespachoPedidosMap("fechavencimiento_"+k)+""))
						    {
						    	errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","La fecha de Vencimiento del Articulo "+codigoArticulo));
						    }
					    }
				    }
				    catch (NumberFormatException e)
				    {
				        errores.add("numeroNoEntero", new ActionMessage("errors.integer", "El despacho de pedidos para el articulo  " + codigoArticulo ));
				    }
	            }
			}
		}	
		return errores;
	}	
	
	/**
	 * resetea los datos pertinentes
	 * @param institucion 
	 */
	public void reset(int institucion)
	{
		this.numeroPedido=0;
		this.fechaHoraPedido="";
		this.usuarioSolicitante="";
		this.observacionesGenerales="";
		this.despachoPedidosMap=new HashMap();
		this.linkSiguiente="";
		this.fechaHoraGrabacion="";
		this.centroCostoSolicitante="";
		this.codCentroCostoSolicitante="";
		this.identificadorPrioridad="";
		this.estadoPedido="";
		this.farmacia="";
		this.fechaHoraDespacho="";
		this.nombreAlmacen = "";
		this.interfazCompras=false;
		this.almacenesConsignacion=UtilidadInventarios.obtenerAlmacenesConsignacion(institucion);
		this.conveniosProveedor=UtilidadInventarios.obtenerConveniosProveedor(institucion);
		this.proveedorCatalogo=UtilidadInventarios.obtenerProveedoresCatalogo(institucion);
		this.institucion=institucion;
		this.mostrarListaPedidos=false;
		
	}

	/**
	 * @return Returns the fechaHoraSolicitud.
	 */
	public String getFechaHoraPedido() {
		return fechaHoraPedido;
	}
	/**
	 * @param fechaHoraSolicitud The fechaHoraSolicitud to set.
	 */
	public void setFechaHoraPedido(String fechaHoraPedido) {
		this.fechaHoraPedido = fechaHoraPedido;
	}
	/**
	 * @return Returns the numeroPedido.
	 */
	public int getNumeroPedido() {
		return numeroPedido;
	}
	/**
	 * @param numeroPedido The numeroPedido to set.
	 */
	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	/**
	 * @return Returns the usuarioSolicitante.
	 */
	public String getUsuarioSolicitante() {
		return usuarioSolicitante;
	}
	/**
	 * @param usuarioSolicitante The usuarioSolicitante to set.
	 */
	public void setUsuarioSolicitante(String usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}
	
	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
	}
	
	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}
	
	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * Set del mapa de despacho
	 * @param key
	 * @param value
	 */
	public void setDespachoPedidosMap(String key, Object value) 
	{
		despachoPedidosMap.put(key, value);
	}

	/**
	 * Get del mapa de despacho
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getDespachoPedidosMap(String key) 
	{
		return despachoPedidosMap.get(key);
	}
	/**
	 * @return Returns the despachoPedidosMap.
	 */
	public HashMap getDespachoPedidosMap() {
		return despachoPedidosMap;
	}
	/**
	 * @param despachoPedidosMap The despachoPedidosMap to set.
	 */
	public void setDespachoPedidosMap(HashMap despachoPedidosMap) {
		this.despachoPedidosMap = despachoPedidosMap;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	/**
	 * @return Returns the fechaHoraGrabacion.
	 */
	public String getFechaHoraGrabacion() {
		return fechaHoraGrabacion;
	}
	/**
	 * @param fechaHoraGrabacion The fechaHoraGrabacion to set.
	 */
	public void setFechaHoraGrabacion(String fechaHoraGrabacion) {
		this.fechaHoraGrabacion = fechaHoraGrabacion;
	}
	
	/**
	 * @return Returns the centroCostoSolicitante.
	 */
	public String getCentroCostoSolicitante() {
		return centroCostoSolicitante;
	}
	/**
	 * @param centroCostoSolicitante The centroCostoSolicitante to set.
	 */
	public void setCentroCostoSolicitante(String centroCostoSolicitante) {
		this.centroCostoSolicitante = centroCostoSolicitante;
	}
	/**
	 * @return Returns the estadoPedido.
	 */
	public String getEstadoPedido() {
		return estadoPedido;
	}
	/**
	 * @param estadoPedido The estadoPedido to set.
	 */
	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	/**
	 * @return Returns the farmacia.
	 */
	public String getFarmacia() {
		return farmacia;
	}
	/**
	 * @param farmacia The farmacia to set.
	 */
	public void setFarmacia(String farmacia) {
		this.farmacia = farmacia;
	}
	/**
	 * @return Returns the identificadorPrioridad.
	 */
	public String getIdentificadorPrioridad() {
		return identificadorPrioridad;
	}
	/**
	 * @param identificadorPrioridad The identificadorPrioridad to set.
	 */
	public void setIdentificadorPrioridad(String identificadorPrioridad) {
		this.identificadorPrioridad = identificadorPrioridad;
	}
	/**
	 * @return Returns the fechaHoraDespacho.
	 */
	public String getFechaHoraDespacho() {
		return fechaHoraDespacho;
	}
	/**
	 * @param fechaHoraDespacho The fechaHoraDespacho to set.
	 */
	public void setFechaHoraDespacho(String fechaHoraDespacho) {
		this.fechaHoraDespacho = fechaHoraDespacho;
	}
	/**
	 * @return Returns the nombreAlmacen.
	 */
	public String getNombreAlmacen() {
		return nombreAlmacen;
	}
	/**
	 * @param nombreAlmacen The nombreAlmacen to set.
	 */
	public void setNombreAlmacen(String nombreAlmacen) {
		this.nombreAlmacen = nombreAlmacen;
	}

	/**
	 * @return the interfazCompras
	 */
	public boolean isInterfazCompras() {
		return interfazCompras;
	}

	/**
	 * @param interfazCompras the interfazCompras to set
	 */
	public void setInterfazCompras(boolean interfazCompras) {
		this.interfazCompras = interfazCompras;
	}

	/**
	 * @return the almacenesConsignacion
	 */
	public HashMap<String, Object> getAlmacenesConsignacion() {
		return almacenesConsignacion;
	}

	/**
	 * @param almacenesConsignacion the almacenesConsignacion to set
	 */
	public void setAlmacenesConsignacion(
			HashMap<String, Object> almacenesConsignacion) {
		this.almacenesConsignacion = almacenesConsignacion;
	}
	

	/**
	 * @return the almacenesConsignacion
	 */
	public Object getAlmacenesConsignacion(String key) 
	{
		return almacenesConsignacion.get(key);
	}

	/**
	 * @param almacenesConsignacion the almacenesConsignacion to set
	 */
	public void setAlmacenesConsignacion(String key,Object value) 
	{
		this.almacenesConsignacion.put(key, value);
	}

	/**
	 * @return the conveniosProveedor
	 */
	public HashMap<String, Object> getConveniosProveedor() {
		return conveniosProveedor;
	}

	/**
	 * @param conveniosProveedor the conveniosProveedor to set
	 */
	public void setConveniosProveedor(HashMap<String, Object> conveniosProveedor) {
		this.conveniosProveedor = conveniosProveedor;
	}
	/**
	 * @return the conveniosProveedor
	 */
	public Object getConveniosProveedor(String key) 
	{
		return conveniosProveedor.get(key);
	}

	/**
	 * @param conveniosProveedor the conveniosProveedor to set
	 */
	public void setConveniosProveedor(String key,Object value) 
	{
		this.conveniosProveedor.put(key, value);
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the proveedorCatalogo
	 */
	public HashMap<String, Object> getProveedorCatalogo() {
		return proveedorCatalogo;
	}

	/**
	 * @param proveedorCatalogo the proveedorCatalogo to set
	 */
	public void setProveedorCatalogo(HashMap<String, Object> proveedorCatalogo) {
		this.proveedorCatalogo = proveedorCatalogo;
	}
	

	/**
	 * @return the proveedorCatalogo
	 */
	public Object getProveedorCatalogo(String key) {
		return proveedorCatalogo.get(key);
	}

	/**
	 * @param proveedorCatalogo the proveedorCatalogo to set
	 */
	public void setProveedorCatalogo(String key,Object value) {
		this.proveedorCatalogo.put(key, value);
	}

	/**
	 * @return the codCentroCostoSolicitante
	 */
	public String getCodCentroCostoSolicitante() {
		return codCentroCostoSolicitante;
	}

	/**
	 * @param codCentroCostoSolicitante the codCentroCostoSolicitante to set
	 */
	public void setCodCentroCostoSolicitante(String codCentroCostoSolicitante) {
		this.codCentroCostoSolicitante = codCentroCostoSolicitante;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostrarListaPedidos
	
	 * @return retorna la variable mostrarListaPedidos 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostrarListaPedidos() {
		return mostrarListaPedidos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostrarListaPedidos
	
	 * @param valor para el atributo mostrarListaPedidos 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarListaPedidos(boolean mostrarListaPedidos) {
		this.mostrarListaPedidos = mostrarListaPedidos;
	}
	
}
