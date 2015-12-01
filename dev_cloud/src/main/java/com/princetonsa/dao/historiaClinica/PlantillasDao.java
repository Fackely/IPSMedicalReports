package com.princetonsa.dao.historiaClinica;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosString;
import util.ResultadoBoolean;

import com.princetonsa.dao.sqlbase.historiaClinica.SqlBasePlantillasDao;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

public interface PlantillasDao
{
	
	/**
	 * Inserta informacion de la Plantilla
	 * @param Connection con
	 * @param HashMap parametros	
	 * */
	public int insertarPlantilla(Connection con,HashMap parametros);
	
	/**
	 * Actualiza la informacion de la plantilla
	 * @param Connection con
	 * @param HashMap parametros
	 * **/
	public boolean actualizarPlantilla(Connection con, HashMap parametros);
	
	/**
	 * Metodo que  consulta la información de la tabla planillas
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarListadoPlantillas(Connection con,HashMap parametros, boolean indicadorCargarServicios);
	
	/**
	 * Método implementado para cargar DTO de Plantilla
	 */
	public DtoPlantilla cargarPlantilla(Connection con,HashMap campos);

	
	/**
	 * Inserta informacion en la plantilla secciones fijas
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarPlantillasSeccionesFijas(Connection con,HashMap parametros);
	
	
	/**
	 * Actualiza la información de plantillas secciones fijas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarPlantillasSeccionesFijas(Connection con, HashMap parametros);
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_ingreso
	 */
	public ResultadoBoolean guardarCamposParametrizablesIngreso(Connection con,DtoPlantilla plantilla, HashMap campos);
		
	/**
	 * Inserta información en secciones parametrizables
	 * @param Connection con 
	 * @param HashMap parametros 
	 * */
	public int insertarSeccionesParametrizables(Connection con, HashMap parametros);	
	
	/**
	 * Inserta Plantillas Secciones
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarPlantillasSecciones(Connection con,HashMap parametros);
	
	/**
	 * Actualizar secciones parametrizables
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarSeccionesParametrizables(Connection con, HashMap parametros);
	
	/**
	 * Actualizar el campo mostrar modificacion en  secciones parametrizables
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarMostrarModSeccionParam(Connection con, HashMap parametros);
		
	/**
	 * Consulta la información de la seccion Parametrizable
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoSeccionParametrizable> consultarSeccionesParametrizables(Connection con, HashMap parametros);
	
	/**
	 * Inserta una nueva escala.
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int insertarEscalaParametrizable(Connection con, HashMap mapa);
	
	/**
	 * Inserta un nuevo componente.
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int insertarComponenteParametrizable(Connection con, HashMap mapa);
	
	/**
	 * Método para obtener el código de una plantilla
	 */
	public int obtenerCodigoPlantillaXIngreso(Connection con,HashMap campos);
	
	/**
	 * Método para obtener el código de una plantilla
	 */
	public int obtenerCodigoPlantillaXEvolucion(Connection con,int evolucion);
	
	/**
	 * Metodo que modifica una escala de la plantilla.
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean actualizarEscalaParametrizable(Connection con, HashMap parametros);

	/**
	 * Metodo que modifica una escal de la plantilla.
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean actualizarComponenteParametrizable(Connection con, HashMap parametros);
	
	
	/**
	 * Inserta datos en campo parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public int insertarCamposParametrizables(Connection con, HashMap vo);
	
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param HashMap vo
	 * */
	public int insertarOpcionesCamposParam(Connection con, HashMap vo);
	
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param HashMap vo
	 * */
	public int insertarPlantillasCamposSec(Connection con, HashMap vo);
	
	/**
	 * Actualiza Mostrar Modificacion de los campos parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public boolean actualizarMostrarModificacionCamposParametrizables(Connection con, HashMap vo);
	
	/**
	 * Actualiza las Opciones de los Campos Parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public boolean actualizarOpcionesCamposParam(Connection con, HashMap vo) ;
	
	/**
	 * Actualiza datos en campo parametrizables
	 * @param Connection con
	 * @param HashMap vo
	 * @return
	 */
	public boolean actualizarCamposParametrizables(Connection con, HashMap vo);
	
	/**
	 * Elimina las opciones de una campo 
	 * @param Connection con 
	 * @param HashMap vo
	 * */
	public boolean eliminarOpcionesCamposSec(Connection con, HashMap vo);
	
	/**
	 * Inserta Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarResultadosProcRequeridos(Connection con,HashMap parametros);
	
	/**
	 * Actualiza Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarResultadosProcRequeridos(Connection con,HashMap parametros);
	
	/**
	 * Consultar Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public String consultarResultadosProcRequeridos(Connection con,HashMap parametros);
	
	
	/**1
	 * Método implementado para guardar los campos parametrizables en plantillas_res_proc
	 * @param Connection con
	 * @param DtoPlantilla plantilla
	 * @param HashMap campos
	 */
	public ResultadoBoolean guardarDatosRespuestaProcedimiento(Connection con,DtoPlantilla plantillaDto, HashMap campos);
	
	/**
	 * Consulta los textos predeterminados para el campo resultado de la respuesta de procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarTextosRespuestaProc(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informacion basica de Plantillas Respuesta Procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString consultarBasicaPlantillasResProc(Connection con,HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param plantillaDto
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean guardarDatosEvolucion(Connection con, DtoPlantilla plantillaDto, HashMap campos);
	
	/**
	 * Método para obtener un listado de las escalas del ingreso
	 */
	public ArrayList<DtoEscala> obtenerEscalasIngreso(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existePlantillaIngreso(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public boolean existePlantillaEvolucion(Connection con, int evolucion);
	
	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public HashMap<String, Object> consultarCamposExitentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad);
	
	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public HashMap<String, Object> consultarSeccionesExistentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad);
	
