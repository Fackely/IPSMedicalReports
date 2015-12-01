package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.FormaFarmaceuticaDao; 


public class FormaFarmaceutica
{
/////
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(FormaFarmaceutica.class);
	
	/**
	 * 
	 */
	private static FormaFarmaceuticaDao objetoDao; 
	
	/**
	 * mapa para manejar las formas.
	 */
	private HashMap formas;

//	variables para menejar la informacion de un registro específico.
	/**
	 * Consecutivo de la caja / Acronimo de la forma farmaceutica
	 */
	private String consecutivo;
	/**
	 * Codigo de la caja
	 */
	/**
	 * Institucion.
	 */
	private int institucion;
	/**
	 * descipcion caja / nombre de la forma farmaceutica
	 */
	private String descripcion;
	/**
	 * Tipo caja.
	 */
	private HashMap vias = new HashMap();

	
	
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.formas = new HashMap ();
    	this.vias = new HashMap();
    }

    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getFormaFarmaceuticaDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * @param con
	 */
	public void cargarInformacion(Connection con,int institucion) 
	{
		ResultSetDecorator rs=objetoDao.cargarInformacion(con,institucion);
		try
		{
			int i=0;
			if(rs!=null)
			{
				while(rs.next())
				{
					formas.put("consecutivo_"+i,rs.getString("consecutivo"));
					formas.put("institucion_"+i,rs.getString("institucion"));
					formas.put("descripcion_"+i,rs.getString("descripcion"));
					formas.put("viaAdmin_"+i,(HashMap)objetoDao.cargarRelacionesViasAdminFormaFarma(con,rs.getString("consecutivo"),institucion));
					formas.put("tiporegistro_"+i,"BD");
					i++;
				}
			}
			this.formas.put("numRegistros",i+"");
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD [FormaFarmaceutica.java]"+e);
		}
	}
	
	/**
	 * 
	 *
	 */
	public FormaFarmaceutica()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	
	public HashMap getFormas() {
		return formas;
	}

	public void setFormas(HashMap formas) {
		this.formas = formas;
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String consecutivo,int institucion) 
	{
		return objetoDao.eliminarRegistro(con,consecutivo,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param mapaVias 
	 * @param color
	 * @param destino
	 * @return
	 */
	public boolean existeModificacion(Connection con, String consecutivo, int institucion, String descripcion, HashMap mapaVias) 
	{
		//*******SE VERIFICA SI LA INFORMACION DE LA FORMA FARMACEÚTICA FUE MODIFICADA**********************
		boolean modificacionFormaFarma = objetoDao.existeModificacion(con,consecutivo,institucion,descripcion);
		
		//********SE VERIFICA SI TAMBIÉN FUERON MODIFICADAS LAS VÍAS************************
		boolean modificacionViasAdm = false;
		this.cargarFormaFarmaceutica(con, consecutivo,institucion);
		int numViasActual = Integer.parseInt(this.getVias("numRegistros").toString());
		int numViasNuevo = Integer.parseInt(mapaVias.get("numRegistros").toString()); 
		
		if(numViasActual!=numViasNuevo)
			modificacionViasAdm = true;
		else
		{
			for(int i=0;i<numViasActual;i++)
			{
				boolean existe = false;
				for(int j=0;j<numViasNuevo;j++)
					if(Integer.parseInt(this.getVias("viaAdmin_"+i).toString())==Integer.parseInt(mapaVias.get("viaAdmin_"+i).toString()))
						existe = true;
				if(!existe)
				{
					modificacionViasAdm = true;
					i = numViasActual;
				}
			}
		}
		//***********************************************************************************************
		
		if(modificacionFormaFarma||modificacionViasAdm)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 */
	private void cargarFormaFarmaceutica(Connection con, String consecutivo,int institucion) 
	{

		ResultSetDecorator rs=objetoDao.cargarFormaFarmaceutica(con, consecutivo,institucion);
		try {
			while (rs.next())
			{
				this.consecutivo= consecutivo.trim(); //Integer.parseInt(consecutivo.trim());
				this.institucion=rs.getInt("institucion");
				this.descripcion=rs.getString("descripcion");
				this.vias = objetoDao.cargarRelacionesViasAdminFormaFarma(con,this.consecutivo,institucion); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param descripcion
	 * @param institucion
	 * @return
	 */
	public boolean modificarRegistro(Connection con, String consecutivo, String descripcion, int institucion) 
	{
		return objetoDao.modificarRegistro(con,consecutivo,descripcion,institucion);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion) 
	{
		return objetoDao.insertarRegistro(con,codigo,institucion,descripcion);
	}


	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
 
	
	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * 
	 * @param con
	 * @param formaFarma
	 * @param viasAdmin
	 * @param institucion 
	 * @return
	 */
	public boolean actualizarRelacionesFormasFarmaViasAdmin(Connection con, String formaFarma, int institucion, HashMap viasAdmin) 
	{
		return objetoDao.actualizarRelacionesFormasFarmaViasAdmin(con,formaFarma,institucion,viasAdmin);
	}


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @param viasAdmin
	 * @return
	 */
	public boolean insertarRelacionesFormaFarmaViasAdmin(Connection con, String codigo, int institucion, HashMap viasAdmin) 
	{
		return objetoDao.insertarRelacionesFormaFarmaViasAdmin(con, codigo, institucion,viasAdmin);
	}

	/**
	 * @return the vias
	 */
	public HashMap getVias() {
		return vias;
	}

	/**
	 * @param vias the vias to set
	 */
	public void setVias(HashMap vias) {
		this.vias = vias;
	}
	
	/**
	 * @return retorna elemento del mapa vias
	 */
	public Object getVias(String key) {
		return vias.get(key);
	}

	/**
	 * @param Asigna elemento al mapa vias 
	 */
	public void setVias(String key,Object obj) {
		this.vias.put(key,obj);
	}

/////	

}
