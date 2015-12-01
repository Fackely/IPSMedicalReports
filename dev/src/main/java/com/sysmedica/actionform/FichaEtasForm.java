package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaEtasForm extends ValidatorForm {


	private boolean fichamodulo;
	private String esPrimeraVez;
	private boolean vieneDeFichasAnteriores;
	private String urlFichasAnteriores;

	private boolean activa;
	private boolean hayServicios;
	private boolean hayServicios2;
    private boolean hayLaboratorios;
    private int codigoInstitucion;
    private boolean notificar;
    private String loginUsuario;
    private int codigoFichaEtas;
    private int codigoConvenio;
    private String valorDivDx;
    private int codigoEnfNotificable;
    
    //*************************************
    // DATOS CARA A :
        
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private int institucionAtendio;
    private String fechaNotificacion;
    private int semanaEpidemiologica;
    private int anyoSemanaEpi;
    
    // Identificacion del paciente
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String departamentoNacimiento;
    private String ciudadNacimiento;
    private String departamentoResidencia;
    private String ciudadResidencia;
    private String direccionPaciente;
    private String telefonoPaciente;
    private String fechaNacimiento;
    private String edad;
    private String genero;
    private String estadoCivil;
    private String documento;
    private String barrioResidencia;
    private String zonaDomicilio;
    private String ocupacion;
    private String aseguradora;
    private String regimenSalud;
    private String etnia;
    private int desplazado;
    private String tipoId;
    private String paisExpedicion;
    private String paisNacimiento;
    private String paisResidencia;
        
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private int tipoCaso;
    private boolean hospitalizado;
    private String fechaHospitalizacion;
    private boolean estaVivo;
    private String fechaDefuncion;
    private String nombreProfesional;
    
    
    //**************************************
    // DATOS CARA B :
    
    private int codigoFichaAcciOfidico;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    

    private String otroSintoma;
    private String horaInicioSintomas;
    private String minutoInicioSintomas;
    private String nombreAlimento1;
    private String nombreAlimento2;
    private String nombreAlimento3;
    private String nombreAlimento4;
    private String nombreAlimento5;
    private String nombreAlimento6;
    private String nombreAlimento7;
    private String nombreAlimento8;
    private String nombreAlimento9;
    private String lugarConsumo1;
    private String lugarConsumo2;
    private String lugarConsumo3;
    private String lugarConsumo4;
    private String lugarConsumo5;
    private String lugarConsumo6;
    private String lugarConsumo7;
    private String lugarConsumo8;
    private String lugarConsumo9;
    private String horaConsumo1;
    private String horaConsumo2;
    private String horaConsumo3;
    private String horaConsumo4;
    private String horaConsumo5;
    private String horaConsumo6;
    private String horaConsumo7;
    private String horaConsumo8;
    private String horaConsumo9;
    private String minutoConsumo1;
    private String minutoConsumo2;
    private String minutoConsumo3;
    private String minutoConsumo4;
    private String minutoConsumo5;
    private String minutoConsumo6;
    private String minutoConsumo7;
    private String minutoConsumo8;
    private String minutoConsumo9;
    private int asociadoBrote;
    private int captadoPor;
    private int relacionExposicion;
    private int tomoMuestra;
    private int tipoMuestra;
    private String cualMuestra;
    private int agente1;
    private int agente2;
    private int agente3;
    private int agente4;
    
    private HashMap sintomas;
    private HashMap alimentos;
    
    private int numeroOtrosTipos;
	

    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    
    // Variables para las valoraciones
    private String valorDiagnostico;
    private String idDiagnostico;
    private String propiedadDiagnostico;
    private String idDiv;
    private String idCheckBox;
    private String idHiddenCheckBox;
    private String propiedadHiddenCheckBox;
    private int tipoDiagnostico;
    private int numero;
    private String idNumero;
    private String diagnosticosSeleccionados;
    private String idDiagSeleccionados;
    private String idValorFicha;
    private boolean epidemiologia;
    
    private String estado;
    
    
    public void reset() {
    	
    	otroSintoma = "";
        horaInicioSintomas = "";
        minutoInicioSintomas = "";
        nombreAlimento1 = "";
        nombreAlimento2 = "";
        nombreAlimento3 = "";
        nombreAlimento4 = "";
        nombreAlimento5 = "";
        nombreAlimento6 = "";
        nombreAlimento7 = "";
        nombreAlimento8 = "";
        nombreAlimento9 = "";
        lugarConsumo1 = "";
        lugarConsumo2 = "";
        lugarConsumo3 = "";
        lugarConsumo4 = "";
        lugarConsumo5 = "";
        lugarConsumo6 = "";
        lugarConsumo7 = "";
        lugarConsumo8 = "";
        lugarConsumo9 = "";
        horaConsumo1 = "";
        horaConsumo2 = "";
        horaConsumo3 = "";
        horaConsumo4 = "";
        horaConsumo5 = "";
        horaConsumo6 = "";
        horaConsumo7 = "";
        horaConsumo8 = "";
        horaConsumo9 = "";
        minutoConsumo1 = "";
        minutoConsumo2 = "";
        minutoConsumo3 = "";
        minutoConsumo4 = "";
        minutoConsumo5 = "";
        minutoConsumo6 = "";
        minutoConsumo7 = "";
        minutoConsumo8 = "";
        minutoConsumo9 = "";
        asociadoBrote = 0;
        captadoPor = 0;
        relacionExposicion = 0;
        tomoMuestra = 0;
        tipoMuestra = 0;
        cualMuestra = "";
        agente1 = 0;
        agente2 = 0;
        agente3 = 0;
        agente4 = 0;
        
        String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        fechaInicioSint = "";
        tipoCaso = 0;
        
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = false;
        fechaDefuncion = "";
        nombreProfesional = "";
        
        pais = "COLOMBIA";
        areaProcedencia = 0;
        grupoPoblacional = "";
        
        numeroOtrosTipos = 0;
        
        sintomas = new HashMap();
        alimentos = new HashMap();
    }
    
    
    

    /**
     * Metodo para validar los atributos provenientes del formulario
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
            
			reset();
		}
        
        if (estado.equals("actualizar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	if (fechaInicioSint.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            else {
            	errores.add("El campo Fecha de Inicio de Sintomas es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Sintomas"));
            }
        	
        	
        	
        	
        	try {
	            if (fechaHospitalizacion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaHospitalizacion)) {
		                
		                errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Hospitalizacion"));
		            }
		            else if (fechaHospitalizacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Hospitalizacion", "actual"));
					}
	            }
	            
	            if (fechaDefuncion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaDefuncion)) {
		                
		                errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.formatoFechaInvalido","de Defuncion"));
		            }
		            else if (fechaDefuncionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Defuncion", "actual"));
					}
	            }
	        	
	            
	            

	            if (!validarFormatoHora(horaInicioSintomas)) {
	            	
	            	errores.add("El Formato de Hora de Inicio de Síntomas es Incorrecto", new ActionMessage("error.epidemiologia.formatohorasintomas"));
	            }
	            
	            
	            
	            if (alimentos.size()==0) {
	            	
	            	errores.add("No se Ingresaron Alimentos", new ActionMessage("error.epidemiologia.noingresoalimentos"));
	            }
	            else {
	            	
	            	int tamAlimentos = alimentos.size()/3;
	            	
	            	for (int i=0;i<tamAlimentos;i++) {
	            		
	            		String hora = alimentos.get("hora_"+i).toString();
	            		
	            		int j = i+1;
	            		
	            		if (!validarFormatoHora(hora)) {
	            			
	            			errores.add("El Formato de Hora de Consumo de Alimento no es Válido", new ActionMessage("error.epidemiologia.formatohoraconsumo",Integer.toString(j)));
	            		}
	            	}
	            }
            }
            catch (NullPointerException npe) {}
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	
        	
        	if (fechaInicioSint.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	                
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            else {
            	errores.add("El campo Fecha de Inicio de Sintomas es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Sintomas"));
            }
        	
        	

        	
            try {
            	if (fechaHospitalizacion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaHospitalizacion)) {
		                
		                errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Hospitalizacion"));
		            }
		            else if (fechaHospitalizacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Hospitalizacion", "actual"));
					}
	            }
	            
	            if (fechaDefuncion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaDefuncion)) {
		                
		                errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.formatoFechaInvalido","de Defuncion"));
		            }
		            else if (fechaDefuncionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Defuncion", "actual"));
					}
	            }
	            
            }
            catch (NullPointerException npe) {}
            
            
            
            if (!validarFormatoHora(horaInicioSintomas)) {
            	
            	errores.add("El Formato de Hora de Inicio de Síntomas es Incorrecto", new ActionMessage("error.epidemiologia.formatohorasintomas"));
            }
            
            
            

            if (alimentos.size()==0) {
            	
            	errores.add("No se Ingresaron Alimentos", new ActionMessage("error.epidemiologia.noingresoalimentos"));
            }
            else {
            	
            	int tamAlimentos = alimentos.size()/3;
            	
            	for (int i=0;i<tamAlimentos;i++) {
            		
            		String hora = alimentos.get("hora_"+i).toString();
            		
            		int j = i+1;
            		
            		if (!validarFormatoHora(hora)) {
            			
            			errores.add("El Formato de Hora de Consumo de Alimento no es Válido", new ActionMessage("error.epidemiologia.formatohoraconsumo",Integer.toString(j)));
            		}
            	}
            }
        }
        
        return errores;
    }
    
    
    public boolean validarFormatoHora(String hora) {
    	
    	boolean resultado = true;
    	
    	String[] elementos = hora.split(":");
    	
    	if (elementos.length!=2) {
    		
    		resultado = false;
    	}
    	else {
    		if (elementos[0].length()>2) {
        		
        		resultado = false;
        	}
        	if (elementos[1].length()>2) {
        		
        		resultado = false;
        	}
        	
        	try {
        		int h = Integer.parseInt(elementos[0]);
        		int m = Integer.parseInt(elementos[1]);
        		
        		if (h>23) {
        			resultado=false;
        		}
        		
        		if (m>59) {
        			
        			resultado=false;
        		}
        		
        		if (h<0) {
        			resultado=false;
        		}
        		
        		if (m<0) {
        			
        			resultado=false;
        		}
        	}
        	catch (NumberFormatException nfe) {
        		
        		resultado = false;
        	}
        	
    	}
    	
    	
    	
    	
    	return resultado;
    }



	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public int getAgente1() {
		return agente1;
	}


	public void setAgente1(int agente1) {
		this.agente1 = agente1;
	}


	public int getAgente2() {
		return agente2;
	}


	public void setAgente2(int agente2) {
		this.agente2 = agente2;
	}


	public int getAgente3() {
		return agente3;
	}


	public void setAgente3(int agente3) {
		this.agente3 = agente3;
	}


	public int getAgente4() {
		return agente4;
	}


	public void setAgente4(int agente4) {
		this.agente4 = agente4;
	}


	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}


	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
	}


	public int getAreaProcedencia() {
		return areaProcedencia;
	}


	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}


	public String getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}


	public int getAsociadoBrote() {
		return asociadoBrote;
	}


	public void setAsociadoBrote(int asociadoBrote) {
		this.asociadoBrote = asociadoBrote;
	}


	public String getBarrioResidencia() {
		return barrioResidencia;
	}


	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}


	public int getCaptadoPor() {
		return captadoPor;
	}


	public void setCaptadoPor(int captadoPor) {
		this.captadoPor = captadoPor;
	}


	public String getCiudadNacimiento() {
		return ciudadNacimiento;
	}


	public void setCiudadNacimiento(String ciudadNacimiento) {
		this.ciudadNacimiento = ciudadNacimiento;
	}


	public String getCiudadResidencia() {
		return ciudadResidencia;
	}


	public void setCiudadResidencia(String ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia;
	}


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}


	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}


	public int getCodigoEnfNotificable() {
		return codigoEnfNotificable;
	}


	public void setCodigoEnfNotificable(int codigoEnfNotificable) {
		this.codigoEnfNotificable = codigoEnfNotificable;
	}


	public int getCodigoFichaAcciOfidico() {
		return codigoFichaAcciOfidico;
	}


	public void setCodigoFichaAcciOfidico(int codigoFichaAcciOfidico) {
		this.codigoFichaAcciOfidico = codigoFichaAcciOfidico;
	}


	public int getCodigoFichaEtas() {
		return codigoFichaEtas;
	}


	public void setCodigoFichaEtas(int codigoFichaEtas) {
		this.codigoFichaEtas = codigoFichaEtas;
	}


	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}


	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
	}


	public int getCodigoNotificacion() {
		return codigoNotificacion;
	}


	public void setCodigoNotificacion(int codigoNotificacion) {
		this.codigoNotificacion = codigoNotificacion;
	}


	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public String getCualMuestra() {
		return cualMuestra;
	}


	public void setCualMuestra(String cualMuestra) {
		this.cualMuestra = cualMuestra;
	}


	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}


	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}


	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}


	public int getDesplazado() {
		return desplazado;
	}


	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
	}


	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}


	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}


	public String getDireccionPaciente() {
		return direccionPaciente;
	}


	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}


	public String getDocumento() {
		return documento;
	}


	public void setDocumento(String documento) {
		this.documento = documento;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public boolean isEpidemiologia() {
		return epidemiologia;
	}


	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}


	public String getEsPrimeraVez() {
		return esPrimeraVez;
	}


	public void setEsPrimeraVez(String esPrimeraVez) {
		this.esPrimeraVez = esPrimeraVez;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getEstadoCivil() {
		return estadoCivil;
	}


	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public String getFechaConsultaGeneral() {
		return fechaConsultaGeneral;
	}


	public void setFechaConsultaGeneral(String fechaConsultaGeneral) {
		this.fechaConsultaGeneral = fechaConsultaGeneral;
	}


	public String getFechaDefuncion() {
		return fechaDefuncion;
	}


	public void setFechaDefuncion(String fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}


	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}


	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}


	public String getFechaInicioSint() {
		return fechaInicioSint;
	}


	public void setFechaInicioSint(String fechaInicioSint) {
		this.fechaInicioSint = fechaInicioSint;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public boolean isFichamodulo() {
		return fichamodulo;
	}


	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}


	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}


	public boolean isHayLaboratorios() {
		return hayLaboratorios;
	}


	public void setHayLaboratorios(boolean hayLaboratorios) {
		this.hayLaboratorios = hayLaboratorios;
	}


	public boolean isHayServicios() {
		return hayServicios;
	}


	public void setHayServicios(boolean hayServicios) {
		this.hayServicios = hayServicios;
	}


	public String getHoraConsumo1() {
		return horaConsumo1;
	}


	public void setHoraConsumo1(String horaConsumo1) {
		this.horaConsumo1 = horaConsumo1;
	}


	public String getHoraConsumo2() {
		return horaConsumo2;
	}


	public void setHoraConsumo2(String horaConsumo2) {
		this.horaConsumo2 = horaConsumo2;
	}


	public String getHoraConsumo3() {
		return horaConsumo3;
	}


	public void setHoraConsumo3(String horaConsumo3) {
		this.horaConsumo3 = horaConsumo3;
	}


	public String getHoraConsumo4() {
		return horaConsumo4;
	}


	public void setHoraConsumo4(String horaConsumo4) {
		this.horaConsumo4 = horaConsumo4;
	}


	public String getHoraConsumo5() {
		return horaConsumo5;
	}


	public void setHoraConsumo5(String horaConsumo5) {
		this.horaConsumo5 = horaConsumo5;
	}


	public String getHoraConsumo6() {
		return horaConsumo6;
	}


	public void setHoraConsumo6(String horaConsumo6) {
		this.horaConsumo6 = horaConsumo6;
	}


	public String getHoraConsumo7() {
		return horaConsumo7;
	}


	public void setHoraConsumo7(String horaConsumo7) {
		this.horaConsumo7 = horaConsumo7;
	}


	public String getHoraConsumo8() {
		return horaConsumo8;
	}


	public void setHoraConsumo8(String horaConsumo8) {
		this.horaConsumo8 = horaConsumo8;
	}


	public String getHoraConsumo9() {
		return horaConsumo9;
	}


	public void setHoraConsumo9(String horaConsumo9) {
		this.horaConsumo9 = horaConsumo9;
	}


	public String getHoraInicioSintomas() {
		return horaInicioSintomas;
	}


	public void setHoraInicioSintomas(String horaInicioSintomas) {
		this.horaInicioSintomas = horaInicioSintomas;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public String getIdCheckBox() {
		return idCheckBox;
	}


	public void setIdCheckBox(String idCheckBox) {
		this.idCheckBox = idCheckBox;
	}


	public String getIdDiagnostico() {
		return idDiagnostico;
	}


	public void setIdDiagnostico(String idDiagnostico) {
		this.idDiagnostico = idDiagnostico;
	}


	public String getIdDiagSeleccionados() {
		return idDiagSeleccionados;
	}


	public void setIdDiagSeleccionados(String idDiagSeleccionados) {
		this.idDiagSeleccionados = idDiagSeleccionados;
	}


	public String getIdDiv() {
		return idDiv;
	}


	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}


	public String getIdHiddenCheckBox() {
		return idHiddenCheckBox;
	}


	public void setIdHiddenCheckBox(String idHiddenCheckBox) {
		this.idHiddenCheckBox = idHiddenCheckBox;
	}


	public String getIdNumero() {
		return idNumero;
	}


	public void setIdNumero(String idNumero) {
		this.idNumero = idNumero;
	}


	public String getIdValorFicha() {
		return idValorFicha;
	}


	public void setIdValorFicha(String idValorFicha) {
		this.idValorFicha = idValorFicha;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public String getLugarConsumo1() {
		return lugarConsumo1;
	}


	public void setLugarConsumo1(String lugarConsumo1) {
		this.lugarConsumo1 = lugarConsumo1;
	}


	public String getLugarConsumo2() {
		return lugarConsumo2;
	}


	public void setLugarConsumo2(String lugarConsumo2) {
		this.lugarConsumo2 = lugarConsumo2;
	}


	public String getLugarConsumo3() {
		return lugarConsumo3;
	}


	public void setLugarConsumo3(String lugarConsumo3) {
		this.lugarConsumo3 = lugarConsumo3;
	}


	public String getLugarConsumo4() {
		return lugarConsumo4;
	}


	public void setLugarConsumo4(String lugarConsumo4) {
		this.lugarConsumo4 = lugarConsumo4;
	}


	public String getLugarConsumo5() {
		return lugarConsumo5;
	}


	public void setLugarConsumo5(String lugarConsumo5) {
		this.lugarConsumo5 = lugarConsumo5;
	}


	public String getLugarConsumo6() {
		return lugarConsumo6;
	}


	public void setLugarConsumo6(String lugarConsumo6) {
		this.lugarConsumo6 = lugarConsumo6;
	}


	public String getLugarConsumo7() {
		return lugarConsumo7;
	}


	public void setLugarConsumo7(String lugarConsumo7) {
		this.lugarConsumo7 = lugarConsumo7;
	}


	public String getLugarConsumo8() {
		return lugarConsumo8;
	}


	public void setLugarConsumo8(String lugarConsumo8) {
		this.lugarConsumo8 = lugarConsumo8;
	}


	public String getLugarConsumo9() {
		return lugarConsumo9;
	}


	public void setLugarConsumo9(String lugarConsumo9) {
		this.lugarConsumo9 = lugarConsumo9;
	}


	public String getLugarNotifica() {
		return lugarNotifica;
	}


	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}


	public String getLugarProcedencia() {
		return lugarProcedencia;
	}


	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
	}


	public String getMinutoConsumo1() {
		return minutoConsumo1;
	}


	public void setMinutoConsumo1(String minutoConsumo1) {
		this.minutoConsumo1 = minutoConsumo1;
	}


	public String getMinutoConsumo2() {
		return minutoConsumo2;
	}


	public void setMinutoConsumo2(String minutoConsumo2) {
		this.minutoConsumo2 = minutoConsumo2;
	}


	public String getMinutoConsumo3() {
		return minutoConsumo3;
	}


	public void setMinutoConsumo3(String minutoConsumo3) {
		this.minutoConsumo3 = minutoConsumo3;
	}


	public String getMinutoConsumo4() {
		return minutoConsumo4;
	}


	public void setMinutoConsumo4(String minutoConsumo4) {
		this.minutoConsumo4 = minutoConsumo4;
	}


	public String getMinutoConsumo5() {
		return minutoConsumo5;
	}


	public void setMinutoConsumo5(String minutoConsumo5) {
		this.minutoConsumo5 = minutoConsumo5;
	}


	public String getMinutoConsumo6() {
		return minutoConsumo6;
	}


	public void setMinutoConsumo6(String minutoConsumo6) {
		this.minutoConsumo6 = minutoConsumo6;
	}


	public String getMinutoConsumo7() {
		return minutoConsumo7;
	}


	public void setMinutoConsumo7(String minutoConsumo7) {
		this.minutoConsumo7 = minutoConsumo7;
	}


	public String getMinutoConsumo8() {
		return minutoConsumo8;
	}


	public void setMinutoConsumo8(String minutoConsumo8) {
		this.minutoConsumo8 = minutoConsumo8;
	}


	public String getMinutoConsumo9() {
		return minutoConsumo9;
	}


	public void setMinutoConsumo9(String minutoConsumo9) {
		this.minutoConsumo9 = minutoConsumo9;
	}


	public String getMinutoInicioSintomas() {
		return minutoInicioSintomas;
	}


	public void setMinutoInicioSintomas(String minutoInicioSintomas) {
		this.minutoInicioSintomas = minutoInicioSintomas;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public String getNombreAlimento1() {
		return nombreAlimento1;
	}


	public void setNombreAlimento1(String nombreAlimento1) {
		this.nombreAlimento1 = nombreAlimento1;
	}


	public String getNombreAlimento2() {
		return nombreAlimento2;
	}


	public void setNombreAlimento2(String nombreAlimento2) {
		this.nombreAlimento2 = nombreAlimento2;
	}


	public String getNombreAlimento3() {
		return nombreAlimento3;
	}


	public void setNombreAlimento3(String nombreAlimento3) {
		this.nombreAlimento3 = nombreAlimento3;
	}


	public String getNombreAlimento4() {
		return nombreAlimento4;
	}


	public void setNombreAlimento4(String nombreAlimento4) {
		this.nombreAlimento4 = nombreAlimento4;
	}


	public String getNombreAlimento5() {
		return nombreAlimento5;
	}


	public void setNombreAlimento5(String nombreAlimento5) {
		this.nombreAlimento5 = nombreAlimento5;
	}


	public String getNombreAlimento6() {
		return nombreAlimento6;
	}


	public void setNombreAlimento6(String nombreAlimento6) {
		this.nombreAlimento6 = nombreAlimento6;
	}


	public String getNombreAlimento7() {
		return nombreAlimento7;
	}


	public void setNombreAlimento7(String nombreAlimento7) {
		this.nombreAlimento7 = nombreAlimento7;
	}


	public String getNombreAlimento8() {
		return nombreAlimento8;
	}


	public void setNombreAlimento8(String nombreAlimento8) {
		this.nombreAlimento8 = nombreAlimento8;
	}


	public String getNombreAlimento9() {
		return nombreAlimento9;
	}


	public void setNombreAlimento9(String nombreAlimento9) {
		this.nombreAlimento9 = nombreAlimento9;
	}


	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}


	public boolean isNotificar() {
		return notificar;
	}


	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public String getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}


	public String getOtroSintoma() {
		return otroSintoma;
	}


	public void setOtroSintoma(String otroSintoma) {
		this.otroSintoma = otroSintoma;
	}


	public String getPais() {
		return pais;
	}


	public void setPais(String pais) {
		this.pais = pais;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public String getPropiedadDiagnostico() {
		return propiedadDiagnostico;
	}


	public void setPropiedadDiagnostico(String propiedadDiagnostico) {
		this.propiedadDiagnostico = propiedadDiagnostico;
	}


	public String getPropiedadHiddenCheckBox() {
		return propiedadHiddenCheckBox;
	}


	public void setPropiedadHiddenCheckBox(String propiedadHiddenCheckBox) {
		this.propiedadHiddenCheckBox = propiedadHiddenCheckBox;
	}


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}


	public int getRelacionExposicion() {
		return relacionExposicion;
	}


	public void setRelacionExposicion(int relacionExposicion) {
		this.relacionExposicion = relacionExposicion;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}


	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}


	public HashMap getSintomas() {
		return sintomas;
	}


	public void setSintomas(HashMap sintomas) {
		this.sintomas = sintomas;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}


	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}


	public String getTipoId() {
		return tipoId;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public int getTipoMuestra() {
		return tipoMuestra;
	}


	public void setTipoMuestra(int tipoMuestra) {
		this.tipoMuestra = tipoMuestra;
	}


	public int getTomoMuestra() {
		return tomoMuestra;
	}


	public void setTomoMuestra(int tomoMuestra) {
		this.tomoMuestra = tomoMuestra;
	}


	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}


	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}


	public String getValorDiagnostico() {
		return valorDiagnostico;
	}


	public void setValorDiagnostico(String valorDiagnostico) {
		this.valorDiagnostico = valorDiagnostico;
	}


	public String getValorDivDx() {
		return valorDivDx;
	}


	public void setValorDivDx(String valorDivDx) {
		this.valorDivDx = valorDivDx;
	}


	public boolean isVieneDeFichasAnteriores() {
		return vieneDeFichasAnteriores;
	}


	public void setVieneDeFichasAnteriores(boolean vieneDeFichasAnteriores) {
		this.vieneDeFichasAnteriores = vieneDeFichasAnteriores;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}




	public HashMap getAlimentos() {
		return alimentos;
	}




	public void setAlimentos(HashMap alimentos) {
		this.alimentos = alimentos;
	}




	public int getNumeroOtrosTipos() {
		return numeroOtrosTipos;
	}




	public void setNumeroOtrosTipos(int numeroOtrosTipos) {
		this.numeroOtrosTipos = numeroOtrosTipos;
	}




	public boolean isHayServicios2() {
		return hayServicios2;
	}




	public void setHayServicios2(boolean hayServicios2) {
		this.hayServicios2 = hayServicios2;
	}




	public String getPaisExpedicion() {
		return paisExpedicion;
	}




	public void setPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion = paisExpedicion;
	}




	public String getPaisNacimiento() {
		return paisNacimiento;
	}




	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}




	public String getPaisResidencia() {
		return paisResidencia;
	}




	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}
}
