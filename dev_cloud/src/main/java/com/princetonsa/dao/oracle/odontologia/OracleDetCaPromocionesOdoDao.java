package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetCaPromocionesOdoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetCaPromocionesOdoDao;
import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;
/**
 * 
 * @author axioma
 *
 */
public class OracleDetCaPromocionesOdoDao  implements DetCaPromocionesOdoDao{

	/**
	 * 
	 */
	public ArrayList<DtoDetCaPromocionesOdo> cargar(DtoDetCaPromocionesOdo dto) {
		return SqlBaseDetCaPromocionesOdoDao.cargar(dto);
	}


	/**
	 * 
	 */
	public boolean eliminar(DtoDetCaPromocionesOdo dto) {
		return SqlBaseDetCaPromocionesOdoDao.eliminar(dto);
	}

	/**
	 * 
	 */
	public double guardar(DtoDetCaPromocionesOdo dto) {
			return SqlBaseDetCaPromocionesOdoDao.guardar(dto);
	}

	/**
	 * 
	 */
	public boolean modificar(DtoDetCaPromocionesOdo dto) {
		return SqlBaseDetCaPromocionesOdoDao.modificar(dto);
	}

	@Override
	public ArrayList<DtoDetCaPromocionesOdo> cargarCentroAtencionNUll(
			DtoDetCaPromocionesOdo dto) {
		
		return SqlBaseDetCaPromocionesOdoDao.cargarCentroAtencionNUll(dto);
	}
	
}
