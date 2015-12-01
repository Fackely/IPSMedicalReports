package com.princetonsa.enu.administracion;

import util.ConstantesBD;

/**
 * Enumeración con los consecutivos disponibles para parametrizar
 * por centro de Atención relacionados con la Factura Varia
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public enum EmunConsecutivoFacturasVariasCentroAtencion implements IEstructuraConsecutivosCentroAtencion { 
	
	
	FacturasVarias("Facturas Varias", ConstantesBD.nombreConsecutivoFacturasVarias), AnulacionFacturasVarias("Anulacion Facturas Varias", ConstantesBD.nombreConsecutivoAnulacionFacturaVaria);

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
	EmunConsecutivoFacturasVariasCentroAtencion(String nombreConsecutivoInterfazGrafica, String nombreConsecutivoBaseDatos)
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
