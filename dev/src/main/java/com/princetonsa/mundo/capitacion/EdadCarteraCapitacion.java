package com.princetonsa.mundo.capitacion;

import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.EdadCarteraCapitacionDao;

/**
 * 
 * @author wilson
 *
 */
public class EdadCarteraCapitacion 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EdadCarteraCapitacionDao edadDao;
	
	 /**
     * Para manejar la fecha inicial del rango de Reporte del estado de la Cartera.
     */
    private String fechaCorte;
    
    /**
     * 
     */
    private String tipoReporte;
    
    /**
     * Tipo Contrato para el Campo Convenio: Normal
     */
    private int convenioCapitado;
    
    /**
     * 
     */  
    private int contrato;
    
    /**
     * 
     */
    private int tipoConvenio;
    
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
    private int index;
    
    /**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			edadDao = myFactory.getEdadCarteraCapitacionDao();
			wasInited = (edadDao != null);
		}
		return wasInited;
	}
    
	/**
     * reset
     */
    public void reset()
    {
        this.tipoReporte="";
        this.fechaCorte="";
        this.convenioCapitado=ConstantesBD.codigoNuncaValido;
        this.tipoConvenio=ConstantesBD.codigoNuncaValido;
        this.contrato=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=false;
        this.tiposMonedaTagMap=new HashMap();
        this.index=ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public static String armarConsultaReporte(EdadCarteraCapitacion mundo, String oldQuery) 
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEdadCarteraCapitacionDao().armarConsultaReporte(mundo, oldQuery);
	}
    
    /**
	 * 
	 */
	public EdadCarteraCapitacion() 
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the convenioCapitado
	 */
	public int getConvenioCapitado() {
		return convenioCapitado;
	}

	/**
	 * @param convenioCapitado the convenioCapitado to set
	 */
	public void setConvenioCapitado(int convenioCapitado) {
		this.convenioCapitado = convenioCapitado;
	}

	/**
	 * @return the fechaCorte
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}

	/**
	 * @param fechaCorte the fechaCorte to set
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	/**
	 * @return the tipoConvenio
	 */
	public int getTipoConvenio() {
		return tipoConvenio;
	}

	/**
	 * @param tipoConvenio the tipoConvenio to set
	 */
	public void setTipoConvenio(int tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
    
	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean getManejaConversionMoneda() {
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

}
