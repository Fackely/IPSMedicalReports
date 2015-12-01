/*
 * @(#)SqlBaseSolicitudConsultaExternaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.List;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Solicitud Consulta
 *
 *	@version 1.0, Apr 13, 2004
 */
public class SqlBaseSolicitudConsultaExternaDao 
{
	/** Objeto para realizar acciones de registro */
	private static Logger il_logger
		= Logger.getLogger(SqlBaseSolicitudConsultaExternaDao.class);

	/** Sentencia SQL para insertar los datos particulares de una solicitud de consulta externa */
	private static final String is_insertar =
		"INSERT INTO solicitudes_consulta (numero_solicitud, codigo_servicio_solicitado) VALUES(?,?)";

	/** Sentencia SQL para cargar los datos particulares de una solicitud de consulta externa */
	private static final String is_cargar =
		"SELECT sc.codigo_servicio_solicitado AS codigoServicioSolicitado,"		+
						"CASE "								+
							"WHEN sc.codigo_servicio_solicitado IS NULL THEN '' "					+
							"ELSE rf.descripcion "			+
						"END AS nombreServicioSolicitado,"		+
						"CASE "								+
							"WHEN sc.codigo_servicio_solicitado IS NULL THEN -1 "					+
							"ELSE e.codigo "				+
						"END AS codigoEspecialidadSolicitada,"	+
						"CASE "								+
							"WHEN sc.codigo_servicio_solicitado IS NULL THEN '' "					+
							"ELSE e.nombre "				+
						"END AS nombreEspecialidadSolicitada "	+
		"FROM solicitudes_consulta sc "	+
						"INNER JOIN servicios s ON(s.codigo=sc.codigo_servicio_solicitado)"			+
						"INNER JOIN referencias_servicio rf ON(s.codigo=rf.servicio AND rf.tipo_tarifario=0)"	+
						"INNER JOIN especialidades e ON(s.especialidad=e.codigo)"							+
		"WHERE numero_solicitud=?";

	/**
	* Inserta una solicitud de consulta externa en una BD Genérica
	* 
	* @param ac_con				Conexión a la fuente de datos
	* @param as_estado			Estado de la transacción
	* @param ai_numeroSolicitud	Número de la solicitud de consulta externa a asignar. Este número
	*							debe existir en la tabla de solicitudes
	* @param ai_codigoServicio	Código del servicio de la solicitud de consulta externa
	* @return Número de solicitudes insertadas correctamente
	* @throws java.sql.SQLException si se presentó un error de base de datos
	*/
	public static int insertarTransaccional(
		Connection	ac_con,
		String		as_estado,
		int			ai_numeroSolicitud,
		int			ai_codigoServicio
	)throws SQLException
	{
		boolean				lb_continuar;
		DaoFactory			ldf_df;
		int					li_resp;
		PreparedStatementDecorator	lps_ps;

		/* Obtener una referencia al objeto pricipal de acceso a base de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Iniciar el control de flujo */
		lb_continuar = true;

		/* El estado de la transacción no puede ser nulo */
		if(as_estado == null)
		{
			ldf_df.abortTransaction(ac_con);

			if(il_logger.isDebugEnabled() )
				il_logger.debug(
					"Estado de transaccion inválida al insertar solicitud de consulta externa"
				);

			throw new SQLException(
				"El estado de la transacción (Insertar Solicitud Consulta Externa) " +
				"no esta especificado"
			);
		}

		/* Se desea iniciar una transaccion */
		if(as_estado.equals(ConstantesBD.inicioTransaccion) )
		{
			/* Abrir una conexíon a la base de datos si es necesario */
			if(ac_con == null || ac_con.isClosed() )
				ac_con = ldf_df.getConnection();

			/* Iniciar la transacción */
			lb_continuar = ldf_df.beginTransaction(ac_con);
		}

		/* Insertar los datos particulares de la solicitud de consulta externa */
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		lps_ps.setInt(1, ai_numeroSolicitud);
		lps_ps.setInt(2, ai_codigoServicio);

		/* Obtener el número de registros insertados */
		lb_continuar = (li_resp = lps_ps.executeUpdate() ) == 1;

		if(lb_continuar)
		{
			/* Se desea finalizar una transaccion */
			if(as_estado.equals(ConstantesBD.finTransaccion) )
			{
				/* Finalizar la transacción */
				ldf_df.endTransaction(ac_con);

				/* Cerrar la conexión a la base de datos */
				if(ac_con != null && !ac_con.isClosed() )
					UtilidadBD.closeConnection(ac_con);
			}
		}
		else
		{
			if(il_logger.isDebugEnabled() )
				il_logger.debug("Error al insertar solicitud de consulta externa");

			/* Se presentó un error en la transacción */
			ldf_df.abortTransaction(ac_con);

			throw new SQLException(
				"Error al insertar la Solicitud de Consulta Externa No." + ai_numeroSolicitud
			);
		}

		return li_resp;
	}

	/**
	* Carga los datos de una solicitud de consulta externa desde una 
	* BD Genérica
	*  
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de consulta externa a cargar
	*/
	public static HashMap cargar(Connection ac_con, int ai_numeroSolicitud)throws SQLException
	{
		List				ll_l;
		PreparedStatementDecorator	lps_ps;

		/* Abrir una conexíon a la base de datos si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_cargar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		lps_ps.setInt(1, ai_numeroSolicitud);

		ll_l = (List)UtilidadBD.resultSet2Collection(new ResultSetDecorator(lps_ps.executeQuery()) );

		return (HashMap) ((ll_l.size() == 1) ? ll_l.get(0) : null);
	}

}
