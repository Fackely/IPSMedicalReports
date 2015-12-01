/*
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 */
package com.princetonsa.dao.sqlbase.ordenesmedicas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.ordenes.InfoArticuloOrdenAmbulatoriaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 */
public class SqlBaseOrdenesAmbulatoriasDao 
{ 

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseOrdenesAmbulatoriasDao.class);
	
	
		
	/**
	 * 
	 */
	private static String cadenaConsultaDetalleOrdenArticulo="SELECT " +
																" doar.articulo as articulo," +
																" getdescarticulo(doar.articulo) as descarticulo," +
																" doar.dosis as dosis," +
																" doar.via as via," +
																" va.nombre as nomvia," +
																" doar.cantidad as cantidad," +
																" doar.observaciones as observaciones," +
																" doar.frecuencia as frecuencia," +
																" doar.tipo_frecuencia as tipofrecuencia," +
																" tf.nombre as nomtipofrecuencia," +
																" medicamento as medicamento," +
																" 'true' as estabd,  " +
																" doar.duracion_tratamiento as duraciontratamiento," +
																" doar.unidosis_articulo as unidosis," +
																"INVENTARIOS.GETCONTROLESPECIALARTICULO(a.categoria) AS controlespecial,"+
																" uxa.unidad_medida ||' - '|| CASE WHEN uxa.cantidad IS NULL THEN '' ELSE uxa.cantidad||'' END as nomunidosis, " +
																" doar.contrato_convenio AS contratoconvenio, " +
																" doar.cubierto AS cubierto, " +
																" a.descripcion AS descripcion_art, " +
																" a.naturaleza AS codnaturaleza, " +
																" si.clase AS codclaseinventario, " +
																" si.grupo AS codgrupo, " +
																" a.subgrupo AS codSubGrupo " +
															" from ordenes.det_orden_amb_articulo doar " +
															" left outer join unidosis_x_articulo uxa on (doar.unidosis_articulo=uxa.codigo) " +
															" left outer join vias_administracion va on(doar.via=va.codigo) " +
															" left outer join tipos_frecuencia tf on(tf.codigo=doar.tipo_frecuencia) " +
															" inner join ordenes_ambulatorias oa on(oa.codigo=doar.codigo_orden) " +
															" INNER JOIN articulo a ON (doar.articulo = a.codigo) " +
															" INNER JOIN subgrupo_inventario si ON(a.subgrupo = si.codigo) " +
															" where doar.codigo_orden=?";
		
	
	/**
	 * 
	 */
