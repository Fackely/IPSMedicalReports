package com.servinte.axioma.dao.fabrica;

import com.servinte.axioma.dao.impl.administracion.BarriosHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.CentroAtencionHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.CiudadesHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.ConsecutivosSistemaDAO;
import com.servinte.axioma.dao.impl.administracion.EspecialidadesHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.InstitucionesHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.LocalidadesHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.LogProcesoInactivacionUsuHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.MedicosHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.OcupacionesMedicasHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.PaisesHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.ParametrizacionSemaforizacionDAO;
import com.servinte.axioma.dao.impl.administracion.PersonasHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.RegionesCoberturaHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.RolesFuncionalidadesHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.TiposIdentificacionHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.UsuarioDAO;
import com.servinte.axioma.dao.impl.facturacion.TiposServicioHibernateDAO;
import com.servinte.axioma.dao.impl.facturacion.convenio.ConvenioDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.PacientesHibernateDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.ViasIngresoHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadesConsultaHibernateDAO;
import com.servinte.axioma.dao.impl.ordenes.SolicitudesHibernateDAO;
import com.servinte.axioma.dao.interfaz.administracion.IBarriosDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICiudadesDAO;
import com.servinte.axioma.dao.interfaz.administracion.IConsecutivosSistemaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IEspecialidadesDAO;
import com.servinte.axioma.dao.interfaz.administracion.IInstitucionesDAO;
import com.servinte.axioma.dao.interfaz.administracion.ILocalidadesDAO;
import com.servinte.axioma.dao.interfaz.administracion.ILogProcesoInactivacionUsuDAO;
import com.servinte.axioma.dao.interfaz.administracion.IMedicosDAO;
import com.servinte.axioma.dao.interfaz.administracion.IOcupacionesMedicasDAO;
import com.servinte.axioma.dao.interfaz.administracion.IPaisesDAO;
import com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO;
import com.servinte.axioma.dao.interfaz.administracion.IPersonasDAO;
import com.servinte.axioma.dao.interfaz.administracion.IRegionesCoberturaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IRolesFuncionalidadesDAO;
import com.servinte.axioma.dao.interfaz.administracion.ITiposIdentificacionDAO;
import com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposServicioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IViasIngresoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;

/**
 * Fabrica para construir objetos DAO para la l&oacute;gica referente a las
 * funcionalidades de Administraci&oacute;n.
 * 
 * @author Fernando Ocampo
 * @see com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO
 */
public abstract class AdministracionFabricaDAO {

