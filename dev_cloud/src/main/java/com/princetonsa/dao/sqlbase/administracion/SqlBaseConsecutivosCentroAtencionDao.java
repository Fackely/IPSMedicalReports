package com.princetonsa.dao.sqlbase.administracion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;





/**
 * 
 * @author axioma
 *
 */
public class SqlBaseConsecutivosCentroAtencionDao {
	

	
	private static Logger logger = Logger.getLogger(SqlBaseConsecutivosCentroAtencionDao.class);
	
	
	/**
	 * 
	 */
	private static  String insertarStr=" insert into administracion.consecutivos_centro_aten " +
										"	(codigo_pk, nombre,  centro_atencion, anio_vigencia , " +
										"	valor, fecha_modifica , hora_modifica , usuario_modifica , activo, id_anual" +
										" " +
										"  )" +
										" values " +
										"(?	,	?	,	?	,	?	,	?	,	?	,	?	,	?	, ? , ?)";
	


	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean guardar(DtoConsecutivoCentroAtencion dto, Connection con)
	{
		
		logger.info("INSERTAR CONSECUTIVO ");
		
		double secuencia= ConstantesBD.codigoNuncaValidoDouble;
		boolean retorna=Boolean.FALSE;
		
		
		try 
		{
			
			
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_conse_centro_aten"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStr);
			
			ps.setDouble(1,secuencia);
			ps.setString(2, dto.getNombreConsecutivo());
			ps.setInt(3, dto.getCentroAtencion().getCodigo());
			
			
			if(!UtilidadTexto.isEmpty(dto.getAnio()))
			{
				ps.setString(4, dto.getAnio());
			}
			else
			{
				ps.setNull(4, Types.VARCHAR);
			}
			
			
			ps.setBigDecimal(5, dto.getConsecutivo());
			ps.setString(6, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(7, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(8, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(9, dto.getActivo());
			ps.setString(10, dto.getIdAnual());
		
				
			logger.info("INSERTAR CONSECUTIVOS **************");
			logger.info(ps);
			logger.info("*********************\n\n\n\n\n\n\n\n");
			
			
			if(ps.executeUpdate()>0)
			{
				 retorna=Boolean.TRUE;
			}
			
		}
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR en insert  ", e );
		}
	
		
			
		return retorna;
		
	}
	
	/**
	 * Metodo para obtener el valor actual del consecutivo x centro de atencion
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @return
	 */
	public static BigDecimal obtenerValorActualConsecutivo(Connection con , int centroAtencion, String nombreConsecutivo, int anio)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr= " select "+
								" coalesce(valor,0) as valor " +
							"from " +
								"administracion.consecutivos_centro_aten cca where 1=1 " +
								"AND cca.centro_atencion=? " +
								"and cca.nombre= ? " +
								"and cca.activo= '"+ConstantesBD.acronimoSi+"' " +
								"and (cca.anio_vigencia= ? or cca.anio_vigencia is null) " +
								"order by cca.anio_vigencia ";
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, centroAtencion);
			ps.setString(2, nombreConsecutivo);
			ps.setInt(3, anio);
			
			Log4JManager.info("\n\n obtenerValorActualConsecutivo-->"+ps+"\n\n");
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				retorna= rs.getBigDecimal(1);
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en obtenerValorActualConsecutivo ", e);
			e.printStackTrace();
		}
		return retorna;
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoConsecutivoCentroAtencion> cargar( DtoConsecutivoCentroAtencion dto){
	 
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n *************************************	CARGAR CONSECUTIVOS CENTRO ATENCION ***************************	\n\n\n\n");
		ArrayList<DtoConsecutivoCentroAtencion> arrayDto= new ArrayList<DtoConsecutivoCentroAtencion>();

		String consultaStr= " select "+
							" cca.codigo_pk  as codigoPk, "+
							" cca.nombre as nombre, "+
							" cca.centro_atencion as centroAtencion ,"+
							" cca.anio_vigencia as anioVigencia, "+
							" coalesce(cca.valor,0) as valor ," +
							" cca.fecha_modifica as fechaModifica , " +
							" cca.hora_modifica as horaModifica , " +
							" cca.usuario_modifica  as usuarioModifica ," +
							" cca.id_anual as idAnual ," +
							" cca.activo as activo" +
							" " +
							"from " +
							"administracion.consecutivos_centro_aten cca where 1=1";
		
		consultaStr+=  dto.getCentroAtencion().getCodigo()>0?" AND cca.centro_atencion="+dto.getCentroAtencion().getCodigo(): " ";
		
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("consultaSt----->>>>>>>>>>>>><<<"+consultaStr);
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
		
		/**
		 * VALIDACION 
		 */
		if(dto.getCentroAtencion().getCodigo()<=0)
		{
			return arrayDto;
		}
		
		
		consultaStr+= (UtilidadTexto.isEmpty(dto.getNombreConsecutivo()))?" ":" and cca.nombre= '"+dto.getNombreConsecutivo()+"' ";
		consultaStr+= (UtilidadTexto.isEmpty(dto.getActivo()))?" ":" and cca.activo= '"+dto.getActivo()+"' ";
		
		logger.info("\n\n\n\n\n SQL cargar CONSECUTIVOS  	 \n\n\n\n "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
				nDto.setCodigoPk(rs.getBigDecimal("codigoPk"));
				nDto.setNombreConsecutivo(rs.getString("nombre"));
				nDto.getCentroAtencion().setCodigo(rs.getInt("centroAtencion"));
				nDto.setAnio(rs.getString("anioVigencia"));
				nDto.setConsecutivo(rs.getBigDecimal("valor"));
				nDto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				nDto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				nDto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				nDto.setIdAnual(rs.getString("idAnual"));
				nDto.setActivo(rs.getString("activo"));
				nDto.setCodigoIndiceArray(i);
				arrayDto.add(nDto);
				i++;
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param con Conexión con la BD
	 * @param dto
	 * @return
	 */
	public static boolean modificar(Connection con, DtoConsecutivoCentroAtencion dto)
	{
	
		logger.info(" 	MODIFICAR CONSECUTIVO \n\n\n");
		
		 boolean retorna= Boolean.FALSE; 
		
		
		String modificarStr=" update  administracion.consecutivos_centro_aten " +
							" set nombre=?," +
							" anio_vigencia=? ," +
							" valor=? ," +
							" fecha_modifica=? ," +
							" hora_modifica=? ," +
							" usuario_modifica=?	," +
							" id_anual=? ,"+ 
							" activo= ? "+
							" where centro_atencion= ? and codigo_pk=? ";
							
		try 
		{
		
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, modificarStr);
			
			ps.setString(1,dto.getNombreConsecutivo());
			if(UtilidadTexto.isEmpty(dto.getAnio()))
			{
				ps.setNull(2, java.sql.Types.INTEGER);
			}
			else
			{
				ps.setInt(2, Utilidades.convertirAEntero(dto.getAnio()));
			}
			ps.setBigDecimal(3, dto.getConsecutivo());
			ps.setString(4, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(5, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(6, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(7,dto.getIdAnual());
			ps.setString(8, dto.getActivo());
			ps.setInt(9, dto.getCentroAtencion().getCodigo());
			ps.setBigDecimal(10, dto.getCodigoPk());
			
			retorna=ps.executeUpdate()>0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , null);
						
		}
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR en insert ", e);
		}
	
		return retorna;
		
	}
	
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminar (DtoConsecutivoCentroAtencion dto)
	{
		
		
		 boolean retorna= Boolean.FALSE; 
		
		
		String modificarStr=" delete  administracion.consecutivos_centro_aten " +
							" where codigo_pk="+dto.getCodigoPk();
							
		try 
		{
		//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, modificarStr);
			retorna=ps.executeUpdate()>0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
						
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en ELIMINAR " + e);
		}
	
		return retorna;
		
	}
	

	/**
	 * Metodo para incrementar el consecutivo, recibe la coneccion porque puede ser transaccional
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @param consulta
	 * @param con
	 * @return
	 */
	public static BigDecimal incrementarConsecutivo(int centroAtencion, String nombreConsecutivo, String consulta, int institucion)
	{
		Connection con=UtilidadBD.abrirConexion();
		DtoConsecutivoCentroAtencion dto=obtenerConsecutivo(con, centroAtencion, nombreConsecutivo, UtilidadFecha.getMesAnioDiaActual("anio"));
		
		if(dto==null || dto.getConsecutivo().intValue()<0)
		{
			return new BigDecimal(-1);
		}
		
		BigDecimal retorna=null;
		String cadenaStr="update administracion.consecutivos_centro_aten set valor=valor+1 ";
		
		cadenaStr+=" where centro_atencion=? and nombre=? and activo='"+ConstantesBD.acronimoSi+"' ";
		
		UtilidadBD.iniciarTransaccion(con);



		try 
		{
			String generarBloqueo="lock uso_consecutivos_centro_aten";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, generarBloqueo);
			psd.execute();
			psd.cerrarPreparedStatement();

			String generarBloqueoConsecutivo="lock consecutivos_centro_aten";
			psd=new PreparedStatementDecorator(con, generarBloqueoConsecutivo);
			psd.execute();
			psd.cerrarPreparedStatement();

			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, cadenaStr);
			ps.setInt(1, centroAtencion);
			ps.setString(2, nombreConsecutivo);
			boolean resultado=ps.executeUpdate()>0;
			
			if(resultado)
			{
				String consecutivo=obtenerSiguienteValorConsecutivoDisponible(con, consulta);
				retorna=new BigDecimal(Utilidades.convertirADouble(consecutivo));
				if(retorna.intValue()<0)
				{
					if(insertarUsoConsecutivoCentroAtencion(con, dto, institucion))
					{
						consecutivo=obtenerSiguienteValorConsecutivoDisponible(con, consulta);
						retorna=new BigDecimal(Utilidades.convertirADouble(consecutivo));
						if(Utilidades.convertirAEntero(consecutivo)<0)
						{
							Log4JManager.error("Error obteniendo el consecutivo disponible");
						}
					}
				}
			}
			ps.cerrarPreparedStatement();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Obtener siguiente consecutivo disponible", e);
			UtilidadBD.abortarTransaccion(con);
		}
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @param valor
	 * @param institucion
	 * @return
	 * @throws SQLException 
	 */
	private static boolean insertarUsoConsecutivoCentroAtencion(Connection con, DtoConsecutivoCentroAtencion dto,
			int institucion) throws SQLException
	{
		int valorSecuencia=UtilidadBD.obtenerSiguienteValorSecuencia("administracion.seq_uso_consecutivo_centro_aten");
		String sentencia=
			"INSERT INTO " +
				"administracion.uso_consecutivos_centro_aten " +
				"(" +
					"codigo, " +
					"nombre, " +
					"institucion, " +
					"anio_vigencia, " +
					"valor, " +
					"centro_atencion) " +
				"VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		psd.setInt(1, valorSecuencia);
		psd.setString(2, dto.getNombreConsecutivo());
		psd.setInt(3, institucion);
		psd.setString(4, dto.getAnio());
		psd.setBigDecimal(5, dto.getConsecutivo());
		psd.setInt(6, dto.getCentroAtencion().getCodigo());
		if(psd.executeUpdate()<=0)
		{
			Log4JManager.error("Error generando el uso consecutivos");
		}
		psd.cerrarPreparedStatement();
		
		return true;
	}

	/**
	 * Obtiene el siguiente consecutivo disponible
	 * @param con
	 * @param consulta
	 */
	private static String obtenerSiguienteValorConsecutivoDisponible(Connection con, String consulta)
	{
		String resultado=ConstantesBD.codigoNuncaValido+"";
		
		PreparedStatementDecorator psd;
		try
		{
			psd=new PreparedStatementDecorator(con, consulta);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				resultado=rsd.getString("valor");
			}
			psd.cerrarPreparedStatement();
		} catch (SQLException e)
		{
			Log4JManager.error("Error capturando el valor del consecutivo disponible", e);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	
	/**
	 * METODO QUE RECIBE UN DTO CONSECUTIVO CENTRO ATENCION Y RETORNA true ESTA ASIGNADO EN OTRO CASO ES false 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 */
	public  static boolean esConsecutivoAsignado(DtoConsecutivoCentroAtencion dto)
	{
			
		boolean retorno=Boolean.FALSE;
		
		String consultar="select count(0) as cantidad from  administracion.consecutivos_centro_aten cca where  1=1  ";
		consultar+=dto.getCodigoPk().doubleValue()>0? " and codigo_pk="+dto.getCodigoPk() : " "; 
		
		consultar+=" " +
						" and cca.valor="+dto.getConsecutivoInterfaz()+" " +
						" and cca.nombre='"+dto.getNombreConsecutivo()+"' " +
						" and cca.centro_atencion="+dto.getCentroAtencion().getCodigo()+ 
						" and cca.activo='"+ConstantesBD.acronimoSi+"' "+
						" and cca.asignado='"+ConstantesBD.acronimoSi+"' ";
		Connection con=null;
				
		 try 
		 {
			con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultar);
			
			Log4JManager.info("Consulta  \n"+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			//int i=0;
			
			if(rs.next())
			{
				if( rs.getInt("cantidad")>0)
				{
					retorno=Boolean.TRUE;
				}
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		 }
		 catch (Exception e) 
		 {
			 Log4JManager.info(e.getMessage());
			 Log4JManager.error(e);
		 }
			
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
				
		
		return retorno;
	}

	/**
	 * Metodo para obtener el valor actual del consecutivo x centro de atencion
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @return
	 */
	public static DtoConsecutivoCentroAtencion obtenerConsecutivo(Connection con , int centroAtencion, String nombreConsecutivo, int anio)
	{
		DtoConsecutivoCentroAtencion dto=null;
		String consultaStr= " select " +
								"cca.codigo_pk AS codigo_pk, " +
								"cca.centro_atencion AS centro_atencion, "+
								"coalesce(valor,0) as valor, " +
								"cca.nombre AS nombre, " +
								"cca.anio_vigencia AS anio " +
							"from " +
								"administracion.consecutivos_centro_aten cca where 1=1 " +
								"AND cca.centro_atencion=? " +
								"and cca.nombre= ? " +
								"and cca.activo= '"+ConstantesBD.acronimoSi+"' " +
								"and (cca.anio_vigencia= ? or cca.anio_vigencia is null) " +
								"order by cca.anio_vigencia ";
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, centroAtencion);
			ps.setString(2, nombreConsecutivo);
			ps.setInt(3, anio);
			
			Log4JManager.info("\n\n obtenerValorActualConsecutivo-->"+ps+"\n\n");
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				dto=new DtoConsecutivoCentroAtencion();
				dto.setCodigoPk(rs.getBigDecimal("codigo_pk"));
				dto.setAnio(rs.getString("anio"));
				dto.setConsecutivo(rs.getBigDecimal("valor"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion")));
				dto.setNombreConsecutivo(rs.getString("nombre"));
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch(SQLException e)
		{
			Log4JManager.error(" Error en obtenerValorActualConsecutivo ", e);
		}
		return dto;
	}

	/**
	 * @param con
	 * @param consecutivo Nombre del consecutivo
	 * @param centroAtencion Centro de atención del consecutivo
	 * @param valor Valor del consecutivo asignado
	 * @param finalizado [S/N] Indica si el consecutivo ya se utilizó.
	 * @param usado [S/N] Indica si el consecutivo ya se fue retornado.
	 * @param anio Año del consecutivo
	 * @return
	 */
	public static boolean cambiarUsoFinalizadoConsecutivo(Connection con,
			String consecutivo, int centroAtencion, BigDecimal valor,
			String finalizado, String usado, int anio)
	{
		String sentencia=
			"UPDATE uso_consecutivos_centro_aten " +
			"SET " +
				"finalizado=?," +
				"usado=? " +
			"WHERE " +
				"nombre=? " +
				"AND " +
				"centro_atencion=? " +
				"AND " +
				"valor=? " +
				"AND (anio_vigencia IS NULL OR (anio_vigencia=?))";
		try
		{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setString(1, finalizado);
			psd.setString(2, usado);
			psd.setString(3, consecutivo);
			psd.setInt(4, centroAtencion);
			psd.setBigDecimal(5, valor);
			psd.setString(6, anio+"");
			
			boolean resultado=psd.executeUpdate()>0;
			psd.cerrarPreparedStatement();
			return resultado;
		} catch (SQLException e)
		{
			Log4JManager.error("Error finalizando el consecutivo", e);
		}
		return false;
	}
	
	
	
}
