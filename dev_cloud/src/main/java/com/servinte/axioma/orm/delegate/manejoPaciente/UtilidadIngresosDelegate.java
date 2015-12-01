package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.math.BigDecimal;

import org.hibernate.type.StandardBasicTypes;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.IngresosHome;



/**
 * Contiene los métodos para consulta de datos básicos necesarios
 * para la creacioón de un ingreso de paciente.
 * 
 * Estos metodos son utilizados para la creacion de:
 * 	-Ingreso
 * 	-Cuentas
 * 	-Subcuentas
 *
 * Esta Clase esta basada en com.servinte.migracion.util.Utilidades
 * 
 * @author Cristhian Murillo
 * 
 */
public class UtilidadIngresosDelegate extends IngresosHome 
{
	/*
	public static int constanteMontoCobroCien=-2;
	public static int constanteMontoCobroCero=-1;
	*/

	
	/**
	 * Obtiene el valor del consevutivo disponible
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return String
	 */
	/*
	public String obtenerValorConsecutivoDisponible(String nombreConsecutivo, int institucion)
	{
		String sql="SELECT siguienteConsdisponible('"+nombreConsecutivo+"',"+institucion+",'"+obtenerAnioActual()+"') as valor";
		String valRet=(String)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("valor",Hibernate.STRING).uniqueResult());
		return valRet;
	}
	*/
	
	
	
	/**
	 * Obtiene el año actual
	 * @return anio(String)
	 */
	/*
	public static String obtenerAnioActual() 
	{
		String anio=(String)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery("SELECT to_char(current_date,'yyyy') as anio").addScalar("anio",Hibernate.STRING).uniqueResult());
		return anio;
	}
	*/
	
	
	
	/**
	 * Obtiene el consevutivo disponible
	 * @param nombreConsecutivo
	 * @param institucion
	 * @param valor
	 * @return String
	 */
	/*
	public String obtenerAnioConsecutivoDisponible(String nombreConsecutivo, int institucion, String valor)
	{
		String sql="SELECT case when anio_vigencia is null then '' when anio_vigencia='-1' then '' else anio_vigencia end as aniovigencia from uso_consecutivos " +
					"where " +
					" nombre='"+nombreConsecutivo+"' and " +
					" institucion="+institucion+" and " +
					" valor="+valor;	
		String valRet=(String)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("aniovigencia",Hibernate.STRING).uniqueResult());
		return valRet;
	}
	*/
	
	
	
	/**
	 * Obtiene el monto cobro
	 * @param contrato
	 * @return int
	 */
	/*
	public static int obtenerMontoCobro(int contrato) 
	{
		String sql="SELECT coalesce(paciente_paga_atencion,'N') as temporal from contratos where codigo="+contrato;
		String pagaPaciente=(String)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("temporal",Hibernate.STRING).uniqueResult());
		if(pagaPaciente.equals("S"))
			return constanteMontoCobroCien;
		return constanteMontoCobroCero;
	}
	*/
	

	public static BigDecimal obtenerPorcentajeMontoCobro(int contrato) 
	{
		String sql="SELECT coalesce(paciente_paga_atencion,'N') as temporal from contratos where codigo="+contrato;
		String pagaPaciente=(String)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("temporal",StandardBasicTypes.STRING).uniqueResult());
		if(pagaPaciente.equals("S"))
			return new BigDecimal(100);
		return new BigDecimal(0);
	}

	
	
	
	
}
