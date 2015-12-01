/*
 * @(#)BusquedaArticulosGenericaDao.java
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
 * de acceso a datos para el objeto <code>BusquedaArticulosGenerica</code>.
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 *  @version 1.0, 14/12/2005
 */
public interface BusquedaArticulosGenericaDao 
{
    /**
     * Metodo que realiza la busqueda avanzada de los articulos x codigos o descripcion
     * @param con
     * @param esBusquedaPorNombre
     * @param criterioBusqueda
     * @param codigosArticulosInsertados
     * @param filtrarXInventarios,
     * @param codigoAlmacen
     * @param codigoTransaccion
     * @param codigoInstitucion
     * @param parejasClaseGrupo
     * @param valorDefectoClasesInventario 
     * @param codigoEsquemaTarifario
     * @return
     */
    public ResultSetDecorator busquedaAvanzadaArticulos(    	Connection con,
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
	                                                boolean filrarXAjusteInvFis,
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
                                               ); 

}