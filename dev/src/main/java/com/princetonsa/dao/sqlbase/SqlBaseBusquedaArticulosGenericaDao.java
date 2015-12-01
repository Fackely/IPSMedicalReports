/*
 * @(#)SqlBaseBusquedaArticulosGenericaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa la funcionalidad comn a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estndar. Mtodos particulares para la Busqueda de Articulos
 *
 *@author <a href="mailto:wilson@PrincetonSA.com">Wilson Ros</a>
 *  @version 1.0, Dic 14, 2005
 */
public class SqlBaseBusquedaArticulosGenericaDao 
{

    /**
     * Objeto para manejar los logs de esta clase
     */
     private static Logger logger = Logger.getLogger(SqlBaseBusquedaArticulosGenericaDao.class);
     
     /**
      * Metodo que realiza la busqueda avanzada de los articulos x codigos o descripcion
      * @param con
      * @param esBusquedaPorNombre
      * @param criterioBusqueda
      * @param codigosArticulosInsertados
      * @param filtrarXInventarios
      * @param codigoAlmacen
      * @param codigoTransaccion
      * @param codigoInstitucion
      * @param parejasClaseGrupo
     * @param valorDefectoClasesInventario 
      * @param codigoEsquemaTarifario
      * @return
      */
     public static  ResultSetDecorator busquedaAvanzadaArticulos(    Connection con,
                                                                                         boolean esBusquedaPorNombre,
                                                                                         String criterioBusqueda,
                                                                                         String codigosArticulosInsertados,
                                                                                         boolean filtrarXInventarios,
                                                                                         int codigoContrato,
                                                                                         String codigoAlmacen,
                                                                                         String codigoTransaccion, 
                                                                                         int codigoInstitucion,
																						 String parejasClaseGrupo,
																						 String esMedicamento,
																						 
																						 String esPos,
																						 
																						 String tipoConsignac,
																						 boolean filtrarXSeccionSubseccion,
										                                                 String codigosSecciones, //codigos secciones separadas por comas
										                                                 String codigoSubseccion, //codigo de la subseccion
										                                                 String codigoSeccion, //codigo de la seccion
										                                                 boolean filtrarXClaseGrupoSub,
										                                                 String codigoClase, //codigo Clase
										                                                 String codigoGrupo, //codigo del grupo
										                                                 String codigoSubgrupo, //codigo del subgrupo
										                                                 boolean filtrarXPrepPen,
										                                                 boolean filtrarXAjusteInvFis,
										                                                 String tipoDispositivo,String tipoAccesoVascular, 
										                                                 boolean soloAlmacen,
										                                                 int categoria,
										                                                 String formaFarmaceutica,
										                                                 String unidadMedida,
										                                                 boolean valorDefectoClasesInventario,	                                                
										                                                 String idTercero,
										                                                 String esMedicamentoControlEspecial,
										                                                 boolean cargarEquivalentes,
										                                                 boolean aplicaCargosDirectos,
										                                                 boolean atencionOdontologica
										                                                 
                                                                                    ) 
     {
         ResultSetDecorator respuesta=null;
         String consultaArmada="", consultaArmadaEquivalentes="";
         try
         {
             if(con==null || con.isClosed())
             {
                 try
                 {
                     DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                     con = myFactory.getConnection();
                 }
                 catch(SQLException e)
                 {
                     logger.warn("No se pudo realizar la conexin "+e.toString());
                     respuesta= null;
                 }
             }
             //consultaArmada=armarConsulta( esBusquedaPorNombre, criterioBusqueda, codigosArticulosInsertados, filtrarXInventarios, codigoAlmacen, codigoTransaccion, codigoInstitucion, parejasClaseGrupo, codigoEsquemaTarifario, esMedicamento, tipoConsignac, filtrarXSeccionSubseccion, codigosSecciones, codigoSubseccion, codigoSeccion, filtrarXClaseGrupoSub, codigoClase, codigoGrupo, codigoSubgrupo );
             consultaArmada=armarConsulta(con, esBusquedaPorNombre, criterioBusqueda, codigosArticulosInsertados, filtrarXInventarios,codigoContrato, codigoAlmacen, codigoTransaccion, codigoInstitucion, parejasClaseGrupo, esMedicamento, esPos, tipoConsignac, filtrarXSeccionSubseccion, codigosSecciones, codigoSubseccion, codigoSeccion, filtrarXClaseGrupoSub, codigoClase, codigoGrupo, codigoSubgrupo, filtrarXPrepPen, filtrarXAjusteInvFis, tipoDispositivo, tipoAccesoVascular, soloAlmacen, categoria, formaFarmaceutica, unidadMedida, valorDefectoClasesInventario, idTercero, esMedicamentoControlEspecial, false, aplicaCargosDirectos, atencionOdontologica);
             
             if(cargarEquivalentes){
            	 consultaArmadaEquivalentes= armarConsulta(con, esBusquedaPorNombre, criterioBusqueda, codigosArticulosInsertados, filtrarXInventarios,codigoContrato, codigoAlmacen, codigoTransaccion, codigoInstitucion, parejasClaseGrupo, esMedicamento, esPos, tipoConsignac, filtrarXSeccionSubseccion, codigosSecciones, codigoSubseccion, codigoSeccion, filtrarXClaseGrupoSub, codigoClase, codigoGrupo, codigoSubgrupo, filtrarXPrepPen, filtrarXAjusteInvFis, tipoDispositivo, tipoAccesoVascular, soloAlmacen, categoria, formaFarmaceutica, unidadMedida, valorDefectoClasesInventario, idTercero, esMedicamentoControlEspecial, true, aplicaCargosDirectos, atencionOdontologica);
            	 consultaArmada= "SELECT * FROM (("+consultaArmada+") UNION ("+consultaArmadaEquivalentes+")) tabla ORDER BY tabla.codigo"; 
             }
             else
            	 consultaArmada += " ORDER BY a.codigo";
             
             PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
             
             logger.info("\nbusquedaAvanzadaArticulos / "+consultaArmada);
             
             respuesta=new ResultSetDecorator(ps.executeQuery());     
             
         }
         catch(SQLException e)
         {
             logger.warn("Error en la bsqueda avanzada de los articulos " +e.toString());
             respuesta=null;
         }
         return respuesta;
     }
     
