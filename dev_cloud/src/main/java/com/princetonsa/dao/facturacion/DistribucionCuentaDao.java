package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.facturacion.DtoMontoCobro;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public interface DistribucionCuentaDao 
{

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public abstract HashMap<String, Object> consultarIngresosValidosDistribucion(Connection con, int codigoPersona, int codigoCentroAtencion);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract HashMap<String, Object> consultarEncabezadoUltimaDistribucion(Connection con, int codigoIngreso) throws BDException;

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public abstract boolean actualizarTipoComplejidadCuenta(Connection con, int tipoComplejidad , int cuenta) throws BDException;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean actualizarEncabezadoDistribucion(Connection con, HashMap vo) throws BDException;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarEncabezadoDistribucion(Connection con, HashMap vo) throws BDException;

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param nroPrioridad
	 * @return
	 */
	public abstract boolean actualizarPrioridadResponsable(Connection con, String subCuenta, int nroPrioridad) throws BDException;

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap consultarFiltroDistribucionResponsable(Connection con, String subCuenta) throws BDException;

	/**
	 * 
	 * @param con
	 * @param voInfoIngreso
	 * @param voRequisitosPaciente
	 * @param usuario 
	 * @return
	 */
	public abstract boolean modificarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario, HashMap voVerificacionDerechos);

	/**
	 * 
	 * @param con
	 * @param voInfoIngreso
	 * @param voRequisitosPaciente
	 * @param usuario
	 * @param viaIngreso 
	 * @return
	 */
	public abstract boolean insertarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario, int viaIngreso, HashMap voVerificacionDerechos);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoUltimoResponsable 
	 * @param conSolicitudes 
	 * @param convenio 
	 * @return
	 */
	public abstract boolean eliminarSubCuenta(Connection con, DtoSubCuentas subCuenta, double codigoUltimoResponsable, int convenio, boolean conSolicitudes) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoIngreso
	 * @return
	 */
	public abstract HashMap obtenerHirtoricoResponsable(Connection con, int codigoConvenio, int codigoIngreso);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap consultarInformacionPoliza(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean guardarFiltroDistribucion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param loginUsuario
	 * @return
	 */
	public abstract boolean modificarParametrosDistribucion(Connection con, HashMap vo, String loginUsuario);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean actualizarMontoAutorizado(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param incluirPaquetes 
	 * @param responsablesEliminados 
	 * @param subCuentasResponsables 
	 * @param liquidacionAutomatica 
	 * @return
	 */
	public abstract HashMap consultarDetSolicitudesPaciente(Connection con, int[] estados, boolean incluirPaquetes, String[] responsablesEliminados, String[] subCuentasResponsables, boolean liquidacionAutomatica) throws BDException;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarDetSolicitudesPaciente(Connection con, HashMap vo, boolean incluirPaquetes, String[] subCuentasResponsables);

	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @param servicio
	 * @param articulo
	 * @param serviciocx 
	 * @param tipodistribucion 
	 * @return
	 */
	public abstract HashMap consultarDistribucionSolicitud(Connection con, String solicitud, String servicio, String articulo, String serviciocx,String detCxHonorarios,String detAsCxSalMat, String tipodistribucion);

	/**
	 * 
	 * @param con
	 * @param codigoSolSubcuenta
	 * @return
	 */
	public abstract boolean eliminarSolicitudSubCuenta(Connection con, String codigoSolSubcuenta) throws BDException;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarSolicitudSubcuenta(Connection con, HashMap vo) throws BDException;

	/**
	 * 
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param servicioCX 
	 * @param esServicio
	 * @param estadoCargo 
	 */
	public abstract boolean eliminarSolicitudSubCuentaDetCargoXSolServArt(Connection con, int numSolicitud, String codServArt, String servicioCX,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, int estadoCargo) throws BDException;


		
	/**
	 * 
	 */
	public abstract boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt, boolean esServicio);

	/**
	 * 
	 * @param con
	 * @param cargoPadre
	 * @return
	 */
	public abstract HashMap consultarDetlleCargoDetPaqueteOrginal(Connection con, String cargoPadre) throws BDException;

	/**
	 * 
	 * @param con
	 * @param solSubcuentaPadre
	 * @return
	 */
	public abstract HashMap consultarSolSubCuentaDetPaqueteOrginal(Connection con, String solSubcuentaPadre) throws BDException;

	/**
	 * 
	 * @param con
	 * @param cargoPadre
	 * @return
	 */
	public abstract boolean eliminarDetalleCargoDetallePaquete(Connection con, int cargoPadre) throws BDException;

	/**
	 * 
	 * @param con
	 * @param solSubcuentaPadre
	 * @return
	 */
	public abstract boolean eliminarSolicitudSubCuentaDetallePaquete(Connection con, String solSubcuentaPadre) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param porcentaje
	 * @return
	 */
	public abstract boolean updateProceDetalleCargoSolSubDetPaquete(Connection con, double codigoDetalleCargo, double porcentaje,String usuario, String tipoDistribucion);

	/**
	 * 
	 * @param con
	 * @param detCargoOrginial
	 * @param solSubcuentaOriginal
	 * @param codigoDetCargo
	 * @param solicitudSubCuenta
	 * @param porcentaje
	 * @param convenio 
	 * @param tipoDistribucion 
	 * @return
	 */
	public abstract boolean insertarDetalleCargoSolSubDetPaquete(Connection con, HashMap detCargoOrginial, HashMap solSubcuentaOriginal, double codigoDetCargo, double solicitudSubCuenta, double porcentaje,String subCuenta,int convenio, String usuario, String tipoDistribucion) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public abstract HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona);
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarDetSolicitudesResponsableAvanzada(Connection con, String subCuenta, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estados
	 * @return
	 */
	public abstract HashMap consultarDetSolicitudesResponsable(Connection con, String subCuenta, int[] estados);

	
	/**
	 * Metodo que cambia el estado de una cuenta a "Cuenta en Proceso de Distribucion"
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public abstract int cuentaProcesoDistribucion(Connection con,int idCuenta);
	
	/**
	 * Método que inicializa el proceso de distribucion Inserta en la tabla
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return n�mero mayor que 0 si se inici� el proceso correctamente
	 */
	public abstract int empezarProcesoDistribucion(Connection con, int idCuenta, String loginUsuario);
	
	/**
	 * M�todo que cancela todos los procesos de distribucion en proceso
	 * @param con
	 * @return
	 */
	public abstract int cancelarTodosLosProcesosDeDistribucion(Connection con);
	
	/**
	 * M�todo para cancelar el proceso de distribucion (transaccional)
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return
	 */
	public abstract int cancelarProcesoDistribucionTransaccional(	Connection con, int idCuenta, String estado);
	
	/**
	 * M�todo para terminar el proceso de distribucion Si el estado es null, se ejecuta no transaccional
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return
	 */
	public abstract int finalizarProcesoDistribuciontransaccional(Connection con,int idCuenta, String estado);
	
	/**
	 * M�todo que termina el proceso de distribuci�n de la cuenta!
	 * NO CAMBIA ESTADOS
	 * @param con
	 * @param codigoCuentaPaciente
	 */
	public abstract boolean terminarProcesoDistribucion(Connection con, int codigoCuentaPaciente);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract ResultadoBoolean reacomodarPrioridades(Connection con, int codigoIngreso, PersonaBasica persona, UsuarioBasico usuario ) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract HashMap consultarCantidadesSubCuentaFacturadas(Connection con, int codigoIngreso) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param loginUsuario
	 * @return
	 */
	public abstract boolean empezarProcesoDistribucionIngreso(Connection con, int codigoIngreso, String loginUsuario);


	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract boolean cancelarIngresoProcesoDistribucion(Connection con, int codigoIngreso);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param subCuenta 
	 * @return
	 */
	public abstract double obtenerValorFacturadoSoat(Connection con, int codigoIngreso, int codigoConvenio) throws BDException;

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap consultarEsquemasTarifariosInventario(Connection con, String subCuenta);

	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap consultarEsquemasTarifariosProcedimientos(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @param esInventario
	 * @return
	 */
	public abstract boolean eliminarEsquema(Connection con, String codigoEsquema, boolean esInventario);

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public abstract HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema);

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public abstract HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarEsquemasInventario(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarEsquemasInventario(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarEsquemasProcedimientos(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarEsquemasProcedimientos(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract boolean limpiarParametrosDistribucion(Connection con,int codigoIngreso) throws BDException;

	/**
	 * @param con
	 * @param subCuenta
	 * @param incluirPaquetizadas
	 * @param agruparPortatiles
	 * @param parametrosBusquedaAvanzadaResponsable
	 * @return
	 */
	public abstract ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaBusquedaAvanzada(Connection con, String subCuenta, boolean incluirPaquetizadas, boolean agruparPortatiles, HashMap parametrosBusquedaAvanzadaResponsable);

	/**
	 * M�todo implementado para consultar si un Articulo � Servicio
	 * tiene justificaci�n y si la respuesta es afirmativa se debe de
	 * eliminar los registros de los reponsables de esa justificaci�n
	 * antes de realizar el proceso de Distribuci�n de la Cuenta
	 * @param con
	 * @param numeroSolicitud
	 * @param servArt
	 * @param esArticulo
	 */
	public abstract int eliminarResponsablesJustificacionSolicitud(Connection con, int numeroSolicitud, String servArt, boolean esArticulo) throws BDException;
	
	/**
	 * Cargar Servicio Articulos 
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	public HashMap cargarServiciosArticulos (Connection con, HashMap criterios);
	
	/**
	 * Cargar Listado Ingresos 
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	public HashMap cargarListadoIngresos (Connection con, HashMap criterios);
	
	
	/**
	 * @param con
	 * @param codigoMontoCobro
	 * @return montocobro
	 */
	public DtoMontoCobro consultarMontoCobro(Connection con,Integer codigoMontoCobro);
	
	/**
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap<String, Object> consultarVerificacionDerechos(Connection con, int subCuenta);
	
	/**
	 * @param con
	 * @param subCuenta
	 * @param voVerificacionDerechos
	 * @param fechaActual
	 * @param horaActual
	 * @param usuario
	 * @return
	 */
	public boolean actualizarVerificacionDerechos(Connection con, int subCuenta, HashMap voVerificacionDerechos, Date fechaActual, String horaActual, String usuario); 
}