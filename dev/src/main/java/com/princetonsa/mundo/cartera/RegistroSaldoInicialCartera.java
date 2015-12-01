
/*
 * Creado   10/06/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

import com.princetonsa.dao.CuentasCobroDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;

/**
 * Clase para manejar el registro de cuentas de
 * cobro para el módulo de cartera que constituyen 
 * las cuentas de cobro de saldos iniciales y 
 * relacionar a cada una de estas cuentas de cobro
 * una selección de facturas del sistema, permitiendo 
 * despues de seleccionarlas realizar modificación en
 * sus valores cartera.
 * Extiende de la superclase de CuentasCobro.
 *
 * @version 1.0, 10/06/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RegistroSaldoInicialCartera extends CuentasCobro 
{
    /**
	 * DAO de este objeto, para trabajar con la cuenta de cobro
	 * en la fuente de datos
	 */    
    private static CuentasCobroDao cuentasCobroDao;
	
	/**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void resetRegistro ()
    {
        super.reset();
    }
      
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( cuentasCobroDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cuentasCobroDao= myFactory.getCuentasCobroDao();			
			if( cuentasCobroDao!= null )
				return true;
		}

		return false;
	}
    
    /**
	 * Constructor
	 *
	 */
	public RegistroSaldoInicialCartera ()
	{
	    this.resetRegistro();
	    this.init(System.getProperty("TIPOBD"));//CuentasCobro
	    super.init(System.getProperty("TIPOBD"));//CuentasCobro
	}
	
	/**
	 * Metodo para verificar sin un código de cuenta de 
	 * cobro esta presente en BD.
	 * @param con Connection,conexión con la fuente de datos
	 * @param numeroCuentaCobro double, número de la cuenta de cobro
	 * @param codInstitucion in, código de la institución
	 * @author jarloc
	 * @return filas int, 
	 */
	public int existeCodigoCunetaDeCobro (Connection con,double numeroCuentaCobro,int codInstitucion)
	{
	    int filas=0;	    
	    ResultSetDecorator rs = cuentasCobroDao.existeCodigoCuentaCobroEnBD(con,codInstitucion,numeroCuentaCobro);
	    try 
	    {
            while(rs.next())
            {
                filas=rs.getInt("codigos");
            }
        } 
	    catch (SQLException e) 
	    {  
            e.printStackTrace();            
        }
        return filas;
	}
	
	/**
	 * Metodo para insertar facturas manualmente, externas
	 * al sistema.
	 * @param con Connection, conexión con la fuente de datos
	 * @param numeroCuentaCobro double, número de la cuenta de cobro
	 * @param tipoFact boolean, true si es del sistema
	 * @param valorCartera double, valor de la cartera factura
	 * @param institucion int, código de la institución
	 * @param fecha String, fecha de inserción
	 * @param estadoFact int, código del estado de la factura
	 * @param estadoPac int, código del estado del paciente
	 * @param usuario String, login del usuario
	 * @param consecutivoFact int, consecutivo de la factura, regulado por lo Dian
	 * @param valorTotal double, valor total de la factura
	 * @param valorConvenio double, valor del convenio
	 * @param codigoVia int, código de la vía de ingreso
	 * @param codigoConvenio int, código del convenio
	 * @author jarloc
	 * @return boolean, true efectivo.
	 */
	public int insertarFacturaManual (Connection con, 
													        double numeroCuentaCobro,
													        boolean tipoFact,
													        double valorCartera,
													        int institucion,
													        String fecha,
													        int estadoFact,
													        int estadoPac,
													        String usuario,
													        int consecutivoFact,
													        double valorTotal,
													        double valorConvenio,
													        int codigoVia,
													        int codigoConvenio,
													        int codigoCentroAtencion)
	{
	    InstitucionBasica inst=new InstitucionBasica();
	    inst.cargar(institucion, ConstantesBD.codigoNuncaValido);
	    DtoFactura dtoFactura = new DtoFactura ();
	    dtoFactura.setAjustesCredito(0);
	    dtoFactura.setAjustesDebito(0);
	    dtoFactura.setCentroAtencion(new InfoDatosInt(codigoCentroAtencion));
	    dtoFactura.setCodigoPaciente(ConstantesBD.codigoNuncaValido);
	    dtoFactura.setCodigoResonsableParticular(ConstantesBD.codigoNuncaValido);
	    dtoFactura.setConsecutivoFactura(consecutivoFact);
	    dtoFactura.setConvenio(new InfoDatosInt(codigoConvenio));
	    dtoFactura.setCuentaCobroCapitacion(ConstantesBD.codigoNuncaValidoDoubleNegativo);
	    Vector vectorCuentas= new Vector();
	    vectorCuentas.add(ConstantesBD.codigoNuncaValido);
	    dtoFactura.setCuentas(vectorCuentas);
	    dtoFactura.setDetallesFactura(new ArrayList<DtoDetalleFactura>());
	    dtoFactura.setDiagnosticoEgresoAcronimoTipoCie("");
	    dtoFactura.setEsParticular(false);
	    dtoFactura.setEstadoFacturacion(new InfoDatosInt(estadoFact));
	    dtoFactura.setEstadoPaciente(new InfoDatosInt(estadoPac));
	    dtoFactura.setEstratoSocial(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
	    dtoFactura.setFacturaCerrada(false);
	    dtoFactura.setFecha(fecha);
	    dtoFactura.setFormatoImpresion(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
	    dtoFactura.setHora("");
	    dtoFactura.setInstitucion(institucion);
	    dtoFactura.setLoginUsuario(usuario);
	    dtoFactura.setMontoCobro(ConstantesBD.codigoNuncaValido);
	    dtoFactura.setNroComprobante(ConstantesBD.codigoNuncaValidoDoubleNegativo);
	    dtoFactura.setNumeroCuentaCobro(numeroCuentaCobro);
	    dtoFactura.setPacienteTieneExcepcionNaturaleza(false);
	    dtoFactura.setPrefijoFactura("");
	    dtoFactura.setSubCuenta(ConstantesBD.codigoNuncaValidoDoubleNegativo);
	    dtoFactura.setTipoAfiliado("");
	    dtoFactura.setTipoComprobante("");
	    dtoFactura.setTipoFacturaSistema(tipoFact);
	    dtoFactura.setTipoMonto(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
	    dtoFactura.setTipoRegimen(new InfoDatos());
	    dtoFactura.setValorAbonos(ConstantesBD.codigoNuncaValidoDouble);
	    dtoFactura.setValorAFavorConvenio(ConstantesBD.codigoNuncaValidoDouble);
	    dtoFactura.setValorBrutoPac(ConstantesBD.codigoNuncaValidoDouble);
	    dtoFactura.setValorBrutoPacienteModificadoXAnioCalendario(false);
	    dtoFactura.setValorBrutoPacienteModificadoXEvento(false);
	    dtoFactura.setValorCartera(valorCartera);
	    dtoFactura.setValorConvenio(valorConvenio);
	    dtoFactura.setValorDescuentoPaciente(ConstantesBD.codigoNuncaValidoDouble);
	    dtoFactura.setValorNetoPaciente(ConstantesBD.codigoNuncaValidoDouble);
	    dtoFactura.setValorPagos(ConstantesBD.codigoNuncaValidoDouble);
	    dtoFactura.setValorTotal(valorTotal);
	    dtoFactura.setViaIngreso(new InfoDatosInt(codigoVia));
	    dtoFactura.setResolucionDian(inst.getResolucion());
	    dtoFactura.setRangoInicialFactura(inst.getRangoInicialFactura());
	    dtoFactura.setRangoFinalFactura(inst.getRangoFinalFactura());

	    return Factura.insertar(con, dtoFactura).getResultado();
	}
	
	/**
	 * consulta el proximo ó ultimo código de la
	 * factura.
	 * @param con Connection, conexión con la fuente de datos
	 * @see com.princetonsa.mundo.Facturacion.FacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
	 * @author jarloc
	 * @return proxFact int.
	 */
	//METODO NO USADO, LO LLAMAVAN EN EL ACTION PERO NO HACEN NADA CON EL, ADEMAS NO CONCUERDA CON LO DOCUMENTADO DE MULTIEMPRESA
	/*public int proximoCodigoFactura (Connection con, UsuarioBasico usuario)
	{
	    ValidacionesFactura factura = new ValidacionesFactura ();
	    int proxFact = factura.obtenerSiguientePosibleNumeroFactura(con, usuario.getCodigoInstitucionInt());
	    return proxFact;
	}*/
	
	/**
  	 * Metodo para consultar si una factura existe en BD.
  	 * @param con Connection, conexión con la fuente de datos
  	 * @param codigoFact in, código de la factura
  	 * @return int, 0 si no existe
  	 * @see com.princetonsa.mundo.Facturacion.FacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
  	 * @author jarloc
  	 */
  	public int existeCodigoFactura (Connection con, int codigoFact)
  	{
  	  return ValidacionesFactura.existeCodigoFactura(con,codigoFact);
  	}
  	
  	/**
	 * Metodo para verificar si una via de ingreso
	 * posee entrada en la tabla vias_ingreso_cxc
	 * @param con Connection, conexión con la fuente de datos
	 * @param institucion int, codigo de la institución
	 * @param viaIngreso int, código de la via de ingreso
	 * @param numCxC double, número de la cuenta de cobro
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCuentasCobroDao#existeViaIngresoCxCEnBD(Connection,int,int,double)
	 * @return ResultSet
	 * @author jarloc
	 */
	public int existeViaIngresoCxCEnBD (Connection con,int institucion,int viaIngreso,double numCxC)
	{
	    return cuentasCobroDao.existeViaIngresoCxCEnBD(con,institucion,viaIngreso,numCxC);
	}
	
	/**
	 * Consultar el consecutivo de la primera factura
	 * generada por el sistema
	 * @param con Connection, conexión con la fuente de datos	 
	 * @return int, -1 si hay error
	 * @see com.princetonsa.mundo.Facturacion.FacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
	 * @author jarloc
	 */
	public int primeraFacturaGeneradaSistema (Connection con)
	{
	    return ValidacionesFactura.primeraFacturaGeneradaSistema(con);
	}
	
}
