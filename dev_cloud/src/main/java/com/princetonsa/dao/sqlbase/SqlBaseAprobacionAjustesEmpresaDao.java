
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todos los metodos de acceso 
 * a la fuente de datos para cierre saldo inicial cartera
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseAprobacionAjustesEmpresaDao 
{
    /**
	 * Variable para manejar los log de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAprobacionAjustesEmpresaDao.class);
	
	/**
	 * consulta los datos principales de la
	 * tabla de ajustes_empresa, filtrando por
	 * fecha, cuentaCobro ó por codigo del ajuste.
	 */
	
	private static final String queryAjustesEmpresa="SELECT " +
														"ae.codigo as codigo_ajuste," +
														"ae.castigo_cartera as castigo_cartera," +
														"ae.tipo_ajuste as tipo_ajuste," +
														"to_char(ae.fecha_ajuste,'yyyy-mm-dd') as fecha_ajuste," +
														"ae.cuenta_cobro as cuenta_cobro," +
														"ae.concepto_ajuste as concepto_ajuste," +
														"ae.metodo_ajuste as metodo_ajuste," +
														"ae.valor_ajuste as valor_ajuste," +
														"ae.observaciones as observaciones " +
														"FROM ajustes_empresa ae ";
	
	/**
	 * query para actualizar los valores credito y debito de ajustes
	 */
	private static final String actualizarValoresAjustesEnFacturasStr="UPDATE facturas SET ";
	
	/**
	 * query para actualizar el estado del ajuste
	 */
	private static final String actualizarEstadoAjusteStr=" UPDATE ajustes_empresa SET estado="+ConstantesBD.codigoEstadoCarteraAprobado+" WHERE codigo=? AND institucion=?";
	
	/**
	 * query para actualizar la tabla de detalles factura
	 */
	private static final String actualizarDetalleFacturasStr=" UPDATE det_factura_solicitud SET ";
    /**
     * query para actualizar la tabla de detalles factura por asocio
     */
    private static final String actualizarDetalleAsociosFacturasStr=" UPDATE asocios_det_factura SET ";
    
    /**
     * query para actualizar la tabla de detalles factura por asocio
     */
    private static final String actualizarDetallePaquetesStr=" UPDATE paquetizacion_det_factura SET ";
	
	/**	
	 *query para insertar la aprobacion del ajuste 
	 */
	private static final String insertarAprobacionStr="INSERT INTO ajus_empresa_aprobados(codigo_ajuste,usuario,fecha_grabacion,hora_grabacion,fecha_aprobacion) VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
    /**
     * query para consultar el detalle de facturas
     */
    private static final String consultaDetalleFacturaStr="SELECT " +
                                                            "adfe.codigo_pk as codigopk," +
                                                            "adfe.codigo as ajuste," +
                                                            "adfe.factura as factura," +
                                                            "adfe.det_fact_solicitud as det_fact_solicitud," +
                                                            "dfs.solicitud as solicitud, " +                                                           
                                                            "adfe.valor_ajuste as valor_ajuste," +
                                                            "adfe.valor_ajuste_pool as valor_ajuste_pool," +
                                                            "adfe.concepto_ajuste as concepto_ajuste," +
                                                            "getNombreConceptoAjuste(adfe.concepto_ajuste) as nombre_concepto_ajuste, dfs.servicio as serv, dfs.articulo as art, "+
                                                            "case when s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" then 'true' else 'false' end as escirugia "+
                                                    "from ajus_det_fact_empresa adfe " +
                                                    "inner join det_factura_solicitud dfs on(dfs.codigo=adfe.det_fact_solicitud) " +
                                                    "inner join solicitudes s on(s.numero_solicitud=dfs.solicitud) " +
                                                    "where adfe.institucion=? and adfe.codigo=? and adfe.factura=?";
    /**
     * query para consultar la informacion a nivel de los asocios
     */
    private static final String consultaDetalleFacturaXAsocios="SELECT a.consecutivo as consecutivo, " +
			"a.codigo as codigo, " +
			"a.servicio_asocio as servicio, " +
			"a.valor_asocio as valorasocio, " +
			"a.tipo_asocio as tipoasocio, " +
			" getcodigopropservicio2(a.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+")||' - '|| getnombreservicio(a.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as nombretipoasocio, " +
			//"getnombretipoasocio(a.tipo_asocio) as nombretipoasocio, " +
			"a.ajuste_credito_medico as ajustecreditomedico, " +
			"a.ajuste_debito_medico as ajustedebitomedico, " +
			"a.porcentaje_pool as porcentajepool, " +
			"a.porcentaje_medico as porcentajemedico, " +
			"a.valor_medico as valormedico, " +
			"a.valor_cargo as valorcargo, " +
			"a.valor_iva as valoriva, " +
			"a.valor_recargo as valorrecargo, " +
			"a.valor_total as valortotal, " +
			"a.ajustes_credito as ajustescredito, " +
			"a.ajustes_debito as ajustesdebito, " +
			"coalesce(a.esquema_tarifario, "+ConstantesBD.codigoNuncaValido+") as esquematarifario, " +
			"a.valor_pool as valorpool, " +
			"coalesce(a.pool, "+ConstantesBD.codigoNuncaValido+") as pool, " +
			"coalesce(a.codigo_medico, "+ConstantesBD.codigoNuncaValido+") as codigomedico, " +
			"coalesce(a.medico_asocio, "+ConstantesBD.codigoNuncaValido+") as codigomedicoasocio, " +
			"coalesce(a.especialidad, "+ConstantesBD.codigoNuncaValido+") as codigoespecialidadasocio, " +
			"ta.tipos_servicio as tiposervicio, "+
			"a.codigo_propietario as codigopropietario "+
		"FROM " +
			"asocios_det_factura a " +
		"INNER JOIN " +
			"salascirugia.tipos_asocio ta ON(a.tipo_asocio=ta.codigo) " +
		"WHERE " +
			"a.codigo=? ";
    
    private static final String consultaDetalleFacturaXAsociosStr="SELECT " +
            "codigo_pk_ser_art as codigopkserart," +
            "codigo_pk as codigopk," +
            "codigo_ajuste as ajuste," +
            "factura as factura," +
            "det_aso_fac_solicitud as det_aso_fac_solicitud," +
            "servicio_asocio as servicio_asocio," +
            "valor_ajuste as valor_ajuste," +
            "valor_ajuste_pool as valor_ajuste_pool " +
      "from ajus_asocios_fact_empresa " +
     // "where institucion=? and codigo_ajuste=? and factura=? and det_aso_fac_solicitud=?";
      "where codigo_pk_ser_art=?";


	
    /**
     * 
     */
    private static final String consultaDetalleFacturaXpaquetes="SELECT " +
    																	"codigo_ajuste," +
    																	"factura," +
    																	"det_fac_solicitud," +
    																	"paq_det_factura," +
    																	"valor_ajuste," +
    																	"valor_ajuste_pool " +
    															" from ajus_paquetizacion_det_factura" +
    															" where institucion=? and codigo_ajuste=? and factura=? and det_fac_solicitud=? ";
	/**
	 * Metodo implementado para realizar una busqueda avanzada,por medio de un HashMap 
	 * que contiene los objetos con los respectivos nombres de los campos y los valores
	 * de cada uno de ellos por los cuales se filtra la busqueda.
	 * <code>numRegBusqueda</code> es la llave que lleva por defecto el mapa, con el número de 
	 * registros que se van adicionando al mapa, para la busqueda por campos.
	 * <code>1. </code>El mapa contendra objetos de tipo InfoDatos(id,value,descripcion), para las 
	 * columnas por las cuales se filtrara la busqueda, siendo el id=campo de la tabla, 
	 * el value=valor a buscar y la descripcion=operador(=,!=,is,not is).
	 * El parametro value del InfoDatos varia de tipo segun el tipo de Dato que alvergue 
	 * el campo de la tabla, pudiendo ser Varchar,Integer ó Float. Por ello la clase InfoDatos 
	 * contiene 3 constructores distintos (String,String,String),(String,itn,String) y (String,double,String). 
	 * La llave del mapa para identificar los registros tendra el siguiente formato 
	 * <code><campo_index></code>,siendo el index un <code>int</code> que comienza en 0 y
	 * se incrementa sucesivamente segun el numero de registros.
	 * <code>2. </code>Si la consulta posee inner join con otras tablas la llave para
	 * identificar los objetos tiene el formato <code><inner_index></code>, que hace 
	 * referencia al objeto infodatos con el constructor(String id,String value),
	 * el id=nombre de la tabla y el value=los campos del inner(ej. a.codigo=f,codigo).
	 * @param con Connection, conexión con la fuente de datos
	 * @param mapa HashMap, Con los campos y valores.
	 * @return ResultSet, Resultado de la consulta
	 * @author jarloc
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#consultaAvanzadaAjustes(java.sql.Connection,HashMap)
	 */
	public static ResultSetDecorator consultaAvanzadaAjustes(Connection con,HashMap mapa)
	{	    
	    String consulta="",p1="",p2="";
	    InfoDatos campo = new InfoDatos();
	    boolean existeWhere=false;
	    try 
	    {   
	        if(!mapa.isEmpty())
		    {
	            consulta="SELECT";	
	            consulta+=" "+mapa.get("camposTraer")+"";
                consulta+=" "+"FROM";
                consulta+=" "+mapa.get("tabla")+"";
                if(mapa.containsKey("numRegInner"))
                {
	                for(int k=0;k<Integer.parseInt(mapa.get("numRegInner")+"");k++)
	                {
	                    campo=(InfoDatos)mapa.get("inner_"+k);
	                    consulta+=" "+"INNER JOIN "+campo.getId()+" ON ("+campo.getValue()+")";
	                }
                }
                if(mapa.containsKey("numRegBusqueda"))
                {
		            for(int k=0;k<Integer.parseInt(mapa.get("numRegBusqueda")+"");k++)
			        {	                	                
		                campo=(InfoDatos)mapa.get("campoBusqueda_"+k);
		                String valor="";
		                //String valor=(campo.getValue().equals("-1")?(campo.getValueInt()==ConstantesBD.codigoNuncaValido?campo.getValueDouble()+"":campo.getValueInt()+""):"'"+campo.getValue()+"'");
		                if(campo.getValue().equals("-1"))
		                {
		                   if(campo.getValueInt()==ConstantesBD.codigoNuncaValido)
		                       valor=campo.getValueDouble()+"";
		                   else
		                       valor=campo.getValueInt()+"";
		                }
		                else
		                {	                    
		                    if(!campo.getValue().equals("null") && !campo.getDescripcion().equals("in"))
		                        valor="'"+campo.getValue()+"'";  
		                    else
		                        valor=campo.getValue();	                        
		                }
		                if(campo.getDescripcion().equals("in"))
		                {
		                    p1="(";
		                    p2=")";
		                }
			            if(!existeWhere)
				        {       
			                consulta+=" WHERE "+campo.getId()+" "+campo.getDescripcion()+" "+p1+valor+p2;
				            existeWhere=true;			            
				        }
				        else
				        {
				            consulta+=" AND "+campo.getId()+" "+campo.getDescripcion()+" "+p1+valor+p2;
				        }
			            if(campo.getDescripcion().equals("in"))
		                {
		                    p1="";
		                    p2="";
		                }
			        }
                }
	            if(mapa.containsKey("order"))
                {
	                campo=(InfoDatos)mapa.get("order");
	                consulta+=" ORDER BY "+campo.getId()+" "+campo.getValue();	                
                }
		    }
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch (SQLException e) 
	    {            
	        e.printStackTrace();
	        return null;
	    }	    
	}
	
	/**
	 * Metodo para actualizar los valores credito y debito
	 * de la factura
	 * @param con Connection
	 * @param numFact int, número de la factura
	 * @param vlrAjuste double, valor del ajuste
	 * @param institucion int, código de la institución
	 * @param tipoAjuste int, código del tipo del ajuste.
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#actualizarValoresFacturas(java.sql.Connection,HashMap)
	 */
	public static boolean actualizarValoresFacturas(Connection con,int numFact,double vlrAjuste,int institucion,int tipoAjuste)
	{
		String queryUpdate=actualizarValoresAjustesEnFacturasStr;
	    if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
	        queryUpdate+="ajustes_credito=ajustes_credito+"+vlrAjuste+" WHERE codigo="+numFact+" AND institucion="+institucion;
	    if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
	        queryUpdate+="ajustes_debito=ajustes_debito+"+vlrAjuste+" WHERE codigo="+numFact+" AND institucion="+institucion;
	    int r=0;
	    try
	    {
	      PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(queryUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	      r = ps.executeUpdate();
	      if(r<=0)
	          return false;
	      else
	          return true;
		}
	    catch(SQLException e)
	    {
			logger.error(e+"Error actualizarValoresFacturas"+e.toString());
			return false;
		}	    
	}
	
	/**
	 * Metodo para actualizar el estado del ajuste
	 * @param con Connection
	 * @param numAjuste double, número del ajuste
	 * @param institucion int, código de la institución
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#actualizarEstadoAjustes(java.sql.Connection,HashMap)
	 */
	public static boolean actualizarEstadoAjustes(Connection con,double numAjuste,int institucion)
	{
	    int r=0;
	    try
	    {
	      PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoAjusteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	      ps.setDouble(1,numAjuste);
	      ps.setInt(2,institucion);
	      r = ps.executeUpdate();
	      if(r<=0)
	          return false;
	      else
	          return true;
		}
	    catch(SQLException e)
	    {
			logger.error(e+"Error actualizarEstadoAjustes"+e.toString());
			return false;
		}	       
	}
	
	/**
	 * metodo para actualizar los valores del detalle de
	 * factura, ajustes debito y credito.
	 * @param con Connection
	 * @param codSolicitud int, código de la solicitud
	 * @param numFactura int, númro de la factura
	 * @param ajuste double, valor del ajuste
	 * @param ajusteMedico double, valor del ajuste medico
	 * @param tipoAjuste int, código del tipo de ajuste
	 * @param actualizarAjusteMedicoPool 
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#actualizarValoresFacturaServicios(java.sql.Connection,HashMap)
	 */
	public static boolean actualizarValoresFacturaServicios(Connection con,
															        int codSolicitud,int numFactura,
															        double ajuste,double ajusteMedico,
															        int tipoAjuste, boolean actualizarAjusteMedicoPool)
	{
		int r=0;
	    String queryUpdate=actualizarDetalleFacturasStr;
	    if(actualizarAjusteMedicoPool)
	    {
	    	
		    if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
		        queryUpdate+="ajustes_credito=ajustes_credito+"+ajuste+",ajuste_credito_medico=ajuste_credito_medico+"+ajusteMedico+" WHERE codigo="+codSolicitud+" AND factura="+numFactura;
		    if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
		        queryUpdate+="ajustes_debito=ajustes_debito+"+ajuste+",ajuste_debito_medico=ajuste_debito_medico+"+ajusteMedico+" WHERE codigo="+codSolicitud+" AND factura="+numFactura;
	    }
	    else
	    {
		    if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
		        queryUpdate+="ajustes_credito=ajustes_credito+"+ajuste+" WHERE codigo="+codSolicitud+" AND factura="+numFactura;
		    if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
		        queryUpdate+="ajustes_debito=ajustes_debito+"+ajuste+" WHERE codigo="+codSolicitud+" AND factura="+numFactura;
	    	
	    }
	    try
	    {
	      PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(queryUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	 
	      r = ps.executeUpdate();
	      if(r<=0)
	          return false;
	      else
	          return true;
		}
	    catch(SQLException e)
	    {
			logger.error(e+"Error actualizarValoresFacturaServicios"+e.toString());
			return false;
		}	     
	}
    
    /**
     * metodo para actualizar los valores del detalle de
     * asocio de facturas
     * @param con Connection
     * @param detAsocFactSol int
     * @param codServicio int
     * @param ajuste double, valor del ajuste
     * @param ajusteMedico double, valor del ajuste medico
     * @param tipoAjuste int, código del tipo de ajuste
     * @param actualizarAjusteMedicoPool 
     * @return boolean, true si es efectivo     
     */
    public static boolean actualizarValoresAsocioDetalleFactura(Connection con,int detAsocFactSol,int codServicio,double ajuste,double ajusteMedico,int tipoAjuste, boolean actualizarAjusteMedicoPool)
    {
        int r=0;
        String queryUpdate=actualizarDetalleAsociosFacturasStr;
        if(actualizarAjusteMedicoPool)
	    {
	        if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
	            queryUpdate+="ajustes_credito=ajustes_credito+"+ajuste+",ajuste_credito_medico=ajuste_credito_medico+"+ajusteMedico+" WHERE codigo="+detAsocFactSol+" AND servicio_asocio="+codServicio;
	        if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
	            queryUpdate+="ajustes_debito=ajustes_debito+"+ajuste+",ajuste_debito_medico=ajuste_debito_medico+"+ajusteMedico+" WHERE codigo="+detAsocFactSol+" AND servicio_asocio="+codServicio;
	    }
        else
        {
	        if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
	            queryUpdate+="ajustes_credito=ajustes_credito+"+ajuste+" WHERE codigo="+detAsocFactSol+" AND servicio_asocio="+codServicio;
	        if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
	            queryUpdate+="ajustes_debito=ajustes_debito+"+ajuste+" WHERE codigo="+detAsocFactSol+" AND servicio_asocio="+codServicio;
        }
        try
        {
          PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(queryUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));    
          r = ps.executeUpdate();
          if(r<=0)
              return false;
          else
              return true;
        }
        catch(SQLException e)
        {
            logger.error(e+"Error actualizarValoresAsocioDetalleFactura"+e.toString());
            return false;
        }        
    }
	
	/**
	 * Metodo para realizar la actualizacion de los
	 * valores de cuentas de cobro.
	 * @param con Connection
	 * @param numCxC double, número de la cuenta de cobro
	 * @param valorAjuste double, valor del ajuste
	 * @param tipoAjuste int, código del tipo de ajuste
	 * @param institucion int, código de la institución
	 * @param estadoCxC int, código del estado de la cuenta de cobro
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#actualizarCuentaDeCobro(java.sql.Connection,HashMap)
	 */
	public static boolean actualizarCuentaDeCobro (Connection con,
													        double numCxC,double valorAjuste,
													        int tipoAjuste,int institucion,int estadoCxC)
	{
	    int r=0;
	    double vlrSaldo=0;
	    String consulta="SELECT saldo_cuenta as saldo_cuenta,valor_inicial_cuenta as valor_inicial FROM cuentas_cobro WHERE numero_cuenta_cobro=? AND institucion=?";
	    try
	    {
	      PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
	      ps1.setDouble(1,numCxC);
	      ps1.setInt(2,institucion);
	      ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
	      if(rs.next())
	      {
	    	  String queryTemp="";
	          if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
	          {
	              vlrSaldo=rs.getDouble("saldo_cuenta") - valorAjuste;
	              queryTemp=", ajuste_credito=ajuste_credito+"+valorAjuste;
	          }
	          else if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
	          {
	              vlrSaldo=rs.getDouble("saldo_cuenta") + valorAjuste;
	              queryTemp=", ajuste_debito=ajuste_debito+"+valorAjuste;
	          }
	          String queryUpdate="UPDATE cuentas_cobro SET saldo_cuenta="+vlrSaldo+queryTemp;
	          
	          //if(estadoCxC==ConstantesBD.codigoEstadoCarteraGenerado)
	              //queryUpdate+=",valor_inicial_cuenta="+vlrSaldo;	            
	          queryUpdate+=" WHERE numero_cuenta_cobro="+numCxC+" AND institucion="+institucion;
	          PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(queryUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	          r=ps2.executeUpdate();
	          if(r<=0)
		          return false;
		      else
		          return true;
	      }
	      else
	          return false;	      
		}
	    catch(SQLException e)
	    {
			logger.error(e+"Error actualizarCuentaDeCobro"+e.toString());
			return false;
		}    
	}
	
	/**
	 * Metodo para realizar la insercion de la 
	 * aprobación del ajuste
	 * @param con Connection
	 * @param codAjuste double
	 * @param usuario String
	 * @param fechaApro String
	 * @return boolean, true efectivo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#insertarAprobacion(java.sql.Connection,HashMap)
	 */
	public static boolean insertarAprobacion(Connection con,double codAjuste,String usuario,String fechaApro)
	{
	    int r=0;
	    try
	    {
	      PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarAprobacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	      ps.setDouble(1,codAjuste);
	      ps.setString(2,usuario);
	      ps.setString(3,fechaApro);
	      r=ps.executeUpdate();
	      if(r<=0)
	          return false;
	      else
	          return true;
	    }
	    catch(SQLException e)
	    {
			logger.error(e+"Error insertarAprobacion"+e.toString());
			return false;
		}    
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
    public static HashMap consultarDetalleFacturasXAjustes(Connection con,double codAjuste,int codFactura,int institucion)
    {        
        try
        {
          PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
          ps.setInt(1,institucion);
          ps.setDouble(2,codAjuste);
          ps.setInt(3,codFactura);
          ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
          return UtilidadBD.cargarValueObjectSinNumRegistros(rs);
        }
        catch(SQLException e)
        {
            logger.error(e+"Error consultarDetalleFacturasXAjustes"+e.toString());
            return null;
        }    
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
     * @param codigoPkServArt 
     * @return HashMap
     */
    public static HashMap consultaDetalleFacturaNivelAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol, int codigoPkServArt)
    {
        try
        {
          PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaXAsociosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
          ps.setInt(1,codigoPkServArt);
          /*
          ps.setInt(1,institucion);
          ps.setDouble(2,codAjuste);
          ps.setInt(3,codFactura);
          ps.setInt(4,detAsocFactSol);
          */
          ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
          return UtilidadBD.cargarValueObjectSinNumRegistros(rs);
        }
        catch(SQLException e)
        {
            logger.error(e+"Error consultaDetalleFacturaNivelAsocios"+e.toString());
            return null;
        }    
    }
    
    public static HashMap consultaDetalleFacturaAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol, int codigoPkServArt)
    {   
        try
        {
          PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaXAsocios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
          ps.setInt(1,detAsocFactSol);
          /*
          ps.setInt(1,institucion);
          ps.setDouble(2,codAjuste);
          ps.setInt(3,codFactura);
          ps.setInt(4,detAsocFactSol);
          */
          ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
              
          return UtilidadBD.cargarValueObjectSinNumRegistros(rs);
        }
        catch(SQLException e)
        {
            logger.error(e+"Error consultaDetalleFacturaNivelAsocios"+e.toString());
            return null;
        }    
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
	public static HashMap consultaDetalleFacturaNivelPaquetes(Connection con, int institucion, double codAjuste, int codFactura, int detFacSol)
	{
		try
        {
          PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaXpaquetes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
          ps.setInt(1,institucion);
          ps.setDouble(2,codAjuste);
          ps.setInt(3,codFactura);
          ps.setInt(4,detFacSol);
          ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
          return UtilidadBD.cargarValueObjectSinNumRegistros(rs);
        }
        catch(SQLException e)
        {
            logger.error(e+"Error consultaDetalleFacturaNivelAsocios"+e.toString());
            return null;
        }    
	}

	public static boolean actualizarValoresPaquetesDetalleFactura(Connection con, int detPaqFacSol, double ajuste, double ajusteMedico, int tipoAjuste, boolean actualizarAjusteMedicoPool) 
	{
		 int r=0;
	        String queryUpdate=actualizarDetallePaquetesStr;
	        /*if(actualizarAjusteMedicoPool)
		    {
		        if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
		            queryUpdate+="ajustes_dif_consumo_credito=ajustes_dif_consumo_credito+"+ajuste+",ajuste_credito_medico=ajuste_credito_medico+"+ajusteMedico+" WHERE codigo="+detPaqFacSol;
		        if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
		            queryUpdate+="ajustes_debito=ajustes_debito+"+ajuste+",ajuste_debito_medico=ajuste_debito_medico+"+ajusteMedico+" WHERE codigo="+detPaqFacSol;
		    }
	        else
	        {*/
		        if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
		            queryUpdate+="ajustes_dif_consumo_credito=coalesce(ajustes_dif_consumo_credito,0)+"+ajuste+" WHERE codigo="+detPaqFacSol;
		        if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
		            queryUpdate+="ajustes_dif_consumo_debito=coalesce(ajustes_dif_consumo_debito,0)+"+ajuste+" WHERE codigo="+detPaqFacSol;
	        //}
		    logger.info("actualizacion: "+queryUpdate);
	        try
	        {
	          PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(queryUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));    
	          r = ps.executeUpdate();
	          if(r<=0)
	              return false;
	          else
	              return true;
	        }
	        catch(SQLException e)
	        {
	            logger.error(e+"Error actualizarValoresAsocioDetalleFactura"+e.toString());
	            return false;
	        }        
	}
	
	
	/**
	 * Método que verifica si el ajuste y la factura tienen valor de pool mayor a 0
	 * @param con
	 * @param codigoAjustes
	 * @param codigoFactura
	 * @return
	 */
	public static String tieneRegistroAjustePoolFactura(Connection con,String codigoAjustes,String codigoFactura)
	{
		String tiene = ConstantesBD.acronimoNo;
		int cuenta = 0;
		try
		{
			String consulta = " select count(1) as cuenta from ajus_det_fact_empresa WHERE codigo = ? and factura = ? and valor_ajuste_pool >0";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Double.parseDouble(codigoAjustes));
			pst.setLong(2,Long.parseLong(codigoFactura));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta = rs.getInt("cuenta");
				
			}
			pst.close();
			rs.close();
			
			consulta = " select count(1) as cuenta from ajus_asocios_fact_empresa WHERE codigo_ajuste = ? and factura = ? and valor_ajuste_pool >0";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Double.parseDouble(codigoAjustes));
			pst.setLong(2,Long.parseLong(codigoFactura));
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta += rs.getInt("cuenta");
				
			}
			pst.close();
			rs.close();
			
			consulta = " select count(1) as cuenta from ajus_paquetizacion_det_factura WHERE codigo_ajuste = ? and factura = ? and valor_ajuste_pool >0";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Double.parseDouble(codigoAjustes));
			pst.setLong(2,Long.parseLong(codigoFactura));
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta += rs.getInt("cuenta");
				
			}
			pst.close();
			rs.close();
			
			if(cuenta > 0)
			{
				tiene = ConstantesBD.acronimoSi;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneRegistroAjustePoolFactura: "+e);
		}
		return tiene;
	}
	
	/**
	 * Método para actualizar el valor de los ajustes al pool
	 * @param con
	 * @param codigoAjustes
	 * @param codigoFactura
	 * @return
	 */
	public static boolean actualizarValorAjustePool(Connection con,String codigoAjustes,String codigoFactura)
	{
		boolean exito = true;
		try
		{
			String consulta = " UPDATE ajus_det_fact_empresa set valor_ajuste_pool  = 0 WHERE codigo = ? and factura = ? ";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Double.parseDouble(codigoAjustes));
			pst.setLong(2,Long.parseLong(codigoFactura));
			pst.executeUpdate();
			
			pst.close();
			
			
			consulta = " update ajus_asocios_fact_empresa set valor_ajuste_pool  = 0 WHERE codigo_ajuste = ? and factura = ? ";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Double.parseDouble(codigoAjustes));
			pst.setLong(2,Long.parseLong(codigoFactura));
			pst.executeUpdate();
			
			
			consulta = "UPDATE ajus_paquetizacion_det_factura set valor_ajuste_pool  = 0 WHERE codigo_ajuste = ? and factura = ? ";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Double.parseDouble(codigoAjustes));
			pst.setLong(2,Long.parseLong(codigoFactura));
			pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarValorAjustePool: "+e);
			exito = false;
		}
		return exito;
	}

}
