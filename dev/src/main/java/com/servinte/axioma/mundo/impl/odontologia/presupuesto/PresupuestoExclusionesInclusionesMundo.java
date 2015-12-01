/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.clonacion.UtilidadClonacion;
import util.facturacion.UtilidadesFacturacion;
import util.odontologia.InfoBonoDcto;
import util.odontologia.InfoDefinirSolucitudDsctOdon;
import util.odontologia.InfoInclusionExclusionBoca;
import util.odontologia.InfoInclusionExclusionPiezaSuperficie;
import util.odontologia.InfoPromocionPresupuestoServPrograma;
import util.odontologia.InfoSuperficiePkDetPlan;

import com.princetonsa.action.odontologia.PresupuestoExclusionesInclusionesAction;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.odontologia.DtoContratarInclusion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;
import com.princetonsa.dto.odontologia.DtoDetalleExclusionSuperficies;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoEncabezadoInclusion;
import com.princetonsa.dto.odontologia.DtoExclusionPresupuesto;
import com.princetonsa.dto.odontologia.DtoInclusionesPresupuesto;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoRegistroContratarInclusion;
import com.princetonsa.dto.odontologia.DtoRegistroGuardarExclusion;
import com.princetonsa.dto.odontologia.DtoTotalesContratarInclusion;
import com.princetonsa.dto.odontologia.DtoValorAnticipoPresupuesto;
import com.princetonsa.enu.general.EnumTipoModificacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.odontologia.AutorizacionDescuentosOdon;
import com.princetonsa.mundo.odontologia.DetPromocionesOdontolgicas;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoExclusionesInclusiones;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.mundo.odontologia.Programa;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.princetonsa.mundo.odontologia.PromocionesOdontologicas;
import com.princetonsa.mundo.odontologia.ValidacionesPresupuesto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.mantenimiento.MantenimientoOdontologiaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.excepcion.ValidacionesPresupuestoException;
import com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.facturacion.IDescuentoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluDctoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluServicioConvenioMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.ControlAnticiposContrato;
import com.servinte.axioma.orm.DetPromocionesOdo;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.IncluPresProgramaPromo;
import com.servinte.axioma.orm.IncluPresuConvenio;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.IncluProgramaConvenio;
import com.servinte.axioma.orm.IncluServicioConvenio;
import com.servinte.axioma.orm.InclusionesPresupuesto;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.ProgramasHallazgoPieza;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;

/**
 * @author Juan David Ramírez
 * @since Dec 29, 2010
 */
public class PresupuestoExclusionesInclusionesMundo implements IPresupuestoExclusionesInclusionesMundo
{

	/**
	 * Indica que se debe preguntar si desea generar una solicitud de descuento
	 */
	public static final String PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO = "generar solicitud de descuento";


	public static final String PREGUNTAR_CONTRATAR_SIN_BONOS_VIGENTES = "contratar sin bonos vigentes";
	
	
	public static final String PREGUNTAR_CONTRATAR_SIN_PROMOCIONES_VIGENTES = "contratar sin promociones vigentes";

	
	public static final String PREGUNTAR_CONTRATAR_DESCUENTO_AUTORIZADO_VENCIDO = "contratar con descuento autorizado vencido";

	
	public static final String PREGUNTAR_CONTRATAR_DESCUENTO_PDEF_PPAUTO = "contratar con descuento pendiente";

	
	public static final String PREGUNTAR_CONTRATAR_DESCUENTO_NOAUTO_ANUL = "contratar con descuento no autorizado o anulado";

	
	public static final String PREGUNTAR_CONTRATAR_APLICAR_NUEVOS_BONOS_PROMOCIONES = "contratar sin nuevos bonos y promociones";

	
	public static final String NO_DEFINIDO_PARAMETRO_MOTIVO_ANULACION = "no definido parámetro motivo anulación solicitud";

	
	@Override
	public void seleccionarPrograma(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int indexRegInclusion, int indexConvenio, boolean checkContratado)
	{
		
		DtoPresupuestoOdoConvenio convenio = registrosContratarInclusion.get(indexRegInclusion) // Registro inclusión
		.getListPresupuestoOdoConvenio().get(indexConvenio); // Convenio
		
		if(convenio.getErrorCalculoTarifa().isEmpty()){
			
			convenio.setContratado(checkContratado); // Asignar valor check contratado

			registrosContratarInclusion.get(indexRegInclusion).setInclusionParaContratar(checkContratado);
			
			if(checkContratado)
			{
				// Se desseleccionan los demás convenios para el mismo programa
				this.desseleccionarOtrosConvenios(registrosContratarInclusion.get(indexRegInclusion), indexConvenio);
			}
		}
	}

	/**
	 * Calcula los totales de los convenios según los programas seleccionado
	 * @param registrosContratarInclusion Lista de registros para ser evaluados
	 * @param listaSumatoriaConvenios Lista de los totales de los convenios
	 */
	public void calcularTotalesConvenios(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios, DtoTotalesContratarInclusion totalesContratarInclusion) throws IPSException
	{
		/*
		 * Se reinicializan los totales calculados
		 */
		totalesContratarInclusion.resetTotales();
		
		for(int indiceTotalConvenio=0; indiceTotalConvenio<listaSumatoriaConvenios.size(); indiceTotalConvenio++)
		{
			DtoPresupuestoTotalConvenio totalConvenio=listaSumatoriaConvenios.get(indiceTotalConvenio);
			totalConvenio.setValorTotalContratado(BigDecimal.ZERO);
			for (int indicePrograma=0; indicePrograma<registrosContratarInclusion.size(); indicePrograma++)
			{
				DtoRegistroContratarInclusion programa=registrosContratarInclusion.get(indicePrograma);
	
				// Los índices de los convenios deben concordar exactamente para poder tomar los valores y calcular los totales
				DtoPresupuestoOdoConvenio convenio=programa.getListPresupuestoOdoConvenio().get(indiceTotalConvenio);

				if(UtilidadTexto.getBoolean(convenio.getContratado()))
				{
					totalConvenio.setValorTotalContratado(totalConvenio.getValorTotalContratado().add(convenio.getValorTotalConvenioXProgServ(programa.getProgramaServicio().getCantidad())));
					
					if(!convenio.getSeleccionadoPromocion() && !convenio.getSeleccionadoPromocion()){
						
						totalesContratarInclusion.setTotalInclusionesParaDescuento(totalesContratarInclusion.getTotalInclusionesParaDescuento().add(convenio.getValorTotalConvenioXProgServ(programa.getProgramaServicio().getCantidad())));
					}
				}
			}
			
			totalesContratarInclusion.setTotalInclusiones(totalesContratarInclusion.getTotalInclusiones().add(totalConvenio.getValorTotalContratado()));
		}
		
		totalesContratarInclusion.setTotalInclusionesAContratar(totalesContratarInclusion.getTotalInclusiones());
	}

	/**
	 * Desselecciona el programa para los demás convenios
	 * @param registroContratarInclusion Programa para desseleccionar en los demás convenios
	 * @param indexConvenio índice del convenio seleccionado
	 */
	private void desseleccionarOtrosConvenios(DtoRegistroContratarInclusion registroContratarInclusion, int indexConvenio)
	{
		for (int i=0; i<registroContratarInclusion.getListPresupuestoOdoConvenio().size(); i++)
		{
			if(i!=indexConvenio)
			{
				DtoPresupuestoOdoConvenio convenio=registroContratarInclusion.getListPresupuestoOdoConvenio().get(i);
				convenio.setContratado(false);
			}
			
		}
	}

	@Override
	public void seleccionarPromocion(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,int indexRegInclusion, int indexConvenio, boolean checkPromocion)
	{
		registrosContratarInclusion.get(indexRegInclusion) // Registro inclusión
				.getListPresupuestoOdoConvenio().get(indexConvenio) // Convenio
				.setSeleccionadoPromocion(checkPromocion); // Asignar valor check de la promoción
	}

