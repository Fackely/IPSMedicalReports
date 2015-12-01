package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.RegistroResumenParcialHistoriaClinicaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseRegistroResumenParcialHistoriaClinicaDao;


/**
 * @author lgchavez@princetonsa.com
 */
public class OracleRegistroResumenParcialHistoriaClinicaDao implements RegistroResumenParcialHistoriaClinicaDao {

	public HashMap consultarNotas(Connection con, HashMap mapa) {
		return SqlBaseRegistroResumenParcialHistoriaClinicaDao.consultarNotas(con, mapa);
	}

	public int insertarNotas(Connection con, HashMap mapa) {
		return SqlBaseRegistroResumenParcialHistoriaClinicaDao.insertarNotas(con, mapa);
	}

	public  HashMap consultarNotasAsocio(Connection con, HashMap mapa){
		return SqlBaseRegistroResumenParcialHistoriaClinicaDao.consultarNotasAsocio(con, mapa);
	}


}