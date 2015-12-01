/*
 * Creado en Sep 21, 2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;
import java.sql.Types;

import com.princetonsa.dao.DaoFactory;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class SqlBaseHojaOftalmologicaDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseHojaOftalmologicaDao.class);
	
	/**
	 * Método para saber si existe o no una hoja oftalmológica para una paciente
	 * @param con -> conexion
	 * @param codigoPaciente
	 * @return codigoHojaOftal si existe sino retorna -1
	 */
	public static int existeHojaOftalmologica(Connection con, int codigoPaciente)
	{
		String consultarStr="SELECT codigo AS codigo from hoja_oftalmologica WHERE paciente = ? ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPaciente);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Vericando Existencia Hoja Oftalmológica : SqlBaseHojaOftalmologicaDao "+e.toString());
			return -1;
		}
	}

	/**
	 * Método para insertar una hoja oftalmológica
	 * @param con
	 * @param codigoPaciente
	 * @param observacionEstrabismo
	 * @param observacionSegmentoAnt
	 * @param observacionRetinaVitreo
	 * @param observacionOrbitaAnexos
	 * @return codigoHojaOftalmologica
	 */	
	public static int insertarHojaOftalmologica(Connection con, int codigoPaciente,
																		 String observacionEstrabismo, String observacionSegmentoAnt,
																		 String observacionRetinaVitreo, String observacionOrbitaAnexos)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigoHojaOftal=0;
		try
		{
			//Se verifica si existe ya la Hoja Oftalmológica
			codigoHojaOftal = existeHojaOftalmologica(con,codigoPaciente);
			
			if ( codigoHojaOftal != -1  )  
			{  
				 //Si existe se modifica
				String modificarStr = " UPDATE hoja_oftalmologica SET observ_estrabismo = ?, observ_segmento_ant = ?, observ_retina_vitreo = ?, observ_orbita_anexos = ? " +
								    								" WHERE codigo = ?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, observacionEstrabismo);
				ps.setString(2, observacionSegmentoAnt);					
				ps.setString(3, observacionRetinaVitreo);
				ps.setString(4, observacionOrbitaAnexos);
				ps.setInt(5, codigoHojaOftal);				
			}
			else
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				
				//Se obtiene el valor de la secuencia
				codigoHojaOftal=myFactory.incrementarValorSecuencia(con, "seq_hoja_oftalmologica");
				
				String insertarStr = " INSERT INTO hoja_oftalmologica " +
																													   "(codigo, " +
																													   "paciente, " +
																													   "observ_estrabismo, " +
																													   "observ_segmento_ant, " +
																													   "observ_retina_vitreo, " +
																													   "observ_orbita_anexos) " +
																													   " VALUES (?, ?, ?, ?, ?, ?) ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoHojaOftal);				
				ps.setInt(2, codigoPaciente);								
				ps.setString(3, observacionEstrabismo);
				ps.setString(4,observacionSegmentoAnt);					
				ps.setString(5,observacionRetinaVitreo);
				ps.setString(6,observacionOrbitaAnexos);
				
			}
		
			resp = ps.executeUpdate();
			
			if (resp > 0)
			{
				resp = codigoHojaOftal;
			}
					
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos de la Hoja Oftalmologica : SqlBaseHojaOftalmologicaDao "+e.toString() );
				resp = 0;
		}
		return resp;
	}
	
	/**
	 * Metodo para insertar el encabezado historico de la hoja oftalmológica  
	 * @param con
	 * @param codHojaOftalmologica
	 * @param datosMedico
	 * @param numeroSolicitud @todo
	 * @return codEncabezadoHisto
	 */ 
	public static int insertarEncabezadoHistoHojaOftalmologica(Connection con, int codHojaOftalmologica, String datosMedico, int numeroSolicitud)
	{	
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
	
		String insertarStr = 	"INSERT INTO enca_histo_hoja_ofta( codigo, " +
																												  "hoja_oftalmologica, " +
																												  "fecha, " +
																												  "hora, " +
																												  "datos_medico) " +
																												  " VALUES " +
																												  " (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?) ";

		try	{					
						DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						codigo=myFactory.incrementarValorSecuencia(con, "seq_enca_histo_hoja_ofta");
												
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codigo);
						ps.setInt(2, codHojaOftalmologica);
						ps.setString(3,datosMedico);
					
						resp = ps.executeUpdate();
						
						if(numeroSolicitud>0)
						{
							String relacionarConValoracionStr="INSERT INTO valoracion_hoja_oftal(encabezado_hoja, valoracion) VALUES(?,?)";
							ps =  new PreparedStatementDecorator(con.prepareStatement(relacionarConValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							ps.setInt(1, codigo);
							ps.setInt(2, numeroSolicitud);
							resp=ps.executeUpdate();
						}
						
						if (resp > 0)
						{
							resp = codigo;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos en el Encabezado Histórico de Hoja Oftalmologica : insertarEncabezadoOrdenMedica "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Metodo para insertar la sección estrabismo  
	 * @param con
	 * @param codEncabezadoHisto
	 * @param ppm
	 * @param coverTestCercaSc
	 * @param coverTestCercaCc
	 * @param coverTestLejosSc
	 * @param coverTestLejosCc
	 * @param coverTestOs
	 * @param ojoFijador
	 * @param ppcInstitucion
	 * @param prismaCcLejos
	 * @param prismaScLejos
	 * @param duccionesVersiones
	 * @param testVisionBinocular
	 * @param estereopsis
	 * @param amplitudFusionCercaMas
	 * @param amplitudFusionCercaMenos
	 * @param amplitudFusionLejosMas
	 * @param amplitudFusionLejosMenos
	 * @param prismaCompensadorLejos
	 * @param prismaCompensadorCerca
	 * @return codEncaEstrabismo
	 */
	public static int insertarEstrabismo (Connection con, int codEncabezadoHisto, String ppm, String coverTestCercaSc, String coverTestCercaCc, String coverTestLejosSc, String coverTestLejosCc, int ojoFijador, int ppcInstitucion, 
															 String prismaCcLejos, String prismaScLejos, String duccionesVersiones, String testVisionBinocular, String estereopsis, String amplitudFusionCercaMas, 
															 String amplitudFusionCercaMenos, String amplitudFusionLejosMas, String amplitudFusionLejosMenos, String prismaCompensadorLejos, String prismaCompensadorCerca)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO estrabismo	( codigo_historico, " +
																								   "ppm, " +
																								   "cover_test_cerca_sc, " +
																								   "cover_test_cerca_cc," +
																								   "cover_test_lejos_sc," +
																								   "cover_test_lejos_cc, " +
																								   "ojo_fijador," +
																								   "ppc_institucion," +
																								   "prisma_cc_lejos," +
																								   "prisma_sc_lejos," +
																								   "ducciones_versiones," +
																								   "vision_binocular," +
																								   "estereopsis," +
																								   "amp_fusion_cerca_mas," +
																								   "amp_fusion_cerca_menos," +
																								   "amp_fusion_lejos_mas," +
																								   "amp_fusion_lejos_menos," +
																								   "prisma_compe_lejos," +
																								   "prisma_compe_cerca) " +
																								   " VALUES " +
																								   " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		
		
		try	{																	
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codEncabezadoHisto);
						ps.setString(2,ppm);
						ps.setString(3,coverTestCercaSc);
						ps.setString(4,coverTestCercaCc);
						ps.setString(5,coverTestLejosSc);
						ps.setString(6,coverTestLejosCc);
						
						if(ojoFijador == -1)
						{
							ps.setString(7, null);
						}
						else
						{
							if(ojoFijador == 1)
								ps.setString(7, ValoresPorDefecto.getValorTrueParaConsultas());
							else
								ps.setString(7, ValoresPorDefecto.getValorFalseParaConsultas());
						}
								
						ps.setInt(8, ppcInstitucion);
						ps.setString(9,prismaCcLejos);
						ps.setString(10,prismaScLejos);
						ps.setString(11,duccionesVersiones);
						ps.setString(12,testVisionBinocular);
						ps.setString(13,estereopsis);
						
						
						if(UtilidadCadena.noEsVacio(amplitudFusionCercaMas))
							ps.setDouble(14,Double.parseDouble(amplitudFusionCercaMas));
						else
							ps.setNull(14,Types.DOUBLE);
						
						if(UtilidadCadena.noEsVacio(amplitudFusionCercaMenos))
							ps.setDouble(15,Double.parseDouble(amplitudFusionCercaMenos));
						else
							ps.setNull(15,Types.DOUBLE);
						
						if(UtilidadCadena.noEsVacio(amplitudFusionLejosMas))
							ps.setDouble(16,Double.parseDouble(amplitudFusionLejosMas));
						else
							ps.setNull(16,Types.DOUBLE);
						
						if(UtilidadCadena.noEsVacio(amplitudFusionLejosMenos))
							ps.setDouble(17,Double.parseDouble(amplitudFusionLejosMenos));
						else
							ps.setNull(17,Types.DOUBLE);
						
						/*ps.setString(14,amplitudFusionCercaMas);
						ps.setString(15,amplitudFusionCercaMenos);
						ps.setString(16,amplitudFusionLejosMas);
						ps.setString(17,amplitudFusionLejosMenos);*/
						ps.setString(18,prismaCompensadorLejos);
						ps.setString(19,prismaCompensadorCerca);
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = codEncabezadoHisto;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos en la sección Estrabismo : insertarEstrabismo "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Metodo para insertar el prisma cerca en la sección estrabismo  
	 * @param con
	 * @param encaHistoEstrabismo
	 * @param seccion
	 * @param correccion
	 * @param valorPrismaCerca
	 * @return encaHistoEstrabismo
	 */
	public static int insertarPrismaCerca (Connection con, int encaHistoEstrabismo, int seccion, boolean correccion, String valorPrismaCerca)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO prisma_cerca	( codigo_histo_estra, " +
																								  	  "seccion, " +
																									  "correccion, " +
																									  "valor) " +
																									  " VALUES " +
																									  " (?, ?, ?, ?) ";
		
		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, encaHistoEstrabismo);
						ps.setInt(2, seccion);
						ps.setBoolean(3, correccion);
						ps.setString(4, valorPrismaCerca);
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = encaHistoEstrabismo;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos del Prisma Cerca : insertarPrismaCerca "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Metodo para insertar el detalle del segmento anterior  
	 * @param con
	 * @param codHistoSegmentoAnt
	 * @param segmentoAntInst
	 * @param valorOd
	 * @param valorOs
	 * @return codHistoSegmentoAnt
	 */
	public static int insertarDetalleSegmentoAnt (Connection con, int codHistoSegmentoAnt, int segmentoAntInst, String valorOd, String valorOs)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO detalle_segmento_ant	( codigo_histo_seg, " +
																													  "segmento_ant_inst, " +
																													  "valor_od, " +
																													  "valor_os) " +
																													  " VALUES " +
																													  " (?, ?, ?, ?) ";
		
		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codHistoSegmentoAnt);
						ps.setInt(2, segmentoAntInst);
						ps.setString(3, valorOd);
						ps.setString(4, valorOs);
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = codHistoSegmentoAnt;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos del detalle del Segmento Anterior : insertarDetalleSegmentoAnt "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Metodo para insertar orbita y anexos  
	 * @param con
	 * @param codigoHistorico
	 * @param orbitaAnexoInst
	 * @param valorOd
	 * @param valorOs
	 * @return codHistoOrbitaAnexos
	 */
	public static int insertarOrbitaAnexos (Connection con, int codigoHistorico, int orbitaAnexoInst, String valorOd, String valorOs)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO orbita_anexos	( codigo_historico, " +
																								  	  "orbita_anexos_inst, " +
																									  "valor_od, " +
																									  "valor_os) " +
																									  " VALUES " +
																									  " (?, ?, ?, ?) ";
		
		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codigoHistorico);
						ps.setInt(2, orbitaAnexoInst);
						ps.setString(3, valorOd);
						ps.setString(4, valorOs);
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = codigoHistorico;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos de la orbita y anexo : insertarOrbitaAnexos "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Metodo para insertar el Segmento anterior  
	 * @param con
	 * @param codHistorico
	 * @return codHistoSegAnt
	 */
	public static int insertarSegmentoAnterior (Connection con, int codHistorico, String imagenOd, String imagenOs)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 
          "INSERT INTO segmento_anterior	( codigo_historico, " +
		  "imagen_od, " +
		  "imagen_os) " +
		  " VALUES " +
		  " (?, ?, ?) ";
		
		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codHistorico);
						ps.setString(2, imagenOd);
						ps.setString(3, imagenOs);
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = codHistorico;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos del Segmento Anterior : insertarSegmentoAnterior "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
    public static int insertarRetinaVitreo(Connection con, int codHistorico, String imagenRetinaOD, String imagenRetinaOS, String imagenVitreoOD, String imagenVitreoOS)
    {
        PreparedStatementDecorator ps;
        int resp=0;
    
        String insertarStr =
            "INSERT INTO retina_vitreo " +
            "(codigo_historico, " +
            "imagen_retina_od, " +
            "imagen_retina_os, " +
            "imagen_vitreo_od, " +
            "imagen_vitreo_os) " +
            "VALUES " +
            "(?, ?, ?, ?, ?)";
        try 
        {                   
            ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            ps.setInt(1, codHistorico);
            ps.setString(2, imagenRetinaOD);
            ps.setString(3, imagenRetinaOS);
            ps.setString(4, imagenVitreoOD);
            ps.setString(5, imagenVitreoOS);
            resp = ps.executeUpdate();
            if (resp > 0)
            {
                resp = codHistorico;
            }
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos de Retina Vitreo : insertarRetinaVitreo "+e.toString() );
            resp = 0;
        }
        return resp;
    }
    
	/**
	 * Método para consultar los tipos aplicables para la hoja oftalmológica según la institución
	 * @param con
	 * @param institucion
	 * @param tipo
	 * @return Collection
	 */
	public static Collection consultarTipoParametrizado (Connection con, int institucion, int nroConsulta)
	{
		String consultaStr="";

		//Seleccionar el tipo de informacion a consultar
	  
	    switch(nroConsulta)
		{
	    	//Tabla de parametrización de tipos ppc por institución
	        case 1:
	        	consultaStr="SELECT ppi.codigo AS codigo, tp.nombre AS nombre FROM tipo_ppc tp " +
	        							"INNER JOIN ppc_institucion ppi ON (ppi.tipo_ppc=tp.codigo) " +
	        							" WHERE ppi.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
										" AND ppi.institucion = ?";
	        	break;
	        
	        //Tabla de parametrización de segmento anterior por institución
			case 2:
				consultaStr="SELECT sai.codigo AS codigo,tsa.nombre AS nombre FROM tipo_segmento_anterior tsa " +
										" INNER JOIN segmento_ant_institucion sai ON (sai.segmento_anterior=tsa.codigo)" +
										" WHERE sai.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
										" AND sai.institucion = ?";
				break;
			
			//Tabla de parametrización de orbitas y anexos por institución
			case 3:
				consultaStr="SELECT oai.codigo AS codigo,toa.nombre AS nombre FROM tipo_orbita_anexos toa " +
										" INNER JOIN orbita_anexos_inst oai ON (oai.tipo_orbita_anexo=toa.codigo)" +
										" WHERE oai.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
										" AND oai.institucion = ?";
				break;
				
			default :
					{
						logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-3] El valor recibido es "+ nroConsulta + "\n\n" );
						return null;
					}
		}
	    
		try
		{
	     	//System.out.print("\n\n\n La sentencia SQL BASE (tipos)" + consultaStr + " \n\n\n");
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psConsulta.setInt(1, institucion);			
			
	 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado tabla de parametrización por institución ---: "+e.toString());
			return null;
		}
	}
	

	/**
	 * Método que consulta un histyórico especifico dado coimo parámentro el codigo de encabezado
	 * @param con
	 * @param nroConsulta
	 * @param codigoEncabezado
	 * @return
	 */
	public static Collection consultarHistoricoEspecifico(Connection con, int nroConsulta, int codigoEncabezado)
	{
		String consultaStr="";

		//Seleccionar el tipo de informacion a consultar
	  
	    switch(nroConsulta)
		{
	    	//Histórico de la sección Estrabismo
	        case 1:
	        	consultaStr="SELECT eho.codigo AS cod_historico, eho.fecha AS fecha_histo FROM enca_histo_hoja_ofta eho " +
	        							"INNER JOIN estrabismo es ON (eho.codigo=es.codigo_historico) " +
	        							"INNER JOIN hoja_oftalmologica ho ON (eho.hoja_oftalmologica=ho.codigo) " +
	        								"WHERE eho.codigo=? ORDER BY eho.codigo DESC";
	        	break;
	        
	        //Histórico de la sección Segmento Anterior
			case 2:
				consultaStr="SELECT eho.codigo AS cod_historico,eho.fecha AS fecha_histo,sai.codigo AS segmento_ant_ins," +
										 "dsa.valor_od AS valor_od,dsa.valor_os AS valor_os, eho.datos_medico AS datos_medico, sa.imagen_od as imagen_od, sa.imagen_os as imagen_os " +
										 "FROM enca_histo_hoja_ofta eho INNER JOIN segmento_anterior sa ON (eho.codigo=sa.codigo_historico) " +
										 "LEFT JOIN detalle_segmento_ant dsa ON (dsa.codigo_histo_seg=sa.codigo_historico) " +
										 "LEFT JOIN segmento_ant_institucion sai ON (dsa.segmento_ant_inst=sai.codigo)" +
										 " INNER JOIN hoja_oftalmologica ho ON (eho.hoja_oftalmologica=ho.codigo) " +
										 "WHERE eho.codigo=? ORDER BY eho.codigo DESC";
				break;
			
			//Histórico de la sección Retina y Vítreo
			case 3:
				consultaStr=
                    "select eho.codigo as cod_historico, eho.fecha as fecha_histo,eho.datos_medico as datos_medico, " +
                    "rv.imagen_retina_od as imagen_retina_od, rv.imagen_retina_os as imagen_retina_os, " +
                    "rv.imagen_vitreo_od as imagen_vitreo_od, rv.imagen_vitreo_os as imagen_vitreo_os " +
                    "from enca_histo_hoja_ofta eho " +
                    "inner join retina_vitreo rv on (eho.codigo=rv.codigo_historico) " +
                    "inner join hoja_oftalmologica ho on (eho.hoja_oftalmologica=ho.codigo) " +
                    "where eho.codigo=? order by eho.codigo desc";
				break;
			
			//Histórico de la sección Orbita y Anexos	
			case 4:
				consultaStr="SELECT eho.codigo AS cod_historico,eho.fecha AS fecha_histo,oai.codigo AS orbita_anexo_ins," +
										"oa.valor_od AS valor_od,oa.valor_os AS valor_os, eho.datos_medico AS datos_medico " +
										" FROM enca_histo_hoja_ofta eho " +
										"INNER JOIN orbita_anexos oa ON (eho.codigo=oa.codigo_historico) " +
										"INNER JOIN orbita_anexos_inst oai ON (oa.orbita_anexos_inst=oai.codigo) " +
										"INNER JOIN hoja_oftalmologica ho ON (eho.hoja_oftalmologica=ho.codigo) " +
											"WHERE eho.codigo=? ORDER BY eho.codigo DESC";
				break;
				
			default :
					{
						logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-4] El valor recibido es "+ nroConsulta + "\n\n" );
						return null;
					}
		}
	    
		try
		{
	     	System.out.print("\n\n\n La sentencia SQL BASE (historicos)" + consultaStr + " codigoEncabezado "+codigoEncabezado+"\n\n\n");
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psConsulta.setInt(1, codigoEncabezado);			
			
	 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado los históricos de la secciones de la Hoja Oftalmológica: "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Método para consultar los históricos de cada una de las secciones  de acuerdo al parámetro mandado
	 * @param con
	 * @param codigoPaciente
	 * @param nroConsulta.  1=>Historico Estrabismo, 2=> Histórico Segmento Anterior
	 * 							   				  3=>Histórico Retina y Vítreo, 4=> Histórico Orbita y anexos
	 * @return Collection
	 */
	public static Collection consultarTipoHistorico (Connection con, int codigoPaciente, int nroConsulta)
	{
		String consultaStr="";

		//Seleccionar el tipo de informacion a consultar
	  
	    switch(nroConsulta)
		{
	    	//Histórico de la sección Estrabismo
	        case 1:
	        	consultaStr="SELECT eho.codigo AS cod_historico, eho.fecha AS fecha_histo FROM enca_histo_hoja_ofta eho " +
	        							"INNER JOIN estrabismo es ON (eho.codigo=es.codigo_historico) " +
	        							"INNER JOIN hoja_oftalmologica ho ON (eho.hoja_oftalmologica=ho.codigo) " +
	        								"WHERE ho.paciente=? ORDER BY eho.codigo DESC";
	        	break;
	        
	        //Histórico de la sección Segmento Anterior
			case 2:
				consultaStr="SELECT eho.codigo AS cod_historico,eho.fecha AS fecha_histo,sai.codigo AS segmento_ant_ins," +
										 "dsa.valor_od AS valor_od,dsa.valor_os AS valor_os, eho.datos_medico AS datos_medico, sa.imagen_od as imagen_od, sa.imagen_os as imagen_os " +
										 "FROM enca_histo_hoja_ofta eho INNER JOIN segmento_anterior sa ON (eho.codigo=sa.codigo_historico) " +
										 "LEFT JOIN detalle_segmento_ant dsa ON (dsa.codigo_histo_seg=sa.codigo_historico) " +
										 "LEFT JOIN segmento_ant_institucion sai ON (dsa.segmento_ant_inst=sai.codigo)" +
										 " INNER JOIN hoja_oftalmologica ho ON (eho.hoja_oftalmologica=ho.codigo) " +
										 "WHERE ho.paciente=? ORDER BY eho.codigo DESC";
				break;
			
			//Histórico de la sección Retina y Vítreo
			case 3:
				consultaStr=
                    "select eho.codigo as cod_historico, eho.fecha as fecha_histo,eho.datos_medico as datos_medico, " +
                    "rv.imagen_retina_od as imagen_retina_od, rv.imagen_retina_os as imagen_retina_os, " +
                    "rv.imagen_vitreo_od as imagen_vitreo_od, rv.imagen_vitreo_os as imagen_vitreo_os " +
                    "from enca_histo_hoja_ofta eho " +
                    "inner join retina_vitreo rv on (eho.codigo=rv.codigo_historico) " +
                    "inner join hoja_oftalmologica ho on (eho.hoja_oftalmologica=ho.codigo) " +
                    "where ho.paciente=? order by eho.codigo desc";
				break;
			
			//Histórico de la sección Orbita y Anexos	
			case 4:
				consultaStr="SELECT eho.codigo AS cod_historico,eho.fecha AS fecha_histo,oai.codigo AS orbita_anexo_ins," +
										"oa.valor_od AS valor_od,oa.valor_os AS valor_os, eho.datos_medico AS datos_medico " +
										" FROM enca_histo_hoja_ofta eho " +
										"INNER JOIN orbita_anexos oa ON (eho.codigo=oa.codigo_historico) " +
										"INNER JOIN orbita_anexos_inst oai ON (oa.orbita_anexos_inst=oai.codigo) " +
										"INNER JOIN hoja_oftalmologica ho ON (eho.hoja_oftalmologica=ho.codigo) " +
											"WHERE ho.paciente=? ORDER BY eho.codigo DESC";
				break;
				
			default :
					{
						logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-4] El valor recibido es "+ nroConsulta + "\n\n" );
						return null;
					}
		}
	    
		try
		{
	     	//System.out.print("\n\n\n La sentencia SQL BASE (historicos)" + consultaStr + " \n\n\n");
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psConsulta.setInt(1, codigoPaciente);			
			
	 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado los históricos de la secciones de la Hoja Oftalmológica: "+e.toString());
			return null;
		}
	}

	/**
	 * Metodo para consultar la hoja oftalmológica del paciente cargado 
	 * @param con
	 * @param codigoPaciente
	 * @return Collection con los datos de la Hoja Oftalmológica
	 */
	public static Collection cargarHojaOftalmologica (Connection con, int codigoPaciente)
	{
		String consultaStr = "SELECT codigo AS codigo, observ_estrabismo AS observ_estrabismo, observ_segmento_ant AS observ_segmento_ant, " +
																"observ_retina_vitreo AS observ_retina_vitreo, observ_orbita_anexos AS observ_orbita_anexos " +
																" FROM hoja_oftalmologica WHERE 	paciente = ?";

		
		try
			{
				PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
				psConsulta.setInt(1, codigoPaciente);
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
			} 
		catch (SQLException e)
			{
				logger.error("Error Consultado la Hoja Oftalmológica :"+e.toString());
				return null;
			}
  }
    
    public static Collection cargarSegmentoAnterior(Connection con, int codHistorico)
    {
        String consultaStr = 
            "select " +
            "codigo_historico as codigo, " +
            "imagen_od as imagenOD, " +
            "imagen_os as imagenOS " +
            "from segmento_anterior where codigo_historico = ?";

        try
        {
            PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
            psConsulta.setInt(1, codHistorico);
            
            return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
        } 
        catch (SQLException e)
        {
            logger.error("Error Consultado Segmento Anterior:"+e.toString());
            return null;
        }
    }
	
    public static Collection cargarRetinaVitreo(Connection con, int codHistorico)
    {
        String consultaStr = 
            "select " +
            "codigo_historico as codigo, " +
            "imagen_retina_od as imagenRetinaOD, " +
            "imagen_retina_os as imagenRetinaOS " +
            "imagen_vitreo_od as imagenVitreoOD " +
            "imagen_vitreo_os as imagenVitreoOS " +
            "from retina_vitreo where codigo_historico = ?";

        try
        {
            PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
            psConsulta.setInt(1, codHistorico);
            
            return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
        } 
        catch (SQLException e)
        {
            logger.error("Error Consultado Retina Vitreo:"+e.toString());
            return null;
        }
    }
    
	/**
	 * Metodo para cargar la información del histórico de la sección Estrabismo, cuando seleccionan la 
	 * fecha histórica
	 * @param con
	 * @param codigoHistorico
	 * @return Collection con los datos del histórico de estrabismo
	 */
	public static Collection cargarHistoricoEstrabismo (Connection con, int codigoHistorico)
	{
		String consultaStr = "SELECT es.ppm AS ppm, es.cover_test_cerca_sc AS cover_test_cerca_sc, " +
															"es.cover_test_cerca_cc AS cover_test_cerca_cc, es.cover_test_lejos_sc AS cover_test_lejos_sc, " +
															"es.cover_test_lejos_cc AS cover_test_lejos_cc, es.ojo_fijador AS ojo_fijador, " +
															"es.ppc_institucion AS ppc_institucion, es.prisma_cc_lejos AS prisma_cc_lejos, " +
															"es.prisma_sc_lejos AS prisma_sc_lejos, es.ducciones_versiones AS ducciones_versiones, " +
															"es.vision_binocular AS vision_binocular, es.estereopsis AS estereopsis, " +
															"es.amp_fusion_cerca_mas AS amp_fusion_cerca_mas, es.amp_fusion_cerca_menos AS amp_fusion_cerca_menos, " +
															"es.amp_fusion_lejos_mas AS amp_fusion_lejos_mas, es.amp_fusion_lejos_menos AS amp_fusion_lejos_menos, " +
															"es.prisma_compe_lejos AS prisma_compe_lejos, es.prisma_compe_cerca AS prisma_compe_cerca, " +
															"eho.datos_medico AS datos_medico, eho.fecha AS fecha_grabacion " +
																" FROM estrabismo es " +
																	" INNER JOIN enca_histo_hoja_ofta eho ON (eho.codigo=es.codigo_historico)  " +
																		" WHERE es.codigo_historico=?";

		
		try
		{
		PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
		psConsulta.setInt(1, codigoHistorico);
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
		} 
		catch (SQLException e)
		{
		logger.error("Error Consultado el histórico de la sección estrabismo para un histórico determinado :"+e.toString());
		return null;
		}
	}
	
	/**
	 * Metodo para cargar la información histórica del prisma cerca en la sección Estrabismo cuando seleccionan una 
	 * fecha histórica
	 * @param con
	 * @param codigoHistorico
	 * @return Collection con los datos del histórico del prisma cerca
	 */
	public static Collection cargarHistoricoPrismaCerca (Connection con, int codigoHistorico)
	{
		String consultaStr = "SELECT pc.seccion AS seccion,pc.correccion AS correccion,pc.valor AS valor " +
												"FROM prisma_cerca pc " +
													"WHERE codigo_histo_estra=?";

			
			try
				{
				PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
				psConsulta.setInt(1, codigoHistorico);
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
				} 
			catch (SQLException e)
				{
				logger.error("Error Consultado el prisma cerca en la sección estrabismo para un histórico determinado :"+e.toString());
				return null;
				}
	}

	/**
	 * Método que consulta el encabezado de una valoración específica
	 * @param con
	 * @param valoracion
	 * @return
	 */
	public static int consultarHistoricoHojaOftal(Connection con, int valoracion, int tipoConsulta)
	{
		String relacionarConValoracionStr="";
		switch(tipoConsulta)
		{
			case 1:
				relacionarConValoracionStr="SELECT max(v.encabezado_hoja) AS encabezado_hoja FROM valoracion_hoja_oftal v INNER JOIN estrabismo e ON(e.codigo_historico=v.encabezado_hoja) WHERE v.valoracion=?";
			break;
			case 2:
				relacionarConValoracionStr="SELECT max(v.encabezado_hoja) AS encabezado_hoja FROM valoracion_hoja_oftal v INNER JOIN detalle_segmento_ant d ON(d.codigo_histo_seg=v.encabezado_hoja) WHERE v.valoracion=?";
			break;
			case 3:
				relacionarConValoracionStr="SELECT max(v.encabezado_hoja) AS encabezado_hoja FROM valoracion_hoja_oftal v INNER JOIN retina_vitreo r ON(r.codigo_historico=v.encabezado_hoja) WHERE v.valoracion=?";
			break;
			case 4:
				relacionarConValoracionStr="SELECT max(v.encabezado_hoja) AS encabezado_hoja FROM valoracion_hoja_oftal v INNER JOIN orbita_anexos o ON(o.codigo_historico=v.encabezado_hoja) WHERE v.valoracion=?";
			break;
		}
		
		PreparedStatementDecorator ps;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(relacionarConValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, valoracion);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("encabezado_hoja");
			}
			return 0;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el ultimo código de encabezado para una valoracion : "+e);
			return -1;
		}
		
	}
}
