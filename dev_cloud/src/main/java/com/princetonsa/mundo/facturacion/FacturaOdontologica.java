package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoPorcentajeValor;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoAsociosDetalleFactura;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoPaquetizacionDetalleFactura;
import com.princetonsa.dto.facturacion.DtoProfesionalSaludSinValorHonorarios;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.princetonsa.dto.facturacion.DtoSolicitudesResponsableFacturaOdontologica;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.TopesFacturacion;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogProfSaludNoHonorarioMundo;
import com.servinte.axioma.orm.LogProfSaludNoHonorario;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Mundo de la factura odontologica
 * @author axioma
 *
 */
public class FacturaOdontologica 
{
	/**
	 * Método que inicializa el proceso de facturación
	 * Inserta en la tabla 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return true si se inició correctamente el proceso de facturación
	 */
	public static boolean empezarProcesoFacturacion(int idCuenta, String loginUsuario, String idSesion)
	{
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		Factura factura= new Factura();
		boolean retorna= factura.empezarProcesoFacturacion(con, idCuenta, loginUsuario, idSesion);
		if(!retorna)
		{
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * 
	 * Metodo que carga los responsables de la cuenta, pero filtrando unos cargos especificos de la factura automatica desde la atencion de la cita
	 * @param cuenta
	 * @param centroAtencion
	 * @param institucion
	 * @param filtroCargosFacturaAutomatica
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @throws BDException 
	 * @see
	 */
	public static ArrayList<DtoResponsableFacturaOdontologica> cargarResponsables(BigDecimal cuenta, int centroAtencion, int institucion, ArrayList<BigDecimal> filtroCargosFacturaAutomatica) throws BDException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaOdontologicaDao().cargarResponsables(cuenta, centroAtencion, institucion, filtroCargosFacturaAutomatica);
	}
	
	/**
	 * Metodo que carga los responsables de la cuenta 
	 * @param cuenta
	 * @param centroAtencion
	 * @return
	 * @throws BDException 
	 */
	public static ArrayList<DtoResponsableFacturaOdontologica> cargarResponsables(BigDecimal cuenta, int centroAtencion, int institucion) throws BDException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaOdontologicaDao().cargarResponsables(cuenta, centroAtencion, institucion, null);
	}
	
