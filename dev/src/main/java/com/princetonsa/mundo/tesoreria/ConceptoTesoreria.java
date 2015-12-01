
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.ConceptoTesoreriaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseConceptoTesoreriaDao;

public class ConceptoTesoreria
{
	//****************ATRIBUTOS*******************************************+
	/**
	 * Código del concepto ingreso tesorería
	 */
	private String codigo;
	
	
	/**
	 * Descripción del concepto ingreso tesorería
	 */
	private String descripcion;
	
	/**
	 * Código del tipo de ingreso del concepto
	 */
	private int tipo;
	
	/**
	 * Valor filtro definido según el tipo de concepto
	 */
	private String valor;
	
	/**
	 * Código de la cuenta contable del concepto de ingreso
	 * 	
	 */
	private String cuenta;
	
	/**
	 * Código del tipo de documento parte contable ingresos
	 */
	private int tipoDocumentoIngreso;
	
	/**
	 * Código del tipo de documento parte contable anulaciones
	 */
	private int tipoDocumentoAnulacion;
	
	/**
	 * Código del centro de costo parte contable de los ingresos y las anulaciones
	 */
	
	private int codCentroCosto;
	
	/**
	 * Nit general ingresos tesorería
	 */
	private int nitHomologacion;
	
	/**
	 * 
	 */
	private boolean activo;
	
	
	/**
	 * DAO de este objeto, para trabajar con Servicio en
	 * la fuente de datos
	 */
    private ConceptoTesoreriaDao tesoreriaDao;
    
    private String rubroPresupuestal;
	
