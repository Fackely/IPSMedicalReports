/*
 * Created on 11-oct-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.actionform.medicamentos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.inventarios.UtilidadInventarios;

/**
 * @author armando
 *
 * Princeton 11-oct-2004
 */
public class RecepcionDevolucionMedicamentosForm extends ValidatorForm 
{
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
	private String centroCosto;
	
	/**
	 * 
	 */
	private String centroAtencion;
	/**
	 * Variable que almacena el codigo del area que realizo la devolucion.
	 */
	private int codigoCentroCosto;
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
	 * Coleccion para obtener la devoluciones de medicamentos
	 */
	private Collection coleccion;
	/**
	 * Coleccion para obtener del detalle de las devoluciones
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
	 * Variable que contiene los datos del paciente.
	 */
	private String paciente;
	/**
	 * variable que almacena la cuenta de un paciente
	 */
	private int cuenta;
	/**
	 * variable para almacenar la cedula del paciente.
	 */
	private int cedula;
	/**
	 * Variable para almacenar el codigo del paciente.
	 */
	private int codigoPaciente;
	/**
	 * Variable para almacenar el numero de orden a la que se le esta realizando la devolucion
	 */
	private int orden;
	
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
     * mapa que contiene los mensajes de advertencia tanto para 
     * articulos con existencias negativas, articulos que no cumplen con las condiciones de 
     * stock maximo - stock minimo y punto de pedido
     */
    private HashMap mensajesAdvertenciaMap= new HashMap();
    
    /**
     * 
     */
    private boolean porPaciente;
    
    /**
     * 
     */
    private String areaFiltro;
    
    /**
     * 
     */
    private String pisoFiltro;
    
    /**
     * 
     */
    private String habitacionFiltro;
    
    /**
     * 
     */
    private String camaFiltro;
    
    /**
     * String con las observaciones
     */
    private String observaciones;
    
    /**
     *
     */
    private String forward;
    
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
	public void reset(int institucion)
	{
		this.numeroDevolucion=-1;
		this.fechaDevolucion="";
		this.horaDevolucion="";
		this.usuario="";
		this.motivo="";
		this.dir="";
		this.columna="";
		this.ultimaPropiedad="numerodevolucion";
		this.coleccion=null;
		this.coleccionDetalle=null;
		this.articulos=new HashMap();
		this.fechaRecepcion="";
		this.horaRecepcion="";
		this.centroCosto="";
		this.codigoCentroCosto=-1;
		this.farmacia="";
		this.codFarmacia=-1;
		this.usuario="";
		this.usuarioRecibe="";
		this.paciente="";
		this.cuenta=-1;
		this.centroAtencion="";
		this.interfazCompras=false;
		this.institucion=institucion;
		this.almacenesConsignacion=UtilidadInventarios.obtenerAlmacenesConsignacion(institucion);
		this.conveniosProveedor=UtilidadInventarios.obtenerConveniosProveedor(institucion);
		this.proveedorCatalogo=UtilidadInventarios.obtenerProveedoresCatalogo(institucion);
		this.porPaciente=false;
		this.areaFiltro="";
		this.pisoFiltro="";
		this.habitacionFiltro="";
		this.camaFiltro="";
		this.observaciones = "";
	}
	
