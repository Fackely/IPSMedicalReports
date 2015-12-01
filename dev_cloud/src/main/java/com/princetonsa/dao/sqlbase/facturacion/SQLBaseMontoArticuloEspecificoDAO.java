/**
 * 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.ConstantesBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class SQLBaseMontoArticuloEspecificoDAO {
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * de artículos específicos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ArrayList<DTOBusquedaMontoArticuloEspecifico> buscarMontoArticuloEspecifico(Connection conn,
			DTOBusquedaMontoArticuloEspecifico dto) {
		ArrayList<DTOBusquedaMontoArticuloEspecifico> lista = new ArrayList<DTOBusquedaMontoArticuloEspecifico>();
		try
		{
			String query =" select art_esp.codigo, art_esp.detalle_codigo, art_esp.articulo, art_esp.cantidad_articulos, " +
					" art_esp.cantidad_monto, art_esp.valor_monto, art.descripcion " +
					" from  facturacion.monto_articulo_especifico art_esp, inventarios.articulo art " +
					" where art_esp.detalle_codigo = ? " +
					" and art_esp.articulo = art.codigo ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,dto.getDetalleCodigo());
			ResultSet rs = ps.executeQuery();
			lista = poblarArticuloEspecifico(rs);			
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return lista;
	}
	
	/**
	 * 
	 * Este Método se encarga de poblar un objeto de tipo
	 * DTOBusquedaMontoArticuloEspecifico
	 * 
	 * @param  ResultSet rs
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico>
	 * @author, Angela Maria Aguirre
	 *
	 */
	private static ArrayList<DTOBusquedaMontoArticuloEspecifico> poblarArticuloEspecifico(ResultSet rs)throws Exception{
		ArrayList<DTOBusquedaMontoArticuloEspecifico> lista = new ArrayList<DTOBusquedaMontoArticuloEspecifico>();
		DTOBusquedaMontoArticuloEspecifico dto=null;
		while (rs.next()) {
			dto = new DTOBusquedaMontoArticuloEspecifico();
			dto.setCodigo(rs.getInt(1));
			dto.setDetalleCodigo(rs.getInt(2));
			dto.setArticuloCodigo(rs.getInt(3));
			if(rs.getInt(4)>0){
				dto.setCantidadArticulos(rs.getInt(4));
			}			
			dto.setCantidadMonto(rs.getInt(5));
			dto.setValorMonto(rs.getDouble(6));
			dto.setArticuloDescripcion(rs.getString(7));
			
			lista.add(dto);
		}			
		return lista;
	}


}
