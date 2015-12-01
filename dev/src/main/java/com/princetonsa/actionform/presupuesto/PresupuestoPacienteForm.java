/*
 * Creado en Dec 15, 2005
 */
package com.princetonsa.actionform.presupuesto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

/**
 * @author Andrés Mauricio Ruiz Vélez  		
 * Princeton S.A. (Parquesoft-Manizales)
 * 
 * Modificado por:  Andrés Eugenio Silva Monsalve
 * Fecha: Julio 27 de 2007
 * 
 */
public class PresupuestoPacienteForm extends ValidatorForm
{
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Atributo que me indica si la persona que se desea ingresar ya se encuentra en el sistema
	 */
	private boolean existePersonaEnSistema;
	
	private int codigoFormato;
	
	//************************** SECCION INFORMACIÓN DEL PACIENTE ***************************************************//
	/**
	 * Consecutivo del presupuesto generado
	 */
	private int consecutivoPresupuesto;
	
	/**
	 * Tipo de identificación del paciente
	 */
	private String tipoIdentificacion;
	
	/**
	 * Número de identificación del paciente
	 */
	private String numeroIdentificacion;
	
	/**
	 * Código de la persona en el sistema
	 */
	private int codigoPersona;
	
	/**
	 * Primer nombre del paciente
	 */
	private String primerNombre;
	
	/**
	 * Segundo nombre del paciente
	 */
	private String segundoNombre;
	
	/**
	 * Primer apellido del paciente
	 */
	private String primerApellido;

	/**
	 * Segundo apellido del paciente
	 */
	private String segundoApellido;
	
	/**
	 * Fecha de nacimiento del paciente
	 */
	private String fechaNacimiento;
	
	/**
	 * Sexo del paciente
	 */
	private int sexo;
	
	/**
	 * Dirección del paciente
	 */
	private String direccion;
	
	/**
	 * Teléfono del paciente
	 */
	private String telefono;
	
	/**
    * Variable para saber el codigo de la ciudad del paciente al ingresar
    */
    private String codigoCiudadId;
    
    
    /**
     * Variable que almacena el grupo poblacional
     */
    private String grupoPoblacional;
    
    
    
    //****************************** VARIABLES CAMBIO FUNCIONALIDAD X VENEZUELA **********************************//
    
    /**
     * Variable que almacena el nombre del pais para la identificacion
     */
    private String pais;
    
    /**
     * Variable que almacena el nombre del pais de nacimiento
     */
    private String paisNac;
    
    /**
     * Variable que almacena el nombre de la ciudad de nacimiento
     */
    private String ciudadNac;
    
    /**
     * Variable que almacena el nombre del pais de residencia
     */
    private String paisResiden;
    
    //Pais de residencia anterior
    private String paisResidenAnterior;
    
    /**
     * Variable que almacena el nombre de la ciudad de residencia
     */
    private String ciudadresiden;
    
    /**
     * Ciudad de residencia anterior
     */
    private String ciudadResidenAnterior;
    
    /**
     * Variable que almacena el nombre de todos los paises
     */
    private ArrayList nomPais;
    
    /**
     * Variable que almacena el nombre de la ciudad de la identificacion
     */
    private ArrayList nomCiudadId;
    
    /**
     * Variable que almacena el Nombre de la Ciudad de Nacimiento
     */
    private ArrayList nomCiudadNac;
    
    /**
     * Variable que Almacena el Nombre de la Ciudad de Residencia
     */
    private ArrayList nomCiudadResiden;
    
    
    
    
	
	//************************************** SECCION INFORMACION DEL RESPONSABLE ***********************************//
	/**
	 * 
	 */
    private String paquete;
    
    /**
     * 
     */
    private String nombrePaquete;
    
    /**
	 * 
	 */
    private String numeroContrato;
    
    /**
     * 
     */
    private int contrato;
    
    /**
     * 
     */
    private ArrayList convenios;
	 
	 /**
	  * 
	  */
	 private ArrayList contratos;
    
    /**
	 * Convenio del responsable 
	 */
	private int convenio;
	
	/**
	 * Nombre del convenio
	 */
	private String nombreConvenio;
	
	/**
	 * Colección para traer los convenios activos que tienen contrato vigente
	 */
	private Collection listadoConveniosVigentes;
	
	/**
	 * 
	 */
	private Collection listadoConveniosContratos;
	
	//*************************** SECCION INTERVENCION *****************************************//
	/**
	 * Médico tratante
	 */
	private int medicoTratante;
	
	/**
	 * Nombre del médico tratante
	 */
	private String nombreMedicoTratante;
	