private static String cadenaInsertDetalleOrdenServicio="INSERT INTO det_orden_amb_servicio(codigo_orden,servicio,finalidad,cantidad,contrato_convenio,cubierto) VALUES ( ?,?,?,?,?,? )";
	
	/**
	 * 
	 */
	private static String cadenaInsertDetalleOrdenArticulo="INSERT INTO det_orden_amb_articulo" +
			" (" +
			" codigo_orden,articulo,dosis,via,observaciones," +
			" frecuencia,tipo_frecuencia,medicamento,cantidad, duracion_tratamiento," +
			" unidosis_articulo,contrato_convenio,cubierto) " +
			"values " +
			"(" +
			"?,?,?,?,?," +
			"?,?,?,?,?," +
			"?,?,?)";
	
	/**
	 * 
	 */
	private static String cadenaInserRespuestaOrden="INSERT INTO resultado_orden_ambulatorias (codigo_orden,usuario,fecha,hora,resultado) values(?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static String cadenaAnularOrden="INSERT INTO anulacion_orden_ambulatorias (codigo_orden,usuario,fecha,hora,motivo_anulacion)  values(?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static String cadenaDeleteOrdenArticulos="DELETE FROM det_orden_amb_articulo WHERE codigo_orden=? AND articulo=? ";
	
	/**
	 * 
	 */
	private static String cadenaUpdateOrdenArticulos="UPDATE det_orden_amb_articulo set dosis=?, via=?, observaciones=?, frecuencia=?, tipo_frecuencia=?, cantidad=?, medicamento=?, duracion_tratamiento=?, unidosis_articulo=?, cubierto=?,contrato_convenio=? WHERE codigo_orden=? AND articulo=?";
	
	/**
	 * Cadena que inserta informacion de referencia de consulta externa
	 */
	private static String ingresarInformacionReferenciaExternaStr = "INSERT INTO ref_orden_ambulatoria " +
		"(codigo_orden,acronimo_diagnostico,cie_diagnostico,finalidad,causa_externa,resultados,numero_sol_ref) " +
		"VALUES(?,?,?,?,?,?,?)";
	
	
	
	/**
	 * Cadena para consultar los centros de costo asociados al grupo de servicio
	 */
	private static String consultaCentrosCostoXUnidadAgendaServ= "SELECT DISTINCT " +
															"cc.codigo AS centro_costo, " +
															"cc.nombre AS nombre, " +
															"cc.tipo_entidad_ejecuta AS tipo_entidad_ejecuta, " +
															"ca.descripcion AS centro_atencion, " +
															"ca.consecutivo AS codCentroAtencion " +
															"FROM facturacion.servicios s "+
															"INNER JOIN consultaexterna.servicios_unidades_consulta suc ON(suc.codigo_servicio = s.codigo) "+ 
															"INNER JOIN consultaexterna.cen_costo_x_un_consulta ccuc ON(ccuc.unidad_consulta=suc.unidad_consulta) "+ 
															"INNER JOIN administracion.centros_costo cc ON (cc.codigo=ccuc.centro_costo) "+
															"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo=cc.centro_atencion) " +
															"WHERE s.codigo=?  AND cc.tipo_entidad_ejecuta IN ('INT', 'EXT') ";
	
	
	
	private static String consultarCodigoOrdenAmb= "SELECT codigo FROM ordenes_ambulatorias WHERE consecutivo_orden=? "; 
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap consultarCodigoOrdenAmb(String consecutivo)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSECUTIVO------->"+consecutivo+"\n\nCONSULTA CODIGO ORDEN----->>>>>>>>>>>"+consultarCodigoOrdenAmb);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCodigoOrdenAmb, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setString(1, consecutivo);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CODIGO ORDEN------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	/**
	 * Metodo para consultar los centros de costo asociados a las unidades de agenda del servicio
	 * @param criterios
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap consultaCentrosCostoXUnidadAgendaServ(Connection con, int codigoServicio)
	{
		HashMap resultados= new HashMap();
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaCentrosCostoXUnidadAgendaServ, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, codigoServicio);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
									
			ps.close();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CENTROS COSTO------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param institucion
	 * @param estado
	 * @param idIngreso 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarOrdenesAmbulatoriasPaciente(Connection con, int codigoPersona, String institucion, int estado, String cadenaConsultaOrdenes, int idIngreso) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try 
		{
			String cadena=cadenaConsultaOrdenes+" and oa.codigo_paciente=? and oa.institucion=? ";
			
			if(idIngreso!=ConstantesBD.codigoNuncaValido)
				cadena += " and oa.ingreso="+idIngreso+" ";
			
			if(estado>0)
			{
				cadena=cadena +" and estado="+estado;
				if(estado==ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente)
				{
					cadena=cadena+" and oa.consulta_externa="+ValoresPorDefecto.getValorFalseParaConsultas();
				}
			}
			
			cadena= cadena+" order by to_number(oa.consecutivo_orden, '99999999999999999') asc";
			logger.info("\n\n\nconsultarOrdenesAmbulatoriasPaciente-->"+cadena+"---codpac--->"+codigoPersona+"------idIngreso----->"+idIngreso);


			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPersona);
			ps.setInt(2,Utilidades.convertirAEntero(institucion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseOrdenesAmbulatoriasDao[consultarOrdenesAmbulatoriasPaciente] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}

	/**
	 * Metodo que realiza las consultas de las ordenes ambulatorias x codigo
	 * @param con
	 * @param codigoPersona
	 * @param codigoInstitucion
	 * @param i
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarOrdenesAmbulatoriasXCodigoOrden(Connection con, String codigoOrden , String institucion, String cadenaConsultaOrdenes)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena=cadenaConsultaOrdenes+" and oa.codigo=? and oa.institucion=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(codigoOrden));
			ps.setInt(2,Utilidades.convertirAEntero(institucion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			
			logger.info("\n\nRESULTADO CONSULTA ORDEN---->"+mapa);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseOrdenesAmbulatoriasDao[consultarOrdenesAmbulatoriasXCodigoOrden] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}


	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap cargarDetalleOrdenArticulos(Connection con, String codigoOrden) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleOrdenArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("-->"+cadenaConsultaDetalleOrdenArticulo+"-->"+Utilidades.convertirADouble(codigoOrden));
			ps.setDouble(1,Utilidades.convertirADouble(codigoOrden));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseOrdenesAmbulatoriasDao[consultarOrdenesAmbulatoriasPaciente] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean guardarInformacionGeneralAmbulatorios(Connection con, HashMap vo,String cadena) 
	{
		try 
		{
			DtoDiagnostico diagnostico=new DtoDiagnostico();
			if(vo.containsKey("ingreso") && UtilidadCadena.noEsVacio(vo.get("ingreso")+"") && !(vo.get("ingreso")+"").equals("-1") && !(vo.get("ingreso")+"").equals("0"))
			{
				diagnostico=Utilidades.getDiagnosticoPacienteIngreso(con,Utilidades.convertirAEntero(vo.get("ingreso")+""));				
			}
			logger.info("****************************************************");
			logger.info("zzzZ- Cadena " + cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			Utilidades.imprimirMapa(vo);
			ps.setString(1,vo.get("consecutivo")+"");
			ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setInt(3,Utilidades.convertirAEntero(vo.get("paciente")+""));
			ps.setDouble(4,Utilidades.convertirADouble(vo.get("tipoOrden")+""));
			ps.setBoolean(5,UtilidadTexto.getBoolean(vo.get("pyp")+""));
			ps.setBoolean(6,UtilidadTexto.getBoolean(vo.get("urgente")+""));
            ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));
			ps.setString(8,vo.get("hora")+"");
			ps.setDouble(9,Utilidades.convertirADouble(vo.get("estado")+""));
			ps.setString(10,vo.get("observaciones")+"");
			ps.setInt(11,Utilidades.convertirAEntero(vo.get("centroAtencion")+""));
			ps.setString(12,vo.get("usuario")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
			{
				ps.setNull(13, Types.INTEGER);
			}else
			{
				ps.setInt(13, Utilidades.convertirAEntero(vo.get("especialidad")+""));
			}
			
			if((vo.get("fechaConfirmacion")+"").equals(""))
			{
				ps.setNull(14,Types.DATE);
			}
			else
			{
	            ps.setDate(14,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaConfirmacion")+"")));
			}
			if((vo.get("horaConfirmacion")+"").equals(""))
			{
				ps.setNull(15,Types.CHAR);
			}
			else
			{
				ps.setString(15,vo.get("horaConfirmacion")+"");
			}
			if((vo.get("usuarioConfirma")+"").equals(""))
			{
				ps.setNull(16,Types.VARCHAR);
			}
			else
			{
				ps.setString(16,vo.get("usuarioConfirma")+"");
			}
			ps.setBoolean(17,UtilidadTexto.getBoolean(vo.get("consultaExterna")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("centroCostoSolicita")+""))
			{
				ps.setNull(18,Types.INTEGER);
			}
			else
			{
				ps.setInt(18, Utilidades.convertirAEntero(vo.get("centroCostoSolicita")+""));
			}
			
			ps.setString(19,vo.get("otros")+"");
			
			//Ingreso del paciente
			if(vo.containsKey("ingreso") && 
					UtilidadCadena.noEsVacio(vo.get("ingreso")+"")
					&&
					!(vo.get("ingreso")+"").equals("-1")
					&&
					!(vo.get("ingreso")+"").equals("0")
				)
				ps.setInt(20,Utilidades.convertirAEntero(vo.get("ingreso")+""));
			else
				ps.setNull(20,Types.INTEGER);
			
			//cuenta del paciente
			if(vo.containsKey("cuenta") && 
					UtilidadCadena.noEsVacio(vo.get("cuenta")+"")
					&&
					!(vo.get("cuenta")+"").equals("-1")
					&&
					!(vo.get("cuenta")+"").equals("0")
				)
				ps.setInt(21,Utilidades.convertirAEntero(vo.get("cuenta")+""));
			else
				ps.setNull(21,Types.INTEGER);
			//Queda pendiente este cambio debido a que en otras funcionalidades no se esta parametrizando bien el campo de control especial
			if (!UtilidadTexto.isEmpty(vo.get("controlEspecial").toString()))
				ps.setString(22, vo.get("controlEspecial").toString());
			else
				ps.setString(22,ConstantesBD.acronimoNo);
						
				
			if (!diagnostico.getTipoCieDiagnostico().isEmpty())
			{
				ps.setString(23, diagnostico.getAcronimoDiagnostico());
				ps.setInt(24, diagnostico.getTipoCieDiagnosticoInt());
			}
			else
			{
				//ps.setObject(23,null);
				//ps.setObject(24,null);
				ps.setNull(23, Types.VARCHAR);
				ps.setNull(24, Types.INTEGER);
			}
			
			if (vo.get("citaAsociada")!=null&&!vo.get("citaAsociada").toString().equals(ConstantesBD.codigoNuncaValido+""))
			{
				ps.setInt(25, (Integer)vo.get("citaAsociada"));
			}
			else
			{
				ps.setNull(25, Types.INTEGER);
			}
			
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("error guardando la orden de ambulatorios [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean guardarInformacionDetalleOrdenAmbulatorioServicio(Connection con, HashMap vo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertDetalleOrdenServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO det_orden_amb_servicio(codigo_orden,servicio,finalidad,cantidad) VALUES ( ?,?,?,?)
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(vo.get("servicio")+""));
			if((vo.get("finalidad")+"").equals(""))
				ps.setNull(3,Types.INTEGER);
			else
				ps.setInt(3,Utilidades.convertirAEntero(vo.get("finalidad")+""));
			ps.setDouble(4,Utilidades.convertirADouble(vo.get("cantidad")+""));
			/**
			 * MT: 3796, 3458, 2756 y 3888
			 * Cambio: DCU 307 V1.62
			 * Diana Ruiz
			 */			
			ps.setInt(5,Utilidades.convertirAEntero(vo.get("contrato_convenio_")+""));
			ps.setString(6,vo.get("cubierto_")+"");
			
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("error guardarInformacionDetalleOrdenAmbulatorioServicio - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean guardarInformacionDetalleOrdenAmbulatorioArticulo(Connection con, HashMap vo) 
	{
		try 
		{

			int numReg=Utilidades.convertirAEntero(vo.get("numRegistros")==null||(vo.get("numRegistros")+"").equals("")?"0":vo.get("numRegistros")+"");
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertDetalleOrdenArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for(int i=0;i<numReg;i++)
			{
				if(!UtilidadTexto.getBoolean(vo.get("fueEliminadoArticulo_"+i)+""))
				{
					
					/**
					 * INSERT INTO det_orden_amb_articulo" +
						" (" +
						" codigo_orden,
						articulo,dosis,via,observaciones," +
						" frecuencia,tipo_frecuencia,medicamento,cantidad, duracion_tratamiento," +
						" unidosis_articulo) " +
						"values " +
						"(" +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"?)
					 */
					
					
					ps.setDouble(1,Utilidades.convertirADouble(vo.get("codOrden")+""));
					ps.setInt(2,Utilidades.convertirAEntero(vo.get("articulo_"+i)+""));
					logger.info("\n\n\n\n\n\n\n-->"+vo.get("medicamento_"+i)+"\n\n\n\n\n\n");
					if(UtilidadTexto.getBoolean(vo.get("medicamento_"+i)+""))
					{
						ps.setString(3,vo.get("dosis_"+i)+"");
						ps.setInt(4,Utilidades.convertirAEntero(vo.get("via_"+i)+""));
						ps.setString(5,vo.get("observaciones_"+i)+"");
						ps.setDouble(6,Utilidades.convertirADouble(vo.get("frecuencia_"+i)+""));
						ps.setInt(7,Utilidades.convertirAEntero(vo.get("tipofrecuencia_"+i)+""));
						ps.setBoolean(8,UtilidadTexto.getBoolean(ValoresPorDefecto.getValorTrueParaConsultas()));
						ps.setDouble(9,Utilidades.convertirADouble((vo.get("cantidad_"+i)+"").trim().equals("")?"0":vo.get("cantidad_"+i)+""));
						ps.setInt(10, Utilidades.convertirAEntero(vo.get("duraciontratamiento_"+i)+""));
						ps.setDouble(11, Utilidades.convertirADouble(vo.get("unidosis_"+i)+""));
						/**
						 * MT: 3796, 3458, 2756 y 3888
						 * Cambio: DCU 307 V1.62
						 * Diana Ruiz
						 */	
						ps.setInt(12,Utilidades.convertirAEntero(vo.get("contrato_convenio_"+i)+""));
						ps.setString(13,vo.get("cubierto_"+i)+"");
						
					}
					else
					{
						ps.setNull(3,Types.VARCHAR);
						ps.setNull(4,Types.INTEGER);
						ps.setNull(5,Types.VARCHAR);
						ps.setNull(6,Types.NUMERIC);
						ps.setNull(7,Types.INTEGER);
						ps.setBoolean(8,UtilidadTexto.getBoolean(ValoresPorDefecto.getValorFalseParaConsultas()));
						ps.setString(9,(vo.get("cantidad_"+i)+"").trim().equals("")?"0":vo.get("cantidad_"+i)+"");
						ps.setNull(10,Types.INTEGER);
						ps.setNull(11,Types.NUMERIC);
						/**
						 * MT: 3796, 3458, 2756 y 3888
						 * Cambio: DCU 307 V1.62
						 * Diana Ruiz
						 */			
						ps.setInt(12,Utilidades.convertirAEntero(vo.get("contrato_convenio_"+i)+""));
						ps.setString(13,vo.get("cubierto_"+i)+"");
					}
					
					ps.executeUpdate();
				}
			}
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("error guardarInformacionDetalleOrdenAmbulatorioArticulo - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean ingresarResultado(Connection con, HashMap vo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInserRespuestaOrden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO resultado_orden_ambulatorias (codigo_orden,usuario,fecha,hora,resultado) values(?,?,?,?,?)
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.setString(2,vo.get("usuario")+"");
			ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));
			ps.setString(4,vo.get("hora")+"");
			ps.setString(5,vo.get("resultado")+"");
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("error ingresarResultado - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}



	/***
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean anularOrden(Connection con, HashMap vo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaAnularOrden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO anulacion_orden_ambulatorias (codigo_orden,usuario,fecha,hora,motivo_anulacion)  values(?,?,?,?,?)
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.setString(2,vo.get("usuario")+"");
			ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));
			ps.setString(4,vo.get("hora")+"");
			ps.setString(5,vo.get("motivo")+"");
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("error anularOrden - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param estado
	 * @return
	 */
	public static boolean actualizarEstadoOrdenAmbulatoria(Connection con, String codigo, int estado) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE ordenes_ambulatorias set estado=? where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(estado+""));
			ps.setDouble(2,Utilidades.convertirADouble(codigo+""));
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("error actualizarEstadoOrdenAmbulatoria - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codOrden
	 * @param estado
	 * @param comentario 
	 * @param usuario 
	 * @return
	 */
	public static boolean actualizarEstadoActividadProgramaPYPPAcienteNumOrden(Connection con, String codOrden, String estado, String usuario, String comentario)
	{
		try
		{
			if(estado==ConstantesBD.codigoEstadoProgramaPYPSolicitado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_solicitar=?,fecha_solicitar=current_date,hora_solicitar=? where numero_orden=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,Utilidades.convertirADouble(estado+""));
				ps.setString(2,usuario);				
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setDouble(4,Utilidades.convertirADouble(codOrden+""));
				return ps.executeUpdate()>0;
			}
			else if(estado==ConstantesBD.codigoEstadoProgramaPYPCancelado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_cancelar=?,fecha_cancelar=current_date,hora_cancelar=?,motivo_cancelacion=? where numero_orden=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,Utilidades.convertirADouble(estado+""));
				ps.setString(2,usuario);
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setString(4,comentario+"");
				ps.setDouble(5,Utilidades.convertirADouble(codOrden+""));
				return ps.executeUpdate()>0;
			}
			else if(estado==ConstantesBD.codigoEstadoProgramaPYPEjecutado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_ejecutar=?,fecha_ejecutar=current_date,hora_ejecutar=? where numero_orden=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,Utilidades.convertirADouble(estado+""));
				ps.setString(2,usuario);
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setDouble(4,Utilidades.convertirADouble(codOrden+""));
				return ps.executeUpdate()>0;
			}
			else if(estado==ConstantesBD.codigoEstadoProgramaPYPProgramado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_programar=?,fecha_programar=current_date,hora_programar=? where numero_orden=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,Utilidades.convertirADouble(estado+""));
				ps.setString(2,usuario);
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setDouble(4,Utilidades.convertirADouble(codOrden+""));
				return ps.executeUpdate()>0;
			}
			
		} 
		catch (SQLException e) 
		{
			logger.error("error actualizarEstadoActividadProgramaPYPPAcienteNumOrden - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int actualizarSolicitudEnOrdenAmbulatoria(Connection con,HashMap campos)
	{
		try
		{
			if(Utilidades.convertirAEntero(campos.get("numeroSolicitud")+"")>0)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO ordenes_amb_solicitudes(orden,numero_solicitud) values(?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
				ps.setInt(2,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
				ps.executeUpdate();
				ps.close();
			}

			String consulta = "UPDATE ordenes_ambulatorias SET estado = ? WHERE codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("estadoOrden")+""));
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarSolicitudEnOrdenAmbulatoria de SQlBaseOrdenesAmbulatoriasDao: "+e);
			return 0;
		}
	}

	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria para la reserva de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int confirmarReservaCitaEnOrdenAmbulatoria(Connection con,HashMap campos)
	{
		try
		{
			if(Utilidades.convertirAEntero(campos.get("codigoCita")+"")>0) {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO ordenes.ordenes_amb_reservas_citas(orden,codigo_cita) values(?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
				ps.setInt(2,Utilidades.convertirAEntero(campos.get("codigoCita")+""));
				ps.executeUpdate();
				ps.close();
			}

			String consulta = "UPDATE ordenes_ambulatorias SET estado = ? , usuario_confirma=?, fecha_confirmacion=current_date,hora_confirmacion=? WHERE codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
			pst.setString(2,campos.get("usuario")+"");
			pst.setString(3,UtilidadFecha.getHoraActual());
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarReservaCitaEnOrdenAmbulatoria de SQlBaseOrdenesAmbulatoriasDao: ", e);
			return 0;
		}
	}


	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @param institucion 
	 * @return
	 * Formato: urgente | observaciones | centro_atencion_solicita | usuario_solicita | especialidad_solicita | servicio | finalidad  | servicio cups
	 */
	public static String obtenerInfoServicioProcOrdenAmbulatoria(Connection con, String ordenAmbulatoria, int institucion) 
	{
		String resultado="";
		try 
		{
			String cadena="SELECT " +
					"oa.urgente," +
					"oa.observaciones||' '," +
					"oa.centro_atencion_solicita," +
					"oa.usuario_solicita," +
					"oa.especialidad_solicita," +
					"doas.servicio," +
					"doas.finalidad," +
					"doas.cantidad," +
					"oa.fecha," +
					"getcodigopropservicio2(doas.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS serviciocups " +
					"from ordenes.ordenes_ambulatorias oa " +
					"left outer join det_orden_amb_servicio doas on(oa.codigo=doas.codigo_orden) " +
					"where oa.codigo=?";
			
			
			logger.info("\n\n obtenerInfoServicioProcOrdenAmbulatoria->"+cadena+" ->"+ordenAmbulatoria);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(ordenAmbulatoria));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return (rs.getString(1).equals("")?" ":
				rs.getString(1))+ConstantesBD.separadorSplit+
				rs.getString(2)+ConstantesBD.separadorSplit+
				rs.getString(3)+ConstantesBD.separadorSplit+
				rs.getString(4)+ConstantesBD.separadorSplit+
				rs.getString(5)+ConstantesBD.separadorSplit+
				rs.getString(6)+ConstantesBD.separadorSplit+
				rs.getString(7)+ConstantesBD.separadorSplit+
				rs.getString(8)+ConstantesBD.separadorSplit+
				rs.getString(9);
			}
				
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}



	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int confirmarOrdenAmbulatoria(Connection con, HashMap campos) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO ordenes_amb_solicitudes(orden,numero_solicitud) values(?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			ps.setInt(2,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			ps.executeUpdate();
			ps.close();
			
			String consulta = "UPDATE ordenes_ambulatorias SET estado = ? , usuario_confirma=?, fecha_confirmacion=current_date,hora_confirmacion=? WHERE codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
			pst.setString(2,campos.get("usuario")+"");
			pst.setString(3,UtilidadFecha.getHoraActual());
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarSolicitudEnOrdenAmbulatoria de SQlBaseOrdenesAmbulatoriasDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para ingresar informacion de referencia de consulta
	 * externa a la orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int ingresarInformacionReferenciaExterna(Connection con,HashMap campos)
	{
		try
		{
			int resp = 0;
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con, ingresarInformacionReferenciaExternaStr);
			
			logger.info("*************** ALEJO ");
			logger.info("Insert: " + ingresarInformacionReferenciaExternaStr);
			Utilidades.imprimirMapa(campos);
			
			/**
			 * INSERT INTO ref_orden_ambulatoria " +
				"(codigo_orden,acronimo_diagnostico,cie_diagnostico,finalidad,causa_externa,resultados,numero_sol_ref) " +
				"VALUES(?,?,?,?,?,?,?)
			 */
			
			logger.info("Numero Orden "+campos.get("numeroOrden"));
			logger.info("Numero Orden D "+Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			//se verifica diagnostico
			if(campos.get("acronimoDiag")!=null&&campos.get("tipoCieDiag")!=null)
			{
				pst.setString(2,campos.get("acronimoDiag")+"");
				pst.setInt(3,Utilidades.convertirAEntero(campos.get("tipoCieDiag")+""));
			}
			else
			{
				pst.setNull(2,Types.VARCHAR);
				pst.setNull(3,Types.INTEGER);
			}
			//se verifica finalidad
			if(campos.get("finalidad")!=null)
				pst.setString(4,campos.get("finalidad")+"");
			else
				pst.setNull(4,Types.VARCHAR);
			//se verifica causa externa
			if(campos.get("causaExterna")!=null)
				pst.setInt(5,Utilidades.convertirAEntero(campos.get("causaExterna")+""));
			else
				pst.setNull(5,Types.INTEGER);
			//Se verifica resultados
			if(campos.get("resultados")!=null)
				pst.setString(6,campos.get("resultados")+"");
			else
				pst.setNull(6,Types.VARCHAR);
			
			if(campos.get("numSolConsulta")!=null)
				pst.setInt(7,Utilidades.convertirAEntero(campos.get("numSolConsulta")+""));
			else
				pst.setNull(7,Types.INTEGER);
			
			logger.info(pst.getPs());
			
			resp = pst.executeUpdate();
			
			if(resp>0)
			{
				//****SE ACTUALIZA LA ORDEN AMBULATORIA PARA CONSULTA EXTERNA**********+
				String consulta = "UPDATE ordenes_ambulatorias SET consulta_externa = "+ValoresPorDefecto.getValorTrueParaConsultas()+" WHERE codigo = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setDouble(1,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
				resp = pst.executeUpdate();
				//**********************************************************************
			}
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en ingresarInformacionReferenciaExterna de SqlBaseOrdenesAmbulatoriasDao: "+e);
			return 0;
		}
	}

	/**
	 * 
	 * @param con
	 * @param numeroOrden
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarServiciosOrdenAmbulatoria(Connection con, String numeroOrden, int institucion)
	{
		HashMap temp=new HashMap();
		temp.put("numRegistros", "0");
		String cadena="SELECT s.codigo AS codigo_servicio, s.grupo_servicio, getcodigoservicio(doas.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as codigo_cups, getnombreservicio(doas.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as descripcion_servicio,s.espos as es_pos,doas.finalidad as finalidad,doas.cantidad as cantidad,case when oa.urgente = 'false' then 'false' else 'true' end as urgente,'false' as fue_eliminado_servicio from ordenes_ambulatorias oa inner join det_orden_amb_servicio doas on(oa.codigo=doas.codigo_orden) inner join servicios s on (doas.servicio=s.codigo) where oa.consecutivo_orden='"+numeroOrden+"' and oa.institucion="+institucion+" ";
		
		logger.info(cadena);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			temp=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e)
		{
			logger.error("error",e);
		}
		
		return temp;
	}

	/**
	 * 
	 * @param con
	 * @param articulosMap
	 * @param codigoOrden
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("rawtypes")
	public static boolean updateOrdenAmbulatoriaArticulos(Connection con, HashMap articulosMap, String codigoOrden)
	{
		logger.info("codigoOrden-->"+codigoOrden);
		logger.info("mapaArticulos-->"+articulosMap);
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		boolean resultado=true;
		try
		{
			con.setAutoCommit(false);
			//primero se elimibna
			for(int w=0; w<Utilidades.convertirAEntero( articulosMap.get("numRegistros")+""); w++)
			{
				if( UtilidadTexto.getBoolean(articulosMap.get("fueEliminadoArticulo_"+w)+"")
					&& UtilidadTexto.getBoolean(articulosMap.get("estabd_"+w)+""))
				{
					pst =  con.prepareStatement(cadenaDeleteOrdenArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					
					/**
					 * DELETE FROM det_orden_amb_articulo WHERE codigo_orden=? AND articulo=? 
					 */
					
					pst.setDouble(1,Utilidades.convertirADouble(codigoOrden));
					pst.setInt(2,Utilidades.convertirAEntero(articulosMap.get("articulo_"+w)+""));
					if(pst.executeUpdate()<=0)
					{
						logger.info("no elimino index->"+w);
						resultado= false;
					}	
				}
			}
			
			//luego se modifica
			for(int w=0; w<Utilidades.convertirAEntero( articulosMap.get("numRegistros")+""); w++)
			{
				if( !UtilidadTexto.getBoolean(articulosMap.get("fueEliminadoArticulo_"+w)+"")
					&& UtilidadTexto.getBoolean(articulosMap.get("estabd_"+w)+""))
				{
					pst2 = con.prepareStatement(cadenaUpdateOrdenArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					if((UtilidadTexto.getBoolean(articulosMap.get("medicamento_"+w)+"")))
					{
						
						/**
						 * UPDATE det_orden_amb_articulo set 
						 * dosis=?, 
						 * via=?, 
						 * observaciones=?, 
						 * frecuencia=?, 
						 * tipo_frecuencia=?, 
						 * cantidad=?, 
						 * medicamento=?, 
						 * duracion_tratamiento=?, 
						 * unidosis_articulo=? 
						 * cubierto=?
						 * WHERE codigo_orden=? AND articulo=?
						 */
						
						pst2.setString(1, articulosMap.get("dosis_"+w)+"");
						pst2.setInt(2, Utilidades.convertirAEntero(articulosMap.get("via_"+w)+""));
						pst2.setString(3, articulosMap.get("observaciones_"+w)+"");
						pst2.setDouble(4, Utilidades.convertirADouble(articulosMap.get("frecuencia_"+w)+""));
						pst2.setInt(5, Utilidades.convertirAEntero(articulosMap.get("tipofrecuencia_"+w)+""));
						pst2.setDouble(6, Utilidades.convertirADouble(articulosMap.get("cantidad_"+w)+""));
						pst2.setBoolean(7, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorTrueParaConsultas()));
						pst2.setInt(8, Utilidades.convertirAEntero(articulosMap.get("duraciontratamiento_"+w)+""));
						pst2.setDouble(9, Utilidades.convertirADouble(articulosMap.get("unidosis_"+w)+""));
						/*
						 * MT 5880 se debe guardar la cobertura y el contrato que lo cubre
						 * segun DCU 437 v1.3 - PROCESO GENERAL, DE LA VALIDACIÓN DE COBERTURA DE SERVICIOS ARTÍCULOS
						 * incluyendo los que existen desde la creacion de la solicitud como los que se agregan en la modificacion
						 * 
						 * jeilones
						 * */
						if(articulosMap.get("cubierto_"+w) != null && Integer.parseInt(articulosMap.get("contrato_convenio_"+w).toString()) != ConstantesBD.codigoNuncaValido){
							pst2.setString(10, articulosMap.get("cubierto_"+w).toString());
							pst2.setInt(11, Utilidades.convertirAEntero(articulosMap.get("contrato_convenio_"+w)+""));
						}else{
							pst2.setNull(10, Types.VARCHAR);
							pst2.setInt(11, Utilidades.convertirAEntero(articulosMap.get("contrato_convenio_"+w)+""));
						}
						/*FIN MT 5880*/
						pst2.setDouble(12, Utilidades.convertirADouble(codigoOrden));
						pst2.setInt(13, Utilidades.convertirAEntero(articulosMap.get("articulo_"+w)+""));
					}
					else
					{
						pst2.setNull(1, Types.VARCHAR);
						pst2.setNull(2, Types.INTEGER);
						pst2.setNull(3, Types.VARCHAR);
						pst2.setNull(4, Types.NUMERIC);
						pst2.setNull(5, Types.INTEGER);
						pst2.setDouble(6, Utilidades.convertirADouble(articulosMap.get("cantidad_"+w)+""));
						pst2.setBoolean(7, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorFalseParaConsultas()));
						pst2.setNull(8, Types.INTEGER);
						pst2.setNull(9, Types.NUMERIC);
						/*
						 * MT 5880 se debe guardar la cobertura y el contrato que lo cubre
						 * segun DCU 437 v1.3 - PROCESO GENERAL, DE LA VALIDACIÓN DE COBERTURA DE SERVICIOS ARTÍCULOS
						 * incluyendo los que existen desde la creacion de la solicitud como los que se agregan en la modificacion
						 * 
						 * jeilones
						 * */
						if(articulosMap.get("cubierto_"+w) != null && Integer.parseInt(articulosMap.get("contrato_convenio_"+w).toString()) != ConstantesBD.codigoNuncaValido){
							pst2.setString(10, articulosMap.get("cubierto_"+w).toString());
							pst2.setInt(11, Utilidades.convertirAEntero(articulosMap.get("contrato_convenio_"+w)+""));
						}else{
							pst2.setNull(10, Types.VARCHAR);
							pst2.setInt(11, Utilidades.convertirAEntero(articulosMap.get("contrato_convenio_"+w)+""));
						}
						/*FIN MT 5880*/
						pst2.setDouble(11, Utilidades.convertirADouble(codigoOrden));
						pst2.setInt(12, Utilidades.convertirAEntero(articulosMap.get("articulo_"+w)+""));
					}
					if(pst2.executeUpdate()<=0)
					{
						logger.info("no modifico index->"+w);
						resultado= false;
					}	
				}
			}
			
			//finalmente se inserta
			for(int w=0; w<Utilidades.convertirAEntero( articulosMap.get("numRegistros")+""); w++)
			{
				if( !UtilidadTexto.getBoolean(articulosMap.get("fueEliminadoArticulo_"+w)+"")
					&& !UtilidadTexto.getBoolean(articulosMap.get("estabd_"+w)+""))
				{
					pst3 = con.prepareStatement(cadenaInsertDetalleOrdenArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					
					/**
					 * INSERT INTO det_orden_amb_articulo" +
							" (" +
							" codigo_orden,articulo,dosis,via,observaciones," +
							" frecuencia,tipo_frecuencia,medicamento,cantidad, duracion_tratamiento," +
							" unidosis_articulo) " +
							"values " +
							"(" +
							"?,?,?,?,?," +
							"?,?,?,?,?," +
							"?)
					 */
		
					pst3.setDouble(1, Utilidades.convertirADouble(codigoOrden));
					pst3.setInt(2, Utilidades.convertirAEntero(articulosMap.get("articulo_"+w)+""));
					
					if((UtilidadTexto.getBoolean(articulosMap.get("medicamento_"+w)+"")))
					{
						pst3.setString(3, articulosMap.get("dosis_"+w)+"");
						pst3.setInt(4, Utilidades.convertirAEntero(articulosMap.get("via_"+w)+""));
						pst3.setString(5, articulosMap.get("observaciones_"+w)+"");
						pst3.setDouble(6, Utilidades.convertirADouble(articulosMap.get("frecuencia_"+w)+""));
						pst3.setInt(7, Utilidades.convertirAEntero(articulosMap.get("tipofrecuencia_"+w)+""));
						pst3.setBoolean(8, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorTrueParaConsultas()));
						pst3.setDouble(9, Utilidades.convertirADouble(articulosMap.get("cantidad_"+w)+""));
						pst3.setInt(10, Utilidades.convertirAEntero(articulosMap.get("duraciontratamiento_"+w)+""));
						pst3.setDouble(11, Utilidades.convertirADouble(articulosMap.get("unidosis_"+w)+""));
						/*
						 * En el DCU 307 no se encuentra definido que se adicione o valide la cobertura del convenio para los nuevos 
						 * medicamentos ingresados, por tanto por ahora se guarda esta información vacia hasta que análisis realice el
						 * cambio en el DCU y se realice un desarrollo formal de este cambio.
						 * Diana Ruiz
						 */
						if(articulosMap.get("cubierto_"+w) != null && Integer.parseInt(articulosMap.get("contrato_convenio_"+w).toString()) != ConstantesBD.codigoNuncaValido){
							pst3.setInt(12, Utilidades.convertirAEntero(articulosMap.get("contrato_convenio_"+w)+""));
							pst3.setString(13, articulosMap.get("cubierto_"+w)+"");
						}else{
							pst3.setNull(12, Types.INTEGER);
							pst3.setNull(13, Types.VARCHAR);
						}

					}
					else
					{
						pst3.setNull(3, Types.VARCHAR);
						pst3.setNull(4, Types.INTEGER);
						pst3.setNull(5, Types.VARCHAR);
						pst3.setNull(6, Types.NUMERIC);
						pst3.setNull(7, Types.INTEGER);
						pst3.setBoolean(8, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorTrueParaConsultas()));
						pst3.setDouble(9, Utilidades.convertirADouble(articulosMap.get("cantidad_"+w)+""));
						pst3.setNull(10, Types.INTEGER);
						pst3.setNull(11, Types.NUMERIC);
						/*
						 * En el DCU 307 no se encuentra definido que se adicione o valide la cobertura del convenio para los nuevos 
						 * medicamentos ingresados, por tanto por ahora se guarda esta información vacia hasta que análisis realice el
						 * cambio en el DCU y se realice un desarrollo formal de este cambio.
						 * Diana Ruiz
						 */
						pst3.setNull(12, Types.INTEGER);
						pst3.setNull(13, Types.VARCHAR);
					}
					if(pst3.executeUpdate()<=0)
					{
						logger.info("no inserto index->"+w);
						resultado= false;
					}	
				}
			}
			con.commit();
		}
		catch(SQLException e)
		{
			try{
				logger.error("Error en updateOrdenAmbulatoriaArticulos de SQlBaseOrdenesAmbulatoriasDao: "+e);
				con.rollback();
				resultado= false;
			}
			catch(SQLException sql)
			{
				logger.error("Error Haciendo Rollback: "+sql);
				resultado= false;
			}
		}
		catch(Exception ex)
		{
			logger.error("Error en updateOrdenAmbulatoriaArticulos de SQlBaseOrdenesAmbulatoriasDao: "+ex);
			resultado= false;
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				con.setAutoCommit(true);
			}
			catch(SQLException e)
			{
				logger.error("Error Cerrando PreparedStatement: "+e);
				resultado= false;
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoOA
	 * @return
	 * @throws SQLException
	 */
	public static String consultarCentroCostoSolicitante(Connection con, String codigoOA) throws SQLException
	{
		
		String getCodigoCentroSolicitaStr="SELECT centro_costo_solicita as centroCosto, getnomcentrocosto(centro_costo_solicita) AS \"desc\" from ordenes_ambulatorias where codigo= ? ";
		PreparedStatementDecorator getCodigoCentroSolicitaStatement= new PreparedStatementDecorator(con.prepareStatement(getCodigoCentroSolicitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		logger.info("--->"+getCodigoCentroSolicitaStr+"\n--->"+Utilidades.convertirADouble(codigoOA));
		getCodigoCentroSolicitaStatement.setDouble(1, Utilidades.convertirADouble(codigoOA));
		ResultSetDecorator rs=new ResultSetDecorator(getCodigoCentroSolicitaStatement.executeQuery());
		if (rs.next())
		{
			String resp=rs.getString("centroCosto")+ConstantesBD.separadorSplit+rs.getString("desc");
			rs.close();
			return resp;
		}
		else
		{
			throw new SQLException ("El Centro de Costo para el método getCodigoCentroSolicitado no existe");
		}		
	}


	public static int otenerUltimaCuentaPacienteValidaParaOrden(Connection con,int codigoPersona) 
	{
		String consulta=" select max(c.id) as cuentaid from cuentas c inner join ingresos ingr on (ingr.id=c.id_ingreso) where ingr.codigo_paciente=? and c.estado_cuenta<>"+ConstantesBD.codigoEstadoCuentaCerrada;
		int cuenta=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			logger.info("consulta -->"+consulta+" -->"+codigoPersona);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				cuenta=rs.getInt("cuentaid");
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("error",e);
		}
		return cuenta;
	}
	


	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public static DtoDiagnostico consultarDiagnosticoOrden(Connection con,int codigoOrden) 
	{
		//String consulta=" select acronimo_diagnostico as diagnostico,tipo_cie_diagnostico as cie from ordenes.ordenes_ambulatorias where codigo=?";
		String consulta=" select acronimo_diagnostico as diagnostico,cie_diagnostico as cie from ordenes.ref_orden_ambulatoria where codigo_orden=?";
		DtoDiagnostico dto=new DtoDiagnostico();
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoOrden);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
				dto.setTipoCieDiagnostico(rs.getString("cie"));
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("error",e);
		}
		return dto;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de consultar el 
	 * diagn&oacute;stico asociado a la orden ambulatoria
	 * @param Connection con, int codigoOrden
	 * @return DtoDiagnostico
	 * @author Diana Carolina G
	 */
	public static DtoDiagnostico consultarDiagnosticoOrdenAmbulatoria(Connection con, int codigoOrdenAmbulatoria){
		
		String consulta="select acronimo_diagnostico as acronimo, tipo_cie_diagnostico as tipo_cie from ordenes.ordenes_ambulatorias where codigo=?";
		DtoDiagnostico dto= new DtoDiagnostico();
		try{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoOrdenAmbulatoria);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setAcronimoDiagnostico(rs.getString("acronimo"));
				dto.setTipoCieDiagnostico(String.valueOf(rs.getInt("tipo_cie")));
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			logger.error("error",e);
		}
		return dto;
	}
	
	/**
	 * Método que consulta los resultados asociados a las órdenes ambulatorias.
	 * 
	 * @param con
	 * @param numeroOrden
	 * 
	 * @return resultado
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String consultarResultadoOrdenesAmbulatorias(Connection con, String numeroOrden, String cadenaConsultaResultadoOrdenes) 
	{
		String resultadoOrden = "";
		try {
			logger.info("\n\n\nconsultarResultadoOrdenesAmbulatorias-->"+cadenaConsultaResultadoOrdenes+"---numeroOrden--->"+numeroOrden);

			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaResultadoOrdenes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,numeroOrden);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next()) {
				resultadoOrden = rs.getString("resultado");
			}
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseOrdenesAmbulatoriasDao[consultarResultadoOrdenesAmbulatorias] -->");
			e.printStackTrace();
		}
		return resultadoOrden;
	}

	/**
	 * Consulta el detalle de los articulos de la orden ambulatoria
	 * @author javrammo
	 * @param con
	 * @param codigoOrden
	 * @return
	 * @throws Exception 
	 */

	public static List<InfoArticuloOrdenAmbulatoriaDto> consultarResultadoOrdenesAmbulatorias(Connection con, int codigoOrden, boolean esMedicamento) throws IPSException {
		
		
		List<InfoArticuloOrdenAmbulatoriaDto> detalleArticulos = new ArrayList<InfoArticuloOrdenAmbulatoriaDto>();
		
		String consulta = "SELECT a.codigo AS codigo, "+
		"a.codigo_interfaz AS codigoInterfaz, "+ 
		"trim(a.descripcion)     AS descripcion, "+ 
		"ff.nombre  AS nombreFF, "+
		"a.concentracion   AS concentracion, "+  
		"um.nombre  AS nombreUM, "+ 
		"na.es_medicamento AS es_medicamento "+
		"FROM ordenes.det_orden_amb_articulo doar "+
		"INNER JOIN ordenes_ambulatorias oa "+
		"ON(oa.codigo=doar.codigo_orden) "+
		"INNER JOIN inventarios.articulo a "+
		"ON (doar.articulo = a.codigo) "+
		"LEFT JOIN inventarios.naturaleza_articulo na "+
		"ON (na.acronimo=a.naturaleza) "+
		"LEFT JOIN inventarios.forma_farmaceutica ff "+
		"ON (a.forma_farmaceutica=ff.acronimo) "+
		"LEFT JOIN inventarios.unidad_medida um "+
		"ON (a.unidad_medida    =um.acronimo) "+
		"WHERE doar.codigo_orden= ? and na.es_medicamento = ? ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement(consulta);
			ps.setInt (1,  codigoOrden);
			ps.setString(2,  esMedicamento ? ConstantesBD.acronimoSi : ConstantesBD.acronimoNo);
			
			rs = ps.executeQuery();			
			while(rs.next()){
				
				InfoArticuloOrdenAmbulatoriaDto info = new InfoArticuloOrdenAmbulatoriaDto();
				info.setCodigoAxioma(rs.getString("codigo"));
				info.setCodigoInterfaz(rs.getString("codigoInterfaz"));
				info.setDescripion(rs.getString("descripcion"));
				info.setFormaFarmaceutica(rs.getString("nombreFF"));
				info.setConcentracion(rs.getString("concentracion"));
				info.setUnidadDeMedida(rs.getString("nombreUM"));
				String tipoArticulo = rs.getString("es_medicamento");
				info.setMedicamento(ConstantesBD.acronimoSi.equals(tipoArticulo) ? true : false);				
				detalleArticulos.add(info);
			}
			
		} catch (SQLException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}finally {
			if(ps != null){
				try {
					ps.close();
				} catch (Exception e) {}				
			}
			if(rs != null){
				try {
					rs.close();
				} catch (Exception e) {}
			}
		}

		
		return detalleArticulos;
	}
	
	
}