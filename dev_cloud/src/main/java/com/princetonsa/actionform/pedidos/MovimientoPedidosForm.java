/*
 * MovimientoPedidosForm.java 
 * Autor			:  mdiaz
 * Creado el	:  29-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.actionform.pedidos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 29-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class MovimientoPedidosForm extends ValidatorForm implements Serializable{
	private String estado;
	private String orderByField;
	private String lastOrderByField;
	private int orderDirection;
	private HashMap pedidos;
	private HashMap articulos;
	private int selectedIndex;
	
	
	// campos de consulta
	private int searchCodigoPedido;
	private int searchEstadoPedido;
	private int searchCodigoCentroCosto;
	private int searchCodigoFarmacia;
	
	
	// tabla pedido
	private int codigoPedido;
	private int urgente;
	private int estadoPedido;
	private String descEstadoPedido;
	private String fechaPedido;
	private String horaPedido;
	private int codigoCentroCosto;
	private int codigoFarmacia;
	private String usuarioPedido;
	
	// tabla despacho_pedido
	private String fechaDespacho;
	private String horaDespacho;
	private String usuarioDespacho;
	

	// funcionalidades permitidas
	private String[] modificacion;
	
	// funcionalidades permitidas
	private String[] devolucion;
	
	//atributos para el listado inicial de almacenes
	private String institucion;
	
	//datos anulacion pedido
	private HashMap datosAnulacion = new HashMap();
	/**
	 * Descripcion del centro de costo
	 */
	private String centroCosto;
	/**
	 * Descripcion del almacen
	 */
	private String farmacia;
	/**
	 * Código del tipo de transaccion pedido
	 */
	private String tipoTransaccionPedido;
	/**
	 * Número de almacenes válidos para el usuario
	 */
	private int numAlmacenes;
	/**
	 * Login del usuario 
	 */
	private String usuarioAlmacen;
	
	/**
	 * Codigo + separadorSplit + Nombre del Centro de Atencion
	 */
	private String centroAtencion;
	
	//***********ATRIBUTOS USADOS PARA LA MODIFICACION/ANULACION**************
	/**
	 * Listado de los articulos solicitados
	 */
	private HashMap pedidosMap = new HashMap();
	
	/**
	 * Listado de los artículos que se van a eliminar
	 */
	private HashMap pedidosEliminacionMap = new HashMap();
	
	/**
	 * Objeto donde se registra el pedido  y su detalle antes de modificar
	 */
	private HashMap pedidosAntiguosMap = new HashMap();
	
	/**
	 * Objeto donde se almacena toda la información del
	 * resumen del Pedido
	 */
	private Collection coleccionPedidos ;
	
	/**
	 * numero de artículos del pedido
	 */
	private int numeroIngresos;
	
	/**
	 * lista de los artículos que ya se han insertado
	 */
	private String codigosArticulosInsertados;
	
	/**
	 * listado de parejas clase-grupo
	 */
	private String parejasClaseGrupo;
	
	/**
	 * variable que indica modificacion
	 */
	private String subEstado;
	
	/**
	 * link siguiente del paginador
	 */
	private String linkSiguiente;
	
	/**
	 * número máximo de registro para el pager
	 */
	private int maxPageItems;
	
	/**
	 * fecha del pedido en la modificacion
	 */
	private String fechaPedidoModificacion;
	
	/**
	 * hora del pedido en la modificacion
	 */
	private String horaPedidoModificacion;
	
	/**
	 * fecha-hora de la grabacion del pedido
	 */
	private String fechaHoraGrabacion;
	
	/**
	 * observaciones generales del pedido
	 */
	private String observacionesGenerales;
	
	/**
	 * Estado del checkBox de Urgente.
	 */
	private String checkTerminarPedido;
	
	/**
	 * Estado del checkBox de Urgente.
	 */
	private String checkPrioridadPedido;
	
	/**
	 * Estado del checkBox de Anulado.
	 */
	private String checkAnularPedido;
	
	/**
	 * motivo de la anulacion
	 */
	private String motivoAnulacion;
	
	/**
	 * Varibale donde se almacena el contenido del LOG MODIFICACION
	 */
	private String logInfo;
	
	private String modificarFecha;
	
	/**
	 * código del articulo que se va a eliminar
	 */
	private String codigoArticulo;
	//************************************************************************
	
	//***************ATRIBUTOS USADOS PARA LOS PEDIDOS QX *********************+
	private HashMap<String, Object> datosQx = new HashMap<String, Object>();
	private String esQuirurgico ;
	//***************************************************************************
	
	
	public void reset(){
		this.pedidos = new HashMap();
		this.articulos = new HashMap();
		this.selectedIndex = -1;
		this.orderByField = "cod_pedido";
	  this.lastOrderByField = "cod_pedido";	
		this.orderDirection = 0; // DESC
		
		this.searchCodigoPedido = -1;
		this.searchEstadoPedido = -1;
		this.searchCodigoCentroCosto = -1;
		this.searchCodigoFarmacia = -1;

		// tabla pedido
		this.codigoPedido = -1;
		this.urgente = -1;
		this.estadoPedido = -1;
		this.descEstadoPedido = "";
		this.fechaPedido = "";
		this.horaPedido = "";
		this.codigoCentroCosto = -1;
		this.codigoFarmacia = -1;
		this.usuarioPedido = "";
		
		// tabla despacho_pedido
		this.fechaDespacho = "";
		this.horaDespacho = "";
		this.usuarioDespacho = "";

		// funcionalidades permitidas
		this.modificacion = new String[2];
		this.devolucion = new String[2];
		
		//atributos para el listado inicial de almacenes
		this.institucion = "";
		this.centroCosto = "";
		this.farmacia = "";
		this.tipoTransaccionPedido = "";
		this.numAlmacenes = 0;
		this.usuarioAlmacen = "";
		
		//datos anulacion
		this.datosAnulacion = new HashMap();
		
		this.centroAtencion="0"+ConstantesBD.separadorSplit+"Todos";
		
		//***************ATRIBUTOS USADOS PARA LOS PEDIDOS QX *********************+
		this.datosQx = new HashMap<String, Object>();
		this.esQuirurgico = "";
		//***************************************************************************
		
		this.resetModificacion();
	}
	
	public void resetModificacion()
	{
		///***ATRIBUTOS DE LA MODIFICACION ANULACION****
		this.pedidosMap = new HashMap();
		this.numeroIngresos = 0;
		this.codigosArticulosInsertados = "";
		this.parejasClaseGrupo = "";
		this.subEstado = "";
		this.linkSiguiente = "";
		this.maxPageItems = 0;
		this.fechaPedidoModificacion = "";
		this.horaPedidoModificacion = "";
		this.fechaHoraGrabacion = "";
		this.observacionesGenerales = "";
		this.checkTerminarPedido = "";
		this.checkPrioridadPedido = "";
		this.checkAnularPedido = "";
		this.motivoAnulacion = "";
		this.logInfo = "";
		this.codigoArticulo = "";
		this.pedidosEliminacionMap = new HashMap();
		this.pedidosAntiguosMap = new HashMap();
		this.coleccionPedidos = new ArrayList();
		//*********************************************
	}
	
	
	/**
	 * Validaciones.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(estado.equals("comenzar_busqueda_modificar"))
		{
			estado = "comenzar_busqueda";
		}
		if(estado.equals("guardar_modificacion"))
		{
			if(UtilidadTexto.getBoolean(this.checkAnularPedido))
			{
				if(this.motivoAnulacion.trim().equals(""))
				{
					errores.add("mot anulacion requerido",new ActionMessage("errors.required","El Motivo de la Anulación"));
					estado = "empezar_modificacion";
				}
			}
		}
		return errores;
	}
	
	
	
	/**
	 * @return Returns the sCodigoCentroCosto.
	 */
	public int getSearchCodigoCentroCosto() {
		return searchCodigoCentroCosto;
	}
	/**
	 * @param codigoCentroCosto The sCodigoCentroCosto to set.
	 */
	public void setSearchCodigoCentroCosto(int codigoCentroCosto) {
		searchCodigoCentroCosto = codigoCentroCosto;
	}
	/**
	 * @return Returns the sCodigoFarmacia.
	 */
	public int getSearchCodigoFarmacia() {
		return searchCodigoFarmacia;
	}
	/**
	 * @param codigoFarmacia The sCodigoFarmacia to set.
	 */
	public void setSearchCodigoFarmacia(int codigoFarmacia) {
		searchCodigoFarmacia = codigoFarmacia;
	}
	/**
	 * @return Returns the sEstadoPedido.
	 */
	public int getSearchEstadoPedido() {
		return searchEstadoPedido;
	}
	/**
	 * @param estadoPedido The sEstadoPedido to set.
	 */
	public void setSearchEstadoPedido(int estadoPedido) {
		searchEstadoPedido = estadoPedido;
	}
	/**
	 * @return Returns the sCodigoPedido.
	 */
	public int getSearchCodigoPedido() {
		return searchCodigoPedido;
	}
	/**
	 * @param codigoPedido The sCodigoPedido to set.
	 */
	public void setSearchCodigoPedido(int codigoPedido) {
		searchCodigoPedido = codigoPedido;
	}
	
	/**
	 * @return Returns the modificacion.
	 */
	public String[] getModificacion() {
		return modificacion;
	}
	/**
	 * @param modificacion The modificacion to set.
	 */
	public void setModificacion(String[] modificacion) {
		this.modificacion = modificacion;
	}
	/**
	 * @return Returns the articulos.
	 */
	public HashMap getArticulos() {
		return articulos;
	}
	/**
	 * @param articulos The articulos to set.
	 */
	public void setArticulos(HashMap articulos) {
		try{
			if( this.articulos == null )
				this.articulos = new HashMap();
			
			this.articulos.clear();
			this.articulos.putAll(articulos);
			}
			catch(NullPointerException  e){
				e.printStackTrace();
			}		
	}

	public int getNumArticulos(){
		if(this.articulos == null )
			return 0;
		
		return this.articulos.size();
	}

	
	/**
	 * @return Returns the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}
	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
	/**
	 * @return Returns the codigoFarmacia.
	 */
	public int getCodigoFarmacia() {
		return codigoFarmacia;
	}
	/**
	 * @param codigoFarmacia The codigoFarmacia to set.
	 */
	public void setCodigoFarmacia(int codigoFarmacia) {
		this.codigoFarmacia = codigoFarmacia;
	}
	/**
	 * @return Returns the codigoPedido.
	 */
	public int getCodigoPedido() {
		return codigoPedido;
	}
	/**
	 * @param codigoPedido The codigoPedido to set.
	 */
	public void setCodigoPedido(int codigoPedido) {
		this.codigoPedido = codigoPedido;
	}
	/**
	 * @return Returns the descEstadoPedido.
	 */
	public String getDescEstadoPedido() {
		return descEstadoPedido;
	}
	/**
	 * @param descEstadoPedido The descEstadoPedido to set.
	 */
	public void setDescEstadoPedido(String descEstadoPedido) {
		this.descEstadoPedido = descEstadoPedido;
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
	 * @return Returns the estadoPedido.
	 */
	public int getEstadoPedido() {
		return estadoPedido;
	}
	/**
	 * @param estadoPedido The estadoPedido to set.
	 */
	public void setEstadoPedido(int estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	/**
	 * @return Returns the fechaDespacho.
	 */
	public String getFechaDespacho() {
		return fechaDespacho;
	}
	/**
	 * @param fechaDespacho The fechaDespacho to set.
	 */
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}
	
	/**
	 * @return Returns the horaDespacho.
	 */
	public String getHoraDespacho() {
		return horaDespacho;
	}
	/**
	 * @param horaDespacho The horaDespacho to set.
	 */
	public void setHoraDespacho(String horaDespacho) {
		this.horaDespacho = horaDespacho;
	}
	/**
	 * @return Returns the fechaPedido.
	 */
	public String getFechaPedido() {
		return fechaPedido;
	}
	/**
	 * @param fechaPedido The fechaPedido to set.
	 */
	public void setFechaPedido(String fechaPedido) {
		this.fechaPedido = fechaPedido;
	}
	/**
	 * @return Returns the horaPedido.
	 */
	public String getHoraPedido() {
		return horaPedido;
	}
	/**
	 * @param horaPedido The horaPedido to set.
	 */
	public void setHoraPedido(String horaPedido) {
		this.horaPedido = horaPedido;
	}
	/**
	 * @return Returns the pedidos.
	 */
	public HashMap getPedidos() {
		return pedidos;
	}
	/**
	 * @param pedidos The pedidos to set.
	 */
	public void setPedidos(HashMap pedidos) {
		try{
			if( this.pedidos == null )
				this.pedidos = new HashMap();
			
			this.pedidos.clear();
			this.pedidos.putAll(pedidos);
			}
			catch(NullPointerException  e){
				e.printStackTrace();
			}		
	}
	
	public int getNumPedidos(){
		if(this.pedidos == null )
			return -1;
		
		if(this.pedidos.size()==0)
			return -1;
		
		return Integer.parseInt(this.pedidos.get("numRegistros")+"");
	}
	
	
	/**
	 * @return Returns the urgente.
	 */
	public int getUrgente() {
		return urgente;
	}
	/**
	 * @param urgente The urgente to set.
	 */
	public void setUrgente(int urgente) {
		this.urgente = urgente;
	}
	/**
	 * @return Returns the usuarioDespacho.
	 */
	public String getUsuarioDespacho() {
		return usuarioDespacho;
	}
	/**
	 * @param usuarioDespacho The usuarioDespacho to set.
	 */
	public void setUsuarioDespacho(String usuarioDespacho) {
		this.usuarioDespacho = usuarioDespacho;
	}
	/**
	 * @return Returns the usuarioPedido.
	 */
	public String getUsuarioPedido() {
		return usuarioPedido;
	}
	/**
	 * @param usuarioPedido The usuarioPedido to set.
	 */
	public void setUsuarioPedido(String usuarioPedido) {
		this.usuarioPedido = usuarioPedido;
	}

	/**
	 * @return Returns the orderBy.
	 */
	public String getOrderByField() {
		return orderByField;
	}
	/**
	 * @param orderBy The orderBy to set.
	 */
	public void setOrderByField(String orderBy) {
		this.orderByField = orderBy;
		if( this.orderByField.equals(this.lastOrderByField))
			this.orderDirection = 1 - this.orderDirection;
		else{
			this.orderDirection = 0;
			this.lastOrderByField = this.orderByField; 
		}
	}
	/**
	 * @return Returns the orderDirection.
	 */
	public int getOrderDirection() {
		return orderDirection;
	}
	/**
	 * @param orderDirection The orderDirection to set.
	 */
	public void setOrderDirection(int orderDirection) {
		this.orderDirection = orderDirection;
	}	
	/**
	 * @return Returns the selectedIndex.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	/**
	 * @param selectedIndex The selectedIndex to set.
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @param centroCosto The centroCosto to set.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
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
	 * @return Returns the tipoTransaccionPedido.
	 */
	public String getTipoTransaccionPedido() {
		return tipoTransaccionPedido;
	}
	/**
	 * @param tipoTransaccionPedido The tipoTransaccionPedido to set.
	 */
	public void setTipoTransaccionPedido(String tipoTransaccionPedido) {
		this.tipoTransaccionPedido = tipoTransaccionPedido;
	}
	/**
	 * @return Returns the numAlmacenes.
	 */
	public int getNumAlmacenes() {
		return numAlmacenes;
	}
	/**
	 * @param numAlmacenes The numAlmacenes to set.
	 */
	public void setNumAlmacenes(int numAlmacenes) {
		this.numAlmacenes = numAlmacenes;
	}
	/**
	 * @return Returns the usuarioAlmacen.
	 */
	public String getUsuarioAlmacen() {
		return usuarioAlmacen;
	}
	/**
	 * @param usuarioAlmacen The usuarioAlmacen to set.
	 */
	public void setUsuarioAlmacen(String usuarioAlmacen) {
		this.usuarioAlmacen = usuarioAlmacen;
	}
	/**
	 * @return Returns the checkAnularPedido.
	 */
	public String getCheckAnularPedido() {
		return checkAnularPedido;
	}
	/**
	 * @param checkAnularPedido The checkAnularPedido to set.
	 */
	public void setCheckAnularPedido(String checkAnularPedido) {
		this.checkAnularPedido = checkAnularPedido;
	}
	/**
	 * @return Returns the checkPrioridadPedido.
	 */
	public String getCheckPrioridadPedido() {
		return checkPrioridadPedido;
	}
	/**
	 * @param checkPrioridadPedido The checkPrioridadPedido to set.
	 */
	public void setCheckPrioridadPedido(String checkPrioridadPedido) {
		this.checkPrioridadPedido = checkPrioridadPedido;
	}
	/**
	 * @return Returns the checkTerminarPedido.
	 */
	public String getCheckTerminarPedido() {
		return checkTerminarPedido;
	}
	/**
	 * @param checkTerminarPedido The checkTerminarPedido to set.
	 */
	public void setCheckTerminarPedido(String checkTerminarPedido) {
		this.checkTerminarPedido = checkTerminarPedido;
	}
	/**
	 * @return Returns the codigosArticulosInsertados.
	 */
	public String getCodigosArticulosInsertados() {
		return codigosArticulosInsertados;
	}
	/**
	 * @param codigosArticulosInsertados The codigosArticulosInsertados to set.
	 */
	public void setCodigosArticulosInsertados(String codigosArticulosInsertados) {
		this.codigosArticulosInsertados = codigosArticulosInsertados;
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
	 * @return Returns the fechaPedidoModificacion.
	 */
	public String getFechaPedidoModificacion() {
		return fechaPedidoModificacion;
	}
	/**
	 * @param fechaPedidoModificacion The fechaPedidoModificacion to set.
	 */
	public void setFechaPedidoModificacion(String fechaPedidoModificacion) {
		this.fechaPedidoModificacion = fechaPedidoModificacion;
	}
	/**
	 * @return Returns the horaPedidoModificacion.
	 */
	public String getHoraPedidoModificacion() {
		return horaPedidoModificacion;
	}
	/**
	 * @param horaPedidoModificacion The horaPedidoModificacion to set.
	 */
	public void setHoraPedidoModificacion(String horaPedidoModificacion) {
		this.horaPedidoModificacion = horaPedidoModificacion;
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
	 * @return Returns the motivoAnulacion.
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}
	/**
	 * @param motivoAnulacion The motivoAnulacion to set.
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}
	/**
	 * @return Returns the numeroIngresos.
	 */
	public int getNumeroIngresos() {
		return numeroIngresos;
	}
	/**
	 * @param numeroIngresos The numeroIngresos to set.
	 */
	public void setNumeroIngresos(int numeroIngresos) {
		this.numeroIngresos = numeroIngresos;
	}
	/**
	 * @return Returns the parejasClaseGrupo.
	 */
	public String getParejasClaseGrupo() {
		return parejasClaseGrupo;
	}
	/**
	 * @param parejasClaseGrupo The parejasClaseGrupo to set.
	 */
	public void setParejasClaseGrupo(String parejasClaseGrupo) {
		this.parejasClaseGrupo = parejasClaseGrupo;
	}
	/**
	 * @return Returns the pedidosMap.
	 */
	public HashMap getPedidosMap() {
		return pedidosMap;
	}
	/**
	 * @param pedidosMap The pedidosMap to set.
	 */
	public void setPedidosMap(HashMap pedidosMap) {
		this.pedidosMap = pedidosMap;
	}
	/**
	 * @return Retorna un elemento del mapa pedidosMap.
	 */
	public Object getPedidosMap(String key) {
		return pedidosMap.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa pedidosMap.
	 */
	public void setPedidosMap(String key,Object obj) {
		this.pedidosMap.put(key,obj);
	}
	/**
	 * @return Returns the subEstado.
	 */
	public String getSubEstado() {
		return subEstado;
	}
	/**
	 * @param subEstado The subEstado to set.
	 */
	public void setSubEstado(String subEstado) {
		this.subEstado = subEstado;
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
	 * @return Returns the logInfo.
	 */
	public String getLogInfo() {
		return logInfo;
	}
	/**
	 * @param logInfo The logInfo to set.
	 */
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	/**
	 * @return Returns the codigoArticulo.
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo The codigoArticulo to set.
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return Returns the pedidosEliminacionMap.
	 */
	public HashMap getPedidosEliminacionMap() {
		return pedidosEliminacionMap;
	}
	/**
	 * @param pedidosEliminacionMap The pedidosEliminacionMap to set.
	 */
	public void setPedidosEliminacionMap(HashMap pedidosEliminacionMap) {
		this.pedidosEliminacionMap = pedidosEliminacionMap;
	}
	/**
	 * @return Retorna un elemento del mapa pedidosEliminacionMap.
	 */
	public Object getPedidosEliminacionMap(String key) {
		return pedidosEliminacionMap.get(key);
	}
	/**
	 * @param asigna un elemento al mapa pedidosEliminacionMap .
	 */
	public void setPedidosEliminacionMap(String key,Object obj) {
		this.pedidosEliminacionMap.put(key,obj);
	}
	/**
	 * @return Returns the pedidosAntiguosMap.
	 */
	public HashMap getPedidosAntiguosMap() {
		return pedidosAntiguosMap;
	}
	/**
	 * @param pedidosAntiguosMap The pedidosAntiguosMap to set.
	 */
	public void setPedidosAntiguosMap(HashMap pedidosAntiguosMap) {
		this.pedidosAntiguosMap = pedidosAntiguosMap;
	}
	/**
	 * @return Retorna un elemento del mapa pedidosAntiguosMap.
	 */
	public Object getPedidosAntiguosMap(String key) 
	{
		return pedidosAntiguosMap.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa pedidosAntiguosMap.
	 */
	public void setPedidosAntiguosMap(String key,Object obj) {
		this.pedidosAntiguosMap.put(key,obj);
	}
	/**
	 * @return Returns the colleccionPedidos.
	 */
	public Collection getColeccionPedidos() {
		return coleccionPedidos;
	}
	/**
	 * @param colleccionPedidos The colleccionPedidos to set.
	 */
	public void setColeccionPedidos(Collection coleccionPedidos) {
		this.coleccionPedidos = coleccionPedidos;
	}
	/**
	 * @return Returns the datosAnulacion.
	 */
	public HashMap getDatosAnulacion() {
		return datosAnulacion;
	}
	/**
	 * @param datosAnulacion The datosAnulacion to set.
	 */
	public void setDatosAnulacion(HashMap datosAnulacion) {
		this.datosAnulacion = datosAnulacion;
	}
	/**
	 * @return Retorna un elemento del mapa datosAnulacion.
	 */
	public Object getDatosAnulacion(String key) {
		return datosAnulacion.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa datosAnulacion.
	 */
	public void setDatosAnulacion(String key,Object obj) {
		this.datosAnulacion.put(key,obj);
	}

	/**
	 * @return Returns the centroAtencion.
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the datosQx
	 */
	public HashMap<String, Object> getDatosQx() {
		return datosQx;
	}

	/**
	 * @param datosQx the datosQx to set
	 */
	public void setDatosQx(HashMap<String, Object> datosQx) {
		this.datosQx = datosQx;
	}
	
	/**
	 * @return the datosQx
	 */
	public Object getDatosQx(String key) {
		return datosQx.get(key);
	}

	/**
	 * @param datosQx the datosQx to set
	 */
	public void setDatosQx(String key,Object obj) {
		this.datosQx.put(key,obj);
	}

	/**
	 * @return the esQuirurgico
	 */
	public String getEsQuirurgico() {
		return esQuirurgico;
	}

	/**
	 * @param esQuirurgico the esQuirurgico to set
	 */
	public void setEsQuirurgico(String esQuirurgico) {
		this.esQuirurgico = esQuirurgico;
	}

	/**
	 * @return the devolucion
	 */
	public String[] getDevolucion() {
		return devolucion;
	}

	/**
	 * @param devolucion the devolucion to set
	 */
	public void setDevolucion(String[] devolucion) {
		this.devolucion = devolucion;
	}

	public String getModificarFecha() {
		return modificarFecha;
	}

	public void setModificarFecha(String modificarFecha) {
		this.modificarFecha = modificarFecha;
	}
	
	
}

