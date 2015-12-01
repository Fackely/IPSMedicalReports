package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * 
 * @author axioma
 *
 */
public class InfoPresupuestoPrecontratado implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7790749547077165908L;

	/**
	 * 
	 */
	private BigDecimal codigoPkPresupuesto;
	
	/**
	 * 
	 */
	private BigDecimal consecutivo;
	
	/**
	 * 
	 */
	private BigDecimal valorPresupuesto;
	
	/**
	 * 
	 */
	private String estadoSolicitudAutorizacionDcto;
	
	/**
	 * 
	 */
	private BigDecimal valorAutorizadoDcto;

	/**
	 * 
	 */
	private BigDecimal codigoPkPresuDctoOdo;
	
	/**
	 * 
	 */
	public InfoPresupuestoPrecontratado() {
		super();
		this.codigoPkPresupuesto= BigDecimal.ZERO;
		this.consecutivo = BigDecimal.ZERO;
		this.valorPresupuesto = BigDecimal.ZERO;
		this.estadoSolicitudAutorizacionDcto = "";
		this.valorAutorizadoDcto = BigDecimal.ZERO;
		this.codigoPkPresuDctoOdo= BigDecimal.ZERO;
	}

	/**
	 * @return the consecutivo
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the valorPresupuesto
	 */
	public BigDecimal getValorPresupuesto() {
		return valorPresupuesto;
	}

	/**
	 * @return the valorPresupuesto
	 */
	public String getValorPresupuestoFormateado() {
		return UtilidadTexto.formatearValores(valorPresupuesto.doubleValue());
	}
	
	
	/**
	 * @param valorPresupuesto the valorPresupuesto to set
	 */
	public void setValorPresupuesto(BigDecimal valorPresupuesto) {
		this.valorPresupuesto = valorPresupuesto;
	}

	/**
	 * @return the estadoSolicitudAutorizacionDcto
	 */
	public String getEstadoSolicitudAutorizacionDcto() {
		return estadoSolicitudAutorizacionDcto;
	}

	/**
	 * @return the estadoSolicitudAutorizacionDcto
	 */
	public String getEstadoSolicitudAutorizacionDctoFormateado() {
		return ValoresPorDefecto.getIntegridadDominio(estadoSolicitudAutorizacionDcto)+"";
	}

	
	
	/**
	 * @param estadoSolicitudAutorizacionDcto the estadoSolicitudAutorizacionDcto to set
	 */
	public void setEstadoSolicitudAutorizacionDcto(
			String estadoSolicitudAutorizacionDcto) {
		this.estadoSolicitudAutorizacionDcto = estadoSolicitudAutorizacionDcto;
	}

	/**
	 * @return the valorAutorizadoDcto
	 */
	public BigDecimal getValorAutorizadoDcto() {
		return valorAutorizadoDcto;
	}

	/**
	 * @return the valorAutorizadoDcto
	 */
	public String getValorAutorizadoDctoFormateado() {
		return UtilidadTexto.formatearValores(valorAutorizadoDcto.doubleValue());
	}
	
	
	/**
	 * @param valorAutorizadoDcto the valorAutorizadoDcto to set
	 */
	public void setValorAutorizadoDcto(BigDecimal valorAutorizadoDcto) {
		this.valorAutorizadoDcto = valorAutorizadoDcto;
	}

	/**
	 * @return the codigoPkPresupuesto
	 */
	public BigDecimal getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}

	/**
	 * @param codigoPkPresupuesto the codigoPkPresupuesto to set
	 */
	public void setCodigoPkPresupuesto(BigDecimal codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}

	/**
	 * @return the codigoPkPresuDctoOdo
	 */
	public BigDecimal getCodigoPkPresuDctoOdo() {
		return codigoPkPresuDctoOdo;
	}

	/**
	 * @param codigoPkPresuDctoOdo the codigoPkPresuDctoOdo to set
	 */
	public void setCodigoPkPresuDctoOdo(BigDecimal codigoPkPresuDctoOdo) {
		this.codigoPkPresuDctoOdo = codigoPkPresuDctoOdo;
	}
	
	
	
	
}
