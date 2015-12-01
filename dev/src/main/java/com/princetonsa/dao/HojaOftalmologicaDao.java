/*
 * Creado en Sep 21, 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public interface HojaOftalmologicaDao
{
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
public int insertarHojaOftalmologica(Connection con, int codigoPaciente, String observacionEstrabismo, String observacionSegmentoAnt, String observacionRetinaVitreo, String observacionOrbitaAnexos);

/**
 * Metodo para insertar el encabezado historico de la hoja oftalmológica  
 * @param con
 * @param codHojaOftalmologica
 * @param datosMedico
 * @param numeroSolicitud @todo
 * @return codEncabezadoHisto
 */ 
public int insertarEncabezadoHistoHojaOftalmologica(Connection con, int codHojaOftalmologica, String datosMedico, int numeroSolicitud);

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
public int insertarEstrabismo (Connection con, int codEncabezadoHisto, String ppm, String coverTestCercaSc, String coverTestCercaCc, String coverTestLejosSc, String coverTestLejosCc, int ojoFijador, int ppcInstitucion, 
														 String prismaCcLejos, String prismaScLejos, String duccionesVersiones, String testVisionBinocular, String estereopsis, String amplitudFusionCercaMas, 
														 String amplitudFusionCercaMenos, String amplitudFusionLejosMas, String amplitudFusionLejosMenos, String prismaCompensadorLejos, String prismaCompensadorCerca);

/**
 * Metodo para insertar el prisma cerca en la sección estrabismo  
 * @param con
 * @param encaHistoEstrabismo
 * @param seccion
 * @param correccion
 * @param valorPrismaCerca
 * @return encaHistoEstrabismo
 */
public int insertarPrismaCerca (Connection con, int encaHistoEstrabismo, int seccion, boolean correccion, String valorPrismaCerca);

/**
 * Metodo para insertar orbita y anexos  
 * @param con
 * @param codigoHistorico
 * @param orbitaAnexoInst
 * @param valorOd
 * @param valorOs
 * @return codHistoOrbitaAnexos
 */
public int insertarOrbitaAnexos (Connection con, int codigoHistorico, int orbitaAnexoInst, String valorOd, String valorOs);

/**
 * Metodo para insertar el detalle del segmento anterior  
 * @param con
 * @param codHistoSegmentoAnt
 * @param segmentoAntInst
 * @param valorOd
 * @param valorOs
 * @return codHistoSegmentoAnt
 */
public int insertarDetalleSegmentoAnt (Connection con, int codHistoSegmentoAnt, int segmentoAntInst, String valorOd, String valorOs);

/**
 * Metodo para insertar el Segmento anterior  
 * @param con
 * @param codHistorico
 * @return codHistoSegAnt
 */
public int insertarSegmentoAnterior (Connection con, int codHistorico, String imagenOd, String imagenOs);

public int insertarRetinaVitreo(Connection con, int codHistorico, String imagenRetinaOD, String imagenRetinaOS, String imagenVitreoOD, String imagenVitreoOS);

/**
 * Método para consultar los tipos aplicables para la hoja oftalmológica según la institución
 * @param con
 * @param institucion
 * @param tipo
 * @return Collection
 */
public Collection consultarTipoParametrizado (Connection con, int institucion, int nroConsulta);

/**
 * Método para consultar los históricos de cada una de las secciones  de acuerdo al parámetro mandado
 * @param con
 * @param codigoPaciente
 * @param nroConsulta.  1=>Historico Estrabismo, 2=> Histórico Segmento Anterior
 * 							   				  3=>Histórico Retina y Vítreo, 4=> Histórico Orbita y anexos
 * @return Collection
 */

public Collection consultarTipoHistorico (Connection con, int codigoPaciente, int nroConsulta);

/**
 * Metodo para consultar la hoja oftalmológica del paciente cargado 
 * @param con
 * @param codigoPaciente
 * @return Collection con los datos de la Hoja Oftalmológica
 */
public Collection cargarHojaOftalmologica (Connection con, int codigoPaciente);

/**
 * Metodo para cargar la información del histórico de la sección Estrabismo, cuando seleccionan la 
 * fecha histórica
 * @param con
 * @param codigoHistorico
 * @return Collection con los datos del histórico de estrabismo
 */
public Collection cargarHistoricoEstrabismo (Connection con, int codigoHistorico);

/**
 * Metodo para cargar la información histórica del prisma cerca en la sección Estrabismo cuando seleccionan una 
 * fecha histórica
 * @param con
 * @param codigoHistorico
 * @return Collection con los datos del histórico del prisma cerca
 */
public Collection cargarHistoricoPrismaCerca (Connection con, int codigoHistorico);

/**
 * Método que consulta un histyórico especifico dado coimo parámentro el codigo de encabezado
 * @param con
 * @param nroConsulta
 * @param codigoEncabezado
 * @return
 */
public Collection consultarHistoricoEspecifico(Connection con, int nroConsulta, int codigoEncabezado);

/**
 * Método que consulta el codigo del encabezado de una valoración específica para cada seccion
 * @param con
 * @param valoracion
 * @return
 */
public int consultarHistoricoHojaOftal(Connection con, int valoracion, int tipoConsulta);

}


