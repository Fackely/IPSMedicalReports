/*
 * Created on 27-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
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
import util.UtilidadTexto;
import util.inventarios.UtilidadInventarios;

/**
 * @author armando
 *
 * Princeton 27-sep-2004
 */
public class RecepcionDevolucionPedidosForm extends ValidatorForm {


	/**
	 * Variable para manejar el estado que se tienen en la aplicacion
	 */
	private String estado;
	/**
	 * Variable que indica el numero de devolucion.
	 */
	private int numeroDevolucion;

	/**
	 * Variable que almacena la fecha de la devolucion
	 */
	private String fechaDevolucion;
	/**
	 * Variable que almacena la fecha de la devolucion
	 */
	private String fechaRecepcion;
	/**
	 * Variable que almacena la hora de la devolucion
	 */
	private String horaDevolucion;
	/**
	 * Variable que almacena la hora de la devolucion
	 */
	private String horaRecepcion;
	
	/**
	 * Variable que almacena el area que realizo la devolucion.
	 */
	private String area;
	/**
	 * Variable que almacena el codigo del area que realizo la devolucion.
	 */
	private int codArea;
	/**
	 * Variable que almacena el nombre de la farmacia
	 */
	private String farmacia;
	/**
	 * Variable que almacena el codigo de la farmacia
	 */
	private int codFarmacia;
	
	/**
	 * Variable que almacena el estado de una devolucion.
	 */
	private String estadoDevolucion;
	
	/**
	 *Variable que almacena el usuario que realizo la devolucion 
	 */
	private String usuario;
	/**
	 * Variable para almacenar el usuarion que realiza la recepcion
	 */
	private String usuarioRecibe;
	/**
	 * Variable que almacena el motivo de la devolucion
	 */
	private String motivo;
	
	/**
	 * Indica si la devolucion es quirurgica
	 */
	private String esQuirurgico;

	/**
	 * Collection para obtener los datos de todas las devoluciones pendientes
	 * de recepcion
	 */
	
	private Collection coleccion;
	/**
	 * Collection para obtener los datos del detalle de una recepcion
	 * 
	 */
	private Collection coleccionDetalle;
	/**
	 * Manejo del listado de los articulos
	 */
	private HashMap articulos;
	
	/**
	 * Manejar la direccion de paginador
	 */
	private String dir;
	
	/**
	 * Valrible que almacena la ultima propiedad por la que se ordeno.
	 */
	private String ultimaPropiedad;
	/**
	 * Columna por la que se desea ordenar
	 */
	private String columna;
	
	/**
	 * 
	 */
	private String centroAtencion;
	
	
	/**
	 * 
	 */
	private boolean interfazCompras;
	
