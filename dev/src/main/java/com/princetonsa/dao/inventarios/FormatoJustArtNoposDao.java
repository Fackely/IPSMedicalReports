/**
 * 
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dto.inventario.DtoAccionCampo;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author axioma
 *
 */
public interface FormatoJustArtNoposDao {

	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	public HashMap obtenerFormularioParametrizado(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente, UsuarioBasico usuario);
	
	
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public boolean existejustificacion(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente);
	
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public HashMap cargarMedicamentosPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente);
	
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public HashMap cargarSustitutosNoPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente);
	
	
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public HashMap cargarMedicamentosNoPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente);
	

	/**
	 * 
	 * @param con
	 * @param medicamentosPos
	 * @return
	 */
	public HashMap cargarInfoMedicamentosPos(Connection con, HashMap medicamentosPos, String medicamentoPos);

	/**
	 * 
	 * @param con
	 * @param ingresosArticulo
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public HashMap cargarInfoIngreso(Connection con, HashMap ingresosArticulo, String ingreso, String cuenta);
	
	
	/**
	 * 
	 * @param con
	 * @param ingresosArticulo
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public HashMap cargarInfoIngresoJus(Connection con, HashMap ingresosArticulo, String ingreso, String cuenta);
	
	
	
	/**
	 * 
	 * @param con
	 * @param numJustificacion
	 * @return
	 */
	public HashMap consultarDiagnosticos(Connection con, int numJustificacion);
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<String, Object> subCuentas (Connection con, int numeroSolicitud, int art, boolean esArticulo, boolean esOrdenAmbulatoria);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @param articulo
	 */
	public boolean ingresarResponsable(Connection con, String numeroSolicitud, int subcuenta, int cantidad, int articulo);
	
	/**
	 * Metodo para revisar existencias de justificaciones y modificarlas segun distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String Articulo, String usuario);
	
	/**
	 * Metodo para validar Convenio y Articulo No Pos
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public boolean validarArtConvJustificacion(Connection con, String articulo, String subCuenta);
	
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
	public HashMap cargarInfoIngresoRango(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo);
	
	
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
	public HashMap cargarInfoIngresoConsultarModificarRango(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo);
	
	
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
	public HashMap cargarInfoIngresoConsultarModificarRangocon(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo);
	
	
	/**
	 * Actualiza la cantidad de justificacion
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCantidadJustificacion(Connection con,HashMap parametros);
	
	
	/**
	 * 
	 * @param con
	 * @param numerosolicitud
	 * @param mapa
	 * @param observacionesGenerales
	 * @param estado
	 * @return
	 */
	//alejo la frecuencia estaba en int
	public int insertarJustificacion(Connection con, 
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
									);
	
	/**
	 * Permite consultar el codigo de una justificacion no pos de un articulo que proviene de una solicitud
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @param codigoArticulo
	 * @return
	 * @author jeilones
	 * @created 25/02/2012
	 */
	
	public Integer consultarCodigoJustificacionSolicitud(Connection con,int codigoSolicitud,int codigoArticulo);
		
		
	/**
	 * Método que evalua si el convenio dado
	 * tiene parametrizado la justificacion de
	 * artículos en 'S' ó en 'N'
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public String requiereJustificacionArticuloConvenio(Connection con, String codConvenio);
	
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
	public void actualizarCantidadJustificacionRespMezcla(Connection con, int cantidad, int numeroSolicitud, int articulo);
	
	/**
	 * Metodo para completar la busqueda de la justificacion no pos por consolidado
	 * @param cual
	 * @param cadena
	 * @return	 */
	public String consultaCompletaConsolidado(int cual, String cadena);

	/**
	 * Método que inserta el registro del responsable de
	 * la justificación del articulo con su correspondiente 
	 * cantidad
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public boolean insertarResponsableJustificacion(Connection con, int numeroSolicitud, int articulo, String subCuenta, String cantidad, int codigoJustificacionSolicitud);

	/**
	 * Método que inserta el registro del responsable de
	 * la justificación del articulo con su correspondiente 
	 * cantidad
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public boolean insertarResponsableJustificacionServicio(Connection con, int numeroSolicitud, int servicio, String subCuenta, String cantidad, int codigoJustificacionSolicitud) throws BDException;


	/**
	 * Metodo que consulta una justificación historica
	 * @param con
	 * @param medicamentoNoPos2
	 * @param codigoSolicitud2
	 * @return
	 */
	public HashMap<String, Object> consultarJustificacionHistorica( Connection con, String medicamentoNoPos, String codigoSolicitud);


	/**
	 * Consutlar las acciones que tiene un campo sobre otros
	 * 
	 * @param idCampo identificador del campo que tiene la accion
	 * @param servicio 
	 * @return 
	 */
	public List<DtoAccionCampo> consultarAccionesCampo(Connection con, int idCampo,
			String servicio);
	
	/**
	 * Consultar la observacion de resumen que justifica el uso de medicamentos NO POS 
	 * 
	 * @param con
	 * @param codigoSolicitudOrden
	 * @param codigoArticulo
	 * @return
	 * @throws Exception
	 */
	public String consultarObservacionesResumenNoPOS(Connection con,String codigoSolicitudOrden,String codigoArticulo, boolean esOrdenAmbulatoria) throws Exception;
	
}