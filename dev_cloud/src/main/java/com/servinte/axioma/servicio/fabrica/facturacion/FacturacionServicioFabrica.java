package com.servinte.axioma.servicio.fabrica.facturacion;

import com.servinte.axioma.servicio.impl.facturacion.BusquedaMontosCobroServicio;
import com.servinte.axioma.servicio.impl.facturacion.CalculoValorCobrarPaciente;
import com.servinte.axioma.servicio.impl.facturacion.DetalleMontoGeneralServicio;
import com.servinte.axioma.servicio.impl.facturacion.DetalleMontoServicio;
import com.servinte.axioma.servicio.impl.facturacion.EmpresasInstitucionServicio;
import com.servinte.axioma.servicio.impl.facturacion.EntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.impl.facturacion.EntregaMedicamentosInsumosEntSubcontratadasServicio;
import com.servinte.axioma.servicio.impl.facturacion.EsquemasTarifariosServicio;
import com.servinte.axioma.servicio.impl.facturacion.EstanciaViaIngCentroCostoServicio;
import com.servinte.axioma.servicio.impl.facturacion.FinalidadesServicioServicio;
import com.servinte.axioma.servicio.impl.facturacion.GruposServiciosServicio;
import com.servinte.axioma.servicio.impl.facturacion.HistoDetalleMontoServicio;
import com.servinte.axioma.servicio.impl.facturacion.HistoMontoAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.impl.facturacion.HistoMontosCobroServicio;
import com.servinte.axioma.servicio.impl.facturacion.LogRipsEntSubInconsisArchServicio;
import com.servinte.axioma.servicio.impl.facturacion.LogRipsEntSubInconsisCampServicio;
import com.servinte.axioma.servicio.impl.facturacion.LogRipsEntSubRegValorServicio;
import com.servinte.axioma.servicio.impl.facturacion.LogRipsEntidadesSubArchivoServicio;
import com.servinte.axioma.servicio.impl.facturacion.LogRipsEntidadesSubRegistrServicio;
import com.servinte.axioma.servicio.impl.facturacion.LogRipsEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.impl.facturacion.MontoAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.impl.facturacion.MontoAgrupacionServiciosServicio;
import com.servinte.axioma.servicio.impl.facturacion.MontoArticuloEspecificoServicio;
import com.servinte.axioma.servicio.impl.facturacion.MontoServicioEspecificoServicio;
import com.servinte.axioma.servicio.impl.facturacion.MontosCobroServicio;
import com.servinte.axioma.servicio.impl.facturacion.ServiciosServicio;
import com.servinte.axioma.servicio.impl.facturacion.TercerosServicio;
import com.servinte.axioma.servicio.impl.facturacion.TiposContratoServicio;
import com.servinte.axioma.servicio.impl.facturacion.TiposMontoServicio;
import com.servinte.axioma.servicio.impl.facturacion.TiposServicioServicio;
import com.servinte.axioma.servicio.impl.facturacion.ValidacionTipoCobroPacienteServicio;
import com.servinte.axioma.servicio.impl.facturacion.ViewFinalidadesServServicio;
import com.servinte.axioma.servicio.impl.facturacion.convenio.ContratoServicio;
import com.servinte.axioma.servicio.impl.facturacion.convenio.ConvenioServicio;
import com.servinte.axioma.servicio.impl.facturacion.convenio.ConveniosIngresoPacienteServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IBusquedaMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ICalculoValorCobrarPaciente;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoGeneralServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEmpresasInstitucionServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEsquemasTarifariosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEstanciaViaIngCentroCostoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IFinalidadesServicioServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IGruposServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoDetalleMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoMontoAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubInconsisArchServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubInconsisCampServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubRegValorServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubArchivoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubRegistrServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoAgrupacionServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoArticuloEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoServicioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITercerosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposContratoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposServicioServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IViewFinalidadesServServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IContratoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConveniosIngresoPacienteServicio;

