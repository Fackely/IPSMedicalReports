/*
 * @(#)Cargos.java
 *
 * Copyright Princeton S.A. &copy;&reg; . Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.6
 *
 */
package com.princetonsa.mundo.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoCobertura;
import util.facturacion.InfoErroresCargo;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifa;
import util.facturacion.InfoTarifaVigente;
import util.facturacion.InfoTarifaYExcepcion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.salas.UtilidadesSalas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosDao;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.cargos.DtoDetalleCargoArticuloConsumo;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Clase para manejar los Cargos
 * @author wilson
 */
public class Cargos 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Cargos.class);
	
	/**
	 * El DAO usado por el objeto <code>Cargos</code> para acceder 
	 * a la fuente de datos. 
	 */
	private CargosDao cargosDao=null;
	
	/**
	 * 
	 */
	private InfoErroresCargo infoErroresCargo;
	
	/**
	 * 
	 */
	private DtoDetalleCargo dtoDetalleCargo;
	
	/**
	 * Variable que indica si se debe generar el cargo para pyp
	 */
	private boolean pyp;
	
	/**
	 * Almacena informacion de la cobertura de los articulos y servicios
	 */
	private InfoResponsableCobertura infoResponsableCoberturaGeneral;
	
	/**
	 * Crea un nuevo objeto <code>Egreso</code>.
	 */
	public Cargos() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaciï¿½n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			cargosDao = myFactory.getCargosDao();
			wasInited = (cargosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * * metodo que intenta generar el cargo de servicios
	 * @param con
	 * @param dejarPendiente
	 * @param esCita
	 * @param numeroSolicitud
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoTipoComplejidad
	 * @param codigoInstitucion
	 * @param observaciones
	 * @param loginUsuario
	 * @param codigoSubcuenta
	 * @param codigoSolicitudSubcuenta
	 * @param cubierto
	 * @param codigoPadrePaquetes
	 * @param paquetizado
	 * @param cantidadCargada
	 * @param requiereAutorizacion
	 * @param tipoDistribucion
	 * @param codigoEvolucionOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param codigoConvenioOPCIONAL
	 * @param codigoEsquemaTarifarioOPCIONAL
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param numeroAutorizacionOPCIONAL
	 * @param porcentajeDescuentoOPCIONAL
	 * @param valorDescuentoOPCIONAL
	 * @param esRegistroNuevo
	 * @param insertarEnBD
	 * @return
	 */
	public 	InfoErroresCargo generarCargoServicio (	Connection con,
													boolean dejarPendiente,
													boolean esCita,		
													
													int numeroSolicitud, 
													int codigoViaIngreso, 
													int codigoContrato,
													int codigoTipoComplejidad,
													int codigoInstitucion,
													String observaciones, 
													String loginUsuario,
													
													double codigoSubcuenta,
													double codigoSolicitudSubcuenta,
													
													String cubierto,
													double codigoPadrePaquetes,
													String paquetizado,
													int cantidadCargada,
													String requiereAutorizacion,
													String tipoDistribucion,  ///////////////********************
													
													int codigoEvolucionOPCIONAL, 
													int codigoServicioOPCIONAL,
													double valorTarifaOPCIONAL,
													//double valorCargadoOPCIONAL,
													int codigoConvenioOPCIONAL,
													int codigoEsquemaTarifarioOPCIONAL,
													int codigoTipoSolicitudOPCIONAL,
													int codigoCentroCostoSolicitanteOPCIONAL,
													String numeroAutorizacionOPCIONAL,
													double porcentajeDescuentoOPCIONAL,
													double valorUnitarioDescuentoOPCIONAL,
													
													boolean esRegistroNuevo,     //////****************************
													boolean insertarEnBD,
													String esPortatil,
													boolean excento,
													String fechaCalculoVigenciaOPCIONAL,
													
													double porcentajeDctoPromocionServicio, 
													BigDecimal valorDescuentoPromocionServicio, 
													double porcentajeHonorarioPromocionServicio, 
													BigDecimal valorHonorarioPromocionServicio,
													
													double programa,
													double porcentajeDctoBono,
													BigDecimal valorDescuentoBono, 
													double porcentajeDctoOdontologico, 
													BigDecimal valorDescuentoOdontologico,
													
													int detallePaqueteOdonConvenio) throws IPSException{
		
		//Se hace el llamado al mï¿½todo sincronizado del proceso del detalle Cargo, esta implementaciï¿½n se realiza
		//porque por temas de concurrencia se estan presentando deadLock en la tabla det_cargos
		//se aclara que esta implementaciï¿½n no es una soluciï¿½n definitiva hasta tanto no se centralice totalmente 
		//el acceso a esta tabla, dicha centralizaciï¿½n no se hace por el momento por cuestiones de tiempo y recursos 
		CargosSingleton singleton = CargosSingleton.getInstance(numeroSolicitud);
		return singleton.procesarDetalleCargoServicioArticulo(con, dejarPendiente, esCita, numeroSolicitud, 
							codigoViaIngreso, codigoContrato, codigoTipoComplejidad, codigoInstitucion, observaciones, 
							loginUsuario, codigoSubcuenta, codigoSolicitudSubcuenta, cubierto, codigoPadrePaquetes,
							paquetizado, cantidadCargada, requiereAutorizacion, tipoDistribucion, codigoEvolucionOPCIONAL, 
							codigoServicioOPCIONAL, valorTarifaOPCIONAL, codigoConvenioOPCIONAL, codigoEsquemaTarifarioOPCIONAL, 
							codigoTipoSolicitudOPCIONAL, codigoCentroCostoSolicitanteOPCIONAL, numeroAutorizacionOPCIONAL, 
							porcentajeDescuentoOPCIONAL, valorUnitarioDescuentoOPCIONAL, esRegistroNuevo, insertarEnBD,
							esPortatil, excento, fechaCalculoVigenciaOPCIONAL, porcentajeDctoPromocionServicio, 
							valorDescuentoPromocionServicio, porcentajeHonorarioPromocionServicio, valorHonorarioPromocionServicio,
							programa, porcentajeDctoBono, valorDescuentoBono, porcentajeDctoOdontologico, valorDescuentoOdontologico,
							detallePaqueteOdonConvenio, this);
	}
	
	/**
	 * metodo que genera la solicitud de subcuenta y el cargo de las solicitudes de cx
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCuenta
	 * @param subCuenta
	 * @param codigoServicio
	 * @param cubierto
	 * @param codigoServicioCx
	 * @param codigoTipoAsocio
	 * @param codigoEsquemaTarifario
	 * @param usuario
	 * @param valorTarifa
	 * @param requiereAutorizacion
	 * @param aplicarMetodoAjuste 
	 * @param codigoInstitucion 
	 * @param esCirugiaPorConsumo 
	 * @return
	 */
	public boolean generarCargoSolicitudesCxYSolSubCuenta(	Connection con, int numeroSolicitud, String codigoCuenta, String subCuenta, String codigoServicio, String cubierto, String codigoServicioCx, int codigoTipoAsocio, int codigoEsquemaTarifario , UsuarioBasico usuario, double valorTarifa, String requiereAutorizacion , String loginUsuario, int detCxHonorarios, int detAsocioCxSalasMat, int codigoInstitucion, boolean aplicarMetodoAjuste, boolean esCirugiaPorConsumo, String tipoServicioAsocio,double desPorcentaje, double valorDescuento) throws IPSException 
	{
		try {
			int consecutivoServicioCx= SolicitudesCx.obtenerConsecutivoServicioCx(con, Integer.parseInt(codigoServicioCx), numeroSolicitud+"");
			double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(	con,
	    																		numeroSolicitud,
	    																		Integer.parseInt(codigoCuenta), 
	    																		subCuenta,
	    																		codigoServicio, 
	    																		"" /*codigoArticulo*/,
	    																		1 /*cantidadServicio*/,
	    																		cubierto,
	    																		ConstantesBD.codigoTipoSolicitudCirugia,
	    																		codigoServicioCx,
	    																		codigoTipoAsocio,
	    																		ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad,
	    																		loginUsuario,
	    																		detCxHonorarios,
	    																		detAsocioCxSalasMat
	    																		);
		    
	    	if(solicitudSubCuenta<1)
	    	{
	    		return false;
	    	}
	    	
	    	DtoSubCuentas dtoSubCuenta=UtilidadesHistoriaClinica.obtenerResponsable(con, Integer.parseInt(subCuenta));
	    	
	    	//*********************VALIDACION DEL Mï¿½TODO DE AJUSTE****************************************
	    	if(aplicarMetodoAjuste&&valorTarifa>0)
	    	{
		    	String metodoAjuste = obtenerMetodoAjuste(con, dtoSubCuenta.getConvenio().getCodigo(), codigoEsquemaTarifario, codigoInstitucion, true);
		    	if(!UtilidadTexto.isEmpty(metodoAjuste))
		    	{
		    		valorTarifa = UtilidadValidacion.aproximarMetodoAjuste(metodoAjuste, valorTarifa);
		    	}
		    }
	    	//*************************************************************************************************
	    	
			this.dtoDetalleCargo= 		insertarDetalleCargo(	con, 
																Double.parseDouble(subCuenta), 
																dtoSubCuenta.getConvenio().getCodigo(), 
																codigoEsquemaTarifario, 
																1 /*cantidadCargada*/, 
																valorTarifa, 
																valorTarifa /*valorUnitarioCargado*/, 
																valorTarifa /*valorTotalCargado*/, 
																ConstantesBD.codigoNuncaValidoDouble /*porcentajecargado PERTENECE A LA DISTRIBUCION*/, 
																ConstantesBD.codigoNuncaValidoDouble, 
																ConstantesBD.codigoNuncaValidoDouble /*valorUnitarioRecargo*/, 
																desPorcentaje  /*porcentajeDescuento*/, 
																valorDescuento /*valorDescuento*/, 
																ConstantesBD.codigoNuncaValidoDouble /*iva*/,
																requiereAutorizacion,
																"" /*numeroAutorizacion*/,
																ConstantesBD.codigoEstadoFCargada, 
																cubierto, 
																ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad/*tipoDistribucion*/, 
																numeroSolicitud, 
																Integer.parseInt(codigoServicio), 
																ConstantesBD.codigoNuncaValido /*codigoArticulo*/, 
																Integer.parseInt(codigoServicioCx) /*codigoServicioCx*/, 
																codigoTipoAsocio /*codigoTipoAsocio*/, 
																ConstantesBD.acronimoNo /*facturado*/, 
																ConstantesBD.codigoTipoSolicitudCirugia/*codigoTipoSolicitud*/, 
																ConstantesBD.acronimoNo /*paquetizado*/, 
																ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
																solicitudSubCuenta, 
																"" /*observaciones*/, 
																usuario.getLoginUsuario(), 
																new InfoErroresCargo(),
																dtoSubCuenta.getContrato(),
																detCxHonorarios,
																detAsocioCxSalasMat,
																"" /*esPortatil*/,
																"" /*dejarExcento*/,
																new ArrayList<Integer>(),
																new ArrayList<Integer>(),
																0 /*porcentajeDctoPromocionServicio*/, 
																BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																0 /*porcentajeHonorarioPromocionServicio*/, 
																BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																
																0/*programa*/,
																0/*porcentajeDctoBono*/,
																BigDecimal.ZERO/*valorDescuentoBono*/, 
																0/*porcentajeDctoOdontologico*/, 
																BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																0/*detallePaqueteOdonConvenio*/);
	
			if(this.dtoDetalleCargo.getCodigoDetalleCargo()<1)
	    	{
	    		return false;
	    	}
			
			if(consecutivoServicioCx==1)
			{
				if(!SolicitudesCx.actualizarSubCuentaCx(con, dtoSubCuenta.getSubCuentaDouble(), numeroSolicitud+""))
				{
					return false;
				}
			}
			
			//insertamos los consumos de articulos
			if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioMaterialesCirugia+"") && esCirugiaPorConsumo && valorTarifa>0)
			{
				int codigoServicioCxParametroConsumo=(UtilidadesSalas.esSolicitudCirugiaPorActo(con, numeroSolicitud))?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(codigoServicioCx);
				HashMap<Object, Object> mapaConsumos= UtilidadesSalas.obtenerArticulosConsumoCx(con,numeroSolicitud+"", codigoServicioCxParametroConsumo);
				for(int w=0; w<Utilidades.convertirAEntero(mapaConsumos.get("numRegistros")+""); w++)
				{
					DtoDetalleCargoArticuloConsumo dto= new DtoDetalleCargoArticuloConsumo();
					dto.setCantidad(Utilidades.convertirAEntero(mapaConsumos.get("cantidad_consumo_total_"+w)+""));
					dto.setCodigoArticulo(Utilidades.convertirAEntero(mapaConsumos.get("articulo_"+w)+""));
					dto.setDetalleCargo(this.dtoDetalleCargo.getCodigoDetalleCargo());
					dto.setPorcentaje(ConstantesBD.codigoNuncaValidoDoubleNegativo);
					dto.setValorTotal(Utilidades.convertirADouble(mapaConsumos.get("valor_total_"+w)+""));
					dto.setValorUnitario(Utilidades.convertirADouble(mapaConsumos.get("valor_unitario_"+w)+""));
					if(insertarDetalleCargosArtConsumos(con, dto, loginUsuario)<=0)
					{
						return false;
					}
				}
			}
			
			return true;
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
	 * metodo para evaluar los posibles errores de cargo para una cuenta - servicio, este metodo no realiza ningun insert solo 
	 * carga los errores en el objeto InfoErroresCargo, tampoco requiere tener la solicitud insertada
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param codigoCuenta
	 * @param codigoServicio
	 * @param validarComplejidad (define si se debe valdiar el tipo de complejidad)
	 * @return
	 */
	public InfoErroresCargo obtenerPosiblesErroresGeneracionCargo(	Connection con, 
																	UsuarioBasico usuario, 
																	PersonaBasica paciente, 
																	int codigoCuenta, 
																	int codigoServicio,
																	boolean validarComplejidad,
																	String fechaCalculoVigenciaOPCIONAL,
																	int centroAtencion
																) throws IPSException
	{
		InfoErroresCargo erroresCargo= new InfoErroresCargo();
		int codigoTipoComplejidad=  Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"");
		//obtenemos el responsable 
		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
		
		
		//obtenemos el tipo paciente asociado a la cuenta
		
		String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, codigoCuenta+"").getAcronimo();
		
		
