/**
 * 
 */
package com.servinte.axioma.dto.administracion;

/**
 * @author armando
 *
 */
public class DtoDepartamentos 
{
	/**
	 * 
	 */
	private String codigoDepartamento;
	
	public DtoDepartamentos()
	{
		super();
	}
	
	/**
	 * 
	 * @param codigoDepartamento
	 * @param descripcionDepartamento
	 * @param pais
	 */
	public DtoDepartamentos(String codigoDepartamento,
			String descripcionDepartamento, DtoPaises pais) {
		super();
		this.codigoDepartamento = codigoDepartamento;
		this.descripcionDepartamento = descripcionDepartamento;
		this.pais = pais;
	}

	/**
	 * 
	 */
	private String descripcionDepartamento;
	
	/**
	 * 
	 */
	private DtoPaises pais;

	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getDescripcionDepartamento() {
		return descripcionDepartamento;
	}

	public void setDescripcionDepartamento(String descripcionDepartamento) {
		this.descripcionDepartamento = descripcionDepartamento;
	}

	public DtoPaises getPais() {
		return pais;
	}

	public void setPais(DtoPaises pais) {
		this.pais = pais;
	}

}
