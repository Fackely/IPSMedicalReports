package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Types;

import com.princetonsa.decorator.PreparedStatementDecorator;

import java.sql.SQLException;
import java.util.HashMap;


import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturasVarias.DtoDeudor;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Juan Sebastican castaño
 * Controlador de acciones sobre la bd en la funcionalidad Deudores
 */
public class SqlBaseDeudoresDao {

	private static Logger logger = Logger.getLogger(SqlBaseDeudoresDao.class);
	
	
	public static final String [] indicesTerceros = {"codigoTercero_","numeroIdTercero_","descripcionTercero_","activoTercero_"};

	public static final String consultaTerceros = "SELECT" +
														" codigo AS codigo_tercero," +
														" numero_identificacion AS numero_id_tercero," +
														" descripcion AS descripcion_tercero," +
														" activo AS activo_tercero" +
													" FROM" +
														" terceros" +
													" WHERE " +
														" institucion = ? ";
	
	
	public static final String [] indicesDeudores = {"codigoDeudor_","activoDeudor_","direccionDeudor_","telDeudor_","mailDeudor_","repreDeudor_","nomContactoDeudor_","obserDeudor_","decTercero_","numIdTercero_"};
	
	
	
	public static final String consultaDeudorOtro = "SELECT" +
	" d.codigo_tercero AS codigo_deudor," +
	
	
	" d.activo AS activo_deudor," +
	" d.direccion AS direccion_deudor," +
	" d.telefono AS tel_deudor," +
	" d.e_mail AS mail_deudor," +
	" d.representante_legal AS repre_deudor," +
	" d.nombre_contacto AS nom_contacto_deudor," +
	" d.observaciones AS obser_deudor," +
	" d.tipo_identificacion AS tipo_identificacion," +
	" d.numero_identificacion AS numero_identificacion ," +
	" d.primer_apellido AS primer_apellido ," +
	" d.segundo_apellido AS segundo_apellido ," +
	" d.primer_nombre AS primer_nombre ," +
	 "d.tipo_identificacion || ' ' || d.numero_identificacion || ' - ' || d.primer_apellido || coalesce(' '||d.segundo_apellido||' ',' ') || d.primer_nombre || coalesce(' '||d.segundo_nombre,' ') as razon_social, "+
	
	" d.segundo_nombre AS segundo_nombre  " +

	
" FROM" +
	"  deudores d " +

"  WHERE " +
	
	" d.codigo = ? ";
	
	public static final String consultaDeudor = "SELECT" +
													" d.codigo_tercero AS codigo_deudor," +
													
													
													" d.activo AS activo_deudor," +
													" d.direccion AS direccion_deudor," +
													" d.telefono AS tel_deudor," +
													" d.e_mail AS mail_deudor," +
													" d.representante_legal AS repre_deudor," +
													" d.nombre_contacto AS nom_contacto_deudor," +
													" d.observaciones AS obser_deudor," +
													" d.tipo_identificacion AS tipo_identificacion," +
													" coalesce(d.numero_identificacion,'') AS numero_identificacion ," +
													" coalesce(d.primer_apellido,'') AS primer_apellido," +
													" coalesce(d.segundo_apellido,'') AS segundo_apellido," +
													" coalesce(d.primer_nombre,'') AS primer_nombre," +
													" coalesce(d.segundo_nombre,'') AS segundo_nombre," +
													" t.descripcion AS dec_tercero," +
													" t.numero_identificacion AS num_id_tercero " +
													
												" FROM" +
													" deudores d " +
												" INNER JOIN terceros t ON (t.codigo = d.codigo_tercero) " +
												" WHERE " +
													" d.institucion = ? and " +
													" d.codigo_tercero = ?";
	
	public static final String [] indicesDeudoresEmpresas = {"codigoTercero_","activoDeudor_","dirDeudor_","telDeudor_","mailDeudor_","repreLegalDeudor_","nomContDeudor_","obsDeudor_","tipoDeudor_","dirEmpresa_","telEmpresa_", "mailEmpresa_", "nomContactoEmpresa_","nomRepreEmpresa_","razonSocialEmpresa_","nitTercero_"};
	public static final String consultarDeudoresEmpresas = "select " +
															" distinct on (e.razon_social,t.numero_identificacion,d.codigo_tercero) " +
															" d.codigo_tercero AS codigo_tercero," +
															" getTipoDeudor(d.codigo_tercero) AS tipo_deudor," +
															
															" d.activo AS activo_deudor," +
															" d.direccion AS dir_deudor," +
															" d.telefono AS tel_deudor, " +
															" d.e_mail AS mail_deudor," +
															" coalesce(d.representante_legal,'') AS repre_legal_deudor, " +
															" d.nombre_contacto AS nom_cont_deudor," +
															" d.observaciones AS obs_deudor, " +
															
																														" e.direccion AS dir_empresa," +
															" e.telefono AS tel_empresa," +
															" e.email AS mail_empresa," +
															" facturacion.getNombreContacto(e.codigo) AS nom_contacto_empresa," +
															" e.nombre_representante AS nom_repre_empresa," +
															" e.razon_social AS razon_social_empresa," +
															" t.numero_identificacion AS nit_tercero" +
														
													" from " +
															"deudores d " +
													" INNER JOIN empresas e on (e.tercero = d.codigo_tercero)" +
													" LEFT OUTER JOIN terceros t on (t.codigo = d.codigo_tercero)" +
													" WHERE " +
															" getTipoDeudor(d.codigo_tercero) = ? and d.institucion = ? and t.activo = " +ValoresPorDefecto.getValorTrueParaConsultas()+
													" ORDER BY e.razon_social,t.numero_identificacion,d.codigo_tercero";
	
	
	
