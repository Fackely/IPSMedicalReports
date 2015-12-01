/*
 * Dic 05, 2007
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.mundo.UsuarioBasico;

/**
 *  Clase que implementa los atributos para
 * comunicar la forma con el WorkFlow, y el validate
 * de la funcionalidad
 * @author Sebastián Gómez R.
 *
 */
public class DespachoPedidoQxForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Variable para manejar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Mapa donde se almacenan las peticiones a las cuales se les desea realizar el despacho de pedidos
	 */
	private HashMap<String, Object> peticiones = new HashMap<String, Object>();
	
	/**
	 * Número de peticiones del mapa peticiones
	 */
	private int numPeticiones;
	
	/**
	 * Número de registro máximo por página
	 */
	private int maxPageItems;
	
	/**
	 * Número de la petición
	 */
	private String numeroPeticion;
	
	/**
	 * Estado de la petición
	 */
	private String estadoPeticion;
	
	/**
	 * FEcha de la cirugía de la petición
	 */
	private String fechaCirugia;
	
	/**
	 * Codigo del paciente
	 */
	private int codigoPaciente;
	
	/**
	 * Nombre del Paciente
	 */
	private String paciente;
	
	/**
	 * ID del paciente
	 */
	private String numeroId;
	
	/**
	 * Numero del Ingreso del Paciente
	 */
	private String ingreso;
	
	/**
	 * Variable que indica si la peticion tiene pedidos urgentes
	 */
	private String urgente;
	
	/**
	 * Centro de costo solicitante de los pedidos de la peticion
	 */
	private String nombreCentroCostoSolicitante;
	
	/**
	 * Código del centro de costo solicitante de los pedidos de la petición
	 */
	private int codigoCentroCostoSolicitante;
	
	/**
	 * Indicador del parametro interfaz compras
	 */
	private String manejaInterfazCompras;
	
	/**
	 * Mapa para almacenar lor artículos
	 */
	private HashMap articulos = new HashMap();
	
	/**
	 * Variable que almacena el número de artículos del despacho
	 */
	private int numArticulos;
	
	/**
	 * Mapa para almacenar los pedidos
	 */
	private HashMap pedidos = new HashMap();
	
	/**
	 * Número de pedidos
	 */
	private int numPedidos;
	
	//*******Atributos para la opcion por Area **************************************+
	
	/**
	 * Mapa para almacenar los centros de costo
	 */
	private HashMap centrosCosto = new HashMap();
	
	/**
	 * Número de centros de costo
	 */
	private int numCentrosCosto;
	
	/**
	 * Código del centro de costo que va a realizar el despacho de pedidos Qx.
	 */
	private int codigoCentroCosto;
	
	//*********Atributos para el resumen del despacho**********************************
	/**
	 * Listado de pedidos del despacho separados por comas
	 */
	private String listadoPedidos;
	
	/**
	 * Nombre del paciente de la peticion
	 */
	private String nombrePaciente;
	
	/**
	 * Fecha del despacho
	 */
	private String fechaDespacho;
	
	/**
	 * Hora del despacho
	 */
	private String horaDespacho;
	
	/**
	 * Nombre del usuario del despacho
	 */
	private String nombreUsuarioDespacho;
	
	/**
	 * Nombre de la farmacia
	 */
	private String nombreFarmacia;
	
	//*******Atributos para el manejo de interfaz compras***************************
	private HashMap<String, Object> almacenesConsignacion = new HashMap<String, Object>();
	private HashMap<String, Object> conveniosProveedor = new HashMap<String, Object>();
	private HashMap<String,Object> proveedorCatalogo= new HashMap<String, Object>();
	
	
	//*********Atributos para paginacion y ordenacion******************************
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	//****************************************************************************
	
	private boolean sinPedidos;
	
	//despacho pendiente
	private int despachoPendiente;
	
	//Consumo materiales estado
	private String consumoMateriales;
	
	/**
	 * inicializa atributos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		
		this.peticiones = new HashMap<String, Object>();
		this.numPeticiones = 0;
		this.maxPageItems = 0;
		
		this.numeroPeticion = "";
		this.estadoPeticion = "";
		this.fechaCirugia = "";
		this.codigoPaciente = 0;
		this.urgente = "";
		this.nombreCentroCostoSolicitante = "";
		this.codigoCentroCostoSolicitante = 0;
		this.paciente = "";
		this.numeroId = "";
		this.ingreso = "";
		
		
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		
		this.manejaInterfazCompras = "";
		
		this.articulos = new HashMap();
		this.numArticulos = 0;
		this.pedidos = new HashMap();
		this.numPedidos = 0;
		
		
		
		//Atributos para el manejo de interfaz compras
		this.almacenesConsignacion = new HashMap<String, Object>();
		this.conveniosProveedor = new HashMap<String, Object>();
		this.proveedorCatalogo = new HashMap<String, Object>();
		
		//Atributos para el resumen del despacho
		this.listadoPedidos = "";
		this.nombrePaciente = "";
		this.fechaDespacho = "";
		this.horaDespacho = "";
		this.nombreUsuarioDespacho = "";
		this.nombreFarmacia = "";
		
		//Atributos para la opcion por area
		this.codigoCentroCosto = 0;
		this.centrosCosto = new HashMap();
		this.numCentrosCosto = 0;
		this.sinPedidos=false;
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
	    
	    if(this.estado.equals("guardarDespacho"))
	    {
	    	
	    	HttpSession sesion = request.getSession();
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(sesion);
			int contador = 0;

		    for(int i=0; i < this.numArticulos; i++)
            { 
		    	String codigoArticulo= String.valueOf(getArticulos("codigosArt_"+i));
		    	boolean valExistConsig=false;
		    	
	    		//************************VALIDACIONES PARA LA PARTE DE INTERFAZ COMPRAS***************************************************
	    		//si se tiene interfaz hacer la siguiente validacion
	    		if(UtilidadTexto.getBoolean(this.manejaInterfazCompras))
	    		{
	    			//Tipo despacho consignacion
	    			if(getArticulos("tipoDespacho_"+i).toString().trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
	    			{
	    				if(UtilidadTexto.isEmpty(getArticulos("almacenConsignacion_"+i).toString()))
	    					errores.add("falta campo", new ActionMessage("errors.required","El almacen de consignacion del Articulo "+codigoArticulo));
	    				else
	    					valExistConsig=true;
	    				
	    				if(UtilidadTexto.isEmpty(getArticulos("convenioProveedor_"+i)+""))
	    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codigoArticulo));
	    			}
	    			//Tipo despacho compra proveedor
	    			else if(getArticulos("tipoDespacho_"+i).toString().trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
	    			{
	    				if(UtilidadTexto.isEmpty(getArticulos("convenioProveedor_"+i)+""))
	    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codigoArticulo));
	    			}
	    			
	    		}
	    		//**********************************************************************************************************************
	    		
	    		//Se toma la cantidad pedida del artículo
	    		int cantidadPedida = 0;
	    		for(int j=0;j<this.getNumPedidos();j++)
	    			if(UtilidadTexto.getBoolean(this.getPedidos("chequeado_"+j).toString()))
	    				cantidadPedida += Utilidades.convertirAEntero(this.getArticulos("cantidadPedido_"+i+"_"+j).toString(), true);
	    		
		    	int cantidadDespachada= 0;
		    	try
		    	{
		    		cantidadDespachada = Integer.parseInt(String.valueOf(getArticulos("cantidadADespachar_"+i)));
		    	}
		    	catch(Exception e)
		    	{
		    		errores.add("",new ActionMessage("errors.integer","La cantidad despacho del artículo "+codigoArticulo));
		    		cantidadDespachada = 0;
		    	}
			    if(cantidadDespachada <0)
				 	errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","La cantidad de despacho para el artículo "+codigoArticulo," 0"));
		        else if(valExistConsig)
		        {
		        	int exArticulo=Utilidades.convertirAEntero(UtilidadInventarios.getExistenciasXArticulo(Utilidades.convertirAEntero(codigoArticulo), Utilidades.convertirAEntero(getArticulos("almacenConsignacion_"+i)+""), usuario.getCodigoInstitucionInt()));
		        	if(cantidadDespachada>exArticulo)
		        		errores.add("error.inventarios.existenciasInsuficientes",new ActionMessage("error.inventarios.existenciasInsuficientes",codigoArticulo,exArticulo+"",Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(getArticulos("almacenConsignacion_"+i).toString()), usuario.getCodigoInstitucionInt())));
		        }
			    //Se verifica que no se despache más de lo que se pidió
			    if(cantidadDespachada>cantidadPedida)
			    	errores.add("errors.integerMenorIgualQue", new ActionMessage("errors.integerMenorIgualQue","La cantidad de despacho para el artículo "+codigoArticulo,"la cantidad total pedida : "+cantidadPedida));
			    
			    //Validacion del consumo
			    //Se relaciona tarea xplanner2008 => 51533
			    /**int cantidadConsumo = Utilidades.convertirAEntero(getArticulos("cantidadConsumo_"+i).toString());
			    if(cantidadConsumo!=ConstantesBD.codigoNuncaValido&&cantidadDespachada<cantidadConsumo)
			    	errores.add("", new ActionMessage("errors.MayorIgualQue","La cantidad del despacho : "+cantidadDespachada+" para el artículo "+codigoArticulo,"la cantidad del consumo : "+cantidadConsumo));**/
			    
			    //******************VALIDACIONES MANEJO DE LOTE**********************************************************************
			    if(UtilidadTexto.getBoolean(getArticulos("manejaLote_"+i)+"") && (getArticulos("lote_"+i)+"").equals(""))
			    	errores.add("falta campo", new ActionMessage("errors.required","El lote del Articulo "+codigoArticulo));
			    //**************************************************************************************************************************
			    
			   //******************VALIDACIONES MANEJO DE FECHA DE VENCIMIENTO*************************************************************** 
			    if(UtilidadTexto.getBoolean(getArticulos("manejaFechaVencimiento_"+i)+""))
			    {
				    if(getArticulos("fechaVencimiento_"+i).toString().equals(""))
				    	errores.add("falta campo", new ActionMessage("errors.required","La fecha de Vencimiento del Articulo "+codigoArticulo));
				    //si maneja fecha de venciminto, y es un lote-fechav generico en null, el lote y fechav seran ' ' 
				    else if(!UtilidadFecha.validarFecha(getArticulos("fechaVencimiento_"+i)+""))
				    	errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","La fecha de Vencimiento del Articulo "+codigoArticulo));
			    }
			    //*********************************************************************************************************************
			    
			    ///*****VALIDACION DE CIERRE DE INVENTARIOS*****************
		        if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt())){
		        	//si existe cierre hay error!!!
		           errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
		        //*********************************************************+
		        }
		        
				// Mt 5595 Diferencia entre consumo y despacho por artículo
				// Validacion Consumo finalizado
				if (articulos.containsKey("consumoFinalizado_0")) {
					if ((articulos.get("consumoFinalizado_0") + "").equals("S")) {

						int consumo = Integer.parseInt(articulos.get("cantidadConsumo_" + i) + "");
						int despacho = Integer.parseInt(articulos.get("cantidadADespachar_" + i) + "");
						// validar si no existe consumo
						if (consumo < 0) {
                             
							consumo = 0;
						}
						if (despacho != consumo) {
							
							if(contador <1){
							    errores.add("", new ActionMessage("error.despachoMed.despachoConsumo", ""));
								contador++;
							}
						}
					}
				}
            }
	    	
	    }
	    return errores;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the numPeticiones
	 */
	public int getNumPeticiones() {
		return numPeticiones;
	}

	/**
	 * @param numPeticiones the numPeticiones to set
	 */
	public void setNumPeticiones(int numPeticiones) {
		this.numPeticiones = numPeticiones;
	}

	/**
	 * @return the peticiones
	 */
	public HashMap<String, Object> getPeticiones() {
		return peticiones;
	}

	/**
	 * @param peticiones the peticiones to set
	 */
	public void setPeticiones(HashMap<String, Object> peticiones) {
		this.peticiones = peticiones;
	}
	
	/**
	 * @return the peticiones
	 */
	public Object getPeticiones(String key) {
		return peticiones.get(key);
	}

	/**
	 * @param peticiones the peticiones to set
	 */
	public void setPeticiones(String key,Object obj) {
		this.peticiones.put(key,obj);
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the numeroPeticion
	 */
	public String getNumeroPeticion() {
		return numeroPeticion;
	}

	/**
	 * @param numeroPeticion the numeroPeticion to set
	 */
	public void setNumeroPeticion(String numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}



	/**
	 * @return the estadoPeticion
	 */
	public String getEstadoPeticion() {
		return estadoPeticion;
	}

	/**
	 * @param estadoPeticion the estadoPeticion to set
	 */
	public void setEstadoPeticion(String estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}

	/**
	 * @return the fechaCirugia
	 */
	public String getFechaCirugia() {
		return fechaCirugia;
	}

	/**
	 * @param fechaCirugia the fechaCirugia to set
	 */
	public void setFechaCirugia(String fechaCirugia) {
		this.fechaCirugia = fechaCirugia;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the codigoCentroCostoSolicitante
	 */
	public int getCodigoCentroCostoSolicitante() {
		return codigoCentroCostoSolicitante;
	}

	/**
	 * @param codigoCentroCostoSolicitante the codigoCentroCostoSolicitante to set
	 */
	public void setCodigoCentroCostoSolicitante(int codigoCentroCostoSolicitante) {
		this.codigoCentroCostoSolicitante = codigoCentroCostoSolicitante;
	}

	/**
	 * @return the nombreCentroCostoSolicitante
	 */
	public String getNombreCentroCostoSolicitante() {
		return nombreCentroCostoSolicitante;
	}

	/**
	 * @param nombreCentroCostoSolicitante the nombreCentroCostoSolicitante to set
	 */
	public void setNombreCentroCostoSolicitante(String nombreCentroCostoSolicitante) {
		this.nombreCentroCostoSolicitante = nombreCentroCostoSolicitante;
	}



	/**
	 * @return the manejaInterfazCompras
	 */
	public String getManejaInterfazCompras() {
		return manejaInterfazCompras;
	}

	/**
	 * @param manejaInterfazCompras the manejaInterfazCompras to set
	 */
	public void setManejaInterfazCompras(String manejaInterfazCompras) {
		this.manejaInterfazCompras = manejaInterfazCompras;
	}

	/**
	 * @return the articulos
	 */
	public HashMap getArticulos() {
		return articulos;
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	
	/**
	 * @return the articulos
	 */
	public Object getArticulos(String key) {
		return articulos.get(key);
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(String key,Object obj) {
		this.articulos.put(key,obj);
	}

	/**
	 * @return the numArticulos
	 */
	public int getNumArticulos() {
		return numArticulos;
	}

	/**
	 * @param numArticulos the numArticulos to set
	 */
	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
	}

	/**
	 * @return the numPedidos
	 */
	public int getNumPedidos() {
		return numPedidos;
	}

	/**
	 * @param numPedidos the numPedidos to set
	 */
	public void setNumPedidos(int numPedidos) {
		this.numPedidos = numPedidos;
	}

	/**
	 * @return the pedidos
	 */
	public HashMap getPedidos() {
		return pedidos;
	}

	/**
	 * @param pedidos the pedidos to set
	 */
	public void setPedidos(HashMap pedidos) {
		this.pedidos = pedidos;
	}
	
	/**
	 * @return the pedidos
	 */
	public Object getPedidos(String key) {
		return pedidos.get(key);
	}

	/**
	 * @param pedidos the pedidos to set
	 */
	public void setPedidos(String key,Object obj) {
		this.pedidos.put(key,obj);
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
	public Object getAlmacenesConsignacion(String key) {
		return almacenesConsignacion.get(key);
	}

	/**
	 * @param almacenesConsignacion the almacenesConsignacion to set
	 */
	public void setAlmacenesConsignacion(String key,Object obj) {
		this.almacenesConsignacion.put(key, obj);
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
	public Object getConveniosProveedor(String key) {
		return conveniosProveedor.get(key);
	}

	/**
	 * @param conveniosProveedor the conveniosProveedor to set
	 */
	public void setConveniosProveedor(String key,Object obj) {
		this.conveniosProveedor.put(key,obj);
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
	public void setProveedorCatalogo(String key,Object obj) {
		this.proveedorCatalogo.put(key,obj);
	}

	/**
	 * @return the urgente
	 */
	public String getUrgente() {
		return urgente;
	}

	/**
	 * @param urgente the urgente to set
	 */
	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}

	/**
	 * @return the listadoPedidos
	 */
	public String getListadoPedidos() {
		return listadoPedidos;
	}

	/**
	 * @param listadoPedidos the listadoPedidos to set
	 */
	public void setListadoPedidos(String listadoPedidos) {
		this.listadoPedidos = listadoPedidos;
	}

	/**
	 * @return the fechaDespacho
	 */
	public String getFechaDespacho() {
		return fechaDespacho;
	}

	/**
	 * @param fechaDespacho the fechaDespacho to set
	 */
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}

	/**
	 * @return the horaDespacho
	 */
	public String getHoraDespacho() {
		return horaDespacho;
	}

	/**
	 * @param horaDespacho the horaDespacho to set
	 */
	public void setHoraDespacho(String horaDespacho) {
		this.horaDespacho = horaDespacho;
	}

	/**
	 * @return the nombreFarmacia
	 */
	public String getNombreFarmacia() {
		return nombreFarmacia;
	}

	/**
	 * @param nombreFarmacia the nombreFarmacia to set
	 */
	public void setNombreFarmacia(String nombreFarmacia) {
		this.nombreFarmacia = nombreFarmacia;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * @return the nombreUsuarioDespacho
	 */
	public String getNombreUsuarioDespacho() {
		return nombreUsuarioDespacho;
	}

	/**
	 * @param nombreUsuarioDespacho the nombreUsuarioDespacho to set
	 */
	public void setNombreUsuarioDespacho(String nombreUsuarioDespacho) {
		this.nombreUsuarioDespacho = nombreUsuarioDespacho;
	}

	/**
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	
	/**
	 * @return the centrosCosto
	 */
	public Object getCentrosCosto(String key) {
		return centrosCosto.get(key);
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(String key,Object obj) {
		this.centrosCosto.put(key,obj);
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return the numCentrosCosto
	 */
	public int getNumCentrosCosto() {
		return numCentrosCosto;
	}

	/**
	 * @param numCentrosCosto the numCentrosCosto to set
	 */
	public void setNumCentrosCosto(int numCentrosCosto) {
		this.numCentrosCosto = numCentrosCosto;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the numeroId
	 */
	public String getNumeroId() {
		return numeroId;
	}

	/**
	 * @param numeroId the numeroId to set
	 */
	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	/**
	 * @return the paciente
	 */
	public String getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo sinPedidos
	
	 * @return retorna la variable sinPedidos 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isSinPedidos() {
		return sinPedidos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo sinPedidos
	
	 * @param valor para el atributo sinPedidos 
	 * @author Angela Maria Aguirre 
	 */
	public void setSinPedidos(boolean sinPedidos) {
		this.sinPedidos = sinPedidos;
	}

	public int getDespachoPendiente() {
		return despachoPendiente;
	}

	public void setDespachoPendiente(int despachoPendiente) {
		this.despachoPendiente = despachoPendiente;
	}

	public String getConsumoMateriales() {
		return consumoMateriales;
	}

	public void setConsumoMateriales(String consumoMateriales) {
		this.consumoMateriales = consumoMateriales;
	}
}
