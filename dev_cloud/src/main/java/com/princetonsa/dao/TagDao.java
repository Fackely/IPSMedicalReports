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
	 * @return un objeto <code>Answer</code> con el resultado de la consulta y la conexión abierta
	*/
	public Answer resultadoConsulta (Connection con, String consulta) throws SQLException;

	/**
	 * Dada una conexion abierta con una fuente de datos (si no existe, la crea) y una cadena con una
	 * actualización arbitraria. Su función es ejecuta esta.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param actualizacion una cadena de texto con una actualización arbitraria
	 * @return un <code>int</code> con el nùmero de actualizaciones hechas
	*/
	public int resultadoActualizacion (Connection con, String actualizacion) throws SQLException;

	/**
	 * Método que busca un valor por defecto dependiendo del 
	 * parámetro dado
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
	 * Método para mostrar errores por usuario inactivo al momento
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
	 * Método auxiliar para la búsqueda del tipo de cie 
	 * válido dada una fecha 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param fecha Fecha para la que se busca un tipo de Cie
	 * Válido
	 * @return
	 * @throws SQLException
	 */
	public int consultaTagBusquedaDiagnosticos_Cie (Connection con, String fecha) throws SQLException;
	
	/**
	 * Método auxiliar para la búsqueda de fecha en el 
	 * caso de estar trabajando con una valoración de 
	 * consulta externa 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String consultaTagBusquedaDiagnosticos_FechaCita (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método que permite realizar búsquedas sobre diagnosticos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param criterioBusquedaDiagnostico Texto con el criterio de
	 * la búsqueda
	 * @param buscarTexto Boolean que indica si se debe buscar por
	 * texto o por código
	 * @param codigoTipoCie Código del tipo cie donde se desea buscar
	 * los diagnosticos
	 * @param codigoFiltro @todo
	 * @param codigoInstitucion @todo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaDiagnosticos(Connection con, String criterioBusquedaDiagnostico, boolean buscarTexto, int codigoTipoCie, int codigoFiltro, int codigoInstitucion) throws SQLException;
	
	/**
	 * Método que permite realizar búsquedas entre personas (La 
	 * diferencia entre este método y consultaTagBusquedaPersonasStruts 
	 * es reciben en el tipo de persona buscada cosas diferentes)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersonaBuscada A través de este parámetro se define
	 * que se desea buscar (usuario?, paciente?, prof. salud?)
	 * @param criteriosBusqueda Arreglo de String's donde llegan todos
	 * los criterios de búsqueda utilizados por el usuario
	 * @param numeroIdentificacion Restricción por el número de 
	 * Identificación 
	 * @param codigoTipoIdentificacion Restricción por el código del 
	 * tipo de Identificación 
	 * @param primerNombrePersona Restricción por el primer nombre
	 * de la persona
	 * @param segundoNombrePersona Restricción por el segundo nombre
	 * de la persona
	 * @param primerApellidoPersona Restricción por el primer apellido
	 * de la persona
	 * @param segundoApellidoPersona Restricción por el segundo apellido
	 * de la persona
	 * @param fechaNacimiento Restricción de fecha de nacimiento
	 * @param direccion Restricción por dirección
	 * @param telefono Restricción por teléfono
	 * @param codigoCiudad Restricción por codigo de ciudad de vivienda
	 * @param codigoCiudadIdentificacion Restricción por codigo de ciudad
	 * donde se expidió la identificación
	 * @param codigoDepartamento Restricción por codigo de departamento
	 * de vivienda
	 * @param codigoDepartamentoIdentificacion Restricción por codigo del
	 * departamento donde se expidió la identificación
	 * @param email Restricción por email
	 * @param codigoInstitucion Institución en la que se está realizando esta
	 * búsqueda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaPersonas(Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, String email, String codCargo, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que dada una identificación devuelve el login usado por
	 * el usuario con esa identificación
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroIdentificacion Número de identificación del 
	 * usuario buscado
	 * @param codigoTipoIdentificacion Código del tipo de identificación 
	 * del usuario buscado
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaPersonas_Login (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion) throws SQLException;

	/**
	 * Método que permite realizar búsquedas entre personas (La 
	 * diferencia entre este método y consultaTagBusquedaPersonas 
	 * es reciben en el tipo de persona buscada cosas diferentes)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersonaBuscada A través de este parámetro se define
	 * que se desea buscar (usuario?, paciente?, prof. salud?)
	 * @param criteriosBusqueda Arreglo de String's donde llegan todos
	 * los criterios de búsqueda utilizados por el usuario
	 * @param numeroIdentificacion Restricción por el número de 
	 * Identificación 
	 * @param codigoTipoIdentificacion Restricción por el código del 
	 * tipo de Identificación 
	 * @param primerNombrePersona Restricción por el primer nombre
	 * de la persona
	 * @param segundoNombrePersona Restricción por el segundo nombre
	 * de la persona
	 * @param primerApellidoPersona Restricción por el primer apellido
	 * de la persona
	 * @param segundoApellidoPersona Restricción por el segundo apellido
	 * de la persona
	 * @param fechaNacimiento Restricción de fecha de nacimiento
	 * @param direccion Restricción por dirección
	 * @param telefono Restricción por teléfono
	 * @param codigoCiudad Restricción por codigo de ciudad de vivienda
	 * @param codigoCiudadIdentificacion Restricción por codigo de ciudad
	 * donde se expidió la identificación
	 * @param codigoDepartamento Restricción por codigo de departamento
	 * de vivienda
	 * @param codigoDepartamentoIdentificacion Restricción por codigo del
	 * departamento donde se expidió la identificación
	 * @param email Restricción por email
	 * @param codigoInstitucion Institución en la que se está realizando esta
	 * búsqueda
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
	 * Método que dice si en una evolucion en particular fué en la que
	 * se vió el motivo de reversión de egreso
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idEvolucion Código de la evolución sobre la que se desea
	 * buscar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagDeboMostrarDatosMotivoReversion(Connection con, int idEvolucion) throws SQLException;
	
	/**
	 * Método que busca información básica sobre un tipo de 
	 * admisiones (o los dos tipos, dependiendo de lo especificado en
	 * el parámetro modo) para un paciente en particular
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroIdBusqueda Número de identificación del 
	 * paciente al que se le quieren buscar sus admisiones
	 * @param tipoIdBusqueda Codigo del tipo de identificación 
	 * del paciente al que se le quieren buscar sus admisiones
	 * @param modo Define si se quiere buscar admisiones 
	 * hospitalarias "hospitalarias", de urgencias "urgencias" o
	 * de los tipos "general" 
	 * @param codigoInstitucion Código de la institución donde
	 * se desea realizar la búsqueda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAdmisiones (Connection con, String numeroIdBusqueda, String tipoIdBusqueda, String modo, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que se encarga de buscar las admisiones hospitalarias. 
	 * Tiene dos posibles criterios de búsqueda, si se especifica el idBusqueda
	 * se busca por código de admisión y si no se busca por paciente
	 * (tipoIdBusqueda y numeroIdBusqueda)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idBusqueda Código de la admisión donde se van a
	 * buscar las admisiones hospitalarias
	 * @param tipoIdBusqueda Codigo del tipo de identificación 
	 * del paciente al que se le quieren buscar sus admisiones
	 * hospitalarias
	 * @param numeroIdBusqueda Número de identificación del 
	 * paciente al que se le quieren buscar sus admisiones 
	 * hospitalarias
	 * @param codigoInstitucion Código de la institución donde
	 * se desea realizar la búsqueda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAdmisionesHospitalarias (Connection con, String idBusqueda, String tipoIdBusqueda, String numeroIdBusqueda, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que busca todos los barrios de una ciudad dados los codigos
	 * de departamento y ciudad
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codDepartamento Código del departemento en el que 
	 * desean buscar los barrios
	 * @param codCiudad Código de la ciudad en la que desean buscar 
	 * los barrios
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraBarrios (Connection con, String codDepartamento, String codCiudad, String nombre) throws SQLException;
	
	/**
	 * Método que busca todas las camas que cumplan con las 
	 * restricciones establecidas (Hombre no se puede quedar en
	 * hab. para mujeres, etc). Existen algunas diferencias entre
	 * este método  y consultaTagMuestraCamasStruts; fueron
	 * desarrolladas en diferentes grupos de trabajo y optimizados
	 * para una admisión en particular, en este caso de hospitalización
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param sexo Código del sexo de la persona a la que se busca
	 * cama
	 * @param sinUrgencias boolean que define si deben aparecer o
	 * no las camas de urgencias
	 * @param conDesinfeccion boolean que indica si deben aparecer
	 * o no las camas en desinfección
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
	 * Método que busca todas las camas que cumplan con las 
	 * restricciones establecidas (Hombre no se puede quedar en
	 * hab. para mujeres, etc). Existen algunas diferencias entre
	 * este método  y consultaTagMuestraCamasStruts; fueron
	 * desarrolladas en diferentes grupos de trabajo y optimizados
	 * para una admisión en particular, en este caso de urgencias
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param sexo Código del sexo de la persona a la que se busca
	 * cama
	 * @param codigoCentroCosto Código del centro de costo por el
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
	 * Método que recibe el código de una evolución y devuelve
	 * (si existen) los datos más importantes de una orden de 
	 * salida (Diagnostico de muerte, destino a la salida, estado 
	 * a la salida)
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param idEvolucion Código de la evolucion en la que se
	 * desean mostrar los datos de la evolución
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestaDatosSalidaEvolucion (Connection con, int idEvolucion) throws SQLException;

	/**
	 * Método que se encarga de averiguar el diagnostico de ingreso
	 * para la valoración, almacenado durante la creación de la 
	 * admisión
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoAdmision Código de la admisión donde se desea
	 * buscar el diagnostico de ingreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticosAdmisionHospitalizacion (Connection con, int codigoAdmision) throws SQLException;
	
	/**
	 * Método que se encarga de averiguar el diagnostico de ingreso
	 * para diferentes objetos (Val. Egreso, etc) almacenado durante
	 * la creación de la valoración hospitalaria inicial. La diferencia con
	 * consultaTagMuestraDiagnosticoValoracionHospitalaria es el
	 * criterio manejado, en esta consulta se busca por ingreso.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoIngreso Código del ingreso para el cual se desea
	 * buscar el diagnostico de ingreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (Connection con, int codigoIngreso) throws SQLException;
	
	/**
	 * Método que se encarga de averiguar el diagnostico de ingreso
	 * para diferentes objetos (Val. Egreso, etc) almacenado durante
	 * la creación de la valoración hospitalaria inicial. La diferencia con
	 * consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso es
	 * el criterio manejado, en esta consulta se busca por cuenta.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoIngreso Código de la cuenta para la cual se desea
	 * buscar el diagnostico de ingreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalaria (Connection con, int codigoCuenta) throws SQLException;
	
	/**
	 * Método que se encarga de buscar la información correspondiente
	 * a todas las ciudades que se encuentran en el sistema
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDosTablasCiudadDepartamento (Connection con) throws SQLException;

	/**
	 * Método que se encarga de buscar todos los estratos sociales para
	 * un tipo de regimen específico
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoTipoRegimen Código del tipo de regimen bajo 
	 * el cual se van a buscar todos los estratos sociales
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraEstratoSocial (Connection con, String codigoTipoRegimen, boolean restringirPorActivo, int codigoEstratoMadre) throws SQLException;
	
	/**
	 * Método que se encarga de buscar todas las epicrisis existentes que
	 * cumplan con:
	 * 
	 * - Ser del paciente identificado con numeroIdentificacionPaciente y
	 * codigoTipoIdentificacionPaciente
	 * - Si el modo es accesoTotal busca con el minimo nivel de privilegio
	 * (todas las que pueda al menos ver), si el modo es accesoRestringido
	 * únicamente a las que pueda añadir nota / definir
	 * - Si mostrarHospitalizacion es true, muestra todas las epicrisis de 
	 * hospitalización, si es false muestra solo las de urgencias
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param paciente Objeto de tipo PersonaBasica sobre el
	 * que se quiere consultar
	 * @param codigoCentroCostoMedico Código del centro de costo del
	 * médico que está accediendo a la funcionalidad (para revisión de 
	 * permisos)
	 * @param modo Define si se quieren buscar las epicrisis solo para 
	 * revisarlas (visualizacion) o para cambios (definición/nota)
	 * @param mostrarHospitalizacion Define si se quieren mostrar las
	 * epicrisis de hospitalización (true) o las de urgencias (false)
	 * @param mostrarSoloActual boolean que indica si se debe mostrar
	 * solo las epicrisis actuales
	 * @param mostrarSoloNOActual que indica si se debe mostrar
	 * solo las epicrisis actuales
	 * @param codigoInstitucion Código de la institución del usuario 
	 * (o médico)
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraIngresosEpicrisis (Connection con, PersonaBasica paciente, int codigoCentroCostoMedico, String modo, boolean mostrarHospitalizacion, boolean mostrarSoloActual, boolean mostrarSoloNoActual, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que busca todas las instituciones (con nombre y datos),
	 * omitiendo la inactiva (institución dummy usada para manejar
	 * usuarios y médicos inactivos) 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraInstituciones (Connection con) throws SQLException;
	
	/**
	 * Método que busca todas las posibles naturaleza que no incluyan
	 * embarazo (en caso hombres)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param sexo Sexo de la persona a la que se está buscando
	 * posibles naturalezas
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (Connection con, String sexo) throws SQLException;

	/**
	 * Método que busca el nombre de la naturaleza del paciente 
	 * con código 1 (embarazo, gravidez...)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza1 (Connection con) throws SQLException;
	
	/**
	 * Método que busca el nombre de la naturaleza del paciente 
	 * con código 0 (ninguno ...)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza0 (Connection con) throws SQLException;

	/**
	 * Método que busca el nombre de la naturaleza del paciente 
	 * con código 3 (especifico para mayores de 65 años)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Mayores65 (Connection con) throws SQLException;

	/**
	 * Método que busca el nombre de la naturaleza del paciente 
	 * con código 3 (especifico para niños con menos de un año)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Bebe (Connection con) throws SQLException;
	
	/**
	 * Método que se encarga de buscar los examenes físicos que se deben
	 * mostrar / se pueden trabajar dependiendo del tipo de valoración 
	 * usado
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoEspecialidad Código de la primera especialidad a
	 * usar en la validación
	 * @param codigoEspecialidad2 Código de la segunda especialidad a
	 * usar en la validación
	 * @param codigoEspecialidad3 Código de la tercera especialidad a
	 * usar en la validación
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraOpcionesExFisicosStruts (Connection con, String codigoEspecialidad,String codigoEspecialidad2,String codigoEspecialidad3) throws SQLException;
	
	/**
	 * Método que busca personas de acuerdo al tipo especificado en
	 * tipoPersona (paciente, medico, usuario, acompanante, 
	 * responsable). Tiene en cuenta la institución y los permisos de
	 * compartir. Aunque comparte la búsqueda con 
	 * consultaTagMuestraPersonasNombreApellidoStruts, recibe
	 * diferentes parámetros (este tag recibe tipos de persona exclusivamente
	 * mientras el otro realiza busquedas por tipos de persona y accion a
	 * realizar), luego las búsquedas son diferentes
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersona Tipo de persona a buscar. Valores válidos
	 * (paciente, medico, usuario, acompanante, responsable)
	 * @param codigoInstitucion Código de la institución del usuario
	 * que está realizando la búsqueda
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraPersonasBasicasStruts (Connection con, String tipoPersona, String codigoInstitucion) throws SQLException;

	/**
	 * Método que busca personas de acuerdo al tipo especificado en
	 * tipoPersona (MostrarSoloMedicos, MostrarAcompanantes,
	 * MostrarMedicos, MostrarMedicosDesactivacion, MostrarMedicosActivacion,
	 * MostrarPacientes, MostrarUsuarios, MostrarUsuariosDesactivacion,
	 * MostrarUsuariosActivacion, MostrarUsuariosInactivos). Tiene en 
	 * cuenta la institución y los permisos de compartir. Aunque comparte 
	 * la búsqueda con consultaTagMuestraPersonasBasicasStruts, recibe
	 * diferentes parámetros (este tag recibe tipos de persona y accion a
	 * realizar mientras el otro realiza busquedas por tipos de persona 
	 * exclusivamente), luego las búsquedas son diferentes 
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersona Tipo de persona a buscar. Valores válidos
	 * (paciente, medico, usuario, acompanante, responsable)
	 * @param codigoInstitucion Código de la institución del usuario
	 * que está realizando la búsqueda
	 * @param codigoCentroCosto
	 * @param x 
	 * @param x 
	 * @return
	 * @throws SQLException
	 */
	public Vector<String> consultaTagMuestraPersonasNombreApellidoStruts (Connection con, String tipoPersonaBuscada, String codigoInstitucion, String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos, String separador) throws SQLException;
	
	/**
	 * Método que busca personas de acuerdo al tipo especificado en
	 * tipoPersona (MostrarSoloMedicos, MostrarAcompanantes,
	 * MostrarMedicos, MostrarMedicosDesactivacion, MostrarMedicosActivacion,
	 * MostrarPacientes, MostrarUsuarios, MostrarUsuariosDesactivacion,
	 * MostrarUsuariosActivacion, MostrarUsuariosInactivos). Tiene en 
	 * cuenta la institución y los permisos de compartir. Aunque comparte 
	 * la búsqueda con consultaTagMuestraPersonasBasicasStruts, recibe
	 * diferentes parámetros (este tag recibe tipos de persona y accion a
	 * realizar mientras el otro realiza busquedas por tipos de persona 
	 * exclusivamente), luego las búsquedas son diferentes 
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoPersona Tipo de persona a buscar. Valores válidos
	 * (paciente, medico, usuario, acompanante, responsable)
	 * @param codigoInstitucion Código de la institución del usuario
	 * que está realizando la búsqueda
	 * @param codigoCentroCosto
	 * @param x 
	 * @param x 
	 * @return
	 * @throws SQLException
	 */
	public String consultaTagMuestraPersonasNombreApellido (Connection con, String tipoPersonaBuscada, String codigoInstitucion, String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos) throws SQLException;
	
	
	/**
	 * Método que busca los posibles centros de costo a los cuales se puede
	 * solicitar una interconsulta
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCuenta Código de la cuenta abierta en la cual
	 * se va a solicitar la interconsulta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraPosiblesInterconsultas (Connection con, int codigoCuenta) throws SQLException;
	
	
	/**
	 * Método que muestra los posibles tipos de afiliado
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraTipoAfiliado (Connection con) throws SQLException;
	
	/**
	 * Método que dado un codigo de ingreso y definiendo si se quiere 
	 * buscar hospitalización o urgencias da el código de la cuenta que 
	 * corresponde a la admisión definida por el usuario (codigo de la 
	 * cuenta de la adm. hosp. si usuario definio esHospitalizacion en 
	 * true y código de la cuenta de adm. urg de lo contrario)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso Id del ingreso al cual se quiere buscar cuenta
	 * dada restricción de esHospitalizacion
	 * @param esHospitalizacion Define si se quiere buscar la cuenta
	 * correspondiente a la admisión de urgencias / hospitalizacion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException;
	
	/**
	 * Método que dada una cuenta, busca todas las valoraciones de 
	 * hospitalización que pueden ser asociadas a una evolución de
	 * esta cuenta (SIN validar todavía)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones de hospitalización a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Método que dada una cuenta, busca todas las valoraciones de 
	 * urgencias que pueden ser asociadas a una evolución de
	 * esta cuenta (SIN validar todavía)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones de urgencias a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Método que dada una cuenta, busca todas las valoraciones de 
	 * interconsulta que pueden ser asociadas a una evolución de
	 * esta cuenta (SIN validar todavía)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones de interconsulta a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection con, int idCuenta) throws SQLException;

	/**
	 * Método que dada una cuenta, busca todas las valoraciones  
	 * (hospitalización, interconsulta, urgencias) que pueden ser 
	 * asociadas a una evolución de esta cuenta (SIN validar todavía)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Cuenta a la que se quieren buscar todas las
	 * posibles valoraciones (hospitalización, interconsulta, urgencias)
	 * a asociar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones (Connection con, int idCuenta, int idCuentaAsociada) throws SQLException;
	
	/**
	 * Método que dado un número de solicitud, busca ciertos datos
	 * de esta, incluyendo si es de hospitalización, urgencias o 
	 * interconsulta
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroSolicitud número de la solicitud a la que
	 * pertenece la valoración a usar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion2 (Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Método que dado un medico y una funcionalidad devuelve la informacion de campos dinamicos asociada a esa funcionalidad
	 * permitiendo editarla
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idMedico id del profesional de la salud
	 * @param idFuncionalidad   funcionalidad desde donde se esta invocando el tag
	 * @param codigoCentroCosto Código del centro de costo
	 * @param codigoInstitucion Código de la institución
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagEditaCamposDinamicos (Connection con, int idMedico, String idFuncionalidad, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	/**
			 * Método que dado un medico y una funcionalidad devuelve la informacion de campos dinamicos asociada a esa funcionalidad
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
	 * Método que dado un medico , una funcionalidad y una seccion dentro de esa funcionalidad 
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
	 * Método que dado un medico,   una funcionalidad y una seccion dentro de esa funcionalidad,
	 * devuelve la informacion de campos dinamicos asociada a esa funcionalidad
	 * permitiendo editarlos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idMedico id del profesional de la salud
	 * @param idFuncionalidad   funcionalidad desde donde 
	 * se esta invocando el tag
	 * @param idSeccion   seccion de la funcionalidad desde 
	 * donde se esta invocando el tag
	 * @param codigoCentroCosto Código del centro de costo
	 * @param codigoInstitucion Código del médico
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagEditaCamposDinamicosSeccion (Connection con, int idMedico, String idFuncionalidad,String idSeccion, int codigoCentroCosto, String codigoInstitucion) throws SQLException;

	/**
	 * Método que se encarga de buscar todos los convenios que se puedan
	 * seleccionar dada la institución
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param codigoInstitucion Código de la institución que busca los
	 * convenios
	 * @param fechaReferencia
	 * @param capitado
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraConvenios (Connection con, String codigoInstitucion, String fechaReferencia,String capitado) throws SQLException;
	
	/**
	 * Método que permite buscar un Servicio
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoServicioBuscado Código del ipo del Servicio
	 * buscado
	 * @param criterioBusqueda Criterio por el que se está buscando
	 * @param esPorNombre Boolean que indica si la búsqueda se 
	 * está haciendo por nombre
	 * @param buscarEnTodosLosTarifarios Boolean que indica que
	 * si se ignora el parametro anterior y se busca en todos los tarifarios
	 * @param buscarConFormulario Boolean que indica si se buscan
	 * procedimientos con tarifario o no
	 * @param codigoSexo Código del sexo del paciente
	 * @param restringirPorFormulario Indica si la restricción por 
	 * formulario aplica o no
	 * @param idCuenta Número de la cuenta en la que se piensa
	 * dejar esta solicitud de servicios
	 * @param ocupacionSolicitada Ocupación seleccionada
	 * @param porCodigoCUPS 
	 * @param offset 
	 * @param limit 
	 * @param codigoTarifarioBusqueda Código del tarifario en el que
	 * se quiere hacer la búsqueda
	 * @param codigoConvenio Código del convenio del paciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaTagBusquedaServicios(Connection con, char tipoServicioBuscado, String criterioBusqueda, boolean esPorNombre, boolean buscarEnTodosLosTarifarios, boolean buscarConFormulario, int codigoSexo, int codigoContrato, boolean restringirPorFormulario, int idCuenta, String ocupacionSolicitada, boolean porCodigoCUPS, int institucion, int offset, int limit) throws SQLException;
	
	/**
	 * Método que hace una búsqueda sencilla en servicios 
	 * (Sin muchas restricciones)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param criterioBusqueda Criterio por el que se está buscando
	 * @param codigoTarifarioBusqueda Código del tarifario en el que
	 * se quiere hacer la búsqueda
	 * @param esPorNombre Boolean que indica si la búsqueda se 
	 * está haciendo por nombre
	 * @param buscarSoloActivos boolean para especificar si se 
	 * buscan solo los servicios activos
	 * @param incluirPaquetes 
	 * @param porCodigoCUPS 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaSencillaServicios (Connection con, String criterioBusqueda, int codigoTarifarioBusqueda, boolean esPorNombre, boolean buscarSoloActivos, boolean incluirPaquetes, boolean porCodigoCUPS) throws SQLException;
	
	/**
	 * Método que permite realizar búsquedas sobre las especialidades
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param criterioBusqueda Texto con el criterio de
	 * la búsqueda de la especialidad
	 * @param buscarTexto Boolean que indica si se debe buscar por
	 * texto o por código  
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 */
	public ResultadoCollectionDB consultaTagBusquedaEspecialidades(Connection con, String criterioBusqueda, boolean buscarTexto);
	
	/**
	 * Devulve una collección de las funcionalidades que se deben dibujar
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
	 * @param institucion Institución para el filtrado
	 * @return Collection con los resultados de la búsqueda
	 */
	public Collection consultaTagMuestraAtributos(Connection con, boolean mostrarAtributosArticulo, int institucion);
	
	/**
	 * Devuelve un resultset de las enfermeras que se encuentran activas
	 * @param con
	 * @return
	 */
	public ResultSetDecorator consultaTagMuestraEnfermerasActivas(Connection con, String restriccion, String codigoInstitucion);

	/**
	 * Método para buscar los diagnoticos NANDA
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
