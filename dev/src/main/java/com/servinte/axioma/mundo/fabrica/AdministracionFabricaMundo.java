package com.servinte.axioma.mundo.fabrica;

import com.servinte.axioma.mundo.impl.administracion.BarriosMundo;
import com.servinte.axioma.mundo.impl.administracion.CentroAtencionMundo;
import com.servinte.axioma.mundo.impl.administracion.CentroCostoMundo;
import com.servinte.axioma.mundo.impl.administracion.ConsecutivosSistemaMundo;
import com.servinte.axioma.mundo.impl.administracion.EspecialidadesMundo;
import com.servinte.axioma.mundo.impl.administracion.InstitucionesMundo;
import com.servinte.axioma.mundo.impl.administracion.LocalizacionMundo;
import com.servinte.axioma.mundo.impl.administracion.MedicosMundo; 
import com.servinte.axioma.mundo.impl.administracion.OcupacionesMedicasMundo;
import com.servinte.axioma.mundo.impl.administracion.ParametrizacionSemaforizacionMundo;
import com.servinte.axioma.mundo.impl.administracion.PersonasMundo;
import com.servinte.axioma.mundo.impl.administracion.ProcesosInactivacionUsuarioCaducidadPassword;
import com.servinte.axioma.mundo.impl.administracion.RolesFuncionalidadesMundo;
import com.servinte.axioma.mundo.impl.administracion.TiposIdentificacionMundo;
import com.servinte.axioma.mundo.impl.administracion.UsuariosMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.HistObserGenerPacienteMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.PacientesMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.PacientesPoliconsultadoresMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IBarriosMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IConsecutivosSistemaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IEspecialidadesMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IMedicosMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IOcupacionesMedicasMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IPersonas;
import com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword;
import com.servinte.axioma.mundo.interfaz.administracion.IRolesFuncionalidadesMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ITiposIdentificacionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IHistObserGenerPacienteMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesPoliconsultadoresMundo;

/**
 * Fabrica para la creaci&oacute;n de objetos del mundo
 * que contienen l&oacute;gica referente a las funcionalidades de Administraci&oacute;n.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class AdministracionFabricaMundo {

	public AdministracionFabricaMundo() {}
	
	
	/**
	 * Fabrica para Usuarios
	 * @return
	 */
	public static IUsuariosMundo crearUsuariosMundo()
	{
		return new UsuariosMundo();
	}
	
	
	/**
	 * Fabrica para tipos de Identificacion
	 */
	public static ITiposIdentificacionMundo crearTiposIdentificacionMundo()
	{
		return new TiposIdentificacionMundo();
	}
	
	
	/**
	 * Fabrica para localizacion
	 */
	public static ILocalizacionMundo crearloILocalizacionMundo()
	{
		return new LocalizacionMundo();
	}
	
	
	
	/**
	 * Fabrica para RolesFuncionalidades
	 */
	public static IRolesFuncionalidadesMundo crearloroRolesFuncionalidadesMundo()
	{
		return new RolesFuncionalidadesMundo();
	}
	
	/**
	 * Este m&eacute;todo se encarga de crear la fabrica para  CentroAtencionMundo
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public static ICentroAtencionMundo crearCentroAtencionMundo(){
		return new CentroAtencionMundo();
	}
	
	/**
	 * Este m&eacute;todo se encarga de crear la fabrica para  IMedicosMundo
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public static IMedicosMundo crearMedicosMundo(){
		return new MedicosMundo();
	}
	
	
	
	/**
	 * 	CREAR CENTRO DE COSTOS MUNDO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final  ICentroCostoMundo crearCentroCostoMundo()
	{	return new CentroCostoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEspecialidadesMundo
	 * 
	 * @return IEspecialidadesMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IEspecialidadesMundo crearEspecialidadesMundo(){
		return new EspecialidadesMundo();
	}


	/**
	 * Crear instancia concreta de IPersonas
	 * @return Instancia concreta
	 */
	public static IPersonas crearPersonasMundo() {
		return new PersonasMundo();
	}
	
	
	/**
	 * crear instancia paciente mundo
	 * @return
	 */
	public static final IPacientesMundo crearPacienteMundo(){
		return new PacientesMundo();
		
	}
	
	
	
	/**
	 * crear instancia Instituciones mundo
	 * @return
	 */
	public static final IInstitucionesMundo crearInstitucionesMundo(){
		return new InstitucionesMundo();
	}
	
	
	/**
	 * crear instancia de historal observacion general paciente mundo
	 * @return
	 */
	public static final IHistObserGenerPacienteMundo crearHistObservacionGenerPacieneMundo(){
		return new HistObserGenerPacienteMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la 
	 * entidad IOcupacionesMedicasMundo
	 * 
	 * @return IOcupacionesMedicasMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IOcupacionesMedicasMundo crearOcupacionesMedicasMundo() {
		return new OcupacionesMedicasMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la 
	 * entidad IProcesosInactivacionUsuarioCaducidadPassword
	 * 
	 * @return IProcesosInactivacionUsuarioCaducidadPassword
	 *
	 */
	public static IProcesosInactivacionUsuarioCaducidadPassword crearProcesosInactivacionUsuarioCaducidadPassword() {
		return new ProcesosInactivacionUsuarioCaducidadPassword();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la 
	 * entidad IConsecutivosSistemaMundo
	 * 
	 * @return IConsecutivosSistemaMundo
	 *
	 */
	public static IConsecutivosSistemaMundo crearConsecutivosSistemaMundo()
	{
		return new ConsecutivosSistemaMundo();
	}
	
	/**
	 *  Método para construir la instancia de ParametrizacionSemaforizacionMundo
	 * @return {@link IParametrizacionSemaforizacionMundo} Instancia concreta
	 */
	public static IParametrizacionSemaforizacionMundo crearParametrizacionMundo(){
		return new ParametrizacionSemaforizacionMundo();
	}
	
	/**
	 *  Método para construir la instancia de PacientesPoliconsultadoresMundo
	 * @return {@link IPacientesPoliconsultadoresMundo} Instancia concreta
	 */
	public static IPacientesPoliconsultadoresMundo crearPacientesPoliconsutadores(){
		return new PacientesPoliconsultadoresMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la 
	 * entidad IBarriosMundo
	 * 
	 * @return IBarriosMundo
	 *
	 */
	public static IBarriosMundo crearBarriosMundo()
	{
		return new BarriosMundo();
	}
}
