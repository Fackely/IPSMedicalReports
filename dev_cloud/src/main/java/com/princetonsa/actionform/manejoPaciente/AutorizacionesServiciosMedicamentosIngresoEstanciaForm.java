package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoServicioAutoHelper;

public class AutorizacionesServiciosMedicamentosIngresoEstanciaForm extends ValidatorForm 
{
	
	/*--------------------------*/
	/* Propias de la Forma
	/*--------------------------*/
	/** * Serial */
	private static final long serialVersionUID = 1L;
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	/** * Posicion de las autorizaciones */
	private int posArray;
	/** * Parametros para ordenar */
	private String patronOrdenar;
	/** * Parametros para ordenar */
	private String esDescendente;
	/** Completa la ruta hacia la cual se debe direccionar la aplicaci&oacute;n. **/
	private String path;
	private String pathAutorizar;
	/** Atributo para el manejo de los tipos de autorizacion  **/
	private String tipoAutorizacion;
	/** Atributo para el manejo de los pacientes seleccionados**/
	private DTOPacienteCapitado dtoPacienteSeleccionado;
	/** Dto para el manejo de la informacion de la EntidadSubcontratada**/
	private DtoEntidadSubcontratada dtoEntidadSubcontratada;
	/** fecha vencimiento de la autorizacion calculada**/
	private Date fechaVencimiento;
	/** observaciones generales de la autorizacion**/
	private String observacionesGenerales;
	/** numero de filas existentes en el hashmap pero en el caso de servicios **/
	private int numeroFilasMapaCasoServicios;
	/**	HashMap de servicios empleado en la busqueda generica de servicios **/
	private HashMap<String, Object> servicios;
	/**  Se guarda la informaci&oacute;n necesaria para guardar la justificacion de cada servicio No POS *  */
    private HashMap justificacionesServicios;
    /** Atributo empleado en la busqueda avanzada de servicios**/
    private String manejoProgramacionSalas;
    /**  codigos de los servicios insetados **/
	private String codigosServiciosInsertados;
	/** numero de filas del mapa de articulos **/
	private int numeroFilasMapa;
	/** HashMap	de articulos empleado en la busqueda generica de articulos */
	private HashMap articulos;
	/** Atributo guarda el codigo del articulo selecciondo **/
	private int codigoArticuloSeleccionado;
	
	private String criterioBusqueda;
	private String criterioBusquedaInsumo;
	
	private boolean convenioValido;

	private DtoServicioAutoHelper dtoServicioHelper;
	private List<DtoServicioAutoHelper> listaServiciosAutoHelper;
	private int posListaServicios;
	private String naturalezaArticuloConsultada;
	private String acronimoUnidadMedidaArticulo;
	
	private String nombreArchivoGenerado;
	
	private boolean formatoImpresionDefinido;
	
	private Character convenioManejaPresupuesto;
	
	
	
	/*--------------------------*/
	/*Listas y opciones a mostrar
	/*--------------------------*/
	/** * Lista de convenios Capitados */
	private ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia;
	private int postListAutorizaciones;
	/**  Atributo que contiene los servicios de la autorización **/	
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	/**  Atributo que contiene los artículos de la autorización **/
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	/** Este atributo se usa para determinar cuando se generó una 
	 * autorización de solicitudes y mostrar el respectivo botón **/
	private boolean mostrarImprimirAutorizacion;
	/** lista que contiene los nombres de los reportes de las autorzaciones **/
	private ArrayList<String> listaNombresReportes;
	
	
	
	
	/*--------------------------*/
	/* Flujos
	/*--------------------------*/
	/** * Indica si se debe mostra para ingresar una nueva autorización */
	private boolean nuevaAutorizacion;
	/** * Indica si se puede extender N días la Autorización */
	private boolean extenderAutorizacion;
	/** * Indica la Autorización está vigente o no*/
	private boolean autorizacionVigente;
	/** * Indica si debe mostrar el popup mostrarPopUpTipoOrden */
	private boolean mostrarPopUpTipoOrden;
	/** Indica que se debe retornar a la jsp de articulos**/
	private boolean retornarDetalleArticulos;
	/** Indica que se debe retornar a la jsp de servicios**/
	private boolean retornarDetalleServicios;
	/** Indica si el articulo seleccionado es o no un medicamento**/
	private String esMedicamentoString;
	/** Indica si el articulo fue o no eliminado **/
	private String fueEliminadoArticulo;
	/** Indica que se debe validar cantidad como campo requerido para los articulos**/
	private int validarCantidadArticulo;
	
