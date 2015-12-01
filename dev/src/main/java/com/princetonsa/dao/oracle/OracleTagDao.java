/*
 * @(#)OracleTagDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.Answer;
import util.ConstantesBD;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.TagDao;
import com.princetonsa.dao.sqlbase.SqlBaseTagDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase implementa el contrato estipulado en <code>TagDao</code> , y presta servicios de acceso a
 * una base de datos PostgreSQL a las clases util.tag.*, que a su vez implementan los <code>custom tags</code>
 * de la aplicacion.
 * 
 *	@version 1.0, Mar 24, 2004
 */

public class OracleTagDao implements TagDao
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(OracleTagDao.class);
	
	/**
	 * Dada una conexion abierta con una base de datos PostgreSQL (si no existe, la crea) y una cadena con una
	 * consulta arbitraria, la ejecuta y retorna el <code>ResultSet</code> y la conexion en un objeto <code>Answer</code>.
	 * @param con una conexion abierta con una base de datos PostgreSQL
	 * @param consulta una cadena de texto con una consulta arbitraria
	 * @return un objeto <code>Answer</code> con el resultado de la consulta y la conexion abierta
	*/
	
	public Answer resultadoConsulta (Connection con, String textoConsulta) throws SQLException
	{
		return SqlBaseTagDao.resultadoConsulta (con, textoConsulta) ;
	}

	/**
	 * Dada una conexion abierta con una base de datos PostgreSQL (si no existe, la crea) y una cadena con una
	 * actualizaci�n arbitraria, ejecuta esta actualizaci�n
	 *
	 * @param con una conexion abierta con una base de datos PostgreSQL
	 * @param actualizacion una cadena de texto con una actualizaci�n arbitraria
	 * @return un <code>int</code> con el n�mero de actualizaciones hechas
	*/
	public int resultadoActualizacion (Connection con, String actualizacion) throws SQLException
	{
		return SqlBaseTagDao.resultadoActualizacion (con, actualizacion) ;
	}


	/**
	 * M�todos particulares para cada Tag
	 */


	/**
	 * Implementaci�n de la b�squeda de un valor por defecto
	 * en una BD Postgresql
	 * (TagValoresPorDefecto)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagValoresPorDefecto (Connection , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagValoresPorDefecto (Connection con, String parametro) throws SQLException
	{
		return SqlBaseTagDao.consultaTagValoresPorDefecto (con, parametro) ;
	}

	/**
	 * Implementaci�n del m�todo para mostrar errores por usuario
	 * inactivo en una BD Postgresql
	 * (TagErroresIngreso)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagErroresIngreso (Connection , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagErroresIngreso (Connection con, String login, String passwordHasheado) throws SQLException
	{
		return SqlBaseTagDao.consultaTagErroresIngreso (con, login, passwordHasheado) ;
	}


	/**
	 * Implementaci�n del m�todo busca el login de un usuario
	 * dada su identificaci�n en una BD Postgresql
	 * (TagBusquedaPersonas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaPersonas_Login (Connection , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaPersonas_Login (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagBusquedaPersonas_Login (con, numeroIdentificacion, codigoTipoIdentificacion) ;
	}

	/**
	 * Implementaci�n del m�todo que revisa si en la evoluci�n especificada el
	 * usuario vio el motivo de reversi�n en una BD Postgresql
	 * (TagDeboMostrarDatosMotivoReversion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagDeboMostrarDatosMotivoReversion(Connection con, int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagDeboMostrarDatosMotivoReversion(Connection con, int idEvolucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagDeboMostrarDatosMotivoReversion(con, idEvolucion) ;
	}
	
	/**
	 * Implementaci�n del m�todo que busca informaci�n b�sica de
	 * las admisiones de un paciente en una BD Postgresql
	 * (TagMuestraAdmisiones)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAdmisiones (Connection , String , String , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAdmisiones (Connection con, String numeroIdBusqueda, String tipoIdBusqueda, String modo, String codigoInstitucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAdmisiones (con, numeroIdBusqueda, tipoIdBusqueda, modo, codigoInstitucion) ;
	}

	/**
	 * Implementaci�n del m�todo que busca admisiones hospitalarias
	 * para un paciente / ingreso en una BD Postgresql
	 * (TagMuestraAdmisionesHospitalarias)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAdmisionesHospitalarias (Connection , String , String , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAdmisionesHospitalarias (Connection con, String idBusqueda, String tipoIdBusqueda, String numeroIdBusqueda, String codigoInstitucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAdmisionesHospitalarias (con, idBusqueda, tipoIdBusqueda, numeroIdBusqueda, codigoInstitucion) ;
	}

	/**
	 * Implementaci�n del m�todo que busca los barrios
	 * de una ciudad espec�fica en una BD Postgresql
	 * (TagMuestraBarrios)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraBarrios (Connection , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraBarrios (Connection con, String codDepartamento, String codCiudad,String nombre) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraBarrios (con, codDepartamento, codCiudad, nombre) ;
	}

	/**
	 * Implementaci�n del m�todo que busca camas que
	 * cumplan ciertas restricciones en una BD Postgresql
	 * (TagMuestraCamas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamas (Connection , String , String , boolean ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraCamas (Connection con, String sexo, String sinUrgencias, boolean conDesinfeccion, String codigoInstitucion,String fechaMovimiento, String horaMovimiento, String centroAtencion, String viaIngreso) throws SQLException {
		return SqlBaseTagDao.consultaTagMuestraCamas (con, sexo, sinUrgencias, conDesinfeccion, codigoInstitucion, fechaMovimiento, horaMovimiento, centroAtencion, viaIngreso) ;
	}

	/**
	 * Implementaci�n del m�todo que busca camas que
	 * cumplan ciertas restricciones en una BD Postgresql
	 * (TagMuestraCamasStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamasStruts (Connection , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraCamasStruts (
		Connection con, String sexo, String codigoCentroCosto, String codEstado, 
		String codigoInstitucion,String centroAtencion, String viaIngreso) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraCamasStruts (con, sexo, codigoCentroCosto, codEstado, codigoInstitucion, centroAtencion, viaIngreso) ;
	}

	/**
	 * Implementaci�n del m�todo que busca datos de orden de
	 * salida llenados por una evoluci�nen una BD Postgresql
	 * (TagMuestaDatosSalidaEvolucion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestaDatosSalidaEvolucion (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestaDatosSalidaEvolucion (Connection con, int idEvolucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestaDatosSalidaEvolucion (con, idEvolucion) ;
	}

	/**
	 * Implementaci�n del m�todo que busca los diagnosticos llenados
	 * en la admisi�n hospitalaria en una BD Postgresql
	 * (TagMuestraDiagnosticosAdmisionHospitalizacion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDiagnosticosAdmisionHospitalizacion (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticosAdmisionHospitalizacion (Connection con, int codigoAdmision) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraDiagnosticosAdmisionHospitalizacion (con, codigoAdmision) ;
	}

	/**
	 * Implementaci�n del m�todo que busca los diagnosticos llenados
	 * en la valoraci�n hospitalaria en una BD Postgresql
	 * (TagMuestraDiagnosticoValoracionHospitalariaIngreso)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (Connection con, int codigoIngreso) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (con, codigoIngreso); 
	}

	/**
	 * Implementaci�n del m�todo que busca los diagnosticos llenados
	 * en la valoraci�n hospitalaria en una BD Postgresql
	 * (TagMuestraDiagnosticoValoracionHospitalaria)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDiagnosticoValoracionHospitalaria (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalaria (Connection con, int codigoCuenta) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraDiagnosticoValoracionHospitalaria (con, codigoCuenta) ;
	}

	/**
	 * Implementaci�n del m�todo que busca las ciudades
	 * presentes en el sistema en una BD Postgresql
	 * (TagMuestraDosTablasCiudadDepartamento)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDosTablasCiudadDepartamento (Connection ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraDosTablasCiudadDepartamento (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraDosTablasCiudadDepartamento (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca los estratos
	 * sociales dadas ciertas restricciones en una BD Postgresql
	 * (TagMuestraEstratoSocial)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraEstratoSocial (Connection , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraEstratoSocial (Connection con, String codigoTipoRegimen, boolean restringirPorActivo,int codigoEstratoMadre) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraEstratoSocial (con, codigoTipoRegimen,  restringirPorActivo,codigoEstratoMadre) ;
	}

	/**
	 * Implementaci�n del m�todo que busca las epicrisis
	 * de un paciente en una BD Postgresql
	 * (TagMuestraIngresosEpicrisis)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraIngresosEpicrisis (Connection , String , String , int , int , String , boolean , boolean, boolean, String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraIngresosEpicrisis (Connection con, PersonaBasica paciente, int codigoCentroCostoMedico, String modo, boolean mostrarHospitalizacion, boolean mostrarSoloActual, boolean mostrarSoloNoActual, String codigoInstitucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraIngresosEpicrisis (con, paciente, codigoCentroCostoMedico, modo, mostrarHospitalizacion, mostrarSoloActual, mostrarSoloNoActual, codigoInstitucion) ;
	}

	/**
	 * Implementaci�n del m�todo que busca todas las instituciones
	 * v�lidas en una BD Postgresql
	 * (TagMuestraInstituciones)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraInstituciones (con) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraInstituciones (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraInstituciones (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca todas las posibles
	 * naturalezas de un paciente, sin incluir embarazada (caso
	 * hombres) en una BD Postgresql
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (Connection , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (Connection con, String sexo) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (con, sexo) ;
	}

	/**
	 * Implementaci�n del m�todo que busca la naturaleza de un
	 * paciente, que maneja el c�digo 1 en una BD Postgresql
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Naturaleza1 (con) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza1 (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraNaturalezaPaciente_Naturaleza1 (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca la naturaleza de un
	 * paciente, que maneja el c�digo 0 en una BD Postgresql
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Naturaleza1 (con) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza0 (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraNaturalezaPaciente_Naturaleza0 (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca la naturaleza de un
	 * paciente, que maneja el c�digo 3 en una BD Postgresql
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Mayores65 (Connection con) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Mayores65 (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraNaturalezaPaciente_Mayores65 (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca la naturaleza de un
	 * paciente, que maneja el c�digo 2 en una BD Postgresql
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Bebe (Connection con) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Bebe (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraNaturalezaPaciente_Bebe (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca los examenes f�sicos dadas las
	 * especialidades de la valoraci�n en una BD Postgresql
	 * (TagMuestraOpcionesExFisicosStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraOpcionesExFisicosStruts (Connection , String ,String ,String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraOpcionesExFisicosStruts (Connection con, String codigoEspecialidad,String codigoEspecialidad2,String codigoEspecialidad3) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraOpcionesExFisicosStruts (con, codigoEspecialidad,codigoEspecialidad2,codigoEspecialidad3) ;
	}

	/**
	 * Implementaci�n del m�todo que busca personas en el sistema
	 * dado su tipo en una BD Postgresql
	 * (TagMuestraPersonasBasicasStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPersonasBasicasStruts (Connection , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraPersonasBasicasStruts (Connection con, String tipoPersona, String codigoInstitucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraPersonasBasicasStruts (con, tipoPersona, codigoInstitucion) ;
	}

	/**
	 * Implementaci�n del m�todo que busca personas en el sistema
	 * dado su tipo y la acci�n a realizar (dados en un mismo par�metro)
	 * en una BD Postgresql
	 * (TagMuestraPersonasNombreApellidoStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPersonasNombreApellidoStruts (Connection , String , String , boolean ) throws SQLException
	 */
	public Vector<String> consultaTagMuestraPersonasNombreApellidoStruts (Connection con, String tipoPersonaBuscada, String codigoInstitucion, String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos, String separador) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraPersonasNombreApellidoStruts (con, tipoPersonaBuscada, codigoInstitucion, codigoCentroCosto, mostrarSoloActivos, mostrarSoloInactivos, separador) ;
	}
	
	/**
	 * Implementaci�n del m�todo que busca personas en el sistema
	 * dado su tipo y la acci�n a realizar (dados en un mismo par�metro)
	 * en una BD Postgresql
	 * (TagMuestraPersonasNombreApellidoStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPersonasNombreApellidoStruts (Connection , String , String , boolean ) throws SQLException
	 */
	public String consultaTagMuestraPersonasNombreApellido (Connection con, String tipoPersonaBuscada, String codigoInstitucion, String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraPersonasNombreApellido (con, tipoPersonaBuscada, codigoInstitucion, codigoCentroCosto, mostrarSoloActivos, mostrarSoloInactivos) ;
	}

	/**
	 * Implementaci�n del m�todo que busca los posibles centros de
	 * costo a los que se puede solicitar interconsulta en una BD Postgresql
	 * (TagMuestraPosiblesInterconsultas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPosiblesInterconsultas (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraPosiblesInterconsultas (Connection con, int codigoCuenta) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraPosiblesInterconsultas (con, codigoCuenta) ;
	}

	

	/**
	 * Implementaci�n del m�todo que busca los tipos de afiliado
	 * disponibles en el sistema en una BD Postgresql
	 * (TagMuestraTipoAfiliado)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraTipoAfiliado (Connection ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraTipoAfiliado (Connection con) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraTipoAfiliado (con) ;
	}

	/**
	 * Implementaci�n del m�todo que busca la cuenta de un
	 * ingreso especificando si se quiere la correspondiente a la de
	 * urgencias o a la de hospitalizacion en una BD Postgresql
	 * (TagMuestraLinkEvolucionesCuentaEnEpicrisis)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (Connection , int , boolean ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (con, idIngreso, esHospitalizacion) ;
	}

	/**
	 * Implementaci�n del m�todo que busca ciertos datos de las
	 * valoraciones hospitalarias que pueden ser asociadas a una
	 * evoluci�n en una BD Postgresql
	 * (TagMuestraLinkEvolucionesCuentaEnEpicrisis)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (con, idCuenta) ;
	}

	/**
	 * Implementaci�n del m�todo que busca ciertos datos de las
	 * valoraciones de urgencias que pueden ser asociadas a una
	 * evoluci�n en una BD Postgresql
	 * (TagMuestraAsocioEvolucionValoracion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (con, idCuenta) ;
	}

	/**
	 * Implementaci�n del m�todo que busca ciertos datos de las
	 * valoraciones de interconsulta que pueden ser asociadas a una
	 * evoluci�n en una BD Postgresql
	 * (TagMuestraAsocioEvolucionValoracion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (con, idCuenta) ;
	}

	/**
	 * Implementaci�n del m�todo que busca ciertos datos de todas las
	 * valoraciones (hospitalizaci�n, interconsulta, urgencias) que pueden
	 * ser asociadas a una evoluci�n en una BD Postgresql
	 * (TagMuestraAsocioEvolucionValoracion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones (Connection con, int idCuenta, int idCuentaAsociada) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones (con, idCuenta, idCuentaAsociada) ;
	}

	/**
	 * Implementaci�n del m�todo que busca ciertos datos de una
	 * valoracion asociada a una evoluci�n en una BD Postgresql
	 * (TagMuestraAsocioEvolucionValoracion2)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion2 (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion2 (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraAsocioEvolucionValoracion2 (con, numeroSolicitud) ;
	}

	/**
	 * Implementaci�n del m�todo que proporciona la informacion de campos dinamicos
	 * para edicion
	 * (TagEditaCamposDinamicos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagEditaCamposDinamicos (Connection con, String idMedico, String idFuncionalidad, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	 */
	public ResultSetDecorator consultaTagEditaCamposDinamicos (Connection con, int idMedico, String idFuncionalidad, int codigoCentroCosto, String codigoInstitucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagEditaCamposDinamicos (con, idMedico, idFuncionalidad, codigoCentroCosto, codigoInstitucion); 
	}

	/**
	 * Implementaci�n del m�todo que proporciona la informacion de campos dinamicos por seccion
	 * para edicion
	 * (TagEditaCamposDinamicosSeccion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagEditaCamposDinamicosSeccion (Connection con, String idMedico, String idFuncionalidad, String idSeccion, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	 */
	public ResultSetDecorator consultaTagEditaCamposDinamicosSeccion (Connection con, int idMedico, String idFuncionalidad, String idSeccion, int codigoCentroCosto, String codigoInstitucion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagEditaCamposDinamicosSeccion (con, idMedico, idFuncionalidad, idSeccion, codigoCentroCosto, codigoInstitucion) ;
	}

	/**
	 * Implementaci�n del m�todo que proporciona la informacion de campos dinamicos
	 * para edicion
	 * (TagMuestraCamposDinamicos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamposDinamicos (Connection con, String idMedico, String idFuncionalidad) throws SQLException;
	 */
	public ResultSetDecorator consultaTagMuestraCamposDinamicos (Connection con, String idFuncionalidad) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraCamposDinamicos (con, idFuncionalidad) ;
	}

	/**
	 * Implementaci�n del m�todo que proporciona la informacion de campos dinamicos por seccion
	 * para edicion
	 * (TagMuestraCamposDinamicosSeccion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamposDinamicosSeccion (Connection con, String idMedico, String idFuncionalidad, String idSeccion) throws SQLException;
	 */

	public ResultSetDecorator consultaTagMuestraCamposDinamicosSeccion (Connection con, String idFuncionalidad,String idSeccion) throws SQLException
	{
		return SqlBaseTagDao.consultaTagMuestraCamposDinamicosSeccion (con, idFuncionalidad,idSeccion) ;
	}


	/**
	 * Tags que utilizan expresiones regulares (Implementados en esta clase
	 * dadas las facilidades / velocidad de las expresiones regulares en 
	 * Postgresql)
	 */

	/**
	 * Implementaci�n del m�todo busca personas
	 * en una BD Postgresql
	 * (TagBusquedaPersonasStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaPersonasStruts (Connection , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String ) throws SQLException
	 */
	public Collection consultaTagBusquedaPersonasStruts (Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, String email, String codCargo, String codigoInstitucion, String numeroHistoriaClinica, String codigoPais, String codigoPaisIdentificacion,String fichaUsuarioCapitado,String accion,String numeroIngreso) throws SQLException
	{
		//Si se ejecuta la siguiente l�nea se llama al gen�rico
		//return SqlBaseTagDao.consultaTagBusquedaPersonasStruts (con, tipoPersonaBuscada, criteriosBusqueda, numeroIdentificacion, codigoTipoIdentificacion, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, fechaNacimiento, direccion, telefono, codigoCiudad, codigoCiudadIdentificacion, codigoDepartamento, codigoDepartamentoIdentificacion, email, codigoInstitucion) ;
		String consultaTagBusquedaPersonasStrutsStr="";
		String consultaTagBusquedaCapitadosStr = "";
		String camposCapitados = "";
		String innerCapitados = "";
		String where="";
		PreparedStatementDecorator consultaTagBusquedaPersonasStrutsStatement = null;
		ResultSetDecorator rs = null;
		/*
		//Si el parametro esta activo se buscan los pacientes capitados que tengan vigente el contrato
		if(!tipoPersonaBuscada.equals("pacienteEncabezado") && 
				ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(Utilidades.convertirAEntero(codigoInstitucion)).equals(ConstantesBD.acronimoSi))
		{
			innerCapitados = " INNER JOIN usuario_x_convenio usaxcon ON (usaxcon.persona = per.codigo AND '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' BETWEEN usaxcon.fecha_inicial AND usaxcon.fecha_final ) " +
							 " INNER JOIN contratos contr ON (contr.codigo = usaxcon.contrato ) ";
			camposCapitados = ",contr.convenio AS codigoconvenio, "+ConstantesBD.codigoNuncaValido+" AS clasificacionsocioeconomica ";
		}*/
		
		try {
		
		camposCapitados = ",'' AS codigoconvenio,"+ConstantesBD.codigoNuncaValido+" AS clasificacionsocioeconomica ";		
		
		if(tipoPersonaBuscada.equals("paciente")|| tipoPersonaBuscada.equals("pacienteEncabezado") )
		{
			where += " where pacins.codigo_institucion="+ codigoInstitucion ;
			consultaTagBusquedaPersonasStrutsStr = "SELECT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, " +
					"coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, " +
					"coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, " +
					"per.tipo_identificacion as  tipoIdentificacionPersona," +
					"getnombretipoidentificacion(per.tipo_identificacion) AS nombreTipoIdPersona,'"+ConstantesBD.acronimoNo+"' as capitado " +
					camposCapitados+
					" from administracion.personas per " +
					" inner join manejopaciente.pacientes pac on (pac.codigo_paciente=per.codigo)" +
					" inner join manejopaciente.pacientes_instituciones pacins on (pacins.codigo_paciente=pac.codigo_paciente)"+
					innerCapitados ;
			
			
			
			if (UtilidadCadena.noEsVacio(numeroIngreso))
				consultaTagBusquedaPersonasStrutsStr+=" inner join manejopaciente.ingresos i on (i.codigo_paciente=per.codigo) ";
			
			//Busqueda de capitado solo si la accion viene del ingreso del paciente  o reserva cita y se han parametrizado ciertos campos
			if((accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))&&
				(!fichaUsuarioCapitado.equals("")||
					!codigoTipoIdentificacion.equals("")||
					!numeroIdentificacion.equals("")||
					!primerApellidoPersona.equals("")||
					!segundoApellidoPersona.equals("")||
					!primerNombrePersona.equals("")||
					!segundoNombrePersona.equals("")||
					!fechaNacimiento.equals("")||
					!direccion.equals("")
				)
			)
			{
				
				innerCapitados = "";
				camposCapitados = "";
				//Si el parametro esta activo se buscan los pacientes capitados que tengan vigente el contrato
				/*if(!tipoPersonaBuscada.equals("pacienteEncabezado") &&
						ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(Utilidades.convertirAEntero(codigoInstitucion)).equals(ConstantesBD.acronimoSi))
				{
					innerCapitados = " INNER JOIN conv_usuarios_capitados conv ON (conv.usuario_capitado = uc.codigo AND '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' BETWEEN conv.fecha_inicial AND conv.fecha_final ) ";
					camposCapitados = " ,conv.convenio AS codigoconvenio,conv.clasificacion_socio_economica AS clasificacionsocioeconomica ";									  
				}*/

				camposCapitados = ",'' AS codigoconvenio,"+ConstantesBD.codigoNuncaValido+" AS clasificacionsocioeconomica ";
				
				consultaTagBusquedaCapitadosStr = "SELECT "+ 
					"0 AS codigo, "+
					"uc.primer_apellido AS primerApellidoPersona, "+
					"coalesce(uc.segundo_apellido,'') AS segundoApellidoPersona, "+
					"uc.primer_nombre AS primerNombrePersona, "+
					"coalesce(uc.segundo_nombre,'') AS segundoNombrePersona, "+
					"uc.numero_identificacion AS numeroIdentificacionPersona, "+
					"uc.tipo_identificacion AS tipoIdentificacionPersona," +
					"getnombretipoidentificacion(uc.tipo_identificacion) AS nombreTipoIdPersona, " +
					"'"+ConstantesBD.acronimoSi+"' as capitado "+
					camposCapitados+
					"FROM capitacion.usuarios_capitados uc "+
					innerCapitados+ 
					"WHERE";
					
			}
			
		}
		else if(tipoPersonaBuscada.equals("usuario") )
			consultaTagBusquedaPersonasStrutsStr = "SELECT DISTINCT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona, getusuariospersona(per.codigo,us.institucion) as login, us.cargo as codCargo from administracion.personas per, administracion.usuarios us where per.codigo=us.codigo_persona and institucion=" + codigoInstitucion ;
		else if(tipoPersonaBuscada.equals("medico") )
			consultaTagBusquedaPersonasStrutsStr = "SELECT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona FROM administracion.personas per, administracion.medicos med, administracion.medicos_instituciones medins where per.codigo=med.codigo_medico and per.codigo=medins.codigo_medico and medins.codigo_institucion=" + codigoInstitucion;
		else if (tipoPersonaBuscada.equals("usuarioInactivo") )
			consultaTagBusquedaPersonasStrutsStr = "SELECT DISTINCT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona, getusuariospersona(per.codigo,us.institucion) as login, us.cargo as codCargo from administracion.personas per, administracion.usuarios us inner join administracion.usuarios_inactivos ui ON (ui.login=us.login) where us.codigo_persona=per.codigo and us.institucion=" + codigoInstitucion;
		else
			throw new SQLException("Tipo de Personas invalido");

		//Ahora empezamos a colgarle arandelas a la consulta,
		//dependiendo de las opciones que nos hayan dado

		//Para evitar problemas con mayusculas y min�sculas
		//utilizamos una de las caracter�sticas de Postgresql,
		//expresiones regulares
		//http://moriarty.dif.um.es/pipermail/prog-gnu/2001-July/000923.html
		
		//se a�ade el where a la cadena de consulta
		consultaTagBusquedaPersonasStrutsStr+=where;
		
		if (criteriosBusqueda != null) 
		{
			boolean tieneCriterios=false;
			for (int i = 0; i < criteriosBusqueda.length; i++) 
			{
				if (criteriosBusqueda[i].equals("numeroHistoriaClinica"))
				{
					consultaTagBusquedaPersonasStrutsStr += " and pac.historia_clinica = '"+numeroHistoriaClinica+"' ";
					tieneCriterios=true;
				}
				if (criteriosBusqueda[i].equals("fichaUsuarioCapitado"))
				{
					consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.numero_ficha = '"+fichaUsuarioCapitado+"'";
					tieneCriterios=true;
				}
				
				if (criteriosBusqueda[i].equals("numeroIdentificacion") && numeroIdentificacion != null && !numeroIdentificacion.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.numero_identificacion = '" + numeroIdentificacion + "'";
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.numero_identificacion = '"+numeroIdentificacion+"'";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("tipoIdentificacion")&&!codigoTipoIdentificacion.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.tipo_identificacion='" + codigoTipoIdentificacion + "'";
					///Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.tipo_identificacion = '"+codigoTipoIdentificacion+"'";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("primerNombrePersona") && primerNombrePersona != null && !primerNombrePersona.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and ( UPPER(per.primer_nombre) LIKE UPPER('%" + (primerNombrePersona) + "%') ";
					//Porci�n adicional para la busqueda de paciente en el encabezado
					if(tipoPersonaBuscada.equals("pacienteEncabezado"))
						consultaTagBusquedaPersonasStrutsStr += " OR UPPER(per.segundo_nombre) LIKE UPPER('%" + (primerNombrePersona) + "%') )";
					else
						consultaTagBusquedaPersonasStrutsStr += " ) ";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.primer_nombre) LIKE UPPER('%" + (primerNombrePersona) + "%')) ";
					
					tieneCriterios=true;
					
				}
				else if (criteriosBusqueda[i].equals("segundoNombrePersona")&&segundoNombrePersona != null && !segundoNombrePersona.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.segundo_nombre) LIKE UPPER('%" + (segundoNombrePersona) + "%')";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.segundo_nombre) LIKE UPPER('%" + (segundoNombrePersona) + "%')) ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("primerApellidoPersona")&&primerApellidoPersona != null && !primerApellidoPersona.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and ( UPPER(per.primer_apellido) LIKE UPPER('%" + (primerApellidoPersona) + "%')";
					//Porci�n adicional para la busqueda de paciente en el encabezado
					if(tipoPersonaBuscada.equals("pacienteEncabezado"))
						consultaTagBusquedaPersonasStrutsStr += " OR UPPER(per.segundo_apellido) LIKE UPPER('%" + (primerApellidoPersona) + "%') )";
					else
						consultaTagBusquedaPersonasStrutsStr += " ) ";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.primer_apellido) LIKE UPPER('%" + (primerApellidoPersona) + "%')) ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("segundoApellidoPersona") && segundoApellidoPersona != null && !segundoApellidoPersona.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.segundo_apellido) LIKE UPPER('%" + (segundoApellidoPersona) + "%')";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita") ||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.segundo_apellido) LIKE UPPER('%" + (segundoApellidoPersona) + "%')) ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("codigoPaisIdentificacion"))
				{
					consultaTagBusquedaPersonasStrutsStr += " and per.codigo_pais_nacimiento = '"+codigoPaisIdentificacion+"' ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("ciudadIdentificacion")) {
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.codigo_departamento_nacimiento='" + codigoDepartamentoIdentificacion + "' and codigo_ciudad_nacimiento='" + codigoCiudadIdentificacion +"' ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("fechaNacimiento")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.fecha_nacimiento='" + fechaNacimiento + "'";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.fecha_nacimiento='" + fechaNacimiento + "' ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("direccion")&&direccion != null && !direccion.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.direccion) LIKE UPPER('%" + (direccion) + "%')";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "(UPPER(uc.direccion) LIKE UPPER('%" + (direccion) + "%'))";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("codigoPaisVivienda"))
				{
					consultaTagBusquedaPersonasStrutsStr += " and per.codigo_pais_vivienda = '"+codigoPais+"' ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("ciudadVivienda")) {
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.codigo_departamento_vivienda='" + codigoDepartamento + "' and codigo_ciudad_vivienda='" + codigoCiudad +"' ";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("telefono")) {
					if (telefono != null && !telefono.equals(""))
						consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.telefono) LIKE UPPER('%" + (telefono) + "%')";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("email")) {
					if (email != null && !email.equals(""))
						consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.email) LIKE UPPER('%" + (email) + "%')";
					
					tieneCriterios=true;
				}
				else if (criteriosBusqueda[i].equals("codigoCargo")) {
					if (codCargo != null && !codCargo.equals(""))
						consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and us.cargo="+codCargo+"";
					
					tieneCriterios=true;
				}
				logger.info("\n\n\nCONSULTA BUSQUEDA CRITERIO=>"+criteriosBusqueda[i]+"\n");
			}
			//se evalua si el  criterio de busqueda viene vacio
			if (UtilidadCadena.noEsVacio(numeroIngreso)){
				consultaTagBusquedaPersonasStrutsStr+=" and i.consecutivo='"+numeroIngreso+"'";
				tieneCriterios=true;
			}
			
			if(!tieneCriterios){
				return null;
			}
			
			
			
		}

		//order by no funciona en oracle con union
		//consultaTagBusquedaPersonasStrutsStr += " order by primerapellidopersona, segundoapellidopersona, primernombrepersona, segundonombrepersona ";
		logger.info("\n\n\nCONSULTA BUSQUEDA PERSONA STRUTS=>"+consultaTagBusquedaPersonasStrutsStr+"\n\n\n");
		logger.info("\n\n\nCONSULTA BUSQUEDA PERSONA BUSCADA=>"+tipoPersonaBuscada+"\n");
		logger.info("\n\n\nCONSULTA BUSQUEDA CAPITADOS STRUTS=>"+consultaTagBusquedaCapitadosStr+"\n\n\n");
		logger.info("\n\n\nCONSULTAR SOLO DE CAPITADOS CON VIGENCIA =>"+ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(Utilidades.convertirAEntero(codigoInstitucion))+"\n\n\n");
		
		//cuando la accion es de IngresoPaciente o Reserva de Cita la forma de consultar es diferente
		//Si se edit� consulta para usuarios capitados se prosigue a la validacion
		if((accion.equals("IngresoPaciente")||accion.equals("ReservaCita")||accion.equals("IngresoPacienteEntiSubcont"))&&!consultaTagBusquedaCapitadosStr.equals("")&&!consultaTagBusquedaCapitadosStr.endsWith("WHERE"))
		{						
			if(fichaUsuarioCapitado.equals(""))
			{
					String consulta = "(" + consultaTagBusquedaPersonasStrutsStr + ") union ("+consultaTagBusquedaCapitadosStr+")";//order by no funciona con oracle //+ " order by primerapellidopersona, segundoapellidopersona, primernombrepersona, segundonombrepersona)";
					
					consultaTagBusquedaPersonasStrutsStatement =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					
			}
			else
				consultaTagBusquedaPersonasStrutsStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaCapitadosStr + " order by uc.primer_apellido, uc.segundo_apellido, uc.primer_nombre, uc.segundo_nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		}
		else 
			consultaTagBusquedaPersonasStrutsStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaPersonasStrutsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
		
		rs = new ResultSetDecorator(consultaTagBusquedaPersonasStrutsStatement.executeQuery());
		return UtilidadBD.resultSet2Collection(rs);
		
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(consultaTagBusquedaPersonasStrutsStatement != null) {
					consultaTagBusquedaPersonasStrutsStatement.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("CERRANDO PREPAREDSTATEMENT: " + e);
			}
		}
	}


	/**
	 * Implementaci�n del m�todo busca personas
	 * en una BD Postgresql
	 * (TagBusquedaPersonas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaPersonas(Connection , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaPersonas(Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, String email, String codCargo, String codigoInstitucion) throws SQLException
	{
		String consultaTagBusquedaPersonasStr="";

		if (tipoPersonaBuscada.equals("IngresoPaciente") || tipoPersonaBuscada.equals("ModificarPaciente")|| tipoPersonaBuscada.equals("MostrarPacienteClinica")|| tipoPersonaBuscada.equals("solicitudLaboratorio") || tipoPersonaBuscada.equals("solicitudProcedimiento") || tipoPersonaBuscada.equals("MostrarPacienteFacturacion")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona from administracion.personas per, manejopaciente.pacientes pac, manejopaciente.pacientes_instituciones pacins where per.codigo=pac.codigo_paciente and pac.codigo_paciente=pacins.codigo_paciente and pacins.codigo_institucion" + codigoInstitucion ;
		}
		else if (tipoPersonaBuscada.equals("ModificarUsuario")||tipoPersonaBuscada.equals("MostrarUsuario")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona, us.cargo as codCargo from administracion.personas per, administracion.usuarios us where per.codigo=us.codigo_persona and institucion=" + codigoInstitucion ;
		}
		else if (tipoPersonaBuscada.equals("ModificarMedico")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona from administracion.personas per, administracion.medicos med, administracion.medicos_instituciones medins where per.codigo=med.codigo_medico and per.codigo=medins.codigo_medico and medins.codigo_institucion=" + codigoInstitucion;
		}
		else if (tipoPersonaBuscada.equals("MostrarUsuariosInactivos")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona,us.cargo as codCargo from administracion.usuarios us, administracion.personas per where us.codigo_persona=per.codigo and us.institucion=0";
		}

		//Ahora empezamos a colgarle arandelas a la consulta,
		//dependiendo de las opciones que nos hayan dado

		//Para evitar problemas con mayusculas y min�sculas
		//utilizamos una de las caracter�sticas de Postgresql,
		//expresiones regulares
		//http://moriarty.dif.um.es/pipermail/prog-gnu/2001-July/000923.html

		if (criteriosBusqueda != null) {
			for (int i = 0; i < criteriosBusqueda.length; i++) {
				if (criteriosBusqueda[i].equals("numeroIdentificacion")) {
					if (numeroIdentificacion != null && !numeroIdentificacion.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.numero_identificacion = '" + numeroIdentificacion + "'";
				}
				else if (criteriosBusqueda[i].equals("tipoIdentificacion")) {
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.tipo_identificacion='" + codigoTipoIdentificacion + "'";
				}
				else if (criteriosBusqueda[i].equals("primerNombrePersona")) {
					//Las expresiones regulares necesitan que llegue algo
					//Luego si esto viene nulo o con '', toca ponerle al
					//menos un espacio
					if (primerNombrePersona != null && !primerNombrePersona.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.primer_nombre) LIKE UPPER('%" + (primerNombrePersona) + "%')";
				}
				else if (criteriosBusqueda[i].equals("segundoNombrePersona")) {
					if (segundoNombrePersona != null && !segundoNombrePersona.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.segundo_nombre) LIKE UPPER('%" + (segundoNombrePersona) + "%')";
				}
				else if (criteriosBusqueda[i].equals("primerApellidoPersona")) {
					if (primerApellidoPersona != null && !primerApellidoPersona.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.primer_apellido) LIKE UPPER('%" + (primerApellidoPersona) + "%')";
				}
				else if (criteriosBusqueda[i].equals("segundoApellidoPersona")) {
					if (segundoApellidoPersona != null && !segundoApellidoPersona.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.segundo_apellido) LIKE UPPER('%" + (primerApellidoPersona) + "%')";
				}
				else if (criteriosBusqueda[i].equals("ciudadIdentificacion")) {
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.codigo_departamento_nacimiento=" + codigoDepartamentoIdentificacion + " and codigo_ciudad_nacimiento=" + codigoCiudadIdentificacion;
				}
				else if (criteriosBusqueda[i].equals("fechaNacimiento")) {
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.fecha_nacimiento='" + fechaNacimiento + "'";
				}
				else if (criteriosBusqueda[i].equals("direccion")) {
					if (direccion != null && !direccion.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.direccion) LIKE UPPER('%" + (direccion) + "%')";
				}
				else if (criteriosBusqueda[i].equals("ciudadVivienda")) {
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.codigo_departamento_vivienda=" + codigoDepartamento + " and codigo_ciudad_vivienda=" + codigoCiudad;
				}
				else if (criteriosBusqueda[i].equals("telefono")) {
					if (telefono != null && !telefono.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.telefono) LIKE UPPER('%" + (telefono) + "%')";
				}
				else if (criteriosBusqueda[i].equals("email")) {
					if (email != null && !email.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.email) LIKE UPPER('%" + (email) + "%')";
				}
				else if (criteriosBusqueda[i].equals("codigoCargo")) {
					if (codCargo != null && !codCargo.equals(""))
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and us.cargo="+codCargo+"";
				}
			}
		}

		consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";

		PreparedStatementDecorator consultaTagBusquedaPersonasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaPersonasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagBusquedaPersonasStatement.executeQuery());

	}

	/**
	 * Implementaci�n del m�todo busca diagnosticos
	 * en una BD Postgresql
	 * (TagBusquedaDiagnosticos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaDiagnosticos(Connection , String , boolean, int, int, int ) throws SQLException
	 */
	public ResultSetDecorator consultaTagBusquedaDiagnosticos(Connection con, String criterioBusquedaDiagnostico, boolean buscarTexto, int codigoTipoCie, int codigoFiltro, int codigoInstitucion) throws SQLException
	{
		//Con la siguiente instrucci�n se llama a la b�squeda
		//sin aplicar ventajas funcionalidad /velocidad postgres
		//para expresiones regulares
		//return SqlBaseTagDao.consultaTagBusquedaDiagnosticos(con, criterioBusquedaDiagnostico, buscarTexto, codigoTipoCie) ;
		
		//Si me ponen un dato vacio, las expresiones regulares no funcionan y adem�s
		//no son necesarias (no hay criterio)

		
		String consultaTagBusquedaDiagnosticosStr;
		String valorTrue=ValoresPorDefecto.getValorTrueParaConsultas();
		if(codigoFiltro!=ConstantesBD.codigoNuncaValido)
		{
			consultaTagBusquedaDiagnosticosStr="SELECT diag.acronimo, diag.tipo_cie, diag.nombre FROM manejopaciente.diagnosticos diag INNER JOIN manejopaciente.filtro_diagnosticos f ON(f.diagnostico=diag.acronimo AND f.tipo_cie=diag.tipo_cie AND f.institucion="+codigoInstitucion+") WHERE 1=1 AND ";
		}
		else
		{
			consultaTagBusquedaDiagnosticosStr="SELECT diag.acronimo, diag.tipo_cie, diag.nombre FROM manejopaciente.diagnosticos diag WHERE ";
		}
		if (criterioBusquedaDiagnostico==null||criterioBusquedaDiagnostico.equals(""))
		{
			consultaTagBusquedaDiagnosticosStr+="diag.tipo_cie=" + codigoTipoCie + " and diag.activo="+valorTrue+"";
		}
		else if ( buscarTexto  )
		{
			consultaTagBusquedaDiagnosticosStr+="diag.tipo_cie=" + codigoTipoCie + " and diag.activo="+valorTrue+" and UPPER(diag.nombre) LIKE UPPER('%" + (criterioBusquedaDiagnostico) + "%')";
		}
		else if ( !buscarTexto )
		{
			consultaTagBusquedaDiagnosticosStr+="diag.tipo_cie=" + codigoTipoCie + " and diag.activo="+valorTrue+" and UPPER(diag.acronimo) like UPPER('%" + criterioBusquedaDiagnostico + "%')";
		}

		PreparedStatementDecorator consultaTagBusquedaDiagnosticosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaDiagnosticosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		logger.info("consultaTagBusquedaDiagnosticosStr--->"+consultaTagBusquedaDiagnosticosStr);
		return new ResultSetDecorator(consultaTagBusquedaDiagnosticosStatement.executeQuery());

	}

	/**
	 * Implementaci�n del m�todo que busca convenios
	 * en una BD Postgresql
	 * (TagBusquedaConvenios)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaConvenios (Connection , String ) throws SQLException
	 */
	public ResultSetDecorator consultaTagMuestraConvenios (Connection con, String codigoInstitucion,String fechaReferencia,String capitado) throws SQLException
	{
		return SqlBaseTagDao.consultaTagBusquedaConvenios (con, codigoInstitucion, fechaReferencia,capitado) ;
	}
	
	/**
	 * Implementaci�n del m�todo busca servicios
	 * en una BD Postgresql
	 * (TagBusquedaServicios)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaServicios(Connection , char , String , boolean , int , boolean , boolean , int , int , boolean , int , String ) throws SQLException
	 */
	@Override
	public HashMap consultaTagBusquedaServicios(Connection con, char tipoServicioBuscado, String criterioBusqueda, boolean esPorNombre, boolean buscarEnTodosLosTarifarios, boolean buscarConFormulario, int codigoSexo, int codigoContrato, boolean restringirPorFormulario, int idCuenta, String ocupacionSolicitada,boolean porCodigoCUPS, int institucion, int offset, int limit) throws SQLException
	{
		String tarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		//Con la siguiente instrucci�n se llama a la b�squeda
		//sin aplicar ventajas funcionalidad /velocidad postgres
		//para expresiones regulares
		//return SqlBaseTagDao.consultaTagBusquedaServicios(con, tipoServicioBuscado, criterioBusqueda, esPorNombre, codigoTarifarioBusqueda, buscarEnTodosLosTarifarios, buscarConFormulario, codigoSexo, codigoContrato, restringirPorFormulario, idCuenta, ocupacionSolicitada) ;
		
		String filtroCriterio;
		
		String restriccionTipoServicio;
		
		//Cuando es de tipo Interconsulta, tambi�n debemos
		//buscar los de consultar externa
		
		if (tipoServicioBuscado!=ConstantesBD.codigoServicioInterconsulta)
		{
			
		    restriccionTipoServicio = " ser.tipo_servicio='" + tipoServicioBuscado + "'";
		}
		else
		{
			
		    restriccionTipoServicio = " (ser.tipo_servicio='" + tipoServicioBuscado + "' or ser.tipo_servicio='" + ConstantesBD.codigoServicioAntiguoInterconsulta+ "')";;
		}
		
		//si es tipo servicio procedimientos se toman tambi�n servicio quir�rgicos, 
		//no cruentos y partos y ces�reas
		if(tipoServicioBuscado==ConstantesBD.codigoServicioProcedimiento)
		{
			restriccionTipoServicio=" ("+restriccionTipoServicio+
			//	" OR ser.tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"'"+    //---no aplica tarea 22850
				" OR ser.tipo_servicio='"+ConstantesBD.codigoServicioNoCruentos+"'"+
			//	" OR ser.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"'"+     //---no aplica tarea 22850
				")";
			
		}
		
		//La siguiente Restricci�n SOLO? aplica
		//para solicitudes de interconsulta (por medio
		//del join - necesario nos aseguramos de esto,por
		//eso no hay restricci�n de tipo de solicitud en
		//la consulta) , no se necesita sub-select con 
		//anuladas porque las anuladas quedan con otro 
		//estado HC!
		String restriccionNoMismaEspecialidadEnInterconsulta="";
		String restriccionInterconsultaPermitidaSistema="";
		
		//Las dos restricciones anteriores solo aplican para interconsulta
		if (tipoServicioBuscado==ConstantesBD.codigoServicioInterconsulta)
		{
			restriccionNoMismaEspecialidadEnInterconsulta=" CASE WHEN(SELECT serv.especialidad from ordenes.solicitudes sol INNER JOIN ordenes.solicitudes_inter solinter ON (sol.numero_solicitud=solinter.numero_solicitud)  INNER JOIN facturacion.servicios serv ON (solinter.codigo_servicio_solicitado=serv.codigo) where sol.cuenta=" + idCuenta +" and sol.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada + " and ser2.especialidad=serv.especialidad "+ValoresPorDefecto.getValorLimit1()+" 1) IS NULL then "+ValoresPorDefecto.getValorFalseParaConsultas()+" else "+ValoresPorDefecto.getValorTrueParaConsultas()+" end AS solicitada";
			
			//Solo se debe permitir la b�squeda en interconsulta
			//de lo permitido por la tabla interconsu_perm
			if(!ocupacionSolicitada.equals("0"))
				restriccionInterconsultaPermitidaSistema=" and ser.especialidad IN (SELECT especialidad from ordenes.interconsu_perm where ocupacion_medica =" + ocupacionSolicitada + " or ocupacion_medica=" +ConstantesBD.codigoOtros + ")";
			else
				restriccionInterconsultaPermitidaSistema=" and ser.especialidad IN (SELECT especialidad from ordenes.interconsu_perm )";
			
		}
		else
		{
			restriccionNoMismaEspecialidadEnInterconsulta=ValoresPorDefecto.getValorFalseParaConsultas()+"  AS solicitada"; 
		}
		
		//Si el usuario genera un caso que aborte la consulta, ej
		//en el campo de c�digo no hay un entero, se cancela
		//la b�squeda (se compensa el tiempo de manejo de la
		//excepci�n con el ahorro en la consulta)
		boolean cancelarBusqueda=false;
		String busquedaCasoCancelar = "SELECT numero_solicitud from historiaclinica.valoraciones where numero_solicitud < -50";

		//La consulta interna nos busca el "core" del asunto
		
		if (esPorNombre)
		{
			filtroCriterio=" UPPER(refser.descripcion) LIKE UPPER('%" + (criterioBusqueda) + "%') and ";
		}
		else
		{
			if(porCodigoCUPS)
			{
				filtroCriterio="refser.codigo_propietario='" + criterioBusqueda + "' and refser.tipo_tarifario=" + tarifario + " and ";
				//int codigoServicio=Servicios.obtenerServicioDadoCodigoTarifario(con, criterioBusqueda, Utilidades.convertirAEntero(tarifario));
				//filtroCriterio=" ser.codigo=" + codigoServicio + " and ";
			}
			else
			{
				try
				{
					Integer.parseInt(criterioBusqueda);
				}
				catch (NumberFormatException e)
				{
					cancelarBusqueda=true;
				}
				filtroCriterio=" ser.codigo=" + criterioBusqueda + " and ";
			}
		}

		if (!cancelarBusqueda)
		{
			String filtroPorFormulario1="";
			if (restringirPorFormulario)
			{
				if (buscarConFormulario)
				{
					filtroPorFormulario1= " ser.codigo in (select servicio from historiaclinica.form_resp_serv) and ";
				}
				else
				{
					filtroPorFormulario1= " ser.codigo not in (select servicio from historiaclinica.form_resp_serv) and ";
				}
			}
			else
			{
				filtroPorFormulario1="";
			}
			
			String filtroPorFormulario2="";
			if (restringirPorFormulario)
			{
				if (buscarConFormulario)
				{
					filtroPorFormulario2= " ser2.codigo in (select servicio from historiaclinica.form_resp_serv) and ";
				}
				else
				{
					filtroPorFormulario2= " ser2.codigo not in (select servicio from historiaclinica.form_resp_serv) and ";
				}
			}
			else
			{
				filtroPorFormulario2="";
			}
			
			String consultaSuperior, filtroSexo=" (ser.sexo is null or (ser.sexo="  + codigoSexo + ")) and ";
			consultaSuperior="";
			if (buscarEnTodosLosTarifarios)
			{
				//En el caso de todos los tarifarios NO aplica lo de excepciones
				String consultaInternaIncompletaConJoinEspecialidades;
				
				if (tipoServicioBuscado==ConstantesBD.codigoServicioFalso)
				{
					consultaInternaIncompletaConJoinEspecialidades=" from facturacion.servicios ser, facturacion.referencias_servicio refser, administracion.especialidades esp where " + filtroCriterio + filtroPorFormulario1 + filtroSexo + " ser.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ser.codigo=refser.servicio and ser.especialidad=esp.codigo " + restriccionInterconsultaPermitidaSistema /*+ restriccionNoMismaEspecialidadEnInterconsulta*/ + " and refser.tipo_tarifario=" + tarifario;
				}
				else
				{
					consultaInternaIncompletaConJoinEspecialidades=" from facturacion.servicios ser, facturacion.referencias_servicio refser, administracion.especialidades esp where " + filtroCriterio + filtroPorFormulario1 + filtroSexo +restriccionTipoServicio + " and ser.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ser.codigo=refser.servicio and ser.especialidad=esp.codigo " + restriccionInterconsultaPermitidaSistema /*+ restriccionNoMismaEspecialidadEnInterconsulta*/ + " and refser.tipo_tarifario=" + tarifario;
				}
				consultaSuperior="(select ser.codigo, ser.espos, ser.especialidad as especialidad,"+restriccionNoMismaEspecialidadEnInterconsulta.replaceAll("ser2","ser")+",  refser.descripcion as nombre, "+ValoresPorDefecto.getValorFalseParaConsultas()+" as esExcepcion, 'N/A' as codigoPropietario, esp.nombre as nombreEspecialidad " + consultaInternaIncompletaConJoinEspecialidades +" order by refser.descripcion) " ;
			}		
			else
			{
				String busquedaExcepciones="SELECT servicio from facturacion.excepciones_servicios where contrato=" + codigoContrato;
				
				consultaSuperior="(SELECT " +
						"ser.codigo as codigo, " +
						"getcodigoservicioportatil(ser.codigo) As portatil, " +
						"ser.espos as espos, " +
						"ser.especialidad as especialidad,"+
						restriccionNoMismaEspecialidadEnInterconsulta.replaceAll("ser2","ser")+", "+
						"getnombreservicio(ser.codigo,"+tarifario+") as nombre, " +
						""+ValoresPorDefecto.getValorTrueParaConsultas()+" as esExcepcion, " +
						//"CASE WHEN refser.tipo_tarifario="+codigoTarifarioBusqueda++" THEN as codigoPropietario, " +
						//CAMBIA SEGUN DOCUMENTACION 2008-11-25
						///////" getcodigopropservicio(ser.codigo,getEsquemaTarifarioSerArt(-1,"+codigoContrato+",ser.codigo,'S')::integer) as codigoPropietario, "+
						" getcodigopropservicio(ser.codigo,"+tarifario+") as codigoPropietario, "+
						"esp.nombre as nombreEspecialidad," +
						"ser.grupo_servicio as gruposervicio, " +
						"ser.toma_muestra as tomamuestra," +
						"ser.respuesta_multiple as respuestamultiple, "+
						"(SELECT CASE WHEN COUNT(gsinter.codigo)>0 THEN 1 ELSE 0 END from facturacion.grupos_servicios gsinter inner join facturacion.servicios sinter on(sinter.grupo_servicio=gsinter.codigo) where sinter.codigo=ser.codigo and gsinter.multiple='S') AS grupomultiple "+
						"FROM facturacion.servicios ser, facturacion.referencias_servicio refser, administracion.especialidades esp " +
						"WHERE "+
						
						filtroCriterio+
						filtroPorFormulario1 + 
						filtroSexo +
						restriccionTipoServicio + 
						" and ser.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ser.codigo=refser.servicio and ser.especialidad=esp.codigo " + 
						restriccionInterconsultaPermitidaSistema+
						" and refser.tipo_tarifario="+tarifario+
						" and ser.codigo IN (" + busquedaExcepciones +") "+
						//no funciona con oracle el order by//" ORDER BY refser.descripcion" +
						" ) "+
						" UNION "+
						"(SELECT " +
						"ser2.codigo as codigo, " +
						"getcodigoservicioportatil(ser2.codigo) As portatil, " +
						"ser2.espos as espos, " +
						"ser2.especialidad as especialidad,"+
						restriccionNoMismaEspecialidadEnInterconsulta+", "+
						"getnombreservicio(ser2.codigo,"+tarifario+") as nombre, " +
						ValoresPorDefecto.getValorFalseParaConsultas()+" as esExcepcion, " +
						//"CASE WHEN refser.tipo_tarifario="+codigoTarifarioBusqueda++" THEN as codigoPropietario, " +
						" getcodigopropservicio(ser2.codigo,"+tarifario+") as codigoPropietario, "+
						"esp2.nombre as nombreEspecialidad," +
						"ser2.grupo_servicio as gruposervicio, " +
						"ser2.toma_muestra as tomamuestra," +
						"ser2.respuesta_multiple as respuestamultiple, "+
						"(SELECT CASE WHEN COUNT(gsinter.codigo)>0 THEN 1 ELSE 0 END from facturacion.grupos_servicios gsinter inner join facturacion.servicios sinter on(sinter.grupo_servicio=gsinter.codigo) where sinter.codigo=ser2.codigo and gsinter.multiple='S') AS grupomultiple "+
						"FROM facturacion.servicios ser2, facturacion.referencias_servicio refser2, administracion.especialidades esp2 " +
						"WHERE "+
						filtroCriterio.replaceAll("ser.","ser2.")+
						filtroPorFormulario2 + 
						filtroSexo.replaceAll("ser.","ser2.") +
						restriccionTipoServicio.replaceAll("ser.","ser2.").replaceAll("ser2.icio","servicio") + 
						" and ser2.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ser2.codigo=refser2.servicio and ser2.especialidad=esp2.codigo " + 
						restriccionInterconsultaPermitidaSistema.replaceAll("ser.","ser2.")+
						" and refser2.tipo_tarifario="+tarifario+
						" and ser2.codigo NOT IN (" + busquedaExcepciones +") "+
						//no funciona con oracle el order by//" ORDER BY refser2.descripcion" +
						") ";
			}
				/*String busquedaExcepciones="SELECT servicio from excepciones_servicios where contrato=" + codigoContrato;
				String consultaInternaIncompleta;
				
				if (tipoServicioBuscado==ConstantesBD.codigoServicioFalso)
				{
					consultaInternaIncompleta=" from servicios ser, referencias_servicio refser where " + filtroCriterio + filtroPorFormulario + filtroSexo + " ser.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ser.codigo=refser.servicio " +  restriccionInterconsultaPermitidaSistema /*+ restriccionNoMismaEspecialidadEnInterconsulta + "and refser.tipo_tarifario=" + ConstantesBD.codigoTarifarioCups;
			/*	}
				else
				{
					consultaInternaIncompleta=" from servicios ser, referencias_servicio refser where " + filtroCriterio + filtroPorFormulario + filtroSexo + restriccionTipoServicio +" and ser.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ser.codigo=refser.servicio " +  restriccionInterconsultaPermitidaSistema /*+ restriccionNoMismaEspecialidadEnInterconsulta + "and refser.tipo_tarifario=" + ConstantesBD.codigoTarifarioCups;
		/*		}
				
				
				consultaInternaIncompleta=" select ser.codigo " + consultaInternaIncompleta;
				String consultaSuperiorIncompleta=" from servicios ser2, referencias_servicio refser2, especialidades esp where  refser2.tipo_tarifario=" + codigoTarifarioBusqueda + " and ser2.codigo=refser2.servicio and ser2.especialidad=esp.codigo and  ser2.codigo IN (" + consultaInternaIncompleta+ ")";
				
				consultaSuperior="(select ser2.codigo, ser2.espos, ser2.especialidad as especialidad, "+restriccionNoMismaEspecialidadEnInterconsulta+", (SELECT max(descripcion) from referencias_servicio where tipo_tarifario=" +  ConstantesBD.codigoTarifarioCups +" and servicio=ser2.codigo) as nombre, "+ValoresPorDefecto.getValorTrueParaConsultas()+" as esExcepcion, refser2.codigo_propietario as codigoPropietario, esp.nombre as nombreEspecialidad  " + consultaSuperiorIncompleta + " and ser2.codigo IN (" + busquedaExcepciones +") ) " +
					" UNION " +
					"(select ser2.codigo,  ser2.espos, ser2.especialidad as especialidad, "+restriccionNoMismaEspecialidadEnInterconsulta+", (SELECT max(descripcion) from referencias_servicio where tipo_tarifario=" +  ConstantesBD.codigoTarifarioCups +" and servicio=ser2.codigo) as nombre, "+ValoresPorDefecto.getValorFalseParaConsultas()+" as esExcepcion, refser2.codigo_propietario as codigoPropietario, esp.nombre as nombreEspecialidad  " + consultaSuperiorIncompleta + " and ser2.codigo NOT IN (" + busquedaExcepciones +") ) ";
					*/ 
			
			/*
			 * Consultar el numero de resultados para permitir que el paginador muetre todos los elementos 
			 */
			String consultaNumResultados="SELECT COUNT(1) as numresultados FROM ("+consultaSuperior+") tabla";
			PreparedStatementDecorator pstNumResultados=new PreparedStatementDecorator(con.prepareStatement(consultaNumResultados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultadoNumResultados=new ResultSetDecorator(pstNumResultados.executeQuery());
			int numResultadosTotal=0;
			if(resultadoNumResultados.next())
			{
				numResultadosTotal=resultadoNumResultados.getInt("numresultados");
			}
			
			if(limit!=0 && offset!=0)
			{
				consultaSuperior="SELECT externa.* FROM (SELECT tabla.*, rownum AS numerofila from ("+consultaSuperior+") tabla) externa WHERE externa.numerofila > "+offset+" AND externa.numerofila <="+(offset+limit);
			}
			else if(limit!=0)
			{
				consultaSuperior="SELECT externa.* FROM (SELECT tabla.*, rownum AS numerofila from ("+consultaSuperior+") tabla) externa WHERE externa.numerofila <= "+limit;
			}

			logger.info("Consulta Superior ________________ "+consultaSuperior);
			PreparedStatementDecorator statementConsulta= new PreparedStatementDecorator(con.prepareStatement(consultaSuperior,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultadoDecorator=new ResultSetDecorator(statementConsulta.executeQuery());
			HashMap resultado=UtilidadBD.cargarValueObject(resultadoDecorator);
			resultadoDecorator.close();
			statementConsulta.close();
			resultado.put("numResultadosTotal", numResultadosTotal);
			return resultado;
			
			
		}
		else
		{
			PreparedStatementDecorator statementConsulta= new PreparedStatementDecorator(con.prepareStatement(busquedaCasoCancelar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultadoDecorator=new ResultSetDecorator(statementConsulta.executeQuery());
			HashMap resultado=UtilidadBD.cargarValueObject(resultadoDecorator);
			resultadoDecorator.close();
			statementConsulta.close();
			
			logger.info("Consulta Superior ________________ "+busquedaCasoCancelar);
			return resultado;
		}
	}

	/**
	 * Implementaci�n del m�todo busca servicios sin restricciones
	 * en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.TagDao#busquedaSencillaServicios (Connection , String , int , boolean , boolean ) throws SQLException
	 */
	public ResultSetDecorator busquedaSencillaServicios (Connection con, String criterioBusqueda, int codigoTarifarioBusqueda, boolean esPorNombre, boolean buscarSoloActivos, boolean incluirPaquetes,boolean porCodigoCUPS) throws SQLException
	{
		String filtroCriterio="";
		String consultaBaseStr=		"SELECT refser1.descripcion as nombreServicio, " +
												"refser1.servicio as codigoServicio, " +
												"serv.especialidad as codigoEspecialidad, " +
												"serv.activo, " +
												"esp.nombre as nombreEspecialidad, " +
												" serv.tipo_servicio As tipoServicio " +
												"from facturacion.referencias_servicio refser1  " +
												"INNER JOIN facturacion.servicios serv ON (serv.codigo=refser1.servicio) " +
												"INNER JOIN administracion.especialidades esp ON (serv.especialidad=esp.codigo) " +
												"where refser1.tipo_tarifario=" + codigoTarifarioBusqueda + " " ;
												
		if (esPorNombre)
		{
			if (criterioBusqueda!=null)
			{
				filtroCriterio= " and UPPER(refser1.descripcion) LIKE UPPER('%" + (criterioBusqueda) + "%') ";
			}
		}
		else
		{
			//Por c�digo
			if(porCodigoCUPS)
			{
				filtroCriterio=" AND refser1.codigo_propietario='" + criterioBusqueda + "' and refser1.tipo_tarifario=" + codigoTarifarioBusqueda +" ";
			}
			else
			{
				try
				{
					filtroCriterio= " and serv.codigo=" + Integer.parseInt(criterioBusqueda);
				}
				catch (Exception e)
				{
					//Si me dijeron que era por c�digo y no era un n�mero
					//no puedo agregarlo a la consulta o me saca error, la dejo
					//igual
				}
			}
			
		}
		
		if (buscarSoloActivos)
		{
			filtroCriterio=filtroCriterio +" and serv.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
		}
		if(!incluirPaquetes)
		{
			filtroCriterio=filtroCriterio +" and serv.tipo_servicio<>'"+ConstantesBD.codigoServicioPaquetes+"'";
		}
		
		consultaBaseStr=consultaBaseStr + filtroCriterio;
		PreparedStatementDecorator busquedaSencillaServiciosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaBaseStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		return new ResultSetDecorator(busquedaSencillaServiciosStatement.executeQuery());
	}

	/**
	 * Implementaci�n del m�todo busca especialidades
	 * en una BD Postgresql (TagBusquedaEspecialidades)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaEspecialidades(Connection , String , boolean ) throws SQLException
	 */
	public ResultadoCollectionDB consultaTagBusquedaEspecialidades(Connection con, String criterioBusqueda, boolean buscarTexto)
	{
		String consultaTagBusquedaEspecialidadesStr = "";
		if( !UtilidadCadena.noEsVacio(criterioBusqueda) )
		{
			consultaTagBusquedaEspecialidadesStr = "SELECT codigo AS codigoEspecialidad, " +
																			 "nombre AS nombreEspecialidad " +
																			 "FROM administracion.especialidades ";
		}
		else 
		if ( buscarTexto  )
		{
			consultaTagBusquedaEspecialidadesStr = "SELECT codigo AS codigoEspecialidad, " +
																			 "nombre AS nombreEspecialidad " +
																			 "FROM administracion.especialidades " +
																			 "WHERE UPPER(nombre) LIKE UPPER('%" + (criterioBusqueda) + "%') ";
		}
		else 
		if ( !buscarTexto )
		{
			consultaTagBusquedaEspecialidadesStr = "SELECT codigo AS codigoEspecialidad, " +
																			 "nombre AS nombreEspecialidad " +
																			 "FROM administracion.especialidades " +
																			 "WHERE UPPER(codigo) like UPPER('%" + criterioBusqueda + "%')";
		}

		ResultSetDecorator resultado = null;

		try
		{
			PreparedStatementDecorator consultaTagBusquedaEspecialidadesStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaEspecialidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado = new ResultSetDecorator(consultaTagBusquedaEspecialidadesStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(" Error en la consulta de especialidades del TagBusquedaEspecialidades\n"+e.getMessage());			
			return new ResultadoCollectionDB(false, " Error en la consulta de especialidades del TagBusquedaEspecialidades\n"+e.getMessage());
		}
		try
		{
			Collection col = UtilidadBD.resultSet2Collection(resultado);
			if( col != null && !col.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", col);
			}
			else
			{
				return new ResultadoCollectionDB(false);
			}
		}
		catch(Exception e)
		{
			logger.warn(" Error en TagBusquedaEspecialidades al convertir el result set a una colecci�n\n"+e.getMessage());
			return new ResultadoCollectionDB(false, " Error en TagBusquedaEspecialidades al convertir el result set a una colecci�n\n"+e.getMessage());
		}
	}

	/**
	 * Implementaci�n del m�todo busca las funcionalidades a dibujar
	 * en una BD Postgresql (TagBusquedaEspecialidades)
	 *
	 * @see com.princetonsa.dao.TagDao#funcionalidadesADibujar(Connection , String , int , String , boolean )
	 */
	public ResultadoCollectionDB funcionalidadesADibujar(Connection con, String login, int funcionalidadPadre, String fincionalidadesHijas, boolean modo)
	{
		return SqlBaseTagDao.funcionalidadesADibujar(con, login, funcionalidadPadre,fincionalidadesHijas, modo);
	}
	/**
	 * Implementaci�n del m�todo auxiliar para la b�squeda
	 * del tipo de cie v�lido dada una fecha en una BD 
	 * Postgresql
	 * (TagBusquedaDiagnosticos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaDiagnosticos_Cie (Connection , String ) throws SQLException
	 */
	public int consultaTagBusquedaDiagnosticos_Cie (Connection con, String fecha) throws SQLException
	{
	 return SqlBaseTagDao.consultaTagBusquedaDiagnosticos_Cie (con, fecha);
	}
	
	/**
	 * Implementaci�n del m�todo auxiliar para la b�squeda
	 * de fecha en el caso de estar trabajando con una
	 * valoraci�n de consulta externa, para una BD Gen�rica
	 * (TagBusquedaDiagnosticos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaDiagnosticos_FechaCita (Connection , int ) throws SQLException
	 */
	public String consultaTagBusquedaDiagnosticos_FechaCita (Connection con, int numeroSolicitud) throws SQLException
	{
	    return SqlBaseTagDao.consultaTagBusquedaDiagnosticos_FechaCita (con, numeroSolicitud) ;
	}
	
	/**
	 * Metodo que busca los atributos de la justificacion de una solicitud
	 * @param con
	 * @param mostrarAtributosArticulo
	 * @param institucion Instituci�n para el filtrado
	 * @return Collection con los resultados de la b�squeda
	 */
	public Collection consultaTagMuestraAtributos(Connection con, boolean mostrarAtributosArticulo, int institucion)
	{
		return SqlBaseTagDao.consultaTagMuestraAtributos(con, mostrarAtributosArticulo, institucion);
	}

    /**
     * Implementacion del metodo para buscar enfermeras activas
     */  
    public ResultSetDecorator consultaTagMuestraEnfermerasActivas(Connection con, String restriccion, String codigoInstitucion) {
        return SqlBaseTagDao.consultaTagMuestraEnfermerasActivas(con,restriccion, codigoInstitucion);
    }

	/**
	 * M�todo para buscar los diagnoticos NANDA
	 * @param con
	 * @param esPorCodigo
	 * @param criterio
	 * @return
	 */
	public Vector buscarNanda(Connection con, boolean esPorCodigo, String criterio, int institucion, String codigosNoBuscados)
	{
		return SqlBaseTagDao.buscarNanda(con, esPorCodigo, criterio, institucion, codigosNoBuscados);
	}
    
	/**
     * Metodo para consultar si un diagnostico es de importancia en salud publica y debe ser diligenciado
	 * en una ficha de vigilancia epidemiologica
     */
    public ResultSetDecorator consultaTagDiagnosticoSaludPublica(Connection con, String acronimo) {
        return SqlBaseTagDao.consultaTagDiagnosticoSaludPublica(con,acronimo);
    }
    
    /**
     * Metodo para consultar si hay fichas de vigilancia epidemiologica pendientes por completar,
     * hacer seguimiento o por notificar
     * @param con
     * @param codigoUsuario
     * @return
     */
    public ResultSetDecorator consultaHayFichasPendientes(Connection con, String loginUsuario) {
        
        return SqlBaseTagDao.consultaHayFichasPendientes(con,loginUsuario);
    }
    
    /**
     * Metodo para consultar los usuarios de la funcionalidad administrador del modulo de epidemiologia
     * @param con
     * @return
     */
    public ResultSetDecorator consultaUsuariosEpidemiologia(Connection con) {
    	
    	return SqlBaseTagDao.consultaUsuariosEpidemiologia(con);
    }
}
