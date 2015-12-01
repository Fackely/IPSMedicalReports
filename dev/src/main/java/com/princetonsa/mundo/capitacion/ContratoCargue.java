package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.ContratoCargueDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseContratoCargueDao;
import com.princetonsa.mundo.cargos.Contrato;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadCadena;
import util.UtilidadFecha;

public class ContratoCargue
{
	private int				codigo;

	private InfoDatosInt	contrato;

	private String			fechaCargue;

	private String			fechaInicial;

	private String			fechaFinal;

	private int				totalPacientes;

	private double			valorTotal;

	private double			upc;

	private boolean			anulado;

	private boolean			enBD;
	
	public String 			codigoTipoPago;
	
	private HashMap		carguesGrupoEtareo;
	
	private int				numCarguesGrupoEtareo;
	
	private String 			fechaFinalModificada;
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private static Logger	logger	= Logger.getLogger(ContratoCargue.class);

	private static ContratoCargueDao contratoCargueDao;
    
    public ContratoCargue()
	{
		this.codigo=-1;
		this.contrato = new InfoDatosInt();
		this.fechaCargue="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.totalPacientes=-1;
		this.valorTotal=0;
		this.upc=-1;
		this.anulado=false;
		this.enBD=false;
		this.codigoTipoPago="";
		this.carguesGrupoEtareo = new HashMap();
		this.numCarguesGrupoEtareo = 0;
		this.fechaFinalModificada="";
	}

    private static ContratoCargueDao getContratoCargueDao()
    {
        if(contratoCargueDao==null)
        {
            String tipoBD = System.getProperty("TIPOBD" );
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            contratoCargueDao=myFactory.getContratoCargueDao();
        }
        return contratoCargueDao;
    }

    
    /**
     * metodo static para insertar el contrato cargue
     * @param con
     * @param codigoContrato
     * @param fechaInicialFormatoApp
     * @param fechaFinalFormatoApp
     * @param totalPaciente
     * @param valorTotal
     * @param valorUpc
     * @param cuentaCobro, inserta null si es ""
     */
    public static void insertarContratoCargue(Connection con, int codigoContrato, String fechaInicialFormatoApp, String fechaFinalFormatoApp, int totalPaciente, double valorTotal, double valorUpc, String cuentaCobro, int codigoInstitucion,String fechaFinalModificada) 
	{
    	try 
    	{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD" )).getContratoCargueDao().insertarContratoCargue( con, codigoContrato, UtilidadFecha.getFechaActual(), fechaInicialFormatoApp, fechaFinalFormatoApp, totalPaciente, valorTotal, valorUpc, cuentaCobro, codigoInstitucion,fechaFinalModificada );
		} 
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
	}

    
	/**
	 * @return Returns the anulado.
	 */
	public boolean isAnulado()
	{
		return anulado;
	}

	/**
	 * @param anulado The anulado to set.
	 */
	public void setAnulado(boolean anulado)
	{
		this.anulado = anulado;
	}

	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Returns the contrato.
	 */
	public InfoDatosInt getContrato()
	{
		return contrato;
	}

	/**
	 * @param contrato The contrato to set.
	 */
	public void setContrato(InfoDatosInt contrato)
	{
		this.contrato = contrato;
	}

	/**
	 * @return Returns the enBD.
	 */
	public boolean isEnBD()
	{
		return enBD;
	}

	/**
	 * @param enBD The enBD to set.
	 */
	public void setEnBD(boolean enBD)
	{
		this.enBD = enBD;
	}

	/**
	 * @return Returns the codigoTipoPago.
	 */
	public String getCodigoTipoPago()
	{
		return codigoTipoPago;
	}

	/**
	 * @param codigoTipoPago The codigoTipoPago to set.
	 */
	public void setCodigoTipoPago(String codigoTipoPago)
	{
		this.codigoTipoPago = codigoTipoPago;
	}

	/**
	 * @return Returns the fechaCargue.
	 */
	public String getFechaCargue()
	{
		return fechaCargue;
	}