	@Override
	public void seleccionarTodosProgramasConvenio(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int indexConvenio, boolean checkSelectTodosConvenio)
	{
		for (int indicePrograma=0; indicePrograma<registrosContratarInclusion.size(); indicePrograma++)
		{
			seleccionarPrograma(registrosContratarInclusion, indicePrograma, indexConvenio, checkSelectTodosConvenio);
		}
		
	}
	

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#guardarInclusiones(com.princetonsa.dto.odontologia.DtoContratarInclusion)
	 */
	@Override
	public ResultadoBoolean guardarInclusiones(DtoContratarInclusion dtoContratarInclusion) throws IPSException{

		long codigoIncluPresuEncabezado = ConstantesBD.codigoNuncaValidoLong;
		
		/*
		 * Cuando se va a contratar Directamente
		 */
		if(dtoContratarInclusion.getEstado().equals(ConstantesIntegridadDominio.acronimoContratado)){
			
			return contratarInclusiones(dtoContratarInclusion.getErrores(), dtoContratarInclusion.getPaciente(), dtoContratarInclusion.getRegistrosContratarInclusion(), 
					dtoContratarInclusion.getListaSumatoriaConvenios(), dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getCodigoCentroAtencion(), 
					dtoContratarInclusion.getCodigoPlanTratamiento(), dtoContratarInclusion.getLoginUsuario(), dtoContratarInclusion.getConnection(), 
					dtoContratarInclusion.getCodigoPresupuesto(), dtoContratarInclusion.isValidarDescuento(), codigoIncluPresuEncabezado);
		
		}else if(dtoContratarInclusion.getEstado().equals(ConstantesIntegridadDominio.acronimoContratarPrecontratado)){
			
			return contratarInclusionesPrecontratada(dtoContratarInclusion);
			
		}else if(dtoContratarInclusion.getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
			
			/*
			 * Cuando se va a Precontratar 
			 */
			codigoIncluPresuEncabezado = precontratarContratarInclusiones(dtoContratarInclusion.getErrores(), dtoContratarInclusion.getRegistrosContratarInclusion(), dtoContratarInclusion.getEstado(), 
					dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getLoginUsuario(), dtoContratarInclusion.getCodigoPresupuesto(), dtoContratarInclusion.getCodigoCentroAtencion()
					, dtoContratarInclusion.getConnection());

			if (codigoIncluPresuEncabezado > 0){
				
				return registrarDescuentoOdontologico(dtoContratarInclusion, codigoIncluPresuEncabezado);
				
			}else{
				
				return retornarError();
			}
			
		}else{
			
			return retornarError();
		}
	}

	
	/**
	 * 
	 * Método que se encarga de realizar todo el proceso necesario para la contratación
	 * de las inclusiones.
	 * 
	 * @param errores
	 * @param paciente
	 * @param registrosContratarInclusion
	 * @param listaSumatoriaConvenios
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion Centro de atención donde se genera la inclusión
	 * @param codigoPlanTratamiento
	 * @param loginUsuario
	 * @param con
	 * @param codigoPresupuesto
	 * @param validarDescuento Validar el descuento para mostrar mensaje
	 * @param codigoIncluPresuEncabezado
	 * @return
	 */
	private ResultadoBoolean contratarInclusiones (ActionErrors errores, PersonaBasica paciente,
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,  
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
			int codigoInstitucion, int codigoCentroAtencion, BigDecimal codigoPlanTratamiento, String loginUsuario, 
			Connection con, BigDecimal codigoPresupuesto, boolean validarDescuento, long codigoIncluPresuEncabezado) throws IPSException{
		
		
		ResultadoBoolean resultado = new ResultadoBoolean();
		resultado.setResultado(true);
		resultado.setDescripcion("");
		
		boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));

		ArrayList<DtoValorAnticipoPresupuesto> listaValorAnticipoPresupuesto = new ArrayList<DtoValorAnticipoPresupuesto>();
		
		try
		{
			listaValorAnticipoPresupuesto = validarGuardarContratar(errores, registrosContratarInclusion, listaSumatoriaConvenios, 
					codigoInstitucion, codigoPlanTratamiento, paciente.getCodigoIngreso(), validarDescuento, codigoCentroAtencion);
			
		} catch (ValidacionesPresupuestoException e)
		{
			resultado.setDescripcion(e.getMensaje());
			return resultado;
		}

		if(errores.isEmpty()){
			
			/*
			 * Si esto se cumple se debe crear la estructura de registros igual a la precontratación
			 * Quiere decir que se contrato directamente sin realizar solicitudes de descuento
			 */
			if(codigoIncluPresuEncabezado  <= 0){
				
				codigoIncluPresuEncabezado = precontratarContratarInclusiones (errores, registrosContratarInclusion, ConstantesIntegridadDominio.acronimoContratado, 
						codigoInstitucion, loginUsuario, codigoPresupuesto, codigoCentroAtencion, con);
			}
			
//			else
//			{
//				/*
//				 * Debe realizar una actualización del estado y de otros valores del encabezado.
//				 * Esto se hace solo cuando se va a contratar una solicitud de inclusión en estado
//				 * precontratado
//				 */
//				
//				if(!actualizarEncabezado(codigoIncluPresuEncabezado, codigoCentroAtencion, loginUsuario, codigoPresupuesto)){
//					
//					return retornarError();
//				}
//			}
			
			if (codigoIncluPresuEncabezado <= 0){
				
				return retornarError();
				
			}
			else{
				
				/* 
				 * Esto se hace para que se sincronicen los objetos en sesión y la base de datos
				 * y se pueda continuar con los demás registros
				 */
				
				UtilidadTransaccion.getTransaccion().flush();
			}
			
			if(!actualizarContratadoPlanTratamientoSeccionPiezaSuperficie(registrosContratarInclusion, con, loginUsuario, utilizaProgramas)){
	
				return retornarError();
			}
			
			if(!insertarInclusion(registrosContratarInclusion, utilizaProgramas, codigoPresupuesto, paciente.getCodigoCuenta(), con, loginUsuario, codigoInstitucion, codigoIncluPresuEncabezado))
			{	
				return retornarError();
			}
			
			/*
			 * Si llega a este punto significa que se ha registrado la contratación de las inclusiones.
			 * Debemos validar una vez más el incumplimiento del paciente para asociarlo a los mensajes de error
			 * ya que en el proceso de validación anterior no se hizo por lo que no es un error bloqueante
			 */
			if(validarIncumplimientoPaciente(codigoPlanTratamiento, paciente.getCodigoIngreso())){
				
				MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
				errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.incumplimientoContrato")));
			}
			
			if(listaValorAnticipoPresupuesto !=null){
				
				for(DtoValorAnticipoPresupuesto dtoAnticipo: listaValorAnticipoPresupuesto)
		   		{
		   			if(!Contrato.modificarValorAnticipoReservadoPresupuesto(con, dtoAnticipo.getContrato(), dtoAnticipo.getValorAnticipo()))
		   			{

		   				return retornarError();
		   			}
		   		}
			}
			
			return resultado;
			
		}else{
			
			resultado.setResultado(false);
			resultado.setDescripcion("erroresValidacion");
			return resultado;
		}
	}
	
	/**
	 * Método que realiza todas las validaciones necesarias para permitir la contratación
	 * de las inclusiones realizadas al presupuesto del paciente.
	 *
	 * @param errores
	 * @param registrosContratarInclusion
	 * @param listaSumatoriaConvenios
	 * @param codigoInstitucion
	 * @param codigoPlanTratamiento
	 * @param codigoIngreso
	 * @param validarDescuento Validar el descuento al contratar 
	 * @param codigoCentroAtencion Centro de atención donde se genera la inclusión
	 * @return
	 * @throws ValidacionesPresupuestoException Lanza excepción en el caso que se necesite genera la solicitud de descuento
	 */
	private ArrayList<DtoValorAnticipoPresupuesto> validarGuardarContratar(ActionErrors errores,
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,  
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
			int codigoInstitucion, BigDecimal codigoPlanTratamiento,  int codigoIngreso, 
			boolean validarDescuento, int codigoCentroAtencion) throws ValidacionesPresupuestoException
	{
		
		ArrayList<DtoValorAnticipoPresupuesto> listaAnticiposPresupuesto = new ArrayList<DtoValorAnticipoPresupuesto>();
		
		boolean registroError = false;
		boolean continuar;

		//validar si se seleccion el programa en algun convenio para marcarlo como contratado
		
		for (DtoRegistroContratarInclusion registroContratarInclusion : registrosContratarInclusion) {
			
			DtoPresuOdoProgServ dtoProSer = registroContratarInclusion.getProgramaServicio();
			
			dtoProSer.setContratado(ConstantesBD.acronimoNo);
			
			for(DtoPresupuestoOdoConvenio dtoConvenio : registroContratarInclusion.getListPresupuestoOdoConvenio())
			{
				if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
				{
					dtoProSer.setContratado(ConstantesBD.acronimoSi);
					break;
				}
			}
		}
		
		/*
		 * Esta validación la sacamos del ciclo, ya que no es necesario
		 * que se realice cada vez, por convenio a contratar.
		 */
		boolean validacionIncumplimientoPaciente = validarIncumplimientoPaciente(codigoPlanTratamiento, codigoIngreso);
		
		if(!validarConsecutivoInclusiones(codigoInstitucion)){
			
			MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
			errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.noConsecutivoInclusiones")));
			
			return null;
		}
		

		for(DtoPresupuestoTotalConvenio totalConvenio : listaSumatoriaConvenios)
		{
			continuar = false;
			
			if(totalConvenio.getValorTotalContratado().doubleValue()>0){
				
				boolean pacientePagaAtencion= Contrato.pacientePagaAtencion(totalConvenio.getContrato());
				DtoValorAnticipoPresupuesto valorAnticipoPresupuesto = null;
				
				if(pacientePagaAtencion)
				{
					continuar = true;
					
				}else if(!pacientePagaAtencion){
			
					boolean controlaAnticipoContratar= Contrato.controlaAnticipos(totalConvenio.getContrato());
					
					if(controlaAnticipoContratar)
					{
						IControlAnticiposContratoMundo controlAnticiposContratoMundo = FacturacionFabricaMundo.crearControlAnticiposContratoMundo();

						ArrayList<ControlAnticiposContrato> listaControlAnticiposContrato = new ArrayList<ControlAnticiposContrato>();

						listaControlAnticiposContrato = controlAnticiposContratoMundo.determinarContratoRequiereAnticipo(totalConvenio.getContrato());
						
						if(!Utilidades.isEmpty(listaControlAnticiposContrato))
						{
							//Requiere Anticipo al contratar
							if(validarValorAnticipoDisponible(errores, totalConvenio)){
								
								registroError = true;
							}
							
						}else{
							
							continuar = true;
						}

					}else{
						
						continuar = true;
					}
				}
				
				if(continuar){
					
					registroError = iniciarValidacionDesdeIncumplimientoPaciente(errores, valorAnticipoPresupuesto, totalConvenio, 
							codigoIngreso, codigoInstitucion, codigoPlanTratamiento, pacientePagaAtencion, validarDescuento, codigoCentroAtencion, 
							validacionIncumplimientoPaciente);
					
					if(!registroError){
				
						if(valorAnticipoPresupuesto!=null){
							
							listaAnticiposPresupuesto.add(valorAnticipoPresupuesto);
						}
						
					}else
					{
						listaAnticiposPresupuesto = null;
						break;
					}
					
				}else if (registroError){
					
					listaAnticiposPresupuesto = null;
					break;
				}
			}
		}
	
		return listaAnticiposPresupuesto;
		
	
//		if(errores.isEmpty())
//		{	
//			/*VALIDACION 1. validar que los programas o servicios del plan de tratamiento esten en estado pendiente o cancelado*/
//			//logger.info("---------VALIDACION 1. validar que los programas o servicios del plan de tratamiento esten en estado pendiente o cancelado");
//			for (DtoRegistroContratarInclusion registroContratarInclusion : registrosContratarInclusion) {
//				
//				DtoPresuOdoProgServ dtoProSer = registroContratarInclusion.getProgramaServicio();
//				
//				for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
//				{
//				    if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
//				    {	
//				    	for(DtoPresupuestoPiezas dtoPiezas: dtoProSer.getListPresupuestoPiezas())
//				    	{
//				    		if(ValidacionesPresupuesto.existeProgramaServicioPresupuestoEnPlanTratamiento(dtoPiezas.getCodigoPk(), ConstantesIntegridadDominio.acronimoEstadoPendiente, utilizaProgramas)
//				    				.doubleValue()<=0)
//				    		{
////				    			if(ValidacionesPresupuesto.esProgramaServicioAdicionPlanTtoPresupuesto(dtoPiezas.getPieza(), dtoPiezas.getSuperficie(), dtoPiezas.getHallazgo(), dtoPiezas.getSeccion(), this.getDtoPresupuesto().getCodigoPK(), this.getUtilizaPrograma(), new BigDecimal(dtoProSer.getProgramaOServicio(this.getUtilizaPrograma())))
////				    					.doubleValue()<=0)
////				    			{
////					    			if(utilizaProgramas)
////					    			{	
////					    				errores.add("", new ActionMessage("errors.notEspecific", "El programa "+dtoProSer.getPrograma().getNombre()+" NO esta en estado pendiente en el plan de tratamiento"));
////					    			}
////					    			else
////					    			{
////					    				errores.add("", new ActionMessage("errors.notEspecific", "El servicio "+dtoProSer.getServicio().getNombre()+" NO esta en estado pendiente en el plan de tratamiento"));
////					    			}
////				    			}	
//				    		}
//				    	}	
//				    }
//				}
//			}
//		}	
	}
	

	/**
	 * Método que se encarga de centralizar el proceso de validaciones para la contratación
	 * de las inclusiones desde la validación: Parametrización del Consecutivo disponible
	 * de Inclusiones.
	 * 
	 * Este método hace llamado a las demás validaciones.
	 * 
	 * @param errores
	 * @param valorAnticipoPresupuesto
	 * @param totalConvenio
	 * @param codigoIngreso
	 * @param codigoInstitucion
	 * @param codigoPlanTratamiento
	 * @param pacientePagaAtencion 
	 * @param validarDescuento Validar el descuento
	 * @param centroAtencion Centro de atención donde se genera la inclusión
	 * @return
	 * @throws ValidacionesPresupuestoException Lanza excepción en el caso que se necesite genera la solicitud de descuento
	 */
	private boolean iniciarValidacionDesdeIncumplimientoPaciente (ActionErrors errores, DtoValorAnticipoPresupuesto valorAnticipoPresupuesto, 
			DtoPresupuestoTotalConvenio totalConvenio,
			int codigoIngreso, int codigoInstitucion, 
			BigDecimal codigoPlanTratamiento, boolean pacientePagaAtencion, 
			boolean validarDescuento, int centroAtencion, boolean validacionIncumplimientoPaciente) throws ValidacionesPresupuestoException{
		
		boolean registroError = false;

		if(errores.isEmpty()){

			/*
			 * Si el paciente no ha incumplido se puede realizar la solicitud de descuento
			 */
			if(!validacionIncumplimientoPaciente && validarDescuento){
				
				// Validación del descuento odontológico
				
				if(validarDescuentoOdontologico(centroAtencion))
				{
					throw new ValidacionesPresupuestoException(PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO);
				}
			
			}else if(!pacientePagaAtencion){
				
				if(!validarSaldoAnticipoVersusValorContrato(errores, totalConvenio, valorAnticipoPresupuesto)){
					
					//Retornar a la pagina de inclusiones.
					registroError = true;
				}
			}
		}else{
			
			//Retornar a la pagina de inclusiones.
			registroError = true;
		}
		
		return registroError;
	}
	
	
	/**
	 * Método que se encarga de validar si el paciente ha incumplido un contrato
	 * 
	 * @param codigoPlanTratamiento
	 * @param ingreso
	 * @return true cuando el paciente ha incumplido un contrato, false de lo contrario
	 */
	private boolean validarIncumplimientoPaciente(BigDecimal codigoPlanTratamiento, int ingreso){
		
		return ValidacionesPresupuesto.existeCancelacionContratoXIncumplimiento(codigoPlanTratamiento, ingreso);
	}
	
	/**
	 * Valida si hay parametrizados descuentos odontológicos
	 * @param centroAtencion Centro de atención donde se está generando la inclusión
	 * @return true en caso de que existan descuentos parametrizados
	 */
	private boolean validarDescuentoOdontologico(int centroAtencion)
	{
		IDescuentoOdontologicoMundo descuentoOdontologicoMundo=MantenimientoOdontologiaFabricaMundo.crearDescuentoOdontologicoMundo();
		
		ArrayList<DtoDescuentosOdontologicos> descuentos=descuentoOdontologicoMundo.validarExistenciaDescuentosOdontologicosXPresupuesto(centroAtencion, UtilidadFecha.getFechaActualTipoBD());

		if(descuentos.size()>0)
		{
			return true;
		}
		
		descuentos=descuentoOdontologicoMundo.validarExistenciaDescuentosOdontologicosXAtencion(centroAtencion);

		if(descuentos.size()>0)
		{
			return true;
		}

		return false;
	}
	
	/**
	 * Método que valida si el valor del Anticipo Disponible para el convenio es mayor a cero
	 * 
	 * @param errores
	 * @param totalConvenio
	 * @return
	 */
	private boolean validarValorAnticipoDisponible (ActionErrors errores, DtoPresupuestoTotalConvenio totalConvenio){
		
		boolean resultado = true;
	
		double valorAnticipoDisponible=0;
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoControlAnticiposContrato> array= Contrato.obtenerControlAnticipos(con, totalConvenio.getContrato());
		UtilidadBD.closeConnection(con);
		if(array.size()>0)
		{
			valorAnticipoDisponible= array.get(0).getValorAnticipoDisponible();
		}
		
		if(valorAnticipoDisponible <= 0){
			
			MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
			errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.noAnticipoDisponible", totalConvenio.getConvenio().getNombre())));
			return false;
		}
		
		return resultado;
	}
	
	/**
	 * Método que se encarga de validar que el consecutivo de Inclusiones si está definido
	 * 
	 * @param codigoInstitucion
	 */
	private boolean validarConsecutivoInclusiones(int codigoInstitucion){
		
		return estaParametrizadoConsecutivo(codigoInstitucion, ConstantesBD.nombreConsecutivoInclusionProgServOdo);
	}

	
	/**
	 * Método que permite determinar si se encuentra realizada 
	 * la parametrización del consecutivo de inclusiones o el de exclusiones
	 * 
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	private boolean estaParametrizadoConsecutivo (int codigoInstitucion, String consecutivo){
		
		Connection con = UtilidadPersistencia.getPersistencia().obtenerConexion();
		String valor = UtilidadBD.obtenerValorActualTablaConsecutivos(con, consecutivo, codigoInstitucion);
		boolean existe = false;
		
		if(UtilidadTexto.isNumber(valor)){
			
			double valorConsecutivo = Utilidades.convertirADouble(valor);
			
			if(valorConsecutivo>0){
				
				existe = true;
			}
		}
		
		return existe;
		
	}

	
	/**
	 * Método que se encarga de realizar las validaciones del valor actual del anticipo 
	 * frente al valor a contratar por las inclusiones.
	 * 
	 * @param errores
	 * @param totalConvenio
	 * @param valorAnticipoPresupuesto
	 * @return
	 */
	private boolean validarSaldoAnticipoVersusValorContrato (ActionErrors errores, DtoPresupuestoTotalConvenio totalConvenio, DtoValorAnticipoPresupuesto valorAnticipoPresupuesto){
		
		boolean resultado = true;
		
		if(validarValorContratarMayorAnticipoConvenio(errores, totalConvenio)){
			
			if(validarValorMaximoPorPaciente(errores, totalConvenio)){
				
				if(errores.isEmpty()){
					
					valorAnticipoPresupuesto = new DtoValorAnticipoPresupuesto(totalConvenio.getTotalContratado(), totalConvenio.getContrato());
				}
			
			}else{
				
				resultado = false;
			}
			
		}else{
			
			resultado = false;
		}
		
		return resultado;
	}
	
	
	/**
	 * Método que se encarga de validar si el el valor total a contratar supera o no el valor
	 * máximo por paciente permitido para ese convenio.
	 * 
	 * @param errores
	 * @param totalConvenio
	 */
	private boolean validarValorContratarMayorAnticipoConvenio (ActionErrors errores, DtoPresupuestoTotalConvenio totalConvenio){
		
		boolean resultado = true;
		
		if(ValidacionesPresupuesto.esValorPresupuestoMayorAnticipo(totalConvenio.getTotalContratado(), totalConvenio.getContrato()))
		{
			MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
			errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.anticipoNoCubrePrograma", totalConvenio.getConvenio().getNombre())));
			resultado =  false;
		}
		
		return resultado;
	}
	
	/**
	 * Método que se encarga de validar si el el valor total a contratar supera o no el valor
	 * máximo por paciente permitido para ese convenio.
	 * 
	 * @param errores
	 * @param totalConvenio
	 */
	private boolean validarValorMaximoPorPaciente (ActionErrors errores, DtoPresupuestoTotalConvenio totalConvenio){
		
		boolean resultado = true;
		
		if (ValidacionesPresupuesto.esValorTotalConvenioMayorValorMaximoXPaciente(totalConvenio.getContrato(), totalConvenio.getTotalContratado()))
		{
			MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
			errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.valorProgramaSuperaValorMaximoPaciente", totalConvenio.getConvenio().getNombre())));
			resultado = false;
		}
		
		return resultado;
	}
	

	/**
	 * Método que actualiza a contratado el Plan de Tratamiento Seccion Pieza Superficie.
	 * 
	 * @param registrosContratarInclusion
	 * @param con
	 * @param loginUsuario
	 * @param utilizaProgramas
	 * @return
	 */
	private boolean actualizarContratadoPlanTratamientoSeccionPiezaSuperficie(
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, 
			Connection con, 
			String loginUsuario, 
			boolean utilizaProgramas)
	{
		
		for (DtoRegistroContratarInclusion registroInclusion : registrosContratarInclusion) {
			
			DtoPresuOdoProgServ presupuestoProgamaServ = registroInclusion.getProgramaServicio();
			
			for (DtoPresupuestoPiezas pieza : presupuestoProgamaServ.getListPresupuestoPiezas()) {

				DtoProgramasServiciosPlanT newPrograma = new DtoProgramasServiciosPlanT();											 		          
				newPrograma.setDetPlanTratamiento(pieza.getCodigoDetallePlanTratamiento());
				
				if(utilizaProgramas)
				{	
					newPrograma.setPrograma(presupuestoProgamaServ.getPrograma());
				}	
				else
				{	
					newPrograma.setServicio(presupuestoProgamaServ.getServicio());
				}
				
				newPrograma.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
				newPrograma.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);	
				newPrograma.setPorConfirmado(ConstantesBD.acronimoNo);
				newPrograma.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
				
				if (!PlanTratamiento.modicarEstadosDetalleProgServ(newPrograma , con))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	
	/**
	 * Metodo para insertar la nueva inclusion en las estructuras de presupuesto
	 * 
	 * @param registrosContratarInclusion
	 * @param utilizaProgramas
	 * @param codigoPresupuesto
	 * @param codigoCuenta
	 * @param con
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param codigoIncluPresuEncabezado
	 * @return
	 */
	private boolean insertarInclusion(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,
			boolean utilizaProgramas, BigDecimal codigoPresupuesto,
			int codigoCuenta, Connection con, String loginUsuario, int codigoInstitucion, long codigoIncluPresuEncabezado) throws IPSException
	{
		
		boolean resultado = false;
		
		BigDecimal valorTotal = new BigDecimal(0);
		 
		for (DtoRegistroContratarInclusion registroContratarInclusion : registrosContratarInclusion) {
			
			valorTotal = new BigDecimal(0);
			
			DtoPresuOdoProgServ dtoPresuOdoProgServ = registroContratarInclusion.getProgramaServicio();

   			for(DtoPresupuestoOdoConvenio dtoConvenio : registroContratarInclusion.getListPresupuestoOdoConvenio())
		   	{
			   	if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
			   	{
				   	//debemos actualizar el indicativo de si utiliza reserva de anticipo
				    //dtoConvenio.setReservaAnticipo(forma.getUtilizaReservaAnticipo(dtoConvenio.getContrato().getCodigo()));
			   		
				   	if(!dtoConvenio.getSeleccionadoBono())
				   	{
					   	dtoConvenio.setValorDescuentoBono(new BigDecimal(0));
				   	}
				   	
				   	if(!dtoConvenio.getSeleccionadoPromocion())
			   		{
					   	dtoConvenio.setValorDescuentoPromocion(new BigDecimal(0));
				   	}
				   	
				   	dtoConvenio.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
				   	
//				   	Temporalmente no se va a realizar esto
				   	
//				   	if(UtilidadTexto.isEmpty(dtoConvenio.getErrorCalculoTarifa()))
//				   	{
//					   	if(!PresupuestoOdontologico.modificarPresupuestoConvenio(dtoConvenio , con))
//					   	{
//					   		return resultado;
//					   	}
//				   	}
				   	
				  	valorTotal = dtoConvenio.getValorTotalConvenioXProgServ(registroContratarInclusion.getProgramaServicio().getCantidad());
			   	}
		   	}

   			/*
   			 * 1.INSERTAMOS LA INCLUSIÖN DE PRESUPUESTO
   			 * 
   			 * Cuando se contrata una inclusión precontratada, los registros realizados 
   			 * previamente en esta tabla son eliminados e ingresados nuevamente.
   			 * 
   			 */
   			double codigoInclusion= insertarInclusionTabla(con, valorTotal, codigoIncluPresuEncabezado, dtoPresuOdoProgServ.getCodigoProgramaHallazgoPieza());
   			
   			if(codigoInclusion<=0)
   			{
   				return resultado;
   				
   			}else{
   				
   				registroContratarInclusion.setCodigoInclusion(codigoInclusion);
   			}
   			
   			double presupuestoOdoProgServ = insertarPresupuestoOdoProgServ(dtoPresuOdoProgServ,	codigoPresupuesto, 
   					loginUsuario, con, new BigDecimal(codigoInclusion));
   			
   			if(presupuestoOdoProgServ<=0)
   			{
   				return resultado;
   			}

   			if(!insertarPresupuestoPieza(con, dtoPresuOdoProgServ.getListPresupuestoPiezas(), presupuestoOdoProgServ))
   			{
   				return resultado;
   			}
   			
			//4. INSERTAMOS LOS CONVENIOS
			////si llega a este punto debemos hacer la insercion de los calculos de presupuesto x convenio
			if(!insertarConvenioYDetalle(registroContratarInclusion.getListPresupuestoOdoConvenio(), con, utilizaProgramas, presupuestoOdoProgServ, loginUsuario))
			{
				return resultado;
			}
   			
   			
   			for (DtoPresupuestoPiezas pieza : dtoPresuOdoProgServ.getListPresupuestoPiezas()) {
				
   				boolean resultadoInsert = insertarSeccionPlanTratamiento(codigoCuenta, con, 
   						pieza.getCodigoDetallePlanTratamiento(), 
   						dtoPresuOdoProgServ.getProgramaOServicio(utilizaProgramas), 
   						pieza.getPieza(), pieza.getSuperficie(),
   						pieza.getHallazgo(), pieza.getSeccion(),
   						utilizaProgramas, 
						codigoPresupuesto,
						loginUsuario);
   				
   				if(!resultadoInsert)
   	   			{
   	   				return resultado;
   	   			}
			}
   			
   		 	if(!modificarBonosPromocionesPresupuesto(registroContratarInclusion.getListPresupuestoOdoConvenio(), dtoPresuOdoProgServ.getTipoModificacion(), con))
   			{
   		 		return resultado;
   			}
   		 	
   		 	resultado = true;
   		 	registroContratarInclusion.setInclusionContratada(resultado);
		}
		
		return resultado;
	}
	
	
	/**
	 * Método para modificar el valor de los bonos y las promociones usados en la contratación
	 * de las inclusiones
	 * 
	 * @param listadoPresupuestoOdoConvenio
	 * @param tipoModificacion
	 * @param con
	 * @return
	 */
	private boolean modificarBonosPromocionesPresupuesto(ArrayList<DtoPresupuestoOdoConvenio> listadoPresupuestoOdoConvenio, EnumTipoModificacion tipoModificacion, Connection con) 
	{
		
		if(tipoModificacion!=EnumTipoModificacion.ELIMINADO)
		{
			for(DtoPresupuestoOdoConvenio dtoConvenio: listadoPresupuestoOdoConvenio)
			{
				if(dtoConvenio.getCodigoPK().doubleValue()>0 && dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
				{	 
					if(!PresupuestoOdontologico.modificarBonos(con, dtoConvenio ))
					{
						return false;
					}
					
					if(!PresupuestoOdontologico.modificarPromociones(con, dtoConvenio))
					{
							
						return false;
					}
				}	 
			}
		}
		
		return true;
	}
	
	
	/**
	 * Método que se encarga de realizar el registro de la inclusión
	 * 
	 * @param con
	 * @param valorTarifa
	 * @param codigoIncluPresuEncabezado
	 * @param codigoProgramaHallazgoPieza
	 * @return
	 */
	private double insertarInclusionTabla(Connection con, BigDecimal valorTarifa, long codigoIncluPresuEncabezado, BigDecimal codigoProgramaHallazgoPieza) 
	{

		DtoInclusionesPresupuesto dtoInclusion= new DtoInclusionesPresupuesto(valorTarifa, codigoIncluPresuEncabezado, codigoProgramaHallazgoPieza.longValue());
		
		return PresupuestoExclusionesInclusiones.guardarInclusion(con, dtoInclusion);

	}
	
	
	/**
	 * Método que realiza el registro de un nuevo programa / servicio en el presupuesto
	 * 
	 * @param dtoPresupuestoProgServ
	 * @param codigoPresupuesto
	 * @param loginUsuario
	 * @param con
	 * @param inclusion
	 * @return
	 */
	private double insertarPresupuestoOdoProgServ(DtoPresuOdoProgServ dtoPresupuestoProgServ, BigDecimal codigoPresupuesto, String loginUsuario,
													Connection con, BigDecimal inclusion) 
	{
		double presupuestoOdoProgServ=ConstantesBD.codigoNuncaValidoDouble;
		
		dtoPresupuestoProgServ.setCantidad(1);
		dtoPresupuestoProgServ.setCodigoPk(new BigDecimal(0));
		dtoPresupuestoProgServ.setPresupuesto(codigoPresupuesto);
		dtoPresupuestoProgServ.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
		dtoPresupuestoProgServ.setInclusion(inclusion);
		presupuestoOdoProgServ= PresupuestoOdontologico.guardarPresupuestoOdoProgramaServicio(dtoPresupuestoProgServ, con);
		
		return presupuestoOdoProgServ;
	}
	
	
	/**
	 * Método que realiza el registro de las piezas asociadas a las inclusiones
	 *  
	 * @param con
	 * @param listadoPresupuestoPiezas
	 * @param presupuestoOdoProgServ
	 * @return
	 */
	private boolean insertarPresupuestoPieza(Connection con, ArrayList<DtoPresupuestoPiezas> listadoPresupuestoPiezas, double presupuestoOdoProgServ) 
	{
		
		for(DtoPresupuestoPiezas pieza: listadoPresupuestoPiezas)
		{	
			pieza.setPresupuestoOdoProgServ(new BigDecimal(presupuestoOdoProgServ));
			double presupuestoPieza= PresupuestoOdontologico.guardarPresupuestoPiezas(pieza, con);
			
			if(presupuestoPieza<=0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 
	 * Método que se encarga de registrar la información para cada inclusión contratada
	 * en el plan de tratamiento
	 * 
	 * @param codigoCuenta
	 * @param con
	 * @param codigoPkDetallePlanTratamiento
	 * @param programaOservicio
	 * @param codigoPiezaDentalOPCIONAL
	 * @param superficieOPCIONAL
	 * @param hallazgo
	 * @param seccion
	 * @param utilizaProgramas
	 * @param codigoPresupuesto
	 * @param loginUsuario
	 * @return
	 */
	private boolean insertarSeccionPlanTratamiento (int codigoCuenta, Connection con,
														BigDecimal codigoPkDetallePlanTratamiento,
														Double programaOservicio,
														BigDecimal codigoPiezaDentalOPCIONAL,
														BigDecimal superficieOPCIONAL,
														BigDecimal hallazgo,
														String seccion, 
														boolean utilizaProgramas, 
														BigDecimal codigoPresupuesto,
														String loginUsuario) 
	{
		if(ValidacionesPresupuesto.existeProgramaServicioAdicionPlanTtoPresupuesto( codigoPiezaDentalOPCIONAL, superficieOPCIONAL, hallazgo, seccion, 
				codigoPresupuesto, utilizaProgramas, new BigDecimal(programaOservicio))
				.doubleValue()<=0)
		{		
			DtoPlanTratamientoPresupuesto dtoPlan= new DtoPlanTratamientoPresupuesto();
			dtoPlan.setActivo(true);
			dtoPlan.setDetPlanTratamiento(codigoPkDetallePlanTratamiento);
			dtoPlan.setFHU(new DtoInfoFechaUsuario(loginUsuario));
			dtoPlan.setPresupuesto(codigoPresupuesto);
			
			if(utilizaProgramas)
			{
				dtoPlan.setPrograma(new BigDecimal(programaOservicio));
				
				ArrayList<DtoDetalleProgramas> arrayDetalleProgramas= ProgramasOdontologicos.cargarDetallePrograma(programaOservicio);
				//obtenemos los servicios del programa
				for(DtoDetalleProgramas serv: arrayDetalleProgramas)
				{	
					dtoPlan.setProgramaServicioPlanTratamientoFK(ValidacionesPresupuesto.obtenerCodigoPkProgramaServicioPlanTratamiento(codigoPkDetallePlanTratamiento, dtoPlan.getPrograma(), serv.getServicio()));
					dtoPlan.setServicio(serv.getServicio());
					BigDecimal codigoPlanTratamientoPresupuesto= PresupuestoOdontologico.insertarPlanTtoPresupuesto(con, dtoPlan, utilizaProgramas);
					
					if(codigoPlanTratamientoPresupuesto.doubleValue()  <=0 )
					{
						return false;
					}
				}	
			}
			else
			{
				dtoPlan.setServicio(programaOservicio.intValue());
				dtoPlan.setProgramaServicioPlanTratamientoFK(ValidacionesPresupuesto.obtenerCodigoPkProgramaServicioPlanTratamiento(
						codigoPkDetallePlanTratamiento, BigDecimal.ZERO, dtoPlan.getServicio()));
				BigDecimal codigoPlanTratamientoPresupuesto= PresupuestoOdontologico.insertarPlanTtoPresupuesto(con, dtoPlan, utilizaProgramas);
				if(codigoPlanTratamientoPresupuesto.doubleValue()  <=0 )
				{
					return false;
				}
			}
		}
		else
		{
			if(utilizaProgramas)
			{
				ArrayList<DtoDetalleProgramas> arrayDetalleProgramas= ProgramasOdontologicos.cargarDetallePrograma(programaOservicio);
				//obtenemos los servicios del programa
				for(DtoDetalleProgramas serv: arrayDetalleProgramas)
				{
					//actualizamos el programa servicio plan t de la relacion
					PresupuestoOdontologico.actualizarPlanTtoPresupuestoProgServ(con, ValidacionesPresupuesto.obtenerCodigoPkProgramaServicioPlanTratamiento(
							codigoPkDetallePlanTratamiento, new BigDecimal(programaOservicio), 
							serv.getServicio()), codigoPkDetallePlanTratamiento, 
							new InfoDatosDouble(programaOservicio, ""), serv.getServicio(), codigoPresupuesto);
				}	
			}	
		}
		
		return true;
	}
	
	

	/**
	 * Método que se encarga de registrar el detalle y la relación de cada uno de los programas y 
	 * sus servicios con cada uno de los convenios
	 * 
	 * @param listPresupuestoOdoConvenio
	 * @param con
	 * @param utilizaProgramas
	 * @param presupuestoOdoProgServ
	 * @param loginUsuario
	 * @return
	 */
	private boolean insertarConvenioYDetalle (ArrayList<DtoPresupuestoOdoConvenio> listPresupuestoOdoConvenio, Connection con, boolean utilizaProgramas, double presupuestoOdoProgServ, String loginUsuario)
	{
		
		for(DtoPresupuestoOdoConvenio dtoConvenio : listPresupuestoOdoConvenio)
	   	{
			dtoConvenio.setPresupuestoOdoProgServ(new BigDecimal(presupuestoOdoProgServ));
			dtoConvenio.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
			
			double codigoPresuOdoConvenio= PresupuestoOdontologico.guardarPresupuestoOdoConvenio(dtoConvenio, con);
			
			if(codigoPresuOdoConvenio<=0)
			{
				return false;
			}
			
			if(utilizaProgramas)
			{
				for(DtoPresupuestoDetalleServiciosProgramaDao detalleServPrograma: dtoConvenio.getListaDetalleServiciosPrograma())
				{

					detalleServPrograma.setPresupuestoOdoConvenio(new BigDecimal(codigoPresuOdoConvenio));
					detalleServPrograma.setFHU(new DtoInfoFechaUsuario(loginUsuario));
					
					PresupuestoOdontologico.insertPresupuestoDetalleServiciosProgramaDao(detalleServPrograma, con);
					
					if(detalleServPrograma.getCodigoPk().doubleValue()<=0)
					{
						return false;
					}
				}
			}
	   	}
		
		return true;
	}
	
	
	/**
	 * Método encargado de guardar el Otro Si para las exclusiones registradas
	 * asociadas al presupuesto cargado en el plan de tratamiento.
	 * 
	 * @param loginUsuario
	 * @param codigoPresupuesto
	 * @param codigoCentroAtencion
	 * @return
	 */
	private OtrosSi guardarOtroSi (String loginUsuario, BigDecimal codigoPresupuesto, int codigoCentroAtencion)
	{
		IPresupuestoOdontologicoMundo presupuestoOdontologicoMundo = PresupuestoFabricaMundo.crearPresupuestoOdontologicoMundo();
		
		DtoOtroSi dtoOtroSi;
		dtoOtroSi = new DtoOtroSi();

		if(codigoPresupuesto != null)
		{
			dtoOtroSi.setPresupuesto(codigoPresupuesto.longValue());
			dtoOtroSi.setUsuario(loginUsuario);
			dtoOtroSi.setCentroAtencion(codigoCentroAtencion);
	
			return presupuestoOdontologicoMundo.guardarOtroSi(dtoOtroSi);
		}
		
		return null;
	}
	
	/**
	 * Método usado para devolver un objeto {@link ResultadoBoolean}
	 * con el mensaje de error que se debe presentar.
	 * 
	 * @return
	 */
	private ResultadoBoolean retornarError (){
		
		ResultadoBoolean resultado = new ResultadoBoolean();
		resultado.setResultado(false);
		resultado.setDescripcion("errors.problemasBd");
		
		return resultado;
	}
	
	
	
	/**
	 * Método que permite guardar la información del proceso de contratación y precontratación
	 * de las inclusiones realizadas al plan de tratamiento y al presupuesto del paciente
	 * 
	 * @param errores
	 * @param registrosContratarInclusion
	 * @param estado
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @param codigoPresupuesto
	 * @param codigoCentroAtencion
	 * @param con 
	 * @return
	 */
	private long precontratarContratarInclusiones (ActionErrors errores, ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,
												   String estado, int codigoInstitucion, String loginUsuario, BigDecimal codigoPresupuesto, 
													int codigoCentroAtencion, Connection con) throws IPSException{
		

		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoInclusionProgServOdo,codigoInstitucion);
		
		long consecutivoInclusion = Long.parseLong(consecutivo);
		
		IncluPresuEncabezado incluPresuEncabezado = crearIncluPresuEncabezado(estado, loginUsuario, codigoPresupuesto, codigoCentroAtencion, consecutivoInclusion);

		obtenerInformacionParaPrecontratarInclusion(incluPresuEncabezado, registrosContratarInclusion, estado, codigoInstitucion);

		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		
		long codigoIncluEncabezado =  incluPresuEncabezadoMundo.registrarInclusionPresupuestoEncabezado(incluPresuEncabezado);
		
		if(codigoIncluEncabezado > 0){
			
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInclusionProgServOdo,codigoInstitucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			
		}else{
			
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInclusionProgServOdo,codigoInstitucion, consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		}
		
		return codigoIncluEncabezado;
	}

	/**
	 * Método que se encarga de obtener la información
	 * necesaria para registrar los programas y convenios que se van a asociar
	 * con el proceso de contratación de la Inclusión
	 * 
	 * @param incluPresuEncabezado
	 * @param registrosContratarInclusion
	 * @param estado
	 * @param codigoInstitucion 
	 */
	@SuppressWarnings("unchecked")
	private void obtenerInformacionParaPrecontratarInclusion(IncluPresuEncabezado incluPresuEncabezado, ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,
															String estado, int codigoInstitucion) throws IPSException{
		
		
		HashMap<Integer, IncluPresuConvenio> registroConvenios = new HashMap<Integer, IncluPresuConvenio>();
				
		boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
		
		char seleccionadoBono;
		char seleccionadoPromocion;
		char convenioContratado;
		int detallePromocion;//Codigo PK de la promoción
		
		HashSet<IncluPresuConvenio> setIncluPresuConvenios = new HashSet<IncluPresuConvenio>();
		HashSet<InclusionesPresupuesto> setInclusionesPresupuesto = new HashSet<InclusionesPresupuesto>();

		for (DtoRegistroContratarInclusion registroInclusion : registrosContratarInclusion) {
		
			DtoPresuOdoProgServ programaServicio = registroInclusion.getProgramaServicio();

			InclusionesPresupuesto inclusionPresupuesto = new InclusionesPresupuesto();
			
			inclusionPresupuesto.setIncluPresuEncabezado(incluPresuEncabezado);
			
			ProgramasHallazgoPieza programaHallazgoPieza = new ProgramasHallazgoPieza();
			programaHallazgoPieza.setCodigoPk(programaServicio.getCodigoProgramaHallazgoPieza().longValue());
		
			inclusionPresupuesto.setProgramasHallazgoPieza(programaHallazgoPieza);
			
			Programas programa = new Programas();
			BigDecimal codigoPrograma = new BigDecimal(programaServicio.getPrograma().getCodigo());
			programa.setCodigo(codigoPrograma.longValue());
			
			for (DtoPresupuestoOdoConvenio convenio : registroInclusion.getListPresupuestoOdoConvenio()) {
				
				seleccionadoBono = convenio.getSeleccionadoBono() ? ConstantesBD.acronimoSiChar : ConstantesBD.acronimoNoChar;
				seleccionadoPromocion = convenio.getSeleccionadoPromocion() ? ConstantesBD.acronimoSiChar : ConstantesBD.acronimoNoChar;
				detallePromocion=((Double)convenio.getDetallePromocion()).intValue();
				
				convenioContratado = convenio.getContratado().equals(ConstantesBD.acronimoSi) ? ConstantesBD.acronimoSiChar : ConstantesBD.acronimoNoChar;
				
				IncluPresuConvenio incluPresuConvenio;
				
				if(registroConvenios.containsKey(convenio.getContrato().getCodigo())){
					
					incluPresuConvenio = registroConvenios.get(convenio.getContrato().getCodigo());
				
				}else{
					
					Contratos contrato = new Contratos();
					contrato.setCodigo(convenio.getContrato().getCodigo());
				
					incluPresuConvenio = new IncluPresuConvenio();
					
					incluPresuConvenio.setContratos(contrato);
					incluPresuConvenio.setIncluPresuEncabezado(incluPresuEncabezado);
					
					registroConvenios.put(convenio.getContrato().getCodigo(), incluPresuConvenio);
				}
				
				if(convenioContratado == ConstantesBD.acronimoSiChar)
			   	{
					inclusionPresupuesto.setValor(convenio.getValorTotalConvenioXProgServ(programaServicio.getCantidad()));
			   	}

				HashSet<IncluServicioConvenio> setIncluServiciosConvenio = new HashSet<IncluServicioConvenio>();
				
				if(utilizaProgramas){
				
					IncluProgramaConvenio incluProgramaConvenio = new IncluProgramaConvenio();
					
					incluProgramaConvenio.setCantidad(programaServicio.getCantidad());
					incluProgramaConvenio.setIncluPresuConvenio(incluPresuConvenio);
					incluProgramaConvenio.setProgramas(programa);
			
					incluProgramaConvenio.setSeleccionadoBono(seleccionadoBono);
					incluProgramaConvenio.setSeleccionadoPromocion(seleccionadoPromocion);
					incluProgramaConvenio.setContratado(convenioContratado);
					incluProgramaConvenio.setProgramasHallazgoPieza(programaHallazgoPieza);
					
					if(detallePromocion>0 && convenio.getSeleccionadoPromocion())
					{
						IncluPresProgramaPromo promocion=new IncluPresProgramaPromo();
						DetPromocionesOdo detPromocionesOdo=new DetPromocionesOdo();
						detPromocionesOdo.setCodigoPk(detallePromocion);
						promocion.setDetPromocionesOdo(detPromocionesOdo);
						promocion.setIncluProgramaConvenio(incluProgramaConvenio);
						incluProgramaConvenio.getIncluPresProgramaPromos().add(promocion);
					}
					
					incluPresuConvenio.getIncluProgramaConvenios().add(incluProgramaConvenio);
			   		
				   	for(DtoPresupuestoDetalleServiciosProgramaDao detalleServPrograma: convenio.getListaDetalleServiciosPrograma())
					{
				   		IncluServicioConvenio incluServicioConvenio = crearIncluServicioConvenio(incluPresuConvenio, detalleServPrograma, seleccionadoBono, seleccionadoPromocion, convenioContratado);
						
						setIncluServiciosConvenio.add(incluServicioConvenio);
					}
				   	
				   	incluProgramaConvenio.setIncluServicioConvenios(setIncluServiciosConvenio);

				}else{
					
					for(DtoPresupuestoDetalleServiciosProgramaDao detalleServPrograma: convenio.getListaDetalleServiciosPrograma())
					{
				   		IncluServicioConvenio incluServicioConvenio = crearIncluServicioConvenio(incluPresuConvenio, detalleServPrograma, seleccionadoBono, seleccionadoPromocion, convenioContratado);
					
				   		setIncluServiciosConvenio.add(incluServicioConvenio);
					}
					
					incluPresuConvenio.setIncluServicioConvenios(setIncluServiciosConvenio);
				}
			}
			
			setInclusionesPresupuesto.add(inclusionPresupuesto);
		}
		
		setIncluPresuConvenios = obtenerIncluPresuConvenio(registroConvenios.values());
		
		incluPresuEncabezado.setIncluPresuConvenios(setIncluPresuConvenios);
		
		/*
		 * Cuando el estado es Contratado, significa que se van a contratar directamente 
		 * las Inclusiones y no es necesario hacer estos registros, ya que se
		 * van a realizar más adelante.
		 */
		if(!estado.equals(ConstantesIntegridadDominio.acronimoContratado)){
			
			incluPresuEncabezado.setInclusionesPresupuestos(setInclusionesPresupuesto);
		}
	}

	
	/**
	 * Método que crea la entidad {@link IncluPresuEncabezado}
	 * Y realiza el registro del otro si solo en el caso que se este 
	 * realizando la contratación definitiva
	 * 
	 * @param estado
	 * @param loginUsuario
	 * @param codigoPresupuesto
	 * @param codigoCentroAtencion
	 * @param consecutivoInclusion
	 * @return
	 */
	private IncluPresuEncabezado crearIncluPresuEncabezado(String estado,
			String loginUsuario, BigDecimal codigoPresupuesto,
			int codigoCentroAtencion, long consecutivoInclusion) {
		
		com.servinte.axioma.orm.PresupuestoOdontologico presupuestoOdontologico = new com.servinte.axioma.orm.PresupuestoOdontologico();
		presupuestoOdontologico.setCodigoPk(codigoPresupuesto.longValue());
		
		Usuarios usuario = new Usuarios();
		usuario.setLogin(loginUsuario);
		
		CentroAtencion centroAtencion = new CentroAtencion();
		centroAtencion.setConsecutivo(codigoCentroAtencion);
		
		
		IncluPresuEncabezado incluPresuEncabezado = new IncluPresuEncabezado();

		incluPresuEncabezado.setConsecutivo(consecutivoInclusion);
		incluPresuEncabezado.setEstado(estado);
		incluPresuEncabezado.setPresupuestoOdontologico(presupuestoOdontologico);
		incluPresuEncabezado.setUsuarios(usuario);
		incluPresuEncabezado.setFecha(UtilidadFecha.getFechaActualTipoBD());
		incluPresuEncabezado.setHora(UtilidadFecha.getHoraActual());
		incluPresuEncabezado.setCentroAtencion(centroAtencion);
		
		if(estado.equals(ConstantesIntegridadDominio.acronimoContratado)){
			
			incluPresuEncabezado.setOtrosSi(guardarOtroSi(loginUsuario, codigoPresupuesto, codigoCentroAtencion));
		}
		
		return incluPresuEncabezado;
	}

	
	/**
	 * Permite obtener los objetos {@link IncluPresuConvenio}
	 * registrados
	 * 
	 * @param listadoConvenios
	 * @return 
	 */
	private HashSet<IncluPresuConvenio> obtenerIncluPresuConvenio(Collection<IncluPresuConvenio> listadoConvenios) {
		
		HashSet<IncluPresuConvenio> setIncluPresuConvenios = new HashSet<IncluPresuConvenio>();

		for (Iterator convenios = listadoConvenios.iterator(); convenios.hasNext();) {
			
			IncluPresuConvenio incluPresuConvenio = (IncluPresuConvenio) convenios.next();
			
			setIncluPresuConvenios.add(incluPresuConvenio);
		}
		
		return setIncluPresuConvenios;
		
	}

	/**
	 * Método que inicializa los atributos de un objeto {@link IncluServicioConvenio}
	 * a partir de un {@link DtoPresupuestoDetalleServiciosProgramaDao}
	 * 
	 * @param incluPresuConvenio
	 * @param detalleServPrograma
	 * @param seleccionadoBono
	 * @param seleccionadoPromocion
	 * @param convenioContratado 
	 * @return
	 */
	private IncluServicioConvenio crearIncluServicioConvenio (IncluPresuConvenio incluPresuConvenio, DtoPresupuestoDetalleServiciosProgramaDao detalleServPrograma,
															 char seleccionadoBono, char seleccionadoPromocion, char convenioContratado) {
		
		Servicios servicio = new Servicios();
		servicio.setCodigo(detalleServPrograma.getServicio());
		
		EsquemasTarifarios esquemaTarifario = new EsquemasTarifarios();
		esquemaTarifario.setCodigo(detalleServPrograma.getEsquemaTarifario());
		
		IncluServicioConvenio incluServicioConvenio = new IncluServicioConvenio();
		
		incluServicioConvenio.setEsquemasTarifarios(esquemaTarifario);
		incluServicioConvenio.setServicios(servicio);
		incluServicioConvenio.setValorUnitario(detalleServPrograma.getValorUnitarioServicio());
		incluServicioConvenio.setCantidad(1);
		incluServicioConvenio.setSeleccionadoBono(seleccionadoBono);
		incluServicioConvenio.setSeleccionadoPromocion(seleccionadoPromocion);
		incluServicioConvenio.setContratado(convenioContratado);
		incluServicioConvenio.setPorcentajeDctoPromocion(new BigDecimal(detalleServPrograma.getPorcentajeDctoPromocionServicio()));
		incluServicioConvenio.setValorDescuentoPromocion(detalleServPrograma.getValorDctoPromocionServicio());
		incluServicioConvenio.setPorcentajeDctoBono(new BigDecimal(detalleServPrograma.getPorcentajeDctoBonoServicio()));
		incluServicioConvenio.setValorDescuentoBono(detalleServPrograma.getValorDctoBonoServicio());
		incluServicioConvenio.setDctoComercialUnitario(detalleServPrograma.getDctoComercialUnitario());
		incluServicioConvenio.setValorHonorarioPromocion(detalleServPrograma.getValorHonorarioDctoPromocionServicio());
		incluServicioConvenio.setPorcHonorarioPromocion(new BigDecimal(detalleServPrograma.getPorcentajeHonorarioDctoPromocionServicio()));
		incluServicioConvenio.setIncluPresuConvenio(incluPresuConvenio);
		
		return incluServicioConvenio;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#cargarRegistrosInclusion(long)
	 */
	@Override
	public ArrayList<DtoEncabezadoInclusion> cargarRegistrosInclusion(	long codigoPresupuesto) {
		
		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		
		ArrayList<IncluPresuEncabezado> listaRegistrosInclusion = (ArrayList<IncluPresuEncabezado>) incluPresuEncabezadoMundo.cargarRegistrosInclusion(codigoPresupuesto);
		
		ArrayList<DtoEncabezadoInclusion> listaEncabezadosInclusion = new ArrayList<DtoEncabezadoInclusion>();
		
		InfoDefinirSolucitudDsctOdon solicitudDescuento;
		
		ArrayList<InfoDefinirSolucitudDsctOdon> solicitudesDescuento;

		for (IncluPresuEncabezado incluPresuEncabezado : listaRegistrosInclusion) {
			
			DtoEncabezadoInclusion encabezadoInclusion = new DtoEncabezadoInclusion();
			
			encabezadoInclusion.setIncluPresuEncabezado(incluPresuEncabezado);

			solicitudDescuento = new InfoDefinirSolucitudDsctOdon();
			
			solicitudDescuento.setCodigoPkEncabezadoInclusion(incluPresuEncabezado.getCodigoPk());
			solicitudDescuento.setCodigoPaciente(incluPresuEncabezado.getPresupuestoOdontologico().getPacientes().getCodigoPaciente());
			solicitudDescuento.setOrdenamiento("desc");
			
			solicitudesDescuento = AutorizacionDescuentosOdon.cargarDefinicionSolicitudesDescuento(solicitudDescuento);
			
			encabezadoInclusion.setSolicitudDescuento(obtenerSolicitudDescuento (solicitudesDescuento));
			
			encabezadoInclusion.cargarTotalesInclusion();
			
			listaEncabezadosInclusion.add(encabezadoInclusion);
		}
		
		return listaEncabezadosInclusion;

	}

	
	/**
	 * Método que devuelve la solicitud de descuento odontológico más reciente asociada al registro
	 * del encabezado de inclusión
	 * 
	 * @param solicitudesDescuento
	 * @return Solicitud de Descuento Odontológica
	 */
	private InfoDefinirSolucitudDsctOdon obtenerSolicitudDescuento(ArrayList<InfoDefinirSolucitudDsctOdon> solicitudesDescuento) {
		
		for (InfoDefinirSolucitudDsctOdon solicitudDescuento : solicitudesDescuento) {
			
			if(solicitudDescuento.isInclusion()){
				
				return solicitudDescuento;
			}
		}
		
		return new InfoDefinirSolucitudDsctOdon();
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#cargarDetalleRegistroInclusion(long, java.util.ArrayList, java.util.ArrayList, com.princetonsa.mundo.UsuarioBasico)
	 */
	@Override
	public ArrayList<DtoRegistroContratarInclusion> cargarDetalleRegistroInclusion (long codigoIncluPresuEncabezado,ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas,
																					ArrayList<InfoInclusionExclusionBoca> inclusionesBoca, UsuarioBasico usuario) {
		
		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();

		IncluPresuEncabezado incluPresuEncabezado = incluPresuEncabezadoMundo.cargarDetalleRegistroInclusion(codigoIncluPresuEncabezado);
		
		//listaSumatoriaConvenios = obtenerListadoSumatoriaConvenios (incluPresuEncabezado.getIncluPresuConvenios().iterator());
		
		ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion = obtenerProgramasDesdeIncluPresuEncabezado(incluPresuEncabezado, inclusionesPiezas, inclusionesBoca, usuario);
		
		if(incluPresuEncabezado!=null){
			
			for (DtoRegistroContratarInclusion registroInclusion : registrosContratarInclusion) {
					
				registroInclusion.setListPresupuestoOdoConvenio(obtenerConveniosPorProgramaDesdeIncluPresuConvenios(incluPresuEncabezado, registroInclusion.getProgramaServicio()));
			}
		}
		
		if(registrosContratarInclusion.size() > 0){
			
			eliminarConveniosSinProgramasContratados(registrosContratarInclusion);
		}
		
		return registrosContratarInclusion;
	}

	
	/**
	 * Método que se encarga de eliminar los convenios que no están contratados de los listados 
	 * de {@link DtoPresupuestoOdoConvenio}, ya que solo deben mostrarse los convenios con los cuales 
	 * se contrató al menos un programa / servicio
	 * 
	 * @param registrosContratarInclusion
	 */
	private void eliminarConveniosSinProgramasContratados(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion) {

		Hashtable<Integer, String> conveniosContratados = new Hashtable<Integer, String>();

		/*
		 * Determinamos cuales son los convenios que tienen al menos un programa / servicio contratado
		 */
		for (DtoRegistroContratarInclusion registroContratarInclusion : registrosContratarInclusion) {

			for (DtoPresupuestoOdoConvenio convenio : registroContratarInclusion.getListPresupuestoOdoConvenio()) {
				
				if(convenio.getContratado().equals(ConstantesBD.acronimoSi)){
					
					if(!conveniosContratados.containsKey(convenio.getConvenio().getCodigo())){
						
						conveniosContratados.put(convenio.getConvenio().getCodigo(), convenio.getConvenio().getNombre());
					}
				}
			}
		}
		
		/*
		 * Eliminamos los convenios que no estan en el Hashtable de convenios contratados
		 */
		for (DtoRegistroContratarInclusion registroContratarInclusion : registrosContratarInclusion) {

			for (int i = 0; i < registroContratarInclusion.getListPresupuestoOdoConvenio().size(); i++) {
				
				DtoPresupuestoOdoConvenio convenio = registroContratarInclusion.getListPresupuestoOdoConvenio().get(i);
				
				if(!conveniosContratados.containsKey(convenio.getConvenio().getCodigo())){
					
					registroContratarInclusion.getListPresupuestoOdoConvenio().remove(i);
					i--;
				}
			}
			
			registroContratarInclusion.getListPresupuestoOdoConvenio().trimToSize();
		}
	}

	/**
	 * 
	 * Método que permite obtener los programas asociados al registro de contratación de una inclusión.
	 * 
	 * @param incluPresuEncabezado
	 * @param inclusionesPiezas
	 * @param inclusionesBoca
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<DtoRegistroContratarInclusion> obtenerProgramasDesdeIncluPresuEncabezado (IncluPresuEncabezado incluPresuEncabezado, ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas, ArrayList<InfoInclusionExclusionBoca> inclusionesBoca, UsuarioBasico usuario){
		
		ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion = new ArrayList<DtoRegistroContratarInclusion>();
		
		String estado = incluPresuEncabezado.getEstado();
		
		for (Iterator<InclusionesPresupuesto> inclusiones = incluPresuEncabezado.getInclusionesPresupuestos().iterator(); inclusiones.hasNext();) 
		{
			InclusionesPresupuesto inclusionPresupuesto = (InclusionesPresupuesto) inclusiones.next();
			
			DtoPresuOdoProgServ programaServicio;
			
			if(estado.equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
				
				programaServicio = obtenerProgramaPorProgramaHallazgoPieza (inclusionPresupuesto.getProgramasHallazgoPieza().getCodigoPk(), inclusionesPiezas, inclusionesBoca, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
				
			}else{
				
				programaServicio = obtenerProgramaDesdeIncluPresuConvenio(incluPresuEncabezado.getIncluPresuConvenios().iterator(), inclusionPresupuesto.getProgramasHallazgoPieza().getCodigoPk(), usuario.getLoginUsuario());
			}
			
			DtoRegistroContratarInclusion registroContratarInclusion = new DtoRegistroContratarInclusion();
			registroContratarInclusion.setProgramaServicio(programaServicio);

			registrosContratarInclusion.add(registroContratarInclusion);
		}
		
		return registrosContratarInclusion;
		
	}
	
	/**
	 * Método que permite obtener los programas asociados al registro de contratación de una inclusión.
	 * Aplica solo en el caso que el registro tenga estado Contratado o Anulado
	 *
	 * @param incluPresuConvenios
	 * @param codigoProgramaHallazgoPieza
	 * @param loginUsuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DtoPresuOdoProgServ obtenerProgramaDesdeIncluPresuConvenio (Iterator incluPresuConvenios, long codigoProgramaHallazgoPieza, String loginUsuario){
		
		DtoPresuOdoProgServ programa = new DtoPresuOdoProgServ();
		String contratado;
		
		for (Iterator<IncluPresuConvenio> convenios = incluPresuConvenios; convenios.hasNext();) {
			
			IncluPresuConvenio incluPresuConvenio = (IncluPresuConvenio) convenios.next();
			
			for (Iterator<IncluProgramaConvenio> programasConvenio = incluPresuConvenio.getIncluProgramaConvenios().iterator(); programasConvenio.hasNext();) {
				
				IncluProgramaConvenio incluProgramaConvenio = (IncluProgramaConvenio) programasConvenio.next();
				
				contratado = incluProgramaConvenio.getContratado() == ConstantesBD.acronimoSiChar ? ConstantesBD.acronimoSi :  ConstantesBD.acronimoNo;
				
				if(incluProgramaConvenio.getProgramasHallazgoPieza().getCodigoPk() == codigoProgramaHallazgoPieza){
					
					programa.setCantidad(incluProgramaConvenio.getCantidad());
					programa.setCodigoProgramaHallazgoPieza(new BigDecimal(codigoProgramaHallazgoPieza));
					programa.setContratado(contratado);
					programa.setEspecialidad(incluProgramaConvenio.getProgramas().getEspecialidades().getNombre());
					
					BigDecimal codigoPrograma = new BigDecimal(incluProgramaConvenio.getProgramas().getCodigo());
					
					InfoDatosDouble infoPrograma = new InfoDatosDouble(codigoPrograma.doubleValue(), 
														incluProgramaConvenio.getProgramas().getNombre(),
														incluProgramaConvenio.getProgramas().getCodigoPrograma());
					
					programa.setPrograma(infoPrograma);
					
					programa.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
					programa.setTipoModificacion(EnumTipoModificacion.NUEVO);
					
					BigDecimal valorUnitario = new BigDecimal(0);
					
					for (Iterator<IncluServicioConvenio> serviciosConvenio = incluProgramaConvenio.getIncluServicioConvenios().iterator(); serviciosConvenio.hasNext();) {
						
						IncluServicioConvenio incluServicioConvenio = (IncluServicioConvenio) serviciosConvenio.next();

						valorUnitario = valorUnitario.add(incluServicioConvenio.getValorUnitario());
					}
					
					programa.setValorTarifa(valorUnitario);
					
					return programa;
				}
			}
		}
		
		return programa;
	}
	
	/**
	 * Método que permite obtener los convenios para cada programa registrados en el 
	 * proceso de contratación de la inclusión

	 * @param incluPresuEncabezado
	 * @param programaServicio
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<DtoPresupuestoOdoConvenio> obtenerConveniosPorProgramaDesdeIncluPresuConvenios(IncluPresuEncabezado incluPresuEncabezado,	DtoPresuOdoProgServ programaServicio) {
		
		boolean seleccionadoBono;
		boolean seleccionadoPromocion;
		InfoDatosInt infoContrato;
		InfoDatosInt infoConvenio;
		
		int codigoDetallePromocion;
		
		String ajusteServicio = "";
		
		ArrayList<DtoPresupuestoOdoConvenio> listadoConvenios = new ArrayList<DtoPresupuestoOdoConvenio>();
		
		for (Iterator<IncluPresuConvenio> convenios = incluPresuEncabezado.getIncluPresuConvenios().iterator(); convenios.hasNext();) {

			IncluPresuConvenio incluPresuConvenio = (IncluPresuConvenio) convenios.next();
			
			infoContrato = new InfoDatosInt(incluPresuConvenio.getContratos().getCodigo());
			infoConvenio = new InfoDatosInt(incluPresuConvenio.getContratos().getConvenios().getCodigo(), 
					                        incluPresuConvenio.getContratos().getConvenios().getNombre());
			
			ajusteServicio = !incluPresuConvenio.getContratos().getConvenios().getAjusteServicios().equals("") ? incluPresuConvenio.getContratos().getConvenios().getAjusteServicios() : ConstantesIntegridadDominio.acronimoSinAjuste;
			
			for (Iterator<IncluProgramaConvenio> programasConvenio = incluPresuConvenio.getIncluProgramaConvenios().iterator(); programasConvenio.hasNext();) {
				
				IncluProgramaConvenio incluProgramaConvenio = (IncluProgramaConvenio) programasConvenio.next();
				
				String contratado = incluProgramaConvenio.getContratado() == ConstantesBD.acronimoSiChar ? ConstantesBD.acronimoSi :  ConstantesBD.acronimoNo;

				codigoDetallePromocion = ConstantesBD.codigoNuncaValido;

				programaServicio.setCantidad(incluProgramaConvenio.getCantidad());
				
				seleccionadoBono = incluProgramaConvenio.getSeleccionadoBono() == ConstantesBD.acronimoSiChar ? true : false;
				seleccionadoPromocion = incluProgramaConvenio.getSeleccionadoPromocion() == ConstantesBD.acronimoSiChar ? true : false;;
				
				if(seleccionadoPromocion){
					
					codigoDetallePromocion = obtenerDetallePromocion(incluProgramaConvenio);
				}
				
				if(incluProgramaConvenio.getProgramasHallazgoPieza().getCodigoPk() == programaServicio.getCodigoProgramaHallazgoPieza().longValue()){

					DtoPresupuestoOdoConvenio convenio = new DtoPresupuestoOdoConvenio();
					
					convenio.setContrato(infoContrato);
					convenio.setConvenio(infoConvenio);
					
					convenio.setSeleccionadoBono(seleccionadoBono);
					convenio.setSeleccionadoPromocion(seleccionadoPromocion);
					
					convenio.setAjusteServicio(ajusteServicio);
					
					convenio.setContratado(contratado);
					
					if(codigoDetallePromocion > ConstantesBD.codigoNuncaValido){
						
						convenio.setDetallePromocion(codigoDetallePromocion);
					}
					
					ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>  detalleServicios = new  ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
					
					for (Iterator<IncluServicioConvenio> serviciosConvenio = incluProgramaConvenio.getIncluServicioConvenios().iterator(); serviciosConvenio.hasNext();) {
						
						IncluServicioConvenio incluServicioConvenio = (IncluServicioConvenio) serviciosConvenio.next();

						detalleServicios.add(crearDetalleServicio(incluServicioConvenio, programaServicio.getPrograma().getCodigo()));
						
						/*
						 * Se verifica si no esta parametrizado el método de ajuste a nivel de convenio, 
						 * si es así, se toma el método de ajuste del esquema tarifario.
						 */
						if(convenio.getAjusteServicio().equals(ConstantesIntegridadDominio.acronimoSinAjuste)){
							
							char acronimoMetodoAjuste = incluServicioConvenio.getEsquemasTarifarios().getMetodosAjuste().getAcronimo();
							
							convenio.setAjusteServicio(acronimoMetodoAjuste+"");
						}
					}
					
					convenio.setListaDetalleServiciosPrograma(detalleServicios);
					
					cargarTotalesPorServicioAConvenio (convenio);

					listadoConvenios.add(convenio);
				}
			}
		}
		
		return listadoConvenios;
	}
	
	
	/**
	 * @param incluProgramaConvenio
	 * @return
	 */
	private int obtenerDetallePromocion(IncluProgramaConvenio incluProgramaConvenio) {
		
		for (Iterator iterator = incluProgramaConvenio.getIncluPresProgramaPromos().iterator(); iterator.hasNext();) {
			
			IncluPresProgramaPromo incluPresProgramaPromo = (IncluPresProgramaPromo) iterator.next();
			
			if(incluPresProgramaPromo!=null){
				
				return incluPresProgramaPromo.getDetPromocionesOdo().getCodigoPk();
			}
		}

		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Método que Totaliza la información por servicio para modificar los totales que se manejan
	 * a nivel de convenio
	 * 
	 * @param convenio
	 */
	private void cargarTotalesPorServicioAConvenio(DtoPresupuestoOdoConvenio convenio) {
		
		BigDecimal descuentoComercialUnitario = new BigDecimal(0);
		double porcentajeDctoBono = 0;
		double porcentajeHonorarioPromocion = 0;
		double porcentajePromocion= 0;
		BigDecimal valorDescuentoBono = new BigDecimal(0);
		BigDecimal valorDescuentoPromocion = new BigDecimal(0);
		BigDecimal valorHonorarioPromocion = new BigDecimal(0);
		BigDecimal valorUnitario = new BigDecimal(0);
		
		
		boolean porcentajes = true;
		
		for (DtoPresupuestoDetalleServiciosProgramaDao servicio : convenio.getListaDetalleServiciosPrograma()) {
			
			if(porcentajes){
				
				porcentajeDctoBono = servicio.getPorcentajeDctoBonoServicio();
				porcentajeHonorarioPromocion = servicio.getPorcentajeHonorarioDctoPromocionServicio();
				porcentajePromocion = servicio.getPorcentajeDctoPromocionServicio();
				
				porcentajes = false;
			}
		
			
			descuentoComercialUnitario = descuentoComercialUnitario.add(servicio.getDctoComercialUnitario());
			valorDescuentoBono = valorDescuentoBono.add(servicio.getValorDctoBonoServicio());
			valorDescuentoPromocion = valorDescuentoPromocion.add(servicio.getValorDctoPromocionServicio());
			valorHonorarioPromocion = valorHonorarioPromocion.add(servicio.getValorHonorarioDctoPromocionServicio());
			valorUnitario = valorUnitario.add(servicio.getValorUnitarioServicio());
		}

		convenio.setDescuentoComercialUnitario(descuentoComercialUnitario);
		convenio.setPorcentajeDctoBono(porcentajeDctoBono);
		convenio.setPorcentajeHonorarioPromocion(porcentajeHonorarioPromocion);
		convenio.setValorDescuentoBono(valorDescuentoBono);

		if(porcentajePromocion > 0 && convenio.getSeleccionadoPromocion()){
			
			convenio.setPorcentajePromocion(porcentajePromocion);
			convenio.setValorDescuentoPromocion(valorDescuentoPromocion);
			convenio.setSeleccionadoPorcentajePromocion(true);
		
		}else{
			
			convenio.setPorcentajePromocion(0);
			convenio.setValorDescuentoPromocion(valorDescuentoPromocion);
		}
		
		
		convenio.setValorHonorarioPromocion(valorHonorarioPromocion);
		convenio.setValorUnitario(valorUnitario);
	}

	/**
	 * Método que permite obtener el Programa con toda la información del 
	 * listado de Inlcusiones teniendo el codigo del Programa Hallazgo Pieza
	 * 
	 * @param codigoProgramaHallazgoPieza
	 * @param inclusionesPiezas
	 * @param inclusionesBoca
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	private DtoPresuOdoProgServ obtenerProgramaPorProgramaHallazgoPieza(long codigoProgramaHallazgoPieza, ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas, 
														ArrayList<InfoInclusionExclusionBoca> inclusionesBoca, String loginUsuario, int codigoInstitucion) {

		boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
		
		
		for (InfoInclusionExclusionPiezaSuperficie inclusionPieza : inclusionesPiezas) {
			
			if(inclusionPieza.getCodigoPkProgramaHallazgoPieza().longValue() == codigoProgramaHallazgoPieza){
				
				return obtenerProgramaServicioDesdeInclusionExclusionPiezaDental(inclusionPieza, loginUsuario, utilizaProgramas);
			}
		}
		
		for (InfoInclusionExclusionBoca inclusionBoca : inclusionesBoca) {
			
			if(inclusionBoca.getCodigoPkProgramaHallazgoPieza().longValue() == codigoProgramaHallazgoPieza){
				
				return obtenerProgramaServicioDesdeInclusionExclusionBoca(inclusionBoca, loginUsuario, utilizaProgramas);
			}
		}

		return null;
	}

	
	
	/**
	 * Método que inicializa los atributos de un objeto {@link IncluServicioConvenio}
	 * a partir de un {@link DtoPresupuestoDetalleServiciosProgramaDao}
	 * 
	 * @param servicio
	 * @param codigoPrograma 
	 * @return
	 */
	private DtoPresupuestoDetalleServiciosProgramaDao crearDetalleServicio (IncluServicioConvenio servicio, Double codigoPrograma) {
		
		
		DtoPresupuestoDetalleServiciosProgramaDao detalleServicio = new DtoPresupuestoDetalleServiciosProgramaDao();
		
		detalleServicio.setEsquemaTarifario(servicio.getEsquemasTarifarios().getCodigo());
		detalleServicio.setServicio(servicio.getServicios().getCodigo());
		detalleServicio.setValorUnitarioServicio(servicio.getValorUnitario());
		detalleServicio.setPrograma(codigoPrograma);
		detalleServicio.setPorcentajeDctoPromocionServicio(servicio.getPorcentajeDctoPromocion().doubleValue());
		detalleServicio.setValorDctoPromocionServicio(servicio.getValorDescuentoPromocion());
		detalleServicio.setPorcentajeDctoBonoServicio(servicio.getPorcentajeDctoBono().doubleValue());
		detalleServicio.setValorDctoBonoServicio(servicio.getValorDescuentoBono());
		detalleServicio.setDctoComercialUnitario(servicio.getDctoComercialUnitario());
		detalleServicio.setValorHonorarioDctoPromocionServicio(servicio.getValorHonorarioPromocion());
		detalleServicio.setPorcentajeHonorarioDctoPromocionServicio(servicio.getPorcHonorarioPromocion().doubleValue());
		
		return detalleServicio;
	
	}
	
	/**
	 * Método que se encarga de transformar la información de los listados de Exclusiones Pieza Dental y Boca
	 * en un solo listado de {@link DtoRegistroGuardarExclusion}
	 * 
	 * @param exclusionesPiezas
	 * @param exclusionesBoca
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	@Override
	public ArrayList<DtoRegistroGuardarExclusion> obtenerListadoExclusiones (ArrayList<InfoInclusionExclusionPiezaSuperficie> exclusionesPiezas, 
			ArrayList<InfoInclusionExclusionBoca> exclusionesBoca, String loginUsuario, int codigoInstitucion){
		
		ArrayList<DtoRegistroGuardarExclusion> listaRegistroGuardarExclusion = new ArrayList<DtoRegistroGuardarExclusion>();
		
		boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
		
		for (InfoInclusionExclusionPiezaSuperficie incExcPiezaDental : exclusionesPiezas) {
			
			if(incExcPiezaDental!=null){
				
				if (incExcPiezaDental.isActivo()){
					
					DtoRegistroGuardarExclusion registroGuardarExclusion = new DtoRegistroGuardarExclusion();
					
					registroGuardarExclusion.setProgramaServicio(obtenerProgramaServicioDesdeInclusionExclusionPiezaDental(incExcPiezaDental, loginUsuario, utilizaProgramas));

					listaRegistroGuardarExclusion.add(registroGuardarExclusion);
				}
			}
		}
		
		for (InfoInclusionExclusionBoca incExcBoca : exclusionesBoca) {
			
			if(incExcBoca!=null){

				if (incExcBoca.isActivo()){

					DtoRegistroGuardarExclusion registroGuardarExclusion = new DtoRegistroGuardarExclusion();
					
					registroGuardarExclusion.setProgramaServicio(obtenerProgramaServicioDesdeInclusionExclusionBoca(incExcBoca, loginUsuario, utilizaProgramas));
					
					listaRegistroGuardarExclusion.add(registroGuardarExclusion);
				}
			}
		}
		
		return listaRegistroGuardarExclusion;
	}

	
	
	/**
	 * Método que se encarga de transformar la información de los listados de Inclusión Pieza Dental y Boca
	 * en un solo listado de {@link DtoPresuOdoProgServ}
	 * 
	 * @param inclusionesPiezas
	 * @param inclusionesBoca
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	@Override
	public ArrayList<DtoPresuOdoProgServ> obtenerListadoInclusiones (ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas, 
			ArrayList<InfoInclusionExclusionBoca> inclusionesBoca, String loginUsuario, int codigoInstitucion){
		
		boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
		
		ArrayList<DtoPresuOdoProgServ> listaProgramasServicios = new ArrayList<DtoPresuOdoProgServ>();
		
		for (InfoInclusionExclusionPiezaSuperficie inclusionPiezaDental : inclusionesPiezas) {
			
			if(inclusionPiezaDental!=null){
				
				if (inclusionPiezaDental.isActivo()){
					
					listaProgramasServicios.add(obtenerProgramaServicioDesdeInclusionExclusionPiezaDental(inclusionPiezaDental, loginUsuario, utilizaProgramas));
				}
			}
		}
		
		for (InfoInclusionExclusionBoca inclusionBoca : inclusionesBoca) {
			
			if(inclusionBoca!=null){
	
				if (inclusionBoca.isActivo()){
	
					listaProgramasServicios.add(obtenerProgramaServicioDesdeInclusionExclusionBoca(inclusionBoca, loginUsuario, utilizaProgramas));
					
				}
			}
		}
		
		return listaProgramasServicios;
	}
	
	/**
	 * Método que se encarga de generar un Objeto de tipo {@link DtoPresuOdoProgServ}
	 * a partir de un objeto {@link InfoInclusionExclusionPiezaSuperficie}
	 * 
	 * @param incExcPiezaDental
	 * @param loginUsuario
	 * @param utilizaProgramas
	 * @return
	 */
	private DtoPresuOdoProgServ obtenerProgramaServicioDesdeInclusionExclusionPiezaDental (InfoInclusionExclusionPiezaSuperficie incExcPiezaDental, String loginUsuario, boolean utilizaProgramas){
		
		DtoPresuOdoProgServ dtoPresuOdoProgServ= new DtoPresuOdoProgServ();
		 
		/////cargar el detalle de las piezas 
		ArrayList<DtoPresupuestoPiezas> arrayPieza= new ArrayList<DtoPresupuestoPiezas>();
		
		for (InfoSuperficiePkDetPlan superficie : incExcPiezaDental.getSuperficies()) {
			
			DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
			dtoPieza.setHallazgo(incExcPiezaDental.getHallazgo());
			dtoPieza.setPieza(new BigDecimal(incExcPiezaDental.getPiezaDental().getCodigo()));
			dtoPieza.setSuperficie(new BigDecimal(superficie.getSuperficie().getCodigo()));
			dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
			dtoPieza.setSeccion(incExcPiezaDental.getSeccion());
			dtoPieza.setNumSuperficies(incExcPiezaDental.getSuperficies().size());
			dtoPieza.setCodigoDetallePlanTratamiento(superficie.getCodigoPkDetallePlanTratamiento());
			
			arrayPieza.add(dtoPieza);
		}
	
		dtoPresuOdoProgServ.setValorTarifa(incExcPiezaDental.getValorTarifa());
		dtoPresuOdoProgServ.setCodigoProgramaHallazgoPieza(incExcPiezaDental.getCodigoPkProgramaHallazgoPieza());
		dtoPresuOdoProgServ.setListPresupuestoPiezas(arrayPieza);
		//dto.setCantidad(1);
		dtoPresuOdoProgServ.getListaCalculoCantidades().add(incExcPiezaDental.getHallazgo().intValue(), new BigDecimal(incExcPiezaDental.getProgramaOservicio().getCodigo()), incExcPiezaDental.getSuperficies().size());
		
		if(utilizaProgramas)
		{
			dtoPresuOdoProgServ.setPrograma(incExcPiezaDental.getProgramaOservicio());
			dtoPresuOdoProgServ.setEspecialidad(Programa.obtenerEspeciliadadPrograma(dtoPresuOdoProgServ.getPrograma().getCodigo()));
		}
		else
		{
			BigDecimal codigoProgramaServicio = new BigDecimal(incExcPiezaDental.getProgramaOservicio().getCodigo());
			
			dtoPresuOdoProgServ.setServicio(new InfoDatosInt(codigoProgramaServicio.intValue(), incExcPiezaDental.getProgramaOservicio().getNombre(), ""));

			dtoPresuOdoProgServ.setEspecialidad(UtilidadesFacturacion.obtenerEspecialidadServicio(dtoPresuOdoProgServ.getServicio().getCodigo()).getNombre());
		}
		
		dtoPresuOdoProgServ.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
		dtoPresuOdoProgServ.setTipoModificacion(EnumTipoModificacion.NUEVO);
		
		return dtoPresuOdoProgServ;
	}
	

	/**
	 * Método que se encarga de generar un Objeto de tipo {@link DtoPresuOdoProgServ}
	 * a partir de un objeto {@link InfoInclusionExclusionBoca}
	 * 
	 * @param incExcBoca
	 * @param loginUsuario
	 * @param utilizaProgramas
	 * @return
	 */
	private DtoPresuOdoProgServ obtenerProgramaServicioDesdeInclusionExclusionBoca (InfoInclusionExclusionBoca incExcBoca, String loginUsuario, boolean utilizaProgramas){
		
		DtoPresuOdoProgServ dtoPresuOdoProgServ = new DtoPresuOdoProgServ();
		 
		/////cargar el detalle de las piezas 
		ArrayList<DtoPresupuestoPiezas> arrayPieza= new ArrayList<DtoPresupuestoPiezas>();
		DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
		dtoPieza.setHallazgo(incExcBoca.getHallazgo());
		dtoPieza.setPieza(new BigDecimal(ConstantesBD.codigoNuncaValido));
		dtoPieza.setSuperficie(new BigDecimal(ConstantesBD.codigoNuncaValido));
		dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
	
		dtoPieza.setCodigoDetallePlanTratamiento(incExcBoca.getCodigoPkDetPlanTratamiento());
		
		dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoBoca);

		dtoPieza.setNumSuperficies(0);
		
		arrayPieza.add(dtoPieza);
		 
		dtoPresuOdoProgServ.setValorTarifa(incExcBoca.getValorTarifa());
		dtoPresuOdoProgServ.setCodigoProgramaHallazgoPieza(incExcBoca.getCodigoPkProgramaHallazgoPieza());
		dtoPresuOdoProgServ.setListPresupuestoPiezas(arrayPieza);
		//dto.setCantidad(1);
		
		BigDecimal codigoProgramaServicio = new BigDecimal(incExcBoca.getProgramaOservicio().getCodigo()); 
		
		dtoPresuOdoProgServ.getListaCalculoCantidades().add(incExcBoca.getHallazgo().intValue(), codigoProgramaServicio, 0); // Numero de superficies
		
		if(utilizaProgramas)
		{	 
			dtoPresuOdoProgServ.setPrograma(incExcBoca.getProgramaOservicio());
			dtoPresuOdoProgServ.setEspecialidad(Programa.obtenerEspeciliadadPrograma(dtoPresuOdoProgServ.getPrograma().getCodigo()));
		}
		else
		{
			dtoPresuOdoProgServ.setServicio(new InfoDatosInt(codigoProgramaServicio.intValue(),incExcBoca.getProgramaOservicio().getNombre(), ""));
			dtoPresuOdoProgServ.setEspecialidad(UtilidadesFacturacion.obtenerEspecialidadServicio(dtoPresuOdoProgServ.getServicio().getCodigo()).getNombre());
		}
		
		dtoPresuOdoProgServ.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
		dtoPresuOdoProgServ.setTipoModificacion(EnumTipoModificacion.NUEVO);
		
		return dtoPresuOdoProgServ;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#generarSolicitudDescuento(com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento)
	 */
	@Override
	public boolean generarSolicitudDescuento(DtoPresupuestoOdontologicoDescuento dtoDcto)
	{
		Connection con = UtilidadPersistencia.getPersistencia().obtenerConexion();
		return PresupuestoOdontologico.guardarPresupuestoDescuento(dtoDcto, con)>0;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#relacionarConInclusiones(com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento, com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado)
	{
		IIncluDctoOdontologicoMundo incluDctoOdontologico=PresupuestoFabricaMundo.crearIncluDctoOdontologico();
		return incluDctoOdontologico.relacionarConInclusiones(dtoDcto, encabezado);
		
	}

	
	/**
	 * Método que registra la solicitud de descuento generada
	 * al momento de realizar la precontratación del proceso de Inclusión
	 * 
	 * @param dtoContratarInclusion
	 * @param codigoIncluPresuEncabezado
	 * @return ResultadoBoolean true, indica que se ha realizado correctamente, false de lo contrario
	 */
	private ResultadoBoolean registrarDescuentoOdontologico(DtoContratarInclusion dtoContratarInclusion, long codigoIncluPresuEncabezado) {
	
		IncluPresuEncabezado encabezado=new IncluPresuEncabezado();
		encabezado.setCodigoPk(codigoIncluPresuEncabezado);
		
		dtoContratarInclusion.getDtoDcto().setConsecutivo(new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, dtoContratarInclusion.getCodigoInstitucion())));
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(dtoContratarInclusion.getConnection(), ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getDtoDcto().getConsecutivo()+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoNo);
		
		double codigoPkDcto=PresupuestoOdontologico.guardarPresupuestoDescuento(dtoContratarInclusion.getDtoDcto(), dtoContratarInclusion.getConnection());
		
		if(codigoPkDcto<0)
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(dtoContratarInclusion.getConnection(), ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getDtoDcto().getConsecutivo()+"", ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			retornarError();
		}
		else
		{
			dtoContratarInclusion.getDtoDcto().setCodigo(new BigDecimal(codigoPkDcto));
			if(!relacionarConInclusiones(dtoContratarInclusion.getDtoDcto(), encabezado))
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(dtoContratarInclusion.getConnection(), ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getDtoDcto().getConsecutivo()+"", ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				retornarError();
			}
			UtilidadTransaccion.getTransaccion().flush();
		}
		
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(dtoContratarInclusion.getConnection(), ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getDtoDcto().getConsecutivo()+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);

		return new ResultadoBoolean(true, "registrado Solicitud Descuento");
	}
	
	/**
	 * Método que modifica los valores del encabezado de la inclusión
	 * 
	 * @param codigoIncluPresuEncabezado
	 * @param codigoCentroAtencion
	 * @param loginUsuario
	 * @param codigoPresupuesto 
	 * @return true en caso de realizar adecuadamente la modificación, false en caso contrario
	 */
	private boolean actualizarEncabezado(long codigoIncluPresuEncabezado, int codigoCentroAtencion, String loginUsuario, BigDecimal codigoPresupuesto, String estado) {
		
		IncluPresuEncabezado incluPresuEncabezado = new IncluPresuEncabezado();
		
		IUsuariosServicio usuariosServicio = AdministracionFabricaServicio.crearUsuariosServicio();
		Usuarios usuario = usuariosServicio.buscarPorLogin(loginUsuario);
		
		ICentroAtencionServicio centroAtencionServicio = AdministracionFabricaServicio.crearCentroAtencionServicio();
		CentroAtencion centroAtencion = centroAtencionServicio.buscarPorCodigoPK(codigoCentroAtencion);
		
		incluPresuEncabezado.setCodigoPk(codigoIncluPresuEncabezado);
		incluPresuEncabezado.setEstado(estado);
		incluPresuEncabezado.setUsuarios(usuario);
		incluPresuEncabezado.setFecha(UtilidadFecha.getFechaActualTipoBD());
		incluPresuEncabezado.setHora(UtilidadFecha.getHoraActual());
		incluPresuEncabezado.setCentroAtencion(centroAtencion);

		if(estado.equals(ConstantesIntegridadDominio.acronimoContratado)){
			
			incluPresuEncabezado.setOtrosSi(guardarOtroSi(loginUsuario, codigoPresupuesto, codigoCentroAtencion));
		}

		return actualizarIncluPresuEncabezado(incluPresuEncabezado);
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#actualizarIncluPresuEncabezado(com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public boolean actualizarIncluPresuEncabezado(IncluPresuEncabezado incluPresuEncabezado) {
	
		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		
		return incluPresuEncabezadoMundo.actualizarIncluPresuEncabezado(incluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#cargarEncabezadoInclusion(long)
	 */
	@Override
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado) {
	
		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		
		return incluPresuEncabezadoMundo.cargarEncabezadoInclusion(codigoIncluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#calcularTotalInclusion(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DtoTotalesContratarInclusion calcularTotalInclusion(long codigoPkEncabezadoInclusion, int codigoIngreso) throws IPSException
	{
		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		IncluPresuEncabezado encabezadoInclusion=incluPresuEncabezadoMundo.cargarEncabezadoInclusion(codigoPkEncabezadoInclusion);

		DtoTotalesContratarInclusion totales=new DtoTotalesContratarInclusion();
		
		if(encabezadoInclusion!=null){
			Iterator<InclusionesPresupuesto> iteradorInclusiones=encabezadoInclusion.getInclusionesPresupuestos().iterator();
			ArrayList<DtoRegistroContratarInclusion> listaRegistrosInclusion=new ArrayList<DtoRegistroContratarInclusion>();
			for (Iterator<InclusionesPresupuesto> inclusionesPresupuesto = iteradorInclusiones; inclusionesPresupuesto.hasNext();) {
				
				InclusionesPresupuesto inclusionPresupuesto = (InclusionesPresupuesto) inclusionesPresupuesto.next();
				
				if(inclusionPresupuesto!=null && inclusionPresupuesto.getValor()!=null){

					DtoPresuOdoProgServ programa=obtenerProgramaDesdeIncluPresuConvenio(encabezadoInclusion.getIncluPresuConvenios().iterator(), inclusionPresupuesto.getProgramasHallazgoPieza().getCodigoPk(), "");

					DtoRegistroContratarInclusion registroContratarInclusion=new DtoRegistroContratarInclusion();
					registroContratarInclusion.setProgramaServicio(programa);
					listaRegistrosInclusion.add(registroContratarInclusion);
					
				}
			}
			for (DtoRegistroContratarInclusion registroInclusion : listaRegistrosInclusion) {
				
				registroInclusion.setListPresupuestoOdoConvenio(obtenerConveniosPorProgramaDesdeIncluPresuConvenios(encabezadoInclusion, registroInclusion.getProgramaServicio()));
			}
			
			ArrayList<DtoPresupuestoTotalConvenio> listaConvenios=obtenerListadoSumatoriaConvenios(listaRegistrosInclusion, codigoIngreso);
			calcularTotalesConvenios(listaRegistrosInclusion, listaConvenios, totales);
			
		}
		
		return totales;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#obtenerListadoSumatoriaConvenios(java.util.ArrayList, int)
	 */
	@Override
	public ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoSumatoriaConvenios(ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle, int codigoIngreso)
	{
		ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios = new ArrayList<DtoPresupuestoTotalConvenio>();
		
		HashMap<Integer, Integer> registrosConvenio = new HashMap<Integer, Integer>();
				
		for (DtoRegistroContratarInclusion registroInclusion : registrosInclusionDetalle) {
			
			for (DtoPresupuestoOdoConvenio convenio : registroInclusion.getListPresupuestoOdoConvenio()) {

				if(!(registrosConvenio.containsKey(convenio.getContrato().getCodigo()))){
					
					DtoPresupuestoTotalConvenio totalConvenio = new DtoPresupuestoTotalConvenio();
					
					totalConvenio.setContrato(convenio.getContrato().getCodigo());
					totalConvenio.setConvenio(convenio.getConvenio());
					
					totalConvenio.setExisteConvenioEnIngreso(ValidacionesPresupuesto.existeConvenioEnIngreso(codigoIngreso, totalConvenio.getConvenio().getCodigo()));
					
					listaSumatoriaConvenios.add(totalConvenio);
					
					registrosConvenio.put(convenio.getContrato().getCodigo(), convenio.getContrato().getCodigo());
				}
			}
		}
		
		return listaSumatoriaConvenios;
	}


	
	/**
	 * 
	 * Método que centraliza el proceso de contratación de una inclusión 
	 * en estado precontratada
	 * 
	 * @param dtoContratarInclusion
	 * @return
	 */
	private ResultadoBoolean contratarInclusionesPrecontratada (DtoContratarInclusion dtoContratarInclusion) throws IPSException{
	
		ResultadoBoolean resultado = new ResultadoBoolean();
		resultado.setResultado(true);
		resultado.setDescripcion("");
	
		if(dtoContratarInclusion.getValidacion().equals("") && dtoContratarInclusion.getDecision().equals("")){
			
			try
			{
				validarGuardarInclusionPrecontratada (dtoContratarInclusion);

			} catch (ValidacionesPresupuestoException e)
			{
				resultado.setDescripcion(e.getMensaje());
				
				return resultado;
			}
			
		}else{
			
			try
			{
				continuarProcesoValidaciones(dtoContratarInclusion);
				
			} catch (ValidacionesPresupuestoException e)
			{
				resultado.setDescripcion(e.getMensaje());
				return resultado;
			}
		}
		
		if(dtoContratarInclusion.getErrores().isEmpty() && dtoContratarInclusion.getEncabezadoInclusion()!=null
				&& dtoContratarInclusion.getEncabezadoInclusion().getIncluPresuEncabezado().getCodigoPk() > 0 ){

			if(dtoContratarInclusion.getEstado().equals(ConstantesIntegridadDominio.acronimoContratado)){
				
				if(terminarProcesoContratacion(dtoContratarInclusion)){
					
					return resultado;
				}
			
			}else if(dtoContratarInclusion.getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
				
				return actualizarRegistroPrecontratacionInclusion (dtoContratarInclusion);
			}

			return retornarError();
			
		}else{
			
			resultado.setResultado(false);
			resultado.setDescripcion("erroresValidacion");
			return resultado;
		}
	}

	/**
	 * Método que se encarga de realizar las validaciones al contratar un registro
	 * de inclusión precontratado
	 * 
	 * @param dtoContratarInclusion
	 * @throws ValidacionesPresupuestoException Lanza excepción en el caso que se requiera preguntarle al usuario sobre el flujo a seguir
	 * @return true si no ocurre ni ningún problema, false de lo contrario.
	 */
	private boolean validarGuardarInclusionPrecontratada(DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException
	{
		
		//validar si se seleccion el programa en algun convenio para marcarlo como contratado
		
		for (DtoRegistroContratarInclusion registroContratarInclusion : dtoContratarInclusion.getRegistrosContratarInclusion()) {
			
			DtoPresuOdoProgServ dtoProSer = registroContratarInclusion.getProgramaServicio();
			
			dtoProSer.setContratado(ConstantesBD.acronimoNo);
			
			for(DtoPresupuestoOdoConvenio dtoConvenio : registroContratarInclusion.getListPresupuestoOdoConvenio())
			{
				if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
				{
					dtoProSer.setContratado(ConstantesBD.acronimoSi);
					break;
				}
			}
		}
		
		for(DtoPresupuestoTotalConvenio totalConvenio : dtoContratarInclusion.getListaSumatoriaConvenios())
		{
			
			if(totalConvenio.getValorTotalContratado().doubleValue()>0){
				
				boolean pacientePagaAtencion= Contrato.pacientePagaAtencion(totalConvenio.getContrato());
				
				if(!pacientePagaAtencion){
					
					boolean controlaAnticipoContratar= Contrato.controlaAnticipos(totalConvenio.getContrato());
					
					if(controlaAnticipoContratar)
					{
						IControlAnticiposContratoMundo controlAnticiposContratoMundo = FacturacionFabricaMundo.crearControlAnticiposContratoMundo();

						ArrayList<ControlAnticiposContrato> listaControlAnticiposContrato = new ArrayList<ControlAnticiposContrato>();

						listaControlAnticiposContrato = controlAnticiposContratoMundo.determinarContratoRequiereAnticipo(totalConvenio.getContrato());
						
						if(!Utilidades.isEmpty(listaControlAnticiposContrato))
						{
							//Requiere Anticipo al contratar
							if(validarValorAnticipoDisponible(dtoContratarInclusion.getErrores(), totalConvenio)){
								
								break;
							}
						}
					}
				}
			}
		}
		
		return iniciarValidacionesDesdeExistenciaBonos(dtoContratarInclusion);
			
		//validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(dtoContratarInclusion);
		
	}


	/**
	 * Método que centraliza las validaciones para los registros de inclusiones precontratadas desde
	 * la existencia de bonos en adelante.
	 * 
	 * @param dtoContratarInclusion
	 * @return
	 * @throws ValidacionesPresupuestoException
	 */
	private boolean iniciarValidacionesDesdeExistenciaBonos (DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException{
		
		validarExistenciaBonos();
		
		if(validarExistenciaVigenciaPromociones(dtoContratarInclusion) && dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento()!=null){
			
			return validarEstadoSolicitudDescuento(dtoContratarInclusion);
		}
		
		return false;
	}

	/**
	 * Método que se encarga de validar la existencia de Bonos asociados a la contratación
	 * de la inclusión
	 * 
	 * @return 
	 */
	//FIXME Este método queda pendiente por desarrollar ya que no se ha definido el tema de bonos en la contratación
	private boolean validarExistenciaBonos (){
		
		return true;
	}
	
	
	/**
	 * Método que se encarga de validar la existencia de promociones asociadas a la contratación
	 * de la inclusión
	 * @param dtoContratarInclusion 
	 * 
	 * @return 
	 * @throws ValidacionesPresupuestoException 
	 */
	private boolean validarExistenciaVigenciaPromociones(DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException{
		
		DtoDetPromocionOdo detallePromocion = new DtoDetPromocionOdo();
		DtoPromocionesOdontologicas promocion = new DtoPromocionesOdontologicas();
		BigDecimal codigoDetallePromocion;
		ArrayList<DtoPromocionesOdontologicas> listadoPromociones =  new ArrayList<DtoPromocionesOdontologicas>();
		
		for (DtoRegistroContratarInclusion registroInclusion : dtoContratarInclusion.getRegistrosContratarInclusion()) {
			
			listadoPromociones.clear();
			
			for (DtoPresupuestoOdoConvenio convenio : registroInclusion.getListPresupuestoOdoConvenio()) {
				
				if(convenio.getSeleccionadoPromocion() && convenio.getContratado().equals(ConstantesBD.acronimoSi)){
					
					codigoDetallePromocion = new BigDecimal(convenio.getDetallePromocion());
					
					detallePromocion.setCodigoPk(codigoDetallePromocion.intValue());
					detallePromocion = DetPromocionesOdontolgicas.cargarObjeto(detallePromocion);
	
					if(detallePromocion!=null && detallePromocion.getPromocionOdontologia() > 0){
						
						promocion.setCodigoPk(detallePromocion.getPromocionOdontologia());
						
						listadoPromociones = PromocionesOdontologicas.cargar(promocion);
						
						
						if(listadoPromociones.size() > 0 && listadoPromociones.get(0)!=null){
							
							promocion = listadoPromociones.get(0);
							
							if(!promocion.getActivo().equals(ConstantesBD.acronimoSi)
									|| !UtilidadFecha.validarFechaRango(promocion.getFechaInicialVigencia(), promocion.getFechaFinalVigencia(), UtilidadFecha.getFechaActual())
									|| !(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(UtilidadFecha.getHoraActual(), promocion.getHoraFinalVigencia())
										&& UtilidadFecha.esHoraMenorIgualQueOtraReferencia(promocion.getHoraInicialVigencia(), UtilidadFecha.getHoraActual()))){

								throw new ValidacionesPresupuestoException(PREGUNTAR_CONTRATAR_SIN_PROMOCIONES_VIGENTES);
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	

	/**
	 * Método que se encarga de validar el estado de la solicitud de descuento 
	 * asociada al proceso de contratación de la inclusión
	 * 
	 * @param dtoContratarInclusion 
	 * @return 
	 * @throws ValidacionesPresupuestoException 
	 */
	private boolean validarEstadoSolicitudDescuento(DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException{
		
		InfoDefinirSolucitudDsctOdon solicitudDescuento = dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento();
		
		/*
		 * Determinar Vigencia
		 */
		if(solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){
			
			if(validarVigenciaSolicitudDescuentoAutorizada(solicitudDescuento)){
				
				return validarNuevosBonosYPromociones (dtoContratarInclusion);
			
			}else {
				
				throw new ValidacionesPresupuestoException(PREGUNTAR_CONTRATAR_DESCUENTO_AUTORIZADO_VENCIDO);
			}
			
		}else if(solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoXDefinir)
				|| solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoPendienteXAutorizar)){
			
			throw new ValidacionesPresupuestoException(PREGUNTAR_CONTRATAR_DESCUENTO_PDEF_PPAUTO);
		
		}else if(solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoNoAutorizado)
				|| solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado)){
			
			throw new ValidacionesPresupuestoException(PREGUNTAR_CONTRATAR_DESCUENTO_NOAUTO_ANUL);
		
		}else{
			
			return validarNuevosBonosYPromociones (dtoContratarInclusion);
		}
	}

	/**
	 * Método que se encarga de validar si una solicitud de descuento en estado Autorizada
	 * sigue aún vigente o no.
	 *
	 * @param solicitudDescuento
	 * @return true indicando si es vigente, false de lo contrario.
	 */
	private boolean validarVigenciaSolicitudDescuentoAutorizada(InfoDefinirSolucitudDsctOdon solicitudDescuento) {
		
		String fecha = UtilidadFecha.conversionFormatoFechaAAp(solicitudDescuento.getFechaAutorizacion());
		String fechaVigencia = UtilidadFecha.incrementarDiasAFecha(fecha, solicitudDescuento.getDiasVigencia(), false);
		
		return UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), fechaVigencia);

	}
	
	/**
	 * Método que da continuación al proceso de validación de la contratación
	 * de una inclusión en estado precontratada.
	 * 
	 * A este método se llega si y solo si, el usuario decide continuar con el proceso, de lo contrario
	 * desde {@link PresupuestoExclusionesInclusionesAction} se redirecciona.
	 * 
	 * @param validacion
	 * @return
	 * @throws ValidacionesPresupuestoException 
	 */
	private boolean continuarProcesoValidaciones (DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException, IPSException{
		
		String validacion = dtoContratarInclusion.getValidacion();
		
		if(validacion.equals(PREGUNTAR_CONTRATAR_SIN_PROMOCIONES_VIGENTES) ||
		   validacion.equals(PREGUNTAR_CONTRATAR_SIN_BONOS_VIGENTES) ||
		   validacion.equals(PREGUNTAR_CONTRATAR_DESCUENTO_AUTORIZADO_VENCIDO) ||
		   validacion.equals(PREGUNTAR_CONTRATAR_DESCUENTO_PDEF_PPAUTO) ||
		   validacion.equals(PREGUNTAR_CONTRATAR_DESCUENTO_NOAUTO_ANUL))
		{
			return validarNuevosBonosYPromociones(dtoContratarInclusion);
			
		}else if (validacion.equals(PREGUNTAR_CONTRATAR_APLICAR_NUEVOS_BONOS_PROMOCIONES)){
			
			if(dtoContratarInclusion.getDecision().equals(ConstantesBD.acronimoNo)){
				
				return validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(dtoContratarInclusion);
				
			}else{
				
				String estadoSolicitud = dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getAcronimoEstadoSolicitud();
				
				if(estadoSolicitud.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoXDefinir) || 
				   estadoSolicitud.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoPendienteXAutorizar) || 
				   estadoSolicitud.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){
					
					if(estaDefinidoMotivoAnulacionSolicitudAutorizacion (dtoContratarInclusion.getCodigoInstitucion())){

						return recalcularTotalesContratacionInclusionPrecontratada(dtoContratarInclusion);

					}else{
						
						throw new ValidacionesPresupuestoException(NO_DEFINIDO_PARAMETRO_MOTIVO_ANULACION);
					}
				}else{
					
					return recalcularTotalesContratacionInclusionPrecontratada(dtoContratarInclusion);
				}
			}
			
		}else if (validacion.equals(PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO)){
			
			if(dtoContratarInclusion.getDecision().equals(ConstantesBD.acronimoSi)){
				
				/*
				 * Esto quiere decir que se va a precontratar
				 */
				dtoContratarInclusion.setEstado(ConstantesIntegridadDominio.acronimoPrecontratado);
				
				return true;
				
			}else{
				
				return validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(dtoContratarInclusion);
			}
		}
		
		return false;
	}
	
	
	/**
	 * Método que se encarga de verificar si esta definido el parámetro
	 * Motivo anulación solicitud autorización descuento presupuesto odontológico del módulo
	 * de Odontologia.
	 * 
	 * @return
	 */
	private boolean estaDefinidoMotivoAnulacionSolicitudAutorizacion(int codigoInstitucion) {
	
		int codigoMotivo = ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(codigoInstitucion);
		
		if(codigoMotivo > ConstantesBD.codigoNuncaValido){
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Método que se encarga de validar si existen nuevos bonos y promociones
	 * que puedan aplicarse a los programas asociados al proceso de contratación
	 * de Inclusiones
	 * 
	 * @param dtoContratarInclusion
	 * @return
	 * @throws ValidacionesPresupuestoException
	 */
	@SuppressWarnings("unchecked")
	private boolean validarNuevosBonosYPromociones(DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException {
		
		ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusionClon = (ArrayList<DtoRegistroContratarInclusion>) UtilidadClonacion.clonar(dtoContratarInclusion.getRegistrosContratarInclusion());
		
		recalcularBonosPromociones(registrosContratarInclusionClon, dtoContratarInclusion.getCodigoInstitucion(), 
				new BigDecimal(dtoContratarInclusion.getPaciente().getCodigoCuenta()));
		
		dtoContratarInclusion.setRegistrosContratarInclusionClon(registrosContratarInclusionClon);
		
		/*
		 * Si la condición se cumple, indica que los valores cambiaron y existen nuevos bonos y promociones
		 */
		if(!verificarSonIgualesValoresConvenios(dtoContratarInclusion.getRegistrosContratarInclusion(), dtoContratarInclusion.getRegistrosContratarInclusionClon())){

			throw new ValidacionesPresupuestoException(PREGUNTAR_CONTRATAR_APLICAR_NUEVOS_BONOS_PROMOCIONES);
		
		}else{
			
			validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(dtoContratarInclusion);
		}
		
		return true;
	}
	

	/**
	 * Método que recalcula la información de los bonos y promociones para cada uno de los
	 * programas asociados al proceso de contratación de Inclusiones
	 * 
	 * @param registrosContratarInclusion
	 * @param codigoInstitucion
	 * @param cuentaPaciente
	 */
   	private void recalcularBonosPromociones(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int codigoInstitucion, BigDecimal cuentaPaciente)
	{
   		
   		for (DtoRegistroContratarInclusion registroContratarInclusion : registrosContratarInclusion) {

			DtoPresuOdoProgServ dtoPresuOdoProgServ = registroContratarInclusion.getProgramaServicio();

   			for(DtoPresupuestoOdoConvenio convenio : registroContratarInclusion.getListPresupuestoOdoConvenio())
		   	{
   				if(convenio.getContratado().equals(ConstantesBD.acronimoSi)){
   					
   					InfoBonoDcto infoBono= CargosOdon.obtenerDescuentoXBonos(convenio.getConvenio().getCodigo() , 
								cuentaPaciente, dtoPresuOdoProgServ.getPrograma().getCodigo(), 
								dtoPresuOdoProgServ.getServicio().getCodigo(), codigoInstitucion, 
								convenio.getValorUnitarioMenosDctoComercial(), UtilidadFecha.getFechaActual());
		
					InfoPromocionPresupuestoServPrograma infoPromocion= CargosOdon.obtenerDescuentoXPromociones(dtoPresuOdoProgServ.getPrograma().getCodigo(), 
																	dtoPresuOdoProgServ.getServicio().getCodigo(), convenio.getConvenio().getCodigo(), 
																	convenio.getContrato().getCodigo(), UtilidadFecha.getFechaActual(), codigoInstitucion, 
																	cuentaPaciente, convenio.getValorUnitarioMenosDctoComercial());
					
					boolean existeBono= infoBono.getValorDctoCALCULADO().doubleValue()>0;
					
					boolean existePromocion= infoPromocion.getValorPromocion().doubleValue()>0;
				
					//1. verificamos si solo existe bono sin promocion
					if(existeBono && !existePromocion)
					{
						convenio.resetPromocion();
						convenio.setSeleccionadoBono(true);
						convenio.setValorDescuentoBono(infoBono.getValorDctoCALCULADO());
						convenio.setPorcentajeDctoBono(infoBono.getPorcentajeDescuento());
						convenio.setAdvertenciaBono(infoBono.getAdvertencia());
						convenio.setSerialBono(infoBono.getSerial());
						convenio.setSeleccionadoPorcentajeBono(infoBono.isSeleccionadoPorcentaje());
						convenio.setBonoPaciente(infoBono.getBonoPaciente());
					}
					//2. verificamos si solo existe promocion sin bono
					else if(!existeBono && existePromocion)
					{
						convenio.resetBono();
						convenio.setSeleccionadoPromocion(true);
						convenio.setValorDescuentoPromocion(infoPromocion.getValorPromocion());
						convenio.setPorcentajePromocion(infoPromocion.getPorcentajePromocion());
						convenio.setPorcentajeHonorarioPromocion(infoPromocion.getPorcentajeHonorario());
						convenio.setValorHonorarioPromocion(infoPromocion.getValorHonorario());
						convenio.setAdvertenciaPromocion(infoPromocion.getAdvertencia());
						convenio.setSeleccionadoPorcentajePromocion(infoPromocion.isSeleccionadoPorcentaje());
					}
					//3. si no existe ni bono ni promocion
					else if(!existeBono && !existePromocion)
					{
						convenio.resetPromocion();
						convenio.resetBono();
					}
					//4. si existen ambas
					else if(existeBono && existePromocion)
					{
						//lo del seleccionado lo dejamos tal como estaba
						convenio.setValorDescuentoBono(infoBono.getValorDctoCALCULADO());
						convenio.setPorcentajeDctoBono(infoBono.getPorcentajeDescuento());
						convenio.setAdvertenciaBono(infoBono.getAdvertencia());
						convenio.setSerialBono(infoBono.getSerial());
						
						convenio.setValorDescuentoPromocion(infoPromocion.getValorPromocion());
						convenio.setPorcentajePromocion(infoPromocion.getPorcentajePromocion());
						convenio.setPorcentajeHonorarioPromocion(infoPromocion.getPorcentajeHonorario());
						convenio.setValorHonorarioPromocion(infoPromocion.getValorHonorario());
						convenio.setAdvertenciaPromocion(infoPromocion.getAdvertencia());
						
						convenio.setSeleccionadoPorcentajeBono(infoBono.isSeleccionadoPorcentaje());
						convenio.setSeleccionadoPorcentajePromocion(infoPromocion.isSeleccionadoPorcentaje());
					}
   				}
		   	}
   		}
 	}
 	
 	/* (non-Javadoc)
 	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#verificarSonIgualesValoresConvenios(java.util.ArrayList, java.util.ArrayList)
 	 */
 	@Override
	public boolean verificarSonIgualesValoresConvenios(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, 
				ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusionClon)

	{
		for(int i=0; i<registrosContratarInclusion.size(); i++)
		{
			DtoRegistroContratarInclusion programa = registrosContratarInclusion.get(i);
			DtoRegistroContratarInclusion programaClon = registrosContratarInclusionClon.get(i);
			
			for(int j=0; j < programa.getListPresupuestoOdoConvenio().size(); j++)
			{
				DtoPresupuestoOdoConvenio convenio=programa.getListPresupuestoOdoConvenio().get(j);
				DtoPresupuestoOdoConvenio convenioClon=programaClon.getListPresupuestoOdoConvenio().get(j);
				
				if(convenio.getContratado().equals(ConstantesBD.acronimoSi)){
					
					if(convenio.getValorDescuentoPromocionDouble()!=convenioClon.getValorDescuentoPromocionDouble())
					{
						return false;
					}
					
					if(convenio.getPorcentajePromocion()!=convenioClon.getPorcentajePromocion())
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}

 	/**
	 * Método que se encarga de recalcular los totales luego que se decida aplicar nuevos
	 * Bonos y Promociones
	 * 
	 * @param dtoContratarInclusion
	 * @return 
	 * @throws ValidacionesPresupuestoException
	 */
	private boolean recalcularTotalesContratacionInclusionPrecontratada(DtoContratarInclusion dtoContratarInclusion) throws ValidacionesPresupuestoException, IPSException {
		
		IPresupuestoOdontologicoMundo presupuestoOdontologicoMundo =  PresupuestoFabricaMundo.crearPresupuestoOdontologicoMundo();
		
		int codigoInstitucion = dtoContratarInclusion.getCodigoInstitucion();
		int codigoCuentaPaciente = dtoContratarInclusion.getPaciente().getCodigoCuenta();
		String loginUsuario = dtoContratarInclusion.getLoginUsuario();
		
		String ajusteServicio = "";
		
		boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
		
		int i=0;
		for (DtoRegistroContratarInclusion registroContratarInclusion : dtoContratarInclusion.getRegistrosContratarInclusion()) {

			DtoPresuOdoProgServ dtoPresuOdoProgServ = registroContratarInclusion.getProgramaServicio();
			int j=0;
   			for(DtoPresupuestoOdoConvenio convenio : registroContratarInclusion.getListPresupuestoOdoConvenio())
		   	{
   				ajusteServicio = "";
   				
   				if(convenio.getContratado().equals(ConstantesBD.acronimoSi)){
   					
   					ajusteServicio = convenio.getAjusteServicio();
   					
   					convenio = presupuestoOdontologicoMundo.obtenerConvenioProgramaServicio(dtoPresuOdoProgServ, convenio.getConvenio(), 
   						convenio.getContrato(), loginUsuario, utilizaProgramas, codigoInstitucion, codigoCuentaPaciente);
   				
   					convenio.setContratado(ConstantesBD.acronimoSi);
   					convenio.setAjusteServicio(ajusteServicio);
   					dtoContratarInclusion.getRegistrosContratarInclusion().get(i).getListPresupuestoOdoConvenio().set(j, convenio);
   				}
   				j++;
		   	}
   			i++;
		}
	
		dtoContratarInclusion.setListaSumatoriaConvenios(obtenerListadoSumatoriaConvenios(dtoContratarInclusion.getRegistrosContratarInclusion(), 
				dtoContratarInclusion.getPaciente().getCodigoIngreso()));
		
		calcularTotalesConvenios(dtoContratarInclusion.getRegistrosContratarInclusion(), dtoContratarInclusion.getListaSumatoriaConvenios(), 
				dtoContratarInclusion.getEncabezadoInclusion().getTotalesContratarInclusion());
		
		if(dtoContratarInclusion.getEncabezadoInclusion().getTotalesContratarInclusion().getTotalInclusionesParaDescuento().doubleValue() > 0){
			
			if (validarDescuentoOdontologico(dtoContratarInclusion.getCodigoCentroAtencion())){
				
				throw new ValidacionesPresupuestoException(PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO);
			
			}else{
				
				return validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(dtoContratarInclusion);
			}

		}else{
			
			return validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(dtoContratarInclusion);
		}
		
//		for(int i=0; i< dtoContratarInclusion.getRegistrosContratarInclusion().size(); i++)
//		{
//			DtoRegistroContratarInclusion registro =  dtoContratarInclusion.getRegistrosContratarInclusion().get(i);
//			DtoRegistroContratarInclusion registroClon = dtoContratarInclusion.getRegistrosContratarInclusionClon().get(i);
//			
//			for(int j=0; j < registro.getListPresupuestoOdoConvenio().size(); j++)
//			{
//				DtoPresupuestoOdoConvenio convenio=registro.getListPresupuestoOdoConvenio().get(j);
//				DtoPresupuestoOdoConvenio convenioClon=registroClon.getListPresupuestoOdoConvenio().get(j);
//				
//				if(convenio.getContratado().equals(ConstantesBD.acronimoSi)){
//					
//					/*
//					 * Si el convenio anterior tiene asociado una promoción
//					 * y si el convenio con los nuevos valores calculados tiene una nueva promoción.
//					 * se toma esa promoción y se aplica.
//					 */
//					if(convenio.getSeleccionadoPromocion() && convenioClon.getSeleccionadoPromocion()){
//					
//						convenio = convenioClon;
//						
//					}else{
//						
//						/*
//						 * Si el convenio nuevo con los valores calculados no tiene una promoción
//						 * asociada, se deben mantener los valores iniciales con los cuales se contrato.
//						 * por lo que solo es necesario eliminar la promoción asociada inicialmente
//						 */
//						convenio.resetPromocion();
//					}
//				}
//			}
//		}
	}

	/**
	 * Método que valida el saldo de Anticipo versus el valor del contrato para
	 * un registro de inclusión precontratada
	 * 
	 * @param dtoContratarInclusion
	 * @return 
	 */
	private boolean validarSaldoAnticipoVersusValorContratoInclusionPrecontratada(DtoContratarInclusion dtoContratarInclusion) {
		
		boolean resultado = true;
		
		dtoContratarInclusion.setEstado(ConstantesIntegridadDominio.acronimoContratado);
		
		ArrayList<DtoValorAnticipoPresupuesto> listaAnticiposPresupuesto = new ArrayList<DtoValorAnticipoPresupuesto>();
		
		for(DtoPresupuestoTotalConvenio totalConvenio : dtoContratarInclusion.getListaSumatoriaConvenios())
		{
			if(totalConvenio.getValorTotalContratado().doubleValue()>0){
				
				boolean pacientePagaAtencion= Contrato.pacientePagaAtencion(totalConvenio.getContrato());
				DtoValorAnticipoPresupuesto valorAnticipoPresupuesto = null;
				
				if(!pacientePagaAtencion){
					
					if(!validarSaldoAnticipoVersusValorContrato(dtoContratarInclusion.getErrores(), totalConvenio, valorAnticipoPresupuesto)){

						listaAnticiposPresupuesto =  null;
						resultado = false;
						break;
						
					}else if(valorAnticipoPresupuesto!=null){
						
						listaAnticiposPresupuesto.add(valorAnticipoPresupuesto);
					}
				}
			}
		}
		
		dtoContratarInclusion.setListaAnticiposPresupuesto(listaAnticiposPresupuesto);
		
		return resultado;
	}
	
 	/**
	 * Método que se encarga de actualizar la información
	 * de la precontratación de una inclusión precontratada.
	 * Se actualizan los valores para cada uno de los programas y convenios.
	 * 
	 * @param dtoContratarInclusion
	 * @return 
	 */
	private ResultadoBoolean actualizarRegistroPrecontratacionInclusion(DtoContratarInclusion dtoContratarInclusion) throws IPSException {

		long codigoIncluPresuEncabezado = dtoContratarInclusion.getEncabezadoInclusion().getIncluPresuEncabezado().getCodigoPk();

		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		
		String estadoSolicitud = dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getAcronimoEstadoSolicitud();
		
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		
		/*
		 * Se verifica si se debe o no anular la solicitud de descuento
		 */
		if(estadoSolicitud.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoXDefinir) || 
		   estadoSolicitud.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoPendienteXAutorizar) || 
		   estadoSolicitud.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){
			
			resultado = anularSolicitudDescuentoOdontologico(dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getCodigoPkPresupuestoDctoOdon(),
					dtoContratarInclusion.getLoginUsuario(), dtoContratarInclusion.getCodigoInstitucion());
			
			if(resultado.isResultado()){
				
				dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().setAcronimoEstadoSolicitud(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado);
			}
		}
			
	
		/*
		 * Si se cumple se puede continuar.
		 */
		if(resultado.isResultado()){
			
			if(incluPresuEncabezadoMundo.eliminarDetalleInclusiones(codigoIncluPresuEncabezado)){
				
				IncluPresuEncabezado incluPresuEncabezado = incluPresuEncabezadoMundo.cargarDetalleRegistroInclusion(codigoIncluPresuEncabezado);
				
				IUsuariosServicio usuariosServicio = AdministracionFabricaServicio.crearUsuariosServicio();
				Usuarios usuario = usuariosServicio.buscarPorLogin(dtoContratarInclusion.getLoginUsuario());
				
				ICentroAtencionServicio centroAtencionServicio = AdministracionFabricaServicio.crearCentroAtencionServicio();
				CentroAtencion centroAtencion = centroAtencionServicio.buscarPorCodigoPK(dtoContratarInclusion.getCodigoCentroAtencion());
				
				incluPresuEncabezado.setEstado(dtoContratarInclusion.getEstado());
				incluPresuEncabezado.setUsuarios(usuario);
				incluPresuEncabezado.setFecha(UtilidadFecha.getFechaActualTipoBD());
				incluPresuEncabezado.setHora(UtilidadFecha.getHoraActual());
				incluPresuEncabezado.setCentroAtencion(centroAtencion);
				
				
				obtenerInformacionParaPrecontratarInclusion(incluPresuEncabezado, dtoContratarInclusion.getRegistrosContratarInclusion(), 
						dtoContratarInclusion.getEstado(), dtoContratarInclusion.getCodigoInstitucion());
				
				dtoContratarInclusion.getEncabezadoInclusion().recalcularTotalesConDescuentoInclusion();
				
				if(actualizarIncluPresuEncabezado(incluPresuEncabezado))
				{
					return registrarDescuentoOdontologico(dtoContratarInclusion, incluPresuEncabezado.getCodigoPk());
				}
			}
		}

		return retornarError();
	}

	/**
	 * Método que se encarga de contratar definitivamente el registro
	 * precontratado de Inclusión
	 * 
	 * @param dtoContratarInclusion
	 * @param utilizaProgramas
	 * @param listaValorAnticipoPresupuesto
	 * @return 
	 */
	private boolean terminarProcesoContratacion(DtoContratarInclusion dtoContratarInclusion) throws IPSException{

		boolean resultado = false;
		
		if(dtoContratarInclusion.getErrores().isEmpty()){
			
			boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(dtoContratarInclusion.getCodigoInstitucion()));

			if(dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento()!=null){
				
				InfoDefinirSolucitudDsctOdon solicitudDescuento = dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento();
				
				/*
				 * Se verifica si se debe o no anular la solicitud de descuento
				 */
				if(solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoXDefinir) || 
					solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoPendienteXAutorizar) || 
				   (solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado) 
					 && !validarVigenciaSolicitudDescuentoAutorizada(solicitudDescuento))){
					
					ResultadoBoolean resultadoAnulacion = anularSolicitudDescuentoOdontologico(solicitudDescuento.getCodigoPkPresupuestoDctoOdon(), dtoContratarInclusion.getLoginUsuario(), dtoContratarInclusion.getCodigoInstitucion());

					if(!resultadoAnulacion.isResultado()){
						
						return resultadoAnulacion.isResultado();
						
					}else{
						
						dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().setAcronimoEstadoSolicitud(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado);
					}
					
				}else if(solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){

					boolean resultadoActualizar = actualizarParaServicioyConvenioPorcentajeDctoOdontologico(dtoContratarInclusion);
					
					if(resultadoActualizar && !actualizarSolicitudDescuentoOdontologico(dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getCodigoPkPresupuestoDctoOdon(), dtoContratarInclusion.getLoginUsuario())){
						
						return resultado;
					}
				}
			}

			/*
			 * Debe realizar una actualización del estado y de otros valores del encabezado.
			 * Esto se hace solo cuando se va a contratar una solicitud de inclusión en estado
			 * precontratado
			 */
				
			if(!actualizarEncabezado(dtoContratarInclusion.getEncabezadoInclusion().getIncluPresuEncabezado().getCodigoPk(), dtoContratarInclusion.getCodigoCentroAtencion(), 
					dtoContratarInclusion.getLoginUsuario(), dtoContratarInclusion.getCodigoPresupuesto(), dtoContratarInclusion.getEstado())){
				
				return resultado;
			}

			/* 
			 * Esto se hace para que se sincronicen los objetos en sesión y la base de datos
			 * y se pueda continuar con los demás registros
			 */
			
			UtilidadTransaccion.getTransaccion().flush();
			
			
			if(!actualizarContratadoPlanTratamientoSeccionPiezaSuperficie(dtoContratarInclusion.getRegistrosContratarInclusion(), dtoContratarInclusion.getConnection(), 
					dtoContratarInclusion.getLoginUsuario(), utilizaProgramas)){

				return resultado;
			}
			
			if(!insertarInclusion(dtoContratarInclusion.getRegistrosContratarInclusion(), utilizaProgramas, dtoContratarInclusion.getCodigoPresupuesto(), 
					dtoContratarInclusion.getPaciente().getCodigoCuenta(), dtoContratarInclusion.getConnection(), dtoContratarInclusion.getLoginUsuario(), 
					dtoContratarInclusion.getCodigoInstitucion(), dtoContratarInclusion.getEncabezadoInclusion().getIncluPresuEncabezado().getCodigoPk()))
			{	
				return resultado;
			}
			
			if(dtoContratarInclusion.getListaAnticiposPresupuesto() !=null){
				
				for(DtoValorAnticipoPresupuesto dtoAnticipo: dtoContratarInclusion.getListaAnticiposPresupuesto())
				{
					if(!Contrato.modificarValorAnticipoReservadoPresupuesto(dtoContratarInclusion.getConnection(), dtoAnticipo.getContrato(), dtoAnticipo.getValorAnticipo()))
					{

						return resultado;
					}
				}
			}
	
			resultado = true;
		}
		
		return resultado;
	}
	
	
 	/**
 	 * Método que permite actualizar el porcentaje del descuento odontológico
 	 * autorizado para cada uno de los convenios contratados, con el fin de aplicarlo 
 	 * a las inclusiones que se van a contratar. 
 	 * 
 	 * También se actualizan los registros de los servicios asociados a los programas de 
 	 * la inclusión
 	 * 
 	 * @param dtoContratarInclusion
 	 * @return 
 	 */
 	private boolean actualizarParaServicioyConvenioPorcentajeDctoOdontologico(DtoContratarInclusion dtoContratarInclusion) {
 		

 		BigDecimal porcentajeDescuentoOdontologico = dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getPorcentaje();
 		
 		for (DtoRegistroContratarInclusion registroContratarInclusion : dtoContratarInclusion.getRegistrosContratarInclusion()) {

   			for(DtoPresupuestoOdoConvenio convenio : registroContratarInclusion.getListPresupuestoOdoConvenio())
		   	{
   				convenio.setPorcentajeDescuentoOdontologico(porcentajeDescuentoOdontologico);
		   	}
 		}
 		
 		IIncluServicioConvenioMundo incluServicioConvenioMundo = PresupuestoFabricaMundo.crearIncluServicioConvenioMundo();
 		
 		return incluServicioConvenioMundo.actualizarPorcentajeDctoOdontologico(dtoContratarInclusion.getEncabezadoInclusion().getIncluPresuEncabezado().getCodigoPk(), 
 				porcentajeDescuentoOdontologico );
 		
	}

	/* (non-Javadoc)
 	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#anularSolicitudDescuentoOdontologico(java.math.BigDecimal, java.lang.String, int)
 	 */
 	@Override
	public ResultadoBoolean anularSolicitudDescuentoOdontologico (BigDecimal codigoPresupuestoDctoOdon, String loginUsuario, int codigoInstitucion){
 		
 		ResultadoBoolean resultado = new ResultadoBoolean();
		resultado.setResultado(true);
		resultado.setDescripcion("");
	
		DtoPresupuestoOdontologicoDescuento presupuestoDescuentoOdontologico = new DtoPresupuestoOdontologicoDescuento();
		
		presupuestoDescuentoOdontologico.setCodigo(codigoPresupuestoDctoOdon);
		presupuestoDescuentoOdontologico.setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado);
		presupuestoDescuentoOdontologico.setUsuarioFechaModifica(new DtoInfoFechaUsuario(loginUsuario));

		int codigoMotivo = ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(codigoInstitucion);
		
		if(codigoMotivo > ConstantesBD.codigoNuncaValido){
			
			BigDecimal motivo = new BigDecimal(codigoMotivo);
			
			presupuestoDescuentoOdontologico.setMotivo(motivo.doubleValue());

			if(!AutorizacionDescuentosOdon.modificarPresupuestoDescuento(presupuestoDescuentoOdontologico)){
				
				resultado.setResultado(false);
			}
			
			return resultado;
			
		}else{
			
			resultado.setDescripcion(NO_DEFINIDO_PARAMETRO_MOTIVO_ANULACION);
			resultado.setResultado(false);
			return resultado;
		}
	}
	
		/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo#eliminarDetalleInclusiones(long)
	 */
	@Override
	public boolean eliminarDetalleInclusiones(long encabezadoInclusionPresupuesto)
	{
		IIncluPresuEncabezadoMundo incluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearIncluPresuEncabezadoMundo();
		return incluPresuEncabezadoMundo.eliminarDetalleInclusiones(encabezadoInclusionPresupuesto);
	}
	
	
	/**
	 * 
	 * Método que centraliza todo el proceso para guardar las exclusiones seleccionadas
	 * 
	 * @param errores
	 * @param listadoRegistroGuardarExclusion
	 * @param con
	 * @param loginUsuario
	 * @param codigoPresupuesto
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * @return
	 */
	public ResultadoBoolean guardarExclusiones(ActionErrors errores,ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion,
												Connection con, String loginUsuario, BigDecimal codigoPresupuesto, int codigoInstitucion, int codigoCentroAtencion) {
		
		ResultadoBoolean resultado = new ResultadoBoolean();
		resultado.setResultado(true);
		resultado.setDescripcion("");
	
		validarConsecutivoExclusiones(errores, codigoInstitucion);
	
		if(errores.isEmpty()){
			
			boolean utilizaProgramas= UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
			
			if(!eliminarInfoPresupuestoXExclusiones(listadoRegistroGuardarExclusion, con, utilizaProgramas, codigoPresupuesto, loginUsuario, codigoInstitucion, codigoCentroAtencion))
			{	
				return retornarError();
			}
			
			return resultado;
			
		}else{
			
			resultado.setResultado(false);
			resultado.setDescripcion("erroresValidacion");
			return resultado;
		}
	}
	
	
	/**
	 * Método que se encarga de validar que el consecutivo de Inclusiones si este definido
	 * 
	 * @param errores
	 * @param codigoInstitucion
	 */
	private void validarConsecutivoExclusiones(ActionErrors errores, int codigoInstitucion){
		
		if(!estaParametrizadoConsecutivo(codigoInstitucion, ConstantesBD.nombreConsecutivoExclusionProgServOdo)){
			
			MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
			errores.add("", new ActionMessage("errors.notEspecific", mensages.getMessage("InclusionesExclusionesForm.noConsecutivoExclusiones")));
		}
	}
	

	/**
	 * 
	 * Método que se encarga de eliminar la información de las exclusiones del presupuesto.
	 * También actualiza la información en el plan de tratamiento y realiza el registro de
	 * la exclusión.
	 * 
	 * @param listadoRegistroGuardarExclusion
	 * @param con
	 * @param utilizaProgramas
	 * @param codigoPresupuesto
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * @return
	 */
	private boolean eliminarInfoPresupuestoXExclusiones(ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion, 
														Connection con, boolean utilizaProgramas, BigDecimal codigoPresupuesto,
														String loginUsuario, int codigoInstitucion, int codigoCentroAtencion)
	{
		
		DtoExclusionPresupuesto exclusionPresupuesto;
		DtoPresuOdoProgServ dtoPresuOdoProgServConsultado;
		
		for (DtoRegistroGuardarExclusion registroGuardarExclusion : listadoRegistroGuardarExclusion) {
			
			DtoPresuOdoProgServ dtoPresuOdoProgServ  = registroGuardarExclusion.getProgramaServicio();
			
			BigDecimal codigoProgServ = new BigDecimal(dtoPresuOdoProgServ.getProgramaOServicio(utilizaProgramas));
			
			exclusionPresupuesto = new DtoExclusionPresupuesto();
			dtoPresuOdoProgServConsultado = null;
			
			/*
			 * Comentario: Supongo que esta consulta es realizada para asegurarse que existe la 
			 * exclusión que se va a guardar
			 */
			
			for (DtoPresupuestoPiezas pieza : dtoPresuOdoProgServ.getListPresupuestoPiezas()) {
				
				pieza.setActivo(true);
				dtoPresuOdoProgServConsultado = PresupuestoOdontologico.obtenerPresupuestoOdoProgServ
												(codigoPresupuesto, codigoProgServ, utilizaProgramas, con, pieza);
				
				break;
			}
			
			if(dtoPresuOdoProgServConsultado!=null)
			{	
				exclusionPresupuesto.setCodigoPk(new BigDecimal(0));
				exclusionPresupuesto.setPresupuesto(codigoPresupuesto);
				exclusionPresupuesto.setProgramaOservicio(codigoProgServ.intValue());
				exclusionPresupuesto.setUsuarioFechaModifica(new DtoInfoFechaUsuario(loginUsuario));
				exclusionPresupuesto.setUtilizaProgramas(utilizaProgramas);
				exclusionPresupuesto.setValor(dtoPresuOdoProgServ.getValorTarifa());
			}
			
			int contadorSuperficies= 1;
			
			for (DtoPresupuestoPiezas pieza : dtoPresuOdoProgServ.getListPresupuestoPiezas()) {
				
				if(!PresupuestoOdontologico.eliminarProgramaServicioPresupuestoCascada(	con, 
																					codigoPresupuesto, 
																					codigoProgServ.intValue(),
																					utilizaProgramas, 
																					pieza.getPieza().intValue(), 
																					pieza.getHallazgo().intValue(), 
																					pieza.getSuperficie().intValue(), 
																					pieza.getSeccion(), 
																					loginUsuario,
																					contadorSuperficies==pieza.getNumSuperficies()))
				{

					return false;
				}
		
				if(dtoPresuOdoProgServConsultado!=null)
				{	
					exclusionPresupuesto .getDetalleSuperficies().add(new DtoDetalleExclusionSuperficies(pieza.getCodigoDetallePlanTratamiento()));
				}
				
				contadorSuperficies++;

			}
			
			if(dtoPresuOdoProgServConsultado!=null)
			{
				registroGuardarExclusion.setExclusionPresupuesto(exclusionPresupuesto);
				registroGuardarExclusion.setExclusionRegistrada(true);
			}
		}
		
		if(!actualizarEstadoPlanTratamientoExclusiones(listadoRegistroGuardarExclusion, con, loginUsuario, utilizaProgramas)){
			
			return false;
		}
		
		OtrosSi otroSi = guardarOtroSi(loginUsuario, codigoPresupuesto, codigoCentroAtencion);
		
		if(otroSi!=null && otroSi.getCodigoPk() <= ConstantesBD.codigoNuncaValidoLong){
			
			return false;
		}
		
		long codigoExcluPresuEncabezado = registrarExclusionPresupuestoEncabezado (otroSi.getCodigoPk(), codigoPresupuesto.longValue(), loginUsuario, codigoInstitucion, con);

		if(codigoExcluPresuEncabezado <= ConstantesBD.codigoNuncaValido){
			
			return false;
		}
		
		UtilidadTransaccion.getTransaccion().flush();
		
		if(!insertarExclusionTabla(listadoRegistroGuardarExclusion, con, codigoExcluPresuEncabezado))
		{	
			return false;
		}
		
		return true;
	}
	
	/**
	 * Método que actualiza la información del plan de tratamiento del paciente
	 * teniendo en cuenta las exclusiones realizadas
	 * 
	 * @param listadoRegistroGuardarExclusion
	 * @param con
	 * @param loginUsuario
	 * @param utilizaProgramas
	 * @return
	 */
	private boolean actualizarEstadoPlanTratamientoExclusiones(ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion,
																Connection con, String loginUsuario, boolean utilizaProgramas)
	{
		
		for (DtoRegistroGuardarExclusion registroGuardarExclusion : listadoRegistroGuardarExclusion) {
			
			DtoPresuOdoProgServ dtoPresuProgServ = registroGuardarExclusion.getProgramaServicio();
			
			for (DtoPresupuestoPiezas pieza : dtoPresuProgServ.getListPresupuestoPiezas()) {

				DtoProgramasServiciosPlanT newPrograma = new DtoProgramasServiciosPlanT();											 		          
				newPrograma.setDetPlanTratamiento(pieza.getCodigoDetallePlanTratamiento());
				
				if(utilizaProgramas)
				{	
					newPrograma.setPrograma(dtoPresuProgServ.getPrograma());
				}	
				else
				{	
					newPrograma.setServicio(dtoPresuProgServ.getServicio());
				}
				
				newPrograma.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				newPrograma.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				newPrograma.setPorConfirmado(ConstantesBD.acronimoNo);
				newPrograma.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
				
				if (!PlanTratamiento.modicarEstadosDetalleProgServ(newPrograma , con))
				{
					return false;
				}
			}
		}
		
		return true;
	}

	
	/**
	 * Método que realiza el registro de la exclusión
	 * 
	 * @param listadoRegistroGuardarExclusion
	 * @param con
	 * @param codigoExcluPresuEncabezado
	 * @return
	 */
	private boolean insertarExclusionTabla(ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion, Connection con, long codigoExcluPresuEncabezado)
	{
		for(DtoRegistroGuardarExclusion registroGuardarExclusion: listadoRegistroGuardarExclusion)
		{
			if(registroGuardarExclusion.isExclusionRegistrada()){
				
				DtoExclusionPresupuesto exclusion = registroGuardarExclusion.getExclusionPresupuesto();
				exclusion.setCodigoExcluPresuEncabezado(codigoExcluPresuEncabezado);
				
				double secuenciaPk = PresupuestoExclusionesInclusiones.guardarExclusion(con, exclusion);
				
				if(secuenciaPk <= 0){

					return false;
					
				}else{
					
					exclusion.setCodigoPk(new BigDecimal(secuenciaPk));
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Método que se encarga de registrar el encabazado con la información del proceso de Exclusión
	 * 
	 * @param codigoOtroSi
	 * @param codigoPresupuesto
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param con 
	 * @return
	 */
	private long registrarExclusionPresupuestoEncabezado (long codigoOtroSi, long codigoPresupuesto, String loginUsuario, int codigoInstitucion, Connection con ){
		
		long codigoExcluPresuEncabezado =  ConstantesBD.codigoNuncaValidoLong;
		
		IExcluPresuEncabezadoMundo excluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearExcluPresuEncabezadoMundo();
		
		ExcluPresuEncabezado excluPresuEncabezado =  new ExcluPresuEncabezado();
		
		Usuarios usuario = new Usuarios();
		usuario.setLogin(loginUsuario);
		
		com.servinte.axioma.orm.PresupuestoOdontologico presupuestoOdontologico = new com.servinte.axioma.orm.PresupuestoOdontologico();
		presupuestoOdontologico.setCodigoPk(codigoPresupuesto);
		
		OtrosSi otroSi = new OtrosSi();
		otroSi.setCodigoPk(codigoOtroSi);
		
		BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoExclusionProgServOdo, codigoInstitucion));
		
		excluPresuEncabezado.setUsuarios(usuario);
		excluPresuEncabezado.setConsecutivo(consecutivo.longValue());
		excluPresuEncabezado.setOtrosSi(otroSi);
		excluPresuEncabezado.setFecha(UtilidadFecha.getFechaActualTipoBD());
		excluPresuEncabezado.setHora(UtilidadFecha.getHoraActual());
		excluPresuEncabezado.setPresupuestoOdontologico(presupuestoOdontologico);

		codigoExcluPresuEncabezado = excluPresuEncabezadoMundo.registrarExclusionPresupuestoEncabezado(excluPresuEncabezado);
		
		if(codigoExcluPresuEncabezado > ConstantesBD.codigoNuncaValidoLong){
			
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoExclusionProgServOdo, codigoInstitucion, consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
	
		}else{
			
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoExclusionProgServOdo, codigoInstitucion ,consecutivo.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		}

		return codigoExcluPresuEncabezado;
	}
	
	/**
	 * Método que consulta los registros de Exclusión realizados al presupuesto del paciente
	 * 
	 * @param codigoPresupuesto
	 * @return 
	 */
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion(long codigoPresupuesto){
		
		IExcluPresuEncabezadoMundo excluPresuEncabezadoMundo = PresupuestoFabricaMundo.crearExcluPresuEncabezadoMundo();
		
		return excluPresuEncabezadoMundo.cargarRegistrosExclusion(codigoPresupuesto);
	}
	
	

	/**
	 * Método que actualiza la solicitud de descuento odontológico Autorizada a Contratada
	 * cuando se contrata la inclusión.
	 * 
	 * @param codigoPresupuestoDctoOdon
	 * @param loginUsuario
	 * @return true si se la actualización fue realizada exitosamente, false de lo contrario
	 */
	private boolean actualizarSolicitudDescuentoOdontologico (BigDecimal codigoPresupuestoDctoOdon, String loginUsuario){
 		
 		
		DtoPresupuestoOdontologicoDescuento presupuestoDescuentoOdontologico = new DtoPresupuestoOdontologicoDescuento();
		
		presupuestoDescuentoOdontologico.setCodigo(codigoPresupuestoDctoOdon);
		presupuestoDescuentoOdontologico.setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoContratado);
		presupuestoDescuentoOdontologico.setUsuarioFechaModifica(new DtoInfoFechaUsuario(loginUsuario));
	
		return AutorizacionDescuentosOdon.modificarPresupuestoDescuento(presupuestoDescuentoOdontologico);
	}
}
