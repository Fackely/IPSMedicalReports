package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.EventosAdversos;
import com.princetonsa.mundo.salasCirugia.ConsultaProgramacionCirugias;

public interface ConsultaProgramacionCirugiasDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarXPaciente(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarServiciosXPeticion(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarProfesionalesXPeticion(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarIngreso(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarMaterialesEspeciales(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarXRango(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarPedidos(Connection con, ConsultaProgramacionCirugias mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarArticulosPedido(Connection con, ConsultaProgramacionCirugias mundo);
	
}