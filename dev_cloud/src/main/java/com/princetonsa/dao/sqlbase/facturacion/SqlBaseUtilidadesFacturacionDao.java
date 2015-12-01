/*
 * Abr 27, 2007
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoPorcentajeValor;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoArtIncluidoServiPpal;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoIgualConsecutivoFactura;
import com.princetonsa.dto.facturacion.DtoServiIncluidoServiPpal;
import com.princetonsa.dto.facturacion.DtoServiPpalIncluido;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.facturacion.CalculoHonorariosPooles;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * 
 * @author sgomez
 * Objeto usado para el acceso común a la fuente de datos
 * de utilidades propias del módulo de FACTURACION
 */
public class SqlBaseUtilidadesFacturacionDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesFacturacionDao.class);
	
	
	/**
	 * Método implementado para cargar los convenios por una clasificacion específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarConveniosXClasificacion(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "SELECT " +
				"c.codigo," +
				"c.nombre " +
				"FROM convenios c " +
				"INNER JOIN tipos_convenio tc ON(tc.codigo=c.tipo_convenio AND tc.institucion=c.institucion) " +
				"INNER JOIN clasificacion_tipo_convenio ctc ON(ctc.codigo=tc.clasificacion) " +
				"WHERE ctc.codigo=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("clasificacion").toString()));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			return UtilidadBD.cargarValueObject(rs);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarConveniosXClasificacion: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los tarifarios oficiales
	 * @param con
	 * @return
	 */
	public static HashMap cargarTarifariosOficiales(Connection con)
	{
		try
		{
			String consulta = "select codigo,nombre from tarifarios_oficiales";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTarifariosOficiales: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para obtener el valor del salario mínimo vigente
	 * @param con
	 * @return
	 */
	public static double obtenerValorSalarioMinimoVigente(Connection con)
	{
		try
		{
			double salario = 0;
			String consulta = "select salario As salario from salario_minimo WHERE current_date between fecha_inicial and fecha_final";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				salario = rs.getDouble("salario");
			
			return salario;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerValorSalarioMinimoVigente: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que carga los estratos sociales
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarEstratosSociales(Connection con,HashMap campos, int tipoBD)
	{
		try
		{
			String consulta = "SELECT DISTINCT es.codigo,es.descripcion FROM estratos_sociales es ";
			//Si se ingresa convenio se debe filtrar por monto de cobro
			if(Utilidades.convertirAEntero(campos.get("codigoConvenio").toString(), true)>0)
			{
				consulta += " INNER JOIN detalle_monto dmc ON(dmc.estrato_social_codigo=es.codigo) inner join " + 
				"(select * from montos_cobro mc " +
				"where mc.convenio            = " + campos.get("codigoConvenio") + " " +
				"and mc.vigencia_inicial = GETVIGENCIACONVENIO(" + campos.get("codigoConvenio") + ", '" + campos.get("fechaReferencia") + "' ) " +
				" order by mc.codigo desc) mc " +
				" on (mc.codigo=dmc.monto_codigo AND mc.convenio = "+campos.get("codigoConvenio")+" "+(Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString(), true)>0?" and dmc.via_ingreso_codigo = "+campos.get("codigoViaIngreso")+" ":"")+" and dmc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+") ";
			}
			
			consulta += " WHERE es.institucion = "+campos.get("institucion")+" ";
			if(!campos.get("activo").toString().equals(""))
				consulta += " and es.activo = "+(UtilidadTexto.getBoolean(campos.get("activo").toString())?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas());
			if(!campos.get("tipoRegimen").toString().equals(""))
				consulta += " and es.tipo_regimen = '"+campos.get("tipoRegimen")+"' ";
			consulta += " ORDER BY es.descripcion ";
			
			logger.info("\n\n\n\n\n\n\n\nCONSULTA DE ESTRATOS SOCIALES: "+consulta+"\n\n\n\n\n\n\n\n");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarEstratosSociales: "+e);
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap obtenerPaquetesValidosResponsable(Connection con, HashMap vo) 
	{
		String cadena="SELECT " +
							" codigo as codigo," +
							" institucion as institucion," +
							" convenio as convenio," +
							" contrato as contrato," +
							" paquete as paquete," +
							" getdescripcionpaquete(paquete,institucion) as descpaquete," +
							" getcodigoserviciopaquete(paquete,institucion) as codserviciopaquete," +
							" getCodigoGrupoServicio(getcodigoserviciopaquete(paquete,institucion)) as codigogruposer," +
							" getnombreservicio(getcodigoserviciopaquete(paquete,institucion),"+ConstantesBD.codigoTarifarioCups+") as nomserviciopaquete, " +
							" via_ingreso as viaingreso," +
							" getnombreviaingreso(via_ingreso) as nomviaingreso," +
							" fecha_inicial_venc as fechainicialvenc," +
							" fecha_final_venc as fechafinalvenc " +
					" from paquetes_convenio " +
					" where convenio=? and (via_ingreso is null or via_ingreso=?) and ((? between fecha_inicial_venc and fecha_final_venc) or (fecha_inicial_venc is null and fecha_final_venc is null)) ";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("convenio")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("viaingreso")+""));
			ps.setDate(3, Date.valueOf(vo.get("fechafiltro")+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * Metodo que valida si un indicador de calidad se encuentra parametrizado
	 * @param con
	 * @param indicador
	 * @return
	 */
	public static boolean indicadorParametrizado(Connection con, String indicador)
	{
		
		String cadena="SELECT tipo, codigo from ind_calidad_codigos where acronimo='"+indicador+"'" ;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		try
		{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("[Errror en la consulta parametrizacion indicadores]"+cadena);
			e.printStackTrace();
		}
		
		if (Utilidades.convertirAEntero(mapa.get("numRegistros")+"")>0)
		{
			if (mapa.get("tipo_0").toString().equals("ES"))
			{
				try{
				cadena="SELECT count(1) as existe from ind_calidad_especialidad where ind_calidad_codigo='"+mapa.get("codigo_0")+"'";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				
				if (Utilidades.convertirAEntero(mapa.get("existe_0")+"")>0)
					return true;
				else
					return false;
				}
				catch (Exception e) {
					logger.info("[Errror en la consulta parametrizacion indicadores]"+cadena+"\n\n"+e);
				}
			}
			else
				if(mapa.get("tipo_0").toString().equals("DX"))
				{
					try{
						cadena="SELECT count(1) as existe from ind_calidad_dx where ind_calidad_codigo='"+mapa.get("codigo_0")+"'";
						PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
						if (Utilidades.convertirAEntero(mapa.get("existe_0")+"")>0)
							return true;
						else
							return false;
						}
						catch (Exception e) {
							logger.info("[Errror en la consulta parametrizacion indicadores]"+cadena+"\n\n"+e);
						}
				}
				else
					if (mapa.get("tipo_0").toString().equals("CC"))
					{
						try{
							cadena="SELECT count(1) as existe from ind_calidad_cc where ind_calidad_codigo='"+mapa.get("codigo_0")+"'";
							PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
							if (Utilidades.convertirAEntero(mapa.get("existe_0")+"")>0)
								return true;
							else
								return false;
							}
							catch (Exception e) {
								logger.info("[Errror en la consulta parametrizacion indicadores]"+cadena+"\n\n"+e);
							}
					}
					else
					return false;
		}
		else
			return false;
		
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public static String obtenerTarfiaServicio(Connection con, int esquemaTarifario, int servicio, String fechaCalculoVigencia) 
	{
		String resultado=ConstantesBD.codigoNuncaValido+"";
		String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
		
		try
		{
			String consultaTarifarioOficialEsquema="select tarifario_oficial from esquemas_tarifarios where codigo="+esquemaTarifario;
			logger.info("\n\n\n\n\nobtenerTarfiaServicio\n");
			logger.info("--->"+consultaTarifarioOficialEsquema);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTarifarioOficialEsquema));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				int tipoTarifario=rs.getInt(1);
				logger.info("VERFICANDO TARIFA TIPO TARIFARIO -->"+tipoTarifario);
				if(tipoTarifario==ConstantesBD.codigoTarifarioISS)
				{
					String consultaTarifa=	"(SELECT valor_tarifa,fecha_vigencia from tarifas_iss where servicio="+servicio+" and esquema_tarifario="+esquemaTarifario+"  and valor_tarifa is not null and to_char(fecha_vigencia,'yyyy-mm-dd')<='"+fecha+"') " +
												"UNION ALL " +
											"(SELECT valor_tarifa,fecha_vigencia from tarifas_iss where servicio="+servicio+" and esquema_tarifario="+esquemaTarifario+"  and valor_tarifa is not null and fecha_vigencia is null)  order by fecha_vigencia desc";
					logger.info("TARIFA -->"+consultaTarifa);
					PreparedStatementDecorator psT= new PreparedStatementDecorator(con.prepareStatement(consultaTarifa));
					ResultSetDecorator rsT=new ResultSetDecorator(psT.executeQuery());
					if(rsT.next())
					{
						resultado=rsT.getString(1);
					}
				}
				else if(tipoTarifario==ConstantesBD.codigoTarifarioSoat)
				{
					String consultaTarifa=	"(SELECT valor_tarifa,fecha_vigencia from tarifas_soat where servicio="+servicio+" and esquema_tarifario="+esquemaTarifario+"  and valor_tarifa is not null and to_char(fecha_vigencia,'yyyy-mm-dd')<='"+fecha+"') "+
												"UNION ALL " +
											"(SELECT valor_tarifa,fecha_vigencia from tarifas_soat where servicio="+servicio+" and esquema_tarifario="+esquemaTarifario+"  and valor_tarifa is not null and fecha_vigencia is null) order by fecha_vigencia desc";
					logger.info("TARIFA -->"+consultaTarifa);
					PreparedStatementDecorator psT= new PreparedStatementDecorator(con.prepareStatement(consultaTarifa));
					ResultSetDecorator rsT=new ResultSetDecorator(psT.executeQuery());
					if(rsT.next())
					{
						resultado=rsT.getString(1);
					}
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
	 * Método que consulta los montos de cobro por convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMontosCobroXConvenio(Connection con, String codigoConvenio, String fechaReferencia)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> mapaAux = new HashMap<String, Object>();
		
		try
		{
			String consulta = "SELECT "+ 
									"codigo, "+
									"CASE WHEN dm.via_ingreso_codigo IS NULL THEN '' ELSE dm.via_ingreso_codigo || '' END AS via_ingreso, "+
									"CASE WHEN dm.tipo_afiliado_codigo IS NULL THEN '' ELSE dm.tipo_afiliado_codigo END AS codigo_tipo_afiliado, "+
									"CASE WHEN dm.tipo_afiliado_codigo IS NULL THEN '' ELSE getnombretipoafiliado(dm.tipo_afiliado_codigo) END as nombre_tipo_afiliado, "+
									"CASE WHEN dm.estrato_social_codigo IS NULL THEN '' ELSE dm.estrato_social_codigo || '' END as estrato_social, "+
									"CASE WHEN tipo_monto_codigo IS NULL THEN '' ELSE tipo_monto_codigo || '' END AS tipo_monto, "+
									"CASE WHEN porcentaje IS NULL THEN '' ELSE porcentaje || '' END AS porcentaje, "+
									"CASE WHEN valor IS NULL THEN '' ELSE valor || '' END AS valor, "+
									"to_char(vigencia_inicial, 'YYYY-MM-DD') AS vigencia_inicial," +
									"dm.via_ingreso_codigo||'_'||dm.tipo_afiliado_codigo||'_'||dm.estrato_social_codigo||'_'||tipo_monto_codigo||'_'||vigencia_inicial AS aux "+ 
								"FROM " +
									"detalle_monto dm inner join montos_cobro mc on (dm.monto_codigo=mc.codigo) left outer join detalle_monto_general dmg on (dmg.detalle_codigo=dm.detalle_codigo)"+ 
								"WHERE "+
									"mc.convenio = "+codigoConvenio+" ";
			
			if(!fechaReferencia.isEmpty())
				consulta +=			"AND mc.vigencia_inicial <= '"+fechaReferencia+"' ";
			
			consulta +=			"ORDER BY " +
									"aux DESC ";
									
			logger.info("\n SQL obtenerMontosCobroXConvenio / "+consulta);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			String combinacion="";
			String fecha="";
			
			while(rs.next())
			{
				if (!combinacion.equals(rs.getString("via_ingreso")+"_"+rs.getString("codigo_tipo_afiliado")+"_"+rs.getString("estrato_social")+"_"+rs.getString("tipo_monto")))
				{
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigo",rs.getObject("codigo"));
					elemento.put("viaIngreso",rs.getObject("via_ingreso")==null?"":rs.getObject("via_ingreso"));
					elemento.put("codigoTipoAfiliado",rs.getObject("codigo_tipo_afiliado")==null?"":rs.getObject("codigo_tipo_afiliado"));
					elemento.put("nombreTipoAfiliado",rs.getObject("nombre_tipo_afiliado")==null?"":rs.getObject("nombre_tipo_afiliado"));
					elemento.put("estratoSocial",rs.getObject("estrato_social")==null?"":rs.getObject("estrato_social"));
					elemento.put("tipoMonto",rs.getObject("tipo_monto")==null?"":rs.getObject("tipo_monto"));
					elemento.put("porcentaje",rs.getObject("porcentaje")==null?"":rs.getObject("porcentaje"));
					elemento.put("valor",rs.getObject("valor")==null?"":rs.getObject("valor"));
					elemento.put("vigenciaInicial",rs.getString("vigencia_inicial"));
					resultados.add(elemento);
				}
				
				if ((combinacion.equals(rs.getString("via_ingreso")+"_"+rs.getString("codigo_tipo_afiliado")+"_"+rs.getString("estrato_social")+"_"+rs.getString("tipo_monto")) && fecha.equals(rs.getString("vigencia_inicial"))))
				{
					logger.info("2");
					logger.info(rs.getString("via_ingreso")+"_"+rs.getString("codigo_tipo_afiliado")+"_"+rs.getString("estrato_social")+"_"+rs.getString("tipo_monto"));
					
					
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigo",rs.getObject("codigo"));
					elemento.put("viaIngreso",rs.getObject("via_ingreso")==null?"":rs.getObject("via_ingreso"));
					elemento.put("codigoTipoAfiliado",rs.getObject("codigo_tipo_afiliado")==null?"":rs.getObject("codigo_tipo_afiliado"));
					elemento.put("nombreTipoAfiliado",rs.getObject("nombre_tipo_afiliado")==null?"":rs.getObject("nombre_tipo_afiliado"));
					elemento.put("estratoSocial",rs.getObject("estrato_social")==null?"":rs.getObject("estrato_social"));
					elemento.put("tipoMonto",rs.getObject("tipo_monto")==null?"":rs.getObject("tipo_monto"));
					elemento.put("porcentaje",rs.getObject("porcentaje")==null?"":rs.getObject("porcentaje"));
					elemento.put("valor",rs.getObject("valor")==null?"":rs.getObject("valor"));
					elemento.put("vigenciaInicial",rs.getString("vigencia_inicial"));
					resultados.add(elemento);
				}
				
				
				
				combinacion = rs.getString("via_ingreso")+"_"+rs.getString("codigo_tipo_afiliado")+"_"+rs.getString("estrato_social")+"_"+rs.getString("tipo_monto");
				fecha = rs.getString("vigencia_inicial");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMontosCobroXConvenio: "+e);
		}
		return resultados;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @param servicio
	 * @return
	 */
	public static boolean esServicioPrincComponentePaquete(Connection con, String codigoPaquete, int institucion, String servicio) 
	{
		String cadena="SELECT principal from paq_comp_servicios where institucion = "+institucion+" and codigo_paquete='"+codigoPaquete+"' and codigo_servicio="+servicio+" and principal='"+ConstantesBD.acronimoSi+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			logger.info(cadena);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return UtilidadTexto.getBoolean(rs.getString(1));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerServiciosPrincipalesPaquete(Connection con, String codigoPaquete, int institucion)
	{
		ArrayList resultado=new ArrayList();
		String cadena="SELECT codigo_servicio as servicio from paq_comp_servicios where institucion = "+institucion+" and codigo_paquete='"+codigoPaquete+"' and principal='"+ConstantesBD.acronimoSi+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(rs.getObject(1)+"");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	
		
	
	
	/**
	 * Método implementado para eliminar los registros en solicitudes_subcuenta por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean eliminarRegistrosSolicitudesSubCuenta(Connection con,String numeroSolicitud)
	{
		try
		{
			String consulta = "DELETE FROM solicitudes_subcuenta WHERE solicitud = "+numeroSolicitud+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			if(st.executeUpdate(consulta)>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarSolicitudesSubCuenta: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numerSolicitud
	 * @param codServArti
	 * @param esServicio
	 * @param cargoFacturado 
	 * @return
	 */
	public static double obtenerValorUnitarioCargadoConvenioBase(Connection con, String numerSolicitud, String codServArti,String serviciocx,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws BDException 
	{
		double resultado=0;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerValorUnitarioCargadoConvenioBase");
			String cadena="SELECT valor_unitario_tarifa from det_cargos dc inner join sub_cuentas sc on(dc.sub_cuenta=sc.sub_cuenta) where dc.solicitud="+numerSolicitud+" and dc.valor_unitario_tarifa is not null and dc.facturado='"+cargoFacturado+"' and dc.paquetizado='"+ConstantesBD.acronimoNo+"' AND dc.eliminado='"+ConstantesBD.acronimoNo+"' ";
			boolean ponerLimit=false;
			if(esServicio)
			{
				//caso raro que se dá por soliicutdes malas.
				if(UtilidadTexto.isEmpty(codServArti)){
					cadena+=" and dc.servicio is null";
					ponerLimit=true;
				}
				else{
					cadena+=" and dc.servicio="+codServArti;
				}
				if(!UtilidadTexto.isEmpty(serviciocx)&&Utilidades.convertirAEntero(serviciocx)>0){
					cadena=cadena+" AND dc.servicio_cx="+serviciocx;
					if(Utilidades.convertirAEntero(detCxHonorarios)>0){
						cadena=cadena+" AND dc.det_cx_honorarios="+detCxHonorarios;
					}
					else{
						cadena=cadena+" AND dc.det_cx_honorarios is null";
					}
					if(Utilidades.convertirAEntero(detAsCxSalMat)>0){
						cadena=cadena+" AND dc.det_asocio_cx_salas_mat="+detAsCxSalMat;
					}
					else{
						cadena=cadena+" AND dc.det_asocio_cx_salas_mat is null";
					}
				}
			}
			else{
				cadena+=" and dc.articulo="+codServArti;
			}
			cadena=cadena+" order by nro_prioridad";
			if(ponerLimit){
				cadena=cadena+" "+ValoresPorDefecto.getValorLimit1()+" 1";
			}
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet );
			rs=pst.executeQuery();
			if(rs.next())
			{
				resultado=rs.getDouble(1);
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e.getMessage(),e);
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
		Log4JManager.info("############## Fin obtenerValorUnitarioCargadoConvenioBase");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param numerSolicitud
	 * @param codServArti
	 * @param serviciocx 
	 * @param esServicio
	 * @param cargoFacturado 
	 * @return
	 */
	public static int obtenerEsquemaTarifarioBase(Connection con, String numerSolicitud, String codServArti, String serviciocx,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws BDException
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerEsquemaTarifarioBase");
			String cadena="SELECT esquema_tarifario from det_cargos dc inner join sub_cuentas sc on(dc.sub_cuenta=sc.sub_cuenta) where dc.solicitud="+numerSolicitud+" and dc.facturado='"+cargoFacturado+"' and dc.paquetizado='"+ConstantesBD.acronimoNo+"' AND dc.eliminado='"+ConstantesBD.acronimoNo+"' ";
			boolean ponerLimit=false;
			if(esServicio){
				//caso raro que se dá por soliicutdes malas.
				if(UtilidadTexto.isEmpty(codServArti)){
					cadena+=" and dc.servicio is null";
					ponerLimit=true;
				}
				else{
					cadena+=" and dc.servicio="+codServArti;
				}
				if(!UtilidadTexto.isEmpty(serviciocx)&&Utilidades.convertirAEntero(serviciocx)>0){
					cadena=cadena+" AND dc.servicio_cx="+serviciocx;
					if(Utilidades.convertirAEntero(detCxHonorarios)>0){
						cadena=cadena+" AND dc.det_cx_honorarios="+detCxHonorarios;
					}
					else{
						cadena=cadena+" AND dc.det_cx_honorarios is null";
					}
					if(Utilidades.convertirAEntero(detAsCxSalMat)>0){
						cadena=cadena+" AND dc.det_asocio_cx_salas_mat="+detAsCxSalMat;
					}
					else{
						cadena=cadena+" AND dc.det_asocio_cx_salas_mat is null";
					}
				}
			}
			else{
				cadena+=" and dc.articulo="+codServArti;
			}
			cadena=cadena+" order by nro_prioridad";
			if(ponerLimit){
				cadena=cadena+" "+ValoresPorDefecto.getValorLimit1()+" 1";
			}
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet );
			rs=pst.executeQuery();
			if(rs.next())
			{
				resultado=rs.getInt(1);
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e.getMessage(),e);
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
		Log4JManager.info("############## Fin obtenerEsquemaTarifarioBase");
		return resultado;
	}

	

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean responsableTieneSolPaquetes(Connection con, String subCuenta) 
	{
		String cadena="SELECT count(1) from det_cargos where sub_cuenta ="+Utilidades.convertirALong(subCuenta)+" and tipo_solicitud="+ConstantesBD.codigoTipoSolicitudPaquetes +"AND eliminado='"+ConstantesBD.acronimoNo+"' ";
		boolean resultado=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getInt(1)>0;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			resultado=false;
		}
		return resultado;
	}


	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerResposablePaquetizadoDadaSolicitud(Connection con, String numeroSolicitud) 
	{
		String resultado=ConstantesBD.codigoNuncaValido+"";
		try
		{
			String consultaTarifarioOficialEsquema=" SELECT sub_cuenta from paquetizacion where numero_solicitud_paquete="+numeroSolicitud;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTarifarioOficialEsquema));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getString(1);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * Método que verifica si una solicitud/servicio/asocio ha sido distribuido
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudServicioAsocioDistribuido(Connection con,HashMap campos)
	{
		boolean esDistribuido = false;
		try
		{
			String tipoServicio = campos.get("tipoServicio").toString();
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			
			String consulta = "";
			
			if(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
			{
				consulta = "SELECT count(1) As cuenta " +
					"FROM solicitudes_subcuenta " +
					"WHERE " +
					"det_cx_honorarios = "+consecutivoAsocio+" AND " +
					"eliminado='"+ConstantesBD.acronimoNo+"' ";
			}
			else
			{
				consulta = "SELECT count(1) As cuenta " +
					"FROM solicitudes_subcuenta " +
					"WHERE " +
					"det_asocio_cx_salas_mat = "+consecutivoAsocio+" AND " +
					"eliminado='"+ConstantesBD.acronimoNo+"' ";
			}
			
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			logger.info("consulta=> "+consulta);
			int cuenta = 0;
			if(rs.next())
				cuenta = rs.getInt("cuenta");
			
			if(cuenta>1)
				esDistribuido = true;
			else
				esDistribuido = false;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudServicioAsocioDistribuido: "+e);
		}
		return esDistribuido;
	}
	
	/**
	 * Verifica su una solicitud/servicio/asocio ha sido facturado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudServicioAsocioFacturado(Connection con,HashMap campos)
	{
		boolean esFacturado = false;
		try
		{
			
			String tipoServicio = campos.get("tipoServicio").toString();
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			
			String consulta = "";
			
			if(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
			{
				consulta = "SELECT count(1) As cuenta " +
					"FROM det_cargos " +
					"WHERE " +
					"det_cx_honorarios = "+consecutivoAsocio+" AND " +
					"facturado='"+ConstantesBD.acronimoSi+"' AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			}
			else
			{
				consulta = "SELECT count(1) As cuenta " +
				"FROM det_cargos " +
				"WHERE " +
				"det_asocio_cx_salas_mat = "+consecutivoAsocio+" AND " +
				"facturado='"+ConstantesBD.acronimoSi+"' AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			}
			
			
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			int cuenta = 0;
			if(rs.next())
				cuenta = rs.getInt("cuenta");
			
			if(cuenta>0)
				esFacturado = true;
			else
				esFacturado = false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudServicioAsocioFacturado: "+e);
		}
		return esFacturado;
	}
	
	/**
	 * Método que verifica si una solicitud es paquetizada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudPaquetizada(Connection con,HashMap campos)
	{
		boolean esPaquetizada = false;
		try
		{
			String consulta = "SELECT count(1) As cuenta " +
				"FROM solicitudes_subcuenta " +
				"WHERE solicitud = "+campos.get("numeroSolicitud")+" AND paquetizada = '"+ConstantesBD.acronimoSi+"' AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			int cuenta = 0;
			if(rs.next())
				cuenta = rs.getInt("cuenta");
			
			if(cuenta>0)
				esPaquetizada = true;
			else
				esPaquetizada = false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudPaquetizada: "+e);
		}
		return esPaquetizada;
	}
	
	/**
	 * Método que verifica si una solicitud/servicio/asocio está cargado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudServicioAsocioCargado(Connection con,HashMap campos)
	{
		boolean esCargado = false;
		try
		{
			
			String tipoServicio = campos.get("tipoServicio").toString();
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			
			String consulta = "";
			
			if(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
			{
				consulta = "SELECT count(1) As cuenta " +
					"FROM det_cargos " +
					"WHERE " +
					"det_cx_honorarios = "+consecutivoAsocio+"  AND " +
					"estado="+ConstantesBD.codigoEstadoFCargada+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			}
			else
			{
				consulta = "SELECT count(1) As cuenta " +
					"FROM det_cargos " +
					"WHERE " +
					"det_asocio_cx_salas_mat = "+consecutivoAsocio+"  AND " +
					"estado="+ConstantesBD.codigoEstadoFCargada+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			}
		
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			int cuenta = 0;
			if(rs.next())
				cuenta = rs.getInt("cuenta");
			
			if(cuenta>0)
				esCargado = true;
			else
				esCargado = false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudServicioAsocioCargado: "+e);
		}
		return esCargado;
	}
	
	/**
	 * Método que consulta los convenios asociados a una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> cargarConveniosSolicitud(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "SELECT DISTINCT "+
				"ss.sub_cuenta AS sub_cuenta, "+
				"getnombreconvenio(sc.convenio) AS nombre_convenio "+ 
				"FROM solicitudes_subcuenta  ss "+ 
				"INNER JOIN sub_cuentas sc on(ss.sub_cuenta=sc.sub_cuenta) "+
				"where "+ 
				"ss.solicitud = "+campos.get("numeroSolicitud")+" AND eliminado='"+ConstantesBD.acronimoNo+"' "+
				"ORDER BY nombre_convenio";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true, true);
	        st.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarConveniosSolicitud: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo encargado de consultar los conceptos
	 * de facturas varias.
	 * Metodo adicionado por Jhony Alexander Duque A.
	 * KEY'S DEL MAPA PARAMETROS
	 * -----------------------------------------
	 * --institucion --> Requerido
	 * --tipoCartera --> Opcional ejem. 1,-1,2
	 * --naturaleza --> Opcional ejem.  5,6
	 * -----------------------------------------
	 * @param connection
	 * @param parametros
	 * Key's del mapa resultado
	 * --------------------------------------------
	 * codigo,naturaleza,descripcion,tipoCartera
	 * 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConseptosFacturasVarias(Connection connection,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		
		String cadena = "SELECT " +
							 	" cac.codigo As codigo," +
							 	" naturaleza As naturaleza," +
							 	" descripcion As descripcion," +
							 	" tipo_cartera AS tipocartera " +
						" FROM  concepto_ajustes_cartera cac ";
		
		String where="WHERE  institucion="+parametros.get("institucion");
		
		if (parametros.containsKey("tipoCartera"))
			if (!(parametros.get("tipoCartera")+"").equals("") && !(parametros.get("tipoCartera")+"").equals("null"))
				where+=" AND cac.tipo_cartera IN ("+parametros.get("tipoCartera")+")";
		
		if (parametros.containsKey("naturaleza"))
			if (!(parametros.get("naturaleza")+"").equals("") && !(parametros.get("tipoCartera")+"").equals("null"))
				where+="AND cac.naturaleza IN ("+parametros.get("naturaleza")+")";
		
		where+=" ORDER BY descripcion ASC";
		
			
		cadena +=where;
		logger.info("\n cadena --> "+cadena);
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("naturaleza",rs.getObject("naturaleza"));
				elemento.put("descripcion",rs.getObject("descripcion"));
				elemento.put("tipoCartera",rs.getObject("tipocartera"));
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en  obteniendo conceptos de facturas varias: "+e);
		}
		      
		return resultados;
		
	}
	
	
	
	/**
	 * Metodo que puede devolver todos los pooles o
	 * los puede filtar mediante el HashMap parametros.
	 * ----------------------------------------
	 * 		KEY'S DEL HASHMAP PARAMETROS
	 * ----------------------------------------
	 * --codigopool --> Opcional
	 * --activopool --> Opcional
	 * --terceroresponsablepool --> Opcional
	 * 
	 * Este metodo devuelve un arrayList de HashMap
	 * con los siguientes Key's.
	 * -------------------------------------------
	 * 	 KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * -------------------------------------------
	 * codigopool, descripcionpool, terceroresponsablepool,
	 * activopool
	 * @param connection
	 * @param codigo
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPooles(Connection connection,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT codigo AS codigopool," +
				"descripcion AS descripcionpool," +
				"tercero_responsable AS terceroresponsablepool," +
				"activo AS activopool " +
				"FROM  pooles ",where="WHERE  1=1 ";
		
		if (parametros.containsKey("codigopool"))
			if (!parametros.get("codigopool").toString().equals(""))
				where+="AND codigo="+Utilidades.convertirAEntero(parametros.get("codigopool").toString());
		
		if (parametros.containsKey("activopool"))
			if (!parametros.get("activopool").toString().equals(""))
				where+="AND activo="+Utilidades.convertirAEntero(parametros.get("activopool").toString());
		
		if (parametros.containsKey("terceroresponsablepool"))
			if (!parametros.get("terceroresponsablepool").toString().equals(""))
				where+="AND tercero_responsable="+Utilidades.convertirAEntero(parametros.get("terceroresponsablepool").toString());
		
		cadena +=where;
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigopool",rs.getObject("codigopool"));
				elemento.put("descripcionpool",rs.getObject("descripcionpool"));
				elemento.put("terceroresponsablepool",rs.getObject("terceroresponsablepool"));
				elemento.put("activopool",rs.getObject("activopool"));
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerPooles: "+e);
		}
		      
		return resultados;
		
	}
	
	
	/**
	 * Metodo que puede devuelver todos los conceptos
	 * de pago de pooles o puede ser filtrado
	 * por el codigo de concepto y/o por el 
	 * tipo del concepto.
	 * El HahMap parametros tiene los siguientes
	 * Key's:
	 * -----------------------------------
	 * 		  KEY'S DE PARAMETROS
	 * -----------------------------------
	 * --institucion --> Requerido
	 * --codigoconcepto --> Opcional
	 * --tipoconcepto --> Opcional
	 * Este metodo devuelve un arrayList de HashMap
	 * con los siguientes Key's.
	 * -------------------------------------------
	 * 	 KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * -------------------------------------------
	 * codigoconcepto, descripcionconcepto, tipoconcepto
	 * @param connection
	 * @param codigo
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosPagoPooles(Connection connection,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT codigo AS codigoconcepto," +
				"descripcion AS descripcionconcepto," +
				"tipo_concepto AS tipoconcepto " +
				"FROM  conceptos_pagos_pooles ",
				where="WHERE institucion="+Utilidades.convertirAEntero(parametros.get("institucion").toString());
		
		if (parametros.containsKey("codigoconcepto"))
			if (!parametros.get("codigoconcepto").toString().equals(""))
				where+=" AND codigo="+Utilidades.convertirAEntero(parametros.get("codigoconcepto").toString());
		    
		if (parametros.containsKey("tipoconcepto"))
			if (!parametros.get("tipoconcepto").toString().equals(""))
				where+=" AND tipo_concepto="+Utilidades.convertirAEntero(parametros.get("tipoconcepto").toString());
		             
		
		cadena +=where;
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoconcepto",rs.getObject("codigoconcepto"));
				elemento.put("descripcionconcepto",rs.getObject("descripcionconcepto"));
				elemento.put("tipoconcepto",rs.getObject("tipoconcepto"));
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerConceptosPagoPooles: "+e);
		}
		      
		return resultados;
		
	}
	
	
	/**
	 * Metodo que puede devuelver todos los
	 * tipos de servicios o puede ser filtrado
	 * por el acronimo y/o por el esqx y/o por
	 * el solicitar.
	 * El HahMap parametros tiene los siguientes
	 * Key's:
	 * -----------------------------------
	 * 		  KEY'S DE PARAMETROS
	 * -----------------------------------
	 * --acronimotiposervicio --> Opcional
	 * --esqx --> Opcional
	 * --solicitar --> Opcional
	 * Este metodo devuelve un arrayList de HashMap
	 * con los siguientes Key's.
	 * -------------------------------------------
	 * 	 KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * -------------------------------------------
	 * acronimotiposervicio, nombretiposervicio,
	 * descripciontiposervicio, esqx, solicitar.
	 * @param connection
	 * @param codigo
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT acronimo AS acronimotiposervicio," +
				"nombre AS nombretiposervicio," +
				"descripcion AS descripciontiposervicio," +
				"es_qx AS esqx," +
				"solicitar AS solicitar " +
				"FROM  tipos_servicio ",
				where="WHERE 1=1";
		
		if (parametros.containsKey("acronimotiposervicio"))
			if (!parametros.get("acronimotiposervicio").toString().equals(""))
				where+=" AND acronimo='"+parametros.get("acronimotiposervicio").toString()+"'";
		    
		if (parametros.containsKey("esqx"))
			if (!parametros.get("esqx").toString().equals(""))
				where+=" AND es_qx='"+parametros.get("esqx").toString()+"'";
		             
		if (parametros.containsKey("solicitar"))
			if (!parametros.get("solicitar").toString().equals(""))
				where+=" AND solicitar='"+parametros.get("solicitar").toString()+"'";
		
		cadena +=where+" ORDER BY nombretiposervicio DESC";
		
		
		
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("acronimotiposervicio",rs.getObject("acronimotiposervicio"));
				elemento.put("nombretiposervicio",rs.getObject("nombretiposervicio"));
				elemento.put("descripciontiposervicio",rs.getObject("descripciontiposervicio"));
				elemento.put("esqx",rs.getObject("esqx"));
				elemento.put("solicitar",rs.getObject("solicitar"));
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerTiposServicio: "+e);
		}
		      
		return resultados;
		
	}

	/**
	 * Metodo encargado de consultar todos los tipos 
	 * de servicios, pudiendolos filtrar por sus diferentes
	 * acronimos y por el indicativo esqx:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimos --> ejemplo, este es el valor de esta variable  'R','Q','D',
	 * --esqx --> debe indicar "t" o "f". 
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,String acronimos, String esqx)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT acronimo AS acronimotiposervicio," +
				"nombre AS nombretiposervicio," +
				"descripcion AS descripciontiposervicio," +
				"es_qx AS esqx," +
				"solicitar AS solicitar " +
				"FROM  tipos_servicio ",
				where="WHERE 1=1";
		
		if (UtilidadCadena.noEsVacio(acronimos))
			where+=" AND acronimo IN ("+acronimos+")";
		    
		if (UtilidadCadena.noEsVacio(esqx))
		{
			if(esqx.equalsIgnoreCase("t")||esqx.equalsIgnoreCase("true"))
				where+=" AND es_qx="+ValoresPorDefecto.getValorTrueParaConsultas();
			else
				where+=" AND es_qx="+ValoresPorDefecto.getValorFalseParaConsultas();
		}
			
		cadena +=where;
		
		logger.info("Tipos de servicio ->  "+cadena);
		
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("acronimotiposervicio",rs.getObject("acronimotiposervicio"));
				elemento.put("nombretiposervicio",rs.getObject("nombretiposervicio"));
				elemento.put("descripciontiposervicio",rs.getObject("descripciontiposervicio"));
				elemento.put("esqx",rs.getObject("esqx"));
				elemento.put("solicitar",rs.getObject("solicitar"));
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtenerTiposServicio: "+e);
			e.printStackTrace();
		}
		      
		return resultados;
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoDetCargo
	 * @return
	 */
	public static String obtenerconsecutivoFacturaDetCargo(Connection con, String codigoDetCargo) 
	{
		String cadena="SELECT consecutivo_factura from det_cargos dc  inner join det_factura_solicitud dfs on (dfs.solicitud=dc.solicitud and (dfs.servicio=dc.servicio or dfs.articulo=dc.articulo)) " +
								"inner join facturas f on(f.codigo=dfs.factura) where dc.codigo_detalle_cargo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetCargo));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerconsecutivoFacturaDetCargo: "+e);
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estadoFactura
	 * @return
	 */
	public static boolean subCuentaConFacturas(Connection con, String subCuenta, int estadoFactura) 
	{
		String cadena="SELECT count(1) from facturas where sub_cuenta =? ";
		if(estadoFactura>ConstantesBD.codigoNuncaValido)
			cadena=cadena+" and estado_facturacion="+estadoFactura;
			
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1)>0;
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerconsecutivoFacturaDetCargo: "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean responsableTieneSolAsociadas(Connection con, String subCuenta) 
	{
		String cadena="SELECT count(1) from solicitudes_subcuenta where sub_cuenta ="+Utilidades.convertirALong(subCuenta)+" and eliminado='"+ConstantesBD.acronimoNo+"' ";
		boolean resultado=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt(1)==0)
				{
					cadena="SELECT count(1) from det_cargos where sub_cuenta ="+Utilidades.convertirALong(subCuenta)+" and eliminado='"+ConstantesBD.acronimoNo+"' ";
					PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
					ResultSetDecorator rs1=  new ResultSetDecorator(ps1.executeQuery());
					if(rs1.next())
						resultado=rs1.getInt(1)>0;
				}
				else
				{
					resultado=rs.getInt(1)>0;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			resultado=false;
		}
		return resultado;
	}
	
	/**
	 * Método que verifica si un convenio es de colsanitas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esConvenioColsanitas(Connection con,HashMap campos)
	{
		try
		{
			boolean existe = false;
			
			String consulta = "SELECT count(1) as cuenta FROM archivo_plano_colsanitas WHERE convenio = "+campos.get("codigoConvenio")+" AND institucion = "+campos.get("codigoInstitucion");
			
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en esConvenioColsanitas: "+e);
			return false;
		}
	}

	
	
	/**
	 * Metodo encargado de consultar todos los tipos 
	 * de liquidacion, pudiendolos filtrar por sus diferentes
	 * acronimos:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimos --> ejemplo, este es el valor de esta variable  'U','V','P',
	 * @param connection
	 * @param acronimos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposLiquidacion(Connection connection,String acronimos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT acronimo AS acronimo," +
				"nombre AS nombre," +
				"codigo AS codigo " +
				"FROM  tipos_liquidacion_soat ",
				where="WHERE 1=1";
		
		if (UtilidadCadena.noEsVacio(acronimos))
			where+=" AND acronimo IN ("+acronimos+")";
		    
		cadena +=where+" ORDER BY nombre DESC";
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("acronimo",rs.getObject("acronimo"));
				elemento.put("nombre",rs.getObject("nombre"));
				elemento.put("codigo",rs.getObject("codigo"));
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtenerTiposLiquidacion: "+e);
		}
		      
		return resultados;
		
	}
	
	/**
	 * Método para consultar el código del grupo del servicio de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int consultarGrupoServicio(Connection con,int codigoServicio)
	{
		int codigoGrupoServicio = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "select grupo_servicio AS grupoServicio from servicios where codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoServicio);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigoGrupoServicio = rs.getInt("grupoServicio");
		}
		catch(SQLException e)
		{
			logger.error("Método para obtener el grupo de servicio de un servicio: "+e);
		}
		
		return codigoGrupoServicio;
	}
	
	/**
	 * Método implementado para consultar los centros de costo de un grupo de servicio por el centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarCentrosCostoGrupoServicio(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String consulta = "";
		String consultaInterno="";
		try
		{
			if((campos.get("interno")+"").equals(ConstantesBD.valorTrueLargoEnString))
			{
				consultaInterno=" AND cc.tipo_entidad_ejecuta='"+ConstantesIntegridadDominio.acronimoInterna+"' ";
			}
				
			if((campos.get("todos")+"").equals(ConstantesBD.valorTrueLargoEnString))
			{
				consulta = "SELECT " +
				"ccgs.centro_costo AS codigoCentroCosto, " +
				"getnomcentrocosto(ccgs.centro_costo) AS nombreCentroCosto," +
				"ca.consecutivo AS consecutivoCentroAtencion," +
				"ca.codigo AS codigoCentroAtencion," +
				"ca.descripcion AS nombreCentroAtencion " +
				"FROM centro_costo_grupo_ser ccgs " +
				"INNER JOIN centros_costo cc ON(cc.codigo=ccgs.centro_costo) " +
				"INNER JOIN centro_atencion ca ON(ca.consecutivo=cc.centro_atencion) " +
				"WHERE ccgs.grupo_servicio = ? ";	
				if((campos.get("interno")+"").equals(ConstantesBD.valorTrueLargoEnString))
					consulta+=consultaInterno;
			}
			else
			{
				consulta = "SELECT " +
				"ccgs.centro_costo AS codigoCentroCosto, " +
				"getnomcentrocosto(ccgs.centro_costo) AS nombreCentroCosto," +
				"ca.consecutivo AS consecutivoCentroAtencion," +
				"ca.codigo AS codigoCentroAtencion," +
				"ca.descripcion AS nombreCentroAtencion " +
				"FROM centro_costo_grupo_ser ccgs " +
				"INNER JOIN centros_costo cc ON(cc.codigo=ccgs.centro_costo) " +
				"INNER JOIN centro_atencion ca ON(ca.consecutivo=cc.centro_atencion) " +
				"WHERE ccgs.grupo_servicio = ? and ccgs.centro_atencion = ? ";
				if((campos.get("interno")+"").equals(ConstantesBD.valorTrueLargoEnString))
					consulta+=consultaInterno;
			}
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoGrupoServicio").toString()));
			if((campos.get("todos")+"").equals(ConstantesBD.valorFalseLargoString))
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoCentroCosto", rs.getInt("codigoCentroCosto")+"");
				elemento.put("nombreCentroCosto", rs.getString("nombreCentroCosto"));
				elemento.put("consecutivoCentroAtencion", rs.getString("consecutivoCentroAtencion"));
				elemento.put("codigoCentroAtencion", rs.getString("codigoCentroAtencion"));
				elemento.put("nombreCentroAtencion", rs.getString("nombreCentroAtencion"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCentrosCostoGrupoServicio: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para obtener los pooles vigentes de un profesional
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPoolesProfesional(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT DISTINCT "+
				"pp.pool AS codigo_pool, "+
				"p.descripcion  AS nombre_pool "+ 
				"FROM participaciones_pooles pp " +
				"inner join pooles p on (p.codigo=pp.pool) " +
				"WHERE " +
				"pp.medico= "+campos.get("codigoProfesional")+" AND " +
				"(" +
				"	pp.fecha_ingreso < '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())+"' OR " +
					"(pp.fecha_ingreso = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())+"' AND pp.hora_ingreso <= "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" )" +
				") AND " +
				"(" +
					"pp.fecha_retiro > '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())+"' OR " +
					"pp.fecha_retiro is NULL OR " +
					"(pp.fecha_retiro = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())+"' AND (pp.hora_retiro >= "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" or pp.hora_retiro is null))" +
				")" +
				" ";
			if(campos.containsKey("todosPooles"))
			{
				if(!UtilidadTexto.getBoolean(campos.get("todosPooles")+""))
				{
					consulta=consulta+" and p.activo=1";
				}
			}
			
			
			logger.info("CONSULTA POOLES PROFESIONAL=> "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_pool"));
				elemento.put("nombre", rs.getObject("nombre_pool"));
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPoolesMedico: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para obtener la descripcion de un tipo de servicio
	 * @param con
	 * @param tipoServicio
	 * @return
	 */
	public static String obtenerNombreTipoServicio(Connection con,String tipoServicio)
	{
		String resultado = "";
		try
		{
			String consulta = "SELECT nombre FROM tipos_servicio WHERE acronimo = '"+tipoServicio+"'";
			Statement st =con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resultado = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoServicio: "+e);
		}
		return resultado;
	}
	
	/**
	 * METODO IMPLEMENTADO PARA CARGAR TODA LA INFORMACION DE LA TABLA TIPO TERCERO
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> tipoTercero(Connection con) 
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT DISTINCT codigo, descripcion from tipo_tercero ";
			
			logger.info("CONSULTA TIPOS TERCEROS => "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("descripcion"));
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener Tipos Terceros: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que verifica que un diagnóstico es requerido
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static boolean esRequeridoDiagnosticoServicio(Connection con,int codigoServicio)
	{
		String resultado = "";
		try
		{
			String consulta = "SELECT coalesce(requiere_diagnostico,'') As requiere FROM servicios WHERE codigo = "+codigoServicio;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resultado = rs.getString("requiere");
		}
		catch(SQLException e)
		{
			logger.error("error en esRequeridoDiagnosticoServicio: "+e);
		}
		return UtilidadTexto.getBoolean(resultado);
	}
	
	/**
	 * Verifica si el convenio requiere justificacion de servicio
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean requiereJustificacioServ(Connection con, int convenio)
	{
		String requiere="";
		boolean resultado = false;
		try
		{
			String consulta = "SELECT requiere_justificacion_serv AS requiere FROM convenios WHERE codigo = "+convenio;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next()){
				requiere = rs.getString("requiere");
				if (requiere.equals(ConstantesBD.acronimoSi))
					resultado=true;
				else
					resultado=false;
			}
			
			rs.close();
			st.close();
				
		}
		catch(SQLException e)
		{
			logger.error(e);
		}
		return resultado;
	}
	
	
	
	/**
	 * Verifica si el convenio requiere justificacion de servicio
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean requiereJustificacioArt(Connection con, int convenio, int art)
	{
		HashMap<String, Object> resultadosNP = new HashMap<String, Object>();
		String requiere="";
		boolean resultado = false;
		String natu="";
		PreparedStatementDecorator ps,ps2;
		ResultSetDecorator rs1;

		/*********************************************************************
		 *  VALIDACION TIPO DE ARTICULO (MEDICAMENTO - INSUMO)
		 *********************************************************************/
		
		try
		{
			
			String consultaStrNaturalezaArt="SELECT na.es_medicamento AS naturalezaa from articulo a INNER JOIN naturaleza_articulo na ON " +
			"(a.naturaleza = na.acronimo AND a.institucion = na.institucion) " +
			"WHERE a.codigo = "+art;
			
			logger.info("SQL / "+consultaStrNaturalezaArt);
			
			ps2= new PreparedStatementDecorator(con.prepareStatement(consultaStrNaturalezaArt,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs1=new ResultSetDecorator(ps2.executeQuery());
			if(rs1.next()){
				natu = rs1.getString("naturalezaa");
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta de la Naturaleza del Articulo");
			return false;
		}
		
		
		/***************************************************
		 *  SI NO ES MEDICAMENTO (INSUMO)
		 ***************************************************/
		
		if(natu.equals("N")){
		
			String consultaStrNP="SELECT c.requiere_justificacion_art as  rja , c.req_just_art_nopos_dif_med as rjadm FROM convenios c " +
											" WHERE " +
												" c.codigo="+convenio;
			logger.info("SQL / "+consultaStrNP);
			
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrNP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultadosNP = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
				
				if((resultadosNP.get("rjadm_0")+"").equals(""+ConstantesBD.acronimoSi))
					resultado=true;
				else
					resultado=false;
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en validacion de articulo NOPOS con requiere jus dif med");
			}
			
		}
		/**************************************************
		 *  SI ES MEDICAMENTO 
		 **************************************************/
		else
		{
			try
			{
				String consulta = "SELECT requiere_justificacion_art AS requiere FROM convenios WHERE codigo = "+convenio;
				logger.info("SQL / "+consulta);
				
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next()){
					requiere = rs.getString("requiere");
					if (requiere.equals(ConstantesBD.acronimoSi))
						resultado=true;
					else
						resultado=false;
				}
					
			}
			catch(SQLException e)
			{
				logger.error(e);
			}
		}
		
		
		return resultado;
	}
	
	
	
	
	
	
	/**
	 * Consulta el nombre del tipo de regimen segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static String consultarNombreTipoRegimen(Connection con, int convenio)
	{
		String resultado = "";
		try
		{
			String consulta = "SELECT tr.nombre AS nombre FROM convenios c INNER JOIN tipos_regimen tr ON (c.tipo_regimen=tr.acronimo) WHERE codigo = "+convenio;
			logger.info("\n\n[consultarNombreTipoRegimen]"+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resultado = rs.getString("nombre");
				
		}
		catch(SQLException e)
		{
			logger.error(e);
		}
		return resultado;
	}
	
	/**
	 * Consulta el nombre del convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static String consultarNombreConvenio(Connection con, int convenio)
	{
		String resultado = "";
		try
		{
			String consulta = "SELECT nombre FROM convenios WHERE codigo = "+convenio;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resultado = rs.getString("nombre");
				
		}
		catch(SQLException e)
		{
			logger.error(e);
		}
		return resultado;
	}
	
	/**
	 * Consulta el codigo del convenio 
	 * @param con
	 * @param codigo factura
	 * @return
	 */
	public static String consultarCodigoConvenio(Connection con, String codigoFactura)
	{
		String resultado = "" ;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try
		{
			StringBuilder consulta = new StringBuilder();
			
			consulta.append("SELECT convenio.codigo ");
			consulta.append("FROM FACTURACION.facturas factura ");
			consulta.append("INNER JOIN FACTURACION.convenios  convenio ");
			consulta.append("ON(factura.convenio=convenio.codigo) ");
			consulta.append("WHERE factura.codigo = ?");
			pst = con.prepareStatement(consulta.toString(), ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setString(1, codigoFactura);
			rs = pst.executeQuery();
			
			
			if(rs.next()){
				
				resultado = rs.getString("codigo");
			}
		}	
		catch(SQLException e)
		{
			logger.error(e);
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
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int ccPlanEspecialConvenio(Connection con, int codigo) 
	{
		String consulta="SELECT centro_costo_plan_especial from convenios where codigo=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("centro_costo_plan_especial");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
		
		
	}

	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Deudor
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String esConvenioExcentoDeudor(Connection con, int codigoConvenio, int codigoInstitucionInt) 
	{
		String consulta="SELECT excento_deudor from convenios where codigo=? and institucion=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ps.setInt(2, codigoInstitucionInt);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("excento_deudor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Documento de Garantia
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String esConvenioExcentoDocGarantia(Connection con, int codigoConvenio, int codigoInstitucionInt) 
	{
		String consulta="SELECT excento_doc_garantia from convenios where codigo=? and institucion=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ps.setInt(2, codigoInstitucionInt);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("excento_doc_garantia");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Utilidad para generar el Log de Facturas Varias entre dos sistemas
	 * @param con
	 * @param loginUsuario
	 * @param resultado
	 */
	public static void insertarLogAdconsta(Connection con, String loginUsuario, String resultado, String insertarLogAdConsulta) 
	{
		PreparedStatement pst=null;
		try
		{
			pst = con.prepareStatement(insertarLogAdConsulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(resultado));
			pst.setDouble(2, Utilidades.convertirADouble(resultado)+1);
			pst.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			pst.setString(4, UtilidadFecha.getHoraSegundosActual());
			pst.setString(5, loginUsuario);
			pst.setString(6, ConstantesBD.acronimoSi);
			
			pst.executeUpdate();
						
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertarLogAdconsta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarLogAdconsta", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene Activado el Sin Contrato
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static String esSinContrato(Connection con, int codigoContrato) 
	{
		String consulta="SELECT sin_contrato from contratos where codigo=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoContrato);
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("sin_contrato");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene Activado el Controla Anticipos
	 * @param con
	 * @param contrato
	 * @return
	 */
	public static String esControlaAnticipos(Connection con, int contrato) 
	{
		String consulta="SELECT controla_anticipos from contratos where codigo=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, contrato);
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("controla_anticipos");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Método que verifica si una solicitud está totalmente pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudTotalPendiente(Connection con,String numeroSolicitud)
	{
		boolean esPendiente = false;
		try
		{
			String consulta = "select " +
				"case when count(1) > 0 then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as resultado  " +
				"from det_cargos " +
				"where solicitud = "+numeroSolicitud+" and eliminado='"+ConstantesBD.acronimoNo+"' and estado<>"+ConstantesBD.codigoEstadoFPendiente;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			logger.info("-->"+consulta);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				esPendiente = UtilidadTexto.getBoolean(rs.getString("resultado"));
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudTotalPendiente: "+e);
		}
		return esPendiente;
	}
	
	/**
	 * Método que retorna si un servicio es nopos
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public static boolean esServicioPos(Connection con, int servicio){
		boolean esPos = false;
		try
		{
			String consulta = "SELECT espos FROM servicios WHERE codigo="+servicio;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				esPos = UtilidadTexto.getBoolean(rs.getString("espos"));
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudTotalPendiente: "+e);
		}
		return esPos;
	}
	
	/**
	 * Método implementado para obtener los tipos de recargo
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposRecargo(Connection con)
	{
		ArrayList<HashMap<String, Object>> tiposRecargo = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo,nombre FROM tipos_recargo";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				
				tiposRecargo.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposRecargo: "+e);
		}
		
		return tiposRecargo;
	}
	
	/**
	 * Método para obtener la especialidad del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static InfoDatosInt obtenerEspecialidadServicio(Connection con,int codigoServicio)
	{
		InfoDatosInt especialidad = new InfoDatosInt();
		try
		{
			String consulta = "SELECT " +
				"especialidad as codigo_especialidad," +
				"getnombreespecialidad(especialidad) as nombre_especialidad " +
				"FROM servicios WHERE codigo = "+codigoServicio;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				especialidad.setCodigo(rs.getInt("codigo_especialidad"));
				especialidad.setNombre(rs.getString("nombre_especialidad"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEspecialidadServicio: "+e);
		}
		
		return especialidad;
	}

	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public static int obtenerContratoSubCuenta(Connection con, String idSubCuenta) 
	{
		try
		{
			String consulta = "SELECT contrato from sub_cuentas where sub_cuenta = "+Utilidades.convertirALong(idSubCuenta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				return rs.getInt(1);
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudTotalPendiente: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Metodo que verifica que todos los cargos de las solicitudes asociadas estan en estado excento o anulado 
	 * retorna true si puede cerrar el ingreso false si no.
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean puedoCerrarIngreso(Connection con, int ingreso)
	{
	boolean cierroIngreso=false;
	Vector<String> cuentas= new Vector<String>();
	cuentas=Cuenta.obtenerCuentasIngreso(con, ingreso,false);
	
		try
		{
			String consulta = "SELECT count(*) as cuantos " +
										" FROM " +
											" solicitudes sol " +
										" INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
										" INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) "+
										" WHERE " +
											" sol.cuenta in (" +UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
											" AND dc.estado not in ("+ConstantesBD.codigoEstadoFAnulada+", "+ConstantesBD.codigoEstadoFExento+", "+ConstantesBD.codigoEstadoFInactiva+") ";
										
			logger.info("[puedoCerrarIngreso]"+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if (rs.getInt(1)==0)
					{
					cierroIngreso=true;
					//Otras solicitudes 
					String cadena="SELECT " +
										"count(*) as cuantos " +
									"FROM " +
										"solicitudes s " +
									"WHERE (SELECT count(*) FROM det_cargos WHERE solicitud=s.numero_solicitud)=0 " +
											"AND s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"" +
											"AND s.cuenta IN ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+")";
					
					logger.info("[puedoCerrarIngreso 2]"+cadena);
					Statement st1 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					ResultSetDecorator rs1 = new ResultSetDecorator(st1.executeQuery(cadena));
					if(rs1.next())
						if (rs1.getInt(1)>0)
							cierroIngreso=false;
						else
							cierroIngreso=true;
					}
				else
					cierroIngreso=false;
		}
		catch(SQLException e)
		{
			logger.error("[ERROR puedoCerrarIngreso] "+e);
		}
		return cierroIngreso;
	}
	
	
	
	
	/**
	 * Método que retorna si hay ventas por facturar por centro de costo
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public static boolean hayVentasXFacturar(Connection con, String anoCorte, String mesCorte, String centroAtencion, String centroCosto)
	{
		String consulta = 	"SELECT " +
								"SUM(cl.valor_total_cargado) AS total, " +
								"getnomcentroatencion(cl.centro_atencion) AS centroAtencion, " +
								"cl.centro_costo_solicitante AS codigoCentroCosto, " +
								"cc.nombre AS centroCosto, " +
								"cc.codigo_interfaz AS interfaz " +
							"FROM " +
								"consumos_liquidados cl " +
							"INNER JOIN  " +
								"centros_costo cc ON (cc.codigo=cl.centro_costo_solicitante) " +
							"WHERE " +
								"cl.tipo_solicitud != "+ConstantesBD.codigoTipoSolicitudPaquetes+" " +
								"AND to_char(cl.fecha_corte, 'YYYY/MM')='"+anoCorte+"/"+mesCorte+"' ";
		if (!centroAtencion.equals(""))
			   consulta += "AND cl.centro_atencion="+centroAtencion+" ";
		if (!centroCosto.toString().equals("")) 
	    	   consulta += "AND ((cl.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND cl.centro_costo_solicitado = "+centroCosto+") OR (cl.tipo_solicitud in ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND cl.centro_costo_principal = "+centroCosto+"))";
		
		consulta += " GROUP BY " +
						"centroAtencion, " +
						"codigoCentroCosto, " +
						"centroCosto, " +
						"interfaz";
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en hayVentasXFacturar: "+e);
		}
		logger.info("hayVentasXFacturar - "+consulta);
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public static String obtenerFechaFacturaResponsable(Connection con, String idSubCuenta) 
	{
		String cadena="SELECT codigo,to_char(fecha,'dd/mm/yyyy') as fecha from facturas where estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" and sub_cuenta = "+Utilidades.convertirAEntero(idSubCuenta)+" order by codigo desc";
		String resultado="";
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			if(rs.next())
			{
				resultado=rs.getString("fecha");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaFacturaResponsable: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para obtener las facturas asociadas a la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerConsecutivoFacturaXSolicitud(Connection con,HashMap campos)
	{
		String consecutivo = "";
		try
		{
			//**************SE TOMAN LOS CAMPOS********************************************
			String idSubCuenta = campos.get("idSubCuenta").toString();
			String numeroSolicitud = campos.get("numeroSolicitud").toString();
			//*******************************************************************************
			
			String consulta = "select " +
				"f.consecutivo_factura as factura " +
				"from facturas f " +
				"inner join det_factura_solicitud dfs on (dfs.factura = f.codigo) " +
				"WHERE " +
				"f.sub_cuenta = "+Utilidades.convertirALong(idSubCuenta)+" and f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" and dfs.solicitud = "+numeroSolicitud;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
				consecutivo += (consecutivo.equals("")?"":", ") + rs.getString("factura");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConsecutivoFacturaXSolicitud: "+e);
		}
		return consecutivo;
	}
	
	/**
	 * Método implementado para obtener las empresas parametrizadas en el sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con)
	{
		ArrayList<HashMap<String, Object>> empresas = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo,razon_social FROM empresas";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("razon_social",rs.getObject("razon_social"));
				
				empresas.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEmpresas: "+e);
		}
		
		return empresas;
	}
	
	/**
	 * Método implementado para obtener la descripción de un requisito de paciente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerDescripcionRequisitoPaciente(Connection con,int codigo)
	{
		String descripcion = "";
		try
		{
			String consulta = "SELECT descripcion FROM requisitos_paciente where codigo = "+codigo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				descripcion = rs.getString("descripcion");
		}
		catch(SQLException e)
		{
			logger.error("Error en "+e);
		}
		return descripcion;
	}
	
	/**
	 * Método para obtener el nit de la empresa del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNitEmpresaConvenio(Connection con,int codigoConvenio)
	{
		String nit = "";
		try
		{
			String consulta = "SELECT t.numero_identificacion AS nit " +
				"from convenios c " +
				"inner join empresas e ON(e.codigo = c.empresa) " +
				"inner join terceros t on (t.codigo = e.tercero) " +
				"WHERE c.codigo = "+codigoConvenio;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				nit = rs.getString("nit");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNitEmpresaConvenio: "+e);
		}
		return nit;
	}

	/**
	 * Método que retorna si el rango de fechas ya existe en la
	 * parametrización de salarios mínimos
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static boolean existeFechasEnSalarioMinimo(Connection con, String fechaInicial, String fechaFinal, Integer idRegistroParaNoTenerEnCuenta)
	{
		String consulta = "SELECT count(1) FROM salario_minimo WHERE (fecha_inicial BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' " +
																 "OR fecha_final BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
		
		if(idRegistroParaNoTenerEnCuenta != null){
			consulta += " AND codigo != " + idRegistroParaNoTenerEnCuenta;
		}
		
		try
		{
			logger.info("===>Consulta Salario Minimo: "+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt(1) > 0)
					return true;
				else
					return false;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeFechasEnSalarioMinimo: "+e);
		}
		return false;
	}
	
	/**
	 * Método para obtener los estados de paciente que puede tener una factura
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstadosPacienteFactura(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "select codigo,nombre from estados_factura_paciente";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstadosPacienteFactura: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método encargado de obtener los convenios por usuario
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConvenioPorUsuario (Connection con, 
			String loginUsuario, String tipoUsuario, boolean activo)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = 
		"SELECT " +
			"c.codigo as codigo_convenio, " +
			"c.nombre as nombre_convenio, " +
			"c.tipo_contrato as tipo_contrato " +
		"FROM usuarios_glosas_conv ugc " +
		"INNER JOIN convenios c ON(c.codigo=ugc.convenio) " +
		"WHERE ugc.usuario = '"+loginUsuario+"' and ugc.tipo_usuario = '"+tipoUsuario+"' ";
		if(activo)
		{
			cadena += "AND ugc.activo = '"+ConstantesBD.acronimoSi+"' ";
		}
		logger.info("consulta usuarios convenio: "+cadena);
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("codigoConvenio", rs.getString("codigo_convenio"));
				mapa.put("nombreConvenio", rs.getString("nombre_convenio"));
				mapa.put("codigoTipoContrato", rs.getString("tipo_contrato"));
				resultados.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("===> Error consultando los convenios por usuario: "+e);
		}
		
		return resultados;
	}
	
	/**
	 * Método para retornar el nombre del tarifario oficial
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreTarifarioOficial(Connection con,int codigo)
	{
		String nombre = "";
		try
		{
			String consulta = "SELECT nombre FROM tarifarios_oficiales WHERE codigo = "+codigo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				nombre = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en "+e);
		}
		return nombre;
	}
	
	/**
	 * 
	 * @param con
	 * @param valorConsecutivo
	 * @param institucion
	 * @return
	 */
	public static boolean esConsecutvioAsignadoFactura(Connection con,String valorConsecutivo, int institucion) 
	{
		String consulta = "SELECT count(1) from facturas where consecutivo_factura =? and institucion=?";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1, valorConsecutivo);
			pst.setInt(2, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt(1) > 0)
				{
					pst.close();
					rs.close();
					return true;
				}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
		logger.error("Error en esConsecutvioAsignadoFactura: "+e);
		}
		return false;
	}
	
	/**
	 * Método para verificar si un convenio es de reporte inicial de urgencias
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioReporteAtencionInicialUrgencias(Connection con,int codigoConvenio)
	{
		boolean indicador = false;
		try
		{
			String consulta = "SELECT coalesce(reporte_atencion_ini_urg,'"+ConstantesBD.acronimoNo+"') as indicador from convenios where codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				indicador = UtilidadTexto.getBoolean(rs.getString("indicador"));
			}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en esConvenioReporteAtencionInicialUrgencias: "+e);
		}
		return indicador;
	}
	
	/**
	 * Método para verificar si un convenio es de Reporte Inconsistencias en Base de Datos
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioReporteInconsistenciasenBD(Connection con,int codigoConvenio)
	{
		boolean indicador = false;
		try
		{
			String consulta = "SELECT coalesce(reporte_incon_bd,'"+ConstantesBD.acronimoNo+"') as indicador from convenios where codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				indicador = UtilidadTexto.getBoolean(rs.getString("indicador"));
			}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en ReporteInconsistenciasenBD: "+e);
		}
		return indicador;
	}
	
	/**
	 * Método para obtener la entidad subcontrata por el codigo pk
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public static DtoEntidadSubcontratada obtenerEntidadSubcontratada(Connection con,String consecutivoEntidad)
	{
		DtoEntidadSubcontratada entidad = new DtoEntidadSubcontratada();
		try
		{
			String consulta = " SELECT codigo,razon_social,codigo_minsalud from entidades_subcontratadas where codigo_pk = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(consecutivoEntidad));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				entidad.setConsecutivo(consecutivoEntidad);
				entidad.setCodigo(rs.getString("codigo"));
				entidad.setRazonSocial(rs.getString("razon_social"));
				entidad.setCodigoMinsalud(rs.getString("codigo_minsalud"));
			}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEntidadSubcontratada: "+e);
		}
		return entidad;
	}

	/**
	 * Metodo para obtener los contratos parametrizados para una entidad subcontratada segun su codigo
	 * @param entidadSubcontratada
	 * @return
	 */
	public static HashMap obtenerContratosPorEntidadSubcontratada(String entidadSubcontratada)
	{
		HashMap contratos= new HashMap();
		
		Connection con;
		con= UtilidadBD.abrirConexion();
		
		String cadena="SELECT " +
		" consecutivo as consecutivo," +
		" entidad_subcontratada as entidadsubcontratada," +
		" numero_contrato as numerocontrato," +
		" valor_contrato as valorcontrato," +
		" fecha_inicial as fechainicial," +
		" fecha_final as fechafinal " +
		" FROM facturacion.contratos_entidades_sub " +
		" WHERE entidad_subcontratada=? ";
		
		contratos.put("numRegistros", "0");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, Utilidades.convertirAEntero(entidadSubcontratada));
			
			contratos=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return contratos;
	}

	
	/**
	 * Metodo para determinar si un Usuario tiene permisos para Responder Consultas para una Entidad Subcontratada
	 * @param entidadSubcontratada
	 * @param usuario
	 * @return
	 */
	public static boolean esUsuarioconPermisoResponderConsultasEntSubcont(String entidadSubcontratada, String usuario) {
		
		Connection con;
		con= UtilidadBD.abrirConexion();
		
		String cadena="SELECT " +
		"usenti.entidad_subcontratada AS entidad " +
		"FROM facturacion.usuarios_entidad_sub usenti " +
		"INNER JOIN entidades_subcontratadas ent ON (usenti.entidad_subcontratada = ent.codigo_pk  AND ent.activo = '"+ConstantesBD.acronimoSi+"')" +
		"WHERE entidad_subcontratada = ?  AND usuario = ? ";
	
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			ps.setInt(1, Utilidades.convertirAEntero(entidadSubcontratada));
			ps.setString(2,usuario);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			  {
				rs.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			  }
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("Error en Validar Permisos Usuario para Responder Consultas Entidad Subcontratada: "+e);
		}
		UtilidadBD.closeConnection(con);
		return false;
	  }

	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 * @param convenio
	 * @return
	 */
	public static boolean esUsuarioGlosaConvenio(Connection con,
			String loginUsuario, String tipoUsuario, boolean activo,
			int convenio) 
	{
		boolean resultado=false;
		String cadena = "SELECT " +
							"codigo " +
						"FROM " +
							"usuarios_glosas_conv " +
						"WHERE " +
							"usuario = '"+loginUsuario+"' " +
							"and tipo_usuario = '"+tipoUsuario+"' " +
							"and convenio= "+convenio+" ";
		if(activo)
		{
			cadena += "AND activo = '"+ConstantesBD.acronimoSi+"' ";
		}
		logger.info("consulta usuarios convenio: "+cadena);
		try
		{
			PreparedStatementDecorator st = new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			if (rs.next())
			{
				resultado=true;
			}
			st.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("===> Error consultando los convenios por usuario: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para consultar la cuenta contable de un tipo especifico del tipo convenio
	 * @param con
	 * @param codigo
	 * @param codigoInstitucion
	 * @param cuentaConvenio
	 * @param cuentaValorConvenio
	 * @param cuentaValorPaciente
	 * @param cuentaDescuentoPaciente
	 * @param cuentaCxCCapitacion
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableTipoConvenio(Connection con,String codigo,int codigoInstitucion,boolean cuentaConvenio,boolean cuentaValorConvenio,boolean cuentaValorPaciente,boolean cuentaDescuentoPaciente,boolean cuentaCxCCapitacion)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		try
		{
			String cuentaFiltro = "cuenta_contable";
			
			if(cuentaConvenio)
			{
				cuentaFiltro = "cuenta_contable";
			}
			else if(cuentaValorConvenio)
			{
				cuentaFiltro = "cuenta_contable_val_conv";
			}
			else if(cuentaValorPaciente)
			{
				cuentaFiltro = "cuenta_contable_val_pac";
			}
			else if(cuentaDescuentoPaciente)
			{
				cuentaFiltro = "cuenta_contable_des_pac";
			}
			else if(cuentaCxCCapitacion)
			{
				cuentaFiltro = "cuenta_contable_cxc_cap";
			}
			String consulta = "SELECT "+ 
				"cc.codigo as codigo, "+
				"cc.cuenta_contable as cuenta_contable, "+
				"cc.descripcion as descripcion, "+
				"cc.activo as activo, "+
				"cc.manejo_terceros as manejo_terceros, "+
				"cc.manejo_centros_costo as manejo_centros_costo, "+
				"cc.manejo_base_gravable as manejo_base_gravable, "+
				"cc.naturaleza_cuenta as naturaleza_cuenta, "+
				"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
				"cc.anio_vigencia "+ 
				"FROM tipos_convenio tc "+ 
				"INNER JOIN cuentas_contables cc ON(cc.codigo = tc."+cuentaFiltro+") "+ 
				"WHERE "+ 
				"tc.codigo = ? and cc.institucion = ?"; 
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigo);
			pst.setInt(2,codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta.setCodigo(rs.getString("codigo"));
				cuenta.setCuentaContable(rs.getString("cuenta_contable"));
				cuenta.setDescripcion(rs.getString("descripcion"));
				cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuenta.setCodigoInstitucion(rs.getInt("institucion"));
				cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContableTipoConvenio: "+e);
		}
		return cuenta;
	}
	
	/**
	 * Método para consultar la cuenta contable de la apramtrizacion de participacion pool tarifas x convenio
	 * @param con
	 * @param codigoPool
	 * @param codigoConvenio
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @param cuentaPool
	 * @param cuentaInstitucion
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableParticipacionPoolTarifasConvenio(Connection con,int codigoPool,int codigoConvenio,int codigoEsquemaTarifario,int codigoInstitucion,boolean cuentaPool,boolean cuentaInstitucion,boolean cuentaVigenciaAnterior)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		try
		{
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			String cuentaFiltro = "";
			String consulta = "";
			
			if(cuentaPool)
			{
				cuentaFiltro = "cuenta_contable_pool";
			}
			else if(cuentaInstitucion)
			{
				cuentaFiltro = "cuenta_contable_ins";
			}
			else if (cuentaVigenciaAnterior)
			{
				cuentaFiltro = "cue_cont_inst_vig_anterior";
			}
			
			//********************SE CONSULTA LA CUENTA X CONVENIO****************************
			consulta = "SELECT  "+
			"cc.codigo as codigo, "+ 
			"cc.cuenta_contable as cuenta_contable, "+ 
			"cc.descripcion as descripcion, "+ 
			"cc.activo as activo, "+ 
			"cc.manejo_terceros as manejo_terceros, "+ 
			"cc.manejo_centros_costo as manejo_centros_costo, "+ 
			"cc.manejo_base_gravable as manejo_base_gravable, "+ 
			"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
			"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, " +
			"cc.anio_vigencia "+  
			"FROM pooles_convenio pc "+
			"INNER JOIN cuentas_contables cc ON(cc.codigo = pc."+cuentaFiltro+") "+  
			"WHERE  pc.pool = ? and pc.convenio = ? and pc.institucion = ?";
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPool);
			pst.setInt(2,codigoConvenio);
			pst.setInt(3,codigoInstitucion);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				cuenta.setCodigo(rs.getString("codigo"));
				cuenta.setCuentaContable(rs.getString("cuenta_contable"));
				cuenta.setDescripcion(rs.getString("descripcion"));
				cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuenta.setCodigoInstitucion(rs.getInt("institucion"));
				cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			pst.close();
			rs.close();
			//**********************************************************************************
			//************SE CONSULTA LA CUENTA X ESQUEMA TARIFARIO*******************************
			//Solo se consulta si no se ha encotnrado nada
			if(cuenta.getCuentaContable().equals(""))
			{
				consulta = "SELECT  "+
					"cc.codigo as codigo, "+ 
					"cc.cuenta_contable as cuenta_contable, "+ 
					"cc.descripcion as descripcion, "+ 
					"cc.activo as activo, "+ 
					"cc.manejo_terceros as manejo_terceros, "+ 
					"cc.manejo_centros_costo as manejo_centros_costo, "+ 
					"cc.manejo_base_gravable as manejo_base_gravable, "+ 
					"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
					"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
					"cc.anio_vigencia "+  
					"FROM pooles_esquema_tarifario pe "+
					"INNER JOIN cuentas_contables cc ON(cc.codigo = pe.cuenta_contable_pool) "+  
					"WHERE  pe.pool = ? and pe.esquema_tarifario = ? and pe.institucion = ?";
				
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoPool);
				pst.setInt(2,codigoEsquemaTarifario);
				pst.setInt(3,codigoInstitucion);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setDescripcion(rs.getString("descripcion"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//*************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContableParticipacionPoolTarifasConvenio: "+e);
		}
		return cuenta;
	}
	
	/**
	 * Método para consultar la cuenta contable del paquete de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param cuentaMayorValor
	 * @param cuentaMenorValor
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContablePaquetexSolicitud(Connection con,String numeroSolicitud,boolean cuentaMayorValor,boolean cuentaMenorValor)
	{
		DtoCuentaContable cuentaContable = new DtoCuentaContable();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			String cuentaFiltro = "";
			if(cuentaMayorValor)
			{
				cuentaFiltro = "cuenta_cont_may_val";
			}
			else if(cuentaMenorValor)
			{
				cuentaFiltro = "cuenta_cont_men_val";
			}
			
			
			String consulta = "SELECT "+ 
				"cc.codigo as codigo, "+ 
				"cc.cuenta_contable as cuenta_contable, "+ 
				"cc.descripcion as descripcion, "+ 
				"cc.activo as activo, "+ 
				"cc.manejo_terceros as manejo_terceros, "+ 
				"cc.manejo_centros_costo as manejo_centros_costo,"+ 
				"cc.manejo_base_gravable as manejo_base_gravable, "+ 
				"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
				"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
				"cc.anio_vigencia "+  
				"FROM paquetizacion pa "+ 
				"INNER JOIN paquetes_convenio pc ON(pc.codigo = pa.codigo_paquete_convenio) "+ 
				"INNER JOIN paquetes p ON(p.codigo_paquete = pc.paquete and p.institucion = pc.institucion) "+ 
				"INNER JOIN cuentas_contables cc ON(cc.codigo = p."+cuentaFiltro+") "+  
				"WHERE "+ 
				"pa.numero_solicitud_paquete = ? ";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuentaContable.setCodigo(rs.getString("codigo"));
				cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
				cuentaContable.setDescripcion(rs.getString("descripcion"));
				cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
				cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			pst.close();
			rs.close();
			
			/*
			 * Solución temporal para cuentas contables de paquetes
			 */
			
			if(cuentaContable.getCuentaContable().equals("")) {
				consulta = "SELECT cc.codigo              AS codigo, " +
						"cc.cuenta_contable          AS cuenta_contable, " +
						"cc.descripcion              AS descripcion, " +
						"cc.activo                   AS activo, " +
						"cc.manejo_terceros          AS manejo_terceros, " +
						"cc.manejo_centros_costo     AS manejo_centros_costo, " +
						"cc.manejo_base_gravable     AS manejo_base_gravable, " +
						"cc.naturaleza_cuenta        AS naturaleza_cuenta, " +
						"COALESCE(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
						"cc.anio_vigencia " +
						"FROM det_cargos dc  " +
						"INNER JOIN paquetes p  " +
						"ON (dc.servicio = p.servicio) " +
						"INNER JOIN cuentas_contables cc " +
						"ON(cc.codigo = p."+cuentaFiltro+") " +
						"WHERE dc.solicitud = ? AND ROWNUM = 1";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setLong(1,Long.parseLong(numeroSolicitud));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuentaContable.setCodigo(rs.getString("codigo"));
						cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
						cuentaContable.setDescripcion(rs.getString("descripcion"));
						cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
						cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContablePaquetexSolicitud: "+e);
		}
		return cuentaContable;
	}


	
	/**
	 * Retorna la un Array list de Deudores 
	 * @param dto recibe un dto de tipo Deuodores
	 * @return
	 */
	public static ArrayList<DtoDeudor> obtenerDeudores (DtoDeudor dto ){
		
		logger.info("deudores");
		
		
		//ojo falta eliminar los dto Innecesarios 
	 	ArrayList<DtoDeudor> arrayDto= new ArrayList<DtoDeudor>();
	
	   String consultaStr= " select codigo_tercero  as codigoTercero,   "+ 
	 	" activo as activo, "+
	 	" direccion as direccion , "+   
	 	" telefono  as telefefono,  "+
	 	" e_mail    as email ,  "+
	 	" representante_legal as representanteLegal,"+   
	 	" nombre_contacto as nombreContacto,"+      
	 	" observaciones as observaciones,"+        
	 	" institucion as institucion,"+        
	 	" fecha_modifica as fechaModificacion,"+      
	 	" hora_modifica as horaModifica,"+        
	 	" usuario_modifica   as usuarioModifica   ,"+
	 	" codigo as codigo,"+               
	 	" codigo_paciente as codigoPaciente ,"+    
	 	" codigo_empresa as codigoEmpresa ,"+      
	 	" tipo   as tipo  ,"+           
	 	" dias_vencimiento_fac as diasVencimientoFac,"+  
	 	" tipo_identificacion  as tipoIdentificacion ,"+  
	 	" numero_identificacion as numeroIdentificacion, "+
	 	" primer_apellido as primerApellido ,"+      
	 	" segundo_apellido as segundoApellido,"+      
	 	" primer_nombre as primerNombre,"+        
	 	" segundo_nombre as segundoNombre "+
	 	" from "+
	 	" facturasvarias.deudores "+
	 	" where 1=1  ";
	 	
	 
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoTercero())?"":" AND codigo_tercero='"+dto.getCodigoTercero()+"'";
	 	
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getDireccion())?"":" AND direccion='"+dto.getDireccion()+"'";
	 	
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getTelefono())?"":" AND telefefono='"+dto.getTelefono()+"'";
	 	
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getEmail())?"":" AND e_mail='"+dto.getEmail()+"'";
		
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getNombreRepresentante())?"":" AND representante_legal'="+dto.getNombreRepresentante()+"'";
		 
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getNombreContacto())?"":" AND nombre_contacto='"+dto.getNombreContacto()+"'";
	 	
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getObservaciones())?"":" AND observaciones='"+dto.getObservaciones()+"'";
	 	
	 	consultaStr+=(dto.getCodigoInstitucion()>0)?" AND institucion="+dto.getCodigoInstitucion()+"":"";
	 	 
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getCodigo())?"":" AND codigo='"+dto.getCodigo()+"'";
	 	
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoPaciente())?"":" AND codigo_paciente='"+dto.getCodigoPaciente()+"'";
		
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoEmpresa())?"":" AND codigo_empresa='"+dto.getCodigoEmpresa()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getTipoDeudor())?"":" AND tipo='"+dto.getTipoDeudor()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getDiasVencimientoFac())?"":" AND dias_vencimiento_fac='"+dto.getDiasVencimientoFac()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getTipoIdentificacion())?"":" AND tipo_identificacion='"+dto.getTipoIdentificacion()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getNumeroIdentificacion())?"":" AND numero_identificacion='"+dto.getNumeroIdentificacion()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerApellido())?"":" AND primer_apellido='"+dto.getCodigoEmpresa()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getSegundoApellido())?"":" AND segundo_apellido='"+dto.getCodigoEmpresa()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerNombre())?"":" AND primer_nombre='"+dto.getPrimerNombre()+"'";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getSegundoNombre())?"":" AND segundo_nombre='"+dto.getSegundoNombre()+"'";
		
		logger.info("log"+consultaStr);
	 	
	 	    
	    logger.info("\n\n\n\n\n SQL Deudores / "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_tercero ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoDeudor newdto = new DtoDeudor();
				newdto.setCodigo(rs.getString("codigo"));
				newdto.setCodigoTercero(rs.getString("codigoTercero"));
				newdto.setDireccion(rs.getString("direccion"));
				newdto.setTelefono(rs.getString("telefefono"));
				newdto.setEmail(rs.getString("email"));
				newdto.setPrimerApellido(rs.getString("primerApellido"));
			 	newdto.setSegundoApellido(rs.getString("segundoApellido"));
			 	newdto.setPrimerNombre(rs.getString("primerNombre"));
			 	newdto.setSegundoNombre(rs.getString("segundoNombre"));
			 	
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;

		
	}
	

	/**
	 * 
	 * @param consecutivoFac
	 * @param institucion
	 * @return
	 */
	public static String obtenerPrefijoFacturaXConsecutivo(String consecutivoFac, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String prefijo="";
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement("select pref_factura from facturas where consecutivo_factura=? and institucion=?"));
			ps.setString(1, consecutivoFac);
			ps.setInt(2, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				prefijo=rs.getString(1);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error",e);
		}
		
		UtilidadBD.closeConnection(con);
		return prefijo;
	}
	

	/**
	 * Método para consultar el valor del anticipo disponible
	 * @param con
	 * @param codigoContrato
	 * @return valor
	 */
	public static double consultarValorAnticipoDisponible(Connection con, int codigoContrato)
	{
		try{
			String sentencia=
							"SELECT " +
								"(coalesce(valor_ant_rec_convenio,0) - coalesce(valor_ant_res_pre_cont_pac,0)) AS valor_antdisponible " +
							"FROM " +
								"facturacion.control_anticipos_contrato " +
							"WHERE " +
								"contrato=?";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, codigoContrato);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			double valor=-1;
			if(rsd.next())
			{
				valor=rsd.getDouble("valor_antdisponible");
			}
			rsd.close();
			psd.close();
			return valor;
		}catch (SQLException e)
		{
			logger.info("Error consultando el valor del anticipo para el contrato "+codigoContrato, e);
			return -1;
		}
	}
	
	/**
	 * Método para verificar si una solicitud ya fue facturada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudFacturada(Connection con,BigDecimal numeroSolicitud)
	{
		boolean esFacturada = false;
		try
		{
			String consulta = "select " +
				"case when count(1) > 0 then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as resultado  " +
				"from det_factura_solicitud dfs " +
				"INNER JOIN facturas f ON(f.codigo = dfs.factura) " +
				"where dfs.solicitud = "+numeroSolicitud+" and f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			logger.info("-->"+consulta);
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				esFacturada = UtilidadTexto.getBoolean(rs.getString("resultado"));
			
			rs.close();
			st.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en esSolicitudFacturada: "+e);
		}
		return esFacturada;
	}
	
	/**
	 * Método implementado para actualizar el pool en la factura de la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean actualizarPoolFacturaSolicitud(Connection con,HashMap campos)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		double porcentajeMedico =0 ;
		double porcentajePool = ConstantesBD.codigoNuncaValido;
		double valorPool = ConstantesBD.codigoNuncaValido;
		double valorMedico = 0;
		double valorTotal = 0;
		long codigo =ConstantesBD.codigoNuncaValido;
		
		try
		{
			//*******************SE TOMAN LOS PARÁMETROS************************************
			String numeroSolicitud = campos.get("numeroSolicitud").toString();
			int codigoPool = Utilidades.convertirAEntero(campos.get("codigoPool").toString());
			int codigoMedico = Utilidades.convertirAEntero(campos.get("codigoMedico").toString());
			//*******************************************************************************
			
			String consulta = "SELECT "+ 
				"dfs.codigo as codigo, "+
				"dfs.valor_total as valor_total, "+
				"coalesce(getPorcentMedPoolXSol(dfs.solicitud, "+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+"),0) as porcentajemedicopool, " +
				"dc.codigo_detalle_cargo as codigo_detalle_cargo "+ 
				"FROM det_factura_solicitud dfs "+ 
				"INNER JOIN facturas f ON(f.codigo = dfs.factura) "+ 
				"INNER JOIN det_cargos dc ON(dc.solicitud = dfs.solicitud and dc.codigo_factura = dfs.factura) "+ 
				"WHERE "+ 
				"dfs.solicitud = ? and f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada;
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setLong(1,Long.parseLong(numeroSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next() && resultado.isTrue())
			{
				porcentajePool = ConstantesBD.codigoNuncaValido;
				valorPool = ConstantesBD.codigoNuncaValido;
				valorTotal = rs.getDouble("valor_total");
				porcentajeMedico = rs.getDouble("porcentajemedicopool");
				valorMedico = 0;
				codigo = rs.getLong("codigo");
				
				//Se toman los porcentajes
				InfoPorcentajeValor honorario= CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaNoCx(rs.getDouble("codigo_detalle_cargo"));
				if(honorario!=null)
				{
					valorPool=honorario.getValor().doubleValue();
					porcentajePool= honorario.getPorcentaje();
					
					logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					logger.info("EL VALOR DEL POOL SETEADO ES-->"+valorPool);
					logger.info("EL PORCENTAJE DEL POOL SETEADO ES-->"+porcentajePool);
					logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				}
				
				if(porcentajePool>=0)
				{
					valorPool = (valorTotal * porcentajePool) / 100;
				}
				
				//Si el valor del pool es válido se realiza la actualización
				if(valorPool>=0)
				{
					valorMedico = (valorPool * porcentajeMedico) / 100;
					
					consulta = "UPDATE  det_factura_solicitud SET valor_medico =?, valor_pool= ?, porcentaje_pool= ?, porcentaje_medico= ?, pool = ?,codigo_medico = ? WHERE codigo = ?";
					PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con,consulta);
					pst1.setDouble(1,valorMedico);
					pst1.setDouble(2, valorPool);
					if(porcentajePool>=0)
					{
						pst1.setDouble(3, porcentajePool);
					}
					else
					{
						pst1.setNull(3,Types.DOUBLE);
					}
					if(porcentajeMedico>=0)
					{
						pst1.setDouble(4,porcentajeMedico);
					}
					else
					{
						pst1.setNull(4,Types.DOUBLE);
					}
					pst1.setInt(5, codigoPool);
					pst1.setInt(6,codigoMedico);
					pst1.setLong(7,codigo);
					
					if(pst1.executeUpdate()<=0)
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Problemas actualizando los valores del pool en la factura");
					}
					pst1.close();
				}
			}
			
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPoolFacturaSolicitud: ",e);
		}
		return resultado;
	}
 
	/**
	 * Método para cargar los servicios/articulos incluidos de un servicio principal
	 * @param con
	 * @param servicioPrincipal
	 */
	public static void cargarServiciosArticulosIncluidos(Connection con,DtoServiPpalIncluido servicioPrincipal)
	{
		try
		{
			//*******************SE CARGA LA PARTE DE ENCABEZADO*****************************************
			String consulta = "SELECT " +
				"sp.codigo as codigo, " +
				"sp.cod_servi_ppal as codigo_servicio," +
				"getnombreservicio(sp.cod_servi_ppal,?) as nombre_servicio," +
				"sp.activo as activo," +
				"sp.institucion as institucion," +
				"sp.usuario_modifica as usuario_modifica," +
				"to_char(sp.fecha_modifica,'"+ConstantesBD.formatoFechaBD+"') as fecha_modifica," +
				"sp.hora_modifica as hora_modifica " +
				"from facturacion.servi_ppalincluidos sp " +
				"inner join servicios s on (s.codigo = sp.cod_servi_ppal) " +
				"where sp.cod_servi_ppal = ? ";
			if(servicioPrincipal.isActivo())
			{
				consulta += " AND sp.activo = '"+ConstantesBD.acronimoSi+"' ";
			}
			if(servicioPrincipal.isAtencionOdontologica())
			{
				consulta += " AND s.atencion_odontologica = '"+ConstantesBD.acronimoSi+"' ";
			}
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(servicioPrincipal.getInstitucion())));
			pst.setInt(2, servicioPrincipal.getServicio().getCodigo());
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				servicioPrincipal.setCodigo(rs.getBigDecimal("codigo"));
				servicioPrincipal.getServicio().setCodigo(rs.getInt("codigo_servicio"));
				servicioPrincipal.getServicio().setNombre(rs.getString("nombre_servicio"));
				servicioPrincipal.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				servicioPrincipal.setInstitucion(rs.getInt("institucion"));
				servicioPrincipal.getUsuarioModifica().setLoginUsuario(rs.getString("usuario_modifica"));
				servicioPrincipal.setFechaModifica(rs.getString("fecha_modifica"));
				servicioPrincipal.setHoraModifica(rs.getString("hora_modifica"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//******************************************************************************************+
			
			//*************SE CARGAN LOS SERVICIOS INCLUIDOS DEL SERVICIO PRINCIPAL*********************
			if(servicioPrincipal.getCodigo().intValue()>0)
			{
				consulta = "SELECT " +
					"si.codigo as codigo," +
					"si.cod_servicio as codigo_servicio," +
					"getnombreservicio(si.cod_servicio,?) as nombre_servicio," +
					"si.centro_costo_grupo_serv as codigo_centro_costo," +
					"getnomcentrocosto(si.centro_costo_grupo_serv) as nombre_centro_costo," +
					"si.cantidad as cantidad " +
					"from facturacion.servi_incluido_servippal si " +
					"where si.cod_servippal = ? ";
				pst = new PreparedStatementDecorator(con,consulta);
				pst.setInt(1,Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(servicioPrincipal.getInstitucion())));
				pst.setBigDecimal(2, servicioPrincipal.getCodigo());
				logger.info(pst);

				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoServiIncluidoServiPpal servicioIncluido = new DtoServiIncluidoServiPpal();
					servicioIncluido.setCodigo(rs.getBigDecimal("codigo"));
					servicioIncluido.getServicio().setCodigo(rs.getInt("codigo_servicio"));
					servicioIncluido.getServicio().setNombre(rs.getString("nombre_servicio"));
					servicioIncluido.getCentroCosto().setCodigo(rs.getInt("codigo_centro_costo"));
					servicioIncluido.getCentroCosto().setNombre(rs.getString("nombre_centro_costo"));
					servicioIncluido.setCantidad(rs.getInt("cantidad"));
					servicioPrincipal.getServiciosIncluidos().add(servicioIncluido);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			}
			//*******************************************************************************************
			
			//*****************SE CARGAN LOS ARTICULOS INCLUIDOS DEL SERVICIO PRINCIPAL********************++
			if(servicioPrincipal.getCodigo().intValue()>0)
			{
				consulta = "SELECT " +
					"ai.codigo as codigo," +
					"ai.articulo_incluido as codigo_articulo," +
					"getdescripcionarticulo(ai.articulo_incluido) as nombre_articulo," +
					"ai.cantidad as cantidad," +
					"ai.farmacia as codigo_farmacia," +
					"getnomcentrocosto(ai.farmacia) as nombre_farmacia " +
					"from facturacion.art_incluidos_servippal ai " +
					"WHERE ai.cod_servi_ppal = ?";
				pst = new PreparedStatementDecorator(con,consulta);
				pst.setBigDecimal(1, servicioPrincipal.getCodigo());
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoArtIncluidoServiPpal articuloIncluido = new DtoArtIncluidoServiPpal();
					articuloIncluido.setCodigo(rs.getBigDecimal("codigo"));
					articuloIncluido.getArticulo().setCodigo(rs.getInt("codigo_articulo"));
					articuloIncluido.getArticulo().setNombre(rs.getString("nombre_articulo"));
					articuloIncluido.setCantidad(rs.getInt("cantidad"));
					articuloIncluido.getFarmacia().setCodigo(rs.getInt("codigo_farmacia"));
					articuloIncluido.getFarmacia().setNombre(rs.getString("nombre_farmacia"));
					servicioPrincipal.getArticulosIncluidos().add(articuloIncluido);
				}
			}
			//************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarServiciosArticulosIncluidos: ",e);
		}
	}
	
	public static String obtenerPrefijoFacturaXInstitucion(Connection con,
			int institucion) {
		String prefijo="";
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement("select pref_factura from instituciones where codigo=?"));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				prefijo=rs.getString(1);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error",e);
		}
		
		return prefijo;
	}
	
	/**
	 * metodo para consultar las facturas que tienen el mismo consecutivo de factura
	 * @param con
	 * @param factura
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ArrayList<DtoIgualConsecutivoFactura> consultarFacturasMismoConsecutivo(BigDecimal consecutivoFactura, int codigoInstitucionInt) 
	{
		ArrayList<DtoIgualConsecutivoFactura> lista= new ArrayList<DtoIgualConsecutivoFactura>();
		String consulta="SELECT " +
							"f.codigo as codigo, " +
							"f.consecutivo_factura as consecutivo, " +
							"case when f.empresa_institucion is not null then f.empresa_institucion else f.institucion end as institucion, " +
							"case when f.empresa_institucion is not null then getDescempresainstitucion(f.empresa_institucion) else getnombreinstitucion(f.institucion) end as nombreinstitucion, " +
							"f.centro_aten as centroatencion, " +
							"getnomcentroatencion(f.centro_aten) as nombrecentroatencion " +
						"from " +
							"facturas f " +
						"where " +
							"f.consecutivo_factura = ? " +
							"and f.institucion=?";
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,consecutivoFactura.longValue());
			pst.setInt(2, codigoInstitucionInt);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoIgualConsecutivoFactura dto= new DtoIgualConsecutivoFactura();
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centroatencion"), rs.getString("nombrecentroatencion")));
				dto.setCodigoFactura(rs.getBigDecimal("codigo"));
				dto.setConsecutivoFactura(rs.getBigDecimal("consecutivo"));
				dto.setInstitucion(new InfoDatosInt(rs.getInt("institucion"), rs.getString("nombreinstitucion")));
				lista.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFacturasMismoConsecutivo: "+e);
		}
		return lista;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap obtenerInfoUltimaCuentaFacturada(Connection con, HashMap filtros) {
		HashMap cuenta = new HashMap();
		cuenta.put("cuenta", "");
		String sql = "SELECT " +
							"c.id, " +
							"c.id_ingreso, "+
							"c.via_ingreso, " +
							"con.tipo_regimen, " +
							"sc.convenio, " +
							"con.pyp " +
						"FROM " +
							"cuentas c " +
						"INNER JOIN " +
							"sub_cuentas sc ON (sc.ingreso=c.id_ingreso) " +
						"INNER JOIN " +
							"convenios con ON (con.codigo=sc.convenio) " +
						"WHERE " +
							"c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+" "+
							"AND c.codigo_paciente="+filtros.get("codigoPaciente")+" "+
							"AND sc.nro_prioridad=1 " +
							"AND con.pyp= '" +ValoresPorDefecto.getValorTrueParaConsultas()+"' "+
						"ORDER BY " +
							"c.id desc";
		
		logger.info("SQL / obtenerInfoUltimaCuentaFacturada / "+sql);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
								
					cuenta.put("cuenta", rs.getString("id"));
					cuenta.put("ingreso", rs.getString("id_ingreso"));
					cuenta.put("viaIngreso", rs.getString("via_ingreso"));
					cuenta.put("convenio", rs.getString("convenio"));
					cuenta.put("tipoRegimen", rs.getString("tipo_regimen"));
				}	
			
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			logger.info("Error", e);
		}
		Utilidades.imprimirMapa(cuenta);
		return cuenta;
	}

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarTiposAfiliado(Connection con, HashMap campos, int tipoBD) {
		try
		{
			String consulta = "SELECT DISTINCT ta.acronimo,ta.nombre from tipos_afiliado ta ";
			//Si se ingresa convenio se debe filtrar por monto de cobro
			if(Utilidades.convertirAEntero(campos.get("codigoConvenio").toString(), true)>0)
			{
				consulta += " inner join detalle_monto dmc on (dmc.tipo_afiliado_codigo=ta.acronimo) " + 
							" inner join " + 
							"( select * from montos_cobro mc " +
								"where mc.convenio            = " + campos.get("codigoConvenio") + " " +
								"and mc.vigencia_inicial = GETVIGENCIACONVENIO(" + campos.get("codigoConvenio") + ", '" + campos.get("fechaReferencia") + "' ) " +
								" order by mc.codigo desc) mc " +
							"on(mc.codigo=dmc.monto_codigo "+(Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString(), true)>0?" and dmc.via_ingreso_codigo = "+campos.get("codigoViaIngreso")+" ":"")+" and dmc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+") ";
			}
			
			
			consulta += " ORDER BY ta.nombre ";
			
			logger.info("\n\n\n\n\n\n\n\nCONSULTA TIPOS AFILIADO: "+consulta+"\n\n\n\n\n\n\n\n");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposAfiliado: "+e);
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarTiposAfiliadoXEstrato(Connection con, HashMap campos, int tipoBD) {
		try
		{
			String consulta = "SELECT DISTINCT ta.acronimo,ta.nombre from tipos_afiliado ta ";
			//Si se ingresa convenio se debe filtrar por monto de cobro
			if(Utilidades.convertirAEntero(campos.get("codigoConvenio").toString(), true)>0)
			{
				consulta += " inner join detalle_monto dmc on (dmc.tipo_afiliado_codigo=ta.acronimo) " + 
							" inner join " + 
							"( select * from montos_cobro mc " +
								"where mc.convenio            = " + campos.get("codigoConvenio") + " " +
								"and mc.vigencia_inicial = GETVIGENCIACONVENIO(" + campos.get("codigoConvenio") + ", '" + campos.get("fechaReferencia") + "' ) " +
								" order by mc.codigo desc) mc " +
							"on(mc.codigo=dmc.monto_codigo "+(Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString(), true) > 0 ? 
									" and dmc.via_ingreso_codigo = "+campos.get("codigoViaIngreso") + " and dmc.estrato_social_codigo = "+campos.get("codigoEstratoSocial") : "") + 
							" and dmc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+") ";
			}
			
			
			consulta += " ORDER BY ta.nombre ";
			
			logger.info("\n\n\n\n\n\n\n\nCONSULTA TIPOS AFILIADO: "+consulta+"\n\n\n\n\n\n\n\n");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposAfiliado: "+e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerTamanioImpresionFactura(Connection con,
			String codigoFactura) {
		String tam="";
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement("SELECT tam_impresion from manejopaciente.garantia_paciente mp inner join cuentas c on (c.via_ingreso=mp.codigo_via_ingreso and c.tipo_paciente=mp.acronimo_tipo_paciente) inner join facturas fac on (fac.cuenta=c.id) where fac.codigo="+codigoFactura));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				tam=rs.getString(1);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error",e);
		}
		
		return tam;
	}

	/**
	 * 
	 * @param con
	 * @param codigoServicioSolicitado
	 * @return
	 */
	public static boolean esServicioOdontologico(Connection con,
			int codigoServicioSolicitado) {
		String consulta="SELECT atencion_odontologica from servicios where codigo="+codigoServicioSolicitado;
		boolean resultado=false;
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=UtilidadTexto.getBoolean(rs.getString(1));
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error",e);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param detalleCodigoMonto
	 * @return
	 */
	public static boolean montoCobroAsociadoSubcuenta(int detalleCodigoMonto) {
		String consulta="select count(1) from sub_cuentas where monto_cobro="+detalleCodigoMonto;
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getInt(1)>0;
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	
	/**
	 * @param codigoArticulo
	 * @return
	 */
	public static Integer tipoMedicamento(int codigoArticulo) {
		String consulta="select a.naturaleza naturaleza from articulo a where a.codigo=? and a.naturaleza='01'";
		Integer   resultado=new Integer(0);
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoArticulo);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getInt("naturaleza");
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	/**
	 * 
	 * Metodo que consulta el tipo de codigo articulo segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 * @author leoquico
	 * 
	 */
	
	public static int consultarTipoConvenioArticulo(Connection con, String convenio){
		
		int tipoCodigoArticulo = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst = null;
		ResultSet rs = null;
        StringBuilder consulta = new StringBuilder();
        
        consulta.append("SELECT tipo_codigo_articulos ");
        consulta.append("FROM convenios ");
        consulta.append("WHERE codigo = ?");

		try {
			pst = con.prepareStatement(consulta.toString(), ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Integer.parseInt(convenio));
			rs = pst.executeQuery();

			if (rs.next()) {
				if (rs.getObject(1) != null) {
					tipoCodigoArticulo = rs.getInt(1);
				}
            }

		} catch (SQLException e) {
			logger.error("Error en Tipo Convenio Articulo CONSULTA: " + e);
		}
        finally
        {
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}

		return tipoCodigoArticulo;
	}
	
	/**
	 * 
	 * Metodo que consulta el tipo de codigo servicio segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 * @author leoquico
	 * 
	 */
	
	public static int consultarTipoConvenioServicio(Connection con, String convenio){
		
		int tipoCodigoServicio = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst = null;
		ResultSet rs = null;

		StringBuilder consulta = new StringBuilder();
		consulta.append("SELECT tipo_codigo ");
		consulta.append("FROM convenios ");
		consulta.append("WHERE codigo = ?");
		
		try {
			pst = con.prepareStatement(consulta.toString(), ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Integer.parseInt(convenio));
			rs = pst.executeQuery();

			if (rs.next()) {
				if (rs.getObject(1) != null) {
					tipoCodigoServicio = rs.getInt(1);
				}
            }

		} catch (SQLException e) {
			logger.error("Error en Tipo Convenio Articulo CONSULTA: " + e);
		}
		finally
        {
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}

		return tipoCodigoServicio;
	}
	
}