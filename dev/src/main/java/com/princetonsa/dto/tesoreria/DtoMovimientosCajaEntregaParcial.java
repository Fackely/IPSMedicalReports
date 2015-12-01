package com.princetonsa.dto.tesoreria;

import com.servinte.axioma.orm.MovimientosCaja;

public class DtoMovimientosCajaEntregaParcial {
	

	private MovimientosCaja movimientosCaja; 
	private Integer codigoCaja;
	private String cajeroSort;
	private String tipoSort;
	
	
	
	public DtoMovimientosCajaEntregaParcial() {
		this.movimientosCaja=new MovimientosCaja();
		this.codigoCaja= new Integer(0);
		this.cajeroSort ="";
		this.tipoSort="";
	}



	/**
	 * @return the movimientosCaja
	 */
	public MovimientosCaja getMovimientosCaja() {
		return movimientosCaja;
	}



	/**
	 * @param movimientosCaja the movimientosCaja to set
	 */
	public void setMovimientosCaja(MovimientosCaja movimientosCaja) {
		this.movimientosCaja = movimientosCaja;
	}



	/**
	 * @return the codigoCaja
	 */
	public Integer getCodigoCaja() {
		return codigoCaja;
	}



	/**
	 * @param codigoCaja the codigoCaja to set
	 */
	public void setCodigoCaja(Integer codigoCaja) {
		this.codigoCaja = codigoCaja;
	}



	/**
	 * @return the cajeroSort
	 */
	public String getCajeroSort() {
		return cajeroSort;
	}



	/**
	 * @param cajeroSort the cajeroSort to set
	 */
	public void setCajeroSort(String cajeroSort) {
		this.cajeroSort = cajeroSort;
	}



	/**
	 * @return the tipoSort
	 */
	public String getTipoSort() {
		return tipoSort;
	}



	/**
	 * @param tipoSort the tipoSort to set
	 */
	public void setTipoSort(String tipoSort) {
		this.tipoSort = tipoSort;
	}




	
	
	

}
