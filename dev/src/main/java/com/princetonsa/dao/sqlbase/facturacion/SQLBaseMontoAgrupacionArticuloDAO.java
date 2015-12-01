/**
 * 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class SQLBaseMontoAgrupacionArticuloDAO {
	
	/**
	 * 
	 * Este Método se encarga de insertar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static int insertarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto,int TIPO_BD) {
		try
		{
			String secuencia="";
			int codigoInstitucion=0;
			int codigoGrupo=0;
			int valorSecuencia=0;
			
			switch (TIPO_BD) {
			case DaoFactory.ORACLE:
				 valorSecuencia = UtilidadBD.obtenerSiguienteValorSecuencia(conn, "facturacion.seq_monto_agrup_art ");
				break;
			case DaoFactory.POSTGRESQL:
				secuencia = "nextval('facturacion.seq_monto_agrup_art')";
				break;

			default:
				break;
			}
			
			String[] codigoNaturaleza = new String[2];
			String[] codigosGrupo = new String[2];
			
			if(dto.getCodigoNaturaleza()!=null && !dto.getCodigoNaturaleza().equals("")){	
				codigoNaturaleza = dto.getCodigoNaturaleza().split("-");
				codigoInstitucion=Integer.valueOf(codigoNaturaleza[1]).intValue();
			}else{
				codigoNaturaleza[0]="";
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				codigosGrupo = dto.getGrupoCodigoConcatenado().split("-");
				codigoGrupo=Integer.valueOf(codigosGrupo[0]);
			}
			
			String query="insert into facturacion.monto_agrupacion_articulos ( codigo, detalle_codigo";
			
			if((dto.getCodigoSubgrupoInventario()!=0) &&
					(dto.getCodigoSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				query+=", subgrupo_inventario";				
			}
			
			if(!UtilidadTexto.isEmpty(dto.getCodigoNaturaleza())){
				query+=", naturaleza_articulo, institucion";				
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				query+=", grupo_inventario ";				
			}
			if(dto.getClaseInventarioCodigo()!=ConstantesBD.codigoNuncaValido){
				query+=", clase_inventario ";				
			}
			if(dto.getCantidadArticulo()!=null && dto.getCantidadArticulo()>0){
				query+=", cantidad_articulos ";
			}
			
			query+=", cantidad_monto, valor_monto )";
			
			if(UtilidadTexto.isEmpty(secuencia)){
				query+= " values ( "+valorSecuencia+", "+dto.getDetalleCodigo();
			}else{
				query+= " values ( "+secuencia+", "+dto.getDetalleCodigo();
			}
						
			if((dto.getCodigoSubgrupoInventario()!=0) &&
					(dto.getCodigoSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				query+=", "+dto.getCodigoSubgrupoInventario();
			}
			
			if(!UtilidadTexto.isEmpty(dto.getCodigoNaturaleza())){
				query+=", '"+codigoNaturaleza[0]+"', "+codigoInstitucion;
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				query+=", "+codigoGrupo;
			}
			if(dto.getClaseInventarioCodigo()!=ConstantesBD.codigoNuncaValido){
				query+=", "+dto.getClaseInventarioCodigo();
			}		
			if(dto.getCantidadArticulo()!=null && dto.getCantidadArticulo()>0){
				query+=", "+dto.getCantidadArticulo();
			}
			
			query+=", "+dto.getCantidadMonto()+", "+dto.getValorMonto() +" ) ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));		
			
			int filasAfectadas = ps.executeUpdate();
			
			return filasAfectadas;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}		
		return 0;
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static boolean eliminarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto) {
		boolean eliminacionExitosa = false; 
		try
		{
			String query="delete from facturacion.monto_agrupacion_articulos where codigo = ? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dto.getCodigoAgrupacionArticulo());
			eliminacionExitosa = ps.execute();
		
		}catch (SQLException e)
		{
			e.printStackTrace();
		}	
		
		return eliminacionExitosa;
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos por el id del detalle relacionado
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static boolean eliminarMontoAgrupacionArticuloPorDetalleID(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto) {
		boolean eliminacionExitosa = false; 
		try
		{
			String query="delete from facturacion.monto_agrupacion_articulos where detalle_codigo = ? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dto.getDetalleCodigo());
			eliminacionExitosa = ps.execute();
		
		}catch (SQLException e)
		{
			e.printStackTrace();
		}	
		
		return eliminacionExitosa;
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static int actualizarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto) {
		int filasAfectadas =0; 
		int codigoInstitucion=0;
		int codigoGrupo=0;
		try
		{
			String[] codigoNaturaleza = new String[2];
			String[] codigosGrupo = new String[2];
			if(dto.getCodigoNaturaleza()!=null && !dto.getCodigoNaturaleza().equals("")){				
				codigoNaturaleza = dto.getCodigoNaturaleza().split("-");
				codigoInstitucion=Integer.valueOf(codigoNaturaleza[1]).intValue();
			}else{
				codigoNaturaleza[0]=null;
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				codigosGrupo = dto.getGrupoCodigoConcatenado().split("-");
				codigoGrupo=Integer.valueOf(codigosGrupo[0]);
			}
			
			String query=" update facturacion.monto_agrupacion_articulos set detalle_codigo = ?, subgrupo_inventario = ?, " +
		     " naturaleza_articulo = ?, institucion = ?,  grupo_inventario = ?, clase_inventario = ?, " +
		     " cantidad_articulos = ?, cantidad_monto = ?, valor_monto = ? " +
		     " where codigo = ? ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(
					conn.prepareStatement(query,ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,dto.getDetalleCodigo());
			int indice = 2;
			
			if((dto.getCodigoSubgrupoInventario()!=0) &&
					(dto.getCodigoSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				ps.setInt(indice, dto.getCodigoSubgrupoInventario());
			}else{
				ps.setNull(indice, Types.INTEGER);
			}
			indice++;
			if(!UtilidadTexto.isEmpty(dto.getCodigoNaturaleza())){
				ps.setString(indice,codigoNaturaleza[0]);
				indice++;
				ps.setInt(indice,codigoInstitucion);
			}else{
				ps.setNull(indice, Types.VARCHAR);
				indice++;
				ps.setNull(indice, Types.INTEGER);
			}			
			indice++;
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				ps.setInt(indice, codigoGrupo);
			}else{
				ps.setNull(indice, Types.INTEGER);
			}
			indice++;
			if(dto.getClaseInventarioCodigo()!=ConstantesBD.codigoNuncaValido){
				ps.setInt(indice, dto.getClaseInventarioCodigo());
			}else{
				ps.setNull(indice, Types.INTEGER);
			}	
			indice++;
			if(dto.getCantidadArticulo()!=null && dto.getCantidadArticulo()>0){
				ps.setInt(indice, dto.getCantidadArticulo());				
			}else{
				ps.setNull(indice, Types.INTEGER);
			}	
			indice++;
			
			ps.setInt(indice, dto.getCantidadMonto());
			indice++;
			ps.setDouble(indice, dto.getValorMonto());
			indice++;		
			ps.setInt(indice,dto.getCodigoAgrupacionArticulo());
			
			filasAfectadas = ps.executeUpdate();
		
		}catch (SQLException e){
			e.printStackTrace();
		}	
		
		return filasAfectadas;
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ArrayList<DTOBusquedaMontoAgrupacionArticulo> buscarMontoAgrupacionArticuloPorDetalleID(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto) {
		ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista = new ArrayList<DTOBusquedaMontoAgrupacionArticulo>();
		try
		{
			String query = " select agr.codigo, agr.detalle_codigo, agr.subgrupo_inventario, agr.naturaleza_articulo, " +
					" agr.cantidad_articulos, agr.cantidad_monto, agr.valor_monto, agr.institucion, agr.grupo_inventario, agr.clase_inventario, " +
					" grup.codigo, grup.clase, grup.nombre, clase.nombre, nat.nombre " +
					" from facturacion.monto_agrupacion_articulos agr " +
					" left join inventarios.subgrupo_inventario sub on (agr.subgrupo_inventario = sub.codigo) " +
					" left join inventarios.grupo_inventario  grup  on ( sub.grupo = grup.codigo and sub.clase = grup.clase ) " +
					" left join inventarios.clase_inventario  clase on ( grup.clase = clase.codigo ) " +
					" left join inventarios.naturaleza_articulo nat on ( agr.naturaleza_articulo = nat.acronimo ) " +
					" where agr.detalle_codigo = ? ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,dto.getDetalleCodigo());
			
			ResultSet rs = ps.executeQuery();
			lista = poblarAgrupacionArticulo(rs);		
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return lista;
	}
	
	/**
	 * 
	 * Este Método se encarga de poblar un objeto de tipo
	 * DTOBusquedaMontoAgrupacionArticulo
	 * 
	 * @param  ResultSet rs
	 * @return ArrayList<DTOBusquedaMontoAgrupacionArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	private static ArrayList<DTOBusquedaMontoAgrupacionArticulo> poblarAgrupacionArticulo(ResultSet rs)throws Exception{
		ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista = new ArrayList<DTOBusquedaMontoAgrupacionArticulo>();
		DTOBusquedaMontoAgrupacionArticulo dto=null;
		while (rs.next()) {
			dto = new DTOBusquedaMontoAgrupacionArticulo();
			dto.setCodigoAgrupacionArticulo(rs.getInt(1));
			dto.setDetalleCodigo(rs.getInt(2));
			dto.setCodigoSubgrupoInventario(rs.getInt(3));
			dto.setCodigoNaturaleza(rs.getString(4));
			if(rs.getInt(5)>0){
				dto.setCantidadArticulo(rs.getInt(5));
			}			
			dto.setCantidadMonto(rs.getInt(6));
			dto.setValorMonto(rs.getDouble(7));
			dto.setCodigoInstitucion(rs.getInt(8));
			dto.setGrupoCodigo(rs.getInt(9));
			dto.setClaseInventarioCodigo(rs.getInt(10));
			
			if(dto.getGrupoCodigo()==0){
				dto.setGrupoCodigo(rs.getInt(11));
			}
			if(dto.getClaseInventarioCodigo()==0){
				dto.setClaseInventarioCodigo(rs.getInt(12));
			}
			
			if(dto.getCodigoNaturaleza()!=null && !dto.getCodigoNaturaleza().equals("")){
				dto.setCodigoNaturaleza(rs.getString(4)+"-"+rs.getInt(8));		
			}			
			
			if((dto.getGrupoCodigo()>0) && 
					(dto.getClaseInventarioCodigo()>0)){
				dto.setGrupoCodigoConcatenado(dto.getGrupoCodigo()+"-"+dto.getClaseInventarioCodigo());
			}
			
			dto.setGrupoNombre(rs.getString(13));
			dto.setClaseNombre(rs.getString(14));
			dto.setNombreNaturaleza(rs.getString(15));					
			
			if(dto.getGrupoCodigo()>0 && dto.getClaseInventarioCodigo()>0){
				dto.setGrupoCodigoConcatenado(dto.getGrupoCodigo()+"-"+dto.getClaseInventarioCodigo());
			}
			lista.add(dto);
		}			
		return lista;
	}

}
