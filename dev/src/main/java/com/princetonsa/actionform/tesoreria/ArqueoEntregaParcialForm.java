package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.orm.CuentasBancarias;
import com.servinte.axioma.orm.TransportadoraValores;

/**
 * Form que contiene los datos espec&iacute;ficos de los procesos relacionados con los anexos:
 *
 * 227 - Arqueo Entrega Parcial
 * 905 - Registro Entrega a Transportadora de Valores
 * 906 - Entrega a Caja Mayor/Principal
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * La informaci&oacute;n com&uacute;n a los procesos de arqueo (Arqueo Caja, Arqueo Entrega Parcial
 * y Cierre Turno de Caja) se encuentra contenida en MovimientosCajaForm.
 *
 * @author Jorge Armando Agudelo Quintero
 * @see MovimientosCajaForm
 */
 
/**
 * @author axioma
 *
 */
public class ArqueoEntregaParcialForm extends MovimientosCajaForm {

	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que contiene toda la informaci&oacute;n necesaria para visualizar el
	 * proceso de Arqueo y que es utilizada para determinar las formas de pago junto con sus
	 * detalles que pueden ser asociados a alg&uacute;n proceso de entrega (anexo 905 - anexo 906)
	 */
	private DtoInformacionEntrega dtoInformacionEntrega;

	/**
	 * Atributo que indica el c&oacute;digo (llave primaria) de la forma de pago que 
	 * se encuentra seleccionada
	 */
	private int formaPagoCheckeada;

	/**
	 * Lista con las Transportadoras de valores registradas en el sistema
	 */
	private ArrayList<TransportadoraValores> listadoTransportadoraValores;

	/**
	 * Lista con las Entidades Financieras registradas en el sistema
	 */
	private ArrayList<DtoEntidadesFinancieras> listadoEntidadesFinancieras;

	/**
	 * Lista con las Cuentas Bancarias registradas en el sistema por entidad Financiera
	 */
	private ArrayList<CuentasBancarias> listadoCuentasBancarias;

	/**
	 * Atributo que indica la Cuenta Bancaria seleccionada en el proceso de entrega a 
	 * Transportadora de valores.
	 */
	private CuentasBancarias cuentaBancaria;

	/**
	 * DtoInformacionEntrega temporal con las formas de pago y los detalles de los documentos de
	 * soporte (informaci&oacute;n) asociados a estos
	 */
	private DtoInformacionEntrega dtoTempInformacionEntrega;
	
	/**
	 * Atributo que indica el valor de la parametrizaci&oacute;n
	 * Instituci&oacute;n Maneja Entrega a Transportadora de Valores
	 */
	private boolean manejaEntregaTransportadora;
	
	
	/**
	 * Atributo que indica si el detalle de los valores sistema, número de documento
	 * y diferencia por cada uno de los registros a entregar por forma de pago ya 
	 * ha sido mostrado en pantalla.
	 */
	private boolean mostrarDetalleEntrega;
	
	/**
	 * Atributo con el consecutivo del movimiento que se quiere consultar
	 */
	private long consecutivoMovimientoConsultar;
	
	/**
	 * Atributo con el tipo de forma de pago a imprimir en la presentación del arqueo parcial
	 */
	private int tipoFormaPago;

	/*
	 * Constructor del form
	 */
	public ArqueoEntregaParcialForm() {
		
	}

	/**
	 * Constructor de la forma. Recibe un objeto de tipo MovimientosCajaForm, del cual herada.
	 * @param forma
	 */
	public ArqueoEntregaParcialForm(MovimientosCajaForm forma) {
		this.setListadoCajas(forma.getListadoCajas());
		this.setListadoCajeros(forma.getListadoCajeros());
		this.setListadoTiposArqueo(forma.getListadoTiposArqueo());
		this.setDtoInformacionEntrega(new DtoInformacionEntrega());
		this.setEstadoGuardarMovimiento("");
		this.setMensajeProceso("");
		this.setExisteConsecutivoFaltante(forma.isExisteConsecutivoFaltante());
		this.setDtoTempInformacionEntrega(new DtoInformacionEntrega());
		this.setManejaEntregaTransportadora(true);
		this.setTurnoDeCaja(forma.getTurnoDeCaja());
		this.setFechaUltimoMovimiento(forma.getFechaUltimoMovimiento());
		this.setConsecutivoMovimientoConsultar(ConstantesBD.codigoNuncaValidoLong);
		this.tipoFormaPago=ConstantesBD.codigoNuncaValido;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return null;
	}

