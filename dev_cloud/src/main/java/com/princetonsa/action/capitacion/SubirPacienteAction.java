/**
 * Ricardo Ruiz 15/06/2011
 * Servinte S.A.
 */
package com.princetonsa.action.capitacion;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.capitacion.SubirPacienteForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOSubirPacientesInconsistencias;
import com.princetonsa.dto.capitacion.DtoInconsistenciasArchivoPlano;
import com.princetonsa.dto.capitacion.DtoVerificacionArchivo;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.SubirPaciente;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.administracion.DtoBarrio;
import com.servinte.axioma.dto.administracion.DtoCiudad;
import com.servinte.axioma.dto.administracion.DtoLocalidad;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IBarriosMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IPersonas;
import com.servinte.axioma.mundo.interfaz.capitacion.ICapitadoInconsistenciaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IContratoCargueMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IConvUsuariosCapitadosMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenSubirPacienteMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciaPersonaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciasCamposMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogSubirPacientesMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUnidadPagoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.INaturalezaPacienteMundo;
import com.servinte.axioma.orm.Barrios;
import com.servinte.axioma.orm.CapitadoInconsistencia;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.CiudadesHome;
import com.servinte.axioma.orm.CiudadesId;
import com.servinte.axioma.orm.ContratoCargue;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.ContratosHome;
import com.servinte.axioma.orm.ConvUsuariosCapitados;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosHome;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.InconsistenSubirPaciente;
import com.servinte.axioma.orm.InconsistenciaPersona;
import com.servinte.axioma.orm.InconsistenciasCampos;
import com.servinte.axioma.orm.LogSubirPacientes;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.Sexo;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposCargue;
import com.servinte.axioma.orm.TiposParentesco;
import com.servinte.axioma.orm.UnidadPago;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.UsuariosCapitados;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;

/**
 * @author Juan David Ramírez
 *
 */
