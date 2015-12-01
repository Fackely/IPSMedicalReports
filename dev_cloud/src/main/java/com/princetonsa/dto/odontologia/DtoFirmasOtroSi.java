package com.princetonsa.dto.odontologia;

import java.io.Serializable;

public class DtoFirmasOtroSi implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String labelFirma;
	private String pathFirma;
	
	public DtoFirmasOtroSi(String labelFirma, String pathFirma) {
		super();
		this.labelFirma = labelFirma;
		this.pathFirma = pathFirma;
	}
	/**
	 * @return the labelFirma
	 */
	public String getLabelFirma() {
		return labelFirma;
	}
	/**
	 * @param labelFirma the labelFirma to set
	 */
	public void setLabelFirma(String labelFirma) {
		this.labelFirma = labelFirma;
	}
	/**
	 * @return the pathFirma
	 */
	public String getPathFirma() {
		return pathFirma;
	}
	/**
	 * @param pathFirma the pathFirma to set
	 */
	public void setPathFirma(String pathFirma) {
		this.pathFirma = pathFirma;
	}
	
	

}