	public static final String [] indicesDeudoresTerceros = {"codigoTercero_","tipoDeudor_","nitTercero_","descripcionTercero_","activoDeudor_","dirDeudor_","telDeudor_","mailDeudor_","repreLegalDeudor_","nomContDeudor_","obsDeudor_"};
	public static final String [] indicesDeudoresOtros = {"codigoTercero_","tipoDeudor_","tipoIdentificacion_","numeroIdentificacion_","activoDeudor_","primerNombre_","primerApellido_","segundoApellido_","segundoNombre_","nomContDeudor_","obsDeudor_"};
	public static final String consultarDeudoresTerceros = "SELECT  " +
																	" distinct on (t.descripcion,t.numero_identificacion,d.codigo_tercero) " +
																	" d.codigo_tercero AS codigo_tercero," +
																	" getTipoDeudor(d.codigo_tercero) AS tipo_deudor," +
																	
																	
																	" d.activo AS activo_deudor," +
																	" d.direccion AS dir_deudor," +
																	" d.telefono AS tel_deudor, " +
																	" d.e_mail AS mail_deudor," +
																	" coalesce(d.representante_legal,'') AS repre_legal_deudor, " +
																	" d.nombre_contacto AS nom_cont_deudor," +
																	" d.observaciones AS obs_deudor, " +
																	
																	
																	
																	
																	
																	
																	
																	" t.numero_identificacion AS nit_tercero," +
																	" t.descripcion as descripcion_tercero " +
															"from " +
																	" deudores d " +
															" INNER JOIN terceros t on (t.codigo = d.codigo_tercero) " +
															" WHERE  " +
																	" getTipoDeudor(d.codigo_tercero) = ? " +
																	" and d.institucion = ?  " +
															" ORDER BY t.descripcion,t.numero_identificacion,d.codigo_tercero";
	
//	********* anexo 791 **********
	public static final String [] indicesDeudoresPacientes = {"codigoPaciente_","activoDeudor_","dirDeudor_","telDeudor_","mailDeudor_","repreLegalDeudor_","nomContDeudor_","obsDeudor_","tipoDeudor_","tipoIdPaciente_","numIdPaciente_","nombrePaciente_","dirPaciente_","telPaciente_","emailPaciente_"};
	
	
	
	public static final String consultarDeudoresPacientes =  "SELECT " +
													"per.codigo AS codigo_paciente, " +
													"'PACI' AS tipo_deudor," +
													"per.tipo_identificacion AS tipo_id_paciente, " +
													"per.numero_identificacion AS num_id_paciente, " +
													"per.primer_nombre || ' ' || coalesce(per.segundo_nombre||' ','')" +
													"|| per.primer_apellido || coalesce(' ' || per.segundo_apellido,'') AS nombre_paciente, " +
													"per.direccion AS dir_paciente, " +
													"per.telefono AS tel_paciente, " +
													"per.email AS email_paciente, " +
													"deu.activo AS activo_deudor, " +
													"deu.direccion AS dir_deudor, " +
													"deu.telefono AS tel_deudor, " +
													"deu.e_mail AS mail_deudor, " +
													"coalesce(deu.representante_legal,'') AS repre_legal_deudor, " +
													"deu.nombre_contacto AS nom_cont_deudor, " +
													"deu.observaciones AS obs_deudor " +
													"FROM " +
													"personas Per " +
													"INNER JOIN " +
													"deudores Deu " +
													"ON (per.codigo = deu.codigo_paciente) " +
													"  WHERE " +
													"deu.codigo_paciente IS NOT NULL " +
													"AND deu.institucion = ?";
//	******************************
	
	//*******************CAMBIOS POR ANEXO 811 ***************************************************+
	/**
	 * Cadena para cargar los datos de la empresa
	 */
	
	
	public static final String consultaDeudorOtroStr = "SELECT" +
	" d.codigo_tercero AS codigo_deudor," +
	
	
	" d.activo AS activo_deudor," +
	" d.direccion AS direccion_deudor," +
	" d.telefono AS tel_deudor," +
	" d.e_mail AS mail_deudor," +
	" d.representante_legal AS repre_deudor," +
	" d.nombre_contacto AS nom_contacto_deudor," +
	" d.observaciones AS obser_deudor," +
	" d.tipo_identificacion AS tipo_identificacion," +
	" d.numero_identificacion AS numero_identificacion ," +
	" d.primer_apellido AS primer_apellido ," +
	" d.segundo_apellido AS segundo_apellido ," +
	" d.primer_nombre AS primer_nombre ," +
	" d.segundo_nombre AS segundo_nombre  " +

	
" FROM" +
	"  deudores d " +

"  WHERE " +
	
	" d.institucion = ? ";
	
	
	
	private static final String cargarDatosEmpresaStr = "SELECT " +
		"'' as codigo_paciente, "+ 
		"e.codigo as codigo_empresa, "+
		"t.numero_identificacion || coalesce('-'||t.digito_verificacion||' ',' ') || '- ' || e.razon_social as razon_social, "+
		"e.tercero as codigo_tercero, "+
		"e.direccion as direccion, "+
		"e.telefono as telefono, "+
		"coalesce(e.email,'') as email, "+
		"coalesce(e.nombre_representante,'') as nombre_representante, "+
		"facturacion.getNombreContacto(e.codigo)  as nombre_contacto "+ 
		"FROM empresas e "+ 
		"INNER JOIN terceros t on (t.codigo = e.tercero) "+ 
		"WHERE e.codigo = ?";
	
	/**
	 * Cadena para cargar los datos del tercero
	 */
	private static final String cargarDatosTerceroStr = "SELECT " +
		"'' as codigo_paciente, "+ 
		"'' as codigo_empresa, "+
		"t.numero_identificacion || coalesce('-'||t.digito_verificacion||' ',' ') || '- ' || t.descripcion as razon_social, "+
		"t.codigo as codigo_tercero, "+
		"'' as direccion, "+
		"'' as telefono, "+
		"'' as email, "+
		"'' as nombre_representante, "+
		"'' as nombre_contacto "+ 
		"FROM terceros t "+ 
		"WHERE t.codigo = ?";
	
