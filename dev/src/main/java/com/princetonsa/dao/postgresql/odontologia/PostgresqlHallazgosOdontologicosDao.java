package com.princetonsa.dao.postgresql.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.HallazgosOdontologicosDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHallazgosOdontologicosDao;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;

public class PostgresqlHallazgosOdontologicosDao implements
		HallazgosOdontologicosDao {

	@Override
	public ArrayList<DtoHallazgoOdontologico> consultarHallazgosDentales(
			Connection con, int codigoInstitucionInt) {
		
		return SqlBaseHallazgosOdontologicosDao.consultarHallazgosDentales(con, codigoInstitucionInt);
	}

	@Override
	public boolean crearNuevoHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, String loginUsuario,int codigoInstitucionInt) {
		
		return SqlBaseHallazgosOdontologicosDao.crearNuevoHallazgoOdontologico(con,nuevoHallazgo,loginUsuario,codigoInstitucionInt);
	}

	@Override
	public boolean modificarHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, int consecutivoHallazgo,String loginUsuario) {
		
		return SqlBaseHallazgosOdontologicosDao.modificarHallazgoOdontologico(con, nuevoHallazgo, consecutivoHallazgo, loginUsuario);
	}

	@Override
	public boolean eliminarHallazgoOdontologico(int codigoHallazgo) {
		
		return SqlBaseHallazgosOdontologicosDao.eliminarHallazgoOdontologico(codigoHallazgo);
	}

	@Override
	public ArrayList<DtoHallazgoOdontologico> busquedaAvanzadaHallazgos(
			DtoHallazgoOdontologico dto) {
		
		return SqlBaseHallazgosOdontologicosDao.busquedaAvanzadaHallazgos(dto);
	}
	
	/**
	 * Retorna las convenciones parametrizadas dentro de los hallazgos
	 * */
	public ArrayList<DtoHallazgoOdontologico> busquedaConvencionesHallagos(DtoHallazgoOdontologico dto )
	{
		return SqlBaseHallazgosOdontologicosDao.busquedaConvencionesHallagos(dto);
	}
}