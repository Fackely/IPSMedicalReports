package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.ConceptosFacturasVariasDao;
import com.princetonsa.dao.historiaClinica.ReferenciaDao;



/**
 * @author Juan Sebastián Castaño C. 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * ConceptosFacturasVarias
 */
public class ConceptosFacturasVarias {

	private int consecutivo;
	private String codigo;
	private String descripcion;
	private InfoDatos cuentaContableDebito;
	private String activo;
	private int institucion;
	private String usuarioModifica;
	private InfoDatos cuentaContableCredito;
	private InfoDatos ajusteDebitoVigAnterior;
	private InfoDatos ajusteCreditoVigAnterior;
	private String tipoconcepto;
	private String descripciontercero;
	private InfoDatos cuentaContableCreditoVigenciaAnterior;
	private InfoDatos cuentaIngrVigencia;
	
	
	
	
	public String getTercero() {
		return tercero;
	}
	public void setTercero(String tercero) {
		this.tercero = tercero;
	}
	
	public String getDescripciontercero() {
		return descripciontercero;
	}
	public void setDescripciontercero(String descripciontercero) {
		this.descripciontercero = descripciontercero;
	}
	private String tercero;
	
	
	
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(ConceptosFacturasVarias.class);
	
	/**
	 * DAO para el manejo de ReferenciaDao
	 */
	private ConceptosFacturasVariasDao conceptosFactVariasDao=null;

//********CONSTRUCTORES E INICIALIZADORES********************
	
