package com.servinte.axioma.dto.tesoreria;

import java.util.ArrayList;


public class DtoConceptoNotasPacientes {
	
	private long codigoPk;
	private int numero;
	private String codigo;
	private String descripcion;
	private String naturaleza;
	private String activo;
	private String relacionNaturalezaNotaPaciente;
	private ArrayList<DtoConcNotaPacCuentaCont> listaConceptoNotaPacCuentaCont;
	
	public DtoConceptoNotasPacientes(){};
	
	public DtoConceptoNotasPacientes(long codigoPk, int numero, String codigo,
			String descripcion, String naturaleza, String activo, boolean permiteEliminar,
			ArrayList<DtoConcNotaPacCuentaCont> listaConceptoNotaPacCuentaCont) {
		super();
		this.codigoPk = codigoPk;
		this.numero = numero;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.naturaleza = naturaleza;
		this.activo = activo;
		this.listaConceptoNotaPacCuentaCont = listaConceptoNotaPacCuentaCont;
	}

	public long getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}


	/**
	 * @param relacionNaturalezaNotaPaciente the relacionNaturalezaNotaPaciente to set
	 */
	public void setRelacionNaturalezaNotaPaciente(
			String relacionNaturalezaNotaPaciente) {
		this.relacionNaturalezaNotaPaciente = relacionNaturalezaNotaPaciente;
	}

	/**
	 * @return the relacionNaturalezaNotaPaciente
	 */
	public String getRelacionNaturalezaNotaPaciente() {
		return relacionNaturalezaNotaPaciente;
	}

	public ArrayList<DtoConcNotaPacCuentaCont> getListaConceptoNotaPacCuentaCont() {
		return listaConceptoNotaPacCuentaCont;
	}

	public void setListaConceptoNotaPacCuentaCont(
			ArrayList<DtoConcNotaPacCuentaCont> listaConceptoNotaPacCuentaCont) {
		this.listaConceptoNotaPacCuentaCont = listaConceptoNotaPacCuentaCont;
	}

	@Override
	public String toString() {
		return "DtoConceptoNotasPacientes [codigoPk=" + codigoPk + ", codigo="
				+ codigo + ", descripcion=" + descripcion + ", naturaleza="
				+ naturaleza + ", activo=" + activo + "]";
	}
	
}
