/*
 * Created on 20/09/2005
 *
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EntidadesFinancierasDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.servinte.axioma.orm.delegate.tesoreria.EntidadesFinancierasDelegate;

/**
 * @author artotor
 *
 */
public class EntidadesFinancieras 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(EntidadesFinancieras.class);
	
	/**
	 * DAO de este objeto, para trabajar con EntidadesFinancieras
	 * en la fuente de datos
	 */    
    private static EntidadesFinancierasDao entFinDao;
	
    /**
	 * Mapa paara manejar las Entidades Financieras definidas en el sistema
	 */
	private HashMap mapaEntidadesFinancieras;
	
	/**
	 * Numero de registros.
	 */
	private int numeroRegistros;
	
	/**
	 * Codigo de una Entidad financiera
	 */
	private String codigo;
	
	/**
	 * Tercero de una entidad financiera
	 */
	private String tercero;
	
	/**
	 * Tipo de una entidad financiera
	 */
	private int tipo;
	private String desTipo;
	
	/**
	 * Activo
	 */
	private boolean activo;
	
	/**
     * Método que limpia este objeto
     * 
     */
	public void reset()
    {
    	this.mapaEntidadesFinancieras=new HashMap();
    	this.numeroRegistros=0;
    	this.codigo="";
    	this.tercero="";
    	this.tipo=ConstantesBD.codigoNuncaValido;
    	this.desTipo="";
    	this.activo=false;
    }
     
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( entFinDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			entFinDao= myFactory.getEntidadesFinancierasDao();
			if( entFinDao!= null )
				return true;
		}
		return false;
	}

	public EntidadesFinancieras() 
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 */
	public void cargarEntidadesFinancieras(Connection con, int codigoInstitucionInt) 
	{
		ResultSetDecorator rs=entFinDao.cargarEntidadesFinancieras(con,codigoInstitucionInt);
		try
		{
			int i=0;
			while(rs.next())
			{
				mapaEntidadesFinancieras.put("consecutivo_"+i,rs.getString("consecutivo"));
				mapaEntidadesFinancieras.put("codigo_"+i,rs.getString("codigo"));
				mapaEntidadesFinancieras.put("institucion_"+i,rs.getString("institucion"));
				mapaEntidadesFinancieras.put("codigotercero_"+i,rs.getString("codigotercero"));
				mapaEntidadesFinancieras.put("descripciontercero_"+i,rs.getString("descripciontercero"));
				mapaEntidadesFinancieras.put("tercero_"+i,rs.getString("tercero"));
				mapaEntidadesFinancieras.put("codigotipo_"+i,rs.getString("codigotipo"));
				mapaEntidadesFinancieras.put("descripciontipo_"+i,rs.getString("descripciontipo"));
				mapaEntidadesFinancieras.put("activo_"+i,rs.getString("activo"));
				mapaEntidadesFinancieras.put("existerelacion_"+i,(UtilidadValidacion.entidadFinanceraUtilizadaEnTarjetasFinancieras(con,rs.getInt("consecutivo")))+"");
				mapaEntidadesFinancieras.put("tiporegistro_"+i,"BD");
				i++;
			}
			this.numeroRegistros=i;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD [EntidadesFinanceras.java]"+e.getStackTrace());
		}
	}

	/**
	 * @return Returns the mapaEntidadesFinancieras.
	 */
	public HashMap getMapaEntidadesFinancieras() {
		return mapaEntidadesFinancieras;
	}
	/**
	 * @param mapaEntidadesFinancieras The mapaEntidadesFinancieras to set.
	 */
	public void setMapaEntidadesFinancieras(HashMap mapaEntidadesFinancieras) {
		this.mapaEntidadesFinancieras = mapaEntidadesFinancieras;
	}
	/**
	 * @return Returns the numeroRegistros.
	 */
	public int getNumeroRegistros() {
		return numeroRegistros;
	}
	/**
	 * @param numeroRegistros The numeroRegistros to set.
	 */
	public void setNumeroRegistros(int numeroRegistros) {
		this.numeroRegistros = numeroRegistros;
	}

	/**
	 * @param con
	 * @param i
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivo)
	{
		return entFinDao.eliminarRegistro(con,consecutivo);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, String codigo, int codigoTercero, int tipo, boolean activo) 
	{
		if(entFinDao.existeModificacion(con,consecutivo,codigo,codigoTercero,tipo,activo))
		{
			this.cargarEntidadFinanciera(con,consecutivo);
			return true;
		}
		return false;
	}

	/**
	 * @param con
	 * @param consecutivo
	 */
	private void cargarEntidadFinanciera(Connection con, int consecutivo) 
	{
		ResultSetDecorator rs=entFinDao.cargarEntidadFinanciera(con,consecutivo);
		try
		{
			int i=0;
			while(rs.next())
			{
				this.codigo=rs.getString("codigo");
				this.tercero=rs.getString("tercero");
				this.tipo=rs.getInt("codigotipo");
				this.desTipo=rs.getString("descripciontipo");
				this.activo=rs.getBoolean("activo");
				i++;
			}
			this.numeroRegistros=i;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD [EntidadesFinanceras.java]"+e.getStackTrace());
		}
		
	}

	/**
	 * @param con
	 * @param i
	 * @param string
	 * @param j
	 * @param k
	 * @param boolean1
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, String codigo, int codigoTercero, int tipo, boolean activo) 
	{
		return entFinDao.modificarRegistro(con,consecutivo,codigo,codigoTercero,tipo,activo);
	}

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @param i
	 * @param boolean1
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int codigoInstitucionInt, int codigoTercero, int tipo, boolean activo)
	{
		return entFinDao.insertarRegistro(con,codigo,codigoInstitucionInt,codigoTercero,tipo,activo);
	}


	/**
	 * @return Returns the activo.
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the tercero.
	 */
	public String getTercero() {
		return tercero;
	}
	/**
	 * @param tercero The tercero to set.
	 */
	public void setTercero(String tercero) {
		this.tercero = tercero;
	}
	/**
	 * @return Returns the tipo.
	 */
	public int getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return Returns the desTipo.
	 */
	public String getDesTipo() {
		return desTipo;
	}
	/**
	 * @param desTipo The desTipo to set.
	 */
	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}

	/**
	 * 
	 * @param todas
	 * @return
	 */
	public static ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancieras(boolean todas) 
	{
		EntidadesFinancierasDelegate dao=new EntidadesFinancierasDelegate();
		return dao.consultarEntidadesFinancieras(todas);
	}

	public static com.servinte.axioma.orm.EntidadesFinancieras cargarEntidadFinanciera(
			int codigo) {
		EntidadesFinancierasDelegate dao=new EntidadesFinancierasDelegate();
		com.servinte.axioma.orm.EntidadesFinancieras entidad=dao.findById(codigo);
		entidad.getTerceros().getDescripcion();
		return entidad;
	}

	public static ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancierasPorTarjeta(int codigoTarjeta)
	{
		EntidadesFinancierasDelegate dao=new EntidadesFinancierasDelegate();
		return dao.obtenerEntidadesPorTarjeta(codigoTarjeta);
	}
	
	/**
	 * Método que se encarga de consultar las entidades financieras asociadas a una institución específica.
	 * 
	 * @param codigoInstitucion
	 * @param activo
	 * @return
	 */
	public static ArrayList<DtoEntidadesFinancieras> obtenerEntidadesPorInstitucion(int codigoInstitucion, boolean activo) 
	{
		EntidadesFinancierasDelegate dao=new EntidadesFinancierasDelegate();
		return (ArrayList<DtoEntidadesFinancieras>) dao.obtenerEntidadesPorInstitucion(codigoInstitucion, activo);
	}
}