	/**
	 * M&eacute;todo para limpiar los atributos del formulario
	 */
	public void reset() {
		this.setListadoCajasPrincipalMayor(null);
		this.setListadoTransportadoraValores(null);
		this.setListadoEntidadesFinancieras(null);
		this.dtoInformacionEntrega = new DtoInformacionEntrega();
		this.setEstadoGuardarMovimiento("");
		this.setMensajeProceso("");
		this.setDtoTempInformacionEntrega(new DtoInformacionEntrega());
		this.setManejaEntregaTransportadora(true);
		this.setConsecutivoMovimientoConsultar(ConstantesBD.codigoNuncaValidoLong);
		this.tipoFormaPago=ConstantesBD.codigoNuncaValido;
	}

	/**
	 * M&eacute;todo que inicializa el objeto transportadoraValores seg&uacute;n la
	 * selecci&oacute;n del usuario.
	 * 
	 * @param codigoTransportadora
	 */
	public void setTransportadoraValoresHelper(int codigoTransportadora) {

		boolean asigno = false;

		if (listadoTransportadoraValores != null && codigoTransportadora >= 0) {

			for (TransportadoraValores transportadoraValores : listadoTransportadoraValores) {

				if (transportadoraValores.getCodigoPk() == codigoTransportadora) {
					asigno = true;
					dtoTempInformacionEntrega
							.setTransportadoraValores(transportadoraValores);
				}
			}
		}

		if (!asigno) {

			this.dtoTempInformacionEntrega.setTransportadoraValores(null);
		}
	}
	
	/**
	 * M&eacute;todo que retorna el c&oacute;digo de la transportadora de valores
	 * seleccionada por el usuario
	 * 
	 * @return
	 */
	public int getTransportadoraValoresHelper() {

		if (dtoTempInformacionEntrega.getTransportadoraValores() == null) {

			return ConstantesBD.codigoNuncaValido;
		}

		return dtoTempInformacionEntrega.getTransportadoraValores().getCodigoPk();
	}
	
	/**
	 * M&eacute;todo que inicializa el objeto entidadFinanciera seg&uacute;n la
	 * selecci&oacute;n del usuario.
	 * @param consecutivoEntidad
	 */
	public void setEntidadesFinancierasHelper(int consecutivoEntidad) {

		boolean asigno = false;

		if (listadoEntidadesFinancieras != null) {

			for (DtoEntidadesFinancieras entidadesFinancieras : listadoEntidadesFinancieras) {

				if (entidadesFinancieras.getConsecutivo() == consecutivoEntidad) {
					asigno = true;
					dtoTempInformacionEntrega.setEntidadFinanciera(entidadesFinancieras);
				}
			}
		}

		if (!asigno) {

			this.dtoTempInformacionEntrega.setEntidadFinanciera(null);
			this.setCuentaBancaria(null);
		}
	}

	/**
	 * M&eacute;todo que retorna el c&oacute;digo de la entidad financiera
	 * seleccionada por el usuario
	 * @return
	 */
	public int getEntidadesFinancierasHelper() {

		if (dtoTempInformacionEntrega.getEntidadFinanciera() == null) {

			return ConstantesBD.codigoNuncaValido;
		}

		return dtoTempInformacionEntrega.getEntidadFinanciera().getConsecutivo();
	}

	/**
	 * M&eacute;todo que inicializa el objeto cuentaBancaria seg&uacute;n la
	 * selecci&oacute;n del usuario.
	 * 
	 * @param codigoCuentaBancaria
	 */
	public void setCuentaBancariaHelper(short codigoCuentaBancaria) {

		boolean asigno = false;

		if (listadoCuentasBancarias != null && codigoCuentaBancaria >= 0) {

			for (CuentasBancarias cuentaBancaria : listadoCuentasBancarias){

				if (cuentaBancaria.getCodigo() == codigoCuentaBancaria) {
					asigno = true;
					this.cuentaBancaria = cuentaBancaria;
				}
			}
		}

		if (!asigno) {

			this.cuentaBancaria = null;
		}
	}

