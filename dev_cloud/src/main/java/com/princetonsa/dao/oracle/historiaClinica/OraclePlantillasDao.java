package com.princetonsa.dao.oracle.historiaClinica;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosString;
import util.ResultadoBoolean;

import com.princetonsa.dao.historiaClinica.PlantillasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBasePlantillasDao;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

public class OraclePlantillasDao implements PlantillasDao
{		
	
	
	/**
	 * 
	 */
	private static final String strInsertarDetSeccionesNuevos = "insert into " +
		"det_secciones (" +
		"codigo_pk, " +
		"codigo_pk_opciones, " +
		"codigo_pk_plan_seccion, " +
		"mostrar_modificacion, " +
		"usuario_modifica, " +
		"fecha_modifica, " +
		"hora_modifica) " +
		"(SELECT seq_det_secciones.nextval, codigo_pk_opciones,?,?,?,?,? " +
		"FROM det_secciones " +
		"WHERE codigo_pk_plan_seccion = ?)";
	
	
	//Metodos*****************************************************************************************
	
	
	/**
	 * Inserta informacion de la Plantilla
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarPlantilla(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.insertarPlantilla(con, parametros);
	}
	
	
	/**
	 * Actualiza la informacion de la plantilla
	 * @param Connection con
	 * @param HashMap parametros
	 * **/
	public boolean actualizarPlantilla(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.actualizarPlantilla(con, parametros);
	}
	
	
	/**
	 * Metodo que  consulta la información de la tabla planillas
	 * @param Connection con
	 * @param HashMap parametros 
	 * @param boolean indicadorCargarServicios
	 * */
	public HashMap consultarListadoPlantillas(Connection con,HashMap parametros, boolean indicadorCargarServicios)
	{
		return SqlBasePlantillasDao.consultarListadoPlantillas(con, parametros,indicadorCargarServicios);
	}
	
		
	/**
	 * Método implementado para cargar DTO de Plantilla
	 */
	public DtoPlantilla cargarPlantilla(Connection con,HashMap campos)
	{
		return SqlBasePlantillasDao.cargarPlantilla(con, campos);
	}
	
		
	/**
	 * Inserta informacion en la plantilla secciones fijas
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String strInsertPlantillaSecFija
	 * */
	public int insertarPlantillasSeccionesFijas(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.insertarPlantillasSeccionesFijas(con, parametros);
	}
	
	/**
	 * Actualiza la información de plantillas secciones fijas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarPlantillasSeccionesFijas(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.actualizarPlantillasSeccionesFijas(con, parametros);
	}
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_ingreso
	 */
	public ResultadoBoolean guardarCamposParametrizablesIngreso(Connection con,DtoPlantilla plantilla, HashMap campos)
	{
		return SqlBasePlantillasDao.guardarCamposParametrizablesIngreso(con, plantilla, campos);
	}
	
	/**
	 * Inserta información en secciones parametrizables
	 * @param Connection con 
	 * @param HashMap parametros 
	 * */
	public int insertarSeccionesParametrizables(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.insertarSeccionesParametrizables(con, parametros);
	}
	
	/**
	 * Inserta Plantillas Secciones
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarPlantillasSecciones(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.insertarPlantillasSecciones(con, parametros);
	}
	
	/**
	 * Actualizar secciones parametrizables
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarSeccionesParametrizables(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.actualizarSeccionesParametrizables(con, parametros);
	}
	
	/**
	 * Actualizar el campo mostrar modificacion en  secciones parametrizables
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarMostrarModSeccionParam(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.actualizarMostrarModSeccionParam(con, parametros);
	}	
	
	/**
	 * Consulta la información de la seccion Parametrizable
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoSeccionParametrizable> consultarSeccionesParametrizables(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.consultarSeccionesParametrizables(con, parametros);
	}
	
	/**
	 *  Metodo que inserta una nueva escala.
	 */
	public int insertarEscalaParametrizable(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.insertarEscalaParametrizable(con, mapa);
	}
	