	/**
	 * Constructor
	 */
	public ConceptosFacturasVarias() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.cuentaContableDebito = new InfoDatos(ConstantesBD.codigoNuncaValido,"");
		this.descripcion = "";
		this.codigo = "";		
		this.usuarioModifica= "";
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.cuentaContableCredito = new InfoDatos(ConstantesBD.codigoNuncaValido,"");
		this.tipoconcepto="";
		this.tercero="";
		this.descripciontercero="";
		this.ajusteDebitoVigAnterior = new InfoDatos(ConstantesBD.codigoNuncaValido, "");
		this.ajusteCreditoVigAnterior = new InfoDatos(ConstantesBD.codigoNuncaValido, "");
		this.cuentaContableCreditoVigenciaAnterior=new InfoDatos(ConstantesBD.codigoNuncaValido, "");
		this.cuentaIngrVigencia= new InfoDatos();
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (conceptosFactVariasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			conceptosFactVariasDao = myFactory.getConceptosFacturasVariasDao();
		}	
	}
	
	
	/**
	 * 	CARGAR FACTURAS VARIAS
	 * 
	 * @param con
	 * @return conceptosFactVariasDao
	 */
	public HashMap<String, Object> cargar (Connection con)
	{
		return conceptosFactVariasDao.cargar(con, institucion,descripciontercero);
	}
	
	
	/**
	 * BUSCAR FACTURAS VARIAS 
	 * 
	 * @param con
	 * @return conceptosFactVariasDao
	 */
	public HashMap<String, Object> buscarConceptFactVarByConsec (Connection con)
	{
		return conceptosFactVariasDao.buscConceptFacVarByConsec(con, consecutivo);
	}
	
	
	
	/**
	 *	ELIMINAR FACTURAS VARIAS  
	 * @param con
	 * @return conceptosFactVariasDao
	 */
	public boolean eliminarConceptoFacturasVarias (Connection con)
	{
		return conceptosFactVariasDao.eliminarConceptoFacturasVarias(con, consecutivo);
	}
	
	
	/***
	 * MODIFICAR CONCEPTOS DE FACTURA VARIAS
	 * @param con
	 * @return conceptosFactVariasDao
	 */
	public boolean modificarConceptoFacturasVarias (Connection con)
	{
		logger.info("LA CUENTA CONTABLE------->"+getCodigoCuentaContableCreditoVigenciaAnterior());
		return conceptosFactVariasDao.modificarConceptoFacturasVarias(con, 
																		consecutivo, 
																		codigo, 
																		descripcion, 
																		getCodigoCuentaContableDebito(), 
																		activo, 
																		institucion, 
																		usuarioModifica, 
																		getCodigoCuentaContableCredito(),
																		tipoconcepto,
																		tercero, 
																		ajusteDebitoVigAnterior.getCodigo(),
																		ajusteCreditoVigAnterior.getCodigo(),
																		getCodigoCuentaContableCreditoVigenciaAnterior(),
																		this.getCuentaIngrVigencia().getCodigo());
	}
	
	
	/**
	 * METODO PARA GUARDAR LOS CONCEPTOS DE FACTURAS VARIAS
	 * @param con
	 * @return conceptosFactVariasDao
	 */
	public boolean insertarConceptoFactura (Connection con)
	{
		logger.info("LA CUENTA CONTABLE------->"+getCodigoCuentaContableCreditoVigenciaAnterior());
		
		return conceptosFactVariasDao.insertarConceptoFactura(con, 
																codigo,	
																descripcion, 
																getCodigoCuentaContableDebito(), 
																activo, 
																institucion, 
																usuarioModifica, 
																getCodigoCuentaContableCredito(),
																tipoconcepto,
																tercero,
																ajusteDebitoVigAnterior.getCodigo(),
																ajusteCreditoVigAnterior.getCodigo(),
																getCodigoCuentaContableCreditoVigenciaAnterior(),
																this.getCuentaIngrVigencia().getCodigo());
		
	}

	
	
	
	
	
	
	
	
	
	
	
	public int getInstitucion() {
		return institucion;
	}
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
	public InfoDatos getCuentaContableDebito() {
		return cuentaContableDebito;
	}
	public void setCuentaContableDebito(InfoDatos cuentaContable) {
		this.cuentaContableDebito = cuentaContable;
	}
	public int getCodigoCuentaContableDebito() {
		return cuentaContableDebito.getCodigo();
	}
	public void setCodigoCuentaContableDebito(int cuentaContable) {
		this.cuentaContableDebito.setCodigo(cuentaContable);
	}
	
	
	public String getTipoconcepto() {
		return tipoconcepto;
	}
	public void setTipoconcepto(String tipoconcepto) {
		this.tipoconcepto = tipoconcepto;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
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
	public int getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	public String getUsuarioModifica() {
		return usuarioModifica;
	}
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	public InfoDatos getCuentaContableCredito() {
		return cuentaContableCredito;
	}
	public void setCuentaContableCredito(InfoDatos cuentaContableCredito) {
		this.cuentaContableCredito = cuentaContableCredito;
	}
	public int getCodigoCuentaContableCredito() {
		return cuentaContableCredito.getCodigo();
	}
	public void setCodigoCuentaContableCredito(int cuentaContable) {
		this.cuentaContableCredito.setCodigo(cuentaContable);
	}
	/**
	 * @return the ajusteDebitoVigAnterior
	 */
	public InfoDatos getAjusteDebitoVigAnterior() {
		return ajusteDebitoVigAnterior;
	}
	/**
	 * @param ajusteDebitoVigAnterior the ajusteDebitoVigAnterior to set
	 */
	public void setAjusteDebitoVigAnterior(InfoDatos ajusteDebitoVigAnterior) {
		this.ajusteDebitoVigAnterior = ajusteDebitoVigAnterior;
	}
	/**
	 * @return the ajusteCreditoVigAnterior
	 */
	public InfoDatos getAjusteCreditoVigAnterior() {
		return ajusteCreditoVigAnterior;
	}
	/**
	 * @param ajusteCreditoVigAnterior the ajusteCreditoVigAnterior to set
	 */
	public void setAjusteCreditoVigAnterior(InfoDatos ajusteCreditoVigAnterior) {
		this.ajusteCreditoVigAnterior = ajusteCreditoVigAnterior;
		
		
	//Anexo 958
	}
	public InfoDatos getCuentaContableCreditoVigenciaAnterior() {
		return cuentaContableCreditoVigenciaAnterior;
	}
	public void setCuentaContableCreditoVigenciaAnterior(
			InfoDatos cuentaContableCreditoVigenciaAnterior) {
		this.cuentaContableCreditoVigenciaAnterior = cuentaContableCreditoVigenciaAnterior;
	}
	
	public void setCodigoCuentaContableCreditoVigenciaAnterior(int cuentaContable) {
		this.cuentaContableCreditoVigenciaAnterior.setCodigo(cuentaContable);
	}
	
	public int getCodigoCuentaContableCreditoVigenciaAnterior() {
		return cuentaContableCreditoVigenciaAnterior.getCodigo();
	}
	public void setCuentaIngrVigencia(InfoDatos cuentaIngrVigencia) {
		this.cuentaIngrVigencia = cuentaIngrVigencia;
	}
	public InfoDatos getCuentaIngrVigencia() {
		return cuentaIngrVigencia;
	}
	
	
}
