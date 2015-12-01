package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

public class FichaSolicitudLaboratoriosForm extends ValidatorForm {


	private int codigoFicha;
	private int codigoFichaLaboratorios;
	private String sire;
	private int evento;
	
	private int codigoServicio;
	private int tipoSolicitud;
	
	// Información General
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
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private String nombreProfesional;
    
    
    ///////////////////////////////
	    
    
	private String examenSolicitado;
	private String muestraEnviada;
	private String hallazgos;
	private String fechaToma;
	private String fechaRecepcion;
	private int muestra;
	private int prueba;
	private int agente;
	private int resultado;
	private String fechaResultado;
	private String valor;
	
	private String estado;
	
	private String valorPrueba;
	
	private HashMap mapaServicios;
	
	
	public void reset()
    {
    	sire="";
    	
    	examenSolicitado = "";
    	muestraEnviada = "";
    	hallazgos = "";
    	fechaToma = "";
    	fechaRecepcion = "";
    	muestra = 0;
    	prueba = 0;
    	agente = 0;
    	resultado = 0;
    	fechaResultado = "";
    	valor = "";
    }
	
	
	/**
     * Metodo para validar los atributos provenientes del formulario
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
            
			reset();
		}
        else if (estado.equals("guardarNuevo")) {
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	String fechaTomaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaToma);
        	String fechaRecepcionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaRecepcion);
        	String fechaResultadoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaResultado);
        	
        	if (!UtilidadFecha.validarFecha(fechaToma)) {
        		
        		errores.add("Campo Fecha de Toma de Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Muestra"));
        	}
        	
        	if (fechaRecepcion.trim().length()>0) {
        		
	        	if (!UtilidadFecha.validarFecha(fechaRecepcion)) {
	        		
	        		errores.add("Campo Fecha de Recepcion no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion"));
	        	}
        	}
        	
        	if (fechaResultado.trim().length()>0) {
	        	if (!UtilidadFecha.validarFecha(fechaResultado)) {
	        		
	        		errores.add("Campo Fecha de Resultado no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado"));
	        	}
        	}

        	if (fechaTomaTransformada.compareTo(fechaActual)>0) {
        		
        		errores.add("Campo Fecha de Toma de Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Muestra", "actual"));
        	}
        	
        	if (fechaRecepcion.trim().length()>0) {
	        	if (fechaTomaTransformada.compareTo(fechaRecepcionTransformada)>0) {
	        		
	        		errores.add("Campo Fecha de Recepcion no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Recepcion", "de Toma de Muestra"));
	        	}
        	}
        	
        	if (fechaRecepcion.trim().length()>0 && fechaResultado.trim().length()>0) {
	        	if (fechaRecepcionTransformada.compareTo(fechaResultadoTransformada)>0) {
	        		
	        		errores.add("Campo Fecha de Resultado no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Resultado", "de Recepcion"));
	        	}
        	}
        }
        
        else if (estado.equals("guardarServicios")) {
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	
        	for (int i=0;i<mapaServicios.size()/11;i++) {
        	
        		String fechaTomaMapa = mapaServicios.get("fechatoma_"+Integer.toString(i)).toString();
        		String fechaRecepcionMapa = mapaServicios.get("fecharecepcion_"+Integer.toString(i)).toString();
        		String fechaResultadoMapa = mapaServicios.get("fecharesultado_"+Integer.toString(i)).toString();
        		
        		String fechaTomaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaMapa);
            	String fechaRecepcionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaRecepcionMapa);
            	String fechaResultadoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaResultadoMapa);
            	
            	if (fechaTomaMapa.trim().length()>0) {
	            	if (!UtilidadFecha.validarFecha(fechaTomaMapa)) {
	            		
	            		errores.add("Campo Fecha de Toma de Muestra "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Muestra "+Integer.toString(i+1)));
	            		estado = "empezar";
	            	}
	            	else if (fechaTomaTransformada.compareTo(fechaActual)>0) {
	            		
	            		errores.add("Campo Fecha de Toma de Muestra "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Muestra "+Integer.toString(i+1), "actual"));
	            		estado = "empezar";
	            	}
            	}
            	
            	if (fechaRecepcionMapa.trim().length()>0) {
            		
    	        	if (!UtilidadFecha.validarFecha(fechaRecepcionMapa)) {
    	        		
    	        		errores.add("Campo Fecha de Recepcion "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion "+Integer.toString(i+1)));
    	        		estado = "empezar";
    	        	}
    	        	else if (fechaRecepcionTransformada.compareTo(fechaActual)>0) {
	            		
	            		errores.add("Campo Fecha de Recepcion "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion "+Integer.toString(i+1), "actual"));
	            		estado = "empezar";
	            	}
            	}
            	
            	if (fechaResultadoMapa.trim().length()>0) {
    	        	if (!UtilidadFecha.validarFecha(fechaResultadoMapa)) {
    	        		
    	        		errores.add("Campo Fecha de Resultado "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado "+Integer.toString(i+1)));
    	        		estado = "empezar";
    	        	}
    	        	else if (fechaResultadoTransformada.compareTo(fechaActual)>0) {
	            		
	            		errores.add("Campo Fecha de Resultado "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Resultado "+Integer.toString(i+1), "actual"));
	            		estado = "empezar";
	            	}
            	}
            	
            	if (fechaTomaMapa.trim().length()>0 && fechaRecepcionMapa.trim().length()>0) {
            		if (UtilidadFecha.validarFecha(fechaTomaMapa) && UtilidadFecha.validarFecha(fechaRecepcionMapa)) {
		            	if (fechaTomaTransformada.compareTo(fechaActual)<=0 &&  fechaRecepcionTransformada.compareTo(fechaActual)<=0) {
		            		if (fechaTomaTransformada.compareTo(fechaRecepcionTransformada)>0) {
		            			errores.add("Campo Fecha de Recepcion "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Recepcion "+Integer.toString(i+1), "de Toma de Muestra "+Integer.toString(i+1)));
		    	        		estado = "empezar";
		            		}
		            	}
            		}
            	}
            	
            	if (fechaRecepcionMapa.trim().length()>0 && fechaResultadoMapa.trim().length()>0) {
            		if (UtilidadFecha.validarFecha(fechaRecepcionMapa) && UtilidadFecha.validarFecha(fechaResultadoMapa)) {
		            	if (fechaRecepcionTransformada.compareTo(fechaActual)<=0 &&  fechaResultadoTransformada.compareTo(fechaActual)<=0) {
		            		if (fechaRecepcionTransformada.compareTo(fechaRecepcionTransformada)>0) {
		            			errores.add("Campo Fecha de Resultado "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Resultado "+Integer.toString(i+1), "de Recepcion "+Integer.toString(i+1)));
		    	        		estado = "empezar";
		            		}
		            	}
            		}
            	}
            	
            	if (fechaTomaMapa.trim().length()>0 && fechaResultadoMapa.trim().length()>0) {
            		if (UtilidadFecha.validarFecha(fechaTomaMapa) && UtilidadFecha.validarFecha(fechaResultadoMapa)) {
		            	if (fechaTomaTransformada.compareTo(fechaActual)<=0 &&  fechaResultadoTransformada.compareTo(fechaActual)<=0) {
		            		if (fechaTomaTransformada.compareTo(fechaRecepcionTransformada)>0) {
		            			errores.add("Campo Fecha de Resultado "+Integer.toString(i+1)+" no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Resultado "+Integer.toString(i+1), "de Toma de Muestra "+Integer.toString(i+1)));
		    	        		estado = "empezar";
		            		}
		            	}
            		}
            	}
        	}
        	
        	
        	
        }
        
        else if (estado.equals("modificar")) {
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	String fechaTomaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaToma);
        	String fechaRecepcionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaRecepcion);
        	String fechaResultadoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaResultado);
        	
        	if (!UtilidadFecha.validarFecha(fechaToma)) {
        		
        		errores.add("Campo Fecha de Toma de Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Muestra"));
        	}
        	
        	if (fechaRecepcion.trim().length()>0) {
        		
	        	if (!UtilidadFecha.validarFecha(fechaRecepcion)) {
	        		
	        		errores.add("Campo Fecha de Recepcion no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion"));
	        	}
        	}
        	
        	if (fechaResultado.trim().length()>0) {
	        	if (!UtilidadFecha.validarFecha(fechaResultado)) {
	        		
	        		errores.add("Campo Fecha de Resultado no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado"));
	        	}
        	}

        	if (fechaTomaTransformada.compareTo(fechaActual)>0) {
        		
        		errores.add("Campo Fecha de Toma de Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Muestra", "actual"));
        	}
        	
        	if (fechaRecepcion.trim().length()>0) {
	        	if (fechaTomaTransformada.compareTo(fechaRecepcionTransformada)>0) {
	        		
	        		errores.add("Campo Fecha de Recepcion no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Recepcion", "de Toma de Muestra"));
	        	}
        	}
        	
        	if (fechaRecepcion.trim().length()>0 && fechaResultado.trim().length()>0) {
	        	if (fechaRecepcionTransformada.compareTo(fechaResultadoTransformada)>0) {
	        		
	        		errores.add("Campo Fecha de Resultado no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Resultado", "de Recepcion"));
	        	}
        	}
        }
        
        return errores;
    }


	public int getAgente() {
		return agente;
	}


	public void setAgente(int agente) {
		this.agente = agente;
	}


	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}


	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
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


	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}


	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}


	public int getCodigoFicha() {
		return codigoFicha;
	}


	public void setCodigoFicha(int codigoFicha) {
		this.codigoFicha = codigoFicha;
	}


	public int getCodigoFichaLaboratorios() {
		return codigoFichaLaboratorios;
	}


	public void setCodigoFichaLaboratorios(int codigoFichaLaboratorios) {
		this.codigoFichaLaboratorios = codigoFichaLaboratorios;
	}


	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}


	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
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


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public int getEvento() {
		return evento;
	}


	public void setEvento(int evento) {
		this.evento = evento;
	}


	public String getExamenSolicitado() {
		return examenSolicitado;
	}


	public void setExamenSolicitado(String examenSolicitado) {
		this.examenSolicitado = examenSolicitado;
	}


	public String getFechaConsultaGeneral() {
		return fechaConsultaGeneral;
	}


	public void setFechaConsultaGeneral(String fechaConsultaGeneral) {
		this.fechaConsultaGeneral = fechaConsultaGeneral;
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


	public String getFechaRecepcion() {
		return fechaRecepcion;
	}


	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}


	public String getFechaResultado() {
		return fechaResultado;
	}


	public void setFechaResultado(String fechaResultado) {
		this.fechaResultado = fechaResultado;
	}


	public String getFechaToma() {
		return fechaToma;
	}


	public void setFechaToma(String fechaToma) {
		this.fechaToma = fechaToma;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getHallazgos() {
		return hallazgos;
	}


	public void setHallazgos(String hallazgos) {
		this.hallazgos = hallazgos;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
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


	public int getMuestra() {
		return muestra;
	}


	public void setMuestra(int muestra) {
		this.muestra = muestra;
	}


	public String getMuestraEnviada() {
		return muestraEnviada;
	}


	public void setMuestraEnviada(String muestraEnviada) {
		this.muestraEnviada = muestraEnviada;
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


	public String getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
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


	public int getPrueba() {
		return prueba;
	}


	public void setPrueba(int prueba) {
		this.prueba = prueba;
	}


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}


	public int getResultado() {
		return resultado;
	}


	public void setResultado(int resultado) {
		this.resultado = resultado;
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


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public String getTipoId() {
		return tipoId;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}


	public int getCodigoServicio() {
		return codigoServicio;
	}


	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	public int getTipoSolicitud() {
		return tipoSolicitud;
	}


	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}


	public String getValorPrueba() {
		return valorPrueba;
	}


	public void setValorPrueba(String valorPrueba) {
		this.valorPrueba = valorPrueba;
	}


	public HashMap getMapaServicios() {
		return mapaServicios;
	}


	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}


			
}