	/**
	 * Còdigo del diagnóstico en la intervenciòn
	 */
	private String diagnosticoIntervencion;
	
	/**
	 * Nombre del diagnóstico de intervención
	 */
	private String nombreDiagnostico;
	
	/**
	 * Tipo Cie del diagnóstico
	 */
	private int cieDiagnostico;
	
	/**
	 * Mapa para almacenar los servicios de la secciòn intervención
	 */
	private HashMap mapaServiciosIntervencion;
	
	//**********************SECCIONES SERVICIOS Y ARTICULOS***************************//
	/**
	 * Mapa para almacenar los servicios del presupuesto generado
	 */
	private HashMap mapaServicios;
	
	/**
	 * Mapa para almacenar los servicios del presupuesto generado
	 */
	private HashMap mapaArticulos;
	
	/**
	 * Mapa para almacenar los servicios del presupuesto que no vienen de paquete
	 */
	private HashMap mapaServiciosSolo;
	
	/**
	 * Mapa para almacenar los servicios del presupuesto que no vienen de paquete
	 */
	private HashMap mapaArticulosSolo;

	
	//*********************    SECCION DE ARTICULOS *********************************///
	/**
	 * Número de filas existentes en el hashmap (este sirve para articulos)
	 */
	private int numeroFilasArticulos;

	/**
	 * Número de filas existentes en el hashmap (este sirve para articulos)
	 */
	private int numeroFilasServicios;
	
	/**
	 * Número de filas existentes en el hashmap (este sirve para los servicios de intervencion)
	 */
	private int numeroFilasIntervenciones;

	/**
	 * Total del presupuesto
	 */
	private String totalPresupuesto;
	
	/**
	 * 
	 */
	private String totalPresupuestoMoneda;
	
	/**
	 * 
	 */
	private HashMap mapaFormatosImpresion;
	
	/**
	 * 
	 */
	private HashMap paquetesValidos;
	
	/**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private int indiceIntervencion;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    /**
     * 
     */
    private String factorConversionMoneda;
    
    /**
     * Mapa que tiene los contratos segun el convenio
     */
    private HashMap contratosMap;
    
    
    private boolean usuarioPaciente;
    
    private String loginUsuarioPaciente;
    

    /**
     * 
     */
    private HashMap esquemasInventario;
    
    /**
     * 
     */
    private HashMap esquemasProcedimientos;
    
    /**
     * 
     */
    private HashMap resultadoProceso;

	
	/**
	 * Método para limpiar el form
	 */
	public void reset(int codigoInstitucion)
	{
		this.tipoIdentificacion="";
		this.numeroIdentificacion="";
		this.codigoPersona=0;
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.fechaNacimiento="";
		this.sexo=0;
		this.direccion="";
		this.telefono="";
		this.convenio = 0;
		this.medicoTratante = 0;
		this.diagnosticoIntervencion="";
		this.nombreDiagnostico = "";
		this.nombreConvenio = "";
		this.nombreMedicoTratante = "";
		this.factorConversionMoneda="";
		this.totalPresupuestoMoneda="";
		
		this.mapaServiciosIntervencion = new HashMap();
		this.mapaServicios = new HashMap();
		this.mapaArticulos = new HashMap();
		this.mapaServiciosSolo = new HashMap();
		this.mapaArticulosSolo = new HashMap();
		
		this.contratosMap=new HashMap();
		this.contratosMap.put("numRegistros", "0");
		
		this.numeroFilasArticulos = 0;
		this.numeroFilasServicios = 0;
		this.numeroFilasIntervenciones = 0;
		
		this.mapaServicios.put("numeroFilasMapaServicios", "0");
		this.mapaServiciosIntervencion.put("numeroFilasMapaServicios", "0");
		
		this.mapaArticulosSolo.put("numRegistros", "0");
		this.mapaServiciosSolo.put("numRegistros", "0");
		
		this.existePersonaEnSistema=false;
		this.totalPresupuesto="0.0";
		
		this.codigoCiudadId = "";
		this.grupoPoblacional = "";
		
		// Se inicializan las variables cambio funcionalidades x Venezuela
		this.pais = "";
		this.paisNac = "";
		this.ciudadNac = "";
		this.paisResiden = "";
		this.paisResidenAnterior = "";
		this.ciudadresiden = "";
		this.ciudadResidenAnterior = "";
		this.nomPais = new ArrayList();
		this.nomCiudadId = new ArrayList();
		this.nomCiudadNac = new ArrayList();
		this.nomCiudadResiden = new ArrayList();
		
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.numeroContrato="";
		
		this.paquete="";
		this.nombrePaquete="";
		this.paquetesValidos=new HashMap();
		this.paquetesValidos.put("numRegistros", "0");
		this.index=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.inicializarTagMap(codigoInstitucion);
        this.indiceIntervencion=ConstantesBD.codigoNuncaValido;
        
        this.usuarioPaciente = false;
		
        this.esquemasInventario=new HashMap<String, Object>();
        this.esquemasInventario.put("numRegistros", "0");
        this.esquemasProcedimientos=new HashMap<String, Object>();
        this.esquemasProcedimientos.put("numRegistros", "0");
        
        
        this.resultadoProceso=new HashMap();
        this.resultadoProceso.put("procesoExitoso", ConstantesBD.acronimoNo);
	}
	
