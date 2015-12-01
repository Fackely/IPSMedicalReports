package com.servinte.axioma.servicio.fabrica.capitacion;

import com.servinte.axioma.servicio.impl.capitacion.CapitadoInconsistenciaServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempGrupoServicioServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempNaturArtServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempNivelAteGruServServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempNivelAteNatArtServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempNivelAtenArtServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempNivelAtenServServicio;
import com.servinte.axioma.servicio.impl.capitacion.CierreTempServArtServicio;
import com.servinte.axioma.servicio.impl.capitacion.HistoricoCapitacionSubcontratadaServicio;
import com.servinte.axioma.servicio.impl.capitacion.HistoricoIngEstanciaSubcontratadaServicio;
import com.servinte.axioma.servicio.impl.capitacion.InconsistenSubirPacienteServicio;
import com.servinte.axioma.servicio.impl.capitacion.InconsistenciaPersonaServicio;
import com.servinte.axioma.servicio.impl.capitacion.InconsistenciasCamposServicio;
import com.servinte.axioma.servicio.impl.capitacion.LogSubirPacientesServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorServMedicServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionAgrupacionServicioServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionArticuloEspecificoServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionOcupacionMedicaServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionServicioArticuloServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionServicioEspecificoServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.impl.capitacion.NivelAutorizacionUsuarioServicio;
import com.servinte.axioma.servicio.impl.capitacion.PrioridadOcupacionMedicaServicio;
import com.servinte.axioma.servicio.impl.capitacion.PrioridadUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.impl.capitacion.ProcesoNivelAutorizacionManualServicio;
import com.servinte.axioma.servicio.impl.capitacion.ProcesoNivelAutorizacionServicio;
import com.servinte.axioma.servicio.impl.capitacion.UsuarioXConvenioServicio;
import com.servinte.axioma.servicio.impl.capitacion.UsuariosCapitadosServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICapitadoInconsistenciaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempGrupoServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNaturArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteGruServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteNatArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempServArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoCapitacionSubcontratadaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoIngEstanciaSubcontratadaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IInconsistenSubirPacienteServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IInconsistenciaPersonaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IInconsistenciasCamposServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ILogSubirPacientesServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorServMedicServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionArticuloEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionOcupacionMedicaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicioArticuloServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionUsuarioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadOcupacionMedicaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IProcesoNivelAutorizacionManualServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IProcesoNivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuarioXConvenioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuariosCapitadosServicio;

