/*
 * @(#)OracleAntecedentesGinecoObstetricosHistoricoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.dao.AntecedentesGinecoObstetricosHistoricoDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesGinecoObstetricosHistoricoDao;

/**
 * Esta clase implementa el contrato estipulado en
 * <code>AntecedentesGinecoObstetricosHistoricoDao</code>, y presta los
 * servicios de acceso a una base de datos Oracle requeridos por la clase
 * <code>AntecedenteGinecoObstetricoHistorico</code>.
 *
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class OracleAntecedentesGinecoObstetricosHistoricoDao implements AntecedentesGinecoObstetricosHistoricoDao
{
	
	/**
	 * Implementación de la inserción de AntecedentesGinecoObstetricos
	 * historicos para Oracle (Definiendo Transaccionalidad)
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosHistoricoDao#insertarTransaccional(Connection, String, String, String, boolean, String, int, String, String, String, String, String, String)
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int duracionMenstruacion, String dolorMenstruacion,  String fechaUltimaRegla, 
									 int codigoConceptoMenstruacion, String fechaUltimaMamografia, String descripcionUltimaMamografia,
									 String fechaUltimaCitologia, String descripcionUltimaCitologia, String descripcionUltimoProcesoGinecoObstetrico, 
									 String estado, String fechaUltimaEcografia, String descripcionUltimaEcografia,
									 String observacionesMenstruacion, int cicloMenstrual, String gInfoEmbarazos, String pInfoEmbarazos,
									 String p2500, String p4000, String aInfoEmbarazos, String mayorA2, String cInfoEmbarazos, 
									 String vInfoEmbarazos, String mInfoEmbarazos, String finEmbarazoAnterior, 
									 String finEmbarazoMayor1o5, String prematuros, String ectropicos, String multiples, 
									 String fechaUltimaDensimetriaOsea, String descUltimaDensimetriaOsea,
									 int vag, String retencion_placentaria, String infeccion_postparto, String malformacion, String muerte_perinatal,									 
									 String sangradoAnormal, String flujoVaginal, String enferTransSexual, String cualEnferTransSex, String cirugiaGineco,	
									 String cualCirugiaGineco, String historiaInfertilidad, String cualHistoInfertilidad,
									 String tipoEmbarazo,String muertosAntes1Semana,String muertosDespues1Semana,String vivosActualmente
									) throws SQLException
	{	
		return SqlBaseAntecedentesGinecoObstetricosHistoricoDao.insertarTransaccional(con,
				codigoPaciente, duracionMenstruacion, dolorMenstruacion,  
				fechaUltimaRegla, codigoConceptoMenstruacion, fechaUltimaMamografia, 
				descripcionUltimaMamografia, fechaUltimaCitologia, descripcionUltimaCitologia,
				descripcionUltimoProcesoGinecoObstetrico, estado, fechaUltimaEcografia, descripcionUltimaEcografia, 
				observacionesMenstruacion, cicloMenstrual, gInfoEmbarazos, pInfoEmbarazos, p2500, p4000,
				aInfoEmbarazos, mayorA2, cInfoEmbarazos,  vInfoEmbarazos, mInfoEmbarazos,
				 finEmbarazoAnterior, finEmbarazoMayor1o5, prematuros, ectropicos, multiples,
				fechaUltimaDensimetriaOsea, descUltimaDensimetriaOsea, 
				vag, retencion_placentaria, infeccion_postparto,malformacion,  muerte_perinatal,
				sangradoAnormal, flujoVaginal, enferTransSexual, cualEnferTransSex, cirugiaGineco,	
				cualCirugiaGineco,  historiaInfertilidad, cualHistoInfertilidad,
				tipoEmbarazo,muertosAntes1Semana,muertosDespues1Semana,vivosActualmente
				);
	}

	

}
