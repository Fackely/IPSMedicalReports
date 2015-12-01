package com.princetonsa.dao.postgresql.facturacion;

import java.util.ArrayList;

import com.princetonsa.dao.facturacion.LogControlAnticipoContratoDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseLogControlAnticipoContratoDao;
import com.princetonsa.dto.facturacion.DtoLogControlAnticipoContrato;

public class PostgresqlLogControlAnticipoContratoDao implements LogControlAnticipoContratoDao {

	@Override
	public double guardar(DtoLogControlAnticipoContrato dto) {
		
		return SqlBaseLogControlAnticipoContratoDao.guardar(dto);
	}
    
	@Override
	public ArrayList<DtoLogControlAnticipoContrato> cargar(
			DtoLogControlAnticipoContrato dtoWhere) {
		
		return SqlBaseLogControlAnticipoContratoDao.cargar(dtoWhere);
	}
	
}