	/**
	 * 
	 * @param con
	 * @param plantillasBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public HashMap<String, Object> consultarEscalasExitentesComponentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad);
	
	/**
	 * Inserta parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarFormularioRespServ(Connection con, HashMap parametros);
	
	
	/**
	 * Modifica parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean modificarFormularioRespServ(Connection con, HashMap parametros);
	
	
	/**
	 * Consulta la informacion de las plantillas Servicios Diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoPlantillaServDiag> consultarPlantillasServDiag(Connection con,HashMap parametros);	
	
	/**
	 * Consulta los diagnosticos parametrizados necesarios para la parametrizacion de plantillas servicios/diag
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap<String,Object>> consultarDiagnosticosPlantillas(Connection con, HashMap parametros);	
	
	/**
	 * Eliminar parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarFormularioRespServ(Connection con, HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarSeccionesAsociadasOpciones(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean actualizarSeccionesAsociadasOpciones(Connection con, HashMap mapa);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarValoresAsociadosOpciones(Connection con, HashMap mapa);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean actualizarValoresAsociadosOpciones(Connection con, HashMap mapa);
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean desasociarSeccionOculta(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarSeccionesAsociadasOpcionesNuevas(Connection con, HashMap mapa);
	
	/**
	 * Desasocia todas las secciones ocultas que se encuentren asociadas a un seccion por medio de sus campos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  boolean desasociarSeccionesOcultasDeSeccionN(Connection con,HashMap mapa);
	
	/**
	 * 
	 * @param codigoPkEscala
	 * @param codigoPkPerfilNED_OPCIONAL (SI ES MAYOR 0 EXISTE, DE LO CONTRARIO LA ESCALA VACIA)
	 * @return
	 */
	public DtoEscala cargarEscalaPerfilNed(BigDecimal codigoPkEscala, double codigoPkPerfilNED_OPCIONAL);
    
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoPlantilla cargarPlantillaExistentePaciente(Connection con, HashMap campos);
	
	
/**
 * 
 * @param con
 * @param codigo
 * @return
 */
	public int obtenerCodigoPlantillaXPacienteOdont(Connection con, int codigo);

	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @param campos
	 * @return
	 */
   public ResultadoBoolean guardarCamposParametrizablesPacOdontologia(Connection con, DtoPlantilla plantilla, HashMap campos);

   
   /**
    * 
    * @param con
    * @param plantilla
    * @param campos
    * @return
    */
	public ResultadoBoolean modificarCamposParametrizablesPacOdontologia(Connection con, DtoPlantilla plantilla, HashMap campos);
	
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_ingreso
	 */
	public double guardarCamposParametrizablesIngresoOdontologia(Connection con,DtoPlantilla plantilla, HashMap campos);
	
	/**
	 * 
	 * @param plantillaBase
	 * @param nombre
	 * @return
	 */
	public boolean consultarPlantillaConIgualNombre(double plantillaBase, String nombre);

	/**
	 * Metodo para obtener el codigo de la plantilla asociada a al Valoracion de Consulta Externa Odontologica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerCodigoPlantillaValoracionConsultaExtOdonto(Connection con, HashMap campos);
	
	
    /**
     * Metodo para Cargar el componente Asociado a la Plantilla de Valoracion y/o  Odontologia
     * @param con
     * @param codPlantilla
     * @return
     */
	public  DtoComponente cargarComponenteAntecedentesOdonto(HashMap campos);
	
	public ArrayList<HashMap> obtenerEspecialidadesConPlantillaParametrizada(Connection con, HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param plantillaDto
	 * @param campos
	 * @return
	 */
	public int guardarEvolucionOdon(Connection con, DtoPlantilla plantillaDto, HashMap campos);
	
	/**
	 * Metodo que  consulta la información de la tabla planillas
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public ArrayList<DtoPlantilla> obtenerListadoPlantillasEnArray(Connection con,HashMap parametros);

	/**
	 * 
	 * @param paremetros
	 * @return
	 */
	public DtoComponente cargarComponenteGenericoPreview(HashMap paremetros);
	
	public boolean plantillaValoracionEsUsada(String plantilla);

	/**
	 * Metodo que obtiene las escalas que son requeridas en una plantilla
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public ArrayList<DtoEscala> obtenerEscalasRequeridas(Connection con, HashMap mapa);

	/**
	 * Metodo que verifica si se ha guardado información de una escala para una plantilla determinada
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public boolean existeInfoEscala(Connection con, HashMap mapa);
	
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return 
	 * @throws SQLException
	 */
	public  Boolean consultarPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param nombreSeccion
	 * @param tipoBD
	 * @throws SQLException
	 */
	public void guardarPlantillaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Integer sexo,Integer centroCosto,String tipoFormato) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param tipoBD
	 * @throws SQLException
	 */
	public  void actualziarPlantillaFijaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible ,Integer sexo, Integer centroCosto,String tipoFormato) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public  Boolean consultarVisibilidadPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param sexo
	 * @param centroCosto
	 * @return
	 * @throws SQLException
	 */
	public  Boolean consultarVisibilidadPlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException;
	
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param sexo
	 * @param centroCosto
	 * @param tipoFormato
	 * @return
	 * @throws SQLException
	 */
	public  Boolean existePlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public  Boolean existePlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException;

	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param nombreSeccion
	 * @param tipoBD
	 * @param especialdiad
	 * @throws SQLException
	 */
	public  void guardarPlantillaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Integer especialdiad) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param especialidad
	 * @throws SQLException
	 */
	public  void actualziarPlantillaFijaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible ,Integer especialidad) throws SQLException;
	
	
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public  Boolean consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException;
	
	
}