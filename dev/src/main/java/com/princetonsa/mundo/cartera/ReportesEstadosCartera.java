package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cartera.ReportesEstadosCarteraDao;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Utilidades;

/**
 *  
 * @author wilson
 */
public class ReportesEstadosCartera 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ReportesEstadosCarteraDao reportesDao;
	
	 /**
     * Para manejar la fecha inicial del rango de Reporte del estado de la Cartera.
     */
    private String fechaInicial;
    /**
     * Para manejar la fecha final del rango de Reporte del estado de la Cartera.
     */
    private String fechaFinal;
    
    /**
     * Tipo de Reporte del estado de la Cartera
     */
    private String tipoReporte;
    
    /**
     * Tipo Contrato para el Campo Convenio: Normal
     */
    private int convenio;
    
    /**
     * Maneja todos los Estados de las cuentas de Cobro.
     */
    private int estadoCuenta;
    
    /**
     * Maneja la Estructura de Empresas.
     */
    private int empresa;
    
    /**
     * es capitado?
     */
    private String esCapitado="";

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
     * 
     */
    private double empresaInstitucion;
    
    /**
     * 
     *
     */
    public void reset()
    {
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.tipoReporte="";
    	this.convenio=ConstantesBD.codigoNuncaValido;
    	this.estadoCuenta=ConstantesBD.codigoNuncaValido;
    	this.empresa=ConstantesBD.codigoNuncaValido;
    	this.esCapitado=ConstantesBD.acronimoNo;
    	this.manejaConversionMoneda=false;
        this.tiposMonedaTagMap=new HashMap();
        this.index=ConstantesBD.codigoNuncaValido;
        this.empresaInstitucion= ConstantesBD.codigoNuncaValidoDoubleNegativo;
    }
    
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
			reportesDao = myFactory.getReportesEstadosCarteraDao();
			wasInited = (reportesDao != null);
		}
		return wasInited;
	}
    
	/**
	 * 
	 * @param mundo
	 * @return
	 */
	public static String armarConsultaEstadoCarteraEvento(ReportesEstadosCartera mundo, String oldQuery)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReportesEstadosCarteraDao().armarConsultaEstadoCarteraEvento(mundo, oldQuery);
	}
	
	/**
	 * 
	 * @param mundo
	 * @param oldQuery
	 * @return
	 */
	public static String armarConsultaTOTALESestadoCarteraEvento(ReportesEstadosCartera mundo, String oldQuery)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReportesEstadosCarteraDao().armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery);
	}
	
	/**
	 * Metodo que ejecuta las consultas que hay en el birt
	 * La primera ejecuta la consulta que devuelve armarConsultaEstadoCarteraEvento 
	 * La segunda ejecuta la consulta que devuelve armarConsultaTOTALESestadoCarteraEvento
	 * @param con
	 * @param query
	 * @return
	 */
	public static HashMap ejecutarConsultaEstadoCarteraEvento(Connection con, String query) 
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReportesEstadosCarteraDao().ejecutarConsultaEstadoCarteraEvento(con, query);
	}
	
	/**
	 * Metodo que carga los datos para generar el archivo plano segun el tipo de reporte
	 * "Detallado por Factura","Detallado por Cxc","Resumido" por estado de cartera evento.
	 * Como el "Detallado por Factura", el "Detallado por Cxc" y el "Resumido" solo cambia en el nombre
	 * de las llaves del convenio y de la factura y con la intencion de no modificar la
	 * consulta en el birt y de no generar una nueva funcion se manda como parametros 
	 * dichas llaves desde el action y se imprime la que se necesite
	 * @param mapaEstadosCartera
	 * @param mapaTotales
	 * @param nombreReporte
	 * @param tipoReporte
	 * @param fechaReporte
	 * @param indiceEmpresa: Resumido "nombreempresa_"; Detallado por Factura "nomempconvcuentacobro_";
	 * Detallado por CxC "nombreempresaconvenio_"
	 * @param indiceConvenio: Resumido "nombreconvenio_"; Detallado por Factura "factura_";
	 * Detallado por CxC "numerocuentacobro_"
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapa(HashMap<String, Object> mapaEstadosCartera, HashMap<String, Object> mapaTotalesEstadosCartera, String nombreReporte, String tipoReporte, String nombreTipoReporte, String fechaReporte, String encabezado, String indiceEmpresa, String indiceConvenio)
	{
		StringBuffer datos = new StringBuffer();
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+nombreTipoReporte+"\n");
		datos.append("PERIODO: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		//For que recorre el mapa con los datosEstadosCartera
		for(int i=0; i<Utilidades.convertirAEntero(mapaEstadosCartera.get("numRegistros")+""); i++)
		{
			//Si es el primer registro se muestra la empresa y se realiza el salto de linea para mostrar los otros datos del primer registro
			if(i == 0)
			{
				//Si el tipo de reporte es resumido se coloca la palabra reservada "Empresa" adelante
				if(tipoReporte.equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
					datos.append("Empresa "+mapaEstadosCartera.get(indiceEmpresa+i)+"\n");
				else
					datos.append(mapaEstadosCartera.get(indiceEmpresa+i)+"\n");
				
				datos.append(mapaEstadosCartera.get(indiceConvenio+i)+", "+mapaEstadosCartera.get("nombreestado_"+i)+", "+mapaEstadosCartera.get("valorinicial_"+i)+", "+mapaEstadosCartera.get("ajustedebito_"+i)+", "+mapaEstadosCartera.get("ajustecredito_"+i)+", "+mapaEstadosCartera.get("pagos_"+i)+", "+mapaEstadosCartera.get("saldo_"+i)+"\n");
			}
			//Si el nombreempresa viene igual se muestra los otros datos sin el nombreempresa
			else if ((mapaEstadosCartera.get(indiceEmpresa+i)+"").equals(mapaEstadosCartera.get(indiceEmpresa+(i-1))+""))
			{
				datos.append(mapaEstadosCartera.get(indiceConvenio+i)+", "+mapaEstadosCartera.get("nombreestado_"+i)+", "+mapaEstadosCartera.get("valorinicial_"+i)+", "+mapaEstadosCartera.get("ajustedebito_"+i)+", "+mapaEstadosCartera.get("ajustecredito_"+i)+", "+mapaEstadosCartera.get("pagos_"+i)+", "+mapaEstadosCartera.get("saldo_"+i)+"\n");
			}
			//Si viene un nuevo nombreempresa
			else
			{
				datos.append("Empresa "+mapaEstadosCartera.get(indiceEmpresa+i)+"\n");
				datos.append(mapaEstadosCartera.get(indiceConvenio+i)+", "+mapaEstadosCartera.get("nombreestado_"+i)+", "+mapaEstadosCartera.get("valorinicial_"+i)+", "+mapaEstadosCartera.get("ajustedebito_"+i)+", "+mapaEstadosCartera.get("ajustecredito_"+i)+", "+mapaEstadosCartera.get("pagos_"+i)+", "+mapaEstadosCartera.get("saldo_"+i)+"\n");
			}
		}
		
		//For que recorre el mapa con los totales
		for(int i=0; i<Utilidades.convertirAEntero(mapaTotalesEstadosCartera.get("numRegistros")+""); i++)
			datos.append("Total Cartera por Estado, "+mapaTotalesEstadosCartera.get("estado_"+i)+", "+mapaTotalesEstadosCartera.get("totalvalorinicial_"+i)+", "+mapaTotalesEstadosCartera.get("totalajustedebito_"+i)+", "+mapaTotalesEstadosCartera.get("totalajustecredito_"+i)+", "+mapaTotalesEstadosCartera.get("totalpagos_"+i)+", "+mapaTotalesEstadosCartera.get("totalsaldo_"+i)+"\n");
		
		return datos;
	}
	
    /**
     * 
     *
     */
	public ReportesEstadosCartera() 
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}

	
	
	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}


	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}


	/**
	 * @return the empresa
	 */
	public int getEmpresa() {
		return empresa;
	}


	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}


	/**
	 * @return the esCapitado
	 */
	public String getEsCapitado() {
		return esCapitado;
	}


	/**
	 * @param esCapitado the esCapitado to set
	 */
	public void setEsCapitado(String esCapitado) {
		this.esCapitado = esCapitado;
	}


	/**
	 * @return the estadoCuenta
	 */
	public int getEstadoCuenta() {
		return estadoCuenta;
	}


	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(int estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}


	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
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

	/**
	 * @return the empresaInstitucion
	 */
	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}
}
