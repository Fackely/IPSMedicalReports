package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

//import org.jboss.util.MuNumber;

import util.ConstantesBD;
import util.InfoDatos;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.EquivalentesDeInventarioDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Clase mundo de equivalencias de inventario
 * @author Juan Sebastian Castaño
 *
 */



public class EquivalentesDeInventario {
	
	/**
	 * Codigo articulo principal
	 */
	public int codigoPpal;
	
	/**
	 * Codigo articulo equivalente
	 */
	public int codigoEquivalente;
	
	/**
	 * Cantidad de articulo equivalente	 * 
	 */
	public int cantidadEquivalente;
	
	
	private String usuarioModifica;
	
	
	public EquivalentesDeInventarioDao equiInventDao = null;
	
	/**
	 * Constructor
	 */
	public EquivalentesDeInventario() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigoPpal = ConstantesBD.codigoNuncaValido;	
		this.codigoEquivalente = ConstantesBD.codigoNuncaValido;
		this.cantidadEquivalente = 0;
		this.usuarioModifica = "";
	}
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (equiInventDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			equiInventDao = myFactory.getEquivalentesDeInventarioDao();
		}	
	}
	
	/**
	 * Metodo de insercion de un nuevo articulo equivalente
	 * @param con
	 * @return
	 */
	public boolean insertarArticuloEquivalente (Connection con)
	{
		return equiInventDao.insertarArticuloEquivalente(con, codigoPpal, codigoEquivalente, cantidadEquivalente, usuarioModifica);
	}
	
	
	/**
	 * Metodo que elimina un registro de articulo equivalente
	 * @param con
	 * @return
	 */
	public boolean eliminarArticuloEquivalente (Connection con)
	{
		return equiInventDao.eliminarArticuloEquivalente(con, codigoPpal, codigoEquivalente);
	}
	
	/**
	 * Metodo para modificar un registro de articulo equivalente
	 * @param con
	 * @return
	 */
	public boolean modificarArticuloEquivalente ( Connection con)
	{
		return equiInventDao.modificarArticuloEquivalente(con, codigoPpal, codigoEquivalente, cantidadEquivalente, usuarioModifica);
	}
	
	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap consultarArticulo(Connection con, int codigoPpal, int codigoEquivalente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEquivalentesDeInventarioDao().consultarArticulo(con, codigoPpal, codigoEquivalente);
	}
	
	/**
	 * @return the codigoPpal
	 */
	public int getCodigoPpal() {
		return codigoPpal;
	}


	/**
	 * @param codigoPpal the codigoPpal to set
	 */
	public void setCodigoPpal(int codigoPpal) {
		this.codigoPpal = codigoPpal;
	}


	/**
	 * Metodo de consulta de articulos equivalentes
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaEquivalentes (Connection con)
	{
		return equiInventDao.consultaEquivalentes(con, codigoPpal);
	}
	
	/**
	 * Metodo que Consulta los Campos Adicionales para los Filtros de la Busqeuda de Equivalentes
	 * @param con
	 * @param codArtPrincipal
	 * @return
	 */
	public HashMap<String, Object> consultaCamposBusqueda (Connection con, int codArtPrincipal)
	{
		return equiInventDao.consultaCamposBusqueda(con, codArtPrincipal);
	}
	
	/**
	 * Metodo de Consulta Datos Adicionales del Articulo Equivlante
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap<String, Object> consultaDatosAd (Connection con, int codigo)
	{
		return equiInventDao.consultaDatosAd(con, codigo);
	}


	/**
	 * @return the cantidadEquivalente
	 */
	public int getCantidadEquivalente() {
		return cantidadEquivalente;
	}


	/**
	 * @param cantidadEquivalente the cantidadEquivalente to set
	 */
	public void setCantidadEquivalente(int cantidadEquivalente) {
		this.cantidadEquivalente = cantidadEquivalente;
	}


	/**
	 * @return the codigoEquivalente
	 */
	public int getCodigoEquivalente() {
		return codigoEquivalente;
	}


	/**
	 * @param codigoEquivalente the codigoEquivalente to set
	 */
	public void setCodigoEquivalente(int codigoEquivalente) {
		this.codigoEquivalente = codigoEquivalente;
	}


	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

}