/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo de capitación
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class CapitacionFabricaServicio {
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicio
	 * 
	 * @return INivelAutorizacionServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicio crearNivelAutorizacionServicio(){
		return new NivelAutorizacionServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionUsuarioServicio
	 * 
	 * @return INivelAutorizacionUsuarioServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionUsuarioServicio crearNivelAutorizacionUsuarioServicio(){
		return new NivelAutorizacionUsuarioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionUsuarioEspecificoServicio
	 * 
	 * @return INivelAutorizacionUsuarioEspecificoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionUsuarioEspecificoServicio crearNivelAutorizacionUsuarioEspecificoServicio(){
		return new NivelAutorizacionUsuarioEspecificoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionOcupacionMedicaServicio
	 * 
	 * @return INivelAutorizacionOcupacionMedicaServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionOcupacionMedicaServicio crearNivelAutorizacionOcupacionMedicaServicio(){
		return new NivelAutorizacionOcupacionMedicaServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPrioridadUsuarioEspecificoServicio
	 * 
	 * @return IPrioridadUsuarioEspecificoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IPrioridadUsuarioEspecificoServicio crearPrioridadUsuarioEspecificoServicio(){
		return new PrioridadUsuarioEspecificoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPrioridadOcupacionMedicaServicio
	 * 
	 * @return IPrioridadOcupacionMedicaServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IPrioridadOcupacionMedicaServicio crearPrioridadOcupacionMedicaServicio(){
		return new PrioridadOcupacionMedicaServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicioArticuloServicio
	 * 
	 * @return INivelAutorizacionServicioArticuloServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicioArticuloServicio crearNivelAutorizacionServicioArticuloServicio(){
		return new NivelAutorizacionServicioArticuloServicio();
	}
	
	 /** 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionAgrupacionServicioServicio
	 * 
	 * @return INivelAutorizacionAgrupacionServicioServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionAgrupacionServicioServicio crearNivelAutorizacionAgrupacionServicioServicio(){
		return new NivelAutorizacionAgrupacionServicioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionArticuloEspecificoServicio
	 * 
	 * @return INivelAutorizacionArticuloEspecificoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionArticuloEspecificoServicio crearNivelAutorizacionArticuloEspecificoServicio(){
		return new NivelAutorizacionArticuloEspecificoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicioEspecificoServicio
	 * 
	 * @return INivelAutorizacionServicioEspecificoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicioEspecificoServicio crearNivelAutorizacionServicioEspecificoServicio(){
		return new NivelAutorizacionServicioEspecificoServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorizacionAgrupacionArticuloServicio
	 * 
	 * @return INivelAutorizacionAgrupacionArticuloServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionAgrupacionArticuloServicio crearNivelAutorizacionAgrupacionArticuloServicio(){
		return new NivelAutorizacionAgrupacionArticuloServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoNivelAutorizacionServicio
	 * 
	 * @return IProcesoNivelAutorizacionServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IProcesoNivelAutorizacionServicio crearProcesoNivelAutorizacionServicio(){
		return new ProcesoNivelAutorizacionServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IProcesoNivelAutorizacionManualServicio
	 * 
	 * @return IProcesoNivelAutorizacionManualServicio
	 * @author, Fabian Becerra
	 *
	 */
	public static IProcesoNivelAutorizacionManualServicio crearProcesoNivelAutorizacionManualServicio(){
		return new ProcesoNivelAutorizacionManualServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IUsuariosCapitadosServicio
	 * 
	 * @return IUsuariosCapitadosServicio
	 * @author Cristhian Murillo
	 *
	 */
	public static IUsuariosCapitadosServicio crearUsuariosCapitadosServicio(){
		return new UsuariosCapitadosServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IUsuarioXConvenioServicio
	 * 
	 * @return IUsuarioXConvenioServicio
	 * @author Cristhian Murillo
	 *
	 */
	public static IUsuarioXConvenioServicio crearUsuarioXConvenioServicio(){
		return new UsuarioXConvenioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoricoCapitacionSubcontratadaServicio
	 * 
	 * @return IHistoricoCapitacionSubcontratadaServicio
	 * @author Camilo Gomez
	 *
	 */
	public static IHistoricoCapitacionSubcontratadaServicio crearHistoricoCapitacionSubServicio(){
		return new HistoricoCapitacionSubcontratadaServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoricoCapitacionSubcontratadaServicio
	 * 
	 * @return IHistoricoIngEstanciaSubcontratadaServicio
	 * @author Camilo Gomez
	 */
	public static IHistoricoIngEstanciaSubcontratadaServicio crearHistoricoIngEstanciaSubServicio(){
		return new HistoricoIngEstanciaSubcontratadaServicio();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad INivelAutorServMedicServicio
	 * 
	 * @return INivelAutorServMedicServicio
	 * @author Fabian Becerra
	 */
	public static INivelAutorServMedicServicio crearNivelAutorServMedicServicio(){
		return new NivelAutorServMedicServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempServArtServicio
	 * 
	 * @return ICierreTempServArtServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempServArtServicio crearCierreTempServArtServicio(){
		return new CierreTempServArtServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAtenServServicio
	 * 
	 * @return ICierreTempNivelAtenServServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAtenServServicio crearCierreTempNivelAtenServServicio(){
		return new CierreTempNivelAtenServServicio();
	}

	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempGrupoServicioServicio
	 * 
	 * @return ICierreTempGrupoServicioServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempGrupoServicioServicio crearCierreTempGrupoServicioServicio(){
		return new CierreTempGrupoServicioServicio();
	}
	

	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempGrupoServicioServicio
	 * 
	 * @return ICierreTempGrupoServicioServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAteGruServServicio crearCierreTempNivelAteGruServServicio(){
		return new CierreTempNivelAteGruServServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNaturArtServicio
	 * 
	 * @return ICierreTempNaturArtServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempNaturArtServicio crearCierreTempNaturArtServicio(){
		return new CierreTempNaturArtServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAtenArtServicio
	 * 
	 * @return ICierreTempNivelAtenArtServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAtenArtServicio crearCierreTempNivelAtenArtServicio(){
		return new CierreTempNivelAtenArtServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteNatArtServicio
	 * 
	 * @return ICierreTempNivelAteNatArtServicio
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAteNatArtServicio crearCierreTempNivelAteNatArtServicio(){
		return new CierreTempNivelAteNatArtServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ISubirPacientesServicio
	 * 	  
	 * @return LogSubirPacientesServicio
	 * @author Camilo Gómez
	 */
	public static ILogSubirPacientesServicio crearLogSubirPacientesServicio(){
		return new LogSubirPacientesServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IInconsistenSubirPacienteServicio
	 * 	  
	 * @return InconsistenSubirPacienteServicio
	 * @author Camilo Gómez
	 */
	public static IInconsistenSubirPacienteServicio crearInconsistenSubirPacienteServicio(){
		return new InconsistenSubirPacienteServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICapitadoInconsistenciaServicio
	 * 	  
	 * @return CapitadoInconsistenciaServicio
	 * @author Camilo Gómez
	 */
	public static ICapitadoInconsistenciaServicio crearCapitadoInconsistenciaServicio(){
		return new CapitadoInconsistenciaServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IInconsistenciasCamposServicio
	 * 	  
	 * @return InconsistenciasCamposServicio
	 * @author Camilo Gómez
	 */
	public static IInconsistenciasCamposServicio crearInconsistenciasCamposServicio(){
		return new InconsistenciasCamposServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IInconsistenciaPersonaServicio
	 * 	  
	 * @return InconsistenciaPersonaServicio
	 * @author Camilo Gómez
	 */
	public static IInconsistenciaPersonaServicio crearInconsistenciaPersonaServicio(){
		return new InconsistenciaPersonaServicio();
	}	
}