public class SubirPacienteAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(SubirPacienteAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.SubirPacienteForm");
	
	private static final int columnaConvenio=0;
	private static final int columnaNumeroContrato=1;
	private static final int columnaTipoIdentificacion=2;
	private static final int columnaNumeroIdentificacion=3;
	private static final int columnaPrimerApellido=4;
	private static final int columnaSegundoApellido=5;
	private static final int columnaPrimerNombre=6;
	private static final int columnaSegundoNombre=7;
	private static final int columnaFechaNacimiento=8;
	private static final int columnaSexo=9;
	private static final int columnaDireccion=10;
	private static final int columnaPaisResidencia=11;					
	private static final int columnaDepartamentoResidencia=12;			
	private static final int columnaMunicipioResidencia=13;				
	private static final int columnaLocalidadResidencia=14;				
	private static final int columnaBarrioResidencia=15;				
	private static final int columnaTelefono=16;	
	private static final int columnaClasificacionSocioEconomica=17;
	private static final int columnaTipoAfiliado=18;					
	private static final int columnaExcepcionMonto=19;					
	private static final int columnaNumeroFicha=20;
	private static final int columnaCentroAtencioAsignado=21;			
	private static final int columnaTipoIdEmpleador=22;					
	private static final int columnaNumeroIdentificacionEmpleador=23;	 
	private static final int columnaRazonSocialEmpleador=24;			
	private static final int columnaTipoIdCotizante=25;					
	private static final int columnaNumeroIdentificacionCotizante=26;	
	private static final int columnaNombresCotizante=27;				
	private static final int columnaApellidosCotizante=28;				
	private static final int columnaParentesco=29;						
	private static final String nombreColumnaConvenio="subirPacienteForm.nombreColumnaConvenio";
	private static final String nombreColumnaNumeroContrato="subirPacienteForm.nombreColumnaNumeroContrato";
	private static final String nombreColumnaTipoIdentificacion="subirPacienteForm.nombreColumnaTipoIdentificacion";
	private static final String nombreColumnaNumeroIdentificacion="subirPacienteForm.nombreColumnaNumeroIdentificacion";
	private static final String nombreColumnaPrimerApellido="subirPacienteForm.nombreColumnaPrimerApellido";
	private static final String nombreColumnaSegundoApellido="subirPacienteForm.nombreColumnaSegundoApellido";
	private static final String nombreColumnaPrimerNombre="subirPacienteForm.nombreColumnaPrimerNombre";
	private static final String nombreColumnaSegundoNombre="subirPacienteForm.nombreColumnaSegundoNombre";
	private static final String nombreColumnaFechaNacimiento="subirPacienteForm.nombreColumnaFechaNacimiento";
	private static final String nombreColumnaSexo="subirPacienteForm.nombreColumnaSexo";
	private static final String nombreColumnaDireccion="subirPacienteForm.nombreColumnaDireccion";
	private static final String nombreColumnaPaisResidencia="subirPacienteForm.nombreColumnaPaisResidencia";					
	private static final String nombreColumnaDepartamentoResidencia="subirPacienteForm.nombreColumnaDepartamentoResidencia";			
	private static final String nombreColumnaMunicipioResidencia="subirPacienteForm.nombreColumnaMunicipioResidencia";				
	private static final String nombreColumnaLocalidadResidencia="subirPacienteForm.nombreColumnaLocalidadResidencia";				
	private static final String nombreColumnaBarrioResidencia="subirPacienteForm.nombreColumnaBarrioResidencia";				
	private static final String nombreColumnaTelefono="subirPacienteForm.nombreColumnaTelefono";	
	private static final String nombreColumnaClasificacionSocioEconomica="subirPacienteForm.nombreColumnaClasificacionSocioEconomica";
	private static final String nombreColumnaTipoAfiliado="subirPacienteForm.nombreColumnaTipoAfiliado";					
	private static final String nombreColumnaExcepcionMonto="subirPacienteForm.nombreColumnaExcepcionMonto";					
	private static final String nombreColumnaNumeroFicha="subirPacienteForm.nombreColumnaNumeroFicha";
	private static final String nombreColumnaCentroAtencioAsignado="subirPacienteForm.nombreColumnaCentroAtencioAsignado";			
	private static final String nombreColumnaTipoIdEmpleador="subirPacienteForm.nombreColumnaTipoIdEmpleador";					
	private static final String nombreColumnaNumeroIdentificacionEmpleador="subirPacienteForm.nombreColumnaNumeroIdentificacionEmpleador";	 
	private static final String nombreColumnaRazonSocialEmpleador="subirPacienteForm.nombreColumnaRazonSocialEmpleador";			
	private static final String nombreColumnaTipoIdCotizante="subirPacienteForm.nombreColumnaTipoIdCotizante";					
	private static final String nombreColumnaNumeroIdentificacionCotizante="subirPacienteForm.nombreColumnaNumeroIdentificacionCotizante";	
	private static final String nombreColumnaNombresCotizante="subirPacienteForm.nombreColumnaNombresCotizante";				
	private static final String nombreColumnaApellidosCotizante="subirPacienteForm.nombreColumnaApellidosCotizante";				
	private static final String nombreColumnaParentesco="subirPacienteForm.nombreColumnaParentesco";						
	
	//dto inconsistencias Archivo Campos
	private DtoInconsistenciasArchivoPlano dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano();
	//dto inconsistencias Archivo Personas
	private DtoInconsistenciasArchivoPlano dtoInconsistenciaPersonaBD=new DtoInconsistenciasArchivoPlano();
	//dto inconsistencias Archivo Personas con Cargue Previo para el mismo Periodo
	private DtoInconsistenciasArchivoPlano dtoInconsistenciaCarguePrevio=new DtoInconsistenciasArchivoPlano();
	//lista que almacena las inconsistencias de campos en el ARCHIVOd
	private ArrayList<DtoInconsistenciasArchivoPlano>listaInconsistenciaCampos=new ArrayList<DtoInconsistenciasArchivoPlano>();	
	//lista que almacena las inconsistencias de personas en el ARCHIVO
	private ArrayList<DtoInconsistenciasArchivoPlano>listaInconsistenciaPersonaBD=new ArrayList<DtoInconsistenciasArchivoPlano>();
	//lista que almacena las inconsistencias de personas con cargue previo en el ARCHIVO
	private ArrayList<DtoInconsistenciasArchivoPlano>listaInconsistenciaCarguePrevio=new ArrayList<DtoInconsistenciasArchivoPlano>();
	//lista de personas del archivo plano que no tuvieron inconsistencias
	private ArrayList<DtoPersonas>listaPersonaArchivoPlano=new ArrayList<DtoPersonas>();
	//variable que almacena el numero de registros leidos de el ARCHIVO
	private long numeroRegistrosLeidos=0;
	//variable que almacena el numero de registros seleccionados de las inconsistencias de personas
	private int contadorLineasSeleccionadas=0;
	//valida si la personaArchivoPlano tuvo Inconsistencia
	private ArrayList<DTOSubirPacientesInconsistencias> listaSubirPacientesInc=new ArrayList<DTOSubirPacientesInconsistencias>();
	//variable que almacena el numero de registros con inconsistencias que fueron grabadas
	private int numeroInconsistenciasSeleccionadas=0;
		
	
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof SubirPacienteForm)
		{
			con=UtilidadBD.abrirConexion();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			SubirPacienteForm forma=(SubirPacienteForm)form;
			ActionErrors errores=new ActionErrors();
			String estado=forma.getEstado();
			//InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			String fechaActual=UtilidadFecha.getFechaActual();
						
			logger.warn("Estado [SubirPacienteAction]--> "+estado);
			if(estado.equalsIgnoreCase("masivo"))
			{
				return accionEmpezar(con, mapping, forma, request, usuario);
			}	
			else if(estado.equalsIgnoreCase("individual"))
			{
				return accionEmpezar(con, mapping, forma, request, usuario);
			}
			else if(estado.equalsIgnoreCase("verificarMasivo"))
			{
				return accionVerificarMasivo(con, mapping,errores, forma, usuario, request,fechaActual);
			}
			else if(estado.equalsIgnoreCase("accionVerificarIdentificacion"))
			{
				return accionVerificarIdentificacion(con, mapping, forma, request);
			}
			else if(estado.equalsIgnoreCase("verificarIndividual"))
			{
				return accionVerificarIndividual(con, mapping, forma, request);
			}
			else if(estado.equalsIgnoreCase("guardarIndividual"))
			{
				//Se valida el parametro general de si es o no requerido el centro de atención
				String required=ValoresPorDefecto.getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(usuario.getCodigoInstitucionInt());
				if(required.equals(ConstantesBD.acronimoSi)){
					if(forma.getConsecutivoCentroAtencionSeleccionado()==ConstantesBD.codigoNuncaValido)
					{
						errores.add("Centro Atención requerido", new ActionMessage("errors.required", "Centro Atención Asignado"));
						saveErrors(request, errores);
						return mapping.findForward("principal"); 
					}
				}
				return accionGuardarIndividual(con, mapping, forma, usuario, request, errores);
			}
			else if(estado.equalsIgnoreCase("postularDatos"))
			{
				return accionSeleccionConvenio(con, mapping, forma, usuario, request, errores);
			}
			else if(estado.equalsIgnoreCase("postularContrato"))
			{
				return accionPostularContrato(con, mapping, forma, usuario, request, errores);
			}
			else if(estado.equals("seleccionConvenioCSE"))
			{
				return accionSeleccionConvenioCSE(con, mapping, forma, usuario);
			}
			else if(estado.equals("subirInconsistencias"))
			{				
				return subirPacientesArchivoPlano(con, mapping, forma, usuario, request);
				
			}else if(estado.equals("confirmarGuardarInconsisPersona"))
			{				
				return confirmarGuardarInconsisPersona(mapping, errores, forma, usuario, request);
				
			}else if(estado.equals("confirmarGuardarPersonaOK")){	
				return confirmarGuardarPersonasConsis(mapping, errores, forma, usuario, request);
			}			
			else if(estado.equals("cambiarPaisResidencia")){
				
				return listarCiudades(forma, con, mapping);
			}
			else if(estado.equals("mostrarPopUp")){
				return hacerMapping(con, mapping, "mostrarPopUpPacientes");
			}
			else if(estado.equals("cargarInfoPaciente")){
				return cargarInformacionPaciente(forma, con, mapping, usuario, request, errores);
			}
			else if(estado.equals("guardarPersonaNombresIgual"))	{
				return guardarPersonaIndividualIgualNombres(con, mapping, forma, usuario, request, errores);
			}
			else if(estado.equals("volverSubirMasivo"))	{
				return mapping.findForward("principal");
			}
			else if(estado.equals("mostrarSeccionCotizante"))
			{				
				if(forma.getCodigoTipoAfiliado().equals(ConstantesBD.codigoTipoAfiliadoBeneficiario))
				{
					forma.setVerSeccionCotizante(true);
				}else{
					forma.setVerSeccionCotizante(false);
					forma.resetDatoscotizante();
				}
				forma.setEstado("continuar");
				return hacerMapping(con, mapping, "principal");
			}
			
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.estadoInvalido", "errors.estadoInvalido", true);
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.formaTipoInvalido", "errors.formaTipoInvalido", true);
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	private ActionForward accionVerificarIdentificacion(Connection con, ActionMapping mapping, SubirPacienteForm forma, HttpServletRequest request)
	{	
		//*********VALIDACION DE LOS Nï¿½MEROS DE IDENTIFICACION***********************************
		forma.setMapaNumerosId(Utilidades.personasConMismoNumeroId(
			con, 
			forma.getNumId(),forma.getCodigoTipoId())
		);
		if(Integer.parseInt(forma.getMapaNumerosId("numRegistros").toString())>0)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("avisoNumerosId");
		}
		return accionVerificarIndividual(con,mapping,forma,request);
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward cargarInformacionPaciente(SubirPacienteForm forma,Connection con, ActionMapping mapping, 
								UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores)
	{		
		//DtoPersonas personaSeleccionada=forma.getListaFinalPersonasInconsistencias().get(forma.getPosicionListaFinalPersonas()).getListaPersonasBD().get(forma.getPosicionListaPersonasBD());
		DtoPersonas personaSeleccionada= new DtoPersonas();
		personaSeleccionada=forma.getListaFinalPersonasInconsistencias().get(forma.getPosicionListaFinalPersonas()).getListaPersonasBD().get(forma.getPosicionListaPersonasBD());
		
		forma.setNumId(personaSeleccionada.getNumeroId());
		forma.setCodigoTipoId(personaSeleccionada.getTipoId());
		forma.setNombreTipoId(personaSeleccionada.getNombreTipoIdentificacion());
		forma.setCodigoPersona(personaSeleccionada.getCodigo());
		forma.setPrimerApellidoPersona(personaSeleccionada.getPrimerApellido());
		forma.setSegundoApellidoPersona(personaSeleccionada.getSegundoApellido());
		forma.setPrimerNombrePersona(personaSeleccionada.getPrimerNombre());
		forma.setSegundoNombrePersona(personaSeleccionada.getSegundoNombre());
		forma.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(personaSeleccionada.getFechaNacimientoTipoDate()));
		forma.setNombreSexo(personaSeleccionada.getSexo());
		
		forma.setDireccion(personaSeleccionada.getDireccion());
		forma.setTelefono(personaSeleccionada.getTelefono());
		forma.setEmail(personaSeleccionada.getEmail());
		
		forma.setMunicipioResidencia(personaSeleccionada.getMunicipio());
		forma.setDepartamentoResidencia(personaSeleccionada.getDepartamento());
		forma.setPaisResidencia(personaSeleccionada.getPais());
		forma.setNombreBarrio(personaSeleccionada.getBarrio());
		forma.setExisteEnPersona(true);
		accionSeleccionConvenio(con, mapping, forma, usuario, request, errores);
		
		forma.setEstado("continuar");
		return hacerMapping(con, mapping, "principal");
	}
	
	/**
	 * Este método se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado país.
	 * 
	 * @param forma 
	 * @author Angela Aguirre
	 */
	@SuppressWarnings("unchecked")
	private ActionForward listarCiudades(SubirPacienteForm forma,Connection con, ActionMapping mapping) {
		String codigoPais="";
		
		if(forma.getEstado().equals("cambiarPaisResidencia")){			
			codigoPais=(String)forma.getCodigoPaisResidencia();
		}		
		
		if(!UtilidadTexto.isEmpty(codigoPais)){
			ArrayList<HashMap<String, Object>> listaCiudades= Utilidades.obtenerCiudadesXPais(con, codigoPais);
			
			if(listaCiudades!=null && listaCiudades.size()==1){
				
				String codigoCiudad = (listaCiudades.get(0)).get("codigoDepartamento")+ConstantesBD.separadorSplit +
				              (listaCiudades.get(0)).get("codigoCiudad");
				
				if(forma.getEstado().equals("cambiarPaisResidencia")){	
					forma.setCodigoCiudadResidencia(codigoCiudad);
				}
			}
			
			if(forma.getEstado().equals("cambiarPaisResidencia")){
				forma.setCiudades(listaCiudades);
			}
		}
		
		forma.setEstado("continuar");
		return hacerMapping(con, mapping, "principal");
	}
	
	/**
	 * Busca la persona en la base de datos y retorna toda su información, si la persona no existe entonces retorna una persona
	 * con su información en vacio.
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 * @throws SQLException
	 */
	private PersonaBasica consultarPersona(Connection con, String tipoId, String numeroId) throws SQLException
	{
		PersonaBasica tempoPersona = new PersonaBasica();
		tempoPersona.cargar(con, new TipoNumeroId(tipoId, numeroId));
		return tempoPersona;
	}
	
	private ActionForward accionVerificarIndividual(Connection con, ActionMapping mapping, SubirPacienteForm forma, HttpServletRequest request)
	{			
		try
		{
			PersonaBasica persona = this.consultarPersona(con, forma.getCodigoTipoId(), forma.getNumId());
			HibernateUtil.beginTransaction();
			if(persona.getCodigoPersona()!=-1) // Si la persona existe
			{
				forma.setCodigoPersona(persona.getCodigoPersona());
				forma.setCodigoUsuarioCapitado(ConstantesBD.codigoNuncaValidoLong);
				forma.setPrimerApellidoPersona(persona.getPrimerApellido());
				forma.setSegundoApellidoPersona(persona.getSegundoApellido());
				forma.setPrimerNombrePersona(persona.getPrimerNombre());
				forma.setSegundoNombrePersona(persona.getSegundoNombre());
				forma.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(persona.getFechaNacimiento()));
				if(persona.getSexo().equals(ConstantesIntegridadDominio.acronimoMasculino))
				{	forma.setSexo(ConstantesBD.codigoSexoMasculino);
					forma.setNombreSexo(persona.getSexo());
				}else if(persona.getSexo().equals(ConstantesIntegridadDominio.acronimoFemenino)){
					forma.setSexo(ConstantesBD.codigoSexoFemenino);
					forma.setNombreSexo(persona.getSexo());
				}else{
					forma.setSexo(ConstantesBD.codigoSexoAmbos);
					forma.setNombreSexo(persona.getSexo());
				}								
				forma.setDireccion(persona.getDireccion());
				forma.setTelefono(UtilidadTexto.isEmpty(persona.getTelefono())?"":persona.getTelefono());
				forma.setEmail(UtilidadTexto.isEmpty(persona.getEmail())?"":persona.getEmail());
				forma.setPaisResidencia(UtilidadTexto.isEmpty(persona.getNombrePaisVivienda())?"":persona.getNombrePaisVivienda());
				forma.setMunicipioResidencia(UtilidadTexto.isEmpty(persona.getNombreCiudadVivienda())?"":persona.getNombreCiudadVivienda());
				forma.setDepartamentoResidencia(UtilidadTexto.isEmpty(persona.getNombreDeptoVivienda())?"":persona.getNombreDeptoVivienda());
				forma.setExisteEnPersona(true);
				if(persona.getBarrio()!=null && persona.getBarrio()!=""){
					String []barrio=persona.getBarrio().split("#");
					forma.setNombreBarrio(barrio[0]);
					forma.setCodigoBarrio(barrio[1]);
				}
			}
			else
			{	
				forma.setCodigoPersona(ConstantesBD.codigoNuncaValido);
				IUsuariosCapitadosMundo usuariosCapitadosMundo=CapitacionFabricaMundo.crearUsuariosCapitadosMundo();
				ArrayList<DtoUsuariosCapitados> dtoUsuarioCapitado=new ArrayList<DtoUsuariosCapitados>();
				DtoUsuariosCapitados parametrosBusqueda=new DtoUsuariosCapitados();
				parametrosBusqueda.setTipoIdentificacion(forma.getCodigoTipoId());
				parametrosBusqueda.setNumeroIdentificacion(forma.getNumId());
				dtoUsuarioCapitado=usuariosCapitadosMundo.buscarUsuariosCapitados(parametrosBusqueda);
				if(!dtoUsuarioCapitado.isEmpty())
				{	
					DtoUsuariosCapitados usuarioCapi =dtoUsuarioCapitado.get(0); 
					
					forma.setCodigoUsuarioCapitado(usuarioCapi.getCodigoUsuarioCapitado());
					forma.setPrimerApellidoPersona(usuarioCapi.getPrimerApellido());
					forma.setSegundoApellidoPersona(usuarioCapi.getSegundoApellido());
					forma.setPrimerNombrePersona(usuarioCapi.getPrimerNombre());
					forma.setSegundoNombrePersona(usuarioCapi.getSegundoNombre());
					forma.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(usuarioCapi.getFechaNacimiento().toString()));						
					if(usuarioCapi.getSexo().equals(ConstantesIntegridadDominio.acronimoMasculino))
					{	forma.setSexo(ConstantesBD.codigoSexoMasculino);
						forma.setNombreSexo(usuarioCapi.getSexo());
					}else if(usuarioCapi.getSexo().equals(ConstantesIntegridadDominio.acronimoFemenino)){
						forma.setSexo(ConstantesBD.codigoSexoFemenino);
						forma.setNombreSexo(usuarioCapi.getSexo());
					}else{
						forma.setSexo(ConstantesBD.codigoSexoAmbos);
						forma.setNombreSexo(usuarioCapi.getSexo());
					}
					forma.setDireccion(UtilidadTexto.isEmpty(usuarioCapi.getDireccion())?"":usuarioCapi.getDireccion());
					forma.setTelefono(UtilidadTexto.isEmpty(usuarioCapi.getTelefono())?"":usuarioCapi.getTelefono());
					if(UtilidadTexto.isEmpty(usuarioCapi.getEmail()))forma.setEmail("");else forma.setEmail(usuarioCapi.getEmail());
					if(UtilidadTexto.isEmpty(usuarioCapi.getNumeroFicha()))forma.setNumeroFicha("");else forma.setNumeroFicha(usuarioCapi.getNumeroFicha());
					forma.setPaisResidencia(UtilidadTexto.isEmpty(usuarioCapi.getPais())?"":usuarioCapi.getPais());
					forma.setDepartamentoResidencia(UtilidadTexto.isEmpty(usuarioCapi.getDepartamento())?"":usuarioCapi.getDepartamento());
					forma.setMunicipioResidencia(UtilidadTexto.isEmpty(usuarioCapi.getCiudad())?"":usuarioCapi.getCiudad());
					if(UtilidadTexto.isEmpty(usuarioCapi.getLocalidad()))forma.setCodigoLocalidad("");else forma.setCodigoLocalidad(usuarioCapi.getLocalidad());
					forma.setNombreBarrio(UtilidadTexto.isEmpty(usuarioCapi.getBarrio())?"":usuarioCapi.getBarrio());
					if(usuarioCapi.getCodigoBarrio()==null)forma.setCodigoBarrio("");else forma.setCodigoBarrio(usuarioCapi.getCodigoBarrio().toString());
					forma.setExisteEnPersona(true);
				}
				else{
					forma.setExisteEnPersona(false);
					forma.setCodigoUsuarioCapitado(ConstantesBD.codigoNuncaValidoLong);
				}
			}
			HibernateUtil.endTransaction();
			forma.setVerSeccionCotizante(false);
			forma.setEstado("continuar");
			return hacerMapping(con, mapping, "principal");
		}
		catch(SQLException sqe)
		{
			HibernateUtil.abortTransaction();
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
	}
	
	private ActionForward accionSeleccionConvenio(Connection con, ActionMapping mapping, SubirPacienteForm forma, 
								UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores)
	{
		try{
			HibernateUtil.beginTransaction();
			if(forma.getConvenio()!= ConstantesBD.codigoNuncaValido){
				INaturalezaPacienteMundo naturalezaPacienteMundo = ManejoPacienteFabricaMundo.crearNaturalezaPacienteMundo();
				forma.setListadoContratos(SubirPaciente.consultarTipos(con, 2, usuario.getCodigoInstitucionInt(), forma.getConvenio()));
				forma.setListadoClasificacionSE(SubirPaciente.consultarTipos(con, 5, usuario.getCodigoInstitucionInt(), forma.getConvenio()));
				forma.setListadoNaturalezaPacientes(naturalezaPacienteMundo.listarNaturalezaPacientesPorConvenio(forma.getConvenio()));
				
				//Se valida que tenga datos ya sea en persona o en usuarios capitados
				if(forma.getExisteEnPersona()){
					//Se valida que exista en la tabla persona
					if(forma.getCodigoPersona()!=ConstantesBD.codigoNuncaValido){
						IUsuarioXConvenioMundo usuarioXConvenioMundo=CapitacionFabricaMundo.crearUsuarioXConvenioMundo();
						UsuarioXConvenio usuarioXConvenio=usuarioXConvenioMundo.buscarUsuarioConvenioRecientePorPaciente(forma.getCodigoPersona());
						if(usuarioXConvenio != null){
							if(usuarioXConvenio.getEstratosSociales()!= null){
								forma.setClasificacionSE(String.valueOf(usuarioXConvenio.getEstratosSociales().getCodigo()));
							}
							else{
								forma.setClasificacionSE("");
							}
							if(usuarioXConvenio.getNaturalezaPacientes()!= null){
								forma.setCodigoNaturaleza(usuarioXConvenio.getNaturalezaPacientes().getCodigo());
							}
							else{
								forma.setCodigoNaturaleza(ConstantesBD.codigoNuncaValido);
							}
							forma.setNumeroFicha(usuarioXConvenio.getNumeroFicha());
							if(usuarioXConvenio.getTiposAfiliado() != null){
								forma.setCodigoTipoAfiliado(String.valueOf(usuarioXConvenio.getTiposAfiliado().getAcronimo()));
							}
							else{
								forma.setCodigoTipoAfiliado("");
							}
							if(usuarioXConvenio.getCentroAtencion() != null){
								forma.setConsecutivoCentroAtencionSeleccionado(usuarioXConvenio.getCentroAtencion().getConsecutivo());
							}
							else{
								forma.setConsecutivoCentroAtencionSeleccionado(ConstantesBD.codigoNuncaValido);
							}
							forma.setAcronimoTipoIdEmpleador(usuarioXConvenio.getTipoIdEmpleador());
							forma.setNumIDEmpleador(usuarioXConvenio.getNumeroIdEmpleador());
							forma.setRazonSocial(usuarioXConvenio.getRazonSociEmpleador());
							if(forma.getCodigoTipoAfiliado().equals(ConstantesBD.codigoTipoAfiliadoBeneficiario)){
								forma.setVerSeccionCotizante(true);
								forma.setAcronimoTipoIdCotizante(usuarioXConvenio.getTipoIdCotizante());
								forma.setNumIDCotizante(usuarioXConvenio.getNumeroIdCotizante());
								forma.setNombresCotizante(usuarioXConvenio.getNombresCotizante());
								forma.setApellidosCotizante(usuarioXConvenio.getApellidosCotizante());
								if(usuarioXConvenio.getTiposParentesco() != null){
									forma.setParentescoCotizante(String.valueOf(usuarioXConvenio.getTiposParentesco().getCodigo()));
								}
								else{
									forma.setParentescoCotizante("");
								}
							}
							else{
								forma.setVerSeccionCotizante(false);
								forma.setAcronimoTipoIdCotizante("");
								forma.setNumIDCotizante("");
								forma.setNombresCotizante("");
								forma.setApellidosCotizante("");
								forma.setParentescoCotizante("");
							}
						}
					}
					//Indica que el usuario existe en la tabla usuarios capitados
					else{
						IConvUsuariosCapitadosMundo convUsuariosCapitadosMundo=CapitacionFabricaMundo.crearConvUsuariosCapitadosMundo();
						ConvUsuariosCapitados convUsuariosCapitados=convUsuariosCapitadosMundo
													.buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(forma.getCodigoUsuarioCapitado());
						if(convUsuariosCapitados != null){
							if(convUsuariosCapitados.getEstratosSociales()!= null){
								forma.setClasificacionSE(String.valueOf(convUsuariosCapitados.getEstratosSociales().getCodigo()));
							}
							else{
								forma.setClasificacionSE("");
							}
							if(convUsuariosCapitados.getNaturalezaPacientes()!= null){
								forma.setCodigoNaturaleza(convUsuariosCapitados.getNaturalezaPacientes().getCodigo());
							}
							else{
								forma.setCodigoNaturaleza(ConstantesBD.codigoNuncaValido);
							}
							forma.setNumeroFicha(convUsuariosCapitados.getNumeroFicha());
							if(convUsuariosCapitados.getTiposAfiliado() != null){
								forma.setCodigoTipoAfiliado(String.valueOf(convUsuariosCapitados.getTiposAfiliado().getAcronimo()));
							}
							else{
								forma.setCodigoTipoAfiliado("");
							}
							if(convUsuariosCapitados.getCentroAtencion() != null){
								forma.setConsecutivoCentroAtencionSeleccionado(convUsuariosCapitados.getCentroAtencion().getConsecutivo());
							}
							else{
								forma.setConsecutivoCentroAtencionSeleccionado(ConstantesBD.codigoNuncaValido);
							}
							forma.setAcronimoTipoIdEmpleador(convUsuariosCapitados.getTipoIdEmpleador());
							forma.setNumIDEmpleador(convUsuariosCapitados.getNumeroIdEmpleador());
							forma.setRazonSocial(convUsuariosCapitados.getRazonSociEmpleador());
							if(forma.getCodigoTipoAfiliado().equals(ConstantesBD.codigoTipoAfiliadoBeneficiario)){
								forma.setVerSeccionCotizante(true);
								forma.setAcronimoTipoIdCotizante(convUsuariosCapitados.getTipoIdCotizante());
								forma.setNumIDCotizante(convUsuariosCapitados.getNumeroIdCotizante());
								forma.setNombresCotizante(convUsuariosCapitados.getNombresCotizante());
								forma.setApellidosCotizante(convUsuariosCapitados.getApellidosCotizante());
								if(convUsuariosCapitados.getTiposParentesco() != null){
									forma.setParentescoCotizante(String.valueOf(convUsuariosCapitados.getTiposParentesco().getCodigo()));
								}
								else{
									forma.setParentescoCotizante("");
								}
							}
							else{
								forma.setVerSeccionCotizante(false);
								forma.setAcronimoTipoIdCotizante("");
								forma.setNumIDCotizante("");
								forma.setNombresCotizante("");
								forma.setApellidosCotizante("");
								forma.setParentescoCotizante("");
							}
						}
					}
				}
				else{
					cleanDatosPostulados(forma);
				}
			}
			else{
				cleanDatosPostulados(forma);
			}
			HibernateUtil.endTransaction();
		}
		catch(Exception e){
			logger.error(e);
			HibernateUtil.abortTransaction();
    		errores.add("Error Portulando datos por Convenios",new ActionMessage("errors.notEspecific",e.getMessage()));
    		saveErrors(request, errores);
    	}
    	forma.setEstado("continuar");
    	return hacerMapping(con, mapping, "principal");
	}
	
	
	private ActionForward accionPostularContrato(Connection con, ActionMapping mapping, SubirPacienteForm forma, 
			UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores)
	{
		try{
			if(forma.getConvenio()!= ConstantesBD.codigoNuncaValido){
				forma.setListadoContratos(SubirPaciente.consultarTipos(con, 2, usuario.getCodigoInstitucionInt(), forma.getConvenio()));
			}
			else{
				forma.setListadoContratos(null);
			}
		}catch(Exception e){
		logger.error(e);
		errores.add("Error Portulando datos por Convenios",new ActionMessage("errors.notEspecific",e.getMessage()));
		saveErrors(request, errores);
		}
		forma.setEstado("continuar");
		return hacerMapping(con, mapping, "principal");
		}
	
	
	/**
	 * Método encargado de limpiar los datos de la forma cuando no existan datos a postular
	 * @param forma
	 */
	private void cleanDatosPostulados(SubirPacienteForm forma){
		forma.setClasificacionSE("");
		forma.setCodigoNaturaleza(ConstantesBD.codigoNuncaValido);
		forma.setNumeroFicha("");
		forma.setTipoAfiliado("");
		forma.setConsecutivoCentroAtencionSeleccionado(ConstantesBD.codigoNuncaValido);
		forma.setAcronimoTipoIdEmpleador("");
		forma.setNumIDEmpleador("");
		forma.setRazonSocial("");
		forma.setVerSeccionCotizante(false);
		forma.setAcronimoTipoIdCotizante("");
		forma.setNumIDCotizante("");
		forma.setNombresCotizante("");
		forma.setApellidosCotizante("");
		forma.setParentescoCotizante("");
	}
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSeleccionConvenioCSE(Connection con, ActionMapping mapping, SubirPacienteForm forma, UsuarioBasico usuario)
	{
		forma.setListadoClasificacionSE(SubirPaciente.consultarTipos(con, 5, usuario.getCodigoInstitucionInt(), forma.getConvenio()));
		return hacerMapping(con, mapping, "listadoCSE");
	}
	
	
	
	/**
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarIndividual(Connection con, ActionMapping mapping, SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores)
	{
		try
		{
			listaSubirPacientesInc=new ArrayList<DTOSubirPacientesInconsistencias>();
			boolean carguePrevio=false;
			int idContratoCarguePrevio=ConstantesBD.codigoNuncaValido;
			List<DtoContrato> contratosCapitadosPersona=null;
			HibernateUtil.beginTransaction();
			if(forma.getCodigoPersona()!=ConstantesBD.codigoNuncaValido){
				IUsuarioXConvenioMundo usuarioXConvenioMundo=CapitacionFabricaMundo.crearUsuarioXConvenioMundo();
				contratosCapitadosPersona=usuarioXConvenioMundo.obtenerCarguesPreviosPorPersonaPorRangoFechas(forma.getCodigoPersona(), 
														UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()),
														UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
			}
			else{
				if(forma.getCodigoUsuarioCapitado()!=ConstantesBD.codigoNuncaValidoLong){
					IConvUsuariosCapitadosMundo convUsuariosCapitadosMundo=CapitacionFabricaMundo.crearConvUsuariosCapitadosMundo();
					contratosCapitadosPersona=convUsuariosCapitadosMundo.obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(forma.getCodigoUsuarioCapitado(), 
																UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()),
																UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
				}
			}
			// Se validan cargues previos tanto para el mismo convenio / contrato como para contratos de
			//diferentes convenios
			if(contratosCapitadosPersona != null && !contratosCapitadosPersona.isEmpty()){
				for(DtoContrato dtoContrato:contratosCapitadosPersona){
					if(forma.getConvenio() == dtoContrato.getConvenio()){
						if(forma.getContrato() == dtoContrato.getCodigo()){
							carguePrevio=true;
							idContratoCarguePrevio=dtoContrato.getCodigo();
							break;
						}
					}
					else{
						carguePrevio=true;
						idContratoCarguePrevio=dtoContrato.getCodigo();
						break;
					}
				}
			}
			
			if(carguePrevio){
				String nombreConvenio="";
				String numeroContrato="";
				if(idContratoCarguePrevio != ConstantesBD.codigoNuncaValido){
					IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
					Contratos contrato=contratoMundo.findById(idContratoCarguePrevio);
					numeroContrato=contrato.getNumeroContrato();
					nombreConvenio=contrato.getConvenios().getNombre();
				}
				errores.add("Usuario ya existe", new ActionMessage("error.subirPacientes.usuarioExiste", new Object[]{nombreConvenio, numeroContrato}));
				saveErrors(request, errores);
				forma.setEstado("continuar");
				HibernateUtil.endTransaction();
				return hacerMapping(con, mapping, "principal");	
			}
			forma.setListaFinalPersonasInconsistencias(new ArrayList<DTOSubirPacientesInconsistencias>());
			// se consultan las personas en la bd
			LogSubirPacientes logSubirPacientes=new LogSubirPacientes();
			
				
			if(forma.getCodigoPersona()!=ConstantesBD.codigoNuncaValido)
			{	
				logger.info("====>Se ingresa el paciente en la tabla usuario_x_convenio");
				IUsuarioXConvenioMundo usuarioXConvenioMundo = CapitacionFabricaMundo.crearUsuarioXConvenioMundo();
				UsuarioXConvenio usuarioXConvenio = new UsuarioXConvenio();
				Personas person = new Personas();
				Contratos contrato = new Contratos();
				TiposCargue tipoCargue = new TiposCargue();
				person.setCodigo(forma.getCodigoPersona());
				contrato.setCodigo(forma.getContrato());
				tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueIndividual);
				usuarioXConvenio.setPersonas(person);
				usuarioXConvenio.setContratos(contrato);
				usuarioXConvenio.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
				usuarioXConvenio.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
				usuarioXConvenio.setTiposCargue(tipoCargue);
				usuarioXConvenio.setFechaCargue(new Date());
				usuarioXConvenio.setUsuario(usuario.getLoginUsuario());
				usuarioXConvenio.setActivo(ConstantesBD.acronimoSi);
				if(forma.getClasificacionSE()!=null && !forma.getClasificacionSE().isEmpty()){
					EstratosSociales estratoSocial = new EstratosSociales();
					estratoSocial.setCodigo(Integer.parseInt(forma.getClasificacionSE()));
					usuarioXConvenio.setEstratosSociales(estratoSocial);
				}
				if(forma.getCodigoTipoAfiliado()!=null && !forma.getCodigoTipoAfiliado().isEmpty()){
					TiposAfiliado tipoAfiliado = new TiposAfiliado();
					tipoAfiliado.setAcronimo(forma.getCodigoTipoAfiliado().charAt(0));
					usuarioXConvenio.setTiposAfiliado(tipoAfiliado);
				}
				if(forma.getCodigoNaturaleza()!=ConstantesBD.codigoNuncaValido){
					NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
					naturalezaPacientes.setCodigo(forma.getCodigoNaturaleza());
					usuarioXConvenio.setNaturalezaPacientes(naturalezaPacientes);
				}
				if(forma.getConsecutivoCentroAtencionSeleccionado()!=ConstantesBD.codigoNuncaValido){
					CentroAtencion centroAtencion = new CentroAtencion();
					centroAtencion.setConsecutivo(forma.getConsecutivoCentroAtencionSeleccionado());
					usuarioXConvenio.setCentroAtencion(centroAtencion);
				}
				usuarioXConvenio.setTipoIdEmpleador(forma.getAcronimoTipoIdEmpleador());
				usuarioXConvenio.setNumeroIdEmpleador(forma.getNumIDEmpleador());
				usuarioXConvenio.setRazonSociEmpleador(forma.getRazonSocial());
				usuarioXConvenio.setTipoIdCotizante(forma.getAcronimoTipoIdCotizante());
				usuarioXConvenio.setNumeroIdCotizante(forma.getNumIDCotizante());
				usuarioXConvenio.setNombresCotizante(forma.getNombresCotizante());
				usuarioXConvenio.setApellidosCotizante(forma.getApellidosCotizante());
				if(forma.getParentescoCotizante() != null && !forma.getParentescoCotizante().isEmpty()){
					TiposParentesco parentesco = new TiposParentesco();
					parentesco.setCodigo(Integer.valueOf(forma.getParentescoCotizante()));
					usuarioXConvenio.setTiposParentesco(parentesco);
				}
				usuarioXConvenio.setNumeroFicha(forma.getNumeroFicha());
				try{
					usuarioXConvenioMundo.attachDirty(usuarioXConvenio);
					UtilidadTransaccion.getTransaccion().flush();
				}
				catch(Exception e){
					errores.add("Error Guardando Usuario Por Convenio",
							new ActionMessage("errors.notEspecific", e.getMessage()));
					saveErrors(request, errores);
					forma.setEstado("continuar");
					UtilidadTransaccion.getTransaccion().rollback();
					return mapping.findForward("principal");
				}				
			}
			else{	//EN LA TABLA USUARIOS CAPITADOS
				IUsuariosCapitadosMundo usuariosCapitadosMundo=CapitacionFabricaMundo.crearUsuariosCapitadosMundo();
				DtoUsuariosCapitados dtoUsuarioCapitado=new DtoUsuariosCapitados();
				DtoUsuariosCapitados parametrosBusqueda=new DtoUsuariosCapitados();
				parametrosBusqueda.setTipoIdentificacion(forma.getCodigoTipoId());
				parametrosBusqueda.setNumeroIdentificacion(forma.getNumId());
				ArrayList<DtoUsuariosCapitados> usuariosCapitados=usuariosCapitadosMundo.buscarUsuariosCapitados(parametrosBusqueda);
				if(usuariosCapitados != null && !usuariosCapitados.isEmpty()){
					dtoUsuarioCapitado=usuariosCapitados.get(0);
					if(dtoUsuarioCapitado.getCodigoUsuarioCapitado()!=ConstantesBD.codigoNuncaValidoLong){	
						logger.info("====>Se ingresa el paciente en la tabla conv_usuarios_capitados");
						IConvUsuariosCapitadosMundo convUsuariosCapitadosMundo = CapitacionFabricaMundo.crearConvUsuariosCapitadosMundo();
						ConvUsuariosCapitados convUsuariosCapitados = new ConvUsuariosCapitados();
						UsuariosCapitados usuarioCapitado = new UsuariosCapitados();
						Contratos contrato = new Contratos();
						TiposCargue tipoCargue = new TiposCargue();
						usuarioCapitado.setCodigo(dtoUsuarioCapitado.getCodigoUsuarioCapitado());
						contrato.setCodigo(forma.getContrato());
						tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueIndividual);
						convUsuariosCapitados.setUsuariosCapitados(usuarioCapitado);
						convUsuariosCapitados.setContratos(contrato);
						convUsuariosCapitados.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
						convUsuariosCapitados.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
						convUsuariosCapitados.setTiposCargue(tipoCargue);
						convUsuariosCapitados.setFechaCargue(new Date());
						convUsuariosCapitados.setUsuario(usuario.getLoginUsuario());
						convUsuariosCapitados.setActivo(ConstantesBD.acronimoSi);
						if(forma.getClasificacionSE()!=null && !forma.getClasificacionSE().isEmpty()){
							EstratosSociales estratoSocial = new EstratosSociales();
							estratoSocial.setCodigo(Integer.parseInt(forma.getClasificacionSE()));
							convUsuariosCapitados.setEstratosSociales(estratoSocial);
						}
						if(forma.getCodigoTipoAfiliado()!=null && !forma.getCodigoTipoAfiliado().isEmpty()){
							TiposAfiliado tipoAfiliado = new TiposAfiliado();
							tipoAfiliado.setAcronimo(forma.getCodigoTipoAfiliado().charAt(0));
							convUsuariosCapitados.setTiposAfiliado(tipoAfiliado);
						}
						if(forma.getCodigoNaturaleza()!=ConstantesBD.codigoNuncaValido){
							NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
							naturalezaPacientes.setCodigo(forma.getCodigoNaturaleza());
							convUsuariosCapitados.setNaturalezaPacientes(naturalezaPacientes);
						}
						if(forma.getConsecutivoCentroAtencionSeleccionado()!=ConstantesBD.codigoNuncaValido){
							CentroAtencion centroAtencion = new CentroAtencion();
							centroAtencion.setConsecutivo(forma.getConsecutivoCentroAtencionSeleccionado());
							convUsuariosCapitados.setCentroAtencion(centroAtencion);
						}
						convUsuariosCapitados.setTipoIdEmpleador(forma.getAcronimoTipoIdEmpleador());
						convUsuariosCapitados.setNumeroIdEmpleador(forma.getNumIDEmpleador());
						convUsuariosCapitados.setRazonSociEmpleador(forma.getRazonSocial());
						convUsuariosCapitados.setTipoIdCotizante(forma.getAcronimoTipoIdCotizante());
						convUsuariosCapitados.setNumeroIdCotizante(forma.getNumIDCotizante());
						convUsuariosCapitados.setNombresCotizante(forma.getNombresCotizante());
						convUsuariosCapitados.setApellidosCotizante(forma.getApellidosCotizante());
						if(forma.getParentescoCotizante() != null && !forma.getParentescoCotizante().isEmpty()){
							TiposParentesco parentesco = new TiposParentesco();
							parentesco.setCodigo(Integer.valueOf(forma.getParentescoCotizante()));
							convUsuariosCapitados.setTiposParentesco(parentesco);
						}
						convUsuariosCapitados.setNumeroFicha(forma.getNumeroFicha());
						try{
							convUsuariosCapitadosMundo.attachDirty(convUsuariosCapitados);
							UtilidadTransaccion.getTransaccion().flush();
						}
						catch(Exception e){
							errores.add("Error Guardando Conv Usuarios Capitados",
									new ActionMessage("errors.notEspecific", e.getMessage()));
							saveErrors(request, errores);
							forma.setEstado("continuar");
							UtilidadTransaccion.getTransaccion().rollback();
							return mapping.findForward("principal");
						}
					}
				}
				else{
					
					UtilidadBD.iniciarTransaccion(con);
					//VERIFICAR SI TIPO Y NUMERO ID EXISTE EN LA TABLA PERSONAS
					PersonaBasica persona = this.consultarPersona(con, forma.getCodigoTipoId(), forma.getNumId());
					DtoPersonas dtoPersonaForma=new DtoPersonas();
					dtoPersonaForma.setTipoIdentificacion(forma.getCodigoTipoId());
					dtoPersonaForma.setNumeroIdentificacion(forma.getNumId());					
					dtoPersonaForma.setPrimerNombre(forma.getPrimerNombrePersona());
					dtoPersonaForma.setSegundoNombre(forma.getSegundoNombrePersona());
					dtoPersonaForma.setPrimerApellido(forma.getPrimerApellidoPersona());
					dtoPersonaForma.setSegundoApellido(forma.getSegundoApellidoPersona());
					dtoPersonaForma.setFechaNacimiento(forma.getFechaNacimiento());
					//se valida inconsistencias por ID si existe la persona y se comparan nombres, sino se busca por nombres y se genera inconsistencia
					validarPersona(con, forma, dtoPersonaForma, persona, 0, logSubirPacientes);
									
					if(forma.isComparaNomApeIndividual())
					{
						forma.setEstado("continuar");
						UtilidadTransaccion.getTransaccion().commit();
						UtilidadBD.finalizarTransaccion(con);
						return hacerMapping(con, mapping, "principal");
					}
					
					if(forma.isMostrarPopUpIndividual())
					{
						forma.setEstado("continuar");
						UtilidadTransaccion.getTransaccion().commit();
						UtilidadBD.finalizarTransaccion(con);
						return hacerMapping(con, mapping, "principal");
					}
					logger.info("====>Se ingresa el paciente en la tabla usuarios_capitados y en la tabla conv_usuarios_capitados");
					UsuariosCapitados usuarioCapitado = new UsuariosCapitados();
					String []separador=forma.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit);
					forma.setCodigoDepartamentoResidencia(separador[0]);
					forma.setCodigoCiudadResidencia(separador[1]);
					Sexo sexo= new Sexo();
					usuarioCapitado.setNumeroIdentificacion(forma.getNumId());
					usuarioCapitado.setTipoIdentificacion(forma.getCodigoTipoId());
					usuarioCapitado.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaNacimiento()));
					sexo.setCodigo(forma.getSexo());
					usuarioCapitado.setSexo(sexo);
					usuarioCapitado.setPrimerNombre(forma.getPrimerNombrePersona());
					usuarioCapitado.setSegundoNombre(forma.getSegundoNombrePersona());
					usuarioCapitado.setPrimerApellido(forma.getPrimerApellidoPersona());
					usuarioCapitado.setSegundoApellido(forma.getSegundoApellidoPersona());
					usuarioCapitado.setDireccion(forma.getDireccion());
					usuarioCapitado.setTelefono(forma.getTelefono());
					usuarioCapitado.setEmail(forma.getEmail());
					if(forma.getCodigoCiudadResidencia() != null && !forma.getCodigoCiudadResidencia().isEmpty()){
						CiudadesHome ciudadH= new CiudadesHome();
						CiudadesId idCiudad = new CiudadesId();
						idCiudad.setCodigoPais(forma.getCodigoPaisResidencia());
						idCiudad.setCodigoDepartamento(forma.getCodigoDepartamentoResidencia());
						idCiudad.setCodigoCiudad(forma.getCodigoCiudadResidencia());
						Ciudades ciudad = new Ciudades();
						ciudad = ciudadH.findById(idCiudad);
						usuarioCapitado.setCiudades(ciudad);
						if(forma.getCodigoLocalidad() != null && !forma.getCodigoLocalidad().isEmpty()){
							usuarioCapitado.setLocalidad(forma.getCodigoLocalidad());
						}
					}
					if(forma.getCodigoBarrio() != null && !forma.getCodigoBarrio().isEmpty()){
						Barrios barrio = new Barrios();
						barrio.setCodigo(Integer.valueOf(forma.getCodigoBarrio()));
						usuarioCapitado.setBarrios(barrio);
					}
					try{
						usuarioCapitado=usuariosCapitadosMundo.merge(usuarioCapitado);
						UtilidadTransaccion.getTransaccion().flush();
					}
					catch(Exception e){
						errores.add("Error Guardando Usuario Capitado",
								new ActionMessage("errors.notEspecific", e.getMessage()));
						saveErrors(request, errores);
						forma.setEstado("continuar");
						UtilidadTransaccion.getTransaccion().rollback();
						return mapping.findForward("principal");
					}
					
					IConvUsuariosCapitadosMundo convUsuariosCapitadosMundo = CapitacionFabricaMundo.crearConvUsuariosCapitadosMundo();
					ConvUsuariosCapitados convUsuariosCapitados = new ConvUsuariosCapitados();
					Contratos contrato = new Contratos();
					TiposCargue tipoCargue = new TiposCargue();
					contrato.setCodigo(forma.getContrato());
					tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueIndividual);
					convUsuariosCapitados.setUsuariosCapitados(usuarioCapitado);
					convUsuariosCapitados.setContratos(contrato);
					convUsuariosCapitados.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
					convUsuariosCapitados.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
					convUsuariosCapitados.setTiposCargue(tipoCargue);
					convUsuariosCapitados.setFechaCargue(new Date());
					convUsuariosCapitados.setUsuario(usuario.getLoginUsuario());
					convUsuariosCapitados.setActivo(ConstantesBD.acronimoSi);
					if(forma.getClasificacionSE()!=null && !forma.getClasificacionSE().isEmpty()){
						EstratosSociales estratoSocial = new EstratosSociales();
						estratoSocial.setCodigo(Integer.parseInt(forma.getClasificacionSE()));
						convUsuariosCapitados.setEstratosSociales(estratoSocial);
					}
					if(forma.getCodigoTipoAfiliado()!=null && !forma.getCodigoTipoAfiliado().isEmpty()){
						TiposAfiliado tipoAfiliado = new TiposAfiliado();
						tipoAfiliado.setAcronimo(forma.getCodigoTipoAfiliado().charAt(0));
						convUsuariosCapitados.setTiposAfiliado(tipoAfiliado);
					}
					if(forma.getCodigoNaturaleza()!=ConstantesBD.codigoNuncaValido){
						NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
						naturalezaPacientes.setCodigo(forma.getCodigoNaturaleza());
						convUsuariosCapitados.setNaturalezaPacientes(naturalezaPacientes);
					}
					if(forma.getConsecutivoCentroAtencionSeleccionado()!=ConstantesBD.codigoNuncaValido){
						CentroAtencion centroAtencion = new CentroAtencion();
						centroAtencion.setConsecutivo(forma.getConsecutivoCentroAtencionSeleccionado());
						convUsuariosCapitados.setCentroAtencion(centroAtencion);
					}
					convUsuariosCapitados.setTipoIdEmpleador(forma.getAcronimoTipoIdEmpleador());
					convUsuariosCapitados.setNumeroIdEmpleador(forma.getNumIDEmpleador());
					convUsuariosCapitados.setRazonSociEmpleador(forma.getRazonSocial());
					convUsuariosCapitados.setTipoIdCotizante(forma.getAcronimoTipoIdCotizante());
					convUsuariosCapitados.setNumeroIdCotizante(forma.getNumIDCotizante());
					convUsuariosCapitados.setNombresCotizante(forma.getNombresCotizante());
					convUsuariosCapitados.setApellidosCotizante(forma.getApellidosCotizante());
					if(forma.getParentescoCotizante() != null && !forma.getParentescoCotizante().isEmpty()){
						TiposParentesco parentesco = new TiposParentesco();
						parentesco.setCodigo(Integer.valueOf(forma.getParentescoCotizante()));
						convUsuariosCapitados.setTiposParentesco(parentesco);
					}
					convUsuariosCapitados.setNumeroFicha(forma.getNumeroFicha());
					try{
						convUsuariosCapitadosMundo.attachDirty(convUsuariosCapitados);
						UtilidadTransaccion.getTransaccion().flush();
					}
					catch(Exception e){
						errores.add("Error Guardando Conv Usuarios Capitados",
								new ActionMessage("errors.notEspecific", e.getMessage()));
						saveErrors(request, errores);
						forma.setEstado("continuar");
						UtilidadTransaccion.getTransaccion().rollback();
						return mapping.findForward("principal");
					}
					UtilidadBD.finalizarTransaccion(con);
				}						
			}		
			forma.setSeGuardoExitosamente(true);		   								
			guardarLogIndividual(forma, usuario, ConstantesBD.codigoTipoCargueIndividual);
			forma.setEstado("continuar");
    		HibernateUtil.endTransaction();
			return hacerMapping(con, mapping, "resumen");				
		}
		catch(SQLException sqe)
		{
			HibernateUtil.abortTransaction();
			forma.setEstado("continuar");
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		catch(Exception e){
			HibernateUtil.abortTransaction();
    		errores.add("Error Guardando Usuario Capitado",
					new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
			forma.setEstado("continuar");
			return mapping.findForward("principal");
    	}
	}
	
	
	/**
	 * Metodo que se encarga de guardar la persona Individual con nombres iguales pero con diferente TIPO y NUM ID 
	 * en subirPaciente Individual.
	 *  
	 * @param forma
	 * @return
	 */
	private ActionForward guardarPersonaIndividualIgualNombres(Connection con, ActionMapping mapping, SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores)
	{
		try{
			HibernateUtil.beginTransaction();
			IUsuariosCapitadosMundo usuariosCapitadosMundo=CapitacionFabricaMundo.crearUsuariosCapitadosMundo(); 
			logger.info("====>Se ingresa el paciente en la tabla usuarios_capitados y en la tabla conv_usuarios_capitados");
			UsuariosCapitados usuarioCapitado = new UsuariosCapitados();
			String []separador=forma.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit);
			forma.setCodigoDepartamentoResidencia(separador[0]);
			forma.setCodigoCiudadResidencia(separador[1]);
			Sexo sexo= new Sexo();
			usuarioCapitado.setNumeroIdentificacion(forma.getNumId());
			usuarioCapitado.setTipoIdentificacion(forma.getCodigoTipoId());
			usuarioCapitado.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaNacimiento()));
			sexo.setCodigo(forma.getSexo());
			usuarioCapitado.setSexo(sexo);
			usuarioCapitado.setPrimerNombre(forma.getPrimerNombrePersona());
			usuarioCapitado.setSegundoNombre(forma.getSegundoNombrePersona());
			usuarioCapitado.setPrimerApellido(forma.getPrimerApellidoPersona());
			usuarioCapitado.setSegundoApellido(forma.getSegundoApellidoPersona());
			usuarioCapitado.setDireccion(forma.getDireccion());
			usuarioCapitado.setTelefono(forma.getTelefono());
			usuarioCapitado.setEmail(forma.getEmail());
			if(forma.getCodigoCiudadResidencia() != null && !forma.getCodigoCiudadResidencia().isEmpty()){
				CiudadesHome ciudadH= new CiudadesHome();
				CiudadesId idCiudad = new CiudadesId();
				idCiudad.setCodigoPais(forma.getCodigoPaisResidencia());
				idCiudad.setCodigoDepartamento(forma.getCodigoDepartamentoResidencia());
				idCiudad.setCodigoCiudad(forma.getCodigoCiudadResidencia());
				Ciudades ciudad = new Ciudades();
				ciudad = ciudadH.findById(idCiudad);
				usuarioCapitado.setCiudades(ciudad);
				if(forma.getCodigoLocalidad() != null && !forma.getCodigoLocalidad().isEmpty()){
					usuarioCapitado.setLocalidad(forma.getCodigoLocalidad());
				}
			}
			if(forma.getCodigoBarrio() != null && !forma.getCodigoBarrio().isEmpty()){
				Barrios barrio = new Barrios();
				barrio.setCodigo(Integer.valueOf(forma.getCodigoBarrio()));
				usuarioCapitado.setBarrios(barrio);
			}
			try{
				usuarioCapitado=usuariosCapitadosMundo.merge(usuarioCapitado);
				UtilidadTransaccion.getTransaccion().flush();
			}
			catch(Exception e){
				errores.add("Error Guardando Usuario Capitado",
						new ActionMessage("errors.notEspecific", e.getMessage()));
				saveErrors(request, errores);
				forma.setEstado("continuar");
				HibernateUtil.abortTransaction();
				return mapping.findForward("principal");
			}
			
			IConvUsuariosCapitadosMundo convUsuariosCapitadosMundo = CapitacionFabricaMundo.crearConvUsuariosCapitadosMundo();
			ConvUsuariosCapitados convUsuariosCapitados = new ConvUsuariosCapitados();
			Contratos contrato = new Contratos();
			TiposCargue tipoCargue = new TiposCargue();
			contrato.setCodigo(forma.getContrato());
			tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueIndividual);
			convUsuariosCapitados.setUsuariosCapitados(usuarioCapitado);
			convUsuariosCapitados.setContratos(contrato);
			convUsuariosCapitados.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
			convUsuariosCapitados.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
			convUsuariosCapitados.setTiposCargue(tipoCargue);
			convUsuariosCapitados.setFechaCargue(new Date());
			convUsuariosCapitados.setUsuario(usuario.getLoginUsuario());
			convUsuariosCapitados.setActivo(ConstantesBD.acronimoSi);
			if(forma.getClasificacionSE()!=null && !forma.getClasificacionSE().isEmpty()){
				EstratosSociales estratoSocial = new EstratosSociales();
				estratoSocial.setCodigo(Integer.parseInt(forma.getClasificacionSE()));
				convUsuariosCapitados.setEstratosSociales(estratoSocial);
			}
			if(forma.getCodigoTipoAfiliado()!=null && !forma.getCodigoTipoAfiliado().isEmpty()){
				TiposAfiliado tipoAfiliado = new TiposAfiliado();
				tipoAfiliado.setAcronimo(forma.getCodigoTipoAfiliado().charAt(0));
				convUsuariosCapitados.setTiposAfiliado(tipoAfiliado);
			}
			if(forma.getCodigoNaturaleza()!=ConstantesBD.codigoNuncaValido){
				NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
				naturalezaPacientes.setCodigo(forma.getCodigoNaturaleza());
				convUsuariosCapitados.setNaturalezaPacientes(naturalezaPacientes);
			}
			if(forma.getConsecutivoCentroAtencionSeleccionado()!=ConstantesBD.codigoNuncaValido){
				CentroAtencion centroAtencion = new CentroAtencion();
				centroAtencion.setConsecutivo(forma.getConsecutivoCentroAtencionSeleccionado());
				convUsuariosCapitados.setCentroAtencion(centroAtencion);
			}
			convUsuariosCapitados.setTipoIdEmpleador(forma.getAcronimoTipoIdEmpleador());
			convUsuariosCapitados.setNumeroIdEmpleador(forma.getNumIDEmpleador());
			convUsuariosCapitados.setRazonSociEmpleador(forma.getRazonSocial());
			convUsuariosCapitados.setTipoIdCotizante(forma.getAcronimoTipoIdCotizante());
			convUsuariosCapitados.setNumeroIdCotizante(forma.getNumIDCotizante());
			convUsuariosCapitados.setNombresCotizante(forma.getNombresCotizante());
			convUsuariosCapitados.setApellidosCotizante(forma.getApellidosCotizante());
			if(forma.getParentescoCotizante() != null && !forma.getParentescoCotizante().isEmpty()){
				TiposParentesco parentesco = new TiposParentesco();
				parentesco.setCodigo(Integer.valueOf(forma.getParentescoCotizante()));
				convUsuariosCapitados.setTiposParentesco(parentesco);
			}
			convUsuariosCapitados.setNumeroFicha(forma.getNumeroFicha());
			try{
				convUsuariosCapitadosMundo.attachDirty(convUsuariosCapitados);
				UtilidadTransaccion.getTransaccion().flush();
			}
			catch(Exception e){
				errores.add("Error Guardando Conv Usuarios Capitados",
						new ActionMessage("errors.notEspecific", e.getMessage()));
				saveErrors(request, errores);
				forma.setEstado("continuar");
				HibernateUtil.abortTransaction();
				return mapping.findForward("principal");
			}	
			forma.setSeGuardoExitosamente(true);		   								
			guardarLogIndividual(forma, usuario, ConstantesBD.codigoTipoCargueIndividual);
			forma.setEstado("continuar");
			HibernateUtil.endTransaction();
			return hacerMapping(con, mapping, "resumen");
		}
		catch(Exception e){
			HibernateUtil.abortTransaction();
			errores.add("Error Guardando Usuario Capitado",
					new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
			forma.setEstado("continuar");
			return mapping.findForward("principal");
		}
	}
	
	
	/**
	 * Verifica si en el listado de ciudades se encuentra el elemento a verificar
	 * @param listaCiudades
	 * @param paisAVerificar
	 * @param departamentoAVerificar
	 * @param ciudadAVerificar
	 * @return
	 */
	private boolean verificarCiudadDepartamentoPais(List<DtoCiudad> listaCiudades, String codigoPais, String codigoDepartamento, String codigoCiudad)
	{
		if(listaCiudades != null && !listaCiudades.isEmpty()){
			if(codigoPais != null && codigoDepartamento != null && codigoCiudad != null){
				for(DtoCiudad ciudad:listaCiudades)
				{
					if(ciudad.getCodigoPais().equals(codigoPais)
							&& ciudad.getCodigoDepartamento().equals(codigoDepartamento)
							&& ciudad.getCodigoCiudad().equals(codigoCiudad)){	
						return true;						
					}
				}
			}
			else if(codigoPais != null && codigoDepartamento != null){
				for(DtoCiudad ciudad:listaCiudades)
				{
					if(ciudad.getCodigoPais().equals(codigoPais)
							&& ciudad.getCodigoDepartamento().equals(codigoDepartamento)){	
						return true;						
					}
				}
			}
			else if(codigoPais != null){
				for(DtoCiudad ciudad:listaCiudades)
				{
					if(ciudad.getCodigoPais().equals(codigoPais)){	
						return true;						
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Verifica si en el listado de barrios se encuentra el elemento a verificar
	 * @param listaBarrios
	 * @param paisAVerificar
	 * @param departamentoAVerificar
	 * @param ciudadAVerificar
	 * @param barrioAVerificar
	 * @return
	 */
	private boolean verificarBarrio(List<DtoBarrio> listaBarrios, String codigoPais, String codigoDepartamento, 
						String codigoCiudad, String codigoBarrio){
		if(listaBarrios != null && !listaBarrios.isEmpty()){
			if(codigoPais != null && codigoDepartamento != null && codigoCiudad != null
					&& codigoBarrio != null){
				for(DtoBarrio barrio:listaBarrios)
				{
					if(barrio.getCodigoPais().equals(codigoPais)
							&& barrio.getCodigoDepartamento().equals(codigoDepartamento)
							&& barrio.getCodigoCiudad().equals(codigoCiudad)
							&& barrio.getCodigoBarrio().equals(codigoBarrio)){	
						return true;						
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Verifica si en el listado de localidades se encuentra el elemento a verificar
	 * @param listaLocalidades
	 * @param paisAVerificar
	 * @param departamentoAVerificar
	 * @param ciudadAVerificar
	 * @param localidadAVerificar
	 * @return
	 */
	private boolean verificarLocalidad(List<DtoLocalidad> listaLocalidades, String codigoPais, String codigoDepartamento, 
						String codigoCiudad, String codigoLocalidad){
		if(listaLocalidades != null && !listaLocalidades.isEmpty()){
			if(codigoPais != null && codigoDepartamento != null && codigoCiudad != null
					&& codigoLocalidad != null){
				for(DtoLocalidad localidad:listaLocalidades)
				{
					if(localidad.getCodigoPais().equals(codigoPais)
							&& localidad.getCodigoDepartamento().equals(codigoDepartamento)
							&& localidad.getCodigoCiudad().equals(codigoCiudad)
							&& localidad.getCodigoLocalidad().equals(codigoLocalidad)){	
						return true;						
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Verifica si en la collection suministrada si se encuentra el elemento a verificar
	 * @param Collection
	 * @param key
	 * @param valorAVerificar
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	private boolean verificarCollection(Collection collection, String key, String valorAVerificar)
	{
		Iterator iterator=collection.iterator();
		while(iterator.hasNext())
		{
			HashMap mapa=(HashMap)iterator.next();
			String codElemento=mapa.get(key).toString();
			if(codElemento.equals(valorAVerificar)){	
				return true;						
			}
		}
		return false;
	}
	
	/**
	 * Verifica si en el ArrayList suministrado si se encuentra el elemento a verificar
	 * @param ArrayList<DTONaturalezaPaciente>
	 * @param valorAVerificar
	 * @return
	 */
	private boolean verificarNaturalezaPaciente(ArrayList<DTONaturalezaPaciente> lista, int valorAVerificar)
	{
		for(DTONaturalezaPaciente naturaleza:lista)		{
			if(naturaleza.getCodigo()==valorAVerificar){	
				return true;						
			}
		}
		return false;
	}
	

	/**
	 * Verifica si en el ArrayList suministrado si se encuentra el elemento a verificar
	 * @param ArrayList<DtoCentroAtencion>
	 * @param valorAVerificar
	 * @return
	 */
	private boolean verificarCentroAtencion(ArrayList<DtoCentrosAtencion> lista, int valorAVerificar)
	{
		for(DtoCentrosAtencion centroAtencion:lista)		{
			if(centroAtencion.getConsecutivo()==valorAVerificar){	
				return true;						
			}
		}
		return false;
	}
	
	/**
	 * Método para empezar el flujo
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, SubirPacienteForm forma, HttpServletRequest request, UsuarioBasico usuario)
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		Collection coleccionTemporal=SubirPaciente.consultarTipos(con, 1, usuario.getCodigoInstitucionInt(), 0);
		if(coleccionTemporal==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}

		forma.setListadoConvenios(coleccionTemporal);
		coleccionTemporal=SubirPaciente.consultarTipos(con, 3, usuario.getCodigoInstitucionInt(), 0);
		if(coleccionTemporal==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		forma.setListadoTiposId(coleccionTemporal);
		coleccionTemporal=SubirPaciente.consultarTipos(con, 4, 0, 0);
		if(coleccionTemporal==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		forma.setListadoSexos(coleccionTemporal);
		coleccionTemporal=SubirPaciente.consultarTipos(con, 6, 0, 0);
		if(coleccionTemporal==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		forma.setListadoTiposParentezco(coleccionTemporal);
		
		coleccionTemporal=SubirPaciente.consultarTipos(con, 7, 0, 0);
		if(coleccionTemporal==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		forma.setListadoTiposAfiliado(coleccionTemporal);
		
		
		ArrayList<HashMap<String,Object>> localidades=Utilidades.obtenerLocalidades(con, "", "", "");
		forma.setLocalidades(localidades);
		
		//*****CARGAR ESTRUCTURAS***********************
		forma.setPaises(Utilidades.obtenerPaises(con));
		//********************************************
		
		//**************SE POSTULA LA INFORMACION POR DEFECTO***********************************
		
		//Consulta la parametrizacion de paises de la institucin
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, usuario.getCodigoInstitucionInt());
		if(!mundoInstitucion.getPais().getCodigo().equals(""))
		{
			forma.setCodigoPaisId(mundoInstitucion.getPais().getCodigo());
			forma.setCodigoPaisResidencia(mundoInstitucion.getPais().getCodigo());
			
			forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
		
		}
		
	//Consulta del parametro paï¿½s residencia
		String paisResidencia = ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt());
		String ciudadResidencia = ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(paisResidencia)&&!paisResidencia.equals(" - "))
		{
			forma.setCodigoPaisResidencia(paisResidencia.split("-")[0]);
			forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, paisResidencia.split("-")[0]));

			if(UtilidadCadena.noEsVacio(ciudadResidencia)&&!ciudadResidencia.equals(" - - "))
			{
				
				forma.setCodigoCiudadResidencia(ciudadResidencia.split("-")[0]+ConstantesBD.separadorSplit+ciudadResidencia.split("-")[1]);					
			}
		}
		
		//Consulta del barrio
		String barrio = ValoresPorDefecto.getBarrioResidencia(usuario.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(barrio))
		{
			forma.setCodigoBarrio(barrio.split("-")[2]);
			forma.setNombreBarrio(barrio.split("-")[0]+"-"+barrio.split("-")[1]);
			
		}
		
		if(!UtilidadTexto.isEmpty(paisResidencia)&&!UtilidadTexto.isEmpty(ciudadResidencia)&&!UtilidadTexto.isEmpty(barrio)){
			
			ArrayList<HashMap<String,Object>> localidad=Utilidades.obtenerLocalidadDeBarrio(con, forma.getCodigoPaisResidencia(),
					forma.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0], 
					forma.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1],
					forma.getCodigoBarrio());
			
			if(!localidad.isEmpty()){
				forma.setCodigoLocalidad(localidad.get(0).get("codigoLocalidad").toString());
				forma.setNombreLocalidad(localidad.get(0).get("nombreLocalidad").toString());
			}
			
		}

		//Add Javier por MT 5939		
		try {
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			forma.setCentrosAtencion((ArrayList<DtoCentrosAtencion>) manejoPacienteFacade.listarTodosCentrosAtencion(true, Boolean.TRUE, usuario.getCodigoInstitucionInt()));
		} catch (Exception e) {
			Log4JManager.error(e);
		}
		//Fin Javier		
		
		
		ArrayList<HashMap<String, Object>> tiposID=Utilidades.obtenerTiposIdentificacion(con, "", usuario.getCodigoInstitucionInt());
		forma.setTiposIDEmpleador(tiposID);
		
		ArrayList<HashMap<String, Object>> tiposIDCotizante=Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuario.getCodigoInstitucionInt());
		forma.setTiposIDCotizante(tiposIDCotizante);
		
		return hacerMapping(con, mapping, "principal");
	}


	/**
	 * @param con
	 * @param string
	 * @param mapping 
	 * @return
	 */
	private ActionForward hacerMapping(Connection con, ActionMapping mapping, String forward)
	{
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión "+e);
		}
		return mapping.findForward(forward);
	}

	@SuppressWarnings("deprecation")
	private boolean validarPersona(Connection con, SubirPacienteForm forma, DtoPersonas personaArchivoPlano, PersonaBasica personaBD,int linea,LogSubirPacientes logSubirPacientes)
	{		
		boolean personasSinInconsistencia=true;
		
		//Agrega la persona de la BD a la lista*/
		ArrayList<DtoPersonas>listaPersonasInconsistenciaBD=new ArrayList<DtoPersonas>();
		/**se debe verificar por los nombres y apellidos del usuario capitado con los de la tabla personas.*/
		DTOSubirPacientesInconsistencias dTOSubirPacientesInconsistencias=new DTOSubirPacientesInconsistencias();
		
		String tempTipoId=personaArchivoPlano.getTipoIdentificacion();
		String tempNumeId=personaArchivoPlano.getNumeroIdentificacion();
		
		//Se setea a null el tipoID y el numeroID para que solo busque por nombres y apellidos
		personaArchivoPlano.setTipoIdentificacion(null);
		personaArchivoPlano.setNumeroIdentificacion(null);
		
		//Busca las personas por nombres y apellidos en la BD
		IPersonasServicio personasServicio=AdministracionFabricaServicio.crearPersonaServicio();
		listaPersonasInconsistenciaBD=personasServicio.buscarPersonasPorNombresApellidos(personaArchivoPlano);

		if(!Utilidades.isEmpty(listaPersonasInconsistenciaBD))
		{				
			forma.setMostrarPopUpIndividual(true);
			ArrayList<DtoPersonas>listaTempoPersonaInconBD=new ArrayList<DtoPersonas>();
			
			for (DtoPersonas newDtoPersonas : listaPersonasInconsistenciaBD) {
				newDtoPersonas.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoNumIDNoCorresponde);
				newDtoPersonas.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(newDtoPersonas.getFechaNacimientoTipoDate()+""));
				newDtoPersonas.setTipoId(newDtoPersonas.getTipoId());				
					listaTempoPersonaInconBD.add(newDtoPersonas);
			}
												
			personaArchivoPlano.setTipoIdentificacion(tempTipoId);	//se setean nuevamente los valores del temporal
			personaArchivoPlano.setNumeroIdentificacion(tempNumeId);
			/*Se llena el dto principal (DTOSubirPacientesInconsistencias) que contiene la persona ingresada
			 * y la lista de las personas encontradas en la Base de Datos */ 
			dTOSubirPacientesInconsistencias.setDtoPersonaArchivoPlano(personaArchivoPlano);
			//dTOSubirPacientesInconsistencias.setListaPersonasBD(listaPersonasInconsistenciaBD);
			dTOSubirPacientesInconsistencias.setListaPersonasBD(listaTempoPersonaInconBD);
			
			//Se adiciona a la lista en la forma
			forma.getListaFinalPersonasInconsistencias().add(dTOSubirPacientesInconsistencias);
			listaSubirPacientesInc.add(dTOSubirPacientesInconsistencias);
			personaBD.setCodigoPersona(1);//Se setea el codigo porque encontro las personas por nombres y no por ID
			personasSinInconsistencia=false;
		}
		return personasSinInconsistencia;
	}
	
	/**
	 * Metodo que se encarga de leer los campos del archivo que pasaron las validaciones para validar las inconsistencias 
	 * de las personas.
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private boolean accionGuardarMasivo(SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		try
		{   
			listaInconsistenciaCarguePrevio=new ArrayList<DtoInconsistenciasArchivoPlano>();
			if(!listaPersonaArchivoPlano.isEmpty()){
				//Se guardan las lineas que no tienen inconsistencia
				IPersonas personasMundo=AdministracionFabricaMundo.crearPersonasMundo();
				IUsuarioXConvenioMundo usuarioXConvenioMundo=CapitacionFabricaMundo.crearUsuarioXConvenioMundo();
				IUsuariosCapitadosMundo usuariosCapitadosMundo=CapitacionFabricaMundo.crearUsuariosCapitadosMundo();
				IConvUsuariosCapitadosMundo convUsuariosCapitadosMundo=CapitacionFabricaMundo.crearConvUsuariosCapitadosMundo();
				IBarriosMundo barriosMundo = AdministracionFabricaMundo.crearBarriosMundo();
				
				for(DtoPersonas personaArchivo: listaPersonaArchivoPlano){
					DtoPersonas filtroPersona=new DtoPersonas();
					filtroPersona.setTipoIdentificacion(personaArchivo.getTipoIdentificacion());
					filtroPersona.setNumeroIdentificacion(personaArchivo.getNumeroIdentificacion());
					List<DtoPersonas> personasBD=personasMundo.buscarPersonasPorFiltro(filtroPersona);
					int codigoPersona=ConstantesBD.codigoNuncaValido;
					long codigoUsuarioCapitado=ConstantesBD.codigoNuncaValidoLong;
					if(personasBD != null && !personasBD.isEmpty()){
						codigoPersona=personasBD.get(0).getCodigo();
					}
					else{
						Long codigoUCapitado =usuariosCapitadosMundo.buscarCodigoUsuarioCapitadoPorTipoNumeroID(
													personaArchivo.getTipoIdentificacion(), personaArchivo.getNumeroIdentificacion());
						if(codigoUCapitado != null){
							codigoUsuarioCapitado=codigoUCapitado.longValue();
						}
					}
					//VALIDACION EXISTE CARGUE PREVIO DENTRO DEL RANGO DE FECHAS PARA EL USUARIO
					boolean carguePrevio=false;
					List<DtoContrato> contratosCapitadosPersona=null;
					if(codigoPersona!=ConstantesBD.codigoNuncaValido){
						contratosCapitadosPersona=usuarioXConvenioMundo.obtenerCarguesPreviosPorPersonaPorRangoFechas(codigoPersona, 
																UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()),
																UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
					}
					else{
						if(codigoUsuarioCapitado!=ConstantesBD.codigoNuncaValidoLong){
							contratosCapitadosPersona=convUsuariosCapitadosMundo.obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(codigoUsuarioCapitado, 
																		UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()),
																		UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
						}
					}
					// Se validan cargues previos tanto para el mismo convenio / contrato como para contratos de
					//diferentes convenios
					if(contratosCapitadosPersona != null && !contratosCapitadosPersona.isEmpty()){
						for(DtoContrato dtoContrato:contratosCapitadosPersona){
							if(forma.getConvenio() == dtoContrato.getConvenio()){
								if(forma.getContrato() == dtoContrato.getCodigo()){
									carguePrevio=true;
									break;
								}
							}
							else{
								carguePrevio=true;
								break;
							}
						}
					}
					
					if(carguePrevio){
						dtoInconsistenciaCarguePrevio=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCarguePrevioMismoPeriodo,
								personaArchivo, personaArchivo.getNumeroRegistro());
						listaInconsistenciaCarguePrevio.add(dtoInconsistenciaCarguePrevio);	
					}
					else{
						if(codigoPersona != ConstantesBD.codigoNuncaValido){
							//Se guarda en la tabla USUARIO_X_CONVENIO
							UsuarioXConvenio usuarioXConvenio = new UsuarioXConvenio();
							Personas person = new Personas();
							Contratos contrato = new Contratos();
							TiposCargue tipoCargue = new TiposCargue();
							person.setCodigo(codigoPersona);
							contrato.setCodigo(forma.getContrato());
							tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueMasivo);
							usuarioXConvenio.setPersonas(person);
							usuarioXConvenio.setContratos(contrato);
							usuarioXConvenio.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
							usuarioXConvenio.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
							usuarioXConvenio.setTiposCargue(tipoCargue);
							usuarioXConvenio.setFechaCargue(new Date());
							usuarioXConvenio.setUsuario(usuario.getLoginUsuario());
							usuarioXConvenio.setActivo(ConstantesBD.acronimoSi);
							if(!personaArchivo.getClasificacionSE().isEmpty()){
								EstratosSociales estratoSocial = new EstratosSociales();
								estratoSocial.setCodigo(Integer.parseInt(personaArchivo.getClasificacionSE()));
								usuarioXConvenio.setEstratosSociales(estratoSocial);
							}
							if(!personaArchivo.getTipoAfiliado().isEmpty()){
								TiposAfiliado tipoAfiliado = new TiposAfiliado();
								tipoAfiliado.setAcronimo(personaArchivo.getTipoAfiliado().charAt(0));
								usuarioXConvenio.setTiposAfiliado(tipoAfiliado);
							}
							if(!personaArchivo.getExcepcionMonto().isEmpty()){
								NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
								naturalezaPacientes.setCodigo(Integer.parseInt(personaArchivo.getExcepcionMonto()));
								usuarioXConvenio.setNaturalezaPacientes(naturalezaPacientes);
							}
							if(!personaArchivo.getCentroAtencion().isEmpty()){
								CentroAtencion centroAtencion = new CentroAtencion();
								centroAtencion.setConsecutivo(Integer.parseInt(personaArchivo.getCentroAtencion()));
								usuarioXConvenio.setCentroAtencion(centroAtencion);
							}
							if(!personaArchivo.getTipoIdEmpleador().isEmpty()){
								usuarioXConvenio.setTipoIdEmpleador(personaArchivo.getTipoIdEmpleador());
								usuarioXConvenio.setNumeroIdEmpleador(personaArchivo.getNumIdEmpleador());
								usuarioXConvenio.setRazonSociEmpleador(personaArchivo.getRazonSociEmpleador());
							}
							if(!personaArchivo.getTipoIdCotizante().isEmpty()){
								usuarioXConvenio.setTipoIdCotizante(personaArchivo.getTipoIdCotizante());
								usuarioXConvenio.setNumeroIdCotizante(personaArchivo.getNumIdCotizante());
								usuarioXConvenio.setNombresCotizante(personaArchivo.getNombresCotizante());
								usuarioXConvenio.setApellidosCotizante(personaArchivo.getApellidosCotizante());
							}
							if(!personaArchivo.getParentesco().isEmpty()){
								TiposParentesco parentesco = new TiposParentesco();
								parentesco.setCodigo(Integer.valueOf(personaArchivo.getParentesco()));
								usuarioXConvenio.setTiposParentesco(parentesco);
							}
							if(!personaArchivo.getNumeroFicha().isEmpty()){
								usuarioXConvenio.setNumeroFicha(personaArchivo.getNumeroFicha());
							}
							try{
								usuarioXConvenioMundo.attachDirty(usuarioXConvenio);
								UtilidadTransaccion.getTransaccion().flush();
							}
							catch(Exception e){
								logger.error(e.getMessage());
							}	
						}
						else{
							if(codigoUsuarioCapitado!=ConstantesBD.codigoNuncaValidoLong){
								//Se guarda en la tabla CONV_USUARIOS_CAPITADOS
								ConvUsuariosCapitados convUsuariosCapitados = new ConvUsuariosCapitados();
								UsuariosCapitados usuarioCapitado = new UsuariosCapitados();
								Contratos contrato = new Contratos();
								TiposCargue tipoCargue = new TiposCargue();
								usuarioCapitado.setCodigo(codigoUsuarioCapitado);
								contrato.setCodigo(forma.getContrato());
								tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueMasivo);
								convUsuariosCapitados.setUsuariosCapitados(usuarioCapitado);
								convUsuariosCapitados.setContratos(contrato);
								convUsuariosCapitados.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
								convUsuariosCapitados.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
								convUsuariosCapitados.setTiposCargue(tipoCargue);
								convUsuariosCapitados.setFechaCargue(new Date());
								convUsuariosCapitados.setUsuario(usuario.getLoginUsuario());
								convUsuariosCapitados.setActivo(ConstantesBD.acronimoSi);
								if(!personaArchivo.getClasificacionSE().isEmpty()){
									EstratosSociales estratoSocial = new EstratosSociales();
									estratoSocial.setCodigo(Integer.parseInt(personaArchivo.getClasificacionSE()));
									convUsuariosCapitados.setEstratosSociales(estratoSocial);
								}
								if(!personaArchivo.getTipoAfiliado().isEmpty()){
									TiposAfiliado tipoAfiliado = new TiposAfiliado();
									tipoAfiliado.setAcronimo(personaArchivo.getTipoAfiliado().charAt(0));
									convUsuariosCapitados.setTiposAfiliado(tipoAfiliado);
								}
								if(!personaArchivo.getExcepcionMonto().isEmpty()){
									NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
									naturalezaPacientes.setCodigo(Integer.valueOf(personaArchivo.getExcepcionMonto()));
									convUsuariosCapitados.setNaturalezaPacientes(naturalezaPacientes);
								}
								if(!personaArchivo.getCentroAtencion().isEmpty()){
									CentroAtencion centroAtencion = new CentroAtencion();
									centroAtencion.setConsecutivo(Integer.valueOf(personaArchivo.getCentroAtencion()));
									convUsuariosCapitados.setCentroAtencion(centroAtencion);
								}
								if(!personaArchivo.getTipoIdEmpleador().isEmpty()){
									convUsuariosCapitados.setTipoIdEmpleador(personaArchivo.getTipoIdEmpleador());
									convUsuariosCapitados.setNumeroIdEmpleador(personaArchivo.getNumIdEmpleador());
									convUsuariosCapitados.setRazonSociEmpleador(personaArchivo.getRazonSociEmpleador());
								}
								if(!personaArchivo.getTipoIdCotizante().isEmpty()){
									convUsuariosCapitados.setTipoIdCotizante(personaArchivo.getTipoIdCotizante());
									convUsuariosCapitados.setNumeroIdCotizante(personaArchivo.getNumIdCotizante());
									convUsuariosCapitados.setNombresCotizante(personaArchivo.getNombresCotizante());
									convUsuariosCapitados.setApellidosCotizante(personaArchivo.getApellidosCotizante());
								}
								if(!personaArchivo.getParentesco().isEmpty()){
									TiposParentesco parentesco = new TiposParentesco();
									parentesco.setCodigo(Integer.valueOf(personaArchivo.getParentesco()));
									convUsuariosCapitados.setTiposParentesco(parentesco);
								}
								convUsuariosCapitados.setNumeroFicha(forma.getNumeroFicha());
								try{
									convUsuariosCapitadosMundo.attachDirty(convUsuariosCapitados);
									UtilidadTransaccion.getTransaccion().flush();
								}
								catch(Exception e){
									logger.error(e.getMessage());
								}
							}
							else{
								//Se guarda en las tablas USUARIOS_CAPITADOS Y CONV_USUARIOS_CAPITADOS
								UsuariosCapitados usuarioCapitado = new UsuariosCapitados();
								Sexo sexo= new Sexo();
								usuarioCapitado.setNumeroIdentificacion(personaArchivo.getNumeroIdentificacion());
								usuarioCapitado.setTipoIdentificacion(personaArchivo.getTipoIdentificacion());
								usuarioCapitado.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaStringDate(personaArchivo.getFechaNacimiento()));
								sexo.setCodigo(Integer.valueOf(personaArchivo.getSexo()));
								usuarioCapitado.setSexo(sexo);
								usuarioCapitado.setPrimerNombre(personaArchivo.getPrimerNombre());
								usuarioCapitado.setSegundoNombre(personaArchivo.getSegundoNombre());
								usuarioCapitado.setPrimerApellido(personaArchivo.getPrimerApellido());
								usuarioCapitado.setSegundoApellido(personaArchivo.getSegundoApellido());
								usuarioCapitado.setDireccion(personaArchivo.getDireccion());
								usuarioCapitado.setTelefono(personaArchivo.getTelefono());
								if(!personaArchivo.getPais().isEmpty() && !personaArchivo.getDepartamento().isEmpty() 
										&& !personaArchivo.getMunicipio().isEmpty() ){
									CiudadesHome ciudadH= new CiudadesHome();
									CiudadesId idCiudad = new CiudadesId();
									idCiudad.setCodigoPais(personaArchivo.getPais());
									idCiudad.setCodigoDepartamento(personaArchivo.getDepartamento());
									idCiudad.setCodigoCiudad(personaArchivo.getMunicipio());
									Ciudades ciudad = new Ciudades();
									ciudad = ciudadH.findById(idCiudad);
									usuarioCapitado.setCiudades(ciudad);
									if(!personaArchivo.getLocalidad().isEmpty()){
										usuarioCapitado.setLocalidad(personaArchivo.getLocalidad());
									}
								}
								if(!personaArchivo.getBarrio().isEmpty()){
									Barrios barrio=barriosMundo.findByCodigoBarrio(personaArchivo.getBarrio(),
											personaArchivo.getMunicipio(), personaArchivo.getDepartamento(), 
											personaArchivo.getPais());
									if(barrio != null){
										usuarioCapitado.setBarrios(barrio);
									}
								}
								try{
									usuarioCapitado=usuariosCapitadosMundo.merge(usuarioCapitado);
									UtilidadTransaccion.getTransaccion().flush();
								}
								catch(Exception e){
									logger.error(e.getMessage());
									continue;
								}
								ConvUsuariosCapitados convUsuariosCapitados = new ConvUsuariosCapitados();
								Contratos contrato = new Contratos();
								TiposCargue tipoCargue = new TiposCargue();
								contrato.setCodigo(forma.getContrato());
								tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueMasivo);
								convUsuariosCapitados.setUsuariosCapitados(usuarioCapitado);
								convUsuariosCapitados.setContratos(contrato);
								convUsuariosCapitados.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
								convUsuariosCapitados.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
								convUsuariosCapitados.setTiposCargue(tipoCargue);
								convUsuariosCapitados.setFechaCargue(new Date());
								convUsuariosCapitados.setUsuario(usuario.getLoginUsuario());
								convUsuariosCapitados.setActivo(ConstantesBD.acronimoSi);
								if(!personaArchivo.getClasificacionSE().isEmpty()){
									EstratosSociales estratoSocial = new EstratosSociales();
									estratoSocial.setCodigo(Integer.parseInt(personaArchivo.getClasificacionSE()));
									convUsuariosCapitados.setEstratosSociales(estratoSocial);
								}
								if(!personaArchivo.getTipoAfiliado().isEmpty()){
									TiposAfiliado tipoAfiliado = new TiposAfiliado();
									tipoAfiliado.setAcronimo(personaArchivo.getTipoAfiliado().charAt(0));
									convUsuariosCapitados.setTiposAfiliado(tipoAfiliado);
								}
								if(!personaArchivo.getExcepcionMonto().isEmpty()){
									NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
									naturalezaPacientes.setCodigo(Integer.valueOf(personaArchivo.getExcepcionMonto()));
									convUsuariosCapitados.setNaturalezaPacientes(naturalezaPacientes);
								}
								if(!personaArchivo.getCentroAtencion().isEmpty()){
									CentroAtencion centroAtencion = new CentroAtencion();
									centroAtencion.setConsecutivo(Integer.valueOf(personaArchivo.getCentroAtencion()));
									convUsuariosCapitados.setCentroAtencion(centroAtencion);
								}
								if(!personaArchivo.getTipoIdEmpleador().isEmpty()){
									convUsuariosCapitados.setTipoIdEmpleador(personaArchivo.getTipoIdEmpleador());
									convUsuariosCapitados.setNumeroIdEmpleador(personaArchivo.getNumIdEmpleador());
									convUsuariosCapitados.setRazonSociEmpleador(personaArchivo.getRazonSociEmpleador());
								}
								if(!personaArchivo.getTipoIdCotizante().isEmpty()){
									convUsuariosCapitados.setTipoIdCotizante(personaArchivo.getTipoIdCotizante());
									convUsuariosCapitados.setNumeroIdCotizante(personaArchivo.getNumIdCotizante());
									convUsuariosCapitados.setNombresCotizante(personaArchivo.getNombresCotizante());
									convUsuariosCapitados.setApellidosCotizante(personaArchivo.getApellidosCotizante());
								}
								if(!personaArchivo.getParentesco().isEmpty()){
									TiposParentesco parentesco = new TiposParentesco();
									parentesco.setCodigo(Integer.valueOf(personaArchivo.getParentesco()));
									convUsuariosCapitados.setTiposParentesco(parentesco);
								}
								if(!personaArchivo.getNumeroFicha().isEmpty()){
									convUsuariosCapitados.setNumeroFicha(personaArchivo.getNumeroFicha());
								}
								try{
									convUsuariosCapitadosMundo.attachDirty(convUsuariosCapitados);
									UtilidadTransaccion.getTransaccion().flush();
								}
								catch(Exception e){
									logger.error(e.getMessage());
								}
							}
						}
					}
				}
				
			}	
			
		}
		catch (Exception e){
			logger.error("Error Guardando Personas Archivo sin Inconsistencias",e);
			return false;
		}
		return true;
	}
	
	/****************************************************************************************************************************/
	/**
	 * Metodo que valida la estructura de los campos del archivo plano
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 * @author 
	 */
	private ActionForward accionVerificarMasivo(Connection con, ActionMapping mapping, ActionErrors errores, SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request, String fechaActual)
	{
		try
		{   
			listaInconsistenciaCampos=new ArrayList<DtoInconsistenciasArchivoPlano>();
			listaInconsistenciaPersonaBD=new ArrayList<DtoInconsistenciasArchivoPlano>();
			listaPersonaArchivoPlano=new ArrayList<DtoPersonas>();
			numeroRegistrosLeidos=0;
			
			ArrayList<DtoVerificacionArchivo> inconsistenciasArchivo = new ArrayList<DtoVerificacionArchivo>();
			ArrayList<String> registrosArchivo = new ArrayList<String>();
			BufferedReader archivo = new BufferedReader(new java.io.InputStreamReader(forma.getArchivo().getInputStream(), "UTF-8"));
			int numRegistro=1;
			String lineaArchivo="";
			while((lineaArchivo=archivo.readLine())!=null) // se recorren cada una de las lineas del archivo
			{
				lineaArchivo +=" ";// se agrega un espacio al final para hacer el split en caso de que la linea este vacia
				lineaArchivo = lineaArchivo.replaceAll("\"",""); // se quitan las comillas a los nombres de los usuarios
				registrosArchivo.add(lineaArchivo);
				String[] campos=lineaArchivo.split("\\|"); // se separan los campos con el separador , de la linea indicada
				if(campos.length==1){
					DtoVerificacionArchivo inconsistencia = new DtoVerificacionArchivo(
								String.valueOf(numRegistro), "", "No");
					inconsistenciasArchivo.add(inconsistencia);
				}
				else{
					if(campos.length != 30){
						DtoVerificacionArchivo inconsistencia = new DtoVerificacionArchivo(
								String.valueOf(numRegistro), String.valueOf(campos.length), "");
						inconsistenciasArchivo.add(inconsistencia);
					}
				}
				numRegistro++;
				lineaArchivo="";
			}
			HibernateUtil.beginTransaction();
			if(!inconsistenciasArchivo.isEmpty()){
				forma.setNombreArchivo(forma.getArchivo().getFileName());
				forma.setInconsistenciasArchivo(inconsistenciasArchivo);
				guardarLogInconsistenciaGeneral(forma, usuario, request, ConstantesBD.codigoTipoCargueMasivo);
				HibernateUtil.endTransaction();
				return mapping.findForward("erroresArchivo");
			}
						
			if(registrosArchivo.isEmpty()){
				errores.add("Archivo Sin Datos", new ActionMessage("error.errorEnBlanco", "El archivo no tiene registros. PROCESO CANCELADO."));
		    	saveErrors(request, errores);
		    	HibernateUtil.endTransaction();
		    	return hacerMapping(con, mapping, "principal");
			}
			//Se llenan todas la listas necesarias para las validaciones con los
			//datos de la base de datos de acuerdo al convenio seleccionado
			
			INaturalezaPacienteMundo naturalezaPacienteMundo = ManejoPacienteFabricaMundo.crearNaturalezaPacienteMundo();
			forma.setListadoContratos(SubirPaciente.consultarTipos(con, 2, usuario.getCodigoInstitucionInt(), forma.getConvenio()));
			forma.setListadoClasificacionSE(SubirPaciente.consultarTipos(con, 5, usuario.getCodigoInstitucionInt(), forma.getConvenio()));
			forma.setListadoNaturalezaPacientes(naturalezaPacienteMundo.listarNaturalezaPacientesPorConvenio(forma.getConvenio()));
			
			//Se obtienen los catalogos de todas las ciudades, localidades y barrio parametrizadas en el sistema
			ILocalizacionMundo localizacionMundo = AdministracionFabricaMundo.crearloILocalizacionMundo();
			List<DtoCiudad> catalogoCiudades = localizacionMundo.listarAllCiudades();
			List<DtoBarrio> catalogoBarrios = localizacionMundo.listarAllBarrios();
			List<DtoLocalidad> catalogoLocalidades = localizacionMundo.listarAllLocalidades();
			
			// SE RECORREN CADA UNA DE LAS LINEAS DEL ARCHIVO YA HABIENDO GARANTIZADO EL NUMERO DE COLUMNAS Y EL SEPARADOR (|)
			numRegistro=1;
			for(String campos_linea:registrosArchivo)
			{			
				String[] campos=campos_linea.split("\\|"); // se separan los campos con el separador , de la linea indicada*/
				boolean datosBasicosValido=true;							
				//SE VALIDA EL CONVENIO
				if(campos[columnaConvenio].trim().isEmpty()){
					dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoEsRequerido,
												messageResource.getMessage(nombreColumnaConvenio), numRegistro);
					listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
					datosBasicosValido=false;
				}
				else{
					if(!campos[columnaConvenio].equals(String.valueOf(forma.getConvenio()))){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoConvenioNoCorresponde,
								messageResource.getMessage(nombreColumnaConvenio), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				//SE VALIDA EL NUMERO DE CONTRATO
				if(campos[columnaNumeroContrato].trim().isEmpty()){
					dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoEsRequerido,
												messageResource.getMessage(nombreColumnaNumeroContrato), numRegistro);
					listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
					datosBasicosValido=false;
				}
				else{
					if(!verificarCollection(forma.getListadoContratos(), "numero_contrato", campos[columnaNumeroContrato])){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoContratoNoCorresponde,
								messageResource.getMessage(nombreColumnaNumeroContrato), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				//SE VALIDA EL TIPO DE IDENTIFICACION
				if(campos[columnaTipoIdentificacion].trim().isEmpty()){
					dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoEsRequerido,
												messageResource.getMessage(nombreColumnaTipoIdentificacion), numRegistro);
					listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
					datosBasicosValido=false;
				}
				else{
					if(campos[columnaTipoIdentificacion].length() != 2
							|| !verificarCollection(forma.getListadoTiposId(), "acronimo",campos[columnaTipoIdentificacion])){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
								messageResource.getMessage(nombreColumnaTipoIdentificacion), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				
				//SE VALIDA NUMERO DE IDENTIFICACION
				if(campos[columnaNumeroIdentificacion].trim().isEmpty()){
					dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoEsRequerido,
												messageResource.getMessage(nombreColumnaNumeroIdentificacion), numRegistro);
					listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
					datosBasicosValido=false;
				}
				else{
					if(campos[columnaNumeroIdentificacion].length()>20){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
								messageResource.getMessage(nombreColumnaNumeroIdentificacion), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				
				//SE VALIDA PRIMER APELLIDO
				if(campos[columnaPrimerApellido].trim().isEmpty()){
					dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoEsRequerido,
												messageResource.getMessage(nombreColumnaPrimerApellido), numRegistro);
					listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
					datosBasicosValido=false;
				}
				else{
					if(campos[columnaPrimerApellido].length()>30){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
								messageResource.getMessage(nombreColumnaPrimerApellido), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				
				//SE VALIDA SEGUNDO APELLIDO
				if(!campos[columnaSegundoApellido].trim().isEmpty()){
					if(campos[columnaSegundoApellido].length()>30){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
													messageResource.getMessage(nombreColumnaSegundoApellido), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				
				//SE VALIDA PRIMER NOMBRE
				if(campos[columnaPrimerNombre].trim().isEmpty()){
					dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoEsRequerido,
												messageResource.getMessage(nombreColumnaPrimerNombre), numRegistro);
					listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
					datosBasicosValido=false;
				}
				else{
					if(campos[columnaPrimerNombre].length()>30){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
								messageResource.getMessage(nombreColumnaPrimerNombre), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}
				
				//SE VALIDA SEGUNDO NOMBRE
				if(!campos[columnaSegundoNombre].trim().isEmpty()){
					if(campos[columnaSegundoNombre].length()>30){
						dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
													messageResource.getMessage(nombreColumnaSegundoNombre), numRegistro);
						listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
						datosBasicosValido=false;
					}
				}				
				if(datosBasicosValido){
					//SE VALIDA FECHA NACIMIENTO
					boolean camposValido=true;
					if(!campos[columnaFechaNacimiento].trim().isEmpty()){
						if(!UtilidadFecha.esFechaValidaSegunAp(campos[columnaFechaNacimiento]) ||
								!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(campos[columnaFechaNacimiento],fechaActual)){					
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoFormatoFechaNoValido,
									messageResource.getMessage(nombreColumnaFechaNacimiento), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
							
						}
					}
					//SE VALIDA SEXO
					if(!campos[columnaSexo].trim().isEmpty()){
						if(!verificarCollection(forma.getListadoSexos(), "codigo", campos[columnaSexo])){					
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoSexoInvalido,
									messageResource.getMessage(nombreColumnaSexo), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA DIRECCION
					if(!campos[columnaDireccion].trim().isEmpty()){
						if(campos[columnaDireccion].length()>256){					
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
									messageResource.getMessage(nombreColumnaDireccion), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					// SE VERIFICA EL PAIS RESIDENCIA
					if(!campos[columnaPaisResidencia].trim().isEmpty()){
						if(verificarCiudadDepartamentoPais(catalogoCiudades, campos[columnaPaisResidencia], null, null)){
						    //SE VERIFICA EL DEPARTAMENTO RESIDENCIA
							if(!campos[columnaDepartamentoResidencia].trim().isEmpty()){	 
								if(verificarCiudadDepartamentoPais(catalogoCiudades, campos[columnaPaisResidencia], campos[columnaDepartamentoResidencia], null)){
									//SE VERIFICA EL MUNICIPIO RESIDENCIA
									if(!UtilidadTexto.isEmpty(campos[columnaMunicipioResidencia])){	
										if(verificarCiudadDepartamentoPais(catalogoCiudades, campos[columnaPaisResidencia], campos[columnaDepartamentoResidencia], campos[columnaMunicipioResidencia])){
											//SE VERIFICA LA LOCALIDAD RESIDENCIA
											if(!campos[columnaLocalidadResidencia].trim().isEmpty()){
												if(!verificarLocalidad(catalogoLocalidades, campos[columnaPaisResidencia], campos[columnaDepartamentoResidencia], campos[columnaMunicipioResidencia], campos[columnaLocalidadResidencia])){
													dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
															messageResource.getMessage(nombreColumnaLocalidadResidencia), numRegistro);
													listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
													camposValido=false;
												}
											}
											//SE VERIFICA EL BARRIO RESIDENCIA
											if(!campos[columnaBarrioResidencia].trim().isEmpty()){
												if(!verificarBarrio(catalogoBarrios, campos[columnaPaisResidencia], campos[columnaDepartamentoResidencia], campos[columnaMunicipioResidencia], campos[columnaBarrioResidencia])){
													dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
															messageResource.getMessage(nombreColumnaBarrioResidencia), numRegistro);
													listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
													camposValido=false;
												}
											}
										}
										else{
											dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
													messageResource.getMessage(nombreColumnaMunicipioResidencia), numRegistro);
											listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
											camposValido=false;
										}
									}	
								}else{								
									dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
											messageResource.getMessage(nombreColumnaDepartamentoResidencia), numRegistro);
									listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
									camposValido=false;									
								}
							}
						}else{
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
									messageResource.getMessage(nombreColumnaPaisResidencia), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA TELEFONO
					if(!campos[columnaTelefono].trim().isEmpty()){
						if(campos[columnaTelefono].length()>512){					
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
									messageResource.getMessage(nombreColumnaTelefono), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA CLASIFICACION SOCIO-ECONOMICA
					if(!campos[columnaClasificacionSocioEconomica].trim().isEmpty()){
						if(!verificarCollection(forma.getListadoClasificacionSE(), "codigo", campos[columnaClasificacionSocioEconomica])){
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoClasificacionSENoCorresponde,
									messageResource.getMessage(nombreColumnaClasificacionSocioEconomica), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA TIPO AFILIADO
					if(!campos[columnaTipoAfiliado].trim().isEmpty()){
						if(!verificarCollection(forma.getListadoTiposAfiliado(), "codigo", campos[columnaTipoAfiliado])){
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
									messageResource.getMessage(nombreColumnaTipoAfiliado), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA EXCEPCION MONTO
					if(!campos[columnaExcepcionMonto].trim().isEmpty()){
						if(UtilidadTexto.isNumber(campos[columnaExcepcionMonto])){
							if(!verificarNaturalezaPaciente(forma.getListadoNaturalezaPacientes(), Integer.valueOf(campos[columnaExcepcionMonto]).intValue())){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoExcepcionMontoNoCorresponde,
										messageResource.getMessage(nombreColumnaExcepcionMonto), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
						}
						else{
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNumericoNoValido,
									messageResource.getMessage(nombreColumnaExcepcionMonto), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA NUMERO FICHA
					if(!campos[columnaNumeroFicha].trim().isEmpty()){
						if(campos[columnaNumeroFicha].length()>15){
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
									messageResource.getMessage(nombreColumnaNumeroFicha), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA CENTRO ATENCION
					if(!campos[columnaCentroAtencioAsignado].trim().isEmpty()){
						if(UtilidadTexto.isNumber(campos[columnaCentroAtencioAsignado])){
							if(!verificarCentroAtencion(forma.getCentrosAtencion(), Integer.valueOf(campos[columnaCentroAtencioAsignado]).intValue())){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
										messageResource.getMessage(nombreColumnaCentroAtencioAsignado), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
						}
						else{
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNumericoNoValido,
									messageResource.getMessage(nombreColumnaCentroAtencioAsignado), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA Tipo ID del empleador, Número identificación del empleador, Razón social del empleador
					String tipoIdEmpleador=campos[columnaTipoIdEmpleador];
					String numeroIdEmpleador=campos[columnaNumeroIdentificacionEmpleador];
					String razonSocialEmpleador=campos[columnaRazonSocialEmpleador];
					if(!tipoIdEmpleador.trim().isEmpty() || !numeroIdEmpleador.trim().isEmpty()
							|| !razonSocialEmpleador.trim().isEmpty()){
						if(!tipoIdEmpleador.trim().isEmpty() && !numeroIdEmpleador.trim().isEmpty()
								&& !razonSocialEmpleador.trim().isEmpty()){
							//SE VALIDA EL TIPO DE IDENTIFICACION EMPLEADOR
							if(!verificarCollection(forma.getListadoTiposId(), "acronimo",tipoIdEmpleador)){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
										messageResource.getMessage(nombreColumnaTipoIdEmpleador), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
							
							//SE VALIDA NUMERO DE IDENTIFICACION EMPLEADOR
							if(numeroIdEmpleador.length()>20){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
										messageResource.getMessage(nombreColumnaNumeroIdentificacionEmpleador), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
														
							//SE VALIDA RAZON SOCIAL EMPLEADOR
							if(razonSocialEmpleador.length()>40){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
										messageResource.getMessage(nombreColumnaRazonSocialEmpleador), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
						}
						else{
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoTodosDatosObligatorios,
									messageResource.getMessage(nombreColumnaTipoIdEmpleador)+", "+
									messageResource.getMessage(nombreColumnaNumeroIdentificacionEmpleador)+", "+
									messageResource.getMessage(nombreColumnaRazonSocialEmpleador), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}	
					//SE VALIDA Tipo ID del cotizante, Número identificación del cotizante, Nombres del Cotizante, Apellidos Cotizante
					String tipoIdCotizante=campos[columnaTipoIdCotizante];
					String numeroIdCotizante=campos[columnaNumeroIdentificacionCotizante];
					String nombresCotizante=campos[columnaNombresCotizante];
					String apellidosCotizante=campos[columnaApellidosCotizante];
					if(!tipoIdCotizante.trim().isEmpty() || !numeroIdCotizante.trim().isEmpty()
							|| !nombresCotizante.trim().isEmpty() || !apellidosCotizante.trim().isEmpty()){
						if(!tipoIdCotizante.trim().isEmpty() && !numeroIdCotizante.trim().isEmpty()
								&& !nombresCotizante.trim().isEmpty() && !apellidosCotizante.trim().isEmpty()){
							//SE VALIDA EL TIPO DE IDENTIFICACION COTIZANTE
							if(!verificarCollection(forma.getListadoTiposId(), "acronimo",tipoIdCotizante)){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
										messageResource.getMessage(nombreColumnaTipoIdCotizante), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
							
							//SE VALIDA NUMERO DE IDENTIFICACION COTIZANTE
							if(numeroIdCotizante.length()>20){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
										messageResource.getMessage(nombreColumnaNumeroIdentificacionCotizante), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
														
							//SE VALIDA NOMBRES COTIZANTE
							if(nombresCotizante.length()>40){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
										messageResource.getMessage(nombreColumnaNombresCotizante), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
							//SE VALIDA APELLIDOS COTIZANTE
							if(apellidosCotizante.length()>40){
								dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida,
										messageResource.getMessage(nombreColumnaApellidosCotizante), numRegistro);
								listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
								camposValido=false;
							}
						}
						else{
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoTodosDatosObligatorios,
									messageResource.getMessage(nombreColumnaTipoIdCotizante)+", "+
									messageResource.getMessage(nombreColumnaNumeroIdentificacionCotizante)+", "+
									messageResource.getMessage(nombreColumnaNombresCotizante)+", "+
									messageResource.getMessage(nombreColumnaApellidosCotizante), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					//SE VALIDA PARENTESCO
					if(!campos[columnaParentesco].trim().isEmpty()){
						if(!verificarCollection(forma.getListadoTiposParentezco(), "codigo", campos[columnaParentesco])){
							dtoInconsistenciaCampos=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCampoNoValido,
									messageResource.getMessage(nombreColumnaParentesco), numRegistro);
							listaInconsistenciaCampos.add(dtoInconsistenciaCampos);
							camposValido=false;
						}
					}
					
					if(camposValido){
						DtoPersonas dtoPersona=new DtoPersonas();
						dtoPersona.setTipoIdentificacion(campos[columnaTipoIdentificacion].trim());
						dtoPersona.setNumeroIdentificacion(campos[columnaNumeroIdentificacion].trim());
						dtoPersona.setPrimerNombre(campos[columnaPrimerNombre].trim());
						dtoPersona.setSegundoNombre(campos[columnaSegundoNombre].trim());
						dtoPersona.setPrimerApellido(campos[columnaPrimerApellido].trim());
						dtoPersona.setSegundoApellido(campos[columnaSegundoApellido].trim());
						dtoPersona.setSoloNombres(dtoPersona.getPrimerNombre()+" "+dtoPersona.getSegundoNombre());
						dtoPersona.setSoloApellidos(dtoPersona.getPrimerApellido()+" "+dtoPersona.getSegundoApellido());
						dtoPersona.setFechaNacimiento(campos[columnaFechaNacimiento].trim());
						dtoPersona.setSexo(campos[columnaSexo].trim());
						dtoPersona.setDireccion(campos[columnaDireccion].trim());
						dtoPersona.setPais(campos[columnaPaisResidencia].trim());
						dtoPersona.setDepartamento(campos[columnaDepartamentoResidencia].trim());
						dtoPersona.setMunicipio(campos[columnaMunicipioResidencia].trim());
						dtoPersona.setLocalidad(campos[columnaLocalidadResidencia].trim());
						dtoPersona.setBarrio(campos[columnaBarrioResidencia].trim());
						dtoPersona.setTelefono(campos[columnaTelefono].trim());
						dtoPersona.setClasificacionSE(campos[columnaClasificacionSocioEconomica].trim());
						dtoPersona.setTipoAfiliado(campos[columnaTipoAfiliado].trim());
						dtoPersona.setExcepcionMonto(campos[columnaExcepcionMonto].trim());
						dtoPersona.setNumeroFicha(campos[columnaNumeroFicha].trim());
						dtoPersona.setCentroAtencion(campos[columnaCentroAtencioAsignado].trim());
						dtoPersona.setTipoIdEmpleador(campos[columnaTipoIdEmpleador].trim());
						dtoPersona.setNumIdEmpleador(campos[columnaNumeroIdentificacionEmpleador].trim());
						dtoPersona.setRazonSociEmpleador(campos[columnaRazonSocialEmpleador].trim());
						dtoPersona.setTipoIdCotizante(campos[columnaTipoIdCotizante].trim());
						dtoPersona.setNumIdCotizante(campos[columnaNumeroIdentificacionCotizante].trim());
						dtoPersona.setNombresCotizante(campos[columnaNombresCotizante].trim());
						dtoPersona.setApellidosCotizante(campos[columnaApellidosCotizante].trim());
						dtoPersona.setParentesco(campos[columnaParentesco].trim());//Se le hace trim al ultimo registro dado qeu se agrego un espacio al fina de cada linea
						dtoPersona.setNumeroRegistro(numRegistro);
						boolean registroValido=true;
						DtoPersonas filtroPersona = new DtoPersonas();
						ArrayList<DtoPersonas> personasInconsistentes = new ArrayList<DtoPersonas>();
						ArrayList<DtoPersonas> personasTipoIdentidad = new ArrayList<DtoPersonas>();
						ArrayList<DtoPersonas> personasNombres = new ArrayList<DtoPersonas>();
						filtroPersona.setTipoIdentificacion(dtoPersona.getTipoIdentificacion());
						filtroPersona.setNumeroIdentificacion(dtoPersona.getNumeroIdentificacion());
						personasTipoIdentidad=validarInconsistenciaTipoPersona(dtoPersona, filtroPersona);
						if(personasTipoIdentidad != null){
							personasInconsistentes.addAll(personasTipoIdentidad);
						}
						filtroPersona = new DtoPersonas();
						filtroPersona.setTipoIdentificacion("");
						filtroPersona.setNumeroIdentificacion("");
						filtroPersona.setPrimerNombre(dtoPersona.getPrimerNombre());
						filtroPersona.setSegundoNombre(dtoPersona.getSegundoNombre());
						filtroPersona.setPrimerApellido(dtoPersona.getPrimerApellido());
						filtroPersona.setSegundoApellido(dtoPersona.getSegundoApellido());
						personasNombres=validarInconsistenciaTipoPersona(dtoPersona, filtroPersona);
						if(personasNombres != null){
							personasInconsistentes.addAll(personasNombres);
						}
						if(!personasInconsistentes.isEmpty()){
							dtoInconsistenciaPersonaBD=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoTipoNumIDNoCorresponde,
																dtoPersona, personasInconsistentes, numRegistro);
							listaInconsistenciaPersonaBD.add(dtoInconsistenciaPersonaBD);
							registroValido=false;
						}
						if(registroValido){
							listaPersonaArchivoPlano.add(dtoPersona);
						}
					}
				}
				numRegistro++;
			}
			numeroRegistrosLeidos=numRegistro-1;
			boolean exito=true;
			exito=accionGuardarMasivo(forma, usuario, request);
			if(!exito){
				errores.add("No se pudo Guardar", new ActionMessage("error.errorEnBlanco", "Error guardando Registros. PROCESO CANCELADO."));
		    	saveErrors(request, errores);
		    	HibernateUtil.endTransaction();
		    	return mapping.findForward("principal");
			}
			if(listaInconsistenciaPersonaBD.isEmpty()){
				guardarLogfinal(forma, usuario, request, ConstantesBD.codigoTipoCargueMasivo);
				HibernateUtil.endTransaction();
				return mapping.findForward("resumen");
			}
			else{
				forma.setListaInconsistenciaPersonaBD(listaInconsistenciaPersonaBD);
				HibernateUtil.endTransaction();
				return mapping.findForward("principal");
			}
		}catch(Exception e){
			logger.error("Error obteniendo el archivo ",e);
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.subirPacientes.errorArchivo", "error.subirPacientes.errorArchivo", true);
		}
		finally{
			UtilidadBD.finalizarTransaccion(con);
    	}
	}	
	


	/**
	 * Método que valida las inconsistencias de tipo Persona
	 * 
	 * @param personaArchivo
	 * @param personaFiltro
	 * @return
	 */
	private ArrayList<DtoPersonas> validarInconsistenciaTipoPersona(DtoPersonas personaArchivo, DtoPersonas personaFiltro){		
		ArrayList<DtoPersonas> listaPersonasInconsistenciaBD=new ArrayList<DtoPersonas>();
		//Busca las personas de acuerdo a los filtros seleccionados
		IPersonas personasMundo=AdministracionFabricaMundo.crearPersonasMundo();
		List<DtoPersonas> listaPersonasBD=personasMundo.buscarPersonasPorFiltro(personaFiltro);
	
		if(listaPersonasBD != null && !listaPersonasBD.isEmpty()){
			if(!personaFiltro.getTipoIdentificacion().isEmpty() && !personaFiltro.getNumeroIdentificacion().isEmpty()){
				for (DtoPersonas newDtoPersonas : listaPersonasBD) {
					String segNombre="";
					String segApellido="";
					if(newDtoPersonas.getSegundoNombre() != null && !newDtoPersonas.getSegundoNombre().isEmpty()){
						segNombre=newDtoPersonas.getSegundoNombre().trim();
					}
					if(newDtoPersonas.getSegundoApellido() != null && !newDtoPersonas.getSegundoApellido().isEmpty()){
						segApellido=newDtoPersonas.getSegundoApellido().trim();
					}
					String nombres=personaArchivo.getPrimerNombre()+personaArchivo.getSegundoNombre();
					String apellidos=personaArchivo.getPrimerApellido()+personaArchivo.getSegundoApellido();
					String nombresBD=newDtoPersonas.getPrimerNombre()+segNombre;
					String apellidosBD=newDtoPersonas.getPrimerApellido()+segApellido;
					if(!nombres.equalsIgnoreCase(nombresBD) || !apellidos.equalsIgnoreCase(apellidosBD)){
						listaPersonasInconsistenciaBD.add(newDtoPersonas);
					}
				}
				if(listaPersonasInconsistenciaBD.isEmpty()){
					return null;
				}
			}
			else{
				/**
				 * Inc 2561
				 * Se realiza el cambio del DCU 237 varsi&oacute;n de cambio 1.2
				 * Se valida si el paciente tiene existe como persona con los mismos nombres-apellidos y tipo-n&uacute;mero
				 * id no se debe generar inconsistencia y permitir continuar con el flujo actual.
				 * Diana Ruiz
				 */
				
				boolean inconsistencia=true;
				for (DtoPersonas newDtoPersonas : listaPersonasBD) {
					String tipoDoc=personaArchivo.getTipoIdentificacion();
					String numeroDoc=personaArchivo.getNumeroIdentificacion();
					String tipoDocBD=newDtoPersonas.getTipoIdentificacion();
					String numeroDocBD=newDtoPersonas.getNumeroIdentificacion();
					if(tipoDoc.equalsIgnoreCase(tipoDocBD) && numeroDoc.equalsIgnoreCase(numeroDocBD)){
						inconsistencia=false;
						break;
					}
				}
				if(inconsistencia){
					listaPersonasInconsistenciaBD.addAll(listaPersonasBD);
				}
				if(listaPersonasInconsistenciaBD.isEmpty()){
					return null;
				}
			}
		}
		else{
			return null;
		}
		return listaPersonasInconsistenciaBD;
	}

	/**
	 * 
	 * 			
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward subirPacientesArchivoPlano(Connection con, ActionMapping mapping, SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		ActionErrors errores= new ActionErrors();
		contadorLineasSeleccionadas=0;
		boolean validoPersonaCheck=false;
		for (DtoInconsistenciasArchivoPlano inconsistenciaPersona : listaInconsistenciaPersonaBD) 
		{
			if(inconsistenciaPersona.getPersonaArchivo().isSeleccionado())
			{	
				validoPersonaCheck=true;
				contadorLineasSeleccionadas++;
			}										
		}
		if(!validoPersonaCheck){			
	    	errores.add("mensaje.subirPacienteMasivo.noSeleccionados", new ActionMessage("mensaje.subirPacienteMasivo.noSeleccionados"));
	    	saveErrors(request, errores);		    	
	    	return hacerMapping(con, mapping, "principal");
		}
		forma.setCobfirmar(validoPersonaCheck);
		return hacerMapping(con, mapping, "principal");
	
	}
	
	/**
	 *Metodo que obtiene las personas del archivo plano con la que se selcciono de la base de datos y que se guarda en el log.
	 *como la nueva inconsistencia 
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward confirmarGuardarInconsisPersona(ActionMapping mapping, ActionErrors errores, SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{	
		try{
			HibernateUtil.beginTransaction();
			IUsuarioXConvenioMundo usuarioXConvenioMundo=CapitacionFabricaMundo.crearUsuarioXConvenioMundo();
			int i = 0;
			for (DtoInconsistenciasArchivoPlano inconsistenciaPersona : listaInconsistenciaPersonaBD){
				if(inconsistenciaPersona.getPersonaArchivo().isSeleccionado()){	
					i++;
					int codigoPersona=ConstantesBD.codigoNuncaValido;
					for(DtoPersonas personaBD:inconsistenciaPersona.getListaPersonas()){
						if(personaBD.isSeleccionado()){
							codigoPersona=personaBD.getCodigo();
							break;
						}
					}
					if(codigoPersona!=ConstantesBD.codigoNuncaValido){
						//VALIDACION EXISTE CARGUE PREVIO DENTRO DEL RANGO DE FECHAS PARA EL USUARIO
						boolean carguePrevio=false;
						List<DtoContrato> contratosCapitadosPersona=usuarioXConvenioMundo.obtenerCarguesPreviosPorPersonaPorRangoFechas(codigoPersona, 
																			UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()),
																			UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
						if(contratosCapitadosPersona != null && !contratosCapitadosPersona.isEmpty()){
							for(DtoContrato dtoContrato:contratosCapitadosPersona){
								if(forma.getConvenio() == dtoContrato.getConvenio()){
									if(forma.getContrato() == dtoContrato.getCodigo()){
										carguePrevio=true;
										break;
									}
								}
								else{
									carguePrevio=true;
									break;
								}
							}
						}
						if(carguePrevio){
							dtoInconsistenciaCarguePrevio=new DtoInconsistenciasArchivoPlano(ConstantesIntegridadDominio.acronimoCarguePrevioMismoPeriodo,
									inconsistenciaPersona.getPersonaArchivo(), inconsistenciaPersona.getPersonaArchivo().getNumeroRegistro());
							listaInconsistenciaCarguePrevio.add(dtoInconsistenciaCarguePrevio);	
						}
						else{
							//Se guarda en la tabla USUARIO_X_CONVENIO
							UsuarioXConvenio usuarioXConvenio = new UsuarioXConvenio();
							Personas person = new Personas();
							Contratos contrato = new Contratos();
							TiposCargue tipoCargue = new TiposCargue();
							person.setCodigo(codigoPersona);
							contrato.setCodigo(forma.getContrato());
							tipoCargue.setCodigo(ConstantesBD.codigoTipoCargueMasivo);
							usuarioXConvenio.setPersonas(person);
							usuarioXConvenio.setContratos(contrato);
							usuarioXConvenio.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
							usuarioXConvenio.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
							usuarioXConvenio.setTiposCargue(tipoCargue);
							usuarioXConvenio.setFechaCargue(new Date());
							usuarioXConvenio.setUsuario(usuario.getLoginUsuario());
							usuarioXConvenio.setActivo(ConstantesBD.acronimoSi);
							if(!inconsistenciaPersona.getPersonaArchivo().getClasificacionSE().isEmpty()){
								EstratosSociales estratoSocial = new EstratosSociales();
								estratoSocial.setCodigo(Integer.parseInt(inconsistenciaPersona.getPersonaArchivo().getClasificacionSE()));
								usuarioXConvenio.setEstratosSociales(estratoSocial);
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getTipoAfiliado().isEmpty()){
								TiposAfiliado tipoAfiliado = new TiposAfiliado();
								tipoAfiliado.setAcronimo(inconsistenciaPersona.getPersonaArchivo().getTipoAfiliado().charAt(0));
								usuarioXConvenio.setTiposAfiliado(tipoAfiliado);
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getExcepcionMonto().isEmpty()){
								NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
								naturalezaPacientes.setCodigo(Integer.parseInt(inconsistenciaPersona.getPersonaArchivo().getExcepcionMonto()));
								usuarioXConvenio.setNaturalezaPacientes(naturalezaPacientes);
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getCentroAtencion().isEmpty()){
								CentroAtencion centroAtencion = new CentroAtencion();
								centroAtencion.setConsecutivo(Integer.parseInt(inconsistenciaPersona.getPersonaArchivo().getCentroAtencion()));
								usuarioXConvenio.setCentroAtencion(centroAtencion);
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getTipoIdEmpleador().isEmpty()){
								usuarioXConvenio.setTipoIdEmpleador(inconsistenciaPersona.getPersonaArchivo().getTipoIdEmpleador());
								usuarioXConvenio.setNumeroIdEmpleador(inconsistenciaPersona.getPersonaArchivo().getNumIdEmpleador());
								usuarioXConvenio.setRazonSociEmpleador(inconsistenciaPersona.getPersonaArchivo().getRazonSociEmpleador());
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getTipoIdCotizante().isEmpty()){
								usuarioXConvenio.setTipoIdCotizante(inconsistenciaPersona.getPersonaArchivo().getTipoIdCotizante());
								usuarioXConvenio.setNumeroIdCotizante(inconsistenciaPersona.getPersonaArchivo().getNumIdCotizante());
								usuarioXConvenio.setNombresCotizante(inconsistenciaPersona.getPersonaArchivo().getNombresCotizante());
								usuarioXConvenio.setApellidosCotizante(inconsistenciaPersona.getPersonaArchivo().getApellidosCotizante());
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getParentesco().isEmpty()){
								TiposParentesco parentesco = new TiposParentesco();
								parentesco.setCodigo(Integer.valueOf(inconsistenciaPersona.getPersonaArchivo().getParentesco()));
								usuarioXConvenio.setTiposParentesco(parentesco);
							}
							if(!inconsistenciaPersona.getPersonaArchivo().getNumeroFicha().isEmpty()){
								usuarioXConvenio.setNumeroFicha(inconsistenciaPersona.getPersonaArchivo().getNumeroFicha());
							}
							try{
								usuarioXConvenioMundo.attachDirty(usuarioXConvenio);
								UtilidadTransaccion.getTransaccion().flush();
							}
							catch(Exception e){
								logger.error(e.getMessage());
							}	
						}
					}
				}
			}
			numeroInconsistenciasSeleccionadas=i;
			guardarLogFinalPersonas(forma, usuario, request, ConstantesBD.codigoTipoCargueMasivo);
			HibernateUtil.endTransaction();
			return mapping.findForward("resumen");
		}catch(Exception e){
			errores.add("No se pudo Guardar Personas Inconsitencia", new ActionMessage("error.errorEnBlanco", "Error guardando Registros. PROCESO CANCELADO."));
	    	saveErrors(request, errores);		    	
	    	HibernateUtil.abortTransaction();
			logger.error("Error guardando personas en usuarios_capitados",e);
		}
		return mapping.findForward("principal");
	}
		
	/*** 
	 * Metodo que se encarga de guardar el log de los registros excepto los de personas
	 * 
	 *
	 * @param mapping
	 * @param errores
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward confirmarGuardarPersonasConsis(ActionMapping mapping, ActionErrors errores, SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request){
		try{
			numeroInconsistenciasSeleccionadas=0;
			HibernateUtil.beginTransaction();
			guardarLogFinalPersonas(forma, usuario, request, ConstantesBD.codigoTipoCargueMasivo);
			HibernateUtil.endTransaction();
			return mapping.findForward("resumen");						
		}		
		catch (Exception e)
		{
			errores.add("Archivo Sin Datos", new ActionMessage("error.errorEnBlanco", "El archivo no tiene registros. PROCESO CANCELADO."));
	    	saveErrors(request, errores);
	    	HibernateUtil.abortTransaction();
		}
		return mapping.findForward("principal");
	}
	
	
	/******************************************************************************************************************************/
	/***********************************GUARDAR FINAL EL LOG SUBIR PACIENTES*******************************************************/
	/******************************************************************************************************************************/
	
	private void guardarLogfinal(SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request, int idTipoCargue)
	{
		ILogSubirPacientesMundo logSubirPacientesMundo=CapitacionFabricaMundo.crearLogSubirPacientesMundo();
		IInconsistenSubirPacienteMundo inconsistenciaSubirPacientesMundo=CapitacionFabricaMundo.crearInconsistenSubirPacienteMundo();
		IInconsistenciasCamposMundo inconsistenciaCamposMundo=CapitacionFabricaMundo.crearInconsistenciasCamposMundo();
		ICapitadoInconsistenciaMundo capitadoInconsistenciaMundo=CapitacionFabricaMundo.crearCapitadoInconsistenciaMundo();
		IContratoCargueMundo contratoCargueMundo=CapitacionFabricaMundo.crearContratoCargueMundo();
		LogSubirPacientes logSubirPacientes=new LogSubirPacientes(); 
		ContratosHome contratosHome=new ContratosHome();
		Contratos contrato=contratosHome.findById(forma.getContrato());
		ConveniosHome conveniosHome = new ConveniosHome();
		Convenios convenio=conveniosHome.findById(forma.getConvenio());
		Usuarios usuarios=new Usuarios();
			usuarios.setLogin(usuario.getLoginUsuario());
			logSubirPacientes.setUsuarios(usuarios);
		TiposCargue tipoCargue= new TiposCargue();
		tipoCargue.setCodigo(idTipoCargue);
		logSubirPacientes.setContratos(contrato);
		logSubirPacientes.setConvenios(convenio);
		Date fechaActual=UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.getFechaActual());
		String horaActual=UtilidadFecha.getHoraActual();
		logSubirPacientes.setFechaCargue(fechaActual);
		logSubirPacientes.setHoraCargue(horaActual);
		logSubirPacientes.setTiposCargue(tipoCargue);
		logSubirPacientes.setTotalLeidos(numeroRegistrosLeidos);				
		logSubirPacientes.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
		logSubirPacientes.setFechaFin(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
		int totalGrabados=listaPersonaArchivoPlano.size()-listaInconsistenciaCarguePrevio.size();
		logSubirPacientes.setTotalGrabados(totalGrabados);
		logSubirPacientesMundo.guardarLogSubirPacientes(logSubirPacientes);
		if(!listaInconsistenciaCampos.isEmpty()){
			InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
			inconsistencia.setLogSubirPacientes(logSubirPacientes);
			inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaCampos"));
			inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoInconsistenciaDatosBasicos);
			inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
			for(DtoInconsistenciasArchivoPlano campo:listaInconsistenciaCampos){
				InconsistenciasCampos inconsistenciaCampo= new InconsistenciasCampos();
				inconsistenciaCampo.setInconsistenSubirPaciente(inconsistencia);
				inconsistenciaCampo.setLinea(campo.getNumeroFila());
				inconsistenciaCampo.setNombreCampo(campo.getCampo());
				inconsistenciaCampo.setTipoInconsistencia(campo.getTipoInconsistencia());
				inconsistenciaCamposMundo.guardarInconsistenciasCampos(inconsistenciaCampo);
			}
		}
		if(!listaInconsistenciaCarguePrevio.isEmpty()){
			InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
			inconsistencia.setLogSubirPacientes(logSubirPacientes);
			inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaCarguePrevio"));
			inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoCarguePrevioMismoPeriodo);
			inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
			for(DtoInconsistenciasArchivoPlano carguePrevio:listaInconsistenciaCarguePrevio){
				CapitadoInconsistencia capitadoInconsistencia= new CapitadoInconsistencia();
				capitadoInconsistencia.setInconsistenSubirPaciente(inconsistencia);
				capitadoInconsistencia.setLinea(carguePrevio.getNumeroFila());
				capitadoInconsistencia.setTipoInconsistencia(carguePrevio.getTipoInconsistencia());
				capitadoInconsistencia.setTipoIdentificacion(carguePrevio.getPersonaArchivo().getTipoIdentificacion());
				capitadoInconsistencia.setNumeroIdentificacion(carguePrevio.getPersonaArchivo().getNumeroIdentificacion());
				capitadoInconsistencia.setPrimerNombre(carguePrevio.getPersonaArchivo().getPrimerNombre());
				capitadoInconsistencia.setSegundoNombre(carguePrevio.getPersonaArchivo().getSegundoNombre());
				capitadoInconsistencia.setPrimerApellido(carguePrevio.getPersonaArchivo().getPrimerApellido());
				capitadoInconsistencia.setSegundoApellido(carguePrevio.getPersonaArchivo().getSegundoApellido());
				capitadoInconsistencia.setFechaNacimiento(carguePrevio.getPersonaArchivo().getFechaNacimiento());
				capitadoInconsistenciaMundo.guardarCapitadoInconsistencia(capitadoInconsistencia);
			}
		}
		ContratoCargue contratoCargue = new ContratoCargue();
		contratoCargue.setContratos(contrato);
		contratoCargue.setFechaCargue(fechaActual);
		contratoCargue.setHoraCargue(horaActual);
		contratoCargue.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
		contratoCargue.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
		contratoCargue.setTotalPacientes(totalGrabados);
		if(contrato.getUpc() != null){
			contratoCargue.setUpc(contrato.getUpc());
			contratoCargue.setValorTotal(contrato.getUpc().multiply(new BigDecimal(totalGrabados)));
		}
		else{
			if(contrato.getPorcentajeUpc() != null){
				IUnidadPagoMundo unidadPagoMundo = CapitacionFabricaMundo.crearUnidadPagoMundo();
				ArrayList<UnidadPago> unidadesPago=unidadPagoMundo.consultarUnidadPagoPorFecha(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
				if(unidadesPago != null && !unidadesPago.isEmpty()){
					UnidadPago unidadPago=unidadesPago.get(0);
					BigDecimal valorUpc=unidadPago.getValor().multiply((new BigDecimal(contrato.getPorcentajeUpc()).divide(new BigDecimal(100))));
					contratoCargue.setUpc(valorUpc);
					contratoCargue.setValorTotal(valorUpc.multiply(new BigDecimal(totalGrabados)));
				}
				else{
					contratoCargue.setUpc(new BigDecimal(0));
					contratoCargue.setValorTotal(new BigDecimal(0));
				}
			}
			else{
				contratoCargue.setUpc(new BigDecimal(0));
				contratoCargue.setValorTotal(new BigDecimal(0));
			}
		}
		contratoCargue.setAnulado(false);
		contratoCargue.setAjustesDebito(new BigDecimal(0));
		contratoCargue.setAjustesCredito(new BigDecimal(0));
		contratoCargueMundo.guardarContratoCargue(contratoCargue);	
		forma.setTotalLeidos(new Long(numeroRegistrosLeidos).intValue());
		forma.setTotalGrabados(listaPersonaArchivoPlano.size()-listaInconsistenciaCarguePrevio.size());
		forma.setTotalInconsistencias(listaInconsistenciaCampos.size()+listaInconsistenciaCarguePrevio.size());
	}
	
	
	private void guardarLogFinalPersonas(SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request, int idTipoCargue)
	{
		ILogSubirPacientesMundo logSubirPacientesMundo=CapitacionFabricaMundo.crearLogSubirPacientesMundo();
		IInconsistenSubirPacienteMundo inconsistenciaSubirPacientesMundo=CapitacionFabricaMundo.crearInconsistenSubirPacienteMundo();
		IInconsistenciasCamposMundo inconsistenciaCamposMundo=CapitacionFabricaMundo.crearInconsistenciasCamposMundo();
		ICapitadoInconsistenciaMundo capitadoInconsistenciaMundo=CapitacionFabricaMundo.crearCapitadoInconsistenciaMundo();
		IInconsistenciaPersonaMundo inconsistenciaPersonaMundo=CapitacionFabricaMundo.crearInconsistenciaPersonaMundo();
		IContratoCargueMundo contratoCargueMundo=CapitacionFabricaMundo.crearContratoCargueMundo();
		LogSubirPacientes logSubirPacientes=new LogSubirPacientes(); 
		ContratosHome contratosHome=new ContratosHome();
		Contratos contrato=contratosHome.findById(forma.getContrato());
		ConveniosHome conveniosHome = new ConveniosHome();
		Convenios convenio=conveniosHome.findById(forma.getConvenio());
		Usuarios usuarios=new Usuarios();
			usuarios.setLogin(usuario.getLoginUsuario());
			logSubirPacientes.setUsuarios(usuarios);
		TiposCargue tipoCargue= new TiposCargue();
		tipoCargue.setCodigo(idTipoCargue);
		logSubirPacientes.setContratos(contrato);
		logSubirPacientes.setConvenios(convenio);
		Date fechaActual=UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.getFechaActual());
		String horaActual=UtilidadFecha.getHoraActual();
		logSubirPacientes.setFechaCargue(fechaActual);
		logSubirPacientes.setHoraCargue(horaActual);
		logSubirPacientes.setTiposCargue(tipoCargue);
		logSubirPacientes.setTotalLeidos(numeroRegistrosLeidos);				
		logSubirPacientes.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
		logSubirPacientes.setFechaFin(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
		int totalGrabados=listaPersonaArchivoPlano.size()+numeroInconsistenciasSeleccionadas-listaInconsistenciaCarguePrevio.size();
		logSubirPacientes.setTotalGrabados(totalGrabados);
		logSubirPacientesMundo.guardarLogSubirPacientes(logSubirPacientes);
		if(!listaInconsistenciaCampos.isEmpty()){
			InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
			inconsistencia.setLogSubirPacientes(logSubirPacientes);
			inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaCampos"));
			inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoInconsistenciaDatosBasicos);
			inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
			for(DtoInconsistenciasArchivoPlano campo:listaInconsistenciaCampos){
				InconsistenciasCampos inconsistenciaCampo= new InconsistenciasCampos();
				inconsistenciaCampo.setInconsistenSubirPaciente(inconsistencia);
				inconsistenciaCampo.setLinea(campo.getNumeroFila());
				inconsistenciaCampo.setNombreCampo(campo.getCampo());
				inconsistenciaCampo.setTipoInconsistencia(campo.getTipoInconsistencia());
				inconsistenciaCamposMundo.guardarInconsistenciasCampos(inconsistenciaCampo);
			}
		}
		if(!listaInconsistenciaCarguePrevio.isEmpty()){
			InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
			inconsistencia.setLogSubirPacientes(logSubirPacientes);
			inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaCarguePrevio"));
			inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoCarguePrevioMismoPeriodo);
			inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
			for(DtoInconsistenciasArchivoPlano carguePrevio:listaInconsistenciaCarguePrevio){
				CapitadoInconsistencia capitadoInconsistencia= new CapitadoInconsistencia();
				capitadoInconsistencia.setInconsistenSubirPaciente(inconsistencia);
				capitadoInconsistencia.setLinea(carguePrevio.getNumeroFila());
				capitadoInconsistencia.setTipoInconsistencia(carguePrevio.getTipoInconsistencia());
				capitadoInconsistencia.setTipoIdentificacion(carguePrevio.getPersonaArchivo().getTipoIdentificacion());
				capitadoInconsistencia.setNumeroIdentificacion(carguePrevio.getPersonaArchivo().getNumeroIdentificacion());
				capitadoInconsistencia.setPrimerNombre(carguePrevio.getPersonaArchivo().getPrimerNombre());
				capitadoInconsistencia.setSegundoNombre(carguePrevio.getPersonaArchivo().getSegundoNombre());
				capitadoInconsistencia.setPrimerApellido(carguePrevio.getPersonaArchivo().getPrimerApellido());
				capitadoInconsistencia.setSegundoApellido(carguePrevio.getPersonaArchivo().getSegundoApellido());
				capitadoInconsistencia.setFechaNacimiento(carguePrevio.getPersonaArchivo().getFechaNacimiento());
				capitadoInconsistenciaMundo.guardarCapitadoInconsistencia(capitadoInconsistencia);
			}
		}
		if(!listaInconsistenciaPersonaBD.isEmpty()){
			InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
			inconsistencia.setLogSubirPacientes(logSubirPacientes);
			inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaDatosIguales"));
			inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoInconsistenciaDatosIguales);
			inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
			for(DtoInconsistenciasArchivoPlano persona:listaInconsistenciaPersonaBD){
				CapitadoInconsistencia capitadoInconsistencia= new CapitadoInconsistencia();
				capitadoInconsistencia.setInconsistenSubirPaciente(inconsistencia);
				capitadoInconsistencia.setLinea(persona.getNumeroFila());
				capitadoInconsistencia.setTipoInconsistencia(persona.getTipoInconsistencia());
				capitadoInconsistencia.setTipoIdentificacion(persona.getPersonaArchivo().getTipoIdentificacion());
				capitadoInconsistencia.setNumeroIdentificacion(persona.getPersonaArchivo().getNumeroIdentificacion());
				capitadoInconsistencia.setPrimerNombre(persona.getPersonaArchivo().getPrimerNombre());
				capitadoInconsistencia.setSegundoNombre(persona.getPersonaArchivo().getSegundoNombre());
				capitadoInconsistencia.setPrimerApellido(persona.getPersonaArchivo().getPrimerApellido());
				capitadoInconsistencia.setSegundoApellido(persona.getPersonaArchivo().getSegundoApellido());
				capitadoInconsistencia.setFechaNacimiento(persona.getPersonaArchivo().getFechaNacimiento());
				capitadoInconsistenciaMundo.guardarCapitadoInconsistencia(capitadoInconsistencia);
				for(DtoPersonas personaBD:persona.getListaPersonas()){
					InconsistenciaPersona inconsistenciaPersona= new InconsistenciaPersona();
					Personas per = new Personas();
					per.setCodigo(personaBD.getCodigo());
					inconsistenciaPersona.setCapitadoInconsistencia(capitadoInconsistencia);
					inconsistenciaPersona.setPersonas(per);
					inconsistenciaPersonaMundo.guardarInconsistenciaPersona(inconsistenciaPersona);
				}
			}
		}
		
		ContratoCargue contratoCargue = new ContratoCargue();
		contratoCargue.setContratos(contrato);
		contratoCargue.setFechaCargue(fechaActual);
		contratoCargue.setHoraCargue(horaActual);
		contratoCargue.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
		contratoCargue.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
		contratoCargue.setTotalPacientes(totalGrabados);
		if(contrato.getUpc() != null){
			contratoCargue.setUpc(contrato.getUpc());
			contratoCargue.setValorTotal(contrato.getUpc().multiply(new BigDecimal(totalGrabados)));
		}
		else{
			if(contrato.getPorcentajeUpc() != null){
				IUnidadPagoMundo unidadPagoMundo = CapitacionFabricaMundo.crearUnidadPagoMundo();
				ArrayList<UnidadPago> unidadesPago=unidadPagoMundo.consultarUnidadPagoPorFecha(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
				if(unidadesPago != null && !unidadesPago.isEmpty()){
					UnidadPago unidadPago=unidadesPago.get(0);
					BigDecimal valorUpc=unidadPago.getValor().multiply((new BigDecimal(contrato.getPorcentajeUpc()).divide(new BigDecimal(100))));
					contratoCargue.setUpc(valorUpc);
					contratoCargue.setValorTotal(valorUpc.multiply(new BigDecimal(totalGrabados)));
				}
				else{
					contratoCargue.setUpc(new BigDecimal(0));
					contratoCargue.setValorTotal(new BigDecimal(0));
				}
			}
			else{
				contratoCargue.setUpc(new BigDecimal(0));
				contratoCargue.setValorTotal(new BigDecimal(0));
			}
		}
		contratoCargue.setAnulado(false);
		contratoCargue.setAjustesDebito(new BigDecimal(0));
		contratoCargue.setAjustesCredito(new BigDecimal(0));
		contratoCargueMundo.guardarContratoCargue(contratoCargue);	
		forma.setTotalLeidos(new Long(numeroRegistrosLeidos).intValue());
		forma.setTotalGrabados(listaPersonaArchivoPlano.size()+numeroInconsistenciasSeleccionadas-listaInconsistenciaCarguePrevio.size());
		forma.setTotalInconsistencias(listaInconsistenciaCampos.size()+listaInconsistenciaCarguePrevio.size()+listaInconsistenciaPersonaBD.size());
	}
	
	private void guardarLogInconsistenciaGeneral(SubirPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request, int idTipoCargue)
	{
		ILogSubirPacientesMundo logSubirPacientesMundo=CapitacionFabricaMundo.crearLogSubirPacientesMundo();
		IInconsistenSubirPacienteMundo inconsistenciaSubirPacientesMundo=CapitacionFabricaMundo.crearInconsistenSubirPacienteMundo();
		LogSubirPacientes logSubirPacientes=new LogSubirPacientes(); 
		ContratosHome contratosHome=new ContratosHome();
		Contratos contrato=contratosHome.findById(forma.getContrato());
		ConveniosHome conveniosHome = new ConveniosHome();
		Convenios convenio=conveniosHome.findById(forma.getConvenio());
		Usuarios usuarios=new Usuarios();
			usuarios.setLogin(usuario.getLoginUsuario());
			logSubirPacientes.setUsuarios(usuarios);
		TiposCargue tipoCargue= new TiposCargue();
		tipoCargue.setCodigo(idTipoCargue);
		logSubirPacientes.setContratos(contrato);
		logSubirPacientes.setConvenios(convenio);
		logSubirPacientes.setFechaCargue(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.getFechaActual()));
		logSubirPacientes.setHoraCargue(UtilidadFecha.getHoraActual());
		logSubirPacientes.setTiposCargue(tipoCargue);
		logSubirPacientes.setTotalLeidos(numeroRegistrosLeidos);				
		logSubirPacientes.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
		logSubirPacientes.setFechaFin(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
		logSubirPacientes.setTotalGrabados(0);
		logSubirPacientesMundo.guardarLogSubirPacientes(logSubirPacientes);
		InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
		inconsistencia.setLogSubirPacientes(logSubirPacientes);
		inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaGeneral"));
		inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoInconsistenciaGeneral);
		inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
	}
	
	private void guardarLogIndividual(SubirPacienteForm forma, UsuarioBasico usuario, int idTipoCargue)
	{
		ILogSubirPacientesMundo logSubirPacientesMundo=CapitacionFabricaMundo.crearLogSubirPacientesMundo();
		IInconsistenSubirPacienteMundo inconsistenciaSubirPacientesMundo=CapitacionFabricaMundo.crearInconsistenSubirPacienteMundo();
		ICapitadoInconsistenciaMundo capitadoInconsistenciaMundo=CapitacionFabricaMundo.crearCapitadoInconsistenciaMundo();
		IInconsistenciaPersonaMundo inconsistenciaPersonaMundo=CapitacionFabricaMundo.crearInconsistenciaPersonaMundo();
		LogSubirPacientes logSubirPacientes=new LogSubirPacientes(); 
		ContratosHome contratosHome=new ContratosHome();
		Contratos contrato=contratosHome.findById(forma.getContrato());
		ConveniosHome conveniosHome = new ConveniosHome();
		Convenios convenio=conveniosHome.findById(forma.getConvenio());
		Usuarios usuarios=new Usuarios();
			usuarios.setLogin(usuario.getLoginUsuario());
			logSubirPacientes.setUsuarios(usuarios);
		TiposCargue tipoCargue= new TiposCargue();
		tipoCargue.setCodigo(idTipoCargue);
		logSubirPacientes.setContratos(contrato);
		logSubirPacientes.setConvenios(convenio);
		logSubirPacientes.setFechaCargue(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.getFechaActual()));
		logSubirPacientes.setHoraCargue(UtilidadFecha.getHoraActual());
		logSubirPacientes.setTiposCargue(tipoCargue);
		logSubirPacientes.setTotalLeidos(1);				
		logSubirPacientes.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaInicial()));
		logSubirPacientes.setFechaFin(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaFinal()));
		logSubirPacientes.setTotalGrabados(1);
		logSubirPacientesMundo.guardarLogSubirPacientes(logSubirPacientes);
		if(!listaSubirPacientesInc.isEmpty()){
			InconsistenSubirPaciente inconsistencia = new InconsistenSubirPaciente();
			inconsistencia.setLogSubirPacientes(logSubirPacientes);
			inconsistencia.setDescripcion(messageResource.getMessage("subirPacienteForm.inconsistenciaDatosIguales"));
			inconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoInconsistenciaDatosIguales);
			inconsistenciaSubirPacientesMundo.guardarInconsistenciaSubirPaciente(inconsistencia);
			for(DTOSubirPacientesInconsistencias dtoInconsistencia:listaSubirPacientesInc){
				CapitadoInconsistencia capitadoInconsistencia= new CapitadoInconsistencia();
				capitadoInconsistencia.setInconsistenSubirPaciente(inconsistencia);
				capitadoInconsistencia.setLinea(0);
				capitadoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoNumIDNoCorresponde);
				capitadoInconsistencia.setTipoIdentificacion(dtoInconsistencia.getDtoPersonaArchivoPlano().getTipoIdentificacion());
				capitadoInconsistencia.setNumeroIdentificacion(dtoInconsistencia.getDtoPersonaArchivoPlano().getNumeroIdentificacion());
				capitadoInconsistencia.setPrimerNombre(dtoInconsistencia.getDtoPersonaArchivoPlano().getPrimerNombre());
				capitadoInconsistencia.setSegundoNombre(dtoInconsistencia.getDtoPersonaArchivoPlano().getSegundoNombre());
				capitadoInconsistencia.setPrimerApellido(dtoInconsistencia.getDtoPersonaArchivoPlano().getPrimerApellido());
				capitadoInconsistencia.setSegundoApellido(dtoInconsistencia.getDtoPersonaArchivoPlano().getSegundoApellido());
				capitadoInconsistencia.setFechaNacimiento(dtoInconsistencia.getDtoPersonaArchivoPlano().getFechaNacimiento());
				capitadoInconsistenciaMundo.guardarCapitadoInconsistencia(capitadoInconsistencia);
				for(DtoPersonas personaBD:dtoInconsistencia.getListaPersonasBD()){
					InconsistenciaPersona inconsistenciaPersona= new InconsistenciaPersona();
					Personas per = new Personas();
					per.setCodigo(personaBD.getCodigo());
					inconsistenciaPersona.setCapitadoInconsistencia(capitadoInconsistencia);
					inconsistenciaPersona.setPersonas(per);
					inconsistenciaPersonaMundo.guardarInconsistenciaPersona(inconsistenciaPersona);
				}
			}
		}
	}
}
