
/*
 * Creado   14/10/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.actionform.pedidos.PedidosInsumosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RecargoTarifasDao;

/**
 * Clase para el manejo de un recargo un recargo de tarifas.
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class RecargoTarifas {
    
    /**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
   private static RecargoTarifasDao recargosDao;
   
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PedidosInsumosForm.class);
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Almacena el codigo del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Almacana el codigo del contrato
	 */
	private int codigoContrato;
	
	/**
	 * Almacena el codigo de la Via de Ingreso
	 */
	private int codigoViaIngreso;
	
	/**
	 * Almacena el codigo del tipo de recargo
	 */
	private int codigoTipoRecargo;
	
	/**
	 * Almacena el codigo del servicio
	 */
	private int codigoServicio;
	
	/**
	 * Almacena el codigo de la especialidad
	 */
	private int codigoEspecialidad;
	
	/**
	 * Porcentaje de recargo
	 */
	private double porcentaje;
	
	/**
	 * Valor del recargo
	 */
	private double valor;
	
	/**
	 * Almacena el codigo del recargo de la BD.
	 */
	private int codigoRecargo;
	
	
	
	public void reset()
	{
	    this.estado="";
	    this.codigoConvenio=0;
	    this.codigoContrato=0;
	    this.codigoViaIngreso=0;
	    this.codigoTipoRecargo=0;
	    this.codigoServicio=0;
	    this.codigoEspecialidad=0;
	    this.porcentaje=0.0;
	    this.valor=0.0;
	    this.codigoRecargo=0;
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
			    recargosDao = myFactory.getRecargoTarifasDao();
				wasInited = (recargosDao != null);
			}
			return wasInited;
	}
  
