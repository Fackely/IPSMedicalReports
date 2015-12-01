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
import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;

public class SqlBaseDetConvPromocionesOdo {


	private static Logger logger = Logger.getLogger(SqlBaseDetConvPromocionesOdo.class);

	private static String InsertarStr= " insert into odontologia.det_conv_promociones_odo( " +
			" codigo_pk," +//1
			" det_promocion_odo ,  " +//2
			" convenio , " + //3
			" activo," + //4
			" fecha_inactivacion , " + //5
			" hora_inactivacion, " + //6
			" usuario_inactivacion , " + //7 	
			" fecha_modifica, " + //8
			" hora_modifica, " + //9
			" usuario_modifica) " + //10
			" values " +
			"( ? , ? , ? , ? , ? " + //5
 			", ? , ? , ? , ?, ? )";	//10
	
	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoDetConvPromocionesOdo dto)
	{
		logger.info("GUARDAR DET CONVENIO PROMOCIONES\n\n\n\n'");
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
		//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_conv_prom"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con ,InsertarStr );
			logger.info("codigo de la secuencia de  Det - Centro Atencio- Promocion "+(int)secuencia);
			
			
			ps.setInt(1,(int)secuencia);
			ps.setInt(2, dto.getDetPromocionOdo());
			ps.setInt(3,dto.getConvenio().getCodigo());
			ps.setString(4, dto.getActivo());
			
			if(UtilidadTexto.isEmpty( dto.getFechaInactivacion()))
			{
				
				ps.setNull(5,Types.VARCHAR);
			}
			else
			{
				ps.setString(5, dto.getFechaInactivacion());
				
			}
			
			if(UtilidadTexto.isEmpty(dto.getHoraInactivacion()))
			{
			
				ps.setNull(6,Types.VARCHAR);
			}
			else
			{
				ps.setString(6, dto.getHoraInactivacion());
				
			}
		
			if(UtilidadTexto.isEmpty(dto.getUsuarioInactivacion()))
			{
				ps.setNull(7,Types.VARCHAR);
			}
			else
			{
				ps.setString(7, dto.getUsuarioInactivacion());
			}
			
			ps.setString(8, dto.getFechaModificaBD());
			ps.setString(9, dto.getHoraModifica());
			ps.setString(10, dto.getUsuarioModifica());
			
			logger.info("SQl\n\n\n\n\n"+ps+"\n\n\n\n");
			
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
	
	public static ArrayList<DtoDetConvPromocionesOdo> cargar( DtoDetConvPromocionesOdo  dto){
	 	ArrayList<DtoDetConvPromocionesOdo> arrayDto= new ArrayList<DtoDetConvPromocionesOdo>();
		
	 	logger.info("\n\n\n\n\n\n CARGAR DET CONVENIO  \n\n\n");
	 	String consultaStr=" select  dcp.codigo_pk as codigoPk ," + //1
										" dcp.det_promocion_odo as detPromocionOdo, " + //2 
										" dcp.convenio as convenio ," + //3
										" getnombreconvenio(dcp.convenio) as nombreConvenio,  " +//4
										" dcp.activo as activo, " + //5
										" dcp.fecha_inactivacion as fechaInactivacion , " + //6
										" dcp.hora_inactivacion as horaInactivacion, " + //7
										" dcp.usuario_inactivacion as usuarioInactivacion, " +	 //8
										" dcp.fecha_modifica as fechaModifica, " + //9
										" dcp.hora_modifica as horaModifica," + //10
										" dcp.usuario_modifica as usuarioModifica " +//11
										" from " +
										"odontologia.det_conv_promociones_odo dcp " + //10
										" where " +
										"1=1 ";

	 	 
		consultaStr+=(0<dto.getDetPromocionOdo())?" AND  det_promocion_odo= "+dto.getDetPromocionOdo()+" ":"";
		consultaStr+=(0<dto.getConvenio().getCodigo())?" AND  convenio= "+dto.getConvenio().getCodigo()+" ":"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?" ":" AND activo='"+dto.getActivo()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInactivacion())?" ":" AND fecha_inactivacion='"+dto.getFechaInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getHoraInactivacion())?" ":" AND hora_inactivacion='"+dto.getHoraInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioInactivacion())?" ":" AND usuario_inactivacion='"+dto.getUsuarioInactivacion()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
		    
	    logger.info("\n\n\n\n\n SQL det convenio / "+consultaStr+ " \n\n\n\n\n");
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoDetConvPromocionesOdo newdto = new DtoDetConvPromocionesOdo();
				newdto.setCodigoPk(rs.getInt("codigoPk"));
				newdto.setDetPromocionOdo(rs.getInt("detPromocionOdo"));
				newdto.setActivo(rs.getString("activo"));
				newdto.getConvenio().setCodigo(rs.getInt("convenio"));//falta Nombre
				newdto.getConvenio().setNombre(rs.getString("nombreConvenio"));
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

	public static boolean modificar( DtoDetConvPromocionesOdo dto )
	{
		boolean retorna=false;
		
		logger.info("Sql Modificar Convenio");
		
		
		String consultaStr ="UPDATE odontologia.det_conv_promociones_odo set codigo_pk=codigo_pk";
		 
			
		consultaStr+=(0<dto.getDetPromocionOdo())?" ,  det_promocion_odo= "+dto.getDetPromocionOdo()+" ":"";
		consultaStr+=(0<dto.getConvenio().getCodigo())?" , convenio= "+dto.getConvenio().getCodigo()+" ":"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?" ":" , activo='"+dto.getActivo()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInactivacion())?" ":" , fecha_inactivacion='"+dto.getFechaInactivacionDB()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getHoraInactivacion())?" ":" , hora_inactivacion='"+dto.getHoraInactivacion()+"'";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioInactivacion())?" ":" ,  usuario_inactivacion='"+dto.getUsuarioInactivacion()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" ,  fecha_modifica='"+dto.getFechaModificaBD()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" , hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" ,   usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigoPk()> 0)?" where codigo_pk= "+ dto.getCodigoPk():"";
	    logger.info("Consulta Modificar----------");
	    logger.info(consultaStr+"\n\n\n\n");
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
   
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
public static boolean eliminar( DtoDetConvPromocionesOdo dto){
		
		String consultaStr="DELETE FROM  odontologia.programas WHERE 1=1 ";
		
		
		consultaStr+=(0<dto.getCodigoPk())?" AND  codigo_pk= "+dto.getCodigoPk()+" ":"";
		consultaStr+=(0<dto.getDetPromocionOdo())?" AND  det_promocion_odo= "+dto.getDetPromocionOdo()+" ":"";
		consultaStr+=(0<dto.getConvenio().getCodigo())?" AND  convenio= "+dto.getConvenio().getCodigo()+" ":"";
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
	
}