     /**
     * resetea los mensajes de advertencia
     * en el resumen
     *
     */
    public void resetMensajes()
    {
        this.mensajesAdvertenciaMap= new HashMap();
    }
    
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
    {
    ActionErrors errors= new ActionErrors();
    if(estado.equals("guardar")||estado.equals("continuar"))
    {
        //validaciones de la existencia
        UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
        if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(), usuario.getCodigoInstitucionInt()))
            errors.add("Fecha", new ActionMessage("error.inventarios.existeCierreInventarios", UtilidadFecha.getFechaActual()));
        
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
	    					errors.add("falta campo", new ActionMessage("errors.required","El almacen de consignacion del articulo "+getArticulos("nomArticulo_"+i)));
	    				}
	    				if(!articulos.containsKey("proveedorCompra_"+i)||UtilidadTexto.isEmpty(articulos.get("proveedorCompra_"+i)+""))
	    				{
	    					errors.add("falta campo", new ActionMessage("errors.required","El Proveedor del articulo "+getArticulos("nomArticulo_"+i)));
	    				}
	    			}
	    			else if((articulos.get("tiporecepcion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDevolucionCompraProveedor))
	    			{
	    				if(!articulos.containsKey("proveedorCatalogo_"+i)||UtilidadTexto.isEmpty(articulos.get("proveedorCatalogo_"+i)+""))
	    				{
	    					errors.add("falta campo", new ActionMessage("errors.required","El Proveedor del articulo "+getArticulos("nomArticulo_"+i)));
	    				}
	    			}
	    			
	    		}
    			if((getArticulos("recibidos_"+i)+"").equals(""))
    				{
    					errors.add("recibidos_"+i, new ActionMessage("errors.required","Cantidad Recibida"));  
    					//setArticulos("recibidos_"+i,"0");
    				}
    			if(Integer.parseInt(getArticulos("recibidos_"+i)+"") <0)
    				{
    				errors.add("recibidos_"+i, new ActionMessage("errors.integerMayorIgualQue","La cantidad recibida del articulo "+getArticulos("nomArticulo_"+i),"0"));  
    				}
    			if(Integer.parseInt(getArticulos("recibidos_"+i)+"") >Integer.parseInt(getArticulos("cantidad_"+i)+""))
    			{
    				errors.add("recibidos_"+i, new ActionMessage("errors.integerMenorIgualQue","La cantidad recibidad del articulo "+getArticulos("nomArticulo_"+i),getArticulos("cantidad_"+i)+""));
    			}
    			
				}
    		catch (NumberFormatException e)
				{
    				errors.add("recibidos_"+i+"numero no entero", new ActionMessage("errors.integer", "La cantidad recibida del articulo Nº  " +getArticulos("nomArticulo_"+i)));
				}
    	}
    }
    return errors;
    }
	/**
	 * @return Retorna el coleccion.
	 */
	public Collection getColeccion() {
		return coleccion;
	}
	/**
	 * @param coleccion Asigna el coleccion.
	 */
	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
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
	 * @return Retorna el centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @param centroCosto Asigna el centroCosto.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
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
	 * @return Retorna el codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}
	/**
	 * @param codigoCentroCosto Asigna el codigoCentroCosto.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
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
	/**
	 * @return Retorna el fechaDevolucion.
	 */
	public String getFechaDevolucion() {
		return fechaDevolucion;
	}
	/**
	 * @param fechaDevolucion Asigna el fechaDevolucion.
	 */
	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
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
	 * @return Retorna el paciente.
	 */
	public String getPaciente() {
		return paciente;
	}
	/**
	 * @param paciente Asigna el paciente.
	 */
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	/**
	 * @return Retorna el cuenta.
	 */
	public int getCuenta() {
		return cuenta;
	}
	/**
	 * @param cuenta Asigna el cuenta.
	 */
	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}
	/**
	 * @return Retorna el cedula.
	 */
	public int getCedula() {
		return cedula;
	}
	/**
	 * @param cedula Asigna el cedula.
	 */
	public void setCedula(int cedula) {
		this.cedula = cedula;
	}
	/**
	 * @return Retorna el codigoPaciente.
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente Asigna el codigoPaciente.
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	/**
	 * @return Retorna el orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden Asigna el orden.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
    /**
     * Set del mapa de mensajes 
     * @param key
     * @param value
     */
    public void setMensajesAdvertenciaMap(String key, Object value) 
    {
        mensajesAdvertenciaMap.put(key, value);
    }
    /**
     * Get del mapa de mensajes 
     */
    public Object getMensajesAdvertenciaMap(String key) 
    {
        return mensajesAdvertenciaMap.get(key);
    }
    /**
     * @return Returns the mensajesAdvertenciaMap.
     */
    public HashMap getMensajesAdvertenciaMap() {
        return mensajesAdvertenciaMap;
    }
    /**
     * @param mensajesAdvertenciaMap The mensajesAdvertenciaMap to set.
     */
    public void setMensajesAdvertenciaMap(HashMap mensajesAdvertenciaMap) {
        this.mensajesAdvertenciaMap = mensajesAdvertenciaMap;
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
	public String getAreaFiltro() {
		return areaFiltro;
	}
	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}
	public String getCamaFiltro() {
		return camaFiltro;
	}
	public void setCamaFiltro(String camaFiltro) {
		this.camaFiltro = camaFiltro;
	}
	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}
	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}
	public String getPisoFiltro() {
		return pisoFiltro;
	}
	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
	public boolean isPorPaciente() {
		return porPaciente;
	}
	public void setPorPaciente(boolean porPaciente) {
		this.porPaciente = porPaciente;
	}
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return the forward
	 */
	public String getForward() {
		return forward;
	}
	/**
	 * @param forward the forward to set
	 */
	public void setForward(String forward) {
		this.forward = forward;
	}
	
}
