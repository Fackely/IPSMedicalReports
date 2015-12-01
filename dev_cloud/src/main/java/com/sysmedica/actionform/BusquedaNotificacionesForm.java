package com.sysmedica.actionform;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import com.sysmedica.util.CalendarioEpidemiologico;
import com.sysmedica.util.SemanaEpidemiologica;


public class BusquedaNotificacionesForm extends ValidatorForm {
	
	private String asemanaEpidemiologica;
    private String periodoEpidemiologico;
    private int tipoNotificacion;
    private String fecha;
    private int anyo;
    private String fechaInicial;
    private String fechaFinal;
    private String diagnostico;
    private boolean esColectiva;
    private HashMap mapaNotificaciones;
    private String estado;
    private int criterioTiempo;
    private Collection coleccion;
    private String usuarioBusqueda;
    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
        	reset();
        }
        else if (estado.equals("consultar")) {
        	
        	if (criterioTiempo==1||criterioTiempo==2) {
        		
        		Calendar calendario = new GregorianCalendar();
        		Date hoy = new Date();
        		calendario.setTime(hoy);
        		
        		int a = calendario.get(GregorianCalendar.YEAR);
        		
        		if (anyo<1900||anyo>a) {
        			errores.add("Valor de año no valido", new ActionMessage("error.epidemiologia.anyoBusqueda"));
        		}
        	}
        	else if (criterioTiempo==3) {
        		
        		String fechaBusquedaTransformada=UtilidadFecha.conversionFormatoFechaABD(fecha);
        		String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        		
        		if (!UtilidadFecha.validarFecha(fecha)) {
        			errores.add("Valor de fecha no valido", new ActionMessage("errors.formatoFechaInvalido","de busqueda"));
        		}
        		else if (fechaBusquedaTransformada.compareTo(fechaActual)>0) {
        			errores.add("La fecha de búsqueda no puede ser superior a la fecha actual", new ActionMessage("errors.fechaPosteriorIgualActual","de busqueda", "actual"));
        		}
        	}
        	else if (criterioTiempo==4) {
        		
        		String fecha1BusquedaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
        		String fecha2BusquedaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
        		String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        		
        		if (!UtilidadFecha.validarFecha(fechaInicial)) {
        			errores.add("Valor de fecha inicial no valido", new ActionMessage("errors.formatoFechaInvalido","de busqueda inicial"));
        		}
        		else if (fecha1BusquedaTransformada.compareTo(fechaActual)>0) {
        			errores.add("La fecha de búsqueda inicial no puede ser superior a la fecha actual", new ActionMessage("errors.fechaPosteriorIgualActual","de busqueda inicial", "actual"));
        		}
        		
        		if (!UtilidadFecha.validarFecha(fechaFinal)) {
        			errores.add("Valor de fecha final no valido", new ActionMessage("errors.formatoFechaInvalido","de busqueda final"));
        		}
        		else if (fecha2BusquedaTransformada.compareTo(fechaActual)>0) {
        			errores.add("La fecha de búsqueda final no puede ser superior a la fecha actual", new ActionMessage("errors.fechaPosteriorIgualActual","de busqueda final", "actual"));
        		}
        		
        		else if (fecha1BusquedaTransformada.compareTo(fecha2BusquedaTransformada)>0) {
        			errores.add("La fecha de búsqueda inicial no puede ser superior a la fecha de busqueda final", new ActionMessage("errors.fechaPosteriorIgualActual","de busqueda inicial", "de busqueda final"));
        		}
        	}
        }
        
        ajustarFechas();
        
        return errores;
    }
    

	public void reset() {
    	
		criterioTiempo = 5;
    	asemanaEpidemiologica = "";
    	fecha = ""; 
        fechaInicial = "";
        fechaFinal = "";
        diagnostico = "0";
        esColectiva = false;
        mapaNotificaciones = new HashMap();
    }
    
	public int getAnyo() {
		return anyo;
	}
	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}
	public String getDiagnostico() {
		return diagnostico;
	}
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	public boolean isEsColectiva() {
		return esColectiva;
	}
	public void setEsColectiva(boolean esColectiva) {
		this.esColectiva = esColectiva;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
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
	public HashMap getMapaNotificaciones() {
		return mapaNotificaciones;
	}
	public void setMapaNotificaciones(HashMap mapaNotificaciones) {
		this.mapaNotificaciones = mapaNotificaciones;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getAsemanaEpidemiologica() {
		return asemanaEpidemiologica;
	}

	public void setAsemanaEpidemiologica(String asemanaEpidemiologica) {
		this.asemanaEpidemiologica = asemanaEpidemiologica;
	}
    
	public int getCriterioTiempo() {
		return criterioTiempo;
	}

	public void setCriterioTiempo(int criterioTiempo) {
		this.criterioTiempo = criterioTiempo;
	}

	public Collection getColeccion() {
		return coleccion;
	}

	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}
	
	
	private void ajustarFechas() {
    	
    	if (criterioTiempo==1)
        {
            SemanaEpidemiologica semana = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(Integer.parseInt(asemanaEpidemiologica),anyo);
            fechaInicial = semana.getFechaInicial();
            fechaFinal = semana.getFechaFinal();
        }
        else if (criterioTiempo==2)
        {
        	int periodo = Integer.parseInt(periodoEpidemiologico);
        	int numeroSemanaInicial = (periodo*4)-3;
        	int numeroSemanaFinal = periodo*4;
        	
        	SemanaEpidemiologica semanaInicial = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(numeroSemanaInicial,anyo);
        	SemanaEpidemiologica semanaFinal = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(numeroSemanaFinal,anyo);
        	
            fechaInicial = semanaInicial.getFechaInicial();
            fechaFinal = semanaFinal.getFechaFinal();
        }
        else if (criterioTiempo==3) {
        	
        	fechaInicial = fecha;
        	fechaFinal = fecha;
        }
        else if (criterioTiempo==5) {
        	
        }
    }


	public String getPeriodoEpidemiologico() {
		return periodoEpidemiologico;
	}


	public void setPeriodoEpidemiologico(String periodoEpidemiologico) {
		this.periodoEpidemiologico = periodoEpidemiologico;
	}


	public int getTipoNotificacion() {
		return tipoNotificacion;
	}


	public void setTipoNotificacion(int tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}


	public String getUsuarioBusqueda() {
		return usuarioBusqueda;
	}


	public void setUsuarioBusqueda(String usuarioBusqueda) {
		this.usuarioBusqueda = usuarioBusqueda;
	}
}