	/**
	 * Cadena para cargar los datos del paciente
	 */
	private static final String cargarDatosPacienteStr = "SELECT "+ 
		"p.codigo as codigo_paciente, " +
		"'' as codigo_empresa,  p.numero_identificacion as numIdentificacion,"+
		"p.tipo_identificacion || ' ' || p.numero_identificacion || ' - ' || p.primer_apellido || coalesce(' '||p.segundo_apellido||' ',' ') || p.primer_nombre || coalesce(' '||p.segundo_nombre,' ') as razon_social, "+
		"'' as codigo_tercero, "+
		"p.direccion as direccion, "+
		"coalesce(p.telefono_fijo,0) as telefono, "+
		"coalesce(p.email,'') as email, "+
		"'' as nombre_representante, "+
		"'' as nombre_contacto "+ 
		" ,  p.primer_apellido primer_apellido, " +
		" p.segundo_apellido segundo_apellido, " +
		" p.primer_nombre primer_nombre, " +
		" p.segundo_nombre segundo_nombre, " +
		" p.tipo_identificacion tipo_identificacion "+
		"from personas p "+ 
		"WHERE p.codigo = ?";
	
	/**
	 * Cadena para verificar si una empresa ya es deudor
	 */
	private static final String verificarDeudorEmpresaStr = "SELECT codigo as codigo from deudores WHERE codigo_empresa = ?";
	
	/**
	 * Cadena para verificar si una empresa ya es deudor a partir del codigo del tercero
	 */
	private static final String verificarDeudorEmpresa02Str = "SELECT d.codigo as codigo " +
		"from deudores d " +
		"inner join empresas e on(e.codigo = d.codigo_empresa) " +
		"WHERE e.tercero = ?";
	

	
	
	
	/**
	 * Cadena para verificar si un tercero ya es deudor
	 */
	private static final String verificarDeudorTerceroStr = "SELECT codigo as codigo from deudores WHERE codigo_tercero = ?";
	
	/**
	 * Cadena para verificar si un tercero ya es deudor y tiene la misma identificacion del paciente
	 */
	private static final String verificarDeudorTercero02Str = "SELECT " +
		"d.codigo as codigo, " +
		"d.representante_legal as nombre_representante, " +
		"d.nombre_contacto as nombre_contacto, " +
		"coalesce(d.observaciones,'') as observaciones "+ 
		"from personas pac "+ 
		"inner join terceros t on(t.numero_identificacion = pac.numero_identificacion) "+ 
		"INNER JOIN deudores d ON(d.codigo_tercero = t.codigo) "+ 
		"WHERE pac.codigo = ?" ; 
	
	/**
	 * Cadena para verificar si un pacientes es deudor
	 */
	private static final String verificarDeudorPacienteStr = "SELECT codigo as codigo from deudores where codigo_paciente =  ?";
	
	
	/**
	 * Cadena para cargar el deudor
	 */
	private static final String cargarDeudorStr = "SELECT "+ 
		"d.codigo as codigo, "+
		"coalesce(d.codigo_tercero||'','') as codigo_tercero, "+
		"coalesce(d.codigo_empresa||'','') as codigo_empresa, "+
		"coalesce(d.codigo_paciente||'','') as codigo_paciente, "+
		"coalesce(d.activo,'"+ConstantesBD.acronimoNo+"') as activo, "+
		"coalesce(d.direccion,'') as direccion, "+
		"coalesce(d.telefono,'') as telefono, "+
		"coalesce(d.e_mail,'') as email, "+
		"coalesce(d.dias_vencimiento_fac||'','') as dias_vencimiento_fac, "+
		"coalesce(d.representante_legal,'') as nombre_representante, "+
		"coalesce(d.nombre_contacto,'') as nombre_contacto, "+
		"coalesce(d.observaciones,'') as observaciones, "+
		"d.usuario_modifica as usuario_modifica, "+
		
		"d.tipo as tipo "+ 
		"FROM deudores d "+ 
		"WHERE d.codigo = ?";
	
	//***********************************************************************************************
	
