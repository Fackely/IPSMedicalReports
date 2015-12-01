/*
 * Dic 15, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.util.ArrayList;

import util.ConstantesBD;

/**
 * Data Transfer Object: Triage
 * @author Sebastián Gómez R.
 *
 */
public class DtoTriage 
{
	private String consecutivoTriage;
	private String consecutivoFechaTriage;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String fechaNacimiento;
	
	private String fechaTriage;
	private String horaTriage;
	private String profesional;
	private String motivoConsulta;
	private String categoriaTriage;
	private String nombreColor;
	private String observaciones;
	
	private ArrayList<DtoSignosVitalesTriage> signosVitales;
	
	
	/**
	 * Método que limpia los datos
	 */
	public void clean()
	{
		this.consecutivoTriage = "";
		this.consecutivoFechaTriage = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.fechaNacimiento = "";
		this.fechaTriage="";
		this.horaTriage="";
		this.profesional="";
		this.motivoConsulta="";
		this.categoriaTriage="";
		this.nombreColor="";
		this.observaciones="";
		this.signosVitales=new ArrayList<DtoTriage.DtoSignosVitalesTriage>();
	}
	
	/**
	 * Constructor
	 */
	public DtoTriage()
	{
		this.clean();
	}

	/**
	 * @return the consecutivoTriage
	 */
	public String getConsecutivoTriage() {
		return consecutivoTriage;
	}

	/**
	 * @param consecutivoTriage the consecutivoTriage to set
	 */
	public void setConsecutivoTriage(String consecutivoTriage) {
		this.consecutivoTriage = consecutivoTriage;
	}

	/**
	 * @return the consecutivoFechaTriage
	 */
	public String getConsecutivoFechaTriage() {
		return consecutivoFechaTriage;
	}

	/**
	 * @param consecutivoFechaTriage the consecutivoFechaTriage to set
	 */
	public void setConsecutivoFechaTriage(String consecutivoFechaTriage) {
		this.consecutivoFechaTriage = consecutivoFechaTriage;
	}

	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}

	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaTriage() {
		return fechaTriage;
	}

	public void setFechaTriage(String fechaTriage) {
		this.fechaTriage = fechaTriage;
	}

	public String getHoraTriage() {
		return horaTriage;
	}

	public void setHoraTriage(String horaTriage) {
		this.horaTriage = horaTriage;
	}

	public String getProfesional() {
		return profesional;
	}

	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	public String getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(String motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public String getCategoriaTriage() {
		return categoriaTriage;
	}

	public void setCategoriaTriage(String categoriaTriage) {
		this.categoriaTriage = categoriaTriage;
	}

	public String getNombreColor() {
		return nombreColor;
	}

	public void setNombreColor(String nombreColor) {
		this.nombreColor = nombreColor;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public class DtoSignosVitalesTriage
	{
		private int codigoSigno;
		private String nombreSigno;
		private String valor;
		private String unidadMedida;
		
		public DtoSignosVitalesTriage()
		{
			this.codigoSigno=ConstantesBD.codigoNuncaValido;
			this.nombreSigno="";
			this.valor="";
			this.unidadMedida="";
		}

		public int getCodigoSigno() {
			return codigoSigno;
		}

		public void setCodigoSigno(int codigoSigno) {
			this.codigoSigno = codigoSigno;
		}

		public String getNombreSigno() {
			return nombreSigno;
		}

		public void setNombreSigno(String nombreSigno) {
			this.nombreSigno = nombreSigno;
		}

		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}

		public String getUnidadMedida() {
			return unidadMedida;
		}

		public void setUnidadMedida(String unidadMedida) {
			this.unidadMedida = unidadMedida;
		}
	}

	public ArrayList<DtoSignosVitalesTriage> getSignosVitales() {
		return signosVitales;
	}

	public void setSignosVitales(ArrayList<DtoSignosVitalesTriage> signosVitales) {
		this.signosVitales = signosVitales;
	}
}
