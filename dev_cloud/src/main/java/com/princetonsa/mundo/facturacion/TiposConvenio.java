package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.TiposConvenioDao;

/**
 * Mundo tipos de convenios
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class TiposConvenio
{
//	--------------------Atributos
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TiposConvenioDao tiposConvenioDao;
	
	/**
	 * Codigo del tipo convenio
	 */
	private String codigo;
	
	/**
	 * Nombre asigando por la institucion a cada tipo de convenio
	 */
	private String descripcion;
	
	/**
	 * Campo de seleccion de acuerdo a los tipos de regimen creados
	 */
	private String acronimoTipoRegimen;
	
	/**
	 * Campo de cuenta contable de acuerdo a las definidas en el sistema
	 */
	private int codigoCuentaContable;
	
	/**
	 * Usuario quien modifico por ultima vez o creo el tipo convenio
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private int codigoInstitucion;
	
	/**
	 * 
	 */
	private int codigoClasificacion;
	
	/**
	 * Variable para manejar el codigo de rubro Presupuestal para el tipo de convenio
	 */
	private double rubroPresupuestal;
	
	/**
	 * 
	 */
	private HashMap tiposConvenioMap;
	
	private int cuentaValConvenio;
	
	private int cuentaValPaciente;
	
	private int cuentaDescPaciente;
	
	private int cuentaCxcCapitacion;
	
	private int cuentaContableUtil;
	
	private int cuentaContablePerd;
	
	private int cuentaDebitoGlsRecibida;

	private int cuentaCreditoGlsRecibida;
	
	private int cuentaDebitoGlsAceptada;

	private int cuentaCreditoGlsAceptada;
	
	/**
	 * 
	 */
	private String aseguradoraAtEv;
	
