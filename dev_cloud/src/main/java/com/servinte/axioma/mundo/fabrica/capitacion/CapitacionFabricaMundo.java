package com.servinte.axioma.mundo.fabrica.capitacion;

import com.servinte.axioma.mundo.impl.capitacion.AutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.impl.capitacion.AutorizacionesCapitacionPeticionesMundo;
import com.servinte.axioma.mundo.impl.capitacion.CapitadoInconsistenciaMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempClaseInvArtMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempGrupoServicioMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempNaturArtMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempNivelAteGruServMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempNivelAteNatArtMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempNivelAtenArtMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempNivelAtenServMundo;
import com.servinte.axioma.mundo.impl.capitacion.CierreTempServArtMundo;
import com.servinte.axioma.mundo.impl.capitacion.ContratoCargueMundo;
import com.servinte.axioma.mundo.impl.capitacion.ConvUsuariosCapitadosMundo;
import com.servinte.axioma.mundo.impl.capitacion.DetalleValorizacionArticuloMundo;
import com.servinte.axioma.mundo.impl.capitacion.DetalleValorizacionServicioMundo;
import com.servinte.axioma.mundo.impl.capitacion.HistoricoCapitacionSubcontratadaMundo;
import com.servinte.axioma.mundo.impl.capitacion.HistoricoIngEstanciaSubcontratadaMundo;
import com.servinte.axioma.mundo.impl.capitacion.InconsistenSubirPacienteMundo;
import com.servinte.axioma.mundo.impl.capitacion.InconsistenciaPersonaMundo;
import com.servinte.axioma.mundo.impl.capitacion.InconsistenciasCamposMundo;
import com.servinte.axioma.mundo.impl.capitacion.LogDetalleParametrizacionPresupuestoMundo;
import com.servinte.axioma.mundo.impl.capitacion.LogParametrizacionPresupuestoMundo;
import com.servinte.axioma.mundo.impl.capitacion.LogSubirPacientesMundo;
import com.servinte.axioma.mundo.impl.capitacion.MotivosModificacionPresupuestoMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAtencionMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorServMedicMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionAgrupacionServicioMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionArticuloEspecificoMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionOcupacionMedicaMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionServicioArticuloMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionServicioEspecificoMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionUsuarioEspecificoMundo;
import com.servinte.axioma.mundo.impl.capitacion.NivelAutorizacionUsuarioMundo;
import com.servinte.axioma.mundo.impl.capitacion.OrdenCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.ParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.PrioridadOcupacionMedicaMundo;
import com.servinte.axioma.mundo.impl.capitacion.PrioridadUsuarioEspecificoMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoAutorizacionesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoCierrePresupuestoMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoFacturacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoNivelAutorizacionManualMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoNivelAutorizacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.ProcesoOrdenesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.UnidadPagoMundo;
import com.servinte.axioma.mundo.impl.capitacion.UsuarioXConvenioMundo;
import com.servinte.axioma.mundo.impl.capitacion.UsuariosCapitadosMundo;
import com.servinte.axioma.mundo.impl.capitacion.ValidacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.impl.capitacion.ValorizacionPresupuestoCapGeneralMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionesCapitacionPeticionesMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICapitadoInconsistenciaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempGrupoServicioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNaturArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteGruServMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteNatArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenServMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempServArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IContratoCargueMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IConvUsuariosCapitadosMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoCapitacionSubcontratadaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoIngEstanciaSubcontratadaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenSubirPacienteMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciaPersonaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciasCamposMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogSubirPacientesMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorServMedicMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionServicioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionArticuloEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionOcupacionMedicaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionServicioArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionServicioEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionUsuarioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IPrioridadOcupacionMedicaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IPrioridadUsuarioEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoAutorizacionesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCierrePresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoFacturacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoNivelAutorizacionManualMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoNivelAutorizacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoOrdenesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUnidadPagoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValidacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo;


