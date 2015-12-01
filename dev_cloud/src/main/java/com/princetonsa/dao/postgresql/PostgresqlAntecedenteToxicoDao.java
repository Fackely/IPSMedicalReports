package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.AntecedenteToxicoDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedenteToxicoDao;

/**
 * Implementación de los métodos para acceder a la fuente de datos, la parte de
 * Antecedentes Mórbidos Médicos, predefinidos y otros, para Postgres.
 *
 * @version 1.0, Noviembre 28, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlAntecedenteToxicoDao implements AntecedenteToxicoDao
{
													  
	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarPredefinido(java.
	 * sql.Connection, java.lang.String, java.lang.String, int, boolean, java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarPredefinido(	Connection con,	
																	int codigoPaciente,
																	int codTipoPredefinido, 
																	boolean habitoActual,
																	String cantidad,
																	String frecuencia,
																	String tiempo,
																	String observaciones)
	{
		
		String insertarPredefinidoStr = "INSERT INTO ant_toxicos_tipos "
			  + "(codigo_paciente, "
			  + "codigo, "
			  + "cod_tipo_antecedente, "
			  + "actual, "
			  + "cantidad, "
			  + "tiempo_habito, "																		  
			  + "frecuencia, "
			  + "fecha_grabacion, "
			  + "hora_grabacion, "																		  
			  + "observaciones, fecha, hora ) "
			  + "VALUES (?, nextval('seq_ant_toxicos_tipos'), ?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";

		
	    return SqlBaseAntecedenteToxicoDao.insertarPredefinido(	con,	
				codigoPaciente,
				codTipoPredefinido, 
				habitoActual,
				cantidad,
				frecuencia,
				tiempo,
				observaciones, 
				insertarPredefinidoStr);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarPredefinidoTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarPredefinidoTransaccional(	Connection con,
																							int codigoPaciente,
																							int codTipoPredefinido,
																							boolean habitoActual,
																							String cantidad,
																							String frecuencia,
																							String tiempo,
																							String observaciones,
																							String estado) throws SQLException
	{

		String insertarPredefinidoStr = "INSERT INTO ant_toxicos_tipos "
			  + "(codigo_paciente, "
			  + "codigo, "
			  + "cod_tipo_antecedente, "
			  + "actual, "
			  + "cantidad, "
			  + "tiempo_habito, "																		  
			  + "frecuencia, "
			  + "fecha_grabacion, "
			  + "hora_grabacion, "																		  
			  + "observaciones, fecha, hora ) "
			  + "VALUES (?, nextval('seq_ant_toxicos_tipos'), ?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";

		
	    return SqlBaseAntecedenteToxicoDao.insertarPredefinidoTransaccional(con,
				codigoPaciente,
				codTipoPredefinido,
				habitoActual,
				cantidad,
				frecuencia,
				tiempo,
				observaciones,
				estado,
				insertarPredefinidoStr);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarOtro(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarOtro(	Connection con,
														int codigoPaciente,
														int codigo,
														String nombre,
														boolean habitoActual,
														String cantidad,
														String frecuencia,
														String tiempo,
														String observaciones)
	{
		return SqlBaseAntecedenteToxicoDao.insertarOtro(con, codigoPaciente, codigo, nombre, habitoActual, cantidad, frecuencia, tiempo, observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarOtroTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarOtroTransaccional(	Connection con,
																				int codigoPaciente,
																				int codigo,
																				String nombre,
																				boolean habitoActual,
																				String cantidad,
																				String frecuencia,
																				String tiempo,
																				String observaciones,
																				String estado) throws SQLException
	{
	    return SqlBaseAntecedenteToxicoDao.insertarOtroTransaccional(	con,
				codigoPaciente,
				codigo,
				nombre,
				habitoActual,
				cantidad,
				frecuencia,
				tiempo,
				observaciones,
				estado) ;
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarPredefinido(java.sql.Connection, java.lang.String, java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarPredefinido(	Connection con,
																		int codigoPaciente,
																		int codigo,
																		boolean habitoActual,
																		String cantidad,
																		String frecuencia,
																		String tiempo,
																		String observaciones)
	{
		return SqlBaseAntecedenteToxicoDao.modificarPredefinido(con, codigoPaciente, codigo, habitoActual, cantidad, frecuencia, tiempo, observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarPredefinidoTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarPredefinidoTransaccional(	Connection con,
																							int codigoPaciente,
																							int codigo,
																							boolean habitoActual,
																							String cantidad,
																							String frecuencia,
																							String tiempo,
																							String observaciones,
																							String estado) throws SQLException
	{
	    return SqlBaseAntecedenteToxicoDao.modificarPredefinidoTransaccional(con,
				codigoPaciente,
				codigo,
				habitoActual,
				cantidad,
				frecuencia,
				tiempo,
				observaciones,
				estado); 	
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarOtro(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarOtro(	Connection con,
															int codigoPaciente,
															int codigo,
															boolean habitoActual,
															String cantidad,
															String frecuencia,
															String tiempo,
															String observaciones)
	{
		return SqlBaseAntecedenteToxicoDao.modificarOtro( con, codigoPaciente, codigo, habitoActual, cantidad,frecuencia, tiempo, observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarOtroTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarOtroTransaccional(	Connection con,
																					int codigoPaciente,
																					int codigo,
																					String nombre,
																					boolean habitoActual,
																					String cantidad,
																					String frecuencia,
																					String tiempo,
																					String observaciones,
																					String estado) throws SQLException
	{
	    return SqlBaseAntecedenteToxicoDao.modificarOtroTransaccional(	con,
				codigoPaciente,
				codigo,
				habitoActual,
				cantidad,
				frecuencia,
				tiempo,
				observaciones,
				estado);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#existeAntecedentePredefinido(java.sql.Connection, java.lang.String, java.lang.String, int)
	 */
	public ResultadoBoolean existeAntecedentePredefinido(	Connection con,
																					int codigoPaciente,
																					int codigo)
	{
		return SqlBaseAntecedenteToxicoDao.existeAntecedentePredefinido(con,codigoPaciente,codigo);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#existeAntecedenteOtro(java.sql.Connection, java.lang.String, java.lang.String, int)
	 */
	public ResultadoBoolean existeAntecedenteOtro(	Connection con,
																			int codigoPaciente,
																			int codigo)
	{
		return SqlBaseAntecedenteToxicoDao.existeAntecedenteOtro(con,codigoPaciente,codigo);
	}
	
}
