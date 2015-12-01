/*
 * Abr 16, 2007
 */
package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.CuentaInvUnidadFunDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCuentaInvUnidadFunDao;

/**
 * 
 * @author sgomez
 * Objeto que representa las cuentas inventario por unidad funcional
 */
public class CuentaInvUnidadFun 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(CuentaInvUnidadFun.class);
	
	/**
	 * DAO para el manejo de los Tipos de Monitoreo
	 */
	private CuentaInvUnidadFunDao cuentaInvDao=null;
	
	/**
	 * Acronimo de la unidad funcional
	 */
	private String unidadFuncional;
	
	/**
	 * Código de la institucion
	 */
	private int institucion;
	
	/**
	 * Código de la clase
	 */
	private int clase;
	
	/**
	 * Código del grupo
	 */
	private int grupo;
	
	/**
	 * Código del subgrupo
	 */
	private int subgrupo;
	
	/**
	 * Consecutivo del subgrupo
	 */
	private int codigo;
	
	/**
	 * Código de la cuenta de costo
	 */
	private int cuentaCosto;
	
	//********CONSTRUCTORES & INICIALIZADORES ***************************************++
	/**
	 * Constructor
	 */
	public CuentaInvUnidadFun() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.unidadFuncional = "";
		this.institucion = 0;
		this.clase = 0;
		this.grupo = 0;
		this.subgrupo = 0;
		this.codigo = 0;
		this.cuentaCosto = 0;
		 
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (cuentaInvDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cuentaInvDao = myFactory.getCuentaInvUnidadFunDao();
		}	
	}
	
	//*************************************************************************************
	//*********************MÉTODOS***********************************************************
	/**
	 * Método que consulta las clases de inventario
	 * @param con
	 * @return
	 */
	public HashMap consultarClases(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		return cuentaInvDao.consultarClases(con, campos);
	}
	
	/**
	 * Método que consulta los grupos de inventario de una clase específica
	 * @param con
	 * @return
	 */
	public HashMap consultarGrupos(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		campos.put("clase",this.clase+"");
		return cuentaInvDao.consultarGrupos(con, campos);
	}
	
	/**
	 * Método que consulta los subgrupos de un grupo y una clase específicas
	 * @param con
	 * @return
	 */
	public HashMap consultarSubgrupos(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		campos.put("clase",this.clase+"");
		campos.put("grupo",this.grupo+"");
		return cuentaInvDao.consultarSubgrupos(con, campos);
	}
	
	/**
	 * Método que inserta una nueva clase x unidad funcional
	 * @param con
	 * @return
	 */
	public int insertarClase(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("clase",this.clase+"");
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		campos.put("cuentaCosto",this.cuentaCosto+"");
		return cuentaInvDao.insertarClase(con, campos);
	}
	
	/**
	 * Método que inserta un grupo x unidad funcional de una clase específica
	 * @param con
	 * @return
	 */
	public int insertarGrupo(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("grupo",this.grupo+"");
		campos.put("clase",this.clase+"");
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		campos.put("cuentaCosto",this.cuentaCosto+"");
		return SqlBaseCuentaInvUnidadFunDao.insertarGrupo(con, campos);
	}
	
	/**
	 * Método que inserta un subgrupo x unidad funcional de un grupo específico
	 * @param con
	 * @return
	 */
	public int insertarSubgrupo(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("codigo",this.codigo+"");
		campos.put("subgrupo",this.subgrupo+"");
		campos.put("grupo",this.grupo+"");
		campos.put("clase",this.clase+"");
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		campos.put("cuentaCosto",this.cuentaCosto+"");
		return cuentaInvDao.insertarSubgrupo(con, campos);
	}
	
	/**
	 * Método que modifica la clase x unidad funcional
	 * @param con
	 * @return
	 */
	public int modificarClase(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("cuentaCosto",this.cuentaCosto+"");
		campos.put("clase",this.clase+"");
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		return cuentaInvDao.modificarClase(con, campos);
	}
	
	/**
	 * Método que modifica el grupo x unidad funcional de una clase específica
	 * @param con
	 * @return
	 */
	public int modificarGrupo(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("cuentaCosto",this.cuentaCosto+"");
		campos.put("grupo",this.grupo+"");
		campos.put("clase",this.clase+"");
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		return cuentaInvDao.modificarGrupo(con, campos);
	}
	
	/**
	 * Método que modifica el subgrupo x unidad funcional de un grupo específico
	 * @param con
	 * @return
	 */
	public int modificarSubgrupo(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("cuentaCosto",this.cuentaCosto+"");
		campos.put("codigo",this.codigo+"");
		campos.put("subgrupo",this.subgrupo+"");
		campos.put("grupo",this.grupo+"");
		campos.put("clase",this.clase+"");
		campos.put("unidadFuncional",this.unidadFuncional);
		campos.put("institucion",this.institucion+"");
		return cuentaInvDao.modificarSubgrupo(con, campos);
	}
	//****************************************************************************************
	//********************GETTERS & SETTERS***************************************************
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return the unidadFuncional
	 */
	public String getUnidadFuncional() {
		return unidadFuncional;
	}
	/**
	 * @param unidadFuncional the unidadFuncional to set
	 */
	public void setUnidadFuncional(String unidadFuncional) {
		this.unidadFuncional = unidadFuncional;
	}
	/**
	 * @return the clase
	 */
	public int getClase() {
		return clase;
	}
	/**
	 * @param clase the clase to set
	 */
	public void setClase(int clase) {
		this.clase = clase;
	}
	/**
	 * @return the grupo
	 */
	public int getGrupo() {
		return grupo;
	}
	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}
	/**
	 * @return the cuentaCosto
	 */
	public int getCuentaCosto() {
		return cuentaCosto;
	}
	/**
	 * @param cuentaCosto the cuentaCosto to set
	 */
	public void setCuentaCosto(int cuentaCosto) {
		this.cuentaCosto = cuentaCosto;
	}
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the subgrupo
	 */
	public int getSubgrupo() {
		return subgrupo;
	}
	/**
	 * @param subgrupo the subgrupo to set
	 */
	public void setSubgrupo(int subgrupo) {
		this.subgrupo = subgrupo;
	}
}
