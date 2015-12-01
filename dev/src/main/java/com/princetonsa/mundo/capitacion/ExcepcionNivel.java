/*
 * Creado el Jun 15, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.ExcepcionNivelDao;

public class ExcepcionNivel {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(NivelAtencion.class);

	
	/**
	 * Variable para ingresar a la BD.  
	 */
	private ExcepcionNivelDao excepcionNivelDao;
	
	/**
	 * Mapa para guardar toda las descripciones de los Niveles de Servicios.
	 */
	private HashMap mapa;

	/**
	 * Mapa para almacenar la información la informacion de los servicios.
	 */
	private HashMap mapaServicio;

	/**
	 * Mapa para almacenar la información de los servicios registrados anteriormente.
	 */
	private HashMap mapaServicioReg;
	
	/**
	 * Mapa para almacenar la información de los contratos de un convenio especifico.
	 */
	private HashMap mapaContrato;

	/**
     * Variable para contabilizar el numero de registros nuevos  
     */
    private int nroRegistrosNuevos;
    
    /**
     * Variable para almacenar el codigo del convenio seleccionado.
     */
    private int codigoConvenio;

    /**
     * Variable para almacenar el codigo del contrato seleccionado.
     */
    private int codigoContrato;

    /**
     * Variable para almacenar el codigo del convenio seleccionado.
     */
    private String nombreConvenio;

    
	/**
	 * Constructor de la clase.
	 *
	 */
	public ExcepcionNivel()
	{
		this.init(System.getProperty("TIPOBD"));
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
			excepcionNivelDao = myFactory.getExcepcionNivelDao();
			wasInited = (excepcionNivelDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Metodo para cargar los niveles de Excepiones de Niveles de Servicios.
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 * @throws SQLException 
	 */
	public HashMap cargarInformacion(Connection con, int codigoInstitucionInt) throws SQLException 
	{
		if (excepcionNivelDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ExcepcionNivel - cargarInformacion )"); 
		}
		
		return excepcionNivelDao.cargarInformacion(con, codigoInstitucionInt);
	}

	/**
	 * Metodo para traer los servicios de un convenio especifico
	 * @param con
	 * @param tipoInformacion
	 * @param institucion 
	 * @param nroContrato 
	 * @return
	 * @throws SQLException 
	 */
	public HashMap cargarServiciosConvenio(Connection con, int tipoInformacion, int institucion, int nroContrato) throws SQLException 
	{
		if (excepcionNivelDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ExcepcionNivel - cargarServiciosConvenio )"); 
		}
		
		return excepcionNivelDao.cargarServiciosConvenio(con, tipoInformacion, this.codigoConvenio, institucion, nroContrato);
	}

	/**
	 * Metodo para insertar los servicios del convenio seleccionado. 
	 * @param con
	 * @param institucion 
	 * @return
	 * @throws SQLException 
	 */
	public int insertarServiciosConvenio(Connection con, int institucion) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (excepcionNivelDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ExcepcionNivel - insertarServicios )"); 
		}	
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);

		
		//-----Validando cambios de los registrados anteriormente.
		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio(this.mapaServicio.get("numeroFilasMapaServicios")+"") ) { nroReg = Integer.parseInt(this.mapaServicio.get("numeroFilasMapaServicios")+""); }

		//logger.info("\n\n Numero de sERvicios A Insertar  [" + nroReg +"] \n\n");
		
		for (int i = 0; i < nroReg; i++)
		{
			//logger.info("\n\n Fue Elimiando [" + !UtilidadTexto.getBoolean(this.getMapaServicio("fueEliminadoServicio_"+ i)+"") +"] \n\n");
			if ( !UtilidadTexto.getBoolean(this.getMapaServicio("fueEliminadoServicio_"+ i)+"") ) 
			{ 
				int servicio = Integer.parseInt(this.getMapaServicio("codigoServicio_" + i)+"");
			    resp1=excepcionNivelDao.insertarServiciosConvenio(con, servicio, this.codigoContrato, institucion);
				if (resp1 <= 0)	{ break; }
			}
		}

		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}


	/**
	 * Metodo para eliminar el contrato.
	 * @param con
	 * @param loginUsuario
	 * @param numServicioEliminar 
	 * @return
	 * @throws SQLException 
	 */
	public int eliminarServicioContrato(Connection con, String loginUsuario, int numServicioEliminar) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (excepcionNivelDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ExcepcionNivel - eliminarServicioContrato)");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		
		//--ELiminar el Servicio.
		resp1=excepcionNivelDao.eliminarServicioContrato(con, numServicioEliminar, this.codigoContrato);
		logger.info("\n\n El Retono [" + resp1 + "] \n\n");
		if (resp1>0)
		{
			  String indice = this.mapaServicioReg.get("indiceEliminado") +  "";
			  String codigo = this.mapaServicioReg.get("cod_servicio_"+indice) +  "";
			  String nombre = this.mapaServicioReg.get("nom_servicio_"+indice) +  "";
			  StringBuffer log = new StringBuffer();
			  log.append("\n============================ ELIMINACIÓN SERVICIO  ======================");
			  log.append("\n CÓDIGO CONVENIO 		 : " + this.codigoConvenio );
			  log.append("\n NOMBRE CONVENIO 		 : " + this.nombreConvenio );
			  log.append("\n CONTRATO   			 : " + this.codigoContrato );
			  log.append("\n CODIGO SERVICIO		 : " + codigo);
			  log.append("\n NOMBRE SERVICIO		 : " + nombre);
			  log.append("\n=========================================================================\n");
			
			  //-- Generar el log. 
			  LogsAxioma.enviarLog(ConstantesBD.logExcepcionNivelServicioCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
		}
		
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
		
	}

	//----------------------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------SET'S Y GET'S-------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * @return Retorna codigoConvenio.
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param Asigna codigoConvenio.
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return Retorna nombreConvenio.
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * @param Asigna nombreConvenio.
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * @return Retorna nroRegistrosNuevos.
	 */
	public int getNroRegistrosNuevos() {
		return nroRegistrosNuevos;
	}

	/**
	 * @param Asigna nroRegistrosNuevos.
	 */
	public void setNroRegistrosNuevos(int nroRegistrosNuevos) {
		this.nroRegistrosNuevos = nroRegistrosNuevos;
	}
	
	/**
	 * @return Returns the mapaFuerzaMuscular.
	 */
	public HashMap getMapa() {
		return this.mapa;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapa(String key) {
		return this.mapa.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaServicio() {
		return this.mapaServicio;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaServicio(HashMap mapaServicio) {
		this.mapaServicio = mapaServicio;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaServicio(String key) {
		return this.mapaServicio.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaServicio(String key, String valor) 
	{
		this.mapaServicio.put(key, valor); 
	}
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaContrato() {
		return this.mapaContrato;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaContrato(HashMap mapaContrato) {
		this.mapaContrato = mapaContrato;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaContrato(String key) {
		return this.mapaContrato.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaContrato(String key, String valor) 
	{
		this.mapaContrato.put(key, valor); 
	}

	/**
	 * @return Retorna codigoContrato.
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param Asigna codigoContrato.
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaServicioReg() {
		return this.mapaServicioReg;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaServicioReg(HashMap mapaServicioReg) {
		this.mapaServicioReg = mapaServicioReg;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaServicioReg(String key) {
		return this.mapaServicioReg.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaServicioReg(String key, String valor) 
	{
		this.mapaServicioReg.put(key, valor); 
	}



}