     /**
      * Metodo que retorna la consulta armada
     * @param con 
      * @param esBusquedaPorNombre
      * @param criterioBusqueda
      * @param codigosArticulosInsertados
      * @param filtrarXInventarios
      * @param codigoAlmacen
      * @param codigo de la transaccion 
      * @param codigoInstitucion
      * @param parejasClaseGrupo
     * @param valorDefectoClasesInventario 
     * @param aplicaCargosDirectos 
      * @param codigoEsquemaTarifario
      * 
      * @return
      */
     private static String armarConsulta  (      Connection con,
    		 									 boolean esBusquedaPorNombre,
                                                 String criterioBusqueda,
                                                 String codigosArticulosInsertados,
                                                 boolean filtrarXInventarios,
                                                 int codigoContrato,
                                                 String codigoAlmacen,
                                                 String codigoTransaccion, 
                                                 int codigoInstitucion, 
                                                 String parejasClaseGrupo,
                                                 String esMedicamento,
                                                 String esPos,
                                                 String tipoConsignac,
                                                 boolean filtrarXSeccionSubseccion,
                                                 String codigosSecciones, //codigos secciones separadas por comas
                                                 String codigoSubseccion, //codigo de la subseccion
                                                 String codigoSeccion, //codigo de la seccion
                                                 boolean filtrarXClaseGrupoSub,
                                                 String codigoClase, //codigo Clase
                                                 String codigoGrupo, //codigo del grupo
                                                 String codigoSubgrupo, //codigo del subgrupo
                                                 boolean filtrarXPrepPen,
                                                 boolean filtrarXAjusteInvFis,
                                                 String tipoDispositivo,
                                                 String tipoAccesoVascular, 
                                                 boolean soloAlmacen,
                                                 int categoria,
                                                 String formaFarmaceutica,
                                                 String unidadMedida, 
                                                 boolean valorDefectoClasesInventario,
                                                 String idTercero,
                                                 String esMedicamentoControlEspecial,
                                                 boolean cargarEquivalentes,
                                                 boolean aplicaCargosDirectos,
                                                 boolean atencionOdontologica
                                           )
     {
    	 String codigosClasesInventarios = "";
    	 String aliasTablaArticulo=(!cargarEquivalentes)?"a":"a1";
    	 
    	 String consulta = obtenerColumnas(codigosArticulosInsertados,
				filtrarXInventarios, codigoAlmacen, codigoInstitucion,
				tipoConsignac, filtrarXAjusteInvFis, idTercero, aliasTablaArticulo);
         
         consulta+=" FROM subgrupo_inventario si, naturaleza_articulo na ";
         
         //si tiene filtro de inventarios o se quiere filtrar por parejas de clase grupo, se debe adicionar la tabla de trans_validas
         if((filtrarXInventarios && !aplicaCargosDirectos) || (!parejasClaseGrupo.equals("")&&!parejasClaseGrupo.trim().equals("-")&&!parejasClaseGrupo.trim().equals("'-'")) )
             consulta+=" , trans_validas_x_cc_inven tv ";
         
         if(filtrarXSeccionSubseccion)
         {
        	 if (!soloAlmacen)
        		 consulta+= " , det_articulos_por_almacen dapa, articulos_por_almacen apa ";
        	 else
        		 consulta+=" , articulos_almacen aa";
         }
         
         if(filtrarXClaseGrupoSub || aplicaCargosDirectos)
        	 consulta+= " , clase_inventario ci, grupo_inventario gi ";
         
         if(filtrarXPrepPen)
        	 consulta+=", preparacion_toma_inventario pti";
         
         if(!UtilidadTexto.isEmpty(tipoDispositivo))
        	 consulta+=", art_tipos_dispositivo atd ";
         
         if(!UtilidadTexto.isEmpty(tipoAccesoVascular))
        	 consulta+=", art_tipos_acceso_vas atv ";
         
         
         consulta+=", articulo a ";
         
         if (!esMedicamentoControlEspecial.isEmpty())
        	 consulta+=" INNER JOIN categoria_articulos ca ON (ca.codigo=a.categoria) ";
         
         if(cargarEquivalentes)
         {
        	 consulta+=" INNER JOIN inventarios.equivalentes_inventario ei ON(ei.articulo_ppal=a.codigo) " +
        	 			" INNER JOIN articulo a1 ON(a1.codigo=ei.articulo_equivalente) ";
         }
         
         
         consulta+=" WHERE a.subgrupo = si.codigo and na.acronimo=a.naturaleza and na.institucion=a.institucion and a.estado = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
         
         if(aplicaCargosDirectos)
        	 consulta+= "AND gi.codigo=si.grupo " +
        	 			"AND gi.clase=si.clase " +
        	 			"AND gi.aplica_cargos_directos='"+ConstantesBD.acronimoSi+"' ";
       
       //Agregado por Anexo 951
         if (atencionOdontologica)
        	 consulta+=" AND a.atencion_odontologica='"+ConstantesBD.acronimoSi+"'";
         
         logger.info("===>Valor por Defecto del Cdigo de Bsqueda de Artculos: "+ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion));
         if(esBusquedaPorNombre)
             consulta+=" AND UPPER(a.descripcion) LIKE UPPER('%" +criterioBusqueda+ "%') ";
         //Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Cdigo Manual para Bsqueda de Artculos (Axioma, Interfaz)
         else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
         {
             logger.info("===>Se realiza el filtro por el Cdigo Axioma");
        	 consulta+=" AND a.codigo = "+criterioBusqueda;
         }
         else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
         {
        	 logger.info("===>Se realiza el filtro por el Cdigo Interfaz");
        	 consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"'";
         }
         
         
         if(filtrarXInventarios && !aplicaCargosDirectos)
             consulta+=  " AND tv.centros_costo="+codigoAlmacen+" " +
			            //CAMBIO SHAIO 728
			            //"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
			            " AND tv.clase_inventario=si.clase " +	
                        " AND tv.tipos_trans_inventario= " +codigoTransaccion+
                        " AND tv.institucion="+codigoInstitucion;
         
         
         logger.info("LLEGA parejasClaseGrupo >>>>> "+parejasClaseGrupo);
         