	/**
	 * 
	 */
	private int institucion;
	
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
	 * @return Retorna el columna.
	 */
	public String getColumna() {
		return columna;
	}
	/**
	 * @param columna Asigna el columna.
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}
	/**
	 * @return Retorna el ultimaPropiedad.
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}
	/**
	 * @param ultimaPropiedad Asigna el ultimaPropiedad.
	 */
	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	    {
	    ActionErrors errors= new ActionErrors();
	    if(estado.equals("guardar")||estado.equals("continuar"))
	    {
	    	for(int i=0;i<coleccionDetalle.size();i++)
	    	{
	    		try
				{
	    			//si se tiene interfaz hacer la siguiente validacion
		    		if(interfazCompras&&articulos.containsKey("tiporecepcion_"+i))
		    		{
		    			if((articulos.get("tiporecepcion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDevolucionConsignacion))
		    			{
		    				if(!articulos.containsKey("almacenConsignacion_"+i)||UtilidadTexto.isEmpty(articulos.get("almacenConsignacion_"+i)+""))
		    				{
		    					errors.add("falta campo", new ActionMessage("errors.required","El almacen de consignacion del articulo Nº "+(i+1)));
		    				}
		    				if(!articulos.containsKey("proveedorCompra_"+i)||UtilidadTexto.isEmpty(articulos.get("proveedorCompra_"+i)+""))
		    				{
		    					errors.add("falta campo", new ActionMessage("errors.required","El Proveedor del articulo Nº "+(i+1)));
		    				}
		    			}
		    			else if((articulos.get("tiporecepcion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDevolucionCompraProveedor))
		    			{
		    				if(!articulos.containsKey("proveedorCatalogo_"+i)||UtilidadTexto.isEmpty(articulos.get("proveedorCatalogo_"+i)+""))
		    				{
		    					errors.add("falta campo", new ActionMessage("errors.required","El Proveedor del articulo Nº "+(i+1)));
		    				}
		    			}
		    			
		    		}
		    		//Mt 5498
		    	    if(getArticulos().containsKey("recibidos_"+i))
		    	    {
	    		   	 if((getArticulos("recibidos_"+i)+"").equals(""))
	    			 {
	    				setArticulos("recibidos_"+i,"0");
	    			}
	    			if(Integer.parseInt(getArticulos("recibidos_"+i)+"") <0)
	    			{
	    				errors.add("recibidos_"+i, new ActionMessage("errors.integerMayorIgualQue","La cantidad recibidad del articulo Nº "+(i+1),"0"));  
	    			}
	    			if(Integer.parseInt(getArticulos("recibidos_"+i)+"") >Integer.parseInt(getArticulos("cantidad_"+i)+""))
	    			{
	    				errors.add("recibidos_"+i, new ActionMessage("errors.integerMenorIgualQue","La cantidad recibidad del articulo Nº "+(i+1),getArticulos("cantidad_"+i)+""));
	    			 }
		    	    } 
				 }
	    		catch (NumberFormatException e)
				{
	    			errors.add("recibidos_"+i+"numero no entero", new ActionMessage("errors.integer", "La cantidad recibida del articulo Nº  " +(i+1)));
				}
	    	}
	    	//Mt 5498
	    	if(estado.equals("guardar"))
	    	{
		    	  int numero = coleccionDetalle.size() - 1;
	    			if(!getArticulos().containsKey("recibidos_"+numero))
	    			{
	    				this.estado = "faltan";
	    			}
	    	}
	    }
	    return errors;
	    }

	public void reset(int institucion)
	{
		numeroDevolucion=-1;
		fechaDevolucion="";
		horaDevolucion="";
		area="";
		codArea=-1;
		usuario="";
		motivo="";
		this.esQuirurgico = "";
		coleccion=null;
		coleccionDetalle=null;
		dir="";
		columna="";
		ultimaPropiedad="numerodevolucion";
		articulos=new HashMap();
		this.centroAtencion="";
		this.interfazCompras=false;
		this.institucion=institucion;
		this.almacenesConsignacion=UtilidadInventarios.obtenerAlmacenesConsignacion(institucion);
		this.conveniosProveedor=UtilidadInventarios.obtenerConveniosProveedor(institucion);
		this.proveedorCatalogo=UtilidadInventarios.obtenerProveedoresCatalogo(institucion);
		
	}
	/**
	 * @return Retorna el estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado Asigna el estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna el colecion.
	 */
	public Collection getColeccion() {
		return coleccion;
	}
	/**
	 * @param colecion Asigna el colecion.
	 */
	public void setColeccion(Collection colecion) {
		this.coleccion = colecion;
	}
	/**
	 * @return Retorna el numeroDevolucion.
	 */
	public int getNumeroDevolucion() {
		return numeroDevolucion;
	}
	/**
	 * @param numeroDevolucion Asigna el numeroDevolucion.
	 */
	public void setNumeroDevolucion(int numeroDevolucion) {
		this.numeroDevolucion = numeroDevolucion;
	}
	/**
	 * @return Retorna el coleccionDetalle.
	 */
	public Collection getColeccionDetalle() {
		return coleccionDetalle;
	}
	/**
	 * @param coleccionDetalle Asigna el coleccionDetalle.
	 */
	public void setColeccionDetalle(Collection coleccionDetalle) {
		this.coleccionDetalle = coleccionDetalle;
	}
	/**
	 * @return Retorna el area.
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area Asigna el area.
	 */
	public void setArea(String area) 
	{
		this.area = area;
	}
	/**
	 * @return Retorna el motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo Asigna el motivo.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	/**
	 * @return Retorna el usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario Asigna el usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Retorna el fechaDevolucion.
	 */
	public String getFechaDevolucion() {
		return fechaDevolucion;
	}
	/**
	 * @param fechaDevolucion Asigna la fecha de recepcion de la devolucion.
	 */
	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	/**
	 * @return Retorna el horaDevolucion.
	 */
	public String getHoraDevolucion() {
		return horaDevolucion;
	}
	/**
	 * @param horaDevolucion Asigna el horaDevolucion.
	 */
	public void setHoraDevolucion(String horaDevolucion) {
		this.horaDevolucion = horaDevolucion;
	}
	/**
	 * @return Retorna el articulos.
	 */
	public HashMap getArticulos() {
		return articulos;
	}
	/**
	 * @param articulos Asigna el articulos.
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	/**
	 * @return Retorna objeto de acuerdo con la llave.
	 */
	public Object getArticulos(String key)
	{
		return articulos.get(key);
	}
	/**
	 * Asigna una propiedad al hashmap
	 * @param key
	 * @param value
	 */
	public void setArticulos(String key, Object value)
	{
		articulos.put(key, value);
	}

	/**
	 * @return Retorna el dir.
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @param dir Asigna el dir.
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	/**
	 * @return Retorna el codArea.
	 */
	public int getCodArea() {
		return codArea;
	}
	/**
	 * @param codArea Asigna el codArea.
	 */
	public void setCodArea(int codArea) {
		this.codArea = codArea;
	}
	/**
	 * @return Retorna el usuarioRecibe.
	 */
	public String getUsuarioRecibe() {
		return usuarioRecibe;
	}
	/**
	 * @param usuarioRecibe Asigna el usuarioRecibe.
	 */
	public void setUsuarioRecibe(String usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}
	/**
	 * @return Retorna el fechaRecepcion.
	 */
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}
	/**
	 * @param fechaRecepcion Asigna el fechaRecepcion.
	 */
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	/**
	 * @return Retorna el horaRecepcion.
	 */
	public String getHoraRecepcion() {
		return horaRecepcion;
	}
	/**
	 * @param horaRecepcion Asigna el horaRecepcion.
	 */
	public void setHoraRecepcion(String horaRecepcion) {
		this.horaRecepcion = horaRecepcion;
	}
	/**
	 * @return Retorna el codFarmacia.
	 */
	public int getCodFarmacia() {
		return codFarmacia;
	}
	/**
	 * @param codFarmacia Asigna el codFarmacia.
	 */
	public void setCodFarmacia(int codFarmacia) {
		this.codFarmacia = codFarmacia;
	}
	/**
	 * @return Retorna el estadoDevolucion.
	 */
	public String getEstadoDevolucion() {
		return estadoDevolucion;
	}
	/**
	 * @param estadoDevolucion Asigna el estadoDevolucion.
	 */
	public void setEstadoDevolucion(String estadoDevolucion) {
		this.estadoDevolucion = estadoDevolucion;
	}
	/**
	 * @return Retorna el farmacia.
	 */
	public String getFarmacia() {
		return farmacia;
	}
	/**
	 * @param farmacia Asigna el farmacia.
	 */
	public void setFarmacia(String farmacia) {
		this.farmacia = farmacia;
	}
	public String getCentroAtencion() {
		return centroAtencion;
	}
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
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
	
}