	private DtoArticulosAutorizaciones dtoArticulosHelper;
	
	/**
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author  Diana Carolina G
	 * 
	 */
	/*public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		ActionErrors errores=null;
		errores=new ActionErrors();
		
	if(estado.equals("autorizarServMedIngresoEstancia")){
		
		if((this.fechaVencimiento==null) || (this.fechaVencimiento.toString().equals("")) ){
			errores.add("La Fecha de Vencimiento es requerida",
					new ActionMessage("errors.required", "El campo Fecha Vencimiento"));
			}
		/*
		String fechaVencimientoFormateada = UtilidadFecha.conversionFormatoFechaAAp(
				this.fechaVencimiento);	
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		String fechaValidar = fechaActual.toString();
		
		
		if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaVencimientoFormateada, fechaValidar)){
			errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
							 "errors.fechaAnteriorIgualAOtraDeReferencia"," Vencimiento "+fechaVencimientoFormateada," Actual "+fechaValidar));
		}
		
		String infoCantidadServicio="";
		int valorCantidadServicio=0;
		String infoFinalidad = "";
		
		for(DtoServicioAutoHelper listaServici : this.listaServiciosAutoHelper)
		{
			infoCantidadServicio = listaServici.getCantidad();
			if(UtilidadTexto.isEmpty(infoCantidadServicio)){
				errores.add("La Fecha de Vencimiento es requerida",
						new ActionMessage("errors.required", "La cantidad del servicio con c&oacute;digo "+listaServici.getCodigoAxiomaJS()));
			}
			
			valorCantidadServicio= Utilidades.convertirAEntero(listaServici.getCantidad());
			if(valorCantidadServicio<1){
				errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del servicio con c&oacute;digo  "+listaServici.getCodigoAxiomaJS(),"0"));
			}
			
			infoFinalidad=listaServici.getFinalidad();
			if(UtilidadTexto.isEmpty(infoFinalidad)){
				errores.add("La Finalidad es requerida",
						new ActionMessage("errors.required", "La finalidad del servicio con c&oacute;digo "+listaServici.getCodigoAxiomaJS()));
			}
		}
		
		//**************************Validaciones Articulos
		

		int cantidad=0, duracionTratamiento=0; //frecuencia=0 
		String frecuencia="";
		//se valida la seccion de medicamentos
		
		if((!articulos.containsKey("numRegistros") || (articulos.containsKey("numRegistros")) && articulos.get("numRegistros").equals("0")))
		{
			errores.add("", new ActionMessage("errors.notEspecific","Es Requerido por lo menos Uno de los Siguientes Campos: Medicamento(s), Insumo(s) u Otros Medicamentos/Insumos "));
		}
		else if (articulos.containsKey("numRegistros"))
		{
			int numRegistros= Utilidades.convertirAEntero(this.getArticulos("numRegistros").toString());
			boolean centinelaEntroError=false;
			int contadorRegistrosNoEliminados=0;
			if(numRegistros>0)					
			{
				//primero validamos los MEDICAMENTOS
				for(int w=0; w<numRegistros; w++)
				{
					if(UtilidadTexto.getBoolean(this.getArticulos("medicamento_"+w).toString()))
					{	
						if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+w).toString()))
						{	
							
							if(!UtilidadCadena.noEsVacio(this.getArticulos("dosis_"+w).toString()))
							{
								errores.add("", new ActionMessage("errors.required","La dosis del art&iacute;culo "+this.getArticulos("articulo_"+w)));
							}
							if(!UtilidadCadena.noEsVacio(this.getArticulos("unidosis_"+w).toString()))
							{
								errores.add("", new ActionMessage("errors.required","La Unidosis del art&iacute;culo "+this.getArticulos("articulo_"+w)));
							}
							try	{
								frecuencia = this.getArticulos("frecuencia_"+w).toString();
								
							}catch (Exception e) 
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La frecuencia del art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
									centinelaEntroError=true;
								}
							if(!centinelaEntroError)
							{
								if(Utilidades.convertirAEntero(frecuencia)<1)
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La frecuencia del art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
								}
							}
							if(!UtilidadCadena.noEsVacio(this.getArticulos("tipofrecuencia_"+w).toString()))
							{
								errores.add("", new ActionMessage("errors.required","El tipo de frecuencia [horas-minutos] del art&iacute;culo "+this.getArticulos("articulo_"+w)));
							}
							if(!UtilidadCadena.noEsVacio(this.getArticulos("via_"+w).toString()))
							{
								errores.add("", new ActionMessage("errors.required","La v&iacute;a del art&iacute;culo "+this.getArticulos("articulo_"+w)));
							}
							if(!UtilidadCadena.noEsVacio(this.getArticulos("duraciontratamiento_"+w).toString()))
							{
								errores.add("", new ActionMessage("errors.required","Los D&iacute;as de Tratamiento del art&iacute;culo "+this.getArticulos("articulo_"+w)));
							}
							centinelaEntroError=false;
							try{
								cantidad = Utilidades.convertirAEntero(this.getArticulos("cantidad_"+w).toString());
								
							}catch (Exception e){
								errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
								centinelaEntroError=true;
							}
							if(!centinelaEntroError)
							{
								if(cantidad<1)
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
								}
							}
							centinelaEntroError=false;
							
							if(!this.getArticulos("duraciontratamiento_"+w).toString().trim().equals(""))
							{	
								try	{
									duracionTratamiento = Utilidades.convertirAEntero(this.getArticulos("duraciontratamiento_"+w).toString());
									
								}catch (Exception e) {
									errores.add("", new ActionMessage("errors.integerMayorQue", "La Duraci&oacute;n del tratamiento para el art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
									centinelaEntroError=true;
								}
								if(!centinelaEntroError)
								{
									if(duracionTratamiento<1)
									{
										errores.add("", new ActionMessage("errors.integerMayorQue", "La Duraci&oacute;n del tratamiento para el art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
									}
								}
							}
							contadorRegistrosNoEliminados++;
						}
					}
				}
				//luego validamos los insumos
				for(int w=0; w<numRegistros; w++)
				{
					if(!UtilidadTexto.getBoolean(this.getArticulos("medicamento_"+w).toString()))
					{	
						if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+w).toString()))
						{	
							try{
								cantidad = Utilidades.convertirAEntero(this.getArticulos("cantidad_"+w).toString());
								
							}catch (Exception e) {
								errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
								centinelaEntroError=true;
							}
							if(!centinelaEntroError)
							{
								if(cantidad<1)
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del art&iacute;culo "+this.getArticulos("articulo_"+w),"0"));
								}
							}
							contadorRegistrosNoEliminados++;
						}
					}	
				}
				if(contadorRegistrosNoEliminados<1)
				{
					errores.add("", new ActionMessage("errors.required","El/los medicamento(s) - insumo(s)"));
				}
			}
		}		
		 
		//**************************Validaciones Articulos
	
	} 
	  return null; 
		
 }  */
	
