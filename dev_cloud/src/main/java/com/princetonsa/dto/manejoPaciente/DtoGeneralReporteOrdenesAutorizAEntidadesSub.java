package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoGeneralReporteOrdenesAutorizAEntidadesSub implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** * Raz�n Social de la Instituci�n */
	private String razonSocial;
	
	/** * NIT de la Instituci�n */
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
     * M�todo constructor de la clase
     */
    public DtoGeneralReporteOrdenesAutorizAEntidadesSub() {
		this.reset();
	}
    
    /**
     * M�todo que inicializa los atributos de la clase
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
	 * M�todo que retorna el valor del atributo razonSocial
	 * @return razonSocial
	 * @author Fabi�n Becerra
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo razonSocial
	 * @param razonSocial valor que se va a almacenar en el atributo razonSocial
	 * @author Fabi�n Becerra
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * M�todo que retorna el valor del atributo usuarioProcesa
	 * @return usuarioProcesa
	 * @author Fabi�n Becerra
	 */
	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo usuarioProcesa
	 * @param usuarioProcesa valor que se va a almacenar en el atributo usuarioProcesa
	 * @author Fabi�n Becerra
	 */
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}

	/**
	 * M�todo que retorna el valor del atributo fechaInicial
	 * @return fechaInicial
	 * @author Fabi�n Becerra
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo fechaInicial
	 * @param fechaInicial valor que se va a almacenar en el atributo fechaInicial
	 * @author Fabi�n Becerra
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M�todo que retorna el valor del atributo fechaFinal
	 * @return fechaFinal
	 * @author Fabi�n Becerra
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo fechaFinal
	 * @param fechaFinal valor que se va a almacenar en el atributo fechaFinal
	 * @author Fabi�n Becerra
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M�todo que retorna el valor del atributo logoDerecha
	 * @return logoDerecha
	 * @author Fabi�n Becerra
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo logoDerecha
	 * @param logoDerecha valor que se va a almacenar en el atributo logoDerecha
	 * @author Fabi�n Becerra
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * M�todo que retorna el valor del atributo logoIzquierda
	 * @return logoIzquierda
	 * @author Fabi�n Becerra
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo logoIzquierda
	 * @param logoIzquierda valor que se va a almacenar en el atributo logoIzquierda
	 * @author Fabi�n Becerra
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	/**
	 * M�todo que retorna el valor del atributo dsTotalOrdenes
	 * @return dsTotalOrdenes
	 * @author Fabi�n Becerra
	 */
	public JRDataSource getDsTotalOrdenes() {
		return dsTotalOrdenes;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo dsTotalOrdenes
	 * @param dsTotalOrdenes valor que se va a almacenar en el atributo dsTotalOrdenes
	 * @author Fabi�n Becerra
	 */
	public void setDsTotalOrdenes(JRDataSource dsTotalOrdenes) {
		this.dsTotalOrdenes = dsTotalOrdenes;
	}

	/**
	 * M�todo que retorna el valor del atributo nit
	 * @return nit
	 * @author Fabi�n Becerra
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nit
	 * @param nit valor que se va a almacenar en el atributo nit
	 * @author Fabi�n Becerra
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
