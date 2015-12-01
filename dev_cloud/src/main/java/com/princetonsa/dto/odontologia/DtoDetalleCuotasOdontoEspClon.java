package com.princetonsa.dto.odontologia;



import util.ConstantesBD;

/**
 * 
 * @author eDG
 *
 */
public class DtoDetalleCuotasOdontoEspClon {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int codigoPk;
	private String tipoCuota;
	private int nroCuotas;
	private double porcentaje;
	private double valor;
	
	
	
	
	
	/**
	 * 
	 */
	public DtoDetalleCuotasOdontoEspClon(){
		
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.tipoCuota="";
		this.nroCuotas=ConstantesBD.codigoNuncaValido;
		this.porcentaje=ConstantesBD.codigoNuncaValidoDouble;
		this.valor=ConstantesBD.codigoNuncaValidoDouble;
	}


	
	



	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}





	/**
	 * @return the tipoCuota
	 */
	public String getTipoCuota() {
		return tipoCuota;
	}





	/**
	 * @return the nroCuotas
	 */
	public int getNroCuotas() {
		return nroCuotas;
	}





	/**
	 * @return the porcentaje
	 */
	public double getPorcentaje() {
		return porcentaje;
	}





	/**
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}





	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}





	/**
	 * @param tipoCuota the tipoCuota to set
	 */
	public void setTipoCuota(String tipoCuota) {
		this.tipoCuota = tipoCuota;
	}





	/**
	 * @param nroCuotas the nroCuotas to set
	 */
	public void setNroCuotas(int nroCuotas) {
		this.nroCuotas = nroCuotas;
	}





	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}





	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}





}
