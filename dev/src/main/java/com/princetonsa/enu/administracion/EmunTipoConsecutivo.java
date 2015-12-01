package com.princetonsa.enu.administracion;

public enum EmunTipoConsecutivo {
	
	
	Facturacion("Facturacion"),Tesoreria("Tesoreria"), FacturasVarias("FacturasVarias");
	
	
	String nombreTipo;
	
	EmunTipoConsecutivo(String nombretipo)
	{
			this.nombreTipo=nombretipo;
	}
	
	
	/**
	 *  getNombreTipo
	 * @return
	 */
	String getNombreTipo(){ return this.nombreTipo; }
	

}
