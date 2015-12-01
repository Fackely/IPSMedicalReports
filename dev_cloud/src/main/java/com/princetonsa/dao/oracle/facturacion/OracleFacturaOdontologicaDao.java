package com.princetonsa.dao.oracle.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dao.facturacion.FacturaOdontologicaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseFacturaOdontologicaDao;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Imp Oracle de la fatura odontologica
 * @author axioma
 *
 */
public class OracleFacturaOdontologicaDao implements FacturaOdontologicaDao 
{
	@Override
	public ArrayList<DtoResponsableFacturaOdontologica> cargarResponsables(
			BigDecimal cuenta, int centroAtencion, int institucion, ArrayList<BigDecimal> filtroCargosFacturaAutomatica) throws BDException{
		return SqlBaseFacturaOdontologicaDao.cargarResponsables(cuenta, centroAtencion, institucion, filtroCargosFacturaAutomatica);
	}
	
}