	/**
	 * Metodo de carga de terceros.
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> cargarTerceros (Connection con, int institucion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>() ;
		PreparedStatementDecorator pst = null;
		
			
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaTerceros,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1, institucion);
			// ejecutar consulta y cargar resultados en el hashMap
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e) {
			logger.error(e+" Error en consulta de terceros en la funcionalidad deudores");
		}
		resultados.put("INDICES",indicesTerceros);
		return resultados;	
	}
		
	/**
	 * Metodo de consulta de deudores por un tipo seleccionado
	 * @param con
	 * @param tipoDeudorSeleccionado
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> consultarDeudoresPorTipoSelec(Connection con, String tipoDeudorSeleccionado, int institucion)
	{
		logger.info("\n entre a consultarDeudoresPorTipoSelec");
		
		logger.info("EL TIPO DE DEUDOR SELECCIONADO ES :"+ tipoDeudorSeleccionado);
		
		
		HashMap<String, Object> resultados = new HashMap<String, Object>() ;
		PreparedStatementDecorator pst = null;
		try
		{
			
			if (tipoDeudorSeleccionado.equals("DEUEMPRE"))
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDeudoresEmpresas,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
				pst.setString(1, tipoDeudorSeleccionado);
				pst.setInt(2, institucion);
				//ejecutar consulta y cargar resultados en el hashMap
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				resultados.put("INDICES",indicesDeudoresEmpresas);
			}
			else if (tipoDeudorSeleccionado.equals("DEUOTROS"))
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDeudoresTerceros,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
				pst.setString(1, tipoDeudorSeleccionado);
				pst.setInt(2, institucion);
				//ejecutar consulta y cargar resultados en el hashMap
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				resultados.put("INDICES",indicesDeudoresTerceros);
			}
//			*********** anexo 791 ***********
			else if(tipoDeudorSeleccionado.equals("PACI"))
			{
				logger.info("consultarDeudoresPacientes:::::::::::."+consultarDeudoresPacientes+"\n"+tipoDeudorSeleccionado+"\n"+institucion);
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDeudoresPacientes,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
				pst.setInt(1, institucion);
				//ejecutar consulta y cargar resultados en el hashMap
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				resultados.put("INDICES",indicesDeudoresPacientes);
			}
			
			else if(tipoDeudorSeleccionado.equals("AOTR"))
			{
				logger.info("consultarDeudoresOtros:::::::::::."+consultaDeudorOtroStr+"\n"+tipoDeudorSeleccionado+"\n"+institucion);
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDeudorOtroStr,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
				pst.setInt(1, institucion);
				//ejecutar consulta y cargar resultados en el hashMap
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				resultados.put("INDICES",indicesDeudoresOtros);
			}
			
//			*********************************
			
		
			
		}
		catch (SQLException e) {
			logger.error(e+" Error en consulta de deudores en la funcionalidad deudores");
		}
		return resultados;	
	}
	
	
	
	
	
	/**
	 * Método para cargar información del nuevo deudor
	 * @param con
	 * @param codigo
	 * @param tipoDeudor
	 * @return
	 */
	public static DtoDeudor cargarInformacionNuevoDeudor(Connection con,String codigo,String tipoDeudor)
	{
		
		
		
		
		DtoDeudor deudor = new DtoDeudor();
		try
		{
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			//Dependiendo del tipo de deudor se carga la informacion correspondiente
			
			
			
			
			
			if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
			{
				pst  = new PreparedStatementDecorator(con.prepareStatement(cargarDatosEmpresaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			}
			else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
			{
				pst = new PreparedStatementDecorator(con.prepareStatement(cargarDatosTerceroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			}
			else if (tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente))
			{
				pst = new PreparedStatementDecorator(con.prepareStatement(cargarDatosPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			}else{
				pst = new PreparedStatementDecorator(con.prepareStatement(consultaDeudor, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
			}
			pst.setInt(1,Integer.parseInt(codigo));
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				deudor.setCodigoEmpresa(rs.getString("codigo_empresa"));
				deudor.setCodigoTercero(rs.getString("codigo_tercero"));
				deudor.setCodigoPaciente(rs.getString("codigo_paciente"));
				deudor.setActivo(ConstantesBD.acronimoSi);
				deudor.setRazonSocial(rs.getString("razon_social"));
				deudor.setDireccion(rs.getString("direccion"));
				deudor.setTelefono(rs.getString("telefono"));
				deudor.setEmail(rs.getString("email"));
				deudor.setNombreRepresentante(rs.getString("nombre_representante"));
				deudor.setNombreContacto(rs.getString("nombre_contacto"));
				deudor.setTipoDeudor(tipoDeudor);
			
				
			}
			
			pst.close();
			rs.close();
			
			if(!deudor.getTipoDeudor().equals(""))
			{
				boolean existeDeudorPaciente = false;
				boolean existeDeudorTercero = false;
				boolean existeDeudorEmpresa = false;
				boolean existeDeudorOtro = false;
				
				logger.info("TIPO DEUDOR********************************************"+tipoDeudor);
				
				//***************VALIDACIONES TERCERO******************************
				if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
				{
					
					//Agregados por Anexo 958
					deudor.setPrimerNombre(rs.getString("primer_nombre"));
					deudor.setSegundoNombre(rs.getString("segundo_nombre"));
					deudor.setPrimerApellido(rs.getString("primer_apellido"));
					deudor.setSegundoApellido(rs.getString("segundo_apellido"));
					deudor.setNumeroIdentificacion(rs.getString("numero_identificacion"));
					
					
					//Se verifica que ese tercero no exista como deudor tercero----------------------------------------------------
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorTerceroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(deudor.getCodigoTercero()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						existeDeudorTercero = true;
						deudor.setCodigo(rs.getString("codigo"));
					}
					pst.close();
					rs.close();
					
					//Se verifica si ese tercero existe como deudor empresa
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorEmpresa02Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(deudor.getCodigoTercero()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						existeDeudorEmpresa = true;
						deudor.setCodigo(rs.getString("codigo"));
						
					}
					pst.close();
					rs.close();
					
					//Se verifica si existe como deudor otro
					/**
					 * Cadena para verificar si un tercero  ya es deudor a partir del codigo de Otro
					 */
					
					logger.info("****************** VERIFICACION DEUDOR =============="+ deudor.getCodigoTercero());
				 String verificarDeudorOtroStr = "SELECT d.codigo as codigo " +
						"from deudores d " +
						
						"WHERE d.numero_identificacion = (select numero_identificacion from terceros  t where t.codigo="+deudor.getCodigoTercero() +")";
					
				 
				    logger.info("CONSULTA ++++===="+ "           "+verificarDeudorOtroStr);
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorOtroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				   
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						existeDeudorOtro = true;
						deudor.setCodigo(rs.getString("codigo"));
						
					}
					pst.close();
					rs.close();
					
					
				}
				//*************VALIDACIONES EMPRESA**********************************************+
				else if(tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
				{
					//Se verifica que esa empresa no exista como deudor tercero----------------------------------------------------
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorTerceroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(deudor.getCodigoTercero()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setCodigo(rs.getString("codigo"));
						existeDeudorTercero = true;
						
					}
					pst.close();
					rs.close();
					
					//Se verifica si esa empresa existe como deudor empresa
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorEmpresaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(deudor.getCodigoEmpresa()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setCodigo(rs.getString("codigo"));
						existeDeudorEmpresa = true;
					}
					pst.close();
					rs.close();
					
					//Se verifica si existe como deudor otro
					/**
					 * Cadena para verificar si un tercero  ya es deudor a partir del codigo de Otro
					 */
					
					logger.info("****************** VERIFICACION DEUDOR EN EMPRESA =============="+ deudor.getCodigoTercero());
				 String verificarDeudorOtroStr2 = "SELECT d.codigo as codigo " +
						"from deudores d " +
						 
						 
						"WHERE d.numero_identificacion = (select numero_identificacion from terceros t where t.codigo = (select tercero from empresas e where e.codigo="+deudor.getCodigoEmpresa()+"))";
					
				 
				    logger.info("CONSULTA ++++===="+ "           "+verificarDeudorOtroStr2);
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorOtroStr2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				   
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						existeDeudorOtro = true;
						deudor.setCodigo(rs.getString("codigo"));
						
					}
					pst.close();
					rs.close();
					
				}
				
				
				
				
				//********VALIDACIONES PACIENTE*****************************************************
				else if (tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					//Se verifica si YA EXISTE DEUDOR COMO TERCERO con la misma identificacion del paciente
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorTercero02Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(deudor.getCodigoPaciente()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setCodigo(rs.getString("codigo"));
						deudor.setNombreRepresentante(rs.getString("nombre_representante"));
						deudor.setNombreContacto(rs.getString("nombre_contacto"));
						deudor.setObservaciones(rs.getString("observaciones"));
						existeDeudorTercero = true;
						
					}
					pst.close();
					rs.close();
					
					//Se verifica si ese paciente existe como deudor paciente
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(deudor.getCodigoPaciente()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setCodigo(rs.getString("codigo"));
						existeDeudorPaciente = true;
						
					}
					pst.close();
					rs.close();
					
					
					//Se verifica si existe como deudor otro
					/**
					 * Cadena para verificar si un tercero  ya es deudor a partir del codigo de Otro
					 */
					
					logger.info("****************** VERIFICACION DEUDOR =============="+ deudor.getCodigoPaciente());
				 String verificarDeudorOtroStr3 = "SELECT d.codigo as codigo " +
						"from deudores d " +
						
						"WHERE d.numero_identificacion = (select numero_identificacion from personas  p where p.codigo=(select codigo_paciente from pacientes pa where pa.codigo_paciente="+deudor.getCodigoPaciente()+")) and "+
					     "d.tipo_identificacion = (select tipo_identificacion from personas  p where p.codigo=(select codigo_paciente from pacientes pa where pa.codigo_paciente="+deudor.getCodigoPaciente()+"))" ;
				 
				    logger.info("CONSULTA ++++===="+ "           "+verificarDeudorOtroStr3);
					pst = new PreparedStatementDecorator(con.prepareStatement(verificarDeudorOtroStr3, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				   
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						existeDeudorOtro = true;
						deudor.setCodigo(rs.getString("codigo"));
						
					}
					pst.close();
					rs.close();
				}
				
				
				logger.info("EXISTE DEUDOR OTRO ?????????????????????"+ existeDeudorOtro);
				deudor.setExisteDeudorEmpresa(existeDeudorEmpresa);
				deudor.setExisteDeudorTercero(existeDeudorTercero);
				deudor.setExisteDeudorPaciente(existeDeudorPaciente);
				deudor.setExisteDeudorOtro(existeDeudorOtro);
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionNuevoDeudor: "+e);
		}
		
		return deudor;
	}	
	/**
	 * Método usado para ingresar un nuevo deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public static ResultadoBoolean ingresar(Connection con,DtoDeudor deudor)
	{
		
		logger.info("TIPO IDENTIFICACION ++++ ===  " + deudor.getTipoIdentificacion());
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String consulta01 = "INSERT INTO deudores ( "+
				"codigo, "+ //1
				"tipo, "+ //2
				"codigo_tercero, "+ //3
				"codigo_empresa, "+ //4
				"codigo_paciente, "+ //5
				"activo, " + //6
				"direccion, " + //7
				"telefono, "+ //8
				"e_mail, "+ //9
				"representante_legal, "+ //10
				"nombre_contacto, " + //11
				"observaciones, " + //12
				"institucion, " + //13				
				"fecha_modifica, "+//14
				"hora_modifica, "+//15
				"usuario_modifica, " + //16
				"dias_vencimiento_fac, " + //2 -segun anexo 809-
				"tipo_identificacion, " + //14
				"numero_identificacion, " + //15
				"primer_apellido, " + //16
				"segundo_apellido, " + //17
				"primer_nombre, " + //18
				"segundo_nombre " + //19
				
				") "+ 
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?)";
			
			String consulta02 = "UPDATE deudores SET " +
				"codigo_empresa = ?, " +
				"codigo_paciente = ?, " +
				"codigo_tercero = ?, " +
				"tipo = '"+deudor.getTipoDeudor()+"', " +
				
				"fecha_modifica = current_date, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"usuario_modifica = ?, " +
				"representante_legal = ?, " +
				"nombre_contacto = ?,  " +
				"direccion = ?,  " +
				"telefono = ?,  " +
				"e_mail = ?,  " +
				"observaciones = ?  " +
				"where codigo = ?";
			
			//Si el deudor a guardar es de tipo empresa y ya existe como tercero de hace la actualización
			if(deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa) && deudor.isExisteDeudorTercero()||
					deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente)&&deudor.isExisteDeudorTercero() || deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa)&&deudor.isExisteDeudorOtro() || deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente)&&deudor.isExisteDeudorOtro()||deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros)&&deudor.isExisteDeudorOtro() )
			{
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta02, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				if(Utilidades.convertirAEntero(deudor.getCodigoEmpresa())>0)
				{
					pst.setInt(1, Integer.parseInt(deudor.getCodigoEmpresa()));
				}
				else
				{
					pst.setNull(1,Types.INTEGER);
				}
				if(Utilidades.convertirAEntero(deudor.getCodigoPaciente())>0)
				{
					pst.setInt(2, Integer.parseInt(deudor.getCodigoPaciente()));
				}
				else
				{
					pst.setNull(2,Types.INTEGER);
				}
				

				if(Utilidades.convertirAEntero(deudor.getCodigoTercero())> 0){
					
					pst.setInt(3, Integer.parseInt(deudor.getCodigoTercero()));
					
				}else{
					
					pst.setNull(3,Types.INTEGER);
				}
				
				
				pst.setString(4,deudor.getUsuarioModifica().getLoginUsuario());
				pst.setString(5,deudor.getNombreRepresentante());
				pst.setString(6,deudor.getNombreContacto());
				
				if(!UtilidadTexto.isEmpty(deudor.getDireccion())){
				pst.setString(7, deudor.getDireccion());
				
				}else {
					pst.setNull(7,Types.CHAR);
				}
				
				if(!UtilidadTexto.isEmpty(deudor.getTelefono())){
					pst.setString(8, deudor.getTelefono());
					
					}else {
						pst.setNull(8,Types.CHAR);
					}
				
				if(!UtilidadTexto.isEmpty(deudor.getEmail())){
					pst.setString(9, deudor.getEmail());
					
					}else {
						pst.setNull(9,Types.CHAR);
					}
				
				if(!UtilidadTexto.isEmpty(deudor.getObservaciones())){
					pst.setString(10, deudor.getObservaciones());
					
					}else {
						pst.setNull(10,Types.CHAR);
					}
				
			
				
				pst.setInt(11,Integer.parseInt(deudor.getCodigo()));
				
				if(pst.executeUpdate()<=0)
				{
					
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas actualizando el deudor a tipo empresa. Proceso cancelado");
					
				}
				pst.close();
			}
			else
			{
				logger.info ("SQLBASE GUARDANDO DEUDOR");
				
				//PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta01, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con, consulta01);
				deudor.setCodigo(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_deudores")+"");
				pst.setInt(1,Integer.parseInt(deudor.getCodigo()));
				pst.setString(2,deudor.getTipoDeudor());
				if(deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
				{
					pst.setInt(3,Integer.parseInt(deudor.getCodigoTercero()));
				}
				else
				{
					pst.setNull(3,Types.INTEGER);
				}
				if(deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
				{
					
					pst.setInt(4,Utilidades.convertirAEntero(deudor.getCodigoEmpresa()));
				}
				else
				{
					pst.setNull(4,Types.INTEGER);
				}
				
				if(deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					pst.setInt(5,Integer.parseInt(deudor.getCodigoPaciente()));
				}
				else
				{
					pst.setNull(5,Types.INTEGER);
				}
				
				pst.setString(6,deudor.getActivo());
				if(!deudor.getDireccion().equals("")&&deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
				{
					pst.setString(7,deudor.getDireccion());
				}
				else
				{
					pst.setNull(7,Types.VARCHAR);
				}
				if(!deudor.getTelefono().equals("")&&deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
				{
					pst.setString(8,deudor.getTelefono());
				}
				else
				{
					pst.setNull(8,Types.VARCHAR);
				}
				if(!deudor.getEmail().equals("")&&deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
				{
					pst.setString(9,deudor.getEmail());
				}
				else
				{
					pst.setNull(9,Types.VARCHAR);
				}
				if(!deudor.getNombreRepresentante().equals("")&&!deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
				{
					pst.setString(10,deudor.getNombreRepresentante());
				}
				else
				{
					pst.setNull(10,Types.VARCHAR);
				}
				if(!deudor.getNombreContacto().equals("")&&!deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
				{
					pst.setString(11,deudor.getNombreContacto());
				}
				else
				{
					pst.setNull(11,Types.VARCHAR);
				}
				if(!deudor.getObservaciones().equals(""))
				{
					pst.setString(12,deudor.getObservaciones());
				}
				else
				{
					pst.setNull(12,Types.VARCHAR);
				}
				pst.setInt(13, deudor.getCodigoInstitucion());
				pst.setString(14,deudor.getUsuarioModifica().getLoginUsuario());
				
				if(!deudor.getDiasVencimientoFac().equals(""))
					pst.setInt(15, Utilidades.convertirAEntero(deudor.getDiasVencimientoFac()));
				else
					pst.setNull(15, Types.INTEGER);
				
				
				if(!UtilidadTexto.isEmpty(deudor.getTipoIdentificacion())){
					
					pst.setString(16,deudor.getTipoIdentificacion());
				}else{
					pst.setNull(16,Types.VARCHAR);
				}
			
	           if(!UtilidadTexto.isEmpty(deudor.getNumeroIdentificacion())){
	        	
				   pst.setString(17,deudor.getNumeroIdentificacion());
				}else{
					pst.setNull(17,Types.VARCHAR);
				}	
				
	           	
				
	           
	           if(!UtilidadTexto.isEmpty(deudor.getPrimerApellido())){
					
					pst.setString(18,deudor.getPrimerApellido());
				}else{
					pst.setNull(18,Types.VARCHAR);
				}	
	           
	           
	           if(!UtilidadTexto.isEmpty(deudor.getSegundoApellido())){
					
					pst.setString(19,deudor.getSegundoApellido());
				}else{
					pst.setNull(19,Types.VARCHAR);
				}	
	           
	           if(!UtilidadTexto.isEmpty(deudor.getPrimerNombre())){
					
					pst.setString(20,deudor.getPrimerNombre());
				}else{
					pst.setNull(20,Types.VARCHAR);
				}
				
	           
	           if(!UtilidadTexto.isEmpty(deudor.getSegundoNombre())){
					
					pst.setString(21,deudor.getSegundoNombre());
				}else{
					pst.setNull(21,Types.VARCHAR);
				}
				
				logger.info("sql-> INSERCION DE DEUDORES ++"+pst);
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas registrando el nuevo deudor. Proceso cancelado");	
				}
				pst.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en ingresar: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Error ingresando el deudor: "+e);
		}
		return resultado;
	}
	
	
	/**
	 * Método implementado para cargar la información del deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoDeudor cargar(Connection con,HashMap<String,Object> campos)
	{
		DtoDeudor deudor = new DtoDeudor();
		try
		{
			//**************SE TOMAN PARÁMETROS*********************************
			int codigoDeudor = Utilidades.convertirAEntero(campos.get("codigoDeudor").toString());
			//******************************************************************
			logger.info("paso por aqui A: "+codigoDeudor);
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(cargarDeudorStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoDeudor);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				deudor.setCodigo(rs.getString("codigo"));
				deudor.setCodigoEmpresa(rs.getString("codigo_empresa"));
				deudor.setCodigoTercero(rs.getString("codigo_tercero"));
				deudor.setCodigoPaciente(rs.getString("codigo_paciente"));
				deudor.setTipoDeudor(rs.getString("tipo"));
				deudor.setActivo(rs.getString("activo"));
				deudor.setDireccion(rs.getString("direccion"));
				deudor.setTelefono(rs.getString("telefono"));
				deudor.setEmail(rs.getString("email"));
				//****** Anexo 809 ***********
				deudor.setDiasVencimientoFac(rs.getString("dias_vencimiento_fac"));
				//****** Anexo 809 ***********
				deudor.setNombreRepresentante(rs.getString("nombre_representante"));
				deudor.setNombreContacto(rs.getString("nombre_contacto"));
				deudor.setObservaciones(rs.getString("observaciones"));
				deudor.getUsuarioModifica().cargarUsuarioBasico(con,rs.getString("usuario_modifica"));
			}
			pst.close();
			rs.close();
			
			logger.info("paso por aqui B: "+deudor.getTipoDeudor());
			//Si se encontró deudor se prosigue a consultar la informacion adicional dependiendo de cada deudor
			if(!deudor.getCodigo().equals(""))
			{
				if(deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
				{
					pst = new PreparedStatementDecorator(con.prepareStatement(cargarDatosEmpresaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Utilidades.convertirAEntero(deudor.getCodigoEmpresa()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setRazonSocial(rs.getString("razon_social"));
						deudor.setDireccion(rs.getString("direccion"));
						deudor.setTelefono(rs.getString("telefono"));
						deudor.setEmail(rs.getString("email"));
						deudor.setNombreRepresentante(rs.getString("nombre_representante"));
						deudor.setNombreContacto(rs.getString("nombre_contacto"));
					}
				}
				else if(deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
				{
					pst = new PreparedStatementDecorator(con.prepareStatement(cargarDatosTerceroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Utilidades.convertirAEntero(deudor.getCodigoTercero()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setRazonSocial(rs.getString("razon_social"));
					}
				}
				else if (deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					pst = new PreparedStatementDecorator(con.prepareStatement(cargarDatosPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Utilidades.convertirAEntero(deudor.getCodigoPaciente()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setRazonSocial(rs.getString("razon_social"));
						deudor.setDireccion(rs.getString("direccion"));
						deudor.setTelefono(rs.getString("telefono"));
						deudor.setEmail(rs.getString("email"));
						deudor.setNumeroIdentificacion(rs.getString("numIdentificacion"));
						deudor.setPrimerApellido(rs.getString("primer_apellido"));
						deudor.setSegundoApellido(!rs.getString("segundo_apellido").equals("")?rs.getString("segundo_apellido"):"");
						deudor.setPrimerNombre(rs.getString("primer_nombre"));
						deudor.setSegundoNombre(!rs.getString("segundo_nombre").equals("")?rs.getString("segundo_nombre"):"");
						deudor.setTipoIdentificacion(!rs.getString("tipo_identificacion").equals("")?rs.getString("tipo_identificacion"):"");
						
					}
				}else if (deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoOtro)){
					
					pst = new PreparedStatementDecorator(con.prepareStatement(consultaDeudorOtro, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Utilidades.convertirAEntero(deudor.getCodigo()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						deudor.setRazonSocial(rs.getString("razon_social"));
						
					}
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargar: "+e);
		}
		return deudor;
	}
	
	/**
	 * Método que realiza una modificación del deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public static ResultadoBoolean modificar(Connection con,DtoDeudor deudor)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String consulta = "update deudores set " +
				"activo = ?, " + //1
				"direccion = ?, " + //2
				"telefono = ?, " + //3
				"e_mail = ?, " + //4
				"representante_legal = ?, " + //5
				"nombre_contacto = ?, " + //6
				"observaciones = ?, " + //7
				"fecha_modifica = current_date, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"usuario_modifica = ?, " + //8
				"dias_vencimiento_fac = ?   " + //9 -segun anexo 809-
				
				"WHERE codigo = ?"; //16
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con ,consulta);
		
			pst.setString(1,deudor.getActivo());
			if(!deudor.getDireccion().equals("")&&deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
			{
				pst.setString(2,deudor.getDireccion());
			}
			else
			{
				pst.setNull(2,Types.VARCHAR);
			}
			if(!deudor.getTelefono().equals("")&&deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
			{
				pst.setString(3,deudor.getTelefono());
			}
			else
			{
				pst.setNull(3,Types.VARCHAR);
			}
			if(!deudor.getEmail().equals("")&&deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
			{
				pst.setString(4,deudor.getEmail());
			}
			else
			{
				pst.setNull(4,Types.VARCHAR);
			}
			if(!deudor.getNombreRepresentante().equals("")&&!deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
			{
				pst.setString(5,deudor.getNombreRepresentante());
			}
			else
			{
				pst.setNull(5,Types.VARCHAR);
			}
			if(!deudor.getNombreContacto().equals("")&&!deudor.getTipoDeudor().equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
			{
				pst.setString(6,deudor.getNombreContacto());
			}
			else
			{
				pst.setNull(6,Types.VARCHAR);
			}
			if(!deudor.getObservaciones().equals(""))
			{
				pst.setString(7,deudor.getObservaciones());
			}
			else
			{
				pst.setNull(7,Types.VARCHAR);
			}
			pst.setString(8,deudor.getUsuarioModifica().getLoginUsuario());
			
			if(!deudor.getDiasVencimientoFac().equals(""))
				pst.setInt(9, Utilidades.convertirAEntero(deudor.getDiasVencimientoFac()));
			else
				pst.setNull(9, Types.INTEGER);
			
			

			
			
			pst.setInt(10,Utilidades.convertirAEntero(deudor.getCodigo()));
			
			
			logger.info("\n\n\n\n\n\n\n\n");
			logger.info(" MODIFICAR DEUDOR \n"+pst);
			
			logger.info("\n\n\n\n\n\n\n\n");
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("Problemas internos al tratar de modificar el deudor");
			}
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("error en modificar: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió excepción al tratar de modificar el deudor: "+e);
		}
		return resultado;
	}
	
	
	
	
	/**
	 * Método que realiza una modificación del deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public static ResultadoBoolean modificarDeudor(Connection con,DtoDeudor deudor)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
		
			
			
			
			String consulta = "update deudores set  codigo=codigo ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getActivo())?" , activo ='"+deudor.getActivo()+"'": ""; 
			consulta+=!UtilidadTexto.isEmpty(deudor.getNumeroIdentificacion())?" ,numero_identificacion='"+deudor.getNumeroIdentificacion()+"'":""; 
			consulta+=!UtilidadTexto.isEmpty(deudor.getTipoIdentificacion())? " ,tipo_identificacion='"+deudor.getTipoIdentificacion()+"'": " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getPrimerApellido())?" ,primer_apellido='"+deudor.getPrimerApellido()+"'": " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getSegundoApellido())?" ,segundo_apellido='"+deudor.getSegundoApellido()+"'":  " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getPrimerNombre())?"  ,primer_nombre='"+deudor.getPrimerNombre()+"'": " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getSegundoNombre())?" ,segundo_nombre='"+deudor.getSegundoNombre()+"'": " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getCodigoTercero())?" , codigo_tercero="+deudor.getCodigoTercero()+" ": " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getCodigoTercero())?" , codigo_paciente="+deudor.getCodigoPaciente()+" ": " ";
			consulta+=!UtilidadTexto.isEmpty(deudor.getTipoDeudor())? ", tipo='"+deudor.getTipoDeudor()+"'": "";
			consulta+="	,fecha_modifica ='"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) +"'";
			consulta+="	, hora_modifica = '"+UtilidadFecha.getHoraActual()+"'";
			consulta+=",	usuario_modifica ='"+deudor.getUsuarioModifica().getLoginUsuario()+"'"; //8
			consulta+="	WHERE codigo = "+deudor.getCodigo();
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con ,consulta);
		
			logger.info("\n\n\n\n\n\n\n\n");
			logger.info(" MODIFICAR DEUDOR ------------------------>>>>>>>>>>>>>>>>>><\n"+pst);
			
			logger.info("\n\n\n\n\n\n\n\n");
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("Problemas internos al tratar de modificar el deudor");
			}
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("error en modificar: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió excepción al tratar de modificar el deudor: "+e);
		}
		return resultado;
	}

	public static DtoDeudor cargar(Connection con, String tipoId, String numeroId, String tipo)
	{
		String cargarDeudorStr = "SELECT "+ 
									"d.codigo as codigo, "+
									"coalesce(d.codigo_tercero||'','') as codigo_tercero, "+
									"coalesce(d.codigo_empresa||'','') as codigo_empresa, "+
									"coalesce(d.codigo_paciente||'','') as codigo_paciente, "+
									"coalesce(d.activo,'"+ConstantesBD.acronimoNo+"') as activo, "+
									"coalesce(d.direccion,'') as direccion, "+
									"coalesce(d.telefono,'') as telefono, "+
									"coalesce(d.e_mail,'') as email, "+
									"coalesce(d.dias_vencimiento_fac||'','') as dias_vencimiento_fac, "+
									"coalesce(d.representante_legal,'') as nombre_representante, "+
									"coalesce(d.nombre_contacto,'') as nombre_contacto, "+
									"coalesce(d.observaciones,'') as observaciones, "+
									"coalesce(d.primer_apellido,'') as primer_apellido, "+
									"coalesce(d.segundo_apellido,'') as segundo_apellido, "+
									"coalesce(d.primer_nombre,'') as primer_nombre, "+
									"coalesce(d.segundo_nombre,'') as segundo_nombre, "+
									"coalesce(d.es_empresa,'') as es_empresa, "+
									"d.usuario_modifica as usuario_modifica, "+
									"d.tipo as tipo "+ 
								"FROM deudores d "+ 
									"WHERE " +
										"d.numero_identificacion = ? " +
										"AND "+
										"d.tipo_identificacion = ?";
		if(tipo!=null)
		{
			cargarDeudorStr+=" AND tipo=?";
		}
		PreparedStatementDecorator psd = new PreparedStatementDecorator(con, cargarDeudorStr);
		try
		{
			psd.setString(1, numeroId);
			psd.setString(2, tipoId);
			if(tipo!=null)
			{
				psd.setString(3, tipo);
			}
			logger.info(psd);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			DtoDeudor deudor=new DtoDeudor();
			if(rs.next())
			{
				deudor.setCodigo(rs.getString("codigo"));
				deudor.setCodigoEmpresa(rs.getString("codigo_empresa"));
				deudor.setCodigoTercero(rs.getString("codigo_tercero"));
				deudor.setCodigoPaciente(rs.getString("codigo_paciente"));
				deudor.setTipoDeudor(rs.getString("tipo"));
				deudor.setActivo(rs.getString("activo"));
				deudor.setDireccion(rs.getString("direccion"));
				deudor.setTelefono(rs.getString("telefono"));
				deudor.setEmail(rs.getString("email"));
				//****** Anexo 809 ***********
				deudor.setDiasVencimientoFac(rs.getString("dias_vencimiento_fac"));
				//****** Anexo 809 ***********
				deudor.setNombreRepresentante(rs.getString("nombre_representante"));
				deudor.setNombreContacto(rs.getString("nombre_contacto"));
				deudor.setObservaciones(rs.getString("observaciones"));
				deudor.setPrimerApellido(rs.getString("primer_apellido"));
				deudor.setSegundoApellido(rs.getString("segundo_apellido"));
				deudor.setPrimerNombre(rs.getString("primer_nombre"));
				deudor.setSegundoNombre(rs.getString("segundo_nombre"));
				deudor.getUsuarioModifica().cargarUsuarioBasico(con,rs.getString("usuario_modifica"));
				UtilidadBD.cerrarObjetosPersistencia(psd, rs, null);
			}
			return deudor;
		} catch (SQLException e)
		{
			logger.error("Error cargando el deudor",e);
		}
		return null;
	}
	
	
	
	
}
