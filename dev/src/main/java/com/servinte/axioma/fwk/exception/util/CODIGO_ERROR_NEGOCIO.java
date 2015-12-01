package com.servinte.axioma.fwk.exception.util;

/**
 * Interface de constantes que contiene los códigos de error de negocio de todos los módulos 
 * 
 * @author Ricardo Ruiz 
 * @since 21/06/2012
 * 
 *
 */
public interface CODIGO_ERROR_NEGOCIO {

	
	/************************************************************************
	 * Códigos error GENERALES
	 ************************************************************************/
	static final Long ERROR_BASE_DATOS = 1L;
	
	static final Long ERROR_NO_CONTROLADO = 2L;
	
	static final Long ERROR_CAST = 3L;
	
	static final Long ERROR_PARSE = 4L;
	
	static final Long ERROR_NULLPOINTER = 5L;
	
	/************************************************************************
	 * Códigos error ADMINISTRACIÓN
	 ************************************************************************/
	static final Long ERROR_GENERAL_ADMINISTRACION=1000L;
	
	
	/************************************************************************
	 * Códigos error MANEJO PACIENTE
	 ************************************************************************/
	static final Long MANEJO_PACIENTE_GENERAL=2000L;
	
	static final Long MANEJO_PACIENTE_ORDENES_REQUERIDAS=2001L;
	
	static final Long MANEJO_PACIENTE_DIFERENTE_MES_ANIO=2002L;
	
	static final Long MANEJO_PACIENTE_ORDENES_DIFERENTES=2003L;

	static final Long MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION=2004L;

	static final Long MANEJO_PACIENTE_NO_CENTROS_COSTO_ORDENES=2005L;
	
	static final Long MANEJO_PACIENTE_NO_NIVEL_AUTORIZACION=2006L;
	
	static final Long MANEJO_PACIENTE_NO_MONTOS_COBRO=2007L;	
	
	/************************************************************************
	 * Códigos error SALAS CIRUGIA
	 ************************************************************************/
	static final Long NO_EXISTE_CIRUJANO=2008L;	
	static final Long NO_HAY_MEDICO_INTERPRETA=2009L;
	
}


