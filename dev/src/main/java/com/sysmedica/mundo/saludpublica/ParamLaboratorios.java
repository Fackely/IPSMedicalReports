package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.ParamLaboratoriosDao;
import com.sysmedica.dao.sqlbase.SqlBaseParamLaboratorios;

public class ParamLaboratorios {

	private Logger logger = Logger.getLogger(ParamLaboratorios.class);
	
	private int evento;
//	private int tipoSolicitud;
	private HashMap opcionesMuestra;
	private HashMap opcionesPrueba;
	private HashMap opcionesAgente;
	private HashMap opcionesResultado;
	
	private Collection coleccion;
	private int codigoCups;
	private int codigoServicioEliminar;
	private int numeroElementos;
	
	private HashMap mapaServicios;
	
	private String codigosServiciosInsertados;
	
	private int codigoAxiomaNueva;
	private int codigoEspecialidadNueva;
	private String descripcionNueva;
	
	ParamLaboratoriosDao paramLaboratoriosDao;
	
	public ParamLaboratorios() {
        reset();
        init(System.getProperty("TIPOBD"));
    }
	
	
	/**
     * Metodo para resetear los atributos 
     *
     */
    public void reset() {
    	
    	evento = 0;
    	codigoCups = 0;
    	
    	try {
    		coleccion.clear();
    	}
    	catch (NullPointerException npe) {
    		
    	}
    }
    
    
    /**
     * Inicializa el acceso a Base de Datos de este objeto
     * @param tipoBD
     * @return
     */
    public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			paramLaboratoriosDao = myFactory.getParamLaboratoriosDao();
			wasInited = (paramLaboratoriosDao != null);
		}
		return wasInited;
	}
    
    
    public int insertarMuestra(Connection con)
    {
    	return paramLaboratoriosDao.insertarMuestra(con,opcionesMuestra,evento);
    }
    
    
    public int insertarPrueba(Connection con)
    {
    	return paramLaboratoriosDao.insertarPrueba(con,opcionesPrueba,evento);
    }
    
    
    public int insertarAgente(Connection con)
    {
    	return paramLaboratoriosDao.insertarAgente(con,opcionesAgente,evento);
    }
    
    
    public int insertarResultado(Connection con)
    {
    	return paramLaboratoriosDao.insertarResultado(con,opcionesResultado,evento);
    }
    
    /*
    public int actualizarTipoSolicitud(Connection con)
	{
		return SqlBaseParamLaboratorios.actualizarTipoSolicitud(con,evento,tipoSolicitud);
	}
    */
    
    public void cargarDatos(Connection con,int codigoEnfNotificable)
    {
    	ResultSet rs = paramLaboratoriosDao.consultarMuestras(con,codigoEnfNotificable);
    	ResultSet rs2 = paramLaboratoriosDao.consultarPruebas(con,codigoEnfNotificable);
    	ResultSet rs3 = paramLaboratoriosDao.consultarAgentes(con,codigoEnfNotificable);
    	ResultSet rs4 = paramLaboratoriosDao.consultarResultados(con,codigoEnfNotificable);
    	
    //	this.setTipoSolicitud(paramLaboratoriosDao.consultarTipoSolicitud(con,codigoEnfNotificable));
    	
    	HashMap mapaMuestras = new HashMap();
    	HashMap mapaPruebas = new HashMap();
    	HashMap mapaAgentes = new HashMap();
    	HashMap mapaResultados = new HashMap();
    	
    	int i = 0;
    	
    	try {
    		while (rs.next()) {
    			
    			mapaMuestras.put("muestra_"+rs.getInt("codigoMuestra"),"true");
    		}
    		
    		this.setOpcionesMuestra(mapaMuestras);
    		
    		while (rs2.next()) {
    			mapaPruebas.put("prueba_"+rs2.getInt("codigoPrueba"),"true");
    			
    			if (rs2.getInt("tipoSolicitud")==1) {
    				mapaPruebas.put("solicitud_"+rs2.getInt("codigoPrueba"),"1");
    			}
    			else {
    				mapaPruebas.put("solicitud_"+rs2.getInt("codigoPrueba"),"2");
    			}
    		}
    		
    		this.setOpcionesPrueba(mapaPruebas);
    		
    		
    		while (rs3.next()) {
    			
    			mapaAgentes.put("agente_"+rs3.getInt("codigoAgente"),"true");
    		}
    		
    		this.setOpcionesAgente(mapaAgentes);
    		
    		
    		while (rs4.next()) {
    			
    			mapaResultados.put("resultado_"+rs4.getInt("codigoResultado"),"true");
    		}
    		
    		this.setOpcionesResultado(mapaResultados);
    	}
		catch (SQLException sqle) {
	         
			Log4JManager.error(sqle.getMessage());
	    }
    }
    
    
    
    public void cargarServicios(Connection con)
    {
    	ResultSet rs = paramLaboratoriosDao.consultarServiciosParametrizados(con,evento);
    	
    	codigosServiciosInsertados = "";
    	    	
    	HashMap mapa = new HashMap();
    	int i=0;
    	
    	try {
	    	while (rs.next())
	    	{
	    		mapa.put("codigo_"+Integer.toString(i),Integer.toString(rs.getInt("especialidad"))+"-"+Integer.toString(rs.getInt("codigo")));
	    		mapa.put("descripcion_"+Integer.toString(i),rs.getString("descripcion"));
	    		mapa.put("tiposolicitud_"+Integer.toString(i),Integer.toString(rs.getInt("tiposolicitud")));
	    		mapa.put("activo_"+Integer.toString(i),Integer.toString(rs.getInt("activo")));
	    		
	    		String codInsertado = mapa.get("codigo_"+i).toString().split("-")[1];
	    		codigosServiciosInsertados += codInsertado+",";
	    		
	    		i++;
	    	}
	    	
	    	this.setMapaServicios(mapa);
	    	this.setNumeroElementos(i);
    	}
    	catch (SQLException sqle) {
    		
    		Log4JManager.error("Error cargando los servicios parametrizados : "+sqle.getMessage());
    	}
    }
    
    public boolean cargarServicioEspecifico(Connection con)
    {
    	boolean resultado = false;
    	codigosServiciosInsertados = "";
    	
    	ResultSet rs = paramLaboratoriosDao.consultaRapidaServicios(con,codigoCups);
    	ResultSet rs2 = paramLaboratoriosDao.consultarServiciosParametrizados(con,evento);
    	
    	HashMap mapa = new HashMap();
    	HashMap mapaAuxiliar = this.mapaServicios;
    	int tamMapaAux = mapaAuxiliar.size()/4;
    	int i = 0;
    	
    	try {
	    	if (rs.next()) {
	    		
	    		mapa.put("codigo_"+Integer.toString(i),Integer.toString(rs.getInt("especialidad"))+"-"+Integer.toString(rs.getInt("codigo")));
	    		mapa.put("descripcion_"+Integer.toString(i),rs.getString("descripcion"));
	    		mapa.put("tiposolicitud_"+Integer.toString(i),2);
	    		mapa.put("activo_"+Integer.toString(i),1);
	    		
	    		String codInsertado = mapa.get("codigo_"+i).toString().split("-")[1];
	    		codigosServiciosInsertados += codInsertado+",";
	    		
	    		i++;
	    		
	    		resultado = true;
	    	}
	    	
	    	for (int j=0;j<tamMapaAux;j++) {
	    		
	    		mapa.put("codigo_"+Integer.toString(j+1),mapaAuxiliar.get("codigo_"+Integer.toString(j)));
	    		mapa.put("descripcion_"+Integer.toString(j+1),mapaAuxiliar.get("descripcion_"+Integer.toString(j)));
	    		mapa.put("tiposolicitud_"+Integer.toString(j+1),mapaAuxiliar.get("tiposolicitud_"+Integer.toString(j)));
	    		mapa.put("activo_"+Integer.toString(j+1),mapaAuxiliar.get("activo_"+Integer.toString(j)));
	    		
	    		int k = j+1;
	    		
	    		String codInsertado = mapa.get("codigo_"+k).toString().split("-")[1];
	    		codigosServiciosInsertados += codInsertado+",";
	    	}
	    	
	    	this.setMapaServicios(mapa);
	    	this.setNumeroElementos(i+tamMapaAux);
    	}
    	catch (SQLException sqle) {
    		
    		Log4JManager.error("Error cargando el servicio : "+sqle.getMessage());
    	}
    	
    	return resultado;
    }
    
    
    
    
    public void agregarServicioBusqueda()
    {
    	HashMap mapa = new HashMap();
    	HashMap mapaAuxiliar = this.mapaServicios;
    	int tamMapaAux = mapaAuxiliar.size()/4;
    	
    	mapa.put("codigo_0",this.codigoEspecialidadNueva+"-"+this.codigoAxiomaNueva);
		mapa.put("descripcion_0",this.descripcionNueva);
		mapa.put("tiposolicitud_0",2);
		mapa.put("activo_0",1);
		
		codigosServiciosInsertados = Integer.toString(this.codigoAxiomaNueva)+",";
		
		for (int j=0;j<tamMapaAux;j++) {
    		
    		mapa.put("codigo_"+Integer.toString(j+1),mapaAuxiliar.get("codigo_"+Integer.toString(j)));
    		mapa.put("descripcion_"+Integer.toString(j+1),mapaAuxiliar.get("descripcion_"+Integer.toString(j)));
    		mapa.put("tiposolicitud_"+Integer.toString(j+1),mapaAuxiliar.get("tiposolicitud_"+Integer.toString(j)));
    		mapa.put("activo_"+Integer.toString(j+1),mapaAuxiliar.get("activo_"+Integer.toString(j)));
    		
    		int k = j+1;
    		
    		String codInsertado = mapa.get("codigo_"+k).toString().split("-")[1];
    		codigosServiciosInsertados += codInsertado+",";
    	}
		
		this.setMapaServicios(mapa);
    	this.setNumeroElementos(mapa.size()/4);
    }
    
    
    public void eliminarServicio()
    {
    	HashMap mapa = new HashMap();
    	int tamMapa = mapaServicios.size()/4;
    	int filaAEliminar = -1;
    	
		for (int j=0;j<tamMapa;j++) {
    		
			String codigoMapa = mapaServicios.get("codigo_"+Integer.toString(j)).toString().split("-")[1];
			
			if (codigoServicioEliminar==Integer.parseInt(codigoMapa)) {
				
				filaAEliminar = j;
				
				break;
			}
    	}
		
		if (filaAEliminar>-1) {
			
			int k = 0;
			
			for (int i=0;i<mapaServicios.size()/4;i++) {
				
				if (i!=filaAEliminar) {
					
					mapa.put("codigo_"+Integer.toString(k),mapaServicios.get("codigo_"+Integer.toString(i)));
		    		mapa.put("descripcion_"+Integer.toString(k),mapaServicios.get("descripcion_"+Integer.toString(i)));
		    		mapa.put("tiposolicitud_"+Integer.toString(k),mapaServicios.get("tiposolicictud_"+Integer.toString(i)));
		    		mapa.put("activo_"+Integer.toString(k),mapaServicios.get("activo_"+Integer.toString(i)));
		    		
		    		codigosServiciosInsertados += mapa.get("codigo_"+Integer.toString(i))+",";
		    		
		    		k++;
				}
			}
			
			this.setMapaServicios(mapa);
	    	this.setNumeroElementos(mapa.size()/4);
		}
    }
    
    
    
    
    public int insertarServicios(Connection con, int codigoInstitucion)
	{
		return paramLaboratoriosDao.insertarServicios(con,mapaServicios,evento,codigoInstitucion);
	}


	public int getEvento() {
		return evento;
	}


	public void setEvento(int evento) {
		this.evento = evento;
	}


	public HashMap getOpcionesAgente() {
		return opcionesAgente;
	}


	public void setOpcionesAgente(HashMap opcionesAgente) {
		this.opcionesAgente = opcionesAgente;
	}


	public HashMap getOpcionesMuestra() {
		return opcionesMuestra;
	}


	public void setOpcionesMuestra(HashMap opcionesMuestra) {
		this.opcionesMuestra = opcionesMuestra;
	}


	public HashMap getOpcionesPrueba() {
		return opcionesPrueba;
	}


	public void setOpcionesPrueba(HashMap opcionesPrueba) {
		this.opcionesPrueba = opcionesPrueba;
	}


	public HashMap getOpcionesResultado() {
		return opcionesResultado;
	}


	public void setOpcionesResultado(HashMap opcionesResultado) {
		this.opcionesResultado = opcionesResultado;
	}


	public Collection getColeccion() {
		return coleccion;
	}


	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}


	public int getCodigoCups() {
		return codigoCups;
	}


	public void setCodigoCups(int codigoCups) {
		this.codigoCups = codigoCups;
	}


	public int getCodigoServicioEliminar() {
		return codigoServicioEliminar;
	}


	public void setCodigoServicioEliminar(int codigoServicioEliminar) {
		this.codigoServicioEliminar = codigoServicioEliminar;
	}


	public HashMap getMapaServicios() {
		return mapaServicios;
	}


	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}


	public int getNumeroElementos() {
		return numeroElementos;
	}


	public void setNumeroElementos(int numeroElementos) {
		this.numeroElementos = numeroElementos;
	}


	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}


	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}


	public int getCodigoAxiomaNueva() {
		return codigoAxiomaNueva;
	}


	public void setCodigoAxiomaNueva(int codigoAxiomaNueva) {
		this.codigoAxiomaNueva = codigoAxiomaNueva;
	}


	public int getCodigoEspecialidadNueva() {
		return codigoEspecialidadNueva;
	}


	public void setCodigoEspecialidadNueva(int codigoEspecialidadNueva) {
		this.codigoEspecialidadNueva = codigoEspecialidadNueva;
	}


	public String getDescripcionNueva() {
		return descripcionNueva;
	}


	public void setDescripcionNueva(String descripcionNueva) {
		this.descripcionNueva = descripcionNueva;
	}

/*
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}


	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
   */
   
}
