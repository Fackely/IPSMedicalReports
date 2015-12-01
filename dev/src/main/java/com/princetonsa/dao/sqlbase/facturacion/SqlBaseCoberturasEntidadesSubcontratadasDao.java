package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.Escalas;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */


public class SqlBaseCoberturasEntidadesSubcontratadasDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCoberturasEntidadesSubcontratadasDao.class);
	
	private static String consultaCoberturasEntiSub="SELECT cobes.consecutivo, " +
													"cobes.cobertura, " +
													"c.descrip_cobertura AS descCobertura, " +
													"cobes.nro_prioridad AS prioridad, " +
													"cobes.activo, " +
													"cobes.institucion, " +
													"es.razon_social AS nomEntiSub, " +
													"cones.numero_contrato AS numContrato " +
													"FROM facturacion.contratos_entidades_sub cones " +
													"LEFT OUTER JOIN facturacion.coberturas_entidad_sub cobes ON(cobes.contrato_entidad_sub=cones.consecutivo) " +
													"LEFT OUTER JOIN facturacion.entidades_subcontratadas es ON(es.codigo_pk= cones.entidad_subcontratada) " +
													"LEFT OUTER JOIN facturacion.cobertura c ON ((c.codigo_cobertura= cobes.cobertura) AND (c.institucion= cobes.institucion)) " +
													"WHERE cones.entidad_subcontratada=? AND cones.consecutivo=? ";
	
	private static String consultaExcCoberturas="SELECT ec.consecutivo, " +
												"ec.contrato_entidad_sub AS contratoEntSub, " +
												"ec.via_ingreso AS via_ingreso, " +
												"ec.tipo_paciente As tipo_paciente, " +
												"ec.naturaleza, " +
												"ec.activo, " +
												"ec.institucion, " +
												"vi.nombre AS desc_via_ingreso, " +
												"tp.nombre AS desc_tipo_paciente, " +
												"n.nombre AS desc_naturaleza " +
												"FROM facturacion.ex_coberturas_entidad_sub ec " +
												"LEFT OUTER JOIN facturacion.contratos_entidades_sub ces ON(ec.contrato_entidad_sub=ces.consecutivo) " +
												"LEFT OUTER JOIN manejopaciente.vias_ingreso vi ON(vi.codigo= ec.via_ingreso) " +
												"LEFT OUTER JOIN manejopaciente.tipos_paciente tp ON(tp.acronimo= ec.tipo_paciente) " +
												"LEFT OUTER JOIN manejopaciente.naturaleza_pacientes n ON(n.codigo= ec.naturaleza) " +
												"WHERE ces.entidad_subcontratada=? AND ces.consecutivo=? " +
												"ORDER BY desc_via_ingreso";
	
	private static String guardarNuevoRegistroExCobertura= "INSERT INTO facturacion.ex_coberturas_entidad_sub " +
															"(consecutivo, " +
															"contrato_entidad_sub, " +
															"via_ingreso, " +
															"tipo_paciente, " +
															"naturaleza, " +
															"usuario_modifica, " +
															"fecha_modifica, " +
															"hora_modifica, " +
															"activo, " +
															"institucion) " +
															"VALUES(?,?,?,?,?,?,CURRENT_DATE,?,'S', ?) ";
	
	private static String guardarNuevoRegistroCobertura= "INSERT INTO facturacion.coberturas_entidad_sub " +
															"(consecutivo, " +
															"contrato_entidad_sub, " +
															"cobertura, " +
															"institucion, " +
															"nro_prioridad, " +
															"usuario_modifica, " +
															"fecha_modifica, " +
															"hora_modifica, " +
															"activo) " +
															"VALUES(?,?,?,?,?,?,CURRENT_DATE,?,?) ";	
	
	private static String eliminarRegistroCobertura= "UPDATE facturacion.coberturas_entidad_sub " +
														"SET activo=?," +
														"fecha_inactivacion=CURRENT_DATE, " +
														"hora_inactivacion=?, " +
														"usuario_inactivacion=? " +
														"WHERE consecutivo=? ";
	
	private static String eliminarRegistroExCobertura="UPDATE facturacion.ex_coberturas_entidad_sub " +
														"SET activo=?, " +
														"fecha_inactivacion=CURRENT_DATE, " +
														"hora_inactivacion=?, " +
														"usuario_inactivacion=? " +
														"WHERE consecutivo=? ";
	
	private static String consultaClasesInventarios="SELECT codigo, nombre " +
														"FROM inventarios.clase_inventario ";
	
	private static String consultaGrupoInventario="SELECT codigo, nombre " +
													"FROM inventarios.grupo_inventario " +
													"WHERE clase=? ";
	
	private static String consultaSubGrupoInventario="SELECT codigo, nombre, subgrupo, grupo, clase " +
														"FROM inventarios.subgrupo_inventario " +
														"WHERE grupo=? AND clase=? ";
	
	private static String consultaGruposServicios="SELECT codigo, descripcion " +
													"FROM facturacion.grupos_servicios ";
	
	private static String consultaTiposServicio="SELECT acronimo, nombre " +
													"FROM facturacion.tipos_servicio ";
	
	private static String consultaEspecialidades= "SELECT codigo, nombre " +
													"FROM administracion.especialidades ";
	
	private static String guardarAgruArtiExCober= "INSERT INTO facturacion.ex_cober_agru_art_ent_sub " +
													"(consecutivo, ex_cobertura_entidad, clase_inventario, " +
													"grupo_inventario, naturaleza, subgrupo_inventario, " +
													"institucion, incluye, fecha_modifica, hora_modifica, " +
													"usuario_modifica, activo) " +
													"VALUES (?,?,?,?,?,?,?,?,CURRENT_DATE,?,?,?) ";
	
	private static String consultaAgruArtiEntiSub="SELECT ca.consecutivo, " +
													"ca.ex_cobertura_entidad, " +
													"ca.clase_inventario, " +
													"ca.grupo_inventario, " +
													"ca.naturaleza, " +
													"ca.subgrupo_inventario, " +
													"getintegridaddominio(ca.activo) AS activo, " +
													"getintegridaddominio(ca.incluye) AS incluye, " +
													"ci.nombre AS nom_inventario, " +
													"gi.nombre AS nom_grupo, " +
													"na.nombre AS nom_naturaleza, " +
													"sgi.nombre AS nom_subgrupo " +
													"FROM facturacion.ex_cober_agru_art_ent_sub ca " +
													"LEFT OUTER JOIN inventarios.clase_inventario ci ON(ci.codigo= ca.clase_inventario) " +
													"LEFT OUTER JOIN inventarios.grupo_inventario gi ON((gi.codigo= ca.grupo_inventario) AND (gi.clase=ca.clase_inventario)) " +
													"LEFT OUTER JOIN inventarios.naturaleza_articulo na ON((na.acronimo= ca.naturaleza) AND na.institucion= ca.institucion) " +
													"LEFT OUTER JOIN inventarios.subgrupo_inventario sgi ON((ca.subgrupo_inventario= sgi.subgrupo) AND (ca.grupo_inventario= sgi.grupo) AND (ca.clase_inventario= sgi.clase)) " +
													"WHERE ca.ex_cobertura_entidad=? ";
	
	private static String guardarExCoberArtiEntiSub= "INSERT INTO facturacion.ex_cober_art_ent_sub " +
													"(consecutivo, ex_cober_entidad_sub, articulo, incluye, " +
													"fecha_modifica, hora_modifica, usuario_modifica, activo) " +
													"VALUES(?,?,?,?,CURRENT_DATE,?,?,?) ";
		
	private static String guardarAgruServExCober="INSERT INTO facturacion.ex_cober_agru_ser_ent_sub " +
													"(consecutivo, ex_cober_entidad_sub, pos, pos_subsidiado, " +
													"grupo, tipo_servicio, especialidad, incluye, fecha_modifica, " +
													"hora_modifica, usuario_modifica, activo) " +
													"VALUES (?,?,?,?,?,?,?,?,CURRENT_DATE,?,?,?) ";
	
	private static String consultaAgruServEntiSub="SELECT ca.consecutivo, " +
													"ca.ex_cober_entidad_sub, " +
													"getintegridaddominio(ca.pos) AS pos, " +
													"ca.pos AS pos_acronimo, " +
													"ca.pos_subsidiado AS pos_subsidiado_acronimo, " +
													"getintegridaddominio(ca.pos_subsidiado) AS pos_subsidiado, " +
													"ca.grupo, " +
													"ca.tipo_servicio, " +
													"ca.especialidad, " +
													"getintegridaddominio(ca.incluye) AS incluye, " +
													"ca.incluye AS incluye_acronimo, " +
													"getintegridaddominio(ca.activo) AS activo, " +
													"gs.descripcion AS nom_grupo_servicio, " +
													"ts.nombre AS nom_tipo_servicio, " +
													"e.nombre AS nom_especialidad " +
													"FROM facturacion.ex_cober_agru_ser_ent_sub ca " +
													"LEFT OUTER JOIN facturacion.grupos_servicios gs ON(ca.grupo= gs.codigo) " +
													"LEFT OUTER JOIN facturacion.tipos_servicio ts ON(ts.acronimo= ca.tipo_servicio) " +
													"LEFT OUTER JOIN administracion.especialidades e ON(e.codigo= ca.especialidad) " +
													"WHERE ca.ex_cober_entidad_sub=? ";
	
	private static String guardarExCoberServEntiSub= "INSERT INTO facturacion.ex_cober_ser_ent_sub " +
														"(consecutivo, ex_cober_entidad_sub, servicio, " +
														"incluye, fecha_modifica, hora_modifica, " +
														"usuario_modifica, activo) " +
														"VALUES (?,?,?,?,CURRENT_DATE,?,?,?,) "; 
			
	private static String modificarRegistroCobertura= "UPDATE facturacion.coberturas_entidad_sub SET " +
														"contrato_entidad_sub=?, " +
														"cobertura=?, " +
														"institucion=?, " +
														"nro_prioridad=?, " +
														"usuario_modifica=?, " +
														"fecha_modifica=CURRENT_DATE, " +
														"hora_modifica=?, " +
														"activo=? " +
														"WHERE consecutivo=? ";

	private static String guardarRegCoberturaLog= "INSERT INTO facturacion.log_coberturas_ent_sub " +
													"(consecutivo, cobertura_entidad_sub, cobertura, " +
													"institucion, nro_prioridad, usuario_modifica, " +
													"fecha_modifica, hora_modifica) " +
													"VALUES(?,?,?,?,?,?,CURRENT_DATE,?) "; 

	private static String eliminarArtiAgru= "UPDATE facturacion.ex_cober_agru_art_ent_sub " +
											"SET activo=?, " +
											"fecha_inactivacion=CURRENT_DATE, " +
											"hora_inactivacion=?, " +
											"usuario_inactivacion=? " +
											"WHERE consecutivo=? ";
	
	private static String eliminarServAgru= "UPDATE facturacion.ex_cober_agru_ser_ent_sub " +
												"SET activo=?, " +
												"fecha_inactivacion=CURRENT_DATE, " +
												"hora_inactivacion=?, " +
												"usuario_inactivacion=? " +
												"WHERE consecutivo=? ";
	
	private static String guardarServEsp= "INSERT INTO facturacion.ex_cober_ser_ent_sub " +
											"(consecutivo, ex_cober_entidad_sub, servicio, " +
											"incluye, fecha_modifica, hora_modifica, " +
											"usuario_modifica, activo) " +
											"VALUES(?,?,?,?,CURRENT_DATE,?,?,?) ";
	
	private static String eliminarServEsp= "UPDATE facturacion.ex_cober_ser_ent_sub " +
											"SET activo=?, " +
											"fecha_inactivacion=CURRENT_DATE, " +
											"hora_inactivacion=?, " +
											"usuario_inactivacion=? " +
											"WHERE consecutivo=? ";
	
	private static String guardarArtiEsp= "INSERT INTO facturacion.ex_cober_art_ent_sub " +
											"(consecutivo, ex_cober_entidad_sub, articulo, " +
											"incluye, fecha_modifica, hora_modifica, " +
											"usuario_modifica, activo) " +
											"VALUES(?,?,?,?,CURRENT_DATE,?,?,?) ";
	
	private static String eliminarArtiEsp="UPDATE facturacion.ex_cober_art_ent_sub " +
											"SET activo=?, " +
											"fecha_inactivacion=CURRENT_DATE, " +
											"hora_inactivacion=?, " +
											"usuario_inactivacion=? " +
											"WHERE consecutivo=? ";
	
	private static String modificarRegExCober="UPDATE facturacion.ex_coberturas_entidad_sub " +
												"SET via_ingreso=?, " +
												"tipo_paciente=?, " +
												"naturaleza=?, " +
												"usuario_modifica=?, " +
												"fecha_modifica= CURRENT_DATE, " +
												"hora_modifica=? " +
												"WHERE consecutivo=? ";
	
	private static String guardarRegExCoberturaLog="INSERT INTO log_ex_coberturas_ent_sub " +
													"(consecutivo, ex_cobertura_entidad_sub, " +
													"via_ingreso, tipo_paciente, " +
													"naturaleza, usuario_modifica, " +
													"fecha_modifica, hora_modifica) " +
													"VALUES (?,?,?,?,?,?,CURRENT_DATE,?) ";
	
	private static String consultaArtiEsp= "SELECT ca.articulo AS codigo_articulo, " +
											"ca.incluye AS check_a_e, " +
											"ca.activo, " +
											"a.descripcion AS descripcion_articulo, " +
											"ca.consecutivo " +
											"FROM facturacion.ex_cober_art_ent_sub ca " +
											"INNER JOIN inventarios.articulo a ON(a.codigo=ca.articulo) " +
											"WHERE ca.ex_cober_entidad_sub=? ";
	
	private static String consultaServEsp= "SELECT cs.servicio AS cod_servicio, " +
											"cs.incluye AS check_s_e, " +
											"cs.activo, " +
											"cs.consecutivo, " +
											"getnombreservicio(servicio, "+0+") AS servicio_desc, " +
											"getcodigocups(servicio, "+0+") AS codigo_cups " +
											"FROM facturacion.ex_cober_ser_ent_sub cs " +
											"WHERE cs.ex_cober_entidad_sub=? ";
	
	
	
	/**
	 * Metodo para consultar los servicios especificos
	 *  agrupados
	 * @return
	 */
	public static HashMap consultaServEsp(int exCoberEntSub)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nEX COBER ENT SUB----->"+exCoberEntSub+"\n\nCONSULTA SERVICIOS ESPECIFICOS----->>>>>>>>>>>"+consultaServEsp);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaServEsp, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, exCoberEntSub);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO SERVICIOS ESPECIFICOS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar los articulos especificos
	 *  agrupados
	 * @return
	 */
	public static HashMap consultaArtiEsp(int exCoberEntSub)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nEX COBER ENT SUB----->"+exCoberEntSub+"\n\nCONSULTA ARTICULOS ESPECIFICOS----->>>>>>>>>>>"+consultaArtiEsp);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaArtiEsp, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, exCoberEntSub);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ARTICULOS ESPECIFICOS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para insertar un registro en el log de excepciones de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public static boolean guardarRegExCoberturaLog(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarRegExCoberturaLog, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_log_ex_coberturas_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("CoberEnti")+""));
			if(Utilidades.convertirAEntero(criterios.get("viaIngreso")+"") > 0)
				ps.setInt(3, Utilidades.convertirAEntero(criterios.get("viaIngreso")+""));
			else
				ps.setNull(3, Types.INTEGER);
			if(!(criterios.get("tipoPaciente")+"").equals("-1"))
				ps.setString(4, criterios.get("tipoPaciente")+"");
			else
				ps.setNull(4, Types.VARCHAR);
			if(Utilidades.convertirAEntero(criterios.get("naturaleza")+"") > 0)
				ps.setInt(5, Utilidades.convertirAEntero(criterios.get("naturaleza")+""));
			else
				ps.setNull(5, Types.INTEGER);
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, UtilidadFecha.getHoraActual());
			
			logger.info("\n\nCRITERIOS------->"+criterios+"\n\nINSERTANDO NUEVO REGISTRO LOG DE EXCEPCION COBERTURA----->>>>>>>>>>>"+guardarRegExCoberturaLog);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO LOG DE EXCEPCION COBERTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
		
	/**
	 * Metodo para actualizar un registro de excepcion cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public static int modificarRegExCober (HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nACTUALIZANDO REGISTRO EXCEPCION COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+modificarRegExCober);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarRegExCober, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
				
			if(Utilidades.convertirAEntero(criterios.get("viaIngreso")+"") > 0)
				ps.setInt(1, Utilidades.convertirAEntero(criterios.get("viaIngreso")+""));
			else
				ps.setNull(1, Types.INTEGER);
			if(!(criterios.get("tipoPaciente")+"").equals("-1"))
				ps.setString(2, criterios.get("tipoPaciente")+"");
			else
				ps.setNull(2, Types.VARCHAR);
			if(Utilidades.convertirAEntero(criterios.get("naturaleza")+"") > 0)
				ps.setInt(3, Utilidades.convertirAEntero(criterios.get("naturaleza")+""));
			else
				ps.setNull(3, Types.INTEGER);
			ps.setString(4, criterios.get("usuario")+"");		
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setInt(6, Utilidades.convertirAEntero(criterios.get("consecutivo")+""));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return Utilidades.convertirAEntero(criterios.get("consecutivo")+"");
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO REGISTRO EXCEPCION COBERTURA POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo que elimina un registro de articulo especifico
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarArtiEsp(int consecutivo, String usuario)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nCONSECUTIVO------->"+consecutivo+"\n\nUSUARIO------->"+usuario+"\n\nELIMINAR REGISTRO ARTICULO ESPECIFICO----->>>>>>>>>>"+eliminarArtiEsp);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarArtiEsp, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario);
			ps.setInt(4, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO ARTICULO ESPECIFICO------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo para insertar un registro de articulo especifico
	 * @param criterios
	 * @return
	 */
	public static int guardarArtiEsp(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarArtiEsp, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_cober_art_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("CoberEnti")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("articulo")+""));
			ps.setString(4, criterios.get("incluye")+"");
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, criterios.get("activo")+"");
			
			logger.info("\n\nCRITERIOS------>"+criterios+"\n\nINSERTANDO NUEVO REGISTRO ARTICULO ESPECIFICO----->>>>>>>>>>>"+guardarArtiEsp);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO ARTICULO ESPECIFICO--------->>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo que elimina un registro de servicio especifico
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarServEsp(int consecutivo, String usuario)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nCONSECUTIVO------->"+consecutivo+"\n\nUSUARIO------->"+usuario+"\n\nELIMINAR REGISTRO SERVICIO ESPECIFICO----->>>>>>>>>>"+eliminarServEsp);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarServEsp, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario);
			ps.setInt(4, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO SERVICIO ESPECIFICO------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo para insertar un registro de servicio especifico
	 * @param criterios
	 * @return
	 */
	public static int guardarServEsp(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarServEsp, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_cober_ser_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("CoberEnti")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("servicio")+""));
			ps.setString(4, criterios.get("incluye")+"");
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, criterios.get("activo")+"");
			
			logger.info("\n\nCRITERIOS------>"+criterios+"\n\nINSERTANDO NUEVO REGISTRO SERVICIO ESPECIFICO----->>>>>>>>>>>"+guardarServEsp);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO SERVICIO ESPECIFICO--------->>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarServAgru(int consecutivo, String usuario)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nCONSECUTIVO------->"+consecutivo+"\n\nUSUARIO------->"+usuario+"\n\nELIMINAR REGISTRO SERVICIO AGRUPADO----->>>>>>>>>>"+eliminarServAgru);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarServAgru, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario);
			ps.setInt(4, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO SERVICIO AGRUPADO------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarArtiAgru(int consecutivo, String usuario)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nCONSECUTIVO------->"+consecutivo+"\n\nUSUARIO------->"+usuario+"\n\nELIMINAR REGISTRO ARTICULO AGRUPADO----->>>>>>>>>>"+eliminarArtiAgru);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarArtiAgru, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario);
			ps.setInt(4, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO ARTICULO AGRUPADO------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo para insertar un registro en el log de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public static boolean guardarRegCoberturaLog(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarRegCoberturaLog, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_log_coberturas_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("CoberEnti")+""));
			ps.setString(3, criterios.get("cobertura")+"");
			ps.setInt(4, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			ps.setInt(5, Utilidades.convertirAEntero(criterios.get("nroPrioridad")+""));
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, UtilidadFecha.getHoraActual());
			
			logger.info("\n\nINSERTANDO NUEVO REGISTRO LOG DE COBERTURA----->>>>>>>>>>>"+guardarRegCoberturaLog);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO LOG DE COBERTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo para actualizar un registro de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public static int modificarRegistroCobertura (HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nACTUALIZANDO REGISTRO COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+modificarRegistroCobertura);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarRegistroCobertura, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
				
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("contratoEntidadSub")+""));
			ps.setString(2, criterios.get("cobertura")+"");
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			ps.setInt(4, Utilidades.convertirAEntero(criterios.get("nroPrioridad")+""));
			ps.setString(5, criterios.get("usuario")+"");			
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.setString(7, criterios.get("activo")+"");
			ps.setInt(8, Utilidades.convertirAEntero(criterios.get("consecutivo")+""));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return Utilidades.convertirAEntero(criterios.get("consecutivo")+"");
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO REGISTRO COBERTURA POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public static int guardarExCoberServEntiSub(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarExCoberServEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_cober_ser_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("exCoberEnti")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("servicio")+""));
			ps.setString(4, criterios.get("incluye")+"");
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, criterios.get("activo")+"");
			
			logger.info("\n\nINSERTANDO NUEVO REGISTRO SERVICIOS DE EXCEPCION DE COBERTURA----->>>>>>>>>>>"+guardarExCoberServEntiSub);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO SERVICIOS DE EXCEPCION DE COBERTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public static int guardarAgruServExCober(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarAgruServExCober, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_cober_agru_ser_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("exCoberEnti")+""));
			if(!(criterios.get("pos")+"").equals(""))
				ps.setString(3, criterios.get("pos")+"");
			else
				ps.setNull(3, Types.VARCHAR);
			if(!(criterios.get("posSubsidiado")+"").equals(""))
				ps.setString(4, criterios.get("posSubsidiado")+"");
			else
				ps.setNull(4, Types.VARCHAR);
			if(!(criterios.get("grupo")+"").equals(""))
				ps.setInt(5, Utilidades.convertirAEntero(criterios.get("grupo")+""));
			else
				ps.setNull(5, Types.INTEGER);
			if(!(criterios.get("tipoServicio")+"").equals(""))
				ps.setString(6, criterios.get("tipoServicio")+"");
			else
				ps.setNull(6, Types.VARCHAR);
			if(!(criterios.get("especialidad")+"").equals(""))
				ps.setInt(7, Utilidades.convertirAEntero(criterios.get("especialidad")+""));
			else
				ps.setNull(7, Types.INTEGER);
			ps.setString(8, criterios.get("incluye")+"");
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setString(10, criterios.get("usuario")+"");	
			ps.setString(11,  criterios.get("activo")+"");
			
			logger.info("\n\nCRITERIOS----->"+criterios+"\n\nINSERTANDO NUEVO REGISTRO SERVICIOS AGRUPADOS DE EXCEPCION DE COBERTURA----->>>>>>>>>>>"+guardarAgruServExCober);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO SERVICIOS AGRUPADOS DE EXCEPCION DE COBERTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo para insertar un nuevo registro Articulo de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public static int guardarExCoberArtiEntiSub(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarExCoberArtiEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_cober_art_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("exCoberEnti")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("articulo")+""));
			ps.setString(4, criterios.get("incluye")+"");
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, criterios.get("usuario")+"");	
			ps.setString(7,  criterios.get("activo")+"");
			
			logger.info("\n\nINSERTANDO NUEVO REGISTRO ARTICULO DE EXCEPCION DE COBERTURA----->>>>>>>>>>>"+guardarExCoberArtiEntiSub);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO ARTICULO DE EXCEPCION DE COBERTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo para insertar un nuevo registro Articulos Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public static int guardarAgruArtiExCober(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarAgruArtiExCober, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_cober_agru_art_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("exCoberEnti")+""));
			if(Utilidades.convertirAEntero(criterios.get("claseInv")+"") > 0)
				ps.setInt(3, Utilidades.convertirAEntero(criterios.get("claseInv")+""));
			else
				ps.setNull(3, Types.INTEGER);
			if(Utilidades.convertirAEntero(criterios.get("grupoInv")+"") > 0)
				ps.setInt(4, Utilidades.convertirAEntero(criterios.get("grupoInv")+""));
			else
				ps.setNull(4, Types.INTEGER);
			if(!(criterios.get("naturaleza")+"").equals("-1"))
				ps.setString(5, criterios.get("naturaleza")+"");
			else
				ps.setNull(5, Types.INTEGER);
			if(Utilidades.convertirAEntero(criterios.get("subGrupoInv")+"") > 0)
				ps.setInt(6, Utilidades.convertirAEntero(criterios.get("subGrupoInv")+""));
			else
				ps.setNull(6, Types.INTEGER);
			ps.setInt(7,  Utilidades.convertirAEntero(criterios.get("institucion")+""));
			ps.setString(8, (criterios.get("incluye")+""));
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setString(10, criterios.get("usuario")+"");
			ps.setString(11, criterios.get("activo")+"");
			
			logger.info("\n\nCRITERIOS--------->"+criterios+"\n\nINSERTANDO NUEVO REGISTRO ARTICULOS AGRUPADOS DE EXCEPCION DE COBERTURA----->>>>>>>>>>>"+guardarAgruArtiExCober);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO ARTICULOS AGRUPADOS DE EXCEPCION DE COBERTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}		
	
	/**
	 * Metodo para consultar los servicios
	 *  agrupados
	 * @return
	 */
	public static HashMap consultaAgruServEntiSub(int consecutivo)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSECUTIVOO--------->"+consecutivo+"\n\nCONSULTA SERVICIOS AGRUPADOS----->>>>>>>>>>>"+consultaAgruServEntiSub);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAgruServEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, consecutivo);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO SERVICIOS AGRUPADOS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar los articulos agrupados
	 * @return
	 */
	public static HashMap consultaAgruArtiEntiSub(int consecutivo)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSECUTIVO-------->"+consecutivo+"\n\nCONSULTA ARTICULOS AGRUPADOS----->>>>>>>>>>>"+consultaAgruArtiEntiSub);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAgruArtiEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, consecutivo);	
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ARTICULOS AGRUPADOS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar las especialidades definidas en el sistema
	 * @return
	 */
	public static HashMap consultaEspecialidades()
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA ESPECIALIDADES----->>>>>>>>>>>"+consultaEspecialidades);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaEspecialidades, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ESPECIALIDADES------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar los tipos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaTiposServicio()
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA TIPOS SERVICIOS----->>>>>>>>>>>"+consultaTiposServicio);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaTiposServicio, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO TIPOS SERVICIOS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar los grupos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaGruposServicios()
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA GRUPO SERVICIOS----->>>>>>>>>>>"+consultaGruposServicios);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaGruposServicios, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO GRUPO SERVICIOS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaSubGrupoInventario(String grupo, String clase)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA SUB GRUPO INVENTARIO----->>>>>>>>>>>"+consultaSubGrupoInventario);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaSubGrupoInventario, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setInt(1, Utilidades.convertirAEntero(grupo));
			ps.setInt(2, Utilidades.convertirAEntero(clase));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO SUB GRUPO INVENTARIO------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaGrupoInventario(String clase)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA GRUPO INVENTARIO----->>>>>>>>>>>"+consultaGrupoInventario);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaGrupoInventario, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setInt(1, Utilidades.convertirAEntero(clase));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO GRUPO INVENTARIO------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar las clases inventarios definidas en el sistema
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaClasesInventarios()
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA CLASES INVENTARIO----->>>>>>>>>>>"+consultaClasesInventarios);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaClasesInventarios, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
								
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CLASES INVENTARIO------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que elimina un registro de excepcion cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarRegistroExCobertura(int consecutivo, String usuario)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nELIMINAR REGISTRO EXCEPCIONES COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>"+eliminarRegistroExCobertura);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistroExCobertura, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario);
			ps.setInt(4, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO EXCEPCIONES COBERTURA POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo que elimina un registro de cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarRegistroCobertura(int consecutivo, String usuario)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nELIMINAR REGISTRO COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>"+eliminarRegistroCobertura);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistroCobertura, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario);
			ps.setInt(4, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO COBERTURA POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
		
	/**
	 * Metodo para insertar un nuevo registro de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public static int guardarNuevoRegistroCobertura(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarNuevoRegistroCobertura, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_coberturas_entidad_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("contratoEntidadSub")+""));
			ps.setString(3, criterios.get("cobertura")+"");
			ps.setInt(4, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			ps.setInt(5, Utilidades.convertirAEntero(criterios.get("nroPrioridad")+""));
			ps.setString(6, criterios.get("usuario")+"");			
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setString(8, criterios.get("activo")+"");
			
			logger.info("\n\nINSERTANDO NUEVO REGISTRO COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+guardarNuevoRegistroCobertura);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO COBERTURA POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo para insertar un nuevo registro de excepcion de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public static int guardarNuevoRegistroExCobertura(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarNuevoRegistroExCobertura, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_coberturas_entidad_sub");
			ps.setInt(1, valorseq);			
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("contratoEntidadSub")+""));			
			if(Utilidades.convertirAEntero(criterios.get("viaIngreso")+"") > 0)
				ps.setInt(3, Utilidades.convertirAEntero(criterios.get("viaIngreso")+""));
			else
				ps.setNull(3, Types.INTEGER);
			if(!(criterios.get("TipoPaciente")+"").equals("-1"))
			{
				ps.setString(4, criterios.get("TipoPaciente")+"");
			}
			else
			{
				ps.setNull(4, Types.VARCHAR);
			}
			if(Utilidades.convertirAEntero(criterios.get("naturaleza")+"") > 0)
				ps.setInt(5, Utilidades.convertirAEntero(criterios.get("naturaleza")+""));
			else
				ps.setNull(5, Types.INTEGER);
			ps.setString(6, criterios.get("usuario")+"");			
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setInt(8, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			logger.info("\n\nINSERTANDO NUEVO REGISTRO EX COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+guardarNuevoRegistroExCobertura);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return valorseq;
			}
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO EX COBERTURA POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Metodo para consultar las excepciones coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaExcCoberturas(HashMap criterios)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCONSULTA EXCEPCIONES COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+consultaExcCoberturas);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaExcCoberturas, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("entidadSub")+""));
			ps.setString(2, criterios.get("numContrato")+"");
					
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO EXCEPCIONES COBERTURA POR ENTIDADES SUBCONTRATADAS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo para consultar las coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaCoberturasEntiSub(HashMap criterios)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
			
		logger.info("\n\nCRITERIOS------->"+criterios+"\n\nCONSULTA COBERTURA POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+consultaCoberturasEntiSub);
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaCoberturasEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("entidadSub")+""));
			ps.setString(2, criterios.get("numContrato")+"");
					
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			logger.info("\n\nRESULTAAAAADOOOOOS--->"+resultados);
			
			ps.close();
			UtilidadBD.cerrarConexion(con);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO COBERTURA POR ENTIDADES SUBCONTRATADAS------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
}