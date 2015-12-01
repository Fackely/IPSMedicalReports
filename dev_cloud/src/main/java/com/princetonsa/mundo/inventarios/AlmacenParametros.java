package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.AlmacenParametrosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseAlmacenParametrosDao;

/**
 * Funcionalidad Parametros Almacen
 * @author Jose Eduardo Arias Doncel
 * */
public class AlmacenParametros{
	
	//--- Atributos
	
	static Logger logger = Logger.getLogger(SqlBaseAlmacenParametrosDao.class);
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 * */
	//private static AlmacenParametrosDao almacenParametrosDao;
	
	/**
	 * Codigo de Centros de Costos (Subalmacen) Int 
	 * */ 
	private int codigo;
	
	/**
	 * Codigo del centro de atencion al cual pertence
	 * */
	private int centroAtencion;
	
	/**
	 * Codigo de Institucion int 
	 * */
	private int institucion;
	
	/**
	 * (S) (N)
	 * */
	private String  exist_negativa;
	
	/**
	 * Almacen tipo Consignacion (S) (N)
	 * */
	private String tipo_consignac;
	
	/**
	 * Codigo Interfaz
	 * */
	private String codigo_interfaz;
	
	/**
	 * Plan Especial (S) (N)
	 */
	private String plan_especial;
	
	/**
	 * Codigo del Centro de Costo Principal
	 */
	private String centro_costo_principal;
	
	/**
	 * Afecta Costo Promedio
	 */
	private String afectaCostoPromedio;
	
	//--- Fin Atributos
	
	//--- Metodos
	
		/**
	 * Constructor de la Clase
	 * **/
	public AlmacenParametros()
	{
		this.reset();
	}
	
	
	/**
	 * Inicializa los atributos de la clase 
	 **/
	public void reset()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.exist_negativa=ConstantesBD.acronimoNo;
		this.tipo_consignac=ConstantesBD.acronimoNo;
		this.plan_especial=ConstantesBD.acronimoNo;
		this.codigo_interfaz="";
		this.centro_costo_principal="";
		this.afectaCostoPromedio=ConstantesBD.acronimoSi;
	}
	
	/**
	 * Retorna el acceso a bases de datos de este objeto. 
	 */
	public static AlmacenParametrosDao AlmacenParametrosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAlmacenParametrosDao();		
	}	
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean manejaExistenciasNegativas(Connection con, int codigoAlmacen, int codigoInstitucion)
	{
		return AlmacenParametrosDao().manejaExistenciasNegativas(con, codigoAlmacen, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean manejaExistenciasNegativas(int codigoAlmacen, int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean w=AlmacenParametrosDao().manejaExistenciasNegativas(con, codigoAlmacen, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return w;
	}
	
	/**
	 * Consulta registros en base al codigo del centro de costos (subalmacen)
	 * @param Connection con
	 * @param int codigo del centro de costos (subalmacen)
	 * @parm int centro de atencion
	 * @param int institucion (*)
	 * @return HashMap
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap consultarAlmacenParametros(Connection con, int codigo, int centroAtencion, int institucion)
	{
		return  AlmacenParametrosDao().consultarAlmacenParametros(con, codigo, centroAtencion,institucion);	
	}
	
	/**
	 * Consulta centro de Costos
	 * @param Connection con
	 * @param int institucion
	 * @return HashMap
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap consultarCentroCostos(Connection con, int institucion, int centroAtencion)
	{
		return AlmacenParametrosDao().consultarCentroCostos(con, institucion, centroAtencion);
	}
	
		
	/**
	 * Inserta un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public static boolean insertarAlmacenParametros(Connection con, AlmacenParametros almacenParametros)
	{
		return AlmacenParametrosDao().insertarAlmacenParametros(con, almacenParametros);
	}
	
	/**
	 * Elimina un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public static boolean eliminarAlmacenParametros(Connection con, int codigo, int institucion )
	{
		return AlmacenParametrosDao().eliminarAlmacenParametros(con, codigo, institucion);
	}
	
	/**
	 * Modifica un registro de la base de datos
	 * @param Connection con
	 * @param AlmacenParametros almacenParametros 
	 * */
	public static boolean modificarAlmacenParametros(Connection con, AlmacenParametros almacenParametros)
	{
		return AlmacenParametrosDao().modificarAlmacenParametros(con, almacenParametros);
	}
	
	/**
	 * Inicializa la tabla almacen parametros si esta se encuentra vacia
	 * @param Connection con
	 * @param int institucion
	 * @param int opc 1 iniciar tabla, 2 completar tabla
	 * */
	public static void iniciarTableAlmacenParametros(Connection con, int institucion, int opc)
	{
		AlmacenParametrosDao().iniciarTableAlmacenParametros(con, institucion, opc);				
	}	
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */	
	public static boolean manejaExistenciasNegativasCentroAten(Connection con, int centroCosto, int codigoInstitucion)
	{
		return AlmacenParametrosDao().manejaExistenciasNegativasCentroAten(con, centroCosto, codigoInstitucion);
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
	 * @return the codigo_interfaz
	 */
	public String getCodigo_interfaz() {
		return codigo_interfaz;
	}

	/**
	 * @param codigo_interfaz the codigo_interfaz to set
	 */
	public void setCodigo_interfaz(String codigo_interfaz) {
		this.codigo_interfaz = codigo_interfaz;
	}

	/**
	 * @return the exist_negativa
	 */
	public String getExist_negativa() {
		return exist_negativa;
	}

	/**
	 * @param exist_negativa the exist_negativa to set
	 */
	public void setExist_negativa(String exist_negativa) {
		this.exist_negativa = exist_negativa;
	}

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
	 * @return the tipo_consignac
	 */
	public String getTipo_consignac() {
		return tipo_consignac;
	}

	/**
	 * @param tipo_consignac the tipo_consignac to set
	 */
	public void setTipo_consignac(String tipo_consignac) {
		this.tipo_consignac = tipo_consignac;
	}


	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getPlan_especial() {
		return plan_especial;
	}

	
	/**
	 * 
	 * @param plan_especial
	 */
	public void setPlan_especial(String plan_especial) {
		this.plan_especial = plan_especial;
	}

	/**
	 * @return the centro_costo_principal
	 */
	public String getCentro_costo_principal() {
		return centro_costo_principal;
	}


	/**
	 * @param centro_costo_principal the centro_costo_principal to set
	 */
	public void setCentro_costo_principal(String centro_costo_principal) {
		this.centro_costo_principal = centro_costo_principal;
	}
	

	/**
	 * @return afectaCostoPromedio
	 */
	public String getAfectaCostoPromedio() {
		return afectaCostoPromedio;
	}


	/**
	 * @param afectaCostoPromedio
	 */
	public void setAfectaCostoPromedio(String afectaCostoPromedio) {
		this.afectaCostoPromedio = afectaCostoPromedio;
	}
	
	/**
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean afectaCostoPromedio(Connection con, int codigoAlmacen, int codigoInstitucion)
	{
		return AlmacenParametrosDao().afectaCostoPromedio(con, codigoAlmacen, codigoInstitucion);
	}
	
	//--- Fin Metodos
} 