//constructor de la clase
//@todo insertar atributos del constructor
	public RecargoTarifas ()
	{
	    reset ();
		this.init (System.getProperty("TIPOBD"));
	}	
	
	/**
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.SqlBaseRecargoTarifasDao#insertar(java.sql.Connection, double, double, int, int, int, int, int)
	 */
	public int insertarRecargoTarifas (Connection con,
										        	 double porcentaje,
													 double valor,
													 int codigoViaIngreso,
													 String tipoPaciente,
													 int codigoEspecialidad,
													 int codigoContrato,
													 int codigoServicio,
													 int codigoTipoRecargo) throws SQLException
	{
	    int resp = 0;
	    
	    if (recargosDao == null) 
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (recargoTarifasDao - insertarRecargoTarifas)");
		}
	    
	    resp=recargosDao.insertarRecargoTarifa(con,
										            porcentaje,
										            valor,
										            codigoViaIngreso,
										            tipoPaciente,
										            codigoEspecialidad,
										            codigoContrato,
										            codigoServicio,
										            codigoTipoRecargo);
	    
	    
	    if (resp < 1) 
		{
			logger.warn("Error insertando Recargo Tarifas");
			return -1;
		}
	    
	    
	    return resp;
	    
	}
	
	/**
	 * Metodo para asignar a una Collection la consulta de Recargo Tarifa.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return Collection.
	 * @see com.princetonsa.dao.SqlBaseRecargoTarifasDao#consultaUnRecargoTarifa(java.sql.Connection,int)
	 */
	public Collection consultaUnRecargoTarifa (Connection con, int codigo)
	{
	    recargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecargoTarifasDao();
	    Collection coleccion = null;
	    
	    try 
	    {
			coleccion = UtilidadBD.resultSet2Collection(recargosDao.consultaRecargoTarifa(con, codigo));
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo Recargo Tarifa con el codigo->"+codigo+" "+e.toString());
			coleccion = null;
		}
	    
		return coleccion;
	}
	
	
	/**
	 * para obtener el codigo del recargo_tarifa actual.
	 * @see SqlBaseRecargoTarifaDao.
	 * @param con Connection con la fuente de datos
	 * @return int (codigo recargo)
	 * @see com.princetonsa.dao.SqlBaseRecargoTarifasDao#recargoActual(java.sql.Connection)
	 */  
		public int recargoActual (Connection con)
		{
		    int codigo=0;
		    codigo=recargosDao.cargarUltimoCodigoSequence(con);
		    return codigo;
		}
		
		/**
		 * Metodo para realizar la Consulta Avanzada.
		 * @param con, Connection, conexión abierta con la fuente de datos
		 * @param porcentaje. double, porcentaje de recargo de la tarifa
		 * @param valor. double, valor de recargo de la tarifa
		 * @param codigoTipoRecargo. int, código del tipo de recargo
		 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
		 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
		 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
		 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
		 * @return Collection con los datos obtenidos de la Consulta Avanzada.
		 * @see com.princetonsa.dao.SqlBaseRecargoTarifasDao#consultaAvanzadaRecargo(java.sql.Connection,double,double,int,int,int,int)
		 */
		public Collection consultaAvanzadaRecargo(Connection con,
		        												double porcentaje,
																double valor,
																int codigoViaIngreso,
																String tipoPaciente,
																int codigoEspecialidad,
																int codigoContrato,
																int codigoServicio,
																int codigoTipoRecargo,
																String nombreEspecialidad,
																String nombreServicio)
		{
		    recargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecargoTarifasDao();
		    Collection coleccion = null;
		    
		    try 
		    {
				coleccion = UtilidadBD.resultSet2Collection(recargosDao.consultaAvanzada(con,
																						        porcentaje,
																						        valor,
																						        codigoViaIngreso,
																						        tipoPaciente,
																						        codigoEspecialidad,
																						        codigoContrato,
																						        codigoServicio,
																						        codigoTipoRecargo,
																						        nombreEspecialidad,
																						        nombreServicio));
			} 
		    catch (Exception e) 
		    {
				logger.warn("Error mundo Recargo Tarifas(metodo -> consultaAvanzadaRecargo)"+e.toString());
				coleccion = null;
			}
		    
		    return coleccion;
		}
		
		/**
		 * Metodo encargado de realizar la consulta de todos los recargos insertados.
		 * @param con, Connection con lafuente de datos.
		 * @return Collection con todos los datos de rcargos de tarifas.
		 * @see com.princetonsa.dao.SqlBaseRecargoTarifasDao#consultaTodos(java.sql.Connection)
		 */
		public Collection consultarTodosRecargos (Connection con)
		{
		    recargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecargoTarifasDao();
		    Collection coleccion = null;
		    
		    try 
		    {
				coleccion = UtilidadBD.resultSet2Collection(recargosDao.consultaTodos(con));
		    }
		    
		    catch (Exception e) 
		    {
				logger.warn("Error mundo Recargo Tarifas(metodo -> consultarTodosRecargos)"+e.toString());
				coleccion = null;
			}
		    
		    return coleccion;
		    
		}
		
		
		/**
		 * Método que elimina una excepción de tarifa dado su código
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		public boolean eliminar(Connection con) throws SQLException 
		{
				boolean resp=false;
				resp=recargosDao.eliminar(con,this.getCodigoRecargo());
				return resp;
		}
		
		/**
		 * Método que modifica un recargo de tarifas 
		 * dentro de una transaccion
		 * @param con Connection con la fuente de datos.
		 * @param codigoRecargo, codigo del Recargo
		 * @param porcentaje Porcentaje de recargo.
		 * @param valor Valor de recargo.
		 * @param codigoViaIngreso Codigo de la via de ingreso.
		 * @param codigoEspecialidad Codigo de la especialidad
		 * @param codigoServicio Codigo del servicio
		 * @param codigoTipoRecargo Codigo del tipo de recargo
		 * @return true modificó de lo contrario false.
		 * @throws SQLException
		 * @see com.princetonsa.dao.RecargoTarifasDao#modificar(java.sql.Connection, int, double, double, int, int ,int, int)
		 */
		public boolean modificarTransaccional (Connection con,
		        										 int codigoRecargo,
		        										 double porcentaje,
		        										 double valor,
		        										 int codigoViaIngreso,
		        										 String tipoPaciente,
		        										 int codigoEspecialidad,
		        										 int codigoServicio,
		        										 int codigoTipoRecargo,
		        										 String estado) throws SQLException
		{
		   
		    boolean resp = false;
		    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			
			if (recargosDao == null) 
			{
				throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (recargoTarifas - modificarTransaccional )");
			}
			
//			****Iniciamos la transacción, si el estado es empezar
			boolean inicioTrans;

			if (estado.equals(ConstantesBD.inicioTransaccion)) 
			{
				inicioTrans = myFactory.beginTransaction(con);
			} 
			else 
			{
				inicioTrans = true;
			}
			
			resp = recargosDao.modificar(con, codigoRecargo, porcentaje, valor, codigoViaIngreso, tipoPaciente, codigoEspecialidad, codigoServicio, codigoTipoRecargo);
			
			if (!inicioTrans || resp == false) 
			{
			    logger.info("Error en la transaccion o no modifico(respuesta)->"+resp);
			    myFactory.abortTransaction(con);
				return false;
			}
			
			return resp;
		}
		
		/**
		 * Método que  carga  el resumen de la modificación de N recargos 
		 * @param con Connection con la fuente de datos.
		 * @param codigosModificados Codigos de los recargos que fueron modificados.
		 * @return ResultSetDecorator con lo registros.
		 * @see com.princetonsa.dao.RecargoTarifasDao#consultarRegistrosModificados(java.sql.Connection, int)
		 */
		public Collection cargarResumenModificacion(Connection con, Vector codigos)
		{
			recargosDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecargoTarifasDao();
			Collection coleccion=null;
			
			try
			{	
				if(	recargosDao.consultarRegistrosModificados(con, codigos) == null)
					return null;
				else	
					coleccion=UtilidadBD.resultSet2Collection(recargosDao.consultarRegistrosModificados(con, codigos));
			}
			catch(Exception e)
			{
				logger.warn("Error mundo Recargo Tarifas(metodo -> cargarResumenModificacion) " +e.toString());
				coleccion=null;
			}
			
			return coleccion;		
		}
    /**
     * @return Retorna codigoContrato.
     */
    public int getCodigoContrato() {
        return codigoContrato;
    }
    /**
     * @param codigoContrato Asigna codigoContrato.
     */
    public void setCodigoContrato(int codigoContrato) {
        this.codigoContrato = codigoContrato;
    }
    /**
     * @return Retorna codigoConvenio.
     */
    public int getCodigoConvenio() {
        return codigoConvenio;
    }
    /**
     * @param codigoConvenio Asigna codigoConvenio.
     */
    public void setCodigoConvenio(int codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }
    /**
     * @return Retorna codigoEspecialidad.
     */
    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }
    /**
     * @param codigoEspecialidad Asigna codigoEspecialidad.
     */
    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }
    /**
     * @return Retorna codigoServicio.
     */
    public int getCodigoServicio() {
        return codigoServicio;
    }
    /**
     * @param codigoServicio Asigna codigoServicio.
     */
    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }
    /**
     * @return Retorna codigoTipoRecargo.
     */
    public int getCodigoTipoRecargo() {
        return codigoTipoRecargo;
    }
    /**
     * @param codigoTipoRecargo Asigna codigoTipoRecargo.
     */
    public void setCodigoTipoRecargo(int codigoTipoRecargo) {
        this.codigoTipoRecargo = codigoTipoRecargo;
    }
    /**
     * @return Retorna codigoViaIngreso.
     */
    public int getCodigoViaIngreso() {
        return codigoViaIngreso;
    }
    /**
     * @param codigoViaIngreso Asigna codigoViaIngreso.
     */
    public void setCodigoViaIngreso(int codigoViaIngreso) {
        this.codigoViaIngreso = codigoViaIngreso;
    }
    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return Retorna codigoRecargo.
     */
    public int getCodigoRecargo() {
        return codigoRecargo;
    }
    /**
     * @param codigoRecargo Asigna codigoRecargo.
     */
    public void setCodigoRecargo(int codigoRecargo) {
        this.codigoRecargo = codigoRecargo;
    }
    /**
     * @return Retorna porcentaje.
     */
    public double getPorcentaje() {
        return porcentaje;
    }
    /**
     * @param porcentaje Asigna porcentaje.
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    /**
     * @return Retorna valor.
     */
    public double getValor() {
        return valor;
    }
    /**
     * @param valor Asigna valor.
     */
    public void setValor(double valor) {
        this.valor = valor;
    }
}
