/*
 * Creado  27/09/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.pedidos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ResultadoBoolean;
import util.UtilidadFecha;

/**
 * Clase para manejar
 *
 * @version 1.0, 27/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Arlen L&oacute;pez Correa.</a>
 */
@SuppressWarnings("unchecked")
public class PedidosInsumosForm extends ActionForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Numero del pedido
	 */
	private int numeroPedido;
	
	/**
	 * codigo del centro de costo
	 */
	private int centroCosto;
	
	/**
	 * Nombre del centro de que costo que solicita. 
	 */
	private String nombreCentroCosto;
	
	/**
	 * nombre del centro de costo que despacaha
	 */
	private String  nombreFarmacia;
	
	/**
	 * codigo del centro de costo que despacha el pedido
	 */
	private int farmacia;
	
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
	 * almacena el numero de insumos insertados.
	 */
	private int numeroIngresos;
	
	/**
	 * Almacena todos los datos correspondientes a la Descripcion del Articulo
	 * codigo, descripci&oacute;n.
	 */
	private String articulo;
	
	/**
	 * Unidad de Medida.
	 */
	private String unidadMedida;
	
	/**
	 * Concentraci&oacute;n del Articulo.
	 */
	private String concentracion;
	
	/**
	 * almacena el nombre de la naturaleza
	 * del articulo
	 */	
	private String naturaleza;
	
	/**
	 * Forma farmace&uacute;tica del Articulo.
	 */
	private String formaFarmaceutica;

	/**
	 * Cantidad Pedida del articulo
	*/
	private int CantidadPedida;
	
	/**
	 * Almacena todos los datos de pedidos que se van a insertar a la BD.
	 */
	private HashMap pedidosMap;
	
	/**
	 * Codigo del articulo.
	 */
	private String codigoArticulo;
	
	/**
	 * Codigo del pedido para modificar.
	 */
	private int codigoPedidoModificar;
	
	/**
	 * Captura las Observaciones Generales.
	 */
	private String observacionesGenerales;
	
	/**
	 * Captura el motivo de la anulacion del pedido
	 */
	private String motivoAnulacion;
	
	
	/**
	 * Fecha  en la que se realizo la solicitud del pedido.
	 */
	private String fechaPedido;
	
	/**
	 * Hora del pedido
	 */
	private String horaPedido;
	
	private String fechaHoraPedido;

	/**
	 * Fecha y Hora en la que se grabo la solicitud del pedido.
	 */
	private String fechaHoraGrabacion;
	
	/**
	 * Indica el sub estado de formulario dentro de pedidos de insumos
	 */
	private String subEstado;
		
	/**
	 * indicador de existencia en la base de datos
	 */
	private boolean dbExist;
	/**
	 * accion que se realizara con este articulo en la base de datos, esto dependera de si existe o no en el repositorio
	 */
	private String dbAction;
	
	
	private String logInfo;
	
	/**
	 * para mantener los datos, cuando se navega con el pager
	 */
	private String linkSiguiente;
	
	/**
	 * numero de registros en el pager
	 */
	private final int maxPageItems = 7;
	
	/**
	 * para la navegación del pager
	 */
	private int offset;
	
	/**
	 * Institucion que aplica la funcionalidad
	 */
	private String institucion;
	
	/**
	 * Centro de Atencion de la sesion
	 */
	private String centroAtencion;
	
	/**
	 * almacena la consulta de un tipo de pedido.
	 */
	private Collection coleccionPedidos;
	
	//*********ATRIBUTOS PROPIOS DE VALIDACION INVENTARIOS*********
	/**
	 * Objeto que almacena los almacenes validos
	 */
	private HashMap listaAlmacenes = new HashMap();
	
	/**
	 * Número de almacenes del listado de almacenes
	 */
	private int numAlmacenes;
	
	/**
	 * Número de centros costo válidos para pedidos
	 */
	private int numCentrosCosto;
	
	/**
	 * Se almacena el código del tipoTransaccion Pedido
	 */
	private String tipoTransaccionPedido;
	
	/**
	 * Registro de los codigos de articulos insertados
	 */
	private String codigosArticulosInsertados;
	
	/**
	 * prejas de interseccion entre el centro costo y almacen
	 */
	private String parejasClaseGrupo;
	
	/**
	 * Permitir Modificar la fecha
	 */
	private String modificarFecha;
	//***************************************************************
	
   
	public void reset()
	{
	    this.estado="";
	    this.centroCosto=0;
	    this.farmacia=0;
	    this.numeroPedido=0;
	    this.checkTerminarPedido="off";
	    this.checkAnularPedido="off";
	    this.numeroIngresos=0;
	    this.pedidosMap=new HashMap();
	    this.codigoArticulo="";
	    this.observacionesGenerales="";
	    this.motivoAnulacion = "";
	    this.checkPrioridadPedido="";
	    this.formaFarmaceutica="";
	    this.logInfo = "";
	    this.CantidadPedida = 0;
	    this.articulo="";
	    this.fechaPedido = 	util.UtilidadFecha.getFechaActual() ;
	    this.horaPedido = util.UtilidadFecha.getHoraActual();
	    this.fechaHoraPedido = util.UtilidadFecha.getFechaActual()+"-"+util.UtilidadFecha.getHoraActual();
	    this.fechaHoraGrabacion= 	util.UtilidadFecha.getFechaActual() + "-" +util.UtilidadFecha.getHoraActual();
	    this.coleccionPedidos= null;;
	    this.nombreCentroCosto="";
	    this.nombreFarmacia="";
	    this.subEstado = "";
	    this.dbAction = "add";
	    this.dbExist = false;
	    this.naturaleza = "";
	    this.linkSiguiente = "";
	    this.offset = 0;
	    
	    this.institucion = "0";
	    this.centroAtencion = "0";
	    
	    //atributos de validacion de inventarios
	    this.listaAlmacenes = new HashMap();
	    this.numAlmacenes = 0;
	    this.numCentrosCosto = 0;
	    this.tipoTransaccionPedido = "";
	    this.codigosArticulosInsertados = "";
	    this.parejasClaseGrupo = "";
	    this.modificarFecha="";
	}
	
	/**
	 * Validaciones.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		boolean existeErrorFechaHora = false;
		// @todo hacer las validaciones de datos
		
		if(estado.equals("salirGuardar") || estado.equals("guardar_modificacion")) 
		{
		  if(getNumeroIngresos() == 0)
		  {
		      errores.add("campo articulo vacio", new ActionMessage("errors.required","El articulo "));
		  }
		  if(this.checkAnularPedido.equals("on"))
		  {
		  	if(this.motivoAnulacion.equals(""))
		  			errores.add("motivo de la anulacion vacio", new ActionMessage("errors.required","El campo Motivo de la Anulacion "));
		  }
			
		  if(this.farmacia == -1)
		  {
		      errores.add("campo farmacia vacio", new ActionMessage("errors.required","El campo farmacia "));
		  }
		  
		  if (this.horaPedido.equals(""))
		  {
		      errores.add("campo farmacia vacio", new ActionMessage("errors.required","La hora del pedido "));
		      existeErrorFechaHora = true;
		  }
		  
		  if (this.fechaPedido.equals(""))
		  {
		      errores.add("campo farmacia vacio", new ActionMessage("errors.required","La fecha del pedido "));
		      existeErrorFechaHora = true;
		  }
		  
		  if(!existeErrorFechaHora)
		  {
			  ResultadoBoolean res= UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),this.fechaPedido,this.horaPedido);
			  if(!res.isTrue())
			  {
			      errores.add("error fecha/hora pedido", new ActionMessage("errores.fechaOHoraSuperiorAlSistema"));
			  }
		  }
		  
		  if(!UtilidadFecha.validarFecha(this.fechaPedido)&&!existeErrorFechaHora)
		  {
		      errores.add("Fecha pedido.", new ActionMessage("errors.formatoFechaInvalido", "del pedido "+this.fechaPedido));
		  }
			if(getNumeroIngresos() > 0)
			{  
		      boolean error=false;
			    for(int k=0; k < getNumeroIngresos(); k++)
			    { 
			       if((getPedidosMap("cantidadDespacho_"+k) + "").equals(""))
			       {
			           errores.add("", new ActionMessage("errors.required","El campo cantidad pedida del Articulo "+getPedidosMap("descripcionArticulo_"+k)));
			           error=true;
			       }
			       
			       if(!error)
			       {
				       String dato= String.valueOf(getPedidosMap("cantidadDespacho_"+k));
				       String[] dato2= String.valueOf(getPedidosMap("articulo_"+k)).split("-");
				       try
				       {
				       		if(Integer.parseInt(dato) < 0)
				       		{
				       			errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","La cantidad pedida del Articulo "+getPedidosMap("descripcionArticulo_"+k),"0"));  
				       		}
				       		if(Integer.parseInt(dato) == 0)
				       		{
				       			errores.add("cantidad igual a 0", new ActionMessage("errors.debeSerNumeroMayor","La cantidad pedida para el articulo "+getPedidosMap("descripcionArticulo_"+k)," 0 "));  
				       		}
				        }
				       catch(NumberFormatException e)
				       {
				           errores.add("numeroNoEntero", new ActionMessage("errors.integer", "La cantidad pedida del insumo " + dato2[0]));
				       }
			       }
			    }
		    }
        }
		return errores;
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
	 * @return Returns the dbAction.
	 */
	public String getDbAction() {
		return dbAction;
	}
	/**
	 * @param dbAction The dbAction to set.
	 */
	public void setDbAction(String dbAction) {
		this.dbAction = dbAction;
	}
	/**
	 * @return Returns the dbExist.
	 */
	public boolean getDbExist() {
		return dbExist;
	}
	/**
	 * @param dbExist The dbExist to set.
	 */
	public void setDbExist(boolean dbExist) {
		this.dbExist = dbExist;
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
     * @return Retorna  numeroIngresos.
     */
    public int getNumeroIngresos()
    {
        return numeroIngresos;
    }
    /**
     * @param numeroIngresos asigna numeroIngresos.
     */
    public void setNumeroIngresos(int numeroIngresos)
    {
        this.numeroIngresos = numeroIngresos;
    }
    
	/**
	 * @return Returns the cantidadPedida.
	 */
	public int getCantidadPedida() {
		return CantidadPedida;
	}
	/**
	 * @param cantidadPedida The cantidadPedida to set.
	 */
	public void setCantidadPedida(int cantidadPedida) {
		CantidadPedida = cantidadPedida;
	}
    /**
     * @return Retorna  estado.
     */
    public String getEstado()
    {
        return estado;
    }
    /**
     * @param estado asigna estado.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    /**
     * @return Retorna  numeroPedido.
     */
    public int getNumeroPedido()
    {
        return numeroPedido;
    }
    /**
     * @param numeroPedido asigna numeroPedido.
     */
    public void setNumeroPedido(int numeroPedido)
    {
        this.numeroPedido = numeroPedido;
    }
    /**
     * @return Retorna  centroCosto.
     */
    public int getCentroCosto()
    {
        return centroCosto;
    }
    /**
     * @param centroCosto asigna centroCosto.
     */
    public void setCentroCosto(int centroCosto)
    {
        this.centroCosto = centroCosto;
    }
    /**
     * @return Retorna  farmacia.
     */
    public int getFarmacia()
    {
        return farmacia;
    }
    /**
     * @param farmacia asigna farmacia.
     */
    public void setFarmacia(int farmacia)
    {
        this.farmacia = farmacia;
    }
    /**
     * @return Retorna  checkTerminarPedido.
     */
    public String getCheckTerminarPedido()
    {
        return checkTerminarPedido;
    }
    /**
     * @param checkTerminarPedido asigna checkTerminarPedido.
     */
    public void setCheckTerminarPedido(String checkTerminarPedido)
    {
        this.checkTerminarPedido = checkTerminarPedido;
    }
    /**
     * @return Retorna  articulo.
     */
    public String getArticulo()
    {
        return articulo;
    }
    /**
     * @param articulo asigna articulo.
     */
    public void setArticulo(String articulo)
    {
        this.articulo = articulo;
    }
    /**
     * @return Retorna  unidadMedida.
     */
    public String getUnidadMedida()
    {
        return unidadMedida;
    }
    /**
     * @param unidadMedida asigna unidadMedida.
     */
    public void setUnidadMedida(String unidadMedida)
    {
        this.unidadMedida = unidadMedida;
    }
    /**
     * @return Retorna  concentracion.
     */
    public String getConcentracion()
    {
        return concentracion;
    }
    /**
     * @param concentracion asigna concentracion.
     */
    public void setConcentracion(String concentracion)
    {
        this.concentracion = concentracion;
    }
    /**
     * @return Retorna  formaFarmaceutica.
     */
    public String getFormaFarmaceutica()
    {
        return formaFarmaceutica;
    }
    /**
     * @param formaFarmaceutica asigna formaFarmaceutica.
     */
    public void setFormaFarmaceutica(String formaFarmaceutica)
    {
        this.formaFarmaceutica = formaFarmaceutica;
    }
    /**
     * @return Retorna  pedidosMap.
     */
    public HashMap getPedidosMap()
    {
        return pedidosMap;
    }
    /**
     * @param pedidosMap asigna pedidosMap.
     */
    public void setPedidosMap(HashMap pedidosMap)
    {
        this.pedidosMap = pedidosMap;
    }
    
    /**
	 * Set del mapa de pedidos insumos
	 * @param key
	 * @param value
	 */

	public void setPedidosMap(String key, Object value) 
	{
		pedidosMap.put(key, value);
	}

	/**
	 * Get del mapa de apedidos insumos
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getPedidosMap(String key) 
	{
		return pedidosMap.get(key);
	}

    /**
     * @return Retorna  codigoArticulo.
     */
    public String getCodigoArticulo()
    {
        return codigoArticulo;
    }
    /**
     * @param codigoArticulo asigna codigoArticulo.
     */
    public void setCodigoArticulo(String codigoArticulo)
    {
        this.codigoArticulo = codigoArticulo;
    }
    /**
     * @return Retorna  checkPrioridadPedido.
     */
    public String getCheckPrioridadPedido()
    {
        return checkPrioridadPedido;
    }
    /**
     * @param checkPrioridadPedido asigna checkPrioridadPedido.
     */
    public void setCheckPrioridadPedido(String checkPrioridadPedido)
    {
        this.checkPrioridadPedido = checkPrioridadPedido;
    }
    /**
     * @return Retorna  observacionesGenerales.
     */
    public String getObservacionesGenerales()
    {
        return observacionesGenerales;
    }
    /**
     * @param observacionesGenerales asigna observacionesGenerales.
     */
    public void setObservacionesGenerales(String observacionesGenerales)
    {
        this.observacionesGenerales = observacionesGenerales;
    }
    /**
     * @return Retorna  codigoPedidoModificar.
     */
    public int getCodigoPedidoModificar()
    {
        return codigoPedidoModificar;
    }
    /**
     * @param codigoPedidoModificar asigna codigoPedidoModificar.
     */
    public void setCodigoPedidoModificar(int codigoPedidoModificar)
    {
        this.codigoPedidoModificar = codigoPedidoModificar;
    }
	
    /**
     * @return Retorna fechaPedido.
     */
    public String getFechaPedido() {
        return fechaPedido;
    }
    /**
     * @param fechaPedido Asigna fechaPedido.
     */
    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    /**
     * @return Retorna horaPedido.
     */
    public String getHoraPedido() {
        return horaPedido;
    }
    /**
     * @param horaPedido Asigna horaPedido.
     */
    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
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
	 * @return Returns the nombreCentroCosto.
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}
	/**
	 * @param nombreCentroCosto The nombreCentroCosto to set.
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}
	/**
	 * @return Returns the nombreFarmacia.
	 */
	public String getNombreFarmacia() {
		return nombreFarmacia;
	}
	/**
	 * @param nombreFarmacia The nombreFarmacia to set.
	 */
	public void setNombreFarmacia(String nombreFarmacia) {
		this.nombreFarmacia = nombreFarmacia;
	}
    /**
     * @return Retorna coleccionPedidos.
     */
    public Collection getColeccionPedidos() {
        return coleccionPedidos;
    }
    /**
     * @param coleccionPedidos Asigna coleccionPedidos.
     */
    public void setColeccionPedidos(Collection coleccionPedidos) {
        this.coleccionPedidos = coleccionPedidos;
    }
    /**
     * @return Retorna naturaleza.
     */
    public String getNaturaleza() {
        return naturaleza;
    }
    /**
     * @param naturaleza Asigna naturaleza.
     */
    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }
    /**
     * @return Retorna fechaHoraPedido.
     */
    public String getFechaHoraPedido() {
        return fechaHoraPedido;
    }
    /**
     * @param fechaHoraPedido Asigna fechaHoraPedido.
     */
    public void setFechaHoraPedido(String fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
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
     * @return Retorna maxPageItems.
     */
    public int getMaxPageItems() {
        return maxPageItems;
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
	 * @return Returns the listaAlmacenes.
	 */
	public HashMap getListaAlmacenes() {
		return listaAlmacenes;
	}
	/**
	 * @param listaAlmacenes The listaAlmacenes to set.
	 */
	public void setListaAlmacenes(HashMap listaAlmacenes) {
		this.listaAlmacenes = listaAlmacenes;
	}
	/**
	 * @return Retorna un elemento del mapa listaAlmacenes.
	 */
	public Object getListaAlmacenes(String key) {
		return listaAlmacenes.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa listaAlmacenes.
	 */
	public void setListaAlmacenes(String key,Object obj) {
		this.listaAlmacenes.put(key,obj);
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
	 * @return Returns the numCentrosCosto.
	 */
	public int getNumCentrosCosto() {
		return numCentrosCosto;
	}
	/**
	 * @param numCentrosCosto The numCentrosCosto to set.
	 */
	public void setNumCentrosCosto(int numCentrosCosto) {
		this.numCentrosCosto = numCentrosCosto;
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

	public String getModificarFecha() {
		return modificarFecha;
	}

	public void setModificarFecha(String modificarFecha) {
		this.modificarFecha = modificarFecha;
	}

	
}
