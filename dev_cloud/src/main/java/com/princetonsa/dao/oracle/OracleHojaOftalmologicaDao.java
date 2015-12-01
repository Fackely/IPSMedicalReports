/*
 * Creado en Sep 21, 2005
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.dao.HojaOftalmologicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseHojaOftalmologicaDao;

/**
 * @author Andr�s Mauricio Ruiz V�lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class OracleHojaOftalmologicaDao implements HojaOftalmologicaDao
{
	/**
	 * M�todo para insertar una hoja oftalmol�gica
	 * @param con
	 * @param codigoPaciente
	 * @param observacionEstrabismo
	 * @param observacionSegmentoAnt
	 * @param observacionRetinaVitreo
	 * @param observacionOrbitaAnexos
	 * @return codigoHojaOftalmologica
	 */	
	public int insertarHojaOftalmologica(Connection con, int codigoPaciente,
																		  String observacionEstrabismo, String observacionSegmentoAnt,
																		  String observacionRetinaVitreo, String observacionOrbitaAnexos)
	{
		return SqlBaseHojaOftalmologicaDao.insertarHojaOftalmologica(con, codigoPaciente, observacionEstrabismo, observacionSegmentoAnt, observacionRetinaVitreo, observacionOrbitaAnexos);
	}
	
	/**
	 * Metodo para insertar el encabezado historico de la hoja oftalmol�gica  
	 * @param con
	 * @param codHojaOftalmologica
	 * @param datosMedico
	 * @return codEncabezadoHisto
	 */ 
	public int insertarEncabezadoHistoHojaOftalmologica(Connection con, int codHojaOftalmologica, String datosMedico, int numeroSolicitud)
	{
		return SqlBaseHojaOftalmologicaDao.insertarEncabezadoHistoHojaOftalmologica(con, codHojaOftalmologica, datosMedico, numeroSolicitud);
	}
	
	/**
	 * Metodo para insertar la secci�n estrabismo  
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
															 String amplitudFusionCercaMenos, String amplitudFusionLejosMas, String amplitudFusionLejosMenos, String prismaCompensadorLejos, String prismaCompensadorCerca)
	{
		return SqlBaseHojaOftalmologicaDao.insertarEstrabismo(con, codEncabezadoHisto, ppm, coverTestCercaSc, coverTestCercaCc, coverTestLejosSc, coverTestLejosCc, ojoFijador, ppcInstitucion, 
																													prismaCcLejos, prismaScLejos, duccionesVersiones, testVisionBinocular, estereopsis, amplitudFusionCercaMas, 
																													amplitudFusionCercaMenos, amplitudFusionLejosMas, amplitudFusionLejosMenos, prismaCompensadorLejos,prismaCompensadorCerca);
	}
	
	/**
	 * Metodo para insertar el prisma cerca en la secci�n estrabismo  
	 * @param con
	 * @param encaHistoEstrabismo
	 * @param seccion
	 * @param correccion
	 * @param valorPrismaCerca
	 * @return encaHistoEstrabismo
	 */
	public int insertarPrismaCerca (Connection con, int encaHistoEstrabismo, int seccion, boolean correccion, String valorPrismaCerca)
	{
		return SqlBaseHojaOftalmologicaDao.insertarPrismaCerca(con, encaHistoEstrabismo, seccion, correccion, valorPrismaCerca);
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
	public int insertarDetalleSegmentoAnt (Connection con, int codHistoSegmentoAnt, int segmentoAntInst, String valorOd, String valorOs)
	{
		return SqlBaseHojaOftalmologicaDao.insertarDetalleSegmentoAnt (con, codHistoSegmentoAnt, segmentoAntInst, valorOd, valorOs);
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
	public int insertarOrbitaAnexos (Connection con, int codigoHistorico, int orbitaAnexoInst, String valorOd, String valorOs)
	{
		return SqlBaseHojaOftalmologicaDao.insertarOrbitaAnexos (con, codigoHistorico, orbitaAnexoInst, valorOd, valorOs);
	}
	
	/**
	 * Metodo para insertar el Segmento anterior  
	 * @param con
	 * @param codHistorico
	 * @return codHistoSegAnt
	 */
	public int insertarSegmentoAnterior (Connection con, int codHistorico, String imagenOd, String imagenOs)
	{
		return SqlBaseHojaOftalmologicaDao.insertarSegmentoAnterior (con, codHistorico, imagenOd, imagenOs);
	}
	
    public int insertarRetinaVitreo(Connection con, int codHistorico, String imagenRetinaOD, String imagenRetinaOS, String imagenVitreoOD, String imagenVitreoOS)
    {
        return SqlBaseHojaOftalmologicaDao.insertarRetinaVitreo(con, codHistorico, imagenRetinaOD, imagenRetinaOS, imagenVitreoOD, imagenVitreoOS);
    }

    /**
	 * M�todo para consultar los tipos aplicables para la hoja oftalmol�gica seg�n la instituci�n
	 * @param con
	 * @param institucion
	 * @param tipo
	 * @return Collection
	 */
	public Collection consultarTipoParametrizado (Connection con, int institucion, int nroConsulta)
	{
		return SqlBaseHojaOftalmologicaDao.consultarTipoParametrizado(con, institucion, nroConsulta);
	}
	
	/**
	 * M�todo para consultar los hist�ricos de cada una de las secciones  de acuerdo al par�metro mandado
	 * @param con
	 * @param codigoPaciente
	 * @param nroConsulta.  1=>Historico Estrabismo, 2=> Hist�rico Segmento Anterior
	 * 							   				  3=>Hist�rico Retina y V�treo, 4=> Hist�rico Orbita y anexos
	 * @return Collection
	 */

	public Collection consultarTipoHistorico (Connection con, int codigoPaciente, int nroConsulta)
	{
		return SqlBaseHojaOftalmologicaDao.consultarTipoHistorico (con, codigoPaciente, nroConsulta);
	}
	
	/**
	 * Metodo para consultar la hoja oftalmol�gica del paciente cargado 
	 * @param con
	 * @param codigoPaciente
	 * @return Collection con los datos de la Hoja Oftalmol�gica
	 */
	public Collection cargarHojaOftalmologica (Connection con, int codigoPaciente)
	{
		return SqlBaseHojaOftalmologicaDao.cargarHojaOftalmologica (con, codigoPaciente);
	}
	
    /**
	 * Metodo para cargar la informaci�n del hist�rico de la secci�n Estrabismo, cuando seleccionan la 
	 * fecha hist�rica
	 * @param con
	 * @param codigoHistorico
	 * @return Collection con los datos del hist�rico de estrabismo
	 */
	public Collection cargarHistoricoEstrabismo (Connection con, int codigoHistorico)
	{
		return SqlBaseHojaOftalmologicaDao.cargarHistoricoEstrabismo (con, codigoHistorico);
	}
	
	/**
	 * Metodo para cargar la informaci�n hist�rica del prisma cerca en la secci�n Estrabismo cuando seleccionan una 
	 * fecha hist�rica
	 * @param con
	 * @param codigoHistorico
	 * @return Collection con los datos del hist�rico del prisma cerca
	 */
	public Collection cargarHistoricoPrismaCerca (Connection con, int codigoHistorico)
	{
		return SqlBaseHojaOftalmologicaDao.cargarHistoricoPrismaCerca (con, codigoHistorico);
	}
	
	/**
	 * M�todo que consulta un histy�rico especifico dado coimo par�mentro el codigo de encabezado
	 * @param con
	 * @param nroConsulta
	 * @param codigoEncabezado
	 * @return
	 */
	public Collection consultarHistoricoEspecifico(Connection con, int nroConsulta, int codigoEncabezado)
	{
		return SqlBaseHojaOftalmologicaDao.consultarHistoricoEspecifico(con, nroConsulta, codigoEncabezado);
	}

	/**
	 * M�todo que consulta el codigo del encabezado de una valoraci�n espec�fica para cada seccion
	 * @param con
	 * @param valoracion
	 * @return
	 */
	public int consultarHistoricoHojaOftal(Connection con, int valoracion, int tipoConsulta)
	{
		return SqlBaseHojaOftalmologicaDao.consultarHistoricoHojaOftal(con, valoracion, tipoConsulta);
	}

}
