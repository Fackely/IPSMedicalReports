package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.InfoPacienteBonoPresupuesto;

import com.princetonsa.dao.odontologia.EmisionBonosDescDao;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseEmisionBonosDescDao;



public class PostgresEmisionBonosDao implements EmisionBonosDescDao {

	/**
	 * 
	 */
	@Override
	public ArrayList<DtoEmisionBonosDesc> cargar(DtoEmisionBonosDesc dto) {
		return	SqlBaseEmisionBonosDescDao.cargar(dto);
		 
	}

	@Override
	/**
	 * 
	 */
	public boolean eliminar(DtoEmisionBonosDesc dto) {
		return SqlBaseEmisionBonosDescDao.eliminar(dto);
	}

	/**
	 * 
	 */
	public double guardar(DtoEmisionBonosDesc dto) {
		return SqlBaseEmisionBonosDescDao.guardar(dto);
	}

	@Override
	/**
	 * 
	 */
	public boolean modificar(DtoEmisionBonosDesc dto) {
		return SqlBaseEmisionBonosDescDao.modificar(dto);
	}

	@Override
	public boolean existeCruceFechasConvenioPrograma(DtoEmisionBonosDesc dto , double codigoPkNotIn) {
			return SqlBaseEmisionBonosDescDao.existeCruceFechasConvenioPrograma(dto, codigoPkNotIn);
	}

	@Override
	public boolean existeCruceSerialesIdYConvenio(DtoEmisionBonosDesc dto,  double codigoPkNotIn) {
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
			BigDecimal serialFinal, int convenios) {
		
		return SqlBaseEmisionBonosDescDao.existeSerialSubCuentas(serialInicial, serialFinal, convenios);
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

	@Override
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono)
	{
		return SqlBaseEmisionBonosDescDao.marcarBonoUtilizado(con, bono);
	}

	@Override
	public boolean asociarBonoProgramaConvenio(Connection con, DtoBonoDescuento bono)
	{
		return SqlBaseEmisionBonosDescDao.asociarBonoProgramaConvenio(con, bono);
	}
	

}
