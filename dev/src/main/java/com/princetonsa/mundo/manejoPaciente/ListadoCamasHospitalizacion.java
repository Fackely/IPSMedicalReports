package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ListadoCamasHospitalizacionDao;

/**
 * 
 * @author Mauricio Jaramillo
 *
 */

public class ListadoCamasHospitalizacion 
{
	
	private String centroAtencion;
	
	private int estadoCama;
	
	private String ocupadas;
	
	private String pendienteTrasladar;
	
	private String salida;
	
	/**
	 * Mensaje generacion del archivo CSV
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ListadoCamasHospitalizacionDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public ListadoCamasHospitalizacion()
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
     * Reset del Mundo
     *
     */
	public void reset()
	{
		this.centroAtencion = "";
		this.estadoCama = ConstantesBD.codigoNuncaValido;
		this.ocupadas = "";
		this.pendienteTrasladar = "";
		this.salida = "";
	}
	
	/**
	 * 
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getListadoCamasHospitalizacionDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}

	/**
     * @param mundo
     * @param oldQuery
     * @return
     */
    public static HashMap listadoCamasHospitalizacion(Connection con, ListadoCamasHospitalizacion mundo, String condiciones) 
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoCamasHospitalizacionDao().listadoCamasHospitalizacion(con, mundo, condiciones);
	}
	
    /**
	 * Método para organizar los datos para el archivo plano
	 * de Mercadeo de Camas
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param usuario
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaMercadeo(HashMap<Object, Object> mapa, String nombreReporte, String usuario, String encabezado)
	{
		StringBuffer datosArchivos = new StringBuffer();
		datosArchivos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datosArchivos.append("FECHA: "+UtilidadFecha.getFechaActual()+"\n");
		datosArchivos.append("USUARIO: "+usuario+"\n");
		datosArchivos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
			datosArchivos.append(mapa.get("convenio_"+i)+","+mapa.get("ocupacion_"+i)+","+mapa.get("porcentaje_"+i)+"\n");
		
		return datosArchivos;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public int getEstadoCama() {
		return estadoCama;
	}

	/**
	 * 
	 * @param estadoCama
	 */
	public void setEstadoCama(int estadoCama) {
		this.estadoCama = estadoCama;
	}

	/**
	 * 
	 * @return
	 */
	public ListadoCamasHospitalizacionDao getObjetoDao() {
		return objetoDao;
	}

	/**
	 * 
	 * @param objetoDao
	 */
	public void setObjetoDao(ListadoCamasHospitalizacionDao objetoDao) {
		this.objetoDao = objetoDao;
	}

	/**
	 * 
	 * @return
	 */
	public String getOcupadas() {
		return ocupadas;
	}

	/**
	 * 
	 * @param ocupadas
	 */
	public void setOcupadas(String ocupadas) {
		this.ocupadas = ocupadas;
	}

	/**
	 * 
	 * @return
	 */
	public String getPendienteTrasladar() {
		return pendienteTrasladar;
	}

	/**
	 * 
	 * @param pendienteTrasladar
	 */
	public void setPendienteTrasladar(String pendienteTrasladar) {
		this.pendienteTrasladar = pendienteTrasladar;
	}

	/**
	 * 
	 * @return
	 */
	public String getSalida() {
		return salida;
	}

	/**
	 * 
	 * @param salida
	 */
	public void setSalida(String salida) {
		this.salida = salida;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
}