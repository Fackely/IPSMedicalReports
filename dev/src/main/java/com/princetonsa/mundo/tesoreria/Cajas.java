/*
 * Created on 12/09/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadValidacion;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dao.CajasDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author artotor
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cajas 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(Cajas.class);
	
	/**
	 * DAO de este objeto, para trabajar con Cajas
	 * en la fuente de datos
	 */    
    private static CajasDao cajasDao;
	
	/**
	 * Mapa paara manejar las cajas definidas en el sistema
	 */
	private HashMap mapaCajas;
	
	
	//variables para menejar la informacion de un registro específico.
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
	private int tipo;
	/**
	 * Tipo caja.
	 */
	private String tipodescripcion;
	/**
	 * Se encuentra activa o no.
	 */
	private boolean activo;
	
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
	
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.mapaCajas = new HashMap ();
    	this.consecutivo=ConstantesBD.codigoNuncaValido;
    	this.codigo=ConstantesBD.codigoNuncaValido;
    	this.institucion=ConstantesBD.codigoNuncaValido;
    	this.descripcion="";
    	this.tipo=ConstantesBD.codigoNuncaValido;
    	this.tipodescripcion="";
    	this.activo=false;
    	this.centroAtencion=ConstantesBD.codigoNuncaValido;
    }
     
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( cajasDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cajasDao= myFactory.getCajaDao();
			if( cajasDao!= null )
				return true;
		}
		return false;
	}
	
	public Cajas()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * @param con
	 */
	public void cargarInformacion(Connection con,int institucion) 
	{
		ResultSetDecorator rs=cajasDao.cargarInformacion(con,institucion, this.centroAtencion);
		try
		{
			int i=0;
			while(rs.next())
			{
				mapaCajas.put("consecutivo_"+i,rs.getString("consecutivo"));
				mapaCajas.put("codigo_"+i,rs.getString("codigo"));
				mapaCajas.put("institucion_"+i,rs.getString("institucion"));
				mapaCajas.put("descripcion_"+i,rs.getString("descripcion"));
				mapaCajas.put("tipo_"+i,rs.getString("tipo"));
				mapaCajas.put("descripciontipo_"+i,rs.getString("descripciontipo"));
				mapaCajas.put("activo_"+i,rs.getBoolean("activo")+"");
				mapaCajas.put("centroatencion_"+i,rs.getString("centroatencion"));
				mapaCajas.put("existerelacion_"+i,(UtilidadValidacion.cajaUtilizadaEnCajerosCaja(con,rs.getInt("consecutivo"))||UtilidadValidacion.cajaUtilizadaEnRecibosCaja(con,rs.getInt("consecutivo")))+"");
				mapaCajas.put("tiporegistro_"+i,"BD");
				mapaCajas.put("valorBase_"+i,rs.getString("valor_base"));
				i++;
			}
			this.mapaCajas.put("numeroregistros",i+"");
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD", e);
		}
	}

	/**
	 * @return Returns the mapaCajas.
	 */
	public HashMap getMapaCajas() {
		return mapaCajas;
	}
	/**
	 * @param mapaCajas The mapaCajas to set.
	 */
	public void setMapaCajas(HashMap mapaCajas) {
		this.mapaCajas = mapaCajas;
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivoCaja) 
	{
		return cajasDao.eliminarRegistro(con,consecutivoCaja);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param valorBase 
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase)
	{
		return cajasDao.modificarRegistro(con,consecutivo,codigo,descripcion,tipo,activo, valorBase);
	}

	/**
	 * @param con
	 * @param i
	 * @param j
	 * @param codigoInstitucionInt
	 * @param valorBase Valor base en caja
	 * @param string
	 * @param k
	 * @param boolean1
	 * @return
	 */
	public boolean insertarRegistro(Connection con, int codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo, Double valorBase)
	{
		return cajasDao.insertarRegistro(con,codigo,codigoInstitucionInt,descripcion,tipo,activo, this.centroAtencion, valorBase);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param valorBase
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase) 
	{
		if(cajasDao.existeModificacion(con,consecutivo,codigo,descripcion,tipo,activo, valorBase))
		{
			this.cargarCaja(con,consecutivo);
			return true;
		}
		return false;
	}
	/**
	 * Metodo que carga una determinada caja dado su consecutivo.
	 * @param con
	 * @param consecutivo2
	 */
	private void cargarCaja(Connection con, int consecutivo) 
	{
		ResultSetDecorator rs=cajasDao.cargarCaja(con,consecutivo);
		try {
			while (rs.next())
			{
				this.consecutivo=consecutivo;
				this.codigo=rs.getInt("codigo");
				this.institucion=rs.getInt("institucion");
				this.descripcion=rs.getString("descripcion");
				this.tipo=rs.getInt("tipo");
				this.tipodescripcion=rs.getString("descripciontipo");
				this.activo=rs.getBoolean("activo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the consecutivo.
	 */
	public int getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
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
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * @return Returns the tipodescripcion.
	 */
	public String getTipodescripcion() {
		return tipodescripcion;
	}
	/**
	 * @param tipodescripcion The tipodescripcion to set.
	 */
	public void setTipodescripcion(String tipodescripcion) {
		this.tipodescripcion = tipodescripcion;
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
}
