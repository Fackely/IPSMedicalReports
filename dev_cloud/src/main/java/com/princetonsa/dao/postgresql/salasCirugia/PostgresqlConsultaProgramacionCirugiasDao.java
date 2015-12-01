package com.princetonsa.dao.postgresql.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.salasCirugia.ConsultaProgramacionCirugiasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseConsultaProgramacionCirugiasDao;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;
import com.princetonsa.mundo.salasCirugia.ConsultaProgramacionCirugias;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlConsultaProgramacionCirugiasDao implements ConsultaProgramacionCirugiasDao {

	/**
	 * 
	 */
	public HashMap consultarXPaciente(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarXPaciente(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarServiciosXPeticion(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarServiciosXPeticion(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarProfesionalesXPeticion(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarProfesionalesXPeticion(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarIngreso(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarIngreso(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarMaterialesEspeciales(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarMaterialesEspeciales(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarXRango(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarXRango(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarPedidos(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarPedidos(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarArticulosPedido(Connection con, ConsultaProgramacionCirugias mundo) {
		return SqlBaseConsultaProgramacionCirugiasDao.consultarArticulosPedido(con, mundo);
	}
}