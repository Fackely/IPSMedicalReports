package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.odontologia.InfoDetalleHistoricoIncExcPresupuesto;
import util.odontologia.InfoHistoricoInclusionesExclusionesPresupuesto;
import util.odontologia.InfoPresupuestoExclusionesInclusiones;
import util.odontologia.InfoSeccionInclusionExclusion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoExclusionPresupuesto;
import com.princetonsa.dto.odontologia.DtoInclusionesPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;

/**
 * 
 * @author axioma
 *
 */
public class PresupuestoExclusionesInclusiones
{
	/**
	 * metodo que carga las inclusiones exclusiones de un presupuesto contratado
	 * @param ingreso
	 * @param codigoPersona 
	 * @return
	 */
	public static InfoPresupuestoExclusionesInclusiones cargar(BigDecimal ingreso, boolean utilizaProgramas, int codigoPersona)
	{
		InfoPresupuestoExclusionesInclusiones info= new InfoPresupuestoExclusionesInclusiones();
		
		/////primero obtenemos el codigo del presupuesto odontologico
		DtoPresupuestoOdontologico dtoPresupuesto= PresupuestoOdontologico.cargarPresupuestoContratado(ingreso);
		
		if(dtoPresupuesto==null)
		{
			return null;
		}
		
		info.setValorPresupuesto(PresupuestoOdontologico.obtenerValorContratadoInicialPresupuesto(dtoPresupuesto.getCodigoPK().longValue()));
		
		info.setCodigoPresupuesto(dtoPresupuesto.getCodigoPK());
		info.setCodigoPlanTratamiento(dtoPresupuesto.getPlanTratamiento());
		info.setConsecutivoPresupuesto(dtoPresupuesto.getConsecutivo());
		info.setFecha(dtoPresupuesto.getUsuarioModifica().getFechaModifica());
		
//		//////segundo cargamos las inclusiones del plan de tratamiento
		info.setSeccionInclusiones(cargarInclusionesPlanTratamiento(dtoPresupuesto.getPlanTratamiento(), ConstantesIntegridadDominio.acronimoEstadoPendiente, ConstantesIntegridadDominio.acronimoAutorizado, utilizaProgramas));
//		/////tercero cargamos las exclusiones del presupuesto
		info.setSeccionExclusiones(cargarExclusionesPresupuesto(dtoPresupuesto.getCodigoPK(), ConstantesIntegridadDominio.acronimoExcluido, ConstantesIntegridadDominio.acronimoAutorizado, utilizaProgramas));
		
		IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		
		ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios = PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(dtoPresupuesto.getCodigoPK());
		
		info.setListaConveniosContratos(presupuestoOdontologicoServicio.cargarConveniosContratoPresupuesto(listaSumatoriaConvenios, codigoPersona));
		
		return info;
	}
	
