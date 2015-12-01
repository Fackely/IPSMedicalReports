/*
 * DevolucionPedidosForm.java 
 * Autor			:  mdiaz
 * Creado el	:  17-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */

package com.princetonsa.actionform.pedidos;


import util.UtilidadFecha;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.pedidos.DetalleDevolucionPedidos;


/**
 * descripcion de esta clase
 *
 * @version 1.0, 17-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class DevolucionPedidosForm extends ValidatorForm implements Serializable{
	
	private Logger logger = Logger.getLogger(DevolucionPedidosForm.class);
	private String estado;
	private String devolucionSimple;
	private HashMap articulosConsulta;
	private HashMap articulosDevolucion;

	
	
	private int codigoFarmacia;
	private int codigoCentroCosto;
	private int codigoPedido;
	
	
	private int detalleCodigoPedido;
	private int detalleCodigoArticulo;
	private String detalleDescripcionArticulo;
	private String detalleUnidadMedidaArticulo;
	private int detalleSaldoDevolucion;
	private int detalleCantidadDevolucion;

	private String detalleLote;
	private String detalleFechaVencimiento;
	
//datos de la cabecera de la devolucion del pedido	
	private 	int      codigoDevolucion;
	private String  observacionesPedido;
	private String  motivo = "";
	private String  fechaDevolucion;
	private String	horaDevolucion;
	private String  fechaGrabacion;
	private String	horaGrabacion;
	private String  usuario;
	private String observaciones;
	

//	 conjunto de detalles de la devolucion del pedido
	private ArrayList detalleDevolucionPedidos;	
	
	//atributos del paginador
	private String linkSiguiente;
	private int offset;
	
	//campos para la búsqueda
	private String codigoPedidoBusqueda;
	private String pedidoInicial;
	private String pedidoFinal;
	private String fechaPedidoInicial;
	private String fechaPedidoFinal;
	private String fechaDespachoInicial;
	private String fechaDespachoFinal;
	private String usuarioPedido;
	private String usuarioDespacho;
	private HashMap usuarios = new HashMap();
	private HashMap despachos = new HashMap();
	
	private boolean ocultarSeccionBusqueda=false;
	
	private String descripcionArticulo;
	
	
	public void reset(){

		this.devolucionSimple = "no";
		
		this.codigoPedido = -1;
		this.codigoFarmacia = -1;
		this.codigoCentroCosto = -1;
		
		this.detalleCodigoPedido = -1;
		this.detalleCodigoArticulo = -1;
		this.detalleDescripcionArticulo="";
		this.detalleSaldoDevolucion = 0;
		this.detalleCantidadDevolucion = 0;
		this.detalleLote="";
		this.detalleFechaVencimiento="";
		
		this.codigoDevolucion = -1;
		this.observacionesPedido = "";
		this.usuario = "";
		this.observaciones = "";
		
		this.articulosConsulta = new HashMap();
		this.articulosDevolucion = new HashMap();
		this.detalleDevolucionPedidos = new ArrayList();	
		
		this.linkSiguiente = "";
		this.offset = 0;
		
		this.ocultarSeccionBusqueda=false;
		
		this.codigoPedidoBusqueda = "";
		clean();
		resetBusqueda();
	}
		
	public void resetBusqueda()
	{
		pedidoInicial = "";
		pedidoFinal = "";
		fechaPedidoInicial = "";
		fechaPedidoFinal = "";
		fechaDespachoInicial = "";
		fechaDespachoFinal = "";
		usuarioPedido = "";
		usuarioDespacho = "";
		usuarios = new HashMap();
		despachos = new HashMap();
	}
	
	public void clean(){
		this.motivo = "";
		this.fechaDevolucion = UtilidadFecha.getFechaActual();
		this.horaDevolucion = UtilidadFecha.getHoraActual();
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		int k, totalArticulosDevolucion, regSaldoDespacho, regCantidadDevolucion;
		String regKey, regNumeroPedido, regCodigoArticulo, regDescripcionArticulo;
		ActionErrors errores = new ActionErrors();
		
 
 
		if( estado.equals("comenzar_devolucion") || estado.equals("comenzar_devolucion_1"))
		{
			if(this.codigoFarmacia == -1){
				errores.add("debe seleccionar una farmacia", new ActionMessage("errors.notEspecific","Debe seleccionar una Farmacia"));
				this.setEstado("seleccion_farmacia");
			}

			if(this.codigoCentroCosto  == -1){
				errores.add("debe seleccionar un centro de costo", new ActionMessage("errors.notEspecific","Debe seleccionar un Centro de Costo"));
				this.setEstado("seleccion_farmacia");
			}
		}
		
		if(estado.equals("buscar_articulos"))
		{
			if(this.codigoPedidoBusqueda.equals(""))
			{
				errores.add("El numero de pedido ",new ActionMessage("error.devpedi"));
				this.setEstado("comenzar_devolucion");
			}
		}
		
		if( estado.equals("adicionar_detalle_devolucion"))
		{
				if (addArticuloDevolucion() == false )
				{
						errores.add("Detalle de devolucion repetido", new ActionMessage("errors.notEspecific","No se puede adicionar el detalle de devolucion de pedidos debido a que ya existe una entrada con el mismo numero del pedido y el mismo articulo."));
						this.setEstado("comenzar_devolucion");
				}
		}	
	
	
		if( estado.equals("guardar_devolucion") )
		{
		
	    // validacion del formato para la hora
			if( UtilidadFecha.validacionHora(this.getHoraDevolucion()).puedoSeguir == false){
		  	errores.add("hora  devolucion incorrecta", new ActionMessage("errors.formatoHoraInvalido",this.getHoraDevolucion()));
		  	this.setEstado("comenzar_devolucion");
			}
	
			// validacion del formato para la fecha y su validez de rango menor o igual a la fecha del sistema
			if( UtilidadFecha.validarFecha(this.getFechaDevolucion()) == false){
			  	errores.add("fecha devolucion incorrecta", new ActionMessage("errors.formatoFechaInvalido",this.getFechaDevolucion()));
			  	this.setEstado("comenzar_devolucion");
				}
				else{
					// comprobamos si la fecha es de la devolucion es mayor que la fecha del sistema
					if ( compareAgainstCurrentDate(this.getFechaDevolucion(), this.getHoraDevolucion()) > 0 ){
							errores.add("fecha devolucion incorrecta", new ActionMessage("errors.notEspecific", "La fecha y/o hora " +this.getFechaDevolucion() + " - " +this.getHoraDevolucion() + " debe ser menor o igual a la fecha del sistema"));
							this.setEstado("comenzar_devolucion");
					}				
				}
			
			
			totalArticulosDevolucion = articulosDevolucion.size() / 8; 
			for(k=0; k<totalArticulosDevolucion; k++ ){
				regKey = "saldo_despacho[" + k + "]";
				regSaldoDespacho = ((Integer)articulosDevolucion.get(regKey)).intValue();
				regKey = "cantidad_devolucion[" + k + "]";
				regCantidadDevolucion = ((Integer)articulosDevolucion.get(regKey)).intValue();
				regKey = "cod_pedido[" + k + "]";
				regNumeroPedido = articulosDevolucion.get(regKey) +"";
				regKey = "des_articulo[" + k + "]";
				regDescripcionArticulo = articulosDevolucion.get(regKey) +"";
				regKey = "unidad_medida_articulo[" + k + "]";
				//La siguiente variable nunca se estaba utilizando
				//regUnidadMedidaArticulo = articulosDevolucion.get(regKey) +"";
				regKey = "cod_articulo[" + k + "]";
				regCodigoArticulo = articulosDevolucion.get(regKey) +"";
			
				
				if(regCantidadDevolucion > regSaldoDespacho){
					errores.add("cantidad devolucion mayor que el saldo del despacho", new ActionMessage("errors.notEspecific", "La cantidad  de la devolucion para el articulo '" + regCodigoArticulo + "-" +regDescripcionArticulo +"'  del pedido numero " + regNumeroPedido + " excede el saldo del despacho"));
					this.setEstado("comenzar_devolucion");
				}
	
				if(regCantidadDevolucion == 0){
					errores.add("cantidad devolucion igual a cero", new ActionMessage("errors.notEspecific", "La cantidad  de la devolucion para el articulo '" + regCodigoArticulo + "-" +regDescripcionArticulo +"'  del pedido numero " + regNumeroPedido + " debe ser mayor que cero"));
					this.setEstado("comenzar_devolucion");
				}
			
			}
	
		if( motivo.equals("") ){
				errores.add("campo motivo de la devolucion requerido", new ActionMessage("errors.required", "El campo motivo de la devolucion "));
				this.setEstado("comenzar_devolucion");
			}
	
		}
		
		return errores;
	}
	

	
// metodo que compara la fecha y hora pasadas como parametros contra la fecha y hora actual	
	private int compareAgainstCurrentDate(String fecha, String hora){
		Date fechaHoraD1 = null;
		Date fechaHoraD2 = null;
		final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");

		try{
			fechaHoraD1 = dateTimeFormatter.parse(fecha  + ":" + hora);
			fechaHoraD2 = dateTimeFormatter.parse(UtilidadFecha.getFechaActual()  + ":" +UtilidadFecha.getHoraActual());
		}
		catch (java.text.ParseException e){
			logger.error("Imposible convertir las fechas para comparacion");
		}

	 return fechaHoraD1.compareTo(fechaHoraD2);
}

	
	
	
	
	private boolean existDetalleDevolucion(int codPedido, int codArticulo){
	  String regKey;
	  int regValuePedido, regValueArticulo;
		int k;
		
		for(k=0; k<(articulosDevolucion.size()/8); k++){
			regKey = "cod_pedido[" +k +"]";
			regValuePedido = ((Integer)articulosDevolucion.get(regKey)).intValue();
			regKey = "cod_articulo[" +k +"]";
			regValueArticulo = ((Integer)articulosDevolucion.get(regKey)).intValue();
			if(regValuePedido == codPedido && regValueArticulo == codArticulo)
				return true;
		}
	  
		return false;
	}
	


private boolean renameHashMapEntry(String oldName, String newName, HashMap hmap){
  Object obj = null;
	
	obj = hmap.get(oldName);
	if ( obj == null )
		return false;
	
	hmap.remove(oldName);
	hmap.put(newName, obj);
	return true;
}
	
	
	private void reorderArticulosDevolucion(){
		String actRegKey, newRegKey;
		int k, j, hmapSize;
		
		k = 0;
		j = 0;
		hmapSize = this.articulosDevolucion.size() / 8;
		while( j < hmapSize){
			
			actRegKey = "cod_pedido[" +k  + "]";
			newRegKey = "cod_pedido[" +j  + "]";
			
			if( renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion ) == true ){
				actRegKey = 	"cod_articulo[" +k + "]";			
				newRegKey = "cod_articulo[" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );
				
				actRegKey = "des_articulo[" +k + "]";  
				newRegKey = "des_articulo[" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );

				actRegKey = "unidad_medida_articulo[" +k + "]";  
				newRegKey = "unidad_medida_articulo[" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );

				actRegKey = "saldo_despacho[" +k + "]";  
				newRegKey = "saldo_despacho[" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );
				
				actRegKey = "cantidad_devolucion[" +k + "]";  
				newRegKey = "cantidad_devolucion[" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );
				
				actRegKey = "lote [" +k + "]";  
				newRegKey = "lote [" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );
				
				actRegKey = "fecha_vencimiento [" +k + "]";  
				newRegKey = "fecha_vencimiento [" +j + "]";
				renameHashMapEntry(actRegKey, newRegKey, this.articulosDevolucion );				
				j++;
			}
			k++;
		}
	}

	
	public boolean addArticuloDevolucion(){
		String regKey;
		Integer regValue;
		int lastIndex;
	
		if( this.articulosDevolucion == null )
			articulosDevolucion = new HashMap();
	
		if (existDetalleDevolucion(this.detalleCodigoPedido, this.detalleCodigoArticulo) == true)
			return false;
				 
		lastIndex = (articulosDevolucion.size()/8);
		
		// verificamos que el saldo del articulo sea diferente de cero para poder adicionarlo al HashMap
		regKey = "saldo_despacho[" +lastIndex + "]";  
		regValue = new Integer(this.detalleSaldoDevolucion);
		if(regValue.intValue() == 0)
			return true;
		
		
		regKey = "cod_pedido[" +lastIndex + "]";  
		regValue = new Integer(this.detalleCodigoPedido);
		articulosDevolucion.put(regKey, regValue);
				
		regKey = "cod_articulo[" +lastIndex + "]";  
		regValue = new Integer(this.detalleCodigoArticulo);
		articulosDevolucion.put(regKey, regValue);
		
		regKey = "des_articulo[" +lastIndex + "]";  
		articulosDevolucion.put(regKey, this.detalleDescripcionArticulo);
		
		regKey = "unidad_medida_articulo[" +lastIndex + "]";  
		articulosDevolucion.put(regKey, this.detalleUnidadMedidaArticulo);
		
		regKey = "saldo_despacho[" +lastIndex + "]";  
		regValue = new Integer(this.detalleSaldoDevolucion);
		articulosDevolucion.put(regKey, regValue);
		
		regKey = "cantidad_devolucion[" +lastIndex + "]";  
		regValue = new Integer(this.detalleCantidadDevolucion);
		articulosDevolucion.put(regKey, regValue);

		regKey = "lote[" +lastIndex + "]";  
		articulosDevolucion.put(regKey, this.detalleLote);
		
		regKey = "fecha_vencimiento[" +lastIndex + "]";  
		articulosDevolucion.put(regKey, this.detalleFechaVencimiento);
		return true;
}

	
	public void deleteDetalleDevolucion(){
	  String regKey;
	  int regValuePedido, regValueArticulo;
		int k;
		
		for(k=0; k<(articulosDevolucion.size()/8); k++){
			regKey = "cod_pedido[" +k +"]";
			regValuePedido = ((Integer)articulosDevolucion.get(regKey)).intValue();
			regKey = "cod_articulo[" +k +"]";
			regValueArticulo = ((Integer)articulosDevolucion.get(regKey)).intValue();
			
			if(regValuePedido == this.detalleCodigoPedido  && regValueArticulo == this.detalleCodigoArticulo ){
				this.articulosDevolucion.remove("cod_pedido[" +k  + "]");
				this.articulosDevolucion.remove("cod_articulo[" +k + "]");
				this.articulosDevolucion.remove("des_articulo[" +k + "]");
				this.articulosDevolucion.remove("unidad_medida_articulo[" +k + "]");
				this.articulosDevolucion.remove("lote[" +k + "]");
				this.articulosDevolucion.remove("fecha_vencimiento[" +k + "]");
				this.articulosDevolucion.remove("saldo_despacho[" +k + "]");
				this.articulosDevolucion.remove("cantidad_devolucion[" +k + "]");
				 
				reorderArticulosDevolucion();
				return;
			}
		}
	}
	
	
	public void fillDetalleDevolucionPedidos(){
	  String regKey;
	  int k, regDevolucion, regPedido, regArticulo, regCantidad;
	  String lote,fechaVencimiento;
		
		detalleDevolucionPedidos  = new ArrayList();
		regDevolucion = codigoDevolucion; 
		
		for(k=0; k<(articulosDevolucion.size()/8); k++){
				regKey = "cod_pedido[" +k +"]";
				regPedido = ((Integer)articulosDevolucion.get(regKey)).intValue();
				regKey = "cod_articulo[" +k +"]";
				regArticulo = ((Integer)articulosDevolucion.get(regKey)).intValue();
				regKey = "cantidad_devolucion[" +k +"]";
				regCantidad = ((Integer)articulosDevolucion.get(regKey)).intValue();
				regKey = "lote[" +k +"]";
				lote = articulosDevolucion.get(regKey)+"";
				regKey = "fecha_vencimiento[" +k +"]";
				fechaVencimiento = articulosDevolucion.get(regKey)+"";
		
				detalleDevolucionPedidos.add(k, new DetalleDevolucionPedidos(0, regDevolucion, regPedido, regArticulo, regCantidad,lote,fechaVencimiento));
		}
	}
	
	
	
	
	
	public HashMap getArticulosConsulta() {
		return articulosConsulta;
	}
	public void setArticulosConsulta(HashMap articulosConsulta) {
		try{
			if( this.articulosConsulta == null )
				this.articulosConsulta = new HashMap();
			
			this.articulosConsulta.clear();
			this.articulosConsulta.putAll(articulosConsulta);
			}
			catch(NullPointerException  e){
				e.printStackTrace();
			}
	}
	public int getNumArticulosConsulta(){
		if(this.articulosConsulta == null )
			return 0;
		
		return (articulosConsulta.size()-1);//resto 1 por el campo observacion.
	}
	
	
	public HashMap getArticulosDevolucion() {
		return articulosDevolucion;
	}
	
	public void setArticulosDevolucion(HashMap articulosDevolucion) {
		try{
			if( this.articulosDevolucion == null )
				this.articulosDevolucion = new HashMap();
			
			this.articulosDevolucion.clear();
			this.articulosDevolucion.putAll(articulosDevolucion);
			}
			catch(NullPointerException  e){
				e.printStackTrace();
			}
	}
	

	

	public int getCantidadDevolucion(int index){
		return ((Integer)articulosDevolucion.get("cantidad_devolucion[" + index + "]")).intValue();
	}
		
	
	public void setCantidadDevolucion(int index, int value) {
			if( articulosDevolucion == null )
				articulosDevolucion = new HashMap();

  		articulosDevolucion.put("cantidad_devolucion[" + index + "]", new Integer(value));
	}
	
	
	
	
	
	
	public int getNumArticulosDevolucion(){
		if(this.articulosDevolucion == null )
			return 0;
		
		return this.articulosDevolucion.size();
	}
	
	
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	
public int getCodigoDevolucion() {
	return codigoDevolucion;
}
public void setCodigoDevolucion(int codigoDevolucion) {
	this.codigoDevolucion = codigoDevolucion;
}

	public int getCodigoFarmacia() {
		return codigoFarmacia;
	}
	public void setCodigoFarmacia(int codigoFarmacia) {
		this.codigoFarmacia = codigoFarmacia;
	}

	public ArrayList getDetalleDevolucionPedidos() {
	return detalleDevolucionPedidos;
}
public void setDetalleDevolucionPedidos(ArrayList detalleDevolucionPedidos) {
	this.detalleDevolucionPedidos = detalleDevolucionPedidos;
}

	
public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaDevolucion() {
		return fechaDevolucion;
	}
	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public String getFechaGrabacion() {
		return fechaGrabacion;
	}
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}
	
	public String getHoraDevolucion() {
		return horaDevolucion;
	}
	public void setHoraDevolucion(String horaDevolucion) {
		this.horaDevolucion = horaDevolucion;
	}
	public String getHoraGrabacion() {
		return horaGrabacion;
	}
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

		public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
		public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
 public int getCodigoPedido() {
		return codigoPedido;
	}
	public void setCodigoPedido(int codigoPedido) {
		this.codigoPedido = codigoPedido;
	}
	
	public int getDetalleCodigoArticulo() {
		return detalleCodigoArticulo;
	}
	public void setDetalleCodigoArticulo(int detalleCodigoArticulo) {
		this.detalleCodigoArticulo = detalleCodigoArticulo;
	}
	public int getDetalleCodigoPedido() {
		return detalleCodigoPedido;
	}
	public void setDetalleCodigoPedido(int detalleCodigoPedido) {
		this.detalleCodigoPedido = detalleCodigoPedido;
	}
	public int getDetalleCantidadDevolucion() {
		return detalleCantidadDevolucion;
	}
	public void setDetalleCantidadDevolucion(int detalleCantidadDevolucion) {
		this.detalleCantidadDevolucion = detalleCantidadDevolucion;
	}
	public int getDetalleSaldoDevolucion() {
		return detalleSaldoDevolucion;
	}
	public void setDetalleSaldoDevolucion(int detalleSaldoDevolucion) {
		this.detalleSaldoDevolucion = detalleSaldoDevolucion;
	}
	public String getDetalleDescripcionArticulo() {
		return detalleDescripcionArticulo;
	}
	public void setDetalleDescripcionArticulo(String detalleDescripcionArticulo) {
		this.detalleDescripcionArticulo = detalleDescripcionArticulo;
	}
	public String getObservacionesPedido() {
		return observacionesPedido;
	}
	public void setObservacionesPedido(String observacionesPedido) {
		this.observacionesPedido = observacionesPedido;
	}
	public String getDevolucionSimple() {
		return devolucionSimple;
	}
	public void setDevolucionSimple(String devolucionSimple) {
		this.devolucionSimple = devolucionSimple;
	}
	public String getDetalleUnidadMedidaArticulo() {
		return detalleUnidadMedidaArticulo;
	}
	public void setDetalleUnidadMedidaArticulo(
			String detalleUnidadMedidaArticulo) {
		this.detalleUnidadMedidaArticulo = detalleUnidadMedidaArticulo;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}


	/**
	 * @return Returns the codigoPedidoBusqueda.
	 */
	public String getCodigoPedidoBusqueda() {
		return codigoPedidoBusqueda;
	}


	/**
	 * @param codigoPedidoBusqueda The codigoPedidoBusqueda to set.
	 */
	public void setCodigoPedidoBusqueda(String codigoPedidoBusqueda) {
		this.codigoPedidoBusqueda = codigoPedidoBusqueda;
	}

	/**
	 * @return Returns the fechaDespachoFinal.
	 */
	public String getFechaDespachoFinal() {
		return fechaDespachoFinal;
	}

	/**
	 * @param fechaDespachoFinal The fechaDespachoFinal to set.
	 */
	public void setFechaDespachoFinal(String fechaDespachoFinal) {
		this.fechaDespachoFinal = fechaDespachoFinal;
	}

	/**
	 * @return Returns the fechaDespachoInicial.
	 */
	public String getFechaDespachoInicial() {
		return fechaDespachoInicial;
	}

	/**
	 * @param fechaDespachoInicial The fechaDespachoInicial to set.
	 */
	public void setFechaDespachoInicial(String fechaDespachoInicial) {
		this.fechaDespachoInicial = fechaDespachoInicial;
	}

	/**
	 * @return Returns the fechaPedidoFinal.
	 */
	public String getFechaPedidoFinal() {
		return fechaPedidoFinal;
	}

	/**
	 * @param fechaPedidoFinal The fechaPedidoFinal to set.
	 */
	public void setFechaPedidoFinal(String fechaPedidoFinal) {
		this.fechaPedidoFinal = fechaPedidoFinal;
	}

	/**
	 * @return Returns the fechaPedidoInicial.
	 */
	public String getFechaPedidoInicial() {
		return fechaPedidoInicial;
	}

	/**
	 * @param fechaPedidoInicial The fechaPedidoInicial to set.
	 */
	public void setFechaPedidoInicial(String fechaPedidoInicial) {
		this.fechaPedidoInicial = fechaPedidoInicial;
	}

	/**
	 * @return Returns the pedidoFinal.
	 */
	public String getPedidoFinal() {
		return pedidoFinal;
	}

	/**
	 * @param pedidoFinal The pedidoFinal to set.
	 */
	public void setPedidoFinal(String pedidoFinal) {
		this.pedidoFinal = pedidoFinal;
	}

	/**
	 * @return Returns the pedidoInicial.
	 */
	public String getPedidoInicial() {
		return pedidoInicial;
	}

	/**
	 * @param pedidoInicial The pedidoInicial to set.
	 */
	public void setPedidoInicial(String pedidoInicial) {
		this.pedidoInicial = pedidoInicial;
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
	 * @return Returns the usuarios.
	 */
	public HashMap getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios The usuarios to set.
	 */
	public void setUsuarios(HashMap usuarios) {
		this.usuarios = usuarios;
	}
	
	/**
	 * @return Retorna elemento del mapa  usuarios.
	 */
	public Object getUsuarios(String key) {
		return usuarios.get(key);
	}

	/**
	 * @param Asigna elemento al mapa usuarios.
	 */
	public void setUsuarios(String key,Object obj) {
		this.usuarios.put(key,obj);
	}

	/**
	 * @return Returns the despachos.
	 */
	public HashMap getDespachos() {
		return despachos;
	}

	/**
	 * @param despachos The despachos to set.
	 */
	public void setDespachos(HashMap despachos) {
		this.despachos = despachos;
	}
	
	/**
	 * @return Retorna elemento del mapa  despachos.
	 */
	public Object getDespachos(String key) {
		return despachos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa despachos.
	 */
	public void setDespachos(String key,Object obj) {
		this.despachos.put(key,obj);
	}

	public String getDetalleFechaVencimiento()
	{
		return detalleFechaVencimiento;
	}

	public void setDetalleFechaVencimiento(String detalleFechaVencimiento)
	{
		this.detalleFechaVencimiento = detalleFechaVencimiento;
	}

	public String getDetalleLote()
	{
		return detalleLote;
	}

	public void setDetalleLote(String detalleLote)
	{
		this.detalleLote = detalleLote;
	}

	public boolean isOcultarSeccionBusqueda() {
		return ocultarSeccionBusqueda;
	}

	public void setOcultarSeccionBusqueda(boolean ocultarSeccionBusqueda) {
		this.ocultarSeccionBusqueda = ocultarSeccionBusqueda;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	
}
