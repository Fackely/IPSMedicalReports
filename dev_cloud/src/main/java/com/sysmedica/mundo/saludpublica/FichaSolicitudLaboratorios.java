package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;


import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaSolicitudLaboratoriosDao;

public class FichaSolicitudLaboratorios {

	private Logger logger = Logger.getLogger(FichaSolicitudLaboratorios.class);
	FichaSolicitudLaboratoriosDao fichaLabsDao;
	
	private int codigoFicha;
	private int codigoFichaLaboratorios;
	private String sire;
    
	private String examenSolicitado;
	private String muestraEnviada;
	private String hallazgos;
	private String fechaToma;
	private String fechaRecepcion;
	private int muestra;
	private int prueba;
	private int agente;
	private int resultado;
	private String fechaResultado;
	private String valor;
	
	private HashMap mapaServicios;
	
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
			fichaLabsDao = myFactory.getFichaSolicitudLaboratoriosDao();
			wasInited = (fichaLabsDao != null);
		}
		return wasInited;
	}
    
    
    public void reset()
    {
    	sire="";
    	
    	examenSolicitado = "";
    	muestraEnviada = "";
    	hallazgos = "";
    	fechaToma = "";
    	fechaRecepcion = "";
    	muestra = 0;
    	prueba = 0;
    	agente = 0;
    	resultado = 0;
    	fechaResultado = "";
    	valor = "";
    }
    
    
    public int insertarFicha(Connection con) 
    {
    	return fichaLabsDao.insertarFicha(con,
											codigoFicha,
											codigoFichaLaboratorios,
											sire,
										    
											examenSolicitado,
											muestraEnviada,
											hallazgos,
											fechaToma,
											fechaRecepcion,
											muestra,
											prueba,
											agente,
											resultado,
											fechaResultado,
											valor);
    }
    
    
    public int insertarServicios(Connection con)
    {
    	return fichaLabsDao.insertarServicioFicha(con, 
													mapaServicios,
													codigoFicha,
													codigoFichaLaboratorios,
													examenSolicitado,
													muestraEnviada,
													hallazgos);
    }
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaLabsDao.modificarFicha(con,
											examenSolicitado,
											muestraEnviada,
											hallazgos,
											fechaToma,
											fechaRecepcion,
											muestra,
											prueba,
											agente,
											resultado,
											fechaResultado,
											valor,
											codigoFichaLaboratorios);
    }
    
    
    
    
    public void cargarDatos(Connection con, int codigo)
    {
    //	this.codigoFichaLaboratorios = codigo;
    	
    	ResultSet rs = fichaLabsDao.consultarFicha(con,codigo);
    	
    	try {
    		
    		if (rs.next()) {
    			
    			this.setExamenSolicitado(rs.getString("examenSolicitado"));
    			this.setMuestraEnviada(rs.getString("muestraEnviada"));
    			this.setHallazgos(rs.getString("hallazgos"));
    			this.setFechaToma(rs.getString("fechaToma"));
    			this.setFechaRecepcion(rs.getString("fechaRecepcion"));
    			this.setMuestra(rs.getInt("muestra"));
    			this.setPrueba(rs.getInt("prueba"));
    			this.setAgente(rs.getInt("agente"));
    			this.setResultado(rs.getInt("resultado"));
    			this.setFechaResultado(rs.getString("fechaResultado"));
    			this.setValor(rs.getString("valor"));
    			this.setCodigoFichaLaboratorios(rs.getInt("codigoFicha"));
    		}
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    }
    
    
    
    public void cargarDatosSolicitud(Connection con, int codigo)
    {
    //	this.codigoFichaLaboratorios = codigo;
    	
    	ResultSet rs = fichaLabsDao.consultarSolicitud(con,codigo);
    	
    	try {
    		
    		if (rs.next()) {
    			
    			this.setExamenSolicitado(rs.getString("examenSolicitado"));
    			this.setMuestraEnviada(rs.getString("muestraEnviada"));
    			this.setHallazgos(rs.getString("hallazgos"));
    			this.setFechaToma(rs.getString("fechaToma"));
    			this.setFechaRecepcion(rs.getString("fechaRecepcion"));
    			this.setMuestra(rs.getInt("muestra"));
    			this.setPrueba(rs.getInt("prueba"));
    			this.setAgente(rs.getInt("agente"));
    			this.setResultado(rs.getInt("resultado"));
    			this.setFechaResultado(rs.getString("fechaResultado"));
    			this.setValor(rs.getString("valor"));
    		}
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    }
    
    
    public void cargarParametros(Connection con,int codigo)
    {
    	ResultSet rs = fichaLabsDao.consultarServicios(con,codigo);
    	
    	HashMap mapa = new HashMap();
    	int i=0;
    	
    	try {
    		
    		while (rs.next())
	    	{
    			mapa.put("codigoaxioma_"+Integer.toString(i),Integer.toString(rs.getInt("especialidad"))+"-"+Integer.toString(rs.getInt("codigo")));
    			mapa.put("descripcion_"+Integer.toString(i),rs.getString("descripcion"));
    			mapa.put("fechatoma_"+Integer.toString(i),"");
    			mapa.put("fecharecepcion_"+Integer.toString(i),"");
    			mapa.put("muestra_"+Integer.toString(i),0);
    			mapa.put("prueba_"+Integer.toString(i),0);
    			mapa.put("agente_"+Integer.toString(i),0);
    			mapa.put("resultado_"+Integer.toString(i),0);
    			mapa.put("fecharesultado_"+Integer.toString(i),"");
    			mapa.put("valor_"+Integer.toString(i),"");
    			mapa.put("activo_"+Integer.toString(i),0);
    			
    			i++;
	    	}
    		
    		this.setMapaServicios(mapa);
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    }
    
    
    
    public FichaSolicitudLaboratorios() {
    	
    	reset();
        init(System.getProperty("TIPOBD"));
    }


	public int getAgente() {
		return agente;
	}


	public void setAgente(int agente) {
		this.agente = agente;
	}


	public int getCodigoFicha() {
		return codigoFicha;
	}


	public void setCodigoFicha(int codigoFicha) {
		this.codigoFicha = codigoFicha;
	}


	public int getCodigoFichaLaboratorios() {
		return codigoFichaLaboratorios;
	}


	public void setCodigoFichaLaboratorios(int codigoFichaLaboratorios) {
		this.codigoFichaLaboratorios = codigoFichaLaboratorios;
	}


	public String getExamenSolicitado() {
		return examenSolicitado;
	}


	public void setExamenSolicitado(String examenSolicitado) {
		this.examenSolicitado = examenSolicitado;
	}


	public String getFechaRecepcion() {
		return fechaRecepcion;
	}


	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}


	public String getFechaResultado() {
		return fechaResultado;
	}


	public void setFechaResultado(String fechaResultado) {
		this.fechaResultado = fechaResultado;
	}


	public String getFechaToma() {
		return fechaToma;
	}


	public void setFechaToma(String fechaToma) {
		this.fechaToma = fechaToma;
	}


	public String getHallazgos() {
		return hallazgos;
	}


	public void setHallazgos(String hallazgos) {
		this.hallazgos = hallazgos;
	}


	public int getMuestra() {
		return muestra;
	}


	public void setMuestra(int muestra) {
		this.muestra = muestra;
	}


	public String getMuestraEnviada() {
		return muestraEnviada;
	}


	public void setMuestraEnviada(String muestraEnviada) {
		this.muestraEnviada = muestraEnviada;
	}


	public int getPrueba() {
		return prueba;
	}


	public void setPrueba(int prueba) {
		this.prueba = prueba;
	}


	public int getResultado() {
		return resultado;
	}


	public void setResultado(int resultado) {
		this.resultado = resultado;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}


	public HashMap getMapaServicios() {
		return mapaServicios;
	}


	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}


		
}