	/**
	 * Metodo que Insertar la factura  odontologica  
	 * @param listaResponsable
	 * @param codigoPaciente
	 * @param cuenta
	 * @param usuario
	 * @param institucion 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DtoFactura> proponerEncabezadoFactura(Connection con, ArrayList<DtoResponsableFacturaOdontologica> listaResponsable, 
																	int codigoPaciente,
																	BigDecimal cuenta,
																	UsuarioBasico usuario, int institucion
																)
	{
		ArrayList<DtoFactura> listaFacturas= new ArrayList<DtoFactura>();
		
		//ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		//RangosConsecutivos rangosConsecutivos= validaciones.obtenerRangosFactura(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		for(DtoResponsableFacturaOdontologica dtoResponsable: listaResponsable)
		{
			DtoFactura dtoFactura = new DtoFactura();
			
			dtoFactura.setAjustesCredito(0);
			dtoFactura.setAjustesDebito(0);
			dtoFactura.setCentroAtencion(new InfoDatosInt(usuario.getCodigoCentroAtencion()));
			dtoFactura.setCentroAtencionDuenio(new InfoDatosInt(UtilidadesManejoPaciente.obtenerCentroAtencionDuenioPaciente(codigoPaciente)));
			dtoFactura.setCodigoPaciente(codigoPaciente);
			dtoFactura.setCodigoResonsableParticular(0);
			dtoFactura.setConvenio(dtoResponsable.getConvenio());
			dtoFactura.setContrato(dtoResponsable.getContrato());
			dtoFactura.getHistoricoEncabezado().setResponsable(dtoResponsable.getNombreSimpleConvenio());
			dtoFactura.getHistoricoEncabezado().setNitResponsable(dtoResponsable.getNitResponsable());

			dtoFactura.setViaIngreso(dtoResponsable.getViaIngreso());

			Vector cuentas= new Vector();
			cuentas.add(cuenta);
			dtoFactura.setCuentas(cuentas);
			
			boolean manejaMultiInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
			int codigoInstitucionUnico=0;
			
			if(manejaMultiInstitucion){
				codigoInstitucionUnico= institucion;
			}
			else{
				codigoInstitucionUnico= usuario.getCodigoInstitucionInt();
			}
			
			dtoFactura.setEmpresaInstitucion(codigoInstitucionUnico);
			dtoFactura.setEntidadSubcontratada(dtoResponsable.getEntidadSubContratada());
			dtoFactura.setPacienteEntidadSubcontratada(dtoResponsable.getPacienteEntidadSubContratada());
			
			dtoFactura.setEsParticular(dtoResponsable.isEsParticular());
			dtoFactura.setEstadoFacturacion(new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionFacturada));
			dtoFactura.setEstadoPaciente(new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionPacientePorCobrar));
			
			dtoFactura.setEstratoSocial(dtoResponsable.getEstratoSocial());
			dtoFactura.setFacturaCerrada(false);
			
			dtoFactura.setFecha("");
			dtoFactura.setHora(UtilidadFecha.getHoraActual());
			dtoFactura.setIdPaciente(codigoPaciente+"");
			dtoFactura.setInstitucion(usuario.getCodigoInstitucionInt());
			dtoFactura.setLoginUsuario(usuario.getLoginUsuario());
			
			dtoFactura.setFechaVigenciaInicialMontoCobro(dtoResponsable.getMontoCobro().getFechaVigenciaInicial());
			dtoFactura.setMontoCobro(dtoResponsable.getMontoCobro().getCodigo());
			//dtoFactura.setPorcentajeMonto(dtoResponsable.getMontoCobro().getPorcentajeMontoCobro());
			dtoFactura.setTipoMonto(new InfoDatosInt(dtoResponsable.getMontoCobro().getTipoMonto()));
			//dtoFactura.setValorMonto(dtoResponsable.getMontoCobro().getValorMontoCobro().doubleValue());
			dtoFactura.setTipoAfiliado(dtoResponsable.getMontoCobro().getTipoAfiliado());
			
			dtoFactura.setFechaVigenciaInicialTopeFacturacion(dtoResponsable.getFechaVigenciaTopeCuenta());
			dtoFactura.setFormatoImpresion(dtoResponsable.getFormatoImpresion());
			
			dtoFactura.setNroComprobante(ConstantesBD.codigoNuncaValidoDouble);
			dtoFactura.setTipoComprobante("");
			
			dtoFactura.setTipoFacturaSistema(true);
			dtoFactura.setPacienteTieneExcepcionNaturaleza(false);
			
			
			/*EL PREFIJO Y LA RESOLUCION SE DEBE TOMAR DEL CENTRO DE ATENCION*/
			//dtoFactura.setPrefijoFactura(dtoResponsable.getPrefijoFactura());
			//dtoFactura.setResolucionDian(dtoResponsable.getResolucion());
			
			
			//dtoFactura.setRangoInicialFactura(rangosConsecutivos.getRangoInicial().doubleValue());
			//dtoFactura.setRangoFinalFactura(rangosConsecutivos.getRangoFinal().doubleValue());
			
			dtoFactura.setSubCuenta(dtoResponsable.getSubCuenta().doubleValue());
			
			dtoFactura.setTipoRegimen(dtoResponsable.getTipoRegimen());
			
			dtoFactura.setTopeFacturacion(TopesFacturacion.cargarTopeParaFacturacion(con, dtoFactura.getTipoRegimenAcronimo(), dtoFactura.getEstratoSocial().getCodigo(), dtoFactura.getTipoMonto().getCodigo(), dtoFactura.getFechaVigenciaInicialTopeFacturacion()));
			
			dtoFactura.setValidarInfoVenezuela(false);
			dtoFactura.setValorFaltaAsignarVenezuela(0);
			dtoFactura.setValorMontoAutorizadoVenezuela(0);
			
			if(dtoResponsable.isPacientePagaAtencionXMontoCobro())
			{	
				dtoFactura.setValorAbonos(dtoResponsable.getValorTotalNetoCargosEstadoCargado().doubleValue());
			}
			else
			{
				if(dtoResponsable.isControlaAnticipos())
				{
					dtoFactura.setValorAnticipos(dtoResponsable.getValorTotalNetoCargosEstadoCargado().doubleValue());
				}
			}
			
			dtoFactura.setValorAFavorConvenio(ConstantesBD.codigoNuncaValidoDouble);
			dtoFactura.setValorAFavorConvenioModificado(false);
			dtoFactura.setValorBrutoPacienteModificadoXAnioCalendario(false);
			dtoFactura.setValorBrutoPacienteModificadoXEvento(false);
			
