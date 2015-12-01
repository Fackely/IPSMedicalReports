package com.servinte.axioma.mundo.fabrica.facturacion;

import com.servinte.axioma.dao.impl.administracion.CentroCostosDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IViewFinalidadesServDAO;
import com.servinte.axioma.mundo.impl.administracion.CentroCostoMundo;
import com.servinte.axioma.mundo.impl.facturacion.BusquedaMontosCobroMundo;
import com.servinte.axioma.mundo.impl.facturacion.ControlAnticiposContratoMundo;
import com.servinte.axioma.mundo.impl.facturacion.DetalleMontoGeneralMundo;
import com.servinte.axioma.mundo.impl.facturacion.DetalleMontoMundo;
import com.servinte.axioma.mundo.impl.facturacion.EmpresasInstitucionMundo;
import com.servinte.axioma.mundo.impl.facturacion.EntidadesSubcontratadasMundo;
import com.servinte.axioma.mundo.impl.facturacion.EntregaMedicamentosInsumosEntSubcontratadasMundo;
import com.servinte.axioma.mundo.impl.facturacion.EsquemasTarifariosMundo;
import com.servinte.axioma.mundo.impl.facturacion.EstanciaViaIngCentroCostoMundo;
import com.servinte.axioma.mundo.impl.facturacion.FinalidadesServicioMundo;
import com.servinte.axioma.mundo.impl.facturacion.GruposServiciosMundo;
import com.servinte.axioma.mundo.impl.facturacion.HistoDetalleMontoGeneralMundo;
import com.servinte.axioma.mundo.impl.facturacion.HistoDetalleMontoMundo;
import com.servinte.axioma.mundo.impl.facturacion.HistoMontoAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.impl.facturacion.HistoMontosCobroMundo;
import com.servinte.axioma.mundo.impl.facturacion.HistoricoEncabezadoMundo;
import com.servinte.axioma.mundo.impl.facturacion.IBusquedaMontosCobroMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogProfSaludNoHonorarioMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogRipsEntSubInconsisArchMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogRipsEntSubInconsisCampMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogRipsEntSubRegValorMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogRipsEntidadesSubArchivoMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogRipsEntidadesSubRegistrMundo;
import com.servinte.axioma.mundo.impl.facturacion.LogRipsEntidadesSubcontratadasMundo;
import com.servinte.axioma.mundo.impl.facturacion.MontoAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.impl.facturacion.MontoAgrupacionServiciosMundo;
import com.servinte.axioma.mundo.impl.facturacion.MontoArticuloEspecificoMundo;
import com.servinte.axioma.mundo.impl.facturacion.MontoServicioEspecificoMundo;
import com.servinte.axioma.mundo.impl.facturacion.MontosCobroMundo;
import com.servinte.axioma.mundo.impl.facturacion.ProgramasMundo;
import com.servinte.axioma.mundo.impl.facturacion.ServiciosMundo;
import com.servinte.axioma.mundo.impl.facturacion.TercerosMundo;
import com.servinte.axioma.mundo.impl.facturacion.TiposContratoMundo;
import com.servinte.axioma.mundo.impl.facturacion.TiposMontoMundo;
import com.servinte.axioma.mundo.impl.facturacion.TiposServicioMundo;
import com.servinte.axioma.mundo.impl.facturacion.ValorizacionServiciosArticulosMundo;
import com.servinte.axioma.mundo.impl.facturacion.ViewFinalidadesServMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.AutorizacionConvIngPacMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ContratoMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ConveniosIngresoPacienteMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ConveniosMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ValidacionTipoCobroPacienteMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ValidacionesBdConvIngPacMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosIngresoPacienteMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoGeneralMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEmpresasInstitucionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEntidadesSubcontratadasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEsquemasTarifariosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEstanciaViaIngCentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IFinalidadesServicioMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IGruposServiciosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoGeneralMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontoAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontosCobroMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogProfSaludNoHonorarioMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisArchMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisCampMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubRegValorMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubArchivoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubRegistrMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubcontratadasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionServiciosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoArticuloEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoServicioEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontosCobroMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IProgramasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITercerosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposServicioMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IValidacionTipoCobroPacienteMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IValorizacionServiciosArticulosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IViewFinalidadesServMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.convenio.IAutorizacionConvIngPac;
import com.servinte.axioma.mundo.interfaz.facturacion.convenio.IValidacionesBdConvIngPacMundo;

