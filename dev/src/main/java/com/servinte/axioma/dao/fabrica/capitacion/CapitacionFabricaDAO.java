package com.servinte.axioma.dao.fabrica.capitacion;

import com.servinte.axioma.dao.impl.HistoAutorizacionCapitaSubHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CapitadoInconsistenciaHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierrePresupuestoCapitacionHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempClaseInvArtHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempGrupoServicioHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempNaturArtHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempNivelAteClaseInvArtHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempNivelAteGruServHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempNivelAteNatArtHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempNivelAtenArtHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempNivelAtenServHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.CierreTempServArtHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.ContratoCargueHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.ConvUsuariosCapitadosHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.DetalleValorizacionArticuloHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.DetalleValorizacionServicioHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.HistoAutorizIngEstanciaSubHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.InconsistenSubirPacienteHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.InconsistenciaPersonaHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.InconsistenciasCamposHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.LogDetalleParametrizacionPresupuestoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.LogParametrizacionPresupuestoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.LogSubirPacientesHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.MotivosModificacionPresupuestoDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAtencionHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorServMedicHibernateDao;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionAgrupacionServicioHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionArticuloEspecificoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionOcupacionMedicaHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionServicioArticuloHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionServicioEspecificoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionUsuarioEspecificoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.NivelAutorizacionUsuarioHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.ParametrizacionPresupuestoCapHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.PrioridadOcupacionMedicaHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.PrioridadUsuarioEspecificoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.UnidadPagoHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.UsuarioXConvenioHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.UsuariosCapitadosHibernateDAO;
import com.servinte.axioma.dao.impl.capitacion.ValorizacionPresupuestoCapGeneralHibernateDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICapitadoInconsistenciaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempClaseInvArtDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempGrupoServicioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNaturArtDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteGruServDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteNatArtDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenArtDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenServDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempServArtDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IContratoCargueDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IConvUsuariosCapitadosDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenSubirPacienteDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenciaPersonaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenciasCamposDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogSubirPacientesDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorServMedicDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionAgrupacionServicioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionArticuloEspecificoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionOcupacionMedicaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioArticuloDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadOcupacionMedicaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadUsuarioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IUnidadPagoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoricoIngEstanciaSubcontratadaDAO;

