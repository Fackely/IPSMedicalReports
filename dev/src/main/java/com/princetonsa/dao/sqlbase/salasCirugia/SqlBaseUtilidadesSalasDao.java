/*
 * Febrero 11, 2008
 */
package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;


/**
 * 
 * @author Sebastián Gómez R.
 * Objeto usado para el acceso común a la fuente de datos
 * de utilidades propias del módulo de SALAS
 */
public class SqlBaseUtilidadesSalasDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesSalasDao.class);
	
	/**
	 * Método implementado para consultar un tipo de cirugía parametrizada por institucion
	 * @param con
	 * @param acronimoTipoCirugia
	 * @param institucion
	 * @return
	 */
	public static int obtenerCodigoTipoCirugia(Connection con,String acronimoTipoCirugia,int institucion)
	{
		int codigoTipoCirugia = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "select codigo from tipos_cirugia_inst " +
				"WHERE tipo_cirugia = '"+acronimoTipoCirugia+"' and institucion = "+institucion+" and es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				codigoTipoCirugia = rs.getInt("codigo");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoTipoCirugia "+e);
		}
		
		return codigoTipoCirugia;
	}
	
	
	/**
	 * Método implementado para retornar el nombre del tipo de cirugia
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerDescripcionTipoCirugia(Connection con,int consecutivo)
	{
		String nombre = "";
		try
		{
			String consulta = "SELECT tc.nombre As nombre FROM tipos_cirugia_inst tci INNER JOIN tipos_cirugia tc ON(tc.acronimo = tci.tipo_cirugia) WHERE tci.codigo = "+consecutivo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				nombre = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoCirugia: "+e);
		}
		return nombre;
	}
	
	/**
	 * Método implementado para obtener las salas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerSalas(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> salas = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT " +
				"consecutivo," +
				"codigo," +
				"institucion," +
				"tipo_sala," +
				"activo," +
				"descripcion," +
				"centro_atencion " +
				"FROM salas " +
				"WHERE " +
				"institucion = "+campos.get("codigoInstitucion")+" AND " +
				"centro_atencion = "+campos.get("codigoCentroAtencion")+" AND " +
				"activo = "+(UtilidadTexto.getBoolean(campos.get("activo").toString())?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas());
			
			if(UtilidadCadena.noEsVacio(campos.get("codigoTipoSala").toString()))
				consulta += " AND tipo_sala = "+campos.get("codigoTipoSala");
			
			consulta += " ORDER BY descripcion ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("consecutivo",rs.getInt("consecutivo"));
				elemento.put("codigo",rs.getInt("codigo"));
				elemento.put("institucion",rs.getInt("institucion"));
				elemento.put("codigoTipoSala",rs.getInt("tipo_sala"));
				elemento.put("activo",rs.getString("activo"));
				elemento.put("descripcion",rs.getString("descripcion"));
				elemento.put("codigoCentroAtencion",rs.getInt("centro_atencion"));
				
				salas.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSalas: "+e);
		}
		return salas;
	}
	
	/**
	 * Método implementado para obtener las especialidades que intervienen de una solicitud de cirugía
	 * @param con
	 * @param numeroSolicitud
	 * @param asignada
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEspecialidadesIntervienen(Connection con,String numeroSolicitud,String asignada)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT "+ 
				"especialidad AS codigo_especialidad, "+
				"getnombreespecialidad(especialidad) AS nombre_especialidad, "+
				"asignada AS asignada "+ 
				"FROM esp_intervienen_sol_cx "+ 
				"WHERE numero_solicitud = "+numeroSolicitud;
			
			if(!asignada.equals(""))
				consulta += " AND asignada = '"+(UtilidadTexto.getBoolean(asignada)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo)+"' ";
			consulta += " ORDER BY nombre_especialidad ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_especialidad"));
				elemento.put("nombre", rs.getObject("nombre_especialidad"));
				elemento.put("asignada", rs.getObject("asignada"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEspecialidadesIntervienen:"+e);
		}
		return resultados;
	}
	
	/**
	 * Método que verifica si un servicio es de via de acceso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esServicioViaAcceso(Connection con,HashMap campos)
	{
		boolean validacion = false;
		try
		{
			String consulta = "SELECT count(1) As cuenta from servicios_via_acceso WHERE servicio = "+campos.get("codigoServicio")+" AND institucion = "+campos.get("codigoInstitucion");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				validacion = (rs.getInt("cuenta")>0?true:false);
		}
		catch(SQLException e)
		{
			logger.error("Error en esServicioViaAcceso: "+e);
		}
		return validacion;
	}
	
	/**
	 * Método que verifica si un servicio es de via de acceso 
	 * @param con
	 * @param numero_solicitud
	 * @return
	 */
	public static boolean esSolicitudCirugiaPorActo(Connection con, int numero_solicitud) throws BDException
	{
		boolean validacion = false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio esSolicitudCirugiaPorActo");
			String consulta = "SELECT es_acto as esacto from materiales_qx WHERE numero_solicitud = "+numero_solicitud;
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next()){
				if(rs.getString("esacto") != null && rs.getString("esacto").toString().equals(ConstantesBD.acronimoSi+"")){
					validacion = true;
				}
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin esSolicitudCirugiaPorActo");
		return validacion;
		
		
	}
	
	
	
	/**
	 * Método implementado para obtener el codigo del tipo de servicio de un asocio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerCodigoTipoServicioAsocio(Connection con,int consecutivo) throws BDException
	{
		String tipoServicio = "";
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerCodigoTipoServicioAsocio");
			String consulta = "SELECT tipos_servicio AS tipo_servicio FROM tipos_asocio WHERE codigo = "+consecutivo;
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs = pst.executeQuery();
			if(rs.next()){
				tipoServicio = rs.getString("tipo_servicio");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoTipoServicioAsocio");
		return tipoServicio;
	}
	
	/**
	 * Método implementado para obtener la descripción de la sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerDescripcionSala(Connection con,int consecutivo)
	{
		String descripcion = "";
		try
		{
			String consulta = "SELECT descripcion FROM salas WHERE consecutivo = "+consecutivo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				descripcion = rs.getString("descripcion");
		}
		catch(SQLException e)
		{
			logger.error("");
		}
		return descripcion;
	}
	
	/**
	 * Método implementado para obtener el listado de materiales especiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerListadoMaterialesEspeciales(Connection con,String numeroSolicitud,String idSubCuenta,boolean desdeConsumo,boolean mostrarPaquetizado)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "";
			//**************************CONSULTA DE LOS MATERIALES ESPECIALES DESDE EL CONSUMO************************
			if(desdeConsumo)
			{
				consulta = "SELECT "+
					"'' AS codigo_detalle_cargo, "+
					"articulo AS codigo_articulo, "+
					"getdescripcionarticulo(articulo) AS nombre_articulo, "+ 
					"coalesce(valor_unitario,0) AS valor_unitario_cargado, "+
					"coalesce(costo_unitario,0) AS costo_unitario, "+ 
					"0 AS valor_total_cargado, " +
					"coalesce(sum(cantidad_consumo_total),0) AS cantidad_cargada, " +
					"'' AS nombre_estado_cargo "+ 
					"FROM det_fin_materiales_qx "+ 
					"WHERE "+ 
					"numero_solicitud = "+numeroSolicitud+" and incluido = '"+ConstantesBD.acronimoNo+"' " +
					"GROUP BY articulo,costo_unitario,valor_unitario";
			}
			//***************CONSULTA DE LOS MATERIALES ESPECIALES DESDE EL CARGO**************************************
			else
			{
				consulta = "select "+
					"codigo_detalle_cargo AS codigo_detalle_cargo, "+
					"articulo AS codigo_articulo, "+
					"getdescripcionarticulo(articulo) AS nombre_articulo, "+
					"coalesce(valor_unitario_cargado,0) AS valor_unitario_cargado, "+
					"0 AS costo_unitario, "+
					"coalesce(valor_total_cargado,0) AS valor_total_cargado, "+
					"coalesce(cantidad_cargada,0) AS cantidad_cargada, " +
					"getestadosolfac(estado) AS nombre_estado_cargo, " +
					"paquetizado as paquetizado "+
					"from det_cargos "+
					"where solicitud = "+numeroSolicitud+" and articulo is not null and tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+"  ";
				
				if(!idSubCuenta.equals(""))
					consulta += " AND sub_cuenta = "+Utilidades.convertirALong(idSubCuenta);
				
				if(mostrarPaquetizado)
					consulta += " and cantidad_cargada > 0 ";
				else
					consulta += " and paquetizado = '"+ConstantesBD.acronimoNo+"' ";
				
				consulta +=	" ORDER BY nombre_articulo";
			}
			
			logger.info("===>Consulta Listado Mat. Especiales: "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("consecutivo", rs.getObject("codigo_detalle_cargo"));
				elemento.put("codigoArticulo", rs.getObject("codigo_articulo"));
				elemento.put("nombreArticulo", rs.getObject("nombre_articulo"));
				elemento.put("costoUnitario", rs.getObject("costo_unitario"));
				elemento.put("valorUnitario", rs.getObject("valor_unitario_cargado"));
				elemento.put("valorTotal", rs.getObject("valor_total_cargado"));
				elemento.put("cantidad", rs.getObject("cantidad_cargada"));
				elemento.put("paquetizado", rs.getObject("paquetizado"));
				elemento.put("nombreEstadoCargo", rs.getObject("nombre_estado_cargo"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerListadoMaterialesEspeciales: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método que verifica si una solicitud tiene consumo de materiales pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeConsumoMaterialesPendiente(Connection con,String numeroSolicitud)
	{
		boolean existe = false;
		try
		{
			String consulta = "SELECT count(1) AS cuenta FROM materiales_qx WHERE numero_solicitud = "+numeroSolicitud+" AND (finalizado is null or finalizado = '"+ConstantesBD.acronimoNo+"')";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
						existe = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeConsumoMaterialesPendiente: "+e);
		}
		return existe;
	}
	
	/**
	 * Método para obtener los tipos de anestesia por institucion y centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposAnestesiaInstitucionCentroCosto(Connection con,HashMap campos)
	{
		logger.info("\n entre a obtenerTiposAnestesiaInstitucionCentroCosto -->"+campos);
		
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			int centroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto")+"");
			
			String consulta = "SELECT "+ 
				"taic.codigo AS consecutivo, "+
				"taic.tipo_anestesia AS codigo_tipo_anestesia, "+
				"ta.descripcion as nombre_tipo_anestesia "+ 
				" FROM tipos_anestesia_inst_cc taic " +
				" INNER JOIN tipos_anestesia ta ON (ta.codigo=taic.tipo_anestesia)"+ 
				" WHERE taic.institucion = "+campos.get("codigoInstitucion");
			
			if(centroCosto>0)
				consulta += " AND taic.centro_costo = "+centroCosto;
			
			if (UtilidadCadena.noEsVacio(campos.get("mostrarHqx")+""))
			{
				if(UtilidadTexto.getBoolean(campos.get("mostrarHqx")+""))
					consulta += " AND ta.mostrar_en_hqx="+ValoresPorDefecto.getValorTrueParaConsultas();
				else
					consulta += " AND ta.mostrar_en_hqx="+ValoresPorDefecto.getValorFalseParaConsultas();
			}
			
			if (UtilidadCadena.noEsVacio(campos.get("mostrarHanes")+""))
				consulta += " AND ta.mostrar_en_hanes="+UtilidadTexto.getBoolean(campos.get("mostrarHanes")+"");
			
			if (UtilidadCadena.noEsVacio(campos.get("codigoTipoAnestesia")+""))
				consulta += " AND ta.codigo="+campos.get("codigoTipoAnestesia");
			
			consulta += " AND taic.activo = '"+ConstantesBD.acronimoSi+"' ORDER BY nombre_tipo_anestesia";
			
			logger.info("\n la consulta1 --> "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap elemento = new HashMap();
				elemento.put("consecutivo", rs.getObject("consecutivo"));
				elemento.put("codigo", rs.getObject("codigo_tipo_anestesia"));
				elemento.put("nombre", rs.getObject("nombre_tipo_anestesia"));
				resultados.add(elemento);
			}
			
			//*******si no habían tipos de anestesia por un centro de costo determinado se consultan los tipos sin centro de costo*************
			if(resultados.size()==0&&centroCosto>0)
			{
				consulta = "SELECT "+ 
				"taic.codigo AS consecutivo, "+
				"taic.tipo_anestesia AS codigo_tipo_anestesia, "+
				"getnombretipoanestesia(taic.tipo_anestesia) as nombre_tipo_anestesia "+ 
				"FROM tipos_anestesia_inst_cc taic " +
				" INNER JOIN tipos_anestesia ta ON (ta.codigo=taic.tipo_anestesia) "+ 
				" WHERE taic.institucion = "+campos.get("codigoInstitucion");
				
				if (UtilidadCadena.noEsVacio(campos.get("mostrarHqx")+""))
				{
					if(UtilidadTexto.getBoolean(campos.get("mostrarHqx")+""))
							consulta += " AND ta.mostrar_en_hqx="+ValoresPorDefecto.getValorTrueParaConsultas();
					else
							consulta += " AND ta.mostrar_en_hqx="+ValoresPorDefecto.getValorFalseParaConsultas();
				}
				if (UtilidadCadena.noEsVacio(campos.get("mostrarHanes")+""))
					consulta += " AND ta.mostrar_en_hanes="+UtilidadTexto.getBoolean(campos.get("mostrarHanes")+"");
				
				if (UtilidadCadena.noEsVacio(campos.get("codigoTipoAnestesia")+""))
					consulta += " AND ta.codigo="+campos.get("codigoTipoAnestesia");
				
				consulta+=" AND taic.centro_costo IS NULL AND taic.activo = '"+ConstantesBD.acronimoSi+"' ORDER BY nombre_tipo_anestesia";
				
				logger.info("\n la consulta2 --> "+consulta);
				st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				
				while(rs.next())
				{
					HashMap elemento = new HashMap();
					elemento.put("consecutivo", rs.getObject("consecutivo"));
					elemento.put("codigo", rs.getObject("codigo_tipo_anestesia"));
					elemento.put("nombre", rs.getObject("nombre_tipo_anestesia"));
					resultados.add(elemento);
				}
			}
			//*********************************************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposAnestesiaInstitucionCentroCosto: ",e);
		}
		
		return resultados;
	}
	
	/**
	 * Método que verifica si un tipo de anestesia está definido como mostrar Hoja Qx
	 * @param con
	 * @param codigoTipoAnestesia
	 * @return
	 */
	public static boolean estaTipoAnestesiaEnMostrarHojaQx(Connection con,int codigoTipoAnestesia)
	{
		boolean definida = false;
		try
		{
			String consulta = "SELECT mostrar_en_hqx as mostrar from tipos_anestesia where codigo = "+codigoTipoAnestesia;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				definida = rs.getBoolean("mostrar");
		}
		catch(SQLException e)
		{
			logger.error("Error en estaTipoAnestesiaEnMostrarHojaQx: "+e);
		}
		return definida;
	}
	
	/**
	 * Metodo encargado de identificar si se deben de mostrar los campos
	 * en la hoja quirurgica de perfusion y hallazgos
	 * @param con
	 * @param TipoCampo
	 * @param CentroCosto
	 * @param institucion
	 * @return
	 */
	public static boolean mostrarCampoTextoHqx(Connection con,  String TipoCampo,String CentroCosto,String institucion)
	{
		String consulta="SELECT * FROM campos_texto_centro_costo WHERE institucion="+institucion+" AND tipo='"+TipoCampo+"' AND (centro_costo is null or centro_costo="+CentroCosto+")";
		
		
		try 
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				return true;
			
		} catch (SQLException e) {
			logger.info("\n problema consultando la tabla campos_texto_centro_costo "+e);
		}
		
		return false;
	}
	
	/**
	 * Método para obtener el nombre de la farmacia del consumo de materiales Qx
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerNombreFarmaciaConsumoMaterialesQx(Connection con,HashMap campos)
	{
		String nombreFarmacia = "";
		try
		{
			//************SE TOMAN LOS PARMÁMETROS******************************************************
			int numeroSolicitud = Integer.parseInt(campos.get("numeroSolicitud").toString());
			
			String consulta = "SELECT coalesce(getnomcentrocosto(farmacia),'') as farmacia from materiales_qx where numero_solicitud = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				nombreFarmacia = rs.getString("farmacia");
			
			//Si no se encuentra farmacia se consulta en los pedidos
			if(nombreFarmacia.equals(""))
			{
				consulta = "SELECT " +
					"coalesce(getnomcentrocosto(p.centro_costo_solicitado),'') as farmacia " +
					"from solicitudes_cirugia sc " +
					"inner join pedidos_peticiones_qx ppq on (ppq.peticion = sc.codigo_peticion) " +
					"inner join pedido p on (p.codigo = ppq.pedido) " +
					"WHERE sc.numero_solicitud = ?";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,numeroSolicitud);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					nombreFarmacia = rs.getString("farmacia");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreFarmaciaConsumoMaterialesQx:"+e);
		}
		return nombreFarmacia;
	}
	
	
	/**
	 * Metodo encargado de consultar el nombre del tipo de participante en la cirugia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreTipoParticipanteQx(Connection con,int codigo)
	{
		try
		{
			String consulta = "SELECT nombre FROM tipos_participantes_qx WHERE codigo = "+codigo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				return rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerNombreTipoParticipanteQx  "+e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatosInt obtenerMedicoSalaMaterialesCx(int numeroSolicitud)
	{
		InfoDatosInt info= new InfoDatosInt();
		String consulta="SELECT coalesce(s.medico, "+ConstantesBD.codigoNuncaValido+") as medico , administracion.getnombremedico(s.medico) as nombremedico from hoja_quirurgica hq inner join salas s on(hq.sala=s.sala) WHERE hq.numero_solicitud= "+numeroSolicitud;
		logger.info("\n\n obtenerMedicoSalaCx-->"+consulta+"\n\n");
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{	
				info.setCodigo(rs.getInt("medico"));
				info.setNombre(rs.getString("nombremedico"));
			}	
			
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerMedicoSalaMaterialesCx  "+e);
		}
		return info;
	}
	
	/**
	 * 
	 * @param sala
	 * @return
	 */
	public static InfoDatosInt obtenerMedicoSalaMaterialesCx(String sala) 
	{
		InfoDatosInt info= new InfoDatosInt();
		String consulta="SELECT coalesce(s.medico, "+ConstantesBD.codigoNuncaValido+") as medico , administracion.getnombremedico(s.medico) as nombremedico from salas s WHERE s.consecutivo= "+Utilidades.convertirAEntero(sala);
		logger.info("\n\n obtenerMedicoSalaMaterialesCx-->"+consulta+"\n\n");
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{	
				info.setCodigo(rs.getInt("medico"));
				info.setNombre(rs.getString("nombremedico"));
			}	
			
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerMedicoSalaMaterialesCx  "+e);
		}
		return info;
	}
	
	
	/**
	 * 
	 * @param tipoAsocio
	 * @return
	 */
	public static InfoDatosInt obtenerCentroCostoEjecutaHonorarios(int tipoAsocio)
	{
		InfoDatosInt info= new InfoDatosInt();
		String consulta="SELECT coalesce(ta.centro_costo_ejecuta, "+ConstantesBD.codigoNuncaValido+") as ccejecuta , getnomcentrocosto(ta.centro_costo_ejecuta) as nombreccejecuta from tipos_asocio ta WHERE ta.codigo= "+tipoAsocio;
		logger.info("\n\n obtenerCentroCostoEjecutaHonorarios-->"+consulta+"\n\n");
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{	
				info.setCodigo(rs.getInt("ccejecuta"));
				info.setNombre(rs.getString("nombreccejecuta"));
			}	
			
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerCentroCostoEjecutaHonorarios  "+e);
		}
		return info;
	}

	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoDatosInt obtenerCentroCostoEjecutaHonorarios(double codigoDetalleCargo)
	{
		InfoDatosInt info= new InfoDatosInt();
		String consulta="SELECT " +
							"coalesce(dch.centro_costo_ejecuta, "+ConstantesBD.codigoNuncaValido+") as ccejecuta , " +
							"getnomcentrocosto(dch.centro_costo_ejecuta) as nombreccejecuta " +
						"from " +
							"det_cx_honorarios dch " +
							"INNER JOIN det_cargos dc ON(dc.det_cx_honorarios= dch.codigo) " +
						"WHERE " +
							"dc.codigo_detalle_cargo= "+codigoDetalleCargo;
		logger.info("\n\n obtenerCentroCostoEjecutaHonorarios-->"+consulta+"\n\n");
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{	
				info.setCodigo(rs.getInt("ccejecuta"));
				info.setNombre(rs.getString("nombreccejecuta"));
			}	
			
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerCentroCostoEjecutaHonorarios  "+e);
		}
		return info;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static String obtenerNumIdMedicoSalaMaterialesCx(double codigoDetalleCargo)
	{
		String consulta="SELECT " +
							"p.numero_identificacion as numid "+
						"from " +
							"det_asocio_cx_salas_mat da " +
							"inner join det_cargos dc on(dc.det_asocio_cx_salas_mat=da.codigo) " +
							"inner join personas p on(p.codigo=da.medico) " +
						"WHERE " +
							"dc.codigo_detalle_cargo= "+codigoDetalleCargo;
		logger.info("\n\n obtenerMedicoSalaMaterialesCx-->"+consulta+"\n\n");
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{	
				return rs.getString(1);
			}	
			
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerMedicoSalaMaterialesCx  "+e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static String obtenerNumIdMedicoHonorariosCx(double codigoDetalleCargo)
	{
		String consulta="SELECT " +
							"p.numero_identificacion as numid "+
						"from " +
							"det_cx_honorarios dch " +
							"inner join det_cargos dc on(dc.det_cx_honorarios=dch.codigo) " +
							"inner join personas p on(p.codigo=dch.medico) " +
						"WHERE " +
							"dc.codigo_detalle_cargo= "+codigoDetalleCargo;
		logger.info("\n\n obtenerMedicoSalaMaterialesCx-->"+consulta+"\n\n");
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));

			if(rs.next())
			{	
				return rs.getString(1);
			}	

			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("\n problema en obtenerMedicoSalaMaterialesCx  "+e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param servicioOPCIONAL
	 * @return
	 */
	public static HashMap<Object, Object> obtenerArticulosConsumoCx(Connection con,String numeroSolicitud, int servicioOPCIONAL) throws BDException
	{
		 HashMap<Object,Object> mapa=new HashMap<Object,Object>();
		 PreparedStatement pst=null;
		 ResultSet rs=null;
		 
		 try {
			Log4JManager.info("############## Inicio obtenerArticulosConsumoCx");
			String consultaStr="SELECT " +
							"d.articulo as articulo, " +
							"d.cantidad_consumo_total as cantidad_consumo_total, " +
							"d.valor_unitario as valor_unitario, " +
							"( d.cantidad_consumo_total*d.valor_unitario) as valor_total " +
						"FROM " +
							"det_fin_materiales_qx d " +
						"where " +
							"d.numero_solicitud="+numeroSolicitud+" "+
							"and (d.incluido is null or d.incluido='"+ConstantesBD.acronimoNo+"') ";
			
			consultaStr+=(servicioOPCIONAL>0)?" and d.servicio= "+servicioOPCIONAL:" ";

			int cont=0;
			mapa.put("numRegistros","0");
			pst= con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
			 
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		 Log4JManager.info("############## Fin obtenerArticulosConsumoCx");
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esCxPorConsumoMateriales(Connection con,int numeroSolicitud) throws BDException
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio esCxPorConsumoMateriales");
			String cadena="select indi_tarifa_consumo_materiales from solicitudes_cirugia where numero_solicitud="+numeroSolicitud;
			pst=con.prepareStatement(cadena);
			rs=pst.executeQuery();
			if(rs.next()){
				resultado=UtilidadTexto.getBoolean(rs.getString(1)==null?"":rs.getString(1));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin esCxPorConsumoMateriales");
		return resultado;
	}
	
	/**
	 * Metodo permite validar si el consumo de materiales esta finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean consultarConsumoMaterialesFinalizado(Connection con, int numeroSolicitud){
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		int consumo = 0;
		boolean resultado = false;
		
		try {
			
			String cadena1 = "SELECT count(1) consumo FROM det_materiales_qx WHERE numero_solicitud = ? ";
			
			ps = new PreparedStatementDecorator(con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			rs = new ResultSetDecorator(ps.executeQuery());
			logger.info("consultarConsumoMaterialesFinalizado (consumo)-> " + cadena1 + "--" + numeroSolicitud);
			
			if(rs.next())
				 consumo = rs.getInt("consumo") ;
			
			if(consumo > 0){
				
				String cadena2 = "SELECT count(1) finalizado FROM det_fin_materiales_qx WHERE numero_solicitud = ? ";
				
				ps = new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, numeroSolicitud);
				rs = new ResultSetDecorator(ps.executeQuery());
				logger.info("consultarConsumoMaterialesFinalizado (finalizado)-> " + cadena2 + "--" + numeroSolicitud);
				
				if(rs.next())
					 resultado = rs.getInt("finalizado")>0 ? true : false ;
				
			}else
				return true;

		} catch (SQLException e) {
			logger.error("consultarConsumoMaterialesFinalizado: " + e);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return false;
	}
	
}
