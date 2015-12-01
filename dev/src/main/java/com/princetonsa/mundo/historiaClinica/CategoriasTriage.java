/*
 * qauthor armando
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.CategoriasTriageDao;

/**
 * 
 * @author artotor
 *
 */
public class CategoriasTriage
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(CategoriasTriage.class);
	
	/**
	 * 
	 */
	private static CategoriasTriageDao objetoDao; 
	
	/**
	 * mapa para manejar las categorias.
	 */
	private HashMap categorias;

//	variables para menejar la informacion de un registro específico.
	/**
	 * Consecutivo de la caja
	 */
	private int consecutivo;
	/**
	 * Codigo de la caja
	 */
	private int codigo;
	/**
	 * Institucion.
	 */
	private int institucion;
	/**
	 * descipcion caja
	 */
	private String descripcion;
	/**
	 * Tipo caja.
	 */
	private int color;

	
	
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.categorias = new HashMap ();
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
			objetoDao= myFactory.getCategoriasTriageDao();
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
					categorias.put("consecutivo_"+i,rs.getString("consecutivo"));
					categorias.put("codigo_"+i,rs.getString("codigo"));
					categorias.put("institucion_"+i,rs.getString("institucion"));
					categorias.put("descripcion_"+i,rs.getString("descripcion"));
					categorias.put("color_"+i,rs.getString("color"));
					categorias.put("nombrecolor_"+i,rs.getString("nombrecolor"));
					//categorias.put("destino_"+i,rs.getString("destino")); cargar los destinos en un mapa.
					categorias.put("destino_"+i,(HashMap)objetoDao.cargarRelacionesDestionsCategorias(con,rs.getString("consecutivo")));
					categorias.put("existerelacion_"+i,((UtilidadValidacion.categoriaTriageUtilizadaEnSignosSintomasSistema(con,rs.getInt("consecutivo")))||(UtilidadValidacion.categoriaTriageUtilizadaEnTriage(con,rs.getInt("consecutivo"))))+"");
					//categorias.put("existerelacion_"+i,"false");
					categorias.put("tiporegistro_"+i,"BD");
					i++;
				}
			}
			this.categorias.put("numRegistros",i+"");
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD [CategoriasTriage.java]"+e.getStackTrace());
		}
	}
	
	/**
	 * 
	 *
	 */
	public CategoriasTriage()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	
	public HashMap getCategorias() {
		return categorias;
	}

	public void setCategorias(HashMap categorias) {
		this.categorias = categorias;
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivo) 
	{
		return objetoDao.eliminarRegistro(con,consecutivo);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @param destino
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int color) 
	{
		if(objetoDao.existeModificacion(con,consecutivo,codigo,descripcion,color))
		{
			this.cargarCategoriaTriage(con,consecutivo);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 */
	private void cargarCategoriaTriage(Connection con, int consecutivo) 
	{
		ResultSetDecorator rs=objetoDao.cargarCategoriaTriage(con,consecutivo);
		try {
			while (rs.next())
			{
				this.consecutivo=consecutivo;
				this.codigo=rs.getInt("codigo");
				this.institucion=rs.getInt("institucion");
				this.descripcion=rs.getString("descripcion");
				this.color=rs.getInt("color");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @param j
	 * @param string
	 * @param k
	 * @param l
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int color) 
	{
		return objetoDao.modificarRegistro(con,consecutivo,codigo,descripcion,color);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param i
	 * @param codigoInstitucionInt
	 * @param string
	 * @param j
	 * @param k
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, int color) 
	{
		return objetoDao.insertarRegistro(con,codigo,institucion,descripcion,color);
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
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
	 * @param i
	 * @param categorias2
	 */
	public boolean actualizarRelacionesCategoriasDestinos(Connection con, int categoria, HashMap destinos) 
	{
		return objetoDao.actualizarRelacionesCategoriasDestinos(con,categoria,destinos);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param colo
	 */
	public void cargarConsecutivoCategoriaTriage(Connection con, String codigo, int institucion, String descripcion, int color) 
	{
		this.consecutivo=objetoDao.obtenerConsecutivoCategoriaTriage( con, codigo, institucion, descripcion, color); 
	}

	/**
	 * 
	 * @param con 
	 * @param consecutivo
	 * @param map
	 */
	public boolean insertarRelacionesCategoriasDestinos(Connection con, int consecutivo, HashMap destinos) 
	{
		return objetoDao.insertarRelacionesCategoriasDestinos(con,consecutivo,destinos);
	}
}
