/*
 * @(#)BusquedaServiciosGenericaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>BusquedaServiciosGenerica</code>.
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 *  @version 1.0, 31/10/2005
 */
public interface BusquedaServiciosGenericaDao 
{
	/**
     * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups
     * @param con
     * @param codigo
     * @param descripcionServicio
     * @param codigoSexo
     * @param codigosServiciosInsertados
     * @param filtrarTipoServicio
     * @param codigoContrato
     * @param tipoTarifario
     * @param filtrarNopos
     * @param codigoAxioma
     * @param codigoFormulario
     * @return
     */
    public ResultSetDecorator busquedaAvanzadaServiciosXCodigos( Connection con,
                                                        String codigo,
                                                        String descripcionServicio,
                                                        int codigoSexo,
                                                        String codigosServiciosInsertados,
                                                        String filtrarTipoServicio,
														int codigoContrato,
														String tipoTarifario,
														boolean filtrarNopos,
														String codigoAxioma,
														int codigoFormulario,
														boolean atencionOdontologica,														
														String tipoAtencion,
														String nombreForma,
														String codigoPrograma,
														String filtrarNaturalezaServicio
													  ) ;
}


