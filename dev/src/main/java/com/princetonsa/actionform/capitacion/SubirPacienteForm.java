/**
 * Princeton S.A.
 */
package com.princetonsa.actionform.capitacion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOSubirPacientesInconsistencias;
import com.princetonsa.dto.capitacion.DtoInconsistenciasArchivoPlano;
import com.princetonsa.dto.capitacion.DtoVerificacionArchivo;
import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.orm.Barrios;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Localidades;

/**
 *
 */
public class SubirPacienteForm extends ValidatorForm
{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejador de logs d la clase
	 */
	private transient Logger logger=Logger.getLogger(SubirPacienteForm.class);
	
	/**
	 * Manejo del flujo
	 */
	private String estado;
	
	/**
	 * Listado de convenios para seleccionar
	 */
	private Collection listadoConvenios;
	
	/**
	 * Listado de los listadoContratos para el resumen
	 */
	private Collection listadoContratos;
	
	/**
	 *  Listado de los listadoClasificacionSE para el resumen
	 */
	private Collection listadoClasificacionSE;
	
	/**
	 *  Listado de los listadoTiposAfiliado para el resumen
	 */
	private Collection listadoTiposAfiliado;
	
	
	/**
	 * Lista que maneja las inconsistencias del archivo
	 */
	private ArrayList<DtoVerificacionArchivo> inconsistenciasArchivo = new ArrayList<DtoVerificacionArchivo>();
	
	
	/**
	 * Manejo del convenio
	 */
	private int convenio;
	
	/**
	 * Contrato para seleccioar en flujo individual
	 */
	private int contrato;
	
	/**
	 * Codigo de la persona cuando existe en la tabla persona
	 */
	private int codigoPersona;
	
	/**
	 * Codigo del usuario capitado cuando existe en la tabla usuarios_capitados
	 */
	private long codigoUsuarioCapitado;

	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * Archivo
	 */
	private transient FormFile archivo;
	
	/**
	 * Manejo de datos de resumen
	 */
	private HashMap resumenSubirPacientes;
	
	/**
	 * Tipo identificación del usuario capitado
	 */
	private String codigoTipoId;
	
	/**
	 * Nombre del tipo de identificación
	 */
	private String nombreTipoId;

	/**
	 * Número identificación del usuario capitado
	 */
	private String numId;
	
	/**
	 * Manejo de listado de tipos id
	 */
	private Collection listadoTiposId;
	
	/**
	 * Primer Apellido Persona
	 */
	private String primerApellidoPersona;
	
	/**
	 * Segundo Apellido Persona
	 */
	private String segundoApellidoPersona;

	/**
	 * Primer Nombre Persona
	 */
	private String primerNombrePersona;
	
	/**
	 * Segundo Nombre Persona
	 */
	private String segundoNombrePersona;
	
	/**
	 * Fecha de Nacimiento
	 */
	private String fechaNacimiento;
	
	/**
	 * sexo
	 */
	private int sexo;
	
	private String nombreSexo;
	
	/**
	 * Direccion
	 */
	private String direccion;
	
	/**
	 * Telefono
	 */
	private String telefono;
	
	/**
	 * Email
	 */
	private String email;
	
	private ArrayList<DTONaturalezaPaciente> listadoNaturalezaPacientes = new ArrayList<DTONaturalezaPaciente>();
	
	private int codigoNaturaleza;
	
	/**
	 * Liostar las opciones de sexo
	 */
	private Collection listadoSexos;
	
	private boolean existeEnPersona;
	
	private String nombreArchivo;
	
	private String inconsistenciasEncontradas;
	
	/**
	 * 
	 */
	private String clasificacionSE;
	
	/**
	 * 
	 */
	private String numeroFicha;
	
	/********* CAMPOS AGREGADOS POR ANEXO 922*****************/
	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
	private String codigoPaisId;
	private String codigoPaisResidencia;
	private ArrayList<HashMap<String, Object>> ciudades = new ArrayList<HashMap<String,Object>>();
	private String codigoCiudadResidencia;
	private String codigoBarrio;
	private String nombreBarrio;
	private ArrayList<HashMap<String, Object>> localidades = new ArrayList<HashMap<String,Object>>();
	private String criterioBarrio;
	private String codigoLocalidad;
	private String nombreLocalidad;
	private String codigoTipoAfiliado;
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	private int consecutivoCentroAtencionSeleccionado;
	private String acronimoTipoIdEmpleador;
	ArrayList<HashMap<String, Object>> tiposIDEmpleador= new ArrayList<HashMap<String,Object>>();
	private String numIDEmpleador;
	private String razonSocial;
	ArrayList<HashMap<String, Object>> tiposIDCotizante= new ArrayList<HashMap<String,Object>>();
	private String numIDCotizante;
	private String acronimoTipoIdCotizante;
	private String nombresCotizante;
	private String apellidosCotizante;
	private Collection listadoTiposParentezco;
	private String parentescoCotizante;
	private String paisResidencia;
	private boolean mostrarPopUpIndividual;
	private int posicionListaFinalPersonas;
	private int posicionListaPersonasBD;
	private String departamentoResidencia;
	private String municipioResidencia;
	
	/**
	 * Lista las Personas del archivo plano con la lista interna de las 
	 * personas de la BD con Inconsistencias.
	 */
	private ArrayList<DTOSubirPacientesInconsistencias> listaFinalPersonasInconsistencias;
	
	private ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciaPersonaBD;
	private int sizelistaFinalPersonasInconsistencias;
	private ArrayList<String> listaPersonasCargue;
	private ArrayList<String> personaAPlanoChecked;
	private ArrayList<String> personaBDatosChecked;
	private boolean cobfirmar; 
	
//	private ArrayList<Paises> paisResidencia;
	//private ArrayList<Departamentos> departamentoResidencia;
	private String codigoDepartamentoResidencia;
	
	private ArrayList<Localidades> localidadResidencia;
	private ArrayList<Barrios> barrioResidencia;
	private String tipoIDEmpleador;	
	private String excepcionMonto;
	private String tipoAfiliado;
	private ArrayList<CentroAtencion> centroAtencion;
	private String nombreCentroAtencion;
	