         if(!parejasClaseGrupo.equals("")&&!parejasClaseGrupo.trim().equals("-")&&!parejasClaseGrupo.trim().equals("'-'")){
        	consulta += " AND (tv.grupo_inventario=si.grupo) " +
        				" AND tv.clase_inventario ||'-'|| tv.grupo_inventario IN("+parejasClaseGrupo+") ";
         }	
         
         if(!UtilidadTexto.isEmpty(esMedicamento))
         {
        	if(UtilidadTexto.getBoolean(esMedicamento))
        		consulta+=" AND na.es_medicamento = '"+ConstantesBD.acronimoSi+"'";
        	else 
        		consulta+=" AND na.es_medicamento = '"+ConstantesBD.acronimoNo+"'";
        	
         }
         
         //
         
         if(!UtilidadTexto.isEmpty(esPos))
         {
        	if(UtilidadTexto.getBoolean(esPos))
        	{
        		////DEBE UTILIZARCE f - t TANTO EN ORACLE COMO EN POSTGRES PUES AS ESTA EN ODIGO CHAR 1
        		/*if(ValoresPorDefecto.getValorTrueParaConsultas().equals("1")){
        			consulta+=" AND na.es_pos = "+ValoresPorDefecto.getValorTrueParaConsultas();
        		} else {
        			consulta+=" AND na.es_pos = '"+ConstantesBD.acronimoSi+"'";
        		}*/
        		consulta+=" AND na.es_pos = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
        	}	
        	else
        	{	
        		////DEBE UTILIZARCE f - t TANTO EN ORACLE COMO EN POSTGRES PUES AS ESTA EN ODIGO CHAR 1
        		/*if(ValoresPorDefecto.getValorTrueParaConsultas().equals("1")){
        			consulta+=" AND na.es_pos = "+ValoresPorDefecto.getValorFalseParaConsultas();
        		} else {
        			consulta+=" AND na.es_pos = '"+ConstantesBD.acronimoNo+"'";
        		}*/
        		consulta+=" AND na.es_pos = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' ";
        	}	
         }
         
