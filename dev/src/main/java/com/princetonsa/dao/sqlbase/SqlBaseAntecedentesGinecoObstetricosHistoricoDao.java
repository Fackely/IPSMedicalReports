/*
 * @(#)SqlBaseAntecedentesGinecoObstetricosHistoricoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedentes Gineco
 * Obstetricos historicos
 *
 *	@version 1.0, Mar 31, 2004
 */
public class SqlBaseAntecedentesGinecoObstetricosHistoricoDao 
{
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un
	 * Antecedente GinecoObstetrico Historico en Genérica.
	 */
	public static final String insertarAntecedenteGinecoHisto = " " +
			"	INSERT INTO ant_gineco_histo (" +
			"		codigo_paciente, codigo, duracion_menstruacion, dolor_menstruacion, fecha_ultima_regla, " +
			"	 	concepto_menstruacion, fecha_ultima_mamografia, descripcion_ultima_mamografia, fecha_ultima_citologia, descripcion_ultima_citologia, " +
			"		descripcion_ultimo_proc_gine, fecha_ultima_ecografia, descripcion_ultima_ecografia, observaciones_menstruacion, ciclo_menstrual, " +
			"		g_informacion_embarazos, p_informacion_embarazos, p2500, p4000, a_informacion_embarazos, " +
			"		mayorA2, c_informacion_embarazos, v_informacion_embarazos, m_informacion_embarazos, finEmbarazoAnterior, " +
			"		finEmbarazoMayor1o5, prematuros, ectropicos, multiples, fecha_ultima_densimetria_osea, " +
			"		desc_ultima_densimetria_osea, fecha_creacion, hora_creacion,  vag, retencion_placentaria, infeccion_postparto, " +
			"		malformacion, muerte_perinatal, sangrado_anormal, " +
			"		flujo_vaginal, enfer_trans_sexual, cual_enfer_trans_sex, cirugia_gineco, cual_cirugia_gineco," +
			"	 	historia_infertilidad, cual_histo_infertilidad,tipo_embarazo, muertos_antes_1semana, muertos_despues_1semana," +
			"		vivos_actualmente 	" +
			"	 ) " +
			"	 VALUES (?, (SELECT count(codigo)+1 FROM ant_gineco_histo WHERE  codigo_paciente=? ), ?, ?, ?, " +
			"			 ?, ?, ?, ?, ?, " +
			"			 ?, ?, ?, ?, ?, " +
			" 			 ?, ?, ?, ?, ?, " +
			"			 ?, ?, ?, ?, ?, " +
			"			 ?, ?, ?, ?, ?, " +
			"			 ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ?, ?, ?," +
			"			 ?, ?, ?, ?, ?, " +
			"			 ?, ?, ?, ?, ?, " +
			"			 ?, ?, ? " +
			"			 )";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar si existe
	 * un antecedente historico en particular
	 */
	
	public static final String buscarAntecedenteHistoricoStr = "SELECT count(1) FROM ant_gineco_histo WHERE codigo_paciente = ? AND codigo = ?";
	
	

