package com.princetonsa.dao.postgresql.inventarios;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre 2008
 */

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MedicamentosControladosAdministradosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseMedicamentosControladosAdministradosDao;


public class PostgresqlMedicamentosControladosAdministradosDao implements MedicamentosControladosAdministradosDao {

	//consultar Medicamentos Controlados Administrados
	public HashMap consultarMediControAdmin(Connection con, HashMap criterios) {
		return SqlBaseMedicamentosControladosAdministradosDao.consultarMediControAdmin(con, criterios, DaoFactory.POSTGRESQL);
	}


	// Enlace al metodo para reailzar la busqueda para lamacenarla en un archivo plano
	public HashMap generarResultados(Connection con, HashMap criterios) {
        return SqlBaseMedicamentosControladosAdministradosDao.generarResultados(con, criterios, DaoFactory.POSTGRESQL);
    }

}