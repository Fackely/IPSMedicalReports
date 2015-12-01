package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;


/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre de 2008 */


public class Servicios_ArticulosIncluidosEnOtrosProcedimientos {
	
	// ATRIBUTOS LOGGER
	public static Logger logger = Logger.getLogger(Servicios_ArticulosIncluidosEnOtrosProcedimientos.class);

	/**	 * Se inicializa el Dao	 */
	public static Servicios_ArticulosIncluidosEnOtrosProcedimientosDao Servicios_ArticulosIncluidosEnOtrosProcedimientosDao() {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServicios_ArticulosIncluidosEnOtrosProcedimientosDao();
	}
	
	/**  * Método inicial de la funcionalidad, la cual se encarga de cargar en un mapa los resultados arrojados
	 * por la consulta de Servicios_Articulos Incluidos En Otros Procedimientos
	 * @param tarifarioServicio 
	 *  @param (con, codigoInstitucionInt)
	 * @return	 */
	public HashMap consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, int codigoInstitucionInt, String tarifarioServicio) {
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(con, codigoInstitucionInt, tarifarioServicio);
	}

	/**	 *	 */
	public HashMap cargarArtIncluServiPpal(Connection con, int criterios) 
	{		
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().cargarArtIncluServiPpal(con, criterios);	
	}
	
	public HashMap cargarServiIncluServiPpal(Connection con, int criterios, int tarifario,UsuarioBasico usuario) {
		
		HashMap respuesta = Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().cargarServiIncluServiPpal(con, criterios, tarifario);
		int grupoServicio = 0;
		
		for(int i = 0; i < Utilidades.convertirAEntero(respuesta.get("numRegistros").toString()); i++)
		{
			grupoServicio = Utilidades.obtenerGrupoServicio(con, Utilidades.convertirAEntero(respuesta.get("codservinclu_"+i).toString()));
			respuesta.put("centrocostoservinclu_"+i, UtilidadesFacturacion.consultarCentrosCostoGrupoServicio(con,grupoServicio, usuario.getCodigoCentroAtencion(), true,false));			
		}
		
		return respuesta;
		
	}

	/**  * Método implementado para consultar los datos (articulos, servicios incluidos) de un servicio Principal * */
	/**	 *	 */
	public HashMap cargarArtIncluServiInclu(Connection con, int criterios) {
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().cargarArtIncluServiInclu(con, criterios);
	}

	/**
	 * Método que genera la inserción de un nuevo Servicio Principal Incluido 
	 * especificando el código, el servicio y si esta activo o no
	 * @param (con,forma)
	 * @return
	 */
	public boolean insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, String usuario, int institucion)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigoServicioPrincipal", forma.getCodigoServicio());
		criterios.put("activo", forma.getActivoServicioPrincipal());
		criterios.put("institucion", institucion);
		criterios.put("usuario", usuario);
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(con, criterios);
	}

	/**
	 * Método que inserta los servicios incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoServicioIncluido
	 * @param centroCosto
	 * @param cantidad
	 * @return
	 */
	public boolean insertarServiciosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoServicioIncluido, String centroCosto, String cantidad)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().insertarServiciosIncluidosEnServicioPrincipal(con, codigoServicioPrincipal, codigoServicioIncluido, centroCosto, cantidad);
	}
	
	/**
	 * Método que inserta los articulos incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoArticuloIncluido
	 * @param cantidad
	 * @param farmacia
	 * @return
	 */
	public boolean insertarArticulosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoArticuloIncluido, String farmacia, String cantidad)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().insertarArticulosIncluidosEnServicioPrincipal(con, codigoServicioPrincipal, codigoArticuloIncluido, farmacia, cantidad);
	}
	
	/**
	 * Método que actualiza los servicios incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal 
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoServicioIncluido
	 * @param centroCosto
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarServiciosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int centroCosto, int cantidad)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().actualizarServiciosIncluidosEnServicioPrincipal(con, codigoServicioIncluido, centroCosto, cantidad);
	}
	
	/**
	 * Método que actualiza los articulos incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal 
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoServicioIncluido
	 * @param centroCosto
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarArticulosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int farmacia, int cantidad)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().actualizarArticulosIncluidosEnServicioPrincipal(con, codigoServicioIncluido, farmacia, cantidad);
	}
	
	/**
	 * Método que actualiza la información del servicio 
	 * principal según lo parametrizado por el usuario
	 * @param con
	 * @param usuario 
	 * @param activoServicioPrincipal
	 * @return
	 */
	public boolean actualizarServicioPrincipal(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, UsuarioBasico usuario)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigoSistemaServiPpal", forma.getHasMapaServiArtiIncluOtroProc("codigosys_"+forma.getPosicion())+"");
		criterios.put("activoServicioPrincipal", forma.getHasMapaServiArtiIncluOtroProc("activo_"+forma.getPosicion())+"");
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		criterios.put("usuario", usuario.getLoginUsuario());
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().actualizarServicioPrincipal(con, criterios);
	}
	
	/**
	 * Método para eliminar un servicio incluido
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public boolean eliminarServicioIncluido(Connection con, int codigoServicioIncluido)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().eliminarServicioIncluido(con, codigoServicioIncluido);
	}
	
	/**
	 * Método para eliminar un articulo incluido
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public boolean eliminarArticuloIncluido(Connection con, int codigoServicioIncluido)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().eliminarArticuloIncluido(con, codigoServicioIncluido);
	}
	
	/**
	 * @param con
	 * @param codigoServicio
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int obtenerCodigoPkServicioIncluye(Connection con, int codigoServicio, boolean activo, int institucion)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().obtenerCodigoPkServicioIncluye(con, codigoServicio, activo, institucion);
	}

/**
	public static HashMap<Object, Object> cargarFarmaciasArtPpal(Connection con, int codigoServicio, boolean activo, int institucion)
	{
		//obtener el centro de cosot
//		Utilidades.obtenerCentrosCosto(institucion, ConstantesBD.codigoTipoAreaSubalmacen, true, centroAtencion, filtrar_externos);
		HashMap<Object, Object> mapaRetorna= new HashMap<Object, Object>();
		return mapaRetorna;
	}
*/
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarServiciosIncluidosServicioPrincipal(Connection con, int codigoServicio, boolean activo, int institucion)
	{
		HashMap<Object, Object> mapaRetorna= new HashMap<Object, Object>();
		HashMap<Object, Object> mapaConsulta= new HashMap<Object, Object>(); 
		int codigoPk= obtenerCodigoPkServicioIncluye(con, codigoServicio, activo, institucion);
		
		int tarifario= ConstantesBD.codigoTarifarioCups;
		if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)))
		{
			tarifario= Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion));
			if(tarifario<0)
				tarifario= ConstantesBD.codigoTarifarioCups;
		}
		
		mapaConsulta= Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().cargarServiIncluServiPpal(con, codigoPk, tarifario);
		
		Utilidades.imprimirMapa(mapaConsulta);
		
		for(int w=0; w<Utilidades.convertirAEntero(mapaConsulta.get("numRegistros")+""); w++)
		{
			mapaRetorna.put("cod_pk_serv_ppal_"+w+"_"+codigoServicio, codigoPk);
			mapaRetorna.put("cod_serv_ppal_"+w+"_"+codigoServicio, codigoServicio);
			mapaRetorna.put("cod_pk_serv_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("codconservinclu_"+w));
			mapaRetorna.put("cod_serv_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("codservinclu_"+w));
			mapaRetorna.put("desc_serv_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("descripservinclu_"+w));
			mapaRetorna.put("cod_centro_costo_ejecuta_"+w+"_"+codigoServicio, mapaConsulta.get("centrocostoseleccionado_"+w));
			mapaRetorna.put("desc_centro_costo_ejecuta_"+w+"_"+codigoServicio, Utilidades.obtenerNombreCentroCosto(Integer.parseInt(mapaConsulta.get("centrocostoseleccionado_"+w)+""), institucion));
			mapaRetorna.put("cantidad_maxima_valida_"+w+"_"+codigoServicio, mapaConsulta.get("cantservinclu_"+w));
			mapaRetorna.put("cantidad_"+w+"_"+codigoServicio, mapaConsulta.get("cantservinclu_"+w));
			mapaRetorna.put("confirmar_"+w+"_"+codigoServicio, ConstantesBD.acronimoSi);
			mapaRetorna.put("cod_tarifario_serv_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("codtarifarioservincluido_"+w));
			mapaRetorna.put("es_pos_serv_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("esposervinclu_"+w));
			
			mapaRetorna.put("ARTICULOS_INCLUIDOS_SERVICIO_INCLUIDO_"+w+"_"+codigoServicio, cargarArticulosIncluidosServicioIncluido(con, Integer.parseInt(mapaConsulta.get("codconservinclu_"+w)+""), Integer.parseInt(mapaConsulta.get("codservinclu_"+w)+"")));
		}
		mapaRetorna.put("abrir_seccion_serv_incluidos_"+codigoServicio, ConstantesBD.acronimoSi);
		mapaRetorna.put("numRegistros_"+codigoServicio, mapaConsulta.get("numRegistros"));
		return mapaRetorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarArticulosIncluidosServicioPrincipal(Connection con, int codigoServicio, boolean activo, int institucion)
	{
		HashMap<Object, Object> mapaRetorna= new HashMap<Object, Object>();
		HashMap<Object, Object> mapaConsulta= new HashMap<Object, Object>(); 
		int codigoPk= obtenerCodigoPkServicioIncluye(con, codigoServicio, activo, institucion);
		
		mapaConsulta= Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().cargarArtIncluServiPpal(con, codigoPk);
		
		for(int w=0; w<Utilidades.convertirAEntero(mapaConsulta.get("numRegistros")+""); w++)
		{
			mapaRetorna.put("cod_pk_serv_ppal_"+w+"_"+codigoServicio, codigoPk);
			mapaRetorna.put("cod_serv_ppal_"+w+"_"+codigoServicio, codigoServicio);
			mapaRetorna.put("cod_pk_art_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("codconartinclu_"+w));
			mapaRetorna.put("cod_art_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("codartinclu_"+w));
			mapaRetorna.put("desc_art_incluido_"+w+"_"+codigoServicio, mapaConsulta.get("descripcompletaartinclu_"+w));
			mapaRetorna.put("cod_farmacia_"+w+"_"+codigoServicio, mapaConsulta.get("farmaciaartinclu_"+w));
			mapaRetorna.put("desc_farmacia_"+w+"_"+codigoServicio, Utilidades.obtenerNombreCentroCosto(Integer.parseInt(mapaConsulta.get("farmaciaartinclu_"+w)+""), institucion));
			mapaRetorna.put("cantidad_maxima_valida_"+w+"_"+codigoServicio, mapaConsulta.get("cantartinclu_"+w));
			mapaRetorna.put("cantidad_"+w+"_"+codigoServicio, mapaConsulta.get("cantartinclu_"+w));
			mapaRetorna.put("codigo_interfaz_"+w+"_"+codigoServicio, mapaConsulta.get("codigointerfazartinclu_"+w));
			mapaRetorna.put("pos_art_inclu_"+w+"_"+codigoServicio, mapaConsulta.get("posartinclu_"+w));
		}
		mapaRetorna.put("abrir_seccion_art_incluidos_"+codigoServicio, ConstantesBD.acronimoSi);
		mapaRetorna.put("numRegistros_"+codigoServicio, mapaConsulta.get("numRegistros"));
		return mapaRetorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkServicioIncluido
	 * @param codigoServicio
	 * @return
	 */
	private static Object cargarArticulosIncluidosServicioIncluido(	Connection con, int codigoPkServicioIncluido, int codigoServicioIncluido) 
	{
		HashMap<Object, Object> mapaRetorna= new HashMap<Object, Object>();
		HashMap<Object, Object> mapaConsulta= Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().cargarArtIncluServiInclu(con, codigoPkServicioIncluido);
		for(int w=0; w<Utilidades.convertirAEntero(mapaConsulta.get("numRegistros")+""); w++)
		{
			mapaRetorna.put("cod_art_incluido_"+w, mapaConsulta.get("codartincluserv_"+w));
			mapaRetorna.put("cod_farmacia_"+w, mapaConsulta.get("farmaciaartincluserv_"+w));
			mapaRetorna.put("cantidad_"+w, mapaConsulta.get("cantartincluserv_"+w));
			mapaRetorna.put("codigo_interfaz_"+w, mapaConsulta.get("codintartincluserv_"+w));
		}
		mapaRetorna.put("numRegistros_"+codigoServicioIncluido, mapaConsulta.get("numRegistros"));
		return mapaRetorna;
	}
	
	/**
	 * Carga el dto con los articulos incluidos dentro de una solicitud
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static ArrayList<DtoArticuloIncluidoSolProc> cargarArticulosIncluidosSolicitudDto(String numeroSolicitud)
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoArticuloIncluidoSolProc> array= RespuestaProcedimientos.cargarArticulosIncluidosSolicitudDto(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return array;
	}
	
	/**
	 * Carga el dto con los servicios incluidos dentro de una solicitud
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static ArrayList<DtoServicioIncluidoSolProc> cargarServiciosIncluidosSolicitudDto(String numeroSolicitud, int institucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoServicioIncluidoSolProc> array= RespuestaProcedimientos.cargarServiciosIncluidosSolicitudDto(con, numeroSolicitud, institucion);
		UtilidadBD.closeConnection(con);
		return array;
	}
	
	/**
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarServiciosIncluidosSolicitudProcedimientos(Connection con, DtoServicioIncluidoSolProc dto)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().insertarServiciosIncluidosSolicitudProcedimientos(con, dto);
	}
	
	/**
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarArticulosIncluidosSolicitudProcedimientos(Connection con, DtoArticuloIncluidoSolProc dto)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().insertarArticulosIncluidosSolicitudProcedimientos(con, dto);
	}
	
	/**
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param cantidad
	 * @param usuario
	 * @return
	 */
	public static boolean modificarCantidadArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int cantidad, String usuario)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().modificarCantidadArticulosIncluidosSolicitudProcedimientos(con, solicitudPpal, articulo, cantidad, usuario);
	}

	
	
	/**	 * @param con	 * @param solicitudPpal	 * @param articulo	 * @param solicitudIncluida	 * @param usuario
	 * @return	 */
	public static boolean modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int solicitudIncluida, String usuario)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(con, solicitudPpal, articulo, solicitudIncluida, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean solicitudIncluyeServiciosArticulos(int numeroSolicitud)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean retorna= Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().solicitudIncluyeServiciosArticulos(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return retorna;
	}


//insertar arti en servinclu
	/**	 * Método que inserta los articulos incluidos dentro
	 * de un servicio incluido
	 * @param con	 * @param codigoServicioPrincipal
	 * @param codigoArticuloIncluido	 * @param cantidad
	 * @param farmacia
	 * @return	 */
	public boolean insertarArticulosIncluidosEnServicioIncluido(Connection con, String codigoServicioIncluido, String codigoArticuloIncluido, String farmacia, String cantidad)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().insertarArticulosIncluidosEnServicioIncluido(con, codigoServicioIncluido, codigoArticuloIncluido, farmacia, cantidad);
	}

	/**
	 * Método que actualiza los articulos incluidos dentro
	 * de un servicio incluido
	 * @param con
	 * @param codigoServicioIncluido
	 * @param centroCosto
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarArticulosIncluidosEnServicioIncluido(Connection con, int codigoServicioIncluido, int farmacia, int cantidad)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().actualizarArticulosIncluidosEnServicioIncluido(con, codigoServicioIncluido, farmacia, cantidad);
	}

	/**
	 * Método para eliminar un articulo incluido en un servinclu
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public boolean eliminarArticuloIncluidoServinclu(Connection con, int codigoServicioIncluido)
	{
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().eliminarArticuloIncluidoServinclu(con, codigoServicioIncluido);
	}

	
	/** Metodo para consultar los codigos de todos los servinclus utilizados o de un ppal especifico
	 * @param con, especifico, codpertenece
	 * @return	 */
	public HashMap consultarServIncluUsados(Connection con, int especifico, int codpertenece) {
		return Servicios_ArticulosIncluidosEnOtrosProcedimientosDao().consultarServIncluUsados(con, especifico, codpertenece);
	}
	
	
	
	
	
}