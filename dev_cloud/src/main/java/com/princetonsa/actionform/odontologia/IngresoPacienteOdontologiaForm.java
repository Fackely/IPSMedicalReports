package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoMotivoCitaPaciente;
import com.princetonsa.dto.odontologia.DtoRolesTipoDeUsuario;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;


/**
 * Contiene toda la informacion a mostrar en el formulario de ingreso del paciente  
 * @author axioma
 */
@SuppressWarnings("rawtypes")
public class IngresoPacienteOdontologiaForm extends ValidatorForm 
{
	
	/*
	#############################################################
	#	ATRIBUTOS												#			
	#############################################################
	*/
	private static final long serialVersionUID = 1L;
	private String estado;
	private DtoPaciente paciente;
	private DtoMotivoCitaPaciente motivoCitaPaciente;
	private ArrayList<DtoBeneficiarioPaciente> arrayBeneficiarios;
	private int posBeneficiario;
	private DtoPlantilla plantilla;
	private boolean existePaciente;
	private String tipoFuncionalidad;
	private String urlRetorno;
	private String operacionExito;
	private String mensaje;
	private String operacionExitoBenef;
	private String mensajeBenef;
	private String ocultarCabezotes;
	private String tipoIdenPac;
	private String numeroIdenPac;
	private boolean existeComoUsuario=false;
	
	//Arreglos que tienen estructuras de datos para el ingreso del paciente
	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudades = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudadesExp = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudadesNac = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> localidades = new ArrayList<HashMap<String,Object>>();
	private HashMap zonasDomicilio = new HashMap();
	private HashMap ocupaciones = new HashMap();
	private ArrayList<HashMap<String,Object>> sexos = new ArrayList<HashMap<String,Object>>();
	private HashMap tiposSangre = new HashMap();
	private HashMap estadosCiviles = new HashMap();
	private HashMap centrosAtencion = new HashMap();
	private HashMap tiposIdentificacion = new HashMap();
	private ArrayList<HashMap<String, Object>> motivosCita = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> mediosConocimiento = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> etnias = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> religiones = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> estudios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> pacientesMismosNombres = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> parentezcos = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<ElementoApResource> mensajesAlerta = new ArrayList<ElementoApResource>();
	// ------------------------------
	
	//Atributos para el manejo de validacion de pacientes con mismos nombres
	private boolean avisoValidacionMismosNombres;
	private boolean preguntoContinuar;
	private String cerrarPopupNormal;
	private DtoPerfilNed perfilNed;
	// ------------------------------
	
	// Anexo 860, cambio 1.12 ---------------
	/** Lista de ciudades */
	private ArrayList<Ciudades> listaCiudades;
	/** Lista de convenios asociados a la institucion en sesion */
	private ArrayList<Convenios> listaConveniosInstitucion;
	/** Lista de contatos asociados al convenio seleccionado */
	private ArrayList<Contratos> listaContratoConvenio;		
	/** Indica si el convenio seleccionado requiere mostrar las validaciones de base de datos */
	private boolean mostrarValidacionesBd;					 
	/** Indica si el convenio seleccionado requiere mostrar las autorizaciones */
	private boolean mostrarAutorizaciones;					
	/** Indica si el convenio seleccionado requiere mostrar los Bonos */
	private boolean mostrarBonos;						
	/** Indica si el boton para ingresar nuevo convenio fue activado para mostrar la seccion en la forma */
	private boolean mostrarNuevoConvenio; 
	/** Indica si se debe mostrar la seccion para ingresar un nuevo grupo de autorizaciones*/
	private boolean mostrarIngresarAutorizaciones; 
	/** Indica si se debe mostrar la seccion para ingresar un nuevo grupo de bonos */
	private boolean mostrarIngresarBonos; 
	/** Indica si el registro que se cargo es para ser modificado */
	private boolean seEstaModificando; 
	/** Contiene el tipo medio autorizacion seleccionado en la forma */
	private String tipoMedioAutoSeleccionado; 
	/** Indica la posicion de la autorizacion asignada. Posicion utilizada par ala eliminacion */
	private int posAutorizacionesAsignadas; 
	/** Indica la posicion del bono asignado. Posicion utilizada par ala eliminacion */
	private int posBonosAsignados; 
	/** Indica la posicion de loas convenios-contratos del paciente. Posicion utilizada para la elimininacion */
	private int  posConveniosContratoPaciente; 
	/** Contiene tipos de medios de autorizacion generados con acronimos de integridad */
	private ArrayList<DtoIntegridadDominio> listaTiposMedioAutorizacion; 
	/** Contiene estatos de medios de autorizacion generados con acronimos de integridad */
	private ArrayList<DtoIntegridadDominio> listaEstadosTiposMedioAutorizacion; 
	/** Informacion a almacenar en la seccion de convenio del paciente */
	private DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente;
	/** Usado para cargar temporalmente la informacion del dto */
	private DtoSeccionConvenioPaciente dtoSeccionConvenioPacienteTemp; 
	/** Usado para cargar los roles y los permisos que tiene el usuario */
	private ArrayList<DtoRolesTipoDeUsuario> listaDtoRolesUsuario;
	/** si se ingreso por la opción de nuevo convenio */
	private boolean entroPorNuevoConvenio; 
	/** Indica si el error generado es de la seccion de convenios */
	private boolean errorSeccionConvenio; 
	
	
	private boolean yaSeCargoPaciente;
	// ------------------------------
	
