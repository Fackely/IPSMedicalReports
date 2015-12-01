/*
 * Creado en 15-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo;

import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.BusquedaFichasDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.UtilidadBD;

/**
 * @author santiago
 *
 */  
public class BusquedaFichas {

	private int criterioTiempo;
    private String semanaEpidemiologica;
    private String fecha;
    private int anyo;
    private String fechaInicial;
    private String fechaFinal;
    private String diagnostico;
    private String estado;
    private boolean verPendientes;
    private Collection coleccion;
    private HashMap mapaFichas;
    private int codigoNotificacion;
    private String usuarioBusqueda;
    private int codigoPaciente;
    
    private boolean esColectiva;
    
    private String numeroIdentificacion;
    private String tipoIdentificacion;
    private int codigoPersona;
    
    private BusquedaFichasDao busquedaFichasDao;
    
    public void reset()
    {
        semanaEpidemiologica = "";
        fecha = "";
        fechaInicial = "";
        fechaFinal = "";
        diagnostico = "";
        estado = "";
        verPendientes = false;
        mapaFichas = new HashMap();
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
			    busquedaFichasDao = myFactory.getBusquedaFichasDao();
				wasInited = (busquedaFichasDao != null);
			}
			return wasInited;
	}
	
	public BusquedaFichas()
	{
	    reset ();
		this.init (System.getProperty("TIPOBD"));
	}
    
    public Collection consultaFichas(Connection con,String loginUsuario)
    {
        return busquedaFichasDao.consultaFichas(con,fechaInicial,fechaFinal,diagnostico,estado,verPendientes,loginUsuario,usuarioBusqueda);    	
    }
     
    
    public Collection consultaFichasPendientes(Connection con,String loginUsuario)
    {
        return busquedaFichasDao.consultaFichasPendientes(con,loginUsuario);
    }
    
    
    public Collection consultaBrotesPendientes(Connection con,String loginUsuario)
    {
    	return busquedaFichasDao.consultaBrotesPendientes(con,loginUsuario);
    }
    
    public Collection consultaBrotes(Connection con, String loginUsuario) 
	{
		return busquedaFichasDao.consultaBrote(con,Integer.parseInt(diagnostico),usuarioBusqueda,loginUsuario,fechaInicial,fechaFinal,Integer.parseInt(estado));
	}
    
    
    
    public ResultSet consultaFichasPorPaciente(Connection con, 
									    		int codigoPaciente, 
									    		String diagnostico,
									    		String codigoDx)
    {
    	return busquedaFichasDao.consultaFichasPorPaciente(con,codigoPaciente,diagnostico,codigoDx);
    }
    
    
    
    public int notificarFicha(Connection con,								
								String codigoUsuario)
    {
        int colectiva = 2;
        
        if (esColectiva) {
            colectiva = 2;
        }
        else {
            colectiva = 1;
        }
        return busquedaFichasDao.notificarFicha(con,this.mapaFichas,codigoUsuario,colectiva);
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
     * @return Returns the semanaEpidemiologica.
     */
    public String getSemanaEpidemiologica() {
        return semanaEpidemiologica;
    }
    /**
     * @param semanaEpidemiologica The semanaEpidemiologica to set.
     */
    public void setSemanaEpidemiologica(String semanaEpidemiologica) {
        this.semanaEpidemiologica = semanaEpidemiologica;
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
    /**
     * @return Returns the mapaFichas.
     */
    public HashMap getMapaFichas() {
        return mapaFichas;
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

	public int getCriterioTiempo() {
		return criterioTiempo;
	}

	public void setCriterioTiempo(int criterioTiempo) {
		this.criterioTiempo = criterioTiempo;
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

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
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

	public int getCodigoPersona() {
		return codigoPersona;
	}

	public void setCodigoPersona(int codigoPersona) {
		this.codigoPersona = codigoPersona;
	}
}