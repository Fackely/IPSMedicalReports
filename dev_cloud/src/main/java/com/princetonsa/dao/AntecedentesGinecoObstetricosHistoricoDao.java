/*
 * @(#)AntecedentesGinecoObstetricosHistoricoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AntecedenteGinecoObstetrico</code>.
 * 
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public interface AntecedentesGinecoObstetricosHistoricoDao 
{

	/**
	 *  Inserta  una entrada m�s para el historico de los
	 * AntecedentesGinecoObstetricos que se llenan por consulta
	 * (No se incluyeron en la Historia Cl�nica ya que no hay un formulario de
	 * Rips para esto) y permite definir el nivel de Transaccionalidad
	 * (Definiendo par�metro estado)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente c�digo de la paciente a la que se le va a ingresar
	 * un antecedente historico GinecoObstetrico
	 * @param duracionMenstruacion Duraci�n actual de la menstruaci�n de la
	 * paciente
	 * @param dolorMenstruacion String que indica si la paciente siente dolor
	 * durante la menstruaci�n
	 * @param fechaUltimaRegla Fecha de la �ltima regla de la paciente
	 * @param codigoConceptoMenstruacion C�digo de la tabla que almacena los
	 * diferentes conceptos de menstruaci�n
	 * @param fechaUltimaMamografica Fecha de la �ltima mamograf�a  de la
	 * paciente
	 * @param descripcionUltimaMamografia Descripcion �ltima mamografia de la
	 * paciente
	 * @param fechaUltimaCitologia Fecha ultima citologia de la paciente
	 * @param descripcionUltimaCitologia Descripci�n de la ultima citologia de
	 * la paciente
	 * @param descripcionUltimoProcesoGinecoObstetrico Descripcion del �ltimo
	 * proceso gineco-obstetrico hecho a la paciente
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @param string11 
	 * @param string10 
	 * @param string9 
	 * @param string8 
	 * @param string7 
	 * @param string6 
	 * @param string5 
	 * @param string8 
	 * @param string7 
	 * @param string6 
	 * @param string5 
	 * @param string4 
	 * @param string3 
	 * @param string2 
	 * @param string 
	 * @param string4 
	 * @param string3 
	 * @param string2 
	 * @param string 
	 * @param i 
  	 * @param selecDolorMenstruacion
	 * @return int N�mero de AntecedentesGinecoObstetricos insertados (1
	 * Inserci�n exitosa, 0 problemas inserci�n)
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int duracionMenstruacion, String dolorMenstruacion, 
									  String fechaUltimaRegla, int codigoConceptoMenstruacion, String fechaUltimaMamografica,
									  String descripcionUltimaMamografia, String fechaUltimaCitologia, String descripcionUltimaCitologia,
									  String descripcionUltimoProcesoGinecoObstetrico, String estado, String fechaUltimaEcografia, 
									  String descripcionUltimaEcografia, String observacionesMenstruacion, int cicloMenstrual,
									  String gInfoEmbarazos, String pInfoEmbarazos, String p2500, String p4000, String aInfoEmbarazos,
									  String mayora2, String cInfoEmbarazos, String vInfoEmbarazos, String mInfoEmbarazos,
									  String finEmbarazoAnterior, String finEmbarazoMayor1o5, String prematuros, String ectropicos,
									  String multiples, String fechaUltimaDensimetriaOsea, String descUltimaDensimetriaOsea, int vag,  
									  String retencion_placentaria, String infeccion_postparto,String malformacion, String muerte_perinatal,
									  String sangradoAnormal, String flujoVaginal, String enferTransSexual, String cualEnferTransSex, String cirugiaGineco,	
									  String cualCirugiaGineco, String historiaInfertilidad, String cualHistoInfertilidad,
									  String tipoEmbarazo,String muertosAntes1Semana,String muertosDespues1Semana,String vivosActualmente
									   
									  ) throws SQLException;
	
}
