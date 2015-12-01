/*
 * @(#)PostgresqlBusquedaServiciosGenericaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.BusquedaServiciosGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaServiciosGenericaDao;

/**
 * Implementación postgres de las funciones de acceso a la fuente de datos
 * para busqueda de servicios generica
 *
 * @version 1.0, Oct  31 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PostgresqlBusquedaServiciosGenericaDao implements BusquedaServiciosGenericaDao
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
													  )
    {
        return SqlBaseBusquedaServiciosGenericaDao.busquedaAvanzadaServiciosXCodigos(con, codigo, descripcionServicio, codigoSexo, codigosServiciosInsertados, filtrarTipoServicio, codigoContrato, tipoTarifario, filtrarNopos, codigoAxioma, codigoFormulario,atencionOdontologica,tipoAtencion,nombreForma,codigoPrograma,filtrarNaturalezaServicio);
    }
}