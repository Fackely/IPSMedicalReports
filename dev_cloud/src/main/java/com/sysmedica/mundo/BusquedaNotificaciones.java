package com.sysmedica.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.BusquedaNotificacionesDao;

public class BusquedaNotificaciones {
	 
	private String semanaEpidemiologica;
    private String fecha;
    private int anyo;
    private String fechaInicial;
    private String fechaFinal;
    private String diagnostico;
    private HashMap mapaNotificaciones;
    private int criterioTiempo;
    private int tipoNotificacion;
    private String usuarioBusqueda;
    
    private BusquedaNotificacionesDao busquedaNotificacionesDao;

    public void reset() {
    	semanaEpidemiologica = "";
    	fecha = "";
    	fechaInicial = "";
    	fechaFinal = "";
    	diagnostico = "";
    	mapaNotificaciones = new HashMap();
    	tipoNotificacion = 0;
    	usuarioBusqueda = "";
    }
    
    /**
	 * Inicializa el acceso a base de datos de este objeto
	 * @param tipoBD
	 * @return
	 */
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			if (myFactory != null)
			{
			    busquedaNotificacionesDao = myFactory.getBusquedaNotificacionesDao();
				wasInited = (busquedaNotificacionesDao != null);
			}
			return wasInited;
	}
	
	public BusquedaNotificaciones()
	{
	    reset ();
		this.init (System.getProperty("TIPOBD"));
	}
	
	
	public Collection consultarNotificaciones(Connection con,String loginUsuario)
    {
        return busquedaNotificacionesDao.consultaNotificaciones(con,fechaInicial,fechaFinal,diagnostico,loginUsuario,usuarioBusqueda,tipoNotificacion);
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

	public String getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}

	public void setSemanaEpidemiologica(String semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}

	public int getCriterioTiempo() {
		return criterioTiempo;
	}

	public void setCriterioTiempo(int criterioTiempo) {
		this.criterioTiempo = criterioTiempo;
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
