
/*
 * Creado   9/09/2005
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

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaAjustesEmpresaDao;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
 
/**
 * Clase implementada para realizar la consulta/impresion
 * de ajustes de cartera y que corresponden a la
 * cartera de empresas de la institución .
 *
 * @version 1.0, 9/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaAjustes 
{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaAjustes.class);
    /**
	 * almacena las sentencias de las
	 * consultas
	 */
	private HashMap mapaConsulta;
	/**
	 * almacena el resultado de la consulta 
	 */
	private HashMap mapaAjustes;
	/**
	 * almacena el resultado de la consulta
	 * del detalle del ajuste
	 */
	private HashMap mapaDetalleAjustes;
	/**
	 * código del tipo de ajuste
	 */
	private int codTipoAjuste;
	/**
	 * almacena el código de la institución
	 */
	private int institucion;
	/**
	 * número del registro seleccionado
	 * para ver el detalle
	 */
	private int regSel;
	/**
     * numero del registro seleccionado
     * para ver el detalle de la factura
     */
    private int regSelFact;
    /**
     * codigo del estado del ajuste
     */
    private int estado;
    /**
     * número del ajuste inicial
     */
    private String ajusteInicial;
    /**
     * número del ajuste final
     */
    private String ajusteFinal;
    /**
     * fecha del ajuste inicial
     */
    private String fechaInicial;
    /**
     * fecha del ajuste final
     */
    private String fechaFinal;
    /**
     * codigo del concepto
     */
    private String concepto;
    /**
     * codigo de la factura
     */
    private int factura;
    /**
     * codigo del convenio
     */
    private int convenio;
    /**
     * registro seleccionado del detalle
     * de la factura
     */
    private int regSelDetFact;
	/**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
        this.mapaConsulta=new HashMap();
        this.codTipoAjuste=ConstantesBD.codigoNuncaValido;
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.mapaAjustes=new HashMap ();
        this.mapaDetalleAjustes=new HashMap ();
        this.regSel=ConstantesBD.codigoNuncaValido;
        this.regSelFact=ConstantesBD.codigoNuncaValido;
        this.estado=ConstantesBD.codigoNuncaValido;
        this.ajusteInicial="";
        this.ajusteFinal="";
        this.fechaFinal="";
        this.fechaInicial="";
        this.concepto="";
        this.factura=ConstantesBD.codigoNuncaValido;
        this.convenio=ConstantesBD.codigoNuncaValido;
        this.regSelDetFact=ConstantesBD.codigoNuncaValido;
    }	
    /**
	 * Constructor	 
	 */
	public  ConsultaAjustes()
	{
	    this.reset();	
	    this.init (System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static ConsultaAjustesEmpresaDao ajustesDao = null;
	
	
	/**
	 * 
	 * @param tipoBD
	 */
	private boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			ajustesDao = myFactory.getConsultaAjustesEmpresaDao();
			wasInited = (ajustesDao != null);
		}
		return wasInited;
	}

	
	/**
	 *metodo para construir la consulta de el
	 *detalle de las facturas. 
	 *
	 */
	private void construirConsultaDetalleFacturas (double numAjus,int codFact)
	{		        
	    int numRegBusqueda=0;
	    int numRegInner=0;
	    int numRegLeft=0;
	    InfoDatos campo;
	    this.mapaConsulta=new HashMap();
	    String columnas="adfe.codigo_pk as codigopk, " +
	    				"adfe.factura as factura," +
			    		"adfe.det_fact_solicitud as solicitud," +
			    		"s.consecutivo_ordenes_medicas as consolicitud," +
			    		"adfe.valor_ajuste as valor_ajuste," +			    					    		
			    		"getNombreConceptoAjuste(adfe.concepto_ajuste) as nombre_concepto_ajuste," +
			    		"getdescripcionpool(adfe.pool) as nombre_pool," +
			    		"getnombremedico(adfe.codigo_medico_responde) as nombre_medico," +
			    		"getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") as nombre_servicio," +
			    		"getdescarticulo(dfs.articulo) as nombre_articulo," +
			    		"dfs.articulo as codigo_art, "+
			    		"s.centro_costo_solicitante as centro_costo_solicitante," +
                        "case when s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" then 'true' else 'false' end as escirugia," +
                        "getDescempresainstitucion(empresa_institucion) as nombreempresa," +
                        "ser.codigo||'-'||ser.especialidad as codigo_servicio";  
	    this.mapaConsulta.put("camposTraer",columnas); 
	    this.mapaConsulta.put("tabla","ajus_det_fact_empresa adfe");
	    campo=new InfoDatos("adfe.codigo",numAjus,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;
        campo=new InfoDatos("adfe.factura",codFact,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;   
        campo=new InfoDatos("adfe.institucion",this.institucion,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++; 
        double cero=0.00;
        campo=new InfoDatos("adfe.valor_ajuste",cero,">");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++; 
        campo=new InfoDatos("facturas f","f.codigo=adfe.factura");
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        campo=new InfoDatos("det_factura_solicitud dfs","adfe.det_fact_solicitud=dfs.codigo");
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        campo=new InfoDatos("solicitudes s","dfs.solicitud=s.numero_solicitud");
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        campo=new InfoDatos("servicios ser","ser.codigo=dfs.servicio");
        this.mapaConsulta.put("left_"+numRegLeft,campo);  
        numRegLeft++;
        this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
        this.mapaConsulta.put("numRegInner",numRegInner+"");
        this.mapaConsulta.put("numRegLeft",numRegLeft+"");
	}
    /**
     * metodo para construir la consulta del 
     * detalle de facturas por asocio
     * @param numAjus double
     * @param codFact int
     * @param codDetAsocFactSol int
     */
    private void construirConsultaDetalleFacturaAsocio(int codigoPKSerArt)
    {
        int numRegBusqueda=0;
        int numRegInner=0;
        InfoDatos campo;
        this.mapaConsulta=new HashMap();
        String columnas="s.codigo||'-'||s.especialidad as codigo," +
                        "rs.descripcion as asocios," +
                        "getnombremedico(aafe.codigo_medico) as medico," +
                        "getdescripcionpool(aafe.pool) as pool," +
                        "aafe.valor_ajuste as valor_ajuste," +
                        "aafe.concepto_ajuste as concepto, "+
        				"getNombreConceptoAjuste(aafe.concepto_ajuste) as nombre_concepto_ajuste " ;
        this.mapaConsulta.put("camposTraer",columnas); 
        this.mapaConsulta.put("tabla","ajus_asocios_fact_empresa aafe");
        campo=new InfoDatos("aafe.codigo_pk_ser_art",codigoPKSerArt,"=");  
        this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
        numRegBusqueda++;
        campo=new InfoDatos("servicios s","s.codigo=aafe.servicio_asocio");
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        campo=new InfoDatos(" referencias_servicio rs","rs.servicio=s.codigo and rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups);
        this.mapaConsulta.put("inner_"+numRegInner,campo);  
        numRegInner++;
        this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
        this.mapaConsulta.put("numRegInner",numRegInner+"");               
    }
	/**
	 * Metodo para realizar la consulta de ajustes,
	 * detalle de facturas por ajuste y detalle
	 * de servicios por factura.
	 * @param con Connection, conexión con la fuente de datos.
	 */
	public void realizarConsultaAjustes(Connection con)
	{
	    this.generarConsultaAjustes(con);	    
	}
	/**
	 * metodo para realizar la consulta del 
	 * detalle de ajustes
	 * @param con Connection
	 * @param codigoAjuste double
	 */
	public void realizarConsultaDetalleAjustes(Connection con,double codigoAjuste)
	{
        HashMap detalleAjustes=new HashMap();        	    
        detalleAjustes=this.generarConsultaDetalleAjustes(con,codigoAjuste);
        this.mapaAjustes.put("detalleAjuste_"+this.regSel,detalleAjustes);        
	}
	/**
	 * metodo para realizar la consulta de detalle
	 * de la factura
	 * @param con Connection	
	 */
	public void realizarConsultaDetalleFacturas(Connection con)
	{
	    double codigoAjuste=Double.parseDouble(this.mapaAjustes.get("codigo_ajuste_"+this.regSel)+"");
	    HashMap detalleAjustes=new HashMap();       
        detalleAjustes=(HashMap)this.mapaAjustes.get("detalleAjuste_"+this.regSel); 
        int codFact=Integer.parseInt(detalleAjustes.get("factura_"+this.regSelFact)+"");
        if((detalleAjustes.get("tipo_factura_sistema_"+this.regSelFact)+"").equals("true"))
        {	                    
            this.construirConsultaDetalleFacturas(codigoAjuste,codFact);	                    
            HashMap detalleFacturas=new HashMap();
            detalleFacturas=this.generarConsultaDetalleFacturas(con);
            this.mapaAjustes.put("detalleFactura_"+this.regSelFact,detalleFacturas);
        }
	}
    /**
     * metodo para realizar la consulta de detalle
     * de la factura
     * @param con Connection    
     */
    public void realizarConsultaDetalleFacturasAsocios(Connection con)
    {
        HashMap detalleFactura=new HashMap();   
        detalleFactura=(HashMap)this.mapaAjustes.get("detalleFactura_"+this.regSelFact);        
        if((detalleFactura.get("escirugia_"+this.regSelDetFact)+"").equals("true"))
        {
            /*double codigoAjuste=Double.parseDouble(this.mapaAjustes.get("codigo_ajuste_"+this.regSel)+"");        
            int codFact=Integer.parseInt(detalleFactura.get("factura_"+this.regSelDetFact)+"");
            int codDetAsocFactSol=Integer.parseInt(detalleFactura.get("solicitud_"+this.regSelDetFact)+"");*/
            int codPkDetFac=Integer.parseInt(detalleFactura.get("codigopk_"+this.regSelDetFact)+"");
            this.construirConsultaDetalleFacturaAsocio(codPkDetFac);
            HashMap detalleFacturasAsocio=new HashMap();
            detalleFacturasAsocio=this.generarConsultaDetalleFacturasAsocios(con);
            this.mapaAjustes.put("detalleFacturaAsocio_"+this.regSelDetFact,detalleFacturasAsocio);
        }        
    }
	/**
	 * metodo para generar la consulta de los ajuste
	 * @param con
	 */
	private void generarConsultaAjustes(Connection con)
	{
	    int pos=0;
	    
	    HashMap vo=this.construirValueObjectConsultaGeneral();
	    
	    ResultSetDecorator rs=ajustesDao.busquedaGeneralAjustes(con,vo);
	    String datoTemp="";
	    String[] datosAprob={};
	    if(rs!=null)
	    {
		    try  
		    {
	            while(rs.next())
	            {
	                this.mapaAjustes.put("codigo_ajuste_"+pos,rs.getInt("codigo_ajuste")+"");
	                this.mapaAjustes.put("tipo_ajuste_"+pos,rs.getInt("tipo_ajuste")+"");
	                this.mapaAjustes.put("consecutivo_ajuste_"+pos,rs.getString("consecutivo_ajuste")+"");
	                this.mapaAjustes.put("nombre_estado_"+pos,rs.getString("nombre_estado")+"");
	                this.mapaAjustes.put("codigo_estado_"+pos,rs.getInt("codigo_estado")+"");
	                this.mapaAjustes.put("fecha_elaboracion_"+pos,UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_elaboracion")+""));
	                this.mapaAjustes.put("hora_elaboracion_"+pos,rs.getString("hora_elaboracion")+"");
	                this.mapaAjustes.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");
	                this.mapaAjustes.put("cuenta_cobro_"+pos,rs.getInt("cuenta_cobro")+"");
	                this.mapaAjustes.put("usuario_elaboracion_"+pos,rs.getString("usuario_elaboracion")+"");
	                this.mapaAjustes.put("castigo_cartera_"+pos,rs.getBoolean("castigo_cartera")+"");
	                this.mapaAjustes.put("observaciones_"+pos,rs.getString("observaciones")+"");
	                this.mapaAjustes.put("reversado_"+pos,rs.getString("reversado")+"");
	                this.mapaAjustes.put("codajustereversado_"+pos,rs.getString("codajustereversado")+"");
	                this.mapaAjustes.put("consecutivo_factura_"+pos,rs.getString("consecutivo_factura")+"");
	                this.mapaAjustes.put("nombre_convenio_"+pos,rs.getString("nombre_convenio")+"");
	                this.mapaAjustes.put("nombreempresa_"+pos,rs.getString("nombreempresa")+"");
	                if(rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteCreditoCuentaCobro || rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteCreditoFactura)              
	                    this.mapaAjustes.put("nombre_tipo_ajuste_corto_"+pos,ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()+"");
	                if(rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteDebitoCuentaCobro || rs.getInt("tipo_ajuste")==ConstantesBD.codigoAjusteDebitoFactura)
	                    this.mapaAjustes.put("nombre_tipo_ajuste_corto_"+pos,ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()+"");
	                datoTemp=consultarDatosAprobacion(con,rs.getDouble("codigo_ajuste"));
	                if(!datoTemp.equals(""))
	                {
	                    datosAprob=datoTemp.split("@");
	                    this.mapaAjustes.put("fecha_aprobacion_"+pos,datosAprob[0]+"");
	                    this.mapaAjustes.put("usuario_aprobacion_"+pos,datosAprob[1]+"");
	                }
	                if(rs.getDouble("cuenta_cobro")!=ConstantesBD.codigoNuncaValidoDouble)
	                {                    
	                    this.mapaAjustes.put("nombre_convenio_"+pos,consultarConvenio(con,rs.getDouble("cuenta_cobro")));
	                    this.mapaAjustes.put("esPorCxC_"+pos,"true");
	                }
	                else  
	                {
	                    this.mapaAjustes.put("esPorCxC_"+pos,"false");
	                    HashMap detalleAjustes=new HashMap();        	    
	                    detalleAjustes=this.generarConsultaDetalleAjustes(con,rs.getDouble("codigo_ajuste"));
	                    this.mapaAjustes.put("detalleAjuste_"+pos,detalleAjustes);
	                }
	                pos ++;
	            }
		    }
		    catch (SQLException e) 
	        {           
	            e.printStackTrace();
	        } 
	    }
        this.mapaAjustes.put("numReg",pos+"");
	}
	
	/**
	 * 
	 * @return
	 */
	private HashMap construirValueObjectConsultaGeneral()
	{
		HashMap vo=new HashMap();
		vo.put("codTipoAjuste", this.codTipoAjuste+"");
	    vo.put("estado", this.estado+"");
	    vo.put("ajusteInicial", this.ajusteInicial+"");
	    vo.put("ajusteFinal", this.ajusteFinal+"");
	    vo.put("fechaInicial", this.fechaInicial+"");
	    vo.put("fechaFinal", this.fechaFinal+"");
	    vo.put("concepto", this.concepto+"");
	    vo.put("factura", this.factura+"");
	    vo.put("convenio", this.convenio+"");
	    vo.put("institucion", this.institucion+"");
	    return vo;
	}
	/**
	 * metodo para consultar informacion de la
	 * aprobacion del ajuste
	 * @param numAjuste double, numero del ajuste	
	 * @param con Connection
	 * @return String 
	 */
	private String consultarDatosAprobacion(Connection con,double numAjuste)
	{
	    this.mapaConsulta.clear();
	    int numRegBusqueda=0;
	    InfoDatos campo;
	    String datos="";
	    String columnas="fecha_aprobacion as fecha_aprobacion," +
	    				"usuario as usuario_aprobacion";
	    this.mapaConsulta.put("camposTraer",columnas); 
		this.mapaConsulta.put("tabla","ajus_empresa_aprobados");
		campo=new InfoDatos("codigo_ajuste",numAjuste,"=");  
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
		ResultSetDecorator rs=UtilidadBD.consultaGenerica(con,this.mapaConsulta);
		 try 
		    {
	            while(rs.next())
	            {
	               datos=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_aprobacion"))+"@"+rs.getString("usuario_aprobacion");   
	            }
		    }catch (SQLException e) 
	        {           
	            e.printStackTrace();            
	        } 
	    return datos;
	}
	/**
	 * metodo para consultar el nombre del
	 * convenio por cuenta de cobro
	 * @param con Connection
	 * @param numCxC double, número de la cuenta de cobro
	 * @return String, nombre del convenio
	 */
	private String consultarConvenio(Connection con,double numCxC)
	{
	    this.mapaConsulta.clear();
	    int numRegBusqueda=0;
	    InfoDatos campo;
	    String convenio="";
	    String columnas="getnombreconvenio(convenio) as nombre_convenio";
		this.mapaConsulta.put("camposTraer",columnas); 
		this.mapaConsulta.put("tabla","cuentas_cobro");
		campo=new InfoDatos("numero_cuenta_cobro",numCxC,"=");  
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		campo=new InfoDatos("institucion",this.institucion,"=");  
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");	
		ResultSetDecorator rs=UtilidadBD.consultaGenerica(con,this.mapaConsulta);
        try 
	    {
            while(rs.next())
            {
                convenio=rs.getString("nombre_convenio");   
            }
	    }catch (SQLException e) 
        {           
            e.printStackTrace();            
        }   
	    return convenio;
	}
	/**
	 * metodo para generar el detalle de los ajustes
	 * @param con
	 * return HashMap
	 * @param codigoAjuste 
	 */
	private HashMap generarConsultaDetalleAjustes(Connection con, double codigoAjuste)
	{
	    int pos=0;
	    HashMap detalleAjustes=new HashMap();
	    ResultSetDecorator rs=ajustesDao.consltarDetalleAjuste(con,codigoAjuste);
	    boolean existenReg=false;
	    if(rs!=null)
	    {
		    try 
		    {
	            while(rs.next())
	            {
	                detalleAjustes.put("factura_"+pos,rs.getInt("factura")+"");
	                detalleAjustes.put("consecutivo_factura_"+pos,rs.getString("consecutivo_factura")+"");
	                detalleAjustes.put("nombre_convenio_"+pos,rs.getString("nombre_convenio")+"");
	                detalleAjustes.put("codigocentroatencion_"+pos,rs.getString("codigocentroatencion")+"");
	                detalleAjustes.put("nombrecentroatencion_"+pos,rs.getString("nombrecentroatencion")+"");
	                detalleAjustes.put("metodo_ajuste_"+pos,rs.getString("metodo_ajuste")+"");
	                detalleAjustes.put("nombre_metodo_ajuste_"+pos,rs.getString("nombre_metodo_ajuste")+"");
	                detalleAjustes.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");
	                detalleAjustes.put("concepto_ajuste_"+pos,rs.getString("concepto_ajuste")+"");
	                detalleAjustes.put("tipo_factura_sistema_"+pos,rs.getBoolean("tipo_factura_sistema")+"");
	                detalleAjustes.put("nombreempresa_"+pos,rs.getString("nombreempresa")+"");
	                pos ++;
	                if(!existenReg)
	                    existenReg=true;
	            }
		    }
		    catch (SQLException e) 
	        {           
	            e.printStackTrace();
	        }
	    }
        detalleAjustes.put("numReg",pos+"");
	    return detalleAjustes;
	}
	/**
	 * metodo para generar el detalle de las facturas
	 * @param con Connection
	 * @return HashMap
	 */
	private HashMap generarConsultaDetalleFacturas(Connection con)
	{
	    int pos=0;
	    int tempPos;
	    HashMap detalleFacturas=new HashMap();  
	    ResultSetDecorator rs=UtilidadBD.consultaGenerica(con,this.mapaConsulta);
	    try 
	    {
	    	HashMap<Integer, Integer>mapaUbicacionArticulos=new HashMap<Integer, Integer>();
	    	
	        while(rs.next())
            {
	        //Mt 6167 Se crea para filtrar artículos repetidos en diferentes ordenes y solo mostrar un registro con el 
	        //total del valor del ajuste 	
	            Integer codigoArt=rs.getInt("codigo_art");
	        	if(codigoArt>0&&mapaUbicacionArticulos.containsKey(codigoArt)){
	        		tempPos=mapaUbicacionArticulos.get(codigoArt);
	        		Double valorAjuste=0d;
	        		if(detalleFacturas.get("valor_ajuste_"+tempPos)!=null){
	        			valorAjuste=Double.parseDouble(detalleFacturas.get("valor_ajuste_"+tempPos).toString());
	        		}
	        		detalleFacturas.put("valor_ajuste_"+tempPos,valorAjuste+rs.getDouble("valor_ajuste")+"");
        		}else{
	        	
        			if(codigoArt!=null){
        				mapaUbicacionArticulos.put(codigoArt,pos);
        			}
        			
		        	detalleFacturas.put("codigopk_"+pos,rs.getInt("codigopk")+"");
	                detalleFacturas.put("factura_"+pos,rs.getInt("factura")+"");
	                detalleFacturas.put("solicitud_"+pos,rs.getInt("solicitud")+"");
	                detalleFacturas.put("consolicitud_"+pos,rs.getInt("consolicitud")+"");
	                detalleFacturas.put("nombreempresa_"+pos,rs.getString("nombreempresa")+"");
		            if(rs.getObject("nombre_servicio")==null)
		            {
		                detalleFacturas.put("nombre_servicio_articulo_"+pos,rs.getString("nombre_articulo")+"");
		                detalleFacturas.put("esServicio_"+pos,"false");
		            }
		            else
		            {
		                detalleFacturas.put("nombre_servicio_articulo_"+pos,rs.getString("nombre_servicio")+"");
		                detalleFacturas.put("esServicio_"+pos,"true");
		            }
		            detalleFacturas.put("nombre_medico_"+pos,rs.getString("nombre_medico")+"");
		            detalleFacturas.put("nombre_pool_"+pos,rs.getString("nombre_pool")+"");
		            detalleFacturas.put("valor_ajuste_"+pos,rs.getDouble("valor_ajuste")+"");
		            detalleFacturas.put("nombre_concepto_ajuste_"+pos,rs.getString("nombre_concepto_ajuste")+"");
		            detalleFacturas.put("centro_costo_solicitante_"+pos,rs.getInt("centro_costo_solicitante")+"");
	                detalleFacturas.put("escirugia_"+pos,rs.getString("escirugia")+"");
	                detalleFacturas.put("codigo_servicio_"+pos,rs.getString("codigo_servicio")+"");
		            pos ++;  
        		}
	        	
            }
	    }
	    catch (SQLException e) 
        {           
            e.printStackTrace();
        } 
        detalleFacturas.put("numReg",pos+"");
	    return detalleFacturas;
	}	
    /**
     * metodo para generar el detalle de las facturas
     * @param con Connection
     * @return HashMap
     */
    private HashMap generarConsultaDetalleFacturasAsocios(Connection con)
    {
    	logger.info("\n entre a generarConsultaDetalleFacturasAsocios ");
        ResultSetDecorator rs=UtilidadBD.consultaGenerica(con,this.mapaConsulta);
        HashMap mapa=new HashMap();
        mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
        //imprime el mapa
     //   Utilidades.imprimirMapa(mapa);
        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
        {
            if((mapa.get("medico_"+k)+"").equals("null"))
                mapa.put("medico_"+k,"");
            if((mapa.get("pool_"+k)+"").equals("null"))
                mapa.put("pool_"+k,"");
        }
        return mapa;
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
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
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
     * @return Retorna mapaAjustes.
     */
    public HashMap getMapaAjustes() {
        return mapaAjustes;
    }
    /**
     * @param mapaAjustes Asigna mapaAjustes.
     */
    public void setMapaAjustes(HashMap mapaAjustes) {
        this.mapaAjustes = mapaAjustes;
    }
    /**
     * @return Retorna mapaDetalleAjustes.
     */
    public HashMap getMapaDetalleAjustes() {
        return mapaDetalleAjustes;
    }
    /**
     * @param mapaDetalleAjustes Asigna mapaDetalleAjustes.
     */
    public void setMapaDetalleAjustes(HashMap mapaDetalleAjustes) {
        this.mapaDetalleAjustes = mapaDetalleAjustes;
    }
    /**
     * @return Retorna regSel.
     */
    public int getRegSel() {
        return regSel;
    }
    /**
     * @param regSel Asigna regSel.
     */
    public void setRegSel(int regSel) {
        this.regSel = regSel;
    }    
    /**
     * @return Retorna regSelFact.
     */
    public int getRegSelFact() {
        return regSelFact;
    }
    /**
     * @param regSelFact Asigna regSelFact.
     */
    public void setRegSelFact(int regSelFact) {
        this.regSelFact = regSelFact;
    }
    /**
     * @return Retorna ajusteFinal.
     */
    public String getAjusteFinal() {
        return ajusteFinal;
    }
    /**
     * @param ajusteFinal Asigna ajusteFinal.
     */
    public void setAjusteFinal(String ajusteFinal) {
        this.ajusteFinal = ajusteFinal;
    }
    /**
     * @return Retorna ajusteInicial.
     */
    public String getAjusteInicial() {
        return ajusteInicial;
    }
    /**
     * @param ajusteInicial Asigna ajusteInicial.
     */
    public void setAjusteInicial(String ajusteInicial) {
        this.ajusteInicial = ajusteInicial;
    }
    /**
     * @return Retorna concepto.
     */
    public String getConcepto() {
        return concepto;
    }
    /**
     * @param concepto Asigna concepto.
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
    /**
     * @return Retorna convenio.
     */
    public int getConvenio() {
        return convenio;
    }
    /**
     * @param convenio Asigna convenio.
     */
    public void setConvenio(int convenio) {
        this.convenio = convenio;
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
     * @return Retorna factura.
     */
    public int getFactura() {
        return factura;
    }
    /**
     * @param factura Asigna factura.
     */
    public void setFactura(int factura) {
        this.factura = factura;
    }
    /**
     * @return Retorna fechaFinal.
     */
    public String getFechaFinal() {
        return fechaFinal;
    }
    /**
     * @param fechaFinal Asigna fechaFinal.
     */
    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    /**
     * @return Retorna fechaInicial.
     */
    public String getFechaInicial() {
        return fechaInicial;
    }
    /**
     * @param fechaInicial Asigna fechaInicial.
     */
    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
    /**
     * @param regSelDetFact Asigna regSelDetFact.
     */
    public void setRegSelDetFact(int regSelDetFact) {
        this.regSelDetFact = regSelDetFact;
    }
    
    
}