	/**
	 * Metodo que inserta un nuevo componente.
	 */
	public int insertarComponenteParametrizable(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.insertarComponenteParametrizable(con, mapa);
	}
	
	/**
	 * Método para obtener el código de una plantilla
	 */
	public int obtenerCodigoPlantillaXIngreso(Connection con,HashMap campos)
	{
		return SqlBasePlantillasDao.obtenerCodigoPlantillaXIngreso(con, campos);
	}
	
	/**
	 * Método para obtener el código de una plantilla
	 */
	public int obtenerCodigoPlantillaXEvolucion(Connection con,int evolucion)
	{
		return SqlBasePlantillasDao.obtenerCodigoPlantillaXEvolucion(con, evolucion);
	}
	
	/**
	 * Metodo que actualiza un componente de una plantilla.
	 */
	public boolean actualizarComponenteParametrizable(Connection con, HashMap parametros) 
	{
		return SqlBasePlantillasDao.actualizarComponenteParametrizable(con, parametros);
	}

	/**
	 * Metodo que actualiza una escala de una plantilla.
	 */
	public boolean actualizarEscalaParametrizable(Connection con, HashMap parametros) 
	{
		return SqlBasePlantillasDao.actualizarEscalaParametrizable(con, parametros);
	}
	
	/**
	 * Inserta datos en campo parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public int insertarCamposParametrizables(Connection con, HashMap vo)
	{
		return SqlBasePlantillasDao.insertarCamposParametrizables(con, vo);
	}
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param HashMap vo
	 * */
	public int insertarOpcionesCamposParam(Connection con, HashMap vo)
	{
		return SqlBasePlantillasDao.insertarOpcionesCamposParam(con, vo);
	}
	
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param HashMap vo
	 * */
	public int insertarPlantillasCamposSec(Connection con, HashMap vo)
	{
		return SqlBasePlantillasDao.insertarPlantillasCamposSec(con, vo);
	}
	
	/**
	 * Actualiza Mostrar Modificacion de los campos parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public boolean actualizarMostrarModificacionCamposParametrizables(Connection con, HashMap vo) 
	{
		return SqlBasePlantillasDao.actualizarMostrarModificacionCamposParametrizables(con, vo);
	}
	
	/**
	 * Actualiza las Opciones de los Campos Parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public boolean actualizarOpcionesCamposParam(Connection con, HashMap vo) 
	{
		return SqlBasePlantillasDao.actualizarOpcionesCamposParam(con, vo);
	}
	
	
	/**
	 * Actualiza datos en campo parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public boolean actualizarCamposParametrizables(Connection con, HashMap vo)
	{
		return SqlBasePlantillasDao.actualizarCamposParametrizables(con, vo);		
	}
	
	/**
	 * Elimina las opciones de una campo 
	 * @param Connection con 
	 * @param HashMap vo
	 * */
	public boolean eliminarOpcionesCamposSec(Connection con, HashMap vo)
	{
		return SqlBasePlantillasDao.eliminarOpcionesCamposSec(con, vo);
	}	
	
	/**
	 * Inserta Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarResultadosProcRequeridos(Connection con,HashMap parametros)
	{		 
		return SqlBasePlantillasDao.insertarResultadosProcRequeridos(con, parametros);
	}
	
	/**
	 * Actualiza Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarResultadosProcRequeridos(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.actualizarResultadosProcRequeridos(con, parametros); 		
	}
	
	/**
	 * Consultar Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public String consultarResultadosProcRequeridos(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.consultarResultadosProcRequeridos(con, parametros);
	}
	
		
	/**1
	 * Método implementado para guardar los campos parametrizables en plantillas_res_proc
	 * @param Connection con
	 * @param DtoPlantilla plantilla
	 * @param HashMap campos
	 */
	public ResultadoBoolean guardarDatosRespuestaProcedimiento(Connection con,DtoPlantilla plantillaDto, HashMap campos)
	{
		return SqlBasePlantillasDao.guardarDatosRespuestaProcedimiento(con, plantillaDto, campos);
	}
	
