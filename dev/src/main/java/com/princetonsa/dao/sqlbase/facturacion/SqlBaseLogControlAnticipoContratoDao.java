package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadLog;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.facturacion.DtoLogControlAnticipoContrato;

public class SqlBaseLogControlAnticipoContratoDao {

	

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseControlAnticiposContratoDao.class);
	
	/**
	 * CADENA para insertar
	 */
	 

	
	
	 
	private static String inserccionStr = "INSERT INTO facturacion.log_control_anticipos_contrato (   " +
	                                                                                "codigo , " +//1
	                                                                                "control_anticipos_contrato , " +//2
																					"valor_anticipo_cont_conv , " +//3
																					"valor_anticipo_cont_conv_ant , " +//4
																					"valor_ant_res_pre_cont_pac , " +//5
																					"valor_ant_rec_convenio , " +//6
																					"valor_ant_uti_fac_pac , " +//7
																					"num_total_pacientes , " +//8
																					"valor_max_pac , " +//9
																					"num_pac_atendidos , " +//10
																					"num_pac_x_atender , " +//11
																					"fecha_modifica , " +//12
																					"hora_modifica , " +//13
																					"req_ant_cont_pre , " +//14
																					"eliminado , " +//15
																					"usuario_modifica ) " +//16
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
																					"? , " +//14
																					"? , " +//15
																					"? ) ";//16
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoLogControlAnticipoContrato dto) 
	{
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"facturacion.seq_log_control_anticipos_con");
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info(">>> Valores a insertar:" +
					"\n>>> 1. Secuencia = "+secuencia+
					"\n>>> 2. Control anticipo contrato = "+dto.getControlAnticipoContrato()+
					"\n>>> 3. Valor anticipo contrato convenio = "+dto.getValorAnticipoContratadoConvenio()+
					"\n>>> 4. Valor anticipo reservado convenio = "+dto.getValorAnticipoContratadoConvenioAnterior()+
					"\n>>> 5. Valor Anticipo Reservado Convenio ="+dto.getValorAnticipoReservadoConvenio()+
					"\n>>> 6. Valor Anticipo Recibido Convenio ="+dto.getValorAnticipoRecibidoConvenio()+
					"\n>>> 7. Valor Anticipo Utilizado ="+dto.getValorAnticipoUtilizado()+
					"\n>>> 8. Valor Anticipo Disponible ="+dto.getValorAnticipoDisponible()+
					"\n>>> 9. Numero Total Pacientes ="+dto.getNumeroTotalPacientes()+
					"\n>>> 10. Numero Maximo Paciente ="+dto.getNumeroMaximoPaciente()+
					"\n>>> 11. Numero Pacientes Atendidos ="+dto.getNumeroPacientesAtendidos()+
					"\n>>> 12. Numero Pacientes X Atender ="+dto.getNumeroPacientesXAtender()+
					"\n>>> 13. Fecha Modifica FromatoBD ="+dto.getFechaModificaFromatoBD()+
					"\n>>> 14. Hora Modifica ="+dto.getHoraModifica()+
					"\n>>> 15. Requiere Anticipo ="+dto.getRequiereAnticipo()+
					"\n>>> 16. Eliminado ="+dto.getEliminado()+
					"\n>>> 17. UsuarioModifica ="+dto.getUsuarioModifica());
			
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, dto.getControlAnticipoContrato());		    
		    ps.setDouble(3, dto.getValorAnticipoContratadoConvenio());
		    ps.setDouble(4, dto.getValorAnticipoContratadoConvenioAnterior());
		    
		    if(dto.getValorAnticipoReservadoConvenio() > 0)
			{	
				ps.setDouble(5, dto.getValorAnticipoReservadoConvenio());
			}	
			else
			{	
				ps.setNull(5, Types.DOUBLE);
			}	
		    
		   
		    if(dto.getValorAnticipoRecibidoConvenio() > 0)
			{	
				ps.setDouble(6, dto.getValorAnticipoRecibidoConvenio());
			}	
			else
			{	
				ps.setNull(6, Types.DOUBLE);
			}	
		    
		    if(dto.getValorAnticipoUtilizado() > 0)
			{	
				ps.setDouble(7, dto.getValorAnticipoUtilizado());
			}	
			else
			{	
				ps.setNull(7, Types.DOUBLE);
			}	
		    
		    if(dto.getNumeroTotalPacientes() > 0)
			{	
				ps.setDouble(8, dto.getNumeroTotalPacientes());
			}	
			else
			{	
				ps.setNull(8, Types.DOUBLE);
			}
		    
		    if(dto.getNumeroMaximoPaciente() > 0)
			{	
				ps.setDouble(9, dto.getNumeroMaximoPaciente());
			}	
			else
			{	
				ps.setNull(9, Types.DOUBLE);
			}
		    
		    if(dto.getNumeroPacientesAtendidos() > 0)
			{	
				ps.setDouble(10, dto.getNumeroPacientesAtendidos());
			}	
			else
			{	
				ps.setNull(10, Types.DOUBLE);
			}
		    
		    if(dto.getNumeroPacientesXAtender() > 0)
			{	
				ps.setDouble(11, dto.getNumeroPacientesXAtender());
			}	
			else
			{	
				ps.setNull(11, Types.DOUBLE);
			}
			ps.setString(12, dto.getFechaModificaFromatoBD());
			ps.setString(13, dto.getHoraModifica());
			ps.setString(14, dto.getRequiereAnticipo());
			ps.setString(15, dto.getEliminado());
			ps.setString(16, dto.getUsuarioModifica());
			logger.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getCodigo();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert cno log ant tar " + e + secuencia);
		}
		return secuencia;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoLogControlAnticipoContrato> cargar(DtoLogControlAnticipoContrato dtoWhere) 
	{
		ArrayList<DtoLogControlAnticipoContrato> arrayDto = new ArrayList<DtoLogControlAnticipoContrato>();
		String consultaStr = 	"SELECT " +
										"lcac.codigo as codigo , " +
										"lcac.control_anticipos_contrato as control_anticipos_contrato , "+
										"lcac.valor_anticipo_cont_conv as valor_anticipo_cont_conv , "+
										"lcac.valor_anticipo_cont_conv_ant as valor_anticipo_cont_conv_ant, "+ 
										"lcac.fecha_modifica as fecha_modifica , " +
										"lcac.hora_modifica as hora_modifica , " +
										"lcac.usuario_modifica as usuario_modifica " +
									"from " +
										"  facturacion.log_control_anticipos_contrato lcac  " +
									"where " +
										"1=1 ";
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and lcac.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getControlAnticipoContrato()>ConstantesBD.codigoNuncaValido)?" and lcac.control_anticipos_contrato= "+dtoWhere.getControlAnticipoContrato():"";
		
		
		consultaStr+=" order by lcac.codigo asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar log cont ant/ " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoLogControlAnticipoContrato dto = new DtoLogControlAnticipoContrato();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setControlAnticipoContrato(rs.getDouble("control_anticipos_contrato"));
				dto.setValorAnticipoContratadoConvenio(rs.getDouble("valor_anticipo_cont_conv"));
				dto.setValorAnticipoContratadoConvenioAnterior(rs.getDouble("valor_anticipo_cont_conv_ant"));
				
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga log con ==> " + e);
		}
		return arrayDto;
	}
	
	
}
