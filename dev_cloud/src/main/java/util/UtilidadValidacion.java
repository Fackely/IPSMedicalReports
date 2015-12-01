/*
 * @(#)UtilidadValidacion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UtilidadValidacionDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.InformacionParto;
import com.princetonsa.mundo.historiaClinica.InformacionRecienNacidos;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Esta clase permite hacer las validaciones necesarias
 * para evitar hacer ingresos invalidos (tipo ingreso
 * usuario con login existente y similares), situaciones
 * que si no son capturadas generarian una pagina de
 * de error poco amigable.
 *
 * @version 1.0, Sep 22, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
*/

public class UtilidadValidacion
{
	/**
	 * Para hacer logs de esta clase.
	 */
	private static Logger logger = Logger.getLogger(UtilidadValidacion.class);
	/**
	 * Este metodo valida que el usuario que va a ser
	 * ingresado en el sistema no exista previamente
	 * Hay dos posibles casos para esto, el primero que
	 * el login exista previamente o que ya exista un
	 * usuario con esa identificacion. El hecho que una
	 * persona exista en el sistema NO impide agregarlo
	 * como usuario, ya que puede estar como paciente o
	 * medico
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param login el login que quiere tener el usuario en el sistema
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el futuro usuario
	 * @param numeroId el numero de identificacion que tiene el futuro usuario
	 * @param codDepartamento código del departamento del usuario
	 * @param codCiudad código de la ciudad del usuario
	 * @param codBarrio código del barrio del usuario
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarUsuario (Connection con, String login,  String tipoBD, String tipoId, String numeroId, String codBarrio) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarUsuario (con, login,  tipoBD, tipoId, numeroId,  codBarrio);
	}
	
	public static RespuestaValidacion validacionIngresarUsuario (Connection con, String login) 
	{
		try{
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarUsuario (con, login);
		}
		catch(SQLException e){
			logger.error("Error validando el login de usuario en UtilidadValidacion: "+e);
			return null;
		}
	}
	/**
	 * Este metodo valida que el medico que va a ser
	 * ingresado en el sistema no exista previamente
	 * A diferencia de la inserción en usuarios, en
	 * medicos solo se puede presentar un caso, y es
	 * que ya exista un medico con esa identificacion
	 * De nuevo, el hecho que una persona exista en
	 * el sistema NO impide agregarlo como Medico, ya
	 * que puede estar como paciente o usuario
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el futuro medico
	 * @param numeroId el numero de identificacion que tiene el futuro medico
	 * @param codDepartamento código del departamento del médico
	 * @param codCiudad código de la ciudad del médico
	 * @param codBarrio código del barrio del médico
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarMedico (Connection con, String tipoId, String numeroId, String codBarrio) throws SQLException
	{
		return validacionIngresarPersona( con,  tipoId,  numeroId,codBarrio, "Medico");
	}

	/**
	 * Este metodo valida que el paciente que va a ser
	 * ingresado en el sistema no exista previamente
	 * A diferencia de la inserción en usuarios, en pacientes solo se puede
	 * presentar un caso, y es que ya exista un paciente con esa identificacion
	 * De nuevo, el hecho que una persona exista en el sistema NO impide
	 * agregarlo como paciente, ya que puede estar como usuario o medico
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el futuro paciente
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codDepartamento código del departamento del paciente
	 * @param codCiudad código de la ciudad del paciente
	 * @param codBarrio código del barrio del paciente
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarPaciente (Connection con, String tipoId, String numeroId, String codBarrio) throws SQLException
	{
		return validacionIngresarPersona( con,  tipoId,  numeroId,codBarrio, "Paciente");
	}

	/**
	 * Este metodo valida que una persona que va a ser
	 * ingresado en el sistema no exista previamente
	 * A diferencia de la inserción en usuarios, en pacientes solo se puede
	 * presentar un caso, y es que ya exista un paciente con esa identificacion.
	 * La idea de este metodo es agrupar la validación de medicos y pacientes
	 * que es practicamente la misma
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoId el tipo de identificacion que tiene el futuro paciente
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codDepartamento código del departamento de la persona
	 * @param codCiudad código de la ciudad de la persona
	 * @param codBarrio código del barrio de la persona
	 * @param rolPersona tipo de persona: usuario, médico, paciente.
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarPersona (Connection con, String tipoId, String numeroId, String codBarrio, String rolPersona) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarPersona (con, tipoId, numeroId, codBarrio, rolPersona);
	}
	
	/**
	* Metodo para realizar la validacion de si exite en la base
	* de datos un paciente con el mismo nombre y apellidos esta
	* validacion se esta haciendo por institucion, en caso de 
	* que se necesite que no se por institucion debe cambiarse
	* el inner join de la tabla pacientes_instituciones por la 
 	* tabla pacientes;
 	* @param con
 	* @param primerNombre
 	* @param segundoNombre
 	* @param primerApellido
 	* @param segundoApellido
 	* @return
 	*/	
	public static String validacionExistePacienteMismoNombre(Connection con, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionExistePacienteMismoNombre (con, primerNombre,segundoNombre,primerApellido,segundoApellido);
	}
	

