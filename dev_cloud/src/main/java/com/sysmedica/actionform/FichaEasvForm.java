package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaEasvForm extends ValidatorForm {



	private boolean fichamodulo;
	private String esPrimeraVez;
	private boolean vieneDeFichasAnteriores;
	private String urlFichasAnteriores;

	private boolean activa;
	private boolean hayServicios;
    private boolean hayLaboratorios;
    private int codigoInstitucion;
    private boolean notificar;
    private String loginUsuario;
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
    
    private int codigoFichaEasv;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private int vacuna1;
    private int vacuna2;
    private int vacuna3;
    private int vacuna4;
    private int dosis1;
    private int dosis2;
    private int dosis3;
    private int dosis4;
    private int via1;
    private int via2;
    private int via3;
    private int via4;
    private int sitio1;
    private int sitio2;
    private int sitio3;
    private int sitio4;
    private String fechaVacunacion1;
    private String fechaVacunacion2;
    private String fechaVacunacion3;
    private String fechaVacunacion4;
    private String fabricante1;
    private String fabricante2;
    private String fabricante3;
    private String fabricante4;
    private String lote1;
    private String lote2;
    private String lote3;
    private String lote4;
    private String otroHallazgo;
    private String tiempo;
    private int unidadTiempo;
    private String lugarVacunacion;
    private String codDepVacunacion;
    private String codMunVacunacion;
    private String lugarVac;
    private int estadoSalud;
    private int recibiaMedicamentos;
    private String medicamentos;
    private int antPatologicos;
    private String cualesAntPatologicos;
    private int antAlergicos;
    private String cualesAntAlergicos;
    private int antEasv;
    private String cualesAntEasv;
    private int biologico1;
    private String fabricanteMuestra1;
    private String loteMuestra1;
    private String cantidadMuestra1;
    private String fechaEnvioMuestra1;
    private int biologico2;
    private String fabricanteMuestra2;
    private String loteMuestra2;
    private String cantidadMuestra2;
    private String fechaEnvioMuestra2;
    private int estadoFinal;
    private String telefonoContacto;
    
    private HashMap hallazgos;
    private HashMap vacunas;
	
    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    private int numeroOtrosTipos;
    
    
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

    	vacuna1 = 0;
        vacuna2 = 0;
        vacuna3 = 0;
        vacuna4 = 0;
        dosis1 = 0;
        dosis2 = 0;
        dosis3 = 0;
        dosis4 = 0;
        via1 = 0;
        via2 = 0;
        via3 = 0;
        via4 = 0;
        sitio1 = 0;
        sitio2 = 0;
        sitio3 = 0;
        sitio4 = 0;
        fechaVacunacion1 = "";
        fechaVacunacion2 = "";
        fechaVacunacion3 = "";
        fechaVacunacion4 = "";
        fabricante1 = "";
        fabricante2 = "";
        fabricante3 = "";
        fabricante4 = "";
        lote1 = "";
        lote2 = "";
        lote3 = "";
        lote4 = "";
        otroHallazgo = "";
        tiempo = "";
        unidadTiempo = 0;
        codDepVacunacion = "";
        codMunVacunacion = "";
        estadoSalud = 0;
        recibiaMedicamentos = 0;
        medicamentos = "";
        antPatologicos = 0;
        cualesAntPatologicos = "";
        antAlergicos = 0;
        cualesAntAlergicos = "";
        antEasv = 0;
        cualesAntEasv = "";
        biologico1 = 0;
        fabricanteMuestra1 = "";
        loteMuestra1 = "";
        cantidadMuestra1 = "";
        fechaEnvioMuestra1 = "";
        biologico2 = 0;
        fabricanteMuestra2 = "";
        loteMuestra2 = "";
        cantidadMuestra2 = "";
        fechaEnvioMuestra2 = "";
        estadoFinal = 0;
        telefonoContacto = "";
        hallazgos = new HashMap();
        vacunas = new HashMap();

    	String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        lugarVacunacion = "";
        lugarVac = lugar;
        
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
            String fechaEnvioMuestra1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaEnvioMuestra1);
            String fechaEnvioMuestra2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaEnvioMuestra2);
        	
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
	        	
	            int tamVacunas = vacunas.size()/7;
	            
	            for (int i=0; i<tamVacunas; i++) {
	            	
	            	String fechaVacunacion = vacunas.get("fecha_"+i).toString();
	            	String fechaVacunacionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaVacunacion);
	            	
	            	if (!UtilidadFecha.validarFecha(fechaVacunacion)) {
	            		
	            		errores.add("Campo Fecha de Vacunación "+i+" No Valido", new ActionMessage("errors.formatoFechaInvalido","de Vacunacion "+i));
	            	}
	            	else if (fechaVacunacionTransformada.compareTo(fechaActual)>0) {
	            		
	            		errores.add("Campo Fecha de Vacunación "+i+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Vacunacion "+i,"actual"));
	            	}
	            }
            }
            catch (NullPointerException npe) {}
            
            
            try {
            	
            	int tiempoTrans = Integer.parseInt(tiempo);
            }
            catch (NumberFormatException nfe) {
            	
            	errores.add("El Valor del Tiempo Transcurrido entre la Aplicación y los Síntomas debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","de Tiempo Transcurrido Entre la Aplicación y el Inicio de los Síntomas"));
            }
            
            
            
            if (fechaEnvioMuestra1.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaEnvioMuestra1)) {
	                
	                errores.add("Campo Fecha de Envío de Muestra 1 no valido", new ActionMessage("errors.formatoFechaInvalido","de Envío de Muestra 1"));
	            }
	            else if (fechaEnvioMuestra1Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Envío de Muestra 1 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Envío de Muestra 1", "actual"));
				}
            }
            
            

            if (fechaEnvioMuestra2.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaEnvioMuestra2)) {
	                
	                errores.add("Campo Fecha de Envío de Muestra 2 no valido", new ActionMessage("errors.formatoFechaInvalido","de Envío de Muestra 2"));
	            }
	            else if (fechaEnvioMuestra2Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Envío de Muestra 2 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Envío de Muestra 2", "actual"));
				}
            }
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaEnvioMuestra1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaEnvioMuestra1);
            String fechaEnvioMuestra2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaEnvioMuestra2);
        	
        	
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
	            
	            int tamVacunas = vacunas.size()/7;
	            
	            for (int i=0; i<tamVacunas; i++) {
	            	
	            	int j = i + 1;
	            	
	            	String fechaVacunacion = vacunas.get("fecha_"+i).toString();
	            	String fechaVacunacionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaVacunacion);
	            	
	            	if (!UtilidadFecha.validarFecha(fechaVacunacion)) {
	            		
	            		errores.add("Campo Fecha de Vacunación "+j+" No Valido", new ActionMessage("errors.formatoFechaInvalido","de Vacunacion "+j));
	            	}
	            	else if (fechaVacunacionTransformada.compareTo(fechaActual)>0) {
	            		
	            		errores.add("Campo Fecha de Vacunación "+j+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Vacunacion "+j,"actual"));
	            	}
	            }
            }
            catch (NullPointerException npe) {}
            

            try {
            	
            	int tiempoTrans = Integer.parseInt(tiempo);
            }
            catch (NumberFormatException nfe) {
            	
            	errores.add("El Valor del Tiempo Transcurrido entre la Aplicación y los Síntomas debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","de Tiempo Transcurrido Entre la Aplicación y el Inicio de los Síntomas"));
            }
            
            

            if (fechaEnvioMuestra1.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaEnvioMuestra1)) {
	                
	                errores.add("Campo Fecha de Envío de Muestra 1 no valido", new ActionMessage("errors.formatoFechaInvalido","de Envío de Muestra 1"));
	            }
	            else if (fechaEnvioMuestra1Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Envío de Muestra 1 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Envío de Muestra 1", "actual"));
				}
            }
            
            

            if (fechaEnvioMuestra2.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaEnvioMuestra2)) {
	                
	                errores.add("Campo Fecha de Envío de Muestra 2 no valido", new ActionMessage("errors.formatoFechaInvalido","de Envío de Muestra 2"));
	            }
	            else if (fechaEnvioMuestra2Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Envío de Muestra 2 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Envío de Muestra 2", "actual"));
				}
            }
        }
        
        return errores;
    }



	public boolean isActiva() {
		return activa;
	}



	public void setActiva(boolean activa) {
		this.activa = activa;
	}



	public int getAntAlergicos() {
		return antAlergicos;
	}



	public void setAntAlergicos(int antAlergicos) {
		this.antAlergicos = antAlergicos;
	}



	public int getAntEasv() {
		return antEasv;
	}



	public void setAntEasv(int antEasv) {
		this.antEasv = antEasv;
	}



	public int getAntPatologicos() {
		return antPatologicos;
	}



	public void setAntPatologicos(int antPatologicos) {
		this.antPatologicos = antPatologicos;
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



	public String getBarrioResidencia() {
		return barrioResidencia;
	}



	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}



	public int getBiologico1() {
		return biologico1;
	}



	public void setBiologico1(int biologico1) {
		this.biologico1 = biologico1;
	}



	public int getBiologico2() {
		return biologico2;
	}



	public void setBiologico2(int biologico2) {
		this.biologico2 = biologico2;
	}



	public String getCantidadMuestra1() {
		return cantidadMuestra1;
	}



	public void setCantidadMuestra1(String cantidadMuestra1) {
		this.cantidadMuestra1 = cantidadMuestra1;
	}



	public String getCantidadMuestra2() {
		return cantidadMuestra2;
	}



	public void setCantidadMuestra2(String cantidadMuestra2) {
		this.cantidadMuestra2 = cantidadMuestra2;
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



	public String getCodDepVacunacion() {
		return codDepVacunacion;
	}



	public void setCodDepVacunacion(String codDepVacunacion) {
		this.codDepVacunacion = codDepVacunacion;
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



	public int getCodigoFichaEasv() {
		return codigoFichaEasv;
	}



	public void setCodigoFichaEasv(int codigoFichaEasv) {
		this.codigoFichaEasv = codigoFichaEasv;
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



	public String getCodMunVacunacion() {
		return codMunVacunacion;
	}



	public void setCodMunVacunacion(String codMunVacunacion) {
		this.codMunVacunacion = codMunVacunacion;
	}



	public String getCualesAntAlergicos() {
		return cualesAntAlergicos;
	}



	public void setCualesAntAlergicos(String cualesAntAlergicos) {
		this.cualesAntAlergicos = cualesAntAlergicos;
	}



	public String getCualesAntEasv() {
		return cualesAntEasv;
	}



	public void setCualesAntEasv(String cualesAntEasv) {
		this.cualesAntEasv = cualesAntEasv;
	}



	public String getCualesAntPatologicos() {
		return cualesAntPatologicos;
	}



	public void setCualesAntPatologicos(String cualesAntPatologicos) {
		this.cualesAntPatologicos = cualesAntPatologicos;
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



	public int getDosis1() {
		return dosis1;
	}



	public void setDosis1(int dosis1) {
		this.dosis1 = dosis1;
	}



	public int getDosis2() {
		return dosis2;
	}



	public void setDosis2(int dosis2) {
		this.dosis2 = dosis2;
	}



	public int getDosis3() {
		return dosis3;
	}



	public void setDosis3(int dosis3) {
		this.dosis3 = dosis3;
	}



	public int getDosis4() {
		return dosis4;
	}



	public void setDosis4(int dosis4) {
		this.dosis4 = dosis4;
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



	public int getEstadoFinal() {
		return estadoFinal;
	}



	public void setEstadoFinal(int estadoFinal) {
		this.estadoFinal = estadoFinal;
	}



	public int getEstadoSalud() {
		return estadoSalud;
	}



	public void setEstadoSalud(int estadoSalud) {
		this.estadoSalud = estadoSalud;
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



	public String getFabricante1() {
		return fabricante1;
	}



	public void setFabricante1(String fabricante1) {
		this.fabricante1 = fabricante1;
	}



	public String getFabricante2() {
		return fabricante2;
	}



	public void setFabricante2(String fabricante2) {
		this.fabricante2 = fabricante2;
	}



	public String getFabricante3() {
		return fabricante3;
	}



	public void setFabricante3(String fabricante3) {
		this.fabricante3 = fabricante3;
	}



	public String getFabricante4() {
		return fabricante4;
	}



	public void setFabricante4(String fabricante4) {
		this.fabricante4 = fabricante4;
	}



	public String getFabricanteMuestra1() {
		return fabricanteMuestra1;
	}



	public void setFabricanteMuestra1(String fabricanteMuestra1) {
		this.fabricanteMuestra1 = fabricanteMuestra1;
	}



	public String getFabricanteMuestra2() {
		return fabricanteMuestra2;
	}



	public void setFabricanteMuestra2(String fabricanteMuestra2) {
		this.fabricanteMuestra2 = fabricanteMuestra2;
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



	public String getFechaEnvioMuestra1() {
		return fechaEnvioMuestra1;
	}



	public void setFechaEnvioMuestra1(String fechaEnvioMuestra1) {
		this.fechaEnvioMuestra1 = fechaEnvioMuestra1;
	}



	public String getFechaEnvioMuestra2() {
		return fechaEnvioMuestra2;
	}



	public void setFechaEnvioMuestra2(String fechaEnvioMuestra2) {
		this.fechaEnvioMuestra2 = fechaEnvioMuestra2;
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



	public String getFechaVacunacion1() {
		return fechaVacunacion1;
	}



	public void setFechaVacunacion1(String fechaVacunacion1) {
		this.fechaVacunacion1 = fechaVacunacion1;
	}



	public String getFechaVacunacion2() {
		return fechaVacunacion2;
	}



	public void setFechaVacunacion2(String fechaVacunacion2) {
		this.fechaVacunacion2 = fechaVacunacion2;
	}



	public String getFechaVacunacion3() {
		return fechaVacunacion3;
	}



	public void setFechaVacunacion3(String fechaVacunacion3) {
		this.fechaVacunacion3 = fechaVacunacion3;
	}



	public String getFechaVacunacion4() {
		return fechaVacunacion4;
	}



	public void setFechaVacunacion4(String fechaVacunacion4) {
		this.fechaVacunacion4 = fechaVacunacion4;
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



	public String getLote1() {
		return lote1;
	}



	public void setLote1(String lote1) {
		this.lote1 = lote1;
	}



	public String getLote2() {
		return lote2;
	}



	public void setLote2(String lote2) {
		this.lote2 = lote2;
	}



	public String getLote3() {
		return lote3;
	}



	public void setLote3(String lote3) {
		this.lote3 = lote3;
	}



	public String getLote4() {
		return lote4;
	}



	public void setLote4(String lote4) {
		this.lote4 = lote4;
	}



	public String getLoteMuestra1() {
		return loteMuestra1;
	}



	public void setLoteMuestra1(String loteMuestra1) {
		this.loteMuestra1 = loteMuestra1;
	}



	public String getLoteMuestra2() {
		return loteMuestra2;
	}



	public void setLoteMuestra2(String loteMuestra2) {
		this.loteMuestra2 = loteMuestra2;
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



	public String getLugarVacunacion() {
		return lugarVacunacion;
	}



	public void setLugarVacunacion(String lugarVacunacion) {
		this.lugarVacunacion = lugarVacunacion;
	}



	public String getMedicamentos() {
		return medicamentos;
	}



	public void setMedicamentos(String medicamentos) {
		this.medicamentos = medicamentos;
	}



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}



	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
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



	public String getOtroHallazgo() {
		return otroHallazgo;
	}



	public void setOtroHallazgo(String otroHallazgo) {
		this.otroHallazgo = otroHallazgo;
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



	public int getRecibiaMedicamentos() {
		return recibiaMedicamentos;
	}



	public void setRecibiaMedicamentos(int recibiaMedicamentos) {
		this.recibiaMedicamentos = recibiaMedicamentos;
	}



	public String getRegimenSalud() {
		return regimenSalud;
	}



	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
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



	public String getSire() {
		return sire;
	}



	public void setSire(String sire) {
		this.sire = sire;
	}



	public int getSitio1() {
		return sitio1;
	}



	public void setSitio1(int sitio1) {
		this.sitio1 = sitio1;
	}



	public int getSitio2() {
		return sitio2;
	}



	public void setSitio2(int sitio2) {
		this.sitio2 = sitio2;
	}



	public int getSitio3() {
		return sitio3;
	}



	public void setSitio3(int sitio3) {
		this.sitio3 = sitio3;
	}



	public int getSitio4() {
		return sitio4;
	}



	public void setSitio4(int sitio4) {
		this.sitio4 = sitio4;
	}



	public String getTelefonoContacto() {
		return telefonoContacto;
	}



	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}



	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}



	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}



	public String getTiempo() {
		return tiempo;
	}



	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
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



	public int getUnidadTiempo() {
		return unidadTiempo;
	}



	public void setUnidadTiempo(int unidadTiempo) {
		this.unidadTiempo = unidadTiempo;
	}



	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}



	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}



	public int getVacuna1() {
		return vacuna1;
	}



	public void setVacuna1(int vacuna1) {
		this.vacuna1 = vacuna1;
	}



	public int getVacuna2() {
		return vacuna2;
	}



	public void setVacuna2(int vacuna2) {
		this.vacuna2 = vacuna2;
	}



	public int getVacuna3() {
		return vacuna3;
	}



	public void setVacuna3(int vacuna3) {
		this.vacuna3 = vacuna3;
	}



	public int getVacuna4() {
		return vacuna4;
	}



	public void setVacuna4(int vacuna4) {
		this.vacuna4 = vacuna4;
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



	public int getVia1() {
		return via1;
	}



	public void setVia1(int via1) {
		this.via1 = via1;
	}



	public int getVia2() {
		return via2;
	}



	public void setVia2(int via2) {
		this.via2 = via2;
	}



	public int getVia3() {
		return via3;
	}



	public void setVia3(int via3) {
		this.via3 = via3;
	}



	public int getVia4() {
		return via4;
	}



	public void setVia4(int via4) {
		this.via4 = via4;
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



	public HashMap getHallazgos() {
		return hallazgos;
	}



	public void setHallazgos(HashMap hallazgos) {
		this.hallazgos = hallazgos;
	}



	public String getLugarVac() {
		return lugarVac;
	}



	public void setLugarVac(String lugarVac) {
		this.lugarVac = lugarVac;
	}



	public HashMap getVacunas() {
		return vacunas;
	}



	public void setVacunas(HashMap vacunas) {
		this.vacunas = vacunas;
	}



	public int getNumeroOtrosTipos() {
		return numeroOtrosTipos;
	}



	public void setNumeroOtrosTipos(int numeroOtrosTipos) {
		this.numeroOtrosTipos = numeroOtrosTipos;
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
