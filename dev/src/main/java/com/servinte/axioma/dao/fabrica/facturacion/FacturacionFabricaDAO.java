package com.servinte.axioma.dao.fabrica.facturacion;

import com.servinte.axioma.dao.impl.administracion.CentroCostosDAO;
import com.servinte.axioma.dao.impl.facturacion.ControlAnticiposContratoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.DetalleMontoGeneralHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.DetalleMontoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.EmpresasInstitucionHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.EntidadesSubcontratadasDAO;
import com.servinte.axioma.dao.impl.facturacion.EsquemasTarifariosHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.EstanciaViaIngCentroCostoDAO;
import com.servinte.axioma.dao.impl.facturacion.FacturasHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.FinalidadesServicioHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.GruposServiciosHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.HistoDetalleMontoGeneralHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.HistoDetalleMontoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.HistoMontosCobroHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.HistoricoEncabezadoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogProfSaludNoHonorarioHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogRipsEntSubInconsisArchHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogRipsEntSubInconsisCampHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogRipsEntSubRegValorHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogRipsEntidadesSubArchivoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogRipsEntidadesSubRegistrHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.LogRipsEntidadesSubcontratadasHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.MontoAgrupacionServiciosHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.MontoArticuloEspecificoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.MontoServicioEspecificoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.MontosCobroHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.ProgramasHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.ServiciosHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.TarifasEntidadSubHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.TercerosDAO;
import com.servinte.axioma.dao.impl.facturacion.TiposContratoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.TiposMontoHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.TiposServicioHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.ViewFinalidadesServDAO;
import com.servinte.axioma.dao.impl.facturacion.convenio.ContratoDAO;
import com.servinte.axioma.dao.impl.facturacion.convenio.ConvenioDAO;
import com.servinte.axioma.dao.impl.facturacion.convenio.ConveniosIngresoPacienteHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesEntidadesSubHibernateDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IControlAnticiposContratoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoGeneralDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEmpresasInstitucionDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEntidadesSubcontratadasDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEsquemasTarifariosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEstanciaViaIngCentroCostoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IFacturasDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IFinalidadesServicioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IGruposServiciosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoDetalleMontoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoDetalleMontoGeneralDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoMontosCobroDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogProfSaludNoHonorarioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisArchDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisCampDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubRegValorDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubArchivoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubRegistrDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubcontratadasDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoAgrupacionServiciosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoArticuloEspecificoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoServicioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontosCobroDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IProgramasDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IServiciosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITarifasEntidadSubDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITercerosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposContratoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposMontoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposServicioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IViewFinalidadesServDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConveniosIngresoPacienteDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;

