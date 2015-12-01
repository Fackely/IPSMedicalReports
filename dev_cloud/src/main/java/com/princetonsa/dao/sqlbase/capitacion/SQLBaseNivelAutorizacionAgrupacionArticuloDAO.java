/**
 * 
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;

/**
 * Esta clase se encarga de ejecutar los métodos de
 * negocio para la entidad NivelAutorizacionAgrupacionArticulo
 * @author Angela Aguirre
 *
 */
public class SQLBaseNivelAutorizacionAgrupacionArticuloDAO {
	
	public static Logger logger = Logger.getLogger(SQLBaseNivelAutorizacionAgrupacionArticuloDAO.class);
	/**
	 * 
	 * Este Método se encarga de insertar un registro de nivel de autorización
	 * de agrupación de artículos
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static int insertarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto,int TIPO_BD) {
		PreparedStatementDecorator ps=  null;
		try
		{
			int codigoInstitucion=0;
			int codigoGrupo=0;
			int valorSecuencia=0;
			
			valorSecuencia = UtilidadBD.obtenerSiguienteValorSecuencia(conn, "capitacion.seq_nivel_autor_agr_art");
			 
			String[] codigoNaturaleza = new String[2];
			String[] codigosGrupo = new String[2];
			
			if(dto.getAcronimoNaturaleza()!=null && !dto.getAcronimoNaturaleza().equals("")){	
				codigoNaturaleza = dto.getAcronimoNaturaleza().split("-");
				codigoInstitucion=Integer.valueOf(codigoNaturaleza[1]).intValue();
			}else{
				codigoNaturaleza[0]="";
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				codigosGrupo = dto.getGrupoCodigoConcatenado().split("-");
				codigoGrupo=Integer.valueOf(codigosGrupo[0]);
			}
			
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
						
			String horaRegistra = UtilidadFecha.conversionFormatoHoraABD(Calendar
					.getInstance().getTime());
			
			String query = "insert into capitacion.nivel_autor_agr_art ( codigo_pk, nivel_autor_serv_medic," +
					" grupo_inventario, clase_inventario, subgrupo_inventario, naturaleza_articulo, institucion," +
					" fecha_registro, hora_registro, usuario_registra ) " +			
			        " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";	
			
			ps= new PreparedStatementDecorator(conn.prepareStatement(query));
			
			ps.setInt(1, valorSecuencia);				
			ps.setInt(2, dto.getNivelAutoriSerArt());
				
				
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				ps.setInt(3, codigoGrupo);	
			}else{
				ps.setNull(3, Types.INTEGER);
			}
			
			if(dto.getClaseInventario()!=ConstantesBD.codigoNuncaValido){
				ps.setInt(4, dto.getClaseInventario());		
			}else{
				ps.setNull(4, Types.INTEGER);
			}
			
			if((dto.getSubgrupoInventario()!=null) && (dto.getSubgrupoInventario()!=0) &&
					(dto.getSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				ps.setInt(5, dto.getSubgrupoInventario());	
			}else{
				ps.setNull(5, Types.INTEGER);
			}
			
			if(!UtilidadTexto.isEmpty(dto.getAcronimoNaturaleza())){
				ps.setString(6, codigoNaturaleza[0]);
				ps.setInt(7, codigoInstitucion);
			}else{
				ps.setNull(6, Types.VARCHAR);
				ps.setNull(7, Types.INTEGER);
			}
			
			ps.setDate(8, new java.sql.Date(fechaActual.getTime()));
			ps.setString(9, horaRegistra);
			ps.setString(10, dto.getLoginRegistra());			
			
			int filasAfectadas = ps.executeUpdate();			
			return filasAfectadas;			
		}
		catch (SQLException e){
			e.printStackTrace();
		}	finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionAgrupacionArticuloDAO "+sqlException.toString() );
			}
			
		}	
		return 0;
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de nivel de autorización
	 * de agrupación de artículos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static boolean eliminarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		boolean eliminacionExitosa = false; 
		PreparedStatementDecorator ps=  null;
		try
		{
			String query="delete from capitacion.nivel_autor_agr_art where codigo_pk = ? ";
			ps= new PreparedStatementDecorator(conn.prepareStatement(query));
			ps.setInt(1, dto.getCodigoPk());
			ps.execute();
			eliminacionExitosa = true;
		
		}catch (SQLException e)
		{
			e.printStackTrace();			
		}	finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionAgrupacionArticuloDAO "+sqlException.toString() );
			}
			
		}
		
		return eliminacionExitosa;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar un registro de autorización
	 * de agrupación de artículos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static int actualizarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		int filasAfectadas =0; 
		int codigoInstitucion=0;
		int codigoGrupo=0;
		PreparedStatementDecorator ps= null;
		try
		{
			String[] codigoNaturaleza = new String[2];
			String[] codigosGrupo = new String[2];
			
			if(!UtilidadTexto.isEmpty(dto.getAcronimoNaturaleza())){				
				codigoNaturaleza = dto.getAcronimoNaturaleza().split("-");
				codigoInstitucion=Integer.valueOf(codigoNaturaleza[1]).intValue();
			}else{
				codigoNaturaleza[0]=null;
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				codigosGrupo = dto.getGrupoCodigoConcatenado().split("-");
				codigoGrupo=Integer.valueOf(codigosGrupo[0]);
			}
			
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();			
						
			String query="update capitacion.nivel_autor_agr_art set nivel_autor_serv_medic = ?, grupo_inventario=?, clase_inventario = ?, " +
				  "subgrupo_inventario=?, naturaleza_articulo = ?, institucion = ?, fecha_registro=?, hora_registro = ?, " +
				  "usuario_registra=? where codigo_pk = ? ";
			
			 ps= new PreparedStatementDecorator(conn.prepareStatement(query));
			
			ps.setInt(1,dto.getNivelAutoriSerArt());
			int indice = 2; 
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				ps.setInt(indice, codigoGrupo);
			}else{
				ps.setNull(indice, Types.INTEGER);
			}
			indice++;
			
			if(dto.getClaseInventario()!=ConstantesBD.codigoNuncaValido){
				ps.setInt(indice, dto.getClaseInventario());
			}else{
				ps.setNull(indice, Types.INTEGER);
			}
			indice++;
			
			if((dto.getSubgrupoInventario()!=null) && (dto.getSubgrupoInventario()!=0) &&
					(dto.getSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				ps.setInt(indice, dto.getSubgrupoInventario());
			}else{
				ps.setNull(indice, Types.INTEGER);
			}
			indice++;
			
			if(!UtilidadTexto.isEmpty(dto.getAcronimoNaturaleza())){
				ps.setString(indice,codigoNaturaleza[0]);
				indice++;
				ps.setInt(indice,codigoInstitucion);
				
			}else{
				ps.setNull(indice,Types.VARCHAR);
				indice++;
				ps.setNull(indice,Types.INTEGER);
			}
			
			indice++;
			ps.setDate(indice, new java.sql.Date(fechaActual.getTime()));
			indice++;
			ps.setString(indice, UtilidadFecha.conversionFormatoHoraABD(Calendar
					.getInstance().getTime()));
			indice++;
			ps.setString(indice, dto.getLoginRegistra());
			indice++;		
			ps.setInt(indice,dto.getCodigoPk());
			
			filasAfectadas = ps.executeUpdate();
		
		}catch (SQLException e){
			e.printStackTrace();
		}	finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionAgrupacionArticuloDAO "+sqlException.toString() );
			}
			
		}		
		return filasAfectadas;
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de nivel de autorización
	 * de agrupación de artículos por el id del nivel de autorización de
	 * servicios - artículos relacionado
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> buscarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> lista = new ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo>();
		PreparedStatementDecorator ps= null;
		ResultSet rs = null;
		try
		{
			String query = "select agr.codigo_pk, agr.nivel_autor_serv_medic, agr.grupo_inventario,agr.clase_inventario, " +
					"agr.subgrupo_inventario, agr.naturaleza_articulo, " +
					"agr.institucion, grup.codigo, grup.clase " +
					"from capitacion.nivel_autor_agr_art agr " +
					"left join inventarios.subgrupo_inventario sub on (agr.subgrupo_inventario = sub.codigo) " +
					"left join inventarios.grupo_inventario grup on (sub.grupo = grup.codigo and sub.clase = grup.clase ) " +
					"where agr.nivel_autor_serv_medic = ? ";
			
					ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,dto.getNivelAutoriSerArt());
			rs = ps.executeQuery();
			lista = poblarAgrupacionArticulo(rs);		
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionAgrupacionArticuloDAO "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionAgrupacionArticuloDAO "+sqlException.toString() );
			}
		}
		
		return lista;
	}
	
	/**
	 * 
	 * Este Método se encarga de poblar un objeto de tipo
	 * DTOBusquedaNivelAutorAgrupacionArticulo
	 * 
	 * @param  ResultSet rs
	 * @return ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	private static ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> poblarAgrupacionArticulo(ResultSet rs)throws Exception{
		ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> lista = new ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo>();
		DTOBusquedaNivelAutorAgrupacionArticulo dto=null;
		while (rs.next()) {
			dto = new DTOBusquedaNivelAutorAgrupacionArticulo();
			dto.setCodigoPk(rs.getInt(1));
			dto.setNivelAutoriSerArt(rs.getInt(2));
			dto.setGrupoInventario(rs.getInt(3));
			dto.setClaseInventario(rs.getInt(4));
			dto.setSubgrupoInventario(rs.getInt(5));
			dto.setAcronimoNaturaleza(rs.getString(6));
			dto.setCodigoInstitucion(rs.getInt(7));
			
			if(dto.getGrupoInventario()==null || dto.getGrupoInventario()==0){
				dto.setGrupoInventario(rs.getInt(8));
			}
			if(dto.getClaseInventario()==null || dto.getClaseInventario()==0){
				dto.setClaseInventario(rs.getInt(9));
			}
			
			if(dto.getAcronimoNaturaleza()!=null && !dto.getAcronimoNaturaleza().equals("")){
				dto.setAcronimoNaturaleza(rs.getString(6)+"-"+rs.getInt(7));		
			}			
			
			if((dto.getGrupoInventario()!=null && dto.getGrupoInventario()>0) && 
					(dto.getClaseInventario()!=null && dto.getClaseInventario()>0)){
				dto.setGrupoCodigoConcatenado(dto.getGrupoInventario()+"-"+dto.getClaseInventario());
			}			
			
			lista.add(dto);
		}			
		return lista;
	}


}
