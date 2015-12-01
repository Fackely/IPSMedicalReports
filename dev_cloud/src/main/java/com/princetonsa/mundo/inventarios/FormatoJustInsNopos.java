/**
 * 
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.FormatoJustArtNoposDao;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author axioma
 *
 */
public class FormatoJustInsNopos {
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(FormatoJustArtNopos.class);

    /**
	  * HashMap Mapa donde se almacenan los mapas de cada una de las secciones del formulario. 
	 */
	private HashMap formularioMap;

	
	/**
	 * String que almacena el numero de la solicitud
	 */
	private String codigoSolicitud;

	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static FormatoJustArtNoposDao FormatoJustArtNoposDao;
	
	
	/**
	 * indices mapa
	 */
	public static String [] indicesformulariomap={};

	
	/**
	 * indices mapa
	 */
	public static String [] indicesposmap={};
		
	/**
	 * indices mapa
	 */
	public static String [] indicesnoposmap={};
	
	/**
	 * indices mapa
	 */
	public static String [] indicessustimap={};
		
	
	/**
	 * indices mapa
	 */
	public static String [] indicesdiagmap={};
		
	
	/**
	 * indices mapa
	 */
	public static String [] indicesdiag1map={};
	
	
	/**
	 * subcuenta
	 */
	private String subcuenta;
	
	
	
	private String unidosisl;
	
	
	
//parametros enviados desde el emisor
	
	/**
	 * String donde se almacena el valor del codigo del medicamento no pos a justificar, este parametro se envia desde donde se hace 
	 * el llamado a la justificacion
	 */
	private String medicamentoNoPos;
	
	/**
	 *  valor de la dosis enviada desde donde se efectua el llamado
	 */
	private String dosis;
	/**
	 * valor de la unidosis enviada desde donde se efectua el llamado
	 */
	private int unidosis;
	/**
	 * valor de la frecuencia enviada desde donde se efectua el llamado
	 */
	private String frecuencia;
	/**
	 * valor del tipo de frecuencia enviado desde donde se efectua el llamado
	 */
	private String tipoFrecuencia;
	/**
	 * valor del total de unidades enviado desde donde se efectua el llamado
	 */
	private int totalUnidades;
	
	/**
	 * valor del tiempo del tratmaiento en dias este parametro es enviado de donde se efectue el llamado al formulario 
	 */
	private int tiempoTratamiento;
	
	/**
	 * mapa medicamentos no pos
	 */
	private HashMap medicamentosNoPos;
	
	
	/**
	 * variable que controla de que flujo se hace llamado al formulario
	 */
	private String emisor;
	
	/**
	 * Subcuenta
	 */
	private String subCuenta;
	

	
	//%%%%%%%%% datos anexo 584 %%%%%%
	
	private String convenio;
	private String cuenta;
	private String centroCosto;
	private String ingreso;
	
	
	
	
	
	
	//METODOS DEL MUNDO 
	
	
	
