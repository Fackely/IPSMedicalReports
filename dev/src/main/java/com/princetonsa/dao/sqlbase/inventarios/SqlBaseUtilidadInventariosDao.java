/*
 * Created on 6/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.inventarios.AlmacenParametros;

/**
 * @version 1.0, 6/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class SqlBaseUtilidadInventariosDao
{

    /**
     * Objeto para manejar los logs de esta clase
     */
     private static Logger logger = Logger.getLogger(SqlBaseUtilidadInventariosDao.class);
     
     /**
     * Cadena consulta las exstencias totales de un articulo
     */
    private static String consultaExistenciasTotalesArticulo = "SELECT " +
    																"CASE WHEN SUM(aa.existencias) IS NULL " +
    																"THEN 0 ELSE SUM(aa.existencias) END AS existencias " +
    															"FROM articulos_almacen aa " +
    																"INNER JOIN almacen_parametros ap " +
    																"ON (ap.codigo=aa.almacen AND ap.institucion=aa.institucion) " +
    															"WHERE aa.articulo = ? AND aa.institucion = ? AND ap.afecta_costo_prom = '"+ConstantesBD.acronimoSi+"' ";
    
    /**
     * @param con
     * @param institucion
     * @param incluirAlmacenTodos 
     * @return
     */
    public static HashMap listadoAlmacensActivos(Connection con, int institucion, boolean incluirAlmacenTodos)
    {
        HashMap mapa=null;
        PreparedStatementDecorator ps;
        String cadena="SELECT codigo as codigo, nombre as nombre,tipo_area as tipoarea,es_activo as activo,centro_atencion as codigocentroatencion,getnomcentroatencion(centro_atencion) as nombrecentroatencion from centros_costo where tipo_area ="+ConstantesBD.codigoTipoAreaSubalmacen+" and institucion="+institucion +" and es_activo="+ValoresPorDefecto.getValorTrueParaConsultas();
        if(!incluirAlmacenTodos)
        	cadena+=" AND codigo != 0";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    /**
     * metodo que actualiza las existencias articulos x almacen tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * no se reutiliza el metodo actualizarExistenciasArticuloAlmacen(con, int, int, int, int)
     * para mantenerlo en un solo execute
     * Para que funcione el bloqueo del registro el metodo debe ser llamado en una transaccion, de 
     * lo contrario, el bloqueo no aplicaria.
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @return boolean 
     */
    public static boolean actualizarExistenciasArticuloAlmacen(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion)
    {
        String cadena="";
        //primero se evalua que el articulo tenga una entrada en 'articulos_almacen' de lo se inserta de una
        ArrayList filtro=new ArrayList();
        filtro.add(codArticulo+"");
        filtro.add(codAlmacen+"");
        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);
        if(existeArticuloEnExistenciasXAlmacen(con, codArticulo, codAlmacen, institucion))
        {    
            cadena="UPDATE articulos_almacen SET existencias = (existencias ";
            if(esCantidadASumar)
                cadena+="+";
            else
                cadena+="-";
            cadena+=cantidadArticulo+") ";
            cadena+=" where almacen=? and articulo=? and institucion=?";
        }
        else
        {
            cadena="INSERT INTO articulos_almacen(almacen, articulo, existencias, institucion) VALUES(?, ?, ";
            if(esCantidadASumar)
                cadena+=cantidadArticulo;
            else
                cadena+="-"+cantidadArticulo;
            cadena+= ", ?)";
        }
        PreparedStatementDecorator ps=null;
        logger.info("actualizarExistenciasArticuloAlmacen->"+cadena+" almacen->"+codAlmacen+" codArticulo->"+codArticulo+" institucion->"+institucion);
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAlmacen);
            ps.setInt(2, codArticulo);
            ps.setInt(3, institucion);
            if(ps.executeUpdate()>0)
            {
            	return true;
            }    
            else
                return false;
        }
        catch (SQLException e)
        {
            Log4JManager.error("actualizarExistenciasArticuloAlmacen");
        }finally{
        	try {
				if(ps!=null){
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("actualizarExistenciasArticuloAlmacen: cerrando ps");
			}
        }
        
        return false;
    }

    /**
     * metodo que evalua si un articulo ya esta insertado en articulos_almacen
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public static boolean existeArticuloEnExistenciasXAlmacen(Connection con, int codArticulo, int codAlmacen, int codInstitucion )
    {
        try
        {
            String consulta="SELECT count(1) AS numResultados FROM articulos_almacen WHERE almacen=? and articulo=? and institucion=?";
            PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            statement.setInt(1, codAlmacen);
            statement.setInt(2, codArticulo);
            statement.setInt(3, codInstitucion);
            ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
            if(resultado.next())
            {
                int numResultados=resultado.getInt("numResultados");
                return numResultados>0;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultando existeArticuloEnExistenciasXAlmacen (SqlBaseUtilidadInventarioDao): "+e);
            return false;
        }
        return false;
    }
    
    /**
     * @param con
     * @param codArticulo
     * @param nuevoCosto
     * @return
     */
    public static boolean actualizarCostoPromedioArticulo(Connection con, int codArticulo, double nuevoCosto)
    {
        String cadena="UPDATE articulo SET costo_promedio = ?  where codigo=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDouble(1, nuevoCosto);
            ps.setInt(2, codArticulo);
            ps.executeUpdate();
            
            Articulo articulo= new Articulo();
            articulo.modificarTarifas(con, nuevoCosto, codArticulo, ConstantesIntegridadDominio.acronimoPrecioUltimaCompra);
            
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


    
    /**
     * metodo para generar el listado de las 
     * transacciones validas por centro de costo, 
     * filtrando las que no se encuentren definidas
     * en parametros generales.
     * @param con Connection
     * @param institucion int
     * @param centroCosto int
     * @return HashMap
     */
    public static HashMap transaccionesValidasCentroCosto(Connection con, int institucion, int centroCosto,Vector restCodigoValDef,boolean esIn)
    {
        HashMap mapa=null;
        String cadena="SELECT " +
        				" tvc.codigo as codigo," +
        				" tvc.tipos_trans_inventario as tipo_trans_inventario," +
						" tvc.centros_costo AS centro_costo, "+
        				" tti.descripcion as desc_tipo_transaccion," +
        				" tti.tipos_conceptos_inv as tipo_conceptotran," +
        				" tci.descripcion as desc_concepto," +
        				" tti.tipos_costo_inv as tipo_costo," +
        				" tcoi.descripcion as desc_costo," +
        				" tti.codigo as cod_tipo_trans_inv," +
        				" tti.activo as activo_tipo_trans," +
        				" coalesce(tvc.grupo_inventario, "+ConstantesBD.codigoNuncaValido+") as grupo," +
        				" CASE WHEN tvc.grupo_inventario is null then '' else getnomgrupoinventario(tvc.grupo_inventario,tvc.clase_inventario) end as desc_grupo," +
        				" coalesce(tvc.clase_inventario, "+ConstantesBD.codigoNuncaValido+") as clase, " +
        				" CASE WHEN tvc.clase_inventario IS NULL THEN '' ELSE getnomclaseinventario(tvc.clase_inventario) end as desc_clase " +
        			" from trans_validas_x_cc_inven tvc " +
        			" inner join tipos_trans_inventarios tti on (tvc.tipos_trans_inventario=tti.consecutivo) " +
        			" inner join tipos_conceptos_inv tci on (tti.tipos_conceptos_inv=tci.codigo) " +
        			" inner join tipos_costo_inv tcoi on (tti.tipos_costo_inv=tcoi.codigo) " +
        			" where tvc.institucion="+institucion+" and tti.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
        			
        			//CAMBIO SHAIO 728
        			//" AND tvc.clase_inventario IS NOT NULL AND tvc.grupo_inventario IS NOT NULL ";
        	        
        if(centroCosto>0)
        	cadena+=" AND tvc.centros_costo= "+centroCosto+" ";
        
        cadena+=restriccionesValoreXDefectoTransacciones(restCodigoValDef,esIn);
        Statement st;
        try
        {
            st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
            logger.info("cadena=> "+cadena);
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    /**
     * metodo para generar el listado de las 
     * transacciones validas por centro de costo, 
     * filtrando las que no se encuentren definidas
     * en parametros generales, y agrupando por la 
     * descripción de la tipo transacciónes.
     * @param con Connection
     * @param institucion int
     * @param centroCosto int
     * @return HashMap
     */
    public static HashMap transaccionesValidasCentroCostoAgrupados(Connection con, int institucion, int centroCosto,Vector restCodigoValDef,boolean esIn)
    {
        HashMap mapa=null;
        String cadena="SELECT DISTINCT " +  
                        " tvc.codigo as codigo," +
                        " tvc.tipos_trans_inventario as tipo_trans_inventario," +
                        " tvc.centros_costo AS centro_costo, "+
                        " tti.descripcion as desc_tipo_transaccion," +
                        " tti.tipos_conceptos_inv as tipo_conceptotran," +
                        " tti.indicativo_consignacion as indicativo_consignacion," +
                        " tci.descripcion as desc_concepto," +
                        " tti.tipos_costo_inv as tipo_costo," +
                        " tcoi.descripcion as desc_costo," +
                        " tti.codigo as cod_tipo_trans_inv," +
                        " tti.activo as activo_tipo_trans," +
                        " 'false' as esDuplicado " +                       
                    " from trans_validas_x_cc_inven tvc " +
                    " inner join tipos_trans_inventarios tti on (tvc.tipos_trans_inventario=tti.consecutivo) " +
                    " inner join tipos_conceptos_inv tci on (tti.tipos_conceptos_inv=tci.codigo) " +
                    " inner join tipos_costo_inv tcoi on (tti.tipos_costo_inv=tcoi.codigo) " +
                    " where tvc.institucion="+institucion+" and tti.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND tvc.clase_inventario IS NOT NULL AND tvc.grupo_inventario IS NOT NULL ";
        if(centroCosto>0)
            cadena+=" AND tvc.centros_costo= "+centroCosto+" ";
        
        cadena+=restriccionesValoreXDefectoTransacciones(restCodigoValDef,esIn);
        cadena+=" order by tvc.tipos_trans_inventario";
        Statement st;        
        try
        {
        	//logger.info(cadena);
            st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    /**
     * metodo para generar el filtro con las
     * transacciones definidas en los parametros
     * generales.
     * @param con Connection
     * @param institucion int
     * @param esIn
     * @return String
     * @author jarloc
     */
    private static String restriccionesValoreXDefectoTransacciones(Vector rest, boolean esIn)
    {
        String temp="",valor="",query="";            
        boolean esPrimero=true;
        Iterator it=rest.iterator();	            
        while(it.hasNext())
        {	                
            valor=it.next()+"";
            if(!valor.equals(""))
            {	                    
                if(!esPrimero)
                    temp+=",";
                temp+=valor;    
                esPrimero=false; 
            }
        }
        if(!temp.equals(""))
        {
            if(esIn)
                query=" AND tvc.tipos_trans_inventario in("+temp+") ";
            else
                query=" AND tvc.tipos_trans_inventario not in("+temp+") ";
        }
        return query;
    }
    
    /**
     * Metodo para cargar los almacenes validos,
     * validando que el usuario pertenezca al almacen 
     * @param con
     * @param institucion
     * @param login
     * @param filtroCentroAtencion
     * @return
     */
    public static HashMap listadoAlmacenesUsuarios(Connection con, int institucion, String login,int filtroCentroAtencion)
    {
        HashMap mapa=null;
        PreparedStatementDecorator ps;
        String cadena="SELECT " +
        	"cc.codigo as codigo, " +
        	"cc.nombre as nombre," +
        	"cc.tipo_area as tipoarea," +
        	"cc.es_activo as activo," +
        	"cc.centro_atencion as codigocentroatencion," +
        	"ap.tipo_consignac as tipo_consignac," +
        	"getnomcentroatencion(cc.centro_atencion) as nombrecentroatencion " +
        	"from " +
        	"centros_costo cc " +
        	"inner join usuarios_x_almacen_inv ua on (cc.codigo=ua.centros_costo) " +
        	"inner join almacen_parametros ap on (cc.codigo=ap.codigo) " +
        	"where " +
        	"cc.tipo_area ="+ConstantesBD.codigoTipoAreaSubalmacen+" and " +
        	"cc.institucion="+institucion +" and " +
        	"cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas();
        
        if(filtroCentroAtencion>0)
        	cadena += " and cc.centro_atencion = "+filtroCentroAtencion;
        
        cadena += " and ua.usuario='"+login+"' " +
        	"ORDER BY cc.nombre";
        try
        {
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    /**
     * 
     * @param con
     * @param institucion
     * @param centroCostoSolicitante
     * @param tipoTransaccion
     * @param paresClaseGrupo
     * @param planEspecial
     * @return
     */
    public static HashMap<Object, Object> listadoAlmacenes(	Connection con, 
    														int institucion, 
    														int centroCostoSolicitante,  
    														String tipoTransaccion, 
    														String paresClaseGrupo, 
    														boolean planEspecial)
    {
    	HashMap mapa=null;
        PreparedStatementDecorator ps;
    	String cadena="SELECT DISTINCT " +
    							"cc.codigo as codigo,  " +
    							"cc.nombre as nombre,  " +
    							"cc.tipo_area as tipoarea, " +
    							"cc.es_activo as activo, " +
    							"getnomcentroatencion(cc.centro_atencion) as centro_atencion " +
    						"from " +
    							"farmacia_x_centro_costo fxcc " +
    							"INNER JOIN det_farmacia_cc dfcc ON (dfcc.farmacia=fxcc.codigo) " +
    							"INNER JOIN centros_costo cc on(cc.codigo=dfcc.codigo_farmacia_cc and cc.institucion=fxcc.institucion) " +
    							"inner join trans_validas_x_cc_inven tv on (tv.centros_costo=cc.codigo)  " +
    							"inner join almacen_parametros ap on (cc.codigo=ap.codigo) " +
    						"WHERE " +
    							"fxcc.centro_costo="+centroCostoSolicitante+" " +
    							"and fxcc.institucion="+institucion+" " +
    							"and cc.tipo_area="+ConstantesBD.codigoTipoAreaSubalmacen+" " +
    							"and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
    							"AND tv.tipos_trans_inventario="+tipoTransaccion+" " +
    							//CAMBIO SHAIO 728
    							//"and tv.grupo_inventario is not null " +
    							"and tv.clase_inventario is not null " +
    							"and ap.tipo_consignac='"+ConstantesBD.acronimoNo+"' ";
    							
    	
    	//se verifica si se definieron pares de clase-grupo
        if(!paresClaseGrupo.equals(""))
        	cadena += " and tv.clase_inventario || '-' || tv.grupo_inventario IN ("+paresClaseGrupo+")";
    	
        if (planEspecial==true)
        	cadena += " AND ap.plan_especial = '" +ConstantesBD.acronimoSi+"'";
        else
        	cadena += " AND ap.plan_especial = '" +ConstantesBD.acronimoNo+"'";
        
        cadena += " ORDER BY cc.nombre";
        try
        {
        	logger.info("\n\nconsulta listadoAlmacenes->"+cadena+"\n\n");
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            // se toma el detalle de las clases inventario x cada centro costo
            for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()) ; i++)
            {
                int codigoAlmacen= Utilidades.convertirAEntero(mapa.get("codigo_"+i).toString());
                mapa.put("detalle_"+i,  cargarInfoClasesInventario(con, institucion, codigoAlmacen, paresClaseGrupo,tipoTransaccion));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    
    
    /**
     * Metodo para cargar los almacenes validos, validando que el usuario pertenezca al almacen y ademas
     *  filtrando por las transacciones validas por centro de costo. Tambien se carga el detalle de la info de
     * las clases inventario x centro costo
     * @param con
     * @param institucion
     * @param login
     * @param tipoTransaccion
     * @param pares clase-grupo (si va vacio no se valida)
     * @return
     */
    public static HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(
    		Connection con,int institucion,String login,String tipoTransaccion,String paresClaseGrupo, int filtroCentroAtencion, boolean planEspecial)
    {
        HashMap mapa=null;
        PreparedStatementDecorator ps;
        String cadena=  " SELECT DISTINCT " +
                                " cc.codigo as codigo, " +
                                " cc.nombre as nombre, " +
                                " cc.tipo_area as tipoarea, " +
                                " cc.es_activo as activo," +
                                " getnomcentroatencion(cc.centro_atencion) as centro_atencion " +
                                " from centros_costo cc " +
                                " inner join usuarios_x_almacen_inv ua on (cc.codigo=ua.centros_costo) " +
                                " inner join trans_validas_x_cc_inven tv on (tv.centros_costo=cc.codigo)  " +
                                " inner join almacen_parametros ap on (cc.codigo=ap.codigo)  " +
                                " where tv.tipos_trans_inventario="+tipoTransaccion +
                                " and tv.grupo_inventario is not null " +
                                " and tv.clase_inventario is not null " +
                                " and  ap.tipo_consignac =  '" +ConstantesBD.acronimoNo+"'"+
                                " and  cc.tipo_area =  " +ConstantesBD.codigoTipoAreaSubalmacen+
                                " and cc.institucion= " +institucion+
                                " and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
								" and ua.usuario = '" +login+"' ";
        //se verifica si se definieron pares de clase-grupo
        if(!paresClaseGrupo.equals(""))
        	cadena += " and tv.clase_inventario || '-' || tv.grupo_inventario IN ("+paresClaseGrupo+")";
        
        //Por la Tarea 3732 se quita el filtro por el centro de atencion
        /*if(filtroCentroAtencion>0)
        	cadena += " AND cc.centro_atencion= "+filtroCentroAtencion+" ";
        */
        
        if (planEspecial==true)
        	cadena += " AND ap.plan_especial = '" +ConstantesBD.acronimoSi+"'";
        else
        	cadena += " AND ap.plan_especial = '" +ConstantesBD.acronimoNo+"'";
        
        cadena += " ORDER BY cc.nombre";
        try
        {
        	logger.info("\n\nconsulta listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC->"+cadena+"\n\n");
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            // se toma el detalle de las clases inventario x cada centro costo
            for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()) ; i++)
            {
                int codigoAlmacen= Utilidades.convertirAEntero(mapa.get("codigo_"+i).toString());
                mapa.put("detalle_"+i,  cargarInfoClasesInventario(con, institucion, codigoAlmacen, paresClaseGrupo,tipoTransaccion));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }    
    
    /**
     * Metodo que carga la informacion de las clases inventario restringiendo por el centro de costo
     * de las transacciones validas
     * @param con
     * @param institucion
     * @param codigoAlmacen
     * @param paresClaseGrupo
     * @param tipoTransaccion 
     * @return
     */
    public static HashMap cargarInfoClasesInventario(Connection con, int institucion, int codigoAlmacen, String paresClaseGrupo, String tipoTransaccion)
    {
        HashMap mapa=null;
        PreparedStatementDecorator ps;
        String cadena=  " SELECT DISTINCT " +
                                " ci.codigo AS codigoClaseInventario, " +
                                " ci.nombre AS nombreClaseInventario " +
                                " FROM " +
                                " trans_validas_x_cc_inven tv, " +
                                " clase_inventario ci " +
                                " WHERE " +
                                " tv.clase_inventario = ci.codigo " +
                                " AND tv.institucion= " +institucion+
                                " AND tv.centros_costo = " +codigoAlmacen;
        
        if(!tipoTransaccion.equals(""))
        	cadena += " AND tv.tipos_trans_inventario = "+tipoTransaccion+" ";
        
        if(!paresClaseGrupo.equals(""))
        	cadena += " and tv.clase_inventario || '-' || tv.grupo_inventario IN ("+paresClaseGrupo+")";
        cadena += " ORDER BY nombreClaseInventario ";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public static int existenciasTotalesArticulo(Connection con, int codArticulo, int institucion)
    {
        PreparedStatementDecorator ps = null;
        ResultSetDecorator rs = null;
        
        try{
            ps = new PreparedStatementDecorator(con.prepareStatement(consultaExistenciasTotalesArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codArticulo);
            ps.setInt(2, institucion);
            
            rs = new ResultSetDecorator(ps.executeQuery());
            
            if(rs.next())
                return rs.getInt("existencias");
        
        }catch(SQLException e){
			logger.error("ERROR -- SQLException existenciasTotalesArticulo: ", e);
			
		}catch(Exception ex){
			logger.error("ERROR -- Exception existenciasTotalesArticulo: ", ex);
			
		}finally{
			try{
				if(ps != null)
				{
					ps.close();
				}
				if(rs != null)
				{
					rs.close();
				}
			}catch(SQLException e){
				logger.info("Error close PreparedStatement - ResultSet", e);
			}
		}
        
        return 0;
    }
    
    /**
     * @param con
     * @param codArticulo
     * @param almacen
     * @param institucion
     * @return
     */
    public static int existenciasArticuloAlmacen(Connection con, int codArticulo, int almacen, int institucion)
    {
        String cadena="SELECT  existencias as existencias from articulos_almacen where articulo=? and almacen=? and institucion=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codArticulo);
            ps.setInt(2, almacen);
            ps.setInt(3, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("existencias");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * @param con
     * @param codArticulo
     * @param nuevoValor
     * @param porcentajeAlerta
     * @return
     */
    public static boolean esValorValidoCostoArticulo(Connection con, int codArticulo, double nuevoValor, double porcentajeAlerta)
    {
        double costoActual=costoActualArticulo(con, codArticulo);
        double difCosto=Math.abs(nuevoValor-costoActual);
        double difPermitida=costoActual*porcentajeAlerta/100;//porcentajeAleta esta en % por esta razon dividimos por 100%
        if(difCosto<=difPermitida)
            return true;
        return false;
    }
    
    /**
     * @param con
     * @param codArticulo
     * @return
     */
    public static double costoActualArticulo(Connection con, int codArticulo)
    {
    	double resultado=0;
        String cadena="SELECT costo_promedio as costo from articulo where codigo=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try
        {
            ps = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            ps.setInt(1, codArticulo);
            rs = ps.executeQuery();
            if(rs.next())
            	resultado= rs.getDouble("costo");

        }catch (SQLException e){
            logger.error("ERROR costoActualArticulo: ", e);
        
        }catch(Exception ex){
			logger.error("ERROR costoActualArticulo: ", ex);
		
        }finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
        
        return resultado;
    }
    
    /**
     * @param con
     * @param codArticulo
     * @param numeroUnidades
     * @param valorTotalTransacion
     * @param tipoTransaccion
     * @param institucion
     * @param codigoAlmacen
     * @return
     */
    public static double calcularCostoPromedioArticulo(Connection con, int codArticulo, int numeroUnidades, double valorTotalTransacion, int tipoTransaccion, int institucion, int codigoAlmacen)
    {
    	logger.info("\n\n*********CALCULO COSTO PROMEDIO DEL ARTICULO*************");
        
    	int multiplicando=tipoTransaccion==ConstantesBD.codigoTipoConceptoEntradaInv?1:-1;
        logger.info("Multiplicando (Entrada? 1:-1 --> "+multiplicando);
        
        int existencias=existenciasTotalesArticulo(con, codArticulo, institucion);
        logger.info("Exitencias Actuales Articulo Antes de la Transaccion --> "+existencias);
        
        double costoActual=costoActualArticulo(con, codArticulo);
        logger.info("Costo Actual Articulo Antes de la Transaccion --> "+costoActual);
        
        double valorTotalActualArticulos = existencias*costoActual;
        logger.info("Valor Total Articulo Antes de la Transaccion --> "+valorTotalActualArticulos);
        
        int totalExistencias = 0;
        double valorTotal = 0;
        logger.info("Numero Unidades de la Transaccion --> "+numeroUnidades);
        logger.info("Valor Total de la Transaccion --> "+valorTotalTransacion);
        
        if(AlmacenParametros.afectaCostoPromedio(con, codigoAlmacen, institucion)){
            totalExistencias = existencias+(numeroUnidades*multiplicando);
            valorTotal = valorTotalActualArticulos+(valorTotalTransacion*multiplicando);
            
            logger.info("Almacen Afecta Costo Promedio Articulo "+codArticulo);
            
        }else{
        	totalExistencias = existencias;
            valorTotal = valorTotalActualArticulos;
            
        	logger.info("Almacen No Afecta Costo Promedio Articulo "+codArticulo);
        }

        logger.info("Nuevo Total de Existencias --> "+totalExistencias);
        logger.info("Nuevo Valor Total --> "+valorTotal);
        
        double nuevoCosto=0;
        
        if(existencias<=0)
        {
        	if(numeroUnidades == 0 || !AlmacenParametros.afectaCostoPromedio(con, codigoAlmacen, institucion) )
        	{
        		nuevoCosto = costoActual;
        	}
        	else
        	{
        		nuevoCosto = valorTotalTransacion/numeroUnidades;// LOS ARTICULOS CUYA EXISTENCIA ES MENOR IGUAL QUE CERO ANTES DE GUARDA LA TRANSACCION ACTUAL, DEBEN ASIGNAR COMO NUEVO COSTO EL VALOR UNITARIO INGRESADO EN LA TRANSACCION
        	}
        }
        else
        {
        	if(totalExistencias!=0)
        	{
        		nuevoCosto=valorTotal/totalExistencias;
        	}
        	else
        	{
        		nuevoCosto= valorTotalTransacion/numeroUnidades;
        	}
        }
        
        logger.info("COSTO PROMEDIO  --> "+nuevoCosto);
    	logger.info("*********FIN CALCULO COSTO PROMEDIO DEL ARTICULO*************\n\n");
    	
        return Math.abs(nuevoCosto);
    }
    
    /**
     * Método que verifica si existe cierre de inventarios
     * para la fecha dada
     * @param con
     * @param fecha
     * @param institucion
     * @return
     */
    public static boolean existeCierreInventarioParaFecha(Connection con, String fecha, int institucion)
    {     
        String cadena=" SELECT codigo from cierre_inventarios where mes_cierre=? and anio_cierre=? and institucion=?";
        try
        {
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,fecha.split("/")[1]);
            ps.setString(2,fecha.split("/")[2]);
            ps.setInt(3, institucion);
            
            logger.info("existeCierreInventarioParaFecha-->"+cadena+" fecha.split(/)[1] -->"+fecha.split("/")[1]+" fecha.split(/)[2]-->"+fecha.split("/")[2]+" inst-->"+institucion);
            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            return rs.next();
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en el existeCierreInventarioParaFecha: SqlBaseUtilidadInventariosDao "+e.toString() );
            return false;
        }
    }


    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public static boolean existenciasArticuloMayorIgualStockMinimo(Connection con, int codArticulo, int institucion)
    {        
        String cadena="SELECT stock_minimo as stock,gettotalexisarticulos("+codArticulo+","+institucion+") as existencias from articulo where codigo="+codArticulo;
        PreparedStatementDecorator ps;
        int stock=0;
        int exis=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
                stock=rs.getInt("stock");
                exis=rs.getInt("existencias");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(exis>=stock)
            return true;
        return false;
    }


    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public static boolean existenciasArticuloMenorIgualStockMaximo(Connection con, int codArticulo, int institucion)
    {
        String cadena="SELECT stock_maximo as stock,gettotalexisarticulos("+codArticulo+","+institucion+") as existencias from articulo where codigo="+codArticulo;
        PreparedStatementDecorator ps;
        int stock=0;
        int exis=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
                stock=rs.getInt("stock");
                exis=rs.getInt("existencias");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(exis<=stock)
            return true;
        return false;
    }


    
    /** 
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public static boolean existenciasArticuloMayorIgualPuntoPedido(Connection con, int codArticulo, int institucion)
    {
        String cadena="SELECT punto_pedido as puntoPedido,gettotalexisarticulos("+codArticulo+","+institucion+") as existencias from articulo where codigo="+codArticulo;
        PreparedStatementDecorator ps;
        int pp=0;
        int exis=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
                pp=rs.getInt("puntoPedido");
                exis=rs.getInt("existencias");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(exis>=pp)
            return true;
        return false;
    }
    
    /**
     * Método que obtiene las parejas de clase-grupo para la 
     * interseccion centro costo y almacen
     * @param con
     * @param centroCosto
     * @param almacen
     * @return
     * El formato es String "'clase-grupo','clase-grupo',...."
     */
    public static String obtenerInterseccionClaseGrupo(Connection con,int centroCosto,int almacen)
    {
    	String cadena = "SELECT DISTINCT " +
    		"clase_inventario ||'-'|| grupo_inventario AS pareja " +
    		"FROM trans_validas_x_cc_inven " +
    		"WHERE " +
    		"centros_costo="+centroCosto+" AND " +
    		"clase_inventario || '-' || grupo_inventario IN " +
    			"(SELECT clase_inventario || '-' || grupo_inventario " +
    			"FROM trans_validas_x_cc_inven WHERE centros_costo = "+almacen+")";
    	String resultado = "";
    	
    	try
		{
    		Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
    		ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
    		
    		while(rs.next())
    		{
    			if(!resultado.equals(""))
    				resultado+=",";
    			resultado+="'"+rs.getString("pareja")+"'";
    		}
    		return resultado;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerInterseccionClaseGrupo de SqlBaseUtilidadInventariosDao:  "+e);
    		return "";
		}
    }
    
    /**
     * Método que verifica 
     * @param con
     * @param usuario
     * @param centroCosto
     * @param codigoIns
     * @return
     */
    public static boolean esAlmacenUsuarioAutorizado(Connection con,String usuario,int centroCosto, int codigoInstitucion)
    {
    	try
		{
    		String consulta = "SELECT " +
    			"count(1) AS resultado " +
    			"FROM inventarios.usuarios_x_almacen_inv " +
    			"WHERE centros_costo=? and usuario=? and institucion= ? ";
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,centroCosto);
    		pst.setString(2,usuario);
    		pst.setInt(3, codigoInstitucion);
            
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    			if(rs.getInt("resultado")>0)
    				return true;
    			else
    				return false;
    		else
    			return false;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en esAlmacenUsuarioAutorizado de SqlBaseUtilidadValidacionDao: "+e);
    		return false;
		}
    }


    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public static int obtenerAnioUltimoCierre(Connection con, int institucion)
    {
        String cadena="SELECT case when max(anio_cierre) is null then "+ConstantesBD.codigoNuncaValido+" else max(anio_cierre) end as aniocierre from cierre_inventarios where institucion = ?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("aniocierre");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public static int obtenerMesUltimoCierreAnio(Connection con, int institucion, int anio)
    {
        String cadena="SELECT case when max(mes_cierre) is null then "+ConstantesBD.codigoNuncaValido+" else max(mes_cierre) end as mescierre from cierre_inventarios where institucion = ? and anio_cierre=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2, anio);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("mescierre");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }


    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public static String fechaCierreInicial(Connection con, int institucion)
    {
        String cadena="SELECT mes_cierre||'/'||anio_cierre as fechacierre from cierre_inventarios where tipo_cierre="+ConstantesBDInventarios.tipoCierreInicial+" and institucion=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString("fechacierre");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido+"";
    }


    
    /**
     * @param con
     * @param institucion
     * @param fecha
     * @return
     */
    public static boolean existenCierresAnterioresAFecha(Connection con, int institucion, String fecha)
    {
        String cadena="select codigo from cierre_inventarios where (anio_cierre<?) or (anio_cierre=? and mes_cierre<?) and institucion=?";
        try
        {
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,Utilidades.convertirAEntero(fecha.split("/")[2]));
            ps.setInt(2,Utilidades.convertirAEntero(fecha.split("/")[2]));
            ps.setInt(3,Utilidades.convertirAEntero(fecha.split("/")[1]));
            ps.setInt(4, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            return rs.next();
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en el existenCierresAnterioresAFecha: SqlBaseUtilidadInventariosDao "+e.toString() );
            return false;
        }
    }


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public static boolean existeCierreFinalAnio(Connection con, int institucion, String anio)
    {
        String cadena="SELECT codigo from cierre_inventarios where anio_cierre = ? and tipo_cierre = ? and institucion=?";
        try
        {
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,anio);
            ps.setInt(2,ConstantesBDInventarios.tipoCierreAnual);
            ps.setInt(3, institucion);
            
            logger.info("existeCierreFinalAnio---->"+cadena+" anio->"+anio+" tipocierreanual-->"+ConstantesBDInventarios.tipoCierreAnual+" institucion->"+institucion);
            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            return rs.next();
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en el existeCierreFinalAnio: SqlBaseUtilidadInventariosDao "+e.toString() );
            return false;
        }
    }


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public static String existeCierreInicialFecha(Connection con, int institucion, String anio)
    {
    	String resultado=ConstantesBD.codigoNuncaValido+"";
        String cadena="SELECT mes_cierre||'/'||anio_cierre as fechacierre from cierre_inventarios where tipo_cierre="+ConstantesBDInventarios.tipoCierreInicial+" and anio_cierre=? and institucion=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,Utilidades.convertirAEntero(anio));
            ps.setInt(2, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado=rs.getString("fechacierre");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
    }


    
    /**
     * @param con
     * @param institucion
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public static int numeroMovimientosInventariosEntreFecha(Connection con, int institucion, String fechaInicial, String fechaFinal)
    {
        String cadena="SELECT count(1) as cantidad from view_movimientos_inventarios vmi inner join centros_costo cc on (convertiranumero(vmi.almacen)=cc.codigo) where cc.institucion=? and vmi.fechaatencion between ? and ?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
            ps.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
            logger.info("Consulta --- "+cadena);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("cantidad");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public static boolean existeCierreFinalesPosterioresAnio(Connection con, int institucion, String anio)
    {
        String cadena="SELECT codigo from cierre_inventarios where anio_cierre > ? and tipo_cierre=? and institucion=?";
        try
        {
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,Utilidades.convertirAEntero(anio));
            ps.setInt(2,ConstantesBDInventarios.tipoCierreAnual);
            ps.setInt(3, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            return rs.next();
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en el existeCierreFinalAnio: existeCierreFinalesPosterioresAnio "+e.toString() );
            return false;
        }
    }


	public static String obtenerCostoUnitarioFinalMes(Connection con, int institucion, int articulo, String anio, String mes)
	{
		String resultado= ConstantesBD.codigoNuncaValido+"";
		//ultimo valor del mes de cierre
		String cadena= "SELECT aca.valor_costo_despues as valor from ajustes_costo_articulos aca inner join centros_costo cc on(aca.almacen=cc.codigo) where cc.institucion=? and aca.codarticulo=? and to_char(fecha, 'yyyy-mm')=? order by hora desc";
		PreparedStatement ps = null;
		ResultSetDecorator rs = null;
		
		PreparedStatement ps1 = null;
		ResultSetDecorator rs1 = null;
		
        try
        {
            ps = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            ps.setInt(1, institucion);
            ps.setInt(2, articulo);
            ps.setString(3,anio+"-"+mes);
            
            rs = new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
            	resultado= rs.getString("valor");
            }
            else
            {
            	//ultimo valor en cierres anteriores
            	cadena= "SELECT aca.valor_costo_despues as valor from ajustes_costo_articulos aca inner join centros_costo cc on(aca.almacen=cc.codigo) where cc.institucion=? and aca.codarticulo=? and to_number(to_char(fecha, 'yyyy'),'9999')=? order by (convertiranumero(to_char(fecha,'mm')||'')),hora desc";
            	ps1 = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
                ps1.setInt(1, institucion);
                ps1.setInt(2, articulo);
                ps1.setInt(3,Utilidades.convertirAEntero(anio));
                
                rs1=new ResultSetDecorator(ps1.executeQuery());
                
                if(rs1.next())
                {
                	resultado= rs1.getString("valor");
                }
                else
                {
                	//no se ha realizado ajustes se retorna el costo del ultimo movimiento, que es el mismo actual.
                	resultado= costoActualArticulo(con,articulo)+"";        
                }
            	
            }
        }catch (SQLException e){
            logger.error("ERROR obtenerCostoUnitarioFinalMes: ", e);
        
        }catch(Exception ex){
			logger.error("ERROR obtenerCostoUnitarioFinalMes: ", ex);
		
        }finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
				if(rs1 != null){
					rs1.close();
				}
				if(ps1 != null){
					ps1.close();
				}
			}catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
        return resultado;
	}


	public static double obtenerValorEntradaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		String cadena="SELECT dci.val_total_entradas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getDouble("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static double obtenerValorSalidaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		String cadena="SELECT dci.val_total_salidas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getDouble("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static int obtenerCantidadEntradaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		String cadena="SELECT dci.cantidad_total_entradas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static int obtenerCantidadSalidaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		String cadena="SELECT dci.cantidad_total_salidas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static double obtenerValorEntradaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		String cadena="SELECT dci.val_total_entradas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
           
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getDouble("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static double obtenerValorSalidaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		String cadena="SELECT dci.val_total_salidas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
          
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getDouble("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static int obtenerCantidadEntradaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		String cadena="SELECT dci.cantidad_total_entradas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
           
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static int obtenerCantidadSalidaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		String cadena="SELECT dci.cantidad_total_salidas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
          
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("valor");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}


	public static String obtenerCodigoCierreInventario(Connection con, int institucion, String mesCierre, String anioCierre)
	{
		String cadena=" SELECT codigo as codigo from cierre_inventarios where mes_cierre=? and anio_cierre=? and institucion=?";
        try
        {
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,Utilidades.convertirAEntero(mesCierre));
            ps.setInt(2,Utilidades.convertirAEntero(anioCierre));
            ps.setInt(3, institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
            	return rs.getString("codigo");
            }
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en el existeCierreInventarioParaFecha: SqlBaseUtilidadInventariosDao "+e.toString() );
        }
        return ConstantesBD.codigoNuncaValido+"";
	}


	/**
	 * metodo para consultar el costo del saldo,
	 * toma el ultimo registro desde la fecha/hora
	 * del movimiento que se esta relacionando.	 
	 * @param con Connection
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String
	 * @param numeroTransaccion String
	 * @return  String
	 */
	public static String obtenerCostoUnitarioKardex(Connection con, int institucion, int articulo, String fecha,String numeroTransaccion)
	{	   
		String fechaTemp=UtilidadFecha.conversionFormatoFechaABD(fecha);
		String cadena= "SELECT " +
								"aca.valor_costo_despues as valor " +
				       "from ajustes_costo_articulos aca " +
				       "inner join centros_costo cc on(aca.almacen=cc.codigo) " +
				       "where cc.institucion=? and aca.codarticulo=? and aca.fecha=?  " +
				       "and aca.numero_transaccion=? " +//joan 27/03/2006 se estaba filtrando por el codigo y es por numero_transaccion
				       "order by aca.fecha desc,aca.hora desc";
		PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2, articulo);
            ps.setDate(3,Date.valueOf(fechaTemp)); 
            ps.setString(4,numeroTransaccion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString("valor");            
            else
                return ConstantesBD.codigoNuncaValido+"";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "0";
	}
	/**
	 * metodo para consultar el costo del saldo,
	 * en el ultimo registro desde el dia anterior,
	 * toma el ultimo regisrro desde la fecha/hora.
	 * si no se encuentran registros se busca el 
	 * ultimo movimiento para el articulo.	 
	 * @param con Connection
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String	 
	 * @return  String
	 */
	public static String obtenerCostoUnitarioKardex(Connection con, String consulta, int institucion, int articulo, String fecha)
	{
	    //ultimo valor del mes de cierre
		String fechaTemp=UtilidadFecha.conversionFormatoFechaABD(fecha);
		String cadena= "SELECT " +
								"aca.valor_costo_despues as valor " +
				       "from ajustes_costo_articulos aca " +
				       "inner join centros_costo cc on(aca.almacen=cc.codigo) " +
				       "where cc.institucion=? and aca.codarticulo=? and fecha<?  " +				       
				       "order by fecha desc,hora desc";
		PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2, articulo);
            ps.setDate(3,Date.valueOf(fechaTemp));           
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString("valor");
            else
            {
            	//no se ha realizado ajustes se retorna el costo del ultimo movimiento.
                return obtenerCostoUnitarioKardexUltimosMovimientos(con, consulta,articulo,institucion,fechaTemp)+"";        
            	
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "0";
	}
	/**
	 * metodo para consultar el ultimo 
	 * movimiento de un articulo segun el
	 * orden cronologico.
	 * @param con Connection
	 * @param articulo int 
	 * @param institucion int  
	 * @param fecha String 
	 * @return String
	 */
	public static String obtenerCostoUnitarioKardexUltimosMovimientos(Connection con,String consulta,int articulo,int institucion,String fecha)
	{	    
  
	    PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("CONSULTA obtenerCostoUnitarioKardexUltimosMovimientos ----- "+consulta);
            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString("valor");
            else
                return "0";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "0";        
	}
	/**
	 * metodo para obtener el area de un 
	 * centro de costo
	 * @param con Connection
	 * @param codCentroCosto int
	 * @param institucion int 
	 * @return int, area del centro de costo
	 */
	public static int obtenerTipoAreaCentroCosto(Connection con,int codCentroCosto,int institucion)
	{
	  String query=" SELECT tipo_area as area from centros_costo where institucion="+institucion+" AND codigo="+codCentroCosto;
	  try 
	  {
        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
        if(rs.next())        
            return rs.getInt("area");
        else
            return ConstantesBD.codigoNuncaValido;
        
	  } catch (SQLException e) 
	  {       
        e.printStackTrace();
	  }
	  return ConstantesBD.codigoNuncaValido;  
	}
	/**
	 * metodo para validar si un almacen es
	 * valido para realizar traslado
	 * @param con Connection
	 * @param institucion int
	 * @param tipoTransaccion int
	 * @param centroCosto int
	 * @return boolean
	 */
	public static boolean esAlmacenAutorizadoParaTraslado(Connection con,int institucion,int tipoTransaccion,int centroCosto)
	{
	    String query="SELECT codigo as valor from trans_validas_x_cc_inven where institucion=? and tipos_trans_inventario=? and centros_costo=? and clase_inventario is not null and grupo_inventario is not null";
	    try 
		  {
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ps.setInt(2,tipoTransaccion);
	        ps.setInt(3, centroCosto);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())  
	            return true;
	        else
	            return false;      
	        
		  } catch (SQLException e) 
		  {       
	        e.printStackTrace();
		  }
	    return false;
	}
	/**
	 * metodo para cargar el listado de clase-grupo, de 
	 * un usuario segun su centro de costo, y un tipo
	 * de transacción definido.
	 * @param con Connection
	 * @param institucion int
	 * @param centroCosto int
	 * @param transaccion int
	 * @return HashMap
	 */
	public static HashMap listadoClaseGrupoSegunCentroCostoUsuario(Connection con,int institucion,int centroCosto,int transaccion)
	{
	    String query="SELECT clase_inventario||'-'||grupo_inventario as pareja from trans_validas_x_cc_inven where centros_costo=? and tipos_trans_inventario=? and institucion=?";
	    try 
		  {
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,centroCosto);
	        ps.setInt(2,transaccion);
	        ps.setInt(3,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		  } catch (SQLException e) 
		  {       
		      e.printStackTrace();
		  }
        return null;
	}
	
	/**
     * Metodo para cargar los almacenes validos, filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
     * @param con Connection
     * @param institucion int
     * @param login String
     * @param tipoTransaccion String
     * @param pares clase-grupo (si va vacio no se valida) String
     * @param centroCosto int
     * @return HashMap
     */
    public static HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(Connection con,int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto)
    {
        HashMap mapa=null;
        PreparedStatementDecorator ps;
        String cadena=  " SELECT DISTINCT " +
                                " cc.codigo as codigo, " +
                                " cc.nombre as nombre, " +
                                " cc.tipo_area as tipoarea, " +
                                " cc.es_activo as activo," +
                                //no es necesario, ya las clases y grupos se toman despues. en el action.
                                //" tv.clase_inventario as clase," +
                                //" tv.grupo_inventario as grupo," +
                                " getnomcentroatencion(cc.centro_atencion) as nombrecentroatencion " +
                                " from centros_costo cc " +                                
                                " inner join trans_validas_x_cc_inven tv on (tv.centros_costo=cc.codigo)  " +
                                " where tv.tipos_trans_inventario="+tipoTransaccion +
                                " and tv.grupo_inventario is not null " +
                                " and tv.clase_inventario is not null " +
                                " and cc.tipo_area =  " +ConstantesBD.codigoTipoAreaSubalmacen+
                                " and tv.institucion= " +institucion+
                                " and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+								
								" and tv.centros_costo!="+centroCosto;
        //se verifica si se definieron pares de clase-grupo
        if(!paresClaseGrupo.equals(""))
        	cadena += " and tv.clase_inventario || '-' || tv.grupo_inventario IN ("+paresClaseGrupo+")";        
        cadena += " ORDER BY cc.nombre";
        
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

	/**
     * Metodo para cargar los almacenes validos, filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
     * @param con Connection
     * @param institucion int
     * @param login String
     * @param tipoTransaccion String
     * @param pares clase-grupo (si va vacio no se valida) String
     * @param centroCosto int
     * @param login
     * @return HashMap
     */
    public static HashMap listadoAlmacenesUsuariosXCentroAtencion(Connection con,int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto, int centroAtencion, String login)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros", "0");
        PreparedStatementDecorator ps;
			        String cadena=  " SELECT DISTINCT " +
			        " cc.codigo as codigo, " +
			        " cc.nombre as nombre, " +
			        " cc.tipo_area as tipoarea, " +
			        " cc.es_activo as activo " +
			        " from centros_costo cc " +
			        " inner join usuarios_x_almacen_inv ua on (ua.centros_costo=cc.codigo) " +
			        " inner join trans_validas_x_cc_inven tv on (tv.centros_costo=cc.codigo)  " +
			        " where tv.tipos_trans_inventario="+tipoTransaccion +
			        " and tv.grupo_inventario is not null " +
			        " and tv.clase_inventario is not null " +
			        " and  cc.tipo_area =  " +ConstantesBD.codigoTipoAreaSubalmacen+
			        " and cc.institucion= " +institucion+
			        " and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
					" and ua.usuario = '" +login+"' "+
					" and cc.centro_atencion="+centroAtencion;
        //se verifica si se definieron pares de clase-grupo
        if(!paresClaseGrupo.equals(""))
        	cadena += " and tv.clase_inventario || '-' || tv.grupo_inventario IN ('"+paresClaseGrupo+"')";        
        cadena += " ORDER BY cc.nombre";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    /**
     * metodo que devuelve el numero de existencias (- o +) de un articulo, en caso de que genere error
     * devuelve un null
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public static String getExistenciasXArticulo(Connection con, int  codArticulo, int codAlmacen,int codInstitucion)
    {
      String query=" SELECT getTotalExisArticulosXAlmacen("+codAlmacen+","+codArticulo+","+codInstitucion+") AS existencias " ;
      if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
    	  query=query+" FROM DUAL ";
      logger.info("\n\n getExistenciasXArticulo->"+query+"\n\n");
      try 
      {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
           if(rs.next())        
               return rs.getString("existencias");
           else
               return null;
      } catch (SQLException e) 
      {       
          e.printStackTrace();
      }
      return null;  
    }


	public static String obtenerParejasClaseGrupoInventario(Connection con, int institucion, int centroCosto, String codigoTransaccion)
	{
		String cadena="SELECT '@'||coalesce(clase_inventario||'','')||'-'||coalesce(grupo_inventario||'','')||'@' from trans_validas_x_cc_inven where tipos_trans_inventario=? and centros_costo=? and institucion=?";
		String resultado="";
		 try 
	     {
	           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	           
	           logger.info("CADENA DE CONSULTA obtenerParejasClaseGrupoInventario >>>>>>>>>> "+cadena +" CODIGO TRANSACCION "+codigoTransaccion+" CENTRO COSTO "+centroCosto);
	           
	           ps.setInt(1, Utilidades.convertirAEntero(codigoTransaccion));
	           ps.setInt(2, centroCosto);
	           ps.setInt(3, institucion);
	           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	           int res=0;
	           while(rs.next())
	           {
	        	   if(res>0)
	        		   resultado=resultado+",";
	        	   resultado+=rs.getString(1).replaceAll("@", "'");
	        	   res++;
	           }
	     } 
		 catch (SQLException e) 
	     {       
	         e.printStackTrace();
	     }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @param esCantidadASumar
	 * @param cantidadArticulo
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static boolean actualizarExistenciasArticuloAlmacenLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento)
	{
        String cadena="";
        
        //primero se evalua que el articulo tenga una entrada en 'articulos_almacen' de lo se inserta de una
        logger.info("lote--- "+lote);
        logger.info("fecha vencimiento - -- "+fechaVencimiento);
        ArrayList filtro=new ArrayList();
        filtro.add(codArticulo+"");
        filtro.add(codAlmacen+"");
        if(!UtilidadTexto.isEmpty(lote))
        	filtro.add(lote.trim()+"");
        if(!UtilidadTexto.isEmpty(fechaVencimiento))
        	filtro.add((fechaVencimiento.trim().equals("")||fechaVencimiento.trim().equalsIgnoreCase("null"))?null:fechaVencimiento);
        if(!UtilidadTexto.isEmpty(lote))
        	UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacenLotes,filtro);
        else
        	UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);
        if(existeArticuloEnExistenciasXAlmacenLote(con, codArticulo, codAlmacen, lote,fechaVencimiento))
        {    
            cadena="UPDATE articulo_almacen_x_lote SET existencias = (existencias ";
            if(esCantidadASumar)
                cadena+="+";
            else
                cadena+="-";
            cadena+=cantidadArticulo+") ";
            cadena+=" where articulo=? and almacen=?  ";
            if(UtilidadTexto.isEmpty(lote.trim()))
            {
            	cadena+=" and lote is null ";
            }
            else
            {
            	cadena+=" and lote = ? ";
            }
            if(UtilidadTexto.isEmpty(fechaVencimiento.trim()))
            {
            	cadena+=" and fecha_vencimiento is null ";
            }
            else
            {
            	cadena+=" and fecha_vencimiento = ? ";
            }
        }
        else
        {
        	cadena="INSERT INTO articulo_almacen_x_lote(codigo,articulo, almacen, lote,fecha_vencimiento,existencias) VALUES("+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_art_alm_lot")+",?,?, ";
        	if(UtilidadTexto.isEmpty(lote.trim()))
            {
            	cadena+="null,";
            }
            else
            {
            	cadena+="?,";
            }
        	
        	if(UtilidadTexto.isEmpty(fechaVencimiento.trim()))
            {
            	cadena+="null,";
            }
            else
            {
            	cadena+="?,";
            }
        	
            if(esCantidadASumar)
                cadena+=cantidadArticulo;
            else
                cadena+="-"+cantidadArticulo;
            cadena+= " )";
        }
        PreparedStatementDecorator ps;
        try
        {
        	logger.info("actualizarExistenciasArticuloAlmacenLote->"+cadena+" art->"+codArticulo+" almacen->"+codAlmacen);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codArticulo);
            ps.setInt(2, codAlmacen);
            if(!UtilidadTexto.isEmpty(lote.trim()))
            {
            	logger.info("lote->"+lote);
            	ps.setString(3, lote);
            }
            if(!UtilidadTexto.isEmpty(fechaVencimiento.trim()))
        	{
            	logger.info("fechaV->"+fechaVencimiento);
            	ps.setDate(4, Date.valueOf(fechaVencimiento));
            }
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
	}

	/**
	 * Metodo que solo actualiza 'articulo_almacen_x_lote' cuando ya se encuentra un registro
	 * inicializado con lote=null , fecha_vencimiento=null , existencias=0
	 * desde [Ubicacion Articulos Almacen]
	 * MT 2353
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @param esCantidadASumar
	 * @param cantidadArticulo
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean actualizarArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento)
	{
        String cadena="";
                       
        ArrayList filtro=new ArrayList();
        filtro.add(codArticulo+"");
        filtro.add(codAlmacen+"");
                
        cadena="UPDATE articulo_almacen_x_lote SET existencias = (existencias ";
        if(esCantidadASumar)
            cadena+="+";
        else
            cadena+="-";
        cadena+=cantidadArticulo+"), lote=?, fecha_vencimiento=? ";
        cadena+=" where articulo=? and almacen=?  ";
            
        PreparedStatementDecorator ps=null;
        try
        {
        	logger.info("actualizarExistenciasArticuloAlmacenLote->"+cadena+" art->"+codArticulo+" almacen->"+codAlmacen);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
         	ps.setString(1, lote);
         	//MT 6015-5484 Se agrega validación, ya que la fecha de vencimiento es vacia y al hacer la conversión a Date arrojaba IllegalArgumentException  
         	if(!UtilidadTexto.isEmpty(fechaVencimiento)){
                ps.setDate(2, Date.valueOf(fechaVencimiento));
            }
            else{
        	    ps.setNull(2,Types.NULL);
            }
            ps.setInt(3, codArticulo);
            ps.setInt(4, codAlmacen);
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
        	Log4JManager.error("actualizarArticuloAlmacenXLote",e);
            
        }finally{
        	try {
				if(ps!=null){
					ps.close();
				}
			} catch (SQLException e1) {
				Log4JManager.error("actualizarArticuloAlmacenXLote--> cerrando ps");
			}
        }
        return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	private static boolean existeArticuloEnExistenciasXAlmacenLote(Connection con, int codArticulo, int codAlmacen, String lote, String fechaVencimiento)
	{
        try
        {
            String consulta="SELECT count(1) AS numResultados FROM articulo_almacen_x_lote WHERE almacen=? and articulo=? ";
    		if(!UtilidadTexto.isEmpty(lote.trim()))
    			consulta=consulta+" and lote = '"+lote+"'";
    		else
    			consulta=consulta+" and lote is null";

    		if(!UtilidadTexto.isEmpty(fechaVencimiento.trim()))
    			consulta=consulta+" and fecha_vencimiento = '"+fechaVencimiento+"'";
    		else
    			consulta=consulta+" and fecha_vencimiento is null";

    		
            PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            statement.setInt(1, codAlmacen);
            statement.setInt(2, codArticulo);
            ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
            if(resultado.next())
            {
                int numResultados=resultado.getInt("numResultados");
                return numResultados>0;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultando existeArticuloEnExistenciasXAlmacenLote (SqlBaseUtilidadInventarioDao): "+e);
            return false;
        }
        return false;
	}

	/**
	 * FIXME 
	 * MT 2353:Método que devuelve el mapa con lote, fechaVencimiento y existencias para saber si se creo solo 
	 * para registrar una existencia en lote='',fechaVencimiento ='' y existencias=0; y si se debe reemplazar
	 * al crear una transaccion sobre ese mismo articulo/almacen 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @return mapa 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap obtenerArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen)
	{
		PreparedStatementDecorator statement= null;
		HashMap mapa=new HashMap();
        try
        {
            mapa.put("numRegistros", "0");
            String consulta="SELECT lote as lote,fecha_vencimiento as fecha_vencimiento,existencias as existencias " +
            		" FROM articulo_almacen_x_lote " +
            		" WHERE almacen=? and articulo=? ";
    		
            statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            statement.setInt(1, codAlmacen);
            statement.setInt(2, codArticulo);
            
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()));  
            	
        }
        catch (SQLException e)
        {
            Log4JManager.error("Error consultando obtenerArticuloAlmacenXLote (SqlBaseUtilidadInventarioDao): "+e);
            
            try {
				if(statement!=null){
					statement.close();
				}
			} catch (SQLException e1) {
				Log4JManager.error("obtenerArticuloAlmacenXLote -->cerrando statement y prepares");				
			}
            
        }
        return (HashMap)mapa.clone();
	}
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param almacen
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static int existenciasArticuloAlmacenLote(Connection con, int codArticulo, int almacen, String lote, String fechaVencimiento)
	{
		String cadena="SELECT case when sum(existencias) is null then 0 else sum(existencias) end as existencias from articulo_almacen_x_lote " +
						" where articulo=? and almacen=? ";
		
		if(!UtilidadTexto.isEmpty(lote.trim()))
			cadena=cadena+" and lote = '"+lote+"'";
		else
			cadena=cadena+" and lote is null";

		if(!UtilidadTexto.isEmpty(fechaVencimiento.trim()))
			cadena=cadena+" and fecha_vencimiento = '"+fechaVencimiento+"'";
		else
			cadena=cadena+" and fecha_vencimiento is null";

        PreparedStatementDecorator ps;

        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            ps.setInt(1, codArticulo);
            ps.setInt(2, almacen);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("existencias");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
	}
  
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
    public static HashMap obtenerLotesDespachoNoAdministradosTotalmente(Connection con, String numeroSolicitud, String codigoArticulo)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros", "0");
        PreparedStatementDecorator ps;
			        String cadena=  "SELECT " +
			        					"DISTINCT dd.lote AS lote, " +
			        					"to_char(dd.fecha_vencimiento, 'DD/MM/YYYY') AS fechavencimiento, " +
			        					"(getDespachoXLote(d.numero_solicitud, dd.articulo,dd.lote, dd.fecha_vencimiento||'') - getAdministradoXLote(d.numero_solicitud, dd.articulo, dd.lote, dd.fecha_vencimiento||'', "+ValoresPorDefecto.getValorFalseParaConsultas()+") ) as existenciaxlote " +
			        				"FROM " +
			        					"despacho d " +
			        					"INNER JOIN detalle_despachos dd ON (d.orden=dd.despacho) " +
			        				"WHERE " +
			        					"d.numero_solicitud=? " +
			        					"and dd.articulo=? " +
			        					"and cantidad>0";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, Utilidades.convertirAEntero(numeroSolicitud));
            ps.setInt(2, Utilidades.convertirAEntero(codigoArticulo));
            mapa=util.UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    
    /**
     * 
     * @param lote
     * @param fechaVencimiento
     * @return
     */
    private static String restriccionLoteFechaVencimiento(String lote, String fechaVencimiento)
	{
		String cadena=" ";
		if(UtilidadTexto.isEmpty(lote.trim()))
		{
			cadena=cadena+" and lote is null and fecha_vencimiento is null ";
		}
		else
		{
			cadena=cadena+" and lote = '"+lote+"'";
			if(UtilidadTexto.isEmpty(fechaVencimiento.trim()))
			{
				cadena=cadena+" and fecha_vencimiento is null ";
			}
			else
			{
				cadena=cadena+" and fecha_vencimiento = '"+fechaVencimiento+"'";
			}
		}
		return cadena;	
	}
    
    /**
     * 
     * @param con
     * @param institucion
     * @param almacen
     * @param articulo
     * @param anio
     * @param mes
     * @param lote
     * @param fechaVencimiento
     * @return
     */
	public static int obtenerCantidadEntradaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		int resultado=0;
		String cadena="SELECT dci.cantidad_total_entradas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
		PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado= rs.getInt("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static int obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		int resultado=0;
		String cadena="SELECT dci.cantidad_total_entradas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado=rs.getInt("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static int obtenerCantidadSalidaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		int resultado=0;
		String cadena="SELECT dci.cantidad_total_salidas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado=rs.getInt("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static int obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		int resultado=0;
		String cadena="SELECT dci.cantidad_total_salidas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado=rs.getInt("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static double obtenerValorEntradaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		double resultado=0;
		String cadena="SELECT dci.val_total_entradas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado= rs.getDouble("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static double obtenerValorEntradaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		double resultado=0;
		String cadena="SELECT dci.val_total_entradas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado=rs.getDouble("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static double obtenerValorSalidaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		double resultado=0;
		String cadena="SELECT dci.val_total_salidas as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=?";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            ps.setInt(5,Utilidades.convertirAEntero(mes));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado= rs.getDouble("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static double obtenerValorSalidaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		double resultado=0;
		String cadena="SELECT dci.val_total_salidas_anio as valor from det_cierre_inventarios dci inner join cierre_inventarios ci on (dci.codigo_cierre=ci.codigo) where ci.institucion=?  and dci.almacen=? and dci.articulo=? and ci.anio_cierre=? and ci.mes_cierre=12";
		cadena=cadena+restriccionLoteFechaVencimiento(lote,fechaVencimiento);
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            ps.setInt(2,almacen);
            ps.setInt(3,articulo);
            ps.setInt(4,Utilidades.convertirAEntero(anio));
            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado= rs.getDouble("valor");
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
	}
	
	/**
	 * Método que consulta el nombre de una via de administracion a partir
	 * de su consecutivo de la tabla vias_admin_institucion
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerNombreViaAdminInstitucion(Connection con,int consecutivo)
	{
		try
		{
			String nombre = "";
			String consulta = "SELECT " +
				"va.nombre AS nombre " +
				"FROM vias_admin_institucion vai " +
				"INNER JOIN vias_administracion va ON(va.codigo=vai.via_administracion) " +
				"WHERE vai.codigo = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivo);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				nombre = rs.getString("nombre");
			rs.close();
			pst.close();
			return nombre;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreViaAdminInstitucion: "+e);
			return  "";
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @return
	 */
	public static int obtenerTipoCierre(Connection con, String codigoCierre)
	{
		String cadena="SELECT tipo_cierre from cierre_inventarios where codigo=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,codigoCierre);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static String obtenerPrecioBaseVentaArticulo(Connection con, int articulo) 
	{
		String cadena="SELECT coalesce(precio_base_venta,0)  from articulo where codigo=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,articulo);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "0";
	}

	
	/**
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static String obtenerPrecioUltimaCompraArticulo(Connection con, int articulo) 
	{
		String cadena="SELECT coalesce(precio_ultima_compra,0)  from articulo where codigo=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,articulo);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "0";
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerAlmacenesConsignacion(Connection con, int institucion) 
	{
		String cadena="SELECT codigo,getnomcentrocosto(codigo) as nombre from inventarios.almacen_parametros where tipo_consignac = '"+ConstantesBD.acronimoSi+"' and codigo>0 and institucion="+institucion;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerConveniosProveedor(Connection con, int institucion,int articulo) 
	{
		String cadena="SELECT DISTINCT proveedor,t.descripcion,codigo_axioma as articulo from facturacion.convenio_proveedor cp inner join facturacion.terceros t on(t.numero_identificacion=cp.proveedor)  where t.institucion="+institucion;
		if(articulo>0)
		{
			cadena=cadena+" and codigo_axioma="+articulo;
		}
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerProveedoresCatalogo(Connection con, int institucion) 
	{
		String cadena="SELECT DISTINCT cp.proveedor,t.descripcion,codigo_axioma as articulo from inventarios.catalogo_proveedor cp inner join facturacion.terceros t on(t.numero_identificacion=cp.proveedor)  where t.institucion="+institucion;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codTransInterfaz
	 * @param institucion
	 * @return
	 */
	public static int obtenerTipoTransaccionInterfaz(Connection con, int codTransInterfaz, int institucion) 
	{
		String cadena="SELECT consecutivo from tipos_trans_inventarios where codigo_interfaz='"+codTransInterfaz+"' and institucion="+institucion;
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("---->"+cadena);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param tipoTransaccion
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreTipoTransaccion(Connection con, int tipoTransaccion, int institucion) 
	{
		String cadena="SELECT descripcion from tipos_trans_inventarios where consecutivo=? and institucion=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,tipoTransaccion);
            ps.setInt(2,institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "";
	}

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo) 
	{
		String cadena="SELECT val_uni_compra+val_uni_iva as valor from convenio_proveedor where proveedor ='"+proveedor+"' and codigo_axioma="+articulo;
        PreparedStatementDecorator ps;
        double resultado=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                resultado=rs.getDouble(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo) 
	{
		String cadena="SELECT val_uni_compra+val_uni_iva from catalogo_proveedor where fecha_ini_vigencia<current_date and proveedor ='"+proveedor+"' and codigo_axioma="+articulo;
        PreparedStatementDecorator ps;
        double resultado=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                resultado=rs.getDouble(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param valorUnitario
	 * @return
	 */
	public static boolean actualizarPrecioUltimaCompra(Connection con, int codigo, double valorUnitario) 
	{
        String cadena="UPDATE articulo SET precio_ultima_compra = ?  where codigo=?";
        
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDouble(1, valorUnitario);
            ps.setInt(2, codigo);
            ps.executeUpdate();
            
            Articulo articulo= new Articulo();
            articulo.modificarTarifas(con, valorUnitario, codigo, ConstantesIntegridadDominio.acronimoPrecioUltimaCompra);
            
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;

	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazArticulo(Connection con, int codigoArticulo) 
	{
		String cadena="SELECT codigo_interfaz from articulo where codigo=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoArticulo);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "";
	}

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorIvaArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo) 
	{
		String cadena="SELECT val_uni_iva from convenio_proveedor where proveedor ='"+proveedor.trim()+"' and codigo_axioma="+articulo;
		
		logger.info("\n\nCONSULTA VALOR IVA CON PROVEEDOR ->"+cadena+"<-\n");
		
		PreparedStatementDecorator ps;
        double resultado=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                resultado=rs.getDouble(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorIvaArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo) 
	{
		String cadena="SELECT val_uni_iva from catalogo_proveedor where fecha_ini_vigencia<current_date and proveedor ='"+proveedor.trim()+"' and codigo_axioma="+articulo;
        
		logger.info("\n\nCONSULTA VALOR IVA CATA PROVEEDOR ->"+cadena+"<-\n");
		
		PreparedStatementDecorator ps;
        double resultado=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                resultado=rs.getDouble(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param pedido
	 * @return
	 */
	public static int obtenerNumeroPeticionPedidoQX(Connection con, int pedido) 
	{
		String cadena="SELECT distinct peticion from pedidos_peticiones_qx where pedido="+pedido;
        PreparedStatementDecorator ps;
        int resultado=ConstantesBD.codigoNuncaValido;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                resultado=rs.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return resultado;
	}
	
	/**
	 * Método que verifica si a una solicitud de cargos directos artículos le afectan los inventarios
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean afectaInventariosSolicitud(Connection con,String numeroSolicitud)
	{
		boolean afectaInventarios = true;
		try
		{
			String consulta = "SELECT afecta_inventarios FROM cargos_directos WHERE numero_solicitud = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				afectaInventarios = UtilidadTexto.getBoolean(rs.getString("afecta_inventarios"));
		}
		catch(SQLException e)
		{
			logger.error("Error en afectaInventariosSolicitud: "+e);
		}
		return afectaInventarios;
	}
	

	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static HashMap obtenerClasesInventario(Connection con, int institucion) 
	{
		String consulta="SELECT codigo,nombre,cuenta_inventario as cuentainventario,cuenta_costo as centrocosto from clase_inventario where institucion=?  order by nombre";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
        PreparedStatementDecorator ps;
		try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mapa;
	}
	
	/**
	 * @author Víctor Gómez
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerClasesInventarioArray(Connection con, int institucion) 
	{
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String consulta="SELECT codigo,nombre,cuenta_inventario as cuentainventario,cuenta_costo as centrocosto from clase_inventario where institucion=?  order by nombre";
		try
        {
			PreparedStatementDecorator  ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
            while(rs.next())
            {
            	HashMap<String, Object> mapa = new HashMap<String, Object>();
            	mapa.put("codigo", rs.getObject("codigo"));
            	mapa.put("nombre", rs.getObject("nombre"));
            	mapa.put("cuenta_inventario", rs.getObject("cuentainventario"));
            	mapa.put("cuenta_costo", rs.getObject("centrocosto"));
            	list.add(mapa);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
	}

	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static HashMap obtenerGrupoInventario(Connection con, int institucion) 
	{
		String consulta="SELECT codigo,clase,nombre,cuenta_inventario as cuentainventario,cuenta_costo as centrocosto from grupo_inventario where institucion=?  order by nombre";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
        PreparedStatementDecorator ps;
		try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mapa;
	}
	
	
	public static boolean obtenerValidarTiempotratamiento(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		//Mt 4568 Se agrega validacion para el despacho de medicamentos entregados a pacientes
		//Se realiza mediante un union
		
		String cadena="SELECT * FROM "+
		                    "(SELECT " +
							"   jas.fecha_modifica as fecham," +
							"   jas.tiempo_tratamiento as tiempot " +
							" FROM " +
							"	justificacion_art_sol jas " +
							" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ " +
							" ON SJ.codigo_justificacion=jas.codigo " +
							"INNER JOIN solicitudes s " +
							"ON (s.numero_solicitud=sj.numero_solicitud) " +
							"INNER JOIN cuentas c ON (c.ID=s.cuenta) "+
							" INNER JOIN " +
							"	admin_medicamentos am ON (sj.numero_solicitud=am.numero_solicitud) " +
							" INNER JOIN " +
							"	detalle_admin da ON (da.administracion=am.codigo) " +
							" WHERE " +
									" jas.articulo="+codigoArticulo+" " +
									" and jas.dosis='"+dosis+"' " +
									" and jas.unidosis='"+unidosis+"' " +
									" and (jas.tipo_frecuencia='"+tipoFrecuencia+"' " +
									" OR ";
							if(tipoFrecuencia.trim().equals("Horas")){
								cadena+="jas.tipo_frecuencia = '1' ";
							}else{
								if(tipoFrecuencia.trim().equals("1")){
									cadena+="jas.tipo_frecuencia = 'Horas' ";
								}else{
									if(tipoFrecuencia.trim().equals("Minutos")){
										cadena+="jas.tipo_frecuencia = '2' ";
									}else{
										if(tipoFrecuencia.trim().equals("2")){
											cadena+="jas.tipo_frecuencia = 'Minutos' ";
										}else{
											if(tipoFrecuencia.trim().equals("Días")){
												cadena+="jas.tipo_frecuencia = '3' ";
											}else{
												if(tipoFrecuencia.trim().equals("3")){
													cadena+="jas.tipo_frecuencia = 'Días' ";
												}
											}
										}
									}
								}
							}
							
							cadena+=")"+
									" and jas.frecuencia='"+frecuencia+"' " +
									" and c.codigo_paciente="+paciente.getCodigoPersona()+" " +
									" and da.fecha>=to_date('"+UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)),-1,true)+"','YYYY-MM-DD') " +
							//Se agrega la unión
							"UNION "+
							"SELECT " +
							"   jas.fecha_modifica as fecham," +
							"   jas.tiempo_tratamiento as tiempot " +
							" FROM " +
							"	justificacion_art_sol jas " +
							" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ " +
							" ON (SJ.codigo_justificacion=jas.codigo) " +
							
							" INNER JOIN solicitudes_medicamentos sm " +
									" ON sm.numero_solicitud=sj.numero_solicitud " +
							
							" INNER JOIN " +
							        " solicitudes s ON (sm.numero_solicitud=s.numero_solicitud) "+
							
							" INNER JOIN " +
									
									" cuentas c ON (s.cuenta=c.id) " +
							" WHERE " +
									" jas.articulo="+codigoArticulo+" " +
									" and jas.dosis='"+dosis+"' " +
									" and jas.unidosis='"+unidosis+"' " +
									" and (jas.tipo_frecuencia='"+tipoFrecuencia+"' " +
									" OR ";
							if(tipoFrecuencia.trim().equals("Horas")){
							   cadena+="jas.tipo_frecuencia = '1' ";
							  }else{
									if(tipoFrecuencia.trim().equals("1")){
											cadena+="jas.tipo_frecuencia = 'Horas' ";
										}else{
											if(tipoFrecuencia.trim().equals("Minutos")){
												cadena+="jas.tipo_frecuencia = '2' ";
											}else{
												if(tipoFrecuencia.trim().equals("2")){
													cadena+="jas.tipo_frecuencia = 'Minutos' ";
												}else{
													if(tipoFrecuencia.trim().equals("Días")){
														cadena+="jas.tipo_frecuencia = '3' ";
													}else{
														if(tipoFrecuencia.trim().equals("3")){
															cadena+="jas.tipo_frecuencia = 'Días' ";
														}
													}
												}
											}
										}
									}   
							cadena+=")"+
									" and jas.frecuencia='"+frecuencia+"' " +
									" and c.codigo_paciente="+paciente.getCodigoPersona()+" " +
									" and s.estado_historia_clinica ='6') resultado"+
							" ORDER BY " +
							" fecham DESC ";
		if (System.getProperty("TIPOBD").equals("POSTGRESQL")) {
			cadena+=ValoresPorDefecto.getValorLimit1()+" 1 ";
		}
		if (System.getProperty("TIPOBD").equals("ORACLE")) {
			cadena="SELECT fecham, tiempot FROM ("+cadena+") rs where rownum <= 1 ";	
		}
			
		
		logger.info("consulta validacion 1 tiempo tratamiento"+cadena);
		
		if(Utilidades.convertirAEntero(dosis)<0 || dosis.equals("") || tipoFrecuencia.equals("") || frecuencia.equals(""))
		{
			logger.info("\nNO SE REALIZO LA VALIDACION DE TIEMPO DE TRATAMIENTO DEBIDO A QUE NO SE ENVIO LA DOSIS O LA FRECUENCIA.\n");
			return true;
		}
		else
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
			}
			catch (SQLException e)
			{
				logger.info("Error en la consulta validacion tiempo tratamiento>>>>>>>>>>>>>"+e);
			}
			logger.info("hay justificaciones >>>"+mapa);
			if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")>0)
			{
				logger.info("hay registros de justificacion");
				int dias=UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecham_0")+""), UtilidadFecha.getFechaActual());
				if(Utilidades.convertirAEntero(mapa.get("tiempot_0")+"")<dias)
				{
					logger.info("toca nueva justificacion");
					return true;
				}
				else{
					return false;
				}
			}
			
		}
		return true;
	}

//Obtener Ultima Justificacion
	public static int obtenerUltimaJustificacion(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		int resultado = ConstantesBD.codigoNuncaValido;
		
		String cadena="SELECT * FROM "+
                "(SELECT " +
				"   jas.codigo as codigo," +
				"   jas.fecha_modifica as fecham," +
				"   jas.tiempo_tratamiento as tiempot " +
				" FROM " +
				"	justificacion_art_sol jas " +
				" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ " +
				" ON SJ.codigo_justificacion=jas.codigo " +
				" INNER JOIN " +
				"	admin_medicamentos am ON (sj.numero_solicitud=am.numero_solicitud) " +
				" INNER JOIN " +
				"	detalle_admin da ON (da.administracion=am.codigo) " +
				"INNER JOIN solicitudes s " +
				"ON (s.numero_solicitud=SJ.numero_solicitud) " +
				"INNER JOIN cuentas c ON (c.ID=s.cuenta) "+
				" WHERE " +
						" jas.articulo="+codigoArticulo+" " +
						" and jas.unidosis='"+unidosis+"' " +
						" and jas.dosis='"+dosis+"' " +
						" and (jas.tipo_frecuencia='"+tipoFrecuencia+"' " +
						" OR ";
				if(tipoFrecuencia.trim().equals("Horas")){
					cadena+="jas.tipo_frecuencia = '1' ";
				}else{
					if(tipoFrecuencia.trim().equals("1")){
						cadena+="jas.tipo_frecuencia = 'Horas' ";
					}else{
						if(tipoFrecuencia.trim().equals("Minutos")){
							cadena+="jas.tipo_frecuencia = '2' ";
						}else{
							if(tipoFrecuencia.trim().equals("2")){
								cadena+="jas.tipo_frecuencia = 'Minutos' ";
							}else{
								if(tipoFrecuencia.trim().equals("Días")){
									cadena+="jas.tipo_frecuencia = '3' ";
								}else{
									if(tipoFrecuencia.trim().equals("3")){
										cadena+="jas.tipo_frecuencia = 'Días' ";
									}
								}
							}
						}
					}
				}
				
				cadena+=")"+
						" and jas.frecuencia='"+frecuencia+"' " +
						" and c.codigo_paciente="+paciente.getCodigoPersona()+" " +
						" and da.fecha>=to_date('"+UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)),-1,true)+"','YYYY-MM-DD') " +
				//Se agrega la unión
				"UNION "+
				"SELECT " +
				"   jas.codigo as codigo," +
				"   jas.fecha_modifica as fecham," +
				"   jas.tiempo_tratamiento as tiempot " +
				" FROM " +
				"	justificacion_art_sol jas " +
				" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ " +
				" ON (SJ.codigo_justificacion=jas.codigo) " +
				
				" INNER JOIN solicitudes_medicamentos sm " +
						" ON sm.numero_solicitud=sj.numero_solicitud " +
				
				" INNER JOIN " +
				        " solicitudes s ON (sm.numero_solicitud=s.numero_solicitud) "+
				
				" INNER JOIN " +
						
						" cuentas c ON (s.cuenta=c.id) " +
				" WHERE " +
						" jas.articulo="+codigoArticulo+" " +
						" and jas.unidosis='"+unidosis+"' " +
						" and jas.dosis='"+dosis+"' " +
						" and (jas.tipo_frecuencia='"+tipoFrecuencia+"' " +
						" OR ";
				if(tipoFrecuencia.trim().equals("Horas")){
				   cadena+="jas.tipo_frecuencia = '1' ";
				  }else{
						if(tipoFrecuencia.trim().equals("1")){
								cadena+="jas.tipo_frecuencia = 'Horas' ";
							}else{
								if(tipoFrecuencia.trim().equals("Minutos")){
									cadena+="jas.tipo_frecuencia = '2' ";
								}else{
									if(tipoFrecuencia.trim().equals("2")){
										cadena+="jas.tipo_frecuencia = 'Minutos' ";
									}else{
										if(tipoFrecuencia.trim().equals("Días")){
											cadena+="jas.tipo_frecuencia = '3' ";
										}else{
											if(tipoFrecuencia.trim().equals("3")){
												cadena+="jas.tipo_frecuencia = 'Días' ";
											}
										}
									}
								}
							}
						}   
				cadena+=")"+
						" and jas.frecuencia='"+frecuencia+"' " +
						" and c.codigo_paciente="+paciente.getCodigoPersona()+" " +
						" and s.estado_historia_clinica ='6') resultado"+
				" ORDER BY " +
				" fecham DESC ";
                if (System.getProperty("TIPOBD").equals("POSTGRESQL")) {
                     cadena+=ValoresPorDefecto.getValorLimit1()+" 1 ";
                }
                if (System.getProperty("TIPOBD").equals("ORACLE")) {
                     cadena="SELECT fecham, tiempot,codigo FROM ("+cadena+") rs where rownum <= 1 ";	
                }
		
		
		
		//until here
		logger.info("consulta validacion 1 tiempo tratamiento"+cadena);
		
		if(Utilidades.convertirAEntero(dosis)<0 || dosis.equals("") || tipoFrecuencia.equals("") || frecuencia.equals(""))
		{
			logger.info("\nNO SE REALIZO LA VALIDACION DE TIEMPO DE TRATAMIENTO DEBIDO A QUE NO SE ENVIO LA DOSIS O LA FRECUENCIA.\n");
			return resultado;
		}
		else
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				
				if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")>0)
				{
					logger.info("hay registros de justificacion");
					int dias=UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecham_0")+""), UtilidadFecha.getFechaActual());
					if(Utilidades.convertirAEntero(mapa.get("tiempot_0")+"")<dias)
					{
						return resultado;
					}
					else{
						resultado=Integer.parseInt(mapa.get("codigo_0").toString());
						logger.info("numero justificacion: "+resultado);
						return resultado;
					}
				}
				
	        }
			catch (SQLException e)
			{
				logger.info("Error en la consulta validacion tiempo tratamiento>>>>>>>>>>>>>"+e);
			}
			
		}
		return resultado;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorCompraMasAlta(Connection con, int articulo) 
	{
		String cadena="SELECT precio_compra_mas_alta as valor from articulo where codigo="+articulo;
        PreparedStatementDecorator ps;
        double resultado=0;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                resultado=rs.getDouble(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param valorUnitario
	 * @return
	 */
	public static boolean actualizarPrecioCompraMasAlta(Connection con, int codigo, double valorUnitario) 
	{
		String cadena="UPDATE articulo SET precio_compra_mas_alta = ?  where codigo=?";
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDouble(1, valorUnitario);
            ps.setInt(2, codigo);
            ps.executeUpdate();
            
            Articulo articulo= new Articulo();
            articulo.modificarTarifas(con, valorUnitario, codigo, ConstantesIntegridadDominio.acronimoPrecioCompraMasAlta);
            
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
	}

	public static int existeAlmacenPlanEspecial(Connection con, int codigoCentroAtencion) 
	{
		String cadena="SELECT count(plan_especial) as almacenes from almacen_parametros where centro_atencion=? and plan_especial=?";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCentroAtencion);
			ps.setString(2, ConstantesBD.acronimoSi);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("almacenes");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/***********************************************************************
	 * Metodo encargado de consultar las clases de inventario
	 * pudiendo filtrar por los siguiente criterios:
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion -->  Requerido
	 * -- codigo	--> Opcional
	 * -- cuentaInventario	--> Opcional
	 * -- centroCosto	--> Opcional
	 * @return ArrayList<HashMap>
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo,nombre,cuentaInventario,
	 * cuentaCosto
	 **************************************************************************/
	public static ArrayList<HashMap<String, Object>> obtenerClaseInventario (Connection connection,HashMap criterios)
	{
		logger.info("\n entre a  obtenerClaseInventario -->"+criterios);
		ArrayList<HashMap<String, Object>> listado = new ArrayList<HashMap<String,Object>>();
		
		String cadena="SELECT codigo As codigo, nombre As nombre, cuenta_inventario As cuentainv, cuenta_costo As cuentcost FROM  clase_inventario   WHERE institucion=?";
		String  order=" order by nombre";
		//codigo
		if (UtilidadCadena.noEsVacio(criterios.get("codigo")+"") && Utilidades.convertirAEntero(criterios.get("codigo")+"")>0)
			cadena+=" AND codigo="+criterios.get("codigo");
		//cuenta inventario
		if (UtilidadCadena.noEsVacio(criterios.get("cuentaInventario")+"") && Utilidades.convertirAEntero(criterios.get("cuentaInventario")+"")>0)
			cadena+=" AND cuenta_inventario="+criterios.get("cuentaInventario");
		//centro costo
		if (UtilidadCadena.noEsVacio(criterios.get("cuentaCosto")+"") && Utilidades.convertirAEntero(criterios.get("cuentaCosto")+"")>0)
			cadena+=" AND centro_costo="+criterios.get("cuentaCosto");
		
		cadena+=order;
		logger.info("\n cadena -->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap dato = new HashMap ();
				
				dato.put("codigo", rs.getObject("codigo"));
				dato.put("nombre", rs.getObject("nombre"));
				dato.put("cuentaInventario", rs.getObject("cuentainv"));
				dato.put("cuentaCosto", rs.getObject("cuentcost"));
				
				listado.add(dato);
			}
		}
		catch (SQLException e) 
		{
			logger.info("\n problema en  obtenerClaseInventario "+e);
		}
		
		return listado;
	}

	/**
	 * Método que retorna el código axioma de un artículo
	 * según el código interfaz
	 * @param con
	 * @param codigoInterfaz
	 * @return
	 */
	public static String obtenerCodigoDadoCodigoInterfaz(Connection con, String codigoInterfaz)
	{
		String cadena = "SELECT codigo FROM articulo WHERE codigo_interfaz = ?";
        PreparedStatementDecorator ps;
        try
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, codigoInterfaz);
            ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "";
	}
	
	/**
	 * Método para obtener las clases de inventario que aplican para un centro de costo
	 * según el tipo de transacción
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerClasesInventarioTransValidasCentroCosto(Connection con,HashMap campos)
	{
		ArrayList<InfoDatosInt> resultados = new ArrayList<InfoDatosInt>();
		try
		{
			//**************SE TOMAN LOS PARÁMETROS************************************
			int codigoCentroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
			int codigoTipoTransaccion = Utilidades.convertirAEntero(campos.get("codigoTipoTransaccion").toString());
			//*************************************************************************
			
			String consulta = "SELECT ci.codigo , ci.nombre " +
				"from trans_validas_x_cc_inven tv " +
				"inner join clase_inventario ci ON(ci.codigo = tv.clase_inventario) " +
				"WHERE tv.centros_costo = ? and tv.tipos_trans_inventario = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCentroCosto);
			pst.setInt(2,codigoTipoTransaccion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				InfoDatosInt elemento = new InfoDatosInt();
				elemento.setCodigo(rs.getInt("codigo"));
				elemento.setNombre(rs.getString("nombre"));
				
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.info("Error en obtenerClasesInventarioTransValidasCentroCosto: "+e);
		}
		
		return resultados;
	}

	/**
	 * Método que devuelve la Naturaleza de un 
	 * artículo determinado
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static boolean esPosArticulo(Connection con, int articulo, int institucion)
	{
		String cadena = "SELECT " +
							"na.es_pos AS esPos " +
						"FROM " +
							"articulo a " +
							"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo AND a.institucion = na.institucion) " +
						"WHERE " +
							"a.codigo = "+articulo+" " +
							"AND a.institucion = "+institucion+" ";
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	return rs.getBoolean("esPos");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
	}

	/**
	 * Metodo que devuelve el numero del consecutivo de una transaccion segun el codigo
	 * @param con
	 * @param string
	 * @return
	 */
	public static String obtenerConsecutivoTransaccion(Connection con, String codigoTransaccion) {
		String cadena = "SELECT consecutivo FROM transacciones_x_almacen WHERE codigo='"+codigoTransaccion+"'";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("consecutivo");
		}
		catch (SQLException e)
		{
			logger.info("ERROR obtenerConsecutivoTransaccion / "+e);
		}
		return "";
	}
	
	/**
	 * Método para obtener las farmacias x centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerFarmaciasXCentroCosto(Connection con,HashMap<String, Object> campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//***************SE TOMAN LOS PARÁMETROS**********************************************
			int codigoCentroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
			int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			//*************************************************************************************
			
			String consulta = "SELECT " +
				"dfc.codigo_farmacia_cc as codigo_farmacia," +
				"getnomcentrocosto(dfc.codigo_farmacia_cc) as nombre_farmacia " +
				"from farmacia_x_centro_costo fxc " +
				"inner join det_farmacia_cc dfc on(dfc.farmacia = fxc.codigo) " +
				"WHERE " +
				"fxc.centro_costo = "+codigoCentroCosto+" and " +
				"fxc.centro_atencion = "+codigoCentroAtencion+" and " +
				"fxc.institucion = "+codigoInstitucion+" " +
				"ORDER BY nombre_farmacia";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_farmacia"));
				elemento.put("nombre", rs.getObject("nombre_farmacia"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFarmaciasXCentroCosto: "+e);
		}
		return resultados;
	}

	/**
	 * Método que indica si un articulo es o no medicamento
	 * @param con
	 * @param codigoArticulo
	 * @param codigoInstitucionInt
	 */
	public static boolean esMedicamento(Connection con, int codigoArticulo, int codigoInstitucion) {
		boolean esMedicamento=true;
		String sql = "SELECT " +
							"na.es_medicamento " +
						"FROM " +
							"articulo a " +
						"INNER JOIN " +
							"naturaleza_articulo na ON (na.acronimo=a.naturaleza AND na.institucion=a.institucion) " +
						"WHERE " +
							"a.codigo=" + codigoArticulo+" " +
							"AND a.institucion=" + codigoInstitucion;
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(!UtilidadTexto.getBoolean(rs.getString("es_medicamento")))
					esMedicamento=false;
			
		} catch (SQLException e) {
			logger.error("Error en esMedicamento: "+e);
		}
		return esMedicamento;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @param esCantidadASumar
	 * @param cantidadArticulo
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static boolean actualizarExistenciasArticuloAlmacenLoteAnulacion(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, 
			int cantidadArticulo, String lote, String fechaVencimiento,String numeroSolciitud,String loteDetalle,HashMap<String,Object> detalleSolicitud,Integer index)
	{
        String cadena="";
        
        //primero se evalua que el articulo tenga una entrada en 'articulos_almacen' de lo se inserta de una
        logger.info("lote--- "+lote);
        logger.info("fecha vencimiento - -- "+fechaVencimiento);
        ArrayList filtro=new ArrayList();
        filtro.add(codArticulo+"");
        filtro.add(codAlmacen+"");
        if(!UtilidadTexto.isEmpty(lote))
        	filtro.add(lote.trim()+"");
        if(!UtilidadTexto.isEmpty(fechaVencimiento))
        	filtro.add((fechaVencimiento.trim().equals("")||fechaVencimiento.trim().equalsIgnoreCase("null"))?null:fechaVencimiento);
        if(!UtilidadTexto.isEmpty(lote))
        	UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacenLotes,filtro);
        else
        	UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);
          
        List<String> loteConsultado =new LinkedList<String>();
      
        
        
            cadena="UPDATE articulo_almacen_x_lote SET existencias = (existencias ";
            if(esCantidadASumar)
                cadena+="+";
            else
                cadena+="-";
            cadena+=cantidadArticulo+") ";
            cadena+=" where articulo=? and almacen=?  ";
            
            
            if(loteDetalle!=null && !loteDetalle.trim().equals("")){
            	cadena+=" and lote = ?  ";
            }
            
            
//            if(UtilidadTexto.isEmpty(lote.trim()))
//            {
//            	cadena+=" and lote is null ";
//            }
//            else
//            {
//            	cadena+=" and lote = ? ";
//            }
//            if(UtilidadTexto.isEmpty(fechaVencimiento.trim()))
//            {
//            	cadena+=" and fecha_vencimiento is null ";
//            }
//            else
//            {
//            	cadena+=" and fecha_vencimiento = ? ";
//            }
       
        PreparedStatementDecorator ps;
        try
        {
        	
        	
        	
        	if(   detalleSolicitud.get("flagTipoDespacho_"+index)==null ||   UtilidadTexto.isEmpty(String.valueOf(detalleSolicitud.get("flagTipoDespacho_"+index)))){
        		PreparedStatement ps1= con.prepareStatement(" select orden from  despacho d JOIN detalle_despachos dd ON (d.orden  = dd.despacho ) where d.numero_solicitud=? ");
        		ps1.setInt(1, Integer.valueOf(String.valueOf( numeroSolciitud  )));
        		ResultSet rs1 = ps1.executeQuery();
        		
        		
        		List<Integer> ordenAModificar=new LinkedList<Integer>();
        		while(rs1.next()){
        			ordenAModificar.add(rs1.getInt("orden"));
        		}     		
        		
    			PreparedStatement ps2 = con.prepareStatement("update detalle_despachos dd set cantidad=cantidad-? where (dd.despacho = ? and dd.articulo = ?)");
    			ps2.setInt(1, cantidadArticulo);
    			ps2.setInt(2, ordenAModificar.get(index));
    			ps2.setInt(3, codArticulo);
    		    ps2.executeUpdate();
    			
    		    ps2.close();
    		    
    		    rs1.close();
        		ps1.close();
        	} 	
        	
        	logger.info("actualizarExistenciasArticuloAlmacenLote->"+cadena+" art->"+codArticulo+" almacen->"+codAlmacen);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codArticulo);
            ps.setInt(2, codAlmacen);
            if(loteDetalle!=null && !loteDetalle.trim().equals(""))
            {
            	ps.setString(3,loteDetalle);
            }
//            if(!UtilidadTexto.isEmpty(fechaVencimiento.trim()))
//        	{
//            	logger.info("fechaV->"+fechaVencimiento);
//            	ps.setDate(4, Date.valueOf(fechaVencimiento));
//            }
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
	}

	public static  List<String> consultarLoteXAlmacenXArticulo(Connection con,Integer numeroSolicitud,Integer codigoArticulo) {
		String consulta = "select lote  from despacho d join detalle_despachos dd on (d.orden = dd.despacho ) where d. numero_solicitud =  ?  and dd.articulo = ? ";
		List<String> listaLotes = new LinkedList<String>();

		PreparedStatement pst;
		try {
			pst = con.prepareStatement(consulta);

			pst.setInt(1, numeroSolicitud);
			pst.setInt(2, codigoArticulo);
	

			ResultSet rs = pst.executeQuery();

			while(rs.next()){
				 listaLotes.add(rs.getString("lote"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("erro consultando el lote a actualzair "+e.getMessage());
		}
		return listaLotes;




	}
	
 }