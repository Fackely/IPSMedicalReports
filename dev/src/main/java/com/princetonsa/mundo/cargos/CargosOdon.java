package com.princetonsa.mundo.cargos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoPorcentajeValor;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifa;
import util.facturacion.InfoTarifaVigente;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.InfoBonoDcto;
import util.odontologia.InfoDctoOdontologicoPresupuesto;
import util.odontologia.InfoPresupuestoXConvenioProgramaServicio;
import util.odontologia.InfoPromocionPresupuestoServPrograma;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.princetonsa.mundo.odontologia.PromocionesOdontologicas;
import com.princetonsa.mundo.parametrizacion.Servicios;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.impl.administracion.CentroCostoMundo;
import com.servinte.axioma.orm.CentrosCosto;

/**
 * 
 * @author axioma
 *
 */
public class CargosOdon 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(CargosOdon.class);
	
	private static final int ESCALA_DIVISION=5;
	
	
	/**
	 * 
	 * @param servicioExcluyente
	 * @param programa
	 * @param cantidadPrograma
	 * @param convenio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param cuenta
	 * @param valorDescuentoPromocionExcluyente
	 * @param porcentajeDescuentoPromocionExcluyente
	 * @return
	 */
	public static InfoPresupuestoXConvenioProgramaServicio obtenerPresupuestoXConvenio(	final int servicioExcluyente,
																		final double programaExcluyente,
																		final int cantidadProgramaServicio,
																		final int convenio, 
																		final int contrato, 
																		String fechaCalculoVigencia, 
																		final int institucion, 
																		final BigDecimal cuenta
																		) throws IPSException
	{
		logger.info("\n\n\n\n******************************************************************************************************************\n\n");
		logger.info("******************************* INICIO DEL CALCULO DEL PRESUPUESTO **************************************************");
		logger.info("******************************************************************************************************************");
		logger.info("servicioExcluyente-->"+servicioExcluyente);
		logger.info("programaExcluyente-->"+programaExcluyente);
		logger.info("cantidadProgramaServicio-->"+cantidadProgramaServicio);
		logger.info("convenio-->"+convenio);
		logger.info("contrato-->"+contrato);
		logger.info("fechaCalculoVigencia-->"+fechaCalculoVigencia);
		logger.info("institucion->"+institucion);
		logger.info("cuenta-->"+cuenta.doubleValue());
		
		InfoPresupuestoXConvenioProgramaServicio info = new InfoPresupuestoXConvenioProgramaServicio();
		info.setCantidad(cantidadProgramaServicio);
		info.setConvenio(convenio);
		info.setProgramaExcluyente(programaExcluyente);
		info.setServicioExcluyente(servicioExcluyente);
		
		info.setDetalleTarifasServicio(obtenerTarifaUnitariaXPresupuestoOservicio(	programaExcluyente, 
																					servicioExcluyente, 
																					convenio, 
																					contrato, 
																					fechaCalculoVigencia, 
																					institucion, 
																					cuenta
																					)); 
		
		obtenerErroresCoberturasXPrograma(servicioExcluyente, programaExcluyente,convenio, contrato, fechaCalculoVigencia, institucion, cuenta,info);
		
		if(programaExcluyente>0)
		{
			logger.info("ENTRAMOS A CALCULAR DCTO COMERCIAL X SERVICIO");
			calcularDctoComercialXServicio(programaExcluyente, contrato,fechaCalculoVigencia, institucion, cuenta, info);
		}
	
		logger.info("info.getValorUnitarioProgramaServicioConvenioConDctoComercial()---->"+info.getValorUnitarioProgramaServicioConvenioConDctoComercial());
		
		info.setDetalleBonoDescuento(obtenerDescuentoXBonos(convenio, cuenta, programaExcluyente, servicioExcluyente, institucion, info.getValorUnitarioProgramaServicioConvenioConDctoComercial(), fechaCalculoVigencia));
		info.setDetallePromocionDescuento(obtenerDescuentoXPromociones(programaExcluyente, servicioExcluyente, convenio, contrato, fechaCalculoVigencia, institucion, cuenta, info.getValorUnitarioProgramaServicioConvenioConDctoComercial()));
		
		////SI EL CALCULO SE REALIZA X PROGRAMA ENTONCES DEBEMOS CALCULAR LOS EQUIVALENTES DE LOS DECUENTOS X CADA UNO DE LOS SERVICIOS DEL PROG 
		///se obtiene el dcto comercial
		if(programaExcluyente>0)
		{
			calcularDctoBonoXServicio(info);
			calcularDctoPromocionXServicio(info);
		}	
		
		for(InfoTarifaServicioPresupuesto infoTarifa: info.getDetalleTarifasServicio())
		{	
			logger.info(UtilidadLog.obtenerString(infoTarifa, true));
		}
		
		logger.info("sele % prom-->"+info.getDetallePromocionDescuento().isSeleccionadoPorcentaje());
		logger.info("tarifa programa/servicio-->"+info.getValorTotalProgramaServicioConvenio());
		logger.info("tarifa programa/servicio-->"+info.getValorTotalProgramaServicioConvenio());
		logger.info("******************************************************************************************************************");
		logger.info("******************************* FIN DEL CALCULO DEL PRESUPUESTO **************************************************");
		logger.info("******************************************************************************************************************\n\n");
		
		return info;
	}

	/**
	 * @param servicioExcluyente
	 * @param programaExcluyente
	 * @param convenio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param cuenta
	 * @param info
	 */
	private static void obtenerErroresCoberturasXPrograma(
			final int servicioExcluyente, final double programaExcluyente,
			final int convenio, final int contrato,
			String fechaCalculoVigencia, final int institucion,
			final BigDecimal cuenta,
			InfoPresupuestoXConvenioProgramaServicio info) throws IPSException
	{
		//LO PRIMERO QUE DEBEMOS HACER ES VERIFICAR SI LA COBERTURA SE EVALUA A NIVEL DEL PROGRAMA O A NIVEL DEL DETALLE DE LOS SERVICIOS
		if(programaExcluyente>0)
		{
			String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(cuenta.intValue()+"").getAcronimo();
			int codigoNaturalezaPaciente=0; 
			InfoCobertura infoCobertura=new InfoCobertura(); 
			if(Contrato.manejaCobertura(contrato))
			{
				infoCobertura=Cobertura.validacionCoberturaPrograma(contrato, ConstantesBD.codigoViaIngresoConsultaExterna, tipoPaciente, programaExcluyente, codigoNaturalezaPaciente, institucion);
				if(!infoCobertura.existe() || !infoCobertura.getIncluido())
				{
					logger.info("NO EXISTE COBERTURA PARA EL PROGRAMA.......");
					if(info.getDetalleTarifasServicio().size()>0)
					{	
						info.getDetalleTarifasServicio().get(0).setError("No cubierto");
					}	
				}
			}
		}
	}

	/**
	 * @param info
	 */
	private static void calcularDctoPromocionXServicio(
			InfoPresupuestoXConvenioProgramaServicio info) throws IPSException
	{
		logger.info("ENTRAMOS A CALCULAR PROMOCION DE DESCUENTO..............");
		logger.info("%-->"+info.getDetallePromocionDescuento().getPorcentajePromocion());
		if(info.getDetallePromocionDescuento().getPorcentajePromocion()>0)
		{
			for(InfoTarifaServicioPresupuesto infoTarifaServicio: info.getDetalleTarifasServicio())
			{
				logger.info("error-->"+infoTarifaServicio.getError());
				if(UtilidadTexto.isEmpty(infoTarifaServicio.getError()))
				{	
					logger.info("getValorTarifaUnitariaConDctoComercial()-->"+infoTarifaServicio.getValorTarifaUnitariaConDctoComercial());
					logger.info("info.getDetallePromocionDescuento().getPorcentajePromocion-->"+info.getDetallePromocionDescuento().getPorcentajePromocion());
					infoTarifaServicio.setValorDescuentoPromocionUnitario(infoTarifaServicio.getValorTarifaUnitariaConDctoComercial().multiply( (new BigDecimal(info.getDetallePromocionDescuento().getPorcentajePromocion()).divide(new BigDecimal(100), ESCALA_DIVISION, RoundingMode.HALF_EVEN))));
					infoTarifaServicio.setPorcentajeDescuentoPromocionUnitario(info.getDetallePromocionDescuento().getPorcentajePromocion());
					
					logger.info("\n\n\n\n777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777HONORARIOS");
					logger.info("infoTarifaServicio.setValorHonorarioPromocion()-->"+infoTarifaServicio.getValorTarifaUnitariaConDctoComercial()+" * info.getDetallePromocionDescuento().getPorcentajeHonorario() "+info.getDetallePromocionDescuento().getPorcentajeHonorario()+"/100");
					infoTarifaServicio.setValorHonorarioPromocion(infoTarifaServicio.getValorTarifaUnitariaConDctoComercial().multiply(( new BigDecimal(info.getDetallePromocionDescuento().getPorcentajeHonorario()/100))));
					infoTarifaServicio.setPorcentajeHonorarioPromocion(info.getDetallePromocionDescuento().getPorcentajeHonorario());
					
					logger.info("infoTarifaServicio.setValorHonorarioPromocion->"+infoTarifaServicio.getValorHonorarioPromocion());
					logger.info("infoTarifaServicio.setPorcentajeHonorarioPromocion-->"+infoTarifaServicio.getPorcentajeHonorarioPromocion());
					
					//infoTarifaServicio.setValorHonorarioPromocion(infoTarifaServicio.getValorHonorarioPromocion().multiply( (info.getDetallePromocionDescuento().getPorcentajeHonorarioCALCULADO().divide(new BigDecimal(100), 5))));
					logger.info("valorDctoProm-->"+infoTarifaServicio.getValorDescuentoPromocionUnitario());
					if(!UtilidadTexto.isEmpty(infoTarifaServicio.getMetodoAjuste()))
					{	
						if(infoTarifaServicio.getValorDescuentoPromocionUnitario().doubleValue()>0)
						{	
							infoTarifaServicio.setValorDescuentoPromocionUnitario(new BigDecimal(UtilidadValidacion.aproximarMetodoAjuste(infoTarifaServicio.getMetodoAjuste(), infoTarifaServicio.getValorDescuentoPromocionUnitario().doubleValue())));
						}
						if(infoTarifaServicio.getValorHonorarioPromocion().doubleValue()>0)
						{	
							infoTarifaServicio.setValorHonorarioPromocion(new BigDecimal(UtilidadValidacion.aproximarMetodoAjuste(infoTarifaServicio.getMetodoAjuste(), infoTarifaServicio.getValorHonorarioPromocion().doubleValue())));
						}	
					}
					
					logger.info("FINAL infoTarifaServicio.setValorHonorarioPromocion->"+infoTarifaServicio.getValorHonorarioPromocion());
					logger.info("FINAL infoTarifaServicio.setPorcentajeHonorarioPromocion-->"+infoTarifaServicio.getPorcentajeHonorarioPromocion());
					
				}	
			}
		}
	}

	/**
	 * @param info
	 */
	private static void calcularDctoBonoXServicio(
			InfoPresupuestoXConvenioProgramaServicio info) throws IPSException
	{
		logger.info("ENTRAMOS A CALCULAR BONO DE DESCUENTO..............");
		logger.info("%-->"+info.getDetalleBonoDescuento().getPorcentajeDescuento());
		if(info.getDetalleBonoDescuento().getPorcentajeDescuento()>0)
		{
			for(InfoTarifaServicioPresupuesto infoTarifaServicio: info.getDetalleTarifasServicio())
			{
				logger.info("error-->"+infoTarifaServicio.getError());
				if(UtilidadTexto.isEmpty(infoTarifaServicio.getError()))
				{	
					logger.info("getValorTarifaUnitariaConDctoComercial()-->"+infoTarifaServicio.getValorTarifaUnitariaConDctoComercial());
					logger.info("info.getDetalleBonoDescuento().getPorcentajeDescuentoCALCULADO()-->"+info.getDetalleBonoDescuento().getPorcentajeDescuento());
					infoTarifaServicio.setValorDescuentoBonoUnitario(infoTarifaServicio.getValorTarifaUnitariaConDctoComercial().multiply( (new BigDecimal(info.getDetalleBonoDescuento().getPorcentajeDescuento()).divide(new BigDecimal(100), ESCALA_DIVISION, RoundingMode.HALF_EVEN)) ));
					logger.info("valorDctoBono-->"+infoTarifaServicio.getValorDescuentoBonoUnitario());
					if(!UtilidadTexto.isEmpty(infoTarifaServicio.getMetodoAjuste()))
					{	
						infoTarifaServicio.setValorDescuentoBonoUnitario(new BigDecimal(UtilidadValidacion.aproximarMetodoAjuste(infoTarifaServicio.getMetodoAjuste(), infoTarifaServicio.getValorDescuentoBonoUnitario().doubleValue())));
						infoTarifaServicio.setPorcentajeDecuentoBonoUnitario(info.getDetalleBonoDescuento().getPorcentajeDescuento());
					}	
				}	
			}
		}
	}

	/**
	 * @param programaExcluyente
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param cuenta
	 * @param info
	 */
	private static void calcularDctoComercialXServicio(
			final double programaExcluyente, final int contrato,
			String fechaCalculoVigencia, final int institucion,
			final BigDecimal cuenta,
			InfoPresupuestoXConvenioProgramaServicio info) throws IPSException
	{
		int codigoViaIngreso= ConstantesBD.codigoViaIngresoConsultaExterna;
		String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(cuenta.intValue()+"").getAcronimo();
		InfoPorcentajeValor dctoComercialSinAjuste= obtenerDescuentoComercialPrograma(programaExcluyente, contrato, fechaCalculoVigencia, institucion, codigoViaIngreso, tipoPaciente, info.getValorUnitarioProgramaServicioConvenio());
		
		//si ese valor del descuento comercial es mayor que cero entonces debemos repartir ese valor en los detalles de los servicios
		
		logger.info("DCTO COMERCIAL %-->"+dctoComercialSinAjuste.getPorcentaje());
		
		if(dctoComercialSinAjuste.getPorcentaje()>0)
		{
			for(InfoTarifaServicioPresupuesto infoTarifaServicio: info.getDetalleTarifasServicio())
			{
				if(UtilidadTexto.isEmpty(infoTarifaServicio.getError()))
				{	
					infoTarifaServicio.setValorDescuentoComercial(infoTarifaServicio.getValorTarifaUnitaria().multiply(  new BigDecimal(dctoComercialSinAjuste.getPorcentaje()/100)));
					if(!UtilidadTexto.isEmpty(infoTarifaServicio.getMetodoAjuste()))
					{	
						infoTarifaServicio.setValorDescuentoComercial(new BigDecimal(UtilidadValidacion.aproximarMetodoAjuste(infoTarifaServicio.getMetodoAjuste(), infoTarifaServicio.getValorDescuentoComercial().doubleValue())));
						logger.info("el valor del dcto comercial a setear es-->"+infoTarifaServicio.getValorDescuentoComercial());
						logger.info("Valor tarifa unitaria final-->"+infoTarifaServicio.getValorTarifaUnitaria().subtract(infoTarifaServicio.getValorTarifaUnitaria().subtract(infoTarifaServicio.getValorDescuentoComercial())));
					}	
				}	
			}
		}
	}
	
	/**
	 * Obtiene el descuento generado por los bonos
	 * @param convenio Convenio para el cual se busca el bono
	 * @param cuenta Cuenta del paciente
	 * @param programaExcluyente Programa para el cual se busca el bono (Excluyente con servicio)
	 * @param servicioExcluyente Servicio para el cual se busca el bono (Excluyente con programa)
	 * @param institucion Institución a la que pertenece el usuario en sesión
	 * @param valorUnitarioProgramaServicioConvenio Valor unitario del programa o del servicio
	 * @return
	 */
	public static InfoBonoDcto obtenerDescuentoXBonos(int convenio,
			BigDecimal cuenta, double programaExcluyente,
			int servicioExcluyente, int institucion,
			BigDecimal valorUnitarioProgramaServicioConvenio, String fechaCalculoApp)
	{
		return obtenerDescuentosBono(cuenta.intValue(), institucion, valorUnitarioProgramaServicioConvenio, programaExcluyente, servicioExcluyente, convenio, fechaCalculoApp);
	}

	/**
	 * 
	 * @param programaExcluyente
	 * @param codigoNuncaValido
	 * @param convenio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param cuenta
	 * @param info 
	 * @return
	 */
	public static InfoPromocionPresupuestoServPrograma obtenerDescuentoXPromociones(	final double programaExcluyente, 
																			final int servicioExcluyente, 
																			final int convenio, 
																			final int contrato,
																			String fechaCalculoVigencia, 
																			final int institucion,
																			final BigDecimal cuenta, 
																			final BigDecimal valorUnitarioProgramaServicio
																			)
	{
		InfoPromocionPresupuestoServPrograma infoDescuento= new InfoPromocionPresupuestoServPrograma();
		//PRIMERO VERIFICAMOS SI EL CONVENIO MANEJA PROMOCIONES
		logger.info("MANEJA DESCUENTOS???????????????");
		logger.info("valor-->"+valorUnitarioProgramaServicio);
		
		if(Convenio.manejaPromociones(convenio))
		{	
			logger.info("\n\n******************************************************************************************************");
			logger.info("INICIAMOS CON LOS DESCUENTOS X PROMOCIONES**************************************");
			/* key= fechabd, hora, programa, servicio, regionCentroAntencion, paisCentroAtencion, ciudadCentroAtencion
			 * deptoCentroAtencion, centroAtencion, convenio, edad, sexo, estadoCivil, nro_hijos, ocupacion
			 */
			HashMap<String, String> vo= obtenerFiltrosPromociones(cuenta);
			setearOtrosFiltrosPromociones(programaExcluyente, servicioExcluyente,convenio, fechaCalculoVigencia, vo);
			
			ArrayList<InfoPromocionPresupuestoServPrograma> arrayPromocion= new ArrayList<InfoPromocionPresupuestoServPrograma>();
			arrayPromocion= PromocionesOdontologicas.obtenerValorDescuentoPromociones(vo);
			
			boolean tomarMayorDescuento= !ValoresPorDefecto.getPrioridadParaAplicarPromocionesOdo(institucion).equals(ConstantesBD.acronimoMenorPorcentajeDescuentoValor);
			logger.info("tomarMayorDescuento--->"+tomarMayorDescuento);
			
			if(tomarMayorDescuento)
				infoDescuento= calcularMayorDescuento(arrayPromocion, valorUnitarioProgramaServicio, programaExcluyente>0);
			else
				infoDescuento= calcularMenorDescuento(arrayPromocion, valorUnitarioProgramaServicio, programaExcluyente>0);
			
			logger.info("DESCUENTO ENCABEZADO UNITARIO-->"+infoDescuento.getValorPromocion());
			logger.info("selecionado prom %->"+infoDescuento.isSeleccionadoPorcentaje());
			logger.info("FIN DESCUENTOS X PROMOCIONES******************************************************************************************************");
		}	
		return infoDescuento;
	}

	/**
	 * 
	 * @param obtenerValorDescuentoPromociones
	 * @param valorTotalConvenio
	 * @return
	 */
	private static InfoPromocionPresupuestoServPrograma calcularMayorDescuento(
										ArrayList<InfoPromocionPresupuestoServPrograma> arrayPromocion,
										BigDecimal valorUnitarioProgramaServicioConvenio,
										boolean utilizaProgramas
										)
	{
		InfoPromocionPresupuestoServPrograma valorPromocion= new InfoPromocionPresupuestoServPrograma();
		
		for(InfoPromocionPresupuestoServPrograma descuento: arrayPromocion)
		{
			boolean asignadoDcto= false;
			if(descuento.getValorPromocion().doubleValue()>0)
			{
				if(descuento.getValorPromocion().doubleValue()> valorPromocion.getValorPromocion().doubleValue())
				{
					valorPromocion= descuento;
					asignadoDcto=true;
					valorPromocion.setDetPromocion(descuento.getDetPromocion());
					valorPromocion.setSeleccionadoPorcentaje(descuento.isSeleccionadoPorcentaje());
					
					//en caso de que supere el valor de la promocion el valor unitario, entonces se aplica lo maxiom
					if(valorPromocion.getValorPromocion().doubleValue()>valorUnitarioProgramaServicioConvenio.doubleValue())
					{
						valorPromocion.setValorPromocion(valorUnitarioProgramaServicioConvenio);
					}
					else
					{	
						valorPromocion.setValorPromocion(descuento.getValorPromocion());
					}
					
					if(	valorPromocion.getPorcentajePromocion()<=0 
						&& valorPromocion.getValorPromocion().doubleValue()>0
						&& valorUnitarioProgramaServicioConvenio.doubleValue()>0)
					{
						logger.info("valorPromocion.getValor-->"+valorPromocion.getValorPromocion()+"  valorUnitarioProgramaServicioConvenio->"+valorUnitarioProgramaServicioConvenio);
						logger.info("valorPromocion.getValorDescuentosUnitarioPromociones.multiply(new BigDecimal(100))->"+valorPromocion.getValorPromocion().multiply(new BigDecimal(100)));
						valorPromocion.setPorcentajePromocion(((valorPromocion.getValorPromocion().multiply(new BigDecimal(100))).divide(valorUnitarioProgramaServicioConvenio, ESCALA_DIVISION, RoundingMode.HALF_EVEN)).doubleValue());
					}
				}
			}
			
			else if(descuento.getPorcentajePromocion()>0)
			{
				if(valorUnitarioProgramaServicioConvenio.multiply( new BigDecimal(descuento.getPorcentajePromocion()/100)).doubleValue() > valorPromocion.getValorPromocion().doubleValue())
				{
					asignadoDcto= true;
					valorPromocion.setSeleccionadoPorcentaje(descuento.isSeleccionadoPorcentaje());
					valorPromocion.setDetPromocion(descuento.getDetPromocion());
					valorPromocion.setValorPromocion(valorUnitarioProgramaServicioConvenio.multiply( new BigDecimal(descuento.getPorcentajePromocion()/100)));
					valorPromocion.setPorcentajePromocion(descuento.getPorcentajePromocion());
				}
			}
			
			//si fue asignado ese dcto entonces debemos hacer el calculo de los honorarios
			if(asignadoDcto)
			{
				calcularHonorarios(valorUnitarioProgramaServicioConvenio,valorPromocion, descuento);
			}
			
		}

		logger.info("el valor promocion es --->"+valorPromocion.getValorPromocion()+" %-->"+valorPromocion.getPorcentajePromocion()+" detalle-->"+valorPromocion.getDetPromocion());
		return valorPromocion;
	}

	/**
	 * 
	 * @param obtenerValorDescuentoPromociones
	 * @param valorTotalConvenio
	 * @return
	 */
	private static InfoPromocionPresupuestoServPrograma calcularMenorDescuento(
										ArrayList<InfoPromocionPresupuestoServPrograma> arrayPromocion,
										BigDecimal valorUnitarioProgramaServicioConvenio,
										boolean utilizaProgramas
										)
	{
		InfoPromocionPresupuestoServPrograma valorPromocion= new InfoPromocionPresupuestoServPrograma();
		
		for(InfoPromocionPresupuestoServPrograma descuento: arrayPromocion)
		{
			boolean asignadoDcto= false;
			if(descuento.getValorPromocion().doubleValue()>0)
			{
				if(descuento.getValorPromocion().doubleValue()< valorPromocion.getValorPromocion().doubleValue()
						|| valorPromocion.getValorPromocion().doubleValue()<=0)
				{
					valorPromocion= descuento;
					asignadoDcto=true;
					valorPromocion.setDetPromocion(descuento.getDetPromocion());
					valorPromocion.setSeleccionadoPorcentaje(descuento.isSeleccionadoPorcentaje());
					
					if(valorPromocion.getValorPromocion().doubleValue()>valorUnitarioProgramaServicioConvenio.doubleValue())
					{
						valorPromocion.setValorPromocion(valorUnitarioProgramaServicioConvenio);
					}
					else
					{	
						valorPromocion.setValorPromocion(descuento.getValorPromocion());
					}
					if(valorPromocion.getPorcentajePromocion()<=0 
						&& valorPromocion.getValorPromocion().doubleValue()>0
						&& valorUnitarioProgramaServicioConvenio.doubleValue()>0)
					{
						valorPromocion.setPorcentajePromocion(((valorPromocion.getValorPromocion().multiply(new BigDecimal(100))).divide(valorUnitarioProgramaServicioConvenio,ESCALA_DIVISION, RoundingMode.HALF_EVEN)).doubleValue());
					}
				}
			}
			
			else 
			{	
				if(descuento.getPorcentajePromocion()>0)
				{
					if(valorUnitarioProgramaServicioConvenio.multiply( new BigDecimal(descuento.getPorcentajePromocion()/100)).doubleValue() < valorPromocion.getValorPromocion().doubleValue()
							|| valorPromocion.getValorPromocion().doubleValue()<=0)
					{
						asignadoDcto= true;
						valorPromocion.setSeleccionadoPorcentaje(descuento.isSeleccionadoPorcentaje());
						valorPromocion.setDetPromocion(descuento.getDetPromocion());
						valorPromocion.setValorPromocion(valorUnitarioProgramaServicioConvenio.multiply( new BigDecimal(descuento.getPorcentajePromocion()/100)));
						valorPromocion.setPorcentajePromocion(descuento.getPorcentajePromocion());
					}
				}
			}	
			
			//si fue asignado ese dcto entonces debemos hacer el calculo de los honorarios
			if(asignadoDcto)
			{
				calcularHonorarios(valorUnitarioProgramaServicioConvenio,valorPromocion, descuento);
			}
		}
		
		logger.info("el valor promocion es --->"+valorPromocion.getValorPromocion());
		return valorPromocion;
	}

	/**
	 * @param valorUnitarioProgramaServicioConvenio
	 * @param valorPromocion
	 * @param descuento
	 */
	private static void calcularHonorarios(
			BigDecimal valorUnitarioProgramaServicioConvenio,
			InfoPromocionPresupuestoServPrograma valorPromocion,
			InfoPromocionPresupuestoServPrograma descuento)
	{
		logger.info("\n\n\n\n44444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
		logger.info("descuento.getValorHonorario()--->"+descuento.getValorHonorario());
		
		if(descuento.getValorHonorario().doubleValue()>0)
		{	
			///en caso de que el valor honorario supere el valor unitario, entonces aplica el maximo
			if(valorPromocion.getValorHonorario().doubleValue()>valorUnitarioProgramaServicioConvenio.doubleValue())
			{
				valorPromocion.setValorHonorario(valorUnitarioProgramaServicioConvenio);
			}
			else
			{	
				valorPromocion.setValorHonorario(descuento.getValorHonorario());
			}
			logger.info("valorPromocion.getPorcentajeHonorarioCALCULADO--->"+(valorPromocion.getValorHonorario())+" * 100  / valorUnitarioProgramaServicioConvenio()->"+valorUnitarioProgramaServicioConvenio);
			if(valorPromocion.getValorHonorario().doubleValue()>0)
			{	
				valorPromocion.setPorcentajeHonorario(((valorPromocion.getValorHonorario().multiply(new BigDecimal(100))).divide(valorUnitarioProgramaServicioConvenio, ESCALA_DIVISION, RoundingMode.HALF_EVEN)).doubleValue());
			}
			logger.info("valorPromocion.getPorcentajeHonorarioCALCULADO--->"+(valorPromocion.getValorHonorario())+" * 100  / valorUnitarioProgramaServicioConvenio()->"+valorUnitarioProgramaServicioConvenio);
			logger.info("valorPromocion.getPorcentajeHonorarioCALCULADO--->"+valorPromocion.getPorcentajeHonorario());
			logger.info("valorPromocion.getValorHonorarioCALCULADO-->"+valorPromocion.getValorHonorario());
			
		}
		else
		{	
			if(descuento.getPorcentajeHonorario()>0)
			{
				valorPromocion.setValorHonorario(valorUnitarioProgramaServicioConvenio.multiply( new BigDecimal(descuento.getPorcentajeHonorario()/100)));
				valorPromocion.setPorcentajeHonorario(descuento.getPorcentajeHonorario());
				
				logger.info("valorPromocion.getPorcentajeHonorario--->"+valorPromocion.getPorcentajeHonorario());
				logger.info("valorPromocion.getValorHonorarioCALCULADO-->"+valorPromocion.getValorHonorario());
			}
		}	
		
		
	}
	
	/**
	 * 
	 * @param programaExcluyente
	 * @param servicioExcluyente
	 * @param convenio
	 * @param fechaCalculoVigencia
	 * @param vo
	 */
	private static void setearOtrosFiltrosPromociones(	final double programaExcluyente, final int servicioExcluyente,
														final int convenio, String fechaCalculoVigencia,
														HashMap<String, String> vo)
	{
		vo.put("fechabd", UtilidadFecha.conversionFormatoFechaABD(fechaCalculoVigencia));
		if(fechaCalculoVigencia.equals(UtilidadFecha.getFechaActual()))
		{	
			vo.put("hora", UtilidadFecha.getHoraActual());
		}	
		if(programaExcluyente>0)
			vo.put("programa", programaExcluyente+"");
		else if(servicioExcluyente>0)
			vo.put("servicio", servicioExcluyente+"");
		vo.put("convenio", convenio+"");
	}

	/**
	 * Metodo que Arma una lista de InfoTarifaServicioPresupuesto,  optiene:
	 * 1. Obtener el Descuento comercial
	 * 2. Obtener el metodo de ajuste
	 * 3. Obtener valor Tarifa Total Unitaria
	 * 4. Obtener estado factura =Cargada
	 * 
	 * Nota. Aplica para servicio o Programa
	 * 
	 * @param programaExcluyente
	 * @param servicioExcluyente
	 * @param convenio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param cuenta
	 * @param calcularCoberturaANivelServ 
	 * @return
	 */
	public static ArrayList<InfoTarifaServicioPresupuesto> obtenerTarifaUnitariaXPresupuestoOservicio(	final double programaExcluyente, 
																										final int servicioExcluyente, 
																										final int convenio, 
																										final int contrato, 
																										String fechaCalculoVigencia, 
																										final int institucion, 
																										final BigDecimal cuenta 
																										) throws IPSException 
																										
	{
		ArrayList<InfoTarifaServicioPresupuesto> arrayInfoTarifaPresupueto= new ArrayList<InfoTarifaServicioPresupuesto>();
		//1. primero determinamos si es un programa o un servicio
		if(programaExcluyente>0)
		{
			logger.info("######################################################################################################################");
			logger.info("###################### PROGRAMA entramos ha cargar los SERVICIOS DEL PROGRAMA -->"+programaExcluyente+"########################");
			ArrayList<DtoDetalleProgramas> arrayDetalleProgramas= ProgramasOdontologicos.cargarDetallePrograma(programaExcluyente);
			
			//iteramos uno a uno los servicios de programa para obtener las tarifas
			for(DtoDetalleProgramas dtoPrograma: arrayDetalleProgramas)
			{
				if(UtilidadTexto.getBoolean(dtoPrograma.getActivo()))
				{
					logger.info("iniciamos calculo de tarifa x servicio,  programa-->"+programaExcluyente+"  servicio-->"+dtoPrograma.getServicio());
					arrayInfoTarifaPresupueto.add( obtenerTarifaUnitariaXServicio( 	dtoPrograma.getServicio(), 
																							convenio, 
																							contrato, 
																							fechaCalculoVigencia, 
																							institucion, 
																							cuenta,
																							programaExcluyente>0,
																							ConstantesBD.codigoNuncaValido /*centroCostoSolicitante*/)); 
				}
			}
			logger.info("#########################################FIN PROGRAMA "+programaExcluyente+" ############################################");
			logger.info("######################################################################################################################");
		}
		else if(servicioExcluyente>0)
		{
			logger.info("######################################################################################################################");
			logger.info("######################entramos ha cargar el SERVICIOS -->"+servicioExcluyente+"########################");
			
			arrayInfoTarifaPresupueto.add(obtenerTarifaUnitariaXServicio( 	servicioExcluyente, 
																			convenio, 
																			contrato, 
																			fechaCalculoVigencia, 
																			institucion, 
																			cuenta,
																			programaExcluyente>0,
																			ConstantesBD.codigoNuncaValido /*centroCostoSolicitante*/)); 
			logger.info("#########################################FIN SERVICIO "+servicioExcluyente+" ############################################");
			logger.info("######################################################################################################################");
		}
		
		return arrayInfoTarifaPresupueto;
	}
	
	
	
	/**
	 * Metodo que Arma el Objeto InfoTarifaServicioPresupuesto 
	 * El Objeto InfoTarifa  sirve para:
	 * 1. Obtener el Descuento comercial
	 * 2. Obtener el metodo de ajuste
	 * 3. Obtener valor Tarifa Total Unitaria
	 * 4. Obtener estado factura =Cargada
	 *   
	 * @param servicio
	 * @param convenio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param cuenta
	 * @param calcularCoberturaANivelServ 
	 * @return
	 */
	public static InfoTarifaServicioPresupuesto obtenerTarifaUnitariaXServicio(	final int servicio, 
																					final int convenio, 
																					final int contrato, 
																					String fechaCalculoVigencia, 
																					final int institucion, 
																					final BigDecimal cuenta, 
																					boolean utilizaProgramas,
																					int centroCostoSolicitante) throws IPSException
	{
		logger.info("\n\n*****************INICIAMOS EL CALCULO DE LA TARIFA UNITARIA X SERVICIO -->"+servicio);
		
		InfoTarifaServicioPresupuesto infoTarifa= new InfoTarifaServicioPresupuesto();
		infoTarifa.setServicio(servicio);
		int codigoViaIngreso= ConstantesBD.codigoViaIngresoConsultaExterna;
		/*
		 * obtener codigo tarifario 
		 */
		String codigoTarifarioSer= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		if(codigoTarifarioSer.equals(""))
		{
			codigoTarifarioSer= ConstantesBD.codigoTarifarioCups+"";
		}
		/*
		 * nombre servicio
		 */
		String nombreServicio= Servicios.obtenerNombreServicio(servicio, Utilidades.convertirAEntero(codigoTarifarioSer));
		
		
		//1. Evaluamos la cobertura del convenio
		String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(cuenta.intValue()+"").getAcronimo();
		logger.info("tipoPaciente-->"+tipoPaciente);
		
		//si utiliza programas la cobertura se hace a nivel del programa........
		if(!utilizaProgramas)
		{	
			logger.info("//////////////	1. EVALUAMOS LA COBERTURA DEL CONVENIO...........................................");
			
			int codigoNaturalezaPaciente=0; 
			InfoCobertura infoCobertura=new InfoCobertura(); 
			if(!Contrato.manejaCobertura(contrato))
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(false);
				infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
				infoCobertura.setCantidad(ConstantesBD.codigoNuncaValido);
			}
			else
			{	
				infoCobertura=Cobertura.validacionCoberturaServicio(contrato, codigoViaIngreso, tipoPaciente, servicio, codigoNaturalezaPaciente, institucion);
			}	
				
			infoTarifa.setError(asignarErrorCobertura(infoCobertura, nombreServicio));
			if( !infoTarifa.getError().isEmpty())
			{
				return infoTarifa; 
			}
		}	
		
		//2. primero debemos obtener el esquema tarifario
		logger.info("//////////////	2. OBTENEMOS EL ESQUEMA TARIFARIO.............................................");
		InfoDatosInt esquemaTarifario= obtenerEsquemaTarifario(servicio, contrato, fechaCalculoVigencia);
		
		infoTarifa.setEsquemaTarifario(esquemaTarifario.getCodigo());
		
		infoTarifa.setError(asignarErrorEsquemaTarifario(esquemaTarifario.getCodigo(), nombreServicio));
		if( !infoTarifa.getError().isEmpty())
		{
			return infoTarifa; 
		}

		//3. obtenemos el tarifario oficial del esquema tarifario
		logger.info("//////////////	3. OBTENEMOS TARIFARIO OFICIAL DEL ESQUEMA TARIFARIO.............................................");
		int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(esquemaTarifario.getCodigo());
		logger.info("codigoTipoTarifario-->"+codigoTipoTarifario);
		
		//4. obtenemos la tarifa base del servicio
		logger.info("//////////////	4. OBTENEMOS LA TARIFA BASE DEL SERVICIO.............................................");
		InfoTarifaVigente infoTarifaVigente= Cargos.obtenerTarifaBaseServicio(codigoTipoTarifario, servicio, esquemaTarifario.getCodigo(), fechaCalculoVigencia);
		BigDecimal valorTarifaBase = new BigDecimal(infoTarifaVigente.getValorTarifa());
		infoTarifa.setError(asignarErrorTarifas(codigoTipoTarifario, servicio, valorTarifaBase, esquemaTarifario, nombreServicio, infoTarifaVigente.isExiste(), institucion));
		if(!infoTarifa.getError().isEmpty())
		{
			return infoTarifa; 
		}
		
		//5 obtenemos el tipo paciente de la cuenta y el metofo de ajuste
		String metodoAjusteEsquemaTarifario= Cargos.obtenerMetodoAjuste(convenio, esquemaTarifario.getCodigo(), institucion, true/*esServicio*/);
		infoTarifa.setMetodoAjuste(metodoAjusteEsquemaTarifario);
		logger.info("//////////////	5. METODO DE AJUSTE.............................................");
		logger.info("metodoAjusteEsquemaTarifario->"+metodoAjusteEsquemaTarifario);
		
		//6. Aplicamos las excepciones de tarifas
		logger.info("//////////////	6. APLICAMOS LAS EXCEPCIONES DE LAS TARIFAS.............................................");
		//segun Nilsa 2009-10-19 los convenios odontologicos no manejan complejidad, por esta razon se envia en -1
		//@FIXME TARIFAS CENTRO ATENCION
		
		CentroCostoMundo centroCostoMundo = new CentroCostoMundo();
		
		int codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
		
		CentrosCosto centroCosto = centroCostoMundo.findById(centroCostoSolicitante);
		
		if(centroCosto!=null){
			codigoCentroAtencion= centroCosto.getCentroAtencion().getConsecutivo();
		}
		
		BigDecimal valorTarifaTotalUnitaria= new BigDecimal(obtenerValorTarifaConExcepcion(valorTarifaBase.doubleValue(), valorTarifaBase.doubleValue(), codigoViaIngreso, tipoPaciente, ConstantesBD.codigoNuncaValido /*codigoTipoComplejidad*/, contrato, servicio, institucion,fechaCalculoVigencia, codigoCentroAtencion));
		logger.info("valor de la tarifa con excepcion--->"+valorTarifaTotalUnitaria.doubleValue());
		
		//7.verificamos si aplican valores de descuentos x promociones
		logger.info("//////////////7. LAS PROMOCIONES SOLAMENTE LAS PODEMOS CALCULAR CUANDO TENEMOS EL TOTAL X PROGRAMA O SERVICIO, ENTONCES ESTO ES LO ULTIMO QUE SE HACE.............");
		//LAS PROMOCIONES SOLAMENTE LAS PODEMOS CALCULAR CUANDO TENEMOS EL TOTAL X PROGRAMA O SERVICIO, ENTONCES ESTO ES LO ULTIMO QUE SE HACE
		
		///si utiliza programas los descuentos comerciales se hacen a nivel de programa
		//no podemos usar el atributo utilizaProgramas porque desde la asignacion lo envian en false.... 
		//y en este punto siempre debe tomar lo que ten ga param la insticuion 
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion)))
		{	
			//8. verificamos los decuentos comerciales x convenio, pero este calculo no afecta el valor total
			logger.info("//////////////	8. OBTENEMOS LOS DESCUENTOS COMERCIALES.............................................");
			infoTarifa.setValorDescuentoComercial(obtenerDescuentoComercial(servicio, contrato, fechaCalculoVigencia,	institucion, codigoViaIngreso, tipoPaciente,metodoAjusteEsquemaTarifario, valorTarifaTotalUnitaria));
		}	
		
		//9. recalculamos el valor del cargo con el ajuste 
		logger.info("//////////////	9. RECALCULAMOS EL VALOR CON EL AJUSTE.............................................");
		if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario)&&valorTarifaTotalUnitaria.doubleValue()>0)
		{	
			valorTarifaTotalUnitaria= new BigDecimal(UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotalUnitaria.doubleValue()));
		}
			
		infoTarifa.setValorTarifaUnitaria(valorTarifaTotalUnitaria);
		
		//10. verificamos los estados de facturacion con las inclusiones/exclusiones
		//POR CAMBIO EN DOCUMENTACION EL 2010-03-16 DE MARGARITA Y NILSA, SE DEFINIO QUE NO SE VAN HA MANEJAR INCLUSIONES EXCLUSIONES X CONVENIO
		//POR ESTA RAZON LO MAS FACIL ES COMENTARIAR ESTA CONSULTA Y QUEDA ABIERTO POR SI CAMBIAN DE OPINION
		//logger.info("//////////////	10. INCLUSIONES/EXCLUSIONES.............................................");
		//String inclusionesExclusiones= Cargos.obtenerInclusionExclusionXConvenioServicio(contrato, centroCostoSolicitante, servicio, institucion,fechaCalculoVigencia);
		int estado=ConstantesBD.codigoEstadoFCargada;
		/*if(inclusionesExclusiones.equals(ConstantesBD.acronimoSi))
		{	
			estado=ConstantesBD.codigoEstadoFExento;
		}*/
		
		infoTarifa.setEstadoFacturacion(estado);
		//logger.info("ESTADO facfturacionnnnnnnnnnnn--->"+estado);
		
		
		logger.info("TARIFA UNITARIA SIN DCTO COMERCIAL-->"+infoTarifa.getValorDescuentoComercial());
		logger.info("\n*****************FINNNN EL CALCULO DE LA TARIFA UNITARIA X SERVICIO -->"+servicio+"*********\n\n");
		return infoTarifa;
	}

	/**
	 * 
	 * @param infoCobertura
	 * @param nombreServicio 
	 * @param errores
	 * @return
	 */
	private static String asignarErrorCobertura(InfoCobertura infoCobertura, String nombreServicio)
	{
		if(!infoCobertura.existe() || !infoCobertura.getIncluido())
		{
			logger.info("NO EXISTE COBERTURAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			return "No existe cobertura para el servicio "+nombreServicio;
		}
		return "";
	}

	/**
	 * 
	 * @param servicio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param infoTarifa
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param metodoAjusteEsquemaTarifario
	 * @param valorTarifaTotal
	 * @return
	 */
	private static BigDecimal obtenerDescuentoComercial(	final int servicio,
																		final int contrato, 
																		String fechaCalculoVigencia,
																		final int institucion, 
																		int codigoViaIngreso, 
																		String tipoPaciente,
																		String metodoAjusteEsquemaTarifario, 
																		BigDecimal valorTarifaTotal) throws IPSException
	{
		InfoTarifa descuentoTarifa= Cargos.obtenerDescuentoComercialXConvenioServicio(codigoViaIngreso, tipoPaciente, contrato, servicio, institucion,fechaCalculoVigencia);
		BigDecimal valorDescuentoComercial= new BigDecimal(0);
		if(descuentoTarifa.getExiste())
		{
			if (descuentoTarifa.getPorcentajes().size()>0 && Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0)+"")>0)
			{
				 valorDescuentoComercial= new BigDecimal(valorTarifaTotal.doubleValue()* (Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0),true)/100));
			}
			if (Utilidades.convertirADouble(descuentoTarifa.getValor())>0)
			{
				valorDescuentoComercial= new BigDecimal(Utilidades.convertirADouble(descuentoTarifa.getValor(),true));
			}
			logger.info("VALOR DESCUENTO COMERCIAL sin ajuste--------------->"+valorDescuentoComercial+"<------------------------------------");
			
			if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario)&&valorDescuentoComercial.doubleValue()>0)
				valorDescuentoComercial= new BigDecimal(UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal.doubleValue()));
			logger.info("VALOR DESCUENTO COMERCIAL con ajuste--------------->"+valorDescuentoComercial+"<------------------------------------");
		}
		return valorDescuentoComercial;
	}
	
	/**
	 * Metodo para obtener el esquema tarifario ligado al presupuesto
	 * @param servicio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @return
	 */
	public static InfoDatosInt obtenerEsquemaTarifario(int servicio, int contrato, String fechaCalculoVigencia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerEsquemaTarifario(servicio, contrato, fechaCalculoVigencia);
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
	private static double obtenerValorTarifaConExcepcion(double valorTarifaTotal, double valorTarifaBase, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContratoDetalle, int codigoServicio, int codigoInstitucion,String fechaCalculoVigencia, int centroAtencion) throws IPSException 
	{
		Connection con=UtilidadBD.abrirConexion();
		//2.2.6.1 obtenemos el objeto InfoTarifa de la excepcion
		InfoTarifa excepcionTarifa= Cargos.obtenerExcepcionesTarifasServicio(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContratoDetalle, codigoServicio, codigoInstitucion,fechaCalculoVigencia, centroAtencion); 
		if(excepcionTarifa.getExiste())
		{	
			valorTarifaTotal=Cargos.asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
		}	
		logger.info("valor tarifa (despues excepciones)->"+valorTarifaTotal);
		UtilidadBD.closeConnection(con);
		return valorTarifaTotal;
	}
	
	
	/**
	 * metod que asigna los errores x esquema tarifario
	 * @param codigoEsquemaTarifario
	 * @param nombreServicio 
	 * @param erroresCargo
	 */
	private static String asignarErrorEsquemaTarifario(int codigoEsquemaTarifario, String nombreServicio)
	{
		if (codigoEsquemaTarifario<=0)
		{
		    logger.info("ERROR NO EXISTE EL ESQUEMA TARIFARIO!!!!!!!");
		    return "No existe esquema tarifario para el servicio "+nombreServicio;
		}
		return "";
	}
	
	/**
	 * metodo que asigna los errores de tarifas ISS - SOAT
	 * @param codigoTipoTarifario
	 * @param esquemaTarifario 
	 * @param nombreServicio 
	 * @param b 
	 * @param erroresCargo
	 * @param codigoServicio
	 */
	private static String asignarErrorTarifas(int codigoTipoTarifario, int servicio, BigDecimal valorTarifaBase, InfoDatosInt esquemaTarifario, String nombreServicio, boolean existeTarifa, int institucion) 
	{
		if(!existeTarifa)
		{
			
			if( ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion)!=null){
				
				if(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion).equals(ConstantesBD.acronimoSi))
				{
					return "Programa no tiene Tarifa";
					
				}else
				{
					
					if (codigoTipoTarifario==ConstantesBD.codigoTarifarioISS)
					{	
						logger.info("NO EXISTE TARIFA!!!!!!!!");
						return "El servicio "+nombreServicio+" no tiene tarifa para el esquema tarifario "+esquemaTarifario.getNombre();
					}	
					else if (codigoTipoTarifario==ConstantesBD.codigoTarifarioSoat)
					{	
						logger.info("NO EXISTE TARIFA!!!!!!!!");
						return "El servicio "+nombreServicio+" no tiene tarifa para el esquema tarifario "+esquemaTarifario.getNombre();
					}
					
				}
				
				
				
			}
			
			
			
			
		}	
		return "";
	}
	
	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	private static HashMap<String, String> obtenerFiltrosPromociones(BigDecimal cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerFiltrosPromociones(cuenta);
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public static int obtenerContrato(int ingreso, int convenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerContrato(ingreso, convenio);
	}
	
	
	
	/**
	 * 
	 * @param serialBono
	 * @param institucion
	 * @param valorTarifa
	 * @return
	 */
	public static InfoBonoDcto obtenerDescuentosBono(final int cuenta, final int institucion, BigDecimal valorTarifa, double programaExcluyente, int servicioExcluyente, final int convenio, String fechaApp)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerDescuentosBono(cuenta, institucion, valorTarifa, programaExcluyente, servicioExcluyente, convenio, fechaApp);
	}
	
	/* *
	 * 
	 * @param convenio
	 * @param ingreso
	 * @return
	 * /
	public static BigDecimal obtenerSerialBono(final int convenio, final int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerSerialBono(convenio, cuenta);
	}*/
	
	/**
	 * 
	 * @param convenio
	 * @param programa TODO
	 * @param cuenta
	 * @return
	 */
	public static boolean liberarSerialBono(Connection con, final int convenio, final int ingreso, BigDecimal serial, int programa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().liberarSerialBono(con, convenio, ingreso, serial, programa);
	}
	
	/**
	 * 
	 * @param centroAtencion
	 * @param fechaCalculoVigencia
	 * @param valorPresupuestoXConvenio
	 * @return
	 */
	public static InfoDctoOdontologicoPresupuesto obtenerDescuentoOdon(final int centroAtencion, final String fechaCalculoVigencia, BigDecimal valorPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerDescuentoOdon(centroAtencion, fechaCalculoVigencia, valorPresupuesto);
	}
	
	/**
	 * NO UTILIZAR ESTE METODO!!!!!! CAMBIO EL FLUJO
	 * Se toma cada uno de los servicios que se tomarán para asignar la cita
	 * @param servicios
	 * @param idCuenta
	 */
	public static void validacionControlAbonos(
			Connection con,
			final ArrayList<DtoServicioCitaOdontologica> servicios,
			final BigDecimal idCuenta,
			final boolean conPlanTratamiento,
			ActionErrors errores,
			final UsuarioBasico usuario,
			int codigoCita,
			int codigoPlanTratamiento) throws IPSException
	{
		//SE CARGAN LOS DATOS DE LA CUENTA
		logger.info("id de la cuenta "+idCuenta);
		DtoCuentas dtoCuenta = new DtoCuentas();
		if(idCuenta.intValue()!=ConstantesBD.codigoNuncaValido)
		{
			dtoCuenta = UtilidadesManejoPaciente.consultarDatosCuenta(con, idCuenta);
		}
		//objeto que maneja el total de contratos
		ArrayList<InfoDatosDouble> totalContratos = new ArrayList<InfoDatosDouble>();
		double totalTarifa = 0;
		boolean citaPrimeraVezTratamiento = false;
		
		logger.info("tiene plan de tratamiento? "+conPlanTratamiento);
		
		//SE ITERA CADA UNO DE LOS SERVICIOS QUE SE PIENSAN ASIGNAR A LA CITA
		for(DtoServicioCitaOdontologica servicio:servicios)
		{
			//si el servicio es nuevo se hace los calculos
			if(UtilidadTexto.getBoolean(servicio.getNuevoServicio()))
			{
				InfoResponsableCobertura responsableCobertura = new InfoResponsableCobertura();
				InfoTarifaServicioPresupuesto tarifaServicioPrespuesto = new InfoTarifaServicioPresupuesto();
				
				//********FLUJO CON PLAN DE TRATAMIENTO*******************************
				if(conPlanTratamiento)
				{
					
					logger.info("codigo detalle presupuesto del servicio: "+servicio.getCodigoPresuOdoProgSer());
					//Se verifica si el servicio tiene presupuesto para tomar la tarifa del presupuesto
					if(servicio.getCodigoPresuOdoProgSer()>0)
					{
						boolean utilizaProgramas=false; // Programas en ves de paquetes
						obtenerInfoPresupuestoContratadoProgSer(con,servicio.getCodigoPresuOdoProgSer(), responsableCobertura, tarifaServicioPrespuesto, utilizaProgramas, servicio.getServicio(), new BigDecimal(codigoPlanTratamiento));
						
						if(!tarifaServicioPrespuesto.getError().equals(""))
						{
							tarifaServicioPrespuesto.setError(tarifaServicioPrespuesto.getError()+" para el servicio "+servicio.getNombreServicio());
						}
						else
						{
							tarifaServicioPrespuesto.setServicio(servicio.getServicio());
						}
						
						
					}
					//De lo contrario se realiza el proceso normal de la cobertura
					else
					{
						//SE valida la cobertura del servicio
						logger.info("VAMOS VALIDAR LA COBERTURA DEL SERVICIO!!!");
						logger.info("idIngreso: "+dtoCuenta.getIdIngreso());
						logger.info("codigoViaIngreso: "+dtoCuenta.getCodigoViaIngreso());
						logger.info("codigoTipoPaciente: "+dtoCuenta.getCodigoTipoPaciente());
						logger.info("servicio: "+servicio.getServicio());
						responsableCobertura = Cobertura.validacionCoberturaServicio(con, dtoCuenta.getIdIngreso(), dtoCuenta.getCodigoViaIngreso(), dtoCuenta.getCodigoTipoPaciente(), servicio.getServicio(), usuario.getCodigoInstitucionInt(), false, "");
						
						//SE calcula la tarifa del servicio con el método de calculo tarifa odnotlogia
						tarifaServicioPrespuesto = obtenerTarifaUnitariaXServicio(servicio.getServicio(),responsableCobertura.getDtoSubCuenta().getConvenio().getCodigo(),responsableCobertura.getDtoSubCuenta().getContrato(),UtilidadFecha.getFechaActual(con),usuario.getCodigoInstitucionInt(),idCuenta, false, ConstantesBD.codigoNuncaValido /*centroCostoSolicitante*/);
						
						
					}
				}
				//***********FLUJO SIN PLAN DE TRATAMIENTO********************************
				else
				{
					//SE valida la cobertura del servicio
					logger.info("idIngreso: "+dtoCuenta.getIdIngreso()+", codigoViaIngreso: "+dtoCuenta.getCodigoViaIngreso()+", tipo paciente: "+dtoCuenta.getCodigoTipoPaciente()+", codigoServicio: "+servicio.getServicio());
					responsableCobertura = Cobertura.validacionCoberturaServicio(con, dtoCuenta.getIdIngreso(), dtoCuenta.getCodigoViaIngreso(), dtoCuenta.getCodigoTipoPaciente(), servicio.getServicio(), usuario.getCodigoInstitucionInt(), false, "");
					
					logger.info("responsable de la cobertura: contrato->"+responsableCobertura.getDtoSubCuenta().getContrato()+", convenio-> "+responsableCobertura.getDtoSubCuenta().getConvenio());
					//SE calcula la tarifa del servicio con el método de calculo tarifa odnotlogia
					tarifaServicioPrespuesto = obtenerTarifaUnitariaXServicio(servicio.getServicio(),responsableCobertura.getDtoSubCuenta().getConvenio().getCodigo(),responsableCobertura.getDtoSubCuenta().getContrato(),UtilidadFecha.getFechaActual(con),usuario.getCodigoInstitucionInt(),idCuenta, false, ConstantesBD.codigoNuncaValido /*centroCostoSolicitante*/);
					
				}
				//************************************************************************
				
				servicio.setInfoResponsableCobertura(responsableCobertura);
				servicio.setInfoTarifa(tarifaServicioPrespuesto);
				
				//Se verifica que no haya errores
				if(tarifaServicioPrespuesto.getError().equals(""))
				{
					
					if(
							//Si en la  cobertura se encontró un contrato entonces se hace control de abonos/anticipos
							responsableCobertura.getDtoSubCuenta().getContrato()>0	
					)
					{
						
						if(///Solo se hace control de abonos/anticipos la tarifa para servicios que no sean de garantia y que n estén excentos
								!UtilidadTexto.getBoolean(servicio.getGarantiaServicio())&&servicio.getInfoTarifa().getEstadoFacturacion()!=ConstantesBD.codigoEstadoFExento)
						{
							aplicacionAbonosAnticipos(con,responsableCobertura.getDtoSubCuenta().getContrato(),servicio,errores,dtoCuenta,usuario,codigoCita);
						}
						
						//************Se totaliza el total de la tarifa x contrato************************
						boolean encontrado = false;
						int posContrato = 0;
						for(InfoDatosDouble contrato:totalContratos)
						{
							if(contrato.getNombre().equals(responsableCobertura.getDtoSubCuenta().getContrato()+""))
							{
								encontrado = true;
								
							}
							else
							{
								posContrato++;
							}
						}
						if(encontrado)
						{
							totalContratos.get(posContrato).setCodigo(totalContratos.get(posContrato).getCodigo()+(servicio.getInfoTarifa().getValorTarifaUnitaria().doubleValue()-servicio.getInfoTarifa().getValorDescuentoComercial().doubleValue()));
						}
						else
						{
							InfoDatosDouble contrato = new InfoDatosDouble((servicio.getInfoTarifa().getValorTarifaUnitaria().doubleValue()-servicio.getInfoTarifa().getValorDescuentoComercial().doubleValue()),responsableCobertura.getDtoSubCuenta().getContrato()+"");
							totalContratos.add(contrato);
						}
						//*******************************************************************************************
					}
					else
					{
						errores.add("", new ActionMessage("errors.problemasGenericos","realizando la validación de cobertura del servicio"));
					}
					
					//Solo se suma la tarifa para servicios que no sean de garantia y que n estén excentos
					if(!UtilidadTexto.getBoolean(servicio.getGarantiaServicio())&&servicio.getInfoTarifa().getEstadoFacturacion()!=ConstantesBD.codigoEstadoFExento)
					{
						totalTarifa += (servicio.getInfoTarifa().getValorTarifaUnitaria().doubleValue()-servicio.getInfoTarifa().getValorDescuentoComercial().doubleValue());
					}
				}
				else
				{
					logger.info("PASÓ POR AQUÍ EN EL CALCULO DE LA TARIFA DEL SERVICIO  "+servicio.getNombreServicio());
					errores.add("", new ActionMessage("errors.notEspecific",tarifaServicioPrespuesto.getError()));
				}
			}
			else if(UtilidadTexto.getBoolean(servicio.getEliminarSer()))
			{
/*	NO SE DEBE ELIMINAR EL MOVIMIENTO SINO ANULAR		
 * 	ResultadoBoolean resultado = AbonosYDescuentos.insertarMovimientoAbonos(con, new BigDecimal(servicio.getCodigoPk()), ConstantesBD.tipoMovimientoAbonoEntradaReservaAbono);
				if(!resultado.isTrue())
				{
					errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				}*/
			}
			
		}//Fin for
		
		
		//******SE VERIFICA SI LA CITA ASOCIADA AL PLAN DE TRATAMIENTO ES LA PRIMERA**************************
		if(conPlanTratamiento)
		{
			citaPrimeraVezTratamiento = esPrimeraCitaPlanTratamiento(con, codigoPlanTratamiento);
		}
		//*****************************************************************************************************
		
		//************ANÁLISIS DE LOS CONTRATOS ENCONTRADOS************************************
		for(InfoDatosDouble contrato:totalContratos)
		{
			Contrato mundoContrato = new Contrato();
			mundoContrato.cargar(con, contrato.getNombre());
			
			if(UtilidadTexto.getBoolean(mundoContrato.getControlaAnticipos())&&UtilidadTexto.getBoolean(mundoContrato.getDtoControlAnticipo().getRequiereAnticipo()))
			{
				//Si el total de la tarifa del servicio del contrato supero el valor maximo x paciente permitido se genera error
				if(mundoContrato.getDtoControlAnticipo().getNumeroMaximoPaciente()<contrato.getCodigo())
				{
					errores.add("",new ActionMessage("errors.notEspecific","El valor máximo por paciente ($"+UtilidadTexto.formatearValores(mundoContrato.getDtoControlAnticipo().getNumeroMaximoPaciente())+") del contrato N° "+mundoContrato.getNumeroContrato()+" del convenio "+Utilidades.obtenerNombreConvenioOriginal(con, mundoContrato.getCodigoConvenio())+" no cubre el total de los servicios de la cita ($"+UtilidadTexto.formatearValores(contrato.getCodigo())+")"));
				}
				
				//Si es la primera cita del plan de tratamiento se actualiza el campo de numero de pacientes atendidos
				if(citaPrimeraVezTratamiento)
				{
					ResultadoBoolean resultado = actualizarNumeroPacientesAtendidosContrato(con, Utilidades.convertirAEntero(contrato.getNombre()));
					if(!resultado.isTrue())
					{
						errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
					}
				}
			}
		}
		//***************************************************************************************
		
	}

	/**
	 * NO UTILIZAR ESTE MÉTODO, ES DEL FLUJO VIEJO
	 * Método implementado para realizar el calculo del detalle de la aplicacion de abonos o anticipos del servicios
	 * dependiendo de la informacion del contrato
	 * @param con
	 * @param contrato
	 * @param servicio
	 * @param errores
	 * @param dtoCuenta
	 * @param usuario
	 * @param codigoCita
	 */
	private static void aplicacionAbonosAnticipos(Connection con, int contrato,DtoServicioCitaOdontologica servicio, ActionErrors errores, DtoCuentas dtoCuenta, UsuarioBasico usuario, int codigoCita) throws IPSException 
	{
		//Se carga el contrato
		Contrato mundoContrato = new Contrato();
		mundoContrato.cargar(con, contrato+"");
		double totalServicio = servicio.getInfoTarifa().getValorTarifaUnitaria().doubleValue() - servicio.getInfoTarifa().getValorDescuentoComercial().doubleValue();
		
		//********¿PACIENTE PAGA ATENCION?********************************************************************
		if(UtilidadTexto.getBoolean(mundoContrato.getPacientePagaAtencion()))
		{
			//*************¿VALIDA ABONO PARA ATENCION ODONTOLOGICA?*********************************************
			if(UtilidadTexto.getBoolean(mundoContrato.getValidarAbonoAtencionOdo()))
			{
				
				double abonoDisponiblePaciente = AbonosYDescuentos.consultarAbonosDisponibles(con,Utilidades.convertirAEntero(dtoCuenta.getCodigoPaciente()), usuario.getCodigoInstitucionInt());
				
				//*********¿cubre el abono el total del servicio?***************************************************
				if(abonoDisponiblePaciente>=totalServicio)
				{
//					AbonosYDescuentos.insertarMovimientoAbonos(con, Utilidades.convertirAEntero(dtoCuenta.getCodigoPaciente()), servicio.getCodigoPk(), ConstantesBD.tipoMovimientoAbonoCitaOdontologica, totalServicio, usuario.getCodigoInstitucionInt());
					servicio.setAplicaAbono(ConstantesBD.acronimoSi);
				}
				else
				{
					errores.add("", new ActionMessage("errors.notEspecific","El paciente no cuenta con abono disponible ($"+UtilidadTexto.formatearValores(abonoDisponiblePaciente)+") para cubrir la tarifa del servicio "+servicio.getNombreServicio()+" ($"+UtilidadTexto.formatearValores(totalServicio)+")"));
				}
			}
		}
		//******************¿REQUIERE ANTICIPO PARA CONTRATAR PACIENTE?***************************************
		else if(UtilidadTexto.getBoolean(mundoContrato.getControlaAnticipos())&&UtilidadTexto.getBoolean(mundoContrato.getDtoControlAnticipo().getRequiereAnticipo()))
		{
			if(mundoContrato.getDtoControlAnticipo().getValorAnticipoDisponible()<totalServicio)
			{
				errores.add("", new ActionMessage("errors.notEspecific","El convenio ("+Utilidades.obtenerNombreConvenioOriginal(con,mundoContrato.getCodigoConvenio())+") no cuenta con valor anticipo disponible ($"+UtilidadTexto.formatearValores(mundoContrato.getDtoControlAnticipo().getValorAnticipoDisponible())+") para cubrir la tarifa del servicio "+servicio.getNombreServicio()+" ($"+UtilidadTexto.formatearValores(totalServicio)+")"));
			}
			else
			{
				servicio.setAplicaAnticipo(ConstantesBD.acronimoSi);
			}
				
		}
		
	}
	
	/**
	 * Método para cargar la informacion de presupuesto de un servicio especifico
	 * @param con
	 * @param codigoPresuOdoProgSer
	 * @param infoRespCobertura
	 * @param infoTarifa
	 * @param utilizaProgramasInstitucion
	 * @param servicio
	 */
	public static void obtenerInfoPresupuestoContratadoProgSer(Connection con,int codigoPresuOdoProgSer,InfoResponsableCobertura infoRespCobertura,InfoTarifaServicioPresupuesto infoTarifa, boolean utilizaProgramasInstitucion, int servicio, BigDecimal codigoPkPlanTratamiento)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerInfoPresupuestoContratadoProgSer(con, codigoPresuOdoProgSer, infoRespCobertura, infoTarifa, utilizaProgramasInstitucion, servicio, codigoPkPlanTratamiento);
	}
	
	/**
	 * Método para saber si la cita procesda es la priemra cita del plan de tratamietno
	 * @param con
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static boolean esPrimeraCitaPlanTratamiento(Connection con,int codigoPlanTratamiento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().esPrimeraCitaPlanTratamiento(con, codigoPlanTratamiento);
	}
	
	/**
	 * Método para actualizar el número de pacientes atendidos por contrato
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static ResultadoBoolean actualizarNumeroPacientesAtendidosContrato(Connection con,int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().actualizarNumeroPacientesAtendidosContrato(con, codigoContrato);
	}
	
	
	/**
	 * METODO QUE OBTIENE EL DESCUENTO COMERCIAL X CONVENIO PROGRAMA, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoPrograma
	 * @param codigoInstitucion
	 * @return
	 * @throws BDException 
	 */
	public static InfoTarifa obtenerDescuentoComercialXConvenioPrograma(int codigoViaIngreso, String tipoPaciente, int codigoContrato, Double codigoPrograma, int codigoInstitucion,String fechaCalculoVigencia ) throws BDException
	{
		Connection con=UtilidadBD.abrirConexion();
		InfoTarifa info= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerDescuentoComercialXConvenioPrograma(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoPrograma, codigoInstitucion,fechaCalculoVigencia);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * 
	 * @param servicio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @param institucion
	 * @param infoTarifa
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param metodoAjusteEsquemaTarifario
	 * @param valorTarifaTotal
	 * @return
	 * @throws BDException 
	 */
	private static InfoPorcentajeValor obtenerDescuentoComercialPrograma(	final Double programa,
																	final int contrato, 
																	String fechaCalculoVigencia,
																	final int institucion, 
																	int codigoViaIngreso, String tipoPaciente,
																	BigDecimal valorTarifaTotal) throws BDException
	{
		InfoPorcentajeValor porcentajeValor= new InfoPorcentajeValor();
		InfoTarifa descuentoTarifa= obtenerDescuentoComercialXConvenioPrograma(codigoViaIngreso, tipoPaciente, contrato, programa, institucion,fechaCalculoVigencia);
		if(descuentoTarifa.getExiste())
		{
			if (descuentoTarifa.getPorcentajes().size()>0 && Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0)+"")>0)
			{
				 porcentajeValor.setValor(new BigDecimal(valorTarifaTotal.doubleValue()* (Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0),true)/100)));
				 porcentajeValor.setPorcentaje(Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0)));
			}
			if (Utilidades.convertirADouble(descuentoTarifa.getValor())>0)
			{
				porcentajeValor.setValor(new BigDecimal(Utilidades.convertirADouble(descuentoTarifa.getValor(),true)));
				porcentajeValor.setPorcentaje( porcentajeValor.getValor().multiply(new BigDecimal(100)).divide(valorTarifaTotal, ESCALA_DIVISION, RoundingMode.HALF_EVEN).doubleValue());
			}
			logger.info("VALOR DESCUENTO COMERCIAL sin ajuste--------------->"+porcentajeValor.getValor()+"<------------------------------------");
			//EL AJUSTE SE DEBE REALIZAR A NIVEL DE CADA SERVICIO DEL PROGRAMA
		}
		return porcentajeValor;
	}
	
	/**
	 * Retorna el código PK de la tabla presupuesto_odo_prog_serv
	 * @param con Conexión con la BD
	 * @param presupuesto Presupuesto asociado
	 * @param programa Programa buscado
	 * @return entero con el codigo_pk buscado, -1 de lo contrario
	 */
	public static int obtenerPresupuestoOdoProgSer(Connection con, int presupuesto, DtoProgramaServicio programa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosOdonDao().obtenerPresupuestoOdoProgSer(con, presupuesto, programa);
	}

}