	/**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }
	
	/**
	 * Método para validar los datos ingresados en el formulario
	 * @param mapping
	 * @param request
	 * @return
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
			if(this.getPais().trim().equals(""))
			{
				errores.add("CodigoPaisId", new ActionMessage("errors.required"," Pais de Identificación del Paciente "));
			}
			if(this.getCodigoCiudadId().trim().equals(""))
			{
				errores.add("CodigoCiudadId", new ActionMessage("errors.required"," Ciudad de Identificación del Paciente "));
			}
			if(this.getPaisNac().trim().equals(""))
			{
				errores.add("CodigoPaisNac", new ActionMessage("errors.required"," Pais de Nacimiento del Paciente "));
			}
			if(this.getCiudadNac().trim().equals(""))
			{
				errores.add("CodigoCiudadNac", new ActionMessage("errors.required"," Ciudad de Nacimiento del Paciente "));
			}
			if(this.getPaisResiden().trim().equals(""))
			{
				errores.add("CodigoPaisResiden", new ActionMessage("errors.required"," Pais de Residencia del Paciente "));
			}
			if(this.getCiudadresiden().trim().equals(""))
			{
				errores.add("CodigoCiudadResiden", new ActionMessage("errors.required"," Ciudad de Residencia del Paciente "));
			}
			if(this.getFechaNacimiento().trim().equals(""))
			{
				errores.add("FechaNacimiento", new ActionMessage("errors.required","La Fecha de Nacimiento"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getFechaNacimiento()))
				{
					errores.add("FechaNacimiento", new ActionMessage("errors.formatoFechaInvalido", this.getFechaNacimiento()));							
				}
				else if(UtilidadFecha.conversionFormatoFechaABD(this.getFechaNacimiento()).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0 || UtilidadFecha.conversionFormatoFechaABD(this.getFechaNacimiento()).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))==0)
				{
					errores.add("fechaEstimada", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Nacimiento ", "Actual"));
				}
			}
			if(this.getPrimerNombre().trim().equals(""))
			{
				errores.add("primerNombre", new ActionMessage("errors.required", "Primer Nombre"));
			}
			if(this.getPrimerApellido().trim().equals(""))
			{
				errores.add("primerApellido", new ActionMessage("errors.required", "Primer Apellido"));
			}
			if(this.sexo==0)
			{
				errores.add("sexo", new ActionMessage("errors.required", "Sexo"));
			}
			if(this.direccion.trim().equals(""))
			{
				errores.add("direccion", new ActionMessage("errors.required", "Dirección"));
			}
			if(this.convenio==0)
			{
				errores.add("convenio", new ActionMessage("errors.required", "El Convenio en la Información del responsable"));
			}
			if(this.contrato<=0)
			{
				errores.add("contrato", new ActionMessage("errors.required", "El Contrato "));
			}
			if(Utilidades.convertirAEntero(this.mapaServiciosIntervencion.get("numeroFilasMapaServicios")+"")>0)
			{
				if(Utilidades.convertirAEntero(this.paquetesValidos.get("numRegistros")+"")>0)
				{
					if(this.paquete.trim().equals(""))
						errores.add("paquete", new ActionMessage("errors.required", "Verifique que la intervencion este marcada como principal en la funcionalidad Componentes de Paquetes. El Paquete "));
				}
			}
			if(this.medicoTratante==0)
			{
				errores.add("medicoTratante", new ActionMessage("errors.required", "El Médico Tratante"));
			}
			if(this.diagnosticoIntervencion.trim().equals(""))
			{
				errores.add("diagnosticoIntervencion", new ActionMessage("errors.required", "El Diagnóstico de Intervención"));
			}
			
			//-Verificar que la cantidad de articulos este definida  
			for(int i=0; i<this.getNumeroFilasArticulos(); i++)
			{
				if(!(mapaArticulos.get("fueEliminadoArticulo_"+i)+"").equals("true"))
				{    
					String cantidad =  (String) mapaArticulos.get("cantidad_"+i);
					if ( cantidad.trim().equals("") ) 
					{
						errores.add("cantidadArticulo", new ActionMessage("errors.required", "La Cantidad del Articulo"));
						break;
					}	
					else if(Integer.parseInt(cantidad)<=0)
					{
						errores.add("cantidadServicio", new ActionMessage("errors.integerMayorQue", "La Cantidad del Artículo", "0"));
						break;
					}
					if(Utilidades.convertirADouble(UtilidadTexto.formatearValores(mapaArticulos.get("valorUnitarioResultados_"+i)+""))==0.0)
					{
						errores.add("articuloSinTarifa", new ActionMessage("error.manejoPaciente.componenteSinTarifa"));
						break;
					}
				}
			}
			
			//-Validar que se inserte un valor en la cantidad del servicio
			String nro = (String) mapaServicios.get("numeroFilasMapaServicios");
			if (UtilidadCadena.noEsVacio(nro))
			{
				for(int i=0; i<Integer.parseInt(nro); i++)
				{
					if(!(mapaServicios.get("fueEliminadoServicio_"+i)+"").equals("true"))
					{    
						String cantidad  =  (String) mapaServicios.get("cantidadSer_"+i);
						if ( cantidad.trim().equals("") ) 
						{
							errores.add("cantidadServicio", new ActionMessage("errors.required", "La Cantidad del Servicio"));
							break;
						}
						else if(Integer.parseInt(cantidad)<=0)
						{
							errores.add("cantidadServicio", new ActionMessage("errors.integerMayorQue", "La Cantidad del Servicio", "0"));
							break;
						}
							
					}//if
					if(!(mapaServicios.get("fueEliminadoServicio_"+i)+"").equals("true"))
					{	
						if(Utilidades.convertirADouble(UtilidadTexto.formatearValores(mapaServicios.get("valor_unitario_"+i)+""))==0.0)
						{
							errores.add("servicioSinTarifa", new ActionMessage("error.manejoPaciente.componenteSinTarifa"));
							break;
						}
					}
				}//for
			}//if
			
			//-Validar que se haya agregado al menos un servicio de intervención
			String nroIntervenciones = (String) mapaServiciosIntervencion.get("numeroFilasMapaServicios");
			if(Integer.parseInt(nroIntervenciones)<1)
				{
					errores.add("servicioIntervencion", new ActionMessage("errors.required", "El Servicio de Intervención"));
				}
			else
			{
				int contaEliminados=0;
				//Se verifica si los servicios han sido eliminados
				for(int c=0; c<Integer.parseInt(nroIntervenciones); c++)
				{
					if(mapaServiciosIntervencion.get("fueEliminadoIntervencion_"+c).equals("true"))
					{
						contaEliminados++;
					}
				}//for
				
				//Si el número de servicios eliminados es igual al número de servicios en el mapa se muestra el error
				if(contaEliminados==Integer.parseInt(nroIntervenciones))
				{
					errores.add("servicioIntervencion", new ActionMessage("errors.required", "El Servicio de Intervención"));
				}
			}//else
			
			if(this.totalPresupuesto.equals("0.0") || this.totalPresupuesto.equals("0"))
			{
				errores.add("totalPresupuestoCero", new ActionMessage("error.manejoPaciente.totalPresupuestoCero"));
			}
			
		}
		else if(estado.equals("empezarContinuarServicio"))
		{
			int tempoNumServicios=Integer.parseInt(this.mapaServicios.get("numeroFilasMapaServicios")+"");
			this.mapaServicios.put("numeroFilasMapaServicios", (tempoNumServicios+1)+"");
		}
		else if(estado.equals("empezarContinuarIntervencion"))
		{
			int tempoNumServicios=Integer.parseInt(this.mapaServiciosIntervencion.get("numeroFilasMapaServicios")+"");
			this.mapaServiciosIntervencion.put("numeroFilasMapaServicios", (tempoNumServicios+1)+"");
		}

		return errores;
	}
	
	
	//************************************** SETTER Y GETTERS ******************************************************//
	
