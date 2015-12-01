package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosDouble;

import com.princetonsa.dao.manejoPaciente.PerfilNEDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePerfilNEDDao;
import com.princetonsa.dto.manejoPaciente.DtoCamposPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;

/**
 * 
 * @author axioma
 *
 */
public class OraclePerfilNEDDao implements PerfilNEDDao 
{

	@Override
	public ArrayList<DtoPerfilNed> cargar(DtoPerfilNed dtoWhere) {
		
		return SqlBasePerfilNEDDao.cargar(dtoWhere);
	}

	@Override
	public ArrayList<DtoCamposPerfilNed> cargarCampos(
			DtoCamposPerfilNed dtoWhere) {
		
		return SqlBasePerfilNEDDao.cargarCampos(dtoWhere);
	}

	@Override
	public double guardar(Connection con, DtoPerfilNed dto) {
		
		return SqlBasePerfilNEDDao.guardar(con, dto);
	}

	@Override
	public double guardarCampo(Connection con, DtoCamposPerfilNed dto) {
		
		return SqlBasePerfilNEDDao.guardarCampo(con, dto);
	}

	@Override
	public boolean modificar(Connection con, DtoPerfilNed dtoNuevo, DtoPerfilNed dtoWhere) {
		
		return SqlBasePerfilNEDDao.modificar(con, dtoNuevo, dtoWhere);
	}

	@Override
	public boolean modificarCampos(Connection con, DtoCamposPerfilNed dtoNuevo,
			DtoCamposPerfilNed dtoWhere) {
		
		return SqlBasePerfilNEDDao.modificarCampos(con, dtoNuevo, dtoWhere);
	}

	/**
	 * 
	 * @param codigoPkPerfil
	 * @return
	 */
	public InfoDatosDouble cargarTotalesFactoresPrediccion(double codigoPkPerfil)
	{
		return SqlBasePerfilNEDDao.cargarTotalesFactoresPrediccion(codigoPkPerfil);
	}
}
