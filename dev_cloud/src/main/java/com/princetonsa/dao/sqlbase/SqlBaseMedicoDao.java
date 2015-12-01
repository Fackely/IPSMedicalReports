/*
 * Created on 29-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.Answer;
import util.ConstantesBD;
import util.RespuestaInsercionPersona;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Especialidad;
import com.princetonsa.mundo.Especialidades;
import com.princetonsa.mundo.Persona;

/**
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseMedicoDao {
	
	private static Logger logger = Logger.getLogger(SqlBaseMedicoDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar las especialidades de un medico.
	 */
	private static final String agregarEspecialidadesStr = "INSERT INTO especialidades_medicos values (?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los datos de un medico.
	 */
	//Consulta antes del inner join "SELECT per.numero_identificacion AS numeroIdentificacion, per.tipo_identificacion AS codigoTipoIdentificacion, ti.nombre AS tipoIdentificacion, per.codigo_departamento_nacimiento AS codDepartamentoIdentificacion, per.codigo_ciudad_nacimiento AS codigoCiudadIdentificacion, ciu.nombre AS ciudadIdentificacion, per.tipo_persona AS codigoTipoPersona, tp.nombre AS tipoPersona, per.fecha_nacimiento AS fechaNacimiento, per.estado_civil AS codigoEstadoCivil, estc.nombre AS estadoCivil, per.sexo AS codigoSexo, sex.nombre AS sexo, per.primer_nombre AS primerNombrePersona, per.segundo_nombre AS segundoNombrePersona, per.primer_apellido AS primerApellidoPersona, per.segundo_apellido AS segundoApellidoPersona, per.direccion AS direccion, per.codigo_departamento_vivienda AS codigoDepartamento, per.codigo_ciudad_vivienda AS codigoCiudad,ciu2.nombre AS ciudad, per.codigo_barrio_vivienda AS codigoBarrio, bv.nombre AS barrio, per.telefono AS telefono, per.email AS email,cat.nombre AS ocupacionMedica, med.numero_registro AS numeroRegistro, med.ocupacion_medica AS codigoOcupacionMedica, esp.nombre AS especialidad, espm.codigo_especialidad AS codigoEspecialidad, espm.activa_sistema as activaSistema, tv.nombre AS tipoVinculacion, med.tipo_vinculacion AS codigoTipoVinculacion, med.centro_costo AS codigoCentroCosto, ccosto.nombre AS centroCosto, med.fecha_vinculacion AS fechaVinculacion FROM personas per, medicos med, tipos_identificacion ti, ciudades ciu, tipos_personas tp, estados_civiles estc, sexo sex, ciudades ciu2, barrios bv, ocupaciones_medicas cat, especialidades esp, tipos_vinculacion tv, especialidades_medicos espm, centros_costo ccosto WHERE per.codigo=med.codigo_medico AND per.tipo_identificacion=ti.acronimo AND per.codigo_ciudad_nacimiento=ciu.codigo_ciudad AND per.codigo_departamento_nacimiento=ciu.codigo_departamento AND per.tipo_persona=tp.codigo AND per.estado_civil=estc.acronimo AND per.sexo=sex.codigo AND per.codigo_departamento_vivienda=ciu2.codigo_departamento AND per.codigo_ciudad_vivienda=ciu2.codigo_ciudad AND per.codigo_departamento_vivienda=bv.codigo_departamento AND per.codigo_ciudad_vivienda=bv.codigo_ciudad AND  per.codigo_barrio_vivienda=bv.codigo_barrio AND med.ocupacion_medica=cat.codigo AND med.tipo_vinculacion=tv.codigo AND med.codigo_medico=espm.codigo_medico AND esp.codigo=espm.codigo_especialidad AND ccosto.codigo=med.centro_costo AND med.codigo_medico=? ";
	private static final String cargarMedicoStr = "SELECT "+ 
		"per.numero_identificacion AS numeroIdentificacion, "+ 
		"per.tipo_identificacion AS codigoTipoIdentificacion, "+ 
		"getnombretipoidentificacion(per.tipo_identificacion) AS tipoIdentificacion, "+ 
		"per.codigo_pais_nacimiento AS codigoPaisIdentificacion, "+ 
		"per.codigo_departamento_nacimiento AS codDepartamentoIdentificacion, "+ 
		"per.codigo_ciudad_nacimiento AS codigoCiudadIdentificacion, "+ 
		"getdescripcionpais(per.codigo_pais_nacimiento) as paisIdentificacion, "+ 
		"getnombreciudad(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento,per.codigo_ciudad_nacimiento) as ciudadIdentificacion, "+ 
		"getnombredepto(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento) AS departamentoIdentificacion, "+
		"per.tipo_persona AS codigoTipoPersona, "+ 
		"tp.nombre AS tipoPersona, "+ 
		"to_char(per.fecha_nacimiento, 'YYYY-MM-DD') AS fechaNacimiento, "+ 
		"per.estado_civil AS codigoEstadoCivil, "+ 
		"estc.nombre AS estadoCivil, "+ 
		"per.sexo AS codigoSexo, "+ 
		"getdescripcionsexo(per.sexo) AS sexo, "+ 
		"per.primer_nombre AS primerNombrePersona, "+ 
		"per.segundo_nombre AS segundoNombrePersona, "+ 
		"per.primer_apellido AS primerApellidoPersona, "+ 
		"per.segundo_apellido AS segundoApellidoPersona, "+ 
		"per.direccion AS direccion, "+ 
		"per.codigo_pais_vivienda AS codigoPais, "+ 
		"per.codigo_departamento_vivienda AS codigoDepartamento, "+ 
		"per.codigo_ciudad_vivienda AS codigoCiudad, "+ 
		"getdescripcionpais(per.codigo_pais_vivienda) as pais, "+ 
		"getnombreciudad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda,per.codigo_ciudad_vivienda) as ciudad, "+ 
		"getnombredepto(per.codigo_pais_vivienda,per.codigo_departamento_vivienda) AS departamento, "+
		"per.codigo_barrio_vivienda AS codigoBarrio, "+ 
		"getdescripcionbarrio(per.codigo_barrio_vivienda) as barrio, "+ 
		"coalesce(per.telefono,'"+ConstantesBD.codigoNuncaValido+"') AS telefono," +
		"coalesce(per.telefono_fijo,"+ConstantesBD.codigoNuncaValido+") AS telefonofijo, "+ 
		"coalesce(per.telefono_celular,"+ConstantesBD.codigoNuncaValido+") AS telefonocelular, "+ 
		"per.email AS email, "+ 
		"per.codigo_pais_id AS codigoPaisId, "+ 
		"per.codigo_depto_id AS codigoDeptoId, "+ 
		"per.codigo_ciudad_id AS codigoCiudadId, "+ 
		"getnombreciudad(per.codigo_pais_id,per.codigo_depto_id,per.codigo_ciudad_id) AS nombreCiudadId, "+
		"getnombredepto(per.codigo_pais_id,per.codigo_depto_id) AS nombreDeptoId, "+ 
		"getDescripcionPais(per.codigo_pais_id) AS nombrePaisId, "+ 
		"cat.nombre AS ocupacionMedica, "+ 
		"med.numero_registro AS numeroRegistro, "+ 
		"med.ocupacion_medica AS codigoOcupacionMedica, "+ 
		"esp.nombre AS especialidad, "+ 
		"espm.codigo_especialidad AS codigoEspecialidad, "+ 
		"espm.activa_sistema as activaSistema, "+ 
		"tv.nombre AS tipoVinculacion, "+ 
		"med.tipo_vinculacion AS codigoTipoVinculacion, "+ 
		"to_char(med.fecha_vinculacion, 'YYYY-MM-DD') AS fechaVinculacion, "+ 
		"CASE WHEN med.codigo_pais_registro IS NULL THEN '' ELSE med.codigo_pais_registro END AS codigoPaisRegistro, "+ 
		"CASE WHEN med.codigo_depto_registro IS NULL THEN '' ELSE med.codigo_depto_registro END AS codigoDeptoRegistro, "+ 
		"CASE WHEN med.codigo_ciudad_registro IS NULL THEN '' ELSE med.codigo_ciudad_registro END AS codigoCiudadRegistro, "+ 
		"CASE WHEN med.codigo_ciudad_registro IS NULL THEN '' ELSE getnombreciudad(med.codigo_pais_registro,med.codigo_depto_registro,med.codigo_ciudad_registro) END AS nombreCiudadRegistro, "+ 
		"CASE WHEN med.codigo_depto_registro IS NULL THEN '' ELSE getnombredepto(med.codigo_pais_registro,med.codigo_depto_registro) END AS nombreDeptoRegistro, "+ 
		"CASE WHEN med.codigo_pais_registro IS NULL THEN '' ELSE getdescripcionpais(med.codigo_pais_registro) END AS nombrePaisRegistro, " +
		"coalesce(med.firma_digital,'') as firmaDigital, " +
		"med.convencion AS convencion, " +
		"med.tipo_liquidacion as tipoliquidacion "+ 
		"FROM personas per "+ 
		"INNER JOIN medicos med ON (per.codigo=med.codigo_medico) "+     
		"INNER JOIN tipos_personas tp ON (per.tipo_persona=tp.codigo) "+        
		"LEFT OUTER JOIN estados_civiles estc ON (per.estado_civil=estc.acronimo) "+     
		"INNER JOIN ocupaciones_medicas cat ON (med.ocupacion_medica=cat.codigo) "+     
		"LEFT OUTER JOIN tipos_vinculacion tv ON (med.tipo_vinculacion=tv.codigo) "+     
		"LEFT OUTER JOIN especialidades_medicos espm ON (med.codigo_medico=espm.codigo_medico) "+       
		"LEFT OUTER JOIN especialidades esp ON (esp.codigo=espm.codigo_especialidad) "+     
		"WHERE med.codigo_medico=?" ;

	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar un medico en la tabla de medicos.
	 */
	private static final String actualizarMedico="UPDATE medicos SET " +
		"ocupacion_medica=?, " +
		"fecha_vinculacion=?, " +
		"tipo_vinculacion=?, " +
		"numero_registro=?, " +
		"codigo_pais_registro = ?, " +
		"codigo_depto_registro = ?, " +
		"codigo_ciudad_registro = ?," +
		"firma_digital = ?, " +
		"convencion=?, " +
		"tipo_liquidacion=? " +
		"WHERE codigo_medico=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para contar las especialidades de un medico
	 */
	private static final String cuentaEspecialidadesStr="SELECT count(1) AS contador FROM especialidades_medicos WHERE codigo_medico = ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para borrar las especialidades de un medico.
	 */
	private static final String borrarEspecialidades = "DELETE FROM especialidades_medicos WHERE codigo_medico = ?";

	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para 
	 * saber si debo insertar una persona o ya existe en el
	 * sistema 
	 */
	private static final String deboInsertarPersonaStr="select count(1) as numResultados from administracion.personas where tipo_identificacion=? and numero_identificacion=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para inactivar un m�dico
	 */
	private static final String inactivarMedicoStr="INSERT into medicos_inactivos (codigo_medico, codigo_institucion) values (?,?) ";
	
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para 
	 * activar un m�dico inactivo
	 */
	private static final String activarMedicoInactivoStr="DELETE from medicos_inactivos where codigo_medico=? and codigo_institucion=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un medico en la tabla de medicos.
	 */
	private static final String insertarMedicoStr = "INSERT INTO MEDICOS (codigo_medico, ocupacion_medica, fecha_vinculacion, tipo_vinculacion, numero_registro, codigo_depto_registro, codigo_ciudad_registro, codigo_pais_registro, firma_digital, convencion, tipo_liquidacion) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * Cadena que realiza la consulta del cargo asignado al usuario
	 */
	private static final String consultaCargoMedicoStr=" SELECT " +
								"codigo  AS codigo, " +
								"nombre AS nombre, " +
								"activo AS activo, " +
								"institucion AS institucion, " +
								"profesionalsalud AS cargo " +
								"FROM cargos_usuarios " +
								"WHERE institucion = ? ";
	
	/**
	 * Dados el numero y tipo de identificacion, retorna los datos de un medico. Reutiliza una
	 * conexion abierta.
	 * @param con una conexion abierta con una base de datos Gen�rica
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @return un objeto <code>Answer</code> con una conexion abierta y los datos del medico
	 */
	public static Answer cargarMedico (Connection con, int codigoMedico) throws SQLException {

		PreparedStatementDecorator cargarMedicoStatement =  new PreparedStatementDecorator(con,cargarMedicoStr);
		//logger.info("CONSULTA MEDICO=> "+cargarMedicoStr+" codigoMedico=> "+codigoMedico);
		cargarMedicoStatement.setInt(1, codigoMedico);

		logger.info(cargarMedicoStatement);
		
		ResultSetDecorator rs = new ResultSetDecorator(cargarMedicoStatement.executeQuery());
		return new Answer(rs, con);

	}

	/**
	 * Dados el numero y tipo de identificacion, cambia los datos de un medico. Usa una conexion abierta,
	 * si no esta abierta, la crea.
	 * @param con una conexion abierta con una base de datos Gen�rica
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param especialidades arreglo con los codigos de las especialidades del medico
	 * @param codigoCategoria codigo con la categoria del medico
	 * @param diaVinculacion dia de vinculacion del medico
	 * @param mesVinculacion mes de vinculacion del medico
	 * @param anioVinculacion a�o de vinculacion del medico
	 * @param codigoTipoVinculacion codigo con el tipo de vinculacion del medico
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedido el numero de identificacion del medico
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedido el numero de identificacion del medico
	 * @param codigoTipoPersona codigo del tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del medico
	 * @param mesNacimiento mes de nacimiento del medico
	 * @param anioNacimiento a�o de nacimiento del medico
	 * @param codigoEstadoCivil codigo del estado civil del medico
	 * @param codigoSexo codigo del sexo del medico
	 * @param primerNombrePersona primer nombre del medico
	 * @param segundoNombrePersona segundo nombre del medico
	 * @param primerApellidoPersona primer apellido del medico
	 * @param segundoApellidoPersona segundo apellido del medico
	 * @param direccion direccion de la residencia del medico
	 * @param codigoDepartamento codigo del departamento donde reside el medico
	 * @param codigoCiudad codigo de la ciudad donde reside el medico
	 * @param codigoBarrio codigo del barrio de residencia del medico
	 * @param telefono telefono del medico
	 * @param email correo electronico del medico
	 * @param numeroRegistro numero de registro del medico
	 * @param codigoDeptoRegistro
	 * @param codigoCiudadRegistro
	 * @return el numero de filas modificadas (0 si no se pudo modificar)
	 */
	public static int modificarMedico (Connection con, int codigoMedico, Especialidades  especialidades, String codigoCategoria, 
			String diaVinculacion, String mesVinculacion, String anioVinculacion, String codigoTipoVinculacion, String numeroRegistro,
			String codigoDeptoRegistro, String codigoCiudadRegistro, String codigoPaisRegistro,String firmaDigital, int convencion, String tipoLiquidacion) throws SQLException 
	{
		
		int resp=1, resp1=1, resp2=1, resp0=1, resp3=1;
	   
		// Actualizamos los datos en medico
		PreparedStatementDecorator actualizarMedicoStatement =  new PreparedStatementDecorator(con.prepareStatement(actualizarMedico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		actualizarMedicoStatement.setInt(1, Integer.parseInt(codigoCategoria) );
		actualizarMedicoStatement.setString(2, anioVinculacion + '-' + mesVinculacion + '-' + diaVinculacion);
		actualizarMedicoStatement.setInt(3, Integer.parseInt(codigoTipoVinculacion) );
		actualizarMedicoStatement.setString(4, numeroRegistro);
		if(codigoPaisRegistro.equals(""))
			actualizarMedicoStatement.setNull(5,Types.VARCHAR);
		else
			actualizarMedicoStatement.setString(5,codigoPaisRegistro);
		if(codigoDeptoRegistro.equals(""))
			actualizarMedicoStatement.setNull(6,Types.VARCHAR);
		else
			actualizarMedicoStatement.setString(6,codigoDeptoRegistro);
		if(codigoCiudadRegistro.equals(""))
			actualizarMedicoStatement.setNull(7,Types.VARCHAR);
		else
			actualizarMedicoStatement.setString(7,codigoCiudadRegistro);
		actualizarMedicoStatement.setString(8,firmaDigital);
		actualizarMedicoStatement.setInt(9, convencion);
		
		if(UtilidadTexto.isEmpty(tipoLiquidacion))
		{
			actualizarMedicoStatement.setNull(10, Types.VARCHAR);
		}
		else
		{
			actualizarMedicoStatement.setString(10, tipoLiquidacion);
		}
		
		actualizarMedicoStatement.setInt(11, codigoMedico);
		
		
		resp3=actualizarMedicoStatement.executeUpdate();

	
		/*Se verifica si el m�dico tiene especialidades antes de modificarlas
		 * para borrarlas*/
		PreparedStatementDecorator cuentaEspecialidades =  new PreparedStatementDecorator(con.prepareStatement(cuentaEspecialidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cuentaEspecialidades.setInt(1,codigoMedico);
		ResultSetDecorator rs=new ResultSetDecorator(cuentaEspecialidades.executeQuery());
		
		if(rs.next())
		{
			//se verifica si hay especialidades para borrar
			if(rs.getInt("contador")>0)
			{
				/* Ahora voy a actualizar las especialidades que ten�a el m�dico. Antes de ingresar las nuevas especialidades,
				   hay que borrar todos los que el m�dico tuvo antes */
				PreparedStatementDecorator borrarEspecialidadesStatement=  new PreparedStatementDecorator(con.prepareStatement(borrarEspecialidades,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				borrarEspecialidadesStatement.setInt(1, codigoMedico);
				
				resp2 = borrarEspecialidadesStatement.executeUpdate();
			}
		}
		else
			//si hay error en la consulta
			resp2=0;
		
		

		/* Inicialmente asumimos que las inserciones van a salir bien sin embargo, si en el for nos damos cuenta
		   que est�bamos mal, cambiamos el valor de resp4 por 0 */
		PreparedStatementDecorator agregarEspecialidadesStatement = null;
		int resp4 = 1;
		Especialidad tmp;
		Collection especialidadesLista=especialidades.getListadoEspecialidades();
		
		Iterator it=null;
		
		
		
		if(especialidadesLista.size()==1){
			it=especialidadesLista.iterator();
			resp4=1;
			tmp=(Especialidad)it.next();
			agregarEspecialidadesStatement =  new PreparedStatementDecorator(con.prepareStatement(agregarEspecialidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			agregarEspecialidadesStatement.setInt(1, codigoMedico);
			agregarEspecialidadesStatement.setInt(2, Integer.parseInt(tmp.getCodigoEspecialidad()));
			agregarEspecialidadesStatement.setBoolean(3, tmp.getActivaSistema());
			if (agregarEspecialidadesStatement.executeUpdate() == 0) 
			{
				resp4 = 0;
			}
				
		}else{
			it=especialidadesLista.iterator();
			resp4=1;
			while (it.hasNext() && resp4==1)
			{
				tmp=(Especialidad)it.next();
				agregarEspecialidadesStatement =  new PreparedStatementDecorator(con.prepareStatement(agregarEspecialidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				agregarEspecialidadesStatement.setInt(1, codigoMedico);

				agregarEspecialidadesStatement.setInt(2, Integer.parseInt(tmp.getCodigoEspecialidad()));
				agregarEspecialidadesStatement.setBoolean(3, tmp.getActivaSistema());

				if (agregarEspecialidadesStatement.executeUpdate() == 0) 
				{
					resp4 = 0;
				}
			}

			
			
		}
		
		if (resp0==0 || resp1==0 || resp2==0 || resp3==0 || resp4==0)
		{
			resp=0;
		
		}
		else
		{
			resp=1;
		}
		return resp;

	}
	


	
	/**
	 * Inserta un medico en una base de datos Gen�rica. Usa una conexion abierta, y la cierra
	 * despues de ejecutarse. Si no esta abierta, la crea.
	 * @param con una conexion abierta con una base de datos Gen�rica
	 * @param especialidades arreglo con los codigos de las especialidades del medico
	 * @param codigoOcupacionMedica codigo de la ocupaci�n m�dica del profesional de la salud
	 * @param diaVinculacion dia de vinculacion del medico
	 * @param mesVinculacion mes de vinculacion del medico
	 * @param anioVinculacion a�o de vinculacion del medico
	 * @param codigoTipoVinculacion codigo con el tipo de vinculacion del medico
	 * @param numeroIdentificacion numero de identificacion del medico
	 * @param codigoTipoIdentificacion codigo con el tipo de identificacion del medico
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedido el numero de identificacion del medico
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedido el numero de identificacion del medico
	 * @param codigoTipoPersona codigo del tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del medico
	 * @param mesNacimiento mes de nacimiento del medico
	 * @param anioNacimiento a�o de nacimiento del medico
	 * @param codigoEstadoCivil codigo del estado civil del medico
	 * @param codigoSexo codigo del sexo del medico
	 * @param primerNombrePersona primer nombre del medico
	 * @param segundoNombrePersona segundo nombre del medico
	 * @param primerApellidoPersona primer apellido del medico
	 * @param segundoApellidoPersona segundo apellido del medico
	 * @param direccion direccion de la residencia del medico
	 * @param codigoDepartamento codigo del departamento donde reside el medico
	 * @param codigoCiudad codigo de la ciudad donde reside el medico
	 * @param codigoBarrio codigo del barrio de residencia del medico
	 * @param telefono telefono del medico
	 * @param email correo electronico del medico
	 * @param numeroRegistro numero de registro del medico
	 * @param codigoDeptoRegistro 
	 * @param codigoCiudadRegistro
	 * @param telefonoCelular 
	 * @param telefonoFijo 
	 * @return numero de filas insertadas
	 */
	public static int insertarMedico (
		Connection con, Especialidades especialidades, String codigoCategoria, String diaVinculacion, String mesVinculacion, 
		String anioVinculacion, String codigoTipoVinculacion, String numeroIdentificacion, String codigoTipoIdentificacion,
		String codigoDeptoId, String codigoCiudadId, String codigoPaisId,
		String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, 
		String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, 
		String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, 
		String codigoCiudad, String codigoPais, String codigoBarrio, String telefono, String email, String numeroRegistro,String codigoDeptoRegistro, 
		String codigoCiudadRegistro, String codigoPaisRegistro,String firmaDigital, int convencion, String tipoLiquidacion, String telefonoFijo, String telefonoCelular) 
	{
		try{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp, resp1, resp2, resp0, resp3=1, numResultados=0;
		RespuestaInsercionPersona respuesta=null;

		// Iniciamos la transacci�n
		if (myFactory.beginTransaction(con))
		{
		    resp0=1;
		}
		else
		{
		    resp0=0;
		}
		
		/* Solo se debe insertar en personas si no existe una previamente El caso en que se quiera agregar un medico
			con la misma identificacion, ya esta manejado por la clase util.UtilidadValidacion */
		
		logger.info("deboInsertarPersonaStr**************"+deboInsertarPersonaStr);
		logger.info("codigoTipoIdentificacion**************"+codigoTipoIdentificacion);
		logger.info("numeroIdentificacion**************"+numeroIdentificacion);
		PreparedStatementDecorator deboInsertarStatement =  new PreparedStatementDecorator(con.prepareStatement(deboInsertarPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		deboInsertarStatement.setString(1, codigoTipoIdentificacion);
		deboInsertarStatement.setString(2, numeroIdentificacion);
				ResultSetDecorator rs=new ResultSetDecorator(deboInsertarStatement.executeQuery());
		
		logger.info("numResultados*****111111111111111***************"+numResultados);
		while (rs.next())
		{
			logger.info("numResultados************22222222222222********"+numResultados);
			numResultados=rs.getInt("numResultados");
		}
		// Solo si no hay una persona insertada, hacemos la insercion en persona
		logger.info("numResultados******3333333333333333**************"+numResultados);
		if (numResultados<1)
		{
			logger.info("tonsqQQQQQQQQQQQQQ!!!");
			respuesta = Persona.insertarPersona(con, numeroIdentificacion, codigoTipoIdentificacion,codigoDeptoId, codigoCiudadId, codigoPaisId, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, "", telefono, email,telefonoCelular,ConstantesBD.tipoPersonaMedico,0,telefonoFijo);
			logger.info("hoooooooooooooooooooo!!!");
			//En ocasiones debemos cambiar el valor del numero de identificacion
			//aca preguntamos si debemos hacerlo
			if (respuesta.isNecesitaCambioIdentificacion())
			{
				numeroIdentificacion=respuesta.getNuevaIdentificacion();
			}
			//Dependiendo de la respuesta recibida,  asignamos el valor de resp1
			if (respuesta.isSalioBien())
			{
				resp1=1;
			}
			else
			{
				resp1=0;
			}
		}
		else
		{
			//Si no necesitamos insertar, se consulta la persona ya existente
			//
			int codigo_aux=Persona.obtenerCodigoPersona(con,numeroIdentificacion,codigoTipoIdentificacion);
			
			//llenado de objeto porvisional
			respuesta=new RespuestaInsercionPersona(true,false,numeroIdentificacion,codigo_aux);
			
			
			
			resp1=1;
		}
		
		int codigoPersona=respuesta.getCodigoPersona();
		logger.info("insertarMedicoStr**************"+insertarMedicoStr);
	
		PreparedStatementDecorator insertarMedicoStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarMedicoStatement.setInt(1, codigoPersona);	
		insertarMedicoStatement.setInt(2, Integer.parseInt(codigoCategoria) );
		insertarMedicoStatement.setString(3, anioVinculacion + '-' + mesVinculacion + '-' + diaVinculacion);
		insertarMedicoStatement.setInt(4, Integer.parseInt(codigoTipoVinculacion) );
		insertarMedicoStatement.setString(5, numeroRegistro);
		if(codigoDeptoRegistro.equals(""))
			insertarMedicoStatement.setNull(6,Types.VARCHAR);
		else
			insertarMedicoStatement.setString(6,codigoDeptoRegistro);
		if(codigoCiudadRegistro.equals(""))
			insertarMedicoStatement.setNull(7,Types.VARCHAR);
		else
			insertarMedicoStatement.setString(7,codigoCiudadRegistro);
		if(codigoPaisRegistro.equals(""))
			insertarMedicoStatement.setNull(8,Types.VARCHAR);
		else
			insertarMedicoStatement.setString(8,codigoPaisRegistro);
		insertarMedicoStatement.setString(9, firmaDigital);
		insertarMedicoStatement.setInt(10, convencion);
		
		if(UtilidadTexto.isEmpty(tipoLiquidacion))
		{
			insertarMedicoStatement.setNull(11, Types.VARCHAR);
		}
		else
		{
			insertarMedicoStatement.setString(11, tipoLiquidacion);
		}
		
		resp2=insertarMedicoStatement.executeUpdate();
	
		/* Inicialmente asumimos que las inserciones van a salir bien sin embargo, si en el for nos damos cuenta
		   que est�bamos mal, cambiamos el valor de resp4 por 0 */
		PreparedStatementDecorator agregarEspecialidadesStatement = null;
		int resp4 = 1;
		Especialidad tmp;
		Collection especialidadesLista=especialidades.getListadoEspecialidades();
		Iterator it=especialidadesLista.iterator();
	
		while (it.hasNext())
		{
			tmp=(Especialidad)it.next();
			
			agregarEspecialidadesStatement =  new PreparedStatementDecorator(con.prepareStatement(agregarEspecialidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			agregarEspecialidadesStatement.setInt(1, codigoPersona);
			agregarEspecialidadesStatement.setInt(2, Integer.parseInt(tmp.getCodigoEspecialidad()));
			agregarEspecialidadesStatement.setBoolean(3, tmp.getActivaSistema());

			if (agregarEspecialidadesStatement.executeUpdate() == 0) 
			{
				resp4 = 0;
			}
		}
		
		
		if (resp0==0 || resp1==0 || resp2==0 || resp3==0 || resp4==0)
		{
			resp=0;
			myFactory.abortTransaction(con);
		}
		else
		{	
		    myFactory.endTransaction(con);
			resp3=1;
			resp=codigoPersona;
			//En caso en el que el usuario sea MS o AS no debe retornar
			//1 sino el valor del nuevo tipo de identificacion. Si este
			//es el caso que ocurre, podemos hacer un parseInt, ya que
			//el numero sera un entero generado por una secuencia
			if (codigoTipoIdentificacion.equals("MS")||codigoTipoIdentificacion.equals("AS"))
			{
				resp = Integer.parseInt(numeroIdentificacion);
			}
		}
		return resp;
		}
		catch(Exception e){
				logger.error("Error insertanod m�dico en SqlBaseMedicoDao: ",e);
				return -1;
			}
	}

	/**
	 * Dados el n�mero y tipo de identificaci�n de un m�dico, lo desactiva
	 * @param con una conexion abierta con la BD Gen�rica
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param codigoInstitucion c�digo de la instituci�n de la que se va a
	 * desactivar el m�dico
	 * @return int 1 si pudo desactivar el m�dico, 0 si no pudo desactivarlo
	 */
	public static int desactivarMedico (Connection con, int codigoMedico, String codigoInstitucion)
	{
		int resp0=0,resp1=0, resp3=0;
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if (myFactory.beginTransaction(con))
			{
			    resp0=1;
			}
			else
			{
			    resp0=0;
			}
		} 
		catch (SQLException e1)
		{
			logger.error("No se logr� abrir la transacci�n");
			return 0;
		}
		    
		try
		{
			PreparedStatementDecorator activarMedico= new PreparedStatementDecorator(con.prepareStatement(inactivarMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			activarMedico.setInt(1, codigoMedico);
			activarMedico.setString(2, codigoInstitucion);
			resp1=activarMedico.executeUpdate();
			

			myFactory.endTransaction(con);
			resp3=1;

			if (resp0==0||resp1==0||resp3==0)
			{
				myFactory.abortTransaction(con);
				return 0;
			}
			return resp1; 
		}
		catch (SQLException e) 
			{
			logger.error("Error al inactivar el profesional de la salud",e);
			return -1;
			}
		}
	/**
	 * Dados el n�mero y tipo de identificaci�n de un m�dico, lo activa
	 * @param con una conexion abierta con la BD Gen�rica
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param codigoInstitucion c�digo de la instituci�n de la que se va a
	 * desactivar el m�dico
	 * @return int 1 si pudo activar el m�dico, 0 si no pudo activarlo
	 */
	public static int activarMedico (Connection con, int codigoMedico, String codigoInstitucion) throws SQLException
	{
		int resp0=0, resp2=0, resp3=0;

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (myFactory.beginTransaction(con))
		{
		    resp0=1;
		}
		else
		{
		    resp0=0;
		}
		
		
		PreparedStatementDecorator eliminarMedicoDeInactivos= new PreparedStatementDecorator(con.prepareStatement(activarMedicoInactivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		eliminarMedicoDeInactivos.setInt(1, codigoMedico);
		eliminarMedicoDeInactivos.setString(2, codigoInstitucion);
		resp2=eliminarMedicoDeInactivos.executeUpdate();

		myFactory.endTransaction(con);
		resp3=1;
		
		if (resp0==0||resp2==0||resp3==0)
		{
			myFactory.abortTransaction(con);
			return 0;
		}
		return resp2; 
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public static String obtenerFirmaDigitalMedico(Connection con, int codigoMedico)
	{
		String consulta="SELECT coalesce(firma_digital, '') FROM medicos where codigo_medico=? ";
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMedico);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}

	
	public static String obtenerCargoMedico(Connection con, String codInstitucion)
	{
		String cadenaConsulta=consultaCargoMedicoStr;
		cadenaConsulta=cadenaConsulta + "  AND activo = '"+ConstantesBD.acronimoSi+"' AND profesionalsalud = '"+ConstantesBD.acronimoSi+"'";
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cadenaConsulta=cadenaConsulta.replace("institucion = ?", "institucion = "+codInstitucion);
			ps.setInt(1, Utilidades.convertirAEntero(codInstitucion));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getString("codigo");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("ERROR en consulta del Cargo del Usuario >> cadena >> "+cadenaConsulta );
		}
		
		
		return "";
	}
	
	/**
	 * 
	 * @param codigoMedico
	 * @return
	 */
	public static String obtenerTipoLiquidacionPool(int codigoMedico) 
	{
		String resultado="";
		String cadenaConsulta="select coalesce(m.tipo_liquidacion, '') as tipoliquidacion from administracion.medicos m where m.codigo_medico="+codigoMedico;
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				resultado = rs.getString("tipoliquidacion");
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("ERROR en consulta del Cargo del Usuario >> cadena >> "+cadenaConsulta );
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		
		
		return resultado;
	}
	
}
