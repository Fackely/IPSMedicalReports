/*
 * Creado el 4/01/2006
 * Jorge Armando Osorio
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.inventarios.FarmaciaCentroCostoAction;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseExistenciasInventariosDao
{
	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(FarmaciaCentroCostoAction.class);
	
    /**
     * Cadena que realiza la consulta de las existencias de articulos
     */
    private static String cadena1 =	"SELECT DISTINCT " +
    									"aa.articulo as codigo," +
    									"va.codigo_interfaz as codigoint," +
    									"getdescarticulosincodigo(va.codigo) as descripcion," +
    									"descripcionum as unidadmedida," +
    									"va.estado as estado," +
    									"getubicacionarticulo(va.codigo,aa.almacen) as ubicacion," +
    									"va.costopromedio as costo," +
    									"aa.existencias as existencias," +
    									"(va.costopromedio*aa.existencias) as valtotal," +
    									"aa.almacen AS almacen " +
    								"FROM " +
										"articulos_almacen aa " +
										"inner join view_articulos va on(aa.articulo=va.codigo) " ;
    
    /**
     * Cadena para realizar la consulta de un articulo en el caso de que no posea existencias
     */
    private static String cadena2="SELECT DISTINCT " +
    									"va.codigo as codigo," +
    									"va.codigo_interfaz as codigoint," +
    									"getdescarticulosincodigo(va.codigo) as descripcion," +
    									"va.descripcionum as unidadmedida," +
    									"va.estado as estado," +
    									"getubicacionarticulo(va.codigo,axa.almacen) as ubicacion," +
    									"va.costopromedio as costo," +
    									"aa.existencias as existencias," +
    									"(va.costopromedio*va.existencias) as valtotal," +
    									"axa.almacen AS almacen " +
    										"from view_articulos va " +
    										"inner join det_articulos_por_almacen daxa on (va.codigo=daxa.articulo) " +
    										"inner join articulos_por_almacen axa on (daxa.codigo_art_por_almacen=axa.codigo_pk) "+
    										"INNER JOIN articulos_almacen aa ON (aa.articulo=va.codigo AND aa.almacen=axa.almacen) ";
    
    
    
    private static String cadena3="SELECT DISTINCT " +
										"aa.articulo as codigo," +
										"va.codigo_interfaz as codigoint, "+
										"getdescarticulosincodigo(va.codigo) as descripcion," +
										"descripcionum as unidadmedida," +
										"va.estado as estado, "+
										"getubicacionarticulo(aa.articulo,aa.almacen) as ubicacion, "+
										"0 as costo," +
										"0 as existencias," +
										"0 as valtotal, " +
										"aa.almacen AS almacen " +
									"FROM " +
										"articulos_almacen aa " +
										"INNER JOIN view_articulos va ON (aa.articulo = va.codigo) ";
    
    private static String filtron="axa=?";
    
    
    /**
     * Filtro por almacen
     */
    private static String filtro1="where aa.almacen=? ";
    
    /**
     * Filtro por almacne y por clase del articulo
     */
    private static String filtro2="where aa.almacen=? and va.clase=? ";
    
    /**
     * Filtro por almacne y por clase y grupo del articulo
     */
    private static String filtro3="where aa.almacen=? and va.clase=? and va.grupo=? ";
    
    /**
     * Filtro por almacen y por clase, grupo y subgrupo del articulo
     */
    private static String filtro4="where aa.almacen=? and va.clase=? and va.grupo=? and va.subgrupo=? ";
    
    /**
     * Filtro por almacen y codigo del articulo.
     */
    private static String filtro5="where aa.almacen=? ";
    
    /**
     * Filtro por almacen y descripcion del articulo;
     */
    private static String filtro6="where aa.almacen=?  and upper(va.descripcion) like upper('%?%') ";
    
    /**
     * Filtro de define si se van a mostrar las existencias o no en la primera consulta
     */
    private static String existencias= "AND aa.existencias<>0 ";

    /**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen
	 * @param con
	 * @param codAlmacen
	 * @return
	 */
	public static HashMap consultarArticulosAlmacen(Connection con, int codAlmacen ,String mostrarExt, int institucion)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
        mapa.put("numRegistros","0");
        String cadenaSQL=ConsultasBirt.existenciasDeInventario2("", codAlmacen, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, mostrarExt, "", institucion);
        try
        {
        	logger.info("consultarArticulosAlmacenClaseGrupoSubgrupo "+cadenaSQL);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadenaSQL));
        	mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mapa;
		/*logger.info("\n entro a consultarArticulosAlmacen codigo almacen -->> "+codAlmacen);
		HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        String consulta1="("+cadena1+filtro1;
        String order1=" Order by aa.articulo )";
        String consulta2="("+cadena2+"  where axa.almacen=? ";
        String order2=" order by codigo )";
        String cadena=" UNION  ";
        logger.info("Aqui está mostrarExt =>"+mostrarExt);
        logger.info("Aqui se muestra lo de la utilidad =>" +UtilidadTexto.getBoolean(mostrarExt));
        
   
        if (UtilidadTexto.getBoolean(mostrarExt)==false)
        {
        	logger.info("\n ***NO se van a mostrar las existencias***");
        	consulta1+="AND va.existencias<>0 ";
        	consulta2+="AND va.existencias<>0 ";
        }
        else
        {
        	logger.info("\n ***SI se van a mostrar las existencias***");
        	
        }
        

        //cadena=consulta1+order1+" UNION "+consulta2+order2;
        cadena=consulta1+order1;
        
        logger.info("\n cadena que armé está aqui ***==>"+cadena);
        try
        {
            //PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro1+" Order by aa.articulo ) UNION ("+cadena2+"  where axa.almacen=? order by codigo )",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAlmacen);
            ps.setInt(2, codAlmacen);
            
            //logger.info("<<<< consultarArticulosAlmacenXdescripcion >>>>"+"("+cadena1+filtro1+" Order by aa.articulo ) UNION ("+cadena2+"  where axa.almacen=? order by codigo )");
            
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();*/
	}

	
	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen,clase,grupo,subgrupo
	 * En caso de que clase,grupo o subgrupo sean igual a 0 no los toma en el filtro de la consulta
	 * @param con
	 * @param codAlmacen
	 * @return
	 */
	public static HashMap consultarArticulosAlmacenClaseGrupoSubgrupo(Connection con, int codAlmacen, String clase, String grupo, String subgrupo,String mostrarExt, int institucion)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
        mapa.put("numRegistros","0");
        String cadenaSQL=ConsultasBirt.existenciasDeInventario2("claseGrupoSubgrupo", codAlmacen, Utilidades.convertirAEntero(clase), Utilidades.convertirAEntero(grupo), Utilidades.convertirAEntero(subgrupo), mostrarExt, "", institucion);
        try
        {
        	logger.info("consultarArticulosAlmacenClaseGrupoSubgrupo "+cadenaSQL);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadenaSQL));
        	mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mapa;
        
        /*
        if (UtilidadTexto.getBoolean(mostrarExt)==false)
        {
        
        	
        	try
            {
            	if(clase.equals("0"))
                {
            		ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro1+existencias+") " );
            				//"UNION ("+cadena2+" where axa.almacen=? AND va.existencias<>0)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                    ps.setInt(1, codAlmacen);
                    //ps.setInt(2, codAlmacen);
                    logger.info(">>>>>>>>>>>"+"("+cadena1+filtro1+existencias+") " );
                    		//"UNION ("+cadena2+" where axa.almacen="+codAlmacen+" AND va.existencias<>0)");
                    logger.info("===> codAlmacen="+codAlmacen);
                }
                else if(grupo.equals("0"))
                {
              		ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro2+existencias+") " );
              				//"UNION ("+cadena2+" where axa.almacen=? and va.clase=? AND va.existencias<>0)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
              		ps.setInt(1, codAlmacen);
                    ps.setInt(2,Utilidades.convertirAEntero(clase));
              		//ps.setInt(3, codAlmacen);
              		//ps.setInt(4,Utilidades.convertirAEntero(clase));
                    logger.info(">>>>>>>>>>>"+"("+cadena1+filtro2+existencias+") " );
                    		//"UNION ("+cadena2+" where axa.almacen="+codAlmacen+" and va.clase=? AND va.existencias<>0)");
                    logger.info("===> codAlmacen="+codAlmacen+", clase="+Utilidades.convertirAEntero(clase));
               	          	
                }
                else if(subgrupo.equals("0"))
                {
                	ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro3+existencias+") " );
                			//"UNION ("+cadena2+" where axa.almacen=? and va.clase=? and va.grupo=? AND va.existencias<>0)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                	ps.setInt(1, codAlmacen);
                    ps.setInt(2,Utilidades.convertirAEntero(clase));
                    ps.setInt(3,Utilidades.convertirAEntero(grupo));
              		//ps.setInt(4, codAlmacen);
              		//ps.setInt(5,Utilidades.convertirAEntero(clase));
              		//ps.setInt(6,Utilidades.convertirAEntero(grupo));
                    logger.info(">>>>>>>>>>>"+"("+cadena1+filtro3+existencias+") " );
                    		//"UNION ("+cadena2+" where axa.almacen="+codAlmacen+" and va.clase=? and va.grupo=? AND va.existencias<>0)");
                    logger.info("===> codAlmacen="+codAlmacen+", clase="+Utilidades.convertirAEntero(clase)+
                    		", grupo="+Utilidades.convertirAEntero(grupo));
               	             	
                }
                else
                {
                	
                	ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro4+existencias+") "  );
                			//"UNION ("+cadena2+" where axa.almacen=? and va.clase=? and va.grupo=? and va.subgrupo=? AND va.existencias<>0)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                	ps.setInt(1, codAlmacen);
                	ps.setInt(2,Utilidades.convertirAEntero(clase));
                    ps.setInt(3,Utilidades.convertirAEntero(grupo));
                    ps.setInt(4,Utilidades.convertirAEntero(subgrupo));
                    //ps.setInt(5, codAlmacen);
                    //ps.setInt(6,Utilidades.convertirAEntero(clase));
                    //ps.setInt(7,Utilidades.convertirAEntero(grupo));
                    //ps.setInt(8,Utilidades.convertirAEntero(subgrupo));
                    logger.info(">>>>>>>>>>>"+"("+cadena1+filtro4+existencias+") " );
                    		//"UNION ("+cadena2+" where axa.almacen="+codAlmacen+" and va.clase=? and va.grupo=? and va.subgrupo=? AND va.existencias<>0)");
                    logger.info("===> codAlmacen="+codAlmacen+", clase="+Utilidades.convertirAEntero(clase)+
                    		", grupo="+Utilidades.convertirAEntero(grupo)+", subgrupo="+Utilidades.convertirAEntero(subgrupo));
               	                	         	
                	
                }
                mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            return (HashMap)mapa.clone();
        }
        else
        {
        	logger.info("===> SI se van a mostrar las existencias ");
        	try
            {
            	if(clase.equals("0"))
                {
            		ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro1+") " );
            				//"UNION ("+cadena2+"  where axa.almacen=? )",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                    ps.setInt(1, codAlmacen);
                    //ps.setInt(2, codAlmacen);
                    logger.info(">>>>>>>>>>>"+"("+cadena1+filtro1+") " );
                    		//"UNION ("+cadena2+"  where axa.almacen="+codAlmacen+")");
                    logger.info("===> codAlmacen="+codAlmacen);
                }
                else if(grupo.equals("0"))
                {
              		ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro2+") " );
              				//"UNION ("+cadena2+" where axa.almacen=? and va.clase=? )",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
              		ps.setInt(1, codAlmacen);
                    ps.setInt(2,Utilidades.convertirAEntero(clase));
              		//ps.setInt(3, codAlmacen);
              		//ps.setInt(4,Utilidades.convertirAEntero(clase));
              		logger.info(">>>>>>>>>>>"+"("+cadena1+filtro2+") " );
              				//"UNION ("+cadena2+"  where axa.almacen="+codAlmacen+" and va.clase=? )");
                    logger.info("===> codAlmacen="+codAlmacen+", clase="+Utilidades.convertirAEntero(clase));
               	          	
                }
                else if(subgrupo.equals("0"))
                {
                	ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro3+") " );
                			//"UNION ("+cadena2+"  where axa.almacen=? and va.clase=? and va.grupo=? )",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                	ps.setInt(1, codAlmacen);
                    ps.setInt(2,Utilidades.convertirAEntero(clase));
                    ps.setInt(3,Utilidades.convertirAEntero(grupo));
              		//ps.setInt(4, codAlmacen);
              		//ps.setInt(5,Utilidades.convertirAEntero(clase));
              		//ps.setInt(6,Utilidades.convertirAEntero(grupo));
              		logger.info(">>>>>>>>>>>"+"("+cadena1+filtro3+") " );
              				//"UNION ("+cadena2+"  where axa.almacen="+codAlmacen+" and va.clase=? and va.grupo=? )");
                    logger.info("===> codAlmacen="+codAlmacen+", clase="+Utilidades.convertirAEntero(clase)+
                    		", grupo="+Utilidades.convertirAEntero(grupo));
               	             	
                }
                else
                {
                	ps= new PreparedStatementDecorator(con.prepareStatement("("+cadena1+filtro4+") " );
                			//"UNION ("+cadena2+"  where axa.almacen=? and va.clase=? and va.grupo=? and va.subgrupo=? )",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                	ps.setInt(1, codAlmacen);
                	ps.setInt(2,Utilidades.convertirAEntero(clase));
                    ps.setInt(3,Utilidades.convertirAEntero(grupo));
                    ps.setInt(4,Utilidades.convertirAEntero(subgrupo));
                    //ps.setInt(5, codAlmacen);
                    //ps.setInt(6,Utilidades.convertirAEntero(clase));
                    //ps.setInt(7,Utilidades.convertirAEntero(grupo));
                    //ps.setInt(8,Utilidades.convertirAEntero(subgrupo));
                    logger.info(">>>>>>>>>>>"+"("+cadena1+filtro4+") " );
                    		//"UNION ("+cadena2+"  where axa.almacen="+codAlmacen+" and va.clase=? and va.grupo=? and va.subgrupo=? )");
                    logger.info("===> codAlmacen="+codAlmacen+", clase="+Utilidades.convertirAEntero(clase)+
                    		", grupo="+Utilidades.convertirAEntero(grupo)+", subgrupo="+Utilidades.convertirAEntero(subgrupo));
                }
                mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            return (HashMap)mapa.clone();
        }*/
	}


	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y codigo del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */
	public static HashMap consultarArticulosAlmacenCodArticulo(Connection con, int codAlmacen, String codArticulo, String mostrarExt, int institucion)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
        mapa.put("numRegistros","0");
        String cadenaSQL=ConsultasBirt.existenciasDeInventario2("codigo", codAlmacen, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, mostrarExt, codArticulo, institucion);
        try
        {
        	logger.info("consultarArticulosAlmacenClaseGrupoSubgrupo "+cadenaSQL);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadenaSQL));
        	mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mapa;
		/*
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
        mapa.put("numRegistros","0");
       
        
        String filtro = filtro5;
        String busCodAxiomaInterfaz = "";
        
        //Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
        if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
        {
        	logger.info("===>Se realiza el filtro por el Código Axioma");
       	 	filtro += " AND va.codigo = "+codArticulo+" ";
       	 	busCodAxiomaInterfaz = "va.codigo = ?";
        }
        else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
        {
        	logger.info("===>Se realiza el filtro por el Código Interfaz");
        	filtro += " AND va.codigo_interfaz = '"+codArticulo+"'";
        	busCodAxiomaInterfaz = "va.codigo_interfaz = ?";
        }
        
        
        
        logger.info("cadena1 filtro 5 >> "+cadena1+""+filtro);
        
        try
        {
        	ps =  new PreparedStatementDecorator(con.prepareStatement(cadena1+filtro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAlmacen);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
                        
            if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")==0)
            {
                PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement("("+cadena3+" WHERE "+busCodAxiomaInterfaz+" AND aa.almacen=? ) ",
                		//"UNION ("+cadena2+" WHERE axa.almacen = ? AND "+busCodAxiomaInterfaz+"  )",
                		ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                
                if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
                	ps2.setInt(1, Utilidades.convertirAEntero(codArticulo));
                else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
                	ps2.setString(1, codArticulo);
                
                ps2.setInt(2, codAlmacen);
                ps2.setInt(3, codAlmacen);
                ps2.setInt(4, Utilidades.convertirAEntero(codArticulo));
                
                logger.info("("+cadena3+" WHERE "+busCodAxiomaInterfaz+" AND aa.almacen=? ) " );
                		//"UNION ("+cadena2+" WHERE axa.almacen = ? AND daxa.articulo = ? )"+codArticulo+"-"+codAlmacen);
                
                mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();*/
	}

	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y descripcion del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */
	public static HashMap consultarArticulosAlmacenDescArticulo(Connection con, int codAlmacen, String descArticulo, String mostrarExt, int institucion)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
        mapa.put("numRegistros","0");
        String cadenaSQL=ConsultasBirt.existenciasDeInventario2("descripcion", codAlmacen, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, mostrarExt, descArticulo, institucion);
        try
        {
        	logger.info("consultarArticulosAlmacenClaseGrupoSubgrupo "+cadenaSQL);
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadenaSQL));
        	mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mapa;
		/*String filtro6="where aa.almacen=?  and upper(va.descripcion) like upper('%"+descArticulo+"%') ";

		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
        mapa.put("numRegistros","0");
        try
        {
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadena1+filtro6,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codAlmacen);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")==0)
            {
                PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(cadena3+" where upper(descripcion) like upper('%"+descArticulo+"%')",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));                
                mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();*/
	}

}