	/**
	 * Este metodo valida que los cambios realizados al usuario durante una actualizacion o
	 * modificacion sean validos. Especificamente, el usuario no puede cambiar su identificacion
	 * (tipo y numero) a otra identificacion previamente existente en el sistema.
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el  usuario
	 * @param numeroId el numero de identificacion que tiene el usuario
	 * @param tipoIdViejo el tipo de identificacion que tenia el  usuario
	 * @param numeroIdViejo el numero de identificacion que tenia el  usuario
	 * @param codDepartamento código del departamento del usuario
	 * @param codCiudad código de la ciudad del usuario
	 * @param codBarrio código del barrio del usuario
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 */
	public static RespuestaValidacion validacionModificarUsuario (Connection con, String tipoBD, String tipoId, String numeroId, String tipoIdViejo, String numeroIdViejo, String codBarrio) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionModificarUsuario (con, tipoBD, tipoId, numeroId, tipoIdViejo, numeroIdViejo,  codBarrio);
	}

	/**
	 * Este metodo valida que los cambios realizados al medico durante una actualizacion o
	 * modificacion sean validos.
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codDepartamento El codigo del departamento de vivienda del médico
	 * @param codCiudad El codigo de la ciudad de vivienda del médico
	 * @param codBarrio El codigo del barrio de vivienda del médico
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valida la modificación que se piensa
	 */
	public static RespuestaValidacion validacionModificarMedico (Connection con,  String tipoBD, String codBarrio) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionModificarPersonaNoUsuario (con,  tipoBD, codBarrio);
	}

	/**
	 * Este metodo valida que los cambios realizados al paciente durante una actualizacion o
	 * modificacion sean validos.
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codDepartamento El codigo del departamento de vivienda del paciente
	 * @param codCiudad El codigo de la ciudad de vivienda del paciente
	 * @param codBarrio El codigo del barrio de vivienda del paciente
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si es valida la modificación que se piensa
	 */
	public static RespuestaValidacion validacionModificarPaciente (Connection con,  String tipoBD, String codBarrio) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionModificarPersonaNoUsuario (con,  tipoBD, codBarrio);
	}

	/**
	 * Este metodo valida que se pueda insertar una historia
	 * Clínica, especificamente, que no exista una historia clínica para este
	 * mismo paciente
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el paciente al que se le va a abrir historia clínica
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarHistoriaClinica (Connection con,  String tipoBD, String tipoId, String numeroId) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarHistoriaClinica (con,  tipoBD, tipoId, numeroId);
	}

	/**
	 * Este método valida que una fecha dada sea válida, tomando incluso los
	 * casos en años biciestos.
	 * @param anio Entero con el año de la fecha a validar
	 * @param mes Entero con el mes de la fecha a validar
	 * @param dia Entero con el día de la fecha a validar
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si la fecha es válida y en caso que no lo
	 * sea, se explica porque.
	 */
	public static RespuestaValidacion validacionFecha(int anio, int mes, int dia)
	{
		return UtilidadFecha.validacionFecha(anio, mes, dia);
	}

	/**
	 * Este método valida que un String contenga la hora en formato
	 * hh:mm (militar). Es claro que también revisa que los valores
	 * de hora y minuto.
	 * @param hora Un String con la hora
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si la hora es válida y en caso que no lo
	 * sea, se explica porque.
	 */
	public static RespuestaValidacion validacionHora(String hora)
	{
		return UtilidadFecha.validacionHora(hora);
	}

	/**
	 * Este método valida que se pueda insertar un ingreso
	 * para un paciente determinado, especificamente, que no
	 * exista otro ingreso para este mismo paciente abierto
	 * (codigo 0)
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el paciente al que se le va a crear un ingreso
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codigoInstitucion el codigo de la institucion donde se piensa hacer el ingreso
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarIngreso (Connection con,  String tipoId, String numeroId, String codigoInstitucion) 
	{
		RespuestaValidacion respuesta = new RespuestaValidacion("Error realizando validacion de ingreso cuenta",false);
		try
		{
			respuesta = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarIngreso (con,  tipoId, numeroId, codigoInstitucion);
		}
		catch(SQLException e)
		{
			logger.error("Error realizando la validacion del ingreso: "+e);
		}
		return respuesta;
		
	}

	/**
	 * Este  método permite validar si es necesario que el paciente al cual se
	 * le va a crear un ingreso (En la pantalla de ingreso de documento) para
	 * evitar que el usuario llene toda la información para al final darse
	 * cuenta que no puede crear el ingreso o la cuenta
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el futuro paciente
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codigoInstitucion el código de la institución donde se espera este
	 * el paciente
	 * @return un objeto <code>RespuestaValidacion</code> que dice si hay un
	 * ingreso para este paciente en particular
	 */
	public static RespuestaValidacion validacionPreviaIngresoPaciente (Connection con,  String tipoId, String numeroId, String codigoInstitucion) 
	{
		RespuestaValidacion respuesta=new RespuestaValidacion ("El usuario no tiene ni cuenta ni ingreso abierto, puede seguir", true);
		RespuestaValidacion temporal=null;
		
		temporal= validacionIngresarIngreso (con,  tipoId, numeroId, codigoInstitucion);
		
		if (!temporal.puedoSeguir)
			return temporal;

		return respuesta;
	}

	/**
	 * Este método valida que se pueda insertar una cuenta
	 * para un paciente determinado, especificamente, que no
	 * exista otra cuenta para este mismo paciente en estado
	 * abierto (codigo 0)
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoId el tipo de identificacion que tiene el paciente al que se le va a crear un ingreso
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codigoInstitucion el código de la institución donde se crea esta
	 * cuenta
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionIngresarCuenta (Connection con,  String tipoId, String numeroId, String codigoInstitucion) 
	{
		RespuestaValidacion respuesta = new RespuestaValidacion("Error realizando la validacion del ingreso de la cuenta",false);
		try
		{
			respuesta = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarCuenta (con,  tipoId, numeroId, codigoInstitucion);
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionIngresarCuenta: "+e);
		}
		return respuesta;
	}




	/**
	 * Este método valida que se pueda insertar una admisión hospitalaria para
	 * un paciente determinado, especificamente, A- Que la cuenta a la que
	 * pertenece esta admisión hospitalaria esté abierta y  B - Que no haya
	 * ninguna admisión hospitalaria abierta para este paciente en esta
	 * institución
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoPaciente Código del paciente sobre el que se
	 * está validando
	 * @param idCuenta Código de la cuente sobre la que se está
	 * validando
	 * @param codigoInstitucion el código de la institución donde se crea esta
	 * cuenta
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 * @throws SQLException
	 */

	public static RespuestaValidacion validacionIngresarAdmisionHospitalaria (Connection con,  int codigoPaciente, int idCuenta, String codigoInstitucion) 
	{
		RespuestaValidacion respuesta = new RespuestaValidacion("Error en base de datos al tratar de validar el ingreso de la admisión",false);
	
		try
		{
			respuesta = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionIngresarAdmisionHospitalaria (con,  codigoPaciente, idCuenta, codigoInstitucion);
		}
		catch (Exception e) 
		{
			logger.error("Error en validacionIngresarAdmisionHospitalaria: "+e);
		}
		return respuesta;
	}

	/**
	 * Este metodo valida la posibilidad de cambiar el documento de un paciente
	 * Para ver si esto es posible o no, hay que revisar que el documento no
	 * este registrado por otra persona, si la persona a la que se piensa
	 * cambiar el documento existe, y que existe solo como paciente
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoIdNuevo nuevo tipo de identificación que se desea establecer
	 * @param numeroIdNuevo nuevo número de la identificación que se desea establecer
	 * @param tipoIdViejo viejo tipo de la identificación, que ya está establecido
	 * @param numeroIdViejo viejo número de la identificación, que ya está establecido
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si es valido el cambio que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public static RespuestaValidacion validacionCambiarDocumento (Connection con, String tipoBD, String tipoIdNuevo, String numeroIdNuevo, String tipoIdViejo, String numeroIdViejo) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionCambiarDocumento (con, tipoBD, tipoIdNuevo, numeroIdNuevo, tipoIdViejo, numeroIdViejo);
	}

	/**
	 * Este método es auxiliar a la mayoría de rutinas de validación. Su papel es revisar si existe o no un paciente.
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el paciente (documentación del paciente a verificar)
	 * @param numeroId el numero de identificacion que tiene el paciente (documentación del paciente a verificar)
	 * @return un <code>boolean</code> con verdadero si el paciente existe o falso si no
	 */
	public static boolean existePaciente (Connection con, String tipoId, String numeroId) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existePaciente(con, tipoId, numeroId);
	}

	public static boolean existePaciente (Connection con, int codigoPaciente) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existePaciente(con, codigoPaciente);
	}

	/**
	 * Este método me permite saber si una institución puede ver un paciente.
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codigoPaciente código del paciente
	 * @param codigoInstitucion el codigo de la institución que intenta revisar
	 * si hay o no paciente
	 * @return un <code>boolean</code> con verdadero si el paciente existe o falso si no
	 */
	public static boolean puedoImprimirPaciente(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoImprimirPaciente(con, codigoPaciente, codigoInstitucion);
	}

	/**
	 * Este método me permite saber si una institución puede ver un paciente.
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el paciente (documentación del paciente a verificar)
	 * @param numeroId el numero de identificacion que tiene el paciente (documentación del paciente a verificar)
	 * @param codigoInstitucion el codigo de la institución que intenta revisar
	 * si hay o no paciente
	 * @return un <code>boolean</code> con verdadero si el paciente existe o falso si no
	 */
	public static boolean puedoImprimirPaciente(Connection con,String s1, String s2, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoImprimirPaciente(con, buscarCodigoPaciente(con, s1,s2), codigoInstitucion);
	}

	/**
	 * Este método es auxiliar a la mayoría de rutinas de validación. Su papel
	 * es revisar si existe o no un médico.
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el médico
	 * (documentación del médico a verificar)
	 * @param numeroId el numero de identificacion que tiene el médico
	 * (documentación del médico a verificar)
	 * @return un <code>boolean</code> con verdadero si el médico existe o falso
	 * si no
	 */
	public static boolean existeMedico (Connection con, String tipoBD, String tipoId, String numeroId) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeMedico (con, tipoBD, tipoId, numeroId);
	}

	/**
	 * Este método es auxiliar a la mayoría de rutinas de validación. Su papel
	 * es revisar si existe o no un usuario.
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el usuario
	 * (documentación del usuario a verificar)
	 * @param numeroId el numero de identificacion que tiene el usuario
	 * (documentación del usuario a verificar)
	 * @return un <code>boolean</code> con verdadero si el usuario existe o
	 * falso si no
	 */
	public static boolean existeUsuario (Connection con, String tipoBD, String tipoId, String numeroId) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeUsuario (con, tipoBD, tipoId, numeroId);
	}

	/**
	 * Método que revisa si un codigo pertenece a una Admision Hospitalaria
	 * abierta hecha por la institución específicada
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codigoAdmision Código de la Admisión Hospitalaria a buscar
	 * @param codigoInstitucion Institución desde la que hace la búsqueda
	 * @return boolean en verdadero si la admision hospitalaria dada existe y
	 * pertenece a esta institución
	 * @throws SQLException
	 */
	public static boolean existeAdmisionHospitalariaAbiertaCodigo (Connection con, String tipoBD, String codigoAdmision, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAdmisionHospitalariaAbiertaCodigo (con, tipoBD, codigoAdmision, codigoInstitucion);
	}

	/**
	 * Método que me dice si una admisión hospitalaria existe y me devuelve en
	 * el texto de la admision hospitalaria el codigo de la misma (Esto último
	 * para hacer las cosas más uniformes con el resto de admisiones
	 * hospitalarias)
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codigoAdmision Código de la Admisión Hospitalaria a buscar
	 * @param codigoInstitucion Institución desde la que hace la búsqueda
	 * @return RespuestaValidacion
	 * @throws SQLException
	 */
	public static RespuestaValidacion existeAdmisionHospitalariaCodigo (Connection con, String tipoBD, String codigoAdmision, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAdmisionHospitalariaCodigo (con, tipoBD, codigoAdmision, codigoInstitucion);
	}

	/**
	 * Método que revisa si existe una admisión hospitalaria abierta para un
	 * paciente dado y si existe devuelve (a traves del parametro textoRespuesta
	 * de RespuestaValidacion) el codigo de la admisión
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId Tipo de Identificación del paciente al que se quiere buscar
	 * su Admision Hospitalaria
	 * @param numeroId Número de Identificación del paciente al que se quiere
	 * buscar su Admision Hospitalaria
	 * @param codigoInstitucion Institución desde la que hace la búsqueda
	 * @return RespuestaValidacion con true o false dependiendo si hay Admisión
	 * Hospitalaria abierta y si la hay, con el código
	 * @throws SQLException
	 */
	public static RespuestaValidacion existeAdmisionHospitalariaAbiertaPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAdmisionHospitalariaAbiertaPaciente (con, tipoBD, tipoId, numeroId, codigoInstitucion);
	}

	/**
	 * Método que revisa si existe una admisión hospitalaria para un paciente
	 * dado y si existe devuelve (a traves del parametro textoRespuesta de
	 * RespuestaValidacion) el codigo de la admisión más reciente
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId Tipo de Identificación del paciente al que se quiere buscar
	 * su Admision Hospitalaria
	 * @param numeroId Número de Identificación del paciente al que se quiere
	 * buscar su Admision Hospitalaria
	 * @param codigoInstitucion Institución desde la que hace la búsqueda
	 * @return RespuestaValidacion con true o false dependiendo si hay Admisión
	 * Hospitalaria y si la hay, con el código de la más reciente
	 * @throws SQLException
	 */
	public static RespuestaValidacion existeAdmisionHospitalariaPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAdmisionHospitalariaPaciente (con, tipoBD, tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Con el nuevo enfoque del sistema, ahora se desea que al crear un medico
	 * este se registre automáticamente como usuario. Esta acción
	 * puede ir mal si ya existe un usuario con ese codigo, si este médico tiene
	 * otro login, si no existe un médico con esta identificación o si simplemente
	 * los datos son vacios.  Si en el futuro se agrega nueva funcionalidad
	 * no olvide que si la respuesta sale mal, debe incluir un numero y un guion
	 * antes del texto de la misma
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del médico
	 * @param numeroId El número de identificacion del médico
	 * @param login El login que desea manejar el médico
	 * @param validarUsuario, es un indicador entero que regular ciertas validaciones que no deben hacerse
	 */
	public static RespuestaValidacion validacionInsertarUsuarioMedico (Connection con, String tipoBD, String tipoId, String numeroId, String login,int validarUsuario) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionInsertarUsuarioMedico (con, tipoBD, tipoId, numeroId, login,validarUsuario) ;
	}

	/**
	 * Con el nuevo enfoque del sistema, ahora se desea que al crear un paciente
	 * este tenga la posibilidad que este se registre como usuario. Esta acción
	 * puede ir mal si ya existe un usuario con ese codigo, si este médico tiene
	 * otro login, si no existe un paciente con esta identificación o si simplemente
	 * los datos son vacios.  Si en el futuro se agrega nueva funcionalidad
	 * no olvide que si la respuesta sale mal, debe incluir un numero y un guion
	 * antes del texto de la misma
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del paciente
	 * @param numeroId El número de identificacion del paciente
	 * @param login El login que desea manejar el paciente
	 */
	public static RespuestaValidacion validacionInsertarUsuarioPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String login) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionInsertarUsuarioPaciente (con, tipoBD, tipoId, numeroId, login);
	}

	/**
	 * Este método permite validar la necesidad de insertar un paciente en una
	 * institución. Como el sistema permite manejar pacientes por institución,
	 * en algunas ocasiones un paciente ingresaría a una institución que no lo
	 * tiene como paciente, sin embargo puede ocurrir que el paciente ya se
	 * encuentre en el sistema, en cuyo caso no se puede insertar sino
	 * únicamente hay que dar permisos a la institución que lo intenta ingresar.
	 * Este método se encarga de dar permisos de 1er Nivel (Acceso a datos
	 * básicos del paciente).
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoPaciente El código con el que se identifica el paciente
	 * en el sistema
	 * @param codigoInstitucion El codigo de la institucion que desea manejar el paciente
	 *
	 */
	public static RespuestaValidacion validacionPermisosInstitucionPaciente (Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionPermisosInstitucionPaciente (con, codigoPaciente, codigoInstitucion);
	}

	/**
	 * Este método permite validar la necesidad de insertar un paciente en una
	 * institución. Como el sistema permite manejar pacientes por institución,
	 * en algunas ocasiones un paciente ingresaría a una institución que no lo
	 * tiene como paciente, sin embargo puede ocurrir que el paciente ya se
	 * encuentre en el sistema, en cuyo caso no se puede insertar sino
	 * únicamente hay que dar permisos a la institución que lo intenta ingresar.
	 * Este método se encarga de dar permisos de 1er Nivel (Acceso a datos
	 * básicos del paciente). Este método realiza un paso extra con respecto
	 * a su homonimo, en lo posible utilize el otro método
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoTipoIdentificacion Código del tipo de identificación
	 * del paciente buscado
	 * @param numeroIdentificacion Número de identificación del
	 * paciente buscado
	 * @param codigoInstitucion El codigo de la institucion que desea manejar el paciente
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion validacionPermisosInstitucionPaciente (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion, String codigoInstitucion) throws SQLException
	{
		return validacionPermisosInstitucionPaciente (con, buscarCodigoPaciente (con, codigoTipoIdentificacion, numeroIdentificacion ), codigoInstitucion);
	}

	/**
	 * Este método se encarga de dar permisos de 2do Nivel (Acceso a datos
	 * clínicos del paciente).
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoPaciente El código con el que se identifica el paciente
	 * en el sistema
	 * @param codigoInstitucionBrinda El codigo de la institucion que brinda
	 * el permiso
	 * @param codigoInstitucionRecibe El codigo de la institucion que recibe
	 * el permiso
	 *
	 */
	public static RespuestaValidacion validacionPermisosInstitucionPaciente2 (Connection con, int codigoPaciente, String codigoInstitucionBrinda, String codigoInstitucionRecibe) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionPermisosInstitucionPaciente2 (con, codigoPaciente, codigoInstitucionBrinda, codigoInstitucionRecibe);
	}

	/**
	 * Este método se encarga de dar permisos de 2do Nivel (Acceso a datos
	 * clínicos del paciente).Este método realiza un paso extra con respecto
	 * a su homonimo, en lo posible utilize el otro método
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoTipoIdentificacion Código del tipo de identificación
	 * del paciente buscado
	 * @param numeroIdentificacion Número de identificación del
	 * paciente buscado
	 * @param codigoInstitucionBrinda El codigo de la institucion que brinda
	 * el permiso
	 * @param codigoInstitucionRecibe El codigo de la institucion que recibe
	 * el permiso
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion validacionPermisosInstitucionPaciente2 (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion, String codigoInstitucionBrinda, String codigoInstitucionRecibe) throws SQLException
	{
		return validacionPermisosInstitucionPaciente2 (con, buscarCodigoPaciente (con, codigoTipoIdentificacion, numeroIdentificacion ), codigoInstitucionBrinda, codigoInstitucionRecibe);
	}

	/**
	 * Este método permite validar la necesidad de insertar un medico en una institución.
	 * Como el sistema permite manejar medicos por institución, en algunas ocasiones un
	 * médico ingresará a una institución que no lo tiene , sin embargo puede ocurrir que
	 * este médico ya se encuentre en el sistema, en cuyo caso no se
	 * puede insertar sino únicamente hay que dar permisos a la institución que lo intenta
	 * ingresar. Este método se encarga de hacer todo esto.
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del médico
	 * @param numeroId El número de identificacion del médico
	 * @param codigoInstitucion El codigo de la institucion que desea manejar el médico
	 */
	public static RespuestaValidacion validacionPermisosInstitucionMedico (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionPermisosInstitucionMedico (con, tipoBD, tipoId, numeroId, codigoInstitucion);
	}

	/**
	 * Este método permite validar la necesidad de insertar un paciente en una
	 * institución. Como el sistema permite manejar pacientes por
	 * institución, en algunas ocasiones un paciente ingresará a una
	 * institución que no lo tiene como paciente, sin embargo puede ocurrir
	 * que el paciente ya se encuentre en el sistema, en cuyo caso no se puede
	 * insertar sino únicamente hay que dar permisos a la institución que lo
	 * intenta ingresar. Este método se encarga de hacer todo esto.
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param nuevoConsecutivo El número con el que se desea que a partir de ahora
	 * se manejen las historias clïìnicas
	 */
	public static RespuestaValidacion validacionCambioConsecutivoHistoriasClinicas (Connection con, String tipoBD, String nuevoConsecutivoStr ) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionCambioConsecutivoHistoriasClinicas (con, tipoBD, nuevoConsecutivoStr );
	}

	 /**
	 * Este método permite averiguar si un médico en particular puede ser
	 * activado
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del médico
	 * @param numeroId El número de identificacion del médico
	 * @param codigoInstitucion El codigo de la institucion que desea manejar el médico
	 * */
	public static RespuestaValidacion validacionActivarMedico (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionActivarMedico (con, tipoBD, tipoId, numeroId, codigoInstitucion);
	}

	/**
	 * Método que revisa si se puede cambiar la contraseña de un usuario en
	 * particular (Debe ser de la misma institución y NO puede ser el
	 * superadministrador)
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param login login del usuario que se desea cambiar
	 * @param codigoInstitucion Codigo de la institución del usuario que quiere
	 * hacer este cambio
	 * @return RespuestaValidacion con true si se puede cambiar el password y en
	 * caso de false en textoRespuesta la explicación del porque
	 * @throws SQLException
	 */
	public static RespuestaValidacion validacionCambiarPasswordAdministrador (Connection con, String login, String codigoInstitucion,String password) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionCambiarPasswordAdministrador (con, login, codigoInstitucion,password);
	}

	/**
	 * Retorna los permisos del usuario de escritura, modificación y consulta de
	 * la valoración que corresponde a la cuenta dada.
	 * @param con
	 * @param centroCostoUsuario, centro de costo del usuario que desea
	 * crear/modificar/consultar la valoración de esta cuenta
	 * @param idCuenta, cuenta de la valoración a revisar
	 * @param centroCosto, centro de costo del médico
	 * @return InfoDatos[], arreglo ed tipo "util.InfoDatos" de tamaño 3, en
	 * donde el primer elemento del arreglo corresponde a los permisos que hay
	 * de ingreso de la valoración, en el segundo elemento corresponde al
	 * permiso de modificación y el tercero a los permisos de consulta. Si se
	 * tienen permisos en alguna de las tres se retorna el código de la
	 * solicitud, si no se tiene permiso retorna cómo código "-1" y la
	 * descripción de la restricción
	 * @throws SQLException
	 */
	public static InfoDatos[] getPermisosValoracion (Connection con,PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		int idCuenta=paciente.getCodigoCuenta();
		InfoDatos arreglo[]=new InfoDatos[3];

		DaoFactory myFactory  = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		UtilidadValidacionDao utilidadValidacionDao = myFactory.getUtilidadValidacionDao();

		
		if(UtilidadValidacion.esMedicoTratante(con, usuario, paciente).equals(""))
		{
			if(!paciente.isHospitalDia()&&!UtilidadValidacion.esMedico(usuario).equals(""))
			{
				if(UtilidadValidacion.esMedico(usuario).equals("errors.noOcupacionMedica"))
					arreglo[0] = new InfoDatos("-1", "errors.noOcupacionMedica", "Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales");
				else
					arreglo[0] = new InfoDatos("-1", "", "No puede llenar la valoración porque el usuario no es médico (a pesar de pertenecer al Centro de Costo tratante)");
				arreglo[1]=arreglo[0];
				arreglo[2]=arreglo[0];
				return arreglo;
			}
		}
		else
		{
			if(!paciente.isHospitalDia())
				arreglo[0] = new InfoDatos("-1", "errors.noProfesionalSaludMedico", "No es médico Tratante");
			else
				arreglo[0] = new InfoDatos("-1", "errors.noProfesionalSalud", "No es profesional de la salud");
			arreglo[1]=arreglo[0];
			arreglo[2]=arreglo[0];
			return arreglo;
		}
		HashMap<String,Object> resultado=utilidadValidacionDao.existeSolicitudValoracionCuenta(con, idCuenta);
		if (Utilidades.convertirAEntero(resultado.get("numRegistros").toString())>0)
		{
			int estadoHistoriaClinica = Utilidades.convertirAEntero(resultado.get("estadoHistoriaClinica")+"");
			int codigoSolicitud = Utilidades.convertirAEntero(resultado.get("codigoAxiomaSolicitud")+"");
			
			
			// Ingresar
			if( estadoHistoriaClinica == ConstantesBD.codigoEstadoHCSolicitada )
			{
				arreglo[0] = new InfoDatos(codigoSolicitud, "", "");
				arreglo[1] = new InfoDatos("-1", "", "No se puede modificar porque no ha sido previamente ingresada");
				arreglo[2] = new InfoDatos("-1", "", "No se puede consultar porque no ha sido previamente ingresada");
			}
			else
			// Modificar
			if( estadoHistoriaClinica == ConstantesBD.codigoEstadoHCInterpretada  )
			{
				arreglo[0] = new InfoDatos("-1", "", "No se puede ingresar porque ya ha sido previamente ingresada");
				arreglo[1] = new InfoDatos(codigoSolicitud, "", "");
				arreglo[2] = new InfoDatos(codigoSolicitud, "", "");
			}
			
		}
		else	//  No existe una solicitud para la cuenta y el centro de costo dados
		{
			arreglo[0] = new InfoDatos("-1", "", "No existe ninguna solicitud para la cuenta "+idCuenta+" y el centro de costo "+paciente.getCodigoArea());
			arreglo[1]=arreglo[0];
			arreglo[2]=arreglo[0];
		}
		return arreglo;
		
	}

	/**
	 * Método que verifica si existe solicitud valoracion cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String[] existeSolicitudValoracionCuenta(Connection con, PersonaBasica paciente)
	{
		HashMap<String, Object> validacion;
		String[] informacion = new String[2];
		try 
		{
			validacion = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeSolicitudValoracionCuenta(con, paciente.getCodigoCuenta());
		} 
		catch (SQLException e) 
		{
			validacion = new HashMap<String, Object>();
			logger.error("Error en existeSolicitudValoracionCuenta: "+e);
			validacion.put("numRegisros","0");
		}
		if (Utilidades.convertirAEntero(validacion.get("numRegistros").toString())>0)
		{
			int estadoHistoriaClinica = Utilidades.convertirAEntero(validacion.get("estadoHistoriaClinica")+"");
			int codigoSolicitud = Utilidades.convertirAEntero(validacion.get("codigoAxiomaSolicitud")+"");
			
			informacion[0] = codigoSolicitud +"";
			informacion[1] = estadoHistoriaClinica + "";
			
		}
		else	//  No existe una solicitud para la cuenta y el centro de costo dados
		{
			informacion[0] = ConstantesBD.codigoNuncaValido +"";
			informacion[1] = ConstantesBD.codigoNuncaValido + "";
		}
		return informacion;
	}
	
	/**
	 * Para poder llenar una evolución se deben cumplir las siguientes reglas:
	 * - Debe existir una valoración anterior llena de este centro de costo
	 * - Debe ser un prof de la salud diferente enfermera (Esto se debe validar
	 * en el Action con el número de autorización y la categoría. Como NO
	 * necesita acceso a BD, NO se hace en este método)
	 * - El centro de costo del médico debe haber hecho al menos una
	 * valoración (debe estar llena)
	 * - La opción de salida solo sale si es médico tratante
	 * - Que no haya un egreso para la cuenta (Ya lleno orden de salida)
	 * (Hay egreso cuando hay una entrada en la tabla egresos y el medico
	 * NO es null)
	 * La diferencia entre los métodos homonimos es que este recibe una cuenta
	 * y asume que está abierta - (El objeto PersonaBasica garantiza esto).
	 *
	 *
	 * @param con Conexión con la fuente de datos
	 * @param paciente Paciente al cual se le desea ingresar una evolución
	 * @param usuario Usuario que desea ingresar la evolución
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacionTratante validarIngresarEvolucion (Connection con, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		//Solo puede llenar una evolución un usuario que sea médico
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validarIngresarEvolucion (con, paciente, usuario) ;
	}

	/**
	 * Este método dice si se debe mostrar reversión del egreso y en caso afirmativo
	 * devuelve en textoRespuesta el motivo a mostrar. Las reglas para saber si hay
	 * que mostrar o no son:
	 * 1. Existe una entrada en la tabla egresos y el médico esta en nulo (Condición
	 * de reversión de egreso)
	 * 2. No se ha mostrado en ninguna evolucion anterior
	 *
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion deboMostrarMotivoReversionEgreso (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().deboMostrarMotivoReversionEgreso (con, idCuenta);
	}

	/**
	 * Este método recibe un número de evolución y revisa si este corresponde
	 * efectivamente a una evolución y en caso que si, nos devuelve en el
	 * campo de texto de RespuestaValidacion "Hospitalizado" si es una
	 * evolución de hospitalizados y un "Urgencias" si es de urgencias
	 * o "General" si es de general
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroEvolucion Número de la Evolución a revisar
	 * @return un objeto RespuestaValidacion que  define si existe evolución
	 * y en caso que si exista en el campo de texto un "Hospitalizado" si es una
	 * evolución de hospitalizados y un "Urgencias" si es de urgencias
	 * o "General" si es de general
	 *
	 * @throws SQLException
	 */
	public static RespuestaValidacion revisarTipoEvolucion (Connection con, int numeroEvolucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().revisarTipoEvolucion(con, numeroEvolucion);
		//@todo En el momento en que se implemente el nuevo estado "Asocio Cuenta" en el sistema, hay que asegurarse que todas las validaciones que se llamen reciban el codigo de la cuenta y no id del paciente, porque las reglas de validacion de cuenta abierta cambian.. (o buscar otra solucion)
	}

	/**
	 * Este método revisa si un usuario puede o no modificar la evolución.
	 *
	 * Las reglas son las siguientes:
	 *
	 * - Si no se ha hecho egreso para a la cuenta, solo el médico tratante
	 * y/o un médico que pertenezca al centro de costo del que lleno la
	 * evolución puede modificar la evolución
	 *
	 * - Después de la orden de egreso y cuando la cuenta aún no ha sido
	 * cerrada, solo el médico tratante.
	 *
	 * - Después de cerrada la cuenta, cualquier usuario puede añadir cosas
	 * (NO eliminar). (condición 4)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta en la que está la evolucion que se
	 * quiere modificar (se asume que la cuenta esta abierta o se pueden agregar
	 * cosas a la misma, tal y como garantiza la cargada de la cuenta )
	 * @param codigoCentroCosto Código del centro de costo del médico (es 0
	 * si se trata de un usuario normal)
	 * @return
	 */
	public static RespuestaValidacionTratante validacionModificarEvolucion (Connection con, int codigoEvolucion, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionModificarEvolucion(con, codigoEvolucion, idCuenta, codigoCentroCosto, institucion);
	}

	/**
	 * Este método revisa cuantas solicitudes de evolucion existen para una
	 * cuenta en particular
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta a la cual se quieren buscar
	 * evoluciones
	 * @return
	 * @throws SQLException
	 */
	public static int numeroSolicitudesValoracionCuenta(Connection con, int idCuenta) throws SQLException
	{

		UtilidadValidacionDao utilidadValidacionDao;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		utilidadValidacionDao=myFactory.getUtilidadValidacionDao();

		return utilidadValidacionDao.numeroSolicitudesValoracionCuenta(con, idCuenta);
	}

	/**
	 * Este método calcula el número de dias que hay entre una fecha
	 * especificadas (La primera debe ser la mayor, en caso contrario
	 * retorna -1)
	 *
	 * @param dia Día de la fecha especificada
	 * @param mes Mes de la fecha especificada
	 * @param anio Año de la fecha especificada
	 * @return
	 */
	public static int numeroDiasEntreFechas (int dia1, int mes1, int anio1, int dia2, int mes2, int anio2)
	{
		return UtilidadFecha.numeroDiasEntreFechas(dia1, mes1, anio1, dia2, mes2, anio2);
	}

	/**
	 * Este método revisa si existe una evolución diaria del tratante,
	 * para saber si puede dar orden de salida (Aparte de la la otra
	 * validación). Si no existe retorna la fecha de la Valoración
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta a la cual se quieren buscar
	 * evoluciones (Debe tener al menos una valoracion)
	 * @return
	 */
	public static RespuestaValidacion existenEvolucionesSuficientesOrdenSalida (Connection con, int idCuenta, int codigoCentroCostoTratante, int diaEvolucion, int mesEvolucion, int anioEvolucion, boolean esUrgencias, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existenEvolucionesSuficientesOrdenSalida(con, idCuenta, codigoCentroCostoTratante, diaEvolucion, mesEvolucion, anioEvolucion, esUrgencias, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param diaEvolucion
	 * @param mesEvolucion
	 * @param anioEvolucion
	 * @param fechaValoracion
	 * @param idCuentaAsociada
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList obtenerListadoEvolucionesNoLlenadas (Connection con, int idCuenta,  int diaEvolucion, int mesEvolucion, int anioEvolucion, String fechaValoracion, int idCuentaAsociada) throws SQLException
	{
		ArrayList resultado=new ArrayList ();
		ArrayList evolucionesExistentes= new ArrayList();
		ArrayList evolucionesTotales= new ArrayList();
		
		logger.info("Fecha Valoracion ==>"+fechaValoracion);
		//por requerimiento no se debe tomar el dia de la valoracion inicial xplanner [id=279412]
		fechaValoracion=UtilidadFecha.incrementarDiasAFecha(fechaValoracion, 1, true);
		logger.info("Fecha Valoracion + 1 dia==>"+fechaValoracion);
		String arregloFechaValoracion[]=fechaValoracion.split("-", 3);
		
		int diaVal=Integer.parseInt(arregloFechaValoracion[2]);
		int mesVal=Integer.parseInt(arregloFechaValoracion[1]);
		int anioVal=Integer.parseInt(arregloFechaValoracion[0]);
		int diaEvol=diaEvolucion;
		int mesEvol=mesEvolucion;
		int anioEvol=anioEvolucion;
		GregorianCalendar calendarioActual = new GregorianCalendar();

		// Los meses en Java empiezan en 0
		int mesJavaEvol = mesEvol - 1, mesJavaVal = mesVal - 1, edadEnDias=0;

		// Fecha Evolucion

		calendarioActual.clear();
		calendarioActual.set(anioEvol, mesJavaEvol, diaEvol);
		Date fechaActual = calendarioActual.getTime();

		//Fecha Dada

		GregorianCalendar calendarioFechaDada = new GregorianCalendar();
		calendarioFechaDada.clear();
		calendarioFechaDada.set(anioVal, mesJavaVal, diaVal);
		Date fechaDada=calendarioFechaDada.getTime();

		int diaMostrar, mesMostrar, anioMostrar;
		
		String fechaInicialVerificacion="";
		String fechaFinalVerificacion="";
		
		while (fechaActual.compareTo(fechaDada)>0)
		{
			//Aumentamos el número de dias
			edadEnDias++;

			diaMostrar=calendarioFechaDada.get(Calendar.DAY_OF_MONTH);
			mesMostrar=calendarioFechaDada.get(Calendar.MONTH) + 1;
			anioMostrar=calendarioFechaDada.get(Calendar.YEAR);
			//Aca se debe buscar si hay una evolucion para este centro de costo
			//en esta fecha, para esta cuenta
			
			if(fechaInicialVerificacion.equals(""))
				fechaInicialVerificacion=anioMostrar + "-" + mesMostrar + "-" + diaMostrar;
			fechaFinalVerificacion=anioMostrar + "-" + mesMostrar + "-" + diaMostrar;
			
			String mesTemp=""+mesMostrar, diaTemp=""+diaMostrar;
			
			if(mesMostrar<10)
				mesTemp="0"+mesTemp;
			if(diaMostrar<10)
				diaTemp="0"+diaTemp;
			
			evolucionesTotales.add(anioMostrar + "-" + mesTemp + "-" + diaTemp);
			/*if (!existeEvolucionDadaFecha (con, idCuenta,  diaMostrar, mesMostrar, anioMostrar, idCuentaAsociada))
			{
				//Lo añadimos a la lista de respuesta
				resultado.add(anioMostrar + "-" + mesMostrar + "-" + diaMostrar);
			}*/
			
			calendarioFechaDada.add(Calendar.DAY_OF_MONTH, 1);
			fechaDada=calendarioFechaDada.getTime();
		}
		
		if(!fechaInicialVerificacion.equals("") && !fechaFinalVerificacion.equals(""))
		{
			logger.info("FECHAINCIIAL-->"+fechaInicialVerificacion+" FECHAFINAL->"+fechaFinalVerificacion);
			evolucionesExistentes = existeEvolucionesRangoFechas(con, idCuenta, fechaInicialVerificacion, fechaFinalVerificacion, idCuentaAsociada);
			
			logger.info("EVOLUCIONESTOTALES-->"+evolucionesTotales);
			logger.info("EVOLUCIONexistentes-->"+evolucionesExistentes);
			
			for(int w=0; w<evolucionesTotales.size(); w++)
			{
				if(!evolucionesExistentes.contains(evolucionesTotales.get(w)))
				{
					resultado.add(evolucionesTotales.get(w));
				}
			}
		}
		logger.info("termina");
		return resultado;
	}

	/**
	 * Indica si existen evoluciones en un rango de fechas, devolviendo un arrayList con las fechas que tienen evol
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param idCuentaAsociada
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList existeEvolucionesRangoFechas(Connection con, int idCuenta,  String fechaInicial, String fechaFinal, int idCuentaAsociada)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEvolucionesRangoFechas(con, idCuenta, fechaInicial, fechaFinal, idCuentaAsociada);
	}
	
	/**
	 * Este método dice si hay una evolución para un día
	 * en particular, para un centro de costo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta a la cual se le quiere buscar
	 * la evolucion
	 * @param dia Día en el cual se quiere buscar la evolucion
	 * @param mes Mes en el cual se quiere buscar la evolucion
	 * @param anio Año en el cual se quiere buscar la evolucion
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeEvolucionDadaFecha (Connection con, int idCuenta, int dia, int mes, int anio, int idCuentaAsociada) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEvolucionDadaFecha(con, idCuenta,  dia, mes, anio, idCuentaAsociada);
	}

	/**
	 * Método que retorna verdadero si para esta cuenta la valoración de
	 * Urgencias no esta en cama de observación y falso si esta en cama
	 * de observación o si no existe la valoración de urgencias. (Se utilizo
	 * el prefijo no para poder seguir trabajando con boolean)
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta para la cual se quiere buscar
	 * la conducta a seguir en su primera valoración
	 * @return
	 */
	public static boolean estaEnCamaObservacion(Connection con, int idCuenta)
	{
		try
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estaEnCamaObservacion(con, idCuenta);
		}
		catch(SQLException e)
		{
			logger.error("Error en estaEnCamaObservacion: "+e);
			return false;
		}
	}

	/**
	 * Método que revisa si existe una epicrisis o no, dado el código. Para el caso
	 * particular de la epicrisis de urgencias no basta con que exista, sino que se
	 * debe revisar si el estado a seguir en la valoracion de urgencias es cama de
	 * observacion
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idEpicrisis Código de la epicrisis (ingreso) que se desea buscar
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion existeEpicrisis (Connection con, int idEpicrisis, boolean esEpicrisisHospitalizacion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEpicrisis (con, idEpicrisis, esEpicrisisHospitalizacion);
	}

	/**
	 * Este método revisa si se tienen permisos suficientes para agregar notas
	 * aclaratorias y/o definir la epicrisis de hospitalizacion o urgencias
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idEpicrisis Código de la epicrisis (ingreso) al que se quiere
	 * revisar los permisos para nota y definicion de epicrisis
	 * @param numeroIdentificacionPaciente Número de identificación
	 * del paciente al que se quiere revisar los permisos para nota y definicion
	 * de epicrisis
	 * @param codigoTipoIdentificacionPaciente Código del tipo de
	 * identificación del paciente al que se quiere revisar los permisos para
	 * nota y definicion de epicrisis
	 * @param centroCostoMedico Centro de costo del médico que quiere
	 * o definir o agregar nota aclaratoria a la epicrisis
	 * @param codigoInstitucion Código de la institución a la que pertenece
	 * el médico que quiere o definir o agregar nota aclaratoria a la epicrisis
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacionTratante permisosEpicrisisNotaYDefinicion (Connection con, int idEpicrisis, PersonaBasica paciente, UsuarioBasico usuario, boolean esEpicrisisHospitalizacion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().permisosEpicrisisNotaYDefinicion (con, idEpicrisis, paciente, usuario, esEpicrisisHospitalizacion, usuario.getCodigoInstitucionInt()) ;
	}

	/**
	 * Este método nos dice que días faltan por evolución para una epicrisis
	 * aparte de la que da orden de salida
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idIngreso Ingreso en el que se va a revisar
	 * @param esEpicrisisHospitalizacion Define si se va a revisar epicrisis
	 * de hospitalización o de urgencias
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion faltanEvolucionesEpicrisis (Connection con, int idIngreso, boolean esEpicrisisHospitalizacion, boolean esUrgencias, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().faltanEvolucionesEpicrisis(con, idIngreso, esEpicrisisHospitalizacion, esUrgencias, institucion);
	}

	/**
	 * Este método se encarga de validar si se puede finalizar un egreso
	 * (Tomar los datos dados por el médico en la orden de salida y
	 * consolidarlos). Las reglas son las siguientes:
	 *
	 * -Debe  haber una entrada en la tabla egresos: Si no existe es porque
	 * el médico no ha dado orden de salida
	 * -El Usuario responsable debe estar en nulo: Si la tabla tiene algún
	 * valor diferente a nulo quiere decir que alguien ya finalizo el egreso.
	 * -El Médico Responsable NO debe estar en nulo: Si lo esta es porque
	 * estamos en medio de una reversión de egreso y el médico no ha dado
	 * orden de salida
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta para la cual se quiere finalizar
	 * el egreso
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion puedoFinalizarEgreso (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoFinalizarEgreso (con, idCuenta);
	}

	/**
	 * Este método se encarga de revisar si una cuenta tiene
	 * un egreso completo. Reglas para saber si un egreso está
	 * completo o no:
	 * 1. Existe una entrada en la tabla egresos con este codigo
	 * 2. En esta entrada el medico no es nulo
	 * 3. En esta entrada el usuario no esnulo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeEgresoCompleto (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEgresoCompleto (con, idCuenta);
	}

	/**
	 * Este método se encarga de revisar si una cuenta tiene
	 * un egreso automático. Reglas para saber si un egreso
	 * es automático o no:
	 * 1. Existe una entrada en la tabla egresos con este codigo
	 * 2. En esta entrada el campo es automático está en true
	 * Este método retorna un objeto de tipo RespuestaValidacion
	 * donde su boolean define si existe un egreso automático y
	 * el String el login del usuario que lo creo. El usuario o método
	 * que llame a este método sabe que si el login es nulo esta
	 * en medio de una reversión de egreso
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion existeEgresoAutomatico (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEgresoAutomatico (con, idCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static boolean existeEgresoMedico(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEgresoMedico(con, cuenta);
	}
	
	/**
	 * Este método se encarga de revisar si se puede reversar un egreso.
	 * Las reglas para esto son:
	 *
	 * 0. Debe existir una admisión de hosp. para la cuenta especificada
	 * 1.Si es el médico tratante puede reversar el egreso si:
	 * 	1.1. Existe una entrada en la tabla egresos con este codigo
	 * 	1.2. En esta entrada el médico no es nulo
	 * 	(La condición de usuario no nulo no es necesaria, ya que esto pasa
	 * 	cuando el médico ha dado orden de salida y todavía no se ha llenado
	 * 	orden de salida)
	 * 2. Si no es el médico tratante puede reversar si:
	 * 	2.1. Existe una entrada en la tabla egresos con este codigo
	 * 	2.2. En esta entrada el médico no es nulo
	 * 	2.3. En esta entrada el usuario no es nulo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @param codigoCentroCosto Código del centro de costo del médico / usuario
	 * que desea saber si puede o no realizar la reversión
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacionTratante puedoReversarEgresoHospitalizacion (Connection con, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoReversarEgresoHospitalizacion (con, idCuenta, codigoCentroCosto, institucion);
	}

	/**
	 * Método que evalúa si se puede o no reversar un egreso por tiempo, (puede haber como
	 * máximo un día (24 horas) entre la evolución y el momento en que se quiere hacer el
	 * egreso)
	 * @param con una conexion abierta con una BD
	 * @param idCuenta Número de la cuenta para la cual se quiere revisar
	 * si se puede reversar un egreso por tiempo
	 * @return
	 * @throws SQLException
	 */
	public static boolean puedoReversarEgresoPorTiempo(Connection ac_con, int ai_idCuenta) throws SQLException
	{
		ResultSetDecorator lrs_rs	= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoReversarEgresoPorTiempo(ac_con, ai_idCuenta);

		String fechaHoraComparar 	= null;
		String fechaHoraActual 		= null;
		
		if(lrs_rs.next() )
		{
			fechaHoraComparar 	=  UtilidadFecha.conversionFormatoFechaAAp(lrs_rs.getString("fechaEgreso"));
			fechaHoraComparar	+= " " + UtilidadFecha.conversionFormatoFechaAAp(lrs_rs.getString("horaEgreso"));
			
			if(UtilidadTexto.isEmpty(fechaHoraComparar)){
				fechaHoraComparar 	=  UtilidadFecha.conversionFormatoFechaAAp(lrs_rs.getString("fechaEvolucion"));
				fechaHoraComparar	+= " " + UtilidadFecha.conversionFormatoFechaAAp(lrs_rs.getString("horaEvolucion"));
			}

			fechaHoraActual		=  UtilidadFecha.conversionFormatoFechaAAp(lrs_rs.getString("fechaActual"));
			fechaHoraActual	 	+= " " + UtilidadFecha.conversionFormatoFechaAAp(lrs_rs.getString("horaActual"));
			
			if(UtilidadTexto.isEmpty(fechaHoraActual) || UtilidadTexto.isEmpty(fechaHoraComparar))
			{
				return false;
			}
			else
			{
				Date				fechaHoraActualDate;
				Date				fechaHoraCompararDate;
				SimpleDateFormat	simpleDateFormat;

				try
				{
					/* Iniciar variables */
					simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm");

					/* Exije una interpretación estricta del formato de fecha/hora esperado */
					simpleDateFormat.setLenient(false);

					fechaHoraActualDate = simpleDateFormat.parse(fechaHoraActual.toString() );
					fechaHoraCompararDate = simpleDateFormat.parse(fechaHoraComparar.toString() );
					
					logger.info("fecha actual: "+fechaHoraActualDate);
					logger.info("fecha Egreso: "+fechaHoraCompararDate);
					
					/* 24 horas corresponden a 86400000 milisegundos */
					logger.info("tiempo: "+(fechaHoraActualDate.getTime() - fechaHoraCompararDate.getTime() )+" <= 86400000");
					return (fechaHoraActualDate.getTime() - fechaHoraCompararDate.getTime() ) <= 86400000;
				}
				catch(Exception lpe_e)
				{
					return false;
				}
			}
		}
		else{
			return false;
		}
			
	}

	/**
	 * Este método valida si para una combinación de llaves de cuenta,
	 * admisión hospitalaria/urgencias existe una fecha de admisión/cuenta
	 * Si ni siquiera existe la cuenta especificada en el texto de RespuestaValidacion
	 * se explica la razón, si existe se devuelve la fecha en cuestión
	 *
	 * Reglas:
	 * - Si el año de admisión es mayor que 0, nos encontramos con una admisión
	 * de urgencias,
	 * - Si el año de admisión no es mayor que 0 pero el codigo de la admisión es
	 * mayor que 0, nos encontramos con una admisión de urgencias
	 * - Si el año de admisión no es mayor que 0 y el codigo de la admisión no es
	 * mayor que 0, nos encontramos con una cuenta sin admisión (o ninguna
	 * cuenta)
	 *
	 * @param con una conexion abierta con una BD
	 * @param idCuenta Código de la cuenta
	 * @param idAdmision Código de la admisión de urgencias (o de
	 * hospitalización si el año de admisión es 0)
	 * @param anioAdmision Año de la admisión de urgencias (o 0 si no es
	 * de urgencias)
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion tieneFechaAdmisionCuenta (Connection con, int idCuenta, int idAdmision, int anioAdmision) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneFechaAdmisionCuenta (con, idCuenta, idAdmision, anioAdmision);
	}

	/**
	 * Este método se encarga de validar si una admisión de urgencias tiene o no
	 * los datos de la cama de observación (Antes de poder dar un egreso NO
	 * automático el paciente debe tener como conducta a seguir cama de observación
	 * asignada - Vía modificar admisión)
	 *
	 * @param con una conexion abierta con una BD
	 * @param idAdmision Código de la admisión de urgencias
	 * @param anioAdmision Año de la admisión de urgencias
	 * @return
	 * @throws SQLException
	 */
	public static boolean tieneCamaParaEgresoUrgencias (Connection con, int idAdmision, int anioAdmision) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneCamaParaEgresoUrgencias (con, idAdmision, anioAdmision);
	}

	/**
	 * Este método se encarga de revisar si existe una cuenta para una evolución
	 * (código evolución dado) existe cuenta, si no existe el texto en
	 * RespuestaValidacion da la razón, si existe da el número de la cuenta
	 *
	 * @param con una conexion abierta con una BD
	 * @param codigoEvolucion Código de la evolución
	 * a la cual se va a buscar cuenta
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion existeCuentaParaEvolucion (Connection con, int codigoEvolucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCuentaParaEvolucion(con, codigoEvolucion);
	}

	/**
	 * Este método se encarga de revisar si se puede reversar un egreso.
	 * de urgencias. Las reglas para esto son:
	 *
	 * 0. Debe existir una admisión de urgencias para la cuenta especificada
	 * 1.Si es el médico tratante puede reversar el egreso si:
	 * 	1.1. Existe una entrada en la tabla egresos con este codigo
	 * 	1.2. En esta entrada el médico no es nulo
	 * 	(La condición de usuario no nulo no es necesaria, ya que esto pasa
	 * 	cuando el médico ha dado orden de salida y todavía no se ha llenado
	 * 	orden de salida)
	 * 2. Si no es el médico tratante puede reversar si:
	 * 	2.1. Existe una entrada en la tabla egresos con este codigo
	 * 	2.2. En esta entrada el médico no es nulo
	 * 	2.3. En esta entrada el usuario no es nulo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Número de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @param codigoCentroCosto Código del centro de costo del médico / usuario
	 * que desea saber si puede o no realizar la reversión
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacionTratante puedoReversarEgresoUrgencias(Connection ac_con, int ai_idCuenta, int ai_codigoCentroCosto) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoReversarEgresoUrgencias(ac_con, ai_idCuenta, ai_codigoCentroCosto);
	}

	/**
	* Valida si un profesional de la salud pertenece o no a una categoría
	*
	* @param aub_profesionalSalud Profesional de la salud que se va a
	* validar
	* @param as_categoria Categoria que se espera tenga el profesional
	* de la salud
	* @return <code>Resultado</code> indicando si aub_profesionalSalud
	* pertenece o no a la categoría as_categoria
	*/
	public static ResultadoBoolean validacionProfesionalSalud(UsuarioBasico aub_profesionalSalud, String as_categoria)
	{
		/* El profesional de la salud es inválido */
		if(aub_profesionalSalud == null)
			return new ResultadoBoolean(false, "No hay níngun usuario cargado");

		/* Número de registro médico del profesional de la salud */
		String ls_registro = aub_profesionalSalud.getNumeroRegistroMedico();

		if(ls_registro == null || (ls_registro = ls_registro.trim() ).equals("") || ls_registro.equals("-") )
			return new ResultadoBoolean(false, "El usuario no es profesional de la salud");

		/* Categoría del profesional de la salud */
		String ls_categoria = aub_profesionalSalud.getOcupacionMedica();

		if(ls_categoria == null || (ls_categoria = ls_categoria.trim() ).equals("") )
			return new ResultadoBoolean(false, "El profesional de la salud no tiene categoría asignada");

		if(ls_categoria.toLowerCase().indexOf(as_categoria.toLowerCase() ) < 0)
			return new ResultadoBoolean(false, "El profesional de la salud no pertenece a la categoría " + as_categoria);

		return new ResultadoBoolean(true, "El profesional de la salud pertenece a la categoría " + as_categoria);
	}

	/**
	 * Este método se  encarga de revisar, dado el número del ingreso,
	 * si se puede o no crear una cuenta en asocio
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta Código de la cuenta a la que se quiere revisar si
	 * está asociada o no
	 * @return
	 * @throws SQLException
	 */
	public static boolean puedoCrearCuentaAsocio (Connection con, int idIngreso) 
	{
		boolean puedoCrear = false;
		try
		{
			puedoCrear = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoCrearCuentaAsocio (con, idIngreso);
		}
		catch(SQLException e)
		{
			logger.error("Error en puedoCrearCuentaAsocio: "+e);
		}
		return puedoCrear;
	}

	/**
	 * Este método revisa si una cuenta para un ingreso por hospitalización
	 * está facturada o no. Si la cuenta no existe y/o no hay admisión hospitalaria,
	 * tira una excepción
	 *
	 * @param con una conexion abierta con una BD
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static boolean validacionCuentaFacturada(Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionCuentaFacturada(con, idIngreso, esHospitalizacion);
	}

	/**
	* Valida si un usuario es o no profesional de la salud
	*
	* @param usuario que se va a validar
	* @return <code>boolean</code> indicando si el usuario es o no profesional de la salud
	* @throws SQLException
	*/
	public static boolean esProfesionalSalud(UsuarioBasico aub_profesionalSalud)
	{
		boolean inactivo=false; 
		boolean registro=false;
	
		/* El profesional de la salud es inválido */
		if(aub_profesionalSalud == null)
			return false;
		
		//**********sección para verificar que el profesional de la salud se encuentre activo*********
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			inactivo=estaMedicoInactivo(con,aub_profesionalSalud.getCodigoPersona(),aub_profesionalSalud.getCodigoInstitucionInt());
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en esProfesionalSalud de UtilidadValidación al verificar si el profesional está activo: "+e);
		}
		//*****************************************************
		
		/* Número de registro médico del profesional de la salud */
		String ls_registro = aub_profesionalSalud.getNumeroRegistroMedico();
		/* Valida si el registro médico es válido. Si lo es, el usuario es profesional de la salud */
		registro=!(ls_registro == null || (ls_registro = ls_registro.trim() ).equals("") || ls_registro.equals("-") );
		
		
		if(registro&&!inactivo)
			return true;
		else
			return false;
	}

	/**
	* Valida si un usuario es o no profesional de la salud
	*
	* @param usuario que se va a validar
	* @return <code>boolean</code> indicando si el usuario es o no profesional de la salud
	* @throws SQLException
	*/
	public static boolean esProfesionalSalud(int codigoPersona, int codigoInstitucion, String numeroRegistroMedico )
	{
		boolean inactivo=false; 
		boolean registro=false;
	
		/* El profesional de la salud es inválido
		if(aub_profesionalSalud == null)
			return false;
		*/
		//**********sección para verificar que el profesional de la salud se encuentre activo*********
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			inactivo=estaMedicoInactivo(con,codigoPersona,codigoInstitucion);
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en esProfesionalSalud de UtilidadValidación al verificar si el profesional está activo: "+e);
		}
		//*****************************************************
		
		/* Número de registro médico del profesional de la salud */
		String ls_registro = numeroRegistroMedico;
		/* Valida si el registro médico es válido. Si lo es, el usuario es profesional de la salud */
		registro=!(ls_registro == null || (ls_registro = ls_registro.trim() ).equals("") || ls_registro.equals("-") );
		
		
		if(registro&&!inactivo)
			return true;
		else
			return false;
	}

	/**
	 * Este método me dice si debo o no mostrar el link de enviar evoluciones a
	 * epicrisis.
	 * Las reglas dicen que NO se debe mostrar si:
	 * 	- La cuenta debe estar abierta (si es 0 ya sabemos que no lo está) Y
	 * 	- El profesional de la salud NO es tratante O es enfermera O No es profesional de la salud
	 *
	 * En realidad hay que validar tanto cuando tenga orden de egreso como
	 * cuando no, mientras no haya egreso completo Y la cuenta no esté facturada,
	 * nadie que no sea el personal de la salud (aparte de enfermera) tiene acceso a
	 * modificar la evolucion
	 *
	 * @param con Conexión con la fuente de datos
	 * @param paciente Paciente cargado
	 * @param usuario Usuario cargado
	 * @return
	 */
	public static boolean deboMostrarEnviarEvolucionesEpicrisis (Connection con, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		if (paciente==null||usuario==null)
		{
			//Si el paciente y/o el usuario no esta cargado no mostramos el link
			return false;
		}

		if (paciente.getCodigoCuenta()==0)
		{
			//En este caso la cuenta esta cerrada, luego debemos mostrar el link
			//Nos debe llegar de un medico cargado
			return true;
		}

		//En este punto sabemos que el usuario tiene la cuenta abierta,
		//primera condición, ahora vamos a revisar si este paciente tiene
		//orden de salida

/*
		En realidad hay que validar tanto cuando tengar orden de egreso
		como cuando no, mientras no haya egreso completo Y la cuenta no
		esté facturada, nadie más tiene acceso
		String tieneOrdenSalidaCompletaStr="SELECT count(1) as numResultados from egresos  where cuenta=" + paciente.getCodigoCuenta() + " and numero_identificacion_medico is not null";
		TagDao tagDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTagDao();
		Answer a=tagDao.resultadoConsulta(con, tieneOrdenSalidaCompletaStr);
		ResultSetDecorator rs=a.getResultSet();

		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				//En este caso no tiene orden de salida, luego mostramos el link
				rs.close();
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en deboMostrarEnviarEvolucionesEpicrisis");
		}
*/

		//A este punto ya sabemos que tiene cuenta abierta y orden de salida (o egreso)
		//valida para esa cuenta. Si en este punto el usuario NO es profesional de la salud
		//o es enfermera  => NO es el tratante, luego no puede ver

		if (!esProfesionalSalud(usuario)||  esEnfermera(usuario).equals("")  )
		{
			return false;
		}

		//A este punto hay cuenta abierta, orden de salida y sabemos que es profesional
		//de la salud y NO es enfermera
		//Si pertenece al centro de costo tratante retornamos true, en cualquier otro
		//caso retornamos false

		return esMedicoTratante(con, usuario, paciente).equals("");

	}

	/**
	 * Método que dado un objeto UsuarioBasico con un posible profesional
	 * de la salud, revisa si este es enfermera o no
	 *
	 * @param profesionalSalud objeto UsuarioBasico con un posible profesional
	 * de la salud
	 * @return
	 * Si el usuario no es enfermera se retorna una cadena con el error key,
	 * de lo contrario se retorna una cadena vacía
	 */
	public static String esEnfermera (UsuarioBasico profesionalSalud)
	{
		int institucion = profesionalSalud.getCodigoInstitucionInt();
		String ocupacionEnfermera = ValoresPorDefecto.getCodigoOcupacionEnfermera(institucion,true);
		String ocupacionAuxiliarEnfermeria = ValoresPorDefecto.getCodigoOcupacionAuxiliarEnfermeria(institucion,true);
		int codigoEnfermera = 0;
		int codigoAuxiliarEnfermeria = 0;

		if(ocupacionEnfermera.equals("")||ocupacionAuxiliarEnfermeria.equals(""))
		{
			return "errors.noOcupacionEnfermera";
		}
		
		codigoEnfermera = Integer.parseInt(ocupacionEnfermera);
		codigoAuxiliarEnfermeria = Integer.parseInt(ocupacionAuxiliarEnfermeria);
		
		if (profesionalSalud==null||
			(profesionalSalud.getCodigoOcupacionMedica()!=codigoEnfermera&&profesionalSalud.getCodigoOcupacionMedica()!=codigoAuxiliarEnfermeria))
		{
			return "errors.usuario.noAutorizado";
		}
		else
		{
			//cadena vacía quiere decir que el usuario es Enfermera
			return "";
		}
	}

	/**
	 * Método que dado un objeto UsuarioBasico con un posible profesional
	 * de la salud, revisa si este es medico o no
	 *
	 * @param medico objeto UsuarioBasico con un médico
	 * @return 
	 * Si el usuario no es médico se retorna una cadena con el error key,
	 * de lo contrario se retorna una cadena vacía
	 */
	public static String esMedico (UsuarioBasico medico)
	{
		int institucion = medico.getCodigoInstitucionInt();
		String ocupacionMedicoGeneral = ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(institucion,true);
		String ocupacionMedicoEspecialista = ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(institucion,true);
		int codigoGeneral = 0;
		int codigoEspecialista = 0;
		
		if(ocupacionMedicoGeneral.equals("")||ocupacionMedicoEspecialista.equals(""))
		{
			
			return "errors.noOcupacionMedica";
		}
		
		codigoGeneral = Integer.parseInt(ocupacionMedicoGeneral);
		codigoEspecialista = Integer.parseInt(ocupacionMedicoEspecialista);
		
		if (medico==null||
			(medico.getCodigoOcupacionMedica()!=codigoGeneral&&medico.getCodigoOcupacionMedica()!=codigoEspecialista))
		{
			
			return "errors.noProfesionalSaludMedico";
		}
		else
		{
			//cadena vacía quiere decir que el usuario es Médico
			
			return "";
		}
	}

	/**
	 * Este método dice si un médico es tratante o no. Primero revisa si
	 * efectivamente es médico y si existe una entrada en la tabla consultas
	 * cuenta una entrada que diga si este médico es el tratante
	 *
	 * @param con Conexión con la fuente de datos
	 * @param medico Médico del que se desea averiguar si es tratante o no
	 * @param paciente PersonaBasica de la sesion
	 * @return
	 * Si el usuario no es médico se retorna una cadena con el error key,
	 * de lo contrario se retorna una cadena vacía
	 * @throws SQLException
	 */
	public static String esMedicoTratante (Connection con, UsuarioBasico medico, PersonaBasica paciente) throws SQLException
	{
		
		//Si no es hospital día tiene que se médico el tratante
		if (!paciente.isHospitalDia()&&!esMedico(medico).equals(""))
		{
			
			return esMedico(medico);
		}
		//Si es hospital día el tratante como mínimo debe ser un profesional de la salud
		else if(paciente.isHospitalDia()&&!esProfesionalSalud(medico))
		{
			return "errors.noProfesionalSalud";
		}
		//Se verifica que el profesional de la salud se encuentra activo
		else if(estaMedicoInactivo(con, medico.getCodigoPersona(), medico.getCodigoInstitucionInt()))
		{
			return "errors.profesionalSaludInactivo";
		}
		else if (DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esMedicoTratante(con,medico.getLoginUsuario(),paciente.getCodigoCuenta()))
		{
			
			return "";
		}
		else
		{
			
			return "error.valoracion.medicoNoGrupoTratante";
		}
	}
	
	/**
	 * Este método dice si un médico es tratante o no. Primero revisa si
	 * efectivamente es médico y si existe una entrada en la tabla consultas
	 * cuenta una entrada que diga si este médico es el tratante
	 *
	 * @param con Conexión con la fuente de datos
	 * @param medico Médico del que se desea averiguar si es tratante o no
	 * @param idCuenta Número de la cuenta sobre la que se desea revisar
	 * si es tratante o no
	 * @return
	 * Si el usuario no es médico se retorna una cadena con el error key,
	 * de lo contrario se retorna una cadena vacía
	 * @throws SQLException
	 */
	public static String esMedicoTratante (Connection con, UsuarioBasico medico, int idCuenta) throws SQLException
	{
		
		if (!esMedico(medico).equals(""))
		{
			
			return esMedico(medico);
		}
		else if (DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esMedicoTratante(con,medico.getLoginUsuario(),idCuenta))
		{
			
			return "";
		}
		else
		{
			
			return "error.valoracion.medicoNoGrupoTratante";
		}
	}
	

	/**
	 * Método que dice si un centro de costo es o no el tratante para
	 * una cuenta particular
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se desea
	 * saber si el centro de costo es tratante o no
	 * @param codigoCentroCosto Código del centro de costo sobre
	 * el que se desea saber si es tratante o no
	 * @return
	 * @throws SQLException
	 */
	public static boolean esCentroCostoTratante (Connection con,  PersonaBasica paciente, int codigoCentroCosto, int institucion) throws SQLException
	{
		/* Cuando es via de ingreso urgencias, el tratante se debe validar
		 * contra el parámetro general CentroCostoUrgencias
		 */
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && codigoCentroCosto==Integer.parseInt(ValoresPorDefecto.getCentroCostoUrgencias(institucion)))
		{
			return true;
		}
		if (UtilidadValidacion.getCodigoCentroCostoTratante(con, paciente, institucion)==codigoCentroCosto)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Método que me dice si para un centro de costo particular y una
	 * cuenta, hay evoluciones del dia de hoy
	 *
	 * @param con Conexion con la fuente de datos
	 * @param idCuenta Codigo de la cuenta en la que se desea buscar
	 * la evolución
	 * @param codigoCentroCostoTratante Codigo del centro costo para
	 * el cual se desea buscar la evolución
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeEvolucionHoy (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeEvolucionHoy(con, idCuenta);
	}

	/**
	 * Este método revisa si hay algún centro de costo válido y si lo hay lo
	 * retorna en el campo textoRespuesta de RespuestaValidacion
	 *
	 * @param con Conexión con la fuente de datos
	 * @return
	 */
	public static RespuestaValidacion existeCentroCostoValido (Connection con) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCentroCostoValido (con);
	}

	/**
	 * Método que dice si un Usuario es además paciente
	 *
	 * @param con Conexión con la fuente de datos
	 * @param usuario objeto UsuarioBasico con los datos
	 * del usuario al que se quiere verificar si es paciente
	 * en el sistema
	 * @return
	 * @throws SQLException
	 */
	public static boolean esUsuarioPaciente (Connection con, UsuarioBasico usuario) throws SQLException
	{
		if (usuario==null)
		{
			return false;
		}

		ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esUsuarioPaciente(con, usuario.getCodigoPersona(), usuario.getCodigoInstitucion());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>=1)
			{
				//Se encontró el paciente con el rol
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en esUsuarioPaciente ");
		}
	}

	/**
	 * Metodo para saber si un usuario esta activo en el sistema.
	 * @param con
	 * @param codigoPersona
	 * @return
	 * @throws SQLException
	 */
	public static boolean esUsuarioActivo (Connection con, String login, int codigoInstitucion) throws SQLException
	{

		ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esUsuarioActivo(con, login, codigoInstitucion);
		if (rs.next())
		{
			if (rs.getInt("numResultados")>=1)
			{
				//Se encontró el paciente en la tabla  
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en esUsuarioPaciente ");
		}
	}
	
	
	/**
	 * Este método permite saber si una cuenta con valoración
	 * de urgencias tiene como conducta a seguir cama de
	 * observación. El método retorna verdadero si se cumple
	 * esta condición y falso de lo contrario (si, por ejemplo no
	 * existe valoración de urgencias también retorna falso)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta en la que se
	 * desea buscar la conducta de la valoración
	 * @return
	 */
	public static boolean tieneValoracionConConductaCamaObservacion (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneValoracionConConductaCamaObservacion (con, idCuenta);
	}

	/**
	 * Método que revisa si una cuenta tiene una valoración la
	 * cual tenga una de las conductas a seguir
	 * pasadas por parámetro
	 * @param con Conexión con la BD
	 * @param idCuenta id de la cuenta que se desea validar
	 * @param conductas Array int con los códigos de las conductas a validar
 	 * @return true si hay valoraciones con alguna de las conductas dadas.
 	 */
	public static boolean validacionConductaASeguir (Connection con, int idCuenta, int[] conductas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionConductaASeguir(con, idCuenta, conductas);
	}

	/**
	 * Método que dice si para una cuenta particular existe un semiegreso
	 * y si este ya se llenó o no. Para esto retorna un arreglo con dos boolean
	 * donde el primero dice si tiene semi egreso y el segundo elemento, en
	 * caso de tener semi egreso dice si ya fue completado o no
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Número de la cuenta en la que se desea buscar
	 * @return
	 * @throws SQLException
	 */
	public static boolean[] tieneSemiEgreso (Connection con, int idCuenta) throws SQLException
	{
		boolean respuesta[]={false, false};
		ResultSetDecorator rs=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneSemiEgreso(con, idCuenta);
		if (rs.next())
		{
			respuesta[0]=true;
			//Si salio un resultado quiere decir que al menos existio
			//un semi-egreso, ahora hay que revisar si
			if (UtilidadTexto.isEmpty(rs.getString("usuarioResponsable")))
			{
				//Si el usuario responsable es nulo, quiere decir que
				//que no se ha completado
				respuesta[1]=false;
			}
			else
			{
				//Si el usuario responsable es nulo, quiere decir que
				//que se ha completado
				respuesta[1]=true;
			}
			return respuesta;
		}
		else
		{
			//retornamos los valores por defecto (false, false)
			return respuesta;
		}
	}

	/**
	 * Método que revisa si se tiene acceso a una solicitud y por medio de
	 * un objeto RespuestaValidacion, da la razón del rechazo, si la hay
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a la que se quieren
	 * revisar permisos de acceso
	 * @param tipoSolicitud Tipo de la solicitud a revisar, definido en la
	 * interfaz de constantes "util.ConstantesBD"
	 * @param medico Médico / usuario que la desea contestar (objeto
	 * UsuarioBasico)
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */
	public static RespuestaValidacion validacionAccesoSolicitud (Connection con, int numeroSolicitud, String tipoSolicitud, UsuarioBasico medico, PersonaBasica paciente) throws SQLException
	{
		if(!esMedicoTratante(con, medico, paciente).equals("") && !esAdjuntoCuenta(con, paciente.getCodigoCuenta(), medico.getLoginUsuario()))
		{
			return new RespuestaValidacion("No es ni tratante ni adjunto. Revisar",false);	
		}	
		
		/*UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		int permisos[]=utilidadValidacionDao.obtenerPermisosAcceso_validacionAccesoSolicitud(con, numeroSolicitud, tipoSolicitud);*/
/*
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			/*
			 * Si la vía de ingreso del paciente es Urgencias, se debe validar contra
			 * el centro de costo paramétrizado en valores por defecto
			 *   *
			permisos[1]=Integer.parseInt(ValoresPorDefecto.getCentroCostoUrgencias(medico.getCodigoInstitucionInt()));
		}
*/
		
		/*logger.info("permisos[0] ocupacionMedica->"+permisos[0]+ "codigoCentroCosto permisos[1]="+permisos[1]);
		
		if (!esMedico(medico).equals(""))
		{
			//Puede que no se haya definido la ocupación medico especialista y general
			//en prámetros generales
			if(esMedico(medico).equals("errors.noOcupacionMedica"))
				return new RespuestaValidacion("Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales",false);
			
			
			//Si el usuario no es médico, el código de la ocupación
			//debe ser 0 y su centro de costo debe coincidir
			//con el permiso dado o ser 0
			

			if (permisos[0]==0&&   (permisos[1]==0||permisos[1]==medico.getCodigoCentroCosto())	)
			{
				return new RespuestaValidacion ("El usuario no es medico, pero tiene permisos para responder esta solicitud", true);
			}
			else
			{
				if (permisos[0]!=0)
				{
					return new RespuestaValidacion ("Para un usuario no médico, la solicitud debe tener permisos abiertos para ocupacion y esta solicitud los tiene cerrados", false);
				}
				else
				{
					return new RespuestaValidacion ("El centro de costo de la solicitud no corresponde con el centro de costo del usuario", false);
				}
			}
		}

		//Si llegamos a este punto es porque el usuario si es médico
		//cada uno de los criterios debe cumplirse (ocupacion y c. costo)

		//Revisamos primero los permisos de la ocupación
		
		if (permisos[0]!=0&&permisos[0]!=medico.getCodigoOcupacionMedica())
		{
			logger.info("La ocupación del médico no concuerda con la especificada en la solicitud --> medico.getCodigoOcupacionMedica()->"+medico.getCodigoOcupacionMedica());
			return new RespuestaValidacion ("La ocupación del médico no concuerda con la especificada en la solicitud", false);
		}
		//Ahora el centro de costo
		if(!UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals(""))
		{
			if(permisos[1]!=medico.getCodigoCentroCosto() && paciente.getExisteAsocio() /*&& medico.getCodigoCentroCosto() == ConstantesBD.codigoCentroCostoUrgencias*//*)
			{	
				logger.info("El paciente tiene una cuenta asociada de urgencias, por eso el centro de costo no concuerda -->medico.getCodigoCentroCosto()="+medico.getCodigoCentroCosto());
				return new RespuestaValidacion("El paciente tiene una cuenta asociada de urgencias, por eso el centro de costo no concuerda",true);
			}	
			else
			{
				if(permisos[1]!=medico.getCodigoCentroCosto())
				{
					logger.info("El centro de costo del médico no concuerda con el especificado en la solicitud-->medico.getCodigoCentroCosto()="+medico.getCodigoCentroCosto());
					return new RespuestaValidacion ("El centro de costo del médico no concuerda con el especificado en la solicitud", false);
				}
			}
		}*/
	
		//Si llega a este punto es porque cumple todas las condiciones
		return new RespuestaValidacion ("Puede responder la solicitud sin problemas", true);
	}

	/**
	 * Método que dada una admisión de urgencias obtiene su fecha y hora de
	 * ingreso a observación
	 *
	 * @param con Conexión con la fuente de datos
	 * @param codigoAdmision Código de la admisión a la que se le buscará
	 * su fecha y hora de ingreso a observación
	 * @param anioAdmision Anio de la admisión a la que se le buscará
	 * su fecha y hora de ingreso a observación
	 * @return
	 * @throws SQLException
	 */
	public static String[] obtenerFechayHoraObservacionAdmisionUrgencias (Connection con, int codigoAdmision, int anioAdmision) throws SQLException
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		String resultado[]= utilidadValidacionDao.obtenerFechayHoraObservacionAdmisionUrgencias(con, codigoAdmision, anioAdmision);
		return resultado;
	}

	/**
	 * Método que revisa si hay alguna evolucion para una cuenta
	 * particular que tenga fecha y hora superior a la dada (La fecha
	 * debe darse en el formato usado en la BD)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta en la que se deseen buscar
	 * las evoluciones
	 * @param fechaDada Fecha en el formato de la BD para la
	 * que se desea buscar evoluciones con fecha superior a esta
	 * @param horaDada Hora para la que se desea buscar evoluciones
	 * con fecha igual a la dada y hora superior a la horaDada
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeEvolucionConFechaSuperiorFormatoBD (Connection con, int idCuenta, String fechaDada, String horaDada) throws SQLException
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.existeEvolucionConFechaSuperior(con, idCuenta, fechaDada, horaDada);
	}

	/**
	 * Método que revisa si hay alguna evolucion para una cuenta
	 * particular que tenga fecha y hora superior a la dada (La fecha
	 * debe darse en el formato usado en la aplicación)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta en la que se deseen buscar
	 * las evoluciones
	 * @param fechaDada Fecha en el formato de la aplicación para la
	 * que se desea buscar evoluciones con fecha superior a esta
	 * @param horaDada Hora para la que se desea buscar evoluciones
	 * con fecha igual a la dada y hora superior a la horaDada
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeEvolucionConFechaSuperiorFormatoAp (Connection con, int idCuenta, String fechaDada, String horaDada) throws SQLException
	{
		//El método de la BD maneja el caso nulo
		if (fechaDada==null)
		{
			return existeEvolucionConFechaSuperiorFormatoBD (con, idCuenta, fechaDada, horaDada);
		}
		else
		{
			return existeEvolucionConFechaSuperiorFormatoBD (con, idCuenta, UtilidadFecha.conversionFormatoFechaABD(fechaDada), horaDada);
		}
	}

	/**
	 * Método que recibe el código de una persona y retorna el nombre
	 * de la misma. Si la persona no existe debe retornar el texto:
	 * "Persona inexistente en el sistema"
	 *
	 * @param con Conexión con la fuente de datos
	 * @param codigoPersona Código de la persona
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerNombrePersona (Connection con, int codigoPersona) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerNombrePersona (con, codigoPersona) ;
	}

	/**
	 * Método que obtiene la máxima fecha y hora de evolución para una cuenta
	 * en particular
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la máxima
	 * fecha y hora de evolución
	 * @return
	 * @throws SQLException
	 */
	public static String[] obtenerMaximaFechaYHoraEvolucion (Connection con, int idCuenta) 
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.obtenerMaximaFechaYHoraEvolucion (con, idCuenta);
	}

	/**
	 * Método que obtiene la máxima fecha y hora de valoración para una cuenta
	 * en particular
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la máxima
	 * fecha y hora de valoración
	 * @return
	 * @throws SQLException
	 */
	public static String[] obtenerMaximaFechaYHoraValoracion (Connection con, int idCuenta) throws SQLException
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.obtenerMaximaFechaYHoraValoracion (con, idCuenta);
	}

	/**
	 * Método que dada una cuenta, obtiene la fecha y hora de la primera valoración
	 * de esta cuenta. Si no las encuentra retorna nulo
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la fecha
	 * y hora de la primera valoración de la misma
	 * @return
	 * @throws SQLException
	 */
	public static String[] obtenerFechaYHoraPrimeraValoracion (Connection con, int idCuenta) throws SQLException
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.obtenerFechaYHoraPrimeraValoracion (con, idCuenta);
	}

	/**
	 * Método que dada una cuenta, obtiene el número de la valoración
	 * inicial de esta cuenta. Si no la encuentra retorna una excepción
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la
	 * primera valoración de la misma
	 * @return
	 * @throws SQLException
	 */
	public static int obtenerNumeroSolicitudPrimeraValoracion (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerNumeroSolicitudPrimeraValoracion (con, idCuenta);
	}
	/**
	 * Método que dice si existen valoraciones para una cuenta
	 * dada
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se van
	 * a buscar valoraciones
	 * @return
	 * @throws SQLException
	 */
	public static boolean tieneValoraciones (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneValoraciones(con, idCuenta);
	}
	/**
	 * Método que dada una conexión y un paciente retorna el código del
	 * centro de costo del tratante de esa persona, para la cuenta activa
	 *
	 * @param con Conexión con la fuente de datos
	 * @param paciente Paciente al cual se le desea conocer el
	 * tratante de su cuenta actual
	 * @return
	 * @throws SQLException
	 */
	public static int getCodigoCentroCostoTratante (Connection con, PersonaBasica paciente, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodigoCentroCostoTratante_noUrgencias(con,paciente.getCodigoCuenta(), institucion);
	}

	public static int getCodigoCentroCostoTratanteMetodoLento (Connection con, int idCuenta, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodigoCentroCostoTratanteMetodoLento(con, idCuenta, institucion);
	}

	/**
	 * Método que busca el código del centro de costo 
	 * para una solicitud determinada 
	 * @param con
	 * @param numeroSolicitud (índice solicitides),
	 * @return cód
	 * @throws SQLException
	 */
	public static int getCodigoCentroCostoSolicitanteXNumeroSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodigoCentroCostoSolicitanteXNumeroSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Método que permite buscar el código de un paciente dada
	 * su identificación
	 *
	 * @param con una conexion abierta con una BD  o null si no
	 * se tiene
	 * @param codigoTipoIdentificacion Código del tipo de
	 * identificación del paciente a buscar
	 * @param numeroIdentificacion Número de identificación
	 * del paciente a buscar
	 * @return
	 * @throws SQLException
	 */
	public static int buscarCodigoPaciente (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion ) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().buscarCodigoPaciente(con, codigoTipoIdentificacion, numeroIdentificacion );
	}

	/**
	 * Método que permite averiguar si una cuenta específica tiene
	 * Egreso (NO Valida Semi-Egreso)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static boolean tieneEgreso_sinManejoSemiEgreso (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneEgreso (con, idCuenta);
	}

	/**
	 * Método que revisa si una cuenta tiene egreso, manejando
	 * todos los posibles casos; para esto revisa que tenga egreso
	 * sin tener en cuenta el caso semiEgreso, después,
	 * en caso de tener semiEgreso revisa si este está completo,
	 * si lo está quiere decir que hay egreso, si no está en un semiEgreso
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static boolean tieneEgreso (Connection con, int idCuenta) throws SQLException
	{

		//Debe tener un egreso y que:
		// o no tenga semiegreso o que lo tenga completo
		//(No -SemiEg ->!resSemiegreso[0]||SemiEg->Comp->semiEggreso[1])

		boolean resSemiEgreso[]=tieneSemiEgreso(con, idCuenta);

		if (UtilidadValidacion.tieneEgreso_sinManejoSemiEgreso(con, idCuenta)
		&& (!resSemiEgreso[0]||resSemiEgreso[1])
		)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Método que permite buscar el código de un paciente dada
	 * su identificación
	 *
	 * @param con una conexion abierta con una BD  o null si no
	 * se tiene
	 * @param codigoTipoIdentificacion Código del tipo de
	 * identificación del paciente a buscar
	 * @param numeroIdentificacion Número de identificación
	 * del paciente a buscar
	 * @return
	 * @throws SQLException
	 */
	public static int getCodigoPersona (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion ) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodigoPersona (con, codigoTipoIdentificacion, numeroIdentificacion );
	}

	/**
	 * Método que permite averiguar para una cuenta y centro de
	 * costo dados, si el centro de costo es adjunto de la cuenta
	 * (obviamente debe ser válido al momento de realizar la
	 * pregunta)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta
	 * @param loginUsuario Código del centro de costo
	 * @return
	 * @throws SQLException
	 */
	public static boolean esAdjuntoCuenta (Connection con, int idCuenta, String loginUsuario) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAdjuntoCuenta (con, idCuenta, loginUsuario);
	}

	/**
	 * Método que revisa si existe una solicitud de transferencia
	 * de manejo (activa por supuesto) para una cuenta dada
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se va
	 * a realizar la revisión
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeSolicitudTransferenciaManejoPrevia(Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeSolicitudTransferenciaManejoPrevia(con, idCuenta) ;
	}

	/**
	 * Método que revisa si existe una solicitud de transferencia
	 * de manejo (activa por supuesto) para una cuenta dada
	 * dirigida al centro de costo especificado, si la hay, devuelve
	 * el número de la solicitud, si no devuelve 0
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se va
	 * a realizar la revisión
	 * @param codigoCentroCosto Código del centro de costo
	 * al que se hizo sol. cambio tratante (futuro "posible" tratante)
	 * @return
	 * @throws SQLException
	 */
	public static int existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (Connection con, int idCuenta, int codigoCentroCosto) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (con, idCuenta, codigoCentroCosto);
	}

	/**
	 * Método que dice si una cuenta tiene como vía de ingreso
	 * consulta externa.
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se va
	 * revisar si tiene como vía de ingreso consulta externa
	 * @return
	 * @throws SQLException
	 */
	public static boolean esViaIngresoConsultaExterna (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esViaIngresoConsultaExterna (con, idCuenta) ;
	}

	/**
	 * Método que dice si un médico perteneciente al grupo adjunto
	 * puede llenar la finalización de la atención o si tiene alguna
	 * solicitud pendiente
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se va
	 * revisar si tiene solicitudes en estados diferentes a interpretar
	 * / anulado
	 * @param codigoCentroCosto Código del centro de costo
	 * para el que se va a revisar si tiene solicitudes en estados
	 * diferentes a interpretar / anulado
	 * @param codigoCuentaAsociada Código de la cuenta asociada
	 * (si existe) 
	 * @return
	 * @throws SQLException
	 */
	public static ResultadoBoolean haySolicitudesIncompletasAdjunto (Connection con, int idCuenta, int codigoCentroCosto, int codigoCuentaAsociada, int institucion) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().haySolicitudesIncompletasAdjunto (con, idCuenta, codigoCentroCosto, codigoCuentaAsociada, institucion);
	}

	/**
	 * Método que dice si existen solicitudes en estados diferentes
	 * a interpretar / anulado en el sistema para una cuenta dada
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que se va
	 * revisar si tiene solicitudes en estados diferentes a interpretar
	 * / anulado
	 * @return
	 * @throws SQLException
	 * @throws IPSException 
	 */
	public static ResultadoBoolean haySolicitudesIncompletasEnCuenta (Connection con, int codigoIngreso, int institucion, boolean esEvolucion, boolean validarMedicamentos) throws SQLException, IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().haySolicitudesIncompletasEnCuenta (con, codigoIngreso, institucion, esEvolucion,validarMedicamentos);
	}

	/**
	* Indica si un servicio es excepcion al convenio de la cuenta.
	* @param	ac_con		Conexión a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @param	ai_servicio	Código del servicio
	* @return	true si el servicio esta paramatrizado como una excepción al convenio de la cuenta.
	*			false de lo contrario
	* @throws	SQLException
	*/
	public static boolean esExcepcionServicio(
		Connection	ac_con,
		int			ai_cuenta,
		int			ai_servicio
	)throws SQLException
	{
		return
			DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")
			).getUtilidadValidacionDao().esExcepcionServicio(ac_con, ai_cuenta, ai_servicio);
	}

	/**
	* Cierra el ingreso del paciente y deja la cuenta en estado excenta
	* @param	ac_con		Conexión a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @return	true Si la cuenta pudo ser cerrada de manera exitosa
	*			false de lo contrario
	* @throws	SQLException
	*/
	public static boolean cerrarCuentaConsultaExterna(
		Connection	ac_con,
		int			ai_cuenta
	)throws SQLException
	{
		return
			DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")
			).getUtilidadValidacionDao().cerrarCuentaConsultaExterna(ac_con, ai_cuenta);
	}
	
	/**
	 * Método que revisa si un paciente tiene o no antecedente
	 * pedíatrico
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código del paciente al que se le
	 * quiere averiguar si tiene antecedentes pedíatricos
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeAntecedentePediatrico(Connection con,int codigoPaciente)throws SQLException{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAntecedentePediatrico(con,codigoPaciente);
	}

	/**
	 * Método que efectua las validaciones correspondientes a un
	 * asocio de cuenta y devuelve un mensaje en código en caso de
	 * error en el campo de texto de una variable ResultadoBoolean
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta sobre la que se desea validar
	 * @param Via Ingreso
	 * @param Tipo Paciente 
	 * @return
	 * @throws SQLException
	 */	
	public static ResultadoBoolean validacionAsocioCuenta (Connection con, int idCuenta, String viaIngreso, String tipoPaciente) throws SQLException
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validacionAsocioCuenta (con, idCuenta,  viaIngreso,  tipoPaciente);
	}
	
	/**
	 * Método que busca el diagnostico a poner en la valoración 
	 * de hospitalización después de un asocio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuentaAsociada Código de la cuenta en la que se realiza
	 * la búsqueda 
	 * @return
	 * @throws SQLException
	 */
	public static String[] obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (Connection con, int idCuentaAsociada,String viaIngresoCuentaVieja) throws SQLException
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (con, idCuentaAsociada,viaIngresoCuentaVieja) ;
	}
	
	/**
	 * Método que retorna el centro de costo dado el login del médico
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoMedico
	 * @return
	 * @throws SQLException
	 */
	public static int getCodigoCentroCostoDadoMedico (Connection con, String loginMedico) throws SQLException
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodigoCentroCostoDadoMedico (con, loginMedico) ;
	}

	/**
	 * Permite verificar que la cuenta tiene un medico adjunto
	 * @param cuenta
	 * @return
	 */
	public static boolean existeManejoConjunto(Connection con, int cuenta, int centroCosto)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeManejoConjunto(con, cuenta, centroCosto);
	}

	/**
	 * Permite verificar que la cuenta tiene manejo conjunto activo de solicitudes de interconsulta
	 * @param cuenta
	 * @return
	 */
	public static boolean existeManejoConjuntoActivoSolInterconsulta(Connection con, int cuenta, int centroCosto)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeManejoConjuntoActivoSolInterconsulta(con, cuenta, centroCosto);
	}
	
	/**
	 * Método que obtiene el valor del flag - centinela (Define si en
	 * ciertas funcionalidades se debe preguntar si desea grabar o
	 * no)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerValorCentinela () throws SQLException
	{
		return "true";
	}
	
	/**
	 * Método que permite encontrar resultados de agenda que se 
	 * encuentren en estado activo y con la unidad de consulta que 
	 * llega de parámetro
	 * @param con
	 * @param unidadConsulta
	 * @param centroAtencion
	 * @return
	 */
	public static boolean existeValoresCancelarAgenda(Connection con, int unidadConsulta,
													String fechaInicial, String fechaFinal, 
													String horaInicial, String horaFinal,
													int consultorio, int dia, int codigoMedico, int centroAtencion,
													String centrosAtencion, String unidadesAgenda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeValoresCancelarAgenda(con, unidadConsulta,fechaInicial,fechaFinal, horaInicial,horaFinal, consultorio, dia, codigoMedico, centroAtencion, centrosAtencion, unidadesAgenda );
	}
	
	/**
	 * Método que permite saber si existe una valoración pediatrica
	 * hospitalaria (método de Consultar Ingreso)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la se quiere 
	 * saber si existe una valoración pediatrica hospitalaria
	 * @return
	 * @throws SQLException
	 */
	public static int tieneValoracionPediatricaHospitalariaConsultarIngreso (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneValoracionPediatricaHospitalariaConsultarIngreso (con, idCuenta) ;
	}
	
	/**
	 * método que permite activar o no la modificación 
	 * del número de identificación de terceros,
	 * únicamente en los casos en los cuales este tercero 
	 * no ha sido utilizado en empresas.
	 * (a futuro se tiene que validar que no se utilice en ningún lado)
	 * @param con, Conexión
	 * @param codigo, Código del tercero
	 * @return
	 */
	public static boolean hanUtilizadoTercero (Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().hanUtilizadoTercero(con,codigo) ;
	}	

	/**
	 * Método para conocer el código de la valoración inicial para una
	 * cuenta especifica, retorna 0 si no existe al solicitud o no la 
	 * valoración no ha sido llenada
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static int getCodigoValoracionInicial (Connection con, int idCuenta) 
	{
		try
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().buscarCodigoValoracionInicial (con, idCuenta) ;
		}
		catch(SQLException e)
		{
			
			return 0;
		}
	}
	
	/**
	 * Adición sebastián
	 * Método para conocer el código de la solicitud de una valoración inicial para una
	 * cuenta especifica, retorna 0 si no existe la solicitud 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 */
	public static int getCodigoSolicitudValoracionInicial (Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().buscarCodigoSolicitudValoracionInicial(con,idCuenta);
	}
	
	/**
	 * Método que obtiene el nombre de un centro de costo
	 * dado su código
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param codigoCentroCostoBuscado Código del centro
	 * de costo
	 * @return
	 * @throws SQLException
	 */
	public static String getNombreCentroCosto (Connection con, int codigoCentroCostoBuscado) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getNombreCentroCosto (con, codigoCentroCostoBuscado);
	}
	
	/**
	 * Método que dado el código de una cuenta devuelve 
	 * la fecha de admisión de la misma, si esta existe, 
	 * si no tiene admisión lanza una excepción
	 * Retorna en formato yyyy-mm-dd 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static String getFechaAdmision (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getFechaAdmision (con, idCuenta) ;
	}
	
	/**
	 * Método que dado el código de una cuenta devuelve 
	 * la fecha de admisión de la misma, si esta existe, 
	 * si no tiene admisión lanza una excepción
	 * Retorna en formato yyyy-mm-dd 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */
	public static String getFechaAdmision (int idCuenta) throws SQLException
	{
		Connection con= UtilidadBD.abrirConexion();
		String fecha=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getFechaAdmision (con, idCuenta) ;
		UtilidadBD.closeConnection(con);
		return fecha;
	}
	
	
	/**
	 * Método que dado el código de una cuenta devuelve 
	 * la hora de admisión de la misma, si esta existe, 
	 * si no tiene admisión lanza una excepción 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static String getHoraAdmision (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getHoraAdmision (con, idCuenta) ;
	}

	/**
	 * Método que dado el código de una cuenta devuelve 
	 * la hora de admisión de la misma, si esta existe, 
	 * si no tiene admisión lanza una excepción 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */
	public static String getHoraAdmision (int idCuenta) throws SQLException
	{	
		Connection con= UtilidadBD.abrirConexion();
		String hora= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getHoraAdmision (con, idCuenta) ;
		UtilidadBD.closeConnection(con);
		return hora;
	}
	
	
	/**
	 * Método que revisa si se debe mostrar el mensaje 
	 * de la cancelación del tratante antes de llenar 
	 * una evolución 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @param centroCostoMedico Centro de costo del
	 * médico
	 * @return
	 * @throws SQLException
	 */
	public static boolean deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (Connection con, int idCuenta, int centroCostoMedico) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (con, idCuenta, centroCostoMedico) ;
	}
	
	/**
	 * Verificar si el paciente (codigoPaciente) tiene citas atendidas por el médico (codigoMedico)
	 * @param con
	 * @param codigoMedico
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean tieneCitaRespondida(Connection con, int codigoMedico, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneCitaRespondida(con, codigoMedico, codigoPaciente);
	}
	
	/**
	 * Método para verificar la existencia de cuenta
	 * asociada
	 * @param con
	 * @param ingreso
	 * @return codigo de la cuenta asociada si existe, 0 de lo contrario
	 */
	public static int tieneCuentaAsociada(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneCuentaAsociada(con, ingreso);
	}
	
	/**
	 * método que dado su acronimo de metodo de ajuste y su valor, estonces lo aproxima
	 * a la centena, decena o unidad
	 * @param acronimoMetodoAjuste
	 * @param valor
	 * @return
	 */
	public static double aproximarMetodoAjuste(String acronimoMetodoAjuste, double valor) throws IPSException
	{
		
		
		String temp= UtilidadTexto.formatearExponenciales(valor), segundoCorteValor="", primerCorteValor="";
		int indexCortar=0;
		
		try	{
		
			for(int i=0; i<temp.length(); i++)
			{
			   if(temp.charAt(i)=='.')
			   {
					indexCortar=i	;
				}
			}
			
			//caso en el cual el número no tieene expresiones decimales
			if(indexCortar==0)
				indexCortar = temp.length();
				
	
			/*
			 * FIXME Método de Ajuste
			 * 
			 * Nota: temporalmente se hace el siguiente arreglo, ya que al hacer el desarrollo
			 * de los convenios no tuvieron en cuenta que los métodos de ajuste estan
			 * definidos en la tabla facturacion.metodos_ajuste y como no se hace referencia
			 * a esta tabla, no utilizan los mismos acrónimos.
			 * 
			 * Los definidos para el convenio son (ConstantesIntegridadDominio):
			 * 
			 * acronimoAjusteCentena = "AJCE"
			 * acronimoAjusteDecena = "AJDE"
			 * acronimoAjusteUnidad = "AJUN"
			 * acronimoSinAjuste = "SIAJ"
			 * 
			 * Los definidos en la tabla facturacion.metodos_ajuste (ConstantesBD):
			 * 
			 * metodoAjusteDecena = "D"
			 * metodoAjusteCentena = "C"
			 * metodoAjusteUnidad = "U"
			 * metodoSinAjuste = "S"
			 * 
			 */
			
			
			if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteUnidad) || 
			   acronimoMetodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteUnidad))
			{
				//caso especial acordado con el grupo calidad ya que el valor no puede ser cero al hacerle ele ajuste
				//se modifica por tarea 82798
				if(valor<0.5)
					return 0;
				else
				{	
					primerCorteValor= temp.substring(0,indexCortar);
					segundoCorteValor= "0"+temp.substring(indexCortar, temp.length());
						
					if((Float.parseFloat(segundoCorteValor))>=0.5)
						return (Double.parseDouble(primerCorteValor)+1);
					else
						return (Double.parseDouble(primerCorteValor));
				}	
			}
			else if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteDecena)|| 
					 acronimoMetodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteDecena))
			{
				//caso especial acordado con el grupo calidad ya que el valor no puede ser cero al hacerle ele ajuste
				if(valor<5)
					return 10;
				else
				{	
					primerCorteValor= temp.substring(0,indexCortar-1);
					segundoCorteValor= temp.substring(indexCortar-1, temp.length());
						
					if((Float.parseFloat(segundoCorteValor))>=5)
						return (Double.parseDouble(primerCorteValor+"0")+10);
					else
						return (Double.parseDouble(primerCorteValor+"0"));
				}
			}
			else if (acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteCentena) || 
					   acronimoMetodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteCentena))
			{
				//caso especial acordado con el grupo calidad ya que el valor no puede ser cero al hacerle ele ajuste
				if(valor<50)
					return 100;
				else
				{
					
					primerCorteValor= temp.substring(0,indexCortar-2);
					segundoCorteValor= temp.substring(indexCortar-2, temp.length());
						
					if((Float.parseFloat(segundoCorteValor))>=50)
						return (Double.parseDouble(primerCorteValor+"00")+100);
					else
						return (Double.parseDouble(primerCorteValor+"00"));
				}
			}
		}
		catch (NumberFormatException ne){
			return valor;
		}	
		catch (NullPointerException e) {
		   return valor;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return valor;
	}
	
	/**
	 * Método que indica si un double es mayor a el número de decimales indicados
	 * @param valor, double
	 * @return boolean
	 */
	public static boolean esMayorNdecimales(double valor, int numDecimales)
	{
		String temp= valor+"";
		int j=0;
		for(int i=0; i<temp.length(); i++)
		{
			if(temp.charAt(i)=='.')
			{
				j=temp.length()-i-1;
			}
		}
		if(j>numDecimales)
		return true;
		else
		return false;
	}
	/**
	 * Método que indica si un double es mayor a el número de decimales indicados
	 * @param valor, String
	 * @return boolean
	 */
	public static boolean esMayorNdecimales(String valor, int numDecimales)
	{
		String temp= valor;
		int j=0;
		for(int i=0; i<temp.length(); i++)
		{
			if(temp.charAt(i)=='.')
			{
				j=temp.length()-i-1;
			}
		}
		if(j>numDecimales)
		return true;
		else
		return false;
	}
	
	/**
	 * Método que indica si un double es mayor a el número de digitos indicados
	 * @param valor, double
	 * @return boolean
	 */
	public static boolean esMayorNdigitos(double valor, int numDigitos)
	{
		String temp= valor+"";
		if(temp.indexOf('.')>numDigitos)
			return true;
		return false;
	}
	/**
	 * Método que indica si un double es mayor a el número de digitos indicados
	 * @param valor, String
	 * @return boolean
	 */
	public static boolean esMayorNdigitos(String valor, int numDigitos)
	{
		String temp= valor;
		if(temp.indexOf('.')>=0)
		{
			if(temp.indexOf('.')>numDigitos)
				return true;
			else
				return false;
		}
		else
		{
			if(temp.length()>numDigitos)
				return true;
			return false;
		}
	}

	
	/**
	 * metodo que valida el parametro general "centro de costo de urgencias" 
	 * @param con  una conexion con la base de datos
	 * @param medico el medico que desea tratar
	 * @param paciente el paciente cargado en el sistema
	 * @return
	 */
	public static RespuestaValidacion esValidoCCMedicoCCUrgencias(Connection con, UsuarioBasico medico, PersonaBasica paciente)
	{
		// comprobamos que la via de ingreso del paciente sea "Urgencias" con el fin de 
		// hacer las validaciones respectivas, de lo contrario no es necesario
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			// validamos que el usuario sea medico
			if (!esMedico(medico).equals(""))
			{
				//s everifica si el error no tiene que ver con no haber definido
				//la ocupación médico especialista y general en parámetros generales
				if(esMedico(medico).equals("errors.noOcupacionMedica"))
					return new RespuestaValidacion ("Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales",false);
				return new RespuestaValidacion ("El usuario no es medico", false);
			}
		
			ValoresPorDefecto.cargarValoresIniciales(con);
			
			if(medico.getCodigoCentroCosto()!=Integer.parseInt(ValoresPorDefecto.getCentroCostoUrgencias(medico.getCodigoInstitucionInt())))
			{
				return new RespuestaValidacion ("El medico no pertenece al centro de costo urgencias, por lo tanto no puede acceder a esta opcion.", false);
			}
		}
		
		return new RespuestaValidacion ("", false);
	}


	
	/**
	 * metodo que valida el parametro general "centro de costo de urgencias" con el medico sin necesidad de paciente cargado 
	 * @param con  una conexion con la base de datos
	 * @param medico el medico que desea tratar
	 * @return
	 */
	public static RespuestaValidacion esValidoCCMedicoCCUrgencias2(Connection con, UsuarioBasico medico)
	{
			// validamos que el usuario sea medico
			if (!esMedico(medico).equals(""))
			{
				//se verifica si se definió la ocupación medico general y especialista
				//en parámetros generales
				if(esMedico(medico).equals("errors.noOcupacionMedica"))
					return new RespuestaValidacion ("Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales",false);
				else
					return new RespuestaValidacion ("El usuario no es medico", false);
			}
		
			ValoresPorDefecto.cargarValoresIniciales(con);
		
			if(medico.getCodigoCentroCosto()!=Integer.parseInt(ValoresPorDefecto.getCentroCostoUrgencias(medico.getCodigoInstitucionInt()))){
				return new RespuestaValidacion ("El medico no pertenece al centro de costo urgencias, por lo tanto no puede acceder a esta opcion.", false);
			}
	
  		return new RespuestaValidacion ("", true);
	}
	
	/**
	 * Método para revisar si una cuenta o subcuenta
	 * tiene un estado definido
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @param estadoAVerificar Código del estado a
	 * verificar
	 * @param esSubCuenta Boolean que indica si es
	 * una subcuenta o no
	 * @return
	 * @throws SQLException
	 */
	public static boolean cuentaOSubcuentaTieneEstadoDefinido (Connection con, int idCuenta, int estadoAVerificar, boolean esSubcuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cuentaOSubcuentaTieneEstadoDefinido (con, idCuenta, estadoAVerificar,esSubcuenta) ;
	}
	
	/**
	 * Método que dado el código de una cuenta o
	 * subcuenta devuelve el nombre de su estado 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param id Código de la cuenta o subcuenta
	 * @param esSubCuenta Boolean que indica si es
	 * una subcuenta o no
	 * @return
	 * @throws SQLException
	 */
	public static String getNombreEstadoCuentaOSubcuenta(Connection con, int id, boolean esSubCuenta)throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getNombreEstadoCuentaOSubcuenta(con, id, esSubCuenta);
	}
		
	/**
	 * Método para validar si una cadena se encuentra en un rango de caracteres
	 * Los simbolos especiales como " ' ", " \ " ó " -" se deben enviar individualmente
	 * sin tenerlos en cuenta entre rangos
	 * Ej: [a-z,A-Z,1-3,-]
	 * @param cadena
	 * @param rango
	 * @return true si la cadena es valida en el rango dado, false
	 * si la cadena no es válida en el rango ó si el rango tiene un formato invalido
	 */
	public static boolean validarRangos(String cadena, String rango)
	{
		try
		{
			// Primero separo los rangos pasados por parámertos (No tengo en cuenta los [])
			String[] rangos=rango.substring(rango.indexOf("[")+1, rango.indexOf("]")).split(",");
			// Recorro todos los rangos
			for(int i=0;i<rangos.length;i++)
			{
				String cadenaTempo="";
				if(rangos[i].length()>1)
				{
					if(rangos[i].charAt(1)=='-')
					{
						// Tomo los límites de los rangos
						char inf=rangos[i].charAt(0);
						char sup=rangos[i].charAt(2);
						// Hago la validación de que el rango inferior efectivamente sea menor que el rango superior
						if(sup<=inf)
						{
							return false;
						}
						/*
						 * Hago un recorrido de la cadena y
						 * genero una nueva con los caracteres no incluidos en el rango actual
						 */
			 			for(int j=0;j<cadena.length();j++)
						{
			 				/*
			 				 * Valido que el caracter se encuentre en el rango
			 				 */
							if(!(cadena.charAt(j)>=inf && cadena.charAt(j)<=sup))
							{
								cadenaTempo+=cadena.charAt(j);
							}
						}
					}
					else
					{
						return false;
					}
				}
				/*
				 * En este caso no se valida contra un
				 * rango sino contra un caracter específico
				 */
				else
				{
					if(rangos[i].length()==1)
					{
						char car=rangos[i].charAt(0);
						/*
						 * Hago un recorrido de la cadena y
						 * genero una nueva con los caracteres que no
						 * concuerdan con el carcter validado
						 */
						for(int j=0;j<cadena.length();j++)
						{
							if(cadena.charAt(j)!=car)
							{
								cadenaTempo+=cadena.charAt(j);
							}
						}
					}
				}
				// Asigno la cadena con el resultado del filtado de caracteres
				cadena=cadenaTempo;

			}
			/*
			 * En este punto ya se hizo el recorrido completo de
			 * rangos, y ya se eliminaron los caracteres incluidos en ellos,
			 * de tal manera que si la cadena tiene algun caracter
			 * es por que no se encontraba incluida en los rangos dados.
			 */
			if(cadena.equals(""))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Método para validar el número de autorización
	 * @param numeroAutorizacion
	 * @return true o false de acuerdo a la requerimiento de validación en Admision - Anexo 03
	 */
	public static boolean validarNumeroAutorizacion(String numeroAutorizacion)
	{
		if(numeroAutorizacion.length()>ConstantesBD.numCaracteresNumeroAutorizacion)
		{
			return false;
		}
		else
		{
			return validarRangos(numeroAutorizacion, "[0-9,a-z,A-Z,-]");			
		}
	}

	/**
	 * Método para validar el número de autorización el cual adiciona
	 * instantaneamente el error en el ActionErrors
	 * @param errores ActionErrors con los errores de toda la validación del form
	 * @param numeroAutorizacion
	 * @return true o false de acuerdo a la requerimiento de validación en Admision - Anexo 03
	 */
	public static boolean validarNumeroAutorizacion(ActionErrors errores, String numeroAutorizacion)
	{
		if(numeroAutorizacion.length()>ConstantesBD.numCaracteresNumeroAutorizacion)
		{
			errores.add("errorLargoNumeroAutorizacion", new ActionMessage("prompt.numeroAutorizacion.maskmsg"));
			return false;
		}
		else
		{
			if(!validarRangos(numeroAutorizacion, "[0-9,a-z,A-Z,-]"))
			{
				errores.add("errorLargoNumeroAutorizacion", new ActionMessage("prompt.numeroAutorizacion.maskmsg"));
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * Metodo que me indica si una cuenta tiene un estado determinado
	 * @param con, Conexion
	 * @param id, id de la cuenta
	 * @return true si es asociada-
	 */
	public static boolean igualEstadoCuenta(Connection con,int id,int estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().igualEstadoCuenta(con,id,estado);
	}
	
	/**
	 * Método que dice si una cuenta es de urgencias o no
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta para la que
	 * se desea saber si es de urgencias o no
	 * @return
	 * @throws SQLException
	 */
	public static boolean   esCuentaUrgencias (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCuentaUrgencias (con, idCuenta) ;
	}
	
	/**
	 * Método que indica si la solicitud pasada por parámetros fue originada
	 * en la cuenta pasada por parámetros
	 * La comparación se hace por tiempo
	 * @param con
	 * @param numeroSolicitud
	 * @param idCuenta
	 * @return true si la solicitud pertenece a la cuenta, false de lo contrario
	 */
	public static boolean perteneceSolicitudACuentaComparacionXTiempo(Connection con, int numeroSolicitud, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().perteneceSolicitudACuentaComparacionXTiempo(con, numeroSolicitud, idCuenta);
	}

	/**
	 * Método para verificar si el paciente tiene o no evoluciones
	 * @param con Conexión con la BD
	 * @param idCuenta Codigo de la cuenta
	 * @return true si el paciente tiene evoluciones, false de lo contrario
	 */
	public static boolean tieneEvoluciones(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneEvoluciones(con,idCuenta);		
	}

	/**
	 * Método para validar que la cuenta dada no se encuentre en proceso de facturación
	 * No valida los usuarios si se envia un nulo ene el login
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente
	 * @param idSesionOPCIONAL, si viene cargado eso quiere decir que no vamos a filtrar por ese numero de sesion
	 * @return true si la cuenta está en proceso de facturación por otro usuario, false de lo contrario
	 */
	public static boolean estaEnProcesofacturacion(Connection con, int codigoPaciente, String idSesionOPCIONAL)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estaEnProcesofacturacion(con, codigoPaciente, idSesionOPCIONAL);
	}
	
	/**
	 * Método para validar que la cuenta dada no se encuentre en proceso de facturación
	 * No valida los usuarios si se envia un nulo ene el login
	 * @param codigoPaciente Código del paciente
	 * @param idSesionOPCIONAL, si viene cargado eso quiere decir que no vamos a filtrar por ese numero de sesion
	 * @return true si la cuenta está en proceso de facturación por otro usuario, false de lo contrario
	 */
	public static boolean estaEnProcesofacturacion(int codigoPaciente, String idSesionOPCIONAL)
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estaEnProcesofacturacion(con, codigoPaciente, idSesionOPCIONAL);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * Método que verifica si la tarifa del artículo ya existe
	 * @param con Conexión con la BD
	 * @param articulo Código del articulo
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public static boolean existeTarifaParaArticulo(Connection con, int articulo, int esquemaTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeTarifaParaArticulo(con, articulo, esquemaTarifario);
	}
	

	/**
	 * Metodo que retorna true si el articulo es un medicamento(independiente de que sea pos o no)
	 * @param articulo
	 * @return boolean, respuesta true indica si es medicamento.
	 */
	public static boolean esMedicamento(int articulo) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esMedicamento(con,articulo);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * Metodo que retorna true si el articulo es un medicamento(independiente de que sea pos o no)
	 * @param conexion
	 * @param articulo
	 * @return boolean, respuesta true indica si es medicamento.
	 */
	public static boolean esMedicamento(Connection con,int articulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esMedicamento(con,articulo);
	}

	/**
	 * Metodo que retorna true si existe un tipo regimen y naturaleza
	 * @param con
	 * @param codigoRegimen
	 * @param codigoNaturaleza
	 * @return
	 */
	public static boolean existeExcNatuRegimen(Connection con, String codigoRegimen, int codigoNaturaleza)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeExcNatuRegimen(con,codigoRegimen,codigoNaturaleza);
	}

	/**
	 * Metodo que retorna un boolean indicanto si el diagnostico con ese tipo CIE ya existe en el sitema.
	 * @param tipoCie Tipo de Cie
	 * @param codigo Codigo del diagnostico
	 * @return true si existe
	 */
	public static boolean diagnosticoExistente(String codigo, int tipoCie) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().diagnosticoExistente(con,codigo,tipoCie);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * Método para cargar el tipo de diagnóstico principal de la última evolución
	 * en caso de no existir evoluciones lo toma de la valoracioçón inicial
	 * @param con
	 * @param codigoCuenta
	 * @param revisarEnEvolucion
	 * Retorna el código del tipo de diagnóstico
	 */
	public static int obtenerTipoDiagnosticoPrincipal(Connection con, int codigoCuenta, boolean revisarEnEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerTipoDiagnosticoPrincipal(con, codigoCuenta, revisarEnEvolucion);
	}
	
	/**
	 * Método para consultar si un día es festivo o no
	 * @param con Conexión con la BD
	 * @param fecha Fecha que se desea consultar
	 * @param incluirDomingos Tener en cuenta cómo dias festivos los domingos 
	 * @return true si es día festivo
	 */
	public static boolean esDiaFestivo(Connection con, String fecha, boolean incluirDomingos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esDiaFestivo(con, fecha, incluirDomingos);
	}
	
	/**
	 * Metodo que consulta una funcionalidad particular la cual no tenga una entrada en
	 * dependencias funcionalidades, entonces se filtra segun el rol y el codigo de la 
	 * funcionalidad
	 * @param con
	 * @param login
	 * @param codigoFuncionalidad
	 * @return devuelve el nombre de la funcionalidad con su path 
	 */
	public static String funcionalidadADibujarNoEntradaDependencias(Connection con, String login, int codigoFuncionalidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().funcionalidadADibujarNoEntradaDependencias(con, login, codigoFuncionalidad);
	}
	
	/**
	 * Metodo que determina si un usuario puede interpretar una solicitud de interconsulta o procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCostoUsuario
	 * @return
	 */
	public static boolean puedoInterpretarSolicitudInterconsultaProcedimiento(Connection con, int numeroSolicitud, UsuarioBasico usuario)
	{
		if(esMedico(usuario).equals(""))
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoInterpretarSolicitudInterconsultaProcedimiento(con, numeroSolicitud, usuario.getCodigoCentroCosto());
		}
		else
		{
			return false;
		}
	}
	/**
	 *Metodo que retorna el manejo en que se encuentra una solicitud específica
	 *retornta true si la solicitud se encuentra en manejo conjunto
	 */
	public static boolean isManejoConjunto(Connection con,int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().isManejoConjunto(con,numeroSolicitud);
	}

	/**
	 * Metodo que dado el centro de costo de un usuario, y el numero de solicitud me dice si la puedo modificar o no.
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCosto
	 * @return
	 */
	public static boolean medicoPuedeResponderSolicitud(Connection con, int numeroSolicitud, int codigoCentroCosto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().medicoPuedeResponderSolicitud(con,numeroSolicitud,codigoCentroCosto);
	}
	
	/**
	 * Metodo que indica si una solicitud de interconsulta tiene manejo conjunto finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudManejoConjuntoFinalizado(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esSolicitudManejoConjuntoFinalizado(con, numeroSolicitud);
	}

	/**
	 * Metodo que premite obtener el id de un ingreso dada la cuenta.
	 * @param con
	 * @param idCuenta
	 */
	public static int obtenerIngreso(Connection con, int idCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerIngreso(con,idCuenta);
	}

	
	/**
	 * Método que verifica si la tarifa del servicio iss ya existe
	 * @param con Conexión con la BD
	 * @param servicio Código del servicio
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public static boolean existeTarifaISSParaServicio(Connection con, int servicio, int esquemaTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeTarifaISSParaServicio(con, servicio, esquemaTarifario);
	}
	
	/**
	 * Método que verifica si la tarifa del servicio soat ya existe
	 * @param con Conexión con la BD
	 * @param servicio Código del servicio
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public static boolean existeTarifaSOATParaServicio(Connection con, int servicio, int esquemaTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeTarifaSOATParaServicio(con, servicio, esquemaTarifario);
	}

	/**
	 * metodo que verifia si ya se gener cierre de saldos iniciales en una institcion.
	 * el metodo, abre la conexion.
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 */
	public static boolean existeCierreSaldosIniciales( int institucion) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCierreSalodosIniciales(con,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * metodo que verifica la existencia de cierre saldos iniciales capitacion
	 * @param institucion
	 * @return
	 */
	public static boolean existeCierreSaldosInicialesCapitacion(int institucion)
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCierreSaldosInicialesCapitacion(con,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Método que verifica si el paciente tiene antecedentes gineco-obstetricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public static boolean existeAntecedenteGinecoObstetrico(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAntecedenteGinecoobstetrico(con, codigoPersona);
	}
	
	/**
	 * Método que verifica si el paciente tiene información histórica de antecedentes gineco-obstétricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public static boolean existeAntecedenteGinecoHisto(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAntecedenteGinecoHisto(con, codigoPersona);
	}

	/**
	 * @param con
	 * @param codigoPersona
	 * @param loginUsuario
	 * @return
	 */
	public static boolean estaEnProcesoDistribucion(Connection con, int codigoPersona, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estaEnProcesoDistribucion(con, codigoPersona, loginUsuario);
	}
	
	/**
	 * Metodo que devuelve el numero de autorización dado el codigo de institucion y un like por 'autorizaci'
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	/*
	public static int getCodNumeroAutorizacionAtributosSolicitud (Connection con, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodNumeroAutorizacionAtributosSolicitud(con, codigoInstitucion);
	}
	*/
	/**
	 * Metodo para obtener la fecha de apertura de una cuenta
	 * @param con Connection
	 * @param idCuenta int
	 * @return fecha String
	 */
	public static String obtenerFechaAperturaCuenta (Connection con, int idCuenta) 
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.obtenerFechaAperturaCuenta(con, idCuenta);
	}

	/**
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static boolean existeCuentaCobro(double cuentaCobro, int institucion) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCuentaCobro(con,cuentaCobro,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Metodo que verifica si ya se ejecuto un cierre mensual.
	 * @param mes.
	 * @param anio.
	 * @param Institucion
	 * @return true si tiene cierre, false de lo contrario.
	 */
	public static boolean existeCierreMensual(int mes, int anio, int institucion) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCierreMensual(con,mes,anio,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * Metodo que me indica si una fecha es menor igual a la fecha del cierre.
	 * la fecha debe esta en formato aplicacion(dd/mm/yyyy).
	 * @param Fecha
	 * @param Intitucion
	 * @return true si la fecha es menor igual a la fecha del sistema, false de lo contrario
	 * @author artotor
	 */
	public static boolean fechaMenorIgualAFechaCierreSalodsIniciales(String fecha, int institucion) 
	{
		boolean respuesta=false;
		String[] fechaVector = fecha.split("/", 3);
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().fechaMenorIgualAFechaCierreSalodsIniciales(con,Integer.parseInt(fechaVector[1]),Integer.parseInt(fechaVector[2]),institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Método que indica si la cama ha sido o no ocupada en el rango de fecha y hora igual o mayor al entregado 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoAxiomaCama
	 * @return
	 */
	public static  boolean esCamaOcupadaRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoAxiomaCama)
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.esCamaOcupadaRangoFechaHoraMayorIgualDado(con, fecha, hora, codigoAxiomaCama);
	}
	
	/**
	 * Método que me indica si el paciente tiene un traslado posterior a la fecha - hora de uno nuevo
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoPaciente) 
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(con, fecha, hora, codigoPaciente);
	}
	
	/**
	 * Método que verifica si una cuenta es de hospitalización
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @param estado de la cuenta
	 * @param diferente (true/false) que indica si se valida con
	 * el estado igual a diferente
	 * @param institucion
	 * @return
	 */
	public static boolean esCuentaHospitalizacion(Connection con,int idCuenta,int estado,boolean diferente,int institucion)
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.esCuentaHospitalizacion(con,idCuenta,estado,diferente,institucion);
	}

	/**
	 * Método que verifica si una cuenta es de hospitalización
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean esCuentaHospitalizacion(Connection con,String idCuenta)
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.esCuentaHospitalizacion(con,idCuenta);
	}
	
	/**
	 * @return
	 */
	public static boolean cuentaCobroAjustesPendientes(double cuentaCobro,int institucion) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cuentaCobroAjustesPendientes(con,cuentaCobro,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * @param d
	 * @param i
	 * @return
	 */
	public static boolean cuentaCobroConFactEnAjustesPendiente(double cuentaCobro,int institucion) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cuentaCobroConFactEnAjustesPendiente(con,cuentaCobro,institucion);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * metodo para obtener el estado de un ajuste especifico
	 * @param con Connection
	 * @param codigo int, código del ajuste
	 * @param institucion int, código de la institución
	 * @return String, con el estado del ajuste y la descripcion
	 * separados por un @
	 * @author jarloc
	 */
	public static String obtenerEstadoAjusteCartera(Connection con, double codigo,int institucion)
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.obtenerEstadoAjusteCartera(con,codigo,institucion);  
	}

	
	/**
	 * metodo para obtener el estado de un ajuste especifico
	 * El abre la conexion
	 * @param codigo int, código del ajuste
	 * @param institucion int, código de la institución
	 * @return String, con el estado del ajuste y la descripcion
	 * separados por un @
	 */
	public static String obtenerEstadoAjusteCartera(double codigo,int institucion)
	{
		String respuesta="";
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
			respuesta=utilidadValidacionDao.obtenerEstadoAjusteCartera(con,codigo,institucion);  
			UtilidadBD.closeConnection(con);
			return respuesta;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * Metodo que retorn true su un ajuste ya fue reversado
	 * @param codigo de aluste
	 * @return
	 */
	public static boolean esAjusteReversado(double codigoAjuste) 
	{
		boolean respuesta=true;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
			respuesta=utilidadValidacionDao.esAjusteReversado(con,codigoAjuste);  
			UtilidadBD.closeConnection(con);
			return respuesta;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * MEtodo que retorna true en caso de que un ajuste tenga entradas en la tabla ajus_fact_empresa
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean existeDistribucionAjusteNivelFacturas(double codigoAjuste) 
	{
		boolean respuesta=true;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
			respuesta=utilidadValidacionDao.existeDistribucionAjusteNivelFacturas(con,codigoAjuste);  
			UtilidadBD.closeConnection(con);
			return respuesta;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * @param codigoAjuste
	 * @param factura
	 * @return
	 */
	public static boolean existeDistribucionAjusteNivelServicios(double codigoAjuste, int factura) 
	{
		boolean respuesta=true;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
			respuesta=utilidadValidacionDao.existeDistribucionAjusteNivelServicios(con,codigoAjuste,factura);  
			UtilidadBD.closeConnection(con);
			return respuesta;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Método para verificar si un centro de costo es SubAlmacen
	 * @param con Conexión con la BD
	 * @param centroCosto codigo del centro de costo a verificar
	 * @return true si el centro de costo es subalmacen
	 */
	public static boolean esCentroCostoSubalmacen(Connection con, int centroCosto)
	{
		UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
		return utilidadValidacionDao.esCentroCostoSubalmacen(con, centroCosto);
	}
	
	/**
	 * 
	 * @param centroCosto
	 * @return
	 */
	public static boolean esCentroCostoSubalmacen(int centroCosto) 
	{
		boolean respuesta=true;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=esCentroCostoSubalmacen(con,centroCosto);  
			UtilidadBD.closeConnection(con);
			return respuesta;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Utilidad que verifica si una solicitud tiene artículos sin cantidad
	 * @param con Conexión con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene artículos sin cantidad, false de lo contrario
	 */
	public static boolean esSolicitudSinCantidad(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esSolicitudSinCantidad(con, numeroSolicitud);
	}
	
	/**
	 * Metodo que indica si ya existe la parametrizacion de tipos de asocios
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean esTiposAsociosParametrizados(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTiposAsociosParametrizados(con, institucion);
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static boolean cajaUtilizadaEnCajerosCaja(Connection con, int consecutivoCaja) 
	{
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cajaUtilizadaEnCajerosCaja(con, consecutivoCaja);
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static boolean cajaUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja) 
	{
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cajaUtilizadaEnRecibosCaja(con, consecutivoCaja);
	}

	/**
	 * @param con
	 * @param int1
	 * @return
	 */
	public static boolean entidadFinanceraUtilizadaEnTarjetasFinancieras(Connection con, int consecutivoEntdadFinanciera) 
	{
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().entidadFinanceraUtilizadaEnTarjetasFinancieras(con,consecutivoEntdadFinanciera);
	}

	/**
	 * @param con
	 * @param int1
	 * @param string
	 * @return
	 */
	public static boolean cajaCajeroUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja, String loginUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cajaCajeroUtilizadaEnRecibosCaja(con,consecutivoCaja,loginUsuario);
	}
	
	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @return
	 */
	public static boolean cajaCajeroConTurno(Connection con, int consecutivoCaja, String loginUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cajaCajeroConTurno(con,consecutivoCaja,loginUsuario);
	}

	/**
	 * @param con
	 * @param int1
	 * @return
	 */
	public static boolean terjetaFinancieraUtilizadaEnMovimientosTarjetas(Connection con, int consecutivoTarjeta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().terjetaFinancieraUtilizadaEnMovimientosTarjetas(con,consecutivoTarjeta);
	}

	/**
	 * @param con
	 * @param int1
	 * @return
	 */
	public static boolean formaPagoUtilizadaEnRecibosCaja(Connection con, int formaPago) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().formaPagoUtilizadaEnRecibosCaja(con,formaPago);
	}

	
	/**
	 * @param string
	 * @param i
	 * @return
	 */
	public static String existeReciboCaja(String numeroReciboCaja, int institucion,int codigoCentroAtencion)
	{
		String respuesta=ConstantesBD.codigoNuncaValido+"";
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			UtilidadValidacionDao utilidadValidacionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao();
			respuesta=utilidadValidacionDao.existeReciboCaja(con,numeroReciboCaja,institucion,codigoCentroAtencion);  
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Dice si esta parametrizado la cobertura de los accidentees de transito
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean existeParametrizacionCobreturaAccTransito(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeParametrizacionCobreturaAccTransito(con,institucion);
	}
	
	/**
	 * Dice si esta parametrizado el salario minimo
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean existeParametrizacionSalarioMinimo(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeParametrizacionSalarioMinimo(con);
	}
	
	/**
	 * Método que indica si una cuenta o subcuenta tiene requisistos paciente por cuenta o subcuenta(distibucion) pendientes
	 * de entregar.
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucion
	 * @param codigoSubcuentaOCuenta
	 * @param hayDistribucion
	 * @return
	 */
	public static boolean faltanRequisitosPacienteXCuenta( Connection con,  String codigoSubcuentaOCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().faltanRequisitosPacienteXCuenta(con, codigoSubcuentaOCuenta);		
	}
	
	/**
	 * Método usado para verificar si un médico se encuentra inactivo
	 * 
	 * @param con
	 * @param codigoMedico
	 * @param institucion
	 * @return
	 */
	public static boolean estaMedicoInactivo(Connection con,int codigoMedico,int institucion)
	{
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estaMedicoInactivo(con,codigoMedico,institucion);
	}
	
	/**
	 * metodo que indica si un servicio tiene o no excepcion 
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public static boolean esServicioConExcepcion(Connection con, String codigoConvenioStr, String codigoServicioStr)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServicioConExcepcion(con, codigoConvenioStr, codigoServicioStr);
	}
	
	/**
	 * metodo que indica si un servicio es pos o no
	 * @param con
	 * @param codigoServicioStr
	 * @return
	 */
	public static boolean esServicioPos(Connection con, String codigoServicioStr)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServicioPos(con, codigoServicioStr);
	}
	
	/**
	 * metodo que indica si un servicio esta cubierto por el convenio
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public static boolean esServicioCubiertoXConvenio(Connection con, String codigoConvenioStr, String codigoServicioStr)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServicioCubiertoXConvenio(con, codigoConvenioStr, codigoServicioStr);
	}
	
	/**
	 * Metodo que verifica la existencia de los pagos parciales realizados por un paciente de una factura
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEstadoPago
	 * @param codigoAxiomaFactura
	 * @return
	 */
	public static boolean existenPagosParcialesFacturaPaciente(Connection con,  int codigoInstitucion, int codigoEstadoPago, String codigoAxiomaFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existenPagosParcialesFacturaPaciente(con, codigoInstitucion, codigoEstadoPago, codigoAxiomaFactura);
	}
	
	/**
	 * Metodo que verifica la existencia de los pagos parciales realizados por un paciente de una factura
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEstadoPago
	 * @param codigoAxiomaFactura
	 * @return
	 */
	public static boolean existenPagosParcialesFacturaPaciente(int codigoInstitucion, int codigoEstadoPago, String codigoAxiomaFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean returna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existenPagosParcialesFacturaPaciente(con, codigoInstitucion, codigoEstadoPago, codigoAxiomaFactura);
		UtilidadBD.closeConnection(con);
		return returna;
	}
	
	
	/**
	 * Método para saber si un médico es de una especialidad específica
	 * @param con
	 * @param codigoMedico
	 * @param codigoEspecialidad
	 * @return true si el médico es de la especialidad mandada como parámetro sino retorna false 
	 */
	public static boolean esMedicoEspecialidad (Connection con, int codigoMedico, int codigoEspecialidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esMedicoEspecialidad(con, codigoMedico, codigoEspecialidad);
	}

	/**
	 * Método que me indica si una peticion yçtiene asociada una solicitud 
	 * @param con Conexión con la base de Datos
	 * @param codigoPeticion Petición a evaluar
	 * @return int -1 En caso de error, 0 en caso de no existir orden asociada, o el número de la orden asociada a la petición
	 */
	public static int[] estaPeticionAsociada(Connection con, int codigoPeticion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estaPeticionAsociada(con, codigoPeticion);
	}
	
	/**
	 * metodo que indica si una hoja qx ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esHojaQxAsociadaSolicitud(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esHojaQxAsociadaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * metodo que indica si una hoja anestesia ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esHojaAnestesiaAsociadaSolicitud(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esHojaAnestesiaAsociadaSolicitud(con, numeroSolicitud);
	}

	/**
	 * metodo que indica si una nota recuperacion ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esNotaRecuperacionAsociadaSolicitud(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esNotaRecuperacionAsociadaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * metodo que indica si una nota general enfermeria ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esNotaGeneralEnfAsociadaSolicitud(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esNotaGeneralEnfAsociadaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * metodo que indica si un consumo materiales ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esConsumoMaterialesAsociadaSolicitud(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConsumoMaterialesAsociadaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Cargar la hora de apertura de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerHoraAperturaCuenta(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerHoraAperturaCuenta(con, idCuenta);
	}
	
	/**
	 * Indica si la ocupacion de un profesional de la salud esta parametrizada como ocupaciones que realizan cirugias 
	 * @param con
	 * @param codigoOcupacion
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean esOcupacionQueRealizaCx(Connection con, int codigoOcupacion, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esOcupacionQueRealizaCx(con, codigoOcupacion, codigoInstitucion);
	}

	/**
	 * Utilidad para verificar la existensia de la hoja de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeHojaAnestesia(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeHojaAnestesia(con, numeroSolicitud);
	}
	
	/**
	 * Utilidad para verificar la existensia de la hoja obstétrica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public static boolean existeHojaObstetrica(Connection con, int codigoPaciente)
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeHojaObstetrica(con, codigoPaciente);
		}
	
	/**
	 * Método que verifica si la cuenta tiene solicitudes de un tipo
	 * especifico, con un estado medico o de facturacion específico
	 * @param con
	 * @param idCuenta
	 * @param tipo
	 * @param estadoMedico
	 * @param estadoFacturacion
	 * @return
	 */
	public static boolean haySolicitudesEnCuenta(Connection con,int idCuenta,int tipo,int estadoMedico)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().haySolicitudesEnCuenta(con,idCuenta,tipo,estadoMedico);
		
	}
	
	/**
	 * Método implementado para verificar si una cirugía requiere justificacion
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio (cirugia)
	 * @param contrato actual de la cuenta que contiene la solicitud
	 * @return
	 */
	public static boolean cirugiaRequiereJustificacion(Connection con,int numeroSolicitud,int servicio,int contrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cirugiaRequiereJustificacion(con,numeroSolicitud,servicio,contrato);
	}

	/**
	 * Metodo para saber si una solcicitud de Procedimientos es multiple.
	 * @param con
	 * @param numeroSolicitud
	 * @return Retorna True Si la solicitud es multiple de lo contrario False. 
	 */
	public static boolean esSolicitudMultiple(Connection con,int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esSolicitudMultiple(con,numeroSolicitud);
	}
	
	/**
	 * Revisar Validez del contrato por fechas
	 * @param con
	 * @param cuenta
	 * @return Nombre Convenio, fecha inicio y fecha fin del contrato, cadena con representacion booleana si es valido o no
	 * Ej:
	 * 	0 - Cafesalud EPS
	 * 	1 - 01/01/2000
	 *  2 - 31/12/2006
	 *  3 - true
	 */
	public static String[] revisarValidezContrato(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().revisarValidezContrato(con, cuenta);
	}
	
	/**
	 * Utilidad para verificar la existencia de la hoja de oftalmológica
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean existeHojaOftalmologica(Connection con, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeHojaOftalmologica (con, codigoPaciente);
	}

	/**
	 * Verifica si esciste un monto en la BD
	 * @param con
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @return
	 */
	public static boolean existeMontoEnBD(Connection con, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeMontoEnBD(con, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo, fecha);
	}

	/**
	 * Metodo que me indica si un medico ya tiene una agenda para derminada fecha hora inicial y hora final,
	 * es decir si se cruza en el rango.
	 * @param con
	 * @param nuevoProfesional
	 * @param fecha --> formato de la aplicacion (dd/mm/yyyy)
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean seCruzaAgendaMedicoFechaHora(Connection con, int profesional, String fecha, String horaInicial, String horaFinal) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().seCruzaAgendaMedicoFechaHora(con,profesional,fecha,horaInicial,horaFinal);
	}
	
	/**
	 * metodo que indica si se debe generar recibos de caja automaticos para
	 * una via de ingreso dada
	 * @param con
	 * @param codigoViaIngreso
	 * @return
	 */
	public static boolean esReciboCajaAutomaticoViaIngresoDada(Connection con, int codigoViaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esReciboCajaAutomaticoViaIngresoDada(con, codigoViaIngreso);
	}
	
	/**
	 * 
	 * Método que consulta el código [pos 1] y la descripción [pos 2] del concepto tipo régimen
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param acronimoTipoRegimen
	 * @param tiposIngresoTesoreria
	 * @param esActivo
	 * @return
	 */
	public static HashMap<Object, Object> getCodigoDescripcionConceptoTipoRegimen(Connection con, int codigoInstitucion, String acronimoTipoRegimen, Vector<Object> tiposIngresoTesoreria, boolean esActivo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getCodigoDescripcionConceptoTipoRegimen(con, codigoInstitucion, acronimoTipoRegimen, tiposIngresoTesoreria, esActivo);
	}
	
	/**
	 * Método para saber si existe un centro ed costo asociado a una via de ingreso 
	 * de una insitucion determinada
	 * @param con
	 * @param centroCosto
	 * @param via
	 * @return
	 */
	public static boolean existeCentroCostoXViaIngreso(Connection con, int centroCosto, int viaIngreso, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCentroCostoXViaIngreso(con, centroCosto, viaIngreso, institucion);
	}
	
	/**
	 * Método implementado para consultar el codigo de area y el
	 * nombre del área del paciente, el formato es:
	 * codigoArea + constantesBD.separadorSplit + nombreArea
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getAreaPaciente(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getAreaPaciente(con,idCuenta);
	}
	
	/**
	 * Método que verifica si un paciente está muerto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean esPacienteMuerto(Connection con,int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esPacienteMuerto(con,codigoPaciente);
	}
	
	/**
	 * Método implementado para actualizar al paciente como muerto
	 * en la base de datos
	 * @param con
	 * @param codigoPaciente
	 * @param esVivo
	 * @param fechaMuerte
	 * @param estado
	 * @return
	 */
	public static boolean actualizarPacienteAMuertoTransaccional(Connection con,int codigoPaciente,boolean esVivo,String fechaMuerte,String horaMuerte,String certificadoDefuncion, String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().actualizarPacienteAMuertoTransaccional(con,codigoPaciente,esVivo,fechaMuerte,horaMuerte, certificadoDefuncion, estado);
	}

	/**
	 * Método para verificar la existencioa de cuentas asociadas a un contrato
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean existeCuentasAsociadasAContrato(Connection con, int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCuentasAbiertasAsociadasAContrato(con, codigoContrato);
	}

	/**
	 * 
	 * @param con
	 * @param int1
	 * @return
	 */
	public static boolean categoriaTriageUtilizadaEnSignosSintomasSistema(Connection con, int consecutivoCategoria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().categoriaTriageUtilizadaEnSignosSintomasSistema(con,consecutivoCategoria);
	}
	
	/**
	 * Obtiene los consecutivos pk del triage 
	 * 
	 * @param con
	 * @param acronimoTipoIdentificacion
	 * @param numeroIdentificacion
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return pos 0 --> consecutivoTriage  , pos 1 --> cpnsecutivofechaTriage
	 */
	public static String[] getConsecutivosTriage(Connection con, String acronimoTipoIdentificacion, String numeroIdentificacion, String codigoCentroAtencion, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getConsecutivosTriage(con, acronimoTipoIdentificacion, numeroIdentificacion, codigoCentroAtencion, codigoInstitucion);
	}
	
	/**
	 * Método para saber si un triage determinado para una institucion tiene
	 * un numero de admision asignado
	 * @param con
	 * @param consecutivoTriage
	 * @param consecutivoFechaTriage
	 * @param institucion
	 * @return
	 */
	public static boolean existeAdmisionParaTriage(Connection con, String consecutivoTriage, String consecutivoFechaTriage, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAdmisionParaTriage(con, consecutivoTriage, consecutivoFechaTriage, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param int1
	 * @return
	 */
	public static boolean categoriaTriageUtilizadaEnTriage(Connection con, int consecutivoCategoria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().categoriaTriageUtilizadaEnTriage(con,consecutivoCategoria);
	}

	/**
	 * 
	 * @param cuentaCobro
	 * @param institucion
	 * @param codigoAjuste 
	 * @return
	 */
	public static boolean cuentaCobroConFactEnAjustesPendienteDiferenteActual(double cuentaCobro, int institucion, double codigoAjuste) 
	{
		boolean respuesta=false;
		Connection con=null;
		try 
		{
			con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().cuentaCobroConFactEnAjustesPendienteDiferenteActual(con,cuentaCobro,institucion,codigoAjuste);
			UtilidadBD.closeConnection(con);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;

	}
	
	/**
	 * Método implementado para verificar si un paciente tiene cuentas Urgencias
	 * en estado Cuenta Activa o Asociada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean tieneCuentaUrgenciasAbierta(Connection con,String codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneCuentaUrgenciasAbierta(con,codigoPaciente);
	}
	
	/**
	 * Método para saber si una solicitud de cirugia que ya tiene hoja de quirurgica
	 * tiene la participacion de anestesiologo
	 * @param con
	 * @param consecutivoOrdenes
	 * @return
	 */
	public static boolean solCxNecesitaHojaAntesia(Connection con, int consecutivoOrdenes)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().solCxNecesitaHojaAntesia(con, consecutivoOrdenes);
	}
	
	/**
	 * Utilidad que obtiene los datos del médico que guardó la hoja
	 * de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String getDatosMedicoHojaAnestesia(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().getDatosMedicoHojaAnestesia(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @param i
	 */
	public static boolean esTipoProgramaPYPUsado(Connection con, String codTipoPrograma, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTipoProgramaPYPUsado(con,codTipoPrograma,institucion);
	}

	/**
	 * 
	 * @param string
	 * @param i
	 * @return
	 */
	public static boolean existeProgramaSaludPYP(String codigo, int institucion) 
	{
		boolean respuesta=false;
		Connection con=null;
		con=UtilidadBD.abrirConexion();
		respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeProgramaSaludPYP(con,codigo,institucion);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}

	/**
	 * 
	 * @param con 
	 * @param institucion 
	 * @param string
	 * @param string2
	 * @return
	 */
	public static boolean existeRelacionProgramaActividadPYP(Connection con, int institucion, String programa, String actividad) 
	{
		boolean respuesta=false;
		respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeRelacionProgramaActividadPYP(con,institucion,programa,actividad);
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public static boolean esActividadProgramaUsaoEnActProConvenios(Connection con, String codigo) 
	{
		boolean respuesta=false;
		respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esActividadProgramaUsaoEnActProConvenios(con,codigo);
		return respuesta;
	}
	
	/**
	 * existe cuenta cobro capitada
	 * @param con
	 * @param cuentaCobroCapitada
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existeCuentaCobroCapitada(Connection con, String cuentaCobroCapitada, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCuentaCobroCapitada(con, cuentaCobroCapitada, codigoInstitucion);
	}
	
	/**
	 * Método que verifica si un convenio es de PYP
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioPYP(Connection con,String codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConvenioPYP(con,codigoConvenio);
	}
	
	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioPYP(String codigoConvenio) 
	{
		boolean respuesta=false;
		Connection con=null;
		con=UtilidadBD.abrirConexion();
		respuesta=esConvenioPYP(con,codigoConvenio);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}

	
	
	/**
	 * Método que obtiene el año y mes de cierre saldo de capitación
	 * de acuerdo a los parámetros enviados 
	 * @param institucion
	 * @param tipoCierre
	 * @return String[]={año_cierre, mes_cierre}
	 * 					sino exite return vacio String[]={"",""}
	 */
	public static String[] obtenerFechaMesCierreSaldoCapitacion (Connection con, int institucion, String tipoCierre) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerFechaMesCierreSaldoCapitacion(con, institucion, tipoCierre);
	}
	

	/**
	 * Método que verifica si un paciente ya tiene información de PYP
	 * @param con
	 * @param paciente
	 * @param institucion
	 * @return
	 */
	public static boolean tienePacienteInformacionPYP(Connection con,PersonaBasica paciente,int institucion)
	{
		boolean resultado = false;
		HashMap campos = new HashMap();
		
		//1) Se consulta si el paciente tiene informaciion de Programas y Actividades pyp ya registradas
		campos.put("codigoPaciente",paciente.getCodigoPersona());
		campos.put("institucion",institucion);
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tienePacienteInformacionPYP(con,campos);
		
		//2) Si no se encuentran registros se verifica estado de la cuenta y que sea convenio PYP
		if(!resultado)
		{
			//Se verifica estado de la cuenta 
			Cuenta cuenta=new Cuenta();
			cuenta.cargar(con,paciente.getCodigoCuenta()+"");
			
			for(DtoSubCuentas subcuenta:cuenta.getCuenta().getConvenios())
			{
				//Si algun convenio de la cuenta es pyp y el estado de la cuenta es valido se puede abrir link de pyp
				if(esConvenioPYP(con,subcuenta.getConvenio().getCodigo()+"")&&
					(cuenta.getCuenta().getCodigoEstado()==ConstantesBD.codigoEstadoCuentaActiva||
					cuenta.getCuenta().getCodigoEstado()==ConstantesBD.codigoEstadoCuentaAsociada||
					cuenta.getCuenta().getCodigoEstado()==ConstantesBD.codigoEstadoCuentaFacturadaParcial|| 
					/* validación agregada x tarea 143622 */ cuenta.getCuenta().getCodigoEstado()==ConstantesBD.codigoEstadoCuentaFacturada
					))
					resultado = true;
			}
			
			
			
		}
		
		return resultado;
	}
	
	/**
	 * Informa si el paciente tiene cuenta activa y el convenio asociado a la cuenta tiene PyP
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static boolean tieneCuentaPacienteConvenioPyP(Connection con, PersonaBasica paciente)
	{
		//Se verifica estado de la cuenta 
		Cuenta cuenta=new Cuenta();
		try 
		{
			cuenta.cargar(con,paciente.getCodigoCuenta()+"");
		} 
		catch (Exception e) 
		{
			logger.error("Error al cargar cuenta en UtilidadValidacion tienePacienteConvenioPyP: "+e);
			e.printStackTrace();
		}
		
		if(!cuenta.getCuenta().getIdCuenta().equals("") && cuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()>0)
		{
			int estadoCuenta = cuenta.getCuenta().getCodigoEstado();
			boolean esConvenioPYP = esConvenioPYP(con,cuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()+"");
			
			if( (estadoCuenta==ConstantesBD.codigoEstadoCuentaActiva||estadoCuenta==ConstantesBD.codigoEstadoCuentaAsociada||
				 estadoCuenta==ConstantesBD.codigoEstadoCuentaFacturadaParcial) && esConvenioPYP )
				return true;
		}
		return false;
	}
	
	/**
	 * Utilidad para verificar si un paciente tiene registros de ordenes ambulatorias
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return true si existe sino retorna false
	 */
	public static boolean tienePacienteOrdenesAmbulatorias(Connection con, int codigoPaciente, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tienePacienteOrdenesAmbulatorias(con, codigoPaciente, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoGrupo
	 * @param institucion
	 * @return
	 */
	public static boolean puedoEliminarGrupoEtareoCreDes(Connection con,String consecutivoGrupo,String institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().puedoEliminarGrupoEtareoCreDes(con,consecutivoGrupo,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean esConvenioCapitado(Connection con, int convenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConvenioCapitado(con,convenio);
	}

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean esConvenioCapitado( int convenio) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConvenioCapitado(con,convenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean esAjusteDistribuidoCompletamete(Connection con, double codigoAjuste) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAjusteDistribuidoCompletamete(con,codigoAjuste);
	}
	
	/**
	 * Método implementado para verificar que el grupo de la actividad
	 * tenga centros de costo parametrizados en la funcionalidad
	 * Centros Costo x Grupo Servicios con centro de atencion que se encuentren
	 * en Actividades por centro atencion
	 * @param con
	 * @param consecutivoActividad
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public static boolean existeCentroCostoXGrupoServicioActividad(Connection con,String consecutivoActividad,int centroAtencion,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeCentroCostoXGrupoServicioActividad(con,consecutivoActividad,centroAtencion,institucion);
	}
	
	/**
	 * Método que verifica si el paciente tiene antecedentes de vacunas
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public static boolean existeAntecedenteVacunas (Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeAntecedenteVacunas(con, codigoPersona);
	}
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esAccidenteTransito(Connection con, int idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAccidenteTransito(con, idIngreso);
	}
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esAccidenteTransito(int idIngreso)
	{
		boolean respuesta=false;
		Connection con=UtilidadBD.abrirConexion();
		respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAccidenteTransito(con, idIngreso);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esAccidenteTransitoEstadoDado(Connection con, int idIngreso, String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAccidenteTransitoEstadoDado(con, idIngreso, estado);
	}
	
	/**
	 * metodo que indica si por ingreso existe evento catastrofico en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esEventoCatastroficoEstadoDado(Connection con, int idIngreso, String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esEventoCatastroficoEstadoDado(con, idIngreso, estado);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static boolean esServicioMultiple(Connection con,String codigoServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServicioMultiple(con, codigoServicio);
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @return
	 */
	public static boolean esServicioViaIngresoCargoSolicitud(Connection con, String viaIngreso, String servicio,String institucion) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServicioViaIngresoCargoSolicitud(con,viaIngreso,servicio,institucion);
	}


	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @return
	 */
	public static boolean esServicioViaIngresoCargoProceso(Connection con, String viaIngreso, String servicio,String institucion) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServicioViaIngresoCargoProceso(con,viaIngreso,servicio,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean estipoTransInvUsada(Connection con,String codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estipoTransInvUsada(con,codigo);
	}
	
	/**
	 * Verifica si a la paciente se le puede dar salida
	 * @param con
	 * @param cuenta
	 * @param codigoPaciente
	 * @param cuentaAsociada
	 * @param validacionGuardar
	 * @return
	 */
	public static Vector puedoDarOrdenSalidaXClap(Connection con, String cuenta, String codigoPaciente, String cuentaAsociada, int codigoDestinoSalida, boolean validacionGuardar)
	{
		Vector vectorErrores= new Vector();
		
		//tiene  solicitudes de tipo de servicio ?Partos y Cesareas??
		Vector vectorSolicitudes= new Vector();
		vectorSolicitudes= obtenerSolicitudesTipoServicioPartosCesarea(con, cuenta, cuentaAsociada);
		
		logger.info("VECTOR DE SOLICITUDES TIPO SERVICO PARTOS CESAREAS-->"+vectorSolicitudes);
		
		for(int w=0; w<vectorSolicitudes.size(); w++)
		{
			//solo se valida cuando no existio un aborto
			if(!InformacionParto.esCirugiaAborto(con, Integer.parseInt(vectorSolicitudes.get(w).toString())))
			{
				boolean entroError=false;
				//tenga información registrada en Información recién nacido X cirugia
				
				logger.info("EXISTE LA INFORMACION NECESARIA DEL RECIEN NACIDO >>>>>>"+existeInfoRecienNacido(con, vectorSolicitudes.get(w).toString()));
				
				/**
				 * MT 7378 y MT 7399
				 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
				 * 
				 * @author jeilones
				 * @date 16/17/2013
				 * */
				if(!validacionGuardar||(validacionGuardar&&(codigoDestinoSalida==ConstantesBD.codigoDestinoSalidaDadoDeAlta || codigoDestinoSalida==ConstantesBD.codigoDestinoSalidaVoluntaria))) {
					
					if(!existeInfoRecienNacido(con, vectorSolicitudes.get(w).toString()))
					{
						vectorErrores.add("Falta información de recién nacido para el consecutivo de Cirugía "+vectorSolicitudes.get(w));
						entroError=true;
					}
					
					logger.info("ESTA LA INFORMACION DE LOS PARTOS FINALIZADA >>>>>"+existeInfoPartosFinalizada(con, codigoPaciente, vectorSolicitudes.get(w).toString()));
					
					
					//la información del parto se encuentre finalizada
					if(!existeInfoPartosFinalizada(con, codigoPaciente, vectorSolicitudes.get(w).toString()))
					{
						vectorErrores.add("Falta finalizar la información correspondiente al parto para el consecutivo de Cirugía "+vectorSolicitudes.get(w));
						entroError=true;
					}
					
					if(!entroError)
					{
						//toca evaluar que la cantidad de hijos en la informacion del parto sea igual
						int cantidadHijosInfoParto= InformacionParto.cantidadHijosVivosMuertos(con, Integer.parseInt(vectorSolicitudes.get(w).toString()));
						int catidadHijosRegistradosRecienNacidos= InformacionRecienNacidos.obtenerConsecutivosInfoRecienNacidoDadoCx(con, vectorSolicitudes.get(w).toString(), ConstantesBD.acronimoSi).size();
						
						if(cantidadHijosInfoParto!=catidadHijosRegistradosRecienNacidos)
						{
							vectorErrores.add("La Cantidad de Hijos definida en Información del parto no corresponde con la cantidad registrada en recién nacidos finalizada.");
							entroError=true;
						}
					}
				}	
			}
		}
			
		return vectorErrores;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static Vector obtenerSolicitudesTipoServicioPartosCesarea(Connection con, String cuenta, String cuentaAsociada)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerSolicitudesTipoServicioPartosCesarea(con,cuenta, cuentaAsociada);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public static boolean existeInfoRecienNacido(Connection con, String consecutivoCx)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeInfoRecienNacido(con, consecutivoCx);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public static boolean existeInfoPartosFinalizada(Connection con, String codigoPaciente, String consecutivoCx)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeInfoPartosFinalizada(con, codigoPaciente, consecutivoCx);
	}

	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public static boolean tieneEgreso(String cuenta)
	{
		boolean respuesta;
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			respuesta=tieneEgreso(con, Integer.parseInt(cuenta));
		}
		catch (NumberFormatException e)
		{
			respuesta=false;
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			respuesta=false;
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return respuesta;
	}

	/**
	 * Metodo que rentorna un mapa con los ingresos validos de un paciente.,
	 * para saber si el paciente tiene ingresos validos, se validad que la fecha de egreso, en el ingreso sea null.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneIngresoAbierto(Connection con, int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().pacienteTieneIngresoAbierto(con,codigoPersona);
	}

	/**
	 * Metodo que rentorna un mapa con los ingresos validos de un paciente.,
	 * para saber si el paciente tiene ingresos validos, se validad que la fecha de egreso, en el ingreso sea null.
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneIngresoAbierto(int codigoPersona)
	{
		HashMap mapa=new HashMap();
		Connection con=UtilidadBD.abrirConexion();
		boolean valido=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().pacienteTieneIngresoAbierto(con,codigoPersona);
		UtilidadBD.closeConnection(con);
		return valido;
	}

	/**
	 * Retorna la fecha de egreso del ultimo ingreso en formato(dd/mm/yyyy)
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerFechaEgresoUltimoIngresoPaciente(Connection con,int codigoPersona)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerFechaEgresoUltimoIngresoPaciente(con,codigoPersona);
	}
	

	/**
	 * Retorna la fecha de egreso del ultimo ingreso en formato(dd/mm/yyyy)
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerFechaEgresoUltimoIngresoPaciente(int codigoPersona)
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerFechaEgresoUltimoIngresoPaciente(con,codigoPersona);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Retorna la fecha de factura de la cuenta en formato(dd/mm/yyyy)
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerFechaFacturaCuenta(int codigoPersona)
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerFechaFacturaCuenta(con,codigoPersona);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Retorna la fecha de factura de la cuenta en formato(dd/mm/yyyy)
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerFechaFacturaCuenta(Connection con, int codigoPersona)
	{
		String resultado="";
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerFechaFacturaCuenta(con,codigoPersona);
		return resultado;
	}
	
	/**
	 * Metodod que retorna true en caso de tener un registro de articulo-unidosis-cantidad activo,
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean articuloTieneCantidadUnidosisActiva(int codigoArticulo)
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().articuloTieneCantidadUnidosisActiva(con,codigoArticulo);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean articuloTieneCantidadUnidosisActivaDadoUnidosis(int codigoUnidosis)
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().articuloTieneCantidadUnidosisActivaDadoUnidosis(con,codigoUnidosis);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Metodod que retorna true en caso de tener un registro de articulo-unidosis-cantidad activo,
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean articuloTieneCantidadUnidosisActiva(Connection con,int codigoArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().articuloTieneCantidadUnidosisActiva(con,codigoArticulo);
	}
	
	/**
	 * Metodo que retorna true o false, de acurdo si toca alertar por que la fecha de vencimiento es menor igual a la fecha actual.
	 * @param fechaVencimiento
	 * @return
	 */
	public static boolean mostrarAlertaFechaVencimiento(String fechaVencimiento)
	{
		return UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))<=0;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudMedicamentos(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esSolicitudMedicamentos(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param clase
	 * @return
	 */
	public static boolean esClaseInventarioUsada(Connection con,int clase)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esClaseInventarioUsada(con,clase);
	}
	
	/**
	 * 
	 * @param con
	 * @param clase
	 * @return
	 */
	public static boolean esClaseGrupoInventarioUsado(Connection con,int grupo,int clase)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esClaseGrupoInventarioUsado(con,grupo,clase);
	}
	
	/**
	 * 
	 * @param con
	 * @param subGrupo
	 * @param clase
	 * @param grupo
	 * @return
	 */
	public static boolean esClaseGrupoSubGrupoInventarioUsado(Connection con,int subGrupo,int clase,int grupo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esClaseGrupoSubGrupoInventarioUsado(con,subGrupo,clase,grupo);
	}
	
	
	/**
	 * Utilidad que consulta si un paquete es referenciado en otras tabla.
	 * este metodo no se puede usar dentro de una transaccion, ya que el internamente abre y aborta una.
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static boolean esPaqueteUsado(Connection con,String codigoPaquete,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esPaqueteUsado(con,codigoPaquete,institucion);
	}
	
	/**
	 * Utilidad que consulta si un concepto ingresos tesoreria es referenciado en otras tabla.
	 * este metodo no se puede usar dentro de una transaccion, ya que el internamente abre y aborta una.
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean esConceptoUsado(Connection con,String codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConceptoUsado(con,codigo,institucion);
	}
	
	/**
	 * esNaturalezaArticulosUsado
	 */
	public static boolean esNaturalezaArticulosUsado(Connection con,String codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esNaturalezaArticulosUsado(con,codigo,institucion);
	}
	
	/**
	 * esViasIngresoUsado
	 */
	public static boolean esViasIngresoUsado(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esViasIngresoUsado(con,codigo);
	}
	
	/**
	 * esUbicacionGeograficaUsado
	 */
	public static boolean esUbicacionGeograficaUsado(Connection con,String codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esUbicacionGeograficaUsado(con,codigo);
	}
	
	/**
	 * esDepartamentoUsado
	 */
	public static boolean esDepartamentoUsado(Connection con,String codigo_departamento, String codigo_pais)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esDepartamentoUsado(con,codigo_departamento,codigo_pais);
	}
	
	/**
	 * esCiudadUsado
	 */
	public static boolean esCiudadUsado(Connection con,String codigo_ciudad,String codigo_departamento, String codigo_pais)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCiudadUsado(con,codigo_ciudad,codigo_departamento,codigo_pais);
	}
	
	/**
	 * esLocalidadUsado
	 */
	public static boolean esLocalidadUsado(Connection con,String codigo_localidad, String codigo_ciudad,String codigo_departamento, String codigo_pais)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esLocalidadUsado(con,codigo_localidad,codigo_ciudad,codigo_departamento,codigo_pais);
	}
	
	/**
	 * Es Tipos de Monedas Usado
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean esTiposMonedaUsado(Connection con,int codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTiposMonedaUsado(con,codigo,institucion);
	}
	
	public static boolean esVigenciaConvenioUsado(Connection con,int codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esVigenciaConvenioUsado(con, codigo, institucion);
	}
	
	
	public static boolean esInclusionesExclusionesUsado(Connection con,String codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esInclusionesExclusionesUsado(con,codigo,institucion);
	}
	
	
	public static boolean esPaqueteConvenioUsado(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esPaqueteConvenioUsado(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean esEventoCatastrofico(Connection con, int codigoIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esEventoCatastrofico(con,codigoIngreso);
	}
	
	
	public static boolean esTiposConvenioUsado(Connection con,String codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTiposConvenioUsado(con,codigo,institucion);
	}
	
	
	public static boolean esServiciosEsteticosUsado(Connection con,String codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esServiciosEsteticosUsado(con,codigo,institucion);
	}
	
	/**
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public static boolean esCoberturaUsado(Connection con, String codigoCobertura, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCoberturaUsado(con, codigoCobertura, institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Condicion de toma de Examen (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public static boolean esCondicionTmExamenUsado(Connection con, int codigoExamen, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCondicionTmExamenUsado(con, codigoExamen, institucion);		
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de pisos (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoPiso
	 * @param int institucion
	 * */
	public static boolean esPisosUsado(Connection con,int codigo)
	{		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esPisosUsado(con,codigo);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Condicion de tipo habitacion (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public static boolean esTipoHabitacionUsado(Connection con,String codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTipoHabitacionUsado(con,codigo,institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Almacen Parametros (true = no depende , false = depende)
	 * @param Connection con
	 * @param int codigo
	 * @param int institucion
	 * */
	public static boolean esAlmacenParametroUsado(Connection con,int codigo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAlmacenParametroUsado(con,codigo,institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de habitacion (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoHabitac
	 * @param int institucion
	 * */
	public static boolean esHabitacionesUsado(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esHabitacionesUsado(con,codigo);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo Instituciones SIRC  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoInstSirc
	 * @param int institucion
	 * */
	public static boolean esInstitucionSircUsado(Connection con, String codigoInstSirc, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esInstitucionSircUsado(con,codigoInstSirc,institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Condicion de tipos usuario cama (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigo
	 * */
	public static boolean esTiposUsuarioCamaUsado(Connection con, int codigo, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTiposUsuarioCamaUsado(con,codigo,institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Centros de Costos  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroCostos
	 * @param int institucion
	 * */
	public static boolean esCentroCostoUsado(Connection con,String codigoCentroCosto, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCentroCostoUsado(con,codigoCentroCosto,institucion);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el consecutivo de Centros de Atencion  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroAtencion
	 * @param int institucion
	 * */
	public boolean esCentroAtencionUsado(Connection con, String codigoCentroAtencion, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCentroAtencionUsado(con, codigoCentroAtencion, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean esContrarreferencia(Connection con, int codigoIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esContrarreferencia(con,codigoIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @return
	 */
	public static boolean esContrarreferenciaEstadoDado(Connection con, int idIngreso, String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esContrarreferenciaEstadoDado(con, idIngreso, estado);
	}	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaComplejidad(Connection con,int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().convenioManejaComplejidad(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaComplejidad(int codigoConvenio)
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean resultado=false;
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().convenioManejaComplejidad(con, codigoConvenio);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Metodo que verfica si un numero cuenta de cobro ya fue asignado.
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	public static boolean esNumeroCuentaCobroUsado(Connection con, double numeroCuentaCobro) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esNumeroCuentaCobroUsado(con, numeroCuentaCobro);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esConvenioUsado(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConvenioUsado(con, codigo);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del detalle de Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * @param String codigo Encabezado
	 * */
	public static boolean esDetallePorcentajeCxMultiUsado(Connection con, int codigo, int codigo_encab)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esDetallePorcentajeCxMultiUsado(con, codigo, codigo_encab);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del encabezado del Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param int institucion
	 * @param String codigo Encabezado 
	 * */
	public static boolean esEncaPorcentajeCxMultiUsado(Connection con, int institucion, int codigo_encab)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esEncaPorcentajeCxMultiUsado(con, institucion, codigo_encab);
	}
	
	
	public static int esCuentaValida(Connection con, int codigoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCuentaValida(con, codigoCuenta);
	}
	
	/**
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public static boolean esAsocioSalaCirugiaUsado(Connection con, String codigoAsocio, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAsocioSalaCirugiaUsado(con, codigoAsocio, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoEstado
	 * @return
	 */
	public static boolean responsableTieneServicioEstado(Connection con, String subCuenta, int codigoEstado) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().responsableTieneServicioEstado(con, subCuenta,codigoEstado);
	}
	
	
	/**
	 * 	Valida el Paciente Cargado en Session     
	 *	@param Connection con
	 *	@param PersonaBasica personabasica
	 * */
	public static RespuestaValidacion esValidoPacienteCargado(Connection con,PersonaBasica paciente)
	{
		// Validar Paciente Cargado
		if(paciente.getCodigoPersona()<1)
		    return new RespuestaValidacion("errors.paciente.noCargado",false);
		
		// Validar Ingreso del Paciente
		else if(paciente.getCodigoIngreso()<1)
			return new RespuestaValidacion("errors.paciente.noIngresoSesion",false);
		
		// Validar Cuenta
		int idCuenta = paciente.getCodigoCuenta();
		if(esCuentaValida(con, idCuenta)<1){
			return new RespuestaValidacion("errors.paciente.cuentaNoValida",false);
		}
		
		return new RespuestaValidacion("",true);		
	}
	
	/**
	 * 	Valida el Paciente Cargado en Session     
	 * @param string 
	 *	@param Connection con
	 *	@param PersonaBasica personabasica
	 * */
	public static RespuestaValidacion esValidoPacienteCargado(Connection con,PersonaBasica paciente, String ultimaCuentaFacturada)
	{
		logger.info("CodigoPersona "+paciente.getCodigoPersona());
		logger.info("CodigoIngreso "+paciente.getCodigoIngreso());
		logger.info("CodigoCuenta "+paciente.getCodigoCuenta());
		logger.info("ultimaCuentaFacturada "+ultimaCuentaFacturada);
		
		
		// Validar Paciente Cargado
		if(paciente.getCodigoPersona()<1)
		    return new RespuestaValidacion("errors.paciente.noCargado",false);
		
		// Validar Ingreso del Paciente
		else if(paciente.getCodigoIngreso()<1 && UtilidadTexto.isEmpty(ultimaCuentaFacturada))
			return new RespuestaValidacion("errors.paciente.noIngresoSesion",false);
		
		// Validar Cuenta
		int idCuenta = paciente.getCodigoCuenta();
		if(esCuentaValida(con, idCuenta)<1){
			if(UtilidadTexto.isEmpty(ultimaCuentaFacturada))
				return new RespuestaValidacion("errors.paciente.cuentaNoValida",false);
		}
		return new RespuestaValidacion("",true);		
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param usuario 
	 * @param loginUsuario
	 * @return
	 */
	public static boolean ingresoEstaEnProcesoDistribucion(Connection con, int codigoIngreso, String usuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().ingresoEstaEnProcesoDistribucion(con, codigoIngreso,usuario);
	}	
	
	
	/**
	 * Verifica si existe cruce en un rango de numeros definidos
	 * @param rangoInicialA
	 * @param rangoFinalA
	 * @param rangoInicialB
	 * @param rangoFinalB
	 * */
	public static boolean hayCruceNumeros(int rangoInicialA,int rangoFinalA,int rangoInicialB, int rangoFinalB)
	{
		int tmp = 0;
		
		if(rangoInicialA < rangoInicialB)
		{
			tmp = rangoInicialA; 
			rangoInicialA = rangoInicialB;
			rangoInicialB = tmp;
			
			tmp = rangoFinalA; 
			rangoFinalA = rangoFinalB;
			rangoFinalB = tmp;			
		}
		
		
		if( (rangoFinalB >= rangoInicialA && rangoFinalB <= rangoFinalA) ||
				(rangoFinalB > rangoFinalA))
		{
			return true;						
		}				
			
		return false;
	}
	
	
	/**
	 * 
	 * @param rangoInicialA
	 * @param rangoFinalA
	 * @param rangoInicialB
	 * @param rangoFinalB
	 * @return
	 */
	public static boolean hayCruceNumerosDouble(double rangoInicialA, double rangoFinalA, double rangoInicialB, double rangoFinalB)
	{
		double tmp = 0;
		
		if(rangoInicialA < rangoInicialB)
		{
			tmp = rangoInicialA; 
			rangoInicialA = rangoInicialB;
			rangoInicialB = tmp;
			
			tmp = rangoFinalA; 
			rangoFinalA = rangoFinalB;
			rangoFinalB = tmp;			
		}
		
		if( (rangoFinalB >= rangoInicialA && rangoFinalB <= rangoFinalA) ||
				(rangoFinalB > rangoFinalA))
		{
			return true;						
		}				
			
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esAsocioXUvrTipoSalaUsado(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAsocioXUvrTipoSalaUsado(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esAsociosXUvrUsado(Connection con,int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esAsociosXUvrUsado(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoBD
	 * @param tipoId
	 * @param numeroId
	 * @return
	 * @throws SQLException
	 */
	public static boolean existePacientes (Connection con, String tipoBD, String tipoId, String numeroId) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existePacientes (con, tipoBD, tipoId, numeroId);
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneMovimientoAbonos(int codigoPersona) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean resultado=false;
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().pacienteTieneMovimientoAbonos(con, codigoPersona);
		UtilidadBD.closeConnection(con);
		return resultado;	
	}

	public static boolean tieneEvolucionesParaElDia(Connection con, int codigoCuenta, String fecha) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneEvolucionesParaElDia(con,codigoCuenta,fecha);
	}
	
	
	/**
	 * Valida si las notas de enfermeria esta cerrada
	 * @param Connection con
	 * @param int codigoCuenta
	 * */
	public static boolean esCerradaNotasEnfermeria(Connection con, int codigoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esCerradaNotasEnfermeria(con,codigoCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public static String[] obtenerFechaYHoraCuidadoEspecial (Connection con, int idCuenta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerFechaYHoraCuidadoEspecial(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tipoMonitoreoRequiereValoracion(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tipoMonitoreoRequiereValoracion(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tieneValoracionCuidadoEspecial(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneValoracionCuidadoEspecial(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tieneIngresoActivoCuidadoEspecial(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneIngresoActivoCuidadoEspecial(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean monitoreoEvolucionRequiereValoracion(Connection con, int codigoEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().monitoreoEvolucionRequiereValoracion(con, codigoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static boolean conductaSeguirUltimaEvolucion(Connection con, int codigoEvolucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().conductaSeguirUltimaEvolucion(con, codigoEvolucion); 
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean tieneIngresoCuidadosFinalizado(Connection con, int codigoIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneIngresoCuidadosFinalizado(con, codigoIngreso); 
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean pacienteFalleceCirugia(Connection con, int codigoCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().pacienteFalleceCirugia(con, codigoCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean estadoSolicitudAnulacion(Connection con, int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().estadoSolicitudAnulacion(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param centroAtencion 
	 * @return
	 */
	public static boolean usuarioAutorizadoAnular(Connection con, String loginUsuario, int centroAtencion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().usuarioAutorizadoAnular(con, loginUsuario, centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean facturaAprobadaAnular(Connection con, int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().facturaAprobadaAnular(con, codigoFactura); 
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean facturaAprobadaAnular(int codigoFactura) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean returna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().facturaAprobadaAnular(con, codigoFactura);
		UtilidadBD.closeConnection(con);
		return returna;
	}
	
	/**
	 * Metodo encargado de verificar si un concepto especifico es 
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public static boolean esConceptoEspecificoUsado(Connection con, String codigo, String institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConceptoEspecificoUsado(con, codigo, institucion);
	}
	
	/***
	 * @param Connection con
	 * @param String numeroIdentificacio
	 * @param String tipoIdentificacion   
	 * */
	public static InfoDatosString esPacienteCapitadoVigente(Connection con, String fechaVigencia, String numeroIdentificacion, String tipoIdentificacion)
	{		
		HashMap parametros = new HashMap();
		logger.info("valor de parametros es paciente capitado >> "+fechaVigencia+" >> "+numeroIdentificacion+" >> "+tipoIdentificacion);
		
		parametros.put("fechaVigencia",UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
		
		if(tipoIdentificacion.length() <= 2)		
			parametros.put("tipoId",tipoIdentificacion);
		else
		{
			if(tipoIdentificacion.contains("-"))			
				parametros.put("tipoId",tipoIdentificacion.split("-")[0].trim());			
			else
				parametros.put("tipoId",ConstantesValoresPorDefecto.valorValoresDefectoTipoId.split("@@")[0].trim());
		}
			
		parametros.put("numeroId",numeroIdentificacion);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esPacienteCapitadoVigente(con,parametros);		
	}
	
	/**
	 * Método que indica si un contrato se 
	 * esta usando en otras funcionalidades 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esContratoUsado(Connection con, int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esContratoUsado(con, codigoContrato);
	}
	
	/**
	 * Metodo encargado de verificar si un textos respuesta procedimientos esta
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public static boolean esTextosRespuestaProcedimientosUsado(Connection con, String codigo, String institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esTextoRespuestaProcedimientosUsado(con, codigo, institucion);
	}
	
	/**
	 * Utilidad que consulta si un paquete es referenciado en otras tabla.
	 * este metodo no se puede usar dentro de una transaccion, ya que el internamente abre y aborta una.
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static boolean esGrupoEspecialUsado(Connection con,String codigoGrupo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esGrupoEspecialUsado(con,codigoGrupo);
	}
	
	/**
	 * Utilidad que consulta si un paquete es referenciado en otras tabla.
	 * este metodo no se puede usar dentro de una transaccion, ya que el internamente abre y aborta una.
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static boolean esConceptoAjusteUsado(Connection con,String codigoAjuste, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConceptoAjusteUsado(con, codigoAjuste, codigoInstitucion);
	}
	
	/**
	 * Metodo encargado de idenficar si un consentimiento informado
	 * ha sido usado o no
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean esConsentimientoInformadoUsado(Connection con, String codigo, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConsentimientoInformadoUsado(con, codigo, institucion);
	}
	
	/**
	 * Metodo encargado de consultar el ultimo ingreso
	 * del paciente y verificar si este tuvo tipo evento
	 * catastrófico
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int esUltimoIngreso(Connection con, String codigoPaciente) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esUltimoIngreso(con, codigoPaciente);
	}
	
	/**
	 * Metodo encargado de verificar si un textos respuesta procedimientos esta siendo utilizado o no.
	 * @param con Connection
	 * @param codArticulo String
	 * @param institucion int
	 * */
	public static boolean esArticuloUsado(Connection con, String codArticulo, String institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esArticuloUsado(con, codArticulo, institucion);
	}

	/**
	 * 
	 * @return
	 */
	public static String obtenerMaximoConsecutivoJustificacionNoPosArticulos() 
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerMaximoConsecutivoJustificacionNoPosArticulos(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @return
	 */
	public static String obtenerMaximoConsecutivoJustificacionNoPosServicios() 
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().obtenerMaximoConsecutivoJustificacionNoPosServicios(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean esUsuarioAUtorizadoAnulacionUsado(Connection con, String codigoUsuario, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esUsuarioAUtorizadoAnulacionUsado(con, codigoUsuario, codigoInstitucion);
	}

	public static boolean validarPacienteEnValoracion(Connection con, int codigoPersona) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validarPacienteEnValoracion(con, codigoPersona);
	}

	/**
	 * Método que valida si el paciente es un deudor o tiene asociado un deudor
	 * de cartera paciente 
	 * @param con
	 * @param paciente
	 * @return true en caso de que sea o tenga asociado un deudor
	 */
	public static boolean esDeudorCarteraPaciente(Connection con, PersonaBasica paciente) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esDeudorCarteraPaciente(con, paciente);
	}

	/**
	 * Método que consulta el saldo de cartera paciente
	 * @param con
	 * @param paciente
	 * @return valor del saldo
	 */
	public static double consultarSaldoPaciente(Connection con, PersonaBasica paciente) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().consultarSaldoPaciente(con, paciente);
	}

	/**
	 * Verifica el bloqueo del paciente por vía de ingreso.
	 * @param con
	 * @param paciente
	 * @param viaIngreso
	 * @return
	 */
	public static boolean verificarBloqueoIngresoPacienteODeudor(Connection con, PersonaBasica paciente, int viaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().verificarBloqueoIngresoPacienteODeudor(con, paciente, viaIngreso);
	}

	public static boolean tieneCuotasPendientes(Connection con, PersonaBasica paciente, int numeroDiasMora) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneCuotasPendientes(con, paciente, numeroDiasMora);
	}

	public static boolean tieneAutorizacionIngresoSaldoPendiente(Connection con, PersonaBasica paciente, int viaIngreso, String codigoTipoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().tieneAutorizacionIngresoSaldoPendiente(con, paciente, viaIngreso, codigoTipoPaciente);
	}
	
	public static boolean validarPacienteEnValoracionDiferenteUsuario(Connection con, int codigoPersona, String loginUsuario) {
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().validarPacienteEnValoracionDiferenteUsuario(con,codigoPersona,loginUsuario);
	}
	
	public static boolean existeConvencionMedico (Connection con, int convencion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().existeConvencionMedico(con, convencion);
	}

	/**
	 * 
	 * @param institucion
	 * @param valorConsecutivo
	 * @return
	 */
	public static boolean esConsecutvioReciboCajaUsado(int institucion,String valorConsecutivo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esConsecutvioReciboCajaUsado(institucion, valorConsecutivo);
	}

	/**
	 * 
	 */
	public static boolean esNaturalezaValidaTipoRegimen(int natPaciente,
			String tipoRegimen) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esNaturalezaValidaTipoRegimen(natPaciente, tipoRegimen);

	}
	
	/**
	 * Método encargado de devolver el nombre de la persona completo, con los
	 * respectivos espacios entre nombres y apellidos, y si no existe alguno de
	 * ellos, deja un solo espacio entre textos.
	 * 
	 * @param persona	Dto de persona que trae la información básica de la persona.
	 * 
	 * @return nombreCompletoPersona
	 */
	public static String obtenerNombreCompletoPersona(DtoPersonas persona) {
		return persona.getPrimerNombre()
				+ (persona.getPrimerNombre().isEmpty() ? "" : " ")
				+ persona.getSegundoNombre()
				+ (persona.getSegundoNombre().isEmpty() ? "" : " ")
				+ persona.getPrimerApellido()
				+ (persona.getPrimerApellido().isEmpty() ? "" : " ")
				+ persona.getSegundoApellido();
	}

	/**
	 * Método encargado de devolver el primer nombre y el primer apellido del
	 * usuario, con los respectivos espacios entre el primer nombre y el primer
	 * apellido.
	 * 
	 * @param usuario	Dto del usuario que trae la información básica del usuario.
	 * 
	 * @return primerNombreApellidoUsuario
	 */
	public static String obtenerPrimerNombreApellidoUsuario(
			DtoUsuarioPersona usuario) {
		return usuario.getNombre() + (usuario.getNombre().isEmpty() ? "" : " ")
				+ usuario.getApellido();
	}
	
	/**
	 * Método que verifica si dicha evolucion tuvo una para ese egreso su estado de salida fue muerto
	 * @param con
	 * @param idCuenta
	 * @param idEvolucion
	 * @return
	 * @author javrammo
	 * @since ipsm_1.1.1
	 */
	public static boolean esEvolucionConOrdenSalidaPacienteMuerto(Connection con,int idCuenta,int idEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadValidacionDao().esEvolucionConOrdenSalidaPacienteMuerto(con,idCuenta,idEvolucion);
	}		
}