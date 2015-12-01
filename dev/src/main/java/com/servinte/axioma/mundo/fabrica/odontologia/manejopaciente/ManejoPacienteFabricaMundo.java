package com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente;

import com.servinte.axioma.mundo.impl.manejoPaciente.AmparosPorReclamarMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AurorizacionesEntSubCapitacionMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AutorizacionCapitacionSubMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AutorizacionIngresoEstanciaMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AutorizacionesEntSubArticuloMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AutorizacionesEntSubRipsMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AutorizacionesEntSubServiMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.AutorizacionesEntidadesSubMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.CentroCostoViaIngresoMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.CuentasMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.DiagnosticosMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.EstratoSocialMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.ExcepcionesNaturalezaMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.IngresosEstanciaMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.IngresosMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.NaturalezaPacienteMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.PacientesMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.ProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.SubCuentasMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.TiposAfiliadoMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.TiposPacienteMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.TiposRegimenMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.ValidacionGeneracionAutorizacionCapitadaMundo;
import com.servinte.axioma.mundo.impl.manejoPaciente.ViasIngresoMundo;
import com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica.CitaOdontologicaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAmparosPorReclamarMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAurorizacionesEntSubCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionCapitacionSubMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubRipsMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubServiMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntidadesSubMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICentroCostoViaIngresoMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICuentasMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IDiagnosticosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IEstratoSocialMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IExcepcionesNaturalezaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosEstanciaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.INaturalezaPacienteMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ISubCuentasMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposAfiliadoMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposPacienteMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposRegimenMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IValidacionGeneracionAutorizacionCapitadaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IViasIngresoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionesEntSubArticuloServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionesEntSubServiServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubServiServicio;

/**
 * Fabrica para contruir objetos para ManejoPaciente
 * @author Cristhian Murillo
 *
 */
public abstract class ManejoPacienteFabricaMundo {
	
