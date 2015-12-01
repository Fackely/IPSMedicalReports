/**
 * 
 */
package com.princetonsa.dao.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.cargos.DtoDetalleCargoArticuloConsumo;

import util.InfoDatosString;
import util.facturacion.InfoTarifa;
import util.facturacion.InfoTarifaVigente;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author wilson
 *
 */
public interface CargosDao 
{
	/**
	 * metodo para insertar el detalle de los cargos
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public double insertarDetalleCargos(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario ) throws BDException;
	
	/**
	 * metodo que obtiene la tarifa base x servicio
	 * @param con
	 * @param codigoTipoTarifario
	 * @param codigoServicio
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public InfoTarifaVigente obtenerTarifaBaseServicio(Connection con, int codigoTipoTarifario, int codigoServicio, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException;
	
	/**
	 * METODO QUE OBTIENE EL VALOR DE LA EXCEPCION DE TARIFAS X SERVICIO, 
	 * DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de eellos contiene el valor
	 * @param con (REQUERIDO)
	 * @param codigoViaIngreso (REQUERIDO)
	 * @param tipoPaciente
	 * @param codigoTipoComplejidad= -1= NO DEFINIDO
	 * @param contrato (REQUERIDO)
	 * @param codigoServicio (REQUERIDO)
	 * @param fechaCalculoVigencia 
	 * @return DEVUELVE UN OBJETO <-InfoTarifa-> 
	 */
	public InfoTarifa obtenerExcepcionesTarifasServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia, int centroAtencion) throws BDException;
	
	/**
	 * METODO QUE OBTIENE EL RECARGO DEL SERVICIO, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param contrato
	 * @param codigoTipoSolicitud
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoTarifa obtenerRecargoServicio (Connection con, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoContrato, int codigoTipoSolicitud, int numeroSolicitud) throws BDException;
	
	/**
	 * METODO QUE OBTIENE EL DESCUENTO COMERCIAL X CONVENIO SERVICIO, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public InfoTarifa obtenerDescuentoComercialXConvenioServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException;
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public InfoDatosString obtenerInclusionExclusionXConvenioServicio(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException;
	
	/**
	 * metodo para obtener la tarifa base de un articulo 
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public double obtenerTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException;
	
	/**
	 * metodo para obtener el TIPO de tarifa base de un articulo
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public String obtenerTipoTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException;
	
	/**
	 * METODO QUE OBTIENE EL VALOR DE LA EXCEPCION DE TARIFAS X Articulo, 
	 * DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de eellos contiene el valor
	 * @param con (REQUERIDO)
	 * @param codigoViaIngreso (REQUERIDO)
	 * @param codigoTipoComplejidad= -1= NO DEFINIDO
	 * @param contrato (REQUERIDO)
	 * @param codigoArticulo (REQUERIDO)
	 * @param fechaCalculoVigencia 
	 * @return DEVUELVE UN OBJETO <-InfoTarifa-> 
	 */
	public InfoTarifa obtenerExcepcionesTarifasArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoArticulo, int codigoInstitucion, int codigoEsquemaTarifario, String fechaCalculoVigencia, int centroAtencion ) throws BDException;
	
	/**
	 * METODO QUE OBTIENE EL DESCUENTO COMERCIAL X CONVENIO ARTICULO, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public InfoTarifa obtenerDescuentoComercialXConvenioArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException;
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public InfoDatosString obtenerInclusionExclusionXConvenioArticulo(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException;
	
	/**
	 * metodo que elimina el detalle cargo x codigo de detalle, debe tener encuenta que primero debe eliminar los
	 * errores_cargos si existen para que no saque errores de dependencias @see: eliminarErroresCargoXCodigoDetalleCargo
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public boolean eliminarDetalleCargoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws BDException;
	
	/**
	 * metodo que realiza una busqueda de los atributos codigo_detalle_cargo 
	 * y realiza filtros (si existen de) key="subCuenta,solicitud,servicio,articulo" 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return 
	 */
	public Vector busquedaCodigosDetalleCargos(Connection con, HashMap criteriosBusquedaMap);
	
	
	/**
	 * Método que se encarga de búscar el código del servicio para una 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @param esPortatil
	 * @return
	 */
	public int busquedaCodigoServicioXSolicitud (Connection con, int numeroSolicitud, int codigoTipoSolicitud, String esPortatil) throws BDException;
	
	/**
	 * Método que se encarga de búscar el código del servicio para una 
	 * evolución
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 * @throws SQLException
	 */
	public int busquedaServicioEnEvolucion (Connection con, int codigoEvolucion) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codigoCargo
	 * @param error
	 * @return
	 */
	public double insertarErrorDetalleCargo(Connection con, double codigoDetalleCargo, String error) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicioArticulo
	 * @param esServicio
	 * @return
	 */
	public int obtenerNumeroResponsablesSolicitudCargo(Connection con, int numeroSolicitud, int codigoServicioArticulo, boolean esServicio);
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public ArrayList<DtoDetalleCargo> cargarDetalleCargos(Connection con, DtoDetalleCargo criteriosBusquedaDtoDetalleCargo) throws BDException;
	
	/**
	 * actualizar el detalle del cargo
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public boolean updateDetalleCargo(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario) throws BDException;
	
	/**
	 * metodo para actauliza las cantidades de la solicitud de subcuenta y el cargo
	 * @param con
	 * @param cantidad
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param esServicio
	 * @return
	 */
	public boolean updateCantidadesCargo(Connection con, int cantidad, int numeroSolicitud, double subCuenta, int codigoArticuloServicio, boolean esServicio);
	
	/**
	 * metodo que obtiene los cargos en estado pendiente - cargado para poderles posteriormente recalcular los cargos  
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public Vector obtenerCodigosArticulosXSolicitudACargar (Connection con, int numeroSolicitud);

	/**
	 * existe cargos pendientes
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existenCargosPendientesXSolicitud(Connection con, String numeroSolicitud);


	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param estado
	 * @return
	 */
	public boolean modificarEstadoCargo(Connection con, double codigoDetalleCargo, int estado) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codigoDetalleCargoPadre
	 * @return
	 */
	public boolean modificarCargoPadre(Connection con, double codigoDetalleCargo, double codigoDetalleCargoPadre);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param facturado
	 * @param portatil ('S', 'N', '')
	 * @return
	 */
	public double obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(Connection con, int numeroSolicitud, double subCuenta, String facturado, String paquetizado, String portatil) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param codigoArticulo
	 * @param facturado
	 * @return
	 */
	public double obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(Connection con, int numeroSolicitud, double subCuenta, int codigoArticulo, String facturado, String paquetizado) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public Vector obtenerErroresDetalleCargo(Connection con, double codigoDetalleCargo);

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param codigoSubcuenta
	 * @return
	 */
	public int obtenerTotalAdminFarmaciaXResponsable(Connection con, int codigoArticulo, int numeroSolicitud, double codigoSubcuenta) throws BDException;

	/**
	 * 
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param esServicio
	 * @return
	 */
	public boolean eliminarDetalleCargoXSolicitudServArt(Connection con, int numSolicitud, String codServArt, boolean esServicio);
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numSolicitud
	 * @param codServArt
	 * @param codigoServicioAsocio 
	 * @param estadoFacturado
	 * @param esServicio
	 * @param numeroFactura 
	 * @return
	 */
	public boolean actualizarEstadoFacturadoDetalleCargo(Connection con, double subCuenta, int numSolicitud, int codServArt, int codigoServicioAsocio, String estadoFacturado, boolean esServicio, int numeroFactura);
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean actualizarEstadoFacturadoSubCuenta(Connection con, double subCuenta, String estado);

	
	/**
	 * metodo para obtener los codigos det cargo de los componenetes de un paquete
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public Vector obtenerCargosComponentesPaquete(Connection con, double codigoDetalleCargo) throws BDException;
	
	/**
	 * metodo para actualizar el cargo padre de los componentes de un paquete
	 * @param con
	 * @param cargoPadre
	 * @param codigosDetalleCargoVector
	 * @return
	 */
	public boolean actualizarCargoPadreComponentesPaquetes(Connection con, double cargoPadre, Vector codigosDetalleCargoVector) throws BDException;
	
	/**
	 * Solo se puede borrar el cargo del portatil cuando esta en estado solicitada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean borrarCargoPortatil(Connection con, int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerFechaCalculoCargo(Connection con, int numeroSolicitud) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public int conveniosPlanEspecial(Connection con, int codigoIngreso);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public boolean actualizarEstadoFacturadoSubCuentasTotalCero(Connection con,	int codigoIngreso);

	/**
	 * Acatualizacion del campo nro_autorizacion de la tabala det_cargos
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public boolean actualizarNumeroAutorizacion(Connection con, HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizacionesEstancia
	 * @return
	 */
	public boolean actualizarCargoDetAutorizacionesEstancia(Connection con,	double codigoDetalleCargo,	ArrayList<Integer> codDetalleAutorizacionesEstancia) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizaciones
	 * @return
	 */
	public boolean actualizarCargoDetAutorizaciones(Connection con,	double codigoDetalleCargo, ArrayList<Integer> codDetalleAutorizaciones) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public ArrayList<Integer> cargarDetAutorizacionesEstanciaXCargo(Connection con, double codigoDetalleCargo) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public ArrayList<Integer> cargarDetAutorizacionesXCargo(Connection con, double codigoDetalleCargo) throws BDException;
	
	/**
	 * Insertar cargos articulos consumo
	 * @param con
	 * @param dto
	 * @param loginUsuario
	 * @return
	 */
	public double insertarDetalleCargosArtConsumos(Connection con, DtoDetalleCargoArticuloConsumo dto, String loginUsuario ) throws BDException;
	
	/**
	 * metodo que elimina el detalle cargo art consumo x codigo de detalle, 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public boolean eliminarDetalleCargoArticuloConsumoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws BDException;
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public ArrayList<DtoDetalleCargoArticuloConsumo> cargarDetalleCargosArticuloConsumo(Connection con, DtoDetalleCargoArticuloConsumo criteriosBusquedaDtoDetalleCargoArtConsumo);
	
	/**
	 * 
	 * 
	 */
	public  boolean  modificarEstadosCargosCitas(int numeroSolicitud, int estado, String facturado , String eliminado ,Connection con);
	
	/**
	 * 
	 * @param con 
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCentroAtencionCargoSolicitud(Connection con, int numeroSolicitud) throws BDException;
	
	/**
	 * 
	 * @param centroCosto
	 * @return
	 */
	public int obtenerCentroAtencionCentroCostoSolicitadoCargo(int centroCosto);

	/**
	 * 
	 * Metodo para obtener los cargos a partir del numero solicitudes
	 * @param con
	 * @param listaSolicitudes
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<BigDecimal> obtenerCodigosPkCargosDadoSolicitudes(Connection con, ArrayList<BigDecimal> listaSolicitudes);
	
	/**
	 * Método obtiene cantidad de Inclusiones de un articulo por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoArticulo
	 * @return
	 */
	public int consultarCantidadInclusionesExclusionesOrdenArticulo(Connection con, int codigoSubcuenta, int codigoArticulo);
	
	/**
	 * Método obtiene cantidad de Inclusiones de un servicio por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoServicio
	 * @return
	 */
	public int consultarCantidadInclusionesExclusionesOrdenServicio(Connection con, int codigoSubcuenta, int codigoServicio);
}