			dtoFactura.setValorBrutoPac(dtoResponsable.isPacientePagaAtencionXMontoCobro()?dtoResponsable.getValorTotalBrutoCargosEstadoCargado().doubleValue():0);
			dtoFactura.setValorBrutoPacSinModParamConvenio(dtoFactura.getValorBrutoPac());
			
			dtoFactura.setValorNetoPaciente((dtoResponsable.isPacientePagaAtencionXMontoCobro())?dtoResponsable.getValorTotalNetoCargosEstadoCargado().doubleValue():0);
			dtoFactura.setValorTotal(dtoResponsable.getValorTotalNetoCargosEstadoCargado().doubleValue());
			
			dtoFactura.setValorConvenio(!dtoResponsable.isPacientePagaAtencionXMontoCobro()?dtoResponsable.getValorTotalNetoCargosEstadoCargado().doubleValue():0);
			dtoFactura.setValorPagos(0);
			dtoFactura.setValorDescuentoPaciente(ConstantesBD.codigoNuncaValidoDouble);
			
			dtoFactura.setValorCartera(dtoFactura.getValorConvenio());
			dtoFactura.setValorLiquidadoPaciente(dtoFactura.getValorBrutoPacSinModParamConvenio());
			
			//dtoFactura.setConsecutivoFactura() LO SETEAMOS DE ULTIMO EN EL MOMENTO DE GUARDAR
			/*
			 * insertar el detalle de la factura--> proponerDetalleFactura()
			 */
			dtoFactura.setDetallesFactura(proponerDetalleFactura(dtoResponsable, usuario));
			
			dtoFactura.setControlaAnticipos(dtoResponsable.getControlaAnticipos());
			dtoFactura.setPacientePagaAtencionXMontoCobro(dtoResponsable.getPacientePagaAtencionXMontoCobro());
			
