package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.facturacion.DtoMontoCobro;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dao.facturacion.DistribucionCuentaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseDistribucionCuentaDao;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class OracleDistribucionCuentaDao implements DistribucionCuentaDao 
{

	/**
	 * 
	 */
	public HashMap<String, Object> consultarIngresosValidosDistribucion(Connection con, int codigoPersona,int codigoCentroAtencion) 
	{
		return SqlBaseDistribucionCuentaDao.consultarIngresosValidosDistribucion(con,codigoPersona,codigoCentroAtencion);
	}
	

	/**
	 * 
	 */
	public HashMap<String, Object> consultarEncabezadoUltimaDistribucion(Connection con, int codigoIngreso) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.consultarEncabezadoUltimaDistribucion(con,codigoIngreso);
	}


	/**
	 * 
	 */
	public boolean actualizarEncabezadoDistribucion(Connection con, HashMap vo) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.actualizarEncabezadoDistribucion(con,vo);
	}


	/**
	 * 
	 */
	public boolean actualizarPrioridadResponsable(Connection con, String subCuenta, int nroPrioridad) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.actualizarPrioridadResponsable(con,subCuenta,nroPrioridad);
	}


	/**
	 * 
	 */
	public boolean actualizarTipoComplejidadCuenta(Connection con, int tipoComplejidad, int cuenta) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.actualizarTipoComplejidadCuenta(con,tipoComplejidad,cuenta);
	}


	/**
	 * 
	 */
	public boolean insertarEncabezadoDistribucion(Connection con, HashMap vo) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.insertarEncabezadoDistribucion(con,vo);
	}
	

	/**
	 * 
	 */
	public HashMap consultarFiltroDistribucionResponsable(Connection con, String subCuenta) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.consultarFiltroDistribucionResponsable(con,subCuenta);
	}
	

	/**
	 * 
	 */
	public boolean modificarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario, HashMap voVerificacionDerechos) 
	{
		return SqlBaseDistribucionCuentaDao.modificarSubCuenta(con,voInfoIngreso,voRequisitosPaciente,usuario, voVerificacionDerechos);
	}
	

	/**
	 * 
	 */
	public boolean insertarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario,int viaIngreso, HashMap voVerificacionDerechos) 
	{
		
		try
		{
			String cadena="select seq_sub_cuentas.nextval from dual";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return SqlBaseDistribucionCuentaDao.insertarSubCuenta(con,rs.getInt(1),voInfoIngreso,voRequisitosPaciente,usuario,viaIngreso, voVerificacionDerechos);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	

	/**
	 * 
	 */
	public boolean eliminarSubCuenta(Connection con, DtoSubCuentas subCuenta, double codigoUltimoResponsable, int convenio, boolean conSolicitudes) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.eliminarSubCuenta(con,subCuenta,codigoUltimoResponsable,convenio,conSolicitudes);
	}


	/**
	 * 
	 */
	public HashMap obtenerHirtoricoResponsable(Connection con, int codigoConvenio, int codigoIngreso) 
	{
		return SqlBaseDistribucionCuentaDao.obtenerHirtoricoResponsable(con,codigoConvenio,codigoIngreso);
	}
	

	/**
	 * 
	 */
	public HashMap consultarInformacionPoliza(Connection con, String subCuenta) 
	{
		return SqlBaseDistribucionCuentaDao.consultarInformacionPoliza(con,subCuenta);
	}
	

	/**
	 * 
	 */
	public boolean guardarFiltroDistribucion(Connection con, HashMap vo) 
	{
		return SqlBaseDistribucionCuentaDao.guardarFiltroDistribucion(con,vo);
	}


	/**
	 * 
	 */
	public boolean modificarParametrosDistribucion(Connection con, HashMap vo, String loginUsuario) 
	{
		return SqlBaseDistribucionCuentaDao.modificarParametrosDistribucion(con,vo,loginUsuario);
	}
	

	/**
	 * 
	 */
	public boolean actualizarMontoAutorizado(Connection con, HashMap vo) 
	{
		return SqlBaseDistribucionCuentaDao.actualizarMontoAutorizado(con,vo);
	}
	


	/**
	 * 
	 */
	public HashMap consultarDetSolicitudesPaciente(Connection con, int[] estados,boolean incluirPaquetes, String[] responsablesEliminados, String[] subCuentasResponsables,boolean liquidacionAutomatica) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.consultarDetSolicitudesPaciente(con,estados,incluirPaquetes,responsablesEliminados,subCuentasResponsables,liquidacionAutomatica);
	}

	/**
	 * 
	 */
	public HashMap consultarDetSolicitudesPaciente(Connection con, HashMap vo,boolean incluirPaquetes, String[] subCuentasResponsables) 
	{
		return SqlBaseDistribucionCuentaDao.consultarDetSolicitudesPaciente(con,vo,incluirPaquetes,subCuentasResponsables);
	}
	

	/**
	 * 
	 */
	public HashMap consultarDistribucionSolicitud(Connection con, String solicitud, String servicio, String articulo,String serviciocx,String detCxHonorarios,String detAsCxSalMat, String tipodistribucion) 
	{
		return SqlBaseDistribucionCuentaDao.consultarDistribucionSolicitud(con,solicitud,servicio,articulo,serviciocx,detCxHonorarios,detAsCxSalMat,tipodistribucion);
	}	

	/**
	 * 
	 */
	public boolean eliminarSolicitudSubCuenta(Connection con, String codigoSolSubcuenta)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.eliminarSolicitudSubCuenta(con,codigoSolSubcuenta);
	}


	/**
	 * 
	 */
	public boolean modificarSolicitudSubcuenta(Connection con, HashMap vo)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.modificarSolicitudSubcuenta(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarSolicitudSubCuentaDetCargoXSolServArt(Connection con, int numSolicitud, String codServArt,String servicioCX,String detCxHonorarios,String detAsCxSalMat, boolean esServicio,int estadoCargo) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,codServArt, servicioCX,detCxHonorarios,detAsCxSalMat,esServicio,estadoCargo);
	}	
	
	
	/**
	 * 
	 */
	public boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt,  boolean esServicio) 
	{
		return SqlBaseDistribucionCuentaDao.actualizarCantidadDetCargo(con, cantidad,subCuenta,numeroSolicitud,codServArt,esServicio);
	}
	

	/**
	 * 
	 */
	public HashMap consultarDetlleCargoDetPaqueteOrginal(Connection con, String cargoPadre)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.consultarDetlleCargoDetPaqueteOrginal(con,cargoPadre);
	}

	/**
	 * 
	 */
	public HashMap consultarSolSubCuentaDetPaqueteOrginal(Connection con, String solSubcuentaPadre)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.consultarSolSubCuentaDetPaqueteOrginal(con,solSubcuentaPadre);
	}

	/**
	 * 
	 */
	public boolean eliminarDetalleCargoDetallePaquete(Connection con, int cargoPadre)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.eliminarDetalleCargoDetallePaquete(con,cargoPadre);
	}

	/**
	 * 
	 */
	public boolean eliminarSolicitudSubCuentaDetallePaquete(Connection con, String solSubcuentaPadre)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.eliminarSolicitudSubCuentaDetallePaquete(con,solSubcuentaPadre);
	}
	
	/**
	 * 
	 */
	public boolean updateProceDetalleCargoSolSubDetPaquete(Connection con, double codigoDetalleCargo, double porcentaje,String usuario, String tipoDistribucion) 
	{
		return SqlBaseDistribucionCuentaDao.updateProceDetalleCargoSolSubDetPaquete(con,codigoDetalleCargo,porcentaje,usuario,tipoDistribucion);
	}

	/**
	 * 
	 */
	public boolean insertarDetalleCargoSolSubDetPaquete(Connection con, HashMap detCargoOrginial, HashMap solSubcuentaOriginal, double codigoDetCargo, double solicitudSubCuenta, double porcentaje,String subCuenta,int convenio,String usuario, String tipoDistribucion) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.insertarDetalleCargoSolSubDetPaquete(con,detCargoOrginial,solSubcuentaOriginal,codigoDetCargo,solicitudSubCuenta,porcentaje,subCuenta,convenio,usuario,tipoDistribucion);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona) 
	{
		return SqlBaseDistribucionCuentaDao.consultarIngresos(con,codigoPersona);
	}

	/**
	 * 
	 */
	public HashMap consultarDetSolicitudesResponsableAvanzada(Connection con, String subCuenta, HashMap vo)
	{
		return SqlBaseDistribucionCuentaDao.consultarDetSolicitudesResponsableAvanzada(con,subCuenta,vo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetSolicitudesResponsable(Connection con, String subCuenta, int[] estados)
	{
		return SqlBaseDistribucionCuentaDao.consultarDetSolicitudesResponsable(con,subCuenta,estados);
	}
	
	/**
	 * Metodo que cambia el estado de una cuenta a "Cuenta en Proceso de Distribucion"
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int cuentaProcesoDistribucion(Connection con,int idCuenta)
	{
		return SqlBaseDistribucionCuentaDao.cuentaProcesoDistribucion(con, idCuenta);
	}
	
	
	/**
	 * Método que inicializa el proceso de distribucion Inserta en la tabla
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return número mayor que 0 si se inició el proceso correctamente
	 */
	public int empezarProcesoDistribucion(Connection con, int idCuenta, String loginUsuario)
	{
		return SqlBaseDistribucionCuentaDao.empezarProcesoDistribucion(con, idCuenta, loginUsuario);
	}
	
	/**
	 * Método que cancela todos los procesos de distribucion en proceso
	 * @param con
	 * @return
	 */
	public int cancelarTodosLosProcesosDeDistribucion(Connection con)
	{
		return SqlBaseDistribucionCuentaDao.cancelarTodosLosProcesosDeDistribucion(con);
	}
	
	/**
	 * Método para cancelar el proceso de distribucion (transaccional)
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return
	 */
	public int cancelarProcesoDistribucionTransaccional(	Connection con, int idCuenta, String estado)
	{
		return SqlBaseDistribucionCuentaDao.cancelarProcesoDistribucionTransaccional(con, idCuenta, estado);
	}
	
	
	/**
	 * Método para terminar el proceso de distribucion Si el estado es null, se ejecuta no transaccional
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return
	 */
	public int finalizarProcesoDistribuciontransaccional(Connection con,int idCuenta, String estado)
	{
		return SqlBaseDistribucionCuentaDao.finalizarProcesoDistribuciontransaccional(con, idCuenta, estado);
	}
	
	/**
	 * Método que termina el proceso de distribución de la cuenta!
	 * NO CAMBIA ESTADOS
	 * @param con
	 * @param codigoCuentaPaciente
	 */
	public boolean terminarProcesoDistribucion(Connection con, int codigoCuentaPaciente)
	{
		return SqlBaseDistribucionCuentaDao.terminarProcesoDistribucion(con, codigoCuentaPaciente);
	}
	

	/**
	 * 
	 */
	//	se agregan los campos de PersonaBasica paciente, UsuarioBasico usuario debido a que se debe realizar la insercion en la tabla ax_pacien en la funcion  reacomodarPrioridades en el sqlBase
	public ResultadoBoolean reacomodarPrioridades(Connection con, int codigoIngreso, PersonaBasica persona, UsuarioBasico usuario )  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.reacomodarPrioridades(con,codigoIngreso,persona,usuario );
	}
	

	/**
	 * 
	 */
	public HashMap consultarCantidadesSubCuentaFacturadas(Connection con, int codigoIngreso)  throws BDException
	{
		return SqlBaseDistribucionCuentaDao.consultarCantidadesSubCuentaFacturadas(con,codigoIngreso);
	}
	
	/**
	 * 
	 */
	public boolean empezarProcesoDistribucionIngreso(Connection con, int codigoIngreso, String loginUsuario) 
	{
		return SqlBaseDistribucionCuentaDao.empezarProcesoDistribucionIngreso(con,codigoIngreso,loginUsuario);
	}


	/**
	 * 
	 */
	public boolean cancelarIngresoProcesoDistribucion(Connection con, int codigoIngreso) 
	{
		return SqlBaseDistribucionCuentaDao.cancelarIngresoProcesoDistribucion(con,codigoIngreso);
	}


	/**
	 * 
	 */
	public double obtenerValorFacturadoSoat(Connection con, int codigoIngreso,int codigoConvenio) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.obtenerValorFacturadoSoat(con,codigoIngreso,codigoConvenio);
	}

	

	/**
	 * 
	 */
	public HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema) 
	{
		return SqlBaseDistribucionCuentaDao.consultarEsquemaInventarioLLave(con,codigoEsquema);
	}

	/**
	 * 
	 */
	public HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema) 
	{
		return SqlBaseDistribucionCuentaDao.consultarEsquemaProcedimientoLLave(con,codigoEsquema);
	}

	/**
	 * 
	 */
	public boolean eliminarEsquema(Connection con, String codigoEsquema, boolean esInventario) 
	{
		return SqlBaseDistribucionCuentaDao.eliminarEsquema(con,codigoEsquema,esInventario);
	}

	/**
	 * 
	 */
	public boolean insertarEsquemasInventario(Connection con, HashMap vo) 
	{
		return SqlBaseDistribucionCuentaDao.insertarEsquemasInventario(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return SqlBaseDistribucionCuentaDao.insertarEsquemasProcedimientos(con,vo);
	}

	/**
	 * 
	 */
	public boolean modificarEsquemasInventario(Connection con, HashMap vo) 
	{
		return SqlBaseDistribucionCuentaDao.modificarEsquemasInventario(con,vo);
	}

	/**
	 * 
	 */
	public boolean modificarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return SqlBaseDistribucionCuentaDao.modificarEsquemasProcedimientos(con,vo);
	}


	/**
	 * 
	 */
	public HashMap consultarEsquemasTarifariosInventario(Connection con, String subCuenta) 
	{
		return SqlBaseDistribucionCuentaDao.consultarEsquemasTarifariosInventario(con,subCuenta);

	}

	/**
	 * 
	 */
	public HashMap consultarEsquemasTarifariosProcedimientos(Connection con, String subCuenta) 
	{
		return SqlBaseDistribucionCuentaDao.consultarEsquemasTarifariosProcedimientos(con,subCuenta);
	}
	
	
	/**
	 * 
	 */
	public boolean limpiarParametrosDistribucion(Connection con,int codigoIngreso) throws BDException 
	{
		return SqlBaseDistribucionCuentaDao.limpiarParametrosDistribucion(con,codigoIngreso);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaBusquedaAvanzada(Connection con, String subCuenta, boolean incluirPaquetizadas, boolean agruparPortatiles, HashMap parametrosBusquedaAvanzadaResponsable)
	{
		return SqlBaseDistribucionCuentaDao.obtenerSolicitudesSubCuentaBusquedaAvanzada(con, subCuenta, incluirPaquetizadas, agruparPortatiles, parametrosBusquedaAvanzadaResponsable);
	}
	
	/**
	 * 
	 */
	public int eliminarResponsablesJustificacionSolicitud(Connection con, int numeroSolicitud, String servArt, boolean esArticulo) throws BDException
	{
		return SqlBaseDistribucionCuentaDao.eliminarResponsablesJustificacionSolicitud(con, numeroSolicitud, servArt, esArticulo);
	}
	
	/**
	 * Cargar Servicio Articulos 
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	public HashMap cargarServiciosArticulos (Connection con, HashMap criterios){
		return SqlBaseDistribucionCuentaDao.cargarServiciosArticulos(con, criterios);
	}
	
	/**
	 * Cargar Listado Ingresos 
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	public HashMap cargarListadoIngresos (Connection con, HashMap criterios){
		return SqlBaseDistribucionCuentaDao.cargarListadoIngresos(con, criterios);
	}
	
	
	/**
	 * @see com.princetonsa.dao.facturacion.DistribucionCuentaDao#consultarMontoCobro(java.sql.Connection, java.lang.Integer)
	 */
	public DtoMontoCobro consultarMontoCobro(Connection con,Integer codigoMontoCobro){
		return SqlBaseDistribucionCuentaDao.consultarMontoCobro(con, codigoMontoCobro);
	}


	/** (non-Javadoc)
	 * @see com.princetonsa.dao.facturacion.DistribucionCuentaDao#consultarVerificacionDerechos(java.sql.Connection, int)
	 */
	@Override
	public HashMap<String, Object> consultarVerificacionDerechos(
			Connection con, int subCuenta) {
		return SqlBaseDistribucionCuentaDao.consultarVerificacionDerechos(con, subCuenta);
	}


	/** (non-Javadoc)
	 * @see com.princetonsa.dao.facturacion.DistribucionCuentaDao#actualizarVerificacionDerechos(java.sql.Connection, int, java.util.HashMap, java.sql.Date, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean actualizarVerificacionDerechos(Connection con,
			int subCuenta, HashMap voVerificacionDerechos, Date fechaActual,
			String horaActual, String usuario) {
		return SqlBaseDistribucionCuentaDao.actualizarVerificacionDerechos(con, subCuenta, voVerificacionDerechos, fechaActual, horaActual, usuario);
	}
	
	
}