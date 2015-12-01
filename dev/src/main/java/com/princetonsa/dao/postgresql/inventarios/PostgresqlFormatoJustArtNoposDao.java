/**
 * 
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.FormatoJustArtNoposDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseFormatoJustArtNoposDao;
import com.princetonsa.dto.inventario.DtoAccionCampo;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author axioma
 *
 */
public class PostgresqlFormatoJustArtNoposDao implements FormatoJustArtNoposDao {

	public HashMap obtenerFormularioParametrizado(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente, UsuarioBasico usuario) {
		
		return SqlBaseFormatoJustArtNoposDao.obtenerFormularioParametrizado(con,fjan, paciente, usuario);
	}

	public HashMap cargarMedicamentosPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente) {
		String ingresos = "";
		if (fjan.getEmisor().equals("solicitud")
				|| fjan.getEmisor().equals("ingresar")) {
			ingresos = "select id as codigo from ingresos where estado='CER' and  codigo_paciente ="
					+ paciente.getCodigoPersona()
					+ " "
					+ " order by fecha_ingreso,hora_ingreso DESC limit 1";
		} else if (fjan.getEmisor().equals("modificar")
				|| fjan.getEmisor().equals("consultar")) {
			ingresos = "select id as codigo from ingresos where estado='CER' and  codigo_paciente ="
					+ fjan.getFormularioMap().get("codigopersona")
					+ " "
					+ " order by fecha_ingreso,hora_ingreso DESC limit 1";
		}
		
		
		return SqlBaseFormatoJustArtNoposDao.cargarMedicamentosPos(con,fjan,paciente,ingresos);
	}
	
	public HashMap cargarMedicamentosNoPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente) {
		return SqlBaseFormatoJustArtNoposDao.cargarMedicamentosNoPos(con,fjan,paciente);
	}

	
	public HashMap cargarInfoMedicamentosPos(Connection con, HashMap medicamentosPos, String medicamentoPos) {
		return SqlBaseFormatoJustArtNoposDao.cargarInfoMedicamentosPos(con,medicamentosPos, medicamentoPos);
	}

	public HashMap cargarSustitutosNoPos(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente) {
		return SqlBaseFormatoJustArtNoposDao.cargarSustitutosNoPos(con,fjan, paciente);
		}

	public HashMap consultarDiagnosticos(Connection con, int numJustificacion) {
		return SqlBaseFormatoJustArtNoposDao.consultarDiagnosticos(con, numJustificacion);
	}

	public boolean existejustificacion(Connection con, FormatoJustArtNopos fjan, PersonaBasica paciente) {
		return SqlBaseFormatoJustArtNoposDao.existejustificacion(con, fjan, paciente);
	}
	
	/**
	 * Metodo para revisar existencia de Justificaciones y modificarlas segun distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String articulo, String usuario)
	{
		return SqlBaseFormatoJustArtNoposDao.revisarJustificacionDistribucion(con, numSol, subCuenta, cantidad, articulo, usuario);
	}
	
	/**
	 * Metodo para validar Convenio y Articulo No Pos
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public boolean validarArtConvJustificacion(Connection con, String articulo, String subCuenta)
	{
		return SqlBaseFormatoJustArtNoposDao.validarArtConvJustificacion(con, articulo, subCuenta);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> subCuentas(Connection con, int numeroSolicitudOrden, int art, boolean esArticulo, boolean esOrdenAmbulatoria)
	{
		return SqlBaseFormatoJustArtNoposDao.subCuentas(con, numeroSolicitudOrden, art, esArticulo, esOrdenAmbulatoria);
	}
	
	
	/**
	 * Metodo que inserta las justificaciones de los articulos.
	 */
	//frecuencia estaba en int
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
									)
	{
		return SqlBaseFormatoJustArtNoposDao.insertarJustificacion(con, 
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
	 * Permite consultar el codigo de una justificacion no pos de un articulo que proviene de una solicitud
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @param codigoArticulo
	 * @return
	 * @author jeilones
	 * @created 3/12/2012
	 */
	
    public Integer consultarCodigoJustificacionSolicitud(Connection con,int codigoSolicitud,int codigoArticulo){
		
		return SqlBaseFormatoJustArtNoposDao.consultarCodigoJustificacionSolicitud(con,codigoSolicitud, codigoArticulo);
		
	}

	/**
	 * Metodo que carga los articulos relacionados a ese ingreso
	 */
	public HashMap cargarInfoIngreso(Connection con, HashMap ingresosArticulo, String ingreso, String cuenta) {
		return SqlBaseFormatoJustArtNoposDao.cargarInfoIngreso(con,ingresosArticulo, ingreso, cuenta);
	}

	/**
	 * Metodo que inserta los registros de las subcuentas responsables del articulo justificado.
	 */
	public boolean ingresarResponsable(Connection con, String numeroSolicitud, int subcuenta, int cantidad, int articulo) {
		return SqlBaseFormatoJustArtNoposDao.ingresarResponsable(con, numeroSolicitud, subcuenta, cantidad, articulo);
	}	
	
	public HashMap cargarInfoIngresoJus(Connection con, HashMap ingresosArticulo, String ingreso, String cuenta) {
		return SqlBaseFormatoJustArtNoposDao.cargarInfoIngresoJus(con,ingresosArticulo, ingreso, cuenta);
	}
	
	
	public HashMap cargarInfoIngresoRango(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo){
		String where = " FROM "
			+ "	ingresos i "
			+ " inner join "
			+ "	cuentas c on (c.id_ingreso=i.id) "
			+ " inner join "
			+ "	solicitudes s on (c.id=s.cuenta) "
			+ " inner join "
			+ "	sub_cuentas sc on (sc.ingreso=i.id) "
			+ " inner join "
			+ "	jus_pendiente_articulos jpa on (s.numero_solicitud=jpa.numero_solicitud) "
			+ " inner join "
			+ "	articulo art on (art.codigo=jpa.articulo) " + " where "
			+ "	  c.estado_cuenta <> "
			+ ConstantesBD.codigoEstadoCuentaCerrada
			+ " and sc.nro_prioridad=1"
			+ " and s.estado_historia_clinica <> "
			+ ConstantesBD.codigoEstadoHCAnulada + " "
			+ " and s.fecha_solicitud BETWEEN '"
			+ UtilidadFecha.conversionFormatoFechaABD(fechaini) + "' and '"
			+ UtilidadFecha.conversionFormatoFechaABD(fechafin) + "' "
			+ " and i.centro_atencion=";
		
		if(filtro.containsKey("centroAtencionCod")){
			where=where + filtro.get("centroAtencionCod")+" ";
		}else{
			where=where + filtro.get("centroAtencion")+" ";
		}
		
		
		
		return SqlBaseFormatoJustArtNoposDao.cargarInfoIngresoRango(con, ingresosMap, filtro, articulos, fechaini, fechafin, tipocodigo,where );
	}
	
	public HashMap cargarInfoIngresoConsultarModificarRango(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo){
		
		String where = " where " + "	  c.estado_cuenta<>"
		+ ConstantesBD.codigoEstadoCuentaCerrada
		+ " and sc.nro_prioridad=1"
		+ " and s.estado_historia_clinica <> "
		+ ConstantesBD.codigoEstadoHCAnulada + " "
		+ " and jas.fecha BETWEEN '"
		+ UtilidadFecha.conversionFormatoFechaABD(fechaini) + "' and '"
		+ UtilidadFecha.conversionFormatoFechaABD(fechafin) + "' "
		+ " and i.centro_atencion=" + filtro.get("centroAtencion")
		+ " ";
		

		
		return SqlBaseFormatoJustArtNoposDao.cargarInfoIngresoConsultarModificarRango(con, ingresosMap, filtro, articulos, fechaini, fechafin, tipocodigo,where);
	}
	
	public HashMap cargarInfoIngresoConsultarModificarRangocon(Connection con, HashMap ingresosMap, HashMap filtro, String articulos,String fechaini, String fechafin, int tipocodigo){
		
		
		String where = " where " + "	  c.estado_cuenta <> "
		+ ConstantesBD.codigoEstadoCuentaCerrada
		+ " and sc.nro_prioridad=1"
		+ " and s.estado_historia_clinica <> "
		+ ConstantesBD.codigoEstadoHCAnulada + " "
		+ " and jas.fecha BETWEEN '"
		+ UtilidadFecha.conversionFormatoFechaABD(fechaini) + "' and '"
		+ UtilidadFecha.conversionFormatoFechaABD(fechafin) + "' "
		+ " and i.centro_atencion=" + filtro.get("centroAtencion")
		+ " ";
		
		
		String group = "group by ";
		if (filtro.get("tipoRompimiento").toString().equals("centro_costo_"))
			group += "codigo_centro_costo,";
		group += "articulo," + "codigo_art,";
		if (filtro.get("tipoRompimiento").toString().equals("piso_"))
			group += "piso,";
		if (filtro.get("tipoRompimiento").toString().equals("estadojus_"))
			group += "estadojus,";

		group += "codigomed, control " + "ORDER BY  codigomed, articulo asc";
		
		
		
		return SqlBaseFormatoJustArtNoposDao.cargarInfoIngresoConsultarModificarRangocon(con, ingresosMap, filtro, articulos, fechaini, fechafin, tipocodigo,where,group );
	}
	
	public String requiereJustificacionArticuloConvenio(Connection con, String codConvenio){
		return SqlBaseFormatoJustArtNoposDao.requiereJustificacionArticuloConvenio(con, codConvenio);
	}
	
	/**
	 * Actualiza la cantidad de justificacion
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCantidadJustificacion(Connection con,HashMap parametros)
	{
		return SqlBaseFormatoJustArtNoposDao.actualizarCantidadJustificacion(con, parametros);		
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
	public void actualizarCantidadJustificacionRespMezcla(Connection con, int cantidad, int numeroSolicitud, int articulo)
	{
		SqlBaseFormatoJustArtNoposDao.actualizarCantidadJustificacionRespMezcla(con, cantidad, numeroSolicitud, articulo);
	}

	/**
	 * Metodo para completar la busqueda de la justificacion no pos por consolidado
	 * @param cual
	 * @param cadena
	 * @return	 */
	public String consultaCompletaConsolidado(int cual, String cadena) {
		return SqlBaseFormatoJustArtNoposDao.consultaCompletaConsolidado(cual, cadena);
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
	 * @return
	 */
	public boolean insertarResponsableJustificacion(Connection con, int numeroSolicitud, int articulo, String subCuenta, String cantidad, int codigoJustificacionSolicitud)
	{
		return SqlBaseFormatoJustArtNoposDao.insertarResponsableJustificacion(con, numeroSolicitud, articulo, subCuenta, cantidad, codigoJustificacionSolicitud);
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
	 * @return
	 */
	public boolean insertarResponsableJustificacionServicio(Connection con, int numeroSolicitud, int servicio, String subCuenta, String cantidad, int codigoJustificacionSolicitud) throws BDException
	{
		return SqlBaseFormatoJustArtNoposDao.insertarResponsableJustificacionServicio(con, numeroSolicitud, servicio, subCuenta, cantidad, codigoJustificacionSolicitud);
	}
	
	/**
	 * Metodo que consulta una justificación historica
	 * @param con
	 * @param medicamentoNoPos2
	 * @param codigoSolicitud2
	 * @return
	 */
	public HashMap<String, Object> consultarJustificacionHistorica(Connection con, String medicamentoNoPos, String codigoSolicitud)
	{
		return SqlBaseFormatoJustArtNoposDao.consultarJustificacionHistorica(con, medicamentoNoPos, codigoSolicitud);
	}

	@Override
	public List<DtoAccionCampo> consultarAccionesCampo(Connection con, int idCampo,
			String servicio) {
		// TODO Auto-generated method stub
		return SqlBaseFormatoJustArtNoposDao.consultarAccionesCampo(con, idCampo,servicio);
	}

	@Override
	public String consultarObservacionesResumenNoPOS(Connection con,
			String codigoSolicitudOrden, String codigoArticulo, boolean esOrdenAmbulatoria) throws Exception {
		// TODO Auto-generated method stub
		return SqlBaseFormatoJustArtNoposDao.consultarObservacionesResumenNoPOS(con, codigoSolicitudOrden, codigoArticulo, esOrdenAmbulatoria);
	}
}