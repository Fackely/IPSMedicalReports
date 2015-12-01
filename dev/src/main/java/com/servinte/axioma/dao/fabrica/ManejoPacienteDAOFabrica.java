package com.servinte.axioma.dao.fabrica;

import com.servinte.axioma.dao.impl.HistoAutorizacionCapitaSubHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.AurorizacionesEntSubCapitacionHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AmparosPorReclamarHibernateDao;
import com.servinte.axioma.dao.impl.manejoPaciente.AutoCapiXCentroCostoHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesCapitacionSubHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesEntSubArticuloHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesEntSubRipsHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesEntSubServiHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesEntidadesSubHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesEstanciaCapitaHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.AutorizacionesIngresoEstanciaHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.CentroCostoViaIngresoHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.CuentasHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.DiagnosticosHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.EstratoSocialHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.ExcepcionesNaturalezaHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.IngresosEstanciaHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.IngresosHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.NaturalezaPacienteHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.PacientesHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.SubCuentasHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.TiposAfiliadoHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.TiposPacienteHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.TiposRegimenHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.ViasIngresoHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.agendaOdontologica.CitaOdontologicaHibernateDAO;
import com.servinte.axioma.dao.interfaz.inventario.IAurorizacionesEntSubCapitacionDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAmparosPorReclamarDao;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutoCapiXCentroCostoDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesCapitacionSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubRipsDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubServiDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEstanciaCapitaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesIngresoEstanciaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ICentroCostoViaIngresoDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ICuentasDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IDiagnosticosDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IEstratoSocialDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IExcepcionesNaturalezaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosEstanciaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.INaturalezaPacienteDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ISubCuentasDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposAfiliadoDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposPacienteDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposRegimenDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IViasIngresoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaDAO;

/**
 * Fabrica para construir objetos DAO para la l&oacute;gica
 * de ManejoPaciente
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.dao.interfaz.tesoreria.ITiposMovimientoCajaDAO
 */

