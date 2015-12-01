
/*
 * Creado   14/07/2005
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
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.CierreSaldoInicialCarteraDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;

/**
 * Clase para manejar los metodos para el proceso de 
 * cierre saldo inicial, para confirmar en el sistema
 * los saldos iniciales ingresados y deacuerdo a la 
 * fecha de corte para el cierre permite identificar las
 * facturas a esa fecha de corte que ya tienen toda la 
 * gestión realizada correspondiente a radicación, pago,
 * ajustes ..., y genera un indicativo como cerradas.
 *
 * @version 1.0, 14/07/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class CierreSaldoInicialCartera 
{
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(CierreSaldoInicialCartera.class);
	
	/**
	 * DAO de este objeto, para trabajar con cierre de saldos iniciales 
	 * cartera en la fuente de datos
	 */    
    private static CierreSaldoInicialCarteraDao cierreDao;
    
    /**
     * almacena el total de ajustes debito por
     * convenio
     */
    private HashMap totalAjustesDebito;
    
    /**
     * almacena el total de ajustes credito por
     * convenio
     */
    private HashMap totalAjustesCredito; 
    
    /**
     * almacena el total del valor cartera
     * por convenio.
     */
    private HashMap totalValorCartera;
    
    /**
     * almacena los pagos por convenio
     */
    private HashMap totalPagos;
    
    /**
     * almacena el total de  los ajustes debito
     */
    private double sumaAjustesDebito;
    
    /**
     * almacena el total de los ajustes credito
     */
    private double sumaAjustesCredito;
    
    /**
     * almacena el total de los valores de cartera
     */
    private double sumaValorCartera;
    
    /**
     * alamcena el total de la suma de pagos
     */
    private double sumaPagos;
    
    private double cuentaCobro;
    
    /**
     * listado de las facturas que tienen ajustes
     * registrados sin aprobar
     */
    private ArrayList factConAjustesPendientes;
    
    /**
     * listado de las facturas que poseen pagos
     * sin aprobar
     */
    private ArrayList factConPagosPendientes;
    
    /**
     * almacena los codigos de los convenios, 
     * a los cuales se les generaron los totales
     * de ajustes, pagos y valorCartera.
     */
    private ArrayList codigosConvenios;
    
    /**
     * contador del numero de registros
     * del arrayList de codigosConvenios
     */
    private int posArrayCodigos;
    
    /**
     * regula el numero de facturas 
     * que no poseen pagos aprobados
     */
    private int contPag;
    
    /**
     * regula el número de facturas que
     * no poseen ajustes aprobados 
     */
    private int contAjus;
    
    /**
     * almacena el año de cierre
     */
    private String year;
    
    /**
     * almacena el mes de cierre
     */
    private String mes;
    
    /**
     * almacena las observaciones
     */
    private String observaciones;
    
    /**
     * almacena el tipo de cierre, anual, mensual, cierre saldos iniciales.
     */
    private String tipoCierre;
    /**
     * almacena el código de la institución
     */
    private int institucion;
    /**
     * almacena el login del usuario
     */
    private String usuario;
    
    /**
     * almacena los codigos de las facturas
     * que seran cerradas
     */
    private ArrayList codFacturas;
    
    /**
     * contador del numero de facturas
     */
    private int contFact;
    
    /**
     * almacena el codigo del cierre
     */
    private int codCierre;
    
    /**
     * fecha en que se genero el cierre
     */
    private String fechaGeneracion;
    
    /**
     * hora en que se genero el cierre
     */
    private String horaGeneracion;
    
    /**
     * contador de las facturas que poseen
     * cuenta de cobro.
     */
    private int contFactConCuentaCobro;
    
    /**
     * almacena los codigos, valor_cartera y
     * convenio de las facturas anuladas
     */
    private HashMap facturasAnuladas; 
    
    /**
     * almacena la fecha del cierre, formato(yyyy-mm)
     */
    private String fechaCierre;
    
    /**
     * almacena el tipo de operacion que se realiza
     * al realizar la transaccio sobre la BD.
     */
    private String accion="generarCierre";
    
    /**
     * almacena la consulta avanzada de
     * cierres. 
     */
    private HashMap cierres;
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
      this.totalAjustesCredito = new HashMap ();
      this.totalAjustesDebito = new HashMap ();
      this.totalValorCartera = new HashMap ();
      this.facturasAnuladas = new HashMap ();
      this.totalPagos = new HashMap ();
      this.cierres=new HashMap ();
      this.sumaAjustesCredito = 0;
      this.sumaAjustesDebito = 0;
      this.sumaPagos = 0;
      this.cuentaCobro=0;
      this.sumaValorCartera = 0;
      this.posArrayCodigos = 0;
      this.contAjus = 0;
      this.contPag = 0;
      this.contFact = 0;
      this.factConAjustesPendientes = new ArrayList ();
      this.factConPagosPendientes = new ArrayList ();
      this.codigosConvenios = new ArrayList ();  
      this.codFacturas = new ArrayList ();
      this.year = "";
      this.mes = "";
      this.observaciones = "";
      this.tipoCierre = "";
      this.institucion = 0;
      this.usuario = "";
      this.codCierre = 0;
      this.fechaGeneracion = "";
      this.horaGeneracion = "";
      this.contFactConCuentaCobro = 0;   
      this.fechaCierre="";
      this.accion="generarCierre";
    }
    
    /**
     * metodo para inicializar las
     * sumas.
     */
    private void resetSumas ()
    {
        this.sumaAjustesCredito = 0;
        this.sumaAjustesDebito = 0;
        this.sumaPagos = 0;
        this.sumaValorCartera = 0;
    }
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( cierreDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cierreDao= myFactory.getCierreSaldoInicialCarteraDao();
			if( cierreDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Constructor
	 *
	 */
	public CierreSaldoInicialCartera ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * metodo para cargar el resumen desde la
	 * BD.
	 * @param con Connection
	 */
	public void cargarResumenCierre(Connection con)
	{	   
	    try 
	    {
	        ResultSetDecorator rs=cierreDao.cargarResumenCierreSaldoInicial(con,this.institucion);    
            while (rs.next())
            {
              this.year = rs.getInt("year")+"";
              this.mes = rs.getInt("mes")+"";
              this.fechaGeneracion = UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fecha_generacion")+"");
              this.horaGeneracion = rs.getString("hora_generacion");
              this.usuario = rs.getString("usuario");
              this.observaciones = rs.getString("observaciones");              
            }
        } 
	    catch (SQLException e) 
        {           
            e.printStackTrace();
        }
	}
	
	/**
	 * Metodo para verificar si existen facturas
	 * para generar cierre con valor mayor a 0 y si
	 * las facturas poseen cuenta de cobro.
	 * @param con Connection
	 * @param fecha String
	 * @param institucion int
	 * @return boolean, true si existen facturas
	 */
	public boolean validarExistenFacturas (Connection con, String fecha, int institucion,boolean esCierreInicial)
	{
		int[] validacion=cierreDao.validarExistenFacturas(con,fecha,institucion,esCierreInicial);
		this.contFactConCuentaCobro=validacion[1];
		return validacion[0]>0;
		
	}
	
		
	/**
	 * Metodo para consultar las facturas que estan dentro
	 * del mes de cierre, y verificar por cada una de ellas
	 * si tienen ajustes pendientes o pagos pendientes, y
	 * realizar por medio de diferentes metodos los totales
	 * de ajustes debito, credito y pagos aprobados.
	 * Consulta las facturas que fueronanuladas en el mes 
	 * del cierre y que fueron generadas
	 * en un mes diferente al del cierre, para obtener
	 * los valores de cartera correspondientes por convenio.
	 * @param con Connection
	 * @param institucion int
	 * @param tipoCierre String
	 */
	public boolean generarTotalesCierre (Connection con, int institucion, String tipoCierre)
	{
	    ArrayList arrayAjustes = new ArrayList ();
	    ArrayList arrayPagos = new ArrayList ();
	    boolean poseeAjustesPendientes=false,poseePagosPendientes=false;
	    boolean enTransaccion=false;
	    ResultSetDecorator rs=null;
	    
	    try
        {	        
	        arrayAjustes = ValidacionesFactura.facturaAjustesAprobadosAnulados(0,this.fechaCierre);//este array contiene todos los codigos de las facturas que poseen ajustes pendientes	        
	        arrayPagos = ValidacionesFactura.facturaTienePagos(0,this.fechaCierre);//este array contiene los códigos de las facturas que tienen pagos pendientes por aprobar
	        if(tipoCierre.equals("cierreMensual"))
	            rs=cierreDao.cargarFacutras(con,this.fechaCierre,institucion,false);  
	        if(tipoCierre.equals("cierreInicial"))
	            rs=cierreDao.cargarFacutras(con,this.fechaCierre,institucion,true);
	        enTransaccion=UtilidadBD.iniciarTransaccion(con);
	        while(rs.next())
			{           
	            this.codFacturas.add(this.contFact,rs.getInt("codigofact")+"");
	            this.contFact ++;
	            ArrayList filtro=new ArrayList();
	            filtro.add(rs.getObject("codigofact"));
	            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
	            poseeAjustesPendientes=validarFacturaConAjustesPendientes(rs.getInt("codigofact"),arrayAjustes);
	            this.setCuentaCobro(rs.getDouble("numero_cuenta_cobro"));
	            if(!poseeAjustesPendientes && rs.getDouble("numero_cuenta_cobro")!=0)
	            {		                       
		            this.totalesAjustesXConvenio(rs.getInt("codigoconvenio"),rs.getInt("codigofact"),rs.getDouble("ajustes_credito"),rs.getDouble("ajustes_debito"));//generar totales ajustes
	            }
	            if((rs.getDouble("numero_cuenta_cobro") != 0 && (tipoCierre.equals("cierreInicial")) || tipoCierre.equals("cierreMensual")))//si es de cierre saldos iniciales
		        {  
	                poseePagosPendientes=validarFacturasConPagosPendientes(rs.getInt("codigofact"),arrayPagos);
	                if(!poseePagosPendientes)
	                {
	                    this.totalPagosXConvenio(rs.getInt("codigoconvenio"),rs.getInt("codigofact"),rs.getInt("valor_pagos"));//generar totales pagos
	                }
	                this.totalValorCarteraXConvenio(rs.getInt("codigoconvenio"),rs.getDouble("valor_cartera"),false);//generar totales valores cartera
		        }
	        }
	        //Tarea de Sasaima 74181
	        /*rs=cierreDao.facturasAnuladasEnMesDeCierre(con,this.institucion,this.fechaCierre);
	        int contPosAnuladas=0;
	        while(rs.next())
            {
	            this.facturasAnuladas.put("fact_"+contPosAnuladas,rs.getInt("codigo")+"");
	            this.totalValorCarteraXConvenio(rs.getInt("convenio"),rs.getDouble("valor_cartera"),true);//restar los valores de cartera de las facturas anuladas
	            contPosAnuladas ++;
            }*/
	        
	        if(!this.factConAjustesPendientes.isEmpty() || !this.factConPagosPendientes.isEmpty())
	        {
	            logger.warn("\nEL PROCESO DE CIERRE SE CANCELO, SE ENCONTRARON FACTURAS CON AJUSTES Y/O PAGOS PENDIENTES POR APROBAR");
	            return true;
	        }
	        else if(this.factConAjustesPendientes.isEmpty() || this.factConPagosPendientes.isEmpty())
	        {
	            if(insertarCierresValoresTrans(con,enTransaccion,this.getCuentaCobro()))
	            {
	            	logger.info("finalizando la transaccion");
	            	UtilidadBD.finalizarTransaccion(con);
	            	return false;
	            	
	            }
	        }
	        UtilidadBD.abortarTransaccion(con);
        }
	    catch(SQLException e)
		{
			logger.error("Error en consultarFacturas"+e);
			UtilidadBD.abortarTransaccion(con);
			return true;
		}
	    UtilidadBD.abortarTransaccion(con);
	    return false;  
	}
	
	/**
	 * Metodo para verificar si una factura tiene
	 * ajustes pendientes por aprobar.
	 * @param codFactura int, código de la factura
	 * @param arrayAjustes ArrayList, lista de los códigos de las facturas
	 * @return boolean, true si posee ajustes pendientes.
	 */
	private boolean validarFacturaConAjustesPendientes(int codFactura,ArrayList arrayAjustes)
	{	  
	  boolean poseeAjustesPendientes=false;
	  InfoDatos temp1,temp2; 
	  
	  for(int k=0;k<arrayAjustes.size();k++)
	  {
	      temp1=(InfoDatos)arrayAjustes.get(k);      
	      if(codFactura==temp1.getCodigo())
	      { 
	          temp2=new InfoDatos(temp1.getCodigo(),temp1.getNombre(),"Ajuste(s) Pendiente(s) por Aprobar");
	          this.factConAjustesPendientes.add(this.contAjus,temp2);//[error1]los ajustes para las facturas se encuentran en estado pendiente	                      
              this.contAjus ++;	
              k=arrayAjustes.size(); 
              poseeAjustesPendientes=true;
	      }
	  }
	  return poseeAjustesPendientes;
	}
	
	/**
	 * Metodo para verificar si una factura tiene
	 * pagos pendientes por aprobar.
	 * @param codFactura int, código de la factura
	 * @param arrayPagos ArrayList, lista con los codigos de las facturas 
	 * @return boolean, true si tiene pagos pendientes
	 */
	private boolean validarFacturasConPagosPendientes (int codFactura,ArrayList arrayPagos)
	{
	    boolean poseePagosPendientes=false;
	    InfoDatos temp1,temp2; 
	    
	    for(int k=0;k<arrayPagos.size();k++)
		  {
	          temp1=(InfoDatos)arrayPagos.get(k);	        
		      if(codFactura==temp1.getCodigo())
		      {          
		          temp2=new InfoDatos(temp1.getCodigo(),temp1.getNombre(),"Pago(s) Pendiente(s) por Aprobar");
		          this.factConPagosPendientes.add(this.contPag,temp2);//[error2]los pagos para las facturas se encuentran en estado pendiente
		          this.contPag ++;	
	              k=arrayPagos.size(); 
	              poseePagosPendientes=true;
		      }
		  }
	    return poseePagosPendientes;
	}
	
	/**
	 * En este metodo se calculan los totales de 
	 * ajustes credito y debito por convenio.	 
	 * @param codigoConvenio int, codigo del convenio
	 * @param codigoFact int, codigo de la factura
	 * @param ajusteCredito double, valor del ajuste credito.
	 * @param ajusteDebito double, valor del ajuste debito.
	 */
	private void totalesAjustesXConvenio (int codigoConvenio,int codigoFact,double ajusteCredito,double ajusteDebito)
	{	 
	    boolean existeCodigo=false;
	    
	    if(ajusteCredito != 0)
        {
            if( this.totalAjustesCredito.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
            {
                this.sumaAjustesCredito = Double.parseDouble(this.totalAjustesCredito.get(codigoConvenio+"")+"") + ajusteCredito;
                this.totalAjustesCredito.put(codigoConvenio+"",this.sumaAjustesCredito+"");
            }
            else
            {                    
                existeCodigo=false;
                this.totalAjustesCredito.put(codigoConvenio+"",ajusteCredito+"");
                for(int k=0;k<this.codigosConvenios.size();k++)
                {
                    if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                       existeCodigo=true;	                              
                }
                if(!existeCodigo)
                {                        
                    this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                    this.posArrayCodigos ++;   
                }                    
            }
        } 
	    if(ajusteDebito != 0)
        {
            if( this.totalAjustesDebito.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
            {
                this.sumaAjustesDebito = Double.parseDouble(this.totalAjustesDebito.get(codigoConvenio+"")+"") + ajusteDebito;
                this.totalAjustesDebito.put(codigoConvenio+"",this.sumaAjustesDebito+"");
            }
            else
            {
                existeCodigo=false;
                this.totalAjustesDebito.put(codigoConvenio+"",ajusteDebito+"");	
                for(int k=0;k<this.codigosConvenios.size();k++)
                {
                    if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                       existeCodigo=true;	                              
                }
                if(!existeCodigo)
                {                        
                    this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                    this.posArrayCodigos ++;
                }
            }
        }
	}
	
	/**
	 * Metodo implementado para calcular el total de 
	 * pagos por convenio	 
	 * @param codigoConvenio int, codigo del convenio
	 * @param codigoFact int, código de la factura
	 * @param valorPago double, valor del pago
	 */
	private void totalPagosXConvenio(int codigoConvenio,int codigoFact,double valorPago)
	{	    
        boolean existeCodigo=false;
        
        if(valorPago != 0)
        {
            if( this.totalPagos.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
            {
                this.sumaPagos = Double.parseDouble(this.totalPagos.get(codigoConvenio+"")+"") + valorPago;
                this.totalPagos.put(codigoConvenio+"",this.sumaPagos+"");                               
            }
            else
            {
                existeCodigo=false;
                this.totalPagos.put(codigoConvenio+"",valorPago+"");	  
                for(int k=0;k<this.codigosConvenios.size();k++)
                {
                    if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                       existeCodigo=true;	                              
                }
                if(!existeCodigo)
                {                        
                    this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                    this.posArrayCodigos ++;   
                }     
            }
        }
	}
	
	/**
	 * metodo para generar el total de los valores 
	 * cartera por convenio, si hay facturas anuladas
	 * se resta del valor de cartera total por convenio. 
	 * @param codigoConvenio int, código del convenio
	 * @param valorCartera double, valor cartera de la factura	 
	 */
	private void totalValorCarteraXConvenio(int codigoConvenio,double valorCartera,boolean esRestarValorCartera)
	{
	    
	    boolean existeCodigo=false;
	    if( this.totalValorCartera.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
        {
            if(!esRestarValorCartera)
                this.sumaValorCartera = Double.parseDouble(this.totalValorCartera.get(codigoConvenio+"")+"") + valorCartera;
            else
                this.sumaValorCartera = Double.parseDouble(this.totalValorCartera.get(codigoConvenio+"")+"") - valorCartera;
            
            this.totalValorCartera.put(codigoConvenio+"",this.sumaValorCartera+"");
        }
        else
        {                    
            existeCodigo=false;
            this.totalValorCartera.put(codigoConvenio+"",valorCartera+"");
            for(int k=0;k<this.codigosConvenios.size();k++)
            {
                if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                   existeCodigo=true;	                              
            }
            if(!existeCodigo)
            {                        
                this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                this.posArrayCodigos ++;   
            }                    
        }    
	}
	
	
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos
	 */
	private void insertarCierresValoresTrans(Connection con)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true,emptyErrorsTransaction=true;
		this.resetSumas();
		
		try 
		{
			if (cierreDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CierreSaldoInicialCarteraDao - insertarCierresValoresTrans )");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");
			    emptyErrorsTransaction = false;
			}
			else
			{
			    //si se genera nuevamente un mes ya calculado, se deben eliminar los registros respectivos al mes, 
			    //para volverloas a insertar con el nuevo calculo.
			    if(this.accion.equals("recalcularCierreMensual"))//este estado se genera en el action cuando entra a volverGenerarCierre
			    {
			      int codigoCierreEliminar=cierreDao.obtenerCodigoCierreActualizar(con,Integer.parseInt(this.year),Integer.parseInt(this.mes),this.institucion);
			      logger.info("Código de cierre a eliminar "+codigoCierreEliminar);
			      if(codigoCierreEliminar==ConstantesBD.codigoNuncaValido)
			      {
			          myFactory.abortTransaction(con);  
				      logger.warn("Transaction Aborted-No se obtuvo el código del cierre para el mes a eliminar");
			          enTransaccion=false;
			          emptyErrorsTransaction = false;
			      }
			      else
			      {
			          if(enTransaccion)
			          {
				          enTransaccion=cierreDao.eliminarCierresSaldosXConvenio(con,codigoCierreEliminar);
				          if(!enTransaccion)
						  {
					        myFactory.abortTransaction(con);  
					        logger.warn("Transaction Aborted-No se eliminaron los registros por convenio pertenecientes al código de cierre "+codigoCierreEliminar);
					        emptyErrorsTransaction = false;
						  }
				          else
				          {
				              enTransaccion=cierreDao.eliminarCierresSaldos(con,codigoCierreEliminar);
				              if(!enTransaccion)
							  {
						        myFactory.abortTransaction(con);  
						        logger.warn("Transaction Aborted-No se eliminaron los registros pertenecientes al código de cierre "+codigoCierreEliminar);
						        emptyErrorsTransaction = false;
							  }
				          }
			          }
			      }
			    }	
			    
			    
			    if(enTransaccion)
			    {	    
				    //insertar los valores de el cierre general
				    enTransaccion = cierreDao.insertarCierresSaldos(con,this.year,this.mes,this.observaciones,this.tipoCierre,this.usuario,this.institucion);
				    if(!enTransaccion)
				    {
				        myFactory.abortTransaction(con);  
				        logger.warn("Transaction Aborted-No se inserto el cierre de saldos");
				        emptyErrorsTransaction = false;
				    }
				    else if(enTransaccion)
				    {
				        //realizar la insercion en el detalle siempre y cuado existan facturas para el cierre
				        if(!this.codFacturas.isEmpty() && !this.codigosConvenios.isEmpty())
				        {
					        ResultSetDecorator rs=cierreDao.ultimoCodigoCierre(con,this.institucion);
					        if(rs.next())
						    {
						        logger.info("ultimo codigo cierre->"+rs.getInt("seq_cierres_saldos"));					        
						        if(rs.getInt("seq_cierres_saldos")!=0)
						        {
						            for(int k=0;k<this.codigosConvenios.size();k++)
						    	    {	       
						            	this.resetSumas();
						            	
						    	        if(this.totalAjustesCredito.containsKey(codigosConvenios.get(k)))
						    	            this.sumaAjustesCredito = Double.parseDouble(this.totalAjustesCredito.get(codigosConvenios.get(k))+"");
						    	        if(this.totalAjustesDebito.containsKey(codigosConvenios.get(k)))
						    	            this.sumaAjustesDebito = Double.parseDouble(this.totalAjustesDebito.get(codigosConvenios.get(k))+"");
						    	        if(this.totalPagos.containsKey(codigosConvenios.get(k)))
						    	            this.sumaPagos = Double.parseDouble(this.totalPagos.get(codigosConvenios.get(k))+"");
						    	        if(this.totalValorCartera.containsKey(codigosConvenios.get(k)))
						    	            this.sumaValorCartera = Double.parseDouble(this.totalValorCartera.get(codigosConvenios.get(k))+"");
						    	        
						    	        //Insertar los valores del cierre por convenio
						    	        enTransaccion = cierreDao.insertarValoresCierreXConvenio(con,rs.getInt("seq_cierres_saldos"),Integer.parseInt(codigosConvenios.get(k)+""),this.sumaValorCartera,this.sumaAjustesDebito,this.sumaAjustesCredito,this.sumaPagos);
						    	        
						    	        if(!enTransaccion)
						    	        {
						    	            myFactory.abortTransaction(con);
						    	            emptyErrorsTransaction = false;
						    	            logger.warn("Transaction Aborted-No se insertaron los valores por convenio para el codigo de cierre "+rs.getInt("seq_cierres_saldos"));  
						    	        }
						    	    }				            
						        }				        
						    }	
					        else
					        {
					            myFactory.abortTransaction(con);   
					            logger.warn("Transaction Aborted-No se genero el código del cierre");
					            emptyErrorsTransaction = false;
					        }
				        }
				        if(!this.codFacturas.isEmpty() && this.tipoCierre.equals("cierreInicial"))//si existen facturas para el cierre, marcarlas como cerradas, solo para cierre inicial
				        {
			                for(int i=0;i<this.codFacturas.size();i++)
			                {
					            enTransaccion = cierreDao.cerrarFacturas(con,Integer.parseInt(this.codFacturas.get(i)+"")); 
				                if(!enTransaccion)
				    	        {
				    	            myFactory.abortTransaction(con);
				    	            emptyErrorsTransaction = false;
				    	            logger.warn("Transaction Aborted-No se cerraron las facturas");  
				    	        }
			                }
				        }			        
				        if(enTransaccion && emptyErrorsTransaction)
				        {			            
				            myFactory.endTransaction(con); 
				            logger.warn("Transaccion finalizada Exitosamente !");
				        }
				    }
			    }
			    else
			    {
			        logger.warn("Transaction Aborted-Se produjo errror en recalcularCierreMensual");
			        myFactory.abortTransaction(con);			      
			    }
			}
		}
		catch (SQLException e) 
		{	       
	        e.printStackTrace();
	    }
		
	}
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos
	 */
	private boolean insertarCierresValoresTrans(Connection con,boolean inTransaction, double cuentaCobro)
	{
		boolean enTransaccion=inTransaction,emptyErrorsTransaction=inTransaction;
		this.resetSumas();
		
		try 
		{
			if (cierreDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CierreSaldoInicialCarteraDao - insertarCierresValoresTrans )");
	        } 
			//si se genera nuevamente un mes ya calculado, se deben eliminar los registros respectivos al mes, 
		    //para volverloas a insertar con el nuevo calculo.
		    if(this.accion.equals("recalcularCierreMensual"))//este estado se genera en el action cuando entra a volverGenerarCierre
		    {
		      int codigoCierreEliminar=cierreDao.obtenerCodigoCierreActualizar(con,Integer.parseInt(this.year),Integer.parseInt(this.mes),this.institucion);
		      logger.info("Código de cierre a eliminar "+codigoCierreEliminar);
		      if(codigoCierreEliminar==ConstantesBD.codigoNuncaValido)
		      {
			      logger.warn("Transaction Aborted-No se obtuvo el código del cierre para el mes a eliminar");
		          enTransaccion=false;
		          emptyErrorsTransaction = false;
		      }
		      else
		      {
		          if(enTransaccion)
		          {
			          enTransaccion=cierreDao.eliminarCierresSaldosXConvenio(con,codigoCierreEliminar);
			          if(!enTransaccion)
					  {
				        logger.warn("Transaction Aborted-No se eliminaron los registros por convenio pertenecientes al código de cierre "+codigoCierreEliminar);
				        emptyErrorsTransaction = false;
					  }
			          else
			          {
			              enTransaccion=cierreDao.eliminarCierresSaldos(con,codigoCierreEliminar);
			              if(!enTransaccion)
						  {
					        logger.warn("Transaction Aborted-No se eliminaron los registros pertenecientes al código de cierre "+codigoCierreEliminar);
					        emptyErrorsTransaction = false;
						  }
			          }
		          }
		      }
		    }	
		    
		    
		    if(enTransaccion)
		    {	    
			    //insertar los valores de el cierre general
			    enTransaccion = cierreDao.insertarCierresSaldos(con,this.year,this.mes,this.observaciones,this.tipoCierre,this.usuario,this.institucion);
			    if(!enTransaccion)
			    {
			        logger.warn("Transaction Aborted-No se inserto el cierre de saldos");
			        emptyErrorsTransaction = false;
			    }
			    else if(enTransaccion)
			    {
			        //realizar la insercion en el detalle siempre y cuado existan facturas para el cierre
			        if(!this.codFacturas.isEmpty() && !this.codigosConvenios.isEmpty())
			        {
			        	logger.info("entre.");
				        ResultSetDecorator rs=cierreDao.ultimoCodigoCierre(con,this.institucion);
				        if(rs.next())
					    {
					        logger.info("ultimo codigo cierre->"+rs.getInt("seq_cierres_saldos"));
					        logger.info(" TIENE CUENTA COBRO >>>> "+cuentaCobro);
					        if(rs.getInt("seq_cierres_saldos")!=0&&cuentaCobro!=0)
					        {
					        	logger.info(" TIENE CUENTA COBRO ENTRA >>>> "+cuentaCobro);
					        	for(int k=0;k<this.codigosConvenios.size();k++)
					    	    {	 
					            	this.resetSumas();
					    	        if(this.totalAjustesCredito.containsKey(codigosConvenios.get(k)))
					    	            this.sumaAjustesCredito = Double.parseDouble(this.totalAjustesCredito.get(codigosConvenios.get(k))+"");
					    	        if(this.totalAjustesDebito.containsKey(codigosConvenios.get(k)))
					    	            this.sumaAjustesDebito = Double.parseDouble(this.totalAjustesDebito.get(codigosConvenios.get(k))+"");
					    	        if(this.totalPagos.containsKey(codigosConvenios.get(k)))
					    	            this.sumaPagos = Double.parseDouble(this.totalPagos.get(codigosConvenios.get(k))+"");
					    	        if(this.totalValorCartera.containsKey(codigosConvenios.get(k)))
					    	            this.sumaValorCartera = Double.parseDouble(this.totalValorCartera.get(codigosConvenios.get(k))+"");
					    	        
					    	        enTransaccion = cierreDao.insertarValoresCierreXConvenio(con,rs.getInt("seq_cierres_saldos"),Integer.parseInt(codigosConvenios.get(k)+""),this.sumaValorCartera,this.sumaAjustesDebito,this.sumaAjustesCredito,this.sumaPagos);
					    	        
					    	        if(!enTransaccion)
					    	        {
					    	            emptyErrorsTransaction = false;
					    	            logger.warn("Transaction Aborted-No se insertaron los valores por convenio para el codigo de cierre "+rs.getInt("seq_cierres_saldos"));  
					    	        }
					    	    }				            
					        }				        
					    }	
				        else
				        {
				            logger.warn("Transaction Aborted-No se genero el código del cierre");
				            emptyErrorsTransaction = false;
				        }
			        }
			        logger.info("1-->"+this.tipoCierre+"<---");
			        logger.info(" TIENE CUENTA COBRO PARTE 2 >>>>"+cuentaCobro+"<<<<<");
			        if(!this.codFacturas.isEmpty() && this.tipoCierre.equals(ConstantesBD.codigoTipoCierreSaldoInicialStr) && cuentaCobro==0)//si existen facturas para el cierre, marcarlas como cerradas, solo para cierre inicial
			        {
			        	logger.info("entre a las facturas");
			        	logger.info(" TIENE CUENTA COBRO ENTRA 2 >>>> "+cuentaCobro);
		                for(int i=0;i<this.codFacturas.size();i++)
		                {
				            enTransaccion = cierreDao.cerrarFacturas(con,Integer.parseInt(this.codFacturas.get(i)+"")); 
			                if(!enTransaccion)
			    	        {
			    	            emptyErrorsTransaction = false;
			    	            logger.warn("Transaction Aborted-No se cerraron las facturas");  
			    	        }
		                }
			        }			        
			        if(enTransaccion && emptyErrorsTransaction)
			        {			            
			            return true;
			        }
			    }
		    }
		    else
		    {
		        logger.warn("Transaction Aborted-Se produjo errror en recalcularCierreMensual");
		    }
		}
		catch (SQLException e) 
		{	       
	        e.printStackTrace();
	    }
		return false;
	}
	
	/**
	 * Metodo para generar el cierre anual dentro de 
	 * una transaccion.
	 * @param con Connection
	 */
	public void generarCierreAnual(Connection con)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true; 
		ResultSetDecorator rs1=null,rs2=null;
		 try 
		 {
            
		    if (cierreDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CierreSaldoInicialCarteraDao - generarCierreAnual)");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");				    
			}
			else
			{
				rs1 = cierreDao.consultarCierresGeneradosEnElAnio(con,this.institucion,Integer.parseInt(this.year));				
				if(rs1.next())
				{
				    rs1.beforeFirst();			    
				    enTransaccion = cierreDao.insertarCierresSaldos(con,this.year,this.mes,this.observaciones,this.tipoCierre,this.usuario,this.institucion);
				    if(!enTransaccion)
				    {
				        myFactory.abortTransaction(con);  
				        logger.warn("Transaction Aborted-No se inserto el cierre de saldos Anual");			        
				    }
				    else
				    {
				       rs2=cierreDao.ultimoCodigoCierre(con,this.institucion);
				       if(rs2.next())
					   {
				           if(rs2.getInt("seq_cierres_saldos")!=0)
					       {
				              while(rs1.next())
				              {	               
				                enTransaccion = cierreDao.insertarValoresCierreXConvenio(con,rs2.getInt("seq_cierres_saldos"),rs1.getInt("convenio"),rs1.getDouble("valor_cartera"),rs1.getDouble("valor_ajuste_debito"),rs1.getDouble("valor_ajuste_credito"),rs1.getDouble("valor_pago"));					    	        
					    	    if(!enTransaccion)
					    	    {
					    	      myFactory.abortTransaction(con);					    	           
					    	      logger.warn("Transaction Aborted-No se insertaron los valores por convenio para el codigo de cierre "+rs2.getInt("seq_cierres_saldos"));  
					    	    }  
						      }
					       }
					   }
				       else
				        {
				            myFactory.abortTransaction(con);   
				            logger.warn("Transaction Aborted-No se genero el código del cierre Anual");				            
				        }
				    }
				    
				    if(enTransaccion)
			        {			            
			            myFactory.endTransaction(con); 
			            logger.warn("Transaccion finalizada Exitosamente !");
			        }
				        
				}
				else
				{
				    logger.warn("Transaction Aborted-No se obtuvo resultados de cierres anuales");
			        myFactory.abortTransaction(con);	   
				}				
			}
        } catch (SQLException e) 
        {        
            e.printStackTrace();
        }
	}
	
	/**
	 * 
	 * @param con Connection
	 * @param convenio String[]
	 */
	public HashMap consultaAvanzadaCierres(Connection con,String[] convenio)
	{
	    ResultSetDecorator rs = null;
	    int numReg=0;
	    this.resetSumas();
	    rs = cierreDao.consultaAvanzadaCierresMensuales(con,Integer.parseInt(this.year),Integer.parseInt(this.mes),this.institucion,convenio);	    
	    try 
	    {
            while(rs.next())
            {
               this.cierres.put("convenio_"+numReg,rs.getInt("convenio")+"");
               this.cierres.put("valor_cartera_"+numReg,rs.getDouble("valor_cartera")+"");
               this.cierres.put("valor_ajuste_debito_"+numReg,rs.getDouble("valor_ajuste_debito")+"");
               this.cierres.put("valor_ajuste_credito_"+numReg,rs.getDouble("valor_ajuste_credito")+"");
               this.cierres.put("valor_pago_"+numReg,rs.getDouble("valor_pago")+"");
               this.cierres.put("anio_cierre_"+numReg,rs.getInt("anio_cierre")+"");               
               this.cierres.put("mes_cierre_"+numReg,UtilidadFecha.obtenerNombreMes(rs.getInt("mes_cierre"))+"");
               this.cierres.put("codigo_cierre_"+numReg,rs.getInt("codigo_cierre")+"");
               this.cierres.put("saldo_Anterior_"+numReg,rs.getDouble("saldo_Anterior")+"");
               this.cierres.put("saldo_Final_"+numReg,rs.getDouble("saldo_Final")+"");
               this.cierres.put("nombre_empresa_"+numReg,rs.getString("nombre_empresa")+"");
               this.cierres.put("nombre_convenio_"+numReg,rs.getString("nombre_convenio")+"");
               this.sumaValorCartera=this.sumaValorCartera+rs.getDouble("valor_cartera");
               this.sumaAjustesDebito=this.sumaAjustesDebito+rs.getDouble("valor_ajuste_debito");
               this.sumaAjustesCredito=this.sumaAjustesCredito+rs.getDouble("valor_ajuste_credito");
               this.sumaPagos=this.sumaPagos+rs.getDouble("valor_pago");              
               numReg++;               
            }
            this.cierres.put("numReg",numReg+"");
            this.totalAjustesCredito.put("totalValorCartera",this.sumaValorCartera+"");
            this.totalAjustesCredito.put("totalAjustesDebito",this.sumaAjustesDebito+"");
            this.totalAjustesCredito.put("totalAjustesCredito",this.sumaAjustesCredito+"");
            this.totalAjustesCredito.put("totalPagos",this.sumaPagos+"");
        }
	    catch (SQLException e) 
        {           
            e.printStackTrace();
        }
        
        return this.cierres;
	}
	
	/** Metodo para consultar si ya fue ejecutado
	 * un cierre anual o mensual.
	 * @param con Connection, conexión con la fuente de datos
	 * @param year String, año del cierre
	 * @param mes String, mes del cierre
	 * @param tipoCierre String, tipo del cierre (Anual-Inicial-Mensual)
	 * @param institucion int, código de la institución
	 * @return boolean, true si posee cierre
	 */
	 
	public boolean existenCierres (Connection con,int year,int mes,String tipoCierre,int institucion)
	{
	   return  cierreDao.existeCierreAnualMensual(con,year,mes,tipoCierre,institucion);
	}
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos
	 * @param year int, año del cierre
	 * @param tipoCierre String, tipo del cierre(Anual-Mensual-Inicial)
	 * @param institucion int, código de la institución.
	 * @return int, mes del cierre 
	 */
	public int ultimoCierreGenerado(Connection con,int year,String tipoCierre,int institucion)
	{
	    return cierreDao.ultimoCierreGenerado(con,year,tipoCierre,institucion);
	}
	
	/**
	 * Metodo para consultar los meses de los
	 * cierres generados para un año en particular.
	 * @param con Connection
	 * @return ArrayList 
	 */
	public ArrayList cierresMensualesGenerados (Connection con)
	{
	    ArrayList arrayList = new ArrayList();
	    int pos=0;
	    ResultSetDecorator rs=cierreDao.cierresMensuales(con,this.institucion,this.tipoCierre,Integer.parseInt(this.year));
	    try 
	    {
            while(rs.next())
            {               
              arrayList.add(pos,rs.getInt("mes_cierre")+"");
              pos ++;
            }
        } catch (SQLException e) 
        {        
            e.printStackTrace();
        }
	    return arrayList;
	}
	
	/**
	 * Metodo para realizar la consulta de facturas pertenecientes
	 * a una fecha de corte que ya tienen toda la gestión de cartera
	 * realizada correspondiente a radicación, pago, ajustes.
	 * Validación de los respectivos estados de ajustes y pagos, para
	 * realizar la suma de los totales solo para estados <code>aprobado</code>,
	 * para estado <code>generado</code> marcar la factura, para mostrar
	 * al usuario porque no son tomadas en cuenta, y se cancela el proceso.
	 * Para la suma de los ajuste se valida el tipo de ajuste, ya sea
	 * <code>credito</code> ó <code>debito</code> para diferenciar
	 * los dos tipos de totales. 
	 * Para las facturas que pertenecen a una cuenta de cobro se ralizan
	 * validaciones de ajustes, pagos y radicación.
	 * Para facturas que no pertenecen a una cuenta de cobro solo se realiza
	 * validación de ajustes.
	 * @param con Connection
	 * @param fecha String
	 * @param institucion int
	 * @author jarloc
	 */
	/*public boolean consultarValidarFacturas (Connection con, String fecha, int institucion)
	{	    
        ArrayList arrayPagos = new ArrayList ();
        ArrayList arrayAjustes = new ArrayList ();
        String [] ajusteTemp={},pagosTemp={};                
        ValidacionesFacturacion validaciones=new ValidacionesFacturacion();
        boolean existeCodigo=false;
        
	    try
        {
	        ResultSetDecorator rs=cierreDao.cargarFacutras(con,fecha,institucion);  
	        while(rs.next())
			{
	           this.codFacturas.add(this.contFact,rs.getInt("codigofact")+"");
	           this.contFact ++;
	           arrayAjustes = validaciones.facturaAjustesAprobadosAnulados(rs.getInt("codigofact"),fecha);//este array contiene 3 valores separados por "@"
	           if(rs.getDouble("numero_cuenta_cobro") != 0)								//1-tipoAjuste,2-estado,3-valor	
	           {		               	               	               
	               arrayPagos = validaciones.facturaTienePagos(rs.getInt("codigofact"),fecha);	               
	           }
	           if(!arrayAjustes.isEmpty())
	           {
	              for(int l=0;l<arrayAjustes.size();l++)
	              {
	                  ajusteTemp=(arrayAjustes.get(l)+"").split("@");	                  
	                  totalesAjustesXConvenio(ajusteTemp,rs.getInt("codigoconvenio"),rs.getInt("codigofact"));
	              }	                           
	           }   
	           if(!arrayPagos.isEmpty())
               {	               
	               for(int i=0;i<arrayPagos.size();i++)
                   {
                       pagosTemp = (arrayPagos.get(i)+"").split("@");                       
                       totalPagosXConvenio(pagosTemp,rs.getInt("codigoconvenio"),rs.getInt("codigofact"));                    
                   }
               }	
	           arrayPagos.clear();//se limpia el arrayPagos porque cuando vuelva a iterar y no posee cuenta de cobro, no entra a realizar la consulta y no se limpia el array
	           if( this.totalValorCartera.containsKey(rs.getInt("codigoconvenio")+"") )//verificar que exista la llave para el convenio
               {
                   this.sumaValorCartera = Double.parseDouble(this.totalValorCartera.get(rs.getInt("codigoconvenio")+"")+"") + rs.getDouble("valor_cartera");
                   this.totalValorCartera.put(rs.getInt("codigoconvenio")+"",this.sumaValorCartera+"");
               }
	           else
               {                    
                   existeCodigo=false;
                   this.totalValorCartera.put(rs.getInt("codigoconvenio")+"",rs.getDouble("valor_cartera")+"");
                   for(int k=0;k<this.codigosConvenios.size();k++)
                   {
                       if(this.codigosConvenios.get(k).equals(rs.getInt("codigoconvenio")+""))		                              
                          existeCodigo=true;	                              
                   }
                   if(!existeCodigo)
                   {                        
                       this.codigosConvenios.add(posArrayCodigos,rs.getInt("codigoconvenio")+"");
                       this.posArrayCodigos ++;   
                   }                    
               }
	           
			}
	        //imprimir();
	        if(!this.factConAjustesPendientes.isEmpty() || !this.factConPagosPendientes.isEmpty())
	        {
	            logger.warn("\nEL PROCESO DE CIERRE SE CANCELO, SE ENCONTRARON FACTURAS CON AJUSTES Y/O PAGOS PENDIENTES POR APROBAR");
	            return true;
	        }
	        else if(this.factConAjustesPendientes.isEmpty() || this.factConPagosPendientes.isEmpty())
	        {
	            insertarCierresValoresTrans(con);
	        }
	            
        }
        catch(SQLException e)
		{
			logger.error("Error en consultarFacturas"+e);
			return true;
		}
        return false; 
	}*/
	
	/**
	 * En este metodo se calculan los totales de 
	 * ajustes credito y debito por convenio.
	 * @param ajusteTemp String[], 1-tipoAjuste,2-estado,3-valor	
	 * @param codigoConvenio int, codigo del convenio
	 * @param codigoFact int, codigo de la factura
	 */
	/*private void totalesAjustesXConvenio (String[] ajusteTemp,int codigoConvenio,int codigoFact)
	{	    
	    boolean existeCodigo=false;
	    
	    if(Integer.parseInt(ajusteTemp[1])==ConstantesBD.codigoEstadoCarteraAprobado)//si no tiene ajuste pendiente de aprobar
        {		                  
            if(Integer.parseInt(ajusteTemp[0])==ConstantesBD.codigoAjusteCreditoFactura)
            {
                if( this.totalAjustesCredito.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
                {
                    this.sumaAjustesCredito = Double.parseDouble(this.totalAjustesCredito.get(codigoConvenio+"")+"") + Double.parseDouble(ajusteTemp[2]);
                    this.totalAjustesCredito.put(codigoConvenio+"",this.sumaAjustesCredito+"");
                }
                else
                {                    
                    existeCodigo=false;
                    this.totalAjustesCredito.put(codigoConvenio+"",Double.parseDouble(ajusteTemp[2])+"");
                    for(int k=0;k<this.codigosConvenios.size();k++)
                    {
                        if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                           existeCodigo=true;	                              
                    }
                    if(!existeCodigo)
                    {                        
                        this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                        this.posArrayCodigos ++;   
                    }                    
                }
            }
            if(Integer.parseInt(ajusteTemp[0])==ConstantesBD.codigoAjusteDebitoFactura)
            {
                if( this.totalAjustesDebito.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
                {
                    this.sumaAjustesDebito = Double.parseDouble(this.totalAjustesDebito.get(codigoConvenio+"")+"") + Double.parseDouble(ajusteTemp[2]);
                    this.totalAjustesDebito.put(codigoConvenio+"",this.sumaAjustesDebito+"");
                }
                else
                {
                    existeCodigo=false;
                    this.totalAjustesDebito.put(codigoConvenio+"",Double.parseDouble(ajusteTemp[2])+"");	
                    for(int k=0;k<this.codigosConvenios.size();k++)
                    {
                        if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                           existeCodigo=true;	                              
                    }
                    if(!existeCodigo)
                    {                        
                        this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                        this.posArrayCodigos ++;
                    }
                }
            }
        }
        else if(Integer.parseInt(ajusteTemp[1])==ConstantesBD.codigoEstadoCarteraGenerado)//almacenar la factura posee ajustes sin aprobar
        {
            this.factConAjustesPendientes.add(this.contAjus,codigoFact+"");//[error1]los ajustes para las facturas se encuentran en estado pendiente	                      
            this.contAjus ++;
        } 
	}*/
	
	/**
	 * Metodo implementado para calcular el total de 
	 * pagos por convenio
	 * @param PagosTemp String[], 1-estado,2-valor
	 * @param codigoConvenio int, codigo del convenio
	 * @param codigoFact int, código de la factura
	 */
	/*private void totalPagosXConvenio(String[] pagosTemp,int codigoConvenio,int codigoFact)
	{	    
        boolean existeCodigo=false;
        
	    if(Integer.parseInt(pagosTemp[0])==ConstantesBD.codigoEstadoCarteraAprobado)
        {
            if( this.totalPagos.containsKey(codigoConvenio+"") )//verificar que exista la llave para el convenio
            {
                this.sumaPagos = Double.parseDouble(this.totalPagos.get(codigoConvenio+"")+"") + Double.parseDouble(pagosTemp[1]);
                this.totalPagos.put(codigoConvenio+"",this.sumaPagos+"");                               
            }
            else
            {
                existeCodigo=false;
                this.totalPagos.put(codigoConvenio+"",Double.parseDouble(pagosTemp[1])+"");	  
                for(int k=0;k<this.codigosConvenios.size();k++)
                {
                    if(this.codigosConvenios.get(k).equals(codigoConvenio+""))		                              
                       existeCodigo=true;	                              
                }
                if(!existeCodigo)
                {                        
                    this.codigosConvenios.add(posArrayCodigos,codigoConvenio+"");
                    this.posArrayCodigos ++;   
                }     
            }
        }
        else if(Integer.parseInt(pagosTemp[0])==ConstantesBD.codigoEstadoCarteraGenerado)
        {    
           this.factConPagosPendientes.add(this.contPag,codigoFact+"");//[error2]los pagos para las facturas se encuentran en estado pendiente
            this.contPag ++;
        }	   
	}*/
	
	
    /**
     * @return Retorna sumaAjustesCredito.
     */
    public double getSumaAjustesCredito() {
        return sumaAjustesCredito;
    }
    /**
     * @param sumaAjustesCredito Asigna sumaAjustesCredito.
     */
    public void setSumaAjustesCredito(double sumaAjustesCredito) {
        this.sumaAjustesCredito = sumaAjustesCredito;
    }
    /**
     * @return Retorna sumaAjustesDebito.
     */
    public double getSumaAjustesDebito() {
        return sumaAjustesDebito;
    }
    /**
     * @param sumaAjustesDebito Asigna sumaAjustesDebito.
     */
    public void setSumaAjustesDebito(double sumaAjustesDebito) {
        this.sumaAjustesDebito = sumaAjustesDebito;
    }
    /**
     * @return Retorna sumaPagos.
     */
    public double getSumaPagos() {
        return sumaPagos;
    }
    /**
     * @param sumaPagos Asigna sumaPagos.
     */
    public void setSumaPagos(double sumaPagos) {
        this.sumaPagos = sumaPagos;
    }
    /**
     * @return Retorna sumaValorCartera.
     */
    public double getSumaValorCartera() {
        return sumaValorCartera;
    }
    /**
     * @param sumaValorCartera Asigna sumaValorCartera.
     */
    public void setSumaValorCartera(double sumaValorCartera) {
        this.sumaValorCartera = sumaValorCartera;
    }
    /**
     * @return Retorna factConAjustesPendientes.
     */
    public ArrayList getFactConAjustesPendientes() {
        return factConAjustesPendientes;
    }
    /**
     * @param factConAjustesPendientes Asigna factConAjustesPendientes.
     */
    public void setFactConAjustesPendientes(ArrayList factConAjustesPendientes) {
        this.factConAjustesPendientes = factConAjustesPendientes;
    }
    /**
     * @return Retorna factConPagosPendientes.
     */
    public ArrayList getFactConPagosPendientes() {
        return factConPagosPendientes;
    }
    /**
     * @param factConPagosPendientes Asigna factConPagosPendientes.
     */
    public void setFactConPagosPendientes(ArrayList factConPagosPendientes) {
        this.factConPagosPendientes = factConPagosPendientes;
    }
    /**
     * @return Retorna totalAjustesCredito.
     */
    public HashMap getTotalAjustesCredito() {
        return totalAjustesCredito;
    }
    /**
     * @param totalAjustesCredito Asigna totalAjustesCredito.
     */
    public void setTotalAjustesCredito(HashMap totalAjustesCredito) {
        this.totalAjustesCredito = totalAjustesCredito;
    }
    /**
     * @return Retorna totalAjustesDebito.
     */
    public HashMap getTotalAjustesDebito() {
        return totalAjustesDebito;
    }
    /**
     * @param totalAjustesDebito Asigna totalAjustesDebito.
     */
    public void setTotalAjustesDebito(HashMap totalAjustesDebito) {
        this.totalAjustesDebito = totalAjustesDebito;
    }
    /**
     * @return Retorna codigosConvenios.
     */
    public ArrayList getCodigosConvenios() {
        return codigosConvenios;
    }
    /**
     * @param codigosConvenios Asigna codigosConvenios.
     */
    public void setCodigosConvenios(ArrayList codigosConvenios) {
        this.codigosConvenios = codigosConvenios;
    }
    /**
     * @return Retorna posArrayCodigos.
     */
    public int getPosArrayCodigos() {
        return posArrayCodigos;
    }
    /**
     * @param posArrayCodigos Asigna posArrayCodigos.
     */
    public void setPosArrayCodigos(int posArrayCodigos) {
        this.posArrayCodigos = posArrayCodigos;
    }
    /**
     * @return Retorna totalValorCartera.
     */
    public HashMap getTotalValorCartera() {
        return totalValorCartera;
    }
    /**
     * @param totalValorCartera Asigna totalValorCartera.
     */
    public void setTotalValorCartera(HashMap totalValorCartera) {
        this.totalValorCartera = totalValorCartera;
    }    
    /**
     * @return Retorna totalPagos.
     */
    public HashMap getTotalPagos() {
        return totalPagos;
    }
    /**
     * @param totalPagos Asigna totalPagos.
     */
    public void setTotalPagos(HashMap totalPagos) {
        this.totalPagos = totalPagos;
    }
    /**
     * @return Retorna mes.
     */
    public String getMes() {
        return mes;
    }
    /**
     * @param mes Asigna mes.
     */
    public void setMes(String mes) {
        this.mes = mes;
    }
    /**
     * @return Retorna year.
     */
    public String getYear() {
        return year;
    }
    /**
     * @param year Asigna year.
     */
    public void setYear(String year) {
        this.year = year;
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
     * @return Retorna institucion.
     */
    public int getInstitucion() {
        return institucion;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @return Retorna tipoCierre.
     */
    public String getTipoCierre() {
        return tipoCierre;
    }
    /**
     * @param tipoCierre Asigna tipoCierre.
     */
    public void setTipoCierre(String tipoCierre) {
        this.tipoCierre = tipoCierre;
    }
    /**
     * @return Retorna usuario.
     */
    public String getUsuario() {
        return usuario;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    /**
     * @return Retorna fechaGeneracion.
     */
    public String getFechaGeneracion() {
        return fechaGeneracion;
    }
    /**
     * @param fechaGeneracion Asigna fechaGeneracion.
     */
    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    /**
     * @return Retorna horaGeneracion.
     */
    public String getHoraGeneracion() {
        return horaGeneracion;
    }
    /**
     * @param horaGeneracion Asigna horaGeneracion.
     */
    public void setHoraGeneracion(String horaGeneracion) {
        this.horaGeneracion = horaGeneracion;
    }
    /**
     * @return Retorna contFactConCuentaCobro.
     */
    public int getContFactConCuentaCobro() {
        return contFactConCuentaCobro;
    }
    /**
     * @param contFactConCuentaCobro Asigna contFactConCuentaCobro.
     */
    public void setContFactConCuentaCobro(int contFactConCuentaCobro) {
        this.contFactConCuentaCobro = contFactConCuentaCobro;
    }
    /**
     * @param fechaCierre Asigna fechaCierre.
     */
    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }   
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @return Retorna cierres.
     */
    public HashMap getCierres() {
        return cierres;
    }
    /**
     * @param cierres Asigna cierres.
     */
    public void setCierres(HashMap cierres) {
        this.cierres = cierres;
    }

    /**
     * retorna mm/yyyy del cierre inicial cartera de institucion.
     * @param con
     * @param institucion
     * @return
     */
	public String obtenerFechaCierreSaldoIncialCartera(Connection con, int institucion)
	{
		return cierreDao.obtenerFechaCierreSaldoIncialCartera(con,institucion);
	}

	public double getCuentaCobro() {
		return cuentaCobro;
	}

	public void setCuentaCobro(double cuentaCobro) {
		this.cuentaCobro = cuentaCobro;
	}
}
