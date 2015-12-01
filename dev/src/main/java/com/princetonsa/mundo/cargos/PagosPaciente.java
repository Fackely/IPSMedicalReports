/*
 * Creado en 16/07/2004
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PagosPacienteDao;
import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author Juan David Ramírez López
 *
 */
public class PagosPaciente
{
	private int codigo;
	
	/**
	 * Acceso a la fuente de datos
	 */
	private PagosPacienteDao pagosPacienteDao;
	
	/**
	 * Campo entidad
	 */
	private String entidad;
	
	/**
	 * Tipo de monto
	 */
	private int tipoMonto;
	
	/**
	 * Documento de la entidad
	 */
	private String documento;
	
	/**
	 * Fecha del monto
	 */
	private String fecha;
	
	/**
	 * Diagnóstico para el cual se hizo el pago
	 */
	private String diagnostico;
	
	/**
	 * Tipo de cie del diagnóstico
	 */
	private int tipoCie;
	
	/**
	 * Descripcion del pago
	 */
	private String descripcion;
	
	/**
	 * Valor del pago
	 */
	private double valor;
	
	/**
	 * Origen del pago
	 */
	private int origen;
	
	/**
	 * Usuario que registró el pago
	 */
	private String usuario;
	
	/**
	 * Institución para la cual se realizó el pago
	 */
	private int institucion;
	
	/**
	 * Tipo de regimen del pago
	 */
	private String tipoRegimen;
	
	/**
	 * Cantidad de pagos acumulados de todo el año
	 * teniendo en cuenta el diagnóstico
	 * y el tipo de monto del paciente
	 */
	private double pagosAcumuladosXDiagnostico;

	/**
	 * Cantidad de pagos acumulados de todo el año
	 * para el tipo de monto correspondiente
	 * al paciente
	 */
	private double pagosAcumuladosTotalAnio;

