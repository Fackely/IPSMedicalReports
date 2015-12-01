
/*
 * Creado   7/04/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.CuentasCobroDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseCuentasCobroDao;
import com.princetonsa.dto.cartera.DtoFiltroReporteCuentasCobro;
import com.princetonsa.dto.cartera.DtoResultadoReporteCuentaCobro;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;

/**
 * Clase para manejar deacuerdo a los <code>registros de facturaciï¿½n</code> 
 * generados, realizar el armado de cuentas de cobro para el 
 * modulo cartera. Toda la informacion que se maneje en esta 
 * corresponde a la instituciï¿½n del usuario respectivo. 
 * Una cuenta de cobro esta formada por <code>N facturas</code> 
 * todas del mismo convenio.
 *
 * @version 1.0, 7/04/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * @author <a href="mailto:dramirez@PrincetonSA.com">Diego Ramirez</a>
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class CuentasCobro 
{
    /**
	 * DAO de este objeto, para trabajar con la cuenta de cobro
	 * en la fuente de datos
	 */    
    private static CuentasCobroDao cuentasCobroDao;
     
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(CuentasCobro.class);
			
	/**
	 * Numero de la cuenta de cobro.
	 */
    private double numeroCuentaCobro;
    /**
     * Codigo de la factura
     */
    private int codigoFactura;
	/**
     * almacena el codigo y nombre del convenio (Responsable).
     */
    private InfoDatosInt convenio;
    
    /**
     * El acronimo de la identificacion del responsable
     */
    private String acronimoIdResponsable;
    /**
     * Descripocion de la Identificacion del Responsable
     */
    private String nomIdResponsable;
    /**
     * Numero de identificacion del responsable
     */
    private String numIdResponsable;
    /**
     * codigo y nombre de la via de ingreso.
     */
    private InfoDatosInt viaIngreso;
    
    /**
     * Array list para almacenar las diferentes vias de ingreso que puede tener una cuenta de cobro.
     */
    private ArrayList viasIngreso;
    
    /**
     * Estado de la cuenta de cobro.
     */
    private int estado;
    
    /**
     * fecha de elaboraciï¿½n de la cuenta de cobro.
     */
    private String fechaElaboracion;
    
    /**
     * hora de la elaboraciï¿½n de la cuenta de cobro.
     */
    private String horaElaboracion;
    
    /**
     * 
     */
    private int codigoCentroAtencion;
    
    /**
     * 
     */
    private String nombreCentroAtencion;
    /**
     * responsable de generar la cuenta de cobro
     */
    private String usuarioGenera;
    
    /**
     * almacena la fecha de radicaciï¿½n.
     */
    private String fechaRadicacion;
    
    
    /**
     * almacena el numero de la radicacion
     */
    private String numeroRadicacion;
    
    /**
     * almacena la hora de radicaciï¿½n.
     */
    private String horaRadicacion;
    
    /**
     * almacena el usuario que radica la cuenta de cobro.
     */
    private String usuarioRadica;
    
    /**
     * almacena el valor inicial de la cuenta de cobro
     * calculado previamente y que corresponde atodas las 
     * facturas.
     */
    private double valInicialCuenta; 
    
    /**
     * almacena el saldo de la cuenta de cobro, en la gneraciï¿½n
     * de la misma es igual al valInicialCuenta.
     */
    private double saldoCuenta;
    
    
    /**
     * Rango de fecha
     */
    private String fechaInicial;
    
    /**
     * Rango de fecha
     */
    private String fechaFinal;
    
    /**
     * observaciones ingresadas en la generaciï¿½n de la
     * cuenta de cobro.
     */
    private String observacionesGen;
    
    /**
     * observaciones ingresadas en la radicaciï¿½n de la
     * cuenta de cobro.
     */
    private String observacionesRad;
    
    /**
     * almacena el objeto de factura.
     */
    private Collection facturas;
    
    /**
     * almacena el objeto de factura.
     */
    private MovimientosCuentaCobro movimiento;
    
    private HashMap facturasCxC=new HashMap();
    
    /**
     * almacena datos de movimientos de 
     * cuentas de cobro.
     * Codigos de facturas que seran liberadas.
     */
    private HashMap mapMovimientos;
    
    /**
     * Variable para manejar el numero de facturas de una cuenta de cobro.
     */
    private int numeroFacturas;
    
    /**
     * Variable para manejar la fecha de elaboracion de una factura
     */
    private String fechaElaboracionFactura;
    
    /**
     * Variable para almacena la via de ingreso de una factura.
     */
    private String viaIngresoFactura;
    
    
    /**
     * consecutivo Factura para consulta
     */
    private String consecutivoFac;
    
    /**
     * Direccion del convenio asociado a la cuenta de cobro
     */
    private String direccionConvenio;
    
    /**
     * Telï¿½fono Convenio asociado a la cuenta de cobro
     */
    private String telefonoConvenio;
    
    private static final String cargarCuentaCobro="SELECT " +
						"cxc.numero_cuenta_cobro as numero_cuenta_cobro, " +
						"cxc.convenio as codigoconvenio, " +
						"c.nombre as nombreconvenio, " +
						"'' as tipo_id_responsable, " +
						"'' as nom_id_responsable, " +
						"t.numero_identificacion as numero_id_responsable, " +
						"cxc.estado as estado, " +
						"to_char(cxc.fecha_elaboracion, '"+ConstantesBD.formatoFechaBD+"') as fecha_elaboracion, " +
						"cxc.hora_elaboracion as hora_elaboracion, " +
						"cxc.usuario_genera as usuario_genera, " +
						//"to_char(cxc.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') as fecha_radicacion, " +
						"cxc.fecha_radicacion as fecha_radicacion, " +
						"cxc.numero_radicacion as numero_radicacion, " +
						"cxc.usuario_radica as usuario_radica, " +
						"cxc.valor_inicial_cuenta as valor_inicial_cuenta, " +
						"cxc.saldo_cuenta as saldo_cuenta, " +
						"to_char(cxc.fecha_inicial, '"+ConstantesBD.formatoFechaBD+"') as fecha_inicial, " +
						"to_char(cxc.fecha_final, '"+ConstantesBD.formatoFechaBD+"') as fecha_final, " +
						"CASE WHEN cxc.obs_generacion IS NULL THEN '' ELSE cxc.obs_generacion END as obs_generacion, " +
						"cxc.obs_radicacion as obs_radicacion," +
						"case when cxc.centro_atencion is null then -1 else cxc.centro_atencion end as codigocentroatencion," +
						"case when cxc.centro_atencion is null then 'Todos' else administracion.getnomcentroatencion(cxc.centro_atencion) end as nombrecentroatencion," +
						"e.direccion as direccion, " +
						"e.telefono as telefono " +
						"FROM  cartera.cuentas_cobro cxc " +
						"inner join facturacion.convenios c on (c.codigo=cxc.convenio) " +
						"inner join facturacion.empresas e on (c.empresa=e.codigo) " +
						"inner join facturacion.terceros t on(e.tercero=t.codigo) " +
						"where cxc.numero_cuenta_cobro=? and cxc.institucion=?";
    
    private static final String facturasCuentasCobro="SELECT " +
							" f.codigo as codigofactura," +
							" f.consecutivo_factura as consecutivo," +
							" f.centro_aten as codigocentroatencion," +
							" f.estado_paciente AS codigo_estado_paciente, " +
							" getnomcentroatencion(f.centro_aten) as centroatencion," +
							" to_char(f.fecha,'dd/mm/yyyy') as fecha," +
							" CASE WHEN p.codigo IS NULL THEN '' ELSE p.tipo_identificacion || ' ' || p.numero_identificacion END AS identificacion_paciente, " +
							" getnombrepersona(p.codigo) as nombrepersona," +
							" f.via_ingreso as viaingreso," +
							" getnombreviaingreso(f.via_ingreso) as nombreviaingreso," +
							" f.valor_cartera as valorcartera," +
							" f.valor_total as valortotal," +
							" f.tipo_factura_sistema as facturasistema, " +
							" coalesce(f.tipo_monto,"+ConstantesBD.codigoNuncaValido+") AS codigo_tipo_monto, " +
							" coalesce(f.valor_bruto_pac,0) AS valor_bruto_pac " +
						"FROM " +
							"facturas f " +
						"LEFT OUTER " +
							"join personas p on(f.cod_paciente=p.codigo) " +
						"WHERE " +
							"numero_cuenta_cobro=? and institucion=? " +
						"ORDER BY " +
							" nombreviaingreso  asc,f.consecutivo_factura asc ";
    
    private String fechaAnulacion;
    private String horaAnulacion;
    private String usuarioAnulacion;
    private String motivoAnulacion;
    private String tipoAnulacion;
    private String nitConvenio;
    
    
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
        this.convenio = new InfoDatosInt();
        this.viaIngreso = new InfoDatosInt();
        this.codigoFactura=ConstantesBD.codigoNuncaValido;
        this.estado = ConstantesBD.codigoNuncaValido;
        this.fechaElaboracion = "";
        this.horaElaboracion = "";
        this.fechaInicial = "";
        this.fechaFinal = "";
        this.fechaRadicacion = "";
        this.horaRadicacion = "";
        this.observacionesGen = "";
        this.observacionesRad = "";
        this.saldoCuenta = 0.0;
        this.usuarioGenera = "";
        this.usuarioRadica = "";
        this.valInicialCuenta = 0.0;
        this.facturas = new ArrayList();
        this.viasIngreso=new ArrayList();
        this.movimiento = new MovimientosCuentaCobro();
        this.facturasCxC=new HashMap();
        this.mapMovimientos = new HashMap(); 
        this.numeroFacturas=0;
        this.fechaElaboracionFactura="";
        this.viaIngresoFactura="";
        this.consecutivoFac="";
        this.facturasCxC.put("numFacRechazadas","0");
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.nombreCentroAtencion="Todos";
        this.direccionConvenio = "";
        this.telefonoConvenio = "";
    }
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
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
	public CuentasCobro ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	//********************************** METODOS MOVIMIENTOS CXC **************************************//
	
	/**
	 *metodo para ralizar las validaciones de devoluciï¿½n de CXC
	 *-la cuenta de cobro debe tener estado radicada
	 *-no debe tener glosas registradas
	 *-no debe tener ajustes pendientes por aprobar
	 *-en caso de tener ajustes aprobados, generados despues
	 *de radicar la cxc, validar que la sumatoria de los ajustes
	 *debito y credito = 0
	 *@param con Connection, conexion con la fuente de datos
	 *@param numCxc double, numero de la cuenta de cobro. 
	 * @param institucion
	 */
	public void validacionesDevolucionCxc(Connection con, double numCxc, int institucion)
	{
	    ResultSetDecorator rs = null;
	    int k=0,codFactura=0;
	    this.getMapMovimientos().clear();
	    this.setMapMovimientos("numFact",-1+"");
	    rs=cuentasCobroDao.facturasCuentaCobro(con,numCxc,institucion);
	    try 
	    {
            while(rs.next())
            {
                this.setMapMovimientos("codFact_"+k,rs.getInt("codigofactura")+"");                
                this.setMapMovimientos("numFact",k+"");
                k ++;
            }
       
		    for(k=0;k<=Integer.parseInt(this.getMapMovimientos("numFact")+"");k++)
		    {	    
		        codFactura = Integer.parseInt(this.getMapMovimientos("codFact_"+k)+"");
		        rs = cuentasCobroDao.validacionesDevolucionCXC(con,codFactura,true,false,false,institucion);
		        if(rs.next())
		            this.setMapMovimientos("existeGlosa_"+k,"true");
		        else
		            this.setMapMovimientos("existeGlosa_"+k,"false");
		    }
		    for(k=0;k<=Integer.parseInt(this.getMapMovimientos("numFact")+"");k++)
		    {	    
		        codFactura = Integer.parseInt(this.getMapMovimientos("codFact_"+k)+"");
		        rs = cuentasCobroDao.validacionesDevolucionCXC(con,codFactura,false,true,false,institucion);
		        if(rs.next())
		            this.setMapMovimientos("existeMovimientoPago_"+k,"true");
		        else
		            this.setMapMovimientos("existeMovimientoPago_"+k,"false");
		        
		    }		    
		   for(k=0;k<=Integer.parseInt(this.getMapMovimientos("numFact")+"");k++)
		    {
		        codFactura = Integer.parseInt(this.getMapMovimientos("codFact_"+k)+"");		        
		        boolean tieneAjustes = ValidacionesFactura.facturaTieneAjustesPendientes(con,codFactura);
		        if(tieneAjustes)
		            this.setMapMovimientos("existeAjustePendiente_"+k,"true");
		        else
		            this.setMapMovimientos("existeAjustePendiente_"+k,"false");
		    }
		   for(k=0;k<=Integer.parseInt(this.getMapMovimientos("numFact")+"");k++)
		    {
		        codFactura = Integer.parseInt(this.getMapMovimientos("codFact_"+k)+"");		        
		        boolean tieneAjustes = ValidacionesFactura.facturaTieneCastigoCartera(con,codFactura);
		        if(tieneAjustes)
		            this.setMapMovimientos("existeCastigo_"+k,"true");
		        else
		            this.setMapMovimientos("existeCastigo_"+k,"false");
		    }
		    for(k=0;k<=Integer.parseInt(this.getMapMovimientos("numFact")+"");k++)
		    {	    
		        codFactura = Integer.parseInt(this.getMapMovimientos("codFact_"+k)+"");
		        rs = cuentasCobroDao.validacionesDevolucionCXC(con,codFactura,false,false,true,institucion);
		        if(rs.next())
		        {
		            if(!(rs.getInt("total")+"").equals(""))
		            {		               
		                if(rs.getInt("total") != 0)
		                    this.setMapMovimientos("ajustesDifCero_"+k,"true");
		                else
		                    this.setMapMovimientos("ajustesDifCero_"+k,"false");
		            }
		        }
		        else
		            this.setMapMovimientos("ajustesDifCero_"+k,"false");
		    }	
		    
	    } catch (SQLException e) 
        {
          e.printStackTrace();
        }	   
	}
	
	
	/**
	 * Metodo implementado para liberar las facturas
	 * correspondientes a una cuenta de cobro especifica,
	 * y relacionarlas nuevamente segun los parametros
	 * que han sido seleccionados, por Vï¿½a de Ingreso,
	 * por Fecha Inicial-Fecha Final.
	 *
	 *@param numCXC, double codigo de la cuenta de cobro
	 *@param esRelacionarFact, boolean true para ejecutar los metodos de 
	 *						   relacionar las facturas
	 *@param esLiberarFact, boolean true para ejecutar los metodos
	 *						de liberar las facturas
	 *@param esModificarCxC, boolean true para ejecutar el proceso 
	 *						de modificar la CXC
	 * @param institucion
	 * @param sumaValInicial
     *@return boolean
     */
    public boolean liberarRelacionarFacturas(Connection con, 
		            										double numCXC,
		            										ArrayList arrayMod,
		            										ArrayList arrayAdc,
		            										boolean esRelacionarFact,
		            										boolean esLiberarFact,
		            										boolean esModificarCxC,
		            										String estado,
		            										String usuario,
		            										double valInicial, 
		            										int institucion,
															boolean saldos)
    {       
        int codFact=0;  
        boolean existenErrores = false;
        try
        {
            DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			boolean inicioTrans;
			if (cuentasCobroDao==null)
			{
				throw new SQLException ("No se pudo inicializar la conexiï¿½n con la fuente de datos (CuentasCobroDao - liberarRelacionarFacturas )");
			}
			if (estado.equals(ConstantesBD.inicioTransaccion))
			{
				inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
				inicioTrans=true;				
			}
			if (!inicioTrans )
			{
			    myFactory.abortTransaction(con);
			    existenErrores = true;
				return false;
			}
			else
			{
	            if(esLiberarFact)
	            {            
	                ResultSetDecorator rs=cuentasCobroDao.facturasCuentaCobro(con,numCXC,institucion);
	                if(rs.next())//si existen facturas para liberar, de lo contrario no se realiza el proceso
	                {
			            boolean liberoFacturas = cuentasCobroDao.liberarFacturasCXC(con,numCXC,institucion);
			            if(!liberoFacturas)
			            {
			                logger.warn("[liberarRelacionarFacturas]No se liberaron las facturas, posiblemente ya se liberaron :ï¿½");		                
			                myFactory.abortTransaction(con);
			                existenErrores = true;
			                return false;
			            }            
	                }
	            }
	            if(esRelacionarFact)
	            {	
	                /*el index <numFacturas> proviene del metodo del mundo <consultarFacturasViaIngreso>
	                 *que es cuando se consultan las facturas segun el rango de busqueda en la CXC*/
		            for(int i = 0;i < Integer.parseInt(this.getMapMovimientos("numFacturas")+""); i++)
		            {																							
		                /*el index <activarCheckBox_> proviene del metodo del action <guardarSalirModificaciones>
		                 donde se copia el HashMap de la forma, que contiene las facturas que estan seleccionadas,
		                 o no, para relacionarlas a la respectiva cuenta*/
		                if((this.getMapMovimientos("activarCheckBox_"+i)+"").equals("on"))
		                {
		                    codFact= Integer.parseInt(this.getMapMovimientos("codigo_"+i)+"");
		                    boolean esModificado = cuentasCobroDao.actulizarNumeroCxCFactura(con,numCXC,codFact,institucion);
		                    if(saldos&&esModificado)
		                    	esModificado=cuentasCobroDao.actualizaValorCartera(con,Double.parseDouble(this.getMapMovimientos("valor_cartera_"+i)+""),Integer.parseInt(this.getMapMovimientos("codigo_"+i)+""),institucion);
		                    
		                    if(!esModificado)
		                    {
		                        logger.info("[liberarRelacionarFacturas]No se actualizo la CXC para la factura->"+codFact+" :ï¿½");
		                        myFactory.abortTransaction(con);
		                        existenErrores = true;
				                return false;
		                    }
		                    
		                }
		            }
	            }
	            /*proviene del metodo del action <guardarSalirModificaciones>, donde se verifica que vias de 
	             * ingreso se le eliminaron a la cuenta de cobro, para actualizar la tabla de de vias_ingreso_cxc*/
	            if(! arrayMod.isEmpty())
	            {
	                for(int k=0;k<arrayMod.size();k++)
	                {
	                    boolean liberoCxC = cuentasCobroDao.borrarRegistroCXCdeViasIngresoCXC(con,numCXC,Integer.parseInt(arrayMod.get(k)+""),institucion);  
	                    if(!liberoCxC)
	                    {
	                        logger.info("[liberarRelacionarFacturas]No se liberaron las CxC en la tabla vias_ingreso_cxc para el numeroCXC->"+numCXC+" :ï¿½");
	                        myFactory.abortTransaction(con);
	                        existenErrores = true;
			                return false;
	                    }
	                }
	            }
	            /*proviene del metodo del action <guardarSalirModificaciones>, donde se verifica que vias de 
	             * ingreso se adicionaron a la cuenta de cobro, para actualizar la tabla de de vias_ingreso_cxc*/
	            if(! arrayAdc.isEmpty())
	            {
	                for(int k=0;k<arrayAdc.size();k++)
	                {
	                    boolean adicionoCxC = cuentasCobroDao.adicionarRegistroCXCaViasIngresoCXC(con,numCXC,Integer.parseInt(arrayAdc.get(k)+""),institucion);  
	                    if(!adicionoCxC)
	                    {
	                        logger.info("[liberarRelacionarFacturas]No se adicionaron las CxC en la tabla vias_ingreso_cxc para el numeroCXC->"+numCXC+" :ï¿½");
	                        myFactory.abortTransaction(con);
	                        existenErrores = true;
			                return false;
	                    }
	                }
	            } 
	            
	            if(esModificarCxC)
	            {
	               boolean modificoCxC = cuentasCobroDao.modificarCuentaCobro(con,numCXC,
																	                        UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal),
																	                        UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial),																	                        
																	                        this.observacionesGen,usuario,valInicial,
																	                        UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracion));
	                if(!modificoCxC)
	                {
	                    logger.info("[liberarRelacionarFacturas]No se modifico la CxC para el numeroCXC->"+numCXC+" :ï¿½");
                        myFactory.abortTransaction(con);
                        existenErrores = true;
		                return false; 
	                }
	            }
			}
			
			if(!existenErrores && !estado.equals(ConstantesBD.continuarTransaccion))
			    myFactory.endTransaction(con);
        }
        catch (SQLException e) 
		{
			e.printStackTrace();
		}
        
        return true;
    }
    
    /**
     * Metodo para insertar una cuenta de cobro
     * a la tabla vias_ingreso_cxc
     * @param con Connection, conexiï¿½n con la fuente de datos
     * @param numCXC double, nï¿½mero de la cuenta de cobro
     * @param institucion int, cï¿½digo de la instituciï¿½n
     * @param viaIngreso int, cï¿½digo de la via de ingreso
     * @return boolean, true efectivo
     * @author jarloc
     */
    public boolean insertarCxCAViasIngresoCxC (Connection con, double numCXC, int institucion,int viaIngreso)
    {
        return cuentasCobroDao.adicionarRegistroCXCaViasIngresoCXC(con,numCXC,viaIngreso,institucion);         
    }
    
    /**
     * Metodo para consultar todas las facturas que se van 
     * a liberar, y tenian relacion a la cuenta de cobro 
     * indicada.
     * 
     * @param con Connection con la fuente de datos
     * @param numCXC double codigo de la cuenta de cobro
     * @param institucion
     */
    public void consultarFactALiberar (Connection con, double numCXC, int institucion)
    {
        int k = 0;
        try
        {
	        ResultSetDecorator rs=cuentasCobroDao.facturasCuentaCobro(con,numCXC,institucion);  
	        while(rs.next())
			{
	            this.setMapMovimientos("codigoFactConRelacionCXC_"+k,rs.getInt("codigofactura")+"");                
	            k ++;
			}
	        this.setMapMovimientos("numeroFactConRelacionCXC",k+""); 
        }
        catch(SQLException e)
		{
			logger.error("Error en liberarRelacionarFacturas"+e);
		}
    }
    
    /**
     * Metodo para anular una cuenta de cobro, Transaccion
     * @param con Connection con lafuente de datos
     * @param numCxc, double numero de la cuenta de cobro
     * @param estado String estado de la transaccion
     * @param usuario2
     * @return boolean, true efectivo
     */
    public boolean anularCuentaCobro (Connection con, 
											            double numCxc, 
											            String estado,
											            String usuario,
											            String fecha,
											            String hora,
											            String observacion, 
											            int tipo, UsuarioBasico usuario2,
											            int institucion)
    {
        
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans;
		boolean existenErrores = false;
		
		try
		{
			if (cuentasCobroDao==null)
			{
				throw new SQLException ("No se pudo inicializar la conexiï¿½n con la fuente de datos (CuentasCobroDao - anularCuentaCobro )");
			}
			if (estado.equals(ConstantesBD.inicioTransaccion))
			{
				inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
				inicioTrans=true;				
			}
			if (!inicioTrans )
			{
			    myFactory.abortTransaction(con);
			    existenErrores = true;
				return false;
			}
			else
			{
		        ResultSetDecorator rs=cuentasCobroDao.facturasCuentaCobro(con,numCxc,usuario2.getCodigoInstitucionInt());
		        if(rs.next())//si existen facturas para liberar, de lo contrario no se realiza el proceso
		        {
		            boolean liberoFacturas = cuentasCobroDao.liberarFacturasCXC(con,numCxc,institucion);
		            if(!liberoFacturas)
		            {
		                logger.warn("[anularCXC]No se liberaron las facturas, posiblemente ya se liberaron :ï¿½");		                
		                myFactory.abortTransaction(con);
		                existenErrores = true;
		                return false;
		            }            
		        } 
		        
		        boolean cambiarEstado = cuentasCobroDao.anularCXC(con,numCxc,institucion);
		        if(!cambiarEstado)
		        {
		            logger.warn("[anularCXC]No se actualizo el estado de la CXC :ï¿½");		                
	                myFactory.abortTransaction(con);
	                existenErrores = true;
	                return false;  
		        }
		        
		        boolean esInsertar = cuentasCobroDao.insertarAnulacionCXC(con,numCxc,usuario,fecha,hora,observacion,tipo,institucion);
		        if(!esInsertar)
		        {
		            logger.warn("[anularCXC]No se inserto anulaciï¿½n de la CXC :ï¿½");		                
	                myFactory.abortTransaction(con);
	                existenErrores = true;
	                return false;   
		        }
			}
			if(!existenErrores)
			    myFactory.endTransaction(con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return true;
    }
	
	//********************************** FIN METODOS MOVIMIENTOS CXC **********************************//
	
	/**
	 * Metodo que cargar una cuenta de cobro incluyendo
	 * las facturas asignadas a ellas, y los movimientos
	 * que ha sifrido. 
	 * @param con, Conexion
	 * @param cuentaCobro, Codigo de la Cuenta de cobro.
	 * @param institucion
	 * @return Boolean, false sin no se pudo cargar.
	 */
	public boolean cargarCuentaCobroCompleta(Connection con, double cuentaCobro, int institucion)
	{
		reset();
		boolean resultado=false;
		//Se bajan los PreparedStatement al Mundo porque se detecto que con el método del DAO que retorna
		//el ResultSetDecorator cuando la cantidad de información es muy grande y la consulta se demora mucho tiempo
		//se genera una SQLException.
		//No se pasa esta lógica al DAO (Deber Ser) dado que el método tiene ya lógica pegada al Mundo
		//y el impacto es muy alto    17/11/2011 Ricardo Ruiz C.
		ResultSet rs=null;
		ResultSet rsFacturas=null;
		PreparedStatement pst=null;
		PreparedStatement pstFacturas=null;
		try
		{
			pst= con.prepareStatement(cargarCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			rs=pst.executeQuery();
//			rs=cuentasCobroDao.cargarCuentaCobro(con,cuentaCobro,institucion);
			if(rs.next())
			{
				this.numeroCuentaCobro=cuentaCobro;
				this.convenio=new InfoDatosInt(rs.getInt("codigoconvenio"), rs.getString("nombreconvenio"));
				this.acronimoIdResponsable=rs.getString("tipo_id_responsable");
				this.nomIdResponsable=rs.getString("nom_id_responsable");
				this.acronimoIdResponsable=rs.getString("numero_id_responsable");
				this.estado=rs.getInt("estado");
				this.fechaElaboracion=rs.getString("fecha_elaboracion");
				this.horaElaboracion=rs.getString("hora_elaboracion");
				this.usuarioGenera=rs.getString("usuario_genera");
				if(estado==ConstantesBD.codigoEstadoCarteraRadicada)
				{
					this.fechaRadicacion=rs.getString("fecha_radicacion");
					this.numeroRadicacion=rs.getString("numero_radicacion");
					this.usuarioRadica=rs.getString("usuario_radica");
					this.observacionesRad=rs.getString("obs_radicacion");
				}
				this.valInicialCuenta=rs.getDouble("valor_inicial_cuenta");
				this.saldoCuenta=rs.getDouble("saldo_cuenta");
				this.fechaInicial=rs.getString("fecha_inicial");
				this.fechaFinal=rs.getString("fecha_final");
				this.observacionesGen=rs.getString("obs_generacion");
				this.codigoCentroAtencion=rs.getInt("codigocentroatencion");
				this.nombreCentroAtencion=rs.getString("nombrecentroatencion");
				this.direccionConvenio = rs.getString("direccion");
				this.telefonoConvenio = rs.getString("telefono");
				cargarViasIngreso(con,cuentaCobro,institucion);
				if(estado==ConstantesBD.codigoEstadoCarteraAnulado)
				{
					movimiento.cargarMovimientoCxC(con,cuentaCobro,institucion);
				}
				pstFacturas= con.prepareStatement(facturasCuentasCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pstFacturas.setDouble(1,cuentaCobro);
				pstFacturas.setInt(2,institucion);
				rsFacturas=pstFacturas.executeQuery();
//				rsFacturas=cuentasCobroDao.facturasCuentaCobro(con,cuentaCobro,institucion);
				while(rsFacturas.next())
				{
					DtoFactura fact=new DtoFactura();
					fact.setCodigo(rsFacturas.getInt("codigofactura"));
					fact.setConsecutivoFactura(rsFacturas.getDouble("consecutivo"));
					fact.setFecha(rsFacturas.getString("fecha"));
					fact.setValorCartera(rsFacturas.getDouble("valorcartera"));
					fact.setValorTotal(rsFacturas.getDouble("valortotal"));
					fact.getEstadoPaciente().setCodigo(rsFacturas.getInt("codigo_estado_paciente"));
					fact.getTipoMonto().setCodigo(rsFacturas.getInt("codigo_tipo_monto"));
					fact.setValorBrutoPac(rsFacturas.getDouble("valor_bruto_pac"));
					if(rsFacturas.getBoolean("facturasistema"))
					{
						fact.setCentroAtencion(new InfoDatosInt(rsFacturas.getInt("codigocentroatencion"),rsFacturas.getString("centroatencion")));
						fact.setViaIngreso(new InfoDatosInt(rsFacturas.getInt("viaingreso"),rsFacturas.getString("nombreviaingreso")));
						fact.setIdPaciente(rsFacturas.getString("identificacion_paciente"));
						fact.setNombrePaciente(rsFacturas.getString("nombrepersona"));
					}
					facturas.add(fact);
					this.numeroFacturas++;
				}
				resultado=true;
			}
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR Iterando Facturas No. "+this.numeroFacturas,sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR Iterando Facturas No. "+this.numeroFacturas, e);
		}
		finally{
			try{
				rsFacturas.close();
				pstFacturas.close();
				rs.close();
				pst.close();
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return resultado;
	}
	
    /**
     * Metodo para cargar las vias de ingreso relacionadas con una cuenta de cobro.
	 * @param con
	 * @param cuentaCobro
     * @param institucion
	 */
	private void cargarViasIngreso(Connection con, double cuentaCobro, int institucion) 
	{
		try
		{
			ResultSetDecorator rs1=cuentasCobroDao.cargarViasIngreso(con,cuentaCobro,institucion);
			while(rs1.next())
			{
				viaIngreso=new InfoDatosInt(rs1.getInt("codigoviaingreso"),rs1.getString("nombreviaingreso"));
				viasIngreso.add((Object)viaIngreso);
			}
		}catch(SQLException e)
		{
			logger.error("Se presento un error cargando las vias de ingreso"+e);
		}
	}
	
	
	/**
	 * @param con
	 * @param usuario
	 * @param institucion
	 * @return
	 */
	public boolean insertarCuentaCobroPorRangos(Connection con, String usuario, int institucion,boolean saldos) 
	{
		boolean noHayErrores=true;
		noHayErrores=cuentasCobroDao.insertarEncabezadoCuentaCobro(con,this.numeroCuentaCobro,this.convenio.getCodigo(),ConstantesBD.codigoEstadoCarteraGenerado,this.fechaElaboracion,this.horaElaboracion,usuario,this.valInicialCuenta,this.saldoCuenta,this.fechaInicial,this.fechaFinal,this.observacionesGen,institucion,this.codigoCentroAtencion);
		//insertar vias de ingreso
		if(noHayErrores)
		{
			if(viasIngreso.size()>0)
			{
				for(int i=0;i<viasIngreso.size();i++)
				{
					noHayErrores=cuentasCobroDao.insertarViasIngresCxC(con,numeroCuentaCobro,Integer.parseInt(viasIngreso.get(i)+""),institucion);
				}
			}
		}
		//actualizar facturas
		if(noHayErrores)
		{
			int numeroFacturas=Integer.parseInt(facturasCxC.get("cantidadFacturas")+"");
			for(int i=0;i<numeroFacturas;i++)
			{
				noHayErrores=cuentasCobroDao.actulizarNumeroCxCFactura(con,numeroCuentaCobro,Integer.parseInt(facturasCxC.get("codigo_"+i)+""),institucion);
				//si es de la funcionalidad de saldos, se debe actualizar el valor cartera de las facturas.
				if(saldos&&noHayErrores)
				{
					noHayErrores=cuentasCobroDao.actualizaValorCartera(con,Double.parseDouble(facturasCxC.get("valor_cartera_"+i)+""),Integer.parseInt(facturasCxC.get("codigo_"+i)+""),institucion);
				}

			}
		}
		return noHayErrores;
	}
	
	
	/**
	 * Metodo transaccional para ingresa una cuenta de cobro por facturas.
	 * @param con
	 * @param loginUsuario
	 * @param institucion
	 */
	public boolean insertarCuentaCobroPorFactura(Connection con, String loginUsuario, int institucion) 
	{
		boolean enTransaccion=false;
		enTransaccion=cuentasCobroDao.insertarEncabezadoCuentaCobro(con,this.numeroCuentaCobro,this.convenio.getCodigo(),ConstantesBD.codigoEstadoCarteraGenerado,this.fechaElaboracion,this.horaElaboracion,loginUsuario,this.valInicialCuenta,this.saldoCuenta,this.fechaInicial,this.fechaFinal,this.observacionesGen,institucion,this.codigoCentroAtencion);
		if(enTransaccion)
			enTransaccion=cuentasCobroDao.insertarViasIngresCxC(con,numeroCuentaCobro,viaIngreso.getCodigo(),institucion);
		if(enTransaccion)
			enTransaccion=cuentasCobroDao.actulizarNumeroCxCFactura(con,numeroCuentaCobro,this.codigoFactura,institucion);
		return enTransaccion;
	}

	
	/**
	 * Metodo las facturas con de una via de ingreso,
	 * si la la cuenta de cobro es -1 solo busca las facturas
	 * que no tienen relacionada cuenta de cobro, sino busca las factura que
	 * cumplen los parametros y que pertenecen a la cuenta de cobro cargada.
	 * @param con
	 * @param codigoViaIngresoUrgencias
	 * @param numeroFacturasCxC
	 * @param institucion
	 * @return
	 */
	public int consultarFacturasViaIngreso(Connection con, String viasIngresoSeleccionadas, int institucion, int numeroFacturasCxC) 
	{
		ResultSetDecorator rs=cuentasCobroDao.consultarFacturasCxC(con,convenio.getCodigo(),fechaInicial,fechaFinal,viasIngresoSeleccionadas,numeroCuentaCobro,institucion,this.codigoCentroAtencion);
		double valorTotal=0;
		double valorTotalCartera=0;
		double valorTotalAjustes=0;
		int facRechazadas=Integer.parseInt(facturasCxC.get("numFacRechazadas")+"");
		try
		{
		   while(rs.next())
			{
				boolean tieneAjustesPendientes=false;
				boolean tieneSaldoPendiente=false;
				boolean tieneAunditoriaGlosa=false;
				tieneAjustesPendientes=ValidacionesFactura.facturaTieneAjustesPendientes(con,rs.getInt("codigo"));
				tieneSaldoPendiente=ValidacionesFactura.facturaTieneSaldoPendiente(con,rs.getInt("codigo"));
				tieneAunditoriaGlosa=consultarFacturaEnProcesoAudi(rs.getInt("codigo"))>0;
				logger.info("----");
				if(!tieneAjustesPendientes&&tieneSaldoPendiente&&!tieneAunditoriaGlosa)
				{
					logger.info("***");
					facturasCxC.put("codigo_"+numeroFacturasCxC,rs.getString("codigo"));
					facturasCxC.put("consecutivo_factura_"+numeroFacturasCxC,Utilidades.convertirAEntero(rs.getString("consecutivo_factura")));
					facturasCxC.put("fecha_"+numeroFacturasCxC,rs.getString("fecha"));
					facturasCxC.put("via_ingreso_"+numeroFacturasCxC,rs.getString("via_ingreso"));
					facturasCxC.put("nombre_viaingreso_"+numeroFacturasCxC,rs.getString("nombre_viaingreso"));
					facturasCxC.put("valor_"+numeroFacturasCxC,rs.getString("valor"));
					facturasCxC.put("valor_cartera_"+numeroFacturasCxC,rs.getString("valor_cartera"));
					facturasCxC.put("valor_convenio_"+numeroFacturasCxC,rs.getString("valor_convenio"));
					facturasCxC.put("valor_ajustes_"+numeroFacturasCxC,rs.getString("valor_ajustes"));
					facturasCxC.put("seleccionado_"+numeroFacturasCxC,"true");
					facturasCxC.put("codigocentroatencion_"+numeroFacturasCxC,rs.getString("codigocentroatencion"));
					facturasCxC.put("nombrecentroatencion_"+numeroFacturasCxC,rs.getString("nombrecentroatencion"));
					if(facturasCxC.get("valor_total")!=null)
						valorTotal=Double.parseDouble(facturasCxC.get("valor_total")+"");
					else
						valorTotal=0;
					if(facturasCxC.get("valor_total_cartera")!=null)
						valorTotalCartera=Double.parseDouble(facturasCxC.get("valor_total_cartera")+"");
					else
						valorTotalCartera=0;
					if(facturasCxC.get("valor_total_ajustes")!=null)
						valorTotalAjustes=Double.parseDouble(facturasCxC.get("valor_total_ajustes")+"");
					else
						valorTotalAjustes=0;					
					valorTotal=valorTotal+rs.getDouble("valor");
					valorTotalCartera=valorTotalCartera+rs.getDouble("valor_cartera");
					valorTotalAjustes=valorTotalAjustes+rs.getDouble("valor_ajustes");
					facturasCxC.put("valor_total",valorTotal+"");
					facturasCxC.put("valor_total_cartera",valorTotalCartera+"");
					facturasCxC.put("valor_total_ajustes",valorTotalAjustes+"");
					facturasCxC.put("tipo_"+numeroFacturasCxC,"BD");		
					facturasCxC.put("mostrar_"+numeroFacturasCxC, true);
					numeroFacturasCxC++;
				}
				else if(tieneAjustesPendientes)
				{
				facturasCxC.put("codigoRechazada_"+facRechazadas,rs.getString("consecutivo_factura"));
				facturasCxC.put("razon_"+facRechazadas,"Tienen Ajustes Pendientes.");
				facRechazadas++;
				}
				else if(tieneAunditoriaGlosa)
				{
				facturasCxC.put("codigoRechazada_"+facRechazadas,rs.getString("consecutivo_factura"));
				facturasCxC.put("razon_"+facRechazadas,"Se Encuentra Asociada a una Pre Glosa Pendiente.");
				facRechazadas++;
				}
				else if(!tieneSaldoPendiente)
				{
					facturasCxC.put("codigoRechazada_"+facRechazadas,rs.getString("consecutivo_factura"));
					facturasCxC.put("razon_"+facRechazadas,"El saldo pendiente es igual a 0");
					facRechazadas++;
				}
			}
			
		}
		catch(SQLException e)
		{
			logger.error("ERRROR EN LA CONSULTA DE FACTURAS"+e);
		}
		facturasCxC.put("numFacRechazadas",facRechazadas+"");
		facturasCxC.put("numFacturas",numeroFacturasCxC+"");
		facturasCxC.put("numCXC",this.numeroCuentaCobro+"");
		return numeroFacturasCxC;
	}
	
	
	
	/**
	 * Mï¿½todo para cargar los datos de la impresion
	 * resumida
	 * @param con
	 * @param numCuentaCobro
	 * @return
	 * @throws SQLException
	 */
	public Collection cargarResumenImpresion(Connection con, double numCuentaCobro) throws SQLException
	{
		return cuentasCobroDao.cargarDatosImpresionResumida(con, numCuentaCobro);
	}
	
	
	/**
	 * Mï¿½todo ara cargar un convenio
	 * @param con
	 * @param numCuentaCobro
	 * @return
	 * @throws SQLException
	 */
	public int convenioxCuentaCobro(Connection con, double numCuentaCobro)throws SQLException
	{
		ResultSetDecorator rs=cuentasCobroDao.convenioxCuentaCobro(con, numCuentaCobro);
	    try 
	    {
            if(rs.next())
            {
                this.nitConvenio=rs.getString("nit");
                return 1;
            }
            else
            {
            	return -1;
            }
		
	    }
	    catch(SQLException e)
		{
			logger.error("ERROR EN LA CONSULTA DEL NIT"+e);
			return -1;
		}
	}
	
	
	/**
	 * Mï¿½todo que consulta todas las facturas de una cuenta de cobro
	 * segun la fecha de elaboracion
	 * @param con
	 * @param numeroCuentaCobro
	 * @param fecha
	 * @return
	 */
	public boolean cargarFacturasPorFecha(Connection con, double numeroCuentaCobro, String fecha)
	{
		ResultSetDecorator rs=cuentasCobroDao.cargarFacturasPorFecha(con,numeroCuentaCobro, fecha);
		this.facturas= new ArrayList();
		try
		{
			while(rs.next())
			{
				DtoFactura fact=Factura.cargarFactura(con, rs.getInt("codigofactura")+"", false);
				facturas.add(fact);
			}
			if(facturas.size()>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			logger.error("ERRROR EN LA CONSULTA DE FACTURAS POR FECHA"+e);
			return false;
		}
	}
	
	
	/**
	 * Mï¿½todo que consulta todas las facturas de una cuenta de cobro
	 * segun la via de Ingreso
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @return
	 */
	public boolean cargarFacturasPorViaIngreso(Connection con, double numeroCuentaCobro, String viaIngreso)
	{
		ResultSetDecorator rs=cuentasCobroDao.cargarFacturasPorViaIngreso(con,numeroCuentaCobro, viaIngreso);
		this.facturas= new ArrayList();
		try
		{
			while(rs.next())
			{
				DtoFactura fact=Factura.cargarFactura(con, rs.getInt("codigofactura")+"", false);
				facturas.add(fact);
			}
			if(facturas.size()>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			logger.error("ERRROR EN LA CONSULTA DE FACTURAS POR VIA DE INGRESO"+e);
			return false;
		}
	}
	
	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @return
	 */
	public int cargarFacturaPorConsecutivo(Connection con, double numeroCuentaCobro, int consecutivo_factura)
	{
		return cuentasCobroDao.cargarFacturaPorConsecutivo(con,numeroCuentaCobro, consecutivo_factura);
	}
	
	/**
	 * Metodo para verificar si una via de ingreso
	 * posee entrada en la tabla vias_ingreso_cxc
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param institucion int, codigo de la instituciï¿½n
	 * @param viaIngreso int, cï¿½digo de la via de ingreso
	 * @param numCxC double, nï¿½mero de la cuenta de cobro
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCuentasCobroDao#existeViaIngresoCxCEnBD(Connection,int,int,double)
	 * @return ResultSet
	 * @author jarloc
	 */
	public int existeViaIngresoCxCEnBD (Connection con,int institucion,int viaIngreso,double numCxc)
	{
	    return cuentasCobroDao.existeViaIngresoCxCEnBD(con,institucion,viaIngreso,numCxc);
	}
	
	/**
	 * Metodo implementado para verificar si una cuenta
	 * de cobro esta radicada
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param institucion int, codigo de la instituciï¿½n	
	 * @param numCXC double, nï¿½mero de la cuenta de cobro
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCuentasCobroDao#cuentaCobroPoseeRadicacion(Connection,int,double)
	 * @return boolean, true si es radicada
	 * @author jarloc
	 */
	public boolean  cuentaCobroPoseeRadicacion (Connection con,int institucion,double numCXC)
	{
	    return cuentasCobroDao.cuentaCobroPoseeRadicacion(con,institucion,numCXC);
	}
	
	/**
	 * Mï¿½todo para consultar los estados de la Glosa correspondiente a la Factura a Inactivar
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static HashMap estadoGlosasFacturas(Connection con,int consecutivoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentasCobroDao().estadoGlosasFacturas(con,consecutivoFactura);
	}
	
	/**
	 * Mï¿½todo para determinar si la factura posee una glosa en estado respondida y que no tenga ajustes asociados con el fin de no permitir inactivarla
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static int requiereGlosaInactivar(Connection con,int consecutivoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentasCobroDao().requiereGlosaInactivar(con,consecutivoFactura);
	}
	
	/**
     * @return Retorna facturas.
     */
    public Collection getFacturas() {
        return facturas;
    }
    
    /**
     * @param facturas Asigna facturas.
     */
    public void setFacturas(Collection facturas) {
        this.facturas = facturas;
    }
    
    public Iterator getFacturasIterador()
    {
    	return facturas.iterator();
    }
    
    
    /**
     * @return Retorna codigoConvenio.
     */
    public InfoDatosInt getConvenio() {
        return convenio;
    }
    /**
     * @param codigoConvenio Asigna codigoConvenio.
     */
    public void setConvenio(InfoDatosInt codigoConvenio) {
        this.convenio = codigoConvenio;
    }
    /**
     * @return Retorna codigoViaIngreso.
     */
    public InfoDatosInt getViaIngreso() {
        return viaIngreso;
    }
    /**
     * @param codigoViaIngreso Asigna codigoViaIngreso.
     */
    public void setViaIngreso(InfoDatosInt codigoViaIngreso) {
        this.viaIngreso = codigoViaIngreso;
    }
    /**
     * @return Retorna estado.
     */
    public int getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna fechaElaboracion.
     */
    public String getFechaElaboracion() {
        return fechaElaboracion;
    }
    /**
     * @param fechaElaboracion Asigna fechaElaboracion.
     */
    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }
    /**
     * @return Retorna fechaInicial.
     */
    public String getFechaInicial() {
        return this.fechaInicial;
    }
    /**
     * @param fechaInicial Asigna fechaInicial.
     */
    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
    /**
     * @return Retorna fechaRadicacion.
     */
    public String getFechaRadicacion() {
        return fechaRadicacion;
    }
    /**
     * @param fechaRadicacion Asigna fechaRadicacion.
     */
    public void setFechaRadicacion(String fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }
    /**
     * @return Retorna horaElaboracion.
     */
    public String getHoraElaboracion() {
        return horaElaboracion;
    }
    /**
     * @param horaElaboracion Asigna horaElaboracion.
     */
    public void setHoraElaboracion(String horaElaboracion) {
        this.horaElaboracion = horaElaboracion;
    }
    /**
     * @return Retorna horaInicial.
     */
    public String getFechaFinal() {
        return fechaFinal;
    }
    /**
     * @param horaInicial Asigna horaInicial.
     */
    public void setFechaFinal(String horaInicial) {
        this.fechaFinal = horaInicial;
    }
    /**
     * @return Retorna horaRadicacion.
     */
    public String getHoraRadicacion() {
        return horaRadicacion;
    }
    /**
     * @param horaRadicacion Asigna horaRadicacion.
     */
    public void setHoraRadicacion(String horaRadicacion) {
        this.horaRadicacion = horaRadicacion;
    }
    /**
     * @return Retorna observacionesGen.
     */
    public String getObservacionesGen() {
        return observacionesGen;
    }
    /**
     * @param observacionesGen Asigna observacionesGen.
     */
    public void setObservacionesGen(String observacionesGen) {
        this.observacionesGen = observacionesGen;
    }
    /**
     * @return Retorna observacionesRad.
     */
    public String getObservacionesRad() {
        return observacionesRad;
    }
    /**
     * @param observacionesRad Asigna observacionesRad.
     */
    public void setObservacionesRad(String observacionesRad) {
        this.observacionesRad = observacionesRad;
    }
    /**
     * @return Retorna saldoCuenta.
     */
    public double getSaldoCuenta() {
        return saldoCuenta;
    }
    /**
     * @param saldoCuenta Asigna saldoCuenta.
     */
    public void setSaldoCuenta(double saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }
    /**
     * @return Retorna usuarioGenera.
     */
    public String getUsuarioGenera() {
        return usuarioGenera;
    }
    /**
     * @param usuarioGenera Asigna usuarioGenera.
     */
    public void setUsuarioGenera(String usuarioGenera) {
        this.usuarioGenera = usuarioGenera;
    }
    /**
     * @return Retorna usuarioRadica.
     */
    public String getUsuarioRadica() {
        return usuarioRadica;
    }
    /**
     * @param usuarioRadica Asigna usuarioRadica.
     */
    public void setUsuarioRadica(String usuarioRadica) {
        this.usuarioRadica = usuarioRadica;
    }
    /**
     * @return Retorna valInicialCuenta.
     */
    public double getValInicialCuenta() {
        return valInicialCuenta;
    }
    /**
     * @param valInicialCuenta Asigna valInicialCuenta.
     */
    public void setValInicialCuenta(double valInicialCuenta) {
        this.valInicialCuenta = valInicialCuenta;
    }

	/**
	 * @return Returns the viasIngreso.
	 */
	public ArrayList getViasIngreso() {
		return viasIngreso;
	}
	/**
	 * @param viasIngreso The viasIngreso to set.
	 */
	public void setViasIngreso(ArrayList viasIngreso) {
		this.viasIngreso = viasIngreso;
	}
	/**
	 * @return Returns the numeroCuentaCobro.
	 */
	public double getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}
	/**
	 * @param numeroCuentaCobro The numeroCuentaCobro to set.
	 */
	public void setNumeroCuentaCobro(double numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}
	/**
	 * @return Returns the movimiento.
	 */
	public MovimientosCuentaCobro getMovimiento() {
		return movimiento;
	}
	/**
	 * @param movimiento The movimiento to set.
	 */
	public void setMovimiento(MovimientosCuentaCobro movimiento) {
		this.movimiento = movimiento;
	}
	/**
	 * @return Returns the numeroRadicacion.
	 */
	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}
	/**
	 * @param numeroRadicacion The numeroRadicacion to set.
	 */
	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}
	/**
	 * @return Returns the acronimoIdResponsable.
	 */
	public String getAcronimoIdResponsable() {
		return acronimoIdResponsable;
	}
	/**
	 * @param acronimoIdResponsable The acronimoIdResponsable to set.
	 */
	public void setAcronimoIdResponsable(String acronimoIdResponsable) {
		this.acronimoIdResponsable = acronimoIdResponsable;
	}
	/**
	 * @return Returns the nomIdResponsable.
	 */
	public String getNomIdResponsable() {
		return nomIdResponsable;
	}
	/**
	 * @param nomIdResponsable The nomIdResponsable to set.
	 */
	public void setNomIdResponsable(String nomIdResponsable) {
		this.nomIdResponsable = nomIdResponsable;
	}
	/**
	 * @return Returns the numIdResponsable.
	 */
	public String getNumIdResponsable() {
		return numIdResponsable;
	}
	/**
	 * @param numIdResponsable The numIdResponsable to set.
	 */
	public void setNumIdResponsable(String numIdResponsable) {
		this.numIdResponsable = numIdResponsable;
	}	
	/**
	 * @return Returns the facturasCxC.
	 */
	public HashMap getFacturasCxC() {
		return facturasCxC;
	}
	/**
	 * @param facturasCxC The facturasCxC to set.
	 */
	public void setFacturasCxC(HashMap facturasCxC) {
		this.facturasCxC = facturasCxC;
	}
		/**
     * @return Retorna mapMovimientos.
     */
    public HashMap getMapMovimientos() {
        return mapMovimientos;
    }
    /**
     * @param mapMovimientos Asigna mapMovimientos.
     */
    public void setMapMovimientos(HashMap mapMovimientos) {
        this.mapMovimientos = mapMovimientos;
    }
    
   /**
    * Retorna un objeto
    * @param key, llave del dato
    * @return Object
    */
    public Object getMapMovimientos(String key) {
        return mapMovimientos.get(key);
    }
    /**
     * Asigna un dato por medio de la llave
     * @param key, llave del dato
     * @param value, dato
     */
    public void setMapMovimientos(String key, Object value) {
        this.mapMovimientos.put(key,value);
    }
	

	/**
	 * @return Returns the facturasCxC.
	 */
	public Object getFacturaCxC(String key) {
		return facturasCxC.get(key);
	}
	/**
	 * @param facturasCxC The facturasCxC to set.
	 */
	public void setFacturaCxC(String key,Object value) {
		this.facturasCxC.put(key,value);
	}
	
	/**
	 * @param con
	 */
	public void siguienteCuentaCobro(Connection con) 
	{
		cuentasCobroDao.siguienteCuentaCobro(con);
	}



	/**
	 * @return Returns the numeroFacturas.
	 */
	public int getNumeroFacturas() {
		return numeroFacturas;
	}
	/**
	 * @param numeroFacturas The numeroFacturas to set.
	 */
	public void setNumeroFacturas(int numeroFacturas) {
		this.numeroFacturas = numeroFacturas;
	}

	/**
	 * Metodo para cargar los datos minimos de una factura
	 * para la generacion de una cuenta de cobro por factura.
	 * dando el consecutivo de la factura.
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 */
	public void cargarFacutraParaCxC(Connection con, int codigoFactura) 
	{
		ResultSetDecorator rs= cuentasCobroDao.cargarFacutraParaCxC(con,codigoFactura);
		try
		{
			if(rs.next())
			{
				this.codigoFactura=codigoFactura;
				this.consecutivoFac=rs.getInt("consecutivo")+"";
				this.fechaElaboracionFactura=rs.getString("fecha");
				this.valInicialCuenta=rs.getDouble("valor");
				this.saldoCuenta=rs.getDouble("valor");
				this.convenio=new InfoDatosInt(rs.getInt("codigoconvenio"),rs.getString("nombreConvenio"));
				this.viaIngreso=new InfoDatosInt(rs.getInt("codigoViaIngres"),rs.getString("nombreViaIngreso"));
				this.codigoCentroAtencion=rs.getInt("codigocentroatencion");
				this.nombreCentroAtencion=rs.getString("nombrecentroatencion");
			}
			else
			{
				this.codigoFactura=ConstantesBD.codigoNuncaValido;
			}
		}
		catch(Exception e)
		{
			logger.error("se produjo error realizando la consulta"+e);
		}
	}
	
	/**
	 * Mï¿½todo para cargar el motivo de anulacion de una factura
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	public int cargarMotivoAnulacion(Connection con, double numeroCuentaCobro){
		try{
			Collection datos=cuentasCobroDao.movimientosCxC(con, numeroCuentaCobro);
			Iterator iterador=datos.iterator();
			if(iterador.hasNext()){
				HashMap dynaDatosAnulacion=(HashMap)iterador.next();
				this.setFechaAnulacion(dynaDatosAnulacion.get("fecha")+"");
				this.setHoraAnulacion(dynaDatosAnulacion.get("hora")+"");
				this.setUsuarioAnulacion(dynaDatosAnulacion.get("usuario")+"");
				this.setMotivoAnulacion(dynaDatosAnulacion.get("motivo")+"");
				this.setTipoAnulacion(dynaDatosAnulacion.get("tipomovimiento")+"");
				
				return 1;
			}
			else
			{
				return 0;
			}
		}
		catch(Exception e){
			logger.error("Error cargando el motivo de anulaciï¿½n de una Cuenta de Cobro "+e);
			return 0;
		}
	}
	
	/**
	 * @return  Returns the fechaAnulacion.
	 */
	public String getFechaAnulacion()
	{
		return fechaAnulacion;
	}
	/**
	 * @param fechaAnulacion The fechaAnulacion to set .
	 */
	public void setFechaAnulacion(String fechaAnulacion)
	{
		this.fechaAnulacion= fechaAnulacion;
	}
	/**
	 * @return  Returns the horaAnulacion.
	 */
	public String getHoraAnulacion()
	{
		return horaAnulacion;
	}
	/**
	 * @param horaAnulacion The horaAnulacion to set .
	 */
	public void setHoraAnulacion(String horaAnulacion)
	{
		this.horaAnulacion= horaAnulacion;
	}
	/**
	 * @return  Returns the motivoAnulacion.
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}
	/**
	 * @param motivoAnulacion The motivoAnulacion to set .
	 */
	public void setMotivoAnulacion(String motivoAnulacion)
	{
		this.motivoAnulacion= motivoAnulacion;
	}
	/**
	 * @return  Returns the tipoAnulacion.
	 */
	public String getTipoAnulacion()
	{
		return tipoAnulacion;
	}
	/**
	 * @param tipoAnulacion The tipoAnulacion to set .
	 */
	public void setTipoAnulacion(String tipoAnulacion)
	{
		this.tipoAnulacion= tipoAnulacion;
	}
	/**
	 * @return  Returns the usuarioAnulacion.
	 */
	public String getUsuarioAnulacion()
	{
		return usuarioAnulacion;
	}
	/**
	 * @param usuarioAnulacion The usuarioAnulacion to set.
	 */
	public void setUsuarioAnulacion(String usuarioAnulacion)
	{
		this.usuarioAnulacion= usuarioAnulacion;
	}
	/**
	 * @return Returns the consecutivoFac.
	 */
	public String getConsecutivoFac() {
		return consecutivoFac;
	}
	/**
	 * @param consecutivoFac The consecutivoFac to set.
	 */
	public void setConsecutivoFac(String consecutivoFac) {
		this.consecutivoFac = consecutivoFac;
	}
	/**
	 * @return Returns the fechaElaboracionFactura.
	 */
	public String getFechaElaboracionFactura() {
		return fechaElaboracionFactura;
	}
	/**
	 * @param fechaElaboracionFactura The fechaElaboracionFactura to set.
	 */
	public void setFechaElaboracionFactura(String fechaElaboracionFactura) {
		this.fechaElaboracionFactura = fechaElaboracionFactura;
	}
	/**
	 * @return Returns the viaIngresoFactura.
	 */
	public String getViaIngresoFactura() {
		return viaIngresoFactura;
	}
	/**
	 * @param viaIngresoFactura The viaIngresoFactura to set.
	 */
	public void setViaIngresoFactura(String viaIngresoFactura) {
		this.viaIngresoFactura = viaIngresoFactura;
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
	 * @return Returns the nitConvenio.
	 */
	public String getNitConvenio()
	{
		return nitConvenio;
	}
	/**
	 * @param nitConvenio The nitConvenio to set.
	 */
	public void setNitConvenio(String nitConvenio)
	{
		this.nitConvenio= nitConvenio;
	}


	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the direccionConvenio
	 */
	public String getDireccionConvenio() {
		return direccionConvenio;
	}

	/**
	 * @param direccionConvenio the direccionConvenio to set
	 */
	public void setDireccionConvenio(String direccionConvenio) {
		this.direccionConvenio = direccionConvenio;
	}

	/**
	 * @return the telefonoConvenio
	 */
	public String getTelefonoConvenio() {
		return telefonoConvenio;
	}

	/**
	 * @param telefonoConvenio the telefonoConvenio to set
	 */
	public void setTelefonoConvenio(String telefonoConvenio) {
		this.telefonoConvenio = telefonoConvenio;
	}

	public boolean aprobarCuentaCobro(Connection con,int cuenta, String usuario, int institucion) {
		return cuentasCobroDao.aprobarCuentasCobro(con,cuenta, usuario, institucion);
		
	}

	public HashMap consultaFacturasCxc(Connection con, double numCuentaCobro,int institucion) {
		return cuentasCobroDao.consultaFacturasCxc(con, numCuentaCobro, institucion);
	}

	public boolean guardarDetMovimientosCxc(Connection con, int factura,double numCuentaCobro, int institucion) {
		return cuentasCobroDao.guardarDetMovimientosCxc(con,factura,numCuentaCobro,institucion);
	}

	public int consultarFacturaEnProcesoAudi(int factura) {
		return cuentasCobroDao.consultarFacturaEnProcesoAudi(factura);
	}

	/**
	 * 
	 * @param dtoFiltro
	 * @return
	 */
	public static ArrayList<DtoResultadoReporteCuentaCobro> generarReporteCuentaCobro(
			DtoFiltroReporteCuentasCobro dtoFiltro) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentasCobroDao().generarReporteCuentaCobro(dtoFiltro);
	}
}