         //
         if(filtrarXSeccionSubseccion)
         {
        	 
        	 if (!soloAlmacen)
        	 {
	        	 consulta+= " AND a.codigo = dapa.articulo AND dapa.codigo_art_por_almacen = apa.codigo_pk AND apa.almacen="+codigoAlmacen+" ";
	        	 
	        	 if(!codigoSubseccion.equals("") && !codigoSubseccion.equals(ConstantesBD.codigoNuncaValido))
	        		 consulta += " AND apa.subseccion = "+codigoSubseccion;
	        	 else if (!codigoSeccion.equals("") && !codigoSeccion.equals(ConstantesBD.codigoNuncaValido))
	        	 	consulta += " AND apa.seccion = "+codigoSeccion+" ";
	        	 
	        	 if(!codigosSecciones.equals(""))
	        		 consulta += " AND apa.seccion IN ("+codigosSecciones+") ";
        	 }
        	 else
        	 {
        		 consulta+=" AND a.codigo=aa.articulo AND aa.almacen="+codigoAlmacen;
        	 }
        	 
         }
         if(filtrarXClaseGrupoSub)
         {
        	 if(UtilidadCadena.noEsVacio(codigoAlmacen))
        	 {
	        	consulta+= " AND ci.codigo = gi.clase AND gi.codigo=si.grupo ";
	        	if(UtilidadCadena.noEsVacio(codigoClase))
	        	{
	        		consulta += " AND ci.codigo = "+codigoClase;
	        	}
	        	if(UtilidadCadena.noEsVacio(codigoGrupo))
	        	{
	        		consulta += " AND gi.codigo = "+codigoGrupo;
	        	}
	        	if(UtilidadCadena.noEsVacio(codigoSubgrupo))
	        	{
	        		consulta += " AND si.codigo = "+codigoSubgrupo;
	        	}
        	 }
        	 else
        	 {
        		consulta+= " AND a.subgrupo = si.codigo " ;
	        	if(UtilidadCadena.noEsVacio(codigoClase))
	        	{
	        		consulta += " AND ci.codigo = "+codigoClase;
	        	}
	        	if(UtilidadCadena.noEsVacio(codigoGrupo))
	        	{
	        		consulta += " AND si.grupo = "+codigoGrupo;
	        	}
	        	if(UtilidadCadena.noEsVacio(codigoSubgrupo))
	        	{
	        		consulta += " AND si.subgrupo = "+codigoSubgrupo;
	        	}
        	 }
         }
         if(filtrarXPrepPen)
         {
        	 consulta+= " AND pti.articulo = a.codigo AND pti.estado='PEN'";
         }
         
