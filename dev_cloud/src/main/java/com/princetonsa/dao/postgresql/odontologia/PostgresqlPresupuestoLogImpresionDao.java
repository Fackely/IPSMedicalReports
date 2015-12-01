/**
 * 
 */
package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;

import com.princetonsa.dao.odontologia.PresupuestoLogImpresionDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoLogImpresionDao;
import com.princetonsa.dto.odontologia.DtoLogImpresionPresupuesto;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:27:12 PM
 */
public class PostgresqlPresupuestoLogImpresionDao implements
		PresupuestoLogImpresionDao {

	@Override
	public BigDecimal insertar(Connection con, DtoLogImpresionPresupuesto dto) {
		return SqlBasePresupuestoLogImpresionDao.insertar(con, dto);
	}

}
