/*
 * @author armando
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.TiposProgamaDao;

/**
 * 
 * @author armando
 *
 */
public class TiposPrograma 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(TiposPrograma.class);
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private boolean activo;
	/**
	 * 
	 */
	TiposProgamaDao objetoDao;

	/**
	 * 
	 */
	private HashMap tiposProgama;
	
	public TiposPrograma()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param property
	 */
	private boolean init(String tipoBD) 
	{
    	if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getTiposProgamaDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * 
	 *
	 */
	public void reset() 
	{
    	this.tiposProgama=new HashMap();
    	this.tiposProgama.put("numRegistros","0");
    	this.codigo=ConstantesBD.codigoNuncaValido+"";
    	this.institucion=ConstantesBD.codigoNuncaValido;
    	this.descripcion=ConstantesBD.codigoNuncaValido+"";
    	this.activo=false;
	}

	public HashMap getTiposProgama() {
		return tiposProgama;
	}

	public void setTiposProgama(HashMap tiposProgama) {
		this.tiposProgama = tiposProgama;
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucionInt
	 */
	public void cargarInformacion(Connection con, int institucion) 
	{
		this.tiposProgama=(HashMap)objetoDao.cargarInfomacionBD(con,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo) 
	{
		return objetoDao.insertarRegistro(con,codigo,institucion,descripcion,activo);
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @param string
	 * @param j
	 * @param boolean1
	 * @return
	 */
	public boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo) {
		return objetoDao.modificarRegistro(con,codigo,institucion,descripcion,activo);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public boolean existeModificacion(Connection con, String codigo, int institucion, String descripcion, boolean activo) {
		if(objetoDao.existeModificacion(con,codigo,institucion,descripcion,activo))
		{
			this.cargarTipoPrograma(con,codigo,institucion);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 */
	private void cargarTipoPrograma(Connection con, String codigo, int institucion) 
	{
		ResultSetDecorator rs=objetoDao.cargarTipoPrograma(con,codigo,institucion);
		try {
			while (rs.next())
			{
				this.codigo=rs.getString("codigo");
				this.institucion=rs.getInt("institucion");
				this.descripcion=rs.getString("descripcion");
				this.activo=rs.getBoolean("activo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @param con
	 * @param string
	 * @param i
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String codigo, int institucion) 
	{
		return objetoDao.eliminarRegistro(con,codigo,institucion);
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
}
