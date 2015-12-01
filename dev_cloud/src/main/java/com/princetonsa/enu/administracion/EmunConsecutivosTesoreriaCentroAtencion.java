package com.princetonsa.enu.administracion;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public enum EmunConsecutivosTesoreriaCentroAtencion implements IEstructuraConsecutivosCentroAtencion {

	AnulacionReciboCaja("Anulacion Recibo de Caja", ConstantesBD.nombreConsecutivoAnulacionRecibosCaja),
	ReciboCaja("Recibo Caja", ConstantesBD.nombreConsecutivoReciboCaja),
	NotasDebitoPacientes("Notas Débito Pacientes", ConstantesBD.nombreConsecutivoNotasDebitoPacientes),
	NotasCreditoPacientes("Notas Crédito Pacientes", ConstantesBD.nombreConsecutivoNotasCreditoPacientes);
	
	
	private String nombreConsecutivoInterfazGrafica;
	private String nombreConsecutivoBaseDatos;
	/**
	 * 
	 * @param nombreConsecutivoInterfazGrafica
	 * @param nombreConsecutivoInterfazGrafica
	 */
	
	EmunConsecutivosTesoreriaCentroAtencion(String nombreConsecutivoInterfazGrafica, String nombreConsecutivoBaseDatos)
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