//	--------------------Metodos
	
	/**
	 * resetea los atributos del objeto 
	 *
	 */
	public void reset()
	{
		this.codigo="";
		this.descripcion="";
		this.acronimoTipoRegimen="";
		this.codigoCuentaContable=-1;
		this.usuarioModifica="";
		this.codigoInstitucion=-1;
		this.codigoClasificacion=-1;
		this.rubroPresupuestal=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		
		this.tiposConvenioMap=new HashMap();
    	this.tiposConvenioMap.put("numRegistros","0");
    	
    	this.cuentaValConvenio = ConstantesBD.codigoNuncaValido;
    	this.cuentaValPaciente = ConstantesBD.codigoNuncaValido;
    	this.cuentaDescPaciente = ConstantesBD.codigoNuncaValido;
    	this.cuentaCxcCapitacion = ConstantesBD.codigoNuncaValido;
    	
    	this.cuentaContableUtil = ConstantesBD.codigoNuncaValido;
    	this.cuentaContablePerd = ConstantesBD.codigoNuncaValido;
    	this.cuentaDebitoGlsRecibida = ConstantesBD.codigoNuncaValido;
    	this.cuentaCreditoGlsRecibida = ConstantesBD.codigoNuncaValido;
    	this.cuentaDebitoGlsAceptada = ConstantesBD.codigoNuncaValido;
    	this.cuentaCreditoGlsAceptada = ConstantesBD.codigoNuncaValido;
    	this.aseguradoraAtEv="";
    	
	}
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public TiposConvenio() 
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			tiposConvenioDao = myFactory.getTiposConvenioDao();
			wasInited = (tiposConvenioDao != null);
		}
		return wasInited;
	}
	
	public static TiposConvenioDao tiposconvenioDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposConvenioDao();
	}
	
	/**
	 * Insertar un registro tipo de convenio
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public boolean insertarTiposConvenio(Connection con, TiposConvenio tiposconvenio)
	{
		return tiposConvenioDao.insertarTiposConvenio(con, tiposconvenio);
	}
	
	/**
	 * Modifica un tipo convenio registrado
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public boolean modificarTiposConvenio(Connection con, TiposConvenio tiposconvenio,String codigoAntesMod)
	{
		return tiposConvenioDao.modificarTiposConvenio(con, tiposconvenio, codigoAntesMod);
	}
	
	/**
	 * Elimina un tipo de convenio registrado
	 * @param con
	 * @param String codigo, int institucion
	 */
	public boolean eliminarTiposConvenio(Connection con, String codigo, int institucion)
	{
		return tiposConvenioDao.eliminarTiposConvenio(con, codigo, institucion);
	}
	
	/**
	 * Consulta de tipos de convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposConvenio(Connection con, int codigoInstitucion)
	{
		return tiposConvenioDao.consultarTiposConvenio(con, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposConvenioEspecifico(Connection con, int codigoInstitucion, String codigo)
	{
		return tiposConvenioDao.consultarTiposConvenioEspecifico(con, codigoInstitucion, codigo);
	}

//	--------------------Getters And Setters
	/**
	 * @return the tiposConvenioDao
	 */
	public static TiposConvenioDao getTiposConvenioDao() {
		return tiposConvenioDao;
	}


	/**
	 * @param tiposConvenioDao the tiposConvenioDao to set
	 */
	public static void setTiposConvenioDao(TiposConvenioDao tiposConvenioDao) {
		TiposConvenio.tiposConvenioDao = tiposConvenioDao;
	}


	/**
	 * @return the acronimoTipoRegimen
	 */
	public String getAcronimoTipoRegimen() {
		return acronimoTipoRegimen;
	}


	/**
	 * @param acronimoTipoRegimen the acronimoTipoRegimen to set
	 */
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen) {
		this.acronimoTipoRegimen = acronimoTipoRegimen;
	}

	
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	/**
	 * @return the codigoCuentaContable
	 */
	public int getCodigoCuentaContable() {
		return codigoCuentaContable;
	}


	/**
	 * @param codigoCuentaContable the codigoCuentaContable to set
	 */
	public void setCodigoCuentaContable(int codigoCuentaContable) {
		this.codigoCuentaContable = codigoCuentaContable;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
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


	/**
	 * @return the codigoClasificacion
	 */
	public int getCodigoClasificacion() {
		return codigoClasificacion;
	}


	/**
	 * @param codigoClasificacion the codigoClasificacion to set
	 */
	public void setCodigoClasificacion(int codigoClasificacion) {
		this.codigoClasificacion = codigoClasificacion;
	}


	/**
	 * @return the tiposConvenioMap
	 */
	public HashMap getTiposConvenioMap() {
		return tiposConvenioMap;
	}


	/**
	 * @param tiposConvenioMap the tiposConvenioMap to set
	 */
	public void setTiposConvenioMap(HashMap tiposConvenioMap) {
		this.tiposConvenioMap = tiposConvenioMap;
	}


	/**
	 * @return the rubroPresupuestal
	 */
	public double getRubroPresupuestal() {
		return rubroPresupuestal;
	}


	/**
	 * @param rubroPresupuestal the rubroPresupuestal to set
	 */
	public void setRubroPresupuestal(double rubroPresupuestal) {
		this.rubroPresupuestal = rubroPresupuestal;
	}

	/**
	 * @param codigoEliminar
	 */
	public boolean eliminarRubro(int codigoEliminar) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean resultado=tiposConvenioDao.eliminarRubro(con, codigoEliminar);
		UtilidadBD.closeConnection(con);
		return resultado;
	}


	/**
	 * @return the cuentaValConvenio
	 */
	public int getCuentaValConvenio() {
		return cuentaValConvenio;
	}


	/**
	 * @param cuentaValConvenio the cuentaValConvenio to set
	 */
	public void setCuentaValConvenio(int cuentaValConvenio) {
		this.cuentaValConvenio = cuentaValConvenio;
	}


	/**
	 * @return the cuentaValPaciente
	 */
	public int getCuentaValPaciente() {
		return cuentaValPaciente;
	}


	/**
	 * @param cuentaValPaciente the cuentaValPaciente to set
	 */
	public void setCuentaValPaciente(int cuentaValPaciente) {
		this.cuentaValPaciente = cuentaValPaciente;
	}


	/**
	 * @return the cuentaDescPaciente
	 */
	public int getCuentaDescPaciente() {
		return cuentaDescPaciente;
	}


	/**
	 * @param cuentaDescPaciente the cuentaDescPaciente to set
	 */
	public void setCuentaDescPaciente(int cuentaDescPaciente) {
		this.cuentaDescPaciente = cuentaDescPaciente;
	}


	/**
	 * @return the cuentaCxcCapitacion
	 */
	public int getCuentaCxcCapitacion() {
		return cuentaCxcCapitacion;
	}


	/**
	 * @param cuentaCxcCapitacion the cuentaCxcCapitacion to set
	 */
	public void setCuentaCxcCapitacion(int cuentaCxcCapitacion) {
		this.cuentaCxcCapitacion = cuentaCxcCapitacion;
	}


	/**
	 * @return the cuentaContableUtil
	 */
	public int getCuentaContableUtil() {
		return cuentaContableUtil;
	}


	/**
	 * @param cuentaContableUtil the cuentaContableUtil to set
	 */
	public void setCuentaContableUtil(int cuentaContableUtil) {
		this.cuentaContableUtil = cuentaContableUtil;
	}


	/**
	 * @return the cuentaContablePerd
	 */
	public int getCuentaContablePerd() {
		return cuentaContablePerd;
	}


	/**
	 * @param cuentaContablePerd the cuentaContablePerd to set
	 */
	public void setCuentaContablePerd(int cuentaContablePerd) {
		this.cuentaContablePerd = cuentaContablePerd;
	}


	/**
	 * @return the cuentaDebitoGlsRecibida
	 */
	public int getCuentaDebitoGlsRecibida() {
		return cuentaDebitoGlsRecibida;
	}


	/**
	 * @param cuentaDebitoGlsRecibida the cuentaDebitoGlsRecibida to set
	 */
	public void setCuentaDebitoGlsRecibida(int cuentaDebitoGlsRecibida) {
		this.cuentaDebitoGlsRecibida = cuentaDebitoGlsRecibida;
	}


	/**
	 * @return the cuentaCreditoGlsRecibida
	 */
	public int getCuentaCreditoGlsRecibida() {
		return cuentaCreditoGlsRecibida;
	}


	/**
	 * @param cuentaCreditoGlsRecibida the cuentaCreditoGlsRecibida to set
	 */
	public void setCuentaCreditoGlsRecibida(int cuentaCreditoGlsRecibida) {
		this.cuentaCreditoGlsRecibida = cuentaCreditoGlsRecibida;
	}


	/**
	 * @return the cuentaDebitoGlsAceptada
	 */
	public int getCuentaDebitoGlsAceptada() {
		return cuentaDebitoGlsAceptada;
	}


	/**
	 * @param cuentaDebitoGlsAceptada the cuentaDebitoGlsAceptada to set
	 */
	public void setCuentaDebitoGlsAceptada(int cuentaDebitoGlsAceptada) {
		this.cuentaDebitoGlsAceptada = cuentaDebitoGlsAceptada;
	}


	/**
	 * @return the cuentaCreditoGlsAceptada
	 */
	public int getCuentaCreditoGlsAceptada() {
		return cuentaCreditoGlsAceptada;
	}


	/**
	 * @param cuentaCreditoGlsAceptada the cuentaCreditoGlsAceptada to set
	 */
	public void setCuentaCreditoGlsAceptada(int cuentaCreditoGlsAceptada) {
		this.cuentaCreditoGlsAceptada = cuentaCreditoGlsAceptada;
	}


	public String getAseguradoraAtEv() {
		return aseguradoraAtEv;
	}


	public void setAseguradoraAtEv(String aseguradoraAtEv) {
		this.aseguradoraAtEv = aseguradoraAtEv;
	}

	
}