/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo de facturación
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class FacturacionFabricaDAO {
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	private FacturacionFabricaDAO(){
		
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroDAO
	 * 
	 * @return IMontosCobroDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IMontosCobroDAO crearMontosCobroDAO(){
		return new MontosCobroHibernateDAO();
	}
	
	/**
	 * Este m&eacute;todo se encarga de crear una instancia 
	 * para la entidad IEmpresasInstitucionDAO
	 * @return EmpresasInstitucionHibernateDAO
	 * 
	 * @author Yennifer Guerrero
	 */
	public static IEmpresasInstitucionDAO crearEmpresasInstitucionDAO(){
		return new EmpresasInstitucionHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroDAO
	 * 
	 * @return IDetalleMontoDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetalleMontoDAO crearDetalleMontoDAO(){
		return new DetalleMontoHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposMontoDAO
	 * 
	 * @return ITiposMontoDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposMontoDAO crearTiposMontoDAO(){
		return new TiposMontoHibernateDAO();
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
	public static IDetalleMontoGeneralDAO crearDetalleMontoGeneralDAO(){
		return new DetalleMontoGeneralHibernateDAO();
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
	public static IHistoDetalleMontoDAO crearHistoDetalleMontoDAO(){
		return new HistoDetalleMontoHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IHistoDetalleMontoGeneralDAO
	 * 
	 * @return IHistoDetalleMontoGeneralDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoDetalleMontoGeneralDAO crearHistoDetalleMontoGeneralDAO(){
		return new HistoDetalleMontoGeneralHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroDAO
	 * 
	 * @return IHistoMontosCobroDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoMontosCobroDAO crearHistoMontosCobroDAO(){
		return new HistoMontosCobroHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITercerosDAO
	 * 
	 * @return TercerosDAO
	 * @author, Diana Carolina G
	 *
	 */
	public static ITercerosDAO crearTercerosDAO(){
		return new TercerosDAO();
	}
		
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoAgrupacionServiciosDAO
	 * 
	 * @return IMontoAgrupacionServiciosDAO
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoAgrupacionServiciosDAO crearMontoAgrupacionServiciosDAO(){
		return new MontoAgrupacionServiciosHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoServicioEspecificoDAO
	 * 
	 * @return IMontoServicioEspecificoDAO
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoServicioEspecificoDAO crearMontoServicioEspecificoDAO(){
		return new MontoServicioEspecificoHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IGruposServiciosDAO
	 * 
	 * @return IGruposServiciosDAO
	 * @author, Angela Aguirre
	 *
	 */
	public static IGruposServiciosDAO crearGruposServiciosDAO(){
		return new GruposServiciosHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposServicioDAO
	 * 
	 * @return ITiposServicioDAO
	 * @author, Angela Aguirre
	 *
	 */
	public static ITiposServicioDAO crearTipoServicioDAO(){
		return new TiposServicioHibernateDAO();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontoArticuloEspecificoDAO
	 * 
	 * @return IMontoArticuloEspecificoDAO
	 * @author, Angela Aguirre
	 *
	 */
	public static IMontoArticuloEspecificoDAO crearMontoArticuloEspecificoDAO(){
		return new MontoArticuloEspecificoHibernateDAO();
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IConveniosIngresoPacienteDAO
	 * 
	 * @return IConveniosIngresoPacienteDAO
	 *
	 */
	public static IConveniosIngresoPacienteDAO crearConveniosIngresoPacienteDAO(){
		return new ConveniosIngresoPacienteHibernateDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IContratoDAO
	 * 
	 * @return IContratoDAO
	 */
	public static IContratoDAO crearContratoDAO(){
		return new ContratoDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEntidadesSubcontratadasDAO
	 * 
	 * @return IEntidadesSubcontratadasDAO
	 */
	public static IEntidadesSubcontratadasDAO crearEntidadesSubcontratadasDAO(){
		return new EntidadesSubcontratadasDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad ITiposContratoDAO
	 * 
	 * @return ITiposContratoDAO
	 */
	public static ITiposContratoDAO crearTiposContratoDAO(){
		return new TiposContratoHibernateDAO();
	}	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad
	 * IEsquemasTarifariosDAO
	 * 
	 * @return EsquemasTarifariosHibernateDAO
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public static IEsquemasTarifariosDAO crearEsquemasTarifariosDAO() {
		return new EsquemasTarifariosHibernateDAO();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad
	 * IProgramasDAO
	 * 
	 * @return ProgramasHibernateDAO
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public static IProgramasDAO crearProgramasDAO() {
		return new ProgramasHibernateDAO();
	}
	
	

	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de 
	 * {@link IHistoricoEncabezadoDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IHistoricoEncabezadoDAO}.
	 */
	public static IHistoricoEncabezadoDAO crearHistoricoEncabezadoDAO() {
		return new HistoricoEncabezadoHibernateDAO();
	}
	
	/**
	 * 
	 * @return
	 */
	public static final IEstanciaViaIngCentroCostoDAO crearEstanciaViaIngresoDAO(){
		
		return new EstanciaViaIngCentroCostoDAO();
	}
	
	
	/**
	 * Este Método devuelve un objeto que implementa la interfaz
	 * IControlAnticiposContratoDAO
	 * 
	 * @return IControlAnticiposContratoDAO
	 */
	public static IControlAnticiposContratoDAO crearControlAnticiposContratoDAO(){
		return new ControlAnticiposContratoHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogProfSaludNoHonorarioDAO
	 * 
	 * @return LogProfSaludNoHonorarioHibernateDAO
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	public static ILogProfSaludNoHonorarioDAO crearLogProfSaludNoHonorarioDAO() {
		return new LogProfSaludNoHonorarioHibernateDAO();
	}

	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IServiciosDAO
	 * 
	 * @return ServiciosHibernateDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static IServiciosDAO crearServiciosDAO() {
		return new ServiciosHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogRipsEntidadesSubcontratadasDAO
	 * 
	 * @return LogRipsEntidadesSubcontratadasDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubcontratadasDAO crearLogRipsEntiSubDAO() {
		return new LogRipsEntidadesSubcontratadasHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogRipsEntSubInconsisArchDAO
	 * 
	 * @return LogRipsEntSubInconsisArchHibernateDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubInconsisArchDAO crearLogRipsEntiSubInconsisArchiDAO() {
		return new LogRipsEntSubInconsisArchHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogRipsEntidadesSubArchivoDAO
	 * 
	 * @return LogRipsEntidadesSubArchivoHibernateDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubArchivoDAO crearLogRipsEntidadesSubArchiDAO() {
		return new LogRipsEntidadesSubArchivoHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogRipsEntidadesSubArchivoDAO
	 * 
	 * @return LogRipsEntidadesSubRegistrHibernateDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntidadesSubRegistrDAO crearLogRipsEntidadesSubRegistrDAO() {
		return new LogRipsEntidadesSubRegistrHibernateDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogRipsEntSubInconsisCampDAO
	 * 
	 * @return LogRipsEntSubInconsisCampHibernateDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubInconsisCampDAO crearLogRipsEntSubInconsisCampDAO() {
		return new LogRipsEntSubInconsisCampHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IFinalidadesServicioDAO
	 * 
	 * @return FinalidadesServicioHibernateDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static IFinalidadesServicioDAO crearFinalidadesServicioDAO() {
		return new FinalidadesServicioHibernateDAO();
	}
		
	/**
	 * M&eacute;todo encargado de crear una instancia para la entidad 
	 * IViewFinalidadesServDAO
	 * @return IViewFinalidadesServDAO
	 * @author Diana Carolina G
	 */
	public static IViewFinalidadesServDAO crearViewFinalidadesServDAO(){
		return new ViewFinalidadesServDAO();
		
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogRipsEntSubRegValorDAO
	 * 
	 * @return ILogRipsEntSubRegValorDAO
	 * 
	 * @author Fabian Becerra
	 */
	public static ILogRipsEntSubRegValorDAO crearLogRipsEntSubRegValorDAO() {
		return new LogRipsEntSubRegValorHibernateDAO();
	}
	
	

	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IFacturasDAO
	 * 
	 * @return IFacturasDAO
	 * 
	 * @author Cristhian Murillo
	 */
	public static IFacturasDAO crearFacturasDAO() {
		return new FacturasHibernateDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ICentroCostosDAO
	 * 
	 * @return ICentroCostosDAO
	 * 
	 * @author Diana Ruiz 
	 */
	
	
	public static ICentroCostosDAO crearCentroCostoDAO() {
		return new CentroCostosDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para
	 * la entidad IConvenioDAO
	 * 
	 * @return IConvenioDAO
	 */
	public static IConvenioDAO crearConvenioDAO(){
		return new ConvenioDAO();
	}
	
	
	
	/**
	 * Este Metodo se encarga de crear una instancia de la clase ITarifasEntidadSubDAO
	 * @return AutorizacionesEntidadesSubHibernateDAO
	 * @author Diana Ruiz
	 */
	public static ITarifasEntidadSubDAO crearTarifasEntidadSubDAO(){
		return new TarifasEntidadSubHibernateDAO();
	}
	
	
}
