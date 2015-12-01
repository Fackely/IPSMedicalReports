package com.princetonsa.dao.oracle.inventarios;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */


import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MedicamentosControladosAdministradosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseMedicamentosControladosAdministradosDao;

public class OracleMedicamentosControladosAdministradosDao implements MedicamentosControladosAdministradosDao {

	//consultar Medicamentos Controlados Administrados
	public HashMap consultarMediControAdmin(Connection con, HashMap criterios) {
		return SqlBaseMedicamentosControladosAdministradosDao.consultarMediControAdmin(con, criterios, DaoFactory.ORACLE);
	}

	
	// Enlace al metodo para realizar la busqueda para almacenarla en un archivo plano o mostrarla en pantalla 
	public HashMap generarResultados(Connection con, HashMap criterios) {
        return SqlBaseMedicamentosControladosAdministradosDao.generarResultados(con, criterios, DaoFactory.ORACLE);
    }
}