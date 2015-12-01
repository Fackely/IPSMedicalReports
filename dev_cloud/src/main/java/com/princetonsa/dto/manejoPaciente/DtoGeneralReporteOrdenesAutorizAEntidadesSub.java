package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoGeneralReporteOrdenesAutorizAEntidadesSub implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** * Razón Social de la Institución */
	private String razonSocial;
	
	/** * NIT de la Institución */
	private String nit;
	
	/** * Usuario que genera el reporte */
	private String usuarioProcesa;
	
	/** * Fecha de Inicio de la consulta */
	private Date fechaInicial;
	
	/** * Fecha Final de la consulta */
	private Date fechaFinal;
	
	/** * Ruta para generar el logo a la derecha */
	private String logoDerecha;
	
	/** * Ruta para generar el logo a la izquierda */
	private String logoIzquierda;
	
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsTotalOrdenes;

    private int numeroEstados;
    
    
    //----------PARA ARCHIVOS PLANOS---------------------
    
    /** * Lista de estados de las autorizaciones seleccionados */
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaEstados;
	
	/** * Objeto jasper para el subreporte del consolidado por estados */
	private JRDataSource dsTotalEstados;
	
	
	private String nombreEntidadSub;
	//---------------------------------------------------
    
    
     
    
    /**
     * Método constructor de la clase
     */
    public DtoGeneralReporteOrdenesAutorizAEntidadesSub() {
		this.reset();
	}
    
    /**
     * Método que inicializa los atributos de la clase
     */
    private void reset()
    {
    	this.razonSocial				= "";
    	this.nit						= "";
    	this.usuarioProcesa				= "";
    	this.fechaInicial				= null;
    	this.fechaFinal					= null;
    	this.logoDerecha				= "";
    	this.logoIzquierda				= "";
    	this.dsTotalOrdenes	= null;
    	this.listaEstados				= new ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub>();
    }
    
    
	/**
	 * Método que retorna el valor del atributo razonSocial
	 * @return razonSocial
	 * @author Fabián Becerra
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo razonSocial
	 * @param razonSocial valor que se va a almacenar en el atributo razonSocial
	 * @author Fabián Becerra
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * Método que retorna el valor del atributo usuarioProcesa
	 * @return usuarioProcesa
	 * @author Fabián Becerra
	 */
	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo usuarioProcesa
	 * @param usuarioProcesa valor que se va a almacenar en el atributo usuarioProcesa
	 * @author Fabián Becerra
	 */
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}

	/**
	 * Método que retorna el valor del atributo fechaInicial
	 * @return fechaInicial
	 * @author Fabián Becerra
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo fechaInicial
	 * @param fechaInicial valor que se va a almacenar en el atributo fechaInicial
	 * @author Fabián Becerra
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * Método que retorna el valor del atributo fechaFinal
	 * @return fechaFinal
	 * @author Fabián Becerra
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo fechaFinal
	 * @param fechaFinal valor que se va a almacenar en el atributo fechaFinal
	 * @author Fabián Becerra
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * Método que retorna el valor del atributo logoDerecha
	 * @return logoDerecha
	 * @author Fabián Becerra
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo logoDerecha
	 * @param logoDerecha valor que se va a almacenar en el atributo logoDerecha
	 * @author Fabián Becerra
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * Método que retorna el valor del atributo logoIzquierda
	 * @return logoIzquierda
	 * @author Fabián Becerra
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo logoIzquierda
	 * @param logoIzquierda valor que se va a almacenar en el atributo logoIzquierda
	 * @author Fabián Becerra
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	/**
	 * Método que retorna el valor del atributo dsTotalOrdenes
	 * @return dsTotalOrdenes
	 * @author Fabián Becerra
	 */
	public JRDataSource getDsTotalOrdenes() {
		return dsTotalOrdenes;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo dsTotalOrdenes
	 * @param dsTotalOrdenes valor que se va a almacenar en el atributo dsTotalOrdenes
	 * @author Fabián Becerra
	 */
	public void setDsTotalOrdenes(JRDataSource dsTotalOrdenes) {
		this.dsTotalOrdenes = dsTotalOrdenes;
	}

	/**
	 * Método que retorna el valor del atributo nit
	 * @return nit
	 * @author Fabián Becerra
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nit
	 * @param nit valor que se va a almacenar en el atributo nit
	 * @author Fabián Becerra
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	public void setNumeroEstados(int numeroEstados) {
		this.numeroEstados = numeroEstados;
	}

	public int getNumeroEstados() {
		return numeroEstados;
	}

	public void setListaEstados(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaEstados() {
		return listaEstados;
	}

	public void setDsTotalEstados(JRDataSource dsTotalEstados) {
		this.dsTotalEstados = dsTotalEstados;
	}

	public JRDataSource getDsTotalEstados() {
		return dsTotalEstados;
	}

	public void setNombreEntidadSub(String nombreEntidadSub) {
		this.nombreEntidadSub = nombreEntidadSub;
	}

	public String getNombreEntidadSub() {
		return nombreEntidadSub;
	}

	

	
	
}
