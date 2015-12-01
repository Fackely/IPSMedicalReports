
/*
 * Creado   11/04/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.CuentasCobroDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase permite deacuerdo a la cuenta de cobro,
 * manejar los atributos y metodos de la modificación,
 * devolución y/o anulación de la misma.
 * Los movimientos son definidos como modificación,
 * anulación o devolución.
 *
 * @version 1.0, 11/04/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class MovimientosCuentaCobro 
{
	
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(MovimientosCuentaCobro.class);
    
    /**
	 * DAO de este objeto, para trabajar con la cuenta de cobro
	 * en la fuente de datos
	 */    
    private static CuentasCobroDao cuentasCobroDao;
    
    /**
     * almacena el numero de la cuenta de cobro.
     */
    private double numCuentaCobro;
    
    /**
     * almacena el usuario que realiza el movimiento
     */
    private String usuarioMovimiento;
    
    /**
     * almacena la fecha del movimiento.
     */
    private String fechaMovimiento;
    
    /**
     * almacena la hora del movimiento
     */
    private String horaMovimiento;
    
    /**
     * almacena las observaciones.
     */
    private String observaciones;   
    
    /**
     * Tipo de moviento.
     */
    private int tipoMovimiento;
    
    /**
     * almacena codigo del sistema de la factura
     */
    private int codigoFactura;
    /**
     * almacena consecutivo de la factura
     */
    private int consecutivoFactura;
    /**
     * almacena la fecha de elaboración de la factura o cuenta de cobro
     */
    private String fechaElaboracion;
    /**
     * almacena la hora de elaboración de la factura o cuenta de cobro
     */
    private String horaElaboracion;
    /**
     * almacena la fecha de aprobacion de la factura o cuenta de cobro
     */
    private String fechaAprobacion;
    /**
     * almacena el convenio de la factura
     */
    private String convenio;
    /**
     * almacena el valor de la factura
     */
    private double valor;
    /**
     * almacena la fecha de radicación de la cuenta de cobro
     * a la cual pertenece la factura
     */
    private String fechaRadicacion;
    
    /**
     * almacena numero de radicacion
     */
    private String numRadicacion;
    
    /**
     * almacena las observaciones de la radicación
     *
     */
    private String observacionesRadicacion;
    
    /**
     * Estado de la cuenta de cobro
     *
     */
    private int estado;
    //***********************************************************************
    
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
        this.numCuentaCobro =ConstantesBD.codigoNuncaValido;
        this.usuarioMovimiento = "";
        this.fechaMovimiento = "";
        this.horaMovimiento = "";
        this.observaciones = "";
        this.tipoMovimiento=ConstantesBD.codigoNuncaValido;
        this.estado=0;
    }
    
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( cuentasCobroDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cuentasCobroDao= myFactory.getCuentasCobroDao();
			if( cuentasCobroDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Constructor
	 *
	 */
	public MovimientosCuentaCobro ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	
	//*****************MÉTODOS DE INACTIVACIÓN DE FACTURAS*****************************
	
	
	/**
	 * Método usado para validar si la factura puede ser inactivada.
	 * Retorna una cadena de etiquetas de mensajes de error separados por '-'
	 * @param con
	 * @return
	 */
	public String validacionInactivacionFacturas(Connection con){
		return cuentasCobroDao.validacionInactivacionFacturas(con,this.codigoFactura);
	}
	
	/**
	 * Método para cargar los datos de la factura antes de ser inactivada
	 * @param con
	 * @return 1 cargado exitoso ó 0 problemas para cargar la factura
	 */
	public int cargarDatosFactura(Connection con){
		try{
			Collection datosPreInactivacion=cuentasCobroDao.cargarDatosFactura(con,this.codigoFactura);
			Iterator iterador=datosPreInactivacion.iterator();
			if(iterador.hasNext()){
				HashMap preInactivacionBD=(HashMap)iterador.next();
				this.setFechaElaboracion(preInactivacionBD.get("fecha_elaboracion")+"");
				this.setConvenio(preInactivacionBD.get("convenio")+"");
				this.setValor(Double.parseDouble(preInactivacionBD.get("valor")+""));
				this.setNumCuentaCobro(Double.parseDouble(preInactivacionBD.get("cuenta_cobro")+""));
				this.setFechaRadicacion(preInactivacionBD.get("fecha_radicacion")+"");
				this.setConsecutivoFactura(Integer.parseInt(preInactivacionBD.get("consecutivo")+""));
				return 1;
			}
			else{
				return 0;
			}
		}
		catch(Exception e){
			logger.error("Error capturando datos de la factura en MovimientosCuentaCobro "+e);
			return 0;
		}
	}
	
	
	/**
	 * Método para insertar una inactivación de facturas
	 * @param con
	 * @param institucion
	 * @param estado transaccional
	 * @return
	 */
	public int insertarInactivacionFactura(Connection con,int institucion,String estado){
		return cuentasCobroDao.insertarInactivacionFactura(con,
				this.getNumCuentaCobro(),this.getCodigoFactura(),
				this.getUsuarioMovimiento(),this.getObservaciones(),institucion,estado);
		
	}
	
	/**
	 * Método usado para registrar la inactivación de la factura en la cuenta de cobro,
	 * restando el valor de la factura en el valor inicial y el saldo de la cuenta
	 * @param con
	 * @param numCuentaCobro
	 * @param valorFactura
	 * @param estado
	 * @return
	 */
	public int actualizarInactivacionEnCuentaCobro(Connection con,int institucion,String estado){
		logger.info("Valor de actualizacvion en inacitvacion=>"+this.getValor());
		return cuentasCobroDao.actualizarInactivacionEnCuentaCobro(con,
				this.getNumCuentaCobro(),this.getValor(),institucion,estado);
	}
	//************MÉTODOS DE RADICACIÓN DE CUENTAS****************************+++
	/**
	 * Método que hace las validaciones antes de radicar una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @param institucion
	 * @return
	 */
	public String validacionRadicacion(Connection con,int institucion){
		return cuentasCobroDao.validacionRadicacion(con,this.getNumCuentaCobro(),institucion);
	}
	//*********************************************************************************
	
    
    /**
     * @return Retorna fechaMovimiento.
     */
    public String getFechaMovimiento() {
        return fechaMovimiento;
    }
    /**
     * @param fechaMovimiento Asigna fechaMovimiento.
     */
    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
    /**
     * @return Retorna horaMovimiento.
     */
    public String getHoraMovimiento() {
        return horaMovimiento;
    }
    /**
     * @param horaMovimiento Asigna horaMovimiento.
     */
    public void setHoraMovimiento(String horaMovimiento) {
        this.horaMovimiento = horaMovimiento;
    }
    /**
     * @return Retorna numCuentaCobro.
     */
    public double getNumCuentaCobro() {
        return numCuentaCobro;
    }
    /**
     * @param numCuentaCobro Asigna numCuentaCobro.
     */
    public void setNumCuentaCobro(double numCuentaCobro) {
        this.numCuentaCobro = numCuentaCobro;
    }
    /**
     * @return Retorna observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones Asigna observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @return Retorna usuarioMovimiento.
     */
    public String getUsuarioMovimiento() {
        return usuarioMovimiento;
    }
    /**
     * @param usuarioMovimiento Asigna usuarioMovimiento.
     */
    public void setUsuarioMovimiento(String usuarioMovimiento) {
        this.usuarioMovimiento = usuarioMovimiento;
    }
    
	/**
	 * @return Returns the codigoFactura.
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}
	/**
	 * @param codigoFactura The codigoFactura to set.
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}
	/**
	 * @return Returns the consecutivoFactura.
	 */
	public int getConsecutivoFactura() {
		return consecutivoFactura;
	}
	/**
	 * @param consecutivoFactura The consecutivoFactura to set.
	 */
	public void setConsecutivoFactura(int consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}
	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the fechaElaboracion.
	 */
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}
	/**
	 * @param fechaElaboracion The fechaElaboracion to set.
	 */
	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}
	/**
	 * @return Returns the fechaAprobacion.
	 */
	public String getFechaAprobacion() {
		return fechaAprobacion;
	}
	/**
	 * @param fechaAprobacion The fechaAprobacion to set.
	 */
	public void setFechaAprobacion(String fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	/**
	 * @return Returns the fechaRadicacion.
	 */
	public String getFechaRadicacion() {
		return fechaRadicacion;
	}
	/**
	 * @param fechaRadicacion The fechaRadicacion to set.
	 */
	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}
	
	/**
	 * @return Returns the valor.
	 */
	public double getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}

	
	/**
	 * @return Returns the numRadicacion.
	 */
	public String getNumRadicacion() {
		return numRadicacion;
	}
	/**
	 * @param numRadicacion The numRadicacion to set.
	 */
	public void setNumRadicacion(String numRadicacion) {
		this.numRadicacion = numRadicacion;
	}
	
	
	/**
	 * @return Returns the observacionesRadicacion.
	 */
	public String getObservacionesRadicacion() {
		return observacionesRadicacion;
	}
	/**
	 * @param observacionesRadicacion The observacionesRadicacion to set.
	 */
	public void setObservacionesRadicacion(String observacionesRadicacion) {
		this.observacionesRadicacion = observacionesRadicacion;
	}
	/**
	 * Metodo para cargar los movimiento que tiene una cuenta de cobro.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 */
	public void cargarMovimientoCxC(Connection con, double cuentaCobro, int institucion) 
	{
		ResultSetDecorator rs=cuentasCobroDao.cargarMovimientoCxC(con,cuentaCobro,institucion);
		try
		{
			if(rs.next())
			{
				this.numCuentaCobro=cuentaCobro;
				this.usuarioMovimiento=rs.getString("usuario");
				this.fechaMovimiento=rs.getString("fecha_movimiento");
				this.horaMovimiento=rs.getString("hora_movimiento");
				this.observaciones=rs.getString("observacion");
				this.tipoMovimiento=rs.getInt("tipo_movimiento");
			}
			else
			{
				reset();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error cargando el objeto moviemientos CXC"+e);
		}
	}
	
	/**
	 * @return Returns the tipoMovimiento.
	 */
	public int getTipoMovimiento() {
		return tipoMovimiento;
	}
	/**
	 * @param tipoMovimiento The tipoMovimiento to set.
	 */
	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	
	/**
	 * @return Returns the horaElaboracion.
	 */
	public String getHoraElaboracion() {
		return horaElaboracion;
	}
	/**
	 * @param horaElaboracion The horaElaboracion to set.
	 */
	public void setHoraElaboracion(String horaElaboracion) {
		this.horaElaboracion = horaElaboracion;
	}
	/**
	 * Método para cargar los datos de una cuenta de cobro
	 * @param con
	 * @return indicado entero sobre el éxito de la transacción
	 */
	public int cargarCuentaCobro(Connection con,int institucion){
		try{
			ResultSetDecorator rs=cuentasCobroDao.cargarCuentaCobro(con,this.getNumCuentaCobro(),institucion);
			int exitoso=0;
			while(rs.next()){
				exitoso=1;
				this.setNumCuentaCobro(rs.getDouble("numero_cuenta_cobro"));
				this.setConvenio(rs.getInt("codigoconvenio")+ConstantesBD.separadorTags+rs.getString("nombreconvenio"));
				this.setFechaElaboracion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_elaboracion")));
				this.setHoraElaboracion(rs.getString("hora_elaboracion"));
				this.setValor(rs.getDouble("valor_inicial_cuenta"));
				this.setFechaAprobacion( UtilidadFecha.conversionFormatoFechaAAp( rs.getString("fecha_aprobacion") ) );
				this.setEstado(rs.getInt("estado"));
				//quedan faltando + atributos
				
				
			}
			return exitoso;
		}
		catch(SQLException e){
			logger.error("Error cargando la cuenta de cobro en MovimientosCuentaCobro: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para registrar la radicación de una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @param fechaRadicacion
	 * @param numRadicacion
	 * @param usuarioRadica
	 * @param observacionRadicacion
	 * @param institucion
	 * @return indicador entero sobre el éxito de la transacción
	 */
	public int insertarRadicacion(Connection con,int institucion){
		return cuentasCobroDao.insertarRadicacion(con,
				this.getNumCuentaCobro(),this.getFechaRadicacion(),
				this.getNumRadicacion(),this.getUsuarioMovimiento(),
				this.getObservacionesRadicacion(),institucion);
	}
	/**
	 * @return Returns the estado.
	 */
	public int getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}
}
