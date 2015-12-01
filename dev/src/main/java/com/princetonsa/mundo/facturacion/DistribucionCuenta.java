/*
 * Jul 5, 2007
 * Proyect axioma
 * Paquete com.princetonsa.mundo.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.DistribucionCuentaDao;
import com.princetonsa.dto.facturacion.DtoMontoCobro;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.servicio.impl.facturacion.CalculoValorCobrarPaciente;
import com.servinte.axioma.servicio.interfaz.facturacion.ICalculoValorCobrarPaciente;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DistribucionCuenta 
{
	
	/**
	 * Objeto para manejar la conexion dao, a la bd.
	 */
	private DistribucionCuentaDao objetoDao;

	/**
	 * 
	 */
	public DistribucionCuenta() 
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getDistribucionCuentaDao();
		}
	}

	/**
	 * Metodo que realiza la consulta de los ingresos validos para distribuir de un paciente.
	 * @param con
	 * @param codigoPersona
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public HashMap<String, Object> consultarIngresosValidosDistribucion(Connection con, int codigoPersona, int codigoCentroAtencion) 
	{
		return objetoDao.consultarIngresosValidosDistribucion(con,codigoPersona,codigoCentroAtencion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public HashMap<String, Object> consultarEncabezadoUltimaDistribucion(Connection con, int codigoIngreso) throws IPSException
	{
		return objetoDao.consultarEncabezadoUltimaDistribucion(con,codigoIngreso);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static HashMap<String, Object> consultarEncabezadoUltimaDistribucionEstatico(Connection con, int codigoIngreso) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().consultarEncabezadoUltimaDistribucion(con,codigoIngreso);
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param cuenta 
	 * @return
	 */
	public boolean actualizarTipoComplejidadCuenta(Connection con, int tipoComplejidad, int cuenta) throws IPSException
	{
		return objetoDao.actualizarTipoComplejidadCuenta(con,tipoComplejidad,cuenta);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean actualizarEncabezadoDistribucion(Connection con, HashMap vo) throws IPSException
	{
		return objetoDao.actualizarEncabezadoDistribucion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEncabezadoDistribucion(Connection con, HashMap vo) throws IPSException
	{
		return objetoDao.insertarEncabezadoDistribucion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param nroPrioridad
	 * @return
	 */
	public boolean actualizarPrioridadResponsable(Connection con, String subCuenta, int nroPrioridad) throws IPSException
	{
		return objetoDao.actualizarPrioridadResponsable(con,subCuenta,nroPrioridad);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap consultarFiltroDistribucionResponsable(Connection con, String subCuenta) throws IPSException
	{
		return objetoDao.consultarFiltroDistribucionResponsable(con,subCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param voInfoIngreso
	 * @param voRequisitosPaciente
	 * @param usuario 
	 * @return
	 */
	public boolean modificarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario, HashMap voVerificacionDerechos) 
	{
		return objetoDao.modificarSubCuenta(con,voInfoIngreso,voRequisitosPaciente,usuario, voVerificacionDerechos);
	}

	/**
	 * 
	 * @param con
	 * @param i 
	 * @param map
	 * @param map2
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario, int viaIngreso, HashMap voVerificacionDerechos) 
	{
		return objetoDao.insertarSubCuenta(con,voInfoIngreso,voRequisitosPaciente,usuario,viaIngreso, voVerificacionDerechos);
	}

	/**
	 * Metodo que realiza el proceso de la eliminacion de un responsable, es decir:
	 * 1- actualiza los registros que aun estan en solicitudes-subcuenta y det_cargos al ultimo responsable.
	 * 2- elimina los registros en titular-poliza
	 * 3- elimina los registros en informacion - poliza
	 * 4- elimina los registros en requisitors_pac_subcuenta
	 * 5- elimina los registros en responsables_subcuenta
	 * 6- crea el historico de la info en subcuenta(de no existir el historico).
	 * 7- elimina la subcuenta.
	 * @param con
	 * @param subCuenta
	 * @param codigoUltimoResponsable 
	 * @param convenio 
	 * @param conSolicitudes 
	 * @return
	 */
	public boolean eliminarSubCuenta(Connection con, DtoSubCuentas subCuenta, double codigoUltimoResponsable, int convenio, boolean conSolicitudes) throws IPSException 
	{
		return objetoDao.eliminarSubCuenta(con,subCuenta,codigoUltimoResponsable,convenio,conSolicitudes);
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoIngreso
	 * @return
	 */
	public HashMap obtenerHirtoricoResponsable(Connection con, int codigoConvenio, int codigoIngreso) 
	{
		return objetoDao.obtenerHirtoricoResponsable(con,codigoConvenio,codigoIngreso);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap consultarInformacionPoliza(Connection con, String subCuenta) 
	{
		return objetoDao.consultarInformacionPoliza(con,subCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean guardarFiltroDistribucion(Connection con, HashMap vo) 
	{
		return objetoDao.guardarFiltroDistribucion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarParametrosDistribucion(Connection con, HashMap vo, String loginUsuario) 
	{
		return objetoDao.modificarParametrosDistribucion(con,vo,loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean actualizarMontoAutorizado(Connection con, HashMap vo) 
	{
		return objetoDao.actualizarMontoAutorizado(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param incluirPaquetes 
	 * @param subCuentasResponsables 
	 * @param liquidacionAutomatica 
	 * @param responsablesEliminados. se deben incluir las solicitudes anuladas de los responsables eliminados.
	 * @return
	 */
	public HashMap consultarDetSolicitudesPaciente(Connection con, int[] estados, boolean incluirPaquetes, String[] responsablesEliminados, String[] subCuentasResponsables, boolean liquidacionAutomatica) throws IPSException 
	{
		return objetoDao.consultarDetSolicitudesPaciente(con,estados,incluirPaquetes,responsablesEliminados,subCuentasResponsables,liquidacionAutomatica);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param vo
	 * @return
	 */
	public HashMap consultarDetSolicitudesPaciente(Connection con, HashMap vo,boolean incluirPaquetes, String[] subCuentasResponsables) 
	{
		return objetoDao.consultarDetSolicitudesPaciente(con,vo,incluirPaquetes,subCuentasResponsables);
	}

	/**
	 * 
	 * @param con
	 * @param serviciocx 
	 * @param tipodistribucion 
	 * @param string
	 * @param string2
	 * @param string3
	 * @return
	 */
	public HashMap consultarDistribucionSolicitud(Connection con, String solicitud, String servicio, String articulo, String serviciocx,String detCxHonorarios,String detAsCxSalMat, String tipodistribucion) 
	{
		return objetoDao.consultarDistribucionSolicitud(con,solicitud,servicio, articulo,serviciocx,detCxHonorarios,detAsCxSalMat,tipodistribucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoSolSubcuenta
	 * @return
	 */
	public boolean eliminarSolicitudSubCuenta(Connection con, String codigoSolSubcuenta) throws IPSException
	{
		return objetoDao.eliminarSolicitudSubCuenta(con,codigoSolSubcuenta);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarSolicitudSubcuenta(Connection con, HashMap vo) throws IPSException
	{
		return objetoDao.modificarSolicitudSubcuenta(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCuenta
	 * @param codigoResponsable
	 * @param  
	 * @param temporalCodigoServicio
	 * @param string
	 * @param temporalCantidadServicio
	 * @return
	 */
	public double insertarSolicitudSubCuenta(Connection con, int numeroSolicitud, int codigoCuenta, String codigoResponsable, String codigoServicio, String codigoArticulo, int cantidad, String cubierto,int tipoSolicitud,String codigoServicioCX,String detCxHonorarios,String detAsCxSalMat, int codigoTipoAsocio,String tipoDistribucion,String usuario,String porcentaje,String monto,String paquetizada,String solSubCuentaPadre) throws IPSException 
	{
		DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
		
		try {
			dto.setCuenta(codigoCuenta+"");
			dto.setSubCuenta(codigoResponsable);
			dto.setNumeroSolicitud(numeroSolicitud+"");
			dto.setServicio(new InfoDatosString(codigoServicio));
			dto.setArticulo(new InfoDatosString(codigoArticulo));
			dto.setCantidad(cantidad+"");
			dto.setCubierto(cubierto);
			dto.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
			dto.setPaquetizada(ConstantesBD.acronimoNo);
			if(UtilidadTexto.isEmpty(tipoDistribucion))
				dto.setTipoDistribucion(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad));
			else
				dto.setTipoDistribucion(new InfoDatosString(tipoDistribucion));
			dto.setServicioCX(new InfoDatosString(codigoServicioCX+""));
			dto.setDetcxhonorarios(Utilidades.convertirAEntero(detCxHonorarios));
			dto.setDetasicxsalasmat(Utilidades.convertirAEntero(detAsCxSalMat));
			dto.setTipoAsocio(new InfoDatosInt(codigoTipoAsocio));
			dto.setUsuarioModifica(usuario);
			dto.setPorcentajeCargado(porcentaje);
			dto.setMonto(monto);
			dto.setPaquetizada(paquetizada);
			dto.setSolicitudPadre(solSubCuentaPadre);
			int resultado=Solicitud.insertarSolicitudSubCuenta(con, dto, "continuar");
			return resultado;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
	}

	/**
	 * 
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param servicioCX 
	 * @param esServicio
	 * @param estadoCargo 
	 * @return
	 */
	public boolean eliminarSolicitudSubCuentaDetCargoXSolServArt(Connection con,  int numSolicitud, String codServArt, String servicioCX,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, int estadoCargo) throws IPSException
	{
		return objetoDao.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,codServArt,servicioCX,detCxHonorarios,detAsCxSalMat,esServicio,estadoCargo);
	}

//	/**
//	 * 
//	 */
//	public boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt,  boolean esServicio) 
//	{
//		return objetoDao.actualizarCantidadDetCargo(con, cantidad,subCuenta,numeroSolicitud,codServArt,esServicio);
//	}

	/**
	 * 
	 * @param con
	 * @param cargoPadre
	 * @return
	 */
	public HashMap consultarDetlleCargoDetPaqueteOrginal(Connection con, String cargoPadre) throws IPSException
	{
		return objetoDao.consultarDetlleCargoDetPaqueteOrginal(con, cargoPadre);
	}

	/**
	 * 
	 * @param con
	 * @param solSubcuentaPadre
	 * @return
	 */
	public HashMap consultarSolSubCuentaDetPaqueteOrginal(Connection con, String solSubcuentaPadre) throws IPSException
	{
		return objetoDao.consultarSolSubCuentaDetPaqueteOrginal(con, solSubcuentaPadre);
	}

	/**
	 * 
	 * @param con
	 * @param cargoPadre
	 * @return
	 */
	public boolean eliminarDetalleCargoDetallePaquete(Connection con, int cargoPadre) throws IPSException
	{
		return objetoDao.eliminarDetalleCargoDetallePaquete(con, cargoPadre);
	}

	/**
	 * 
	 * @param con
	 * @param solSubcuenta
	 * @return
	 */
	public boolean eliminarSolicitudSubCuentaDetallePaquete(Connection con, String solSubcuentaPadre) throws IPSException
	{
		return objetoDao.eliminarSolicitudSubCuentaDetallePaquete(con, solSubcuentaPadre);

	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param porcentaje
	 * @param tipoDistribucion 
	 * @param usuario 
	 * @return
	 */
	public boolean updateProceDetalleCargoSolSubDetPaquete(Connection con, double codigoDetalleCargo, double porcentaje, String usuario, String tipoDistribucion) 
	{
		return objetoDao.updateProceDetalleCargoSolSubDetPaquete(con, codigoDetalleCargo,porcentaje,usuario,tipoDistribucion);
	}

	/**
	 * 
	 * @param con
	 * @param detCargoOrginial
	 * @param solSubcuentaOriginal
	 * @param codigoDetCargo
	 * @param solicitudSubCuenta
	 * @param porcentaje
	 * @param subCuenta 
	 * @param convenio 
	 * @param usuario 
	 * @return
	 */
	public boolean insertarDetalleCargoSolSubDetPaquete(Connection con, HashMap detCargoOrginial, HashMap solSubcuentaOriginal, double codigoDetCargo, double solicitudSubCuenta, double porcentaje, String subCuenta, int convenio, String usuario, String tipoDistribucion) throws IPSException
	{
		return objetoDao.insertarDetalleCargoSolSubDetPaquete(con, detCargoOrginial,solSubcuentaOriginal,codigoDetCargo,solicitudSubCuenta,porcentaje,subCuenta,convenio,usuario,tipoDistribucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona) 
	{
		return objetoDao.consultarIngresos(con,codigoPersona);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo 
	 * @param codigoIngreso
	 * @param vo
	 * @param incluirPaquetes
	 * @return
	 */
	public HashMap consultarDetSolicitudesResponsableAvanzada(Connection con, String subCuenta, HashMap vo) 
	{
		return objetoDao.consultarDetSolicitudesResponsableAvanzada(con,subCuenta,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estados
	 * @return
	 */
	public HashMap consultarDetSolicitudesResponsable(Connection con, String subCuenta, int[] estados)
	{
		return objetoDao.consultarDetSolicitudesResponsable(con,subCuenta,estados);
	}
	
	 /**
	 * Metodo que cambia el estado de una cuenta a "Cuenta en Proceso de Distribucion"
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int cuentaProcesoDistribucion(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().cuentaProcesoDistribucion(con, idCuenta);
	}
	
	/**
	  * Metodo para poner el estado de una cuenta en proceso de distribucion dado su ID
	  * @param con
	  * @param idCuenta
	  * @param loginUsuario
	  * @return
	  */
	 public static int empezarProcesoDistribucion(Connection con, int idCuenta, String loginUsuario) 
	 {
	 	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().empezarProcesoDistribucion(con, idCuenta, loginUsuario);
	 }
	 
	/**
	 * Método que cancela todos los procesos de facturación en proceso
	 * @param con Conexión con la BD
	 * @return numero de cancelaciones
	 */
	public static int cancelarTodosLosProcesosDeDistribucion(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().cancelarTodosLosProcesosDeDistribucion(con);
	}
	
	/**
	 * Método para cancelar el proceso de distribucion (transaccional)
	 * @param con
	 * @param idCuenta
	 * @return true si se canceló el proceso correctamente
	 */
	public static boolean cancelarProcesoDistribucion(Connection con, int idCuenta)
	{
		try
		{	
			int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().cancelarProcesoDistribucionTransaccional(con, idCuenta, ConstantesBD.inicioTransaccion);
			if(resultado==0)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return false;
			}
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().finalizarProcesoDistribuciontransaccional(con, idCuenta, ConstantesBD.finTransaccion)>0 && resultado>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que termina el proceso de distribución de la cuenta!
	 * NO CAMBIA ESTADOS
	 * @param con
	 * @param codigoCuentaPaciente
	 */
	public boolean terminarProcesoDistribucion(Connection con, int codigoCuentaPaciente)
	{
		return objetoDao.terminarProcesoDistribucion(con, codigoCuentaPaciente);
	}

		/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public ResultadoBoolean reacomodarPrioridades(Connection con, int codigoIngreso, PersonaBasica persona, UsuarioBasico usuario ) throws IPSException
	{
//		se agregan los campos de PersonaBasica paciente, UsuarioBasico usuario debido a que se debe realizar la insercion en la tabla ax_pacien en la funcion  reacomodarPrioridades en el sqlBase
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().reacomodarPrioridades(con,codigoIngreso,persona,usuario );
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public HashMap consultarCantidadesSubCuentaFacturadas(Connection con, int codigoIngreso) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().consultarCantidadesSubCuentaFacturadas(con,codigoIngreso);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param loginUsuario
	 */
	public static boolean empezarProcesoDistribucionIngreso(Connection con, int codigoIngreso, String loginUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().empezarProcesoDistribucionIngreso(con,codigoIngreso,loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean cancelarIngresoProcesoDistribucion(Connection con, int codigoIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().cancelarIngresoProcesoDistribucion(con,codigoIngreso);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param subCuenta 
	 * @return
	 */
	public double obtenerValorFacturadoSoat(Connection con, int codigoIngreso, int codigoConvenio) throws IPSException
	{
		return objetoDao.obtenerValorFacturadoSoat(con,codigoIngreso,codigoConvenio);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static double obtenerValorFacturadoSoatEstatico(Connection con, int codigoIngreso, int codigoConvenio) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDistribucionCuentaDao().obtenerValorFacturadoSoat(con,codigoIngreso,codigoConvenio);
	}
	

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap consultarEsquemasTarifariosInventario(Connection con, String subCuenta) 
	{
		return objetoDao.consultarEsquemasTarifariosInventario(con,subCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap consultarEsquemasTarifariosProcedimientos(Connection con, String subCuenta) 
	{
		return objetoDao.consultarEsquemasTarifariosProcedimientos(con,subCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @param esInventario
	 * @return
	 */
	public boolean eliminarEsquema(Connection con, String codigoEsquema,boolean esInventario) 
	{
		return objetoDao.eliminarEsquema(con,codigoEsquema,esInventario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema) 
	{
		return objetoDao.consultarEsquemaInventarioLLave(con,codigoEsquema);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema) 
	{
		return objetoDao.consultarEsquemaProcedimientoLLave(con,codigoEsquema);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean modificarEsquemasInventario(Connection con, HashMap vo) 
	{
		return objetoDao.modificarEsquemasInventario(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEsquemasInventario(Connection con, HashMap vo) 
	{
		return objetoDao.insertarEsquemasInventario(con,vo);

	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean modificarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return objetoDao.modificarEsquemasProcedimientos(con,vo);
	}

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return objetoDao.insertarEsquemasProcedimientos(con,vo);
	}

	/**
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public boolean limpiarParametrosDistribucion(Connection con,int codigoIngreso) throws IPSException
	{
		return objetoDao.limpiarParametrosDistribucion(con,codigoIngreso);
	}

	/**
	 * @param con
	 * @param subCuenta
	 * @param incluirPaquetizadas
	 * @param agruparPortatiles
	 * @param parametrosBusquedaAvanzadaResponsable
	 * @return
	 */
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaBusquedaAvanzada(Connection con, String subCuenta, boolean incluirPaquetizadas, boolean agruparPortatiles, HashMap parametrosBusquedaAvanzadaResponsable)
	{
		return objetoDao.obtenerSolicitudesSubCuentaBusquedaAvanzada(con, subCuenta, incluirPaquetizadas, agruparPortatiles, parametrosBusquedaAvanzadaResponsable);
	}

	/**
	 * Método implementado para consultar si un Articulo ó Servicio
	 * tiene justificación y si la respuesta es afirmativa se debe de
	 * eliminar los registros de los reponsables de esa justificación
	 * antes de realizar el proceso de Distribución de la Cuenta
	 * @param con
	 * @param numeroSolicitud
	 * @param servArt
	 * @param esArticulo
	 */
	public int eliminarResponsablesJustificacionSolicitud(Connection con, int numeroSolicitud, String servArt, boolean esArticulo) throws IPSException
	{
		return objetoDao.eliminarResponsablesJustificacionSolicitud(con, numeroSolicitud, servArt, esArticulo);
	}
	
	/**
	 * Cargar Servicio Articulos 
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	public HashMap cargarServiciosArticulos (Connection con, int cod_det_cargo){
		HashMap criterios = new HashMap();
		criterios.put("cod_det_cargo", cod_det_cargo);
		return objetoDao.cargarServiciosArticulos(con, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar el listado de ingresos (cerrados, abiertos)
	 * y cuentas (no importa el estado) de un paciente. 
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param paciente
	 * @param institucion
	 * @return Mapa
	 */
	public HashMap cargarListadoIngresos (Connection connection, int paciente,int institucion )
	{
		HashMap criterios = new HashMap ();
		criterios.put("paciente", paciente);//codigo del paciente
		criterios.put("institucion", institucion);//codigo institucion
		return objetoDao.cargarListadoIngresos(connection, criterios);
	}
	
	/**
	 * @param con
	 * @param codigoMontoCobro
	 * @return
	 */
	public DtoMontoCobro consultarMontoCobro(Connection con,Integer codigoMontoCobro){
		return objetoDao.consultarMontoCobro(con, codigoMontoCobro);
	}
	
	/**
	 * @param con
	 * @param codigoMontoCobro
	 * @return
	 */
	public HashMap<String, Object> consultarVeridicacionDerechos(Connection con,int codigoSubCuenta){
		return objetoDao.consultarVerificacionDerechos(con, codigoSubCuenta);
	}
	
	public ICalculoValorCobrarPaciente crearCalculoValorCobrarPaciente(){
		return new CalculoValorCobrarPaciente();
}
	
}