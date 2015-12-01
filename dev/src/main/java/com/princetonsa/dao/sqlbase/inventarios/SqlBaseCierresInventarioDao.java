/*
 * Creado el 26/12/2005
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseCierresInventarioDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCierresInventarioDao.class);

	private static String cadenaInsercionDetalleMovInventarios="INSERT INTO det_cierre_inventarios (" +
																" codigo_cierre," +
																" almacen," +
																" articulo," +
																" val_total_entradas," +
																" val_total_salidas, " +
																" cantidad_total_entradas," +
																" cantidad_total_salidas," +
																" costo_unitario_final_mes," +
																" val_total_entradas_anio," +
																" val_total_salidas_anio," +
																" cantidad_total_entradas_anio," +
																" cantidad_total_salidas_anio," +
																" lote," +
																" fecha_vencimiento, " +
																" codigo)" +
																" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 



	public static HashMap cargarMovimientosInventarios(Connection con, String consulta)
	{
		HashMap mapa=new HashMap();
        PreparedStatementDecorator ps = null;
		
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("cargarMovimientosInventarios-->"+ps);
            
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

        } catch (SQLException e) {
        	logger.error("cargarMovimientosInventarios: " + e);
        } catch(Exception ex){
        	logger.error("cargarMovimientosInventarios: " + ex);	
        } finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
        }
        
        return (HashMap)mapa.clone();
	}

	/**
	 * Metodo que genera el cierre dado un mapa con el encabezado del cierre
	 * y un mapa con el detalle de los movimientos.
	 * @param con
	 * @param encabezadoCirre
	 * @param movimientos
	 * @param codigo 
	 * @return
	 */
	
	public static boolean generarCierre(Connection con, HashMap encabezadoCirre, HashMap movimientos, String codigo)
	{
		String cadenaInsertCierreEncabezado="INSERT INTO " +
												" cierre_inventarios " +
												" (codigo," +
												"institucion," +
												"anio_cierre," +
												"mes_cierre," +
												"observaciones," +
												"tipo_cierre," +
												"fecha_generacion," +
												"hora_generacion," +
												"usuario) " +
												" values(?,?,?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
		String  codigoCierre="";
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		try
		{
			PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(codigo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rsTemp=new ResultSetDecorator(psTemp.executeQuery());
			if(rsTemp.next())
			{
				codigoCierre=rsTemp.getString("codigo");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertCierreEncabezado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,codigoCierre);
				ps.setInt(2,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""));
				ps.setInt(3,Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+""));
				ps.setInt(4,Utilidades.convertirAEntero(encabezadoCirre.get("mescierre")+""));
				ps.setString(5,encabezadoCirre.get("observaciones")+"");
				ps.setInt(6,Utilidades.convertirAEntero(encabezadoCirre.get("tipocierre")+""));
				ps.setString(7,encabezadoCirre.get("usuario")+"");
				enTransaccion=(ps.executeUpdate()>0);
				//guardar el detalle dcierre de inventarios
				if(enTransaccion)
				{
					//articulos que presentaron movimientos en diciembre
					enTransaccion=guardarDetalleMovimientos(con,movimientos,encabezadoCirre,codigoCierre);
					//si es cierre final, tambien deben mirarse los articulos que no tienen movimientos en diciembre.
					if(enTransaccion&&(Utilidades.convertirAEntero(encabezadoCirre.get("tipocierre")+"")==ConstantesBDInventarios.tipoCierreAnual))
					{
					//articulos que no presentaron movimientos en diciembre
					enTransaccion=guardarMovimientoArticulosNoDiciembre(con,encabezadoCirre,codigoCierre);
					}

				}
				ps.close();
			}
			else
			{
				enTransaccion=false;
			}
			rsTemp.close();
			psTemp.close();
			
		}
		catch (SQLException e)
		{
			enTransaccion=false;
			e.printStackTrace();
		}
		
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return false;
	}

	private static boolean guardarMovimientoArticulosNoDiciembre(Connection con, HashMap encabezadoCirre, String codigoCierre)
	{
		PreparedStatementDecorator ps=null;
		HashMap movimientos=new HashMap();
		String cadena="SELECT almacen as almacen,articulo as articulo,'0' as valtotalentradas,'0' as valtotalsalida,'0' as cantotalentrada,'0' as cantidadsalida, lote as lote, fecha_vencimiento as fechavencimiento from det_cierre_inventarios where almacen||'-'||articulo not in (select almacen||'-'||articulo from det_cierre_inventarios where codigo_cierre='"+codigoCierre+"') group by almacen,articulo,lote, fecha_vencimiento ";
		boolean enTransaccion=true;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			movimientos=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			for(int i=0;i<Utilidades.convertirAEntero(movimientos.get("numRegistros")+"");i++)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionDetalleMovInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				String lote=movimientos.get("lote_"+i)+"";
				String fechaVencimiento=movimientos.get("fechavencimiento_"+i)+"";
				
				ps.setString(1,codigoCierre);
				ps.setInt(2,Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""));
				ps.setInt(3,Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""));
				ps.setDouble(4,Utilidades.convertirADouble(movimientos.get("valtotalentradas_"+i)+""));
				ps.setDouble(5,Utilidades.convertirADouble(movimientos.get("valtotalsalida_"+i)+""));
				ps.setDouble(6,Utilidades.convertirADouble(movimientos.get("cantotalentrada_"+i)+""));
				ps.setDouble(7,Utilidades.convertirADouble(movimientos.get("cantidadsalida_"+i)+""));
				ps.setDouble(8,Utilidades.convertirADouble(UtilidadInventarios.obtenerCostoUnitarioFinalMes(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",encabezadoCirre.get("mescierre")+"")));//utilidad para obtener el costo.
				double valEntrada=0,valSalida=0;
				int canEntrada=0,canSalida=0;
				String temp=UtilidadInventarios.existeCierreInicialFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),encabezadoCirre.get("aniocierre")+"");
		        if(!temp.equals(ConstantesBD.codigoNuncaValido+""))//el cierre inicial es el el mismo anio.
		        {
		            for(int j=Utilidades.convertirAEntero(temp.split("/")[0]);j<12;j++)
		            {
		                valEntrada=valEntrada+UtilidadInventarios.obtenerValorEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		                valSalida=valSalida+UtilidadInventarios.obtenerValorSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		                canEntrada=canEntrada+UtilidadInventarios.obtenerCantidadEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		                canSalida=canSalida+UtilidadInventarios.obtenerCantidadSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		            }
		        }
		        else 
	            {
	                valEntrada=valEntrada+UtilidadInventarios.obtenerValorEntradaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
	                valSalida=valSalida+UtilidadInventarios.obtenerValorSalidaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
	                canEntrada=canEntrada+UtilidadInventarios.obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
	                canSalida=canSalida+UtilidadInventarios.obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
		            for(int j=1;j<12;j++)
		            {
		                valEntrada=valEntrada+UtilidadInventarios.obtenerValorEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		                valSalida=valSalida+UtilidadInventarios.obtenerValorSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		                canEntrada=canEntrada+UtilidadInventarios.obtenerCantidadEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		                canSalida=canSalida+UtilidadInventarios.obtenerCantidadSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
		            }
		        }
				ps.setDouble(9,Utilidades.convertirADouble(valEntrada+""));
				ps.setDouble(10,Utilidades.convertirADouble(valSalida+""));
				ps.setDouble(11,Utilidades.convertirADouble(canEntrada+""));
				ps.setDouble(12,Utilidades.convertirADouble(canSalida+""));
				if(UtilidadTexto.isEmpty(lote))
			        {
			        	ps.setNull(13, Types.VARCHAR);
			        	ps.setNull(14, Types.DATE);
			        }
			        else
			        {
			        	ps.setString(13, lote);
			        	if(UtilidadTexto.isEmpty(fechaVencimiento))
			        	{
			        		ps.setNull(14, Types.VARCHAR);
			        	}
			        	else
			        	{
			        		ps.setDate(14, Date.valueOf(fechaVencimiento));	
			        	}
			        }    
				ps.setInt(15,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_cie_inv"));
				
				
				if(ps.executeUpdate()<=0)
				{
					enTransaccion=false;
					i=Utilidades.convertirAEntero(movimientos.get("numRegistros")+"");
				}
				ps.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return enTransaccion;
	}

	/**
	 * 
	 * @param con
	 * @param movimientos
	 * @param encabezadoCirre
	 * @param codigoCierre
	 * @return
	 */
	private static boolean guardarDetalleMovimientos(Connection con, HashMap movimientos, HashMap encabezadoCirre, String codigoCierre)
	{
		PreparedStatementDecorator ps=null;
		boolean enTransaccion=true;
		try
		{
			for(int i=0;i<Utilidades.convertirAEntero(movimientos.get("numRegistros")+"");i++)
			{
				
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionDetalleMovInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				String lote=movimientos.get("lote_"+i)+"";
				String fechaVencimiento=movimientos.get("fechavencimiento_"+i)+"";
				
				/**
				 * INSERT INTO det_cierre_inventarios (" +
																" codigo_cierre," +
																" almacen," +
																" articulo," +
																" val_total_entradas," +
																" val_total_salidas, " +
																" cantidad_total_entradas," +
																" cantidad_total_salidas," +
																" costo_unitario_final_mes," +
																" val_total_entradas_anio," +
																" val_total_salidas_anio," +
																" cantidad_total_entradas_anio," +
																" cantidad_total_salidas_anio," +
																" lote," +
																" fecha_vencimiento)" +
																" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
				 */
				
				ps.setString(1,codigoCierre);
				ps.setInt(2,Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""));
				ps.setInt(3,Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""));
				ps.setDouble(4,Utilidades.convertirADouble(movimientos.get("valtotalentradas_"+i)+""));
				ps.setDouble(5,Utilidades.convertirADouble(movimientos.get("valtotalsalida_"+i)+""));
				ps.setDouble(6,Utilidades.convertirADouble(movimientos.get("cantotalentrada_"+i)+""));
				ps.setDouble(7,Utilidades.convertirADouble(movimientos.get("cantidadsalida_"+i)+""));
				ps.setDouble(8,Utilidades.convertirADouble(UtilidadInventarios.obtenerCostoUnitarioFinalMes(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",encabezadoCirre.get("mescierre")+"")));//utilidad para obtener el costo.
				if(Utilidades.convertirAEntero(encabezadoCirre.get("tipocierre")+"")==ConstantesBDInventarios.tipoCierreAnual)
				{
					double valEntrada=Double.parseDouble(movimientos.get("valtotalentradas_"+i)+""),valSalida=Double.parseDouble(movimientos.get("valtotalsalida_"+i)+"");
					int canEntrada=Utilidades.convertirAEntero(movimientos.get("cantotalentrada_"+i)+""),canSalida=Utilidades.convertirAEntero(movimientos.get("cantidadsalida_"+i)+"");
					String temp=UtilidadInventarios.existeCierreInicialFecha( con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),encabezadoCirre.get("aniocierre")+"");
			        if(!temp.equals(ConstantesBD.codigoNuncaValido+""))//el cierre inicial es el el mismo anio.
			        {
		                //no debo tener en cuenta el mes 12 por que el cierre a penas se generara
			            for(int j=Utilidades.convertirAEntero(temp.split("/")[0]);j<12;j++)
			            {
			                valEntrada=valEntrada+UtilidadInventarios.obtenerValorEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			                valSalida=valSalida+UtilidadInventarios.obtenerValorSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			                canEntrada=canEntrada+UtilidadInventarios.obtenerCantidadEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			                canSalida=canSalida+UtilidadInventarios.obtenerCantidadSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			            }
			        }
			        else 
		            {
		                valEntrada=valEntrada+UtilidadInventarios.obtenerValorEntradaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
		                valSalida=valSalida+UtilidadInventarios.obtenerValorSalidaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
		                canEntrada=canEntrada+UtilidadInventarios.obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
		                canSalida=canSalida+UtilidadInventarios.obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),(Utilidades.convertirAEntero(encabezadoCirre.get("aniocierre")+"")-1)+"",lote,fechaVencimiento);
		                //no debo tener en cuenta el mes 12 por que el cierre a penas se generara
			            for(int j=1;j<12;j++)
			            {
			                valEntrada=valEntrada+UtilidadInventarios.obtenerValorEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			                valSalida=valSalida+UtilidadInventarios.obtenerValorSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			                canEntrada=canEntrada+UtilidadInventarios.obtenerCantidadEntradaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			                canSalida=canSalida+UtilidadInventarios.obtenerCantidadSalidaCierreMesAnioLoteFecha(con,Utilidades.convertirAEntero(encabezadoCirre.get("institucion")+""),Utilidades.convertirAEntero(movimientos.get("almacen_"+i)+""),Utilidades.convertirAEntero(movimientos.get("articulo_"+i)+""),encabezadoCirre.get("aniocierre")+"",(j<10?"0"+j:j+""),lote,fechaVencimiento);
			            }
			        }
					ps.setDouble(9,Utilidades.convertirADouble(valEntrada+""));
					ps.setDouble(10,Utilidades.convertirADouble(valSalida+""));
					ps.setDouble(11,Utilidades.convertirADouble(canEntrada+""));
					ps.setDouble(12,Utilidades.convertirADouble(canSalida+""));					
				}
				else
				{
					ps.setDouble(9, 0.0);
					ps.setDouble(10,0.0);
					ps.setDouble(11,0.0);
					ps.setDouble(12,0.0);	
				}
				
				if(UtilidadTexto.isEmpty(lote))
			        {
			        	ps.setNull(13, Types.VARCHAR);
			        	ps.setNull(14, Types.DATE);
			        }
			        else
			        {
			        	ps.setString(13, lote);
			        	if(UtilidadTexto.isEmpty(fechaVencimiento))
			        	{
			        		ps.setNull(14, Types.DATE);
			        	}
			        	else
			        	{
			        		ps.setDate(14, Date.valueOf(fechaVencimiento));	
			        	}
			        }    
				
				ps.setInt(15, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_cie_inv"));
				
				if(ps.executeUpdate()<=0)
				{
					enTransaccion=false;
					i=Utilidades.convertirAEntero(movimientos.get("numRegistros")+"");
				}
				ps.close();
			}
		}
		catch (SQLException e)
		{
			logger.error("ERROR ", e);
			return false;
		}
		return enTransaccion;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @return
	 */
	public static int eliminarCierreInventarios(Connection con, String codigoCierre)
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		String cadena1="DELETE FROM det_cierre_inventarios where codigo_cierre = ?";
		String cadena2="DELETE FROM cierre_inventarios where codigo = ?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoCierre);
			ps.executeUpdate();
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoCierre);
			ps.executeUpdate();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			enTransaccion=false;
		}
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return 0;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return ConstantesBD.codigoNuncaValido;
		}
	}
}
