package com.princetonsa.dao.oracle.capitacion;

import com.princetonsa.dao.capitacion.ProcesoNivelAutorizacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseProcesoNivelAutorizacionDao;
import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;

public class OracleProcesoNivelAutorizacionDao implements
		ProcesoNivelAutorizacionDao {

	@Override
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacion(
			DtoParametrosValidacionNivelAutorizacion dto) {
		return SqlBaseProcesoNivelAutorizacionDao.validarNivelAutorizacion(dto);
	}

}
