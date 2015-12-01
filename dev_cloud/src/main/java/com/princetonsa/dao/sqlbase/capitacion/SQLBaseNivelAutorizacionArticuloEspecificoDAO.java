/**
 * 
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;

/**
 * Esta clase se encarga de ejecutar los métodos de
 * negocio para la entidad NivelAutorizacionArticuloEspecifico
 * @author Angela Aguirre
 *
 */
public class SQLBaseNivelAutorizacionArticuloEspecificoDAO {
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * de nivel de autorización de artículos específicos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	private static Logger logger = Logger.getLogger(SQLBaseNivelAutorizacionArticuloEspecificoDAO.class);
	public static ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> buscarNivelAutorizacionArticuloEspecifico(Connection conn,
			DTOBusquedaNivelAutorArticuloEspecifico dto) {
		ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> lista = new ArrayList<DTOBusquedaNivelAutorArticuloEspecifico>();
		ResultSet rs  = null;
		PreparedStatementDecorator ps= null;
		try
		{
			String query =" select art_esp.codigo_pk, art_esp.nivel_autor_serv_medic, art_esp.articulo, art.descripcion " +
					" from  capitacion.nivel_autor_articulo art_esp, inventarios.articulo art where art_esp.nivel_autor_serv_medic = ? " +
					" and art_esp.articulo = art.codigo ";
			
			ps= new PreparedStatementDecorator(conn.prepareStatement(query));
			
			ps.setInt(1,dto.getNivelAutoriSerArt());
			rs = ps.executeQuery();
			lista = poblarArticuloEspecifico(rs);			
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}	finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
		}	
		
		return lista;
	}
	
	/**
	 * 
	 * Este Método se encarga de poblar un objeto de tipo
	 * DTOBusquedaNivelAutorArticuloEspecifico
	 * 
	 * @param  ResultSet rs
	 * @return ArrayList<DTOBusquedaNivelAutorArticuloEspecifico>
	 * @author, Angela Maria Aguirre
	 *
	 */
	private static ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> poblarArticuloEspecifico(ResultSet rs)throws Exception{
		ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> lista = new ArrayList<DTOBusquedaNivelAutorArticuloEspecifico>();
		DTOBusquedaNivelAutorArticuloEspecifico dto=null;
		while (rs.next()) {
			dto = new DTOBusquedaNivelAutorArticuloEspecifico();
			dto.setCodigo(rs.getInt(1));
			dto.setNivelAutoriSerArt(rs.getInt(2));
			dto.setArticuloCodigo(rs.getInt(3));			
			dto.setArticuloDescripcion(rs.getString(4));
			
			lista.add(dto);
		}			
		return lista;
	}


}