         if(!UtilidadTexto.isEmpty(tipoDispositivo))
        	consulta+=" AND atd.articulo=a.codigo AND atd.tipo_dispositivo="+tipoDispositivo+" AND atd.institucion="+codigoInstitucion+" "; 
         
         if(!UtilidadTexto.isEmpty(tipoAccesoVascular))
         	consulta+=" AND atv.articulo=a.codigo AND atv.tipo_acceso_vascular="+tipoAccesoVascular+" AND atv.institucion="+codigoInstitucion+" ";
         
         if(categoria!=0)
        	 consulta+=" AND a.categoria="+categoria+" ";
         logger.info("paso por aqui D ");
         if(!UtilidadTexto.isEmpty(formaFarmaceutica))
        	 consulta+=" AND a.forma_farmaceutica='"+formaFarmaceutica+"' ";
         
         if(!UtilidadTexto.isEmpty(unidadMedida))
        	 consulta+=" AND a.unidad_medida='"+unidadMedida+"' ";
         
         if (!esMedicamentoControlEspecial.isEmpty())
         {
        	 if(esMedicamentoControlEspecial.equals("S")){
        		 consulta+=" AND ca.control_especial='"+ConstantesBD.acronimoSi+"'";
        	 } 
           //else if (esMedicamentoControlEspecial.equals("N"))
          //	 consulta+=" AND ca.control_especial='"+ConstantesBD.acronimoNo+"'";
         }
         
         logger.info("===>Valor Defecto: "+valorDefectoClasesInventario);
         if(valorDefectoClasesInventario)
         {
        	 codigosClasesInventarios = ValoresPorDefecto.codigosClasesValorPorDefecto(con);
        	 logger.info("===>Codigo Clases Inventarios: "+codigosClasesInventarios);
        	 if(UtilidadCadena.noEsVacio(codigosClasesInventarios))
        	 	 consulta += "AND ci.codigo IN ("+codigosClasesInventarios+") ";
         }	 
         
         //consulta+=" ORDER BY a.codigo ";
          
         logger.info("-----------CONSULTA GENERICA DE ARTICULO--------->> "+consulta);
         logger.info("- "+ValoresPorDefecto.getValorTrueCortoParaConsultas());
         logger.info("- "+ValoresPorDefecto.getValorTrueParaConsultas());
         
