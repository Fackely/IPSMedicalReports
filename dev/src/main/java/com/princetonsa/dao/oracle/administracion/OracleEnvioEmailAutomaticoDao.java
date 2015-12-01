package com.princetonsa.dao.oracle.administracion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.administracion.EnvioEmailAutomaticoDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseEnvioEmailAutomaticoDao;
import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;

public class OracleEnvioEmailAutomaticoDao implements  EnvioEmailAutomaticoDao{

	@Override
	public boolean insertar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con) {
		return SqlBaseEnvioEmailAutomaticoDao.insertar(dtoEnvioEmailAutomatico, con);
	}

	@Override
	public boolean eliminar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con){
		return SqlBaseEnvioEmailAutomaticoDao.eliminar(dtoEnvioEmailAutomatico, con);
	}
	
	
	@Override
	public ArrayList<DtoEnvioEmailAutomatico> listar(Connection con,
			int codigoInstitucion) {
		return SqlBaseEnvioEmailAutomaticoDao.listar(con, codigoInstitucion);
	}
	@Override
	public boolean modificar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con){
		return SqlBaseEnvioEmailAutomaticoDao.modificar(dtoEnvioEmailAutomatico, con);
	}

}
