/*
 * @(#)SqlBaseBusquedaServiciosGenericaDao.java
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
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares para la Busqueda de Servicios
 *
 *@author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 *  @version 1.0, Oct 31, 2005
 */
public class SqlBaseBusquedaServiciosGenericaDao 
{
    /**
    * Objeto para manejar los logs de esta clase
    */
    private static Logger logger = Logger.getLogger(SqlBaseBusquedaServiciosGenericaDao.class);
    
    /**
     * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups
     * @param con
     * @param codigo
     * @param descripcionServicio
     * @param codigoSexo
     * @param codigosServiciosInsertados
     * @param filtrarTipoServicio
     * @param codigoContrato
     * @param tipoTarifario
     * @param filtrarNopos
     * @param codigoAxioma
     * @param codigoFormulario
     * @return
     */
    public static  ResultSetDecorator busquedaAvanzadaServiciosXCodigos( Connection con,
                                                                String codigo,
                                                                String descripcionServicio,
                                                                int codigoSexo,
                                                                String codigosServiciosInsertados,
                                                                String filtrarTipoServicio,
																int codigoContrato,
																String tipoTarifario,
																boolean filtrarNopos,
	    														String codigoAxioma,
	    														int codigoFormulario,
	    														boolean atencionOdontologica,
	    														String tipoAtencion,
	    														String nombreForma,
	    														String codigoPrograma,
	    														String filtrarNaturalezaServicio
															  ) 
    {
        ResultSetDecorator respuesta=null;
        String consultaArmada="";
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
                    logger.warn("No se pudo realizar la conexión "+e.toString());
                    respuesta= null;
                }
            }
            consultaArmada=armarConsulta(codigo, descripcionServicio, codigoSexo, codigosServiciosInsertados, filtrarTipoServicio, codigoContrato, tipoTarifario, filtrarNopos, codigoAxioma, codigoFormulario, atencionOdontologica, tipoAtencion, nombreForma, codigoPrograma, filtrarNaturalezaServicio);
            Log4JManager.info("consulta Armada : " + consultaArmada);
            
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con,consultaArmada);
            respuesta=new ResultSetDecorator(ps.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn("Error en la búsqueda avanzada de los servicios " +e.toString());
            respuesta=null;
        }
        return respuesta;
    }
    
    
    
    
    
    
    
    
    /**
     * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups
     * @param con
     * @param codigo
     * @param descripcionServicio
     * @param codigoSexo
     * @param codigosServiciosInsertados
     * @param filtrarTipoServicio
     * @param codigoContrato
     * @param tipoTarifario
     * @param filtrarNopos
     * @param codigoAxioma
     * @param codigoFormulario
     * @return
     */
    public static  ResultSetDecorator busquedaAvanzadaServiciosXDescripcion( Connection con,
                                                                String codigo,
                                                                String descripcionServicio,
                                                                int codigoSexo,
                                                                String codigosServiciosInsertados,
                                                                String filtrarTipoServicio,
																int codigoContrato,
																String tipoTarifario,
																boolean filtrarNopos,
	    														String codigoAxioma,
	    														int codigoFormulario,
	    														boolean atencionOdontologica,
	    														String tipoAtencion,
	    														String nombreForma,
	    														String codigoPrograma,
	    														String filtraNaturalezaServicio
															  ) 
    {
        ResultSetDecorator respuesta=null;
        String consultaArmada="";
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
                    logger.warn("No se pudo realizar la conexión "+e.toString());
                    respuesta= null;
                }
            }
            consultaArmada=armarConsulta(codigo, descripcionServicio, codigoSexo, codigosServiciosInsertados, filtrarTipoServicio, codigoContrato, tipoTarifario, filtrarNopos, codigoAxioma, codigoFormulario, atencionOdontologica, tipoAtencion, nombreForma, codigoPrograma,filtraNaturalezaServicio);
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con,consultaArmada);
            respuesta=new ResultSetDecorator(ps.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn("Error en la búsqueda avanzada de los servicios " +e.toString());
            respuesta=null;
        }
        return respuesta;
    }
    
    /**
	 * Metodo que arma la consulta de los servicios
	 * @param codigoAxioma
	 * @param codigoPropietarioCups
	 * @param codigoPropietarioIss
	 * @param codigoPropietarioSoat
	 * @param descripcionCups
	 * @param codigoSexo
	 * @param codigosServiciosInsertados
	 * @param esquemaTarifario
	 * @param convenio
	 * @param filtrarTipoServicio
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoFormulario 
	 * @param filtrarTipoLiquidacion
	 * @param liquidarServicio
	 * @return
	 * @throws SQLException
	 */
	private static String armarConsulta  (      String codigo,
	                                            String descripcionServicio,
	                                            int codigoSexo,
	                                            String codigosServiciosInsertados, 
												String filtrarTipoServicio,
												int codigoContrato,
												String tipoTarifario,
												boolean filtrarNopos,
												String codigoAxioma,
												int codigoFormulario,
												boolean atencionOdontologica,
												String tipoAtencion,
												String nombreForma,
												String codigoPrograma,
												String filtraNaturalezaServicio
	                                      ) throws SQLException
	{
	    String consulta=         "SELECT distinct s.codigo AS codigoAxioma, " +
	    								"s.atencion_odontologica AS atencionodontologica, " +
	    								"s.grupo_servicio AS grupoServicio, " +
	                                    "CASE WHEN (SELECT rs.codigo_propietario FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+tipoTarifario+" ) IS NULL THEN (select nombre||' NO PARAMETRIZADO'  from tarifarios_oficiales where codigo="+tipoTarifario+") " +
	                                    "ELSE (SELECT rs.codigo_propietario ||' '||getnombretarfioofi(rs.tipo_tarifario) FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+tipoTarifario+" )  END  AS codigoPropietario, " +
	                                    "CASE WHEN (SELECT rs.descripcion FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+tipoTarifario+") IS NULL THEN (select nombre||' NO PARAMETRIZADO'  from tarifarios_oficiales where codigo="+tipoTarifario+") " +
	                                    "ELSE (SELECT rs.descripcion FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+tipoTarifario+") END AS descripcion, " +
	                                    "s.especialidad AS codigoEspecialidad," +
	                                    "s.tipo_servicio AS tiposervicio, " +
	                                    "e.nombre AS nombreEspecialidad, " +
	                                    "s.espos AS booleanEsPos, " +
	                                    "'' AS justificar, " +
	                                    "'' AS cobertura, " +
	                                    "'' AS subcuenta," +
	                                    "s.minutos_duracion AS minutosDuracion, " +
	                                    "CASE WHEN s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS esPos, "                                    ;
	
	    if(codigoContrato>0)
	    {
	    	consulta+=" getEsNivelServContratado("+codigoContrato+", s.codigo) AS esNivelServicioContratado, ";
	    }
	    else
	    {
	    	consulta+=" 'true' AS esNivelServicioContratado, ";
	    }
	    
	    
	
	    if(codigosServiciosInsertados !=null && !codigosServiciosInsertados.equals(""))
	    {
	        consulta+=" CASE WHEN s.codigo IN ("+codigosServiciosInsertados+" "+ConstantesBD.codigoNuncaValido+") THEN 'true' ELSE 'false' END AS yaFueSeleccionado ";
	    }
	    else
	    {
	        consulta+=" 'false' AS yaFueSeleccionado ";
	    }
	    
	    
	    
	    consulta+=  " FROM servicios s "+
			        " INNER JOIN referencias_servicio refs ON (refs.servicio=s.codigo) " +
				    " INNER JOIN especialidades e ON (e.codigo= s.especialidad) ";
	    
	
	    if(!UtilidadTexto.isEmpty(codigoPrograma)){
	    	
	    	consulta+= " inner join odontologia.detalle_programas dp on(dp.servicio=s.codigo) " +
	    			" inner join odontologia.programas p on (p.codigo=dp.programas)";
	    }
	    
	   	
	    consulta+=   " WHERE " +
				     " s.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
	    
	   if(atencionOdontologica)
	      consulta+=" AND s.atencion_odontologica = '"+ConstantesBD.acronimoSi+"' "; 	
	    
	   if(filtrarNopos)
		   consulta+=" AND s.espos=false ";
	    
	   //-----Se filtra la consulta por los tipos de servicios enviados por parámetro
	   if(filtrarTipoServicio!=null && !filtrarTipoServicio.trim().equals(""))
	   {
	       	String[] tiposServicios=filtrarTipoServicio.split("-");
	       	consulta+=" AND s.tipo_servicio IN (";
	       	for(int i=0; i<tiposServicios.length; i++)
	       	{
	   			switch(tiposServicios[i].charAt(0))
				{      			
	       			case ConstantesBD.codigoServicioQuirurgico:
		       					consulta+="'"+ConstantesBD.codigoServicioQuirurgico+"'";
		       					break;
	       			case ConstantesBD.codigoServicioPartosCesarea:
		       					consulta+="'"+ConstantesBD.codigoServicioPartosCesarea+"'";
		       					break;
	       			case ConstantesBD.codigoServicioNoCruentos:
		       					consulta+="'"+ConstantesBD.codigoServicioNoCruentos+"'";
		       					break;
	       			case ConstantesBD.codigoServicioPaquetes:
		       					consulta+="'"+ConstantesBD.codigoServicioPaquetes+"'";
		       					break;	       			
	       			case ConstantesBD.codigoServicioAnestesiaCirugia:
			   					consulta+="'"+ConstantesBD.codigoServicioAnestesiaCirugia+"'";
			   					break;
	       			case ConstantesBD.codigoServicioAntiguoInterconsulta:
			   					consulta+="'"+ConstantesBD.codigoServicioAntiguoInterconsulta+"'";
			   					break;
	       			case ConstantesBD.codigoServicioAyudantesCirugia:
			   					consulta+="'"+ConstantesBD.codigoServicioAyudantesCirugia+"'";
			   					break;
	       			case ConstantesBD.codigoServicioCamaEstancias:
			   					consulta+="'"+ConstantesBD.codigoServicioCamaEstancias+"'";
			   					break;
	       			case ConstantesBD.codigoServicioCargosConsultaExterna:
			   					consulta+="'"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
			   					break;
	       			case ConstantesBD.codigoServicioHonorariosCirugia:
			   					consulta+="'"+ConstantesBD.codigoServicioHonorariosCirugia+"'";
			   					break;
	       			case ConstantesBD.codigoServicioMaterialesCirugia:
			   					consulta+="'"+ConstantesBD.codigoServicioMaterialesCirugia+"'";
			   					break;
	       			case ConstantesBD.codigoServicioNoDefinido:
			   					consulta+="'"+ConstantesBD.codigoServicioNoDefinido+"'";
			   					break;
	       			case ConstantesBD.codigoServicioSalaCirugia:
			   					consulta+="'"+ConstantesBD.codigoServicioSalaCirugia+"'";
			   					break;
	       			case ConstantesBD.codigoServicioProcedimiento:
	   							consulta+="'"+ConstantesBD.codigoServicioProcedimiento+"'";
	   							break;
	       			case ConstantesBD.codigoServicioDyT:
	   							consulta+="'"+ConstantesBD.codigoServicioDyT+"'";
	   							break;
	       			case ConstantesBD.codigoServicioOtros:
	   							consulta+="'"+ConstantesBD.codigoServicioOtros+"'";
	   							break;
					
	   			default :
				{
					logger.warn(" [ERROR] El tipo de servicio que se desea filtrar no existe "+ "\n\n" );
					return null;
				}
	   			
				
				}//switch
	       		
	       		if(i<tiposServicios.length-1)
	       			consulta+=",";
	       	}//for
	   	consulta+=") ";
	   }
	
	 //-----Se filtra la consulta por las naturalezas de servicios enviados por parámetro
	   if(filtraNaturalezaServicio!=null && !filtraNaturalezaServicio.trim().equals(""))
	   {
	       	String[] naturalezaServicios=filtraNaturalezaServicio.split("-");
	       	consulta+=" AND s.naturaleza_servicio IN (";
	       	for(int i=0; i<naturalezaServicios.length; i++)
	       	{
	   			if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioConsultas))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioConsultas+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioDiagnostico))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioDiagnostico+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioTerapeuticoQx+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioPromocionPrevencion))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioPromocionPrevencion+"'";		
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioEstancias))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioEstancias+"'";		
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioHonorarios))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioHonorarios+"'";			
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioDerechosSala))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioDerechosSala+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioMaterialesInsumos))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioMaterialesInsumos+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioBancoSangre))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioBancoSangre+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioTrasladoPaciente))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"'";
	   			else if(naturalezaServicios[i].equals(ConstantesBD.codigoNaturalezaServicioNoAplica))
	   				consulta+="'"+ConstantesBD.codigoNaturalezaServicioNoAplica+"'";
	   			else {
	   				logger.warn(" [ERROR] La naturaleza de servicio que se desea filtrar no existe "+ "\n\n" );
					return null;
	   			}
	   			if(i<naturalezaServicios.length-1)
	       			consulta+=",";
	       	}//for
	   	consulta+=") ";
	   }
	   
	   //se verifica si se va a validar por sexo
	   if(codigoSexo>0)
		   consulta+=" AND (s.sexo="+codigoSexo+" OR s.sexo IS NULL) ";
	    
	   if(!UtilidadTexto.isEmpty(codigo))
	   {
		   consulta += " AND (refs.tipo_tarifario= "+tipoTarifario+" AND refs.codigo_propietario= '"+codigo+"' ) ";
	   }   
	
	   if(!UtilidadTexto.isEmpty(codigoAxioma))
	   {
	       consulta+=" AND refs.servicio = "+codigoAxioma+" ";
	       //consulta = consulta + "AND UPPER(e.razon_social) LIKE  UPPER('%"+razonSocial+"%')  ";
	   }
	
	   if(!UtilidadTexto.isEmpty(descripcionServicio))
	   {
		   consulta +=" AND (refs.tipo_tarifario= "+tipoTarifario+" AND UPPER(refs.descripcion) LIKE UPPER('%"+descripcionServicio+"%') )";
	   }
	
	   //Filtro por un formulario de respuesta específico
	   if(codigoFormulario>0)
		   consulta += " AND s.codigo IN (SELECT servicio from historiaclinica.form_resp_serv where plantilla = "+codigoFormulario+") ";
	
	   if(!UtilidadTexto.isEmpty(nombreForma) &&
			   nombreForma.equals("formaUnidadConsulta"))
	   {
		   if(tipoAtencion!=null)
		   {
	    	   if(tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
	    	   {
	    		   consulta += "AND s.atencion_odontologica='"+ConstantesBD.acronimoSi+"'";
	    	   }
	    	   else if(tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
	    	   {
	    		   consulta += "AND s.atencion_odontologica='"+ConstantesBD.acronimoNo+"'";
	    	   }
		   }
	   }
	
	   if(!UtilidadTexto.isEmpty(codigoPrograma)){
		   
		   consulta+=" AND p.codigo="+codigoPrograma;
	   }
	
	   logger.info("\n\n\nCONSULTA GENERICA===>"+consulta+"\n\n\n");
	    
	   consulta+=" ORDER BY s.codigo ";
	   return consulta;
	}
    
}