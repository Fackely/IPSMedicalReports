/*
 * Enero 15, 2008
 */
package com.princetonsa.dao.oracle.cargos;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.cargos.CargosDirectosDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosDirectosDao;
import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Cargos Directos
 */
public class OracleCargosDirectosDao implements CargosDirectosDao 
{
	/**
	 * Método implementado para insertar regitros de cargos directos incluyendo su información de historia clínica
	 * @param con
	 * @param cargoDirecto
	 * @param cargoDirectoHC
	 * @return
	 */
	public int insertar(Connection con,ArrayList<DtoCargoDirecto> arregloCargoDirecto,DtoCargoDirectoHC cargoDirectoHC)
	{
		return SqlBaseCargosDirectosDao.insertar(con, arregloCargoDirecto, cargoDirectoHC);
	}
	
	/**
	 * Método para cargar la información e historia clínica de un cargo directo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoCargoDirectoHC consultarInformacionHC(Connection con,String numeroSolicitud)
	{
		return SqlBaseCargosDirectosDao.consultarInformacionHC(con, numeroSolicitud);
	}

	@Override
	public DtoProcedimiento buscarServiciosCargosDirectos(Connection con,
			int numeroSolicitud, int codigoTarifario) {
		return SqlBaseCargosDirectosDao.buscarServiciosCargosDirectos(con, numeroSolicitud, codigoTarifario);
	}
}
