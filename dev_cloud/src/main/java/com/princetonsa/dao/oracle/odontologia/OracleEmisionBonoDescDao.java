package com.princetonsa.dao.oracle.odontologia;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.InfoPacienteBonoPresupuesto;

import com.princetonsa.dao.sqlbase.odontologia.SqlBaseEmisionBonosDescDao;
import com.princetonsa.dao.odontologia.EmisionBonosDescDao;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;


public class OracleEmisionBonoDescDao implements  EmisionBonosDescDao{
	
	/**
	 * 
	 */
	public double guardar( DtoEmisionBonosDesc dto) 
	{
		return SqlBaseEmisionBonosDescDao.guardar(dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoEmisionBonosDesc> cargar(DtoEmisionBonosDesc dto) 
	{
		return SqlBaseEmisionBonosDescDao.cargar(dto);
	}

	/**
	 * 
	 */
	public boolean modificar(DtoEmisionBonosDesc dto) 
	{
		return SqlBaseEmisionBonosDescDao.modificar(dto);
    }

	/**
	 * 
	 */
	public boolean eliminar(DtoEmisionBonosDesc dto) 
	{
		return SqlBaseEmisionBonosDescDao.eliminar(dto) ;
	}
	/**
	 * 
	 */

	@Override
	public boolean existeCruceFechasConvenioPrograma(DtoEmisionBonosDesc dto,  double codigoPkNotIn) {
		return SqlBaseEmisionBonosDescDao.existeCruceFechasConvenioPrograma(dto, codigoPkNotIn);
		}
	
	@Override
	/**
	 * 
	 */
	public boolean existeCruceSerialesIdYConvenio(DtoEmisionBonosDesc dto, double codigoPkNotIn) {
		return SqlBaseEmisionBonosDescDao.existeCruceSerialesIdYConvenio(dto, codigoPkNotIn);
		}

	@Override
	public boolean esSerialVigenteDisponible(Connection con, Integer bono, int codigoConvenio) {
		return SqlBaseEmisionBonosDescDao.esSerialVigenteDisponible(con, bono, codigoConvenio);
	}

	@Override
	public ArrayList<InfoPacienteBonoPresupuesto> consultaEmisionBonos(
			InfoPacienteBonoPresupuesto dto, boolean aplicaProgramas) {
		
		return SqlBaseEmisionBonosDescDao.consultaEmisionBonos(dto, aplicaProgramas);
	}

	
	@Override
	public boolean existeSerialSubCuentas(BigDecimal serialInicial,
			BigDecimal serialFinal, int convenio) {
		
		return SqlBaseEmisionBonosDescDao.existeSerialSubCuentas(serialInicial, serialFinal, convenio);
	}
	

	/**
	 * Método que valida si un serial está vigente o disponible
	 * @param con
	 * @param bono
	 * @param codigoConvenio
	 * @return Mensaje de error, "" en caso de no existir error
	 * @author Juan David
	 */
	public String esSerialVigenteDisponibleMensajeError(Connection con, Integer bono, int codigoConvenio, DtoSubCuentas dtoSubcuentas) {
		return SqlBaseEmisionBonosDescDao.esSerialVigenteDisponibleMensajeError(con, bono, codigoConvenio, dtoSubcuentas);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.EmisionBonosDescDao#asociarBonoProgramaConvenio(java.sql.Connection, com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento)
	 */
	@Override
	public boolean asociarBonoProgramaConvenio(Connection con,
			DtoBonoDescuento bono)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono)
	{
		return SqlBaseEmisionBonosDescDao.marcarBonoUtilizado(con, bono);
	}


}
