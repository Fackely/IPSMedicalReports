
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.AprobacionAjustesEmpresaDao;
import com.princetonsa.dao.DaoFactory;



/**
 * Clase para realizar la aprobacion de 
 * ajustes previamente ingresados.
 * Se permite la aprobacion de ajustes que solo
 * tengan estado Generado.
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class AprobacionAjustesEmpresa 
{
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(AprobacionAjustesEmpresa.class);
	
	/**
	 * DAO de este objeto, para trabajar aprobacion ajustes Empresa en la fuente de datos
	 */    
    private static AprobacionAjustesEmpresaDao aprobacionDao;
    
    /**
     * datos generales de aprobación de ajustes
     */
    private HashMap mapaAprobacion;       
    /**
     * datos generales de aprobación de ajustes
     */
    private HashMap mapaConsulta;
    /**
     * código del tipo de ajuste
     */
    private int codTipoAjuste;
    /**
     * número del ajuste
     */
    private double numAjuste;
    /**
     * número de la cuenta de cobro
     */
    private double numCuentaCobro;
    /**
     * número de la factura
     */
    private double numFactura;
    /**
     * código del convenio
     */
    private int codConvenio;
    /**
     * fecha del ajuste
     */
    private String fechaAjuste;    
    /**
     * número de registros del mapa
     * de aprobación
     */
    private int numRegMapAprobacion;
    /**
     * institucion del usuario
     */
    private int institucion;
    /**
     * registro seleccionado
     */
    private int regSeleccionado;
    /**
     *login del usuario 
     */
    private String usuario;
    /**
     * fecha de aprobación
     */
    private String fechaAprobacion;
        
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
     this.mapaAprobacion=new HashMap ();
     this.mapaConsulta=new HashMap ();
     this.codTipoAjuste=ConstantesBD.codigoNuncaValido;
     this.numAjuste=ConstantesBD.codigoNuncaValido;
     this.numCuentaCobro=ConstantesBD.codigoNuncaValido;
     this.numFactura=ConstantesBD.codigoNuncaValido;
     this.codConvenio=ConstantesBD.codigoNuncaValido;
     this.fechaAjuste="";
     this.numRegMapAprobacion=0;
     this.institucion=ConstantesBD.codigoNuncaValido;
     this.regSeleccionado=ConstantesBD.codigoNuncaValido;    
     this.fechaAprobacion="";
     this.usuario="";
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
			aprobacionDao= myFactory.getAprobacionAjustesEmpresaDao();
			
			if( aprobacionDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Constructor
	 *
	 */
	public AprobacionAjustesEmpresa ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * Metodo en el cual se realiza la generación
	 * de la busqueda Avanzada.	 
	 * @param con Connection
	 */
	public void realizarConsulta1(Connection con,boolean incluirInfoFac)
	{	    
	    int pos=this.numRegMapAprobacion;
	    boolean existeRegistro=false;
	    ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);
	    
	    logger.info("---->"+this.mapaConsulta);
	    try 
	    {
            while(rs.next())
            {
              this.mapaAprobacion.put("codigo_ajuste_"+pos,rs.getInt("codigo_ajuste")+"");
              this.mapaAprobacion.put("consecutivo_ajuste_"+pos,rs.getString("consecutivo_ajuste")+"");
              this.mapaAprobacion.put("castigo_cartera_"+pos,rs.getBoolean("castigo_cartera")+"");              
              this.mapaAprobacion.put("tipo_ajuste_"+pos,rs.getInt("tipo_ajuste")+"");
              if(incluirInfoFac)
              {
	              this.mapaAprobacion.put("factura_"+pos,rs.getString("factura"));
	              this.mapaAprobacion.put("consecutivo_factura_"+pos,rs.getString("consecutivo_factura"));
              }
              this.mapaAprobacion.put("nombre_tipo_ajuste_largo_"+pos,rs.getString("nombre_tipo_ajuste")+"");
              this.mapaAprobacion.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");
              this.mapaAprobacion.put("fecha_ajuste_"+pos,UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_ajuste")+""));
              this.mapaAprobacion.put("cuenta_cobro_"+pos,rs.getInt("cuenta_cobro")+"");
              this.mapaAprobacion.put("concepto_ajuste_"+pos,rs.getString("concepto_ajuste")+"");
              this.mapaAprobacion.put("nombre_concepto_ajuste_"+pos,rs.getString("nombre_concepto_ajuste")+"");
              this.mapaAprobacion.put("metodo_ajuste_"+pos,rs.getString("metodo_ajuste")+"");
              this.mapaAprobacion.put("nombre_metodo_ajuste_"+pos,rs.getString("nombre_metodo_ajuste")+"");              
              this.mapaAprobacion.put("observaciones_"+pos,rs.getString("observaciones")+"");             
              this.mapaAprobacion.put("fecha_elaboracion_"+pos,UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_elaboracion"))+"");
              
              if(rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteCreditoCuentaCobro || rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteCreditoFactura)              
                  this.mapaAprobacion.put("nombre_tipo_ajuste_corto_"+pos,ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()+"");
              if(rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteDebitoCuentaCobro || rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteDebitoFactura)
                  this.mapaAprobacion.put("nombre_tipo_ajuste_corto_"+pos,ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()+"");
              
              if(rs.getDouble("cuenta_cobro")!=ConstantesBD.codigoNuncaValidoDouble)
              {
                  realizarConsultaCxcOConvenio(con,rs.getDouble("cuenta_cobro"),pos,true);
                  this.mapaAprobacion.put("esPorCxC_"+pos,"true");
              }
              else
              {
                  realizarConsultaCxcOConvenio(con,rs.getInt("codigo_ajuste"),pos,false);
                  this.mapaAprobacion.put("esPorCxC_"+pos,"false");
              }
              
              this.mapaAprobacion.put("selected_"+pos,"false");
              existeRegistro=true;
              pos++;              
            }
            if(existeRegistro)
            {
	            this.numRegMapAprobacion=pos;
	            this.mapaAprobacion.put("numReg",numRegMapAprobacion+"");
            
	            if(pos==1)
	            {                
	                pos--;
	                this.mapaAprobacion.put("selected_"+pos,"true");
	                this.mapaAprobacion.put("mostrarPopOut","false");
	                this.regSeleccionado=pos;
	            } 
	            else
	                this.mapaAprobacion.put("mostrarPopOut","true");
            }
        } catch (SQLException e) 
        {           
            e.printStackTrace();
        }
       // Utilidades.imprimirMapa(this.mapaAprobacion);
	}
	
	/**
	 * metodo para consultar el convenio del ajuste, 
	 * ya sea por cuenta de cobro o por factura.
	 * Si la consulta es por factura se deben consultar
	 * el consecutivo de la factura, el valor del ajuste y
	 * el convenio ya que estos pertenecen a otra tabla, 
	 * puesto que enla consulta general se cargan estos valores 
	 * pero cuando es por CxC.
	 * @param con
	 * @param codigo double
	 */
	public void realizarConsultaCxcOConvenio(Connection con,double codigo,int pos,boolean esCxC)
	{
	    this.mapaConsulta.clear();
	    int numRegBusqueda=0;
	    InfoDatos campo;
	    if(esCxC)
	    {	        
		    String columnas="convenio as convenio," +
		    				"getnombreconvenio(convenio) as nombre_convenio";
		    this.mapaConsulta.put("camposTraer",columnas); 
		    this.mapaConsulta.put("tabla","cuentas_cobro");
		    campo=new InfoDatos("numero_cuenta_cobro",codigo,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
	        campo=new InfoDatos("institucion",this.institucion,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
	        this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");	        
	    }
	    else if(!esCxC)
	    {
	        int numRegInner=0;
	        String columnas="f.convenio as convenio," +
	        				"getDescempresainstitucion(f.empresa_institucion) as nombreempresa," +
	        				"getnombreconvenio(f.convenio) as nombre_convenio," + 
	        				"f.codigo as factura," +
	        				"f.consecutivo_factura as consecutivo_factura,"+
	        				"afe.valor_ajuste as valor_ajuste," +
	        				"getnombremedico(f.cod_paciente) as nombre_paciente," +
	        				"f.tipo_factura_sistema as tipo_factura_sistema";
	        this.mapaConsulta.put("camposTraer",columnas); 
		    this.mapaConsulta.put("tabla","facturas f");
		    campo=new InfoDatos("f.institucion",this.institucion,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
	        campo=new InfoDatos("afe.codigo",codigo,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
		    campo=new InfoDatos("ajus_fact_empresa afe","afe.factura=f.codigo");
	        this.mapaConsulta.put("inner_"+numRegInner,campo);  
	        numRegInner++;
	        this.mapaConsulta.put("numRegInner",numRegInner+"");
	        this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");	        
	    }	    
        ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);
        try 
	    {
            while(rs.next())
            {
                this.mapaAprobacion.put("nombre_convenio_"+pos,rs.getString("nombre_convenio")+""); 
                if(!esCxC)
                {
                    this.mapaAprobacion.put("factura_"+pos,rs.getInt("factura")+"");
                    this.mapaAprobacion.put("nombreempresa_"+pos,rs.getString("nombreempresa")+"");
                    this.mapaAprobacion.put("consecutivo_factura_"+pos,rs.getInt("consecutivo_factura")+"");
                    this.mapaAprobacion.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");//se sobrescribe el valor del ajuste cuando es por factura,
                    //puesto que este valor se trae por primera vez en la consulta general cuando es por CxC
                    if(rs.getBoolean("tipo_factura_sistema"))//si la factura no es del sistema, no se debe mostrar el nombre del paciente
                        this.mapaAprobacion.put("nombre_paciente_"+pos,rs.getString("nombre_paciente")+""); 
                    this.mapaAprobacion.put("tipo_factura_sistema_"+pos,rs.getBoolean("tipo_factura_sistema")+"");
                }
            }
	    } catch (SQLException e) 
        {           
            e.printStackTrace();
        }		    		 
	}
	
	/**
	 * metodo para realizar la busqueda de las
	 * facturas que pertenecen a un ajuste 
	 * determinado.
	 * @param con Connection
	 * @param codigoAjus double, código del ajuste
	 */
	public void realizarConsulta2(Connection con,double codigoAjus)
	{
	  this.construirConsulta2(codigoAjus); 
	  
	    HashMap mapFact = new HashMap();
	    int pos=0;
	    logger.info("--->"+mapaConsulta);
		  ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);	  
		  try 
		    {
	          while(rs.next())
	          {              
        		  mapFact.put("consecutivo_factura_"+pos,rs.getInt("consecutivo_factura")+"");
        		  mapFact.put("factura_"+pos,rs.getInt("factura")+"");
	              mapFact.put("codigocentroatencion_"+pos,rs.getString("codigocentroatencion")+"");
	              mapFact.put("nombrecentroatencion_"+pos,rs.getString("nombrecentroatencion")+"");
	              mapFact.put("concepto_ajuste_"+pos,rs.getString("concepto_ajuste")+"");
	              mapFact.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");
	              mapFact.put("tipo_factura_sistema_"+pos,rs.getBoolean("tipo_factura_sistema")+"");
	            pos++;
	          }
	          mapFact.put("numRegFact",pos+"");
	          logger.info("mapFact -->"+mapFact);
	          this.mapaAprobacion.put("facturas_"+this.regSeleccionado,mapFact);
	          logger.info("mapaAprobacion -->"+mapaAprobacion);
		    } catch (SQLException e) 
	        {           
	            e.printStackTrace();
	        }
		   
	}
	
	/**
	 * Método para verificar el valor del pool
	 * @param con
	 */
	public void verificarValorPoolAjustesFactura(Connection con)
	{
		logger.info("--->"+this.mapaAprobacion);
		
		//
		for(int i=0;i<Utilidades.convertirAEntero(this.mapaAprobacion.get("numReg")+"");i++)
		{
			this.mapaAprobacion.put("tiene_valor_pool_"+i, 
				aprobacionDao.tieneRegistroAjustePoolFactura(
					con, 
					this.mapaAprobacion.get("codigo_ajuste_"+i).toString(), 
					this.mapaAprobacion.get("factura_"+i).toString()
				)
			);
		}
	}
	
	/**
	 * metodo para consultar el detalle de las
	 * facturas, a las que se les realizo un
	 * ajuste
	 * @param codigoAjus
	 */
	private void construirConsulta2(double codigoAjus)
	{	    
	    int numRegBusqueda=0;
	    int numRegInner=0;
	    InfoDatos campo;
	    String columnas="afe.factura as factura," +
			    		"afe.metodo_ajuste as metodo_ajuste," +
			    		"getNombreConceptoAjuste(afe.concepto_ajuste) as concepto_ajuste," +
			    		"afe.valor_ajuste as valor_ajuste," +
			    		"f.consecutivo_factura as consecutivo_factura," +
			    		"f.tipo_factura_sistema as tipo_factura_sistema," +
			    		"getnombremedico(f.cod_paciente) as nombre_paciente," +
			    		"f.centro_aten as codigocentroatencion," +
			    		"getnomcentroatencion(f.centro_aten) as nombrecentroatencion";
	    this.mapaConsulta.put("camposTraer",columnas); 
	    this.mapaConsulta.put("tabla","ajus_fact_empresa afe");
	    campo=new InfoDatos("afe.codigo",codigoAjus,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;
        campo=new InfoDatos("afe.institucion",this.institucion,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;
        campo=new InfoDatos("facturas f","afe.factura=f.codigo");
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
        this.mapaConsulta.put("numRegInner",numRegInner+"");
        campo=new InfoDatos("f.consecutivo_factura","asc");
        this.mapaConsulta.put("order",campo);
	}
	
	/**
	 * metodo para consultar el detalle de las
	 * facturas por servicio.
	 * @param con
	 * @param numAjus
	 * @param codFact
	 * @return
	 */
	private HashMap consultarAjustesDetalleFactura(Connection con,double numAjus,int codFact)
	{
	    this.mapaConsulta.clear();
	    HashMap map = new HashMap ();	    
	    int numRegBusqueda=0;	    
	    InfoDatos campo;
	    String columnas="codigopk as codigopk," +
	    				"factura as factura," +
			    		"det_fact_solicitud as det_fact_solicitud," +
			    		"valor_ajuste as valor_ajuste," +
			    		"valor_ajuste_pool as valor_ajuste_pool," +
			    		"concepto_ajuste as concepto_ajuste," +
			    		"getNombreConceptoAjuste(concepto_ajuste) as nombre_concepto_ajuste";  
	    this.mapaConsulta.put("camposTraer",columnas); 
	    this.mapaConsulta.put("tabla","ajus_det_fact_empresa");
	    campo=new InfoDatos("codigo",numAjus,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;
        campo=new InfoDatos("factura",codFact,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;   
        campo=new InfoDatos("institucion",this.institucion,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;        
        this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
        int pos=0;     
        ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);	  
  	  	try 
  	  	{
            while(rs.next())
            {              
              map.put("codigopk_"+pos,rs.getInt("codigopk")+"");
              map.put("factura_"+pos,rs.getInt("factura")+"");
              map.put("det_fact_solicitud_"+pos,rs.getInt("det_fact_solicitud")+"");              
              map.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");
              map.put("valor_ajuste_pool_"+pos,rs.getDouble("valor_ajuste_pool")+"");              
              map.put("concepto_ajuste_"+pos,rs.getString("concepto_ajuste")+"");
              map.put("nombre_concepto_ajuste_"+pos,rs.getString("nombre_concepto_ajuste")+"");
              pos++;
            }
            map.put("numReg",pos+"");          
  	  	} catch (SQLException e) 
          {           
              e.printStackTrace();
          }
        return map;
	}
	
	/**
	 * Metodo implementado para construir la
	 * consulta, para buscar solo por el codigo,
	 * obteniendo toda la estructura del ajuste, 
	 * con sus respectivas facturas y el detalle
	 * de las mismas	
	 * @param con Connection 
	 */
	public void consultaAjustePorCodigo(Connection con)
	{
	    this.mapaConsulta.clear();
	    int numRegBusqueda=0;	    
	    InfoDatos campo;
	    String columnas="ae.codigo as codigo_ajuste," +
                        "ae.consecutivo_ajuste as consecutivo_ajuste," +
						"ae.castigo_cartera as castigo_cartera," +
						"ae.tipo_ajuste as tipo_ajuste," +
						"getNombreTipoAjuste(ae.tipo_ajuste) as nombre_tipo_ajuste,"+
						"ae.fecha_ajuste as fecha_ajuste," +
						"ae.cuenta_cobro as cuenta_cobro," +
						"ae.concepto_ajuste as concepto_ajuste," +
						"getNombreConceptoAjuste(ae.concepto_ajuste) as nombre_concepto_ajuste,"+
						"ae.metodo_ajuste as metodo_ajuste," +
						"getNombreMetodoAjuste(ae.metodo_ajuste) as nombre_metodo_ajuste,"+
						"ae.valor_ajuste as valor_ajuste," +
						"ae.observaciones as observaciones," +
						"ae.fecha_elaboracion as fecha_elaboracion" ;
	    this.mapaConsulta.put("camposTraer",columnas);          
	    this.mapaConsulta.put("tabla","ajustes_empresa ae");  
	    if( this.numAjuste!=ConstantesBD.codigoNuncaValidoDouble)
	    {
	    	logger.info("entre a aqui.");
	        campo=new InfoDatos("ae.codigo",this.numAjuste,"=");
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;	        
	    }
	    campo=new InfoDatos("ae.estado",ConstantesBD.codigoEstadoCarteraGenerado,"=");
	    this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	    numRegBusqueda++;	    
	    this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+""); 
	    this.realizarConsulta1(con,false);
	    this.realizarConsulta2(con,this.numAjuste);
	    logger.info("REALIZO CONSULTAS");
	    this.consultarDetalleFacturas(con);	    
        this.consultarDetalleFacturasAsocio(con);
        this.consultarDetallePaquetes(con);
        this.verificarValorPoolAjustesFactura(con);
	}
	
	/**
	 * Se validan los datos de la busqueda, para pasar
	 * al mapa los campos por los cules se filtrara la
	 * busqueda con sus respectivos valores.
	 * El mapa contendra objetos de tipo InfoDatos(id,value),
	 * siendo el id=campo de la tabla y el value=valor a buscar. 
	 *
	 */
	public void construirConsulta1()
	{
		logger.info("pasa por aca");
	    this.mapaConsulta.clear();
	    int numRegBusqueda=0;
	    int numRegInner=0;
	    String temp="";
	    InfoDatos campo;
	    String columnas="ae.codigo as codigo_ajuste," +
	    				"ae.consecutivo_ajuste as consecutivo_ajuste," +
						"ae.castigo_cartera as castigo_cartera," +
						"ae.tipo_ajuste as tipo_ajuste," +
						"afe.factura as factura," +
						"f.consecutivo_factura as consecutivo_factura," +
						"getNombreTipoAjuste(ae.tipo_ajuste) as nombre_tipo_ajuste,"+
						"to_char(ae.fecha_ajuste,'"+ConstantesBD.formatoFechaBD+"') as fecha_ajuste," +
						"ae.cuenta_cobro as cuenta_cobro," +
						"ae.concepto_ajuste as concepto_ajuste," +
						"getNombreConceptoAjuste(ae.concepto_ajuste) as nombre_concepto_ajuste,"+
						"ae.metodo_ajuste as metodo_ajuste," +
						"getNombreMetodoAjuste(ae.metodo_ajuste) as nombre_metodo_ajuste,"+
						"ae.valor_ajuste as valor_ajuste," +
						"ae.observaciones as observaciones," +
						"to_char(ae.fecha_elaboracion,'"+ConstantesBD.formatoFechaBD+"') as fecha_elaboracion ";
	    this.mapaConsulta.put("camposTraer",columnas);          
	    this.mapaConsulta.put("tabla","ajustes_empresa ae");
	    if(this.codTipoAjuste!=ConstantesBD.codigoNuncaValido && this.numAjuste!=ConstantesBD.codigoNuncaValidoDouble)
	    {
	        campo=new InfoDatos("ae.codigo",this.numAjuste,"=");
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
	        if(this.codTipoAjuste==ConstantesBD.codigoConceptosCarteraCredito)
	        {
	            temp=ConstantesBD.codigoAjusteCreditoCuentaCobro+","+ConstantesBD.codigoAjusteCreditoFactura;
	            campo=new InfoDatos("ae.tipo_ajuste",temp,"in");
	        }
	        else if(this.codTipoAjuste==ConstantesBD.codigoConceptosCarteraDebito)
	        {
	            temp=ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoAjusteDebitoFactura;
	            campo=new InfoDatos("ae.tipo_ajuste",temp,"in");
	        }
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);
	        numRegBusqueda++;
	    }
	    if(!this.fechaAjuste.equals(""))
	    {
	        campo=new InfoDatos("ae.fecha_ajuste",UtilidadFecha.conversionFormatoFechaABD(this.fechaAjuste),"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
	    }
	    if(this.numCuentaCobro!=ConstantesBD.codigoNuncaValidoDouble)
	    {
	        campo=new InfoDatos("ae.cuenta_cobro",this.numCuentaCobro,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++; 
	    }
	    
        campo=new InfoDatos("ajus_fact_empresa afe","ae.codigo=afe.codigo");
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        
	     campo=new InfoDatos("facturas f","afe.factura=f.codigo");
	     this.mapaConsulta.put("inner_"+numRegInner,campo);  
	     numRegInner++;
	    this.mapaConsulta.put("numRegInner",numRegInner+"");
	    
	    if(this.numFactura!=ConstantesBD.codigoNuncaValidoDouble)
	    {	        
	        campo=new InfoDatos("f.consecutivo_factura",numFactura,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;
	        campo=new InfoDatos("ae.cuenta_cobro","null","is");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;  
	    }
	    if(this.codConvenio!=ConstantesBD.codigoNuncaValido)
	    {   
	        campo=new InfoDatos("f.convenio",this.codConvenio,"=");  
	        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	        numRegBusqueda++;	        
	    }
	    campo=new InfoDatos("ae.estado",ConstantesBD.codigoEstadoCarteraGenerado,"=");
	    this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	    numRegBusqueda++;
	    campo=new InfoDatos("ae.institucion",this.institucion,"=");
	    this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	    numRegBusqueda++;
	    this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+""); 
	    //logger.info("mapa ajustes[mundo]->"+this.mapaConsulta);
	}
	
	/**
	 * Metodo para consultar la fecha de cierre
	 * inicial
	 * @param con Connection.
	 * @return String, fecha cierre inicial
	 */
	public String fechaCierreSaldoInicial(Connection con)
	{
	   String resp="";
	   this.mapaConsulta.clear(); 
	   String columnas="anio_cierre as anio," +
	   				   "mes_cierre as mes";
	   this.mapaConsulta.put("camposTraer",columnas);          
	   this.mapaConsulta.put("tabla","cierres_saldos");
	   ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);
	   try 
	    {
         while(rs.next())
         {
           resp=(rs.getInt("mes")+"")+"/"+(rs.getInt("anio")+"");  
         }
	    }catch (SQLException e) 
        {           
            e.printStackTrace();
        }
	    return resp;
	}
	
	/**
	 * Metodo para verificar si un ajuste posee
	 * detalle de facturas
	 * @param con Connection, conexión con la fuente de datos.
	 * @param numAjuste double, número del ajuste
	 * @param institucion int, código de la institución
	 * @return boolean, true si es efectivo
	 */
	public boolean existeInfoAjustesXFacturas (Connection con,double numAjuste,int institucion)
	{
	 boolean existeInfo=true;
	 int numRegBusqueda=0;
	 InfoDatos campo;
	 this.mapaConsulta.clear();
	 String columnas="COUNT(1) as filas";
	 this.mapaConsulta.put("camposTraer",columnas);          
	 this.mapaConsulta.put("tabla","ajus_fact_empresa");
	 campo=new InfoDatos("codigo",numAjuste,"=");
	 this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	 numRegBusqueda++;
	 campo=new InfoDatos("institucion",institucion,"=");
	 this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
	 numRegBusqueda++;
	 this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
	 ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);
	 try 
	  {
	      while(rs.next())
	      {
	          if(rs.getInt("filas")<=0)
	              existeInfo=false;
	      }
	  }catch (SQLException e) 
     {           
         e.printStackTrace();
     }
	 return existeInfo;
	}
	
	/**
	 * metodo para verificar si una factura se
	 * encuentra en estado facturada.
	 * @param con Connection
	 * @param numFactura double, número de la factura
	 * @param institucion int, código de la factura
	 * @return boolean, true si es estado facturada
	 */
	public boolean existefacturaEnEstadoFacturada (Connection con,double numFactura,int institucion)
	{
	    boolean existeInfo=true;
	    int numRegBusqueda=0;
		InfoDatos campo;
		 this.mapaConsulta.clear();
	    String columnas="COUNT(1) as filas"; 
	    this.mapaConsulta.put("camposTraer",columnas);          
		this.mapaConsulta.put("tabla","facturas");
		campo=new InfoDatos("estado_facturacion",ConstantesBD.codigoEstadoFacturacionFacturada,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		campo=new InfoDatos("codigo",numFactura,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		campo=new InfoDatos("institucion",institucion,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
		ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);
		 try 
		  {
		      while(rs.next())
		      {
		          if(rs.getInt("filas")<=0)
		              existeInfo=false;
		      }
		  }catch (SQLException e) 
	     {           
	         e.printStackTrace();
	     }
		 return existeInfo;
	}
	
	/**
	 * metodo para verificar sin un ajuste tiene 
	 * entrada por factura a nivel de servicios.
	 * @param con
	 * @param numFact double, número de la factura
	 * @param numAjuste double, número del ajuste
	 * @param institucion int, código de la institucion
	 * @return boolean, true si posee servicios
	 */
	public boolean existeAjusteDeLaFacturaANivelDeServicios(Connection con,double numFact,double numAjuste,int institucion)	
	{
	    int numRegBusqueda=0;
		InfoDatos campo;
		 this.mapaConsulta.clear();
	    String columnas="codigo"; 
	    this.mapaConsulta.put("camposTraer",columnas);          
		this.mapaConsulta.put("tabla","ajus_det_fact_empresa");  
		campo=new InfoDatos("codigo",numAjuste,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		campo=new InfoDatos("factura",numFact,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		campo=new InfoDatos("institucion",institucion,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
		ResultSetDecorator rs=aprobacionDao.consultaAvanzadaAjustes(con,this.mapaConsulta);
		 try 
		  {
		      return rs.next();
		  }catch (SQLException e) 
	     {           
	         e.printStackTrace();
	     }
		 return false;
	}	
	
	
	/**
	 * Metodo para realizar la aprobacion del ajuste
	 * @param con
	 * @param actualizarAjusteMedicoPool 
	 * @return boolean
	 */
	public boolean generarAprobacionTrans (Connection con, boolean actualizarAjusteMedicoPool)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		
		try 
		{
			if (aprobacionDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (AprobacionAjustesEmpresaDao - generarAprobacionTrans )");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}
			else
			{	
				//********************VALIDACION APLICACION DE AJUSTES AL POOL*************************
				//Si el ajustes tenía valor del pool y no se deseó aplicar el ajuste se pone en 0 el valor de ajuste al pool 
				if(UtilidadTexto.getBoolean(this.mapaAprobacion.get("tiene_valor_pool_"+this.regSeleccionado).toString())&&
					!actualizarAjusteMedicoPool)
				{
					enTransaccion = aprobacionDao.actualizarValorAjustePool(con, this.mapaAprobacion.get("codigo_ajuste_"+this.regSeleccionado)+"", this.mapaAprobacion.get("factura_"+this.regSeleccionado).toString());
				}
				//*****************************************************************************************
				
				if(enTransaccion)
				{
				    HashMap facturas=new HashMap();
				    facturas=(HashMap)this.mapaAprobacion.get("facturas_"+this.regSeleccionado);
				    for(int j=0;j<Integer.parseInt(facturas.get("numRegFact")+"");j++)
				    {
				        //actualizar los valores credito y debito de la factura
	                    enTransaccion=aprobacionDao.actualizarValoresFacturas(con,Integer.parseInt(facturas.get("factura_"+j)+""),Double.parseDouble(facturas.get("valor_ajuste_"+j)+""),this.institucion,Integer.parseInt(this.mapaAprobacion.get("tipo_ajuste_"+this.regSeleccionado)+""));
	                    if(enTransaccion)
	    		        {
	                    	//debe hacerse siempre que tenga cuenta de cobro.
	    			        //if(this.mapaAprobacion.get("esPorCxC_"+this.regSeleccionado).equals("true"))
	    			        {
	    			        	double ccTemp=Utilidades.obtenerCuentaCobroFactura(Integer.parseInt(facturas.get("factura_"+j)+""));
	    				        int estadoCxC=Utilidades.obtenerEstadoCuentaCobro(con,ccTemp,this.institucion);
	    				        //si la factura tiene cuenta de cobro.
	    				        if(ccTemp>0)
	    				        	enTransaccion=aprobacionDao.actualizarCuentaDeCobro(con,ccTemp,Double.parseDouble(facturas.get("valor_ajuste_"+j)+""),Integer.parseInt(this.mapaAprobacion.get("tipo_ajuste_"+this.regSeleccionado)+""),this.institucion,estadoCxC); 
	    			            if(!enTransaccion)
	    				        {
	    				            myFactory.abortTransaction(con);
	    						    logger.warn("Transaction Aborted-No se actualizo la tabla cuentas cobro");	
	    				        }
	    			        }
	    		        }
				        if(!enTransaccion)
				        {
				            myFactory.abortTransaction(con);
						    logger.warn("Transaction Aborted-No se actualizo la tabla de facturas");
	                        break;
				        }
				        if(enTransaccion)
					    {
					        if((facturas.get("tipo_factura_sistema_"+j)+"").equals("true"))
					        {
					            HashMap detFact=new HashMap();
	                            detFact=(HashMap)this.mapaAprobacion.get("detFacturas_"+j);
						        for(int i=0;i<Integer.parseInt(detFact.get("numRegistros")+"");i++)
							    {
						            //actualizar los valores del detalle de factura, ajustes debito y credito.
	                                enTransaccion=aprobacionDao.actualizarValoresFacturaServicios(con,Integer.parseInt(detFact.get("det_fact_solicitud_"+i)+""),Integer.parseInt(detFact.get("factura_"+i)+""),Double.parseDouble(detFact.get("valor_ajuste_"+i)+""),Double.parseDouble(detFact.get("valor_ajuste_pool_"+i)+""),Integer.parseInt(this.mapaAprobacion.get("tipo_ajuste_"+this.regSeleccionado)+""),actualizarAjusteMedicoPool);		
						            if(!enTransaccion)
							        {
							            myFactory.abortTransaction(con);
									    logger.warn("Transaction Aborted-No se actualizo la tabla detalle facturas");	
	                                    break;
							        }
	                                if(enTransaccion)
	                                {                                    
	                                   //actualizar los valores del detalle de factura a nivel de los asocios, ajustes debito y credito.
	                                    if(detFact.containsKey("detFacturasAsocio_"+i))
	                                    {
	                                        HashMap detFactAsoc=new HashMap();
	                                        detFactAsoc=(HashMap)detFact.get("detFacturasAsocio_"+i);    
	                                        if(!detFactAsoc.isEmpty())
	                                        {
	                                            for(int k=0;k<Integer.parseInt(detFactAsoc.get("numRegistros")+"");k++)
	                                            {
	                                                enTransaccion=aprobacionDao.actualizarValoresAsocioDetalleFactura(con,Integer.parseInt(detFactAsoc.get("det_aso_fac_solicitud_"+k)+""),Integer.parseInt(detFactAsoc.get("servicio_asocio_"+k)+""),Double.parseDouble(detFactAsoc.get("valor_ajuste_"+k)+""),Double.parseDouble(detFactAsoc.get("valor_ajuste_pool_"+k)+""),Integer.parseInt(this.mapaAprobacion.get("tipo_ajuste_"+this.regSeleccionado)+""),actualizarAjusteMedicoPool);
	                                                if(!enTransaccion)
	                                                {
	                                                    myFactory.abortTransaction(con);
	                                                    logger.warn("Transaction Aborted-No se actualizo la tabla detalle facturas asocios");  
	                                                    break;
	                                                }
	                                            }
	                                        }
	                                    }
	                                }
	                                if(enTransaccion)
	                                { 
	                                	if(Utilidades.esSolicitudTipo(con, detFact.get("solicitud_"+i)+"", ConstantesBD.codigoTipoSolicitudPaquetes))
	                                	{
	                                		AjustesFacturaEmpresa mundoAjuste = new AjustesFacturaEmpresa();
	                                		mundoAjuste.getAjustesDetalle().setDetFactSolicitud(Integer.parseInt(detFact.get("det_fact_solicitud_"+i)+""));
	                                		mundoAjuste.getAjustesDetalle().cargarDetallePaquete(con);
	                                		mundoAjuste.getAjustesDetalle().setValorAjuste(Double.parseDouble(facturas.get("valor_ajuste_"+j)+""));
	                                		
	                                		enTransaccion = calcularDistribucionDetallePaquetes(con,mundoAjuste,Integer.parseInt(this.mapaAprobacion.get("tipo_ajuste_"+this.regSeleccionado)+""));
	                                	}
	                                	
	                                	
	                                }
							    }
					        }
					    }                    
				    }
				}
			    if(enTransaccion)
		        {
		            enTransaccion=aprobacionDao.actualizarEstadoAjustes(con,Double.parseDouble(this.mapaAprobacion.get("codigo_ajuste_"+this.regSeleccionado)+""),this.institucion);
		            if(!enTransaccion)
			        {
			            myFactory.abortTransaction(con);
					    logger.warn("Transaction Aborted-No se actualizo el estado del ajuste");	
			        }		            
		        }
		        if(enTransaccion)
		        {
		            enTransaccion=aprobacionDao.insertarAprobacion(con,Double.parseDouble(this.mapaAprobacion.get("codigo_ajuste_"+this.regSeleccionado)+""),this.usuario,this.fechaAprobacion);
		            if(!enTransaccion)
			        {
			            myFactory.abortTransaction(con);
					    logger.warn("Transaction Aborted-No se inserto la aprobacion");	
			        }		            
		        }
			}
			if(enTransaccion)
	        {
			    myFactory.endTransaction(con);
			    logger.warn("End Transaction");	
	        }
			return enTransaccion;
		}
		catch (SQLException e) 
		{	       
	        e.printStackTrace();
	        return false;
	    }    
	}
	
	/**
	 * 
	 * @param con 
	 * @param mundo
	 * @param tipoAjuste 
	 * @return
	 */
	private boolean calcularDistribucionDetallePaquetes(Connection con, AjustesFacturaEmpresa mundo, int tipoAjuste)
	{
		double valAjusteTotal=mundo.getAjustesDetalle().getValorAjuste();
        double valAjusteInt = 0;
        double valorConsumoPaquete = 0;
        //hace referencia a la misma direccion de memoria, en tonces lo que se haga en la 
        //variable mapa queda reflejado en el mundo.
        boolean enTransaccion = true;
        try
        {
            HashMap mapa=mundo.getAjustesDetalle().getDetallePaquete();
            
            ///distribucion del ajuste
            {
                int k=0;
                
                for(k = 0 ; k < Integer.parseInt(mapa.get("numRegistros")+"") ; k ++)
                {
                	valorConsumoPaquete += Double.parseDouble(mapa.get("saldo_"+k)+"");
                }
                
                for(k = 0 ; k < Integer.parseInt(mapa.get("numRegistros")+"") ; k ++)
                {
                	logger.info("["+k+"] valor ajuste: "+valAjusteTotal);
                	logger.info("["+k+"] valor consumo paquete: "+valorConsumoPaquete);
                	logger.info("["+k+"] valor detalle: "+mapa.get("saldo_"+k));
                    valAjusteInt=((valAjusteTotal/valorConsumoPaquete)*Double.parseDouble(mapa.get("saldo_"+k)+""));
                    logger.info("=======>["+k+"] resultado: "+valAjusteInt);
                    valAjusteInt = Math.floor(valAjusteInt);
                    logger.info("=======>["+k+"] resultado con ceil: "+valAjusteInt);
                    mapa.put("valorajuste_"+k,valAjusteInt+"");
                    
                    enTransaccion=aprobacionDao.actualizarValoresPaquetesDetalleFactura(con,Integer.parseInt(mapa.get("codpaqdetfac_"+k)+""),Double.parseDouble(mapa.get("valorajuste_"+k)+""),0,tipoAjuste,false);
                    if(!enTransaccion)
                    {
                        UtilidadBD.abortarTransaccion(con);
                        logger.warn("Transaction Aborted-No se actualizo la tabla detalle facturas paquetes");  
                        break;
                    }
                }
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return enTransaccion;
	}
	
	/**
	 * Metodo para enviar los parametros al
	 * metodo que realiza la consulta del
	 * detalle por factura
	 * @param con
	 */
	public void consultarDetalleFacturas(Connection con)
	{
	    double codAjuste;
		int codFact;
		HashMap mapResult=new HashMap();
	    codAjuste=Double.parseDouble(this.mapaAprobacion.get("codigo_ajuste_"+this.regSeleccionado)+"");
	    HashMap facturas=new HashMap();
	    facturas=(HashMap)this.mapaAprobacion.get("facturas_"+this.regSeleccionado);
        if(!facturas.isEmpty())
	     for(int j=0;j<Integer.parseInt(facturas.get("numRegFact")+"");j++)
	     {
	         codFact=Integer.parseInt(facturas.get("factura_"+j)+"");
	         //mapResult=this.consultarAjustesDetalleFactura(con,codAjuste,codFact);
             mapResult=this.consultarDetalleFacturasXAjustes(con,codAjuste,codFact);
	         this.mapaAprobacion.put("detFacturas_"+j,mapResult);
	     }     
	}
    /**
     * Metodo para enviar los parametros al
     * metodo que realiza la consulta del
     * detalle por factura para asocios
     * @param con
     */
    public void consultarDetalleFacturasAsocio(Connection con)
    {       
        int codFact;
        HashMap mapResult;
        HashMap facturas=new HashMap();
        HashMap detFacturas=new HashMap();
        double codAjuste=Double.parseDouble(this.mapaAprobacion.get("codigo_ajuste_"+this.regSeleccionado)+"");        
        facturas=(HashMap)this.mapaAprobacion.get("facturas_"+this.regSeleccionado);
        for(int j=0;j<Integer.parseInt(facturas.get("numRegFact")+"");j++)
        {
            if(this.mapaAprobacion.containsKey("detFacturas_"+j))
            {
                detFacturas=new HashMap();
                detFacturas=(HashMap)this.mapaAprobacion.get("detFacturas_"+j);
                if(!detFacturas.isEmpty())
                    for(int i=0;i<Integer.parseInt(detFacturas.get("numRegistros")+"");i++)
                    {
                        mapResult=new HashMap();
                        if((detFacturas.get("escirugia_"+i)+"").equals("true"))
                        {
                            codFact=Integer.parseInt(detFacturas.get("factura_"+i)+""); 
                            int codigoPk=Integer.parseInt(detFacturas.get("codigopk_"+i)+"");
                            mapResult=this.consultaDetalleFacturaNivelAsocios(con,this.institucion,codAjuste,codFact,Integer.parseInt(detFacturas.get("det_fact_solicitud_"+i)+""),codigoPk);
                            detFacturas.put("detFacturasAsocio_"+i,mapResult);    
                        }                               
                    }
            }            
        }
        this.mapaAprobacion.putAll(detFacturas);           
    }
    
    /**
     * 
     * @param con
     */
    public void consultarDetallePaquetes(Connection con)
    {       
        int codFact;
        HashMap mapResult;
        HashMap facturas=new HashMap();
        HashMap detFacturas=new HashMap();
        double codAjuste=Double.parseDouble(this.mapaAprobacion.get("codigo_ajuste_"+this.regSeleccionado)+"");        
        facturas=(HashMap)this.mapaAprobacion.get("facturas_"+this.regSeleccionado);
        for(int j=0;j<Integer.parseInt(facturas.get("numRegFact")+"");j++)
        {
            if(this.mapaAprobacion.containsKey("detFacturas_"+j))
            {
                detFacturas=new HashMap();
                detFacturas=(HashMap)this.mapaAprobacion.get("detFacturas_"+j);
                if(!detFacturas.isEmpty())
                    for(int i=0;i<Integer.parseInt(detFacturas.get("numRegistros")+"");i++)
                    {
                        mapResult=new HashMap();
                        if(Utilidades.esSolicitudTipo(con, detFacturas.get("solicitud_"+i)+"", ConstantesBD.codigoTipoSolicitudPaquetes))
                        {
                            codFact=Integer.parseInt(detFacturas.get("factura_"+i)+"");             
                            mapResult=this.consultaDetalleFacturaNivelPaquetes(con,this.institucion,codAjuste,codFact,Integer.parseInt(detFacturas.get("det_fact_solicitud_"+i)+""));
                            detFacturas.put("detPaquetes_"+i,mapResult);    
                        }                               
                    }
            }            
        }
        this.mapaAprobacion.putAll(detFacturas);           
    }
    /**
     * metodo para consultar el detalle de facturas
     * para un ajuste, obteniendo el tipo de solicitud,
     * si es de cirugia ó no.
     * @param con Connection
     * @param codAjuste double
     * @param codFactura int 
     * @param institucion int 
     * @return HashMap
     */
    private HashMap consultarDetalleFacturasXAjustes(Connection con,double codAjuste,int codFactura)
    {
        return aprobacionDao.consultarDetalleFacturasXAjustes(con,codAjuste,codFactura,this.institucion);     
    }
    /**
     * metodo para generar la consulta del detalle de
     * las facturas a nivel de asocios, si existe información
     * de las mismas en ajustes de solicitudes cirugias, previamente
     * consultadas
     * @param con Connection
     * @param institucion int
     * @param codAjuste double
     * @param codFactura int 
     * @param detAsocFactSol int
     * @return HashMap
     */
    private HashMap consultaDetalleFacturaAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol,int codigoPkServArt)
    {
        return aprobacionDao.consultaDetalleFacturaAsocios(con, institucion, codAjuste, codFactura, detAsocFactSol,codigoPkServArt);
    }
    private HashMap consultaDetalleFacturaNivelAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol,int codigoPkServArt)
    {
        return aprobacionDao.consultaDetalleFacturaNivelAsocios(con, institucion, codAjuste, codFactura, detAsocFactSol,codigoPkServArt);
    }
    
    /**
     * 
     * @param con
     * @param institucion
     * @param codAjuste
     * @param codFactura
     * @param detAsocFactSol
     * @return
     */
    private HashMap consultaDetalleFacturaNivelPaquetes(Connection con,int institucion,double codAjuste,int codFactura,int detFacSol)
    {
        return aprobacionDao.consultaDetalleFacturaNivelPaquetes(con, institucion, codAjuste, codFactura, detFacSol);
    }
    
    /**
     * metodo para validar que las facturas internas 
     * que tengan definidos ajustes a solicitudes de Cx,
     * exista la información del ajuste a nivel de asocios. 
     * @param con Connection
     * @param codAjuste int
     * @param codFactura int
     * @return boolean
     */
    public boolean ajusteFacturaASolicitudDeCxExisteAsocio(Connection con,double codAjuste,int codFactura)
    {
      HashMap map=new HashMap();
      map=this.consultarDetalleFacturasXAjustes(con,codAjuste,codFactura);   
      boolean existe=true;
      for(int k=0;k<Integer.parseInt(map.get("numRegistros").toString());k++)
      {
          if((map.get("escirugia_"+k).toString()).equals("true") && (map.get("serv_"+k)!=null))
          {
              HashMap map1=new HashMap();
              map1=this.consultaDetalleFacturaNivelAsocios(con,this.institucion,codAjuste,Integer.parseInt(map.get("factura_"+k)+""),Integer.parseInt(map.get("det_fact_solicitud_"+k)+""),Utilidades.convertirAEntero(map.get("codigopk_"+k)+""));
              if(map1.isEmpty())
              {             
                  existe=false;
                  break;
              }
          }
      }
      return existe;
    }
    public boolean ajusteFacturaASolicitudDeCxExisteANivelDeAsocio(Connection con,double codAjuste,int codFactura)
    {
      HashMap map=new HashMap();
      map=this.consultarDetalleFacturasXAjustes(con,codAjuste,codFactura);   
      boolean existe=true;
      for(int k=0;k<Integer.parseInt(map.get("numRegistros").toString());k++)
      {
          if((map.get("escirugia_"+k).toString()).equals("true"))
          {
              HashMap map1=new HashMap();
              map1=this.consultaDetalleFacturaNivelAsocios(con,this.institucion,codAjuste,Integer.parseInt(map.get("factura_"+k)+""),Integer.parseInt(map.get("det_fact_solicitud_"+k)+""),Utilidades.convertirAEntero(map.get("codigopk_"+k)+""));
              if(map1.isEmpty())
              {             
                  existe=false;
                  break;
              }
          }
      }
      return existe;
    }
	
	
    /**
     * @return Retorna mapaAprobacion.
     */
    public HashMap getMapaAprobacion() {
        return mapaAprobacion;
    }
    /**
     * @param mapaAprobacion Asigna mapaAprobacion.
     */
    public void setMapaAprobacion(HashMap mapaAprobacion) {
        this.mapaAprobacion = mapaAprobacion;
    }
    /**
     * @return Retorna codConvenio.
     */
    public int getCodConvenio() {
        return codConvenio;
    }
    /**
     * @param codConvenio Asigna codConvenio.
     */
    public void setCodConvenio(int codConvenio) {
        this.codConvenio = codConvenio;
    }
    /**
     * @return Retorna codTipoAjuste.
     */
    public int getCodTipoAjuste() {
        return codTipoAjuste;
    }
    /**
     * @param codTipoAjuste Asigna codTipoAjuste.
     */
    public void setCodTipoAjuste(int codTipoAjuste) {
        this.codTipoAjuste = codTipoAjuste;
    }
    /**
     * @return Retorna fechaAjuste.
     */
    public String getFechaAjuste() {
        return fechaAjuste;
    }
    /**
     * @param fechaAjuste Asigna fechaAjuste.
     */
    public void setFechaAjuste(String fechaAjuste) {
        this.fechaAjuste = fechaAjuste;
    }
    /**
     * @return Retorna numAjuste.
     */
    public double getNumAjuste() {
        return numAjuste;
    }
    /**
     * @param numAjuste Asigna numAjuste.
     */
    public void setNumAjuste(double numAjuste) {
        this.numAjuste = numAjuste;
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
     * @return Retorna numFactura.
     */
    public double getNumFactura() {
        return numFactura;
    }
    /**
     * @param numFactura Asigna numFactura.
     */
    public void setNumFactura(double numFactura) {
        this.numFactura = numFactura;
    }
    /**
     * @return Retorna mapaConsulta.
     */
    public HashMap getMapaConsulta() {
        return mapaConsulta;
    }
    /**
     * @param mapaConsulta Asigna mapaConsulta.
     */
    public void setMapaConsulta(HashMap mapaConsulta) {
        this.mapaConsulta = mapaConsulta;
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
     * @return Retorna regSeleccionado.
     */
    public int getRegSeleccionado() {
        return regSeleccionado;
    }
    /**
     * @param regSeleccionado Asigna regSeleccionado.
     */
    public void setRegSeleccionado(int regSeleccionado) {
        this.regSeleccionado = regSeleccionado;
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
}
