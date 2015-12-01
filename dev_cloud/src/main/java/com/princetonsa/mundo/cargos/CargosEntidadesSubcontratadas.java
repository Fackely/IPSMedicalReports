/*
 * Jun 23, 2009
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ElementoApResource;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoCobertura;
import util.facturacion.InfoTarifaVigente;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.cargos.DTOCalculoTarifaServicioArticulo;
import com.princetonsa.dto.cargos.DtoCargoEntidadSub;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Objeto usado para el proceso de caculo tarifa y generacion de cargo
 * a entidades subcontratadas
 * @author Sebastián Gómez R
 *
 */
public class CargosEntidadesSubcontratadas 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CargosEntidadesSubcontratadas.class);
	
	/**
	 * DAO para el manejo de transacciones
	 */
	private CargosEntidadesSubcontratadasDao cargosDao = null;
	
	/**
	 * Manejo de los errores del cargo
	 */
	private ArrayList<ElementoApResource> erroresCargo;
	
	/**
	 * Objeto donde se almacenan los errores de la transaccion
	 */
	private ActionErrors erroresProceso;
	
	private double tarifaCalculadad;
	
	/**
	 * Reset 
	 */
	public void clean()
	{
		this.erroresCargo = new ArrayList<ElementoApResource>();
		this.erroresProceso = new ActionErrors();
	}
	
	/**
	 * Instancia estática del dao
	 * @return
	 */
	public static CargosEntidadesSubcontratadasDao cargosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao();
	}
	
	/**
	 * @return the erroresProceso
	 */
	public ActionErrors getErroresProceso() {
		return erroresProceso;
	}

	/**
	 * @param erroresProceso the erroresProceso to set
	 */
	public void setErroresProceso(ActionErrors erroresProceso) {
		this.erroresProceso = erroresProceso;
	}

	/**
	 * Crea un nuevo objeto <code>Egreso</code>.
	 */
	public CargosEntidadesSubcontratadas()  
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			cargosDao = myFactory.getCargosEntidadesSubcontratadasDao();
			wasInited = (cargosDao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Método para obtener el contrato vigente de una entidad subcontratada
	 * @param con
	 * @param codigoEntidadSubcontratada
	 * @param fechaReferencia
	 * @return
	 */
	public static DtoContratoEntidadSub obtenerContratoVigenteEntidadSubcontratada(Connection con,String codigoEntidadSubcontratada,String fechaReferencia)
	{
		return cargosDao().obtenerContratoVigenteEntidadSubcontratada(con, codigoEntidadSubcontratada, fechaReferencia);
	}

	
	
	/**
	 * obtener entidades subcontratadas
	 * @param Connection con
	 * @param int codigoCentroCosto
	 * @param int codigoInstitucion
	 * */
	public static ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(Connection con,int codigoCentroCosto,int codigoInstitucion)
	{
		return cargosDao().obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCosto, codigoInstitucion);	
	}
	
	
	/**
	 * Método que obtiene el listado de entidades subocntratadas que cubren el servicio de la autorizacion
	 * @param con
	 * @param codigoCentroCosto (Solicitado)
	 * @param codigoServicio
	 * @param fecha
	 * @param codigoInstitucion
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoNaturalezaPaciente
	 * @return ArrayList<DtoEntidadSubcontratada>
	 */
	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasXCentroCostoAutorizacion(Connection con,int codigoCentroCosto,int codigoServicio,String fecha,int codigoInstitucion,int codigoViaIngreso,String tipoPaciente,int codigoNaturalezaPaciente, String solicitud) throws IPSException
	{
		ArrayList<DtoEntidadSubcontratada> arregloFinal = new ArrayList<DtoEntidadSubcontratada>();
		ArrayList<DtoEntidadSubcontratada> arregloInicial =  cargosDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCosto, codigoInstitucion);
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		if(!UtilidadTexto.isEmpty(solicitud))
		{
			parametros.put("solicitud", solicitud);
			parametros.put("centro_costo_solicitado", codigoCentroCosto);
			//ent_subcontratada
			logger.info("Número de entidades encontradas: "+arregloInicial.size());
			for(DtoEntidadSubcontratada entidad:arregloInicial)
			{
				parametros.put("ent_subcontratada", entidad.getConsecutivo());
				//La entidad subcontratad debe tener contrato vigente y el servicio cubierto
				InfoCobertura infoCobertura = Cobertura.validacionCoberturaServicioEntidadSubDadaEntidad(con, entidad.getConsecutivo(), codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion, fecha);
				if(infoCobertura.getIncluido())
				{
					//Se quita esta validación por la incidencia MT 2375, la razon por la cual se quita la validación
					// es porque ya no aplica debido a la incidencia MT 1221  Ricardo Ruiz 
//					if (consultaCentroCostoSolicitanteIgualACubierto(con, parametros).equals(ConstantesBD.acronimoSi)){
//						arregloFinal.add(entidad);
//					}
					arregloFinal.add(entidad);
				}
			}
			return arregloFinal;
		}
		else{
			/* Se modifica por Ordenes ambulatorias, ya que tambien se verifica la cobertura para ordenes ambulatorias.
			 * En el caso de ser una orden ambulatoria no se recibe número de solicitud y en cambio se retornan TODAS las entidades
			 * Subcontratadas con las cuales se encontro cobertura, para que se compare manualmente desde donde se llama este metodo.
			 * Cristhian Murillo - Estabilización de Ordenes Ambulatorias.  * */
			return arregloInicial;
		}
		
	}
	
	
	/**
	 * obtenerEntidadesSubcontratadasXCentroCostoAutorizacionArticulo por articulo
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigosArticulos
	 * @param fecha
	 * @param codigoInstitucion
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoNaturalezaPaciente
	 * @param solicitud
	 * @return
	 */
	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasXCentroCostoAutorizacionArticulo(Connection con,int codigoCentroCosto,ArrayList<Integer> codigosArticulos,String fecha,int codigoInstitucion,int codigoViaIngreso,String tipoPaciente,int codigoNaturalezaPaciente, String solicitud) throws IPSException
	{
		ArrayList<DtoEntidadSubcontratada> arregloFinal = new ArrayList<DtoEntidadSubcontratada>();
		ArrayList<DtoEntidadSubcontratada> arregloInicial =  cargosDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCosto, codigoInstitucion);
		
		if(!UtilidadTexto.isEmpty(solicitud))
		{
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("solicitud", solicitud);
			parametros.put("centro_costo_solicitado", codigoCentroCosto);

			logger.info("Número de entidades encontradas: "+arregloInicial.size());
			int entroConCobertura=0;
			
			for(DtoEntidadSubcontratada entidad:arregloInicial)
			{	
				logger.info("\tentidad: "+entidad.getConsecutivo());
			
				parametros.put("ent_subcontratada", entidad.getConsecutivo());
				//La entidad subcontratada debe tener contrato vigente y el servicio cubierto
				
				for (Integer codArti : codigosArticulos)//------
				{	logger.info("codigo Articulo a evaluar: "+codArti);
					InfoCobertura infoCobertura = Cobertura.validacionCoberturaArticuloEntidadSubDadaEntidad(con, entidad.getConsecutivo(),
							codigoViaIngreso, tipoPaciente, codArti, codigoNaturalezaPaciente, codigoInstitucion, fecha);
					if(infoCobertura.getIncluido())
					{
						if (consultaCentroCostoSolicitanteIgualACubierto(con, parametros).equals(ConstantesBD.acronimoSi)){
							arregloFinal.add(entidad);
							entroConCobertura++;
						}
					}
					else
					{	if(entroConCobertura>0)
							arregloFinal.add(null);						
					}
				}
			}
			return arregloFinal;
		}
		else{
			/* Se modifica por Ordenes ambulatorias, ya que tambien se verifica la cobertura para ordenes ambulatorias.
			 * En el caso de ser una orden ambulatoria no se recibe número de solicitud y en cambio se retornan TODAS las entidades
			 * Subcontratadas con las cuales se encontro cobertura, para que se compare manualmente desde donde se llama este metodo.
			 * Cristhian Murillo - Estabilización de Ordenes Ambulatorias.  * */
			return arregloInicial;
		}
		
	}
	
	
	/**
	 * Método implementado para generar el cargo del artículo
	 * @param con
	 * @param codigoFarmacia
	 * @param codigoArticulo
	 * @param codigoArticuloPrincipal
	 * @param numeroSolicitud
	 * @param numeroPedido
	 * @param fecha
	 * @param hora
	 * @param vieneDespacho
	 * @param usuario
	 */
	public void generarCargoArticulo(Connection con, int codigoFarmacia, int codigoArticulo, int codigoArticuloPrincipal, String numeroSolicitud,
			String numeroPedido, String fecha, String hora, boolean vieneDespacho, UsuarioBasico usuario, String observaciones, String codigoAutorizacion) throws IPSException
	{
		
		Log4JManager.info("*************** GENERACIÓN CARGO ARTÍCULO ENTIDAD SUBCONTRATADA ************");
		String nombreFarmacia = Utilidades.obtenerNombreCentroCosto(con, codigoFarmacia, usuario.getCodigoInstitucionInt());
		DtoEntidadSubcontratada entidadSub = new DtoEntidadSubcontratada();
		DtoContratoEntidadSub contrato = new DtoContratoEntidadSub();
		EsquemaTarifario esquema = new EsquemaTarifario();
		double tarifaArticulo = ConstantesBD.codigoNuncaValido; 
		ResultadoBoolean respuesta = new ResultadoBoolean(true,"");
		
		//1) SE VERIFICA SI EL CENTRO DE COSTO ES DE TIPO ENTIDAD EJECUTA = EXTERNO
		String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, codigoFarmacia);
		
		Log4JManager.info("TIPO ENTIDAD "+tipoEntidad+" DE CENTRO COSTO "+codigoFarmacia);
		
		//if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
		//{
			//2) SE CONSULTA LA ENTIDAD SUBCONTRATADA ASOCIADA AL CENTRO DE COSTO
			ArrayList<DtoEntidadSubcontratada> entidades = cargosDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoFarmacia, usuario.getCodigoInstitucionInt());
			
			Log4JManager.info("N° DE ENTIDADES SUBCONTRADAS ENCONTRADAS: "+entidades.size());
			if(entidades.size()>0)
			{
				//3) SE CONSULTA CONTRATOS VIGENTES PARA LA ENTIDAD SUBCONTRATADA
				entidadSub = entidades.get(0);
				Log4JManager.info("LA ENTIDAD SUBCONTRATADA ENCONTRADA ES: "+entidadSub.getConsecutivo()+" - "+entidadSub.getRazonSocial());
				
				DTOCalculoTarifaServicioArticulo dtoCalculoTarifa = new DTOCalculoTarifaServicioArticulo();
				dtoCalculoTarifa.setFechaVigencia(fecha);
				dtoCalculoTarifa.setCodigoArticuloServicio(codigoArticulo);
				dtoCalculoTarifa.setContratoEntidadSubcontratada(contrato);
				dtoCalculoTarifa.setEntidadSubcontratada(entidadSub);
				dtoCalculoTarifa.setEsServicio(false);
				
				//4) SE CALCULA LA TARIFA				
				tarifaArticulo = calcularTarifaEntidadSubcontratada(con, dtoCalculoTarifa,esquema);
			}
			else
			{
				Log4JManager.info("NO SE ENCONTRÓ ENTIDAD SUBCONTRATADA. PROCESO CANCELADO");
				agregarError("errors.noExiste2", "entidad subcontratada asociada a la farmacia "+nombreFarmacia);
			}
			
			//6) *********** SE REGISTRA EL CARGO DE ENTIDAD SUBCONTRATADA**************************************
			DtoCargoEntidadSub cargo = new DtoCargoEntidadSub();
			cargo.setFechaModifica(UtilidadFecha.getFechaActual(con));
			cargo.setHoraModifica(UtilidadFecha.getHoraActual(con));
			cargo.setUsuarioModifica(usuario);
			
			Log4JManager.info("***INSERCIÓN DE LA TARIFA ENTIDAD SUBCONTRATADA***");
			//Se verifica si ya existía un cargo para la solicitud
			String codigoDetalleCargo = this.obtenerCodigoCargoEntidadSubcontratadaArticulo(con, numeroSolicitud, numeroPedido, codigoArticulo, codigoArticuloPrincipal);
			
			Log4JManager.info("CÓDIGO DETALLE CARGO EXISTENTE? "+codigoDetalleCargo);
			cargo.setCodigoArticulo(codigoArticulo);
			cargo.setCodigoArticuloPrincipal(codigoArticuloPrincipal);
			cargo.setNumeroSolicitud(numeroSolicitud);
			cargo.setNumeroPedido(numeroPedido);
			cargo.setContrato(contrato);
			cargo.setEntidad(entidadSub);
			cargo.getEsquemaTarifario().setCodigo(esquema.getCodigo());
			cargo.setObservaciones(observaciones);
			cargo.setConsecutivoAutorizacion(codigoAutorizacion);
			
			if(tarifaArticulo>=0)
			{
				cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFCargada);
				cargo.setValorUnitario(tarifaArticulo+"");
				this.tarifaCalculadad = tarifaArticulo;
			}
			else
			{
				cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFPendiente);
			}
			cargo.setFecha(fecha);
			cargo.setHora(hora);
			cargo.setVieneDespacho(vieneDespacho);
			
			 //Se verifica si ya existía cargo
			if(!codigoDetalleCargo.equals(""))
			{
				Log4JManager.info("MODIFICACION!!!");
				cargo.setCodigoDetalleCargo(codigoDetalleCargo);
				respuesta = cargosDao.modificarCargoEntidadSubcontratada(con, cargo);
			}
			else
			{
				Log4JManager.info("INSERCIÓN!!!");
				respuesta = cargosDao.insertarCargoEntidadSubcontratada(con, cargo);
			}
			
			//Si el proceso es exitoso se continua con la validacion de los errores
			if(respuesta.isTrue())
			{
				//7) *******************SE REGISTRAN LOS ERRORES DEL CARGO*************************************+
				Log4JManager.info("SE REGISTRARON ERRORES DEL CARGO!!!");
				respuesta = cargosDao.registrarErroresCargoEntidadSub(con,  cargo.getCodigoDetalleCargo(), erroresCargo);
				if(!respuesta.isTrue())
				{
					Log4JManager.info("OCURRIÓ ERROR AL TRATAR DER EGISTRAR LOS ERRORES DEL CARGO "+respuesta.getDescripcion());
					this.erroresProceso.add("Error al guardar errores cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
				}
				//***********************************************************************************************
			}
			else
			{
				Log4JManager.info("OCURRIÓ ERROR AL TRATAR DER EGISTRAR EL CARGO "+respuesta.getDescripcion());
				this.erroresProceso.add("Error al guardar cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
			}
			
			Log4JManager.info("***fin INSERCIÓN DE LA TARIFA ENTIDAD SUBCONTRATADA***");
			//**************************************************************************************************
		//}
		Log4JManager.info("*********FIN GENERACIÓN CARGO ARTÍCULO ENTIDAD SUBCONTRATADA**********");
	}

	
	
	/**
	 * Este Método se encarga de calcular las tarifas de servicios o de artículos 
	 * pactadas entre la IPS y las entidades subcontratadas
	 * 
	 * @param con, dtoCalculoTarifario
	 * @return double tarifaArticulo
	 * @author, Angela Maria Aguirre
	 * 
	 */
	public double calcularTarifaEntidadSubcontratada(Connection con,
			DTOCalculoTarifaServicioArticulo dtoCalculoTarifario, EsquemaTarifario esquemaTarifario ) throws IPSException {
		
		double tarifa = ConstantesBD.codigoNuncaValido;
		boolean esServicio = false;		
				
		DtoEntidadSubcontratada entidadSubcontratada = dtoCalculoTarifario.getEntidadSubcontratada();
				
		DtoContratoEntidadSub contrato = cargosDao.obtenerContratoVigenteEntidadSubcontratada(
				con,entidadSubcontratada.getConsecutivo()+"", dtoCalculoTarifario.getFechaVigencia());
				
		if(contrato!=null && !contrato.getConsecutivo().equals("")) 
		{
			
		// *******SE TOMA EL ESQUEMA TARIFARIO DEL CONTRATO*************************
			dtoCalculoTarifario.setContratoEntidadSubcontratada(contrato);
			if(dtoCalculoTarifario.isEsServicio()){
				esServicio = true;
			}
			
			esquemaTarifario.obtenerEsquemaTarifarioServicioArticuloEntidadSub(con, 
					contrato.getConsecutivo(), dtoCalculoTarifario.getCodigoArticuloServicio(), 
					dtoCalculoTarifario.getFechaVigencia(), esServicio);
		
			Log4JManager.info("EL ESQUEMA TARIFARIO ENCONTRADO ES: "+esquemaTarifario.getCodigo()+" - "+
					esquemaTarifario.getNombre()+" con método de ajuste: "+
					esquemaTarifario.getMetodoAjuste().getDescripcion());
			
			if(esquemaTarifario.getCodigo()>0)
			{
				
				// ******** SE OBTIENE LA TARIFA DEL SERVICIO O ARTICULO******************************
				if(esServicio){
					InfoTarifaVigente tarifaServicio = Cargos.obtenerTarifaBaseServicio(con, esquemaTarifario.getTarifarioOficial().getCodigo(),
							dtoCalculoTarifario.getCodigoArticuloServicio(), 
							esquemaTarifario.getCodigo(), dtoCalculoTarifario.getFechaVigencia());
					if(tarifaServicio.isExiste()){
						tarifa = tarifaServicio.getValorTarifa();
					}
					else{
						tarifa = ConstantesBD.codigoNuncaValido;
					}
				}else{				
					
					tarifa = Cargos.obtenerTarifaBaseArticulo(con, dtoCalculoTarifario.getCodigoArticuloServicio(), 
							esquemaTarifario.getCodigo(), dtoCalculoTarifario.getFechaVigencia());
				}				
				Log4JManager.info("LA TARIFA DEL ARTÍCULO ENCONTRADA ES: "+tarifa);
				
				if(tarifa>=0)
				{
					tarifa =UtilidadValidacion.aproximarMetodoAjuste(esquemaTarifario.getMetodoAjuste().getId(), tarifa);
				}
				else
				{
					Log4JManager.info("NO SE ENCONTRÓ TARIFA DE ARTÍCULO PARA ARTICULO: "+dtoCalculoTarifario.getCodigoArticuloServicio()+
							" en la fecha "+dtoCalculoTarifario.getFechaVigencia()+". PROCESO CANCELADO");
					agregarError("errors.noExiste2", "tarifa vigente ("+dtoCalculoTarifario.getFechaVigencia()+
							") para el artículo en el esquema tarifario "+esquemaTarifario.getNombre());
				}
			}
			else
			{
				Log4JManager.info("NO SE ENCONTRÓ ESQUEMA TARIFARIO VIGENTE. PROCESO CANCELADO");
				agregarError("errors.noExiste2", "esquema tarifario vigente para el contrato "+contrato.getNumeroContrato()+
						" de la entidad subcontratada "+entidadSubcontratada.getRazonSocial());
			}
		}
		else
		{
			Log4JManager.info("NO SE ENCONTRÓ CONTRATO VIGENTE. PROCESO CANCELADO");
			agregarError("errors.noExiste2", "contrato vigente para la entidad subcontratada "+
					entidadSubcontratada.getRazonSocial());
		}
		return tarifa;
	}
	
	
	/**
	 * Método implementado para generar el cargo de autorizacion
	 * @param con
	 * @param codigoAutorizacion
	 * @param codigoServicio
	 * @param codigoEntidadSubcontratada
	 * @param fecha
	 * @param hora
	 * @param usuario
	 */
	public void generarCargoAutorizacion(Connection con, String codigoAutorizacion, int codigoServicio, String codigoEntidadSubcontratada,
			String fecha, String hora, UsuarioBasico usuario, String observaciones, String numeroSolicitud) throws IPSException
	{
		
		DtoContratoEntidadSub contrato = new DtoContratoEntidadSub();
		EsquemaTarifario esquema = new EsquemaTarifario();
		ResultadoBoolean respuesta = new ResultadoBoolean(true,"");
		
		//1) Se carga la entidad subcontratada
		DtoEntidadSubcontratada entidadSub = UtilidadesFacturacion.obtenerEntidadSubcontratada(con, codigoEntidadSubcontratada);
		if(!entidadSub.getConsecutivo().equals(""))
		{
			//contrato = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,entidadSub.getConsecutivo(), fecha);
			
			DTOCalculoTarifaServicioArticulo dtoCalculoTarifa = new DTOCalculoTarifaServicioArticulo();
			dtoCalculoTarifa.setFechaVigencia(fecha);
			dtoCalculoTarifa.setCodigoArticuloServicio(codigoServicio);
			dtoCalculoTarifa.setContratoEntidadSubcontratada(contrato);
			dtoCalculoTarifa.setEntidadSubcontratada(entidadSub);
			dtoCalculoTarifa.setEsServicio(true);
			
			//2) Se calcula la tarifa			
			double tarifa = calcularTarifaEntidadSubcontratada(con, dtoCalculoTarifa,esquema);		
						
			//3) *********** SE REGISTRA EL CARGO DE ENTIDAD SUBCONTRATADA**************************************
			DtoCargoEntidadSub cargo = new DtoCargoEntidadSub();
			cargo.setFechaModifica(UtilidadFecha.getFechaActual(con));
			cargo.setHoraModifica(UtilidadFecha.getHoraActual(con));
			cargo.setUsuarioModifica(usuario);
			
			//Se verifica si ya existía un cargo para la solicitud
			String codigoDetalleCargo = this.obtenerCodigoCargoEntidadSubcontratadaAutorizacion(con, codigoAutorizacion);
			
			
			cargo.setConsecutivoAutorizacion(codigoAutorizacion);
			cargo.setNumeroSolicitud(numeroSolicitud);
			cargo.setContrato(contrato);
			cargo.setEntidad(entidadSub);
			cargo.getEsquemaTarifario().setCodigo(esquema.getCodigo());
			if(tarifa>=0){
				cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFCargada);
				cargo.setValorUnitario(tarifa+"");
				this.tarifaCalculadad = tarifa; 
				
			}
			else
			{
				cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFPendiente);
			}
			cargo.setFecha(fecha);
			cargo.setHora(hora);
			cargo.setVieneDespacho(false);
			cargo.setObservaciones(observaciones);
			
			 //Se verifica si ya existía cargo
			if(!codigoDetalleCargo.equals(""))
			{
				cargo.setCodigoDetalleCargo(codigoDetalleCargo);
				respuesta = cargosDao.modificarCargoEntidadSubcontratada(con, cargo);
			}
			else
			{
				respuesta = cargosDao.insertarCargoEntidadSubcontratada(con, cargo);
			}
			
			//Si el proceso es exitoso se continua con la validacion de los errores
			if(respuesta.isTrue())
			{
				//5) *******************SE REGISTRAN LOS ERRORES DEL CARGO*************************************+
				respuesta = cargosDao.registrarErroresCargoEntidadSub(con,  cargo.getCodigoDetalleCargo(), erroresCargo);
				if(!respuesta.isTrue())
				{
					this.erroresProceso.add("Error al guardar errores cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
				}
				//***********************************************************************************************
			}
			else
			{
				this.erroresProceso.add("Error al guardar cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
			}
			//**************************************************************************************************
			
		}
		else
		{
			this.erroresProceso.add("", new ActionMessage("errors.problemasGenericos","cargando informacion de la entidad subcontratada para generar cargo"));
		}
	}
	
	
	/**
	 * Método para obtener el codigo del cargo de la entidad subcontratada
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroPedido
	 * @param codigoArticulo
	 * @param codigoArticuloPrincipal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String obtenerCodigoCargoEntidadSubcontratadaArticulo(Connection con, String numeroSolicitud, String numeroPedido, int codigoArticulo, int codigoArticuloPrincipal)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("numeroPedido",numeroPedido);
		campos.put("codigoArticulo",codigoArticulo);
		campos.put("codigoArticuloPrincipal",codigoArticuloPrincipal);
		
		return cargosDao.obtenerCodigoCargoEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método para obtener el codigo del cargo de la entidad subcontratada
	 */
	@SuppressWarnings("unchecked")
	public String obtenerCodigoCargoEntidadSubcontratadaAutorizacion(Connection con,String codigoAutorizacion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoAutorizacion",codigoAutorizacion);
		
		return cargosDao.obtenerCodigoCargoEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método usado para agregar error
	 * @param llave
	 * @param atributos
	 */
	@SuppressWarnings("unchecked")
	private void agregarError(String llave,String atributos)
	{
		boolean agregado = false;
		
		//Se verifica si el error ya fue agregado
		for(ElementoApResource ele:this.erroresCargo)
		{
			
			String atributosCompleto = "";
			
			Iterator iterador = ele.getAtributosIterator();
			
			while(iterador.hasNext())
			{
				atributosCompleto += (atributosCompleto.equals("")?"":ConstantesBD.separadorSplit) + iterador.next();
			}
			
			if(atributosCompleto.equals(atributos))
			{
				agregado = true;
			}
			
		}
		
		if(!agregado)
		{
			String[] vectorAtr = atributos.split(ConstantesBD.separadorSplit);
			ElementoApResource elemento = new ElementoApResource(llave);
			for(int i=0;i<vectorAtr.length;i++)
			{
				if(!vectorAtr[i].equals(""))
				{
					elemento.agregarAtributo(vectorAtr[i]);
				}
			}
			this.erroresCargo.add(elemento);
		}
	}

	public ArrayList<ElementoApResource> getErroresCargo() {
		return erroresCargo;
	}

	public void setErroresCargo(ArrayList<ElementoApResource> erroresCargo) {
		this.erroresCargo = erroresCargo;
	}
	
	/**
	 * Método que verifica si el centro de costo solicitante esta relacionado con el centro de atencion cubierto de la unidad subcontratada.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public String consultaCentroCostoSolicitanteIgualACubierto(Connection connection, HashMap<String, Object> parametros){
		return cargosDao().consultaCentroCostoSolicitanteIgualACubierto(connection, parametros);
	}

	/**
	 *Este método retorna el valor de la variable tarifaCalculadad
	 */
	public double getTarifaCalculadad() {
		return tarifaCalculadad;
	}

	/**
	 *Este método asigna el valor de la variable tarifaCalculadad
	 */
	public void setTarifaCalculadad(double tarifaCalculadad) {
		this.tarifaCalculadad = tarifaCalculadad;
	}
	
	
	
	/**
	 * Método implementado para generar el cargo de autorizacion (servicio) recibiendo la tarifa ya calculada.
	 * La tarifa se puede calcular con el Anexo 799 o el 438.
	 *
	 * @param con
	 * @param tarifa
	 * @param usuario
	 * @param codigoAutorizacion
	 * @param contrato
	 * @param entidadSub
	 * @param esquema
	 * @param fecha
	 * @param hora
	 * @param observaciones
	 * 
	 * @author Cristhian Murillo
	 */
	public void generarCargoAutorizacionConTarifaCalculada(Connection con, double tarifa, UsuarioBasico usuario, String codigoAutorizacion, DtoContratoEntidadSub contrato, 
			DtoEntidadSubcontratada entidadSub, EsquemaTarifario esquema, String fecha, String hora, String observaciones, String numeroSolicitud)
	{
			
		ResultadoBoolean respuesta = new ResultadoBoolean(true,"");
			
		//3) *********** SE REGISTRA EL CARGO DE ENTIDAD SUBCONTRATADA********
		DtoCargoEntidadSub cargo = new DtoCargoEntidadSub();
		cargo.setFechaModifica(UtilidadFecha.getFechaActual(con));
		cargo.setHoraModifica(UtilidadFecha.getHoraActual(con));
		cargo.setUsuarioModifica(usuario);
		
		//Se verifica si ya existía un cargo para la solicitud
		String codigoDetalleCargo = this.obtenerCodigoCargoEntidadSubcontratadaAutorizacion(con, codigoAutorizacion);
		
		cargo.setConsecutivoAutorizacion(codigoAutorizacion);
		cargo.setNumeroSolicitud(numeroSolicitud);
		cargo.setContrato(contrato);
		cargo.setEntidad(entidadSub);
		cargo.getEsquemaTarifario().setCodigo(esquema.getCodigo());
		
		
		if(tarifa >= 0)
		{
			cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFCargada);
			cargo.setValorUnitario(tarifa+"");
			this.tarifaCalculadad = tarifa; 
		}
		else
		{
			cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFPendiente);
		}
		
		cargo.setFecha(fecha);
		cargo.setHora(hora);
		cargo.setVieneDespacho(false);
		cargo.setObservaciones(observaciones);
		
		 //Se verifica si ya existía cargo
		if(!UtilidadTexto.isEmpty(codigoDetalleCargo))
		{
			cargo.setCodigoDetalleCargo(codigoDetalleCargo);
			respuesta = cargosDao.modificarCargoEntidadSubcontratada(con, cargo);
		}
		else
		{
			respuesta = cargosDao.insertarCargoEntidadSubcontratada(con, cargo);
		}
		
		//Si el proceso es exitoso se continua con la validacion de los errores
		if(respuesta.isTrue())
		{
			//5) *******************SE REGISTRAN LOS ERRORES DEL CARGO*************************************+
			respuesta = cargosDao.registrarErroresCargoEntidadSub(con,  cargo.getCodigoDetalleCargo(), erroresCargo);
			if(!respuesta.isTrue())
			{
				this.erroresProceso.add("Error al guardar errores cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
			}
			//***********************************************************************************************
		}
		else
		{
			this.erroresProceso.add("Error al guardar cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
		}
	}
	
	
	
	
	/**
	 * Método implementado para generar el cargo de autorizacion (articulo) recibiendo la tarifa ya calculada.
	 * La tarifa se puede calcular con el Anexo 799 o el 438.
	 *
	 * @param con
	 * @param tarifaArticulo
	 * @param usuario
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param esquema
	 * @param contrato
	 * @param entidadSub
	 * @param observaciones
	 * @param fecha
	 * @param hora
	 * @param vieneDespacho
	 * @param codigoArticuloPrincipal
	 * 
	 * @author Cristhian Murillo
	 */
	public void generarCargoArticuloConTarifaCalculada(Connection con, double tarifaArticulo, UsuarioBasico usuario, int codigoArticulo, String numeroSolicitud, 
			EsquemaTarifario esquema, DtoContratoEntidadSub contrato, DtoEntidadSubcontratada entidadSub, String observaciones,
			String fecha, String hora, boolean vieneDespacho, int codigoArticuloPrincipal, String codigoAutorizacion)
	{
		ResultadoBoolean respuesta = new ResultadoBoolean(true,"");
		String numeroPedido = null;
			
		//*********** SE REGISTRA EL CARGO DE ENTIDAD SUBCONTRATADA**************************************
		DtoCargoEntidadSub cargo = new DtoCargoEntidadSub();
		cargo.setFechaModifica(UtilidadFecha.getFechaActual(con));
		cargo.setHoraModifica(UtilidadFecha.getHoraActual(con));
		cargo.setUsuarioModifica(usuario);
		
		Log4JManager.info("*** INSERCIÓN DE LA TARIFA ENTIDAD SUBCONTRATADA ***");
		//Se verifica si ya existía un cargo para la solicitud
		String codigoDetalleCargo = this.obtenerCodigoCargoEntidadSubcontratadaArticulo(con, numeroSolicitud, numeroPedido, codigoArticulo, codigoArticuloPrincipal);
		
		Log4JManager.info("CÓDIGO DETALLE CARGO EXISTENTE? "+codigoDetalleCargo);
		cargo.setCodigoArticulo(codigoArticulo);
		cargo.setCodigoArticuloPrincipal(codigoArticuloPrincipal);
		cargo.setNumeroSolicitud(numeroSolicitud);
		cargo.setNumeroPedido(numeroPedido);
		cargo.setContrato(contrato);
		cargo.setEntidad(entidadSub);
		cargo.getEsquemaTarifario().setCodigo(esquema.getCodigo());
		cargo.setObservaciones(observaciones);
		cargo.setConsecutivoAutorizacion(codigoAutorizacion);
		
		if(tarifaArticulo>=0)
		{
			cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFCargada);
			cargo.setValorUnitario(tarifaArticulo+"");
			this.tarifaCalculadad = tarifaArticulo;
		}
		else
		{
			cargo.getEstado().setCodigo(ConstantesBD.codigoEstadoFPendiente);
		}
		cargo.setFecha(fecha);
		cargo.setHora(hora);
		cargo.setVieneDespacho(vieneDespacho);
		
		 //Se verifica si ya existía cargo
		if(!codigoDetalleCargo.equals(""))
		{
			Log4JManager.info("MODIFICACION!!!");
			cargo.setCodigoDetalleCargo(codigoDetalleCargo);
			respuesta = cargosDao.modificarCargoEntidadSubcontratada(con, cargo);
		}
		else
		{
			Log4JManager.info("INSERCIÓN!!!");
			respuesta = cargosDao.insertarCargoEntidadSubcontratada(con, cargo);
		}
		
		//Si el proceso es exitoso se continua con la validacion de los errores
		if(respuesta.isTrue())
		{
			// *******************SE REGISTRAN LOS ERRORES DEL CARGO*************************************+
			Log4JManager.info("SE REGISTRARON ERRORES DEL CARGO!!!");
			respuesta = cargosDao.registrarErroresCargoEntidadSub(con,  cargo.getCodigoDetalleCargo(), erroresCargo);
			if(!respuesta.isTrue())
			{
				Log4JManager.info("OCURRIÓ ERROR AL TRATAR DER EGISTRAR LOS ERRORES DEL CARGO "+respuesta.getDescripcion());
				this.erroresProceso.add("Error al guardar errores cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
			}
			//***********************************************************************************************
		}
		else
		{
			Log4JManager.info("OCURRIÓ ERROR AL TRATAR DER EGISTRAR EL CARGO "+respuesta.getDescripcion());
			this.erroresProceso.add("Error al guardar cargo", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
		}
		
		Log4JManager.info("***fin INSERCIÓN DE LA TARIFA ENTIDAD SUBCONTRATADA***");
	}
	
	
	
	
}