/**
 * 
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo de facturas varias
 * 
 * @author Juan David Ramírez
 * @since 11 Septiembre 2010
 */
public abstract class FacturacionFabricaMundo
{
	/**
	 * 
	 * Este Método se encarga de de crear una instancia para 
	 * la entidad IValidacionTipoCobroPacienteMundo
	 * 
	 * @return IValidacionTipoCobroPacienteMundo
	 *
	 */
	public static IValidacionTipoCobroPacienteMundo crearValidacionTipoCobroPacienteMundo()
	{
		return new ValidacionTipoCobroPacienteMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de de crear una instancia para 
	 * la entidad IBusquedaMontosCobroMundo
	 * 
	 * @return IBusquedaMontosCobroMundo
	 *
	 */
	public static IBusquedaMontosCobroMundo crearBusquedaMontosCobroMundo()
	{
		return new BusquedaMontosCobroMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroMundo
	 * 
	 * @return IMontosCobroMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IMontosCobroMundo crearMontosCobroMundo(){
		return new MontosCobroMundo();
	}
	
	public static IEmpresasInstitucionMundo crearEmpresasInstitucionMundo(){
		return new EmpresasInstitucionMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IDetalleMontoMundo
	 * 
	 * @return IDetalleMontoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetalleMontoMundo crearDetalleMontoMundo(){
		return new DetalleMontoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposMontoMundo
	 * 
	 * @return ITiposMontoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposMontoMundo crearTiposMontoMundo(){
		return new TiposMontoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IDetalleMontoGeneralMundo
	 * 
	 * @return IDetalleMontoGeneralMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetalleMontoGeneralMundo crearDetalleMontoGeneralMundo(){
		return new DetalleMontoGeneralMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroDAO
	 * 
	 * @return IDetalleMontoGeneralDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoDetalleMontoMundo crearHistoDetalleMontoMundo(){
		return new HistoDetalleMontoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoDetalleMontoGeneralMundo
	 * 
	 * @return IHistoDetalleMontoGeneralMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoDetalleMontoGeneralMundo crearHistoDetalleMontoGeneralMundo(){
		return new HistoDetalleMontoGeneralMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoMontosCobroMundo
	 * 
	 * @return IHistoMontosCobroMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoMontosCobroMundo crearHistoMontosCobroMundo(){
		return new HistoMontosCobroMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITercerosMundo
	 * 
	 * @return TercerosMundo
	 * @author, Diana Carolina G
	 *
	 */
	
	public static ITercerosMundo crearTercerosMundo(){
		return new TercerosMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoAgrupacionServiciosMundo
	 * 
	 * @return IMontoAgrupacionServiciosMundo
	 * @author, Angela Aguirre
	 *
	 */
	
	public static IMontoAgrupacionServiciosMundo crearMontoAgrupacionServiciosMundo(){
		return new MontoAgrupacionServiciosMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoServicioEspecificoMundo
	 * 
	 * @return IMontoServicioEspecificoMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoServicioEspecificoMundo crearMontoServicioEspecificoMundo(){
		return new MontoServicioEspecificoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IGruposServiciosMundo
	 * 
	 * @return IGruposServiciosMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IGruposServiciosMundo crearGruposServiciosMundo(){
		return new GruposServiciosMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposServicioMundo
	 * 
	 * @return ITiposServicioMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static ITiposServicioMundo crearTipoServicioMundo(){
		return new TiposServicioMundo();
	}
		
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoArticuloEspecificoMundo
	 * 
	 * @return IMontoArticuloEspecificoMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoArticuloEspecificoMundo crearMontoArticuloEspecificoMundo(){
		return new MontoArticuloEspecificoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoAgrupacionArticuloMundo
	 * 
	 * @return IMontoAgrupacionArticuloMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoAgrupacionArticuloMundo crearMontoAgrupacionArticuloMundo(){
		return new MontoAgrupacionArticuloMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoMontoAgrupacionArticuloMundo
	 * 
	 * @return IHistoMontoAgrupacionArticuloMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IHistoMontoAgrupacionArticuloMundo crearHistoMontoAgrupacionArticuloMundo(){
		return new HistoMontoAgrupacionArticuloMundo();
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IConveniosIngresoPacienteMundo
	 * 
	 * @return IConveniosIngresoPacienteMundo
	 *
	 */
	public static IConveniosIngresoPacienteMundo crearconConveniosIngresoPacienteMundo(){
		return new ConveniosIngresoPacienteMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IConvenios
	 * 
	 * @return IConvenios
	 *
	 */
	public static IConveniosMundo crearcConveniosMundo(){
		return new ConveniosMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IContratoMundo
	 * 
	 * @return IContratoMundo
	 *
	 */
	public static IContratoMundo crearContratoMundo(){
		return new ContratoMundo();
	}
	
	
	/**
	 * 
	 */
	private static  IValidacionesBdConvIngPacMundo iValidacinBdConvIngPag;
	
	/**
	 * Este metodo crea la instancia de Validaciones Bd Convenio Ing pac MUndo
	 * @return
	 */
	public static final IValidacionesBdConvIngPacMundo crearValidacionBdConvIngPacMundo(){
		
		if(iValidacinBdConvIngPag==null)
		{
			return new  ValidacionesBdConvIngPacMundo();
		}
		
		return iValidacinBdConvIngPag;
	}
	
	
	/**
	 * 
	 */
	private static IAutorizacionConvIngPac iAutorizacion;
	
	/**
	 * Este metodo retorna la autorizacion m 
	 * @return
	 */
	public static final IAutorizacionConvIngPac crearAutorizacionMundo(){
		
		if(iAutorizacion==null)
		{
			return new AutorizacionConvIngPacMundo();
		}
		
		return iAutorizacion;
	} 
	
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEntregaMedicamentosInsumosEntSubcontratadasMundo
	 * 
	 * @return IEntregaMedicamentosInsumosEntSubcontratadasMundo
	 * @author Cristhian Murillo
	 */
	public static IEntregaMedicamentosInsumosEntSubcontratadasMundo crearEntregaMedicamentosInsumosEntSubcontratadas(){
		return new EntregaMedicamentosInsumosEntSubcontratadasMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposContratoMundo
	 * 
	 * @return ITiposContratoMundo
	 */
	public static ITiposContratoMundo crearTiposContratoMundo(){
		return new TiposContratoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEsquemasTarifariosMundo
	 * 
	 * @return EsquemasTarifariosMundo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 *
	 */
	public static IEsquemasTarifariosMundo crearEsquemasTarifariosMundo(){
		return new EsquemasTarifariosMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProgramasMundo
	 * 
	 * @return ProgramasMundo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 *
	 */
	public static IProgramasMundo crearProgramasMundo(){
		return new ProgramasMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IHistoricoEncabezadoMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IHistoricoEncabezadoMundo}.
	 */
	public static IHistoricoEncabezadoMundo crearHistoricoEncabezadoMundo(){
		
		return new HistoricoEncabezadoMundo();
	}
	
		
	/**
	 * Retorna una instancia de IEstanciaViaIngCentroCostoMundo
	 * @return IEstanciaViaIngCentroCostoMundo
	 */
	public static IEstanciaViaIngCentroCostoMundo crearEstanciaViaIngCentroCostoMundo(){
		return new EstanciaViaIngCentroCostoMundo();
		
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEntidadesSubcontratadasMundo
	 * 
	 * @return IEntidadesSubcontratadasMundo
	 * @author Cristhian Murillo
	 */
	public static IEntidadesSubcontratadasMundo crearEntidadesSubcontratadasMundo(){
		return new EntidadesSubcontratadasMundo();
	}
	
	
	/**
	 * Este Método devuelve un objeto que implementa la interfaz
	 * IControlAnticiposContratoMundo
	 * 
	 * @return IControlAnticiposContratoMundo
	 */
	public static IControlAnticiposContratoMundo crearControlAnticiposContratoMundo(){
		return new ControlAnticiposContratoMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogProfSaludNoHonorarioMundo
	 * 
	 * @return LogProfSaludNoHLogProfSaludNoHonorarioMundoonorarioMundo
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	public static ILogProfSaludNoHonorarioMundo crearLogProfSaludNoHonorarioMundo(){
		return new LogProfSaludNoHonorarioMundo();
	}

	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IServiciosMundo
	 * 
	 * @return ServiciosMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static IServiciosMundo crearServiciosMundo(){
		return new ServiciosMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntidadesSubcontratadasMundo
	 * 
	 * @return LogRipsEntidadesSubcontratadasMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubcontratadasMundo crearLogRipsEntidadesSubMundo(){
		return new LogRipsEntidadesSubcontratadasMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntSubInconsisArchMundo
	 * 
	 * @return LogRipsEntSubInconsisArchMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubInconsisArchMundo crearLogRipsEntSubInconsisArchMundo(){
		return new LogRipsEntSubInconsisArchMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntidadesSubArchivoMundo
	 * 
	 * @return LogRipsEntidadesSubArchivoMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubArchivoMundo crearLogRipsEntidadesSubArchivoMundo(){
		return new LogRipsEntidadesSubArchivoMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntidadesSubRegistrMundo
	 * 
	 * @return LogRipsEntidadesSubRegistrMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubRegistrMundo crearLogRipsEntidadesSubRegistrMundo(){
		return new LogRipsEntidadesSubRegistrMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntSubInconsisCampMundo
	 * 
	 * @return LogRipsEntSubInconsisCampMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubInconsisCampMundo crearLogRipsEntSubInconsisCampMundo(){
		return new LogRipsEntSubInconsisCampMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IFinalidadesServicioMundo
	 * 
	 * @return FinalidadesServicioMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static IFinalidadesServicioMundo crearFinalidadesServicioMundo(){
		return new FinalidadesServicioMundo();
	}
		
	/**
	 * M&eacute;todo encargado de crear una instancia para
	 * la entidad IViewFinalidadesServMundo
	 * @return ViewFinalidadesServMundo
	 * @author Diana Carolina G
	 */
	public static IViewFinalidadesServMundo crearViewFinalidadesServMundo(){
		return new ViewFinalidadesServMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogRipsEntSubRegValorMundo
	 * 
	 * @return LogRipsEntSubRegValorMundo
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubRegValorMundo crearLogRipsEntSubRegValorMundo(){
		return new LogRipsEntSubRegValorMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IValorizacionServiciosArticulosMundo
	 * 
	 * @return ValorizacionServiciosArticulosMundo
	 * 
	 * @author Ricardo Ruiz
	 */
	public static IValorizacionServiciosArticulosMundo crearValorizacionServiciosArticulosMundo(){
		return new ValorizacionServiciosArticulosMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ICentroCostoMundo
	 * 
	 * @return ICentroCostosDAO
	 * 
	 * @author Diana Ruiz 
	 */	
	
	public static ICentroCostoMundo crearCentroCostoMundo() {
		return new CentroCostoMundo();
	}
	
}