    //**********CONSTRUCTORES & INICIALIZADORES*******************************+++
    
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.codigo = "";
        this.descripcion = "";
        this.tipo = -1;
        this.cuenta = "";
        this.tipoDocumentoIngreso=-1;
        this.tipoDocumentoIngreso=-1;
        this.codCentroCosto=-1;
        this.nitHomologacion=-1;
        this.valor="";
        this.rubroPresupuestal="";
    }
     
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( tesoreriaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tesoreriaDao= myFactory.getTesoreriaDao();
			if( tesoreriaDao!= null )
				return true;
		}
		return false;
	}
	
	/**
     * Constructor vacío de esta clase
     */
    public ConceptoTesoreria()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
    
    //******************GETTERS & SETTERS****************************************
    
	/**
	 * @return Returns the codCentroCosto.
	 */
	public int getCodCentroCosto() {
		return codCentroCosto;
	}
	/**
	 * @param codCentroCosto The codCentroCosto to set.
	 */
	public void setCodCentroCosto(int codCentroCosto) {
		this.codCentroCosto = codCentroCosto;
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
	 * @return Returns the codigoCuenta.
	 */
	public String getCuenta() {
		return this.cuenta;
	}
	/**
	 * @param codigoCuenta The codigoCuenta to set.
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	/**
	 * @return Returns the Tipo.
	 */
	public int getTipo() {
		return tipo;
	}
	/**
	 * @param codigoTipo The codigoTipo to set.
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the nitHomologacion.
	 */
	public int getNitHomologacion() {
		return nitHomologacion;
	}
	/**
	 * @param nitHomologacion The nitHomologacion to set.
	 */
	public void setNitHomologacion(int nitHomologacion) {
		this.nitHomologacion = nitHomologacion;
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
	 * @return Returns the tipoDocumentoAnulacion.
	 */
	public int getTipoDocumentoAnulacion() {
		return tipoDocumentoAnulacion;
	}
	/**
	 * @param tipoDocumentoAnulacion The tipoDocumentoAnulacion to set.
	 */
	public void setTipoDocumentoAnulacion(int tipoDocumentoAnulacion) {
		this.tipoDocumentoAnulacion = tipoDocumentoAnulacion;
	}
	/**
	 * @return Returns the tipoDocumentoIngreso.
	 */
	public int getTipoDocumentoIngreso() {
		return tipoDocumentoIngreso;
	}
	/**
	 * @param tipoDocumentoIngreso The tipoDocumentoIngreso to set.
	 */
	public void setTipoDocumentoIngreso(int tipoDocumentoIngreso) {
		this.tipoDocumentoIngreso = tipoDocumentoIngreso;
	}
	/**
	 * @return Returns the valor.
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
    //******************MÉTODOS****************************************************
    /**
	 * Método usado para cargar los conceptos de ingreso de tesorería 
	 * parametrizados por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptosTesoreria(Connection con,int institucion)
	{
		return tesoreriaDao.cargarConceptosTesoreria(con,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String cargarTipoPagoEspecial (Connection con, int codigo)
	{
		return tesoreriaDao.cargarTipoPagoEspecial(con, codigo);
	}
	
	/**
	 * Método usado para ingresar un nuevo concepto de tesoreria
	 * @param con
	 * @param institucion
	 * @return
	 */
	public int insertarConceptoTesoreria (Connection con,int institucion)
	{
		return tesoreriaDao.insertarConceptoTesoreria(
				con,
				this.codigo,
				this.descripcion,
				this.tipo,
				this.valor,
				this.cuenta,
				this.tipoDocumentoIngreso,
				this.tipoDocumentoAnulacion,
				this.codCentroCosto,
				this.nitHomologacion,
				this.activo,
				institucion,this.rubroPresupuestal);
	}
	
	/**
	 * Método usado para actualizar un registro de conceptos ingreso tesorería
	 * @param con
	 * @param institucion
	 * @return
	 */
	public int actualizarConceptoTesoreria (Connection con, String codigo, int institucion)
	{
		return tesoreriaDao.actualizarConceptoTesoreria(
				con,
				this.codigo,
				this.descripcion,
				this.tipo,
				this.valor,
				this.cuenta,
				this.tipoDocumentoIngreso,
				this.tipoDocumentoAnulacion,
				this.codCentroCosto,
				this.nitHomologacion,
				this.activo,
				codigo,
				institucion,this.rubroPresupuestal);
	}
	
	/**
	 * Método para eliminar un concepto de ingreso tesoreria
	 * @param con
	 * @param institucion
	 * @return
	 */
	public int eliminarConceptoTesoreria(Connection con,int institucion)
	{
		return tesoreriaDao.eliminarConceptoTesoreria(con,this.codigo,institucion);
	}
	
	/**
	 * Método usado para cargar un registro de los conceptos de ingreso de tesoreria
	 * por su código y la institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptoTesoreria(Connection con,int institucion)
	{
		return tesoreriaDao.cargarConceptoTesoreria(con,this.codigo,institucion);
	}
	
	/**
	 * Método usado para cargar un registro de los conceptos de ingreso de tesoreria
	 * por su código y la institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptoTesoreriaAntesActualizar(Connection con, String codigo, int institucion)
	{
		return tesoreriaDao.cargarConceptoTesoreria(con, codigo, institucion);
	}
	
	/**
	 * Método usado para la búsqueda avanzada de los conceptos de tesorería
	 * en la opción de consulta
	 * @param con
	 * @param activo
	 * @param institucion
	 * @return
	 */
	
	public HashMap busquedaConceptosTesoreria(Connection con,String activo,int institucion)
	{
		
		return tesoreriaDao.busquedaConceptosTesoreria(con,
				codigo,
				descripcion,
				tipo,
				valor,
				cuenta,
				tipoDocumentoIngreso,
				tipoDocumentoAnulacion,
				codCentroCosto,
				nitHomologacion,
				activo,
				institucion);
		
		
		
	}

	/**
	 * Método que verifica si el registro se está usando en otras
	 * funcionalidades
	 * @param con
	 * @return
	 */
	public boolean revisarUsoConcepto(Connection con,int institucion) 
	{
		return tesoreriaDao.revisarUsoConcepto(con,this.codigo,institucion);
	}
	
	/**
	 * Método usado para la búsqueda avanzada de los registros vinculados
	 * con el ingreso/modificación de los conceptos de ingreso tesorería
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaConceptosTesoreria2(Connection con,String activo,int institucion)
	{
		return tesoreriaDao.busquedaConceptosTesoreria2(
				con,
				this.codigo,
				this.descripcion,
				this.tipo,
				this.valor,
				this.cuenta,
				this.tipoDocumentoIngreso,
				this.tipoDocumentoAnulacion,
				this.codCentroCosto,
				this.nitHomologacion,
				activo,
				institucion);
	}
	
	/**
	 * Método que carga los tipos de pagos
	 * @param con
	 * @return
	 */
	public Collection cargarTiposPagos(Connection con)
	{
		return tesoreriaDao.cargarTiposPagos(con);
	}
	
	/**
	 * Método que carga los tipos de documentos de contabilidad
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarTiposDocContabilidad(Connection con,int institucion)
	{
		return tesoreriaDao.cargarTiposDocContabilidad(con,institucion);
	}
	
	/**
	 * Método que carga los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarCentrosCosto(Connection con,int institucion)
	{
		return tesoreriaDao.cargarCentrosCosto(con,institucion);
	}
	
	/**
	 * Método que carga los terceros
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarTerceros(Connection con,int institucion)
	{
		return tesoreriaDao.cargarTerceros(con,institucion);
	}

	public String getRubroPresupuestal() {
		return rubroPresupuestal;
	}

	public void setRubroPresupuestal(String rubroPresupuestal) {
		this.rubroPresupuestal = rubroPresupuestal;
	}
}