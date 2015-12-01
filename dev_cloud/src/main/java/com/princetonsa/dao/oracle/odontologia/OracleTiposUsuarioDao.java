package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.TiposDeUsuarioDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseTiposUsuarioDao;
import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;

public class OracleTiposUsuarioDao implements TiposDeUsuarioDao {

	@Override

	public ArrayList<DtoTiposDeUsuario> cargar(DtoTiposDeUsuario dtoWhere) {
		
		return SqlBaseTiposUsuarioDao.cargar(dtoWhere);
	}
	
}
