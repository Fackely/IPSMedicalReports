/*
 * Creado el 17-dic-2005
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class SqlBasePresupuestoPacienteDao
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBasePreanestesiaDao.class);

		/**
	 * Método que consulta los convenios que están activos y tiene un contrato vigente a la fecha actual
	 * @param con
	 * @return Collection con los convenios vigentes
	 */
	public static Collection consultarConveniosVigentes (Connection con, boolean cargarConvenio)
	{
		
		String consultaStr="";
		
		if(!cargarConvenio)
		{
				consultaStr=	"SELECT distinct " +
								"co.codigo AS codigo_contrato, "+
								"con.codigo AS codigo_convenio, "+
								"con.nombre AS nombre_convenio "+
							"FROM "+
								"convenios con "+
								"INNER JOIN contratos co ON (con.codigo=co.convenio) "+
							"WHERE "+
								"co.fecha_inicial<=CURRENT_DATE AND co.fecha_final>=CURRENT_DATE "+
								"AND con.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' "+
								"ORDER BY con.nombre ";
		
		}
		else
		{
			consultaStr="SELECT " +
							"con.codigo AS codigo_convenio,  " +
							"con.nombre AS nombre_convenio " +
						"FROM " +
							"convenios con  " +
							"INNER JOIN contratos co ON (con.codigo=co.convenio) " +
						"WHERE " +
							"co.fecha_inicial<=CURRENT_DATE " +
							"AND co.fecha_final>=CURRENT_DATE " +
							"AND con.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +
							"group by con.codigo, con.nombre ORDER BY con.nombre";
			
		}
				
		try
			{
				PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
		 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
			} 
		catch (SQLException e)
			{
				logger.error("Error en la consulta de los convenios activos vigentes: SqlBasePresupuestoPacienteDao : "+e.toString());
				return null;
			}
	}
	

	/**
	 * Metodo para insertar la cotizacion de un paciente 
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valor
	 */
	public static int  insertarMateriales(Connection con, int consecutivoCotizacion, int codigoArticulo, int cantidad, float valor,int esquemaTarifario)
	{
		PreparedStatementDecorator ps;
		int resp=0; 
		String consultaStr = " INSERT INTO presupuesto_articulos (presupuesto, articulo, cantidad, valor_unitario,esquema_tarifario)  " +
						  	 "			                  VALUES ( ?, ?, ?, ?,? ) ";
		try{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, consecutivoCotizacion);
				ps.setInt(2, codigoArticulo);
				ps.setInt(3, cantidad);
				ps.setFloat(4, valor);
				ps.setInt(5, esquemaTarifario);
				resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(" Error en la inserción de Materiales  : SqlBasePresupuestoPacienteDao "+e.toString() );
				resp = -2;
		}
		return resp;

	}
	
	/**
	 * Mètodo que inserta la informaciòn básica del presupuesto del paciente
	 * @param con
	 * @param consecutivoPresupuesto
	 * @param codigoPersona
	 * @param convenio
	 * @param medicoTratante
	 * @param diagnosticoIntervencion
	 * @param cieDiagnostico
	 * @param loginUsuario
	 * @param centroAtencion
	 * @return consecutivoPresupuesto
	 */	
	public static int insertarPresupuesto (Connection con, int consecutivoPresupuesto, int codigoPersona, int convenio, 
																int medicoTratante, String diagnosticoIntervencion, int cieDiagnostico, String loginUsuario, int centroAtencion, int contrato, String paquete)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO presupuesto_paciente ( consecutivo, " +
																												  "paciente, " +
																												  "convenio, " +
																												  "medico_tratante," +
																												  "diagnost_intervencion," +
																												  "tipo_cie," +
																												  "fecha_presupuesto," +
																												  "hora_presupuesto," +
																												  "login_usuario," +
																												  "centro_atencion," +
																												  "contrato," +
																												  "paquete) " +
																												  " VALUES " +
																												  " (?, ?, ?, ?, ?, ?, CURRENT_DATE,'"+UtilidadFecha.getHoraActual()+"', ?, ?, ?, ?) ";
		
		
		try	
			{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, consecutivoPresupuesto);
						ps.setInt(2, codigoPersona);
						ps.setInt(3, convenio);
						ps.setInt(4, medicoTratante);
						ps.setString(5, diagnosticoIntervencion);
						ps.setInt(6,cieDiagnostico);
						ps.setString(7,loginUsuario);
						ps.setInt(8, centroAtencion);
						ps.setInt(9, contrato);
						if(paquete.equals(""))
						{
							paquete=" ";
						}
						ps.setString(10, paquete);
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = consecutivoPresupuesto;
						}
						logger.info("\n\nCadena Presupuesto  >> : "+insertarStr );
						logger.info(" \nConsecutivo>> : "+consecutivoPresupuesto+" " +
								"codPersona>> "+codigoPersona+"convenio >> "+convenio+" " +
								" medicoTrat >> "+medicoTratante+" diagnosInterven >>"+diagnosticoIntervencion+
								" cieDiagnos >> "+cieDiagnostico+" loginUsuario >> "+loginUsuario+"CentroAten >> "+centroAtencion+" " +
								" contrato >>"+contrato+" paqute >> "+paquete);
				}
				catch(SQLException e)
				{
					logger.info("\n\nCadena Presupuesto  >> : "+insertarStr );
					logger.info(" \nConsecutivo>> : "+consecutivoPresupuesto+" " +
							"codPersona>> "+codigoPersona+"convenio >> "+convenio+" " +
							" medicoTrat >> "+medicoTratante+" diagnosInterven >>"+diagnosticoIntervencion+
							" cieDiagnos >> "+cieDiagnostico+" loginUsuario >> "+loginUsuario+"CentroAten >> "+centroAtencion+" " +
							" contrato >>"+contrato+" paqute >> "+paquete );
						logger.warn(e+" Error en la inserción de datos de la información básica del presupuesto : insertarPresupuesto "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Método para cargar la información básica del presupuesto
	 * @param con
	 * @param consecutivoPresupuesto
	 * @return Collection con la informaciòn del presupuesto
	 */
	public static Collection cargarInfoPresupuesto (Connection con, int consecutivoPresupuesto)
	{
		String consultaStr="SELECT getnombreconvenio(pp.convenio) AS nombre_convenio," +
															"getnombrepersona(pp.medico_tratante) AS nombre_medico," +
															"getnumerocontrato(pp.contrato) AS numero_contrato," +
															"getdescripcionpaquete(pc.paquete,pc.institucion) AS nombre_paquete," +
															"getnombrediagnostico(pp.diagnost_intervencion,pp.tipo_cie) AS nombre_diagnostico " +
																" FROM presupuesto_paciente pp LEFT OUTER JOIN paquetes_convenio pc ON(pc.paquete=pp.paquete)" +
																"		WHERE consecutivo = ?";
		
		try
		{
		PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		psConsulta.setInt(1, consecutivoPresupuesto);
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
		logger.error("Error en la consulta del presupuesto: SqlBasePresupuestoPacienteDao : "+e.toString());
		return null;
		}
	}


	/**
	 * Metodo para insertar los materiales del presupuesto 
	 * @param con
	 * @param consecutivoPresupuesto
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valor
	 * @return
	 */
	public static int insertarServicios(Connection con, int consecutivoPresupuesto, int codigoServicio, int cantidad, float valor,int esquemaTarifario)
	{
		PreparedStatementDecorator ps;
		int resp=0; 
		String consultaStr = " INSERT INTO presupuesto_servicios (presupuesto, servicio, cantidad, valor_unitario,esquema_tarifario)  " +
						  	 "			                  VALUES ( ?, ?, ?, ?, ? ) ";
		try{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, consecutivoPresupuesto);
				ps.setInt(2, codigoServicio);
				ps.setInt(3, cantidad);
				ps.setFloat(4, valor);
				ps.setInt(5, esquemaTarifario);
				
				resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(" Error en la inserción de Servicios  : SqlBasePresupuestoPacienteDao "+e.toString() );
				resp = -2;
		}
		return resp;
	}
	
	/**
	 * Método para insertar los servicios de intervención del presupuesto
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoServicio
	 * @return
	 */
	public static int insertarServiciosIntervencion(Connection con, int consecutivoCotizacion, int codigoServicio)
	{
		PreparedStatementDecorator ps;
		int resp=0; 
		String consultaStr = " INSERT INTO presupuesto_intervencion (presupuesto, servicio_intervencion)  " +
						  	 "			                  VALUES ( ?, ?) ";
		try{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, consecutivoCotizacion);
				ps.setInt(2, codigoServicio);
				
				resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(" Error en la inserción de Servicios de Intervencion  : SqlBasePresupuestoPacienteDao "+e.toString() );
				resp = -2;
		}
		return resp;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @return
	 */
	public static HashMap cargarServiciosPaquetes(Connection con, String codigoPaquete, int convenio, int contrato) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		int tarifario=ConstantesBD.codigoNuncaValido;
		String consulta="";
		try
		{
			consulta=" 0 as valor_unitario";

		
			String cadena="SELECT " +
									" s.codigo as codigo_servicio," +
									" getnombreservicio(s.codigo,0) as descripcion_servicio," +
									consulta+
									" pgs.cantidad as cantidad_ser " +
								" from servicios s, paq_agrupacion_servicios pgs " +
								" where pgs.codigo_paquete=?  and s.tipo_servicio not in('"+ConstantesBD.codigoServicioPaquetes+"') and ((pgs.grupo_servicio=s.grupo_servicio and pgs.tipo_servicio is null and pgs.especialidad is null) or (pgs.grupo_servicio=s.grupo_servicio and pgs.tipo_servicio=s.tipo_servicio and pgs.especialidad is null) or (pgs.grupo_servicio=s.grupo_servicio and pgs.tipo_servicio=s.tipo_servicio and pgs.especialidad=s.especialidad) or (pgs.grupo_servicio is null and pgs.tipo_servicio=s.tipo_servicio and pgs.especialidad=s.especialidad) or (pgs.grupo_servicio is null and pgs.tipo_servicio is null and pgs.especialidad=s.especialidad) or (pgs.grupo_servicio is null and pgs.tipo_servicio=s.tipo_servicio and pgs.especialidad is null))"+
								" UNION SELECT " + 
									" s.codigo as codigo_servicio," +
									" getnombreservicio(s.codigo,0) as descripcion_servicio,"+
									consulta+
									" pcs.cantidad as cantidad_ser" +
								" from servicios s " +
									" inner join paq_comp_servicios pcs on(pcs.codigo_servicio=s.codigo) " + 
									" LEFT OUTER JOIN tarifas_soat ts ON (ts.servicio=s.codigo) " +
									" LEFT OUTER JOIN tarifas_iss ti ON (ti.servicio=s.codigo) " +
								" where pcs.codigo_paquete=?" +
								" AND s.tipo_servicio not in('"+ConstantesBD.codigoServicioPaquetes+"')"+
								" AND ((ti.tipo_liquidacion NOT IN ("+ConstantesBD.codigoTipoLiquidacionSoatGrupo+","+ConstantesBD.codigoTipoLiquidacionSoatUvr+"))"+
								" OR (ts.tipo_liquidacion NOT IN ("+ConstantesBD.codigoTipoLiquidacionSoatGrupo+","+ConstantesBD.codigoTipoLiquidacionSoatUvr+")))";
			
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setString(2, codigoPaquete);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				mapa.put("valor_unitario_"+i, mapa.get("valorUnitario_"+i));
				mapa.remove("valorUnitario_"+i);
			}
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @return
	 */
	public static HashMap cargarArticulosPaquetes(Connection con, String codigoPaquete) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		
		try
		{
		
			String cadena="SELECT " +
									" va.codigo as codigo_articulo," +
									" va.descripcion as  descripcion_articulo," +
									" coalesce(ti.valor_tarifa,0) as valor_unitario_resultados, " +
									" pga.cantidad as cantidad, " +
									" coalesce(ti.valor_tarifa*pga.cantidad,0) as valor_total_articulo " +
								" from view_articulos va, paq_agrupacion_articulos pga, tarifas_inventario ti " +
								" where pga.codigo_paquete=? and (ti.articulo=va.codigo) and ((pga.clase=va.clase and  pga.grupo is null and pga.subgrupo is null and pga.naturaleza is null) or (pga.clase=va.clase and pga.grupo=va.grupo and pga.subgrupo is null and pga.naturaleza is null) or (pga.clase=va.clase and pga.grupo=va.grupo and pga.subgrupo=va.subgrupo and pga.naturaleza is null) or (pga.clase=va.clase and pga.grupo=va.grupo and pga.subgrupo=va.subgrupo and pga.naturaleza=va.naturaleza) or (pga.clase is null and  pga.grupo=va.grupo and pga.subgrupo is null and  pga.naturaleza is null) or (pga.clase is null and pga.grupo=va.grupo and pga.subgrupo=va.subgrupo and pga.naturaleza is null) or (pga.clase is null and pga.grupo=va.grupo and pga.subgrupo=va.subgrupo and pga.naturaleza=va.naturaleza) or (pga.clase is null and pga.grupo is null and pga.subgrupo=va.subgrupo and pga.naturaleza is null) or (pga.clase is null and pga.grupo is null and pga.subgrupo=va.subgrupo and pga.naturaleza=va.naturaleza) or (pga.clase=va.clase and pga.grupo is null and pga.subgrupo=va.subgrupo and pga.naturaleza is null) or (pga.clase is null and pga.grupo is null and pga.subgrupo is null and pga.naturaleza=va.naturaleza) or (pga.clase=va.clase and pga.grupo is null and pga.subgrupo is null and pga.naturaleza=va.naturaleza) or (pga.clase is null and pga.grupo=va.grupo and pga.subgrupo is null and pga.naturaleza=va.naturaleza) or (pga.clase=va.clase and pga.grupo=va.grupo and pga.subgrupo is null and pga.naturaleza=va.naturaleza) or (pga.clase=va.clase and pga.grupo is null and pga.subgrupo=va.subgrupo and pga.naturaleza=va.naturaleza))"+
								" UNION SELECT " + 
									" va.codigo as codigo_articulo," +
									" va.descripcion as descripcion_articulo,"+
									" coalesce(ti.valor_tarifa,0) as valor_unitario_resultados,"+
									" pca.cantidad as cantidad," +
									" coalesce(ti.valor_tarifa*pca.cantidad,0) as valor_total_articulo " +
								" from view_articulos va " +
									" inner join paq_comp_articulos pca on(pca.codigo_articulo=va.codigo) " +
									" left join tarifas_inventario ti on(ti.articulo=va.codigo) " +
								" where pca.codigo_paquete=?";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setString(2, codigoPaquete);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param convenio 
	 * @param servicios
	 * @return
	 */
	public static HashMap obtenerPaquetesValidos(Connection con, int convenio, String servicios) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
		
			String cadena="SELECT DISTINCT pcs.codigo_paquete,getdescripcionpaquete(pcs.codigo_paquete,pcs.institucion) from paq_comp_servicios pcs inner join paquetes_convenio pc on(pc.paquete=codigo_paquete and pc.institucion=pcs.institucion and pc.convenio=?) where principal='"+ConstantesBD.acronimoSi+"' and codigo_servicio in("+servicios+")";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, convenio);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Método que consulta los presupuesto de un paciente para unos convenios
	 * determinados cuando no tengan ingreso definido
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerPrespuestosSinIngreso(Connection con,HashMap campos)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros","0");
		try
		{
			String consulta = "SELECT "+
				"pp.consecutivo AS consecutivo, "+                               
				"getnombreconvenio(pp.convenio) AS convenio, "+
				"to_char(pp.fecha_presupuesto,'"+ConstantesBD.formatoFechaAp+"') AS fecha_presupuesto, "+
				"substr(pp.hora_presupuesto,0,6) AS hora_presupuesto "+ 
				"FROM presupuesto_paciente pp "+ 
				"WHERE " +
				"pp.paciente = "+campos.get("codigoPaciente")+" AND " +
				"pp.convenio IN ("+campos.get("listadoConvenios")+") AND " +
				"pp.ingreso IS NULL";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPrespuestosSinIngreso: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que actualiza el ingreso de un prespuesto
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarIngreso(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "UPDATE presupuesto_paciente SET ingreso = "+campos.get("idIngreso")+" WHERE consecutivo = "+campos.get("consecutivo");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			return st.executeUpdate(consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarIngreso: "+e);
			return 0;
		}
	}
	
	
	/**
	 * Método implementado para obtener el presupuesto de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerPresupuestoXIngreso(Connection con,HashMap campos)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros","0");
		try
		{
			String consulta = "SELECT "+ 
				"pp.consecutivo AS consecutivo, "+
				"getnombreconvenio(pp.convenio) AS convenio, "+
				"to_char(pp.fecha_presupuesto,'"+ConstantesBD.formatoFechaAp+"') AS fecha_presupuesto, "+
				"substr(pp.hora_presupuesto,0,6) AS hora_presupuesto "+ 
				"FROM presupuesto_paciente pp "+ 
				"WHERE pp.ingreso = "+campos.get("idIngreso");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), false, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPresupuestoXIngreso: "+e);
		}
		return resultados;
	}
	
	/**
	 * Metodo para la consulta de los contratos segun el codigo del convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static HashMap consultarContratos(Connection con,int convenio)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String consulta="SELECT codigo,numero_contrato FROM contratos WHERE convenio="+convenio;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	
	

	
}
