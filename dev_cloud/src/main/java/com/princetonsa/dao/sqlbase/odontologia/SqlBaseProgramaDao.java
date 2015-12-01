package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class SqlBaseProgramaDao {
	

	private static Logger logger = Logger.getLogger(SqlBaseEmisionBonosDescDao.class);
	
	private static String InsertarStr= "insert into odontologia.programas ( codigo, nombre" +
																			", fecha_modifica,  " +
																			"hora_modifica ," +
																			" usuario_modifica) " +
																			"values (? , ?, ?, ?, ? )"; 
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoPrograma dto)
	{
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
		//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_programas"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(InsertarStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,secuencia);
			ps.setString(2, dto.getNombre());
			ps.setString(3,dto.getFechaModifica());
			ps.setString(4, dto.getHoraModifica());
			ps.setString(5, dto.getUsuarioModifica());
			
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
	public static ArrayList<DtoPrograma> cargar( DtoPrograma dto){
	 	ArrayList<DtoPrograma> arrayDto= new ArrayList<DtoPrograma>();
		String consultaStr=" select  	codigo as codigo, " +
									"	codigo_programa as codigoPrograma, " +
									"	nombre as nombre, " +
									"	fecha_modifica as fechaModifica,  " +
									"	hora_modifica as horaModifica , " +
									" 	usuario_modifica as usuarioModifica" +
									" 	from odontologia.programas" +
									" 	where " +
									"	1=1 ";
		consultaStr+=dto.getCodigo()>0?" AND codigo="+dto.getCodigo():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoPrograma())?"":" AND codigo_programa='"+dto.getCodigoPrograma()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?"":" AND UPPER(nombre) like  UPPER('%"+dto.getNombre()+"%')";
		consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoPrograma())?"":" AND codigo_programa='"+dto.getCodigoPrograma()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?"":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?"":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?"":" AND usuario_modifica='"+dto.getUsuarioModifica()+"'";
		    
	    logger.info("\n\n\n\n\n SQL cargar Programas Odontologicos / "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by nombre ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPrograma newdto = new DtoPrograma();
				newdto.setCodigo(rs.getDouble("codigo"));
				newdto.setNombre(rs.getString("nombre"));
				newdto.setFechaModifica(rs.getString("fechaModifica"));
				newdto.setHoraModifica(rs.getString("horaModifica"));
				newdto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setCodigoPrograma(rs.getString("codigoPrograma"));
				newdto.setListaServiciosPrograma(SqlBaseProgramasOdontologicosDao.cargarDetallePrograma(rs.getDouble("codigo"), ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())));
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
	 * BUSQUEDA DE  PROGRAMAS AVANZADO
	 * @param incluirInactivos 
	 */
	
	public static ArrayList<DtoPrograma> cargarAvanzado( DtoPrograma dto, boolean incluirInactivos){
	 	ArrayList<DtoPrograma> arrayDto= new ArrayList<DtoPrograma>();
		String consultaStr=" select  	codigo as codigo, " +
									"	 codigo_programa as codigoPrograma , ";
									if(!UtilidadTexto.isEmpty(dto.getYaSeleccionado()))
									{
										consultaStr+=" 	case when codigo in ("+dto.getYaSeleccionado()+") then 'S' else 'N' end as yaSeleccionado ,";
									}
									else
									{
										consultaStr+= " 'N' as yaSeleccionado , ";
									}
									
									consultaStr+= "nombre as nombre, " +
									"	fecha_modifica as fechaModifica,  " +
									"	hora_modifica as horaModifica , " +
									" 	usuario_modifica as usuarioModifica ," +
									"	especialidad as especialidad," +
									"	getnombreespecialidad(especialidad) AS nombreEspecialidad  ," +
									"	activo as activo " +
									" 	from odontologia.programas " +
									" 	where " +
									"	1=1 ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?" ":" AND UPPER(nombre) like  UPPER('%"+dto.getNombre()+"%')";
		consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoPrograma())?" ":" AND codigo_programa='"+dto.getCodigoPrograma()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=dto.getEspecialidad()>0?" AND especialidad="+dto.getEspecialidad(): "";
	    
	    if (!incluirInactivos) {
	    	consultaStr+=  "AND activo= '"+ ConstantesBD.acronimoSi + "'";
		}
		    
	    logger.info("\n\n\n\n\n SQL cargar Programas Odontologicos / "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by nombre ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPrograma newdto = new DtoPrograma();
				newdto.setCodigo(rs.getDouble("codigo"));
				newdto.setNombre(rs.getString("nombre"));
				newdto.setFechaModifica(rs.getString("fechaModifica"));
				newdto.setHoraModifica(rs.getString("horaModifica"));
				newdto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setCodigoPrograma(rs.getString("codigoPrograma"));
				newdto.setYaSeleccionado(rs.getString("yaSeleccionado"));
				newdto.setEspecialidad(rs.getInt("especialidad"));
				newdto.setNombreEspecialidad(rs.getString("nombreEspecialidad"));
				newdto.setActivo(rs.getString("activo"));
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
 * @param dto
 * @return
 */
	
public static boolean eliminar( DtoPrograma dto){
		
		String consultaStr="DELETE FROM  odontologia.programas WHERE  ";
		

		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?" ":" AND nombre='"+dto.getNombre()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigo()> 0)?" AND codigo= "+ dto.getCodigo():"";
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
		 * 
		 * @param dto
		 * @return
		 */
	public static boolean modificar( DtoPrograma dto )
	{
		boolean retorna=false;
		String consultaStr ="UPDATE odontologia.programas set codigo=codigo ";
	
		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?" ":" , nombre='"+dto.getNombre()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" ,fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" , hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" ,  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigo()> 0)?" where codigo= "+ dto.getCodigo():"";
	    logger.info("Update "+consultaStr);
		   
	    try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma "+e);
		}
		return retorna;
		
	}
	
	/**
	 * 
	 * @param programa
	 * @return
	 */
	public static String obtenerEspeciliadadPrograma(double programa)
	{
		String consultaStr= "select administracion.getnombreespecialidad(especialidad) from odontologia.programas where codigo=?";
		String retorna="";
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, programa);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				retorna=rs.getString(1);
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma ",e);
		}
		return retorna;
	}
	
	
	
	
	/**
	 * 	PARA POSTGRES 
	 */
	
	
	

}