	/*-------------------------------------------------------*/
	/* RESETS
	/*-------------------------------------------------------*/
	
	/** * Reset de la forma  */
	public void reset()
	{
		this.patronOrdenar 						= "";
		this.esDescendente						= "";
		this.listaAutorizacionesIngresoEstancia = new ArrayList<DTOAdministracionAutorizacion>();
		this.nuevaAutorizacion					= false;
		this.extenderAutorizacion				= false;
		this.autorizacionVigente				= false;
		this.mostrarPopUpTipoOrden					= false;
		this.path								= "";
		this.pathAutorizar						= "";
		this.tipoAutorizacion					= "";
		this.dtoPacienteSeleccionado			= new DTOPacienteCapitado();
		this.dtoEntidadSubcontratada			= new DtoEntidadSubcontratada();
		this.fechaVencimiento					= null;
		this.observacionesGenerales				= "";
		this.numeroFilasMapaCasoServicios		= 0;
		this.justificacionesServicios=new HashMap();
		this.servicios=new HashMap<String, Object>();
		this.servicios.put("numRegistros","0");
		this.numeroFilasMapaCasoServicios		= 0;
		this.manejoProgramacionSalas 			= "";
		this.codigosServiciosInsertados			= "";
		this.dtoServicioHelper					= new DtoServicioAutoHelper();
		this.listaServiciosAutoHelper			= new ArrayList<DtoServicioAutoHelper>();
		this.postListAutorizaciones				= ConstantesBD.codigoNuncaValido;
		this.posListaServicios					= ConstantesBD.codigoNuncaValido;
		this.codigoArticuloSeleccionado			= ConstantesBD.codigoNuncaValido;
		this.resetMapaArticulos();
		this.listaArticulos 					= new ArrayList<DtoArticulosAutorizaciones>();
		this.listaServicios						= new ArrayList<DtoServiciosAutorizaciones>();
		this.mostrarImprimirAutorizacion		= false;
		this.listaNombresReportes				= new ArrayList<String>();
		this.retornarDetalleArticulos			= false;
		this.retornarDetalleServicios			= false;
		this.listaNombresReportes				= new ArrayList<String>();
		this.mostrarImprimirAutorizacion		= false;
		this.criterioBusqueda					= "";
		this.criterioBusquedaInsumo				= "";
		this.naturalezaArticuloConsultada		= "";
		this.acronimoUnidadMedidaArticulo		= "";
		this.esMedicamentoString				= "";
		this.fueEliminadoArticulo				= "";
		this.validarCantidadArticulo			= ConstantesBD.codigoNuncaValido;
		this.convenioValido						= false;
		
		this.nombreArchivoGenerado				= "";
		this.formatoImpresionDefinido			= false;
	}
	