/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo de facturación
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class FacturacionServicioFabrica {
	
	/**
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	private FacturacionServicioFabrica(){
		
	}
	
	public static IValidacionTipoCobroPacienteServicio crearValidacionTipoCobroPacienteServicio()
	{
		return new ValidacionTipoCobroPacienteServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IBusquedaMontosCobroServicio
	 * 
	 * @return IMontosCobroServicio
	 * @author, Armando Osorio
	 *
	 */
	public static IBusquedaMontosCobroServicio crearBusquedaMontosCobroServicio()
	{
		return new BusquedaMontosCobroServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroDAO
	 * 
	 * @return IMontosCobroServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IMontosCobroServicio crearMontosCobroServicio(){
		return new MontosCobroServicio();
	}
	
	public static IEmpresasInstitucionServicio crearEmpresasInstitucionServicio(){
		return new EmpresasInstitucionServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IDetalleMontoServicio
	 * 
	 * @return IDetalleMontoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetalleMontoServicio crearDetalleMontoServicio(){
		return new DetalleMontoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IConvenioServicio
	 * 
	 * @return IConvenioServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IConvenioServicio crearConvenioServicio(){
		return new ConvenioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposMontoServicio
	 * 
	 * @return ITiposMontoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposMontoServicio crearTiposMontoServicio(){
		return new TiposMontoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IDetalleMontoGeneralServicio
	 * 
	 * @return IDetalleMontoGeneralServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetalleMontoGeneralServicio crearDetalleMontoGeneralServicio(){
		return new DetalleMontoGeneralServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoDetalleMontoServicio
	 * 
	 * @return IHistoDetalleMontoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoDetalleMontoServicio crearHistoDetalleMontoServicio(){
		return new HistoDetalleMontoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoMontosCobroServicio
	 * 
	 * @return IHistoMontosCobroServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoMontosCobroServicio crearHistoMontosCobroServicio(){
		return new HistoMontosCobroServicio();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITercerosServicio
	 * 
	 * @return TercerosServicio
	 * @author, Diana Carolina G
	 *
	 */
	
	public static ITercerosServicio crearTercerosServicio(){
		return new TercerosServicio();
		
	}
	
	/** 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoAgrupacionServiciosServicio
	 * 
	 * @return IMontoAgrupacionServiciosServicio
	 * @author, Angela Aguirre
	 *
	 */
	
	public static IMontoAgrupacionServiciosServicio crearMontoAgrupacionServicio(){
		return new MontoAgrupacionServiciosServicio();
	}	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoServicioEspecificoServicio
	 * 
	 * @return IMontoServicioEspecificoServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoServicioEspecificoServicio crearMontoServicioEspecificoServicio(){
		return new MontoServicioEspecificoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IGruposServiciosServicio
	 * 
	 * @return IGruposServiciosServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IGruposServiciosServicio crearGruposServiciosServicio(){
		return new GruposServiciosServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposServicioServicio
	 * 
	 * @return ITiposServicioServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static ITiposServicioServicio crearTipoServicioServicio(){
		return new TiposServicioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoArticuloEspecificoServicio
	 * 
	 * @return IMontoArticuloEspecificoServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoArticuloEspecificoServicio crearMontoArticuloEspecificoServicio(){
		return new MontoArticuloEspecificoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoAgrupacionArticuloServicio
	 * 
	 * @return IMontoAgrupacionArticuloServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoAgrupacionArticuloServicio crearMontoAgrupacionArticuloServicio(){
		return new MontoAgrupacionArticuloServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoMontoAgrupacionArticuloServicio
	 * 
	 * @return IHistoMontoAgrupacionArticuloServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IHistoMontoAgrupacionArticuloServicio crearHistoMontoAgrupacionArticuloServicio(){
		return new HistoMontoAgrupacionArticuloServicio();
	}
	
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IContratoServicio
	 * 
	 * @return IContratoServicio
	 * @author, Cristhian Murillo
	 */
	public static IContratoServicio crearContratoServicio(){
		return new ContratoServicio();
	}
	
	public static ICalculoValorCobrarPaciente crearCalculoValorCobrarPaciente(){
		return new CalculoValorCobrarPaciente();
	}
	
	
	/**
	 * Crear convenio ingreso paciente servicio
	 * @return
	 */
	public static IConveniosIngresoPacienteServicio crearConvenioIngresoPaciente(){
		return new ConveniosIngresoPacienteServicio();
		
	}
	
	
	/**
	 * Crear IEntregaMedicamentosInsumosEntSubcontratadasServicio
	 * @return IEntregaMedicamentosInsumosEntSubcontratadasServicio
	 */
	public static IEntregaMedicamentosInsumosEntSubcontratadasServicio crearEntregaMedicamentosInsumosEntSubcontratadasServicio(){
		return new EntregaMedicamentosInsumosEntSubcontratadasServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposContratoServicio
	 * 
	 * @return ITiposContratoServicio
	 */
	public static ITiposContratoServicio crearTiposContratoServicio(){
		return new TiposContratoServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEsquemasTarifariosServicio
	 * 
	 * @return IEsquemasTarifariosServicio
	 */
	public static IEsquemasTarifariosServicio crearEsquemasTarifariosServicio(){
		return new EsquemasTarifariosServicio();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEstanciaViaIngCentroCostoServicio
	 * 
	 * @return IEstanciaViaIngCentroCostoServicio
	 */
	public static IEstanciaViaIngCentroCostoServicio crearEstanciaViaIngCentroCosto(){
		return new EstanciaViaIngCentroCostoServicio();
		
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEntidadesSubcontratadasServicio
	 * 
	 * @return IEntidadesSubcontratadasServicio
	 */
	public static IEntidadesSubcontratadasServicio crearEntidadesSubcontratadasServicio(){
		return new EntidadesSubcontratadasServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IServiciosServicio
	 * 
	 * @return ServiciosServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static IServiciosServicio crearServiciosServicio(){
		return new ServiciosServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntidadesSubcontratadasServicio
	 * 
	 * @return LogRipsEntidadesSubcontratadasServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubcontratadasServicio crearLogRipsEntidadesSubServicio(){
		return new LogRipsEntidadesSubcontratadasServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntSubInconsisArchServicio
	 * 
	 * @return LogRipsEntSubInconsisArchServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubInconsisArchServicio crearLogRipsEntSubInconsisArchServicio(){
		return new LogRipsEntSubInconsisArchServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntidadesSubArchivoServicio
	 * 
	 * @return LogRipsEntidadesSubArchivoServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubArchivoServicio crearLogRipsEntidadesSubArchivoServicio(){
		return new LogRipsEntidadesSubArchivoServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntidadesSubRegistrServicio
	 * 
	 * @return LogRipsEntidadesSubRegistrServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubRegistrServicio crearLogRipsEntidadesSubRegistrServicio(){
		return new LogRipsEntidadesSubRegistrServicio();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntSubInconsisCampServicio
	 * 
	 * @return LogRipsEntSubInconsisCampServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubInconsisCampServicio crearLogRipsEntSubInconsisCampServicio(){
		return new LogRipsEntSubInconsisCampServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IFinalidadesServicioServicio
	 * 
	 * @return FinalidadesServicioServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static IFinalidadesServicioServicio crearFinalidadesServicioServicio(){
		return new FinalidadesServicioServicio();
	}
	
	
	/**
	 * M&eacute;todo encargado de crear una instancia para la 
	 * entidad ViewFinalidadesServServicio
	 * @return ViewFinalidadesServServicio
	 * @author Diana Carolina G
	 */
	public static IViewFinalidadesServServicio crearViewFinalidadesServServicio(){
		return new  ViewFinalidadesServServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntSubRegValorServicio
	 * 
	 * @return LogRipsEntSubRegValorServicio
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubRegValorServicio crearLogRipsEntSubRegValorServicio(){
		return new LogRipsEntSubRegValorServicio();
	}
	
	
}

