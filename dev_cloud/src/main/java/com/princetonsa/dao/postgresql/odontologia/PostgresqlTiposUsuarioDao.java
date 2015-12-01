package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.TiposDeUsuarioDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseTiposUsuarioDao;
import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;

public class PostgresqlTiposUsuarioDao implements TiposDeUsuarioDao{

	@Override
	public ArrayList<DtoTiposDeUsuario> cargar(DtoTiposDeUsuario dtoWhere) {
		
		return SqlBaseTiposUsuarioDao.cargar(dtoWhere);
	}

}
