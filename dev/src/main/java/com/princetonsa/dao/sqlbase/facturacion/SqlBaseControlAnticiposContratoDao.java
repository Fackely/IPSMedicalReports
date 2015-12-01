package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadLog;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

public class SqlBaseControlAnticiposContratoDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseControlAnticiposContratoDao.class);
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = "INSERT INTO facturacion.control_anticipos_contrato (   " +
	                                                                                "codigo , " +//1
	                                                                                "contrato , " +//2
																					"valor_anticipo_cont_conv , " +//3
																					"valor_ant_res_pre_cont_pac , " +//4
																					"valor_ant_rec_convenio , " +//5
																					"valor_ant_uti_fac_pac , " +//6
																					"num_total_pacientes , " +//7
																					"valor_max_pac , " +//8
																					"num_pac_atendidos , " +//9
																					"num_pac_x_atender , " +//10
																					"fecha_modifica , " +//11
																					"hora_modifica , " +//12
																					"req_ant_cont_pre , " +//13
																					"usuario_modifica ) " +//14
																		"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? , " +//6
																					"? , " +//7
																					"? , " +//8
																					"? , " +//9
																					"? , " +//10
																					"? , " +//11
																					"? , " +//12
																					"? , " +//13
																					"? ) ";//14


	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoControlAnticiposContrato dtoNuevo, DtoControlAnticiposContrato dtoWhere) 
	{
		boolean retorna=false;
		logger.info("\n\n\n\n\n\n\n\n");
		
		String consultaStr = "UPDATE facturacion.control_anticipos_contrato  set codigo=codigo";
		
		consultaStr+=((!dtoNuevo.getRequiereAnticipo().equals(""))?", req_ant_cont_pre= '"+dtoNuevo.getRequiereAnticipo()+"'":" ");
		consultaStr+= ((dtoNuevo.getValorAnticipoContratadoConvenio()) > ConstantesBD.codigoNuncaValido)?" , valor_anticipo_cont_conv= "+dtoNuevo.getValorAnticipoContratadoConvenio():"";
		consultaStr+= ((dtoNuevo.getNumeroTotalPacientes()) >  0 )?" , num_total_pacientes= "+dtoNuevo.getNumeroTotalPacientes():", num_total_pacientes = null";
		consultaStr+= ((dtoNuevo.getNumeroMaximoPaciente()) >  0 )?" , valor_max_pac= "+dtoNuevo.getNumeroMaximoPaciente():", valor_max_pac = null";
		//consultaStr+= " , req_ant_cont_pre='"+dtoNuevo.getRequiereAnticipo()+"'";
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getContrato()>ConstantesBD.codigoNuncaValido)?" and contrato= "+dtoWhere.getContrato():"";
		
		logger.info("LA CONSULTA------>"+consultaStr);
		logger.info("\n\n\n\n\n\n\n\n");

		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			logger.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() > 0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Cont Odontologia" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	
	public static ArrayList<DtoControlAnticiposContrato> cargar(Connection con, DtoControlAnticiposContrato dtoWhere) throws BDException
	{
		ArrayList<DtoControlAnticiposContrato> arrayDto = new ArrayList<DtoControlAnticiposContrato>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio cargar");
			String consultaStr = 	"SELECT "+
						"cac.codigo as codigo , " +
						"cac.contrato as contrato , "+
						"cac.valor_anticipo_cont_conv as valor_anticipo_cont_conv , "+
						"cac.valor_ant_res_pre_cont_pac as valor_ant_res_pre_cont_pac , "+
						"cac.valor_ant_rec_convenio as valor_ant_rec_convenio , "+
						"cac.valor_ant_uti_fac_pac  as valor_ant_uti_fac_pac  , "+
						"(coalesce(cac.valor_ant_rec_convenio,0) - coalesce(cac.valor_ant_res_pre_cont_pac, 0) -   coalesce(cac.valor_ant_uti_fac_pac, 0) ) as valor_antdisponible   , "+
						"cac.num_total_pacientes   as num_total_pacientes   , "+
						"coalesce(cac.valor_max_pac ,"+ConstantesBD.codigoNuncaValido+")   as valor_max_pac  , "+
						"cac.num_pac_atendidos   as num_pac_atendidos  , "+
						"cac.num_pac_x_atender   as num_pac_x_atender  , "+
						"cac.req_ant_cont_pre   as req_ant_cont_pre  , "+
						"cac.fecha_modifica as fecha_modifica , " +
						"cac.hora_modifica as hora_modifica , " +
						"cac.usuario_modifica as usuario_modifica " +
					"from " +
						"  facturacion.control_anticipos_contrato cac  " +
					"where " +
						"1=1 ";
			consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and cac.codigo= "+dtoWhere.getCodigo():"";
			consultaStr+= (dtoWhere.getContrato()>ConstantesBD.codigoNuncaValido)?" and cac.contrato= "+dtoWhere.getContrato():"";
			consultaStr+=" order by cac.codigo asc"; 

			pst = con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			while (rs.next()) 
			{
				DtoControlAnticiposContrato dto = new DtoControlAnticiposContrato();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setContrato(rs.getDouble("contrato"));
				dto.setValorAnticipoContratadoConvenio(rs.getDouble("valor_anticipo_cont_conv"));
				dto.setValorAnticipoReservadoConvenio(rs.getDouble("valor_ant_res_pre_cont_pac"));
				dto.setValorAnticipoRecibidoConvenio(rs.getDouble("valor_ant_rec_convenio"));
				dto.setValorAnticipoUtilizado(rs.getDouble("valor_ant_uti_fac_pac"));
                dto.setValorAnticipoDisponible(rs.getDouble("valor_antdisponible"));
                dto.setNumeroTotalPacientes(rs.getDouble("num_total_pacientes"));
				dto.setNumeroMaximoPaciente(rs.getDouble("valor_max_pac"));
				dto.setNumeroPacientesAtendidos(rs.getDouble("num_pac_atendidos"));
				dto.setNumeroPacientesXAtender(rs.getDouble("num_pac_x_atender"));
				dto.setRequiereAnticipo(rs.getString("req_ant_cont_pre"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargar");
		return arrayDto;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoControlAnticiposContrato dtoWhere) 
	{
		String consultaStr = "DELETE FROM  facturacion.control_anticipos_contrato  WHERE 1=1 ";
		
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
			logger.error("ERROR EN eliminar  cont ant "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	public static double guardar(DtoControlAnticiposContrato dto, Connection con) 
	{
		logger.info(UtilidadLog.obtenerString(dto, true));
		logger.info(inserccionStr);
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		logger.info("secuencia"+ secuencia);
		try 
		{
			BigDecimal valor;
			
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"facturacion.seq_control_anticipos_con");
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuencia);		
			
			ps.setDouble(2, dto.getContrato());		    
		    ps.setDouble(3, dto.getValorAnticipoContratadoConvenio());	
		    
		    if(dto.getValorAnticipoReservadoConvenio() > 0)
			{	
				//Como el numero puede ser muy grande y a veces llega en exponencial, lo convierto a bigdecimal
		    	valor=new BigDecimal(dto.getValorAnticipoReservadoConvenio());
		    	ps.setBigDecimal(4, valor);
			}	
			else
			{	
				ps.setNull(4, Types.DOUBLE);
			}	
		    if(dto.getValorAnticipoRecibidoConvenio() > 0)
			{	
				ps.setDouble(5, dto.getValorAnticipoRecibidoConvenio());
			}	
			else
			{	
				ps.setNull(5, Types.DOUBLE);
			}	
		    
		    if(dto.getValorAnticipoUtilizado() > 0)
			{	
				ps.setDouble(6, dto.getValorAnticipoUtilizado());
			}	
			else
			{	
				ps.setNull(6, Types.DOUBLE);
			}	
		    
		    if(dto.getNumeroTotalPacientes() > 0)
			{	
				ps.setDouble(7, dto.getNumeroTotalPacientes());
			}	
			else
			{	
				ps.setNull(7, Types.DOUBLE);
			}
		    
		    if(dto.getNumeroMaximoPaciente() > 0)
			{	
				ps.setDouble(8, dto.getNumeroMaximoPaciente());
			}	
			else
			{	
				ps.setNull(8, Types.DOUBLE);
			}
		    	
		    
		    if(dto.getNumeroPacientesAtendidos() > 0)
			{	
				ps.setDouble(9, dto.getNumeroPacientesAtendidos());
			}	
			else
			{	
				ps.setNull(9, Types.DOUBLE);
			}
		    
		    if(dto.getNumeroPacientesXAtender() > 0)
			{	
				ps.setDouble(10, dto.getNumeroPacientesXAtender());
			}	
			else
			{	
				ps.setNull(10, Types.DOUBLE);
			}
		    
			ps.setString(11, dto.getFechaModificaFromatoBD());
			ps.setString(12, dto.getHoraModifica());
			ps.setString(13, dto.getRequiereAnticipo());
			ps.setString(14, dto.getUsuarioModifica());
 			
			int resp = ps.executeUpdate();
			
			if(resp>0)
				return secuencia;
			else
				return 0;
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert cno ant tar " + e + secuencia);
		}
		return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param valorAnticipoPresupuesto
	 * @return
	 */
	public static boolean modificarValorAnticipoReservadoPresupuesto(Connection con, int contrato, BigDecimal valorAnticipoPresupuesto)
	{
		boolean retorna=false;
		String consultaStr="update facturacion.control_anticipos_contrato set valor_ant_res_pre_cont_pac=(coalesce(valor_ant_res_pre_cont_pac,0)+?) where contrato=?";
		
		try 
		{
			logger.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, valorAnticipoPresupuesto);
			ps.setInt(2, contrato);
			
			logger.info("Modificar --->"+ps);
			retorna=ps.executeUpdate() > 0; 
			ps.close();
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Cont Odontologia" + e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param valorAnticipo
	 * @return
	 */
	public static boolean modificarValorAnticipoUtilizadoFactura(Connection con, int contrato, BigDecimal valorAnticipo)
	{
		boolean retorna=false;
		String consultaStr="update facturacion.control_anticipos_contrato set valor_ant_uti_fac_pac=(coalesce(valor_ant_uti_fac_pac,0)+?) where contrato=?";
		
		try 
		{
			logger.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, valorAnticipo);
			ps.setInt(2, contrato);
			retorna=ps.executeUpdate() > 0; 
			ps.close();
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Cont Odontologia" + e);
		}
		return retorna;
	}
}