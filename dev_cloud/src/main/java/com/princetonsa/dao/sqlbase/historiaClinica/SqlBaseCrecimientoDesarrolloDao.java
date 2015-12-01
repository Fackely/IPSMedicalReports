/* Princeton S.A (Parquesoft-Manizales)
*  Julian Montoya
*  Creado 14-nov-2006 11:05:38
*/
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseCrecimientoDesarrolloDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCrecimientoDesarrolloDao.class);

	
	/**
	 * Metodo estandar para consultar la informacion del paciente.
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap consultarInformacion(Connection con, HashMap mp)
	{
		String consulta =""; 
		int nroConsulta = 0;
		
		if ( UtilidadCadena.noEsVacio(mp.get("nroConsulta")+"")  )
		{
			nroConsulta = UtilidadCadena.vInt(mp.get("nroConsulta")+"");
		}
		else { return (new HashMap()); }

		switch (nroConsulta)
		{
			case 1:  //-- Consultar la informacion del paciente (Los examenes fisicos). 
			{
				consulta = "SELECT "+
						"getEdad(p.fecha_nacimiento) as edad_anios, "+
						"to_char(p.fecha_nacimiento, 'YYYY-MM-DD')  as  fecha_nacimiento, "+
						"s.numero_solicitud as solicitud, "+ 
						"to_char(v.fecha_valoracion, 'YYYY-MM-DD') as fecha, "+ 
						"vs.signo_vital as cod_signo_vital, "+ 
						"vs.valor, "+ 
						"sv.nombre as des_signo_vital "+ 
						"FROM personas p "+ 
						"INNER JOIN cuentas c ON ( c.codigo_paciente = p.codigo  ) "+ 
						"INNER JOIN centros_costo cc ON (cc.codigo = c.area) "+ 
						"INNER JOIN solicitudes s ON ( s.cuenta = c.id ) "+ 
						"INNER JOIN valoraciones v ON ( v.numero_solicitud = s.numero_solicitud ) "+ 
						"INNER JOIN val_signos_vitales vs ON(vs.valoracion = v.numero_solicitud) "+
						"INNER JOIN signos_vitales sv ON ( sv.codigo = vs.signo_vital) "+ 
						"WHERE p.codigo = " + mp.get("codigoPaciente") +" AND cc.institucion = " + mp.get("institucion") +
						"AND TO_DATE(TO_CHAR(v.fecha_valoracion, 'YYYY-MM-DD') || ' ' || v.hora_valoracion, 'YYYY-MM-DD HH24:MI') <= TO_DATE('"+ UtilidadFecha.conversionFormatoFechaABD(mp.get("fechaCorte").toString()) + " " + mp.get("horaCorte").toString() +"' , 'YYYY-MM-DD HH24:MI')"+
						"ORDER BY TO_DATE(TO_CHAR(v.fecha_valoracion, 'YYYY-MM-DD') || ' ' || v.hora_valoracion, 'YYYY-MM-DD HH24:MI')";
					
			}
			break;
			//------
			//------ Consulta para sacar EDAD X PESO. 
			//------
			case 2:   //---------- Niñas 
			{																			//-- 0-36
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		      WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF036_EdXP;
			}
			break;
			case 3:   
			{						//--La division se hace para retornar en años		//-- 2-20	
				consulta = "  SELECT valor_x/12 as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  	       WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF220_EdXP;
			}
			break;
			case 4:   //-----Niños
			{																			//-- 0-36
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		      WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM036_EdXP;
			}
			break;
			case 5:   
			{																			//-- 2-20	
				consulta = "  SELECT valor_x/12 as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  	       WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM220_EdXP;
			}
			break;
			//------
			//------ Consulta para sacar ESTATURA x EDAD 
			//------
			case 6:   //-- Niños
			{	//-- 0-36
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  		    WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM036_EXE;
			}
			break;
			case 7:   
			{	//-- 2-20	
				consulta = "  SELECT valor_x/12 as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  		    WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM220_EXE;
			}
			break;
			case 8:   //-- Niñas
			{		  //-- 0-36
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  		    WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF036_EXE;
			}
			break;
			case 9:   
			{				//-- 2-20	
				consulta = "  SELECT valor_x/12 as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  		    WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF220_EXE;
			}
			break;
			//------
			//------ Consulta para sacar PESO X ESTATURA PERCENTIL  
			//------
			case 10:   //-- Estatura Peso Percentil Niñas
			{		   
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p						    " +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  		   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF220_EsXP;
			}	
			break;
			case 11:   
			{		   //-- Estatura Peso Percentil Niños	
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p " +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		     	   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM220_EsXP; 
			}
			break;
			case 12:   //-- Estatura Peso Niños HC (0-36)
			{																
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p						    " +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		  		   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM036_EsXP;
			}	
			break;
			case 13:   
			{			//-- Estatura Peso HC Niñas (0-36)	
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p " +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		     	   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF036_EsXP; 
			}
			break;
			//------
			//------ Consulta para sacar EDAD X PERIMETRO CEFALICO 
			//------
			case 14:   //- Niñas
			{		   //-- 0-36
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		     	   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF036_PCE;
			}
			break;
			case 15:   //- Niños
			{			//-- 0-36
				consulta = "  SELECT valor_x as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		     	   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM036_PCE;
			}
			break;
			//------
			//------ Consulta para sacar EDAD X IMC 
			//------
			case 16:   //- Niñas
			{		   //-- 2-20	
				consulta = "  SELECT valor_x/12 as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		   		   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloF220_IMCXE;
			}
			break;
			case 17:   //-Niños
			{																			//-- 2-20	
				consulta = "  SELECT valor_x/12 as x, valor_y as y, percentil_curva as p 							" +
						   "		 FROM grafica_crecimiento_dllo												" +
						   "		   		   WHERE plantilla_curva = " + ConstantesBD.codTpGrafCurvaCreciDlloM220_IMCXE;
			}
			break;
			case 18:   //-- Consultar los rangos de los ejes para las diferentes plantillas 
			{																			//-- 2-20	
				consulta = "  SELECT 'ejex' as eje, m.rango_inicial, m.rango_final							" +
						   "  		 FROM medicion_curva m													" +
						   "  		      INNER JOIN plantilla_curva p ON ( p.medicion_eje_x = m.codigo )	" +
						   "		     	   WHERE p.codigo = " + mp.get("plantilla") +
						   "  UNION ALL																  		" +      	   
						   "  SELECT 'ejey' as eje,  m.rango_inicial, m.rango_final							" +	
						   "  		 FROM medicion_curva m													" +
						   " 		      INNER JOIN plantilla_curva p ON ( p.medicion_eje_y = m.codigo )	" +
						   "		      	   WHERE p.codigo = " + mp.get("plantilla");
			}
			break;
			default:
			{
				logger.info(" EL NUMERO DE CONSULTA ESTA FUERA DE RANGO [" + mp.get("nroConsulta") + "]");
			}		
		}
		PreparedStatementDecorator pst = null;
		try
		{
			logger.info("\n\n Consulta -->  ["  + nroConsulta + "] [" + consulta + "]  \n\n"); 
			 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarInformacion de SqlBaseCrecimientoDesarrolloDao: "+e); 
			return null;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
	}

}
