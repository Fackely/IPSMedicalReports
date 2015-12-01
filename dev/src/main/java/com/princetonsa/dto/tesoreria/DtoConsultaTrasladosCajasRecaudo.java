package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;

/**
 * 
 * Esta clase se encarga de almacenar los datos para la consulta de
 * traslados cajas recaudo.
 *
 * @author Yennifer Guerrero
 * @since  02/08/2010
 *
 */
public class DtoConsultaTrasladosCajasRecaudo extends DtoInformacionEntrega implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena la fecha inicial desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes.
	 */
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha final desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes
	 */
	private Date fechaFin;
	
	/**
	 * Atributo que almacena el consecutivo del centro de atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;
	
	/**
	 * Atributo que almacena el consecutivo de la solicitud
	 */
	private Long consecutivoSolicitud;
	
	/**
	 * Atributo que determina el estado de la solicitud
	 */
	private String estadoSolicitud;
	
	/**
	 * Atributo que almacena la hora de la solicitud
	 * del traslado
	 */	
	private String horaSolicitud;
	
	/**
	 * Atributo que almacena el login del cajero solicitante
	 */
	private String loginCajeroSolicitante;
	
	/**
	 * Atributo que almacena el login del cajero que acepta
	 * la solicitud
	 */
	private String loginCajeroAcepta;
		
	/**
	 * Atributo que almacena el codigo de la caja de origen
	 * de la solicitud de traslado
	 */
	private int codigoCajaOrigen;
	
	/**
	 * Atributo que almacena el codigo de la caja de destino
	 * de la solicitud de traslado
	 */
	private int codigoCajaDestino;
	
	/**
	 * Atributo que almacena el valor total de la solicitud
	 * que es la sumatoria de cada pago asociado.
	 */
	private BigDecimal valorTotalSolicitud;
	
	/**
	 * Atributo que almacena la fecha en la cual ha sido aceptada
	 * una solicitud de traslado de caja recaudo.
	 */
	private Date fechaAceptacion;
	
	/**
	 * Atributo que almacena la hora en la cual ha sido aceptada 
	 * una solicitud de traslado de caja recaudo.
	 */
	private String horaAceptacion;
	
	/**
	 * Atributo que almacena el codigo del movimiento de caja.
	 */
	private long codigoPk;
	
	/**
	 * Atributo que almacena la descripci&oacute;n de la caja 
	 * que realiza la solicitud.
	 */
	private String descripcionCajaOrigen;
	
	/**
	 * Atributo que almacena la descripci&oacute;n de la caja
	 * destino de la solicitud de traslado.
	 */
	private String descripcionCajaDestino;
	
	/**
	 * Atributo que almacena las observaciones realizadas en el movimiento
	 * de solicitud de traslado entre cajas de recaudo.
	 */
	private String observaciones;
	
	/**
	 * Atributo que almacena las observaciones realizadas en el movimiento
	 * de aceptaci&oacute;n de traslado entre cajas de recaudo.
	 */
	private String observacionesAceptacion;
	
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoConsultaTrasladosCajasRecaudo() {
		
		this.setCodigoCajaDestino(ConstantesBD.codigoNuncaValido);
		this.setCodigoCajaOrigen(ConstantesBD.codigoNuncaValido);
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaInicial
	
	 * @return retorna la variable fechaInicial 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo fechaInicial
	
	 * @param valor para el atributo fechaInicial 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaFin
	
	 * @return retorna la variable fechaFin 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo fechaFin
	
	 * @param valor para el atributo fechaFin 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo consecutivoCentroAtencion
	
	 * @return retorna la variable consecutivoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencion
	
	 * @param valor para el atributo consecutivoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo consecutivoSolicitud
	
	 * @return retorna la variable consecutivoSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public Long getConsecutivoSolicitud() {
		return consecutivoSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo consecutivoSolicitud
	
	 * @param valor para el atributo consecutivoSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoSolicitud(Long consecutivoSolicitud) {
		this.consecutivoSolicitud = consecutivoSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo estadoSolicitud
	
	 * @return retorna la variable estadoSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo estadoSolicitud
	
	 * @param valor para el atributo estadoSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo horaSolicitud
	
	 * @return retorna la variable horaSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo horaSolicitud
	
	 * @param valor para el atributo horaSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo loginCajeroSolicitante
	
	 * @return retorna la variable loginCajeroSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginCajeroSolicitante() {
		return loginCajeroSolicitante;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo loginCajeroSolicitante
	
	 * @param valor para el atributo loginCajeroSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginCajeroSolicitante(String loginCajeroSolicitante) {
		this.loginCajeroSolicitante = loginCajeroSolicitante;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo loginCajeroAcepta
	
	 * @return retorna la variable loginCajeroAcepta 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginCajeroAcepta() {
		return loginCajeroAcepta;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo loginCajeroAcepta
	
	 * @param valor para el atributo loginCajeroAcepta 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginCajeroAcepta(String loginCajeroAcepta) {
		this.loginCajeroAcepta = loginCajeroAcepta;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo codigoCajaOrigen
	
	 * @return retorna la variable codigoCajaOrigen 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoCajaOrigen() {
		return codigoCajaOrigen;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo codigoCajaOrigen
	
	 * @param valor para el atributo codigoCajaOrigen 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoCajaOrigen(int codigoCajaOrigen) {
		this.codigoCajaOrigen = codigoCajaOrigen;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo codigoCajaDestino
	
	 * @return retorna la variable codigoCajaDestino 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoCajaDestino() {
		return codigoCajaDestino;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo codigoCajaDestino
	
	 * @param valor para el atributo codigoCajaDestino 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoCajaDestino(int codigoCajaDestino) {
		this.codigoCajaDestino = codigoCajaDestino;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo valorTotalSolicitud
	
	 * @return retorna la variable valorTotalSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public BigDecimal getValorTotalSolicitud() {
		return valorTotalSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo valorTotalSolicitud
	
	 * @param valor para el atributo valorTotalSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorTotalSolicitud(BigDecimal valorTotalSolicitud) {
		this.valorTotalSolicitud = valorTotalSolicitud;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaAceptacion
	 * 
	 * @return  Retorna la variable fechaAceptacion
	 */
	public Date getFechaAceptacion() {
		return fechaAceptacion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo horaAceptacion
	 * 
	 * @return  Retorna la variable horaAceptacion
	 */
	public String getHoraAceptacion() {
		return horaAceptacion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaAceptacion
	 * 
	 * @param  valor para el atributo fechaAceptacion 
	 */
	public void setFechaAceptacion(Date fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo horaAceptacion
	 * 
	 * @param  valor para el atributo horaAceptacion 
	 */
	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPk
	 * 
	 * @return  Retorna la variable codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPk
	 * 
	 * @param  valor para el atributo codigoPk 
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCajaOrigen
	 * 
	 * @return  Retorna la variable descripcionCajaOrigen
	 */
	public String getDescripcionCajaOrigen() {
		return descripcionCajaOrigen;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCajaDestino
	 * 
	 * @return  Retorna la variable descripcionCajaDestino
	 */
	public String getDescripcionCajaDestino() {
		return descripcionCajaDestino;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionCajaOrigen
	 * 
	 * @param  valor para el atributo descripcionCajaOrigen 
	 */
	public void setDescripcionCajaOrigen(String descripcionCajaOrigen) {
		this.descripcionCajaOrigen = descripcionCajaOrigen;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionCajaDestino
	 * 
	 * @param  valor para el atributo descripcionCajaDestino 
	 */
	public void setDescripcionCajaDestino(String descripcionCajaDestino) {
		this.descripcionCajaDestino = descripcionCajaDestino;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo observaciones
	 * 
	 * @return  Retorna la variable observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo observacionesAceptacion
	 * 
	 * @return  Retorna la variable observacionesAceptacion
	 */
	public String getObservacionesAceptacion() {
		return observacionesAceptacion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo observaciones
	 * 
	 * @param  valor para el atributo observaciones 
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo observacionesAceptacion
	 * 
	 * @param  valor para el atributo observacionesAceptacion 
	 */
	public void setObservacionesAceptacion(String observacionesAceptacion) {
		this.observacionesAceptacion = observacionesAceptacion;
	}
}
