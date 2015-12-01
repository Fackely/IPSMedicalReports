/** 
 * 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.facturacion.DtoInfoMontoCobroDetallado;

/**
 * @author armando
 *
 */
public class SqlBaseCalculoValorCobrarPacienteDao 
{

	public static DtoInfoMontoCobroDetallado obtenerInfoMontoCobroServicioArticulo(int codigoDetalleMonto, int codigoServicio, int codigoArticulo) 
	{
		DtoInfoMontoCobroDetallado resultado=new DtoInfoMontoCobroDetallado();
		//por servicio
		if(codigoServicio>0)
		{
			resultado=obtenerInfoMontoServicio(codigoDetalleMonto,codigoServicio);
		}
		//por articulo
		else if(codigoArticulo>0)
		{
			resultado=obtenerInfoMontoArticulo(codigoDetalleMonto,codigoArticulo);
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoDetalleMonto
	 * @param codigoArticulo
	 * @return
	 */
	private static DtoInfoMontoCobroDetallado obtenerInfoMontoArticulo(int codigoDetalleMonto, int codigoArticulo) 
	{
		DtoInfoMontoCobroDetallado resultado=new DtoInfoMontoCobroDetallado();
		Connection con=UtilidadBD.abrirConexion();
		String consultaArticuloEspecifico="SELECT " +
												" codigo as codigo," +
												" articulo as articulocodigo," +
												" cantidad_articulos as cantidadarticulo," +
												" coalesce(cantidad_monto,-1) as cantidadmonto," +
												" valor_monto as valormonto " +
										" from monto_articulo_especifico " +
										" where " +
												" detalle_codigo=? and " +
												" articulo=?";
		
		String consultaArticuloAgrupado="SELECT " +
											" maa.codigo as codigo," +
											" maa.subgrupo_inventario as codigosubgrupo," +
											//" s.subgrupo as subgrupoinventario," +
											" maa.grupo_inventario as grupoinventario," +
											" maa.clase_inventario as claseinventario," +
											" maa.naturaleza_articulo as naturalezaarticulo," +
											" maa.cantidad_articulos as cantidadarticulo," +
											" coalesce(cantidad_monto,-1) as cantidadmonto," +
											" maa.valor_monto as valormonto " +
										" from monto_agrupacion_articulos maa " +
										" where " +
													" detalle_codigo=? " +
													"and (maa.subgrupo_inventario=(select a.subgrupo from articulo a where a.codigo=?) or maa.subgrupo_inventario is null) " +
													"and (maa.grupo_inventario=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or maa.grupo_inventario is null) " +
													"and (maa.clase_inventario=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or maa.clase_inventario is null) " +
													"and (maa.naturaleza_articulo=(select a.naturaleza from articulo a where a.codigo=?) or maa.naturaleza_articulo is null) ";
								consultaArticuloAgrupado+=" ORDER BY maa.subgrupo_inventario, maa.grupo_inventario, maa.clase_inventario, maa.naturaleza_articulo ";


		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consultaArticuloEspecifico);
			ps.setInt(1, codigoDetalleMonto);
			ps.setInt(2, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado.setPorAgrupacion(false);
				resultado.setCodigo(rs.getInt("codigo"));
				resultado.setArticulo(rs.getInt("articulocodigo"));
				resultado.setCantidadArticuloServicio(rs.getInt("cantidadarticulo"));
				resultado.setCantidadMonto(rs.getInt("cantidadmonto"));
				resultado.setValorMonto(rs.getDouble("valormonto"));
			}
			//buscar por agrupacion
			else
			{
				ps=new PreparedStatementDecorator(con,consultaArticuloAgrupado);
				ps.setInt(1, codigoDetalleMonto);
				ps.setInt(2, codigoArticulo);
				ps.setInt(3, codigoArticulo);
				ps.setInt(4, codigoArticulo);
				ps.setInt(5, codigoArticulo);
				rs=new ResultSetDecorator(ps.executeQuery());
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado.setPorAgrupacion(true);
					resultado.setCodigo(rs.getInt("codigo"));
					resultado.setCantidadArticuloServicio(rs.getInt("cantidadarticulo"));
					resultado.setCantidadMonto(rs.getInt("cantidadmonto"));
					resultado.setValorMonto(rs.getDouble("valormonto"));
					resultado.setCodigoSubgrupoArticulo(rs.getInt("codigosubgrupo"));
					resultado.setSubGrupoArticulo(rs.getInt("subgrupoinventario"));
					resultado.setGrupoArticulo(rs.getInt("grupoinventario"));
					resultado.setClaseArticulo(rs.getInt("claseinventario"));
					resultado.setNaturalezaArticulo(rs.getString("naturalezaarticulo"));
					resultado.setArticulo(codigoArticulo);
				}	
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.info("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}

	/**
	 * 
	 */
	private static DtoInfoMontoCobroDetallado obtenerInfoMontoServicio(int codigoDetalleMonto, int codigoServicio) 
	{
		DtoInfoMontoCobroDetallado resultado=new DtoInfoMontoCobroDetallado();
		Connection con=UtilidadBD.abrirConexion();
		String consultaServicioEspecifico="SELECT " +
												" codigo as codigo," +
												" servicio_codigo as servicio," +
												" cantidad_servicio as cantidadservicio," +
												" coalesce(cantidad_monto,-1) as cantidadmonto," +
												" valor_monto as valormonto " +
										" from monto_servicio_especifico " +
										" where " +
												" detalle_codigo=? and " +
												" servicio_codigo=?";
		
		String consultaServicioAgrupado="SELECT " +
											" codigo as codigo," +
											" grupo_servicio_codigo as gruposervicio," +
											" acronimo_tipo_servicio as tiposervicio," +
											" especialidad_codigo as especialidadservicio," +
											" cantidad_servicio as cantidadservicio," +
											" coalesce(cantidad_monto,-1) as cantidadmonto," +
											" valor_monto as valormonto " +
										" from monto_agrupacion_servicios " +
										" where " +
													" detalle_codigo=? " +
													" and ( (especialidad_codigo=(select s.especialidad from servicios s where s.codigo=?)) or (especialidad_codigo is null)) " +
													" and ( (acronimo_tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?)) or (acronimo_tipo_servicio is null)) " +
													" and ( (grupo_servicio_codigo=(select s.grupo_servicio from servicios s where s.codigo=?)) or (grupo_servicio_codigo is null)) ";
								consultaServicioAgrupado+=" ORDER BY especialidad_codigo, acronimo_tipo_servicio, grupo_servicio_codigo ";


		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consultaServicioEspecifico);
			ps.setInt(1, codigoDetalleMonto);
			ps.setInt(2, codigoServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado.setPorAgrupacion(false);
				resultado.setCodigo(rs.getInt("codigo"));
				resultado.setServicio(rs.getInt("servicio"));
				resultado.setCantidadArticuloServicio(rs.getInt("cantidadservicio"));
				resultado.setCantidadMonto(rs.getInt("cantidadmonto"));
				resultado.setValorMonto(rs.getDouble("valormonto"));
			}
			//buscar por agrupacion
			else
			{
				ps=new PreparedStatementDecorator(con,consultaServicioAgrupado);
				ps.setInt(1, codigoDetalleMonto);
				ps.setInt(2, codigoServicio);
				ps.setInt(3, codigoServicio);
				ps.setInt(4, codigoServicio);
				rs=new ResultSetDecorator(ps.executeQuery());
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado.setPorAgrupacion(true);
					resultado.setCodigo(rs.getInt("codigo"));
					resultado.setCantidadArticuloServicio(rs.getInt("cantidadservicio"));
					resultado.setCantidadMonto(rs.getInt("cantidadmonto"));
					resultado.setValorMonto(rs.getDouble("valormonto"));
					resultado.setGrupoServicio(rs.getInt("gruposervicio"));
					resultado.setTipoServicio(rs.getString("tiposervicio"));
					resultado.setEspecialidad(rs.getInt("especialidadservicio"));
					resultado.setServicio(codigoServicio);
				}	
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.info("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}

}
