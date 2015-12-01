/*
 * @(#)OracleBusquedaArticulosGenericaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.BusquedaArticulosGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaArticulosGenericaDao;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para busqueda de articulos generica
 *
 * @version 1.0, Dic  14 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleBusquedaArticulosGenericaDao implements BusquedaArticulosGenericaDao
{
	/**
     * Metodo que realiza la busqueda avanzada de los articulos x codigos o descripcion
     * @param con
     * @param esBusquedaPorNombre
     * @param criterioBusqueda
     * @param codigosArticulosInsertados
     * @param filtrarXInventarios
     * @param codigoAlmacen
     * @param codigoTransaccion
     * @param codigoInstitucion 
     * @param parejasClaseGrupo
     * @param codigoEsquemaTarifario
     * @return
     */
    public ResultSetDecorator busquedaAvanzadaArticulos(    Connection con,
                                                                            boolean esBusquedaPorNombre,
                                                                            String criterioBusqueda,
                                                                            String codigosArticulosInsertados,
                                                                            boolean filtrarXInventarios,
                                                                            int codigoContrato,
                                                                            String codigoAlmacen,
                                                                            String codigoTransaccion,
                                                                            int codigoInstitucion,
																			String parejasClaseGrupo,
																			String esMedicamento,
																			String esPos,
																			String tipoConsignac,
																			boolean filtrarXSeccionSubseccion,
							                                                String codigosSecciones, //codigos secciones separadas por comas
							                                                String codigoSubseccion, //codigo de la subseccion
							                                                String codigoSeccion, //codigo de la seccion
							                                                boolean filtrarXClaseGrupoSub,
							                                                String codigoClase, //codigo Clase
							                                                String codigoGrupo, //codigo del grupo
							                                                String codigoSubgrupo, //codigo del subgrupo
							                                                boolean filtrarXPrepPen,
							                                                boolean filtrarXAjusteInvFis,
							                                                String tipoDispositivo,
                                                							String tipoAccesoVascular, 
							                                                boolean soloAlmacen,
							                                                int categoria,
							                                                String formaFarmaceutica,
							                                                String unidadMedida,
							                                                boolean valorDefectoClasesInventario,
							                                                String idTercero,
							                                                String esMedicamentoControlEspecial,
							                                                boolean cargarEquivalentes,
							                                                boolean aplicaCargosDirectos,
							                                                boolean atencionOdontologica
                                                                       ) 
    {
        return SqlBaseBusquedaArticulosGenericaDao.busquedaAvanzadaArticulos(con, esBusquedaPorNombre, criterioBusqueda, codigosArticulosInsertados, filtrarXInventarios, codigoContrato,codigoAlmacen, codigoTransaccion, codigoInstitucion,parejasClaseGrupo, esMedicamento, esPos, tipoConsignac,filtrarXSeccionSubseccion,codigosSecciones,codigoSubseccion,codigoSeccion, filtrarXClaseGrupoSub, codigoClase, codigoGrupo, codigoSubgrupo, filtrarXPrepPen, filtrarXAjusteInvFis, tipoDispositivo, tipoAccesoVascular,soloAlmacen, categoria, formaFarmaceutica, unidadMedida, valorDefectoClasesInventario, idTercero, esMedicamentoControlEspecial,cargarEquivalentes,aplicaCargosDirectos,atencionOdontologica);
    }
}