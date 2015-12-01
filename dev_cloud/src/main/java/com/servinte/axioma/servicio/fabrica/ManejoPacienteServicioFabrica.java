package com.servinte.axioma.servicio.fabrica;

import com.servinte.axioma.servicio.impl.AutorizacionCapitacionSubServicio;
import com.servinte.axioma.servicio.impl.inventario.AurorizacionesEntSubCapitacionServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AmparosPorReclamarServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionIngresoEstanciaServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionesEntSubArticuloServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionesEntSubRipsServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionesEntSubServiServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.AutorizacionesEntidadesSubServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.CentroCostoViaIngresoServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.CuentasServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.DiagnosticosServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.EstratoSocialServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.ExcepcionesNaturalezaServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.IngresosEstanciaServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.IngresosServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.NaturalezaPacienteServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.SubCuentasServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.TiposAfiliadoServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.TiposPacienteServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.TiposRegimenServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.ViasIngresoServicio;
import com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica.CitaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAmparosPorReclamarServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionCapitacionSubServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubRipsServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubServiServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ICentroCostoViaIngresoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ICuentasServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IDiagnosticosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IEstratoSocialServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IExcepcionesNaturalezaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.INaturalezaPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ISubCuentasServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposAfiliadoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposRegimenServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;

/**
 * Esta clase se encarga de construir los objetos relacionados
 * a Manejo Paciente
 *  
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
public class ManejoPacienteServicioFabrica {
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la 
	 * entidad INaturalezaPacienteServicio
	 * 
	 * @return INaturalezaPacienteServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INaturalezaPacienteServicio crearNaturalezaPacienteServicio(){
		return new NaturalezaPacienteServicio();
	}
	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la 
	 * entidad IExcepcionesNaturalezaServicio
	 * 
	 * @return IExcepcionesNaturalezaServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IExcepcionesNaturalezaServicio crearExcepcionNaturalezaServicio(){
		return new ExcepcionesNaturalezaServicio();
	}
	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la 
	 * entidad ITiposRegimenServicio
	 * 
	 * @return ITiposRegimenServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposRegimenServicio crearTipoRegimenServicio(){
		return new TiposRegimenServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IEstratoSocialServicio
	 * 
	 * @return IEstratoSocialServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IEstratoSocialServicio crearEstratoSocialServicio(){
		return new EstratoSocialServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ITiposAfiliadoServicio
	 * 
	 * @return ITiposAfiliadoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposAfiliadoServicio crearTiposAfiliadoServicio(){
		return new TiposAfiliadoServicio();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IIngresosServicio}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IIngresosServicio}.
	 */
	public static IIngresosServicio crearIngresosServicio(){
		return new IngresosServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IViasIngresoServicio
	 * 
	 * @return IViasIngresoServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IViasIngresoServicio crearViasIngresoServicio(){
		return new ViasIngresoServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * ITiposPacienteServicio
	 * 
	 * @return ITiposPacienteServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ITiposPacienteServicio crearTiposPacienteServicio(){
		return new TiposPacienteServicio();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de {@link ICuentasServicio}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICuentasServicio}.
	 * @author, Cristhian Murillo
	 */
	public static ICuentasServicio crearCuentasServicio(){
		return new CuentasServicio();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de {@link ISubCuentasServicio}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ISubCuentasServicio}.
	 * @author, Cristhian Murillo
	 */
	public static ISubCuentasServicio crearSubCuentasServicio(){
		return new SubCuentasServicio();
	}
	
	
	/**
	 * Este M&eacute;todo se encarga de crear una instancia de la 
	 * entidad ICitaOdontologicaServicio
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICitaOdontologicaServicio}.
	 * @author, Fabian Becerra
	 * @author, Wilson Gomez
	 * @author, Javier Gonzalez
	 */
	public static ICitaOdontologicaServicio crearCitaOdontologicaServicio(){
		return new CitaOdontologicaServicio();
	}
	
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de {@link IAurorizacionesEntSubCapitacionServicio}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IAurorizacionesEntSubCapitacionServicio}.
	 * @author, Cristhian Murillo
	 */
	public static IAurorizacionesEntSubCapitacionServicio crearAurorizacionesEntSubCapitacionServicio(){
		return new AurorizacionesEntSubCapitacionServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IAutorizacionIngresoEstanciaServicio
	 * 
	 * @return IAutorizacionIngresoEstanciaServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IAutorizacionIngresoEstanciaServicio crearAutorizacionIngresoEstanciaServicio(){
		return new AutorizacionIngresoEstanciaServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia de la clase
	 * IAutorizacionIngresoEstanciaServicio
	 * 
	 * @return IAutorizacionIngresoEstanciaServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IAutorizacionCapitacionSubServicio crearAutorizacionCapitacionSubServicio(){
		return new AutorizacionCapitacionSubServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de IAutorizacionesEntidadesSubServicio
	 * 
	 * @return IAutorizacionesEntidadesSubServicio
	 * @author Angela Aguirre
	 */
	public static IAutorizacionesEntidadesSubServicio crearAutorizacionEntidadesSubServicio(){
		return new AutorizacionesEntidadesSubServicio();
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
	public static IAutorizacionesEntSubArticuloServicio crearAutorizacionesEntSubArticulo(){
		return new AutorizacionesEntSubArticuloServicio();
	}

	
	/**
	 * Este Método se encarga de crear una instancia de la clase IIngresosEstanciaServicio
	 * @return IIngresosEstanciaServicio
	 * @author Angela Aguirre
	 */
	public static IIngresosEstanciaServicio crearIngresosEstancia(){
		return new IngresosEstanciaServicio();
	}
	
	/**
	 * M&eacute;todo encargado de crear una instancia de la clase ICentroCostoViaIngresoServicio
	 * @return ICentroCostoViaIngresoServicio
	 * @author Diana Carolina G
	 */
	public static ICentroCostoViaIngresoServicio crearCentroCostoViaIngreso(){
		return new CentroCostoViaIngresoServicio();
	}


	/**
	 * M&eacute;todo encargado de crear una instancia de la clase IAmparosPorReclamarServicio
	 * @return IAmparosPorReclamarServicio
	 * @author Jorge Armando Osorio V.
	 */
	public static IAmparosPorReclamarServicio crearAmparosPorReclamarServicio() {
		return new AmparosPorReclamarServicio();
	}
	
	
	/**
	 * Método encargado de crear una instancia de la clase IDiagnosticosServicio
	 * @return DiagnosticosServicio
	 * @author Fabián Becerra
	 */
	public static IDiagnosticosServicio crearDiagnosticosServicio(){
		return new DiagnosticosServicio();
	}
	
	
	/**
	 * Método encargado de crear una instancia de la clase IAutorizacionesEntSubRipsServicio
	 * @return AutorizacionesEntSubRipsServicio
	 * @author Fabián Becerra
	 */
	public static IAutorizacionesEntSubRipsServicio crearIAutorizacionesEntSubRipsServicio(){
		return new AutorizacionesEntSubRipsServicio();
	}
	

}
