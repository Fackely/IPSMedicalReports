package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;

/**
 * Define la lógica de negocio relacionada con los movimientos abonos de
 * paciente.
 * 
 * @author Luis Fernando Hincapié Ospina
 * @see com.servinte.axioma.mundo.impl.tesoreria.MovimientosAbonosMundo
 * @since 08/03/2011
 */
public interface IMovimientosAbonosMundo {

	/**
	 * Este Método se encarga de obtener los movimientos de
	 * abono generados en una institución específica.
	 * @param codigoInstitucion
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerMovimientosAbonosPorInstitucion(int codigoInstitucion);

		
	
	/**
	 * Valida si tiene movimientos abonos.
	 * 
	 * @param 
	 * @return S/N
	 */
	public String validarSiTieneMovimientosAbonos(int codigoPaciente);
	
	/**
	 * Este Método se encarga de obtener el consolidado de los tipos de movimientos de abonos 
	 * de un paciente.
	 * @param codigoPaciente
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerConsolidadoPorTipoMovimiento(int codigoPaciente);
}
