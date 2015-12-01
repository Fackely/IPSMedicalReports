package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetConvPromocionesOdoDao;
import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetConvPromocionesOdo;


/**
 * 
 * @author axioma
 *
 */
public class OracleDetConvPromocionesOdoDao implements DetConvPromocionesOdoDao{

	@Override
	public ArrayList<DtoDetConvPromocionesOdo> cargar(
			DtoDetConvPromocionesOdo dto) {
		return SqlBaseDetConvPromocionesOdo.cargar(dto);
		
	}

	@Override
	public boolean eliminar(DtoDetConvPromocionesOdo dto) {
				return SqlBaseDetConvPromocionesOdo.eliminar(dto);

	}

	@Override
	public double guardar(DtoDetConvPromocionesOdo dto) {
		return SqlBaseDetConvPromocionesOdo.guardar(dto);
	}

	@Override
	public boolean modificar(DtoDetConvPromocionesOdo dto) {
		return SqlBaseDetConvPromocionesOdo.modificar(dto);
	}

}
