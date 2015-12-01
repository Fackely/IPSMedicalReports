/*
 * Dic 01, 2005
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Sebastián Gómez
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad de Modificar Reversar Liquidacion Qx.
 */
public class SqlBaseModificarReversarQxDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseModificarReversarQxDao.class);
	
	/**
	 * Cadena que elimina los asocios HONORARIOS de la solicitud
	 */
	private final static String reversarLiquidacionQxDELETE_01STr = "DELETE " +
		"FROM det_cx_honorarios " +
		"WHERE cod_sol_cx_servicio IN " +
			"(SELECT codigo " +
			"FROM sol_cirugia_por_servicio " +
			"WHERE numero_solicitud=?)";
	
	/**
	 * Cadena que elimina los asocios SALAS / MATERIALES  de la solicitud
	 */
	private final static String reversarLiquidacionQxDELETE_02STr = "DELETE " +
		"FROM det_asocio_cx_salas_mat " +
		"WHERE cod_sol_cx_servicio IN " +
			"(SELECT codigo " +
			"FROM sol_cirugia_por_servicio " +
			"WHERE numero_solicitud=?)";
	
	/**
	 * Cadena que elimina los registros de solicitudes sub_xcuenta para la reversion
	 */
	private final static String reversarLiquidacionQxDELETE_03STr = "DELETE FROM solicitudes_subcuenta WHERE solicitud = ?";
	
	/**
	 * Cadena que elimina los registros de det argos de una solicitud
	 */
	private final static String reversarLiquidacionQxDELETEConsumos_05STr = "DELETE FROM facturacion.det_cargos_art_consumo WHERE det_cargo in (SELECT codigo_detalle_cargo FROM det_cargos WHERE solicitud =? )";
	
	/**
	 * Cadena que elimina los registros de det argos de una solicitud
	 */
	private final static String reversarLiquidacionQxDELETE_04STr = "DELETE FROM det_cargos WHERE solicitud = ?";
	
	/**
	 * Sección SELECT que realiza la busqueda inicial de los datos LOG
	 * en moficiacion/reversion liquidacion Qx.
	 */
	private final static String busquedaGeneralLOGSELECT_01Str = "SELECT "+ 
		"c.codigo AS codigo, "+
		"to_char(c.fecha_grabacion,'DD/MM/YYYY') || ' ' || c.hora_grabacion AS fecha_cambio, "+
		"t.codigo AS codigo_tipo_cambio, "+
		"t.nombre AS nombre_tipo_cambio, "+
		"c.usuario AS usuario, "+
		"to_char(c.fecha_cirugia,'DD/MM/YYYY') AS fecha_cirugia, "+
		"s.consecutivo_ordenes_medicas AS orden, " +
		"s.numero_solicitud as numero_solicitud, "+
		"c.motivo AS motivo "+
		"FROM cambios_liquidacion_qx c "+ 
		"INNER JOIN solicitudes s on(s.numero_solicitud=c.numero_solicitud) "+ 
		"INNER JOIN tipos_cambio t ON(t.codigo=c.tipo_cambio) "+ 
		"INNER JOIN cuentas cu ON(cu.id=s.cuenta) " + 
		"INNER JOIN centros_costo cc ON(cc.codigo=cu.area) " +
		"INNER JOIN personas p ON(cu.codigo_paciente=p.codigo) ";
	
	/**
	 * Cadena que consulta el detalle de un registro LOG
	 */
	private final static String getDetalleLogStr = "SELECT "+ 
		"dc.consecutivo AS consecutivo, "+
		"dc.descripcion_servicio AS servicio,"+
		"dc.nombre_asocio || coalesce(' (' || getnombretiposervicio(ta.tipos_servicio) || ')','') || ' ' || coalesce(getnomprofesionalasocio(dc.det_cx_honorarios),'')  AS asocio, "+
		"dc.valor_inicial As valor_inicial, "+
		"dc.valor_modificado AS valor_modificado "+ 
		"FROM det_cambios_liquidacion_qx dc " +
		"LEFT OUTER JOIN tipos_asocio ta ON(ta.codigo=dc.codigo_asocio) "+ 
		"WHERE " + 
		"dc.codigo_cambio_liquid = ?";
	/**
	 * Cadena que actualiza el pool médico de un asocio Honorario
	 */
	private final static String actualizarPoolAsocioStr = "UPDATE " +
		"det_cx_honorarios SET pool = ? WHERE codigo = ?"; 
	
	
	
	
	
	/**
	 * Método implementado para actualizar el valor de un asocio
	 * vinculado a una cirugia específica
	 * @param con
	 * @param codigo
	 * @param valor
	 * @param tipoServicioAsocio
	 * @param estado
	 * @return
	 */
	public static int actualizarValorAsocio(Connection con,int codigo,double valor,String tipoServicioAsocio,String estado)
	{
		try
		{
			String consulta = "UPDATE ";
			
			//se verifica inicio de transaccion
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			//segun el tipo de servicio se actualiza la tabla correpsondiente
			if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+""))
				//HONORARIOS
				consulta+=" det_cx_honorarios ";
			else if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioSalaCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioMaterialesCirugia+""))
				//SALAS MATERIALES
				consulta+=" det_asocio_cx_salas_mat ";
			
			consulta+= " SET valor = "+valor+" WHERE codigo = "+codigo;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			int resp = st.executeUpdate(consulta);
			
			//se verifica fin de la transaccion
			if(estado.equals(ConstantesBD.finTransaccion))
				UtilidadBD.finalizarTransaccion(con);
			
			return resp;
			
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarValorAsocio de SqlBaseModificarReversarDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para reversar la liquidacion de una
	 * orden Quirúrgica
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public static int reversarLiquidacionQx(Connection con,int numeroSolicitud,String estado)
	{
		try
		{
			int resp0 = 0;
			int resp1 = 0;
			int resp2 = 0;
			int resp3 = 0;
			int tamano = 0;
			ResultSetDecorator rs;
			
			//iniciar transaccion
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			//eliminamos los detalles de los consumos de materiales
			PreparedStatementDecorator  pst =  new PreparedStatementDecorator(con.prepareStatement(reversarLiquidacionQxDELETEConsumos_05STr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			resp3 = pst.executeUpdate();
			
			///ELIMINAR LOS REGISTROS DE CARGOS DE LA SOLICITUD
			pst =  new PreparedStatementDecorator(con.prepareStatement(reversarLiquidacionQxDELETE_04STr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			resp3 = pst.executeUpdate();
			
			
			//ELIMINAR LOS REGISTROS DE SOLICITUDES X SUB CUENTA
			pst =  new PreparedStatementDecorator(con.prepareStatement(reversarLiquidacionQxDELETE_03STr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			resp2 = pst.executeUpdate();
			
			
			//ELIMINAR HONORARIOS DE LA ORDEN
			//se revisa si habían asocios de honorarios
			String consulta = "SELECT count(1) AS cuenta " +
				"FROM det_cx_honorarios " +
				"WHERE cod_sol_cx_servicio IN " +
					"(SELECT codigo " +
					"FROM sol_cirugia_por_servicio " +
					"WHERE numero_solicitud=?)";
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				tamano = rs.getInt("cuenta");
				
				if(tamano>0)
				{
					//si habían asocios se borran
					consulta = reversarLiquidacionQxDELETE_01STr;
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,numeroSolicitud);
					resp0 = pst.executeUpdate();
				}
				else
					resp0 = 1;
			}
			
			
			//ELIMINAR SALAS/MATERIALES DE LA ORDEN
			//se revisa si habían asocios de salas materiales
			consulta = "SELECT count(1) AS cuenta " +
				"FROM det_asocio_cx_salas_mat " +
				"WHERE cod_sol_cx_servicio IN " +
					"(SELECT codigo " +
					"FROM sol_cirugia_por_servicio " +
					"WHERE numero_solicitud=?)";
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				tamano = rs.getInt("cuenta");
				
				if(tamano>0)
				{
					//si habían asocios se borran
					consulta = reversarLiquidacionQxDELETE_02STr;
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,numeroSolicitud);
					resp1 = pst.executeUpdate();
				}
				else 
					resp1 = 1;
			}
			
			
			
			
			
			//finalizar transaccion
			if(estado.equals(ConstantesBD.finTransaccion))
				UtilidadBD.finalizarTransaccion(con);
			
			//validacion del resultado
			if(resp1>0&&resp0>0&&resp2>0&&resp3>0)
				return 1;
			else
				return 0;
		}
		catch(SQLException e)
		{
			logger.error("Error en reversarLiquidacionQx de SqlBaseModificarReversarQx: "+e);
			return -1;
		}
	}
	
	/**
	 * Método que inserta el encabezado de un log
	 * de modificacion/reversion liquidacion _Qx
	 * @param con
	 * @param codigoCambio
	 * @param fechaCirugia
	 * @param tipoCambio
	 * @param usuario
	 * @param motivo
	 * @param estado
	 * @param consulta
	 * @return
	 */
	public static int insertarEncabezadoLog(Connection con,int codigoCambio,String fechaCirugia,int tipoCambio,String usuario,String motivo,String estado,String consulta)
	{
		try
		{
			int resp = 0;
			
			//iniciar transaccion
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,codigoCambio);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaCirugia));
			pst.setInt(3,tipoCambio);
			pst.setString(4,usuario);
			//se revisa el motivo
			if(motivo.equals(""))
				pst.setNull(5,Types.VARCHAR);
			else
				pst.setString(5,motivo);
			
			resp = pst.executeUpdate();
			
			//finalizar transaccion
			if(estado.equals(ConstantesBD.finTransaccion))
				UtilidadBD.finalizarTransaccion(con);
			
			if(resp>0)
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_cambios_liquid_qx");
			else
				return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarEncabezadoLog de SqlBaseModificarReversarDao: "+e);
			return -1;
		}
		
	}
	
	
	/**
	 * Método implementado para insertar el detalle de un log
	 * de modificacion/reversion liquidacion Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroServicio
	 * @param codigoServicio
	 * @param nombreServicio
	 * @param codigoServicioAsocio
	 * @param nombreServicioAsocio
	 * @param codigoAsocio
	 * @param nombreAsocio
	 * @param valorInicial
	 * @param valorModificado
	 * @param consulta
	 * @param estado
	 * @return
	 */
	public static int insertarDetalleLog(
			Connection con,int numeroSolicitud,int numeroServicio,int codigoServicio,String nombreServicio,
			int codigoServicioAsocio,String nombreServicioAsocio,int codigoAsocio, String nombreAsocio,double valorInicial,
			double valorModificado,String consecutivoAsocio,String tipoServicioAsocio,String consulta,
			String estado)
	{
		try
		{
			int resp =0;
			
			//iniciar transaccion
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,numeroServicio);
			pst.setInt(3,codigoServicio);
			pst.setString(4,nombreServicio);
			pst.setInt(5,codigoServicioAsocio);
			pst.setString(6,nombreServicioAsocio);
			pst.setInt(7,codigoAsocio);
			pst.setString(8,nombreAsocio);
			pst.setDouble(9,valorInicial);
			//se revisa el valor modificado
			if(valorModificado<0)
				pst.setNull(10,Types.FLOAT);
			else
				pst.setDouble(10,valorModificado);
			logger.info("codigoServicio=> "+codigoServicio);
			logger.info("nombreAsocio=> "+nombreAsocio);
			logger.info("consecutivoAsocio=> "+consecutivoAsocio);
			logger.info("tipoServicioAsocio=> "+tipoServicioAsocio);
			if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+""))
			{
				pst.setInt(11,Integer.parseInt(consecutivoAsocio));
				pst.setNull(12,Types.INTEGER);
			}
			else
			{
				pst.setNull(11,Types.INTEGER);
				pst.setInt(12,Integer.parseInt(consecutivoAsocio));
				
			}
			
			resp = pst.executeUpdate();
			
			//finalizar transaccion
			if(estado.equals(ConstantesBD.finTransaccion))
				UtilidadBD.finalizarTransaccion(con);
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertardETALLELogBD de SqlBaseModificarReversarQxDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método que realiza la busqueda de los LOG modificacion/reversion Qx.
	 * de la BD
	 * @param con
	 * @param tipoCambio
	 * @param usuario
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param ordenInicial
	 * @param ordenFinal
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap busquedaGeneralLOG(Connection con,int tipoCambio,String usuario,String fechaInicial,String fechaFinal,int ordenInicial,int ordenFinal,String tipoIdentificacion,String numeroIdentificacion,int centroAtencion)
	{
		//columnas
		String[] columnas={
				"codigo",
				"fecha_cambio",
				"codigo_tipo_cambio",
				"nombre_tipo_cambio",
				"usuario",
				"fecha_cirugia",
				"orden",
				"numero_solicitud",
				"motivo"
				};
		try
		{
			String consulta = busquedaGeneralLOGSELECT_01Str;
			
			consulta += " WHERE cc.centro_atencion = "+centroAtencion;
			
			//TIPO CAMBIO
			if(tipoCambio==ConstantesBD.codigoTipoCambioModificacion||
					tipoCambio==ConstantesBD.codigoTipoCambioReversion)
			{
				consulta += " AND t.codigo = "+tipoCambio+" ";
			}
			
			//LOGIN USUARIO
			if(!usuario.equals(""))
				consulta += " AND c.usuario = '"+usuario+"' ";
			
			//RANGOS FECHA CAMBIO
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				consulta += " AND c.fecha_grabacion BETWEEN '"+
					UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+
					UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
			}
			
			//RANGOS CONSECUTIVOS ORDEN
			if(ordenInicial>0&&ordenFinal>0)
			{
				consulta += " AND s.consecutivo_ordenes_medicas BETWEEN "+
					ordenInicial+" AND "+ordenFinal+" ";
			}
			
			//DATOS DEL PACIENTE
			if(!tipoIdentificacion.equals("")&&!numeroIdentificacion.equals(""))
			{
				consulta += " AND p.tipo_identificacion = '"+tipoIdentificacion+
					"' AND p.numero_identificacion = '"+numeroIdentificacion+"' ";
			}
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaGeneralLOG de SqlBaseModificarReversarQxDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método que consulta el detalle del LOG Modificar/Reversar Qx.
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public static HashMap getDetalleLog(Connection con,int codigoRegistro)
	{
		//columnas
		String[] columnas={
				"consecutivo",
				"servicio",
				"asocio",
				"valor_inicial",
				"valor_modificado"
				};
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getDetalleLogStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoRegistro);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en getDetalleLog de SqlBaseMofificarReversarQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para actualizar el pool de un asocio
	 * @param con
	 * @param codigo
	 * @param pool
	 * @return
	 */
	public static int actualizarPoolAsocio(Connection con,int codigo,int pool)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarPoolAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,pool);
			pst.setInt(2,codigo);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPoolAsocio de SqlBaseNodificarReversarQxDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para cargar los convenios de un asocio y su correspondiente valor del cargo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarConveniosAsocio(Connection con,HashMap campos)
	{
		
		try
		{
			//**************SE TOMAN PARÁMETROS*********************************
			int numeroSolicitud = Utilidades.convertirAEntero(campos.get("numeroSolicitud").toString());
			int servicioCx = Utilidades.convertirAEntero(campos.get("servicioCx").toString());
			int consecutivoAsocio = Utilidades.convertirAEntero(campos.get("consecutivoAsocio").toString());
			String tipoServicio = campos.get("tipoServicio").toString();
			//*******************************************************************
			
			String convenios = "";
			/**
			 * Nota * Se saca el listado de convenios que aplican para ese asocio,
			 * con tal que no hayan repetidos
			 */
			String consulta = "SELECT DISTINCT convenio FROM det_cargos " +
				"WHERE "+ 
				"solicitud = "+numeroSolicitud+" AND " +
				"servicio_cx = "+servicioCx+" AND " +
				(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")?"det_cx_honorarios = "+consecutivoAsocio:"det_asocio_cx_salas_mat = "+consecutivoAsocio)+" AND " +
				//"paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
				"cantidad_cargada > 0 AND "+
				"eliminado = '"+ConstantesBD.acronimoNo+"' ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
				convenios += rs.getInt("convenio")+",";
			
			consulta = "SELECT DISTINCT "+ 
				"getnombreconvenio(convenio) AS nombre_convenio, "+
				"paquetizado AS paquetizado, "+
				"coalesce(valor_total_cargado,0) AS valor_total "+ 
				"FROM det_cargos "+ 
				"WHERE "+ 
				"solicitud = "+numeroSolicitud+" AND " +
				"servicio_cx = "+servicioCx+" AND " +
				(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")?"det_cx_honorarios = "+consecutivoAsocio:"det_asocio_cx_salas_mat = "+consecutivoAsocio)+" AND " +
				//"paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
				"cantidad_cargada > 0 AND "+
				"eliminado = '"+ConstantesBD.acronimoNo+"' AND " +
				"convenio in ("+convenios+ConstantesBD.codigoNuncaValido+") "+
				"ORDER BY nombre_convenio";
			
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
			st.close();
			return mapaRetorno;
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarConveniosAsocio: "+e);
			return null;
		}
	}
	
	/**
	 * Método que realiza la actualizacion del cargo de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarValorAsocioEnCargos(Connection con,HashMap campos)
	{
		try
		{
			String tipoServicioAsocio = campos.get("tipoServicioAsocio").toString();
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			
			String consulta = "UPDATE det_cargos " +
				"SET " +
				"valor_unitario_tarifa = ?, " +
				"valor_unitario_cargado = ?, " +
				"valor_total_cargado = ? " +
				"WHERE " +
				"solicitud = ? AND " +
				"servicio_cx = ? AND " ;
			
			if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+""))
				consulta += " det_cx_honorarios = "+consecutivoAsocio;
			else
				consulta += " det_asocio_cx_salas_mat = "+consecutivoAsocio;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("valorAsocio"));
			pst.setObject(2,campos.get("valorAsocio"));
			pst.setObject(3,campos.get("valorAsocio"));
			pst.setObject(4,campos.get("numeroSolicitud"));
			pst.setObject(5,campos.get("servicioCx"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarValorAsocioEnCargos: "+e);
			return 0;
		}
	}
	
	/**
	 * Método para actualizar el profesional y/o la especialidad del asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarProfesionalEspecialidadAsocio(Connection con,HashMap campos)
	{
		int resp = ConstantesBD.codigoNuncaValido;
		try
		{
			//*****************	SE TOMAN LOS CAMPOS**********************************++
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			int codigoProfesional = Utilidades.convertirAEntero(campos.get("codigoProfesional").toString());
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
			//**************************************************************************
			
			String consulta = "UPDATE det_cx_honorarios SET ";
			String seccionSET = "";
			
			if(codigoProfesional>0)
				seccionSET += " medico = "+codigoProfesional;
			if(codigoEspecialidad>0)
				seccionSET +=  (seccionSET.length()>0?",":"") + " especialidad_medico = "+codigoEspecialidad;
			
			if(seccionSET.length()>0)
			{
				consulta += seccionSET + " WHERE codigo = "+consecutivoAsocio;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resp = st.executeUpdate(consulta);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actulaizarProfesionalEspecialidadAsocios:"+e);
		}
		return resp;
	}
	
}
