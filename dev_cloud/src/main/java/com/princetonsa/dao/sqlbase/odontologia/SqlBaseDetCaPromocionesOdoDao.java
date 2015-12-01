package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Types;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;


public class SqlBaseDetCaPromocionesOdoDao {
	

	private static Logger logger = Logger.getLogger(SqlBaseDetCaPromocionesOdoDao.class);

	private static String InsertarStr= " insert into odontologia.det_ca_promociones_odo( " +
			" codigo_pk," + //1
			" det_promocion_odo , " + //2 
			" centro_atencion ," + //3
			" activo , " + //4
			" fecha_inactivacion , " + //5
			" hora_inactivacion , " + //6
			" usuario_inactivacion, " +	 //7
			" fecha_modifica, " + //8
			" hora_modifica, " + //9
			" usuario_modifica) " + //10
			"values " +
			"( ? , ? , ? , ? , ? " + //5
 			", ? , ? , ? , ?, ? )" + //10
			"" ;			
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoDetCaPromocionesOdo dto)
	{
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		
		logger.info("--Guardar  Centro Atencion");
		logger.info("Fecha "+dto.getFechaModificaBD());
	
	 	
		try 
		{
		//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_ca_prom"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(InsertarStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			ps.setInt(1,(int)secuencia);
			
			
			ps.setInt(2, dto.getDetPromocionOdo());
			
			
			// CASO QUE APLIQUE PARA TODOS LOS CENTROS DE ATENCION
			if(dto.getCentroAtencion().getCodigo()>0)
			{
				ps.setInt(3,dto.getCentroAtencion().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.INTEGER);
			}
				
				
			ps.setString(4, dto.getActivo());
			
			if(UtilidadTexto.isEmpty(dto.getFechaInactivacion()))
			{
				ps.setNull(5,Types.VARCHAR);
				
			}
			else
			{
				ps.setString(5, dto.getFechaInactivacionDB());
			}
			
			if(UtilidadTexto.isEmpty(dto.getHoraInactivacion()))
			{
				ps.setNull(6, Types.VARCHAR);
			}
			else
			{
				ps.setString(6, dto.getHoraInactivacion());
			}
			
			
			if(UtilidadTexto.isEmpty( dto.getUsuarioInactivacion()))
			{
				ps.setNull(7, Types.VARCHAR);
			}
			else
			{
				ps.setString(7, dto.getUsuarioInactivacion());
			}
			
			
			ps.setString(8, dto.getFechaModificaBD());
			ps.setString(9, dto.getHoraModifica());
			ps.setString(10, dto.getUsuarioModifica());
			
			logger.info("\n\n\n\n SQL ---> "+ps+"\n\n\n\n\n");
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
	
	public static ArrayList<DtoDetCaPromocionesOdo> cargar( DtoDetCaPromocionesOdo  dto){
	 	ArrayList<DtoDetCaPromocionesOdo> arrayDto= new ArrayList<DtoDetCaPromocionesOdo>();
		
	 	logger.info("CARGAR CENTRO DE ATENCION");
	 	
	 	String consultaStr=" select  dcp.codigo_pk as codigoPk ," + //1
										" dcp.det_promocion_odo as detPromocionOdo, " + //2 
										" dcp.centro_atencion as centroAtencion," + //3
										" getnomcentroatencion(dcp.centro_atencion) as nombreAtencion, "+
										" dcp.activo as activo, " + //4
										" dcp.fecha_inactivacion as fechaInactivacion , " + //5
										" dcp.hora_inactivacion as horaInactivacion, " + //6
										" dcp.usuario_inactivacion as usuarioInactivacion, " +	 //7
										" dcp.fecha_modifica as fechaModifica, " + //8
										" dcp.hora_modifica as horaModifica," + //9
										" dcp.usuario_modifica as usuarioModifica " +
										" from " +
										"odontologia.det_ca_promociones_odo dcp " + //10
										" where " +
										"1=1 ";
	 	
	 	consultaStr+="  AND centro_atencion is not null ";
		consultaStr+=(0<dto.getDetPromocionOdo())?" AND  det_promocion_odo= "+dto.getDetPromocionOdo()+" ":"";
		consultaStr+=(0<dto.getCentroAtencion().getCodigo())?" AND  centro_atencion= "+dto.getCentroAtencion().getCodigo()+" ":"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?" ":" AND activo='"+dto.getActivo()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInactivacion())?" ":" AND fecha_inactivacion='"+dto.getFechaInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getHoraInactivacion())?" ":" AND hora_inactivacion='"+dto.getHoraInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioInactivacion())?" ":" AND usuario_inactivacion='"+dto.getUsuarioInactivacion()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
		    
	    logger.info("\n\n\n\n\n SQL CENTROS DE ATENCIO/ "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoDetCaPromocionesOdo newdto = new DtoDetCaPromocionesOdo();
				newdto.setCodigoPk(rs.getInt("codigoPK"));
				newdto.setActivo(rs.getString("activo"));
				newdto.getCentroAtencion().setCodigo(rs.getInt("centroAtencion"));
				newdto.getCentroAtencion().setNombre(rs.getString("nombreAtencion"));
				newdto.setFechaInactivacion(rs.getString("fechaInactivacion"));
				newdto.setHoraInactivacion(rs.getString("horaInactivacion"));
				newdto.setUsuarioInactivacion(rs.getString("usuarioInactivacion"));
				newdto.setFechaModifica(rs.getString("fechaModifica"));
				newdto.setHoraModifica(rs.getString("horaModifica"));
				newdto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setEstadoDb(true);
				arrayDto.add(newdto);
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
	 */

	public static boolean modificar( DtoDetCaPromocionesOdo dto )
	{
		boolean retorna=false;
		String consultaStr ="UPDATE odontologia.det_ca_promociones_odo set codigo_pk=codigo_pk";
		 
		consultaStr+=(0<dto.getDetPromocionOdo())?" ,  det_promocion_odo= "+dto.getDetPromocionOdo()+" ":"";
		consultaStr+=(0<dto.getCentroAtencion().getCodigo())?" , centro_atencion= "+dto.getCentroAtencion().getCodigo()+" ":"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?" ":" , activo='"+dto.getActivo()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInactivacion())?" ":" , fecha_inactivacion='"+dto.getFechaInactivacionDB()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getHoraInactivacion())?" ":" , hora_inactivacion='"+dto.getHoraInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioInactivacion())?" ":" ,  usuario_inactivacion='"+dto.getUsuarioInactivacion()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" ,  fecha_modifica='"+dto.getFechaModificaBD()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" , hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" ,   usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigoPk()> 0)?" where codigo_pk= "+ dto.getCodigoPk():"";
	    logger.info("\n\n\n\n\n Update "+consultaStr +" \n\n\n");
	    logger.info(" MODIFICAR CENTRO DE ATENCION ");
		   
	    try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN update Det CA  Promociones  "+e);
		}
		return retorna;
			
	}
   
	
public static boolean eliminar( DtoDetCaPromocionesOdo dto){
		
		String consultaStr="DELETE FROM  odontologia.programas WHERE 1=1 ";
		
		
		consultaStr+=(0<dto.getCodigoPk())?" AND  codigo_pk= "+dto.getCodigoPk()+" ":"";
		consultaStr+=(0<dto.getDetPromocionOdo())?" AND  det_promocion_odo= "+dto.getDetPromocionOdo()+" ":"";
		consultaStr+=(0<dto.getCentroAtencion().getCodigo())?" AND  centro_atencion= "+dto.getCentroAtencion().getCodigo()+" ":"";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?" ":" AND activo='"+dto.getActivo()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInactivacion())?" ":" AND fecha_inactivacion='"+dto.getFechaInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getHoraInactivacion())?" ":" AND hora_inactivacion='"+dto.getHoraInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioInactivacion())?" ":" AND usuario_inactivacion='"+dto.getUsuarioInactivacion()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigoPk()> 0)?" AND codigo_pk= "+ dto.getCodigoPk():"";
	    logger.info(consultaStr);
	    
		    
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Programas  ");
			e.printStackTrace();
		}
			return false;
		}
	



	/**
	 * CARAR CENTRO DE ATENCION QUE ES NULL
	 * SIGNIFICA QUE LA PROMOCION APLICA PARA TODOS LOS CENTROS DE ATENCION
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoDetCaPromocionesOdo> cargarCentroAtencionNUll(DtoDetCaPromocionesOdo dto)
	{
		
		String consultaStr= "select  dcp.codigo_pk as codigoPk ," + //1
										" dcp.det_promocion_odo as detPromocionOdo, " + //2 
										" dcp.centro_atencion as centroAtencion," + //3
										" getnomcentroatencion(dcp.centro_atencion) as nombreAtencion, "+
										" dcp.activo as activo, " + //4
										" dcp.fecha_inactivacion as fechaInactivacion , " + //5
										" dcp.hora_inactivacion as horaInactivacion, " + //6
										" dcp.usuario_inactivacion as usuarioInactivacion, " +	 //7
										" dcp.fecha_modifica as fechaModifica, " + //8
										" dcp.hora_modifica as horaModifica," + //9
										" dcp.usuario_modifica as usuarioModifica " +
										" from " +
										"odontologia.det_ca_promociones_odo dcp " + //10
										" where dcp.centro_atencion is null AND dcp.det_promocion_odo="+dto.getDetPromocionOdo();
		
		
		logger.info("\n\n\n\n\n BUSCAR NULO  \n\n\n\n\n");
		logger.info(""+consultaStr);
		logger.info("\n\n\n\n\n");
		
		ArrayList<DtoDetCaPromocionesOdo> listaCentro = new ArrayList<DtoDetCaPromocionesOdo>();
		
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoDetCaPromocionesOdo newdto = new DtoDetCaPromocionesOdo();
				newdto.setCodigoPk(rs.getInt("codigoPK"));
				newdto.setActivo(rs.getString("activo"));
				newdto.getCentroAtencion().setCodigo(rs.getInt("centroAtencion"));
				newdto.getCentroAtencion().setNombre(rs.getString("nombreAtencion"));
				newdto.setFechaInactivacion(rs.getString("fechaInactivacion"));
				newdto.setHoraInactivacion(rs.getString("horaInactivacion"));
				newdto.setUsuarioInactivacion(rs.getString("usuarioInactivacion"));
				newdto.setFechaModifica(rs.getString("fechaModifica"));
				newdto.setHoraModifica(rs.getString("horaModifica"));
				newdto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setEstadoDb(true);
				listaCentro.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return listaCentro;
	}

	
	



}
