package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Collection;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.AcroFields;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ArticuloCatalogoDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ArticuloCatalogo {
	
	private Logger logger=Logger.getLogger(ArticuloCatalogo.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ArticuloCatalogoDao objetoDao;
	
	//private static ArticuloCatalogoDao articuloCatalogoDao=null;
	
	private int acronimo;
	
	private String proveedor;
	
	private String descripcion;
	
	private String ref_proveedor;

	private String codigoAxioma;
	
	private String estado;	
	
	private Collection resultados;
	
	/**
	 * Mapa articulo de catalogo
	 */
	private HashMap articuloCatalogoMap;
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getArticuloCatalogoDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	/**
	 * Resetea los atributos del objeto
	 */
	private void reset()
	{
		articuloCatalogoMap=new HashMap();
		articuloCatalogoMap.put("numRegistros", "0");
	}
	
	public void clean()
	{
		this.setDescripcion("");
		this.setRef_proveedor("");
		this.setCodigoAxioma("");
		
	}
	
	/**
	 * Constructor de la clase
	 */
	public ArticuloCatalogo()
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
	 * Insertar articulo
	 */
	public int insertar(Connection con, HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	
	/**
	 * Modificar catalogo_proveedor
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarArticuloCatalogo(Connection con, int codigoArticulo,int acronimo)
	{
		return objetoDao.modificarArticuloCatalogo(con,codigoArticulo,acronimo);
	}
	
	/**
	 * Buscar Articulo
	 * @param con
	 * @return
	 */
	public Collection buscar(Connection con, boolean buscarEstado)
	{
		return objetoDao.buscar(con,getProveedor(),getDescripcion(),getRef_proveedor(),getCodigoAxioma(),getAcronimo());
	}
	
	public void consultarArticuloCatalogo(Connection con, int acronimo)
	{
		articuloCatalogoMap = objetoDao.consultarArticuloCatalogo(con,acronimo);
	}

	public HashMap getArticuloCatalogoMap() {
		return articuloCatalogoMap;
	}
	
	public Object getArticuloCatalogoMap(String key) {
		return articuloCatalogoMap.get(key);
	}
	
	public void setArticuloCatalogoMap(String key, Object Value) {
		this.articuloCatalogoMap.put(key,Value);
	}

	public void setArticuloCatalogoMap(HashMap articuloCatalogoMap) {
		this.articuloCatalogoMap = articuloCatalogoMap;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArticuloCatalogoDao getObjetoDao() {
		return objetoDao;
	}

	public void setObjetoDao(ArticuloCatalogoDao objetoDao) {
		this.objetoDao = objetoDao;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getRef_proveedor() {
		return ref_proveedor;
	}

	public void setRef_proveedor(String ref_proveedor) {
		this.ref_proveedor = ref_proveedor;
	}

	public String getCodigoAxioma() {
		return codigoAxioma;
	}

	public void setCodigoAxioma(String codigoAxioma) {
		this.codigoAxioma = codigoAxioma;
	}


	public Collection getResultados() {
		return resultados;
	}

	public void setResultados(Collection resultados) {
		this.resultados = resultados;
	}

	
	public int getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(int acronimo) {
		this.acronimo = acronimo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public HashMap consultarTarifasArticulos(Connection con, String codigoArticulo) 
	{
		return objetoDao.consultarTarifasArticulos(con,codigoArticulo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean guardarEsquemasInventario(Connection con, HashMap vo) 
	{
		return objetoDao.guardarEsquemasInventario(con,vo);
	}

}