	/**
	 * Implementación de la inserción de AntecedentesGinecoObstetricos
	 * historicos para Genérica (Definiendo Transaccionalidad)
	 * @param causa_muerte_perinatal 
	 * @param muerte_perinatal 
	 * @param cual_malformacion 
	 * @param malformacion 
	 * @param infeccion_postparto 
	 * @param retencion_placentaria 
	 * @param vag 
	 * @param cualHistoInfertilidad 
	 * @param historiaInfertilidad 
	 * @param cualCirugiaGineco 
	 * @param cirugiaGineco 
	 * @param cualEnferTransSex 
	 * @param enferTransSexual 
	 * @param flujoVaginal 
	 * @param sangradoAnormal 
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosHistoricoDao#insertarTransaccional(Connection, String, String, String, boolean, String, int, String, String, String, String, String, String)
	 */
	public static int insertarTransaccional (Connection con, int codigoPaciente, int duracionMenstruacion, String dolorMenstruacion, 
											String fechaUltimaRegla, int codigoConceptoMenstruacion, String fechaUltimaMamografia, 
											String descripcionUltimaMamografia, String fechaUltimaCitologia, String descripcionUltimaCitologia, 
											String descripcionUltimoProcesoGinecoObstetrico, String estado, String fechaUltimaEcografia,
											String descripcionUltimaEcografia, String observacionesMenstruacion, int cicloMenstrual,
											String gInfoEmbarazos, String pInfoEmbarazos, String p2500, String p4000, String aInfoEmbarazos,
											String mayorA2, String cInfoEmbarazos, String vInfoEmbarazos, String mInfoEmbarazos,
											String finEmbarazoAnterior, String finEmbarazoMayor1o5, String prematuros, 
											String ectropicos, String multiples, String fechaUltimaDensimetriaOsea, String descUltimaDensimetriaOsea,
											int vag, String retencion_placentaria, String infeccion_postparto,String malformacion, String muerte_perinatal,
											String sangradoAnormal, String flujoVaginal, String enferTransSexual, String cualEnferTransSex,
											String cirugiaGineco, String cualCirugiaGineco, String historiaInfertilidad, String cualHistoInfertilidad,
											String tipoEmbarazo,String muertosAntes1Semana,String muertosDespues1Semana,String vivosActualmente
											) throws SQLException
	{	
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));	
		PreparedStatementDecorator stm=null;   
		try
		{
			
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}
				
			if (estado.equals("empezar"))
			{
				con.setAutoCommit(false);
				myFactory.beginTransaction(con);
				resp0=1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
	
			stm = new PreparedStatementDecorator(con.prepareStatement(insertarAntecedenteGinecoHisto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoPaciente);
			stm.setInt(2, codigoPaciente);
		
			if(duracionMenstruacion==0)
			{
				stm.setString(3, null);	
			}
			else
			{
				stm.setInt(3, duracionMenstruacion);
			}
			
			if(dolorMenstruacion.equals(""))
			{
				stm.setString(4, null);
			}	
			else
			{	
				if(dolorMenstruacion.equals("true"))
				{
					stm.setString(4, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{
					stm.setString(4, ValoresPorDefecto.getValorFalseParaConsultas());
				}	
			}
			//Si la fecha es vacia NO hay que insertarla, hay que poner un nulo
			if (UtilidadCadena.noEsVacio(fechaUltimaRegla))
			{
				stm.setString(5, fechaUltimaRegla);
			}
			else
			{
				stm.setString(5, null);
			}
			
			stm.setInt(6, codigoConceptoMenstruacion);
			
			//Si la fecha es vacia NO hay que insertarla, hay que poner un nulo
			if (fechaUltimaMamografia==null)
			{
				stm.setString(7,null);
			}
			else
			{
				stm.setString(7,fechaUltimaMamografia);
			}
			
			stm.setString(8, descripcionUltimaMamografia);

			if (fechaUltimaCitologia==null||fechaUltimaCitologia.equals(""))
			{
				stm.setString(9, null);
			}
			else
			{
				stm.setString(9, fechaUltimaCitologia);
			}

			stm.setString(10,descripcionUltimaCitologia);
			stm.setString(11,descripcionUltimoProcesoGinecoObstetrico);

			if (fechaUltimaEcografia==null||fechaUltimaEcografia.equals(""))
			{
				stm.setString(12, null);
			}
			else
			{
				stm.setString(12, fechaUltimaEcografia);
			}
			
			stm.setString(13, descripcionUltimaEcografia);
			stm.setString(14, observacionesMenstruacion);
			
			if(cicloMenstrual==0)
			{
				stm.setString(15, null);
			}
			else
			{
				stm.setInt(15, cicloMenstrual);	
			}
			
			stm.setString(16, gInfoEmbarazos);
			stm.setString(17, pInfoEmbarazos);
			
			if (UtilidadTexto.getBoolean(p2500)) 
			{
				p2500 = ValoresPorDefecto.getValorTrueParaConsultas();
			}
			else
			{
				p2500 = null;
			}	
			stm.setString(18, p2500);			

			if (UtilidadTexto.getBoolean(p4000)) 
			{
				p4000 = ValoresPorDefecto.getValorTrueParaConsultas();
			}
			else
			{
		        p4000 = null;
			}
			stm.setString(19, p4000);
			
			stm.setString(20, aInfoEmbarazos);
			
        	if (UtilidadTexto.getBoolean(mayorA2)) 
			{
        		mayorA2 = ValoresPorDefecto.getValorTrueParaConsultas();
			}
			else
			{
				mayorA2 = null;	
			}	
			stm.setString(21, mayorA2);
			stm.setString(22, cInfoEmbarazos);

			stm.setString(23, vInfoEmbarazos);
			stm.setString(24, mInfoEmbarazos);
			
			if ( (finEmbarazoAnterior==null) || finEmbarazoAnterior.trim().equals("") ) 
			{
				finEmbarazoAnterior = null;
			}
			stm.setString(25, finEmbarazoAnterior);	
			
			if ((finEmbarazoMayor1o5 == null) || finEmbarazoMayor1o5.trim().equals("") ) 
			{
				finEmbarazoMayor1o5 = null;
			}
			stm.setString(26, finEmbarazoMayor1o5);
			
			
			if ((prematuros == null) || prematuros.trim().equals("")  )
			{
				prematuros = null;
			}
			
			stm.setString(27, prematuros);
			
			
			if ( (ectropicos == null) || ectropicos.trim().equals("") ) 
			{
				ectropicos = null;
			}
			stm.setString(28, ectropicos);
			
			if ( (multiples == null) || multiples.trim().equals("") ) 
			{
				multiples = null;
			}
			stm.setString(29, multiples);				

			//-Insertar los datos nuevos Ultima modificacion Antecedentes...

			if( !UtilidadCadena.noEsVacio(fechaUltimaDensimetriaOsea) )
			{
				stm.setString(30, null);
			}
			else
			{
				stm.setString(30, fechaUltimaDensimetriaOsea);			
			}
			stm.setString(31, descUltimaDensimetriaOsea);
			

			
			//------------------------------------------- Informacion Nueva -----------------------------------------------
			if (vag != 0) 
			{ 
				stm.setInt(32, vag); 
			}
			else
			{ 
				stm.setNull(32,Types.NULL); 
			}
				
			if ( UtilidadCadena.noEsVacio(retencion_placentaria) )
			{
				if(UtilidadTexto.getBoolean(retencion_placentaria))
				{
					stm.setString(33, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{	
					stm.setString(33, ValoresPorDefecto.getValorFalseParaConsultas());
				}	
			}	
			else
			{
				stm.setNull(33,Types.NULL);
			}	
			if ( UtilidadCadena.noEsVacio(infeccion_postparto) )
			{
				if(UtilidadTexto.getBoolean(infeccion_postparto))
				{
					stm.setString(34, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{	
					stm.setString(34, ValoresPorDefecto.getValorFalseParaConsultas());
				}	
			}
			else
			{
				stm.setNull(34, Types.NULL);
			}	

			if ( UtilidadCadena.noEsVacio(malformacion) )
			{
				if(UtilidadTexto.getBoolean(malformacion))
				{
					stm.setString(35, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{	
					stm.setString(35, ValoresPorDefecto.getValorFalseParaConsultas());
				}
			}	
			else 
			{
				stm.setNull(35, Types.NULL);
			}
				
			if ( UtilidadCadena.noEsVacio(muerte_perinatal) )
			{
				if(UtilidadTexto.getBoolean(muerte_perinatal))
				{
					stm.setString(36, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{	
					stm.setString(36, ValoresPorDefecto.getValorFalseParaConsultas());
				}
			}	
			else 
			{
				stm.setNull(36, Types.NULL);
			}	

			
			if ( UtilidadCadena.noEsVacio(sangradoAnormal) )
			{
				stm.setString(37, sangradoAnormal);
			}	
			else
			{
				stm.setNull(37, Types.NULL);
			}
			if ( UtilidadCadena.noEsVacio(flujoVaginal) )
			{
				stm.setString(38, flujoVaginal);
			}	
			else
			{
				stm.setNull(38, Types.NULL);
			}
			if ( UtilidadCadena.noEsVacio(enferTransSexual) )
			{
				stm.setString(39, enferTransSexual);
			}	
			else
			{
				stm.setNull(39, Types.NULL);
			}	

			if ( UtilidadCadena.noEsVacio(cualEnferTransSex) )
			{
				stm.setString(40, cualEnferTransSex);
			}	
			else
			{
				stm.setNull(40, Types.NULL);
			}	

			if ( UtilidadCadena.noEsVacio(cirugiaGineco) )
			{	
				stm.setString(41, cirugiaGineco);
			}	
			else
			{	
				stm.setNull(41, Types.NULL);
			}	
			
			if ( UtilidadCadena.noEsVacio(cualCirugiaGineco) )
			{	
				stm.setString(42, cualCirugiaGineco);
			}	
			else
			{	
				stm.setNull(42, Types.NULL);
			}	

			if ( UtilidadCadena.noEsVacio(historiaInfertilidad) )
			{	
				stm.setString(43, historiaInfertilidad);
			}	
			else
			{
				stm.setNull(43, Types.NULL);
			}	
			if ( UtilidadCadena.noEsVacio(cualHistoInfertilidad) )
			{
				stm.setString(44, cualHistoInfertilidad);
			}	
			else 
			{
				stm.setNull(44, Types.NULL);
			}	

			stm.setString(45, tipoEmbarazo.trim());

			if ( UtilidadCadena.noEsVacio(muertosAntes1Semana) )
			{
				if(UtilidadTexto.getBoolean(muertosAntes1Semana))
				{
					stm.setString(46, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{	
					stm.setString(46, ValoresPorDefecto.getValorFalseParaConsultas());
				}
			}	
			else 
			{
				stm.setNull(46, Types.NULL);
			}	

			if ( UtilidadCadena.noEsVacio(muertosDespues1Semana) )
			{
				if(UtilidadTexto.getBoolean(muertosDespues1Semana))
				{
					stm.setString(47, ValoresPorDefecto.getValorTrueParaConsultas());
				}
				else
				{	
					stm.setString(47, ValoresPorDefecto.getValorFalseParaConsultas());
				}
			}	
			else 
			{
				stm.setNull(47, Types.NULL);
			}	


			if ( UtilidadCadena.noEsVacio(vivosActualmente) )
			{
				stm.setString(48, vivosActualmente);	
			}	
			else
			{
				stm.setNull(48, Types.NULL);
			}	
			
			//--------------------------------------------------------------------------------------------------
			//--------------------------------------------------------------------------------------------------
			//------INSERTAR EL HISTORICO DE LA INFORMACION DE EMBARAZOS 
			resp1=stm.executeUpdate();
			stm.close();
			
			if (resp0<1||resp1<1)
			{			
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{		 
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}
			}		
		}
		catch(Exception e){
		    Log4JManager.error("ERROR insertarTransaccional", e);
	    }
	    finally{
			try{
				if(stm != null){
					stm.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp1;
	}

	
}