			listaFacturas.add(dtoFactura);
			
		}
		
		return listaFacturas;
	}

	/**
	 * Metodo que inserta le detalle de la factura 
	 * @param listaResponsable
	 * @return
	 */
	private static ArrayList<DtoDetalleFactura> proponerDetalleFactura(DtoResponsableFacturaOdontologica dtoResponsable, UsuarioBasico usuario) 
	{
		ArrayList<DtoDetalleFactura> listaDetalle= new ArrayList<DtoDetalleFactura>();
		for(DtoSolicitudesResponsableFacturaOdontologica dtoSolicitudes: dtoResponsable.getListaSolicitudes())
		{
			DtoDetalleFactura dtoDetalle= new DtoDetalleFactura();
			
			dtoDetalle.setAjusteCreditoMedico(0);
			dtoDetalle.setAjusteDebitoMedico(0);
			dtoDetalle.setAjustesCredito(0);
			dtoDetalle.setAjustesDebito(0);
			dtoDetalle.setAsociosDetalleFactura(new ArrayList<DtoAsociosDetalleFactura>());
			dtoDetalle.setCantidadCargo(dtoSolicitudes.getDetalleCargo().getCantidadCargada());
			dtoDetalle.setCodigoArticulo(dtoSolicitudes.getDetalleCargo().getCodigoArticulo());
			dtoDetalle.setCodigoEsquemaTarifario(dtoSolicitudes.getDetalleCargo().getCodigoEsquemaTarifario());
			dtoDetalle.setCodigoEstadoHC(dtoSolicitudes.getEstadoHC().getCodigo());
			//dtoDetalle.setCodigoFactura(codigoFactura)
			dtoDetalle.setCodigoMedico(dtoSolicitudes.getMedicoResponde().getCodigo());
			dtoDetalle.setCodigoServicio(dtoSolicitudes.getDetalleCargo().getCodigoServicio());
			dtoDetalle.setCodigoTipoCargo(dtoDetalle.getCodigoArticulo()>0?ConstantesBD.codigoTipoCargoArticulos:ConstantesBD.codigoTipoCargoServicios);
			dtoDetalle.setCodigoTipoSolicitud(dtoSolicitudes.getDetalleCargo().getCodigoTipoSolicitud());
			dtoDetalle.setDiagnosticoAcronimoTipoCie("");
			//en odontologia no se maneja cx 
			dtoDetalle.setEsCx(false);
			dtoDetalle.setFechaCargo(dtoSolicitudes.getDetalleCargo().getFHU().getFechaModificaFromatoBD());
			dtoDetalle.setNumeroSolicitud(dtoSolicitudes.getDetalleCargo().getNumeroSolicitud());
			dtoDetalle.setPaquetizacionDetalleFactura(new ArrayList<DtoPaquetizacionDetalleFactura>());
			dtoDetalle.setPool(dtoSolicitudes.getPool().getCodigo());
			dtoDetalle.setPorcentajeMedico(dtoSolicitudes.getPorcentajeParticipacionPool());
			dtoDetalle.setSolicitudPago(0);
			dtoDetalle.setValorCargo(dtoSolicitudes.getDetalleCargo().getValorTotalCargado());
			dtoDetalle.setValorConsumoPaquete(0);
			dtoDetalle.setValorDescuentoComercial(dtoSolicitudes.getDetalleCargo().getValorUnitarioDescuento()*dtoSolicitudes.getDetalleCargo().getCantidadCargada());
			dtoDetalle.setValorIva(0);
			dtoDetalle.setValorRecargo(dtoSolicitudes.getDetalleCargo().getValorUnitarioRecargo()*dtoSolicitudes.getDetalleCargo().getCantidadCargada());
			dtoDetalle.setValorTotal(dtoSolicitudes.getDetalleCargo().getValorTotalCargado());
			dtoDetalle.setPrograma(new InfoDatosDouble(dtoSolicitudes.getDetalleCargo().getPrograma(), ""));
			dtoDetalle.setValorDctoOdo(dtoSolicitudes.getDetalleCargo().getValorDescuentoOdontologico());
			dtoDetalle.setValorDctoBono(dtoSolicitudes.getDetalleCargo().getValorDescuentoBono());
			dtoDetalle.setValorDctoProm(dtoSolicitudes.getDetalleCargo().getValorDescuentoPromocionServicio());
			
			dtoDetalle.setDetallePaqueteOdonConvenio(dtoSolicitudes.getDetalleCargo().getDetallePaqueteOdontoConvenio());
			
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(usuario.getCodigoInstitucionInt())))
			{
				InfoPorcentajeValor honorario= CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaNoCx(dtoSolicitudes.getDetalleCargo().getCodigoDetalleCargo());
					
				if(honorario!=null)
				{
					dtoDetalle.setValorPool(honorario.getValor().doubleValue());
					dtoDetalle.setPorcentajePool(new BigDecimal(honorario.getPorcentaje()).doubleValue());
				}	
			}	
			
			try
			{
				if(dtoDetalle.getPorcentajePool()>=0)
				{
					

					Log4JManager.info(" Servicio "+dtoDetalle.getCodigoServicio());
					Log4JManager.info("Porcentaje Honorarios ---->"+dtoDetalle.getPorcentajePool());
					Log4JManager.info("Valor total -Exception  --->"+dtoDetalle.getValorTotal());
					Log4JManager.info("Valor Descuento Comercial "+dtoDetalle.getValorDescuentoComercial());
					Log4JManager.info("Valor Bono"+dtoDetalle.getValorDctoBono());
					Log4JManager.info("Valor Descuento Odo"+dtoDetalle.getValorDctoOdo());
					Log4JManager.info("Valor Promocion"+dtoDetalle.getValorDctoProm());
					Log4JManager.info("Calculo Correcto Valor Total Paciente- -->"+(
															dtoDetalle.getValorTotal()
															-dtoDetalle.getValorDescuentoComercial()
															-dtoDetalle.getValorDctoBono().doubleValue()
															-dtoDetalle.getValorDctoProm().doubleValue()
															-dtoDetalle.getValorDctoOdo().doubleValue()) );	
					
					
					/*
					 * Calcular el honorario profesional 
					 */
					double valorTmpDescuentoComercial=0;
					
					if( dtoDetalle.getValorDescuentoComercial()>0)
					{
						valorTmpDescuentoComercial=dtoDetalle.getValorDescuentoComercial();
					}
					
					dtoDetalle.setValorPool( (((dtoDetalle.getValorTotal()-valorTmpDescuentoComercial -dtoDetalle.getValorDctoBono().doubleValue()-dtoDetalle.getValorDctoProm().doubleValue()-dtoDetalle.getValorDctoOdo().doubleValue())   *dtoDetalle.getPorcentajePool())/100));
					dtoDetalle.setValorMedico( (dtoDetalle.getValorPool()* dtoDetalle.getPorcentajeMedico())/100 );
				}
				else if(dtoDetalle.getValorPool()>=0)
				{
					
					
					Log4JManager.info("***************************************************************************************");
					Log4JManager.info(" Servicio "+dtoDetalle.getCodigoServicio());
					Log4JManager.info("Porcentaje Honorarios ---->"+dtoDetalle.getPorcentajePool());
					Log4JManager.info("Valor total -Exception  --->"+dtoDetalle.getValorTotal());
					Log4JManager.info("Valor Descuento Comercial "+dtoDetalle.getValorDescuentoComercial());
					Log4JManager.info("Valor Bono"+dtoDetalle.getValorDctoBono());
					Log4JManager.info("Valor Descuento Odo"+dtoDetalle.getValorDctoOdo());
					Log4JManager.info("Valor Promocion"+dtoDetalle.getValorDctoProm());
					Log4JManager.info("Calculo Correcto Valor Total Paciente- -->"+(
															dtoDetalle.getValorTotal()
															-dtoDetalle.getValorDescuentoComercial()
															-dtoDetalle.getValorDctoBono().doubleValue()
															-dtoDetalle.getValorDctoProm().doubleValue()
															-dtoDetalle.getValorDctoOdo().doubleValue()) );	
					
					
					
					Log4JManager.info("**************\n pool" );
					Log4JManager.info("Valor porcentaje  --->"+dtoDetalle.getPorcentajePool());
					Log4JManager.info("Valor Pool valor --->"+dtoDetalle.getValorPool());
					
					
					
					
					dtoDetalle.setValorPool(dtoDetalle.getValorPool());
					dtoDetalle.setValorMedico((dtoDetalle.getValorPool()*dtoDetalle.getPorcentajeMedico())/100);
				}
			}	
			catch (Exception e) 
			{
				dtoDetalle.setValorPool(ConstantesBD.codigoNuncaValido);
				dtoDetalle.setValorMedico(ConstantesBD.codigoNuncaValido);
			}
			
			listaDetalle.add(dtoDetalle);
			
		}
		return listaDetalle;
	}

	/**
	 * 
	 * @param con
	 * @param listaFacturas
	 * @return
	 */
	public static boolean insertarFacturas(Connection con,ArrayList<DtoFactura> listaFacturas) 
	{
		boolean retorna=false;
		for(DtoFactura dto: listaFacturas)
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			IHistoricoEncabezadoMundo historicoEncabezadoMundo = FacturacionFabricaMundo.crearHistoricoEncabezadoMundo();
			
			historicoEncabezadoMundo.insertar(dto.getHistoricoEncabezado());
			
			UtilidadTransaccion.getTransaccion().commit();
		
			dto.setCodigo(Factura.insertar(con,	dto).getResultado());
			retorna= dto.getCodigo()>0;
			
			if(!retorna)
			{
				eliminarHistoricoEncabezado (listaFacturas);
				
				break;
			}
		}
		return retorna;
	}

	/**
	 * Método encargado de insertar el log para profesionales de la salud a los
	 * cuales no se les va a generar valor de honorarios.
	 * 
	 * @param con  Conexión a la base de datos
	 * @param dtoProfesional  Dto que contiene la información del log
	 * @return retorna  Cuando es true es porque se pudo almacenar el log, en
	 *         caso contrario es false
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	public static boolean insertarLogProfesionalesSaludNoHonorarios(
			Connection con, DtoProfesionalSaludSinValorHonorarios dtoProfesional) {

		boolean retorna = false;
		LogProfSaludNoHonorario logProfSaludNoHonorario = new LogProfSaludNoHonorario(
				dtoProfesional.getNumeroSolicitud(), 
				dtoProfesional.getCodigoServicio(), 
				dtoProfesional.getFechaLiquidacionHonorarios(), 
				dtoProfesional.getHoraLiquidacionHonorarios(), 
				dtoProfesional.getNombreProfesional());

		UtilidadTransaccion.getTransaccion().begin();

		ILogProfSaludNoHonorarioMundo logProfSaludNoHonorarioMundo = FacturacionFabricaMundo
				.crearLogProfSaludNoHonorarioMundo();

		retorna = logProfSaludNoHonorarioMundo.insertar(logProfSaludNoHonorario);

		UtilidadTransaccion.getTransaccion().commit();

		return retorna;

	}

	/**
	 * Método que elimina los Historicos Encabezados asociados a la
	 * Factura.
	 * 
	 * @param listaFacturas
	 */
	private static void eliminarHistoricoEncabezado(ArrayList<DtoFactura> listaFacturas) {
		
		IHistoricoEncabezadoMundo historicoEncabezadoMundo = FacturacionFabricaMundo.crearHistoricoEncabezadoMundo();
	
		HibernateUtil.beginTransaction();
		
		for(DtoFactura dto: listaFacturas)
		{
			if(dto.getHistoricoEncabezado()!=null && dto.getHistoricoEncabezado().getCodigoPk() > 0){
			
				historicoEncabezadoMundo.eliminar(dto.getHistoricoEncabezado());
			}
		}
		
		HibernateUtil.endTransaction();
	}

	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public static boolean actualizarEstadoCuenta(Connection con, BigDecimal cuenta) 
	{
		//Se debe verificar si el estado de todos los Programas Contratados en el Plan de Tratamiento se encuentran en estado = Terminado
		boolean retorna=true;
		InfoDatosDouble planTratamiento= PlanTratamiento.obtenerPlanTratamientoXCuenta(cuenta);
		int estadoNuevoCuenta=ConstantesBD.codigoEstadoCuentaActiva;
		if(planTratamiento.getCodigo()>0 && planTratamiento.getNombre().equals(ConstantesIntegridadDominio.acronimoTerminado))
		{	
			estadoNuevoCuenta= ConstantesBD.codigoEstadoCuentaFacturada;
		}
		
		Cuenta mundoCuenta= new Cuenta();
		try 
		{
			retorna= mundoCuenta.cambiarEstadoCuentaTransaccional(con, estadoNuevoCuenta, cuenta.intValue(), ConstantesBD.continuarTransaccion)>0;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param listaFacturas
	 * @param ingreso 
	 * @return
	 */
	public static boolean insertarAbonos(Connection con,ArrayList<DtoFactura> listaFacturas, Integer ingreso) 
	{
		boolean retorna=true;
		for(DtoFactura dto: listaFacturas)
		{
			if(dto.isPacientePagaAtencionXMontoCobro())
			{	
				/*
				if(AbonosYDescuentos.insertarMovimientoAbonos(con, dto.getCodigoPaciente(), dto.getCodigo(), ConstantesBD.tipoMovimientoAbonoEntradaReservaAbono, dto.getValorAbonos(), dto.getInstitucion(), ingreso)<1)
				{
					retorna= false;
					break;
				}
				if(AbonosYDescuentos.insertarMovimientoAbonos(con, dto.getCodigoPaciente(), dto.getCodigo(), ConstantesBD.tipoMovimientoAbonoFacturacion, dto.getValorAbonos(), dto.getInstitucion(), ingreso)<1)
				{
					retorna= false;
					break;
				}
				*/
			}	
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param listaFacturas
	 * @return
	 */
	public static boolean insertarAnticipos(Connection con,	ArrayList<DtoFactura> listaFacturas) 
	{
		boolean retorna=true;
		for(DtoFactura dto: listaFacturas)
		{
			if(!dto.isPacientePagaAtencionXMontoCobro() && dto.isControlaAnticipos())
			{	
				if(!Contrato.modificarValorAnticipoReservadoPresupuesto(con, UtilidadesFacturacion.obtenerContratoSubCuenta(con, dto.getSubCuenta()+""), new BigDecimal(dto.getValorAnticipos()*-1)))
				{	
					retorna= false;
					break;
				}
				
				if(!Contrato.modificarValorAnticipoUtilizadoFactura(con, UtilidadesFacturacion.obtenerContratoSubCuenta(con, dto.getSubCuenta()+""), new BigDecimal(dto.getValorAnticipos())))
				{	
					retorna= false;
					break;
				}
			}	
		}
		return retorna;
	}

	public static boolean actualizarAcumuladoContratos(Connection con,ArrayList<DtoFactura> listaFacturas) 
	{
		boolean retorna=true;
		for(DtoFactura dto: listaFacturas)
		{
			if(Contrato.actualizarValorAcumulado(con, dto.getContrato().getCodigo(), dto.getValorTotal())>0)
				retorna=true;
			else
				retorna=false;
		}
		return retorna;
	}
	
	
}
