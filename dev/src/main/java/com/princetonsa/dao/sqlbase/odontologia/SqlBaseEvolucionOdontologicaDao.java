package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;

public class SqlBaseEvolucionOdontologicaDao
{
	private static Logger logger = Logger.getLogger(SqlBaseEvolucionOdontologicaDao.class);
	
	private static String insertarEvolucionOdon=	"INSERT INTO odontologia.evoluciones_odo " +
													"(codigo_pk," +
													"cita," +
													"fecha_modifica," +
													"hora_modifica," +
													"usuario_modifica) " +
													"VALUES " +
													"(?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	private static String consultarEvolucion	=	"SELECT " +
														"eo.codigo_pk," +
														"eo.cita," +
														"eo.fecha_modifica," +
														"eo.hora_modifica as hora," +
														"eo.usuario_modifica as usuario," +
														"pe.plantilla, " +
														"pe.codigo_pk as plantilla_evolucion," +
														"pe.usuario_creacion " +
													"FROM " +
														"odontologia.evoluciones_odo eo " +
													"INNER JOIN " +
														"odontologia.citas_odontologicas co ON (co.codigo_pk=eo.cita) " +
													"INNER JOIN " +
														"historiaclinica.plantillas_evolucion pe ON (pe.evolucion_odonto=eo.codigo_pk) " +
													"WHERE " +
														"eo.cita=? AND " +
														"co.codigo_paciente=? ";
	
	private static String strActualizarEvolucion	=	"UPDATE odontologia.evoluciones_odo " +
															"SET " +
															"cita=?," +
															"fecha_modifica=current_date," +
															"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
															"usuario_modifica=? " +
															"WHERE codigo_pk=? ";
	
	/**
	 * Cadena que realiza la consulta de la evolucion
	 */
	private static String consultarStr = "SELECT "+
		"eo.codigo_pk as codigo_pk, "+
		"eo.cita as cita, "+
		"coalesce(to_char(eo.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_modifica, "+
		"coalesce(eo.hora_modifica,'') as hora, "+
		"coalesce(eo.usuario_modifica,'') as usuario "+ 
		"FROM odontologia.evoluciones_odo eo "+ 
		"WHERE eo.codigo_pk = ?";
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double insertarEvolucionOdon(DtoEvolucionOdontologica dto,Connection con)
	{
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			
			logger.info("****************codigopk********* antes de if **** "+dto.getCodigoPk());
			if (dto.getCodigoPk()<=0)
			{
				secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_evolucion_odo");
				PreparedStatementDecorator psd=new PreparedStatementDecorator(con, insertarEvolucionOdon);
				
				logger.info("datos a insertar:");
				logger.info("cita>> "+dto.getCita());
				logger.info("usuario Modifica>> "+dto.getUsuario());
				
				psd.setDouble(1, secuencia);
				psd.setDouble(2, dto.getCita());
				psd.setString(3, dto.getUsuario());
				
				if(psd.executeUpdate()>0)
				{
					psd.close();
					return secuencia;
				}
				else
				{
					psd.close();
					secuencia=ConstantesBD.codigoNuncaValidoDouble;
				}
			}
			else
			{
				PreparedStatementDecorator psd=new PreparedStatementDecorator(con, strActualizarEvolucion);
				
				psd.setDouble(1, dto.getCita());
				psd.setString(2, dto.getUsuario());
				psd.setDouble(3, dto.getCodigoPk());
				
				logger.info("**************** consulta ********"+psd);
				if(psd.executeUpdate()>0)
				{
					psd.close();
					return dto.getCodigoPk();
				}
				else
				{
					psd.close();
					secuencia=ConstantesBD.codigoNuncaValidoDouble;
				}
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarEvolucionOdon==> "+e);
		}
		return secuencia;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static ArrayList<DtoEvolucionOdontologica> consultarEvolucion(DtoEvolucionOdontologica dto)
	{
		ArrayList<DtoEvolucionOdontologica> listdoEvoluciones=new ArrayList<DtoEvolucionOdontologica>();
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			String consulta = consultarEvolucion;
			if(dto.getPlantilla()>0)
			{
				consulta += " AND pe.plantilla = "+dto.getPlantilla();
			}
			
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consulta);
			psd.setDouble(1, dto.getCita());
			psd.setInt(2, dto.getCodigoPaciente());
			
			logger.info("LA CONSUTLA EVOLUCIONES-->"+psd);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			 {
				DtoEvolucionOdontologica dtoEvo=new DtoEvolucionOdontologica();
				
				dtoEvo.setCodigoPk(rs.getDouble("codigo_pk"));
				dtoEvo.setCita(rs.getDouble("cita"));
				dtoEvo.setFechaModifica(rs.getString("fecha_modifica"));
				dtoEvo.setHora(rs.getString("hora"));
				dtoEvo.setUsuario(rs.getString("usuario"));
				dtoEvo.setPlantilla(rs.getInt("plantilla"));
				dtoEvo.setCodigoPlantillaEvolucion(rs.getBigDecimal("plantilla_evolucion"));
				dtoEvo.setUsuarioCreacion(rs.getString("usuario_creacion"));
				
				listdoEvoluciones.add(dtoEvo); 
			 }
			rs.close();
			psd.close();
			UtilidadBD.closeConnection(con);
		}
		
		
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarEvolucion==> "+e);
		}
		return listdoEvoluciones;
	}
	
	/**
	 * Método implementado para cargar la informacion de la evolucion
	 * @param con
	 * @param evolucionOdo
	 */
	public static void consultar(Connection con,DtoEvolucionOdontologica evolucionOdo)
	{
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consultarStr);
			pst.setBigDecimal(1, new BigDecimal(evolucionOdo.getCodigoPk()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				evolucionOdo.setCodigoPk(rs.getDouble("codigo_pk"));
				evolucionOdo.setCita(rs.getDouble("cita"));
				evolucionOdo.setFechaModifica(rs.getString("fecha_modifica"));
				evolucionOdo.setHora(rs.getString("hora"));
				evolucionOdo.setUsuario(rs.getString("usuario"));
				
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar: ",e);
		}
	}
}