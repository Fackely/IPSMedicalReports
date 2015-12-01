package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.actionform.odontologia.IngresoPacienteOdontologiaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.IngresoPacienteOdontologiaDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.facturacion.DtoAutorizacionConvIngPac;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.manejoPaciente.DtoCamposPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.DtoBusquedaPacientesConvOdo;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.manejoPaciente.PerfilNED;
import com.servinte.axioma.orm.AutorizacionConvIngPac;
import com.servinte.axioma.orm.BonosConvIngPac;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.ContratosHome;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.ConveniosIngresoPacienteHome;
import com.servinte.axioma.orm.DetAutorizacionConvIngPac;
import com.servinte.axioma.orm.EmisionBonosDesc;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.PacientesHome;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.UsuariosHome;
import com.servinte.axioma.orm.ValidacionesBdConvIngPac;
import com.servinte.axioma.orm.delegate.facturacion.convenio.BonosConvIngPacDelegate;
import com.servinte.axioma.orm.delegate.odontologia.PacientesConvOdoDelegate;
import com.servinte.axioma.orm.delegate.odontologia.bonos.EmisionBonosDescDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.impl.odontologia.administracion.EmisionBonosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEmisionBonosServicio;

public class IngresoPacienteOdontologia {

	
	private static Logger logger = Logger.getLogger(IngresoPacienteOdontologia.class);
	private DtoPaciente pacienteOdontologico;
	
	
	private static IngresoPacienteOdontologiaDao getIngresoPacienteOdontologiaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoPacienteOdontologiaDao();		
	}

	public IngresoPacienteOdontologia()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.pacienteOdontologico= new DtoPaciente();		
	}
	
	
	@SuppressWarnings("unchecked")
	public HashMap cargarDatosPaciente(PersonaBasica paciente) {
		
		HashMap datosPaciente=new HashMap();
        datosPaciente.put("primernombre",paciente.getPrimerNombre());
        datosPaciente.put("segundonombre",paciente.getSegundoNombre());
        datosPaciente.put("primerapellido",paciente.getPrimerApellido());
        datosPaciente.put("segundoapellido",paciente.getSegundoApellido());
        datosPaciente.put("tipodocumento",paciente.getTipoIdentificacionPersona(false));
        datosPaciente.put("numeroidentificacion",paciente.getNumeroIdentificacionPersona());
        datosPaciente.put("fechanacimiento",paciente.getFechaNacimiento());
         
        return datosPaciente;
	}

	
	
	/**
	 * 
	 * @param forma
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public ActionErrors validacionDatosRequeridos(IngresoPacienteOdontologiaForm forma) {
		
		
		ActionErrors errores = new ActionErrors();
		//****************VALIDACION DE CAMPOS******************************************************
		//Paï¿½s de expedicion
	   //	if(forma.getPaciente().getCodigoPaisId().equals(""))
	   //		errores.add("Pais de expedicion es requerido", new ActionMessage("errors.required","El paï¿½s de expediciï¿½n"));
		
		//Ciudad de expediciï¿½n
		//if(forma.getPaciente().getCodigoCiudadId().equals(""))
		//	errores.add("Ciudad de expedicion es requerido", new ActionMessage("errors.required","La ciudad de expediciï¿½n"));
		
		//Se quitan los espacios de los campos del nombre
		forma.getPaciente().setPrimerApellido(forma.getPaciente().getPrimerApellido().trim());
		forma.getPaciente().setSegundoApellido(forma.getPaciente().getSegundoApellido().trim());
		forma.getPaciente().setPrimerNombre(forma.getPaciente().getPrimerNombre().trim());
		forma.getPaciente().setSegundoNombre(forma.getPaciente().getSegundoNombre().trim());
		
		//Primer apellido
		if(forma.getPaciente().getPrimerApellido().equals(""))
			errores.add("primer apellido es requerido", new ActionMessage("errors.required","El primer apellido"));
		//Primer nombre
		if(forma.getPaciente().getPrimerNombre().equals(""))
			errores.add("primer nombre es requerido", new ActionMessage("errors.required","El primer nombre"));
		
		//Paï¿½s nacimiento
		//if(forma.getPaciente().getCodigoPaisNacimiento().equals(""))
		//	errores.add("Pais de expedicion es nacimiento", new ActionMessage("errors.required","El paï¿½s de nacimiento"));
		
		//Ciudad nacimiento
		//if(forma.getPaciente().getCodigoCiudadNacimiento().equals(""))
			//errores.add("Ciudad de nacimiento es requerido", new ActionMessage("errors.required","La ciudad de nacimiento"));
		
		
		//Ciudad recidencia
		if(UtilidadTexto.isEmpty(forma.getPaciente().getCiudadDeptoPais()))
		{
			errores.add("ciudad residencia", new ActionMessage("errors.required","La Ciudad Residencia"));
		}

		
		//Fecha de nacimiento
		if(forma.getPaciente().getFechaNacimiento().equals(""))
			errores.add("Fecha de nacimiento es requerido", new ActionMessage("errors.required","La fecha de nacimiento"));
		else
		{
			//Se verifica que la fecha sea vï¿½lida
			if(!UtilidadFecha.validarFecha(forma.getPaciente().getFechaNacimiento()))
				errores.add("Fecha nacimiento invï¿½lida", new ActionMessage("errors.formatoFechaInvalido","de nacimiento"));
			//Se verifica que la fecha sea menor o igual a la actual
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getPaciente().getFechaNacimiento(), UtilidadFecha.getFechaActual()))
					errores.add("FEcha nacimiento es mayor a fecha actual", new ActionMessage("errors.fechaPosteriorIgualActual","de nacimiento","actual"));
			
		}
		
		//Pais de residencia
		//if(forma.getPaciente().getCodigoPaisResidencia().equals(""))
		//	errores.add("Pais de residencia es requerido", new ActionMessage("errors.required","El paï¿½s de residencia"));
		
		//Ciudad de residencia
		//if(forma.getPaciente().getCodigoCiudadResidencia().equals(""))
		//	errores.add("Ciudad de residencia es requerido", new ActionMessage("errors.required","La ciudad de residencia"));
		
		//Barrio 
		//if(forma.getPaciente().getCodigoBarrio().equals(""))
		//	errores.add("Barrios es requirido",new ActionMessage("errors.required","El barrio"));
		
		//Direccion
		//forma.getPaciente().setDireccion(forma.getPaciente().getDireccion().trim());
		//if(forma.getPaciente().getDireccion().equals(""))
		//	errores.add("Direcciï¿½n es requerida",new ActionMessage("errors.required","La direcciï¿½n"));
		
		
		//Telï¿½fono celular
		/*
		if(!forma.getPaciente().getTelefonoCelular().equals("")&&Utilidades.convertirADouble(forma.getPaciente().getTelefonoCelular())==ConstantesBD.codigoNuncaValidoDoubleNegativo)
		{
			errores.add("Telefono celular", new ActionMessage("errors.integer","El telï¿½fono celular"));
		}
		*/
		
		// Telefono fijo
		if(UtilidadTexto.isEmpty(forma.getPaciente().getTelefonoFijo()))
		{
			errores.add("Telefono fijo", new ActionMessage("errors.required","El teléfono fijo"));
		}
		
		//Zona Domicilio
		//if(forma.getPaciente().getZonaDomicilio().equals(""))
		//	errores.add("Zona domicilio es requerida",new ActionMessage("errors.required","La zona de domicilio"));
		
		//Ocupaciï¿½n
		//if(forma.getPaciente().getOcupacion().equals(""))
		//	errores.add("Ocupaciï¿½n es requerida",new ActionMessage("errors.required","La ocupaciï¿½n"));
		
		//Sexo
		if(forma.getPaciente().getSexo().equals(""))
			errores.add("Sexo es requerido",new ActionMessage("errors.required","El sexo"));
        
	 int pos=0;
	  for(int i=0; i< forma.getArrayBeneficiarios().size(); i++)
 	      {
		  DtoBeneficiarioPaciente dtoBenef=new DtoBeneficiarioPaciente();
		  dtoBenef=forma.getArrayBeneficiarios().get(i);
		  
		  pos++;
		  if(i>=0 && dtoBenef.getCodigoTipoIdentificacion()!=null && dtoBenef.getNumeroId()!=null)
		  {
 		   	   if(!dtoBenef.getCodigoTipoIdentificacion().equals("") && !dtoBenef.getNumeroId().equals("") )
 			   {
 		   		//Primer apellido Beneficiario
 		  		if(dtoBenef.getPrimerApellido()==null || dtoBenef.getPrimerApellido().equals(""))
 		  			errores.add("primer apellido es requerido", new ActionMessage("errors.required","El primer apellido del Beneficiario "+pos));
 		  		
 		  		//Primer nombre Beneficiario 		  		
 		  		if(dtoBenef.getPrimerNombre()==null || dtoBenef.getPrimerNombre().equals(""))
 		  			errores.add("primer nombre es requerido", new ActionMessage("errors.required","El primer nombre del Beneficiario "+pos));		   		
 		  		
 		  		//Parentesco
 		  		if(dtoBenef.getParentezco()==null || dtoBenef.getParentezco().equals(""))
 		  			errores.add("parentesco es requerido", new ActionMessage("errors.required","El Parentesco con del Paciente del Beneficiario "+pos));		   		
 		  		
 		  		//Sexo
 		  		if(dtoBenef.getSexo()==null || dtoBenef.getSexo().equals(""))
 		  			errores.add("sexo es requerido", new ActionMessage("errors.required","El Sexo del Beneficiario "+pos));		   		
 		  		
 		  		
 			   }else
 			   {
 				  if((!dtoBenef.getCodigoTipoIdentificacion().equals("") && dtoBenef.getNumeroId().equals("") )|| (!dtoBenef.getNumeroId().equals("") && dtoBenef.getCodigoTipoIdentificacion().equals("") ))
 				  {
 					 errores.add("Datos Beneficiario", new ActionMessage("errors.required","Los Datos del Beneficiario  "+pos));
 				  }
 			   }
		    }
 	      }
		return errores;
	}

	

	/**
	 * Metodo para consultar si un Paciente de Odontologia tiene asiganada una plantilla segun el tipo de funcionalidad
	 * @param codigoPaciente
	 * @param tipoPlantilla
	 * @return
	 */
	public int verificarExistenciaPlantillaPaciente(int codigoPaciente, String tipoFuncionalidad)
	{
	  return getIngresoPacienteOdontologiaDao().existenciaPlantillaPaciente(codigoPaciente,tipoFuncionalidad);	
	}
	
	
	
	/**
	 *Metodo para guardar un nueva plantilla los campos parametrizables ( cuando el paciente no tiene plantilla asignada previamente) 
	 * @param con
	 * @param plantilla
	 * @param codigoPersona
	 * @param tipoFuncionalidad
	 * @return
	 */
	public ResultadoBoolean guardarCamposParametrizables(Connection con,DtoPlantilla plantilla, int codigoPersona, String tipoFuncionalidad, String loginUsuario, int institucion) {
		
		return Plantillas.guardarCamposParametrizablesPacOdontologia(con, plantilla, codigoPersona, tipoFuncionalidad, loginUsuario, institucion);
	}

	/**
	 * Metodo para modificar una plantilla los datos parametrizables ( cuando el paciente ya tiene asignada una plantilla
	 * @param con
	 * @param plantilla
	 * @param codigoPersona
	 * @param codPlantillaPaciente
	 * @param tipoFuncionalidad
	 * @return
	 */
	public ResultadoBoolean modificarCamposParametrizablesPacOdontologia(Connection con,DtoPlantilla plantilla, int codigoPersona,int codPlantillaPaciente, String tipoFuncionalidad,String loginUsuario, int institucion) {
		
		return Plantillas.modificarCamposParametrizablesPacOdontologia(con,plantilla,codigoPersona,codPlantillaPaciente,tipoFuncionalidad,loginUsuario, institucion);
	}

	
	
	/**
	 * Metodo para cargar un paciente 
	 * @param forma
	 * @return
	 */
	public Paciente cargarMundoPaciente(IngresoPacienteOdontologiaForm forma, String usuario) {
		
		Paciente mundoPaciente= new Paciente();
		
		//Se llena el mundo del paciente con los datos del form		
		// 
		mundoPaciente.setCodigoPersona(forma.getPaciente().getCodigo());
		mundoPaciente.setCodigoTipoPersona(ConstantesBDManejoPaciente.codigoTipoPersonaNatural+"");
		mundoPaciente.setNumeroHistoriaClinica(forma.getPaciente().getNumeroHistoriaClinica());
		mundoPaciente.setAnioHistoriaClinica(forma.getPaciente().getAnioHistoriaClincia());
		mundoPaciente.setCodigoTipoIdentificacion(forma.getPaciente().getTipoIdentificacion());
		mundoPaciente.setNumeroIdentificacion(forma.getPaciente().getNumeroIdentificacion());
		//mundoPaciente.setCodigoDepartamentoId(forma.getPaciente().getCodigoCiudadId().split(ConstantesBD.separadorSplit)[0]);
		//mundoPaciente.setCodigoCiudadId(forma.getPaciente().getCodigoCiudadId().split(ConstantesBD.separadorSplit)[1]);
		//mundoPaciente.setCodigoPaisId(forma.getPaciente().getCodigoPaisId());
		mundoPaciente.setPrimerApellidoPersona(forma.getPaciente().getPrimerApellido());
		mundoPaciente.setSegundoApellidoPersona(forma.getPaciente().getSegundoApellido());
		mundoPaciente.setPrimerNombrePersona(forma.getPaciente().getPrimerNombre());
		mundoPaciente.setSegundoNombrePersona(forma.getPaciente().getSegundoNombre());
		//mundoPaciente.setCodigoPaisIdentificacion(forma.getPaciente().getCodigoPaisNacimiento());
		//mundoPaciente.setCodigoDepartamentoIdentificacion(forma.getPaciente().getCodigoCiudadNacimiento().split(ConstantesBD.separadorSplit)[0]);
		//mundoPaciente.setCodigoCiudadIdentificacion(forma.getPaciente().getCodigoCiudadNacimiento().split(ConstantesBD.separadorSplit)[1]);
		mundoPaciente.setNumeroHijos(forma.getPaciente().getNroHijos());
		String[] fechaNac = forma.getPaciente().getFechaNacimiento().split("/"); 
		mundoPaciente.setAnioNacimiento(fechaNac[2]);
		mundoPaciente.setMesNacimiento(fechaNac[1]);
		mundoPaciente.setDiaNacimiento(fechaNac[0]);
		//mundoPaciente.setCodigoPais(forma.getPaciente().getCodigoPaisResidencia());
		//mundoPaciente.setCodigoDepartamento(forma.getPaciente().getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0]);
		//mundoPaciente.setCodigoCiudad(forma.getPaciente().getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]);
		//mundoPaciente.setCodigoBarrio(forma.getPaciente().getCodigoBarrio());
		//mundoPaciente.setCodigoLocalidad(forma.getPaciente().getCodigoLocalidad());
		//mundoPaciente.setDireccion(forma.getPaciente().getDireccion());
		mundoPaciente.setTelefono(forma.getPaciente().getTelefono());
		mundoPaciente.setTelefonoFijo(forma.getPaciente().getTelefonoFijo()+"");
		mundoPaciente.setTelefonoCelular(forma.getPaciente().getTelefonoCelular());
		//mundoPaciente.setEmail(forma.getPaciente().getEmail());
		//mundoPaciente.setCodigoZonaDomicilio(forma.getPaciente().getZonaDomicilio());
		//mundoPaciente.setCodigoOcupacion(forma.getPaciente().getOcupacion());
		mundoPaciente.setCodigoSexo(forma.getPaciente().getSexo());
		//mundoPaciente.setCodigoTipoSangre(forma.getPaciente().getTipoSangre());
		//mundoPaciente.setCodigoTipoPersona(forma.getPaciente().getTipoPersona().split("-")[0]);
		//mundoPaciente.setCodigoEstadoCivil(forma.getPaciente().getEstadoCivil());
		mundoPaciente.setCentro_atencion(forma.getPaciente().getCentroAtencion().equals("")?0:Integer.parseInt(forma.getPaciente().getCentroAtencion()));
		/*mundoPaciente.setEtnia(forma.getPaciente().getEtnia().equals("")?0:Integer.parseInt(forma.getPaciente().getEtnia()));
		mundoPaciente.setCodigoReligion(forma.getPaciente().getReligion());
		mundoPaciente.setLee_escribe(UtilidadTexto.getBoolean(forma.getPaciente().getLeeEscribe()));
		mundoPaciente.setCodigoZonaDomicilio(forma.getPaciente().getZonaDomicilio());
		
		mundoPaciente.setEstudio(forma.getPaciente().getEstudio().equals("")?0:Integer.parseInt(forma.getPaciente().getEstudio()));
		mundoPaciente.setCodigoGrupoPoblacional(forma.getPaciente().getGrupoPoblacional());
		mundoPaciente.setFoto(forma.getPaciente().getFoto());*/
		mundoPaciente.setActivo(forma.getPaciente().getActivo());
		
		
		
		// Datos Motivo Cita
		 
		 mundoPaciente.getMotivoCitaPaciente().setCodigoPaciente(forma.getPaciente().getCodigo()+"");
		 mundoPaciente.getMotivoCitaPaciente().setCodMotivo(forma.getMotivoCitaPaciente().getCodMotivo());
		 mundoPaciente.getMotivoCitaPaciente().setCodMedioConocimiento(forma.getMotivoCitaPaciente().getCodMedioConocimiento());
		 
		 mundoPaciente.getMotivoCitaPaciente().setFechaModificacion(forma.getMotivoCitaPaciente().getFechaProgramacion());
		 mundoPaciente.setFechaMotivoCita(forma.getMotivoCitaPaciente().getFechaProgramacion());
		 mundoPaciente.getMotivoCitaPaciente().setHoraModificacion(forma.getMotivoCitaPaciente().getHoraProgramacion());
		 mundoPaciente.setHoraMotivoCita(forma.getMotivoCitaPaciente().getHoraProgramacion());
		 mundoPaciente.getMotivoCitaPaciente().setObservaciones(forma.getMotivoCitaPaciente().getObservaciones());
		 mundoPaciente.setObservacionesMotivoCita(forma.getMotivoCitaPaciente().getObservaciones());
		 mundoPaciente.getMotivoCitaPaciente().setUsuarioModificacion(usuario);
	      
		 logger.info("Cargï¿½ Motivo CIta FORM >>"+forma.getMotivoCitaPaciente().getCodMotivo());
		 logger.info("Cargï¿½ Motivo CIta MUNDO PACIENTE >>"+mundoPaciente.getMotivoCitaPaciente().getCodMotivo());
		
		 logger.info("Cargando paciente Num Beneficiarios "+forma.getArrayBeneficiarios().size());
		 
		 // Datos Beneficiarios
		 if(forma.getArrayBeneficiarios().size()>0)
		 {
			 for(int i=0;i<forma.getArrayBeneficiarios().size();i++)
			 {			   
			   mundoPaciente.getBeneficiariosPac().add(forma.getArrayBeneficiarios().get(i));
			 }
		 }
		 
		 // Convenio
		 mundoPaciente.setConvenioReserva(forma.getPaciente().getConvenio());
		 
		return mundoPaciente;
	}
	
	
	
	
	/**
	 * Mï¿½todo que consulta una persona como paciente
	 * @param con
	 * @param parametros
	 * @return DtoPaciente
	 */
	@SuppressWarnings("unchecked")
	public static DtoPaciente consultarPersonaPaciente(Connection con,String numIdentificacion,	String tipoIdentificacion,String activo)
	{
		HashMap parametros = new HashMap();
		parametros.put("numero_identificacion",numIdentificacion);
		parametros.put("tipo_identificacion",tipoIdentificacion);
		if(!activo.equals(""))
			parametros.put("activo",activo);
		return getIngresoPacienteOdontologiaDao().consultarPersonaPaciente(con, parametros);
	}

	
	/**
	 * Metodo para Evaluar si existe un beneficiario 
	 * @param arrayBeneficiarios
	 * @param tipoId
	 * @param numId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean existeBeneficiario(ArrayList<DtoBeneficiarioPaciente> arrayBeneficiarios, int pos,String tipoId, String numId) {
		
		boolean existe = false;
	    logger.info("entro al mundo   tamaï¿½o Array "+arrayBeneficiarios.size() );	
	    logger.info("posicion cero "+arrayBeneficiarios.get(0).getNumeroId() );
	    
	    for(int i=0;i< arrayBeneficiarios.size();i++)
		{
	    	 
		    if(pos!=i)
		    {	
				if(arrayBeneficiarios.get(i).getCodigoTipoIdentificacion().equals(tipoId) && arrayBeneficiarios.get(i).getNumeroId().equals(numId))
				{
					existe=true;
					i=arrayBeneficiarios.size();			
				}
		    }
		}
		
		 logger.info("Salio del mundo" );
		return existe;
	}

	
	/**
	 * @return the pacienteOdontologico
	 */
	public DtoPaciente getPacienteOdontologico() {
		return pacienteOdontologico;
	}

	/**
	 * @param pacienteOdontologico the pacienteOdontologico to set
	 */
	public void setPacienteOdontologico(DtoPaciente pacienteOdontologico) {
		this.pacienteOdontologico = pacienteOdontologico;
	}

	/**
	 * 
	 * @return
	 */
	public boolean insertarPerfilNed(Connection con, DtoEscala escala, int codigoPaciente, String loginUsuario, int institucion) 
	{
		boolean retorna=true;
		DtoPerfilNed dto = cargarEncabezadoPerilNED(escala, codigoPaciente,	loginUsuario, institucion);
		dto.setArrayCampos(cargarCamposPerfilNED(escala, loginUsuario));
		
		if(!PerfilNED.existePerfilNedPaciente(codigoPaciente, institucion))
		{	
			retorna = insertarPerfilNED(con, retorna, dto);
		}
		else
		{
			retorna = modificarPerfilNED(con, codigoPaciente, institucion,retorna, dto);
		}
		
		return retorna;
	}

	/**
	 * @param con
	 * @param codigoPaciente
	 * @param institucion
	 * @param retorna
	 * @param dto
	 * @return
	 */
	private boolean modificarPerfilNED(Connection con, int codigoPaciente,
			int institucion, boolean retorna, DtoPerfilNed dto) {
		DtoPerfilNed dtoWhere = new DtoPerfilNed();
		dtoWhere.setCodigoPk(PerfilNED.cargarPerfilNEDXPaciente(codigoPaciente, institucion).getCodigoPk());
		
		if(!PerfilNED.modificar(con, dto, dtoWhere))
		{
			logger.error("NO MODIFICA ENCABEZADO");
			retorna=false;
		}
		
		for(DtoCamposPerfilNed dtoCampo: dto.getArrayCampos())
		{
			DtoCamposPerfilNed dtoWhereDetalle= new DtoCamposPerfilNed();
			dtoWhereDetalle.setCodigoPerfilNed(dtoWhere.getCodigoPk());
			dtoWhereDetalle.setEscalaCampoSeccion(dtoCampo.getEscalaCampoSeccion());
			
			if(!PerfilNED.modificarCampos(con, dtoCampo, dtoWhereDetalle))
			{
				logger.error("NO INSERTA CAMPO ");
				retorna= false;
			}
		}
		return retorna;
	}

	/**
	 * @param con
	 * @param retorna
	 * @param dto
	 * @return
	 */
	private boolean insertarPerfilNED(Connection con, boolean retorna,	DtoPerfilNed dto) {
		double codigoPerfilNED= PerfilNED.guardar(con,dto);
		
		if(codigoPerfilNED<=0)
		{
			logger.error("NO INSERTA ENCABEZADO");
			retorna=false;
		}
		
		if(retorna)
		{	
			for(DtoCamposPerfilNed dtoCampo: dto.getArrayCampos())
			{
				dtoCampo.setCodigoPerfilNed(codigoPerfilNED);
				if(PerfilNED.guardarCampo(con, dtoCampo)<=0)
				{
					logger.error("NO INSERTA CAMPO ");
					retorna= false;
				}
			}
		}
		return retorna;
	}

	/**
	 * @param escala
	 * @param loginUsuario
	 * @param arrayCampos
	 */
	private ArrayList<DtoCamposPerfilNed> cargarCamposPerfilNED(DtoEscala escala, String loginUsuario) 
	{
		ArrayList<DtoCamposPerfilNed> arrayCampos= new ArrayList<DtoCamposPerfilNed>();
		for(DtoSeccionParametrizable seccion:escala.getSecciones())
		{
			if(seccion.isVisible())
			{
				//logger.info("numero campos de la seccion "+seccion.getDescripcion()+">> "+seccion.getCampos().size());
				for(DtoCampoParametrizable campo:seccion.getCampos())
				{	
					if(campo.isMostrar())
					{
						DtoCamposPerfilNed dtoArray= new DtoCamposPerfilNed();
						dtoArray.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
						dtoArray.setEscalaCampoSeccion(Utilidades.convertirADouble(campo.getConsecutivoParametrizacion()));
						
						logger.info("\n\n\n campo.getCodigoPK()--->"+campo.getConsecutivoParametrizacion()+"\n\n");
						
						dtoArray.setObservaciones(campo.getObservaciones());
						if(UtilidadTexto.isEmpty(campo.getValor()))
						{	
							dtoArray.setValor(null);
						}
						else
						{
							dtoArray.setValor(new BigDecimal( Utilidades.convertirADouble(campo.getValor())));
						}
						arrayCampos.add(dtoArray);
					}
				}	
			}
		}
		return arrayCampos;
	}

	
	
	/**
	 * @param escala
	 * @param codigoPaciente
	 * @param loginUsuario
	 * @param institucion
	 * @return
	 */
	private DtoPerfilNed cargarEncabezadoPerilNED(DtoEscala escala,
			int codigoPaciente, String loginUsuario, int institucion) {
		DtoPerfilNed dto= new DtoPerfilNed();
		dto.setCodigoPaciente(codigoPaciente);
		dto.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
		dto.setDatosfechaUsuarioRegistro(new DtoInfoFechaUsuario(loginUsuario));
		dto.setEscala(Utilidades.convertirADouble(escala.getCodigoPK()));
		dto.setEscalaFactorPrediccion(Utilidades.convertirADouble(escala.getCodigoFactorPrediccion()));
		dto.setInstitucion(institucion);
		dto.setValorTotal(escala.getTotalEscala());
		return dto;
	}

	
	
	
	
	/*
	#############################################################
	#	METODOS: Anexo860, Cambio 1.12 							#			
	#############################################################
	*/
	
	/** 
	 * Anexo 860, Cambio 1.12 -
	 * Crea una nueva autorizacion
	 * 
	 * @author Cristhian Murillo
	 * @param forma
	 * @param usuario
	 * 
	 */
	public DtoAutorizacionConvIngPac construirAutorizacionConvIngPac(IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario)
	{
		DtoAutorizacionConvIngPac autorizacionConvIngPac;
		autorizacionConvIngPac = new DtoAutorizacionConvIngPac();
		
		autorizacionConvIngPac.setFechaModifica(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getFechaModifica());
		autorizacionConvIngPac.setHoraModifica(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getHoraModifica());
		autorizacionConvIngPac.setObservacionesCambio(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getObservacionesCambio());
		autorizacionConvIngPac.setTipoMedioAuto(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getTipoMedioAuto());
		autorizacionConvIngPac.setUsuarios(new UsuariosHome().findById(usuario.getLoginUsuario()));
		return autorizacionConvIngPac;
	}
	
	
	
	/** 
	 * Anexo 860, Cambio 1.12 -
	 * Retorna el resultado de la validacion en base de datos.
	 * Si no se encuentran registros indica que no paso la validacion
	 * 
	 * @author Cristhian Murillo
	 * @param dtoBusquedaPacientesConvOdo
	 * 
	 */
	public boolean validacionEnBaseDatosIngresoPaciente(DtoBusquedaPacientesConvOdo dtoBusquedaPacientesConvOdo)
	{
		boolean pasoValidacion = false;
		
		if(Utilidades.isEmpty(new PacientesConvOdoDelegate().validacionEnBaseDatosIngresoPaciente(dtoBusquedaPacientesConvOdo))){
			pasoValidacion = false;
		}
		else{
			pasoValidacion = true;
		}
		
		return pasoValidacion;
	}
	
	
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * Agrega al dto el nombre del convenio y el numero de contrato correspondiente
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * 
	 */
	public void llenarDetallesConvenioContratoParaMostrar(IngresoPacienteOdontologiaForm forma) 
	{
		for (Convenios conve : forma.getListaConveniosInstitucion()) {
			if(conve.getCodigo() == Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())){
				forma.getDtoSeccionConvenioPaciente().setDescripcionConvenio(conve.getNombre());
			}
		}
		
		for (Contratos contra : forma.getListaContratoConvenio()) {
			if(contra.getCodigo() == Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())){
				forma.getDtoSeccionConvenioPaciente().setNumeroContrato(contra.getNumeroContrato());
			}
		}
	}
	
	
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * Retorna el estado de la seleccion convenio-contrato segun las validaciones requeridas para el convenio
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * @return boolean
	 * 
	 */
	public boolean obtenerEstadoConfirmacionConvenioContrato(IngresoPacienteOdontologiaForm forma) 
	{
		boolean estadoValidacionBd = true;
		boolean estaroAutorizacion = true;
		
		// se verifica si se requieren las validaciones en base de datos
		if(forma.isMostrarValidacionesBd()){
			if(forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().getEstado().equals
					(ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacNoExitoso)){
				 estadoValidacionBd = false;
				 forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().setEstado(ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacNoExitoso);
			}
			else{
				forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().setEstado(ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacExitoso);
			}
		}
		
		// se verifica si se requieren las validaciones de autorizacion
		if(forma.isMostrarAutorizaciones()){
			if(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getEstado().equals
					(ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacNoExitoso)){
				estaroAutorizacion = false;
			}
		}
		
		boolean resultado = false;
		
		if(estadoValidacionBd){
			if(estaroAutorizacion){
				resultado = true;
			}
		}
		
		if(!forma.isMostrarValidacionesBd()){
			if(!forma.isMostrarAutorizaciones()){
				resultado = UtilidadTexto.getBoolean(forma.getDtoSeccionConvenioPaciente().getActivo());
			}
		}
		
		return resultado;
	}
	
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * Retorna un boolean que indica si ya existe un resitro de contenio-convtrato para el paciente
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * @return boolean
	 */
	public boolean existeMismoConvenioContrato(IngresoPacienteOdontologiaForm forma) 
	{
		boolean existeMismoConvenioContrato = false;
		
		String convenioAgregar = forma.getDtoSeccionConvenioPaciente().getCodigoConvenio();
		String contratoAgregar = forma.getDtoSeccionConvenioPaciente().getCodigoContrato();
		
		for (DtoSeccionConvenioPaciente convenioContratoPaciente : forma.getPaciente().getListaDtoSeccionConvenioPaciente()) 
		{
			
			if(convenioAgregar.equals(convenioContratoPaciente.getCodigoConvenio()))
			{
				
				if(contratoAgregar.equals(convenioContratoPaciente.getCodigoContrato()))
				{
					existeMismoConvenioContrato = true;
				}
			}
		}
		return existeMismoConvenioContrato;
	}
	
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * Guarda y asocia al paciente los convenios qué han sido cargados a el
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * @param codigoPacienteAsigndo
	 * @param usuario
	 * 
	 * @return boolean
	 */
	public boolean guardarSeccionConvenios(IngresoPacienteOdontologiaForm forma, int codigoPacienteAsigndo, UsuarioBasico usuario) 
	{
		Pacientes paciente;
		paciente = new Pacientes();
		
		boolean save = true;
		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			
			paciente = new PacientesHome().findById(codigoPacienteAsigndo);
			ArrayList<ConveniosIngresoPaciente> listaConveniosIngresoPaciente = construirConvenios(forma, paciente, usuario);
			
			
			// Se actualizan o guardan los convenio-contrato
			for (ConveniosIngresoPaciente conveniosIngresoPaciente : listaConveniosIngresoPaciente) 
			{
				// Si el ConveniosIngresoPaciente trae asociada una llave primaria valida se debe actualizar
				if(conveniosIngresoPaciente.getCodigoPk() > 0){
					new ConveniosIngresoPacienteHome().merge(conveniosIngresoPaciente);
				}
				else{
					new ConveniosIngresoPacienteHome().persist(conveniosIngresoPaciente);
				}
			}
			
			
			
			// Eliminacion de registros
			//------------------------------------------------------------------------------------------------------------------------------
			// Se eliminan los bonos seleccionados para eliminar (Los quitados de la lista que venian referenciados al convenioIngresoPaciente)
			for (Long codBonoConveniosIngresoPacienteEliminar : forma.getDtoSeccionConvenioPaciente().getListaBonosConveniosIngresoEliminar()) 
			{
				BonosConvIngPac bonosConvIngPacEliminar;
				bonosConvIngPacEliminar = new BonosConvIngPacDelegate().findById(codBonoConveniosIngresoPacienteEliminar);
				new BonosConvIngPacDelegate().delete(bonosConvIngPacEliminar);
			}
			// Se eliminan los convenio-contrato seleccionados para eliminar (Los quitados de la lista que venian referenciados al paciente)
			for (Long codConveniosIngresoPacienteEliminar : forma.getDtoSeccionConvenioPaciente().getListaConveniosIngresoEliminar()) 
			{
				ConveniosIngresoPaciente conveniosIngresoPacienteEliminar;
				conveniosIngresoPacienteEliminar = new  ConveniosIngresoPacienteHome().findById(codConveniosIngresoPacienteEliminar);
				new ConveniosIngresoPacienteHome().delete(conveniosIngresoPacienteEliminar);
			}
			//------------------------------------------------------------------------------------------------------------------------------
			UtilidadTransaccion.getTransaccion().commit();
			
		} catch (Exception e) { 
			UtilidadTransaccion.getTransaccion().rollback();
			save = false; 	
		}
		return save;
	}
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * Construye una lista de los convenios a guardar
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * 
	 * @return ArrayList<ConveniosIngresoPaciente>
	 */
	public ArrayList<ConveniosIngresoPaciente> construirConvenios(IngresoPacienteOdontologiaForm forma, Pacientes paciente, UsuarioBasico usuario) 
	{
		ArrayList<ConveniosIngresoPaciente> listaConveniosIngresoPaciente = new ArrayList<ConveniosIngresoPaciente>();
		Usuarios usuarioModifica 	= new Usuarios();
		usuarioModifica 			= new UsuariosHome().findById(usuario.getLoginUsuario());
		String horaModifica 		= UtilidadFecha.getHoraActual();
		Date fechaModifica 			= UtilidadFecha.getFechaActualTipoBD();
		
		for (DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente : forma.getPaciente().getListaDtoSeccionConvenioPaciente()) 
		{
			// Se crea ConveniosIngresoPaciente y se llena con la información faltante
			ConveniosIngresoPaciente conveniosIngresoPaciente = construirConvenioPaciente(paciente, usuarioModifica, horaModifica, fechaModifica,dtoSeccionConvenioPaciente);
			if(dtoSeccionConvenioPaciente.getCodigoConveniosIngresoPaciente() > 0){
				conveniosIngresoPaciente.setCodigoPk(dtoSeccionConvenioPaciente.getCodigoConveniosIngresoPaciente());
			}
			// -----------------------------------------------------------------------------
			
			// Se construyen solo los que fueron cargados por el usuario, los cargados por defecto no se tienen en cuenta
			//if(conveniosIngresoPaciente.getPorDefecto().equals(ConstantesBD.acronimoNoChar))
			//{
				// Si existen las Validaciones de base de datos con un estado definido se asocian a ConveniosIngresoPaciente para persistirlo
				if(dtoSeccionConvenioPaciente.getValidacionesBdConvIngPac() != null)
				{
					if(!UtilidadTexto.isEmpty(dtoSeccionConvenioPaciente.getValidacionesBdConvIngPac().getEstado()))
					{
						HashSet<ValidacionesBdConvIngPac> setValidacionesBdConvIngPac = new HashSet<ValidacionesBdConvIngPac>();
						dtoSeccionConvenioPaciente.getValidacionesBdConvIngPac().setConveniosIngresoPaciente(conveniosIngresoPaciente);
						dtoSeccionConvenioPaciente.getValidacionesBdConvIngPac().setUsuarios(usuarioModifica);
						setValidacionesBdConvIngPac.add(dtoSeccionConvenioPaciente.getValidacionesBdConvIngPac());
						conveniosIngresoPaciente.setValidacionesBdConvIngPacs(setValidacionesBdConvIngPac);
					} // -----------------------------------------------------------------------------

				}
				
				// Si existen las Autorizaciones con un estado definido se asocian a ConveniosIngresoPaciente para guardarlo
				if(!UtilidadTexto.isEmpty(dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().getEstado()))
				{
					AutorizacionConvIngPac autorizacionConvIngPac;
					autorizacionConvIngPac = new AutorizacionConvIngPac();
					autorizacionConvIngPac = dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac();
					autorizacionConvIngPac.setConveniosIngresoPaciente(conveniosIngresoPaciente);
					autorizacionConvIngPac.setUsuarios(usuarioModifica);
					
					// Si fue un convenio-contrato cargado tiene una llave primaria asignada
					if(dtoSeccionConvenioPaciente.getCodigoConveniosIngresoPaciente() > 0){
						autorizacionConvIngPac.setCodigoPk(dtoSeccionConvenioPaciente.getCodigoConveniosIngresoPaciente());
					}
					
					HashSet<DetAutorizacionConvIngPac> setDetAutorizacionConvIngPac = new HashSet<DetAutorizacionConvIngPac>();
					
					for (DtoAutorizacionConvIngPac dtoAutorizacionConvIngPac : dtoSeccionConvenioPaciente.getListaDtoAutorizacionConvIngPac()) 
					{
						DetAutorizacionConvIngPac detAutorizacionConvIngPacs;
						detAutorizacionConvIngPacs = new DetAutorizacionConvIngPac();
						
						detAutorizacionConvIngPacs.setAutorizacionConvIngPac(autorizacionConvIngPac);
						detAutorizacionConvIngPacs.setObservacionesCambio(dtoAutorizacionConvIngPac.getObservacionesCambio());
						detAutorizacionConvIngPacs.setTipoMedioAuto(dtoAutorizacionConvIngPac.getTipoMedioAuto());
						
						setDetAutorizacionConvIngPac.add(detAutorizacionConvIngPacs);
					}
					
					autorizacionConvIngPac.setDetAutorizacionConvIngPacs(setDetAutorizacionConvIngPac);
					
					HashSet<AutorizacionConvIngPac> setAutorizacionConvIngPac = new HashSet<AutorizacionConvIngPac>();
					setAutorizacionConvIngPac.add(autorizacionConvIngPac);

					conveniosIngresoPaciente.setAutorizacionConvIngPacs(setAutorizacionConvIngPac);
					
				} // -----------------------------------------------------------------------------
				
				// Si existen los Bonos se asocian a ConveniosIngresoPaciente para persistirlo
				if(!Utilidades.isEmpty(dtoSeccionConvenioPaciente.getListaBonosConvIngPac()))
				{
					HashSet<BonosConvIngPac> setBonosConvIngPac = new HashSet<BonosConvIngPac>();
					
					for (BonosConvIngPac bonosConvIngPac : dtoSeccionConvenioPaciente.getListaBonosConvIngPac()) {
						IEmisionBonosServicio emision=AdministracionFabricaServicio.crearEmisionBonosServicio();
						DtoBusquedaEmisionBonos dtoBonos=new DtoBusquedaEmisionBonos();
						dtoBonos.setNumeroSerial(bonosConvIngPac.getNumeroSerial());
						dtoBonos.setCodigoConvenios((int)conveniosIngresoPaciente.getContratos().getConvenios().getCodigo());
						dtoBonos.setCodInstituciones(usuario.getCodigoInstitucionInt());
						EmisionBonosDesc emisionBonos=emision.buscarEmisionXBono(dtoBonos);
						
						bonosConvIngPac.setConveniosIngresoPaciente(conveniosIngresoPaciente);
						bonosConvIngPac.setFechaModifica(fechaModifica);
						bonosConvIngPac.setHoraModifica(horaModifica);
						bonosConvIngPac.setUsuarios(usuarioModifica);
						bonosConvIngPac.setEmisionBonosDesc(emisionBonos);
					}
					
					setBonosConvIngPac.addAll(dtoSeccionConvenioPaciente.getListaBonosConvIngPac());
					conveniosIngresoPaciente.setBonosConvIngPacs(setBonosConvIngPac);
				} // -----------------------------------------------------------------------------
				
			//}
			
			listaConveniosIngresoPaciente.add(conveniosIngresoPaciente);
		}
		
		return listaConveniosIngresoPaciente;
	}

	/**
	 * Construir el objeto convenio paciente según los parámetros enviados
	 * @param paciente
	 * @param usuarioModifica
	 * @param horaModifica
	 * @param fechaModifica
	 * @param dtoSeccionConvenioPaciente
	 * @return
	 */
	public ConveniosIngresoPaciente construirConvenioPaciente(
			Pacientes paciente, Usuarios usuarioModifica, String horaModifica,
			Date fechaModifica,
			DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente)
	{
		ConveniosIngresoPaciente conveniosIngresoPaciente;
		conveniosIngresoPaciente = new ConveniosIngresoPaciente();
		conveniosIngresoPaciente.setPacientes(paciente);
		conveniosIngresoPaciente.setHoraModifica(horaModifica);
		conveniosIngresoPaciente.setFechaModifica(fechaModifica);
		conveniosIngresoPaciente.setActivo(dtoSeccionConvenioPaciente.getActivo().charAt(0));
		conveniosIngresoPaciente.setPorDefecto(dtoSeccionConvenioPaciente.getPorDefecto());
		conveniosIngresoPaciente.setContratos(new ContratosHome().findById(Integer.parseInt(dtoSeccionConvenioPaciente.getCodigoContrato())));
		conveniosIngresoPaciente.setUsuarios(usuarioModifica);
		return conveniosIngresoPaciente;
	}
	
	
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * valida el numero serial del bono ingresado
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param dtoBusquedaEmisionBonos
	 * 
	 * @return DtoResultado
	 */
	public DtoResultado validarNumeroSerialBono(DtoBusquedaEmisionBonos dtoBusquedaEmisionBonos)
	{
		DtoResultado dtoResultado = new DtoResultado();
		MessageResources fuenteMensaje=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.IngresoPacienteOdontologiaForm");
		String mensajeConcreto = "";
		
		// se valida que exista para el convenio y este dentro de los rangos validos
		EmisionBonosDesc emisionBonosDesc;
		dtoBusquedaEmisionBonos.setFecha(null); // Se envia nula para qué no se tenga en cuenta y consultar por rango de seriales
		emisionBonosDesc = new EmisionBonosDescDelegate().buscarSerialBono(dtoBusquedaEmisionBonos);
		
		// Si es null es porque no encontro coincidencia del convenio con el rango de seriales
		if(emisionBonosDesc == null)
		{
			mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.SerialNoRegistradoEmision");
			dtoResultado.getErrores().add("error operacion_no_realizada", new ActionMessage("error.odontologia.SerialNoValido",mensajeConcreto));
		}
		else
		{
			// se valida si es vigente -  Se busca el bono, en este caso se tienen en cuenta las fechas de vigencia
			dtoBusquedaEmisionBonos.setFecha(UtilidadFecha.getFechaActualTipoBD());	// Se agrega la fecha apra que se tenga en cuenta
			emisionBonosDesc = new EmisionBonosDescDelegate().buscarSerialBono(dtoBusquedaEmisionBonos);
		
			if(emisionBonosDesc == null)
			{
				mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.serialNoVigente");
				dtoResultado.getErrores().add("error operacion_no_realizada", new ActionMessage("error.odontologia.SerialNoValido",mensajeConcreto));
			}
			else
			{
				
				// se verifica que no este asociado a ningun paciente-contrato. Si encuentra información indica qué ya esta asociado
				ArrayList<BonosConvIngPac> listaBonosConvIngPac = new BonosConvIngPacDelegate().buscarBonosConvIngPac(dtoBusquedaEmisionBonos);
				
				if(!Utilidades.isEmpty(listaBonosConvIngPac))
				{
					String pacienteAsignado = listaBonosConvIngPac.get(0).getConveniosIngresoPaciente().getPacientes().getPersonas().getPrimerNombre();
					pacienteAsignado		+= " "+listaBonosConvIngPac.get(0).getConveniosIngresoPaciente().getPacientes().getPersonas().getPrimerApellido();
					
					mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.serialYaRegistradoPaciente",pacienteAsignado);
					dtoResultado.getErrores().add("error operacion_no_realizada", new ActionMessage("error.odontologia.SerialNoValido",mensajeConcreto));
				}
				else {
					
					// Se toma el valor del bono
					if(emisionBonosDesc.getValorDescuento() != null)
					{
						dtoResultado.setTipoPk(ConstantesIntegridadDominio.acronimoTipoValorBonoValor);
						dtoResultado.setPk(emisionBonosDesc.getValorDescuento().toString());
					}
					else if (emisionBonosDesc.getPorcentajeDescuentos() != null)
					{
						dtoResultado.setTipoPk(ConstantesIntegridadDominio.acronimoTipoValorBonoDescuento);
						dtoResultado.setPk(emisionBonosDesc.getPorcentajeDescuentos().toString());
					}
				}
				
			}
		}
		
		return dtoResultado;
	}
	
	
	
	
	/**
	 * Anexo 860, Cambio 1.12 -
	 * Carga los convenios parametrizados por defecto
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * 
	 */
	public void cargarConveniosParametrizadosPorDefecto(IngresoPacienteOdontologiaForm forma)
	{
		ArrayList<HashMap<String, Object>> arrayConveniosParametrizadosPorDefecto = ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();	
		
		// Se verifica qué no hay cargados convenios por defecto
		if(forma.getPaciente().getListaDtoSeccionConvenioPaciente().size() < arrayConveniosParametrizadosPorDefecto.size())
		{
			//forma.getPaciente().setListaDtoSeccionConvenioPaciente(new ArrayList<DtoSeccionConvenioPaciente>());
		
			for (HashMap<String, Object> hashMapConvenio : arrayConveniosParametrizadosPorDefecto) 
			{
				
				// Solo muestra los convenios por defecto: TipoTarjetaCliente = NO
				if(hashMapConvenio.get("esConvenioTarjCliente").toString().equals(ConstantesBD.acronimoNo))
				{
					boolean existeEnPaciente=false;
					for(DtoSeccionConvenioPaciente convenioPaciente:forma.getPaciente().getListaDtoSeccionConvenioPaciente())
					{
						if(Utilidades.convertirAEntero(hashMapConvenio.get("codigoConvenio")+"")==convenioPaciente.getCodigoConvenioInt())
						{
							existeEnPaciente=true;
							break;
						}
					}
					if(!existeEnPaciente)
					{

						DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente;
						dtoSeccionConvenioPaciente = new DtoSeccionConvenioPaciente();
						
						dtoSeccionConvenioPaciente.setActivo(ConstantesBD.acronimoSi);
						dtoSeccionConvenioPaciente.setPorDefecto(ConstantesBD.acronimoSiChar);
						dtoSeccionConvenioPaciente.setCodigoContrato(hashMapConvenio.get("codigoContrato").toString());
						dtoSeccionConvenioPaciente.setNumeroContrato(hashMapConvenio.get("numeroContrato").toString());
						dtoSeccionConvenioPaciente.setCodigoConvenio(hashMapConvenio.get("codigoConvenio").toString());
						dtoSeccionConvenioPaciente.setDescripcionConvenio(hashMapConvenio.get("descripcionConvenio").toString());
						dtoSeccionConvenioPaciente.setEsEliminable(false);
						dtoSeccionConvenioPaciente.setEsModificable(true);
						
						forma.getPaciente().getListaDtoSeccionConvenioPaciente().add(dtoSeccionConvenioPaciente);
					}
				}
				
			}
		}
		
	}
	
	
}
