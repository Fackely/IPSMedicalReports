/*
 * Mayo 10, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */

public class TurnoDeCajaForm extends MovimientosCajaForm 
{
	private static final long serialVersionUID = 1L;

	
	/*--------------------------*/
	/* TURNO DE CAJA			*/
	/*--------------------------*/
	
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	
	/** * DTO  */
	private TurnoDeCaja dto = new TurnoDeCaja();
	
	/** * Indica si se debe mostrar el formulario nuevo/modificar  */
	private boolean mostrarFormularioIngreso;
	
	/** * Lista DTO Cajas */
	private ArrayList<Cajas> listaCajas;
	
	/** * Codigo de la caja	 */
	private String strCaja;
	
	/** * Caja seleccionada 	 */
	private Cajas cajaSeleccionada;
	
	/** * Indica el usuario puede abrir un nuevo turno 	 */
	private boolean mostrarBotonConfirmar;
	
	/** * Dto para las consultas de consolidado de cierre*/
	//private DtoConsultaMovArqueoCaja dtoConsultaMovArqueoCaja;
	
	/** * Indica el usuario puede abrir un nuevo turno 	 */
	private boolean mostrarMensajeConsecutivoFaltanteSobrante;
	
	/*-------------------------------------------------------*/
	/* ACEPTACION TRASLADO DE CAJA
	/*-------------------------------------------------------*/
	
	/** * Solicitudes 	 */
	private ArrayList<DtoSolicitudTrasladoPendiente> listaSolicitudesPendientes;

	/** * Indica si se debe mostrar una ventana con las solicitudes pendientes  */
	private boolean mostrarMensajeSolicitudes;
	
	/** * Paginador */
	private int posListaSolPendiente;
	
	/** * Lista solicitudes donde se van a manejar las aceactiones de caja 	 */
	private ArrayList<DtoConsolidadoMovimiento> aceptacionesSolicitudes;
	
	/** * Contiene la informacion detallada de la entrega realizada */
	private DtoInformacionEntrega dtoInformacionEntrega;
	
	/** * Contiene la informacion detallada de la entrega para mostrar como consolidado */
	private DtoInformacionEntrega dtoInformacionEntregaConsolidado;
	
	/** * Contiene la informacion detallada de la entrega realizada */
	private ArrayList<DtoInformacionEntrega> listaDtoInformacionEntrega;
	
	/** * Indica si se debe mostrar la pagina con los detalles de las solicitudes  */
	private boolean mostrarDetalleAceptacion;
	
	/** * Indica si se debe mostrar el resultado de la aceptacion  */
	private boolean mostrarCuadreCaja;
	//private boolean estadoMostrarCuadreCaja = true;
	
	/** * Parametro que indica si se debe mostrar el testigo en la aceptacion o no */
	private boolean parametroRequiereTestigo;
	
	/** * Parametro que indica si se debe mostrar el testigo en la aceptacion o no */
	private boolean todasSolicitudesAceptadasTemporalmente;
	
	/** * Lista de testigos para la aceptacion */
	private ArrayList<DtoUsuarioPersona> listaTestigos;
	
	/** * Parametro que indica si se deben limpiar las aceptaciones aceptadas hasta el momento */
	private boolean limpiarAceptaciones;
	
	
	/*-------------------------------------------------------*/
	/* CONSOLIDADO DE CIERRE
	/*-------------------------------------------------------*/
	
