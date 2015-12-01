/*
 * Created on 31-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.Collection;

import com.princetonsa.dao.SustitutosInventariosDao;
import com.princetonsa.dao.sqlbase.SqlBaseSustitutosInventariosDao;

/**
 * Esta clase implementa el contrato estipulado en 
 * <code>RegistrosDiagnosticosDao</code>, proporcionando 
 * los servicios de acceso a una base de datos PostgreSQL 
 * requeridos por la clase <code>RegistroDiagnosticos</code>
 * 
 * @author armando
 *
 * Princeton 31-ago-2004
 */
public class PostgresqlSustitutosInventariosDao implements SustitutosInventariosDao 
{

    public int insertarSustitutoInventario(Connection con, int codigoPrincipal,int codigoSustituto) 
    {
       return SqlBaseSustitutosInventariosDao.insertarSustitutosInventarios(con,codigoPrincipal,codigoSustituto);
    }
    public int modificarSustitutoInventario(Connection con,int codigoPrincipal,int codigoSustituto,int codigoSustitutoOld)
    {
        return SqlBaseSustitutosInventariosDao.modificarSustitutosInventarios(con,codigoPrincipal,codigoSustituto,codigoSustitutoOld);
    }
    public int eliminarSustitutoInventario(Connection con,int codigoPrincipal,int codigoSustituto)
    {
        return SqlBaseSustitutosInventariosDao.eliminarSustitutosInventarios(con,codigoPrincipal,codigoSustituto);
    }
    public Collection consultarSustitutoInventario(Connection con,int codigoPrincipal)
    {
        return SqlBaseSustitutosInventariosDao.consultarSustitutosInventarios(con,codigoPrincipal);
    }
    public ResultSetDecorator consultarSustitutoInventarioEspecifico(Connection con,int codigoPrincipal,int codigoSustituto)
    {
        return SqlBaseSustitutosInventariosDao.consultarSustitutoInventarioEspecifico(con,codigoPrincipal,codigoSustituto);
    }
    

}