	/**
	 * @return Returna el descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	/**
	 * @param Asigna el descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	/**
	 * @return Returna el diagnostico.
	 */
	public String getDiagnostico()
	{
		return diagnostico;
	}
	/**
	 * @param Asigna el diagnostico.
	 */
	public void setDiagnostico(String diagnostico)
	{
		this.diagnostico = diagnostico;
	}
	/**
	 * @return Returna el documento.
	 */
	public String getDocumento()
	{
		return documento;
	}
	/**
	 * @param Asigna el documento.
	 */
	public void setDocumento(String documento)
	{
		this.documento = documento;
	}
	/**
	 * @return Returna el entidad.
	 */
	public String getEntidad()
	{
		return entidad;
	}
	/**
	 * @param Asigna el entidad.
	 */
	public void setEntidad(String entidad)
	{
		this.entidad = entidad;
	}
	/**
	 * @return Returna el fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}
	/**
	 * @param Asigna el fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	/**
	 * @return Returna el institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param Asigna el institucion.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}
	/**
	 * @return Returna el origen.
	 */
	public int getOrigen()
	{
		return origen;
	}
	/**
	 * @param Asigna el origen.
	 */
	public void setOrigen(int origen)
	{
		this.origen = origen;
	}
	/**
	 * @return Returna el tipoCie.
	 */
	public int getTipoCie()
	{
		return tipoCie;
	}
	/**
	 * @param Asigna el tipoCie.
	 */
	public void setTipoCie(int tipoCie)
	{
		this.tipoCie = tipoCie;
	}
	/**
	 * @return Returna el tipoMonto.
	 */
	public int getTipoMonto()
	{
		return tipoMonto;
	}
	/**
	 * @param Asigna el tipoMonto.
	 */
	public void setTipoMonto(int tipoMonto)
	{
		this.tipoMonto = tipoMonto;
	}
	/**
	 * @return Returna el usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}
	/**
	 * @param Asigna el usuario.
	 */
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}
	/**
	 * @return Returna el valor.
	 */
	public double getValor()
	{
		return valor;
	}
	/**
	 * @param Asigna el valor.
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}
	
	/**
	 * Inicializar el Dao
	 *
	 */
	public void init()
	{
		String tipoBD=System.getProperty("TIPOBD")+"";
		DaoFactory daoFactory = DaoFactory.getDaoFactory(tipoBD);
		pagosPacienteDao = (PagosPacienteDao) daoFactory.getPagosPacienteDao();
	}
	
	/**
	 * Resetear la clase
	 */
	public PagosPaciente()
	{
		entidad="";
		tipoMonto=0;
		documento="";
		fecha="";
		diagnostico="";
		tipoCie=0;
		descripcion="";
		valor=0;
		origen=0;
		usuario="";
		institucion=0;
		tipoRegimen="";
		init();
	}
	
	/**
	 * Ingresar pagos por paciente
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente insertado
	 * @return true si el pago fue insertado correctamente
	 */
	public boolean insertar(Connection con, int codigoPaciente)
	{
		this.codigo=pagosPacienteDao.insertar(con, entidad, tipoMonto, documento,
								fecha, diagnostico, tipoCie, descripcion,
								valor, origen, usuario,
								institucion, codigoPaciente, tipoRegimen);
		return (codigo>0);
	}
	
	/**
	 * Método que devuelve el siguiente numero de transacción
	 * para los pagos temporales del paciente
	 * @param con
	 * @return
	 */
	public int siguienteNumeroTransaccion(Connection con)
	{
		return pagosPacienteDao.siguienteNumeroTransaccion(con);
	}
	
	/**
	 * Ingresar pagos por paciente
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente insertado
	 * @return true si el pago fue insertado correctamente
	 */
	public boolean insertarPagoTemporal(Connection con, int codigoPaciente, int numeroTransccion)
	{
		this.codigo=pagosPacienteDao.insertarPagoTemporal(con, tipoMonto,
				diagnostico, tipoCie,
				valor, institucion, codigoPaciente, tipoRegimen, numeroTransccion);
		return (codigo>0);
	}

	/**
	 * Inserta un tope del paciente en una transacción
	 * @param con, Connection con la fuente de datos
	 * @param codigoPaciente, código del paciente
	 * @param estado, estado de la transacción
	 * @return, 1 -0 
	 * @throws SQLException
	 */
	 public int insertarTopesPacienteTransaccional (Connection con, int codigoPaciente, String estado) throws SQLException
	 {
	      int numElementosInsertados=0;
	      DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!myFactory.beginTransaction(con))
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      try
	      {
	          numElementosInsertados=pagosPacienteDao.insertar(	con, this.getEntidad(), this.getTipoMonto(), this.getDocumento(), 
	                  																			this.getFecha(), this.getDiagnostico(), this.getTipoCie(), 
	                  																			this.getDescripcion(), this.getValor(), this.getOrigen(), this.getUsuario(),
	                  																			this.getInstitucion(), codigoPaciente, this.tipoRegimen);
	          
	          if (numElementosInsertados<=0)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	          throw e;
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return numElementosInsertados;
	 }
	
	 /**
	  * Método que actualiza la información de anulación de los pagos de una paciente dada la factura en una transaccion
	  * @param con
	  * @param anulado
	  * @param loginUsuario
	  * @param codigoInstitucion
	  * @param origenPago
	  * @param codigoDIANFactura
	  * @param estado
	  * @return
	  */
	public boolean  actuallizarAnulacionPagosPacienteDadoFacturaTransaccional(	Connection con, 
																				String anulado, 
																				String loginUsuario, 
																				int codigoInstitucion,  
																				int origenPago, 
																				String codigoDIANFactura, 
																				String estado)throws SQLException
	{
	    boolean inserto=false;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	        if (!myFactory.beginTransaction(con))
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	        inserto=pagosPacienteDao.actuallizarAnulacionPagosPacienteDadoFactura(con, anulado, loginUsuario, codigoInstitucion, origenPago, codigoDIANFactura);
	        if (!inserto)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return inserto;
	}
	
	/**
	 * Realizar la búsqueda avanzada
	 * @param con
	 * @return collecion con los resultados de la búsqueda
	 */
	public Collection consultar(Connection con, String usuario, int paciente)
	{
		return pagosPacienteDao.consultaModificar(con, entidad, tipoMonto, documento,
									fecha, diagnostico, tipoCie, descripcion,
									valor, origen, usuario,
									institucion, tipoRegimen, paciente);
	}
	
	/**
	 * Metodo para cargar el pago de un paciente
	 * @param con
	 * @return
	 */
	public boolean cargar(Connection con)
	{
		Collection cargar=pagosPacienteDao.consultarPago(con, this.codigo);
		if(cargar.isEmpty())
		{
			return false;
		}
		else
		{
			Iterator iterador=cargar.iterator();
			for(int i=0; i<cargar.size(); i++)
			{
				HashMap fila=(HashMap)iterador.next();
				entidad=fila.get("entidad")+"";
				tipoMonto=Integer.parseInt(fila.get("tipomonto")+"");
				documento=fila.get("documento")+"";
				fecha=UtilidadFecha.conversionFormatoFechaAAp(fila.get("fecha")+"");
				diagnostico=String.valueOf(fila.get("diagnostico"));
				if(diagnostico==null || diagnostico.equals("null"))
				{
					diagnostico="";
				}
				tipoCie=Integer.parseInt(fila.get("tipocie")+"");
				descripcion=fila.get("descripcion")+"";
				valor=Double.parseDouble(fila.get("valor")+"");
				origen=Integer.parseInt(fila.get("origen")+"");
				institucion=Integer.parseInt(fila.get("institucion")+"");
				tipoRegimen=fila.get("tiporegimen")+"";
			}
			return true;
		}
	}
	
	/**
	 * @return Returna el codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}
	
	/**
	 * @param Asigna el codigo.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Modificar un pago de un paciente
	 * @param con
	 * @return
	 */
	public boolean modificar(Connection con)
	{
		return pagosPacienteDao.modificar(con, codigo, entidad, tipoMonto, documento,
								fecha, diagnostico, tipoCie, descripcion,
								valor, institucion, tipoRegimen);
	}

	/**
	 * Eliminar un pago específico
	 * @param con
	 * @return
	 */
	public boolean borrar(Connection con)
	{
		return pagosPacienteDao.borrar(con, codigo);
	}
	
	/**
	 * Método para consultar los pagos acumulados de los pacientes
	 * @param con
	 * @return
	 */
	public Collection consultarAcumulado(Connection con, int codigoPaciente)
	{
		return pagosPacienteDao.consultarPagosAcumulados(con, codigoPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param diagnostico
	 * @param tipoCie
	 * @param tipoMonto
	 * @param tipoRegimen
	 */
	public void cargarAcumuladosXDx(Connection con, int codigoPaciente, String diagnostico, int tipoCie, int tipoMonto, String tipoRegimen)
	{
		this.pagosAcumuladosXDiagnostico=pagosPacienteDao.consultarPagoAcumuladoXDiagnostico(con, codigoPaciente, diagnostico, tipoCie, tipoMonto, tipoRegimen);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param tipoMonto
	 * @param tipoRegimen
	 */
	public void cargarAcumuladosTotalAnio(Connection con, int codigoPaciente, int tipoMonto, String tipoRegimen)
	{
		this.pagosAcumuladosTotalAnio=pagosPacienteDao.consultarPagoAcumuladoAnioActual(con, codigoPaciente, tipoMonto, tipoRegimen);
	}
	
	
	/**
	 * Consultar el detalle de los pagos por año
	 * @param con
	 * @param anio
	 * @param codigoPaciente
	 * @return
	 */
	public Collection detalle(Connection con, String anio, int codigoPaciente)
	{
		return pagosPacienteDao.detalle(con, tipoMonto, anio, diagnostico, codigoPaciente, tipoCie, tipoRegimen);
	}
	
	/**
	 * Método que actualiza el diagnóstico de pagos paciente, si cuando se va ha realizar la atención de la cita, la cuenta
	 * del paciente ya se encuentra facturada y el origen del pago es interno
	 * @param con
	 * @param numeroSolicitud
	 * @param diagnostico
	 * @param tipoCie
	 * @param estado
	 * @return
	 */
	
	public boolean actualizarDiagnosticoCuentaFacturada(Connection con, int numeroSolicitud, String diagnostico, int tipoCie, String estado) throws SQLException
	{
		 boolean exito=false;
		    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		    if (estado.equals(ConstantesBD.inicioTransaccion))
		    {
		        if (!myFactory.beginTransaction(con))
		        {
		            myFactory.abortTransaction(con);
		        }
		    }
		    try
		    {
		    	exito=pagosPacienteDao.actualizarDiagnosticoCuentaFacturada(con, numeroSolicitud, diagnostico, tipoCie);
		        if (!exito)
		        {
		            myFactory.abortTransaction(con);
		        }
		    }
		    catch (SQLException e)
		    {
		        myFactory.abortTransaction(con);
		        throw e;
		    }
		    if (estado.equals(ConstantesBD.finTransaccion))
		    {
		        myFactory.endTransaction(con);
		    }
		    return exito;
	}
	
	
	/**
	 * @return Retorna pagosAcumuladosTotalAnio.
	 */
	public double getPagosAcumuladosTotalAnio()
	{
		return pagosAcumuladosTotalAnio;
	}
	/**
	 * @param pagosAcumuladosTotalAnio Asigna pagosAcumuladosTotalAnio.
	 */
	public void setPagosAcumuladosTotalAnio(double pagosAcumuladosTotalAnio)
	{
		this.pagosAcumuladosTotalAnio = pagosAcumuladosTotalAnio;
	}
	/**
	 * @return Retorna pagosAcumuladosXDiagnóstico.
	 */
	public double getPagosAcumuladosXDiagnostico()
	{
		return pagosAcumuladosXDiagnostico;
	}
	/**
	 * @param pagosAcumuladosXDiagnóstico Asigna pagosAcumuladosXDiagnóstico.
	 */
	public void setPagosAcumuladosXDiagnostico(
			double pagosAcumuladosXDiagnostico)
	{
		this.pagosAcumuladosXDiagnostico = pagosAcumuladosXDiagnostico;
	}
	
	/**
	 * Método para borrar los pagos de los pacientes temporales insertados
	 * durante el proceso de facturación
	 * @param con
	 * @param numeroTransaccion
	 * @return numero de elemntos eliminados
	 */
	public int borrarPagosPacienteTempo(Connection con, int numeroTransaccion)
	{
		return pagosPacienteDao.borrarPagosPacienteTempo(con, numeroTransaccion);
	}

	/**
	 * @return Retorna tipoRegimen.
	 */
	public String getTipoRegimen()
	{
		return tipoRegimen;
	}
	/**
	 * @param tipoRegimen Asigna tipoRegimen.
	 */
	public void setTipoRegimen(String tipoRegimen)
	{
		this.tipoRegimen = tipoRegimen;
	}
}
