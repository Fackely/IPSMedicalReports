package com.princetonsa.actionform.tesoreria;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Form que contiene los datos espec&iacute;ficos para generar 
 * el Cierre de Turno de Caja (Anexo 228).
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * La informaci&oacute;n com&uacute;n a los procesos de arqueo (Arqueo Caja, Arqueo Entrega Parcial
 * y Cierre Turno de Caja) se encuentra contenida en MovimientosCajaForm.
 *
 * @author Jorge Armando Agudelo Quintero
 * @see MovimientosCajaForm
 *
 */
public class CierreTurnoCajaForm extends MovimientosCajaForm {

	private static final long serialVersionUID = 1L;

	/**
	 * DTO con el consolidado de la informaci&oacute;n disponible por forma de pago para
     * realizar un proceso de arqueo (Entregas a caja Mayor / Principal, Solicitudes de Traslado, 
     * Entregas a Transportadora de valores)
	 */
	private DtoInformacionEntrega dtoInformacionEntrega;
	
	/**
	 * Atributo que indica el valor de la parametrizaci&oacute;n
	 * Instituci&oacute;n Maneja Traslado a Otra Caja de Recaudo
	 */
	private boolean manejaTrasladoCajaRecaudo;

	/**
	 * Atributo en donde se almacena el consecutivo de la forma de pago
	 * seleccionada para mostrar el respectivo detalle de la secci&oacute;n Cuadre Caja.
	 */
	private String consecutivoFormaPagoSeleccionada;
	
	
	/**
	 * Atributo que guarda el movimiento de caja de tipo
	 * Cierre Turno de Caja, realizado en el proceso de arqueo
	 */
	private MovimientosCaja cierreTurnoCaja;
	
	/**
	 * Atributo que indica si existen formas de pago a entregar en el movimiento
	 * de Solicitud de Traslado a Caja de Recaudo.
	 */
	private boolean formasPagoRegistroSolicitud;
	
	/**
	 * Atributo que contiene la información de la consulta realizada de la 
	 * Solicitud de Traslado a Caja de Recaudo realizada en el movimiento
	 * de cierre.
	 */
	private DtoConsultaTrasladosCajasRecaudo dtoConsultaTrasladosCajasRecaudo;
	
	/**
	 * Atributo que almacena el tipo de error a ser mostrado.
	 */
	private String tipoError;
	
	
	/**
	 * Constructor del form. Recibe un objeto de tipo MovimientosCajaForm, del cual herada.
	 * @param forma
	 */
	public CierreTurnoCajaForm(MovimientosCajaForm forma) {

		this.setListadoCajas(forma.getListadoCajas());
		this.setListadoCajeros(forma.getListadoCajeros());
		this.setListadoTiposArqueo(forma.getListadoTiposArqueo());
		this.setManejaTrasladoCajaRecaudo(false);
		this.setConsecutivoFormaPagoSeleccionada("");
		this.setEstadoGuardarMovimiento("");
		this.setMensajeProceso("");
		this.setExisteConsecutivoFaltante(forma.isExisteConsecutivoFaltante());
		this.setFormasPagoRegistroSolicitud(false);
		this.setTurnoDeCaja(forma.getTurnoDeCaja());
		this.setFechaUltimoMovimiento(forma.getFechaUltimoMovimiento());
		this.setCierreTurnoCaja(new MovimientosCaja());
		this.setDtoConsultaTrasladosCajasRecaudo(new DtoConsultaTrasladosCajasRecaudo());
		this.tipoError = "";
	}

	/**
	 * M&eacute;todo encargado de procesar los errores presentados
	 * @param forma
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return null;
	}

	/**
	 * @return the dtoInformacionEntrega
	 */
	public DtoInformacionEntrega getDtoInformacionEntrega() {
		return dtoInformacionEntrega;
	}

	/**
	 * @param dtoInformacionEntrega the dtoInformacionEntrega to set
	 */
	public void setDtoInformacionEntrega(DtoInformacionEntrega dtoInformacionEntrega) {
		this.dtoInformacionEntrega = dtoInformacionEntrega;
	}

	/**
	 * @return the manejaTrasladoCajaRecaudo
	 */
	public boolean isManejaTrasladoCajaRecaudo() {
		return manejaTrasladoCajaRecaudo;
	}

	/**
	 * @param manejaTrasladoCajaRecaudo the manejaTrasladoCajaRecaudo to set
	 */
	public void setManejaTrasladoCajaRecaudo(boolean manejaTrasladoCajaRecaudo) {
		this.manejaTrasladoCajaRecaudo = manejaTrasladoCajaRecaudo;
	}

	/**
	 * @return the consecutivoFormaPagoSeleccionada
	 */
	public String getConsecutivoFormaPagoSeleccionada() {
		return consecutivoFormaPagoSeleccionada;
	}

	/**
	 * @param consecutivoFormaPagoSeleccionada the consecutivoFormaPagoSeleccionada to set
	 */
	public void setConsecutivoFormaPagoSeleccionada(
			String consecutivoFormaPagoSeleccionada) {
		this.consecutivoFormaPagoSeleccionada = consecutivoFormaPagoSeleccionada;
	}

	/**
	 * @param formasPagoRegistroSolicitud the formasPagoRegistroSolicitud to set
	 */
	public void setFormasPagoRegistroSolicitud(boolean formasPagoRegistroSolicitud) {
		this.formasPagoRegistroSolicitud = formasPagoRegistroSolicitud;
	}

	/**
	 * @return the formasPagoRegistroSolicitud
	 */
	public boolean isFormasPagoRegistroSolicitud() {
		return formasPagoRegistroSolicitud;
	}

	/**
	 * @param dtoConsultaTrasladosCajasRecaudo the dtoConsultaTrasladosCajasRecaudo to set
	 */
	public void setDtoConsultaTrasladosCajasRecaudo(
			DtoConsultaTrasladosCajasRecaudo dtoConsultaTrasladosCajasRecaudo) {
		this.dtoConsultaTrasladosCajasRecaudo = dtoConsultaTrasladosCajasRecaudo;
	}

	/**
	 * @return the dtoConsultaTrasladosCajasRecaudo
	 */
	public DtoConsultaTrasladosCajasRecaudo getDtoConsultaTrasladosCajasRecaudo() {
		return dtoConsultaTrasladosCajasRecaudo;
	}

	/**
	 * @param cierreTurnoCaja the cierreTurnoCaja to set
	 */
	public void setCierreTurnoCaja(MovimientosCaja cierreTurnoCaja) {
		this.cierreTurnoCaja = cierreTurnoCaja;
	}

	/**
	 * @return the cierreTurnoCaja
	 */
	public MovimientosCaja getCierreTurnoCaja() {
		return cierreTurnoCaja;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoError
	 * @return retorna la variable tipoError 
	 * @author Yennifer Guerrero 
	 */
	public String getTipoError() {
		return tipoError;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoError
	 * @param valor para el atributo tipoError 
	 * @author Yennifer Guerrero
	 */
	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}
}
