/**
 * 
 */
package com.princetonsa.dao.oracle.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.SaldosInicialesCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseSaldosInicialesCarteraPacienteDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoSaldosInicialesCarteraPaciente;

/**
 * @author armando
 *
 */
public class OracleSaldosInicialesCarteraPacienteDao implements
		SaldosInicialesCarteraPacienteDao {

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.carterapaciente.SaldosInicialesCarteraPacienteDao#busquedaAvanzadaSaldosIncialesCP(java.util.HashMap)
	 */
	@Override
	public ArrayList<DtoSaldosInicialesCarteraPaciente> busquedaAvanzadaSaldosIncialesCP(HashMap<String, String> parametros) 
	{
		return SqlBaseSaldosInicialesCarteraPacienteDao.busquedaAvanzadaSaldosIncialesCP(parametros);
	}

	@Override
	public int insertarActualizarPersona(Connection con,DtoPaciente paciente) 
	{
		return SqlBaseSaldosInicialesCarteraPacienteDao.insertarActualizarPersona(con,paciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public DtoDatosFinanciacion cargarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto){
		return SqlBaseSaldosInicialesCarteraPacienteDao.cargarDatosFinanciacion(con,dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto){
		return SqlBaseSaldosInicialesCarteraPacienteDao.actualizarDatosFinanciacion(con,dto);
	}
}