	private AdministracionFabricaDAO() {
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IUsuarioDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IUsuarioDAO}.
	 */
	public static IUsuarioDAO crearUsuarioDAO() {
		return new UsuarioDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link ITiposIdentificacionDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de
	 *         {@link ITiposIdentificacionDAO}.
	 */
	public static ITiposIdentificacionDAO crearTiposIdentificacionDAO() {
		return new TiposIdentificacionHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IPaisesDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IPaisesDAO}.
	 */
	public static IPaisesDAO crearPaisesDAO() {
		return new PaisesHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICiudadesDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICiudadesDAO}.
	 */
	public static ICiudadesDAO crearciCiudadesDAO() {
		return new CiudadesHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IRegionesCoberturaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de
	 *         {@link IRegionesCoberturaDAO}.
	 */
	public static IRegionesCoberturaDAO crearregCoberturaDAO() {
		return new RegionesCoberturaHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IInstitucionesDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IInstitucionesDAO}.
	 */
	public static IInstitucionesDAO crearInstitucionesDAO() {
		return new InstitucionesHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICentroAtencionDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICentroAtencionDAO}
	 *         .
	 */
	public static ICentroAtencionDAO crearCentroAtencionDAO() {
		return new CentroAtencionHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IRolesFuncionalidadesDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de
	 *         {@link IRolesFuncionalidadesDAO}.
	 */
	public static IRolesFuncionalidadesDAO crearRolesFuncionalidadesDAO() {
		return new RolesFuncionalidadesHibernateDAO();
	}

	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IMedicosDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IMedicosDAO}.
	 */
	public static IMedicosDAO creaMedicosHibernateDAO() {
		return new MedicosHibernateDAO();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad
	 * IEspecialidadesDAO
	 * 
	 * @return IEspecialidadesDAO
	 * @author, Angela Aguirre
	 * 
	 */
	public static IEspecialidadesDAO crearEspecialidadesDAO() {
		return new EspecialidadesHibernateDAO();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad
	 * IPersonasDAO
	 * 
	 * @return IPersonasDAO
	 * @author Juan David Ramírez
	 * 
	 */
	public static IPersonasDAO crearPersonasDao() {
		return new PersonasHibernateDAO();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad
	 * IOcupacionesMedicasDAO
	 * 
	 * @return IOcupacionesMedicasDAO
	 * @author, Angela Maria Aguirre
	 * 
	 */
	public static IOcupacionesMedicasDAO crearOcupacionesMedicasDao() {
		return new OcupacionesMedicasHibernateDAO();
	}

	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * ILogProcesoInactivacionUsuDAO
	 * 
	 * @return LogProcesoInactivacionUsuHibernateDAO
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 15/01/2011
	 */
	public static ILogProcesoInactivacionUsuDAO crearLogProcesoInactivacionUsuarioDAO() {
		return new LogProcesoInactivacionUsuHibernateDAO();
	}

	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IConsecutivosSistemaDAO
	 * 
	 * @return IConsecutivosSistemaDAO
	 * 
	 * @author Juan David Ramírez
	 * @since 18 Ene 2011
	 */
	public static IConsecutivosSistemaDAO crearConsecutivosSistemaDAO() {
		return new ConsecutivosSistemaDAO();
	}

	/**
	 * Metodo que trae el DAO Concreto
	 * 
	 * @return IParametrizacionSemaforizacionDAO
	 */
	public static IParametrizacionSemaforizacionDAO crearParametrizacionSemaforizacionDAO() {
		return new ParametrizacionSemaforizacionDAO();
	}

	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         ConvenioDAO
	 * @return IConvenioDAO
	 */
	public static IConvenioDAO crearConvenioDAO() {
		return new ConvenioDAO();
	}

	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         TiposServicioHibernateDAO
	 * @return ITiposServicioDAO
	 */
	public static ITiposServicioDAO crearTipoServicioDAO(){
		return new TiposServicioHibernateDAO();
	}
	
	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         UnidadesConsultaHibernateDAO
	 * @return IUnidadesConsultaDAO
	 */
	public static IUnidadesConsultaDAO crearUnidadesConsultaDAO(){
		return new UnidadesConsultaHibernateDAO();
	}
	
	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         TiposIdentificacionHibernateDAO
	 * @return ITiposIdentificacionDAO
	 */
	public static ITiposIdentificacionDAO crearTipoIdentificacionDAO(){
		return new TiposIdentificacionHibernateDAO();
	}
	
	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         ViasIngresoHibernateDAO
	 * @return IViasIngresoDAO
	 */
	public static IViasIngresoDAO crearViasIngresoDAO(){
		return new ViasIngresoHibernateDAO();
	}
	
	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         PacientesHibernateDAO
	 * @return IPacientesDAO
	 */
	public static IPacientesDAO crearPacientesDAO(){
		return new PacientesHibernateDAO();
	}
	
	/**
	 * @author Cesar Gomez Metodo que crea la instancia de la clase
	 *         SolicitudesHibernateDAO
	 * @return ISolicitudesDAO
	 */
	public static ISolicitudesDAO crearSolicitudesDAO(){
		return new SolicitudesHibernateDAO();
	}
	
	
	
	/**
	 * @author Ricardo Ruiz Metodo que crea la instancia de la clase
	 *         BarriosHibernateDAO
	 * @return IBarriosDAO
	 */
	public static IBarriosDAO crearBarriosDAO() {
		return new BarriosHibernateDAO();
	}
	/**
	 * @author Ricardo Ruiz Metodo que crea la instancia de la clase
	 *         LocalidadesHibernateDAO
	 * @return IBarriosDAO
	 */
	public static ILocalidadesDAO crearLocalidadesDAO() {
		return new LocalidadesHibernateDAO();
	}

}