	/**
	 * @return Returns the mapaFormatosImpresion.
	 */
	public HashMap getMapaFormatosImpresion()
	{
		return mapaFormatosImpresion;
	}
	
	/**
	 * @param mapaFormatosImpresion The mapaFormatosImpresion to set.
	 */
	public void setMapaFormatosImpresion(HashMap mapaFormatosImpresion)
	{
		this.mapaFormatosImpresion= mapaFormatosImpresion;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatosImpresion(String key, Object value) 
	{
		mapaFormatosImpresion.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatosImpresion(String key) 
	{
		return mapaFormatosImpresion.get(key);
	}
	
	/**
	 * @return Returns the cieDiagnostico.
	 */
	public int getCieDiagnostico()
	{
		return cieDiagnostico;
	}
	/**
	 * @param cieDiagnostico The cieDiagnostico to set.
	 */
	public void setCieDiagnostico(int cieDiagnostico)
	{
		this.cieDiagnostico = cieDiagnostico;
	}
	/**
	 * @return Returns the codigoPersona.
	 */
	public int getCodigoPersona()
	{
		return codigoPersona;
	}
	/**
	 * @param codigoPersona The codigoPersona to set.
	 */
	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
	}
	/**
	 * @return Returns the consecutivoPresupuesto.
	 */
	public int getConsecutivoPresupuesto()
	{
		return consecutivoPresupuesto;
	}
	/**
	 * @param consecutivoPresupuesto The consecutivoPresupuesto to set.
	 */
	public void setConsecutivoPresupuesto(int consecutivoPresupuesto)
	{
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}
	/**
	 * @return Returns the diagnosticoIntervencion.
	 */
	public String getDiagnosticoIntervencion()
	{
		return diagnosticoIntervencion;
	}
	/**
	 * @param diagnosticoIntervencion The diagnosticoIntervencion to set.
	 */
	public void setDiagnosticoIntervencion(String diagnosticoIntervencion)
	{
		this.diagnosticoIntervencion = diagnosticoIntervencion;
	}
	/**
	 * @return Returns the nombreDiagnostico.
	 */
	public String getNombreDiagnostico()
	{
		return nombreDiagnostico;
	}
	/**
	 * @param nombreDiagnostico The nombreDiagnostico to set.
	 */
	public void setNombreDiagnostico(String nombreDiagnostico)
	{
		this.nombreDiagnostico = nombreDiagnostico;
	}
	/**
	 * @return Returns the convenio.
	 */
	public int getConvenio()
	{
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}
	
	/**
	 * @return Returns the direccion.
	 */
	public String getDireccion()
	{
		return direccion;
	}
	/**
	 * @param direccion The direccion to set.
	 */
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Returns the existePersonaEnSistema.
	 */
	public boolean isExistePersonaEnSistema()
	{
		return existePersonaEnSistema;
	}
	/**
	 * @param existePersonaEnSistema The existePersonaEnSistema to set.
	 */
	public void setExistePersonaEnSistema(boolean existePersonaEnSistema)
	{
		this.existePersonaEnSistema = existePersonaEnSistema;
	}
	/**
	 * @return Returns the fechaNacimiento.
	 */
	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}
	/**
	 * @param fechaNacimiento The fechaNacimiento to set.
	 */
	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}
	/**
	 * @return Returns the medicoTratante.
	 */
	public int getMedicoTratante()
	{
		return medicoTratante;
	}
	/**
	 * @param medicoTratante The medicoTratante to set.
	 */
	public void setMedicoTratante(int medicoTratante)
	{
		this.medicoTratante = medicoTratante;
	}
	/**
	 * @return Returns the numeroIdentificacion.
	 */
	public String getNumeroIdentificacion()
	{
		return numeroIdentificacion;
	}
	/**
	 * @param numeroIdentificacion The numeroIdentificacion to set.
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion)
	{
		this.numeroIdentificacion = numeroIdentificacion;
	}
	/**
	 * @return Returns the primerApellido.
	 */
	public String getPrimerApellido()
	{
		return primerApellido;
	}
	/**
	 * @param primerApellido The primerApellido to set.
	 */
	public void setPrimerApellido(String primerApellido)
	{
		this.primerApellido = primerApellido;
	}
	/**
	 * @return Returns the primerNombre.
	 */
	public String getPrimerNombre()
	{
		return primerNombre;
	}
	/**
	 * @param primerNombre The primerNombre to set.
	 */
	public void setPrimerNombre(String primerNombre)
	{
		this.primerNombre = primerNombre;
	}
	/**
	 * @return Returns the segundoApellido.
	 */
	public String getSegundoApellido()
	{
		return segundoApellido;
	}
	/**
	 * @param segundoApellido The segundoApellido to set.
	 */
	public void setSegundoApellido(String segundoApellido)
	{
		this.segundoApellido = segundoApellido;
	}
	/**
	 * @return Returns the segundoNombre.
	 */
	public String getSegundoNombre()
	{
		return segundoNombre;
	}
	/**
	 * @param segundoNombre The segundoNombre to set.
	 */
	public void setSegundoNombre(String segundoNombre)
	{
		this.segundoNombre = segundoNombre;
	}
	/**
	 * @return Returns the sexo.
	 */
	public int getSexo()
	{
		return sexo;
	}
	/**
	 * @param sexo The sexo to set.
	 */
	public void setSexo(int sexo)
	{
		this.sexo = sexo;
	}
	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono()
	{
		return telefono;
	}
	/**
	 * @param telefono The telefono to set.
	 */
	public void setTelefono(String telefono)
	{
		this.telefono = telefono;
	}
	/**
	 * @return Returns the tipoIdentificacion.
	 */
	public String getTipoIdentificacion()
	{
		return tipoIdentificacion;
	}
	/**
	 * @param tipoIdentificacion The tipoIdentificacion to set.
	 */
	public void setTipoIdentificacion(String tipoIdentificacion)
	{
		this.tipoIdentificacion = tipoIdentificacion;
	}
	/**
	 * @return Retorna numeroFilasArticulos.
	 */
	public int getNumeroFilasArticulos() {
		return numeroFilasArticulos;
	}
	/**
	 * @param Asigna numeroFilasArticulos.
	 */
	public void setNumeroFilasArticulos(int numeroFilasArticulos) {
		this.numeroFilasArticulos = numeroFilasArticulos;
	}
	
	
	public Object getMapaArticulos(String key) {
		return mapaArticulos.get(key);
	}
	/**
	 * @param Asigna mapaArticulos.
	 */
	public void setMapaArticulos(String key, Object obj) {
		this.mapaArticulos.put(key, obj) ;
	}
	
		/**
	 * @return Returns the listadoConveniosVigentes.
	 */
	public Collection getListadoConveniosVigentes()
	{
		return listadoConveniosVigentes;
	}
	/**
	 * @param listadoConveniosVigentes The listadoConveniosVigentes to set.
	 */
	public void setListadoConveniosVigentes(Collection listadoConveniosVigentes)
	{
		this.listadoConveniosVigentes = listadoConveniosVigentes;
	}
	/**
	 * @return Returns the mapaArticulos.
	 */
	public HashMap getMapaArticulos()
	{
		return mapaArticulos;
	}
	/**
	 * @param mapaArticulos The mapaArticulos to set.
	 */
	public void setMapaArticulos(HashMap mapaArticulos)
	{
		this.mapaArticulos = mapaArticulos;
	}
	
	public void setMapaArticulos(Object key, Object value) 
	{
		this.mapaArticulos.put(key, value);
	}
	
	/**
	 * @return Returns the mapaServicios.
	 */
	public HashMap getMapaServicios()
	{
		return mapaServicios;
	}
	/**
	 * @param mapaServicios The mapaServicios to set.
	 */
	public void setMapaServicios(HashMap mapaServicios)
	{
		this.mapaServicios = mapaServicios;
	}
	
	public void setMapaServicios(Object key, Object value) 
	{
		this.mapaServicios.put(key, value);
	}
	
	/**
	 * @return Returns the mapaServiciosIntervencion.
	 */
	public HashMap getMapaServiciosIntervencion()
	{
		return mapaServiciosIntervencion;
	}
	/**
	 * @param mapaServiciosIntervencion The mapaServiciosIntervencion to set.
	 */
	public void setMapaServiciosIntervencion(HashMap mapaServiciosIntervencion)
	{
		this.mapaServiciosIntervencion = mapaServiciosIntervencion;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaServiciosIntervencion(Object key, Object value) 
	{
		this.mapaServiciosIntervencion.put(key, value);
	}
	
	/**
	 * @return Retorna numeroFilasServicios.
	 */
	public int getNumeroFilasServicios() {
		return numeroFilasServicios;
	}
	/**
	 * @param Asigna numeroFilasServicios.
	 */
	public void setNumeroFilasServicios(int numeroFilasServicios) {
		this.numeroFilasServicios = numeroFilasServicios;
	}
	
	/**
	 * @return Returns the nombreConvenio.
	 */
	public String getNombreConvenio()
	{
		return nombreConvenio;
	}
	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setNombreConvenio(String nombreConvenio)
	{
		this.nombreConvenio = nombreConvenio;
	}
	/**
	 * @return Returns the nombreMedicoTratante.
	 */
	public String getNombreMedicoTratante()
	{
		return nombreMedicoTratante;
	}
	/**
	 * @param nombreMedicoTratante The nombreMedicoTratante to set.
	 */
	public void setNombreMedicoTratante(String nombreMedicoTratante)
	{
		this.nombreMedicoTratante = nombreMedicoTratante;
	}
	/**
	 * @return Returns the numeroFilasIntervenciones.
	 */
	public int getNumeroFilasIntervenciones()
	{
		return numeroFilasIntervenciones;
	}
	/**
	 * @param numeroFilasIntervenciones The numeroFilasIntervenciones to set.
	 */
	public void setNumeroFilasIntervenciones(int numeroFilasIntervenciones)
	{
		this.numeroFilasIntervenciones = numeroFilasIntervenciones;
	}

	/**
	 * @return Returns the totalPresupuesto.
	 */
	public String getTotalPresupuesto()
	{
		return totalPresupuesto;
	}
	/**
	 * @param totalPresupuesto The totalPresupuesto to set.
	 */
	public void setTotalPresupuesto(String totalPresupuesto)
	{
		this.totalPresupuesto = totalPresupuesto;
	}

	/**
	 * @return Returns the codigoFormato.
	 */
	public int getCodigoFormato()
	{
		return codigoFormato;
	}

	/**
	 * @param codigoFormato The codigoFormato to set.
	 */
	public void setCodigoFormato(int codigoFormato)
	{
		this.codigoFormato=codigoFormato;
	}
	
	public String getCodigoCiudadId() {
		return codigoCiudadId;
	}

	public void setCodigoCiudadId(String codigoCiudadId) {
		this.codigoCiudadId = codigoCiudadId;
	}

	/**
	 * @return the grupoPoblacional
	 */
	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}

	/**
	 * @param grupoPoblacional the grupoPoblacional to set
	 */
	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}

	/**
	 * 
	 * @return
	 */
	public String getCiudadNac() {
		return ciudadNac;
	}
	
	/**
	 * 
	 * @param ciudadNac
	 */
	public void setCiudadNac(String ciudadNac) {
		this.ciudadNac = ciudadNac;
	}

	/**
	 * 
	 * @return
	 */
	public String getCiudadresiden() {
		return ciudadresiden;
	}
	
	/**
	 * 
	 * @param ciudadresiden
	 */
	
	public void setCiudadresiden(String ciudadresiden) {
		this.ciudadresiden = ciudadresiden;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPais() {
		return pais;
	}
	
	/**
	 * 
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * 
	 * @return
	 */
	public String getPaisNac() {
		return paisNac;
	}
	
	/**
	 * 
	 * @param paisNac
	 */
	public void setPaisNac(String paisNac) {
		this.paisNac = paisNac;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisResiden() {
		return paisResiden;
	}
	
	/**
	 * 
	 * @param paisResiden
	 */
	public void setPaisResiden(String paisResiden) {
		this.paisResiden = paisResiden;
	}

	public ArrayList getNomCiudadId() {
		return nomCiudadId;
	}

	public void setNomCiudadId(ArrayList nomCiudadId) {
		this.nomCiudadId = nomCiudadId;
	}

	public ArrayList getNomCiudadNac() {
		return nomCiudadNac;
	}

	public void setNomCiudadNac(ArrayList nomCiudadNac) {
		this.nomCiudadNac = nomCiudadNac;
	}

	public ArrayList getNomCiudadResiden() {
		return nomCiudadResiden;
	}

	public void setNomCiudadResiden(ArrayList nomCiudadResiden) {
		this.nomCiudadResiden = nomCiudadResiden;
	}

	public ArrayList getNomPais() {
		return nomPais;
	}

	public void setNomPais(ArrayList nomPais) {
		this.nomPais = nomPais;
	}

	public int getContrato() {
		return contrato;
	}

	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public ArrayList getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList contratos) {
		this.contratos = contratos;
	}

	public ArrayList getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	public String getNombrePaquete() {
		return nombrePaquete;
	}

	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}

	public String getPaquete() {
		return paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	public HashMap getPaquetesValidos() {
		return paquetesValidos;
	}

	public void setPaquetesValidos(HashMap paquetesValidos) 
	{
		this.paquetesValidos = paquetesValidos;
	}

	public void setPaquetesValidos(Object key, Object value) 
	{
		this.paquetesValidos.put(key, value);
	}
	
	/**
	 * @return the ciudadResidenAnterior
	 */
	public String getCiudadResidenAnterior() {
		return ciudadResidenAnterior;
	}

	/**
	 * @param ciudadResidenAnterior the ciudadResidenAnterior to set
	 */
	public void setCiudadResidenAnterior(String ciudadResidenAnterior) {
		this.ciudadResidenAnterior = ciudadResidenAnterior;
	}

	/**
	 * @return the paisResidenAnterior
	 */
	public String getPaisResidenAnterior() {
		return paisResidenAnterior;
	}

	/**
	 * @param paisResidenAnterior the paisResidenAnterior to set
	 */
	public void setPaisResidenAnterior(String paisResidenAnterior) {
		this.paisResidenAnterior = paisResidenAnterior;
	}

	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean isManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}
	
	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the totalPresupuestoMoneda
	 */
	public String getTotalPresupuestoMoneda() {
		return totalPresupuestoMoneda;
	}

	/**
	 * @param totalPresupuestoMoneda the totalPresupuestoMoneda to set
	 */
	public void setTotalPresupuestoMoneda(String totalPresupuestoMoneda) {
		this.totalPresupuestoMoneda = totalPresupuestoMoneda;
	}

	/**
	 * @return the factorConversionMoneda
	 */
	public String getFactorConversionMoneda() {
		return factorConversionMoneda;
	}

	/**
	 * @param factorConversionMoneda the factorConversionMoneda to set
	 */
	public void setFactorConversionMoneda(String factorConversionMoneda) {
		this.factorConversionMoneda = factorConversionMoneda;
	}

	/**
	 * @return the contratosMap
	 */
	public HashMap getContratosMap() {
		return contratosMap;
	}

	/**
	 * @param contratosMap the contratosMap to set
	 */
	public void setContratosMap(HashMap contratosMap) {
		this.contratosMap = contratosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		return contratosMap.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setContratosMap(Object key, Object value) {
		this.contratosMap.put(key, value);
	}

	/**
	 * @return the indiceIntervencion
	 */
	public int getIndiceIntervencion() {
		return indiceIntervencion;
	}

	/**
	 * @param indiceIntervencion the indiceIntervencion to set
	 */
	public void setIndiceIntervencion(int indiceIntervencion) {
		this.indiceIntervencion = indiceIntervencion;
	}

	/**
	 * @return the mapaArticulosSolo
	 */
	public HashMap getMapaArticulosSolo() {
		return mapaArticulosSolo;
	}

	/**
	 * @param mapaArticulosSolo the mapaArticulosSolo to set
	 */
	public void setMapaArticulosSolo(HashMap mapaArticulosSolo) {
		this.mapaArticulosSolo = mapaArticulosSolo;
	}

	/**
	 * @return the mapaServiciosSolo
	 */
	public HashMap getMapaServiciosSolo() {
		return mapaServiciosSolo;
	}

	/**
	 * @param mapaServiciosSolo the mapaServiciosSolo to set
	 */
	public void setMapaServiciosSolo(HashMap mapaServiciosSolo) {
		this.mapaServiciosSolo = mapaServiciosSolo;
	}

	public boolean isUsuarioPaciente() {
		return usuarioPaciente;
	}

	public void setUsuarioPaciente(boolean usuarioPaciente) {
		this.usuarioPaciente = usuarioPaciente;
	}

	public String getLoginUsuarioPaciente() {
		return loginUsuarioPaciente;
	}

	public void setLoginUsuarioPaciente(String loginUsuarioPaciente) {
		this.loginUsuarioPaciente = loginUsuarioPaciente;
	}

	/**
	 * @return the listadoConveniosContratos
	 */
	public Collection getListadoConveniosContratos() {
		return listadoConveniosContratos;
	}

	/**
	 * @param listadoConveniosContratos the listadoConveniosContratos to set
	 */
	public void setListadoConveniosContratos(Collection listadoConveniosContratos) {
		this.listadoConveniosContratos = listadoConveniosContratos;
	}

	public HashMap getEsquemasInventario() {
		return esquemasInventario;
	}

	public void setEsquemasInventario(HashMap esquemasInventario) {
		this.esquemasInventario = esquemasInventario;
	}

	public HashMap getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}

	public void setEsquemasProcedimientos(HashMap esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}

	/**
	 * @return the resultadoProceso
	 */
	public HashMap getResultadoProceso() {
		return resultadoProceso;
	}

	/**
	 * @param resultadoProceso the resultadoProceso to set
	 */
	public void setResultadoProceso(HashMap resultadoProceso) {
		this.resultadoProceso = resultadoProceso;
	}


	public Object getResultadoProceso(String key) {
		return resultadoProceso.get(key);
	}

	public void setResultadoProceso(String key, Object value) {
		this.resultadoProceso.put(key,value);
	}
	

}
