/*
 * Created on 19/09/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.FormasPagoDao;

/**
 * @author artotor
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormasPago 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(FormasPago.class);
	
	/**
	 * DAO de este objeto, para trabajar con FormasPago
	 * en la fuente de datos
	 */    
    private static FormasPagoDao formasPagoDao;
    
    /**
     * Mapa para manejar lar formas de pago
     */
    private HashMap mapaFormasPago;
    
    //variables para menejar la informacion de un registro específico.
    
    /**
     * Consecutivo del registro
     */
    private int consecutivo;
    
    /**
     * Institicion del registro
     */
    private int institucion;
    
    /**
     * Codigo del registro.
     */
    private String codigo;
    
    /**
     * Descripcion del registro.
     */
	private String descripcion;
	
	/**
	 * codigo tipo Detalle del registor
	 */
	private int tipoDetalle;
	
	/**
	 * cuenta contable
	 */
	private int cuentaContable;
	
	/**
	 * 
	 */
	private String indicativoConsignacion;

	/**
	 * Permite definir las formas de pago que se incluyen en una solicitud de traslado a caja de recuado
	 */
	private String trasladoCajaRecaudo;

	/**
	 * nombre del tipo detalle del registro
	 */
	private String nomTipoDetalle;
	
	/**
	 * Boolean que indica si un registro se encuentra activo o no.
	 */
	private boolean activo;
	
	private int numeroRegistros;

	
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.mapaFormasPago = new HashMap ();
    	this.consecutivo=ConstantesBD.codigoNuncaValido;
    	this.codigo="";
    	this.institucion=ConstantesBD.codigoNuncaValido;
    	this.descripcion="";
    	this.tipoDetalle=ConstantesBD.codigoNuncaValido;
    	this.nomTipoDetalle="";
    	this.activo=false;
    	this.numeroRegistros=0;
    	this.cuentaContable=ConstantesBD.codigoNuncaValido;
    	this.indicativoConsignacion=ConstantesBD.acronimoSi;
    	this.trasladoCajaRecaudo=ConstantesBD.acronimoSi;
    }
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( formasPagoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			formasPagoDao= myFactory.getFormasPagoDao();
			if( formasPagoDao!= null )
				return true;
		}
		return false;
	}
	
	public FormasPago()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
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
	 * @return Returns the mapaFormasPago.
	 */
	public HashMap getMapaFormasPago() {
		return mapaFormasPago;
	}
	/**
	 * @param mapaFormasPago The mapaFormasPago to set.
	 */
	public void setMapaFormasPago(HashMap mapaFormasPago) {
		this.mapaFormasPago = mapaFormasPago;
	}
	/**
	 * @return Returns the nomTipoDetalle.
	 */
	public String getNomTipoDetalle() {
		return nomTipoDetalle;
	}
	/**
	 * @param nomTipoDetalle The nomTipoDetalle to set.
	 */
	public void setNomTipoDetalle(String nomTipoDetalle) {
		this.nomTipoDetalle = nomTipoDetalle;
	}
	/**
	 * @return Returns the tipoDetalle.
	 */
	public int getTipoDetalle() {
		return tipoDetalle;
	}
	/**
	 * @param tipoDetalle The tipoDetalle to set.
	 */
	public void setTipoDetalle(int tipoDetalle) {
		this.tipoDetalle = tipoDetalle;
	}
	
	/**
	 * @return Returns the numeroRegistros.
	 */
	public int getNumeroRegistros() {
		return numeroRegistros;
	}
	/**
	 * @param numeroRegistros The numeroRegistros to set.
	 */
	public void setNumeroRegistros(int numeroRegistros) {
		this.numeroRegistros = numeroRegistros;
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 */
	public void cargarFormasPagos(Connection con, int codigoInstitucionInt) 
	{
		ResultSetDecorator rs=formasPagoDao.cargarFormasPago(con,codigoInstitucionInt);
		try
		{
			int i=0;
			while(rs.next())
			{
				mapaFormasPago.put("consecutivo_"+i,rs.getString("consecutivo"));
				mapaFormasPago.put("codigo_"+i,rs.getString("codigo"));
				mapaFormasPago.put("descripcion_"+i,rs.getString("descripcion"));
				mapaFormasPago.put("tipodetalle_"+i,rs.getString("tipodetalle"));
				mapaFormasPago.put("nomtipodetalle_"+i,rs.getString("nomtipodetalle"));
				mapaFormasPago.put("activo_"+i,rs.getString("activo"));
				mapaFormasPago.put("existerelacion_"+i,UtilidadValidacion.formaPagoUtilizadaEnRecibosCaja(con,rs.getInt("consecutivo"))+"");
				mapaFormasPago.put("tiporegistro_"+i,"BD");
				mapaFormasPago.put("cuentacontable_"+i, rs.getString("cuentacontable"));
				mapaFormasPago.put("indicativoconsignacion_"+i, rs.getString("indicativoconsignacion"));
				mapaFormasPago.put("traslado_caja_recaudo_"+i, rs.getString("traslado_caja_recaudo"));
				mapaFormasPago.put("req_traslado_caja_recaudo_"+i, rs.getString("req_traslado_caja_recaudo"));
				i++;
			}
			this.numeroRegistros=i;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD [FormasPago.java]"+e.getStackTrace());
		}

	}

	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivo) 
	{
		return formasPagoDao.eliminarRegistro(con,consecutivo);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param trasladoCajaRecaudo
	 * @param tipodetalle
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, String codigo, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo) 
	{
		if(formasPagoDao.existeModificacion(con,consecutivo,codigo,descripcion,tipo,activo, cuentaContable, indicativoConsignacion, trasladoCajaRecaudo, reqTrasladoCajaRecaudo))
		{
			this.cargarFormaPago(con,consecutivo);
			return true;
		}
		return false;
	}

	/**
	 * @param con
	 * @param consecutivo2
	 */
	private void cargarFormaPago(Connection con, int consecutivo) 
	{
		ResultSetDecorator rs=formasPagoDao.cargarFormaPago(con,consecutivo);
		try 
		{
			while (rs.next())
			{
				this.consecutivo=consecutivo;
				this.codigo=rs.getString("codigo");
				this.institucion=rs.getInt("institucion");
				this.descripcion=rs.getString("descripcion");
				this.tipoDetalle=rs.getInt("tipodetalle");
				this.nomTipoDetalle=rs.getString("nomtipodetalle");
				this.activo=rs.getBoolean("activo");
				this.cuentaContable=rs.getInt("cuentacontable");
				this.indicativoConsignacion= rs.getString("indicativoconsignacion");
				this.trasladoCajaRecaudo= rs.getString("traslado_caja_recaudo");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param trasladoCajaRecaudo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo)
	{
		return formasPagoDao.modificarRegistro(con,consecutivo,codigo,descripcion,tipo,activo, cuentaContable, indicativoConsignacion, trasladoCajaRecaudo, reqTrasladoCajaRecaudo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param tipo
	 * @param trasladoCajaRecaudo
	 * @param boolean1
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo) 
	{
		return formasPagoDao.insertarRegistro(con,codigo,codigoInstitucionInt,descripcion,tipo,activo, cuentaContable, indicativoConsignacion, trasladoCajaRecaudo, reqTrasladoCajaRecaudo);
	}

	/**
	 * @return the cuentaContable
	 */
	public int getCuentaContable() {
		return cuentaContable;
	}

	/**
	 * @param cuentaContable the cuentaContable to set
	 */
	public void setCuentaContable(int cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	/**
	 * @return the indicativoConsignacion
	 */
	public String getIndicativoConsignacion() {
		return indicativoConsignacion;
	}

	/**
	 * @param indicativoConsignacion the indicativoConsignacion to set
	 */
	public void setIndicativoConsignacion(String indicativoConsignacion) {
		this.indicativoConsignacion = indicativoConsignacion;
	}

	/**
	 * @return Retorna atributo trasladoCajaRecaudo
	 */
	public String getTrasladoCajaRecaudo()
	{
		return trasladoCajaRecaudo;
	}

	/**
	 * @param trasladoCajaRecaudo Asigna atributo trasladoCajaRecaudo
	 */
	public void setTrasladoCajaRecaudo(String trasladoCajaRecaudo)
	{
		this.trasladoCajaRecaudo = trasladoCajaRecaudo;
	}

}
