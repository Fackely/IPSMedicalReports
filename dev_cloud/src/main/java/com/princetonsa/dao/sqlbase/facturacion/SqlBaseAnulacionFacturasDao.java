package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseAnulacionFacturasDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAnulacionFacturasDao.class);
	
	/**
	 * cadena para el insert de la anulación de facturas
	 */
	private static final String  anularFacturaStr=	"INSERT INTO anulaciones_facturas (	codigo, " +
																				"consecutivo_anulacion,  " +
																				"consecutivo_factura, " +
																				"motivo_anulacion, " +
																				"observaciones, " +
																				"fecha_grabacion, " +
																				"hora_grabacion, " +
																				"usuario, " +
																				"estado_cuenta_madre, " +
																				"contabilizado, " +
																				"institucion ) " +
																				"VALUES (" +
																				"?, " +
																				"?, " +
																				"(SELECT consecutivo_factura FROM facturas WHERE codigo=?), " +
																				"?," +
																				"?," +
																				"CURRENT_DATE, " +
																				""+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", " +
																				"?, " +
																				"(SELECT c.estado_cuenta FROM facturas f, cuentas c WHERE c.id=f.cuenta AND f.codigo=?), " +
																				"?, " +
																				"? " +
																				")";
	
	/**
	 * cadena para cargarb el detalle de una anulacion de factura
	 */
	private static final String detalleAnulacionfacturaStr=	"SELECT " +
																"a.codigo, "+
																"a.consecutivo_anulacion AS consecutivoAnulacion, " +
																"to_char(a.fecha_grabacion, 'DD/MM/YYYY') AS fechaAnulacion, " +
																"substr(a.hora_grabacion,1,5) AS horaAnulacion, " +
																"a.usuario AS loginUsuario, " +
																"ec.codigo AS codigoEstadoCuenta, " +
																"ec.nombre AS nombreEstadoCuenta, " +
																"a.motivo_anulacion AS codigoMotivoAnulacion, " +
																"ma.descripcion AS descripcionMotivoAnulacion, " +
																"a.observaciones AS observacionesAnulacion, " +
																"ec2.codigo AS codigoEstadoCuentaMadre, " +
																"ec2.nombre AS nombreEstadoCuentaMadre, " +
																"cue.id_ingreso as ingresopaciente " +
															"FROM " +
																"anulaciones_facturas a " +
																"INNER JOIN motivos_anul_fact ma ON (ma.codigo=a.motivo_anulacion) " +
																"inner join facturas fac on(a.codigo=fac.codigo) " +
																"left outer join  cuentas cue on(fac.cuenta=cue.id) " +
																"left outer JOIN estados_cuenta ec ON (ec.codigo=cue.estado_cuenta) " +
																"left outer JOIN estados_cuenta ec2 ON (ec2.codigo=a.estado_cuenta_madre) " +
															"WHERE " +
																"a.codigo=? " ;
	
	/**
	 * cadena para cargar el listado de la anulacion de facturas
	 */
	private static final String listadoAnulacionFacturaStr=	"SELECT " +
																	"a.codigo, "+
																	"a.consecutivo_anulacion AS consecutivoAnulacion, " +
																	"to_char(a.fecha_grabacion, 'DD/MM/YYYY') ||' '|| substr(a.hora_grabacion,1,5) AS fechaHoraAnulacion, "+ //necesario en ordenamiento
																	"a.usuario AS loginUsuario, " +
																	"a.consecutivo_factura AS consecutivoFactura, " +
																	"to_char(f.fecha, 'DD/MM/YYYY') ||' '|| substr(f.hora,1,5) AS fechaHoraElaboracion, "+ //necesario en ordenamiento
																	"p.primer_nombre ||' '|| p.segundo_nombre ||' '|| p.primer_apellido ||' '|| p.segundo_apellido AS nombrePaciente, " +
																	"coalesce(getDescempresainstitucion(f.empresa_institucion),'') as nombreempresa,"+
																	"CASE WHEN f.cod_res_particular IS NULL " +
																	"THEN "+
																	"(SELECT c.nombre FROM convenios c WHERE f.convenio=c.codigo) "+ 
																	"ELSE "+ 
																	"(SELECT t.descripcion FROM terceros t WHERE t.codigo=f.cod_res_particular) END AS nombreResponsable, " +
																	"coalesce(f.entidad_subcontratada,"+ConstantesBD.codigoNuncaValido+") as entidadsubcontratada, " +
																	"coalesce(getdescentitadsubcontratada(f.entidad_subcontratada), '') as descentidadsubcontratada, "+
																	"a.observaciones as observaciones, "+
																	"motivo.descripcion as desmotivoanulacion, "+
																	"cue.id_ingreso as ingresopaciente "+
																"FROM " +
																	"anulaciones_facturas a " +
																	"INNER JOIN facturas f ON (f.codigo= a.codigo) " +
																	"INNER JOIN personas p ON (f.cod_paciente=p.codigo) "+
																	"INNER JOIN motivos_anul_fact motivo ON (motivo.codigo=a.motivo_anulacion) "+
																	"INNER JOIN MANEJOPACIENTE.CUENTAS cue ON (cue.id=f.cuenta) " +
																"WHERE " +
																	"a.institucion=? ";
	
	
	/**
	 * indica si la factura ya fue o no anulada
	 */
	private static String estaFacturaAnuladaCodigoStr="SELECT count(1) AS numResultados FROM anulaciones_facturas WHERE codigo=?";
	
	/**
	 * Metodo para insertar la anulacion de facturas
	 * @param con
	 * @param codigoFactura
	 * @param consecutivoAnulacion
	 * @param motivoAnulacion
	 * @param observaciones
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param contabilizado
	 * @return
	 */
	public static boolean  insertarAnulacionFactura(	Connection con,
														int codigoFactura,	
														String consecutivoAnulacion,
														int motivoAnulacion, 
														String observaciones,
														String loginUsuario,
														int codigoInstitucion,
														String contabilizado
													)
	{
	    try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(anularFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("LLEGA A INSERTAR LA ANULACION----->"+anularFacturaStr+"-->codFact-->"+codigoFactura+" cons-->"+
			        consecutivoAnulacion+" codFac-->"+codigoFactura+" motivo-->"+motivoAnulacion+" obser-->"+observaciones+" login--->"+loginUsuario+
			        " ins-->"+codigoInstitucion);
			
			ps.setInt(1,codigoFactura);
			
			int index=consecutivoAnulacion.indexOf(".");
			if(index!=ConstantesBD.codigoNuncaValido)
	        	consecutivoAnulacion = consecutivoAnulacion.substring(0,index);                
	        ps.setString(2, consecutivoAnulacion);
			ps.setInt(3, codigoFactura);
			ps.setInt(4,motivoAnulacion);
			ps.setString(5,observaciones);
			ps.setString(6,loginUsuario);
			ps.setInt(7,codigoFactura);
			if(contabilizado.trim().equals(""))
			    ps.setString(8, ConstantesBD.acronimoNo);
			else
			    ps.setString(8, contabilizado);
			ps.setInt(9, codigoInstitucion);
			if(ps.executeUpdate()>0)
			    return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseValidacionesAnulacionFacturasDao " );
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Metodo que carga el Detalle de anulación factura
	 * @param con
	 * @param codigoFacturaAnulacion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator detalleAnulacionFactura(Connection con, int codigoFacturaAnulacion) 
	{
	    try
	    {
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(detalleAnulacionfacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarResumenStatement.setInt(1, codigoFacturaAnulacion);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.error("Error en detalleAnulacionFactura de SqlBaseFacturaDao: "+e);
			return null;
	    }
	}
	
	/**
	 * Metodo que carga el Listado de anulación factura
	 * @param con
	 * @param codigoFacturaAnulacion
	 * @param codigoInstitucion
	 * @param empresaInstitucion 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator listadoAnulacionFactura(	Connection con, int codigoInstitucion, 
														String consecutivoFactura, String consecutivoAnulacion,
														String fechaInicialAnulacion, String fechaFinalAnulacion,
														String loginUsuarioAnulacion,
														int codigoCentroAtencion, String empresaInstitucion) 
	{
	    try
	    {
	        String restricciones="";
	        //@todo el consecutivo_factura se tiene cono integer pero en algun momento cambiara a String,
	        //cuando suceda eso este captura del catch desaparece
	        if(!consecutivoFactura.trim().equals(""))
	        {
	            try
	            {
	                restricciones+=" AND a.consecutivo_factura='"+Utilidades.convertirALong(consecutivoFactura)+"' ";
	            }
	            catch(Exception e)
	            {
	                restricciones+=" AND a.consecutivo_factura='"+ConstantesBD.codigoNuncaValido+"' ";
	            }    
	        }    
	        if(!consecutivoAnulacion.trim().equals(""))
	            restricciones+=" AND a.consecutivo_anulacion='"+consecutivoAnulacion+"' ";
	        if(!fechaFinalAnulacion.equals(""))
	            restricciones+=" AND (to_char(a.fecha_grabacion, '"+ConstantesBD.formatoFechaBD+"') >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicialAnulacion)+"' and to_char(a.fecha_grabacion, '"+ConstantesBD.formatoFechaBD+"') <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinalAnulacion)+"') ";
	        
	        if(!loginUsuarioAnulacion.trim().equals(""))
	        	restricciones+=" AND a.usuario = '"+loginUsuarioAnulacion+"' ";
	        
	        if(codigoCentroAtencion>0)
	        	restricciones+=" and f.centro_aten = "+codigoCentroAtencion+" ";
	        
	        if(!UtilidadTexto.isEmpty(empresaInstitucion))
	        	restricciones+=" and f.empresa_institucion = "+empresaInstitucion+" ";
	        
	        restricciones+=" ORDER BY consecutivoAnulacion ";
	        logger.info("CONSULTA LISTADO===>"+listadoAnulacionFacturaStr+ restricciones);
			PreparedStatementDecorator cargarListadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoAnulacionFacturaStr+ restricciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarListadoStatement.setInt(1, codigoInstitucion);
			return new ResultSetDecorator(cargarListadoStatement.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.error("Error en listadoAnulacionFactura de SqlBaseFacturaDao: "+e);
			return null;
	    }
	}
	
	/**
	 * indica si la factura esta anulada o no
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean estaFacturaAnulada(Connection con, int codigoFactura)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(estaFacturaAnuladaCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoFactura);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				int numResultados=resultado.getInt("numResultados");
				return numResultados>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error existeCargoDadaNumSolicitud (SqlBaseCargoMedicamentosDao): "+e);
			return false;
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean actualizarEstadoDetalleCargo(Connection con, String codigoFactura) 
	{
		boolean respuesta=true;
		
		respuesta=activarCargosServicios(con,codigoFactura);
		if(respuesta)
			respuesta=activarCargosServiciosCX(con,codigoFactura);
		if(respuesta)
			respuesta=activarCargosArticulos(con,codigoFactura);
		
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	private static boolean activarCargosServicios(Connection con, String codigoFactura) 
	{
		logger.info("ACOMODACION DE CARGOS DE SERVICIOS");
		PreparedStatementDecorator ps=null;
		HashMap mapaTempo=new HashMap();

		boolean resultado=true;
		
		try
		{
			/////acomodocacion de servicios no paquetizados.
			String cadenaCargosTempo="SELECT " +
													" codigo_detalle_cargo as codigo," +
													" cod_sol_subcuenta as codsolsub," +
													" solicitud," +
													" servicio," +
													" estado," +
													" sub_cuenta as subcuenta," +
													" convenio as convenio," +
													" tipo_distribucion as tipodistribucion," +
													" cantidad_cargada as cantidadcargada," +
													" coalesce(porcentaje_cargado,0) as porcentajecargado," +
													" coalesce(valor_unitario_cargado,0) as valorunitariocargado," +
													" coalesce(valor_total_cargado,0) as valortotalcargado " +
												" from det_cargos where codigo_factura=? and servicio is not null and servicio_cx is null and paquetizado='"+ConstantesBD.acronimoNo+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaCargosTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
			mapaTempo=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Utilidades.convertirAEntero(mapaTempo.get("numRegistros")+"");i++)
			{
				String cadena="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
									"FROM det_cargos " +
									"WHERE codigo_detalle_cargo!="+mapaTempo.get("codigo_"+i)+" " +
											"and sub_cuenta=? " +
											"and solicitud=? " +
											"and servicio=? " +
											"and estado=? " +
											"and servicio_cx is null " +
											"and articulo is null " +
											"and facturado='"+ConstantesBD.acronimoNo+"' " +
											"and paquetizado='"+ConstantesBD.acronimoNo+"'";
				
				PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setLong(1, Utilidades.convertirALong(mapaTempo.get("subcuenta_"+i).toString()));
				ps1.setInt(2, Utilidades.convertirAEntero(mapaTempo.get("solicitud_"+i).toString()));
				ps1.setInt(3, Utilidades.convertirAEntero(mapaTempo.get("servicio_"+i).toString()));
				ps1.setInt(4, Utilidades.convertirAEntero(mapaTempo.get("estado_"+i).toString()));
				
				resultado=acomodacionCargos(con,mapaTempo,i,cadena,new ResultSetDecorator(ps1.executeQuery()));
			}
			
			
			if(resultado)
			{
				/////acomodocacion de servicios paquetizados.
				cadenaCargosTempo="SELECT " +
														" codigo_detalle_cargo as codigo," +
														" cod_sol_subcuenta as codsolsub," +
														" solicitud," +
														" servicio," +
														" estado," +
														" sub_cuenta as subcuenta," +
														" convenio as convenio," +
														" tipo_distribucion as tipodistribucion," +
														" cantidad_cargada as cantidadcargada," +
														" coalesce(porcentaje_cargado,0) as porcentajecargado," +
														" coalesce(valor_unitario_cargado,0) as valorunitariocargado," +
														" coalesce(valor_total_cargado,0) as valortotalcargado " +
													" from det_cargos where codigo_factura=? and servicio is not null and servicio_cx is null and paquetizado='"+ConstantesBD.acronimoSi+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaCargosTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
				mapaTempo=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int i=0;i<Utilidades.convertirAEntero(mapaTempo.get("numRegistros")+"");i++)
				{
					String cadena="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
										"FROM det_cargos " +
										"WHERE codigo_detalle_cargo!="+mapaTempo.get("codigo_"+i)+" " +
												"and sub_cuenta=? " +
												"and solicitud=? " +
												"and servicio=? " +
												"and estado=? " +
												"and servicio_cx is null " +
												"and articulo is null " +
												"and facturado='"+ConstantesBD.acronimoNo+"' " +
												"and paquetizado='"+ConstantesBD.acronimoSi+"'";
					
					PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps1.setLong(1, Utilidades.convertirALong(mapaTempo.get("subcuenta_"+i).toString()));
					ps1.setInt(2, Utilidades.convertirAEntero(mapaTempo.get("solicitud_"+i).toString()));
					ps1.setInt(3, Utilidades.convertirAEntero(mapaTempo.get("servicio_"+i).toString()));
					ps1.setInt(4, Utilidades.convertirAEntero(mapaTempo.get("estado_"+i).toString()));
					resultado=acomodacionCargos(con,mapaTempo,i,cadena,new ResultSetDecorator(ps1.executeQuery()));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mapaTempo
	 * @param i
	 * @param cadena 
	 * @throws SQLException 
	 */
	private static boolean acomodacionCargos(Connection con, HashMap mapaTempo, int i, String cadena,ResultSetDecorator rs)  
	{
		logger.info("**********************************ACOMODACION CARGOS-****************************************");
		try
		{
			//si existe actualizar simplemente.
			if(rs.next())
			{
				if((mapaTempo.get("tipodistribucion_"+i)+"").toString().trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
				{
					String actualizarCargos="UPDATE det_cargos set cantidad_cargada=cantidad_cargada+"+mapaTempo.get("cantidadcargada_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs.getObject(1);
					
					logger.info("DIST X CANT->"+actualizarCargos);
					PreparedStatementDecorator psTempo= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					psTempo.executeUpdate();
					
					//no se actualiza solicitudes_subcuenta por que esta se actualiza por el trigger.
				}
				else if((mapaTempo.get("tipodistribucion_"+i)+"").toString().trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
				{
					String actualizarCargos="UPDATE det_cargos set porcentaje_cargado=porcentaje_cargado+"+mapaTempo.get("porcentajecargado_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs.getObject(1);
					PreparedStatementDecorator psTempo= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					psTempo.executeUpdate();
					
					actualizarCargos="UPDATE solicitudes_subcuenta set porcentaje=porcentaje+"+mapaTempo.get("porcentajecargado_"+i)+" where codigo= "+rs.getObject(2);
					psTempo= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					psTempo.executeUpdate();
				} 
				else if((mapaTempo.get("tipodistribucion_"+i)+"").toString().trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
				{
					String actualizarCargos="UPDATE det_cargos set valor_total_cargado=valor_total_cargado+"+mapaTempo.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs.getObject(1);
					PreparedStatementDecorator psTempo= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					psTempo.executeUpdate();
					
					actualizarCargos="UPDATE solicitudes_subcuenta set monto=monto+"+mapaTempo.get("valortotalcargado_"+i)+" where codigo= "+rs.getObject(2);
					psTempo= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					psTempo.executeUpdate();
				} 
				logger.info("ELIMINANDO EXISTENTE");
				PreparedStatementDecorator  psTempo= new PreparedStatementDecorator(con.prepareStatement("update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE codigo= "+mapaTempo.get("codsolsub_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("-->"+("update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE codigo= "+mapaTempo.get("codsolsub_"+i)));
				psTempo.executeUpdate();
				
				psTempo =  new PreparedStatementDecorator(con.prepareStatement("UPDATE det_cargos set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,cargo_padre=null  where codigo_detalle_cargo= "+mapaTempo.get("codigo_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("-->"+("UPDATE det_cargos set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,cargo_padre=null  where codigo_detalle_cargo= "+mapaTempo.get("codigo_"+i)));
				psTempo.executeUpdate();
				
			}
			else
			{
				String actualizarCargos="UPDATE det_cargos set facturado='"+ConstantesBD.acronimoNo+"',codigo_factura=null where codigo_detalle_cargo= "+mapaTempo.get("codigo_"+i);
				logger.info("-->"+actualizarCargos);
				PreparedStatementDecorator psTempo= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psTempo.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		logger.info("**********************************FIN ACOMODACION CARGOS-****************************************");
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	private static boolean activarCargosServiciosCX(Connection con, String codigoFactura) 
	{
		
		logger.info("ACOMODACION DE CARGOS DE SERVICIOS CX");
		
		
		PreparedStatementDecorator ps=null;
		HashMap mapaTempo=new HashMap();
		boolean resultado=true;

		try
		{
			/////acomodocacion de servicios no paquetizados.
			String cadenaCargosTempo="SELECT " +
													" codigo_detalle_cargo as codigo," +
													" cod_sol_subcuenta as codsolsub," +
													" solicitud," +
													" servicio," +
													" estado," +
													" servicio_cx as serviciocx," +
													" sub_cuenta as subcuenta," +
													" convenio as convenio," +
													" tipo_distribucion as tipodistribucion," +
													" cantidad_cargada as cantidadcargada," +
													" coalesce(porcentaje_cargado,0) as porcentajecargado," +
													" coalesce(valor_unitario_cargado,0) as valorunitariocargado," +
													" coalesce(valor_total_cargado,0) as valortotalcargado," +
													" det_cx_honorarios as detcxhonorarios," +
													" det_asocio_cx_salas_mat as detasosalmat " +
												" FROM det_cargos where codigo_factura=? " +
													"and servicio is not null " +
													"and servicio_cx is not null " +
													"and paquetizado='"+ConstantesBD.acronimoNo+"'";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaCargosTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
			
			logger.info("\n NO PAQUETIZADOS--->"+cadenaCargosTempo+"-->"+codigoFactura);
			
			mapaTempo=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Utilidades.convertirAEntero(mapaTempo.get("numRegistros")+"");i++)
			{
				//examinando si hay otros cargos del mismo servicio dierentes al actual.
				String cadena="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
									"FROM det_cargos " +
									"WHERE codigo_detalle_cargo!="+mapaTempo.get("codigo_"+i)+" " +
											"and sub_cuenta=? " +
											"and solicitud=? " +
											"and servicio=? " +
											"and servicio_cx=? " +
											"and det_cx_honorarios=? " +
											"and det_asocio_cx_salas_mat=? " +
											"and estado=? " +
											"and articulo is null " +
											"and facturado='"+ConstantesBD.acronimoNo+"' " +
											"and paquetizado='"+ConstantesBD.acronimoNo+"'";
				
				logger.info("x cada registro--->"+cadena+ " ->subcu:"+mapaTempo.get("subcuenta_"+i)+" sol:"+mapaTempo.get("solicitud_"+i)+" serv:"+mapaTempo.get("servicio_"+i)+" servcx:"+mapaTempo.get("serviciocx_"+i)+" estado:"+mapaTempo.get("estado_"+i));
				
				PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setLong(1, Utilidades.convertirALong(mapaTempo.get("subcuenta_"+i).toString()));
				ps1.setInt(2, Utilidades.convertirAEntero(mapaTempo.get("solicitud_"+i).toString()));
				ps1.setInt(3, Utilidades.convertirAEntero(mapaTempo.get("servicio_"+i).toString()));
				ps1.setInt(4, Utilidades.convertirAEntero(mapaTempo.get("serviciocx_"+i).toString()));
				if(Utilidades.convertirAEntero(mapaTempo.get("detcxhonorarios_"+i).toString())>0)
					ps1.setInt(5, Utilidades.convertirAEntero(mapaTempo.get("detcxhonorarios_"+i).toString()));
				else
					ps1.setObject(5, null);
				if(Utilidades.convertirAEntero(mapaTempo.get("detasosalmat_"+i).toString())>0)
					ps1.setInt(6, Utilidades.convertirAEntero(mapaTempo.get("detasosalmat_"+i).toString()));
				else
					ps1.setObject(6, null);
				ps1.setInt(7, Utilidades.convertirAEntero(mapaTempo.get("estado_"+i).toString()));
				resultado=acomodacionCargos(con,mapaTempo,i,cadena,new ResultSetDecorator(ps1.executeQuery()));
			}
			
			
			if(resultado)
			{
				/////acomodocacion de servicios paquetizados.
				cadenaCargosTempo="SELECT " +
							" codigo_detalle_cargo as codigo," +
							" cod_sol_subcuenta as codsolsub," +
							" solicitud," +
							" servicio," +
							" estado," +
							" servicio_cx as serviciocx," +
							" sub_cuenta as subcuenta," +
							" convenio as convenio," +
							" tipo_distribucion as tipodistribucion," +
							" cantidad_cargada as cantidadcargada," +
							" coalesce(porcentaje_cargado,0) as porcentajecargado," +
							" coalesce(valor_unitario_cargado,0) as valorunitariocargado," +
							" coalesce(valor_total_cargado,0) as valortotalcargado," +
							" det_cx_honorarios as detcxhonorarios," +
							" det_asocio_cx_salas_mat as detasosalmat " +							
						" FROM det_cargos " +
						"WHERE codigo_factura=? " +
						"and servicio is not null " +
						"and servicio_cx is not null " +
						"and paquetizado='"+ConstantesBD.acronimoSi+"'";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaCargosTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
				
				logger.info("PAQUETIADOS--->"+cadenaCargosTempo+" ->"+codigoFactura);
				
				mapaTempo=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int i=0;i<Utilidades.convertirAEntero(mapaTempo.get("numRegistros")+"");i++)
				{
					String cadena="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
							"FROM det_cargos " +
							"WHERE codigo_detalle_cargo!="+mapaTempo.get("codigo_"+i)+" " +
									"and sub_cuenta=? " +
									"and solicitud=? " +
									"and servicio=? " +
									"and servicio_cx=? " +
									"and det_cx_honorarios=? " +
									"and det_asocio_cx_salas_mat=? " +
									"and estado=? " +
									"and articulo is null " +
									"and facturado='"+ConstantesBD.acronimoNo+"' " +
									"and paquetizado='"+ConstantesBD.acronimoSi+"'";
					
					PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps1.setLong(1, Utilidades.convertirALong(mapaTempo.get("subcuenta_"+i).toString()));
					ps1.setInt(2, Utilidades.convertirAEntero(mapaTempo.get("solicitud_"+i).toString()));
					ps1.setInt(3, Utilidades.convertirAEntero(mapaTempo.get("servicio_"+i).toString()));
					ps1.setInt(4, Utilidades.convertirAEntero(mapaTempo.get("serviciocx_"+i).toString()));
					if(Utilidades.convertirAEntero(mapaTempo.get("detcxhonorarios_"+i).toString())>0)
						ps1.setInt(5, Utilidades.convertirAEntero(mapaTempo.get("detcxhonorarios_"+i).toString()));
					else
						ps1.setObject(5, null);
					if(Utilidades.convertirAEntero(mapaTempo.get("detasosalmat_"+i).toString())>0)
						ps1.setInt(6, Utilidades.convertirAEntero(mapaTempo.get("detasosalmat_"+i).toString()));
					else
						ps1.setObject(6, null);
					ps1.setInt(7, Utilidades.convertirAEntero(mapaTempo.get("estado_"+i).toString()));
					resultado=acomodacionCargos(con,mapaTempo,i,cadena,new ResultSetDecorator(ps1.executeQuery()));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	private static boolean activarCargosArticulos(Connection con, String codigoFactura) 
	{
		
		logger.info("ACOMODACION DE CARGOS DE ARTICULOS");

		PreparedStatementDecorator ps=null;
		HashMap mapaTempo=new HashMap();
		boolean resultado=true;

		try
		{
			/////acomodocacion de servicios no paquetizados.
			String cadenaCargosTempo="SELECT " +
													" codigo_detalle_cargo as codigo," +
													" cod_sol_subcuenta as codsolsub," +
													" solicitud," +
													" articulo," +
													" estado," +
													" sub_cuenta as subcuenta," +
													" convenio as convenio," +
													" tipo_distribucion as tipodistribucion," +
													" cantidad_cargada as cantidadcargada," +
													" coalesce(porcentaje_cargado,0) as porcentajecargado," +
													" coalesce(valor_unitario_cargado,0) as valorunitariocargado," +
													" coalesce(valor_total_cargado,0) as valortotalcargado" +
												" FROM det_cargos " +
												"WHERE codigo_factura=? " +
												"and articulo is not null " +
												"and servicio_cx is null " +
												"and paquetizado='"+ConstantesBD.acronimoNo+"'";
			
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaCargosTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
			mapaTempo=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Utilidades.convertirAEntero(mapaTempo.get("numRegistros")+"");i++)
			{
				String cadena="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
						"FROM det_cargos " +
						"WHERE codigo_detalle_cargo!="+mapaTempo.get("codigo_"+i)+" " +
								"and sub_cuenta=? " +
								"and solicitud=? " +
								"and articulo=? " +
								"and estado=? " +
								"and servicio_cx is null " +
								"and servicio is null " +
								"and facturado='"+ConstantesBD.acronimoNo+"' " +
								"and paquetizado='"+ConstantesBD.acronimoNo+"'";
				
				PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setLong(1, Utilidades.convertirALong(mapaTempo.get("subcuenta_"+i).toString()));
				ps1.setInt(2, Utilidades.convertirAEntero(mapaTempo.get("solicitud_"+i).toString()));
				ps1.setInt(3, Utilidades.convertirAEntero(mapaTempo.get("articulo_"+i).toString()));
				ps1.setInt(4, Utilidades.convertirAEntero(mapaTempo.get("estado_"+i).toString()));
				resultado=acomodacionCargos(con,mapaTempo,i,cadena,new ResultSetDecorator(ps1.executeQuery()));
			}
			
			
			if(resultado)
			{
				/////acomodocacion de servicios paquetizados.
				cadenaCargosTempo="SELECT " +
							" codigo_detalle_cargo as codigo," +
							" cod_sol_subcuenta as codsolsub," +
							" solicitud," +
							" articulo," +
							" estado," +
							" sub_cuenta as subcuenta," +
							" convenio as convenio," +
							" tipo_distribucion as tipodistribucion," +
							" cantidad_cargada as cantidadcargada," +
							" coalesce(porcentaje_cargado,0) as porcentajecargado," +
							" coalesce(valor_unitario_cargado,0) as valorunitariocargado," +
							" coalesce(valor_total_cargado,0) as valortotalcargado " +
						"FROM det_cargos " +
						"WHERE codigo_factura=? " +
						"and articulo is not null " +
						"and servicio_cx is null " +
						"and paquetizado='"+ConstantesBD.acronimoSi+"'";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaCargosTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
				mapaTempo=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int i=0;i<Utilidades.convertirAEntero(mapaTempo.get("numRegistros")+"");i++)
				{
					String cadena="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
							"FROM det_cargos " +
							"WHERE codigo_detalle_cargo!="+mapaTempo.get("codigo_"+i)+" " +
									"and sub_cuenta=? " +
									"and solicitud=? " +
									"and articulo=? " +
									"and estado=? " +
									"and servicio_cx is null " +
									"and servicio is null " +
									"and facturado='"+ConstantesBD.acronimoNo+"' " +
									"and paquetizado='"+ConstantesBD.acronimoSi+"'";
					
					PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps1.setLong(1, Utilidades.convertirALong(mapaTempo.get("subcuenta_"+i).toString()));
					ps1.setInt(2, Utilidades.convertirAEntero(mapaTempo.get("solicitud_"+i).toString()));
					ps1.setInt(3, Utilidades.convertirAEntero(mapaTempo.get("articulo_"+i).toString()));
					ps1.setInt(4, Utilidades.convertirAEntero(mapaTempo.get("estado_"+i).toString()));
					resultado=acomodacionCargos(con,mapaTempo,i,cadena,new ResultSetDecorator(ps1.executeQuery()));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	
}