	/* ############################################################# */
	
	
	
	
	public IngresoPacienteOdontologiaForm()
	{
		this.reset();
	}
	
	
	
	/*
	#############################################################
	#	SECCION RESET											#			
	#############################################################
	*/
	
	public void resetParametrosAgenOdo()
	{
		this.existePaciente= false;
		this.existeComoUsuario=false;
		this.tipoFuncionalidad= new String("");
		this.urlRetorno = new String("");
		this.tipoIdenPac = new String("");
		this.numeroIdenPac = new String("");
	}
		
	
	public void reset()
	{
		this.estado=new String("");
		this.paciente=new DtoPaciente();
		this.motivoCitaPaciente= new DtoMotivoCitaPaciente();
		this.arrayBeneficiarios= new ArrayList<DtoBeneficiarioPaciente>();
		this.mensajesAlerta = new ArrayList<ElementoApResource>();
		this.plantilla=new DtoPlantilla();
		this.posBeneficiario=0;
		this.avisoValidacionMismosNombres= false;
		this.preguntoContinuar=false;
		this.operacionExito= new String("");
		this.mensaje= new String("");
		this.operacionExitoBenef= new String("");
		this.mensajeBenef= new String("");
		this.perfilNed= new DtoPerfilNed();
		
		//arreglos estructuras del ingreso del paciente
		this.paises = new ArrayList<HashMap<String,Object>>();
		this.ciudades = new ArrayList<HashMap<String,Object>>();
		this.ciudadesExp = new ArrayList<HashMap<String,Object>>();
		this.ciudadesNac = new ArrayList<HashMap<String,Object>>();
		this.localidades = new ArrayList<HashMap<String,Object>>();
		this.zonasDomicilio = new HashMap();
		this.ocupaciones = new HashMap();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		this.tiposSangre = new HashMap();
		this.estadosCiviles = new HashMap();
		this.centrosAtencion = new HashMap();
		this.tiposIdentificacion= new HashMap();
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.etnias = new ArrayList<HashMap<String,Object>>();
		this.religiones = new ArrayList<HashMap<String,Object>>();
		this.estudios = new ArrayList<HashMap<String,Object>>();
		this.pacientesMismosNombres = new ArrayList<HashMap<String,Object>>();
		this.motivosCita= new ArrayList<HashMap<String,Object>>();
		this.mediosConocimiento= new ArrayList<HashMap<String,Object>>();		
		this.parentezcos = new ArrayList<HashMap<String,Object>>();
		//******************************************
		this.ocultarCabezotes = ConstantesBD.acronimoSi;
		this.cerrarPopupNormal=ConstantesBD.acronimoNo;
		
		
		// Anexo 860, cambio 1.12 ---------------
		this.listaCiudades 					= new ArrayList<Ciudades>();
		this.listaConveniosInstitucion 		= new ArrayList<Convenios>();
		this.listaContratoConvenio			= new ArrayList<Contratos>();
		this.mostrarNuevoConvenio 			= false;
		this.tipoMedioAutoSeleccionado 		= "";
		this.dtoSeccionConvenioPaciente 	= new DtoSeccionConvenioPaciente();
		this.dtoSeccionConvenioPacienteTemp	= new DtoSeccionConvenioPaciente();
		this.mostrarValidacionesBd 			= false;
		this.mostrarAutorizaciones 			= false;
		this.mostrarBonos  					= false;
		this.mostrarIngresarAutorizaciones 	= true;
		this.mostrarIngresarBonos 			= false;
		this.posAutorizacionesAsignadas 	= ConstantesBD.codigoNuncaValido;
		this.posConveniosContratoPaciente 	= ConstantesBD.codigoNuncaValido;
		this.posBonosAsignados  			= ConstantesBD.codigoNuncaValido;
		this.seEstaModificando 				= false;
		this.listaDtoRolesUsuario			= new ArrayList<DtoRolesTipoDeUsuario>();
		this.entroPorNuevoConvenio			= false;
		this.errorSeccionConvenio 			= false;
		this.yaSeCargoPaciente				= false;
		// ----------------------------------------
	}

	
	public void resetMensaje()
	{
		this.operacionExito= new String("");
		this.mensaje= new String("");
	}
	
	
	public void resetMensajeBeneficiario()
	{
		this.operacionExitoBenef= new String("");
		this.mensajeBenef= new String("");
	}
	
	
	/**
	 * Limpia todas las variables de la seccion de convenios
	 */
	public void resetSeccionConvenios()
	{
		this.listaCiudades 				= new ArrayList<Ciudades>();
		this.listaConveniosInstitucion 	= new ArrayList<Convenios>();
		this.listaContratoConvenio 		= new ArrayList<Contratos>();
		this.mostrarNuevoConvenio 		= false;
		this.tipoMedioAutoSeleccionado 	= "";
		this.posAutorizacionesAsignadas = ConstantesBD.codigoNuncaValido;
		this.posBonosAsignados			= ConstantesBD.codigoNuncaValido;
		this.errorSeccionConvenio		= false;
		resetDtoConvenios();
	}
	
	
	/**
	 * Limpia todas las variables de la seccion de convenios
	 */
	public void resetSeccionConveniosMantenerConvenios()
	{
		this.listaCiudades 				= new ArrayList<Ciudades>();
		this.listaConveniosInstitucion 	= new ArrayList<Convenios>();
		this.listaContratoConvenio 		= new ArrayList<Contratos>();
		this.mostrarNuevoConvenio 		= false;
		this.tipoMedioAutoSeleccionado 	= "";
		this.errorSeccionConvenio		= false;
		resetDtoConvenios();
	}
	
	
	/**
	 * Limpia el dto con la informacion del nuevo convenio
	 */
	public void resetDtoConvenios()
	{
		this.mostrarValidacionesBd 			= false;
		this.mostrarAutorizaciones 			= false;
		this.mostrarBonos 					= false;
		this.mostrarIngresarAutorizaciones 	= false;
		this.mostrarIngresarBonos 			= false;
		this.errorSeccionConvenio			= false;
		this.dtoSeccionConvenioPaciente 	= new DtoSeccionConvenioPaciente();
	}
	
	
	/**
	 * Limpia la informacion de las autorizaciones seleccionadas
	 */
	public void resetAutorizaciones()
	{
		this.errorSeccionConvenio		= false;
		this.mostrarIngresarAutorizaciones = false;
		this.tipoMedioAutoSeleccionado 	= "";
		this.dtoSeccionConvenioPaciente.limpiarRegistrosNoBasicos();
	}
	
	
	
