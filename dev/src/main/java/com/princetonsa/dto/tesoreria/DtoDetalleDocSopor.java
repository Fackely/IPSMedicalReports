package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;

import com.servinte.axioma.mundo.fabrica.ConstruccionEntidadFabrica;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Contiene la informacion de los documentos de soportes con informacion
 * detallada de los valores, formas de pago y entidades dado el caso.
 * 
 * @author Cristhian Murillo
 */
public class DtoDetalleDocSopor implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreRecibidoDe;
	private String nroDocumentoEntregado;
	private String nroDocumentoRecibido;
	private int consecutivoFormaPago;
	private String formaPago;
	
	private int tiposDetalleFormaPago;
	private int consecutivoEntFinanciera;
	private String descripcionEntFinanciera;
	private String consecutivoEntFinancieraTarjeta;
	private String consecutivoEntFinancieraCheque;
	
	private String reciboCajaId;
	private int detallePagosId;
	
	/**
	 * Este atributo es utilizado para identificar los detalles
	 * que ya han sido registrados en movimientos previos justo antes 
	 * del momento de registrar la informaci&oacute;n en la base de datos.
	 * Utilizado en el m&eacute;todo crearSetDocSopMovimCajas de la clase {@link ConstruccionEntidadFabrica}.
	 * 
	 * Cuando el detalle ha sido registrado se le asigna el valor 1,
	 * por defecto es -1 (no registrado)
	 */
	private int indicativoRegistroMovimiento;
	
	/**
	 * * Valor que se encuentra en la caja y es el que debe ser comparado con el
	 * valor que trae el sistema
	 */
	private BigDecimal valorActual;

	/**
	 * * Valor de la diferencia cuando exista una asociada al documento de
	 * soporte. La diferencia generada entre el valor del Sistema (igual al
	 * valorRecibido)
	 */
	private BigDecimal valorDiferencia;

	/**
	 * * Valor Sistema para almacenar los registros que tengan valor recibido
	 * (valor sistema) con este tipo de dato
	 */
	private double valorSistema;

	/**
	 * * Valor Sistema para almacenar los registros que tengan valor recibido
	 * (valor sistema) con este tipo de dato
	 */
	private BigDecimal valorSistemaBig;

	/**
	 * Determina si el valor de esta diferencia es de tipo sobrante o faltante
	 * segun ConstantesIntegridadDominio
	 */
	private String tipodiferencia;
	
	/**
	 * Indica si el documento se recibi&oacute; o no
	 */
	private String indicativoNoRecibido;
	
	public DtoDetalleDocSopor() {
		this.nombreRecibidoDe = "";
		this.nroDocumentoEntregado = "";
		this.nroDocumentoRecibido = "";
		this.valorSistemaBig = null;
		this.valorDiferencia = null;
		this.consecutivoFormaPago = ConstantesBD.codigoNuncaValido;
		this.consecutivoEntFinanciera = ConstantesBD.codigoNuncaValido;
		this.descripcionEntFinanciera = "";
		this.tiposDetalleFormaPago = ConstantesBD.codigoNuncaValido;
		this.formaPago = "";
		this.valorSistema = ConstantesBD.codigoNuncaValidoDouble;
		this.consecutivoEntFinancieraTarjeta = null;
		this.consecutivoEntFinancieraCheque = null;
		this.valorActual = null;
		this.tipodiferencia = null;
		this.indicativoNoRecibido=ConstantesBD.acronimoNo;
		this.setIndicativoRegistroMovimiento(ConstantesBD.codigoNuncaValido);
	}

	/**
	 * Verifica que tipo de dato se utilizo para almacenar el valor traido desde
	 * el sistema y devuelve un valor valido
	 * 
	 * @return
	 */
	public double getValorSistemaUnico() {
		if (this.getValorSistema() != ConstantesBD.codigoNuncaValidoDouble) {

			return getValorSistema();

		} else if (this.getValorSistemaBig() != null) {
			return this.getValorSistemaBig().doubleValue();
		}
		return ConstantesBD.codigoNuncaValidoDouble;
	}

	public String getNombreRecibidoDe() {
		return nombreRecibidoDe;
	}

	public void setNombreRecibidoDe(String nombreRecibidoDe) {
		this.nombreRecibidoDe = nombreRecibidoDe;
	}

	public String getNroDocumentoEntregado() {
		return nroDocumentoEntregado;
	}

	public void setNroDocumentoEntregado(String nroDocumentoEntregado) {
		this.nroDocumentoEntregado = nroDocumentoEntregado;
	}

	public String getNroDocumentoRecibido() {
		return nroDocumentoRecibido;
	}

	public void setNroDocumentoRecibido(String nroDocumentoRecibido) {
		this.nroDocumentoRecibido = nroDocumentoRecibido;
	}

	public BigDecimal getValorDiferencia() {
		return valorDiferencia;
	}

	public void setValorDiferencia(BigDecimal valorDiferencia) {
		this.valorDiferencia = valorDiferencia;
	}

	public void setConsecutivoEntFinanciera(int consecutivoEntFinanciera) {
		this.consecutivoEntFinanciera = consecutivoEntFinanciera;
	}

	public int getConsecutivoEntFinanciera() {
		return consecutivoEntFinanciera;
	}

	public void setDescripcionEntFinanciera(String descripcionEntFinanciera) {
		this.descripcionEntFinanciera = descripcionEntFinanciera;
	}

	public String getDescripcionEntFinanciera() {
		return descripcionEntFinanciera;
	}

	public int getTiposDetalleFormaPago() {
		return tiposDetalleFormaPago;
	}

	public void setTiposDetalleFormaPago(int tiposDetalleFormaPago) {
		this.tiposDetalleFormaPago = tiposDetalleFormaPago;
	}

	public String getConsecutivoEntFinancieraTarjeta() {
		return consecutivoEntFinancieraTarjeta;
	}

	public void setConsecutivoEntFinancieraTarjeta(
			String consecutivoEntFinancieraTarjeta) {
		this.consecutivoEntFinancieraTarjeta = consecutivoEntFinancieraTarjeta;
	}

	public String getConsecutivoEntFinancieraCheque() {
		return consecutivoEntFinancieraCheque;
	}

	public void setConsecutivoEntFinancieraCheque(
			String consecutivoEntFinancieraCheque) {
		this.consecutivoEntFinancieraCheque = consecutivoEntFinancieraCheque;
	}

	public void setValorSistema(double valorSistema) {
		this.valorSistema = valorSistema;
	}

	public double getValorSistema() {
		return valorSistema;
	}

	public String getReciboCajaId() {
		return reciboCajaId;
	}

	public void setReciboCajaId(String reciboCajaId) {
		this.reciboCajaId = reciboCajaId;
	}

	public int getDetallePagosId() {
		return detallePagosId;
	}

	public void setDetallePagosId(Integer detallePagosId) {
		
		if(detallePagosId!=null){
			
			this.detallePagosId = detallePagosId;
		}
	}

	public void setValorSistemaBig(BigDecimal valorSistemaBig) {
		this.valorSistemaBig = valorSistemaBig;
	}

	public BigDecimal getValorSistemaBig() {
		return valorSistemaBig;
	}

	public BigDecimal getValorActual() {
	
		return valorActual;
	}

	public void setValorActual(BigDecimal valorActual) {
		this.valorActual = valorActual;
	}

	public int getConsecutivoFormaPago() {
		return consecutivoFormaPago;
	}

	public void setConsecutivoFormaPago(int consecutivoFormaPago) {
		this.consecutivoFormaPago = consecutivoFormaPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getTipodiferencia() {
		return tipodiferencia;
	}

	public void setTipodiferencia(String tipodiferencia) {
		this.tipodiferencia = tipodiferencia;
	}

	/**
	 * Indica si se debe deshabilitar el indicativo de recibido
	 */
	public boolean getDeshabilitarIndicativoRecibido()
	{
		if(!UtilidadTexto.isEmpty(nroDocumentoRecibido) || ( valorActual!=null && valorActual.doubleValue()>0) )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Indica si se debe deshabilitar el ingreso de datos en los campos
	 * valorCaja y documentoRecibido
	 * @return
	 */
	public boolean getDeshabilitarCamposIngresoDatos()
	{
		if(indicativoNoRecibido!=null && indicativoNoRecibido.equals(ConstantesBD.acronimoSi))
		{
			return true;
		}
		return false;
	}

	/**
	 * @return Retorna atributo indicativoRecibido
	 */
	public String getIndicativoNoRecibido()
	{
		return indicativoNoRecibido;
	}

	/**
	 * @param indicativoRecibido Asigna atributo indicativoRecibido
	 */
	public void setIndicativoNoRecibido(String indicativoRecibido)
	{
		this.indicativoNoRecibido = indicativoRecibido;
	}

	/**
	 * @param indicativoRegistroMovimiento the indicativoRegistroMovimiento to set
	 */
	public void setIndicativoRegistroMovimiento(int indicativoRegistroMovimiento) {
		this.indicativoRegistroMovimiento = indicativoRegistroMovimiento;
	}

	/**
	 * @return the indicativoRegistroMovimiento
	 */
	public int getIndicativoRegistroMovimiento() {
		return indicativoRegistroMovimiento;
	}

}
