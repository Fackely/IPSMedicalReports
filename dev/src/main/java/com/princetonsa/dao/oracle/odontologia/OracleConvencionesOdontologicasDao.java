package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ConvencionesOdontologicasDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseConvencionesOdontologicasDao;
import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;

public class OracleConvencionesOdontologicasDao implements
		ConvencionesOdontologicasDao {

	@Override
	public ArrayList<DtoConvencionesOdontologicas> consultarConvencionesOdontologicas(int codInstitucion) {
		
		return SqlBaseConvencionesOdontologicasDao.consultarConvencionesOdontologicas(codInstitucion);
	}

	@Override
	public boolean eliminarConvencion(int codigoConvencion) {
		
		return SqlBaseConvencionesOdontologicasDao.eliminarConvencion(codigoConvencion);
	}

	@Override
	public boolean modificarConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion,int consecutivoConvencion, String loginUsuario) {
		
		return SqlBaseConvencionesOdontologicasDao.modificarConvencionOdontologica(con,nuevaConvencion,consecutivoConvencion, loginUsuario);
	}

	@Override
	public int crearNuevaConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion, String loginUsuario,int codInstitucion) {
		
		return SqlBaseConvencionesOdontologicasDao.crearNuevaConvencionOdontologica(con,nuevaConvencion,loginUsuario,codInstitucion);
	}

}
