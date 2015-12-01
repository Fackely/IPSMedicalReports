package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoPazYSalvoCarteraPaciente;
import com.princetonsa.dto.consultaExterna.DtoMotivosCita;


public class SqlBaseMotivosCitaDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseMotivosCitaDao.class);

	private static String consultarMotivosCita="SELECT codigo_pk AS codigoPk, " +
												"codigo AS codigo, " +
												"institucion AS institucion, " +
												"descripcion AS descripcion, " +
												"activo AS activo, " +
												"fecha_modifica AS fecha, " +
												"usuario_modifica AS usuario, " +
												"hora_modifica AS hora, " +
												"tipo_motivo " +
												"FROM odontologia.motivos_cita ORDER BY descripcion";

	private static String insertarMotivoCita ="INSERT INTO odontologia.motivos_cita ( " +
											"codigo_pk, " +
											"codigo, " +
											"institucion, " +
											"descripcion, " +
											"activo, " +
											"fecha_modifica, " +
											"hora_modifica, " +
											"usuario_modifica,  " +
											"tipo_motivo) VALUES( " +
											"?,?,?,?,?,CURRENT_DATE,?,?,?)";

	private static String modificarMotivoCita="UPDATE odontologia.motivos_cita SET codigo=?,  descripcion=?, activo=?, " +
												"fecha_modifica=CURRENT_DATE, hora_modifica=?, usuario_modifica=?,tipo_motivo=? " +
												"WHERE codigo_pk=? ";
	
	private static String insertarLogMotivoCita ="INSERT INTO odontologia.log_motivos_cita ( " +
												"codigo_pk, " +
												"codigo, " +
												"descripcion, " +
												"activo, " +
												"eliminado, " +
												"fecha_modifica, " +
												"hora_modifica, " +
												"usuario_modifica, " +
												"motivo_cita, " +
												"tipo_motivo) VALUES( " +
												"?,?,?,?,?,CURRENT_DATE,?,?,?,?)";
	
	private static String eliminarMotivoCita ="DELETE FROM odontologia.motivos_cita WHERE codigo_pk=? ";
	
	public static boolean eliminarMotivoCita(int codigoPk) 
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarMotivoCita, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				ps.setInt(1,codigoPk);
												
				if(ps.executeUpdate()>0)
				{	
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return true;
				}
				ps.close();
								
			}
			catch (SQLException e) 
			{	
				logger.info("error al eliminar motivo cita---> "+e);
			}		
		return false;
	}
	
	public static boolean insertarLogMotivoCita(String codigo,String descripcion, String activo, String eliminado,String usuario, int motivoCita, String tipoMotivo) {
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarLogMotivoCita, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				int seq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_motivos_cita");
				ps.setInt(1,seq);
				ps.setString(2, codigo);
				ps.setString(3, descripcion);
				ps.setString(4, activo);
				ps.setString(5, eliminado);
				ps.setString(6, UtilidadFecha.getHoraActual());
				ps.setString(7, usuario);
				ps.setInt(8, motivoCita);
				ps.setString(9, tipoMotivo);
				
				logger.info("\n\nquery::: "+insertarLogMotivoCita+" seq: "+seq);
				logger.info(" codigo::: "+codigo+" descripcion: "+descripcion+" activo: "+activo);
				
				if(ps.executeUpdate()>0)
				{	
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return true;
				}
				ps.close();
								
			}
			catch (SQLException e) 
			{	

				logger.info("error al insertar log motivo cita---> "+e);
			}		
		return false;
	}
	
	public static boolean modificarMotivoCita(String codigo,String descripcion, String activo, int codigoPk, String usuario, String tipoMotivo) 
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {						
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarMotivoCita, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
			logger.info("\n\nquery:: "+modificarMotivoCita);
			
			ps.setString(1,codigo);
			ps.setString(2,descripcion);
			ps.setString(3,activo);
			ps.setString(4,UtilidadFecha.getHoraActual());
			ps.setString(5,usuario);
			ps.setString(6, tipoMotivo);
			ps.setInt(7,codigoPk);	
			
			logger.info("consulta *************** "+tipoMotivo );
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("error actualizando motivo cita "+e);		
		}		
		return false;
	}
	
	public static int insertarMotivoCita(String codigo, String descripcion, String activo, int institucion, String usuario, String tipoMotivo) 
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarMotivoCita, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				int seq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_motivos_cita");
				ps.setInt(1,seq);
				ps.setString(2, codigo);
				ps.setInt(3, institucion);
				ps.setString(4, descripcion);
				ps.setString(5, activo);
				ps.setString(6, UtilidadFecha.getHoraActual());
				ps.setString(7, usuario);
				ps.setString(8, tipoMotivo);
				
				
				logger.info("\n\nquery::: "+insertarMotivoCita+" seq: "+seq);
				logger.info(" codigo::: "+codigo+" descripcion: "+descripcion+" activo: "+activo);
				
				if(ps.executeUpdate()>0)
				{	
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return seq;
				}
				ps.close();
								
			}
			catch (SQLException e) 
			{	
				logger.info("error al insertar motivo cita---> "+e);
			}		
		return 0;
	}
	
	public static ArrayList<DtoMotivosCita> consultarMotivosCita() 
	{
		ArrayList<DtoMotivosCita> lista= new ArrayList<DtoMotivosCita>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarMotivosCita, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
					
			while(rs.next())
			{
				DtoMotivosCita dto= new DtoMotivosCita();
				dto.setCodigoPk(rs.getInt("codigoPk"));
				dto.setCodigo(rs.getString("codigo"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setActivo(rs.getString("activo"));
				dto.setFechaModifica(rs.getString("fecha"));
				dto.setUsuarioModifica(rs.getString("usuario"));
				dto.setHoraModifica(rs.getString("hora"));
				dto.setTipoMotivo(rs.getString("tipo_motivo"));
				lista.add(dto);				
			}
		}
		catch (Exception e) {
			logger.info("Error en Consultar Motivos Cita >> "+e+" "+consultarMotivosCita);
		}		
		return lista;
	}
}