/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.salasCirugia.Peticion;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Sebastián Gómez
 *
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Materiales Quirúrgicos
 */
public class MaterialesQxForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MaterialesQxForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	/**
	 * indica el valor del index de un hashmap
	 */
	private String index;
	
	private String peticion;
	/**
	 * Variable usada para almacenar el código de la institucion
	 */
	private String institucion;
	
	/**
	 * Código del centro de atencion de la sesión
	 */
	private String centroAtencion;
	
	/**
	 * Variable usada para almacenar el código de la opción centro costo todos
	 */
	private String codigoOpcionTodos;
	
	/**
	 * Variable usada para almacenar el código de la opción seleccione
	 */
	private String codigoOpcionSeleccione;
	
	/**
	 * Variable usada para almacenar el código del tipo de área
	 */
	private String tipoArea;
	
	/**
	 * Código del tipo de transacción de Pedido
	 */
	private String tipoTransaccionPedido;
	
	/**
	 * Descripcion del estado de la peticion
	 */
	private String estadoPeticion;
	
	/**
	 * Fecha de la peticion
	 */
	private String fechaPeticion;
	
	/**
	 * Hora de la peticion
	 */
	private String horaPeticion;
	

	/**
	 * Número del ingreso
	 */
	private String numeroIngreso;
	
	//******ATRIBUTOS DE LA SECCIÓN GENERAL DE MATERIALES QX*******************
	/**
	 * Variable que almacena el ID de la cuenta del paciente
	 */
	private int idCuenta;
	
	/**
	 * Campo fecha de la sección de Materiales Qx.
	 */
	private String fecha;
	
	/**
	 * Campo hora de la sección de Materiales Qx.
	 */
	private String hora;
	
	/**
	 * Campo donde se almacena el código del centro de costo
	 */
	private int centroCosto;
	
	/**
	 * Campo donde se almacena el nombre del centro de costo
	 */
	private String nombreCentroCosto;
	
	/**
	 * Campo donde se almacena el código de la farmacia
	 */
	private int farmacia;
	
	/**
	 * CAmpo donde se almacena el código de la farmacia anterior
	 */
	private int farmaciaAnterior;
	
	/**
	 * Campo donde se almacena el nombre de la farmacia
	 */
	private String nombreFarmacia;
	
	/**
	 * Variable que verifica si existe farmacia
	 */
	private boolean existeFarmacia;
	
	/**
	 * Variable que indica si el Acto Qx. ya tiene un registro en Materiales Qx.
	 */
	private boolean existe;
	
	/**
	 * Objeto para cargar las cirugias del acto quirúrgico
	 */
	private HashMap cirugias = new HashMap();
	
	/**
	 * Variable que almacena el número de registros del mapa cirugias
	 */
	private int numCirugias;
	
	/**
	 * variable que contiene los codigos de las cirugias de la petición separados por comas
	 */
	private String listadoCirugias;
	
	/**
	 * Objeto para almacenar los articulos
	 */
	private HashMap articulos = new HashMap();
	
	/**
	 * Variable que almacena el número de registros del mapa cirugias
	 */
	private int numArticulos;
	
	/**
	 * Variable que me indica si el consumo tiene pedidos
	 */
	private boolean tienePedido;
	
	/**
	 * Variable que toma la posicion del articulo seleccionado
	 */
	private int posicionArticulo;
	
	/**
	 * Consecutivo del paquete que se desea eliminar
	 */
	private String consecutivoPaquete;
	
	//datos usado para la búsqueda (popUp) de cada artículo*******************
	/**
	 * Código - Nombre del artículo
	 */
	private String articulo; 
	
	/**
	 * Descripción de la concentracion del articulo
	 */
	private String concentracion;
	
	/**
	 * Descripciñon de la forma farmaceútica
	 */
	private String formaFarmaceutica;
	
	/**
	 * Descripción de la unidad de medida
	 */
	private String unidadMedida;
	
	/**
	 * Descripción de la naturaleza del artículo
	 */
	private String naturaleza;
	
	/**
	 * Listado de los artículos ya ingresados
	 */
	private String codigosArticulosInsertados;
	
	
	private String consecutivoCirugia;
	
	
	//**************************************************************************
	//*******ATRIBUTOS ESPECÍFICOS DE OPCIÓN PEDIDO QX************************
	
	/**
	 * Variable para almacenar la fecha de grabación del pedido
	 */
	private String fechaGrabacion;
	
	/**
	 * Variable para almacener la hora de grabación del pedido
	 */
	private String horaGrabacion;
	
	/**
	 * Variable para almacenar el indicador de prioridad del pedido
	 */
	private String urgente;
	
	/**
	 * Variable para almacenar el login de quien ingresó el pedido
	 */
	private String usuario;
	
	/**
	 * Variable para almacenar el número del pedido
	 */
	private int numeroPedido;
	
	/**
	 * Variable que almacena el número de petición asociado al pedido
	 */
	private int numeroPeticion;
	
	/**
	 * Colección de peticiones
	 */
	private Peticion[] peticiones;
	
	/**
	 * Número de peticiones de la colección
	 */
	private int numPeticiones;
	
	/**
	 * Listado consecutivos de paquetes insertados
	 */
	private String consecutivosPaquetesInsertados;
	
	/**
	 * Número de paquetes
	 */
	private int numPaquetes;
	
	/**
	 * Mapa que almacena los paquetes seleccionados
	 */
	private HashMap paquetes = new HashMap();
	
	/**
	 * Posición del paquete
	 */
	private int posPaquete;
	
	/**
	 * Método que indica si el pedido es terminado o pendiente
	 */
	private String terminado ;

	
	//*******ATRIBUTOS ESPECÍFICOS DE OPCIÓN INGRESO MATERIALES CONSUMO*********
	
	/**
	 * Objeto donde se almacena el listado de ordenes de cirugia
	 * de la cuenta 
	 */
	private HashMap ordenes = new HashMap();
	
	/**
	 * Variable que almacena el tamaño del Mapa
	 */
	private int numRegistros;
	
	/**
	 * Variable que almacena el numero de la solicitud a la cual
	 * se le ingresaran/modificarán los artículos
	 */
	private int orden;
	
	/**
	 * Variable que indica la forma como se ingresarán/modificarán
	 * los artículos al acto quirúrgico
	 */
	private boolean porActo;
	
	/**
	 * Concatenación de las órdenes que no tienen el pedido despachado
	 */
	private String ordenesSinPedidoDespachado;
	
	/**
	 * Variable que indica si el consumo está finalizado o no
	 */
	private String finalizado;
	
	/**
	 * Variable que verifica si ya se hizo la validacion de los artículos del servicio
	 */
	private boolean validacionArticulosServicio;
	
	
	/**
	 * Variable que indica si ya se hizo la confirmacion de la finalización del consumo
	 */
	private boolean validacionConfirmacionConsumoFinalizacion;
	
	/**
	 * Variable que indica si hay cantidades pendientes para generar pedido 
	 */
	private boolean existenCantidadesPendientes;
	
	/**
	 * Variable que indica si la solicitud tiene una hoja de anestesia ya finalizada para confirmar el consumo
	 */
	private boolean existeHojaAnesSinFinalizar;
	
	/**
	 * Variable que indica si se debe generar pedido x consumo
	 */
	private String generarPedidoXConsumo;
	
	/**
	 * Variable que verifica si una orden tiene pedidos solicitados
	 */
	private boolean tienePedidosSolicitados;
	
	
	//*********datos usados para la ordenación y paginación de registros *************
	
	/**
	 * campo índice por el cual se ordena el listado de registros
	 */
	private String indice;
	/**
	 * campo por el cual se ordenó la última vez el listado de registros
	 */
	private String ultimoIndice;
	
	/**
	 * Enlace siguiente en la paginación
	 */
	private String linkSiguiente;
	
	/**
	 * Número de página de la paginación
	 */
	private int offset;
	
	//*****ATRIBUTO USADO CUANDO LA FUNCIONALIDAD ES LLAMADA DESDE POPUP*******
	
	/**
	 * Indica si se deben mostrar los menus de tercer nivel en la funcionalidad
	 */
	private boolean conMenu;
	
	
	//****************************************************************************
	
	//*******ATRIBUTOS USADOS PARA LA SELECCION INICIAL DE UN ALMACEN************************
	/**
	 * Número de centros de costo válidos para transacción de pedidos
	 */
	private int numCentrosCosto;
	
	/**
	 * listado de almacenes válidos
	 */
	private HashMap listaAlmacenes = new HashMap();
	
	/**
	 * Número de registros del listado de almacenes válidos
	 */
	private int numAlmacenes;
	
	/**
	 * Listado de parejas clase-grupos válidas para 
	 * el ingreso de artículos en la petición
	 */
	private String parejasClaseGrupo;
	
	//***************************************************************************************
	
	//*******ATRIBUTOS USADOS PARA EL LLAMADO DEL PEDIDO QUIRURGICO DESDE LA FUNCIONALIDAD PROGRAMACION CX***********
	/**
	 * Indica si el llamado se realiza desde la funcionalidad de Programar CX por paciente, por peticion
	 * */
	private String indicadorFuncOut;
	
	//*****************************************************************************************************************
	
	//*****************************************************************************************
	//***** CAMBIOS JUSTIFICACION NO POS **************
	
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	private HashMap justificacionMap=new HashMap();
	
	/**
	 * Mapa medicamento pos
	 */
	private HashMap medicamentosPosMap=new HashMap();
	
	/**
	 * Mapa medicamento no pos
	 */
	private HashMap medicamentosNoPosMap=new HashMap();
	
	/**
	 * Mapa medicamento sustituto no pos
	 */
	private HashMap sustitutosNoPosMap=new HashMap();
	
	/**
	 * Mapa diagnosticos definitivos
	 */
	private HashMap diagnosticosDefinitivos=new HashMap();
	
	/**
	 * Mapa diagnosticos presuntivos
	 */
	private HashMap diagnosticosPresuntivos=new HashMap();
	
	
	/**
	 * numero de justificacion
	 */
	private int numjus=0;
	
	/**
	 * numero solicitud
	 */
	private int solicitud=0;
	
	
	/**
	 * mapa de alertas justificacion pendiente
	 */
	private HashMap justificacionNoPosMap=new HashMap();
	
	
	//********************************************************************************************************
	
	
	
	
	
	
	
	
	
	
	
	
	
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
		
		if(this.estado.equals("agregar"))
		{
			//se verifica que el artículo no se haya seleccionado antes
			for(int i=0;i<this.numArticulos;i++)
				if(articulo.equals(articulos.get("articulo_"+i)))
				{
					errores.add(
						"artículo ya elegido", 
						new ActionMessage("errors.existeCampo",
								articulo.split("-")[1]+" "+
								articulos.get("concentracion_"+i)+" "+
								articulos.get("formaFarmaceutica_"+i)
						)
					);
				}
		}
		else if(this.estado.equals("guardarPedido")||this.estado.equals("guardarMateriales"))
		{
			String labelFecha = "del consumo";
			String labelArticulo = "inserción del consumo de materiales";
			if(this.estado.equals("guardarPedido"))
			{
				labelFecha = "del pedido";
				labelArticulo = "inserción del pedido";
			}
			//******validación de fecha***************
			if(this.fecha.equals(""))
				errores.add("la fecha es requerida",new ActionMessage("errors.required","La fecha"));
			else if(!UtilidadFecha.validarFecha(this.fecha))
				errores.add("formato fecha inválido", new ActionMessage("errors.formatoFechaInvalido",this.fecha));
			else if((UtilidadFecha.conversionFormatoFechaABD(this.fecha)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
				errores.add("fecha posterior a fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", labelFecha, "del sistema"));
			else if((UtilidadFecha.conversionFormatoFechaABD(this.fecha)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))==0)
				if(UtilidadFecha.validacionHora(this.hora).puedoSeguir&&this.hora.compareTo(UtilidadFecha.getHoraActual())>0)
					errores.add("fecha/hora posterior a fecha/hora actual", new ActionMessage("errors.fechaHoraPosteriorIgualActual", labelFecha, "del sistema"));
			
			//****validacion de la hora ****************
			if(this.hora.equals(""))
				errores.add("la hora es requerida",new ActionMessage("errors.required","La hora"));
			else if(!UtilidadFecha.validacionHora(this.hora).puedoSeguir)
				errores.add("formato hora inválido", new ActionMessage("errors.formatoHoraInvalido",this.hora));
			
			//***validación de los artículos**********
			if(this.numArticulos<=0)
				errores.add("pedido sin artículos", new ActionMessage("errors.minimoCampos","1 artículo",labelArticulo));
			
			//se verifica que aunque sea un artículo tenga una cantidad > 0
			int articulosConCantidad = 0, articulosConConsumoAnt = 0;
			String cantidad = "";
			for(int i=0;i<numArticulos;i++)
			{
				if(Utilidades.convertirAEntero(this.getArticulos("totalConsAnt_"+i)+"", true)>0)
					articulosConConsumoAnt ++;
				
				//Validacion para Pedido Qx
				if(estado.equals("guardarPedido"))
				{
					cantidad = this.getArticulos("cantidad_"+i)+"";
					if(!cantidad.equals("")&&!cantidad.equals("0"))
					{
						//Se verifica que se haya ingresado una cantidad entera
						try
						{
							Integer.parseInt(cantidad);
						}
						catch(Exception e)
						{
							errores.add("",new ActionMessage("errors.integer","La cantidad del artículo "+this.getArticulos("articulo_"+i)));
						}
						
						articulosConCantidad ++;
					}
				}
				//Validacion para el consumo por Acto
				else if(this.porActo)
				{
					cantidad = this.getArticulos("total_"+i)+"";
					if(!cantidad.equals("")&&!cantidad.equals("0"))
					{
						//Se verifica que se haya ingresado una cantidad entera
						try
						{
							Integer.parseInt(cantidad);
						}
						catch(Exception e)
						{
							errores.add("",new ActionMessage("errors.integer","El consumo actual del artículo "+this.getArticulos("articulo_"+i)));
						}
						
						articulosConCantidad ++;
					}
				}
				//Validacion para el consumo por Cirugía
				else
				{
					int cont =0;
					for(int j=0;j<this.numCirugias;j++)
					{
						cantidad = this.getArticulos("consumoActual"+this.getCirugias("consecutivo_"+j)+"_"+i)+"";
						if(!cantidad.equals("")&&!cantidad.equals("0"))
						{
							//Se verifica que se haya ingresado una cantidad entera
							try
							{
								Integer.parseInt(cantidad);
							}
							catch(Exception e)
							{
								errores.add("",new ActionMessage("errors.integer","El consumo actual del artículo "+this.getArticulos("articulo_"+i)+" para el Serv. "+this.getCirugias("consecutivo_"+j)));
							}
							
							cont ++;
						}
					}
					if(cont>0)
						articulosConCantidad++;
				}
				
			}
			if(articulosConCantidad<=0&&articulosConConsumoAnt<=0)
				errores.add("pedido sin artículos", new ActionMessage("errors.minimoCampos","1 artículo",labelArticulo));
			
			//***validacion de terminado******************
			if(this.terminado.equals(""))
				errores.add("el campo terminado/pendiente es requerido",new ActionMessage("errors.required","El campo Terminado/Pendiente"));
			//Validacion que solo aplica para consumo
			else if(this.estado.equals("guardarMateriales"))
			{
				if(UtilidadTexto.getBoolean(this.terminado)&&UtilidadTexto.getBoolean(this.finalizado))
				{
					/**
					 * EL CONSUMO ESTÁ SIENDO FINALIZADO
					 */
					;
				}
			}
			
			if(this.estado.equals("guardarPedido"))
			{
				//***validación de farmacia*****************
				if(this.farmacia==ConstantesBD.codigoNuncaValido)
					errores.add("la farmacia es requerida",new ActionMessage("errors.required","La farmacia"));
			}
				
		}
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset(boolean excepto)
	{		
		//***** formato justificacion no pos
		this.justificacionMap=new HashMap();
		this.medicamentosNoPosMap=new HashMap();
		this.medicamentosPosMap=new HashMap();
		this.sustitutosNoPosMap=new HashMap();
		this.numjus=0;
		this.diagnosticosDefinitivos=new HashMap();
		this.diagnosticosPresuntivos=new HashMap();
		this.justificacionNoPosMap=new HashMap();
		//*****
		
		
		
		this.estado = "";
		this.index = "0";
		this.institucion = "0";
		this.centroAtencion = "0";
		this.codigoOpcionTodos = "0";
		this.codigoOpcionSeleccione = "-1";
		this.tipoArea = "";
		this.articulo = "";
		this.concentracion = "";
		this.formaFarmaceutica = "";
		this.unidadMedida = "";
		this.naturaleza = "";
		this.tipoTransaccionPedido = "";
		this.codigosArticulosInsertados = "";
		this.consecutivosPaquetesInsertados = "";
		this.numPaquetes = 0;
		this.paquetes = new HashMap();
		this.paquetes.put("numRegistros","0");
		this.posPaquete = 0;
		this.terminado = "";
		this.consecutivoCirugia="";
		
		
		//atributos generales materiales Qx
		this.fecha = "";
		this.hora = "";
		this.centroCosto = -1;
		this.nombreCentroCosto = "";
		this.farmacia = ConstantesBD.codigoNuncaValido;
		this.farmaciaAnterior = ConstantesBD.codigoNuncaValido;
		this.nombreFarmacia = "";
		this.existeFarmacia = false;
		this.existe = false;
		this.cirugias = new HashMap();
		this.numCirugias = 0;
		this.listadoCirugias = "";
		this.idCuenta = 0;
		
		//atributos opcion ingresar materiales consumo
		this.ordenes = new HashMap();
		this.numRegistros = 0;
		this.orden = 0;
		this.porActo = true;
		this.articulos = new HashMap();
		this.numArticulos = 0;
		this.ordenesSinPedidoDespachado = "";
		this.finalizado = "";
		this.validacionArticulosServicio = false;
		this.validacionConfirmacionConsumoFinalizacion = false;
		this.existenCantidadesPendientes = false;
		this.existeHojaAnesSinFinalizar= false;
		this.generarPedidoXConsumo = ConstantesBD.acronimoNo;
		this.tienePedidosSolicitados = false;
		
		//atributos opción pedidos Qx.
		this.fechaGrabacion = "";
		this.horaGrabacion = "";
		this.urgente = "false";
		this.usuario = "";		
		this.peticiones = new Peticion[0];
		this.numPeticiones = 0;		
		
		if(!excepto)
		{
			this.numeroPeticion = 0;
			this.fechaPeticion = "";
			this.horaPeticion = "";
			this.estadoPeticion = "";
			
			//atributos para las funcionalidades externas que realizan llamados
			this.indicadorFuncOut = "";
		}
		
		this.numeroIngreso = "";
		
		//atributos de ordenaciòn de registros
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.offset = 0;
		
		this.conMenu = true;
		
		//atributos para la selección de almacen
		this.numCentrosCosto = 0;
		this.listaAlmacenes = new HashMap();
		this.numAlmacenes = 0;
		this.parejasClaseGrupo = "";
		
		this.tienePedido = false;
		this.posicionArticulo = ConstantesBD.codigoNuncaValido;
		this.consecutivoPaquete = "";
		
		
	}
	/**
	 * @return the generarPedidoXConsumo
	 */
	public String getGenerarPedidoXConsumo() {
		return generarPedidoXConsumo;
	}

	/**
	 * @param generarPedidoXConsumo the generarPedidoXConsumo to set
	 */
	public void setGenerarPedidoXConsumo(String generarPedidoXConsumo) {
		this.generarPedidoXConsumo = generarPedidoXConsumo;
	}

	/**
	 * @return the existeFarmacia
	 */
	public boolean isExisteFarmacia() {
		return existeFarmacia;
	}

	/**
	 * @param existeFarmacia the existeFarmacia to set
	 */
	public void setExisteFarmacia(boolean existeFarmacia) {
		this.existeFarmacia = existeFarmacia;
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
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the ordenes.
	 */
	public HashMap getOrdenes() {
		return ordenes;
	}
	/**
	 * @param ordenes The ordenes to set.
	 */
	public void setOrdenes(HashMap ordenes) {
		this.ordenes = ordenes;
	}
	/**
	 * @return Retorna un elemento del mapa ordenes.
	 */
	public Object getOrdenes(String key) {
		return ordenes.get(key);
	}
	/**
	 * @param asigna un elemnto al mapa ordenes 
	 */
	public void setOrdenes(String key,Object obj) {
		this.ordenes.put(key,obj);
	}
	/**
	 * @return Returns the orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	/**
	 * @return Returns the porActo.
	 */
	public boolean isPorActo() {
		return porActo;
	}
	/**
	 * @param porActo The porActo to set.
	 */
	public void setPorActo(boolean porActo) {
		this.porActo = porActo;
	}
	/**
	 * @return Returns the centroCosto.
	 */
	public int getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @param centroCosto The centroCosto to set.
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}
	/**
	 * @return Returns the farmacia.
	 */
	public int getFarmacia() {
		return farmacia;
	}
	/**
	 * @param farmacia The farmacia to set.
	 */
	public void setFarmacia(int farmacia) {
		this.farmacia = farmacia;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the hora.
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora The hora to set.
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	/**
	 * @return Returns the existe.
	 */
	public boolean isExiste() {
		return existe;
	}
	/**
	 * @param existe The existe to set.
	 */
	public void setExiste(boolean existe) {
		this.existe = existe;
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
	 * @return Retorna un elemento del mapa cirugías.
	 */
	public Object getCirugias(String key) {
		return cirugias.get(key);
	}
	/**
	 * @param asigna un elemento al mapa cirugias.
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
	 * @return Returns the articulos.
	 */
	public HashMap getArticulos() {
		return articulos;
	}
	/**
	 * @param articulos The articulos to set.
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	/**
	 * @return Retorna un elemento del mapa articulos.
	 */
	public Object getArticulos(String key) {
		return articulos.get(key);
	}
	/**
	 * @param asigna un elemento al mapa articulos.
	 */
	public void setArticulos(String key,Object obj) {
		this.articulos.put(key,obj);
	}
	/**
	 * @return Returns the numArticulos.
	 */
	public int getNumArticulos() {
		return numArticulos;
	}
	/**
	 * @param numArticulos The numArticulos to set.
	 */
	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
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
	 * @return Returns the urgente.
	 */
	public String getUrgente() {
		return urgente;
	}
	/**
	 * @param urgente The urgente to set.
	 */
	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
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
	 * @return Returns the numeroPeticion.
	 */
	public int getNumeroPeticion() {
		return numeroPeticion;
	}
	/**
	 * @param numeroPeticion The numeroPeticion to set.
	 */
	public void setNumeroPeticion(int numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
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
	 * @return Returns the codigoOpcionSeleccione.
	 */
	public String getCodigoOpcionSeleccione() {
		return codigoOpcionSeleccione;
	}
	/**
	 * @param codigoOpcionSeleccione The codigoOpcionSeleccione to set.
	 */
	public void setCodigoOpcionSeleccione(String codigoOpcionSeleccione) {
		this.codigoOpcionSeleccione = codigoOpcionSeleccione;
	}
	/**
	 * @return Returns the codigoOpcionTodos.
	 */
	public String getCodigoOpcionTodos() {
		return codigoOpcionTodos;
	}
	/**
	 * @param codigoOpcionTodos The codigoOpcionTodos to set.
	 */
	public void setCodigoOpcionTodos(String codigoOpcionTodos) {
		this.codigoOpcionTodos = codigoOpcionTodos;
	}
	/**
	 * @return Returns the tipoArea.
	 */
	public String getTipoArea() {
		return tipoArea;
	}
	/**
	 * @param tipoArea The tipoArea to set.
	 */
	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}
	/**
	 * @return Returns the articulo.
	 */
	public String getArticulo() {
		return articulo;
	}
	/**
	 * @param articulo The articulo to set.
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	/**
	 * @return Returns the concentracion.
	 */
	public String getConcentracion() {
		return concentracion;
	}
	/**
	 * @param concentracion The concentracion to set.
	 */
	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}
	/**
	 * @return Returns the formaFarmaceutica.
	 */
	public String getFormaFarmaceutica() {
		return formaFarmaceutica;
	}
	/**
	 * @param formaFarmaceutica The formaFarmaceutica to set.
	 */
	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}
	/**
	 * @return Returns the naturaleza.
	 */
	public String getNaturaleza() {
		return naturaleza;
	}
	/**
	 * @param naturaleza The naturaleza to set.
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}
	/**
	 * @return Returns the unidadMedida.
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}
	/**
	 * @param unidadMedida The unidadMedida to set.
	 */
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	/**
	 * @return Returns the fechaGrabacion.
	 */
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}
	/**
	 * @param fechaGrabacion The fechaGrabacion to set.
	 */
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}
	/**
	 * @return Returns the horaGrabacion.
	 */
	public String getHoraGrabacion() {
		return horaGrabacion;
	}
	/**
	 * @param horaGrabacion The horaGrabacion to set.
	 */
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the numPeticiones.
	 */
	public int getNumPeticiones() {
		return numPeticiones;
	}
	/**
	 * @param numPeticiones The numPeticiones to set.
	 */
	public void setNumPeticiones(int numPeticiones) {
		this.numPeticiones = numPeticiones;
	}
	/**
	 * @return Returns the peticiones.
	 */
	public Peticion[] getPeticiones() {
		return peticiones;
	}
	/**
	 * @param peticiones The peticiones to set.
	 */
	public void setPeticiones(Peticion[] peticiones) {
		this.peticiones = peticiones;
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the ordenesSinPedidoDespachado.
	 */
	public String getOrdenesSinPedidoDespachado() {
		return ordenesSinPedidoDespachado;
	}
	/**
	 * @param ordenesSinPedidoDespachado The ordenesSinPedidoDespachado to set.
	 */
	public void setOrdenesSinPedidoDespachado(String ordenesSinPedidoDespachado) {
		this.ordenesSinPedidoDespachado = ordenesSinPedidoDespachado;
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
	 * @return Returns the conMenu.
	 */
	public boolean isConMenu() {
		return conMenu;
	}
	/**
	 * @param conMenu The conMenu to set.
	 */
	public void setConMenu(boolean conMenu) {
		this.conMenu = conMenu;
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

	public String getListadoCirugias() {
		return listadoCirugias;
	}

	public void setListadoCirugias(String listadoCirugias) {
		this.listadoCirugias = listadoCirugias;
	}

	/**
	 * @return the tienePedido
	 */
	public boolean isTienePedido() {
		return tienePedido;
	}

	/**
	 * @param tienePedido the tienePedido to set
	 */
	public void setTienePedido(boolean tienePedido) {
		this.tienePedido = tienePedido;
	}

	public String getEstadoPeticion() {
		return estadoPeticion;
	}

	public void setEstadoPeticion(String estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}

	public String getFechaPeticion() {
		return fechaPeticion;
	}

	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}

	public String getHoraPeticion() {
		return horaPeticion;
	}

	public void setHoraPeticion(String horaPeticion) {
		this.horaPeticion = horaPeticion;
	}

	/**
	 * @return the consecutivosPaquetesInsertados
	 */
	public String getConsecutivosPaquetesInsertados() {
		return consecutivosPaquetesInsertados;
	}

	/**
	 * @param consecutivosPaquetesInsertados the consecutivosPaquetesInsertados to set
	 */
	public void setConsecutivosPaquetesInsertados(
			String consecutivosPaquetesInsertados) {
		this.consecutivosPaquetesInsertados = consecutivosPaquetesInsertados;
	}

	/**
	 * @return the numPaquetes
	 */
	public int getNumPaquetes() {
		return numPaquetes;
	}

	/**
	 * @param numPaquetes the numPaquetes to set
	 */
	public void setNumPaquetes(int numPaquetes) {
		this.numPaquetes = numPaquetes;
	}

	/**
	 * @return the paquetes
	 */
	public HashMap getPaquetes() {
		return paquetes;
	}

	/**
	 * @param paquetes the paquetes to set
	 */
	public void setPaquetes(HashMap paquetes) {
		this.paquetes = paquetes;
	}
	
	/**
	 * @return the paquetes
	 */
	public Object getPaquetes(String key) {
		return paquetes.get(key);
	}

	/**
	 * @param paquetes the paquetes to set
	 */
	public void setPaquetes(String key,Object obj) {
		this.paquetes.put(key,obj);
	}

	/**
	 * @return the terminado
	 */
	public String getTerminado() {
		return terminado;
	}

	/**
	 * @param terminado the terminado to set
	 */
	public void setTerminado(String terminado) {
		this.terminado = terminado;
	}


	/**
	 * @return the numeroIngreso
	 */
	public String getNumeroIngreso() {
		return numeroIngreso;
	}

	/**
	 * @param numeroIngreso the numeroIngreso to set
	 */
	public void setNumeroIngreso(String numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getPeticion() {
		return peticion;
	}

	public void setPeticion(String peticion) {
		this.peticion = peticion;
	}

	public String getConsecutivoCirugia() {
		return consecutivoCirugia;
	}

	public void setConsecutivoCirugia(String consecutivoCirugia) {
		this.consecutivoCirugia = consecutivoCirugia;
	}

	/**
	 * @return the posPaquete
	 */
	public int getPosPaquete() {
		return posPaquete;
	}

	/**
	 * @param posPaquete the posPaquete to set
	 */
	public void setPosPaquete(int posPaquete) {
		this.posPaquete = posPaquete;
	}

	/**
	 * @return the finalizado
	 */
	public String getFinalizado() {
		return finalizado;
	}

	/**
	 * @param finalizado the finalizado to set
	 */
	public void setFinalizado(String finalizado) {
		this.finalizado = finalizado;
	}

	/**
	 * @return the validacionArticulosServicio
	 */
	public boolean isValidacionArticulosServicio() {
		return validacionArticulosServicio;
	}

	/**
	 * @param validacionArticulosServicio the validacionArticulosServicio to set
	 */
	public void setValidacionArticulosServicio(boolean validacionArticulosServicio) {
		this.validacionArticulosServicio = validacionArticulosServicio;
	}

	/**
	 * @return the validacionConfirmacionConsumoFinalizacion
	 */
	public boolean isValidacionConfirmacionConsumoFinalizacion() {
		return validacionConfirmacionConsumoFinalizacion;
	}

	/**
	 * @param validacionConfirmacionConsumoFinalizacion the validacionConfirmacionConsumoFinalizacion to set
	 */
	public void setValidacionConfirmacionConsumoFinalizacion(
			boolean validacionConfirmacionConsumoFinalizacion) {
		this.validacionConfirmacionConsumoFinalizacion = validacionConfirmacionConsumoFinalizacion;
	}



	/**
	 * @return the existenCantidadesPendientes
	 */
	public boolean isExistenCantidadesPendientes() {
		return existenCantidadesPendientes;
	}

	/**
	 * @param existenCantidadesPendientes the existenCantidadesPendientes to set
	 */
	public void setExistenCantidadesPendientes(boolean existenCantidadesPendientes) {
		this.existenCantidadesPendientes = existenCantidadesPendientes;
	}

	/**
	 * @return the existeHojaAnesSinFinalizar
	 */
	public boolean isExisteHojaAnesSinFinalizar() {
		return existeHojaAnesSinFinalizar;
	}

	/**
	 * @param existeHojaAnesSinFinalizar the existeHojaAnesSinFinalizar to set
	 */
	public void setExisteHojaAnesSinFinalizar(boolean existeHojaAnesSinFinalizar) {
		this.existeHojaAnesSinFinalizar = existeHojaAnesSinFinalizar;
	}

	/**
	 * @return the indicadorFuncOut
	 */
	public String getIndicadorFuncOut() {
		return indicadorFuncOut;
	}

	/**
	 * @param indicadorFuncOut the indicadorFuncOut to set
	 */
	public void setIndicadorFuncOut(String indicadorFuncOut) {
		this.indicadorFuncOut = indicadorFuncOut;
	}

	/**
	 * @return the diagnosticosDefinitivos
	 */
	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * @return the diagnosticosPresuntivos
	 */
	public HashMap getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
	 */
	public void setDiagnosticosPresuntivos(HashMap diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * @return the justificacionMap
	 */
	public HashMap getJustificacionMap() {
		return justificacionMap;
	}

	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionMap(HashMap justificacionMap) {
		this.justificacionMap = justificacionMap;
	}

	/**
	 * @return the medicamentosNoPosMap
	 */
	public HashMap getMedicamentosNoPosMap() {
		return medicamentosNoPosMap;
	}

	/**
	 * @param medicamentosNoPosMap the medicamentosNoPosMap to set
	 */
	public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
		this.medicamentosNoPosMap = medicamentosNoPosMap;
	}

	/**
	 * @return the medicamentosPosMap
	 */
	public HashMap getMedicamentosPosMap() {
		return medicamentosPosMap;
	}

	/**
	 * @param medicamentosPosMap the medicamentosPosMap to set
	 */
	public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
		this.medicamentosPosMap = medicamentosPosMap;
	}

	/**
	 * @return the numjus
	 */
	public int getNumjus() {
		return numjus;
	}

	/**
	 * @param numjus the numjus to set
	 */
	public void setNumjus(int numjus) {
		this.numjus = numjus;
	}

	/**
	 * @return the sustitutosNoPosMap
	 */
	public HashMap getSustitutosNoPosMap() {
		return sustitutosNoPosMap;
	}

	/**
	 * @param sustitutosNoPosMap the sustitutosNoPosMap to set
	 */
	public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
		this.sustitutosNoPosMap = sustitutosNoPosMap;
	}

	/**
	 * @return the solicitud
	 */
	public int getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the justificacionNoPosMap
	 */
	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}

	/**
	 * @param justificacionNoPosMap the justificacionNoPosMap to set
	 */
	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}
	
	/**
	 * 
	 * @param o
	 * @param key
	 */
	public void setJustificacionNoPosMap(String key,Object obj)
	{
		this.justificacionNoPosMap.put(key,obj);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionNoPosMap(String key){
		return this.justificacionNoPosMap.get(key);
	}

	/**
	 * @return the farmaciaAnterior
	 */
	public int getFarmaciaAnterior() {
		return farmaciaAnterior;
	}

	/**
	 * @param farmaciaAnterior the farmaciaAnterior to set
	 */
	public void setFarmaciaAnterior(int farmaciaAnterior) {
		this.farmaciaAnterior = farmaciaAnterior;
	}

	/**
	 * @return the posicionArticulo
	 */
	public int getPosicionArticulo() {
		return posicionArticulo;
	}

	/**
	 * @param posicionArticulo the posicionArticulo to set
	 */
	public void setPosicionArticulo(int posicionArticulo) {
		this.posicionArticulo = posicionArticulo;
	}

	/**
	 * @return the consecutivoPaquete
	 */
	public String getConsecutivoPaquete() {
		return consecutivoPaquete;
	}

	/**
	 * @param consecutivoPaquete the consecutivoPaquete to set
	 */
	public void setConsecutivoPaquete(String consecutivoPaquete) {
		this.consecutivoPaquete = consecutivoPaquete;
	}

	/**
	 * @return the tienePedidosSolicitados
	 */
	public boolean isTienePedidosSolicitados() {
		return tienePedidosSolicitados;
	}

	/**
	 * @param tienePedidosSolicitados the tienePedidosSolicitados to set
	 */
	public void setTienePedidosSolicitados(boolean tienePedidosSolicitados) {
		this.tienePedidosSolicitados = tienePedidosSolicitados;
	}

	
	
	
	

	
}
