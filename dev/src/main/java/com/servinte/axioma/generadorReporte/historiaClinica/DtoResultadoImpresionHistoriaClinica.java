/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import util.ConstantesBD;

/**
 * @author JorOsoVe
 *
 */
public class DtoResultadoImpresionHistoriaClinica 
{
	

	
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String fechaBD;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int codigoTipoEvolucion;
	
	private int codigoCuenta;
	
	/**
	 * 
	 */
	private Object evolucion;
	
	
	/**
	 * 
	 */
	private DtoHojaQuirurgicaAnestesia hojaQuirurgica;
	
	/**
	 * 
	 */
	public DtoResultadoImpresionHistoriaClinica() 
	{
		this.fecha="";
		this.fechaBD="";
		this.hora="";
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.codigoTipoEvolucion=ConstantesBD.codigoNuncaValido;
		this.evolucion=null;
		this.hojaQuirurgica = new DtoHojaQuirurgicaAnestesia();	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFechaBD() {
		return fechaBD;
	}

	public void setFechaBD(String fechaBD) {
		this.fechaBD = fechaBD;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoTipoEvolucion() {
		return codigoTipoEvolucion;
	}

	public void setCodigoTipoEvolucion(int codigoTipoEvolucion) {
		this.codigoTipoEvolucion = codigoTipoEvolucion;
	}

	public Object getEvolucion() {
		return evolucion;
	}

	public void setEvolucion(Object evolucion) {
		this.evolucion = evolucion;
	}

	/**
	 * @return the hojaQuirurgica
	 */
	public DtoHojaQuirurgicaAnestesia getHojaQuirurgica() {
		return hojaQuirurgica;
	}

	/**
	 * @param hojaQuirurgica the hojaQuirurgica to set
	 */
	public void setHojaQuirurgica(DtoHojaQuirurgicaAnestesia hojaQuirurgica) {
		this.hojaQuirurgica = hojaQuirurgica;
	}

	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	
	public int getCodigoCuenta() {
		return codigoCuenta;
	}

}
