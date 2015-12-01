package com.princetonsa.dao.postgresql.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.facturacion.ControlAnticipoContratoDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseControlAnticiposContratoDao;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.servinte.axioma.fwk.exception.BDException;

public class PostgesqlControlAnticipoContratoDao implements ControlAnticipoContratoDao {

	@Override
	public ArrayList<DtoControlAnticiposContrato> cargar(Connection con,
			DtoControlAnticiposContrato dtoWhere) throws BDException{
		
		return SqlBaseControlAnticiposContratoDao.cargar(con, dtoWhere);
	}

	@Override
	public boolean eliminar(DtoControlAnticiposContrato dtoWhere) {
		
		 return SqlBaseControlAnticiposContratoDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoControlAnticiposContrato dto, Connection con) {
		
		 return SqlBaseControlAnticiposContratoDao.guardar(dto, con);
	}

	@Override
	public boolean modificar(DtoControlAnticiposContrato dtoNuevo,
			DtoControlAnticiposContrato dtoWhere) {
		
		return SqlBaseControlAnticiposContratoDao.modificar(dtoNuevo, dtoWhere);
	}

	@Override
	public boolean modificarValorAnticipoReservadoPresupuesto(Connection con,
			int contrato, BigDecimal valorAnticipoPresupuesto) {
		return SqlBaseControlAnticiposContratoDao.modificarValorAnticipoReservadoPresupuesto(con, contrato, valorAnticipoPresupuesto);
	}

	@Override
	public boolean modificarValorAnticipoUtilizadoFactura(Connection con,
			int contrato, BigDecimal valorAnticipo) {
		return SqlBaseControlAnticiposContratoDao.modificarValorAnticipoUtilizadoFactura(con, contrato, valorAnticipo);
	}
}
