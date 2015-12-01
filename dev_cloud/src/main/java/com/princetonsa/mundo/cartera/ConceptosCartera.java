
/*
 * Creado   24/05/2005
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
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.actionform.cargos.ConvenioForm;
import com.princetonsa.actionform.cartera.ConceptosCarteraForm;
import com.princetonsa.dao.ConceptosCarteraDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.RegistrarModificarGlosasDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Esta clase permite manejar los atributos y las 
 * operaciones de insertar, modificar y eliminar 
 * conceptos cartera.
 *
 * @version 1.0, 24/05/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConceptosCartera 
{
	private static ConceptosCarteraDao getConceptosCarteraDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosCarteraDao();
	}
	
	
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ConceptosCartera.class);
	
	/**
	 * DAO de este objeto, para trabajar con ConceptosPagoCartera
	 * en la fuente de datos
	 */    
    private static ConceptosCarteraDao conceptosDao;
    
    /**
     * codigo del concepto
     */
    private String codigo;
    
    /**
     * descripcion del concepto
     */
    private String descripcion;
    
    /**
     * codigo del tipo de concepto
     */
    private int codTipo;
    
    /**
     * codigo de la cuenta contable
     */
    private int codCuenta;
    
    /**
     * porcentaje del concepto
     */
    private double porcentaje;
    
    /**
     * 
     */
    private String ajusteCredito;
    
    /**
     * código de la cuenta
     */
    private double codigoCuenta;
    
    /**
     * código de la naturaleza
     */
    private int codNaturaleza;
    
    /**
     * para almacenar todos los
     * datos correspondientes al formulario.
     * estados de los datos.
     */
    private HashMap mapConceptos;
    
    
    private double cuentaDebito;
    private double cuentaCredito;
    
    /**
	 * Atributo Tipo Concepto
	 */
	private String tipoConcepto;
	
	/**
	 * Atributo Concepto General
	 */
	private String conceptoGeneral;
	
	/**
	 * Atributo Concepto Especifico
	 */
	private String conceptoEspecifico;
	
	/**
	 * 
	 */
	private int tipoCartera;
	
    
    /**
     * limpiar e inicializar atributos
     * de esta clase
     */
    public void reset ()
    {
      this.codigo = "";
      this.descripcion = "";
      this.codTipo = -1;
      this.codCuenta = -1;
      this.porcentaje = -1;
      this.mapConceptos = new HashMap ();
      this.cuentaDebito=-1;
      this.cuentaCredito=-1;
      this.codigoCuenta = -1;
      this.codNaturaleza = -1;
      this.tipoConcepto="";
      this.conceptoGeneral="";
      this.conceptoEspecifico="";
      this.tipoCartera=-2;
    }
    
    public int getTipoCartera() {
		return tipoCartera;
	}

	public void setTipoCartera(int tipoCartera) {
		this.tipoCartera = tipoCartera;
	}

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( conceptosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			conceptosDao= myFactory.getConceptosCarteraDao();
			if( conceptosDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Constructor
	 *
	 */
	public ConceptosCartera ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de pago cartera existentes.
	 * El ResultSetDecorator es pasado a un HashMap, el
	 * cual es retornado.
	 * @param con Connection, conexión con la fuente de datos
	 * @return HashMap 
	 */
	public HashMap consultarConceptosPago (Connection con,boolean esAvanzada,int codInstitucion)
	{
	    int k =0,filas=0;
	    ResultSetDecorator rs1 = null,rs = null;
	    try
		{
	        if(esAvanzada)
	            rs = conceptosDao.busquedaAvanzadaPago(con,this.codigo,this.descripcion,this.codTipo,this.codCuenta,this.porcentaje,codInstitucion);
	        else
	            rs = conceptosDao.consultaConceptosPago(con,codInstitucion);
			while(rs.next())
			{
			    this.setMapConceptos("codigoConcepto_"+k,rs.getString("codigoconcepto")+"");
			    this.setMapConceptos("codigoConceptoOld_"+k,rs.getString("codigoconcepto")+"");
			    this.setMapConceptos("codigoTipo_"+k,rs.getInt("codigotipo")+"");
			    this.setMapConceptos("codigoCuenta_"+k,rs.getInt("codigocuenta")+"");
			    this.setMapConceptos("porcentaje_"+k,rs.getDouble("porcentaje")+"");
			    this.setMapConceptos("descripcion_"+k,rs.getString("descripcion")+"");	
			    this.setMapConceptos("descripcionTipo_"+k,rs.getString("descripciontipo")+"");
			    this.setMapConceptos("tipo_"+k,"BD");//este registro pertenece a la BD.			    
			    rs1 = conceptosDao.consultaRelacionConceptosPago(con,rs.getString("codigoconcepto"),codInstitucion);
			    while(rs1.next())
			    {
			    	filas=rs1.getInt("numFilas");
			    }
			    rs1.close();
	            if(filas > 0)	
	                this.setMapConceptos("existeRelacion_"+k,"true");//este registro posee relación con aplicación de pagos.
	            else
	                this.setMapConceptos("existeRelacion_"+k,"false");
			    k ++;
			}
			rs.close();
		}	
	    catch(SQLException e)
	    {
	        logger.error(e+"Error sql en consultarConceptosPago"+e.toString());	        
	    }
	    
	    this.setMapConceptos("numRegistros",k+"");
        return mapConceptos;
	}
	
	/**
	 * Metodo implementado para realizar la insercion,
	 * de un concepto de pago cartera.
	 * @param con Connection, 
	 * @param codigoConcepto String,
	 * @param codInstitucion int, 
	 * @param descripcion String, 
	 * @param codTipo int,
	 * @param codCuenta int,
	 * @param porcentaje double,
	 * @param insertar boolean
	 * @param modificar boolean
	 * @param eliminar boolean
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean insertarModificarEliminarConceptosPagoTrans (Connection con, 
																		        String codigoConcepto, 
																		        String codigoConceptoOld,
																		        int codInstitucion, 
																		        String descripcion,
																		        int codTipo,
																		        double codCuenta,
																		        double porcentaje,
																		        boolean insertar,
																		        boolean modificar,
																		        boolean eliminar) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    boolean siInserto = false,sielimino = false,siModifico = false;
	    
	    if (conceptosDao == null) 
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos");
		}
	    
//	  ****Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;

		if (conceptosDao.equals(ConstantesBD.inicioTransaccion)) 
		{
			inicioTrans = myFactory.beginTransaction(con);
		} 
		else 
		{
			inicioTrans = true;
		}
		
		if(insertar)
		{
		    siInserto = conceptosDao.insertarConceptosPago(con,codigoConcepto,codInstitucion,descripcion,codTipo,codCuenta,porcentaje);
		    if (!inicioTrans || !siInserto) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}			
			return siInserto;
		}
		else if(modificar)
		{
		    siModifico =  conceptosDao.modicarConceptosPago(con,codigoConcepto,codigoConceptoOld,descripcion,codTipo,codCuenta,porcentaje,codInstitucion);
		    if (!inicioTrans || !siModifico) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}			
			return siModifico;
		}
		else if (eliminar)
		{
		    sielimino =  conceptosDao.eliminarConceptosPago(con,codigoConcepto,codInstitucion); 
		    if (!inicioTrans || !sielimino) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}			
			return sielimino;
		}
		else
		    logger.warn("El tipo de transacción no esta definido[Pagos]");
		
		myFactory.endTransaction(con);
		
		return false;    
	}
	
	/**
	 * Metodo implementado para realizar la insercion,
	 * de un concepto de pago cartera.
	 * @param con Connection, 
	 * @param codigoConcepto String,
	 * @param codInstitucion int, 
	 * @param descripcion String, 
	 * @param codCuenta int,
	 * @param insertar boolean
	 * @param modificar boolean
	 * @param eliminar boolean
	 * @param tipoCartera
	 * @param codTipo int,
	 * @param porcentaje double,
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean insertarModificarEliminarConceptosAjusteTrans (Connection con, 
																		        String codigoConcepto, 
																		        String codigoConceptoOld,
																		        int codInstitucion, 
																		        String descripcion,																		        
																		        double codCuenta,
																		        int codNaturaleza,
																		        boolean insertar,
																		        boolean modificar,
																		        boolean eliminar,
																		        int tipoCartera) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    boolean sielimino = false,siModifico = false;
	    
	    if (conceptosDao == null) 
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos");
		}
	    
//	  ****Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;

		if (conceptosDao.equals(ConstantesBD.inicioTransaccion)) 
		{
			inicioTrans = myFactory.beginTransaction(con);
		} 
		else 
		{
			inicioTrans = true;
		}
		
		if (eliminar)
		{
		    sielimino =  conceptosDao.eliminarConceptosAjustes(con,codigoConcepto,codInstitucion); 
		    if (!inicioTrans || !sielimino) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}			
			return sielimino;
		}
		else if(modificar)
		{
			logger.info("MODIFICANDO --> "+tipoCartera);
		    siModifico =  conceptosDao.modicarConceptosAjustes(con,codigoConcepto,codigoConceptoOld,descripcion,codCuenta,codNaturaleza,codInstitucion, tipoCartera);
		    if (!inicioTrans || !siModifico) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}			
			return siModifico;
		}
		else if (insertar)
		{
		    sielimino =  conceptosDao.insertarConceptosAjustes(con,codigoConcepto,codInstitucion,descripcion,codCuenta,codNaturaleza, tipoCartera); 
		    if (!inicioTrans || !sielimino) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}			
			return sielimino;
		}
		else
		    logger.warn("El tipo de transacción no esta definido[Ajustes]");
		
		myFactory.endTransaction(con);
		
		return false;    
	}
	
	/**
	 * Metodo para verificar sin un concepto tiene relacion
	 * con aplicaciones de pagos.
	 * @param con Connection,
	 * @param codigoConcepto String,
	 * @param codInstitucion in, código de la institución
	 * @return filas int, 
	 */
	public int existeRelacionAplicacionPagos (Connection con,String codigoConcepto,int codInstitucion)
	{
	    int filas=0;
	    ResultSetDecorator rs = conceptosDao.consultaRelacionConceptosPago(con,codigoConcepto,codInstitucion);
	    try 
	    {
            while(rs.next())
            {
                filas=rs.getInt("numFilas");
            }
            rs.close();
        } 
	    catch (SQLException e) 
	    {  
            e.printStackTrace();            
        }
        return filas;
	}
	
	/**
	 * Metodo para verificar sin un concepto tiene relacion
	 * con aplicaciones de ajustes.
	 * @param con Connection,
	 * @param codigoConcepto String,
	 * @param codInstitucion int, código de la institución
	 * @return filas int, 
	 */
	public int existeRelacionAplicacionAjustes (Connection con,String codigoConcepto,int codInstitucion)
	{
	    return conceptosDao.consultaRelacionConceptosAjustes(con,codigoConcepto,codInstitucion);
	    
	}
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de ajustes cartera existentes.
	 * El ResultSetDecorator es pasado a un HashMap, el
	 * cual es retornado.
	 * @param con Connection, conexión con la fuente de datos
	 * @return HashMap 
	 */
	public HashMap consultarConceptosAjustes (Connection con,boolean esAvanzada,int codInstitucion)
	{
	    int k =0,filas=0;
	    ResultSetDecorator rs = null;
	    try
		{
	        if(esAvanzada)
	            rs = conceptosDao.busquedaAvanzadaAjustes(con,this.codigo,this.descripcion,this.codCuenta,this.codNaturaleza,codInstitucion,this.tipoCartera);
	        else
	            rs = conceptosDao.consultaConceptosAjustes(con,codInstitucion);
	        logger.info("paso por aqui 1");
			while(rs.next())
			{
				logger.info("paso por aqui 1.2");
			    this.setMapConceptos("codigoConcepto_"+k,rs.getString("codigoconcepto")+"");
			    this.setMapConceptos("codigoConceptoOld_"+k,rs.getString("codigoconcepto")+"");			    
			    this.setMapConceptos("codigoCuenta_"+k,rs.getInt("codigocuenta")+"");			    
			    this.setMapConceptos("descripcion_"+k,rs.getString("descripcion")+"");	
			    this.setMapConceptos("codigoNaturaleza_"+k,rs.getString("codigonaturaleza")+"");
			    this.setMapConceptos("tipoCartera_"+k,rs.getString("tipo_cartera")+"");
			    this.setMapConceptos("desccuentacontable_"+k,rs.getString("desccuentacontable")+"");
			    this.setMapConceptos("nomtipocartera_"+k,rs.getString("nomtipocartera")+"");
			    this.setMapConceptos("tipo_"+k,"BD");//este registro pertenece a la BD.
			    logger.info("paso por aqui 1.3");
			    filas = conceptosDao.consultaRelacionConceptosAjustes(con,rs.getString("codigoconcepto"),codInstitucion);
			    
			    logger.info("paso por aqui 1.5");
	            if(filas > 0)	
	                this.setMapConceptos("existeRelacion_"+k,"true");//este registro posee relación con aplicación de pagos.
	            else
	                this.setMapConceptos("existeRelacion_"+k,"false");
			    k ++;
			}
			
			if(rs!=null)
			{
				rs.close();
			}
		}	
	    catch(SQLException e)
	    {
	        logger.error(e+"Error sql en consultarConceptosPago"+e.toString());	        
	    }
	    
	    this.setMapConceptos("numRegistros",k+"");
        return mapConceptos;
	}
	
    /**
     * @return Retorna codCuenta.
     */
    public int getCodCuenta() {
        return codCuenta;
    }
    /**
     * @param codCuenta Asigna codCuenta.
     */
    public void setCodCuenta(int codCuenta) {
        this.codCuenta = codCuenta;
    }
    /**
     * @return Retorna codigo.
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * @param codigo Asigna codigo.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Retorna codTipo.
     */
    public int getCodTipo() {
        return codTipo;
    }
    /**
     * @param codTipo Asigna codTipo.
     */
    public void setCodTipo(int codTipo) {
        this.codTipo = codTipo;
    }
    /**
     * @return Retorna descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion Asigna descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * @return Retorna mapConceptos.
     */
    public HashMap getMapConceptos() {
        return mapConceptos;
    }
    /**
     * @param mapConceptos Asigna mapConceptos.
     */
    public void setMapConceptos(HashMap mapConceptos) {
        this.mapConceptos = mapConceptos;
    }
    /**
     * @param key String.
     * @return Object.
     */
    public Object getMapConceptos(String key) {
        return mapConceptos.get(key);
    }
    /**
     * @param key String.
     * @param value Object.
     */
    public void setMapConceptos(String key,Object value) {
        this.mapConceptos.put(key,value);
    }
    
    
    /**
	 * @return Returns the ajusteCredito.
	 */
	public String getAjusteCredito() {
		return ajusteCredito;
	}
	/**
	 * @param ajusteCredito The ajusteCredito to set.
	 */
	public void setAjusteCredito(String ajusteCredito) {
		this.ajusteCredito = ajusteCredito;
	}
	
    /**
     * @return Retorna codigoCuenta.
     */
    public double getCodigoCuenta() {
        return codigoCuenta;
    }
    /**
     * @param codigoCuenta Asigna codigoCuenta.
     */
    public void setCodigoCuenta(double codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }
    /**
     * @return Retorna codNaturaleza.
     */
    public int getCodNaturaleza() {
        return codNaturaleza;
    }
    /**
     * @param codNaturaleza Asigna codNaturaleza.
     */
    public void setCodNaturaleza(int codNaturaleza) {
        this.codNaturaleza = codNaturaleza;
    }
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de glosas cartera existentes.
	 * El ResultSetDecorator es pasado a un HashMap, el
	 * cual es retornado.
	 * @param con
	 * @param esAvanzada
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarConceptosGlosas(Connection con, boolean esAvanzada, int codigoInstitucionInt) 
	{
		int k =0,relaciones=0;
		ResultSetDecorator rs1 = null;
		ResultSetDecorator rs = null;
		try
		{
		if(esAvanzada)
		{
            rs = conceptosDao.busquedaAvanzadaGlosas(
            		con,
            		this.codigo,
            		codigoInstitucionInt,
            		this.descripcion,
            		this.cuentaDebito,
            		this.cuentaCredito,
            		this.tipoConcepto,
            		this.conceptoGeneral,
            		this.conceptoEspecifico);
		}
        else
            rs = conceptosDao.consultaConceptosGlosas(con,codigoInstitucionInt);

		if(rs==null)
			logger.info("va error ");
		
		rs.beforeFirst();
		while(rs.next())
		{
			this.setMapConceptos("codigoConcepto_"+k,rs.getString("codigoconcepto")+"");
		    this.setMapConceptos("codigoConceptoAntiguo_"+k,rs.getString("codigoconcepto")+"");
		    this.setMapConceptos("descripcion_"+k,rs.getString("descripcion")+"");
		    this.setMapConceptos("tipoConcepto_"+k, rs.getString("tipoconcepto")+"");
		    this.setMapConceptos("conceptoGeneral_"+k, rs.getString("conceptogeneral")+"");
		    this.setMapConceptos("conceptoEspecifico_"+k, rs.getString("conceptoespecifico")+"");
		    this.setMapConceptos("cuentadebito_"+k,rs.getString("cuentadebito")+"");
		    this.setMapConceptos("cuentacredito_"+k,rs.getString("cuentacredito")+"");
		    this.setMapConceptos("numeroCuentaContable_"+k,rs.getString("numerocuentacontable")+"");
		    this.setMapConceptos("anioCuenta_"+k,rs.getString("aniocuenta")+"");
		    this.setMapConceptos("numeroCuentaContableCredito_"+k,rs.getString("numerocuentacontablecredito")+"");
		    this.setMapConceptos("anioCuentaCredito_"+k,rs.getString("aniocuentacredito")+"");
		    this.setMapConceptos("tipo_"+k,"BD");
		    
		    this.setMapConceptos("codConceptGral_"+k, rs.getString("codconceptgral")+"");
		    this.setMapConceptos("codConceptEspe_"+k, rs.getString("codconceptespe")+"");
		 
		    
		    rs1 = conceptosDao.consultaRelacionConceptosGlosas(con,rs.getString("codigoconcepto"),codigoInstitucionInt);
	   
		    
		rs1.beforeFirst();
		    while(rs1.next())
		    {
		    	relaciones=rs1.getInt("numfilas");
		    }
		    rs1.close();
            if(relaciones > 0)	
                this.setMapConceptos("existeRelacion_"+k,"true");  
            else
                this.setMapConceptos("existeRelacion_"+k,"false");
		    k++;
		}
		rs.close();
	}	
    catch(SQLException e)
    {
        logger.error(e+"Error sql en consultarConceptosGlosas"+e.toString());	        
    }
    
    this.setMapConceptos("numRegistros",k+"");
    this.setMapConceptos("registrosEliminados","0");
    return mapConceptos;
	}

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @param d
	 * @param e
	 * @param String tipoConcepto 
	 * @param String conceptoGeneral
	 * @param String conceptoEspecifico
	 */
	public boolean insertarConceptoGlosas(
			Connection con, 
			String codigoConcepto, 
			int codigoInstitucionInt, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico,
			String usuarioModifica) 
	{
		int temp=conceptosDao.insertarConceptoGlosa(
				con,
				codigoConcepto,
				codigoInstitucionInt,
				descripcion,
				cuentaDebito,
				cuentaCredito,
				tipoConcepto,
				conceptoGeneral,
				conceptoEspecifico,
				usuarioModifica);
		
		if(temp>0)
			return true;
		else
			return false;
	}

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @param d
	 * @param e
	 * @return
	 */
	public boolean modificarConceptoGlosas(
			Connection con, 
			String codigoConceptoAntiguo, 
			int codigoInstitucionInt, 
			String codigoConcepto, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico,
			String usuarioModifica) 
	{
		int temp=conceptosDao.modificarConceptoGlosas(
				con,
				codigoConceptoAntiguo,
				codigoInstitucionInt,
				codigoConcepto,
				descripcion,
				cuentaDebito,
				cuentaCredito,
				tipoConcepto,
				conceptoGeneral,
				conceptoEspecifico,
				usuarioModifica);
		
		if(temp>0)
			return true;
		else
			return false;
	}
	
	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean eliminarConceptoGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) 
	{
		int temp=conceptosDao.eliminarConceptoGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt);
		if(temp>0)
			return true;
		else
			return false;
	}
	/**
	 * @return Returns the cuentaCredito.
	 */
	public double getCuentaCredito() {
		return cuentaCredito;
	}
	/**
	 * @param cuentaCredito The cuentaCredito to set.
	 */
	public void setCuentaCredito(double cuentaCredito) {
		this.cuentaCredito = cuentaCredito;
	}
	/**
	 * @return Returns the cuentaDebito.
	 */
	public double getCuentaDebito() {
		return cuentaDebito;
	}
	/**
	 * @param cuentaDebito The cuentaDebito to set.
	 */
	public void setCuentaDebito(double cuentaDebito) {
		this.cuentaDebito = cuentaDebito;
	}

		///CONCEPTO CASTIGO
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de castigos de cartera existentes.
	 * El ResultSetDecorator es pasado a un HashMap, el
	 * cual es retornado.
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarConceptoCastigo(Connection con, boolean esAvanzada, int codigoInstitucionInt) 
	{
			int k =0,relaciones=0;
			ResultSetDecorator rs1 = null;
			ResultSetDecorator rs =null;
			try
			{
			if(esAvanzada)
	            rs = conceptosDao.busquedaAvanzadaCastigo(con,this.codigo,codigoInstitucionInt,this.descripcion,this.ajusteCredito);
	        else
	        	rs = conceptosDao.consultaConceptoCastigo(con,codigoInstitucionInt);
			
			while(rs.next())
			{
			    this.setMapConceptos("codigoConcepto_"+k,rs.getString("codigoConcepto")+"");
			    this.setMapConceptos("codigoConceptoAntiguo_"+k,rs.getString("codigoConcepto")+"");
			    this.setMapConceptos("descripcion_"+k,rs.getString("descripcion")+"");
			    this.setMapConceptos("ajusteCredito_"+k,rs.getString("ajusteCredito")+"");    
			    this.setMapConceptos("tipo_"+k,"BD");
			    rs1 = conceptosDao.consultaRelacionConceptoCastigo(con,rs.getString("codigoConcepto"),codigoInstitucionInt);
			    while(rs1.next())
			    {
			    	relaciones=rs1.getInt("numFilas");
			    }
			    rs1.close();
	            if(relaciones > 0)	
	                this.setMapConceptos("existeRelacion_"+k,"true");  
	            else
	                this.setMapConceptos("existeRelacion_"+k,"false");
			    k ++;
			}
			rs.close();
		}	
	    catch(SQLException e)
	    {
	        logger.error(e+"Error sql en consultarConceptosGlosas"+e.toString());	        
	    }
	    
	    this.setMapConceptos("numRegistros",k+"");
	    this.setMapConceptos("registrosEliminados","0");
	    return mapConceptos;
	
	}
	
	/**
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param ajusteCredito
	 * @param accion
	 * @return
	 */
	public boolean insertarModificarEliminarConceptoCastigoTrans(Connection con, int codigoInstitucionInt, String codigoConceptoAntiguo,String codigoConcepto,String descripcion, String ajusteCredito, String accion) throws SQLException 
	{	
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			boolean resultado = false;
			if (conceptosDao == null) 
			{
				throw new SQLException(	"No se pudo inicializar la conexión.");
			}
			
			boolean inicioTrans;
	
			if (conceptosDao.equals(ConstantesBD.inicioTransaccion)) 
			{
				inicioTrans = myFactory.beginTransaction(con);
			} 
			else 
			{
				inicioTrans = true;
			}
	
			if (accion=="eliminar")
			{
			    resultado =  conceptosDao.eliminarConceptoCastigo(con,codigoConceptoAntiguo); 
			    if (!inicioTrans || !resultado) 
				{
			        myFactory.abortTransaction(con);
					return false;
				}			
				return resultado;
			}
			if(accion=="insertar")
			{
				resultado = conceptosDao.insertarConceptoCastigo(con,codigoConcepto,codigoInstitucionInt,descripcion,ajusteCredito);
				if (!inicioTrans || !resultado) 
				{
					myFactory.abortTransaction(con);
					return false;
				}			
				return resultado;
			}
			else if(accion=="modificar")
			{
				resultado =  conceptosDao.modificarConceptoCastigo(con,codigoInstitucionInt,codigoConceptoAntiguo,codigoConcepto,descripcion,ajusteCredito);
				if (!inicioTrans || !resultado) 
				{
					myFactory.abortTransaction(con);
					return false;
				}			
				return resultado;
			}
			else
				logger.warn("El tipo de transacción no esta definido[Castigo]");
			myFactory.endTransaction(con);
			
			return false;    
	}
	
	
	/**
	 * @param con
	 * @param b
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarConceptosRespuestasGlosas(Connection con, boolean esAvanzada, int codigoInstitucionInt) 
	{
		int k =0,relaciones=0;
		ResultSetDecorator rs1 = null;
		ResultSetDecorator rs =null;
		try
		{
			if(esAvanzada)
	            rs = conceptosDao.busquedaAvanzadaRespuestasGlosas(con,this.codigo,codigoInstitucionInt,this.descripcion);
	        else
	            rs = conceptosDao.consultaConceptosRespuestasGlosas(con,codigoInstitucionInt);
			while(rs.next())
			{
			    this.setMapConceptos("codigoRespuestaConcepto_"+k,rs.getString("codigoRespuestaConcepto")+"");
			    this.setMapConceptos("codigoRespuestaConceptoAntiguo_"+k,rs.getString("codigoRespuestaConcepto")+"");
			    this.setMapConceptos("descripcion_"+k,rs.getString("descripcion")+"");
			    this.setMapConceptos("tipo_"+k,"BD");
			    rs1 = conceptosDao.consultaRelacionConceptosRespuestasGlosas(con,rs.getString("codigoRespuestaConcepto"),codigoInstitucionInt);
			    while(rs1.next())
			    {
			    	relaciones=rs1.getInt("numFilas");
			    }
			    rs1.close();
	            if(relaciones > 0)	
	                this.setMapConceptos("existeRelacion_"+k,"true");  
	            else
	                this.setMapConceptos("existeRelacion_"+k,"false");
			    k ++;
			}
			rs.close();
		}	
	    catch(SQLException e)
	    {
	        logger.error(e+"Error sql en consultarConceptosGlosas"+e.toString());	        
	    }
	    this.setMapConceptos("numRegistros",k+"");
	    this.setMapConceptos("registrosEliminados","0");
	    logger.info("NUMERO DE REGISTROS ---> "+k);
	    return mapConceptos;
	}

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) 
	{
		int temp=conceptosDao.eliminarConceptoRespuestaGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt);
		if(temp>0)
			return true;
		else
			return false;
	}

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @return
	 */
	public boolean insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion) 
	{
		int temp=conceptosDao.insertarConceptoRespuestaGlosas(con,codigoConcepto,codigoInstitucionInt,descripcion);
		if(temp>0)
			return true;
		else
			return false;
	}

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @param string3
	 * @param d
	 * @param e
	 * @return
	 */
	public boolean modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion) 
	{
		int temp=conceptosDao.modificarConceptoRespuestaGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt,codigoConcepto,descripcion);
		if(temp>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Método encargado de cargar los conceptos generales
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void cargarConceptosGenerales(Connection con, ConceptosCarteraForm forma, UsuarioBasico usuario)
	{
		logger.info("4555555555555555555555555555555555555 Mapa Conceptos Generales");
		logger.info(Utilidades.obtenerConceptosGenerales(con, usuario.getCodigoInstitucionInt(), ""));
		
		forma.setConceptosGenerales(Utilidades.obtenerConceptosGenerales(con, usuario.getCodigoInstitucionInt(), ""));
	}
	
	/**
	 * Método encargado de cargar los conceptos especificos
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void cargarConceptosEspecificos(Connection con, ConceptosCarteraForm forma, UsuarioBasico usuario)
	{
		logger.info("4444444444444444444444444445 Mapa Conceptos Especificos");
		logger.info(Utilidades.obtenerConceptosEspecificos(con, usuario.getCodigoInstitucionInt()));
		forma.setConceptosEspecificos(Utilidades.obtenerConceptosEspecificos(con, usuario.getCodigoInstitucionInt()));
	}

	/**
	 * Get de Tipo Concepto
	 * @return tipoConcepto
	 */
	public String getTipoConcepto() {
		return tipoConcepto;
	}

	/**
	 * Set de Tipo Concepto
	 * @param tipoConcepto
	 */
	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	/**
	 * Get de Concepto General
	 * @return conceptoGeneral
	 */
	public String getConceptoGeneral() {
		return conceptoGeneral;
	}

	/**
	 * Set de Concepto General
	 * @param conceptoGeneral
	 */
	public void setConceptoGeneral(String conceptoGeneral) {
		this.conceptoGeneral = conceptoGeneral;
	}

	/**
	 * Get de Concepto Especifico
	 * @return conceptoEspecifico
	 */
	public String getConceptoEspecifico() {
		return conceptoEspecifico;
	}

	/**
	 * Set de Concepto Especifico
	 * @param conceptoEspecifico
	 */
	public void setConceptoEspecifico(String conceptoEspecifico) {
		this.conceptoEspecifico = conceptoEspecifico;
	}
	
	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static boolean consultarConceptosGlosa (Connection con, String codConcepto)
	{
		return getConceptosCarteraDao().consultarConceptosGlosa(con, codConcepto);
	}
	
	public static String[] indicesListadoConceptos = {
		"codigoConcepto_",
		"descripcion_",
		"tipoConcepto_",
		"codConceptGral_",
		"conceptoGeneral_",
		"codConceptEspe_",
		"conceptoEspecifico_",
		"cuentadebito_" +
		"anioCuenta_",
		"numeroCuentaContable_",
		"cuentacredito_",
		"anioCuentaCredito_",
		"numeroCuentaContableCredito_",
		""
	};
}
