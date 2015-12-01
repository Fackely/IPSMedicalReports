package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadTexto;

public class DtoPresupuestoTotalConvenio implements Serializable , Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal presupuesto;
	private InfoDatosInt convenio;
	private BigDecimal valorSubTotalSinContratar;
	private BigDecimal valorSubTotalContratado;
	private BigDecimal valorTotalContratado;
	private BigDecimal valorSubTotalContratadoSinDescuentos;
	private int contrato;
	private String contratado;
	private boolean esConvenioTarjetaCliente;
	
	/**
	 * atributo para validar si lo puedo o no contratar 
	 */
	private boolean existeConvenioEnIngreso;
	
	/**
	 * Paciente tiene tarjeta activa para el convenio específico
	 */
	private boolean tarjetaActiva;

	
	public DtoPresupuestoTotalConvenio() {
           reset();
	}
	
	public void reset(){
		this.presupuesto =  new BigDecimal(0) ;
		this.convenio = new InfoDatosInt();
		this.valorSubTotalSinContratar = new BigDecimal(0);
		this.valorSubTotalContratado = new BigDecimal(0);
		this.contratado = ConstantesBD.acronimoNo;
		this.contrato = 0;
		this.existeConvenioEnIngreso= false;
		this.valorTotalContratado=new BigDecimal(0);
		this.esConvenioTarjetaCliente=false;
	}
	
	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}
	
	/**
	 * @return the presupuesto
	 */
	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getTotalSinContratar(){
		return this.getValorSubTotalSinContratar();
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getTotalEnProcesoContratacion(){
		return this.getValorSubTotalContratado();
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getTotalContratado(){
			return this.getValorSubTotalContratado();
	}

	/**
	 * @return the valorSubTotalSinContratar
	 */
	public BigDecimal getValorSubTotalSinContratar() {
		return valorSubTotalSinContratar;
	}

	/**
	 * @param valorSubTotalSinContratar the valorSubTotalSinContratar to set
	 */
	public void setValorSubTotalSinContratar(BigDecimal valorSubTotalSinContratar) {
		this.valorSubTotalSinContratar = valorSubTotalSinContratar;
	}

	/**
	 * @return the valorSubTotalContratado
	 */
	public BigDecimal getValorSubTotalContratado() {
		return valorSubTotalContratado;
	}

	/**
	 * @return the valorSubTotalContratado
	 */
	public String getValorTotalContratadoFormateado() {
		return UtilidadTexto.formatearValores(valorTotalContratado.doubleValue());
	}

	/**
	 * Obtiene el valor subtotal contratado en formato para impresión
	 * @return String con el valor formateado
	 */
	public String getValorSubTotalContratadoFormateado() {
		return UtilidadTexto.formatearValores(valorSubTotalContratado.doubleValue());
	}

	/**
	 * @param valorSubTotalContratado the valorSubTotalContratado to set
	 */
	public void setValorSubTotalContratado(BigDecimal valorSubTotalContratado) {
		this.valorSubTotalContratado = valorSubTotalContratado;
	}

	/**
	 * @return the contratado
	 */
	public String getContratado() {
		return contratado;
	}

	/**
	 * @param contratado the contratado to set
	 */
	public void setContratado(String contratado) {
		this.contratado = contratado;
	}

	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the existeConvenioEnIngreso
	 */
	public boolean isExisteConvenioEnIngreso() {
		return existeConvenioEnIngreso;
	}

	/**
	 * @return the existeConvenioEnIngreso
	 */
	public boolean getExisteConvenioEnIngreso() {
		return existeConvenioEnIngreso;
	}
	
	/**
	 * @param existeConvenioEnIngreso the existeConvenioEnIngreso to set
	 */
	public void setExisteConvenioEnIngreso(boolean existeConvenioEnIngreso) {
		this.existeConvenioEnIngreso = existeConvenioEnIngreso;
	}

	public BigDecimal getValorTotalContratado() {
		return valorTotalContratado;
	}

	public void setValorTotalContratado(BigDecimal valorTotalContratado) {
		this.valorTotalContratado = valorTotalContratado;
	}

	public BigDecimal getSubTotalContratadoSinDescuentos() {
		return valorSubTotalContratadoSinDescuentos;
	}

	public void setSubTotalContratadoSinDescuentos(
			BigDecimal valorSubTotalContratadoSinInclusiones) {
		this.valorSubTotalContratadoSinDescuentos = valorSubTotalContratadoSinInclusiones;
	}

	/**
	 * @return Retorna el atributo tarjetaActiva
	 */
	public boolean isTarjetaActiva() {
		return tarjetaActiva;
	}

	/**
	 * @param tarjetaActiva Asigna el atributo tarjetaActiva
	 */
	public void setTarjetaActiva(boolean tarjetaActiva) {
		this.tarjetaActiva = tarjetaActiva;
	}

	/**
	 * @return Retorna el atributo esConvenioTarjetaCliente
	 */
	public boolean isEsConvenioTarjetaCliente() {
		return esConvenioTarjetaCliente;
	}

	/**
	 * @param esConvenioTarjetaCliente Asigna el atributo esConvenioTarjetaCliente
	 */
	public void setEsConvenioTarjetaCliente(boolean esConvenioTarjetaCliente) {
		this.esConvenioTarjetaCliente = esConvenioTarjetaCliente;
	}
	
	/**
	 * Método que permite conocer si se debe mostrar o no
	 * un mensaje de advertencia relacionado al convenio
	 * 
	 * @return
	 */
	public boolean getExisteMensajeAdvertencia (){
		
		if (!isExisteConvenioEnIngreso() || (isEsConvenioTarjetaCliente() && !isTarjetaActiva())) {
			
			return true;
		
		}else{
			
			return false;
		}
	}
	

	/**
	 * Método que devuelve el mensaje de advertencia relacionado al convenio
	 * que se debe mostrar.
	 * 
	 * @return
	 */
	public String getMensajeAdvertencia (){
		
		String mensaje = "";
		
		if (!isExisteConvenioEnIngreso()) {
			
			mensaje =  "No asociado al Paciente";

		}else if (isEsConvenioTarjetaCliente() && !isTarjetaActiva()) {
			
			mensaje = "No tiene tarjeta activa";
		}
		
		return mensaje;
	}
}
