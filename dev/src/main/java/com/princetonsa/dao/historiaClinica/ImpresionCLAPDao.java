package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface ImpresionCLAPDao
{

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarSolicitudes(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public abstract boolean generarRegistroLogImpresion(Connection con, HashMap vo);
	
	
//-------------------------------------- MANIZALES ---------------------------------------------------//
	
	/**
	 * Metodo que consulta la información obstetrica del paciente
	 * @param con -> Conexion
	 * @param parametros -> Mapa que contiene los parametros de la consulta
	 */
	public HashMap consultarInformacionObstetrica(Connection con, HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param codigoCirugia
	 * @param codigoPaciente 
	 * @return
	 */
	public abstract HashMap consultarInformacionRecienNacido(Connection con, String codigoCirugia, String codigoPaciente); 

}