	/**
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * 
	 *
	 */
	public FormatoJustInsNopos() 
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	private void reset() 
	{
		this.frecuencia = "";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			FormatoJustArtNoposDao = myFactory.getFormatoJustArtNoposDao();
			wasInited = (FormatoJustArtNoposDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap obtenerFormularioParametrizado(Connection con,FormatoJustArtNopos fjan, PersonaBasica paciente, UsuarioBasico usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().obtenerFormularioParametrizado(con, fjan, paciente,usuario);
	}
	
	
	
	public static boolean existejustificacion(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().existejustificacion(con,fjan,paciente);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarMedicamentosPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarMedicamentosPos(con, fjan, paciente);
	}

	
	/**
	 * 
	 * @param con
	 * @param numJustificacion
	 * @return
	 */
	public static HashMap consultarDiagnosticos(Connection con, int numJustificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().consultarDiagnosticos(con, numJustificacion);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarMedicamentosNoPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarMedicamentosNoPos(con, fjan, paciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @return
	 */
	public static HashMap cargarInfoIngresoRango(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarInfoIngresoRango(con, ingresosMap, filtro, articulos, fechaini, fechafin, tipocodigo);
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRango(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarInfoIngresoConsultarModificarRango(con, ingresosMap, filtro, articulos, fechaini, fechafin, tipocodigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRangocon(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarInfoIngresoConsultarModificarRangocon(con, ingresosMap, filtro, articulos, fechaini, fechafin, tipocodigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarSustitutosNoPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarSustitutosNoPos(con, fjan, paciente);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param medicamentosPos
	 * @return
	 */
	public static HashMap cargarInfoMedicamentosPos(Connection con, HashMap medicamentosPos,String medicamentoPos){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarInfoMedicamentosPos(con, medicamentosPos, medicamentoPos);
		
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param ingresosArticulos
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public static HashMap cargarInfoIngreso(Connection con, HashMap ingresosArticulos,String ingreso,String cuenta){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarInfoIngreso(con, ingresosArticulos, ingreso, cuenta);
		
	}
	

	/**
	 * 
	 * @param con
	 * @param ingresosArticulos
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public static HashMap cargarInfoIngresoJus(Connection con, HashMap ingresosArticulos,String ingreso,String cuenta){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().cargarInfoIngresoJus(con, ingresosArticulos, ingreso, cuenta);
		
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> SubCuentas (Connection con, int numeroSolicitud, int art, boolean esArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().subCuentas(con, numeroSolicitud, art, esArticulo,/* x cambiar*/true);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @param articulo
	 */
	public static boolean ingresarResponsable(Connection con, String numeroSolicitud, int subcuenta, int cantidad, int articulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().ingresarResponsable(con, numeroSolicitud, subcuenta, cantidad, articulo);
	}
	
	/**
	 * Metodo para revisar existencia de justificaciones y modificarlas segun distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public static int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String articulo, String usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().revisarJustificacionDistribucion(con, numSol, subCuenta, cantidad, articulo, usuario);
	}
	
	/**
	 * Metodo para validar COnvenio y Articulo No POs
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public static boolean validarArtConvJustificacion(Connection con, String articulo, String subCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().validarArtConvJustificacion(con, articulo, subCuenta);
	}
	
	/**
	 * Actualiza la cantidad de justificacion
	 * @param Connection con
	 * @param String codigoPk
	 * @param String cantidad
	 * */
	public static boolean actualizarCantidadJustificacion(Connection con,String codigoPk,String cantidad)
	{
		HashMap parametros = new HashMap();
		parametros.put("cantidad",cantidad);
		parametros.put("codigoPk",codigoPk);		
		return FormatoJustArtNoposDao.actualizarCantidadJustificacion(con, parametros);
	}
	
	/**
	 * Actualiza la cantidad de la justificacion resp para una mezcla, 
	 * este llamdo se hace desde el despacho de med, 
	 * donde solamente puede haber un despacho final, 
	 * es decir, 
	 * en ese punto no puede haber distribucion, 
	 * por tal motivo solo es requerido el numero solicitud articulo
	 * @param con
	 * @param cantidad
	 * @param numeroSolicitud
	 * @param articulo
	 * @return
	 */
	public static void actualizarCantidadJustificacionRespMezcla(Connection con, int cantidad, int numeroSolicitud, int articulo)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().actualizarCantidadJustificacionRespMezcla(con, cantidad, numeroSolicitud, articulo);
	}
	
	
	/**
	 * Para insertar la justificacion es necesario 
	 * el numero de solicitud o la orden ambulatoria
	 * ambas son excluyentes.
	 * @param con
	 * @param numerosolicitud
	 * @param ordenAmbulatoria
	 * @param mapa
	 * @param MedicamentosNoPosMap
	 * @param MedicamentosPosMap
	 * @param SustitutosNoPosMap
	 * @param DiagnosticosDefinitivos
	 * @param i
	 * @param ins
	 * @param observacionesGenerales
	 * @param estado
	 * @param articulo
	 * @param observaciones
	 * @param dosis
	 * @param unidosis
	 * @param frecuencia
	 * @param tipoFrecuencia
	 * @param via
	 * @param cantidadSolicitada
	 * @param diasTratamiento
	 * @param usuario
	 * @return
	 */
	public static int insertarJustificacion (Connection con, 
													int numerosolicitud,
													int ordenAmbulatoria,
													HashMap mapa,
													HashMap MedicamentosNoPosMap,
													HashMap MedicamentosPosMap,
													HashMap SustitutosNoPosMap,
													HashMap DiagnosticosDefinitivos,
													int i,
													int ins, 
													String observacionesGenerales, 
													String estado, 
													int articulo, 
													String observaciones, 
													String dosis, 
													String unidosis, 
													int frecuencia, 
													String tipoFrecuencia, 
													String via, 
													double cantidadSolicitada, 
													String diasTratamiento,
													String usuario
													)
	{
	
		logger.info("\n\nGenerar Justificacion >> numerosolicitud >> "+numerosolicitud+" estado >> "+estado+" >> cantidadSolicitada >> "+cantidadSolicitada+" ");
	
		return	FormatoJustArtNoposDao.insertarJustificacion(	 con, 
																 numerosolicitud,
																 ordenAmbulatoria,
																 mapa,
																 MedicamentosNoPosMap,
																 MedicamentosPosMap,
																 SustitutosNoPosMap,
																 DiagnosticosDefinitivos,
																 i,
																 ins, 
																 observacionesGenerales, 
																 estado, 
																 articulo, 
																 observaciones, 
																 dosis, 
																 unidosis, 
																 frecuencia, 
																 tipoFrecuencia, 
																 via, 
																 cantidadSolicitada, 
																 diasTratamiento,
																 usuario
																);
	}
	
	/**
	 * Método que evalua si los N responsables de la cuenta requieren 
	 * justificación de artículos. Devuelve una cadena de String
	 * en donde:
	 * ----> [0] es la Subcuenta
	 * ----> [1] es el Convenio
	 * @param con
	 * @param idIngreso
	 * @param codigoUltimaViaIngreso
	 * @param idCuenta
	 * @return
	 */
	public String[] requiereJustificacionArticulo(Connection con, String idIngreso, int codigoUltimaViaIngreso, String idCuenta) throws IPSException
	{
		String[] subCuentaConvenio = new String [2];
		//Reseteamos los Campos del Convenio y la Subcuenta
		subCuentaConvenio[0] = "";
		subCuentaConvenio[1] = "";
		String requiereJustificacion = "";
		DtoSubCuentas dtoSubCuenta = new DtoSubCuentas();
		
		//Obtenemos los Responsables de la Cuenta
		ArrayList<DtoSubCuentas> dtoSubCuentasVector = UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, Integer.parseInt(idIngreso), false, new String[0], false, "" /*subCuenta*/, codigoUltimaViaIngreso);
		int numRegistros = dtoSubCuentasVector.size();
		//Iteramos los N Responsables de la Cuenta
		for(int w=0; w < numRegistros; w++)
		{
			dtoSubCuenta = dtoSubCuentasVector.get(w);
			//Consultamos si el Convenio requiere Justificación de Artículos
			requiereJustificacion = FormatoJustArtNoposDao.requiereJustificacionArticuloConvenio(con, dtoSubCuenta.getConvenio().getCodigo()+"");
			//Si el Convenio Requiere Justificación. Salimos del ciclo y asignamos el Valor del Convenio y de la Subcuenta
			if(requiereJustificacion.equals(ConstantesBD.acronimoSi))
			{
				w = numRegistros;
				//Adicionamos la Subcuenta y el Convenio para ese Convenio que requiere justificación de artículos
				subCuentaConvenio[0] = dtoSubCuenta.getSubCuenta();
				subCuentaConvenio[1] = dtoSubCuenta.getConvenio().getCodigo()+"";
			}
		}
		return subCuentaConvenio;
	}

	
	
	/**	 * Metodo para completar sql del reporte
	 * @param cual
	 * @param cadena
	 * @return	 */
	public String consultaCompletaConsolidado(int cual, String cadena) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().consultaCompletaConsolidado(cual, cadena);
	}
	
	
	
	
/**
 * Getters y Setters
 */	

	/**
	 * @return the formularioMap
	 */
	public HashMap getFormularioMap() {
		return formularioMap;
	}

	/**
	 * @param formularioMap the formularioMap to set
	 */
	public void setFormularioMap(HashMap formularioMap) {
		this.formularioMap = formularioMap;
	}


	/**
	 * @return the codigoSolicitud
	 */
	public String getCodigoSolicitud() {
		return codigoSolicitud;
	}

	/**
	 * @param codigoSolicitud the codigoSolicitud to set
	 */
	public void setCodigoSolicitud(String codigoSolicitud) {
		this.codigoSolicitud = codigoSolicitud;
	}

	/**
	 * @return the medicamentoNoPos
	 */
	public String getMedicamentoNoPos() {
		return medicamentoNoPos;
	}

	/**
	 * @param medicamentoNoPos the medicamentoNoPos to set
	 */
	public void setMedicamentoNoPos(String medicamentoNoPos) {
		this.medicamentoNoPos = medicamentoNoPos;
	}

	/**
	 * @return the dosis
	 */
	public String getDosis() {
		return dosis;
	}

	/**
	 * @param dosis the dosis to set
	 */
	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	/**
	 * @return the frecuencia
	 */
	public String getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return the tiempoTratamiento
	 */
	public int getTiempoTratamiento() {
		return tiempoTratamiento;
	}

	/**
	 * @param tiempoTratamiento the tiempoTratamiento to set
	 */
	public void setTiempoTratamiento(int tiempoTratamiento) {
		this.tiempoTratamiento = tiempoTratamiento;
	}

	/**
	 * @return the tipoFrecuencia
	 */
	public String getTipoFrecuencia() {
		return tipoFrecuencia;
	}

	/**
	 * @param tipoFrecuencia the tipoFrecuencia to set
	 */
	public void setTipoFrecuencia(String tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

	/**
	 * @return the totalUnidades
	 */
	public int getTotalUnidades() {
		return totalUnidades;
	}

	/**
	 * @param totalUnidades the totalUnidades to set
	 */
	public void setTotalUnidades(int totalUnidades) {
		this.totalUnidades = totalUnidades;
	}

	/**
	 * @return the unidosis
	 */
	public int getUnidosis() {
		return unidosis;
	}

	/**
	 * @param unidosis the unidosis to set
	 */
	public void setUnidosis(int unidosis) {
		this.unidosis = unidosis;
	}

	/**
	 * @return the medicamentosNoPos
	 */
	public HashMap getMedicamentosNoPos() {
		return medicamentosNoPos;
	}
	/**
	 * @return the medicamentosNoPos
	 */
	public Object getMedicamentosNoPos(String key) {
		return medicamentosNoPos.get(key);
	}

	/**
	 * @param medicamentosNoPos the medicamentosNoPos to set
	 */
	public void setMedicamentosNoPos(HashMap medicamentosNoPos) {
		this.medicamentosNoPos = medicamentosNoPos;
	}

	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the indicesformulariomap
	 */
	public static String[] getIndicesformulariomap() {
		return indicesformulariomap;
	}

	
	/**
	 * @return the indicesformulariomap
	 */
	public  void setIndicesformulariomap(String[] o) {
		this.indicesformulariomap=o;
	}

	/**
	 * @return the indicesnoposmap
	 */
	public static String[] getIndicesnoposmap() {
		return indicesnoposmap;
	}

	/**
	 * @param indicesnoposmap the indicesnoposmap to set
	 */
	public static void setIndicesnoposmap(String[] indicesnoposmap) {
		FormatoJustArtNopos.indicesnoposmap = indicesnoposmap;
	}

	/**
	 * @return the indicesposmap
	 */
	public static String[] getIndicesposmap() {
		return indicesposmap;
	}

	/**
	 * @param indicesposmap the indicesposmap to set
	 */
	public static void setIndicesposmap(String[] indicesposmap) {
		FormatoJustArtNopos.indicesposmap = indicesposmap;
	}

	/**
	 * @return the indicessustimap
	 */
	public static String[] getIndicessustimap() {
		return indicessustimap;
	}

	/**
	 * @param indicessustimap the indicessustimap to set
	 */
	public static void setIndicessustimap(String[] indicessustimap) {
		FormatoJustArtNopos.indicessustimap = indicessustimap;
	}

	/**
	 * @return the indicesdiag1map
	 */
	public static String[] getIndicesdiag1map() {
		return indicesdiag1map;
	}

	/**
	 * @param indicesdiag1map the indicesdiag1map to set
	 */
	public static void setIndicesdiag1map(String[] indicesdiag1map) {
		FormatoJustArtNopos.indicesdiag1map = indicesdiag1map;
	}

	/**
	 * @return the indicesdiagmap
	 */
	public static String[] getIndicesdiagmap() {
		return indicesdiagmap;
	}

	/**
	 * @param indicesdiagmap the indicesdiagmap to set
	 */
	public static void setIndicesdiagmap(String[] indicesdiagmap) {
		FormatoJustArtNopos.indicesdiagmap = indicesdiagmap;
	}

	/**
	 * @return the subcuenta
	 */
	public String getSubcuenta() {
		return subcuenta;
	}

	/**
	 * @param subcuenta the subcuenta to set
	 */
	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	public String getUnidosisl() {
		return unidosisl;
	}

	public void setUnidosisl(String unidosisl) {
		this.unidosisl = unidosisl;
	}

	/**
	 * Método que inserta el registro del responsable de
	 * la justificación del articulo con su correspondiente 
	 * cantidad
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param subCuenta
	 * @param cantidad
	 * @param codigoJustificacionSolicitud 
	 * @return
	 */
	public static boolean insertarResponsableJustificacion(Connection con, int numeroSolicitud, int articulo, String subCuenta, String cantidad, int codigoJustificacionSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().insertarResponsableJustificacion(con, numeroSolicitud, articulo, subCuenta, cantidad, codigoJustificacionSolicitud);
	}

	/**
	 * Método que inserta el registro del responsable de
	 * la justificación del articulo con su correspondiente 
	 * cantidad
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param subCuenta
	 * @param cantidad
	 * @param codigoJustificacionSolicitud 
	 * @return
	 */
	public static boolean insertarResponsableJustificacionServicio(Connection con, int numeroSolicitud, int servicio, String subCuenta, String cantidad, int codigoJustificacionSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().insertarResponsableJustificacionServicio(con, numeroSolicitud, servicio, subCuenta, cantidad, codigoJustificacionSolicitud);
	}

	/**
	 * Metodo que consulta una justificación historica
	 * @param con
	 * @param medicamentoNoPos2
	 * @param codigoSolicitud2
	 * @return
	 */
	public static HashMap<String, Object> consultarJustificacionHistorica(Connection con, String medicamentoNoPos, String codigoSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustArtNoposDao().consultarJustificacionHistorica(con, medicamentoNoPos, codigoSolicitud);
	}
	
}