package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.odontologia.InfoConvenioContratoPresupuesto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author axioma
 *
 */
public class ValidacionesPresupuesto
{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(ValidacionesPresupuesto.class);
	
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public static boolean existeConvenioEnIngreso(int ingreso, int convenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().existeConvenioEnIngreso(ingreso, convenio);
	}
	
	/**
	 * 
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static boolean existeCancelacionContratoXIncumplimiento(BigDecimal codigoPlanTratamiento, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().existeCancelacionContratoXIncumplimiento(codigoPlanTratamiento, ingreso);
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public static boolean existenPresupuestosDadosEstados(int ingreso, ArrayList<String> estados)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().existenPresupuestosDadosEstados(ingreso, estados);
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public static boolean esPlanTratamientoPorConfirmar(BigDecimal planTratamiento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().esPlanTratamientoPorConfirmar(planTratamiento);
	}	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @return
	 */
	public static boolean tienePlanTratamientoEstados(BigDecimal planTratamiento, ArrayList<String> estados)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().tienePlanTratamientoEstados(planTratamiento, estados);
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @param utilizaProgramas
	 * @return
	 */
	public static boolean tieneServiciosOProgramasPlanTratamientoEstadosCorrectos(BigDecimal planTratamiento, ArrayList<String> estados, boolean utilizaProgramas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().tieneServiciosOProgramasPlanTratamientoEstadosCorrectos(planTratamiento, estados, utilizaProgramas);
	}
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static boolean puedoContratarXTipoAtencionConvenio(int convenio) throws IPSException
	{
		if(Convenio.obtenerTipoAtencion(convenio).equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral))
		{
			logger.info("EL CONVENIO ES DE 'ATENCION MEDICA GENERAL' X LO TANTO LO PUEDO CONTRATAR");
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	
	public static boolean poseeAbonosDisponibles(int codigoPaciente, Integer ingreso, int institucion)
	{
		boolean retorna = false;
		if(AbonosYDescuentos.consultarAbonosDisponibles(codigoPaciente, ingreso, institucion)>0)
		{
			logger.info("El paciente tiene saldo disponible, por esta razón puede contratar.");
			retorna = true; 
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @param valorPresupuesto
	 * @param contrato
	 * @return
	 */
	public static boolean esValorPresupuestoMayorAnticipo(BigDecimal valorPresupuesto , int contrato)
	{
		double valorAnticipoDisponible=0;
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoControlAnticiposContrato> array= Contrato.obtenerControlAnticipos(con,contrato);
		UtilidadBD.closeConnection(con);
		if(array.size()>0)
		{
			valorAnticipoDisponible= array.get(0).getValorAnticipoDisponible();
		}
		
		logger.info("valor presupuesto="+valorPresupuesto+"  valorAntDisponible-->"+valorAnticipoDisponible);
		
		return valorPresupuesto.doubleValue() > valorAnticipoDisponible;
	}
	
	/**
	 * 
	 * @param contrato
	 * @param valorTotalConvenio
	 * @return
	 */
	public static boolean esValorTotalConvenioMayorValorMaximoXPaciente( int contrato , BigDecimal valorTotalConvenio)
	{ 
		double valorMaximoPaciente=0;
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoControlAnticiposContrato> array= Contrato.obtenerControlAnticipos(con, contrato);
		UtilidadBD.closeConnection(con);
		if(array.size()>0)
		{
			valorMaximoPaciente= array.get(0).getNumeroMaximoPaciente();
		}
		if(valorMaximoPaciente==ConstantesBD.codigoNuncaValidoDoubleNegativo)
		{
			return	false; // no Aplica
		}
		
		return valorTotalConvenio.doubleValue() > valorMaximoPaciente;
	}
	
	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public static ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratosPacientePresupuesto(int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().cargarConveniosContratosPacientePresupuesto(ingreso);
	}	
	
	/**
	 * 
	 * @param centroAtencion
	 * @param fechaCalculoVigencia
	 * @param valorPresupuesto
	 * @return
	 */
	public static boolean existeDescuentoOdontologico(int centroAtencion, String fechaCalculoVigencia, BigDecimal valorPresupuesto)
	{
		///PRIMERO VERIFICAMOS LOS QUE SON DE TIPO PRESUPUESTO
		boolean existeDcto= CargosOdon.obtenerDescuentoOdon(centroAtencion, fechaCalculoVigencia, valorPresupuesto).getValorDctoCALCULADO().doubleValue()>0;
		if(existeDcto)
			return existeDcto;
		///SEGUNDO VERIFICAMOS LOS QUE SON DE TIPO ATENCION
		DtoDescuentoOdontologicoAtencion dtoAten= new DtoDescuentoOdontologicoAtencion();
		dtoAten.setCentroAtencion(new InfoDatosInt(centroAtencion));
		existeDcto= DescuentoOdontologico.cargarAtencion(dtoAten).size()>0;
		return existeDcto;
	}
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal existeProgramaServicioPresupuestoEnPlanTratamiento(BigDecimal codigoPkPresupuestoPieza, String estadoOPCIONAL, boolean utilizaProgramas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().existeProgramaServicioPresupuestoEnPlanTratamiento(codigoPkPresupuestoPieza, estadoOPCIONAL, utilizaProgramas);
	}

	/**
	 * 
	 * @param codigoPkDetalle
	 * @param codigoPkProgramaServicio
	 * @param servicio
	 * @return
	 */
	public static BigDecimal obtenerCodigoPkProgramaServicioPlanTratamiento(	BigDecimal codigoPkDetallePlan, 
																				BigDecimal codigoPkPrograma,
																				int servicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().obtenerCodigoPkProgramaServicioPlanTratamiento(codigoPkDetallePlan, codigoPkPrograma, servicio);
	}
	
	/**
	 * 
	 * @param pieza
	 * @param superficie
	 * @param hallazgo
	 * @param seccion
	 * @param presupuesto
	 * @param utilizaProgramas
	 * @param programaOServicio
	 * @return
	 */
	public static BigDecimal esProgramaServicioAdicionPlanTtoPresupuesto(	BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo,
																	String seccion, BigDecimal presupuesto, boolean utilizaProgramas,
																	BigDecimal programaOServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().esProgramaServicioAdicionPlanTtoPresupuesto(pieza, superficie, hallazgo, seccion, presupuesto, utilizaProgramas, programaOServicio);
	}
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal existeProgramaServicioAdicionPlanTtoPresupuesto(BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo, String seccion, BigDecimal presupuesto, boolean utilizaProgramas, BigDecimal programaOServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().existeProgramaServicioAdicionPlanTtoPresupuesto(pieza, superficie, hallazgo, seccion, presupuesto, utilizaProgramas, programaOServicio);
	}

	/**
	 * Verifica si el paciente tiene tarjeta activa
	 * @param codigoPersona código de la persona a verificar
	 * @return true en caso de tener tarjeta activa, false de lo contrario
	 */
	public static boolean pacienteTienetarjetaActiva(int codigoPersona) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesPresupuestoDao().pacienteTienetarjetaActiva(codigoPersona);
	}
}