	/**
	 * @param fechaCargue The fechaCargue to set.
	 */
	public void setFechaCargue(String fechaCargue)
	{
		this.fechaCargue = fechaCargue;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Returns the totalPacientes.
	 */
	public int getTotalPacientes()
	{
		return totalPacientes;
	}

	/**
	 * @param totalPacientes The totalPacientes to set.
	 */
	public void setTotalPacientes(int totalPacientes)
	{
		this.totalPacientes = totalPacientes;
	}

	/**
	 * @return Returns the upc.
	 */
	public double getUpc()
	{
		return upc;
	}

	/**
	 * @param upc The upc to set.
	 */
	public void setUpc(double upc)
	{
		this.upc = upc;
	}

	/**
	 * @return Returns the valorTotal.
	 */
	public double getValorTotal()
	{
		return valorTotal;
	}

	/**
	 * @return Returns the carguesGrupoEtareo.
	 */
	public HashMap getCarguesGrupoEtareo()
	{
		return carguesGrupoEtareo;
	}

	/**
	 * @param carguesGrupoEtareo The carguesGrupoEtareo to set.
	 */
	public void setCarguesGrupoEtareo(HashMap carguesGrupoEtareo)
	{
		this.carguesGrupoEtareo = carguesGrupoEtareo;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setCargueGrupoEtareo(String key, Object value)
	{
		this.carguesGrupoEtareo.put(key, value);
	}
	
	public Object getCargueGrupoEtareo(String key)
	{
		return this.carguesGrupoEtareo.get(key);
	}

    /**
	 * @return Returns the numCarguesGrupoEtareo.
	 */
	public int getNumCarguesGrupoEtareo()
	{
		return numCarguesGrupoEtareo;
	}

	/**
	 * @param numCarguesGrupoEtareo The numCarguesGrupoEtareo to set.
	 */
	public void setNumCarguesGrupoEtareo(int numCarguesGrupoEtareo)
	{
		this.numCarguesGrupoEtareo = numCarguesGrupoEtareo;
	}

	private void insertar(Connection con, int codigoInstitucion) throws SQLException
	{
    	this.codigo = ContratoCargue.getContratoCargueDao().insertarContratoCargue(
    			con, this.getContrato().getCodigo(), this.getFechaCargue(), this.getFechaInicial(), this.getFechaFinal(), 
    			this.getTotalPacientes(), this.getValorTotal(), this.getUpc(), "", codigoInstitucion,this.fechaFinalModificada);
    	
    	if(this.getCodigoTipoPago().equals(ConstantesBD.codigoTipoPagoGrupoEtareo)) // si es tipo de pago grupo etareo
    	{
    		for(int i=0; i<this.getNumCarguesGrupoEtareo(); i++)
    		{
				String eliminado = (String)this.getCargueGrupoEtareo("eliminado_"+i);

				if(!UtilidadCadena.noEsVacio(eliminado) || !eliminado.equals("true")) // si el registro no ha sido eliminado
				{
					int totalUsuarios = Integer.parseInt(""+this.getCargueGrupoEtareo("totalusuarios_"+i));
					double upc = Double.parseDouble(""+this.getCargueGrupoEtareo("upc_"+i));
					int codigoGrupoEtareo = Integer.parseInt(""+this.getCargueGrupoEtareo("grupoetareo_"+i));
					ContratoCargue.getContratoCargueDao().insertarCargueGrupoEtareo(con, this.getCodigo(), codigoGrupoEtareo, totalUsuarios, upc, totalUsuarios*upc);
				}
    		}
    	}
	}

    private void modificar(Connection con) throws SQLException
	{
       	ContratoCargue.getContratoCargueDao().modificarContratoCargue(con, this.getCodigo(), this.getTotalPacientes(), this.getUpc(), this.getValorTotal(),this.fechaFinalModificada);

    	if(this.getCodigoTipoPago().equals(ConstantesBD.codigoTipoPagoGrupoEtareo)) // si es tipo de pago grupo etareo
    	{
    		for(int i=0; i<this.getNumCarguesGrupoEtareo(); i++)
    		{

				String enbd = (String)this.getCargueGrupoEtareo("enbd_"+i);

				String eliminado = (String)this.getCargueGrupoEtareo("eliminado_"+i);

				if(UtilidadCadena.noEsVacio(enbd) && enbd.equals("false")) // si el registro no está en la bd
				{

					if(!UtilidadCadena.noEsVacio(eliminado) || !eliminado.equals("true")) // si el registro no ha sido eliminado
					{
						int totalUsuarios = Integer.parseInt(""+this.getCargueGrupoEtareo("totalusuarios_"+i));
						double upc = Double.parseDouble(""+this.getCargueGrupoEtareo("upc_"+i));
						int codigoGrupoEtareo = Integer.parseInt(""+this.getCargueGrupoEtareo("grupoetareo_"+i));
						ContratoCargue.getContratoCargueDao().insertarCargueGrupoEtareo(con, this.getCodigo(), codigoGrupoEtareo, totalUsuarios, upc, totalUsuarios*upc);
					}
						
				}
				else
				{
					int consecutivo = Integer.parseInt(""+this.getCargueGrupoEtareo("consecutivo_"+i));
					if(!UtilidadCadena.noEsVacio(eliminado) || !eliminado.equals("true")) // si el registro no ha sido eliminado
					{
						int totalUsuarios = Integer.parseInt(""+this.getCargueGrupoEtareo("totalusuarios_"+i));
						double upc = Double.parseDouble(""+this.getCargueGrupoEtareo("upc_"+i));
						ContratoCargue.getContratoCargueDao().actualizarCargueGrupoEtareo(con, consecutivo, totalUsuarios, upc, totalUsuarios*upc);
					}
					else // si el registro ha sido eliminado
					{
						ContratoCargue.getContratoCargueDao().eliminarCargueGrupoEtareo(con, consecutivo);
					}
				}
    		}
    	}
	}
    
    private void calcularValorTotalCargue()
    {
    	valorTotal=0;
    	
    	if(this.getCodigoTipoPago().equals(ConstantesBD.codigoTipoPagoUpc)) // si es tipo de pago capitado
    	{
    		this.valorTotal=this.getUpc()*this.getTotalPacientes();
    	}
    	else if(this.getCodigoTipoPago().equals(ConstantesBD.codigoTipoPagoGrupoEtareo)) // si es tipo de pago grupo etareo
    	{
    		for(int i=0; i<this.getNumCarguesGrupoEtareo(); i++)
    		{
				String eliminado = (String)this.getCargueGrupoEtareo("eliminado_"+i);

				if(!UtilidadCadena.noEsVacio(eliminado) || !eliminado.equals("true")) // si el registro no ha sido eliminado
				{
	    			this.valorTotal+=Integer.parseInt(""+this.getCargueGrupoEtareo("totalusuarios_"+i))*Double.parseDouble(""+this.getCargueGrupoEtareo("upc_"+i));
				}
    		}
    	}
    }
    
    public void guardar(Connection con, int codigoInstitucion) throws SQLException
    {
    	try
    	{
        	this.calcularValorTotalCargue();
	    	if(this.isEnBD())
	    	{
	    		this.modificar(con);
	    		if(this.anulado) // si en el mundo esta anulado
	    		{
	    			 if(!ContratoCargue.getContratoCargueDao().estaAnuladoContratoCargue(con, this.getCodigo())) // y en la bd aun no
	    			 { // entonces hay que anularlo
	    				 ContratoCargue.getContratoCargueDao().anularContratoCargue(con, this.getCodigo());
	    			 }
	    		}
	    	}
	    	else
	    	{
	    		this.insertar(con, codigoInstitucion);
	    	}
    	}
    	catch(SQLException se)
    	{
        	logger.warn("Error guardando contrato cargue: "+se.getMessage());
        	throw se;
    	}
    }

	public String getFechaFinalModificada() {
		return fechaFinalModificada;
	}

	public void setFechaFinalModificada(String fechaFinalModificada) {
		this.fechaFinalModificada = fechaFinalModificada;
	}
}