//		CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
		String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;

		
		infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", codigoViaIngreso,tipoPaciente, codigoServicio, usuario.getCodigoInstitucionInt(), this.pyp, "" /*subCuentaCoberturaOPCIONAL*/);
		
		//primero se asignan los errores por tipo complejidad
		if(validarComplejidad)
			erroresCargo= asignarErroresTipoComplejidad(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo(), codigoTipoComplejidad, erroresCargo);
		
		//segundo asignamos los errores por servicio
		erroresCargo= asignarErroresServicio(codigoServicio, erroresCargo);
		
		//tercero asignamos los errores del esquema tarifario
		int codigoEsquemaTarifario= obtenerCodigoEsquemaTarifarioContrato(con, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicio, true,fechaCalculoVigencia, centroAtencion);
		erroresCargo=asignarErroresEsquemaTarifario(codigoEsquemaTarifario, erroresCargo);
		
		//tercero verificamos q la tarifa base sea mayor a cero
		int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
		
		InfoTarifaVigente infoTarifaVigente= obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicio, codigoEsquemaTarifario, fechaCalculoVigencia);
		if(!infoTarifaVigente.isExiste())
		{
			logger.info("error tarifa base inexistente");
			//2.2.4.1 no se encontrï¿½ tarifa
			erroresCargo=asignarErroresTarifas_ISS_Soat(codigoTipoTarifario, erroresCargo, codigoServicio);
		}
		return erroresCargo;
	}
	
	
	
	
	/**
	 * Metodo centralizado que evalua la cobertura - inserta la solicitud de subcuenta y finalmente GENERA CARGO
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param dejarPendiente
	 * @param numeroSolicitud
	 * @param codigoCuentaOPCIONAL
	 * @param codigoServicio
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	public boolean generarSolicitudSubCuentaCargoServicioNoCruentoEvaluandoCobertura(
																					Connection con, 
																					UsuarioBasico usuario, 
																					PersonaBasica paciente, 
																					boolean dejarPendiente, 
																					int numeroSolicitud,  
																					int codigoCuentaOPCIONAL,  
																					int codigoServicio,  
																					/*String numeroAutorizacionOPCIONAL,*/
																					String fechaCalculoVigenciaOPCIONAL) 
	{
		
		try
	    {
			
			//LA CUENTA SE PUEDE ENVIAR X PARAMETRO PARA EL CASO DE CARGOS DIRECTOS
			if(codigoCuentaOPCIONAL<1)
				codigoCuentaOPCIONAL= paciente.getCodigoCuenta();
			if(codigoCuentaOPCIONAL<1)
				codigoCuentaOPCIONAL= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
	    	int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuentaOPCIONAL+"");
	    	
	    	int codigoTipoSolicitudOPCIONAL= ConstantesBD.codigoTipoSolicitudCirugia;
	    	boolean esCita=false;
	    	
	    	// obtenemos el tipo paciente asociado a la solicitud
	    	
	    	String tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
	    	
	    	logger.info("Tipo Paciente>>>"+tipoPaciente);
	    	
	    	
	    	//1.OBTENEMOS EL CODIGO DEL SERVICIO, 
	    	
	    	if(codigoServicio<1)
	    	{
	    		//se genera cargo pendiente responsable #1
	    		infoResponsableCobertura= Cobertura.validacionCoberturaNoAsignadoServicio(con, paciente.getCodigoIngreso()+"",this.pyp);
	    		dejarPendiente=true;
	    	}
	    	else
	    	{	
	    		//1. EVALUAMOS LA COBERTURA
	    		infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", codigoViaIngreso, tipoPaciente, codigoServicio, usuario.getCodigoInstitucionInt(),this.pyp, "" /*subCuentaCoberturaOPCIONAL*/);
	    	}
	    	
	    	
	    	//SE VERIFICA QUE EL SERVICIO TENGA PARAMETRIZADO TARIFA
	    	/*Se consulta el tarifario oficial segun el esquema tarifario del convenio*/
	    	int codigoTarifarioOficial = EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicio,fechaCalculoVigenciaOPCIONAL, obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud) ));
	    	/*Se consulta la tarifa del servicio*/
	    	InfoTarifaVigente infoTarifaVigenteServicio = obtenerTarifaBaseServicio(con, codigoTarifarioOficial, codigoServicio, infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicio,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)), fechaCalculoVigenciaOPCIONAL);
	    	
	    	//SE verifica si el servicio tiene definido tarifa
	    	if(infoTarifaVigenteServicio.isExiste())
	    	{
	    		/*Se toma el codigo del tipo de liquidacion, el valor de la tarifa y el indicador de Liquidar Asocios*/
	    		int codigoTipoLiquidacion = infoTarifaVigenteServicio.getTipoLiquidacion();
	    		double valorTarifaOPCIONAL = infoTarifaVigenteServicio.getValorTarifa();
	    		boolean liquidarAsocio = infoTarifaVigenteServicio.isLiquidarAsocios();
	    		
	    		
	    		logger.info("EVALUA VALIDACIONES GENERAR CARGO CX NO CRUENTO");
	    		
	    		//Validaciones para los servicios 
	    		if((codigoTipoLiquidacion == ConstantesBD.codigoTipoLiquidacionSoatValor 
	    				|| codigoTipoLiquidacion == ConstantesBD.codigoTipoLiquidacionSoatUnidades)
	    					&& !liquidarAsocio)
	    		{
	    			 	
	    			logger.info("PASO VALIDACIONES GENERAR CARGO CX NO CRUENTO");
	    			
			    	double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(	con,
																		    			numeroSolicitud,
																		    			codigoCuentaOPCIONAL, 
																		    			infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),
																		    			codigoServicio+"",
																		    			"",
																		    			1/*cantidadServicio*/, 
																		    			infoResponsableCobertura.getInfoCobertura().getIncluidoStr(),
																		    			codigoTipoSolicitudOPCIONAL, 
																		    			"" /*codigoServicioCx*/, 
																		    			ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, 
																		    			ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, 
																		    			usuario.getLoginUsuario(),
																		    			ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																		    			ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/
																		    			);
				    if(solicitudSubCuenta<1)
				    {
				    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
				        return false;
				    }
				    
				    this.setInfoErroresCargo(	generarCargoServicio(		con, 
																			dejarPendiente/*dejarPendiente*/, 
																			esCita /*esCita*/, 
																			numeroSolicitud, 
																			codigoViaIngreso/*codigoViaIngreso*/, 
																			infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
																			Cuenta.obtenerTipoComplejidad(con, codigoCuentaOPCIONAL+"") /*codigoTipoComplejidad*/, 
																			usuario.getCodigoInstitucionInt(), 
																			""/*observaciones*/, 
																			usuario.getLoginUsuario(), 
																			infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble() /*codigoSubcuenta*/, 
																			solicitudSubCuenta /*codigoSolicitudSubcuenta*/, 
																			infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
																			ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																			ConstantesBD.acronimoNo/*paquetizado*/,
																			1 /*cantidadCargada*/, 
																			infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																			ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/, 
																			codigoServicio /*codigoServicioOPCIONAL*/, 
																			valorTarifaOPCIONAL/*valorTarifaOpcional*/, 
																			infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																			infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicio,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud))/*codigoEsquemaTarifarioServicioOPCIONAL*/,
																			codigoTipoSolicitudOPCIONAL /*codigoTipoSolicitudOPCIONAL*/,
																			ConstantesBD.codigoNuncaValido/*codigoCentroCostoSolicitanteOPCIONAL*/,
																			"",/* numeroAutorizacionOPCIONAL  -- numeroAutorizacionOPCIONAL*/
																			-1 /*porcentajeDescuentoOPCIONAL*/,
																			-1 /*double valorUnitarioDescuentoOPCIONAL*/,
																			true /*esRegistroNuevo*/,
																			true /*insertarEnBD*/,
																			"" /*esPortatil*/,
																			false,
																			fechaCalculoVigenciaOPCIONAL,
																			0 /*porcentajeDctoPromocionServicio*/, 
																			BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																			0 /*porcentajeHonorarioPromocionServicio*/, 
																			BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																			
																			0/*programa*/,
																			0/*porcentajeDctoBono*/,
																			BigDecimal.ZERO/*valorDescuentoBono*/, 
																			0/*porcentajeDctoOdontologico*/, 
																			BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																			0/*detallePaqueteOdonConvenio*/
																		));
			    }
	    	}
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
	        e.printStackTrace();
			return false;
	    }
	    
	    return true;
	}
	
	/**
	 * Metodo centralizado que evalua la cobertura - inserta la solicitud de subcuenta y finalmente GENERA CARGO
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param dejarPendiente
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroSolicitanteOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param cantidadServicioOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	public boolean generarCargoOdontologia(	Connection con, 
											UsuarioBasico usuario, 
											PersonaBasica paciente, 
											boolean dejarPendiente, 
											int numeroSolicitud, 
											int codigoTipoSolicitudOPCIONAL, 
											int codigoCuentaOPCIONAL, 
											int codigoCentroCostoSolicitanteOPCIONAL, 
											int codigoServicioOPCIONAL, 
											int cantidadServicioOPCIONAL, 
											BigDecimal valorTarifaOPCIONAL, 
											String esPortatil, 
											boolean excento, 
											String fechaCalculoVigenciaOPCIONAL, 
											String subCuentaCoberturaOPCIONAL,
											double porcentajeDctoPromocionServicio, 
											BigDecimal valorDescuentoPromocionServicio, 
											double porcentajeHonorarioPromocionServicio, 
											BigDecimal valorHonorarioPromocionServicio,
											double programa,
											double porcentajeDctoBono,
											BigDecimal valorDescuentoBono, 
											double porcentajeDctoOdontologico, 
											BigDecimal valorDescuentoOdontologico,
											int detallePaqueteOdonConvenio,
											int esquemaTarifario) 
	{
		logger.info("\n\n\n*********************LLEGA A EVALUAR COBERTURA - GENERAR SOLICItud SUBCUENTA Y GENERAR CARGO DEJARPENDIENTE-->"+dejarPendiente+"************************");
		logger.info("\n\n\n ESPORTATIL----------------->"+esPortatil+"   subcuenta opcional (control postopersatorio)-->"+subCuentaCoberturaOPCIONAL);
		String tipoPaciente;
		try
	    {
			logger.info("codigocuentaopcional=> "+codigoCuentaOPCIONAL);
			//LA CUENTA SE PUEDE ENVIAR X PARAMETRO PARA EL CASO DE CARGOS DIRECTOS
			if(codigoCuentaOPCIONAL<1)
				codigoCuentaOPCIONAL= paciente.getCodigoCuenta();
			if(codigoCuentaOPCIONAL<1)
				codigoCuentaOPCIONAL= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
	    	int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuentaOPCIONAL+"");
	    	
	    	if(codigoTipoSolicitudOPCIONAL<=0)
	    	{	
	    		codigoTipoSolicitudOPCIONAL= asignarTipoSolicitud(numeroSolicitud, codigoTipoSolicitudOPCIONAL, con);
	    	}	
	    	boolean esCita=false;
			if(codigoTipoSolicitudOPCIONAL==ConstantesBD.codigoTipoSolicitudCita)
				esCita=true;
	    	
	    	//SE OBTIENE EL TIPO DE PACIENTE DE LA CUENTA.
			tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, codigoCuentaOPCIONAL+"").getAcronimo();
			
			logger.info("Tipo Paciente asociado a la cuenta ->"+tipoPaciente);
			
	    	//1.OBTENEMOS EL CODIGO DEL SERVICIO, 
	    	//EN CASO DE NO EXISTIR SE LO ASIGNAMOS AL RESPONSABLE DE PRIORIDAD #1
	    	//CONFIRMADO MARGARITA (2007-07-04 14:32) Y SE DEJA PENDIENTE
	    	if(codigoServicioOPCIONAL<1)
	    		codigoServicioOPCIONAL= obtenerCodigoServicio(con, codigoServicioOPCIONAL, codigoTipoSolicitudOPCIONAL, numeroSolicitud, ConstantesBD.codigoNuncaValido, esPortatil);
	    	
	    	logger.info("codigoIngreso=> "+paciente.getCodigoIngreso());
	    	logger.info("codigoServicioOpcioanl=> "+codigoServicioOPCIONAL);
	    	if(codigoServicioOPCIONAL<1)
	    	{
	    		logger.info("no existe servicio!!!!!!!");
	    		//se genera cargo pendiente responsable #1
	    		infoResponsableCobertura= Cobertura.validacionCoberturaNoAsignadoServicio(con, paciente.getCodigoIngreso()+"",this.pyp);
	    		dejarPendiente=true;
	    	}
	    	else
	    	{
	    		logger.info("existe servicio!!!!!!!");
	    		//1. EVALUAMOS LA COBERTURA
	    		infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", codigoViaIngreso, tipoPaciente, codigoServicioOPCIONAL, usuario.getCodigoInstitucionInt(),this.pyp, subCuentaCoberturaOPCIONAL /*subCuentaCoberturaOPCIONAL*/);
	    	}
	    	
	    	logger.info("va ha insertar la solicitud de subcuenta!!!!!!!!");
	    	double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(con,numeroSolicitud,codigoCuentaOPCIONAL, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),codigoServicioOPCIONAL+"","", cantidadServicioOPCIONAL/*cantidadServicio*/, infoResponsableCobertura.getInfoCobertura().getIncluidoStr(),codigoTipoSolicitudOPCIONAL, "" /*codigoServicioCx*/, ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
		    if(solicitudSubCuenta<1)
		    {
		    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
		        return false;
		    }
		    logger.info("inserto la solicitud de sub_cuenta->"+solicitudSubCuenta+" dejarPendiente-->"+dejarPendiente);
		    
		    if(esquemaTarifario<=0)
		    {
		    	esquemaTarifario= infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicioOPCIONAL,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud))/*codigoEsquemaTarifarioServicioOPCIONAL*/;
		    }
		    
		    this.setInfoErroresCargo(	generarCargoServicio(		con, 
																	dejarPendiente/*dejarPendiente*/, 
																	esCita /*esCita*/, 
																	numeroSolicitud, 
																	codigoViaIngreso/*codigoViaIngreso*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuentaOPCIONAL+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt(), 
																	""/*observaciones*/, 
																	usuario.getLoginUsuario(), 
																	infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble() /*codigoSubcuenta*/, 
																	solicitudSubCuenta /*codigoSolicitudSubcuenta*/, 
																	infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	ConstantesBD.acronimoNo/*paquetizado*/,
																	cantidadServicioOPCIONAL /*cantidadCargada*/, 
																	infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
																	ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/, 
																	codigoServicioOPCIONAL /*codigoServicioOPCIONAL*/, 
																	valorTarifaOPCIONAL.doubleValue()/*valorTarifaOpcional*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																	esquemaTarifario,
																	codigoTipoSolicitudOPCIONAL /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido/*codigoCentroCostoSolicitanteOPCIONAL*/,
																	"",/* numeroAutorizacionOPCIONAL -- numeroAutorizacionOPCIONAL*/
																	-1 /*porcentajeDescuentoOPCIONAL*/,
																	-1 /*double valorUnitarioDescuentoOPCIONAL*/,
																	true /*esRegistroNuevo*/,
																	true /*insertarEnBD*/,
																	esPortatil,
																	excento,
																	fechaCalculoVigenciaOPCIONAL,
																	porcentajeDctoPromocionServicio, 
																	valorDescuentoPromocionServicio, 
																	porcentajeHonorarioPromocionServicio, 
																	valorHonorarioPromocionServicio,
																	programa,
																	porcentajeDctoBono,
																	valorDescuentoBono, 
																	porcentajeDctoOdontologico, 
																	valorDescuentoOdontologico,
																	detallePaqueteOdonConvenio
																));
		    
		    logger.info("inserto cargo!!!");
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
	        e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	/**
	 * Metodo centralizado que evalua la cobertura - inserta la solicitud de subcuenta y finalmente GENERA CARGO
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param dejarPendiente
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroSolicitanteOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param cantidadServicioOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	public boolean generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	Connection con, 
																				UsuarioBasico usuario, 
																				PersonaBasica paciente, 
																				boolean dejarPendiente, 
																				int numeroSolicitud, 
																				int codigoTipoSolicitudOPCIONAL, 
																				int codigoCuentaOPCIONAL, 
																				int codigoCentroCostoSolicitanteOPCIONAL, 
																				int codigoServicioOPCIONAL, 
																				int cantidadServicioOPCIONAL, 
																				double valorTarifaOPCIONAL, 
																				int codigoEvolucionOPCIONAL, 
																				String esPortatil, 
																				boolean excento, 
																				String fechaCalculoVigenciaOPCIONAL, 
																				String subCuentaCoberturaOPCIONAL)
	{
		return generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																			usuario, 
																			paciente, 
																			dejarPendiente, 
																			numeroSolicitud, 
																			codigoTipoSolicitudOPCIONAL, 
																			codigoCuentaOPCIONAL, 
																			codigoCentroCostoSolicitanteOPCIONAL, 
																			codigoServicioOPCIONAL, 
																			cantidadServicioOPCIONAL, 
																			valorTarifaOPCIONAL, 
																			codigoEvolucionOPCIONAL, 
																			esPortatil, 
																			excento, 
																			fechaCalculoVigenciaOPCIONAL, 
																			subCuentaCoberturaOPCIONAL,
																			0/*porcentajeDescuentoComercialOPCIONAL,*/,
																			BigDecimal.ZERO /*valorUnitarioDescuentoComercialOPCIONAL*/,
																			0 /*porcentajeDctoPromocionServicio*/, 
																			BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																			0 /*porcentajeHonorarioPromocionServicio*/, 
																			BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																			0/*programa*/,
																			0/*porcentajeDctoBono*/,
																			BigDecimal.ZERO/*valorDescuentoBono*/, 
																			0/*porcentajeDctoOdontologico*/, 
																			BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																			0 /*detallePaqueteOdonConvenio*/,
																			0 /*esquematarifarioopcional*/);
																			
	}
	
	
	/**
	 * Metodo centralizado que evalua la cobertura - inserta la solicitud de subcuenta y finalmente GENERA CARGO
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param dejarPendiente
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroSolicitanteOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param cantidadServicioOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	public boolean generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	Connection con, 
																				UsuarioBasico usuario, 
																				PersonaBasica paciente, 
																				boolean dejarPendiente, 
																				int numeroSolicitud, 
																				int codigoTipoSolicitudOPCIONAL, 
																				int codigoCuentaOPCIONAL, 
																				int codigoCentroCostoSolicitanteOPCIONAL, 
																				int codigoServicioOPCIONAL, 
																				int cantidadServicioOPCIONAL, 
																				double valorTarifaOPCIONAL, 
																				int codigoEvolucionOPCIONAL, 
																				String esPortatil, 
																				boolean excento, 
																				String fechaCalculoVigenciaOPCIONAL, 
																				String subCuentaCoberturaOPCIONAL,
																				double porcentajeDescuentoComercialOPCIONAL,
																				BigDecimal valorUnitarioDescuentoComercialOPCIONAL,
																				double porcentajeDctoPromocionServicio, 
																				BigDecimal valorDescuentoPromocionServicio, 
																				double porcentajeHonorarioPromocionServicio, 
																				BigDecimal valorHonorarioPromocionServicio,
																				double programa,
																				double porcentajeDctoBono,
																				BigDecimal valorDescuentoBono, 
																				double porcentajeDctoOdontologico, 
																				BigDecimal valorDescuentoOdontologico,
																				int detallePaqueteOdonConvenio,
																				int esquemaTarifarioOPCIONAL) 
	{
		logger.info("\n\n\n*********************LLEGA A EVALUAR COBERTURA - GENERAR SOLICItud SUBCUENTA Y GENERAR CARGO DEJARPENDIENTE-->"+dejarPendiente+"************************");
		logger.info("\n\n\n ESPORTATIL----------------->"+esPortatil+"   subcuenta opcional (control postopersatorio)-->"+subCuentaCoberturaOPCIONAL);
		String tipoPaciente;
		try
	    {
			logger.info("codigocuentaopcional=> "+codigoCuentaOPCIONAL);
			//LA CUENTA SE PUEDE ENVIAR X PARAMETRO PARA EL CASO DE CARGOS DIRECTOS
			if(codigoCuentaOPCIONAL<1)
				codigoCuentaOPCIONAL= paciente.getCodigoCuenta();
			if(codigoCuentaOPCIONAL<1)
				codigoCuentaOPCIONAL= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
	    	int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuentaOPCIONAL+"");
	    	
	    	codigoTipoSolicitudOPCIONAL= asignarTipoSolicitud(numeroSolicitud, codigoTipoSolicitudOPCIONAL, con);
	    	boolean esCita=false;
			if(codigoTipoSolicitudOPCIONAL==ConstantesBD.codigoTipoSolicitudCita)
				esCita=true;
	    	
	    	//SE OBTIENE EL TIPO DE PACIENTE DE LA CUENTA.
			tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, codigoCuentaOPCIONAL+"").getAcronimo();
			
			logger.info("Tipo Paciente asociado a la cuenta ->"+tipoPaciente);
			
	    	//1.OBTENEMOS EL CODIGO DEL SERVICIO, 
	    	//EN CASO DE NO EXISTIR SE LO ASIGNAMOS AL RESPONSABLE DE PRIORIDAD #1
	    	//CONFIRMADO MARGARITA (2007-07-04 14:32) Y SE DEJA PENDIENTE
	    	if(codigoServicioOPCIONAL<1)
	    		codigoServicioOPCIONAL= obtenerCodigoServicio(con, codigoServicioOPCIONAL, codigoTipoSolicitudOPCIONAL, numeroSolicitud, codigoEvolucionOPCIONAL, esPortatil);
	    	
	    	logger.info("codigoIngreso=> "+paciente.getCodigoIngreso());
	    	logger.info("codigoServicioOpcioanl=> "+codigoServicioOPCIONAL);
	    	if(codigoServicioOPCIONAL<1)
	    	{
	    		logger.info("no existe servicio!!!!!!!");
	    		//se genera cargo pendiente responsable #1
	    		infoResponsableCobertura= Cobertura.validacionCoberturaNoAsignadoServicio(con, paciente.getCodigoIngreso()+"",this.pyp);
	    		dejarPendiente=true;
	    	}
	    	else
	    	{
	    		logger.info("existe servicio!!!!!!!");
	    		//1. EVALUAMOS LA COBERTURA
	    		infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", codigoViaIngreso, tipoPaciente, codigoServicioOPCIONAL, usuario.getCodigoInstitucionInt(),this.pyp, subCuentaCoberturaOPCIONAL /*subCuentaCoberturaOPCIONAL*/);
	    		 //FIXME Se obtien infoResponsableCobertura para posteior evaluacion para AutorizacionCapitacion  
	    	    this.infoResponsableCoberturaGeneral=infoResponsableCobertura;
	    	}
	    	
	    	logger.info("va ha insertar la solicitud de subcuenta!!!!!!!!");
	    	double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(con,numeroSolicitud,codigoCuentaOPCIONAL, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),codigoServicioOPCIONAL+"","", cantidadServicioOPCIONAL/*cantidadServicio*/, infoResponsableCobertura.getInfoCobertura().getIncluidoStr(),codigoTipoSolicitudOPCIONAL, "" /*codigoServicioCx*/, ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
		    if(solicitudSubCuenta<1)
		    {
		    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
		        return false;
		    }
		    logger.info("inserto la solicitud de sub_cuenta->"+solicitudSubCuenta+" dejarPendiente-->"+dejarPendiente);
		    
		    if(esquemaTarifarioOPCIONAL<=0)
		    {
		    	esquemaTarifarioOPCIONAL= infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicioOPCIONAL,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud))/*codigoEsquemaTarifarioServicioOPCIONAL*/;
		    }
		    
		    this.setInfoErroresCargo(	generarCargoServicio(		con, 
																	dejarPendiente/*dejarPendiente*/, 
																	esCita /*esCita*/, 
																	numeroSolicitud, 
																	codigoViaIngreso/*codigoViaIngreso*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuentaOPCIONAL+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt(), 
																	""/*observaciones*/, 
																	usuario.getLoginUsuario(), 
																	infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble() /*codigoSubcuenta*/, 
																	solicitudSubCuenta /*codigoSolicitudSubcuenta*/, 
																	infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	ConstantesBD.acronimoNo/*paquetizado*/,
																	cantidadServicioOPCIONAL /*cantidadCargada*/, 
																	infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
																	ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/, 
																	codigoServicioOPCIONAL /*codigoServicioOPCIONAL*/, 
																	valorTarifaOPCIONAL/*valorTarifaOpcional*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																	esquemaTarifarioOPCIONAL,
																	codigoTipoSolicitudOPCIONAL /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido/*codigoCentroCostoSolicitanteOPCIONAL*/,
																	"",/* numeroAutorizacionOPCIONAL -- numeroAutorizacionOPCIONAL*/
																	porcentajeDescuentoComercialOPCIONAL,
																	valorUnitarioDescuentoComercialOPCIONAL.doubleValue(),
																	true /*esRegistroNuevo*/,
																	true /*insertarEnBD*/,
																	esPortatil,
																	excento,
																	fechaCalculoVigenciaOPCIONAL,
																	porcentajeDctoPromocionServicio, 
																	valorDescuentoPromocionServicio, 
																	porcentajeHonorarioPromocionServicio, 
																	valorHonorarioPromocionServicio,
																	programa,
																	porcentajeDctoBono,
																	valorDescuentoBono, 
																	porcentajeDctoOdontologico, 
																	valorDescuentoOdontologico,
																	detallePaqueteOdonConvenio
																));
		    
		    infoResponsableCobertura.getDtoSubCuenta().setTipoPaciente(tipoPaciente);
		    logger.info("inserto cargo!!!");
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
	        e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codigoCuenta
	 * @param numeroIngreso
	 * @param dejarPendiente
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param cantidadServicioOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	public boolean generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(Connection con, UsuarioBasico usuario, int codigoCuenta,int numeroIngreso, boolean dejarPendiente, int numeroSolicitud, int codigoTipoSolicitudOPCIONAL, int codigoCentroCostoSolicitanteOPCIONAL, int codigoServicioOPCIONAL, int cantidadServicioOPCIONAL, double valorTarifaOPCIONAL, int codigoEvolucionOPCIONAL, /*String numeroAutorizacionOPCIONAL, */String esPortatil,String fechaCalculoVigenciaOPCIONAL) 
	{
		logger.info("\n\n\n*********************LLEGA A EVALUAR COBERTURA - GENERAR SOLICItud SUBCUENTA Y GENERAR CARGO************************");
		try
	    {
			logger.info("codigocuentaopcional=> "+codigoCuenta);
			
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
	    	int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
	    	
	    	codigoTipoSolicitudOPCIONAL= asignarTipoSolicitud(numeroSolicitud, codigoTipoSolicitudOPCIONAL, con);
	    	boolean esCita=false;
			if(codigoTipoSolicitudOPCIONAL==ConstantesBD.codigoTipoSolicitudCita)
				esCita=true;
	    	
			
			//obtenemos el tipo paciente de la solicitud
			
			String tipoPaciente= UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
			
			logger.info("Tipo Paciente "+tipoPaciente);
	    	
	    	//1.OBTENEMOS EL CODIGO DEL SERVICIO, 
	    	//EN CASO DE NO EXISTIR SE LO ASIGNAMOS AL RESPONSABLE DE PRIORIDAD #1
	    	//CONFIRMADO MARGARITA (2007-07-04 14:32) Y SE DEJA PENDIENTE
	    	if(codigoServicioOPCIONAL<1)
	    		codigoServicioOPCIONAL= obtenerCodigoServicio(con, codigoServicioOPCIONAL, codigoTipoSolicitudOPCIONAL, numeroSolicitud, codigoEvolucionOPCIONAL, esPortatil);
	    	
	    	logger.info("codigoIngreso=> "+numeroIngreso);
	    	logger.info("codigoServicioOpcioanl=> "+codigoServicioOPCIONAL);
	    	if(codigoServicioOPCIONAL<1)
	    	{
	    		logger.info("no existe servicio!!!!!!!");
	    		//se genera cargo pendiente responsable #1
	    		infoResponsableCobertura= Cobertura.validacionCoberturaNoAsignadoServicio(con,numeroIngreso+"",this.pyp);
	    		dejarPendiente=true;
	    	}
	    	else
	    	{	
	    		logger.info("existe servicio!!!!!!!");
	    		//1. EVALUAMOS LA COBERTURA
	    		infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, numeroIngreso+"", codigoViaIngreso, tipoPaciente, codigoServicioOPCIONAL, usuario.getCodigoInstitucionInt(),this.pyp, "" /*subCuentaCoberturaOPCIONAL*/);
	    	}
	    	
	    	logger.info("va ha insertar la solicitud de subcuenta!!!!!!!!");
	    	double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(con,numeroSolicitud,codigoCuenta, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),codigoServicioOPCIONAL+"","", cantidadServicioOPCIONAL/*cantidadServicio*/, infoResponsableCobertura.getInfoCobertura().getIncluidoStr(),codigoTipoSolicitudOPCIONAL, "" /*codigoServicioCx*/, ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
		    if(solicitudSubCuenta<1)
		    {
		    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
		        return false;
		    }
		    logger.info("inserto la solicitud de sub_cuenta->"+solicitudSubCuenta);
		    
		    this.setInfoErroresCargo(	generarCargoServicio(		con, 
																	dejarPendiente/*dejarPendiente*/, 
																	esCita /*esCita*/, 
																	numeroSolicitud, 
																	codigoViaIngreso/*codigoViaIngreso*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt(), 
																	""/*observaciones*/, 
																	usuario.getLoginUsuario(), 
																	infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble() /*codigoSubcuenta*/, 
																	solicitudSubCuenta /*codigoSolicitudSubcuenta*/, 
																	infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	ConstantesBD.acronimoNo/*paquetizado*/,
																	cantidadServicioOPCIONAL /*cantidadCargada*/, 
																	infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
																	ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/, 
																	codigoServicioOPCIONAL /*codigoServicioOPCIONAL*/, 
																	valorTarifaOPCIONAL/*valorTarifaOpcional*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																	infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoServicioOPCIONAL,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud))/*codigoEsquemaTarifarioServicioOPCIONAL*/,
																	codigoTipoSolicitudOPCIONAL /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido/*codigoCentroCostoSolicitanteOPCIONAL*/,
																	"",/*numeroAutorizacionOPCIONAL -- numeroAutorizacionOPCIONAL*/
																	-1 /*porcentajeDescuentoOPCIONAL*/,
																	-1 /*double valorUnitarioDescuentoOPCIONAL*/,
																	true /*esRegistroNuevo*/,
																	true /*insertarEnBD*/,
																	esPortatil,
																	false,
																	fechaCalculoVigenciaOPCIONAL,
																	0 /*porcentajeDctoPromocionServicio*/, 
																	BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																	0 /*porcentajeHonorarioPromocionServicio*/, 
																	BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																	0/*programa*/,
																	0/*porcentajeDctoBono*/,
																	BigDecimal.ZERO/*valorDescuentoBono*/, 
																	0/*porcentajeDctoOdontologico*/, 
																	BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																	0 /*detallePaqueteOdonConvenio*/
																));
		    
		    logger.info("inserto cargo!!!");
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
	        e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	
	/**
	 * metodo para recalcular el cargo de una solicitud de servicio que este en estado de facturacion diferente a anulado
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param codigoEvolucionOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param convenioResponsableOPCIONAL
	 * @return
	 */
	public boolean recalcularCargoServicio(Connection con, int numeroSolicitud, UsuarioBasico usuario, int codigoEvolucionOPCIONAL, String observaciones, int codigoServicioOPCIONAL, double subCuentaOPCIONAL, int codigoEsquemaTarifarioOPCIONAL, boolean filtrarSoloCantidadesMayoresCero, String esComponentePaquete, String esPortatil,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	{
		logger.info("\n\n\n*********************LLEGA A RECALCULAR CARGO DE LA SOLICITUD "+numeroSolicitud+"************************");
		
		DtoDetalleCargo criteriosBusquedaDtoDetalleCargo= new DtoDetalleCargo();
		criteriosBusquedaDtoDetalleCargo.setNumeroSolicitud(numeroSolicitud);
		criteriosBusquedaDtoDetalleCargo.setFacturado(ConstantesBD.acronimoNo);
		criteriosBusquedaDtoDetalleCargo.setFiltrarSoloCantidadesMayoresCero(filtrarSoloCantidadesMayoresCero);
		criteriosBusquedaDtoDetalleCargo.setPaquetizado(esComponentePaquete);
		criteriosBusquedaDtoDetalleCargo.setEsPortatil(esPortatil);
		if(subCuentaOPCIONAL>0)
			criteriosBusquedaDtoDetalleCargo.setCodigoSubcuenta(subCuentaOPCIONAL);
		ArrayList<DtoDetalleCargo> arrayDtoDetalleCargo=cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
	
		int codigoCuenta= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
		
		if(UtilidadTexto.isEmpty(esComponentePaquete))
			esComponentePaquete=ConstantesBD.acronimoNo;
		
		InfoDatosString tipoPaciente= UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"");
		
		logger.info("size---->"+arrayDtoDetalleCargo.size());
		for(int w=0; w<arrayDtoDetalleCargo.size(); w++)
		{
			try
			{
				DtoSubCuentas dtoSubCuenta= UtilidadesHistoriaClinica.obtenerResponsable(con, (int)arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				
				logger.info("responsable------->"+dtoSubCuenta.getConvenio().getCodigo());
				logger.info("subcuenta------->"+arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				logger.info("DetallesCargo(0).getCubierto()->"+arrayDtoDetalleCargo.get(w).getCubierto());
				
				logger.info("DEjarExcento----->"+arrayDtoDetalleCargo.get(w).getDejarExcento());
				
				logger.info("DEBEMOS VOLVER A EVALUAR SI REQUIERE AUTORIZACION Y SI ESTA CUBIERTO!!!!!!");
				
				if(codigoServicioOPCIONAL>0)
					arrayDtoDetalleCargo.get(w).setCodigoServicio(codigoServicioOPCIONAL);
				
				InfoCobertura infoCobertura= Cobertura.validacionCoberturaServicioDadoResponsable(con, dtoSubCuenta, codigoViaIngreso, tipoPaciente.getAcronimo(), arrayDtoDetalleCargo.get(w).getCodigoServicio(), usuario.getCodigoInstitucionInt());
				
				/*if(codigoEsquemaTarifarioOPCIONAL>0)
					arrayDtoDetalleCargo.get(w).setCodigoEsquemaTarifario(codigoEsquemaTarifarioOPCIONAL);*/
				
				this.setInfoErroresCargo(	generarCargoServicio(	con, 
																	false/*dejarPendiente*/, 
																	false /*esCita*/, 
																	numeroSolicitud, 
																	codigoViaIngreso/*codigoViaIngreso*/, 
																	dtoSubCuenta.getContrato()/*codigoContrato*/, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt(), 
																	observaciones/*observaciones*/, 
																	usuario.getLoginUsuario(), 
																	arrayDtoDetalleCargo.get(w).getCodigoSubcuenta() /*codigoSubcuenta*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoSolicitudSubCuenta() /*codigoSolicitudSubcuenta*/, 
																	infoCobertura.getIncluidoStr()/*cubierto*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	esComponentePaquete/*paquetizado*/,
																	arrayDtoDetalleCargo.get(w).getCantidadCargada() /*cantidadCargada*/, 
																	infoCobertura.getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
																	codigoEvolucionOPCIONAL /*codigoEvolucionOPCIONAL*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoServicio() /*codigoServicioOPCIONAL*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOpcional*/, 
																	dtoSubCuenta.getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																	codigoEsquemaTarifarioOPCIONAL /*codigoEsquemaTarifarioServicioOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido /*codigoCentroCostoSolicitanteOPCIONAL*/,
																	arrayDtoDetalleCargo.get(w).getNumeroAutorizacion()/* "" -- numeroAutorizacionOPCIONAL*/,
																	-1 /*porcentajeDescuentoOPCIONAL*/,
																	-1 /*double valorUnitarioDescuentoOPCIONAL*/,
																	false,
																	true /*insertarEnBD*/,
																	esPortatil,
																	UtilidadTexto.getBoolean(arrayDtoDetalleCargo.get(w).getDejarExcento()),
																	fechaCalculoVigenciaOPCIONAL,
																	0 /*porcentajeDctoPromocionServicio*/, 
																	BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																	0 /*porcentajeHonorarioPromocionServicio*/, 
																	BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																	
																	0/*programa*/,
																	0/*porcentajeDctoBono*/,
																	BigDecimal.ZERO/*valorDescuentoBono*/, 
																	0/*porcentajeDctoOdontologico*/, 
																	BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																	0 /*detallePaqueteOdonConvenio*/
																));
				    
			}
		    catch(Exception e)
		    {
		        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
		        e.printStackTrace();
				return false;
		    }
		}    
	    return true;
	}
	
	/**
	 * metodo para recalcular el cargo de una solicitud de servicio que este en estado de facturacion diferente a anulado
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param codigoEvolucionOPCIONAL
	 * @param codigoServicioOPCIONAL
	 * @param convenioResponsableOPCIONAL
	 * @return
	 */
	public boolean recalcularCargoServicioValidandoCobertura(Connection con, int numeroSolicitud, UsuarioBasico usuario, int codigoEvolucionOPCIONAL,int ingreso, String observaciones, int codigoServicioOPCIONAL, double subCuentaOPCIONAL, int codigoEsquemaTarifarioOPCIONAL, boolean filtrarSoloCantidadesMayoresCero, String esComponentePaquete, String esPortatil,String fechaCalculoVigenciaOPCIONAL) throws IPSException 
	{
		logger.info("\n\n\n*********************LLEGA A RECALCULAR CARGO DE LA SOLICITUD "+numeroSolicitud+"************************");
		
		DtoDetalleCargo criteriosBusquedaDtoDetalleCargo= new DtoDetalleCargo();
		criteriosBusquedaDtoDetalleCargo.setNumeroSolicitud(numeroSolicitud);
		criteriosBusquedaDtoDetalleCargo.setFacturado(ConstantesBD.acronimoNo);
		criteriosBusquedaDtoDetalleCargo.setFiltrarSoloCantidadesMayoresCero(filtrarSoloCantidadesMayoresCero);
		criteriosBusquedaDtoDetalleCargo.setPaquetizado(esComponentePaquete);
		if(subCuentaOPCIONAL>0)
			criteriosBusquedaDtoDetalleCargo.setCodigoSubcuenta(subCuentaOPCIONAL);
		ArrayList<DtoDetalleCargo> arrayDtoDetalleCargo=cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
	
		int codigoCuenta= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
		
		String tipoPaciente= UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
		
		if(UtilidadTexto.isEmpty(esComponentePaquete))
			esComponentePaquete=ConstantesBD.acronimoNo;
		
		for(int w=0; w<arrayDtoDetalleCargo.size(); w++)
		{
			try
		    {
				DtoSubCuentas dtoSubCuenta= UtilidadesHistoriaClinica.obtenerResponsable(con, (int)arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				
				logger.info("responsable------->"+dtoSubCuenta.getConvenio().getCodigo());
				logger.info("subCuenta------->"+arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				logger.info("DetallesCargo(0).getCubierto()->"+arrayDtoDetalleCargo.get(w).getCubierto());
				if(codigoServicioOPCIONAL>0)
					arrayDtoDetalleCargo.get(w).setCodigoServicio(codigoServicioOPCIONAL);
				
				if(codigoEsquemaTarifarioOPCIONAL>0)
					arrayDtoDetalleCargo.get(w).setCodigoEsquemaTarifario(codigoEsquemaTarifarioOPCIONAL);
				
				InfoResponsableCobertura infoCobertura=new InfoResponsableCobertura();
				infoCobertura=Cobertura.validacionCoberturaServicio(con,ingreso+"", codigoViaIngreso, tipoPaciente, arrayDtoDetalleCargo.get(w).getCodigoServicio(), usuario.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);

				
				this.setInfoErroresCargo(	generarCargoServicio(	con, 
																	false/*dejarPendiente*/, 
																	false /*esCita*/, 
																	numeroSolicitud, 
																	codigoViaIngreso/*codigoViaIngreso*/, 
																	dtoSubCuenta.getContrato() /*codigoContrato*/, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt(), 
																	observaciones/*observaciones*/, 
																	usuario.getLoginUsuario(), 
																	arrayDtoDetalleCargo.get(w).getCodigoSubcuenta() /*codigoSubcuenta*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoSolicitudSubCuenta() /*codigoSolicitudSubcuenta*/, 
																	infoCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	esComponentePaquete/*paquetizado*/,
																	arrayDtoDetalleCargo.get(w).getCantidadCargada() /*cantidadCargada*/, 
																	infoCobertura.getInfoCobertura().getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
																	codigoEvolucionOPCIONAL /*codigoEvolucionOPCIONAL*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoServicio() /*codigoServicioOPCIONAL*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOpcional*/, 
																	dtoSubCuenta.getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoEsquemaTarifario()/*codigoEsquemaTarifarioServicioOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido /*codigoCentroCostoSolicitanteOPCIONAL*/,
																	"",/* "" -- numeroAutorizacionOPCIONAL*/
																	-1 /*porcentajeDescuentoOPCIONAL*/,
																	-1 /*double valorUnitarioDescuentoOPCIONAL*/,
																	false,
																	true /*insertarEnBD*/,
																	esPortatil,
																	false,
																	fechaCalculoVigenciaOPCIONAL,
																	0 /*porcentajeDctoPromocionServicio*/, 
																	BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																	0 /*porcentajeHonorarioPromocionServicio*/, 
																	BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																	
																	0/*programa*/,
																	0/*porcentajeDctoBono*/,
																	BigDecimal.ZERO/*valorDescuentoBono*/, 
																	0/*porcentajeDctoOdontologico*/, 
																	BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																	0/*detallePaqueteOdonConvenio*/
																));
				    
			}
		    catch(Exception e)
		    {
		        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
		        e.printStackTrace();
				return false;
		    }
		}    
	    return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param cantidadArticulo
	 * @param dejarPendiente
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param pyp
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(	Connection con, UsuarioBasico usuario, PersonaBasica paciente, int numeroSolicitud, int codigoArticulo, int cantidadArticuloSolicitada, 
																				boolean dejarPendiente, int codigoTipoSolicitudOPCIONAL, int codigoCuentaOPCIONAL, int codigoCentroCostoSolicitanteOPCIONAL, 
																				double valorTarifaOPCIONAL,boolean pyp,String fechaCalculoVigenciaOPCIONAL, boolean tarifaNoModificada) throws IPSException
	{
		
		//la cantidad cargada va ha ser cero porque apenas se generara la solicitud
		if(codigoCuentaOPCIONAL<1)
			codigoCuentaOPCIONAL= paciente.getCodigoCuenta();
		if(codigoCuentaOPCIONAL<1)
			codigoCuentaOPCIONAL= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuentaOPCIONAL+"");
    	codigoTipoSolicitudOPCIONAL= asignarTipoSolicitud(numeroSolicitud, codigoTipoSolicitudOPCIONAL, con);
    	
    	//obtenemos el tipo paciente asociado a la solicitud
    	String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
    	logger.info("tipo paciente"+tipoPaciente);
    	
    	
    	//1. EVALUAMOS LA COBERTURA
	    InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", codigoViaIngreso,tipoPaciente, codigoArticulo, usuario.getCodigoInstitucionInt(),pyp);
	    	    	
	    //FIXME Se obtien infoResponsableCobertura para posteior evaluacion para AutorizacionCapitacion  
	    this.infoResponsableCoberturaGeneral=infoResponsableCobertura;
	    
	    double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(con,numeroSolicitud, codigoCuentaOPCIONAL, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),"", codigoArticulo+"", 0 /*cantidadArticuloSolicitada*/, infoResponsableCobertura.getInfoCobertura().getIncluidoStr(), codigoTipoSolicitudOPCIONAL,"" /*codigoServicioCx*/, ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
	    logger.info("\n\nSOLICITUD SUBCUENTA INSERTADO-->"+solicitudSubCuenta+"\n\n");
	    
	    if(solicitudSubCuenta<1)
	    {
	    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
	    	return false;
	    }
	    		    
	    //Cargos cargoArticulos= new Cargos();
	    this.setInfoErroresCargo(	this.generarCargoArticulo	(	con, 
					    		    										dejarPendiente,/*dejarPendiente*/ 
					    		    										numeroSolicitud, 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
					    		    										codigoArticulo, 
					    		    										codigoViaIngreso, 
					    		    										Cuenta.obtenerTipoComplejidad(con, codigoCuentaOPCIONAL+"")/*codigoTipoComplejidad*/, 
					    		    										usuario.getCodigoInstitucionInt(), 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()/*codigoSubcuenta*/, 
					    		    										solicitudSubCuenta/*codigoSolicitudSubcuenta*/, 
					    		    										infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
					    		    										ConstantesBD.acronimoNo/*paquetizado*/, 
					    		    										ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
					    		    										infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr()/*requiereAutorizacion*/,
					    		    										ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad/*tipoDistribucion*/,
					    		    										infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioArticuloPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)) /*codigoEsquemaTarifarioOPCIONAL*/, 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioBaseOPCIONAL*/,
					    		    										valorTarifaOPCIONAL/*valorTarifaOPCIONAL*/,
					    		    										codigoTipoSolicitudOPCIONAL /*codigoTipoSolicitudOPCIONAL*/,
					    		    										ConstantesBD.codigoNuncaValido /*centroCostoSolicitanteOPCIONAL*/,
					    		    										cantidadArticuloSolicitada /*cantidadArticuloSolicitadaOPCIONAL*/,
					    		    										/*"" -- numeroAutorizacionOPCIONAL ,*/
					    		    										-1 /*porcentajeDescuentoOPCIONAL*/,
																			-1 /*double valorUnitarioDescuentoOPCIONAL*/,
					    		    										usuario.getLoginUsuario(), 
					    		    										""/*observaciones*/,
					    		    										true /*esRegistroNuevo*/,
					    		    										true /*insertarEnBD*/,
					    		    										false /*cancelarInsercionSiExisteError*/,
					    		    										fechaCalculoVigenciaOPCIONAL,
					    		    										tarifaNoModificada /*tarifaNoModificada*/));
	    
	    return true;
	}
	
	/**
	 * Actualiza el estado de cobertura de un articulo con respecto al convenio/contrato 
	 * 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param cantidadArticuloSolicitada
	 * @param dejarPendiente
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param pyp
	 * @param fechaCalculoVigenciaOPCIONAL
	 * @param tarifaNoModificada
	 * @author jeilones
	 * @throws IPSException 
	 * @created 5/02/2013
	 */
	public void modificarCoberturaArticulosXConvenioSolicitud(
			Connection con, 
			UsuarioBasico usuario, 
			PersonaBasica paciente,
			int numeroSolicitud,
			int codigoServicio,
			int codigoArticulo,
			int codigoTipoSolicitudOPCIONAL, 
			int codigoCuentaOPCIONAL,
			boolean pyp
			) throws IPSException {

		// la cantidad cargada va ha ser cero porque apenas se generara la
		// solicitud
		if (codigoCuentaOPCIONAL < 1)
			codigoCuentaOPCIONAL = paciente.getCodigoCuenta();
		if (codigoCuentaOPCIONAL < 1)
			codigoCuentaOPCIONAL = Utilidades.getCuentaSolicitud(con,
					numeroSolicitud);

		int codigoViaIngreso = Cuenta.obtenerCodigoViaIngresoCuenta(con,
				codigoCuentaOPCIONAL + "");
		codigoTipoSolicitudOPCIONAL = asignarTipoSolicitud(numeroSolicitud,
				codigoTipoSolicitudOPCIONAL, con);

		// obtenemos el tipo paciente asociado a la solicitud
		String tipoPaciente = UtilidadesManejoPaciente
				.obtenerTipoPacienteSolicitud(con, numeroSolicitud + "")
				.getAcronimo();
		logger.info("tipo paciente" + tipoPaciente);

		// 1. EVALUAMOS LA COBERTURA
		InfoResponsableCobertura infoResponsableCobertura = Cobertura
				.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()
						+ "", codigoViaIngreso, tipoPaciente, codigoArticulo,
						usuario.getCodigoInstitucionInt(), pyp);

		// FIXME Se obtien infoResponsableCobertura para posteior evaluacion
		// para AutorizacionCapitacion
		this.infoResponsableCoberturaGeneral = infoResponsableCobertura;

		OrdenesFacade ordenesFacade=new OrdenesFacade();
	
		DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
		dto.setCuenta(codigoCuentaOPCIONAL+"");
		dto.setSubCuenta(infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()+"");
		dto.setNumeroSolicitud(numeroSolicitud+"");
		dto.setServicio(new InfoDatosString(codigoServicio+""));
		dto.setArticulo(new InfoDatosString(codigoArticulo+""));
		dto.setCubierto(infoResponsableCobertura.getInfoCobertura().getIncluidoStr());
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		
		/*
		 * MT 5880 se debe guardar la cobertura y el contrato que lo cubre
		 * segun DCU 437 v1.3 - PROCESO GENERAL, DE LA VALIDACIÓN DE COBERTURA DE SERVICIOS ARTÍCULOS
		 * incluyendo los que existen desde la creacion de la solicitud como los que se agregan en la modificacion
		 * 
		 * jeilones
		 * */
		ordenesFacade.actualizarCobertura(dto, true);
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param cantidadArticulo
	 * @param dejarPendiente
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param pyp
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean generarSolicitudSubCuentaCargoMaterialesEspeciales(	Connection con, UsuarioBasico usuario, int numeroSolicitud, int codigoArticulo, int cantidadArticuloSolicitada, 
																		int codigoCuenta, int codigoCentroCostoSolicitante, double valorTarifa, int codigoViaIngreso, InfoResponsableCobertura infoResponsableCobertura,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	
	{
		double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(con,numeroSolicitud, codigoCuenta, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),"", codigoArticulo+"", cantidadArticuloSolicitada /*cantidadArticuloSolicitada*/, ConstantesBD.acronimoNo/*incluido*/, ConstantesBD.codigoTipoSolicitudCirugia, "" /*codigoServicioCx*/, ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
	    logger.info("\n\nSOLICITUD SUBCUENTA INSERTADO-->"+solicitudSubCuenta+"\n\n");
	    
	    if(solicitudSubCuenta<1)
	    {
	    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
	    	return false;
	    }
	    		    
	    Cargos cargoArticulos= new Cargos();
	    this.setInfoErroresCargo(	cargoArticulos.generarCargoArticulo	(	con, 
					    		    										false,/*dejarPendiente*/ 
					    		    										numeroSolicitud, 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
					    		    										codigoArticulo, 
					    		    										codigoViaIngreso, 
					    		    										Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"")/*codigoTipoComplejidad*/, 
					    		    										usuario.getCodigoInstitucionInt(), 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()/*codigoSubcuenta*/, 
					    		    										solicitudSubCuenta/*codigoSolicitudSubcuenta*/, 
					    		    										infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
					    		    										ConstantesBD.acronimoNo/*paquetizado*/, 
					    		    										ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
					    		    										infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr()/*requiereAutorizacion*/,
					    		    										ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad/*tipoDistribucion*/,
					    		    										infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioArticuloPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)) /*codigoEsquemaTarifarioOPCIONAL*/, 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioBaseOPCIONAL*/,
					    		    										valorTarifa/*valorTarifaOPCIONAL*/,
					    		    										ConstantesBD.codigoTipoSolicitudCirugia/*codigoTipoSolicitudOPCIONAL*/,
					    		    										ConstantesBD.codigoNuncaValido,// codigoCentroCostoSolicitante /*OPCIONAL*/,
					    		    										cantidadArticuloSolicitada /*cantidadArticuloSolicitadaOPCIONAL*/,
					    		    										/*"" -- numeroAutorizacionOPCIONAL,*/
					    		    										-1 /*porcentajeDescuentoOPCIONAL*/,
																			-1 /*double valorUnitarioDescuentoOPCIONAL*/,
					    		    										usuario.getLoginUsuario(), 
					    		    										""/*observaciones*/,
					    		    										true /*esRegistroNuevo*/,
					    		    										true /*insertarEnBD*/,
					    		    										true /*cancelarInsercionSiExisteError*/,
					    		    										fechaCalculoVigenciaOPCIONAL, 
					    		    										false /*tarifaNoModificada*/));
	    return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param numeroIngreso
	 * @param codigoCuenta
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param cantidadArticulo
	 * @param dejarPendiente
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCuentaOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param pyp
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(	Connection con, UsuarioBasico usuario, int numeroIngreso,int codigoCuenta, int numeroSolicitud, int codigoArticulo, int cantidadArticuloSolicitada, 
																				boolean dejarPendiente, int codigoTipoSolicitudOPCIONAL, int codigoCuentaOPCIONAL, int codigoCentroCostoSolicitanteOPCIONAL, double valorTarifaOPCIONAL,boolean pyp,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	{
		//la cantidad cargada va ha ser cero porque apenas se generara la solicitud
		if(codigoCuentaOPCIONAL<1)
			codigoCuentaOPCIONAL= codigoCuenta;
		if(codigoCuentaOPCIONAL<1)
			codigoCuentaOPCIONAL= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuentaOPCIONAL+"");
    	codigoTipoSolicitudOPCIONAL= asignarTipoSolicitud(numeroSolicitud, codigoTipoSolicitudOPCIONAL, con);
    	
    	//se obtiene el tipo paciente asociado a la solicitud
    	String tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
    	
    	logger.info("tipo paciente"+tipoPaciente);
    	
    	//1. EVALUAMOS LA COBERTURA
	    InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaArticulo(con, numeroIngreso+"", codigoViaIngreso,tipoPaciente, codigoArticulo, usuario.getCodigoInstitucionInt(),pyp);
	    	    	
	    double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(con,numeroSolicitud, codigoCuentaOPCIONAL, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),"", codigoArticulo+"", 0 /*cantidadArticuloSolicitada*/, infoResponsableCobertura.getInfoCobertura().getIncluidoStr(), codigoTipoSolicitudOPCIONAL,"" /*codigoServicioCx*/, ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad, usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
	    logger.info("\n\nSOLICITUD SUBCUENTA INSERTADO-->"+solicitudSubCuenta+"\n\n");
	    
	    if(solicitudSubCuenta<1)
	    {
	    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
	    	return false;
	    }
	    		    
	    Cargos cargoArticulos= new Cargos();
	    this.setInfoErroresCargo(	cargoArticulos.generarCargoArticulo	(	con, 
					    		    										dejarPendiente,/*dejarPendiente*/ 
					    		    										numeroSolicitud, 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
					    		    										codigoArticulo, 
					    		    										codigoViaIngreso, 
					    		    										Cuenta.obtenerTipoComplejidad(con, codigoCuentaOPCIONAL+"")/*codigoTipoComplejidad*/, 
					    		    										usuario.getCodigoInstitucionInt(), 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()/*codigoSubcuenta*/, 
					    		    										solicitudSubCuenta/*codigoSolicitudSubcuenta*/, 
					    		    										infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
					    		    										ConstantesBD.acronimoNo/*paquetizado*/, 
					    		    										ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
					    		    										infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr()/*requiereAutorizacion*/,
					    		    										ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad/*tipoDistribucion*/,
					    		    										infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioArticuloPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)) /*codigoEsquemaTarifarioOPCIONAL*/, 
					    		    										infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioBaseOPCIONAL*/,
					    		    										valorTarifaOPCIONAL/*valorTarifaOPCIONAL*/,
					    		    										codigoTipoSolicitudOPCIONAL /*codigoTipoSolicitudOPCIONAL*/,
					    		    										ConstantesBD.codigoNuncaValido /*centroCostoSolicitanteOPCIONAL*/,
					    		    										cantidadArticuloSolicitada /*cantidadArticuloSolicitadaOPCIONAL*/,
					    		    										/*"" --numeroAutorizacionOPCIONAL ,*/
					    		    										-1 /*porcentajeDescuentoOPCIONAL*/,
																			-1 /*double valorUnitarioDescuentoOPCIONAL*/,
					    		    										usuario.getLoginUsuario(), 
					    		    										""/*observaciones*/,
					    		    										true /*esRegistroNuevo*/,
					    		    										true /*insertarEnBD*/,
					    		    										false /*cancelarInsercionSiExisteError*/,
					    		    										fechaCalculoVigenciaOPCIONAL, 
					    		    										false /*tarifaNoModificada*/));
	    return true;
	}
	

	
	/**
	 * metodo que intenta generar el cargo
	 * @param con
	 * @param dejarPendiente
	 * @param numeroSolicitud
	 * @param codigoContrato
	 * @param codigoArticulo
	 * @param codigoViaIngreso
	 * @param codigoTipoComplejidad
	 * @param codigoInstitucion
	 * @param codigoSubcuenta
	 * @param codigoSolicitudSubcuenta
	 * @param cubierto
	 * @param paquetizado
	 * @param codigoPadrePaquetes
	 * @param requiereAutorizacion
	 * @param codigoEsquemaTarifarioOPCIONAL
	 * @param codigoConvenioBaseOPCIONAL
	 * @param valorTarifaOPCIONAL
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param cantidadArticuloOPCIONAL
	 * @param loginUsuario
	 * @param observaciones
	 * @param boolean insertarEnBD
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public InfoErroresCargo generarCargoArticulo (	Connection con, 
													boolean dejarPendiente,
													
													int numeroSolicitud,
													int codigoContrato,
													int codigoArticulo,
													int codigoViaIngreso, 
													int codigoTipoComplejidad,
													int codigoInstitucion,
													
													double codigoSubcuenta,
													double codigoSolicitudSubcuenta,
													String cubierto,
													String paquetizado,
													double cargoPadrePaquetes,
													String requiereAutorizacion,
													String tipoDistribucion, ///////**********************************
													
													int codigoEsquemaTarifarioOPCIONAL,
													int codigoConvenioBaseOPCIONAL,
													double valorTarifaOPCIONAL,
													int codigoTipoSolicitudOPCIONAL,
													int codigoCentroCostoSolicitanteOPCIONAL,
													int cantidadArticuloOPCIONAL,
													/*String numeroAutorizacionOPCIONAL,*/
													double porcentajeDescuentoOPCIONAL,
													double valorUnitarioDescuentoOPCIONAL,
													
													String loginUsuario,
													String observaciones,
													boolean esRegistroNuevo, ////////*******************************
													boolean insertarEnBD,
													boolean cancelarInsercionSiExisteError,
													String fechaCalculoVigenciaOPCIONAL,
													boolean tarifaNoModificada
													) throws IPSException
	{
		///VARIABLES
		InfoErroresCargo erroresCargo =new InfoErroresCargo();
		double valorTarifaBase=ConstantesBD.codigoNuncaValidoDouble;
		double valorTarifaTotal=ConstantesBD.codigoNuncaValidoDouble;
		int codigoCentroCostoSolicitante= ConstantesBD.codigoNuncaValido;
		String metodoAjusteEsquemaTarifario="";
		DtoDetalleCargo dtoDetalleCargoBDAntesDeBorrar= new DtoDetalleCargo();
		Vector codigosComponentesPaquetes= new Vector();
		
		ArrayList<Integer> codDetalleAutorizaciones= new ArrayList<Integer>();
		ArrayList<Integer> codDetalleAutorizacionesEstancia= new ArrayList<Integer>();
		
		try {
	//		CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
			String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;
			fechaCalculoVigencia= UtilidadFecha.obtenerFechaSinHora(fechaCalculoVigencia);
			
	//		obtenemos el tipo paciente asociado a la solicitud
			String tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
			
			///1. CARGAMOS LA INFORMACION OPCIONAL
			codigoConvenioBaseOPCIONAL=obtenerCodigoConvenio(codigoContrato, codigoConvenioBaseOPCIONAL,con);
			codigoCentroCostoSolicitante= obtenerCodigoCentroCostoSolicitante(numeroSolicitud, codigoCentroCostoSolicitanteOPCIONAL, con);
			codigoTipoSolicitudOPCIONAL= asignarTipoSolicitud(numeroSolicitud, codigoTipoSolicitudOPCIONAL, con);
			
			if(codigoEsquemaTarifarioOPCIONAL<1){
				codigoEsquemaTarifarioOPCIONAL= obtenerCodigoEsquemaTarifarioContrato(con,codigoSubcuenta+"", codigoContrato,codigoArticulo, false,fechaCalculoVigencia, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud));
			}
			//2. CARGAMOS EL POSIBLE DETALLE DE CARGO X RESPONSABLE, Y LO ELIMINAMOS PARA GENERARLO NUEVAMENTE
			if(!esRegistroNuevo && insertarEnBD ){	
				DtoDetalleCargo criteriosBusquedaDtoDetalleCargo = new DtoDetalleCargo();
				criteriosBusquedaDtoDetalleCargo.setCodigoDetalleCargo(Cargos.obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(con, numeroSolicitud, codigoSubcuenta, codigoArticulo, ConstantesBD.acronimoNo/*facturado*/, paquetizado));
				if(criteriosBusquedaDtoDetalleCargo.getCodigoDetalleCargo()<1){	
					erroresCargo.setTieneErroresCodigo(true);
					return erroresCargo;
				}
				
				//el filtro debe retornar solo un registro, es unico el codigo del detalle de cargo cunado se filtra x solicitud - responsable (caso servicios)
				dtoDetalleCargoBDAntesDeBorrar=Cargos.cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo).get(0);
				//ya lo tenemos temporalmente en el dtoDetalleCargoBDAntesDeBorrar entonces lo podemos eliminar y recalcularlo
				//si es una solicitud tipo paquete debemos obtener el codigo del detalle del cargo, actualizarles el cargo_padre a null y al final cuanod se genera nuevamente actualizar con el nuevo
				if(codigoTipoSolicitudOPCIONAL==ConstantesBD.codigoTipoSolicitudPaquetes){
					codigosComponentesPaquetes=Cargos.obtenerCargosComponentesPaquete(con, dtoDetalleCargoBDAntesDeBorrar.getCodigoDetalleCargo());
					if(codigosComponentesPaquetes.size()>0)
						Cargos.actualizarCargoPadreComponentesPaquetes(con, ConstantesBD.codigoNuncaValidoDoubleNegativo /*para que los coloque null*/, codigosComponentesPaquetes);
				}
				
				//antes de eliminar hacemos un llamado para actualizar los detalles de cargo de las autorizaciones
				codDetalleAutorizaciones= cargarDetAutorizacionesXCargo(con, dtoDetalleCargoBDAntesDeBorrar.getCodigoDetalleCargo()); 
				codDetalleAutorizacionesEstancia= cargarDetAutorizacionesEstanciaXCargo(con, dtoDetalleCargoBDAntesDeBorrar.getCodigoDetalleCargo());
				actualizarCargoDetAutorizaciones(con, ConstantesBD.codigoNuncaValidoDoubleNegativo, codDetalleAutorizaciones);
				actualizarCargoDetAutorizacionesEstancia(con, ConstantesBD.codigoNuncaValidoDoubleNegativo, codDetalleAutorizacionesEstancia);
				
				if(!eliminarDetalleCargoYErroresYArtConsumoXCodigoDetalleCargo(con, dtoDetalleCargoBDAntesDeBorrar.getCodigoDetalleCargo())){
					erroresCargo.setTieneErroresCodigo(true);
					return erroresCargo;
				}
			}
			
			//3. BUSCAMOS LAS CANTIDADES TOTALES X RESPONSABLE
			if(!dejarPendiente){	
				if(cantidadArticuloOPCIONAL<0){
					cantidadArticuloOPCIONAL=Cargos.obtenerTotalAdminFarmaciaXResponsable(con, codigoArticulo, numeroSolicitud, codigoSubcuenta);
				}
			}
			else{
				//LA CANTIDAD CARGADA DEBE SER CERO
				cantidadArticuloOPCIONAL=0;
			}
			
			//4. ASIGNAMOS LOS POSIBLES ERRORES
			erroresCargo=asignarErroresEsquemaTarifario(codigoEsquemaTarifarioOPCIONAL, erroresCargo);
			if(erroresCargo.getTieneErrores() && cancelarInsercionSiExisteError){
				erroresCargo.setTieneErroresCodigo(true);	
				return erroresCargo;
			}
			else{	
				if(erroresCargo.getTieneErrores() && insertarEnBD){
					this.dtoDetalleCargo= insertarDetalleCargo(	con, 
																	codigoSubcuenta, 
																	codigoConvenioBaseOPCIONAL, 
																	codigoEsquemaTarifarioOPCIONAL, 
																	ConstantesBD.codigoNuncaValido/*cantidadCargada*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioTarifa*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorTotalCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*porcentajeCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*porcentajeRecargo*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioRecargo*/, 
																	porcentajeDescuentoOPCIONAL/*porcentajeDescuento*/, 
																	valorUnitarioDescuentoOPCIONAL/*valorUnitarioDescuento*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioIva*/, 
																	requiereAutorizacion, 
																	"",/* numeroAutorizacionOPCIONAL -- numeroAutorizacion*/ 
																	ConstantesBD.codigoEstadoFPendiente/*estado*/, 
																	cubierto, 
																	tipoDistribucion/*tipoDistribucion*/, 
																	numeroSolicitud, 
																	ConstantesBD.codigoNuncaValido/*codigoServicio*/, 
																	codigoArticulo, 
																	ConstantesBD.codigoNuncaValido /*codigoServicioCx*/, 
																	ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, 
																	ConstantesBD.acronimoNo /*facturado*/, 
																	codigoTipoSolicitudOPCIONAL, 
																	paquetizado, 
																	cargoPadrePaquetes /*cargoPadre*/, 
																	codigoSolicitudSubcuenta, 
																	observaciones, 
																	loginUsuario, 
																	erroresCargo, 
																	codigoContrato,
																	ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																	ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/,
																	"" /*esPortatil*/,
																	"" /*dejarExcento*/,
																	codDetalleAutorizaciones,
																	codDetalleAutorizacionesEstancia,
																	0 /*porcentajeDctoPromocionServicio*/, 
																	BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																	0 /*porcentajeHonorarioPromocionServicio*/, 
																	BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																	
																	0/*programa*/,
																	0/*porcentajeDctoBono*/,
																	BigDecimal.ZERO/*valorDescuentoBono*/, 
																	0/*porcentajeDctoOdontologico*/, 
																	BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																	0/*detallePaqueteOdonConvenio*/
																	);
					
					if(codigosComponentesPaquetes.size()>0){
						Cargos.actualizarCargoPadreComponentesPaquetes(con, this.dtoDetalleCargo.getCodigoDetalleCargo(), codigosComponentesPaquetes);
					}
					if(this.dtoDetalleCargo.getCodigoDetalleCargo()<1){
						erroresCargo.setTieneErroresCodigo(true);
						return erroresCargo;
					}
					return erroresCargo;
				}
			}
			//5. OBTENEMOS EL VALOR DE LA TARIFA BASE
			metodoAjusteEsquemaTarifario= obtenerMetodoAjuste(con, codigoConvenioBaseOPCIONAL, codigoEsquemaTarifarioOPCIONAL, codigoInstitucion, false/*esServicio*/);
			if(valorTarifaOPCIONAL>0){
				valorTarifaBase=valorTarifaOPCIONAL;
			}
			else{	
				valorTarifaBase=obtenerTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifarioOPCIONAL, fechaCalculoVigencia);
			}	
				
			if(valorTarifaBase<0){
			    erroresCargo.setMensajesErrorDetalle("error.cargo.noHayTarifaInventario");
			}
			
			if(erroresCargo.getTieneErrores() && cancelarInsercionSiExisteError){
				erroresCargo.setTieneErroresCodigo(true);	
				return erroresCargo;
			}
			else{
				//6. ASIGNAMOS LOS POSIBLES ERRORES DE TARIFA ARTICULO
				if(erroresCargo.getTieneErrores() || dejarPendiente && insertarEnBD){
				    //2.2.3.1 INSERTAMOS UN CARGO INCORRECTO LO DEJAMOS EN ESTADO FACTURACION PENDIENTE
					this.dtoDetalleCargo= insertarDetalleCargo(	con, 
																	codigoSubcuenta, 
																	codigoConvenioBaseOPCIONAL, 
																	codigoEsquemaTarifarioOPCIONAL, 
																	ConstantesBD.codigoNuncaValido/*cantidadCargada*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioTarifa*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorTotalCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*porcentajeCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*porcentajeRecargo*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioRecargo*/, 
																	porcentajeDescuentoOPCIONAL /*porcentajeDescuento*/, 
																	valorUnitarioDescuentoOPCIONAL /*valorUnitarioDescuento*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorUnitarioIva*/, 
																	requiereAutorizacion, 
																	"",/* numeroAutorizacionOPCIONAL -- numeroAutorizacion*/ 
																	ConstantesBD.codigoEstadoFPendiente/*estado*/, 
																	cubierto, 
																	tipoDistribucion /*tipoDistribucion*/, 
																	numeroSolicitud, 
																	ConstantesBD.codigoNuncaValido/*codigoServicio*/, 
																	codigoArticulo, 
																	ConstantesBD.codigoNuncaValido /*codigoServicioCx*/, 
																	ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, 
																	ConstantesBD.acronimoNo /*facturado*/, 
																	codigoTipoSolicitudOPCIONAL, 
																	paquetizado, 
																	cargoPadrePaquetes /*cargoPadre*/, 
																	codigoSolicitudSubcuenta, 
																	observaciones, 
																	loginUsuario, 
																	erroresCargo, 
																	codigoContrato,
																	ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																	ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/,
																	"" /*esPortatil*/,
																	"" /*dejarExcento*/,
																	codDetalleAutorizaciones,
																	codDetalleAutorizacionesEstancia,
																	0 /*porcentajeDctoPromocionServicio*/, 
																	BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																	0 /*porcentajeHonorarioPromocionServicio*/, 
																	BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																	
																	0/*programa*/,
																	0/*porcentajeDctoBono*/,
																	BigDecimal.ZERO/*valorDescuentoBono*/, 
																	0/*porcentajeDctoOdontologico*/, 
																	BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																	0 /*detallePaqueteOdonConvenio*/);
					
					if(codigosComponentesPaquetes.size()>0){
						Cargos.actualizarCargoPadreComponentesPaquetes(con, this.dtoDetalleCargo.getCodigoDetalleCargo(), codigosComponentesPaquetes);
					}
					if(this.dtoDetalleCargo.getCodigoDetalleCargo()<1){
						erroresCargo.setTieneErroresCodigo(true);
						return erroresCargo;
					}
					return erroresCargo;
				}
			}
			//7. CALCULAMOS LA EXCEPCION DE LA TARIFA y SU DESCUENTO 
			valorTarifaTotal=valorTarifaBase;
				if (!tarifaNoModificada) { //Si el valor fue modificado calcula el % total de la tarifa del articulo
			valorTarifaTotal=obtenerValorTarifaConExcepcionArticulo(con, valorTarifaOPCIONAL, valorTarifaTotal, valorTarifaBase, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoArticulo, codigoInstitucion, codigoEsquemaTarifarioOPCIONAL,fechaCalculoVigencia, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud));
				}
			if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario)&&valorTarifaTotal>0){
				valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
			}
			//obtenemos el descuento de la tarifa
			if(porcentajeDescuentoOPCIONAL<0 && valorUnitarioDescuentoOPCIONAL<0){	
				InfoTarifa descuentoTarifa= obtenerDescuentoComercialXConvenioArticulo(con, codigoViaIngreso, tipoPaciente, codigoContrato, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
				if(descuentoTarifa.getExiste()){
					if (descuentoTarifa.getPorcentajes().size()>0 && Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0)+"")>0){
						porcentajeDescuentoOPCIONAL=Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0));
						valorUnitarioDescuentoOPCIONAL= valorTarifaTotal*(porcentajeDescuentoOPCIONAL/100.0);
					}
					if (Utilidades.convertirADouble(descuentoTarifa.getValor())>0){
						valorUnitarioDescuentoOPCIONAL= Utilidades.convertirADouble(descuentoTarifa.getValor());
					}
					if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario)){
						valorUnitarioDescuentoOPCIONAL=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorUnitarioDescuentoOPCIONAL);
					}
				}
			}	
			//debemos validar que el valor del descuento no supere el valor del cargo total mas los recargos
			//ES UNITARIO X ESTA RAZON NO SE MULTIPLICA POR LA CANTIDAD
			if(valorUnitarioDescuentoOPCIONAL> (valorTarifaTotal)){
				valorUnitarioDescuentoOPCIONAL=(valorTarifaTotal);
			}
			//8. EVALUAMOS LAS INCLUSIONES - EXCLUSIONES
	
			InfoDatosString inclusionesExclusiones= obtenerInclusionExclusionXConvenioArticulo(con, codigoContrato, codigoCentroCostoSolicitante, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
			int estado=ConstantesBD.codigoEstadoFCargada;
			
			HashMap<String,Integer> datosCargoXPartes= new HashMap<String, Integer>();
			
			datosCargoXPartes.put("numRegistros", 1); 
	
			datosCargoXPartes.put("estado_0", estado);
			datosCargoXPartes.put("cantidadArticulos_0", cantidadArticuloOPCIONAL);
			
			//valida si el idicativo incluye es Si
			if(inclusionesExclusiones.getAcronimo().equals(ConstantesBD.acronimoSi)){
				
				//Verifica si existe cantidad - si existe se validan los cargos que existen para el paciente en el ingreso con convenio no halla superado el maximo
				//Valida que exista Valor en inclusionesExclusiones
				if(inclusionesExclusiones.getValueInt() != ConstantesBD.codigoNuncaValido){
					int cantidadInclusionesOrdenes = consultarCantidadInclusionesExclusionesOrdenArticulo( con, (int)codigoSubcuenta, codigoArticulo);
					
					//Valida que el Total de las inclusiones sea un valor valido
					if(cantidadInclusionesOrdenes != ConstantesBD.codigoNuncaValido){
						
						//Valida que la cantidad en inclusionesExclusiones sea mayor que la cantidad de Inclusiones en las ordenes
						if(inclusionesExclusiones.getValueInt() > cantidadInclusionesOrdenes){
							
							//Valida cantidad articulos en la orden sea valido
							if(cantidadArticuloOPCIONAL > 0){
								int totalInclusiones = cantidadInclusionesOrdenes + cantidadArticuloOPCIONAL;
								
								//Valida cantidad en inclusionesExclusiones sea mayor o igual la cantidad inclusiones en las ordenes mas las de la nueva orden
								if(inclusionesExclusiones.getValueInt() >= totalInclusiones){
									datosCargoXPartes.put("estado_0", ConstantesBD.codigoEstadoFExento);
								
								}else{
									//Se carga la orden por partes
									
									int solicitudSinExclusion = totalInclusiones - inclusionesExclusiones.getValueInt();
									int solicitudExclusion = cantidadArticuloOPCIONAL - solicitudSinExclusion;
									
									datosCargoXPartes.clear();
									datosCargoXPartes.put("numRegistros", 2); 
									
									datosCargoXPartes.put("estado_0", ConstantesBD.codigoEstadoFCargada);
									datosCargoXPartes.put("cantidadArticulos_0", solicitudSinExclusion);
									
									datosCargoXPartes.put("estado_1", ConstantesBD.codigoEstadoFExento);
									datosCargoXPartes.put("cantidadArticulos_1", solicitudExclusion);
	
								}
								
							}
							
						}
						
					}
									
				}else{
					//si no existe cantidad todos los articulos se van como excentos
					datosCargoXPartes.put("estado_0", ConstantesBD.codigoEstadoFExento);
				}
	
			}
			
				//Iteramos datosCargoXPartes x si la orden se carga por partes
				for(int i=0 ; i < datosCargoXPartes.get("numRegistros") ; i++ ){
					
					cantidadArticuloOPCIONAL = datosCargoXPartes.get("cantidadArticulos_"+i);
					estado = datosCargoXPartes.get("estado_"+i);
						
						//9. INSERTAMOS UN CARGO CORRECTO LO DEJAMOS EN ESTADO CARGADO
						if(insertarEnBD){
						this.dtoDetalleCargo= insertarDetalleCargo(	con, 
																		codigoSubcuenta, 
																		codigoConvenioBaseOPCIONAL, 
																		codigoEsquemaTarifarioOPCIONAL, 
																		cantidadArticuloOPCIONAL, 
																		valorTarifaTotal /*valorUnitarioTarifa*/, /////////***********************PREGUNTAR 
																		valorTarifaTotal /*valorUnitarioCargado*/, 
																		(cantidadArticuloOPCIONAL*valorTarifaTotal) /*valorTotalCargado*/, 
																		ConstantesBD.codigoNuncaValidoDouble /*porcentajecargado PERTENECE A LA DISTRIBUCION*/, 
																		ConstantesBD.codigoNuncaValidoDouble /*porcentajeRecargo*/, 
																		ConstantesBD.codigoNuncaValidoDouble /*valorUnitarioRecargo*/, 
																		porcentajeDescuentoOPCIONAL, 
																		valorUnitarioDescuentoOPCIONAL, 
																		ConstantesBD.codigoNuncaValidoDouble /*iva*/, 
																		requiereAutorizacion,
																		"",/* numeroAutorizacionOPCIONAL, ---numeroAutorizacion*/
																		estado, 
																		cubierto, 
																		tipoDistribucion, 
																		numeroSolicitud, 
																		ConstantesBD.codigoNuncaValido /*codigoServicio*/, 
																		codigoArticulo, 
																		ConstantesBD.codigoNuncaValido /*codigoServicioCx*/, 
																		ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, 
																		ConstantesBD.acronimoNo /*facturado*/, 
																		codigoTipoSolicitudOPCIONAL, 
																		paquetizado, 
																		cargoPadrePaquetes, 
																		codigoSolicitudSubcuenta, 
																		observaciones, 
																		loginUsuario, 
																		erroresCargo, 
																		codigoContrato,
																		ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																		ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/,
																		"" /*esPortatil*/,
																		"" /*dejarExcento*/,
																		codDetalleAutorizaciones,
																		codDetalleAutorizacionesEstancia,
																		0 /*porcentajeDctoPromocionServicio*/, 
																		BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																		0 /*porcentajeHonorarioPromocionServicio*/, 
																		BigDecimal.ZERO /*valorHonorarioPromocionServicio*/, 
																		
																		0/*programa*/,
																		0/*porcentajeDctoBono*/,
																		BigDecimal.ZERO/*valorDescuentoBono*/, 
																		0/*porcentajeDctoOdontologico*/, 
																		BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																		0 /*detallePaqueteOdonConvenio*/);
						
						if(codigosComponentesPaquetes.size()>0){
							Cargos.actualizarCargoPadreComponentesPaquetes(con, this.dtoDetalleCargo.getCodigoDetalleCargo(), codigosComponentesPaquetes);
						}
						if(this.dtoDetalleCargo.getCodigoDetalleCargo()<1){
							erroresCargo.setTieneErroresCodigo(true);
							return erroresCargo;
						}
					}	
					else{
						this.dtoDetalleCargo= new DtoDetalleCargo(	ConstantesBD.codigoNuncaValidoDouble, 
																	codigoSubcuenta, 
																	codigoConvenioBaseOPCIONAL, 
																	codigoEsquemaTarifarioOPCIONAL, 
																	cantidadArticuloOPCIONAL, 
																	valorTarifaTotal, 
																	valorTarifaTotal /*valorUnitarioCargado*/, 
																	(cantidadArticuloOPCIONAL*valorTarifaTotal) /*valorTotalCargado*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*porcentajecargado PERTENECE A LA DISTRIBUCION*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*porcentajeRecargo*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*valorUnitarioRecargo*/, 
																	porcentajeDescuentoOPCIONAL, 
																	valorUnitarioDescuentoOPCIONAL, 
																	ConstantesBD.codigoNuncaValidoDouble /*iva*/,
																	requiereAutorizacion,
																	"",/*numeroAutorizacionOPCIONAL -- numeroAutorizacion,*/  
																	estado, 
																	cubierto, 
																	tipoDistribucion, 
																	numeroSolicitud, 
																	ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, 
																	codigoArticulo /*codigoArticulo*/,
																	
																	//las solicitudes de cx nunca pueden quedar pendientes, ni siquiera en la distribucion x cantidad poprque siempre son cantidad=1
																	ConstantesBD.codigoNuncaValido /*codigoServicioCx*/, 
																	ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, 
																	ConstantesBD.acronimoNo /*facturado*/, 
																	codigoTipoSolicitudOPCIONAL, 
																	paquetizado, 
																	cargoPadrePaquetes, 
																	codigoSolicitudSubcuenta, 
																	observaciones, 
																	codigoContrato,
																	false /*filtrarSoloCantidadesMayoresCero*/,
																	ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																	ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/,
																	"" /*esPortatil*/,
																	"" /*dejarExcento*/,
																	0 /*porcentajeDctoPromocionServicio*/, 
																	BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
																	0 /*porcentajeHonorarioPromocionServicio*/, 
																	BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
																	
																	0/*programa*/,
																	0/*porcentajeDctoBono*/,
																	BigDecimal.ZERO/*valorDescuentoBono*/, 
																	0/*porcentajeDctoOdontologico*/, 
																	BigDecimal.ZERO/*valorDescuentoOdontologico*/,
																	0 /*detallePaqueteOdonConvenio*/);
						
					}
							
				}
		
			return erroresCargo;	
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
	 * @param subcuentas
	 * @param codigoEsquemaTarifario
	 * @param usuario
	 * @param codigoServicioArticulo
	 * @param esServicio
	 * @param numeroSolicitud
	 * @return
	 */
	public static double obtenerValorTarifaYExcepcion(Connection con, DtoSubCuentas subcuentas, int codigoEsquemaTarifario, UsuarioBasico usuario, int codigoServicioArticulo, boolean esServicio, int numeroSolicitud,String fechaCalculoVigenciaOPCIONAL ) throws IPSException
	{
		String idCuenta=Utilidades.getCuentaSolicitud(con, numeroSolicitud)+"";
		String metodoAjusteEsquemaTarifario = "";

		try {
			metodoAjusteEsquemaTarifario = obtenerMetodoAjuste(con, subcuentas.getConvenio().getCodigo(), codigoEsquemaTarifario, usuario.getCodigoInstitucionInt(), esServicio);
		
			//(con, codigoEsquemaTarifario, usuario.getCodigoInstitucionInt());
			int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
			
			// obtener tipo paciente asociado a la solicitud
			String tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
			
			
			//CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
			String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;
	
			
			
			if(esServicio)
			{	
				InfoTarifaVigente infoTarifaVigente= obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicioArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
				double valorTarifaBase=infoTarifaVigente.getValorTarifa();
				if(!infoTarifaVigente.isExiste())
				{
					return ConstantesBD.codigoNuncaValidoDouble;
				}
				
				//5. CALCULO VALOR TARIFA CON EXCEPCION
				//solo aplica la excepciï¿½n cuando la tarifa no viene como parametro opcional
				double valorTarifaTotal=valorTarifaBase;
				
				InfoTarifa excepcionTarifa= obtenerExcepcionesTarifasServicio(con, Cuenta.obtenerCodigoViaIngresoCuenta(con, idCuenta), tipoPaciente, Cuenta.obtenerTipoComplejidad(con, idCuenta), subcuentas.getContrato(), codigoServicioArticulo, usuario.getCodigoInstitucionInt(),fechaCalculoVigencia, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)); 
				if(excepcionTarifa.getExiste())
				{	
					valorTarifaTotal=asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
				}
					
				if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario))
				{	
					valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
				}
					
				return valorTarifaTotal;
			}
			else
			{
				double valorTarifaBase=obtenerTarifaBaseArticulo(con, codigoServicioArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
				if(valorTarifaBase<=0)
				{
					return ConstantesBD.codigoNuncaValidoDouble;
				}
				
				double valorTarifaTotal=valorTarifaBase;
				InfoTarifa excepcionTarifa= obtenerExcepcionesTarifasArticulo(con,  Cuenta.obtenerCodigoViaIngresoCuenta(con, idCuenta),tipoPaciente, Cuenta.obtenerTipoComplejidad(con, idCuenta), subcuentas.getContrato(), codigoServicioArticulo, usuario.getCodigoInstitucionInt(), codigoEsquemaTarifario,fechaCalculoVigencia,Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)); 
				if(excepcionTarifa.getExiste())
					valorTarifaTotal=asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
					
				if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario))
					valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
				
				return valorTarifaTotal;
			}
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
	 * @param convenio
	 * @param codigoEsquemaTarifario
	 * @param usuario
	 * @param codigoServicioArticulo
	 * @param esServicio
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoTarifaYExcepcion obtenerValorTarifaYExcepcion(Connection con, int convenio,int contrato, int codigoEsquemaTarifario, int institucion, int codigoServicioArticulo, boolean esServicio, String tipoPaciente,int viaIngreso,int tipoComplejidad,String fechaCalculoVigenciaOPCIONAL, int centroAtencion ) throws IPSException
	{
		String metodoAjusteEsquemaTarifario= obtenerMetodoAjuste(con, convenio, codigoEsquemaTarifario, institucion, esServicio);
		//(con, codigoEsquemaTarifario, usuario.getCodigoInstitucionInt());
		int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
		
		
		//CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
		String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;

		
		
		if(esServicio)
		{	
			InfoTarifaVigente infoTarifaVigente= obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicioArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
			double valorTarifaBase= infoTarifaVigente.getValorTarifa();
			if(!infoTarifaVigente.isExiste())
			{
				return new InfoTarifaYExcepcion(ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble);
			}
			
			//5. CALCULO VALOR TARIFA CON EXCEPCION
			//solo aplica la excepciï¿½n cuando la tarifa no viene como parametro opcional
			double valorTarifaTotal=valorTarifaBase;
			
			InfoTarifa excepcionTarifa= obtenerExcepcionesTarifasServicio(con, viaIngreso, tipoPaciente, tipoComplejidad, contrato, codigoServicioArticulo, institucion,fechaCalculoVigencia, centroAtencion); 
			if(excepcionTarifa.getExiste())
			{	
				valorTarifaTotal=asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
			}
			if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario))
			{	
				valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
			}	
			return new InfoTarifaYExcepcion(valorTarifaBase, valorTarifaTotal);
		}
		else
		{
			//System.out.print("\n .........0 articulo ................. ");
			double valorTarifaBase=obtenerTarifaBaseArticulo(con, codigoServicioArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
			//System.out.print("\n .........1 tarifa base ................. "+valorTarifaBase);
			if(valorTarifaBase<=0)
			{
				return new InfoTarifaYExcepcion(ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble);
			}
			
			double valorTarifaTotal=valorTarifaBase;
			
			InfoTarifa excepcionTarifa= obtenerExcepcionesTarifasArticulo(con,  viaIngreso,tipoPaciente, tipoComplejidad, contrato, codigoServicioArticulo, institucion, codigoEsquemaTarifario,fechaCalculoVigencia, centroAtencion);
			//System.out.print("\n .........1 valorTarifaTotal................. "+valorTarifaTotal+"    excepcionTarifa.getNuevaTarifa "+excepcionTarifa.getNuevaTarifa()+"    excepcionTarifa.getExiste "+excepcionTarifa.getExiste());
			
			if(excepcionTarifa.getExiste())
				valorTarifaTotal=asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
				
			if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario))
				valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
			
			//System.out.print("\n .........3 valorTarifaTotal................. "+valorTarifaTotal);
			
			return new InfoTarifaYExcepcion(valorTarifaBase, valorTarifaTotal);
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param codigoSubCuenta
	 * @return
	 */
	public static int obtenerTotalAdminFarmaciaXResponsable(Connection con, int codigoArticulo, int numeroSolicitud, double codigoSubcuenta) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerTotalAdminFarmaciaXResponsable(con, codigoArticulo, numeroSolicitud, codigoSubcuenta);
	}

	/***
	 * metodo que recalcula todos los cargos de la solicitud COMPLETA de medicamentos 
	 * este metodo aplica para el despacho de medicamentos cuando se una solicitud de mezcla o es depacho entrega dir paciente y finalizado  
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean recalcularCargoArticuloXSolicitudDespachoMedicamentos (Connection con, int numeroSolicitud, UsuarioBasico usuario,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	{
		Vector codigosArticulosACargar= obtenerCodigosArticulosXSolicitudACargar(con, numeroSolicitud);

		
		//		CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
		String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;

		logger.info("\n\n\n*********recalcularCargoArticuloXSolicitudDespachoMedicamentos");
		for(int w=0; w<codigosArticulosACargar.size(); w++)
		{
			logger.info("articulo->"+codigosArticulosACargar.get(w));
			int totalDespachadoACargar= Utilidades.getTotalAdminFarmacia(con, codigosArticulosACargar.get(w).toString(), numeroSolicitud+"", false);
			logger.info("total a cargar->"+totalDespachadoACargar);
			boolean inserto= recalcularCargoArticulo(con, numeroSolicitud, usuario, Integer.parseInt(codigosArticulosACargar.get(w).toString()), ConstantesBD.codigoNuncaValido /*subcuenta*/, totalDespachadoACargar, /*codigoEsquemaTarifarioOPCIONAL*/ ConstantesBD.codigoNuncaValido, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/,fechaCalculoVigencia);
			if(!inserto)
			{
				logger.error("ERROR NO RECALCULO DESDE EL DESPACHO EL CARGO!!!! numeroSolicitu->"+numeroSolicitud);
				return false;
			}	
		}
		return true;
	}
	
	/**
	 * metodo para recalar el cargo de una solicitud de medicamentos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param usuario
	 * @param paciente
	 * @parma observaciones
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean recalcularCargoArticuloXMedicamentoAdmin (Connection con, int numeroSolicitud, int codigoArticulo, UsuarioBasico usuario, PersonaBasica paciente, String observaciones,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	{
		
		logger.info("\n\n****************************************************************************************************************************************************************************************************");
		logger.info("LLEGA A RECALCULAR CARGO DEL ARTICULO X MEDICAMENTO ADMINISTRADO!!!!!!!");
		logger.info("****************************************************************************************************************************************************************************************************\n\n");
		
		//en este punto puede que el medicamento halla sido distribuido por cantidad, entonces tenemos que evaluar la cobertura nuevamente y 
		//actualizar las solicitudes_subcuenta y el det_cargos
	
		//obtenemos el tipo paciente asociado a la solicitud 
		String tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
		int	codigoCuenta= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
		logger.info("tipo Paciente->"+tipoPaciente+" cuenta->"+codigoCuenta+" via->"+codigoViaIngreso);
		
		//CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
		String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;
		logger.info("fechaCalculoVigencia->"+fechaCalculoVigencia);
		
		DtoDetalleCargo criteriosBusquedaDtoDetalleCargo= new DtoDetalleCargo();
		criteriosBusquedaDtoDetalleCargo.setNumeroSolicitud(numeroSolicitud);
		criteriosBusquedaDtoDetalleCargo.setFacturado(ConstantesBD.acronimoNo);
		criteriosBusquedaDtoDetalleCargo.setFiltrarSoloCantidadesMayoresCero(false);
		criteriosBusquedaDtoDetalleCargo.setCodigoArticulo(codigoArticulo);
		ArrayList<DtoDetalleCargo> arrayDtoDetalleCargo=cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
		
		int numeroResponsables= obtenerNumeroResponsablesSolicitudCargo(con, numeroSolicitud, codigoArticulo, false);
		int totalAdministradoACargar = Utilidades.getTotalAdminFarmacia(con, codigoArticulo+"", numeroSolicitud+"", false);
		InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", codigoViaIngreso, tipoPaciente, codigoArticulo, usuario.getCodigoInstitucionInt(),false);
		logger.info("NUMERO DE RESPONSABLES DESDE EL RE-CALCULO DE CARGO SOL MED ADMIN  -->"+numeroResponsables);
		
		if(numeroResponsables==1 && arrayDtoDetalleCargo.size()==1)
		{
			logger.info("totalAdministradoACargar->"+totalAdministradoACargar);
			logger.info("ingreso->"+paciente.getCodigoIngreso()+""+" cod via ingreso->"+codigoViaIngreso+" tipo pac->"+tipoPaciente+" cod articulo->"+codigoArticulo);
			logger.info("subcuenta-->"+infoResponsableCobertura.getDtoSubCuenta().getSubCuenta());
		    
		    //si sigue siendo el mismo responsable entonces recalculamos
		    if(infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()==arrayDtoDetalleCargo.get(0).getCodigoSubcuenta())
		    {
			    int codigoEsquemaTarifario= Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"", infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo, false,fechaCalculoVigenciaOPCIONAL, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud));
				boolean inserto= recalcularCargoArticulo(con, numeroSolicitud, usuario, codigoArticulo, infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble(), totalAdministradoACargar, codigoEsquemaTarifario, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/,fechaCalculoVigencia);
				if(!inserto)
				{
					logger.error("ERROR NO RECALCULO DESDE ADMIN DEL CARGO!!!! numeroSolicitu->"+numeroSolicitud+" codArt->"+codigoArticulo);
					return false;
				}
		    }
		    //si es otro responsable se debe eliminar el actual e insertar el nuevo
		    else
		    {
		    	//primero borramos el detalle del cargo
		    	//@todo VERIFICAR
		    	eliminarDetalleCargoYErroresYArtConsumoXCodigoDetalleCargo(con, arrayDtoDetalleCargo.get(0).getCodigoDetalleCargo());
		    	
		    	//segundo borramos la solicitud de subcuenta
		    	DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta= new DtoSolicitudesSubCuenta();
		    	dtoSolicitudesSubCuenta.setCodigo(arrayDtoDetalleCargo.get(0).getCodigoSolicitudSubCuenta()+"");
		    	Solicitud.eliminarSolicitudSubCuenta(con, dtoSolicitudesSubCuenta);
		    	
		    	//tercero insertamos el nuevo responsable
		    	int codigoTipoSolicitud= Solicitud.getCodigoTipoSolicitud(con, numeroSolicitud+"");
		    	
				double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(	con, 
																					numeroSolicitud, 
																					codigoCuenta, 
																					infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),
																					""/*codigoServicio*/, 
																					codigoArticulo+"", 
																					totalAdministradoACargar /*cantidadArticuloSolicitada*/, 
																					infoResponsableCobertura.getInfoCobertura().getIncluidoStr(), 
																					codigoTipoSolicitud/*codigoTipoSolicitud*/,
																					"" /*codigoServicioCx*/, 
																					ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/,
																					ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad,
																					usuario.getLoginUsuario(),
																					ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																	    			ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
			    logger.info("\n\nSOLICITUD SUBCUENTA INSERTADO-->"+solicitudSubCuenta+"\n\n");
			    
			    if(solicitudSubCuenta<1)
			    {
			    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
			    	return false;
			    }
			    
			    Cargos cargoArticulos= new Cargos();
			    this.setInfoErroresCargo(	cargoArticulos.generarCargoArticulo	(	con, 
							    		    										false,/*dejarPendiente*/ 
							    		    										numeroSolicitud, 
							    		    										infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
							    		    										codigoArticulo, 
							    		    										codigoViaIngreso, 
							    		    										Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"")/*codigoTipoComplejidad*/, 
							    		    										usuario.getCodigoInstitucionInt(), 
							    		    										infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()/*codigoSubcuenta*/, 
							    		    										solicitudSubCuenta/*codigoSolicitudSubcuenta*/, 
							    		    										infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
							    		    										ConstantesBD.acronimoNo/*paquetizado*/, 
							    		    										ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
							    		    										infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr()/*requiereAutorizacion*/,
							    		    										ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad/*tipoDistribucion*/,
							    		    										infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioArticuloPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo,fechaCalculoVigencia, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)) /*codigoEsquemaTarifarioOPCIONAL*/, 
							    		    										infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioBaseOPCIONAL*/,
							    		    										ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/,
							    		    										codigoTipoSolicitud /*codigoTipoSolicitudOPCIONAL*/,
							    		    										ConstantesBD.codigoNuncaValido /*codigoCentroCostoSolicitanteOPCIONAL /*OPCIONAL*/,
							    		    										totalAdministradoACargar /*cantidadArticuloSolicitadaOPCIONAL*/,
							    		    										/*"" -- numeroAutorizacionOPCIONAL ,*/
							    		    										-1 /*porcentajeDescuentoOPCIONAL*/,
							    		    										-1 /*valorDescuentoOPCIONAL*/,
							    		    										usuario.getLoginUsuario(), 
							    		    										observaciones /*observaciones*/,
							    		    										true /*esRegistroNuevo*/,
							    		    										true /*insertarEnBD*/,
							    		    										false /*cancelarInsercionSiExisteError*/,
							    		    										fechaCalculoVigencia, 
							    		    										false /*tarifaNoModificada*/));
		    	
		    }
		    
		}
		else if(numeroResponsables>1)
		{
			//en este punto tenmemos que reevaluar la cobertura y verificar que el detalle cargo exista con ese responsable
			//cargamos los responsables de la DISTRIBUCION X CANTIDAD	de ese articulo -solicitud NO FACTURADOS
			//se hace la suma de todas las cantidades distribuidas para luego restarle el total administrado y saber cuanto le corresponde a ese responsable
			int cantidadAdministradaTotalDistribuida=obtenerCantidadesDistribuidasAdminMed(con, numeroSolicitud, codigoArticulo);
			int cantidadAdministradaTotalXSolicitud= Utilidades.getTotalAdminFarmacia(con, codigoArticulo+"", numeroSolicitud+"", false);
			int cantidadACargarSinDistribuir= cantidadAdministradaTotalXSolicitud - cantidadAdministradaTotalDistribuida;
			
			boolean existeResponsable=false;
			
			DtoDetalleCargo dtoDetalleCargo= new DtoDetalleCargo();
			for(int w=0; w<arrayDtoDetalleCargo.size();w++)
			{
				if(infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()==arrayDtoDetalleCargo.get(w).getCodigoSubcuenta())
				{
					dtoDetalleCargo= arrayDtoDetalleCargo.get(w);
					existeResponsable=true;
				}
			}
			
			if(existeResponsable)
			{	
				int cantidadACargarXResponsable= dtoDetalleCargo.getCantidadCargada() + cantidadACargarSinDistribuir;
				boolean inserto= recalcularCargoArticulo(con, numeroSolicitud, usuario, codigoArticulo, dtoDetalleCargo.getCodigoSubcuenta() /*subCuenta*/, cantidadACargarXResponsable, /*codigoEsquemaTarifarioOPCIONAL*/ ConstantesBD.codigoNuncaValido, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/,fechaCalculoVigencia);	
				
				if(!inserto)
				{
					logger.error("NO ACTUALIZO EL RESPONSABLES "+dtoDetalleCargo.getCodigoConvenio()+" VERIFICAR!!!!! subcuenta-> "+dtoDetalleCargo.getCodigoSubcuenta());
					return false;
				}
			}
			//de lo contrario debe insertar una nueva solicitud de sub_cuenta y det_cargos y la cantidad es cantidadACargarSinDistribuir
			else
			{
				int codigoTipoSolicitud= Solicitud.getCodigoTipoSolicitud(con, numeroSolicitud+"");
				double solicitudSubCuenta = Solicitud.insertarSolicitudSubCuenta(	con, numeroSolicitud, codigoCuenta, 
																					infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),
																					""/*codigoServicio*/, 
																					codigoArticulo+"", cantidadACargarSinDistribuir /*cantidadArticuloSolicitada*/, 
																					infoResponsableCobertura.getInfoCobertura().getIncluidoStr(), 
																					codigoTipoSolicitud/*codigoTipoSolicitud*/,
																					"" /*codigoServicioCx*/, 
																					ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/,
																					ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad,
																					usuario.getLoginUsuario(),
																					ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																	    			ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/);
			    logger.info("\n\nSOLICITUD SUBCUENTA INSERTADO-->"+solicitudSubCuenta+"\n\n");
			    
			    if(solicitudSubCuenta<1)
			    {
			    	logger.error("NO INSERTO LA SOLICITUD DE SUB CUENTA");
			    	return false;
			    }
			    
			    Cargos cargoArticulos= new Cargos();
			    this.setInfoErroresCargo(	cargoArticulos.generarCargoArticulo	(	con, 
							    		    										false,/*dejarPendiente*/ 
							    		    										numeroSolicitud, 
							    		    										infoResponsableCobertura.getDtoSubCuenta().getContrato()/*codigoContrato*/, 
							    		    										codigoArticulo, 
							    		    										codigoViaIngreso, 
							    		    										Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"")/*codigoTipoComplejidad*/, 
							    		    										usuario.getCodigoInstitucionInt(), 
							    		    										infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble()/*codigoSubcuenta*/, 
							    		    										solicitudSubCuenta/*codigoSolicitudSubcuenta*/, 
							    		    										infoResponsableCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
							    		    										ConstantesBD.acronimoNo/*paquetizado*/, 
							    		    										ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
							    		    										infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacionStr()/*requiereAutorizacion*/,
							    		    										ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad/*tipoDistribucion*/,
							    		    										infoResponsableCobertura.getDtoSubCuenta().getEsquemaTarifarioArticuloPpalOoriginal(con,infoResponsableCobertura.getDtoSubCuenta().getSubCuenta(),infoResponsableCobertura.getDtoSubCuenta().getContrato(),codigoArticulo,fechaCalculoVigencia, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud)) /*codigoEsquemaTarifarioOPCIONAL*/, 
							    		    										infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo() /*codigoConvenioBaseOPCIONAL*/,
							    		    										ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/,
							    		    										codigoTipoSolicitud /*codigoTipoSolicitudOPCIONAL*/,
							    		    										ConstantesBD.codigoNuncaValido /*codigoCentroCostoSolicitanteOPCIONAL /*OPCIONAL*/,
							    		    										cantidadACargarSinDistribuir /*cantidadArticuloSolicitadaOPCIONAL*/,
							    		    										/*"" -- numeroAutorizacionOPCIONAL ,*/
							    		    										-1 /*porcentajeDescuentoOPCIONAL*/,
							    		    										-1 /*valorDescuentoOPCIONAL*/,
							    		    										usuario.getLoginUsuario(), 
							    		    										observaciones /*observaciones*/,
							    		    										true /*esRegistroNuevo*/,
							    		    										true /*insertarEnBD*/,
							    		    										false /*cancelarInsercionSiExisteError*/,
							    		    										fechaCalculoVigencia,
							    		    										false /*tarifaNoModificada*/));
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	private int obtenerCantidadesDistribuidasAdminMed(Connection con, int numeroSolicitud, int codigoArticulo) throws IPSException
	{
		int cantidad=0;
		ArrayList<DtoDetalleCargo> dtoDetalleCargoVector= new ArrayList<DtoDetalleCargo>();
		DtoDetalleCargo criteriosBusquedaDto= new DtoDetalleCargo();
		criteriosBusquedaDto.setNumeroSolicitud(numeroSolicitud);
		criteriosBusquedaDto.setCodigoArticulo(codigoArticulo);
		dtoDetalleCargoVector= cargarDetalleCargos(con, criteriosBusquedaDto);
		for(int w=0; w<dtoDetalleCargoVector.size(); w++)
			cantidad+=dtoDetalleCargoVector.get(w).getCantidadCargada();
		return cantidad;
	}

	/**
	 * * metodo para recalcular todos los cargos de los servicios pendientes- cargados de una solicitud de medicamentos
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param codigoArticuloOPCIONAL
	 * @param subCuentaOPCIONAL
	 * @param cantidadArticuloOPCIONAL
	 * @return
	 */
	public boolean recalcularCargoArticulo (Connection con, int numeroSolicitud, UsuarioBasico usuario, int codigoArticuloOPCIONAL, double subCuentaOPCIONAL, int cantidadArticuloOPCIONAL, int codigoEsquemaTarifarioOPCIONAL, boolean filtrarSoloCantidadesMayoresCero, String esComponentePaquete,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	{
		logger.info("FECHA CALCULO ------------>"+fechaCalculoVigenciaOPCIONAL);
		fechaCalculoVigenciaOPCIONAL= UtilidadFecha.obtenerFechaSinHora(fechaCalculoVigenciaOPCIONAL);
		logger.info("FECHA CALCULO despues ------------>"+fechaCalculoVigenciaOPCIONAL);
		//Vector codigosArticulosACargar= obtenerCodigosArticulosXSolicitudACargar(con, numeroSolicitud);
		logger.info("\n\n\n*********************LLEGA A RECALCULAR CARGO DE LA SOLICITUD de ARTICULOS "+numeroSolicitud+"************************");
		
		DtoDetalleCargo criteriosBusquedaDtoDetalleCargo= new DtoDetalleCargo();
		criteriosBusquedaDtoDetalleCargo.setNumeroSolicitud(numeroSolicitud);
		criteriosBusquedaDtoDetalleCargo.setFacturado(ConstantesBD.acronimoNo);
		criteriosBusquedaDtoDetalleCargo.setFiltrarSoloCantidadesMayoresCero(filtrarSoloCantidadesMayoresCero);
		criteriosBusquedaDtoDetalleCargo.setPaquetizado(esComponentePaquete);
		
		if(subCuentaOPCIONAL>0)
			criteriosBusquedaDtoDetalleCargo.setCodigoSubcuenta(subCuentaOPCIONAL);
		if(codigoArticuloOPCIONAL>0)
			criteriosBusquedaDtoDetalleCargo.setCodigoArticulo(codigoArticuloOPCIONAL);
		ArrayList<DtoDetalleCargo> arrayDtoDetalleCargo=cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
	
		int codigoCuenta= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
		
		if(UtilidadTexto.isEmpty(esComponentePaquete))
			esComponentePaquete=ConstantesBD.acronimoNo;
		
		for(int w=0; w<arrayDtoDetalleCargo.size(); w++)
		{
			try
		    {
				DtoSubCuentas dtoSubCuenta= UtilidadesHistoriaClinica.obtenerResponsable(con, (int)arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				logger.info("responsable------->"+dtoSubCuenta.getConvenio().getCodigo());
				logger.info("subCuenta------->"+arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				logger.info("DetallesCargo(0).getCubierto()->"+arrayDtoDetalleCargo.get(w).getCubierto());
				logger.info("solicitud subcuenta->"+arrayDtoDetalleCargo.get(w).getCodigoSolicitudSubCuenta());
				logger.info("subcuenta->"+arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				
				/*if(codigoEsquemaTarifarioOPCIONAL>0)
					arrayDtoDetalleCargo.get(w).setCodigoEsquemaTarifario(codigoEsquemaTarifarioOPCIONAL);*/
				
				
				this.setInfoErroresCargo(	generarCargoArticulo(	con, 
																	false /*dejarPendiente*/, 
																	numeroSolicitud, 
																	dtoSubCuenta.getContrato(), 
																	arrayDtoDetalleCargo.get(w).getCodigoArticulo(), 
																	codigoViaIngreso, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt()/*codigoInstitucion*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoSubcuenta() /*codigoSubcuenta*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoSolicitudSubCuenta(), 
																	arrayDtoDetalleCargo.get(w).getCubierto()/*cubierto*/, 
																	esComponentePaquete/*paquetizado*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	arrayDtoDetalleCargo.get(w).getRequiereAutorizacion() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad,
																	codigoEsquemaTarifarioOPCIONAL/*codigoEsquemaTarifarioServicioOPCIONAL*/, 
																	dtoSubCuenta.getConvenio().getCodigo()/*codigoConvenioOPCIONAL*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOpcional*/,
																	ConstantesBD.codigoNuncaValido /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido /*codigoCentroCostoSolicitanteOPCIONAL*/, 
																	cantidadArticuloOPCIONAL /*cantidadArticuloOPCIONAL*/, 
																	/*"" -- numeroAutorizacionOPCIONAL ,*/
																	-1 /*porcentajeDescuentoOPCIONAL*/,
																	-1 /*valorUnitarioDescuentoOPCIONAL*/,
																	usuario.getLoginUsuario(), 
																	"" /*observaciones*/,
																	false /*esRegistroNuevo*/,
																	true /*insertarEnBD*/,
			    		    										false /*cancelarInsercionSiExisteError*/,
			    		    										fechaCalculoVigenciaOPCIONAL, 
			    		    										false /*tarifaNoModificada*/));
				    
			}
		    catch(Exception e)
		    {
		        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
		        e.printStackTrace();
				return false;
		    }
		}    
	    return true;
	}
	
	
	/**
	 * * metodo para recalcular todos los cargos de los servicios pendientes- cargados de una solicitud de medicamentos
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param codigoArticuloOPCIONAL
	 * @param subCuentaOPCIONAL
	 * @param cantidadArticuloOPCIONAL
	 * @return
	 */
	public boolean recalcularCargoArticuloValidandoCobertura (Connection con, int numeroSolicitud,int ingreso, UsuarioBasico usuario, int codigoArticuloOPCIONAL, double subCuentaOPCIONAL, int cantidadArticuloOPCIONAL, int codigoEsquemaTarifarioOPCIONAL, boolean filtrarSoloCantidadesMayoresCero, String esComponentePaquete,String fechaCalculoVigenciaOPCIONAL) throws IPSException
	{
		//Vector codigosArticulosACargar= obtenerCodigosArticulosXSolicitudACargar(con, numeroSolicitud);
		logger.info("\n\n\n*********************LLEGA A RECALCULAR CARGO DE LA SOLICITUD de ARTICULOS "+numeroSolicitud+"************************");

		//CARGAMOS LA FECHA PARA EL CACULO DE VIGENCIAS
		String fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;

		DtoDetalleCargo criteriosBusquedaDtoDetalleCargo= new DtoDetalleCargo();
		criteriosBusquedaDtoDetalleCargo.setNumeroSolicitud(numeroSolicitud);
		criteriosBusquedaDtoDetalleCargo.setFacturado(ConstantesBD.acronimoNo);
		criteriosBusquedaDtoDetalleCargo.setFiltrarSoloCantidadesMayoresCero(filtrarSoloCantidadesMayoresCero);
		criteriosBusquedaDtoDetalleCargo.setPaquetizado(esComponentePaquete);
		
		if(subCuentaOPCIONAL>0)
			criteriosBusquedaDtoDetalleCargo.setCodigoSubcuenta(subCuentaOPCIONAL);
		if(codigoArticuloOPCIONAL>0)
			criteriosBusquedaDtoDetalleCargo.setCodigoArticulo(codigoArticuloOPCIONAL);
		ArrayList<DtoDetalleCargo> arrayDtoDetalleCargo=cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
		
		String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
		int codigoCuenta= Utilidades.getCuentaSolicitud(con, numeroSolicitud);
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
		
		if(UtilidadTexto.isEmpty(esComponentePaquete))
			esComponentePaquete=ConstantesBD.acronimoNo;
		
		for(int w=0; w<arrayDtoDetalleCargo.size(); w++)
		{
			try
		    {
				DtoSubCuentas dtoSubCuenta= UtilidadesHistoriaClinica.obtenerResponsable(con, (int)arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				
				logger.info("responsable------->"+dtoSubCuenta.getConvenio().getCodigo());
				logger.info("subcuenta------->"+arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				logger.info("DetallesCargo(0).getCubierto()->"+arrayDtoDetalleCargo.get(w).getCubierto());
				logger.info("solicitud subcuenta->"+arrayDtoDetalleCargo.get(w).getCodigoSolicitudSubCuenta());
				logger.info("subcuenta->"+arrayDtoDetalleCargo.get(w).getCodigoSubcuenta());
				
				if(codigoEsquemaTarifarioOPCIONAL>0)
					arrayDtoDetalleCargo.get(w).setCodigoEsquemaTarifario(codigoEsquemaTarifarioOPCIONAL);
				
				InfoResponsableCobertura infoCobertura=new InfoResponsableCobertura();
				infoCobertura=Cobertura.validacionCoberturaArticulo(con,ingreso+"", codigoViaIngreso, tipoPaciente, arrayDtoDetalleCargo.get(w).getCodigoArticulo(), usuario.getCodigoInstitucionInt(), Utilidades.esSolicitudPYP(con, numeroSolicitud));
				
				this.setInfoErroresCargo(	generarCargoArticulo(	con, 
																	false /*dejarPendiente*/, 
																	numeroSolicitud, 
																	dtoSubCuenta.getContrato(), 
																	arrayDtoDetalleCargo.get(w).getCodigoArticulo(), 
																	codigoViaIngreso, 
																	Cuenta.obtenerTipoComplejidad(con, codigoCuenta+"") /*codigoTipoComplejidad*/, 
																	usuario.getCodigoInstitucionInt()/*codigoInstitucion*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoSubcuenta() /*codigoSubcuenta*/, 
																	arrayDtoDetalleCargo.get(w).getCodigoSolicitudSubCuenta(), 
																	infoCobertura.getInfoCobertura().getIncluidoStr()/*cubierto*/, 
																	esComponentePaquete/*paquetizado*/, 
																	ConstantesBD.codigoNuncaValidoDouble /*codigoPadrePaquetes*/, 
																	infoCobertura.getInfoCobertura().getRequiereAutorizacionStr() /*requiereAutorizacion*/,
																	ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad,
																	ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioServicioOPCIONAL*/, 
																	dtoSubCuenta.getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
																	ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOpcional*/,
																	ConstantesBD.codigoNuncaValido /*codigoTipoSolicitudOPCIONAL*/,
																	ConstantesBD.codigoNuncaValido /*codigoCentroCostoSolicitanteOPCIONAL*/, 
																	cantidadArticuloOPCIONAL /*cantidadArticuloOPCIONAL*/, 
																	/*"" -- numeroAutorizacionOPCIONAL ,*/
																	-1 /*porcentajeDescuentoOPCIONAL*/,
																	-1 /*valorUnitarioDescuentoOPCIONAL*/,
																	usuario.getLoginUsuario(), 
																	"" /*observaciones*/,
																	false /*esRegistroNuevo*/,
																	true /*insertarEnBD*/,
																	false /*cancelarInsercionSiExisteError*/,
																	fechaCalculoVigencia,
																	false /*tarifaNoModificada*/));
				    
			}
		    catch(Exception e)
		    {
		        logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud+" "+e);
		        e.printStackTrace();
				return false;
		    }
		}    
	    return true;
	}
	
	
	/**
	 * metodo que obtiene los cargos en estado pendiente - cargado para poderles posteriormente recalcular los cargos  
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Vector obtenerCodigosArticulosXSolicitudACargar (Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCodigosArticulosXSolicitudACargar(con, numeroSolicitud);
	}
	
	
	/**
	 * metodo que obtiene el valor de la tarifa con la excepcion aplicada
	 * @param con
	 * @param valorTarifaOPCIONAL
	 * @param valorTarifaTotal
	 * @param valorTarifaBase
	 * @param codigoViaIngreso
	 * @param codigoTipoComplejidad
	 * @param codigoContrato
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	private double obtenerValorTarifaConExcepcionArticulo(Connection con, double valorTarifaOPCIONAL, double valorTarifaTotal, double valorTarifaBase, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoArticulo, int codigoInstitucion, int codigoEsquemaTarifario,String fechaCalculoVigencia, int centroAtencion) throws IPSException
	{
		try{
			InfoTarifa excepcionTarifa= obtenerExcepcionesTarifasArticulo(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoArticulo, codigoInstitucion, codigoEsquemaTarifario,fechaCalculoVigencia, centroAtencion); 
			if(excepcionTarifa.getExiste()) {
				valorTarifaTotal=asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
			} 
			
				Log4JManager.info("valor tarifa (despues excepciones)->"+valorTarifaTotal);
			return valorTarifaTotal;
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
	 * M&eacute;todo que carga la informaci&oacute;n del detalle de cargo dato el mismo dto como criterio de b&uacute;squeda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public static ArrayList<DtoDetalleCargo> cargarDetalleCargos(Connection con, DtoDetalleCargo criteriosBusquedaDtoDetalleCargo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
	}
	
	public static ArrayList<DtoDetalleCargo> cargarDetalleCargos(DtoDetalleCargo criteriosBusquedaDtoDetalleCargo) throws IPSException
	{
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoDetalleCargo> resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * metodo para actauliza las cantidades del cargo y con un trigger actualizamos las cantidades de la sub cuenta
	 * @param con
	 * @param cantidad
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param esServicio
	 * @return
	 */
	public static boolean updateCantidadesCargo(Connection con, int cantidad, int numeroSolicitud, double subCuenta, int codigoServicioArticulo, boolean esServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().updateCantidadesCargo(con, cantidad, numeroSolicitud, subCuenta, codigoServicioArticulo, esServicio);
	}
	
	/**
	 * actualizar el detalle del cargo
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean updateDetalleCargo(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().updateDetalleCargo(con, detalleCargo, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoTipoComplejidad
	 * @param erroresCargo
	 * @return
	 */
	private InfoErroresCargo asignarErroresTipoComplejidad(Connection con, int codigoConvenio, int codigoTipoComplejidad, InfoErroresCargo erroresCargo) throws IPSException
	{
		if(Convenio.convenioManejaComplejidad(con, codigoConvenio) && codigoTipoComplejidad<=0)
			erroresCargo.setMensajesErrorDetalle("error.tipoComplejidad.noExiste");
		return erroresCargo;
	}	

	/**
	 * metodo para asignar el tipo de solicitud
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudOPCIONAL
	 * @param con
	 * @return
	 */
	private int asignarTipoSolicitud(int numeroSolicitud, int codigoTipoSolicitudOPCIONAL, Connection con ) throws IPSException
	{
		if(codigoTipoSolicitudOPCIONAL<1){
			try {
				return Solicitud.getCodigoTipoSolicitud(con, numeroSolicitud+"");
			}
			catch (IPSException ipsme) {
				throw ipsme;
			}
			catch (Exception e) {
				Log4JManager.error(e.getMessage(),e);
				throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
			}
		}	
		else
			return codigoTipoSolicitudOPCIONAL;
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param codigoCentroCostoSolicitanteOPCIONAL
	 * @param con
	 * @return
	 */
	private int obtenerCodigoCentroCostoSolicitante(int numeroSolicitud, int codigoCentroCostoSolicitanteOPCIONAL, Connection con) throws IPSException
	{
		if(codigoCentroCostoSolicitanteOPCIONAL<1){
			try {
				return Solicitud.obtenerCodigoCentroCostoSolicitante(con, numeroSolicitud+"");
			} 
			catch (IPSException ipsme) {
				throw ipsme;
			}
			catch (Exception e) {
				Log4JManager.error(e.getMessage(),e);
				throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
			}
		}	
		else
			return codigoCentroCostoSolicitanteOPCIONAL;
	}
	
	/**
	 * metodo que obtiene el valor tarifa con excepcion
	 * @param con
	 * @param valorTarifaOPCIONAL
	 * @param valorTarifaTotal
	 * @param valorTarifaBase
	 * @param codigoViaIngreso
	 * @param codigoTipoComplejidad
	 * @param codigoContratoDetalle
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings("unused")
	private double obtenerValorTarifaConExcepcion(Connection con, double valorTarifaOPCIONAL, double valorTarifaTotal, double valorTarifaBase, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContratoDetalle, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia, int centroAtencion) throws IPSException
	{
		if(valorTarifaOPCIONAL<=0)
		{	
			//2.2.6.1 obtenemos el objeto InfoTarifa de la excepcion
			InfoTarifa excepcionTarifa= obtenerExcepcionesTarifasServicio(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContratoDetalle, codigoServicio, codigoInstitucion,fechaCalculoVigencia, centroAtencion); 
			if(excepcionTarifa.getExiste())
				valorTarifaTotal=asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
		}	
		else
		{
			valorTarifaTotal=valorTarifaOPCIONAL;
		}
		logger.info("valor tarifa (despues excepciones)->"+valorTarifaTotal);
		return valorTarifaTotal;
	}

	/**
	 * metodo que asigna el valor tarifa con la excepcion
	 * @param excepcionTarifa
	 * @param valorTarifaTotal
	 * @param valorTarifaBase
	 */
	public static double asignarValorTarifaConExcepcion(InfoTarifa excepcionTarifa, double valorTarifaTotal, double valorTarifaBase) 
	{
		if(!UtilidadTexto.isEmpty(excepcionTarifa.getNuevaTarifa()))
		{
			valorTarifaTotal= Double.parseDouble( excepcionTarifa.getNuevaTarifa() );
		}	
		else if(excepcionTarifa.getPorcentajes().size()>0)
		{	
			for(int w=0; w<excepcionTarifa.getPorcentajes().size(); w++)
			{	
				valorTarifaTotal= valorTarifaBase * (1.0+( Double.parseDouble(excepcionTarifa.getPorcentajes().get(w))/100.0));
				valorTarifaBase=valorTarifaTotal;
			}	
		}	
		else if(!UtilidadTexto.isEmpty(excepcionTarifa.getValor()))
		{	
			valorTarifaTotal+=Double.parseDouble(excepcionTarifa.getValor());
		}	
		return valorTarifaTotal;
	}

	/**
	 * metodo que asigna los errores de tarifas ISS - SOAT
	 * @param codigoTipoTarifario
	 * @param erroresCargo
	 * @param codigoServicio
	 */
	private InfoErroresCargo asignarErroresTarifas_ISS_Soat(int codigoTipoTarifario, InfoErroresCargo erroresCargo, int codigoServicio) 
	{
		if (codigoTipoTarifario==ConstantesBD.codigoTarifarioISS)
			erroresCargo.setMensajesErrorDetalle("error.cargo.noHayTarifaEsquemaTarifarioISSCita"+ConstantesBD.separadorTags+codigoServicio);
		else if (codigoTipoTarifario==ConstantesBD.codigoTarifarioSoat)	
			erroresCargo.setMensajesErrorDetalle("error.cargo.noHayTarifaEsquemaTarifarioSoatCita"+ConstantesBD.separadorTags+codigoServicio);
		return erroresCargo;
	}

	/**
	 * metodo que asigna los errores de servicio a errores cargo
	 * @param codigoServicio
	 * @param erroresCargo
	 */
	private InfoErroresCargo asignarErroresServicio(int codigoServicio, InfoErroresCargo erroresCargo) 
	{
		if (codigoServicio<=ConstantesBD.codigoServicioNoDefinido)
		{
		    logger.info("error cargo no existe servicio para la solicitud");
			erroresCargo.setMensajesErrorDetalle("error.cargo.noSeEspecificoServicio");
		}
		//********** EVALUAMOS QUE EL CONTRATO NO ESTE VENCIDO ****************
		//SEGUN MARGARITA - NURY ESTO YA NO DEBE HACER
		/*if(Contrato.estaContratoVencido(con, codigoContratoBase))
		{
			logger.info("error contrato vencido codcontrato->"+codigoContratoBase+" numeroSolicitud="+numeroSolicitud);
			erroresCargo.setMensajesErrorEncabezado("error.cargo.contratoVencido");
			erroresCargo.setMensajesErrorDetalle("error.cargo.contratoVencido");
		}*/
		return erroresCargo;
	}

	/**
	 * metod que asigna los errores x esquema tarifario
	 * @param codigoEsquemaTarifario
	 * @param erroresCargo
	 */
	private InfoErroresCargo asignarErroresEsquemaTarifario(int codigoEsquemaTarifario, InfoErroresCargo erroresCargo)
	{
		if (codigoEsquemaTarifario<=0)
		{
		    logger.info("error esquema tar serv");
		    erroresCargo.setMensajesErrorDetalle("error.cargo.esquemaNoSeleccionado");
		}
		return erroresCargo;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicioOPCIONAL
	 * @param codigoTipoSolicitud
	 * @param numeroSolicitud
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	private int obtenerCodigoServicio(Connection con, int codigoServicioOPCIONAL, int codigoTipoSolicitud, int numeroSolicitud, int codigoEvolucionOPCIONAL, String esPortatil) throws IPSException
	{
		int codigoServicio=ConstantesBD.codigoServicioNoDefinido;
		if (codigoServicioOPCIONAL<=ConstantesBD.codigoServicioNoDefinido)
		{	
			if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEvolucion)
			{
				try {
					codigoServicio= cargosDao.busquedaServicioEnEvolucion(con, codigoEvolucionOPCIONAL);
				} catch (IPSException e) {
					Log4JManager.error(e);
				}
			}
			else 
			{
				codigoServicio= cargosDao.busquedaCodigoServicioXSolicitud(con, numeroSolicitud, codigoTipoSolicitud, esPortatil);
			}
		}	
		else
		{	
		   codigoServicio=codigoServicioOPCIONAL;
		} 
		return codigoServicio;
	}

	/**
	 * 
	 * @param con
	 * @param codigoServicioOPCIONAL
	 * @param codigoTipoSolicitud
	 * @param numeroSolicitud
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	@SuppressWarnings("unused")
	private int obtenerCodigoServicioSolicitudBD(Connection con, int codigoTipoSolicitud, int numeroSolicitud, int codigoEvolucionOPCIONAL, String esPortatil) throws IPSException
	{
		int codigoServicio=ConstantesBD.codigoServicioNoDefinido;
		if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEvolucion)
		{
			try {
				codigoServicio= cargosDao.busquedaServicioEnEvolucion(con, codigoEvolucionOPCIONAL);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
		}
		else 
		{
			codigoServicio= cargosDao.busquedaCodigoServicioXSolicitud(con, numeroSolicitud, codigoTipoSolicitud, esPortatil);
		}
		return codigoServicio;
	}
	
	
	/**
	 * Mï¿½todo que se encarga de bï¿½scar el cï¿½digo del servicio para un numero de solicitud y tipo
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @return
	 */
	public static int busquedaCodigoServicioXSolicitud (Connection con, int numeroSolicitud, int codigoTipoSolicitud, String esPortatil) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().busquedaCodigoServicioXSolicitud(con, numeroSolicitud, codigoTipoSolicitud, esPortatil);
	}
	
	/**
	 * metodo que obtiene el servicio de la evoulcion
	 * @param con
	 * @param codigoEvolucionOPCIONAL
	 * @return
	 */
	public static int obtenerServicioEvolucion(Connection con, int codigoEvolucionOPCIONAL) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().busquedaServicioEnEvolucion(con, codigoEvolucionOPCIONAL);
	}
	
	
	/**
	 * 
	 * @param codigoContratoBase
	 * @param codigoConvenioBaseOPCIONAL
	 * @param con
	 * @return
	 */
	private int obtenerCodigoConvenio(int codigoContratoBase, int codigoConvenioBaseOPCIONAL, Connection con) throws IPSException  
	{
		if(codigoConvenioBaseOPCIONAL<=0)
		{
			Contrato contrato= new Contrato();
			try {
				contrato.cargar(con, codigoContratoBase+"");
			}
			catch (IPSException ipsme) {
				throw ipsme;
			}
			catch (Exception e) {
				Log4JManager.error(e.getMessage(),e);
				throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
			}
			
			codigoConvenioBaseOPCIONAL=contrato.getCodigoConvenio();
		}
		return codigoConvenioBaseOPCIONAL;
	}

	/**
	 * 
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoConvenio
	 * @param codigoEsquemaTarifario
	 * @param cantidadCargada
	 * @param valorUnitarioTarifa
	 * @param valorUnitarioCargado
	 * @param valorTotalCargado
	 * @param porcentajeCargado
	 * @param porcentajeRecargo
	 * @param valorUnitarioRecargo
	 * @param porcentajeDescuento
	 * @param valorUnitarioDescuento
	 * @param valorUnitarioIva
	 * @param numeroAutorizacion
	 * @param estado
	 * @param cubierto
	 * @param tipoDistribucion
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param codigoArticulo
	 * @param codigoServicioCx
	 * @param codigoTipoAsocio
	 * @param facturado
	 * @param codigoTipoSolicitud
	 * @param paquetizado
	 * @param cargoPadre
	 * @param codigoSolicitudSubCuenta
	 * @param observaciones
	 * @param loginUsuario
	 * @param erroresCargo
	 * @return
	 */
	private static DtoDetalleCargo insertarDetalleCargo (	Connection con, 
															double codigoSubcuenta, 
															int codigoConvenio, 
															int codigoEsquemaTarifario, 
															int cantidadCargada, 
															double valorUnitarioTarifa, 
															double valorUnitarioCargado, 
															double valorTotalCargado, 
															double porcentajeCargado, 
															double porcentajeRecargo, 
															double valorUnitarioRecargo, 
															double porcentajeDescuento, 
															double valorUnitarioDescuento, 
															double valorUnitarioIva, 
															String requiereAutorizacion,
															String numeroAutorizacion, 
															int estado, 
															String cubierto, 
															String tipoDistribucion, 
															int numeroSolicitud, 
															int codigoServicio, 
															int codigoArticulo, 
															int codigoServicioCx, 
															int codigoTipoAsocio, 
															String facturado, 
															int codigoTipoSolicitud, 
															String paquetizado, 
															double cargoPadre, 
															double codigoSolicitudSubCuenta, 
															String observaciones,
															String loginUsuario,
															InfoErroresCargo erroresCargo,
															int codigoContrato,
															int detCxHonorarios,
															int detAsocioCxSalasMat,
															String esPortatil,
															String dejarExcento,
															ArrayList<Integer> codDetalleAutorizaciones,
															ArrayList<Integer> codDetalleAutorizacionesEstancia,
															double porcentajeDctoPromocionServicio, 
															BigDecimal valorDescuentoPromocionServicio, 
															double porcentajeHonorarioPromocionServicio, 
															BigDecimal valorHonorarioPromocionServicio, 
															
															double programa,
															double porcentajeDctoBono,
															BigDecimal valorDescuentoBono, 
															double porcentajeDctoOdontologico, 
															BigDecimal valorDescuentoOdontologico,
															
															int detallePaqueteOdonConvenio
														) throws IPSException
	{
		DtoDetalleCargo detalleCargo= new DtoDetalleCargo(ConstantesBD.codigoNuncaValidoDouble, codigoSubcuenta, codigoConvenio, codigoEsquemaTarifario, cantidadCargada, valorUnitarioTarifa, valorUnitarioCargado, valorTotalCargado, porcentajeCargado, porcentajeRecargo, valorUnitarioRecargo, porcentajeDescuento, valorUnitarioDescuento, valorUnitarioIva, requiereAutorizacion, numeroAutorizacion, estado, cubierto, tipoDistribucion, numeroSolicitud, codigoServicio, codigoArticulo, codigoServicioCx, codigoTipoAsocio, facturado, codigoTipoSolicitud, paquetizado, cargoPadre, codigoSolicitudSubCuenta, observaciones, codigoContrato, false /*filtrarSoloCantidadesMayoresCero*/, detCxHonorarios, detAsocioCxSalasMat, esPortatil, dejarExcento, porcentajeDctoPromocionServicio, valorDescuentoPromocionServicio, porcentajeHonorarioPromocionServicio, valorHonorarioPromocionServicio, programa, porcentajeDctoBono,	valorDescuentoBono, porcentajeDctoOdontologico, valorDescuentoOdontologico, detallePaqueteOdonConvenio);
		detalleCargo.setCodigoDetalleCargo(insertarDetalleCargos(con, detalleCargo, loginUsuario));
		
		actualizarCargoDetAutorizaciones(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizaciones);
		actualizarCargoDetAutorizacionesEstancia(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizacionesEstancia);
		
		//INSERTAMOS LOS ERRORES DE DETALLE 
		for(int w=0; w<erroresCargo.getMensajesErrorDetalle().size();w++)
			insertarErrorDetalleCargo(con, detalleCargo.getCodigoDetalleCargo() ,erroresCargo.getMensajesErrorDetalle(w));
		return detalleCargo;
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizacionesEstancia
	 * @return
	 */
	public static boolean actualizarCargoDetAutorizacionesEstancia(Connection con, double codigoDetalleCargo,ArrayList<Integer> codDetalleAutorizacionesEstancia) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarCargoDetAutorizacionesEstancia(con, codigoDetalleCargo, codDetalleAutorizacionesEstancia);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizaciones
	 * @return
	 */
	public static boolean actualizarCargoDetAutorizaciones(Connection con,	double codigoDetalleCargo,	ArrayList<Integer> codDetalleAutorizaciones) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarCargoDetAutorizaciones(con, codigoDetalleCargo, codDetalleAutorizaciones);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static ArrayList<Integer> cargarDetAutorizacionesEstanciaXCargo(Connection con, double codigoDetalleCargo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().cargarDetAutorizacionesEstanciaXCargo(con, codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static ArrayList<Integer> cargarDetAutorizacionesXCargo(Connection con, double codigoDetalleCargo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().cargarDetAutorizacionesXCargo(con, codigoDetalleCargo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @param esServicio
	 * @return
	 */
	private static int obtenerCodigoEsquemaTarifarioContrato(Connection con,String subCuenta, int codigoContrato,int servart, boolean esServicio,String fechaCalculoVigencia, int centroAtencion) throws IPSException
	{
		return Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, subCuenta, codigoContrato, servart, esServicio,fechaCalculoVigencia, centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @param esServicio
	 * @return
	 */
	public static String obtenerMetodoAjuste(Connection con, int codigoConvenio, int codigoEsquemaTarifario, int codigoInstitucion, boolean esServicio) throws IPSException
	{
		try{
			String metodoAjuste= obtenerMetodoAjusteConvenio(con, codigoConvenio, esServicio);
			if(UtilidadTexto.isEmpty(metodoAjuste))
			{
				metodoAjuste=obtenerMetodoAjusteEsqTarifario(con, codigoEsquemaTarifario, codigoInstitucion);
			}
			return metodoAjuste;
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
	 * @param codigoConvenio
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @param esServicio
	 * @return
	 */
	public static String obtenerMetodoAjuste(int codigoConvenio, int codigoEsquemaTarifario, int codigoInstitucion, boolean esServicio) throws IPSException
	{
		Connection con= UtilidadBD.abrirConexion();
		String metodoAjuste="";
		try {
			metodoAjuste = obtenerMetodoAjusteConvenio(con, codigoConvenio, esServicio);
		} catch (IPSException e) {
			Log4JManager.error(e);
		}
		if(UtilidadTexto.isEmpty(metodoAjuste))
		{
			metodoAjuste=obtenerMetodoAjusteEsqTarifario(con, codigoEsquemaTarifario, codigoInstitucion);
		}
		UtilidadBD.closeConnection(con);
		return metodoAjuste;
	}
	
	/**
	 * obtiene  el metodo de ajuste del esquema tarifario
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @return
	 */
	private static String obtenerMetodoAjusteEsqTarifario(Connection con, int codigoEsquemaTarifario, int codigoInstitucion) throws IPSException
	{
		EsquemaTarifario esquema= new EsquemaTarifario();
		try {
			esquema.cargarXcodigo(con, codigoEsquemaTarifario, codigoInstitucion);
			return esquema.getMetodoAjuste().getAcronimo();
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
	 * obtiene  el metodo de ajuste del esquema tarifario
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @return
	 */
	private static String obtenerMetodoAjusteConvenio(Connection con, int codigoConvenio, boolean esServicio) throws IPSException
	{
		//System.out.print("\n entre a obtenerMetodoAjusteConvenio codigoConvenio-->"+codigoConvenio);
		Convenio convenio= new Convenio();
		String metodoAjuste="";
		
		try {
			convenio.cargarResumen(con, codigoConvenio);
			if( esServicio )
				metodoAjuste=convenio.getAjusteServicios();
			else
				metodoAjuste=convenio.getAjusteArticulos();
			//System.out.print("\n el metodo de ajuste es -->"+metodoAjuste);
			if(UtilidadTexto.isEmpty(metodoAjuste))
				return "";
			else
			{
				if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteCentena))
					return ConstantesBD.metodoAjusteCentena;
				if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteDecena))
					return ConstantesBD.metodoAjusteDecena;
				if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteUnidad))
						return ConstantesBD.metodoAjusteUnidad;
					if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoSinAjuste))
						return ConstantesBD.metodoSinAjuste;
				}
				return "";
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
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @return
	 */
	@SuppressWarnings("unused")
	private double obtenerCodigoDetalleCargoXServ(Connection con, int numeroSolicitud, String subcuenta, int codigoServicio, String facturado) 
	{
		String codigoStr=busquedaCodigosDetalleCargos(con, subcuenta, numeroSolicitud+"", codigoServicio+"","", facturado).get(0).toString();
		logger.info("codigo->"+codigoStr);
		double codigo=Double.parseDouble(codigoStr);
		return codigo;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @return
	 */
	public static double obtenerCodigoDetalleCargoXArticulo(Connection con, String subcuenta, int numeroSolicitud, int codigoArticulo, String facturado) 
	{
		String codigoStr=busquedaCodigosDetalleCargos(con, subcuenta, numeroSolicitud+"", "", codigoArticulo+"", facturado).get(0).toString();
		double codigo=Double.parseDouble(codigoStr);
		return codigo;
	}

	/**
	 * metodo para insertar el detalle de los cargos
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public static double insertarDetalleCargos(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().insertarDetalleCargos(con, detalleCargo, loginUsuario);
	}
	
	/**
	 * metodo para insertar el detalle de los cargos
	 * @param con
	 * @param cargo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarDetalleCargos(Connection con, ArrayList<DtoDetalleCargo> detalleCargoArray, String loginUsuario ) throws IPSException
	{
		//primero se inserta el encabezado
		double codigoDetalleCargo= 0;
		for(int w=0; w<detalleCargoArray.size();w++)
		{
			codigoDetalleCargo= insertarDetalleCargos(con, detalleCargoArray.get(w), loginUsuario);
			if(codigoDetalleCargo<=0)
				w=detalleCargoArray.size();
		}
		
		if(codigoDetalleCargo<=0)
			return false;
		return true;
	}
	
	
	/**
	 * metodo que obtiene la tarifa base x servicio
	 * @param con
	 * @param codigoTipoTarifario
	 * @param codigoServicio
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static InfoTarifaVigente obtenerTarifaBaseServicio(Connection con, int codigoTipoTarifario, int codigoServicio, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicio, codigoEsquemaTarifario, fechaCalculoVigencia);
	}
	
	/**
	 * metodo que obtiene la tarifa base x servicio
	 * @param con
	 * @param codigoTipoTarifario  
	 * @param codigoServicio
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static InfoTarifaVigente obtenerTarifaBaseServicio(int codigoTipoTarifario, int codigoServicio, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws IPSException
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoTarifaVigente info= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicio, codigoEsquemaTarifario, fechaCalculoVigencia);
		UtilidadBD.closeConnection(con);
		return info;
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
	public static InfoTarifa obtenerExcepcionesTarifasServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia, int centroAtencion ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerExcepcionesTarifasServicio(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoServicio, codigoInstitucion,fechaCalculoVigencia, centroAtencion);
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
	public static InfoTarifa obtenerRecargoServicio (Connection con, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoContrato, int codigoTipoSolicitud, int numeroSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerRecargoServicio(con, codigoViaIngreso,tipoPaciente, codigoServicio, codigoContrato, codigoTipoSolicitud, numeroSolicitud);
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
	public static InfoTarifa obtenerDescuentoComercialXConvenioServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerDescuentoComercialXConvenioServicio(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
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
	public static InfoTarifa obtenerDescuentoComercialXConvenioServicio(int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia ) throws IPSException
	{
		Connection con=UtilidadBD.abrirConexion();
		InfoTarifa info= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerDescuentoComercialXConvenioServicio(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicitante
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoDatosString obtenerInclusionExclusionXConvenioServicio(Connection con, int codigoContrato, int codigoCentroCostoSolicitante, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerInclusionExclusionXConvenioServicio(con, codigoContrato, codigoCentroCostoSolicitante, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicitante
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static String obtenerInclusionExclusionXConvenioServicio(int codigoContrato, int codigoCentroCostoSolicitante, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia ) throws IPSException
	{
		Connection con=UtilidadBD.abrirConexion();
		String returna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerInclusionExclusionXConvenioServicio(con, codigoContrato, codigoCentroCostoSolicitante, codigoServicio, codigoInstitucion,fechaCalculoVigencia).getAcronimo();
		UtilidadBD.closeConnection(con);
		return returna;
	}
	
	/**
	 * metodo para obtener la tarifa base de un articulo 
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static double obtenerTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener el TIPO de tarifa base de un articulo
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static String obtenerTipoTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerTipoTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
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
	public static InfoTarifa obtenerExcepcionesTarifasArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoArticulo, int codigoInstitucion, int codigoEsquemaTarifario,String fechaCalculoVigencia, int centroAtencion ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerExcepcionesTarifasArticulo(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoArticulo, codigoInstitucion, codigoEsquemaTarifario,fechaCalculoVigencia, centroAtencion);
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
	public static InfoTarifa obtenerDescuentoComercialXConvenioArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoArticulo, int codigoInstitucion,String fechaCalculoVigencia ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerDescuentoComercialXConvenioArticulo(con, codigoViaIngreso, tipoPaciente, codigoContrato, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicitante
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosString obtenerInclusionExclusionXConvenioArticulo(Connection con, int codigoContrato, int codigoCentroCostoSolicitante, int codigoArticulo, int codigoInstitucion,String fechaCalculoVigencia ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerInclusionExclusionXConvenioArticulo(con, codigoContrato, codigoCentroCostoSolicitante, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
	}
	
	/**
	 * metodo que elimina los errores_cargos -  det_cargos x codigo_detalle_cargo
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static boolean eliminarDetalleCargoYErroresYArtConsumoXCodigoDetalleCargo(Connection con, double codigoDetalleCargo) throws IPSException
	{
		boolean eliminoExitoso=false;
		if(eliminarDetalleCargoXCodigoDetalle(con, codigoDetalleCargo)){
			eliminoExitoso=true;
		}
		return eliminoExitoso;
	}
	
	/**
	 * metodo que elimina el detalle cargo x codigo de detalle, debe tener encuenta que primero debe eliminar los
	 * errores_cargos si existen para que no saque errores de dependencias @see: eliminarErroresCargoXCodigoDetalleCargo
	 * y tambien eliminar los detalles de los cargos de los articulos x consumo @see: eliminarDetalleCargoArticuloConsumoXCodigoDetalle
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static boolean eliminarDetalleCargoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().eliminarDetalleCargoXCodigoDetalle(con, codigoDetalleCargo);
	}
	
	/**
	 * * metodo que realiza una busqueda de los atributos codigo_detalle_cargo 
	 * y realiza filtros (si existen de) key="subCuenta,solicitud,servicio,articulo" 
	 * @param con
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param codigoArticulo
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector busquedaCodigosDetalleCargos(Connection con, String subCuenta, String numeroSolicitud, String servicio, String articulo, String facturado)
	{
		HashMap criteriosBusquedaMap= new HashMap<Object, String>();
		if(!UtilidadTexto.isEmpty(subCuenta))
			criteriosBusquedaMap.put("subCuenta", subCuenta);
		if(!UtilidadTexto.isEmpty(numeroSolicitud))
			criteriosBusquedaMap.put("solicitud", numeroSolicitud);
		if(!UtilidadTexto.isEmpty(servicio))
			criteriosBusquedaMap.put("servicio", servicio);
		if(!UtilidadTexto.isEmpty(articulo))
			criteriosBusquedaMap.put("articulo", articulo);
		if(!UtilidadTexto.isEmpty(facturado))
			criteriosBusquedaMap.put("facturado", facturado);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().busquedaCodigosDetalleCargos(con, criteriosBusquedaMap);
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
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerNumeroResponsablesSolicitudCargo(con, numeroSolicitud, codigoServicioArticulo, esServicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param error
	 * @return
	 */
	public static double insertarErrorDetalleCargo(Connection con, double codigoDetalleCargo, String error) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().insertarErrorDetalleCargo(con, codigoDetalleCargo, error);
	}
	
	/**
	 * existe cargos pendientes
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existenCargosPendientesXSolicitud(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().existenCargosPendientesXSolicitud(con, numeroSolicitud);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param estado
	 * @return
	 */
	public static boolean modificarEstadoCargo(Connection con, double codigoDetalleCargo, int estado) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().modificarEstadoCargo(con, codigoDetalleCargo, estado);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codigoDetalleCargoPadre
	 * @return
	 */
	public static boolean modificarCargoPadre(Connection con, double codigoDetalleCargo, double codigoDetalleCargoPadre)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().modificarCargoPadre(con, codigoDetalleCargo, codigoDetalleCargoPadre);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoConvenio
	 * @param facturado
	 * @param portatil ('S', 'N', '')
	 * @return
	 */
	public static double obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(Connection con, int numeroSolicitud, double subCuenta, String facturado, String paquetizado, String portatil) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(con, numeroSolicitud, subCuenta, facturado, paquetizado, portatil);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoConvenio
	 * @param codigoArticulo
	 * @param facturado
	 * @return
	 */
	public static double obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(Connection con, int numeroSolicitud, double subCuenta, int codigoArticulo, String facturado, String paquetizado) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(con, numeroSolicitud, subCuenta, codigoArticulo, facturado, paquetizado);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Vector obtenerErroresDetalleCargo(Connection con, double codigoDetalleCargo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerErroresDetalleCargo(con, codigoDetalleCargo);
	}

	/**
	 * Este mï¿½todo inicializa en valores vacï¿½os, -mas no nulos- los atributos de este objeto.
	 */
	public void clean()
	{
		this.infoErroresCargo= new InfoErroresCargo();
		this.dtoDetalleCargo = new DtoDetalleCargo();
		this.pyp = false;
		this.infoResponsableCoberturaGeneral=new InfoResponsableCobertura();
	}

	/**
	 * 
	 * @return
	 */
	public InfoErroresCargo getInfoErroresCargo() {
		return infoErroresCargo;
	}

	/**
	 * 
	 * @param infoErroresCargo
	 */
	public void setInfoErroresCargo(InfoErroresCargo infoErroresCargo) {
		this.infoErroresCargo = infoErroresCargo;
	}

	/**
	 * @return the dtoDetalleCargo
	 */
	public DtoDetalleCargo getDtoDetalleCargo() {
		return dtoDetalleCargo;
	}

	/**
	 * @param dtoDetalleCargo the dtoDetalleCargo to set
	 */
	public void setDtoDetalleCargo(DtoDetalleCargo dtoDetalleCargo) {
		this.dtoDetalleCargo = dtoDetalleCargo;
	}

	/**
	 * 
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param esServicio
	 * @return
	 */
	public static boolean eliminarDetalleCargoXSolicitudServArt(Connection con, int numSolicitud, String codServArt, boolean esServicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().eliminarDetalleCargoXSolicitudServArt(con, numSolicitud, codServArt,esServicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param numSolicitud
	 * @param codServArt
	 * @param codigoServicioAsocio 
	 * @param estadoFacturado
	 * @param esServicio
	 * @param numeroFactura 
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoDetalleCargo(Connection con, double subCuenta, int numSolicitud, int codServArt, int codigoServicioAsocio, String estadoFacturado, boolean esServicio, int numeroFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarEstadoFacturadoDetalleCargo(con, subCuenta, numSolicitud, codServArt,codigoServicioAsocio, estadoFacturado, esServicio,numeroFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoSubCuenta(Connection con, double subCuenta, String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarEstadoFacturadoSubCuenta(con, subCuenta, estado);
	}

	/**
	 * metodo para obtener los codigos det cargo de los componenetes de un paquete
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Vector obtenerCargosComponentesPaquete(Connection con, double codigoDetalleCargo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCargosComponentesPaquete(con, codigoDetalleCargo);
	}
	
	/**
	 * metodo para actualizar el cargo padre de los componentes de un paquete
	 * @param con
	 * @param cargoPadre
	 * @param codigosDetalleCargoVector
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean actualizarCargoPadreComponentesPaquetes(Connection con, double cargoPadre, Vector codigosDetalleCargoVector) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarCargoPadreComponentesPaquetes(con, cargoPadre, codigosDetalleCargoVector);
	}
	
	/**
	 * Solo se puede borrar el cargo del portatil cuando esta en estado solicitada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean borrarCargoPortatil(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().borrarCargoPortatil(con, numeroSolicitud);
	}
	
	/**
	 * @return the pyp
	 */
	public boolean isPyp() {
		return pyp;
	}

	/**
	 * @param pyp the pyp to set
	 */
	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}

	/**
	 * Metodo que retorna la fecha valida para el calculo del cargo, esto depende del tipo de solicitud y del estado de la misma.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerFechaCalculoCargo(Connection con, int numeroSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerFechaCalculoCargo(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para consultar el Centro de costo plan especial por Convenio
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static int conveniosPlanEspecial(Connection con, int codigoIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().conveniosPlanEspecial(con, codigoIngreso);
	}
	
	/**
	 * Mï¿½todo que consulta el Cï¿½digo del Convenio del Cargo
	 * segï¿½n el Artï¿½culo ï¿½ Servicio y el Nï¿½mero de Solicitud
	 * establecida
	 */
	public static int obtenerCodigoConvenioDetalleCargo(Connection con, int codigoArticulo, int numeroSolicitud, boolean esArticulo) throws IPSException
	{
		int codigoConvenio = ConstantesBD.codigoNuncaValido;
		DtoDetalleCargo dto = new DtoDetalleCargo();
		dto.setNumeroSolicitud(numeroSolicitud);
		if(esArticulo)
			dto.setCodigoArticulo(codigoArticulo);
		else
			dto.setCodigoServicio(codigoArticulo);
		
		ArrayList<DtoDetalleCargo> array = Cargos.cargarDetalleCargos(con, dto);
		
		if(array.size()==1)
			codigoConvenio=array.get(0).getCodigoConvenio();
		
		return codigoConvenio;
	}
	
	/**
	 * Mï¿½todo que consulta el Cï¿½digo del Convenio del Cargo
	 * segï¿½n el Artï¿½culo ï¿½ Servicio y el Nï¿½mero de Solicitud
	 * establecida
	 */
	public static double obtenerCodigoSubcuentaDetalleCargo(Connection con, int codigoArticulo, int numeroSolicitud, int convenio, boolean esArticulo) throws IPSException
	{
		double codigoSubcuenta = ConstantesBD.codigoNuncaValido;
		DtoDetalleCargo dto = new DtoDetalleCargo();
		dto.setNumeroSolicitud(numeroSolicitud);
		if (convenio!=ConstantesBD.codigoNuncaValido)
			dto.setCodigoConvenio(convenio);
		if(esArticulo)
			dto.setCodigoArticulo(codigoArticulo);
		else
			dto.setCodigoServicio(codigoArticulo);
		
		ArrayList<DtoDetalleCargo> array = Cargos.cargarDetalleCargos(con, dto);
		
		if(array.size()==1)
			codigoSubcuenta = array.get(0).getCodigoSubcuenta();
		
		return codigoSubcuenta;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoSubCuentasTotalCero(Connection con, int codigoIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarEstadoFacturadoSubCuentasTotalCero(con, codigoIngreso);
	}

	/**
	 * Acatualizacion del campo nro_autrorizacion de la tabla det_cargos
	 * @param con
	 * @param int codigo_detalle_cargo
	 * @return
	 */
	public static boolean actualizarNumeroAutorizacion(Connection con, String nro_autorizacion, double codigo_det_cargo) 
	{
		HashMap<String, Object> datos = new HashMap<String, Object>();
		datos.put("numero_autorizacion", nro_autorizacion);
		datos.put("codigo_det_cargo", codigo_det_cargo);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().actualizarNumeroAutorizacion(con, datos);
	}
	
	/**
	 * Insertar cargos articulos consumo
	 * @param con
	 * @param dto
	 * @param loginUsuario
	 * @return
	 */
	public static double insertarDetalleCargosArtConsumos(Connection con, DtoDetalleCargoArticuloConsumo dto, String loginUsuario ) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().insertarDetalleCargosArtConsumos(con, dto, loginUsuario);
	}
	
	/**
	 * metodo que elimina el detalle cargo art consumo x codigo de detalle, 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static boolean eliminarDetalleCargoArticuloConsumoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().eliminarDetalleCargoArticuloConsumoXCodigoDetalle(con, codigoDetalleCargo);
	}
	
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public static ArrayList<DtoDetalleCargoArticuloConsumo> cargarDetalleCargosArticuloConsumo(Connection con, DtoDetalleCargoArticuloConsumo criteriosBusquedaDtoDetalleCargoArtConsumo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().cargarDetalleCargosArticuloConsumo(con, criteriosBusquedaDtoDetalleCargoArtConsumo);
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param estado
	 * @param facturado
	 * @param eliminado
	 * @param con
	 * @return
	 */
	public static boolean modificarEstadosCargosCitas(
			int numeroSolicitud, int estado, String facturado,
			String eliminado, Connection con) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().modificarEstadosCargosCitas(numeroSolicitud, estado, facturado, eliminado, con);
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCentroAtencionCargoSolicitud(Connection con,int numeroSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud);
	}
	

	/**
	 * 
	 * @param centroCosto
	 * @return
	 */
	public static int obtenerCentroAtencionCentroCostoSolicitadoCargo(int centroCosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCentroAtencionCentroCostoSolicitadoCargo(centroCosto);
	}

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
	public static ArrayList<BigDecimal> obtenerCodigosPkCargosDadoSolicitudes(Connection con, ArrayList<BigDecimal> listaSolicitudes) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().obtenerCodigosPkCargosDadoSolicitudes(con, listaSolicitudes);
	}
	
	
	/** 
	 * Obtiene la info Responsable de la cobertura de cada Articulo Servicio
	 * @return InfoResponsableCobertura
	 */
	public InfoResponsableCobertura getInfoResponsableCoberturaGeneral() {
		return infoResponsableCoberturaGeneral;
	}

	/**
	 * Almacena la info Responsable de la cobertura de cada Articulo Servicio
	 * @param infoResponsableCoberturaGeneral
	 */
	public void setInfoResponsableCoberturaGeneral(
			InfoResponsableCobertura infoResponsableCoberturaGeneral) {
		this.infoResponsableCoberturaGeneral = infoResponsableCoberturaGeneral;
	}
	
	/**
	 * Mï¿½todo obtiene cantidad de Inclusiones de un articulo por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoArticulo
	 * @return
	 */
	public int consultarCantidadInclusionesExclusionesOrdenArticulo(Connection con, int codigoSubcuenta, int codigoArticulo){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().consultarCantidadInclusionesExclusionesOrdenArticulo(con, codigoSubcuenta, codigoArticulo);
	}
	
	/**
	 * Mï¿½todo obtiene cantidad de Inclusiones de un servicio por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoServicio
	 * @return
	 */
	public int consultarCantidadInclusionesExclusionesOrdenServicio(Connection con, int codigoSubcuenta, int codigoServicio){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDao().consultarCantidadInclusionesExclusionesOrdenServicio(con, codigoSubcuenta, codigoServicio);
	}
}