package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoServiciosAdicionalesProfesionales;

public class SqlBaseServiAdicionalesXProfAtenOdontoDao {

	private static Logger logger = Logger.getLogger(SqlBaseServiAdicionalesXProfAtenOdontoDao.class);
	
	
	/**
	 * Cadena Sql para realizar la consulta de Servicios Adicionales Profesionales de la Salud de Atencion Odonotologica
	 */
	private static String consultaServiciosAdicionalesProfesionalesStr="SELECT DISTINCT " +
			"seradd.codigo_medico AS codmedico, " +
			"seradd.codigo_servicio AS codservicio, " +
			"seradd.institucion AS institucion, " +
			"getNombreServicio(seradd.codigo_servicio,"+ConstantesBD.separadorSplit+") AS descripcionserv, " +
			"coalesce (facturacion.getcodigopropservicio2(seradd.codigo_servicio,"+ConstantesBD.separadorSplit+"),facturacion.getcodigopropservicio2(seradd.codigo_servicio,0)) AS codpropietario " +
			"FROM odontologia.serv_adicionales_profesionales seradd " +
			"INNER JOIN facturacion.servicios serv ON (serv.codigo = seradd.codigo_servicio) " +
			"WHERE seradd.codigo_medico = ? ";
	
	
	/**
	 * Cadena Sql para realizar la insercion de un nuevo servicio adicional
	 */
	private static String insertarServicioAdicionalProfesionalStr= "INSERT INTO odontologia.serv_adicionales_profesionales ( " +
			"codigo_medico, " +
			"codigo_servicio, " +
			"institucion, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica )  " +
			"VALUES (?, ?, ?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ? ) ";

	/**
	 * Cadena Sql para eliminar un servicio adicional por profesional de la Salud de Atención Odontologica
	 */
     private static String eliminarServicioAdicionalProfesionalStr="DELETE FROM odontologia.serv_adicionales_profesionales WHERE codigo_medico = ? AND codigo_servicio = ?  AND  institucion = ? ";
    
	
	/**
	 * Metodo para consultar Servicios Adicionales por Profesional de la salud de atencion odontologica
	 * @param codigoMedico
	 * @return
	 */
	public static ArrayList<DtoServiciosAdicionalesProfesionales> consultarServiciosAdicionalesProfesionales(int codigoMedico, int codEstandar)
	{
		ArrayList<DtoServiciosAdicionalesProfesionales> arrayServProf=new ArrayList<DtoServiciosAdicionalesProfesionales>();
		String consultaServAdd = consultaServiciosAdicionalesProfesionalesStr;
		consultaServAdd=consultaServAdd.replace(ConstantesBD.separadorSplit, codEstandar+"");
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
	   	
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaServAdd,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoMedico);
			
			logger.info("\n\nCadena de consulta Servicios Adicioinales>> "+consultaServAdd+"  CodMedico:"+codigoMedico );
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
			  DtoServiciosAdicionalesProfesionales dto=new DtoServiciosAdicionalesProfesionales();
			  dto.setCodigoMedico(rs.getInt("codmedico")+"");
			  dto.setCodigoServicio(rs.getInt("codservicio")+"");
			  dto.setInstitucion(rs.getInt("institucion")+"");
			  dto.setDescripcionServicio(rs.getString("descripcionserv"));
			  dto.setCodigoPropietario(rs.getString("codpropietario"));
			  
				arrayServProf.add(dto);
			}
		
		   ps.close();
		   
		  
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("error en  consulta Servicios Adicionales >>  cadena >> "+consultaServAdd+" codMedico"+codigoMedico);
			
		}
		
		UtilidadBD.closeConnection(con);
		return arrayServProf;
	}
	
	
	/**
	 * Metodo para insertar un servicio adicional a un profesional de la salud de atencion odontologica
	 * @param con
	 * @param nuevoServicio
	 * @return
	 */
	public static boolean insertarServicioAdicionalProfesional(Connection con, DtoServiciosAdicionalesProfesionales nuevoServicio)
	{
		boolean insertar=false;
		String cadenaInsercion=insertarServicioAdicionalProfesionalStr;
		
          try{
	   		 
	   		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		ps.setInt(1, Utilidades.convertirAEntero(nuevoServicio.getCodigoMedico()));
	   		ps.setInt(2, Utilidades.convertirAEntero(nuevoServicio.getCodigoServicio()));
	   		ps.setInt(3,Utilidades.convertirAEntero(nuevoServicio.getInstitucion()));
	   		ps.setString(4, nuevoServicio.getUsuarioModificacion());
	   		
	   		if(ps.executeUpdate()>0)
			{
	   			insertar=true;
			}
	   		
	   	    ps.close();
		    }
	         catch (SQLException e) 
				{
					logger.info("Error en Crear Servicio Adicional>> "+cadenaInsercion    );
					e.printStackTrace();
				}
		
		return insertar;
	}
	
	/**
	 * Metodo para eliminar un Servicio adicional de un Profesional de la Salud de Atencion Odontologica
	 * @param codConvencion
	 * @return
	 */
	public static boolean eliminarServicioAdicionalProfesional(int codMedico, int codServicio, int codInstitucion)
	{
		
     String cadenaEliminar= eliminarServicioAdicionalProfesionalStr;
     boolean resp= false;
		
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codMedico);
			ps.setInt(2,codServicio);
			ps.setInt(3,codInstitucion);
			
			logger.info("Eliminacion Convencion Odontologica Cadena>> "+cadenaEliminar+"  CodMedico> "+codMedico+" codServicio> "+codServicio+"cosInstitucion> "+codInstitucion  );
			
			if(ps.executeUpdate()>0)
			{
				resp=true;
			}
		}
		catch (SQLException e) 
		{
			logger.info("Error en Eliminacion Convencion Odontologica  Cadena>> "+cadenaEliminar+"  CodMedico> "+codMedico+" codServicio> "+codServicio+"cosInstitucion> "+codInstitucion  );
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		
		
		return resp;
	}

    
	/**
	 * Metodo para insertar una lista de los Servicios Adicionales asociados a un Profesional
	 * @param con
	 * @param listServiciosSel
	 * @param codProfesional
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarServiciosAdicionales(Connection con,HashMap<String, Object> listServiciosSel, int codProfesional,	String loginUsuario,int codInstitucion) {
		
		boolean exito=true;
		int numRegistros=Utilidades.convertirAEntero(listServiciosSel.get("numRegistros").toString());
		
		for(int i=0;i<numRegistros;i++)
		{
			if(listServiciosSel.get("fueEliminado_"+i)==null)
			{
			DtoServiciosAdicionalesProfesionales dto=new DtoServiciosAdicionalesProfesionales();
			dto.setCodigoMedico(codProfesional+"");
			dto.setCodigoServicio(listServiciosSel.get("codigoServicio_"+i).toString());
			dto.setInstitucion(codInstitucion+"");
			dto.setUsuarioModificacion(loginUsuario);	
			
				if(!insertarServicioAdicionalProfesional(con,dto))
				{
					exito=false;
					i=numRegistros;
				}
			}
		}	
		
		return exito;
	}


	
}
