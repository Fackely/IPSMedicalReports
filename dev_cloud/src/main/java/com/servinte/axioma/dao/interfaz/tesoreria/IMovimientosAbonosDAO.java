package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;

/**
 * Interfaz donde se define el comportamiento del DAO.
 * 
 * @author Luis Fernando Hincapié Ospina
 * @see com.servinte.axioma.dao.impl.tesoreria.MovimientosAbonosHibernateDAO
 * @since 08/03/2011
 */
public interface IMovimientosAbonosDAO {

	/**
	 * Este Método se encarga de obtener los movimientos de
	 * abono generados en una institución específica.
	 * @param codigoInstitucion
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerMovimientosAbonosPorInstitucion(int codigoInstitucion);

	/**
	 * Valida si tienes movimientos abonos.
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