	private boolean noExistenPersonasIncon;
	
	/**++++++++++++++Se valida por paciente individual+++++++++++++++++++++++*/
	private boolean comparaNomApeIndividual;
	private DtoPersonas personaInconIndividual;
	/**Se valida si se selecciono respuesta positiva para guardar en log las inconsistencias de las personas */
	private boolean respuestaPositivaGuardarLog;
	private boolean seGuardoExitosamente;
	
	/********************filtro mostrar seccion datos cotizante***********************/
	private boolean verSeccionCotizante;
	private boolean existeUsuarioPeriodo;
	
	private int totalLeidos=0;
	
	private int totalGrabados=0;
	
	private int totalInconsistencias=0;
	
	/**
	 * Objeto implementado para mostrar las personas
	 * que tienen el mismo numero id pero diferente tipo identificacion
	 * cuando se ingresa un paciente
	 */
	private HashMap mapaNumerosId = new HashMap();
	
	/**
	 * Hacer requerida la selección del Centro de Atención Asignado en Subir Paciente Individual
	 */
	private boolean haceRequeridoCentroAtencion;
	
	/**
	 * Reset de la clase
	 */
	public void reset(int institucion)
	{
		this.listadoConvenios=null;
		this.listadoContratos=null;
		this.listadoClasificacionSE=null;
		this.convenio=ConstantesBD.codigoNuncaValido;
		this.contrato=ConstantesBD.codigoNuncaValido;
		String fechaActual=UtilidadFecha.getFechaActual();
		String[] componentes=fechaActual.split("/");
		this.archivo=null;
		this.resumenSubirPacientes=null;
		this.inconsistenciasEncontradas = "";
		this.mapaNumerosId = new HashMap();
		
		// Datos paciente
		this.codigoTipoId=ValoresPorDefecto.getTipoId(institucion);
		this.nombreTipoId="";
		this.numId="";
		this.listadoTiposId=null;
		this.listadoSexos=null;
		this.existeEnPersona=false;
		
		
		
		this.primerApellidoPersona="";
		this.segundoApellidoPersona="";
		this.primerNombrePersona="";
		this.segundoNombrePersona="";
		this.fechaNacimiento="";
		this.nombreArchivo="";
		this.sexo=0;
		this.nombreSexo="";
		this.direccion=ValoresPorDefecto.getDireccionPaciente(institucion);
		this.email="";
		this.telefono="";
		
		this.clasificacionSE="";
		this.numeroFicha="";
		
		this.fechaInicial=UtilidadFecha.getFechaActual();
		this.fechaFinal=asignarUltimoDia(this.fechaInicial);
		
		this.listaFinalPersonasInconsistencias 	=new ArrayList<DTOSubirPacientesInconsistencias>();
		this.listaInconsistenciaPersonaBD		= new ArrayList<DtoInconsistenciasArchivoPlano>();
		this.listaPersonasCargue				=new ArrayList<String>();		
	
		this.personaAPlanoChecked=new ArrayList<String>();
		this.personaBDatosChecked=new ArrayList<String>();
		this.cobfirmar=false;
		
		//this.paisResidencia=new ArrayList<Paises>();
		//this.departamentoResidencia=new ArrayList<Departamentos>();
		this.municipioResidencia="";
		this.localidadResidencia=new ArrayList<Localidades>();
		this.barrioResidencia=new ArrayList<Barrios>();
		this.tipoIDEmpleador="";
		this.numIDEmpleador="";
		this.razonSocial="";
		this.acronimoTipoIdCotizante="";
		this.numIDCotizante="";
		this.nombresCotizante="";
		this.apellidosCotizante="";
		this.parentescoCotizante="";
		this.excepcionMonto="";
		this.tipoAfiliado="";	
		this.codigoTipoAfiliado="";
		this.centroAtencion=new ArrayList<CentroAtencion>();
		this.codigoDepartamentoResidencia="";//no se esta capturando
		
		/*****CAMPOS AGREGADOS 922********/
		this.paises = new ArrayList<HashMap<String,Object>>();
		this.codigoPaisId = "";
		this.codigoPaisResidencia = "";
		this.ciudades = new ArrayList<HashMap<String,Object>>();
		this.setCodigoCiudadResidencia("");
		this.setCodigoBarrio("");
		this.setNombreBarrio("");
		this.localidades = new ArrayList<HashMap<String,Object>>();
		this.criterioBarrio = "";
		this.codigoLocalidad = "";
		this.nombreLocalidad = "";
		this.listadoTiposAfiliado = null;
		this.tiposIDEmpleador= new ArrayList<HashMap<String,Object>>();
		this.tiposIDCotizante= new ArrayList<HashMap<String,Object>>();
		this.listadoTiposParentezco=null;
		this.posicionListaPersonasBD=ConstantesBD.codigoNuncaValido;
		this.posicionListaFinalPersonas=ConstantesBD.codigoNuncaValido;
		this.mostrarPopUpIndividual=false;
		this.departamentoResidencia="";
		this.noExistenPersonasIncon=false;
		this.centrosAtencion=new ArrayList<DtoCentrosAtencion>();
		this.nombreCentroAtencion="";
		
		/**++++++++++++++Se valida por paciente individual+++++++++++++++++++++++*/		
		this.personaInconIndividual=new DtoPersonas();
		this.comparaNomApeIndividual=false;
		
		this.respuestaPositivaGuardarLog=false;
		this.seGuardoExitosamente=false;
		
		this.verSeccionCotizante=false;
		
		this.existeUsuarioPeriodo=false;
		
		this.listadoNaturalezaPacientes= new ArrayList<DTONaturalezaPaciente>();
		
		this.codigoNaturaleza=ConstantesBD.codigoNuncaValido;
		this.codigoPersona=ConstantesBD.codigoNuncaValido;
		this.codigoUsuarioCapitado=ConstantesBD.codigoNuncaValidoLong;
		this.totalLeidos=0;
		this.totalGrabados=0;
		this.totalInconsistencias=0;
		this.haceRequeridoCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(institucion));
	}
	
	public void resetDatoscotizante()
	{
		this.acronimoTipoIdCotizante="";
		this.numIDCotizante="";
		this.nombresCotizante="";
		this.apellidosCotizante="";
		this.parentescoCotizante="";
	}
	
	/**
	 * 
	 * @param fecha
	 * @return
	 */
	private String asignarUltimoDia(String fecha) 
	{
		int mes=Utilidades.convertirAEntero(fecha.split("/")[1]);
		if(mes==1||mes==3||mes==5||mes==7||mes==8||mes==10||mes==12)
		{
			return "31/"+fecha.split("/")[1]+"/"+fecha.split("/")[2];
		}
		else if(mes==2)
		{
			return "28/"+fecha.split("/")[1]+"/"+fecha.split("/")[2];
		}
		else
		{
			return "30/"+fecha.split("/")[1]+"/"+fecha.split("/")[2];
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equalsIgnoreCase("accionVerificarIdentificacion"))
			{
			if(numId.trim().equals(""))
			{
				errores.add("numId requerido", new ActionMessage("errors.required", "Número de identificación"));
			}
			if(codigoTipoId.trim().equals(""))
			{
				errores.add("tipoId requerido", new ActionMessage("errors.required", "Tipo de identificación"));
			}
		}
		if(estado.equalsIgnoreCase("guardarIndividual"))
		{
			if(haceRequeridoCentroAtencion)
			{
				if(consecutivoCentroAtencionSeleccionado<=0)
				{
					errores.add("centro atencion requerido", new ActionMessage("errors.required", "Centro de atención"));
				}
			}
			if(convenio==ConstantesBD.codigoNuncaValido)
			{
				errores.add("convenio requerido", new ActionMessage("errors.required", "Convenio"));
			}
			if(contrato==ConstantesBD.codigoNuncaValido)
			{
				errores.add("contrato requerido", new ActionMessage("errors.required", "Contrato"));
			}
			
			//validacion fecha
			validarFechas(errores);
			
			if(numId.trim().equals(""))
			{
				errores.add("numId requerido", new ActionMessage("errors.required", "Número de identificación"));
			}
			if(codigoTipoId.trim().equals(""))
			{
				errores.add("tipoId requerido", new ActionMessage("errors.required", "Tipo de identificación"));
			}
			if(!UtilidadCadena.noEsVacio(primerApellidoPersona))
			{
				errores.add("primerApellidoPersona requerido", new ActionMessage("errors.required", "Primer Apellido Persona"));
			}
			if(!UtilidadCadena.noEsVacio(primerNombrePersona))
			{
				errores.add("primerNombrePersona requerido", new ActionMessage("errors.required", "Primer Nombre Persona"));
			}
			if(!UtilidadCadena.noEsVacio(fechaNacimiento))
			{
				errores.add("fechaNacimiento requerido", new ActionMessage("errors.required", "Fecha de Nacimiento"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(fechaNacimiento))
				{
					errores.add("Fecha invalida", new ActionMessage("errors.formatoFechaInvalido", " de Nacimiento"));					
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaNacimiento,UtilidadFecha.getFechaActual()))
				{
					errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Nacimiento ("+this.fechaNacimiento+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
				}
				else
				{
					if(UtilidadFecha.calcularEdad(fechaNacimiento)>130)
					{
						errores.add("Edad > 130", new ActionMessage("error.subirPacientes.edadMayorLimite", 130));					
					}
				}
			}
			if(sexo==0)
			{
				errores.add("sexo requerido", new ActionMessage("errors.required", "Sexo"));
			}
			if(!UtilidadCadena.noEsVacio(direccion))
			{
				errores.add("direccion requerido", new ActionMessage("errors.required", "Dirección"));
			}
			if(UtilidadCadena.noEsVacio(email))
			{
				email=email.trim();
				if(email.contains(" "))
				{
					errores.add("errors.email", new ActionMessage("errors.email", email));
				}
				else
				{
					String[] componentesEmail=email.split("@");
					if(componentesEmail.length<2 || componentesEmail[1].indexOf(".")==-1 || componentesEmail[1].indexOf(".")==componentesEmail[1].length()-1)
					{
						errores.add("errors.email", new ActionMessage("errors.email", email));
					}
				}
			}
			if(!this.numeroFicha.trim().equals(""))
			{
				if(this.numeroFicha.length()>15)
				{
					errores.add("errors.email", new ActionMessage("errors.maxlength", "Número Ficha","15"));
				}
			}
			
			if(!((UtilidadTexto.isEmpty(this.acronimoTipoIdEmpleador)&&UtilidadTexto.isEmpty(this.numIDEmpleador)&&UtilidadTexto.isEmpty(this.razonSocial))
					||(!UtilidadTexto.isEmpty(this.acronimoTipoIdEmpleador)&&!UtilidadTexto.isEmpty(this.numIDEmpleador)&&!UtilidadTexto.isEmpty(this.razonSocial)))){
				
				if(UtilidadTexto.isEmpty(this.acronimoTipoIdEmpleador))
					errores.add("tipo id empleador requerido", new ActionMessage("errors.required", "Tipo ID Empleador"));
				if(UtilidadTexto.isEmpty(this.numIDEmpleador))
					errores.add("numero id empleador requerido", new ActionMessage("errors.required", "Numero ID Empleador"));
				if(UtilidadTexto.isEmpty(this.razonSocial))
					errores.add("razon social empleador requerido", new ActionMessage("errors.required", "Razón Social Empleador"));
			}
			
			if(!((UtilidadTexto.isEmpty(this.acronimoTipoIdCotizante)&&UtilidadTexto.isEmpty(this.numIDCotizante)&&UtilidadTexto.isEmpty(this.nombresCotizante)&&UtilidadTexto.isEmpty(this.apellidosCotizante)&&UtilidadTexto.isEmpty(this.parentescoCotizante))
					||(!UtilidadTexto.isEmpty(this.acronimoTipoIdCotizante)&&!UtilidadTexto.isEmpty(this.numIDCotizante)&&!UtilidadTexto.isEmpty(this.nombresCotizante)&&!UtilidadTexto.isEmpty(this.apellidosCotizante)&&!UtilidadTexto.isEmpty(this.parentescoCotizante)))){
				
				if(UtilidadTexto.isEmpty(this.acronimoTipoIdCotizante))
					errores.add("tipo id cotizante requerido", new ActionMessage("errors.required", "Tipo ID Cotizante"));
				if(UtilidadTexto.isEmpty(this.numIDCotizante))
					errores.add("numero id cotizante requerido", new ActionMessage("errors.required", "Numero ID Cotizante"));
				if(UtilidadTexto.isEmpty(this.nombresCotizante))
					errores.add("nombres cotizante requerido", new ActionMessage("errors.required", "Nombres Cotizante"));
				if(UtilidadTexto.isEmpty(this.apellidosCotizante))
					errores.add("apellidos cotizante requerido", new ActionMessage("errors.required", "Apellidos Cotizante"));
				if(UtilidadTexto.isEmpty(this.parentescoCotizante))
					errores.add("parentesco requerido", new ActionMessage("errors.required", "Parentesco del Usuario con el Cotizante"));
				
			}
			
			if(UtilidadTexto.isEmpty(codigoPaisResidencia)){
				errores.add("pais requerido", new ActionMessage("errors.required", "País"));
			}
			
			if(UtilidadTexto.isEmpty(codigoCiudadResidencia)){
				errores.add("ciudad requerido", new ActionMessage("errors.required", "Ciudad"));
			}
			
			if(UtilidadTexto.isEmpty(codigoBarrio)){
				errores.add("barrio requerido", new ActionMessage("errors.required", "Barrio"));
			}
			/*if(clasificacionSE.trim().equals(""))
			{
				errores.add("convenio requerido", new ActionMessage("errors.required", "Clasificacion SocioEconomica"));
			}*/
			
			if(!errores.isEmpty())
			{
				this.setEstado("continuar");
			}
			
		}
		if(estado.equalsIgnoreCase("verificarMasivo"))
		{
			if (FileUpload.isMultipartContent(request))
			{
				try
				{
					if(archivo==null)
					{
						errores.add("archivo requerido", new ActionMessage("errors.required", "Archivo"));
					}
					else
					{
						if(!UtilidadCadena.noEsVacio(archivo.getFileName()))
						{
							errores.add("archivo requerido", new ActionMessage("errors.required", "Archivo"));
						}
						else
						{
							//Se debe validar que el Nombre del Archivo contenga una extensión
							if(archivo.getFileName().contains("."))
							{
								//Se debe validar que el archivo adjunto tenga extensión "CVS". Tarea 65221
								String nombreArchivo=(archivo.getFileName()+"");
								if(nombreArchivo.indexOf(".")>0)
								{
									 //nombreArchivo.split(".");
									String nombreArchivo1=  nombreArchivo.substring(0, nombreArchivo.indexOf("."));
									String extensionArchivo1= nombreArchivo.substring(nombreArchivo.indexOf("."), nombreArchivo.length());
									if(!extensionArchivo1.equals(".csv") && !extensionArchivo1.equals(".CSV"))
										errores.add("extension desconocida", new ActionMessage("error.subirPacientes.extensionNoValida", archivo.getFileName()));
								}
								else
									errores.add("extension desconocida", new ActionMessage("error.subirPacientes.sinExtension", archivo.getFileName()));
							}
							else
								errores.add("extension desconocida", new ActionMessage("error.subirPacientes.sinExtension", archivo.getFileName()));
							
							String cadenaArchivo=new String(archivo.getFileData());
							if(!UtilidadCadena.noEsVacio(cadenaArchivo))
								errores.add("archivo vacío", new ActionMessage("error.subirPacientes.archivoVacio", archivo.getFileName()));
						}
					}
				}
				catch (FileNotFoundException e)
				{
					logger.error("Error leyendo el archivo "+e);
					return null;
				}
				catch (IOException e)
				{
					logger.error("Error leyendo el archivo "+e);
					return null;
				}
			}
			if(convenio==ConstantesBD.codigoNuncaValido)
			{
				errores.add("convenio requerido", new ActionMessage("errors.required", "Convenio"));
			}
			if(contrato==ConstantesBD.codigoNuncaValido)
			{
				errores.add("contrato requerido", new ActionMessage("errors.required", "Contrato"));
			}
			
			//validacion fecha
			validarFechas(errores);

			
		}
		
		return errores;
	}

	
	private void validarFechas(ActionErrors errores) 
	{
		/**
		 * validaciones de fechas
		 */
		if(!UtilidadCadena.noEsVacio(this.fechaInicial))
		{
			errores.add("fechaIniciala requerido", new ActionMessage("errors.required", "Fecha Inicial"));
		}
		else if(!UtilidadCadena.noEsVacio(this.fechaFinal))
		{
			errores.add("fechafinal requerido", new ActionMessage("errors.required", "Fecha Final"));
		}
		else
		{
			if(!UtilidadFecha.validarFecha(fechaInicial))
			{
				errores.add("Fecha invalida", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));					
			}
			else if(!UtilidadFecha.validarFecha(fechaFinal))
			{
				errores.add("Fecha invalida", new ActionMessage("errors.formatoFechaInvalido", " Final"));					
			}
			else
			{
				//////fechas validas
				/*
				 * no hacer validacion con la fecha actual. tarea 9991 con modificacion en documento.
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
				{
					errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Final ("+this.fechaFinal+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
				}
				*/
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,fechaInicial))
				{
					errores.add("Fecha Mayor Inicial",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Final ("+this.fechaFinal+")","Inicial ("+this.fechaInicial+")"));
				}

			}
		}
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna listadoConvenios.
	 */
	public Collection getListadoConvenios()
	{
		return listadoConvenios;
	}

	/**
	 * @param listadoConvenios Asigna listadoConvenios.
	 */
	public void setListadoConvenios(Collection listadoConvenios)
	{
		this.listadoConvenios = listadoConvenios;
	}

	
	/**
	 * @return Retorna convenio.
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * @param convenio Asigna convenio.
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @return Retorna archivo.
	 */
	public FormFile getArchivo()
	{
		return archivo;
	}

	/**
	 * @param archivo Asigna archivo.
	 */
	public void setArchivo(FormFile archivo)
	{
		this.archivo = archivo;
	}

	/**
	 * @return Retorna resumenSubirPacientes.
	 */
	public HashMap getResumenSubirPacientes()
	{
		return resumenSubirPacientes;
	}

	/**
	 * @param resumenSubirPacientes Asigna resumenSubirPacientes.
	 */
	public void setResumenSubirPacientes(HashMap resumenSubirPacientes)
	{
		this.resumenSubirPacientes = resumenSubirPacientes;
	}

	/**
	 * @return Retorna listadoContratos.
	 */
	public Collection getListadoContratos()
	{
		return listadoContratos;
	}

	/**
	 * @param listadoContratos Asigna listadoContratos.
	 */
	public void setListadoContratos(Collection contratos)
	{
		this.listadoContratos = contratos;
	}

	/**
	 * @return Retorna contrato.
	 */
	public int getContrato()
	{
		return contrato;
	}

	/**
	 * @param contrato Asigna contrato.
	 */
	public void setContrato(int contrato)
	{
		this.contrato = contrato;
	}

	/**
	 * @return Retorna numId.
	 */
	public String getNumId()
	{
		return numId;
	}

	/**
	 * @param numId Asigna numId.
	 */
	public void setNumId(String numId)
	{
		this.numId = numId;
	}

	/**
	 * @return Retorna listadoTiposId.
	 */
	public Collection getListadoTiposId()
	{
		return listadoTiposId;
	}

	/**
	 * @param listadoTiposId Asigna listadoTiposId.
	 */
	public void setListadoTiposId(Collection listadoTiposId)
	{
		this.listadoTiposId = listadoTiposId;
	}

	/**
	 * @return Retorna primerApellidoPersona.
	 */
	public String getPrimerApellidoPersona()
	{
		return primerApellidoPersona;
	}

	/**
	 * @param primerApellidoPersona Asigna primerApellidoPersona.
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona)
	{
		this.primerApellidoPersona = primerApellidoPersona;
	}

	/**
	 * @return Retorna primerNombrePersona.
	 */
	public String getPrimerNombrePersona()
	{
		return primerNombrePersona;
	}

	/**
	 * @param primerNombrePersona Asigna primerNombrePersona.
	 */
	public void setPrimerNombrePersona(String primerNombrePersona)
	{
		this.primerNombrePersona = primerNombrePersona;
	}

	/**
	 * @return Retorna segundoApellidoPersona.
	 */
	public String getSegundoApellidoPersona()
	{
		return segundoApellidoPersona;
	}

	/**
	 * @param segundoApellidoPersona Asigna segundoApellidoPersona.
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona)
	{
		this.segundoApellidoPersona = segundoApellidoPersona;
	}

	/**
	 * @return Retorna segundoNombrePersona.
	 */
	public String getSegundoNombrePersona()
	{
		return segundoNombrePersona;
	}

	/**
	 * @param segundoNombrePersona Asigna segundoNombrePersona.
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona)
	{
		this.segundoNombrePersona = segundoNombrePersona;
	}

	/**
	 * @return Retorna fechaNacimiento.
	 */
	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento Asigna fechaNacimiento.
	 */
	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return Retorna sexo.
	 */
	public int getSexo()
	{
		return sexo;
	}

	/**
	 * @param sexo Asigna sexo.
	 */
	public void setSexo(int sexo)
	{
		this.sexo = sexo;
	}

	/**
	 * @return Retorna direccion.
	 */
	public String getDireccion()
	{
		return direccion;
	}

	/**
	 * @param direccion Asigna direccion.
	 */
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}

	/**
	 * @return Retorna email.
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email Asigna email.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return Retorna telefono.
	 */
	public String getTelefono()
	{
		return telefono;
	}

	/**
	 * @param telefono Asigna telefono.
	 */
	public void setTelefono(String telefono)
	{
		this.telefono = telefono;
	}

	/**
	 * @return Retorna listadoSexos.
	 */
	public Collection getListadoSexos()
	{
		return listadoSexos;
	}

	/**
	 * @param listadoSexos Asigna listadoSexos.
	 */
	public void setListadoSexos(Collection listadoSexos)
	{
		this.listadoSexos = listadoSexos;
	}

	/**
	 * @return Returns the nombreTipoId.
	 */
	public String getNombreTipoId() 
	{
		return nombreTipoId;
	}

	/**
	 * @param nombreTipoId The nombreTipoId to set.
	 */
	public void setNombreTipoId(String nombreTipoId) 
	{
		this.nombreTipoId = nombreTipoId;
	}

	/**
	 * @return Returns the codigoTipoId.
	 */
	public String getCodigoTipoId() 
	{
		return codigoTipoId;
	}

	/**
	 * @param codigoTipoId The codigoTipoId to set.
	 */
	public void setCodigoTipoId(String codigoTipoId) 
	{
		this.codigoTipoId = codigoTipoId;
	}

	/**
	 * @return Returns the existeEnPersona.
	 */
	public boolean getExisteEnPersona() 
	{
		return existeEnPersona;
	}

	/**
	 * @param existeEnPersona The existeEnPersona to set.
	 */
	public void setExisteEnPersona(boolean existeEnPersona) 
	{
		this.existeEnPersona = existeEnPersona;
	}

	/**
	 * @return Returns the nombreSexo.
	 */
	public String getNombreSexo() 
	{
		return nombreSexo;
	}

	/**
	 * @param nombreSexo The nombreSexo to set.
	 */
	public void setNombreSexo(String nombreSexo) 
	{
		this.nombreSexo = nombreSexo;
	}

	/**
	 * @return Returns the nombreArchivo.
	 */
	public String getNombreArchivo() 
	{
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo The nombreArchivo to set.
	 */
	public void setNombreArchivo(String nombreArchivo) 
	{
		this.nombreArchivo = nombreArchivo;
	}

	public String getInconsistenciasEncontradas()
	{
		return inconsistenciasEncontradas;
	}

	public void setInconsistenciasEncontradas(
			String inconsistenciasEncontradas)
	{
		this.inconsistenciasEncontradas = inconsistenciasEncontradas;
	}

	public String getClasificacionSE() {
		return clasificacionSE;
	}

	public void setClasificacionSE(String clasificacionSE) {
		this.clasificacionSE = clasificacionSE;
	}

	public String getNumeroFicha() {
		return numeroFicha;
	}

	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Collection getListadoClasificacionSE() {
		return listadoClasificacionSE;
	}

	public void setListadoClasificacionSE(Collection listadoClasificacionSE) {
		this.listadoClasificacionSE = listadoClasificacionSE;
	}

	/**
	 * Se llena la lista con las personas de archivo plano y las personas con inconsistencias 
	 * @param listaFinalPersonasInconsistencias
	 */
	public void setListaFinalPersonasInconsistencias(
			ArrayList<DTOSubirPacientesInconsistencias> listaFinalPersonasInconsistencias) {
		this.listaFinalPersonasInconsistencias = listaFinalPersonasInconsistencias;
	}

	/**
	 * 
	 * @return listaFinalPersonasInconsistencias 
	 */
	public ArrayList<DTOSubirPacientesInconsistencias> getListaFinalPersonasInconsistencias() {
		return listaFinalPersonasInconsistencias;
	}

	public void setListaPersonasCargue(ArrayList<String> listaPersonasCargue) {
		this.listaPersonasCargue = listaPersonasCargue;
	}

	public ArrayList<String> getListaPersonasCargue() {
		return listaPersonasCargue;
	}

	public void setPersonaAPlanoChecked(ArrayList<String> personaAPlanoChecked) {
		this.personaAPlanoChecked = personaAPlanoChecked;
	}

	public ArrayList<String> getPersonaAPlanoChecked() {
		return personaAPlanoChecked;
	}

	public void setPersonaBDatosChecked(ArrayList<String> personaBDatosChecked) {
		this.personaBDatosChecked = personaBDatosChecked;
	}

	public ArrayList<String> getPersonaBDatosChecked() {
		return personaBDatosChecked;
	}

	/**
	 * @return the sizelistaFinalPersonasInconsistencias
	 */
	public int getSizelistaFinalPersonasInconsistencias() {
		sizelistaFinalPersonasInconsistencias=0;
		if(!Utilidades.isEmpty(listaFinalPersonasInconsistencias))
		{
			sizelistaFinalPersonasInconsistencias= listaFinalPersonasInconsistencias.size();
		}
		return sizelistaFinalPersonasInconsistencias;
	}

	public void setCobfirmar(boolean cobfirmar) {
		this.cobfirmar = cobfirmar;
	}

	public boolean isCobfirmar() {
		return cobfirmar;
	}

	/**
	 * @return the municipioResidencia
	 */
	public String getMunicipioResidencia() {
		return municipioResidencia;
	}

	/**
	 * @param municipioResidencia the municipioResidencia to set
	 */
	public void setMunicipioResidencia(String municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	/**
	 * @return the tipoIDEmpleador
	 */
	public String getTipoIDEmpleador() {
		return tipoIDEmpleador;
	}

	/**
	 * @param tipoIDEmpleador the tipoIDEmpleador to set
	 */
	public void setTipoIDEmpleador(String tipoIDEmpleador) {
		this.tipoIDEmpleador = tipoIDEmpleador;
	}

	/**
	 * @return the numIDEmpleador
	 */
	public String getNumIDEmpleador() {
		return numIDEmpleador;
	}

	/**
	 * @param numIDEmpleador the numIDEmpleador to set
	 */
	public void setNumIDEmpleador(String numIDEmpleador) {
		this.numIDEmpleador = numIDEmpleador;
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the numIDCotizante
	 */
	public String getNumIDCotizante() {
		return numIDCotizante;
	}

	/**
	 * @param numIDCotizante the numIDCotizante to set
	 */
	public void setNumIDCotizante(String numIDCotizante) {
		this.numIDCotizante = numIDCotizante;
	}

	/**
	 * @return the nombresCotizante
	 */
	public String getNombresCotizante() {
		return nombresCotizante;
	}

	/**
	 * @param nombresCotizante the nombresCotizante to set
	 */
	public void setNombresCotizante(String nombresCotizante) {
		this.nombresCotizante = nombresCotizante;
	}

	/**
	 * @return the apellidosCotizante
	 */
	public String getApellidosCotizante() {
		return apellidosCotizante;
	}

	/**
	 * @param apellidosCotizante the apellidosCotizante to set
	 */
	public void setApellidosCotizante(String apellidosCotizante) {
		this.apellidosCotizante = apellidosCotizante;
	}

	/**
	 * @return the parentescoCotizante
	 */
	public String getParentescoCotizante() {
		return parentescoCotizante;
	}

	/**
	 * @param parentescoCotizante the parentescoCotizante to set
	 */
	public void setParentescoCotizante(String parentescoCotizante) {
		this.parentescoCotizante = parentescoCotizante;
	}

	/**
	 * @return the excepcionMonto
	 */
	public String getExcepcionMonto() {
		return excepcionMonto;
	}

	/**
	 * @param excepcionMonto the excepcionMonto to set
	 */
	public void setExcepcionMonto(String excepcionMonto) {
		this.excepcionMonto = excepcionMonto;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	

	public void setLocalidadResidencia(ArrayList<Localidades> localidadResidencia) {
		this.localidadResidencia = localidadResidencia;
	}

	public ArrayList<Localidades> getLocalidadResidencia() {
		return localidadResidencia;
	}

	public void setBarrioResidencia(ArrayList<Barrios> barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}

	public ArrayList<Barrios> getBarrioResidencia() {
		return barrioResidencia;
	}

	public void setCentroAtencion(ArrayList<CentroAtencion> centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public ArrayList<CentroAtencion> getCentroAtencion() {
		return centroAtencion;
	}

	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}

	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}

	public void setCodigoPaisId(String codigoPaisId) {
		this.codigoPaisId = codigoPaisId;
	}

	public String getCodigoPaisId() {
		return codigoPaisId;
	}

	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}

	public void setCiudades(ArrayList<HashMap<String, Object>> ciudades) {
		this.ciudades = ciudades;
	}

	public ArrayList<HashMap<String, Object>> getCiudades() {
		return ciudades;
	}

	public void setCodigoCiudadResidencia(String codigoCiudadResidencia) {
		this.codigoCiudadResidencia = codigoCiudadResidencia;
	}

	public String getCodigoCiudadResidencia() {
		return codigoCiudadResidencia;
	}

	public void setCodigoBarrio(String codigoBarrio) {
		this.codigoBarrio = codigoBarrio;
	}

	public String getCodigoBarrio() {
		return codigoBarrio;
	}

	public void setNombreBarrio(String nombreBarrio) {
		this.nombreBarrio = nombreBarrio;
	}

	public String getNombreBarrio() {
		return nombreBarrio;
	}

	public void setLocalidades(ArrayList<HashMap<String, Object>> localidades) {
		this.localidades = localidades;
	}

	public ArrayList<HashMap<String, Object>> getLocalidades() {
		return localidades;
	}
	
	public void setTiposIDEmpleador(ArrayList<HashMap<String, Object>> tiposIDEmpleador) {
		this.tiposIDEmpleador = tiposIDEmpleador;
	}

	public ArrayList<HashMap<String, Object>> getTiposIDEmpleador() {
		return tiposIDEmpleador;
	}
	
	public void setTiposIDCotizante(ArrayList<HashMap<String, Object>> tiposIDCotizante) {
		this.tiposIDCotizante = tiposIDCotizante;
	}

	public ArrayList<HashMap<String, Object>> getTiposIDCotizante() {
		return tiposIDCotizante;
	}
	
	
	public void setCriterioBarrio(String criterioBarrio) {
		this.criterioBarrio = criterioBarrio;
	}

	public String getCriterioBarrio() {
		return criterioBarrio;
	}

	public void setCodigoLocalidad(String codigoLocalidad) {
		this.codigoLocalidad = codigoLocalidad;
	}

	public String getCodigoLocalidad() {
		return codigoLocalidad;
	}

	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}

	public String getNombreLocalidad() {
		return nombreLocalidad;
	}

	public void setCodigoTipoAfiliado(String codigoTipoAfiliado) {
		this.codigoTipoAfiliado = codigoTipoAfiliado;
	}

	public String getCodigoTipoAfiliado() {
		return codigoTipoAfiliado;
	}

	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}

	public void setConsecutivoCentroAtencionSeleccionado(
			int consecutivoCentroAtencionSeleccionado) {
		this.consecutivoCentroAtencionSeleccionado = consecutivoCentroAtencionSeleccionado;
	}

	public int getConsecutivoCentroAtencionSeleccionado() {
		return consecutivoCentroAtencionSeleccionado;
	}

	

	public void setAcronimoTipoIdEmpleador(String acronimoTipoIdEmpleador) {
		this.acronimoTipoIdEmpleador = acronimoTipoIdEmpleador;
	}

	public String getAcronimoTipoIdEmpleador() {
		return acronimoTipoIdEmpleador;
	}

	public void setAcronimoTipoIdCotizante(String acronimoTipoIdCotizante) {
		this.acronimoTipoIdCotizante = acronimoTipoIdCotizante;
	}

	public String getAcronimoTipoIdCotizante() {
		return acronimoTipoIdCotizante;
	}

	public void setListadoTiposParentezco(Collection listadoTiposParentezco) {
		this.listadoTiposParentezco = listadoTiposParentezco;
	}

	public Collection getListadoTiposParentezco() {
		return listadoTiposParentezco;
	}

	public void setCodigoDepartamentoResidencia(
			String codigoDepartamentoResidencia) {
		this.codigoDepartamentoResidencia = codigoDepartamentoResidencia;
	}

	public String getCodigoDepartamentoResidencia() {
		return codigoDepartamentoResidencia;
	}

	public void setComparaNomApeIndividual(boolean comparaNomApeIndividual) {
		this.comparaNomApeIndividual = comparaNomApeIndividual;
	}

	public boolean isComparaNomApeIndividual() {
		return comparaNomApeIndividual;
	}

	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}

	public String getPaisResidencia() {
		return paisResidencia;
	}

	public void setMostrarPopUpIndividual(boolean mostrarPopUpIndividual) {
		this.mostrarPopUpIndividual = mostrarPopUpIndividual;
	}

	public boolean isMostrarPopUpIndividual() {
		return mostrarPopUpIndividual;
	}

	public void setPosicionListaFinalPersonas(int posicionListaFinalPersonas) {
		this.posicionListaFinalPersonas = posicionListaFinalPersonas;
	}

	public int getPosicionListaFinalPersonas() {
		return posicionListaFinalPersonas;
	}

	public void setPosicionListaPersonasBD(int posicionListaPersonasBD) {
		this.posicionListaPersonasBD = posicionListaPersonasBD;
	}

	public int getPosicionListaPersonasBD() {
		return posicionListaPersonasBD;
	}

	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}

	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}

	public void setPersonaInconIndividual(DtoPersonas personaInconIndividual) {
		this.personaInconIndividual = personaInconIndividual;
	}

	public DtoPersonas getPersonaInconIndividual() {
		return personaInconIndividual;
	}

	public void setRespuestaPositivaGuardarLog(boolean respuestaPositivaGuardarLog) {
		this.respuestaPositivaGuardarLog = respuestaPositivaGuardarLog;
	}

	public boolean isRespuestaPositivaGuardarLog() {
		return respuestaPositivaGuardarLog;
	}

	public void setSeGuardoExitosamente(boolean seGuardoExitosamente) {
		this.seGuardoExitosamente = seGuardoExitosamente;
	}

	public boolean isSeGuardoExitosamente() {
		return seGuardoExitosamente;
	}

	public void setNoExistenPersonasIncon(boolean noExistenPersonasIncon) {
		this.noExistenPersonasIncon = noExistenPersonasIncon;
	}

	public boolean isNoExistenPersonasIncon() {
		return noExistenPersonasIncon;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setVerSeccionCotizante(boolean verSeccionCotizante) {
		this.verSeccionCotizante = verSeccionCotizante;
	}

	public boolean isVerSeccionCotizante() {
		return verSeccionCotizante;
	}

	public void setExisteUsuarioPeriodo(boolean existeUsuarioPeriodo) {
		this.existeUsuarioPeriodo = existeUsuarioPeriodo;
	}

	public boolean isExisteUsuarioPeriodo() {
		return existeUsuarioPeriodo;
	}
	
	
	/**
	 * @return the mapaNumerosId
	 */
	public HashMap getMapaNumerosId() {
		return mapaNumerosId;
	}

	/**
	 * @param mapaNumerosId the mapaNumerosId to set
	 */
	public void setMapaNumerosId(HashMap mapaNumerosId) {
		this.mapaNumerosId = mapaNumerosId;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaNumerosId
	 */
	public Object getMapaNumerosId(String key) {
		return mapaNumerosId.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaNumerosId 
	 */
	public void setMapaNumerosId(String key,Object obj) {
		this.mapaNumerosId.put(key,obj);
	}

	/**
	 * @return the listadoNaturalezaPacientes
	 */
	public ArrayList<DTONaturalezaPaciente> getListadoNaturalezaPacientes() {
		return listadoNaturalezaPacientes;
	}

	/**
	 * @param listadoNaturalezaPacientes the listadoNaturalezaPacientes to set
	 */
	public void setListadoNaturalezaPacientes(
			ArrayList<DTONaturalezaPaciente> listadoNaturalezaPacientes) {
		this.listadoNaturalezaPacientes = listadoNaturalezaPacientes;
	}

	/**
	 * @return the codigoNaturaleza
	 */
	public int getCodigoNaturaleza() {
		return codigoNaturaleza;
	}

	/**
	 * @param codigoNaturaleza the codigoNaturaleza to set
	 */
	public void setCodigoNaturaleza(int codigoNaturaleza) {
		this.codigoNaturaleza = codigoNaturaleza;
	}

	/**
	 * @return the listadoTiposAfiliado
	 */
	public Collection getListadoTiposAfiliado() {
		return listadoTiposAfiliado;
	}

	/**
	 * @param listadoTiposAfiliado the listadoTiposAfiliado to set
	 */
	public void setListadoTiposAfiliado(Collection listadoTiposAfiliado) {
		this.listadoTiposAfiliado = listadoTiposAfiliado;
	}

	/**
	 * @return the codigoPersona
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}

	/**
	 * @param codigoPersona the codigoPersona to set
	 */
	public void setCodigoPersona(int codigoPersona) {
		this.codigoPersona = codigoPersona;
	}

	/**
	 * @return the codigoUsuarioCapitado
	 */
	public long getCodigoUsuarioCapitado() {
		return codigoUsuarioCapitado;
	}

	/**
	 * @param codigoUsuarioCapitado the codigoUsuarioCapitado to set
	 */
	public void setCodigoUsuarioCapitado(long codigoUsuarioCapitado) {
		this.codigoUsuarioCapitado = codigoUsuarioCapitado;
	}

	/**
	 * @return the inconsistenciasArchivo
	 */
	public ArrayList<DtoVerificacionArchivo> getInconsistenciasArchivo() {
		return inconsistenciasArchivo;
	}

	/**
	 * @param inconsistenciasArchivo the inconsistenciasArchivo to set
	 */
	public void setInconsistenciasArchivo(
			ArrayList<DtoVerificacionArchivo> inconsistenciasArchivo) {
		this.inconsistenciasArchivo = inconsistenciasArchivo;
	}

	/**
	 * @return the listaInconsistenciaPersonaBD
	 */
	public ArrayList<DtoInconsistenciasArchivoPlano> getListaInconsistenciaPersonaBD() {
		return listaInconsistenciaPersonaBD;
	}

	/**
	 * @param listaInconsistenciaPersonaBD the listaInconsistenciaPersonaBD to set
	 */
	public void setListaInconsistenciaPersonaBD(
			ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciaPersonaBD) {
		this.listaInconsistenciaPersonaBD = listaInconsistenciaPersonaBD;
	}

	/**
	 * @return the totalLeidos
	 */
	public int getTotalLeidos() {
		return totalLeidos;
	}

	/**
	 * @param totalLeidos the totalLeidos to set
	 */
	public void setTotalLeidos(int totalLeidos) {
		this.totalLeidos = totalLeidos;
	}

	/**
	 * @return the totalGrabados
	 */
	public int getTotalGrabados() {
		return totalGrabados;
	}

	/**
	 * @param totalGrabados the totalGrabados to set
	 */
	public void setTotalGrabados(int totalGrabados) {
		this.totalGrabados = totalGrabados;
	}

	/**
	 * @return the totalInconsistencias
	 */
	public int getTotalInconsistencias() {
		return totalInconsistencias;
	}

	/**
	 * @param totalInconsistencias the totalInconsistencias to set
	 */
	public void setTotalInconsistencias(int totalInconsistencias) {
		this.totalInconsistencias = totalInconsistencias;
	}

	/**
	 * @return Retorna haceRequeridoCentroAtencion
	 */
	public boolean isHaceRequeridoCentroAtencion() {
		return haceRequeridoCentroAtencion;
	}

	/**
	 * @param haceRequeridoCentroAtencion Asigna el atributo haceRequeridoCentroAtencion
	 */
	public void setHaceRequeridoCentroAtencion(boolean haceRequeridoCentroAtencion) {
		this.haceRequeridoCentroAtencion = haceRequeridoCentroAtencion;
	}

	

}