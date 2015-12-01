package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class SqlBaseProgramasOdontologicosDao
{
	private static Logger logger = Logger.getLogger(SqlBaseProgramasOdontologicosDao.class);
	
	private static String strConsultaProgramas=	"SELECT " +
													"p.codigo," +
													"p.codigo_programa AS codprograma," +
													"p.institucion," +
													"p.nombre," +
													"p.activo," +
													"p.fecha_modifica AS fechamodifica," +
													"p.hora_modifica AS horamodifica," +
													"p.usuario_modifica AS usuariomodifica," +
													"p.especialidad," +
													"getnombreespecialidad(p.especialidad) AS nombreesp," +
													"p.convencion," +
													"co.codigo AS codconvencion," +
													"co.nombre AS nomconvencion," +
													"co.archivo_convencion AS archivo " +
												"FROM " +
													"odontologia.programas p "+
												"LEFT OUTER JOIN " +
													"odontologia.convenciones_odontologicas co ON (co.consecutivo=p.convencion) ";
	
	private static String strInsertarPrograma="INSERT INTO " +
													"odontologia.programas " +
													"(" +
													"codigo," +
													"codigo_programa," +
													"institucion," +
													"nombre," +
													"activo," +
													"fecha_modifica," +
													"hora_modifica," +
													"usuario_modifica," +
													"especialidad," +
													"convencion" +
													")" +
												"VALUES " +
													"(" +
													"?,?,?,?,?,?,?,?,?,?" +
													")";

	private static String strInsertarDetalle	=	"INSERT INTO " +
														"odontologia.detalle_programas " +
													"(" +
														"codigo_pk," +
														"programas," +
														"orden," +
														"servicio," +
														"activo," +
														"fecha," +
														"hora," +
														"usuario" +
													") " +
													"VALUES " +
														"(" +
														"?,?,?,?,?,?,?,?" +
														")";
	
	private static String strEliminarPrograma	=	"DELETE FROM odontologia.programas WHERE codigo=?";
	
	private static String strInsertarLogPrograma="INSERT INTO " +
													"odontologia.log_programas " +
													"(" +
													"codigo_pk," +
													"programa," +
													"codigo_programa," +
													"nombre," +
													"activo," +
													"especialidad," +
													"eliminado," +
													"fecha," +
													"hora," +
													"usuario" +
													")" +
												"VALUES " +
													"(" +
													"?,?,?,?,?,?,?,?,?,?" +
													")";
	
	private static String strActualizarPrograma	=	"UPDATE odontologia.programas SET "+ 
														"codigo_programa=? ," +
														"institucion=? ," +
														"nombre=? ," +
														"activo=? ," +
														"fecha_modifica=? ," +
														"hora_modifica=? ," +
														"usuario_modifica=? ," +
														"especialidad=?," +
														"convencion=? " +
													"WHERE " +
														"codigo=? ";
	
	private static String strEliminarDetallePrograma	=	"DELETE FROM odontologia.detalle_programas WHERE codigo_pk=?";
														
	private static String strInsertarLogDetalle	=	"INSERT INTO " +
														"odontologia.log_detalle_programas " +
													"(" +
														"codigo_pk," +
														"detalle_programa," +
														"programa," +
														"orden," +
														"servicio," +
														"activo," +
														"eliminado," +
														"fecha," +
														"hora," +
														"usuario" +
													") " +
													"VALUES " +
														"(" +
														"?,?,?,?,?,?,?,?,?,?" +
														")";
	
	private static String strActualizarDetalle	=	"UPDATE " +
														"odontologia.detalle_programas " +
													"SET " +
														"activo=? " +
													"WHERE " +
														"codigo_pk=?";
	
	
	public static boolean actualizarDetalle (String activo, double codigoDetalle)
	{
		boolean transaccionExitosa=false;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarDetalle, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1,activo);
			ps.setDouble(2,codigoDetalle);
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			else
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizarDetalle==> "+e);
		}
		return transaccionExitosa;
	}
	
	
	public static boolean insertarLogDetalle (DtoDetalleProgramas dto)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			double secuenciaLogDetPrograma=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_detalle_programas");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarLogDetalle, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
			ps.setDouble(1,secuenciaLogDetPrograma);
			ps.setDouble(2,dto.getCodigoPk());
			ps.setDouble(3, dto.getProgramas());
			ps.setInt(4, dto.getOrden());
			ps.setInt(5, dto.getServicio());
			ps.setString(6, dto.getActivo());
			ps.setString(7, dto.getEliminado());
			ps.setString(8, dto.getFecha());
			ps.setString(9, dto.getHora());
			ps.setString(10, dto.getUsuario());
			
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarLogDetalle==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	
	public static boolean eliminarDetallePrograma(double codigoDetalle)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarDetallePrograma, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,codigoDetalle);
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarDetallePrograma==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	public static boolean actualizarPrograma(DtoPrograma dto)
	{
		boolean transaccionExitosa=false;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strActualizarPrograma);
			
			ps.setString(1, dto.getCodigoPrograma());
			ps.setInt(2, dto.getInstitucion());
			ps.setString(3, dto.getNombre());
			ps.setString(4, dto.getActivo());
			ps.setString(5, dto.getFechaModifica());
			ps.setString(6, dto.getHoraModifica());
			ps.setString(7, dto.getUsuarioModifica());
			ps.setInt(8, dto.getEspecialidad());
			if(dto.getDtoConvencion().getConsecutivo()<=0)
				ps.setNull(9, Types.NULL);
			else
				ps.setInt(9, dto.getDtoConvencion().getConsecutivo());
			ps.setDouble(10,dto.getCodigo());
			
			logger.info("\n\nconsulta modificar programas: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizarPrograma==> "+e);
		}
		return transaccionExitosa;
	}
	
	public static boolean insertarLogPrograma (DtoPrograma dto)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			double secuenciaLogPrograma=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_programas");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertarLogPrograma);
						
			ps.setDouble(1,secuenciaLogPrograma);
			ps.setDouble(2,dto.getCodigo());
			ps.setString(3, dto.getCodigoPrograma());
			ps.setString(4, dto.getNombre());
			ps.setString(5, dto.getActivo());
			ps.setInt(6, dto.getEspecialidad());
			ps.setString(7, dto.getEliminado());
			ps.setString(8, dto.getFechaModifica());
			ps.setString(9, dto.getHoraModifica());
			ps.setString(10, dto.getUsuarioModifica());
			
			logger.info("\n\nconsulta insertar log: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarLogPrograma==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	
	public static boolean eliminarPrograma(double programa)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strEliminarPrograma);
			ps.setDouble(1,programa);
			logger.info("\n\nconsulta eliminar programas odontologicos:: "+ps);
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarPrograma==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	public static boolean insertarDetalle (DtoDetalleProgramas dto)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			double secuenciaDetalle=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_detalle_programas");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalle, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
			ps.setDouble(1,secuenciaDetalle);
			ps.setDouble(2,dto.getProgramas());
			ps.setInt(3, dto.getOrden());
			ps.setInt(4, dto.getServicio());
			ps.setString(5, dto.getActivo());
			ps.setString(6, dto.getFecha());
			ps.setString(7, dto.getHora());
			ps.setString(8, dto.getUsuario());
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarPrograma==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	
	
	/**
	 * Metodo que cargar los Detalles de un Programa Odontologico
	 * @param programa
	 * @param codigoManualBusqueda
	 * @return
	 */
	public static ArrayList<DtoDetalleProgramas> cargarDetallePrograma (double programa, String codigoManualBusqueda)
	{
		logger.info("EL TARIFARIO OFICIAL QUE LLEGA AHSTA ACA----->"+codigoManualBusqueda);
		
		String strConsultaDetallePrograma=	"SELECT " +
												"dp.codigo_pk AS codigo," +
												"dp.programas," +
												"dp.orden," +
												"dp.servicio," +
												"dp.activo," +
												"dp.fecha," +
												"dp.hora," +
												"dp.usuario,";
												
												
											if (codigoManualBusqueda.equals("")||codigoManualBusqueda.equals(ConstantesBD.codigoTarifarioCups))
											{
												strConsultaDetallePrograma+="getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups+") AS descservicio, "+
																			"getcodigocups(servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigocups," +
																			"getnombretarfioofi("+ConstantesBD.codigoTarifarioCups+") AS acronimotarifario ";								
											}
											else
											{
												strConsultaDetallePrograma+="getnombreservicio(servicio,"+codigoManualBusqueda+") AS descservicio, "+
																			"getcodigocups(servicio,"+codigoManualBusqueda+") AS codigocups, "+
																			"getnombretarfioofi("+codigoManualBusqueda+") AS acronimotarifario ";
											}
												
											strConsultaDetallePrograma+="FROM " +
																			"odontologia.detalle_programas dp ";
		
		
		ArrayList<DtoDetalleProgramas> listadoDetalleProgramas= new ArrayList<DtoDetalleProgramas>();
		String consulta=strConsultaDetallePrograma;
		consulta+="WHERE programas=? ORDER BY orden";
		
		logger.info("cargarDetallePrograma-->"+consulta+" --> "+programa+" Codigo busqueda programa--->"+codigoManualBusqueda);
		
		Connection con = UtilidadBD.abrirConexion();
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,programa);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoDetalleProgramas dto=new DtoDetalleProgramas();
				dto.setCodigoPk(rs.getDouble("codigo"));
				dto.setProgramas(rs.getDouble("programas"));
				dto.setOrden(rs.getInt("orden"));
				dto.setServicio(rs.getInt("servicio"));
				dto.setActivo(rs.getString("activo"));
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setUsuario(rs.getString("usuario"));
				dto.setDescripcionServicio(rs.getString("descservicio"));
				dto.setCodigoCUPS(rs.getString("codigocups"));
				dto.setAcronimoTarifario(rs.getString("acronimotarifario"));
				listadoDetalleProgramas.add(dto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN cargarDetallePrograma==> "+e);
		}
		
		return listadoDetalleProgramas;
	}
	
	
	public static boolean insertarPrograma(DtoPrograma dto)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			double secuenciaPrograma=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_programas");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarPrograma, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,secuenciaPrograma);
			ps.setString(2, dto.getCodigoPrograma());
			ps.setInt(3, dto.getInstitucion());
			ps.setString(4, dto.getNombre());
			ps.setString(5, dto.getActivo());
			ps.setString(6, dto.getFechaModifica());
			ps.setString(7, dto.getHoraModifica());
			ps.setString(8, dto.getUsuarioModifica());
			ps.setInt(9, dto.getEspecialidad());
			if (dto.getDtoConvencion().getConsecutivo()!=ConstantesBD.codigoNuncaValido)
				ps.setInt(10, dto.getDtoConvencion().getConsecutivo());
			else
				ps.setNull(10, Types.INTEGER);
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return transaccionExitosa=true;
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarPrograma==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPrograma> buscarProgramasXEspecialidad (DtoPrograma dto)
	{
		ArrayList<DtoPrograma> listadoProgramas= new ArrayList<DtoPrograma>();
		
		String consulta=strConsultaProgramas;
		consulta+=UtilidadTexto.isEmpty(dto.getEspecialidad())?" ":" WHERE especialidad="+dto.getEspecialidad()+"";
		consulta+="	ORDER BY p.codigo_programa,p.nombre ";
		logger.info("LA CONDULTA----->"+consulta);
		Connection con = UtilidadBD.abrirConexion();
		 try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPrograma dtoPrograma= new DtoPrograma();
				dtoPrograma.setCodigo(rs.getDouble("codigo"));
				dtoPrograma.setCodigoPrograma(rs.getString("codprograma"));
				dtoPrograma.setInstitucion(rs.getInt("institucion"));
				dtoPrograma.setNombre(rs.getString("nombre"));
				dtoPrograma.setActivo(rs.getString("activo"));
				dtoPrograma.setFechaModifica(rs.getString("fechamodifica"));
				dtoPrograma.setHoraModifica(rs.getString("horamodifica"));
				dtoPrograma.setUsuarioModifica("usuariomodifica");
				dtoPrograma.setEspecialidad(rs.getInt("especialidad"));
				dtoPrograma.setNombreEspecialidad(rs.getString("nombreesp"));
				dtoPrograma.getDtoConvencion().setConsecutivo(rs.getInt("convencion"));
				dtoPrograma.getDtoConvencion().setCodigo(rs.getString("codconvencion"));
				dtoPrograma.getDtoConvencion().setNombre(rs.getString("nomconvencion"));
				dtoPrograma.getDtoConvencion().setArchivoConvencion(rs.getString("archivo"));
				listadoProgramas.add(dtoPrograma);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN buscarProgramasXEspecialidad==> "+e);
		}
		
		return listadoProgramas;
	}
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public static String obtenerNombrePrograma(double codigoPk)
	{
		String nombre="";
		String consultaStr=	"SELECT " +
								"nombre " +
							"FROM " +
								"odontologia.programas WHERE codigo=? ";
		
		try 
		{
			
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,codigoPk);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				nombre=rs.getString(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}	
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerNombrePrograma==> "+e);
		}
		
		return	nombre;					
	}
	
	/**
	 * 
	 */
	public static boolean existeProgramaConNombre (String nombrePrograma)
	{
		String consultaStr=	"SELECT " +
								"codigo " +
							"FROM " +
								"odontologia.programas WHERE codigo_programa=? ";
		
		boolean existe=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, nombrePrograma);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			 {
				existe=true;
			 }
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("EXISTE----->"+existe+"  ***** "+nombrePrograma);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN existeProgramaConNombre==> "+e);
		}
		return existe;
			
	}
	
}