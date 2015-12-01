package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.EvolucionOdontologicaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseEvolucionOdontologicaDao;
import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;

public class OracleEvolucionOdontologicaDao implements EvolucionOdontologicaDao
{
	public ArrayList<DtoEvolucionOdontologica> consultarEvolucion(DtoEvolucionOdontologica dto)
	{
		return SqlBaseEvolucionOdontologicaDao.consultarEvolucion(dto);
	}
	
	public double insertarEvolucionOdon(DtoEvolucionOdontologica dto,Connection con)
	{
		return SqlBaseEvolucionOdontologicaDao.insertarEvolucionOdon(dto, con);
	}
	
	/**
	 * Método implementado para cargar la informacion de la evolucion
	 * @param con
	 * @param evolucionOdo
	 */
	public void consultar(Connection con,DtoEvolucionOdontologica evolucionOdo)
	{
		SqlBaseEvolucionOdontologicaDao.consultar(con, evolucionOdo);
	}
}