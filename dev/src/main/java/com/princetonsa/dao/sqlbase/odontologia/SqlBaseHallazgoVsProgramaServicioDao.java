package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoHallazgoVsProgramaServicio;


public class SqlBaseHallazgoVsProgramaServicioDao {
	
	

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseHallazgoVsProgramaServicioDao.class);
	
	/**
	 * CADENA para insertar
	 */
	 

    
	
	
	private static String inserccionStr = "INSERT INTO odontologia.hallazgos_vs_prog_ser (   " +
																					"codigo , " +//1
																					"hallazgo , " +//2
																					"institucion  , " +//3
																					
																					"fecha_modifica , " +//4
																					"hora_modifica , " +//5
																					
																					"usuario_modifica ) " +//6
																		"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? ) ";//6


	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoHallazgoVsProgramaServicio dtoNuevo, DtoHallazgoVsProgramaServicio dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.hallazgos_vs_prog_ser set codigo=codigo";
		consultaStr+= (dtoNuevo.getHallazgo().getCodigo() > ConstantesBD.codigoNuncaValido)?" , hallazgo= "+dtoNuevo.getHallazgo().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica()+"' ":"";
		consultaStr+=" where 1=1 ";
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			logger.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Emision Targeta Odontologia" + e);
			
		}
		return retorna;
	}

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHallazgoVsProgramaServicio> cargar(DtoHallazgoVsProgramaServicio dtoWhere) 
	{
		ArrayList<DtoHallazgoVsProgramaServicio> arrayDto = new ArrayList<DtoHallazgoVsProgramaServicio>();
		String consultaStr = 	"SELECT " +
										"hvps.codigo as codigo , " +
										"hvps.hallazgo as hallazgo , "+
										"hvps.institucion as institucion , "+
										"h.nombre as nombre_hallazgo ," +
										"h.codigo as codigohallazgo, "+ 
										"hvps.fecha_modifica as fecha_modifica , " +
										"hvps.hora_modifica as hora_modifica , " +
										"hvps.usuario_modifica as usuario_modifica," +
										"h.aplica_a as aplicaA , " +
										" CASE WHEN " +
										" (" +
										"	select count(0)  from  odontologia.det_plan_tratamiento dpt where dpt.hallazgo=hvps.hallazgo " +
										" )>0 THEN 'S' ELSE 'N' END AS existenPlan  "+
									
									"FROM  " +
										" odontologia.hallazgos_vs_prog_ser  hvps INNER JOIN  odontologia.hallazgos_odontologicos h ON(h.consecutivo= hvps.hallazgo) " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and hvps.codigo= "+dtoWhere.getCodigo():"";
		
		consultaStr+= (dtoWhere.getHallazgo().getCodigo() > ConstantesBD.codigoNuncaValido)?" and hvps.hallazgo= "+dtoWhere.getHallazgo().getCodigo():"";
		consultaStr+= (dtoWhere.getInstitucion() > ConstantesBD.codigoNuncaValido)?" and hvps.institucion = "+dtoWhere.getInstitucion():"";
		
		//consultaStr+=" order by hvps.codigo desc";
		//en la tarea 134779, se solicita mostrar en el codigo, el codigo_hallazgo, entonces se debe ordenar por este codigo.
		//consultaStr+=" order by h.codigo desc";
		
		//en la tarea 8264, se modifican las tareas anteriores y se solicita que se ordene por descripcion asc
		consultaStr+=" order by h.nombre ASC";
		
		Log4JManager.info(consultaStr);
		
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar halla vs / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoHallazgoVsProgramaServicio dto = new DtoHallazgoVsProgramaServicio();
				dto.setCodigo(rs.getDouble("codigo"));
			    dto.setInstitucion(rs.getInt("institucion"));
			    dto.setHallazgo(new InfoDatosDouble(rs.getDouble("hallazgo"),rs.getString("nombre_hallazgo")));
			    dto.getHallazgo().setDescripcion(rs.getString("codigohallazgo"));
				dto.getDatosfechaUsuarioModifica().setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.getDatosfechaUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
				dto.setAplicaSuperificie(rs.getString("aplicaA"));
				dto.getDatosfechaUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setExiteEnPlan(rs.getString("existenPlan"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga hall vs Odon==> " + e);
		}
		return arrayDto;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoHallazgoVsProgramaServicio dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.hallazgos_vs_prog_ser  WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		
		logger.info("elmi-->"+consultaStr);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= null;
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar  emi tar odo "+ e);
		
		}
		return false;
	}

	
	/**
	 * Método encargado de realizar el proceso de guardar
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoHallazgoVsProgramaServicio dto) 
	{
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_hallazgos_vs_prog_ser");
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("EL VALOR DE LA SECUENCIA ES ************"+ secuencia);
			
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, dto.getHallazgo().getCodigo());		    
		    ps.setInt(3, dto.getInstitucion());	
			ps.setString(4, dto.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(5, dto.getDatosfechaUsuarioModifica().getHoraModifica());
			ps.setString(6, dto.getDatosfechaUsuarioModifica().getUsuarioModifica());
			logger.info(inserccionStr);
			logger.info(">>> Secuencia = "+secuencia+
					"\n>>> Codigo = "+dto.getHallazgo().getCodigo()+
					"\n>>> Institución = "+dto.getInstitucion()+
					"\n>>> Fecha Modifica = "+dto.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD()+
					"\n>>> Hora Modifica = "+dto.getDatosfechaUsuarioModifica().getHoraModifica()+
					"\n>>> Usuario Modifica = "+dto.getDatosfechaUsuarioModifica().getUsuarioModifica());
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getCodigo();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert halla vs " + e + dto.getCodigo());
		}
		logger.info(">>> El valor del código a retornar es: "+dto.getCodigo());
		return dto.getCodigo();
	}
	
	
	
	
	
	
	
	
	
	
}
