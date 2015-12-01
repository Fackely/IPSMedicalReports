/*
 * @(#)UtilidadValidacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;
import util.RespuestaValidacion;
import util.RespuestaValidacionTratante;
import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Esta clase define la <i>interface</i> de servicios de acceso a base de datos
 * usada por los m�todos de <code>UtilidadValidacion</code> en la aplicacion.
 *
 *	@version 1.0, Oct 15, 2003
 */
public interface UtilidadValidacionDao
{
	/**
	 * M�todo que da los permisos de acceso a una solicitud en
	 * particular. Devuelve un arreglo de enteros que:
	 * - En su primera posici�n tiene el c�digo de la ocupaci�n que
	 * puede responder esta solicitud (0 si puede ser cualquiera)
	 * - En su segunda posici�n tiene el c�digo del centro de costo que
	 * puede responder esta solicitud (0 si puede ser cualquiera)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud
	 * @param tipoSolicitud
	 * @return
	 * @throws SQLException
	 */
	public int[] obtenerPermisosAcceso_validacionAccesoSolicitud (Connection con, int numeroSolicitud, String tipoSolicitud) throws SQLException;

	/**
	 * M�todo que permite averiguar el n�mero de solicitudes de
	 * valoraci�n para una cuenta espec�fica
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se quiere
	 * averiguar el n�mero de solicitudes de valoraci�n
	 * @return
	 * @throws SQLException
	 */
	public int numeroSolicitudesValoracionCuenta (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que obtiene la fecha y hora de ingreso a observaci�n
	 * para una admisi�n de urgencias
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoAdmision C�digo de la admisi�n de urgencias
	 * @param anioAdmision A�o de la admisi�n de urgencias
	 * @return
	 * @throws SQLException
	 */
	public String[] obtenerFechayHoraObservacionAdmisionUrgencias (Connection con, int codigoAdmision, int anioAdmision) throws SQLException;

	/**
	 * M�todo que revisa si hay alguna evolucion para una cuenta
	 * particular que tenga fecha y hora superior a la dada (La fecha
	 * debe darse en el formato usado en la BD)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta en la que se deseen buscar
	 * las evoluciones
	 * @param fechaDada Fecha en el formato de la BD para la
	 * que se desea buscar evoluciones con fecha superior a esta
	 * @param horaDada Hora para la que se desea buscar evoluciones
	 * con fecha igual a la dada y hora superior a la horaDada
	 * @return
	 * @throws SQLException
	 */
	public boolean existeEvolucionConFechaSuperior (Connection con, int idCuenta, String fechaDada, String horaDada) throws SQLException;

	/**
	 * M�todo que obtiene la m�xima fecha y hora de evoluci�n para una cuenta
	 * en particular. Si no las encuentra retorna nulo
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la m�xima
	 * fecha y hora de evoluci�n
	 * @return
	 * @throws SQLException
	 */
	public String[] obtenerMaximaFechaYHoraEvolucion (Connection con, int idCuenta) ;

	/**
	 * M�todo que obtiene la m�xima fecha y hora de valoraci�n para una cuenta
	 * en particular. Si no las encuentra retorna nulo
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la m�xima
	 * fecha y hora de valoraci�n
	 * @return
	 * @throws SQLException
	 */
	public String[] obtenerMaximaFechaYHoraValoracion (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que dada una cuenta, obtiene la fecha y hora de la primera valoraci�n
	 * de esta cuenta. Si no las encuentra retorna nulo
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la fecha
	 * y hora de la primera valoraci�n de la misma
	 * @return
	 * @throws SQLException
	 */
	public String[] obtenerFechaYHoraPrimeraValoracion (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que dada una cuenta, obtiene el n�mero de la valoraci�n
	 * inicial de esta cuenta. Si no la encuentra retorna una excepci�n
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta Id de la cuenta en la que se quiere buscar la
	 * primera valoraci�n de la misma
	 * @return
	 * @throws SQLException
	 */
	public int obtenerNumeroSolicitudPrimeraValoracion (Connection con, int idCuenta) throws SQLException;

	/**
	 * Retorna si existe una solicitud de valoraci�n de hospitalizaci�n o
	 * urgencias para la cuenta dada
	 *
	 * @param con
	 * @param idCuenta
	 * @return Resultado, en su booleano tiene true si existe y en la
	 * descripci�n el c�digo de la solicitud, false de lo contrario, y si es
	 * false la descripci�n si fue porque la consulta no se pudo realizar o
	 * porque no existe.
	 */
	public HashMap<String,Object> existeSolicitudValoracionCuenta(Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que dice si una cuenta es de urgencias o no
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que
	 * se desea saber si es de urgencias o no
	 * @return
	 * @throws SQLException
	 */
	public boolean esCuentaUrgencias (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que obtiene el centro de costo tratante, sin
	 * importar si es de urgencias u hospitalizaci�n, lo hace
	 * de forma lenta (Debido a complejidad man. Tratante).
	 * En lo posible utilize el m�todo _noUrgencias
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta a la que se desea
	 * el c�digo del centro de costo tratante
	 * @return
	 * @throws SQLException
	 */
	public int getCodigoCentroCostoTratanteMetodoLento (Connection con, int idCuenta, int institucion) throws SQLException;

	/**
	 * M�todo que busca el c�digo del centro de costo tratante
	 * para una cuenta, sin manejar el caso de urgencias, en
	 * caso de no encontrar retorna -1024
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta a la que se desea
	 * el c�digo del centro de costo tratante
	 * @return
	 * @throws SQLException
	 */
	public int getCodigoCentroCostoTratante_noUrgencias (Connection con, int idCuenta, int institucion) throws SQLException;
	
	
	/**
	 * M�todo que verifica si un medico es tratante:
	 * Se consulta si el area de la cuenta del paciente hace 
	 * parte de alguno de los centros de costos del usuario del m�dico
	 * @param con
	 * @param login
	 * @param idCuenta
	 * @return
	 */
	public boolean esMedicoTratante(Connection con,String login,int idCuenta);
	
	/**
	 * M�todo que busca el c�digo del centro de costo 
	 * para una solicitud determinada 
	 * @param con
	 * @param numeroSolicitud (�ndice solicitides),
	 * @return c�d
	 * @throws SQLException
	 */
	public int getCodigoCentroCostoSolicitanteXNumeroSolicitud (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Este m�todo revisa si existe una evoluci�n diaria del tratante,
	 * para saber si puede dar orden de salida (Aparte de la la otra
	 * validaci�n). Si no existe retorna la fecha de la Valoraci�n
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta a la cual se quieren buscar
	 * evoluciones (Debe tener al menos una valoracion)
	 * @return
	 */
	public RespuestaValidacion existenEvolucionesSuficientesOrdenSalida (Connection con, int idCuenta, int codigoCentroCostoTratante, int diaEvolucion, int mesEvolucion, int anioEvolucion, boolean esUrgencias, int institucion) throws SQLException;

	/**
	 * M�todo que me dice si para un centro de costo particular y una
	 * cuenta, hay evoluciones del dia de hoy
	 *
	 * @param con Conexion con la fuente de datos
	 * @param idCuenta Codigo de la cuenta en la que se desea buscar
	 * la evoluci�n
	 * @param codigoCentroCostoTratante Codigo del centro costo para
	 * el cual se desea buscar la evoluci�n
	 * @return
	 * @throws SQLException
	 */
	public boolean existeEvolucionHoy (Connection con, int idCuenta) throws SQLException;

	/**
	 * Este m�todo recibe un n�mero de evoluci�n y revisa si este corresponde
	 * efectivamente a una evoluci�n y en caso que si, nos devuelve en el
	 * campo de texto de RespuestaValidacion "Hospitalizado" si es una
	 * evoluci�n de hospitalizados y un "Urgencias" si es de urgencias
	 * o "General" si es de general
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroEvolucion N�mero de la Evoluci�n a revisar
	 * @return un objeto RespuestaValidacion que  define si existe evoluci�n
	 * y en caso que si exista en el campo de texto un "Hospitalizado" si es una
	 * evoluci�n de hospitalizados y un "Urgencias" si es de urgencias
	 * o "General" si es de general
	 *
	 * @throws SQLException
	 */
	public RespuestaValidacion revisarTipoEvolucion (Connection con, int numeroEvolucion) throws SQLException;

	/**
	 * Este m�todo se encarga de revisar si existe una cuenta para una evoluci�n
	 * (c�digo evoluci�n dado) existe cuenta, si no existe el texto en
	 * RespuestaValidacion da la raz�n, si existe da el n�mero de la cuenta
	 *
	 * @param con una conexion abierta con una BD
	 * @param codigoEvolucion C�digo de la evoluci�n
	 * a la cual se va a buscar cuenta
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion existeCuentaParaEvolucion (Connection con, int codigoEvolucion) throws SQLException;

	/**
	 * Este m�todo revisa si un usuario puede o no modificar la evoluci�n.
	 *
	 * Las reglas son las siguientes:
	 *
	 * - Si no se ha hecho egreso para a la cuenta, solo el m�dico tratante
	 * y/o un m�dico que pertenezca al centro de costo del que lleno la
	 * evoluci�n puede modificar la evoluci�n
	 *
	 * - Despu�s de la orden de egreso y cuando la cuenta a�n no ha sido
	 * cerrada, solo el m�dico tratante.
	 *
	 * - Despu�s de cerrada la cuenta, cualquier usuario puede a�adir cosas
	 * (NO eliminar). (condici�n 4)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta en la que est� la evolucion que se
	 * quiere modificar (se asume que la cuenta esta abierta o se pueden agregar
	 * cosas a la misma, tal y como garantiza la cargada de la cuenta )
	 * @param codigoCentroCosto C�digo del centro de costo del m�dico (es 0
	 * si se trata de un usuario normal)
	 * @return
	 */
	public RespuestaValidacionTratante validacionModificarEvolucion (Connection con, int codigoEvolucion, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException;

	/**
	 * Este m�todo dice si hay una evoluci�n para un d�a
	 * en particular, para un centro de costo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta a la cual se le quiere buscar
	 * la evolucion
	 * @param dia D�a en el cual se quiere buscar la evolucion
	 * @param mes Mes en el cual se quiere buscar la evolucion
	 * @param anio A�o en el cual se quiere buscar la evolucion
	 * @return
	 * @throws SQLException
	 */
	public boolean existeEvolucionDadaFecha (Connection con, int idCuenta, int dia, int mes, int anio, int idCuentaAsociada) throws SQLException;

	/**
	 * Este m�todo revisa si se tienen permisos suficientes para agregar notas
	 * aclaratorias y/o definir la epicrisis de hospitalizacion o urgencias
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idEpicrisis C�digo de la epicrisis (ingreso) al que se quiere
	 * revisar los permisos para nota y definicion de epicrisis
	 * 
	 * @param paciente : El paciente al cual se le quieren revisar la epicrisis
	 * @param usuario : El usuario que intenta cambiar los permisos de epicrisis
	 * @param esEpicrisisHospitalizacion Boolean que indica si esta es la epicrisis
	 * de hospitalizaci�n
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacionTratante permisosEpicrisisNotaYDefinicion (Connection con, int idEpicrisis, PersonaBasica paciente, UsuarioBasico usuario, boolean esEpicrisisHospitalizacion, int institucion) throws SQLException;

	/**
	 * Este m�todo nos dice que d�as faltan por evoluci�n para una epicrisis
	 * aparte de la que da orden de salida
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idIngreso Ingreso en el que se va a revisar
	 * @param esEpicrisisHospitalizacion Define si se va a revisar epicrisis
	 * de hospitalizaci�n o de urgencias
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion faltanEvolucionesEpicrisis (Connection con, int idIngreso, boolean esEpicrisisHospitalizacion, boolean esUrgencias, int institucion) throws SQLException;

	/**
	 * M�todo que retorna verdadero si para esta cuenta la valoraci�n de
	 * Urgencias no esta en cama de observaci�n y falso si esta en cama
	 * de observaci�n o si no existe la valoraci�n de urgencias. (Se utilizo
	 * el prefijo no para poder seguir trabajando con boolean)
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta para la cual se quiere buscar
	 * la conducta a seguir en su primera valoraci�n
	 * @return
	 */
	public boolean estaEnCamaObservacion(Connection con, int idCuenta) throws SQLException;

	/**
	 * Este m�todo permite saber si una cuenta con valoraci�n
	 * de urgencias tiene como conducta a seguir cama de
	 * observaci�n. El m�todo retorna verdadero si se cumple
	 * esta condici�n y falso de lo contrario (si, por ejemplo no
	 * existe valoraci�n de urgencias tambi�n retorna falso)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta en la que se
	 * desea buscar la conducta de la valoraci�n
	 * @return
	 */
	public boolean tieneValoracionConConductaCamaObservacion (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que revisa si una cuenta tiene una valoraci�n la
	 * cual tenga una de las conductas a seguir
	 * pasadas por par�metro
	 * @param con Conexi�n con la BD
	 * @param idCuenta id de la cuenta que se desea validar
	 * @param conductas Array int con los c�digos de las conductas a validar
	 * @return true si hay valoraciones con alguna de las conductas dadas.
	 */
	public boolean validacionConductaASeguir (Connection con, int idCuenta, int[] conductas);

	/**
	 * Este m�todo se encarga de revisar si se puede reversar un egreso.
	 * Las reglas para esto son:
	 *
	 * 0. Debe existir una admisi�n de hosp. para la cuenta especificada
	 * 1.Si es el m�dico tratante puede reversar el egreso si:
	 * 	1.1. Existe una entrada en la tabla egresos con este codigo
	 * 	1.2. En esta entrada el m�dico no es nulo
	 * 	(La condici�n de usuario no nulo no es necesaria, ya que esto pasa
	 * 	cuando el m�dico ha dado orden de salida y todav�a no se ha llenado
	 * 	orden de salida)
	 * 2. Si no es el m�dico tratante puede reversar si:
	 * 	2.1. Existe una entrada en la tabla egresos con este codigo
	 * 	2.2. En esta entrada el m�dico no es nulo
	 * 	2.3. En esta entrada el usuario no es nulo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @param codigoCentroCosto C�digo del centro de costo del m�dico / usuario
	 * que desea saber si puede o no realizar la reversi�n
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacionTratante puedoReversarEgresoHospitalizacion (Connection con, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException;

	/**
	 * Este m�todo dice si se debe mostrar reversi�n del egreso y en caso afirmativo
	 * devuelve en textoRespuesta el motivo a mostrar. Las reglas para saber si hay
	 * que mostrar o no son:
	 * 1. Existe una entrada en la tabla egresos y el m�dico esta en nulo (Condici�n
	 * de reversi�n de egreso)
	 * 2. No se ha mostrado en ninguna evolucion anterior
	 *
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion deboMostrarMotivoReversionEgreso (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que revisa si existe una epicrisis o no, dado el c�digo. Para el caso
	 * particular de la epicrisis de urgencias no basta con que exista, sino que se
	 * debe revisar si el estado a seguir en la valoracion de urgencias es cama de
	 * observacion
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idEpicrisis C�digo de la epicrisis (ingreso) que se desea buscar
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion existeEpicrisis (Connection con, int idEpicrisis, boolean esEpicrisisHospitalizacion) throws SQLException;

	/**
	 * Este m�todo es auxiliar a la mayor�a de rutinas de validaci�n. Su papel es revisar si existe o no un paciente.
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoId el tipo de identificacion que tiene el paciente (documentaci�n del paciente a verificar)
	 * @param numeroId el numero de identificacion que tiene el paciente (documentaci�n del paciente a verificar)
	 * @return un <code>boolean</code> con verdadero si el paciente existe o falso si no
	 */
	public boolean existePaciente (Connection con, String tipoId, String numeroId) throws SQLException;

	/**
	 * Este m�todo es auxiliar a la mayor�a de rutinas de validaci�n. Su papel es revisar si existe o no un paciente.
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param codigoPaciente el c�digo que tiene el paciente (documentaci�n del paciente a verificar)
	 * @return un <code>boolean</code> con verdadero si el paciente existe o falso si no
	 */
	public boolean existePaciente (Connection con, int codigoPaciente) throws SQLException;

	/**
	 * Este m�todo valida que se pueda insertar una cuenta
	 * para un paciente determinado, especificamente, que no
	 * exista otra cuenta para este mismo paciente en estado
	 * abierto (codigo 0)
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoId el tipo de identificacion que tiene el paciente al que se le va a crear un ingreso
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codigoInstitucion el c�digo de la instituci�n donde se crea esta
	 * cuenta
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public RespuestaValidacion validacionIngresarCuenta (Connection con,  String tipoId, String numeroId, String codigoInstitucion) throws SQLException;

	/**
	 * Este m�todo valida que se pueda insertar un ingreso
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
	public RespuestaValidacion validacionIngresarIngreso (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException;

	/**
	 * Este metodo valida que una persona que va a ser
	 * ingresado en el sistema no exista previamente
	 * A diferencia de la inserci�n en usuarios, en pacientes solo se puede
	 * presentar un caso, y es que ya exista un paciente con esa identificacion.
	 * La idea de este metodo es agrupar la validaci�n de medicos y pacientes
	 * que es practicamente la misma
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoId el tipo de identificacion que tiene el futuro paciente
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @param codDepartamento c�digo del departamento de la persona
	 * @param codCiudad c�digo de la ciudad de la persona
	 * @param codBarrio c�digo del barrio de la persona
	 * @param rolPersona tipo de persona: usuario, m�dico, paciente.
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public RespuestaValidacion validacionIngresarPersona (Connection con, String tipoId, String numeroId, String codBarrio, String rolPersona) throws SQLException;

	/**
	 * Este m�todo permite validar la necesidad de insertar un paciente en una
	 * instituci�n. Como el sistema permite manejar pacientes por instituci�n,
	 * en algunas ocasiones un paciente ingresar�a a una instituci�n que no lo
	 * tiene como paciente, sin embargo puede ocurrir que el paciente ya se
	 * encuentre en el sistema, en cuyo caso no se puede insertar sino
	 * �nicamente hay que dar permisos a la instituci�n que lo intenta ingresar.
	 * Este m�todo se encarga de dar permisos de 1er Nivel (Acceso a datos
	 * b�sicos del paciente).
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoPaciente El c�digo con el que se identifica el paciente
	 * en el sistema
	 * @param codigoInstitucion El codigo de la institucion que desea manejar el paciente
	 *
	 */
	public RespuestaValidacion validacionPermisosInstitucionPaciente (Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException;

	/**
	 * Este m�todo se encarga de dar permisos de 2do Nivel (Acceso a datos
	 * cl�nicos del paciente).
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoPaciente El c�digo con el que se identifica el paciente
	 * en el sistema
	 * @param codigoInstitucionBrinda El codigo de la institucion que brinda
	 * el permiso
	 * @param codigoInstitucionRecibe El codigo de la institucion que recibe
	 * el permiso
	 *
	 */
	public RespuestaValidacion validacionPermisosInstitucionPaciente2 (Connection con, int codigoPaciente, String codigoInstitucionBrinda, String codigoInstitucionRecibe) throws SQLException;

	/**
	 * M�todo que permite buscar el c�digo de un paciente dada
	 * su identificaci�n
	 *
	 * @param con una conexion abierta con una BD  o null si no
	 * se tiene
	 * @param codigoTipoIdentificacion C�digo del tipo de
	 * identificaci�n del paciente a buscar
	 * @param numeroIdentificacion N�mero de identificaci�n
	 * del paciente a buscar
	 * @return
	 * @throws SQLException
	 */
	public int getCodigoPersona (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion ) throws SQLException;
	
	/**
	 * M�todo que permite buscar el c�digo de un paciente dada
	 * su identificaci�n
	 *
	 * @param con una conexion abierta con una BD  o null si no
	 * se tiene
	 * @param codigoTipoIdentificacion C�digo del tipo de
	 * identificaci�n del paciente a buscar
	 * @param numeroIdentificacion N�mero de identificaci�n
	 * del paciente a buscar
	 * @return
	 * @throws SQLException
	 */
	public int buscarCodigoPaciente (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion );

	/**
	 * Este m�todo me permite saber si una instituci�n puede ver un paciente.
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoId el tipo de identificacion que tiene el paciente (documentaci�n del paciente a verificar)
	 * @param numeroId el numero de identificacion que tiene el paciente (documentaci�n del paciente a verificar)
	 * @param codigoInstitucion el codigo de la instituci�n que intenta revisar
	 * si hay o no paciente
	 * @return un <code>boolean</code> con verdadero si el paciente existe o falso si no
	 */
	public boolean puedoImprimirPaciente(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException;

	/**
	 * Este m�todo valida que se pueda insertar una admisi�n hospitalaria para
	 * un paciente determinado, especificamente, A- Que la cuenta a la que
	 * pertenece esta admisi�n hospitalaria est� abierta y  B - Que no haya
	 * ninguna admisi�n hospitalaria abierta para este paciente en esta
	 * instituci�n
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param codigoPaciente C�digo del paciente sobre el que se
	 * est� validando
	 * @param idCuenta C�digo de la cuente sobre la que se est�
	 * validando
	 * @param codigoInstitucion el c�digo de la instituci�n donde se crea esta
	 * cuenta
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 * @throws SQLException
	 */
	public RespuestaValidacion validacionIngresarAdmisionHospitalaria (Connection con,  int codigoPaciente, int idCuenta, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que permite averiguar si una cuenta espec�fica tiene
	 * Egreso
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneEgreso (Connection con, int idCuenta) throws SQLException;

	/**
	 * Para poder llenar una evoluci�n se deben cumplir las siguientes reglas:
	 * - Debe existir una valoraci�n anterior llena de este centro de costo
	 * - Debe ser un prof de la salud diferente enfermera (Esto se debe validar
	 * en el Action con el n�mero de autorizaci�n y la categor�a. Como NO
	 * necesita acceso a BD, NO se hace en este m�todo)
	 * - El centro de costo del m�dico debe haber hecho al menos una
	 * valoraci�n (debe estar llena)
	 * - La opci�n de salida solo sale si es m�dico tratante
	 * - Que no haya un egreso para la cuenta (Ya lleno orden de salida)
	 * (Hay egreso cuando hay una entrada en la tabla egresos y el medico
	 * NO es null)
	 * La diferencia entre los m�todos homonimos es que este recibe una cuenta
	 * y asume que est� abierta - (El objeto PersonaBasica garantiza esto).
	 *
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param paciente Paciente al cual se le desea ingresar una evoluci�n
	 * @param usuario Usuario que desea ingresar la evoluci�n
	 * 
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacionTratante validarIngresarEvolucion (Connection con, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException;

	/**
	 * Este m�todo se encarga de validar si se puede finalizar un egreso
	 * (Tomar los datos dados por el m�dico en la orden de salida y
	 * consolidarlos). Las reglas son las siguientes:
	 *
	 * -Debe  haber una entrada en la tabla egresos: Si no existe es porque
	 * el m�dico no ha dado orden de salida
	 * -El Usuario responsable debe estar en nulo: Si la tabla tiene alg�n
	 * valor diferente a nulo quiere decir que alguien ya finalizo el egreso.
	 * -El M�dico Responsable NO debe estar en nulo: Si lo esta es porque
	 * estamos en medio de una reversi�n de egreso y el m�dico no ha dado
	 * orden de salida
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta para la cual se quiere finalizar
	 * el egreso
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion puedoFinalizarEgreso (Connection con, int idCuenta) throws SQLException;

	/**
	 * Este m�todo se encarga de revisar si una cuenta tiene
	 * un egreso completo. Reglas para saber si un egreso est�
	 * completo o no:
	 * 1. Existe una entrada en la tabla egresos con este codigo
	 * 2. En esta entrada el medico no es nulo
	 * 3. En esta entrada el usuario no esnulo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @return
	 * @throws SQLException
	 */
	public boolean existeEgresoCompleto (Connection con, int idCuenta) throws SQLException;

	/**
	 * Este m�todo se encarga de revisar si una cuenta tiene
	 * un egreso autom�tico. Reglas para saber si un egreso
	 * es autom�tico o no:
	 * 1. Existe una entrada en la tabla egresos con este codigo
	 * 2. En esta entrada el campo es autom�tico est� en true
	 * Este m�todo retorna un objeto de tipo RespuestaValidacion
	 * donde su boolean define si existe un egreso autom�tico y
	 * el String el login del usuario que lo creo. El usuario o m�todo
	 * que llame a este m�todo sabe que si el login es nulo esta
	 * en medio de una reversi�n de egreso
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion existeEgresoAutomatico (Connection con, int idCuenta) throws SQLException;

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public boolean existeEgresoMedico(Connection con, int cuenta);
	
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
	 * @param codDepartamento c�digo del departamento del usuario
	 * @param codCiudad c�digo de la ciudad del usuario
	 * @param codBarrio c�digo del barrio del usuario
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public RespuestaValidacion validacionIngresarUsuario (Connection con, String login,  String tipoBD, String tipoId, String numeroId,  String codBarrio) throws SQLException;
	
	/**adici�n sebasti�n
	 * 
	 * Este metodo valida que el login de usuario que va a ser
	 * ingresado en el sistema no exista previamente
	 * 
	 * @param con
	 * @param login
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion validacionIngresarUsuario (Connection con, String login) throws SQLException;

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
	 * @param codDepartamento c�digo del departamento del usuario
	 * @param codCiudad c�digo de la ciudad del usuario
	 * @param codBarrio c�digo del barrio del usuario
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valido el insert que se piensa
	 */
	public RespuestaValidacion validacionModificarUsuario (Connection con, String tipoBD, String tipoId, String numeroId, String tipoIdViejo, String numeroIdViejo,  String codBarrio) throws SQLException;

	/**
	 * Este metodo valida que los cambios realizados a una persona
	 * (no usuario) durante una actualizacion o modificacion sean validos.
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codDepartamento El codigo del departamento de vivienda del m�dico
	 * @param codCiudad El codigo de la ciudad de vivienda del m�dico
	 * @param codBarrio El codigo del barrio de vivienda del m�dico
	 * @return un objeto <code>RespuestaValidacion</code> que dice si es valida la modificaci�n que se piensa
	 */
	public RespuestaValidacion validacionModificarPersonaNoUsuario (Connection con,  String tipoBD, String codBarrio) throws SQLException;

	/**
	 * Este metodo valida que se pueda insertar una historia
	 * Cl�nica, especificamente, que no exista una historia cl�nica para este
	 * mismo paciente
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el paciente al que se le va a abrir historia cl�nica
	 * @param numeroId el numero de identificacion que tiene el futuro paciente
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si es valido el insert que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public RespuestaValidacion validacionIngresarHistoriaClinica (Connection con,  String tipoBD, String tipoId, String numeroId) throws SQLException;

	/**
	 * Este metodo valida la posibilidad de cambiar el documento de un paciente
	 * Para ver si esto es posible o no, hay que revisar que el documento no
	 * este registrado por otra persona, si la persona a la que se piensa
	 * cambiar el documento existe, y que existe solo como paciente
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoIdNuevo nuevo tipo de identificaci�n que se desea establecer
	 * @param numeroIdNuevo nuevo n�mero de la identificaci�n que se desea establecer
	 * @param tipoIdViejo viejo tipo de la identificaci�n, que ya est� establecido
	 * @param numeroIdViejo viejo n�mero de la identificaci�n, que ya est� establecido
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si es valido el cambio que se piensa
	 * hacer y en caso de no poderse, se explica porque.
	 */
	public RespuestaValidacion validacionCambiarDocumento (Connection con, String tipoBD, String tipoIdNuevo, String numeroIdNuevo, String tipoIdViejo, String numeroIdViejo) throws SQLException;

	/**
	 * Este m�todo es auxiliar a la mayor�a de rutinas de validaci�n. Su papel
	 * es revisar si existe o no un m�dico.
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el m�dico
	 * (documentaci�n del m�dico a verificar)
	 * @param numeroId el numero de identificacion que tiene el m�dico
	 * (documentaci�n del m�dico a verificar)
	 * @return un <code>boolean</code> con verdadero si el m�dico existe o falso
	 * si no
	 */
	public boolean existeMedico (Connection con, String tipoBD, String tipoId, String numeroId) throws SQLException;

	/**
	 * Este m�todo es auxiliar a la mayor�a de rutinas de validaci�n. Su papel
	 * es revisar si existe o no un usuario.
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId el tipo de identificacion que tiene el usuario
	 * (documentaci�n del usuario a verificar)
	 * @param numeroId el numero de identificacion que tiene el usuario
	 * (documentaci�n del usuario a verificar)
	 * @return un <code>boolean</code> con verdadero si el usuario existe o
	 * falso si no
	 */
	public boolean existeUsuario (Connection con, String tipoBD, String tipoId, String numeroId) throws SQLException;

	/**
	 * M�todo que revisa si un codigo pertenece a una Admision Hospitalaria
	 * abierta hecha por la instituci�n espec�ficada
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codigoAdmision C�digo de la Admisi�n Hospitalaria a buscar
	 * @param codigoInstitucion Instituci�n desde la que hace la b�squeda
	 * @return boolean en verdadero si la admision hospitalaria dada existe y
	 * pertenece a esta instituci�n
	 * @throws SQLException
	 */
	public boolean existeAdmisionHospitalariaAbiertaCodigo (Connection con, String tipoBD, String codigoAdmision, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que me dice si una admisi�n hospitalaria existe y me devuelve en
	 * el texto de la admision hospitalaria el codigo de la misma (Esto �ltimo
	 * para hacer las cosas m�s uniformes con el resto de admisiones
	 * hospitalarias)
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param codigoAdmision C�digo de la Admisi�n Hospitalaria a buscar
	 * @param codigoInstitucion Instituci�n desde la que hace la b�squeda
	 * @return RespuestaValidacion
	 * @throws SQLException
	 */
	public RespuestaValidacion existeAdmisionHospitalariaCodigo (Connection con, String tipoBD, String codigoAdmision, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que revisa si existe una admisi�n hospitalaria abierta para un
	 * paciente dado y si existe devuelve (a traves del parametro textoRespuesta
	 * de RespuestaValidacion) el codigo de la admisi�n
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId Tipo de Identificaci�n del paciente al que se quiere buscar
	 * su Admision Hospitalaria
	 * @param numeroId N�mero de Identificaci�n del paciente al que se quiere
	 * buscar su Admision Hospitalaria
	 * @param codigoInstitucion Instituci�n desde la que hace la b�squeda
	 * @return RespuestaValidacion con true o false dependiendo si hay Admisi�n
	 * Hospitalaria abierta y si la hay, con el c�digo
	 * @throws SQLException
	 */
	public RespuestaValidacion existeAdmisionHospitalariaAbiertaPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que revisa si existe una admisi�n hospitalaria para un paciente
	 * dado y si existe devuelve (a traves del parametro textoRespuesta de
	 * RespuestaValidacion) el codigo de la admisi�n m�s reciente
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId Tipo de Identificaci�n del paciente al que se quiere buscar
	 * su Admision Hospitalaria
	 * @param numeroId N�mero de Identificaci�n del paciente al que se quiere
	 * buscar su Admision Hospitalaria
	 * @param codigoInstitucion Instituci�n desde la que hace la b�squeda
	 * @return RespuestaValidacion con true o false dependiendo si hay Admisi�n
	 * Hospitalaria y si la hay, con el c�digo de la m�s reciente
	 * @throws SQLException
	 */
	public RespuestaValidacion existeAdmisionHospitalariaPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException;

	/**
	 * Con el nuevo enfoque del sistema, ahora se desea que al crear un medico
	 * este se registre autom�ticamente como usuario. Esta acci�n
	 * puede ir mal si ya existe un usuario con ese codigo, si este m�dico tiene
	 * otro login, si no existe un m�dico con esta identificaci�n o si simplemente
	 * los datos son vacios.  Si en el futuro se agrega nueva funcionalidad
	 * no olvide que si la respuesta sale mal, debe incluir un numero y un guion
	 * antes del texto de la misma
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del m�dico
	 * @param numeroId El n�mero de identificacion del m�dico
	 * @param login El login que desea manejar el m�dico
	 * @param validarUsuario, es un indicador entero que regular ciertas validaciones que no deben hacerse
	 */
	public RespuestaValidacion validacionInsertarUsuarioMedico (Connection con, String tipoBD, String tipoId, String numeroId, String login,int validarUsuario) throws SQLException;

	/**
	 * Con el nuevo enfoque del sistema, ahora se desea que al crear un paciente
	 * este tenga la posibilidad que este se registre como usuario. Esta acci�n
	 * puede ir mal si ya existe un usuario con ese codigo, si este m�dico tiene
	 * otro login, si no existe un paciente con esta identificaci�n o si simplemente
	 * los datos son vacios.  Si en el futuro se agrega nueva funcionalidad
	 * no olvide que si la respuesta sale mal, debe incluir un numero y un guion
	 * antes del texto de la misma
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del paciente
	 * @param numeroId El n�mero de identificacion del paciente
	 * @param login El login que desea manejar el paciente
	 */
	public RespuestaValidacion validacionInsertarUsuarioPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String login) throws SQLException;

	/**
	 * Este m�todo permite validar la necesidad de insertar un medico en una instituci�n.
	 * Como el sistema permite manejar medicos por instituci�n, en algunas ocasiones un
	 * m�dico ingresar� a una instituci�n que no lo tiene , sin embargo puede ocurrir que
	 * este m�dico ya se encuentre en el sistema, en cuyo caso no se
	 * puede insertar sino �nicamente hay que dar permisos a la instituci�n que lo intenta
	 * ingresar. Este m�todo se encarga de hacer todo esto.
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param tipoId El tipo de identificacion del m�dico
	 * @param numeroId El n�mero de identificacion del m�dico
	 * @param codigoInstitucion El codigo de la institucion que desea manejar el m�dico
	 */
	public RespuestaValidacion validacionPermisosInstitucionMedico (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException;

	/**
	 * Este m�todo permite validar la necesidad de insertar un paciente en una
	 * instituci�n. Como el sistema permite manejar pacientes por
	 * instituci�n, en algunas ocasiones un paciente ingresar� a una
	 * instituci�n que no lo tiene como paciente, sin embargo puede ocurrir
	 * que el paciente ya se encuentre en el sistema, en cuyo caso no se puede
	 * insertar sino �nicamente hay que dar permisos a la instituci�n que lo
	 * intenta ingresar. Este m�todo se encarga de hacer todo esto.
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param tipoBD el nombre / tipo de la base de datos
	 * @param nuevoConsecutivo El n�mero con el que se desea que a partir de ahora
	 * se manejen las historias cl��nicas
	 */
	public RespuestaValidacion validacionCambioConsecutivoHistoriasClinicas (Connection con, String tipoBD, String nuevoConsecutivoStr ) throws SQLException;

	/**
	* Este m�todo permite averiguar si un m�dico en particular puede ser
	* activado
	*
	* @param con una conexion abierta con una BD  o null si no se tiene
	* @param tipoBD el nombre / tipo de la base de datos
	* @param tipoId El tipo de identificacion del m�dico
	* @param numeroId El n�mero de identificacion del m�dico
	* @param codigoInstitucion El codigo de la institucion que desea manejar el m�dico
	* */
	public RespuestaValidacion validacionActivarMedico (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que revisa si se puede cambiar la contrase�a de un usuario en
	 * particular (Debe ser de la misma instituci�n y NO puede ser el
	 * superadministrador)
	 *
	 * @param con una conexion abierta con una BD  o null si no se tiene
	 * @param login login del usuario que se desea cambiar
	 * @param codigoInstitucion Codigo de la instituci�n del usuario que quiere
	 * hacer este cambio
	 * @return RespuestaValidacion con true si se puede cambiar el password y en
	 * caso de false en textoRespuesta la explicaci�n del porque
	 * @throws SQLException
	 */
	public RespuestaValidacion validacionCambiarPasswordAdministrador (Connection con, String login, String codigoInstitucion,String passwd) throws SQLException;

	/**
	 * M�todo que consulta la fecha actual y la fecha de egreso
	 * para una cuenta, buscando verificar que se pueda hacer
	 * la reversi�n del egreso por tiempo
	 *
	 * @param ac_con Conexi�n con la fuente de datos
	 * @param ai_idCuenta C�digo de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator puedoReversarEgresoPorTiempo(Connection ac_con, int ai_idCuenta) throws SQLException;

	/**
	 * M�todo que recibe el c�digo de una persona y retorna el nombre
	 * de la misma. Si la persona no existe debe retornar el texto:
	 * "Persona inexistente en el sistema"
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoPersona C�digo de la persona
	 * @return
	 * @throws SQLException
	 */
	public String obtenerNombrePersona (Connection con, int codigoPersona) ;

	/**
	 * Este m�todo valida si para una combinaci�n de llaves de cuenta,
	 * admisi�n hospitalaria/urgencias existe una fecha de admisi�n/cuenta
	 * Si ni siquiera existe la cuenta especificada en el texto de RespuestaValidacion
	 * se explica la raz�n, si existe se devuelve la fecha en cuesti�n
	 *
	 * Reglas:
	 * - Si el a�o de admisi�n es mayor que 0, nos encontramos con una admisi�n
	 * de urgencias,
	 * - Si el a�o de admisi�n no es mayor que 0 pero el codigo de la admisi�n es
	 * mayor que 0, nos encontramos con una admisi�n de urgencias
	 * - Si el a�o de admisi�n no es mayor que 0 y el codigo de la admisi�n no es
	 * mayor que 0, nos encontramos con una cuenta sin admisi�n (o ninguna
	 * cuenta)
	 *
	 * @param con una conexion abierta con una BD
	 * @param idCuenta C�digo de la cuenta
	 * @param idAdmision C�digo de la admisi�n de urgencias (o de
	 * hospitalizaci�n si el a�o de admisi�n es 0)
	 * @param anioAdmision A�o de la admisi�n de urgencias (o 0 si no es
	 * de urgencias)
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion tieneFechaAdmisionCuenta (Connection con, int idCuenta, int idAdmision, int anioAdmision) throws SQLException;

	/**
	 * Este m�todo se encarga de validar si una admisi�n de urgencias tiene o no
	 * los datos de la cama de observaci�n (Antes de poder dar un egreso NO
	 * autom�tico el paciente debe tener como conducta a seguir cama de observaci�n
	 * asignada - V�a modificar admisi�n)
	 *
	 * @param con una conexion abierta con una BD
	 * @param idAdmision C�digo de la admisi�n de urgencias
	 * @param anioAdmision A�o de la admisi�n de urgencias
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneCamaParaEgresoUrgencias (Connection con, int idAdmision, int anioAdmision) throws SQLException;

	/**
	 * Este m�todo se encarga de revisar si se puede reversar un egreso.
	 * de urgencias. Las reglas para esto son:
	 *
	 * 0. Debe existir una admisi�n de urgencias para la cuenta especificada
	 * 1.Si es el m�dico tratante puede reversar el egreso si:
	 * 	1.1. Existe una entrada en la tabla egresos con este codigo
	 * 	1.2. En esta entrada el m�dico no es nulo
	 * 	(La condici�n de usuario no nulo no es necesaria, ya que esto pasa
	 * 	cuando el m�dico ha dado orden de salida y todav�a no se ha llenado
	 * 	orden de salida)
	 * 2. Si no es el m�dico tratante puede reversar si:
	 * 	2.1. Existe una entrada en la tabla egresos con este codigo
	 * 	2.2. En esta entrada el m�dico no es nulo
	 * 	2.3. En esta entrada el usuario no es nulo
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta N�mero de la cuenta para la cual se quiere revisar
	 * si tiene un egreso completo
	 * @param codigoCentroCosto C�digo del centro de costo del m�dico / usuario
	 * que desea saber si puede o no realizar la reversi�n
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacionTratante puedoReversarEgresoUrgencias(Connection ac_con, int ai_idCuenta, int ai_codigoCentroCosto) throws SQLException;

	/**
	 * Este m�todo se  encarga de revisar, dado el n�mero del ingreso,
	 * si se puede o no crear una cuenta en asocio
	 *
	 * @param con una conexion abierta con una BD o null si no se tiene
	 * @param idCuenta C�digo de la cuenta a la que se quiere revisar si
	 * est� asociada o no
	 * @return
	 * @throws SQLException
	 */
	public boolean puedoCrearCuentaAsocio (Connection con, int idIngreso) throws SQLException;

	/**
	 * Este m�todo revisa si una cuenta para un ingreso por hospitalizaci�n
	 * est� facturada o no. Si la cuenta no existe y/o no hay admisi�n hospitalaria,
	 * tira una excepci�n
	 *
	 * @param con una conexion abierta con una BD
	 * @param idCuenta C�digo de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public boolean validacionCuentaFacturada(Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException;

	/**
	 * Este m�todo revisa si hay alg�n centro de costo v�lido y si lo hay lo
	 * retorna en el campo textoRespuesta de RespuestaValidacion
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @return
	 */
	public RespuestaValidacion existeCentroCostoValido (Connection con) throws SQLException;

	/**
	 * M�todo que recibe un c�digo de persona (usuario) y una
	 * instituci�n y revisa si este paciente se puede ver en esta
	 * instituci�n. Retorna un ResultSet, el cual es analizado
	 * por el mundo
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoUsuario C�digo de la persona buscada
	 * @param codigoInstitucion C�digo de la instituci�n sobre
	 * la que se desea averiguar si tiene permiso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator esUsuarioPaciente (Connection con, int codigoUsuario, String codigoInstitucion) throws SQLException;

	/**
	 * Metodo para saber si un usuario esta activo en el sistema.
	 * @param con
	 * @param codigoPersona
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator esUsuarioActivo (Connection con, String login, int codigoInstitucion) throws SQLException;
	
	
	/**
	 * M�todo que devuelve un ResultSetDecorator con la informaci�n necesaria
	 * para saber si esta cuenta tiene un semiegreso
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta sobre la que se desea
	 * saber si tiene semiegreso o no
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator tieneSemiEgreso (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que permite averiguar para una cuenta y centro de
	 * costo dados, si el centro de costo es adjunto de la cuenta
	 * (obviamente debe ser v�lido al momento de realizar la
	 * pregunta)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta
	 * @param loginUsuario C�digo del centro de costo
	 * @return
	 * @throws SQLException
	 */
	public boolean esAdjuntoCuenta (Connection con, int idCuenta, String loginUsuario) throws SQLException;

	/**
	 * M�todo que revisa si existe una solicitud de transferencia
	 * de manejo (activa por supuesto) para una cuenta dada
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se va
	 * a realizar la revisi�n
	 * @return
	 * @throws SQLException
	 */
	public boolean existeSolicitudTransferenciaManejoPrevia(Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que revisa si existe una solicitud de transferencia
	 * de manejo (activa por supuesto) para una cuenta dada
	 * dirigida al centro de costo especificado
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se va
	 * a realizar la revisi�n
	 * @param codigoCentroCosto C�digo del centro de costo
	 * al que se hizo sol. cambio tratante (futuro "posible" tratante)
	 * @return
	 * @throws SQLException
	 */
	public int existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (Connection con, int idCuenta, int codigoCentroCosto) throws SQLException;

	/**
	 * M�todo que dice si existen valoraciones para una cuenta
	 * dada
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se van
	 * a buscar valoraciones
	 * @return
	 * @throws SQLException
	 */
	public boolean tieneValoraciones (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que dice si una cuenta tiene como v�a de ingreso
	 * consulta externa.
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se va
	 * revisar si tiene como v�a de ingreso consulta externa
	 * @return
	 * @throws SQLException
	 */
	public boolean esViaIngresoConsultaExterna (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que dice si existen solicitudes en estados diferentes
	 * a interpretar / anulado en el sistema para una cuenta y centro
	 * de costo dados
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se va
	 * revisar si tiene solicitudes en estados diferentes a interpretar
	 * / anulado
	 * @param codigoCentroCosto C�digo del centro de costo
	 * para el que se va a revisar si tiene solicitudes en estados
	 * diferentes a interpretar / anulado
	 * @param codigoCuentaAsociada C�digo de la cuenta asociada
	 * (si existe) 
	 * @return
	 * @throws SQLException
	 */
	public ResultadoBoolean haySolicitudesIncompletasAdjunto (Connection con, int idCuenta, int codigoCentroCosto, int codigoCuentaAsociada, int institucion) throws SQLException;

	/**
	 * M�todo que dice si existen solicitudes en estados diferentes
	 * a interpretar / anulado en el sistema para una cuenta dada
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la que se va
	 * revisar si tiene solicitudes en estados diferentes a interpretar
	 * / anulado
	 * @return
	 * @throws SQLException
	 */
	public ResultadoBoolean haySolicitudesIncompletasEnCuenta (Connection con, int codigoIngreso, int institucion, boolean esEvolucion, boolean validarMedicamentos) throws SQLException, BDException;

	/**
	* Indica si un servicio es excepcion al convenio de la cuenta.
	* @param	ac_con		Conexi�n a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @param	ai_servicio	C�digo del servicio
	* @return	true si el servicio esta paramatrizado como una excepci�n al convenio de la cuenta.
	*			false de lo contrario
	* @throws	SQLException
	*/
	public boolean esExcepcionServicio(
		Connection	ac_con,
		int			ai_cuenta,
		int			ai_servicio
	)throws SQLException;

	/**
	* Cierra el ingreso del paciente y deja la cuenta en estado excenta
	* @param	ac_con		Conexi�n a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @return	true Si la cuenta pudo ser cerrada de manera exitosa
	*			false de lo contrario
	* @throws	SQLException
	*/
	public boolean cerrarCuentaConsultaExterna(
		Connection	ac_con,
		int			ai_cuenta
	)throws SQLException;
	
	/**
	 * M�todo que revisa si existe un antecedente ped�atrico dado el c�digo
	 * del paciente
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoPaciente C�digo del paciente
	 * @return
	 * @throws SQLException
	 */
	public boolean existeAntecedentePediatrico(Connection con,int codigoPaciente)throws SQLException;
	
	/**
	 * M�todo que valida si se puede realizar un asocio de cuenta, 
	 * devolviendo en su campo de texto el c�digo del mensaje 
	 * en caso de ser negativo. Reglas a aplicar:
	 * 
	 * Debe ser cuenta de urgencias
	 * A- Si el usuario selecciono cond. seguir hospitalizaci�n y tiene
	 * egreso autom�tico completo, tiene permiso
	 * B- Si el usuario selecciono cond. seguir cama observ. y tiene
	 * egreso completo, tiene permiso.
	 * 
	 * En cualquier otro caso mandar c�digo del mensaje de error
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta en la que se realiza
	 * la validaci�n
	 * @return
	 * @throws SQLException
	 */
	public ResultadoBoolean validacionAsocioCuenta (Connection con, int idCuenta, String viaIngreso, String tipoPaciente) throws SQLException;
	
	/**
	 * M�todo que busca el diagnostico a poner en la valoraci�n 
	 * de hospitalizaci�n despu�s de un asocio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuentaAsociada C�digo de la cuenta en la que se realiza
	 * la b�squeda 
	 * @return
	 * @throws SQLException
	 */
	public String[] obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (Connection con, int idCuentaAsociada,String tipoCuenta) throws SQLException;
	
	/**
	 * M�todo que retorna el centro de costo dado el login m�dico
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoMedico
	 * @return
	 * @throws SQLException
	 */
	public int getCodigoCentroCostoDadoMedico (Connection con, String loginMedico) throws SQLException;
	
	/**
	 * M�todo que verifica si la cuenta tiene un adjunto
	 * @param cuenta
	 * @return
	 */
	public boolean existeManejoConjunto(Connection con, int cuenta, int centroCosto);
	
	/**
	 * Permite verificar que la cuenta tiene manejo conjunto activo de solicitudes de interconsulta
	 * @param cuenta
	 * @return
	 */
	public boolean existeManejoConjuntoActivoSolInterconsulta(Connection con, int cuenta, int centroCosto);
	
	/**
	 * M�todo que permite encontrar resultados de agenda que se 
	 * encuentren en estado activo y con la unidad de consulta que 
	 * llega de par�metro 
	 * @param con
	 * @param unidadConsulta
	 * @param centroAtencion
	 * @return
	 */
	public boolean existeValoresCancelarAgenda(	Connection con, int unidadConsulta,
																					String fechaInicial, String fechaFinal, 
																					String horaInicial, String horaFinal,
																					int consultorio, int dia, int codigoMedico, int centroAtencio,
																					String centrosAtencion, String unidadesAgenda);

	/**
	 * M�todo que permite saber si existe una valoraci�n pediatrica
	 * hospitalaria (m�todo de Consultar Ingreso)
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta para la se quiere 
	 * saber si existe una valoraci�n pediatrica hospitalaria
	 * @return
	 * @throws SQLException
	 */
	public int tieneValoracionPediatricaHospitalariaConsultarIngreso (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * m�todo que permite activar o no la modificaci�n 
	 * del n�mero de identificaci�n de terceros,
	 * �nicamente en los casos en los cuales este tercero 
	 * no ha sido utilizado en empresas.
	 * (a futuro se tiene que validar que no se utilice en ning�n lado)
	 * @param con, Conexi�n
	 * @param codigo, C�digo del tercero
	 * @return
	 */
	public boolean hanUtilizadoTercero (Connection con, int codigo);
	
	/**
	 * M�todo para conocer el c�digo de la valoraci�n inicial para una
	 * cuenta especifica, retorna 0 si no existe al solicitud o no la 
	 * valoraci�n no ha sido llenada
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public int buscarCodigoValoracionInicial (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Adici�n sebasti�n
	 * M�todo para conocer el c�digo de la solicitud de una valoraci�n inicial para una
	 * cuenta especifica, retorna 0 si no existe la solicitud 
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta
	 * @return
	 */
	public int buscarCodigoSolicitudValoracionInicial (Connection con, int idCuenta);
	
	/**
	 * M�todo que obtiene el nombre de un centro de costo
	 * dado su c�digo
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param codigoCentroCostoBuscado C�digo del centro
	 * de costo
	 * @return
	 * @throws SQLException
	 */
	public String getNombreCentroCosto (Connection con, int codigoCentroCostoBuscado) throws SQLException;
	
	/**
	 * M�todo que dado el c�digo de una cuenta devuelve 
	 * la fecha de admisi�n de la misma, si esta existe, 
	 * si no tiene admisi�n lanza una excepci�n 
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public String getFechaAdmision (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que dado el c�digo de una cuenta devuelve 
	 * la hora de admisi�n de la misma, si esta existe, 
	 * si no tiene admisi�n lanza una excepci�n 
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public String getHoraAdmision (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que revisa si se debe mostrar el mensaje 
	 * de la cancelaci�n del tratante antes de llenar 
	 * una evoluci�n 
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta
	 * @param centroCostoMedico Centro de costo del
	 * m�dico
	 * @return
	 * @throws SQLException
	 */
	public boolean deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (Connection con, int idCuenta, int centroCostoMedico) throws SQLException;
	
	/**
	 * Verificar si el paciente (codigoPaciente) tiene citas 
	 * atendidas por el m�dico (codigoMedico)
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param codigoMedico
	 * @param codigoPaciente
	 * @return
	 */
	public boolean tieneCitaRespondida(Connection con, int codigoMedico, int codigoPaciente);
	
	/**
	 * M�todo para verificar la existencia de cuenta
	 * asociada
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param ingreso
	 * @return codigo de la cuenta asociada si existe, 0 de lo contrario
	 */
	public int tieneCuentaAsociada(Connection con, int ingreso);
	
	/**
	 * M�todo para revisar si una cuenta o subcuenta
	 * tiene un estado definido
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param idCuenta C�digo de la cuenta
	 * @param estadoAVerificar C�digo del estado a
	 * verificar
	 * @param esSubCuenta Boolean que indica si es
	 * una subcuenta o no
	 * @return
	 * @throws SQLException
	 */
	public boolean cuentaOSubcuentaTieneEstadoDefinido (Connection con, int idCuenta, int estadoAVerificar, boolean esSubcuenta) throws SQLException;
	
	/**
	 * M�todo que dado el c�digo de una cuenta o
	 * subcuenta devuelve el nombre de su estado 
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param id C�digo de la cuenta o subcuenta
	 * @param esSubCuenta Boolean que indica si es
	 * una subcuenta o no
	 * @return
	 * @throws SQLException
	 */
	public String getNombreEstadoCuentaOSubcuenta(Connection con, int id, boolean esSubCuenta)throws SQLException;

	/**
	 * Metodo para mirar si una cuenta tiene un estado determinado
	 * @param con, Conexion
	 * @param id, Cuenta
	 * @return True si es igual el estado
	 */
	public boolean igualEstadoCuenta(Connection con, int id, int estado);
	
	/**
	 * M�todo que indica si la solicitud pasada por par�metros fue originada
	 * en la cuenta pasada por par�metros
	 * La comparaci�n se hace por tiempo
	 * @param con
	 * @param numeroSolicitud
	 * @param idCuenta
	 * @return true si la solicitud pertenece a la cuenta, false de lo contrario
	 */
	public boolean perteneceSolicitudACuentaComparacionXTiempo(Connection con, int numeroSolicitud, int idCuenta);

	/**
	 * M�todo para verificar si el paciente tiene o no evoluciones
	 * @param con Conexi�n con la BD
	 * @param idCuenta Codigo de la cuenta
	 * @return true si el paciente tiene evoluciones, false de lo contrario
	 */
	public boolean tieneEvoluciones(Connection con, int idCuenta);
	
	/**
	 * M�todo para validar que la cuenta dada no se encuentre en proceso de facturaci�n
	 * @param con Conexi�n con la BD
	 * @param codigoPaciente C�digo del paciente
	 * @param idSesionOPCIONAL, si viene cargado eso quiere decir que no vamos a filtrar por ese numero de sesion
	 * @return true si la cuenta est� en proceso de facturaci�n por otro usuario, false de lo contrario
	 */
	public boolean estaEnProcesofacturacion(Connection con, int codigoPaciente, String idSesionOPCIONAL);
	
	
	/**
	 * M�todo que verifica si la tarifa del art�culo ya existe
	 * @param con Conexi�n con la BD
	 * @param articulo C�digo del articulo
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public boolean existeTarifaParaArticulo(Connection con, int articulo, int esquemaTarifario);

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
	public String validacionExistePacienteMismoNombre(Connection con, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido);

	/**
	 * Metodo que retorna true si el articulo es un medicamento(independiente de que sea pos o no)
	 * @param conexion
	 * @param articulo
	 * @return boolean, respuesta true indica si es medicamento.
	 */
	public boolean esMedicamento(Connection con, int articulo);

	/**Metodo que retorna true si existe un tipo regimen y naturaleza
	 * @param con
	 * @param codigoRegimen
	 * @param codigoNaturaleza
	 * @return
	 */
	public boolean existeExcNatuRegimen(Connection con, String codigoRegimen, int codigoNaturaleza);

	/**
	 * Metodo que retorna un boolean indicanto si el diagnostico con ese tipo CIE ya existe en el sitema.
	 * @param con
	 * @param codigo
	 * @param tipoCie
	 * @return
	 */
	public boolean diagnosticoExistente(Connection con, String codigo, int tipoCie);

	/**
	 * M�todo para cargar el tipo de diagn�stico principal de la �ltima evoluci�n
	 * en caso de no existir evoluciones lo toma de la valoracio��n inicial
	 * @param con
	 * @param codigoCuenta
	 * Retorna el c�digo del tipo de diagn�stico
	 * @param revisarEnEvolucion
	 */
	public int obtenerTipoDiagnosticoPrincipal(Connection con, int codigoCuenta, boolean revisarEnEvolucion);

	/**
	 * M�todo para consultar si un d�a es festivo o no
	 * @param con Conexi�n con la BD
	 * @param fecha Fecha que se desea consultar
	 * @param incluirDomingos
	 * @return true si es d�a festivo
	 */
	public boolean esDiaFestivo(Connection con, String fecha, boolean incluirDomingos);

	/**
	 * Metodo que consulta una funcionalidad particular la cual no tenga una entrada en
	 * dependencias funcionalidades, entonces se filtra segun el rol y el codigo de la 
	 * funcionalidad
	 * @param con
	 * @param login
	 * @param codigoFuncionalidad
	 * @return devuelve el nombre de la funcionalidad con su path 
	 */
	public String funcionalidadADibujarNoEntradaDependencias(Connection con, String login, int codigoFuncionalidad);
	
	/**
	 * M�todo que verifica si la tarifa ISS del servicio ya existe
	 * @param con Conexi�n con la BD
	 * @param servicio C�digo del articulo
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public boolean existeTarifaISSParaServicio(Connection con, int servicio, int esquemaTarifario);
	
	/**
	 * M�todo que verifica si la tarifa SOAT del servicio ya existe
	 * @param con Conexi�n con la BD
	 * @param servicio C�digo del articulo
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public boolean existeTarifaSOATParaServicio(Connection con, int servicio, int esquemaTarifario);
		
	/**
	 * Metodo que determina si un usuario puede interpretar una solicitud de interconsulta o procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCostoUsuario
	 * @return
	 */
	public boolean puedoInterpretarSolicitudInterconsultaProcedimiento(Connection con, int numeroSolicitud, int codigoCentroCostoUsuario);

	/**
	 *Metodo que retorna el manejo en que se encuentra una solicitud espec�fica
	 *retornta true si la solicitud se encuentra en manejo conjunto
	 */
	public boolean isManejoConjunto(Connection con,int numeroSolicitud);


	/**
	 * Metodo que dado el centro de costo de un usuario, y el numero de solicitud me dice si la puedo modificar o no.
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCosto
	 * @return
	 **/
	public boolean medicoPuedeResponderSolicitud(Connection con, int numeroSolicitud, int codigoCentroCosto);
		
	/**
	 * Metodo que indica si una solicitud de interconsulta tiene manejo conjunto finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudManejoConjuntoFinalizado(Connection con, int numeroSolicitud);
	
	

	/**
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerIngreso(Connection con, int idCuenta);

	/**
	 * @param con
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 */
	public boolean existeCierreSalodosIniciales(Connection con, int institucion);

	/**
	 * M�todo que verifica si el paciente tiene antecedentes gineco-obstetricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public boolean existeAntecedenteGinecoobstetrico(Connection con, int codigoPersona);

	/**
	 * M�todo que verifica si el paciente tiene informaci�n hist�rica de antecedentes gineco-obst�tricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public boolean existeAntecedenteGinecoHisto(Connection con, int codigoPersona);

	/**
	 * M�todo que valida si la cuenta esta en proceso de distrobucion
	 * @param con
	 * @param codigoPersona
	 * @param loginUsuario
	 * @return
	 */
	public boolean estaEnProcesoDistribucion(Connection con, int codigoPersona, String loginUsuario);
	
	/**
	 * Metodo que devuelve el numero de autorizaci�n dado el codigo de institucion y un like por 'autorizaci'
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	//public int getCodNumeroAutorizacionAtributosSolicitud (Connection con, int codigoInstitucion);
	
	/**
	 * Metodo que devuelve la fecha de apertura de la cuenta
	 * @param con Connection
	 * @param idCuenta int
	 * @author jarloc
	 * @return fecha String
	 * @see com.princetonsa.dao.SqlBase.SqlBaseUtilidadValidacionDao#obtenerFechaAperturaCuenta (Connection,int ) 
	 */
	public String obtenerFechaAperturaCuenta (Connection con, int idCuenta);

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public boolean existeCuentaCobro(Connection con, double cuentaCobro, int institucion);

	/**
	 * @param con
	 * @param mes
	 * @param anio
	 * @param Institucion
	 * @return
	 */
	public boolean existeCierreMensual(Connection con, int mes, int anio, int Institucion);

	/**
	 * @param con
	 * @param mes
	 * @param anio
	 * @param Institucion
	 * @return
	 */
	public boolean fechaMenorIgualAFechaCierreSalodsIniciales(Connection con, int mes, int anio, int Institucion);
	
	/**
	 * M�todo que indica si la cama ha sido o no ocupada en el rango de fecha y hora igual o mayor al entregado 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoAxiomaCama
	 * @return
	 */
	public boolean esCamaOcupadaRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoAxiomaCama);
	
	/**
	 * M�todo que me indica si el paciente tiene un traslado posterior a la fecha - hora de uno nuevo
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoPaciente
	 * @return
	 */
	public boolean pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoPaciente);
	
	/**
	 * M�todo que verifica si una cuenta es de hospitalizaci�n
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @param estado de la cuenta
	 * @param diferente (true/false) que indica si se valida con
	 * el estado igual a diferente
	 * @param institucion
	 * @return
	 */
	public boolean esCuentaHospitalizacion(Connection con,int idCuenta,int estado,boolean diferente,int institucion);

	/**
	 * M�todo que verifica si una cuenta es de hospitalizaci�n
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean esCuentaHospitalizacion(Connection con,String idCuenta);
	
	/**
	 * Metodo que retorna true si una cuenta de cobro tiene ajustes en estado generado(pendientes)
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public boolean cuentaCobroAjustesPendientes(Connection con, double cuentaCobro, int institucion);

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public boolean cuentaCobroConFactEnAjustesPendiente(Connection con, double cuentaCobro, int institucion);
	
	/**
	 * metodo para obtener el estado de un ajuste especifico
	 * @param con Connection
	 * @param codigo int, c�digo del ajuste
	 * @param institucion int, c�digo de la instituci�n
	 * @return String, con el estado del ajuste y la descripcion
	 * separados por un @
	 * @author jarloc
	 */
	public String obtenerEstadoAjusteCartera(Connection con, double codigo,int institucion);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean esAjusteReversado(Connection con, double codigoAjuste);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean existeDistribucionAjusteNivelFacturas(Connection con, double codigoAjuste);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param factura
	 * @return
	 */
	public boolean existeDistribucionAjusteNivelServicios(Connection con, double codigoAjuste, int factura);

	/**
	 * M�todo para verificar si un centro de costo es SubAlmacen
	 * @param con Conexi�n con la BD
	 * @param centroCosto codigo del centro de costo a verificar
	 * @return true si el centro de costo es subalmacen
	 */
	public boolean esCentroCostoSubalmacen(Connection con, int centroCosto);

	/**
	 * Utilidad que verifica si una solicitud tiene art�culos sin cantidad
	 * @param con Conexi�n con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene art�culos sin cantidad, false de lo contrario
	 */
	public boolean esSolicitudSinCantidad(Connection con, int numeroSolicitud);
	
	/**
	 * Metodo que indica si ya existe la parametrizacion de tipos de asocios
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean esTiposAsociosParametrizados(Connection con, int institucion);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean cajaUtilizadaEnCajerosCaja(Connection con, int consecutivoCaja);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean cajaUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja);

	/**
	 * @param con
	 * @param consecutivoEntdadFinanciera
	 * @return
	 */
	public boolean entidadFinanceraUtilizadaEnTarjetasFinancieras(Connection con, int consecutivoEntdadFinanciera);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param logginUsuario
	 * @return
	 */
	public boolean cajaCajeroUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja, String logginUsuario);
	
	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param logginUsuario
	 * @return
	 */
	public boolean cajaCajeroConTurno(Connection con, int consecutivoCaja, String logginUsuario);

	/**
	 * @param con
	 * @param consecutivoTarjeta
	 * @return
	 */
	public boolean terjetaFinancieraUtilizadaEnMovimientosTarjetas(Connection con, int consecutivoTarjeta);

	/**
	 * @param con
	 * @param formaPago
	 * @return
	 */
	public boolean formaPagoUtilizadaEnRecibosCaja(Connection con, int formaPago);

	
	/**
	 * @param con
	 * @param numeroReciboCaja
	 * @param institucion
	 * @return
	 */
	public String existeReciboCaja(Connection con, String numeroReciboCaja, int institucion,int codigoCentroAtencion);
	
	/**
	 * Dice si esta parametrizado la cobertura de los accidentees de transito
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean existeParametrizacionCobreturaAccTransito(Connection con, int institucion);
	
	/**
	 * Dice si esta parametrizado el salario minimo
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean existeParametrizacionSalarioMinimo(Connection con);
	
	
	/**
	 * M�todo que indica si una cuenta o subcuenta tiene requisistos paciente por cuenta o subcuenta(distibucion) pendientes
	 * de entregar.
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucion
	 * @param codigoSubcuentaOCuenta
	 * @return
	 */
	public boolean faltanRequisitosPacienteXCuenta( Connection con,  String codigoSubcuentaOCuenta);
	
	/**
	 * M�todo usado para verificar si un m�dico se encuentra inactivo
	 * 
	 * @param con
	 * @param codigoMedico
	 * @param institucion
	 * @return
	 */
	public boolean estaMedicoInactivo(Connection con,int codigoMedico,int institucion);
	
	/**
	 * metodo que indica si un servicio tiene o no excepcion 
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public boolean esServicioConExcepcion(Connection con, String codigoConvenioStr, String codigoServicioStr);
	
	/**
	 * metodo que indica si un servicio es pos o no
	 * @param con
	 * @param codigoServicioStr
	 * @return
	 */
	public boolean esServicioPos(Connection con, String codigoServicioStr);
	
	/**
	 * metodo que indica si un servicio esta cubierto por el convenio
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public boolean esServicioCubiertoXConvenio(Connection con, String codigoConvenioStr, String codigoServicioStr);
	
	/**
	 * Metodo que verifica la existencia de los pagos parciales realizados por un paciente de una factura
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEstadoPago
	 * @param codigoAxiomaFactura
	 * @return
	 */
	public boolean existenPagosParcialesFacturaPaciente(Connection con,  int codigoInstitucion, int codigoEstadoPago, String codigoAxiomaFactura);
	
	/**
	 * M�todo para saber si un m�dico es de una especialidad espec�fica
	 * @param con
	 * @param codigoMedico
	 * @param codigoEspecialidad
	 * @return true si el m�dico es de la especialidad mandada como par�metro sino retorna false 
	 */
	public boolean esMedicoEspecialidad (Connection con, int codigoMedico, int codigoEspecialidad);
	
	/**
	 * M�todo que me indica si una peticion y�tiene asociada una solicitud 
	 * @param con Conexi�n con la base de Datos
	 * @param codigoPeticion Petici�n a evaluar
	 * @return int -1 En caso de error, 0 en caso de no existir orden asociada, o el n�mero de la orden asociada a la petici�n
	 */
	public int[] estaPeticionAsociada(Connection con, int codigoPeticion);

	/**
	 * metodo que indica si una hoja qx ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esHojaQxAsociadaSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * metodo que indica si una hoja anestesia ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esHojaAnestesiaAsociadaSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * metodo que indica si una nota recuperacion ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esNotaRecuperacionAsociadaSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * metodo que indica si una nota general enfermeria ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esNotaGeneralEnfAsociadaSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * metodo que indica si un consumo materiales ya tiene una solicitud asociada dado el numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esConsumoMaterialesAsociadaSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * Cargar la hora de apertura de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public String obtenerHoraAperturaCuenta(Connection con, int idCuenta);
	
	 /**
	 * Indica si la ocupacion de un profesional de la salud esta parametrizada como ocupaciones que realizan cirugias 
	 * @param con
	 * @param codigoOcupacion
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean esOcupacionQueRealizaCx(Connection con, int codigoOcupacion, int codigoInstitucion);

	/**
	 * Utilidad para verificar la existensia de la hoja de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeHojaAnestesia(Connection con, int numeroSolicitud);
	
	/**
	 * Utilidad para verificar la existensia de la hoja obst�trica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public boolean existeHojaObstetrica(Connection con, int codigoPaciente);
	
	/**
	 * M�todo que verifica si la cuenta tiene solicitudes de un tipo
	 * especifico, con un estado medico o de facturacion espec�fico
	 * @param con
	 * @param idCuenta
	 * @param tipo
	 * @param estadoMedico
	 * @param estadoFacturacion
	 * @return
	 */
	public boolean haySolicitudesEnCuenta(Connection con,int idCuenta,int tipo,int estadoMedico);
	
	/**
	 * M�todo implementado para verificar si una cirug�a requiere justificacion
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio (cirugia)
	 * @param contrato actual de la cuenta que contiene la solicitud
	 * @return
	 */
	public boolean cirugiaRequiereJustificacion(Connection con,int numeroSolicitud,int servicio,int contrato);
	
	
	/**
	 * Metodo para saber si una solcicitud de Procedimientos es multiple.
	 * @param con
	 * @param numeroSolicitud
	 * @return Retorna True Si la solicitud es multiple de lo contrario False. 
	 */
	public boolean esSolicitudMultiple(Connection con,int numeroSolicitud);
	
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
	public String[] revisarValidezContrato(Connection con, int cuenta);
	
	/**
	 * Utilidad para verificar la existencia de la hoja oftalmol�gica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public boolean existeHojaOftalmologica(Connection con, int codigoPaciente);

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
	public boolean existeMontoEnBD(Connection con, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha);

	/**
	 * 
	 * @param con
	 * @param profesional
	 * @param fecha
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean seCruzaAgendaMedicoFechaHora(Connection con, int profesional, String fecha, String horaInicial, String horaFinal);
	
	/**
	 * metodo que indica si se debe generar recibos de caja automaticos para
	 * una via de ingreso dada
	 * @param con
	 * @param codigoViaIngreso
	 * @return
	 */
	public boolean esReciboCajaAutomaticoViaIngresoDada(Connection con, int codigoViaIngreso);
	
	/**
	 * 
	 * M�todo que consulta el c�digo [pos 1] y la descripci�n [pos 2] del concepto tipo r�gimen
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param acronimoTipoRegimen
	 * @param tiposIngresoTesoreria
	 * @param esActivo
	 * @return
	 */
	public HashMap<Object, Object> getCodigoDescripcionConceptoTipoRegimen(Connection con, int codigoInstitucion, String acronimoTipoRegimen, Vector<Object> tiposIngresoTesoreria, boolean esActivo);
	
	/**
	 * M�todo para saber si existe un centro ed costo asociado a una via de ingreso 
	 * de una insitucion determinada
	 * @param con
	 * @param centroCosto
	 * @param via
	 * @return
	 */
	public boolean existeCentroCostoXViaIngreso(Connection con, int centroCosto, int viaIngreso, int institucion);
	
	/**
	 * M�todo implementado para consultar el codigo de area y el
	 * nombre del �rea del paciente, el formato es:
	 * codigoArea + constantesBD.separadorSplit + nombreArea
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getAreaPaciente(Connection con,int idCuenta);
	
	/**
	 * M�todo que verifica si un paciente est� muerto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean esPacienteMuerto(Connection con,int codigoPaciente);
	
	/**
	 * M�todo implementado para actualizar al paciente como muerto
	 * en la base de datos
	 * @param con
	 * @param codigoPaciente
	 * @param esVivo
	 * @param fechaMuerte
	 * @param estado
	 * @return
	 */
	public boolean actualizarPacienteAMuertoTransaccional(Connection con,int codigoPaciente,boolean esVivo,String fechaMuerte,String horaMuerte,String certificadoDefuncion, String estado);

	/**
	 * M�todo para verificar la existencioa de cuentas asociadas a un contrato
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean existeCuentasAbiertasAsociadasAContrato(Connection con, int codigoContrato);

	/**
	 * 
	 * @param con
	 * @param consecutivoCategoria
	 * @return
	 */
	public boolean categoriaTriageUtilizadaEnSignosSintomasSistema(Connection con, int consecutivoCategoria);
	
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
	public String[] getConsecutivosTriage(Connection con, String acronimoTipoIdentificacion, String numeroIdentificacion, String codigoCentroAtencion, int codigoInstitucion);
	
	/**
	 * M�todo para saber si un triage determinado para una institucion tiene
	 * un numero de admision asignado
	 * @param con
	 * @param consecutivoTriage
	 * @param consecutivoFechaTriage
	 * @param institucion
	 * @return
	 */
	public boolean existeAdmisionParaTriage(Connection con, String consecutivoTriage, String consecutivoFechaTriage, int institucion);

	/**
	 * 
	 * @param con
	 * @param consecutivoCategoria
	 * @return
	 */
	public boolean categoriaTriageUtilizadaEnTriage(Connection con, int consecutivoCategoria);

	/**
	 * 
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param codigoAjuste
	 * @return
	 */
	public boolean cuentaCobroConFactEnAjustesPendienteDiferenteActual(Connection con, double cuentaCobro, int institucion, double codigoAjuste);
	
	/**
	 * M�todo implementado para verificar si un paciente tiene cuentas Urgencias
	 * en estado Cuenta Activa o Asociada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean tieneCuentaUrgenciasAbierta(Connection con,String codigoPaciente);
	
	/**
	 * M�todo para saber si una solicitud de cirugia que ya tiene hoja de quirurgica
	 * tiene la participacion de anestesiologo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean solCxNecesitaHojaAntesia(Connection con, int numeroSolicitud);

	/**
	 * Utilidad que obtiene los datos del m�dico que guard� la hoja
	 * de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String getDatosMedicoHojaAnestesia(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param codTipoPrograma
	 * @param institucion
	 * @return
	 */
	public boolean esTipoProgramaPYPUsado(Connection con, String codTipoPrograma, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean existeProgramaSaludPYP(Connection con, String codigo, int institucion);
	
	/**
	 * metodo que verifica la ecistencia de cierre saldos iniciales capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean existeCierreSaldosInicialesCapitacion(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @return
	 */
	public boolean existeRelacionProgramaActividadPYP(Connection con, int institucion, String programa, String actividad);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esActividadProgramaUsaoEnActProConvenios(Connection con, String codigo);	
	
	
	
	/**
	 * existe cuenta cobro capitada
	 * @param con
	 * @param cuentaCobroCapitada
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existeCuentaCobroCapitada(Connection con, String cuentaCobroCapitada, int codigoInstitucion);
	
	/**
	 * M�todo que verifica si un convenio es de PYP
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioPYP(Connection con,String codigoConvenio);

	/**
	 * M�todo que obtiene el a�o y mes de cierre saldo de capitaci�n
	 * de acuerdo a los par�metros enviados 
	 * @param institucion
	 * @param tipoCierre
	 * @return String[]={a�o_cierre, mes_cierre}
	 * 					sino exite return vacio String[]={"",""}
	 */
	public String[] obtenerFechaMesCierreSaldoCapitacion(Connection con, int institucion, String tipoCierre);
	
	/**
	 * M�todo que verifica si un paciente ya tiene informaci�n de PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean tienePacienteInformacionPYP(Connection con,HashMap campos);

	/**
	 * Utilidad para verificar si un paciente tiene registros de ordenes ambulatorias
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return true si existe sino retorna false
	 */
	public boolean tienePacienteOrdenesAmbulatorias(Connection con, int codigoPaciente, int codigoInstitucion);

	
	/**
	 * 
	 * @param con
	 * @param consecutivoGrupo
	 * @param institucion
	 * @return
	 */
	public boolean puedoEliminarGrupoEtareoCreDes(Connection con, String consecutivoGrupo, String institucion);

	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public boolean esConvenioCapitado(Connection con, int convenio);

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean esAjusteDistribuidoCompletamete(Connection con, double codigoAjuste);
	
	/**
	 * M�todo implementado para verificar que el grupo de la actividad
	 * tenga centros de costo parametrizados en la funcionalidad
	 * Centros Costo x Grupo Servicios con centro de atencion que se encuentren
	 * en Actividades por centro atencion
	 * @param con
	 * @param consecutivoActividad
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public boolean existeCentroCostoXGrupoServicioActividad(Connection con,String consecutivoActividad,int centroAtencion,int institucion);
	
	/**
	 * M�todo que verifica si el paciente tiene antecedentes vacunas
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public boolean existeAntecedenteVacunas (Connection con, int codigoPersona);
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean esAccidenteTransito(Connection con, int idIngreso);
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean esAccidenteTransitoEstadoDado(Connection con, int idIngreso, String estado);

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public boolean esServicioMultiple(Connection con, String codigoServicio);

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion 
	 * @return
	 */
	public boolean esServicioViaIngresoCargoSolicitud(Connection con, String viaIngreso, String servicio, String institucion) throws BDException;

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @return
	 */
	public boolean esServicioViaIngresoCargoProceso(Connection con, String viaIngreso, String servicio,String institucion) throws BDException;

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean estipoTransInvUsada(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public Vector obtenerSolicitudesTipoServicioPartosCesarea(Connection con, String cuenta, String cuentaAsociada);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public boolean existeInfoRecienNacido(Connection con, String consecutivoCx);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public boolean existeInfoPartosFinalizada(Connection con, String codigoPaciente, String consecutivoCx);
	
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
	public ArrayList existeEvolucionesRangoFechas(Connection con, int idCuenta,  String fechaInicial, String fechaFinal, int idCuentaAsociada);

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public boolean pacienteTieneIngresoAbierto(Connection con, int codigoPersona);

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public String obtenerFechaEgresoUltimoIngresoPaciente(Connection con, int codigoPersona);

	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public String obtenerFechaFacturaCuenta(Connection con, int codigoPersona);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public boolean articuloTieneCantidadUnidosisActiva(Connection con, int codigoArticulo); 
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public boolean articuloTieneCantidadUnidosisActivaDadoUnidosis(Connection con, int codigoUnidosi);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudMedicamentos(Connection con, String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param clase
	 * @return
	 */
	public boolean esClaseInventarioUsada(Connection con, int clase);

	/**
	 * 
	 * @param con
	 * @param grupo
	 * @return
	 */
	public boolean esClaseGrupoInventarioUsado(Connection con, int grupo,int clase);

	/**
	 * 
	 * @param con
	 * @param subGrupo
	 * @param clase
	 * @param grupo
	 * @return
	 */
	public boolean esClaseGrupoSubGrupoInventarioUsado(Connection con, int subGrupo, int clase, int grupo);

	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public boolean esPaqueteUsado(Connection con, String codigoPaquete, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean esConceptoUsado(Connection con, String codigo, int institucion);
	
	/**
	 *  esNaturalezaArticulosUsado
	 */
	public boolean esNaturalezaArticulosUsado(Connection con, String codigo, int institucion);
	
	/**
	 *  esViasIngresoUsado
	 */
	public boolean esViasIngresoUsado(Connection con, int codigo);
	
	/**
	 *  esUbicacionGeograficaUsado
	 */
	public boolean esUbicacionGeograficaUsado(Connection con, String codigo);
	
	/**
	 *  esDepartamentoUsado
	 */
	public boolean esDepartamentoUsado(Connection con, String codigo_departamento, String codigo_pais);
	
	/**
	 *  esCiudadUsado
	 */
	public boolean esCiudadUsado(Connection con, String codigo_ciudad,String codigo_departamento, String codigo_pais);
	
	/**
	 *  esLocalidadUsado
	 */
	public boolean esLocalidadUsado(Connection con, String codigo_localidad, String codigo_ciudad,String codigo_departamento, String codigo_pais);
	
	/**
	 * Es tipos de monedas usado
	 * @param con
	 * @param codigo
	 * @param localidad
	 * @return
	 */
	public boolean esTiposMonedaUsado(Connection con,int codigo,int institucion);
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Centros de Costos  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroCostos
	 * @param int institucion
	 * */
	public boolean esCentroCostoUsado(Connection con,String codigoCentroCosto, int institucion);
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el consecutivo de Centros de Atencion  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroAtencion
	 * @param int institucion
	 * */
	public boolean esCentroAtencionUsado(Connection con, String codigoCentroAtencion, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	
	public boolean esInclusionesExclusionesUsado(Connection con, String codigo, int institucion);

	
	
	public boolean esPaqueteConvenioUsado(Connection con, int codigo);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public boolean esEventoCatastrofico(Connection con, int codigoIngreso);

	/**
	 * metodo que indica si por ingreso existe evento catastrofico en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean esEventoCatastroficoEstadoDado(Connection con, int idIngreso, String estado);

	/**
	 * Verifica que otras funcionalidades no manejen el codigo de tipos convenio (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public boolean esTiposConvenioUsado(Connection con, String codigo, int institucion);
	
	
	public boolean esServiciosEsteticosUsado(Connection con, String codigo, int institucion);
	
	
	/** 
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public boolean esCoberturaUsado(Connection con, String codigoCobertura, int institucion);
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Condicion de toma de Examen (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public boolean esCondicionTmExamenUsado(Connection con, int codigoExamen, int institucion);
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de pisos (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoPiso
	 * @param int institucion
	 * */
	public boolean esPisosUsado(Connection con, int codigo);
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de tipo habitacion (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public boolean esTipoHabitacionUsado(Connection con, String codigo, int institucion);
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Almacen Parametros (true = no depende , false = depende)
	 * @param Connection con
	 * @param int codigo
	 * @param int institucion
	 * */
	public boolean esAlmacenParametroUsado(Connection con, int codigo,int institucion);
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de habitaciones (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoHabitac
	 * @param int institucion
	 * */
	public boolean esHabitacionesUsado(Connection con, int codigo);
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo Instituciones SIRC  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoInstSirc
	 * @param int institucion
	 * */
	public boolean esInstitucionSircUsado(Connection con, String codigoInstSirc, int institucion); 
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de tipos usuario cama (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigo
	 * */
	public boolean esTiposUsuarioCamaUsado(Connection con, int codigo, int institucion);
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Asocio (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoAsocio
	 * @param int institucion
	 * */
	public boolean esAsocioSalaCirugiaUsado(Connection con, String codigoAsocio, int institucion);
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del detalle de Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * @param String codigo Encabezado
	 * */
	public boolean esDetallePorcentajeCxMultiUsado(Connection con, int codigo, int codigo_encab);
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del encabezado del Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param int institucion
	 * @param String codigo Encabezado 
	 * */
	public boolean esEncaPorcentajeCxMultiUsado(Connection con, int institucion, int codigo_encab);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public boolean esContrarreferencia(Connection con, int codigoIngreso);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @return
	 */
	public boolean esContrarreferenciaEstadoDado(Connection con, int idIngreso, String estado);

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean convenioManejaComplejidad(Connection con, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	public boolean esNumeroCuentaCobroUsado(Connection con, double numeroCuentaCobro);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esConvenioUsado(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int esCuentaValida(Connection con, int codigoCuenta);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoEstado
	 * @return
	 */
	public boolean responsableTieneServicioEstado(Connection con, String subCuenta, int codigoEstado);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param usuario 
	 * @return
	 */
	public boolean ingresoEstaEnProcesoDistribucion(Connection con, int codigoIngreso, String usuario);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean esVigenciaConvenioUsado(Connection con,int codigo,int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esAsocioXUvrTipoSalaUsado(Connection con,int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esAsociosXUvrUsado(Connection con,int codigo);
	
	/**
	 * 
	 * @param con
	 * @param tipoBD
	 * @param tipoId
	 * @param numeroId
	 * @return
	 * @throws SQLException
	 */
	public boolean existePacientes (Connection con, String tipoBD, String tipoId, String numeroId);
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public boolean pacienteTieneMovimientoAbonos(Connection con, int codigoPersona);
	
		
	/**
	 * M�todo para verificar si el paciente tiene o no evoluciones para el d�a en que se genera la orden
	 * @param con
	 * @param codigoCuenta
	 * @param fecha
	 * @return
	 */
	public boolean tieneEvolucionesParaElDia(Connection con, int codigoCuenta, String fecha);
	
	
	/**
	 * Valida si las notas de enfermeria esta cerrada
	 * @param Connection con
	 * @param int codigoCuenta
	 * */
	public boolean esCerradaNotasEnfermeria(Connection con, int codigoCuenta);

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String[] obtenerFechaYHoraCuidadoEspecial(Connection con, int idCuenta);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean tipoMonitoreoRequiereValoracion(Connection con, int idCuenta);

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean tieneValoracionCuidadoEspecial(Connection con, int idCuenta);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean tieneIngresoActivoCuidadoEspecial(Connection con, int idCuenta);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean monitoreoEvolucionRequiereValoracion(Connection con, int codigoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public boolean conductaSeguirUltimaEvolucion(Connection con, int codigoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public boolean tieneIngresoCuidadosFinalizado(Connection con, int codigoIngreso);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean pacienteFalleceCirugia(Connection con, int codigoCuenta);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean estadoSolicitudAnulacion(Connection con, int codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	public boolean usuarioAutorizadoAnular(Connection con, String loginUsuario, int centroAtencion);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean facturaAprobadaAnular(Connection con, int codigoFactura);
	

	/**
	 * Metodo encargado de verificar si un concepto especifico es 
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public boolean esConceptoEspecificoUsado(Connection con, String codigo, String institucion) ;
	
	/**
	 * verifica si el paciente se encuentra como paciente con capitacion vigente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString esPacienteCapitadoVigente(Connection con, HashMap parametros);

	/**
	 * M�todo que indica si un contrato se 
	 * esta usando en otras funcionalidades 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean esContratoUsado(Connection con, int codigoContrato);
	
	/**
	 * Metodo encargado de verificar si un texto respuesta procedimientos esta
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public boolean esTextoRespuestaProcedimientosUsado(Connection con, String codigo, String institucion) ;
	
	/**
	 * 
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public boolean esGrupoEspecialUsado(Connection con, String codigoGrupo);

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean esConceptoAjusteUsado(Connection con, String codigoAjuste, int codigoInstitucion);
	
	/**
	 * Metodo encargado de idenficar si un consentimiento informado
	 * ha sido usado o no
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean esConsentimientoInformadoUsado(Connection con, String codigo, int institucion);

	/**
	 * Metodo encargado de consultar el ultimo ingreso
	 * del paciente y verificar si este tuvo tipo evento
	 * catastr�fico
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int esUltimoIngreso(Connection con, String codigoPaciente); 
	
	/**
	 * Metodo encargado de verificar si un textos respuesta procedimientos esta siendo utilizado o no.
	 * @param con Connection
	 * @param codArticulo String
	 * @param institucion int
	 * */
	public boolean esArticuloUsado(Connection con, String codArticulo, String institucion);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public String obtenerMaximoConsecutivoJustificacionNoPosArticulos(Connection con);

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public String obtenerMaximoConsecutivoJustificacionNoPosServicios(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param codigoUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean esUsuarioAUtorizadoAnulacionUsado(Connection con, String codigoUsuario, int codigoInstitucion);

	public boolean validarPacienteEnValoracion(Connection con, int codigoPersona);

	/**
	 * M�todo que valida si el paciente es un deudor o tiene asociado un deudor
	 * de cartera paciente 
	 * @param con
	 * @param paciente
	 * @return true en caso de que sea o tenga asociado un deudor
	 */
	public boolean esDeudorCarteraPaciente(Connection con, PersonaBasica paciente);

	/**
	 * M�todo que consulta el saldo de cartera paciente
	 * @param con
	 * @param paciente
	 * @return valor del saldo
	 */
	public double consultarSaldoPaciente(Connection con, PersonaBasica paciente);

	public boolean verificarBloqueoIngresoPacienteODeudor(Connection con, PersonaBasica paciente, int viaIngreso);

	public boolean tieneCuotasPendientes(Connection con, PersonaBasica paciente, int numeroDiasMora);

	public boolean tieneAutorizacionIngresoSaldoPendiente(Connection con, PersonaBasica paciente, int viaIngreso, String codigoTipoPaciente);

	public boolean existeConvencionMedico(Connection con, int convencion);

	public boolean validarPacienteEnValoracionDiferenteUsuario(Connection con,int codigoPersona, String loginUsuario);

	public boolean esConsecutvioReciboCajaUsado(int institucion,
			String valorConsecutivo);

	public boolean esNaturalezaValidaTipoRegimen(int natPaciente,
			String tipoRegimen);
	
	/**
	 * M�todo que verifica si para ese egreso su estado de salida fue muerto
	 * @param con
	 * @param idCuenta
	 * @param idEvolucion
	 * @return
	 * @author javrammo
	 * @since ipsm_1.1.1
	 */
	public boolean esEvolucionConOrdenSalidaPacienteMuerto(Connection con,int idCuenta, int idEvolucion);	
}