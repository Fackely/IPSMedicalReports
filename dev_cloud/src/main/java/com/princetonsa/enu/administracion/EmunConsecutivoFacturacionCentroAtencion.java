package com.princetonsa.enu.administracion;

/**
 * 
 * @author axioma
 *
 */
public enum EmunConsecutivoFacturacionCentroAtencion implements IEstructuraConsecutivosCentroAtencion { 
	
	Factuacion("Factura", "consecutivo_facturas"), AnulacionFacturacion("Anulacion Factura", "consecutivo_anulacion_factura");
	
	
	
	/**
	 * 
	 */
	private String nombreConsecutivoInterfazGrafica;
	private String nombreConsecutivoBaseDatos;
	
	
	
	/**
	 * 
	 * @param nombreConsecutivoInterfazGrafica
	 * @param nombreConsecutivoBaseDatos
	 */
	EmunConsecutivoFacturacionCentroAtencion(String nombreConsecutivoInterfazGrafica, String nombreConsecutivoBaseDatos)
	{
		this.nombreConsecutivoInterfazGrafica=nombreConsecutivoInterfazGrafica;
		this.nombreConsecutivoBaseDatos=nombreConsecutivoBaseDatos;
	}

	
	
	@Override
	public String getNombreConsecutivoBaseDatos() {
		
		return this.nombreConsecutivoBaseDatos;
	}

	@Override
	public String getNombreConsecutivoInterfazGrafica() {
		
		return this.nombreConsecutivoInterfazGrafica;
	}
	
}
