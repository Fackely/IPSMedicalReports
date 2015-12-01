/*
 * Creado en 04-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

/**
 * @author santiago
 *
 */
public class TratamientoAntirrabico {
    
    private int codigoFichaRabia;
    private int codigoTratamientoAntirrabico;
    
    private boolean lavadoHerida;
    private boolean suturaHerida;
    private boolean aplicacionSuero;
    private String fechaAplicacionSuero;
    private int tipoSueroTratamiento;
    private int cantidadSueroGlutea;
    private int cantidadSueroHerida;
    private String numeroLote;
    private String laboratorioProductor;
    private boolean aplicarVacuna;
    private int numeroDosisTratamiento;
    private int tipoVacunaTratamiento;
    private String fechaVacunaDosis1;
    private String fechaVacunaDosis2;
    private String fechaVacunaDosis3;
    private String fechaVacunaDosis4;
    private String fechaVacunaDosis5;
    private boolean suspensionTratamiento;
    private int razonSuspension;
    private String fechaTomaMuestraMuerte;
    private boolean confirmacionDiagnostica;
    private int pruebasLaboratorio;
    private int reaccionesVacunaSuero;
    private int evolucionPaciente;
    
    /**
     * Metodo que resetea los atributos
     *
     */
    public void reset() {
        
    	lavadoHerida = false;
        suturaHerida = false;
        aplicacionSuero = false;
        fechaAplicacionSuero = "";
        tipoSueroTratamiento = 0;
        cantidadSueroGlutea = 0;
        cantidadSueroHerida = 0;
        numeroLote = "";
        laboratorioProductor = "";
        aplicarVacuna = false;
        numeroDosisTratamiento = 0;
        tipoVacunaTratamiento = 0;
        fechaVacunaDosis1 = "";
        fechaVacunaDosis2 = "";
        fechaVacunaDosis3 = "";
        fechaVacunaDosis4 = "";
        fechaVacunaDosis5 = "";
        suspensionTratamiento = false;
        razonSuspension = 0;
        fechaTomaMuestraMuerte = "";
        confirmacionDiagnostica = false;
        pruebasLaboratorio = 0;
        reaccionesVacunaSuero = 0;
        evolucionPaciente = 0;
    }

	public boolean isAplicacionSuero() {
		return aplicacionSuero;
	}

	public void setAplicacionSuero(boolean aplicacionSuero) {
		this.aplicacionSuero = aplicacionSuero;
	}

	public boolean isAplicarVacuna() {
		return aplicarVacuna;
	}

	public void setAplicarVacuna(boolean aplicarVacuna) {
		this.aplicarVacuna = aplicarVacuna;
	}

	public int getCantidadSueroGlutea() {
		return cantidadSueroGlutea;
	}

	public void setCantidadSueroGlutea(int cantidadSueroGlutea) {
		this.cantidadSueroGlutea = cantidadSueroGlutea;
	}

	public int getCantidadSueroHerida() {
		return cantidadSueroHerida;
	}

	public void setCantidadSueroHerida(int cantidadSueroHerida) {
		this.cantidadSueroHerida = cantidadSueroHerida;
	}

	public int getCodigoFichaRabia() {
		return codigoFichaRabia;
	}

	public void setCodigoFichaRabia(int codigoFichaRabia) {
		this.codigoFichaRabia = codigoFichaRabia;
	}

	public int getCodigoTratamientoAntirrabico() {
		return codigoTratamientoAntirrabico;
	}

	public void setCodigoTratamientoAntirrabico(int codigoTratamientoAntirrabico) {
		this.codigoTratamientoAntirrabico = codigoTratamientoAntirrabico;
	}

	public boolean isConfirmacionDiagnostica() {
		return confirmacionDiagnostica;
	}

	public void setConfirmacionDiagnostica(boolean confirmacionDiagnostica) {
		this.confirmacionDiagnostica = confirmacionDiagnostica;
	}

	public String getFechaAplicacionSuero() {
		return fechaAplicacionSuero;
	}

	public void setFechaAplicacionSuero(String fechaAplicacionSuero) {
		this.fechaAplicacionSuero = fechaAplicacionSuero;
	}

