package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionesSolicitudesDto;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author 
 * @since 
 */
public class ConsultaAutorizacionesCapitacionSubDetalleForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para definir la acción que se está
	 * realizando
	 */
	private String estado="";
	
	
	/**
	 * Atributo que almacena el código de la autorización seleccionada para ver su detalle
	 */
	private long codigoAutorizacion;	
	
	/**
	 * Atributo que almacena los datos de la autorización de capitación
	 */
	private DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionCapitacion;												   
	
	
	/**
	 * Atributo que almacena los datos de la autorización de ingreso estancia
	 */
	private DTOAutorizacionIngresoEstancia dtoAutorizacionIngEstancia;
	
	/**
	 * Atributo que determina si la autorizacion es de ingreso estancia o por solicitud
	 */
	private String tipoAutorizacion;
	
	/**
	 * Atributo que contiene las solicitudes autorizadas y que provienen de una orden ambulatoria o peticion
	 */	
	private ArrayList<AutorizacionesSolicitudesDto> listaSolicitudesOrdenesPeticiones;
	
	/**
	 * Atributo que contiene los servicios de la autorización
	 */	
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	
	/**
	 * Atributo que contiene los artículos de la autorización
	 */
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	
	/**
	 * Atributo que determina a que página debe volver la funcionlidad
	 */
	private String paginaRedireccion;	
	
	/**
	 * Atributo que contiene los días de estancia ingresados por el usuario
	 */
	private int diasEstanciaActualizados;
	
	private boolean procesoExitoso;
	
	private long indiceEntidadSub;
	
	private int numeroSolicitud;
	
	private ArrayList<DtoEntidadSubcontratada> listadoEntidadesSub;
	
	private boolean solPYP;
	
	private ArrayList<String> listaNombresReportes;
	/**lista registros de prorroga historicos para el detalle de la autorizacion */		
	private ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaProrroga;
	/**lista registros de anulacion historicos para el detalle de la autorizacion */
	private ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAnulada;
	/**lista registros de temporal historicos para el detalle de la autorizacion */
	private ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaTemporal;
	/**lista registros de prorroga historicos para el detalle de la autorizacion de ingreso estancia*/
	private ArrayList<DTOAutorizacionIngresoEstancia> listaModificaIngEstancia;
	/**lista registros de prorroga historicos para el detalle de la autorizacion de ingreso estancia*/
	private ArrayList<DTOAutorizacionIngresoEstancia> listaAnuladaIngEstancia;
	/**lista registros de prorroga historicos para el detalle de la autorizacion de ingreso estancia*/
	private ArrayList<DTOAutorizacionIngresoEstancia> listaTemporalIngEstancia;
	/**Atributo para saber cuando el usuario capitado tiene otras autorizaciones de ingreso estancia relacionadas*/
	private String tieneOtrasAutorizaciones; 
	/** Usuario Capitado  para consulta de otras autorizaciones de ingreso Estancia -->Historico*/
	private DtoUsuariosCapitados usuarioCapitadoSeleccionado;
	
	private String abrirPopUpOtrasAutorizaciones =ConstantesBD.acronimoNo;;
	/**Carga datos del paciente que se llena en la 'forma' cuando busqueda es por rangos*/
	private PersonaBasica paciente;
	
	private boolean consultaPorRango; 
	
	/**
	 * Atributo que controla si se muestra o no el titulo de la trazabilidad
	 */
	private boolean mostrarTituloTrazabilidad;
	
	/**
	 * Atributo que representa ala primera fecha de vencimiento de la autorización
	 */
	private java.util.Date primeraFechaVencimiento;
	
	/**
	 * Atributo que almacena la fecha de la autorización
	 */
	private String fechaAutorizacion;
	
	/**
	 * Atributo que almacena la bandera que permite mostrar popup cuando es la primera impresion de la autorizacion 
	 */
	private boolean mostrarPopupPrimeraImpresion;
	
	/**
	 * Atributo que almacena la bandera que permite identificar en la impresion si es (true: original, false:copia, null:nunguna de las anteriores)
	 */
	private Boolean esImpresionOriginal;
	
	/**
	 * Atributo que almacena el nombre de la persona quien recibe la autorizacion
	 */
	private String personaRecibeAutorizacion;
	
	/**
	 * Atributo que almacena las observaciones en la entrega de la autorizacion
	 */
	private String observacionesEntregaAutorizacion;
	
	/**
	 * Este método se encarga de inicializar los valores de la 
	 * página de consulta de las autorizaciones de capitación.
	 * 
	 * @author,
	 */
	public void reset(){	
		this.estado="";		
		this.codigoAutorizacion=ConstantesBD.codigoNuncaValidoLong;
		this.dtoAutorizacionCapitacion = new DTOAutorEntidadSubcontratadaCapitacion();
		this.dtoAutorizacionIngEstancia = new DTOAutorizacionIngresoEstancia();
		this.tipoAutorizacion="";
		this.listaSolicitudesOrdenesPeticiones=new ArrayList<AutorizacionesSolicitudesDto>();
		this.listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
		this.listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
		this.paginaRedireccion = "";	
		this.diasEstanciaActualizados = ConstantesBD.codigoNuncaValido;
		this.procesoExitoso = false;
		this.indiceEntidadSub = ConstantesBD.codigoNuncaValidoLong;
		this.listadoEntidadesSub = new ArrayList<DtoEntidadSubcontratada>();
		this.numeroSolicitud=ConstantesBD.codigoNuncaValido;
		this.listaProrroga		= new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
		this.listaAnulada		= new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
		this.listaTemporal		= new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
		this.listaModificaIngEstancia	= new ArrayList<DTOAutorizacionIngresoEstancia>();
		this.listaAnuladaIngEstancia	= new ArrayList<DTOAutorizacionIngresoEstancia>();
		this.listaTemporalIngEstancia	= new ArrayList<DTOAutorizacionIngresoEstancia>();
		this.setTieneOtrasAutorizaciones(ConstantesBD.acronimoNo);
		this.usuarioCapitadoSeleccionado= new DtoUsuariosCapitados();
		this.abrirPopUpOtrasAutorizaciones=ConstantesBD.acronimoNo;		
		this.usuarioCapitadoSeleccionado=new DtoUsuariosCapitados();
		this.paciente=new PersonaBasica();
		this.consultaPorRango=false;
		this.mostrarTituloTrazabilidad=false;
		this.primeraFechaVencimiento = null;
		this.fechaAutorizacion="";
		
		this.mostrarPopupPrimeraImpresion = false;
		this.esImpresionOriginal = null;
		this.personaRecibeAutorizacion  = "";
		this.observacionesEntregaAutorizacion = "";
	}
	
	/**
	 * Metodo que se encaraga de inicializar la lista del reporte de 
	 * Articulos y Servicios
	 * 
	 */
	public void resetDatosReporte(){
		this.listaNombresReportes=new ArrayList<String>();
		this.estado="";
		this.paciente=new PersonaBasica();
		//this.consultaPorRango=false;
		this.dtoAutorizacionIngEstancia=new DTOAutorizacionIngresoEstancia();
		this.dtoAutorizacionCapitacion=new DTOAutorEntidadSubcontratadaCapitacion();
		this.listaSolicitudesOrdenesPeticiones = new ArrayList<AutorizacionesSolicitudesDto>();
		this.listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
		this.listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author, 
	 *
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=null;
		errores=new ActionErrors();
		
		
		return errores;
		
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author  
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAutorizacion
	
	 * @return retorna la variable codigoAutorizacion 
	 * @author 
	 */
	public long getCodigoAutorizacion() {
		return codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAutorizacion
	
	 * @param valor para el atributo codigoAutorizacion 
	 * @author 
	 */
	public void setCodigoAutorizacion(long codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}


	public DTOAutorEntidadSubcontratadaCapitacion getDtoAutorizacionCapitacion() {
		return dtoAutorizacionCapitacion;
	}


	public void setDtoAutorizacionCapitacion(
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionCapitacion) {
		this.dtoAutorizacionCapitacion = dtoAutorizacionCapitacion;
	}


	public DTOAutorizacionIngresoEstancia getDtoAutorizacionIngEstancia() {
		return dtoAutorizacionIngEstancia;
	}


	public void setDtoAutorizacionIngEstancia(
			DTOAutorizacionIngresoEstancia dtoAutorizacionIngEstancia) {
		this.dtoAutorizacionIngEstancia = dtoAutorizacionIngEstancia;
	}


	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}


	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}


	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}


	public void setListaServicios(
			ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}


	public ArrayList<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}


	public void setListaArticulos(
			ArrayList<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}


	public String getPaginaRedireccion() {
		return paginaRedireccion;
	}


	public void setPaginaRedireccion(String paginaRedireccion) {
		this.paginaRedireccion = paginaRedireccion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasEstanciaActualizados
	
	 * @return retorna la variable diasEstanciaActualizados 
	 * @author Angela Maria Aguirre 
	 */
	public int getDiasEstanciaActualizados() {
		return diasEstanciaActualizados;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasEstanciaActualizados
	
	 * @param valor para el atributo diasEstanciaActualizados 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasEstanciaActualizados(int diasEstanciaActualizados) {
		this.diasEstanciaActualizados = diasEstanciaActualizados;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo procesoExitoso
	
	 * @return retorna la variable procesoExitoso 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo procesoExitoso
	
	 * @param valor para el atributo procesoExitoso 
	 * @author Angela Maria Aguirre 
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}


	/**
	 * @return the indiceEntidadSub
	 */
	public long getIndiceEntidadSub() {
		return indiceEntidadSub;
	}


	/**
	 * @param indiceEntidadSub the indiceEntidadSub to set
	 */
	public void setIndiceEntidadSub(long indiceEntidadSub) {
		this.indiceEntidadSub = indiceEntidadSub;
	}


	/**
	 * @return the listadoEntidadesSub
	 */
	public ArrayList<DtoEntidadSubcontratada> getListadoEntidadesSub() {
		return listadoEntidadesSub;
	}


	/**
	 * @param listadoEntidadesSub the listadoEntidadesSub to set
	 */
	public void setListadoEntidadesSub(
			ArrayList<DtoEntidadSubcontratada> listadoEntidadesSub) {
		this.listadoEntidadesSub = listadoEntidadesSub;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}


	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}


	public boolean isSolPYP() {
		return solPYP;
	}

	public void setListaProrroga(ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaProrroga) {
		this.listaProrroga = listaProrroga;
	}


	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> getListaProrroga() {
		return listaProrroga;
	}


	public void setListaAnulada(ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAnulada) {
		this.listaAnulada = listaAnulada;
	}


	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> getListaAnulada() {
		return listaAnulada;
	}


	public void setListaTemporal(ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaTemporal) {
		this.listaTemporal = listaTemporal;
	}


	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> getListaTemporal() {
		return listaTemporal;
	}


	public void setListaAnuladaIngEstancia(ArrayList<DTOAutorizacionIngresoEstancia> listaAnuladaIngEstancia) {
		this.listaAnuladaIngEstancia = listaAnuladaIngEstancia;
	}


	public ArrayList<DTOAutorizacionIngresoEstancia> getListaAnuladaIngEstancia() {
		return listaAnuladaIngEstancia;
	}


	public void setListaTemporalIngEstancia(ArrayList<DTOAutorizacionIngresoEstancia> listaTemporalIngEstancia) {
		this.listaTemporalIngEstancia = listaTemporalIngEstancia;
	}


	public ArrayList<DTOAutorizacionIngresoEstancia> getListaTemporalIngEstancia() {
		return listaTemporalIngEstancia;
	}


	public void setListaModificaIngEstancia(ArrayList<DTOAutorizacionIngresoEstancia> listaModificaIngEstancia) {
		this.listaModificaIngEstancia = listaModificaIngEstancia;
	}


	public ArrayList<DTOAutorizacionIngresoEstancia> getListaModificaIngEstancia() {
		return listaModificaIngEstancia;
	}


	public void setTieneOtrasAutorizaciones(String tieneOtrasAutorizaciones) {
		this.tieneOtrasAutorizaciones = tieneOtrasAutorizaciones;
	}


	public String getTieneOtrasAutorizaciones() {
		return tieneOtrasAutorizaciones;
	}


	public void setUsuarioCapitadoSeleccionado(
			DtoUsuariosCapitados usuarioCapitadoSeleccionado) {
		this.usuarioCapitadoSeleccionado = usuarioCapitadoSeleccionado;
	}


	public DtoUsuariosCapitados getUsuarioCapitadoSeleccionado() {
		return usuarioCapitadoSeleccionado;
	}


	public void setAbrirPopUpOtrasAutorizaciones(
			String abrirPopUpOtrasAutorizaciones) {
		this.abrirPopUpOtrasAutorizaciones = abrirPopUpOtrasAutorizaciones;
	}


	public String getAbrirPopUpOtrasAutorizaciones() {
		return abrirPopUpOtrasAutorizaciones;
	}

	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}

	public PersonaBasica getPaciente() {
		return paciente;
	}

	public void setConsultaPorRango(boolean consultaPorRango) {
		this.consultaPorRango = consultaPorRango;
	}

	public boolean isConsultaPorRango() {
		return consultaPorRango;
	}

	/**
	 * @return the mostrarTituloTrazabilidad
	 */
	public boolean isMostrarTituloTrazabilidad() {
		return mostrarTituloTrazabilidad;
	}

	/**
	 * @param mostrarTituloTrazabilidad the mostrarTituloTrazabilidad to set
	 */
	public void setMostrarTituloTrazabilidad(boolean mostrarTituloTrazabilidad) {
		this.mostrarTituloTrazabilidad = mostrarTituloTrazabilidad;
	}

	/**
	 * @return the primeraFechaVencimiento
	 */
	public java.util.Date getPrimeraFechaVencimiento() {
		return primeraFechaVencimiento;
	}

	/**
	 * @param primeraFechaVencimiento the primeraFechaVencimiento to set
	 */
	public void setPrimeraFechaVencimiento(java.util.Date primeraFechaVencimiento) {
		this.primeraFechaVencimiento = primeraFechaVencimiento;
	}

	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}
	/**
	 * Atributo que contiene las solicitudes autorizadas y que provienen de una orden ambulatoria o peticion
	 */	
	public ArrayList<AutorizacionesSolicitudesDto> getListaSolicitudesOrdenesPeticiones() {
		return listaSolicitudesOrdenesPeticiones;
	}

	public void setListaSolicitudesOrdenesPeticiones(
			List<AutorizacionesSolicitudesDto> list) {
		this.listaSolicitudesOrdenesPeticiones = (ArrayList<AutorizacionesSolicitudesDto>) list;
	}

	/**
	 * @return the mostrarPopupPrimeraImpresion
	 */
	public boolean isMostrarPopupPrimeraImpresion() {
		return mostrarPopupPrimeraImpresion;
	}

	/**
	 * @param mostrarPopupPrimeraImpresion the mostrarPopupPrimeraImpresion to set
	 */
	public void setMostrarPopupPrimeraImpresion(boolean mostrarPopupPrimeraImpresion) {
		this.mostrarPopupPrimeraImpresion = mostrarPopupPrimeraImpresion;
	}

	/**
	 * @return the esImpresionOriginal
	 */
	public Boolean getEsImpresionOriginal() {
		return esImpresionOriginal;
	}

	/**
	 * @param esImpresionOriginal the esImpresionOriginal to set
	 */
	public void setEsImpresionOriginal(Boolean esImpresionOriginal) {
		this.esImpresionOriginal = esImpresionOriginal;
	}

	/**
	 * @return the personaRecibeAutorizacion
	 */
	public String getPersonaRecibeAutorizacion() {
		return personaRecibeAutorizacion;
	}

	/**
	 * @param personaRecibeAutorizacion the personaRecibeAutorizacion to set
	 */
	public void setPersonaRecibeAutorizacion(String personaRecibeAutorizacion) {
		this.personaRecibeAutorizacion = personaRecibeAutorizacion;
	}

	/**
	 * @return the observacionesEntregaAutorizacion
	 */
	public String getObservacionesEntregaAutorizacion() {
		return observacionesEntregaAutorizacion;
	}

	/**
	 * @param observacionesEntregaAutorizacion the observacionesEntregaAutorizacion to set
	 */
	public void setObservacionesEntregaAutorizacion(
			String observacionesEntregaAutorizacion) {
		this.observacionesEntregaAutorizacion = observacionesEntregaAutorizacion;
	}

}
