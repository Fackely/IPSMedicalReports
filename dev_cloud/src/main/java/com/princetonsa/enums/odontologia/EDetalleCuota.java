package com.princetonsa.enums.odontologia;


/**
 * ENUMERACION DETALLE CUOTA
 * NOTA. 
 * 
 * En la clase DtoPresupuestoDetalleCuotasEspecialidad tambien existe una enumeracion para detalleCuota.
 *  
 * 
 * @author Edgar Carvajal
 *
 */
public enum EDetalleCuota {
	
	
	/**
	 * 	ENUMERACIONES 
	 */
	INIC("Inicial"), PORCI("Por Cita"), ADIC("Adicional");
	
	
	
	
	/**
	 * NOMBRE DE LA ENUMERACION
	 */
	private String nombreDetalleCuota;
	
	
	/**
	 * 
	 * @param nombreDetalleCuota
	 */
	EDetalleCuota(String nombreDetalleCuota)
	{
		this.nombreDetalleCuota=nombreDetalleCuota;
	}
	

	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreDetalleCuota() {
		return nombreDetalleCuota;
	}
	
	

}
