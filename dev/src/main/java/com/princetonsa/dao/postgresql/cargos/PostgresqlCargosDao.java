package com.princetonsa.dao.postgresql.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;
import util.ValoresPorDefecto;
import util.facturacion.InfoTarifa;
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.dao.cargos.CargosDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosDao;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.cargos.DtoDetalleCargoArticuloConsumo;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlCargosDao implements CargosDao 
{
	/**
	 * metodo para insertar el detalle de los cargos
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public double insertarDetalleCargos(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario ) throws BDException
	{
		return SqlBaseCargosDao.insertarDetalleCargos(con, detalleCargo, loginUsuario);
	}
	
	/**
	 * metodo que obtiene la tarifa base x servicio
	 * @param con
	 * @param codigoTipoTarifario
	 * @param codigoServicio
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public InfoTarifaVigente obtenerTarifaBaseServicio(Connection con, int codigoTipoTarifario, int codigoServicio, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException
	{
		return SqlBaseCargosDao.obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicio, codigoEsquemaTarifario, fechaCalculoVigencia);
	}
	
	/**
	 * METODO QUE OBTIENE EL VALOR DE LA EXCEPCION DE TARIFAS X SERVICIO, 
	 * DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de eellos contiene el valor
	 * @param con (REQUERIDO)
	 * @param codigoViaIngreso (REQUERIDO)
	 * @param codigoTipoComplejidad= -1= NO DEFINIDO
	 * @param contrato (REQUERIDO)
	 * @param codigoServicio (REQUERIDO)
	 * @return DEVUELVE UN OBJETO <-InfoTarifa-> 
	 */
	public InfoTarifa obtenerExcepcionesTarifasServicio(Connection con, int codigoViaIngreso,String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia, int centroAtencion ) throws BDException
	{
		return SqlBaseCargosDao.obtenerExcepcionesTarifasServicio(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoServicio, codigoInstitucion,fechaCalculoVigencia, centroAtencion);
	}
	
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
	public InfoTarifa obtenerRecargoServicio (Connection con, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoContrato, int codigoTipoSolicitud, int numeroSolicitud) throws BDException
	{
		return SqlBaseCargosDao.obtenerRecargoServicio(con, codigoViaIngreso, tipoPaciente, codigoServicio, codigoContrato, codigoTipoSolicitud, numeroSolicitud);
	}
	
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
	 * @return
	 */
	public InfoTarifa obtenerDescuentoComercialXConvenioServicio(Connection con, int codigoViaIngreso,String tipoPaciente, int codigoContrato, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia ) throws BDException
	{
		return SqlBaseCargosDao.obtenerDescuentoComercialXConvenioServicio(con, codigoViaIngreso, tipoPaciente, codigoContrato, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosString obtenerInclusionExclusionXConvenioServicio(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia ) throws BDException
	{
		return SqlBaseCargosDao.obtenerInclusionExclusionXConvenioServicio(con, codigoContrato, codigoCentroCostoSolicita, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener la tarifa base de un articulo 
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public double obtenerTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException
	{
		return SqlBaseCargosDao.obtenerTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener el TIPO de tarifa base de un articulo
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public String obtenerTipoTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException
	{
		return SqlBaseCargosDao.obtenerTipoTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
	}
	
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
	 * @return DEVUELVE UN OBJETO <-InfoTarifa-> 
	 */
	public InfoTarifa obtenerExcepcionesTarifasArticulo(Connection con, int codigoViaIngreso,String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoArticulo, int codigoInstitucion, int codigoEsquemaTarifario,String fechaCalculoVigencia, int centroAtencion ) throws BDException
	{
		return SqlBaseCargosDao.obtenerExcepcionesTarifasArticulo(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoArticulo, codigoInstitucion, codigoEsquemaTarifario,fechaCalculoVigencia, centroAtencion);
	}
	
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
	 * @return
	 */
	public InfoTarifa obtenerDescuentoComercialXConvenioArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoArticulo, int codigoInstitucion,String fechaCalculoVigencia ) throws BDException
	{
		return SqlBaseCargosDao.obtenerDescuentoComercialXConvenioArticulo(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosString obtenerInclusionExclusionXConvenioArticulo(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoArticulo, int codigoInstitucion,String fechaCalculoVigencia ) throws BDException
	{
		return SqlBaseCargosDao.obtenerInclusionExclusionXConvenioArticulo(con, codigoContrato, codigoCentroCostoSolicita, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo que elimina el detalle cargo x codigo de detalle, debe tener encuenta que primero debe eliminar los
	 * errores_cargos si existen para que no saque errores de dependencias @see: eliminarErroresCargoXCodigoDetalleCargo
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public boolean eliminarDetalleCargoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws BDException
	{
		return SqlBaseCargosDao.eliminarDetalleCargoXCodigoDetalle(con, codigoDetalleCargo);
	}
	
	/**
	 * metodo que realiza una busqueda de los atributos codigo_detalle_cargo,  
	 * y realiza filtros (si existen de) key="subCuenta,solicitud,servicio,articulo" 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public Vector busquedaCodigosDetalleCargos(Connection con, HashMap criteriosBusquedaMap)
	{
		return SqlBaseCargosDao.busquedaCodigosDetalleCargos(con, criteriosBusquedaMap);
	}
	
	/**
	 * Método que se encarga de búscar el código del servicio para un numero de solicitud y tipo
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @param esPortatil
	 * @return
	 */
	public int busquedaCodigoServicioXSolicitud (Connection con, int numeroSolicitud, int codigoTipoSolicitud, String esPortatil) throws BDException
	{
		return SqlBaseCargosDao.busquedaCodigoServicioXSolicitud(con, numeroSolicitud, codigoTipoSolicitud, esPortatil);
	}
	
	/**
	 * Método que se encarga de búscar el código del servicio para una 
	 * evolución
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 * @throws SQLException
	 */
	public int busquedaServicioEnEvolucion (Connection con, int codigoEvolucion) throws BDException
	{
		return SqlBaseCargosDao.busquedaServicioEnEvolucion(con, codigoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param error
	 * @return
	 */
	public double insertarErrorDetalleCargo(Connection con, double codigoDetalleCargo, String error) throws BDException
	{
		return SqlBaseCargosDao.insertarErrorDetalleCargo(con, codigoDetalleCargo, error);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicioArticulo
	 * @param esServicio
	 * @return
	 */
	public int obtenerNumeroResponsablesSolicitudCargo(Connection con, int numeroSolicitud, int codigoServicioArticulo, boolean esServicio)
	{
		return SqlBaseCargosDao.obtenerNumeroResponsablesSolicitudCargo(con, numeroSolicitud, codigoServicioArticulo, esServicio, "");
	}
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public ArrayList<DtoDetalleCargo> cargarDetalleCargos(Connection con, DtoDetalleCargo criteriosBusquedaDtoDetalleCargo) throws BDException
	{
		return SqlBaseCargosDao.cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
	}
	
	/**
	 * actualizar el detalle del cargo
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public boolean updateDetalleCargo(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario) throws BDException
	{
		return SqlBaseCargosDao.updateDetalleCargo(con, detalleCargo, loginUsuario);
	}
	
	/**
	 * metodo para actauliza las cantidades de la solicitud de subcuenta y el cargo
	 * @param con
	 * @param cantidad
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param esServicio
	 * @return
	 */
	public boolean updateCantidadesCargo(Connection con, int cantidad, int numeroSolicitud, double subCuenta, int codigoServicioArticulo, boolean esServicio)
	{
		return SqlBaseCargosDao.updateCantidadesCargo(con,cantidad,numeroSolicitud,subCuenta, codigoServicioArticulo, esServicio);
	}

	/**
	 * metodo que obtiene los cargos en estado pendiente - cargado para poderles posteriormente recalcular los cargos  
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public Vector obtenerCodigosArticulosXSolicitudACargar (Connection con, int numeroSolicitud)
	{
		return SqlBaseCargosDao.obtenerCodigosArticulosXSolicitudACargar(con, numeroSolicitud);
	}
	
	/**
	 * existe cargos pendientes
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existenCargosPendientesXSolicitud(Connection con, String numeroSolicitud)
	{
		return SqlBaseCargosDao.existenCargosPendientesXSolicitud(con, numeroSolicitud);
	}
	
	

	/** 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param estado
	 * @return
	 */
	public boolean modificarEstadoCargo(Connection con, double codigoDetalleCargo, int estado) throws BDException
	{
		return SqlBaseCargosDao.modificarEstadoCargo(con, codigoDetalleCargo, estado);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codigoDetalleCargoPadre
	 * @return
	 */
	public boolean modificarCargoPadre(Connection con, double codigoDetalleCargo, double codigoDetalleCargoPadre)
	{
		return SqlBaseCargosDao.modificarCargoPadre(con, codigoDetalleCargo, codigoDetalleCargoPadre);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param facturado
	 * @param portatil ('S', 'N', '')
	 * @return
	 */
	public double obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(Connection con, int numeroSolicitud, double subCuenta, String facturado, String paquetizado, String portatil) throws BDException
	{
		return SqlBaseCargosDao.obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(con, numeroSolicitud, subCuenta, facturado, paquetizado, portatil);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param codigoArticulo
	 * @param facturado
	 * @param paquetizado
	 * @return
	 */
	public double obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(Connection con, int numeroSolicitud, double subCuenta, int codigoArticulo, String facturado, String paquetizado) throws BDException
	{
		return SqlBaseCargosDao.obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(con, numeroSolicitud, subCuenta, codigoArticulo, facturado, paquetizado);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public Vector obtenerErroresDetalleCargo(Connection con, double codigoDetalleCargo)
	{
		return SqlBaseCargosDao.obtenerErroresDetalleCargo(con, codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @return
	 */
	public int obtenerTotalAdminFarmaciaXResponsable(Connection con, int codigoArticulo, int numeroSolicitud, double codigoSubcuenta) throws BDException
	{
		String consulta= "SELECT getTotalAdminResponsable("+codigoArticulo+", "+numeroSolicitud+", "+(long)codigoSubcuenta+", "+ValoresPorDefecto.getValorFalseParaConsultas()+") as cant ";
		return SqlBaseCargosDao.obtenerTotalAdminFarmaciaXResponsable(con, codigoArticulo, numeroSolicitud, codigoSubcuenta, consulta);
	}

	/**
	 * 
	 */
	public boolean eliminarDetalleCargoXSolicitudServArt(Connection con, int numSolicitud, String codServArt, boolean esServicio) 
	{
		return SqlBaseCargosDao.eliminarDetalleCargoXSolicitudServArt(con, numSolicitud, codServArt, esServicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numSolicitud
	 * @param codServArt
	 * @param estadoFacturado
	 * @param esServicio
	 * @return
	 */
	public boolean actualizarEstadoFacturadoDetalleCargo(Connection con, double subCuenta, int numSolicitud, int codServArt, int codigoServicioAsocio, String estadoFacturado, boolean esServicio,int numeroFactura)
	{
		return SqlBaseCargosDao.actualizarEstadoFacturadoDetalleCargo(con, subCuenta, numSolicitud, codServArt,codigoServicioAsocio, estadoFacturado, esServicio,numeroFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean actualizarEstadoFacturadoSubCuenta(Connection con, double subCuenta, String estado)
	{
		return SqlBaseCargosDao.actualizarEstadoFacturadoSubCuenta(con, subCuenta, estado);
	}
	
	
	/**
	 * metodo para obtener los codigos det cargo de los componenetes de un paquete
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public Vector obtenerCargosComponentesPaquete(Connection con, double codigoDetalleCargo) throws BDException
	{
		return SqlBaseCargosDao.obtenerCargosComponentesPaquete(con, codigoDetalleCargo);
	}
	
	/**
	 * metodo para actualizar el cargo padre de los componentes de un paquete
	 * @param con
	 * @param cargoPadre
	 * @param codigosDetalleCargoVector
	 * @return
	 */
	public boolean actualizarCargoPadreComponentesPaquetes(Connection con, double cargoPadre, Vector codigosDetalleCargoVector) throws BDException
	{
		return SqlBaseCargosDao.actualizarCargoPadreComponentesPaquetes(con, cargoPadre, codigosDetalleCargoVector);
	}
	
	/**
	 * Solo se puede borrar el cargo del portatil cuando esta en estado solicitada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean borrarCargoPortatil(Connection con, int numeroSolicitud)
	{
		return SqlBaseCargosDao.borrarCargoPortatil(con, numeroSolicitud);
	}
	

	/**
	 * 
	 */
	public String obtenerFechaCalculoCargo(Connection con, int numeroSolicitud) throws BDException
	{
		return SqlBaseCargosDao.obtenerFechaCalculoCargo(con, numeroSolicitud);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public int conveniosPlanEspecial(Connection con, int codigoIngreso) 
	{
		return SqlBaseCargosDao.conveniosPlanEspecial(con, codigoIngreso);
	}
	
	/**
	 * 
	 */
	public boolean actualizarEstadoFacturadoSubCuentasTotalCero(Connection con,int codigoIngreso)
	{
		return SqlBaseCargosDao.actualizarEstadoFacturadoSubCuentasTotalCero(con, codigoIngreso);
	}
	
	/**
	 * Actualizacion del campo nro_autorizacion de la tabla det_cargos
	 */
	public boolean actualizarNumeroAutorizacion(Connection con, HashMap parametros)
	{
		return SqlBaseCargosDao.actualizarNumeroAutorizacion(con, parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizacionesEstancia
	 * @return
	 */
	public boolean actualizarCargoDetAutorizacionesEstancia(Connection con,	double codigoDetalleCargo,	ArrayList<Integer> codDetalleAutorizacionesEstancia) throws BDException
	{
		return SqlBaseCargosDao.actualizarCargoDetAutorizacionesEstancia(con, codigoDetalleCargo,	codDetalleAutorizacionesEstancia);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizaciones
	 * @return
	 */
	public boolean actualizarCargoDetAutorizaciones(Connection con,	double codigoDetalleCargo, ArrayList<Integer> codDetalleAutorizaciones) throws BDException
	{
		return SqlBaseCargosDao.actualizarCargoDetAutorizaciones(con, codigoDetalleCargo, codDetalleAutorizaciones);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public ArrayList<Integer> cargarDetAutorizacionesEstanciaXCargo(Connection con, double codigoDetalleCargo) throws BDException
	{
		return SqlBaseCargosDao.cargarDetAutorizacionesEstanciaXCargo(con, codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public ArrayList<Integer> cargarDetAutorizacionesXCargo(Connection con, double codigoDetalleCargo) throws BDException
	{
		return SqlBaseCargosDao.cargarDetAutorizacionesXCargo(con, codigoDetalleCargo);
	}
	
	/**
	 * Insertar cargos articulos consumo
	 * @param con
	 * @param dto
	 * @param loginUsuario
	 * @return
	 */
	public double insertarDetalleCargosArtConsumos(Connection con, DtoDetalleCargoArticuloConsumo dto, String loginUsuario ) throws BDException
	{
		return SqlBaseCargosDao.insertarDetalleCargosArtConsumos(con, dto, loginUsuario);
	}
	
	/**
	 * metodo que elimina el detalle cargo art consumo x codigo de detalle, 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public boolean eliminarDetalleCargoArticuloConsumoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws BDException
	{
		return SqlBaseCargosDao.eliminarDetalleCargoArticuloConsumoXCodigoDetalle(con, codigoDetalleCargo);
	}
	
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public ArrayList<DtoDetalleCargoArticuloConsumo> cargarDetalleCargosArticuloConsumo(Connection con, DtoDetalleCargoArticuloConsumo criteriosBusquedaDtoDetalleCargoArtConsumo)
	{
		return SqlBaseCargosDao.cargarDetalleCargosArticuloConsumo(con, criteriosBusquedaDtoDetalleCargoArtConsumo);
	}
	
	@Override
	public boolean modificarEstadosCargosCitas(
			int numeroSolicitud, int estado, String facturado,
			String eliminado, Connection con) {
		
		return SqlBaseCargosDao.modificarEstadosCargosCitas(numeroSolicitud, estado, facturado, eliminado, con);
	}

	@Override
	public int obtenerCentroAtencionCargoSolicitud(Connection con,int numeroSolicitud) throws BDException{
		return SqlBaseCargosDao.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud);
	}

	@Override
	public int obtenerCentroAtencionCentroCostoSolicitadoCargo(int centroCosto) {
		return SqlBaseCargosDao.obtenerCentroAtencionCentroCostoSolicitadoCargo(centroCosto);
	}
	
	@Override
	public ArrayList<BigDecimal> obtenerCodigosPkCargosDadoSolicitudes(
			Connection con, ArrayList<BigDecimal> listaSolicitudes) {
		return SqlBaseCargosDao.obtenerCodigosPkCargosDadoSolicitudes(con, listaSolicitudes);
	}
	
	/**
	 * Método obtiene cantidad de Inclusiones de un articulo por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoArticulo
	 * @return
	 */
	@Override
	public int consultarCantidadInclusionesExclusionesOrdenArticulo(Connection con,
			int codigoSubcuenta, int codigoArticulo) {

		return SqlBaseCargosDao.consultarCantidadInclusionesExclusionesOrdenArticulo(con, codigoSubcuenta, codigoArticulo);
	}
	
	/**
	 * Método obtiene cantidad de Inclusiones de un servicio por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoServicio
	 * @return
	 */
	@Override
	public int consultarCantidadInclusionesExclusionesOrdenServicio(Connection con,
			int codigoSubcuenta, int codigoServicio) {

		return SqlBaseCargosDao.consultarCantidadInclusionesExclusionesOrdenServicio(con, codigoSubcuenta, codigoServicio);
	}
}