/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo de capitación
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class CapitacionFabricaMundo {
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionMundo
	 * 
	 * @return INivelAutorizacionMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionMundo crearNivelAutorizacionMundo(){
		return new NivelAutorizacionMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionUsuarioMundo
	 * 
	 * @return INivelAutorizacionUsuarioMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionUsuarioMundo crearNivelAutorizacionUsuarioMundo(){
		return new NivelAutorizacionUsuarioMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionUsuarioEspecificoMundo
	 * 
	 * @return INivelAutorizacionUsuarioEspecificoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionUsuarioEspecificoMundo crearNivelAutorizacionUsuarioEspecificoMundo(){
		return new NivelAutorizacionUsuarioEspecificoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionOcupacionMedicaMundo
	 * 
	 * @return INivelAutorizacionOcupacionMedicaMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionOcupacionMedicaMundo crearNivelAutorizacionOcupacionMedicaMundo(){
		return new NivelAutorizacionOcupacionMedicaMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPrioridadOcupacionMedicaMundo
	 * 
	 * @return IPrioridadOcupacionMedicaMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IPrioridadOcupacionMedicaMundo crearPrioridadOcupacionMedicaMundo(){
		return new PrioridadOcupacionMedicaMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPrioridadUsuarioEspecificoMundo
	 * 
	 * @return IPrioridadUsuarioEspecificoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IPrioridadUsuarioEspecificoMundo crearPrioridadUsuarioEspecificoMundo(){
		return new PrioridadUsuarioEspecificoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicioArticuloMundo
	 * 
	 * @return INivelAutorizacionServicioArticuloMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicioArticuloMundo crearNivelAutorizacionServicioArticuloMundo(){
		return new NivelAutorizacionServicioArticuloMundo();
	}
	
	 /** 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionAgrupacionServicioMundo
	 * 
	 * @return INivelAutorizacionAgrupacionServicioMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionAgrupacionServicioMundo crearNivelAutorizacionAgrupacionServicioMundo(){
		return new NivelAutorizacionAgrupacionServicioMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionArticuloEspecificoMundo
	 * 
	 * @return INivelAutorizacionArticuloEspecificoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionArticuloEspecificoMundo crearNivelAutorizacionArticuloEspecificoMundo(){
		return new NivelAutorizacionArticuloEspecificoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicioEspecificoMundo
	 * 
	 * @return INivelAutorizacionServicioEspecificoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicioEspecificoMundo crearNivelAutorizacionServicioEspecificoMundo(){
		return new NivelAutorizacionServicioEspecificoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionAgrupacionArticuloMundo
	 * 
	 * @return INivelAutorizacionAgrupacionArticuloMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionAgrupacionArticuloMundo crearNivelAutorizacionAgrupacionArticuloMundo(){
		return new NivelAutorizacionAgrupacionArticuloMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IUsuariosCapitadosMundo
	 * 
	 * @return IUsuariosCapitadosMundo
	 * @author Cristhian Murillo
	 *
	 */
	public static IUsuariosCapitadosMundo crearUsuariosCapitadosMundo(){
		return new UsuariosCapitadosMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoricoCapitacionSubcontratadaMundo
	 * 
	 * @return IHistoricoCapitacionSubcontratadaMundo
	 * @author Camilo Gomez
	 *
	 */
	public static IHistoricoCapitacionSubcontratadaMundo crearHistoricoCapitacionSubMundo(){
		return new HistoricoCapitacionSubcontratadaMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IUsuarioXConvenioMundo
	 * 
	 * @return IUsuarioXConvenioMundo
	 * @author Cristhian Murillo
	 *
	 */
	public static IUsuarioXConvenioMundo crearUsuarioXConvenioMundo(){
		return new UsuarioXConvenioMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoricoIngEstanciaSubcontratadaMundo
	 * 
	 * @return IHistoricoIngEstanciaSubcontratadaMundo
	 * @author Camilo Gomez
	 *
	 */
	public static IHistoricoIngEstanciaSubcontratadaMundo crearHistoricoIngEstanciaSubMundo(){
		return new HistoricoIngEstanciaSubcontratadaMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorServMedicMundo
	 * 
	 * @return INivelAutorServMedicMundo
	 * @author Fabian Becerra
	 *
	 */
	public static INivelAutorServMedicMundo crearNivelAutorServMedicMundo(){
		return new NivelAutorServMedicMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoNivelAutorizacionMundo
	 * 
	 * @return IProcesoNivelAutorizacionMundo
	 * @author, Fabian Becerra
	 *
	 */
	public static IProcesoNivelAutorizacionMundo crearProcesoNivelAutorizacionMundo(){
		return new ProcesoNivelAutorizacionMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoNivelAutorizacionManualMundo
	 * 
	 * @return IProcesoNivelAutorizacionManualMundo
	 * @author, Fabian Becerra
	 *
	 */
	public static IProcesoNivelAutorizacionManualMundo crearProcesoNivelAutorizacionManualMundo(){
		return new ProcesoNivelAutorizacionManualMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempServArtMundo
	 * 
	 * @return ICierreTempServArtMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempServArtMundo crearCierreTempServArtMundo(){
		return new CierreTempServArtMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAtenServMundo
	 * 
	 * @return ICierreTempNivelAtenServMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAtenServMundo crearCierreTempNivelAtenServMundo(){
		return new CierreTempNivelAtenServMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempGrupoServicioMundo
	 * 
	 * @return ICierreTempGrupoServicioMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempGrupoServicioMundo crearCierreTempGrupoServicioMundo(){
		return new CierreTempGrupoServicioMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteGruServMundo
	 * 
	 * @return ICierreTempNivelAteGruServMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAteGruServMundo crearCierreTempNivelAteGruServMundo(){
		return new CierreTempNivelAteGruServMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteGruServMundo
	 * 
	 * @return ICierreTempNivelAteGruServMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempNaturArtMundo crearCierreTempNaturArtMundo(){
		return new CierreTempNaturArtMundo();
	}
	
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAtenArtMundo
	 * 
	 * @return ICierreTempNivelAtenArtMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAtenArtMundo crearCierreTempNivelAtenArtMundo(){
		return new CierreTempNivelAtenArtMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteNatArtMundo
	 * 
	 * @return ICierreTempNivelAteNatArtMundo
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAteNatArtMundo crearCierreTempNivelAteNatArtMundo(){
		return new CierreTempNivelAteNatArtMundo();
	}	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAtencionMundo
	 * 
	 * @return INivelAtencionMundo
	 * @author Fabián Becerra
	 */
	public static INivelAtencionMundo crearNivelAtencionMundo(){
		return new NivelAtencionMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogSubirPacientesMundo
	 * 
	 * @return LogSubirPacientesMundo
	 * @author Camilo Gómez
	 */
	public static ILogSubirPacientesMundo crearLogSubirPacientesMundo(){
		return new LogSubirPacientesMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IInconsistenSubirPacienteMundo
	 * 
	 * @return InconsistenSubirPacienteMundo
	 * @author Camilo Gómez
	 */
	public static IInconsistenSubirPacienteMundo crearInconsistenSubirPacienteMundo(){
		return new InconsistenSubirPacienteMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICapitadoInconsistenciaMundo
	 * 
	 * @return CapitadoInconsistenciaMundo
	 * @author Camilo Gómez
	 */
	public static ICapitadoInconsistenciaMundo crearCapitadoInconsistenciaMundo(){
		return new CapitadoInconsistenciaMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICapitadoInconsistenciaMundo
	 * 
	 * @return CapitadoInconsistenciaMundo
	 * @author Camilo Gómez
	 */
	public static IInconsistenciasCamposMundo crearInconsistenciasCamposMundo(){
		return new InconsistenciasCamposMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IInconsistenciaPersonaMundo
	 * 
	 * @return IInconsistenciaPersonaMundo
	 * @author Camilo Gómez
	 */
	public static IInconsistenciaPersonaMundo crearInconsistenciaPersonaMundo(){
		return new InconsistenciaPersonaMundo();
	}
	

	/**
	 * Este Método se encarga de crear una instancia para
	 * la clase IOrdenCapitacionMundo
	 * 
	 * @return OrdenCapitacionMundo
	 * @author Ricardo Ruiz Combita
	 */
	public static IOrdenCapitacionMundo crearOrdenCapitacionMundo(){
		return new OrdenCapitacionMundo();
	}
	
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoCierrePresupuestoMundo
	 * 
	 * @return crearProcesoCierrePresupuestoMundo
	 * @author Cristhian Murillo
	 */
	public static IProcesoCierrePresupuestoMundo crearProcesoCierrePresupuestoMundo(){
		return new ProcesoCierrePresupuestoMundo();
	}	
		
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMotivosModificacionPresupuestoMundo
	 * @return MotivosModificacionPresupuestoMundo
	 */
	public static IMotivosModificacionPresupuestoMundo crearMotivosModifiaccionPresupuestoMundo(){
		return new MotivosModificacionPresupuestoMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierrePresupuestoCapitacionoMundo
	 * @return CierrePresupuestoCapitacionoMundo
	 */
	public static ICierrePresupuestoCapitacionoMundo crearCierrePresupuestoCapitacionoMundo(){
		return new CierrePresupuestoCapitacionoMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IParametrizacionPresupuestoCapitacion
	 * 
	 * @return ParametrizacionPresupuestoCapitacion
	 * @author Diego Corredor
	 */
	public static IParametrizacionPresupuestoCapitacionMundo crearParametrizacionPresupuestoCapitacionMundo(){
		return new ParametrizacionPresupuestoCapitacionMundo();
	}	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IValorizacionPresupuestoCapGeneral
	 * 
	 * @return ValorizacionPresupuestoCapGeneral
	 * @author Diego Corredor
	 */
	public static IValorizacionPresupuestoCapGeneralMundo crearValorizacionPresupuestoCapGeneralMundo(){
		return new ValorizacionPresupuestoCapGeneralMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IDetalleValorizacionServicioMundo
	 * 
	 * @return IDetalleValorizacionServicioMundo
	 * @author Diego Corredor
	 */
	public static IDetalleValorizacionServicioMundo crearDetalleValorizacionServicioMundo(){
		return new DetalleValorizacionServicioMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IDetalleValorizacionArticuloMundo
	 * 
	 * @return IDetalleValorizacionArticuloMundo
	 * @author Diego Corredor
	 */
	public static IDetalleValorizacionArticuloMundo crearDetalleValorizacionArticuloMundo(){
		return new DetalleValorizacionArticuloMundo();
	}
		
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogParametrizacionPresupuestoCapMundo
	 * 
	 * @return ILogParametrizacionPresupuestoCapMundo
	 * @author Diego Corredor
	 */
	public static ILogParametrizacionPresupuestoCapMundo crearLogParametrizacionPresupuestoCapMundo(){
		return new LogParametrizacionPresupuestoMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ILogDetalleParametrizacionPresupuestoMundo
	 * 
	 * @return ILogDetalleParametrizacionPresupuestoMundo
	 * @author Diego Corredor
	 */
	public static ILogDetalleParametrizacionPresupuestoMundo crearLogDetalleParametrizacionPresupuestoMundo(){
		return new LogDetalleParametrizacionPresupuestoMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoFacturacionPresupuestoCapitacionMundo
	 * @return ProcesoFacturacionPresupuestoCapitacionMundo
	 */
	public static IProcesoFacturacionPresupuestoCapitacionMundo crearProcesoFacturacionPresupuestoCapitacionMundo(){
		return new ProcesoFacturacionPresupuestoCapitacionMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoAutorizacionesPresupuestoCapitacionMundo
	 * @return ProcesoAutorizacionesPresupuestoCapitacionMundo
	 */
	public static IProcesoAutorizacionesPresupuestoCapitacionMundo crearProcesoAutorizacionesPresupuestoCapitacionMundo(){
		return new ProcesoAutorizacionesPresupuestoCapitacionMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoOrdenesPresupuestoCapitacionMundo
	 * @return ProcesoOrdenesPresupuestoCapitacionMundo
	 */
	public static IProcesoOrdenesPresupuestoCapitacionMundo crearProcesoOrdenesPresupuestoCapitacionMundo(){
		return new ProcesoOrdenesPresupuestoCapitacionMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo
	 * @return ProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo
	 */
	public static IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo crearProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo(){
		return new ProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IConvUsuariosCapitadosMundo
	 * 
	 * @return IConvUsuariosCapitadosMundo
	 * @author Ricardo Ruiz
	 */
	public static IConvUsuariosCapitadosMundo crearConvUsuariosCapitadosMundo(){
		return new ConvUsuariosCapitadosMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IContratoCargueMundo
	 * 
	 * @return IContratoCargueMundo
	 * @author Ricardo Ruiz
	 */
	public static IContratoCargueMundo crearContratoCargueMundo(){
		return new ContratoCargueMundo(); 
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IUnidadPagoMundo
	 * 
	 * @return IUnidadPagoMundo
	 * @author Ricardo Ruiz
	 */
	public static IUnidadPagoMundo crearUnidadPagoMundo(){
		return new UnidadPagoMundo(); 
	}
	
	/**
	 * Este método se encarga de crear una instancia para
	 * la entidad IAutorizacionCapitacionOrdenesAmbulatoriasMundo
	 * @return AutorizacionCapitacionOrdenesAmbulatoriasMundo
	 * @author Camilo Gómez
	 */
	public static IAutorizacionCapitacionOrdenesAmbulatoriasMundo crearAutorizacionCapitacionOrdenesAmbulatoriasMundo(){
		return new AutorizacionCapitacionOrdenesAmbulatoriasMundo();
	}
	
	/**
	 * Este método se encarga de crear una instancia para
	 * la entidad IAutorizacionesCapitacionPeticionesMundo
	 * @return AutorizacionesCapitacionPeticionesMundo
	 * @author Camilo Gómez
	 */
	public static IAutorizacionesCapitacionPeticionesMundo crearAutorizacionesCapitacionPeticionesMundo(){
		return new AutorizacionesCapitacionPeticionesMundo();
	}
	
	/**
	 * Este método se encarga de crear una instancia para
	 * la entidad IValidacionPresupuestoCapitacionMundo
	 * @return ValidacionPresupuestoCapitacionMundo
	 * @author Ricardo Ruiz
	 */
	public static IValidacionPresupuestoCapitacionMundo crearValidacionPresupuestoCapitacionMundo(){
		return new ValidacionPresupuestoCapitacionMundo();
	}
	
	/**
	 * Este método se encarga de crear una instancia para
	 * la entidad ICierreTempClaseInvArtMundo
	 * @return CierreTempClaseInvArtMundo
	 * @author Ricardo Ruiz
	 */
	public static ICierreTempClaseInvArtMundo crearCierreTempClaseInvArtMundo(){
		return new CierreTempClaseInvArtMundo();
	}
	
	/**
	 * Este método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteClaseInvArtMundo
	 * @return CierreTempNivelAteClaseInvArtMundo
	 * @author Ricardo Ruiz
	 */
	public static ICierreTempNivelAteClaseInvArtMundo crearCierreTempNivelAteClaseInvArtMundo(){
		return new CierreTempNivelAteClaseInvArtMundo();
	}
}
