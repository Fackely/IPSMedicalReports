package com.princetonsa.dao.oracle.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.consultaExterna.ServiAdicionalesXProfAtenOdontoDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseServiAdicionalesXProfAtenOdontoDao;
import com.princetonsa.dto.consultaExterna.DtoServiciosAdicionalesProfesionales;

public class OracleServiAdicionalesXProfAtenOdontoDao implements
		ServiAdicionalesXProfAtenOdontoDao {

	@Override
	public ArrayList<DtoServiciosAdicionalesProfesionales> consultarServiciosAdicionalesProfesionales(int codProfesional, int codEstandar) {
		
		return SqlBaseServiAdicionalesXProfAtenOdontoDao.consultarServiciosAdicionalesProfesionales(codProfesional, codEstandar);
		
	}

	@Override
	public boolean insertarServiciosAdicionales(Connection con,HashMap<String, Object> listServiciosSel, int codProfesional,String loginUsuario, int codInstitucion) {
		
		return SqlBaseServiAdicionalesXProfAtenOdontoDao.insertarServiciosAdicionales(con,listServiciosSel,codProfesional,loginUsuario,codInstitucion);
	}

	@Override
	public boolean eliminarServicioExistente(int codigoServicio, int codMedico,int codInstitucion ) {
		
		return SqlBaseServiAdicionalesXProfAtenOdontoDao.eliminarServicioAdicionalProfesional(codMedico, codigoServicio, codInstitucion);
	}

}
