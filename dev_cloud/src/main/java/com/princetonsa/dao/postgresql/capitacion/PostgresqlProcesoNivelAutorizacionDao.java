/**
 * 
 */
package com.princetonsa.dao.postgresql.capitacion;

import com.princetonsa.dao.capitacion.ProcesoNivelAutorizacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseProcesoNivelAutorizacionDao;
import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;

/**
 * @author armando
 *
 */
public class PostgresqlProcesoNivelAutorizacionDao implements
		ProcesoNivelAutorizacionDao {
	
	@Override
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacion(
			DtoParametrosValidacionNivelAutorizacion dto) {
		return SqlBaseProcesoNivelAutorizacionDao.validarNivelAutorizacion(dto);
	}
	

}