/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del m�dulo de capitaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class CapitacionFabricaDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionDAO
	 * 
	 * @return INivelAutorizacionDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionDAO crearNivelAutorizacionDAO(){
		return new NivelAutorizacionHibernateDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionUsuarioDAO
	 * 
	 * @return INivelAutorizacionUsuarioDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionUsuarioDAO crearNivelAutorizacionUsuarioDAO(){
		return new NivelAutorizacionUsuarioHibernateDAO();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionUsuarioEspecificoDAO
	 * 
	 * @return INivelAutorizacionUsuarioEspecificoDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionUsuarioEspecificoDAO crearNivelAutorizacionUsuarioEspecificoDAO(){
		return new NivelAutorizacionUsuarioEspecificoHibernateDAO();
	}
	

	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionOcupacionMedicaDAO
	 * 
	 * @return INivelAutorizacionOcupacionMedicaDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionOcupacionMedicaDAO crearNivelAutorizacionOcupacionMedicaDAO(){
		return new NivelAutorizacionOcupacionMedicaHibernateDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IPrioridadUsuarioEspecificoDAO
	 * 
	 * @return IPrioridadUsuarioEspecificoDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IPrioridadUsuarioEspecificoDAO crearPrioridadUsuarioEspecificoDAO(){
		return new PrioridadUsuarioEspecificoHibernateDAO();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IPrioridadOcupacionMedicaDAO
	 * 
	 * @return IPrioridadOcupacionMedicaDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IPrioridadOcupacionMedicaDAO crearPrioridadOcupacionMedicaDAO(){
		return new PrioridadOcupacionMedicaHibernateDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicioArticuloDAO
	 * 
	 * @return INivelAutorizacionServicioArticuloDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicioArticuloDAO crearNivelAutorizacionServicioArticuloDAO(){
		return new NivelAutorizacionServicioArticuloHibernateDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionAgrupacionServicioDAO
	 * 
	 * @return INivelAutorizacionAgrupacionServicioDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionAgrupacionServicioDAO crearNivelAutorizacionAgrupacionServicioDAO(){
		return new NivelAutorizacionAgrupacionServicioHibernateDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionArticuloEspecificoDAO
	 * 
	 * @return INivelAutorizacionArticuloEspecificoDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionArticuloEspecificoDAO crearNivelAutorizacionArticuloEspecificoDAO(){
		return new NivelAutorizacionArticuloEspecificoHibernateDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAutorizacionServicioEspecificoDAO
	 * 
	 * @return INivelAutorizacionServicioEspecificoDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INivelAutorizacionServicioEspecificoDAO crearNivelAutorizacionServicioEspecificoDAO(){
		return new NivelAutorizacionServicioEspecificoHibernateDAO();
	}
	
	
	/**
	 * @return IUsuariosCapitadosDAO
	 * @author Cristhian Murillo
	 */
	public static IUsuariosCapitadosDAO crearUsuariosCapitadosDAO(){
		return new UsuariosCapitadosHibernateDAO();
	}
	
	/**
	 * @return IUsuarioXConvenioDAO
	 * @author Cristhian Murillo
	 */
	public static IUsuarioXConvenioDAO crearUsuarioXConvenioDAO(){
		return new UsuarioXConvenioHibernateDAO();
	}
	
	
	/**
	 * 
	 * @return HistoAutorizacionCapitaSubHibernateDAO
	 * @author Camilo Gomez
	 */
	public static IHistoAutorizacionCapitaSubDAO crearHistoricoCapitacionSubcontratadaDAO(){
		return new HistoAutorizacionCapitaSubHibernateDAO();
	}
	
	/**
	 * @return IHistoAutorizacionIngEstanciaDAO
	 * @author Camilo Gomez
	 */
	public static IHistoricoIngEstanciaSubcontratadaDAO crearHistoricoIngEstanciaSubcontratadaDAO(){
		return new HistoAutorizIngEstanciaSubHibernateDAO();
	}
	
	
	/**
	 * @return INivelAutorServMedicDAO
	 * @author Fabian Becerra
	 */
	public static INivelAutorServMedicDAO crearNivelAutorServMedicDAO(){
		return new NivelAutorServMedicHibernateDao();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempServArtDAO
	 * 
	 * @return ICierreTempServArtDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempServArtDAO crearCierreTempServArtDAO(){
		return new CierreTempServArtHibernateDAO();
	}	
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAtenServDAO
	 * 
	 * @return ICierreTempNivelAtenServDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAtenServDAO crearCierreTempNivelAtenServDAO(){
		return new CierreTempNivelAtenServHibernateDAO();
	}
	
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempGrupoServicioDAO
	 * 
	 * @return ICierreTempGrupoServicioDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempGrupoServicioDAO crearCierreTempGrupoServicioDAO(){
		return new CierreTempGrupoServicioHibernateDAO();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteGruServDAO
	 * 
	 * @return ICierreTempNivelAteGruServDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAteGruServDAO crearCierreTempNivelAteGruServDAO(){
		return new CierreTempNivelAteGruServHibernateDAO();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempNaturArtDAO
	 * 
	 * @return ICierreTempNaturArtDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempNaturArtDAO crearCierreTempNaturArtDAO(){
		return new CierreTempNaturArtHibernateDAO();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAtenArtDAO
	 * 
	 * @return ICierreTempNivelAtenArtDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAtenArtDAO crearCierreTempNivelAtenArtDAO(){
		return new CierreTempNivelAtenArtHibernateDAO();
	}
	
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteNatArtDAO
	 * 
	 * @return ICierreTempNivelAteNatArtDAO
	 * @author Angela Aguirre
	 */
	public static ICierreTempNivelAteNatArtDAO crearCierreTempNivelAteNatArtDAO(){
		return new CierreTempNivelAteNatArtHibernateDAO();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ILogSubirPacientesDAO
	 * 
	 * @return ILogSubirPacientesDAO
	 * @author Camilo G�mez
	 */
	public static ILogSubirPacientesDAO crearLogSubirPacientesDAO(){
		return new LogSubirPacientesHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IInconsistenSubirPacienteDAO
	 * 
	 * @return InconsistenSubirPacienteHibernateDAO
	 * @author Camilo G�mez
	 */
	public static IInconsistenSubirPacienteDAO crearInconsistenSubirPacienteDAO(){
		return new InconsistenSubirPacienteHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICapitadoInconsistenciaDAO
	 * 
	 * @return CapitadoInconsistenciaHibernateDAO
	 * @author Camilo G�mez
	 */
	public static ICapitadoInconsistenciaDAO crearCapitadoInconsistenciaDAO(){
		return new CapitadoInconsistenciaHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IInconsistenciasCamposDAO
	 * 
	 * @return InconsistenciasCamposHibernateDAO
	 * @author Camilo G�mez
	 */
	public static IInconsistenciasCamposDAO crearInconsistenciasCamposDAO(){
		return new InconsistenciasCamposHibernateDAO(); 
	}
	
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IInconsistenciaPersonaDAO
	 * 
	 * @return InconsistenciaPersonaHibernateDAO
	 * @author Camilo G�mez
	 */
	public static IInconsistenciaPersonaDAO crearInconsistenciaPersonaDAO(){
		return new InconsistenciaPersonaHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad INivelAtencionDAO
	 * 
	 * @return INivelAtencionDAO
	 * @author Cristhian Murillo
	 */
	public static INivelAtencionDAO crearNivelAtencionDAO(){
		return new NivelAtencionHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IMotivosModificacionPresupuestoDAO
	 * @return IMotivosModificacionPresupuestoDAO
	 */
	public static IMotivosModificacionPresupuestoDAO crearMotivosModificacionPresupuesto(){
		return new MotivosModificacionPresupuestoDAO();
	}
	
	
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierrePresupuestoCapitacionDAO
	 * 
	 * @return ICierrePresupuestoCapitacionDAO
	 * @author Cristhian Murillo
	 */
	public static ICierrePresupuestoCapitacionDAO crearCierrePresupuestoCapitacionHibernateDAO(){
		return new CierrePresupuestoCapitacionHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IParamPresupuestosCapDAO
	 * 
	 * @return IParamPresupuestosCapDAO
	 * @author Diego Corredor
	 */
	public static IParametrizacionPresupuestosCapDAO crearParamPresupuestosCapDAO(){
		return new ParametrizacionPresupuestoCapHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IValorizacionPresupuestoCapDAO
	 * 
	 * @return IValorizacionPresupuestoCapDAO
	 * @author Diego Corredor
	 */
	public static IValorizacionPresupuestoCapDAO crearValorizacionPresupuestoCapDAO(){
		return new ValorizacionPresupuestoCapGeneralHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IDetalleValorizacionServicioDAO
	 * 
	 * @return IDetalleValorizacionServicioDAO
	 * @author Diego Corredor
	 */
	public static IDetalleValorizacionServicioDAO crearDetalleValorizacionServicioDAO(){
		return new DetalleValorizacionServicioHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IDetalleValorizacionArticuloDAO
	 * 
	 * @return IDetalleValorizacionArticuloDAO
	 * @author Diego Corredor
	 */
	public static IDetalleValorizacionArticuloDAO crearDetalleValorizacionArticuloDAO(){
		return new DetalleValorizacionArticuloHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ILogParametrizacionPresupuestoCapDAO
	 * 
	 * @return ILogParametrizacionPresupuestoCapDAO
	 * @author Diego Corredor
	 */
	public static ILogParametrizacionPresupuestoCapDAO crearLogParametrizacionPresupuestoCapDAO(){
		return new LogParametrizacionPresupuestoHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ILogDetalleParametrizacionPresupuestoDAO
	 * 
	 * @return ILogDetalleParametrizacionPresupuestoDAO
	 * @author Diego Corredor
	 */
	public static ILogDetalleParametrizacionPresupuestoDAO crearLogDetalleParametrizacionPresupuestoDAO(){
		return new LogDetalleParametrizacionPresupuestoHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IConvUsuariosCapitadosDAO
	 * 
	 * @return IConvUsuariosCapitadosDAO
	 * @author Ricardo Ruiz
	 */
	public static IConvUsuariosCapitadosDAO crearConvUsuariosCapitadosDAO(){
		return new ConvUsuariosCapitadosHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IContratoCargueDAO
	 * 
	 * @return IContratoCargueDAO
	 * @author Ricardo Ruiz
	 */
	public static IContratoCargueDAO crearContratoCargueDAO(){
		return new ContratoCargueHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad IUnidaPagoDAO
	 * 
	 * @return IUnidadPagoDAO
	 * @author Ricardo Ruiz
	 */
	public static IUnidadPagoDAO crearUnidadPagoDAO(){
		return new UnidadPagoHibernateDAO(); 
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempClaseInvArtDAO
	 * 
	 * @return ICierreTempClaseInvArtDAO
	 * @author Ricardo Ruiz
	 */
	public static ICierreTempClaseInvArtDAO crearCierreTempClaseInvArtDAO(){
		return new CierreTempClaseInvArtHibernateDAO();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia para
	 * la entidad ICierreTempNivelAteClaseInvArtDAO
	 * 
	 * @return ICierreTempNivelAteClaseInvArtDAO
	 * @author Ricardo Ruiz
	 */
	public static ICierreTempNivelAteClaseInvArtDAO crearCierreTempNivelAteClaseInvArtDAO(){
		return new CierreTempNivelAteClaseInvArtHibernateDAO();
	}
	
}
