package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoAgrupHonorariosPool;
import com.princetonsa.dto.odontologia.DtoHonorarioPoolServicio;
import com.princetonsa.dto.odontologia.DtoHonorariosPool;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseHonorariosPool 
{
	/**
	 * logger 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseHonorariosPool.class);
	
	/**
	 * 
	 */
	private static String insertarStrHonorarioPool="" +
			"INSERT INTO facturacion.honorarios_pool(" +	
			"codigo_pk , " +//1
			"pool, " +		//2
			"convenio, " + 	//3
			"esquema_tarifario," +//4
			"fecha_modifica ," + //5
			"hora_modifica, " +//6
			"usuario_modifica, " +//7
			"centro_atencion," +//8
			"es_convenio) " + //9
			"values (" +
			"?, ?, ? , " +//3
			"? , ? , ?, " +//6
			"? , ?, ?)";//9
	
	/**
	 * 
	 */
	private static String insertarStrAgrupacionHonorarios =" " +
			"INSERT INTO facturacion.agrup_honorarios_pool( "+
			"codigo_pk,"+
			"honorario_pool, "+ 
			"grupo_servicio, "+ 
			"tipo_servicio,  "+
			"porcentaje_participacion, "+ 
			"valor_participacion, "+
			"cuenta_contable_honorario, "+
			"cuenta_contable_ins, "+
			"cue_cont_inst_vig_anterior, "+ 
			"fecha_modifica , "+
			"hora_modifica , "+
			"usuario_modifica ) " +
			"VALUES" +
			" (? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? , ?) ";  
	
	
	
	/**
	 * 
	 */
	private static String insertStrHonorariosPoolServ="" +
			"INSERT INTO  facturacion.honorarios_pool_serv( " +
			"codigo_pk, "+  
			"honorario_pool, "+  
			"servicio, "+
			"porcentaje_participacion , "+
			"valor_participacion , "+
			"cuenta_contable_ins , "+
			"cue_cont_inst_vig_anterior , "+
			"cuenta_contable_honorario , "+
			"fecha_modifica , "+ 
			"hora_modifica , "+
			"usuario_modifica  )" +
			"VALUES (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
	
	
	
	
	
	/** Retorna la Secuencia.
	 * GUARDAR HONORARIOS POOLES  
	 * @param dto
	 * @return
	 */
	public static double guardarHonorariosPool(DtoHonorariosPool dto) 
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble; // falta
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_honorarios_pool_serv"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStrHonorarioPool);
			
			ps.setDouble(1, secuencia);
			ps.setInt(2, dto.getPool().getCodigo());
			
			
			if( dto.getConvenio().getCodigo()>0)
			{
				ps.setInt(3, dto.getConvenio().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.INTEGER);
			}
			
			if(dto.getEsquemaTarifario().getCodigo()>0)
			{
				ps.setInt(4, dto.getEsquemaTarifario().getCodigo());
			}
			else
			{
				ps.setNull(4, Types.NUMERIC);
			}
			
			
			ps.setString(5, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(6, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(7, dto.getUsuarioModifica().getUsuarioModifica());
			
			if(dto.getCentroAtencion().getCodigo()>0)
			{	
				ps.setInt(8, dto.getCentroAtencion().getCodigo());
			}	
			else
			{
				ps.setNull(8, Types.INTEGER);
			}
			
			ps.setString(9, UtilidadTexto.convertirSN(dto.isEsConvenio()+""));
			
			logger.info("\n\n\n\n\n\n");
			logger.info(ps);
			logger.info("\n\n\n\n\n");
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
				return secuencia;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		return secuencia;
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoHonorariosPool> cargarHonorariosPool( DtoHonorariosPool dto)
	{
		ArrayList<DtoHonorariosPool> listaHonoriarios = new ArrayList<DtoHonorariosPool>();
		
		String consultaStr="" +
						"select  hop.codigo_pk as codigoPk, " +
								" hop.pool as pool ," +
								" p.descripcion as descripcionPool,  " +
								" coalesce(hop.convenio, "+ConstantesBD.codigoNuncaValido+") as convenio, " +
								" case when hop.convenio is null then 'Todos' else getnombreconvenio(hop.convenio) end as nombreConvenio, " +
								" coalesce(hop.esquema_tarifario, "+ConstantesBD.codigoNuncaValido+") as esquemaTarifario, " +
								" case when hop.esquema_tarifario is null then 'Todos' else getnombreesquematarifario(hop.esquema_tarifario) end as nombreEsquemaTarifario , " +
								" hop.fecha_modifica as fechaModifica,  " +
								" hop.hora_modifica as horaModifica, " +
								" hop.usuario_modifica as usuarioModifica, " +
								" coalesce(hop.centro_atencion, "+ConstantesBD.codigoNuncaValido+") as centro_atencion, " +
								" case when hop.centro_atencion is null then 'Todos' else getnomcentroatencion(hop.centro_atencion) end as nom_centro_atencion  " +
								" FROM facturacion.honorarios_pool hop" +
								" INNER JOIN facturacion.pooles p on (hop.pool=p.codigo) " +
								" where es_convenio ='"+UtilidadTexto.convertirSN(dto.isEsConvenio()+"")+"' ";
		
		consultaStr+=(dto.getCodigoPk().doubleValue()>0)?" AND hop.codigo_pk= "+dto.getCodigoPk():" ";
		consultaStr+=(dto.getPool().getCodigo()>0) ?" AND hop.pool="+dto.getPool().getCodigo():" ";
		
		//todos
		if(dto.isEsConvenio())
		{	
			if(dto.getConvenio().getCodigo()==ConstantesBD.codigoNuncaValido)
			{
				consultaStr+=" and hop.convenio = "+dto.getConvenio().getCodigo();
			}
			else
			{	
				consultaStr+=(dto.getConvenio().getCodigo()>0)?" AND hop.convenio="+dto.getConvenio().getCodigo(): " ";
			}	
		}
		else
		{
			if(dto.getEsquemaTarifario().getCodigo()==ConstantesBD.codigoNuncaValido)
			{
				consultaStr+=" and hop.esquema_tarifario = "+dto.getEsquemaTarifario().getCodigo();
			}
			else
			{	
				consultaStr+=(dto.getEsquemaTarifario().getCodigo()>0)?" and hop.esquema_tarifario="+dto.getEsquemaTarifario().getCodigo(): " ";
			}	
		}	
		
		if(dto.getCentroAtencion().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and hop.centro_atencion is null ";
		}	
		else
		{		
			consultaStr+=(dto.getCentroAtencion().getCodigo()>0)?" and hop.centro_atencion = "+dto.getCentroAtencion().getCodigo():" ";
		}	
		
		consultaStr+=" ORDER BY p.descripcion, "+(dto.isEsConvenio()?"getnombreconvenio(hop.convenio),": "getnombreesquematarifario(hop.esquema_tarifario),")+" getnomcentroatencion(hop.centro_atencion) ";
		
		logger.info("**********************************************************************************************");
		logger.info("------------------------------------CARGAR HONORARIOS POOL------------------------------------------");
		logger.info(consultaStr);
		logger.info("**********************************************************************************************");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());

			while(rs.next())
			{
				DtoHonorariosPool newDto = new  DtoHonorariosPool();
				newDto.setCodigoPk(rs.getBigDecimal("codigoPk"));
				newDto.setPool(new InfoDatosInt(rs.getInt("pool"), rs.getString("descripcionPool")));
				newDto.setConvenio(new InfoDatosInt(rs.getInt("convenio"), rs.getString("nombreConvenio")));
				newDto.setEsquemaTarifario(new InfoDatosInt(rs.getInt("esquemaTarifario"), rs.getString("nombreEsquemaTarifario")));
				newDto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				newDto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newDto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				newDto.setEsConvenio(dto.isEsConvenio());
				newDto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"), rs.getString("nom_centro_atencion")));
				listaHonoriarios.add(newDto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN ==> ",e);
		}
		
		return listaHonoriarios;
	} 
	
	/**
	 * 
	 * @param pool
	 * @param convenioEXCLUYENTE
	 * @param esquemaEXCLUYENTE
	 * @param centroAtencion
	 * @param codigoPkNOT_IN
	 * @return
	 */
	public static boolean existeHonorarioPool(int pool, int convenioEXCLUYENTE, int esquemaEXCLUYENTE, int centroAtencion, BigDecimal codigoPkNOT_IN)
	{
		boolean retorna=false;
		String consultaStr="SELECT count(1) from facturacion.honorarios_pool where pool=? ";
		
		if(convenioEXCLUYENTE==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and convenio is null ";
		}
		else
		{	
			consultaStr+=(convenioEXCLUYENTE>0)?" and convenio="+convenioEXCLUYENTE+" ":"";
		}
		if(esquemaEXCLUYENTE==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and esquema_tarifario is null ";
		}
		else
		{	
			consultaStr+=(esquemaEXCLUYENTE>0)?" and esquema_tarifario="+esquemaEXCLUYENTE+" ":"";
		}
		if(centroAtencion==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and centro_atencion is null ";
		}
		else
		{	
			consultaStr+=(centroAtencion>0)?" and centro_atencion="+centroAtencion+" ":"";
		}
		consultaStr+=" and codigo_pk <> "+codigoPkNOT_IN;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, pool);
			
			logger.info(ps.toString());
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());

			if(rs.next())
			{
				retorna= rs.getInt(1)>0;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN ==> ",e);
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarHonorarioPool(DtoHonorariosPool dto)
	{
		boolean retorna=false;
		String consultaStr=	"UPDATE facturacion.honorarios_pool SET " +
							"pool=?, " +//1
							"fecha_modifica= CURRENT_DATE, " +
							"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
							"usuario_modifica='"+dto.getUsuarioModifica().getUsuarioModifica()+"', " +
							"centro_atencion=?, " +//2
							"convenio=?, " + 
							"esquema_tarifario=? ";
		
		
		//consultaStr+= dto.isEsConvenio()?" convenio= "+dto.getConvenio().getCodigo():" esquema_tarifario= "+dto.getEsquemaTarifario().getCodigo();
		
		consultaStr+=" WHERE codigo_pk= ? "; //3
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, dto.getPool().getCodigo());
			
			if(dto.getCentroAtencion().getCodigo()>0)
			{	
				ps.setInt(2, dto.getCentroAtencion().getCodigo());
			}
			else
			{
				ps.setNull(2, Types.INTEGER);		
			}			
			
			/**
			 * Se valida si para el convenio/esquema tarifario el valor es -1
			 * Incidencia 449 Mantis.
			 * @author Diana Ruiz
			 */
			
			if( dto.getConvenio().getCodigo()>0)
			{
				ps.setInt(3, dto.getConvenio().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.INTEGER);
			}		
			if(dto.getEsquemaTarifario().getCodigo()>0)
			{
				ps.setInt(4, dto.getEsquemaTarifario().getCodigo());
			}
			else
			{
				ps.setNull(4, Types.NUMERIC);
			}
					
			ps.setBigDecimal(5, dto.getCodigoPk());
			ps.executeUpdate();
			retorna= true;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN ==> ",e);
		}
		return retorna;
		
	}
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public static boolean eliminarHonorarioPool (BigDecimal codigoPk)
	{
		boolean retorna= false;
		String consultaStr="DELETE FROM facturacion.honorarios_pool WHERE codigo_pk=?";
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPk);
			if(ps.executeUpdate()>0)
			{
				retorna=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN ==> ",e);
		}
		return retorna;
	}
	
	/**
	 * GUARDAR HONORARIOS POOLES  
	 * @param dto
	 * @return
	 */
	public static double guardarHonoriarioPoolServicio(Connection con,  DtoHonorarioPoolServicio dto) 
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble; 
		
		logger.info("\n\n GUARDAR HONORARIO POOL SERVICIO ");
		try 
		{
			ResultSetDecorator rs = null;
			
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_honorarios_pool_serv"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertStrHonorariosPoolServ);
	
			ps.setDouble(1, secuencia);
			ps.setBigDecimal(2,dto.getHonorarioPool());
			
			if(dto.getServicio().getCodigo()>0)
			{
				ps.setInt(3, dto.getServicio().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.INTEGER);
			}
			if(dto.getPorcentajeParticipacion()>=0)
			{
				ps.setDouble(4, dto.getPorcentajeParticipacion());
			}
			else
			{
				ps.setNull(4, Types.INTEGER);
			}
			
			if (dto.getValorParticipacion()!=null&&dto.getValorParticipacion().doubleValue()>=0)
			{
				ps.setBigDecimal(5, dto.getValorParticipacion());
			}
			else
			{
				ps.setNull(5, Types.NUMERIC);
			}
			
			if(dto.getCuentaContableIns().getCodigo() >0)
			{
				ps.setDouble(6, dto.getCuentaContableIns().getCodigo());
			}
			else
			{
				ps.setNull(6,Types.INTEGER);
			}
			
			if(dto.getCueContInstVigAnterior().getCodigo()>0)
			{
				ps.setDouble(7, dto.getCueContInstVigAnterior().getCodigo());
			}
			else
			{
				ps.setNull(7, Types.INTEGER);
			}
		
			
			
			if(dto.getCuentaContableHonorario().getCodigo()>0)
			{
				ps.setDouble(8,dto.getCuentaContableHonorario().getCodigo());
				
			}
			else
			{
				ps.setNull(8, Types.INTEGER);
			}
			
			ps.setString(9, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(10, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(11, dto.getUsuarioModifica().getUsuarioModifica());
			
			logger.info("\n\n\n\n\n");
			logger.info(ps);
			logger.info("\n\n\n\n\n");
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , null);
				return secuencia;
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , null);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		return secuencia;
	}
	
	/**
	 * MODIFICAR HONORARIO POOL SERVICIO
	 * @param dto
	 * @return
	 */
	public static  boolean modicarHonorarioPoolServicios(DtoHonorarioPoolServicio dto)
	{
		boolean retorna= true;
		String  updateStrPoolServicios= " " +
				"update facturacion.honorarios_pool_serv set  codigo_pk=codigo_pk , "+ 
				"honorario_pool =?, "+  
				"servicio=?, "+
				"porcentaje_participacion=? , "+
				"valor_participacion=? , "+
				"cuenta_contable_ins=? , "+
				"cue_cont_inst_vig_anterior=? , "+
				"cuenta_contable_honorario=? , "+
				"fecha_modifica=? , "+ 
				"hora_modifica=? , "+
				"usuario_modifica=?  where 1=1" ;
		updateStrPoolServicios+= (dto.getCodigoPk().doubleValue()>0) ?" and codigo_pk="+dto.getCodigoPk(): "";
		updateStrPoolServicios+=(dto.getHonorarioPool().doubleValue()>0)?" and  honorario_pool="+dto.getHonorarioPool(): "";
		logger.info("\n\n\n	MODIFICAR HONORARIO POOL SERVICIO ");
		logger.info(""+updateStrPoolServicios);
		logger.info("\n\n\n\n");
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, updateStrPoolServicios);
	
			ps.setBigDecimal(1,dto.getHonorarioPool());
			
			if(dto.getServicio().getCodigo()>0)
			{
				ps.setInt(2, dto.getServicio().getCodigo());
			}
			else
			{
				ps.setNull(2, Types.INTEGER);
			}
			if(dto.getPorcentajeParticipacion()>=0)
			{
				ps.setDouble(3, dto.getPorcentajeParticipacion());
			}
			else
			{
				ps.setNull(3, Types.DOUBLE);
			}
			
			if (dto.getValorParticipacion()!=null&&dto.getValorParticipacion().doubleValue()>=0)
			{
				ps.setBigDecimal(4, dto.getValorParticipacion());
			}
			else
			{
				ps.setNull(4, Types.NUMERIC);
			}
			
			if(dto.getCuentaContableIns().getCodigo() >0)
			{
				ps.setDouble(5, dto.getCuentaContableIns().getCodigo());
			}
			else
			{
				ps.setNull(5,Types.INTEGER);
			}
			
			if(dto.getCueContInstVigAnterior().getCodigo()>0)
			{
				ps.setDouble(6, dto.getCueContInstVigAnterior().getCodigo());
			}
			else
			{
				ps.setNull(6, Types.INTEGER);
			}
		
			
			
			if(dto.getCuentaContableHonorario().getCodigo()>0)
			{
				ps.setDouble(7,dto.getCuentaContableHonorario().getCodigo());
				
			}
			else
			{
				ps.setNull(7, Types.INTEGER);
			}
			
			ps.setString(8, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(9, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(10, dto.getUsuarioModifica().getUsuarioModifica());
			
			logger.info("\n\n\n\n\n");
			logger.info(ps);
			logger.info("\n\n\n\n\n");
			retorna=ps.executeUpdate()>0;
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en Modificar " + e);
		}
	
		return retorna;
		
	}
	
	
	/**
	 * ELIMINAR HONORARIO POOL SERVICIO
	 * @param dto
	 * @return
	 */
	public static boolean eliminarHonorariosPoolServicio(DtoHonorarioPoolServicio dto){
		
		boolean retorna =false;
		String eliminarStr="delete  from facturacion.honorarios_pool_serv where 1=1 ";
				eliminarStr+=" AND codigo_pk="+dto.getCodigoPk().doubleValue();
				eliminarStr+=( dto.getHonorarioPool().doubleValue()>0)? "and honorario_pool="+dto.getHonorarioPool():" ";
		
		logger.info("\n\n\n\n\n Eliminar Servicio");
		logger.info("\n\n\n\n\n"+eliminarStr);
		
		
		if(dto.getCodigoPk().doubleValue()<=0)
		{
			return false; 
		}
		
	    
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarStr);
			retorna=ps.executeUpdate()>0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+eliminarStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Programas  "+e);
			
		}
		return retorna;
		
	}
	
	/**
	 * ELIMINAR AGRUPACION DE HONORARIO
	 * @param dto
	 * @return
	 */
	public static boolean eliminarAgrupacionHonorario(DtoAgrupHonorariosPool dto){
		
		boolean retorna =false;
		String eliminarStr="delete  from facturacion.agrup_honorarios_pool where 1=1 ";
				eliminarStr+=" AND codigo_pk="+dto.getCodigoPk().doubleValue();
				eliminarStr+=" AND honorario_pool="+dto.getHonorarioPool();
		
		logger.info("\n\n\n\n\n Eliminar Agrupacion Servicio");
		logger.info("\n\n\n\n\n"+eliminarStr);
		
		if(dto.getCodigoPk().doubleValue()<=0)
		{
			return false; 
		}
		
	    
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarStr);
			retorna=ps.executeUpdate()>0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+eliminarStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Programas  "+e);
			
		}
		return retorna;
		
	}


	/**   
	 * CARGAR facturacion.honorarios_pool_serv
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoHonorarioPoolServicio> cargarHonorariosPoolServ( DtoHonorarioPoolServicio dto, int institucion){
		
		ArrayList<DtoHonorarioPoolServicio> listaHonoriarios = new ArrayList<DtoHonorarioPoolServicio>();
		
		String consultaStr="" +
								"select  hps.codigo_pk  as codigoPk," +
								" hps.honorario_pool as codigoPkHonorarioPool ," +
								" hps.servicio as servicio ," +
								" getnombreservicio(hps.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as descripcionServicio, " +
								" coalesce (hps.porcentaje_participacion,"+ConstantesBD.codigoNuncaValidoDoubleNegativo+")  as porcentajeParticipacion ," +
								" coalesce (hps.valor_participacion, "+ConstantesBD.codigoNuncaValido+")  as valorParticipacion," +
								" hps.cuenta_contable_ins as cuentaContableIns ," +
								" (select cu.cuenta_contable ||' '|| cu.descripcion from  interfaz.cuentas_contables cu   where cu.codigo=hps.cuenta_contable_ins) as nombreCuentaContableIns , " +
								" hps.cue_cont_inst_vig_anterior as cueContInsVigAnterior , " +
								" (select cu.cuenta_contable ||' '|| cu.descripcion from  interfaz.cuentas_contables cu   where cu.codigo=hps.cue_cont_inst_vig_anterior) as nombreCueContInstVigA," +
								" hps.cuenta_contable_honorario as cuentaContableHonorario ," +
								" (select cu.cuenta_contable ||' '|| cu.descripcion from  interfaz.cuentas_contables cu   where cu.codigo=hps.cuenta_contable_honorario) as nombreCuentaContableHonorario, " +
								" hps.fecha_modifica as fechaModifica ," +
								" hps.hora_modifica as horaModifica," +
								" hps.usuario_modifica as usuarioModifica " + 
								" FROM  facturacion.honorarios_pool_serv hps " +
								"where 1=1";
		
		consultaStr+=dto.getCodigoPk().doubleValue()>0?" AND hop.codigo_pk= "+dto.getCodigoPk():" ";
		consultaStr+=(dto.getHonorarioPool().doubleValue()>0)?" AND honorario_pool="+dto.getHonorarioPool():"";
		
		
		logger.info("**********************************************************************************************");
		logger.info("------------------------------------CARGAR HONORARIOS POOL SERVICIO------------------------------------------");
		logger.info(consultaStr);
		logger.info("**********************************************************************************************");
		
	
		Connection con = UtilidadBD.abrirConexion();
		
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			 {
				DtoHonorarioPoolServicio newDto = new  DtoHonorarioPoolServicio();
				newDto.setCodigoPk(rs.getBigDecimal("codigoPk"));
				newDto.setHonorarioPool(rs.getBigDecimal("codigoPkHonorarioPool"));
				newDto.setServicio(new  InfoDatosInt(rs.getInt("servicio"), rs.getString("descripcionServicio")));
				newDto.setPorcentajeParticipacion(rs.getDouble("porcentajeParticipacion"));
				newDto.setValorParticipacion(rs.getBigDecimal("valorParticipacion"));
				newDto.setCuentaContableIns( new InfoDatosDouble(rs.getDouble("cuentaContableIns"), rs.getString("nombreCuentaContableIns")));
				newDto.setCueContInstVigAnterior(new InfoDatosDouble(rs.getDouble("cueContInsVigAnterior"), rs.getString("nombreCueContInstVigA")));				
				newDto.setCuentaContableHonorario(new InfoDatosDouble(rs.getDouble("cuentaContableHonorario"), rs.getString("nombreCuentaContableHonorario")));
				newDto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				newDto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newDto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				listaHonoriarios.add(newDto);
			 }
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN cargarDetallePrograma==> "+e);
		}
		
		return listaHonoriarios;
	} 
	
	
	
	
	/**
	 * GUARDAR GRUPOS HONORARIO POOL 
	 * @param dto
	 * @return
	 */
	public static double guardarGruposHonorarioPool(Connection con, DtoAgrupHonorariosPool dto) 
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble; // falta
		logger.info("\n\n\n GUARDANDO AGRUPACION POOL ");
		
		try 
		{
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_agrup_honorarios_p"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStrAgrupacionHonorarios);

			ps.setDouble(1, secuencia);
			ps.setBigDecimal(2,dto.getHonorarioPool() );
			ps.setInt(3, dto.getGrupoServicio().getCodigo());
			if(!UtilidadTexto.isEmpty(dto.getTipoServicio().getCodigo() ))
			{
				ps.setString(4, dto.getTipoServicio().getCodigo());
			}
			else
			{
				ps.setNull(4, Types.VARCHAR);
			}
			
			if( dto.getPorcentajeParticipacion()>0)
			{
				ps.setDouble(5, dto.getPorcentajeParticipacion());
			}
			else
			{
				ps.setNull(5, Types.FLOAT);
			}

			if(dto.getValorParticipacion()!=null&&dto.getValorParticipacion().doubleValue()>0)
			{
				ps.setBigDecimal(6, dto.getValorParticipacion());
			}
			else
			{
				ps.setNull(6, Types.NUMERIC);
			}
			
			if(dto.getCuentaContableHonorario().getCodigo()>0)
			{
				ps.setDouble(7, dto.getCuentaContableHonorario().getCodigo());
			}
			else
			{
				ps.setNull(7, Types.NUMERIC);
			}
	
			if(dto.getCuentaContableIns().getCodigo()>0)
			{
				ps.setDouble(8, dto.getCuentaContableIns().getCodigo());
			}
			else
			{
				ps.setNull(8, Types.NUMERIC);
			}
			
			if(dto.getCueContInstVigAnterior().getCodigo()>0)
			{
				ps.setDouble(9, dto.getCueContInstVigAnterior().getCodigo());
			}
			else
			{
				ps.setNull(9,Types.NUMERIC);
			}
			
			ps.setString(10, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(11, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(12, dto.getUsuarioModifica().getUsuarioModifica());
			
			logger.info("\n\n\n\n\n");
			logger.info(ps);
			logger.info("\n\n\n\n");
			
	
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , null);
				return secuencia;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , null);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		return secuencia;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean modicarAgrupacionServicios(DtoAgrupHonorariosPool dto)
	{
		boolean retorna= true;
		String  updateStrAgrupacion= " update  facturacion.agrup_honorarios_pool  set codigo_pk=codigo_pk ,"+
										"honorario_pool=?, "+ 
										"grupo_servicio=?, "+ 
										"tipo_servicio=?,  "+
										"porcentaje_participacion=?, "+ 
										"valor_participacion=?, "+
										"cuenta_contable_honorario=?, "+
										"cuenta_contable_ins=?, "+
										"cue_cont_inst_vig_anterior=?, "+ 
										"fecha_modifica=?, "+
										"hora_modifica=?, "+
										"usuario_modifica=?  where 1=1" ;
		updateStrAgrupacion+= (dto.getCodigoPk().doubleValue()>0) ?" and codigo_pk="+dto.getCodigoPk(): "";
		updateStrAgrupacion+=(dto.getHonorarioPool().doubleValue()>0)?" and honorario_pool="+dto.getHonorarioPool(): "";
		
		
		logger.info(" MODIFICAR HONORARIO ");
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, updateStrAgrupacion);

			
			ps.setBigDecimal(1,dto.getHonorarioPool() );
			ps.setInt(2, dto.getGrupoServicio().getCodigo());
			
			if(!UtilidadTexto.isEmpty(dto.getTipoServicio().getCodigo() ))
			{
				ps.setString(3, dto.getTipoServicio().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.VARCHAR);
			}
			
			if( dto.getPorcentajeParticipacion()>=0)
			{
				ps.setDouble(4, dto.getPorcentajeParticipacion());
			}
			else
			{
				ps.setNull(4, Types.FLOAT);
			}

			if(dto.getValorParticipacion()!=null&&dto.getValorParticipacion().doubleValue()>=0)
			{
				ps.setBigDecimal(5, dto.getValorParticipacion());
			}
			else
			{
				ps.setNull(5, Types.NUMERIC);
			}
			
			if(dto.getCuentaContableHonorario().getCodigo()>0)
			{
				ps.setDouble(6, dto.getCuentaContableHonorario().getCodigo());
			}
			else
			{
				ps.setNull(6, Types.NUMERIC);
			}
	
			if(dto.getCuentaContableIns().getCodigo()>0)
			{
				ps.setDouble(7, dto.getCuentaContableIns().getCodigo());
			}
			else
			{
				ps.setNull(7, Types.NUMERIC);
			}
			
			if(dto.getCueContInstVigAnterior().getCodigo()>0)
			{
				ps.setDouble(8, dto.getCueContInstVigAnterior().getCodigo());
			}
			else
			{
				ps.setNull(8,Types.NUMERIC);
			}
			
			ps.setString(9, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(10, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(11, dto.getUsuarioModifica().getUsuarioModifica());
			
			logger.info("\n\n\n\n\n");
			logger.info(ps);
			logger.info("\n\n\n\n");
			
	
			retorna= ps.executeUpdate()>0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
	
		return retorna;
	}
	
	/** 
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoAgrupHonorariosPool> cargarAgrupacionHonorariosPool( DtoAgrupHonorariosPool dto){
		
		ArrayList<DtoAgrupHonorariosPool> listaAgrupPool = new ArrayList<DtoAgrupHonorariosPool>();
		
		String consultaStr=" SELECT  	ahp.codigo_pk  as codigoPk, "+ 
								" ahp.honorario_pool as honorarioPool,"+
								" ahp.grupo_servicio as grupoServicio, " +
								" getnombregruposervicio(ahp.grupo_servicio ) AS descgruposerv , "+
								" ahp.tipo_servicio as tipoServicio, " +
								" (select tp.descripcion from tipos_servicio tp where tp.acronimo=ahp.tipo_servicio) as nombreTipoServicio, "+ 
								" coalesce (ahp.porcentaje_participacion ,"+ConstantesBD.codigoNuncaValidoDoubleNegativo+")  as porcentajeParticipacion ," +
								" coalesce (ahp.valor_participacion, "+ConstantesBD.codigoNuncaValido+")  as valorParticipacion," +  
								" ahp.cuenta_contable_ins as cuentaContableIns , "+
								" (select cu.cuenta_contable ||' '|| cu.descripcion from  interfaz.cuentas_contables cu   where cu.codigo=ahp.cuenta_contable_ins) as nombreCuentaContableIns ," +
								" ahp.cue_cont_inst_vig_anterior as cueContInsVigAnterior," +
								" (select cu.cuenta_contable ||' '|| cu.descripcion from  interfaz.cuentas_contables cu   where cu.codigo=ahp.cue_cont_inst_vig_anterior) as nombreCueContInstVigA ," +
								" ahp.cuenta_contable_honorario as  cuentaContableHonorario," +
								" (select cu.cuenta_contable ||' '|| cu.descripcion from  interfaz.cuentas_contables cu   where cu.codigo=ahp.cuenta_contable_honorario) as nombrecuentaContableHonorario ," +
								" ahp.fecha_modifica as fechaModifica,"+
								" ahp.hora_modifica  as horaModifica,"+
								" ahp.usuario_modifica as usuarioModifica  FROM " +
								" facturacion.agrup_honorarios_pool ahp where 1=1"; 
		
		
		
		consultaStr+=dto.getCodigoPk().doubleValue()>0?" AND ahp.codigo_pk="+dto.getCodigoPk():" ";
		consultaStr+=(dto.getHonorarioPool().doubleValue()>0)? "AND  ahp.honorario_pool="+dto.getHonorarioPool(): " ";
		consultaStr+=(dto.getGrupoServicio().getCodigo()>0)?" AND ahp.grupo_servicio="+dto.getGrupoServicio().getCodigo(): "";
		consultaStr+=(dto.getCuentaContableIns().getCodigo()>0)? " AND ahp.cuenta_contable_ins="+dto.getCuentaContableIns(): "";
		consultaStr+=(dto.getCuentaContableHonorario().getCodigo()>0)? " AND ahp.cuenta_contable_honorario="+dto.getCuentaContableHonorario(): "";
		consultaStr+=(dto.getCueContInstVigAnterior().getCodigo()>0)? " AND ahp.cue_cont_inst_vig_anterior="+dto.getCueContInstVigAnterior(): "";
		
		logger.info("**********************************************************************************************");
		logger.info("------------------------------------CARGAR AGRUPACION HONORIARIO POOL ------------------------------------------");
		logger.info(consultaStr);
		logger.info("**********************************************************************************************");
		
		Connection con = UtilidadBD.abrirConexion();
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoAgrupHonorariosPool newDto = new  DtoAgrupHonorariosPool();
				newDto.setCodigoPk(rs.getBigDecimal("codigoPk"));
				newDto.setHonorarioPool(rs.getBigDecimal("honorarioPool"));
				newDto.setGrupoServicio(new InfoDatosInt(rs.getInt("grupoServicio"),rs.getString("descgruposerv") ));
				newDto.setTipoServicio(new InfoDatosStr(rs.getString("tipoServicio"), rs.getString("nombreTipoServicio")));
				newDto.setValorParticipacion(rs.getBigDecimal("valorParticipacion"));
				newDto.setPorcentajeParticipacion(rs.getDouble("porcentajeParticipacion"));
				newDto.setCuentaContableIns( new InfoDatosDouble(rs.getDouble("cuentaContableIns"), rs.getString("nombreCuentaContableIns")));
				newDto.setCueContInstVigAnterior(new InfoDatosDouble(rs.getDouble("cueContInsVigAnterior"), rs.getString("nombreCueContInstVigA")));				
				newDto.setCuentaContableHonorario(new InfoDatosDouble(rs.getDouble("cuentaContableHonorario"), rs.getString("nombreCuentaContableHonorario")));
				newDto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				newDto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newDto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				listaAgrupPool.add(newDto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN cargarDetallePrograma==> "+e);
		}
		
		return listaAgrupPool;
		
	} 
}