         logger.info("TIPO CONSIGNACION-->> "+tipoConsignac);
         return consulta;
     }

	private static String obtenerColumnas(String codigosArticulosInsertados,
			boolean filtrarXInventarios, String codigoAlmacen,
			int codigoInstitucion, String tipoConsignac,
			boolean filtrarXAjusteInvFis, String idTercero, String aliasTablaArticulo) {
		String consulta=       " SELECT DISTINCT " +
                                        " "+aliasTablaArticulo+".codigo AS codigo, " +
                                        " coalesce("+aliasTablaArticulo+".codigo_interfaz,'') as codigointerfaz," +
                                        " getdescarticulosincodigo("+aliasTablaArticulo+".codigo) AS descripcion, " +
                                        " getNomUnidadMedida("+aliasTablaArticulo+".unidad_medida) AS unidadMedida, " +
                                        " a.naturaleza AS codNaturaleza, " +
                                        " si.clase AS codClaseInventario, " +
                                        " si.grupo AS codGrupo, " +
                                        " a.subgrupo AS codSubGrupo, " +
                                        " a.descripcion AS descripcionArticulo, " +
                                        " CASE WHEN na.es_pos='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN 'POS' WHEN na.es_pos = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN 'NOPOS' ELSE '' END  AS esPos, " +
                                        " -1 AS valorUnitario, " +
                                        " "+aliasTablaArticulo+".costo_promedio as costopromedio, ";// +
                                        //" ca.control_especial as controlEspecial, ";
         
         if(tipoConsignac.equals(ConstantesBD.acronimoSi))
        	 consulta+=" CASE WHEN (SELECT count(1) FROM convenio_proveedor cp WHERE "+aliasTablaArticulo+".codigo=cp.codigo_axioma AND cp.proveedor=(select ter.numero_identificacion FROM terceros ter where ter.codigo="+idTercero+" and ter.institucion="+codigoInstitucion+" "+ValoresPorDefecto.getValorLimit1()+" 1)     )>0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' end as mostrarshaio, ";
         else
        	 consulta+=" '"+ConstantesBD.acronimoSi+"' as mostrarshaio, ";
         if(codigosArticulosInsertados !=null && !codigosArticulosInsertados.equals(""))
             consulta+=" CASE WHEN "+aliasTablaArticulo+".codigo IN ("+codigosArticulosInsertados+" "+ConstantesBD.codigoNuncaValido+") THEN 'true' ELSE 'false' END AS yaFueSeleccionado, ";
         else
             consulta+=" 'false' AS yaFueSeleccionado, ";

         if(filtrarXAjusteInvFis)
        	 consulta+=" getPosibilidadDeAjuste("+aliasTablaArticulo+".codigo, "+ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(codigoInstitucion)+") AS puedoAjustarInvFisico, ";
         else
        	 consulta+=" '' as  puedoAjustarInvFisico, ";
         
         if(!UtilidadTexto.isEmpty(idTercero) && Utilidades.convertirAEntero(idTercero)>0)
        	 consulta+=" coalesce(    (select (coalesce(cop.val_uni_compra,0)+coalesce(cop.val_uni_iva,0)) from convenio_proveedor cop where cop.codigo_axioma="+aliasTablaArticulo+".codigo and cop.proveedor=(select ter.numero_identificacion FROM terceros ter where ter.codigo="+idTercero+" and ter.institucion="+codigoInstitucion+" "+ValoresPorDefecto.getValorLimit1()+" 1))  ,0)  as valorprovedor,  ";
         else
        	 consulta+=" 0 as valorprovedor, ";
         
         if(filtrarXInventarios)
             consulta+=" getTotalExisArticulosXAlmacen("+codigoAlmacen+", "+aliasTablaArticulo+".codigo, "+codigoInstitucion+") AS existenciaXAlmacen ";
         else
             consulta+=" 'no_aplica' AS existenciaXAlmacen ";
		return consulta;
	}
     
}