	public String getFechaTomaMuestraMuerte() {
		return fechaTomaMuestraMuerte;
	}

	public void setFechaTomaMuestraMuerte(String fechaTomaMuestraMuerte) {
		this.fechaTomaMuestraMuerte = fechaTomaMuestraMuerte;
	}

	public String getFechaVacunaDosis1() {
		return fechaVacunaDosis1;
	}

	public void setFechaVacunaDosis1(String fechaVacunaDosis1) {
		this.fechaVacunaDosis1 = fechaVacunaDosis1;
	}

	public String getFechaVacunaDosis2() {
		return fechaVacunaDosis2;
	}

	public void setFechaVacunaDosis2(String fechaVacunaDosis2) {
		this.fechaVacunaDosis2 = fechaVacunaDosis2;
	}

	public String getFechaVacunaDosis3() {
		return fechaVacunaDosis3;
	}

	public void setFechaVacunaDosis3(String fechaVacunaDosis3) {
		this.fechaVacunaDosis3 = fechaVacunaDosis3;
	}

	public String getFechaVacunaDosis4() {
		return fechaVacunaDosis4;
	}

	public void setFechaVacunaDosis4(String fechaVacunaDosis4) {
		this.fechaVacunaDosis4 = fechaVacunaDosis4;
	}

	public String getFechaVacunaDosis5() {
		return fechaVacunaDosis5;
	}

	public void setFechaVacunaDosis5(String fechaVacunaDosis5) {
		this.fechaVacunaDosis5 = fechaVacunaDosis5;
	}

	public String getLaboratorioProductor() {
		return laboratorioProductor;
	}

	public void setLaboratorioProductor(String laboratorioProductor) {
		this.laboratorioProductor = laboratorioProductor;
	}

	public boolean isLavadoHerida() {
		return lavadoHerida;
	}

	public void setLavadoHerida(boolean lavadoHerida) {
		this.lavadoHerida = lavadoHerida;
	}

	public int getNumeroDosisTratamiento() {
		return numeroDosisTratamiento;
	}

	public void setNumeroDosisTratamiento(int numeroDosisTratamiento) {
		this.numeroDosisTratamiento = numeroDosisTratamiento;
	}

	public String getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}

	public int getPruebasLaboratorio() {
		return pruebasLaboratorio;
	}

	public void setPruebasLaboratorio(int pruebasLaboratorio) {
		this.pruebasLaboratorio = pruebasLaboratorio;
	}

	public int getRazonSuspension() {
		return razonSuspension;
	}

	public void setRazonSuspension(int razonSuspension) {
		this.razonSuspension = razonSuspension;
	}

	public boolean isSuspensionTratamiento() {
		return suspensionTratamiento;
	}

	public void setSuspensionTratamiento(boolean suspensionTratamiento) {
		this.suspensionTratamiento = suspensionTratamiento;
	}

	public boolean isSuturaHerida() {
		return suturaHerida;
	}

	public void setSuturaHerida(boolean suturaHerida) {
		this.suturaHerida = suturaHerida;
	}

	public int getTipoSueroTratamiento() {
		return tipoSueroTratamiento;
	}

	public void setTipoSueroTratamiento(int tipoSueroTratamiento) {
		this.tipoSueroTratamiento = tipoSueroTratamiento;
	}

	public int getTipoVacunaTratamiento() {
		return tipoVacunaTratamiento;
	}

	public void setTipoVacunaTratamiento(int tipoVacunaTratamiento) {
		this.tipoVacunaTratamiento = tipoVacunaTratamiento;
	}

	public int getEvolucionPaciente() {
		return evolucionPaciente;
	}

	public void setEvolucionPaciente(int evolucionPaciente) {
		this.evolucionPaciente = evolucionPaciente;
	}

	public int getReaccionesVacunaSuero() {
		return reaccionesVacunaSuero;
	}

	public void setReaccionesVacunaSuero(int reaccionesVacunaSuero) {
		this.reaccionesVacunaSuero = reaccionesVacunaSuero;
	}
}
