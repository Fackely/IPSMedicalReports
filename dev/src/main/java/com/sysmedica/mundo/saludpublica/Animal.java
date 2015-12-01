/*
 * Creado en 02-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;


/**
 * @author santiago
 *
 */
public class Animal {
    
    private int codigoFichaRabia;
    private int codigoAnimal;
    
    private int especie;
    private String fechaInicioSintomas;
    private String fechaMuerte;
    private String fechaTomaMuestra;
    private int fuenteInformacionLaboratorio;
    private boolean vacunado;
    private String fechaUltimaDosisAnimal;
    
    private String nombrePropietario;
    private String direccionPropietario;
    private int estadoMomentoAgresion;
    private int ubicacionAnimal;
    private int numeroDiasObserva;
    private int lugarObservacion;
    private int estadoAnimalObserva;
    private int confirmacionDiagnosticaAnimal;
   
    
    /**
     * Metodo que resetea los atributos
     *
     */
    public void reset() {
    	
    	especie = 0;
    	fechaInicioSintomas = "";
    	fechaMuerte = "";
    	fechaTomaMuestra = "";
    	fuenteInformacionLaboratorio = 0;
    	vacunado = false;
    	fechaUltimaDosisAnimal = "";
    	
    	nombrePropietario = "";
        direccionPropietario = "";
        estadoMomentoAgresion = 0;
        ubicacionAnimal = 0;
        numeroDiasObserva = 0;
        lugarObservacion = 0;
        estadoAnimalObserva = 0;
        confirmacionDiagnosticaAnimal = 0;
    }


	public int getCodigoAnimal() {
		return codigoAnimal;
	}


	public void setCodigoAnimal(int codigoAnimal) {
		this.codigoAnimal = codigoAnimal;
	}


	public int getCodigoFichaRabia() {
		return codigoFichaRabia;
	}


	public void setCodigoFichaRabia(int codigoFichaRabia) {
		this.codigoFichaRabia = codigoFichaRabia;
	}


	public int getEspecie() {
		return especie;
	}


	public void setEspecie(int especie) {
		this.especie = especie;
	}


	public String getFechaInicioSintomas() {
		return fechaInicioSintomas;
	}


	public void setFechaInicioSintomas(String fechaInicioSintomas) {
		this.fechaInicioSintomas = fechaInicioSintomas;
	}


	public String getFechaMuerte() {
		return fechaMuerte;
	}


	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}


	public String getFechaTomaMuestra() {
		return fechaTomaMuestra;
	}


	public void setFechaTomaMuestra(String fechaTomaMuestra) {
		this.fechaTomaMuestra = fechaTomaMuestra;
	}


	public String getFechaUltimaDosisAnimal() {
		return fechaUltimaDosisAnimal;
	}


	public void setFechaUltimaDosisAnimal(String fechaUltimaDosisAnimal) {
		this.fechaUltimaDosisAnimal = fechaUltimaDosisAnimal;
	}


	public int getFuenteInformacionLaboratorio() {
		return fuenteInformacionLaboratorio;
	}


	public void setFuenteInformacionLaboratorio(int fuenteInformacionLaboratorio) {
		this.fuenteInformacionLaboratorio = fuenteInformacionLaboratorio;
	}


	public boolean getVacunado() {
		return vacunado;
	}


	public void setVacunado(boolean vacunado) {
		this.vacunado = vacunado;
	}


	public int getConfirmacionDiagnosticaAnimal() {
		return confirmacionDiagnosticaAnimal;
	}


	public void setConfirmacionDiagnosticaAnimal(int confirmacionDiagnosticaAnimal) {
		this.confirmacionDiagnosticaAnimal = confirmacionDiagnosticaAnimal;
	}


	public String getDireccionPropietario() {
		return direccionPropietario;
	}


	public void setDireccionPropietario(String direccionPropietario) {
		this.direccionPropietario = direccionPropietario;
	}


	public int getEstadoAnimalObserva() {
		return estadoAnimalObserva;
	}


	public void setEstadoAnimalObserva(int estadoAnimalObserva) {
		this.estadoAnimalObserva = estadoAnimalObserva;
	}


	public int getEstadoMomentoAgresion() {
		return estadoMomentoAgresion;
	}


	public void setEstadoMomentoAgresion(int estadoMomentoAgresion) {
		this.estadoMomentoAgresion = estadoMomentoAgresion;
	}


	public int getLugarObservacion() {
		return lugarObservacion;
	}


	public void setLugarObservacion(int lugarObservacion) {
		this.lugarObservacion = lugarObservacion;
	}


	public String getNombrePropietario() {
		return nombrePropietario;
	}


	public void setNombrePropietario(String nombrePropietario) {
		this.nombrePropietario = nombrePropietario;
	}


	public int getNumeroDiasObserva() {
		return numeroDiasObserva;
	}


	public void setNumeroDiasObserva(int numeroDiasObserva) {
		this.numeroDiasObserva = numeroDiasObserva;
	}


	public int getUbicacionAnimal() {
		return ubicacionAnimal;
	}


	public void setUbicacionAnimal(int ubicacionAnimal) {
		this.ubicacionAnimal = ubicacionAnimal;
	}
    
}