	/**
	 * Consulta la informacion basica de Plantillas Respuesta Procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString consultarBasicaPlantillasResProc(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.consultarBasicaPlantillasResProc(con, parametros);
	}

	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_evolucion
	 * @param Connection con
	 * @param DtoPlantilla plantilla
	 * @param HashMap campos
	 */
	public ResultadoBoolean guardarDatosEvolucion(Connection con, DtoPlantilla plantillaDto, HashMap campos) 
	{
		return SqlBasePlantillasDao.guardarDatosEvolucion(con, plantillaDto, campos);
	}
	
	/**
	 * Método para obtener un listado de las escalas del ingreso
	 */
	public ArrayList<DtoEscala> obtenerEscalasIngreso(Connection con,HashMap campos)
	{
		return SqlBasePlantillasDao.obtenerEscalasIngreso(con, campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existePlantillaIngreso(Connection con, int numeroSolicitud)
	{
		return SqlBasePlantillasDao.existePlantillaIngreso(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public boolean existePlantillaEvolucion(Connection con, int evolucion)
	{
		return SqlBasePlantillasDao.existePlantillaEvolucion(con, evolucion);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarCamposExitentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad) 
	{
		return SqlBasePlantillasDao.consultarCamposExitentes(con, plantillaBase, centroCosto, sexo, especialidad);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarSeccionesExistentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad) 
	{
		return SqlBasePlantillasDao.consultarSeccionesExistentes(con, plantillaBase, centroCosto, sexo, especialidad);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarEscalasExitentesComponentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad) 
	{
		return SqlBasePlantillasDao.consultarEscalasExitentesComponentes(con, plantillaBase, centroCosto, sexo, especialidad);
	}
	
	/**
	 * Consulta los textos predeterminados para el campo resultado de la respuesta de procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarTextosRespuestaProc(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.consultarTextosRespuestaProc(con, parametros);
	}
	
	/**
	 * Inserta parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarFormularioRespServ(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.insertarFormularioRespServ(con, parametros);
	}
	
	
	/**
	 * Modifica parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean modificarFormularioRespServ(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.modificarFormularioRespServ(con, parametros);
	}
	
	
	/**
	 * Consulta la informacion de las plantillas Servicios Diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoPlantillaServDiag> consultarPlantillasServDiag(Connection con,HashMap parametros)
	{
		return SqlBasePlantillasDao.consultarPlantillasServDiag(con, parametros);
	}
	
	/**
	 * Consulta los diagnosticos parametrizados necesarios para la parametrizacion de plantillas servicios/diag
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap<String,Object>> consultarDiagnosticosPlantillas(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.consultarDiagnosticosPlantillas(con, parametros);
	}
	
	/**
	 * Eliminar parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarFormularioRespServ(Connection con, HashMap parametros)
	{
		return SqlBasePlantillasDao.eliminarFormularioRespServ(con, parametros);
	}


	/**
	 * 
	 */
	public boolean actualizarSeccionesAsociadasOpciones(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.actualizarSeccionesAsociadasOpciones(con, mapa);
	}


	/**
	 * 
	 */
	public boolean actualizarValoresAsociadosOpciones(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.actualizarValoresAsociadosOpciones(con, mapa);
	}


	/**
	 * 
	 */
	public boolean insertarSeccionesAsociadasOpciones(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.insertarSeccionesAsociadasOpciones(con, mapa);
	}


	/**
	 * 
	 */
	public boolean insertarValoresAsociadosOpciones(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.insertarValoresAsociadosOpciones(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean desasociarSeccionOculta(Connection con, HashMap mapa)
	{
		return SqlBasePlantillasDao.desasociarSeccionOculta(con, mapa);
	}

	/**
	 * 
	 */
	public boolean insertarSeccionesAsociadasOpcionesNuevas(Connection con, HashMap mapa) 
	{
		return SqlBasePlantillasDao.insertarSeccionesAsociadasOpcionesNuevas(con, mapa, strInsertarDetSeccionesNuevos);
	}
	
	/**
	 * Desasocia todas las secciones ocultas que se encuentren asociadas a un seccion por medio de sus campos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  boolean desasociarSeccionesOcultasDeSeccionN(Connection con,HashMap mapa)
	{
		return SqlBasePlantillasDao.desasociarSeccionesOcultasDeSeccionN(con, mapa);
	}
	
	/**
	 * 
	 * @param codigoPkEscala
	 * @param codigoPkPerfilNED_OPCIONAL (SI ES MAYOR 0 EXISTE, DE LO CONTRARIO LA ESCALA VACIA)
	 * @return
	 */
	public DtoEscala cargarEscalaPerfilNed(BigDecimal codigoPkEscala, double codigoPkPerfilNED_OPCIONAL)
	{
		return SqlBasePlantillasDao.cargarEscalaPerfilNed(codigoPkEscala, codigoPkPerfilNED_OPCIONAL);
	}


	@Override
	public DtoPlantilla cargarPlantillaExistentePaciente(Connection con,HashMap campos) {
		
		return SqlBasePlantillasDao.cargarPlantillaExistentePaciente(con,campos);
	}


	@Override
	public int obtenerCodigoPlantillaXPacienteOdont(Connection con, int codigo) {
		
		return SqlBasePlantillasDao.obtenerCodigoPlantillaXPacienteOdont(con, codigo);
	}


	@Override
	public ResultadoBoolean guardarCamposParametrizablesPacOdontologia(
			Connection con, DtoPlantilla plantilla, HashMap campos) {
		
		return SqlBasePlantillasDao.guardarCamposParametrizablesPacOdontologia(con,plantilla,campos);
	}


	@Override
	public ResultadoBoolean modificarCamposParametrizablesPacOdontologia(
			Connection con, DtoPlantilla plantilla, HashMap campos) {
		
		return SqlBasePlantillasDao.modificarCamposParametrizablesPacOdontologia(con,plantilla,campos);
	}
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_ingreso para odontologia
	 */
	public double guardarCamposParametrizablesIngresoOdontologia(Connection con,DtoPlantilla plantilla, HashMap campos)
	{
		return SqlBasePlantillasDao.guardarCamposParametrizablesIngresoOdontologia(con, plantilla, campos);
	}
	
	public boolean consultarPlantillaConIgualNombre(double plantillaBase, String nombre)
	{
		return SqlBasePlantillasDao.consultarPlantillaConIgualNombre(plantillaBase, nombre);
	}


	public DtoComponente cargarComponenteAntecedentesOdonto(HashMap campos)
	{
		return SqlBasePlantillasDao.cargarComponenteAntecedentesOdonto(campos);
		
	}

	@Override
	public int obtenerCodigoPlantillaValoracionConsultaExtOdonto(
			Connection con, HashMap campos) {
		
		return SqlBasePlantillasDao.obtenerCodigoPlantillaValoracionConsultaExtOdonto(con, campos);
	}
	
	public ArrayList<HashMap> obtenerEspecialidadesConPlantillaParametrizada(Connection con, HashMap campos)
	{
		return SqlBasePlantillasDao.obtenerEspecialidadesConPlantillaParametrizada(con, campos);
	}
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_evolucion para odontologia
	 * @param Connection con
	 * @param DtoPlantilla plantilla
	 * @param HashMap campos
	 */
	public int guardarEvolucionOdon(Connection con, DtoPlantilla plantillaDto, HashMap campos) 
	{
		return SqlBasePlantillasDao.guardarEvolucionOdon(con, plantillaDto, campos);
	}
	

	/**
	 * @param Connection con
	 * @param DtoPlantilla plantilla
	 * @param HashMap campos
	 */
	public ArrayList<DtoPlantilla> obtenerListadoPlantillasEnArray(Connection con, HashMap campos) 
	{
		return SqlBasePlantillasDao.obtenerListadoPlantillasEnArray(con,campos);
	}


	@Override
	public DtoComponente cargarComponenteGenericoPreview(HashMap paremetros) {
		
		return SqlBasePlantillasDao.cargarComponenteGenericoPreview(paremetros);
	}
	
	public boolean plantillaValoracionEsUsada(String plantilla)
	{
		return SqlBasePlantillasDao.plantillaValoracionEsUsada(plantilla);
	}
	
	/**
	 * Metodo que obtiene las escalas que son requeridas en una plantilla
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public ArrayList<DtoEscala> obtenerEscalasRequeridas(Connection con, HashMap campos)
	{
		return SqlBasePlantillasDao.obtenerEscalasRequeridas(con,campos);
	}

	/**
	 * Metodo que verifica si se ha guardado información de una escala para una plantilla determinada
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public boolean existeInfoEscala(Connection con, HashMap campos)
	{
		return SqlBasePlantillasDao.existeInfoEscala(con,campos);
	}
	
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#consultarPlantillaFijaSinOrden(java.sql.Connection, java.lang.Integer)
	 */
	public  Boolean consultarPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException
	{
		return SqlBasePlantillasDao.consultarPlantillaFijaSinOrden(con, codigoPkPlantilla, tipoFormato);
	}
	
	

	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#guardarPlantillaSinOrden(java.sql.Connection, java.lang.Integer, java.lang.Boolean, java.lang.String)
	 */
	public void guardarPlantillaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Integer sexo,Integer centroCosto,String tipoFormato) throws SQLException
	{
		 SqlBasePlantillasDao.guardarPlantillaSinOrden(con, codigoPlantilla, visible, nombreSeccion, true, sexo, centroCosto, tipoFormato);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#actualziarPlantillaFijaSinOrden(java.sql.Connection, java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
	 */
	public  void actualziarPlantillaFijaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible ,Integer sexo, Integer centroCosto,String tipoFormato) throws SQLException
	{
		SqlBasePlantillasDao.actualziarPlantillaFijaSinOrden(con, codigoPlantilla, visible, sexo,  centroCosto, tipoFormato);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#consultarVisibilidadPlantillaFijaSinOrden(java.sql.Connection, java.lang.Integer)
	 */
	public  Boolean consultarVisibilidadPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException
	{
		return SqlBasePlantillasDao.consultarVisibilidadPlantillaFijaSinOrden(con, codigoPkPlantilla, tipoFormato);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#consultarVisibilidadPlantillaFijaSinOrdenUrgencias(java.sql.Connection, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public  Boolean consultarVisibilidadPlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException
	{
		return SqlBasePlantillasDao.consultarVisibilidadPlantillaFijaSinOrdenUrgencias(con, codigoPkPlantilla, sexo, centroCosto, tipoFormato);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#existePlantillaFijaSinOrdenUrgencias(java.sql.Connection, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public  Boolean existePlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException
	{
		return SqlBasePlantillasDao.existePlantillaFijaSinOrdenUrgencias(con, codigoPkPlantilla, sexo, centroCosto, tipoFormato);
	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public  Boolean existePlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException{
		return SqlBasePlantillasDao.existePlantillaFijaSinOrdenConsultaExterna(con, codigoPkPlantilla, especialidad);
	}
	
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#guardarPlantillaSinOrdenConsultaExterna(java.sql.Connection, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Integer)
	 */
	public  void guardarPlantillaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Integer especialdiad) throws SQLException
	{
		 SqlBasePlantillasDao.guardarPlantillaSinOrdenConsultaExterna(con, codigoPlantilla, visible, nombreSeccion, false, especialdiad);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#actualziarPlantillaFijaSinOrdenConsultaExterna(java.sql.Connection, java.lang.Integer, java.lang.Boolean, java.lang.Integer)
	 */
	public  void actualziarPlantillaFijaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible ,Integer especialidad) throws SQLException
	{
		SqlBasePlantillasDao.actualziarPlantillaFijaSinOrdenConsultaExterna(con, codigoPlantilla, visible, especialidad);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(java.sql.Connection, java.lang.Integer, java.lang.Integer)
	 */
	public  Boolean consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException
	{
		return SqlBasePlantillasDao.consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(con, codigoPkPlantilla, especialidad);
	}
	
}