package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;


public interface ContrarreferenciaDao 
{
	
	
		
		
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap busquedaInstitucionesSirc(Connection con,HashMap vo);

	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	public HashMap cargarConductasSeguirContrareferencia(Connection con, int numeroReferenciaContra);
	
	
	public abstract HashMap consultarReferenciaPaciente(Connection con, HashMap vo);
	
	
	public abstract HashMap consultarReferenciaArea(Connection con, HashMap vo);
	
	
	public abstract HashMap consultarContrarreferencia(Connection con, HashMap vo);
	
	
	public abstract boolean insertar(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public abstract boolean modificar(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract int actualizarEstadoContrarreferencia(Connection con,HashMap vo);

	/**
	 * 
	 * @param con
	 * @param numeroReferencia
	 * @return
	 */
	public HashMap consultarResultadosProcedimiento(Connection con, int numeroReferencia);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarProcedimientos(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarDiagnosticos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param numeroContrarreferencia
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean eliminarProcedimientos(Connection con, String numeroContrarreferencia, String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	public boolean eliminarDiagnosticos(Connection con, int numeroReferenciaContra);

	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	public HashMap consultarDiagnosticos(Connection con, int numeroReferenciaContra);

	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @param codigoConducta
	 * @return
	 */
	public boolean eliminarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta);

	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @param codigoConducta
	 * @param valor
	 * @return
	 */
	public boolean insertarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta, String valor);

	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap getUltimosDiagnosticosIngreso(Connection con, int ingreso);
	
	
	
	

}
