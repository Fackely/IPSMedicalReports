/*
 * Creado en 15-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;
import com.sysmedica.util.*;
import java.util.HashMap;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.UtilidadFecha;
import org.apache.struts.action.ActionMessage;

/**
 * @author santiago
 *
 */
public class BusquedaFichasForm extends ValidatorForm {
    
    private int criterioTiempo;
    private String asemanaEpidemiologica;
    private String periodoEpidemiologico;
    private String fecha;
    private int anyo;
    private String fechaInicial;
    private String fechaFinal;
    private String diagnostico;
    private String estadoFicha;
    private String estado;
    private HashMap mapaFichas;
    private boolean verPendientes;
    private boolean esColectiva;
    private int codigoNotificacion;
    private String usuarioBusqueda;
    
    private Collection coleccion;
    
    private Collection coleccionBrotes;
    
    private String numeroIdentificacion;
    private String tipoIdentificacion;
    private int codigoPaciente;
    
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
    
    public void reset()
    {
        criterioTiempo = 5;
        asemanaEpidemiologica = "";
        fecha = "";
        fechaInicial = "";
        fechaFinal = "";
        diagnostico = "";
        estadoFicha = "";
        mapaFichas = new HashMap();
    }
    
    public void resetColeccion() {
    	
    	coleccion.clear();
    	coleccionBrotes.clear();
    }
    
    /**
     * @return Returns the diagnostico.
     */
    public String getDiagnostico() {
        return diagnostico;
    }
    /**
     * @param diagnostico The diagnostico to set.
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Returns the fechaFinal.
     */
    public String getFechaFinal() {
        return fechaFinal;
    }
    /**
     * @param fechaFinal The fechaFinal to set.
     */
    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    /**
     * @return Returns the fechaInicial.
     */
    public String getFechaInicial() {
        return fechaInicial;
    }
    /**
     * @param fechaInicial The fechaInicial to set.
     */
    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
    /**
     * @return Returns the verPendientes.
     */
    public boolean isVerPendientes() {
        return verPendientes;
    }
    /**
     * @param verPendientes The verPendientes to set.
     */
    public void setVerPendientes(boolean verPendientes) {
        this.verPendientes = verPendientes;
    }
    /**
     * @return Returns the estadoFicha.
     */
    public String getEstadoFicha() {
        return estadoFicha;
    }
    /**
     * @param estadoFicha The estadoFicha to set.
     */
    public void setEstadoFicha(String estadoFicha) {
        this.estadoFicha = estadoFicha;
    }
    /**
     * @return Returns the anyo.
     */
    public int getAnyo() {
        return anyo;
    }
    /**
     * @param anyo The anyo to set.
     */
    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }
    /**
     * @return Returns the semanaEpidemiologica.
     */
    public String getAsemanaEpidemiologica() {
        return asemanaEpidemiologica;
    }
    /**
     * @param semanaEpidemiologica The semanaEpidemiologica to set.
     */
    public void setAsemanaEpidemiologica(String semanaEpidemiologica) {
        this.asemanaEpidemiologica = semanaEpidemiologica;
    }
    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @return Returns the criterioTiempo.
     */
    public int getCriterioTiempo() {
        return criterioTiempo;
    }
    /**
     * @param criterioTiempo The criterioTiempo to set.
     */
    public void setCriterioTiempo(int criterioTiempo) {
        this.criterioTiempo = criterioTiempo;
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
    /**
     * @return Returns the coleccion.
     */
    public Collection getColeccion() {
        return coleccion;
    }
    /**
     * @param coleccion The coleccion to set.
     */
    public void setColeccion(Collection coleccion) {
        this.coleccion = coleccion;
    }  
    
    public HashMap getMapaFichas() {
        return this.mapaFichas;
    }
    
    /**
     * @param mapaFichas The mapaFichas to set.
     */
    public void setMapaFichas(HashMap mapaFichas) {
        this.mapaFichas = mapaFichas;
    }
    
    /**
     * @return Returns the esColectiva.
     */
    public boolean isEsColectiva() {
        return esColectiva;
    }
    /**
     * @param esColectiva The esColectiva to set.
     */
    public void setEsColectiva(boolean esColectiva) {
        this.esColectiva = esColectiva;
    }
    

	public String getPeriodoEpidemiologico() {
		return periodoEpidemiologico;
	}

	public void setPeriodoEpidemiologico(String periodoEpidemiologico) {
		this.periodoEpidemiologico = periodoEpidemiologico;
	}

	public int getCodigoNotificacion() {
		return codigoNotificacion;
	}

	public void setCodigoNotificacion(int codigoNotificacion) {
		this.codigoNotificacion = codigoNotificacion;
	}

	public String getUsuarioBusqueda() {
		return usuarioBusqueda;
	}

	public void setUsuarioBusqueda(String usuarioBusqueda) {
		this.usuarioBusqueda = usuarioBusqueda;
	}

	public Collection getColeccionBrotes() {
		return coleccionBrotes;
	}

	public void setColeccionBrotes(Collection coleccionBrotes) {
		this.coleccionBrotes = coleccionBrotes;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
}
