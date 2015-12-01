/**
 * Juan David Ramírez 13/06/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.capitacion.DtoUsuariosXConvenio;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ramírez
 *
 */
public class SqlBaseSubirPacienteDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSubirPacienteDao.class);

	/**
	 * Sentencia para ingresar usuarios por convenio
	 */
	private static 	String insertarUsuarioXConvenioStr="INSERT INTO capitacion.usuario_x_convenio (" +
																				" consecutivo, " +
																				" contrato, " +
																				" persona, " +
																				" fecha_inicial, " +
																				" fecha_final, " +
																				" tipo_cargue, " +
																				" fecha_cargue, " +
																				" usuario," +
																				" CLASIFICACION_SOCIO_ECONOMICA," +
																				" tipo_afiliado," +
																				" activo," +
																				" NATURALEZA_PACIENTES," +
																				" CENTRO_ATENCION," +
																				" TIPO_ID_EMPLEADOR," +
																				" NUMERO_ID_EMPLEADOR," +
																				" RAZON_SOCI_EMPLEADOR," +
																				" TIPO_ID_COTIZANTE," +
																				" NUMERO_ID_COTIZANTE," +
																				" NOMBRES_COTIZANTE," +
																				" APELLIDOS_COTIZANTE," +
																				" PARENTESCO," +
																				" NUMERO_FICHA " +
																		") VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Sentencia para ingresar usuarios capitados
	 */
	private static String insertarUsuariosCapitadosStr="INSERT INTO capitacion.usuarios_capitados (codigo, numero_identificacion, tipo_identificacion, fecha_nacimiento, sexo, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, direccion, telefono, email,numero_ficha,activo," +
			" tipo_id_empleador,num_id_empleador,razon_soci_empleador,tipo_id_cotizante,num_id_cotizante,nombres_cotizante,apellidos_cotizante,parentesco,centro_atencion,tipo_afiliado,excepcion_monto,pais,departamento,municipio,localidad,barrio) " +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'S',?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Sentencia para consultar la existencia de un usuario capitado en BD
	 */
	private static String consultarExistenciaUsuarioCapitadoStr="SELECT codigo AS codigo FROM capitacion.usuarios_capitados WHERE numero_identificacion=? AND tipo_identificacion=?";
	
	/**
	 * Sentencia para ingresar los convenios de los usuarios capitados
	 */
	private static String ingresarConvUsuariosaCapitadosStr="INSERT INTO capitacion.conv_usuarios_capitados (consecutivo, usuario_capitado, contrato, fecha_inicial, fecha_final, tipo_cargue, convenio, clasificacion_socio_economica,tipo_afiliado) VALUES(?,?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Sentencia para ingresar el contrato cargue
	 */
	private static String ingresarContratoCargueStr="INSERT INTO capitacion.contrato_cargue (codigo, contrato, fecha_cargue, fecha_inicial, fecha_final, total_pacientes, valor_total, upc, ajustes_debito, ajustes_credito, anulado) VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, 0, 0, ?)";
	
	/**
	 * Sentencia para ingresar contrato cargue por grupo etáreo
	 */
	private static String ingresarGrupoEtareoCargueStr="INSERT INTO capitacion.cargue_grupo_etareo (consecutivo, contrato_cargue, grupo_etareo, total_usuarios, upc, total_a_pagar) VALUES(?, ?, ?, ?, ?, ?)";
	
	/**
	 * Sentencia para ingresar el log del cargue
	 */
	private static String ingresarLogBDStr="INSERT INTO capitacion.log_subir_pacientes (contrato, fecha_cargue, total_leidos, total_grabados, usuario, archivo_inconsistencias) VALUES(?, CURRENT_DATE, ?, ?, ?, ?)";
	
	/**
	 * Sentencia para consultar la existencia de usuarios capitados
	 */
	private static String existeUsuarioXConvenioStr="SELECT contrato,convenio,empresa FROM usuario_x_convenio cuc inner join contratos c on(c.codigo=cuc.contrato) inner join convenios conv on(conv.codigo=c.convenio) WHERE cuc.persona=? AND cuc.fecha_inicial BETWEEN ? AND ? AND cuc.fecha_final BETWEEN ? AND ? group by contrato,convenio,empresa";
	
	
	/**
	 * Cadena que consulta los datos de un usuarios cpaitado 
	 */
	private static final String consultarUsuarioCapitadoStr = "SELECT "+ 
		"uc.codigo, "+
		"to_char(uc.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento, "+
		"uc.sexo, "+
		"uc.primer_nombre, "+
		"CASE WHEN uc.segundo_nombre IS NULL THEN '' ELSE uc.segundo_nombre END AS segundo_nombre , "+
		"uc.primer_apellido, "+
		"CASE WHEN uc.segundo_apellido IS NULL THEN '' ELSE uc.segundo_apellido END AS segundo_apellido , "+
		"CASE WHEN uc.direccion IS NULL THEN '' ELSE uc.direccion END AS direccion, "+
		"CASE WHEN uc.telefono IS NULL THEN '' ELSE uc.telefono END AS telefono, "+
		"CASE WHEN uc.email IS NULL THEN '' ELSE uc.email END AS email, "+ 
		"cuc.contrato As codigo_contrato, "+
		"coalesce(c.numero_contrato,'') As numero_contrato, "+
		"to_char(cuc.fecha_inicial, 'YYYY-MM-DD') AS fecha_inicial, "+
		"to_char(cuc.fecha_final, 'YYYY-MM-DD') AS fecha_final, "+
		"cuc.tipo_cargue AS tipo_cargue, "+
		"conv.codigo AS codigo_convenio, "+
		"getnombreconvenio(conv.codigo) AS nombre_convenio, "+
		"cuc.clasificacion_socio_economica as clasificacion," +
		"cuc.tipo_afiliado as tipoafiliado," +
		"cuc.numero_ficha as numeroficha," +
		"conv.tipo_regimen AS codigo_tipo_regimen," +
		"conv.empresa AS codigo_empresa," +
		"emp.razon_social AS nombre_empresa," +
		"uc.pais as pais_residencia," +
		"uc.departamento as departamento_residencia," +
		"uc.municipio as ciudad_residencia," +
		"uc.localidad as localidad_residencia," +
		"uc.barrio as barrio_residencia," +
		"bar.descripcion as descripcion_barrio, "+
		"cuc.fecha_cargue as fechacargue," +
		"cuc.naturaleza_pacientes as naturaleza," +
		"cuc.centro_atencion as centroatencion," +
		"cuc.tipo_id_empleador as tipoidempleador," +
		"cuc.numero_id_empleador as numeroidempleador," +
		"cuc.razon_soci_empleador as razonsociempleador," +
		"cuc.tipo_id_cotizante as tipoidcotizante," +
		"cuc.numero_id_cotizante as numeroidcotizante," +
		"cuc.nombres_cotizante as nombrescotizante," +
		"cuc.apellidos_cotizante as apellidoscotizante," +
		"cuc.parentesco as parentesco," +
		"cuc.numero_ficha as numeroficha "+					
		
		
		
		"from usuarios_capitados uc "+ 
		"INNER JOIN conv_usuarios_capitados cuc ON(cuc.usuario_capitado=uc.codigo) "+ 
		"INNER JOIN contratos c ON(cuc.contrato=c.codigo) "+ 
		"INNER JOIN convenios conv ON(conv.codigo=c.convenio) " +
		"INNER JOIN empresas emp ON(emp.codigo=conv.empresa) "+
		"left outer join barrios bar on (bar.codigo=uc.barrio) "+  
		"WHERE " +
		"uc.tipo_identificacion = ? and " +
		"uc.numero_identificacion = ? ";
	
	/**
	 * Cadena que elimina los contratos de un usuario capitado
	 */
	private static final String eliminarContratosUsuariosCapitadoStr = "DELETE FROM conv_usuarios_capitados WHERE usuario_capitado = ?";
	
	/**
	 * Cadena que elimina la informacion de un usuario capitado
	 */
	private static final String eliminarUsuarioCapitadoStr = "DELETE FROM usuarios_capitados WHERE codigo = ?";
	
	/**
	 * Cadena que consulta el usuario x convenio
	 */
	private static final String consultarUsuarioXConvenioStr = "SELECT "+ 
		"uxc.persona AS codigo_paciente, "+
		"c.codigo As codigo_contrato, "+
		"conv.codigo AS codigo_convenio, "+
		"conv.nombre AS nombre_convenio, "+
		"conv.maneja_montos	AS maneja_montos, "+
		"conv.tipo_regimen As codigo_tipo_regimen," +
		"getnomtiporegimen(conv.tipo_regimen) AS nombre_tipo_regimen, "+
		"CASE WHEN conv.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_pyp, "+
		"CASE WHEN conv.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_capitado," +
		"CASE WHEN c.numero_contrato IS NULL THEN 'SIN NUMERO CONTRATO' ELSE c.numero_contrato END AS numero_contrato, "+
		"CASE WHEN uxc.clasificacion_socio_economica IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE uxc.clasificacion_socio_economica END AS clasificacion_socio_economica, " +
		"getnombreestrato(clasificacion_socio_economica) as nombre_estrato, " +
		"CASE WHEN uxc.tipo_afiliado IS NULL THEN '' ELSE uxc.tipo_afiliado END AS tipo_afiliado, " +
		"getnombretipoafiliado(tipo_afiliado) AS nombre_tipo_afiliado, " +
		"CASE WHEN uxc.naturaleza_pacientes IS NULL THEN -1 ELSE uxc.naturaleza_pacientes END AS naturaleza_paciente, " +
		"np.nombre AS nombre_naturaleza "+
  		"FROM usuario_x_convenio uxc "+
		"INNER JOIN contratos c ON(c.codigo=uxc.contrato) "+ 
		"INNER JOIN convenios conv ON(c.convenio=conv.codigo) " +
		"LEFT JOIN naturaleza_pacientes np ON (np.codigo = uxc.naturaleza_pacientes) " + 
		"WHERE "+ 
		"uxc.persona = ? AND "+ 
		"(TO_DATE(?,'dd/MM/yyyy') BETWEEN uxc.fecha_inicial AND uxc.fecha_final) " +
		"ORDER BY uxc.fecha_inicial ASC ";
	
	public static Collection consultarGruposEtareos(Connection con, int institucion, int codigoConvenio, String fechaInicial, String fechaFinal)
	{
		String consulta=
			"SELECT " +
			"codigo as codigo, " +
			"convenio as convenio, " +
			"edad_inicial as edad_inicial, " +
			"edad_final as edad_final, " +
			"sexo as sexo, " +
			"fecha_inicial as fecha_inicial, " +
			"fecha_final as fecha_final, " +
			"valor as valor " +
			"FROM " +
			"grupos_etareos_x_convenio " +
			"WHERE " +
			"institucion=? and " +
			"convenio=? and " +
			"fecha_inicial<=? and " +
			"fecha_final>=?";
		PreparedStatementDecorator stm =  null;
	
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setInt(1, institucion);
			stm.setInt(2, codigoConvenio);
			stm.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			stm.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			Collection col= UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));

			return col;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando Grupos Etareos en Subir Pacientes"+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	/**
	 * Métodos para consultar el listado de los tipos para los selects
	 * @param con
	 * @param institucion @todo
	 * @param codigoConvenio @todo
	 * @return
	 */
	public static Collection consultarTipos(Connection con, int tipoConsulta, int institucion, int codigoConvenio)
	{
		String consulta="";
		switch(tipoConsulta)
		{
			case 1: // para consultar los contratos con convenios con tipo de contrato normal
				consulta="" +
							"SELECT " +
								"distinct conv.codigo AS codigo, " +
								"conv.nombre AS nombre " +
							"FROM view_contratos_vigentes vcont " +
								"INNER JOIN convenios conv " +
									"ON(conv.codigo=vcont.convenio) " +
							"WHERE conv.tipo_contrato!="+ConstantesBD.codigoTipoContratoEvento+" AND conv.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+ 
							"AND conv.institucion="+institucion+" "+
							"AND vcont.es_vigente="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
							"ORDER BY conv.nombre";
			break;
			case 2: // para consultar los contratos pertenecientes a un convenio que estan vigentes
				consulta="" +
							"SELECT " +
								"cont.codigo AS codigo, " +
								"cont.numero_contrato AS numero_contrato, " +
								"cont.upc AS upc, " +
								"cont.tipo_pago AS tipo_pago," +
								"cont.convenio AS convenio," +
								"cont.porcentaje_upc as porcentaje_upc " +
							"FROM " +
								"contratos cont " +
							"INNER JOIN " +
								"view_contratos_vigentes vig " +
							"on (cont.codigo=vig.codigo) " +
							"WHERE cont.convenio="+codigoConvenio+" "+
							"AND vig.es_vigente="+ValoresPorDefecto.getValorTrueParaConsultas();
			break;
			case 3: // consulta los tipos de identificacion asociados a la institucion exceptuando los anonimos
				consulta="" +
							"SELECT " +
								"tii.acronimo AS acronimo, " +
								"ti.nombre AS nombre, " +
								"tii.es_consecutivo AS es_consecutivo " +
							"FROM " +
								"tipos_identificacion ti " +
								"INNER JOIN " +
									"tipos_id_institucion tii " +
								"ON(tii.acronimo=ti.acronimo) " +
							"WHERE tii.institucion="+institucion+" " +
							"AND tii.acronimo!='NI'";
			break;
			case 4: // consulta los sexos ingresados
				consulta="" +
							"SELECT " +
								"codigo AS codigo, " +
								"nombre AS nombre " +
							"FROM " +
								"sexo";
			break;
			case 5: // consulta los Estratos Sociales
				consulta="SELECT DISTINCT estsol.codigo as codigo,estsol.descripcion as descripcion from estratos_sociales estsol inner join convenios conv on(conv.tipo_regimen=estsol.tipo_regimen) where conv.codigo="+codigoConvenio+" order by descripcion";
			break;
			
			case 6: // consulta los codigos de parentezco
				consulta="SELECT tiparen.codigo as codigo,tiparen.nombre as nombre FROM historiaclinica.tipos_parentesco tiparen";
			break;
			
			case 7: // consulta los tipos de Afiliado
				consulta="SELECT tipafi.acronimo as codigo,tipafi.nombre as nombre FROM manejopaciente.tipos_afiliado tipafi";
			break;
		}
		
		logger.info("consulta tipo "+tipoConsulta+"\n"+consulta);
		PreparedStatementDecorator stm =  null;
		
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			Collection col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
			
			return col;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando tipos "+tipoConsulta+": "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Metodo para ingresar un usuario por convenio
	 * @param con
	 * @param contrato
	 * @param codigoPersona
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoCargue
	 * @param usuario
	 * @param tipoAfiliado 
	 * @return
	 */
	public static int ingresarUsuarioXConvenio(Connection con, DtoUsuariosXConvenio datosCapitacion)
	{
		PreparedStatementDecorator stm =  null;
		
		try
		{
			int consecutivo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_usuario_x_convenio");
			//consecutivo, contrato, persona, fecha_inicial, fecha_final, tipo_cargue, fecha_cargue, usuario
			stm= new PreparedStatementDecorator(con.prepareStatement(insertarUsuarioXConvenioStr));
			
			stm.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
			stm.setInt(2, datosCapitacion.getContrato());
			stm.setInt(3, datosCapitacion.getCodigoPersona());
			stm.setDate(4, Date.valueOf(datosCapitacion.getFechaInicial()));
			stm.setDate(5, Date.valueOf(datosCapitacion.getFechaFinal()));
			stm.setDouble(6, Utilidades.convertirADouble(datosCapitacion.getTipoCargue()+""));
			stm.setString(7, datosCapitacion.getUsuario());
			if(datosCapitacion.getClasificacionSE()>0)
				stm.setInt(8, datosCapitacion.getClasificacionSE());
			else
				stm.setNull(8, Types.INTEGER);
			if(UtilidadCadena.noEsVacio(datosCapitacion.getTipoAfiliado()) && !datosCapitacion.getTipoAfiliado().equals("''") && !datosCapitacion.getTipoAfiliado().equals("' '"))
				stm.setString(9, datosCapitacion.getTipoAfiliado());
			else
				stm.setObject(9, null);
			stm.setString(10,datosCapitacion.getActivo());//" activo," +
			if(datosCapitacion.getNaturalezaPaciente()>=0)
				stm.setInt(11,datosCapitacion.getNaturalezaPaciente());//" naturaleza_paciente," +
			else
				stm.setObject(11, null);
			if(datosCapitacion.getCentroAtencion()>0)
				stm.setInt(12,datosCapitacion.getCentroAtencion());//" centro_atencion," +
			else
				stm.setObject(12, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getTipoIdEmpleador()))
				stm.setString(13,datosCapitacion.getTipoIdEmpleador());//	" tipo_id_empleador," +
			else
				stm.setObject(13, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getNumeroIdEmpleador()))
				stm.setString(14,datosCapitacion.getNumeroIdEmpleador());//" numero_id_empleador," +
			else
				stm.setObject(14, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getRazonSocialEmpleador()))
				stm.setString(15,datosCapitacion.getRazonSocialEmpleador());//" razo_soci_empleador," +
			else
				stm.setObject(15, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getTipoIdCotizante()))
				stm.setString(16,datosCapitacion.getTipoIdCotizante());//" tipo_id_cotizante," +
			else
				stm.setObject(16, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getNumeroIdCotizante()))
				stm.setString(17,datosCapitacion.getNumeroIdCotizante());//" numero_id_cotizante," +
			else
				stm.setObject(17, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getNombresCotizante()))
				stm.setString(18,datosCapitacion.getNombresCotizante());//" nombres_cotizante," +
			else
				stm.setObject(18, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getApellidosCotizante()))
				stm.setString(19,datosCapitacion.getApellidosCotizante());//" apellidos_cotizante," +
			else
				stm.setObject(19, null);
			if(datosCapitacion.getParentesco()>0)
				stm.setInt(20,datosCapitacion.getParentesco());//	" parentesco," +
			else
				stm.setObject(20, null);
			if(!UtilidadTexto.isEmpty(datosCapitacion.getNumeroFicha()))
				stm.setString(21,datosCapitacion.getNumeroFicha());//" numero_ficha " +
			else
				stm.setObject(21, null);
			int resultado=stm.executeUpdate();
			
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el usuario por convenio ",e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	public static int ingresarConvUsuarioCapitado(Connection con, int contrato, String fechaInicial, String fechaFinal, int codUsuarioCapitado, int tipoCargue, String convenio, String clasificacionSE, String codigoTipoAfiliado)
	{
		PreparedStatementDecorator stm =  null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(ingresarConvUsuariosaCapitadosStr));
			int consecutivo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_conv_usuarios_capitados");
			stm.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
			stm.setDouble(2, Utilidades.convertirADouble(codUsuarioCapitado+""));
			stm.setInt(3, contrato);
			stm.setDate(4, Date.valueOf(fechaInicial));
			stm.setDate(5, Date.valueOf(fechaFinal));
			stm.setDouble(6, Utilidades.convertirADouble(tipoCargue+""));
			stm.setInt(7, Utilidades.convertirAEntero(convenio));
			if(UtilidadCadena.noEsVacio(clasificacionSE) && !clasificacionSE.equals("''") && !clasificacionSE.equals("' '"))
				stm.setInt(8, Utilidades.convertirAEntero(clasificacionSE));
			else
				stm.setNull(8, Types.INTEGER);
			if(UtilidadCadena.noEsVacio(codigoTipoAfiliado) && !codigoTipoAfiliado.equals("''") && !codigoTipoAfiliado.equals("' '"))
				stm.setString(9, codigoTipoAfiliado);
			else
				stm.setObject(9, null);
			int resultado=stm.executeUpdate();
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el conv usuario capitado "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}


	/**
	 * Método para ingresar un usuario capitado
	 * y el convenios para los mismos
	 * @param con
	 * @param numeroIdentificacion
	 * @param tipoIdentificacion
	 * @param fechaNacimiento
	 * @param sexo
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param direccion
	 * @param telefono
	 * @param email
	 * @param tipoCargue
	 * @return el codigo del usuario capitado
	 */
	public static int insertarUsuarioCapitado(Connection con, String numeroIdentificacion, String tipoIdentificacion, String fechaNacimiento, int sexo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String direccion, String telefono, String email, String numeroFicha,
			String tipoIdEmpleador,String numIdEmpleador,String razonSociEmpleador,String tipoIdCotizante,String numIdCotizante,String nombresCotizante,String apellidosCotizante,int parentesco,int centroAtencion,String tipoAfiliado,String excepcionMonto ,String pais,String departamento ,String municipio,String localidad,
			String barrio)
	{
		PreparedStatementDecorator stm =  null;
		try
		{
			int codigoPersona=UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_usuarios_capitados");
			stm= new PreparedStatementDecorator(con.prepareStatement(insertarUsuariosCapitadosStr));
			logger.info("insertar Usuario_capitado:--_>"+insertarUsuariosCapitadosStr+"secuencia"+codigoPersona);
			
			stm.setDouble(1, Utilidades.convertirADouble(codigoPersona+""));
			stm.setString(2, numeroIdentificacion);
			stm.setString(3, tipoIdentificacion);
			if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
			{	
				stm.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento));				
			}
			else if((System.getProperty("TIPOBD")+"").equals("POSTGRESQL"))
			{
				stm.setDate(4, Date.valueOf(fechaNacimiento));
			}
			stm.setInt(5, sexo);
			stm.setString(6, primerNombre);
			stm.setString(7, segundoNombre);
			stm.setString(8, primerApellido);
			stm.setString(9, segundoApellido);
			stm.setString(10, direccion);
			stm.setString(11, telefono);
			stm.setString(12, email);
			stm.setObject(13, numeroFicha);
				if(tipoIdEmpleador.trim().equals(""))//Tipo Id Empleador
				{	stm.setNull(14, Types.VARCHAR);				
				}else
				{	stm.setString(14, tipoIdEmpleador);
				}
				if(numIdEmpleador.trim().equals(""))//Num Id Empleador
				{	stm.setNull(15, Types.VARCHAR);					
				}else
				{	stm.setString(15, numIdEmpleador);
				}
				if(razonSociEmpleador.trim().equals(""))//Razon Social Empleador
				{	stm.setNull(16, Types.VARCHAR);					
				}else
				{	stm.setString(16, razonSociEmpleador);
				}
				if(tipoIdCotizante.trim().equals(""))//Tipo Id Cotizante
				{	stm.setNull(17, Types.VARCHAR);					
				}else
				{	stm.setString(17, tipoIdCotizante);
				}
				if(numIdCotizante.trim().equals(""))//Num Id Cotizante
				{	stm.setNull(18, Types.VARCHAR);					
				}else
				{	stm.setString(18, numIdCotizante);
				}
				if(nombresCotizante.trim().equals(""))//Nombres Cotizante
				{	stm.setNull(19, Types.VARCHAR);					
				}else
				{	stm.setString(19, nombresCotizante);
				}
				if(apellidosCotizante.trim().equals(""))//Apellidos Cotizante
				{	stm.setNull(20, Types.VARCHAR);					
				}else
				{	stm.setString(20, apellidosCotizante);
				}			
				if(parentesco==ConstantesBD.codigoNuncaValido)//Parentesco
				{	stm.setNull(21, Types.INTEGER);
				}else
				{	stm.setInt(21, parentesco);
				}	
				if(centroAtencion==ConstantesBD.codigoNuncaValido)//Centro Atnecion
				{	stm.setNull(22, Types.INTEGER);
				}else
				{	stm.setInt(22, centroAtencion);
				}
				if(tipoAfiliado.trim().equals(""))//Tipo Afiliado
				{	stm.setNull(23, Types.CHAR);
				}else	
				{	stm.setString(23, tipoAfiliado);
				}			
				if(excepcionMonto.trim().equals(""))//Excepcion Monto
				{	stm.setNull(24, Types.VARCHAR);
				}else	
				{	stm.setString(24, excepcionMonto);
				}
				if(pais.trim().equals(""))//Pais
				{	stm.setNull(25, Types.VARCHAR);
				}else	
				{	stm.setString(25, pais);
				}
				if(departamento.trim().equals(""))//Pais
				{	stm.setNull(26, Types.VARCHAR);
				}else	
				{	stm.setString(26, departamento);
				}
				if(municipio.trim().equals(""))//Pais
				{	stm.setNull(27, Types.VARCHAR);
				}else	
				{	stm.setString(27, municipio);
				}						
				if(localidad.trim().equals(""))//Localidad
				{	stm.setString(28,null);
				}else
				{	stm.setString(28, localidad);				
				}
				if(barrio.trim().equals(""))//Barrio
				{	stm.setString(29,null);
				}else
				{	stm.setString(29, barrio);			
				}
			
			int resultado=(stm.executeUpdate()>0)?codigoPersona:-1;
			
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el usuario capitado "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Metodo para ingresar el en la tabla capitacion.contrato_cargue
	 * @param con
	 * @param contrato
	 * @param totalPacientes
	 * @param upc
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoPago
	 * @return
	 */
	public static int ingresarContratoCargue(Connection con, int contrato, int totalPacientes, double upc, String fechaInicial, String fechaFinal, int tipoPago, double valorTotal) throws SQLException
	{
		/*
		 * Todavía no se si se debe utilizar el tipo de pago
		 */
		int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_contrato_cargue");
		PreparedStatementDecorator stm =  null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(ingresarContratoCargueStr));
			/*
			 * codigo
			 * contrato
			 * fecha_inicial
			 * fecha_final
			 * total_pacientes
			 * valor_total
			 * upc
			 */
			
			/**
			 * INSERT INTO capitacion.contrato_cargue 
			 * (codigo, contrato, fecha_cargue, fecha_inicial, 
			 * fecha_final, total_pacientes, valor_total, upc, 
			 * ajustes_debito, ajustes_credito, anulado) VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, 0, 0, ?)";
			 */
			stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
			stm.setInt(2, contrato);
			stm.setDate(3, Date.valueOf(fechaInicial));
			stm.setDate(4, Date.valueOf(fechaFinal));
			stm.setDouble(5, Utilidades.convertirADouble(totalPacientes+""));
			/*
			 * stm.setDouble(6, totalPacientes*upc);
			 */
			stm.setDouble(6, valorTotal);
			stm.setDouble(7, upc);
			stm.setBoolean(8, false); // no esta anulado por defecto
			int numIngresos=stm.executeUpdate();
			
			if(numIngresos>0)
			{
				return codigo;
			}
			return 0;
		}
		catch (SQLException e)
		{
			logger.info(ingresarContratoCargueStr);
			logger.info("Utilidades.convertirADouble(codigo)--->"+Utilidades.convertirADouble(codigo+""));
			logger.info("contrato-->"+contrato);
			logger.info("Date.valueOf(fechaInicial)-->"+Date.valueOf(fechaInicial));
			logger.info("Date.valueOf(fechaFinal)-->"+Date.valueOf(fechaFinal));
			logger.info("Utilidades.convertirADouble(totalPacientes)-->"+Utilidades.convertirADouble(totalPacientes+""));
			logger.info("valorTotal-->"+valorTotal);
			logger.info("upc-->"+upc);
			
			logger.error("Error ingresando el contrato cargue ",e);
			throw e;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Aumenta la cantidad de pacientes para el contrato cargue
	 * @param con
	 * @param codigo
	 * @param totalPacientes
	 * @return
	 */
	public static int aumentarPacientesContratoCargue(Connection con, int codigo, int totalPacientes, double valorTotal) throws SQLException
	{
		String consulta="update contrato_cargue set total_pacientes=total_pacientes+?, valor_total=valor_total+? where codigo=?";
		PreparedStatementDecorator stm =  null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setDouble(1, Utilidades.convertirADouble(totalPacientes+""));
			stm.setDouble(2, valorTotal);
			stm.setDouble(3, Utilidades.convertirADouble(codigo+""));
			int resultado=stm.executeUpdate();
			
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error modificando el contrato cargue "+e);
			throw e;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Método para ingresar cargue grupo etareo
	 * @param con
	 * @param contratoCargue
	 * @param grupoEtareo
	 * @param totalPacientes
	 * @param upc
	 * @return
	 */
	public static int ingresarGrupoEtareoCargue(Connection con, int contratoCargue, int grupoEtareo, int totalPacientes, double upc)
	{
		PreparedStatementDecorator stm =  null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(ingresarGrupoEtareoCargueStr));
			/*
			 * consecutivo
			 * contrato_cargue
			 * grupo_etareo
			 * total_usuarios
			 * upc
			 * total_a_pagar
			 */
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_cargue_grupo_etareo");
			
			/**
			 * INSERT INTO capitacion.cargue_grupo_etareo 
			 * (consecutivo, contrato_cargue, grupo_etareo, total_usuarios, upc, total_a_pagar) VALUES(?, ?, ?, ?, ?, ?)";
			 */
			
			stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
			stm.setDouble(2, Utilidades.convertirADouble(contratoCargue+""));
			stm.setDouble(3, Utilidades.convertirADouble(grupoEtareo+""));
			stm.setDouble(4, Utilidades.convertirADouble(totalPacientes+""));
			stm.setDouble(5, upc);
			stm.setDouble(6, totalPacientes*upc);
			int resultado=stm.executeUpdate();
			
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando grupo etareo cargue "+e);
			return 0;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Metodo para actualizar el cargue grupo etareo
	 * @param con
	 * @param codigo
	 * @param totalPacientes
	 * @param upc
	 * @return
	 */
	public static int modificarGrupoEtareoCargue(Connection con, int codigo, int totalPacientes, double upc)
	{
		PreparedStatementDecorator stm =  null;
		try
		{
			String consulta="update cargue_grupo_etareo set upc=?, total_usuarios=total_usuarios+?, total_a_pagar=total_a_pagar+? where consecutivo=?";
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setDouble(1, upc);
			stm.setDouble(2, Utilidades.convertirADouble(totalPacientes+""));
			stm.setDouble(3, Utilidades.convertirADouble(totalPacientes+"")*upc);
			stm.setDouble(4, Utilidades.convertirADouble(codigo+""));
			int resultado=stm.executeUpdate();
			
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando grupo etareo cargue "+e);
			return 0;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Consulta el codigo de un cargue grupo_etareo especificado el contrato y el grupo_etareo
	 * @param con
	 * @param codigoContratoCargue
	 * @param codigoGrupoEtareo
	 * @return
	 */
	public static int consultarCodigoCargueGrupoEtareo(Connection con, int codigoContratoCargue, int codigoGrupoEtareo)
	{
		String consulta = "select consecutivo as consecutivo from cargue_grupo_etareo where contrato_cargue=? and grupo_etareo=?";
		int resultadoInt=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator stm =  null;
		ResultSetDecorator resultado= null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setDouble(1, Utilidades.convertirADouble(codigoContratoCargue+""));
			stm.setDouble(2, Utilidades.convertirADouble(codigoGrupoEtareo+""));
			resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				resultadoInt=resultado.getInt("consecutivo");
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error consultando existencia cargue_grupo_etareo: "+e);
		}finally{
			
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(resultado!=null){
					resultado.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
		}
		return resultadoInt;
	}
	
	/**
	 * Método para ingresar el log tipo BD del cargue
	 * @param con
	 * @param contrato
	 * @param registrosLeidos
	 * @param registrosGuardados
	 * @param loginUsuario
	 */
	public static boolean ingresarLogBD(Connection con, int contrato, int registrosLeidos, int registrosGuardados, String loginUsuario, String archivoInconsistencias)
	{
		PreparedStatementDecorator stm =  null;
		try
		{
			if(!UtilidadTexto.isEmpty(archivoInconsistencias))
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(ingresarLogBDStr));
				/*
				 * contrato
				 * total_leidos
				 * total_grabados
				 * usuario
				 */
				
				/**
				 * INSERT INTO capitacion.log_subir_pacientes 
				 * (contrato, fecha_cargue, total_leidos, total_grabados, usuario, archivo_inconsistencias) VALUES(?, CURRENT_DATE, ?, ?, ?, ?)";
				 */
				
				stm.setInt(1, contrato);
				stm.setDouble(2, Utilidades.convertirADouble(registrosLeidos+""));
				stm.setDouble(3, Utilidades.convertirADouble(registrosGuardados+""));
				stm.setString(4, loginUsuario);
				stm.setString(5, archivoInconsistencias);
				boolean result= stm.executeUpdate()>0?true:false;
				
				return result;
			}
			else
			{
				return true;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el log del cargue "+e);
			return false;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static int codigoEmpresaConvenio(Connection con,int convenio)
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator ps =  null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement("select empresa from convenios where codigo="+convenio));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				resultado=rs.getInt(1);
	
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el log del cargue "+e);
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return resultado;
	}
	
	/**
	 * Método para consultar la existencia de usuarios capitados
	 * @param con
	 * @param codigoPersona
	 * @param fechaInicial @todo
	 * @param fechaFinal @todo
	 * @param contrato 
	 * @param convenio 
	 * @return 
	 */
	public static boolean existeUsuarioXConvenio(Connection con, int codigoPersona, String fechaInicial, String fechaFinal, int convenio, int contrato)
	{
		PreparedStatementDecorator stm =  null;
		ResultSetDecorator resultado= null;
		try
		{
			/*
			 * Validacion en caso de que se permita subir pacientes de diferentes convenios pero la misma empres
			int codigoEmpresa=codigoEmpresaConvenio(con,convenio);
			if(codigoEmpresa==ConstantesBD.codigoNuncaValido)
				return true;
			*/
			stm= new PreparedStatementDecorator(con.prepareStatement(existeUsuarioXConvenioStr));
			logger.info("--consulta UsuarioXConvenio: "+existeUsuarioXConvenioStr+"-->codPersona:"+codigoPersona+"-fechaInicial:"+fechaInicial+"-fechaFinal:"+fechaFinal);
			/**
			 * SELECT contrato,convenio,empresa 
			 * FROM usuario_x_convenio cuc 
			 * inner join contratos c on(c.codigo=cuc.contrato) 
			 * inner join convenios conv on(conv.codigo=c.convenio) 
			 * WHERE cuc.persona=? AND cuc.fecha_inicial BETWEEN ? AND ? AND cuc.fecha_final BETWEEN ? AND ? group by contrato,convenio,empresa";
			 */
			
			stm.setInt(1, codigoPersona);
			stm.setDate(2, Date.valueOf(fechaInicial));
			stm.setDate(3, Date.valueOf(fechaFinal));
			stm.setDate(4, Date.valueOf(fechaInicial));
			stm.setDate(5, Date.valueOf(fechaFinal));
			resultado=new ResultSetDecorator(stm.executeQuery());
			while(resultado.next())
			{
				//Validacion en caso de que se permita subir pacientes de diferentes convenios pero la misma empres
				//if((resultado.getInt("empresa")!=codigoEmpresa) || (resultado.getInt("convenio")==convenio && resultado.getInt("contrato")==contrato))
				//validar que siempre es del mismo convenio diferente contrato.
				if(resultado.getInt("convenio")!=convenio||resultado.getInt("contrato")==contrato)
				{
					
					return true;
				}
			}
		
			return false;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los usuarios por convenio "+e);
			return false;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(resultado!=null){
					resultado.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Consulta si un usuario ya tiene cargue en el periodo indicado
	 * @param con
	 * @param codigoUsuarioCapitado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param contrato 
	 * @param convenio 
	 * @return
	 */
	public static boolean existeUsuarioCapitadoPeriodo(Connection con, int codigoUsuarioCapitado, String fechaInicial, String fechaFinal, int convenio, int contrato)
	{
		PreparedStatementDecorator stm =  null;
		ResultSetDecorator resultado= null;
		try
		{
			String existeUsuarioCapitadoPeriodoStr=
																"SELECT "+
																	"contrato, convenio, empresa "+
																"FROM "+
																	"conv_usuarios_capitados cuc "+
																	"INNER JOIN convenios conv on(cuc.convenio=conv.codigo) "+
																"WHERE "+
																	"cuc.usuario_capitado = ? "+
																	"AND (('"+fechaInicial+"' BETWEEN to_char(cuc.fecha_inicial,'yyyy-mm-dd') AND to_char(cuc.fecha_final,'yyyy-mm-dd') OR '"+fechaFinal+"' BETWEEN to_char(cuc.fecha_inicial,'yyyy-mm-dd') AND to_char(cuc.fecha_final,'yyyy-mm-dd')) OR ((to_char(cuc.fecha_inicial,'yyyy-mm-dd') BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"') OR (cuc.fecha_final BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"'))) "+
																"GROUP BY contrato, convenio, empresa";
			
			stm= new PreparedStatementDecorator(con.prepareStatement(existeUsuarioCapitadoPeriodoStr));
	
			logger.info("Consulta (conv_usuarios_capitados):"+existeUsuarioCapitadoPeriodoStr+"-->codUsuario:"+codigoUsuarioCapitado);
			stm.setDouble(1, Utilidades.convertirADouble(codigoUsuarioCapitado+""));
			
			resultado=new ResultSetDecorator(stm.executeQuery());
			while(resultado.next())
			{
				//if((resultado.getInt("empresa")!=codigoEmpresa) || (resultado.getInt("convenio")==convenio && resultado.getInt("contrato")==contrato))
					
				if(resultado.getInt("convenio")!=convenio||resultado.getInt("contrato")==contrato)
				{
					
				
					return true;
				}
			}
			//no enccontro ninguno entonces retorn false sin entrar al ciclo.
			
			return false;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los usuarios capitados "+e);
			return false;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(resultado!=null){
					resultado.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	public static int consultarCodigoContratoCarguePeriodo(Connection con, int contrato, String fechaInicial, String fechaFinal)
	{
		String consulta = "select codigo from contrato_cargue where contrato=? and to_char(fecha_inicial,'yyyy-mm-dd')='"+fechaInicial+"' and to_char(fecha_final,'yyyy-mm-dd')='"+fechaFinal+"' and anulado="+ValoresPorDefecto.getValorFalseParaConsultas();
		int resultadoInt=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator stm =  null;
		ResultSetDecorator resultado= null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setInt(1, contrato);
			resultado=new ResultSetDecorator(stm.executeQuery());
			
			if(resultado.next())
			{
				resultadoInt=resultado.getInt("codigo");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando existencia contrato_cargue"+e);
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			try{
				if(resultado!=null){
					resultado.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return resultadoInt;
	}
	
	/**
	 * Consulta si existe el usuario capitado y retorna su id
	 * @param con
	 * @return
	 */
	public static int consultarCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{
		int resultadoInt=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator stm =  null;
		ResultSetDecorator resultado= null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(SqlBaseSubirPacienteDao.consultarExistenciaUsuarioCapitadoStr));
			
			/**
			 * SELECT codigo AS codigo FROM capitacion.usuarios_capitados WHERE numero_identificacion=? AND tipo_identificacion=?";
			 */
			
			stm.setString(1, numeroIdentificacion);
			stm.setString(2, tipoIdentificacion);
			resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				resultadoInt=resultado.getInt("codigo");
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error consultando existencia de usuario capitado "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(resultado!=null){
					resultado.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return resultadoInt;
	}
	
	public static HashMap consultarUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{
		PreparedStatementDecorator stm =  null;
		ResultSetDecorator resultado= null;
		try
		{
			String consulta=
				"SELECT "+
					"uc.codigo as codigo, "+
					"uc.numero_identificacion as numeroIdentificacion, "+
					"uc.tipo_identificacion as tipoIdentificacion, "+
					"to_char(uc.fecha_nacimiento,'dd/mm/yyyy') as fechaNacimiento, "+
					"sexo.codigo as codigoSexo, "+
					"sexo.nombre as nombreSexo, "+
					"uc.primer_nombre as primerNombre, "+
					"CASE WHEN uc.segundo_nombre IS NULL THEN '' ELSE uc.segundo_nombre END AS segundoNombre, "+
					"uc.primer_apellido as primerApellido, "+
					"CASE WHEN uc.segundo_apellido IS NULL THEN '' ELSE uc.segundo_apellido END AS segundoApellido, "+
					"CASE WHEN uc.direccion IS NULL THEN '' ELSE uc.direccion END AS direccion, "+
					"CASE WHEN uc.telefono IS NULL THEN '' ELSE uc.telefono END AS telefono, "+
					"CASE WHEN uc.email IS NULL THEN '' ELSE uc.email END AS email," +
					"cuc.CONTRATO AS codigo_contrato," +
					"to_char(cuc.fecha_inicial, 'YYYY-MM-DD') AS fecha_inicial, "+
					"to_char(cuc.fecha_final, 'YYYY-MM-DD') AS fecha_final, "+
					"cuc.tipo_cargue as tipo_cargue," +
					"cuc.clasificacion_socio_economica as clasificacion, "+
					"cuc.tipo_afiliado as tipoafiliado," +
					"cuc.fecha_cargue as fechacargue," +
					"cuc.naturaleza_pacientes as naturaleza," +
					"cuc.centro_atencion as centroatencion," +
					"cuc.tipo_id_empleador as tipoidempleador," +
					"cuc.numero_id_empleador as numeroidempleador," +
					"cuc.razon_soci_empleador as razonsociempleador," +
					"cuc.tipo_id_cotizante as tipoidcotizante," +
					"cuc.numero_id_cotizante as numeroidcotizante," +
					"cuc.nombres_cotizante as nombrescotizante," +
					"cuc.apellidos_cotizante as apellidoscotizante," +
					"cuc.parentesco as parentesco," +
					"cuc.numero_ficha as numeroficha "+					
				"FROM "+    
					"sexo, "+
					"usuarios_capitados uc "+
					"INNER JOIN conv_usuarios_capitados cuc on (uc.codigo=cuc.usuario_capitado) "+
				"WHERE "+
					"uc.sexo = sexo.codigo "+
					"and uc.tipo_identificacion = ? "+
					"and uc.numero_identificacion = ?";
		
			logger.info("consulta UsuarioCapitado (num-tipo ID):>>>>>>>>"+consulta+"--->"+tipoIdentificacion+"-"+numeroIdentificacion);
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setString(1, tipoIdentificacion);
			stm.setString(2, numeroIdentificacion);
			resultado=new ResultSetDecorator(stm.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando existencia de usuario capitado "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			try{
				if(resultado!=null){
					resultado.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Método que consulta un usuario capitado por tipo y número identificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarUsuarioCapitado(Connection con,HashMap campos)
	{
		String cadena = consultarUsuarioCapitadoStr;
		
		if(!campos.containsKey("validarVigencia")) 				
			cadena+= " and (to_char(CURRENT_DATE,'yyyy-mm-dd') between to_char(cuc.fecha_inicial,'yyyy-mm-dd') and to_char(cuc.fecha_final,'yyyy-mm-dd')) ";			
			
		PreparedStatementDecorator pst =  null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			
			pst.setString(1,campos.get("tipoIdentificacion")+"");
			pst.setString(2,campos.get("numeroIdentificacion")+"");
			
			logger.info("--->"+cadena);
			logger.info("campos -->"+campos);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUsuarioCapitado de SqlBaseSubirPacienteDao: "+e);
			return null;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
				
		}
	}
	
	/**
	 * Método que elimina un usuario capitado 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int eliminarUsuarioCapitado(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst =  null;
		try
		{
			String estado = campos.get("estado").toString();
			int resp0 = 0, resp1 = 0, resp = 0;
			
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarContratosUsuariosCapitadoStr));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("codigo")+""));
			
			resp0 = pst.executeUpdate();
			
			if(resp0>0)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarUsuarioCapitadoStr));
				pst.setDouble(1,Utilidades.convertirADouble(campos.get("codigo")+""));
				resp1 = pst.executeUpdate();
			}
			
			if(resp0>0&&resp1>0)
				resp = 1;
			else
				resp = 0;
			
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				if(resp>0)
					UtilidadBD.finalizarTransaccion(con);
				
			}
			
		
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarUsuarioCapitado de SqlBaseSubirPacienteDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Método implementado para consultar los datos del usuario x convenio
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarUsuarioXConvenio(Connection con,String codigoPaciente)
	{			
		PreparedStatementDecorator pst =  null;
		try
		{
			//logger.info("valor consultarUsuarioXConvenio >> "+consultarUsuarioXConvenioStr+" >> "+codigoPaciente);
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultarUsuarioXConvenioStr));
			pst.setInt(1,Utilidades.convertirAEntero(codigoPaciente));
			pst.setString(2,UtilidadFecha.getFechaActual(con));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUsuarioXConvenio de SqlBaseSubirPacienteDao: "+e);
			return null;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param mes
	 * @param anio
	 * @return
	 */
	public static double obtenerValorUnidadPagoCapitacionMesAnio(Connection con, int mes, int anio)
	{
		

		
		PreparedStatementDecorator pst =  null;
		String fecha=anio+"-"+(mes<10?"0"+mes:mes);
		String cadena=" SELECT valor from unidad_pago where '"+fecha+"' between to_char(fecha_inicial,'yyyy-mm') and to_char(fecha_final,'yyyy-mm')";
		double resultado=0;
		ResultSetDecorator rs= null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				resultado=rs.getDouble(1);
		
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUsuarioXConvenio de SqlBaseSubirPacienteDao: "+e);
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean existeConvenioCapitado(Connection con, String convenio) 
	{
		String cadena="SELECT count(1) from convenios where codigo=? and tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado;
		boolean result=false;
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs= null;
		try
		{
			 pst =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			pst.setInt(1, Utilidades.convertirAEntero(convenio));
			 rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				result=rs.getInt(1)>0;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeConvenioCapitado de SqlBaseSubirPacienteDao: "+e);
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param con
	 * @param clasificacionSE
	 * @param codigoConvenio 
	 * @return
	 */
	public static boolean existeClasificacionSocioEconomica(Connection con, String clasificacionSE, int codigoConvenio) 
	{
		if(clasificacionSE.equals("''"))
		{
			//CAMBIO SOLICITADO POR NURY //SASAIMA 2008-02-13
			logger.info("\nLA CLASIFICACION SOCIOECONOMICA ES NULL!!!!!!!!!!!!");
			return true;
		}	
		
		String cadena="SELECT count(1) from estratos_sociales estsol inner join convenios conv on(conv.tipo_regimen=estsol.tipo_regimen) where conv.codigo=? and estsol.codigo=? and estsol.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
		boolean result=false;
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs= null;
		
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			pst.setInt(1, codigoConvenio);
			pst.setInt(2, Utilidades.convertirAEntero(clasificacionSE));
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				result=rs.getInt(1)>0;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeClasificacionSocioEconomica de SqlBaseSubirPacienteDao: "+e);
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoID
	 * @param numeroID
	 * @return
	 */
	public static int obtenerCodigoPersona(Connection con, String tipoID,String numeroID) 
	{
		int codigoPersona=ConstantesBD.codigoNuncaValido;
		String cadena=" select codigo from personas where tipo_identificacion=? and numero_identificacion=?";
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs = null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			pst.setString(1, tipoID);
			pst.setString(2, numeroID);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoPersona=rs.getInt(1);
			}
		
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoPersona de SqlBaseSubirPacienteDao: "+e);
		}finally{
			
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
			}
			
			
		}
		return codigoPersona;
	}
}
