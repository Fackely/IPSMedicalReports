/**
 * Juan David Ramírez 4/07/2006
 * Princeton S.A.
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.AjusteCxCDao;

/**
 * @author Juan David Ramírez
 *
 */
public class AjusteCxC
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AjusteCxC.class);
	/**
	 * Conexion con la fuente de datos
	 */
	private AjusteCxCDao ajusteCxCDao;
	
	/**
	 * Código en BD
	 */
	private int codigo;
	
	/**
	 * Tipo de ajuste
	 */
	private int tipoAjuste;
	
	/**
	 * Número del ajuste 
	 */
	private int numero;
	
	/**
	 * Cuenta de cobro
	 */
	private int cuentaCobro;

	/**
	 * Fecha del ajuste
	 */
	private String fecha;
	
	/**
	 * Comnepto del ajuste
	 */
	private String concepto;
	
	/**
	 * Observaciones del ajuste
	 */
	private String observaciones;
	
	/**
	 * Convenio del ajuste
	 */
	private int convenio;
	
	/**
	 * Código del estado
	 */
	private int codigoEstado;
	
	/**
	 * Estado del ajuste
	 */
	private String estado;

	/**
	 * Saldo de la cuenta de cobro
	 */
	private double saldo;
	
	/**
	 * Valor del ajuste
	 */
	private double valorAjuste;
	
	/**
	 * Motivo de anulación
	 */
	private String motivoAnulacion;
	
	  /**
     * Fecha de la Radicacion Cuenta de Cobro si esta en estado radicada. 
     */
    private String fechaRadicacionCc; 

	
	/**
	 * Constructor
	 */
	public AjusteCxC()
	{
		ajusteCxCDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAjusteCxCDao();
		this.reset();
	}
	
	/**
	 * Método para resetear la clase
	 */
	public void reset()
	{
		this.codigo=0;
		this.tipoAjuste=0;
		this.numero=0;
		this.cuentaCobro=0;
		this.fecha="";
		this.convenio=0;
		this.concepto="";
		this.cuentaCobro=0;
		this.observaciones="";
		this.estado="";
		this.codigoEstado=0;
		this.saldo=0;
		this.valorAjuste=0;
		this.motivoAnulacion=null;
	}

	/*------------- Métodos -------------------------------------------*/
	
	/**
	 * Método para consultar las opciones de selección de la funcionalidad
	 * @param con
	 * @param tipoConsulta
	 * @param codigoAjuste
	 * @return
	 */
	public Collection consultarTipos(Connection con, int tipoConsulta, int codigoAjuste)
	{
		return ajusteCxCDao.consultarTipos(con, tipoConsulta, codigoAjuste);
	}

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param tipoAjuste
	 * @param consecutivo
	 * @return
	 */
	public String cargarAjuste(Connection con, int tipoAjuste, int consecutivo)
	{
		/*
			"	acxc.codigo AS codigo, " +
			"	acxc.tipo_ajuste AS tipo_ajuste, " +
			"	acxc.consecutivo AS consecutivo, " +
			"	acxc.fecha AS fecha, " +
			"	acxc.concepto AS concepto, " +
			"	acxc.observaciones AS observaciones, " +
			"	cc.numero_cuenta_cobro AS cuenta_cobro, " +
			"	cc.convenio AS convenio " +

		 */
		HashMap mapa=ajusteCxCDao.cargarAjuste(con, tipoAjuste, consecutivo);
		if(mapa==null)
		{
			return "errors.problemasBd";
		}
		if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
		{
			this.codigoEstado=Integer.parseInt(mapa.get("estado")+"");
			this.estado=mapa.get("nombre_estado")+"";
			if(codigoEstado==ConstantesBD.codigoEstadoAjusteCxCPendiente)
			{
				//logger.info("codigop "+mapa.get("codigo")+"");
				this.codigo=Integer.parseInt(mapa.get("codigo")+"");
				this.tipoAjuste=Integer.parseInt(mapa.get("tipo_ajuste")+"");
				this.numero=Integer.parseInt(mapa.get("consecutivo")+"");
				this.fecha=UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha")+"");
				this.concepto=(String)mapa.get("concepto");
				this.observaciones=mapa.get("observaciones")+"";
				this.cuentaCobro=Integer.parseInt(mapa.get("cuenta_cobro")+"");
				this.convenio=Integer.parseInt(mapa.get("convenio")+"");
				this.saldo=Double.parseDouble(mapa.get("saldo")+"");
				this.valorAjuste=Double.parseDouble(mapa.get("valor")+"");
				
				return "";
			}
			else
			{
				return "error.ajustesCxC.estado";
			}
		}
		return "error.ajustesCxC.noExiste";
	}

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String cargarAjuste(Connection con, int codigo)
	{
		/*
			"	acxc.codigo AS codigo, " +
			"	acxc.tipo_ajuste AS tipo_ajuste, " +
			"	acxc.consecutivo AS consecutivo, " +
			"	acxc.fecha AS fecha, " +
			"	acxc.concepto AS concepto, " +
			"	acxc.observaciones AS observaciones, " +
			"	cc.numero_cuenta_cobro AS cuenta_cobro, " +
			"	cc.convenio AS convenio " +

		 */
		HashMap mapa=ajusteCxCDao.cargarAjuste(con, codigo);
		if(mapa==null)
		{
			return "errors.problemasBd";
		}
		if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
		{
			this.codigoEstado=Integer.parseInt(mapa.get("estado")+"");
			this.estado=mapa.get("nombre_estado")+"";
			this.codigo=Integer.parseInt(mapa.get("codigo")+"");
			this.tipoAjuste=Integer.parseInt(mapa.get("tipo_ajuste")+"");
			this.numero=Integer.parseInt(mapa.get("consecutivo")+"");
			this.fecha=UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha")+"");
			this.concepto=(String)mapa.get("concepto");
			this.observaciones=mapa.get("observaciones")+"";
			this.cuentaCobro=Integer.parseInt(mapa.get("cuenta_cobro")+"");
			this.convenio=Integer.parseInt(mapa.get("convenio")+"");
			this.saldo=Double.parseDouble(mapa.get("saldo")+"");
			this.valorAjuste=Double.parseDouble(mapa.get("valor")+"");
			this.motivoAnulacion=(String)mapa.get("motivo_anulacion");
			return "";
		}
		else
		{
			return "error.ajustesCxC.noExiste";
		}
	}

	/**
	 * Método Para cargar la cuenta de cobro para realizar el ajuste
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public String cargarCuentaCobro(Connection con, int cuentaCobro, int institucion)
	{
		HashMap mapa=ajusteCxCDao.cargarCuentaCobro(con, cuentaCobro, institucion);
		if(mapa==null)
		{
			return "errors.problemasBd";
		}
		else
		{
			//logger.info("mapa --> "+mapa);
			if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
			{
				convenio=Integer.parseInt(mapa.get("convenio")+"");
				this.cuentaCobro=Integer.parseInt(mapa.get("codigo")+"");
				saldo=Double.parseDouble(mapa.get("saldo")+"");
				fecha=UtilidadFecha.getFechaActual();
				return "";
			}
		}
		return "error.ajustesCxC.noExisteCC";
	}

	/**
	 * Método Para cargar El codigo y Nombre del Ajuste de una Cuenta de Cobro Especifica.   
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public String verificarCuentaCobro(Connection con,int tipoConsulta, int cuentaCobro, int institucion)
	{
		HashMap mapa=ajusteCxCDao.verificarCuentaCobro(con, tipoConsulta, cuentaCobro, institucion);

		logger.info(" mapa (verificarCuentaCobro) -->  [" + mapa + "]");
		
		if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
		{
			if ( tipoConsulta == 1 )
			{
				return "error.ajustesCxC.ajustePendienteCc" + ConstantesBD.separadorSplit + Integer.parseInt(mapa.get("codigo_ajuste")+"") + " Tipo Ajuste ( " + mapa.get("tipo_ajuste") + ")";
			}
			if ( tipoConsulta == 2 )
			{
				return mapa.get("fecha")+"";
			}
		}
		
		return "";
	}

	/**
	 * Método Para Verficicar las Fechas del Cierre Cartera Capitacion.   
	 * @param con
	 * @param fechaGeneracionAjuste
	 * @param institucion 
	 * @return
	 */
	public String verificarFechaCierre(Connection con, int tipoFecha, String fechaGeneracionAjuste, int institucion)
	{
		HashMap mapa=ajusteCxCDao.verificarFechaCierre(con, tipoFecha, fechaGeneracionAjuste, institucion);

		logger.info(" mapa (verificarCuentaCobro) -->  [" + mapa + "]");
		
		if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
		{
			if ( tipoFecha == 0 )
				return  mapa.get("fecha_cierre") + "";
			if ( tipoFecha == 1 )
				return  mapa.get("anio_cierre") + "";
		}
		
		return "";
	}
	
	/**
	 * Método para guardar el nuevo ajuste
	 * @param con
	 * @param usuario
	 * @param institucion
	 * @return
	 */
	public int guardar(Connection con, String usuario, int institucion,int consecutivo)
	{
		return ajusteCxCDao.guardar(con, tipoAjuste, fecha, cuentaCobro, concepto, valorAjuste, observaciones, usuario, institucion,consecutivo);
	}

	/**
	 * Método para modificar el ajuste
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return ajusteCxCDao.modificar(con, codigo, fecha, concepto, valorAjuste, observaciones);
	}

	/**
	 * Método para anular el ajuste
	 * @param con
	 * @param motivoAnulacion
	 * @param loginUsuario
	 */
	public int anular(Connection con, int codigo, String motivoAnulacion, String loginUsuario)
	{
		return ajusteCxCDao.anular(con, codigo, motivoAnulacion, loginUsuario);
	}

	/**
	 * Método para cargar los cargues del ajuste
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap cargarCargues(Connection con, int cuentaCobro,  int ajuste,  int institucion)
	{
		return ajusteCxCDao.cargarCargues(con, cuentaCobro,  ajuste, institucion);
	}

	/**
	 * Método para guardar un detalle de un ajuste
	 * para un cargue
	 * @param con
	 * @param codigoCargue
	 * @param valor
	 * @param conceptoDetalle
	 * @param institucion
	 * @param esModificacion
	 * @return
	 */
	public int detalleAjuste(Connection con, int codigoCargue, double valor, String conceptoDetalle, int institucion, boolean esModificacion)
	{
		return ajusteCxCDao.detalleAjuste(con, codigoCargue, valor, codigo, conceptoDetalle, institucion, esModificacion);
	}

	/**
	 * Método que retorna el detalle de los ajustes
	 * @param con
	 * @param codigoAjuste
	 * @param mostrarTodos
	 * @return
	 */
	public HashMap consultarDetalleAjustes(Connection con, int codigoAjuste, boolean mostrarTodos)
	{
		return ajusteCxCDao.consultarDetalleAjustes(con, codigoAjuste, mostrarTodos);
	}

	/**
	 * Método para eliminar el detalle de los cargues existentes
	 * @param con
	 * @return
	 */
	public boolean eliminarDetalleAjustes(Connection con)
	{
		return ajusteCxCDao.eliminarDetalleAjustes(con, codigo);
	}

	/*------------- Getter y setters ---------------------------*/
	/**
	 * @return Retorna cuentaCobro.
	 */
	public int getCuentaCobro()
	{
		return cuentaCobro;
	}

	/**
	 * @param cuentaCobro Asigna cuentaCobro.
	 */
	public void setCuentaCobro(int cuentaCobro)
	{
		this.cuentaCobro = cuentaCobro;
	}

	/**
	 * @return Retorna numero.
	 */
	public int getNumero()
	{
		return numero;
	}

	/**
	 * @param numero Asigna numero.
	 */
	public void setNumero(int numero)
	{
		this.numero = numero;
	}

	/**
	 * @return Retorna tipoAjuste.
	 */
	public int getTipoAjuste()
	{
		return tipoAjuste;
	}

	/**
	 * @param tipoAjuste Asigna tipoAjuste.
	 */
	public void setTipoAjuste(int tipoAjuste)
	{
		this.tipoAjuste = tipoAjuste;
	}

	/**
	 * @return Retorna codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna codigo.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Retorna fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * @param fecha Asigna fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * @return Retorna concepto.
	 */
	public String getConcepto()
	{
		return concepto;
	}

	/**
	 * @param concepto Asigna concepto.
	 */
	public void setConcepto(String concepto)
	{
		this.concepto = concepto;
	}

	/**
	 * @return Retorna observaciones.
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * @param observaciones Asigna observaciones.
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * @return Retorna codigoEstado.
	 */
	public int getCodigoEstado()
	{
		return codigoEstado;
	}

	/**
	 * @param codigoEstado Asigna codigoEstado.
	 */
	public void setCodigoEstado(int codigoEstado)
	{
		this.codigoEstado = codigoEstado;
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna convenio.
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * @param convenio Asigna convenio.
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @return Retorna valorAjuste.
	 */
	public double getValorAjuste()
	{
		return valorAjuste;
	}

	/**
	 * @param valorAjuste Asigna valorAjuste.
	 */
	public void setValorAjuste(double valorAjuste)
	{
		this.valorAjuste = valorAjuste;
	}

	/**
	 * @return Retorna motivoAnulacion.
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion Asigna motivoAnulacion.
	 */
	public void setMotivoAnulacion(String motivoAnulacion)
	{
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return Retorna saldo.
	 */
	public double getSaldo()
	{
		return saldo;
	}

	/**
	 * @param saldo Asigna saldo.
	 */
	public void setSaldo(double saldo)
	{
		this.saldo = saldo;
	}

	/**
	 * @return Retorna fechaRadicacionCc.
	 */
	public String getFechaRadicacionCc() {
		return fechaRadicacionCc;
	}

	/**
	 * @param Asigna fechaRadicacionCc.
	 */
	public void setFechaRadicacionCc(String fechaRadicacionCc) {
		this.fechaRadicacionCc = fechaRadicacionCc;
	}

}