	/** * Indica el usuario puede abrir un nuevo turno  */
	private boolean mostrarConsolidadoCierre;
	
	
	

	
	/*-------------------------------------------------------*/
	/* RESET's
	/*-------------------------------------------------------*/
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dto 						= new TurnoDeCaja();
		this.mostrarFormularioIngreso	= false;
		this.listaCajas					= new ArrayList<Cajas>();
		this.strCaja					= "";
		this.mostrarBotonConfirmar		= false;
		this.limpiarAceptaciones		= true;
		//this.dtoConsultaMovArqueoCaja  	= new DtoConsultaMovArqueoCaja();
		resetAceptacion();
		resetConsolidado();
	}
	
	/**
	 * Reset de la forma para als variables utilizadas en la aceptacion
	 */
	public void resetAceptacion()
	{
		this.mostrarMensajeSolicitudes	= false;
		this.posListaSolPendiente		= ConstantesBD.codigoNuncaValido;
		this.mostrarDetalleAceptacion	= false;
		this.mostrarCuadreCaja			= false;
		this.parametroRequiereTestigo	= false;
		this.todasSolicitudesAceptadasTemporalmente = false;
		this.listaTestigos				= new ArrayList<DtoUsuarioPersona>(); 
	}
	
	/**
	 * Reset de la forma para als variables utilizadas en la aceptacion
	 */
	public void resetConsolidado()
	{
		this.mostrarConsolidadoCierre	= false;
	}
	
	/**
	 * Reset de la lista de solicitudes pendientes aceptadas temporalmente
	 */
	public void resetSolicitudesPendientes()
	{
		this.listaSolicitudesPendientes	= new ArrayList<DtoSolicitudTrasladoPendiente>();
		this.dtoInformacionEntrega 		= new DtoInformacionEntrega();
		this.listaDtoInformacionEntrega = new ArrayList<DtoInformacionEntrega>();
		this.dtoInformacionEntregaConsolidado = new DtoInformacionEntrega();
	}
	
	
	
	
	/**
	 * Reset de los mensajes
	 */
	public void resetMensajes()
	{
		this.mostrarMensajeConsecutivoFaltanteSobrante = false;
	}
	
	
	
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
		if(  (estado.equals("guardar")) || (estado.equals("guardarmodificar"))  )
		{
			// CAJA
			if(getCajaSeleccionada() == null)
			{
				errores.add("error caja", new ActionMessage("errors.required", "La Caja"));
				setMostrarFormularioIngreso(true);
			}
		}
		return errores;
	}
	
	
	
	
	/*-------------------------------------------------------*/
	/* METODOS SETs Y GETs
	/*-------------------------------------------------------*/
	
	/** 
	 * @return the mostrarConsolidadoCierre
	 */
	public boolean isMostrarConsolidadoCierre() {
		return mostrarConsolidadoCierre;
	}
	
	
	/**
	 * @return the aceptacionesSolicitudes
	 */
	public ArrayList<DtoConsolidadoMovimiento> getAceptacionesSolicitudes() {
		return aceptacionesSolicitudes;
	}

	/**
	 * @param aceptacionesSolicitudes the aceptacionesSolicitudes to set
	 */
	public void setAceptacionesSolicitudes(
			ArrayList<DtoConsolidadoMovimiento> aceptacionesSolicitudes) {
		this.aceptacionesSolicitudes = aceptacionesSolicitudes;
	}

	/**
	 * @return the posListaSolPendiente
	 */
	public int getPosListaSolPendiente() {
		return posListaSolPendiente;
	}

	/**
	 * @param posListaSolPendiente the posListaSolPendiente to set
	 */
	public void setPosListaSolPendiente(int posListaSolPendiente) {
		this.posListaSolPendiente = posListaSolPendiente;
	}

	/**
	 * @return the mostrarMensajeSolicitudes
	 */
	public boolean isMostrarMensajeSolicitudes() {
		return mostrarMensajeSolicitudes;
	}

	/**
	 * @param mostrarMensajeSolicitudes the mostrarMensajeSolicitudes to set
	 */
	public void setMostrarMensajeSolicitudes(boolean mostrarMensajeSolicitudes) {
		this.mostrarMensajeSolicitudes = mostrarMensajeSolicitudes;
	}


	/**
	 * @return the listaSolicitudesPendientes
	 */
	public ArrayList<DtoSolicitudTrasladoPendiente> getListaSolicitudesPendientes() {
		return listaSolicitudesPendientes;
	}


	/**
	 * @param listaSolicitudesPendientes the listaSolicitudesPendientes to set
	 */
	public void setListaSolicitudesPendientes(ArrayList<DtoSolicitudTrasladoPendiente> listaSolicitudesPendientes) {
		this.listaSolicitudesPendientes = listaSolicitudesPendientes;
	}
	

	/**
	 * @param mostrarConsolidadoCierre the mostrarConsolidadoCierre to set
	 */
	public void setMostrarConsolidadoCierre(boolean mostrarConsolidadoCierre) {
		this.mostrarConsolidadoCierre = mostrarConsolidadoCierre;
	}
	

	/**
	 * @return dto
	 */
	public TurnoDeCaja getDto() {
		return dto;
	}


	/**
	 * @param dto  dto a setear
	 */
	public void setDto(TurnoDeCaja dto) {
		this.dto = dto;
	}


	/**
	 * @return the cajaSeleccionada
	 */
	public Cajas getCajaSeleccionada() {
		return cajaSeleccionada;
	}


	/**
	 * @return mostrarBotonConfirmar
	 */
	public boolean isMostrarBotonConfirmar() {
		return mostrarBotonConfirmar;
	}


	/**
	 * @param mostrarBotonConfirmar  mostrarBotonConfirmar a setear
	 */
	public void setMostrarBotonConfirmar(boolean mostrarBotonConfirmar) {
		this.mostrarBotonConfirmar = mostrarBotonConfirmar;
	}


	/**
	 * @param cajaSeleccionada the cajaSeleccionada to set
	 */
	public void setCajaSeleccionada(Cajas cajaSeleccionada) {
		this.cajaSeleccionada = cajaSeleccionada;
	}

	
	/**
	 * @return the strCaja
	 */
	public String getStrCaja() 
	{
		if(getCajaSeleccionada() == null){
			strCaja = "";
		}else{
			strCaja = getCajaSeleccionada().getConsecutivo()+"";
		}
		return strCaja;
	}


	/**
	 * @param strCaja the strCaja to set
	 */
	public void setStrCaja(String strCaja) 
	{
		int caja = Utilidades.convertirAEntero(strCaja);
		if(caja>0){
			setCajaSeleccionada(new CajasDelegate().findById(caja));
			this.strCaja = strCaja;
		}else{
			setCajaSeleccionada(null);
		}
	}


	/**
	 * @return the listaCajas
	 */
	public ArrayList<Cajas> getListaCajas() {
		return listaCajas;
	}


	/**
	 * @param listaCajas the listaCajas to set
	 */
	public void setListaCajas(ArrayList<Cajas> listaCajas) {
		this.listaCajas = listaCajas;
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
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {
		return mostrarFormularioIngreso;
	}

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @param mostrarFormularioIngreso the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}


	public DtoInformacionEntrega getDtoInformacionEntrega() {
		return dtoInformacionEntrega;
	}


	public void setDtoInformacionEntrega(DtoInformacionEntrega dtoInformacionEntrega) {
		this.dtoInformacionEntrega = dtoInformacionEntrega;
	}


	public boolean isMostrarDetalleAceptacion() {
		return mostrarDetalleAceptacion;
	}


	public void setMostrarDetalleAceptacion(boolean mostrarDetalleAceptacion) {
		this.mostrarDetalleAceptacion = mostrarDetalleAceptacion;
	}


	public boolean isMostrarCuadreCaja() {
		return mostrarCuadreCaja;
	}


	public void setMostrarCuadreCaja(boolean mostrarCuadreCaja) {
		this.mostrarCuadreCaja = mostrarCuadreCaja;
	}
	
	public boolean isParametroRequiereTestigo() {
		return parametroRequiereTestigo;
	}
	
	public void setParametroRequiereTestigo(boolean parametroRequiereTestigo) {
		this.parametroRequiereTestigo = parametroRequiereTestigo;
	}
	
	public boolean isTodasSolicitudesAceptadasTemporalmente() {
		return todasSolicitudesAceptadasTemporalmente;
	}
	
	public void setTodasSolicitudesAceptadasTemporalmente(
			boolean todasSolicitudesAceptadasTemporalmente) {
		this.todasSolicitudesAceptadasTemporalmente = todasSolicitudesAceptadasTemporalmente;
	}
	
	public ArrayList<DtoUsuarioPersona> getListaTestigos() {
		return listaTestigos;
	}
	
	public void setListaTestigos(ArrayList<DtoUsuarioPersona> listaTestigos) {
		this.listaTestigos = listaTestigos;
	}
	public ArrayList<DtoInformacionEntrega> getListaDtoInformacionEntrega() {
		return listaDtoInformacionEntrega;
	}
	public void setListaDtoInformacionEntrega(
			ArrayList<DtoInformacionEntrega> listaDtoInformacionEntrega) {
		this.listaDtoInformacionEntrega = listaDtoInformacionEntrega;
	}
	public boolean isLimpiarAceptaciones() {
		return limpiarAceptaciones;
	}
	public void setLimpiarAceptaciones(boolean limpiarAceptaciones) {
		this.limpiarAceptaciones = limpiarAceptaciones;
	}

//	public DtoConsultaMovArqueoCaja getDtoConsultaMovArqueoCaja() {
//		return dtoConsultaMovArqueoCaja;
//	}
//
//	public void setDtoConsultaMovArqueoCaja(
//			DtoConsultaMovArqueoCaja dtoConsultaMovArqueoCaja) {
//		this.dtoConsultaMovArqueoCaja = dtoConsultaMovArqueoCaja;
//	}

	public boolean isMostrarMensajeConsecutivoFaltanteSobrante() {
		return mostrarMensajeConsecutivoFaltanteSobrante;
	}

	public DtoInformacionEntrega getDtoInformacionEntregaConsolidado() {
		return dtoInformacionEntregaConsolidado;
	}

	public void setDtoInformacionEntregaConsolidado(
			DtoInformacionEntrega dtoInformacionEntregaConsolidado) {
		this.dtoInformacionEntregaConsolidado = dtoInformacionEntregaConsolidado;
	}

	public void setMostrarMensajeConsecutivoFaltanteSobrante(
			boolean mostrarMensajeConsecutivoFaltanteSobrante) {
		this.mostrarMensajeConsecutivoFaltanteSobrante = mostrarMensajeConsecutivoFaltanteSobrante;
	}
	
}
