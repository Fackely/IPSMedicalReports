/*
 * @(#)TagDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.Answer;
import util.ResultadoCollectionDB;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase define la <i>interface</i> de servicios de acceso a base de datos usada por las clases util.tag.*, que
 * implementan  los <code>custom tags</code> de la aplicacion.
 *
 * @version 1.0, Sep 21, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface TagDao 
{

	/**
	 * Dada una conexion abierta con una fuente de datos (si no existe, la crea) y una cadena con una
	 * consulta arbitraria, la ejecuta y retorna el <code>ResultSet</code> y la conexion en un objeto <code>Answer</code>.
	 * @param con una conexion abierta con una fuente de datos
	 * @param consulta una cadena de texto con una consulta arbitraria
	 * @return un objeto <code>Answer</code> con el resultado de la consulta y la conexi�n abierta
	*/
	public Answer resultadoConsulta (Connection con, String consulta) throws SQLException;

	/**
	 * Dada una conexion abierta con una fuente de datos (si no existe, la crea) y una cadena con una
	 * actualizaci�n arbitraria. Su funci�n es ejecuta esta.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param actualizacion una cadena de texto con una actualizaci�n arbitraria
	 * @return un <code>int</code> con el n�mero de actualizaciones hechas
	*/
	public int resultadoActualizacion (Connection con, String actualizacion) throws SQLException;

	/**
	 * M�todo que busca un valor por defecto dependiendo del 
	 * par�metro dado
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param parametro Parametro al cual se le desea buscar su
	 * valor por defecto
	 * (TagValoresPorDefecto)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagValoresPorDefecto (Connection con, String parametro) throws SQLException;
	
	/**
	 * M�todo para mostrar errores por usuario inactivo al momento
	 * de hacer login (El password debe ser previamente hasheado con
	 * MD5)
	 * (TagErroresIngreso)
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param login login del usuario a revisar
	 * @param passwordHasheado Password hasheado con MD5
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagErroresIngreso (Connection con, String login, String passwordHasheado) throws SQLException;
	

	/**
	 * M�todo auxiliar para la b�squeda del tipo de cie 
	 * v�lido dada una fecha 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param fecha Fecha para la que se busca un tipo de Cie
	 * V�lido
	 * @return
	 * @throws SQLException
	 */
	public int consultaTagBusquedaDiagnosticos_Cie (Connection con, String fecha) throws SQLException;
	
	/**
	 * M�todo auxiliar para la b�squeda de fecha en el 
	 * caso de estar trabajando con una valoraci�n de 
	 * consulta externa 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String consultaTagBusquedaDiagnosticos_FechaCita (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * M�todo que permite realizar b�squedas sobre diagnosticos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param criterioBusquedaDiagnostico Texto con el criterio de
	 * la b�squeda
	 * @param buscarTexto Boolean que indica si se debe buscar por
	 * texto o por c�digo
	 * @param codigoTipoCie C�digo del tipo cie donde se desea buscar
	 * los diagnosticos
	 * @param codigoFiltro @todo
	 * @param codigoInstitucion @todo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaDiagnosticos(Connection con, String criterioBusquedaDiagnostico, boolean buscarTexto, int codigoTipoCie, int codigoFiltro, int codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que permite realizar b�squedas entre personas (La 
	 * diferencia entre este m�todo y consultaTagBusquedaPersonasStruts 
	 * es reciben en el tipo de persona buscada cosas diferentes)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersonaBuscada A trav�s de este par�metro se define
	 * que se desea buscar (usuario?, paciente?, prof. salud?)
	 * @param criteriosBusqueda Arreglo de String's donde llegan todos
	 * los criterios de b�squeda utilizados por el usuario
	 * @param numeroIdentificacion Restricci�n por el n�mero de 
	 * Identificaci�n 
	 * @param codigoTipoIdentificacion Restricci�n por el c�digo del 
	 * tipo de Identificaci�n 
	 * @param primerNombrePersona Restricci�n por el primer nombre
	 * de la persona
	 * @param segundoNombrePersona Restricci�n por el segundo nombre
	 * de la persona
	 * @param primerApellidoPersona Restricci�n por el primer apellido
	 * de la persona
	 * @param segundoApellidoPersona Restricci�n por el segundo apellido
	 * de la persona
	 * @param fechaNacimiento Restricci�n de fecha de nacimiento
	 * @param direccion Restricci�n por direcci�n
	 * @param telefono Restricci�n por tel�fono
	 * @param codigoCiudad Restricci�n por codigo de ciudad de vivienda
	 * @param codigoCiudadIdentificacion Restricci�n por codigo de ciudad
	 * donde se expidi� la identificaci�n
	 * @param codigoDepartamento Restricci�n por codigo de departamento
	 * de vivienda
	 * @param codigoDepartamentoIdentificacion Restricci�n por codigo del
	 * departamento donde se expidi� la identificaci�n
	 * @param email Restricci�n por email
	 * @param codigoInstitucion Instituci�n en la que se est� realizando esta
	 * b�squeda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaPersonas(Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, String email, String codCargo, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que dada una identificaci�n devuelve el login usado por
	 * el usuario con esa identificaci�n
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroIdentificacion N�mero de identificaci�n del 
	 * usuario buscado
	 * @param codigoTipoIdentificacion C�digo del tipo de identificaci�n 
	 * del usuario buscado
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaPersonas_Login (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion) throws SQLException;

	/**
	 * M�todo que permite realizar b�squedas entre personas (La 
	 * diferencia entre este m�todo y consultaTagBusquedaPersonas 
	 * es reciben en el tipo de persona buscada cosas diferentes)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersonaBuscada A trav�s de este par�metro se define
	 * que se desea buscar (usuario?, paciente?, prof. salud?)
	 * @param criteriosBusqueda Arreglo de String's donde llegan todos
	 * los criterios de b�squeda utilizados por el usuario
	 * @param numeroIdentificacion Restricci�n por el n�mero de 
	 * Identificaci�n 
	 * @param codigoTipoIdentificacion Restricci�n por el c�digo del 
	 * tipo de Identificaci�n 
	 * @param primerNombrePersona Restricci�n por el primer nombre
	 * de la persona
	 * @param segundoNombrePersona Restricci�n por el segundo nombre
	 * de la persona
	 * @param primerApellidoPersona Restricci�n por el primer apellido
	 * de la persona
	 * @param segundoApellidoPersona Restricci�n por el segundo apellido
	 * de la persona
	 * @param fechaNacimiento Restricci�n de fecha de nacimiento
	 * @param direccion Restricci�n por direcci�n
	 * @param telefono Restricci�n por tel�fono
	 * @param codigoCiudad Restricci�n por codigo de ciudad de vivienda
	 * @param codigoCiudadIdentificacion Restricci�n por codigo de ciudad
	 * donde se expidi� la identificaci�n
	 * @param codigoDepartamento Restricci�n por codigo de departamento
	 * de vivienda
	 * @param codigoDepartamentoIdentificacion Restricci�n por codigo del
	 * departamento donde se expidi� la identificaci�n
	 * @param email Restricci�n por email
	 * @param codigoInstitucion Instituci�n en la que se est� realizando esta
	 * b�squeda
	 * @return
	 * @throws SQLException
	 */
	public Collection consultaTagBusquedaPersonasStruts (
		Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, 
		String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, 
		String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, 
		String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, 
		String email, String codCargo, String codigoInstitucion, String numeroHistoriaClinica, String codigoPais, String codigoPaisIdentificacion,String fichaUsuarioCapitado,String accion,String numeroIngreso) throws SQLException;
	
	/**
	 * M�todo que dice si en una evolucion en particular fu� en la que
	 * se vi� el motivo de reversi�n de egreso
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idEvolucion C�digo de la evoluci�n sobre la que se desea
	 * buscar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagDeboMostrarDatosMotivoReversion(Connection con, int idEvolucion) throws SQLException;
	
	/**
	 * M�todo que busca informaci�n b�sica sobre un tipo de 
	 * admisiones (o los dos tipos, dependiendo de lo especificado en
	 * el par�metro modo) para un paciente en particular
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroIdBusqueda N�mero de identificaci�n del 
	 * paciente al que se le quieren buscar sus admisiones
	 * @param tipoIdBusqueda Codigo del tipo de identificaci�n 
	 * del paciente al que se le quieren buscar sus admisiones
	 * @param modo Define si se quiere buscar admisiones 
	 * hospitalarias "hospitalarias", de urgencias "urgencias" o
	 * de los tipos "general" 
	 * @param codigoInstitucion C�digo de la instituci�n donde
	 * se desea realizar la b�squeda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAdmisiones (Connection con, String numeroIdBusqueda, String tipoIdBusqueda, String modo, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que se encarga de buscar las admisiones hospitalarias. 
	 * Tiene dos posibles criterios de b�squeda, si se especifica el idBusqueda
	 * se busca por c�digo de admisi�n y si no se busca por paciente
	 * (tipoIdBusqueda y numeroIdBusqueda)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idBusqueda C�digo de la admisi�n donde se van a
	 * buscar las admisiones hospitalarias
	 * @param tipoIdBusqueda Codigo del tipo de identificaci�n 
	 * del paciente al que se le quieren buscar sus admisiones
	 * hospitalarias
	 * @param numeroIdBusqueda N�mero de identificaci�n del 
	 * paciente al que se le quieren buscar sus admisiones 
	 * hospitalarias
	 * @param codigoInstitucion C�digo de la instituci�n donde
	 * se desea realizar la b�squeda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAdmisionesHospitalarias (Connection con, String idBusqueda, String tipoIdBusqueda, String numeroIdBusqueda, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que busca todos los barrios de una ciudad dados los codigos
	 * de departamento y ciudad
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codDepartamento C�digo del departemento en el que 
	 * desean buscar los barrios
	 * @param codCiudad C�digo de la ciudad en la que desean buscar 
	 * los barrios
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraBarrios (Connection con, String codDepartamento, String codCiudad, String nombre) throws SQLException;
	
	/**
	 * M�todo que busca todas las camas que cumplan con las 
	 * restricciones establecidas (Hombre no se puede quedar en
	 * hab. para mujeres, etc). Existen algunas diferencias entre
	 * este m�todo  y consultaTagMuestraCamasStruts; fueron
	 * desarrolladas en diferentes grupos de trabajo y optimizados
	 * para una admisi�n en particular, en este caso de hospitalizaci�n
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param sexo C�digo del sexo de la persona a la que se busca
	 * cama
	 * @param sinUrgencias boolean que define si deben aparecer o
	 * no las camas de urgencias
	 * @param conDesinfeccion boolean que indica si deben aparecer
	 * o no las camas en desinfecci�n
	 * @param codigoInstitucion
	 * @param fechaMovimiento
	 * @param horaMovimiento
	 * @param centroAtencion
	 * @param viaIngreso 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraCamas (
		Connection con, String sexo, String sinUrgencias, boolean conDesinfeccion, String codigoInstitucion, 
		String fechaMovimiento, String horaMovimiento,String centroAtencion,String viaIngreso) throws SQLException;

	/**
	 * M�todo que busca todas las camas que cumplan con las 
	 * restricciones establecidas (Hombre no se puede quedar en
	 * hab. para mujeres, etc). Existen algunas diferencias entre
	 * este m�todo  y consultaTagMuestraCamasStruts; fueron
	 * desarrolladas en diferentes grupos de trabajo y optimizados
	 * para una admisi�n en particular, en este caso de urgencias
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param sexo C�digo del sexo de la persona a la que se busca
	 * cama
	 * @param codigoCentroCosto C�digo del centro de costo por el
	 * que se desea restringir ("" vacio si se quieren ver todos)
	 * @param codEstado
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @param viaIngreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraCamasStruts (Connection con, String sexo, String codigoCentroCosto, String codEstado, 
		String codigoInstitucion,String centroAtencion,String viaIngreso) throws SQLException;
	
	/**
	 * M�todo que recibe el c�digo de una evoluci�n y devuelve
	 * (si existen) los datos m�s importantes de una orden de 
	 * salida (Diagnostico de muerte, destino a la salida, estado 
	 * a la salida)
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param idEvolucion C�digo de la evolucion en la que se
	 * desean mostrar los datos de la evoluci�n
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestaDatosSalidaEvolucion (Connection con, int idEvolucion) throws SQLException;

	/**
	 * M�todo que se encarga de averiguar el diagnostico de ingreso
	 * para la valoraci�n, almacenado durante la creaci�n de la 
	 * admisi�n
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoAdmision C�digo de la admisi�n donde se desea
	 * buscar el diagnostico de ingreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticosAdmisionHospitalizacion (Connection con, int codigoAdmision) throws SQLException;
	
	/**
	 * M�todo que se encarga de averiguar el diagnostico de ingreso
	 * para diferentes objetos (Val. Egreso, etc) almacenado durante
	 * la creaci�n de la valoraci�n hospitalaria inicial. La diferencia con
	 * consultaTagMuestraDiagnosticoValoracionHospitalaria es el
	 * criterio manejado, en esta consulta se busca por ingreso.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoIngreso C�digo del ingreso para el cual se desea
	 * buscar el diagnostico de ingreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (Connection con, int codigoIngreso) throws SQLException;
	
	/**
	 * M�todo que se encarga de averiguar el diagnostico de ingreso
	 * para diferentes objetos (Val. Egreso, etc) almacenado durante
	 * la creaci�n de la valoraci�n hospitalaria inicial. La diferencia con
	 * consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso es
	 * el criterio manejado, en esta consulta se busca por cuenta.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoIngreso C�digo de la cuenta para la cual se desea
	 * buscar el diagnostico de ingreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalaria (Connection con, int codigoCuenta) throws SQLException;
	
	/**
	 * M�todo que se encarga de buscar la informaci�n correspondiente
	 * a todas las ciudades que se encuentran en el sistema
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDosTablasCiudadDepartamento (Connection con) throws SQLException;

	/**
	 * M�todo que se encarga de buscar todos los estratos sociales para
	 * un tipo de regimen espec�fico
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoTipoRegimen C�digo del tipo de regimen bajo 
	 * el cual se van a buscar todos los estratos sociales
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraEstratoSocial (Connection con, String codigoTipoRegimen, boolean restringirPorActivo, int codigoEstratoMadre) throws SQLException;
	
	/**
	 * M�todo que se encarga de buscar todas las epicrisis existentes que
	 * cumplan con:
	 * 
	 * - Ser del paciente identificado con numeroIdentificacionPaciente y
	 * codigoTipoIdentificacionPaciente
	 * - Si el modo es accesoTotal busca con el minimo nivel de privilegio
	 * (todas las que pueda al menos ver), si el modo es accesoRestringido
	 * �nicamente a las que pueda a�adir nota / definir
	 * - Si mostrarHospitalizacion es true, muestra todas las epicrisis de 
	 * hospitalizaci�n, si es false muestra solo las de urgencias
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param paciente Objeto de tipo PersonaBasica sobre el
	 * que se quiere consultar
	 * @param codigoCentroCostoMedico C�digo del centro de costo del
	 * m�dico que est� accediendo a la funcionalidad (para revisi�n de 
	 * permisos)
	 * @param modo Define si se quieren buscar las epicrisis solo para 
	 * revisarlas (visualizacion) o para cambios (definici�n/nota)
	 * @param mostrarHospitalizacion Define si se quieren mostrar las
	 * epicrisis de hospitalizaci�n (true) o las de urgencias (false)
	 * @param mostrarSoloActual boolean que indica si se debe mostrar
	 * solo las epicrisis actuales
	 * @param mostrarSoloNOActual que indica si se debe mostrar
	 * solo las epicrisis actuales
	 * @param codigoInstitucion C�digo de la instituci�n del usuario 
	 * (o m�dico)
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraIngresosEpicrisis (Connection con, PersonaBasica paciente, int codigoCentroCostoMedico, String modo, boolean mostrarHospitalizacion, boolean mostrarSoloActual, boolean mostrarSoloNoActual, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que busca todas las instituciones (con nombre y datos),
	 * omitiendo la inactiva (instituci�n dummy usada para manejar
	 * usuarios y m�dicos inactivos) 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraInstituciones (Connection con) throws SQLException;
	
	/**
	 * M�todo que busca todas las posibles naturaleza que no incluyan
	 * embarazo (en caso hombres)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param sexo Sexo de la persona a la que se est� buscando
	 * posibles naturalezas
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (Connection con, String sexo) throws SQLException;

	/**
	 * M�todo que busca el nombre de la naturaleza del paciente 
	 * con c�digo 1 (embarazo, gravidez...)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza1 (Connection con) throws SQLException;
	
	/**
	 * M�todo que busca el nombre de la naturaleza del paciente 
	 * con c�digo 0 (ninguno ...)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza0 (Connection con) throws SQLException;

	/**
	 * M�todo que busca el nombre de la naturaleza del paciente 
	 * con c�digo 3 (especifico para mayores de 65 a�os)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Mayores65 (Connection con) throws SQLException;

	/**
	 * M�todo que busca el nombre de la naturaleza del paciente 
	 * con c�digo 3 (especifico para ni�os con menos de un a�o)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Bebe (Connection con) throws SQLException;
	
	/**
	 * M�todo que se encarga de buscar los examenes f�sicos que se deben
	 * mostrar / se pueden trabajar dependiendo del tipo de valoraci�n 
	 * usado
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoEspecialidad C�digo de la primera especialidad a
	 * usar en la validaci�n
	 * @param codigoEspecialidad2 C�digo de la segunda especialidad a
	 * usar en la validaci�n
	 * @param codigoEspecialidad3 C�digo de la tercera especialidad a
	 * usar en la validaci�n
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraOpcionesExFisicosStruts (Connection con, String codigoEspecialidad,String codigoEspecialidad2,String codigoEspecialidad3) throws SQLException;
	
	/**
	 * M�todo que busca personas de acuerdo al tipo especificado en
	 * tipoPersona (paciente, medico, usuario, acompanante, 
	 * responsable). Tiene en cuenta la instituci�n y los permisos de
	 * compartir. Aunque comparte la b�squeda con 
	 * consultaTagMuestraPersonasNombreApellidoStruts, recibe
	 * diferentes par�metros (este tag recibe tipos de persona exclusivamente
	 * mientras el otro realiza busquedas por tipos de persona y accion a
	 * realizar), luego las b�squedas son diferentes
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersona Tipo de persona a buscar. Valores v�lidos
	 * (paciente, medico, usuario, acompanante, responsable)
	 * @param codigoInstitucion C�digo de la instituci�n del usuario
	 * que est� realizando la b�squeda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraPersonasBasicasStruts (Connection con, String tipoPersona, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que busca personas de acuerdo al tipo especificado en
	 * tipoPersona (MostrarSoloMedicos, MostrarAcompanantes,
	 * MostrarMedicos, MostrarMedicosDesactivacion, MostrarMedicosActivacion,
	 * MostrarPacientes, MostrarUsuarios, MostrarUsuariosDesactivacion,
	 * MostrarUsuariosActivacion, MostrarUsuariosInactivos). Tiene en 
	 * cuenta la instituci�n y los permisos de compartir. Aunque comparte 
	 * la b�squeda con consultaTagMuestraPersonasBasicasStruts, recibe
	 * diferentes par�metros (este tag recibe tipos de persona y accion a
	 * realizar mientras el otro realiza busquedas por tipos de persona 
	 * exclusivamente), luego las b�squedas son diferentes 
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersona Tipo de persona a buscar. Valores v�lidos
	 * (paciente, medico, usuario, acompanante, responsable)
	 * @param codigoInstitucion C�digo de la instituci�n del usuario
	 * que est� realizando la b�squeda
	 * @param codigoCentroCosto
	 * @param x 
	 * @param x 
	 * @return
	 * @throws SQLException
	 */
	public Vector<String> consultaTagMuestraPersonasNombreApellidoStruts (Connection con, String tipoPersonaBuscada, String codigoInstitucion, String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos, String separador) throws SQLException;
	
	/**
	 * M�todo que busca personas de acuerdo al tipo especificado en
	 * tipoPersona (MostrarSoloMedicos, MostrarAcompanantes,
	 * MostrarMedicos, MostrarMedicosDesactivacion, MostrarMedicosActivacion,
	 * MostrarPacientes, MostrarUsuarios, MostrarUsuariosDesactivacion,
	 * MostrarUsuariosActivacion, MostrarUsuariosInactivos). Tiene en 
	 * cuenta la instituci�n y los permisos de compartir. Aunque comparte 
	 * la b�squeda con consultaTagMuestraPersonasBasicasStruts, recibe
	 * diferentes par�metros (este tag recibe tipos de persona y accion a
	 * realizar mientras el otro realiza busquedas por tipos de persona 
	 * exclusivamente), luego las b�squedas son diferentes 
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersona Tipo de persona a buscar. Valores v�lidos
	 * (paciente, medico, usuario, acompanante, responsable)
	 * @param codigoInstitucion C�digo de la instituci�n del usuario
	 * que est� realizando la b�squeda
	 * @param codigoCentroCosto
	 * @param x 
	 * @param x 
	 * @return
	 * @throws SQLException
	 */
	public String consultaTagMuestraPersonasNombreApellido (Connection con, String tipoPersonaBuscada, String codigoInstitucion, String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos) throws SQLException;
	
	
	/**
	 * M�todo que busca los posibles centros de costo a los cuales se puede
	 * solicitar una interconsulta
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCuenta C�digo de la cuenta abierta en la cual
	 * se va a solicitar la interconsulta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraPosiblesInterconsultas (Connection con, int codigoCuenta) throws SQLException;
	
	
	/**
	 * M�todo que muestra los posibles tipos de afiliado
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraTipoAfiliado (Connection con) throws SQLException;
	
	/**
	 * M�todo que dado un codigo de ingreso y definiendo si se quiere 
	 * buscar hospitalizaci�n o urgencias da el c�digo de la cuenta que 
	 * corresponde a la admisi�n definida por el usuario (codigo de la 
	 * cuenta de la adm. hosp. si usuario definio esHospitalizacion en 
	 * true y c�digo de la cuenta de adm. urg de lo contrario)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso Id del ingreso al cual se quiere buscar cuenta
	 * dada restricci�n de esHospitalizacion
	 * @param esHospitalizacion Define si se quiere buscar la cuenta
	 * correspondiente a la admisi�n de urgencias / hospitalizacion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException;
	
	/**
	 * M�todo que dada una cuenta, busca todas las valoraciones de 
	 * hospitalizaci�n que pueden ser asociadas a una evoluci�n de
	 * esta cuenta (SIN validar todav�a)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones de hospitalizaci�n a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que dada una cuenta, busca todas las valoraciones de 
	 * urgencias que pueden ser asociadas a una evoluci�n de
	 * esta cuenta (SIN validar todav�a)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones de urgencias a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que dada una cuenta, busca todas las valoraciones de 
	 * interconsulta que pueden ser asociadas a una evoluci�n de
	 * esta cuenta (SIN validar todav�a)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones de interconsulta a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection con, int idCuenta) throws SQLException;

	/**
	 * M�todo que dada una cuenta, busca todas las valoraciones  
	 * (hospitalizaci�n, interconsulta, urgencias) que pueden ser 
	 * asociadas a una evoluci�n de esta cuenta (SIN validar todav�a)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones (hospitalizaci�n, interconsulta, urgencias)
	 * a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones (Connection con, int idCuenta, int idCuentaAsociada) throws SQLException;
	
	/**
	 * M�todo que dado un n�mero de solicitud, busca ciertos datos
	 * de esta, incluyendo si es de hospitalizaci�n, urgencias o 
	 * interconsulta
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroSolicitud n�mero de la solicitud a la que
	 * pertenece la valoraci�n a usar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion2 (Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * M�todo que dado un medico y una funcionalidad devuelve la informacion de campos dinamicos asociada a esa funcionalidad
	 * permitiendo editarla
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idMedico id del profesional de la salud
	 * @param idFuncionalidad   funcionalidad desde donde se esta invocando el tag
	 * @param codigoCentroCosto C�digo del centro de costo
	 * @param codigoInstitucion C�digo de la instituci�n
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagEditaCamposDinamicos (Connection con, int idMedico, String idFuncionalidad, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	/**
			 * M�todo que dado un medico y una funcionalidad devuelve la informacion de campos dinamicos asociada a esa funcionalidad
			 * 
			 * 
			 * @param con una conexion abierta con una fuente de datos
			 * @param idMedico id del profesional de la salud
			 *  @param idFuncionalidad   funcionalidad desde donde se esta invocando el tag
			 * @return
			 * @throws SQLException
			 */
	public ResultSetDecorator consultaTagMuestraCamposDinamicos (Connection con,  String numSolicitud) throws SQLException;

	/**
	 * M�todo que dado un medico , una funcionalidad y una seccion dentro de esa funcionalidad 
	 * devuelve la informacion de campos dinamicos asociada a esa funcionalidad para esa seccion
	 * 
	 * 
				 * @param con una conexion abierta con una fuente de datos
				 * @param idMedico id del profesional de la salud
				 *  @param idFuncionalidad   funcionalidad desde donde se esta invocando el tag
				 * @param idSeccion   seccion de la funcionalidad desde donde se esta invocando el tag
				 * @return
				 * @throws SQLException
				 */
	public ResultSetDecorator consultaTagMuestraCamposDinamicosSeccion (Connection con,  String numSolicitud, String idSeccion) throws SQLException;
	
	/**
	 * M�todo que dado un medico,   una funcionalidad y una seccion dentro de esa funcionalidad,
	 * devuelve la informacion de campos dinamicos asociada a esa funcionalidad
	 * permitiendo editarlos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idMedico id del profesional de la salud
	 * @param idFuncionalidad   funcionalidad desde donde 
	 * se esta invocando el tag
	 * @param idSeccion   seccion de la funcionalidad desde 
	 * donde se esta invocando el tag
	 * @param codigoCentroCosto C�digo del centro de costo
	 * @param codigoInstitucion C�digo del m�dico
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagEditaCamposDinamicosSeccion (Connection con, int idMedico, String idFuncionalidad,String idSeccion, int codigoCentroCosto, String codigoInstitucion) throws SQLException;

	/**
	 * M�todo que se encarga de buscar todos los convenios que se puedan
	 * seleccionar dada la instituci�n
	 * 
	 * @param con Conexi�n con la fuente de datos 
	 * @param codigoInstitucion C�digo de la instituci�n que busca los
	 * convenios
	 * @param fechaReferencia
	 * @param capitado
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraConvenios (Connection con, String codigoInstitucion, String fechaReferencia,String capitado) throws SQLException;
	
	/**
	 * M�todo que permite buscar un Servicio
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoServicioBuscado C�digo del ipo del Servicio
	 * buscado
	 * @param criterioBusqueda Criterio por el que se est� buscando
	 * @param esPorNombre Boolean que indica si la b�squeda se 
	 * est� haciendo por nombre
	 * @param buscarEnTodosLosTarifarios Boolean que indica que
	 * si se ignora el parametro anterior y se busca en todos los tarifarios
	 * @param buscarConFormulario Boolean que indica si se buscan
	 * procedimientos con tarifario o no
	 * @param codigoSexo C�digo del sexo del paciente
	 * @param restringirPorFormulario Indica si la restricci�n por 
	 * formulario aplica o no
	 * @param idCuenta N�mero de la cuenta en la que se piensa
	 * dejar esta solicitud de servicios
	 * @param ocupacionSolicitada Ocupaci�n seleccionada
	 * @param porCodigoCUPS 
	 * @param offset 
	 * @param limit 
	 * @param codigoTarifarioBusqueda C�digo del tarifario en el que
	 * se quiere hacer la b�squeda
	 * @param codigoConvenio C�digo del convenio del paciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaTagBusquedaServicios(Connection con, char tipoServicioBuscado, String criterioBusqueda, boolean esPorNombre, boolean buscarEnTodosLosTarifarios, boolean buscarConFormulario, int codigoSexo, int codigoContrato, boolean restringirPorFormulario, int idCuenta, String ocupacionSolicitada, boolean porCodigoCUPS, int institucion, int offset, int limit) throws SQLException;
	
	/**
	 * M�todo que hace una b�squeda sencilla en servicios 
	 * (Sin muchas restricciones)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param criterioBusqueda Criterio por el que se est� buscando
	 * @param codigoTarifarioBusqueda C�digo del tarifario en el que
	 * se quiere hacer la b�squeda
	 * @param esPorNombre Boolean que indica si la b�squeda se 
	 * est� haciendo por nombre
	 * @param buscarSoloActivos boolean para especificar si se 
	 * buscan solo los servicios activos
	 * @param incluirPaquetes 
	 * @param porCodigoCUPS 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaSencillaServicios (Connection con, String criterioBusqueda, int codigoTarifarioBusqueda, boolean esPorNombre, boolean buscarSoloActivos, boolean incluirPaquetes, boolean porCodigoCUPS) throws SQLException;
	
	/**
	 * M�todo que permite realizar b�squedas sobre las especialidades
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param criterioBusqueda Texto con el criterio de
	 * la b�squeda de la especialidad
	 * @param buscarTexto Boolean que indica si se debe buscar por
	 * texto o por c�digo  
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 */
	public ResultadoCollectionDB consultaTagBusquedaEspecialidades(Connection con, String criterioBusqueda, boolean buscarTexto);
	
	/**
	 * Devulve una collecci�n de las funcionalidades que se deben dibujar
	 * @param con
	 * @param login
	 * @param funcionalidadPadre
	 * @param fincionalidadesHijas
	 * @param modo
	 * @return
	 */
	public ResultadoCollectionDB funcionalidadesADibujar(Connection con, String login, int funcionalidadPadre, String fincionalidadesHijas, boolean modo);
	
	/**
	 * Metodo que busca los atributos de la justificacion de una solicitud
	 * @param con
	 * @param mostrarAtributosArticulo
	 * @param institucion Instituci�n para el filtrado
	 * @return Collection con los resultados de la b�squeda
	 */
	public Collection consultaTagMuestraAtributos(Connection con, boolean mostrarAtributosArticulo, int institucion);
	
	/**
	 * Devuelve un resultset de las enfermeras que se encuentran activas
	 * @param con
	 * @return
	 */
	public ResultSetDecorator consultaTagMuestraEnfermerasActivas(Connection con, String restriccion, String codigoInstitucion);

	/**
	 * M�todo para buscar los diagnoticos NANDA
	 * @param con
	 * @param esPorCodigo
	 * @param criterio
	 * @param institucion @todo
	 * @param codigosNoBuscados @todo
	 * @return
	 */
	public Vector buscarNanda(Connection con, boolean esPorCodigo, String criterio, int institucion, String codigosNoBuscados); 
	
	/**
	 * Metodo para consultar si un diagnostico es de importancia en salud publica y debe ser diligenciado
	 * en una ficha de vigilancia epidemiologica
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public ResultSetDecorator consultaTagDiagnosticoSaludPublica(Connection con, String acronimo);
	
	/**
     * Metodo para consultar si hay fichas de vigilancia epidemiologica pendientes por completar,
     * hacer seguimiento o por notificar
     * @param con
     * @param codigoUsuario
     * @return
     */
	public ResultSetDecorator consultaHayFichasPendientes(Connection con, String codigoUsuario);
	
	/**
	 * Metodo para consultar los usuarios de la funcionalidad administrador de epidemiologia
	 * @param con
	 * @return
	 */
	public ResultSetDecorator consultaUsuariosEpidemiologia(Connection con);
}
