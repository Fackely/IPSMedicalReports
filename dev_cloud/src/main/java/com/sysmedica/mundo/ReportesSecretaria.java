package com.sysmedica.mundo;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadFecha;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.sysmedica.dao.ReportesSecretariaDao;
import com.sysmedica.util.CalendarioEpidemiologico;
import com.sysmedica.util.SemanaEpidemiologica;
import com.sysmedica.util.UtilidadGenArchivos;

public class ReportesSecretaria {

	private Logger logger = Logger.getLogger(ReportesSecretaria.class);
	
	private boolean morbilidad;
	private boolean mortalidad;
	private boolean brotes;
	private boolean sivim;
	private boolean sisvan;
	private int semanaEpidemiologica;
	private int anyo;
	
	private boolean generoArchivoMorbilidad;
	private boolean generoArchivoMortalidad;
	private boolean generoArchivoBrotes;
	private boolean generoArchivoSivim;
	private boolean generoArchivoSisvan;
	
	private Collection coleccion;
	
	ReportesSecretariaDao reportesSecretariaDao;
	
	public ReportesSecretaria() {
		
		reset();
		init(System.getProperty("TIPOBD"));
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
			reportesSecretariaDao = myFactory.getReportesSecretariaDao();
			wasInited = (reportesSecretariaDao != null);
		}
		return wasInited;
	}
    
    
    public void reset() {
    	
    	semanaEpidemiologica = 0;
    	anyo = 0;
    	
    	morbilidad = false;
    	mortalidad = false;
    	brotes = false;
    	sivim = false;
    	sisvan = false;
    	semanaEpidemiologica = 0;
    	
    	generoArchivoMorbilidad = false;
    	generoArchivoMortalidad = false;
    	generoArchivoBrotes = false;
    	generoArchivoSivim = false;
    	generoArchivoSisvan = false;
    	
    	try {
    		coleccion.clear();
    	}
    	catch (NullPointerException npe) {
    		
    	}
    }
    
    public void cargarDatosMorbilidad(Connection con)
    {
    	ResultSet rs = reportesSecretariaDao.consultaMorbilidad(con,semanaEpidemiologica,anyo);
    	
    	String primerNombre = "";
    	String segundoNombre = "";
    	String primerApellido = "";
    	String segundoApellido = "";
    	String identificacion = "";
    	String tipoIdentificacion = "";
    	String municipioResidencia = "";
    	String departamentoResidencia = "";
    	
    	try {
	    	while (rs.next()) {
	    		
	    		primerNombre = rs.getString("primer_nombre");
	    		segundoNombre = rs.getString("segundo_nombre");
	    		primerApellido = rs.getString("primer_apellido");
	    		segundoApellido = rs.getString("segundo_apellido");
	    		identificacion = rs.getString("numero_identificacion");
	    		tipoIdentificacion = rs.getString("tipo_identificacion");
	    		municipioResidencia = rs.getString("nombreMunResidencia");
	    		departamentoResidencia = rs.getString("nombreDepResidencia");
	    		
	    		logger.info("--------------------------------");
	    		logger.info("");
	    		logger.info("PRIMER NOMBRE : "+primerNombre);
	    		logger.info("SEGUNDO NOMBRE : "+segundoNombre);
	    		logger.info("PRIMER APELLIDO : "+primerApellido);
	    		logger.info("SEGUNDO APELLIDO : "+segundoApellido);
	    		logger.info("IDENTIFICACION : "+identificacion);
	    		logger.info("TIPO ID : "+tipoIdentificacion);
	    		logger.info("MUNICIPIO RESIDENCIA : "+municipioResidencia);
	    		logger.info("DEPARTAMENTO RESIDENCIA : "+departamentoResidencia);
	    		logger.info("");
	    		logger.info("");	    		
	    	}
    	}
    	catch (SQLException sqle) {
    		logger.info("Error consultando los datos de morbilidad distrital : "+sqle.getMessage());
    	}
    }
    
    
    
    
    public boolean generarConsolidados(Connection con, String loginUsuario)
    {
    	boolean resultado=false;
    	
    	if (semanaEpidemiologica==0) {
    		
    		String fechaActual = UtilidadFecha.getFechaActual();
    		
			int numSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[0];
			
			anyo = Integer.parseInt(fechaActual.split("/")[2]);
			
			if (numSemanaActual==1) {
				semanaEpidemiologica = 52;
				anyo = anyo-1;
			}
			else {
				semanaEpidemiologica = numSemanaActual-1;
			}
    	}
    	
    //	SemanaEpidemiologica semana = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semanaEpidemiologica,anyo);
    	
    	if (morbilidad) {
    		
    		boolean archivoExiste = false;
    		boolean archivoExisteBaseDatos = false;
    	//	ResultSet rs = reportesSecretariaDao.consultaMorbilidad(con,semanaEpidemiologica,anyo);
    	//	ResultSet rs = reportesSecretariaDao.consultaArchivos(con,this.morbilidad,this.mortalidad,this.brotes,this.sivim,this.sisvan,semanaEpidemiologica,anyo);
    		archivoExisteBaseDatos = reportesSecretariaDao.consultaExisteArchivo(con,1,semanaEpidemiologica,anyo);
    		
    		if (UtilidadGenArchivos.archivoExiste(1,semanaEpidemiologica,anyo)) {
    			
    			archivoExiste = true;
    		}
    		/*
    		try {
	    		if (rs.next()) {
	    			
	    			archivoExisteBaseDatos = true;
	    			rs.first();
	    		}
    		}
    		catch (SQLException sqle) {
    			
    		}
    		*/
    		
    		if (UtilidadGenArchivos.generarArchivoMorbilidad(reportesSecretariaDao.consultaMorbilidad(con,semanaEpidemiologica,anyo),semanaEpidemiologica,anyo)>0) {
    			
    		//	generoArchivoMorbilidad = true;
    			
    			if (archivoExiste||archivoExisteBaseDatos) {
        			
        			reportesSecretariaDao.modificarRegistroArchivo(con,semanaEpidemiologica,anyo,loginUsuario);
        		}
        		else {
        			
        		//	String rutaArchivo = UtilidadGenArchivos.getRutaEpidemiologia()+"secretariaDistrital/"+Integer.toString(anyo)+"-"+Integer.toString(semanaEpidemiologica)+"-MORBI.SDF";
        			String rutaArchivo = "../saludPublica/secretariaDistrital/"+Integer.toString(anyo)+"-"+Integer.toString(semanaEpidemiologica)+"-MORBI.SDF";
        			reportesSecretariaDao.insertarRegistroArchivo(con,rutaArchivo,1,loginUsuario,semanaEpidemiologica,anyo);
        		}
    		}
    		else {
    			
    			generoArchivoMorbilidad = true;
    		}
    	}
    	
    	if (mortalidad) {
    		
    		boolean archivoExiste = false;
    		boolean archivoExisteBaseDatos = false;
    	//	ResultSet rs = reportesSecretariaDao.consultaMortalidad(con,semanaEpidemiologica,anyo);
    	//	ResultSet rs = reportesSecretariaDao.consultaArchivos(con,this.morbilidad,this.mortalidad,this.brotes,this.sivim,this.sisvan,semanaEpidemiologica,anyo);
    		archivoExisteBaseDatos = reportesSecretariaDao.consultaExisteArchivo(con,2,semanaEpidemiologica,anyo);
    		
    		if (UtilidadGenArchivos.archivoExiste(2,semanaEpidemiologica,anyo)) {
    			
    			archivoExiste = true;
    		}
    		/*
    		try {
	    		if (rs.next()) {
	    			
	    			archivoExisteBaseDatos = true;
	    			rs.first();
	    		}
    		}
    		catch (SQLException sqle) {
    			
    		}
    		*/

    		if (UtilidadGenArchivos.generarArchivoMortalidad(reportesSecretariaDao.consultaMortalidad(con,semanaEpidemiologica,anyo),semanaEpidemiologica,anyo)>0) 
    		{
    		//	generoArchivoMortalidad = true;
    			
    			if (archivoExiste||archivoExisteBaseDatos) {
        			
        			reportesSecretariaDao.modificarRegistroArchivo(con,semanaEpidemiologica,anyo,loginUsuario);
        		}
        		else {
        			
        		//	String rutaArchivo = UtilidadGenArchivos.getRutaEpidemiologia()+"secretariaDistrital/"+Integer.toString(anyo)+"-"+Integer.toString(semanaEpidemiologica)+"-MORTAL.SDF";
        			String rutaArchivo = "../saludPublica/secretariaDistrital/"+Integer.toString(anyo)+"-"+Integer.toString(semanaEpidemiologica)+"-MORTAL.SDF";
        			reportesSecretariaDao.insertarRegistroArchivo(con,rutaArchivo,2,loginUsuario,semanaEpidemiologica,anyo);
        		}
    		}
    		else {
    			generoArchivoMortalidad = true;
    		}
    	}
    	
    	if (brotes) {
    		
    		boolean archivoExiste = false;
    		boolean archivoExisteBaseDatos = false;
    	//	ResultSet rs = reportesSecretariaDao.consultaMortalidad(con,semanaEpidemiologica,anyo);
    	//	ResultSet rs = reportesSecretariaDao.consultaArchivos(con,this.morbilidad,this.mortalidad,this.brotes,this.sivim,this.sisvan,semanaEpidemiologica,anyo);
    		archivoExisteBaseDatos = reportesSecretariaDao.consultaExisteArchivo(con,3,semanaEpidemiologica,anyo);
    		    		
    		if (UtilidadGenArchivos.archivoExiste(3,semanaEpidemiologica,anyo)) {
    			
    			archivoExiste = true;
    		}
    		/*
    		try {
	    		if (rs.next()) {
	    			
	    			archivoExisteBaseDatos = true;
	    			rs.first();
	    		}
    		}
    		catch (SQLException sqle) {
    			
    		}
    		*/
    		

    		if (UtilidadGenArchivos.generarArchivoBrotes(reportesSecretariaDao.consultaBrotes(con,semanaEpidemiologica,anyo),semanaEpidemiologica,anyo)>0)
    		{
    		//	generoArchivoBrotes = true;
    			
    			if (archivoExiste||archivoExisteBaseDatos) {
        			
        			reportesSecretariaDao.modificarRegistroArchivo(con,semanaEpidemiologica,anyo,loginUsuario);
        		}
        		else {
        			
        		//	String rutaArchivo = UtilidadGenArchivos.getRutaEpidemiologia()+"secretariaDistrital/"+Integer.toString(anyo)+"-"+Integer.toString(semanaEpidemiologica)+"-BROTE.SDF";
        			String rutaArchivo = "../saludPublica/secretariaDistrital/"+Integer.toString(anyo)+"-"+Integer.toString(semanaEpidemiologica)+"-BROTE.SDF";
        			reportesSecretariaDao.insertarRegistroArchivo(con,rutaArchivo,3,loginUsuario,semanaEpidemiologica,anyo);
        		}
    		}
    		else {
    			generoArchivoBrotes = true;
    		}
    	}
    	
    	try {
    		coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(reportesSecretariaDao.consultaArchivos(con,morbilidad,mortalidad,brotes,sivim,sisvan,semanaEpidemiologica,anyo)));
    		this.coleccion = coleccion;
    	}
    	catch (SQLException sqle) {
    		logger.info("Error consultando los archivos generados : "+sqle.getMessage());
    	}
    	
    	return resultado;
    }
    
    
    
    public void consultarArchivos(Connection con)
    {
    	if (semanaEpidemiologica==0) {
    		
    		String fechaActual = UtilidadFecha.getFechaActual();
			
			int numSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[0];
			anyo = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[1];
			
		//	anyo = Integer.parseInt(fechaActual.split("/")[2]);
			
			if (numSemanaActual==1) {
				semanaEpidemiologica = 52;
				anyo = anyo-1;
			}
			else {
				semanaEpidemiologica = numSemanaActual-1;
			}
    	}

    	ResultSet rs = reportesSecretariaDao.consultaArchivos(con,morbilidad,mortalidad,brotes,sivim,sisvan,semanaEpidemiologica,anyo);
    	
    	try {
    		this.setColeccion(UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs)));
    	}
    	catch (SQLException sqle) {
    		
    	}
    }
    
    
    

	public int getAnyo() {
		return anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	public boolean isBrotes() {
		return brotes;
	}

	public void setBrotes(boolean brotes) {
		this.brotes = brotes;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public boolean isMorbilidad() {
		return morbilidad;
	}

	public void setMorbilidad(boolean morbilidad) {
		this.morbilidad = morbilidad;
	}

	public boolean isMortalidad() {
		return mortalidad;
	}

	public void setMortalidad(boolean mortalidad) {
		this.mortalidad = mortalidad;
	}

	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}

	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}

	public boolean isSisvan() {
		return sisvan;
	}

	public void setSisvan(boolean sisvan) {
		this.sisvan = sisvan;
	}

	public boolean isSivim() {
		return sivim;
	}

	public void setSivim(boolean sivim) {
		this.sivim = sivim;
	}

	public boolean isGeneroArchivoBrotes() {
		return generoArchivoBrotes;
	}

	public void setGeneroArchivoBrotes(boolean generoArchivoBrotes) {
		this.generoArchivoBrotes = generoArchivoBrotes;
	}

	public boolean isGeneroArchivoMorbilidad() {
		return generoArchivoMorbilidad;
	}

	public void setGeneroArchivoMorbilidad(boolean generoArchivoMorbilidad) {
		this.generoArchivoMorbilidad = generoArchivoMorbilidad;
	}

	public boolean isGeneroArchivoMortalidad() {
		return generoArchivoMortalidad;
	}

	public void setGeneroArchivoMortalidad(boolean generoArchivoMortalidad) {
		this.generoArchivoMortalidad = generoArchivoMortalidad;
	}

	public boolean isGeneroArchivoSisvan() {
		return generoArchivoSisvan;
	}

	public void setGeneroArchivoSisvan(boolean generoArchivoSisvan) {
		this.generoArchivoSisvan = generoArchivoSisvan;
	}

	public boolean isGeneroArchivoSivim() {
		return generoArchivoSivim;
	}

	public void setGeneroArchivoSivim(boolean generoArchivoSivim) {
		this.generoArchivoSivim = generoArchivoSivim;
	}

	public Collection getColeccion() {
		return coleccion;
	}

	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}
    	
}