	/**
	 * Método que se encarga de determinar cual es el mensaje a mostrar 
	 * según sea el caso para las Inclusiones del paciente
	 * 
	 * @param codigoPlanTratamiento
	 * @param codigoPkPresupuesto
	 * @param existeInclusionesExclusiones
	 * @param utilizaProgramas
	 * @return
	 */
	public static String cargarMensajesInclusion (BigDecimal codigoPlanTratamiento, BigDecimal codigoPkPresupuesto,	boolean existeInclusionesExclusiones, boolean utilizaProgramas) 
	{
		MessageResources mensages=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
		String mensajeInclusiones = "";

		if(codigoPlanTratamiento.doubleValue()>0)
		{
			//1. verificamos si existen inclusiones no autorizadas:
			InfoSeccionInclusionExclusion info= cargarInclusionesPlanTratamiento(codigoPlanTratamiento, ConstantesIntegridadDominio.acronimoNoAautorizado, "", utilizaProgramas);
			if(info.getListaBoca().size()>0 || info.getListaPiezasDentalesSuperficies().size()>0)
			{
				mensajeInclusiones = mensages.getMessage("InclusionesExclusionesForm.inclusionesNoAutorizadas");
			}
			
			info= cargarInclusionesPlanTratamiento(codigoPlanTratamiento, ConstantesIntegridadDominio.acronimoPorAutorizar, "", utilizaProgramas);
			if(info.getListaBoca().size()>0 || info.getListaPiezasDentalesSuperficies().size()>0)
			{
				mensajeInclusiones = mensages.getMessage("InclusionesExclusionesForm.inclusionesPorAutorizar");
			}
			
			if(mensajeInclusiones.isEmpty())
			{
				mensajeInclusiones = mensages.getMessage("InclusionesExclusionesForm.noInclusiones");
			}
		}
		
		return mensajeInclusiones;
	}
	
	
	/**
	 * 
	 * Método que se encarga de determinar cual es el mensaje a mostrar 
	 * según sea el caso para las Exclusiones del paciente
	 * 
	 * @param codigoPlanTratamiento
	 * @param codigoPkPresupuesto
	 * @param existeInclusionesExclusiones
	 * @param utilizaProgramas
	 * @return
	 */
	public static String cargarMensajesExclusion(BigDecimal codigoPlanTratamiento, BigDecimal codigoPkPresupuesto,	boolean existeInclusionesExclusiones, boolean utilizaProgramas) 
	{
		MessageResources mensages=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
		String mensajeExclusiones = "";
		
		if(codigoPlanTratamiento.doubleValue()>0)
		{
			InfoSeccionInclusionExclusion info= cargarExclusionesPresupuesto(codigoPkPresupuesto, ConstantesIntegridadDominio.acronimoNoAautorizado, "", utilizaProgramas);
			if(info.getListaBoca().size()>0 || info.getListaPiezasDentalesSuperficies().size()>0)
			{
				mensajeExclusiones = mensages.getMessage("InclusionesExclusionesForm.exclusionesNoAutorizadas");
			}
			
			info= cargarExclusionesPresupuesto(codigoPkPresupuesto, ConstantesIntegridadDominio.acronimoPorAutorizar, "", utilizaProgramas);
			if(info.getListaBoca().size()>0 || info.getListaPiezasDentalesSuperficies().size()>0)
			{
				mensajeExclusiones = mensages.getMessage("InclusionesExclusionesForm.exclusionesPorAutorizar");
			}
			
			if(mensajeExclusiones.isEmpty())
			{
				mensajeExclusiones = mensages.getMessage("InclusionesExclusionesForm.noExclusiones");
			}
		}
		
		return mensajeExclusiones;
	}
	
	
	/**
	 * 
	 * @param planTratamiento
	 * @param bigDecimal 
	 * @param estadoProgramasServicios
	 * @param acronimoAutorizado
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoSeccionInclusionExclusion cargarInclusionesPlanTratamiento(	BigDecimal planTratamiento, 
																					String estadoProgramasServiciosPlanTratamiento,
																					String estadoAutorizacion, 
																					boolean utilizaProgramas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().cargarInclusionesPlanTratamiento(planTratamiento,estadoProgramasServiciosPlanTratamiento,estadoAutorizacion,utilizaProgramas);
	}

	/**
	 * 
	 * @param codigoPK
	 * @param acronimoExcluido
	 * @param acronimoAutorizado
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoSeccionInclusionExclusion cargarExclusionesPresupuesto( 	BigDecimal codigoPresupuesto, 
																				String estadoProgramasServiciosPlanTratamiento,
																				String estadoAutorizacion, 
																				boolean utilizaProgramas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().cargarExclusionesPresupuesto(codigoPresupuesto, estadoProgramasServiciosPlanTratamiento, estadoAutorizacion, utilizaProgramas);
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double guardarExclusion(Connection con, DtoExclusionPresupuesto dto  )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().guardarExclusion(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double guardarInclusion(Connection con, DtoInclusionesPresupuesto dto  )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().guardarInclusion(con, dto);
	}

	/**
	 * 
	 * @param bigDecimal
	 * @return
	 */
	public static InfoHistoricoInclusionesExclusionesPresupuesto cargarHistorico(BigDecimal ingreso, boolean utilizaProgramas, int institucion)
	{
		InfoHistoricoInclusionesExclusionesPresupuesto info= new InfoHistoricoInclusionesExclusionesPresupuesto();
		DtoPresupuestoOdontologico dtoPresupuesto= PresupuestoOdontologico.cargarPresupuestoContratado(ingreso);
		info.setCodigoPresupuesto(dtoPresupuesto.getCodigoPK());
		info.setConsecutivoPresupuesto(dtoPresupuesto.getConsecutivo()+"");
		info.setEstadoActual(dtoPresupuesto.getEstado());
		info.setListaExclusiones(cargarHistoricoExclusiones(dtoPresupuesto.getCodigoPK(), institucion));
		info.setListaInclusiones(cargarHistoricoInclusiones(dtoPresupuesto.getCodigoPK(), institucion));
		return info;
	}
	
	/**
	 * 
	 * @param bigDecimal
	 * @return
	 */
	public static InfoHistoricoInclusionesExclusionesPresupuesto cargarHistoricoDadoCodigoPk(BigDecimal codigoPkPresupuesto, boolean utilizaProgramas, int institucion)
	{
		InfoHistoricoInclusionesExclusionesPresupuesto info= new InfoHistoricoInclusionesExclusionesPresupuesto();
		ArrayList<DtoPresupuestoOdontologico> array= PresupuestoOdontologico.cargarPresupuesto(new DtoPresupuestoOdontologico(codigoPkPresupuesto));
		if(array.size()>0)
		{
			DtoPresupuestoOdontologico dtoPresupuesto= array.get(0);
			info.setCodigoPresupuesto(dtoPresupuesto.getCodigoPK());
			info.setConsecutivoPresupuesto(dtoPresupuesto.getConsecutivo()+"");
			info.setEstadoActual(dtoPresupuesto.getEstado());
			info.setListaExclusiones(cargarHistoricoExclusiones(dtoPresupuesto.getCodigoPK(), institucion));
			info.setListaInclusiones(cargarHistoricoInclusiones(dtoPresupuesto.getCodigoPK(), institucion));
		}	
		return info;
	}

	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public static boolean existenInclusionesExclusionesPresupuesto(BigDecimal presupuesto, int institucion)
	{
		return (cargarHistoricoExclusiones(presupuesto, institucion).size()>0 || cargarHistoricoInclusiones(presupuesto, institucion).size()>0);
	}
	
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoInclusiones(BigDecimal codigoPresupuesto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().cargarHistoricoInclusiones(codigoPresupuesto, institucion);
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoExclusiones(BigDecimal codigoPresupuesto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().cargarHistoricoExclusiones(codigoPresupuesto, institucion);
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @param codigoProgramaServicio
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal obtenerTarifaProgramaServicioPresupuesto(	BigDecimal codigoPresupuesto, Double codigoProgramaServicio,boolean utilizaProgramas) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoExclusionesInclusionesDao().obtenerTarifaProgramaServicioPresupuesto(codigoPresupuesto, codigoProgramaServicio, utilizaProgramas);
	}
	
}
