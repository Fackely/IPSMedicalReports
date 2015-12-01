
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;




/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *
 * Clase para las transacciones de la Consulta de Facturas
 */
public class SqlBaseConsultaFacturasDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaFacturasDao.class);
	
	/**
	 * Statement para consultar las facturas de un paciente cargado en session
	 */
	private final static String consultarFacturasPacienteStr=" SELECT f.codigo as factura, "+
														     " f.consecutivo_factura as consecutivo, "+
															 " to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"')||'-'||substr(f.hora,1,5) as fechaHoraElaboracion, "+
															 " getnombreviaingresotipopac(f.cuenta) as viaIngreso, "+
															 " i.consecutivo as codigoIngreso, "+
															 " f.valor_total as valorTotal, "+
															 " CASE WHEN f.cod_res_particular IS NULL THEN con.nombre ELSE getnomdeudoringreso(c.id_ingreso) END as responsable, "+
															 " ef.nombre as estadoFactura, "+
															 " ep.nombre as estadoPaciente," +
															 " getnomcentroatencion(f.centro_aten) as nombreCentroAtencion, " +
															 " coalesce(f.entidad_subcontratada, "+ConstantesBD.codigoNuncaValido+") as entidadsubcontratada, " +
															 " coalesce(getdescentitadsubcontratada(f.entidad_subcontratada), '') as descentidadsubcontratada, " +
															 " coalesce(f.empresa_institucion, "+ConstantesBD.codigoNuncaValido+") as empresainstitucion, " +
															 " coalesce(getdescempresainstitucion(f.empresa_institucion), '') as descempresainstitucion "+
															 " FROM facturas f "+
															 " INNER JOIN vias_ingreso vi ON(f.via_ingreso=vi.codigo) "+
															 " INNER JOIN estados_factura_f ef ON(f.estado_facturacion=ef.codigo) "+
															 " INNER JOIN estados_factura_paciente ep ON(f.estado_paciente=ep.codigo) "+
															 " INNER JOIN cuentas c ON(f.cuenta=c.id) "+
															 " INNER JOIN convenios con ON(f.convenio=con.codigo) "+
															 " INNER JOIN ingresos i ON (i.id=c.id_ingreso) "+
															 " WHERE c.codigo_paciente=? " +
															 " ORDER BY f.consecutivo_factura desc ";
	
	/**
	 * Statement para consultar las facturas despues de una busqueda (TODOS)
	 */
	private final static String consultaFacturasTodosStr=" SELECT f.codigo as factura, " +
														 " f.valor_total as valorTotal, "+
														 " f.consecutivo_factura as consecutivo, "+
														 " to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"')||'-'||substr(f.hora,1,5) as fechaHoraElaboracion, "+
														 " f.usuario as usuario, "+
														 " f.fecha as fechaElaboracion, "+
														 " getnombreviaingresotipopac(f.cuenta) as viaIngreso, "+
														 " i.consecutivo as codigoIngreso, "+
														 " (p.primer_apellido||' '||p.segundo_apellido||' '||p.primer_nombre||' '||p.segundo_nombre) as paciente, "+
														 " p.numero_identificacion as numIdentificacion, "+
														 " p.tipo_identificacion as tipoIdentificacion, "+
														 " CASE WHEN f.cod_res_particular IS NULL THEN con.nombre ELSE getnomdeudoringreso(c.id_ingreso) END as responsable,  "+
														 " vi.codigo as codigoViaIngreso, "+
														 " con.codigo as codigoConveio, "+
														 " ef.codigo as codgioEstadoFactura, "+
														 " ep.codigo as codigoEstadoPaciente, "+
														 " ef.nombre as estadoFactura, "+
														 " ep.nombre as estadoPaciente, "+
														 " f.estado_facturacion as codigoEstadoFactura, "+
														 " f.valor_convenio as valorConvenio, "+
														 " f.valor_bruto_pac as valorPaciente, "+
														 " f.cod_paciente as codPaciente, " +
														 " getnomcentroatencion(f.centro_aten) as nombreCentroAtencion, "+
														 " coalesce(f.entidad_subcontratada, "+ConstantesBD.codigoNuncaValido+") as entidadsubcontratada, " +
														 " coalesce(getdescentitadsubcontratada(f.entidad_subcontratada), '') as descentidadsubcontratada, " +
														 " coalesce(f.empresa_institucion, "+ConstantesBD.codigoNuncaValido+") as empresainstitucion, " +
														 " coalesce(getdescempresainstitucion(f.empresa_institucion), '') as descempresainstitucion "+
														 " FROM facturas f "+
														 " LEFT OUTER JOIN vias_ingreso vi ON(f.via_ingreso=vi.codigo) "+
														 " LEFT OUTER JOIN personas p ON(f.cod_paciente=p.codigo) "+
														 " LEFT OUTER JOIN estados_factura_f ef ON(f.estado_facturacion=ef.codigo) "+
														 " LEFT OUTER JOIN estados_factura_paciente ep ON(f.estado_paciente=ep.codigo) "+
														 " LEFT OUTER JOIN cuentas c ON(f.cuenta=c.id) "+
														 " LEFT OUTER JOIN convenios con ON(f.convenio=con.codigo) "+
														 " LEFT OUTER JOIN ingresos i ON (i.id=c.id_ingreso) "+
														 " WHERE 1=1 ";
	
	/**
	 * Statement para consulta todo el detalle de la Factura segun el consecutivo de la factura
	 */
	private final static String consultarDetalleFacturaStr=" SELECT f.consecutivo_factura as consecutivo, "+
														   " to_char(f.fecha, '"+ConstantesBD.formatoFechaAp+"')||'-'||substr(f.hora, 1,5) as fechaHoraElaboracion, "+
														   "f.codigo as codfactura, " +
														   " ef.nombre as estadoFactura, "+
														   " CASE WHEN f.cod_res_particular IS NULL THEN con.nombre ELSE getnomdeudoringreso(c.id_ingreso) END as responsable,  "+
														   " getnombreviaingresotipopac(f.cuenta) as viaIngreso, "+
														   //MT6082 se agrega referencia prefijoFactura
														   " f.pref_factura as prefijoFactura, "+
														   //Fin MT
														   " f.valor_total as totalFactura, "+
														   " f.valor_convenio as totalResponsable, "+
														   " f.valor_bruto_pac as totalBrutoPaciente, "+
														   " f.val_desc_pac as totalDescuentos, "+
														   " f.valor_abonos as totalAbonos, "+
														   " f.valor_neto_paciente as totalNetoPaciente, "+
														   " coalesce(f.valor_favor_convenio,0) as valorfavorconvenio," +
														   " ep.nombre as estadoPaciente, "+
														   " tm.nombre as tipoMonto, "+
														   " f.numero_cuenta_cobro as cuentaCobro, "+
														   " getnomcentroatencion(f.centro_aten) as nombreCentroAtencion," +
														   " f.sub_cuenta AS subcuenta, " +
														   " f.cuenta AS cuenta, " +
														   " (" +
														   		"SELECT " +
														   			"consecutivo_recibo " +
														   		"FROM " +
														   			"detalle_conceptos_rc drc " +
														   		"INNER JOIN " +
														   			"recibos_caja rc on (drc.numero_recibo_caja = rc.numero_recibo_caja) " +
														   		
														   		/**
														   		 * MT 3184
														   		 * Diana Ruiz
														   		 */
														   		"INNER JOIN " +
														   			"conceptos_ing_tesoreria cit on (drc.concepto = cit.codigo) " +
														   		"INNER JOIN " +
														   			"tipo_ing_tesoreria tit on (cit.codigo_tipo_ingreso = tit.codigo) " +
														   			
														   		"WHERE " +
														   			"drc.doc_soporte=(f.consecutivo_factura||'') " +
														   			"and drc.institucion=f.institucion " +
														   			
														   			//cambio diana R
														   			"and tit.codigo= " +1+" "+
														   			
														   			
														   			"and rc.estado <> " +ConstantesBD.codigoEstadoReciboCajaAnulado+" "+
														   			ValoresPorDefecto.getValorLimit1()+" 1" +
														   	") AS reciboCaja," +
														   	" coalesce (dmg.valor,dmg.porcentaje) as montoCobro "+
														   //el anterior select tocaba filtrarlo por codigo de concepto pero eso es dinamico y en este punto no se sabe a que codigo pertenece
														   " FROM facturas  f "+
														   " INNER JOIN estados_factura_f ef ON(f.estado_facturacion=ef.codigo) "+
														   " INNER JOIN estados_factura_paciente ep ON(f.estado_paciente=ep.codigo) " +
														   " LEFT OUTER JOIN vias_ingreso vi ON(f.via_ingreso=vi.codigo) "+
														   " LEFT OUTER JOIN convenios con ON(f.convenio=con.codigo) "+
														   " LEFT OUTER JOIN cuentas c ON(f.cuenta=c.id) "+
														   " LEFT OUTER JOIN sub_cuentas sbc ON (f.sub_cuenta=sbc.sub_cuenta) " +
														   " LEFT OUTER JOIN histo_detalle_monto hdm ON(f.monto_cobro=hdm.codigo_pk) "+
														   " LEFT OUTER JOIN tipos_monto tm ON(hdm.tipo_monto_codigo=tm.codigo) " +
														   " left outer join detalle_monto demo ON(demo.detalle_codigo=hdm.detalle_codigo) " +
														   " left outer join detalle_monto_general dmg ON (dmg.detalle_codigo=demo.detalle_codigo) "+
														   " WHERE f.codigo=?";
	
	
	/**Statemwent que ocnsulta todas las solicitudes asociadas a una factura dado su consecutivo**/
	private final static String consultaSolicitudesFacturaStr = "SELECT " +
																	"dfs.codigo AS codigodetallefactura, " +
																	"sol.numero_solicitud AS numero_solicitud, " +
																	"cc.nombre AS area, " +
																	"CASE WHEN dfs.articulo IS NOT NULL THEN dfs.articulo ELSE dfs.servicio END AS codigo, " +
																	
																	//Modificado por la Tarea 52557
																	"CASE WHEN dfs.articulo IS NOT NULL THEN " + 
																		"a.codigo_interfaz " +
																	"ELSE " + 
																		"(CASE WHEN con.tipo_codigo IS NOT NULL THEN " + 
																			"getcodigopropservicio2(dfs.servicio, con.tipo_codigo) " +
																		"ELSE " +
																			"getcodigopropservicio2(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
																		"END) " +
																	"END AS codigopropietario, " +
																	
																	//Modificado por la Tarea 52557
																	"CASE WHEN dfs.articulo IS NOT NULL THEN " + 
																		"a.descripcion " + 
																	"ELSE " + 
																		"(CASE WHEN con.tipo_codigo IS NOT NULL THEN " +
																			"getnombreserviciotarifa2(dfs.servicio, con.tipo_codigo) " +
																		"ELSE " +
																			"getnombreserviciotarifa2(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
																		"END) " +
																	"END AS descripcionservicio, " +
																
																	"dfs.cantidad_cargo AS cantidad, " +
																	"dfs.valor_cargo AS valunitario, " +
																	"dfs.valor_total AS valtotal, " +
																	"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
																	"ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END AS es_qx, " +
																	"CASE WHEN sol.tipo= "+ConstantesBD.codigoTipoSolicitudCirugia+" AND dfs.articulo IS NOT NULL THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' end as es_material_especial "+
																"FROM " +
																	"facturas f " +
																	"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) " +
																	"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) " +
																	"INNER JOIN centros_costo cc ON(sol.centro_costo_solicitado=cc.codigo) " +
																	"INNER JOIN convenios con ON (f.convenio = con.codigo) " + 
																	"LEFT OUTER JOIN articulo a ON(dfs.articulo=a.codigo and f.institucion=a.institucion) " +
																	"LEFT OUTER JOIN referencias_servicio rs ON(dfs.servicio=rs.servicio and rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") " +
																"WHERE " +
																	"f.codigo=? AND dfs.valor_total>0 ORDER BY sol.numero_solicitud, dfs.articulo";
	
	/**
	 * Cadena que consulta datos necesarios de la factura para impresion 
	 */
	/**
	* Tipo Modificacion: Segun incidencia 6078
	* Autor: Alejandro Aguirre Luna
	* usuario: aleagulu
	* Fecha: 19/02/2013
	* Descripcion: Se adiciona la columna estadofac para ser retornada al 
	* 			   metodo generarReporteAnexoIT de ConsultaFacturasAction.java
	*              y posteriormente visualizar en EstadoCuenta.java el estado
	*              de la factura; Anulada o Facturada. Ya no es necesario retornar
	*              la columna estadofac para ser retornada al 
	* 			   metodo generarReporteAnexoIT, debido a que ya no se utiliza
	* 			   la sobrecarga de dicho método. 
	**/
	private static String consultaDatosFactura="SELECT tp.nombre AS tipomonto, " +
														"ca.descripcion AS centroatencion, " +
														"i.estado AS estadoingreso, " +
														"es.razon_social AS entidadsub, " +
														"ec.nombre AS estadocuenta, " +
														"c.codigo AS codigoconvenio, " +
														"c.nombre AS nombreconvenio, " +
														"np.nombre AS natupaciente, " +
														"est.descripcion AS clasecon, " +
														"f.sub_cuenta AS subcuenta, " +
														"f.cuenta AS cuenta " +
												        "FROM facturas f " +
												        "" +
														"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
														"LEFT OUTER JOIN detalle_monto dm ON (dm.detalle_codigo=f.monto_cobro) "+
														"LEFT OUTER JOIN centro_atencion ca ON (f.centro_aten=ca.consecutivo) " +
														"LEFT OUTER JOIN cuentas cu ON (f.cuenta=cu.id) " +
														"LEFT OUTER JOIN ingresos i ON (cu.id_ingreso=i.id) " +
														"LEFT OUTER JOIN entidades_subcontratadas es ON (f.entidad_subcontratada=es.codigo_pk) " +
														"LEFT OUTER JOIN estados_cuenta ec ON (cu.estado_cuenta=ec.codigo) " +
														"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
														"LEFT OUTER JOIN sub_cuentas sbc ON (f.sub_cuenta=sbc.sub_cuenta) " +
														"LEFT OUTER JOIN naturaleza_pacientes np ON (sbc.naturaleza_paciente=np.codigo) " +
														"LEFT OUTER JOIN estratos_sociales est ON (sbc.clasificacion_socioeconomica=est.codigo) " +
														"" +
												        "WHERE f.codigo=? ";

	
	/**
	 * Método que carga las facturas de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static ResultSetDecorator cargarFacturasPaciente(Connection con, int codigoPaciente)  throws SQLException
	{
		try
		{
			logger.info("consultarFacturasPacienteStr-->"+consultarFacturasPacienteStr+" ->"+codigoPaciente);
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarFacturasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPaciente);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de las facturas del paciente : SqlBaseConsultaFacturasDao"+e.toString());
			return null;
		}
	}
	
	
	
	
	/**
	 * Método que carga el detalle de una factura dado su consecutivo
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarDetalleFactura(Connection con, double codigoFactura)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n consultarDetalleFacturaStr-->"+ consultarDetalleFacturaStr+" ->"+codigoFactura);
			cargarStatement.setLong(1, (long)codigoFactura);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle de una factura : SqlBaseConsultaFacturasDao"+e.toString());
			return null;
		}
	}
	
	
	
	
	/**
	 * Método para la busuqeda de facturas (TODOS)
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap busquedaFacturasTodos (Connection con,int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		HashMap map= null;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
		
			String[] colums={"factura","consecutivo","valorTotal","fechaHoraElaboracion","usuario","viaIngreso","codigoIngreso","paciente","numIdentificacion","tipoIdentificacion","responsable","estadoFactura","estadoPaciente","codigoEstadoFactura","valorConvenio","valorPaciente","codPaciente","nombreCentroAtencion","entidadsubcontratada","descentidadsubcontratada","empresainstitucion","descempresainstitucion"};
			String consulta= consultaFacturasTodosStr + armarWhereFiltroFacturas(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion)+" ORDER BY f.consecutivo_factura desc ";
			logger.info("BUSQUEDA FACTURAS -->"+consulta);
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(ps.executeQuery());
			map = UtilidadBD.resultSet2HashMap(colums, rs, false, true).getMapa();
			
			return map;
		}
		catch(SQLException e)
		{
			logger.error(e+"Error en Busqueda Facturas Todos : SqlBaseConsultaFacturasDao "+e.toString() );
			return null;
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
				if(rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				Log4JManager.info("ERROR CERRANDO PS - RS", e);
			}
		}
	}
	
	/**
	 * 
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static double cantidadRegistrosBusqueda(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		double retorna=0;
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = null;
		
			String consulta= "SELECT COUNT(1) FROM facturas f "+
							 " LEFT OUTER JOIN vias_ingreso  vi ON(f.via_ingreso=vi.codigo) "+
							 " LEFT OUTER JOIN personas p ON(f.cod_paciente=p.codigo) "+
							 " LEFT OUTER JOIN estados_factura_f ef ON(f.estado_facturacion=ef.codigo) "+
							 " LEFT OUTER JOIN estados_factura_paciente ep ON(f.estado_paciente=ep.codigo) "+
							 " LEFT OUTER JOIN cuentas c ON(f.cuenta=c.id) "+
							 " LEFT OUTER JOIN convenios con ON(f.convenio=con.codigo) "+
							 " WHERE 1=1 " + armarWhereFiltroFacturas(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
			logger.info("CANTIDAD BUSQUEDA FACTURAS -->"+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				retorna= rs.getDouble(1);
			rs.close();
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en Busqueda Facturas Todos : SqlBaseConsultaFacturasDao "+e.toString() );
			retorna=0;
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static String armarWhereFiltroFacturas(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		String avanzadaStr = "";
		
		if(facturaInicial>0 && facturaFinal>0)
		{
			avanzadaStr+=" AND f.consecutivo_factura between "+facturaInicial +" and "+facturaFinal;
		}
		if(!fechaElaboracionIncial.equals("") && !fechaElaboracionFinal.equals(""))
		{
			
			avanzadaStr+=" AND f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaElaboracionIncial)+"' and  '"+UtilidadFecha.conversionFormatoFechaABD(fechaElaboracionFinal)+"' ";
		}
		if(responsable!=-1)
		{	
			avanzadaStr+=" AND con.codigo="+responsable+" ";					
		}
		//para el senior que hizo esto la via de ingreso 5 es todas
		if(viaIngreso!=-1 && viaIngreso!=5)
		{
			avanzadaStr+=" AND c.via_ingreso = "+viaIngreso+" ";	
		}
		if(!tipoPaciente.equals("-1") && !tipoPaciente.equals("-2"))
		{
			avanzadaStr+=" AND c.tipo_paciente = '"+tipoPaciente+"' ";	
		}
		if(estadoFactura!=-1)
		{
			avanzadaStr+=" AND ef.codigo= "+estadoFactura+" ";
		}
		if(estadoPaciente!=-1)
		{
			avanzadaStr+=" AND ep.codigo="+estadoPaciente+" ";
		}
		if(!usuario.equals("-1"))
		{
			avanzadaStr+=" AND UPPER(f.usuario) LIKE UPPER ('%"+usuario.trim()+"%') ";
		}
		if(codigoCentroAtencion!=-1)
		{
			avanzadaStr+=" AND f.centro_aten = "+codigoCentroAtencion+" ";
		}
		if(entidadSubcontratada>0)
		{
			avanzadaStr+=" AND f.entidad_subcontratada = "+entidadSubcontratada+" ";
		}
		if(empresaInstitucion>0)
		{
			avanzadaStr+=" AND f.empresa_institucion= "+empresaInstitucion+" ";
		}
		
		return avanzadaStr;
	}
	
	
	
	/**
	 * Mètodo que carga las solicitudes de una factura dado su consecutivo
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarSolicitudesFactura(Connection con, double codigoFactura, String tipoArticulo, int tipoServicio)  throws SQLException
	{
		try
		{
			/**
			 * Control Cambio Anexo de la Cuenta (3463) 
			 * desarrollador : leoquico
			 * fecha : 2-Mayo-2013
			 */
        
			String consulta = "SELECT "
					+ "dfs.codigo AS codigodetallefactura, "
					+ "sol.numero_solicitud AS numero_solicitud, "
					+ "cc.nombre AS area, "
					+ "CASE WHEN ";

			if (tipoArticulo.isEmpty()) {

				consulta += "dfs.articulo IS NOT NULL THEN CAST(dfs.articulo AS VARCHAR2(200))";
			}
			else {
				if (tipoArticulo.equals("") || tipoArticulo.equals("AXM")) {

					consulta += "dfs.articulo IS NOT NULL THEN CAST(dfs.articulo AS VARCHAR2(200)) ";
				}
				if (tipoArticulo.equals("CUM")) {

					consulta += "dfs.articulo IS NOT NULL THEN FACTURACION.getcodigocumarticulo(dfs.articulo) ";
				}
				if (tipoArticulo.equals("INZ")) {

					consulta += "dfs.articulo IS NOT NULL THEN FACTURACION.getcodigointerfaz(dfs.articulo) ";
				}
			}
			if (tipoServicio < 0) {

					consulta += "ELSE dfs.servicio END AS codigo, ";
			}
			else
			{
				consulta += "ELSE CASE WHEN FACTURACION.getcodigosoat(dfs.servicio,'"+tipoServicio+"') "+
						    "IS NOT NULL THEN FACTURACION.getcodigosoat(dfs.servicio,'"+tipoServicio+"') " +
						    "ELSE  CAST(dfs.servicio AS VARCHAR2(200)) END END AS codigo, ";		
				
			}
			

			// Modificado por la Tarea 52557
			consulta += "CASE WHEN dfs.articulo IS NOT NULL THEN "
					+ "a.codigo_interfaz " + "ELSE "
					+ "(CASE WHEN con.tipo_codigo IS NOT NULL THEN "
					+ "getcodigopropservicio2(dfs.servicio, con.tipo_codigo) "
					+ "ELSE " + "getcodigopropservicio2(dfs.servicio, "
					+ ConstantesBD.codigoTarifarioCups
					+ ") "
					+ "END) "
					+ "END AS codigopropietario, "
					+

					// Modificado por la Tarea 52557
					"CASE WHEN dfs.articulo IS NOT NULL THEN "
					+ "a.descripcion "
					+ "ELSE "
					+ "(CASE WHEN con.tipo_codigo IS NOT NULL THEN "
					+ "getnombreserviciotarifa2(dfs.servicio, con.tipo_codigo) "
					+ "ELSE "
					+ "getnombreserviciotarifa2(dfs.servicio, "
					+ ConstantesBD.codigoTarifarioCups
					+ ") "
					+ "END) "
					+ "END AS descripcionservicio, "
					+

					"dfs.cantidad_cargo AS cantidad, "
					+ "dfs.valor_cargo AS valunitario, "
					+ "dfs.valor_total AS valtotal, "
					+ "CASE WHEN sol.tipo = "
					+ ConstantesBD.codigoTipoSolicitudCirugia
					+ " THEN "
					+ ValoresPorDefecto.getValorTrueParaConsultas()
					+ " "
					+ "ELSE "
					+ ValoresPorDefecto.getValorFalseParaConsultas()
					+ " END AS es_qx, "
					+ "CASE WHEN sol.tipo= "
					+ ConstantesBD.codigoTipoSolicitudCirugia
					+ " AND dfs.articulo IS NOT NULL THEN '"
					+ ConstantesBD.acronimoSi
					+ "' ELSE '"
					+ ConstantesBD.acronimoNo
					+ "' end as es_material_especial "
					+ "FROM "
					+ "facturas f "
					+ "INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "
					+ "INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "
					+ "INNER JOIN centros_costo cc ON(sol.centro_costo_solicitado=cc.codigo) "
					+ "INNER JOIN convenios con ON (f.convenio = con.codigo) "
					+ "LEFT OUTER JOIN articulo a ON(dfs.articulo=a.codigo and f.institucion=a.institucion) "
					+ "LEFT OUTER JOIN referencias_servicio rs ON(dfs.servicio=rs.servicio and rs.tipo_tarifario="
					+ ConstantesBD.codigoTarifarioCups
					+ ") "
					+ "WHERE "
					+ "f.codigo=? AND dfs.valor_total>0 ORDER BY sol.numero_solicitud, dfs.articulo";
			
			
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===>consultaSolicitudesFacturaStr-->"+consulta+" ->codigoFactura="+(long)codigoFactura);
			cargarStatement.setLong(1, (long)codigoFactura);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de las solicitudes asociadas a una factura: SqlBaseConsultaFacturasDao"+e.toString());
			return null;
		}
	}


	/**
	 * Metodo para cargar los recibos de caja Asociados a una Factura.
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public static Collection cargarListadoRecibosCaja(Connection con, double codigoFactura)
	{
        try
        {
            String consulta = "		SELECT  rc.consecutivo_recibo as numero_recibo, pfp.valor as valor  " +
	 		    			  "				FROM pagos_facturas_paciente pfp  					" + 
							  "					 INNER JOIN recibos_caja rc ON ( rc.numero_recibo_caja = pfp.documento ) " +  
							  "					 INNER JOIN facturas fac ON ( fac.codigo = pfp.factura )   " + 
							  "								WHERE pfp.tipo_doc =  " + ConstantesBD.codigoTipoDocumentoPagosReciboCaja  +  //-Recibo de Caja
							  "							      AND rc.estado <> " + ConstantesBD.codigoEstadoReciboCajaAnulado +
		 			          "								  AND fac.codigo = ? ";
        	
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setDouble(1, codigoFactura);
            return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStatement.executeQuery()));
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en cargarListadoRecibosCaja de : SqlBaseConsultaFacturasDao "+e.toString());
            return null;
        }
	}
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarListadoConvenio(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta="SELECT " +
							"tc.descripcion as nombretipoconvenio, " +
							"con.nombre as nombreconvenio, " +
							"f.consecutivo_factura as consecutivofactura, " +
							"to_char(f.fecha, 'DD/MM/YYYY') as fechafactura, " +
							"f.valor_total as valorfactura, " +
							"coalesce(f.ajustes_credito,0) as ajustescredito, " +
							"coalesce(f.ajustes_debito,0) as ajustesdebito, " +
							"(f.valor_total+ coalesce(f.ajustes_debito,0)- coalesce(f.ajustes_credito,0)) as totalfacturado  " +
						"FROM " +
							"facturas f " +
							"INNER JOIN convenios con ON(f.convenio=con.codigo) " +
							"INNER JOIN tipos_convenio tc ON(tc.codigo=con.tipo_convenio and tc.institucion=con.institucion) " +
							"INNER JOIN cuentas c ON(c.id=f.cuenta) "+
							"INNER JOIN estados_factura_f ef ON(f.estado_facturacion=ef.codigo) "+
							"INNER JOIN estados_factura_paciente ep ON(f.estado_paciente=ep.codigo) "+
						"WHERE " +
							"1=1 ";
		
		consulta+=armarWhereFiltroFacturas(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion );
		consulta+=" ORDER BY nombreconvenio, consecutivofactura ";
		
		logger.info("\n\ncargarListadoConvenioPaciente->"+consulta);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarListadoFacturadoRadicado(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta="SELECT " +
							"tc.descripcion as nombretipoconvenio, " +
							"con.nombre as nombreconvenio, " +
							"f.consecutivo_factura as consecutivofactura, " +
							"to_char(f.fecha, 'DD/MM/YYYY') as fechafactura, " +
							"case when cc.fecha_radicacion is not null then to_char(cc.fecha_radicacion, 'DD/MM/YYYY') else '' end as fecharadicacion, " +
							"case when cc.estado<>"+ConstantesBD.codigoEstadoCarteraRadicada+" then 0 else (coalesce(f.valor_cartera,0) + getAjustesFactura(f.codigo, f.numero_cuenta_cobro,"+ConstantesBD.codigoAjusteDebitoFactura+","+ConstantesBD.codigoEstadoCarteraAprobado+") - getAjustesFactura(f.codigo, f.numero_cuenta_cobro, "+ConstantesBD.codigoAjusteCreditoFactura+","+ConstantesBD.codigoEstadoCarteraAprobado+")) end as valorradicado, " +
							"getAjustesFactura(f.codigo, f.numero_cuenta_cobro,"+ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoEstadoCarteraAprobado+") as ajustesdebito, "+
							"getAjustesFactura(f.codigo, f.numero_cuenta_cobro,"+ConstantesBD.codigoAjusteCreditoCuentaCobro+","+ConstantesBD.codigoEstadoCarteraAprobado+") as ajustescredito, " +
							"case when cc.estado<>"+ConstantesBD.codigoEstadoCarteraRadicada+" then 0 else ((coalesce(f.valor_cartera,0) + getAjustesFactura(f.codigo, f.numero_cuenta_cobro,"+ConstantesBD.codigoAjusteDebitoFactura+","+ConstantesBD.codigoEstadoCarteraAprobado+") - getAjustesFactura(f.codigo, f.numero_cuenta_cobro, "+ConstantesBD.codigoAjusteCreditoFactura+","+ConstantesBD.codigoEstadoCarteraAprobado+")) + getAjustesFactura(f.codigo, f.numero_cuenta_cobro,"+ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoEstadoCarteraAprobado+") - getAjustesFactura(f.codigo, f.numero_cuenta_cobro,"+ConstantesBD.codigoAjusteCreditoCuentaCobro+","+ConstantesBD.codigoEstadoCarteraAprobado+") ) end as totalradicado "+
						"FROM " +
							"facturas f " +
							"INNER JOIN convenios con ON(f.convenio=con.codigo) " +
							"INNER JOIN tipos_convenio tc ON(tc.codigo=con.tipo_convenio and tc.institucion=con.institucion) " +
							"INNER JOIN cuentas c  ON(c.id=f.cuenta) " +
							"INNER JOIN estados_factura_f ef ON(f.estado_facturacion=ef.codigo) " +
							"INNER JOIN estados_factura_paciente ep ON(f.estado_paciente=ep.codigo) " +
							"inner JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro and f.institucion=cc.institucion) " +
						"WHERE " +
							"1=1 ";
		
		consulta+=armarWhereFiltroFacturas(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
		consulta+=" ORDER BY nombreconvenio, consecutivofactura ";
		
		logger.info("\n\ncargarListadoFacturadoRadicado->"+consulta);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
	
	/**
	 * Metodo que consulta los datos de la factura para la generacio del Reporte Resumido por solicitud
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap<Object, Object> consultaDatosFactura(Connection con, String codFactura)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDatosFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codFactura);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			logger.info("ERROR. En consulta de los datos de la Factura para generar Reporte por Solicitud.>>>>>>"+e);
			return null;
		}
		return mapa;
	}
	
	/**
	 * Metodo para obtener el numero de Autorizacion
	 * @param codigoCuenta
	 * @param codSubCuenta
	 * @return
	 */
	public static  String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta)
	{
	    String nroAutorizacion= "";
		String cadenaConsulta= ConsultasBirt.consultarNumeroAutorizacion(codigoCuenta, codSubCuenta);
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps;
		try
		{
            logger.info("\n\n Cadena de Busqueda Numero Autorizacion: >> "+ cadenaConsulta);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
            
			if(rs.next())
			  {
				 
				nroAutorizacion  = rs.getString("numeroautorizacion");
			  } 
			
			rs.close();
			
			 
		}	
		catch (SQLException e) 
		{
			logger.info("ERROR. En consulta del Numero de la Autorizacion .>>>>>>"+e);
			
		}
		UtilidadBD.closeConnection(con);
		return nroAutorizacion;
		
	}
	
	
	
}
