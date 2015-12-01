package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DtoConsolidadoCierreXCentroAtencion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal valor;
	private String formaPagoDescripcion;
	private String centroAtencion;
	private String Insitucion;
	private String tipoDiferenciaFaltante;
	private BigDecimal valorDiferencia;
	private String horaApertura;
	private String hora;
	private Date fecha;
	private String primerNombre;
	private String segundonombre;
	private String primerApellido;
	private String segundoApellido;
	private String login;
	private String cajaDescripcion;
	private Integer cajaCodigo;
	private Long consecutivo;
	private Long movimientoCaja;
	private Long trasladoCajaPrincipalMayor;
	private Long consecutivoTraslado;
	private String cajaInicialDescripcion;
	private Integer cajaInicialCodigo;
	private String cajaFinalDescripcion;
	private Integer cajaFinalCodigo;
	private Date fechaTraslado;
	private String horaTraslado;
	private String primerNombreTraslado;
	private String segundonombreTraslado;
	private String primerApellidoTraslado;
	private String segundoApellidoTraslado;
	private String loginTraslado;
	
	/**
	 * Constructor de la clase
	 */
	public DtoConsolidadoCierreXCentroAtencion() 
	{
		this.consecutivo 				= null;
		this.movimientoCaja				= null;
		this.trasladoCajaPrincipalMayor	= null;
		this.consecutivoTraslado		= null;
		this.cajaInicialDescripcion		= null;
		this.cajaInicialCodigo			= null;
		this.cajaFinalDescripcion		= null;
		this.cajaFinalCodigo			= null;
		this.fechaTraslado				= null;
		this.horaTraslado				= null;
		this.primerNombreTraslado		= null;
		this.segundonombreTraslado		= null;
		this.primerApellidoTraslado		= null;
		this.segundoApellidoTraslado	= null;
		this.loginTraslado				= null;
	}


	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}



	/**
	 * @param valor the valor to set
	 */
	public void setValor(Object valor) {
		
		BigDecimal valorDefinitivo= new BigDecimal(0);
		
		
		if (valor instanceof BigDecimal ) {
			valorDefinitivo =(BigDecimal) valor;
		} else if (valor instanceof Double) {
			valorDefinitivo = BigDecimal.valueOf((Double)valor);
		}
		
		this.valor = valorDefinitivo;
	}


	/**
	 * @return the formaPagoDescripcion
	 */
	public String getFormaPagoDescripcion() {
		return formaPagoDescripcion;
	}





	/**
	 * @param formaPagoDescripcion the formaPagoDescripcion to set
	 */
	public void setFormaPagoDescripcion(String formaPagoDescripcion) {
		this.formaPagoDescripcion = formaPagoDescripcion;
	}





	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}





	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}





	/**
	 * @return the insitucion
	 */
	public String getInsitucion() {
		return Insitucion;
	}





	/**
	 * @param insitucion the insitucion to set
	 */
	public void setInsitucion(String insitucion) {
		Insitucion = insitucion;
	}





	/**
	 * @return the tipoDiferenciaFaltante
	 */
	public String getTipoDiferenciaFaltante() {
		return tipoDiferenciaFaltante;
	}





	/**
	 * @param tipoDiferenciaFaltante the tipoDiferenciaFaltante to set
	 */
	public void setTipoDiferenciaFaltante(String tipoDiferenciaFaltante) {
		this.tipoDiferenciaFaltante = tipoDiferenciaFaltante;
	}





	/**
	 * @return the valorDiferencia
	 */
	public BigDecimal getValorDiferencia() {
		return valorDiferencia;
	}





	/**
	 * @param valorDiferencia the valorDiferencia to set
	 */
	public void setValorDiferencia(BigDecimal valorDiferencia) {
		this.valorDiferencia = valorDiferencia;
	}





	public String getHoraApertura() {
		return horaApertura;
	}





	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}





	public String getHora() {
		return hora;
	}





	public void setHora(String hora) {
		this.hora = hora;
	}





	public String getPrimerNombre() {
		return primerNombre;
	}





	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}





	public String getSegundonombre() {
		return segundonombre;
	}





	public void setSegundonombre(String segundonombre) {
		this.segundonombre = segundonombre;
	}





	public String getPrimerApellido() {
		return primerApellido;
	}





	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}





	public String getSegundoApellido() {
		return segundoApellido;
	}





	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}





	public String getLogin() {
		return login;
	}





	public void setLogin(String login) {
		this.login = login;
	}





	public String getCajaDescripcion() {
		return cajaDescripcion;
	}





	public void setCajaDescripcion(String cajaDescripcion) {
		this.cajaDescripcion = cajaDescripcion;
	}





	public Integer getCajaCodigo() {
		return cajaCodigo;
	}





	public void setCajaCodigo(Integer cajaCodigo) {
		this.cajaCodigo = cajaCodigo;
	}





	/**
	 * @return the consecutivo
	 */
	public Long getConsecutivo() {
		return consecutivo;
	}





	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}


	/**
	 * @return the movimientoCaja
	 */
	public Long getMovimientoCaja() {
		return movimientoCaja;
	}


	/**
	 * @param movimientoCaja the movimientoCaja to set
	 */
	public void setMovimientoCaja(Long movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}


	/**
	 * @return the trasladoCajaPrincipalMayor
	 */
	public Long getTrasladoCajaPrincipalMayor() {
		return trasladoCajaPrincipalMayor;
	}


	/**
	 * @param trasladoCajaPrincipalMayor the trasladoCajaPrincipalMayor to set
	 */
	public void setTrasladoCajaPrincipalMayor(Long trasladoCajaPrincipalMayor) {
		this.trasladoCajaPrincipalMayor = trasladoCajaPrincipalMayor;
	}


	/**
	 * @return valor de consecutivoTraslado
	 */
	public Long getConsecutivoTraslado() {
		return consecutivoTraslado;
	}


	/**
	 * @param consecutivoTraslado el consecutivoTraslado para asignar
	 */
	public void setConsecutivoTraslado(Long consecutivoTraslado) {
		this.consecutivoTraslado = consecutivoTraslado;
	}


	/**
	 * @return valor de cajaInicialDescripcion
	 */
	public String getCajaInicialDescripcion() {
		return cajaInicialDescripcion;
	}


	/**
	 * @param cajaInicialDescripcion el cajaInicialDescripcion para asignar
	 */
	public void setCajaInicialDescripcion(String cajaInicialDescripcion) {
		this.cajaInicialDescripcion = cajaInicialDescripcion;
	}


	/**
	 * @return valor de cajaInicialCodigo
	 */
	public Integer getCajaInicialCodigo() {
		return cajaInicialCodigo;
	}


	/**
	 * @param cajaInicialCodigo el cajaInicialCodigo para asignar
	 */
	public void setCajaInicialCodigo(Integer cajaInicialCodigo) {
		this.cajaInicialCodigo = cajaInicialCodigo;
	}


	/**
	 * @return valor de cajaFinalDescripcion
	 */
	public String getCajaFinalDescripcion() {
		return cajaFinalDescripcion;
	}


	/**
	 * @param cajaFinalDescripcion el cajaFinalDescripcion para asignar
	 */
	public void setCajaFinalDescripcion(String cajaFinalDescripcion) {
		this.cajaFinalDescripcion = cajaFinalDescripcion;
	}


	/**
	 * @return valor de cajaFinalCodigo
	 */
	public Integer getCajaFinalCodigo() {
		return cajaFinalCodigo;
	}


	/**
	 * @param cajaFinalCodigo el cajaFinalCodigo para asignar
	 */
	public void setCajaFinalCodigo(Integer cajaFinalCodigo) {
		this.cajaFinalCodigo = cajaFinalCodigo;
	}


	/**
	 * @return valor de fechaTraslado
	 */
	public Date getFechaTraslado() {
		return fechaTraslado;
	}


	/**
	 * @param fechaTraslado el fechaTraslado para asignar
	 */
	public void setFechaTraslado(Date fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}


	/**
	 * @return valor de horaTraslado
	 */
	public String getHoraTraslado() {
		return horaTraslado;
	}


	/**
	 * @param horaTraslado el horaTraslado para asignar
	 */
	public void setHoraTraslado(String horaTraslado) {
		this.horaTraslado = horaTraslado;
	}


	/**
	 * @return valor de primerNombreTraslado
	 */
	public String getPrimerNombreTraslado() {
		return primerNombreTraslado;
	}


	/**
	 * @param primerNombreTraslado el primerNombreTraslado para asignar
	 */
	public void setPrimerNombreTraslado(String primerNombreTraslado) {
		this.primerNombreTraslado = primerNombreTraslado;
	}


	/**
	 * @return valor de segundonombreTraslado
	 */
	public String getSegundonombreTraslado() {
		return segundonombreTraslado;
	}


	/**
	 * @param segundonombreTraslado el segundonombreTraslado para asignar
	 */
	public void setSegundonombreTraslado(String segundonombreTraslado) {
		this.segundonombreTraslado = segundonombreTraslado;
	}


	/**
	 * @return valor de primerApellidoTraslado
	 */
	public String getPrimerApellidoTraslado() {
		return primerApellidoTraslado;
	}


	/**
	 * @param primerApellidoTraslado el primerApellidoTraslado para asignar
	 */
	public void setPrimerApellidoTraslado(String primerApellidoTraslado) {
		this.primerApellidoTraslado = primerApellidoTraslado;
	}


	/**
	 * @return valor de segundoApellidoTraslado
	 */
	public String getSegundoApellidoTraslado() {
		return segundoApellidoTraslado;
	}


	/**
	 * @param segundoApellidoTraslado el segundoApellidoTraslado para asignar
	 */
	public void setSegundoApellidoTraslado(String segundoApellidoTraslado) {
		this.segundoApellidoTraslado = segundoApellidoTraslado;
	}


	/**
	 * @return valor de loginTraslado
	 */
	public String getLoginTraslado() {
		return loginTraslado;
	}


	/**
	 * @param loginTraslado el loginTraslado para asignar
	 */
	public void setLoginTraslado(String loginTraslado) {
		this.loginTraslado = loginTraslado;
	}


	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}


	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	
}
