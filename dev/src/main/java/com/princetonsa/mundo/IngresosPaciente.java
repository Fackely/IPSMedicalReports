/*
 * @(#)IngresosPaciente.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.Answer;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase permite conocer todos los ingresos para un paciente en
 * particular (para los cuales este tiene permiso) , únicamente está compuesta
 * por la identificación del paciente, su institución, la lista de ingresos del
 * mismo (como atributos) y una constructora como métodos.
 * 
 * Porqué crear una Clase para esto y no simplemente una lista en Paciente?
 * 
 * Principalmente porque la lista no solo depende la institución, sino de la
 * institución (Solo ven los ingresos para los cuales esta esta institución
 * tenga permisos de segundo nivel). Después se puede incluir esta información
 * y funcionalidad en Paciente. Otra razón es que un paciente no tiene
 * como atributo la institución (puede pertenecer a varias) mientras que la
 * lista depende no solo del paciente sino de su institución
 *
 * @version 1.0, Feb 4, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class IngresosPaciente {

	private String codigoInstitucion="";
	private String [] idsIngreso=null;
	private int codigoPaciente=0; 
	
	public IngresosPaciente (int codigoPaciente, String codigoInstitucion)
	{
		this.codigoPaciente=codigoPaciente;
		this.codigoInstitucion=codigoInstitucion; 
	}
	public String[] cargarIngresosPaciente(Connection con, String tipoBD) throws SQLException
	{

		TagDao tagDao;
					
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		
		String cargarIngresosPaciente="SELECT id from ingresos where institucion IN (SELECT codigo_institucion_duena from pacientes_instituciones2 where codigo_institucion_permitida =" + codigoInstitucion + " and codigo_paciente=" + codigoPaciente + ") and codigo_paciente=" + codigoPaciente;
		
		Answer a=tagDao.resultadoConsulta(con, cargarIngresosPaciente);
		ResultSetDecorator rs=a.getResultSet();
		String temporal="";
		boolean primeraVez=true;
		
		int numResultados=0;
		
		while (rs.next())
		{
			if (primeraVez)
			{
				primeraVez=false;
				temporal=rs.getString("id");
			}
			else
			{
				temporal=temporal + "-" +rs.getString("id");
			}
			numResultados++;
		}

		if (numResultados==0)
			return null;
		else if (numResultados==1)
		{
			String res[]=new String[1];
			res[0]=temporal;
		}
		else
		{
			return UtilidadTexto.separarNombresDeCodigos(temporal, (numResultados-1));
		}
		
		return temporal.split("-");
	}

	
}