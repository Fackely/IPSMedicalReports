package util;

import java.io.Serializable;
import java.math.BigDecimal;

public class InfoPacienteBonoPresupuesto implements Serializable{
	
	
	/**
	 * 
	 */
	private double codigoPkBono;
	private BigDecimal bono;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String fechaPresupuesto;
	private double codigoPresupuesto;
	private String fechaIngreso;
	private int numeroIngreso;
	private int codigoConvenio;
	private double codigoPrograma;
	private int codigoServcio;
	
	
	/**
	 * 
	 */
	public InfoPacienteBonoPresupuesto()
	{
		this.codigoPkBono=ConstantesBD.codigoNuncaValidoDouble;
		this.bono= BigDecimal.ZERO;
		
		
		
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.fechaPresupuesto="";
		this.codigoPresupuesto= ConstantesBD.codigoNuncaValidoDouble;
		this.fechaIngreso= "";
		this.numeroIngreso=ConstantesBD.codigoNuncaValido;
		this.setCodigoConvenio(ConstantesBD.codigoNuncaValido);
		this.setCodigoPrograma(ConstantesBD.codigoNuncaValidoDouble);
		this.setCodigoServcio(ConstantesBD.codigoNuncaValido);
	}
	
	

	public Double getCodigoPkBono() {
		return codigoPkBono;
	}

	public void setCodigoPkBono(Double codigoPkBono) {
		this.codigoPkBono = codigoPkBono;
	}

	public BigDecimal getBono() {
		return bono;
	}

	public void setBono(BigDecimal bono) {
		this.bono = bono;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
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



	public String getFechaPresupuesto() {
		return fechaPresupuesto;
	}



	public void setFechaPresupuesto(String fechaPresupuesto) {
		this.fechaPresupuesto = fechaPresupuesto;
	}



	public double getCodigoPresupuesto() {
		return codigoPresupuesto;
	}



	public void setCodigoPresupuesto(double codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}



	public String getFechaIngreso() {
		return fechaIngreso;
	}



	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}



	public int getNumeroIngreso() {
		return numeroIngreso;
	}



	public void setNumeroIngreso(int numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}



	public void setCodigoPkBono(double codigoPkBono) {
		this.codigoPkBono = codigoPkBono;
	}



	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}



	public int getCodigoConvenio() {
		return codigoConvenio;
	}



	public void setCodigoPrograma(Double codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}



	public Double getCodigoPrograma() {
		return codigoPrograma;
	}



	public void setCodigoServcio(int codigoServcio) {
		this.codigoServcio = codigoServcio;
	}



	public int getCodigoServcio() {
		return codigoServcio;
	}
	
	

}