	/**
	 * M&eacute;todo que retorna el c&oacute;digo de la Cuenta bancaria seleccionada
	 * por el usuario
	 * @return
	 */
	public short getCuentaBancariaHelper() {

		if (cuentaBancaria == null) {

			return ConstantesBD.codigoNuncaValido;
		}

		return cuentaBancaria.getCodigo();
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
	 * @return the formaPagoCheckeada
	 */
	public int getFormaPagoCheckeada() {
		return formaPagoCheckeada;
	}

	/**
	 * @param formaPagoCheckeada the formaPagoCheckeada to set
	 */
	public void setFormaPagoCheckeada(int formaPagoCheckeada) {
		this.formaPagoCheckeada = formaPagoCheckeada;
	}

	/**
	 * @return the listadoTransportadoraValores
	 */
	public ArrayList<TransportadoraValores> getListadoTransportadoraValores() {
		return listadoTransportadoraValores;
	}

	/**
	 * @param listadoTransportadoraValores the listadoTransportadoraValores to set
	 */
	public void setListadoTransportadoraValores(
			ArrayList<TransportadoraValores> listadoTransportadoraValores) {
		this.listadoTransportadoraValores = listadoTransportadoraValores;
	}

	/**
	 * @return the listadoEntidadesFinancieras
	 */
	public ArrayList<DtoEntidadesFinancieras> getListadoEntidadesFinancieras() {
		return listadoEntidadesFinancieras;
	}

	/**
	 * @param listadoEntidadesFinancieras the listadoEntidadesFinancieras to set
	 */
	public void setListadoEntidadesFinancieras(
			ArrayList<DtoEntidadesFinancieras> listadoEntidadesFinancieras) {
		this.listadoEntidadesFinancieras = listadoEntidadesFinancieras;
	}

	/**
	 * @return the listadoCuentasBancarias
	 */
	public ArrayList<CuentasBancarias> getListadoCuentasBancarias() {
		return listadoCuentasBancarias;
	}

	/**
	 * @param listadoCuentasBancarias the listadoCuentasBancarias to set
	 */
	public void setListadoCuentasBancarias(
			ArrayList<CuentasBancarias> listadoCuentasBancarias) {
		this.listadoCuentasBancarias = listadoCuentasBancarias;
	}

	/**
	 * @return the cuentaBancaria
	 */
	public CuentasBancarias getCuentaBancaria() {
		return cuentaBancaria;
	}

	/**
	 * @param cuentaBancaria the cuentaBancaria to set
	 */
	public void setCuentaBancaria(CuentasBancarias cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	/**
	 * @param dtoTempInformacionEntrega the dtoTempInformacionEntrega to set
	 */
	public void setDtoTempInformacionEntrega(DtoInformacionEntrega dtoTempInformacionEntrega) {
		this.dtoTempInformacionEntrega = dtoTempInformacionEntrega;
	}

	/**
	 * @return the dtoTempInformacionEntrega
	 */
	public DtoInformacionEntrega getDtoTempInformacionEntrega() {
		return dtoTempInformacionEntrega;
	}

	/**
	 * @param manejaEntregaTransportadora the manejaEntregaTransportadora to set
	 */
	public void setManejaEntregaTransportadora(boolean manejaEntregaTransportadora) {
		this.manejaEntregaTransportadora = manejaEntregaTransportadora;
	}

	/**
	 * @return the manejaEntregaTransportadora
	 */
	public boolean isManejaEntregaTransportadora() {
		return manejaEntregaTransportadora;
	}
	
	/**
	 * @return the mostrarDetalleEntrega
	 */
	public boolean isMostrarDetalleEntrega() {
		return mostrarDetalleEntrega;
	}

	/**
	 * @param mostrarDetalleEntrega the mostrarDetalleEntrega to set
	 */
	public void setMostrarDetalleEntrega(boolean mostrarDetalleEntrega) {
		this.mostrarDetalleEntrega = mostrarDetalleEntrega;
	}

	public void setConsecutivoMovimientoConsultar(
			long consecutivoMovimientoConsultar) {
		this.consecutivoMovimientoConsultar = consecutivoMovimientoConsultar;
	}

	public long getConsecutivoMovimientoConsultar() {
		return consecutivoMovimientoConsultar;
	}

	/**
	 * @return the tipoFormaPago
	 */
	public int getTipoFormaPago() {
		return tipoFormaPago;
	}

	/**
	 * @param tipoFormaPago the tipoFormaPago to set
	 */
	public void setTipoFormaPago(int tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}
	
	
}