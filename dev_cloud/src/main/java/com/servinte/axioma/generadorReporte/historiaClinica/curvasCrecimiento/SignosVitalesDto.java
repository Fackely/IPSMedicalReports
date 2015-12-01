package com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento;

/**
 * Dto Signos Vitales en la Curva de Crecimiento
 * @author hermorhu
 * @created 26-Oct-2012 
 */
public class SignosVitalesDto{

	private String fecha;
	private String edad;
	private String peso;
	private String talla;
	private String imc;
	private String perimetroCefalico;
	
	/**
	 * 
	 */
	public SignosVitalesDto() {
		super();
	}

	/**
	 * @param fecha
	 * @param edad
	 * @param peso
	 * @param talla
	 * @param imc
	 * @param perimetroCefalico
	 */
	public SignosVitalesDto(String fecha, String edad, String peso,
			String talla, String imc, String perimetroCefalico) {
		super();
		this.fecha = fecha;
		this.edad = edad;
		this.peso = peso;
		this.talla = talla;
		this.imc = imc;
		this.perimetroCefalico = perimetroCefalico;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}

	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * @return the peso
	 */
	public String getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(String peso) {
		this.peso = peso;
	}

	/**
	 * @return the talla
	 */
	public String getTalla() {
		return talla;
	}

	/**
	 * @param talla the talla to set
	 */
	public void setTalla(String talla) {
		this.talla = talla;
	}

	/**
	 * @return the imc
	 */
	public String getImc() {
		return imc;
	}

	/**
	 * @param imc the imc to set
	 */
	public void setImc(String imc) {
		this.imc = imc;
	}

	/**
	 * @return the perimetroCefalico
	 */
	public String getPerimetroCefalico() {
		return perimetroCefalico;
	}

	/**
	 * @param perimetroCefalico the perimetroCefalico to set
	 */
	public void setPerimetroCefalico(String perimetroCefalico) {
		this.perimetroCefalico = perimetroCefalico;
	}
	
}