	private ManejoPacienteFabricaMundo(){}
	
	
	/**
	 * Crea un PacientesMundo
	 * @return
	 */
	public static IPacientesMundo crearPacientesMundo()
	{
		return new PacientesMundo();
	}
	
	
	/**
	 * Crea un IngresosMundo
	 * @return
	 */
	public static IIngresosMundo crearIngresosMundo()
	{
		return new IngresosMundo();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una Instancia de la clase 
	 * INaturalezaPacienteMundo
	 * 
	 * @return INaturalezaPacienteMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INaturalezaPacienteMundo crearNaturalezaPacienteMundo(){
		return new NaturalezaPacienteMundo();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una Instancia de la clase 
	 * INaturalezaPacienteMundo
	 * 
	 * @return INaturalezaPacienteMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IExcepcionesNaturalezaMundo crearExcepcionNaturalezaMundo(){
		return new ExcepcionesNaturalezaMundo();
	}
	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una Instancia de la clase 
	 * ITiposRegimenMundo
	 * 
	 * @return ITiposRegimenMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposRegimenMundo crearTipoRegimenMundo(){
		return new TiposRegimenMundo();
	}
	

	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IEstratoSocialMundo
	 * 
	 * @return IEstratoSocialMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IEstratoSocialMundo crearEstratoSocialMundo(){
		return new EstratoSocialMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ITiposAfiliadoMundo
	 * 
	 * @return ITiposAfiliadoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposAfiliadoMundo crearTiposAfiliadoMundo(){
		return new TiposAfiliadoMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IIngresosMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IIngresosMundo}.
	 */
	public static IIngresosMundo crearinIngresosMundo(){
		return new IngresosMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IViasIngresoMundo
	 * 
	 * @return IViasIngresoMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IViasIngresoMundo crearViasIngresoMundo(){
		return new ViasIngresoMundo();
	}	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ITiposPacienteMundo
	 * 
	 * @return ITiposPacienteMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposPacienteMundo crearTiposPacienteMundo(){
		return new TiposPacienteMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ICitaOdontologicaMundo
	 * 
	 * @return ICitaOdontologicaMundo
	 */
	public static ICitaOdontologicaMundo crearCitaOdontologicaMundo(){
		return new CitaOdontologicaMundo();
	}
	
	
	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase ICuentasMundo
	 * 
	 * @return ICuentasMundo
	 * @author, Cristhian Murillo
	 */
	public static ICuentasMundo crearCuentasMundo()
	{
		return new CuentasMundo();
	}
	
	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase ISubCuentasMundo
	 * 
	 * @return ISubCuentasMundo
	 * @author, Cristhian Murillo
	 */
	public static ISubCuentasMundo crearSubCuentasMundo()
	{
		return new SubCuentasMundo();
	}
	
	
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IAutorizacionesEntidadesSubMundo
	 * 
	 * @return IAutorizacionesEntidadesSubMundo
	 * @author, Cristhian Murillo
	 */
	public static IAurorizacionesEntSubCapitacionMundo crearAutorizacionesEntidadesSubMundo()
	{
		return new AurorizacionesEntSubCapitacionMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IProcesoGeneracionAutorizacionMundo
	 * 
	 * @return IAutorizacionesEntidadesSubMundo
	 * @author Angela Aguirre
	 */
	public static IProcesoGeneracionAutorizacionMundo crearProcesoGeneracionAutorizacionMundo()
	{
		return new ProcesoGeneracionAutorizacionMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionIngresoEstanciaMundo
	 * 
	 * @return IAutorizacionIngresoEstanciaMundo
	 * @author Angela Aguirre
	 */
	public static IAutorizacionIngresoEstanciaMundo crearAutorizacionIngresoEstanciaMundo(){
		return new AutorizacionIngresoEstanciaMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionCapitacionSubMundo
	 * 
	 * @return IAutorizacionCapitacionSubMundo
	 * @author Angela Aguirre
	 */
	public static IAutorizacionCapitacionSubMundo crearAutorizacionCapitacionSubMundo(){
		return new AutorizacionCapitacionSubMundo();
	}
	
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionesEntidadesSubMundo
	 * 
	 * @return IAutorizacionesEntidadesSubMundo
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntidadesSubMundo crearAutorizacionEntidadesSubMundo(){
		return new AutorizacionesEntidadesSubMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionesEntSubServiMundo
	 * 
	 * @return IAutorizacionesEntSubServiMundo
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntSubServiMundo crearAutorizacionesEntSubServiMundo(){
		return new AutorizacionesEntSubServiMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionesEntSubArticuloMundo
	 * 
	 * @return IAutorizacionesEntSubArticuloMundo
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntSubArticuloMundo crearAutorizacionesEntSubArticuloMundo(){
		return new AutorizacionesEntSubArticuloMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionesEntSubServiServicio
	 * 
	 * @return IAutorizacionesEntSubServiServicio
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntSubServiServicio crearAutorizacionesEntSubServiServicio(){
		return new AutorizacionesEntSubServiServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionesEntSubArticuloServicio
	 * 
	 * @return IAutorizacionesEntSubArticuloServicio
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntSubArticuloServicio crearAutorizacionesEntSubArticuloServicio(){
		return new AutorizacionesEntSubArticuloServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la clase IIngresosEstanciaMundo
	 * @return IIngresosEstanciaMundo
	 * @author Angela Aguirre
	 */
	public static IIngresosEstanciaMundo crearIngresosEstancia(){
		return new IngresosEstanciaMundo();
	}
	
	/**
	 * M&eacutetodo encargado de crear una instancia de la clase ICentroCostoViaIngresoMundo
	 * @return ICentroCostoViaIngresoMundo
	 * @author Diana Carolina G
	 */
	public static ICentroCostoViaIngresoMundo crearCentroCostoViaIngresoMundo(){
		return new CentroCostoViaIngresoMundo();
	}

	/**
	 * M&eacutetodo encargado de crear una instancia de la clase IAmparosPorReclamarMundo
	 * @return IAmparosPorReclamarMundo
	 * @author Diana Carolina G
	 */
	public static IAmparosPorReclamarMundo crearAmparosPorReclamarMundo() {
		return new AmparosPorReclamarMundo();
	}
	
	/**
	 * M&eacutetodo encargado de crear una instancia de la clase IDiagnosticosMundo
	 * @return DiagnosticosMundo
	 * @author Fabián Becerra
	 */
	public static IDiagnosticosMundo crearDiagnosticosMundo(){
		return new DiagnosticosMundo();
	}
	
	/**
	 * M&eacutetodo encargado de crear una instancia de la clase IAutorizacionesEntSubRipsMundo
	 * @return AutorizacionesEntSubRipsMundo
	 * @author Fabián Becerra
	 */
	public static IAutorizacionesEntSubRipsMundo crearAutorizacionesEntSubRipsMundo(){
		return new AutorizacionesEntSubRipsMundo();
	}

	/**
	 * Este método se encarga de crear una instancia para
	 * la entidad IValidacionGeneracionAutorizacionCapitadaMundo
	 * @return ValidacionGeneracionAutorizacionCapitadaMundo
	 */
	public static IValidacionGeneracionAutorizacionCapitadaMundo crearValidacionGeneracionAutorizacionCapitadaMundo(){
		return new ValidacionGeneracionAutorizacionCapitadaMundo();
	}
	
}
