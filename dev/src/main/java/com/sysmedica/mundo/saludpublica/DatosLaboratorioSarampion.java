package com.sysmedica.mundo.saludpublica;

public class DatosLaboratorioSarampion {

	private int codigoFichaSarampion;
	private int codigoLaboratorio;
	
	private String fechaToma;
	private String fechaRecepcion;
	private int muestra;
	private int prueba;
	private int agente;
	private int resultado;
	private String fechaResultado;
	private String valor;
	
	public int getAgente() {
		return agente;
	}
	public void setAgente(int agente) {
		this.agente = agente;
	}
	public int getCodigoFichaSarampion() {
		return codigoFichaSarampion;
	}
	public void setCodigoFichaSarampion(int codigoFichaSarampion) {
		this.codigoFichaSarampion = codigoFichaSarampion;
	}
	public int getCodigoLaboratorio() {
		return codigoLaboratorio;
	}
	public void setCodigoLaboratorio(int codigoLaboratorio) {
		this.codigoLaboratorio = codigoLaboratorio;
	}
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	public String getFechaResultado() {
		return fechaResultado;
	}
	public void setFechaResultado(String fechaResultado) {
		this.fechaResultado = fechaResultado;
	}
	public String getFechaToma() {
		return fechaToma;
	}
	public void setFechaToma(String fechaToma) {
		this.fechaToma = fechaToma;
	}
	public int getMuestra() {
		return muestra;
	}
	public void setMuestra(int muestra) {
		this.muestra = muestra;
	}
	public int getPrueba() {
		return prueba;
	}
	public void setPrueba(int prueba) {
		this.prueba = prueba;
	}
	public int getResultado() {
		return resultado;
	}
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
