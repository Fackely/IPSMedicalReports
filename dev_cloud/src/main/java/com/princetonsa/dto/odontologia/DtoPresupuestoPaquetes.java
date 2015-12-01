package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 4:35:50 PM
 */
public class DtoPresupuestoPaquetes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8067605500696689635L;

	/**
	 * codigoPk
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 */
	private int detallePaqueteOdontologicoConvenio;
	
	/**
	 * 
	 */
	private BigDecimal presupuesto;
	
	/**
	 * 
	 */
	private DtoInfoFechaUsuario FHU;

	/**
	 * codigo del paquete a mostrar
	 */
	private String codigoPaqueteMostrar;
	
	/**
	 * 
	 */
	private boolean vigente;
	
	/**
	 * Constructor de la clase
	 */
	public DtoPresupuestoPaquetes() {
		this.codigoPk = BigDecimal.ZERO;
		this.detallePaqueteOdontologicoConvenio = 0;
		this.presupuesto = BigDecimal.ZERO;
		this.FHU = new DtoInfoFechaUsuario();
		this.codigoPaqueteMostrar="";
		this.vigente= true;
	}

	/**
	 * Constructor de la clase
	 */
	public DtoPresupuestoPaquetes(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
		this.detallePaqueteOdontologicoConvenio = 0;
		this.presupuesto = BigDecimal.ZERO;
		this.FHU = new DtoInfoFechaUsuario();
		this.codigoPaqueteMostrar="";
		this.vigente= true;
	}

	
	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
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
	 * @return the fHU
	 */
	public DtoInfoFechaUsuario getFHU() {
		return FHU;
	}

	/**
	 * @param fHU the fHU to set
	 */
	public void setFHU(DtoInfoFechaUsuario fHU) {
		FHU = fHU;
	}

	/**
	 * @return the codigoPaqueteMostrar
	 */
	public String getCodigoPaqueteMostrar() {
		return codigoPaqueteMostrar;
	}

	/**
	 * @param codigoPaqueteMostrar the codigoPaqueteMostrar to set
	 */
	public void setCodigoPaqueteMostrar(String codigoPaqueteMostrar) {
		this.codigoPaqueteMostrar = codigoPaqueteMostrar;
	}

	/**
	 * @return the detallePaqueteOdontologicoConvenio
	 */
	public int getDetallePaqueteOdontologicoConvenio() {
		return detallePaqueteOdontologicoConvenio;
	}

	/**
	 * @param detallePaqueteOdontologicoConvenio the detallePaqueteOdontologicoConvenio to set
	 */
	public void setDetallePaqueteOdontologicoConvenio(
			int detallePaqueteOdontologicoConvenio) {
		this.detallePaqueteOdontologicoConvenio = detallePaqueteOdontologicoConvenio;
	}

	/**
	 * @return the vigente
	 */
	public boolean isVigente() {
		return vigente;
	}

	/**
	 * @param vigente the vigente to set
	 */
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	
}