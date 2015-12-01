
/*
 * Creado   1/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.AprobacionPagosCarteraDao;
import com.princetonsa.dao.DaoFactory;

/** 
 * Clase para realizar la aprobacion de 
 * pagos previamente ingresados.
 * Se permite la aprobacion de ajustes que solo
 * tengan estado Pendiente.
 *
 * @version 1.0, 1/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class AprobacionPagosCartera 
{
   /**
    * manejador de logs de la clase
    */
    private Logger logger=Logger.getLogger(AprobacionPagosCartera.class);  
    /**
	 * DAO de este objeto, para trabajar con aprobacion pagos
	 * cartera en la fuente de datos
	 */    
    private static AprobacionPagosCarteraDao aprobacionDao;
    /**
     * tipo de documento correspondiente a la
     * aplicacion de pago
     */
    private String tipo;
    /**
     * número del documento
     */
    private String documento;
    /**
     * consecutivo de la aplicación de pago
     */
    private String numeroAplicacionPago;
    /**
     * código del convenio
     */
    private String codConvenio;
    /**
     * mapa que almacena toda la información de las
     * consultas para la aprobación
     */
    private HashMap mapaApliPagos;
    /**
     * institucion del usuario
     */
    private int institucion;
    /**
     * código de la factura
     */
    private int codFactura;
    /**
     * valor del pago de la factura
     */
    private double valorPagoFactura;
    /**
     * valor del pago por cxc
     */
    private double saldoCxC;
    /**
     * fecha de la aprobación
     */
    private String fechaAprobacion;
    /**
     * login del usurio
     */
    private String usuario;
    
    /**
     * Cambio por Tarea 88494
     */
    private String estadoPago;
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
       this.codConvenio="";
       this.documento="";
       this.numeroAplicacionPago="";
       this.tipo="";
       this.mapaApliPagos=new HashMap();
       this.institucion=ConstantesBD.codigoNuncaValido;
       this.codFactura=ConstantesBD.codigoNuncaValido;
       this.valorPagoFactura=ConstantesBD.codigoNuncaValidoDouble;
       this.saldoCxC=ConstantesBD.codigoNuncaValidoDouble;
       this.fechaAprobacion="";
       this.usuario="";
       this.estadoPago="";
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( aprobacionDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aprobacionDao= myFactory.getAprobacionPagosCarteraDao();
			
			if( aprobacionDao!= null )
				return true;
		}

		return false;
	}
	/**
	 * metodo para generar la consulta de aplicación 
	 * de pagos cartera empresas
	 * @param con Connection 
	 * @return HashMap
	 */
	public HashMap generarConsultaAplicacionPagos(Connection con,boolean esAprobacion)
	{
	    this.mapaApliPagos=aprobacionDao.generarConsultaAplicacionPagos(con,this.tipo,this.documento,this.numeroAplicacionPago,this.codConvenio,this.estadoPago, esAprobacion);
	    for(int i=0;i<Utilidades.convertirAEntero(this.mapaApliPagos.get("numRegistros")+"");i++)
	    {
	      this.mapaApliPagos.put("fecha_"+i, UtilidadFecha.conversionFormatoFechaAAp(this.mapaApliPagos.get("fecha_"+i)+""));  
	    }
	    return mapaApliPagos; 
	}
	/**
	 * metodo para generar la consulta del detalle
	 * de la aplicación del pago
	 * @param con Connection
	 * @return HashMap
	 */
	public HashMap generarConsultaDetalleAplicacionPagos(Connection con)
	{
	  HashMap mapa=new HashMap();
	  mapa=aprobacionDao.generarConsultaDetalleAplicacionPagos(con,Integer.parseInt(this.numeroAplicacionPago));	 
	  return mapa;
	}
	/**
	 * metodo para consultar los conceptos de 
	 * aplicación de pagos
	 * @param con Connection
	 * @param codigoAplicacionPago 
	 * @return HashMap
	 */
	public HashMap generarConsultaConceptosAplicacionPagos(Connection con, int codigoAplicacionPago)
	{
	    HashMap mapa=new HashMap();
	    mapa=aprobacionDao.generarConsultaConceptosAplicacionPagos(con, this.institucion,codigoAplicacionPago);	    
		return mapa;
	}
	/**
	 * metodo para validar que el total de pago
	 * de la cuenta de cobro se encuentre distribuido 
	 * en su totalidad en las facturas
	 * @param con Connection
	 * @return HashMap
	 */ 
	public HashMap valoresAplicacionPagosBalanceados(Connection con)
	{
	    HashMap mapa=new HashMap();
	    mapa=aprobacionDao.cuentasCobroPorAplicacionPagosStr(con,Integer.parseInt(this.numeroAplicacionPago),this.institucion);
	    return mapa;    
	}
	/**
	 * metodo para generar la aprobacion del pago.
	 * incluye, actualización del estado del pago.
	 * actualización del valor del pago de la factura
	 * actualización del saldo de la cuenta de cobro
	 * @param con Connection
	 * @return boolean
	 */
	public boolean generarAprobacionTrans(Connection con)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		
		try 
		{
			if (aprobacionDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}
			else
			{	
			    enTransaccion=aprobacionDao.actualizarEstadoPago(con,Integer.parseInt(this.numeroAplicacionPago));			    
			    if(!enTransaccion)
		        {
		            myFactory.abortTransaction(con);
				    logger.warn("Transaction Aborted-No se actualizo el estado del pago");	
		        }
			    else
			    {
			        enTransaccion=aprobacionDao.insertarAprobacion(con,Integer.parseInt(this.numeroAplicacionPago),this.usuario,UtilidadFecha.conversionFormatoFechaABD(this.fechaAprobacion),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
			        if(!enTransaccion)
			        {
			            myFactory.abortTransaction(con);
					    logger.warn("Transaction Aborted-No se inserto la aprobacion del pago");	
			        }
			        else
			        {
				        HashMap mapaFact=new HashMap();
				        mapaFact=(HashMap)this.mapaApliPagos.get("facturas");
				        if(mapaFact!=null)
				        {
					        for(int k=0;k<Integer.parseInt(mapaFact.get("numRegistros")==null?"0":mapaFact.get("numRegistros")+"");k++)
					        {
					            this.codFactura=Integer.parseInt(mapaFact.get("codigo_factura_"+k)+"");
					            this.valorPagoFactura=Double.parseDouble(mapaFact.get("valor_pago_"+k)+"");
					            enTransaccion=aprobacionDao.actualizarValorPagoFactura(con,this.codFactura,this.valorPagoFactura);
						        if(!enTransaccion)
						        {
						            myFactory.abortTransaction(con);
								    logger.warn("Transaction Aborted-No se actualizo valor del pago de la factura "+this.codFactura);	
						        }
					        }
				        }
					    if(enTransaccion)
					    {
					        HashMap mapaCxC=new HashMap();
					        mapaCxC=(HashMap)this.mapaApliPagos.get("cxc");
					        double numCxC=0;
					        if(mapaCxC!=null)
					        {
						        for(int k=0;k<Integer.parseInt(mapaCxC.get("numRegistros")==null?"0":mapaCxC.get("numRegistros")+"");k++)
						        {
							        this.saldoCxC=Double.parseDouble(mapaCxC.get("valor_pago_cxc_"+k)+"");
							        numCxC=Double.parseDouble(mapaCxC.get("numero_cuenta_cobro_"+k)+"");
						            enTransaccion=aprobacionDao.actualizarSaldoCxC(con,numCxC,this.institucion,this.saldoCxC);
							        if(!enTransaccion)
							        {
							            myFactory.abortTransaction(con);
									    logger.warn("Transaction Aborted-No se actualizo el saldo de la CxC "+numCxC);	
							        }
						        }
					        }
					    }
			        }
			    }	
			}
			if(enTransaccion)
	        {
			    myFactory.endTransaction(con);
			    logger.warn("End Transaction");	
	        }
		}
		catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    }
        return enTransaccion;    
	}
	/**
	 * Constructor
	 *
	 */
	public AprobacionPagosCartera ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
    /**
     * @param codConvenio Asigna codConvenio.
     */
    public void setCodConvenio(String codConvenio) {
        this.codConvenio = codConvenio;
    }
    /**
     * @param documento Asigna documento.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    /**
     * @param numeroAplicacionPago Asigna numeroAplicacionPago.
     */
    public void setNumeroAplicacionPago(String numeroAplicacionPago) {
        this.numeroAplicacionPago = numeroAplicacionPago;
    }
    /**
     * @param tipo Asigna tipo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * @param mapaApliPagos Asigna mapaApliPagos.
     */
    public void setMapaApliPagos(HashMap mapaApliPagos) {
        this.mapaApliPagos = mapaApliPagos;
    }
    /**
     * @param mapaApliPagos Asigna mapaApliPagos.
     */
    public void setMapaApliPagos(String key, Object value) {
        this.mapaApliPagos.put(key, value);
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @param codFactura Asigna codFactura.
     */
    public void setCodFactura(int codFactura) {
        this.codFactura = codFactura;
    }
    /**
     * @param valorPagoFactura Asigna valorPagoFactura.
     */
    public void setValorPagoFactura(double valorPagoFactura) {
        this.valorPagoFactura = valorPagoFactura;
    }
    /**
     * @param saldoCxC Asigna saldoCxC.
     */
    public void setSaldoCxC(double saldoCxC) {
        this.saldoCxC = saldoCxC;
    }
    /**
     * @param fechaAprobacion Asigna fechaAprobacion.
     */
    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
	public String getEstadoPago() {
		return estadoPago;
	}
	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}
}