public class ManejoPacienteDAOFabrica {

	
	public ManejoPacienteDAOFabrica() {	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IPacientesDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IPacientesDAO}.
	 */
	public static IPacientesDAO crearPacientesDAO(){
		return new PacientesHibernateDAO();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * INaturalezaPacienteDAO
	 * 
	 * @return INaturalezaPacienteDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INaturalezaPacienteDAO crearNaturalezaPacienteDAO(){
		return new NaturalezaPacienteHibernateDAO();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * IExcepcionesNaturalezaDAO
	 * 
	 * @return IExcepcionesNaturalezaDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IExcepcionesNaturalezaDAO crearExcepcionNaturalezaDAO(){
		return new ExcepcionesNaturalezaHibernateDAO();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * ITiposRegimenDAO
	 * 
	 * @return ITiposRegimenDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposRegimenDAO crearTipoRegimenDAO(){
		return new TiposRegimenHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IEstratoSocialDAO
	 * 
	 * @return IEstratoSocialDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IEstratoSocialDAO crearEstratoSocialDAO(){
		return new EstratoSocialHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ITiposAfiliadoDAO
	 * @return ITiposAfiliadoDAO
	 * @author, Angela Maria Aguirre
	 */
	public static ITiposAfiliadoDAO crearTiposAfiliadoDAO(){
		return new TiposAfiliadoHibernateDAO();
	}
	
	/**
	 * Este m&eacute;todo se encarga de 
	 * @return
	 * @author Yennifer Guerrero
	 */
	public static ICitaOdontologicaDAO crearCitaOdontologicaDAO(){
		return new CitaOdontologicaHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IIngresosDAO}.
	 * @return objeto que es implementaci&oacute;n de {@link IIngresosDAO}.
	 */
	public static IIngresosDAO crearinIngresosDAO(){
		return new IngresosHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IViasIngresoDAO
	 * @return IViasIngresoDAO
	 * @author, Angela Maria Aguirre
	 */
	public static IViasIngresoDAO crearViasIngresoDAO(){
		return new ViasIngresoHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ITiposPacienteDAO
	 * @return ITiposPacienteDAO
	 * @author, Angela Maria Aguirre
	 */
	public static ITiposPacienteDAO crearTiposPacienteDAO(){
		return new TiposPacienteHibernateDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase ICuentasDAO
	 * @return ICuentasDAO
	 * @author, Cristhian Murillo
	 */
	public static ICuentasDAO crearCuentasDAO(){
		return new CuentasHibernateDAO();
	}

	/**
	 * Este Método se encarga de crear una instancia de la clase ICuentasDAO
	 * @return ICuentasDAO
	 * @author, Cristhian Murillo
	 */
	public static ISubCuentasDAO crearSubCuentasDAO(){
		return new SubCuentasHibernateDAO();
	}

	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesEntidadesSubDAO
	 * @return IAutorizacionesEntidadesSubDAO
	 * @author, Cristhian Murillo
	 */
	public static IAutorizacionesEntidadesSubDAO crearAutorizacionesEntidadesSubDAO(){
		return new AutorizacionesEntidadesSubHibernateDAO();
	}

	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAurorizacionesEntSubCapitacionHibernateDAO
	 * @return IAurorizacionesEntSubCapitacionHibernateDAO
	 * @author, Cristhian Murillo
	 */
	public static IAurorizacionesEntSubCapitacionDAO crearAurorizacionesEntSubCapitacion(){
		return new AurorizacionesEntSubCapitacionHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesEntSubServiDAO
	 * @return IAutorizacionesEntSubServiDAO
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntSubServiDAO crearAurorizacionEntidadSubServi(){
		return new AutorizacionesEntSubServiHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesEntSubArticuloDAO
	 * @return IAutorizacionesEntSubArticuloDAO
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntSubArticuloDAO crearAurorizacionEntidadSubArticulo(){
		return new AutorizacionesEntSubArticuloHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesCapitacionSubDAO
	 * @return IAutorizacionesCapitacionSubDAO
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesCapitacionSubDAO crearAutorizacionCapitacion(){
		return new AutorizacionesCapitacionSubHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesEstanciaCapitaDAO
	 * @return IAutorizacionesEstanciaCapitaDAO
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEstanciaCapitaDAO crearAutorizacionesEstanciaCapita(){
		return new AutorizacionesEstanciaCapitaHibernateDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IHistoAutorizacionCapitaSubDAO
	 * @return IHistoAutorizacionCapitaSubDAO
	 * @author Angela Aguirre
	 */
	public static IHistoAutorizacionCapitaSubDAO crearHistoAutorizacionCapitaSub(){
		return new HistoAutorizacionCapitaSubHibernateDAO();
	}
	
	/**
	 * Este Metodo se encarga de crear una instancia de la clase IAutoCapiXCentroCostoDAO
	 * @return IAutoCapiXCentroCostoDAO
	 * @author Diana Ruiz
	 */
	
	public static IAutoCapiXCentroCostoDAO crearAutoCapiXCentroCosto(){
		return new AutoCapiXCentroCostoHibernateDAO();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesIngresoEstanciaDAO
	 * @return IAutorizacionesIngresoEstanciaDAO
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesIngresoEstanciaDAO crearAutorizacionesIngresoEstancia(){
		return new AutorizacionesIngresoEstanciaHibernateDAO();
	}	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IIngresosEstanciaDAO
	 * @return IIngresosEstanciaDAO
	 * @author Angela Aguirre
	 */
	public static IIngresosEstanciaDAO crearIngresosEstancia(){
		return new IngresosEstanciaHibernateDAO();
	}
	
	/**
	 * M&eacute;todo encargado de crear una instancia de la clase ICentroCostoViaIngresoDAO
	 * @return ICentroCostoViaIngresoDAO
	 * @author Diana Carolina G
	 */
	public static ICentroCostoViaIngresoDAO crearCentroCostoViaIngreso(){
		return new CentroCostoViaIngresoHibernateDAO();
	}
	
	/**
	 * M&eacute;todo encargado de crear una instancia de la clase IAmparosPorReclamarDao
	 * @return IAmparosPorReclamarDao
	 * @author Jorge Armando Osorio V.
	 */
	public static IAmparosPorReclamarDao crearAmparosPorReclamarDao() 
	{
		return new AmparosPorReclamarHibernateDao();
	}
	
	/**
	 * Método encargado de crear una instancia de la clase IDiagnosticosDAO
	 * @return DiagnosticosHibernateDAO
	 * @author Fabián Becerra
	 */
	public static IDiagnosticosDAO crearDiagnosticosDAO(){
		return new DiagnosticosHibernateDAO();
	}
	
	/**
	 * Método encargado de crear una instancia de la clase IAutorizacionesEntSubRipsDAO
	 * @return AutorizacionesEntSubRipsHibernateDAO
	 * @author Fabián Becerra
	 */
	public static IAutorizacionesEntSubRipsDAO crearAutorizacionesEntSubRipsDAO(){
		return new AutorizacionesEntSubRipsHibernateDAO();
	}
	
}
