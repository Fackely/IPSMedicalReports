package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoContactoEmpresa;



public class SqlContactosEmpresaDao {
	
	
	private static Logger logger = Logger.getLogger(SqlContactosEmpresaDao.class);
	
	private static String insertarStr=" INSERT INTO facturacion.contactos_empresa (	" +
			"codigo ," + 			//1
			" empresa, " + 			//2
			" nombre,"+				//3
			" direccion,"+ 			//4
			" telefono ,"+ 			//5
			" email ,"+ 			//6
			" cargo ,"+ 			//7
			" usuario_modifica ,"+ 	//8
			" fecha_modifica , "+ 	//9
			" hora_modifica ,"+ 	//10
			" institucion  "+ 		//11
	
	") values ( " +
	"? , ? , ? , ? , ? , " +//5
	"? , ? , ? , ? , ? , ? ) " ;//11
	
	
	
	
	public static double guardar( DtoContactoEmpresa dto) 
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble; // falta
	    logger.info(insertarStr);
		logger.info("Institucion: "+dto.getInstitucion()); 
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			////////////////////////////////////falta aaaaaaaaaaa
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_contactos_empresa"); // Por ejecutar
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuencia);
			ps.setInt(2, dto.getEmpresa()); 
			ps.setString(3, dto.getNombre());
			ps.setString(4,dto.getDireccion());
			ps.setString(5,dto.getTelefono());
			ps.setString(6,dto.getEmail());
			ps.setString(7, dto.getCargo());
			ps.setString(8, dto.getUsuarioModifica());
			ps.setString(9,dto.getFechaModifica());
			ps.setString(10,dto.getHoraModifica());
			ps.setInt(11, dto.getInstitucion());
			
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
	
	
	

	public static boolean modificar(DtoContactoEmpresa dto )
	{
		boolean retorna=false;
		String consultaStr ="UPDATE	facturacion.contactos_empresa set codigo=codigo ";
		
		consultaStr+=(ConstantesBD.codigoNuncaValido<dto.getEmpresa())?" , empresa="+dto.getEmpresa():"";
		 consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?"":", nombre= '"+dto.getNombre()+"' ";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getDireccion()))?"":" , direccion='"+dto.getDireccion() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getTelefono()))?"":" , telefono='"+dto.getTelefono() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getCargo()))?"":" , cargo='"+dto.getCargo()+"' ";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getEmail()))?"":" , email='"+dto.getEmail()+"' ";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getUsuarioModifica()))?"":" , usuario_modifica='"+dto.getUsuarioModifica() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getFechaModifica()))?"":" , fecha_modifica='"+dto.getFechaModifica() +"'";
		 consultaStr+=(0<dto.getInstitucion())?" , institucion = "+dto.getInstitucion():"";
		 
        consultaStr+=(dto.getCodigo()> 0)?" where codigo= "+ dto.getCodigo():"";
		
        logger.info("\n\n\n\n\n SQL  actualizar ContactoEmpresa / "+consultaStr);
        
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updateDetalleCargo 1 "+e);
		}
		return retorna;
	}
	
	
	
	public static ArrayList<DtoContactoEmpresa> cargar( DtoContactoEmpresa dto) 
	{
		ArrayList<DtoContactoEmpresa> arrayDto= new ArrayList<DtoContactoEmpresa>();
		String consultaStr= "select " +
										" codigo ," + 			//1
										" empresa, " + 			//2
										" nombre ,"+
										" direccion,"+ 			//7
										" telefono ,"+ 			//8
										" email ,"+ 			//9
										" cargo ,"+ 			//10
										" usuario_modifica ,"+ 	//11
										" fecha_modifica , "+ 	//12
										" hora_modifica ,"+ 	//13
										" institucion  "+ 		//14

										"from " +
										" facturacion.contactos_empresa  " +
							"where " +
								"1=1 ";
      
		 consultaStr+=(0<dto.getCodigo())?" AND codigo = "+dto.getCodigo():"";
				 consultaStr+=(ConstantesBD.codigoNuncaValido<dto.getEmpresa())?" AND empresa="+dto.getEmpresa():"";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getNombre()))?"":" AND nombre='"+dto.getNombre() +"'";
  	     consultaStr+=(UtilidadTexto.isEmpty(dto.getDireccion()))?"":" AND direccion='"+dto.getDireccion() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getTelefono()))?"":" AND telefono=' "+dto.getTelefono() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getCargo()))?"":" AND cargo= '"+dto.getCargo()+"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getEmail()))?"":" AND email= '"+dto.getEmail()+"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getUsuarioModifica()))?"":" AND usuario_modifica='"+dto.getUsuarioModifica() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getFechaModifica()))?"":" AND fecha_modifica='"+dto.getFechaModifica() +"'";
		 consultaStr+=(ConstantesBD.codigoNuncaValido<dto.getInstitucion())?" AND institucion = "+dto.getInstitucion():"";
		 	
		 
	     logger.info("\n\n\n\n\n SQL cargarTarjetaCliente / "+consultaStr);
	     try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by nombre ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoContactoEmpresa dtoContactoEmpresa= new DtoContactoEmpresa();
				dtoContactoEmpresa.setCodigo(rs.getDouble("codigo"));
				dtoContactoEmpresa.setNombre(rs.getString("nombre"));
				dtoContactoEmpresa.setDireccion(rs.getString("direccion"));
				dtoContactoEmpresa.setTelefono(rs.getString("telefono"));
				dtoContactoEmpresa.setEmail(rs.getString("email"));
				dtoContactoEmpresa.setCargo(rs.getString("cargo"));
				dtoContactoEmpresa.setUsuarioModifica(rs.getString("usuario_modifica"));
				dtoContactoEmpresa.setFechaModifica(rs.getString("fecha_modifica"));
				dtoContactoEmpresa.setHoraModifica(rs.getString("hora_modifica"));
				dtoContactoEmpresa.setInstitucion(rs.getInt("institucion"));
				arrayDto.add(dtoContactoEmpresa);
			}
			for(DtoContactoEmpresa i : arrayDto)
			{
				//i.logger();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}
	


	public static boolean eliminar( DtoContactoEmpresa dto)
	{
		String consultaStr="DELETE FROM facturacion.contactos_empresa WHERE 1=1 ";
		 consultaStr+=(0<dto.getCodigo())?" AND codigo = "+dto.getCodigo():"";
		 consultaStr+=(ConstantesBD.codigoNuncaValido<dto.getEmpresa())?" AND empresa="+dto.getEmpresa():"";
		 consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?"":"AND nombre= '"+dto.getNombre() +"' ";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getDireccion()))?"":" AND direccion='"+dto.getDireccion() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getTelefono()))?"":" AND telefono=' "+dto.getTelefono() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getCargo()))?"":" AND cargo= '"+dto.getCargo()+"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getEmail()))?"":" AND email= '"+dto.getEmail()+"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getUsuarioModifica()))?"":" AND usuario_modifica='"+dto.getUsuarioModifica() +"'";
		 consultaStr+=(UtilidadTexto.isEmpty(dto.getFechaModifica()))?"":" AND fecha_modifica='"+dto.getFechaModifica() +"'";
		 consultaStr+=(ConstantesBD.codigoNuncaValido<dto.getInstitucion())?" AND institucion = "+dto.getInstitucion():"";
		 
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
			logger.error("ERROR EN eliminarContactoEmpresa ");
			e.printStackTrace();
		}
		return false;
		
	}
	
	

	
}
