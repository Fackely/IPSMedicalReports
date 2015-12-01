/**
 * 
 */
package com.princetonsa.dao.postgresql.carteraPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.carterapaciente.CierreSaldoInicialCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseCierreSaldoInicialCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoCierreCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDetCierreSaldoInicialCartera;

/**
 * @author armando
 *
 */
public class PostgresqlCierreSaldoInicialCarteraPacienteDao implements
		CierreSaldoInicialCarteraPacienteDao {

	@Override
	public ArrayList<DtoDetCierreSaldoInicialCartera> consultarPosibleListadoDocumentosCierre(String anioCierre, String mesCierre) {
		return SqlBaseCierreSaldoInicialCarteraPacienteDao.consultarPosibleListadoDocumentosCierre(anioCierre, mesCierre);
	}

	@Override
	public int insertarCierreSaldoInicial(DtoCierreCarteraPaciente cierreCarteraPaciente) 
	{
		return SqlBaseCierreSaldoInicialCarteraPacienteDao.insertarCierreSaldoInicial(cierreCarteraPaciente);
	}

	@Override
	public DtoCierreCarteraPaciente consultarCierreInicial(int institucion) 
	{
		return SqlBaseCierreSaldoInicialCarteraPacienteDao.consultarCierreInicial(institucion);
	}
	
	

}