	public void resetMapaArticulos()
	{
		this.articulos=new HashMap();
		this.articulos.put("numRegistros","0");
		this.numeroFilasMapa=0;
	}
	
	/** Reset de servicios*
	public void resetMapaServicios()
	{
		this.servicios=new HashMap<String, Object>();
		this.servicios.put("numRegistros","0");
		this.numeroFilasMapaCasoServicios=0;
	}
	
	/** Reset pendiente *
	public void resetJustificacion(){
		
		// Servicios
		this.justificacionesServicios=new HashMap();
	} **/


	/*-------------------------------------------------------*/
	/* METODOS SETs Y GETs
	/*-------------------------------------------------------*/
	

	public String getEstado() {
		return estado;
	}




	public void setEstado(String estado) {
		this.estado = estado;
	}




	public int getPosArray() {
		return posArray;
	}




	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}




	public String getPatronOrdenar() {
		return patronOrdenar;
	}




	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}




	public String getEsDescendente() {
		return esDescendente;
	}




	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}




	public ArrayList<DTOAdministracionAutorizacion> getListaAutorizacionesIngresoEstancia() {
		return listaAutorizacionesIngresoEstancia;
	}




	public void setListaAutorizacionesIngresoEstancia(
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia) {
		this.listaAutorizacionesIngresoEstancia = listaAutorizacionesIngresoEstancia;
	}




	public boolean isNuevaAutorizacion() {
		return nuevaAutorizacion;
	}




	public void setNuevaAutorizacion(boolean nuevaAutorizacion) {
		this.nuevaAutorizacion = nuevaAutorizacion;
	}




	public boolean isExtenderAutorizacion() {
		return extenderAutorizacion;
	}




	public void setExtenderAutorizacion(boolean extenderAutorizacion) {
		this.extenderAutorizacion = extenderAutorizacion;
	}




	public boolean isAutorizacionVigente() {
		return autorizacionVigente;
	}




	public void setAutorizacionVigente(boolean autorizacionVigente) {
		this.autorizacionVigente = autorizacionVigente;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getPath() {
		return path;
	}


	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}


	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}


	public void setDtoPacienteSeleccionado(DTOPacienteCapitado dtoPacienteSeleccionado) {
		this.dtoPacienteSeleccionado = dtoPacienteSeleccionado;
	}


	public DTOPacienteCapitado getDtoPacienteSeleccionado() {
		return dtoPacienteSeleccionado;
	}

	public void setDtoEntidadSubcontratada(DtoEntidadSubcontratada dtoEntidadSubcontratada) {
		this.dtoEntidadSubcontratada = dtoEntidadSubcontratada;
	}


	public DtoEntidadSubcontratada getDtoEntidadSubcontratada() {
		return dtoEntidadSubcontratada;
	}


	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}


	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}


	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}


	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}


	public void setNumeroFilasMapaCasoServicios(int numeroFilasMapaCasoServicios) {
		this.numeroFilasMapaCasoServicios = numeroFilasMapaCasoServicios;
	}


	public int getNumeroFilasMapaCasoServicios() {
		return numeroFilasMapaCasoServicios;
	}


	public void setServicios(HashMap<String, Object> servicios) {
		this.servicios = servicios;
	}


	public HashMap<String, Object> getServicios() {
		return servicios;
	}

	public void setJustificacionesServicios(HashMap justificacionesServicios) {
		this.justificacionesServicios = justificacionesServicios;
	}

	public HashMap getJustificacionesServicios() {
		return justificacionesServicios;
	}

	public boolean isMostrarPopUpTipoOrden() {
		return mostrarPopUpTipoOrden;
	}

	public void setMostrarPopUpTipoOrden(boolean mostrarPopUpTipoOrden) {
		this.mostrarPopUpTipoOrden = mostrarPopUpTipoOrden;
	}

	public void setManejoProgramacionSalas(String manejoProgramacionSalas) {
		this.manejoProgramacionSalas = manejoProgramacionSalas;
	}

	public String getManejoProgramacionSalas() {
		return manejoProgramacionSalas;
	}

	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	public void setDtoServicioHelper(DtoServicioAutoHelper dtoServicioHelper) {
		this.dtoServicioHelper = dtoServicioHelper;
	}

	public DtoServicioAutoHelper getDtoServicioHelper() {
		return dtoServicioHelper;
	}

	public void setListaServiciosAutoHelper(List<DtoServicioAutoHelper> listaServiciosAutoHelper) {
		this.listaServiciosAutoHelper = listaServiciosAutoHelper;
	}

	public List<DtoServicioAutoHelper> getListaServiciosAutoHelper() {
		return listaServiciosAutoHelper;
	}

	public void setPostListAutorizaciones(int postListAutorizaciones) {
		this.postListAutorizaciones = postListAutorizaciones;
	}

	public int getPostListAutorizaciones() {
		return postListAutorizaciones;
	}

	public void setPosListaServicios(int posListaServicios) {
		this.posListaServicios = posListaServicios;
	}

	public int getPosListaServicios() {
		return posListaServicios;
	}

	public void setPathAutorizar(String pathAutorizar) {
		this.pathAutorizar = pathAutorizar;
	}

	public String getPathAutorizar() {
		return pathAutorizar;
	}

	public void setNumeroFilasMapa(int numeroFilasMapa) {
		this.numeroFilasMapa = numeroFilasMapa;
	}

	public int getNumeroFilasMapa() {
		return numeroFilasMapa;
	}

	public HashMap getArticulos() {
		return articulos;
	}
	
	public Object getArticulos(String key) {
		return articulos.get(key);
	}

	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	
	public void setArticulos(String key,Object value) {
		this.articulos.put(key,value);
	}

	public void setCodigoArticuloSeleccionado(int codigoArticuloSeleccionado) {
		this.codigoArticuloSeleccionado = codigoArticuloSeleccionado;
	}

	public int getCodigoArticuloSeleccionado() {
		return codigoArticuloSeleccionado;
	}

	/**
	 * @return the listaServicios
	 */
	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}

	/**
	 * @param listaServicios the listaServicios to set
	 */
	public void setListaServicios(
			ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}

	/**
	 * @return the listaArticulos
	 */
	public ArrayList<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}

	/**
	 * @param listaArticulos the listaArticulos to set
	 */
	public void setListaArticulos(
			ArrayList<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}

	/**
	 * @return the mostrarImprimirAutorizacion
	 */
	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}

	/**
	 * @param mostrarImprimirAutorizacion the mostrarImprimirAutorizacion to set
	 */
	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}

	/**
	 * @return the listaNombresReportes
	 */
	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	/**
	 * @param listaNombresReportes the listaNombresReportes to set
	 */
	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	/**
	 * @return the retornarDetalleArticulos
	 */
	public boolean isRetornarDetalleArticulos() {
		return retornarDetalleArticulos;
	}

	/**
	 * @param retornarDetalleArticulos the retornarDetalleArticulos to set
	 */
	public void setRetornarDetalleArticulos(boolean retornarDetalleArticulos) {
		this.retornarDetalleArticulos = retornarDetalleArticulos;
	}

	/**
	 * @return the retornarDetalleServicios
	 */
	public boolean isRetornarDetalleServicios() {
		return retornarDetalleServicios;
	}

	/**
	 * @param retornarDetalleServicios the retornarDetalleServicios to set
	 */
	public void setRetornarDetalleServicios(boolean retornarDetalleServicios) {
		this.retornarDetalleServicios = retornarDetalleServicios;
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public String getCriterioBusquedaInsumo() {
		return criterioBusquedaInsumo;
	}

	public void setCriterioBusquedaInsumo(String criterioBusquedaInsumo) {
		this.criterioBusquedaInsumo = criterioBusquedaInsumo;
	}

	public void setNaturalezaArticuloConsultada(
			String naturalezaArticuloConsultada) {
		this.naturalezaArticuloConsultada = naturalezaArticuloConsultada;
	}

	public String getNaturalezaArticuloConsultada() {
		return naturalezaArticuloConsultada;
	}

	public void setAcronimoUnidadMedidaArticulo(
			String acronimoUnidadMedidaArticulo) {
		this.acronimoUnidadMedidaArticulo = acronimoUnidadMedidaArticulo;
	}

	public String getAcronimoUnidadMedidaArticulo() {
		return acronimoUnidadMedidaArticulo;
	}

	public void setEsMedicamentoString(String esMedicamentoString) {
		this.esMedicamentoString = esMedicamentoString;
	}

	public String getEsMedicamentoString() {
		return esMedicamentoString;
	}

	public void setFueEliminadoArticulo(String fueEliminadoArticulo) {
		this.fueEliminadoArticulo = fueEliminadoArticulo;
	}

	public String getFueEliminadoArticulo() {
		return fueEliminadoArticulo;
	}

	public void setValidarCantidadArticulo(int validarCantidadArticulo) {
		this.validarCantidadArticulo = validarCantidadArticulo;
	}

	public int getValidarCantidadArticulo() {
		return validarCantidadArticulo;
	}

	public void setConvenioValido(boolean convenioValido) {
		this.convenioValido = convenioValido;
	}

	public boolean isConvenioValido() {
		return convenioValido;
	}

	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	public void setFormatoImpresionDefinido(boolean formatoImpresionDefinido) {
		this.formatoImpresionDefinido = formatoImpresionDefinido;
	}

	public boolean isFormatoImpresionDefinido() {
		return formatoImpresionDefinido;
	}

	public Character getConvenioManejaPresupuesto() {
		return convenioManejaPresupuesto;
	}

	public void setConvenioManejaPresupuesto(Character convenioManejaPresupuesto) {
		this.convenioManejaPresupuesto = convenioManejaPresupuesto;
	}

	public DtoArticulosAutorizaciones getDtoArticulosHelper() {
		return dtoArticulosHelper;
	}

	public void setDtoArticulosHelper(DtoArticulosAutorizaciones dtoArticulosHelper) {
		this.dtoArticulosHelper = dtoArticulosHelper;
	}

	
	
	
}
