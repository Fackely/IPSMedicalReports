// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConsultaFacturacion.java

package util.interfaces.scriptFacturacion;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class ConsultaFacturacion
{

    public ConsultaFacturacion()
    {
    }

    public static void generarArchivos(String tipoPar, String fechaIniPar, String fechaFinPar, String rutaPar, String driver, String databaseURL, String user, String password)
    {
        String tipo = tipoPar;
        String fInicial = fechaIniPar;
        String fFinal = fechaFinPar;
        String ruta = rutaPar;
        String fMesInicialTemp = fInicial.split("-")[1];
        String fAnioInicialTemp = fInicial.split("-")[0];
        String tipoConsulta = tipo.equalsIgnoreCase("HO") ? "HO" : "AM";
        Date fecha = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String fechaHoraActual = dateFormatter.format(fecha);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_cfa", false, consultaArchivo_trans_cfa.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_fac", false, consultaArchivo_trans_fac.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_fac", true,  consultaArchivo_trans_fac_copago.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_ing", false, consultaArchivo_trans_ing.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_carg", false, consultaArchivo_trans_carg.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        String consultaArmadaTransDet = armarConsultaTransDet(tipoConsulta, fInicial, fFinal, ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_det", false, consultaArmadaTransDet.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        String consultaArmadaTransDetCopago = armarConsultaTransDetCopago(tipoConsulta, fInicial, fFinal, ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "trans_det", true, consultaArmadaTransDetCopago.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("tipodeconsulta", tipoConsulta), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "ivdro", false, consultaArchivo_ivdro, ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "ivdrodet", false, consultaArchivo_ivdrodet.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("variable_anio_fecha_inicial_parametro", fAnioInicialTemp), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "rec_caj_ban", false, consultaArchivo_rec_caj_ban.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("variable_anio_fecha_inicial_parametro", fAnioInicialTemp), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "enc_caj_banc", false, consultaArchivo_enc_caj_banc.replace("variable_mes_fecha_inicial_parametro", (new StringBuilder(String.valueOf(fMesInicialTemp))).toString()).replace("variable_anio_fecha_inicial_parametro", fAnioInicialTemp), ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "det_caj_ban", false, consultaArchivo_det_caj_ban, ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "log_tras_fac", false, consultaArchivo_log_tras_fac, ruta, driver, databaseURL, user, password);
        generacionArchivo(tipoConsulta, fInicial, fFinal, fechaHoraActual, "log_trans_rc", false, consultaArchivo_log_trans_rc, ruta, driver, databaseURL, user, password);
    }

    private static int numeroServiciosArticulosTransDet(String tipoConsulta, String fInicial, String fFinal, String ruta, String driver, String databaseURL, String user, String password)
    {
        int contador = 0;
        String subConsulta = (new StringBuilder("select count(1) as contadorservicios from facturas f  inner join det_factura_solicitud dfs on (f.codigo=dfs.factura)  inner join solicitudes s on(dfs.solicitud=s.numero_solicitud)  inner join centros_costo cc on(s.centro_costo_solicitante=cc.codigo)  inner join cuentas c on f.cuenta=c.id  inner join convenios conv on conv.codigo=f.convenio  where f.fecha between '")).append(fInicial).append("' and '").append(fFinal).append("' ").toString();
        if(tipoConsulta.equalsIgnoreCase("HO"))
            subConsulta = (new StringBuilder(String.valueOf(subConsulta))).append(" and f.via_ingreso=1").toString();
        else
            subConsulta = (new StringBuilder(String.valueOf(subConsulta))).append(" and f.via_ingreso<>1").toString();
        try
        {
            Connection con = abrirConexion(driver, databaseURL, user, password);
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(subConsulta));
            ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                contador = rs.getInt("contadorservicios");
            rs.close();
            ps.close();
            con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return contador;
    }

    private static String armarConsultaTransDet(String tipoConsulta, String fInicial, String fFinal, String ruta, String driver, String databaseURL, String user, String password)
    {
        int contadorServiciosArticulos = numeroServiciosArticulosTransDet(tipoConsulta, fInicial, fFinal, ruta, driver, databaseURL, user, password);
        String subConsulta = "";
        if(contadorServiciosArticulos == 0)
            subConsulta = " 0 ";
        else
            subConsulta = (new StringBuilder(" f.valor_bruto_pac / ")).append(contadorServiciosArticulos).append(" ").toString();
        String consultaArchivo_trans_det = (new StringBuilder(" select   'tipodeconsulta' as detcen, 'variable_mes_fecha_inicial_parametro' as dettra, \tcase \t\twhen f.via_ingreso=1 then \t\t\t case when ca.codigo in('2','3','4','5','7','00') then '22' else '19' end \t\telse \t\t\t case when ca.codigo in('2','3','4','5','7','00') then '21' else '19' end \tend as detfue, f.consecutivo_factura as detdoc, to_char(f.fecha,'yyyy') as detano, to_char(f.fecha,'mm') as detmes, to_char(f.fecha,'yyyy-mm-dd') as detfec, CASE WHEN dfs.servicio is not null THEN (select codigo_propietario from referencias_servicio where referencias_servicio.tipo_tarifario=0 and referencias_servicio.servicio=dfs.servicio) ||''  ELSE  'A'  END  as detcon, cc.identificador as detcco, '0' as detnit, case when upper(conv.tipo_regimen)='P' then ( case when f.tipo_monto='1' then (dfs.valor_total*f.porcentaje_monto/100) else ( (dfs.valor_total*f.porcentaje_monto/100) - (")).append(subConsulta).append(") ").append(") end ").append(") ").append("else ").append("( ").append("case when f.tipo_monto='1' then ").append("( ").append("dfs.valor_total-(dfs.valor_total*f.porcentaje_monto/100)").append(") ").append("else").append("( ").append("(dfs.valor_total-(dfs.valor_total*f.porcentaje_monto/100)) - (").append(subConsulta).append(") ").append(") end ").append(") end as detval,").append(" '0.00' as detvde,").append(" '0.00' as detdfa,").append(" '0.00' as detdes,").append(" case when f.estado_facturacion||''='1' then '0' else '1' end as detanu ").append(" from facturas f ").append(" inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) ").append(" inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) ").append(" inner join centros_costo cc on(s.centro_costo_solicitante=cc.codigo) ").append(" inner join cuentas c on f.cuenta=c.id ").append(" inner join convenios conv on conv.codigo=f.convenio ").append(" left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten) ").append(" where 1=1 ").toString();
        return consultaArchivo_trans_det;
    }

    private static int numeroServiciosArticulosTransDetCopago(String tipoConsulta, String fInicial, String fFinal, String ruta, String driver, String databaseURL, String user, String password)
    {
        int contador = 0;
        String subConsulta = "select count(1) as contadorservicios from facturas f  inner join det_factura_solicitud dfs on (f.codigo=dfs.factura)  inner join solicitudes s on(dfs.solicitud=s.numero_solicitud)  inner join centros_costo cc on(s.centro_costo_solicitante=cc.codigo)  inner join cuentas c on f.cuenta=c.id  inner join convenios conv on conv.codigo=f.convenio  where f.valor_neto_paciente>0 and conv.tipo_regimen<>'P' ";
        if(tipoConsulta.equalsIgnoreCase("HO"))
            subConsulta = (new StringBuilder(String.valueOf(subConsulta))).append(" and f.via_ingreso=1").toString();
        else
            subConsulta = (new StringBuilder(String.valueOf(subConsulta))).append(" and f.via_ingreso<>1").toString();
        try
        {
            Connection con = abrirConexion(driver, databaseURL, user, password);
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(subConsulta));
            ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                contador = rs.getInt("contadorservicios");
            rs.close();
            ps.close();
            con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return contador;
    }

    private static String armarConsultaTransDetCopago(String tipoConsulta, String fInicial, String fFinal, String ruta, String driver, String databaseURL, String user, String password)
    {
        int contadorServiciosArticulos = numeroServiciosArticulosTransDetCopago(tipoConsulta, fInicial, fFinal, ruta, driver, databaseURL, user, password);
        String subConsulta = "";
        if(contadorServiciosArticulos == 0)
            subConsulta = " 0 ";
        else
            subConsulta = (new StringBuilder(" f.valor_bruto_pac / ")).append(contadorServiciosArticulos).append(" ").toString();
        String consultaArchivo_trans_det_copago = (new StringBuilder(" select   'tipodeconsulta' as detcen, 'variable_mes_fecha_inicial_parametro' as dettra, \tcase \t\twhen f.via_ingreso=1 then \t\t\t case when ca.codigo in('2','3','4','5','7','00') then '22' else '19' end \t\telse \t\t\t case when ca.codigo in('2','3','4','5','7','00') then '21' else '19' end \tend as detfue, '7'||f.consecutivo_factura as detdoc, to_char(f.fecha,'yyyy') as detano, to_char(f.fecha,'mm') as detmes, to_char(f.fecha,'yyyy-mm-dd') as detfec, CASE WHEN dfs.servicio is not null THEN (select codigo_propietario from referencias_servicio where referencias_servicio.tipo_tarifario=0 and referencias_servicio.servicio=dfs.servicio) ||''  ELSE  'A'  END  as detcco, '0' as detnit, case when upper(conv.tipo_regimen)='P' then ( case when f.tipo_monto='1' then (dfs.valor_total*f.porcentaje_monto/100) else ( (dfs.valor_total*f.porcentaje_monto/100) - (")).append(subConsulta).append(") ").append(") end ").append(") ").append("else ").append("( ").append("case when f.tipo_monto='1' then ").append("( ").append("dfs.valor_total-(dfs.valor_total*f.porcentaje_monto/100)").append(") ").append("else").append("( ").append("(dfs.valor_total-(dfs.valor_total*f.porcentaje_monto/100)) - (").append(subConsulta).append(") ").append(") end ").append(") end as detval,").append(" '0' as detvde,").append(" '0' as detdfa,").append(" '0' as detdes,").append(" case when f.estado_facturacion||''='1' then '0' else '1' end as detanu ").append(" from facturas f ").append(" inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) ").append(" inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) ").append(" inner join centros_costo cc on(s.centro_costo_solicitante=cc.codigo) ").append(" inner join cuentas c on f.cuenta=c.id ").append(" inner join convenios conv on conv.codigo=f.convenio ").append(" left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten) ").append(" where f.valor_neto_paciente>0 and conv.tipo_regimen<>'P' ").toString();
        return consultaArchivo_trans_det_copago;
    }

    private static void generacionArchivo(String tipoConsulta, String fInicial, String fFinal, String fechaHoraActual, String nombreArchivo, boolean concatenarArchivo, String cadenaConsulta, String ruta, 
            String driver, String databaseURL, String user, String password)
    {
        try
        {
            String cadena = (new StringBuilder(String.valueOf(cadenaConsulta))).append(" and f.fecha between '").append(fInicial).append("' and '").append(fFinal).append("' ").toString();
            if(tipoConsulta.equalsIgnoreCase("HO"))
                cadena = (new StringBuilder(String.valueOf(cadena))).append(" and f.via_ingreso=1").toString();
            else
                cadena = (new StringBuilder(String.valueOf(cadena))).append(" and f.via_ingreso<>1").toString();
            Connection con = abrirConexion(driver, databaseURL, user, password);
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
            ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
            File archivo = new File((new StringBuilder(String.valueOf(ruta))).append("/").append(nombreArchivo).append("_").append(fechaHoraActual).append(".txt").toString());
            FileWriter arWriter = new FileWriter(archivo, concatenarArchivo);
            BufferedWriter buWriter = new BufferedWriter(arWriter);
            boolean listo = false;
            ResultSetMetaData rsmd;
            for(; rs.next(); buWriter.write((new StringBuilder(String.valueOf(rs.getString(rsmd.getColumnCount())))).append("\n").toString()))
            {
                rsmd = rs.getMetaData();
                if(!listo)
                    listo = true;
                for(int i = 1; i < rsmd.getColumnCount(); i++)
                    buWriter.write((new StringBuilder(String.valueOf(rs.getString(i)))).append(",").toString());

            }

            buWriter.close();
            rs.close();
            ps.close();
            con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static Connection abrirConexion(String driver, String databaseURL, String user, String password)
        throws SQLException, ClassNotFoundException
    {
        Class.forName(driver);
        Connection con = DriverManager.getConnection(databaseURL, user, password);
        return con;
    }

    public static final String consultaArchivo_trans_cfa = "select  'tipodeconsulta' as cfacen, 'variable_mes_fecha_inicial_parametro' as cfatra, '19' as cfafue , consecutivo_factura as cfadoc, dfs.valor_total as cfaval, f.val_desc_pac as cfades, 'SN' as cfafuo, c.id_ingreso as cfadoo, '' as cfalin, case when f.estado_facturacion||''='1' then '0' else '1' end as cfaanu  from facturas f  inner join det_factura_solicitud dfs on (f.codigo=dfs.factura)  inner join cuentas c on f.cuenta=c.id  where 1=1 ";
    public static final String consultaArchivo_trans_fac = " select  'tipodeconsulta' as faccen, 'variable_mes_fecha_inicial_parametro' as factra, \tcase \t\twhen f.via_ingreso=1 then \t\t\t case when ca.codigo in('2','3','4','5','7','00') then '22' else '19' end \t\telse \t\t\t case when ca.codigo in('2','3','4','5','7','00') then '21' else '19' end \tend as facfue , consecutivo_factura||'' as facdoc, to_char(fecha,'yyyy')||'' as facano, to_char(fecha,'mm')||'' as facmes, to_char(fecha,'yyyy-mm-dd')||'' as facfec, '30' as facpla, 'SN' as movfuo, c.id_ingreso||'' as fachis, '1' as facnum, per.numero_identificacion||'' as facced, case when upper(conv.tipo_regimen)='P' then 'P' else 'E' end as facemp, '' as faccer, conv.nombre||'' as facres, 'R' as factip, case when f.via_ingreso=1 then 'H' else case when f.via_ingreso=3 then 'U' else  case when f.via_ingreso=2 or f.via_ingreso=4 then 'C' else '' end end end as factse, '0' as facinv, '01' as factar, case when upper(conv.tipo_regimen)='P' then valor_total||'' else (valor_total - (valor_total*porcentaje_monto/100)) ||'' end as facval, '0.00' as facva3, case when f.estado_facturacion||''='1' then '0' else '1' end as facanu, case  when ca.codigo='2' then '2901'  when ca.codigo='3' then '3901'  when ca.codigo='4' then '4901'  when ca.codigo='5' then '5901'  when ca.codigo='7' then '7901'  when ca.codigo='00' then '0011'  else 'A911' end as faccco, to_char(fecha+30,'mm')||'' as carfev, getnombrepersona(f.cod_paciente)||'' as facpac,  '' as faccfa,  '' as facnum  from  facturas f  inner join cuentas c on f.cuenta=c.id  inner join personas per on(per.codigo=f.cod_paciente)  inner join convenios conv on conv.codigo=f.convenio left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten)  where 1=1 ";
    public static final String consultaArchivo_trans_fac_copago = " select  'tipodeconsulta' as faccen,  'variable_mes_fecha_inicial_parametro' as factra,  '19' as facfue ,  case when (f.valor_bruto_pac<>f.valor_total) then '7'||f.consecutivo_factura else rc.numero_recibo_caja end as facdoc,  to_char(rc.fecha,'yyyy')||'' as facano,  to_char(rc.fecha,'mm')||'' as facmes,  to_char(rc.fecha,'yyyy-mm-dd')||'' as facfec,  '0' as facpla,  '01' as movfuo,  '' as fachis,  '1' as facnum,  per.numero_identificacion||'' as facced,  'P' as facemp,  getnombrepersona(f.cod_paciente)||'' as faccer,  '' as facres,  '' as factip,  '' as factse,  '' as facinv,  '' as factar,  case when upper(conv.tipo_regimen)='P' then f.valor_total||'' else f.valor_neto_paciente||'' end as facval,  '' as facva3,  case when f.estado_facturacion||''='1' then '0' else '1' end as facanu,  case  when ca.codigo='2' then '2901'  when ca.codigo='3' then '3901'  when ca.codigo='4' then '4901'  when ca.codigo='5' then '5901'  when ca.codigo='7' then '7901'  when ca.codigo='00' then '0011'  else 'A911' end as faccco, '' as carfev,  '' as facpac ,  '19' as faccfa,  f.consecutivo_factura  as facnum  from  facturas f   inner join cuentas c on f.cuenta=c.id  inner join personas per on(per.codigo=f.cod_paciente)  inner join convenios conv on conv.codigo=f.convenio  left outer join ( detalle_conceptos_rc dcrc inner join recibos_caja rc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja) ) on (dcrc.doc_soporte=f.consecutivo_factura)  left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten)  where f.valor_neto_paciente>0 and f.valor_bruto_pac<>f.valor_total ";
    public static final String consultaArchivo_trans_ing = "select  'tipodeconsulta' as ingcen, 'variable_mes_fecha_inicial_parametro' as ingtra, 'SN' as ingfuo, id_ingreso as inghis, case when f.via_ingreso=1 then to_char(ah.fecha_admision,'yyyy') else case when f.via_ingreso=3 then to_char(au.fecha_admision,'yyyy') else to_char(c.fecha_apertura,'yyyy') end end as ingano, case when f.via_ingreso=1 then to_char(ah.fecha_admision,'mm') else case when f.via_ingreso=3 then to_char(au.fecha_admision,'mm') else to_char(c.fecha_apertura,'mm') end end as ingmes, 'A911' as ingcco, case when f.via_ingreso=1 then to_char(ah.fecha_admision,'yyyy-mm-dd') else case when f.via_ingreso=3 then to_char(au.fecha_admision,'yyyy-mm-dd') else to_char(c.fecha_apertura,'yyyy-mm-dd') end end as ingfec,  case when upper(conv.tipo_regimen)='P' then 'P' else 'E' end as ingemp, per.tipo_identificacion as ingtid, per.numero_identificacion as ingced, per.numero_identificacion as ingnui, case when f.via_ingreso=1 then to_char(ah.hora_admision,'hh24:mi') else case when f.via_ingreso=3 then to_char(au.hora_admision,'hh24:mi') else to_char(c.hora_apertura,'hh24:mi') end end as inghor, '' as ingdoc, '' as ingva1, per.codigo_departamento_vivienda||per.codigo_ciudad_vivienda as inglug, to_char(per.fecha_nacimiento,'yyyy-mm-dd') as ingnac, per.primer_apellido as ingape, per.segundo_apellido as ingap2, per.primer_nombre||' '||per.segundo_nombre as ingnom, per.sexo as ingsex, per.telefono as ingtel, per.direccion as ingdir,  case when f.via_ingreso=1 then ah.codigo_medico||'' else case when f.via_ingreso=3 then au.codigo_medico||'' else '' end end as ingmed, case when f.via_ingreso=1 then (select codigo_especialidad from especialidades_medicos where activa_sistema=true and especialidades_medicos.codigo_medico=ah.codigo_medico "+ValoresPorDefecto.getValorLimit1()+" 1)||'' else case when f.via_ingreso=3 then (select codigo_especialidad from especialidades_medicos where activa_sistema=true and especialidades_medicos.codigo_medico=au.codigo_medico limit 1)||'' else '' end end as ingesp, case when c.indicativo_acc_transito is true then (select nro_poliza from sub_cuentas where sub_cuentas.ingreso=c.id_ingreso and nro_prioridad=1 limit 1) else '' end as ingpol,  case when f.via_ingreso=1 then 'H' else case when f.via_ingreso=3 then 'U' else  case when f.via_ingreso=2 or f.via_ingreso=4 then 'C' else '' end end end as ingsin , (select acronimo_diagnostico from val_diagnosticos inner join solicitudes on(val_diagnosticos.valoracion=solicitudes.numero_solicitud) where solicitudes.cuenta=c.id order by solicitudes.numero_solicitud limit 1) as ingdia, case when upper(conv.tipo_regimen)='P' then 'P' else 'E' end as ingind, '' as ingcer, case when f.cod_res_particular is null then getnombreconvenio(f.convenio) else getnombrepersona(f.cod_res_particular) end as ingres, '01' as ingtar, case when f.via_ingreso=1 then 'H' else case when f.via_ingreso=3 then 'U' else  case when f.via_ingreso=2 or f.via_ingreso=4 then 'C' else '' end end end as factse, i.nit as ingpaq, '11011' as ingmun, case when f.estado_facturacion||''='1' then '0' else '1' end as inganu, '0' as ingniv, '0' as ingni1, conv.tipo_regimen as ingtus, '' as ingva2, pac.zona_domicilio as ingzon, per.codigo_barrio_vivienda as ingbar  from facturas f  inner join cuentas c on f.cuenta=c.id  inner join personas per on(per.codigo=f.cod_paciente)  inner join pacientes pac on pac.codigo_paciente=c.codigo_paciente  inner join convenios conv on conv.codigo=f.convenio  inner join instituciones i on (f.institucion=i.codigo)  left outer join admisiones_hospi ah on(ah.cuenta=c.id)  left outer join admisiones_urgencias au on(au.cuenta=c.id)  where 1=1 ";
    public static final String consultaArchivo_trans_carg = "select  'tipodeconsulta' as carcen, 'variable_mes_fecha_inicial_parametro' as cartra, 'SN' as carfue,  c.id_ingreso as cardoc,  '' as carlin, to_char(s.fecha_solicitud,'yyyy') as carano, to_char(s.fecha_solicitud,'mm') as carmes, to_char(s.fecha_solicitud,'yyyy-mm-dd') as carfec, 'SN' as carfuo, c.id_ingreso as cardoo, '1' as carnum, '01' as cartar, case when f.via_ingreso=1 then 'H' else case when f.via_ingreso=3 then 'U' else  case when f.via_ingreso=2 or f.via_ingreso=4 then 'C' else '' end end end as cartse, '' as carcon, case when dfs.servicio is null then 'A' else 'P' end as carprc, case when dfs.servicio is not null then (select codigo_propietario from referencias_servicio where referencias_servicio.tipo_tarifario=0 and referencias_servicio.servicio=dfs.servicio)||'' else dfs.articulo||'' end as carcod, cc.identificador as carcco, '0' as carnit, cantidad_cargo as carcan, valor_cargo as carva4, 'N' as carfes, dfs.valor_total as cartot, '0.00' as carrec, (dfs.valor_total-(dfs.valor_total*f.porcentaje_monto/100)) as carvre, (dfs.valor_total*f.porcentaje_monto/100) as carvex, '0.00' as carvfa, '0.00' as cardfa, '0.00' as cardes, '0.00' as carva1,  '0.00' as carva2, '0.00' as carva3, 'R' as cartip, 'S' as carfac, 'CA' as carfte, c.id_ingreso as carft1, '' as carft2, case when f.estado_facturacion||''='1' then '0' else '1' end as caranu  from facturas f  inner join det_factura_solicitud dfs on (f.codigo=dfs.factura)  inner join cuentas c on f.cuenta=c.id  inner join solicitudes s on(dfs.solicitud=s.numero_solicitud)  inner join centros_costo cc on(s.centro_costo_solicitante=cc.codigo)  where 1=1 ";
    public static final String consultaArchivo_ivdro = "SELECT  case  when ca.codigo='2' then '57'  when ca.codigo='3' then '55'  when ca.codigo='4' then '56'  else 'SN' end as drofue, c.id_ingreso as drodoc, to_char(d.fecha,'yyyy') as droano, to_char(d.fecha,'mm') as dromes, to_char(d.fecha,'yyyy-mm-dd') as drofec, c.id_ingreso as drohis, '' as dronum, cc1.identificador as drocco, cc2.identificador as droccc, '' as drofde, '' as drodde, case when f.estado_facturacion||''='1' then '0' else '1' end as droanu  from facturas  f  inner join cuentas c on(f.cuenta=c.id)  inner join det_factura_solicitud dfs on (f.codigo=dfs.factura and dfs.articulo is not null)  inner join solicitudes s on(dfs.solicitud=s.numero_solicitud)  inner join despacho d on d.numero_solicitud=s.numero_solicitud  inner join centros_costo cc1 on (cc1.codigo=s.centro_costo_solicitado)  inner join centros_costo cc2 on (cc2.codigo=s.centro_costo_solicitante)  inner join articulo art on(art.codigo=dfs.articulo and art.naturaleza='09')  left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten)  where 1=1 ";
    public static final String consultaArchivo_ivdrodet = "select  case  when ca.codigo='2' then '57'  when ca.codigo='3' then '55'  when ca.codigo='4' then '56'  else 'SN' end as drodetfue, c.id_ingreso as drodetdoc, 0 as drodetite, 'variable_anio_fecha_inicial_parametro' as drodetano, 'variable_mes_fecha_inicial_parametro' as drodetmes, dfs.articulo as drodetart, va.unidad_medida as drodetuni, dfs.cantidad_cargo as drodetcan, dfs.valor_cargo as drodetpre, (dfs.cantidad_cargo*dfs.valor_cargo) as drodettot, (dfs.cantidad_cargo*dfs.valor_cargo) as drodetcos, 'R' as drodettip, 'S' as drodetfac, '0.00' as drodetiva, case when f.estado_facturacion||''='1' then '0' else '1' end as drodetanu  from facturas  f  inner join cuentas c on(f.cuenta=c.id)  inner join det_factura_solicitud dfs on (f.codigo=dfs.factura and dfs.articulo is not null)  inner join view_articulos va on (va.codigo=dfs.articulo and va.naturaleza='09') left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten)  where 1=1 ";
    public static final String consultaArchivo_rec_caj_ban = "select  '36' as carfue, rc.numero_recibo_caja as cardoc, '1' as carsec, 'variable_anio_fecha_inicial_parametro' as carano, 'variable_mes_fecha_inicial_parametro' as carmes, 'A911' as carcco, rc.fecha as carfec, to_char(rc.fecha+30,'mm') as carfev, 'SN' as carfuo, c.id_ingreso as carhis, '1' as carnum, case when f.via_ingreso=1 then 'H' else case when f.via_ingreso=3 then 'U' else  case when f.via_ingreso=2 or f.via_ingreso=4 then 'C' else '' end end end as cartse, per.numero_identificacion as carcep, getnombrepersona(f.cod_paciente) as carpac,  'P' as carind, per.numero_identificacion as carcep, getnombrepersona(f.cod_paciente) as carres, '19' as carfca, f.consecutivo_factura as carfac, f.valor_neto_paciente as carsal, f.valor_neto_paciente as carval, 0 as cargas, '' as carcon, 0 as carvlc, case when rc.estado||''='4' then '1' else '0' end as caranu  from facturas f  inner join cuentas c on(f.cuenta=c.id)  inner join personas per on(per.codigo=f.cod_paciente)   inner join detalle_conceptos_rc dcrc on(dcrc.doc_soporte=f.consecutivo_factura and dcrc.institucion=f.institucion)  inner join recibos_caja rc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja and dcrc.institucion=rc.institucion)  where 1=1 ";
    public static final String consultaArchivo_enc_caj_banc = "select  '36' as movfue, rc.numero_recibo_caja as movdoc, 'variable_anio_fecha_inicial_parametro' as movano, 'variable_mes_fecha_inicial_parametro' as movmes, rc.fecha as movfec, '' as movban, '' as movche, '105' as movven, case  when ca.codigo='2' then '2901'  when ca.codigo='3' then '3901'  when ca.codigo='4' then '4901'  when ca.codigo='5' then '5901'  when ca.codigo='7' then '7901'  when ca.codigo='00' then '0011'  else 'A911' end as movcco, per.numero_identificacion as movben, getnombrepersona(f.cod_paciente) as movnom, per.numero_identificacion as movnit, '' as movcoc, f.valor_neto_paciente as movval, 0 as movind, rc.usuario as movusu, case when rc.estado||''='4' then '1' else '0' end as movanu, to_char(f.fecha,'yyyy-mm-dd')||' '||to_char(f.hora,'hh24:mi:ss') as movfch, '' as movpro, '01' as movsed, 'P' as movemp  from facturas f  inner join personas per on(per.codigo=f.cod_paciente)   inner join detalle_conceptos_rc dcrc on (dcrc.doc_soporte=f.consecutivo_factura and dcrc.institucion=f.institucion)  inner join recibos_caja rc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja and dcrc.institucion=rc.institucion)  left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten)  where 1=1 ";
    public static final String consultaArchivo_det_caj_ban = "select  '36' as movdetfue, rc.numero_recibo_caja as movdetdoc, '1' as movdetlin, to_char(rc.fecha,'yyyy') as movdetano, to_char(rc.fecha,'mm') as movdetmes, to_char(rc.fecha,'yyyy-mm-dd') as movdetfec, 'CON' as movdetcob, case  when ca.codigo='2' then '2901'  when ca.codigo='3' then '3901'  when ca.codigo='4' then '4901'  when ca.codigo='5' then '5901'  when ca.codigo='7' then '7901'  when ca.codigo='00' then '0011'  else 'A911' end as movdetcco, case when dprc.forma_pago=3 then '99' else 'LT' end as movdetfpa, '' as movdetban, '' as movdetdpa,  '' as movdetcba, '' as movdetpla, '' as movdetpob, '' as movdetfdo, dprc.valor as movdetval, '' as movdetfco, '' as movdetdco, '999' as movdetbco, '11050101' as movdetcue, rc.caja as movdetori, rc.caja as movdetori, rc.caja as movdetcaj, 'M' as movdettip, case when rc.estado||''='4' then '1' else '0' end as movdetanu, '01' as movdetsed  from facturas f  inner join detalle_conceptos_rc dcrc on (dcrc.doc_soporte=f.consecutivo_factura and dcrc.institucion=f.institucion)  inner join recibos_caja rc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja and dcrc.institucion=rc.institucion)  inner join detalle_pagos_rc dprc on(dprc.numero_recibo_caja=rc.numero_recibo_caja and dprc.institucion=rc.institucion and (dprc.forma_pago=3 or dprc.forma_pago=4))  left outer join centro_atencion ca on (ca.consecutivo=f.centro_aten)  where 1=1 ";
    public static final String consultaArchivo_log_tras_fac = "select  f.usuario as logusu, f.usuario as logter, '' as logpro, 'Facturar' as logope, 'Fte Factura' as logde1, '19' as logva1, 'Nro Factura' as logde2, f.consecutivo_factura as logva2, '' as logde3, '' as logva3, '' as logreg, '|' as logtip, 'caenc' as logtab, to_char(f.fecha,'yyyy-mm-dd')||' '||to_char(f.hora,'hh24:mi:ss') as  logfec  from facturas f  where 1=1 ";
    public static final String consultaArchivo_log_trans_rc = "select  rc.usuario as logusu, rc.usuario as logter, '' as logpro, 'Facturar' as logope, 'Fte Recibo' as logde1, '36' as logva1, 'Nro Recibo' as logde2, rc.numero_recibo_caja as logva2,  'Nro Factura' as logde3, f.consecutivo_factura as logva3, '' as logreg, '|' as logtip, 'caenc' as logtab, to_char(f.fecha,'yyyy-mm-dd')||' '||to_char(f.hora,'hh24:mi:ss') as  logfec  from facturas f  inner join detalle_conceptos_rc dcrc on (dcrc.doc_soporte=f.consecutivo_factura and dcrc.institucion=f.institucion)  inner join recibos_caja rc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja and dcrc.institucion=rc.institucion)  where 1=1 ";
}