	/**
	 * Limpia la informacion de los Bonos seleccionadas
	 */
	public void resetbonos()
	{
		this.errorSeccionConvenio = false;
		this.mostrarIngresarBonos = false;
		this.dtoSeccionConvenioPaciente.limpiarRegistrosNoBasicos();
	}
	
	/* ############################################################# */
	
	
	
	
	/*
	#############################################################
	#	METODOS SETs Y GETs									#			
	#############################################################
	*/
	
	/**
	 * 
	 * @return
	 */
	public DtoEscala getEscalaPerfilNed(int codigoInstitucion) 
	{
		DtoEscala escala= null;
		double codigoEscalaValorDefecto= Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(codigoInstitucion));
		for(DtoSeccionFija seccionFija:this.getPlantilla().getSeccionesFijas())
		{
			if(escala==null)
			{	
				if(seccionFija.isVisible())
				{
					//Se itera cada elemento de la seccion fija
					for(DtoElementoParam elemento:seccionFija.getElementos())
					{
						if(elemento.isVisible() && elemento.isEscala())
						{
							if(Utilidades.convertirADouble(elemento.getCodigoPK()) == codigoEscalaValorDefecto)
							{
								escala=(DtoEscala) elemento;
								break;
							}
						}
					}
				}
			}	
		}	
		return escala;				
	}
	
	
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return the paciente
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}
	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}
	
	public String getTipIdentificacionPaciente()
	{
		return paciente.getTipoIdentificacion();
	}
	
	public void setTipoIdentificacionPaciente(String codIndentificacion)
	{
		this.paciente.setTipoIdentificacion(codIndentificacion);
	}
	
	
	public String getNumIdentificacionPaciente()
	{
		return paciente.getNumeroIdentificacion();	
	}
	
	public void setNumIdentificacionPaciente(String codIdentificacion)
	{
	 this.paciente.setNumeroIdentificacion(codIdentificacion);	
	}
	
	/**
	 * @return the paises
	 */
	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}
	/**
	 * @param paises the paises to set
	 */
	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}
	/**
	 * @return the ciudades
	 */
	public ArrayList<HashMap<String, Object>> getCiudades() {
		return ciudades;
	}
	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(ArrayList<HashMap<String, Object>> ciudades) {
		this.ciudades = ciudades;
	}
	/**
	 * @return the ciudadesExp
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesExp() {
		return ciudadesExp;
	}
	/**
	 * @param ciudadesExp the ciudadesExp to set
	 */
	public void setCiudadesExp(ArrayList<HashMap<String, Object>> ciudadesExp) {
		this.ciudadesExp = ciudadesExp;
	}
	/**
	 * @return the ciudadesNac
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesNac() {
		return ciudadesNac;
	}
	/**
	 * @param ciudadesNac the ciudadesNac to set
	 */
	public void setCiudadesNac(ArrayList<HashMap<String, Object>> ciudadesNac) {
		this.ciudadesNac = ciudadesNac;
	}
	/**
	 * @return the localidades
	 */
	public ArrayList<HashMap<String, Object>> getLocalidades() {
		return localidades;
	}

	/**
	 * @param localidades the localidades to set
	 */
	public void setLocalidades(ArrayList<HashMap<String, Object>> localidades) {
		this.localidades = localidades;
	}
	/**
	 * @return the zonasDomicilio
	 */
	public HashMap getZonasDomicilio() {
		return zonasDomicilio;
	}
	/**
	 * @param zonasDomicilio the zonasDomicilio to set
	 */
	public void setZonasDomicilio(HashMap zonasDomicilio) {
		this.zonasDomicilio = zonasDomicilio;
	}
	public ArrayList<DtoIntegridadDominio> getListaTiposMedioAutorizacion() {
		return listaTiposMedioAutorizacion;
	}

	public void setListaTiposMedioAutorizacion(
			ArrayList<DtoIntegridadDominio> listaTiposMedioAutorizacion) {
		this.listaTiposMedioAutorizacion = listaTiposMedioAutorizacion;
	}

	public String getTipoMedioAutoSeleccionado() {
		return tipoMedioAutoSeleccionado;
	}

	public void setTipoMedioAutoSeleccionado(String tipoMedioAutoSeleccionado) {
		this.tipoMedioAutoSeleccionado = tipoMedioAutoSeleccionado;
	}

	public ArrayList<DtoIntegridadDominio> getListaEstadosTiposMedioAutorizacion() {
		return listaEstadosTiposMedioAutorizacion;
	}

	public void setListaEstadosTiposMedioAutorizacion(
			ArrayList<DtoIntegridadDominio> listaEstadosTiposMedioAutorizacion) {
		this.listaEstadosTiposMedioAutorizacion = listaEstadosTiposMedioAutorizacion;
	}

	public DtoSeccionConvenioPaciente getDtoSeccionConvenioPaciente() {
		return dtoSeccionConvenioPaciente;
	}

	public void setDtoSeccionConvenioPaciente(
			DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente) {
		this.dtoSeccionConvenioPaciente = dtoSeccionConvenioPaciente;
	}

	/**
	 * @return the ocupaciones
	 */
	public HashMap getOcupaciones() {
		return ocupaciones;
	}

	/**
	 * @param ocupaciones the ocupaciones to set
	 */
	public void setOcupaciones(HashMap ocupaciones) {
		this.ocupaciones = ocupaciones;
	}
	/**
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}
	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}
	/**
	 * @return the tiposSangre
	 */
	public HashMap getTiposSangre() {
		return tiposSangre;
	}
	/**
	 * @param tiposSangre the tiposSangre to set
	 */
	public void setTiposSangre(HashMap tiposSangre) {
		this.tiposSangre = tiposSangre;
	}
	/**
	 * @return the estadosCiviles
	 */
	public HashMap getEstadosCiviles() {
		return estadosCiviles;
	}
	public boolean isSeEstaModificando() {
		return seEstaModificando;
	}


	public void setSeEstaModificando(boolean seEstaModificando) {
		this.seEstaModificando = seEstaModificando;
	}


	/**
	 * @param estadosCiviles the estadosCiviles to set
	 */
	public void setEstadosCiviles(HashMap estadosCiviles) {
		this.estadosCiviles = estadosCiviles;
	}
	/**
	 * @return the centrosAtencion
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}
	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	public ArrayList<Convenios> getListaConveniosInstitucion() {
		return listaConveniosInstitucion;
	}

	public void setListaConveniosInstitucion(
			ArrayList<Convenios> listaConveniosInstitucion) {
		this.listaConveniosInstitucion = listaConveniosInstitucion;
	}

	/**
	 * @return the etnias
	 */
	public ArrayList<HashMap<String, Object>> getEtnias() {
		return etnias;
	}
	/**
	 * @param etnias the etnias to set
	 */
	public void setEtnias(ArrayList<HashMap<String, Object>> etnias) {
		this.etnias = etnias;
	}
	/**
	 * @return the religiones
	 */
	public ArrayList<HashMap<String, Object>> getReligiones() {
		return religiones;
	}
	/**
	 * @param religiones the religiones to set
	 */
	public void setReligiones(ArrayList<HashMap<String, Object>> religiones) {
		this.religiones = religiones;
	}
	/**
	 * @return the estudios
	 */
	public ArrayList<HashMap<String, Object>> getEstudios() {
		return estudios;
	}
	/**
	 * @param estudios the estudios to set
	 */
	public void setEstudios(ArrayList<HashMap<String, Object>> estudios) {
		this.estudios = estudios;
	}
	/**
	 * @return the pacientesMismosNombres
	 */
	public ArrayList<HashMap<String, Object>> getPacientesMismosNombres() {
		return pacientesMismosNombres;
	}
	/**
	 * @param pacientesMismosNombres the pacientesMismosNombres to set
	 */
	public void setPacientesMismosNombres(
			ArrayList<HashMap<String, Object>> pacientesMismosNombres) {
		this.pacientesMismosNombres = pacientesMismosNombres;
	}

	/**
	 * @return the motivoPaciente
	 */
	public DtoMotivoCitaPaciente getMotivoCitaPaciente() {
		return motivoCitaPaciente;
	}

	/**
	 * @param motivoPaciente the motivoPaciente to set
	 */
	public void setMotivoCitaPaciente(DtoMotivoCitaPaciente motivoCitaPaciente) {
		this.motivoCitaPaciente = motivoCitaPaciente;
	}

	/**
	 * @return the arrayBeneficiarios
	 */
	public ArrayList<DtoBeneficiarioPaciente> getArrayBeneficiarios() {
		return arrayBeneficiarios;
	}

	/**
	 * @param arrayBeneficiarios the arrayBeneficiarios to set
	 */
	public void setArrayBeneficiarios(
			ArrayList<DtoBeneficiarioPaciente> arrayBeneficiarios) {
		this.arrayBeneficiarios = arrayBeneficiarios;
	}

	/**
	 * @return the plantilla
	 */
	public DtoPlantilla getPlantilla() {
		return plantilla;
	}

	/**
	 * @param plantilla the plantilla to set
	 */
	public void setPlantilla(DtoPlantilla plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * @return the mensajesAlerta
	 */
	public ArrayList<ElementoApResource> getMensajesAlerta() {
		return mensajesAlerta;
	}

	/**
	 * @param mensajesAlerta the mensajesAlerta to set
	 */
	public void setMensajesAlerta(ArrayList<ElementoApResource> mensajesAlerta) {
		this.mensajesAlerta = mensajesAlerta;
	}

	
	public int getNumMensajesAlerta()
	{
		return this.mensajesAlerta.size();
	}

	public ArrayList<Contratos> getListaContratoConvenio() {
		return listaContratoConvenio;
	}

	public void setListaContratoConvenio(ArrayList<Contratos> listaContratoConvenio) {
		this.listaContratoConvenio = listaContratoConvenio;
	}

	/**
	 * @return the motivosCita
	 */
	public ArrayList<HashMap<String, Object>> getMotivosCita() {
		return motivosCita;
	}

	/**
	 * @param motivosCita the motivosCita to set
	 */
	public void setMotivosCita(ArrayList<HashMap<String, Object>> motivosCita) {
		this.motivosCita = motivosCita;
	}

	/**
	 * @return the mediosConocimiento
	 */
	public ArrayList<HashMap<String, Object>> getMediosConocimiento() {
		return mediosConocimiento;
	}

	/**
	 * @param mediosConocimiento the mediosConocimiento to set
	 */
	public void setMediosConocimiento(
			ArrayList<HashMap<String, Object>> mediosConocimiento) {
		this.mediosConocimiento = mediosConocimiento;
	}

	/**
	 * @return the parentezcos
	 */
	public ArrayList<HashMap<String, Object>> getParentezcos() {
		return parentezcos;
	}

	/**
	 * @param parentezcos the parentezcos to set
	 */
	public void setParentezcos(ArrayList<HashMap<String, Object>> parentezcos) {
		this.parentezcos = parentezcos;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the existePaciente
	 */
	public boolean isExistePaciente() {
		return existePaciente;
	}

	/**
	 * @param existePaciente the existePaciente to set
	 */
	public void setExistePaciente(boolean existePaciente) {
		this.existePaciente = existePaciente;
	}

	/**
	 * @return the avisoValidacionMismosNombres
	 */
	public boolean isAvisoValidacionMismosNombres() {
		return avisoValidacionMismosNombres;
	}

	/**
	 * @param avisoValidacionMismosNombres the avisoValidacionMismosNombres to set
	 */
	public void setAvisoValidacionMismosNombres(boolean avisoValidacionMismosNombres) {
		this.avisoValidacionMismosNombres = avisoValidacionMismosNombres;
	}

	/**
	 * @return the tiposIdentificacion
	 */
	public HashMap getTiposIdentificacion() {
		return tiposIdentificacion;
	}

	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacion(HashMap tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}


	/**
	 * @return the urlRetorno
	 */
	public String getUrlRetorno() {
		return urlRetorno;
	}

	/**
	 * @param urlRetorno the urlRetorno to set
	 */
	public void setUrlRetorno(String urlRetorno) {
		this.urlRetorno = urlRetorno;
	}

	public boolean isMostrarNuevoConvenio() {
		return mostrarNuevoConvenio;
	}

	public void setMostrarNuevoConvenio(boolean mostrarNuevoConvenio) {
		this.mostrarNuevoConvenio = mostrarNuevoConvenio;
	}

	/**
	 * @return the posBeneficiario
	 */
	public int getPosBeneficiario() {
		return posBeneficiario;
	}

	/**
	 * @param posBeneficiario the posBeneficiario to set
	 */
	public void setPosBeneficiario(int posBeneficiario) {
		this.posBeneficiario = posBeneficiario;
	}

	/**
	 * @return the operacionExito
	 */
	public String getOperacionExito() {
		return operacionExito;
	}

	/**
	 * @param operacionExito the operacionExito to set
	 */
	public void setOperacionExito(String operacionExito) {
		this.operacionExito = operacionExito;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the tipoFuncionalidad
	 */
	public String getTipoFuncionalidad() {
		return tipoFuncionalidad;
	}

	public int getPosConveniosContratoPaciente() {
		return posConveniosContratoPaciente;
	}


	public void setPosConveniosContratoPaciente(int posConveniosContratoPaciente) {
		this.posConveniosContratoPaciente = posConveniosContratoPaciente;
	}


	/**
	 * @param tipoFuncionalidad the tipoFuncionalidad to set
	 */
	public void setTipoFuncionalidad(String tipoFuncionalidad) {
		this.tipoFuncionalidad = tipoFuncionalidad;
	}

	/**
	 * @return the ocultarCabezotes
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}

	/**
	 * @param ocultarCabezotes the ocultarCabezotes to set
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}


	public int getPosAutorizacionesAsignadas() {
		return posAutorizacionesAsignadas;
	}

	public void setPosAutorizacionesAsignadas(int posAutorizacionesAsignadas) {
		this.posAutorizacionesAsignadas = posAutorizacionesAsignadas;
	}

	/**
	 * @return the tipoIdenPac
	 */
	public String getTipoIdenPac() {
		return tipoIdenPac;
	}


	/**
	 * @param tipoIdenPac the tipoIdenPac to set
	 */
	public void setTipoIdenPac(String tipoIdenPac) {
		this.tipoIdenPac = tipoIdenPac;
	}


	/**
	 * @return the numeroIdenPac
	 */
	public String getNumeroIdenPac() {
		return numeroIdenPac;
	}


	/**
	 * @param numeroIdenPac the numeroIdenPac to set
	 */
	public void setNumeroIdenPac(String numeroIdenPac) {
		this.numeroIdenPac = numeroIdenPac;
	}

	/**
	 * @return the preguntoContinuar
	 */
	public boolean isPreguntoContinuar() {
		return preguntoContinuar;
	}

	/**
	 * @param preguntoContinuar the preguntoContinuar to set
	 */
	public void setPreguntoContinuar(boolean preguntoContinuar) {
		this.preguntoContinuar = preguntoContinuar;
	}


	/**
	 * @return the operacionExitoBenef
	 */
	public String getOperacionExitoBenef() {
		return operacionExitoBenef;
	}

	/**
	 * @param operacionExitoBenef the operacionExitoBenef to set
	 */
	public void setOperacionExitoBenef(String operacionExitoBenef) {
		this.operacionExitoBenef = operacionExitoBenef;
	}

	/**
	 * @return the mensajeBenef
	 */
	public String getMensajeBenef() {
		return mensajeBenef;
	}

	/**
	 * @param mensajeBenef the mensajeBenef to set
	 */
	public void setMensajeBenef(String mensajeBenef) {
		this.mensajeBenef = mensajeBenef;
	}

	/**
	 * @return the perfilNed
	 */
	public DtoPerfilNed getPerfilNed() {
		return perfilNed;
	}

	/**
	 * @param perfilNed the perfilNed to set
	 */
	public void setPerfilNed(DtoPerfilNed perfilNed) {
		this.perfilNed = perfilNed;
	}

	/**
	 * @return the cerrarPopupNormal
	 */
	public String getCerrarPopupNormal() {
		return cerrarPopupNormal;
	}

	/**
	 * @param cerrarPopupNormal the cerrarPopupNormal to set
	 */
	public void setCerrarPopupNormal(String cerrarPopupNormal) {
		this.cerrarPopupNormal = cerrarPopupNormal;
	}

	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}
	
	
	public boolean isMostrarValidacionesBd() {
		return mostrarValidacionesBd;
	}

	public void setMostrarValidacionesBd(boolean mostrarValidacionesBd) {
		this.mostrarValidacionesBd = mostrarValidacionesBd;
	}

	public boolean isMostrarAutorizaciones() {
		return mostrarAutorizaciones;
	}

	public DtoSeccionConvenioPaciente getDtoSeccionConvenioPacienteTemp() {
		return dtoSeccionConvenioPacienteTemp;
	}



	public void setDtoSeccionConvenioPacienteTemp(
			DtoSeccionConvenioPaciente dtoSeccionConvenioPacienteTemp) {
		this.dtoSeccionConvenioPacienteTemp = dtoSeccionConvenioPacienteTemp;
	}



	public void setMostrarAutorizaciones(boolean mostrarAutorizaciones) {
		this.mostrarAutorizaciones = mostrarAutorizaciones;
	}
	

	public boolean isExisteComoUsuario() {
		return existeComoUsuario;
	}


	public void setExisteComoUsuario(boolean existeComoUsuario) {
		this.existeComoUsuario = existeComoUsuario;
	}


	public boolean isMostrarBonos() {
		return mostrarBonos;
	}


	public void setMostrarBonos(boolean mostrarBonos) {
		this.mostrarBonos = mostrarBonos;
	}


	public boolean isMostrarIngresarAutorizaciones() {
		return mostrarIngresarAutorizaciones;
	}


	public void setMostrarIngresarAutorizaciones(
			boolean mostrarIngresarAutorizaciones) {
		this.mostrarIngresarAutorizaciones = mostrarIngresarAutorizaciones;
	}


	public boolean isMostrarIngresarBonos() {
		return mostrarIngresarBonos;
	}


	public void setMostrarIngresarBonos(boolean mostrarIngresarBonos) {
		this.mostrarIngresarBonos = mostrarIngresarBonos;
	}


	public int getPosBonosAsignados() {
		return posBonosAsignados;
	}


	public void setPosBonosAsignados(int posBonosAsignados) {
		this.posBonosAsignados = posBonosAsignados;
	}



	public ArrayList<DtoRolesTipoDeUsuario> getListaDtoRolesUsuario() {
		return listaDtoRolesUsuario;
	}



	public void setListaDtoRolesUsuario(
			ArrayList<DtoRolesTipoDeUsuario> listaDtoRolesUsuario) {
		this.listaDtoRolesUsuario = listaDtoRolesUsuario;
	}



	public boolean isEntroPorNuevoConvenio() {
		return entroPorNuevoConvenio;
	}



	public void setEntroPorNuevoConvenio(boolean entroPorNuevoConvenio) {
		this.entroPorNuevoConvenio = entroPorNuevoConvenio;
	}



	public boolean isErrorSeccionConvenio() {
		return errorSeccionConvenio;
	}



	public void setErrorSeccionConvenio(boolean errorSeccionConvenio) {
		this.errorSeccionConvenio = errorSeccionConvenio;
	}



	/**
	 * Obtiene el valor del atributo yaSeCargoPaciente
	 *
	 * @return Retorna atributo yaSeCargoPaciente
	 */
	public boolean isYaSeCargoPaciente()
	{
		return yaSeCargoPaciente;
	}



	/**
	 * Establece el valor del atributo yaSeCargoPaciente
	 *
	 * @param valor para el atributo yaSeCargoPaciente
	 */
	public void setYaSeCargoPaciente(boolean yaSeCargoPaciente)
	{
		this.yaSeCargoPaciente = yaSeCargoPaciente;
	}

	
	/* ############################################################# */
	
	
}
