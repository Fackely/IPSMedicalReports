/*
 * Creado en 2/07/2004
 *
 * Juan David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MontosCobroDao;

/**
 * @author Juan David Ramírez
 *
 */
public class MontosCobro
{
	/**
	 * Manejador de logs de la claes
	 */
	private Logger logger=Logger.getLogger(MontosCobro.class);
	
	/**
	 * Manejador del dao
	 */
	private MontosCobroDao montosCobroDao=null;
	
	/**
	 * Código del monto manejado
	 */
	private int codigo;
	
	/**
	 * Convenio utilizado en el monto
	 */
	private int convenio;
	
	/**
	 * Via de ingreso del paciente
	 */
	private int viaIngreso;
	
	/**
	 * Tipo afiliación para el cual existe el monto
	 */
	private String tipoAfiliado;
	
	/**
	 * Estrato para el cual existe el monto
	 */
	private int estratoSocial;
	
	/**
	 * Tipo de monto
	 */
	private int tipoMonto;
	
	/**
	 * Valor del monto
	 */
	private float valor;
	
	/**
	 * Porcentaje del monto
	 */
	private float porcentaje;
	
	/**
	 * Variable para almaena la fecha de vigencia
	 */
	private String fecha;
	
	/**
	 * Validez del monto
	 */
	private boolean activo;
	
	private String activoString;
	
	private String consultaSql="";
	/**
	 * @return Retorna activo.
	 */
	public boolean getActivo()
	{
		return activo;
	}
	
	/**
	 * @param activo Asigna activo.
	 */
	public void setActivo(boolean activo)
	{
		this.activo = activo;
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
	 * @return Retorna estrato.
	 */
	public int getEstratoSocial()
	{
		return estratoSocial;
	}
	
	/**
	 * @param estrato Asigna estrato.
	 */
	public void setEstratoSocial(int estrato)
	{
		this.estratoSocial = estrato;
	}
	
	/**
	 * @return Retorna porcentaje.
	 */
	public float getPorcentaje()
	{
		return porcentaje;
	}
	
	/**
	 * @param porcentaje Asigna porcentaje.
	 */
	public void setPorcentaje(float porcentaje)
	{
		this.porcentaje = porcentaje;
	}
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return Retorna tipoAfiliado.
	 */
	public String getTipoAfiliado()
	{
		return tipoAfiliado;
	}
	
	/**
	 * @param tipoAfiliado Asigna tipoAfiliado.
	 */
	public void setTipoAfiliado(String tipoAfiliado)
	{
		this.tipoAfiliado = tipoAfiliado;
	}
	
	/**
	 * @return Retorna tipoMonto.
	 */
	public int getTipoMonto()
	{
		return tipoMonto;
	}
	/**
	 * @param tipoMonto Asigna tipoMonto.
	 */
	public void setTipoMonto(int tipoMonto)
	{
		this.tipoMonto = tipoMonto;
	}
	
	/**
	 * @return Retorna valor.
	 */
	public float getValor()
	{
		return valor;
	}
	
	/**
	 * @param valor Asigna valor.
	 */
	public void setValor(float valor)
	{
		this.valor = valor;
	}
	
	/**
	 * @return Retorna viaIngreso.
	 */
	public int getViaIngreso()
	{
		return viaIngreso;
	}
	
	/**
	 * @param viaIngreso Asigna viaIngreso.
	 */
	public void setViaIngreso(int viaIngreso)
	{
		this.viaIngreso = viaIngreso;
	}

	public MontosCobro()
	{
		reset();
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
		if (myFactory != null)
		{
			montosCobroDao = myFactory.getMontosCobroDao();
		}
	}
	
	public void reset()
	{
		codigo=0;
		convenio=0;
		viaIngreso=0;
		tipoAfiliado="";
		estratoSocial=0;
		tipoMonto=0;
		valor=-1;
		porcentaje=-1;
		activo=false;
		activoString="";
		fecha="";
	}
	
	public boolean insertarMonto(Connection con)
	{
		if(!UtilidadValidacion.existeMontoEnBD(con, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo, fecha))
		{
			return montosCobroDao.insertarMonto(con, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo, fecha);
		}
		else
		{
			return false;
		}
	}
	
	public boolean modificarMonto(Connection con)
	{
		if(montosCobroDao.modificarMonto(con, codigo, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo,fecha))
		{
			return true;
		}
		else
		{
			logger.error("Hubo un error al insertar el monto");
			return false;
		}
	}
	
	public Collection bucarMontos(Connection con, boolean esModificar)
	{
		return montosCobroDao.buscarMontos(con, codigo, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activoString, esModificar, fecha);
	}

	/**
	 * Método para cargar un monto de cobro
	 * @param con Conexión con la BD
	 * @return true si fue existosa la carga del elemento
	 */
	public boolean cargar(Connection con)
	{
		Collection montoCol=montosCobroDao.cargar(con, this.codigo);
		if(!montoCol.isEmpty())
		{
			Iterator iterador=montoCol.iterator();
			if(iterador.hasNext())
			{
				HashMap montoFila=(HashMap)iterador.next();
				this.codigo=Integer.parseInt(montoFila.get("codigo")+"");
				this.convenio=Integer.parseInt(montoFila.get("convenio")+"");
				this.viaIngreso=Integer.parseInt(montoFila.get("viaingreso")+"");
				this.tipoAfiliado=montoFila.get("tipoafiliado")+"";
				this.estratoSocial=Integer.parseInt(montoFila.get("estratosocial")+"");
				this.tipoMonto=Integer.parseInt(montoFila.get("tipomonto")+"");
				String valorPorcentaje=montoFila.get("valor")+"";
				valorPorcentaje=(valorPorcentaje.equals("")||valorPorcentaje.equals("null"))?"0":valorPorcentaje;
				this.valor=Float.parseFloat(valorPorcentaje);
				valorPorcentaje=montoFila.get("porcentaje")+"";
				valorPorcentaje=(valorPorcentaje.equals("")||valorPorcentaje.equals("null"))?"0":valorPorcentaje;
				this.porcentaje=Float.parseFloat(valorPorcentaje);
				this.activo=UtilidadTexto.getBoolean(montoFila.get("activo")+"");
				this.fecha=UtilidadFecha.conversionFormatoFechaAAp(montoFila.get("fecha")+"");
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @return Retorna activoString.
	 */
	public String getActivoString()
	{
		return activoString;
	}
	/**
	 * @param activoString Asigna activoString.
	 */
	public void setActivoString(String activoString)
	{
		this.activoString = activoString;
	}
	
	/**
	 * Adición Sebastián
	 * Método para cargar los códigos de los montos de cobro de acuerdo
	 * a ciertos parámetros, estos parámetros de búsqueda pueden omitirse
	 * con 0 (Enteros) o "" (String)
	 * @param con
	 * @param convenio
	 * @param tipoAfiliado
	 * @param estrato
	 * @param viaIngreso
	 * @return
	 */
	public Collection consultarMontosCobro(Connection con,int convenio,String tipoAfiliado,int estrato,int viaIngreso){
		return montosCobroDao.consultarMontosCobro(con,convenio,tipoAfiliado,estrato,viaIngreso);
	}

	/* *
	 * Método que carga los montos de cobro según el convenio
	 * (Pagar 100% o Pagar 0%) 
	 * @param con
	 * @param contrato
	 */
/*	public static MontosCobro cargarMontoCobroOdontologiaSegunConvenio(Connection con, int contrato) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMontosCobroDao().cargarMontoCobroOdontologiaSegunConvenio(con, contrato);
	}*/
}
