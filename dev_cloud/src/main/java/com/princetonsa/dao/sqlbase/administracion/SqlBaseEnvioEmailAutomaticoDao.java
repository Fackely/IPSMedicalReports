package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;

public class SqlBaseEnvioEmailAutomaticoDao {

	private static Logger logger=Logger.getLogger(SqlBaseEnvioEmailAutomaticoDao.class);
	
	// INSERTA TODOS LOS DATOS EN LA BD
	public static boolean insertar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico, Connection con) {
		String sentencia="INSERT INTO administracion.envio_automatico_email  (codigo_pk,funcionalidad,usuario,institucion,usuario_modifica,fecha_modifica,hora_modifica) VALUES(?, ?, ?, ?, ?, current_date, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
		boolean respuesta = false;
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		
		int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_envio_automatico_email");
		
		try {
			psd.setInt(1, secuencia);
			psd.setString(2, dtoEnvioEmailAutomatico.getFuncionalidad());
			psd.setString(3, dtoEnvioEmailAutomatico.getUsuario());
			psd.setInt(4, dtoEnvioEmailAutomatico.getInstitucion());
			psd.setString (5,dtoEnvioEmailAutomatico.getUsuarioModifica());
			
			if(psd.executeUpdate()>0)
			{
				respuesta = true;
			}
		
			UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
		} catch (SQLException e) 
		{
			
			logger.info(psd);
			logger.error("error ingresando datos ....................", e);
			return respuesta;
		}
		return respuesta;
		
	}
	
	
	
	/**
	 * M�todo que elimina un registro de envio automatico email
	 * @param dtoEnvioEmailAutomatico
	 * @param con
	 * @return
	 */
	public static boolean eliminar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con)
	{
		boolean respuesta = false;
		String sentencia="delete from administracion.envio_automatico_email where codigo_pk = ?";
		try
		{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			
			psd.setInt(1,dtoEnvioEmailAutomatico.getCodigoPk());
			if (psd.executeUpdate()>0)
			{
				respuesta = true;
				
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
		}
		catch (SQLException e) 
		{
		
			logger.error("error borrando datos ....................", e);
			return respuesta;
		}
	
		return respuesta;
	}
	
	public static boolean modificar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con){
		
		boolean respuesta = false;
		String sentencia="update  administracion.envio_automatico_email set  funcionalidad = ?, usuario = ?, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", institucion= ? , usuario_modifica = ? where codigo_pk = ?";
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try
		{
			
			
			
			psd.setString(1, dtoEnvioEmailAutomatico.getFuncionalidad());
			psd.setString(2, dtoEnvioEmailAutomatico.getUsuario());
			psd.setInt(3, dtoEnvioEmailAutomatico.getInstitucion());
			psd.setString (4,dtoEnvioEmailAutomatico.getUsuarioModifica());
			psd.setInt(5, dtoEnvioEmailAutomatico.getCodigoPk());
			
			if (psd.executeUpdate()>0)
			{
				respuesta = true;
				
				
				
				
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
		}
		catch (SQLException e) 
		{
			
			logger.info(psd);
			logger.error("error modificando datos ....................", e);
			return respuesta;
		}
	
		return respuesta;
		
	}
	
	
	//CONSULTA LOS DATOS EN LA BD Y LLENA TODO EL DTO.
	
	
	public static ArrayList<DtoEnvioEmailAutomatico> listar(Connection con,
			int codigoInstitucion) {
		String sentencia="SELECT " +
								"eae.codigo_pk AS codigo_pk, " +
								"eae.funcionalidad AS funcionalidad, " +
								"eae.usuario AS usuario, " +
								"eae.institucion AS institucion, " +
								"eae.usuario_modifica AS usuario_modifica, " +
								"eae.fecha_modifica AS fecha_modifica, " +
								"eae.hora_modifica AS hora_modifica, " +
								"getNombreUsuario2(eae.usuario) AS nombre_usuario, " +
								"p.email AS email " +
							"FROM " +
								"administracion.envio_automatico_email eae " +
							"INNER JOIN " +
								"administracion.usuarios u ON(eae.usuario=u.login) " +
							"INNER JOIN " +
								"administracion.personas p ON(p.codigo=u.codigo_persona) " +
								"WHERE " +
									"eae.institucion=? " +
							"ORDER BY nombre_usuario";
		
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try {
			psd.setInt(1, codigoInstitucion);
			
		
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			ArrayList<DtoEnvioEmailAutomatico> resultado=new ArrayList<DtoEnvioEmailAutomatico>();
			
			while(rsd.next())
			{
				DtoEnvioEmailAutomatico dto=new DtoEnvioEmailAutomatico();
				dto.setCodigoPk(rsd.getInt("codigo_pk"));
				dto.setFuncionalidad(rsd.getString("funcionalidad"));
				dto.setFuncionalidadAnterior(rsd.getString("funcionalidad"));
				dto.setUsuario(rsd.getString("usuario"));
				dto.setUsuarioAnterior(rsd.getString("usuario"));
				dto.setInstitucion(rsd.getInt("institucion"));
				dto.setUsuarioModifica(rsd.getString("usuario_modifica"));
				dto.setFechaModifica(rsd.getString("fecha_modifica"));
				dto.setHoraModifica(rsd.getString("hora_modifica"));
				dto.setNombreCompleto(rsd.getString("nombre_usuario"));
				dto.setEmailUsuario(rsd.getString("email"));
				
				dto.setExisteBD(true);
				resultado.add(dto);
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			return resultado;
		} catch (SQLException e) {
			logger.info(psd);
			logger.error("Error consultando la BD ", e);
		}
		return null;
		
	}

}
