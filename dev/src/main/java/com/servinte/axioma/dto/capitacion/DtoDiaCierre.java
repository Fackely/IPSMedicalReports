package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto que contiene el dia en que fue o no ejecutado un cierre
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoDiaCierre implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1369245049360235711L;
	
	/**
	 * Atributo que representa la fecha del cierre
	 */
	private Date fechaCierre;
	
	/**
	 * Attributo que representa si para la fechaCierre se ha ejecutado un cierre
	 */
	private boolean tieneCierre;
	
	/**
	 * Constructor del Dto
	 * @param fechaCierre
	 * @param tieneCierre
	 */
	public DtoDiaCierre(Date fechaCierre, boolean tieneCierre){
		this.fechaCierre=fechaCierre;
		this.tieneCierre=tieneCierre;
	}

	/**
	 * @return the fechaCierre
	 */
	public Date getFechaCierre() {
		return fechaCierre;
	}

	/**
	 * @param fechaCierre the fechaCierre to set
	 */
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	/**
	 * @return the tieneCierre
	 */
	public boolean isTieneCierre() {
		return tieneCierre;
	}

	/**
	 * @param tieneCierre the tieneCierre to set
	 */
	public void setTieneCierre(boolean tieneCierre) {
		this.tieneCierre = tieneCierre;
	}
	

}
